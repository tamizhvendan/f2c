(ns f2c.web.app.individual.auth
  (:require [f2c.infra.db :as db]
            [honeyeql.core :as heql]))

(defn authenticated-individual [username password]
  (heql/query-single db/adapter
                     {[[:individual/mobile-number username]
                       {:where [:= :individual/password-hash [:crypt password :individual/password-hash]]}]
                      [:individual/id
                       :individual/name]}))

(defn current-individual [req]
  (:basic-authentication req))




