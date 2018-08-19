package com.lebupay.connection;

import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

/**
 * This class is used for Database Connection purpose.
 * @author Java-Team
 *
 */
@Component
public class DatabaseProperties {

	private static ResourceBundle resourceBundle = null;

	static {

		resourceBundle = ResourceBundle.getBundle("resources.database");
	}
	
	/**
	 * This method is used to get database properties from database.properties file.
	 * @param key
	 * @return String
	 */
	public String getBundle(String key) {

		return resourceBundle.getString(key);
	}
}
