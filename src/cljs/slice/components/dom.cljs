(ns slice.components.dom
  (:require [clojure.string :as str]
            [slice.document :as doc]))

(defn label [tag-name classes attrs]
  (let [tag-str (name tag-name)
        classes-str (apply str (map #(str %2 %) classes (repeat ".")))
        attrs-str (str/join "," (map (fn [[k v]] (str k "=" v)) attrs))]
    (str tag-str classes-str (if-not (empty? attrs-str)
                               (str "[" attrs-str "]")))))

(defn render-tag [the-tag]
  (if (associative? the-tag)
    (let [{:keys [tag classes current attrs content collapsed]} the-tag]
      [:div {:class (str "sl__tag" (if current " sl__current"))}
       [:span.sl__tag-name (label tag classes attrs)]
       (if collapsed
         "..."
         (map render-tag content))])))

(defn component []
  [:div.sl__dom
   (render-tag (doc/document-root))])
