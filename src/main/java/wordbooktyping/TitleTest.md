テスト駆動開発（ＴＤＤ）のやりかた（twadaさん）


ToDoリストの作成
Junitファイル（Testファイル）の作成

	前準備（コンパイルエラーをなくす）
	実行
	検証（ミューテーションテスティング←かなり時間がかかるので仮実装をする）

メインのメソッドでreturn"1"だけにしてテストクラスでほんとうに1が返ってくるかテストする
→テストコードがしっかり実装できているかの証明にもなる

assertEqualsはテストメソッド1つにつき1つがいい
→どこで落ちだかわかる、それよりも後ろのassertEqualsがうまくいっているかわからないから

GreenになればGreenになることを確認しながらリファクタリングしコードをきれいにする

テストは増やすのは簡単だけど、減らすのは難しい
実装した人が用済みのテストケースは必要最小限まで減らす

ＴＤＤのスキル
・問題を小さく分割する
・歩幅を調整する
	・テスト→仮実装→三角測量→実装
	・テスト→仮実装→実装
	・テスト→明白な実装
・テストの構造化とリファクタリング


前準備のメソッドは@Beforeをつける

＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

ToDOリスト
===================================================


- [ ] setingメソッドのテスト
	-[ ] Requestクラスの変数を受け取り、englishテーブルの
	内容をid順にStringクラスのjapaneseとStringクラスのenglish
	としてArrayListのjapanesewordlist、englishwordlistにそれ
	ぞれ追加、それぞれを配列型のjapansesearray、englisharrayに
	格納しなおしす。
	配列型のjapanesecodearray、englishcodearrayを30要素分用
	意し、japanese1、japanese2・・・english1、english2・・・の
	ような形でString型で格納。コレクションのHashMap型のmodel
	を使い、janesecodearray、englishcodearrayの対応する番号を
	キーとして同番号のjapanesearray、englisharrayを関連付け
	る。ただし、何も格納されていないjapanesearray、
	englisharrayはnullとしてキーと関連付ける。
	templatelayoutをキーとして、templates/setting.vtlファイル
	を関連付けて、modelを返す
