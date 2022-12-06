(ns f2c.web.app.view.layout.default
  (:require [f2c.extension.ring-response :as rr]))

(defn render [title body]
  (rr/html
   [:html
    [:head
     [:title title]]
    [:body body]]))