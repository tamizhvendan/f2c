(ns f2c.community.item.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]))

(defn fetch-items [community-id]
  ; TODO: do sorting at the query
  (->> (heql/query db/adapter
                   {[:community.item-availability/community-id community-id]
                    [:community.item-availability/is-available
                     {:community.item-availability/item
                      [:item/id
                       :item/name]}]})
       (sort-by #(get-in % [:community.item-availability/item :item/name]))
       (map (fn [{:community.item-availability/keys [is-available item]}]
              (assoc item :item/is-available is-available)))))

(comment
  (fetch-items "74e06d97-cf9f-4133-b6e2-8f06c886f1cd")
  (fetch-items "4f6d13df-b11b-4a37-a45c-194c60a803af"))