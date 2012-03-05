(ns thoth.views.get
  (:require [thoth.models.shortener :as shortener])
  (:require [noir.response :as response])
  (:use noir.core))

(defpage "/:url" {url-id :url} 
  (let [original-url (shortener/get-url-from-id url-id)]
  (cond 
    (not (nil? original-url))
        (response/redirect original-url)
    :else 
      (response/status 404 "The url provided coudln't be found"))))
