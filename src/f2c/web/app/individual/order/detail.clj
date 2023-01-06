(ns f2c.web.app.individual.order.detail
  (:require [f2c.extension.format :as fmt]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.components.app :as app-component]
            [f2c.web.app.view.layout.individual :as layout]
            [f2c.individual.order.repository :as individual-order-repo]))

(defn- item-quantity-update-form [item-id supported-unit-of-measures value]
  [:div {:x-show "isEditingQuantity"
         :class "flex space-x-3 items-center"
         :style {:display "none"}}
   [:div {:class "relative w-1/2 md:w-1/4"}
    [:input {:x-ref item-id
             :class "block w-full pr-20 md:pr-16" :x-model "quantity" :type "number" :min 0.1 :value value}]
    (if (= 1 (count supported-unit-of-measures))
      [:div {:class "pointer-events-none absolute inset-y-0 right-0 flex items-center pr-3"}
       [:span {:class "text-gray-500"} (first supported-unit-of-measures)]]
      [:div {:class "absolute inset-y-0 right-0 flex items-center"}
       [:select {:x-model "unit" :class "h-full rounded-md border-transparent bg-transparent py-0 pl-2 pr-7"}
        (map (fn [uom]
               [:option uom]) supported-unit-of-measures)]])]
   [:button {:class "btn-primary"
             :x-bind:disabled "!canSave()"
             :type "button" :x-on:click "save"} "Save"]
   [:button {:class "text-sm" :type "cancel" :x-on:click "cancel()"} "Cancel"]])

(defn- render-item [req {:item/keys [id name prices supported-unit-of-measures] :as item}]
  (let [{:item.ordered/keys [unit quantity]} item
        has-already-updated-quantity (and unit quantity)]
    [:li {:x-data (format "f2c.orderItemCreate('%s', %s, '%s', '%s')"
                          id
                          (if has-already-updated-quantity quantity "null")
                          (if has-already-updated-quantity unit (first supported-unit-of-measures))
                          (r/path req :route.individual.order/items
                                  {:individual-order-id (get-in req [:parameters :path :individual-order-id])}))
          :class "p-4"}
     [:div {:class "flex items-center flex-wrap text-lg"}
      [:p {:class "font-medium flex mr-2 font-display"} name]
      [:ul {:class "text-gray-600 flex items-center space-x-2"}
       (map (fn [{:item.price/keys [price currency pricing-unit]}]
              [:li {:class "text-sm"} (format " · %s/%s" (fmt/humanize-price currency price) pricing-unit)]) prices)]]
     [:div {:class "mt-2"}
      (item-quantity-update-form id supported-unit-of-measures nil)
      [:p {:x-show "canDisplayQuantity()"
           :style {:display (if has-already-updated-quantity "block" "none")}}
       [:span {:class "text-xl font-medium font-mono"
               :x-text "quantity"} quantity]
       [:span {:class "ml-1 text-gray-700" :x-text "unit"} unit]]
      [:p {:x-show "canAddQuantity()"
           :x-on:click "addQuantity($refs, $nextTick)"
           :style {:display (if has-already-updated-quantity "none" "block")}
           :class "text-gray-700 underline hover:cursor-pointer hover:text-primary-700"} "Add"]]]))

(defn- render-items [req items]
  (if (seq items)
    [:ul {:class "mt-4 rounded shadow-md bg-white divide-y divide-gray-100 divide-solid "} (map (partial render-item req) items)]
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