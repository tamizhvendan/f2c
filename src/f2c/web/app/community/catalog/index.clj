(ns f2c.web.app.community.catalog.index
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.extension.format :as fmt]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.components.toggle :as toggle]
            [f2c.web.app.view.layout.community :as layout]))

(defn on-price-update-js-body [form-submit-url]
  (format
   "isEditingPrice = false
    let formData = new FormData();
    formData.append('community.item-price/item-id', itemId);
    formData.append('community.item-price/price', price);
    formData.append('community.item-price/pricing-unit', pricingUnit);
    fetch('%s', {method : 'PUT', body : new URLSearchParams(formData)})
     .then(response => { 
        if (!response.ok) { 
          isEditingPrice = true
          alert('%s'); 
          return
        }
      });"
   form-submit-url
   "Sorry, Unable to update price"))

(defn- render-price-update-form [req item-id uom]
  [:li {:x-data (format "{isEditingPrice : false, itemId : '%s', price: 0, pricingUnit: '%s'}" item-id uom)
        :class "text-sm"}

   [:p {:x-show "!isEditingPrice"
        :x-on:click "isEditingPrice = true"
        :class "underline hover:cursor-pointer hover:text-primary-700"}
    (str "Set price per " uom)]

   [:div {:x-show "isEditingPrice"
          :class "flex space-x-2 text-base items-center"
          :style {:display "none"}}
    [:span {:class "text-lg"} (fmt/currency-symbol "INR")]
    [:input {:class "w-20 h-10"
             :x-model "price"
             :type "number" :min 0 :value "0"}]
    [:span (str "per " uom)]
    [:button {:type "cancel" :x-on:click "isEditingPrice = false"} "Cancel"]
    [:button {:type "button" :x-on:click (on-price-update-js-body (r/path req :route.community.catalog.item/update-price
                                                                          {:community-id (get-in req [:current-community :community/id])
                                                                           :item-id item-id}))} "Save"]]])

(defn- render-prices [req {:item/keys [id supported-unit-of-measures prices]}]
  (if (seq prices)
    (map (fn [{:item.price/keys [price currency pricing-unit]}]
           [:li {:class "text-sm"} (format "%s per %s" (fmt/humanize-price currency price) pricing-unit)]) prices)
    (map (partial render-price-update-form req id) supported-unit-of-measures)))

(defn on-availability-change-js-body [form-submit-url]
  (format
   "isAvailableAtClient = !isAvailableAtClient;
    let formData = new FormData();
    formData.append('community.item-availability/is-available', isAvailableAtClient);
    fetch('%s', {method : 'PUT', body : new URLSearchParams(formData)})
     .then(response => { 
        if (!response.ok) { 
          isAvailableAtClient = isAvailableAtServer; 
          alert('%s'); 
          return
        }
        isAvailableAtServer = isAvailableAtClient
      });"
   form-submit-url
   "Sorry, Unable to update availability"))

(defn- render-item [req community-id {:item/keys [id name] :as item}]
  (let [is-available (:item-availability/is-available item)
        availability-change-url (r/path req :route.community.catalog.item/update-availability
                                        {:community-id community-id
                                         :item-id id})]
    [:li {:class "p-4 rounded shadow-md bg-white mt-4"}
     [:div {:class "flex justify-between"
            :x-data (format "{isAvailableAtClient : %b, itemId : '%s', isAvailableAtServer: %b}" is-available id is-available)}
      [:p {:class "font-medium flex mr-2"}
       [:span name]
       [:span {:x-show "!isAvailableAtClient"
               :class "ml-2 text-xs badge ~neutral @high"
               :style {:display (if is-available "none" "inline")}} "Not available"]]
      (toggle/render "isAvailableAtClient" (on-availability-change-js-body availability-change-url))]
     [:ul {:class "mt-4 space-y-2"}
      (render-prices req item)]]))

(defn- render-items [req community-id items]
  [:ul
   (map (partial render-item req community-id) items)])

(defn- items-section [req]
  (let [community-id (get-in req [:parameters :path :community-id])
        items (community-item-repo/fetch-items community-id)]
    (if (seq items)
      (render-items req community-id items)
      [:p "No items found"])))

(defn handler [req]
  (layout/render req
                 (items-section req)
                 :catalog))