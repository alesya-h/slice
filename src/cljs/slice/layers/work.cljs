(ns slice.layers.work
  (:require [slice.document :as doc]))

(defn display-tag [tag]
  (into [(:tag tag) (:attrs tag)]
        (map display-tag (:content tag))))

(defn display-tags [tags]
  (mapv display-tag tags))

(defn work-layer []
  [:div.work.layer
   (display-tag (doc/document-root))])
