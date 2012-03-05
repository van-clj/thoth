(ns thoth.models.shortener
  (:require [thoth.models.base62 :as base62])
  (:use [thoth.config :only (mongo-conn)])
  (:use somnium.congomongo))

;; Utility functions
(defn add-http-if-needed [url]
  "Add http:// to an url if needed"
  (cond
    (not (nil? (re-find #"^http://" url))) url
    :else (str "http://" url)))

(defn insert-to-mongo [shortened url]
  (with-mongo mongo-conn
    (insert! :shortenedurls {:_id shortened
                             :url url})))
(defn get-url-from-id [_id] 
  (with-mongo mongo-conn 
    (:url (fetch-by-id :shortenedurls _id))))

(defn get-record-from-url [raw-url] 
  (with-mongo mongo-conn 
    (let [url (add-http-if-needed raw-url)]
      (first (fetch :shortenedurls :where {:url url})))))

(defn generate-shortened-url []
  (with-mongo mongo-conn
    (let [counter
      (fetch-and-modify
        :idspool ;; In the collection named 'ids-pool',
        {:_id "current"} ;; find the current record.
        {:$inc {:value 1} } ;; Increment it.
        :return-new true :upsert? true)]
      (cond 
        (nil? (:value counter)) "0" ;; Base case 
        :else (base62/int-to-base62 (:value counter))))))

(defn create-url [raw-url]
  (let [url (add-http-if-needed raw-url)]
    (insert-to-mongo (generate-shortened-url) url)))

(defn get-or-create-url [raw-url]
  (let [existent-url (get-record-from-url raw-url)]
    (cond (not (nil? existent-url)) existent-url
     :else (create-url raw-url))))
