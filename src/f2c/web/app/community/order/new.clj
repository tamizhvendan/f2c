(ns f2c.web.app.community.order.new
  (:require [f2c.web.app.view.layout.default :as layout]
            [f2c.extension.reitit :as r]
            [ring.util.response :as response]
            [f2c.community.order.repository :as community-order-repo]))

(defn handler [req]
  (layout/render "New Order"
                 [:form {:action (r/path req :route.community/create-order
                                         {:community-id (get-in req [:path-params :community-id])})
                         :method "POST"}
                  [:legend "Create New Order"]
                  [:label {:for "community.order/name"}]
                  [:input {:type "text" :id "community.order/name" :name "community.order/name"
                           :max-length 256 :min-length 4
                           :placehoder "Name (4 to 256 characters)"}]
                  [:input {:style {:margin-left "0.5rem"}
                           :type "submit"
                           :value "Create Order"}]]))

(def create-request
  [:map
   [:community.order/name [:string {:min 4 :max 256}]]])

(defn create-handler [req]
  (let [order-name (get-in req [:parameters :form :community.order/name])
        community-id (get-in req [:parameters :path :community-id])]
    (community-order-repo/create-order community-id order-name)
    (response/redirect (r/path req :route.individual/index))))