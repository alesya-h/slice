(ns slice.components.layers
  (:require [slice.state :as st]
            [slice.components.image :as image]
            [slice.components.overlay :as overlay]
            [slice.components.document :as document]))

(def layers-db
  {:image image/component
   :document document/component
   :overlay overlay/component})

(defn get-layer-component [layer]
  [(layers-db layer)])

(defn component []
  (->> (st/get-state :layers)
       (map get-layer-component)
       (into [:div.sl__layers])))
