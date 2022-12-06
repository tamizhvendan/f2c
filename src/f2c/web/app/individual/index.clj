(ns f2c.web.app.individual.index
  (:require [f2c.community :as community]
            [f2c.community.repository :as community-repo]
            [f2c.extension.ring-response :as rr]
            [f2c.web.app.individual.auth :as individual-auth]
            [f2c.extension.reitit :as r]))

(defn handler [req]
  (let [current-individual-id (:individual/id (individual-auth/current-individual req))
        communities (community-repo/fetch-communities current-individual-id)]
    (rr/html
     [:html
      [:head
       [:title "Dashboard"]]
      [:body
       [:div
        [:p (str "Hi " (:individual/name (individual-auth/current-individual req)))]
        [:p "Your communties"]
        [:ul
         (map (fn [{:community/keys [id name] :as community}]
                [:li
                 [:p
                  [:span name]
                  (when (community/is-facilitator community current-individual-id)
                    [:a {:style {:margin-left "1rem"}
                         :href (r/path req :route.community/new-order {:community-id id})} "Create Order"])]])
              communities)]]]])))