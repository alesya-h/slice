(ns slice.components.demo
  (:require [slice.document :as doc]))

(defn display-tag [tag]
  (if (associative? tag)
    (into [(:tag tag) (:attrs tag)]
          (map display-tag (:content tag)))
    tag))

(defn display-tags [tags]
  (mapv display-tag tags))

(defn component []
  [:div.work.layer
   (display-tag (doc/document-root))])
