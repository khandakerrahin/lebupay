package com.lebupay.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lebupay.exception.DBConnectionException;

import databasehub.oracle.connect.OracleConnect;

/**
 * This Class will supply Database Connection Properties for connection.
 * @author Java-Team
 *
 */
@Component
public class OracleConnection {

	@Autowired
	private DatabaseProperties databaseProperties;

	private java.sql.Connection con = null;

	/**
	 * This method is used to connect database.
	 * @return Connection object of Connection class 
	 * @throws Exception
	 */
	public java.sql.Connection Connect() throws Exception {

		String url = databaseProperties.getBundle("database.url");
		String userName = databaseProperties.getBundle("database.user");
		String password = databaseProperties.getBundle("database.password");

		OracleConnect oracleConnect = new OracleConnect(url, userName, password);

		try {
				con = oracleConnect.getConnection();
		} catch(Exception e) {
			throw new DBConnectionException(e.getMessage());
		}

		con.setAutoCommit(false);

		return con;

	}
}
