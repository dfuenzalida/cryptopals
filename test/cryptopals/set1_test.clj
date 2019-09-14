(ns cryptopals.set1-test
  (:require [clojure.test :refer :all]
            [cryptopals.set1 :refer :all]))

(defn- md5 [bytes]
  (let [digester (java.security.MessageDigest/getInstance "MD5")]
    (->> (.digest digester bytes)
         (map (partial format "%02x"))
         (reduce str))))

(deftest hex->base64-tests
  (testing "Challenge 1"
    (is (= (hex->base64 "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
           "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t")))

  (testing "Challenge 2"
    (is (= (fixed-xor "1c0111001f010100061a024b53535009181c"
                      "686974207468652062756c6c277320657965")
           "746865206b696420646f6e277420706c6179")))

  (testing "Challenge 3"
    (is (= "2aa4ac426ec0624441a0bee9b6abd80e"
           (->> (decrypt-single-xor) .getBytes md5))))

  (testing "Challenge 4"
    (is (= "90cb2dc65138fac5a9a3ce1b5d570123"
           (->> (detect-single-xor) .getBytes md5))))

  (testing "Challenge 5"
    (let [input (str "Burning 'em, if you ain't quick and nimble\n"
                     "I go crazy when I hear a cymbal")]
    (is (= (str "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272"
                "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f")
           (repeating-key-xor "ICE" input)))))

  (testing "Challenge 6"
    (is (= 37
           (hamming-distance "this is a test" "wokka wokka!!!")))

    ;; Decypher the key:
    (is (= "011327c9d6d57189d01c768cd3d6f4a3"
           (->> (find-repeating-key-xor) byte-array md5)))

    ;; Decypher the message:
    (is (= "6187f6e338437e32f9cfc89ff6ee3b4d"
           (->> (break-repeating-key-xor) .getBytes md5))))

  ;; end tests
  )
