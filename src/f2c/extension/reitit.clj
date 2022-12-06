(ns f2c.extension.reitit
  (:require [reitit.core :as r]))

(defn path [req name path-params]
  (-> (::r/router req)
      (r/match-by-name name path-params)
      (r/match->path)))