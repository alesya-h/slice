(ns slice.components.overlay
  (:require [slice.state :as st]
            [slice.document :as doc]))

(defn component []
  [:div.overlay.layer
   [:div.tools
    [:p.state (st/state-str)]
    [:p.document (pr-str (doc/document-root))]]])
