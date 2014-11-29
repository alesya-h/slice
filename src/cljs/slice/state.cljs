(ns slice.state
  (:require [reagent.core :as r]
            [historian.core :as hist]))

(def app-state (r/atom {}))

(defn enable-history! []
  (hist/record! app-state :app-state))

(defn state-str []
  (pr-str @app-state))

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
