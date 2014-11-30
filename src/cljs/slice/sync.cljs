(ns slice.sync
  (:require [slice.state :as st]
            [ajax.core :refer [GET POST]]))

(defn setup! []
  (load!)
  (add-watch st/app-state :auto-save
             #(save!)))

(defn load! []
  (GET "/state"
       {:handler st/restore!
        :timeout 10000
        :response-format :edn}))

(defn handler [response]
  (.log js/console (str response)))

(defn save! []
  (POST "/state"
        {:params (st/dump)
         :handler save-handler
         :format :edn
         :timeout 10000
         :response-format :raw}))
