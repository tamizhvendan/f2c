(ns user
  (:require [clojure.tools.namespace.repl :as repl]
            [f2c.infra.core :refer [stop-app]]))

(repl/set-refresh-dirs "src/f2c")

(defn reset []
  (stop-app)
  (repl/refresh :after 'f2c.infra.core/start-app))

(defn stop []
  (stop-app))