(ns f2c.web.app.community.catalog.item.handlers
  (:require [f2c.community.item.repository :as community-item-repo]
            [f2c.extension.ring-response :as rr]))

(def update-availability-request
  [:map
   [:community.item-availability/is-available boolean?]])

(defn update-availability-handler [req]
  (let [is-available (get-in req [:parameters :form :community.item-availability/is-available])
        {:keys [community-id item-id]} (get-in req [:parameters :path])]
    (community-item-repo/update-availability community-id item-id is-available)
    (rr/json {:community.item-availability/is-available is-available})))