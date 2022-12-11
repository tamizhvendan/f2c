(ns f2c.web.app.individual.auth
  (:require [f2c.individual.repository :as individual-repo]))

(defn authenticated-individual [username password]
  (individual-repo/fetch-individual username password))

(defn current-individual [req]
  (:basic-authentication req))




