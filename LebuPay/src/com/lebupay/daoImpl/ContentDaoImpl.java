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

import com.lebupay.dao.ContentDAO;
import com.lebupay.model.ContentModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.Status;
import com.lebupay.model.TypeModel;

/**
 * This is ContentDaoImpl extends BaseDao and Implements ContentDAO used to perform operation on Content.
 * @author Java Team
 *
 */
@Repository
public class ContentDaoImpl extends BaseDao implements ContentDAO {

	private static Logger logger = Logger.getLogger(ContentDaoImpl.class);
	
	/**
	 * This method is used for inserting Content.
	 * @param contentModel
	 * @return long
	 * @throws Exception
	 */
	public long insertContent(ContentModel contentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertContent -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "insert into CONTENT_MASTER (CONTENT_ID,PATH,TYPE_ID,CONTENT,STATUS,CREATED_BY,CREATED_DATE) values(CONTENT_MASTER_SEQ.nextval,"
					+ ":PATH,"
					+ ":TYPE_ID,"
					+ ":CONTENT,"
					+ ":status,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0)) ";
			
			String pk[] = {"CONTENT_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setStringAtName("PATH", contentModel.getPath()); // PATH
			pst.setLongAtName("TYPE_ID", contentModel.getTypeModel().getTypeId()); // TYPE_ID
			pst.setStringAtName("CONTENT", contentModel.getContent()); // CONTENT
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
			pst.setLongAtName("CREATED_BY", contentModel.getCreatedBy()); // CREATED_BY
			
			System.out.println("Insert Content Master=> "+sql);
			result = pst.executeUpdate();
			
			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Content GENERATED KEY => " + result);
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
							logger.debug("Connection Closed for insertContent");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for insertContent"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("insertContent -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for updating the Content.
	 * @param contentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateContent(ContentModel contentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateContent -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "update CONTENT_MASTER set PATH =:PATH,"
					+ "TYPE_ID=:TYPE_ID,"
					+ "CONTENT=:CONTENT,"
					+ "STATUS=:STATUS,"
					+ "MODIFIED_DATE=localtimestamp(0),"
					+ "MODIFIED_BY=:MODIFIED_BY "
					+ "where CONTENT_ID =:CONTENT_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("PATH", contentModel.getPath()); // PATH
			pst.setLongAtName("TYPE_ID", contentModel.getTypeModel().getTypeId()); // TYPE_ID
			pst.setStringAtName("CONTENT", contentModel.getContent()); // CONTENT
			pst.setIntAtName("STATUS", contentModel.getStatus().ordinal()); // Status
			pst.setLongAtName("MODIFIED_BY", contentModel.getaModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("CONTENT_ID", contentModel.getContentId()); // CONTENT_ID
			
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
							logger.debug("Connection Closed for updateContent");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for updateContent"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateContent -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for fetch all the active Content Details.
	 * @return List
	 * @throws Exception
	 */
	public List<ContentModel> fetchAllActiveContents()  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveContents -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		List<ContentModel> contentModels = new ArrayList<ContentModel>();
		
		try {
			String sql = "select cm.CONTENT_ID,cm.PATH,cm.CONTENT,cm.STATUS,cm.CREATED_DATE,cm.CREATED_BY,cm.MODIFIED_DATE,cm.MODIFIED_BY,cm.TYPE_ID,tm.TYPE_NAME from CONTENT_MASTER cm, TYPE_MASTER tm where cm.TYPE_ID = tm.TYPE_ID and cm.STATUS!=:STATUS order by cm.CREATED_BY desc";
			
			System.out.println("Fetch Contents :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				ContentModel contentModel = new ContentModel();
				
				contentModel.setContentId(rs.getLong(1));
				contentModel.setPath(rs.getString(2));
				contentModel.setContent(rs.getString(3));
				
				if(rs.getInt(4) == 0)
					contentModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(4) == 1)
					contentModel.setStatus(Status.INACTIVE);
				else
					contentModel.setStatus(Status.DELETE);
				
				contentModel.setCreatedDate(rs.getString(5));
				contentModel.setCreatedBy(rs.getLong(6));
				contentModel.setModifiedDate(rs.getString(7));
				contentModel.setaModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				contentModel.setTypeModel(typeModel);
				
				contentModels.add(contentModel);
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
						logger.debug("Connection Closed for fetchAllActiveContents");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchAllActiveContents"
							+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveContents -- END");
		}
		
		return contentModels;
	}
	
	
	/**
	 * This method is used internally to generate the search query.
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchQueryForAdmin(DataTableModel dataTableModel, boolean isCount){
		String[] searchColumns = {"cm.PATH", "cm.CONTENT", "tm.TYPE_NAME"};
		
		String sql = "from CONTENT_MASTER cm, TYPE_MASTER tm";
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
			sql = "select cm.CONTENT_ID,cm.PATH,cm.CONTENT,cm.STATUS,cm.CREATED_DATE,cm.CREATED_BY,cm.MODIFIED_DATE,cm.MODIFIED_BY,cm.TYPE_ID,tm.TYPE_NAME "+sql;
		}
		else{
			sql = "select COUNT(*)as total "+sql;
		}
		return sql;
	}
	
	
	/**
	 * This method is used to fetch all the content details according to the searching criteria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List<ContentModel>
	 * @throws Exception
	 */
	public List<ContentModel> fetchAllContentsForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllContentsForAdmin -- START");
		}
		
		List<ContentModel> contentModels = new ArrayList<ContentModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, false)+" AND cm.TYPE_ID = TM.TYPE_ID and cm.STATUS!=:STATUS order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";

			System.out.println("Fetch Contents: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				ContentModel contentModel = new ContentModel();
				
				contentModel.setContentId(rs.getLong(1));
				contentModel.setPath(rs.getString(2));
				contentModel.setContent(rs.getString(3));
				
				if(rs.getInt(4) == 0)
					contentModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(4) == 1)
					contentModel.setStatus(Status.INACTIVE);
				else
					contentModel.setStatus(Status.DELETE);
				
				contentModel.setCreatedDate(rs.getString(5));
				contentModel.setCreatedBy(rs.getLong(6));
				contentModel.setModifiedDate(rs.getString(7));
				contentModel.setaModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				contentModel.setTypeModel(typeModel);
				
				contentModels.add(contentModel);
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
			logger.info("fetchAllContentsForAdmin -- END");
		}
		
		return contentModels;
		
	}
	
	
	/**
	 * This method is used to count the total number of contents in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getContentsCountForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("getContentsCountForAdmin -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, true)+"  AND cm.TYPE_ID = TM.TYPE_ID and cm.STATUS!=:STATUS";
			
				
			System.out.println("Contents Count: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
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
			logger.info("getContentsCountForAdmin -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used during the export of the content list.
	 * @param dataTableModel
	 * @return List<ContentModel>
	 * @throws Exception
	 */
	public List<ContentModel> fetchAllContentsForExportForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllContentsForExportForAdmin -- START");
		}
		
		List<ContentModel> contentModels = new ArrayList<ContentModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, false)+" AND cm.TYPE_ID = TM.TYPE_ID and cm.STATUS!=:STATUS order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";
			
				
			System.out.println("Fetch Contents For Export: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				ContentModel contentModel = new ContentModel();
				
				contentModel.setContentId(rs.getLong(1));
				contentModel.setPath(rs.getString(2));
				contentModel.setContent(rs.getString(3));
				
				if(rs.getInt(4) == 0)
					contentModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(4) == 1)
					contentModel.setStatus(Status.INACTIVE);
				else
					contentModel.setStatus(Status.DELETE);
				
				contentModel.setCreatedDate(rs.getString(5));
				contentModel.setCreatedBy(rs.getLong(6));
				contentModel.setModifiedDate(rs.getString(7));
				contentModel.setaModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				contentModel.setTypeModel(typeModel);
				
				contentModels.add(contentModel);
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
			logger.info("fetchAllContentsForExportForAdmin -- END");
		}
		
		return contentModels;
		
	}
	
	/**
	 * This method is used for fetching the active Content By ID.
	 * @param contentId
	 * @return ContentModel
	 * @throws Exception
	 */
	public ContentModel fetchActiveContentByID(long contentId)  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveContentByID -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		ContentModel contentModel = new ContentModel();
		
		try {
			String sql = "select cm.CONTENT_ID,cm.PATH,cm.CONTENT,cm.STATUS,cm.CREATED_DATE,cm.CREATED_BY,cm.MODIFIED_DATE,cm.MODIFIED_BY,cm.TYPE_ID,tm.TYPE_NAME from CONTENT_MASTER cm, TYPE_MASTER tm where cm.TYPE_ID = tm.TYPE_ID and cm.STATUS !=:STATUS and cm.CONTENT_ID=:CONTENT_ID order by cm.CREATED_BY desc";
			
			System.out.println("Fetch Contents :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			pst.setLongAtName("CONTENT_ID", contentId); // CONTENT_ID
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				contentModel.setContentId(rs.getLong(1));
				contentModel.setPath(rs.getString(2));
				contentModel.setContent(rs.getString(3));
				
				if(rs.getInt(4) == 0)
					contentModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(4) == 1)
					contentModel.setStatus(Status.INACTIVE);
				else
					contentModel.setStatus(Status.DELETE);
				
				contentModel.setCreatedDate(rs.getString(5));
				contentModel.setCreatedBy(rs.getLong(6));
				contentModel.setModifiedDate(rs.getString(7));
				contentModel.setaModifiedBy(rs.getLong(8));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(9));
				typeModel.setTypeName(rs.getString(10));
				contentModel.setTypeModel(typeModel);
				
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
						logger.debug("Connection Closed for fetchActiveContentByID");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchActiveContentByID"+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveContentByID -- END");
		}
		
		return contentModel;
	}

}
