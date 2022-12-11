(ns f2c.community.item.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]
            [next.jdbc.sql :as sql]))

(defn fetch-items [community-id]
  (->> (heql/query db/adapter
                   {[[:community.item-availability/community-id community-id]
                     {:order-by [[:community.item-availability/item :item/name]]}]
                    [:community.item-availability/is-available
                     {:community.item-availability/item
                      [:item/id
                       :item/name]}]})
       (map (fn [{:community.item-availability/keys [is-available item]}]
              (assoc item :item/is-available is-available)))))

(defn update-availability [community-id item-id is-available]
  (sql/update! db/datasource :community.item_availability
               {:is_available is-available}
               {:community_id community-id
                :item_id item-id}))

(comment
  (fetch-items "74e06d97-cf9f-4133-b6e2-8f06c886f1cd")
  (fetch-items "4f6d13df-b11b-4a37-a45c-194c60a803af"))

