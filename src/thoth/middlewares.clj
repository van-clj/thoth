(ns thoth.middlewares
  (:require [noir.response :as response])
  (:use [thoth.config :only (config)]))

(defn non-authorized-request? [request]
  (reduce #(and %1 %2) `(~(= (:request-method request) :post) 
                         ~(= (:uri request) "/shortify") 
                         ~(not (= (:auth-token config) (:auth (:params request)))))))

(defn auth-required
  "Add require authorization for shortify action"
  [handler]
  (fn [request]
    (cond (non-authorized-request? request)
      (response/status 401 "You need a valid auth token to access this resource.")
    :else
      (handler request))))
      
