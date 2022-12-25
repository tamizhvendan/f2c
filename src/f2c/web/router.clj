(ns f2c.web.router
  (:require [reitit.ring :as ring]
            [reitit.coercion.malli :as r-malli]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.coercion :as coercion]
            [muuntaja.core :as m]

            [f2c.web.app.middleware :as app-middleware]

            [f2c.web.index :as index]
            [f2c.web.status :as status]

            [f2c.web.app.individual.index :as individual-index]
            [f2c.web.app.individual.community.order.index :as individual-community-order-index]
            [f2c.web.app.individual.order.index :as individual-order-index]

            [f2c.web.app.community.order.index :as community-order-index]
            [f2c.web.app.community.order.new :as community-order-new]
            [f2c.web.app.community.order.individual :as community-order-individual]

            [f2c.web.app.community.catalog.index :as catalog-index]
            [f2c.web.app.community.catalog.item.handlers :as catalog-item]))

(defn root []
  (ring/router
   [["/" index/handler]
    ["/status" status/handler]
    ["/assets/*" (ring/create-resource-handler)]
    ["/app" {:middleware [[app-middleware/individual-basic-authentication]]}
     ["" {:name :route.individual/index
          :handler individual-index/handler}]

     ["/communities/:community-id" {:middleware [[app-middleware/facilitator-only]]
                                    :parameters {:path {:community-id uuid?}}}
      ["/catalog"
       ["" {:name :route.community.catalog/index
            :handler catalog-index/handler}]
       ["/items/:item-id/availability" {:name :route.community.catalog.item/update-availability
                                        :parameters {:path {:item-id uuid?}
                                                     :form catalog-item/update-availability-request}
                                        :put catalog-item/update-availability-handler}]]
      ["/orders"
       ["" {:name :route.community/orders
            :post {:parameters {:form community-order-new/create-request}
                   :handler community-order-new/create-handler}
            :get {:handler community-order-index/handler}}]
       ["/new" {:name :route.community/new-order
                :handler community-order-new/handler}]]]

     ["/individual"
      ["/communities/:community-id"
       {:middleware [[app-middleware/individual-community-only]]
        :parameters {:path {:community-id uuid?}}}
       ["/orders" {:name :route.individual.community.order/index
                   :handler individual-community-order-index/handler}]]
      ["/orders/:individual-order-id" {:middleware [[app-middleware/individual-order]]
                                       :parameters {:path {:individual-order-id uuid?}}}
       ["" {:name :route.individual-order/index
            :handler individual-order-index/handler}]]]

     ["/community/orders/:community-order-id" {:middleware [[app-middleware/community-order]]
                                               :parameters {:path {:community-order-id uuid?}}}
      ["/individual-orders" {:name :route.community-order/create-individual-order
                             :post community-order-individual/create-individual-order}]
      ["/individual-order" {:name :route.community-order/view-current-individual-order
                            :handler community-order-individual/view-current-individual-order-handler}]]]]
   {:data {:coercion   r-malli/coercion
           :muuntaja   m/instance
           :middleware [app-middleware/exception
                        muuntaja/format-middleware
                        coercion/coerce-exceptions-middleware
                        parameters/parameters-middleware
                        coercion/coerce-request-middleware]}}))