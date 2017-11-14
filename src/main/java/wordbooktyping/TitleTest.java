package wordbooktyping;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.Request;

public class TitleTest extends Title {

	static ArrayList<String> japanesewordlist = new ArrayList<String>();
	static ArrayList<String> englishwordlist = new ArrayList<String>();

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
			}catch(BadSQLException e){
				System.out.print("登録、もしくは削除できませんでした");
			}catch(Exception e){
				e.printStackTrace();
			}
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
		}catch(BadSQLException e){
			System.out.println("aftercareは登録できませんでした");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
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
	public void DBのデータに余分なものがないか個数を確認() throws Exception{

		assertThat(Title.setting().size() ,is(4));

	}




	@Test
	public void registryTest() throws Exception{

	}




	@Test
	public void deleteTest() throws Exception{


	}



	public void playingTest(){

		Title title = new Title();
		HashMap model = new HashMap();
		Request request = null;






		assertEquals(model,title.playing(request));


	}


	public void clearcountupTest(){

	}


	public void clearcountresultandclearTest(){

		Title title = new Title();
		HashMap model = new HashMap();
		Request request = null;




		assertEquals(model,Title.playing(request));

	}

}
