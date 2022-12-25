(ns f2c.web.app.individual.order.new
  (:require [f2c.community.order.repository :as community-order-repo]
            [f2c.extension.reitit :as r]
            [f2c.individual.order.repository :as individual-order-repo]
            [f2c.web.app.view.layout.individual :as layout]
            [ring.util.response :as res]))

(defn render-form [req community-id community-orders]
  (layout/render
   req
   [:form {:action (r/path req :route.individual/orders {:community-id community-id})
           :method "POST"
           :class "md:max-w-md"}
    [:legend {:class "page-heading"} "Create New Individual Order"]
    [:div {:class "mt-2"}
     [:label {:for "individual.order/community-order-id" :class "field-label"} "Community Order"]
     [:select {:id "individual.order/community-order-id" :name "individual.order/community-order-id"
               :required true :autofocus true
               :class "text-sm w-full ~neutral"}
      (map (fn [{:community.order/keys [id name]}]
             [:option {:value id} name]) community-orders)]]
    [:div {:class "mt-4 flex items-center justify-end text-sm space-x-4"}
     [:a {:class "link-tertiary"
          :href (r/path req :route.individual/orders {:community-id community-id})} "Cancel"]
     [:input {:type "submit"
              :class "btn-primary"
              :value "Create New Order"}]]]))

(defn- render-no-community-orders [req community-id]
  (layout/render
   req
   [:section {:class "section ~info p-4 md:max-w-lg md:mx-auto space-y-3 md:space-y-4 rounded"}
    [:p {:class "font-bold"} "No community orders found"]
    [:p {:class ""} "Your community facilitator should create an order for your community first to enable your individual orders"]
    [:a {:class "inline-flex no-underline font-medium hover:text-primary-700"
         :href (r/path req :route.individual/orders {:community-id community-id})}
     [:i {:class "ri-arrow-left-line mr-1"}]
     [:span "Go Back"]]]))

(defn handler [req]
  (let [community-id (get-in req [:current-community :community/id])
        individual-id (get-in req [:current-individual :individual/id])
        community-orders (community-order-repo/fetch-eligible-for-individual-orders community-id individual-id)]
    (if (seq community-orders)
      (render-form req community-id community-orders)
      (render-no-community-orders req community-id))))

(def create-request
  [:map
   [:individual.order/community-order-id uuid?]])

(defn create-handler [req]
  (let [community-id (get-in req [:current-community :community/id])
        individual-id (get-in req [:current-individual :individual/id])
        community-order-id (get-in req [:parameters :form :individual.order/community-order-id])]
    (if (community-order-repo/eligible-for-individual-order? community-id individual-id community-order-id)
      (let [{:individual.order/keys [id]} (individual-order-repo/create-order community-order-id individual-id)]
        (res/redirect (r/path req :route.individual.order/detail {:individual-order-id id})))
      (res/bad-request "Invalid Individual Order Request"))))