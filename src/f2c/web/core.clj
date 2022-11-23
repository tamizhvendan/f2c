(ns f2c.web.core 
  (:require [f2c.web.router :as router]
            [reitit.ring :as ring]
            [mount.core :as mount]
            [ring.adapter.jetty9 :as jetty]
            [f2c.infra.config :as config]))

(def app
  (ring/ring-handler (router/root)))

(defn- start-web-app []
  (jetty/run-jetty #'app {:port (config/http-port)
                          :join? false}))
(mount/defstate web-app
  :start (start-web-app)
  :stop (.stop web-app))