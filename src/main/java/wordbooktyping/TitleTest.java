package wordbooktyping;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import spark.Request;

public class TitleTest extends Title {



	@Test
	public void settingTest() throws Exception{

		Title title = new Title();
		HashMap model = new HashMap();

		//ここからのHashMap modelへの設定は現在のMySQLのデータで確認
		//(localhost user:admin password:slime825)
		model.put("japanese0", "空");
		model.put("japanese1", "水");
		model.put("japanese2", "りんご");
		model.put("japanese3", "化学");
		model.put("japanese4", "体");
		model.put("japanese5", "太陽");
		model.put("japanese6", "海");
		model.put("japanese7", "水素");
		model.put("japanese8", "酸素");
		model.put("japanese9", "炭素");


		model.put("japanese10", "窒素");
		model.put("japanese11", "アルミニウム");
		model.put("japanese12", "カリウム");
		model.put("japanese13", "水銀");
		model.put("japanese14", "あ");
		model.put("japanese15", "j");
		model.put("japanese16", "あいうえお");
		model.put("japanese17", "時計");
		model.put("japanese18", "道路");


		model.put("english0", "sky");
		model.put("english1", "water");
		model.put("english2", "apple");
		model.put("english3", "chemical");
		model.put("english4", "body");
		model.put("english5", "sun");
		model.put("english6", "ocean");
		model.put("english7", "H");
		model.put("english8", "O");
		model.put("english9", "C");

		model.put("english10", "N");
		model.put("english11", "Al");
		model.put("english12", "K");
		model.put("english13", "Hg");
		model.put("english14", "a");
		model.put("english15", "j");
		model.put("english16", "aiueo");
		model.put("english17", "clock");
		model.put("english18", "road");

		model.put("templatelayout", "templates/setting.vtl");
		model.put("listsize", 19);


		for(int i = 19; i<30; i++){
			model.put("japanese" + i, null);
			model.put("english" + i, null);
		}






		assertEquals(model,title.setting());

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
