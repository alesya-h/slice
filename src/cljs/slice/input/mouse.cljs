(ns slice.input.mouse
  (:require [slice.state :as st]
            [historian.core :as hist :include-macros true]))

(defn mouse-down [evt]
  (hist/off-the-record
   (st/put! :mouse-old {:x (.-screenX evt)
                        :y (.-screenY evt)}
            :mouse-moving true)))

(defn mouse-up [_]
  (st/put! :mouse-moving false)
  (hist/trigger-record!))

(defn mouse-move [f]
  (if (st/get-state :mouse-moving)
    (fn [evt]
      (let [{old-x :x old-y :y} (st/get-state :mouse-old)
            x (.-screenX evt)
            y (.-screenY evt)
            dx (- x old-x)
            dy (- y old-y)]
        (hist/off-the-record
         (st/put! :mouse-old {:x x :y y})
         (f dx dy))))))
