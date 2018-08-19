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

import com.lebupay.dao.QuickPayDAO;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.QuickPayModel;
import com.lebupay.model.Status;

/**
 * This is QuickPayDaoImpl extends BaseDao and Implements QuickPayDAO Interface used to perform operation on Quick Pay Operations.
 * @author Java Team
 *
 */
@Repository
public class QuickPayDaoImpl extends BaseDao implements QuickPayDAO{

	private static Logger logger = Logger.getLogger(QuickPayDaoImpl.class);
	
	/**
	 * This method is used for saving the QUICK PAY Details w.r.t Merchant ID.
	 * @param quickPayModel
	 * @return long
	 * @throws Exception
	 */
	public long saveQuickPay(QuickPayModel quickPayModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveQuickPay -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
				String sql = "insert into QUICKPAY_MASTER (QUICKPAY_ID,"
						+ "PRODUCT_SKU,"
						+ "PRODUCT_NAME,"
						+ "ORDER_AMOUNT,"
						+ "KEYS,"
						+ "MERCHANT_ID,"
						+ "STATUS,"
						+ "CREATED_DATE,"
						+ "CREATED_BY) values(QUICKPAY_MASTER_seq.nextval,:PRODUCT_SKU,:PRODUCT_NAME,:ORDER_AMOUNT,:KEYS,:MERCHANT_ID,:status,localtimestamp(0),:CREATED_BY) ";
			String pk[] = {"QUICKPAY_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql,pk);
			pst.setStringAtName("PRODUCT_SKU", quickPayModel.getProductSKU()); // Ticket Details
			pst.setStringAtName("PRODUCT_NAME", quickPayModel.getProductName()); // User Type
			pst.setStringAtName("ORDER_AMOUNT", quickPayModel.getOrderAmount()); // Ticket Details
			pst.setStringAtName("KEYS", quickPayModel.getKeys()); // User Type
			pst.setLongAtName("MERCHANT_ID", quickPayModel.getMerchantModel().getMerchantId()); // User Type
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
			pst.setLongAtName("CREATED_BY", quickPayModel.getCreatedBy()); // User ID
			
			System.out.println("Save QUICK PAY ==>> "+sql);
			
			boolean result1 = pst.execute();
			if(!result1) {
				
				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Quick Pay GENERATED KEY => "+result);
				connection.commit();
				
			}
			
		} finally {
	
			try { // Closing Connection Object
				if (connection != null) {
	
					if (!connection.isClosed())
						connection.close();
	
					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for saveQuickPay");
					}
				}
			} catch (Exception e) {
	
				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for saveQuickPay"
							+ e.getMessage());
				}
	
			}
	
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("saveQuickPay -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for fetching the QuickPay w.r.t Merchant and ID.
	 * @param quickPayId
	 * @param merchantId
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayById(long quickPayId, long merchantId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuickPayById -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		QuickPayModel quickPayModel = null;
		
		try {
				String sql = "select QUICKPAY_ID,PRODUCT_SKU,PRODUCT_NAME,ORDER_AMOUNT,KEYS,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,A_MODIFIED_DATE,A_MODIFIED_BY"
						+ "from QUICKPAY_MASTER where MERCHANT_ID =:MERCHANT_ID "
						+ "and STATUS !=:STATUS and QUICKPAY_ID=:QUICKPAY_ID";
				
				System.out.println("Fetch QuickPay By ID ==>> "+sql);
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setLongAtName("MERCHANT_ID", merchantId); // MERCHANT_ID
				pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
				pst.setLongAtName("QUICKPAY_ID", quickPayId); // QUICKPAY_ID
				ResultSet rs =  pst.executeQuery();
				if(rs.next()){
					
					quickPayModel = new QuickPayModel();
					quickPayModel.setQuickPayId(rs.getLong(1));
					quickPayModel.setProductSKU(rs.getString(2));
					quickPayModel.setProductName(rs.getString(3));
					quickPayModel.setOrderAmount(rs.getString(4));
					quickPayModel.setKeys(rs.getString(5));
					
					if(rs.getInt(6) == 0)
						quickPayModel.setStatus(Status.ACTIVE);
					else if(rs.getInt(6) == 1)
						quickPayModel.setStatus(Status.INACTIVE);
					else if(rs.getInt(6) == 2)
						quickPayModel.setStatus(Status.DELETE);
					else
						quickPayModel.setStatus(Status.ACTIVATED);
					
					quickPayModel.setCreatedDate(rs.getString(7));
					quickPayModel.setCreatedBy(rs.getLong(8));
					quickPayModel.setModifiedDate(rs.getString(9));
					quickPayModel.setModifiedBy(rs.getLong(10));
					quickPayModel.setaModifiedDate(rs.getString(11));
					quickPayModel.setaModifiedBy(rs.getLong(12));
				}
		} finally {
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
			logger.info("fetchQuickPayById -- END");
	   }
		
		return quickPayModel;
	}
	
	/**
	 * This method is used for fetching the Quick Pay w.r.t Unique Keys.
	 * @param keys
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayByKeys(String keys) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuickPayByKeys -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		QuickPayModel quickPayModel = null;
		
		try {
				String sql = "select QUICKPAY_ID,PRODUCT_SKU,PRODUCT_NAME,ORDER_AMOUNT,KEYS,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,A_MODIFIED_DATE,A_MODIFIED_BY,MERCHANT_ID"
						+ " from QUICKPAY_MASTER where STATUS !=:STATUS and KEYS=:KEYS";
				
				System.out.println("Fetch QuickPay By KEYS ==>> "+sql);
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
				pst.setStringAtName("KEYS", keys); // KEYS
				ResultSet rs =  pst.executeQuery();
				if(rs.next()){
					
					quickPayModel = new QuickPayModel();
					quickPayModel.setQuickPayId(rs.getLong(1));
					quickPayModel.setProductSKU(rs.getString(2));
					quickPayModel.setProductName(rs.getString(3));
					quickPayModel.setOrderAmount(rs.getString(4));
					quickPayModel.setKeys(rs.getString(5));
					
					if(rs.getInt(6) == 0)
						quickPayModel.setStatus(Status.ACTIVE);
					else if(rs.getInt(6) == 1)
						quickPayModel.setStatus(Status.INACTIVE);
					else if(rs.getInt(6) == 2)
						quickPayModel.setStatus(Status.DELETE);
					else
						quickPayModel.setStatus(Status.ACTIVATED);
					
					quickPayModel.setCreatedDate(rs.getString(7));
					quickPayModel.setCreatedBy(rs.getLong(8));
					quickPayModel.setModifiedDate(rs.getString(9));
					quickPayModel.setModifiedBy(rs.getLong(10));
					quickPayModel.setaModifiedDate(rs.getString(11));
					quickPayModel.setaModifiedBy(rs.getLong(12));
					MerchantModel merchantModel = new MerchantModel();
					merchantModel.setMerchantId(rs.getLong(13));
					quickPayModel.setMerchantModel(merchantModel);
				}
		} finally {
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
			logger.info("fetchQuickPayByKeys -- END");
	   }
		
		return quickPayModel;
	}

}