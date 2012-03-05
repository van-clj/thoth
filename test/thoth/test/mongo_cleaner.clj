(ns thoth.test.mongo-cleaner 
  (:use [thoth.config :only (mongo-conn)])
  (:use somnium.congomongo))

(defn teardown! []
    (with-mongo mongo-conn
      (drop-database! (.getName (mongo-conn :db)))))

(defmacro with-test-mongo [& body]
  `(do
     (teardown!)
     ~@body
     (teardown!)))

