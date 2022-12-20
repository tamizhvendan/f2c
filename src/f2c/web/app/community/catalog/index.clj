(ns f2c.web.app.community.catalog.index
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.web.app.view.layout.community :as layout]
            [f2c.extension.reitit :as r]))

(defn on-availability-change-js-body [form-submit-url]
  (format
   "let formData = new FormData();
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

(defn- render-item [req community-id {:item/keys [id name prices] :as item}]
  (let [is-available (:item-availability/is-available item)
        availability-change-url (r/path req :route.community.catalog.item/update-availability
                                        {:community-id community-id
                                         :item-id id})]
    [:li {:class "p-4 rounded shadow-md bg-white mt-4"}
     [:div {:class "flex justify-between"
            :x-data (format "{isAvailableAtClient : %b, itemId : '%s', isAvailableAtServer: %b}" is-available id is-available)}
      [:p {:class "font-medium"} name]
      [:select {:x-model "isAvailableAtClient"
                :x-on:change (on-availability-change-js-body availability-change-url)}
       [:option {:value "true" :selected is-available} "Available"]
       [:option {:value "false" :selected (not is-available)} "Not Available"]]]
     [:ul
      (map (fn [{:item.price/keys [price currency pricing-unit]}]
             [:li (format "%s %s per %s" currency price pricing-unit)]) prices)]]))

(defn- render-items [req community-id items]
  [:ul {:class ""}
   (map (partial render-item req community-id) items)])

(defn- items-section [req community-id]
  (let [items (community-item-repo/fetch-items community-id)]
    (if (seq items)
      (render-items req community-id items)
      [:p "No items found"])))

(defn handler [req]
  (layout/render req
                 [(items-section req (get-in req [:parameters :path :community-id]))]
                 :catalog))