(ns cryptopals.set1-test
  (:require [clojure.test :refer :all]
            [cryptopals.set1 :refer :all]))

(deftest hex->base64-tests
  (testing "Challenge 1"
    (is (= (hex->base64 "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
           "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t")))

  (testing "Challenge 2"
    (is (= (fixed-xor "1c0111001f010100061a024b53535009181c"
                      "686974207468652062756c6c277320657965")
           "746865206b696420646f6e277420706c6179")))

  (testing "Challenge 3"
    (is (= "Q29va2luZyBNQydzIGxpa2UgYSBwb3VuZCBvZiBiYWNvbg==" ;; Base64 to hide it
           (->> (decrypt-single-xor)
                .getBytes
                (.encode (java.util.Base64/getEncoder))
                (map char)
                (apply str)))))

  (testing "Challenge 4"
    (is (= "4e6f77207468617420746865207061727479206973206a756d70696e670a"
           (->> (detect-single-xor)
                (map (comp (partial format "%02x") int))
                (apply str)))))

  (testing "Challenge 5"
    (is (= (str "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272"
                "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f")
           (repeating-key-xor "ICE" (str "Burning 'em, if you ain't quick and nimble\n"
                                         "I go crazy when I hear a cymbal")))))

  (testing "Challenge 6"
    (is (= 37
           (hamming-distance "this is a test" "wokka wokka!!!"))))

  ;; end tests
  )
