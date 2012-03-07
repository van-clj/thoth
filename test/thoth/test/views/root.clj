(ns thoth.test.views.get
  (:use [thoth.config :only (check-for-test-env!)])
  (:use thoth.views.root)
  (:use noir.util.test)
  (:use [clojure.test]))

(check-for-test-env!)

(deftest get-root-url-should-be-404
  (-> (send-request "/")
      (has-status 404)
      (has-body "The url provided coudln't be found")))

