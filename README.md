# testgen

A Clojure library designed to generate charaterisation tests.

## What are 'characterisation tests'?

Characterisation tests are a suite of tests which characterise the behaviour of a corpus of code. That is to say, they describe, and test, what it does now, whether that's correct or not. Otherwise, they are like unit tests. In fact, they are unit tests - except that unit tests are normally written to describe the desired behaviour of a corpus of code; these describe the actual behaviour.

## Why use charaterisation tests?

When modifying a corpus of legacy code, one wants to make only the desired change to its behaviour, and not introduce any 'regressions' (undesired behaviour changes). One way to do this is to start by writing (or generating) a set of tests which fully exercise the code, and all pass. Then, change only the test which describes the particular aspect of the behaviour that you want to change. That test (presumably) now fails, but all the others continue to pass. Now change the code under test until all tests pass again. The behaviour change is made, and no regressions have been introduced.

## Usage

At this stage, try

    (testgen <function-source>)

It will attempt to generate as Clojure source a set of clojure.test test definitions
for the code passed. For example:

    user=> (pprint (testgen '(defn testgen [fndef]
      #_=> (cond (= (first fndef) 'defn)
      #_=> (let [name (first (rest fndef))]
      #_=> (list 'deftest (symbol (str "test-" name))
      #_=> (map #(write-test name %) (find-interesting-args fndef))))))
      #_=> )
      #_=> )
    (deftest
     test-testgen
     ((is (= (testgen nil) nil))
      (is (= (testgen ()) nil))
      (is (thrown? java.lang.IllegalArgumentException (testgen true)))
      (is (= (testgen "test") nil))
      (is (thrown? java.lang.IllegalArgumentException (testgen :test)))
      (is (thrown? java.lang.IllegalArgumentException (testgen 0)))
      (is
       (thrown?
        java.lang.IllegalArgumentException
        (testgen Integer/MAX_VALUE)))
      (is (thrown? java.lang.IllegalArgumentException (testgen 1.0E-4)))
      (is (thrown? java.lang.IllegalArgumentException (testgen -1.0E-4)))
      (is (= (testgen "test-") nil))))

Note, however, that it only works if the function for which tests are being generated already exists in the environment.

## Where this is going

What I intend to end up with is a function

    (generate-tests <filename>)

which will read the file at <filename>, presumed to be Clojure source, and write a new file containing a complete test suite for that source file, which should exercise every branch and find as many as possible of the interesting corner cases. This does not work yet!

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
