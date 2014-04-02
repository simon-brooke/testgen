(ns testgen.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn write-test [fnname arg] 
     (list 'is (list '= (list fnname arg) (eval (list fnname arg)))))

(defn testgen [fndef] nil)
