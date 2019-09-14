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

(defn bytes-score
  [bytes mask]
  (let [freq-phrase (apply str (reverse "ETAOIN SHRDLU"))
        upper-freqs (map vector freq-phrase (rest (range)))
        lower-freqs (map vector (.toLowerCase freq-phrase) (rest (range)))
        score-map   (into {} (concat upper-freqs lower-freqs))]
    (->> bytes
         (map (comp score-map char (partial bit-xor mask)))
         (remove nil?)
         (reduce +))))

(defn score [message mask]
  (bytes-score (read-bytes message) mask))

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

;; Challenge 5
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn repeating-key-xor [xor-key text]
  (let [key-seq    (cycle (map int xor-key))
        text-bytes (map int text)]
    (->> (map bit-xor key-seq text-bytes)
         (map (partial format "%02x"))
         (reduce str))))

;; Challenge 6
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn hamming-distance-bytes [bytes1 bytes2]
  (let [xors   (map bit-xor bytes1 bytes2)
        bits1  (fn [acc n]
                 (if (zero? n) acc (recur (+ acc (rem n 2)) (quot n 2))))]
    (->> (map (partial bits1 0) xors)
         (reduce +))))

(defn hamming-distance [text1 text2]
  (let [bytes1 (map int text1)
        bytes2 (map int text2)]
    (hamming-distance-bytes bytes1 bytes2)))

(defn read-input6 []
  (let [decoder (java.util.Base64/getDecoder)]
    (->> (slurp "resources/set1-ch6.txt")
         (clojure.string/split-lines)
         (clojure.string/join)
         (.decode decoder)
         (map int))))

;; Take nblox block of keysizes ranging 2 to 40, compute the hamming distance
;; between pairs and compute the weighted sum. Then find the lowest weight in
;; pairs of [keysize weighted-distance]

(defn find-keysize [input]
  (let [nblox 20 ;; take 20 samples to find the actual best keysize
        pairs (for [keysize (range 2 40)
                    :let [blocks (take nblox (partition keysize input))
                          dist   (map (partial apply hamming-distance-bytes)
                                      (partition 2 1 blocks))
                          wdist  (/ (reduce + dist) (* nblox keysize))]]
                [keysize wdist])]
    (->> (sort-by second pairs) ffirst)))

(defn best-byte-mask [bytes]
  (apply max-key (partial bytes-score bytes) (range 256)))

(defn transpose [input keysize] ;; Returns input as blocks of keysize elems
  (let [nblocks (quot (count input) keysize)
        blocks  (mapv vec (partition keysize input))]
    (for [i (range keysize)] ;; we want keysize blocks
      (mapv #(nth % i) blocks))))

(defn find-repeating-key-xor []
  (let [input   (read-input6)
        keysize (find-keysize input)
        blocks  (transpose input keysize)]
    (map best-byte-mask blocks)))

(defn break-repeating-key-xor []
  (let [xor-key    (find-repeating-key-xor)
        key-seq    (cycle (map int xor-key))
        text-bytes (read-input6)]
    (->> (map bit-xor key-seq text-bytes)
         (map char)
         (reduce str))))

