(defproject slice "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [reagent "0.6.0"]
                 ;; [reagent-utils "0.1.0"]
                 [hiccup "1.0.5"]
                 [cljs-ajax "0.7.3"]
                 [historian "1.1.1"]
                 [org.clojure/clojurescript "1.9.946"]
                 [com.cemerick/piggieback "0.2.2"]
                 [weasel "0.7.0"]
                 [ring "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [compojure "1.6.0"]
                 [selmer "1.11.3"]
                 [garden "1.3.3"]
                 [environ "1.1.0"]
                 [leiningen "2.7.1"]
                 [prone "1.1.4"]]

  :plugins [
            [lein-figwheel "0.5.14"]
            [lein-cljsbuild "1.1.7"]
            [lein-environ "1.1.0"]
            [lein-ring "0.12.2"]
            [cider/cider-nrepl "0.8.1"]
            [lein-asset-minifier "0.2.0"]]

  :ring {:handler slice.handler/app}

  :min-lein-version "2.5.0"

  :uberjar-name "slice.jar"

  :minify-assets
  {:assets
    {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :source-map    true ;"resources/public/js/out.js.map"
                                        :externs       ["react/externs/react.js"]
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :profiles {:dev {:repl-options {:init-ns slice.handler
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[ring-mock "0.1.5"]
                                  [ring/ring-devel "1.3.1"]
                                  [pjstadig/humane-test-output "0.6.0"]]

                   :plugins [[lein-figwheel "0.1.4-SNAPSHOT"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :css-dirs ["resources/public/css"]}

                   :env {:dev? true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]}}}}

             :uberjar {:hooks [leiningen.cljsbuild minify-assets.plugin/hooks]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                             {:source-paths ["env/prod/cljs"]
                                              :compiler
                                              {:optimizations :advanced
                                               :pretty-print false}}}}}

             :production {:ring {:open-browser? false
                                 :stacktraces?  false
                                 :auto-reload?  false}}})
