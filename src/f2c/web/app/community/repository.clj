(ns f2c.web.app.community.repository
  (:require [f2c.infra.db :as db]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]))

(defn fetch-communities [invididual-id]
  (sql/query db/datasource
             ["SELECT c.id, c.name, cf.id IS NOT NULL AS \"community/is-facilitator\" 
               FROM community AS c
               INNER JOIN community.individual AS ci ON ci.community_id = c.id AND ci.individual_id = ?
               LEFT OUTER JOIN community.facilitator AS cf ON cf.community_id = c.id AND cf.individual_id = ?"
              invididual-id
              invididual-id]
             {:builder-fn rs/as-kebab-maps}))

(comment
  (fetch-communities #uuid "22f994e0-b89f-43b2-adc7-a13b0e6ef760")
  (fetch-communities #uuid "0a51f1ac-f4e9-478d-8cd3-4b1c135a4470"))
