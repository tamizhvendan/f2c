(ns f2c.web.index
  (:require [ring.util.response :as response]))

(defn handler [_]
  (response/redirect "/app"))

