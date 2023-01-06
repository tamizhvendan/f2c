(ns f2c.web.app.community.catalog.index
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.extension.format :as fmt]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.components.toggle :as toggle]
            [f2c.web.app.view.layout.community :as layout]
            [f2c.infra.config :as config]))

(defn- price-display [humnaized-price pricing-unit has-price]
  [:div {:x-show "canDisplayPrice()" :class "flex items-center" :style {:display (if has-price "flex" "none")}}
   [:p
    [:span {:class "text-xl font-medium font-mono" :x-text "humanizedPrice"} humnaized-price]
    [:span {:class "ml-1 text-gray-700"} (str "per " pricing-unit)]]
   [:i {:class "px-2 ri-pencil-fill text-gray-600 text-base hover:cursor-pointer hover:text-primary-700" :aria-label "Edit Price"
        :x-on:click "editPrice($refs, $nextTick)"}]])

(defn- price-update-form [item-id uom value]
  [:div {:x-show "isEditingPrice"
         :class "flex space-x-3 text-base items-center"
         :style {:display "none"}}
   [:div {:class "relative w-1/2 md:w-1/3 lg:w-1/4 text-sm"}
    [:div {:class "pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3"}
     [:span {:class "text-gray-500"} (fmt/currency-symbol config/default-currency)]]
    [:input {:x-ref (format "%s-%s" item-id uom) :class "block w-full pl-7 pr-20" :x-model "price" :type "number" :min 1 :value value}]
    [:div {:class "pointer-events-none absolute inset-y-0 right-0 flex items-center pr-3"}
     [:span {:class "text-gray-500"} (str "per " uom)]]]
   [:button {:class "btn-primary text-sm"
             :x-bind:disabled "!canSave()"
             :type "button" :x-on:click "save()"} "Save"]
   [:button {:class "text-sm" :type "cancel" :x-on:click "cancel()"} "Cancel"]])

(defn- render-price-update-label [req item-id uom]
  (let [humnaized-price (fmt/humanize-price config/default-currency 0)]
    [:li {:x-data (format "f2c.priceUpdate('%s', %s, '%s', '%s', '%s')"
                          item-id "null" uom "null"
                          (r/path req :route.community.catalog.item/update-price
                                  {:community-id (get-in req [:current-community :community/id])
                                   :item-id item-id}))
          :class "text-sm"}
     [:p {:x-show "isSaving" :style {:display "none"} :class "text-sm"} "Saving..."]
     [:p {:x-show "canShowSetPriceCta()"
          :x-on:click "editPrice($refs, $nextTick)"
          :class "underline hover:cursor-pointer hover:text-primary-700"}
      (str "Set price per " uom)]
     (price-display humnaized-price uom false)
     (price-update-form item-id uom nil)]))

(defn- render-price [req item-id {:item.price/keys [price currency pricing-unit]}]
  (let [humnaized-price (fmt/humanize-price currency price)]
    [:li {:x-data (format "f2c.priceUpdate('%s', %s, '%s', '%s', '%s')"
                          item-id price pricing-unit humnaized-price
                          (r/path req :route.community.catalog.item/update-price
                                  {:community-id (get-in req [:current-community :community/id])
                                   :item-id item-id}))}
     [:p {:x-show "isSaving" :style {:display "none"} :class "text-sm"} "Saving..."]
     (price-display humnaized-price pricing-unit true)
     (price-update-form item-id pricing-unit price)]))

(defn- render-prices [req {:item/keys [id supported-unit-of-measures prices]}]
  (map (fn [uom]
         (if-let [price (first (filter #(= uom (:item.price/pricing-unit %)) prices))]
           (render-price req id price)
           (render-price-update-label req id uom))) supported-unit-of-measures))

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
     [:ul {:class "mt-3 space-y-3"}
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