(ns slice.config
  (:require [slice.document :as d]
            [slice.css :as css]
            [slice.state :as st]
            [slice.layers :as l]
            [slice.util :as u]
            [slice.sync :as sync]
            [clojure.zip :as zip]
            [slice.components.image :as img]
            [slice.input.kb :as kb]))

(defn configure! []
  (reset! st/app-state
          {:image {:x 0 :y 0 :src "/images/home_signed.jpg"}
           :mouse-old {:x 0 :y 0}
           :tools {:x 50 :y 50}
           :mouse-moving false
           :active-class 0
           :mode :html
           :layers [:image
                    :document
                    :overlay]
           :document nil
           :stylesheet nil})

  (kb/defkbmap :html
    { #{} {
           "Space" #(l/toggle-layer :overlay)
           "1" #(css/set-active-class! 0)
           "2" #(css/set-active-class! 1)
           "3" #(css/set-active-class! 2)
           "o" #(d/change! zip/append-child d/new-div)
           "s" #(kb/change-mode :css)
           "h" #(kb/change-mode :css)
           "t" #(d/add-text-last!)
           "c" #(d/set-classes!)
           "u" #(st/undo!)
           "x" #(d/cut!)
           "d" #(d/cut!)
           "f" #(d/collapse-or-expand-current!)
           "Down"  #(d/change! zip/down)
           "Up"    #(d/change! zip/up)
           "Left"  #(d/change! zip/left)
           "Right" #(d/change! zip/right)
           }

      #{:shift} {
                 "Right" #(kb/change-mode :css)
                 "o" #(d/change! zip/insert-child d/new-div)
                 "t" #(d/set-tag-name!)
                 "Down"  #(d/change! zip/next)
                 "Up"    #(d/change! zip/prev)
                 }
      #{:ctrl} {
                "r" #(st/redo!)
                "x" #(d/cut!)
                "c" #(d/copy!)
                "v" #(d/paste-first!)
                }
      #{:ctrl :shift} {
                       "v" #(d/paste-last!)
                       "Down"  #(img/move-image 0  1)
                       "Up"    #(img/move-image 0 -1)
                       "Left"  #(img/move-image -1 0)
                       "Right" #(img/move-image 1  0)
                       }})

  (kb/defkbmap :css
    { #{} {
           "Space" #(l/toggle-layer :overlay)
           "u" #(st/undo!)
           "h" #(kb/change-mode :html)
           "c" #(css/set-value!)
           "o" #(css/new-rule!)
           "n" #(css/new-rule!)
           "d" #(css/delete-rule!)
           "x" #(css/delete-rule!)
           "1" #(css/set-active-class! 0)
           "2" #(css/set-active-class! 1)
           "3" #(css/set-active-class! 2)
           "Left" #(css/dec!)
           "Right" #(css/inc!)
           "Up" #(css/prev-rule!)
           "Down" #(css/next-rule!)
           }
      #{:ctrl} {
                "r" #(st/redo!)
                }
      #{:shift} {
                 "Left" #(kb/change-mode :html)
                 "Down"  #(d/change! zip/next)
                 "Up"    #(d/change! zip/prev)
                 "u" #(css/set-unit!)
                 }}))

(defn setup! []
  (st/disable-history!)
  (configure!)
  (kb/setup!)
  (d/new-document)
  (d/change! d/edit-protected d/add-class "foo")
  (css/new-stylesheet!)
  (sync/setup!)
  (st/enable-history!))
