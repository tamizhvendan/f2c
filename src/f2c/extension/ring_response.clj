(ns f2c.extension.ring-response
  (:require [clojure.data.json :as json]
            [rum.core :as rum]))

(defn json [data]
  {:body (json/write-str data)
   :headers {"Content-Type" "application/json"}})

(defn html [content]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<!doctype html>" (rum/render-static-markup content))})