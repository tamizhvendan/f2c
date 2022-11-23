(ns f2c.core
  (:require [f2c.infra.core :as infra]
            [f2c.infra.db :as db]))

(.addShutdownHook (Runtime/getRuntime)
                  (Thread. (fn []
                             (infra/stop-app))))

(defn -main [& args]
  (db/migrate)
  (infra/start-app))