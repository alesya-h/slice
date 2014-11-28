(ns slice.document)

(defmacro with-document-zipper-> [& body]
  `(update-document
    (fn [zipper#]
      (-> zipper#
          ~@body))))
