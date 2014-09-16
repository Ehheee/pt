package ptserver.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PlayerMapper implements RowMapper<Player>{

	public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
		Player p = new Player();
		p.setUserName(rs.getString("USERNAME"));
		p.setBalance(rs.getLong("BALANCE"));
		p.setBalanceVersion(rs.getLong("BALANCE_VERSION"));
		return p;
	}

}
