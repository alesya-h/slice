(ns slice.layers.overlay
  (:require [slice.state :as st]))

(defn overlay []
  [:div.overlay.layer
   [:div.tools (st/state-str)]])
