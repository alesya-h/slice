(ns slice.components.dom
  (:require [slice.document :as doc]))

(defn label [tag-name classes]
  (apply str
         (name tag-name)
         (map #(str %2 %) classes (repeat "."))))

(defn render-tag [the-tag]
  (if (associative? the-tag)
    (let [{:keys [tag classes current attrs content collapsed]} the-tag]
      [:div {:class (str "sl__tag" (if current " sl__current"))}
       [:span.sl__tag-name (label tag classes)]
       (if collapsed
         "..."
         (map render-tag content))])))

(defn component []
  [:div.sl__dom
   (render-tag (doc/document-root))])
