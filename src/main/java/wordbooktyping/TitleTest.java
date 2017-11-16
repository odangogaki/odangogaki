package wordbooktyping;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TitleTest extends Title {

	static ArrayList<String> japanesewordlist = new ArrayList<String>();
	static ArrayList<String> englishwordlist = new ArrayList<String>();

	static HashMap<String ,String> actualmodel = new HashMap<String, String>();
	static String japanese;
	static String english;
	static char[] answercharAt ;


	//初期設定としてデータはDBのjapaneseカラムにテスト0、テスト1、englishカラムにtest0、test1、idはあるがauto_increment
	@BeforeClass
	public static void 前準備としてテスト前のデータをリストに逃がしDBのデータを削除後テスト用のデータを入れる(){
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japanesewordlist.add(japanese);
					englishwordlist.add(english);
				}
				String deletemysql = "delete from english";
				db.executeUpdate(deletemysql);
				for(int i = 0; i<2; i++){
					String testmysql = "insert into english(japanese , english)values('テスト" + i+ "' , 'test"+ i +"');";
					db.executeUpdate(testmysql);
				}
				String testdataclearcountmysql = "update clearcounttable set clearcount = 825";
				db.executeUpdate(testdataclearcountmysql);

			}catch(BadSQLException e){
				System.out.print("テスト用設定の登録、もしくは削除できませんでした");
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@AfterClass
	public static void テスト前のデータを復活させる(){
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String deletemysql = "delete from english";
			db.executeUpdate(deletemysql);
			for(int j = 0; j<japanesewordlist.size(); j++){
				String mysql = "insert into english(japanese , english)values('"+ japanesewordlist.get(j) + "' , '" + englishwordlist.get(j) + "');";
				db.executeUpdate(mysql);
			}
			String deleteclearcountmysql = "update clearcounttable set clearcount = 0";
			db.executeUpdate(deleteclearcountmysql);
		}catch(BadSQLException e){
			System.out.println("aftercareの登録、もしくはclearcountの初期化ができませんでした");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

//===============settingメソッドのテスト開始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝//
//HashMapのジェネリクスを外すとassertThatにエラーが発生（junitのフレームワークの問題）
	@Test
	public void 設定ページのvtlファイルが返されるか確認() throws Exception{
		assertThat(Title.setting() ,hasEntry("templatelayout" , "templates/setting.vtl"));
	}

	@Test
	public void 登録してある単語の対の数listsizeが返されるか確認() throws Exception{
		assertThat(Title.setting() ,hasEntry("listsize" , "2"));
	}

	@Test
	public void 登録してある日本語の単語japanesewordlist配列が返されるか確認() throws Exception{
		assertThat(Title.setting() ,hasEntry("japanesekey" , "[テスト0, テスト1]"));
	}

	@Test
	public void 登録してある英語の単語englishwordlist配列が返されるか確認() throws Exception{
		assertThat(Title.setting() ,hasEntry("englishkey" , "[test0, test1]"));
	}

	@Test
	public void DBのデータに余分なものがないか個数を確認varSetting() throws Exception{

		assertThat(Title.setting().size() ,is(4));

	}

//===============settingメソッドのテスト終了＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝//



//===============registryメソッドのテスト開始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝//

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にregisterキーで登録されたtourokuの値がある場合japaneseの登録内容が正しく登録されているか確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "登録テスト";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//				model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("japanesekey", "[テスト0, テスト1, 登録テスト]"));

				String deletemysql = "delete from english where japanese = '登録テスト';";
				db.executeUpdate(deletemysql);

			}catch(BadSQLException e){
				System.out.print("登録テスト用のデータの削除ができませんでした");
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にregisterキーで登録されたtourokuの値がある場合englishの登録内容が正しく登録されているか確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "登録テスト";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//				model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("englishkey", "[test0, test1, registertest]"));

				String deletemysql = "delete from english where english = 'registertest';";
				db.executeUpdate(deletemysql);

			}catch(BadSQLException e){
				System.out.print("登録テスト用のデータの削除ができませんでした");
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にregisterキーで登録されたtourokuの値がある場合登録内容に余分なものがないか確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();

		String registervalue = "touroku";
		String japanesevalue = "登録テスト";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

				assertThat(japaneseregistry.size(), is(3));

				String deletemysql = "delete from english where english = 'registertest';";
				db.executeUpdate(deletemysql);
			}catch(BadSQLException e){
				System.out.print("登録テスト用のデータの削除ができませんでした");
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//registerキーの内容がtourokuでない場合
	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にregisterキーで登録されたtourokuの値がない場合japaneseの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "間違えたー！";
		String japanesevalue = "登録テスト";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//				model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("japanesekey", "[テスト0, テスト1]"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にregisterキーで登録されたtourokuの値がない場合englishの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "間違えたー！";
		String japanesevalue = "登録テスト";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//				model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("englishkey", "[test0, test1]"));
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にregisterキーで登録されたtourokuの値がない場合登録内容に余分なものがないか確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();

		String registervalue = "間違えたー！";
		String japanesevalue = "登録テスト";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

				assertThat(japaneseregistry.size(), is(2));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


//registerキーの内容がtourokuの場合
	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にjapaneseキーで登録されたjapaneseの値がない場合japaneseの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//					model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("japanesekey", "[テスト0, テスト1]"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にjapaneseキーで登録されたjapaneseの値がない場合englishの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//					model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("englishkey", "[test0, test1]"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にenglishキーで登録されたenglishの値がない場合englishの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "登録テスト";
		String englishvalue = "";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//					model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("englishkey", "[test0, test1]"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にenglishキーで登録されたjapaneseの値がない場合englishの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "登録テスト";
		String englishvalue = "";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//					model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("japanesekey", "[テスト0, テスト1]"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にjapaneseキーで登録されたjapaneseの値がない場合登録内容に余分なものがないか確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();

		String registervalue = "touorku";
		String japanesevalue = "";
		String englishvalue = "registertest";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

				assertThat(japaneseregistry.size(), is(2));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にenglishキーで登録されたenglishの値がない場合登録内容に余分なものがないか確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();

		String registervalue = "touorku";
		String japanesevalue = "登録テスト";
		String englishvalue = "";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

				assertThat(japaneseregistry.size(), is(2));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


//japaneseもenglishもどっちもあってない
	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にenglishキーで登録されたenglishの値とjapaneseキーで登録されたjapaneseの値がどちらもない場合englishの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "";
		String englishvalue = "";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//					model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("englishkey", "[test0, test1]"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にenglishキーで登録されたenglishの値とjapaneseキーで登録されたjapaneseの値がどちらもない場合japaneseの内容が登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();
		HashMap<String, String> model = new HashMap<String, String>();

		String registervalue = "touroku";
		String japanesevalue = "";
		String englishvalue = "";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

//					model.put("listsizeafterregistry", japaneseregistry.size());
				model.put("japanesekey", ""+japaneseregistry);
				model.put("englishkey", ""+englishregistry);

				assertThat(model, hasEntry("japanesekey", "[テスト0, テスト1]"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void registryメソッドでクライアントから受け取ったRequestの情報にenglishキーで登録されたenglishの値とjapaneseキーで登録されたjapaneseの値がどちらもない場合登録内容に余分なものが登録されていないことの確認()throws Exception{
		ArrayList<String> japaneseregistry = new ArrayList<>();
		ArrayList<String> englishregistry = new ArrayList<>();

		String registervalue = "touorku";
		String japanesevalue = "";
		String englishvalue = "";

		Title.registry(registervalue, japanesevalue, englishvalue);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					japaneseregistry.add(japanese);
					englishregistry.add(english);
				}

				assertThat(japaneseregistry.size(), is(2));

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//=================registryメソッドのテスト終了==========================


//=================deleteメソッドのテスト開始==========================



//消去したい文字列が何らかの理由で存在しない場合
	@Test
	public void deleteメソッドで消去したい文字列が何らかの理由で選択できていない場合のDBのjapaneseのデータが変わらないことの確認() throws Exception{

		String valueOfdelete = "";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();
		Title.delete(valueOfdelete);
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(deleteTestjapanese,contains("テスト0", "テスト1"));
	}

	@Test
	public void deleteメソッドで消去したい文字列が何らかの理由で選択できていない場合のDBのenglishのデータが変わらないことの確認() throws Exception{

		String valueOfdelete = "";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();
		Title.delete(valueOfdelete);
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(deleteTestenglish,contains("test0", "test1"));
	}

	@Test
	public void deleteメソッドで消去したい文字列が何らかの理由で選択できていない場合のDBのデータの数が変わらないことの確認() throws Exception{

		String valueOfdelete = "";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();
		Title.delete(valueOfdelete);
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(deleteTestjapanese.size(),is(2));
	}


//消去したい文字列が選択できている場合
	@Test
	public void deleteメソッドで消去したい文字列がテスト0equaltest0場合のDBのjapaneseのデータが削除されていることの確認() throws Exception{

		String valueOfdelete = "テスト0 = test0";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();
		Title.delete(valueOfdelete);
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
				assertThat(deleteTestjapanese,contains("テスト1"));
				Title.delete("テスト1 = test1");
				String insertmysql = "insert into english(japanese, english)values('テスト0', 'test0'),('テスト1', 'test1');";
				db.executeUpdate(insertmysql);

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void deleteメソッドで消去したい文字列がテスト0equaltest0場合のDBのenglishのデータが削除されていることの確認() throws Exception{

		String valueOfdelete = "テスト0 = test0";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();
		Title.delete(valueOfdelete);
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
				assertThat(deleteTestenglish,contains("test1"));
				Title.delete("テスト1 = test1");
				String insertmysql = "insert into english(japanese, english)values('テスト0', 'test0'),('テスト1', 'test1');";
				db.executeUpdate(insertmysql);
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void deleteメソッドで消去したい文字列がテスト0equaltest0場合のDBのデータの数が1つ減っていることの確認() throws Exception{

		String valueOfdelete = "テスト0 = test0";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();
		Title.delete(valueOfdelete);
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
				assertThat(deleteTestjapanese.size(),is(1));
				Title.delete("テスト1 = test1");
				String insertmysql = "insert into english(japanese, english)values('テスト0', 'test0'),('テスト1', 'test1');";
				db.executeUpdate(insertmysql);
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	@Test
	public void deleteメソッドで消去したい文字列がテスト1equaltest1場合のDBのjapaneseのデータが削除されていることの確認() throws Exception{

		String valueOfdelete = "テスト1 = test1";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();
		Title.delete(valueOfdelete);
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
				assertThat(deleteTestjapanese,contains("テスト0"));
				Title.delete("テスト0 = test0");
				String insertmysql = "insert into english(japanese, english)values('テスト0', 'test0'),('テスト1', 'test1');";
				db.executeUpdate(insertmysql);
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void deleteメソッドで消去したい文字列がテスト1equaltest1場合のDBのenglishのデータが削除されていることの確認() throws Exception{

		String valueOfdelete = "テスト1 = test1";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();

		Title.delete(valueOfdelete);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
				assertThat(deleteTestenglish,contains("test0"));
				Title.delete("テスト0 = test0");
				String insertmysql = "insert into english(japanese, english)values('テスト0', 'test0'),('テスト1', 'test1');";
				db.executeUpdate(insertmysql);
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void deleteメソッドで消去したい文字列がテスト1equaltest1場合のDBのデータの数が1つ減っていることの確認() throws Exception{

		String valueOfdelete = "テスト1 = test1";
		ArrayList<String> deleteTestjapanese = new ArrayList<String>();
		ArrayList<String> deleteTestenglish = new ArrayList<String>();

		Title.delete(valueOfdelete);

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from english order by id";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					deleteTestjapanese.add(japanese);
					deleteTestenglish.add(english);
				}
				assertThat(deleteTestjapanese.size(),is(1));
				Title.delete("テスト0 = test0");
				String insertmysql = "insert into english(japanese, english)values('テスト0', 'test0'),('テスト1', 'test1');";
				db.executeUpdate(insertmysql);
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//=================deleteメソッドのテスト終了==========================



//=================playingメソッドのテスト開始==========================
	@Test
	public void 乱数が0の場合のenglishの文字列の長さが正しくDBから取得できているかの確認(){
		assertThat(Title.playing(0),hasEntry("answerlength", "5"));
	}

	@Test
	public void 乱数が0の場合の問題文であるjapaneseが正しくDBから取得できているかの確認(){
		assertThat(Title.playing(0),hasEntry("question", "テスト0"));
	}


	@Test
	public void 乱数が0の場合の答えであるenglishが正しくDBから取得できているかの確認(){
		assertThat(Title.playing(0),hasEntry("answer", "test0"));
	}


	@Test
	public void 乱数が0の場合の答えであるenglishの文字列が正しく1文字ずつに分解されてそれぞれ正しく格納されているかの確認(){
		char answercharAt[] =  {'t','e','s','t','0'};
		for(int i= 0; i<5; i++){
			assertThat(Title.playing(0),hasEntry("answercharAt"+i, ""+answercharAt[i]));
		}
	}


	@Test
	public void 乱数が1の場合のenglishの文字列の長さが正しくDBから取得できているかの確認(){
		assertThat(Title.playing(1),hasEntry("answerlength", "5"));
	}

	@Test
	public void 乱数が1の場合の問題文であるjapaneseが正しくDBから取得できているかの確認(){
		assertThat(Title.playing(1),hasEntry("question", "テスト1"));
	}


	@Test
	public void 乱数が1の場合の答えであるenglishが正しくDBから取得できているかの確認(){
		assertThat(Title.playing(1),hasEntry("answer", "test1"));
	}


	@Test
	public void 乱数が1の場合の答えであるenglishの文字列が正しく1文字ずつに分解されてそれぞれ正しく格納されているかの確認(){
		char answercharAt[] =  {'t','e','s','t','1'};
		for(int i= 0; i<5; i++){
			assertThat(Title.playing(1),hasEntry("answercharAt"+i, ""+answercharAt[i]));
		}
	}

	@Test
	public void 乱数が0の時DBのデータに余分なものがないか個数を確認verplaying() throws Exception{

		assertThat(Title.playing(0).size() ,is(8));

	}

	@Test
	public void 乱数が1の時DBのデータに余分なものがないか個数を確認verplaying() throws Exception{

		assertThat(Title.playing(1).size() ,is(8));

	}





//=================playingメソッドのテスト終了======================


//=================createrandomメソッドのテスト開始======================

	public void createrandomTest(){

	}


//=================createrandomメソッドのテスト終了======================

//=================clearcountupメソッドのテスト開始======================
	@Test
	public void clearcountupメソッドを１回行うとDBのclearcounttableのclearcountが1増えるかの確認(){
		int clearcountbegined =0;
		int clearcountended = 0;

		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from clearcounttable";
			db.open();
			ResultSet rsbegined = db.executeQuery(mysql);
			try{
				while(rsbegined.next()){
					clearcountbegined = rsbegined.getInt("clearcount");
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			Title.clearcountup();

			ResultSet rsended = db.executeQuery(mysql);
			try{
				while(rsended.next()){
					clearcountended = rsended.getInt("clearcount");
				}

			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat((clearcountended - clearcountbegined), is(1));
	}
//=================clearcountupメソッドのテスト終了======================

//=================clearcountresultandclearメソッドのテスト開始======================
//このテストはclearcountupメソッドのテストを行う前に行われていれば設定値の825を実際の値として返すが、
//上のclearcountupメソッドのテストを行うと、DB上のclearcountが1増えるので、設定値より1多い826を実際の値として返す
	@Test
	public void 正しいクリアした回数をDBから取得しているかの確認(){
		assertThat(Title.clearcountresultandclear(), hasEntry("clearcount", 825));
	}

	@Test
	public void 設定ページのvtlファイルが返されるか確認verClearCountResultAndClear(){
		assertThat(Title.clearcountresultandclear(), hasEntry("templatelayout", "templates/result.vtl"));
	}

	@Test
	public void DBのクリア回数の値clearcountが0に初期化されているか確認(){
		int clearcount = 19900825;
		Title.clearcountresultandclear();
		try(WordBookDB db = new WordBookDB()){
			String mysql = "select * from clearcounttable";
			db.open();
			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					clearcount = rs.getInt("clearcount");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		assertThat(clearcount, is(0));
	}

	@Test
	public void DBのデータに余分なものがないか個数を確認verClearCountResultAndClear() throws Exception{

		assertThat(Title.clearcountresultandclear().size() ,is(2));

	}



//=================clearcountresultandclearメソッドのテスト終了======================

}
