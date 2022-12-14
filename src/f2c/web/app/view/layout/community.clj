(ns f2c.web.app.view.layout.community
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.app :as layout]
            [f2c.extension.rum :as rum]))

(defn- render-tab-item [label url is-active]
  (let [tab-item-classes "whitespace-nowrap p-3 border-b-2 font-medium text-sm"]
    (if is-active
      [:span {:class (str tab-item-classes " border-primary-700 text-primary-900")} label]
      [:a {:class (str tab-item-classes " border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300")
           :href url} label])))

(defn- render-tabs [req current-page-name]
  (let [{:community/keys [id name]} (:current-community req)]
    [:div {:class "border-b border-gray-200 space-y-1 mb-4"}
     [:div {:class "flex items-center"}
      [:i {:class "text-gray-600 ri-community-line text-2xl mr-1.5"}]
      [:h2 {:class "font-display font-semibold text-lg text-gray-600"} name]]
     [:nav {:class "-mb-px flex space-x-4" :aria-label "Tabs"}
      (render-tab-item "Orders"
                       (r/path req :route.community/orders {:community-id id})
                       (= :orders current-page-name))
      (render-tab-item "Catalog"
                       (r/path req :route.community.catalog/index {:community-id id})
                       (= :catalog current-page-name))]]))

(defn- render-body [req body current-page-name]
  [(render-tabs req current-page-name)
   (rum/element :main {:class ""} body)])

(defn render
  ([req body]
   (render req body nil))
  ([req body current-page-name]
   (layout/render req
                  (str (get-in req [:current-community :community/name]) " - Community")
                  (render-body req body current-page-name))))