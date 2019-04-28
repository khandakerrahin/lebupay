/**
 * This package is used to contain all the controller files.
 */
package com.lebupay.controller;

import java.io.BufferedReader;//
import java.io.DataOutputStream;//
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;//
import java.io.StringReader;
import java.net.HttpURLConnection;//
import java.net.URL;//
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import com.itextpdf.xmp.impl.Base64;//

import java.util.ArrayList;//
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;//
import java.util.Map;//
import java.util.Map.Entry; //
import java.util.Objects;
import java.util.Set; //
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.Gson; //
import com.google.gson.JsonObject; //TODO added By Wasif 20190331
import com.google.gson.reflect.TypeToken;
import com.lebupay.common.CityBankUtil;
import com.lebupay.common.Encryption;
import com.lebupay.common.HTTPConnection;
import com.lebupay.common.MessageUtil;
import com.lebupay.common.SaltTracker;
import com.lebupay.common.SendMail;
import com.lebupay.common.SpiderEmailSender;
import com.lebupay.common.SendSMS;
import com.lebupay.common.Util;
import com.lebupay.daoImpl.BaseDao;
import com.lebupay.exception.EmptyValueException;
import com.lebupay.exception.FormExceptions;
import com.lebupay.exception.NumericException;
import com.lebupay.model.BKashModel;
import com.lebupay.model.CheckoutModel;
import com.lebupay.model.CityBankTransactionModel;
import com.lebupay.model.CompanyModel;
import com.lebupay.model.EmailInvoicingModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.ParameterModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.TransactionModel;
import com.lebupay.model.CardIssuerModel; //TODO added By Wasif 20190318
import com.lebupay.service.CheckOutService;
import com.lebupay.service.MerchantService;
import com.lebupay.service.TransactionService;
import com.lebupay.serviceImpl.TransactionServiceImpl;
import com.lebupay.logwriter.Logwriter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;/**/

import oracle.jdbc.OraclePreparedStatement;

/**
 * This is Payment Controller that will handle all the payment related work like
 * CityBank,EBL, BKASH etc.
 * 
 * @author Java-Team
 *
 */
@Controller
public class PaymentController extends BaseDao implements SaltTracker {

	private static Logger logger = Logger.getLogger(PaymentController.class);
	// TODO needs attention below
	// private static Logwriter logwrite ; //WASIF 20190224

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionServiceImpl transactionServiceImpl;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private Encryption encryption;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private CheckOutService checkOutService;

	@Autowired
	private SendMail sendMail;

	@Autowired
	private SpiderEmailSender spiderEmailSender;

	@Autowired
	private SendSMS sendSMS;

	/**
	 * This method is used to Open CityBank Payment Page.
	 * 
	 * @param request
	 * @param model
	 * @param transactionId
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/paycitybank", method = RequestMethod.GET)
	public String payCityBank(HttpServletRequest request, Model model, @RequestParam String transactionId,
			HttpServletResponse response, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Payment -- START");
		}

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		Map<String, String> argsMap = new HashMap<String, String>();
		Long transactionId1 = 0L;
		TransactionModel transactionModel2 = null;
		try {

			System.out.println("SESSIONKEY Before Decode== >> " + transactionId);
			String key = messageUtil.getBundle("secret.key");
			transactionId1 = Long.parseLong(encryption.decode(key, transactionId));
			System.out.println("TransactionId After Decode== >> " + transactionId1);

			try {

				/// transactionService..writeLog(0L,"Start
				/// Citybank",1,"trxId:"+transactionId+",paycitybank RequestMethod.GET");
				writeLogV2(0L, "Start Citybank", 1, "trxId:" + transactionId1 + ",paycitybank RequestMethod.GET");
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}

			transactionModel2 = transactionService.fetchTransactionById(transactionId1);

		} catch (Exception e) {
			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;
		}

		if (Objects.isNull(transactionModel2)) {
			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;

		} else {
			if (transactionModel2.getTransactionStatus() == 0 || transactionModel2.getTransactionStatus() == 1
					|| transactionModel2.getTransactionStatus() == 2) { // Already Paid
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("already.paid"));
				return "redirect:/payment_failure";

			} else if (transactionModel2.getTransactionStatus() == 7) { // expired

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();

			}
			if (transactionModel2.getIsValid()) { // valid, do nothing
			} else {
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();

			}
		}
		transactionModelSaltTracker.setTxnId(transactionModel2.getTxnId());

		String data = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
		data += "<TKKPG>";
		data += "<Request>";
		data += "<Operation>CreateOrder</Operation>";
		data += "<Language>EN</Language>";
		data += "<Order>";
		data += "<OrderType>Purchase</OrderType>";
		data += "<Merchant>" + transactionModel2.getMerchantModel().getCityMerchantId() + "</Merchant>";
		data += "<Amount>" + transactionModel2.getAmount() * 100 + "</Amount>";
		data += "<Currency>" + "050" + "</Currency>";
		data += "<Description>" + transactionModel2.getTxnId() + "</Description>";
		data += "<ApproveURL>" + basePath + "approveOrder" + "</ApproveURL>";
		data += "<CancelURL>" + basePath + "cancelOrder" + "</CancelURL>";
		data += "<DeclineURL>" + basePath + "declineOrder" + "</DeclineURL>";
		data += "</Order></Request></TKKPG>";

		// remove after debugging
		/*
		 * if (logger.isInfoEnabled()) {
		 * logger.info("City Bank request data --> "+data); }/
		 **/

		try {
			// logwrite.writeLog(0L,"Citybank Get REQ",1,"data:"+data);
			writeLogV2(0L, "Citybank CreateOrder Get", 1, "trxID:" + transactionModel2.getTxnId() + ",data:" + data);
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		String Response = null;
		try {

			Response = CityBankUtil.PostQW(data);
			try {
				// logwrite.writeLog(0L,"Citybank Get resp",2,"Response:"+Response);
				writeLogV2(0L, "Citybank CreateOrder Get", 2,
						"trxID:" + transactionModel2.getTxnId() + ",Response:" + Response);
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		// For getting only the responsed XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document xmlDoc = null;
		try {
			xmlDoc = builder.parse(new InputSource(new StringReader(Response.substring(Response.indexOf("<TKKPG>")))));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// For getting the required values
		xmlDoc.getDocumentElement().normalize();

		// remove after debugging
		/*
		 * if (logger.isInfoEnabled()) {
		 * logger.info("City Bank Payment xmlDoc response -->"+xmlDoc); }/
		 **/

		NodeList nList = xmlDoc.getElementsByTagName("Order");
		CityBankUtil.OrderID = nList.item(0).getChildNodes().item(0).getTextContent();

		CityBankUtil.SessionID = nList.item(0).getChildNodes().item(1).getTextContent();
		String URL = nList.item(0).getChildNodes().item(2).getTextContent();

		// Call for Payment Page
		try {
			response.sendRedirect(URL + "?ORDERID=" + CityBankUtil.OrderID + "&SESSIONID=" + CityBankUtil.SessionID);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This method is Used to Payment of CityBank. Create order
	 * 
	 * @param request
	 * @param model
	 * @param transactionId
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/paycitybank", method = RequestMethod.POST)
	public String payCityBankPost(HttpServletRequest request, Model model, @RequestParam String transactionId,
			HttpServletResponse response, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Payment -- START");
		}

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		Map<String, String> argsMap = new HashMap<String, String>();
		Long transactionId1 = 0L;
		TransactionModel transactionModel2 = null;
		try {

			String key = messageUtil.getBundle("secret.key");
			transactionId1 = Long.parseLong(encryption.decode(key, transactionId));

			transactionModel2 = transactionService.fetchTransactionById(transactionId1);

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;
		}

		if (Objects.isNull(transactionModel2)) {

			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;

		} else {

			if (transactionModel2.getTransactionStatus() == 0 || transactionModel2.getTransactionStatus() == 1
					|| transactionModel2.getTransactionStatus() == 2) { // Already Paid
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("already.paid"));
				return "redirect:/payment_failure?orderId=" + transactionModel2.getTxnId();

			} else if (transactionModel2.getTransactionStatus() == 3) { // Failed

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				return "redirect:/payment_failure?orderId=" + transactionModel2.getTxnId();

			}
		}
		transactionModelSaltTracker.setTxnId(transactionModel2.getTxnId());

		String data = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
		data += "<TKKPG>";
		data += "<Request>";
		data += "<Operation>CreateOrder</Operation>";
		data += "<Language>EN</Language>";
		data += "<Order>";
		data += "<OrderType>Purchase</OrderType>";
		data += "<Merchant>" + transactionModel2.getMerchantModel().getCityMerchantId() + "</Merchant>";
		data += "<Amount>" + transactionModel2.getAmount() * 100 + "</Amount>";
		data += "<Currency>" + "050" + "</Currency>";
		data += "<Description>" + transactionModel2.getTxnId() + "</Description>"; // TODO add 26-3-trx ID 26=amex
																					// 3=tenure(can be changed) and
																					// trxid
		data += "<ApproveURL>" + basePath + "approveOrder" + "</ApproveURL>";
		data += "<CancelURL>" + basePath + "cancelOrder" + "</CancelURL>";
		data += "<DeclineURL>" + basePath + "declineOrder" + "</DeclineURL>";
		data += "</Order></Request></TKKPG>";

		try {
			// logwrite.writeLog(0L,"Citybank paycity
			// POST",1,"trxID:"+transactionModel2.getTxnId()+", data:"+data);
			writeLogV2(0L, "Citybank paycity POST", 1, "trxID:" + transactionModel2.getTxnId() + ", data:" + data);
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		String Response = null;
		try {
			Response = CityBankUtil.PostQW(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		try {
			// logwrite.writeLog(0L,"Citybank paycity
			// POST",2,"trxID:"+transactionModel2.getTxnId()+", Response:"+Response);
			writeLogV2(0L, "Citybank paycity POST", 2,
					"trxID:" + transactionModel2.getTxnId() + ", Response:" + Response);

		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		// For getting only the responsed XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document xmlDoc = null;
		try {
			xmlDoc = builder.parse(new InputSource(new StringReader(Response.substring(Response.indexOf("<TKKPG>")))));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// remove after debugging
		/*
		 * if (logger.isInfoEnabled()) {
		 * logger.info("City Bank Payment xmlDoc response -->"+xmlDoc);
		 * System.out.println("City Bank Payment xmlDoc response print -->"+xmlDoc); }/
		 **/

		// For getting the required values
		xmlDoc.getDocumentElement().normalize();
		NodeList nList = xmlDoc.getElementsByTagName("Order");
		CityBankUtil.OrderID = nList.item(0).getChildNodes().item(0).getTextContent();
		CityBankUtil.SessionID = nList.item(0).getChildNodes().item(1).getTextContent();
		String URL = nList.item(0).getChildNodes().item(2).getTextContent();

		// Call for Payment Page
		try {
			response.sendRedirect(URL + "?ORDERID=" + CityBankUtil.OrderID + "&SESSIONID=" + CityBankUtil.SessionID);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This Method is used to Approve CityBank Orders.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/approveOrder", method = RequestMethod.POST)
	public String approvedCityBankOrder(HttpServletRequest request, Model model, HttpServletResponse response,
			final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Approve Order -- START");
		}
		/*
		 * try { //logwrite.writeLog(0L,"Citybank approveOrder",1,
		 * "approveOrder POST start"); writeLogV2(0L,"Citybank approveOrder",1,
		 * "approveOrder POST start"); } catch (Exception e1) { // Auto-generated catch
		 * block e1.printStackTrace(); }/
		 **/

		Document xmlDoc;

		String returnURL = "";
		String cityOrderID = "";
		String transactionType = "";
		String currency = "";
		String purchaseAmount = "";
		String responseCode = "";
		String responseDescription = "";
		String orderStatus = "";
		String approvalCode = "";
		String pan = "";
		String transDateTime = "";
		String brand = "";
		String acqFee = "";
		String merchantTransId = "";
		String orderDescription = "";
		String approvalCodeScr = "";
		String purchaseAmountScr = "";
		String currencyScr = "";
		String orderStatusScr = "";
		String threeDSVerificaion = "";
		String threeDSStatus = "";
		String cardHolderName = "";
		String name = "";
		String merchantId = "";

		if (request.getParameter("xmlmsg") != "") {

			try {

				xmlDoc = CityBankUtil.RequestHandler(request);

				InputSource source = new InputSource(new StringReader(Util.convertDocumentToString(xmlDoc)));
				XPath xpath = XPathFactory.newInstance().newXPath();

				Object citiResponse = xpath.evaluate("/Message", source, XPathConstants.NODE);

				if (logger.isInfoEnabled()) {
					logger.info("City bank full response" + citiResponse.toString());
					logger.info("City bank full source response" + source.toString());
				}

				cityOrderID = xpath.evaluate("OrderID", citiResponse);
				transactionType = xpath.evaluate("TransactionType", citiResponse);
				pan = xpath.evaluate("PAN", citiResponse);
				purchaseAmount = xpath.evaluate("PurchaseAmount", citiResponse);
				currency = xpath.evaluate("Currency", citiResponse);
				transDateTime = xpath.evaluate("TranDateTime", citiResponse);
				responseCode = xpath.evaluate("ResponseCode", citiResponse);
				responseDescription = xpath.evaluate("ResponseDescription", citiResponse);
				cardHolderName = xpath.evaluate("CardHolderName", citiResponse);
				brand = xpath.evaluate("Brand", citiResponse);
				orderStatus = xpath.evaluate("OrderStatus", citiResponse);
				approvalCode = xpath.evaluate("ApprovalCode", citiResponse);
				acqFee = xpath.evaluate("AcqFee", citiResponse);
				merchantTransId = xpath.evaluate("MerchantTranID", citiResponse);
				orderDescription = xpath.evaluate("OrderDescription", citiResponse);
				approvalCodeScr = xpath.evaluate("ApprovalCodeScr", citiResponse);
				purchaseAmountScr = xpath.evaluate("PurchaseAmountScr", citiResponse);
				currencyScr = xpath.evaluate("CurrencyScr", citiResponse);
				orderStatusScr = xpath.evaluate("OrderStatusScr", citiResponse);
				name = xpath.evaluate("Name", citiResponse);
				threeDSVerificaion = xpath.evaluate("ThreeDSVerificaion", citiResponse);
				threeDSStatus = xpath.evaluate("ThreeDSStatus", citiResponse);
				// issue trxid takes from global value
				// String txnId = String.valueOf(transactionModelSaltTracker.getTxnId());
				String txnId = orderDescription; // update by Wasif 20190223

				try {
					// logwrite.writeLog(0L,"Citybank approveOrder",2, "approveOrder respCity
					// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
					writeLogV2(0L, "Citybank approveOrder", 2, "approveOrder respCity txnId:" + txnId
							+ ",purchaseAmount:" + purchaseAmount + ",merchantTransId:" + merchantTransId);

				} catch (Exception e1) {
					// Auto-generated catch block
					e1.printStackTrace();
				}

				// TODO
				/*
				 * if(pan.contains("530772")) { int trx_status=6; // fdsgdf }/
				 **/

				TransactionModel transactionModel = null;

				try {

					transactionModel = transactionService.fetchTransactionByTXNId(txnId);
					if (Objects.isNull(transactionModel)) {
						throw new Exception();
					}

				} catch (Exception e) {
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
					returnURL = "redirect:/payment_failure?orderId=" + txnId;
				}

				Long transactionId = transactionModel.getTransactionId();

				transactionModel.setBank("CityBank");
				transactionModel.setTransaction_type(transactionType);
				transactionModel.setTotalCapturedAmount(purchaseAmount);
				transactionModel.setTransaction_currency("BDT");
				transactionModel.setAuthorizationResponse_date(transDateTime);
				transactionModel.setResponseCode(responseCode);
				transactionModel.setAcquirerMessage(responseDescription);
				transactionModel.setCard_brand(brand);
				transactionModel.setOrder_status(orderStatus);
				transactionModel.setResponse_gatewayCode(approvalCode);
				transactionModel.setDescription(cityOrderID);
				transactionModel.setNameOnCard(cardHolderName);
				transactionModel.setCustomer_firstName(name);
				// transactionModel.setDevice_ipAddress(device_ipAddress);
				transactionModel.setProvided_card_number(pan);
				// Set the status
				/*
				 * if(false) { transactionModel.setTransactionStatus(6); }/
				 **/
				// transactionModel.setMerchantId(transactionModel2.getMerchantModel().getCityMerchantId());
				// city bank mid add
				int result = transactionServiceImpl.updateTransactionAfterCityPayment(transactionModel);

				if (result > 0) {

					CityBankTransactionModel cityTransactionModel = new CityBankTransactionModel();
					cityTransactionModel.setCitybankOrderId(cityOrderID);
					cityTransactionModel.setTransactionType(transactionType);
					cityTransactionModel.setPan(pan);
					cityTransactionModel.setAmount(Double.parseDouble(purchaseAmount) / 100);
					cityTransactionModel.setCurrency("BDT");
					cityTransactionModel.setCurrencyCode(currency);
					cityTransactionModel.setTranDateTime(transDateTime);
					cityTransactionModel.setResponseCode(responseCode);
					cityTransactionModel.setResponseDescription(responseDescription);
					cityTransactionModel.setBrand(brand);
					cityTransactionModel.setOrderStatus(orderStatus);
					cityTransactionModel.setApprovalCode(approvalCode);
					cityTransactionModel.setAcqFee(acqFee);
					cityTransactionModel.setMerchantTransactionId(merchantTransId);
					cityTransactionModel.setOrderDescription(orderDescription);
					cityTransactionModel.setApprovalCodeScr(approvalCodeScr);
					cityTransactionModel.setPurchaseAmountScr(purchaseAmountScr);
					cityTransactionModel.setCurrencyScr(currencyScr);
					cityTransactionModel.setOrderStatusScr(orderStatusScr);
					cityTransactionModel.setThreeDsVerificaion(threeDSVerificaion);
					cityTransactionModel.setThreeDsStatus(threeDSStatus);
					cityTransactionModel.setTransactionMasterId(String.valueOf(transactionId));
					cityTransactionModel.setCreatedBy(transactionModel.getMerchantModel().getMerchantId());

					try {
						// logwrite.writeLog(transactionModel.getMerchantModel().getMerchantId(),"Citybank
						// approveOrder",1, "approveOrder cityTrxModelUpd
						// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
						writeLogV2(transactionModel.getMerchantModel().getMerchantId(), "Citybank approveOrder", 1,
								"CityTrxModelUpd txnId:" + txnId + ",purchaseAmount:" + purchaseAmount
										+ ",merchantTransId:" + merchantTransId);
					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}

					int cityResult = transactionServiceImpl.saveCityBankTransaction(cityTransactionModel);

					if (cityResult > 0) {
						// TODO need to check here
						returnURL = "redirect:/payment_success?orderId=" + txnId;

						if (Objects.nonNull(transactionModel)) {

							PaymentModel paymentModel = new PaymentModel();
							Gson gson = new Gson();
							// TODO Added by Wasif 20190312
							paymentModel = transactionModel.getPaymentModel();

							String sms_notifictaion = paymentModel.getNotification_sms();
							String email_notification = paymentModel.getNotification_email();
							String customerDetails = paymentModel.getCustomerDetails();
							String firstName = decode(customerDetails, "firstName");
							String lastName = decode(customerDetails, "lastName");
							paymentModel = gson.fromJson(paymentModel.getCustomerDetails(), PaymentModel.class);// Wasif
																												// 20190312
							// JSON to Java object, read it from a Json String.
							/*
							 * paymentModel =
							 * gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
							 * PaymentModel.class);/
							 **/

							if (Objects.nonNull(paymentModel.getEmailId()) || !paymentModel.getEmailId().equals("")) {
								if (email_notification.equals("1")) {
									try {
										// Send Email
										String action = "paymentSuccess";
										String header = "Payment successful";
										String subject = messageUtil.getBundle("transaction.email.subject");
										String emailMessageBody = "<p>Hi there!</p><p>We have successfully received "
												+ transactionModel.getGrossAmount() + " BDT. </p> <p>Lebupay Team </p>";

										EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
										emailInvoicingModel.setAction(action);
										emailInvoicingModel.setHeader(header);
										emailInvoicingModel.setAmount("" + transactionModel.getGrossAmount());
										emailInvoicingModel.setEmailMessageBody(emailMessageBody);
										emailInvoicingModel.setSubject(subject);
										emailInvoicingModel.setEmailId(paymentModel.getEmailId());
										emailInvoicingModel.setCardType(brand);
										emailInvoicingModel.setNameOnCard(transactionModel.getNameOnCard());
										emailInvoicingModel.setFirstName(firstName);
										emailInvoicingModel.setLastName(lastName);
										emailInvoicingModel.setMobileNumber(paymentModel.getMobileNumber());
										emailInvoicingModel.setCardNumber(pan);
										emailInvoicingModel.setBankTrxID(cityOrderID);
										emailInvoicingModel.setTrxID(orderDescription);
										emailInvoicingModel.setTransactionType(transactionType);
										//emailInvoicingModel.setIPAddress(ipAddress);
										emailInvoicingModel.setCurrency(cityTransactionModel.getCurrency());
										emailInvoicingModel.setIsTemplate(true);

										spiderEmailSender.sendEmail(emailInvoicingModel);

										// sendMail.send(paymentModel.getEmailId(), emailMessageBody, subject);

									} catch (Exception e) {

										e.printStackTrace();
									}
								}
							}
							if (sms_notifictaion.equals("1")) { // SENDs sms if sms is enabled
								if (Objects.nonNull(paymentModel.getMobileNumber())
										|| !paymentModel.getMobileNumber().equals("")) {
									// TODO add option to send SMS only if amount more than 100
									try {
										/*
										 * sendSMS.smsSend(paymentModel.getMobileNumber(),
										 * "Thank you for the payment. We have received " +
										 * transactionModel.getGrossAmount() + " BDT.");/
										 **/

										sendSMS.smsSend(paymentModel.getMobileNumber(), "We have received "
												+ transactionModel.getGrossAmount()
												+ " BDT. Thank You for paying with LEBUPAY. Visit www.we-top-up.com for mobile recharge.");
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}
						}
					} else {
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("something.went.wrong"));
						returnURL = "redirect:/payment_failure?orderId=" + txnId;
					}

				} else {
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("something.went.wrong"));
					returnURL = "redirect:/payment_failure?orderId=" + txnId;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("XML Passing Exception = " + e);
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Approve Order -- END");
		}

		return returnURL;
	}

	/**
	 * This method is used for Cancel Order of CityBank Payment.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
	public String cancelledCityBankOrder(HttpServletRequest request, Model model, HttpServletResponse response,
			final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Cancel Order -- START");
		}

		Document xmlDoc;

		String returnURL = "";
		String cityOrderID = "";
		String transactionType = "";
		String currency = "";
		String purchaseAmount = "";
		String responseCode = "";
		String responseDescription = "";
		String orderStatus = "";
		String approvalCode = "";
		String pan = "";
		String transDateTime = "";
		String brand = "";
		String acqFee = "";
		String merchantTransId = "";
		String orderDescription = "";
		String approvalCodeScr = "";
		String purchaseAmountScr = "";
		String currencyScr = "";
		String orderStatusScr = "";
		String threeDSVerificaion = "";
		String threeDSStatus = "";
		String OrderStatus = "";
		String cardHolderName = "";
		String name = "";

		// String txnId = String.valueOf(transactionModelSaltTracker.getTxnId());

		if (request.getParameter("xmlmsg") != "") {

			try {

				xmlDoc = CityBankUtil.RequestHandler(request);

				InputSource source = new InputSource(new StringReader(Util.convertDocumentToString(xmlDoc)));
				XPath xpath = XPathFactory.newInstance().newXPath();

				Object citiResponse = xpath.evaluate("/Message", source, XPathConstants.NODE);

				cityOrderID = xpath.evaluate("OrderID", citiResponse);
				transactionType = xpath.evaluate("TransactionType", citiResponse);
				pan = xpath.evaluate("PAN", citiResponse);
				purchaseAmount = xpath.evaluate("PurchaseAmount", citiResponse);
				currency = xpath.evaluate("Currency", citiResponse);
				transDateTime = xpath.evaluate("TranDateTime", citiResponse);
				responseCode = xpath.evaluate("ResponseCode", citiResponse);
				responseDescription = xpath.evaluate("ResponseDescription", citiResponse);
				cardHolderName = xpath.evaluate("CardHolderName", citiResponse);
				brand = xpath.evaluate("Brand", citiResponse);
				orderStatus = xpath.evaluate("OrderStatus", citiResponse);
				approvalCode = xpath.evaluate("ApprovalCode", citiResponse);
				acqFee = xpath.evaluate("AcqFee", citiResponse);
				merchantTransId = "";
				orderDescription = xpath.evaluate("OrderDescription", citiResponse);
				approvalCodeScr = xpath.evaluate("ApprovalCodeScr", citiResponse);
				purchaseAmountScr = xpath.evaluate("PurchaseAmountScr", citiResponse);
				currencyScr = xpath.evaluate("CurrencyScr", citiResponse);
				orderStatusScr = xpath.evaluate("OrderStatusScr", citiResponse);
				name = "";
				threeDSVerificaion = "";
				threeDSStatus = "";
				String txnId = orderDescription; // TODO order description

				if (orderStatus.equals("CANCELED")) {

					TransactionModel transactionModel = null;
					try {

						transactionModel = transactionService.fetchTransactionByTXNId(txnId);
						if (Objects.isNull(transactionModel)) {
							throw new Exception();
						}

					} catch (Exception e) {
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
						returnURL = "redirect:/payment_failure?orderId=" + txnId;
					}

					transactionModel.setBank("CityBank");
					transactionModel.setTransaction_type(transactionType);
					transactionModel.setTotalCapturedAmount(purchaseAmount);
					transactionModel.setTransaction_currency("BDT");
					transactionModel.setAuthorizationResponse_date(transDateTime);
					transactionModel.setResponseCode(responseCode);
					transactionModel.setAcquirerMessage(responseDescription);
					transactionModel.setCard_brand(brand);
					transactionModel.setOrder_status(orderStatus);
					transactionModel.setResponse_gatewayCode(approvalCode);
					transactionModel.setDescription(cityOrderID);
					transactionModel.setNameOnCard(cardHolderName);
					transactionModel.setCustomer_firstName(name);

					int result = transactionServiceImpl.updateCalcelledTransactionAfterCityPayment(transactionModel, 3);

					try {
						// logwrite.writeLog(0L,"Citybank approveOrder",2, "approveOrder respCity
						// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
						writeLogV2(0L, "Citybank cancelOrder", 2, "cancelOrder respCity txnId:" + txnId
								+ ",purchaseAmount:" + purchaseAmount + ",pan:" + pan);

					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}

					if (result > 0) {

						returnURL = "redirect:/payment_failure?orderId=" + txnId;

						if (Objects.nonNull(transactionModel)) {
							PaymentModel paymentModel = new PaymentModel();

							Gson gson = new Gson();
							// WASIF 20190312
							paymentModel = transactionModel.getPaymentModel();

							String sms_notifictaion = paymentModel.getNotification_sms();
							String email_notification = paymentModel.getNotification_email();
							paymentModel = gson.fromJson(paymentModel.getCustomerDetails(), PaymentModel.class);// Wasif
																												// 20190312
							// JSON to Java object, read it from a Json String.
							/*
							 * paymentModel =
							 * gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
							 * PaymentModel.class);/
							 **/

							// JSON to Java object, read it from a Json String.
							/*
							 * paymentModel =
							 * gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
							 * PaymentModel.class);/
							 **/

							if (Objects.nonNull(paymentModel.getEmailId()) || !paymentModel.getEmailId().equals("")) {
								if (email_notification.equals("1")) {
									try {
										// Send Email
										String action = "paymentCancelled";
										String header = "Payment cancelled";
										String subject = messageUtil.getBundle("transaction.email.subject.cancelled");
										String emailMessageBody = "<p>Hi there!</p><p>You have cancelled your order. Please try again.</p> <p>Lebupay Team </p>";

										EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
										emailInvoicingModel.setAction(action);
										emailInvoicingModel.setHeader(header);
										emailInvoicingModel.setAmount("" + transactionModel.getGrossAmount());
										emailInvoicingModel.setFirstName(transactionModel.getCustomer_firstName());
										emailInvoicingModel.setLastName(transactionModel.getCustomer_lastName());
										emailInvoicingModel.setEmailMessageBody(emailMessageBody);
										emailInvoicingModel.setSubject(subject);
										emailInvoicingModel.setEmailId(paymentModel.getEmailId());
										emailInvoicingModel.setIsTemplate(true);

										spiderEmailSender.sendEmail(emailInvoicingModel);

										// sendMail.send(paymentModel.getEmailId(), messageBody, subject);
									} catch (Exception e) {
										System.out.println("Mail Sending Failed");
									}
								}
							}
							// remove comment tag to enable sms sending when order is cancelled
							/*
							 * if (Objects.nonNull(paymentModel.getMobileNumber()) ||
							 * !paymentModel.getMobileNumber().equals("")) { //SMS for cancel order try {
							 * sendSMS.smsSend(paymentModel.getMobileNumber(),
							 * "You have cancelled your order. Please try again."); } catch (Exception e) {
							 * e.printStackTrace(); System.out.println("Send SMS"); } }/
							 **/
						}

					} else {
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("something.went.wrong"));
						returnURL = "redirect:/payment_failure?orderId=" + txnId;
					}
				} else {
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
					returnURL = "redirect:/payment_failure?orderId=" + txnId;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("XML Passing Exception = " + e);
			}
		} else {
			System.out.println("XML message blank --> " + request.getParameter("xmlmsg"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Decline Order -- END");
		}

		return returnURL;
	}

	/**
	 * This method is used CityBank Decline Order.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/declineOrder", method = RequestMethod.POST)
	public String declinedCityBankOrder(HttpServletRequest request, Model model, HttpServletResponse response,
			final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Decline Order -- START");
		}

		Document xmlDoc;

		String returnURL = "";
		String cityOrderID = "";
		String transactionType = "";
		String currency = "";
		String purchaseAmount = "";
		String responseCode = "";
		String responseDescription = "";
		String orderStatus = "";
		String approvalCode = "";
		String pan = "";
		String transDateTime = "";
		String brand = "";
		String acqFee = "";
		String merchantTransId = "";
		String orderDescription = "";
		String approvalCodeScr = "";
		String purchaseAmountScr = "";
		String currencyScr = "";
		String orderStatusScr = "";
		String threeDSVerificaion = "";
		String OrderStatus = "";
		String cardHolderName = "";
		String name = "";

		// String txnId = String.valueOf(transactionModelSaltTracker.getTxnId());

		if (request.getParameter("xmlmsg") != "") {

			try {
				xmlDoc = CityBankUtil.RequestHandler(request);

				InputSource source = new InputSource(new StringReader(Util.convertDocumentToString(xmlDoc)));
				XPath xpath = XPathFactory.newInstance().newXPath();
				Object citiResponse = xpath.evaluate("/Message", source, XPathConstants.NODE);

				cityOrderID = xpath.evaluate("OrderID", citiResponse);
				transactionType = xpath.evaluate("TransactionType", citiResponse);
				pan = xpath.evaluate("PAN", citiResponse);
				purchaseAmount = xpath.evaluate("PurchaseAmount", citiResponse);
				currency = xpath.evaluate("Currency", citiResponse);
				transDateTime = xpath.evaluate("TranDateTime", citiResponse);
				responseCode = xpath.evaluate("ResponseCode", citiResponse);
				responseDescription = xpath.evaluate("ResponseDescription", citiResponse);
				cardHolderName = xpath.evaluate("CardHolderName", citiResponse);
				brand = xpath.evaluate("Brand", citiResponse);
				orderStatus = xpath.evaluate("OrderStatus", citiResponse);
				approvalCode = xpath.evaluate("ApprovalCode", citiResponse);
				acqFee = xpath.evaluate("AcqFee", citiResponse);
				merchantTransId = xpath.evaluate("MerchantTranID", citiResponse);
				orderDescription = xpath.evaluate("OrderDescription", citiResponse);
				approvalCodeScr = xpath.evaluate("ApprovalCodeScr", citiResponse);
				purchaseAmountScr = xpath.evaluate("PurchaseAmountScr", citiResponse);
				currencyScr = xpath.evaluate("CurrencyScr", citiResponse);
				orderStatusScr = xpath.evaluate("OrderStatusScr", citiResponse);
				name = xpath.evaluate("Name", citiResponse);
				threeDSVerificaion = xpath.evaluate("ThreeDSVerificaion", citiResponse);
				String txnId = orderDescription; // TODO order description need to change when CITYbank EMI will be
													// included

				if (orderStatus.equals("DECLINED")) {
					TransactionModel transactionModel = null;

					try {
						// logwrite.writeLog(0L,"Citybank approveOrder",2, "approveOrder respCity
						// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
						writeLogV2(0L, "Citybank declineOrder", 2,
								"declineOrder respCity txnId:" + txnId + ",purchaseAmount:" + purchaseAmount + ",pan:"
										+ pan + ",merchantTransId:" + merchantTransId);
					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						transactionModel = transactionService.fetchTransactionByTXNId(txnId);
						if (Objects.isNull(transactionModel)) {
							throw new Exception();
						}
					} catch (Exception e) {
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
						returnURL = "redirect:/payment_failure_declined?orderId=" + txnId;
					}
					transactionModel.setBank("CityBank");
					transactionModel.setTransaction_type(transactionType);
					transactionModel.setTotalCapturedAmount(purchaseAmount);
					transactionModel.setTransaction_currency("BDT");
					transactionModel.setAuthorizationResponse_date(transDateTime);
					transactionModel.setResponseCode(responseCode);
					transactionModel.setAcquirerMessage(responseDescription);
					transactionModel.setCard_brand(brand);
					transactionModel.setOrder_status(orderStatus);
					transactionModel.setResponse_gatewayCode(approvalCode);
					transactionModel.setDescription(cityOrderID);
					transactionModel.setNameOnCard(cardHolderName);
					transactionModel.setCustomer_firstName(name);

					int result = transactionServiceImpl.updateCalcelledTransactionAfterCityPayment(transactionModel, 5);

					if (result > 0) {
						returnURL = "redirect:/payment_failure_declined?orderId=" + txnId;
						if (Objects.nonNull(transactionModel)) {
							PaymentModel paymentModel = new PaymentModel();
							paymentModel = transactionModel.getPaymentModel();
							String sms_notifictaion = paymentModel.getNotification_sms();
							String email_notification = paymentModel.getNotification_email();
							Gson gson = new Gson();
							paymentModel = gson.fromJson(paymentModel.getCustomerDetails(), PaymentModel.class);

							// JSON to Java object, read it from a Json String.
							/*
							 * paymentModel =
							 * gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
							 * PaymentModel.class);
							 * 
							 * String sms_notifictaion=paymentModel.getNotification_sms(); String
							 * email_notification=paymentModel.getNotification_email();/
							 **/

							if (email_notification.equals("1")) {
								if (Objects.nonNull(paymentModel.getEmailId())) {
									try {
										// Send Email
										String action = "paymentDeclined";
										String header = "Payment declined";
										String emailMessageBody = "<p>Hi there!</p><p>Your payment has been declined. Please try again.</p> <p>Lebupay Team </p>";
										String subject = messageUtil.getBundle("transaction.email.subject.declined");

										EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
										emailInvoicingModel.setAction(action);
										emailInvoicingModel.setHeader(header);
										emailInvoicingModel.setAmount("" + transactionModel.getGrossAmount());
										emailInvoicingModel.setFirstName(transactionModel.getCustomer_firstName());
										emailInvoicingModel.setLastName(transactionModel.getCustomer_lastName());
										emailInvoicingModel.setEmailMessageBody(emailMessageBody);
										emailInvoicingModel.setSubject(subject);
										emailInvoicingModel.setEmailId(paymentModel.getEmailId());
										emailInvoicingModel.setIsTemplate(true);

										spiderEmailSender.sendEmail(emailInvoicingModel);

										// sendMail.send(paymentModel.getEmailId(), messageBody, subject);
									} catch (Exception e) {
										e.printStackTrace();
										System.out.println("Mail Sending Failed");
									}
								}
							}
						}

					} else {
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("something.went.wrong"));
						returnURL = "redirect:/payment_failure_declined?orderId=" + txnId;
					}
				} else {
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
					returnURL = "redirect:/payment_failure?orderId=" + txnId;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("XML Passing Exception = " + e);
			}
		} else {
			System.out.println("XML message blank --> " + request.getParameter("xmlmsg"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("City Bank Decline Order -- END");
		}

		return returnURL;
	}

	/**
	 * This method is used for CityBank Payment Success.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return payment-success.jsp
	 */
	@RequestMapping(value = "/payment_success", method = RequestMethod.GET)
	public String paymentSuccess(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("payment success -- START");
		}

		try {
			System.out.println("Coming In City SUCCESS Section ==>> " + orderId);
			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);
			PaymentModel paymentModel1 = transactionService.fetchTransactionByTXNId_detail(orderId);// TODO accommodate
																									// get Customer
																									// detail

			if (Objects.nonNull(transactionModel)) {

				PaymentModel paymentModel = new PaymentModel();
				Gson gson = new Gson();
				// JSON to Java object, read it from a Json String.
				paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
						PaymentModel.class);
				System.out.println("paymentModel ==>> " + paymentModel);
				if (Objects.nonNull(paymentModel1.getSuccessURL()))
					if (!paymentModel1.getSuccessURL().equals("None"))
						response.sendRedirect(paymentModel1.getSuccessURL());
				// TODO Added By Wasif 20190314
				if (!paymentModel1.getServerSuccessURL().equals("NotSet")) {
					// response.sendRedirect(transactionModel.getPaymentModel().getServerSuccessURL());

					String inputJsonString = "{\"email\":\"" + paymentModel1.getEmailId() + "\", \"mobileNumber\":\""
							+ paymentModel1.getMobileNumber() + "\", \"amount\":\"" + paymentModel1.getAmount()
							+ "\", \"orderTransactionID\":\"" + paymentModel1.getOrderTransactionID()
							+ "\", \"responseCode\":\"" + paymentModel1.getResponseCode()
							+ "\", \"transactionStatus\":\"" + paymentModel1.getTransactionStatus()
							+ "\", \"transactionDate\":\"" + paymentModel1.getTransactionDate()
							+ "\", \"card_brand\":\"" + paymentModel1.getCard_brand()
							+ "\", \"provided_card_number\":\"" + paymentModel1.getProvided_card_number()
							+ "\", \"bank_merchant_id\":\"" + paymentModel1.getBank_merchant_id()
							+ "\", \"transaction_type\":\"" + paymentModel1.getTransaction_type()
							+ "\", \"billing_name\":\"" + paymentModel1.getBilling_name() + "\", \"bank\":\""
							+ paymentModel1.getBank() + "\"}";

					System.out.println("inputJsonString ==>> " + inputJsonString);
					ClientResponse client_response = payconnectApiAcess(paymentModel1.getServerSuccessURL(),
							inputJsonString, "post");

					try {
						// logwrite.writeLog(transactionModel.getMerchantModel().getMerchantId(),"Citybank
						// approveOrder",1, "approveOrder cityTrxModelUpd
						// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
						writeLogV2(transactionModel.getMerchantModel().getMerchantId(), "server-server API", 2,
								"on success order trx ID:" + transactionModel.getPaymentModel().getOrderTransactionID()
										+ ",response:" + client_response);

					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}
					/***/
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Payment Success -- END");
		}

		return "payment-success";
	}

	// added by Shaker on 16.04.2019
	/**
	 * This method is used to open Transaction Expired Page.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return transaction-expired.jsp
	 */
	@RequestMapping(value = "/transaction_expired", method = RequestMethod.GET)
	public String transactionExpired(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("Transaction Expired -- START");
		}

		try {

			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);
			PaymentModel paymentModel1 = transactionService.fetchTransactionByTXNId_detail(orderId);// TODO accommodate
																									// get Customer
																									// detail

			if (Objects.nonNull(transactionModel)) {
				if (transactionModel.getTransactionStatus() != 0 && transactionModel.getTransactionStatus() != 7) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 7,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					// shaker move from here
					// added by Shaker on 16.04.2019
				} else {
					System.out.println("ALREADY UPDATED!!");
				}
				// shaker move start
				if (Objects.nonNull(paymentModel1.getFailureURL()))
					if (!paymentModel1.getFailureURL().equals("None"))
						response.sendRedirect(paymentModel1.getFailureURL());

				// TODO Added By Wasif 20190314

				if (!paymentModel1.getServerFailureURL().equals("NotSet")) {
					// response.sendRedirect(transactionModel.getPaymentModel().getServerFailureURL());

					String inputJsonString = "{\"email\":\"" + paymentModel1.getEmailId() + "\", \"mobileNumber\":\""
							+ paymentModel1.getMobileNumber() + "\", \"amount\":\"" + paymentModel1.getAmount()
							+ "\", \"orderTransactionID\":\"" + paymentModel1.getOrderTransactionID()
							+ "\", \"responseCode\":\"" + paymentModel1.getResponseCode()
							+ "\", \"transactionStatus\":\"" + "Failure" + "\"}";

					System.out.println("inputJsonString ==>> " + inputJsonString);
					ClientResponse client_response = payconnectApiAcess(paymentModel1.getServerFailureURL(),
							inputJsonString, "post");

					try {
						// logwrite.writeLog(transactionModel.getMerchantModel().getMerchantId(),"Citybank
						// approveOrder",1, "approveOrder cityTrxModelUpd
						// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
						writeLogV2(transactionModel.getMerchantModel().getMerchantId(), "server-server API", 2,
								"on failure order trx ID:" + transactionModel.getPaymentModel().getOrderTransactionID()
										+ ",response:" + client_response);

					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}
					/***/
				}
				// shaker move end
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Transaction Expired -- END");
		}

		return "transaction-expired";
	}

	/**
	 * This method is used to open Payment Failure Page for City Bank.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return payment-failure.jsp
	 */
	@RequestMapping(value = "/payment_failure", method = RequestMethod.GET)
	public String paymentfailure(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("Payment Failure -- START");
		}

		try {

			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);
			PaymentModel paymentModel1 = transactionService.fetchTransactionByTXNId_detail(orderId);// TODO accommodate
																									// get Customer
																									// detail

			if (Objects.nonNull(transactionModel)) {
				if (transactionModel.getTransactionStatus() != 0) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 3,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					// shaker move from here
					// added by Shaker on 16.04.2019
				}

				// shaker move start
				if (Objects.nonNull(paymentModel1.getFailureURL()))
					if (!paymentModel1.getFailureURL().equals("None"))
						response.sendRedirect(paymentModel1.getFailureURL());

				// TODO Added By Wasif 20190314

				if (!paymentModel1.getServerFailureURL().equals("NotSet")) {
					// response.sendRedirect(transactionModel.getPaymentModel().getServerFailureURL());

					String inputJsonString = "{\"email\":\"" + paymentModel1.getEmailId() + "\", \"mobileNumber\":\""
							+ paymentModel1.getMobileNumber() + "\", \"amount\":\"" + paymentModel1.getAmount()
							+ "\", \"orderTransactionID\":\"" + paymentModel1.getOrderTransactionID()
							+ "\", \"responseCode\":\"" + paymentModel1.getResponseCode()
							+ "\", \"transactionStatus\":\"" + "Failure" + "\"}";

					System.out.println("inputJsonString ==>> " + inputJsonString);
					ClientResponse client_response = payconnectApiAcess(paymentModel1.getServerFailureURL(),
							inputJsonString, "post");

					try {
						// logwrite.writeLog(transactionModel.getMerchantModel().getMerchantId(),"Citybank
						// approveOrder",1, "approveOrder cityTrxModelUpd
						// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
						writeLogV2(transactionModel.getMerchantModel().getMerchantId(), "server-server API", 2,
								"on failure order trx ID:" + transactionModel.getPaymentModel().getOrderTransactionID()
										+ ",response:" + client_response);

					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}
					/***/
				}
				// shaker move end
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("payment failure -- END");
		}

		return "payment-failure";
	}

	/**
	 * This method is used to open Payment Failure Page for City Bank.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return payment-failure.jsp
	 */
	@RequestMapping(value = "/payment_failure_declined", method = RequestMethod.GET)
	public String paymentfailureDeclined(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("Payment Failure -- START");
		}

		try {

			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);
			PaymentModel paymentModel1 = transactionService.fetchTransactionByTXNId_detail(orderId);// TODO accommodate
																									// get Customer
																									// detail

			if (Objects.nonNull(transactionModel)) {
				if (transactionModel.getTransactionStatus() != 0) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 5,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					// shaker move from here
					// added by Shaker on 16.04.2019
				}
				// shaker move start
				if (Objects.nonNull(paymentModel1.getFailureURL()))
					if (!paymentModel1.getFailureURL().equals("None"))
						response.sendRedirect(paymentModel1.getFailureURL());
				// TODO Added By Wasif 20190314
				if (!paymentModel1.getServerFailureURL().equals("NotSet"))
					response.sendRedirect(paymentModel1.getServerFailureURL());
				// shaker move end
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("payment failure -- END");
		}

		return "payment-failure";
	}

	// EBL STARTS HERE
	/**
	 * This method is used to open payment page for EBL gateway.
	 * 
	 * @param request
	 * @param model
	 * @param transactionId
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/ebl", method = RequestMethod.GET)

	public String ebl(HttpServletRequest request, Model model, @RequestParam String transactionId,
			HttpServletResponse response, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("ebl -- START");
		}

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";

		Long transactionId1 = 0L;
		TransactionModel transactionModel2 = null;

		try {

			System.out.println("SESSIONKEY Before Decode== >> " + transactionId);
			String key = messageUtil.getBundle("secret.key");
			transactionId1 = Long.parseLong(encryption.decode(key, transactionId));
			System.out.println("TransactionId After Decode== >> " + transactionId1);

			try {
				// TODO
				// logwrite.writeLog
				writeLogV2(0L, "EBL GET", 1, "trxID:" + transactionId1 + ",SESSIONKEY:" + transactionId);
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}
			transactionModel2 = transactionService.fetchTransactionById(transactionId1);

		} catch (Exception e) {

			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;
		}

		if (Objects.isNull(transactionModel2)) {
			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;

		} else {

			if (transactionModel2.getTransactionStatus() == 0 || transactionModel2.getTransactionStatus() == 1
					|| transactionModel2.getTransactionStatus() == 2) { // Already Paid

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("already.paid"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();

			} else if (transactionModel2.getTransactionStatus() == 3) { // Failed

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();
			} else if (transactionModel2.getTransactionStatus() == 7) { // expired

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();

			}
			if (transactionModel2.getIsValid()) { // valid, do nothing
			} else {
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();
			}
		}

		transactionModelSaltTracker.setTxnId(transactionModel2.getTxnId());

		// String requestURL = messageUtil.getBundle("ebi.first.url");//Replaced by
		// Wasif 20190416
		String requestURL = messageUtil.getBundle("ebl.jsonReq1.url");
		System.out.println("requestURL ==>> " + requestURL);
		Map<String, String> argsMap = new HashMap<String, String>();

		try {
			// logwrite.writeLog
			writeLogV2(0L, "EBL CREATE_CHECKOUT_SESSION GET", 1,
					"trxid:" + transactionId1 + ",CREATE_CHECKOUT_SESSION, REQ MAP :" + argsMap.toString());
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			/*
			 * URL url = new URL(
			 * "https://easternbank.test.gateway.mastercard.com/api/rest/version/43/merchant/"
			 * + transactionModel2.getMerchantModel().getEblId() + "/session");/
			 **/
			URL url = new URL(requestURL + transactionModel2.getMerchantModel().getEblId() + "/session");

			String authStr = transactionModel2.getMerchantModel().getEblUserName() + ":"
					+ transactionModel2.getMerchantModel().getEblPassword();
			String authEncoded = Base64.encode(authStr);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("Authorization", "Basic " + authEncoded);
			http.setDoOutput(true);

			JsonObject mainJson = new JsonObject();
			JsonObject orderJson = new JsonObject();
			JsonObject interaction = new JsonObject();
			JsonObject displayControl = new JsonObject();
			JsonObject merchant = new JsonObject();

			mainJson.addProperty("apiOperation", "CREATE_CHECKOUT_SESSION");

			orderJson.addProperty("id", transactionModel2.getTxnId());
			orderJson.addProperty("currency", "BDT");
			orderJson.addProperty("amount", String.valueOf(transactionModel2.getAmount()));
			orderJson.addProperty("description", "OrderFromLebuPay");

			displayControl.addProperty("billingAddress", "HIDE");
			displayControl.addProperty("orderSummary", "HIDE");

			merchant.addProperty("name", "Lebupay");

			interaction.add("displayControl", displayControl);
			interaction.add("merchant", merchant);
			interaction.addProperty("returnUrl", basePath + "retriveOrder?orderId=" + transactionModel2.getTxnId());
			interaction.addProperty("cancelUrl", basePath + "failure?orderId=" + transactionModel2.getTxnId());

			mainJson.add("order", orderJson);
			mainJson.add("interaction", interaction);

			String data1 = mainJson.toString();
			DataOutputStream dos = new DataOutputStream(http.getOutputStream());
			dos.writeBytes(data1);
			dos.flush();
			dos.close();
			System.out.println("1st Json data: " + data1);

			String res_status = http.getResponseMessage();
			BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
			String inputLine;
			StringBuffer response1 = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response1.append(inputLine);
			}
			System.out.println("1st Json resp: " + response1);
			in.close();
			JsonParser parser = new JsonParser();
			JsonObject json1 = (JsonObject) parser.parse(response1.toString());

			Map<String, Object> respMap = toMap(json1);

			String firstUrlResult = json1.get("result").getAsString();

			JsonObject getSession = json1.getAsJsonObject("session");

			String sessionId = getSession.get("id").getAsString();

			try {
				// TODO
				// logwrite.writeLog
				writeLogV2(0L, "EBL CREATE_CHECKOUT_SESSION GET", 2,
						"trxid:" + transactionId1 + ",CREATE_CHECKOUT_SESSION, resp Map :" + respMap.toString());
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}

			if (respMap.containsKey("result")) {
				if (firstUrlResult.equals("SUCCESS")) {

					System.out.println("SUCCESS");
					String secondAPI = messageUtil.getBundle("ebi.second.url") + sessionId;
					System.out.println("secondAPI ==>> " + secondAPI);

					try {
						// TODO
						// logwrite.writeLog
						writeLogV2(0L, "EBL 2nd API GET", 1,
								"trxid:" + transactionId1 + ",secondAPI, request session :" + sessionId);
					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}
					response.sendRedirect(secondAPI);
				} else {

					System.out.println("Failure");
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
					return "redirect:/failure?orderId=" + transactionModel2.getTxnId();
				}
			} else {

				System.out.println("No Response");
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
			return "redirect:/failure?orderId=" + transactionModel2.getTxnId();
		}

		HTTPConnection.disconnect();

		if (logger.isInfoEnabled()) {
			logger.info("ebl -- END");
		}

		return null;
	}

	public static Map<String, Object> toMap(JsonObject object) {
		Map<String, Object> map = new HashMap<String, Object>();

		Set<Map.Entry<String, JsonElement>> entries = object.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			String key = entry.getKey();
			Object value = object.get(key);
			if (value instanceof JsonArray) {
				value = toList((JsonArray) value);
			}

			else if (value instanceof JsonObject) {
				value = toMap((JsonObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JsonArray array) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.size(); i++) {
			Object value = array.get(i);
			if (value instanceof JsonArray) {
				value = toList((JsonArray) value);
			}

			else if (value instanceof JsonObject) {
				value = toMap((JsonObject) value);
			}
			list.add(value);
		}
		return list;
	}

	/**
		 * This method is used to get Response from EBL gateway after Payment.
		 * 
		 * @param request
		 * @param model
		 * @param response
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/retriveOrder", method = RequestMethod.GET)
		
		public String retriveOrder(HttpServletRequest request, Model model, HttpServletResponse response,
				@RequestParam(required = false) String orderId, final RedirectAttributes redirectAttributes) {

			/*
			 * 20190223 public String retriveOrder(HttpServletRequest request, Model model,
			 * HttpServletResponse response ,final RedirectAttributes redirectAttributes) {
			 * /
			 **/

			if (logger.isInfoEnabled()) {
				logger.info("retriveOrder -- START");
			}

			String returnURL = "";
			String resultIndicator = (String) request.getParameter("resultIndicator");
			System.out.println("resultIndicator ==>> " + resultIndicator + " orderId: " + orderId);

			try {
				// logwrite.writeLog
				writeLogV2(0L, "EBL RETRIEVE_ORDER GET", 1, "orderId:" + orderId + ",resultIndicator :" + resultIndicator);
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}
			// Wasif 20190223 String orderId =
			// String.valueOf(transactionModelSaltTracker.getTxnId());

			TransactionModel transactionModel2 = null;
			try {

				transactionModel2 = transactionService.fetchTransactionByTXNId(orderId);
				if (Objects.isNull(transactionModel2)) {
					throw new Exception();
				}

			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				returnURL = "redirect:/failure?orderId=" + orderId;
			}


			try {
				
				String requestURL = messageUtil.getBundle("ebl.jsonReq1.url");
/*
				URL url = new URL("https://easternbank.test.gateway.mastercard.com/api/rest/version/43/merchant/"
						+ transactionModel2.getMerchantModel().getEblId() + "/order/" + orderId);/**/
				
				URL url = new URL(requestURL+ transactionModel2.getMerchantModel().getEblId() + "/order/" + orderId);

				String authStr = transactionModel2.getMerchantModel().getEblUserName() + ":"
						+ transactionModel2.getMerchantModel().getEblPassword();
				String authEncoded = Base64.encode(authStr);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				http.setRequestMethod("GET");
				http.setRequestProperty("Authorization", "Basic " + authEncoded);
				http.setDoOutput(true);

				String res_status = http.getResponseMessage();
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
				String inputLine;
				StringBuffer response2 = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response2.append(inputLine);
				}
				//write json log here
				System.out.println("3rd json response: " + response2);
				in.close();
				
				JsonParser parser = new JsonParser();
				JsonObject json2 = (JsonObject) parser.parse(response2.toString());
				
				Map<String, Object> respMap = toMap(json2);
				
				
				String successJsonStr = successJsonEbl(response2.toString());
				//write success transaction log here
				JsonObject successJson = (JsonObject) parser.parse(successJsonStr);
				Map<String, Object> successJsonMap = toMap(successJson);

				String creationTime = json2.get("creationTime").getAsString() != null ? json2.get("creationTime").getAsString() : "";


				JsonObject authorizationResponse = successJson.getAsJsonObject("authorizationResponse");
				String authDate = authorizationResponse.get("date").getAsString() != null ? authorizationResponse.get("date").getAsString() : "";
				
				
				String financialNetworkCode = "";
	            if (authorizationResponse.has("financialNetworkDate")) {
	                financialNetworkCode = authorizationResponse.get("financialNetworkCode").getAsString() != null
	                        ? authorizationResponse.get("financialNetworkCode").getAsString() : "";
	            }
				
				
			String transactionIdentifier = authorizationResponse.get("transactionIdentifier").getAsString() != null ? authorizationResponse.get("transactionIdentifier").getAsString() : "";
		//	transactionIdentifier
			String authTime = authorizationResponse.get("time").getAsString() != null ? authorizationResponse.get("time").getAsString() : "";
				String stan = authorizationResponse.get("stan").getAsString() != null ? authorizationResponse.get("stan").getAsString() : "";
				String commercialCardIndicator = authorizationResponse.get("commercialCardIndicator").getAsString() != null ? authorizationResponse.get("commercialCardIndicator").getAsString() : "";
				String financialNetworkDate = "";
				if(authorizationResponse.has("financialNetworkDate")) {
					financialNetworkDate = !authorizationResponse.get("financialNetworkDate").getAsString().equals(null)? authorizationResponse.get("financialNetworkDate").getAsString() : "";
				}
				
				String responseCode = authorizationResponse.get("responseCode").getAsString() != null ? authorizationResponse.get("responseCode").getAsString() : "";
				String processingCode = authorizationResponse.get("processingCode").getAsString() != null ? authorizationResponse.get("processingCode").getAsString() : "";
				String cardSecurityCodeError = authorizationResponse.get("cardSecurityCodeError").getAsString() != null ? authorizationResponse.get("cardSecurityCodeError").getAsString() : "";

				String totalCapturedAmount = json2.get("totalCapturedAmount").getAsString();

				JsonObject sourceOfFunds = json2.getAsJsonObject("sourceOfFunds");
				String cardType = sourceOfFunds.get("type").getAsString() != null ? sourceOfFunds.get("type").getAsString() : "";

				JsonObject customer = json2.getAsJsonObject("customer");
				String firstName = customer.get("firstName").getAsString() != null ? customer.get("firstName").getAsString() : "";
				String lastName = "";
				if(customer.has("lastName")) {
					lastName = customer.get("lastName").getAsString() != null ? customer.get("lastName").getAsString() : "";
				}
				


				JsonObject sourceOfFunds1 = json2.getAsJsonObject("sourceOfFunds");
				JsonObject provided = sourceOfFunds1.getAsJsonObject("provided");
				JsonObject cardData = provided.getAsJsonObject("card");
				String fundingMethod = "";
				if(cardData.has("fundingMethod")) {
					fundingMethod= cardData.get("fundingMethod").getAsString() != null ? cardData.get("fundingMethod").getAsString() :  "";
				}
				String nameOnCard = "";
				if(cardData.has("nameOnCard")) {
					nameOnCard = cardData.get("nameOnCard").getAsString() != null ? cardData.get("nameOnCard").getAsString() : "";
				}
				
				String issuer = cardData.get("issuer").getAsString() != null ? cardData.get("issuer").getAsString() : "";

				
				
				JsonObject device = successJson.getAsJsonObject("device");
				String browser = device.get("browser").getAsString() != null ? device.get("browser").getAsString() : "";
				JsonObject device1 = json2.getAsJsonObject("device");
				String ipAddress = device1.get("ipAddress").getAsString() != null ? device.get("ipAddress").getAsString() : "";


				JsonObject sourceOfFundSuccess = successJson.getAsJsonObject("sourceOfFunds");
				JsonObject providedSuccess = sourceOfFundSuccess.getAsJsonObject("provided");
				JsonObject cardSuccess = providedSuccess.getAsJsonObject("card");
				String cardNumber = cardSuccess.get("number").getAsString() != null ? cardSuccess.get("number").getAsString() : "";
				String cardBrand = cardSuccess.get("brand").getAsString() != null ? cardSuccess.get("brand").getAsString() : "";

				JsonObject cardExpirySuccess = cardSuccess.getAsJsonObject("expiry");
				String expiryYear = cardExpirySuccess.get("year").getAsString() != null ? cardExpirySuccess.get("year").getAsString() : "";
				String expiryMonth = cardExpirySuccess.get("month").getAsString() != null ? cardExpirySuccess.get("month").getAsString() : "";


				JsonObject transaction = successJson.getAsJsonObject("transaction");
				String currency = transaction.get("currency").getAsString() != null ? transaction.get("currency").getAsString() : "";
				String receipt = transaction.get("receipt").getAsString() != null ? transaction.get("receipt").getAsString() : "";
				String trxSource = transaction.get("source").getAsString() != null ? transaction.get("source").getAsString() : "";
				String trxType = transaction.get("type").getAsString() != null ? transaction.get("type").getAsString() : "";
				String frequency = transaction.get("frequency").getAsString() != null ? transaction.get("frequency").getAsString() : "";
				String terminal = transaction.get("terminal").getAsString() != null ? transaction.get("terminal").getAsString() : "";
				String authorizationCode = transaction.get("authorizationCode").getAsString() != null ? transaction.get("authorizationCode").getAsString() : "";
				String trxId = transaction.get("id").getAsString() != null ? transaction.get("id").getAsString() : "";
	//	trxId
				String targetTransactionId = "";
				if(transaction.has("targetTransactionId")) {
					targetTransactionId = transaction.get("targetTransactionId").getAsString() != null ? transaction.get("targetTransactionId").getAsString() : "";
				}
				String transactionId = "";
				if(transaction.has("transactionId")) {
					transactionId = transaction.get("transactionId").getAsString() != null ? transaction.get("transactionId").getAsString() : "";
				}
				


				JsonObject acquirerSuccess = transaction.getAsJsonObject("acquirer");
				String acquirerDate = acquirerSuccess.get("date").getAsString() != null ? acquirerSuccess.get("date").getAsString() : "";
				String acquirerId = acquirerSuccess.get("id").getAsString() != null ? acquirerSuccess.get("id").getAsString() : "";
				String settlementDate = acquirerSuccess.get("settlementDate").getAsString() != null ? acquirerSuccess.get("settlementDate").getAsString() : "";
				String acquirerBatch = acquirerSuccess.get("batch").getAsString() != null ? acquirerSuccess.get("batch").getAsString() : "";
				String timeZone = acquirerSuccess.get("timeZone").getAsString() != null ? acquirerSuccess.get("timeZone").getAsString() : "";


				String secure3DId = successJson.get("3DSecureId").getAsString() != null ? successJson.get("3DSecureId").getAsString() : "";
				String merchantSuccess = successJson.get("merchant").getAsString() != null ? successJson.get("merchant").getAsString() : "";
				String version = successJson.get("version").getAsString() != null ? successJson.get("version").getAsString() : "";
				String trxResult = successJson.get("result").getAsString() != null ? successJson.get("result").getAsString() : "";
				String gatewayEntryPoint = successJson.get("gatewayEntryPoint").getAsString() != null ? successJson.get("gatewayEntryPoint").getAsString() : "";


				JsonObject responseSuccess = successJson.getAsJsonObject("response");
				String responseGatewayCode = "";
				if(responseSuccess.has("gateway")) {
					responseGatewayCode = responseSuccess.get("gateway").getAsString();
				}
				

				JsonObject cardSecurityCode = responseSuccess.getAsJsonObject("cardSecurityCode");
				String acquirerCode = cardSecurityCode.get("acquirerCode").getAsString() != null ? cardSecurityCode.get("acquirerCode").getAsString() : "";
				String gatewayCode = cardSecurityCode.get("gatewayCode").getAsString() != null ? cardSecurityCode.get("gatewayCode").getAsString() : "";


				JsonObject secure3D = successJson.getAsJsonObject("3DSecure");
				String authenticationToken = secure3D.get("authenticationToken").getAsString() != null ? secure3D.get("authenticationToken").getAsString() : "";
				String acsEci = secure3D.get("acsEci").getAsString() != null ? secure3D.get("acsEci").getAsString() : "";
				String authenticationStatus = secure3D.get("authenticationStatus").getAsString() != null ? secure3D.get("authenticationStatus").getAsString() : "";
				String xid = secure3D.get("xid").getAsString() != null ? secure3D.get("xid").getAsString() : "";
				String enrollmentStatus = secure3D.get("enrollmentStatus").getAsString() != null ? secure3D.get("enrollmentStatus").getAsString() : "";


				JsonObject orderSuccess = successJson.getAsJsonObject("order");
				String orderStatus = orderSuccess.get("status").getAsString() != null ? orderSuccess.get("status").getAsString() :"";
				String totalRefundedAmount = orderSuccess.get("totalRefundedAmount").getAsString() != null ? orderSuccess.get("totalRefundedAmount").getAsString() :"";


				
				
				JsonObject responseJson1 = successJson.getAsJsonObject("response");
				String acquirerMessage = responseJson1.get("acquirerMessage").getAsString();
				String totalAuthorizedAmount = json2.get("totalAuthorizedAmount").getAsString() != null ? json2.get("totalAuthorizedAmount").getAsString() : "";
				
				String resultJson = json2.get("result").getAsString();

				try {
					// logwrite.writeLog
					writeLogV2(0L, "EBL RETRIEVE_ORDER POST", 2, "orderId:" + orderId + ",respMap :" + respMap.toString());
				} catch (Exception e1) {
					// Auto-generated catch block
					e1.printStackTrace();
				}

				for (Entry<String, Object> entry : respMap.entrySet()) {

					System.out.println("Key ==>> " + entry.getKey() + "  Value ==>> " + entry.getValue());
				}
	//testing EBL Void Transactions
				// TODO

				if (json2.has("result")) {
					if (resultJson.equals("SUCCESS")) {

						System.out.println("SUCCESS");

						// successJson(String json);
						// successJson("");

						System.err.println("orderId ==>> " + orderId);

						int result = 0;
						try {

							TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);

							transactionModel.setAuthorizationResponse_date(authDate);
							transactionModel.setTotalCapturedAmount(totalCapturedAmount);
							transactionModel.setFundingMethod(fundingMethod);
							transactionModel.setAcquirerMessage(acquirerMessage);
							transactionModel.setFinancialNetworkCode(financialNetworkCode);
							transactionModel.setTransactionIdentifier(transactionIdentifier);// TODO
																												
							transactionModel.setNameOnCard(nameOnCard);
							transactionModel.setCard_expiry_year(expiryYear);
							transactionModel.setAuthorizationResponse_time(authTime);
							transactionModel
									.setTransaction_currency(currency);
							transactionModel.setSecureId(secure3DId);
							transactionModel.setAcquirerCode(acquirerCode);
							transactionModel.setAuthorizationResponse_stan(stan);
							transactionModel.setMerchantId(merchantSuccess);
							transactionModel.setTotalAuthorizedAmount(totalAuthorizedAmount);
							transactionModel.setProvided_card_number(cardNumber);
							transactionModel.setCardSecurityCode_gatewayCode(gatewayCode);
							transactionModel
									.setAuthenticationToken(authenticationToken);
							transactionModel.setTransaction_receipt(receipt);
							transactionModel
									.setResponse_gatewayCode(responseGatewayCode);
							transactionModel.setOrder_status(orderStatus);
							transactionModel.setAcquirer_date(acquirerDate);
							transactionModel.setVersion(version);
							transactionModel.setCommercialCardIndicator(commercialCardIndicator);
							transactionModel
									.setCard_brand(cardBrand);
							transactionModel.setSourceOfFunds_type(cardType);
							transactionModel.setCustomer_firstName(firstName);
							transactionModel.setCustomer_lastName(lastName);
							transactionModel.setDevice_browser(browser);
							transactionModel.setDevice_ipAddress(ipAddress);
							transactionModel.setAcsEci_value(acsEci);
							transactionModel.setAcquirer_id(acquirerId);
							transactionModel.setSettlementDate(settlementDate);
							transactionModel.setTransaction_source(trxSource);
							transactionModel.setResult(trxResult);
							transactionModel.setCreationTime(creationTime);
							transactionModel
									.setTotalRefundedAmount(totalRefundedAmount);
							transactionModel
									.setAcquirer_batch(acquirerBatch);
							transactionModel.setDescription(respMap.get("description").toString());
							transactionModel.setTransaction_type(trxType);
							transactionModel.setFinancialNetworkDate(financialNetworkDate);
							transactionModel
									.setResponseCode(responseCode);
							transactionModel
									.setTransaction_frequency(frequency);
							transactionModel
									.setTransaction_terminal(terminal);
							transactionModel
									.setAuthorizationCode(authorizationCode);
							transactionModel.setAuthenticationStatus(authenticationStatus);
							transactionModel.setProcessingCode(processingCode);
							transactionModel.setExpiry_month(expiryMonth);
							transactionModel.setSecure_xid(xid);
//							System.out.println("xid: " + transactionModel.getSecure_xid());
							transactionModel
									.setEnrollmentStatus(enrollmentStatus);
//							System.out.println("setEnrollmentStatus: " + transactionModel.getEnrollmentStatus());
							transactionModel.setCardSecurityCodeError(cardSecurityCodeError);
							transactionModel.setTimeZone(timeZone);
							transactionModel.setGatewayEntryPoint(gatewayEntryPoint);
							transactionModel.setBank("EBL");
							transactionModel.setIssuer_bank(issuer);
							// TODO need to add EBl logic Here can redirect to void request .
							String AcsEci = transactionModel.getAcsEci_value(); // Local:allow Int:block(ECI: Visa 6, MC 1)

							String transaction_Id_bankresp = trxId;
							transactionModel.setTransaction_Id_bankresp(transaction_Id_bankresp);
							String targetTransactionId_bankresp = targetTransactionId;
							transactionModel.setTargetTransactionId_bankresp(targetTransactionId_bankresp);
							String trx_transactionId_bankresp = transactionId;
							transactionModel.setTrx_transactionId_bankresp(trx_transactionId_bankresp);

							System.out.println("------->trx_transactionId_bankresp:" + trx_transactionId_bankresp
									+ ", targetTransactionId_bankresp:" + targetTransactionId_bankresp
									+ ", transaction_Id_bankresp:" + transaction_Id_bankresp);

							// TODO
							// TODO
							// both block (ECI: Visa 7, MC 0)
							// hello
							String Provided_card = transactionModel.getProvided_card_number();
							String CardBrand = transactionModel.getCard_brand();
							
							String cardIssuerType="";
							
							CardIssuerModel cardIssuerModel= bankCardLogic( AcsEci, CardBrand,Provided_card,"EBL" );
							//need to add logic to void here also insert to transaction master 
							transactionModel.setCardIssuerModel(cardIssuerModel);
							
							String retvalVoid="";
							if(cardIssuerModel.getBinType()!= 1 ) {
								//TODO Initiate Void
								retvalVoid=voidEblOrder( orderId, transactionModel,trxId, transactionIdentifier) ;
								System.out.println("retvalVoid:"+retvalVoid);
							}else if(cardIssuerModel.getResp_ACSEI()== -1 ) {
								//TODO Initiate Void
							}
							if(retvalVoid.contains("SUCCESS")) {
								System.out.println("inside success if cond retvalVoid:"+retvalVoid);
								transactionModel.setTrx_status(6);
							}
							
							// TODO
							/*
							 * int responseFromBanklogic= bankCardLogic( AcsEci, CardBrand,
							 * Provided_card,"EBL" );
							 * 
							 * if(responseFromBanklogic == -1) {
							 * System.out.println("Entering void from retrieve order");
							 * 
							 * try { eblVoid(request,orderId,transactionModel,trx_transactionId_bankresp,
							 * trx_transactionId_bankresp); }catch(Exception e) { e.printStackTrace();
							 * System.out.println(); } //TODO Void redirection
							 * 
							 * returnURL = "redirect:/failure?orderId=" + orderId;//TODO remove }else {/
							 **/
							result = transactionServiceImpl.updateTransactionAfterPayment(transactionModel);
							int eblUpdateResult = 0;
							if(!retvalVoid.contains("SUCCESS")) {
							if (result > 0) {
								

								eblUpdateResult = transactionServiceImpl.updateEblTransactionAfterPayment(transactionModel);
								if (eblUpdateResult > 0) {
									returnURL = "redirect:/success?orderId=" + orderId;

									System.out.println("Success Transaction Model-->" + transactionModel);

									if (Objects.nonNull(transactionModel)) {

										PaymentModel paymentModel = new PaymentModel();
										Gson gson = new Gson();

										// WASIF 20190312
										paymentModel = transactionModel.getPaymentModel();

										String sms_notifictaion = paymentModel.getNotification_sms();
										String email_notification = paymentModel.getNotification_email();

										String customerDetails = paymentModel.getCustomerDetails();
										firstName = decode(customerDetails, "firstName");
										lastName = decode(customerDetails, "lastName");
										
										paymentModel = gson.fromJson(paymentModel.getCustomerDetails(), PaymentModel.class);// Wasif

										// JSON to Java object, read it from a Json String.
										/*
										 * paymentModel = gson.fromJson(
										 * transactionModel.getPaymentModel().getCustomerDetails(),
										 * PaymentModel.class);/
										 **/
										System.out.println("paymentModel ==>> " + paymentModel);

										if (email_notification.equals("1")) {
											if (Objects.nonNull(paymentModel.getEmailId())) {

												// Send Email
												String action = "transactionSuccess";
												String header = "Transaction successful";
												String emailMessageBody = "<p>Dear Sir/Madam!</p><p>We have successfully received "
														+ transactionModel.getGrossAmount()
														+ " BDT. </p> <p>Thank You for paying with LEBUPAY</p>";
												String subject = messageUtil.getBundle("transaction.email.subject");

												EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
												emailInvoicingModel.setAction(action);
												emailInvoicingModel.setHeader(header);
												emailInvoicingModel.setFirstName(firstName);
												emailInvoicingModel.setLastName(lastName);
												emailInvoicingModel.setAmount(""+transactionModel.getGrossAmount());
												emailInvoicingModel.setEmailMessageBody(emailMessageBody);
												emailInvoicingModel.setSubject(subject);
												emailInvoicingModel.setEmailId(paymentModel.getEmailId());
												emailInvoicingModel.setCardType(cardBrand);
												emailInvoicingModel.setNameOnCard(transactionModel.getNameOnCard());
												emailInvoicingModel.setMobileNumber(paymentModel.getMobileNumber());
												emailInvoicingModel.setCardNumber(cardNumber);
												emailInvoicingModel.setBankTrxID(receipt);
												emailInvoicingModel.setTrxID(orderId);
												emailInvoicingModel.setTransactionType(trxType);
												emailInvoicingModel.setIPAddress(ipAddress);
												emailInvoicingModel.setCurrency(currency);
												emailInvoicingModel.setIsTemplate(true);
												
												spiderEmailSender.sendEmail(emailInvoicingModel);

												// sendMail.send(paymentModel.getEmailId(), messageBody, subject);

											}
										}
										if (sms_notifictaion.equals("1")) {
											if (Objects.nonNull(paymentModel.getMobileNumber())) {
												sendSMS.smsSend(paymentModel.getMobileNumber(), "We have received "
														+ transactionModel.getGrossAmount()
														+ " BDT. Thank You for paying with LEBUPAY. Visit www.we-top-up.com for mobile recharge.");
											}
										}

									}
								} else {

									System.out.println("Failure");
									redirectAttributes.addFlashAttribute("failure",
											messageUtil.getBundle("something.went.wrong"));
									returnURL = "redirect:/failure?orderId=" + orderId;
								}
							} else {
								
								eblUpdateResult = transactionServiceImpl.updateEblTransactionAfterPayment(transactionModel);
								

								System.out.println("Failure");
								redirectAttributes.addFlashAttribute("failure",
										messageUtil.getBundle("something.went.wrong"));
								returnURL = "redirect:/failure?orderId=" + orderId;
							}
						 }else {
							 //TODO void transaction
							 System.out.println("transaction voided");
								redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
								returnURL = "redirect:/failure_void?orderId=" + orderId;
						
						 }

						} catch (Exception e) {
							e.printStackTrace();
							redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
							returnURL = "redirect:/failure?orderId=" + orderId;
						}

					} else {

						System.out.println("Failure");
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
						returnURL = "redirect:/failure?orderId=" + orderId;
					}
				} else {
					System.out.println("No Response");
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
					returnURL = "redirect:/failure?orderId=" + orderId;
				}

				HTTPConnection.disconnect();

			} catch (Exception ex) {
				ex.printStackTrace();
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				returnURL = "redirect:/failure?orderId=" + orderId;
			}

			if (logger.isInfoEnabled()) {
				logger.info("retriveOrder -- END");
			}

			return returnURL;
		}

	/*
	 * public String voidEblOrder(HttpServletRequest request, String orderid,
	 * TransactionModel tm, String targetTransactionId, String transactionId) {/
	 **/

	public String voidEblOrder(String orderid, TransactionModel tm, String targetTransactionId, String transactionId) {

		String retval = "";
		/*
		 * //@RequestMapping(value = "/ebl_void", method = RequestMethod.GET)
		 * 
		 * public String eblVoid(HttpServletRequest request, Model model, @RequestParam
		 * String transactionId,String orderid, HttpServletResponse response, final
		 * RedirectAttributes redirectAttributes) { /
		 **/
		if (logger.isInfoEnabled()) {
			logger.info("ebl void -- START");
		}
		/*
		 * String path = request.getContextPath(); String basePath = request.getScheme()
		 * + "://" + request.getServerName() + ":" + request.getServerPort() + path +
		 * "/";/
		 **/
		Map<String, String> argsMap = new HashMap<String, String>();
		Long transactionId1 = 0L;
		TransactionModel transactionModel2 = null;

		/**
		 * Wasif try {
		 * 
		 * String key = messageUtil.getBundle("secret.key"); transactionId1 =
		 * Long.parseLong(encryption.decode(key, transactionId));
		 * System.out.println("TransactionId After Decode== >> " + transactionId1);
		 * 
		 * try { // TODO // logwrite.writeLog writeLogV2(0L, "EBL void GET", 1, "trxID:"
		 * + transactionId1 + ",SESSIONKEY:" + transactionId); } catch (Exception e1) {
		 * // Auto-generated catch block e1.printStackTrace(); } transactionModel2 = tm;
		 * 
		 * } catch (Exception e) { e.printStackTrace();
		 * 
		 * }/
		 ***/ // Wasif

		try {

			// change before final commit. after order put lebupay order id, after
			// transaction put transaction identifier value
			String requestURL = messageUtil.getBundle("ebl.jsonReq2.url");
			URL url = new URL(requestURL + tm.getMerchantModel().getEblId() + "/order/" + orderid + "/transaction/"
					+ transactionId);

			String authStr = tm.getMerchantModel().getEblUserName() + ":" + tm.getMerchantModel().getEblPassword();
			String authEncoded = Base64.encode(authStr);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("PUT");
			http.setRequestProperty("Authorization", "Basic " + authEncoded);
			http.setDoOutput(true);

			JsonObject mainJson = new JsonObject();
			JsonObject transactionJson = new JsonObject();

			mainJson.addProperty("apiOperation", "VOID");
			transactionJson.addProperty("targetTransactionId", targetTransactionId); // change it with id which received
																						// with transaction retrieve
																						// order
			// field

			mainJson.add("transaction", transactionJson);

			String data1 = mainJson.toString();
			DataOutputStream dos = new DataOutputStream(http.getOutputStream());
			dos.writeBytes(data1);
			dos.flush();
			dos.close();
			System.out.println("void request Json data: " + data1); // RAW Json

			System.out.println("Initiation void json");
			String res_status = http.getResponseMessage();
			BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
			String inputLine;
			StringBuffer response2 = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response2.append(inputLine);
			}
			// TODO write json log here
			System.out.println("void json response: " + response2.toString());
			in.close();

			JsonParser parser = new JsonParser();
			JsonObject json2 = (JsonObject) parser.parse(response2.toString());

			Map<String, Object> respMap = toMap(json2);

			JsonObject authorizationResponse = json2.getAsJsonObject("authorizationResponse");
			String transactionIdentifier = authorizationResponse.get("transactionIdentifier").getAsString();

			JsonObject customer = json2.getAsJsonObject("customer");
			String firstName = customer.get("firstName").getAsString() != null ? customer.get("firstName").getAsString()
					: "";
			String lastName = "";
			if (customer.has("lastName")) {
				lastName = customer.get("lastName").getAsString() != null ? customer.get("lastName").getAsString() : "";
			}

			JsonObject device = json2.getAsJsonObject("device");
			String browser = device.get("browser").getAsString() != null ? device.get("browser").getAsString() : "";
			JsonObject device1 = json2.getAsJsonObject("device");
			String ipAddress = device1.get("ipAddress").getAsString() != null ? device.get("ipAddress").getAsString()
					: "";

			String merchant = json2.get("merchant").getAsString();

			JsonObject orderJson = json2.getAsJsonObject("order");
			String orderAmount = orderJson.get("amount").getAsString() != null ? orderJson.get("amount").getAsString()
					: "";
			String orderCreationTime = orderJson.get("creationTime").getAsString() != null
					? orderJson.get("creationTime").getAsString()
					: "";
			String orderCurrency = orderJson.get("currency").getAsString() != null
					? orderJson.get("currency").getAsString()
					: "";
			String orderId = orderJson.get("id").getAsString() != null ? orderJson.get("id").getAsString() : "";
			String orderStatus = orderJson.get("status").getAsString() != null ? orderJson.get("status").getAsString()
					: "";

			String orderTotalAuthorizedAmount = orderJson.get("totalAuthorizedAmount").getAsString() != null
					? orderJson.get("totalAuthorizedAmount").getAsString()
					: "";
			String orderTotalCapturedAmount = orderJson.get("totalCapturedAmount").getAsString() != null
					? orderJson.get("totalCapturedAmount").getAsString()
					: "";
			String orderTotalRefundedAmount = orderJson.get("totalRefundedAmount").getAsString() != null
					? orderJson.get("totalRefundedAmount").getAsString()
					: "";

			JsonObject transaction = json2.getAsJsonObject("transaction");
			String trxAmount = transaction.get("amount").getAsString() != null ? transaction.get("amount").getAsString()
					: "";
			String trxCurrency = transaction.get("currency").getAsString() != null
					? transaction.get("currency").getAsString()
					: "";
			String trxFrequency = transaction.get("frequency").getAsString() != null
					? transaction.get("frequency").getAsString()
					: "";
			String trxId = transaction.get("id").getAsString() != null ? transaction.get("id").getAsString() : "";
			String trxReceipt = transaction.get("receipt").getAsString() != null
					? transaction.get("receipt").getAsString()
					: "";
			String trxSource = transaction.get("source").getAsString() != null ? transaction.get("source").getAsString()
					: "";
			String trxTargetTransactionId = transaction.get("targetTransactionId").getAsString() != null
					? transaction.get("targetTransactionId").getAsString()
					: "";
			String trxTerminal = transaction.get("terminal").getAsString() != null
					? transaction.get("terminal").getAsString()
					: "";
			String trxType = transaction.get("type").getAsString() != null ? transaction.get("type").getAsString() : "";

			JsonObject acquirerSuccess = transaction.getAsJsonObject("acquirer");
			String acquirerDate = acquirerSuccess.get("date").getAsString() != null
					? acquirerSuccess.get("date").getAsString()
					: "";
			String acquirerId = acquirerSuccess.get("id").getAsString() != null
					? acquirerSuccess.get("id").getAsString()
					: "";
			String settlementDate = acquirerSuccess.get("settlementDate").getAsString() != null
					? acquirerSuccess.get("settlementDate").getAsString()
					: "";
			String acquirerBatch = acquirerSuccess.get("batch").getAsString() != null
					? acquirerSuccess.get("batch").getAsString()
					: "";
			String timeZone = acquirerSuccess.get("timeZone").getAsString() != null
					? acquirerSuccess.get("timeZone").getAsString()
					: "";
			String acquirerTrxId = acquirerSuccess.get("transactionId").getAsString() != null
					? acquirerSuccess.get("transactionId").getAsString()
					: "";

			String result = json2.get("result").getAsString();
			retval = result;// TODO
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {

			// TODO
			// logwrite.writeLog
			writeLogV2(0L, "EBL VOID GET", 1, "trxid:" + transactionId1 + ",VOID, REQ MAP :" + argsMap.toString());
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		HTTPConnection.disconnect();

		if (logger.isInfoEnabled()) {
			logger.info("ebl -- END");
		}

		return retval;
	}

	/**
	 * 
	 * @param json String
	 * @return only Success portion of transaction
	 */

	/**
	 * 
	 * @param json
	 * @return
	 */
	public String successJsonEbl(String json) {
		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
		return successJsonEbl(jsonObject);
	}

	/**
	 * 
	 * @param jsonObject
	 * @return
	 */
	public String successJsonEbl(JsonObject jsonObject) {
		String retvl = null;
		JsonArray arr = jsonObject.getAsJsonArray("transaction");
		for (int i = 0; i < arr.size(); ++i) {
			String jsonStr = arr.get(i).toString();
			jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
			String jsonResult = jsonObject.getAsJsonObject().get("result").getAsString();
			if (jsonResult.matches("SUCCESS")) {
				retvl = arr.get(i).toString();
			}
		}
		return retvl;
	}/**/

	/**
	 * 
	 * @param ACESI
	 * @param CardBrand
	 * @param Provided_card
	 * @param bank
	 * @return 0 default, 1= accept, -1= reject/void
	 */

	public CardIssuerModel bankCardLogic(String ACESI, String CardBrand, String Provided_card, String bank) {
		// TODO

		CardIssuerModel cardIssuerModel = CheckBin(Provided_card);

		System.out.println("bankCardLogic CardBrand:" + CardBrand);
		int resp = 0;
		if (bank.equals("EBL") || bank.equals("SEBL")) {
			if (CardBrand.equals("MASTERCARD")) {
				if (ACESI.equals("2")) {
					resp = 1;
					// resp=-1;//remove
				} else if (ACESI.equals("1")) {
					// TODO check if international
					resp = -1;// remove
				} else if (ACESI.equals("0")) {
					resp = -1;
				}
			} else

			if (CardBrand.equals("VISA")) {
				if (ACESI.equals("5")) {
					resp = 1;
				} else if (ACESI.equals("6")) {
					// TODO check if international
					resp = -1;// remove
				} else if (ACESI.equals("7")) {
					resp = -1;
				}
			}
		}
		// TODO use EBL logic Here
		cardIssuerModel.setResp_ACSEI(resp);

		return cardIssuerModel;
	}

	public CardIssuerModel CheckBin(String Provided_card) {
		CardIssuerModel cardIssuerModel = null;
		try {
			cardIssuerModel = transactionService.fetchBinDetail(Provided_card);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Payment Controller:checkBin Exception: " + e);
		}

		return cardIssuerModel;
	}

	public boolean InternationCardheck(String provided_card) {
		boolean retval = false;
		String url = "https://api.freebinchecker.com/bin/";
		url = url + provided_card;

		ClientResponse client_response = payconnectApiAcess(url, "", "GET");
		String outputJsonString = client_response.getEntity(String.class);
		/*
		 * JsonObject countryObject = cardObject.getJsonObject("country"); String
		 * country = countryObject.getString("name"); String alpha2Code =
		 * countryObject.getString("alpha2"); System.out.println("Country: ");
		 * System.out.println(country); System.out.println(alpha2Code);/
		 **/

		return retval;
	}

	// WASIF 20190305
	/*************************************************************************************************************************************************/

	/**
	 * This method is used to void payment EBL gateway.
	 * 
	 * @param request
	 * @param model
	 * @param transactionId
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */

	public String eblVoid(HttpServletRequest request, String orderid, TransactionModel tm, String targetTransactionId,
			String transactionId) {

		String retval = "";
		/*
		 * //@RequestMapping(value = "/ebl_void", method = RequestMethod.GET)
		 * 
		 * public String eblVoid(HttpServletRequest request, Model model, @RequestParam
		 * String transactionId,String orderid, HttpServletResponse response, final
		 * RedirectAttributes redirectAttributes) { /
		 **/
		if (logger.isInfoEnabled()) {
			logger.info("ebl void -- START");
		}

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		Map<String, String> argsMap = new HashMap<String, String>();
		Long transactionId1 = 0L;
		TransactionModel transactionModel2 = null;

		try {

			String key = messageUtil.getBundle("secret.key");
			transactionId1 = Long.parseLong(encryption.decode(key, transactionId));
			System.out.println("TransactionId After Decode== >> " + transactionId1);

			try {
				// TODO
				// logwrite.writeLog
				writeLogV2(0L, "EBL void GET", 1, "trxID:" + transactionId1 + ",SESSIONKEY:" + transactionId);
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}
			transactionModel2 = tm;

		} catch (Exception e) {
			e.printStackTrace();

		}

		/**
		 * argsMap.put("apiOperation", "RETRIEVE_ORDER"); argsMap.put("order.id",
		 * orderId); argsMap.put("merchant",
		 * transactionModel2.getMerchantModel().getEblId()); argsMap.put("apiPassword",
		 * transactionModel2.getMerchantModel().getEblPassword());
		 * argsMap.put("apiUsername",
		 * transactionModel2.getMerchantModel().getEblUserName());
		 * 
		 */

		String requestURL = messageUtil.getBundle("ebi.third.url");
//TODO need to change here
		transactionModelSaltTracker.setTxnId(transactionModel2.getTxnId());
		argsMap.put("apiOperation", "VOID");
		argsMap.put("order.id", transactionModel2.getTxnId());
		argsMap.put("transaction.id", transactionModel2.getTxnId());
		argsMap.put("transaction.targetTransactionId", transactionModel2.getTxnId());
		argsMap.put("order.currency", "BDT");
		argsMap.put("order.amount", String.valueOf(transactionModel2.getAmount()));
		argsMap.put("order.description", "Order%20from%20LebuPay");
		// argsMap.put("interaction.returnUrl", basePath + "retriveOrder");
		// argsMap.put("interaction.returnUrl", basePath + "retriveOrder?orderId=" +
		// transactionModel2.getTxnId());
		// argsMap.put("interaction.cancelUrl", basePath + "failure?orderId=" +
		// transactionModel2.getTxnId());

		argsMap.put("merchant", transactionModel2.getMerchantModel().getEblId());
		argsMap.put("apiPassword", transactionModel2.getMerchantModel().getEblPassword());
		argsMap.put("apiUsername", transactionModel2.getMerchantModel().getEblUserName());

		// String requestURL = messageUtil.getBundle("ebi.first.url");

		// TODO argsMap.put("interaction.returnUrl", basePath + "retriveOrder?orderId="
		// + transactionModel2.getTxnId()); //TODO Wasif 20190223

		System.out.println("requestURL ==>> " + requestURL);

		try {
			// TODO
			// logwrite.writeLog
			writeLogV2(0L, "EBL VOID GET", 1, "trxid:" + transactionId1 + ",VOID, REQ MAP :" + argsMap.toString());
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		 * try {
		 * 
		 * HTTPConnection.sendPostRequest(requestURL, argsMap); String[] resp =
		 * HTTPConnection.readMultipleLinesRespone();
		 * 
		 * String[] respArr = resp[0].split("&");
		 * 
		 * HashMap<String, String> respMap = new HashMap<String, String>(); for (String
		 * line : respArr) {
		 * 
		 * String[] respPart = line.split("="); respMap.put(respPart[0], respPart[1]); }
		 * 
		 * try { //TODO //logwrite.writeLog writeLogV2(0L,"EBL VOID GET",2,
		 * "trxid:"+transactionId1+",VOID, resp Map :"+respMap.toString()); } catch
		 * (Exception e1) { // Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * if (respMap.containsKey("result")) { if
		 * (respMap.get("result").equals("SUCCESS")) {
		 * 
		 * System.out.println("SUCCESS"); String secondAPI =
		 * messageUtil.getBundle("ebi.second.url") + respMap.get("session.id");
		 * System.out.println("secondAPI ==>> " + secondAPI);
		 * 
		 * try { //TODO //logwrite.writeLog writeLogV2(0L,"EBL VOID API GET",1,
		 * "trxid:"+transactionId1+",EBL VOID, request session :"+respMap.get(
		 * "session.id")); } catch (Exception e1) { // Auto-generated catch block
		 * e1.printStackTrace(); } //response.sendRedirect(secondAPI); } else {
		 * 
		 * System.out.println("Failure");
		 * //redirectAttributes.addFlashAttribute("failure",
		 * messageUtil.getBundle("transaction.failed")); //return
		 * "redirect:/failure?orderId=" + transactionModel2.getTxnId(); } } else {
		 * 
		 * System.out.println("No Response");
		 * //redirectAttributes.addFlashAttribute("failure",
		 * messageUtil.getBundle("transaction.failed")); //return
		 * "redirect:/failure?orderId=" + transactionModel2.getTxnId(); }
		 * 
		 * } catch (IOException ex) { ex.printStackTrace();
		 * //redirectAttributes.addFlashAttribute("failure",
		 * messageUtil.getBundle("transaction.failed")); //return
		 * "redirect:/failure?orderId=" + transactionModel2.getTxnId(); }/
		 *****/

		HTTPConnection.disconnect();

		if (logger.isInfoEnabled()) {
			logger.info("ebl -- END");
		}

		return retval;
	}
	/**/

	/*************************************************************************************************************************************************/

	/**
	 * This method is used to open success page after completion of EBL payment.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return payment-success.jsp
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String success(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("success -- START");
		}

		try {
			System.out.println("Coming In SUCCESS Section ==>> " + orderId);
			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);

			PaymentModel paymentModel1 = transactionService.fetchTransactionByTXNId_detail(orderId);// TODO accommodate
																									// get Customer
																									// detail
			System.out.println("transactionModel success ==>> " + paymentModel1);

			if (Objects.nonNull(paymentModel1)) {
				PaymentModel paymentModel = new PaymentModel();
				Gson gson = new Gson();
				// JSON to Java object, read it from a Json String.
				paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
						PaymentModel.class);
				// paymentModel2=transactionModel.getPaymentModel(); //TODO 20190319
				// TODO need to change here

				System.out.println("paymentModel success ==>> " + paymentModel);
				/*
				 * if (Objects.nonNull(paymentModel1.getSuccessURL()))
				 * System.out.println("Success URL:" + paymentModel1.getSuccessURL());/
				 **/

				if (!paymentModel1.getSuccessURL().equals("None")) {
					response.sendRedirect(paymentModel1.getSuccessURL());

					// Added By Wasif 20190314
					if (!paymentModel1.getServerSuccessURL().equals("NotSet")) {
						// response.sendRedirect(transactionModel.getPaymentModel().getServerSuccessURL());
						System.out.println("Server Success URL:" + paymentModel1.getServerSuccessURL());

						String inputJsonString = "{\"email\":\"" + paymentModel1.getEmailId()
								+ "\", \"mobileNumber\":\"" + paymentModel1.getMobileNumber() + "\", \"amount\":\""
								+ paymentModel1.getAmount() + "\", \"orderTransactionID\":\""
								+ paymentModel1.getOrderTransactionID() + "\", \"responseCode\":\""
								+ paymentModel1.getResponseCode() + "\", \"transactionStatus\":\""
								+ paymentModel1.getTransactionStatus() + "\", \"transactionDate\":\""
								+ paymentModel1.getTransactionDate() + "\", \"card_brand\":\""
								+ paymentModel1.getCard_brand() + "\", \"provided_card_number\":\""
								+ paymentModel1.getProvided_card_number() + "\", \"bank_merchant_id\":\""
								+ paymentModel1.getBank_merchant_id() + "\", \"bkash_payment_number\":\""
								+ paymentModel1.getBkash_customer() + "\", \"transaction_type\":\""
								+ paymentModel1.getTransaction_type() + "\", \"transactionStatus\":\""
								+ paymentModel1.getTransactionStatus() + "\", \"billing_name\":\""
								+ paymentModel1.getBilling_name() + "\", \"bank\":\"" + paymentModel1.getBank() + "\"}";

						// TODO Check card location

						System.out.println("inputJsonString ==>> " + inputJsonString);
						ClientResponse client_response = payconnectApiAcess(paymentModel1.getServerSuccessURL(),
								inputJsonString, "post");

						try {
							// logwrite.writeLog(transactionModel.getMerchantModel().getMerchantId(),"Citybank
							// approveOrder",1, "approveOrder cityTrxModelUpd
							// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
							writeLogV2(transactionModel.getMerchantModel().getMerchantId(), "server-server API", 2,
									"on success ebl order trx ID:"
											+ transactionModel.getPaymentModel().getOrderTransactionID() + ",response:"
											+ client_response);
						} catch (Exception e1) {
							// Auto-generated catch block
							e1.printStackTrace();
						}
						// TODO
						/*
						 * String issuer_check_url="https://lookup.binlist.net/"; //TO CHECK CARD issuer
						 * detail TPS 10 per min String
						 * full_url=issuer_check_url+transactionModel.getPaymentModel().
						 * getProvided_card_number().substring(0,6); ClientResponse card_issuer_detail =
						 * payconnectApiAcess(full_url,"", "GET");
						 * 
						 * try{
						 * System.out.println("card_issuer_detail resp: "+card_issuer_detail.toString())
						 * ; //TODO testing }catch(Exception e){ e.printStackTrace(); } //TODO String
						 * outputJsonString2 = card_issuer_detail.getEntity(String.class);
						 * System.out.println("outputJsonString card issuer url check ==>> " +
						 * outputJsonString2); Gson gson2 = new Gson();
						 * 
						 * CardIssuerModel cardIssuerModel =
						 * gson2.fromJson(outputJsonString2,CardIssuerModel.class);
						 * 
						 * if(cardIssuerModel.getAlpha2()!="BD") { cardIssuerModel.setOriginType(0);
						 * }else { cardIssuerModel.setOriginType(1); }
						 * 
						 * 
						 * 
						 * /
						 ***/
					} else {

						System.out.println("Server Side URL not set");

					}

				}

			}
			try {
				writeLogV2(0L, "success", 2, "orderId:" + orderId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("success -- END");
		}

		return "payment-success";
	}

	// SEBL STARTS HERE

	/**********************************************************************************************/

	/**
	 * This method is used to open payment page for SEBL gateway.
	 * 
	 * @param request
	 * @param model
	 * @param transactionId
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/sebl", method = RequestMethod.GET)
	public String sebl(HttpServletRequest request, Model model, @RequestParam String transactionId,
			HttpServletResponse response, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("sebl -open payment page- START");
		}

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		Map<String, String> argsMap = new HashMap<String, String>();
		Long transactionId1 = 0L;
		TransactionModel transactionModel2 = null;

		try {

			System.out.println("SESSIONKEY Before Decode== >> " + transactionId);
			String key = messageUtil.getBundle("secret.key");
			transactionId1 = Long.parseLong(encryption.decode(key, transactionId));
			System.out.println("TransactionId After Decode== >> " + transactionId1);

			transactionModel2 = transactionService.fetchTransactionById(transactionId1);

		} catch (Exception e) {

			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;
		}

		if (Objects.isNull(transactionModel2)) {
			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;

		} else {
			if (transactionModel2.getTransactionStatus() == 0 || transactionModel2.getTransactionStatus() == 1
					|| transactionModel2.getTransactionStatus() == 2) { // Already Paid
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("already.paid"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();

			} else if (transactionModel2.getTransactionStatus() == 3) { // Failed
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();

			} else if (transactionModel2.getTransactionStatus() == 7) { // expired

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();

			}
			if (transactionModel2.getIsValid()) { // valid, do nothing
			} else {
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();

			}
		}

		transactionModelSaltTracker.setTxnId(transactionModel2.getTxnId());
		argsMap.put("apiOperation", "CREATE_CHECKOUT_SESSION");
		argsMap.put("order.id", transactionModel2.getTxnId());
		argsMap.put("order.currency", "BDT");
		argsMap.put("order.amount", String.valueOf(transactionModel2.getAmount()));
		argsMap.put("order.description", "Order%20from%20LebuPay");
		argsMap.put("interaction.displayControl.billingAddress", "HIDE");
		argsMap.put("interaction.merchant.name", "Lebupay");
		argsMap.put("interaction.displayControl.orderSummary", "HIDE");
		argsMap.put("interaction.returnUrl", basePath + "retriveOrderSEBL?orderId=" + transactionModel2.getTxnId());
		argsMap.put("interaction.cancelUrl", basePath + "failure?orderId=" + transactionModel2.getTxnId());

		argsMap.put("merchant", transactionModel2.getMerchantModel().getSeblId());
		argsMap.put("apiPassword", transactionModel2.getMerchantModel().getSeblPassword());
		argsMap.put("apiUsername", transactionModel2.getMerchantModel().getSeblUserName());

		String requestURL = messageUtil.getBundle("sebl.first.url");

		try {

			HTTPConnection.sendPostRequest(requestURL, argsMap);
			String[] resp = HTTPConnection.readMultipleLinesRespone();

			String[] respArr = resp[0].split("&");

			HashMap<String, String> respMap = new HashMap<String, String>();
			for (String line : respArr) {

				String[] respPart = line.split("=");
				respMap.put(respPart[0], respPart[1]);
			}
			if (respMap.containsKey("result")) {
				if (respMap.get("result").equals("SUCCESS")) {
					System.out.println("SEBL first API SUCCESS");
					String secondAPI = messageUtil.getBundle("sebl.second.url") + respMap.get("session.id");
					System.out.println("SEBL secondAPI ==>> " + secondAPI);
					logger.info("SEBL secondAPI ==>> " + secondAPI);
					response.sendRedirect(secondAPI);
				} else {

					System.out.println("SEBL Failure");
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
					return "redirect:/failure?orderId=" + transactionModel2.getTxnId();
				}
			} else {

				System.out.println("SEBL No Response");
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
			return "redirect:/failure?orderId=" + transactionModel2.getTxnId();
		}

		HTTPConnection.disconnect();

		if (logger.isInfoEnabled()) {
			logger.info("sebl -open payment page- START");
		}

		return null;
	}

	/**
	 * This method is used to get Response from SEBL gateway after Payment.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */

	@RequestMapping(value = "/retriveOrderSEBL", method = RequestMethod.GET)
	public String retriveOrderSEBL(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId, final RedirectAttributes redirectAttributes) {
		System.out.println("SEBl retriveOrderSEBL entered");
		if (logger.isInfoEnabled()) {
			logger.info("retriveOrderSEBL -- START");
		}

		String returnURL = "";
		String resultIndicator = (String) request.getParameter("resultIndicator");
		System.out.println("resultIndicator ==>> " + resultIndicator);
		/* String orderId = String.valueOf(transactionModelSaltTracker.getTxnId()); */

		try {
			// logwrite.writeLog
			writeLogV2(0L, "SEBL RETRIEVE_ORDER GET", 1, "orderId:" + orderId + ",resultIndicator :" + resultIndicator);
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		TransactionModel transactionModel2 = null;
		try {

			transactionModel2 = transactionService.fetchTransactionByTXNId(orderId);
			if (Objects.isNull(transactionModel2)) {
				throw new Exception();
			}

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
			returnURL = "redirect:/failure?orderId=" + orderId;
		}

		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("apiOperation", "RETRIEVE_ORDER");
		argsMap.put("order.id", orderId);
		argsMap.put("merchant", transactionModel2.getMerchantModel().getSeblId());
		argsMap.put("apiPassword", transactionModel2.getMerchantModel().getSeblPassword());
		argsMap.put("apiUsername", transactionModel2.getMerchantModel().getSeblUserName());

		String requestURL = messageUtil.getBundle("sebl.third.url");

		try {

			HTTPConnection.sendPostRequest(requestURL, argsMap);

			String[] resp = HTTPConnection.readMultipleLinesRespone();

			String[] respArr = resp[0].split("&");

			HashMap<String, String> respMap = new HashMap<String, String>();
			for (String line : respArr) {
				String[] respPart = line.split("=");

				respMap.put(respPart[0], respPart[1]);
			}
			try {
				// logwrite.writeLog
				writeLogV2(0L, "SEBL RETRIEVE_ORDER POST", 2, "respMap :" + respMap.toString());
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}

			for (Map.Entry<String, String> entry : respMap.entrySet()) {
				System.out.println("Key ==>> " + entry.getKey() + "  Value ==>> " + entry.getValue());
			}

			if (respMap.containsKey("result")) {
				if (respMap.get("result").equals("SUCCESS")) {
					System.out.println("SUCCESS SEBL ");
					System.err.println("orderId ==>> " + orderId);

					int result = 0;
					try {

						TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);

						transactionModel.setAuthorizationResponse_date(
								respMap.get("transaction%5B0%5D.authorizationResponse.date"));
						transactionModel.setTotalCapturedAmount(respMap.get("totalCapturedAmount"));
						transactionModel.setFundingMethod(respMap.get("sourceOfFunds.provided.card.fundingMethod"));
						transactionModel.setAcquirerMessage(respMap.get("transaction%5B0%5D.response.acquirerMessage"));
						transactionModel.setFinancialNetworkCode(
								respMap.get("transaction%5B0%5D.authorizationResponse.financialNetworkCode"));
						transactionModel.setTransactionIdentifier(
								respMap.get("transaction%5B0%5D.authorizationResponse.transactionIdentifier"));
						transactionModel.setNameOnCard(respMap.get("sourceOfFunds.provided.card.nameOnCard"));
						transactionModel.setCard_expiry_year(
								respMap.get("transaction%5B0%5D.sourceOfFunds.provided.card.expiry.year"));
						transactionModel.setAuthorizationResponse_time(
								respMap.get("transaction%5B0%5D.authorizationResponse.time"));
						transactionModel
								.setTransaction_currency(respMap.get("transaction%5B0%5D.transaction.currency"));
						transactionModel.setSecureId(respMap.get("transaction%5B0%5D.3DSecureId"));
						transactionModel.setAcquirerCode(
								respMap.get("transaction%5B0%5D.response.cardSecurityCode.acquirerCode"));
						transactionModel.setAuthorizationResponse_stan(
								respMap.get("transaction%5B0%5D.authorizationResponse.stan"));
						transactionModel.setMerchantId(respMap.get("transaction%5B0%5D.merchant"));
						transactionModel.setTotalAuthorizedAmount(respMap.get("totalAuthorizedAmount"));
						transactionModel.setProvided_card_number(
								respMap.get("transaction%5B0%5D.sourceOfFunds.provided.card.number"));
						transactionModel.setCardSecurityCode_gatewayCode(
								respMap.get("transaction%5B0%5D.response.cardSecurityCode.gatewayCode"));
						transactionModel
								.setAuthenticationToken(respMap.get("transaction%5B0%5D.3DSecure.authenticationToken"));
						transactionModel.setTransaction_receipt(respMap.get("transaction%5B0%5D.transaction.receipt"));
						transactionModel
								.setResponse_gatewayCode(respMap.get("transaction%5B0%5D.response.gatewayCode"));
						transactionModel.setOrder_status(respMap.get("transaction%5B0%5D.order.status"));
						transactionModel.setAcquirer_date(respMap.get("transaction%5B0%5D.transaction.acquirer.date"));
						transactionModel.setVersion(respMap.get("transaction%5B0%5D.version"));
						transactionModel.setCommercialCardIndicator(
								respMap.get("transaction%5B0%5D.authorizationResponse.commercialCardIndicator"));
						transactionModel
								.setCard_brand(respMap.get("transaction%5B0%5D.sourceOfFunds.provided.card.brand"));
						transactionModel.setSourceOfFunds_type(respMap.get("sourceOfFunds.type"));
						transactionModel.setCustomer_firstName(respMap.get("customer.firstName"));
						transactionModel.setCustomer_lastName(respMap.get("customer.lastName"));
						transactionModel.setDevice_browser(respMap.get("transaction%5B0%5D.device.browser"));
						transactionModel.setDevice_ipAddress(respMap.get("device.ipAddress"));
						transactionModel.setAcsEci_value(respMap.get("transaction%5B0%5D.3DSecure.acsEci"));
						transactionModel.setAcquirer_id(respMap.get("transaction%5B0%5D.transaction.acquirer.id"));
						transactionModel.setSettlementDate(
								respMap.get("transaction%5B0%5D.transaction.acquirer.settlementDate"));
						transactionModel.setTransaction_source(respMap.get("transaction%5B0%5D.transaction.source"));
						transactionModel.setResult(respMap.get("transaction%5B0%5D.result"));
						transactionModel.setCreationTime(respMap.get("creationTime"));
						transactionModel
								.setTotalRefundedAmount(respMap.get("transaction%5B0%5D.order.totalRefundedAmount"));
						transactionModel
								.setAcquirer_batch(respMap.get("transaction%5B0%5D.transaction.acquirer.batch"));
						transactionModel.setDescription(respMap.get("description"));
						transactionModel.setTransaction_type(respMap.get("transaction%5B0%5D.transaction.type"));
						transactionModel.setFinancialNetworkDate(
								respMap.get("transaction%5B0%5D.authorizationResponse.financialNetworkDate"));
						transactionModel
								.setResponseCode(respMap.get("transaction%5B0%5D.authorizationResponse.responseCode"));
						transactionModel
								.setTransaction_frequency(respMap.get("transaction%5B0%5D.transaction.frequency"));
						transactionModel
								.setTransaction_terminal(respMap.get("transaction%5B0%5D.transaction.terminal"));
						transactionModel
								.setAuthorizationCode(respMap.get("transaction%5B0%5D.transaction.authorizationCode"));
						transactionModel.setAuthenticationStatus(
								respMap.get("transaction%5B0%5D.3DSecure.authenticationStatus"));
						transactionModel.setProcessingCode(
								respMap.get("transaction%5B0%5D.authorizationResponse.processingCode"));
						transactionModel.setExpiry_month(respMap.get("sourceOfFunds.provided.card.expiry.month"));
						transactionModel.setSecure_xid(respMap.get("transaction%5B0%5D.3DSecure.xid"));
						transactionModel
								.setEnrollmentStatus(respMap.get("transaction%5B0%5D.3DSecure.enrollmentStatus"));
						transactionModel.setCardSecurityCodeError(
								respMap.get("transaction%5B0%5D.authorizationResponse.cardSecurityCodeError"));
						transactionModel.setTimeZone(respMap.get("transaction%5B0%5D.transaction.acquirer.timeZone"));
						transactionModel.setGatewayEntryPoint(respMap.get("transaction%5B0%5D.gatewayEntryPoint"));
						transactionModel.setBank("SEBL");

						result = transactionServiceImpl.updateTransactionAfterPayment(transactionModel);
						int seblUpdateResult = 0;
						if (result > 0) {

							seblUpdateResult = transactionServiceImpl
									.updateSEblTransactionAfterPayment(transactionModel);
							if (seblUpdateResult > 0) {

								returnURL = "redirect:/success?orderId=" + orderId;

								System.out.println("Success Transaction Model-->" + transactionModel);

								if (Objects.nonNull(transactionModel)) {

									PaymentModel paymentModel = new PaymentModel();
									Gson gson = new Gson();

									paymentModel = transactionModel.getPaymentModel();
									String sms_notifictaion = paymentModel.getNotification_sms();
									String email_notification = paymentModel.getNotification_email();
									paymentModel = gson.fromJson(paymentModel.getCustomerDetails(), PaymentModel.class);
									String customerDetails = paymentModel.getCustomerDetails();
									String firstName = decode(customerDetails, "firstName");
									String lastName = decode(customerDetails, "lastName");
									// JSON to Java object, read it from a Json String.
									/*
									 * paymentModel = gson.fromJson(
									 * transactionModel.getPaymentModel().getCustomerDetails(), PaymentModel.class);
									 * System.out.println("paymentModel ==>> " + paymentModel);
									 * 
									 * String sms_notifictaion=paymentModel.getNotification_sms(); String
									 * email_notification=paymentModel.getNotification_email();/
									 **/
									if (email_notification.equals("1")) {
										if (Objects.nonNull(paymentModel.getEmailId())) {

											// Send Email
											String action = "transactionSuccess";

											String header = "Transaction successful";

											Double amount = transactionModel.getGrossAmount();

											String emailMessageBody = "<p>Dear Sir/Madam!</p><p>We have successfully received "
													+ amount + " BDT. </p> <p>Thank You for paying with LEBUPAY</p>";

											String subject = messageUtil.getBundle("transaction.email.subject");

											EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
											emailInvoicingModel.setAction(action);
											emailInvoicingModel.setHeader(header);
											emailInvoicingModel.setAmount("" + transactionModel.getGrossAmount());
											emailInvoicingModel.setEmailMessageBody(emailMessageBody);
											emailInvoicingModel.setSubject(subject);
											emailInvoicingModel.setEmailId(paymentModel.getEmailId());
											emailInvoicingModel.setFirstName(firstName);
											emailInvoicingModel.setLastName(lastName);
											emailInvoicingModel.setCardType(transactionModel.getCard_brand());
											emailInvoicingModel.setCardNumber(transactionModel.getProvided_card_number());
											emailInvoicingModel.setNameOnCard(transactionModel.getNameOnCard());
											emailInvoicingModel.setBankTrxID(transactionModel.getTransaction_receipt());
											emailInvoicingModel.setMobileNumber(paymentModel.getMobileNumber());
											emailInvoicingModel.setTrxID(orderId);
											emailInvoicingModel.setTransactionType(transactionModel.getTransaction_type());
											//emailInvoicingModel.setIPAddress(ipAddress);
											emailInvoicingModel.setCurrency(transactionModel.getTransaction_currency());
											emailInvoicingModel.setIsTemplate(true);

											spiderEmailSender.sendEmail(emailInvoicingModel);

											// sendMail.send(paymentModel.getEmailId(), messageBody, subject);

										}
									}

									if (sms_notifictaion.equals("1")) {
										if (Objects.nonNull(paymentModel.getMobileNumber())) {
											sendSMS.smsSend(paymentModel.getMobileNumber(), "We have received "
													+ transactionModel.getGrossAmount()
													+ " BDT. Thank You for paying with LEBUPAY. Visit www.we-top-up.com for mobile recharge.");
										}
									}

								}
							} else {

								System.out.println("Failure");
								redirectAttributes.addFlashAttribute("failure",
										messageUtil.getBundle("something.went.wrong"));
								returnURL = "redirect:/failure?orderId=" + orderId;
							}
						} else {

							System.out.println("Failure");
							redirectAttributes.addFlashAttribute("failure",
									messageUtil.getBundle("something.went.wrong"));
							returnURL = "redirect:/failure?orderId=" + orderId;
						}

					} catch (Exception e) {
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
						returnURL = "redirect:/failure?orderId=" + orderId;
					}

				} else {
					logger.info("sebl - response:  Failure ");
					System.out.println("Failure");
					redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
					returnURL = "redirect:/failure?orderId=" + orderId;
				}
			} else {
				logger.info("sebl - response: contains No response");
				System.out.println("No Response");
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				returnURL = "redirect:/failure?orderId=" + orderId;
			}

			HTTPConnection.disconnect();

		} catch (Exception ex) {
			ex.printStackTrace();
			redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
			returnURL = "redirect:/failure?orderId=" + orderId;
		}

		if (logger.isInfoEnabled()) {
			logger.info("retriveOrderSEBL -- END");
		}

		return returnURL;
	}

	/***********************************************************************************************/
	// SEBL ENDS

	/**
	 * This method is used to open failure page if payment is failed.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return payment-failure.jsp
	 */
	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public String failure(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("failure -- START");
		}

		try {

			System.out.println("Coming In FAILURE Section ==>> " + orderId);
			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);
			PaymentModel paymentModel1 = transactionService.fetchTransactionByTXNId_detail(orderId);// TODO accommodate
																									// get Customer
																									// detail

			if (Objects.nonNull(transactionModel)) {
				try {
					writeLogV2(0L, "failure", 1, "orderId:" + orderId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (transactionModel.getTransactionStatus() != 0) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 3,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					// shaker move from here
					// added by Shaker on 16.04.2019
				}
				// shaker move start
				if (Objects.nonNull(paymentModel1.getFailureURL()))
					if (!paymentModel1.getFailureURL().equals("None")) {
						response.sendRedirect(transactionModel.getPaymentModel().getFailureURL());
						// TODO Added By Wasif 20190314
						if (!paymentModel1.getServerFailureURL().equals("NotSet")) {
							// response.sendRedirect(transactionModel.getPaymentModel().getServerSuccessURL());

							String inputJsonString = "{\"email\":\"" + paymentModel1.getEmailId()
									+ "\", \"mobileNumber\":\"" + paymentModel1.getMobileNumber() + "\", \"amount\":\""
									+ paymentModel1.getAmount() + "\", \"orderTransactionID\":\""
									+ paymentModel1.getOrderTransactionID() + "\", \"transactionStatus\":\"" + "Failure"
									+ "\"}";

							System.out.println("inputJsonString ==>> " + inputJsonString);
							ClientResponse client_response = payconnectApiAcess(paymentModel1.getServerFailureURL(),
									inputJsonString, "post");

							try {
								// logwrite.writeLog(transactionModel.getMerchantModel().getMerchantId(),"Citybank
								// approveOrder",1, "approveOrder cityTrxModelUpd
								// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
								writeLogV2(transactionModel.getMerchantModel().getMerchantId(), "server-server API", 2,
										"on failure order trx ID:"
												+ transactionModel.getPaymentModel().getOrderTransactionID()
												+ ",response:" + client_response);

							} catch (Exception e1) {
								// Auto-generated catch block
								e1.printStackTrace();
							}
							/***/
						}
					}
				// shaker move end
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("failure -- END");
		}

		return "payment-failure";
	}

	// TODO WASIF & AJMAIN change the fixed status value , currently hard coded to 6
	// , make it variable
	@RequestMapping(value = "/failure_void", method = RequestMethod.GET)
	public String failure_void(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("failure_void -- START");
		}

		try {

			System.out.println("Coming In failure_void Section ==>> " + orderId);
			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);
			PaymentModel paymentModel1 = transactionService.fetchTransactionByTXNId_detail(orderId);// TODO accommodate
																									// get Customer
																									// detail

			if (Objects.nonNull(transactionModel)) {
				try {
					writeLogV2(0L, "failure_void", 1, "orderId:" + orderId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (transactionModel.getTransactionStatus() != 0) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 6,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					if (Objects.nonNull(paymentModel1.getFailureURL()))
						if (!paymentModel1.getFailureURL().equals("None")) {
							response.sendRedirect(transactionModel.getPaymentModel().getFailureURL());
							// TODO Added By Wasif 20190314
							if (!paymentModel1.getServerFailureURL().equals("NotSet")) {
								// response.sendRedirect(transactionModel.getPaymentModel().getServerSuccessURL());

								String inputJsonString = "{\"email\":\"" + paymentModel1.getEmailId()
										+ "\", \"mobileNumber\":\"" + paymentModel1.getMobileNumber()
										+ "\", \"amount\":\"" + paymentModel1.getAmount()
										+ "\", \"orderTransactionID\":\"" + paymentModel1.getOrderTransactionID()
										+ "\", \"transactionStatus\":\"" + "Failure" + "\"}";

								System.out.println("inputJsonString ==>> " + inputJsonString);
								ClientResponse client_response = payconnectApiAcess(paymentModel1.getServerFailureURL(),
										inputJsonString, "post");

								try {
									// logwrite.writeLog(transactionModel.getMerchantModel().getMerchantId(),"Citybank
									// approveOrder",1, "approveOrder cityTrxModelUpd
									// txnId:"+txnId+",purchaseAmount:"+purchaseAmount+",merchantTransId:"+merchantTransId);
									writeLogV2(transactionModel.getMerchantModel().getMerchantId(), "server-server API",
											2,
											"on failure order trx ID:"
													+ transactionModel.getPaymentModel().getOrderTransactionID()
													+ ",response:" + client_response);

								} catch (Exception e1) {
									// Auto-generated catch block
									e1.printStackTrace();
								}
								/***/
							}
						}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("failure_void -- END");
		}

		return "payment-failure";
	}

	/**
	 * This method is used for Hit from API. Initial Call
	 * 
	 * @param paymentModel
	 * @param request
	 * @return PaymentModel
	 */
	@RequestMapping(value = "/check-payment", method = RequestMethod.POST)
	public @ResponseBody PaymentModel checkPayment(@RequestBody PaymentModel paymentModel, HttpServletRequest request) {

		if (logger.isInfoEnabled()) {
			// logger.info("checkPayment -- START");
			// TODO 20190220
			logger.info("checkPayment -- START for " + paymentModel.getOrderTransactionID() + " Amount: "
					+ (paymentModel.getAmount() != null ? paymentModel.getAmount() : null));

		}

		try {
			// logwrite.writeLog
			writeLogV2(0L, "check-payment", 1,
					"Start orderTxnId:" + paymentModel.getOrderTransactionID() + ",Amount:" + paymentModel.getAmount());

		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			String ipAddress = "";
			try {

				ipAddress = request.getHeader("X-FORWARDED-FOR");
				if (ipAddress == null) {
					ipAddress = request.getRemoteAddr();
					System.out.println("ipAddress ==>> " + ipAddress);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Check Payment-->" + paymentModel);
			String token = UUID.randomUUID().toString();
			paymentModel.setToken(token);

			try {
				// logwrite.writeLog
				writeLogV2(0L, "check-payment", 1, "orderTxnId:" + paymentModel.getOrderTransactionID() + ",Amount:"
						+ paymentModel.getAmount() + ",token:" + token);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			long result = transactionService.insertOrder(paymentModel);
			// System.out.println("paymentModel.getMerchantModel().getCompanyModel().getIp()
			// ==>> "
			// + paymentModel.getMerchantModel().getCompanyModel().getIp());//TODO dont find
			// any necessity
			if (Objects.nonNull(paymentModel))
				if (Objects.nonNull(paymentModel.getMerchantModel()))
					if (Objects.nonNull(paymentModel.getMerchantModel().getCompanyModel()))
						if (Objects.nonNull(paymentModel.getMerchantModel().getCompanyModel().getIp()))
							if (!ipAddress.equals(paymentModel.getMerchantModel().getCompanyModel().getIp()))
								throw new NumericException(messageUtil.getBundle("ip.not.matched"));

			paymentModel = new PaymentModel();
			paymentModel.setToken(token);
			try {
				// logwrite.writeLog
				writeLogV2(0L, "check-payment", 1, "order txnId:" + paymentModel.getOrderTransactionID() + ",Amount:"
						+ paymentModel.getAmount() + ",token:" + token);

			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}
			if (result > 0) {
				paymentModel.setResponseCode("200");
				paymentModel.setResponseMessage(messageUtil.getBundle("checkpayment.success"));
			} else {
				paymentModel.setResponseCode("201");
				paymentModel.setResponseMessage(messageUtil.getBundle("checkpayment.failure"));
			}

		} catch (NumericException ne) {

			paymentModel.setResponseCode(messageUtil.getBundle("ip.not.matched.code"));
			paymentModel.setResponseMessage(ne.getMessage());

		} catch (FormExceptions fe) {

			fe.printStackTrace();
			for (Entry<String, Exception> entry : fe.getExceptions().entrySet()) {

				paymentModel.setResponseCode(entry.getKey());
				paymentModel.setResponseMessage(entry.getValue().getMessage());

				System.out.println(
						"Form Exception :: " + entry.getValue().getMessage() + "==== KEY ==== " + entry.getKey());
				break;
			}

		} catch (Exception e) {

			paymentModel.setResponseCode("201");
			paymentModel.setResponseMessage(messageUtil.getBundle("checkpayment.failure"));
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("checkPayment -- END");
		}

		return paymentModel;
	}

	/**
	 * This method is used to execute payment after Hit from Api.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @param token
	 * @param redirectAttributes
	 * @param response
	 * @return String
	 */
	@RequestMapping(value = "/execute-payment", method = RequestMethod.GET)
	public String executePayment(HttpServletRequest httpServletRequest, Model model, @RequestParam String token,
			final RedirectAttributes redirectAttributes, HttpServletResponse response) {

		if (logger.isInfoEnabled()) {
			logger.info("executePayment -- START");
		}

		String ipAddress = "";
		String sessionKey = "";
		try {

			ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = httpServletRequest.getRemoteAddr();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		PaymentModel paymentModel = null;

		try {

			paymentModel = transactionService.fetchOrderByToken(token);
			if (Objects.nonNull(paymentModel)) {
				// TOOD WASIF 20190217 block unidentified block seems like no use currently may
				// be used for test purpose
				// so that only
				/*
				 * CompanyModel companyModel = merchantService
				 * .fetchCompanyByMerchantId(paymentModel.getMerchantModel().getMerchantId());
				 * if (Objects.nonNull(companyModel)) if (Objects.nonNull(companyModel.getIp()))
				 * if (!companyModel.getIp().equals(ipAddress)) throw new
				 * NumericException(messageUtil.getBundle("ip.not.matched")); /
				 **/
				String customerDetails = paymentModel.getCustomerDetails();
				Gson gson = new Gson();
				PaymentModel paymentModel2 = gson.fromJson(customerDetails, PaymentModel.class);
				paymentModel.setFirstName(paymentModel2.getFirstName());
				paymentModel.setLastName(paymentModel2.getLastName());
				paymentModel.setMobileNumber(paymentModel2.getMobileNumber());
				paymentModel.setEmailId(paymentModel2.getEmailId());

				model.addAttribute("paymentModel", paymentModel);
				String key = messageUtil.getBundle("secret.key");
				TransactionModel transactionModel = new TransactionModel();
				MerchantModel merchantModel = merchantService
						.fetchActiveMerchantById(paymentModel.getMerchantModel().getMerchantId());
				transactionModel.setMerchantModel(merchantModel);
				transactionModel.setAmount(paymentModel.getAmount());
				transactionModel.setOrder_id(paymentModel.getOrderID().toString());
				Long transactionId = transactionService.insertTransaction(transactionModel);
				if (transactionId > 0) {

					sessionKey = encryption.encode(key, String.valueOf(transactionId)); // Modified

					model.addAttribute("SESSIONKEY", sessionKey); // Modified

					try {
						// logwrite.writeLog
						writeLogV2(0L, "execute-payment", 1,
								"trxId:" + transactionId + ",token:" + token + ",sessionKey:" + sessionKey);

					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						CheckoutModel checkoutModel = checkOutService
								.fetchCheckoutById(paymentModel.getMerchantModel().getMerchantId());
						model.addAttribute("checkoutModel", checkoutModel);

					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("Error in fetchCheckoutById");
					}

					try {
						List<ParameterModel> parameterModels = checkOutService
								.fetchParametersById(paymentModel.getMerchantModel().getMerchantId());

						model.addAttribute("parameterModels", parameterModels);
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("Error in fetchParametersById");
					}

				}
			} else {

				Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
				exceptions.put(messageUtil.getBundle("token.invalid.code"),
						new EmptyValueException(messageUtil.getBundle("token.invalid")));
				if (exceptions.size() > 0)
					throw new FormExceptions(exceptions);
			}

		} catch (NumericException ne) {

			paymentModel.setResponseCode(messageUtil.getBundle("ip.not.matched.code"));
			paymentModel.setResponseMessage(ne.getMessage());
			try {
				response.sendRedirect(paymentModel.getFailureURL());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FormExceptions fe) {

			fe.printStackTrace();
			try {
				response.sendRedirect(paymentModel.getFailureURL());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendRedirect(paymentModel.getFailureURL());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("executePayment -- END");
		}

		if (paymentModel.getOrderTransactionID() == null || paymentModel.getOrderTransactionID().trim().equals("")) {
			return "quickpay-checkout";
		} else {

			httpServletRequest.setAttribute("SESSIONKEY", sessionKey);
			httpServletRequest.setAttribute("csrfPreventionSalt",
					httpServletRequest.getAttribute("csrfPreventionSaltPage"));

			return "forward:/checkout-payment";
		}
	}

	/**
	 * This method is used for Check Out Payment.
	 * 
	 * @param paymentModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/checkout-payment", method = { RequestMethod.POST, RequestMethod.GET })
	public String checkoutPayment(@ModelAttribute PaymentModel paymentModel, HttpServletRequest httpServletRequest,
			final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("checkoutPayment -- START");
		}

		if (httpServletRequest.getAttribute("SESSIONKEY") != null) {
			paymentModel.setSESSIONKEY((String) httpServletRequest.getAttribute("SESSIONKEY"));
			paymentModel.setCsrfPreventionSalt((String) httpServletRequest.getAttribute("csrfPreventionSalt"));
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {

			salt = paymentModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				try {
					String key = messageUtil.getBundle("secret.key");
					Long transactionId = Long.parseLong(encryption.decode(key, paymentModel.getSESSIONKEY()));
					TransactionModel transactionModel = transactionService.fetchTransactionById(transactionId);

					try {
						// logwrite.writeLog
						writeLogV2(0L, "checkout-payment", 1, "trxID:" + transactionId + ",sessionId:" + sessionId
								+ ",SESSIONKEY:" + (String) httpServletRequest.getAttribute("SESSIONKEY"));

					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}

					if (Objects.isNull(transactionModel))
						redirectAttributes.addFlashAttribute("transactionidInvalid",
								messageUtil.getBundle("transactionid.invalid"));
					else {
						String bkash = "N";
						
						paymentModel.setOrderID(Long.parseLong(transactionModel.getOrder_id()));
						
						// shaker TO-DO BANK REDIRECT on 20190428
						if(transactionModel.getPaymentModel().getPaymentMethod().equals("1")) {	// bkash selected through API
							bkash = "Y";
						} else if(transactionModel.getPaymentModel().getPaymentMethod().equals("0") && transactionModel.getMerchantModel().getApiGatewaySelection().equals("1")) {	// bank selected through API
							String city = "N";
							String ebl = "N";
							String sebl = "N";
							
							
							//	fetching allowed banks
							if (transactionModel.getMerchantModel().getCityMerchantId() != null
									&& !transactionModel.getMerchantModel().getCityMerchantId().isEmpty()) {
								city = "Y";
							}
							if (transactionModel.getMerchantModel().getEblUserName() != null
									&& !transactionModel.getMerchantModel().getEblUserName().isEmpty()) {
								ebl = "Y";
							}
							if (transactionModel.getMerchantModel().getSeblUserName() != null
									&& !transactionModel.getMerchantModel().getSeblUserName().isEmpty()) {
								sebl = "Y";
							}
							System.out.println("Bank flags : \nbkash : "+bkash +"\nebl : " + ebl +"\ncity : "+ city +"\nsebl : "+ sebl);
							
							//	redirecting to merchant desired banks skipping the linkpay page
							if(transactionModel.getPaymentModel().getGateway().equalsIgnoreCase("EBL") && ebl.equals("Y")) {
								System.out.println("redirecting to EBL");
								return "redirect:/ebl?transactionId="+paymentModel.getSESSIONKEY();
							}
							if(transactionModel.getPaymentModel().getGateway().equalsIgnoreCase("CITY") && city.equals("Y")) {
								System.out.println("redirecting to CITY");
								return "redirect:/paycitybank?transactionId="+paymentModel.getSESSIONKEY();
							}
							if(transactionModel.getPaymentModel().getGateway().equalsIgnoreCase("SEBL") && sebl.equals("Y")) {
								System.out.println("redirecting to SEBL");
								return "redirect:/sebl?transactionId="+paymentModel.getSESSIONKEY();
							}
							
							//	redirecting to default banks skipping the linkpay page
							if(ebl.equals("Y")) {
								System.out.println("by default, redirecting to EBL");
								return "redirect:/ebl?transactionId="+paymentModel.getSESSIONKEY();
							}
							if(city.equals("Y")) {
								System.out.println("by default, redirecting to CITY");
								return "redirect:/paycitybank?transactionId="+paymentModel.getSESSIONKEY();
							}
							if(sebl.equals("Y")) {
								System.out.println("by default, redirecting to SEBL");
								return "redirect:/sebl?transactionId="+paymentModel.getSESSIONKEY();
							}
						}
						
						if (httpServletRequest.getAttribute("SESSIONKEY") != null) {
							return "redirect:/link-pay?bkash="+bkash+"&SESSIONKEY="
									+ URLEncoder.encode(paymentModel.getSESSIONKEY(), "UTF-8");
						} else {
							int result = transactionService.updateOrderCustomerDetails(paymentModel);
							if (result > 0)
								return "redirect:/link-pay?bkash="+bkash+"&SESSIONKEY="
										+ URLEncoder.encode(paymentModel.getSESSIONKEY(), "UTF-8");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			activeSalts.add(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("checkoutPayment -- END");
		}

		return "redirect:/execute-payment";
	}

	/**
	 * This method is used to open BKASH payment page.
	 * 
	 * @param request
	 * @param model
	 * @param transactionId
	 * @param response
	 * @param redirectAttributes
	 * @return bkash.jsp
	 */
	@RequestMapping(value = "/bkash", method = RequestMethod.GET)
	public String bkash(HttpServletRequest request, Model model, @RequestParam String transactionId,
			HttpServletResponse response, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("bkash -- START");
		}

		Long transactionId1 = 0L;
		TransactionModel transactionModel2 = null;
		try {

			System.out.println("SESSIONKEY Before Decode== >> " + transactionId);
			String key = messageUtil.getBundle("secret.key");
			transactionId1 = Long.parseLong(encryption.decode(key, transactionId));
			System.out.println("TransactionId After Decode== >> " + transactionId1);

			transactionModel2 = transactionService.fetchTransactionById(transactionId1);
			try {
				// TODO
				// logwrite.writeLog
				writeLogV2(0L, "bkash GET", 1, "trxID:" + transactionId1 + ",sessionId:" + transactionId);
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (Exception e) {

			redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;
		}

		if (Objects.isNull(transactionModel2)) {

			model.addAttribute("transactionidInvalid", messageUtil.getBundle("transactionid.invalid"));
			return "redirect:/link-pay?SESSIONKEY=" + transactionId;

		} else {
			if (transactionModel2.getTransactionStatus() == 0 || transactionModel2.getTransactionStatus() == 1
					|| transactionModel2.getTransactionStatus() == 2) { // Already Paid

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("already.paid"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();

			} else if (transactionModel2.getTransactionStatus() == 3) { // Failed

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
				return "redirect:/failure?orderId=" + transactionModel2.getTxnId();

			} else if (transactionModel2.getTransactionStatus() == 7) { // expired

				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();

			} else if (transactionModel2.getTransactionStatus() == 4) { // Not Paid Yet

				transactionModelSaltTracker.setTxnId(transactionModel2.getTxnId());
				model.addAttribute("amount", transactionModel2.getAmount());
				model.addAttribute("transactionId", transactionId);
				model.addAttribute("wallet", messageUtil.getBundle("bKash.wallet"));
				model.addAttribute("reference", messageUtil.getBundle("bKash.reference"));
				model.addAttribute("counter", messageUtil.getBundle("bKash.counter"));
			}
			if (transactionModel2.getIsValid()) { // valid, do nothing
			} else {
				redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.expired"));
				return "redirect:/transaction_expired?orderId=" + transactionModel2.getTxnId();

			}

		}

		if (logger.isInfoEnabled()) {
			logger.info("bkash -- END");
		}

		// return "bkash?orderId="+transactionModel2.getTxnId();

		return "bkash";
	}

	/**
	 * This method is used to Payment for BKASH.
	 * 
	 * @param request
	 * @param model
	 * @param bKashModel
	 * @param response
	 * @param redirectAttributes
	 * @return String
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/bkash", method = RequestMethod.POST)
	public String bkashCheck(HttpServletRequest request, Model model, @ModelAttribute BKashModel bKashModel,
			// @RequestParam String orderId,
			HttpServletResponse response, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("bkashCheck -- START");
		}
		// TODO needs to look closely
		// fetchTransactionByTXNId
		// String orderId =
		// String.valueOf(transactionModelSaltTracker.getTxnId());//LEBUPAY22844

		String returnURL = "";
		try {
			Long transactionId_w = 0L;
			TransactionModel transactionModel_w = null;
			String key = messageUtil.getBundle("secret.key");
			transactionId_w = Long.parseLong(encryption.decode(key, bKashModel.getTransactionId1()));
			transactionModel_w = transactionService.fetchTransactionById(transactionId_w);
			String orderId = transactionModel_w.getTxnId();
			System.out.println("bKashModel.getTransactionId1():" + bKashModel.getTransactionId1()
					+ " -> decoded bKashModel.getTransactionId1():" + transactionId_w + " -> bKashModel.getTrx_id():"
					+ bKashModel.getTrx_id() + " ->orderId:" + orderId);

			// TODO String returnURL = "";
			String sessionId = request.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";

			try {
				// logwrite.writeLog
				writeLogV2(0L, "bkash POST", 1,
						"orderId:" + orderId + ",sessionId:" + sessionId + "trx_id:" + transactionId_w);
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				salt = bKashModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

					Long transactionId1 = 0L;
					TransactionModel transactionModel2 = null;
					try {

						System.out
								.println("Bkash Post SESSIONKEY Before Decode== >> " + bKashModel.getTransactionId1());
						// String key = messageUtil.getBundle("secret.key"); //TODO wasif 20190304

						transactionId1 = Long.parseLong(encryption.decode(key, bKashModel.getTransactionId1()));

						System.out.println("Bkash Post SESSIONKEY After Decode== >> " + transactionId1);

						transactionModel2 = transactionService.fetchTransactionById(transactionId1); // TODO

					} catch (Exception e) {

						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transactionid.invalid"));
						returnURL = "redirect:/failure?orderId=" + orderId;
					}

					if (Objects.isNull(transactionModel2)) {
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transactionid.invalid"));
						returnURL = "redirect:/failure?orderId=" + orderId;

					} else {

						try {

							List<TransactionModel> transactionModels = transactionService
									.fetchAllTransactionByBKashId(bKashModel.getTrx_id());
							if (Objects.nonNull(transactionModels)) {

								if (transactionModels.size() > 0) {

									try {
										// logwrite.writeLog
										writeLogV2(0L, "Bkash Attempt", 1, "OrderId:" + orderId + " ,Duplicate bkashID:"
												+ bKashModel.getTrx_id() + " ,trx_id:" + transactionId1);
									} catch (Exception e1) {
										// Auto-generated catch block
										e1.printStackTrace();
									}

									redirectAttributes.addFlashAttribute("failure",
											messageUtil.getBundle("duplicate.bkash.id"));
									// TODO
									returnURL = "redirect:/bkash?transactionId=" + bKashModel.getTransactionId1();
									// returns to same page prompting input again

								} else {

									try {
										// TODO logwrite.writeLog
										writeLogV2(0L, "Bkash Attempt", 1, "OrderId:" + orderId + " , bkashID:"
												+ bKashModel.getTrx_id() + " ,trx_id:" + transactionId1);
									} catch (Exception e1) {
										// Auto-generated catch block
										e1.printStackTrace();
									}

									String inputJsonString = "{\"user\":\"" + messageUtil.getBundle("bKash.username")
											+ "\", \"pass\":\"" + messageUtil.getBundle("bKash.password")
											+ "\", \"msisdn\":\"" + messageUtil.getBundle("bKash.wallet")
											+ "\", \"trxid\":\"" + bKashModel.getTrx_id() + "\"}";
									System.out.println("inputJsonString ==>> " + inputJsonString);
									ClientResponse client_response = payconnectApiAcess(
											messageUtil.getBundle("bKash.url"), inputJsonString, "post");
									String outputJsonString = client_response.getEntity(String.class);
									System.out.println("outputJsonString ==>> " + outputJsonString);
									Gson gson = new Gson();
									TransactionModel transactionModel3 = gson.fromJson(outputJsonString,
											TransactionModel.class);
									if (Objects.nonNull(transactionModel3)) {

										BKashModel bKashModel2 = transactionModel3.getTransaction();
										if (Objects.nonNull(bKashModel2)) {

											try {
												TransactionModel transactionModel = transactionService
														.fetchTransactionByTXNId(orderId);
												int result = 0;
												transactionModel.setResponseCode(bKashModel2.getTrxStatus());
												transactionModel.setSecureId(bKashModel2.getTrx_id());
												transactionModel.setBkashTrxId(bKashModel.getTrx_id());

												if (bKashModel2.getTrxStatus().equals("0000")
														&& (bKashModel2.getAmount().equals(
																transactionModel.getPaymentModel().getAmount()))) {

													transactionModel.setTransaction_currency(bKashModel2.getCurrency());
													transactionModel.setSettlementDate(bKashModel2.getDatetime());
													transactionModel.setAcquirerMessage(bKashModel2.getReference());
													transactionModel.setTransaction_receipt(bKashModel2.getReceiver());
													transactionModel.setTotalCapturedAmount(
															String.valueOf(bKashModel2.getAmount()));
													transactionModel.setResponse_gatewayCode(bKashModel2.getService());
													transactionModel.setEnrollmentStatus(bKashModel2.getSender());
													transactionModel.setProcessingCode(bKashModel2.getCounter());
													transactionModel.setSourceOfFunds_type("CASH");
													transactionModel.setCard_brand("bkash");
													transactionModel.setBank("BKASH");

													// send email

													PaymentModel paymentModel = transactionModel.getPaymentModel();
													String sms_notifictaion = paymentModel.getNotification_sms();
													String email_notification = paymentModel.getNotification_email();

													String customerDetails = paymentModel.getCustomerDetails();
													String to = decode(customerDetails, "email");
													String firstName = decode(customerDetails, "firstName");
													String lastName = decode(customerDetails, "lastName");

													String action = "bkashSuccess";

													// TODO
													if (logger.isInfoEnabled()) {
														logger.info(
																"Bkash  email_notification : " + email_notification);
														logger.info("Bkash  sms_notifictaion : " + sms_notifictaion);
													}

													if (email_notification.equals("1")) {
														// TODO
														if (logger.isInfoEnabled()) {
															logger.info("Bkash inside email_notification : "
																	+ email_notification);
														}
														// if (logger.isInfoEnabled()) {
														// logger.info("customerDetails : " + customerDetails);
														// logger.info("customerEmail : " + customerEmail);
														// }
														String header = "Transaction successful";

														Double amount = transactionModel.getGrossAmount();

														String emailMessageBody = "<p>Dear " + firstName + " "
																+ lastName + ",</p><p>We have successfully received "
																+ transactionModel.getGrossAmount()
																+ " BDT. </p> <p>Thank You for paying with LEBUPAY</p>";
														String subject = messageUtil
																.getBundle("transaction.email.subject");

														EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
														emailInvoicingModel.setAction(action);
														emailInvoicingModel.setHeader(header);
														emailInvoicingModel.setFirstName(firstName);
														emailInvoicingModel.setLastName(lastName);
														emailInvoicingModel.setAmount("" + transactionModel.getGrossAmount());
														emailInvoicingModel.setEmailMessageBody(emailMessageBody);
														emailInvoicingModel.setSubject(subject);
														emailInvoicingModel.setEmailId(to);
														emailInvoicingModel.setBankTrxID(transactionModel.getBkashTrxId());
														emailInvoicingModel.setMobileNumber(bKashModel2.getSender());
														emailInvoicingModel.setCreatedDate(bKashModel2.getDatetime());
														emailInvoicingModel.setTrxID(orderId);
														emailInvoicingModel.setTransactionType(transactionModel.getTransaction_type());
														//emailInvoicingModel.setIPAddress(ipAddress);
														emailInvoicingModel.setCurrency(transactionModel.getTransaction_currency());
														emailInvoicingModel.setIsTemplate(true);

														spiderEmailSender.sendEmail(emailInvoicingModel);
													}

													if (sms_notifictaion.equals("1")) {
														// TODO
														if (logger.isInfoEnabled()) {
															logger.info("Bkash inside sms_notifictaion : "
																	+ sms_notifictaion);
														}
														if (Objects.nonNull(paymentModel.getMobileNumber())) {
															sendSMS.smsSend(paymentModel.getMobileNumber(),
																	"We have received "
																			+ transactionModel.getGrossAmount()
																			+ " BDT. Thank You for paying with LEBUPAY. Visit www.we-top-up.com for mobile recharge.");
														}
													}

													// TODO
													result = transactionServiceImpl
															.updateTransactionAfterPayment(transactionModel);
													// TODO added by wasif 20190122
													returnURL = "redirect:/success?orderId=" + orderId;

												} else {

													System.out.println("Failure");
													redirectAttributes.addFlashAttribute("failure",
															messageUtil.getBundle("transaction.failed"));
													returnURL = "redirect:/failure?orderId=" + orderId;
												}
												System.out.println("transactionModel -----> "
														+ new ObjectMapper().writeValueAsString(transactionModel));

												// TODO comment added by Wasif 20190122
												// result =
												// transactionServiceImpl.updateTransactionAfterPayment(transactionModel);

											} catch (Exception e) {
												e.printStackTrace();
												redirectAttributes.addFlashAttribute("failure",
														messageUtil.getBundle("transaction.failed"));
												returnURL = "redirect:/failure?orderId=" + orderId;
											}
										}
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							redirectAttributes.addFlashAttribute("failure",
									messageUtil.getBundle("transaction.failed"));
							returnURL = "redirect:/failure?orderId=" + orderId;
						}
					}
				} else {
					throw new ServletException(messageUtil.getBundle("CSRF"));
				}

			} catch (Exception e) {
				e.printStackTrace();
				returnURL = "redirect:/failure?orderId=" + orderId;

			} finally {
				activeSalts.remove(salt);
				String newSalt = (String) request.getAttribute("csrfPreventionSaltPage");
				activeSalts.add(newSalt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isInfoEnabled()) {
			logger.info("bkashCheck -- END");
		}

		return returnURL;
	}

	/**
	 * This method is used in Call BKASH Payment API.
	 * 
	 * @param url
	 * @param inputString
	 * @param method
	 * @return ClientResponse
	 */
	public static ClientResponse payconnectApiAcess(String url, String inputString, String method) {

		ClientResponse client_response = null;

		try {
			if (url != null && inputString != null) {

				Client client = Client.create();
				WebResource webResource = client.resource(url);

				if (method.equals("post")) {
					client_response = webResource.type("application/json").post(ClientResponse.class, inputString);
				} else {
					client_response = webResource.type("application/json").get(ClientResponse.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return client_response;
	}

	/**
	 * Hit from API to get order details.
	 * 
	 * @param paymentModel
	 * @param request
	 * @return paymentModel
	 */
	@RequestMapping(value = "/get-order-trx-status", method = RequestMethod.POST)
	public @ResponseBody PaymentModel checkOrderStatus(@RequestBody PaymentModel paymentModel,
			HttpServletRequest request) {
		// need to work here
		if (logger.isInfoEnabled()) {
			logger.info("Get Order -- START");
		}

		System.out.println("Order Transaction ID-->" + paymentModel.getOrderTransactionID());
		System.out.println("Access Key -->" + paymentModel.getAccessKey());
		try {
			// logwrite.writeLog
			writeLogV2(0L, "get-order-trx-status", 1, "order txnId:" + paymentModel.getOrderTransactionID());
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		PaymentModel paymentModel2 = null;
		PaymentModel paymentModel3 = new PaymentModel();
		try {

			MerchantModel merchantModel = merchantService.fetchMerchantByAccessKey(paymentModel.getAccessKey());
			if (Objects.nonNull(merchantModel)) {
				paymentModel2 = transactionService.fetchTransactionByAccessKey(paymentModel.getAccessKey(),
						paymentModel.getOrderTransactionID());

				if (Objects.nonNull(paymentModel2)) {
					paymentModel2.setResponseCode("200");
					try {
						// logwrite.writeLog
						writeLogV2(0L, "get-order-trx-status", 2,
								"order txnId (reqParam):" + paymentModel.getOrderTransactionID()
										+ ",order txnId (respParam):" + paymentModel2.getOrderTransactionID()
										+ ",transactionStatus:" + paymentModel2.getTransactionStatus());
					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}
					return paymentModel2;
				} else {
					paymentModel3.setResponseCode("202");
					paymentModel3.setResponseMessage(messageUtil.getBundle("wrong.order.id"));
				}
			} else {
				// Wrong access key
				paymentModel3.setResponseCode("201");
				paymentModel3.setResponseMessage(messageUtil.getBundle("wrong.access.key"));
			}

		} catch (Exception e) {

			paymentModel3.setResponseCode("203");
			paymentModel3.setResponseMessage(messageUtil.getBundle("someting.went.wrong"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("Get Order -- END");
		}

		return paymentModel3;

	}

	/**
	 * Hit from API to get order details. new 3rd API
	 * 
	 * @param paymentModel
	 * @param request
	 * @return paymentModel
	 */
	@RequestMapping(value = "/get-order-trx-status-v2", method = RequestMethod.POST)
	public @ResponseBody PaymentModel checkOrderStatus_v2(@RequestBody PaymentModel paymentModel,
			HttpServletRequest request) {
		// TODO need to work here
		System.out.println("print get-order-trx-status-v2 -- START");
		if (logger.isInfoEnabled()) {
			logger.info("get-order-trx-status-v2 -- START");
		}
		try {
			// logwrite.writeLog
			writeLogV2(0L, "get-order-trx-status-v2", 1, "order txnId:" + paymentModel.getOrderTransactionID());
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

		PaymentModel paymentModel2 = null;
		PaymentModel paymentModel3 = new PaymentModel();
		try {

			MerchantModel merchantModel = merchantService.fetchMerchantByAccessKey(paymentModel.getAccessKey());
			if (Objects.nonNull(merchantModel)) {
				paymentModel2 = transactionService.fetchTransactionByAccessKey_V2(paymentModel.getAccessKey(),
						paymentModel.getOrderTransactionID());

				if (Objects.nonNull(paymentModel2)) {
					paymentModel2.setResponseCode("200");

					try {
						// logwrite.writeLog
						writeLogV2(0L, "get-order-trx-status-v2", 2,
								"order txnId:" + paymentModel.getOrderTransactionID() + ",order txnId (respParam):"
										+ paymentModel2.getOrderTransactionID() + ",transactionStatus:"
										+ paymentModel2.getTransactionStatus());
					} catch (Exception e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}

					return paymentModel2;
				} else {
					paymentModel3.setResponseCode("202");
					paymentModel3.setResponseMessage(messageUtil.getBundle("wrong.order.id"));
				}
			} else {
				paymentModel3.setResponseCode("201");
				paymentModel3.setResponseMessage(messageUtil.getBundle("wrong.access.key"));
			}

		} catch (Exception e) {
			// remove later
			// System.out.println("get-order-trx-status-v2 exception print -->" + e);
			e.printStackTrace();
			// logger.info("Exception caught: "+e);
			paymentModel3.setResponseCode("203");
			paymentModel3.setResponseMessage(messageUtil.getBundle("someting.went.wrong"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("get-order-trx-status-v2 -- END");
		}

		return paymentModel3;
	}

	public String decode(String message, String key) {
		Map<String, Object> mb;
		mb = new Gson().fromJson((String) message, new TypeToken<HashMap<String, Object>>() {
		}.getType());
		String keyMessage = mb.containsKey(key) ? (String) mb.get(key) : "";
		return keyMessage;
	}

	// TODO remove below block it is for testing only
	public void writeLogV2(Long merchant_id, String action, int action_type, String log) throws Exception {

		System.out.println("logwriter.writeLog -- START");
		System.out.println("logwriter.writeLog -log ===>" + log);
		if (log.length() > 1000)
			log = log.substring(0, 999);

		if (action.length() > 100)
			action = action.substring(0, 99);

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into APPLICATION_LOG (merchant_id,action,action_type,log) values("
					+ ":merchant_id,:action,:action_type,:log)";

			String pk[] = { "ID" };
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);

			pst.setLongAtName("merchant_id", merchant_id);
			pst.setStringAtName("action", action);
			pst.setIntAtName("action_type", action_type);
			pst.setStringAtName("log", log);

			// System.out.println("logwriter.writeLog==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
			}
			connection.commit();

		} finally {

			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
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
				 * if (logger.isDebugEnabled()) {
				 * logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				 * }/
				 **/

			}
		}
		/*
		 * if (logger.isInfoEnabled()) {
		 * logger.info("updateOrderCustomerDetails -- END"); }/
		 **/

	}

	public void writeVoidLog(Long merchant_id, String action, String order_id, String log) throws Exception {

		System.out.println("logwriter.writeVoidLog -- START");
		System.out.println("logwriter.writeVoidLog -log ===>" + log);

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into VOID_LOG (merchant_id,action,order_id,log) values("
					+ ":merchant_id,:action,:order_id,:log)";

			String pk[] = { "ID" };
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);

			pst.setLongAtName("merchant_id", merchant_id);
			pst.setStringAtName("action", action);
			pst.setStringAtName("order_id", order_id);
			pst.setStringAtName("log", log);

			// System.out.println("logwriter.writeLog==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
			}
			connection.commit();

		} finally {

			try {
				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();
				}
			} catch (Exception e) {
				System.out.println("Connection not closed for logwriter.writeLog ");/**/
				/*
				 * if (logger.isDebugEnabled()) {
				 * logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				 * }/
				 **/

			}
		}

	}
}
