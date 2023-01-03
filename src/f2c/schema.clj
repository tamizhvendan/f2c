(ns f2c.schema)

(def positive-decimal
  [:and decimal? [:> 0]])

(def unit-of-measure
  [:enum "kg" "piece"])