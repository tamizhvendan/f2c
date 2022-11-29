(ns f2c.web.status
  (:require [f2c.extension.ring-response :as rr]))

(defn handler [req]
  (rr/json {:status "ok"}))