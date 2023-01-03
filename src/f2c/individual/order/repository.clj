(ns f2c.individual.order.repository
  (:require [honeyeql.core :as heql]
            [f2c.infra.db :as db]))

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

(comment
  (create-order-item  #:individual.order-item{:individual-order-id "8248128e-a137-48a9-9913-c2f4fe3e4198"
                                              :item-id "bd6f86ab-b266-4757-9d3f-12dd286c4433"
                                              :quantity 1
                                              :unit "piece"}))