(ns f2c.infra.core
  (:require [mount.core :as mount]
            [f2c.infra.config]
            [f2c.infra.db]
            [f2c.web.core]))

(defn start-app []
  (mount/start))

(defn stop-app []
  (mount/stop))