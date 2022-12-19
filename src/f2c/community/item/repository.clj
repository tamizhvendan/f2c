(ns f2c.community.item.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]))

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

(defn fetch-available-items [community-id]
  (->> (heql/query db/adapter
                   {[[:community.item-availability/community-id community-id]
                     {:where [:= :community.item-availability/is-available true]
                      :order-by [[:community.item-availability/item :item/name]]}]
                    [{:community.item-availability/item
                      [:item/id
                       :item/name]}]})
       (map :community.item-availability/item)))

(defn update-availability [community-id item-id is-available]
  (heql/update! db/adapter
                #:community.item-availability{:is-available is-available}
                #:community.item-availability{:community-id community-id
                                              :item-id item-id}))

(comment
  (update-availability #uuid "74e06d97-cf9f-4133-b6e2-8f06c886f1cd"
                       #uuid "21ad557e-7fc6-4cb1-9a93-b3f87a8812d7"
                       true)
  (fetch-available-items "74e06d97-cf9f-4133-b6e2-8f06c886f1cd")
  (fetch-items "4f6d13df-b11b-4a37-a45c-194c60a803af"))

