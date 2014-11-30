(ns slice.handler
  (:require [slice.dev :refer [browser-repl start-figwheel]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [selmer.parser :refer [render-file]]
            [environ.core :refer [env]]
            [prone.middleware :refer [wrap-exceptions]]))

(defroutes routes
  (GET "/" [] (render-file "templates/index.html" {:dev (env :dev?)}))

  (GET "/state" [] (slurp "state.edn"))
  (POST "/state" {:keys [body params]}
        (spit "state.edn" (slurp body))
        "saved")

  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (wrap-defaults routes api-defaults)]
    (if (env :dev?) (wrap-exceptions handler) handler)))
