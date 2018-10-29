/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.common.Encryption;
import com.lebupay.common.MessageUtil;
import com.lebupay.common.SendMail;
import com.lebupay.common.SpiderEmailSender;
import com.lebupay.dao.AdminDAO;
import com.lebupay.dao.MerchantDao;
import com.lebupay.dao.TicketDAO;
import com.lebupay.dao.TransactionDAO;
import com.lebupay.daoImpl.BaseDao;
import com.lebupay.exception.MailSendException;
import com.lebupay.model.AdminModel;
import com.lebupay.model.CardTypeModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.MerchantCardPercentageModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.Status;
import com.lebupay.model.TicketModel;
import com.lebupay.service.AdminService;
import com.lebupay.validation.AdminValidation;

import oracle.jdbc.OraclePreparedStatement;

/**
 * This is AdminServiceImpl Class implements AdminService interface is used to perform operation on Admin Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class AdminServiceImpl extends BaseDao implements AdminService {
	
	private static Logger logger = Logger.getLogger(AdminService.class);
	
	@Autowired
	private AdminValidation adminValidation;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private Encryption encryption;
	
	@Autowired
	private AdminDAO adminDao;
	
	@Autowired
	private SendMail sendMail;
	
	@Autowired
	private SpiderEmailSender spiderEmailSender;
	
	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private TransactionDAO transactionDao;
	
	@Autowired
	private TicketDAO ticketDao;
	
	/**
	 * This method is used for Login Check of the Admin.
	 * @param adminModel
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel login(AdminModel adminModel) throws Exception {
			
		if (logger.isInfoEnabled()) {
			logger.info("loginCheck -- START");
		}
		
		adminValidation.loginValidation(adminModel);
		adminModel = adminDao.login(adminModel.getUserName(),adminModel.getPassword());
		
		if (logger.isInfoEnabled()) {
			logger.info("login -- END");
		}
		
		return adminModel;
	}
	
	/**
	 * This method is used for email sending in the Forgot password of the Admin.
	 * @param adminModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(AdminModel adminModel, HttpServletRequest request) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- START");
		}
		
		int result = 0;
		try {
			
			adminModel = adminDao.emailCheckCreate(adminModel.getEmailId());
			
			if(Objects.nonNull(adminModel)) {
				
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://"
						+ request.getServerName() + ":" + request.getServerPort()
						+ path + "/";
				
				// Send Email
				String action="passwordResetAdmin";
				String jsonReqName = "";
				String jsonReqPath = "";
				String templateID = "";
				
				Connection connection = oracleConnection.Connect();
				OraclePreparedStatement  pst = null;
				
				try {
						String sql = "select c.templateID," // 1
								+ "t.req_file_name," // 2
								+ "t.req_file_location " // 3
								+ "from template_configuration c left outer join template_table t on c.templateID = t.ID "
								+ "where c.action=:ACTION";
						
						System.out.println("template congfig fetching ==>> "+sql);
						
						pst = (OraclePreparedStatement) connection.prepareStatement(sql);
						pst.setStringAtName("ACTION", action); // mobileNo
						ResultSet rs =  pst.executeQuery();
						if(rs.next()){
							jsonReqName = rs.getString("req_file_name");
							jsonReqPath = rs.getString("req_file_location");
							templateID = rs.getString("templateID");
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
				
				String header = "Password reset link";
				String resetLink = basePath+"admin/mail-forgot-password?adminId="+adminModel.getAdminId();
				String emailMessageBody = "<p>Hi there!</p><p>We got a request to reset your password for your Payment Gateway account </p><p><a href="+resetLink+">Click here</a> to change your password. </p> <p>Payment GateWay Team </p>";
				String subject = messageUtil.getBundle("forgotPassword.email.subject");
				
				
				String jsonReqString = getFileString(jsonReqName,jsonReqPath);
				jsonReqString= jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");
				
				jsonReqString= jsonReqString.replace("replace_header_here", header);
				jsonReqString= jsonReqString.replace("replace_resetLink_here", resetLink);
				jsonReqString= jsonReqString.replace("replace_emailMessageBody_here", emailMessageBody);
				jsonReqString= jsonReqString.replace("replace_subject_here",subject);
				jsonReqString= jsonReqString.replace("replace_to_here", adminModel.getEmailId());
				jsonReqString= jsonReqString.replace("replace_cc_here", "");
				jsonReqString= jsonReqString.replace("replace_bcc_here", "");
				jsonReqString= jsonReqString.replace("replace_templateID_here", templateID);
				
				spiderEmailSender.sendEmail(jsonReqString,true);
				
				
				//sendMail.send(adminModel.getEmailId(), messageBody, subject);
				result = 1;
			}
		} catch (MailSendException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			  e.printStackTrace();
	    }
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for the forgot password section of the Admin.
	 * @param adminModel
	 * @return int
	 * @throws Exception
	 */
	public int forgotPasswordChange(AdminModel adminModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordChange -- START");
		}
		
		adminValidation.forgotPassword(adminModel);
		int result = adminDao.forgotPassword(adminModel.getAdminId(), adminModel.getPassword());
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordChange -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used for fetching the Details of the Admin w.r.t Admin Id.
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel fetchAdminById(long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAdminById -- START");
		}
		
		AdminModel adminRoleModel = adminDao.fetchAdminById(adminId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAdminById -- END");
		}
		
		return adminRoleModel;
	}
	
	/**
	 * This method is used to Update the profile of the Admin.
	 * @param adminModel
	 * @return long
	 * @throws Exception
	 */
	public long updateProfile(AdminModel adminModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- START");
		}
		
		adminValidation.profileUpdateValidation(adminModel);
		long result = adminDao.update(adminModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to change password of the Admin.
	 * @param adminModel
	 * @return int
	 * @throws Exception
	 */
	public int changePassword(AdminModel adminModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- START");
		}
		
		adminValidation.changePassword(adminModel);
		int result = adminDao.changePassword(adminModel.getAdminId(), adminModel.getPassword(), adminModel.getConfirmNewPassword());
				
		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- END");
		}
		
		return result;
	}

	/**
	 * This method is used to show in active merchant list.
	 * @param status
	 * @return List
	 * @throws Exception
	 */
	public List<MerchantModel> fetchAllActiveMerchants(Status status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveMerchants -- START");
		}
		
		List<MerchantModel> merchantModels = merchantDao.fetchAllActiveMerchants(status);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveMerchants -- START");
		}
		
		return merchantModels;
	}
	
	/**
	 * This method is used to fetch the Active Merchant List.
	 * @param dataTableModel
	 * @param status
	 * @throws Exception
	 */
	public void fetchActiveMerchantList(DataTableModel dataTableModel, Status status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveMerchantList -- START");
		}
		
		List<MerchantModel> merchantModels = merchantDao.fetchActiveMerchantList(dataTableModel, status);
		dataTableModel.setData(merchantModels);
		dataTableModel.setRecordsTotal(merchantDao.getActiveMerchantListCount(dataTableModel, status));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveMerchantList -- END");
		}
	}
	
	/**
	 * This method is used for generating the Excel of the Merchant List in the Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param status
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportActiveMerchantListForExcel(DataTableModel dataTableModel, int noOfColumns, Status status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportActiveMerchantListForExcel -- START");
		}
		
		List<MerchantModel> merchantModels = merchantDao.fetchAllActiveMerchantListForExport("Merchant", dataTableModel, status);
		Object[][] objects = new Object[merchantModels.size()+1][noOfColumns];
		
		
		String[] columns = dataTableModel.getColumnNames().split(",");
		objects[0] = columns;
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}
			objects2[j++] = merchantModels.get(i).getFirstName();
			objects2[j++] = merchantModels.get(i).getLastName();
			objects2[j++] = merchantModels.get(i).getEmailId();
			objects2[j++] = merchantModels.get(i).getMobileNo();
			objects2[j] = merchantModels.get(i).getStatus().name();
			
			objects[++i] = objects2;
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("exportActiveMerchantListForExcel -- END");
		}
		
		return objects;
	}
	
	/**
	 * This method is used for Activate the Merchant.
	 * @param merchantId
	 * @param adminId
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int activateMerchant(long merchantId, long adminId, Status status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("activateMerchant -- START");
		}
		
		int result = merchantDao.activateMerchant(merchantId, adminId, status);
				
		if (logger.isInfoEnabled()) {
			logger.info("activateMerchant -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for Fetching the Active and Activated Merchant List.
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveAndActivateMerchantById(long merchantId) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- START");
		}
		
		MerchantModel merchantModel = merchantDao.fetchActiveAndActivateMerchantById(merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- END");
		}
		
		return merchantModel;
	}
	
	/**
	 * This method is used for Update the Profile of the Admin.
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int updateProfileByAdmin(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateProfileByAdmin -- START");
		}
		
		adminValidation.merchantProfileValidation(merchantModel);
		int result = merchantDao.updateProfileByAdmin(merchantModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateProfileByAdmin -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used for Fetching the Ticket Details.
	 * @return List
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTickets() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- START");
		}
		
		List<TicketModel> ticketModels = ticketDao.fetchAllTickets("User", 0);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- END");
		}
		
		return ticketModels;
	}
	
	/**
	 * This method is used for Fetching the Ticket Details of the Datatable.
	 * @param dataTableModel
	 * @param userId
	 * @throws Exception
	 */
	public void fetchTickets(DataTableModel dataTableModel, long userId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTickets -- START");
		}
		
		List<TicketModel> ticketModels = ticketDao.fetchAllTickets("User", userId, dataTableModel);
		dataTableModel.setData(ticketModels);
		dataTableModel.setRecordsTotal(ticketDao.getTicketsCount("User",userId, dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTickets -- END");
		}
	}
	
	/**
	 * This method is used for Generating the Excel of the Ticket Details.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param userId
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportTicketsForExcel(DataTableModel dataTableModel, int noOfColumns, long userId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportTicketsForExcel -- START");
		}
		
		List<TicketModel> ticketModels = ticketDao.fetchAllTicketsForExport("User", userId, dataTableModel);
		Object[][] objects = new Object[ticketModels.size()+1][noOfColumns];
		
		
		String[] columns = dataTableModel.getColumnNames().split(",");
		objects[0] = columns;
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}
			objects2[j++] = ticketModels.get(i).getSubject();
			objects2[j++] = ticketModels.get(i).getTicketMessage();
			objects2[j++] = ticketModels.get(i).getReply();
			objects2[j] = ticketModels.get(i).getStatus().name();
			
			objects[++i] = objects2;
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("exportTicketsForExcel -- END");
		}
		
		return objects;
	}
	
	/**
	 * This method is used for Fetching the Tickets w.r.t Ticket Id.
	 * @param ticketId
	 * @return TicketModel
	 * @throws Exception
	 */
	public TicketModel fetchTicketById(int ticketId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTicketById -- START");
		}
		
		TicketModel ticketModel = ticketDao.fetchTicketById("User", 0, ticketId);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchTicketById -- END");
		}
		
		return ticketModel;
		
	}
	
	/**
	 * This method is used for Replying the Ticket.
	 * @param ticketModel
	 * @return int
	 * @throws Exception
	 */
	public int replyTicket(TicketModel ticketModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("replyTicket -- START");
		}
		
		int result = ticketDao.replyTicket(ticketModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("replyTicket -- END");
		}
		
		return result;
	}
	
	/**
	 * CREATE USERID AND PASSWORD
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int createUserIdAndPassword(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("createUserIdAndPassword -- START");
		}
		
		// On Client Demand There will be no validation on Bank Details Update.
		//adminValidation.createUserIdAndPasswordValidation(merchantModel); 
		int result = merchantDao.createUserIdAndPassword(merchantModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("createUserIdAndPassword -- END");
		}
		
		return result;
		
	}
	
	@Override
	public List<CardTypeModel> fetchCardTypeList() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCardTypeList -- START");
		}
		
			List<CardTypeModel> cardTypeModels = adminDao.fetchCardTypeList();
		
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
		
			List<MerchantCardPercentageModel> merchantCardPercentageModels =  adminDao.fetchCardPercentageByMerchantId(merchantId); 
		
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCardPercentageByMerchantId -- END");
		}
		
		return merchantCardPercentageModels;
	}

	@Override
	public long saveCardPercentageByMerchantId(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveCardPercentageByMerchantId -- START");
		}
		
		adminValidation.saveCardPercentageValidation(merchantCardPercentageModel);
		long result = 0;
		
		result = adminDao.addCardPercenatge(merchantCardPercentageModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("saveCardPercentageByMerchantId -- END");
		}
		
		return result;
	}

	@Override
	public long updateCardPercentageByMerchantId(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCardPercentageByMerchantId -- START");
		}
		adminValidation.saveCardPercentageValidation(merchantCardPercentageModel);
		long result = 0;
		
		result = adminDao.updateCardPercenatge(merchantCardPercentageModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCardPercentageByMerchantId -- END");
		}
		
		return result;
	}
	
	public String getFileString(String filename,String path) throws IOException {
		File fl = new File(path+"/"+filename);
		
		String targetFileStr = new String(Files.readAllBytes(Paths.get(fl.getAbsolutePath())));
		return targetFileStr;
	}
}
