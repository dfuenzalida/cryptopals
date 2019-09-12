(ns cryptopals.set1-test
  (:require [clojure.test :refer :all]
            [cryptopals.set1 :refer :all]))

(deftest hex->base64-tests
  (testing "example tests"
    (is (= (hex->base64 "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
           "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"))))
