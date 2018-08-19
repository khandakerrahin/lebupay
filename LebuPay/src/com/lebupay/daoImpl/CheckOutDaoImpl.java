/**
 * @formatter:off
 *
 */
package com.lebupay.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.lebupay.dao.CheckOutDAO;
import com.lebupay.model.CheckoutModel;
import com.lebupay.model.Status;

/**
 * This is checkoutDaoImpl extends BaseDao and Implements CheckOutDAO Interface to perform operations on Checkout.
 * @author Java Team
 *
 */
@Repository
public class CheckOutDaoImpl extends BaseDao implements CheckOutDAO {

	private static Logger logger = Logger.getLogger(CheckOutDaoImpl.class);
	
	/**
	 * This method is used for update the checkout w.r.t Merchant ID.
	 * @param checkoutModel
	 * @return int
	 * @throws Exception
	 */
	public int updateCheckout(CheckoutModel checkoutModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCheckout -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String query = "update CHECKOUT_MASTER set BACKGROUND_COLOUR =:BACKGROUND_COLOUR,BANNER_NAME=:BANNER_NAME,MODIFIED_DATE=localtimestamp(0),MODIFIED_BY=:MODIFIED_BY where MERCHANT_ID =:MERCHANT_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(query);
			pst.setStringAtName("BACKGROUND_COLOUR", checkoutModel.getBackgroundColour()); // BACKGROUND_COLOUR
			pst.setStringAtName("BANNER_NAME", checkoutModel.getBannerName()); // BANNER_NAME
			pst.setLongAtName("MODIFIED_BY", checkoutModel.getModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("MERCHANT_ID", checkoutModel.getModifiedBy()); // Merchant Id
			
			System.out.println("Update Checkout Master ==>> "+query);
			result = pst.executeUpdate();
			
			if (result > 0)
				result = 1; 
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

						if (logger.isDebugEnabled()) {
							logger.debug("Connection Closed for updateCheckout");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for updateCheckout"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCheckout -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used internally for fetching the Checkout Details as per Merchant during merchant login.
	 * @param merchantId
	 * @return CheckoutModel
	 * @throws Exception
	 */
	public CheckoutModel fetchCheckoutById(long merchantId) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCheckoutById -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		CheckoutModel checkoutModel = new CheckoutModel();
		
		try {
				String sql="select CHECKOUT_ID,BANNER_NAME,BACKGROUND_COLOUR,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY from CHECKOUT_MASTER where merchant_id =:merchant_id order by CREATED_DATE";
				
				System.out.println("Fetch Checkout :=> "+sql);
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setLongAtName("merchant_id",merchantId);
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					
					checkoutModel.setCheckoutId(rs.getLong(1));
					checkoutModel.setBannerName(rs.getString(2));
					checkoutModel.setBackgroundColour(rs.getString(3));
					
					if(rs.getInt(4) == 0)
						checkoutModel.setStatus(Status.INACTIVE);
					else if(rs.getInt(4) == 1)
						checkoutModel.setStatus(Status.ACTIVE);
					else
						checkoutModel.setStatus(Status.DELETE);
					
					checkoutModel.setCreatedDate(rs.getString(5));
					checkoutModel.setCreatedBy(rs.getLong(6));
					checkoutModel.setModifiedDate(rs.getString(7));
					checkoutModel.setModifiedBy(rs.getLong(8));
					
				}
		}	
		finally {
	          try{
	           
	           if(pst != null)
	            if(!pst.isClosed())
	            	pst.close();
	           
	          }catch(Exception e){
	                 e.printStackTrace();
	          }
	
	          try{
	
	           if(connection != null)
	            if(!connection.isClosed())
	             connection.close();
	
	          }catch(Exception e){
	        	  e.printStackTrace();
	          }      
       }
		
	   if (logger.isInfoEnabled()) {
			logger.info("fetchCheckoutById -- END");
	   }
		
	   return checkoutModel;
	}

}
