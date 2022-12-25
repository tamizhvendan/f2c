(ns f2c.web.app.community.catalog.index
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.extension.format :as fmt]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.components.toggle :as toggle]
            [f2c.web.app.view.layout.community :as layout]))

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

(defn- render-item [req community-id {:item/keys [id name prices] :as item}]
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
               :style {:display (if is-available "inline" "none")}} "Not available"]]
      (toggle/render "isAvailableAtClient" (on-availability-change-js-body availability-change-url))]
     [:ul {:class "mt-4 space-y-2"}
      (map (fn [{:item.price/keys [price currency pricing-unit]}]
             [:li {:class "text-sm"} (format "%s per %s" (fmt/humanize-price currency price) pricing-unit)]) prices)]]))

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