(ns thoth.test.views.get
  (:use [clojure.test]))

(deftest int-to-base62-tests
  (is (= (int-to-base62 0) "0"))
  (is (= (int-to-base62 61) "Z"))
  (is (= (int-to-base62 62) "10"))
  (is (= (int-to-base62 63) "11"))
  (is (= (int-to-base62 10000000000001) "2Q3rKTOF"))
  (is (= (int-to-base62 13243525321) (base62-to-int (int-to-base62 13243525321)))))

(deftest base62-to-int-tests
  (is (= (base62-to-int "0") 0))
  (is (= (base62-to-int "Z") 61))
  (is (= (base62-to-int "10") 62))
  (is (= (base62-to-int "11") 63))
  (is (= (base62-to-int "2Q3rKTOF") 10000000000001))
  (is (= (base62-to-int "ZZZZZZZZ") (int-to-base62 (base62-to-int "ZZZZZZZZ"))))) 
