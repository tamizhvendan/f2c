(ns f2c.community.order.repository
  (:require [clojure.set :as set]
            [f2c.infra.db :as db]
            [honeyeql.core :as heql]
            [next.jdbc.sql :as sql]
            [clojure.data.json :as json]))


(defn create-order [community-id order-name]
  (heql/insert! db/adapter
                #:community.order{:name order-name
                                  :community-id community-id}))

(defn fetch-orders [community-id]
  (heql/query db/adapter
              {[[:community.order/community-id community-id] {:order-by [[:community.order/created-at :desc]]}]
               [:community.order/id
                :community.order/name
                :community.order/state
                :community.order/community-id]}))

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

(def ^:private community-order-aggregated-by-item-query
  "
WITH co_aggregated_by_item AS ( 
  SELECT 
    i.name, ioi.unit, SUM(ioi.quantity) quantity
  FROM community.\"order\" co 
  INNER JOIN individual.\"order\" io ON io.community_order_id = co.id
  INNER JOIN individual.order_item ioi ON ioi.individual_order_id = io.id
  INNER JOIN item i ON i.id = ioi.item_id
  WHERE co.id = ?
  GROUP BY i.name, ioi.unit)
SELECT name, JSON_AGG(JSON_BUILD_OBJECT('item.order/unit', unit, 'item.order/quantity', quantity)) orders 
FROM co_aggregated_by_item
GROUP BY name;    
  ")

(defn fetch-order-aggregated-by-items [community-order-id]
  (->> (sql/query db/datasource
                  [community-order-aggregated-by-item-query
                   community-order-id])
       (map (fn [item-wise-order]
              #:item {:name (str (:item/name item-wise-order))
                      :orders (json/read-str (str (:orders item-wise-order)) :key-fn keyword)}))))

(comment
  (fetch-order-aggregated-by-items #uuid "36f36ab0-3e7c-418d-a8fd-d76a54d08809")
  (eligible-for-individual-order?
   #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
   #uuid "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470"
   #uuid "fed35df4-94f4-426f-a0d3-3781da639e5a")
  (fetch-order "fed35df4-94f4-426f-a0d3-3781da639e5a")
  (fetch-eligible-for-individual-orders #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                                        #uuid "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470")
  (create-order #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                "Test Order"))