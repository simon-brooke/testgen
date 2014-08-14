(ns testgen.manual_test
	(:require [clojure.test :refer :all]
	[testgen.core :refer :all]))

(deftest find-interesting-args-test
  (testing "find-interesting-args"
           (let [args (find-interesting-args 
                        '(defn find-interesting-args [sexpr extra-vars]
                           "Find things in sexpr which would be even more interesting if passed as arguments to it"
                           (concat generic-args extra-vars
                                   (flatten
                                     (map
                                       #(cond
                                          (integer? %) (list % (inc %) (dec %))
                                          (number? %) (list % (+ % 0.0001) (- % 0.0001))
                                          true %)
                                       (constants sexpr))))) nil)]
             (is (= args '(nil 
                            0 
                            0.0 
                            () 
                            "Find things in sexpr which would be even more interesting if passed as arguments to it" 
                            (quote (a :b "c")) 
                            :test 
                            true 
                            1.0E-4 
                            2.0E-4 
                            "test"))
                 "Fix: I ran it, and this is what it produced")
             (is (= (find-interesting-args nil nil) 
                    '(nil 0 () (quote (a :b "c")) :test true "test"))
                 "Fix: I ran it, and this is what it produced"))))

(deftest n-of-test
  (testing "n-of"
           (is (nil? (n-of true 0)) "Zero of anything should be nil")
           (is (nil? (n-of nil 0)) "Zero of anything should be nil")
           (is (nil? (n-of 4 0)) "Zero of anything should be nil")
           (is (nil? (n-of '(a) 0)) "Zero of anything should be nil")    
           (is (nil? (n-of "a" 0)) "Zero of anything should be nil")
           (is (empty? 
                 (remove true? 
                         (map 
                           #(let [result (n-of % 4)]
                              (is (= (count result) 4) 
                                  "4 of anything should be 4")
                              (is (= (first result) %) 
                                  "the first of four of anything should be that thing"))
                           generic-args))))))

(deftest generate-assertion-test
  (testing "generate-assertion"
           (is (= (generate-assertion '+ '( 1 2))
                  '(is (= (+ 1 2) 3) "Generating assertion for (+ 1 2)"))
               "This is what we're aiming for")))



