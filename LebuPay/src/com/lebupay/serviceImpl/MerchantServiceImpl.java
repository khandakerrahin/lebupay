/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.common.Encryption;
import com.lebupay.common.HTTPConnection;
import com.lebupay.common.MessageUtil;
import com.lebupay.common.SendMail;
import com.lebupay.common.SpiderEmailSender;
import com.lebupay.common.SendSMS;
import com.lebupay.common.Util;
import com.lebupay.dao.MerchantDao;
import com.lebupay.dao.TicketDAO;
import com.lebupay.dao.TransactionDAO;
import com.lebupay.daoImpl.BaseDao;
import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CompanyModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.EmailInvoicingModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.Status;
import com.lebupay.model.TicketModel;
import com.lebupay.model.TransactionModel;
import com.lebupay.model.TypeModel;
import com.lebupay.service.MerchantService;
import com.lebupay.validation.MerchantValidation;

import oracle.jdbc.OraclePreparedStatement;

/**
 * This is MerchantServiceImpl Class implements MerchantService interface is used to perform operation on Merchant Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class MerchantServiceImpl extends BaseDao implements MerchantService {

	private static Logger logger = Logger.getLogger(MerchantServiceImpl.class);
	
	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private MerchantValidation merchantValidation;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private TransactionDAO transactionDao;
	
	@Autowired
	private SendMail sendMail;
	
	@Autowired
	private SpiderEmailSender spiderEmailSender;
	
	@Autowired
	private Encryption encryption;
	
	@Autowired
	private SendSMS sendSMS;
	
	@Autowired
	private TicketDAO ticketDao;
	
	@Value("${bitly.access.token}")
	private String bitlyAccessToken;
	
	@Value("${bitly.base.url}")
	private String bitlyBaseUrl;
	
	/**
	 * This method is use to create merchant
	 * @param merchantModel
	 * @param httpServletRequest
	 * @return long
	 * @throws Exception
	 */
	public long create(MerchantModel merchantModel, HttpServletRequest httpServletRequest) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("create -- START: ");
		}
		
		merchantValidation.signUpValidation(merchantModel, httpServletRequest);
		
		SecureRandom random = new SecureRandom();
		
		String uniqueKey = new BigInteger(130, random).toString(32);
		String accessKey = new BigInteger(130, random).toString(32);
		String secretKey = new BigInteger(130, random).toString(32);
		
		
		merchantModel.setUniqueKey(uniqueKey.substring(0, 10));
		merchantModel.setAccessKey(accessKey.substring(0, 20));
		merchantModel.setSecretKey(secretKey);
		
		int randomPIN = (int)(Math.random()*9000)+1000;
		Integer phoneCode = randomPIN;
		
		merchantModel.setPhoneCode(phoneCode);
		merchantModel.setCreatedBy(0L);
		
		long result = merchantDao.merchantSignUp(merchantModel);
		
		try{
			// Send Email
			
			String action="sendOTP";

			String [] retval = spiderEmailSender.fetchTempConfig(action);
			
			String jsonReqName = retval[0];
			String jsonReqPath = retval[1];
			String templateID = retval[2];
			
			String header = "OTP";
			String emailMessageBody = "<p>Hi there!</p><p>Your new OTP is "+phoneCode+". </p> <p>Payment GateWay Team </p>";
			String subject = messageUtil.getBundle("new.otp.email.subject");
			
			
			String jsonReqString = getFileString(jsonReqName,jsonReqPath);
			jsonReqString= jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");
			
			jsonReqString= jsonReqString.replace("replace_header_here", header);
			jsonReqString= jsonReqString.replace("replace_emailMessageBody_here", emailMessageBody);
			jsonReqString= jsonReqString.replace("replace_otp_here",""+phoneCode);
			jsonReqString= jsonReqString.replace("replace_subject_here",subject);
			jsonReqString= jsonReqString.replace("replace_to_here", merchantModel.getEmailId());
			jsonReqString= jsonReqString.replace("replace_cc_here", "");
			jsonReqString= jsonReqString.replace("replace_bcc_here", "");
			jsonReqString= jsonReqString.replace("replace_templateID_here", templateID);
			
			spiderEmailSender.sendEmail(jsonReqString,true);
			
			//sendMail.send(merchantModel.getEmailId(), messageBody, subject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * Send SMS
		 */
		if(result == 1){
			try {
				sendSMS.smsSend(merchantModel.getMobileNo(), "Dear Merchant, Your OTP is: "+phoneCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		if (logger.isInfoEnabled()) {
			logger.info("create -- END");
		}
		
		return result;
	}
	
	
	/**
	 * This method is use to verified mobile no and if mobile no is present then send a sms code in provided mobile no.
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int phoneVerification(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant phoneVerification -- START");
		}
		
		
		if(Util.isEmpty(merchantModel.getMerchantId())){
			throw new Exception(messageUtil.getBundle("merchant.id.required"));
		}
		
		if(Util.isEmpty(merchantModel.getPhoneCode())){
			throw new Exception(messageUtil.getBundle("merchant.phone.code.required"));
		}
		
		int result = merchantDao.phoneVerification(merchantModel.getMerchantId(), String.valueOf(merchantModel.getPhoneCode()));
		if(result > 0) {
			
			String phoneNumber = merchantModel.getMobileNo();
			String userEmail = merchantModel.getEmailId();
			phoneNumber = phoneNumber.substring(phoneNumber.length()-6, phoneNumber.length());
			
			try{
				
				// Send Email
				String action="phoneVerification";

				String [] retval = spiderEmailSender.fetchTempConfig(action);
				
				String jsonReqName = retval[0];
				String jsonReqPath = retval[1];
				String templateID = retval[2];
				
				String header = "Payment Gateway account request";
				String emailMessageBody = "<p>Hi there!</p><p>We got a request to create a new Payment Gateway account with your mobile number *****"+phoneNumber+" and email ID <a href=#>"+userEmail+"</a>. </p> <p>Payment GateWay Team </p>";
				String subject = messageUtil.getBundle("signup.email.subject");
				
				
				String jsonReqString = getFileString(jsonReqName,jsonReqPath);
				jsonReqString= jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");
				
				jsonReqString= jsonReqString.replace("replace_header_here", header);
				jsonReqString= jsonReqString.replace("replace_emailMessageBody_here", emailMessageBody);
				jsonReqString= jsonReqString.replace("replace_phoneNumber_here",""+phoneNumber);
				jsonReqString= jsonReqString.replace("replace_email_here",""+userEmail);
				jsonReqString= jsonReqString.replace("replace_subject_here",subject);
				jsonReqString= jsonReqString.replace("replace_to_here", userEmail);
				jsonReqString= jsonReqString.replace("replace_cc_here", "");
				jsonReqString= jsonReqString.replace("replace_bcc_here", "");
				jsonReqString= jsonReqString.replace("replace_templateID_here", templateID);
				
				spiderEmailSender.sendEmail(jsonReqString,true);

				//sendMail.send(userEmail, messageBody, subject);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("Merchant phoneVerification -- END");
		}
		
		return result;
	}
	
	
	/**
	 * This method is use to check login of the Merchant.
	 * @param merchantModel
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel login(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("login -- START: ");
		}
		
		merchantValidation.loginValidation(merchantModel);
		
		MerchantModel merchant = merchantDao.login(merchantModel.getUserName(), merchantModel.getPassword());
		
		if (logger.isInfoEnabled()) {
			logger.info("login -- END: ");
		}
		
		return merchant;
	}
	
	
	/**
	 * This method is use to check forgot password and if email id is present then send a link in provided email id. 
	 * @param merchantModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(MerchantModel merchantModel, HttpServletRequest request) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant forgotPassword -- START");
		}
		
		int result = 0;
		long checkResult =0;
			
			
			if(Util.isEmpty(merchantModel.getEmailId())){
				throw new Exception("Email id Required");
			}else {
				
				if(merchantModel.getEmailId().contains("@")) {
					MerchantModel checkEmail = merchantDao.createEmailCheck(merchantModel.getEmailId());
					checkResult = checkEmail.getMerchantId();
					if(Objects.isNull(checkEmail)){
						throw new Exception(messageUtil.getBundle("merchant.email.not.exist"));
					}
				}
			}
			
			long userId = checkResult;
			
			if(userId > 0){
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
				
				try{
					
					// Send Email
					String action="passwordResetMerchant";
					
					String [] retval = spiderEmailSender.fetchTempConfig(action);
					
					String jsonReqName = retval[0];
					String jsonReqPath = retval[1];
					String templateID = retval[2];
					
					String header = "Password reset link";
					String key = messageUtil.getBundle("secret.key");
					String id = encryption.encode(key, String.valueOf(userId));
					String subject = messageUtil.getBundle("forgotPassword.email.subject");
					
					String resetLink = basePath+"merchant/mail-forgot-password?userId="+id;
					String emailMessageBody = "<p>Hi there!</p><p>We got a request to reset your password for your Payment Gateway account </p><p><a href="+resetLink+">Click here</a> to change your password. </p> <p>Payment GateWay Team </p>";
					
					
					String jsonReqString = getFileString(jsonReqName,jsonReqPath);
					jsonReqString= jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");
					
					jsonReqString= jsonReqString.replace("replace_header_here", header);
					jsonReqString= jsonReqString.replace("replace_resetLink_here", resetLink);
					jsonReqString= jsonReqString.replace("replace_emailMessageBody_here", emailMessageBody);
					jsonReqString= jsonReqString.replace("replace_subject_here",subject);
					jsonReqString= jsonReqString.replace("replace_to_here", merchantModel.getEmailId());
					jsonReqString= jsonReqString.replace("replace_cc_here", "");
					jsonReqString= jsonReqString.replace("replace_bcc_here", "");
					jsonReqString= jsonReqString.replace("replace_templateID_here", templateID);
					
					spiderEmailSender.sendEmail(jsonReqString,true);
					
					
					
					//sendMail.send(merchantModel.getEmailId(), messageBody, subject);
					result = 1;
					
				} catch(Exception e){
					result = 1;
					e.printStackTrace();
				}
				
			}
		
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant forgotPassword -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is use in the forgot password of the Merchant.
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int forgotPasswordChange(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant forgotPasswordChange -- START");
		}
		
		merchantValidation.forgotPassword(merchantModel);
		
		int result = merchantDao.forgotPassword(merchantModel.getMerchantId(), merchantModel.getPassword());
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant forgotPasswordChange -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is use to change password of the Merchant.
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int changePassword(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant changePassword -- START");
		}
		
		merchantValidation.changePassword(merchantModel);
		int result = merchantDao.changePassword(merchantModel.getMerchantId(), merchantModel.getPassword(), merchantModel.getConfirmPassword());
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant changePassword -- END");
		}
		
		return result;
	}

	
	/**
	 * This method is used for Updating the Profile of the Merchant.
	 * @param merchantModel
	 * @return long
	 * @throws Exception
	 */
	public long updateProfile(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- START");
		}
		
		merchantValidation.merchantProfileValidation(merchantModel);
		long result = merchantDao.updateProfile(merchantModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- END");
		}
		
		return result;
	}
	
	
	/**
	 * This method is used for fetching the Active Merchant w.r.t Merchant Id.
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantById(long merchantId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveMerchantById -- START: ");
		}
		
		MerchantModel merchant = merchantDao.fetchActiveMerchantById(merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveMerchantById -- END: ");
		}
		
		return merchant;
	}
	
	/**
	 * This method is used to skip the COMPANY DETAILS.
	 * @param merchantID
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int skipCompany(long merchantID, int status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("skipCompany -- START");
		}
		
		int result = merchantDao.skipCompany(merchantID, status);
				
		if (logger.isInfoEnabled()) {
			logger.info("skipCompany -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is use to resent mobile code of the Merchant.
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int resend(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant resend -- START");
		}
		
		int randomPIN = (int)(Math.random()*9000)+1000;
		Integer phoneCode = randomPIN;
		
		merchantModel.setPhoneCode(phoneCode);
		int result = merchantDao.resend(merchantModel);
		
		if(result > 0) {
			
			try{
				// Send Email
				String action="sendOTP";

				String [] retval = spiderEmailSender.fetchTempConfig(action);
				
				String jsonReqName = retval[0];
				String jsonReqPath = retval[1];
				String templateID = retval[2];
				
				String header = "OTP";
				String emailMessageBody = "<p>Hi there!</p><p>Your new OTP is "+phoneCode+". </p> <p>. Thank you for using Lebupay. </p>";
				String subject = messageUtil.getBundle("new.otp.email.subject");
				
				String jsonReqString = getFileString(jsonReqName,jsonReqPath);
				jsonReqString= jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");
				
				jsonReqString= jsonReqString.replace("replace_header_here", header);
				jsonReqString= jsonReqString.replace("replace_emailMessageBody_here", emailMessageBody);
				jsonReqString= jsonReqString.replace("replace_otp_here",""+phoneCode);
				jsonReqString= jsonReqString.replace("replace_subject_here",subject);
				jsonReqString= jsonReqString.replace("replace_to_here", merchantModel.getEmailId());
				jsonReqString= jsonReqString.replace("replace_cc_here", "");
				jsonReqString= jsonReqString.replace("replace_bcc_here", "");
				jsonReqString= jsonReqString.replace("replace_templateID_here", templateID);
				
				spiderEmailSender.sendEmail(jsonReqString,true);
				
				//sendMail.send(merchantModel.getEmailId(), messageBody, subject);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			/**
			 * Send SMS
			 */
			try {
				sendSMS.smsSend(merchantModel.getMobileNo(), "Dear Merchant, Your New OTP is: "+phoneCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant resend -- END");
		}
		
		return result;
	}
	
	
	/**
	 * This method is used to create the Company of the Merchant.
	 * @param companyModel
	 * @return long
	 * @throws Exception
	 */
	public long createCompany(CompanyModel companyModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant createCompany -- START");
		}
		
		merchantValidation.companyInfoValidation(companyModel);
		long result = merchantDao.insertCompany(companyModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("Merchant createCompany -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is use to save merchant ticket details
	 * @param ticketModel
	 * @param userId
	 * @return long
	 * @throws Exception
	 */
	public long saveTicket(TicketModel ticketModel, long userId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant saveTicket -- START");
		}
		
		merchantValidation.saveTicketValidation(ticketModel);
		long result = ticketDao.saveTicket(ticketModel, "Merchant".trim(), userId);
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant saveTicket -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for fetching the Tickets w.r.t Merchant Id. 
	 * @param userId
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchTickets(long userId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTickets -- START");
		}
		
		List<TicketModel> ticketModels = ticketDao.fetchAllTickets("Merchant", userId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTickets -- END");
		}
		
		return ticketModels;
	}
	
	/**
	 * This method is used for Fetching Tickets for the Datatable w.r.t Merchant Id.
	 * @param dataTableModel
	 * @param userId
	 * @throws Exception
	 */
	public void fetchTickets(DataTableModel dataTableModel, long userId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTickets -- START");
		}
		
		List<TicketModel> ticketModels = ticketDao.fetchAllTickets("Merchant", userId, dataTableModel);
		dataTableModel.setData(ticketModels);
		dataTableModel.setRecordsTotal(ticketDao.getTicketsCount("Merchant",userId, dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTickets -- END");
		}
	}
	
	/**
	 * This method is  used for generating Excel for the Tickets Datatable.
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
		
		List<TicketModel> ticketModels = ticketDao.fetchAllTicketsForExport("Merchant", userId, dataTableModel);
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
			objects2[j++] = ticketModels.get(i).getMessage();
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
	 * This method is used to fetch the Company Details w.r.t Merchant Id.
	 * @param merchantId
	 * @return CompanyModel
	 * @throws Exception
	 */
	public CompanyModel fetchCompanyByMerchantId(long merchantId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchCompanyByMerchantId -- START");
		}
		
		CompanyModel companyModel = merchantDao.fetchCompanyByMerchantId(merchantId);
				
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchCompanyByMerchantId -- END");
	   }
		
		return companyModel;
	}
	
	/**
	 * This method is used to Udpate the Company Details.
	 * @param companyModel
	 * @return long
	 * @throws Exception
	 */
	public long updateCompany(CompanyModel companyModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant updateCompany -- START");
		}
		
		merchantValidation.companyInfoValidation(companyModel);
		long result = merchantDao.updateCompany(companyModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("Merchant updateCompany -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to Sending the Email Invoicing Details.
	 * @param emailInvoicingModel
	 * @return int
	 * @throws Exception
	 */
	public int emailInvoicing(EmailInvoicingModel emailInvoicingModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant emailInvoicing -- START");
		}
		
		merchantValidation.createemailInvoicingValidation(emailInvoicingModel);
		
		int result = 0;	
		try{
			// Send Email
			String action="emailInvoicing";

			String [] retval = spiderEmailSender.fetchTempConfig(action);
			
			String jsonReqName = retval[0];
			String jsonReqPath = retval[1];
			String templateID = retval[2];
			
			String header = "Invoice";
			String emailMessageBody = "<p>Dear "+emailInvoicingModel.getFirstName()+" "+emailInvoicingModel.getLastName()+"</p>"
					+ "<p>Your Invoice No is "+emailInvoicingModel.getInvoiceNo()+" for the type "+emailInvoicingModel.getType()+"</p>"
					+ " <p>Your Billing Amount is "+emailInvoicingModel.getBDT()+" "+emailInvoicingModel.getAmount()+"</p>"
					+ " <p>"+emailInvoicingModel.getDescription()+"</p>"
					+ " <p>Payment GateWay Team </p>";
			String subject = emailInvoicingModel.getSubject();
			
			
			String jsonReqString = getFileString(jsonReqName,jsonReqPath);
			jsonReqString= jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");
			
			jsonReqString= jsonReqString.replace("replace_header_here", header);
			jsonReqString= jsonReqString.replace("replace_emailMessageBody_here", emailMessageBody);
			jsonReqString= jsonReqString.replace("replace_firstName_here",""+emailInvoicingModel.getFirstName());
			jsonReqString= jsonReqString.replace("replace_lastName_here",""+emailInvoicingModel.getLastName());
			jsonReqString= jsonReqString.replace("replace_invoiceNo_here", emailInvoicingModel.getInvoiceNo());
			jsonReqString= jsonReqString.replace("replace_type_here", emailInvoicingModel.getType());
			jsonReqString= jsonReqString.replace("replace_BDT_here", emailInvoicingModel.getBDT());
			jsonReqString= jsonReqString.replace("replace_amount_here", emailInvoicingModel.getAmount());
			jsonReqString= jsonReqString.replace("replace_description_here", emailInvoicingModel.getDescription());
			jsonReqString= jsonReqString.replace("replace_subject_here",subject);
			jsonReqString= jsonReqString.replace("replace_to_here", emailInvoicingModel.getEmailId());
			jsonReqString= jsonReqString.replace("replace_cc_here", "");
			jsonReqString= jsonReqString.replace("replace_bcc_here", "");
			jsonReqString= jsonReqString.replace("replace_templateID_here", templateID);
			
			spiderEmailSender.sendEmail(jsonReqString,true);
			
			//sendMail.send(emailInvoicingModel.getEmailId(), messageBody, emailInvoicingModel.getSubject());
			result = 1;
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
			
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant emailInvoicing -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to create the Link for Payment, send it via mail or sms and save in the Order Table.
	 * @param emailInvoicingModel
	 * @param request
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> link(EmailInvoicingModel emailInvoicingModel, HttpServletRequest request) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant link -- START");
		}
		
		merchantValidation.createLinkValidation(emailInvoicingModel);
		
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
		String key = messageUtil.getBundle("secret.key");
		int result = 0;	
		MerchantModel merchantModel = new MerchantModel();
		merchantModel.setMerchantId(emailInvoicingModel.getCreatedBy());
		
		PaymentModel paymentModel = new PaymentModel();
		paymentModel.setAmount(Double.parseDouble(emailInvoicingModel.getAmount()));
		paymentModel.setSuccessURL("None");
		paymentModel.setFailureURL("None");
		paymentModel.setOrderTransactionID("None");
		
		paymentModel.setMerchantModel(merchantModel);
		String customerDetails = "{\"firstName\": \""+emailInvoicingModel.getFirstName()+"\",  \"lastName\": \""+emailInvoicingModel.getLastName()+"\",  \"email\": \""+emailInvoicingModel.getEmailId()+"\",  \"mobileNumber\": \""+emailInvoicingModel.getMobileNumber()+"\"}";
		paymentModel.setCustomerDetails(customerDetails);
		long result1 = transactionDao.insertOrder(paymentModel);
		returnMap.put("result", result1);
		
		if(result1 > 0) {
			
			TransactionModel transactionModel = new TransactionModel();
			
			transactionModel.setCustomer_firstName(emailInvoicingModel.getFirstName());
			transactionModel.setCustomer_lastName(emailInvoicingModel.getLastName());
			transactionModel.setCustomer_email(emailInvoicingModel.getEmailId());
			transactionModel.setMerchantModel(merchantModel);
			transactionModel.setMerchantModel(merchantModel);
			transactionModel.setMerchantModel(merchantModel);
			transactionModel.setAmount(Double.parseDouble(emailInvoicingModel.getAmount()));
			transactionModel.setOrder_id(String.valueOf(result1));
			Long transactionId = transactionDao.insertTransaction(transactionModel);
			returnMap.put("result", transactionId);
			
			if(transactionId > 0) {
				
				String paymentInfo = encryption.encode(key, String.valueOf(transactionId));
				
				String paymentLink = "<a href="+basePath+"link-pay?SESSIONKEY="+URLEncoder.encode(paymentInfo, "UTF-8")+">Click here</a>";
				String baseLink = basePath+"link-pay?SESSIONKEY="+URLEncoder.encode(paymentInfo, "UTF-8");
				
				String baseLinkForBitly = URLEncoder.encode(basePath+"link-pay?SESSIONKEY="+paymentInfo,"UTF-8");
				
				String bitlyLink = "";
				
				try {
					
					String bitlyFullUrl = bitlyBaseUrl+bitlyAccessToken+"&longUrl="+baseLinkForBitly+"&format=txt";
					HTTPConnection.sendGetRequest(bitlyFullUrl);
					bitlyLink = HTTPConnection.readSingleLineRespone();
					
				} catch (IOException IE){
					
					bitlyLink = baseLink;
					IE.printStackTrace();
				}
				
				if(emailInvoicingModel.getPluggers().contains("email")) {
					
					try{
						// Send Email
						String action="payViaLink";

						String [] retval = spiderEmailSender.fetchTempConfig(action);
						
						String jsonReqName = retval[0];
						String jsonReqPath = retval[1];
						String templateID = retval[2];
						
						String header = "Payment Link";
						String emailMessageBody = "<p>Dear "+emailInvoicingModel.getFirstName()+" "+emailInvoicingModel.getLastName()+",</p>"
								+ "<p>Your Payment Link is: "+paymentLink+"</p>"
								+ " <p>"+emailInvoicingModel.getDescription()+"</p>"
								+ " <p>Payment GateWay Team </p>";
						
						String jsonReqString = getFileString(jsonReqName,jsonReqPath);
						jsonReqString= jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");
						
						jsonReqString= jsonReqString.replace("replace_header_here", header);
						jsonReqString= jsonReqString.replace("replace_emailMessageBody_here", emailMessageBody);
						jsonReqString= jsonReqString.replace("replace_firstName_here", emailInvoicingModel.getFirstName());
						jsonReqString= jsonReqString.replace("replace_lastName_here", emailInvoicingModel.getLastName());
						jsonReqString= jsonReqString.replace("replace_paymentLink_here", paymentLink);
						jsonReqString= jsonReqString.replace("replace_description_here", emailInvoicingModel.getDescription());
						jsonReqString= jsonReqString.replace("replace_subject_here", emailInvoicingModel.getSubject());
						jsonReqString= jsonReqString.replace("replace_to_here", emailInvoicingModel.getEmailId());
						jsonReqString= jsonReqString.replace("replace_cc_here", "");
						jsonReqString= jsonReqString.replace("replace_bcc_here", "");
						jsonReqString= jsonReqString.replace("replace_templateID_here", templateID);
						
						spiderEmailSender.sendEmail(jsonReqString,true);
						
						result = 1;
						
						returnMap.put("result", result);
						returnMap.put("baseLink", bitlyLink);
						returnMap.put("plugger", "email");
						
					
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				} 
				
				if(emailInvoicingModel.getPluggers().contains("sms")) {
					
					try {
						// Send SMS
						sendSMS.smsSend(emailInvoicingModel.getMobileNumber(), "Dear "+emailInvoicingModel.getFirstName()+" "+emailInvoicingModel.getLastName()+", Your Payment Link is: "+bitlyLink+" . \nThank you for paying with LebuPay.");
						result = 1;
						
						returnMap.put("result", result);
						returnMap.put("baseLink", bitlyLink);
						returnMap.put("plugger", "sms");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant link -- END");
		}
		
		return returnMap;
	}
	
	/**
	 * This method is used for fetching the Merchant Details w.r.t AccessKey.
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantByAccessKey(String accessKey) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveMerchantByAccessKey -- START");
		}
		
		MerchantModel merchantModel = merchantDao.fetchActiveMerchantByAccessKey(accessKey);
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveMerchantByAccessKey -- END");
	   }
		
		return merchantModel;
	}
	
	
	/**
	 * This method is used for fetching the Merchant Details w.r.t AccessKey.
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchMerchantByAccessKey(String accessKey) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchMerchantByAccessKey -- START");
		}
		
		MerchantModel merchantModel = merchantDao.fetchMerchantByAccessKey(accessKey);
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchMerchantByAccessKey -- END");
	   }
		
		return merchantModel;
	}
	
	@Override
	public List<CardTypePercentageModel> getAllCardPercentageByMerchantId(Long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Get All Card Percentage By MerchantId -- START");
		}
		
		List<CardTypePercentageModel> cardTypePercentageModels = merchantDao.getAllCardPercentageByMerchantId(merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("Get All Card Percentage By MerchantId -- END");
	   }
		
		return cardTypePercentageModels;
	}
	
	public String getFileString(String filename,String path) throws IOException {
		File fl = new File(path+"/"+filename);
		
		String targetFileStr = new String(Files.readAllBytes(Paths.get(fl.getAbsolutePath())));
		return targetFileStr;
	}
	
}