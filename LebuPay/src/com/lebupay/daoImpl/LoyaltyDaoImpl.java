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

import com.lebupay.dao.LoyaltyDAO;
import com.lebupay.model.LoyaltyModel;
import com.lebupay.model.Status;

/**
 * This is LoyaltyDaoImpl extends BaseDao and Implements LoyaltyDao Interface used to perform operation on Loyalty Module.
 * @author Java Team
 *
 */
@Repository
public class LoyaltyDaoImpl extends BaseDao implements LoyaltyDAO{

	private static Logger logger = Logger.getLogger(LoyaltyDaoImpl.class);
	
	/**
	 * This method is used for inserting Loyalty.
	 * @param loyaltyModel
	 * @return long
	 * @throws Exception
	 */
	public long insertLoyalty(LoyaltyModel loyaltyModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertLoyalty -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "insert into LOYALTY_MASTER (LOYALTY_ID,LOYALTY_TYPE,AMOUNT,POINT,STATUS,CREATED_BY,CREATED_DATE) values(LOYALTY_MASTER_SEQ.nextval,"
					+ ":LOYALTY_TYPE,"
					+ ":AMOUNT,"
					+ ":POINT,"
					+ ":status,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0)) ";
			
			String pk[] = {"LOYALTY_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setStringAtName("LOYALTY_TYPE", loyaltyModel.getLoyaltyType()); // LOYALTY_TYPE
			pst.setDoubleAtName("AMOUNT", loyaltyModel.getAmount()); // AMOUNT
			pst.setDoubleAtName("POINT", loyaltyModel.getPoint()); // POINT
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
			pst.setLongAtName("CREATED_BY", loyaltyModel.getCreatedBy()); // CREATED_BY
			
			System.out.println("Insert Loyalty Master=> "+sql);
			
			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Loyalty GENERATED KEY => " + result);
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

						if (logger.isDebugEnabled()) {
							logger.debug("Connection Closed for insertLoyalty");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for insertLoyalty"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("insertLoyalty -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for updating Loyalty.
	 * @param loyaltyModel
	 * @return int
	 * @throws Exception
	 */
	public int updateLoyalty(LoyaltyModel loyaltyModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateLoyalty -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "update LOYALTY_MASTER set LOYALTY_TYPE =:LOYALTY_TYPE,"
					+ "AMOUNT=:AMOUNT,"
					+ "POINT=:POINT,"
					+ "STATUS=:STATUS,"
					+ "MODIFIED_DATE=localtimestamp(0),"
					+ "MODIFIED_BY=:MODIFIED_BY "
					+ "where LOYALTY_ID =:LOYALTY_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("LOYALTY_TYPE", loyaltyModel.getLoyaltyType()); // LOYALTY_TYPE
			pst.setDoubleAtName("AMOUNT", loyaltyModel.getAmount()); // AMOUNT
			pst.setDoubleAtName("POINT", loyaltyModel.getPoint()); // POINT
			pst.setIntAtName("STATUS", loyaltyModel.getStatus().ordinal()); // Status
			pst.setLongAtName("MODIFIED_BY", loyaltyModel.getaModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("LOYALTY_ID", loyaltyModel.getLoyaltyId()); // LOYALTY_ID
			
			System.out.println("Update Parameter Master=> "+sql);
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
							logger.debug("Connection Closed for updateLoyalty");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for updateLoyalty"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateLoyalty -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for fetching active Loyalty By LoyalityType.
	 * @param loyalityType
	 * @return List<LoyaltyModel>
	 * @throws Exception
	 */
	public LoyaltyModel fetchActiveLoyaltyByType(String loyalityType)  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveLoyaltyByType -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		LoyaltyModel loyaltyModel = null;
		
		try {
			String sql = "select LOYALTY_ID,LOYALTY_TYPE,AMOUNT,POINT,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY from LOYALTY_MASTER where STATUS!=:STATUS and LOYALTY_TYPE=:LOYALTY_TYPE order by CREATED_BY desc";
			
			System.out.println("Fetch Active LoyaltyByType :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			pst.setStringAtName("LOYALTY_TYPE", loyalityType); // LOYALTY_TYPE
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				loyaltyModel = new LoyaltyModel();
				loyaltyModel.setLoyaltyId(rs.getLong(1));
				loyaltyModel.setLoyaltyType(rs.getString(2));
				loyaltyModel.setAmount(rs.getDouble(3));
				loyaltyModel.setPoint(rs.getDouble(4));
				
				if(rs.getInt(5) == 0)
					loyaltyModel.setStatus(Status.INACTIVE);
				else if(rs.getInt(5) == 1)
					loyaltyModel.setStatus(Status.ACTIVE);
				else
					loyaltyModel.setStatus(Status.DELETE);
				
				loyaltyModel.setCreatedDate(rs.getString(6));
				loyaltyModel.setCreatedBy(rs.getLong(7));
				loyaltyModel.setModifiedDate(rs.getString(8));
				loyaltyModel.setaModifiedBy(rs.getLong(9));
				
			}
			
			
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
						logger.debug("Connection Closed for fetchActiveLoyaltyByType");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchActiveLoyaltyByType"+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveLoyaltyByType -- END");
		}
		
		return loyaltyModel;
	}
	
	/**
	 * This method is used for fetching Loyalty By Id.
	 * @param loyaltyId
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel fetchActiveLoyaltyByID(long loyaltyId)  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveLoyaltyByID -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		LoyaltyModel loyaltyModel = new LoyaltyModel();
		
		try {
			String sql = "select LOYALTY_ID,LOYALTY_TYPE,AMOUNT,POINT,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY from LOYALTY_MASTER where STATUS=:STATUS and LOYALTY_ID=:LOYALTY_ID order by CREATED_BY desc";
			
			System.out.println("Fetch Loyaltys :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.ACTIVE.ordinal()); // Status
			pst.setLongAtName("LOYALTY_ID", loyaltyId); // LOYALTY_ID
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				loyaltyModel.setLoyaltyId(rs.getLong(1));
				loyaltyModel.setLoyaltyType(rs.getString(2));
				loyaltyModel.setAmount(rs.getDouble(3));
				loyaltyModel.setPoint(rs.getDouble(4));
				
				if(rs.getInt(5) == 0)
					loyaltyModel.setStatus(Status.INACTIVE);
				else if(rs.getInt(5) == 1)
					loyaltyModel.setStatus(Status.ACTIVE);
				else
					loyaltyModel.setStatus(Status.DELETE);
				
				loyaltyModel.setCreatedDate(rs.getString(6));
				loyaltyModel.setCreatedBy(rs.getLong(7));
				loyaltyModel.setModifiedDate(rs.getString(8));
				loyaltyModel.setaModifiedBy(rs.getLong(9));
				
			}
			
			
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
						logger.debug("Connection Closed for fetchActiveLoyaltyByID");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchActiveLoyaltyByID"+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveLoyaltyByID -- END");
		}
		
		return loyaltyModel;
	}

}
