(ns slice.config
  (:require [slice.document :as d]
            [slice.css :as css]
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
           :mode :css
           :layers [:image
                    :document
                    :overlay]
           :document nil
           :stylesheet nil})

  (kb/defkbmap :html
    { #{} {
           "Space" #(l/toggle-layer :overlay)
           "o" #(d/change! zip/append-child d/new-div)
           "s" #(kb/change-mode :css)
           "t" #(d/set-text!)
           "c" #(d/set-classes!)
           }

      #{:shift} {
                 "o" #(d/change! zip/insert-child d/new-div)
                 "u" #(st/undo!)
                 "r" #(st/redo!)
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
           "Space" #(l/toggle-layer :overlay)
           "h" #(kb/change-mode :html)
           "u" #(css/set-unit!)
           "c" #(css/set-value!)
           "o" #(css/new-rule!)
           "n" #(css/new-rule!)
           "d" #(css/delete-rule!)
           "x" #(css/delete-rule!)
           }
      #{:shift} {
                 "u" #(st/undo!)
                 "r" #(st/redo!)
                 "Left" #(css/dec!)
                 "Right" #(css/inc!)
                 "Up" #(css/prev-rule!)
                 "Down" #(css/next-rule!)
                 }}))

(defn setup! []
  (configure!)
  (kb/setup!)
  (d/new-document)
  (d/change! d/edit-protected d/add-class "foo")
  (css/new-stylesheet!)
  (st/enable-history!))
