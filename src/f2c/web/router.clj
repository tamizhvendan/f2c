(ns f2c.web.router
  (:require [reitit.ring :as ring]
            [f2c.web.app.middleware :as app-middleware]
            [f2c.web.status :as status]
            [f2c.web.app.individual.index :as individual-index]
            [f2c.web.app.community.order.new :as order-new]
            [reitit.coercion.malli :as r-malli]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.coercion :as coercion]
            [muuntaja.core :as m]))

(defn root []
  (ring/router
   [["/status" status/handler]
    ["/app" {:middleware [[app-middleware/individual-basic-authentication]]}
     ["" {:name :route.individual/index
          :handler individual-index/handler}]
     ["/communities/:community-id/orders" {:middleware [[app-middleware/facilitator-only]]
                                           :parameters {:path {:community-id uuid?}}}
      ["" {:post order-new/create-handler
           :parameters {:form order-new/create-request}
           :name :route.community/create-order}]
      ["/new" {:name :route.community/new-order
               :handler order-new/handler}]]]]
   {:data {:coercion   r-malli/coercion
           :muuntaja   m/instance
           :middleware [app-middleware/exception
                        muuntaja/format-middleware
                        coercion/coerce-exceptions-middleware
                        parameters/parameters-middleware
                        coercion/coerce-request-middleware]}}))