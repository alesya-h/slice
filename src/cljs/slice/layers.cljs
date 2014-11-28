(ns slice.layer
  (:require [slice.state :as st]
            [slice.layers.image :as image]
            [slice.layers.overlay :as overlay]
            [slice.layers.work :as work]))

(def layers-db
  {:image image/image-layer
   :work work/work-layer
   :overlay overlay/overlay})

(defn get-layer-component [layer]
  [(layers-db layer)])

(defn layers []
  (->> (st/get-state :layers)
       (map get-layer-component)
       (into [:div.layers])))

(defn layer-enabled? [layer]
  ((set (st/get-state :layers)) layer))

(defn disable-layer [layer]
  (st/update-in! [:layers] #(vec (remove #{layer} %))))

(defn enable-layer [layer]
  (if-not (layer-enabled? layer)
    (st/update-in! [:layers] #(conj % layer))))

(defn toggle-layer [layer]
  (if (layer-enabled? layer)
    (disable-layer layer)
    (enable-layer layer)))

(defn pop-layer [layer]
  (disable-layer layer)
  (enable-layer layer))
