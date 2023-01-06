(ns f2c.individual.order.repository
  (:require [clojure.data.json :as json]
            [f2c.infra.db :as db]
            [honeyeql.core :as heql]
            [next.jdbc.result-set :as rs]
            [next.jdbc.sql :as sql]))

(defn fetch-order [individual-order-id]
  (heql/query-single db/adapter
                     {[:individual.order/id individual-order-id]
                      [:individual.order/id
                       :individual.order/community-order-id
                       :individual.order/individual-id
                       {:individual.order/community-order
                        [:community.order/community-id
                         :community.order/name
                         :community.order/state
                         {:community.order/community
                          [:community/id
                           :community/name]}]}]}))

(defn create-order [community-order-id individual-id]
  (heql/insert! db/adapter
                {:individual.order/individual-id individual-id
                 :individual.order/community-order-id community-order-id}))

(defn create-order-item [individual-order-item]
  (heql/insert! db/adapter individual-order-item))

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

(def ^:private fetch-items-query
  "SELECT 
  i.id, 
  i.name, 
  i.supported_unit_of_measures,
  ioi.quantity, 
  ioi.unit,
  COALESCE(
    JSON_AGG(JSON_BUILD_OBJECT(
      'item.price/price', ip.price, 
      'item.price/currency', ip.currency, 
      'item.price/pricing-unit', ip.pricing_unit))
  FILTER (WHERE ip.price IS NOT NULL), '[]') AS item_prices
FROM individual.\"order\" io 
INNER JOIN community.\"order\" co ON co.id = io.community_order_id
INNER JOIN community.item_availability ia ON ia.community_id = co.community_id AND ia.is_available = TRUE
INNER JOIN item i ON i.id = ia.item_id
LEFT OUTER JOIN community.item_price AS ip ON ip.item_id = i.id AND upper(ip.valid_period) >= now () AT TIME ZONE 'UTC'
LEFT OUTER JOIN individual.order_item ioi ON io.id = ioi.individual_order_id AND ioi.item_id = i.id
WHERE io.id = ?
GROUP BY i.id, i.name, ia.is_available, ioi.quantity, ioi.unit
ORDER BY i.name")

(defn fetch-items [individual-order-id]
  (->> (sql/query db/datasource
                  [fetch-items-query individual-order-id]
                  {:builder-fn rs/as-kebab-maps})
       (map (fn [{:keys [item-prices]
                  :as item}]
              (dissoc (assoc item :item/prices (json/read-str (str item-prices) :key-fn keyword)
                             :item/supported-unit-of-measures (.getArray (:item/supported-unit-of-measures item))
                             :item.ordered/quantity (:order-item/quantity item)
                             :item.ordered/unit (:order-item/unit item))
                      :item-prices
                      :order-item/quantity
                      :order-item/unit)))))

(comment
  (fetch-items #uuid "8248128e-a137-48a9-9913-c2f4fe3e4198")
  (create-order-item  #:individual.order-item{:individual-order-id "8248128e-a137-48a9-9913-c2f4fe3e4198"
                                              :item-id "bd6f86ab-b266-4757-9d3f-12dd286c4433"
                                              :quantity 1
                                              :unit "piece"}))