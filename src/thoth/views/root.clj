(ns thoth.views.root
  (:require [thoth.models.shortener :as shortener])
  (:require [noir.response :as response])
  (:use noir.core))

(defpage "/" []
  (response/status 404 "The url provided coudln't be found"))
