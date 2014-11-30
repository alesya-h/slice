(ns slice.state
  (:require [reagent.core :as r]
            [clojure.zip :as zip]
            [historian.core :as hist :include-macros true]))

(def app-state (r/atom {}))

(defn enable-history! []
  (hist/record! app-state :app-state))

(defn current-state []
  @app-state)

(defn get-state [k & [default]]
  (get @app-state k default))

(defn put! [& pairs]
  (swap! app-state #(apply assoc % pairs)))

(defn put-in! [ks v]
  (swap! app-state assoc-in ks v))

(defn update-in! [ks f]
  (swap! app-state update-in ks f))

(def undo! hist/undo!)
(def redo! hist/redo!)

(defn dump []
  {:state (dissoc @app-state :document)
   :document (zip/root (get-state :document))})

(defn restore! [{:keys [state document] :as data}]
  (reset! app-state
          (assoc state :document
                 (zip/xml-zip document))))
