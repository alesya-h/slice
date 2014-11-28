(ns slice.state
  (:require #_[historian.core :as hist]
            [clojure.zip :as zip]
            [reagent.core :as r]))

(def app-state
  (r/atom
   {:image {:x 0 :y 0 :src "/images/home_signed.jpg"}
    :mouse-old {:x 0 :y 0}
    :mouse-moving false
    :layers [
             :image
             :work
             :overlay
             ]
    :document (zip/xml-zip
               {:tag :div :attrs {:class "current"}
                :content [{:tag :div
                           :content []}]})}))

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
