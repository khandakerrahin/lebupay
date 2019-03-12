package com.lebupay.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lebupay.common.Encryption;
import com.lebupay.common.MessageUtil;
import com.lebupay.common.SaltTracker;
import com.lebupay.common.Util;
import com.lebupay.connection.OracleConnection;
import com.lebupay.exception.FormExceptions;
import com.lebupay.model.CheckoutModel;
import com.lebupay.model.CommonModel;
import com.lebupay.model.ContactUsModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.MerchantSessionModel;
import com.lebupay.model.ParameterModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.QuickPayModel;
import com.lebupay.model.Status;
import com.lebupay.model.TransactionModel;
import com.lebupay.service.CheckOutService;
import com.lebupay.service.ContactUsService;
import com.lebupay.service.FaqService;
import com.lebupay.service.MerchantService;
import com.lebupay.service.QuickPayService;
import com.lebupay.service.TransactionService;

import oracle.jdbc.OraclePreparedStatement;
import com.lebupay.daoImpl.BaseDao;
//import com.lebupay.connection.OracleConnection;

/**
 * This class is used to all the methods that are not related to any session and all error pages.
 * @author Java-Team
 *
 */
@Controller
public class IndexController extends BaseDao implements SaltTracker {

	private static Logger logger = Logger.getLogger(IndexController.class);
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private FaqService faqService;
	
	@Autowired
	private ContactUsService contactUsService;
	
	@Autowired
	private CheckOutService checkOutService;
	
	@Autowired
	private QuickPayService quickPayService;
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private Encryption encryption;
	
	@Autowired
	private TransactionService transactionService;
	
	/**
	 * This method is used to open the landing page.
	 * @param request
	 * @param model
	 * @param response
	 * @return landing-page.jsp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model, HttpServletResponse response) throws IOException, ServletException {

		if (logger.isInfoEnabled()) {
			logger.info("index -- START");
		}
		
		HttpSession httpSession = request.getSession(true);
		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpSession.getAttribute("merchantModel");

		if (Objects.nonNull(merchantSessionModel)) {
			return "redirect:/merchant/dashboard";
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("index -- END");
		}
		
		return "landing-page";
	}
	
	/**
	 * This method is used generate token.
	 * @param httpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/get-token", method = RequestMethod.POST)
	public @ResponseBody String getToken(HttpServletRequest httpServletRequest) {
		
		if (logger.isInfoEnabled()) {
			logger.info("getToken -Start");
		}

		String generateToken = "";

		try {

			generateToken = Util.getRandom28();
			
			HttpSession httpSession1 = httpServletRequest.getSession(true);
			
			httpSession1.setAttribute("serverToken", generateToken);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (logger.isInfoEnabled()) {
			logger.info("getToken -End");
		}
		
		return generateToken;

	}
	
	/**
	 * This method is used to open 404 error page.
	 * @param request
	 * @return page-404.jsp
	 */
	@RequestMapping(value = "/error404", method = RequestMethod.GET)
	public String error404(HttpServletRequest request) {

		if (logger.isInfoEnabled()) {
			logger.info("error404 -- START");
		}

		if (logger.isInfoEnabled()) {
			logger.info("error404 -- END");
		}

		return "page-404";
	}

	/**
	 * This method is used to open 500 error page.
	 * @param request
	 * @return page-500.jsp
	 */
	@RequestMapping(value = { "/error500", "/error" }, method = RequestMethod.GET)
	public String error500(HttpServletRequest request) {

		if (logger.isInfoEnabled()) {
			logger.info("error500 -- START");
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("error500 -- END");
		}

		return "page-500";
	}

	/**
	 * This method is used to open 403 error page.
	 * @param request
	 * @return page-403.jsp
	 */
	@RequestMapping(value = "/error403", method = RequestMethod.GET)
	public String error403(HttpServletRequest request) {

		if (logger.isInfoEnabled()) {
			logger.info("error403 -- START");
		}

		if (logger.isInfoEnabled()) {
			logger.info("error403 -- END");
		}

		return "page-403";
	}
	
	/**
	 * This method is used to open FAQ page.
	 * @param httpServletRequest
	 * @param model
	 * @return faq.jsp
	 */
	@RequestMapping(value = "/faq", method = RequestMethod.GET)
	public String faq(HttpServletRequest httpServletRequest, Model model) {
		
		if (logger.isInfoEnabled()) {
			logger.info("faq -- START");
		}

		try {
			
				model.addAttribute("faqModels", faqService.fetchAllActiveFaqsByType("Other"));
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("faq -- END");
		}

		return "faq";
	}
	
	/**
	 * This method is used to add Contact.
	 * @param contactUsModel
	 * @param httpServletRequest
	 * @return CommonModel
	 */
	@RequestMapping(value = "/add-contactus", method = RequestMethod.POST)
	public @ResponseBody CommonModel addContactUs(@RequestBody ContactUsModel contactUsModel, 
			HttpServletRequest httpServletRequest) {
		
		if (logger.isInfoEnabled()) {
			logger.info("addContactUs -- START");
		}
		
		CommonModel commonModel = new CommonModel();
		
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		
		try {
			
			salt = contactUsModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
				
				long result = contactUsService.saveContactUs(contactUsModel, httpServletRequest);
				if(result > 0){
					
					commonModel.setStatus(Status.ACTIVE);
					commonModel.setMessage(messageUtil.getBundle("common.successfully"));
				} else {
					throw new Exception();
				}
				
			} else {
				
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}
			
		} catch (FormExceptions e1) {

			commonModel.setStatus(Status.INACTIVE);
			List<String> exe = new ArrayList<String>();
			for (String e : e1.getExe()) {
				exe.add(e);
			}
			commonModel.setMultipleMessage(exe);
			commonModel.setMessage(messageUtil.getBundle("common.not.successfully"));

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {
			e.printStackTrace();

			commonModel.setStatus(Status.INACTIVE);
			commonModel.setMessage(messageUtil.getBundle("common.error"));
		} finally {
			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			commonModel.setCsrfPreventionSalt(newSalt);
			activeSalts.add(newSalt);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("addContactUs -- END");
		}
		
		return commonModel;
	}
	
	/**
	 * This method is used for QuickPay CheckOut created from Merchant.
	 * @param httpServletRequest
	 * @param model
	 * @param keys
	 * @return String
	 */
	@RequestMapping(value = "/quickpay-checkout", method = RequestMethod.POST)
	public String getQuickpaydetails(HttpServletRequest httpServletRequest, Model model,@RequestParam String keys) {

		if (logger.isInfoEnabled()) {
			logger.info("getQuickpaydetails -- START");
		}
		
		try {
			
			MerchantModel merchantModel = new MerchantModel();
			QuickPayModel quickPayModel = quickPayService.fetchQuickPayByKeys(keys);
			if(Objects.isNull(quickPayModel))
				throw new Exception();
			else {
				
				try {
					
					merchantModel = merchantService.fetchActiveMerchantById(quickPayModel.getMerchantModel().getMerchantId());
					if(Objects.isNull(merchantModel))
						throw new Exception();
					
					try {
						
						CheckoutModel checkoutModel = checkOutService.fetchCheckoutById(merchantModel.getMerchantId());
						model.addAttribute("checkoutModel",checkoutModel);
						
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("Error in fetchCheckoutById");
					}
					
					try {
						List<ParameterModel> parameterModels = checkOutService.fetchParametersById(merchantModel.getMerchantId());
						model.addAttribute("parameterModels",parameterModels);
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("Error in fetchParametersById");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Error in fetchActiveMerchantById");
				}
			}
			
			PaymentModel paymentModel = new PaymentModel();
			paymentModel.setAccessKey(merchantModel.getAccessKey());
			paymentModel.setAmount(Double.parseDouble(quickPayModel.getOrderAmount()));
			paymentModel.setSuccessURL("None");
			paymentModel.setFailureURL("None");
			paymentModel.setOrderTransactionID("None");
			
			paymentModel.setToken(keys);
			long orderId = transactionService.insertOrder(paymentModel);
			if(orderId > 0) {
				
				TransactionModel transactionModel = new TransactionModel();
				transactionModel.setMerchantModel(merchantModel);
				transactionModel.setAmount(paymentModel.getAmount());
				transactionModel.setOrder_id(String.valueOf(orderId));
				Long transactionId = transactionService.insertTransaction(transactionModel);
				if(transactionId > 0) {
					
					String key = messageUtil.getBundle("secret.key");
					model.addAttribute("SESSIONKEY", encryption.encode(key, String.valueOf(transactionId)));
					model.addAttribute("paymentModel", paymentModel);
				
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in fetchQuickPayByKeys");
		}

		if (logger.isInfoEnabled()) {
			logger.info("getQuickpaydetails -- END");
		}

		return "quickpay-checkout";
	}
	
	/**
	 * This method is used for Link Pay Created from Merchant Dashboard.
	 * @param httpServletRequest
	 * @param model
	 * @param SESSIONKEY
	 * @return merchant-link-pay.jsp
	 */
	@RequestMapping(value = "/link-pay", method = RequestMethod.GET)
	public String linkPayPage(HttpServletRequest httpServletRequest, 
			Model model, @RequestParam String SESSIONKEY) {

		if (logger.isInfoEnabled()) {
			logger.info("linkPayPage -- START");
		}
		
		try {
			
			String key = messageUtil.getBundle("secret.key"); 
			Long transactionId = Long.parseLong(encryption.decode(key, SESSIONKEY));
			
			TransactionModel transactionModel = transactionService.fetchTransactionById(transactionId);
			if(Objects.isNull(transactionModel))
				model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			String transactionId1 =URLEncoder.encode(SESSIONKEY, "UTF-8");
			
			model.addAttribute("merchantName", transactionModel.getMerchantModel().getFirstName()+ " "+transactionModel.getMerchantModel().getLastName());
			model.addAttribute("amount", transactionModel.getAmount());
			model.addAttribute("grossAmount", transactionModel.getGrossAmount());
			model.addAttribute("transactionId1", transactionId1); //encoded
			model.addAttribute("transactionId", transactionModel.getTxnId());
			
			try {
				//logwrite.writeLog(0L,"Citybank approveOrder",2, "approveOrder respCity txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
				writeLogV2(0L,"IndexController/link-pay",1, "initial txnId:"+transactionId+",SESSIONKEY:"+SESSIONKEY+",transactionId1: "+transactionId1+",transactionId:"+transactionId);
				
		    } catch (Exception e1) {
				e1.printStackTrace();
			}
			
			//TODO Removed by 
			/*
			model.addAttribute("citybankMerchantId", transactionModel.getMerchantModel().getCityMerchantId());
			
			model.addAttribute("eBLUserName", transactionModel.getMerchantModel().getEblUserName());
			model.addAttribute("eBLUserPassword", transactionModel.getMerchantModel().getEblPassword());
			model.addAttribute("eBLUserId", transactionModel.getMerchantModel().getEblId());
			//WASIF 20181115			
			model.addAttribute("sEBLUserName", transactionModel.getMerchantModel().getSeblUserName());
			model.addAttribute("sEBLUserPassword", transactionModel.getMerchantModel().getSeblPassword());
			model.addAttribute("sEBLUserId", transactionModel.getMerchantModel().getSeblId());/**/
			
			if(transactionModel.getMerchantModel().getCityMerchantId() != null && !transactionModel.getMerchantModel().getCityMerchantId().isEmpty() ) {
				model.addAttribute("CITY", "Y");
			}else {
				model.addAttribute("CITY", "N");
			}
			if(transactionModel.getMerchantModel().getEblUserName() !=null && !transactionModel.getMerchantModel().getEblUserName().isEmpty()) {
				model.addAttribute("EBL", "Y");
			}else {
				model.addAttribute("EBL", "N");
			}
			if(transactionModel.getMerchantModel().getSeblUserName() !=null && !transactionModel.getMerchantModel().getSeblUserName().isEmpty()) {
				model.addAttribute("SEBL", "Y");
			}else {
				model.addAttribute("SEBL", "N");
			}
			
			
			TransactionModel transactionModel1 = transactionService.fetchTransactionByTXNId(transactionModel.getTxnId());
			
			if((transactionModel1.getPaymentModel().getFailureURL()).equals("None")) {
				model.addAttribute("returnUrl", "index");
			} else {
				model.addAttribute("returnUrl", transactionModel1.getPaymentModel().getFailureURL());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("linkPayPage -- END");
		}

		return "merchant-link-pay";
	}
	
	//TODO remove
		//TODO remove below block it is for testing only
		public void writeLogV2(Long merchant_id,String action,int action_type,String log) throws Exception {

			
			System.out.println("logwriter.writeLog -- START");
			System.out.println("logwriter.writeLog -log ===>"+log);
			if(log.length()>999)
				log=log.substring(0,999);

		//OracleConnection oracleConnection = new OracleConnection();
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into APPLICATION_LOG (merchant_id,action,action_type,log) values("
					+ ":merchant_id,:action,:action_type,:log)";

			String pk[] = {"ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);

			pst.setLongAtName("merchant_id", merchant_id); 
			pst.setStringAtName("action", action); 
			pst.setIntAtName("action_type", action_type); 
			pst.setStringAtName("log", log); 
			

			//System.out.println("logwriter.writeLog==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
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
				}
			} catch (Exception e) {
				System.out.println("Connection not closed for logwriter.writeLog ");
				/*
				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				}/**/

			}
		}
	/*
		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- END");
		}/**/


	}
}