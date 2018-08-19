package com.lebupay.common;

import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

/**
 * This Class is used to load i18n properties file.
 * @author Java-Team
 *
 */
@Component
public class MessageUtil {

	private static ResourceBundle resourceBundle = null;

	static {

		resourceBundle = ResourceBundle.getBundle("resources.i18n");
	}

	/**
	 * This method is used to get Messages from i18 file.
	 * @param key
	 * @return String
	 */
	public String getBundle(String key) {

		return resourceBundle.getString(key);
	}
}
