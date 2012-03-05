(ns thoth.server
;  (:require [thoth.middlewares :as middlewares])
  (:require [noir.server :as server]))

(server/load-views "src/thoth/views/")
;(server/add-middleware middlewares/auth-required)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'thoth})))

