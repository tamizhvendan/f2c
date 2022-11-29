(ns f2c.web.app.individual.auth
  (:require [f2c.infra.db :as db]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]))

(defn authenticated-individual [username password]
  (first (sql/query db/datasource
              ["SELECT * FROM individual
                WHERE mobile_number = ? 
                AND password_hash = crypt(?, password_hash)"
               username
               password]
               {:return-keys true 
                :builder-fn rs/as-kebab-maps})))


(defn current-individual [req]
  (:basic-authentication req))




