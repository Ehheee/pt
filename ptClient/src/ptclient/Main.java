package ptclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;

public class Main {

	private JFrame theFrame;
	private ActionPanel actionPanel;
	private JButton loginButton;
	
	private ServerConnector sc;
	private String userName;
	private HashMap<String, Object> dataMap;
	
	public static void main(String[] args){
		Main main = new Main();
	}
	
	public Main(){
		try {
			sc = new ServerConnector();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		userName = null;
		
		theFrame = new JFrame("ptClient");
		theFrame.setSize(300, 200);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		actionPanel = new ActionPanel(sc);
		
		
		userName = JOptionPane.showInputDialog("Enter username");
		
		if(userName == null){
			loginButton = new JButton("Enter username to use program");
			loginButton.addActionListener(new LoginButtonListener());

			theFrame.add(loginButton);
		}else{
			theFrame.add(actionPanel);
			actionPanel.setVisible(true);
			try {
				dataMap = sc.sendData(userName, null, 0);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			actionPanel.setDataMap(dataMap);
			actionPanel.refresh();
		}
		theFrame.setVisible(true);
		
	}
	
	
	
	private class LoginButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			userName = JOptionPane.showInputDialog("Enter username");
			if(userName != null){
				loginButton.setVisible(false);
				loginButton = null;
				theFrame.add(actionPanel);
				actionPanel.setVisible(true);
				try {
					dataMap = sc.sendData(userName, null, 0);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				actionPanel.setDataMap(dataMap);
				actionPanel.refresh();
			}			
		}
		
	}
}
