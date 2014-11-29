(ns slice.config
  (:require [slice.document :as d]
            [slice.state :as st]
            [slice.layers :as l]
            [slice.util :as u]
            [clojure.zip :as zip]
            [slice.input.kb :as kb]))

(defn configure! []
  (reset! st/app-state
          {:image {:x 0 :y 0 :src "/images/home_signed.jpg"}
           :mouse-old {:x 0 :y 0}
           :mouse-moving false
           :mode :document
           :layers [:image
                    :document
                    :overlay]
           :document nil})

  (kb/defkbmap :document
    { #{} {
           " " #(u/alert "SPAAAAACE!")
           "o" #(l/toggle-layer :overlay)
           "u" #(st/undo!)
           "r" #(st/redo!)
           }

      #{:ctrl} {
                "Down"  #(d/change-current zip/down)
                "Up"    #(d/change-current zip/up)
                "Left"  #(d/change-current zip/prev)
                "Right" #(d/change-current zip/next)
                }})

  (kb/defkbmap :css
    { #{} {} }))

(defn setup! []
  (configure!)
  (kb/setup!)
  (d/new-document)
  (st/enable-history!))
