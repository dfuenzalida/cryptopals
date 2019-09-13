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

(defn fixed-xor
  "Takes two equal-length hex strings and produces their XOR combination as an hex string"
  [s1 s2]
  (let [bytes1 (read-bytes s1)
        bytes2 (read-bytes s2)]
    (->> (map bit-xor bytes1 bytes2)
         (map #(format "%02x" %))
         (apply str))))

;; Challenge 3
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def hidden "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")

(defn score
  [message mask]
  (let [freq-phrase (apply str (reverse "ETAOIN SHRDLU"))
        upper-freqs (map vector freq-phrase (rest (range)))
        lower-freqs (map vector (.toLowerCase freq-phrase) (rest (range)))
        score-map   (into {} (concat upper-freqs lower-freqs))]
    (->> (read-bytes message)
         (map (comp score-map char (partial bit-xor mask)))
         (remove nil?)
         (reduce +))))

(defn decrypt-single-xor []
  (let [mask (apply max-key (partial score hidden) (range 256))]
    (->> (read-bytes hidden)
         (map (comp char (partial bit-xor mask)))
         (apply str))))


;; Challenge 4
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn read-input4 []
  (->> (slurp "resources/set1-ch4.txt")
       (clojure.string/split-lines)))

(defn detect-single-xor []
  (let [messages (into [] (read-input4))
        scores   (for [i (range (count messages))
                       n (range 1 256)]
                   [[i n] (score (messages i) n)])
        [i n]    (first (apply max-key second scores))]
    (->> (read-bytes (messages i))
         (map (comp char (partial bit-xor n)))
         (apply str))))
