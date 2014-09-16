package ptclient;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.http.client.ClientProtocolException;

public class ActionPanel extends JPanel {

	private ServerConnector sc;
	private JTextField balanceInput;
	private JLabel errorText;
	private JLabel totalBalance;
	private JLabel balanceVersion;
	private JButton submitButton;
	
	
	public ActionPanel(ServerConnector sc){
		super();
		this.sc = sc;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		errorText = new JLabel();
		totalBalance = new JLabel();
		balanceVersion = new JLabel();
		submitButton = new JButton("submit");
		submitButton.addActionListener(new SubmitButtonActionListener());
		
		balanceInput = new JTextField(13);
		balanceInput.addKeyListener(new BalanceInputKeyListener());
		balanceInput.setMaximumSize(new Dimension(100, 20));
		
		this.add(balanceInput);
		this.add(errorText);
		this.add(submitButton);
		this.add(totalBalance);
		this.add(balanceVersion);
		
	}
	
	public void refresh(){
		String totalBalanceText = "Current total balance: " + String.valueOf(dataMap.get("newBalance"));
		totalBalance.setText(totalBalanceText);
		String balanceVersionText = "Current balance version: " + String.valueOf(dataMap.get("newVersion"));
		balanceVersion.setText(balanceVersionText);
	}
	
	private HashMap<String, Object> dataMap;
	public HashMap<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(HashMap<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	private class SubmitButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			submitButton.setEnabled(false);
			String transactionId = dataMap.get("userName") + String.valueOf(System.currentTimeMillis());
			String userName = (String) dataMap.get("userName");
			Long balanceChange = null;
			try{
				balanceChange = Long.valueOf(balanceInput.getText());
			}catch(Exception ex){
				errorText.setText("Input was not numeric!");
				return;
			}
			
			try {
				HashMap<String, Object> resultMap = sc.sendData(userName, transactionId, balanceChange);
				//Check if results from server are OK
				if(resultMap != null && resultMap.get("transactionId").equals(transactionId)){
					dataMap = resultMap;
					submitButton.setEnabled(true);
					refresh();
				}else{
					errorText.setText("There was some weird problem");
				}
			} catch (ClientProtocolException e1) {
				errorText.setText("Fatal error, the program will not work!");
				e1.printStackTrace();
			} catch (IOException e1) {
				errorText.setText("There was a problem with network connection. You could try again");
				e1.printStackTrace();
			}
		}
		
	}
	
	private class BalanceInputKeyListener implements KeyListener{

		public void keyTyped(KeyEvent e) {
			boolean flag = false;
			String text = ((JTextField)e.getComponent()).getText();
			for(char c: text.toCharArray()){
				if(!Character.isDigit(c)){
					flag = true;
					if(c == '-'){
						flag = false;
					}
					break;
				}
			}
			if(flag){
				errorText.setText("Only numeric input allowed!");
				submitButton.setEnabled(false);
			}else{
				errorText.setText("");
				submitButton.setEnabled(true);
			}
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
		
	}
}
