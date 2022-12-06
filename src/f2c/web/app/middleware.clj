(ns f2c.web.app.middleware
  (:require [ring.middleware.basic-authentication :as rbauth]
            [f2c.web.app.individual.auth :as individual-auth]
            [reitit.ring.middleware.exception :as exception]
            [f2c.community.repository :as community-repo]
            [ring.util.response :as response]
            [f2c.community :as community]))

(defn- wrap-basic-authentication [handler]
  (rbauth/wrap-basic-authentication handler individual-auth/authenticated-individual))

(def individual-basic-authentication
  {:name :individual-basic-authentication
   :compile (constantly wrap-basic-authentication)})

(defn- wrap-facilitator-only [handler]
  (fn [req]
    (let [community-id (get-in req [:path-params :community-id])]
      (if-let [community (community-repo/fetch-community community-id)]
        (if (community/is-facilitator community
                                      (:individual/id (individual-auth/current-individual req)))
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

