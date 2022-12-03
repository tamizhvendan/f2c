(ns f2c.community)

(defn is-facilitator [{:community/keys [facilitators]} individual-id]
  (contains? (set (map :community.facilitator/individual-id facilitators))
             individual-id))