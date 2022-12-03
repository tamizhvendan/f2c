(ns f2c.web.app.individual.index 
  (:require [f2c.web.app.individual.auth :as individual-auth]
            [f2c.extension.ring-response :as rr]
            [f2c.web.app.community.repository :as repo]))

(defn handler [req]
  (let [communities (-> (individual-auth/current-individual req)
                       :individual/id
                       repo/fetch-communities)]
    (rr/html
    [:html 
     [:head 
      [:title "Dashboard"]]
     [:body 
      [:div 
       [:p (str "Hi " (:individual/name (individual-auth/current-individual req)))]
       [:p "Your communties"]
       [:ul 
        (map (fn [{:community/keys [id name is-facilitator]}]
               [:li {:data-id id
                     :data-is-facilitator is-facilitator} name])
             communities)]]]])))