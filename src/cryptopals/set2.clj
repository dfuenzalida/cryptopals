(ns cryptopals.set2)

;; Challenge 9
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn pad [bytes padding]
  (let [length (count bytes)
        to-pad (rem padding length)]
    (->> (concat bytes (repeat to-pad 4)) byte-array)))

(defn read-input10 []
  (let [decoder (java.util.Base64/getDecoder)]
    (->> (slurp "resources/10.txt")
         (clojure.string/split-lines)
         (clojure.string/join)
         (.decode decoder))))

(defn aes-cbc-decrypt [bytes password]
  (let [ivparam (javax.crypto.spec.IvParameterSpec. (->> (repeat 16 0) byte-array))
        keyspec (javax.crypto.spec.SecretKeySpec. (.getBytes password) "AES")
        cypher  (javax.crypto.Cipher/getInstance "AES/CBC/NoPadding")]
    (.init cypher javax.crypto.Cipher/DECRYPT_MODE keyspec ivparam)
    (String. (.doFinal cypher bytes))))

;; This works
;; (aes-cbc-decrypt (read-input10) "YELLOW SUBMARINE")
