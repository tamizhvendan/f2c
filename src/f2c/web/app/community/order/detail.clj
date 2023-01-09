(ns f2c.web.app.community.order.detail
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.components.app :as app-component]
            [f2c.web.app.view.layout.community :as layout]))

(defn- body [req]
  (let [community-id (get-in req [:current-community :community/id])
        community-order (:current-community-order req)]
    [:div
     [:div {:class "flex"}
      [:a {:class "flex hover:text-primary-700"
           :href (r/path req :route.community/orders {:community-id community-id})}
       [:i {:class "ri-arrow-left-line mr-1 font-bold text-lg"}]
       [:h3 {:class "page-heading mr-2"} (:community.order/name community-order)]]
      (app-component/community-order-state (:community.order/state community-order))]]))

(defn handler [req]
  (layout/render req (body req) :orders))