(ns slice.util)

(defn log [& args]
  (js/console.log (apply str args)))

(defn logp [& args]
  (js/console.log (apply pr-str args)))

(defn alert [& args]
  (js/alert (apply str args)))
