package ptserver.controllrer;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ptserver.database.JDBCDao;
import ptserver.database.Player;
import ptserver.service.Logija;

@Controller
@RequestMapping("/")
public class MainController {

	private Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "post", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> postData(@RequestParam(value = "userName", required = false) String userName,
							@RequestParam(value = "transactionId", required = false) String transactionId,
							@RequestParam(value = "balanceChange", required = false) Long balanceChange){
		Long startTime = System.currentTimeMillis();
		Player p = jdbcDao.getPlayersLatestVersion(userName);
		
		long newBalance;
		long newVersion;
		if(p == null){
			p = new Player();
			p.setUserName(userName);
			p.setBalance(balanceChange);
			p.setBalanceVersion(1);
			jdbcDao.save(p);
			newBalance = balanceChange;
			newVersion = 1;
		}else{
			newBalance = p.getBalance() + balanceChange;
			p.setBalance(newBalance);
			newVersion = p.getBalanceVersion() + 1;
			p.setBalanceVersion(newVersion);
			jdbcDao.save(p);
		}
	
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("transactionId", transactionId);
		returnMap.put("newBalance", newBalance);
		returnMap.put("newVersion", newVersion);
		returnMap.put("balanceChange", balanceChange);
		Long endTime = System.currentTimeMillis();
		Long queryTime = endTime - startTime;
		logija.addTime(queryTime);
		String inf = "**PLAYER: " + userName + " *BalanceChange " +balanceChange + " *TransacionId " + transactionId +  "**";
		logger.info(inf);
		return returnMap;
	}
	@Autowired
	private Logija logija;

	@Autowired
	private JDBCDao jdbcDao;
}
