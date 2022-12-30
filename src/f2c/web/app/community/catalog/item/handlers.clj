(ns f2c.web.app.community.catalog.item.handlers
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.extension.ring-response :as rr]
            [f2c.extension.format :as fmt]
            [f2c.infra.config :as config]))

(def update-availability-request
  [:map
   [:community.item-availability/is-available boolean?]])

(defn update-availability-handler [req]
  (let [is-available (get-in req [:parameters :form :community.item-availability/is-available])
        {:keys [community-id item-id]} (get-in req [:parameters :path])]
    (community-item-repo/update-availability community-id item-id is-available)
    (rr/json {})))

(def update-price-request
  [:map
   [:community.item-price/price [:and decimal? [:> 0]]]
   [:community.item-price/pricing-unit [:enum "kg" "piece"]]])

(defn update-price-handler [req]
  (let [{:community.item-price/keys [price pricing-unit]} (get-in req [:parameters :form])
        {:keys [community-id item-id]} (get-in req [:parameters :path])]
    (community-item-repo/update-price community-id item-id price pricing-unit)
    (rr/json {:humanizedPrice (fmt/humanize-price config/default-currency price)})))

