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
   :collapsed false
   :content children})

(defn add-class [tag class]
  (update-in tag [:classes] conj class))

(defn remove-class [tag class]
  (update-in tag [:classes] disj class))

(defn make-current [tag]
  (assoc tag :current true))

(defn make-non-current [tag]
  (assoc tag :current false))

(def empty-div (make-tag :div []))
(def new-div (add-class empty-div "new-div"))

(defn new-document []
  (put-document
   (zip/xml-zip
    (make-current
     (make-tag :div
               [empty-div])))))

(defn document-node []
  (zip/node (get-document)))

(defn document-root []
  (zip/root (get-document)))

(defn protected [zipper f]
  (let [new-zipper (f zipper)]
    (cond
     (nil? new-zipper) (do
                         (u/log "protection error")
                         zipper)
     (zip/branch? new-zipper) new-zipper
     (string? (zip/node new-zipper)) (zip/up new-zipper)
     :else (do
             (u/log "something is wrong")
             zipper))))

(defn edit-protected [zipper f & args]
  (if (zip/branch? zipper)
    (apply zip/edit zipper f args)
    zipper))

(defn change! [f & args]
  (-> (get-document)
      (edit-protected make-non-current)
      (protected #(apply f % args))
      (edit-protected make-current)
      (put-document)))

(defn copy! []
  (->> (document-node)
       (make-non-current)
       (st/put! :document-clip)))

(defn cut! []
  (copy!)
  (change! zip/remove))

(defn paste-first! []
  (change! zip/insert-child (st/get-state :document-clip)))

(defn paste-last! []
  (change! zip/append-child (st/get-state :document-clip)))

(defn collapse-or-expand-current! []
  (change! edit-protected update-in [:collapsed] not))

(defn set-tag-name! []
  (let [current-tag (:tag (document-node))
        new-tag (u/ask! "New tag name: " (name current-tag))]
    (if new-tag
      (change! edit-protected assoc :tag (keyword new-tag)))))

(defn set-classes! []
  (let [classes-str (str/join " " (current-classes))
        new-classes-str (u/ask! "New classes:" classes-str)]
    (if new-classes-str
     (change! edit-protected assoc
             :classes (str/split new-classes-str #"\s")))))

(defn add-text-first! []
  (if-let [text (u/ask! "Text: " (str content))]
    (change! zip/insert-child text)))

(defn add-text-last! []
  (if-let [text (u/ask! "Text: " (str content))]
    (change! zip/append-child text)))

(defn current-classes []
  (:classes (document-node)))
