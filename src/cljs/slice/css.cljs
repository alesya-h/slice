(ns slice.css
  (:require [slice.state :as st]
            [slice.document :as doc]
            [slice.util :as u]
            [garden.core :as g]))

(defn get-stylesheet []
  (st/get-state :stylesheet))

(defn put-stylesheet! [stylesheet]
  (st/put! :stylesheet stylesheet))

(defn to-garden-style [rules]
  (into {}
        (for [{:keys [attribute value unit]} rules]
          [attribute (str value unit)])))

(defn to-garden-css [stylesheet]
  (->> stylesheet
       (map (fn [[k v]]
              [(str "." k) (to-garden-style (:rules v))]))
       (into [])))

(defn to-css [stylesheet]
  (-> stylesheet
      to-garden-css
      g/css))

(defn stylesheet []
  (to-css (get-stylesheet)))

(def sample
  {"foo" {:idx 0
          :rules [{:attribute "border" :value "1px black solid"}
                  {:attribute "margin" :value 42 :unit "px"}
                  {:attribute "border" :value 10 :unit "px"}]}})

(defn set-active-class! [n]
  (st/put! :active-class n))

(defn active-class []
  (st/get-state :active-class))

(defn new-stylesheet! []
  (put-stylesheet! sample))

(defn find-current-class [prev counter seq]
  (cond
   (empty? seq) prev
   (<= counter 0) (first seq)
   :else (recur (first seq) (dec counter) (rest seq))))

(defn current-class []
  (find-current-class nil (active-class) (doc/current-classes)))

(defn current-style []
  ((get-stylesheet) (current-class)))

(defn current-rule []
  (let [{:keys [idx rules]} (current-style)]
    (get rules idx)))

(defn current-unit []
  (:unit (current-rule)))

(defn current-value []
  (:value (current-rule)))

(defn add-rule [style attribute value]
  (let [new-idx (count (:rules style))]
    (as-> style %
          (update-in % [:rules] conj {:attribute attribute :value value})
          (assoc-in  % [:idx] new-idx))))

(defn set-unit [{:keys [idx rules] :as style} unit]
  (if idx
    (assoc-in style [:rules idx :unit] unit)
    style))

(defn update-value [{:keys [idx rules] :as style} f]
  (if idx
    (update-in style [:rules idx :value] f)
    style))

(defn set-value [{:keys [idx rules] :as style} value]
  (if idx
    (assoc-in style [:rules idx :value] value)
    style))

(defn fix-value [value]
  (if (= (str (int value)) value)
    (int value)
    value))

(defn fix-style-value [{:keys [idx rules] :as style} value]
  (if idx
    (update-in style [:rules idx :value] fix-value)
    style))

(defn delete-rule [{:keys [idx rules] :as style}]
  (if idx
    (assoc style
      :rules (u/vec-remove rules idx)
      :idx (u/in-bounds 0 (dec idx) (count rules)))
    style))

(defn navigate-rules [{:keys [idx rules] :as style} direction]
  (if (empty? rules)
    style
    (let [max-idx (dec (count rules))
          new-idx (direction idx)
          result-idx (u/in-bounds 0 new-idx max-idx)]
      (assoc style :idx result-idx))))

(def blank-style {:rules []})

(defn update-style [stylesheet class f args]
  (let [style (get stylesheet class blank-style)
        new-style (apply f style args)]
    (assoc stylesheet class new-style)))

(defn change-style! [f & args]
  (-> (get-stylesheet)
      (update-style (current-class) f args)
      (update-style (current-class) fix-style-value args)
      (put-stylesheet!)))

(defn new-rule! []
  (let [attribute (u/ask! "Attribute:" "")
        value (u/ask! "Value:" "")]
    (change-style! add-rule attribute value)))

(defn prev-rule! []
  (change-style! navigate-rules dec))

(defn next-rule! []
  (change-style! navigate-rules inc))

(defn set-value! []
  (change-style! set-value (u/ask! "Value:" (current-value))))

(defn set-unit! []
  (change-style! set-unit (u/ask! "Unit:" (current-unit))))

(defn delete-rule! []
  (change-style! delete-rule))

(defn inc! []
  (change-style! update-value inc))

(defn dec! []
  (change-style! update-value dec))
