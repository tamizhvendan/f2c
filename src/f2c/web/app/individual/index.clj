(ns f2c.web.app.individual.index 
  (:require [f2c.web.app.individual.auth :as individual-auth]
            [ring.util.response :as rr]))

(defn handler [req]
  (rr/response (str "Hi " (:individual/name (individual-auth/current-individual req)))))