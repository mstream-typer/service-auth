(defproject typer-service-auth "0.1.0-SNAPSHOT"


  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [buddy/buddy-sign "2.2.0"]
   [pedestal-api "0.3.1"]
   [io.pedestal/pedestal.service "0.5.2"]
   [io.pedestal/pedestal.jetty "0.5.2"]
   [ch.qos.logback/logback-classic "1.2.3"]]
  
  
  :plugins
  [[lein-ancient "0.6.10"]
   [lein-bikeshed "0.4.1"]
   [lein-kibit "0.1.6-beta2"]
   [macluck/lein-docker "1.3.0"]]


  :min-lein-version
  "2.5.3"


  :source-paths
  ["src/clj"
   "config"]


  :main
  ^{:skip-aot true} typer-service-auth.server

  
  :clean-targets
  ^{:protect false}
  ["target"]


  :profiles
  {:dev {:dependencies [[org.clojure/test.check "0.10.0-alpha2"]]
         :plugins []}
   :uberjar {:aot :all}}
  

  :docker
  {:image-name "typer/typer-service-auth"
   :dockerfile "Dockerfile"
   :build-dir  "."}


  :aliases
  {"build" ["do"
            ["bikeshed"]
            ["kibit"]
            ["deps"]
            ["uberjar"]
            ["docker" "build"]]})
