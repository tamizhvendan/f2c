(ns f2c.web.app.view.layout.default
  (:require [f2c.extension.ring-response :as rr]
            [f2c.extension.rum :as rum]))

(defn render
  ([title body]
   (render title body {}))
  ([title body body-attrs]
   (rr/html
    [:html
     [:head
      [:title title]
      [:meta {:name "viewport" :content "width=device-width"}]
      [:link {:rel "stylesheet" :href "https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css"}]
      [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Figtree:wght@300;400;500;600&display=swap"}]
      [:link {:rel "stylesheet" :type "text/css" :href "/assets/web/site.css"}]]
     (rum/element :body (merge {:class "font-body text-base"} body-attrs)
                  body
                  [:script {:src "//unpkg.com/alpinejs" :defer true}]
                  [:script {:src "https://unpkg.com/flowbite@1.5.5/dist/flowbite.js"}])])))