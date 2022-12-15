(ns f2c.web.app.individual.community.index
  (:require [f2c.community.order.repository :as community-order-repo]
            [f2c.web.app.view.layout.individual.community :as layout]
            [f2c.extension.reitit :as r]))

(defn- render-current-community-orders [req]
  (let [{community-name :community/name
         community-id :community/id} (:current-community req)
        community-orders (community-order-repo/fetch-orders community-id)]
    [[:p {:class "font-bold font-display text-xl"} community-name]
     (if (seq community-orders)
       [:ul {:class "mt-4 space-y-4"}
        (map (fn [{:community.order/keys [id name]}]
               [:li {:class "flex items-center"}
                [:a {:href (r/path req :route.community-order/view-current-individual-order
                                   {:community-order-id id})} name]]) community-orders)]
       [:p "No community orders found"])]))

(defn handler [req]
  (layout/render req (render-current-community-orders req)))