(ns slice.input.mouse
  (:require [slice.state :as st]))

(defn mouse-down [evt]
  (st/put! :mouse-old {:x (.-screenX evt)
                       :y (.-screenY evt)}
           :mouse-moving true))

(defn mouse-up [_]
  (st/put! :mouse-moving false))

(defn mouse-move [f]
  (if (st/get-state :mouse-moving)
    (fn [evt]
      (let [{old-x :x old-y :y} (st/get-state :mouse-old)
            x (.-screenX evt)
            y (.-screenY evt)
            dx (- x old-x)
            dy (- y old-y)]
        (st/put-in! [:mouse-old :x] x)
        (st/put-in! [:mouse-old :y] y)
        (f dx dy)))))
