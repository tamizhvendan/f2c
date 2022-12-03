(ns f2c.community.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]))

(defn fetch-communities [individual-id]
  (heql/query db/adapter
              {[[] {:where [:= [:community/community-individuals
                                :community.individual/individual-id] individual-id]}]
               [:community/id
                :community/name
                {[:community/community-facilitators :as :community/facilitators]
                 [:community.facilitator/individual-id]}]}))

(comment
  (fetch-communities #uuid "22f994e0-b89f-43b2-adc7-a13b0e6ef760")
  (fetch-communities #uuid "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470"))
