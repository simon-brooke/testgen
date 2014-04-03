(ns testgen.core-test
	"In the spirit of eating your own dogfood, all these tests are generated
	with testgen."
  (:require [clojure.test :refer :all]
            [testgen.core :refer :all]))

(deftest
 test-write-test
 (is (thrown? clojure.lang.ArityException (write-test nil)))
 (is (thrown? clojure.lang.ArityException (write-test ())))
 (is (thrown? clojure.lang.ArityException (write-test '(a :b "c"))))
 (is (thrown? clojure.lang.ArityException (write-test true)))
 (is (thrown? clojure.lang.ArityException (write-test "test")))
 (is (thrown? clojure.lang.ArityException (write-test :test)))
 (is (thrown? clojure.lang.ArityException (write-test 0)))
 (is
  (thrown? clojure.lang.ArityException (write-test Integer/MAX_VALUE)))
 (is (thrown? clojure.lang.ArityException (write-test 22/7)))
 (is (thrown? clojure.lang.ArityException (write-test 1.0E-4)))
 (is (thrown? clojure.lang.ArityException (write-test -1.0E-4))))
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
 test-generic-args
 (is (thrown? java.lang.ClassCastException (generic-args nil)))
 (is (thrown? java.lang.ClassCastException (generic-args ())))
 (is (thrown? java.lang.ClassCastException (generic-args '(a :b "c"))))
 (is (thrown? java.lang.ClassCastException (generic-args true)))
 (is (thrown? java.lang.ClassCastException (generic-args "test")))
 (is (thrown? java.lang.ClassCastException (generic-args :test)))
 (is (thrown? java.lang.ClassCastException (generic-args 0)))
 (is
  (thrown?
   java.lang.ClassCastException
   (generic-args Integer/MAX_VALUE)))
 (is (thrown? java.lang.ClassCastException (generic-args 22/7)))
 (is (thrown? java.lang.ClassCastException (generic-args 1.0E-4)))
 (is (thrown? java.lang.ClassCastException (generic-args -1.0E-4)))
 (is (thrown? java.lang.ClassCastException (generic-args nil)))
 (is (thrown? java.lang.ClassCastException (generic-args :b)))
 (is (thrown? java.lang.ClassCastException (generic-args "c")))
 (is (thrown? java.lang.ClassCastException (generic-args true)))
 (is (thrown? java.lang.ClassCastException (generic-args "test")))
 (is (thrown? java.lang.ClassCastException (generic-args :test)))
 (is (thrown? java.lang.ClassCastException (generic-args 0)))
 (is (thrown? java.lang.ClassCastException (generic-args 1)))
 (is (thrown? java.lang.ClassCastException (generic-args -1)))
 (is (thrown? java.lang.ClassCastException (generic-args 22/7)))
 (is
  (thrown?
   java.lang.ClassCastException
   (generic-args 3.142957142857143)))
 (is
  (thrown?
   java.lang.ClassCastException
   (generic-args 3.1427571428571426)))
 (is (thrown? java.lang.ClassCastException (generic-args 1.0E-4)))
 (is (thrown? java.lang.ClassCastException (generic-args 2.0E-4)))
 (is (thrown? java.lang.ClassCastException (generic-args 0.0)))
 (is (thrown? java.lang.ClassCastException (generic-args -1.0E-4)))
 (is (thrown? java.lang.ClassCastException (generic-args 0.0)))
 (is (thrown? java.lang.ClassCastException (generic-args -2.0E-4))))
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
(deftest
 test-find-interesting-args
 (is
  (=
   (find-interesting-args nil)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args ())
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args '(a :b "c"))
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4
     :b
     "c")))
 (is
  (=
   (find-interesting-args true)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args "test")
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args :test)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 0)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args Integer/MAX_VALUE)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 22/7)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 1.0E-4)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args -1.0E-4)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args
    "Find things in sexpr which would be even more interesting if passed as arguments to it")
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 1.0E-4)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 2.0E-4)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 0.0)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 1.0E-4)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 2.0E-4)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args 0.0)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4)))
 (is
  (=
   (find-interesting-args true)
   '(nil
     ()
     '(a :b "c")
     true
     "test"
     :test
     0
     Integer/MAX_VALUE
     22/7
     1.0E-4
     -1.0E-4))))
(deftest
 test-testgen
 (is (= (testgen nil) 'nil))
 (is (= (testgen ()) 'nil))
 (is (= (testgen '(a :b "c")) 'nil))
 (is (thrown? java.lang.IllegalArgumentException (testgen true)))
 (is (= (testgen "test") 'nil))
 (is (thrown? java.lang.IllegalArgumentException (testgen :test)))
 (is (thrown? java.lang.IllegalArgumentException (testgen 0)))
 (is
  (thrown?
   java.lang.IllegalArgumentException
   (testgen Integer/MAX_VALUE)))
 (is (thrown? java.lang.IllegalArgumentException (testgen 22/7)))
 (is (thrown? java.lang.IllegalArgumentException (testgen 1.0E-4)))
 (is (thrown? java.lang.IllegalArgumentException (testgen -1.0E-4)))
 (is (= (testgen "test-") 'nil)))
(deftest
 test-clean-filename
 (is (thrown? java.lang.NullPointerException (clean-filename nil)))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename ())))
 (is
  (thrown?
   java.lang.IllegalArgumentException
   (clean-filename '(a :b "c"))))
 (is
  (thrown? java.lang.IllegalArgumentException (clean-filename true)))
 (is (= (clean-filename "test") '"test"))
 (is
  (thrown? java.lang.IllegalArgumentException (clean-filename :test)))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename 0)))
 (is
  (thrown?
   java.lang.IllegalArgumentException
   (clean-filename Integer/MAX_VALUE)))
 (is
  (thrown? java.lang.IllegalArgumentException (clean-filename 22/7)))
 (is
  (thrown? java.lang.IllegalArgumentException (clean-filename 1.0E-4)))
 (is
  (thrown?
   java.lang.IllegalArgumentException
   (clean-filename -1.0E-4)))
 (is
  (=
   (clean-filename
    "remove the trailing '.clj' from a Clojure file name")
   '"remove the trailing '.clj' from a Clojure file name"))
 (is (= (clean-filename ".clj") '""))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename 0)))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename 1)))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename -1)))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename 4)))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename 5)))
 (is (thrown? java.lang.IllegalArgumentException (clean-filename 3)))
 (is
  (thrown? java.lang.IllegalArgumentException (clean-filename true))))
