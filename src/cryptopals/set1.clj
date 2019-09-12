(ns cryptopals.set1)

(defn hex->base64
  "Reads an string of hexadecimal digits, returns a base64 representation"
  [s]
  (let [encoder (java.util.Base64/getEncoder)]
    (->> s
         (partition 2)
         (map #(apply str %))
         (map #(Integer/parseInt % 16))
         byte-array
         (.encode encoder)
         (map char)
         (apply str))))

  
