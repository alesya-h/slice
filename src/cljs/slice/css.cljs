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
        (for [[attribute {:keys [value unit]}] rules]
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
          :rules [["border" {:value "1px black solid"}]
                  ["margin" {:value 42 :unit "px"}]
                  ["border" {:value 10 :unit "px"}]]}})

(defn new-stylesheet! []
  (put-stylesheet! sample))

(defn add-rule [style attribute value]
  (let [new-idx (count (:rules style))]
    (as-> style %
          (update-in % [:rules] conj [attribute {:value value}])
          (assoc-in  % [:idx] new-idx))))

(defn set-unit [{:keys [idx rules] :as style} unit]
  (if idx
    (assoc-in style [:rules idx 1 :unit] unit)
    style))

(defn set-value [{:keys [idx rules] :as style} value]
  (if idx
    (assoc-in style [:rules idx 1 :value] value)
    style))

(defn navigate-rules [style direction]
  (let [{:keys [idx rules]} style
        max-idx (dec (count rules))
        new-idx (direction idx)
        result-idx (u/in-bounds 0 new-idx max-idx)]
    (assoc style :idx result-idx)))

(def blank-style {:rules []})

(defn update-style [stylesheet class f args]
  (let [style (get stylesheet class blank-style)
        new-style (apply f style args)]
    (assoc stylesheet class new-style)))

(defn change-style! [f & args]
  (-> (get-stylesheet)
      (update-style (current-class) f args)
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

(defn current-class []
  (first (doc/current-classes)))

(defn current-style []
  ((get-stylesheet) (current-class)))

(defn current-rule []
  (let [{:keys [idx rules]} (current-style)]
    (get rules idx)))

(defn current-unit []
  (:unit (current-rule)))

(defn current-value []
  (:value (current-rule)))
