(ns f2c.web.app.individual.order.index
  (:require [f2c.web.app.view.layout.individual :as layout]
            [f2c.community.item.repository :as community-item-repo]))

(defn- render-available-items [available-items]
  [:p "Order Items"
   [:ul {:class "mt-4"}
    (map (fn [{:item/keys [name]}]
           [:li name])
         available-items)]])

(defn render [req]
  (let [community-id (get-in req [:current-individual-order :individual.order/community-order :community.order/community-id])
        available-items (community-item-repo/fetch-available-items community-id)]
    (layout/render req
                   (if (seq available-items)
                     [(render-available-items available-items)]
                     [[:p "Sorry, No Available Items Found"]]))))

(defn handler [req]
  (render req))