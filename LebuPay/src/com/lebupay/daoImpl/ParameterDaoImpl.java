/**
 * @formatter:off
 *
 */
package com.lebupay.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.lebupay.dao.ParameterDAO;
import com.lebupay.model.ParameterModel;
import com.lebupay.model.Status;

/**
 * This is ParameterDaoImpl extends BaseDao and Implements ParameterDao Interface used to perform operation on Parameters.
 * @author Java Team
 *
 */
@Repository
public class ParameterDaoImpl extends BaseDao implements ParameterDAO {

	private static Logger logger = Logger.getLogger(ParameterDaoImpl.class);
	
	/**
	 * This method is used to insert the parameters w.r.t Merchant Id.
	 * @param parameterModel
	 * @return long
	 * @throws Exception
	 */
	public long insertParameter(ParameterModel parameterModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertParameter -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "insert into PARAMETER_MASTER (PARAMETER_ID,PARAMETER_NAME,PARAMETER_TYPE,VISIBLE,PERSISTENT,MANDATORY,MERCHANT_ID,IS_DELETABLE,STATUS,CREATED_BY,CREATED_DATE) values(PARAMETER_MASTER_SEQ.nextval,"
					+ ":PARAMETER_NAME,"
					+ ":PARAMETER_TYPE,"
					+ ":VISIBLE,"
					+ ":PERSISTENT,"
					+ ":MANDATORY,"
					+ ":MERCHANT_ID,"
					+ ":IS_DELETABLE,"
					+ ":status,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0)) ";
			
			String pk[] = {"PARAMETER_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setStringAtName("PARAMETER_NAME", parameterModel.getParameterName()); // PARAMETER_NAME
			pst.setStringAtName("PARAMETER_TYPE", parameterModel.getParameterType()); // PARAMETER_TYPE
			pst.setStringAtName("VISIBLE", parameterModel.getVisible()); // VISIBLE
			pst.setStringAtName("PERSISTENT", parameterModel.getPersistent()); // PERSISTENT
			pst.setStringAtName("MANDATORY", parameterModel.getMandatory()); // MANDATORY
			pst.setLongAtName("MERCHANT_ID", parameterModel.getCreatedBy()); // Merchant Id
			pst.setIntAtName("IS_DELETABLE", Status.INACTIVE.ordinal()); // IS_DELETABLE
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
			pst.setLongAtName("CREATED_BY", parameterModel.getCreatedBy()); // CREATED_BY
			
			System.out.println("Insert Parameter Master=> "+sql);
			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Banner GENERATED KEY => " + result);
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
							logger.debug("Connection Closed for insertParameter");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for insertParameter"+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("insertParameter -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for deleting the parameter for Merchant.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int deleteParameter(ParameterModel parameterModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteParameter -- START");
		}
		
		int result = 0;
		
		Connection connection1 = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql1 = "delete from PARAMETER_MASTER where PARAMETER_ID =:PARAMETER_ID";
			pst = (OraclePreparedStatement) connection1.prepareStatement(sql1);
			pst.setLongAtName("PARAMETER_ID", parameterModel.getParameterId()); // PARAMETER_ID
			
			System.out.println("Update Parameter Master=> "+sql1);
			result = pst.executeUpdate();
			
			if (result > 0)
				result = 1; 
			connection1.commit();
			
		} finally {
			
				try{
			           
		           if(pst != null)
		            if(!pst.isClosed())
		            	pst.close();
			           
			     } catch(Exception e){
			           e.printStackTrace();
			      }

				try { // Closing Connection Object
					if (connection1 != null) {

						if (!connection1.isClosed())
							connection1.close();

						if (logger.isDebugEnabled()) {
							logger.debug("Connection Closed for deleteParameter");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for deleteParameter"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteParameter -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to update the parameters for the Merchant.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int updateParameter(ParameterModel parameterModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateParameter -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "update PARAMETER_MASTER set PARAMETER_NAME =:PARAMETER_NAME,PARAMETER_TYPE=:PARAMETER_TYPE,VISIBLE=:VISIBLE,PERSISTENT=:PERSISTENT,MANDATORY=:MANDATORY,MODIFIED_DATE=localtimestamp(0),MODIFIED_BY=:MODIFIED_BY where PARAMETER_ID =:PARAMETER_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("PARAMETER_NAME", parameterModel.getParameterName()); // PARAMETER_NAME
			pst.setStringAtName("PARAMETER_TYPE", parameterModel.getParameterType()); // PARAMETER_TYPE
			pst.setStringAtName("VISIBLE", parameterModel.getVisible()); // VISIBLE
			pst.setStringAtName("PERSISTENT", parameterModel.getPersistent()); // PERSISTENT
			pst.setStringAtName("MANDATORY", parameterModel.getMandatory()); // MANDATORY
			pst.setLongAtName("MODIFIED_BY", parameterModel.getModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("PARAMETER_ID", parameterModel.getParameterId()); // PARAMETER_ID
			
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
							logger.debug("Connection Closed for updateParameter");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for updateParameter"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateParameter -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used internally for fetching the Parameter List as per Merchant during merchant login.
	 * @param merchantId
	 * @return List<ParameterModel>
	 * @throws Exception
	 */
	public List<ParameterModel> fetchParametersById(long merchantId) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchParametersById -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		List<ParameterModel> parameterModels = new ArrayList<ParameterModel>();
		
		try {
				String sql="select PARAMETER_ID,PARAMETER_NAME,PARAMETER_TYPE,VISIBLE,PERSISTENT,MANDATORY,IS_DELETABLE,STATUS,CREATED_BY,CREATED_DATE from PARAMETER_MASTER where merchant_id =:merchant_id order by CREATED_DATE";
				
				System.out.println("Fetch Parameters :=> "+sql);
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setLongAtName("merchant_id",merchantId);
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					
					ParameterModel parameterModel = new ParameterModel();
					
					parameterModel.setParameterId(rs.getLong(1));
					parameterModel.setParameterName(rs.getString(2));
					parameterModel.setParameterType(rs.getString(3));
					parameterModel.setVisible(rs.getString(4));
					parameterModel.setPersistent(rs.getString(5));
					parameterModel.setMandatory(rs.getString(6));
					parameterModel.setIsDeletable(rs.getInt(7));
					
					if(rs.getInt(8) == 0)
						parameterModel.setStatus(Status.INACTIVE);
					else if(rs.getInt(8) == 1)
						parameterModel.setStatus(Status.ACTIVE);
					else
						parameterModel.setStatus(Status.DELETE);
					
					parameterModel.setCreatedBy(rs.getLong(9));
					parameterModel.setCreatedDate(rs.getString(10));
					
					parameterModels.add(parameterModel);
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
			logger.info("fetchParametersById -- END");
	   }
		
	   return parameterModels;
	}
	
	/**
	 * This method is used for fetching Parameter w.r.t ID.
	 * @param parameterId
	 * @return ParameterModel
	 * @throws Exception
	 */
	public ParameterModel fetchParameterById(long parameterId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchParameterById -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		ParameterModel parameterModel = new ParameterModel();
		
		try {
				String sql="select PARAMETER_ID,PARAMETER_NAME,PARAMETER_TYPE,VISIBLE,PERSISTENT,MANDATORY,IS_DELETABLE,STATUS,CREATED_BY,CREATED_DATE from PARAMETER_MASTER where PARAMETER_ID =:PARAMETER_ID order by CREATED_DATE";
				
				System.out.println("Fetch Parameter By Id :=> "+sql);
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setLongAtName("PARAMETER_ID",parameterId);
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					
					parameterModel.setParameterId(rs.getLong(1));
					parameterModel.setParameterName(rs.getString(2));
					parameterModel.setParameterType(rs.getString(3));
					parameterModel.setVisible(rs.getString(4));
					parameterModel.setPersistent(rs.getString(5));
					parameterModel.setMandatory(rs.getString(6));
					parameterModel.setIsDeletable(rs.getInt(7));
					
					if(rs.getInt(8) == 0)
						parameterModel.setStatus(Status.INACTIVE);
					else if(rs.getInt(8) == 1)
						parameterModel.setStatus(Status.ACTIVE);
					else
						parameterModel.setStatus(Status.DELETE);
					
					parameterModel.setCreatedBy(rs.getLong(9));
					parameterModel.setCreatedDate(rs.getString(10));
					
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
			logger.info("fetchParameterById -- END");
	   }
		
	   return parameterModel;
	}
	
	/**
	 * This method is used to fetch the Parameters w.r.t Parameter Name.
	 * @param parameterName
	 * @param merchantId
	 * @return ParameterModel
	 * @throws Exception
	 */
	public ParameterModel fetchParameterById(String parameterName, long merchantId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchParameterById -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		ParameterModel parameterModel = null;
		
		try {
				String sql="select PARAMETER_ID,PARAMETER_NAME,PARAMETER_TYPE,VISIBLE,PERSISTENT,MANDATORY,IS_DELETABLE,STATUS,CREATED_BY,CREATED_DATE from PARAMETER_MASTER where PARAMETER_NAME =:PARAMETER_NAME and MERCHANT_ID =:MERCHANT_ID order by CREATED_DATE";
				
				System.out.println("Fetch Parameter By Id :=> "+sql);
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("PARAMETER_NAME",parameterName);
				pst.setLongAtName("MERCHANT_ID", merchantId);
				ResultSet rs = pst.executeQuery();
				if(rs.next()){
					
					parameterModel = new ParameterModel();
					parameterModel.setParameterId(rs.getLong(1));
					parameterModel.setParameterName(rs.getString(2));
					parameterModel.setParameterType(rs.getString(3));
					parameterModel.setVisible(rs.getString(4));
					parameterModel.setPersistent(rs.getString(5));
					parameterModel.setMandatory(rs.getString(6));
					parameterModel.setIsDeletable(rs.getInt(7));
					
					if(rs.getInt(8) == 0)
						parameterModel.setStatus(Status.INACTIVE);
					else if(rs.getInt(8) == 1)
						parameterModel.setStatus(Status.ACTIVE);
					else
						parameterModel.setStatus(Status.DELETE);
					
					parameterModel.setCreatedBy(rs.getLong(9));
					parameterModel.setCreatedDate(rs.getString(10));
					
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
			logger.info("fetchParameterById -- END");
	   }
		
	   return parameterModel;
	}

}
