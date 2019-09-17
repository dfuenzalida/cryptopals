(ns cryptopals.set2-test
  (:require [clojure.test :refer :all]
            [cryptopals.set2 :refer :all]))

(deftest set2-tests

  (testing "Challenge 9"
    (is (= [89 69 76 76 79 87 32 83 85 66 77 65 82 73 78 69 4 4 4 4]
           (->> (pad (.getBytes "YELLOW SUBMARINE") 20) seq))))

  ;; end tests
  )
