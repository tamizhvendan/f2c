(ns f2c.web.app.individual.index
  (:require [f2c.community :as community]
            [f2c.community.repository :as community-repo]
            [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.default :as layout]))

(defn handler [req]
  (let [{individual-id :individual/id individual-name :individual/name} (:current-individual req)
        communities (community-repo/fetch-communities individual-id)]
    (layout/render "Dashboard"
                   [:div
                    [:p (str "Hi " individual-name)]
                    [:p "Your communties"]
                    [:ul
                     (map (fn [{:community/keys [id name] :as community}]
                            (let [is-facilitator (community/is-facilitator community individual-id)]
                              [:li
                               [:p
                                (if is-facilitator
                                  [:a {:href (r/path req :route.community/index {:community-id id})} name]
                                  [:span name])]]))
                          communities)]])))