(ns thoth.test.models.shortener
  (:use [thoth.models.shortener])
  (:use [thoth.test.mongo-cleaner :only (with-test-mongo)])
  (:use [thoth.config :only (check-for-test-env!)])
  (:use [clojure.test]))

(check-for-test-env!)

(deftest  add-http-if-needed-test
  (is (= (add-http-if-needed "www.google.com") "http://www.google.com"))
  (is (= (add-http-if-needed "http://www.google.com") "http://www.google.com")))

(deftest generate-shortened-url-tests
  (with-test-mongo
    (is (= (generate-shortened-url!) "0"))
    (is (= (generate-shortened-url!) "1"))
    (is (= (generate-shortened-url!) "2")))) ;; increments one by one

(deftest insert-to-mongo-tests
  (with-test-mongo
    (let [result (insert-to-mongo! "0" "www.google.com")]
    (is (= (:_id result) "0")) ;; increments one by one
    (is (= (:url result) "www.google.com"))))) ;; increments one by one


(deftest create-url-tests
  (with-test-mongo
    (let [url-0 (create-url! "http://www.google.com")
          url-1 (create-url! "http://www.github.com")
          url-2 (create-url! "www.test.com")]
    (is (= (:_id url-0) "0"))
    (is (= (:url url-0) "http://www.google.com"))
    (is (= (:_id url-1) "1"))
    (is (= (:url url-1) "http://www.github.com"))
    (is (= (:_id url-2) "2"))
    (is (= (:url url-2) "http://www.test.com")))))

(deftest get-or-create-url-tests
  (with-test-mongo
    (let [url-0 (get-or-create-url! "http://www.google.com")
          url-1 (get-or-create-url! "www.google.com")
          url-2 (get-or-create-url! "http://www.google.com")]
    (is (= (:_id url-0) "0"))
    (is (= (:url url-0) "http://www.google.com"))
    (is (= (:_id url-1) "0"))
    (is (= (:url url-1) "http://www.google.com"))
    (is (= (:_id url-2) "0"))
    (is (= (:url url-2) "http://www.google.com")))))

(deftest get-record-from-id-tests
  (with-test-mongo
    (create-url! "http://www.google.com")
    (create-url! "http://www.github.com")
    (is (= (:url (get-record-from-id "0")) "http://www.google.com"))
    (is (= (:count (get-record-from-id "0")) nil))
    (is (= (:url (get-record-from-id "1")) "http://www.github.com"))
    (is (= (:count (get-record-from-id "0")) nil))))

(deftest get-url-from-id-and-incr-tests
  (with-test-mongo
    (create-url! "http://www.google.com")
    (create-url! "http://www.github.com")
    (is (= (get-url-from-id-and-incr! "0") "http://www.google.com"))
    (is (= (:count (get-record-from-id "0")) 1))
    (get-url-from-id-and-incr! "0")
    (is (= (:count (get-record-from-id "0")) 2))
    (is (= (get-url-from-id-and-incr! "1") "http://www.github.com"))
    (is (= (:count (get-record-from-id "1")) 1))
    (get-url-from-id-and-incr! "1")
    (is (= (:count (get-record-from-id "1")) 2))))

;; This test is because of a small bug I found when incremeting the count.
;; If an id is not in the database, it shouldn't add any new records.
(deftest get-url-from-id-and-incr-doesnt-create-records
  (with-test-mongo
    (is (= (get-url-from-id-and-incr! "1") nil))
    (is (= (:_id (get-record-from-id "1")) nil))))




(deftest get-record-from-url-tests
  (with-test-mongo
    (create-url! "http://www.google.com")
    (create-url! "http://www.github.com")
    (is (= (get-record-from-url "http://www.google.com") {:_id "0", :url "http://www.google.com"}))
    (is (= (get-record-from-url "http://www.github.com") {:_id "1", :url "http://www.github.com"}))
    (is (= (get-record-from-url "www.google.com") {:_id "0", :url "http://www.google.com"}))
    (is (= (get-record-from-url "www.github.com") {:_id "1", :url "http://www.github.com"}))))
