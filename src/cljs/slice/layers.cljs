(ns slice.layers
  (:require [slice.state :as st]))

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
