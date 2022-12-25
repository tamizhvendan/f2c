(ns f2c.web.app.individual.index
  (:require [f2c.community :as community]
            [f2c.community.repository :as community-repo]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.base :as layout]))

(defn render-index [req]
  (let [{individual-id :individual/id individual-name :individual/name} (:current-individual req)
        communities (community-repo/fetch-communities individual-id)]
    (layout/render "Home"
                   [:div {:class "p-4 md:max-w-lg md:mx-auto"}
                    [:img {:class "h-28 w-28 mx-auto" :src "/assets/web/images/logo.png"}]
                    [:p {:class "heading font-display text-lg font-bold"} (format "Hi %s!" individual-name)]
                    [:ul {:classs "flex"}
                     (map (fn [{:community/keys [id name] :as community}]
                            (let [is-facilitator (community/is-facilitator community individual-id)]
                              [:li {:class "card mt-4"}
                               [:h2 {:class "heading text-primary-900"} name]
                               [:div {:class "flex justify-between mt-2 md:mt-4"}
                                [:a {:class "inline-flex card-link"
                                     :href (r/path req :route.individual.community.order/index {:community-id id})}
                                 [:span "Manage Orders"]
                                 [:i {:class "ri-arrow-right-line ml-1"}]]
                                (when is-facilitator
                                  [:a {:class "card-link"
                                       :href (r/path req :route.community/orders {:community-id id})}
                                   [:span "Manage Community"]
                                   [:i {:class "ri-arrow-right-line ml-1"}]])]]))
                          communities)]]
                   {:class "bg-primary-50"})))

(defn handler [req]
  (render-index req))