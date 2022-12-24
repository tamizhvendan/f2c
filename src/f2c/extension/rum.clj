(ns f2c.extension.rum)

(defn element [tag attrs dynamic-element-or-elements & fixed-elements]
  (as-> (if (keyword? (first dynamic-element-or-elements))
          [dynamic-element-or-elements]
          dynamic-element-or-elements) $
    (concat (vector tag attrs) $ fixed-elements)
    (vec $)))