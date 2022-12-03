(ns f2c.infra.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [mount.core :as mount]))

(defn- read-config []
  (aero/read-config (io/resource "config.edn")))

(mount/defstate root 
  :start (read-config))

(defn db-connection-string []
  (:database-url root))

(defn http-port []
  (:http-port root))

(defn version []
  (:version root))
