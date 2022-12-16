(ns f2c.web.app.community.order.individual
  (:require [f2c.individual.order.repository :as individual-order-repo]
            [f2c.web.app.view.layout.individual.community :as layout]
            [ring.util.response :as response]
            [f2c.extension.reitit :as r]))

(defn- render-create-individual-order-view [req {:community.order/keys [id name community]}]
  [:div
   [:p (format "%s's order '%s' is now open. You can place your order now." (:community/name community) name)]
   [:form {:method "POST"
           :class "mt-2"
           :action (r/path req :route.community-order/create-individual-order
                           {:community-order-id id})}
    [:input {:class "text-white bg-primary-800 rounded px-3 py-2 cursor-pointer" :type "submit" :value "Create your order"}]]])

(defn view-current-individual-order-handler [req]
  (let [current-community-order-id (get-in req [:current-community-order :community.order/id])
        current-individual-id (get-in req [:current-individual :individual/id])]
    (if-let [{:individual.order/keys [id]} (individual-order-repo/fetch-order current-community-order-id current-individual-id)]
      (response/redirect (r/path req :route.individual-order/index
                                 {:individual-order-id id}))
      (render-create-individual-order-view req (:current-community-order req)))))

(defn create-individual-order [req]
  (let [current-community-order-id (get-in req [:current-community-order :community.order/id])
        current-individual-id (get-in req [:current-individual :individual/id])
        {:individual.order/keys [id]} (individual-order-repo/create-order current-community-order-id current-individual-id)]
    (response/redirect (r/path req :route.individual-order/index
                               {:individual-order-id id}))))