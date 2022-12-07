(ns f2c.community.order.repository
  (:require [f2c.infra.db :as db]
            [next.jdbc.sql :as sql]))


(defn create-order [community-id order-name]
  (sql/insert! db/datasource
               :community.order
               {:name order-name
                :community_id community-id}))

(comment
  (create-order #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                "Test Order"))