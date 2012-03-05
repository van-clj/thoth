(ns thoth.models.base62)

(def alphabet "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn int-to-base62 
  "Converts an integer to a base62 number"
   ([n] (int-to-base62 (rem n 62) (quot n 62) ""))
   ([remainder number accum] 
      (cond
        (zero? number) (str (nth alphabet remainder) accum)
        :else (recur (rem number 62) (quot number 62) (str (nth alphabet remainder) accum)))))

(defn base62-to-int
  "Converts a base 62 string to number"
  ([str] (base62-to-int  (reverse str) 0 0))
  ([inverse-str power accum] 
    (cond
      (empty? inverse-str) accum
      :else (base62-to-int 
              (rest inverse-str) 
              (+ power 1) 
              (+ accum (* (bigint (Math/pow  62 power)) 
                          (.indexOf alphabet (str (first inverse-str)))))))))

