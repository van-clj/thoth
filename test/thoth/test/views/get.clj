(ns thoth.test.views.get
  (:use [thoth.config :only (check-for-test-env!)])
  (:use thoth.views.get)
  (:use noir.util.test)
  (:use [clojure.test]))

(check-for-test-env!)

(deftest get-with-unexistent-url
  (-> (send-request "/nonexistent")
      (has-status 404)
      (has-body "The url provided coudln't be found")))

