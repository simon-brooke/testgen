(ns testgen.core)

(defn write-test [fnname arg] 
     (try
     	(list 'is (list '= (list fnname arg) (eval (list fnname arg))))
	(catch Exception e (list 'is (list 'thrown? (.getClass e) (list fnname arg))))))

(defn testgen [fndef] nil)

(defn is-constant? [x] (not (symbol? x)))

(defn constants [form]
  (filter is-constant? (flatten form)))

(comment
  (constants '(fn is-four [x] (= 88 [44])))
  )
