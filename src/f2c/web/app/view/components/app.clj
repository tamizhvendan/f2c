(ns f2c.web.app.view.components.app)

(defn community-order-state [state]
  (case state
    "open" [:span {:class "badge ~positive @low text-xs"} state]))