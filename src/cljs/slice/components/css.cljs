(ns slice.components.css
  (:require [slice.state :as st]
            [slice.css :as css]))

(defn label [tag-name classes]
  (apply str
         (name tag-name)
         (map #(str %2 %) classes (repeat "."))))

(defn mark-current [current-idx]
  (fn [idx {:keys [attribute unit value]}]
    [:tr {:class (if (= idx current-idx) "current")}
     [:td (str attribute ": ")]
     [:td (str value unit)]]))

(defn render-rules [{:keys [idx rules]}]
  [:table
   [:tbody (map-indexed (mark-current idx) rules)]])

(defn component []
  [:div.css
   (render-rules (css/current-style))])
