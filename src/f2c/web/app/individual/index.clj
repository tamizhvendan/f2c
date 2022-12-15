(ns f2c.web.app.individual.index
  (:require [f2c.community :as community]
            [f2c.community.repository :as community-repo]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.default :as layout]))

(defn render-index [req]
  (let [{individual-id :individual/id individual-name :individual/name} (:current-individual req)
        communities (community-repo/fetch-communities individual-id)]
    (layout/render "Home"
                   [:div {:class "p-4 md:max-w-lg md:mx-auto"}
                    [:img {:class "h-28 w-28 mx-auto" :src "/assets/web/images/logo.png"}]
                    [:p {:class "font-display text-xl font-bold"} (format "Hi %s!" individual-name)]
                    [:ul {:classs "flex"}
                     (map (fn [{:community/keys [id name] :as community}]
                            (let [is-facilitator (community/is-facilitator community individual-id)]
                              [:li {:class "p-4 rounded shadow-md bg-white mt-4"}
                               [:p {:class "font-medium text-lg"} name]
                               [:div {:class "flex justify-end"}
                                (when is-facilitator
                                  [:a {:class "font-medium text-primary-900 hover:text-primary-700 no-underline inline-flex items-center text-sm"
                                       :href (r/path req :route.community/index {:community-id id})}
                                   [:span "Manage"]
                                   [:i {:class "ri-arrow-right-line ml-1"}]])]]))
                          communities)]])))

(defn handler [req]
  (render-index req))