package wordbooktyping;

//使ってはないクラス
import java.sql.ResultSet;
import java.util.ArrayList;

public class WordList {

	ArrayList<String> japanesewordlist = new ArrayList<String>();
	ArrayList<String> englishwordlist = new ArrayList<String>();
	String[] japanesecodearray = new String[30];
	String[] englishcodearray = new String[30];
	int listsize = 0;

	public WordList wordset(){
		WordList wordlist = new WordList();
		WordBookDB db = new WordBookDB();

		try{
			db.open();
			String mysql = "select * from english order by id";

			ResultSet rs = db.executeQuery(mysql);
			try{
				while(rs.next()){
					String japanese = rs.getString("japanese");
					String english = rs.getString("english");
					wordlist.japanesewordlist.add(japanese);
					wordlist.englishwordlist.add(english);
				}
				listsize = japanesewordlist.size();
				//可能ならlistsizeを配列の上限数にして動的に配列を変化させたい
				/*
				 * String japanesecode = "japanese" + number;
				 * String englishcode = "english" + number;
				 */
				for(int i = 0; i<30; i++){
					wordlist.japanesecodearray[i] = "japanese" + i;
					wordlist.englishcodearray[i] = "english"+ i;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.close();
		}
		return wordlist;
	}


}
