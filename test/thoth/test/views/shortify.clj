(ns thoth.test.views.shortify
  (:use [thoth.test.mongo-cleaner :only (with-test-mongo)])
  (:use [thoth.config :only (check-for-test-env!)])
  (:use thoth.views.shortify)
  (:use noir.util.test)
  (:use [clojure.test]))

(check-for-test-env!)

(deftest shortify-with-no-auth
  (with-test-mongo
    (-> (send-request [:post "/shortify"])
        (has-status 401)
        (has-body "You need a valid auth token to access this resource."))
    (-> (send-request [:post "/shortify"] {"url" "www.google.com"})
        (has-status 401)
        (has-body "You need a valid auth token to access this resource."))))
 
(deftest shortify-with-no-url
  (with-test-mongo
    (-> (send-request [:post "/shortify"] {"auth" "12345678"})
        (has-status 422)
        (has-body "You must provide an url parameter."))))

(deftest shortify-with-url
  (with-test-mongo
    (-> (send-request [:post "/shortify"] 
                      {"url" "www.google.com" "auth" "12345678"})
        (has-status 200)
        (has-body "{\"shortened_url\":\"0\",\"url\":\"http://www.google.com\"}"))
    (-> (send-request [:post "/shortify"] 
                      {"url" "www.github.com" "auth" "12345678"})
        (has-status 200)
        (has-body "{\"shortened_url\":\"1\",\"url\":\"http://www.github.com\"}"))
    (-> (send-request [:post "/shortify"] 
                      {"url" "http://www.google.com" "auth" "12345678" }) ;; Do not create a new record
        (has-status 200)
        (has-body "{\"shortened_url\":\"0\",\"url\":\"http://www.google.com\"}"))))
