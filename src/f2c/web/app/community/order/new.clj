(ns f2c.web.app.community.order.new
  (:require [f2c.web.app.view.layout.default :as layout]))

(defn handler [req]
  (layout/render "New Order"
                 [:p "New Order Form Coming Soon"]))