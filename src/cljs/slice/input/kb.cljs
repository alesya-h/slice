(ns slice.input.kb
  (:require [slice.util :as u]
            [slice.state :as st]))

(defn evt->key [e]
  [(disj #{(if (.-ctrlKey e) :ctrl)
           (if (.-shiftKey e) :shift)
           (if (.-altKey e) :alt)} nil)
   (code->key (.-keyCode e))])

(def code->key
  {13 "Enter"
   27 "Esc"
   32 "Space"
   37 "Left"
   38 "Up"
   39 "Right"
   40 "Down"
   45 "Insert"
   46 "Delete"
   48 "0"
   49 "1"
   50 "2"
   51 "3"
   52 "4"
   53 "5"
   54 "6"
   55 "7"
   56 "8"
   57 "9"
   65 "a"
   66 "b"
   67 "c"
   68 "d"
   69 "e"
   70 "f"
   71 "g"
   72 "h"
   73 "i"
   74 "j"
   75 "k"
   76 "l"
   77 "m"
   78 "n"
   79 "o"
   80 "p"
   81 "q"
   82 "r"
   83 "s"
   84 "t"
   85 "u"
   86 "v"
   87 "w"
   88 "x"
   89 "y"
   90 "z"
   186 ";"
   188 ","
   190 "."
   192 "~"
   })

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
    (js/console.log (.-keyCode e))
    (run-kb-map kb-map keys))
  (.stopPropagation e))

(defn change-mode [mode]
  (st/put! :mode mode))

(defn setup! []
  (set! (.-onkeydown js/document.body) handler))
