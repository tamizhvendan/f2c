(ns f2c.web.app.view.layout.individual.community
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.default :as layout]))

(defn render [req children]
  (let [{community-name :community/name} (:current-community req)
        {individual-name :individual/name} (:current-individual req)]
    (layout/render (str individual-name " - " community-name)
                   (vec (concat [:main {:class "p-4 md:max-w-lg md:mx-auto"}
                                 [:div {:class "flex items-center justify-between"}
                                  [:a {:href (r/path req :route.individual/index) :class "flex-1"}
                                   [:img {:class "h-16 w-16 -ml-2" :src "/assets/web/images/logo.png"}]]]
                                 ; invidual name
                                 [:p {:class "mt-2 font-medium font-display"} (format "Hi %s!" individual-name)]
                                 ; spacing
                                 [:div {:class "mt-4"}]]
                                children)))))