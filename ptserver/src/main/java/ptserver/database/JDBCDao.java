package ptserver.database;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


public class JDBCDao extends JdbcDaoSupport{
	
	private static final String PLAYER_INSERT = "INSERT INTO PLAYER(USERNAME, BALANCE, BALANCE_VERSION)VALUES (?,?,?)";
	private static final String MAX_VERSION = "SELECT * FROM PLAYER a RIGHT OUTER JOIN(SELECT USERNAME u, MAX(BALANCE_VERSION) maxBalance FROM PLAYER WHERE USERNAME = ? GROUP BY u) b ON a.BALANCE_VERSION = b.maxBalance AND a.USERNAME = b.u";
	private Log logger = LogFactory.getLog(getClass());
	
	public List<Player> getPlayers(){
		return getJdbcTemplate().query("SELECT USERNAME, BALANCE, BALANCE_VERSION FROM PLAYER", new PlayerMapper());
	}
	
	public void save(Player player){
		getJdbcTemplate().update(PLAYER_INSERT, new Object[]{player.getUserName(), player.getBalance(), player.getBalanceVersion()});
	}
	
	public Player getPlayersLatestVersion(String userName){
		List<Player> players = getJdbcTemplate().query(MAX_VERSION,  new PlayerMapper(), userName);
		try{
			return players.get(0);
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	
}
