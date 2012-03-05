(ns thoth.views.shortify
  (:require [thoth.models.shortener :as shortener])
  (:require [noir.response :as response])
  (:use noir.core))

(defpage [:post "/shortify"] {:keys [url]} 
  (cond 
    (not (nil? url))
      (let [shortened-url (shortener/get-or-create-url url)]
        (response/json {:shortened_url (:_id shortened-url)
                    :url (:url shortened-url)}))
    :else 
      (response/status 422 "You must provide an url parameter.")))
