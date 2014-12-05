(ns slice.components.image
  (:require [slice.state :as st]
            [slice.input.mouse :as m]))

(defn get-image []
  (st/get-state :image))

(defn put-image [image]
  (st/put! :image image))

(defn move-image [dx dy]
  (let [image (get-image)
        {:keys [x y]} image]
    (put-image (assoc image
                 :x (+ x dx)
                 :y (+ y dy)))))

(defn component []
  (let [{:keys [src x y]} (get-image)]
    [:div.sl__image.sl__layer
     {:on-mouse-down m/mouse-down
      :on-mouse-up   m/mouse-up
      :on-mouse-move (m/mouse-move move-image)
      :style {:background-position (str x "px " y "px")
              :background-image (str "url('" src "')")}}]))
