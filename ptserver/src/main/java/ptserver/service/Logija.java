package ptserver.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component("logija")
public class Logija {
	
	private List<Long> queryTimes;
	private Long totalQueryTimes;
	Timer timer;
	private Log logger = LogFactory.getLog(getClass());
	
	public Logija(){
		queryTimes = new ArrayList<Long>();
		totalQueryTimes = (long) 0;
		timer = new Timer();
		TimedLogger timedLogger = new TimedLogger();
		timer.scheduleAtFixedRate(timedLogger, 10000, 60000);
	}
	
	public void addTime(Long time){
		queryTimes.add(time);
		totalQueryTimes += time;
		
	}
	
	private class TimedLogger extends TimerTask{
		public void run() {
			if(queryTimes.size() != 0){
				Long min = Collections.min(queryTimes);
				Long max = Collections.max(queryTimes);
				Long avg = totalQueryTimes / queryTimes.size();
				logger.info("Minimum for last minute: " + min); 
				logger.info("Maximum for last minute: " + max);
				logger.info("Average for last minute: " + avg);
				queryTimes = new ArrayList<Long>();
				totalQueryTimes = (long) 0;
			}else{
				logger.info("No requests last minute");
			}
		}
		
	}
}
