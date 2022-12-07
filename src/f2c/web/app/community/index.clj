(ns f2c.web.app.community.index
  (:require [f2c.community.repository :as community-repo]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.default :as layout]
            [f2c.community.order.repository :as community-order-repo]))

(defn handler [req]
  (let [community-id (get-in req [:parameters :path :community-id])
        {:community/keys [name]} (community-repo/fetch-community community-id)
        orders (community-order-repo/fetch-orders community-id)]
    (layout/render (str name " - Community")
                   [:main
                    [:div
                     [:p name]
                     [:a {:style {:margin-left "1rem"}
                          :href (r/path req :route.community/new-order {:community-id community-id})} "Create Order"]]
                    [:div
                     (if (seq orders)
                       [:p "Your Orders"
                        [:ul
                         (map (fn [{:community.order/keys [name state]}]
                                [:li (format "%s - %s" name state)]) orders)]]
                       [:p "Sorry, You don't have any orders"])]])))