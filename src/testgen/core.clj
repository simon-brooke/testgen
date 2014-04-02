(ns testgen.core)

(defn write-test [fnname arg] 
     (try
     	(list 'is (list '= (list fnname arg) (eval (list fnname arg))))
	(catch Exception e (list 'is (list 'thrown? (.getClass e) (list fnname arg))))))

(defn testgen [fndef] nil)

(defn constant? [x] (not (symbol? x)))

(defn constants [form]
  (filter constant? (flatten form)))

(defn gen-tests [fnname form]
  (map (partial write-test fnname) (constants form)))

(comment
  (def is-four (fn is-four [x] (= 88 [44])))
  (gen-tests 'is-four '(fn is-four [x] (= 88 [44])))
  )
