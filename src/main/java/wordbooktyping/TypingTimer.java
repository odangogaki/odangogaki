package wordbooktyping;

//使ってないクラス、記念においてある

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import spark.template.velocity.VelocityTemplateEngine;




public class TypingTimer {

	Timer  timer = new Timer();
	int maxtime;
	int progressedtime;
	int remainingtime;





	public void TimerCheck(){


		timer.schedule(new TimerCheckTask(), 1000, 1000);







	}

	public void TimerStop(){
		timer.cancel();
	}

}


class TimerCheckTask extends TimerTask{

	int maxtime = 10;
	int progressedtime;
	int remainingtime;






	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		time++;
		System.out.println(time +"秒");

		progressedtime  = time;
		remainingtime = maxtime - progressedtime;
		System.out.println(remainingtime);

		if(remainingtime == 0){
			TypingTimer typingtimer = new TypingTimer();
			typingtimer.TimerStop();
				get("/result", (request,responce) ->{
					HashMap model = new HashMap();
					model.put("templatelayout", "templates/result.vtl");
					return modelAndView(model, "templates/templatelayout.vtl");

				},new VelocityTemplateEngine());
		}




	}
	//time++より後ろで宣言しても動くの！？！？
	int time ;


}
