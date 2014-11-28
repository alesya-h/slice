(ns slice.core
  (:require [reagent.core :as reagent]
            [slice.layer :as l]
            [slice.document :as d]
            [slice.util :as u]
            [slice.events :as evt]
            [clojure.zip :as zip]))

(defn main-page []
  [l/layers])

(defn kbd-handler [e]
  (let [key (.-key e)
        code (.-keyCode e)]
    (u/log "keydown " key code)
    (case key
      " " (u/alert "SPAAAAACE!")
      "O" (l/toggle-layer :overlay)
      "I" (l/toggle-layer :image)
      "W" (l/toggle-layer :work)
      "o" (l/pop-layer :overlay)
      "i" (l/pop-layer :image)
      "w" (l/pop-layer :work)
      "Down"  (d/change-current zip/down)
      "Up"    (d/change-current zip/up)
      "Left"  (d/change-current zip/prev)
      "Right" (d/change-current zip/next)
      nil)))

(defn init! []
  (reagent/render-component [main-page]
                      (js/document.getElementById "app"))
  (evt/setup-keyboard-handler! kbd-handler))
