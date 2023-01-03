(ns f2c.web.app.individual.order.item.handlers
  (:require [f2c.schema :as schema]))


(def create-request
  [:map
   [:individual.order-item/individual-order-id uuid?]
   [:individual.order-item/item-id uuid?]
   [:individual.order-item/quantity schema/positive-decimal]
   [:individual.order-item/unit schema/unit-of-measure]])

(defn create-order-item [req]
  ; TODO wireup the handler using the above create-request
  )