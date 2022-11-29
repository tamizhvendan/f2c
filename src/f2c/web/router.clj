(ns f2c.web.router
  (:require [reitit.ring :as ring]
            [f2c.web.app.middleware :as app-middleware]
            [f2c.web.status :as status]
            [f2c.web.app.individual.index :as individual-index]))

(defn root []
  (ring/router
   [["/status" status/handler]
    ["/app" {:middleware [[app-middleware/individual-basic-authentication]]}
     ["" individual-index/handler]]]))