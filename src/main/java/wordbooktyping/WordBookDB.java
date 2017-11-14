package wordbooktyping;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WordBookDB implements AutoCloseable{

	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;



	//データベースに接続
	public void open(){
		String url = "jdbc:mysql://localhost/wordbook";
		String user = "admin";
		String pass = "slime825";
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}

		try{
			//DBに接続
			con =DriverManager.getConnection(url, user, pass);
			//DB処理のための準備、Statementオブジェクトの作成
			stmt = con.createStatement();

			}catch(SQLException e){
				e.printStackTrace();
		}
	}




	//オブジェクトを解放
	public void close(){
		if(rs !=null){
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}

		if(stmt !=null){
			try{
				stmt.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}

		if(con !=null){
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}


	//select文を実行
	public ResultSet executeQuery(String sql){
		if(stmt != null){
			try{
				//stmt.executeQueryでsql文（select文）の内容を実行して、rsに格納
				rs = stmt.executeQuery(sql);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}

		//rsの結果を返す
		return rs;
	}




	//insert,update,delete文を実行
	//BadSQLEXceptionが発生したときに、このメソッド内で例外処理を行わず、throws BadSQLexceptionで呼び出し元に処理をゆだねている
	public int executeUpdate(String sql)throws BadSQLException{
		int num = 0;
		if(stmt != null){
			try{
				//オートコミットを無効にしてトランザクションんを開始
				con.setAutoCommit(false);
				//executeUpdateでsql文（insert,updagte,delete文）の内容を実行し、numに格納
				num  = stmt.executeUpdate(sql);
				//SQLで何も問題なければConnection.commiti(); commitはtry～catch文で囲む必要あり
				con.commit();
			}catch(SQLException e){
				//con.rollback()自体が例外を発生する可能性があるので、catch文の中でさらにtry～catch文で囲む
				try{
					//SQL文が適切でない場合ここにくる、rollback(取り消し）して、エラーの詳細を出力、例外をBadSQLExceptionへスロー
					con.rollback();
					e.printStackTrace();
					//呼び出し元に例外処理を投げる（呼び出し元によって例外への対処方法が違う）
					throw new BadSQLException();
				}catch(SQLException e1){
					e1.printStackTrace();
				}
			}
		}


		//executeQuery()メソッドでSQL文を実行し、追加、更新、削除されたレコード数を返す
		return num;
	}


}


//Exceptionを継承した独自の例外クラス、例外をスローするために使う
class BadSQLException extends Exception{

}
