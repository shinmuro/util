(ns shinmuro.util
  (:require [clojure.string :as s]))

(defn dash->camel
  "lisp ライクな命名をキャメルケース化する。オプションで :lcc を指定すれば先頭のみ小文字にする。
    [例]
    (dash->camel \"one-two-three\")
    ;=> \"OneTwoThree\"

    (dash->camel \"one-two-three\" :lcc)
    ;=> \"oneTwoThree\""
  [^String s & opts]
  (as-> (s/split s #"-") x
        (if (= (first opts) :lcc)
          (concat (first x) (map s/capitalize (rest x)))
          (map s/capitalize x))
        (apply str x)))

(defn camel->dash
  "dash->camel の逆バージョン。"
  [^String s]
  (as-> (s/replace s #"([A-Z])" " $1") x
        (s/lower-case x)
        (s/triml x)
        (s/split x #" ")
        (s/join "-" x)))

(defn invoke-method
  "clojure.lang.Reflector/invokeInstanceMethod の Clojure 関数版。
   例外も少し分かりやすくしているつもり。"
  [target meth & args]
  (try (clojure.lang.Reflector/invokeInstanceMethod target meth (to-array args))
       (catch Exception e
         (throw (IllegalArgumentException. (.getMessage e))))))

(def ^:no-doc clj-invoke "非推奨。invoke-method へのエイリアス。"
  invoke-method)

(defn const->key
  " FOO_BAR -> :foo-bar にする。prefix があれば :prefix-foo-bar にする。"
  [^String s & [prefix]]
  (keyword (apply str prefix (s/replace (s/lower-case s) "_" "-"))))

(defn map->vec
  "マップエントリを keyseq で指定した順のベクタに変換する。"
  [m keyseq]
  (->> (mapv #(vector % (m %)) keyseq)
       (into [])))

(defmacro domap
  "map 版 doseq。複数のシーケンスをバインドさせた時、全ての組み合わせを行うのではなく、
   map と同様に先頭から順に処理し、一番短い所で終了する。残りのアイテムは無視する。"
  [seq-exprs & body]
  `(doall (map (fn ~(vec (take-nth 2 seq-exprs))
                 ~@body)
               ~@(take-nth 2 (rest seq-exprs)))))

(defn replace-val
  "map データで keyseq 中に指定されている key の値を v にする"
  [m keyseq v]
  (apply conj m (map vector keyseq (repeat v))))

(defn count-if 
  "各要素が述語関数 pred を満たす数をカウントする。"
  [pred coll]
  (count (filter pred coll)))

(def mapall
  "(comp doall map) と同じ。map のように複数シーケンスを副作用を伴いながら
   並列で進めたい場合に使う。doseq も複数シーケンスは対応しているが
   シーケンス数 * 各要素数で進む為用意。当然 lazy-seq ではなくなる。

   戻り値は map の結果シーケンスを返す。"
  (comp doall map))

(def maprun
  "(comp dorun map) と同じ。map のように複数シーケンスを副作用を伴いながら
   並列で進めたい場合に使う。doseq も複数シーケンスは対応しているが
   シーケンス数 * 各要素数で進む為用意。当然 lazy-seq ではなくなる。

   戻り値は dorun により常に nil を返す。"
  (comp dorun map))

(defn intable?
  "数値 n が小数部を含まなければ true を返す。"
  [n]
  (and (number? n)
       (zero? (- n (int n)))))

(defn tz-millis
  "user.timezone システムプロパティで取得できる時差をミリ秒で返す"
  []
  (-> (System/getProperty "user.timezone")
      java.util.TimeZone/getTimeZone
      .getRawOffset))
