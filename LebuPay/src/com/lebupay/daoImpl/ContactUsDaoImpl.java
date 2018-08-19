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

import com.lebupay.dao.ContactUsDAO;
import com.lebupay.model.ContactUsModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.Status;

/**
 * This is ContactUsDaoImpl used to perform operation Contact Us. 
 * @author Java Team
 *
 */
@Repository
public class ContactUsDaoImpl extends BaseDao implements ContactUsDAO{

	private static Logger logger = Logger.getLogger(ContactUsDaoImpl.class);
	
	/**
	 * This method is used to save the contact us.
	 * @param contactUsModel
	 * @return long
	 * @throws Exception
	 */
	public long saveContactUs(ContactUsModel contactUsModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveContactUs -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
				String sql = "insert into CONTACTUS_MASTER (CONTACTUS_ID,"
						+ "MESSAGE,"
						+ "EMAILID,"
						+ "NAME,"
						+ "STATUS,"
						+ "CREATED_DATE,"
						+ "SUBJECT) values(CONTACTUS_MASTER_SEQ.nextval,:MESSAGE,:EMAILID,:NAME,:status,localtimestamp(0),:SUBJECT) ";
			String pk[] = {"CONTACTUS_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql,pk);
			pst.setStringAtName("MESSAGE", contactUsModel.getContactUsMessage()); // MESSAGE
			pst.setStringAtName("EMAILID", contactUsModel.getEmailId()); // EMAILID
			pst.setStringAtName("NAME", contactUsModel.getName()); // NAME
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
			pst.setStringAtName("SUBJECT", contactUsModel.getSubject()); // Contact Us Subject
			
			System.out.println("Save Contact Us => "+sql);
			
			boolean result1 = pst.execute();
			if(!result1) {
				
				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Contact Us GENERATED KEY => "+result);
				connection.commit();
				
			}
			
		} finally {
	
			try { // Closing Connection Object
				if (connection != null) {
	
					if (!connection.isClosed())
						connection.close();
	
					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for saveContactUs");
					}
				}
			} catch (Exception e) {
	
				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for saveContactUs"
							+ e.getMessage());
				}
	
			}
	
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("saveContactUs -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to fetch all the Contact Us for Admin.
	 * @return List<ContactUsModel>
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUs() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllContactUs -- START");
		}
		
		List<ContactUsModel> contactUsModels = new ArrayList<ContactUsModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			
			String sql = "select CONTACTUS_ID,SUBJECT,MESSAGE,REPLY,NAME,EMAILID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY from CONTACTUS_MASTER ORDER BY CREATED_DATE DESC";
			
			System.out.println("Fetch Contact Us: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				ContactUsModel contactUsModel = new ContactUsModel();
				
				contactUsModel.setContactUsId(rs.getLong(1));
				contactUsModel.setSubject(rs.getString(2));
				contactUsModel.setContactUsMessage(rs.getString(3));
				contactUsModel.setReply(rs.getString(4));
				contactUsModel.setName(rs.getString(5));
				contactUsModel.setEmailId(rs.getString(6));
				
				if(rs.getInt(7) == 0)
					contactUsModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(7) == 1)
					contactUsModel.setStatus(Status.INACTIVE);
				else
					contactUsModel.setStatus(Status.DELETE);
				
				contactUsModel.setCreatedDate(rs.getString(8));
				contactUsModel.setCreatedBy(rs.getLong(9));
				contactUsModel.setModifiedDate(rs.getString(10));
				contactUsModel.setModifiedBy(rs.getLong(11));
				
				contactUsModels.add(contactUsModel);
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
			logger.info("fetchAllContactUs -- END");
		}
		
		return contactUsModels;
		
	}
	
	/**
	 * This method is used for fetching the ContactUs Details w.r.t ID.
	 * @param contactUsId
	 * @return ContactUsModel
	 * @throws Exception
	 */
	public ContactUsModel fetchContactUsByID(long contactUsId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchContactUsByID -- START");
		}
		
		ContactUsModel contactUsModel = new ContactUsModel();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			
			String sql = "select CONTACTUS_ID,SUBJECT,MESSAGE,REPLY,NAME,EMAILID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY from CONTACTUS_MASTER ORDER BY CREATED_DATE DESC";
			
			System.out.println("Fetch Contact Us By ID: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				contactUsModel.setContactUsId(rs.getLong(1));
				contactUsModel.setSubject(rs.getString(2));
				contactUsModel.setContactUsMessage(rs.getString(3));
				contactUsModel.setReply(rs.getString(4));
				contactUsModel.setName(rs.getString(5));
				contactUsModel.setEmailId(rs.getString(6));
				
				if(rs.getInt(7) == 0)
					contactUsModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(7) == 1)
					contactUsModel.setStatus(Status.INACTIVE);
				else
					contactUsModel.setStatus(Status.DELETE);
				
				contactUsModel.setCreatedDate(rs.getString(8));
				contactUsModel.setCreatedBy(rs.getLong(9));
				contactUsModel.setModifiedDate(rs.getString(10));
				contactUsModel.setModifiedBy(rs.getLong(11));
				
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
			logger.info("fetchContactUsByID -- END");
		}
		
		return contactUsModel;
		
	}
	
	/**
	 * This method is used by the Admin to reply the Contact Us.
	 * @param contactUsModel
	 * @return int
	 * @throws Exception
	 */
	public int replyContactUs(ContactUsModel contactUsModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("replyContactUs -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
				String sql = "update CONTACTUS_MASTER set REPLY=:REPLY,MODIFIED_DATE=localtimestamp(0),MODIFIED_BY=:MODIFIED_BY where CONTACTUS_ID=:CONTACTUS_ID";
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("REPLY", contactUsModel.getReply()); // Contact Us Reply
				pst.setLongAtName("MODIFIED_BY", contactUsModel.getCreatedBy()); // MODIFIED_BY ID
				pst.setLongAtName("CONTACTUS_ID", contactUsModel.getContactUsId()); // CONTACTUS_ID ID
				
				System.out.println("Reply Contact Us => "+sql);
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
						logger.debug("Connection Closed for replyContactUs");
					}
				}
			} catch (Exception e) {
	
				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for replyContactUs"
							+ e.getMessage());
				}
	
			}
	
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("replyContactUs -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to fetch all the query details according to the searching criteria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List<ContactUsModel>
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUsForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllContactUsForAdmin -- START");
		}
		
		List<ContactUsModel> contactUsModels = new ArrayList<ContactUsModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchContactUsForAdmin(dataTableModel, false)+" order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";

			System.out.println("Fetch Query: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchStr","%"+dataTableModel.getSearchString()+"%");
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				ContactUsModel contactUsModel = new ContactUsModel();
				
				contactUsModel.setContactUsId(rs.getLong(1));
				contactUsModel.setSubject(rs.getString(2));
				contactUsModel.setContactUsMessage(rs.getString(3));
				contactUsModel.setReply(rs.getString(4));
				contactUsModel.setName(rs.getString(5));
				contactUsModel.setEmailId(rs.getString(6));
				
				if(rs.getInt(7) == 0)
					contactUsModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(7) == 1)
					contactUsModel.setStatus(Status.INACTIVE);
				else
					contactUsModel.setStatus(Status.DELETE);
				
				contactUsModel.setCreatedDate(rs.getString(8));
				contactUsModel.setCreatedBy(rs.getLong(9));
				contactUsModel.setModifiedDate(rs.getString(10));
				contactUsModel.setModifiedBy(rs.getLong(11));
				
				contactUsModels.add(contactUsModel);
			}
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
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
			logger.info("fetchAllQueryForAdmin -- END");
		}
		
		return contactUsModels;
		
	}
	
	/**
	 * This method is used internally to generate the search query.
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchContactUsForAdmin(DataTableModel dataTableModel, boolean isCount){
		
		
		String[] searchColumns = {"EMAILID", "NAME", "SUBJECT", "REPLY", "MESSAGE"};
		
		String sql = "from CONTACTUS_MASTER";
		if(searchColumns.length > 0){
			sql += " where (";
			for (int i = 0; i < searchColumns.length; i++) {
				if(i != 0){
					sql += " OR";
				}
				sql += " "+ searchColumns[i] +" like :searchStr";
			}
			sql += ")";
		}
		
		
		if(!isCount){
			sql = "select CONTACTUS_ID,SUBJECT,MESSAGE,REPLY,NAME,EMAILID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY "+sql;
		} else {
			sql = "select COUNT(*)as total "+sql;
		}
		
		return sql;
	}
	
	/**
	 * This method is used to count the total number of query in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getContactUsCountForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("getContactUsCountForAdmin -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchContactUsForAdmin(dataTableModel, true);
			
				
			System.out.println("Query Count: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchStr","%"+dataTableModel.getSearchString()+"%");
			
			ResultSet rs =  pst.executeQuery();
			rs.next();
			result = rs.getInt(1);
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
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
			logger.info("getContactUsCountForAdmin -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used during the export of the query list.
	 * @param dataTableModel
	 * @return List<ContactUsModel>
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUsForExportForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllContactUsForExportForAdmin -- START");
		}
		
		List<ContactUsModel> contactUsModels = new ArrayList<ContactUsModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchContactUsForAdmin(dataTableModel, false)+" order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";
			
				
			System.out.println("Fetch Query For Export: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchStr","%"+dataTableModel.getSearchString()+"%");
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				ContactUsModel contactUsModel = new ContactUsModel();
				
				contactUsModel.setContactUsId(rs.getLong(1));
				contactUsModel.setSubject(rs.getString(2));
				contactUsModel.setContactUsMessage(rs.getString(3));
				contactUsModel.setReply(rs.getString(4));
				contactUsModel.setName(rs.getString(5));
				contactUsModel.setEmailId(rs.getString(6));
				
				if(rs.getInt(7) == 0)
					contactUsModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(7) == 1)
					contactUsModel.setStatus(Status.INACTIVE);
				else
					contactUsModel.setStatus(Status.DELETE);
				
				contactUsModel.setCreatedDate(rs.getString(8));
				contactUsModel.setCreatedBy(rs.getLong(9));
				contactUsModel.setModifiedDate(rs.getString(10));
				contactUsModel.setModifiedBy(rs.getLong(11));
				
				contactUsModels.add(contactUsModel);
			}
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
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
			logger.info("fetchAllContactUsForExportForAdmin -- END");
		}
		
		return contactUsModels;
		
	}

}
