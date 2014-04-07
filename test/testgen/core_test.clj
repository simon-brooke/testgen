(ns testgen.core_test
	(:require [clojure.test :refer :all]
	[testgen.core :refer :all]))

;; auto-generated by testgen - see https://github.com/simon-brooke/testgen

(deftest
 test-maybe-quote
 (testing
  "maybe-quote"
  (is (= (maybe-quote nil) ''nil))
  (is (= (maybe-quote ()) ''()))
  (is (= (maybe-quote '(a :b "c")) ''(a :b "c")))
  (is (= (maybe-quote true) ''true))
  (is (= (maybe-quote "test") ''"test"))
  (is (= (maybe-quote :test) '':test))
  (is (= (maybe-quote 0) ''0))
  (is (= (maybe-quote Integer/MAX_VALUE) ''2147483647))
  (is (= (maybe-quote 22/7) ''22/7))
  (is (= (maybe-quote 1.0E-4) ''1.0E-4))
  (is (= (maybe-quote -1.0E-4) ''-1.0E-4))
  (is
   (=
    (maybe-quote generic-args)
    ''(nil
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
    (maybe-quote
     "Convert val into a form in which, after being passed through the pretty\n\tprinter, it will be reconstituted in a form useful to the test")
    ''"Convert val into a form in which, after being passed through the pretty\n\tprinter, it will be reconstituted in a form useful to the test"))
  (is (= (maybe-quote true) ''true))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-generate-assertion
 (testing
  "generate-assertion"
  (is
   (=
    (generate-assertion nil)
    '(is (thrown? clojure.lang.Compiler$CompilerException (nil)))))
  (is
   (=
    (generate-assertion ())
    '(is (thrown? java.lang.ClassCastException (())))))
  (is
   (=
    (generate-assertion '(a :b "c"))
    '(is
      (thrown? clojure.lang.Compiler$CompilerException ((a :b "c"))))))
  (is
   (=
    (generate-assertion true)
    '(is (thrown? java.lang.ClassCastException (true)))))
  (is
   (=
    (generate-assertion "test")
    '(is (thrown? java.lang.ClassCastException ("test")))))
  (is
   (=
    (generate-assertion :test)
    '(is (thrown? java.lang.IllegalArgumentException (:test)))))
  (is
   (=
    (generate-assertion 0)
    '(is (thrown? java.lang.ClassCastException (0)))))
  (is
   (=
    (generate-assertion Integer/MAX_VALUE)
    '(is (thrown? java.lang.ClassCastException (2147483647)))))
  (is
   (=
    (generate-assertion 22/7)
    '(is (thrown? java.lang.ClassCastException (22/7)))))
  (is
   (=
    (generate-assertion 1.0E-4)
    '(is (thrown? java.lang.ClassCastException (1.0E-4)))))
  (is
   (=
    (generate-assertion -1.0E-4)
    '(is (thrown? java.lang.ClassCastException (-1.0E-4)))))
  (is
   (=
    (generate-assertion generic-args)
    '(is
      (thrown?
       clojure.lang.Compiler$CompilerException
       ((nil
         ()
         '(a :b "c")
         true
         "test"
         :test
         0
         Integer/MAX_VALUE
         22/7
         1.0E-4
         -1.0E-4))))))
  (is
   (=
    (generate-assertion
     "Generate an appropiate assertion for these arguments passed to this function")
    '(is
      (thrown?
       java.lang.ClassCastException
       ("Generate an appropiate assertion for these arguments passed to this function")))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-constant?
 (testing
  "constant?"
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
  (is (= (constant? -1.0E-4) 'true))
  (is (= (constant? generic-args) 'false))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-constants
 (testing
  "constants"
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
    (constants generic-args)
    '(nil :b "c" true "test" :test 0 22/7 1.0E-4 -1.0E-4)))
  (is
   (=
    (constants
     "return a list of all elements in this form which are constants")
    '()))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-find-interesting-args
 (testing
  "find-interesting-args"
  (is
   (thrown? clojure.lang.ArityException (find-interesting-args nil)))
  (is (thrown? clojure.lang.ArityException (find-interesting-args ())))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args '(a :b "c"))))
  (is
   (thrown? clojure.lang.ArityException (find-interesting-args true)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args "test")))
  (is
   (thrown? clojure.lang.ArityException (find-interesting-args :test)))
  (is (thrown? clojure.lang.ArityException (find-interesting-args 0)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args Integer/MAX_VALUE)))
  (is
   (thrown? clojure.lang.ArityException (find-interesting-args 22/7)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args 1.0E-4)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args -1.0E-4)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args generic-args)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args
     "Find things in sexpr which would be even more interesting if passed as arguments to it")))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args 1.0E-4)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args 2.0E-4)))
  (is
   (thrown? clojure.lang.ArityException (find-interesting-args 0.0)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args 1.0E-4)))
  (is
   (thrown?
    clojure.lang.ArityException
    (find-interesting-args 2.0E-4)))
  (is
   (thrown? clojure.lang.ArityException (find-interesting-args 0.0)))
  (is
   (thrown? clojure.lang.ArityException (find-interesting-args true)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-generate-test
 (testing
  "generate-test"
  (is (thrown? clojure.lang.ArityException (generate-test nil)))
  (is (thrown? clojure.lang.ArityException (generate-test ())))
  (is
   (thrown? clojure.lang.ArityException (generate-test '(a :b "c"))))
  (is (thrown? clojure.lang.ArityException (generate-test true)))
  (is (thrown? clojure.lang.ArityException (generate-test "test")))
  (is (thrown? clojure.lang.ArityException (generate-test :test)))
  (is (thrown? clojure.lang.ArityException (generate-test 0)))
  (is
   (thrown?
    clojure.lang.ArityException
    (generate-test Integer/MAX_VALUE)))
  (is (thrown? clojure.lang.ArityException (generate-test 22/7)))
  (is (thrown? clojure.lang.ArityException (generate-test 1.0E-4)))
  (is (thrown? clojure.lang.ArityException (generate-test -1.0E-4)))
  (is
   (thrown? clojure.lang.ArityException (generate-test generic-args)))
  (is (thrown? clojure.lang.ArityException (generate-test "test-")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-clean-filename
 (testing
  "clean-filename"
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
   (thrown?
    java.lang.IllegalArgumentException
    (clean-filename 1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (clean-filename -1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (clean-filename generic-args)))
  (is
   (=
    (clean-filename
     "remove the leading 'src/' and trailing '.clj' (if present) from a Clojure file name")
    '"' and trailing '.clj' (if present) from a Clojure file name"))
  (is (= (clean-filename ".clj") '""))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 0)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 1)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename -1)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 4)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 5)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 3)))
  (is
   (thrown? java.lang.IllegalArgumentException (clean-filename true)))
  (is (= (clean-filename "src/") '""))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename -1)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 0)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename -2)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 4)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 5)))
  (is (thrown? java.lang.IllegalArgumentException (clean-filename 3)))
  (is
   (thrown? java.lang.IllegalArgumentException (clean-filename true)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-testname-from-filename
 (testing
  "testname-from-filename"
  (is
   (thrown?
    java.lang.NullPointerException
    (testname-from-filename nil)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename ())))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename '(a :b "c"))))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename true)))
  (is (= (testname-from-filename "test") '"test/test_test.clj"))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename :test)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename 0)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename Integer/MAX_VALUE)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename 22/7)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename 1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename -1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename generic-args)))
  (is
   (=
    (testname-from-filename
     "return an approximately-correct filename in which to save tests")
    '"test/return an approximately-correct filename in which to save tests_test.clj"))
  (is (= (testname-from-filename "src/") '"test/_test.clj"))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename -1)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename 0)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename -2)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename 0)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename 1)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename -1)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (testname-from-filename true)))
  (is (= (testname-from-filename "") '"test/_test.clj"))
  (is (= (testname-from-filename "test/") '"test/test/_test.clj"))
  (is (= (testname-from-filename "_test.clj") '"test/_test_test.clj"))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-packagename-from-filename
 (testing
  "packagename-from-filename"
  (is
   (thrown?
    java.lang.NullPointerException
    (packagename-from-filename nil)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename ())))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename '(a :b "c"))))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename true)))
  (is (= (packagename-from-filename "test") (symbol "test")))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename :test)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename 0)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename Integer/MAX_VALUE)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename 22/7)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename 1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename -1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (packagename-from-filename generic-args)))
  (is
   (=
    (packagename-from-filename
     "Return, as a symbol, an appropiate name for a test file associated with this filename. There's\n  probably a better way of doing this.")
    (symbol
     "Return, as a symbol, an appropiate name for a test file associated with this filename. There's\n  probably a better way of doing this.")))
  (is (= (packagename-from-filename "/") (symbol ".")))
  (is (= (packagename-from-filename ".") (symbol ".")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-expr-seq
 (testing
  "expr-seq"
  (is (thrown? java.lang.NullPointerException (expr-seq nil)))
  (is (thrown? java.lang.ClassCastException (expr-seq ())))
  (is (thrown? java.lang.ClassCastException (expr-seq '(a :b "c"))))
  (is (thrown? java.lang.ClassCastException (expr-seq true)))
  (is (thrown? java.lang.ClassCastException (expr-seq "test")))
  (is (thrown? java.lang.ClassCastException (expr-seq :test)))
  (is (thrown? java.lang.ClassCastException (expr-seq 0)))
  (is
   (thrown? java.lang.ClassCastException (expr-seq Integer/MAX_VALUE)))
  (is (thrown? java.lang.ClassCastException (expr-seq 22/7)))
  (is (thrown? java.lang.ClassCastException (expr-seq 1.0E-4)))
  (is (thrown? java.lang.ClassCastException (expr-seq -1.0E-4)))
  (is (thrown? java.lang.ClassCastException (expr-seq generic-args)))
  (is
   (thrown?
    java.lang.ClassCastException
    (expr-seq
     "Returns forms from src (assumed to be Clojure source) as a lazy sequence of expressions")))
  (is (thrown? java.lang.ClassCastException (expr-seq false)))
  (is (thrown? java.lang.NullPointerException (expr-seq nil)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-find-vars-in-reader
 (testing
  "find-vars-in-reader"
  (is
   (thrown? java.lang.NullPointerException (find-vars-in-reader nil)))
  (is (thrown? java.lang.ClassCastException (find-vars-in-reader ())))
  (is
   (thrown?
    java.lang.ClassCastException
    (find-vars-in-reader '(a :b "c"))))
  (is
   (thrown? java.lang.ClassCastException (find-vars-in-reader true)))
  (is
   (thrown? java.lang.ClassCastException (find-vars-in-reader "test")))
  (is
   (thrown? java.lang.ClassCastException (find-vars-in-reader :test)))
  (is (thrown? java.lang.ClassCastException (find-vars-in-reader 0)))
  (is
   (thrown?
    java.lang.ClassCastException
    (find-vars-in-reader Integer/MAX_VALUE)))
  (is
   (thrown? java.lang.ClassCastException (find-vars-in-reader 22/7)))
  (is
   (thrown? java.lang.ClassCastException (find-vars-in-reader 1.0E-4)))
  (is
   (thrown?
    java.lang.ClassCastException
    (find-vars-in-reader -1.0E-4)))
  (is
   (thrown?
    java.lang.ClassCastException
    (find-vars-in-reader generic-args)))
  (is
   (thrown?
    java.lang.ClassCastException
    (find-vars-in-reader
     "Return a list of names of vars declared in the stream this reader reads")))
  (is
   (thrown? java.lang.ClassCastException (find-vars-in-reader false)))
  (is
   (thrown? java.lang.NullPointerException (find-vars-in-reader nil)))
  (is
   (thrown? java.lang.NullPointerException (find-vars-in-reader nil)))
  (is
   (thrown? java.lang.ClassCastException (find-vars-in-reader true)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-find-vars-in-file
 (testing
  "find-vars-in-file"
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file nil)))
  (is
   (thrown? java.lang.IllegalArgumentException (find-vars-in-file ())))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file '(a :b "c"))))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file true)))
  (is
   (thrown? java.io.FileNotFoundException (find-vars-in-file "test")))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file :test)))
  (is
   (thrown? java.lang.IllegalArgumentException (find-vars-in-file 0)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file Integer/MAX_VALUE)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file 22/7)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file 1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file -1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (find-vars-in-file generic-args)))
  (is
   (thrown?
    java.io.FileNotFoundException
    (find-vars-in-file
     "Return a list of names of vars declared in the file at this path name")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-write-header
 (testing
  "write-header"
  (is (thrown? clojure.lang.ArityException (write-header nil)))
  (is (thrown? clojure.lang.ArityException (write-header ())))
  (is (thrown? clojure.lang.ArityException (write-header '(a :b "c"))))
  (is (thrown? clojure.lang.ArityException (write-header true)))
  (is (thrown? clojure.lang.ArityException (write-header "test")))
  (is (thrown? clojure.lang.ArityException (write-header :test)))
  (is (thrown? clojure.lang.ArityException (write-header 0)))
  (is
   (thrown?
    clojure.lang.ArityException
    (write-header Integer/MAX_VALUE)))
  (is (thrown? clojure.lang.ArityException (write-header 22/7)))
  (is (thrown? clojure.lang.ArityException (write-header 1.0E-4)))
  (is (thrown? clojure.lang.ArityException (write-header -1.0E-4)))
  (is
   (thrown? clojure.lang.ArityException (write-header generic-args)))
  (is (thrown? clojure.lang.ArityException (write-header "(ns ")))
  (is (thrown? clojure.lang.ArityException (write-header "_test\n")))
  (is
   (thrown?
    clojure.lang.ArityException
    (write-header "\t(:require [clojure.test :refer :all]\n\t[")))
  (is
   (thrown?
    clojure.lang.ArityException
    (write-header " :refer :all]))\n\n")))
  (is
   (thrown?
    clojure.lang.ArityException
    (write-header
     ";; auto-generated by testgen - see https://github.com/simon-brooke/testgen\n\n")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest
 test-generate-tests
 (testing
  "generate-tests"
  (is (thrown? java.lang.NullPointerException (generate-tests nil)))
  (is (thrown? java.lang.IllegalArgumentException (generate-tests ())))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (generate-tests '(a :b "c"))))
  (is
   (thrown? java.lang.IllegalArgumentException (generate-tests true)))
  (is (thrown? java.io.FileNotFoundException (generate-tests "test")))
  (is
   (thrown? java.lang.IllegalArgumentException (generate-tests :test)))
  (is (thrown? java.lang.IllegalArgumentException (generate-tests 0)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (generate-tests Integer/MAX_VALUE)))
  (is
   (thrown? java.lang.IllegalArgumentException (generate-tests 22/7)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (generate-tests 1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (generate-tests -1.0E-4)))
  (is
   (thrown?
    java.lang.IllegalArgumentException
    (generate-tests generic-args)))
  (is
   (thrown?
    java.io.FileNotFoundException
    (generate-tests
     "Generate a suite of characterisation tests for the file indicated by this filename.\n\n  filename: the file path name of a file containing Clojure code to be tested.")))
  (is
   (thrown?
    java.io.FileNotFoundException
    (generate-tests "Read vars: ")))
  (is
   (thrown?
    java.io.FileNotFoundException
    (generate-tests "Writing to: ")))
  (is
   (thrown?
    java.io.FileNotFoundException
    (generate-tests "reading...")))
  (is
   (thrown? java.lang.IllegalArgumentException (generate-tests false)))
  (is (thrown? java.lang.NullPointerException (generate-tests nil)))
  (is (thrown? java.io.FileNotFoundException (generate-tests "...")))
  (is
   (thrown?
    java.io.FileNotFoundException
    (generate-tests "\n\n;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;\n\n")))
  (is
   (thrown?
    java.io.FileNotFoundException
    (generate-tests "\n\n;; end of file ;;\n\n")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



;; end of file ;;

