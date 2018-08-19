/**
 * @formatter:off
 *
 */
package com.lebupay.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.lebupay.dao.AdminDAO;
import com.lebupay.model.AdminModel;
import com.lebupay.model.CardTypeModel;
import com.lebupay.model.MerchantCardPercentageModel;
import com.lebupay.model.Status;

/**
 * This Class is used to declare methods for Admin Database Operations which Implements AdminDAO Interface.
 * @author Java Team
 *
 */
@Repository
public class AdminDaoImpl extends BaseDao implements AdminDAO{

	private static Logger logger = Logger.getLogger(AdminDaoImpl.class);
	
	/**
	 * This method is used for login purpose of the Admin.
	 * @param userName
	 * @param password
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel login(String userName, String password) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("login -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		AdminModel adminModel = null;
		
		try {
			String sql = "";
			if(userName.contains("@")) { // User has provided Email ID for login
				
				sql = "SELECT ADMIN_ID,"
						+ "FIRST_NAME,"
						+ "LAST_NAME,"
						+ "EMAIL_ID,"
						+ "MOBILE_NO "
						+ "FROM ADMIN_MASTER am left outer join TYPE_MASTER t on am.TYPE_ID = t.TYPE_ID "
						+ "WHERE am.EMAIL_ID =:EMAIL_ID and am.PASSWORD =:PASSWORD and am.STATUS =:status ";
				
			} else { // User has provided Mobile Number for login
				
				sql = "SELECT ADMIN_ID,"
						+ "FIRST_NAME,"
						+ "LAST_NAME,"
						+ "EMAIL_ID,"
						+ "MOBILE_NO "
						+ "FROM ADMIN_MASTER am left outer join TYPE_MASTER t on am.TYPE_ID = t.TYPE_ID "
						+ "WHERE am.MOBILE_NO =:EMAIL_ID and am.PASSWORD =:PASSWORD and am.STATUS =:status ";
				
			}
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("EMAIL_ID", userName);
				pst.setStringAtName("PASSWORD", password.toUpperCase());
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				
				System.out.println("Login Check => "+sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {

					adminModel = new AdminModel();
					adminModel.setAdminId(rs.getLong(1));
					adminModel.setFirstName(rs.getString(2));
					adminModel.setLastName(rs.getString(3));
					adminModel.setEmailId(rs.getString(4));
					adminModel.setMobileNo(rs.getString(5));
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for login");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for login"
							+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("login -- END");
		}
		
		return adminModel;
	}
	
	/**
	 * This method is used for checking whether the email is valid or not.
	 * @param emailId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel emailCheckCreate(String emailId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("emailCheckCreate -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		AdminModel adminModel = null;
		
		try {
			String sql = "SELECT ADMIN_ID,"
						+ "FIRST_NAME,"
						+ "LAST_NAME,"
						+ "EMAIL_ID,"
						+ "MOBILE_NO "
						+ "FROM ADMIN_MASTER am left outer join TYPE_MASTER t on am.TYPE_ID = t.TYPE_ID "
						+ "WHERE am.EMAIL_ID =:EMAIL_ID and am.STATUS =:status ";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("EMAIL_ID", emailId); // EMAIL_ID
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				
				System.out.println("Email Check Create=> "+sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {

					adminModel = new AdminModel();
					adminModel.setAdminId(rs.getLong(1));
					adminModel.setFirstName(rs.getString(2));
					adminModel.setLastName(rs.getString(3));
					adminModel.setEmailId(rs.getString(4));
					adminModel.setMobileNo(rs.getString(5));
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for emailCheckCreate");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for emailCheckCreate"+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("emailCheckCreate -- END");
		}
		
		return adminModel;
	}
	
	/**
	 * This method is used for checking whether the Mobile Number is valid or not.
	 * @param mobileNo
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel mobileCheckCreate(String mobileNo) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("mobileCheckCreate -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		AdminModel adminModel = null;
		
		try {
			String sql = "SELECT ADMIN_ID,"
						+ "FIRST_NAME,"
						+ "LAST_NAME,"
						+ "EMAIL_ID,"
						+ "MOBILE_NO "
						+ "FROM ADMIN_MASTER am left outer join TYPE_MASTER t on am.TYPE_ID = t.TYPE_ID "
						+ "WHERE am.MOBILE_NO =:MOBILE_NO and am.STATUS =:status ";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("MOBILE_NO", mobileNo); // MOBILE_NO
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				
				System.out.println("Mobile Check Create=> "+sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {

					adminModel = new AdminModel();
					adminModel.setAdminId(rs.getLong(1));
					adminModel.setFirstName(rs.getString(2));
					adminModel.setLastName(rs.getString(3));
					adminModel.setEmailId(rs.getString(4));
					adminModel.setMobileNo(rs.getString(5));
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for mobileCheckCreate");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for mobileCheckCreate"+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("mobileCheckCreate -- END");
		}
		
		return adminModel;
	}
	
	/**
	 * This method is used for checking whether the email is valid or not during update.
	 * @param emailId
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel emailCheckUpdate(String emailId, long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("emailCheckUpdate -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		AdminModel adminModel = null;
		
		try {
			String sql = "SELECT ADMIN_ID,"
						+ "FIRST_NAME,"
						+ "LAST_NAME,"
						+ "EMAIL_ID,"
						+ "MOBILE_NO "
						+ "FROM ADMIN_MASTER am left outer join TYPE_MASTER t on am.TYPE_ID = t.TYPE_ID "
						+ "WHERE am.EMAIL_ID =:EMAIL_ID and am.STATUS =:status and ADMIN_ID !=:ADMIN_ID";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("EMAIL_ID", emailId); // EMAIL_ID
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				pst.setLongAtName("ADMIN_ID", adminId); // ADMIN_ID
				
				System.out.println("Email Check Update=> "+sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {

					adminModel = new AdminModel();
					adminModel.setAdminId(rs.getLong(1));
					adminModel.setFirstName(rs.getString(2));
					adminModel.setLastName(rs.getString(3));
					adminModel.setEmailId(rs.getString(4));
					adminModel.setMobileNo(rs.getString(5));
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for emailCheckUpdate");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for emailCheckUpdate"+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("emailCheckUpdate -- END");
		}
		
		return adminModel;
	}
	
	/**
	 * This method is used for checking whether the Mobile Number is valid or not during update.
	 * @param mobileNo
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel mobileCheckUpdate(String mobileNo, long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("mobileCheckUpdate -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		AdminModel adminModel = null;
		
		try {
			String sql = "SELECT ADMIN_ID,"
						+ "FIRST_NAME,"
						+ "LAST_NAME,"
						+ "EMAIL_ID,"
						+ "MOBILE_NO "
						+ "FROM ADMIN_MASTER am left outer join TYPE_MASTER t on am.TYPE_ID = t.TYPE_ID "
						+ "WHERE am.MOBILE_NO =:MOBILE_NO and am.STATUS =:status and ADMIN_ID !=:ADMIN_ID";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("MOBILE_NO", mobileNo); // MOBILE_NO
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				pst.setLongAtName("ADMIN_ID", adminId); // ADMIN_ID
				
				System.out.println("Mobile Check Update=> "+sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {

					adminModel = new AdminModel();
					adminModel.setAdminId(rs.getLong(1));
					adminModel.setFirstName(rs.getString(2));
					adminModel.setLastName(rs.getString(3));
					adminModel.setEmailId(rs.getString(4));
					adminModel.setMobileNo(rs.getString(5));
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for mobileCheckUpdate");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for mobileCheckUpdate"+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("mobileCheckUpdate -- END");
		}
		
		return adminModel;
	}
	
	/**
	 * This method is used for forgot password.
	 * @param adminId
	 * @param password
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(long adminId, String password) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update ADMIN_MASTER set password =( select standard_hash(:password, 'MD5') md5 from DUAL) where ADMIN_ID =: adminId";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("password", password);
			pst.setLongAtName("adminId", adminId);
			
			System.out.println("Forgot Password => "+sql);
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
							logger.debug("Connection Closed for forgotPassword");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for forgotPassword"
								+ e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is for Admin Profile Data Update.
	 * @param adminModel
	 * @return long
	 * @throws Exception
	 */
	public long update(AdminModel adminModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("update -- START");
		}
		
		long result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {
				String sql = "update ADMIN_MASTER set first_name =:first_name,"
					+ "last_name =:last_name,"
					+ "email_id =:email_id,"
					+ "mobile_no =:mobile_no,"
					+ "status =:status,"
					+ "MODIFIED_BY =:MODIFIED_BY,"
					+ "MODIFIED_DATE = localtimestamp(0) where ADMIN_ID =:ADMIN_ID ";
				
			String pk[] = {"ADMIN_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql,pk);
			
			pst.setStringAtName("first_name", adminModel.getFirstName()); // first_name
			pst.setStringAtName("last_name", adminModel.getLastName()); // last_name
			pst.setStringAtName("email_id", adminModel.getEmailId()); // email_id
			pst.setStringAtName("mobile_no", adminModel.getMobileNo()); // mobile_no
			pst.setIntAtName("status", adminModel.getStatus().ordinal()); // mobile_no
			pst.setLongAtName("MODIFIED_BY", adminModel.getModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("ADMIN_ID", adminModel.getAdminId()); // ADMIN_ID
			
			System.out.println("Admin update => "+sql);
			
			result = pst.executeUpdate();
			if(result > 0) 
				result = 1;
			connection.commit();
			
			
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for update");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for update"
							+ e.getMessage());
				}

			}
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("update -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to change Admin's password.
	 * @param adminId
	 * @param oldPassword
	 * @param newPassword
	 * @return int
	 * @throws Exception
	 */
	public int changePassword(long adminId, String oldPassword, String newPassword) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- START");
		}
		
		int result = 0; // Checking failed for old password failed
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {
				String sql = "select password from ADMIN_MASTER where ADMIN_ID=:ADMIN_ID and password=( select standard_hash(:oldPassword, 'MD5') md5 from DUAL)";
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setLongAtName("ADMIN_ID",adminId);
				pst.setStringAtName("oldPassword", oldPassword);
				System.out.println("Change Password 1 => "+sql);
				ResultSet rs = pst.executeQuery();
				if(rs.next()){
					
					OraclePreparedStatement pst1 = null;
					try {
						String sql1 = "update ADMIN_MASTER set password =( select standard_hash(:newPassword, 'MD5') md5 from DUAL) where ADMIN_ID =:ADMIN_ID";
						pst1 = (OraclePreparedStatement) connection.prepareStatement(sql1);
						pst1.setStringAtName("newPassword", newPassword);
						pst1.setLongAtName("ADMIN_ID", adminId);
						
						System.out.println("Change Password 2 => "+sql1);
						result = pst1.executeUpdate();
						if (result > 0)
							result = 1; // Password Changed Successfully
						else
							result = 2; // Password Not Changed
						connection.commit();
						
					} finally {
						
							try{
						           
						           if(pst1 != null)
						            if(!pst1.isClosed())
						            	pst1.close();
						           
						          } catch(Exception e){
						                 e.printStackTrace();
						          }
		
					}
				}
				
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
	            if(!pst.isClosed())
	            	pst.close();
	           
	          } catch(Exception e){
	                 e.printStackTrace();
	          }
	
	          try{
	
	           if(connection != null)
	            if(!connection.isClosed())
	             connection.close();
	
	          } catch(Exception e){
	        	  e.printStackTrace();
	          }      
        }
		
		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to fetch the Admin w.r.t AdminId.
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel fetchAdminById(long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAdminById -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		AdminModel adminModel = null;
		
		try {
			String sql = "SELECT ADMIN_ID,"
						+ "FIRST_NAME,"
						+ "LAST_NAME,"
						+ "EMAIL_ID,"
						+ "MOBILE_NO "
						+ "FROM ADMIN_MASTER am left outer join TYPE_MASTER t on am.TYPE_ID = t.TYPE_ID "
						+ "WHERE am.ADMIN_ID =:ADMIN_ID and am.STATUS =:status ";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setLongAtName("ADMIN_ID", adminId);
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				
				System.out.println("Fetch AdminById => "+sql);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {

					adminModel = new AdminModel();
					adminModel.setAdminId(rs.getLong(1));
					adminModel.setFirstName(rs.getString(2));
					adminModel.setLastName(rs.getString(3));
					adminModel.setEmailId(rs.getString(4));
					adminModel.setMobileNo(rs.getString(5));
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for fetchAdminById");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchAdminById"+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAdminById -- END");
		}
		
		return adminModel;
	}
	
	@Override
	public List<CardTypeModel> fetchCardTypeList() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCardTypeList -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		List<CardTypeModel> cardTypeModels = new LinkedList<CardTypeModel>();
		
		try {
			String sql = "SELECT CARD_TYPE_ID,"
						+ "CARD_TYPE"						
						+ " FROM CARD_TYPE_MASTER WHERE STATUS =:status ";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				
				System.out.println("Fetch AdminById => "+sql);
				ResultSet rs = pst.executeQuery();
				
				while (rs.next()) {
					
					CardTypeModel cardTypeModel= new CardTypeModel();		
					cardTypeModel.setCardTypeId(rs.getLong(1));
					cardTypeModel.setCardType(rs.getString(2));
					
					cardTypeModels.add(cardTypeModel);
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for fetchCardTypeList");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchCardTypeList"+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCardTypeList -- END");
		}
		
		return cardTypeModels;
	}

	@Override
	public List<MerchantCardPercentageModel> fetchCardPercentageByMerchantId(String merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchCardPercentageByMerchantId -- START");
		}
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		List<MerchantCardPercentageModel> merchantCardPercentageModels = new LinkedList<MerchantCardPercentageModel>();
		
		try {
			String sql = "SELECT CARD_PER_ID,MERCHANT_ID,CARD_TYPE_ID,PERCENTAGE,FIXED FROM MERCHANT_CARD_PERCENTAGE WHERE STATUS =:status AND MERCHANT_ID =:merchantId ORDER BY CARD_TYPE_ID ";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
				pst.setStringAtName("merchantId", merchantId); // status
				
				System.out.println("Fetch Card Percentage By MerchantId => "+sql);
				ResultSet rs = pst.executeQuery();
				
				while (rs.next()) {
					
					MerchantCardPercentageModel merchantCardPercentageModel= new MerchantCardPercentageModel();		
					merchantCardPercentageModel.setCardPerId(rs.getLong(1));
					merchantCardPercentageModel.setMerchantId(rs.getString(2));
					merchantCardPercentageModel.setCardTypeId(rs.getString(3));
					if(!rs.getString(4).equals("0"))
						merchantCardPercentageModel.setPercentage(rs.getDouble(4)>=1?rs.getString(4):String.valueOf(rs.getDouble(4)));
					else
						merchantCardPercentageModel.setPercentage(rs.getString(4));
					
					if(!rs.getString(5).equals("0"))
						merchantCardPercentageModel.setFlatFees(rs.getDouble(5)>=1?rs.getString(5):String.valueOf(rs.getDouble(5)));
					else
						merchantCardPercentageModel.setFlatFees(rs.getString(5));
					
					merchantCardPercentageModels.add(merchantCardPercentageModel);
				}
				
		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for fetchCardPercentageByMerchantId");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for fetchCardPercentageByMerchantId"+ e.getMessage());
				}

			}

		}
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCardPercentageByMerchantId -- END");
		}
		
		return merchantCardPercentageModels;
	}

	@Override
	public long addCardPercenatge(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("addCardPercenatge -- START");
		}
		System.out.println("DAO merchantCardPercentageModel "+merchantCardPercentageModel);
		long result = 0;
		boolean result1 = false;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			 if (Objects.nonNull(merchantCardPercentageModel.getListCardTypeIds()) && merchantCardPercentageModel.getListCardTypeIds() != null && merchantCardPercentageModel.getListCardTypeIds().size() > 0) {
				 for(int i = 0 ; i<merchantCardPercentageModel.getListCardTypeIds().size();i++){
					
					String sql = "insert into MERCHANT_CARD_PERCENTAGE (CARD_PER_ID,MERCHANT_ID,CARD_TYPE_ID,PERCENTAGE,FIXED,STATUS,CREATED_BY,CREATED_DATE) values(MERCHANT_CARD_PERCENTAGE_SEQ.nextval,"
							+ ":merchantId,"
							+ ":cardTypeId,"
							+ ":percentage,"
							+ ":fixed,"
							+ ":status,"
							+ ":createdBy,"
							+ "systimestamp(0)) ";
					
					String pk[] = {"CARD_PER_ID"};
					pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
					pst.setLongAtName("merchantId", Long.valueOf(merchantCardPercentageModel.getMerchantId())); // Merchant Id
					pst.setLongAtName("cardTypeId", Long.valueOf(merchantCardPercentageModel.getListCardTypeIds().get(i))); // CARD_TYPE_ID
					pst.setDoubleAtName("percentage", Double.valueOf(merchantCardPercentageModel.getListPercentage().get(i))); // percentage
					pst.setDoubleAtName("fixed", Double.valueOf(merchantCardPercentageModel.getListFlatFees().get(i))); // fixed
					pst.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
					pst.setLongAtName("createdBy", merchantCardPercentageModel.getCreatedBy()); // CREATED_BY
					
					System.out.println("Insert Banner Master=> "+sql);
					result1 = pst.execute();
					
					if (!result1) {

						ResultSet rs = pst.getGeneratedKeys();
						rs.next();
						result = rs.getInt(1);
						System.out.println("Card Percentage GENERATED KEY => " + result);
					}
				}
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
							logger.debug("Connection Closed for addCardPercenatge");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for addCardPercenatge" + e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("addCardPercenatge -- END");
		}
		
		return result;
	}

	@Override
	public long updateCardPercenatge(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateCardPercenatge -- START");
		}
		
		long result = 0;
		boolean result1 = false;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			
			String sqlDelete = "delete from MERCHANT_CARD_PERCENTAGE where MERCHANT_ID =:MERCHANT_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sqlDelete);
			pst.setLongAtName("MERCHANT_ID",Long.valueOf(merchantCardPercentageModel.getMerchantId())); // Merchant Id
			
			System.out.println("Delete Card Percentage=> "+sqlDelete);
			result = pst.executeUpdate();
			
				if (result > 0)
				{
					Connection connection1 = oracleConnection.Connect();
					OraclePreparedStatement pst1 = null;
					try{
						 if (Objects.nonNull(merchantCardPercentageModel.getListCardTypeIds()) && merchantCardPercentageModel.getListCardTypeIds() != null && merchantCardPercentageModel.getListCardTypeIds().size() > 0) {
							 for(int i = 0 ; i<merchantCardPercentageModel.getListCardTypeIds().size();i++){
								
								String sqlInsert = "insert into MERCHANT_CARD_PERCENTAGE (CARD_PER_ID,MERCHANT_ID,CARD_TYPE_ID,PERCENTAGE,FIXED,STATUS,MODIFIED_BY,MODIFIED_DATE) values(MERCHANT_CARD_PERCENTAGE_SEQ.nextval,"
										+ ":merchantId,"
										+ ":cardTypeId,"
										+ ":percentage,"
										+ ":fixed,"
										+ ":status,"
										+ ":modifiedBy,"
										+ "systimestamp(0)) ";
								
								String pk[] = {"CARD_PER_ID"};
								pst1 = (OraclePreparedStatement) connection1.prepareStatement(sqlInsert, pk);
								pst1.setLongAtName("merchantId", Long.valueOf(merchantCardPercentageModel.getMerchantId())); // Merchant Id
								pst1.setLongAtName("cardTypeId", Long.valueOf(merchantCardPercentageModel.getListCardTypeIds().get(i))); // CARD_TYPE_ID
								pst1.setDoubleAtName("percentage", Double.valueOf(merchantCardPercentageModel.getListPercentage().get(i))); // percentage
								pst1.setDoubleAtName("fixed", Double.valueOf(merchantCardPercentageModel.getListFlatFees().get(i))); // fixed
								pst1.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
								pst1.setLongAtName("modifiedBy", merchantCardPercentageModel.getaModifiedBy()); // MODIFIED_BY
								
								System.out.println("Update Card Percentage=> "+sqlInsert);
								result1 = pst1.execute();
								
								if (!result1) {
			
									ResultSet rs = pst1.getGeneratedKeys();
									rs.next();
									result = rs.getInt(1);
									System.out.println("Card Percentage GENERATED KEY => " + result);
								}
							}
						 }
						 connection1.commit();
					} finally {
						
						try{
					           
					           if(pst1 != null)
					            if(!pst1.isClosed())
					            	pst1.close();
					           
					          } catch(Exception e){
					                 e.printStackTrace();
					          }
	
						try { // Closing Connection Object
							if (connection1 != null) {
	
								if (!connection1.isClosed())
									connection1.close();
	
								if (logger.isDebugEnabled()) {
									logger.debug("Connection1 Closed for phoneValidation");
								}
							}
						} catch (Exception e) {
	
							if (logger.isDebugEnabled()) {
								logger.debug("Connection1 not closed for phoneValidation"
										+ e.getMessage());
							}
	
						}
					}
				
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
							logger.debug("Connection Closed for updateCardPercenatge");
						}
					}
				} catch (Exception e) {

					if (logger.isDebugEnabled()) {
						logger.debug("Connection not closed for updateCardPercenatge" + e.getMessage());
					}

				}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCardPercenatge -- END");
		}
		
		return result;
	}

}
