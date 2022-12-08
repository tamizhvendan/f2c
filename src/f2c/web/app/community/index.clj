(ns f2c.web.app.community.index
  (:require [f2c.community.repository :as community-repo]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.default :as layout]
            [f2c.community.order.repository :as community-order-repo]))

(defn- orders-section [req community-id]
  (let [orders (community-order-repo/fetch-orders community-id)]
    [:div
     [:a {:href (r/path req :route.community/new-order {:community-id community-id})} "Create Order"]
     (if (seq orders)
       [:p {:class "font-bold"} "Your Orders"
        [:ul
         (map (fn [{:community.order/keys [name state]}]
                [:li (format "%s - %s" name state)]) orders)]]
       [:p "Sorry, You don't have any orders"])]))

(defn- item-catalog-section [req community-id]
  [:div
   [:p {:class "font-bold"} "Items Catalog"]
   [:a {:href (r/path req :route.community.catalog/index
                      {:community-id community-id})}
    "Configure Availability"]])

(defn handler [req]
  (let [community-id (get-in req [:parameters :path :community-id])
        {:community/keys [name]} (community-repo/fetch-community community-id)]
    (layout/render (str name " - Community")
                   [:main {:class "space-y-4"}
                    [:div [:p name]]
                    (orders-section req community-id)
                    (item-catalog-section req community-id)])))