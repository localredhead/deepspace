(defproject deepspace "0.0.3"
  :description "A minimal Clojure(Script) IPFS library for the browser"
  :url "https://github.com/localredhead/deepspace"
  :dependencies [[org.clojure/clojure "1.9.0-alpha8"]
                 [org.clojure/clojurescript "1.9.225"]
                 [org.clojure/core.async "0.2.385"]
                 [funcool/cuerdas "0.8.0"]
                 [funcool/promesa "1.5.0"]
                 [funcool/httpurr "0.6.1"]]
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.4-7"]]
  :profiles {:dev  {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                   [figwheel-sidecar "0.5.4-7"]
                                   [org.clojure/tools.nrepl "0.2.12"]]}
             :repl {:plugins      [[refactor-nrepl "2.0.0-SNAPSHOT"]
                                   [cider/cider-nrepl "0.11.0-SNAPSHOT"]]}}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :figwheel     {:server-port 3449}
  :cljsbuild    {:builds {:dev {:source-paths ["src"]
                                :figwheel     true
                                :compiler     {:optimizations :none
                                               :main          deepspace.core
                                               :asset-path    "js/out"
                                               :output-to     "resources/public/js/deepspace.js"
                                               :output-dir    "resources/public/js/out"
                                               :pretty-print  true}}}}
  :source-paths  ["src" "target/classes"]
  :clean-targets ["out" "release" "target"]
  :auto          {:default {:file-pattern #"\.(clj|cljs|cljx|edn|sol)$"}
                  :paths   [ "src" "test"]})
