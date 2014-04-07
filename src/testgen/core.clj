(ns testgen.core
  (:use clojure.java.io
        clojure.pprint))

(defn maybe-quote [val]
	"Convert val into a form in which, after being passed through the pretty
	printer, it will be reconstituted in a form useful to the test"
	(cond
		(symbol? val) (list 'symbol (str val))
		true (list 'quote val)))

(defn generate-assertion [fnname arg]
  "Generate an appropiate assertion for this argument passed to this function"
	(try
		(let [val (eval (list fnname arg))]
		   	(list 'is (list '= (list fnname arg) (maybe-quote val))))
			  (catch Exception e (list 'is (list 'thrown? (.getClass e) (list fnname arg))))))

(defn constant? [arg]
	(not (or
		(symbol? arg)
		(seq? arg)
		(vector? arg)
		(map? arg))))

(def generic-args '(nil () (quote (a :b "c")) true "test" :test 0 Integer/MAX_VALUE 22/7 0.0001 -0.0001))

(defn constants [form]
	"return a list of all elements in this form which are constants"
  (filter constant? (flatten form)))

(defn find-interesting-args [sexpr extra-vars]
	"Find things in sexpr which would be even more interesting if passed as arguments to it"
	(concat generic-args extra-vars
		(flatten
			(map
				#(cond
					(integer? %) (list % (inc %) (dec %))
					(number? %) (list % (+ % 0.0001) (- % 0.0001))
					true %)
				(constants sexpr)))))

(defn generate-test [fndef extra-vars]
	(cond (or (= (first fndef) 'def)(= (first fndef) 'defn))
		(let [name (first (rest fndef))]
			 (list 'deftest (symbol (str "test-" name))
				(concat (list 'testing (str name))
					(map #(generate-assertion name %)
						(find-interesting-args fndef extra-vars)))))))

;; generating a test file

(defn clean-filename [filename]
  "remove the leading 'src/' and trailing '.clj' (if present) from a Clojure file name"
  (let [without-suffix (cond
   		(.endsWith filename ".clj") (.substring filename 0 (- (count filename) 4))
   		true filename)
	prefix-position (.indexOf filename "src/")]
	(cond (> prefix-position -1) (.substring without-suffix (+ prefix-position 4))
		true without-suffix)))

(defn test-filename [filename]
	"return an approximately-correct filename in which to save tests"
	(let [prefix-position (.indexOf filename "src/")
				prefix (cond (> prefix-position -1) (.substring filename 0 prefix-position)
									true "")]
		(str prefix "test/" (clean-filename filename) "_test.clj")))


(defn packagename-from-filename [filename]
  "Return, as a symbol, the package name associated with this filename. There's
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

  filename: the file path name of a file containing Clojure code to be tested."
    (let [fn (clean-filename filename)
          pn (packagename-from-filename filename)
					extra-vars (find-vars-in-file filename)]
			(println "Read vars: " extra-vars)
			(println "Writing to: " (test-filename filename))
      ;; load the file so that any functions in it are usable
      (load fn)
      (refer pn)
			(with-open [eddi (java.io.PushbackReader. (reader filename))
                  dickens (writer (test-filename filename))]
        (write-header dickens pn)
				(while (.ready eddi)
          (println "reading...")
          (let [form (read eddi false nil)]
            (cond (= (first form) 'defn)
                  (do
                    (println (first (rest form)) "...")
                    (pprint (generate-test form extra-vars) dickens)
                    (.write dickens "\n\n;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;\n\n")
                    ))))
				(.write dickens "\n\n;; end of file ;;\n\n")
				(.flush dickens))))



