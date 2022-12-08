(ns f2c.web.app.community.catalog.index
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.web.app.view.layout.default :as layout]))

(defn- render-items [items]
  [:table
   [:thead
    [:tr
     [:th "Item Name"]
     [:th "Availability"]]]
   [:tbody
    (map (fn [{:item/keys [name is-available]}]
           [:tr
            [:td name]
            [:td (if is-available "Available" "Not Available")]])
         items)]])

(defn- items-section [community-id]
  (let [items (community-item-repo/fetch-items community-id)]
    (if (seq items)
      (render-items items)
      [:p "No items found"])))

(defn handler [req]
  (layout/render "Items Catalog"
                 (items-section (get-in req [:parameters :path :community-id]))))