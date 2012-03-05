(ns thoth.views.shortify
  (:require [thoth.models.shortener :as shortener])
  (:require [noir.response :as response])
  (:use [thoth.config :only (config)])
  (:use noir.core))

(defpage [:post "/shortify"] {:keys [url auth]}
  (cond
    (not (= (:auth-token config) auth))
      (response/status 401 "You need a valid auth token to access this resource.")
    (not (nil? url))
      (let [shortened-url (shortener/get-or-create-url url)]
        (response/json {:shortened_url (:_id shortened-url)
                    :url (:url shortened-url)}))
    :else
      (response/status 422 "You must provide an url parameter.")))
