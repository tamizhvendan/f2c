(ns f2c.community.order.repository
  (:require [clojure.set :as set]
            [f2c.infra.db :as db]
            [honeyeql.core :as heql]
            [next.jdbc.sql :as sql]))


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

(defn fetch-eligible-for-individual-orders [community-id individual-id]
  (map
   (fn [order]
     (set/rename-keys order {:order/id :community.order/id
                             :order/name :community.order/name}))
   (sql/query db/datasource
              ["SELECT co.id, co.name
               FROM community.\"order\" co
               LEFT OUTER JOIN individual.\"order\" io ON io.community_order_id = co.id AND io.individual_id = ?
               WHERE co.community_id = ? AND io.id IS NULL
               ORDER BY co.created_at DESC"
               individual-id community-id])))

(defn eligible-for-individual-order? [community-id individual-id community-order-id]
  (-> (sql/query db/datasource
                 ["SELECT 1
                   FROM community.\"order\" co
                   LEFT OUTER JOIN individual.\"order\" io ON io.community_order_id = co.id AND io.individual_id = ?
                   WHERE co.community_id = ? AND co.id = ? AND io.id IS NULL
                   ORDER BY co.created_at DESC"
                  individual-id community-id community-order-id])
      seq
      boolean))

(comment
  (eligible-for-individual-order?
   #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
   #uuid "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470"
   #uuid "fed35df4-94f4-426f-a0d3-3781da639e5a")
  (fetch-order "fed35df4-94f4-426f-a0d3-3781da639e5a")
  (fetch-eligible-for-individual-orders #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                                        #uuid "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470")
  (create-order #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                "Test Order"))