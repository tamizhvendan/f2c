(ns f2c.web.app.individual.order.item.handlers
  (:require [f2c.extension.ring-response :as rr]
            [f2c.individual.order.repository :as individual-order-repo]
            [f2c.schema :as schema]))

(def create-request
  [:map
   [:individual.order-item/item-id uuid?]
   [:individual.order-item/quantity schema/positive-decimal]
   [:individual.order-item/unit schema/unit-of-measure]])

(defn create-order-item [req]
  (let [individual-order-item (assoc (get-in req [:parameters :form])
                                     :individual.order-item/individual-order-id
                                     (get-in req [:parameters :path :individual-order-id]))]
    (individual-order-repo/create-order-item individual-order-item)
    (rr/json {})))