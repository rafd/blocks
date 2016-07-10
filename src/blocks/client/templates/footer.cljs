(ns blocks.client.templates.footer
  (:require [blocks.client.template :refer [template]]))

(def pad "3rem")

(defn styles [data]
  [:.footer
   {:padding pad
    :background (data :background-color)}

   [:a.logo
    {:display "inline-block"
     :color (data :text-color)}

    [:img
     {:height "2rem"
      :display "inline-block"
      :vertical-align "middle"
      :margin-right "0.5em" }]

    [:h1
     {:display "inline-block"
      :font-size "1.25em"
      :line-height "2rem"
      :vertical-align "middle" }]

    [:.version
     {:display "inline-block"
      :opacity "0.5"
      :font-size "0.75em"
      :line-height "2rem"
      :vertical-align "sub"
      :margin-left "0.5em"
      :text-transform "uppercase"
      :letter-spacing "0.1em"}]]

   [:.menu
    {:display "inline-block"
     :position "absolute"
     :right 0}

    [:a
     {:color (data :text-color)
      :opacity 0.6
      :text-decoration "none"
      :margin-left "2em"
      :transition "opacity 0.1s ease-in-out"
      :line-height "2em"}

     [:&:hover
      {:opacity 1}]

     ["&[data-icon]:before"
      {:content "attr(data-icon)"
       :font-family "FontAwesome"
       :margin-right "0.25em"}]

     [:&.button
      {:display "inline-block"
       :border [["1px" "solid" (data :text-color)]]
       :border-radius "0.25em"
       :height "2em"
       :padding "0 0.5em" }]]]])

(defn view [data]
  [:section.footer
   [:div.content
    [:a.logo {:href "/"}
     [:img {:src (data :logo-url)}]
     [:h1 (data :title)]]
    [:div.menu
     (for [link (data :menu)]
       [:a {:href (link :url)
            :class (link :style)
            :data-icon (link :icon)}
        (link :text)])]]])

(defmethod template "footer" [_]
  {:css styles
   :component view})
