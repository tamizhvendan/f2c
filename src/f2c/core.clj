(ns f2c.core
  (:require [f2c.infra.core :as infra]))

(.addShutdownHook (Runtime/getRuntime)
                  (Thread. (fn []
                             (infra/stop-app))))

(defn -main [& args]
  (infra/start-app))