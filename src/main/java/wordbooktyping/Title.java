package wordbooktyping;


import static spark.Spark.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;

public class Title {



	public static void main(String[]args){



		//public にあるファイルを使う宣言
		staticFileLocation("/public");
		//Web画面のspark.ModelAndViewでテンプレートの引数（viewvName)指定用String
		String layout ="templates/templatelayout.vtl";


		//sparkのgetmethodでは個人のウェブページ(localhost?)のURIと2つの引数が与えられる
		//１つめの引数は作成されたページのURL path(http://www.localhost4567:〇〇〇）
		//2つめの引数(request,responce -> "hellofriend"　はlambda
		//lambdaの書き方、引数 -> 返り値でかく
		//参考サイトhttp://emoson.hateblo.jp/entry/2014/10/14/022053
		//response内容が増えただけ、構文はかわらない
		//http://localhost:4567でみれるように
		get("/title", (request,responce) ->{
			HashMap<String, String> model = new HashMap<String, String>();
			model.put("templatelayout", "templates/title.vtl");
			return new ModelAndView(model, layout);
		},new VelocityTemplateEngine());




		get("/setting",(request,responce) ->{
			HashMap<String,String> model= new HashMap<String, String>();
			model = setting(/*request*/);
			return new ModelAndView(model, layout);
		},new VelocityTemplateEngine());


		get("/register",(request,replace) ->{
			HashMap<String,String> model =new HashMap<String,String>();
			registry(request);
			model = setting(/*request*/);
			return new ModelAndView(model, layout);
		},new VelocityTemplateEngine());

		/////////////////////////////////////////////////////////

		get("/delete",(request,replace) ->{
			HashMap<String, String> model = new HashMap<String,String>();
			delete(request);
			model = setting(/*request*/);
			model.put("templatelayout", "templates/setting.vtl");
			return new ModelAndView(model, layout);






		},new VelocityTemplateEngine());
		get("/playing", (request,responce)->{
			HashMap<String, String> model = new HashMap<String,String>();
			int ransu = createrandom();
			model = playing(ransu);
			return new ModelAndView(model, "templates/playing.vtl");
		},new VelocityTemplateEngine());


		get("/playing2", (request,responce)->{
			HashMap<String, String> model = new HashMap<String, String>();
			int ransu = createrandom();
			model = playing(ransu);
			clearcountup();
			return new ModelAndView(model, "templates/playing.vtl");
		},new VelocityTemplateEngine());



		get("/result",(request,resonce) ->{
			HashMap<String, String> model = new HashMap<String, String>();
			model = clearcountresultandclear();
			return new ModelAndView(model, layout);
		}, new VelocityTemplateEngine());


	}


	public static HashMap<String, String> setting(/*Request request*/){
		ArrayList<String> japanesewordlist = new ArrayList<String>();
		ArrayList<String> englishwordlist = new ArrayList<String>();
		HashMap<String, String> model= new HashMap<String, String>();

		try(WordBookDB db  = new WordBookDB()){
			String mysql = "select * from english order by id";
			System.out.println(mysql);
			//DBに接続
			db.open();
			//結果をまるっと取得
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					int id = rs.getInt("id");
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japanesewordlist.add(japanese);
					englishwordlist.add(english);
				}
					model.put("listsize", ""+japanesewordlist.size());
				 for(int i = 0; i<japanesewordlist.size(); i++){
					model.put("japanesekey", "" +japanesewordlist);
					model.put("englishkey", "" +englishwordlist);
				 }
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
//		System.err.println("Request parameters:"+(Arrays.asList(request.queryMap())));
		//データの数がわかる
//		System.err.println("count of params: " + request.queryMap().toMap().size());
		model.put("templatelayout", "templates/setting.vtl");
		return (model);
	}


	public static void registry(Request request){
		System.out.println(request);
		request.queryMap().toMap().entrySet().forEach(e ->
		System.err.println(e.getKey() + "=" + Arrays.toString(e.getValue())));

		System.err.println("Request parameters:"+(Arrays.asList(request.queryMap())));

		//データの数がわかる
		System.err.println("count of params: " + request.queryMap().toMap().size());


		WordBookDB db  = new WordBookDB();
		ArrayList<String> japanesewordlist = new ArrayList<String>();
		ArrayList<String> englishwordlist = new ArrayList<String>();
		String[] japanesecodearray = new String[30];
		String[] englishcodearray = new String[30];
		HashMap<String, String> model= new HashMap<String, String>();

		if(request.queryParams("register").equals("touroku")){

			String addjapanese = request.queryParams("japanese");
			String addenglish = request.queryParams("english");

				WordBookDB adddb = new WordBookDB();
			try{

				if(!addjapanese.equals("") && !addenglish.equals("")){

					String mysql = "insert into english(japanese, english)values('"+ addjapanese + "','" + addenglish +"')";
					System.out.println(mysql);

					//データベースに接続して閉じるまでの基本的な流れ
					//DBのメソッドをまとめたクラスWordBookDBのインスタンス化
					//DBに接続
					adddb.open();
					//（追加、削除、更新）するSQL文の実行（または検索するSQL文の実行）
					int num = adddb.executeUpdate(mysql);
					System.out.println(num +"件登録しました");

					model.put("japanese", addjapanese);
					model.put("english", addenglish);
					System.out.println(addjapanese +"="+addenglish);
				}

			}catch(BadSQLException e){
				System.out.println("登録できませんでした");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				adddb.close();
			}

		}

	}


	public static void delete(Request request){

		request.queryMap().toMap().entrySet().forEach(e ->
		System.err.println(e.getKey() + "=" + Arrays.toString(e.getValue())));






		System.err.println("Request parameters:"+(Arrays.asList(request.queryMap())));

		//データの数がわかる
		System.err.println("count of params: " + request.queryMap().toMap().size());


		WordBookDB db  = new WordBookDB();
		ArrayList<String> japanesewordlist = new ArrayList<String>();
		ArrayList<String> englishwordlist = new ArrayList<String>();
		String[] japanesecodearray = new String[30];
		String[] englishcodearray = new String[30];
		HashMap<?, ?> model= new HashMap<Object, Object>();
		/////////////////////////////////////////////////

		if(!request.queryParams("delete").equals(null)){



			String deleteparam = request.queryParams("delete");



				WordBookDB deletedb = new WordBookDB();
			try{

					//データベースに接続して閉じるまでの基本的な流れ
					//DBのメソッドをまとめたクラスWordBookDBのインスタンス化
					//DBに接続
					deletedb.open();
					//（追加、削除、更新）するSQL文の実行（または検索するSQL文の実行）

					String[] deleteparamsplit = deleteparam.split(" ");
					for(int i = 0; i< deleteparamsplit.length; i++){
						System.out.println(deleteparamsplit[i]);
					}

					String deletemysql = "delete from english where japanese = '"+ deleteparamsplit[0] +"';";
					System.out.println(deletemysql);


					int num = deletedb.executeUpdate(deletemysql);
					System.out.println(num +"件削除しました");

			}catch(BadSQLException e){
				System.out.println("削除できませんでした");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				deletedb.close();
			}


		}

	}


	public  static HashMap<String, String> playing(int ransu){




		Random rnd = new Random();

		WordBookDB db  = new WordBookDB();
		ArrayList<String> japanesewordlist = new ArrayList<String>();
		ArrayList<String> englishwordlist = new ArrayList<String>();
		String[] japanesearray = null;
		String[] englisharray =null;
		char[] answercharAt;
		int answerlength;
		int clearcount =0;


		HashMap<String,String> model = new HashMap<String,String>();

		int countwordlist =0;
		try{
			String mysql = "select * from english order by id";
			System.out.println(mysql);

			//DBに接続
			db.open();
			//結果をまるっと取得
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					int id = rs.getInt("id");
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japanesewordlist.add(japanese);
					englishwordlist.add(english);
					countwordlist++;
				}

				japanesearray = new String[japanesewordlist.size()];
				japanesearray = (String[])japanesewordlist.toArray(new String[]{});
				englisharray = new String[englishwordlist.size()];
				englisharray = (String[]) englishwordlist.toArray(new String[]{});;

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.close();

		}



		model.put("question",japanesearray[ransu]);
		model.put("answer", englisharray[ransu]);

		System.out.println(englisharray[ransu]);

//////////////////////////////////////////////////////////////////////////////
		answercharAt = new char[englisharray[ransu].length()];

		for(int j =0; j<answercharAt.length; j++){
			answercharAt[j] = englisharray[ransu].charAt(j);
			model.put("answercharAt"+j, ""+answercharAt[j]);
		}

		model.put("answerlength", ""+answercharAt.length);

		System.out.println(answercharAt.length);
		System.out.println(answercharAt[0]);

		return (model);

	}


	//ＤＢに登録してあるデータの数の範囲内で乱数をつくるためのメソッド
	public static int createrandom(){

		ArrayList<String> japanesewordlist = new ArrayList<String>();
		Random rnd = new Random();

		try(WordBookDB db  = new WordBookDB()){
			String mysql = "select * from english order by id";
			//DBに接続
			db.open();
			//結果をまるっと取得
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					japanesewordlist.add(japanese);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		int ransu = rnd.nextInt(japanesewordlist.size());
		return ransu;

	}
	//////////////////////////////////////乱数をつくるメソッドにテストコードはつくれる？？＝＝＝＝＝＝＝＝＝＝

	public static void clearcountup(){
		int clearcount = 0;
		try(WordBookDB selectclearcountdb = new WordBookDB()){
			selectclearcountdb.open();
			String mysqlselectclearcount = "select * from clearcounttable;";
			System.out.println(mysqlselectclearcount);
			//結果をまるっと取得
			ResultSet rsclearcount =   selectclearcountdb.executeQuery(mysqlselectclearcount);
			try{
				while(rsclearcount.next()){
					clearcount = rsclearcount.getInt("clearcount");
				}
				clearcount = clearcount + 1;
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//DBに接続
		try(WordBookDB updateclearcountdb = new WordBookDB()){
			String mysqlupdateclearcount = "update clearcounttable set clearcount = '" + clearcount + " ';";
			System.out.println(mysqlupdateclearcount);
			updateclearcountdb.open();
			//結果をまるっと取得
			try{
				int num = updateclearcountdb.executeUpdate(mysqlupdateclearcount);
				System.out.println(num + "件更新しました");
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public static HashMap<String, String> clearcountresultandclear(){
		int clearcount =0;
		HashMap model = new HashMap<Object, Object>();
		//DBに接続
		try(WordBookDB selectclearcountdb = new WordBookDB()){
			selectclearcountdb.open();
			String mysqlselectclearcount = "select * from clearcounttable;";
			System.out.println(mysqlselectclearcount);
			//結果をまるっと取得
			ResultSet rsclearcount =   selectclearcountdb.executeQuery(mysqlselectclearcount);

			try{
				while(rsclearcount.next()){
					clearcount = rsclearcount.getInt("clearcount");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		model.put("clearcount", clearcount);
		model.put("templatelayout", "templates/result.vtl");
			WordBookDB deleteclearcountdb = new WordBookDB();
		try{
				String deleteclearcountmysql = "update clearcounttable set clearcount = 0";
				System.out.println(deleteclearcountmysql);
				//データベースに接続して閉じるまでの基本的な流れ
				//DBのメソッドをまとめたクラスWordBookDBのインスタンス化
				//DBに接続
				deleteclearcountdb.open();
				//（追加、削除、更新）するSQL文の実行（または検索するSQL文の実行）
				int num = deleteclearcountdb.executeUpdate(deleteclearcountmysql);
				System.out.println(num +"件解答結果を削除しました");
		}catch(BadSQLException e){
			System.out.println("削除できませんでした");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			deleteclearcountdb.close();
		}


		return (model);
	}







}
