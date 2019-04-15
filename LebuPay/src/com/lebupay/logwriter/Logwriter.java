package com.lebupay.logwriter;

import java.sql.Connection;
import java.sql.ResultSet;


import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;

import com.lebupay.connection.OracleConnection;

public class Logwriter {
	//TODO will it be protected like dao
	
	
	/**
	 * 
	 * @param merchant_id
	 * @param action
	 * @param action_type
	 * @param log
	 * @throws Exception
	 */
	public void writeLog(Long merchant_id,String action,int action_type,String log) throws Exception {
		 OracleConnection oracleConnection = new OracleConnection();
		 
	
			System.out.println("logwriter.writeLog -- START");
			System.out.println("logwriter.writeLog -log ===>"+log);
			if(log.length()>999)
				log=log.substring(0,999);
	


		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into APPLICATION_LOG (merchant_id,action,action_type,log) values("
					+ ":merchant_id,:action,:action_type,:log)";

			String pk[] = {"ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);

			pst.setLongAtName("merchant_id", merchant_id); 
			pst.setStringAtName("action", action); 
			pst.setIntAtName("action_type", action_type); 
			pst.setStringAtName("log", log); 
			

			System.out.println("logwriter.writeLog==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
			}
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();
				}
			} catch (Exception e) {
				System.out.println("Connection not closed for logwriter.writeLog ");
				/*
				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				}/**/

			}
		}
/*
		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- END");
		}/**/


	}

}
