(ns testgen.core-test
	"In the spirit of eating your own dogfood, all these tests are generated
	with testgen."
  (:require [clojure.test :refer :all]
            [testgen.core :refer :all]))



(deftest
 test-integer?
 (is (= (integer? nil) false))
 (is (= (integer? ()) false))
 (is (= (integer? '(a :b "c")) false))
 (is (= (integer? true) false))
 (is (= (integer? "test") false))
 (is (= (integer? :test) false))
 (is (= (integer? 0) true))
 (is (= (integer? Integer/MAX_VALUE) true))
 (is (= (integer? 22/7) false))
 (is (= (integer? 1.0E-4) false))
 (is (= (integer? -1.0E-4) false))
 (is (= (integer? "Returns true if n is an integer") false)))


(deftest
 test-testgen
 (is (= (testgen nil) nil))
 (is (= (testgen ()) nil))
 (is (= (testgen '(a :b "c")) nil))
 (is (thrown? java.lang.IllegalArgumentException (testgen true)))
 (is (= (testgen "test") nil))
 (is (thrown? java.lang.IllegalArgumentException (testgen :test)))
 (is (thrown? java.lang.IllegalArgumentException (testgen 0)))
 (is
  (thrown?
   java.lang.IllegalArgumentException
   (testgen Integer/MAX_VALUE)))
 (is (thrown? java.lang.IllegalArgumentException (testgen 22/7)))
 (is (thrown? java.lang.IllegalArgumentException (testgen 1.0E-4)))
 (is (thrown? java.lang.IllegalArgumentException (testgen -1.0E-4)))
 (is (= (testgen "test-") nil)))

(deftest
 test-constant?
 (is (= (constant? nil) 'true))
 (is (= (constant? ()) 'false))
 (is (= (constant? '(a :b "c")) 'false))
 (is (= (constant? true) 'true))
 (is (= (constant? "test") 'true))
 (is (= (constant? :test) 'true))
 (is (= (constant? 0) 'true))
 (is (= (constant? Integer/MAX_VALUE) 'true))
 (is (= (constant? 22/7) 'true))
 (is (= (constant? 1.0E-4) 'true))
 (is (= (constant? -1.0E-4) 'true)))

(deftest
 test-constants
 (is (= (constants nil) '()))
 (is (= (constants ()) '()))
 (is (= (constants '(a :b "c")) '(:b "c")))
 (is (= (constants true) '()))
 (is (= (constants "test") '()))
 (is (= (constants :test) '()))
 (is (= (constants 0) '()))
 (is (= (constants Integer/MAX_VALUE) '()))
 (is (= (constants 22/7) '()))
 (is (= (constants 1.0E-4) '()))
 (is (= (constants -1.0E-4) '()))
 (is
  (=
   (constants
    "return a list of all elements in this form which are constants")
   '())))



