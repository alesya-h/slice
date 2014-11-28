(ns slice.document
  (:require [slice.state :as st]
            [clojure.zip :as zip]
            [clojure.string :as str])
  (:require-macros [slice.document :as m]))

(defn get-document []
  (st/get-state :document))

(defn put-document [document]
  (st/put! :document document))

(defn document-node []
  (zip/node (get-document)))

(defn document-root []
  (zip/root (get-document)))

(defn protected [zipper f]
  (or (f zipper) zipper))

(defn update-document [f]
  (-> (get-document)
      (protected f)
      (put-document)))

(defn add-class-str [classes-string class]
  (str/join " "
            (-> (str/split classes-string #"\s+")
                (set)
                (conj class))))

(defn remove-class-str [classes-string class]
  (->> (str/split classes-string #"\s+")
       (remove #{class})
       (str/join " ")))

(defn add-class [tag class]
  (update-in tag [:attrs :class]
             #(add-class-str % class)))

(defn remove-class [tag class]
  (update-in tag [:attrs :class]
             #(remove-class-str % class)))

(defn set-attr [tag attr value]
  (assoc-in tag [:attrs attr] value))

(defn update-attr [tag attr f]
  (update-in tag [:attrs attr] f))

(defn change-current [f]
  (m/with-document-zipper->
    (zip/edit #(remove-class % "current"))
    (protected f)
    (zip/edit #(add-class % "current"))))
