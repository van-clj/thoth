(ns thoth.test.mongo-cleaner 
  (:use [thoth.config :only (mongo-conn)])
  (:use somnium.congomongo))


(defn- drop-test-collections! []
  (doseq [^String coll (collections)]
    (cond (= coll "shortenedurls") (drop-coll! coll)
          (= coll "idspool") (drop-coll! coll))))

(defn teardown! []
    (with-mongo mongo-conn
      (drop-test-collections!)))

(defmacro with-test-mongo [& body]
  `(do
     (teardown!)
     ~@body
     (teardown!)))

