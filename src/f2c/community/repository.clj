(ns f2c.community.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]))

(defn fetch-community [community-id]
  (heql/query db/adapter
              {[:community/id community-id]
               [:community/id
                :community/name
                {[:community/community-facilitators :as :community/facilitators]
                 [:community.facilitator/individual-id]}]}))

(defn fetch-communities [individual-id]
  (heql/query db/adapter
              {[[] {:where [:= [:community/community-individuals
                                :community.individual/individual-id] individual-id]}]
               [:community/id
                :community/name
                {[:community/community-facilitators :as :community/facilitators]
                 [:community.facilitator/individual-id]}]}))

(defn is-part-of-community? [community-id individual-id]
  (some? (heql/query-single db/adapter {[:community.individual/community-id community-id
                                         :community.individual/individual-id individual-id]
                                        [:community.individual/joined-on]})))

(comment
  ; community.isFacilator(individualId)
  (is-part-of-community?
   "4f6d13df-b11b-4a37-a45c-194c60a803af"
   "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470")

  (fetch-community "4f6d13df-b11b-4a37-a45c-194c60a803af")
  (fetch-communities #uuid "22f994e0-b89f-43b2-adc7-a13b0e6ef760")
  (fetch-communities #uuid "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470"))
