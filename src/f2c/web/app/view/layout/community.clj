(ns f2c.web.app.view.layout.community
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.default :as layout]))

(defn- render-nav-link [title path page-name current-page-name]
  (let [default-class "px-2 py-1 font-medium text-sm"
        link-props {:class (str default-class " cursor-pointer no-underline hover:text-primary-900")
                    :href path}
        non-link-props {:class (str default-class " border-b-2 border-primary-800 text-primary-800")
                        :aria-current "page"}
        is-current-page (= page-name current-page-name)]
    [:li [(if is-current-page :span :a)
          (if is-current-page non-link-props link-props)
          title]]))

(defn render [req children current-page-name]
  (let [{:community/keys [id name]} (:current-community req)]
    (layout/render (str name " - Community")
                   (vec (concat [:main {:class "p-4 md:max-w-lg md:mx-auto"}

                                 [:div {:class "flex items-center justify-between"}
                                  [:a {:href (r/path req :route.individual/index) :class "flex-1"}
                                   [:img {:class "h-16 w-16 -ml-2" :src "/assets/web/images/logo.png"}]]
                                  [:nav {:class "mt-2"}
                                   [:ul {:class "flex space-x-2"}
                                    (render-nav-link "Home" (r/path req :route.community/index {:community-id id})
                                                     :home current-page-name)
                                    (render-nav-link "Catalog" (r/path req :route.community.catalog/index {:community-id id})
                                                     :catalog current-page-name)]]]

                                 [:p {:class "mt-2 font-medium font-display"} name]

                                 [:div {:class "mt-4"}]]
                                children)))))