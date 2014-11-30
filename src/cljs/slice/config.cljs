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
           :tools {:x 50 :y 50}
           :mouse-moving false
           :mode :html
           :layers [:image
                    :document
                    :overlay]
           :document nil})

  (kb/defkbmap :html
    { #{} {
           "Space" #(u/alert "SPAAAAACE!")
           "u" #(st/undo!)
           "r" #(st/redo!)
           "i" #(d/change! zip/insert-child d/new-div)
           "a" #(d/change! zip/append-child d/new-div)
           "s" #(kb/change-mode :css)
           "t" #(d/set-text!)
           "c" #(d/set-classes!)
           }

      #{:shift} {
                 "o" #(l/toggle-layer :overlay)
                 "i" #(d/change! zip/insert-left  d/new-div)
                 "a" #(d/change! zip/insert-right d/new-div)
                 "t" #(d/set-tag-name!)
                 "Down"  #(d/change! zip/right)
                 "Up"    #(d/change! zip/left)
                 "Left"  #(d/change! zip/up)
                 "Right" #(d/change! zip/down)
                 }
      #{:ctrl} {
                "x" #(d/cut!)
                "c" #(d/copy!)
                "v" #(d/paste-first!)
                }
      #{:ctrl :shift} {
                       "v" #(d/paste-last!)
                       }})

  (kb/defkbmap :css
    { #{} {
           "Space" #(u/alert "SPAAAAACE!")
           "u" #(st/undo!)
           "r" #(st/redo!)
           "h" #(kb/change-mode :html)
           }

      #{:ctrl} {
                "o" #(l/toggle-layer :overlay)
                }}))

(defn setup! []
  (configure!)
  (kb/setup!)
  (d/new-document)
  (st/enable-history!))
