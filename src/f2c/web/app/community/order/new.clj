(ns f2c.web.app.community.order.new
  (:require [f2c.web.app.view.layout.default :as layout]
            [f2c.extension.reitit :as r]
            [ring.util.response :as response]))

(defn handler [req]
  (layout/render "New Order"
                 [:form {:action (r/path req :route.community/create-order
                                         {:community-id (get-in req [:path-params :community-id])})
                         :method "POST"}
                  [:legend "Create New Order"]
                  [:label {:for "community.order/name"}]
                  [:input {:type "text" :id "community.order/name" :name "community.order/name"
                           :placehoder "Name of the Order"}]
                  [:input {:style {:margin-left "0.5rem"}
                           :type "submit"
                           :value "Create Order"}]]))

(defn create-handler [req]
  (response/redirect (r/path req :route.individual/index)))