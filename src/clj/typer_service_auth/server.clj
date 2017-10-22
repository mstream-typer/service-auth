(ns typer-service-auth.server
  (:gen-class)
  (:require [io.pedestal.http.cors :as cors]
            [io.pedestal.http :as http]
            [typer-service-auth.service :as service]))


(defonce service-instance nil)


(defn add-cors-config [service]
  (assoc service
         ::http/interceptors
         (cons (cors/allow-origin (constantly true))
               (::http/interceptors service))))


(defn add-port-config [service]
  (assoc service
         ::http/port
         8080))


(def service
  (-> service/auth
      (http/default-interceptors)
      (add-cors-config)
      (add-port-config)))


(defn create-server []
  (alter-var-root #'service-instance
                  (constantly (http/create-server service))))
                         

(defn start []
  (when-not service-instance
    (create-server))
  (http/start service-instance))


(defn stop []
  (when service-instance
    (http/stop service-instance)))


(defn -main [& args]
  (start))
