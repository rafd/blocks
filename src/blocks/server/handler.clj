(ns blocks.server.handler
  (:require
    [hiccup.core :refer [html]]
    [ring.middleware.resource :refer [wrap-resource]]
    [ring.middleware.not-modified :refer [wrap-not-modified]]
    [ring.middleware.content-type :refer [wrap-content-type]]))

(defn read-pages-edn []
  (read-string (slurp (clojure.java.io/resource "data/pages.edn"))))

(defn read-blocks-edn []
  (read-string (slurp (clojure.java.io/resource "data/blocks.edn"))))

(defn get-page-data [page]
  (let [blocks (read-blocks-edn)]
    {:page (-> page
               (update-in [:blocks]
                          (fn [bs]
                            (vec (map (fn [block-id]
                                        (blocks block-id)) bs)))))}))

(defn path->page [domain url]
  (let [pages (read-pages-edn)]
    (when-let [page (->> pages
                         (filter (fn [page]
                                   (and
                                     (= (page :url) url)
                                     (= (page :domain) domain))))
                         first)]
      page)))

(defn render-page [domain url]
  (when-let [page (path->page domain url)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (html
             [:html
              [:head
               [:meta {:charset "utf-8"}]
               [:meta {:http-equiv "x-ua-compatible" :content "ie=edge"}]
               [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
               [:meta {:name "generator" :content "blocks"}]
               [:style {:type "text/css"}
                (slurp (clojure.java.io/resource "public/css/reset.css"))]
               [:title (get-in page [:meta :title])]]
              [:body
               [:div {:id "app"}]
               [:script {:type "text/edn" :id "data"}
                (pr-str (get-page-data page))]
               [:script {:src "/js/blocks.js" :type "text/javascript"}]
               [:script {:type "text/javascript"}
                "blocks.client.core.run()"]]])}))

(defn index-page []
 (let [pages (read-pages-edn)]
   {:status 200
    :headers {"Content-Type" "text/html"}
    :body (html
            [:html
             [:body
              [:ul
               (for [page pages]
                 [:li
                  [:a {:href (str "/" (page :domain) (page :url))}
                  (str (page :domain) (page :url))]])]]])}))

(defn page-handler [request]
  (let [{:keys [url domain]} (if (= "localhost" (request :server-name))
                               (let [[_ domain url] (re-find #"/(.+?)/(.*)?" (request :uri))]
                                 {:domain domain
                                  :url (str "/" url)})
                               {:domain (request :server-name)
                                :url (request :uri)})]
    (if (nil? domain)
      (index-page)
      (if-let [page (render-page domain url)]
        page
        {:status 404
         :body "404; Thank you visitor! But our page is in another castle!"}))))

(def app
  (-> page-handler
      (wrap-resource "public")
      (wrap-content-type)
      (wrap-not-modified)))
