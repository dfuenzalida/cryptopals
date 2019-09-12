(ns cryptopals.set1)

(defn- read-bytes [s]
  (->> s
       (partition 2)
       (map #(apply str %))
       (map #(Integer/parseInt % 16))))

;; Challenge 1
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn hex->base64
  "Reads an string of hexadecimal digits, returns a base64 representation"
  [s]
  (let [encoder (java.util.Base64/getEncoder)]
    (->> (read-bytes s)
         byte-array
         (.encode encoder)
         (map char)
         (apply str))))
  
;; Challenge 2
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn fixed-xor [s1 s2]
  (let [bytes1 (read-bytes s1)
        bytes2 (read-bytes s2)]
    (->> (map bit-xor bytes1 bytes2)
         (map #(format "%02x" %))
         (apply str))))
