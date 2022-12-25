(ns f2c.web.app.community.order.new
  (:require [f2c.web.app.view.layout.community :as layout]
            [f2c.extension.reitit :as r]
            [ring.util.response :as response]
            [f2c.community.order.repository :as community-order-repo]))

(defn render-form [req]
  (let [community-id (get-in req [:path-params :community-id])]
    (layout/render
     req
     [[:form {:action (r/path req :route.community/orders {:community-id community-id})
              :method "POST"
              :class "space-y-5 md:space-y-6 md:max-w-lg"}
       [:legend {:class "page-heading"} "Create New Community Order"]
       [:div
        [:label {:for "community.order/name" :class "field-label"} "Name of the order"]
        [:input {:type "text" :id "community.order/name" :name "community.order/name"
                 :class "text-sm w-full field ~neutral"
                 :autofocus true
                 :max-length 256 :min-length 4
                 :required true
                 :placeholder "Dec 16th Order"}]]
       [:div {:class "flex items-center justify-end text-sm space-x-4"}
        [:a {:class "link-tertiary"
             :href (r/path req :route.community/orders {:community-id community-id})} "Cancel"]
        [:input {:type "submit"
                 :class "btn-primary px-3"
                 :value "Create New Order"}]]]]
     :orders)))

(defn handler [req]
  (render-form req))

(def create-request
  [:map
   [:community.order/name [:string {:min 4 :max 256}]]])

(defn create-handler [req]
  (let [order-name (get-in req [:parameters :form :community.order/name])
        community-id (get-in req [:parameters :path :community-id])]
    (community-order-repo/create-order community-id order-name)
    (response/redirect (r/path req :route.community/orders {:community-id community-id}))))