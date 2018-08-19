package com.lebupay.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lebupay.common.SendSMS;
import com.lebupay.service.DatabaseService;

@Controller
public class DatabaseController {

	private static Logger logger = Logger.getLogger(DatabaseController.class);

	@Autowired
	private DatabaseService databaseService;
	
	@Autowired
	private SendSMS sendSMS;
	
	@RequestMapping(value = "/database-operation", method = RequestMethod.GET)
	public String databaseOperationPage(Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("databaseOperationPage -- START");
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("databaseOperationPage -- END");
		}
		
		return "database-operation";
		
	}
	
	@RequestMapping(value = "/database-operation", method = RequestMethod.POST)
	public String databaseOperation(Model model, @RequestParam(value="query", required=false) String query) {

		if (logger.isInfoEnabled()) {
			logger.info("databaseOperation -- START");
		}
		
		System.out.println("Query is => "+query);
		model.addAttribute("query", query);
		String result = databaseService.databaseOperation(query);
		model.addAttribute("result", result);
		
		if (logger.isInfoEnabled()) {
			logger.info("databaseOperation -- END");
		}
		
		return "database-operation";
		
	}
	
	@RequestMapping(value = "/send-mail", method = RequestMethod.GET)
	public String sendMailPage(Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("sendMailPage -- START");
		}
		
		String result = "";
		model.addAttribute("result", result);
		
		if (logger.isInfoEnabled()) {
			logger.info("sendMailPage -- END");
		}
		
		return "send-mail";
		
	}
	
	@RequestMapping(value = "/send-mail", method = RequestMethod.POST)
	public String sendMail(Model model, @RequestParam(value="fromMail", required=false) String fromMail, @RequestParam(value="host", required=false) String host,
							@RequestParam(value="toMail", required=false) String toMail, @RequestParam(value="subject", required=false) String subject,
							@RequestParam(value="port", required=false) String port, @RequestParam(value="messageBody", required=false) String messageBody) {

		if (logger.isInfoEnabled()) {
			logger.info("sendMail -- START");
		}
		
		String result = "";
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", port);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port);
		
		try {

			SmtpMailAuthenticator smtpAuthenticator = new SmtpMailAuthenticator();
			javax.mail.Message message = new MimeMessage(Session.getDefaultInstance(props, smtpAuthenticator));
			//Message message = new MimeMessage(session);
			BodyPart messageBodyPart = new MimeBodyPart();
			
			String htmlText = "";
			String msgs = htmlText + messageBody;
			String sysmail= "<br/><br/><br/><br/><br/>This is a system generated mail from LebuPay. Please do not reply to this mail.";
			msgs+= sysmail;
			
			messageBodyPart.setContent(msgs, "text/html");
			
			// Create a related multi-part to combine the parts
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
						
			try {
				message.setFrom(new InternetAddress("lebupay@gmail.com", fromMail));
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);

			Transport.send(message);

			System.out.println("Mail Send Successfully");
			result = "Mail Send Successfully";
			
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			result = errors.toString();
		}
		
		model.addAttribute("result", result);
		
		if (logger.isInfoEnabled()) {
			logger.info("sendMail -- END");
		}
		
		return "send-mail";
		
	}
	
	public class SmtpMailAuthenticator extends Authenticator {
		
		public SmtpMailAuthenticator() {

		    super();
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			
			if (logger.isInfoEnabled()) {
				logger.info("getPasswordAuthentication -- START");
			}
			
			String username = "testmailvalid9999@gmail.com";
			String password = "Wb06m4079@";
			
		    if ((username != null) && (username.length() > 0) && (password != null) && (password.length() > 0)) {

		    	if (logger.isInfoEnabled()) {
					logger.info("getPasswordAuthentication -- END");
				}
		    	
		        return new PasswordAuthentication(username, password);
		    }

		    return null;
		}
	}
	
	@RequestMapping(value = "/send-sms", method = RequestMethod.GET)
	public String sendSMSPage(Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("sendSMSPage -- START");
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("sendSMSPage -- END");
		}
		
		return "send-sms";
		
	}
	
	@RequestMapping(value = "/send-sms", method = RequestMethod.POST)
	public String sendSMS(Model model, @RequestParam(value="number", required=false) String number, @RequestParam(value="message", required=false) String message) {

		if (logger.isInfoEnabled()) {
			logger.info("sendSMS -- START");
		}
		
		System.out.println("Number is => "+number);
		System.out.println("Message is => "+message);
		
		String result = sendSMS.smsSend(number, message);
		model.addAttribute("result", result.trim());
		
		if (logger.isInfoEnabled()) {
			logger.info("sendSMS -- END");
		}
		
		return "send-sms";
		
	}
	
}
