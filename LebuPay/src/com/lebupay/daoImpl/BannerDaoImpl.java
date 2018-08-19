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

import com.lebupay.dao.BannerDAO;
import com.lebupay.model.BannerModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.Status;
import com.lebupay.model.TypeModel;

/**
 * This is BannerDAOImpl Class is implemented BannerDAO interface to perform operation on Banner like add ,update Banners.
 * @author Java Team
 *
 */
@Repository
public class BannerDaoImpl extends BaseDao implements BannerDAO {

	private static Logger logger = Logger.getLogger(BannerDaoImpl.class);
	
	/**
	 * This method is used for inserting BANNER.
	 * @param bannerModel
	 * @return long
	 * @throws Exception
	 */
	public long insertBanner(BannerModel bannerModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertBanner -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "insert into BANNER_MASTER (BANNER_ID,IMAGE_NAME,TYPE_ID,STATUS,CREATED_BY,CREATED_DATE) values(BANNER_MASTER_SEQ.nextval,"
					+ ":IMAGE_NAME,"
					+ ":TYPE_ID,"
					+ ":status,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0)) ";
			
			String pk[] = {"BANNER_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setStringAtName("IMAGE_NAME", bannerModel.getImageName()); // IMAGE_NAME
			pst.setLongAtName("TYPE_ID", bannerModel.getTypeModel().getTypeId()); // TYPE_ID
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
			pst.setLongAtName("CREATED_BY", bannerModel.getCreatedBy()); // CREATED_BY
			
			System.out.println("Insert Banner Master=> "+sql);
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
							logger.debug("Connection Closed for insertBanner");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for insertBanner"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("insertBanner -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for updating BANNER.
	 * @param bannerModel
	 * @return int
	 * @throws Exception
	 */
	public int updateBanner(BannerModel bannerModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateBanner -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sql = "update BANNER_MASTER set IMAGE_NAME =:IMAGE_NAME,"
					+ "TYPE_ID=:TYPE_ID,"
					+ "STATUS=:STATUS,"
					+ "MODIFIED_DATE=localtimestamp(0),"
					+ "MODIFIED_BY=:MODIFIED_BY "
					+ "where BANNER_ID =:BANNER_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("IMAGE_NAME", bannerModel.getImageName()); // IMAGE_NAME
			pst.setLongAtName("TYPE_ID", bannerModel.getTypeModel().getTypeId()); // TYPE_ID
			pst.setIntAtName("STATUS", bannerModel.getStatus().ordinal()); // Status
			pst.setLongAtName("MODIFIED_BY", bannerModel.getaModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("BANNER_ID", bannerModel.getBannerId()); // BANNER_ID
			
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
							logger.debug("Connection Closed for updateBanner");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for updateBanner"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateBanner -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for fetching all active Banners.
	 * @return List<BannerModel>
	 * @throws Exception
	 */
	public List<BannerModel> fetchAllActiveBanners()  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveBanners -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		List<BannerModel> bannerModels = new ArrayList<BannerModel>();
		
		try {
			String sql = "select bm.BANNER_ID,bm.IMAGE_NAME,bm.STATUS,bm.CREATED_DATE,bm.CREATED_BY,bm.MODIFIED_DATE,bm.MODIFIED_BY,bm.TYPE_ID,tm.TYPE_NAME from BANNER_MASTER bm, TYPE_MASTER tm where bm.TYPE_ID = tm.TYPE_ID and bm.STATUS!=:STATUS order by bm.CREATED_BY desc";
			
			System.out.println("Fetch Banners :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				BannerModel bannerModel = new BannerModel();
				
				bannerModel.setBannerId(rs.getLong(1));
				bannerModel.setImageName(rs.getString(2));
				
				if(rs.getInt(3) == 0)
					bannerModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(3) == 1)
					bannerModel.setStatus(Status.INACTIVE);
				else
					bannerModel.setStatus(Status.DELETE);
				
				bannerModel.setCreatedDate(rs.getString(4));
				bannerModel.setCreatedBy(rs.getLong(5));
				bannerModel.setModifiedDate(rs.getString(6));
				bannerModel.setaModifiedBy(rs.getLong(7));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(8));
				typeModel.setTypeName(rs.getString(9));
				bannerModel.setTypeModel(typeModel);
				
				bannerModels.add(bannerModel);
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
						logger.debug("Connection Closed for fetchAllActiveBanners");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchAllActiveBanners"
							+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveBanners -- END");
		}
		
		return bannerModels;
	}
	
	
	/**
	 * This method is used internally to generate the search query.
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchQueryForAdmin(DataTableModel dataTableModel, boolean isCount){
		String[] searchColumns = {"bm.IMAGE_NAME", "tm.TYPE_NAME"};
		
		String sql = "from BANNER_MASTER bm, TYPE_MASTER tm";
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
			sql = "select bm.BANNER_ID,bm.IMAGE_NAME,bm.STATUS,bm.CREATED_DATE,bm.CREATED_BY,bm.MODIFIED_DATE,bm.MODIFIED_BY,bm.TYPE_ID,tm.TYPE_NAME "+sql;
		}
		else{
			sql = "select COUNT(*)as total "+sql;
		}
		return sql;
	}
	
	
	/**
	 * This method is used to fetch all the banner details according to the searching citeria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List<BannerModel>
	 * @throws Exception
	 */
	public List<BannerModel> fetchAllBannersForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllBannersForAdmin -- START");
		}
		
		List<BannerModel> bannerModels = new ArrayList<BannerModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, false)+" AND BM.TYPE_ID = TM.TYPE_ID and bm.STATUS!=:STATUS order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";

			System.out.println("Fetch Banners: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				BannerModel bannerModel = new BannerModel();
				
				bannerModel.setBannerId(rs.getLong(1));
				bannerModel.setImageName(rs.getString(2));
				
				if(rs.getInt(3) == 0)
					bannerModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(3) == 1)
					bannerModel.setStatus(Status.INACTIVE);
				else
					bannerModel.setStatus(Status.DELETE);
				
				bannerModel.setCreatedDate(rs.getString(4));
				bannerModel.setCreatedBy(rs.getLong(5));
				bannerModel.setModifiedDate(rs.getString(6));
				bannerModel.setaModifiedBy(rs.getLong(7));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(8));
				typeModel.setTypeName(rs.getString(9));
				bannerModel.setTypeModel(typeModel);
				
				bannerModels.add(bannerModel);
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
			logger.info("fetchAllBannersForAdmin -- END");
		}
		
		return bannerModels;
		
	}
	
	
	/**
	 * This method is used to count the total number of banners in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getBannersCountForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("getBannersCountForAdmin -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, true)+"  AND BM.TYPE_ID = TM.TYPE_ID and bm.STATUS!=:STATUS";
			
				
			System.out.println("Banners Count: "+sql);
			
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
			logger.info("getBannersCountForAdmin -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used during the export of the banner list.
	 * @param dataTableModel
	 * @return List<BannerModel>
	 * @throws Exception
	 */
	public List<BannerModel> fetchAllBannersForExportForAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllBannersForExportForAdmin -- START");
		}
		
		List<BannerModel> bannerModels = new ArrayList<BannerModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQueryForAdmin(dataTableModel, false)+" AND BM.TYPE_ID = TM.TYPE_ID and bm.STATUS!=:STATUS order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";
			
				
			System.out.println("Fetch Banners For Export: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				BannerModel bannerModel = new BannerModel();
				
				bannerModel.setBannerId(rs.getLong(1));
				bannerModel.setImageName(rs.getString(2));
				
				if(rs.getInt(3) == 0)
					bannerModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(3) == 1)
					bannerModel.setStatus(Status.INACTIVE);
				else
					bannerModel.setStatus(Status.DELETE);
				
				bannerModel.setCreatedDate(rs.getString(4));
				bannerModel.setCreatedBy(rs.getLong(5));
				bannerModel.setModifiedDate(rs.getString(6));
				bannerModel.setaModifiedBy(rs.getLong(7));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(8));
				typeModel.setTypeName(rs.getString(9));
				bannerModel.setTypeModel(typeModel);
				
				bannerModels.add(bannerModel);
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
			logger.info("fetchAllBannersForExportForAdmin -- END");
		}
		
		return bannerModels;
		
	}
	
	/**
	 * This method is used for fetching active Banners By Id.
	 * @param bannerId
	 * @return BannerModel
	 * @throws Exception
	 */
	public BannerModel fetchActiveBannerByID(long bannerId)  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveBannerByID -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		BannerModel bannerModel = new BannerModel();
		
		try {
			String sql = "select bm.BANNER_ID,bm.IMAGE_NAME,bm.STATUS,bm.CREATED_DATE,bm.CREATED_BY,bm.MODIFIED_DATE,bm.MODIFIED_BY,bm.TYPE_ID,tm.TYPE_NAME from BANNER_MASTER bm, TYPE_MASTER tm where bm.TYPE_ID = tm.TYPE_ID and bm.BANNER_ID=:BANNER_ID and bm.STATUS!=:STATUS order by bm.CREATED_BY desc";
			
			System.out.println("Fetch Banners :=> "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // Status
			pst.setLongAtName("BANNER_ID", bannerId); // BANNER_ID
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				bannerModel.setBannerId(rs.getLong(1));
				bannerModel.setImageName(rs.getString(2));
				
				if(rs.getInt(3) == 0)
					bannerModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(3) == 1)
					bannerModel.setStatus(Status.INACTIVE);
				else
					bannerModel.setStatus(Status.DELETE);
				
				bannerModel.setCreatedDate(rs.getString(4));
				bannerModel.setCreatedBy(rs.getLong(5));
				bannerModel.setModifiedDate(rs.getString(6));
				bannerModel.setaModifiedBy(rs.getLong(7));
				
				TypeModel typeModel = new TypeModel();
				typeModel.setTypeId(rs.getLong(8));
				typeModel.setTypeName(rs.getString(9));
				bannerModel.setTypeModel(typeModel);
				
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
						logger.debug("Connection Closed for fetchActiveBannerByID");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchActiveBannerByID"+ e.getMessage());
				}

			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveBannerByID -- END");
		}
		
		return bannerModel;
	}

}
