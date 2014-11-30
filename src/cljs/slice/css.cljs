(ns slice.css
  (:require [slice.state :as st]
            [garden.core :as g]))

(defn stylesheet []
  (st/get-state :stylesheet))

{:margin {:value 42 :units "px" :children {:top "10pt"}}
 :border {:value 10 :units "px"
          :children {:bottom {:value nil
                              :units nil
                              :children {:color {:value "red"}}}}}}
