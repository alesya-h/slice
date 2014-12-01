(ns slice.state
  (:require [reagent.core :as r]
            [historian.core :as hist :include-macros true]))

(def app-state (r/atom {}))

(defn disable-history! []
  (if (and hist/overseer (not (empty? @hist/overseer)))
    (hist/stop-record! :app-state)))

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
