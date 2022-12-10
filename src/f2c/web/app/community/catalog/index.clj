(ns f2c.web.app.community.catalog.index
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.web.app.view.layout.default :as layout]
            [f2c.extension.reitit :as r]))

(defn on-availability-change-js-body [form-submit-url]
  (format
   "let formData = new FormData();
    formData.append('community.item-availability/is-available', isAvailableAtClient);
    fetch('%s', {method : 'PUT', body : new URLSearchParams(formData)})
     .then(response => { if (!response.ok) { isAvailableAtClient = isAvailableAtServer; alert('%s'); } })"
   form-submit-url
   "Sorry, Unable to update availability"))

(defn- render-items [req community-id items]
  [:table
   [:thead
    [:tr
     [:th "Item Name"]
     [:th "Availability"]]]
   [:tbody
    (map (fn [{:item/keys [id name is-available]}]
           (let [availability-change-url (r/path req :route.community.catalog.item/update-availability
                                                 {:community-id community-id
                                                  :item-id id})]
             [:tr
              [:td name]
              [:td {:x-data (format "{isAvailableAtClient : %b, itemId : '%s', isAvailableAtServer: %b}" is-available id is-available)}
               [:select {:x-model "isAvailableAtClient"
                         :x-on:change (on-availability-change-js-body availability-change-url)}
                [:option {:value "true" :selected is-available} "Available"]
                [:option {:value "false" :selected (not is-available)} "Not Available"]]]]))
         items)]])

(defn- items-section [req community-id]
  (let [items (community-item-repo/fetch-items community-id)]
    (if (seq items)
      (render-items req community-id items)
      [:p "No items found"])))

(defn handler [req]
  (layout/render "Items Catalog"
                 (items-section req (get-in req [:parameters :path :community-id]))))