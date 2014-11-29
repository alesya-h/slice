(ns slice.input.kb
  (:require [slice.util :as u]
            [slice.state :as st]))

(defn evt->key [e]
  [(disj #{(if (.-ctrlKey e) :ctrl)
           (if (.-shiftKey e) :shift)
           (if (.-altKey e) :alt)} nil)
   (.-key e)])

(def mode-maps (atom {}))

(defn defkbmap [name mapping]
 (swap! mode-maps assoc name mapping))

(defn run-kb-map [mode-map keys]
  (if-let [f (get-in mode-map keys)]
    (f)))

(defn handler [e]
  (let [keys (evt->key e) ;; [#{:ctrl} "y"]
        mode (st/get-state :mode)
        kb-map (@mode-maps mode)]
    (u/logp keys)
    (run-kb-map kb-map keys)))

(defn setup! []
  (set! (.-onkeydown js/document.body) handler))
