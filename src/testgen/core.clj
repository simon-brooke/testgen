(ns testgen.core)

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
	(cond (= (first fndef) 'defn)
		(let [name (first (rest fndef))]
			(concat (list 'deftest (symbol (str "test-" name)))
				(map #(write-test name %) (find-interesting-args fndef))))))

;; (defn gen-tests [fnname form]
;;   (map (partial write-test fnname) (constants form)))


