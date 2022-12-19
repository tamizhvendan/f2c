(ns f2c.infra.core
  (:require [mount.core :as mount]
            [f2c.infra.config]
            [f2c.infra.db]
            [f2c.web.core]))

(defn start-app []
  (System/setProperty "user.timezone" "UTC")
  (java.util.TimeZone/setDefault (java.util.TimeZone/getTimeZone "UTC"))
  (mount/start))

(defn stop-app []
  (mount/stop))