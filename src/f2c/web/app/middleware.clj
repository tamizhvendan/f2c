(ns f2c.web.app.middleware
  (:require [f2c.community :as community]
            [f2c.community.repository :as community-repo]
            [f2c.individual.repository :as individual-repo]
            [reitit.ring.middleware.exception :as exception]
            [ring.middleware.basic-authentication :as rbauth]
            [ring.util.response :as response]))

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
          (handler req)
          {:status 401
           :message "you are not authorized to perform this operation"})
        (response/not-found "community not found")))))

(def facilitator-only
  {:name :facilitator-only
   :compile (constantly wrap-facilitator-only)})

(defn- global-exception-handler [_ exception _]
  (prn "application exception" exception)
  {:status 500
   :body "Something went wrong!"})

(def exception
  (exception/create-exception-middleware
   (merge
    exception/default-handlers
    {::exception/wrap global-exception-handler})))

