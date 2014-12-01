(ns slice.sync
  (:require [slice.state :as st]
            [clojure.zip :as zip]
            [slice.css :as css]
            [slice.components.document :as cdoc]
            [ajax.core :refer [GET POST]]))

(defn dump []
  {:state (dissoc @st/app-state :document)
   :document (zip/root (st/get-state :document))
   :html (cdoc/document)
   :css (css/stylesheet)})

(defn restore! [{:keys [state document] :as data}]
  (reset! st/app-state
          (assoc state :document
                 (zip/xml-zip document))))

(defn setup! []
  (load!)
  (add-watch st/app-state :auto-save
             #(save!)))

(defn load! []
  (GET "/state"
       {:handler restore!
        :timeout 10000
        :response-format :edn}))

(defn handler [response]
  (.log js/console (str response)))

(defn save! []
  (POST "/state"
        {:params (dump)
         :handler save-handler
         :format :edn
         :timeout 10000
         :response-format :raw}))
