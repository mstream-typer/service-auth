(ns typer-service-auth.service
  (:require [clj-time.core :as time]
            [io.pedestal.http :as http]
            [pedestal-api.core :as api]
            [pedestal-api.helpers :as api-helpers]
            [schema.core :as s]
            [buddy.sign.jwt :as jwt]))


(s/defschema TokenResponse
  {:token s/Str})


(s/defschema UserCredentials
  {:username s/Str
   :password s/Str})


(defn generate-token [user]
  (let [iat (time/now)
        claims {:iss "typer-service-auth"
                :sub (:id user)
                :iat iat
                :exp (time/plus iat
                                (time/seconds 60))}]
    (jwt/sign claims
              "secret")))


(api-helpers/defhandler login
  {:summary "Generate an auth token"
   :parameters {:body-params UserCredentials}
   :responses {200 {:body TokenResponse}}}
  [request]
  {:status 200
   :body {:token (generate-token {:id "1234"})}})


(s/with-fn-validation
  (api/defroutes routes
    {:info {:title "Auth service"
            :description ""
            :version "2.0"}
     :tags [{:name "auth"
             :description  ""}]}
    [[["/"
       ^:interceptors [api/error-responses
                       (api/negotiate-response)
                       (api/body-params)
                       api/common-body
                       (api/coerce-request)
                       (api/validate-response)]
       ["/api/auth/login"
        ^:interceptors [(api/doc {:tags ["auth"]})]
        {:post login}]
       ["/swagger.json"
        {:get api/swagger-json}]
       ["/*resource"
        {:get api/swagger-ui}]]]]))


(def auth
  {::http/routes #(deref #'routes)
   ::http/router :linear-search
   ::http/resource-path "/public"
   ::http/type :jetty
   ::http/join? false})
