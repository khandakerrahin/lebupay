/**
 * @formatter:off
 *
 */
package com.lebupay.common;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lebupay.exception.MailSendException;

/**
 * This Class is used to send mail.
 * @author Java-Team
 *
 */
@Component
public class SendMail {
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private SmtpAuthenticator smtpAuthenticator;
	
	/**
	 * This method is used to send mail. This is a common method used multiple times.
	 * @param emailId
	 * @param messageBody
	 * @param subject
	 * @throws MailSendException
	 */
	@Async
	public void send(String emailId, String messageBody, String subject) throws MailSendException {

		String fromEmail = messageUtil.getBundle("fromEmail");//"mail@example.com";//
		String port = messageUtil.getBundle("port");
		
		Properties props = new Properties();
		// props.put("mail.smtp.host", "smtp.gmail.com");
		/*
		props.put("mail.smtp.host", "smtp.zoho.com");
		props.put("mail.smtp.socketFactory.port", port);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port);/**/
		
     /*
        
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        
        */
        
        // new properties added on 22/07/2018 by Shaker
        props.put("mail.smtp.host", "smtp.zoho.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");

		try {

			javax.mail.Message message = new MimeMessage(Session.getDefaultInstance(props, smtpAuthenticator));
			//Message message = new MimeMessage(session);
			BodyPart messageBodyPart = new MimeBodyPart();
			
			String htmlText = "";
			String msgs = htmlText + messageBody;
			String sysmail= "<br/><br/><br/><br/><br/>This is a system generated mail from LebuPay.Please do not reply to this mail.";
			msgs+= sysmail;
			
			messageBodyPart.setContent(msgs, "text/html");
			
			// Create a related multi-part to combine the parts
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
						
			try {
				message.setFrom(new InternetAddress(fromEmail, "Lebupay"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(emailId));
			message.setSubject(subject);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new MailSendException(e.getMessage());
		}
	}
	
}
