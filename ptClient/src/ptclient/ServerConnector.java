package ptclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class ServerConnector {

	private DefaultHttpClient httpClient;
	private String url;
	
	public ServerConnector() throws NoSuchAlgorithmException, KeyManagementException{
		/*
		 * For using SSL connections
		 * 
		X509TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {}
			
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}	
		};
		
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[]{tm}, null);
		SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme httpsScheme = new Scheme("https", 8443, sf);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);
		HttpParams params = new BasicHttpParams();
		ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
		httpClient = new DefaultHttpClient(cm, params);
		url = https://localhost:8443/ptserver/post
		*/
		url= "http://localhost/ptserver/post";
		httpClient = new DefaultHttpClient();
	}
	
	public HashMap<String, Object> sendData(String userName, String transactionId, long balanceChange) throws ClientProtocolException, IOException{	
		HttpPost post = new HttpPost(url);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userName", userName));
        nameValuePairs.add(new BasicNameValuePair("transactionId", transactionId));
        nameValuePairs.add(new BasicNameValuePair("balanceChange", String.valueOf(balanceChange)));
        
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Accept", "application/json");
		
		HttpResponse response = httpClient.execute(post);

		System.out.println(response.getStatusLine());

		HashMap<String, Object> resultMap = createMap(response);
		resultMap.put("userName", userName);
		return resultMap;
	}
	
	private HashMap<String, Object> createMap(HttpResponse response) throws UnsupportedEncodingException, IllegalStateException, IOException{
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser parser = jsonFactory.createJsonParser(in);
		if(parser.nextToken() != JsonToken.START_OBJECT){
			throw new IOException("Expected Json Object");
		}
		while(parser.nextToken() != JsonToken.END_OBJECT){
			String key = parser.getCurrentName();
			parser.nextToken();
			if(key.equals("transactionId")){
				resultMap.put(key, parser.getText());
			}else if(key.equals("balanceChange")){
				resultMap.put(key, parser.getLongValue());
			}else if(key.equals("newVersion")){
				resultMap.put(key, parser.getLongValue());
			}else if(key.equals("newBalance")){
				resultMap.put(key, parser.getLongValue());
			}
		}
		in.close();
		for(Map.Entry<String, Object> entry: resultMap.entrySet()){
			System.out.println(entry.getKey() + "  --   " + entry.getValue());
		}
		return resultMap;
	}
}
