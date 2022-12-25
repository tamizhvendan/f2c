(ns f2c.community.item.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [clojure.data.json :as json]))

(defn fetch-items
  ([community-id]
   (fetch-items community-id nil))
  ([community-id is-available]
   (->> (sql/query db/datasource
                   [(format
                     "SELECT 
                        i.id, 
                        i.name, 
                        ia.is_available,
                        COALESCE(
                          JSON_AGG(JSON_BUILD_OBJECT(
                            'item.price/price', ip.price, 
                            'item.price/currency', ip.currency, 
                            'item.price/pricing-unit', ip.pricing_unit))
                      FILTER (WHERE ip.price IS NOT NULL), '[]') AS item_prices
                      FROM community.item_availability AS ia
                      INNER JOIN item AS i on i.id = ia.item_id
                      LEFT OUTER JOIN community.item_price AS ip on ip.item_id = i.id AND upper(ip.valid_period) >= now () AT TIME ZONE 'UTC'
                      WHERE ia.community_id = ? AND %s
                      GROUP BY i.id, i.name, ia.is_available
                      ORDER BY i.name"
                     (if (nil? is-available) "?" "ia.is_available = ?"))
                    community-id (if (nil? is-available) true is-available)]
                   {:builder-fn rs/as-kebab-maps})
        (map (fn [{:keys [item-prices]
                   :as item}]
               (dissoc (assoc item :item/prices (json/read-str (str item-prices) :key-fn keyword))
                       :item-prices))))))

(defn fetch-available-items [community-id]
  (fetch-items community-id true))

(defn update-availability [community-id item-id is-available]
  (heql/update! db/adapter
                #:community.item-availability{:is-available is-available}
                #:community.item-availability{:community-id community-id
                                              :item-id item-id}))

(comment
  (update-availability #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                       #uuid "21ad557e-7fc6-4cb1-9a93-b3f87a8812d7"
                       true)
  (fetch-available-items #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd")

  (fetch-items #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"))

