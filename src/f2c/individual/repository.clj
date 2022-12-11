(ns f2c.individual.repository
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]))

(defn fetch-individual [mobile-number password]
  (heql/query-single db/adapter
                     {[[:individual/mobile-number mobile-number]
                       {:where [:= :individual/password-hash [:crypt password :individual/password-hash]]}]
                      [:individual/id
                       :individual/name]}))