(ns f2c.community.order.repository
  (:require [f2c.infra.db :as db]
            [next.jdbc.sql :as sql]
            [honeyeql.core :as heql]))


(defn create-order [community-id order-name]
  (sql/insert! db/datasource
               :community.order
               {:name order-name
                :community_id community-id}))

(defn fetch-orders [community-id]
  (heql/query db/adapter
              {[[:community.order/community-id community-id] {:order-by [[:community.order/created-at :desc]]}]
               [:community.order/id
                :community.order/name
                :community.order/state]}))

(comment
  (fetch-orders #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd")
  (create-order #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                "Test Order"))