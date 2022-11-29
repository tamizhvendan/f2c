(ns f2c.extension.ring-response
  (:require [clojure.data.json :as json]))

(defn json [data]
  {:body (json/write-str data)
   :headers {"Content-Type" "application/json"}})