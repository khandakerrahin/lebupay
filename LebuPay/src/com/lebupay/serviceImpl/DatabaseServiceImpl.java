package com.lebupay.serviceImpl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.dao.DatabaseDAO;
import com.lebupay.service.DatabaseService;


@Transactional
@Service
public class DatabaseServiceImpl implements DatabaseService {

	private static Logger logger = Logger.getLogger(DatabaseServiceImpl.class);

	@Autowired
	private DatabaseDAO databaseDao;
	
	public String databaseOperation(String query) {

		if (logger.isInfoEnabled()) {
			logger.info("databaseOperation -- START");
		}
		
		String result = null;
		
		//Finding the type of the query i.e "SELECT","INSERT","UPDATE","DELETE"
		if(query != null && !query.trim().equals("")) { // Checking whether the query is null or not
			
			query = query.trim();
			query = query.toUpperCase();
			String type = query.substring(0, query.indexOf(' ')); // Getting the first keyword of the query
			
			if(type != null) {
				
				if(type.equalsIgnoreCase("SELECT")) { // SELECT QUERY
					
					result =  this.fetchQuery(query);
					
				} else if(type.equalsIgnoreCase("INSERT")) { // INSERT QUERY
					
					result = this.insertQuery(query);
					
				} else if(type.equalsIgnoreCase("UPDATE")) { // UPDATE QUERY
					
					if(query.contains("WHERE"))
						result = this.updateQuery(query);
					else
						result = "Missing WHERE Condition";
					
				} else if(type.equalsIgnoreCase("DELETE")) { // DELETE QUERY
					
					if(query.contains("WHERE"))
						result = this.deleteQuery(query);
					else
						result = "Missing WHERE Condition";
					
				} else { // No Proper Keyword Found
					
					result = "Please Enter Proper Query";
				}
			}
			
		} else {
			
			result = "Please Enter A Query";
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("databaseOperation -- END");
		}
		
		return result;
	}
	
	private int checkTableName(String tableName) {
		
		if (logger.isInfoEnabled()) {
			logger.info("checkTableName -- START");
		}
		
		int result = databaseDao.checkTableName(tableName);
		
		if (logger.isInfoEnabled()) {
			logger.info("checkTableName -- END");
		}
		
		return result;
	}
	
	private String fetchQuery(String query) {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuery -- START");
		}
		
		String result = databaseDao.fetchQuery(query);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuery -- END");
		}
		
		return result;
	}
	
	private String insertQuery(String query) {
			
		if (logger.isInfoEnabled()) {
			logger.info("insertQuery -- START");
		}
		
		String result = null;
		String tableName = query.substring(query.indexOf("INTO") + 4, query.indexOf("(")).trim();
		int tablePresent = checkTableName(tableName);
		if(tablePresent != 0)
			result = databaseDao.insertQuery(query);
		else
			result = "Table Not Found";
		
		if (logger.isInfoEnabled()) {
			logger.info("insertQuery -- END");
		}
		
		return result;
	}
	
	private String updateQuery(String query) {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateQuery -- START");
		}
		
		String result = null;
		String tableName = query.substring(query.indexOf("UPDATE") + 6, query.indexOf("SET")).trim();
		int tablePresent = checkTableName(tableName);
		if(tablePresent != 0)
			result = databaseDao.updateQuery(query);
		else
			result = "Table Not Found";
		
		if (logger.isInfoEnabled()) {
			logger.info("updateQuery -- END");
		}
		
		return result;
	}
	
	private String deleteQuery(String query) {
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteQuery -- START");
		}
		
		String result = null;
		String tableName = query.substring(query.indexOf("FROM") + 4, query.indexOf("WHERE")).trim();
		int tablePresent = checkTableName(tableName);
		if(tablePresent != 0)
			result = databaseDao.deleteQuery(query);
		else
			result = "Table Not Found";
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteQuery -- END");
		}
		
		return result;
	}
}
