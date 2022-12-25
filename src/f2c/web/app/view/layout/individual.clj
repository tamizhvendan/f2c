(ns f2c.web.app.view.layout.individual
  (:require [f2c.extension.rum :as rum]
            [f2c.web.app.view.layout.app :as layout]))

(defn- render-body [req body]
  [[:div {:class "border-b border-gray-200 space-y-1 mb-4 pb-2"}
    [:div {:class "flex items-center"}
     [:i {:class "text-gray-600 ri-community-line text-2xl mr-1.5"}]
     [:h2 {:class "font-display font-semibold text-lg text-gray-600"}
      (get-in req [:current-community :community/name])]]]
   (rum/element :main {:class ""} body)])

(defn render [req body]
  (let [{community-name :community/name} (:current-community req)
        {individual-name :individual/name} (:current-individual req)]
    (layout/render req
                   (str individual-name " - " community-name)
                   (render-body req body))))