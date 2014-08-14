(ns testgen.core
  (:use clojure.java.io
        clojure.pprint
        clojure.math.combinatorics))


(defn maybe-quote 
	"Convert val into a form in which, after being passed through the pretty
	printer, it will be reconstituted in a form useful to the test"
 [val]
 (let [mval (try
              (macroexpand val)
              (catch Exception any val))]
	(cond
   (= true val) true
   (nil? val) nil
   (number? val) val
   (string? val) val
   (keyword? val) val
   (vector? val) val
   (map? val) val
   (symbol? val) (list 'symbol (str val))
   (and (seq mval) (= (first mval) 'quote)) val
   true (list 'quote val))))

(defn generate-assertion 
  "Generate an appropiate assertion for these arguments passed to this function"
  [fnname args]
  (let [doc-string (str "Generating assertion for " (cons fnname args))]
    (try
      (let [val (eval (cons fnname args))]
        (list 'is (list '= (cons fnname args) (maybe-quote val)) doc-string))
      (catch Exception e 
        (list 'is (list 'thrown? (.getClass e) (cons fnname args)))))))

(defn constant? [arg]
	(not (or
		(symbol? arg)
		(seq? arg)
		(vector? arg)
		(map? arg))))

;; (def generic-args '(nil () (quote (a :b "c")) "test" true :test 0 Integer/MAX_VALUE 22/7 0.0001 -0.0001))
(def generic-args '(nil () (quote (a :b "c")) "test" true :test 0))
;; (def generic-args nil)

(defn constants [form]
	"return a list of all elements in this form which are constants"
  (filter constant? (flatten form)))

(defn find-interesting-args [sexpr extra-vars]
	"Find things in sexpr which would be even more interesting if passed as arguments to it"
	(apply list
        (set
          (concat generic-args extra-vars
                  (flatten
                    (map
                      #(cond
                         (integer? %) (list % (inc %) (dec %))
                         (number? %) (list % (+ % 0.0001) (- % 0.0001))
                         true %)
                      (constants sexpr)))))))


(defn n-of [arg n]
	"Return a list of n instances of arg"
	(cond
		(zero? n) nil
		true (cons arg (n-of arg (dec n)))))


;; This version of generate-test tries to generate good tests for functions of one
;; argument. It works.
(defn generate-test-1 [fndef extra-vars]
   "Generate a test for this function definition"
 	(cond (or (= (first fndef) 'def)(= (first fndef) 'defn))
 		(let [name (first (rest fndef))
 					potential-args (find-interesting-args fndef extra-vars)]
 			 (list 'deftest (symbol (str "test-" name))
 				(concat (list 'testing (str name))
 					(map #(generate-assertion name (list %))  potential-args))))))

;; This version of generate-test tries to generate good tests for functions of one or more than one
;; argument. Unfortunately, it is borked.
(defn generate-test-n [fndef extra-vars]
  "Generate a test for this function definition"
	(cond (or (= (first fndef) 'def)(= (first fndef) 'defn))
		(let [name (first (rest fndef))
          arg-list (nth fndef 2)
          potential-args (map maybe-quote (find-interesting-args fndef extra-vars))]
      (print potential-args)
      (try
        (list 'deftest (symbol (str "test-" name))
              (concat (list 'testing (str name))
                      (map #(generate-assertion name %)
                           (cond 
                             (vector? arg-list) 
                             (apply cartesian-product 
                                    (n-of potential-args (count arg-list)))
                             true 
                             (map #(list %) potential-args)))))
        (catch Exception any)))))

;; generating a test file

(defn clean-filename [filename]
  "remove the leading 'src/' and trailing '.clj' (if present) from a Clojure file name"
  (let [without-suffix (cond
   		(.endsWith filename ".clj") (.substring filename 0 (- (count filename) 4))
   		true filename)
	prefix-position (.indexOf filename "src/")]
	(cond (> prefix-position -1) (.substring without-suffix (+ prefix-position 4))
		true without-suffix)))

(defn testname-from-filename [filename]
	"return an approximately-correct filename in which to save tests"
	(let [prefix-position (.indexOf filename "src/")
				prefix (cond (> prefix-position -1) (.substring filename 0 prefix-position)
									true "")]
		(str prefix "test/" (clean-filename filename) "_test.clj")))


(defn packagename-from-filename [filename]
  "Return, as a symbol, an appropiate name for a test file associated with this filename. There's
  probably a better way of doing this."
  (let [fn (clean-filename filename)]
    (symbol (.replace fn "/" "."))))

;; This should be a better mechanism for reading expressions from a file but I haven't
;; really made it work yet.
(defn expr-seq
  "Returns forms from src (assumed to be Clojure source) as a lazy sequence of expressions"
  [^java.io.PushbackReader src]
  (when-let [expr (read src false nil)]
    (cons expr (lazy-seq (expr-seq src)))))

(defn find-vars-in-reader [eddi]
	"Return a list of names of vars declared in the stream this reader reads"
  (let [sexpr (read eddi false nil)]
    (cond
     (nil? sexpr) nil
     (= (first sexpr) 'def) (cons (first (rest sexpr)) (find-vars-in-reader eddi))
     true (find-vars-in-reader eddi))))

(defn find-vars-in-file [filename]
	"Return a list of names of vars declared in the file at this path name"
	(with-open [eddi (java.io.PushbackReader. (reader filename))]
		(find-vars-in-reader eddi)))

(defn write-header [writer package]
	 			(.write writer (str "(ns " package "_test\n"))
				(.write writer (str "\t(:require [clojure.test :refer :all]\n\t["
						package " :refer :all]))\n\n"))
				(.write writer
						";; auto-generated by testgen - see https://github.com/simon-brooke/testgen\n\n"))

(defn generate-tests [filename]
  "Generate a suite of characterisation tests for the file indicated by this filename.

   * `filename`: the file path name of a file containing Clojure code to be tested."
    (let [fn (clean-filename filename)
          pn (packagename-from-filename filename)
					extra-vars (find-vars-in-file filename)]
			(println "Read vars: " extra-vars)
			(println "Writing to: " (testname-from-filename filename))
      ;; load the file so that any functions in it are usable
      (load fn)
      (refer pn)
			(with-open [eddi (java.io.PushbackReader. (reader filename))
                  dickens (writer (testname-from-filename filename))]
        (write-header dickens pn)
				(while (.ready eddi)
          (println "reading...")
          (let [form (read eddi false nil)]
            (try
              (cond (= (first form) 'defn)
                  (do
                    (println (first (rest form)) "...")
                    (pprint (generate-test-n form extra-vars) dickens)
                    (.write dickens "\n\n;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;\n\n")
                    ))
              (catch Exception any
                (.write dickens 
                  "\n\n;; ERROR while attempting to generate\n\n")))))
				(.write dickens "\n\n;; end of file ;;\n\n")
				(.flush dickens))))



