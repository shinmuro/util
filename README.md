shinmuro.util
================================================================================

**JAPANESE DOCUMENTATION ONLY**

INTERNAL miscellaneous utility functions.

内部的に使用してるユーティリティ関数集。個人的に利用頻度が高いのをここにまとめてます。
個々の関数は恐らくネットを探せば似たようなのがゴロゴロ転がっていると思います。

## Usage

``project.clj`` に以下追加して``lein`` ``deps``します。
```clojure
[shinmuro/util "x.x.x"]
```

後は [API doc](http://shinmuro.github.io/util/doc/) 見て気に入ったのあれば、使いたい所で以下など追加してお使い下さい。
```clojure
(use 'shinmuro.util)
```

## History

### 0.4.0 2014-12-09
- 初期パブリックリリース。0.4.0 以前はローカル環境で管理していた。

## License

Copyright © 2014 shinmuro

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
