(ns f2c.web.router
  (:require [reitit.ring :as ring]
            [ring.util.response :as rr]
            [f2c.web.app.middleware :as app-middleware]
            [f2c.web.app.individual.auth :as individual-auth]))

(defn version-handler [req]
  (rr/response "It's working"))

(defn app-home-handler [req]
  (rr/response (str "Hi " (:individual/name (individual-auth/current-individual req)))))

(defn root []
  (ring/router
   [["/version" version-handler]
    ["/app" {:middleware [[app-middleware/individual-basic-authentication]]}
     ["" app-home-handler]]]))