package wordbooktyping;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WordBookDBTest extends WordBookDB {

//あれ・・・これいる？

	@Before
	public  void 前準備としてテスト用のデータを入れる(){
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String testdataclearcountmysql = "update clearcounttable set clearcount = 825";
			db.executeUpdate(testdataclearcountmysql);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@After
	public  void テスト前のデータを削除しclearcount0１行で初期設定する(){
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String testdatadeletemysql = "delete from clearcounttable";
			String clearcount0insertmysql = "insert into clearcounttable(clearcount)values(0)";
			db.executeUpdate(testdatadeletemysql);
			db.executeUpdate(clearcount0insertmysql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
//===============executeQueryメソッドのテスト開始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝//
	@Test
	public void DBのselect文が正しい内容をとってくるか確認() throws Exception{
		int testdata = 0;
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String mysql = "select * from clearcounttable";
			ResultSet rs = db.executeQuery(mysql);
			while(rs.next()){
			testdata = rs.getInt("clearcount");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(testdata, is(825));
	}

//===============executeQueryメソッドのテスト終了＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝//

//===============executeUpdateメソッドのテスト開始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝//
	@Test
	public void DBのinsert文が正しい内容を追加するか確認() throws Exception{
		int testdata = 0;
		int num = 0;
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String insertmysql = "insert into clearcounttable(clearcount)values(1990)";
			String mysql = "select * from clearcounttable";
			num = db.executeUpdate(insertmysql);
			ResultSet rs = db.executeQuery(mysql);
			while(rs.next()){
			testdata = rs.getInt("clearcount");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(testdata, is(1990));
		assertThat(num, is(1));

	}

	@Test
	public void DBのupdate文が正しい内容を更新するか確認() throws Exception{
		int testdata = 0;
		int num = 0;
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String updatemysql = "update clearcounttable set clearcount = 1990";
			String mysql = "select * from clearcounttable";
			num = db.executeUpdate(updatemysql);
			ResultSet rs = db.executeQuery(mysql);
			while(rs.next()){
			testdata = rs.getInt("clearcount");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(testdata, is(1990));
		assertThat(num, is(1));

	}

	@Test
	public void DBのdelete文が正しい内容を削除するか確認() throws Exception{
		int testdata = 0;
		int num = 0;
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String deletemysql = "delete from clearcounttable";
			String mysql = "select * from clearcounttable";
			num = db.executeUpdate(deletemysql);
			ResultSet rs = db.executeQuery(mysql);
			while(rs.next()){
			testdata = rs.getInt("clearcount");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(testdata, is(0));
		assertThat(num, is(1));

	}

	@Test
	public void DBのsql文が間違っている場合エラーを吐くことを確認() throws Exception{
		int testdata = 0;
		int num = 0;
		try(WordBookDB db = new WordBookDB()){
			db.open();
			String deletemysql = "間違えた！";
			String mysql = "select * from clearcounttable";
			num = db.executeUpdate(deletemysql);
			ResultSet rs = db.executeQuery(mysql);
			while(rs.next()){
			testdata = rs.getInt("clearcount");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		assertThat(testdata, is(0));
		assertThat(num, is(0));

	}

//===============executeUpdateメソッドのテスト開始＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝//



}
