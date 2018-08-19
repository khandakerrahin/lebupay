package com.lebupay.dao;

public interface DatabaseDAO {

	public int checkTableName(String tableName) ;
	
	public String fetchQuery(String query) ;
	
	public String insertQuery(String query) ;
	
	public String updateQuery(String query) ;
	
	public String deleteQuery(String query) ;
}
