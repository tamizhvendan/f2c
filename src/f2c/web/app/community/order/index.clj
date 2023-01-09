(ns f2c.web.app.community.order.index
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.community :as layout]
            [f2c.community.order.repository :as community-order-repo]
            [f2c.web.app.view.components.app :as app-component]))

(defn- render-no-orders [req community-id]
  [:div
   [:p "You are yet to create a community order."]
   [:a {:class "button mt-2 ~neutral @low text-sm"
        :href (r/path req :route.community/new-order {:community-id community-id})} "Create your first order"]])

(defn- render-orders [req orders]
  [:div
   [:table {:class "table mt-2 md:mt-4 max-w-lg"}
    [:thead
     [:tr {:class "font-display opacity-70 text-sm"}
      [:th "Name"]
      [:th "Status"]]]
    [:tbody
     (map (fn [{:community.order/keys [id name state community-id]}]
            [:tr
             [:td {:class "text-sm"} [:a {:class "mr-2 link-tertiary"
                                          :href (r/path req :route.community.order/detail {:community-id community-id
                                                                                           :community-order-id id})} name]]
             [:td (app-component/community-order-state state)]]) orders)]]])

(defn- orders-section [req community-id]
  (let [orders (community-order-repo/fetch-orders community-id)]
    [:div
     [:div {:class "flex items-center justify-between"}
      [:h3 {:class "page-heading mb-1"} "Community Orders"]
      (when (seq orders)
        [:p
         [:a {:class "button ~neutral @low text-sm"
              :href (r/path req :route.community/new-order {:community-id community-id})} "Create New Order"]])]
     (if (seq orders)
       (render-orders req orders)
       (render-no-orders req community-id))]))

(defn handler [req]
  (let [{:community/keys [id]} (:current-community req)]
    (layout/render req (orders-section req id) :orders)))