(ns slice.sync
  (:require [slice.state :as st]
            [clojure.zip :as zip]
            [historian.core :as hist]
            [slice.css :as css]
            [slice.document :as doc]
            [slice.components.document :as cdoc]
            [ajax.core :refer [GET POST]]
            [ajax.edn :as ajax-edn]))

(defn dump []
  {:state (dissoc @st/app-state :document)
   :document (-> (doc/get-document)
                 (doc/edit-protected doc/make-non-current)
                 zip/root)
   :html (cdoc/document)
   :css (css/stylesheet)})

(defn restore! [{:keys [state document] :as data}]
  (reset! st/app-state
          (assoc state :document
                 (zip/xml-zip document))))

(defn load! []
  (GET "/state"
       {:handler restore!
        :timeout 10000
        :response-format (ajax-edn/edn-response-format)}))

(defn handler [response]
  (.log js/console (str response)))

(defn save! []
  (POST "/state"
        {:params (dump)
         :handler handler
         :format (ajax-edn/edn-request-format)
         :timeout 10000
         :response-format :raw}))

(defn save-if-recording! []
  (if hist/*record-active*
    (save!)))

(defn setup! []
  (load!)
  (add-watch st/app-state :auto-save save-if-recording!))
