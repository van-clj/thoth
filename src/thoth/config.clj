(ns thoth.config
  (:use somnium.congomongo))

(def production? 
  (= "production" (get (System/getenv) "APP_ENV")))

(def development? 
  (= "development" (get (System/getenv) "APP_ENV")))

(def test? 
  (= "test" (get (System/getenv) "APP_ENV")))

(defn set-mongo-url []
  (let [heroku-mongo (get (System/getenv) "MONGOHQ_URL")]
    (cond 
      production? heroku-mongo
      development? "mongodb://thoth:thoth@localhost:27017/thoth"
      test? "mongodb://thoth:thoth@127.0.0.1:27017/test"
      :else ((println "You must set APP_ENV environment variable to production, development or test")
             (System/exit 1)))))

(defn set-auth-token []
    (cond 
      production? "12345678"
      development? "12345678"
      test? "12345678"
      :else ((println "You must set APP_ENV environment variable to production, development or test")
             (System/exit 1))))

(defn check-for-test-env! []
  (when (not test?)
    (println (str "Do you want to blow your data?!. You are trying to run the tests in a " (get (System/getenv) "APP_ENV") " environment. Set APP_ENV to test in order to run the tests"))
    (System/exit 1)))

(def config
  {:mongo-url (set-mongo-url)
   :auth-token (set-auth-token)})

(defn split-mongo-url [url]
  "Parses mongodb url from heroku, eg. mongodb://user:pass@localhost:1234/db"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)] ;; Setup the regex.
    (when (.find matcher) ;; Check if it matches.
      (zipmap [:match :user :pass :host :port :db] (re-groups matcher))))) 

(def mongo-conn
  "Connection to mongo"
  (let [mongo-url (:mongo-url config)
        config-mongo (split-mongo-url mongo-url)] 
    (make-connection (:db config-mongo) 
                     :host (:host config-mongo)
                     :port (Integer. (:port config-mongo)))))

;; Initialize connection with mongo.
(let [mongo-url (:mongo-url config)
      config-mongo    (split-mongo-url mongo-url)] 
  (authenticate mongo-conn (:user config-mongo) (:pass config-mongo))) ;; Setup u/p.


