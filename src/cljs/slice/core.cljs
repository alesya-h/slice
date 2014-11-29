(ns slice.core
  (:require [reagent.core :as reagent]
            [slice.components.layers :as layers]
            [slice.config :as config]))

(defn init! []
  (config/setup!)
  (reagent/render-component
   [layers/component]
   (js/document.getElementById "app")))
