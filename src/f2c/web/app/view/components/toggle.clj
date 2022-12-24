(ns f2c.web.app.view.components.toggle)

(defn render [js-model-var-name js-on-click-handler]
  (let [id (gensym)]
    [:div {:class "flex items-center"
           :x-on:click js-on-click-handler}
     [:button {:type "button" :class "relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full
                                    border-2 border-transparent
                                    focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-2
                                    transition-colors duration-200 ease-in-out
                                    "
               :role "switch"
               :x-bind:aria-checked js-model-var-name
               :aria-labelledby id
               :x-bind:class (format "%s ? 'bg-primary-600' : 'bg-gray-200'" js-model-var-name)}
      [:span {:aria-hidden "true"
              :x-bind:class (format "%s ? 'translate-x-5' : 'translate-x-0'" js-model-var-name)
              :class "pointer-events-none inline-block h-5 w-5 transform rounded-full shadow
                      shadow ring-0 transition duration-200 ease-in-out bg-white"}]]]))