(ns f2c.web.app.middleware
  (:require [ring.middleware.basic-authentication :as rbauth]
            [f2c.web.app.individual.auth :as individual-auth]
            [reitit.ring.middleware.exception :as exception]))

(defn- wrap-basic-authentication [handler]
  (rbauth/wrap-basic-authentication handler individual-auth/authenticated-individual))

(def individual-basic-authentication 
  {:name :individual-basic-authentication
   :compile (constantly wrap-basic-authentication)})

(defn- global-exception-handler [_ exception _]
  (prn "application exception" exception)
  {:status 500
   :body "Something went wrong!"})

(def exception
  (exception/create-exception-middleware
   (merge
    exception/default-handlers
    {::exception/wrap global-exception-handler})))

