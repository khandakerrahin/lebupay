package com.lebupay.common;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This Class is used to Generate of Session ID..
 * @author Java-Team
 *
 */
public class SessionIdentifierGenerator {
	
	private SecureRandom random = new SecureRandom();

	/**
	 * This method is used to generate sessionID.
	 * @return String
	 */
	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}
}
