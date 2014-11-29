(ns slice.components.overlay
  (:require [slice.state :as st]))

(defn component []
  [:div.overlay.layer
   [:div.tools (st/state-str)]])
