(ns f2c.community.order.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]))


(defn create-order [community-id order-name]
  (heql/insert! db/adapter
                #:community.order{:name order-name
                                  :community-id community-id}))

(defn fetch-orders [community-id]
  (heql/query db/adapter
              {[[:community.order/community-id community-id] {:order-by [[:community.order/created-at :desc]]}]
               [:community.order/id
                :community.order/name
                :community.order/state]}))

(defn fetch-order [community-order-id]
  (heql/query-single db/adapter
                     {[:community.order/id community-order-id]
                      [:community.order/id
                       :community.order/name
                       :community.order/state
                       {:community.order/community
                        [:community/id
                         :community/name]}]}))

(comment
  (fetch-order "fed35df4-94f4-426f-a0d3-3781da639e5a")
  (fetch-orders #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd")
  (create-order #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                "Test Order"))