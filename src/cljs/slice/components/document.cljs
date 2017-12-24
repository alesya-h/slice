(ns slice.components.document
  (:require [slice.document :as doc]
            [slice.css :as css]
            [slice.util :as u]
            [clojure.string :as str]))

(defn mark-current [tag]
  (if (:current tag)
    (doc/add-class tag "sl__current")
    tag))

(defn add-classes [tag]
  (let [{:keys [classes]} tag
        class-str (str/join " " classes)]
    (assoc-in tag [:attrs :class] class-str)))

(defn preprocess [tag]
  (-> tag
      mark-current
      add-classes))

(defn display-tag [the-tag]
  (if (associative? the-tag)
    (let [{:keys [tag attrs content]} (preprocess the-tag)]
      (into [tag attrs]
            (for [i (range (count content))]
              ^{:key i} (display-tag (get content i)))))
    the-tag))

(defn display-tags [tags]
  (mapv display-tag tags))

(defn document []
  (display-tag (doc/document-root)))

(defn component []
  [:div.sl__work.sl__layer
   [:style (css/stylesheet)]
   [document]])
