(ns testgen.core
  (:use clojure.java.io
        clojure.pprint))

(defn write-test [fnname arg]
     (try
     	(list 'is (list '= (list fnname arg) (list 'quote (eval (list fnname arg)))))
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

(defn find-interesting-args [sexpr]
	"Find things in sexpr which would be even more interesting if passed as arguments to it"
	(concat generic-args
		(flatten
			(map
				#(cond
					(integer? %) (list % (inc %) (dec %))
					(number? %) (list % (+ % 0.0001) (- % 0.0001))
					true %)
				(constants sexpr)))))

(defn testgen [fndef]
	(cond (or (= (first fndef) 'def)(= (first fndef) 'defn))
		(let [name (first (rest fndef))]
			(concat (list 'deftest (symbol (str "test-" name)))
				(map #(write-test name %) (find-interesting-args fndef))))))

;; (defn gen-tests [fnname form]
;;   (map (partial write-test fnname) (constants form)))

(defn clean-filename [filename]
  "remove the trailing '.clj' from a Clojure file name"
  (cond
   (.endsWith filename ".clj") (.substring filename 0 (- (count filename) 4))
   true filename))

(defn packagename-from-filename [filename]
  "Return, as a symbol, the package name associated with this filename. There's
  probably a better way of doing this."
  (let [fn (clean-filename filename)]
    (symbol (.replace fn "/" "."))))


(defn generate-tests [filename]
  "Generate a suite of characterisation tests for the file indicated by this filename.

  filename: the file path name of a file containing Clojure code to be tested."
  (try
    (let [fn (clean-filename filename)
          pn (packagename-from-filename filename)]
      ;; load the file so that any functions in it are usable
      ;; (load fn)
      ;; (refer pn)
      (with-open [eddie (java.io.PushbackReader. (reader filename))
                  dickens (writer "output")]
          (while (.ready eddie)
            (let [form (macroexpand (read eddie))]
              (cond (= (first form) 'def)
                (pprint (testgen form) dickens))))))
    (catch Exception eof)))
