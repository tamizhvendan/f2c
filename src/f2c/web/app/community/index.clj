(ns f2c.web.app.community.index
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.community :as layout]
            [f2c.community.order.repository :as community-order-repo]))

(defn render-state [state]
  (case state
    "open" [:span {:class "badge ~positive @low text-xs"} state]))

(defn- render-no-orders [req community-id]
  [:div
   [:p "You are yet to create a community order."]
   [:a {:class "button mt-2 ~neutral @low text-sm"
        :href (r/path req :route.community/new-order {:community-id community-id})} "Create your first order"]])

(defn- orders-section [req community-id]
  (let [orders (community-order-repo/fetch-orders community-id)]
    [:div
     [:div {:class "flex items-center justify-between"}
      [:h3 {:class "section-heading mb-1"} "Community Orders"]
      (when (seq orders)
        [:p
         [:a {:class "button ~neutral @low text-sm"
              :href (r/path req :route.community/new-order {:community-id community-id})} "Create New Order"]])]
     (if (seq orders)
       [:div
        [:table {:class "table mt-2 md:mt-4"}
         [:thead
          [:tr {:class "font-display opacity-70 text-sm"}
           [:th "Name"]
           [:th "Status"]]]
         [:tbody
          (map (fn [{:community.order/keys [name state]}]
                 [:tr
                  [:td {:class "text-sm"}  name]
                  [:td (render-state state)]]) orders)]]]
       (render-no-orders req community-id))]))

(defn handler [req]
  (let [{:community/keys [id]} (:current-community req)]
    (layout/render req (orders-section req id) :home)))