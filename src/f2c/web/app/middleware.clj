(ns f2c.web.app.middleware
  (:require [ring.middleware.basic-authentication :as rbauth]
            [f2c.web.app.individual.auth :as individual-auth]))

(defn- wrap-basic-authentication [handler]
  (rbauth/wrap-basic-authentication handler individual-auth/authenticated-individual))

(def individual-basic-authentication 
  {:name :individual-basic-authentication
   :compile (constantly wrap-basic-authentication)})

