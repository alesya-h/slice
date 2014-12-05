(ns slice.components.css
  (:require [slice.state :as st]
            [slice.css :as css]))

(defn mark-current [current-idx]
  (fn [idx {:keys [attribute unit value]}]
    [:tr {:class (if (= idx current-idx) "sl__current")}
     [:td (str attribute ": ")]
     [:td (str value unit)]]))

(defn render-rules [{:keys [idx rules]}]
  [:table
   [:tbody (map-indexed (mark-current idx) rules)]])

(defn component []
  [:div.sl__css
   (render-rules (css/current-style))])
