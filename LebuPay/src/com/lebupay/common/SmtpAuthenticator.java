package com.lebupay.common;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used for Smtp Authenticator.
 * @author Java-Team
 *
 */
@Component
public class SmtpAuthenticator extends Authenticator {
	
	@Autowired
	private MessageUtil messageUtil;
	
	public SmtpAuthenticator() {

	    super();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		
		String username = messageUtil.getBundle("email");
		String password = messageUtil.getBundle("email.password");
		
	    if ((username != null) && (username.length() > 0) && (password != null)  && (password.length   () > 0)) {

	        return new PasswordAuthentication(username, password);
	    }

	    return null;
	}
}