(ns slice.config
  (:require [slice.document :as d]
            [slice.state :as st]
            [slice.layers :as l]
            [slice.util :as u]
            [slice.input.kb :as kb]))

(defn configure! []
  (reset! st/app-state
          {:image {:x 0 :y 0 :src "/images/home_signed.jpg"}
           :mouse-old {:x 0 :y 0}
           :mouse-moving false
           :mode :normal
           :layers [:image
                    :demo
                    :overlay]
           :document nil})

  (kb/defkbmap :normal
    { #{} {" "     #(u/alert "SPAAAAACE!")
           "O"     #(l/toggle-layer :overlay)
           "I"     #(l/toggle-layer :image)
           "D"     #(l/toggle-layer :demo)
           "o"     #(l/pop-layer :overlay)
           "i"     #(l/pop-layer :image)
           "d"     #(l/pop-layer :demo)
           "Down"  #(d/change-current zip/down)
           "Up"    #(d/change-current zip/up)
           "Left"  #(d/change-current zip/prev)
           "Right" #(d/change-current zip/next)}})

  (kb/defkbmap :edit
    { #{} {} }))

(defn setup! []
  (configure!)
  (kb/setup!)
  (d/new-document))
