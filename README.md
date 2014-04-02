# testgen

A Clojure library designed to generate charaterisation tests.

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

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
