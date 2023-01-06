(ns f2c.web.app.individual.order.detail
  (:require [f2c.extension.format :as fmt]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.components.app :as app-component]
            [f2c.web.app.view.layout.individual :as layout]
            [f2c.individual.order.repository :as individual-order-repo]))

(defn- render-item [req {:item/keys [id name prices] :as item}]
  [:li {:x-data (format "f2c.orderItemCreate('%s', %s, %s, '%s')"
                        id "null" "null"
                        (r/path req :route.individual.order/items
                                {:individual-order-id (get-in req [:parameters :path :individual-order-id])}))
        :class "py-2"}
   [:div {:class "flex items-center"}
    [:p {:class "font-medium flex mr-2"} name]
    [:ul {:class "text-gray-600 flex items-center space-x-2"}
     (map (fn [{:item.price/keys [price currency pricing-unit]}]
            [:li {:class "text-sm"} (format " · %s per %s" (fmt/humanize-price currency price) pricing-unit)]) prices)]]
   [:div {:class "mt-2"}
    (let [{:item.ordered/keys [unit quantity]} item]
      (if (and unit quantity)
        [:p (format "already placed %s %s" quantity unit)]
        [:p {:x-show "!isEditingQuantity"
             :x-on:click "updateQuantity()"
             :class "underline hover:cursor-pointer hover:text-primary-700"} "Update quantity"]))]])

(defn- render-items [req items]
  (if (seq items)
    [:ul {:class "mt-4 p-4 rounded shadow-md bg-white divide-y divide-gray-100 divide-solid "} (map (partial render-item req) items)]
    [:section {:class "section ~positive @low p-4 md:max-w-lg space-y-3 md:space-y-4 rounded mt-4"}
     [:p {:class "font-bold"} "No available items found"]
     [:p "Items that are available for ordering will be shown here once the community facilitator updates the availability of the items."]]))

(defn- render-body [req]
  (let [community-id (get-in req [:current-community :community/id])
        community-order (:current-community-order req)
        items (individual-order-repo/fetch-items (get-in req [:parameters :path :individual-order-id]))]
    (layout/render req
                   [:div

                    [:div {:class "flex"}
                     [:a {:class "flex hover:text-primary-700"
                          :href (r/path req :route.individual/orders {:community-id community-id})}
                      [:i {:class "ri-arrow-left-line mr-1 font-bold text-lg"}]
                      [:h3 {:class "page-heading mr-2"} (:community.order/name community-order)]]
                     (app-component/community-order-state (:community.order/state community-order))]
                    (render-items req items)])))

(defn handler [req]
  (render-body req))