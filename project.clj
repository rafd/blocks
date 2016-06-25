(defproject blocks "0.0.1"
  :dependencies [[org.clojure/clojure "1.8.0"]

                 ; server-side
                 [http-kit "2.1.19"]
                 [ring/ring-devel "1.4.0"]
                 [hiccup "1.0.5"]

                 ; client-side
                 [org.clojure/clojurescript "1.9.89"]
                 [re-frame "0.7.0"]
                 [garden "1.3.2"]]

  :source-paths ["src"]

  :main blocks.server.core

  :plugins [[lein-figwheel "0.5.4-4"]]

  :figwheel {:server-port 3434}

  :cljsbuild {:builds
              [{:id "dev"
                :figwheel {:on-jsload "blocks.client.core/reload"}
                :source-paths ["src/blocks/client"]
                :compiler {:main blocks.client.core
                           :asset-path "/js/dev"
                           :output-to "resources/public/js/dev.js"
                           :output-dir "resources/public/js/dev"
                           :verbose true}}]})
