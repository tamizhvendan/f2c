(ns f2c.web.router
  (:require [reitit.ring :as ring]
            [ring.util.response :as rr]))

(defn version-handler [req]
  (rr/response "It's working"))

(defn root []
  (ring/router
   [["/version" version-handler]]))