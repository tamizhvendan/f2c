(ns f2c.extension.format
  (:require [clojure.string :as str]))

(defn initial [name]
  (str/upper-case (str (first name) (second name))))