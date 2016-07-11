(ns blocks.client.templates.cta
  (:require [blocks.client.template :refer [template]]
            [blocks.client.templates.partials.email-field :as email-field]))

  (def pad "3em")

  (defn styles [data]
    [:.cta
     {:padding pad
      :background (get-in data [:background :color])
      :text-align "center"}

     [:.content

      [:h1
       {:color (get-in data [:heading :color])
        :font-size "2.2em"
        :margin-bottom "0.5em"}]

      [:p
       {:color (get-in data [:description :color])}
       {:margin-bottom "2em"}]

      (email-field/styles data)]])

  (defn component [data]
    [:section.cta
      [:div.content
        [:div.text
          [:h1 (get-in data [:heading :text])]
          [:p  (get-in data [:description :text])]]
        [email-field/component data]]])

  (defmethod template "cta" [_]
    {:css styles
     :component component})
