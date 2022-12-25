(ns f2c.web.app.individual.community.order.index
  (:require [f2c.extension.reitit :as r]
            [f2c.individual.order.repository :as individual-order-repo]
            [f2c.web.app.view.components.app :as app-component]
            [f2c.web.app.view.layout.individual :as layout]))

(defn- render-no-individual-orders [req]
  [:div {:class "mt-"}
   [:p "You are yet to create an individual order."]
   [:a {:class "button mt-2 ~neutral @low text-sm"
        :href (r/path req :route.community/new-order {:community-id (get-in req [:current-community :community/id])})}
    "Create your first order"]])

(defn- render-individual-orders [req individual-orders]
  [:ul
   (map (fn [{:individual.order/keys [community-order]}]
          (let [{:community.order/keys [name state]} community-order]
            [:li {:class "text-sm flex"}
             [:a {:class "mr-2 link-tertiary"
                  :href "#"} name]
             (app-component/community-order-state state)])) individual-orders)])

(defn- render-body [req]
  [:div
   [:h3 {:class "page-heading mb-2"} "Your Orders"]
   (let [individual-orders (individual-order-repo/fetch-orders (get-in req [:current-community :community/id])
                                                               (get-in req [:current-individual :individual/id]))]
     (if (seq individual-orders)
       (render-individual-orders req individual-orders)
       (render-no-individual-orders req)))])

(defn handler [req]
  (layout/render req (render-body req)))
