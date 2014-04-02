(ns testgen.core)

(defn write-test [fnname arg] 
     (try
     	(list 'is (list '= (list fnname arg) (eval (list fnname arg))))
	(catch Exception e (list 'is (list 'thrown? (.getClass e) (list fnname arg))))))

(defn constant? [arg]
	(not (or
		(symbol? arg)
		(seq? arg)
		(vector? arg)
		(map? arg))))

(def generic-args '(nil () true "test" :test 0 Integer/MAX_VALUE 0.0001 -0.0001))

(defn find-interesting-args [sexpr]
	"Find things in sexpr which would be interesting if passed as arguments to it"
	(filter constant? (flatten sexpr)))

(defn find-more-interesting-args [sexpr]
	"Find things in sexpr which would be even more interesting if passed as arguments to it"
	(concat generic-args
		(flatten 
			(map 
				#(cond 
					(number? %) (list % (inc %) (dec %)) 
					true %) 
				(find-interesting-args sexpr)))))

(defn testgen [fndef] 
	(cond (= (first fndef) 'defn)
		(let [name (first (rest fndef))]
			(list 'deftest (symbol (str "test-" name))
				(map #(write-test name %) (find-more-interesting-args fndef))))))

