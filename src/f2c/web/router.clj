(ns f2c.web.router
  (:require [reitit.ring :as ring]
            [f2c.web.app.middleware :as app-middleware]
            [f2c.web.status :as status]
            [f2c.web.app.individual.index :as individual-index]
            [f2c.web.app.community.order.new :as order-new]))

(defn root []
  (ring/router
   [["/status" status/handler]
    ["/app" {:middleware [[app-middleware/individual-basic-authentication]]}
     ["" individual-index/handler]
     ["/communities/:community-id/orders/new" {:name :route.community/new-order
                                               :middleware [[app-middleware/facilitator-only]]
                                               :handler order-new/handler}]]]
   {:data {:middleware [app-middleware/exception]}}))