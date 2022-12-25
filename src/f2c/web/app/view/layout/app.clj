(ns f2c.web.app.view.layout.app
  (:require [f2c.extension.reitit :as r]
            [f2c.web.app.view.layout.base :as layout]
            [f2c.extension.rum :as rum]
            [f2c.extension.format :as fmt]))

(defn- menu-item [icon-class text url]
  [:a {:class "py-3 px-3.5 space-y-0.5 text-gray-600 hover:text-primary-700 flex items-center"
       :href url}
   [:i {:class (str "text-lg mr-1.5 mt-1 " icon-class)}]
   [:span {:class "text-sm "} text]])

(defn- render-individual-menu [req]
  [:div {:x-show "open"
         :x-transition:enter "transition ease-out duration-200" :x-transition:enter-start "opacity-0 scale-95" :x-transition:enter-end "opacity-100 scale-100"
         :x-transition:leave "transition ease-in duration-75" :x-transition:leave-start "opacity-100 scale-100" :x-transition:leave-end "opacity-0 scale-95"
         :style {:display "none"}
         :class "absolute right-0 w-60 mt-1 rounded-md shadow-lg
                 border border-solid border-gray-200
                 text-sm divide-y divide-gray-100 bg-white z-50"}
   [:p {:class "py-3 px-3.5 space-y-0.5"}
    [:span {:class "block text-xs font-normal text-gray-600"} "Signed in as"]
    [:span {:class "block font-semibold text-on-surface-primary truncate"} (get-in req [:current-individual :individual/name])]]
   (menu-item "ri-community-line" "My Communities" (r/path req :route.individual/index))])

(defn- render-individual-menu-section [req]
  [:div {:x-data "{'open' : false}" "@click.away" "open = false"
         :class "relative"}
   [:button {:type "submit" :class "shrink-0 flex items-center text-gray-500"
             :x-on:click "open = !open"}
    [:span {:class "sr-only"} "Open profile menu"]
    [:span {:class "h-8 w-8 rounded-full inline-flex items-center justify-center border border-solid border-gray-500"}
     [:span {:class "text-xs font-semibold text-gray-600 leading-none"} (fmt/initial (get-in req [:current-individual :individual/name]))]]
    [:i {:class "ri-arrow-down-s-line text-sm"}]]
   (render-individual-menu req)])

(defn- render-body [req body]
  [[:div {:class "flex items-center justify-between"}
    [:a {:href (r/path req :route.individual/index)}
     [:img {:class "h-16 w-16 -ml-2 object-contain" :src "/assets/web/images/logo.png"}]]
    (render-individual-menu-section req)]
   (rum/element :main {:class ""} body)])

(defn render [req title body]
  (layout/render title (render-body req body) {:class "px-4 py-2 md:max-w-2xl md:mx-auto bg-gray-50 relative"}))