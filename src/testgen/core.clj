(ns testgen.core)

(defn write-test [fnname arg] 
     (try
     	(list 'is (list '= (list fnname arg) (eval (list fnname arg))))
	(catch Exception e (list 'is (list 'thrown? (.getClass e) (list fnname arg))))))


(defn testgen [fndef] nil)
