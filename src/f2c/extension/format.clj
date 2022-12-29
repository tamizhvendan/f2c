(ns f2c.extension.format
  (:require [clojure.string :as str])
  (:import [java.text NumberFormat]
           [java.util Locale Currency]))


(defn initial [name]
  (str/upper-case (str (first name) (second name))))

(defn- currency-number-format [currency]
  (case currency
    "INR" (NumberFormat/getCurrencyInstance (new Locale "en" "IN"))
    nil))

(defn humanize-price
  ([currency amount]
   (humanize-price currency amount 0))
  ([currency amount digits]
   (if-let [number-format (currency-number-format currency)]
     (do
       (.setMinimumFractionDigits number-format digits)
       (.setMaximumFractionDigits number-format digits)
       (.format number-format amount))
     (format "%s %s" currency amount))))

(defn currency-symbol [currency]
  (.getSymbol (Currency/getInstance currency)))