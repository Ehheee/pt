package ptserver.database;


public class Player {

	private String userName;
	private long balanceVersion;
	private long balance;
	
	public Player(){
		
	}
	
	public Player(String userName, long balanceVersion, long balance) {
		this.userName = userName;
		this.balanceVersion = balanceVersion;
		this.balance = balance;
	}



	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public long getBalanceVersion() {
		return balanceVersion;
	}
	public void setBalanceVersion(long balanceVersion) {
		this.balanceVersion = balanceVersion;
	}
	
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
}
