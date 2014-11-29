(ns slice.components.demo
  (:require [slice.document :as doc]))

(defn display-tag [tag]
  (into [(:tag tag) (:attrs tag)]
        (map display-tag (:content tag))))

(defn display-tags [tags]
  (mapv display-tag tags))

(defn component []
  [:div.work.layer
   (display-tag (doc/document-root))])
