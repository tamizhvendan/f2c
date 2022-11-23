(ns user
  (:require [clojure.tools.namespace.repl :as repl]
            [f2c.infra.core :as infra]))

(repl/set-refresh-dirs "src/f2c")

(defn reset []
  (infra/stop-app)
  (repl/refresh :after 'f2c.infra.core/start-app))

(defn stop []
  (infra/stop-app))