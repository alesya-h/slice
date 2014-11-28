(ns slice.layers.image
  (:require [slice.state :as st]
            [slice.events :as e]))

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

(defn image-layer []
  (let [{:keys [src x y]} (get-image)]
    [:div.image.layer
     {:on-mouse-down e/mouse-down
      :on-mouse-up   e/mouse-up
      :on-mouse-move (e/mouse-move move-image)
      :style {:background-position (str x "px " y "px")
              :background-image (str "url('" src "')")}}]))
