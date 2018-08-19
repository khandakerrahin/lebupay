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

import com.lebupay.dao.FaqDAO;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.FaqModel;
import com.lebupay.model.Status;
import com.lebupay.model.TypeModel;

/**
 * This is FaqDaoImpl extends BaseDao and Implements FaqDao Interface used to perform operation on FaqModule.
 * @author Java Team
 *
 */
@Repository
public class FaqDaoImpl extends BaseDao implements FaqDAO {

	private static Logger logger = Logger.getLogger(FaqDaoImpl.class);
	
	/**
	 * This method is used for inserting FAQ.
	 * @param faqModel
	 * @return long
	 * @throws Exception
	 */
	public long insertFaq(FaqModel faqModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertFaq -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "insert into FAQ_MASTER (FAQ_ID,QUESTION,ANSWER,TYPE_ID,STATUS,CREATED_BY,CREATED_DATE) values(FAQ_MASTER_SEQ.nextval,"
					+ ":QUESTION,"
					+ ":ANSWER,"
					+ ":TYPE_ID,"
					+ ":status,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0)) ";
			
			String pk[] = {"FAQ_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setStringAtName("QUESTION", faqModel.getQuestion()); // QUESTION
			pst.setStringAtName("ANSWER", faqModel.getAnswer()); // QUESTION
			pst.setLongAtName("TYPE_ID", faqModel.getTypeModel().getTypeId()); // TYPE_ID
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
			pst.setLongAtName("CREATED_BY", faqModel.getCreatedBy()); // CREATED_BY
			
			System.out.println("Insert Faq Master=> "+sql);
			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Faq GENERATED KEY => " + result);
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
							logger.debug("Connection Closed for insertFaq");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for insertFaq"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("insertFaq -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for updating the FAQ.
	 * @param faqModel
	 * @return int
	 * @throws Exception
	 */
	public int updateFaq(FaqModel faqModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateFaq -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "update FAQ_MASTER set QUESTION =:QUESTION,"
					+ "ANSWER=:ANSWER,"
					+ "TYPE_ID=:TYPE_ID,"
					+ "STATUS=:STATUS,"
					+ "MODIFIED_DATE=localtimestamp(0),"
					+ "MODIFIED_BY=:MODIFIED_BY "
					+ "where FAQ_ID =:FAQ_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("QUESTION", faqModel.getQuestion()); // QUESTION
			pst.setStringAtName("ANSWER", faqModel.getAnswer()); // QUESTION
			pst.setLongAtName("TYPE_ID", faqModel.getTypeModel().getTypeId()); // TYPE_ID
			pst.setIntAtName("STATUS", faqModel.getStatus().ordinal()); // Status
			pst.setLongAtName("MODIFIED_BY", faqModel.getaModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("FAQ_ID", faqModel.getFaqId()); // FAQ_ID
			
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
							logger.debug("Connection Closed for updateFaq");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for updateFaq"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateFaq -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for fetching all active FAQS.
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllActiveFaqs()  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveFaqs -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		List<FaqModel> faqModels = new ArrayList<FaqModel>();
		
		try {
			String sql = "select fm.FAQ_ID,fm.QUESTION,fm.ANSWER,fm.STATUS,fm.CREATED_DATE,fm.CREATED_BY,fm.MODIFIED_DATE,fm.MODIFIED_BY,fm.TYPE_ID,tm.TYPE_NAME from FAQ_MASTER fm, TYPE_MASTER tm where fm.STATUS!=:STATUS and fm.TYPE_ID = tm.TYPE_ID order by fm.CREATED_BY desc";
			
			System.out.println("Fetch Faqs :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				FaqModel faqModel = new FaqModel();
				
				faqModel.setFaqId(rs.getLong(1));
				faqModel.setQuestion(rs.getString(2));
				faqModel.setAnswer(rs.getString(3));
				
				if(rs.getInt(4) == 0)
					faqModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(4) == 1)
					faqModel.setStatus(Status.INACTIVE);
				else
					faqModel.setStatus(Status.DELETE);
				
				faqModel.setCreatedDate(rs.getString(5));
				faqModel.setCreatedBy(rs.getLong(6));
				faqModel.setModifiedDate(rs.getString(7));
				faqModel.setaModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				faqModel.setTypeModel(typeModel);
				
				faqModels.add(faqModel);
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
						logger.debug("Connection Closed for fetchAllActiveFaqs");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchAllActiveFaqs"
							+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveFaqs -- END");
		}
		
		return faqModels;
	}
	
	/**
	 * This method is used internally to generate the search query.
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchQueryForAdmin(DataTableModel dataTableModel, boolean isCount){
		String[] searchColumns = {"fm.QUESTION", "fm.ANSWER", "tm.TYPE_NAME"};
		
		String sql = "from FAQ_MASTER fm, TYPE_MASTER tm";
		if(searchColumns.length > 0){
			sql += " where (";
			for (int i = 0; i < searchColumns.length; i++) {
				if(i != 0){
					sql += " OR";
				}
				sql += " "+ searchColumns[i] +" like :searchstr";
			}
			sql += ")";
		}
		
		
		if(!isCount){
			sql = "select fm.FAQ_ID,fm.QUESTION,fm.ANSWER,fm.STATUS,fm.CREATED_DATE,fm.CREATED_BY,fm.MODIFIED_DATE,fm.MODIFIED_BY,fm.TYPE_ID,tm.TYPE_NAME "+sql;
		}
		else{
			sql = "select COUNT(*)as total "+sql;
		}
		return sql;
	}
	
	
	/**
	 * This method is used to fetch all the faq details according to the searching citeria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllFaqsForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllFaqsForAdmin -- START");
		}
		
		List<FaqModel> faqModels = new ArrayList<FaqModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, false)+" AND FM.TYPE_ID = TM.TYPE_ID order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";

			System.out.println("Fetch Faqs: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				FaqModel faqModel = new FaqModel();
				
				faqModel.setFaqId(rs.getLong(1));
				faqModel.setQuestion(rs.getString(2));
				faqModel.setAnswer(rs.getString(3));
				if(rs.getInt(4) == 0)
					faqModel.setStatus(Status.INACTIVE);
				else if(rs.getInt(4) == 1)
					faqModel.setStatus(Status.ACTIVE);
				else
					faqModel.setStatus(Status.DELETE);
				
				faqModel.setCreatedDate(rs.getString(5));
				faqModel.setCreatedBy(rs.getLong(6));
				faqModel.setModifiedDate(rs.getString(7));
				faqModel.setModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				faqModel.setTypeModel(typeModel);
				
				faqModels.add(faqModel);
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
			logger.info("fetchAllFaqsForAdmin -- END");
		}
		
		return faqModels;
		
	}
	
	
	/**
	 * This method is used to count the total number of faqs in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getFaqsCountForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("getFaqsCountForAdmin -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, true)+"  AND FM.TYPE_ID = TM.TYPE_ID";
			
				
			System.out.println("Faqs Count: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			
			ResultSet rs =  pst.executeQuery();
			rs.next();
			result = rs.getInt(1);
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
			logger.info("getFaqsCountForAdmin -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used during the export of the faq list.
	 * @param dataTableModel
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllFaqsForExportForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllFaqsForExportForAdmin -- START");
		}
		
		List<FaqModel> faqModels = new ArrayList<FaqModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, false)+" AND FM.TYPE_ID = TM.TYPE_ID order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";
			
				
			System.out.println("Fetch Faqs For Export: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				FaqModel faqModel = new FaqModel();
				
				faqModel.setFaqId(rs.getLong(1));
				faqModel.setQuestion(rs.getString(2));
				faqModel.setAnswer(rs.getString(3));
				if(rs.getInt(4) == 0)
					faqModel.setStatus(Status.INACTIVE);
				else if(rs.getInt(4) == 1)
					faqModel.setStatus(Status.ACTIVE);
				else
					faqModel.setStatus(Status.DELETE);
				
				faqModel.setCreatedDate(rs.getString(5));
				faqModel.setCreatedBy(rs.getLong(6));
				faqModel.setModifiedDate(rs.getString(7));
				faqModel.setModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				faqModel.setTypeModel(typeModel);
				
				faqModels.add(faqModel);
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
			logger.info("fetchAllFaqsForExportForAdmin -- END");
		}
		
		return faqModels;
		
	}
	
	/**
	 * This method is used to fetch Active FAQ by ID.
	 * @param faqId
	 * @return FaqModel
	 * @throws Exception
	 */
	public FaqModel fetchActiveFaqByID(long faqId)  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveFaqByID -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		FaqModel faqModel = new FaqModel();
		
		try {
			String sql = "select fm.FAQ_ID,fm.QUESTION,fm.ANSWER,fm.STATUS,fm.CREATED_DATE,fm.CREATED_BY,fm.MODIFIED_DATE,fm.MODIFIED_BY,fm.TYPE_ID,tm.TYPE_NAME from FAQ_MASTER fm, TYPE_MASTER tm where fm.STATUS !=:STATUS and fm.TYPE_ID = tm.TYPE_ID and fm.FAQ_ID=:FAQ_ID order by fm.CREATED_BY desc";
			
			System.out.println("Fetch Faqs :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			pst.setLongAtName("FAQ_ID", faqId); // FAQ_ID
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				faqModel.setFaqId(rs.getLong(1));
				faqModel.setQuestion(rs.getString(2));
				faqModel.setAnswer(rs.getString(3));
				
				if(rs.getInt(4) == 0)
					faqModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(4) == 1)
					faqModel.setStatus(Status.INACTIVE);
				else
					faqModel.setStatus(Status.DELETE);
				
				faqModel.setCreatedDate(rs.getString(5));
				faqModel.setCreatedBy(rs.getLong(6));
				faqModel.setModifiedDate(rs.getString(7));
				faqModel.setaModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				faqModel.setTypeModel(typeModel);
				
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
						logger.debug("Connection Closed for fetchActiveFaqByID");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchActiveFaqByID"+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveFaqByID -- END");
		}
		
		return faqModel;
	}
	
	
	/**
	 * This method is used to fetch the active FAQ depending upon the user type.
	 * @param userType
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllActiveFaqsByType(String userType)  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveFaqsByType -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		List<FaqModel> faqModels = new ArrayList<FaqModel>();
		
		try {
			String sql = "select fm.FAQ_ID,fm.QUESTION,fm.ANSWER,fm.STATUS,fm.CREATED_DATE,fm.CREATED_BY,fm.MODIFIED_DATE,fm.MODIFIED_BY,fm.TYPE_ID,tm.TYPE_NAME from FAQ_MASTER fm, TYPE_MASTER tm where fm.STATUS!=:STATUS and fm.TYPE_ID = tm.TYPE_ID and tm.TYPE_ID =(select TYPE_ID from type_master where type_name = '"+userType+"') order by fm.CREATED_BY desc";
			
			System.out.println("Fetch Faqs By UserType:=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				FaqModel faqModel = new FaqModel();
				
				faqModel.setFaqId(rs.getLong(1));
				faqModel.setQuestion(rs.getString(2));
				faqModel.setAnswer(rs.getString(3));
				
				if(rs.getInt(4) == 0)
					faqModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(4) == 1)
					faqModel.setStatus(Status.INACTIVE);
				else
					faqModel.setStatus(Status.DELETE);
				
				faqModel.setCreatedDate(rs.getString(5));
				faqModel.setCreatedBy(rs.getLong(6));
				faqModel.setModifiedDate(rs.getString(7));
				faqModel.setaModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				faqModel.setTypeModel(typeModel);
				
				faqModels.add(faqModel);
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
						logger.debug("Connection Closed for fetchAllActiveFaqsByType");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchAllActiveFaqsByType"+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveFaqsByType -- END");
		}
		
		return faqModels;
	}

}
