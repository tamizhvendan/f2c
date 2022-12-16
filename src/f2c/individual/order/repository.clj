(ns f2c.individual.order.repository
  (:require [honeyeql.core :as heql]
            [f2c.infra.db :as db]))

(defn fetch-order
  ([individual-order-id]
   (heql/query-single db/adapter
                      {[:individual.order/id individual-order-id]
                       [:individual.order/id
                        :individual.order/community-order-id
                        :individual.order/individual-id]}))
  ([community-order-id individual-id]
   (heql/query-single db/adapter
                      {[:individual.order/individual-id individual-id
                        :individual.order/community-order-id community-order-id]
                       [:individual.order/id
                        :individual.order/community-order-id
                        :individual.order/individual-id]})))

(defn create-order [community-order-id individual-id]
  (heql/insert! db/adapter
                {:individual.order/individual-id individual-id
                 :individual.order/community-order-id community-order-id}))

(comment
  (fetch-order "01bd38a6-a794-4fc7-9f4e-816a57791645")
  (fetch-order "fed35df4-94f4-426f-a0d3-3781da639e5a" "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470"))