(ns cryptopals.set2)

;; Challenge 9
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn pad [bytes padding]
  (let [length (count bytes)
        to-pad (rem padding length)]
    (->> (concat bytes (repeat to-pad 4)) byte-array)))

