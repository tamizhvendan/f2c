(ns f2c.individual.order.repository
  (:require [honeyeql.core :as heql]
            [f2c.infra.db :as db]))

(defn fetch-order
  ([individual-order-id]
   (heql/query-single db/adapter
                      {[:individual.order/id individual-order-id]
                       [:individual.order/id
                        :individual.order/community-order-id
                        :individual.order/individual-id
                        {:individual.order/community-order
                         [:community.order/community-id
                          {:community.order/community
                           [:community/id
                            :community/name]}]}]}))
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

(defn fetch-orders [community-id individual-id]
  (heql/query db/adapter
              {[[:individual.order/individual-id individual-id]
                {:where [:= [:individual.order/community-order :community.order/community-id] community-id]
                 :order-by [[[:individual.order/community-order :community.order/created-at] :desc]]}]
               [:individual.order/id
                {:individual.order/community-order
                 [:community.order/name
                  :community.order/created-at
                  :community.order/state]}]}))

(comment
  (fetch-orders "74e06d97-cf9f-4133-b6e2-8f06c886f1cd" "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470")
  (fetch-order "e80e9644-e77d-4648-88fe-a79cf1b97f08")
  (fetch-order "fed35df4-94f4-426f-a0d3-3781da639e5a" "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470"))