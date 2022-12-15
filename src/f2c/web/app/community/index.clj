(ns f2c.web.app.community.index
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.community :as layout]
            [f2c.community.order.repository :as community-order-repo]))

(defn render-state [state]
  (case state
    "open" [:span {:class "text-xs bg-primary-600 text-white ml-2 rounded px-1 py-0.5"} state]))

(defn- orders-section [req community-id]
  (let [orders (community-order-repo/fetch-orders community-id)]
    [:div
     (if (seq orders)
       [:div
        [:p {:class "flex justify-between items-center"}
         [:span {:class "font-bold font-display text-xl"} "Orders"]
         [:a {:class "no-underline border border-primary-800 hover:text-white hover:bg-primary-800 rounded px-3 py-2 text-sm"
              :href (r/path req :route.community/new-order {:community-id community-id})} "Create Order"]]
        [:ul {:class "mt-4 space-y-4"}
         (map (fn [{:community.order/keys [name state]}]
                [:li {:class "flex items-center"}
                 [:span name]
                 (render-state state)]) orders)]]
       [:p "Sorry, You don't have any orders"])]))

(defn handler [req]
  (let [{:community/keys [id]} (:current-community req)]
    (layout/render req [(orders-section req id)] :home)))