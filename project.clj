(defproject org.van-clj/thoth "0.1.0-SNAPSHOT"
            :description "A simple URL Shortener using noir and mongodb"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [congomongo "0.1.8"]
                           [noir "1.2.1"]]
            :main thoth.server)

