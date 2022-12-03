(ns f2c.web.status
  (:require [f2c.extension.ring-response :as rr]
            [f2c.infra.config :as config]))

(defn handler [req]
  (rr/json {:status "ok"
            :version (config/version)}))