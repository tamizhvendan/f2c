(ns f2c.web.app.individual.index 
  (:require [f2c.web.app.individual.auth :as individual-auth]
            [f2c.extension.ring-response :as rr]))

(defn handler [req]
  (rr/html
   [:html 
    [:head 
     [:title "Dashboard"]]
    [:body 
     [:div 
      [:p (str "Hi " (:individual/name (individual-auth/current-individual req)))]]]]))