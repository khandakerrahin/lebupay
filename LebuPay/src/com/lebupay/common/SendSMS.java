package com.lebupay.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This Class is used to Send SMS.
 * @author Java-Team
 *
 */
@Component
public class SendSMS {

	private static Logger logger = Logger.getLogger(SendSMS.class);
	
	@Autowired
	private MessageUtil messageUtil;

	/**
	 * This is a common method for sending SMS taking input as mobileNumer, smsMessage.
	 * @param mobileNumber
	 * @param smsMessage
	 * @return String
	 */
	public String smsSend(String mobileNumber, String smsMessage) {
		
		if (logger.isInfoEnabled()) {
			logger.info("smsSend -- START");
		}
		
		String result = "";
		   
		try {
			
			String userName = messageUtil.getBundle("userName");
			String password = messageUtil.getBundle("password");
			//String originator = messageUtil.getBundle("originator");
			
			// params for Authentications
		    String action = "Authenticate";
		    
			
			String url="https://bubble.fyi/sms/api?test=Y";
		    String errorCode = "-1";
		    
		    JSONObject cred = new JSONObject();
			cred.put("action",action);
		    cred.put("username", userName);
		    cred.put("password", password);
		    
		    // params for SendSMS
		    action = "SendSMS";
			String tokenId = "";
			String recipient = mobileNumber;
			String message = smsMessage;
			String id = "1";
			String expiry = "2";
			String sender = "88019";
			
		    URL object;
		    HttpURLConnection con = null;
		    try {
				object = new URL(url);
			
				con = (HttpURLConnection) object.openConnection();
				con.setDoOutput(true);
			    con.setDoInput(true);
			    con.setRequestProperty("Content-Type", "application/json");
			    con.setRequestProperty("Accept", "application/json");
			    con.setRequestMethod("POST");
				OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
				wr.write(cred.toString());
				wr.flush();
				StringBuilder sb = new StringBuilder();  
			    int HttpResult;
				HttpResult = con.getResponseCode();
				if (HttpResult == HttpURLConnection.HTTP_OK) {
			        BufferedReader br = new BufferedReader(
			                new InputStreamReader(con.getInputStream(), "utf-8"));
			        String line = null;  
			        while ((line = br.readLine()) != null) {  
			            sb.append(line + "\n");  
			        }
			        br.close();
			        System.out.println("raw JSON : "+sb.toString());
			        
			        errorCode = Util.getJsonObject(sb.toString()).getString("errorCode");

			        if(errorCode.equals("0")) 
			        {
				        tokenId = Util.getJsonObject(sb.toString()).getString("token");
				        System.out.println("Token : "+tokenId);
				        JSONObject smsInfo = new JSONObject();
						smsInfo.put("action",action);
						smsInfo.put("tokenId", tokenId);
						smsInfo.put("recipient", recipient);
						smsInfo.put("message",message);
						smsInfo.put("id", id);
						smsInfo.put("expiry", expiry);
						smsInfo.put("sender", sender);
						
						con = (HttpURLConnection) object.openConnection();
						con.setDoOutput(true);
					    con.setDoInput(true);
					    con.setRequestProperty("Content-Type", "application/json");
					    con.setRequestProperty("Accept", "application/json");
					    con.setRequestMethod("POST");
						wr = new OutputStreamWriter(con.getOutputStream());
						
						wr.write(smsInfo.toString());
						wr.flush();
						
						sb.setLength(0);
						
						HttpResult = con.getResponseCode();
						if (HttpResult == HttpURLConnection.HTTP_OK) {
					        br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
					        line = null;  
					        while ((line = br.readLine()) != null) {  
					            sb.append(line + "\n");  
					        }
					        br.close();
					        System.out.println("raw JSON : "+sb.toString());
					    } else {
					        System.out.println(con.getResponseMessage());  
					    }
					}
					
			    } else {
			        System.out.println(con.getResponseMessage());  
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
		}catch (Exception e) {
					e.printStackTrace();
				}
						
		if (logger.isInfoEnabled()) {
			logger.info("smsSend -- END");
		}
		
		
        return result;
	}
}