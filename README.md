# testgen

A Clojure library designed to generate charaterisation tests.

## What are 'characterisation tests'?

Characterisation tests are a suite of tests which characterise the behaviour of a corpus of code. That is to say, they describe, and test, what it does now, whether that's correct or not. Otherwise, they are like unit tests. In fact, they are unit tests - except that unit tests are normally written to describe the desired behaviour of a corpus of code; these describe the actual behaviour.

## Why use charaterisation tests?

When modifying a corpus of legacy code, one wants to make only the desired change to its behaviour, and not introduce any 'regressions' (undesired behaviour changes). One way to do this is to start by writing (or generating) a set of tests which fully exercise the code, and all pass. Then, change only the test which describes the particular aspect of the behaviour that you want to change. That test (presumably) now fails, but all the others continue to pass. Now change the code under test until all tests pass again. The behaviour change is made, and no regressions have been introduced.

## Usage

At this stage, try

    (generate-tests <filename>)

It will attempt to read function definitions from the indicated file and generate as Clojure source a set of clojure.test test definitions
for the code passed. Currently the output goes to a file just called 'output'. For example:

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


Note, however, that it only works if the function for which tests are being generated already exists in the environment, so for now you have to 'use' the file first.

Note also that it only generates meaningful tests - thus far - for functions of one argument.

## Where this is going

What I intend to end up with is a function

    (generate-tests <filename>)

which will read the file at *filename*, presumed to be Clojure source, and write a new file containing a complete test suite for that source file, which should exercise every branch and find as many as possible of the interesting corner cases. This does not work yet!

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
