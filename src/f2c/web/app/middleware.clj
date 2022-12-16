(ns f2c.web.app.middleware
  (:require [f2c.community :as community]
            [f2c.community.repository :as community-repo]
            [f2c.individual.repository :as individual-repo]
            [reitit.ring.middleware.exception :as exception]
            [ring.middleware.basic-authentication :as rbauth]
            [ring.util.response :as response]
            [f2c.community.order.repository :as community-order-repo]
            [f2c.individual.order.repository :as individual-order-repo]))

(defn- wrap-basic-authentication [handler]
  (rbauth/wrap-basic-authentication (fn [req]
                                      (->> (:basic-authentication req)
                                           (assoc req :current-individual)
                                           handler))
                                    individual-repo/fetch-individual))

(def individual-basic-authentication
  {:name :individual-basic-authentication
   :compile (constantly wrap-basic-authentication)})

(defn- wrap-facilitator-only [handler]
  (fn [req]
    (let [community-id (get-in req [:path-params :community-id])]
      (if-let [community (community-repo/fetch-community community-id)]
        (if (community/is-facilitator community (get-in req [:current-individual :individual/id]))
          (handler (assoc req :current-community community))
          {:status 401
           :message "you are not authorized to perform this operation"})
        (response/not-found "community not found")))))

(def facilitator-only
  {:name :facilitator-only
   :compile (constantly wrap-facilitator-only)})

(defn- wrap-individual-community-only [handler]
  (fn [req]
    (let [{:keys [community-id individual-id]} (:path-params req)]
      (if-let [community (community-repo/fetch-community community-id)]
        (if (community-repo/is-part-of-community? community-id individual-id)
          (handler (assoc req :current-community community))
          {:status 401
           :message "you are not authorized to perform this operation"})
        (response/not-found "community not found")))))

(def individual-community-only
  {:name :individual-community-only
   :compile (constantly wrap-individual-community-only)})

(defn- wrap-community-order [handler]
  (fn [req]
    (let [{:keys [community-order-id]} (:path-params req)
          {current-individual-id :individual/id} (:current-individual req)]
      (if-let [community-order (community-order-repo/fetch-order community-order-id)]
        (if (community-repo/is-part-of-community? (get-in community-order [:community.order/community :community/id]) current-individual-id)
          (handler (assoc req :current-community-order community-order))
          {:status 401
           :message "you are not authorized to perform this operation"})
        (response/not-found "community order not found")))))

(def community-order
  {:name :community-order
   :compile (constantly wrap-community-order)})

(defn- wrap-individual-order [handler]
  (fn [req]
    (let [{:keys [individual-order-id]} (:path-params req)
          {current-individual-id :individual/id} (:current-individual req)]
      (if-let [individual-order (individual-order-repo/fetch-order individual-order-id)]
        (if (= current-individual-id (:individual.order/individual-id individual-order))
          (handler (assoc req :current-individual-order individual-order))
          {:status 401
           :message "you are not authorized to perform this operation"})
        (response/not-found "individual order not found")))))

(def individual-order
  {:name :individual-order
   :compile (constantly wrap-individual-order)})

(defn- global-exception-handler [_ exception _]
  (prn "application exception" exception)
  {:status 500
   :body "Something went wrong!"})

(def exception
  (exception/create-exception-middleware
   (merge
    exception/default-handlers
    {::exception/wrap global-exception-handler})))

