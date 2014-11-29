(ns slice.document
  (:require [slice.state :as st]
            [slice.util :as u]
            [clojure.zip :as zip]
            [clojure.string :as str]))

(defn get-document []
  (st/get-state :document))

(defn put-document [document]
  (st/put! :document document))

(defn make-tag [tag-name children]
  {:tag tag-name
   :classes #{}
   :attrs {}
   :current false
   :content children})

(defn make-empty-div []
  (make-tag :div []))

(defn new-document []
  (put-document
   (zip/xml-zip
    (make-current
     (make-tag :div
               [(make-empty-div)])))))

(defn document-node []
  (zip/node (get-document)))

(defn document-root []
  (zip/root (get-document)))

(defn protected [zipper f]
  (let [new-zipper (f zipper)]
    (if (zip/branch? new-zipper)
      new-zipper
      zipper)))

(defn add-class [tag class]
  (update-in tag [:classes] conj class))

(defn remove-class [tag class]
  (update-in tag [:classes] disj class))

(defn make-current [tag]
  (assoc tag :current true))

(defn make-non-current [tag]
  (assoc tag :current false))

(defn set-attr [tag attr value]
  (assoc-in tag [:attrs attr] value))

(defn update-attr [tag attr f]
  (update-in tag [:attrs attr] f))

(defn edit-protected [zipper f]
  (if (zip/branch? zipper)
    (zip/edit zipper f)
    zipper))

(defn change-current [f]
  (-> (get-document)
      (edit-protected make-non-current)
      (protected f)
      (edit-protected make-current)
      (put-document)))
