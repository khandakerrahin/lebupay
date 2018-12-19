/**
 * This package is used to contain all the controller files.
 */
package com.lebupay.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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

import com.google.gson.Gson;
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
import com.lebupay.model.MerchantModel;
import com.lebupay.model.ParameterModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.TransactionModel;
import com.lebupay.service.CheckOutService;
import com.lebupay.service.MerchantService;
import com.lebupay.service.TransactionService;
import com.lebupay.serviceImpl.TransactionServiceImpl;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import oracle.jdbc.OraclePreparedStatement;

/**
 * This is Payment Controller that will handle all the payment related work like
 * CitiBank,EBL, BKSAH etc.
 * 
 * @author Java-Team
 *
 */
@Controller
public class PaymentController extends BaseDao implements SaltTracker {

	private static Logger logger = Logger.getLogger(PaymentController.class);

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
		
		//TODO remove after debugging
		if (logger.isInfoEnabled()) {
			logger.info("City Bank request data --> "+data);
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
		//TODO
		//TODO remove after debugging
		if (logger.isInfoEnabled()) {
			logger.info("City Bank Payment xmlDoc response -->"+xmlDoc);
			logger.info("City Bank Payment response -->"+Response);
			
		}
		

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
	 * This method is Used to Payment of CityBank.
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
		data += "<Description>" + transactionModel2.getTxnId() + "</Description>";
		data += "<ApproveURL>" + basePath + "approveOrder" + "</ApproveURL>";
		data += "<CancelURL>" + basePath + "cancelOrder" + "</CancelURL>";
		data += "<DeclineURL>" + basePath + "declineOrder" + "</DeclineURL>";
		data += "</Order></Request></TKKPG>";

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

		if (request.getParameter("xmlmsg") != "") {

			try {

				xmlDoc = CityBankUtil.RequestHandler(request);

				// For getting the required values
				/*
				 * xmlDoc.getDocumentElement().normalize(); NodeList nList =
				 * xmlDoc.getElementsByTagName("Message"); cityOrderID =
				 * nList.item(0).getChildNodes().item(1).getTextContent(); transactionType =
				 * nList.item(0).getChildNodes().item(2).getTextContent(); pan =
				 * nList.item(0).getChildNodes().item(4).getTextContent(); purchaseAmount =
				 * nList.item(0).getChildNodes().item(5).getTextContent(); currency =
				 * nList.item(0).getChildNodes().item(6).getTextContent(); transDateTime =
				 * nList.item(0).getChildNodes().item(7).getTextContent(); responseCode =
				 * nList.item(0).getChildNodes().item(8).getTextContent(); responseDescription =
				 * nList.item(0).getChildNodes().item(9).getTextContent(); cardHolderName =
				 * nList.item(0).getChildNodes().item(10).getTextContent(); brand =
				 * nList.item(0).getChildNodes().item(11).getTextContent(); orderStatus =
				 * nList.item(0).getChildNodes().item(12).getTextContent(); approvalCode =
				 * nList.item(0).getChildNodes().item(13).getTextContent(); acqFee =
				 * nList.item(0).getChildNodes().item(14).getTextContent(); merchantTransId =
				 * nList.item(0).getChildNodes().item(15).getTextContent(); orderDescription =
				 * nList.item(0).getChildNodes().item(16).getTextContent(); approvalCodeScr =
				 * nList.item(0).getChildNodes().item(17).getTextContent(); purchaseAmountScr =
				 * nList.item(0).getChildNodes().item(18).getTextContent(); currencyScr =
				 * nList.item(0).getChildNodes().item(19).getTextContent(); orderStatusScr =
				 * nList.item(0).getChildNodes().item(20).getTextContent(); name =
				 * nList.item(0).getChildNodes().item(21).getTextContent(); threeDSVerificaion =
				 * nList.item(0).getChildNodes().item(22).getTextContent(); threeDSStatus =
				 * nList.item(0).getChildNodes().item(23).getTextContent();
				 */

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
				threeDSStatus = xpath.evaluate("ThreeDSStatus", citiResponse);

				String txnId = String.valueOf(transactionModelSaltTracker.getTxnId());
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

					int cityResult = transactionServiceImpl.saveCityBankTransaction(cityTransactionModel);

					if (cityResult > 0) {

						returnURL = "redirect:/payment_success?orderId=" + txnId;

						if (Objects.nonNull(transactionModel)) {

							PaymentModel paymentModel = new PaymentModel();
							Gson gson = new Gson();
							// JSON to Java object, read it from a Json String.
							paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
									PaymentModel.class);

							if (Objects.nonNull(paymentModel.getEmailId()) || !paymentModel.getEmailId().equals("")) {

								try {
									// Send Email
									String action = "paymentSuccess";

									String[] retval = spiderEmailSender.fetchTempConfig(action);

									String jsonReqName = retval[0];
									String jsonReqPath = retval[1];
									String templateID = retval[2];

									String header = "Payment successful";
									String emailMessageBody = "<p>Hi there!</p><p>We have successfully received "
											+ transactionModel.getGrossAmount() + " BDT. </p> <p>Lebupay Team </p>";
									String subject = messageUtil.getBundle("transaction.email.subject");

									String jsonReqString = getFileString(jsonReqName, jsonReqPath);
									jsonReqString = jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");

									jsonReqString = jsonReqString.replace("replace_header_here", header);
									jsonReqString = jsonReqString.replace("replace_amount_here",
											"" + transactionModel.getGrossAmount());
									jsonReqString = jsonReqString.replace("replace_emailMessageBody_here",
											emailMessageBody);
									jsonReqString = jsonReqString.replace("replace_subject_here", subject);
									jsonReqString = jsonReqString.replace("replace_to_here", paymentModel.getEmailId());
									jsonReqString = jsonReqString.replace("replace_cc_here", "");
									jsonReqString = jsonReqString.replace("replace_bcc_here", "");
									jsonReqString = jsonReqString.replace("replace_templateID_here", templateID);

									spiderEmailSender.sendEmail(jsonReqString, true);

									// sendMail.send(paymentModel.getEmailId(), emailMessageBody, subject);

								} catch (Exception e) {

									e.printStackTrace();
								}
							}

							if (Objects.nonNull(paymentModel.getMobileNumber())
									|| !paymentModel.getMobileNumber().equals("")) {
								try {
									sendSMS.smsSend(paymentModel.getMobileNumber(),
											"Thank you for the payment. We have received "
													+ transactionModel.getGrossAmount() + " BDT");
								} catch (Exception e) {
									e.printStackTrace();
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

		String txnId = String.valueOf(transactionModelSaltTracker.getTxnId());

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

					if (result > 0) {

						returnURL = "redirect:/payment_failure?orderId=" + txnId;

						if (Objects.nonNull(transactionModel)) {
							PaymentModel paymentModel = new PaymentModel();
							Gson gson = new Gson();
							// JSON to Java object, read it from a Json String.
							paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
									PaymentModel.class);

							if (Objects.nonNull(paymentModel.getEmailId()) || !paymentModel.getEmailId().equals("")) {
								try {
									// Send Email
									String action = "paymentCancelled";

									String[] retval = spiderEmailSender.fetchTempConfig(action);

									String jsonReqName = retval[0];
									String jsonReqPath = retval[1];
									String templateID = retval[2];

									String header = "Payment cancelled";
									String emailMessageBody = "<p>Hi there!</p><p>You have cancelled your order. Please try again.</p> <p>Lebupay Team </p>";
									String subject = messageUtil.getBundle("transaction.email.subject.cancelled");

									String jsonReqString = getFileString(jsonReqName, jsonReqPath);
									jsonReqString = jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");

									jsonReqString = jsonReqString.replace("replace_header_here", header);
									jsonReqString = jsonReqString.replace("replace_emailMessageBody_here",
											emailMessageBody);
									jsonReqString = jsonReqString.replace("replace_subject_here", subject);
									jsonReqString = jsonReqString.replace("replace_to_here", paymentModel.getEmailId());
									jsonReqString = jsonReqString.replace("replace_cc_here", "");
									jsonReqString = jsonReqString.replace("replace_bcc_here", "");
									jsonReqString = jsonReqString.replace("replace_templateID_here", templateID);

									spiderEmailSender.sendEmail(jsonReqString, true);

									// sendMail.send(paymentModel.getEmailId(), messageBody, subject);
								} catch (Exception e) {
									System.out.println("Mail Sending Failed");
								}
							}

							if (Objects.nonNull(paymentModel.getMobileNumber())
									|| !paymentModel.getMobileNumber().equals("")) {

								try {
									sendSMS.smsSend(paymentModel.getMobileNumber(),
											"You have cancelled your order. Please try again.");
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("Send SMS");
								}
							}
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

		String txnId = String.valueOf(transactionModelSaltTracker.getTxnId());

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

				if (orderStatus.equals("DECLINED")) {
					TransactionModel transactionModel = null;
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
							Gson gson = new Gson();
							// JSON to Java object, read it from a Json String.
							paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
									PaymentModel.class);

							if (Objects.nonNull(paymentModel.getEmailId())) {
								try {
									// Send Email
									String action = "paymentDeclined";

									String [] retval = spiderEmailSender.fetchTempConfig(action);
									
									String jsonReqName = retval[0];
									String jsonReqPath = retval[1];
									String templateID = retval[2];

									String header = "Payment declined";
									String emailMessageBody = "<p>Hi there!</p><p>Your payment has been declined. Please try again.</p> <p>Lebupay Team </p>";
									String subject = messageUtil.getBundle("transaction.email.subject.declined");

									String jsonReqString = getFileString(jsonReqName, jsonReqPath);
									jsonReqString = jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");

									jsonReqString = jsonReqString.replace("replace_header_here", header);
									jsonReqString = jsonReqString.replace("replace_emailMessageBody_here",
											emailMessageBody);
									jsonReqString = jsonReqString.replace("replace_subject_here", subject);
									jsonReqString = jsonReqString.replace("replace_to_here", paymentModel.getEmailId());
									jsonReqString = jsonReqString.replace("replace_cc_here", "");
									jsonReqString = jsonReqString.replace("replace_bcc_here", "");
									jsonReqString = jsonReqString.replace("replace_templateID_here", templateID);

									spiderEmailSender.sendEmail(jsonReqString, true);

									// sendMail.send(paymentModel.getEmailId(), messageBody, subject);
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("Mail Sending Failed");
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
			if (Objects.nonNull(transactionModel)) {

				PaymentModel paymentModel = new PaymentModel();
				Gson gson = new Gson();
				// JSON to Java object, read it from a Json String.
				paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
						PaymentModel.class);
				System.out.println("paymentModel ==>> " + paymentModel);
				if (Objects.nonNull(transactionModel.getPaymentModel().getSuccessURL()))
					if (!transactionModel.getPaymentModel().getSuccessURL().equals("None"))
						response.sendRedirect(transactionModel.getPaymentModel().getSuccessURL());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Payment Success -- END");
		}

		return "payment-success";
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
			if (Objects.nonNull(transactionModel)) {
				if (transactionModel.getTransactionStatus() != 0) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 3,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					if (Objects.nonNull(transactionModel.getPaymentModel().getFailureURL()))
						if (!transactionModel.getPaymentModel().getFailureURL().equals("None"))
							response.sendRedirect(transactionModel.getPaymentModel().getFailureURL());
				}
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
				if (Objects.nonNull(transactionModel)) {
					if (transactionModel.getTransactionStatus() != 0) {
						int result = transactionService.updateTransaction(
								transactionModel.getMerchantModel().getMerchantId(), 5,
								transactionModel.getTransactionId());
						System.out.println("Result ==>> " + result);
						if (Objects.nonNull(transactionModel.getPaymentModel().getFailureURL()))
							if (!transactionModel.getPaymentModel().getFailureURL().equals("None"))
								response.sendRedirect(transactionModel.getPaymentModel().getFailureURL());
					}
				}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("payment failure -- END");
		}

		return "payment-failure";
	}

           //TODO EBL STARTS HERE	
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
		argsMap.put("interaction.returnUrl", basePath + "retriveOrder");
		argsMap.put("interaction.cancelUrl", basePath + "failure?orderId=" + transactionModel2.getTxnId());

		argsMap.put("merchant", transactionModel2.getMerchantModel().getEblId());
		argsMap.put("apiPassword", transactionModel2.getMerchantModel().getEblPassword());
		argsMap.put("apiUsername", transactionModel2.getMerchantModel().getEblUserName());

		String requestURL = messageUtil.getBundle("ebi.first.url");
		System.out.println("requestURL ==>> " + requestURL);

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

					System.out.println("SUCCESS");
					String secondAPI = messageUtil.getBundle("ebi.second.url") + respMap.get("session.id");
					System.out.println("secondAPI ==>> " + secondAPI);
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
			final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("retriveOrder -- START");
		}

		String returnURL = "";
		String resultIndicator = (String) request.getParameter("resultIndicator");
		System.out.println("resultIndicator ==>> " + resultIndicator);
		String orderId = String.valueOf(transactionModelSaltTracker.getTxnId());

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
		argsMap.put("merchant", transactionModel2.getMerchantModel().getEblId());
		argsMap.put("apiPassword", transactionModel2.getMerchantModel().getEblPassword());
		argsMap.put("apiUsername", transactionModel2.getMerchantModel().getEblUserName());

		String requestURL = messageUtil.getBundle("ebi.third.url");

		try {

			HTTPConnection.sendPostRequest(requestURL, argsMap);
			String[] resp = HTTPConnection.readMultipleLinesRespone();

			String[] respArr = resp[0].split("&");

			HashMap<String, String> respMap = new HashMap<String, String>();
			for (String line : respArr) {
				String[] respPart = line.split("=");

				respMap.put(respPart[0], respPart[1]);
			}

			for (Map.Entry<String, String> entry : respMap.entrySet()) {

				System.out.println("Key ==>> " + entry.getKey() + "  Value ==>> " + entry.getValue());
			}

			if (respMap.containsKey("result")) {
				if (respMap.get("result").equals("SUCCESS")) {

					System.out.println("SUCCESS");

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
						transactionModel.setBank("EBL");

						result = transactionServiceImpl.updateTransactionAfterPayment(transactionModel);
						int cityUpdateResult = 0;
						if (result > 0) {

							cityUpdateResult = transactionServiceImpl
									.updateEblTransactionAfterPayment(transactionModel);
							if (cityUpdateResult > 0) {
								returnURL = "redirect:/success?orderId=" + orderId;

								System.out.println("Success Transaction Model-->" + transactionModel);

								if (Objects.nonNull(transactionModel)) {

									PaymentModel paymentModel = new PaymentModel();
									Gson gson = new Gson();
									// JSON to Java object, read it from a Json String.
									paymentModel = gson.fromJson(
											transactionModel.getPaymentModel().getCustomerDetails(),
											PaymentModel.class);
									System.out.println("paymentModel ==>> " + paymentModel);
									if (Objects.nonNull(paymentModel.getEmailId())) {

										// Send Email
										String action = "transactionSuccess";
										String [] retval = spiderEmailSender.fetchTempConfig(action);
										
										String jsonReqName = retval[0];
										String jsonReqPath = retval[1];
										String templateID = retval[2];
										
										String header = "Transaction successful";
										String emailMessageBody = "<p>Dear Sir/Madam!</p><p>We have successfully received "
												+ transactionModel.getGrossAmount()
												+ " BDT. </p> <p>Thank You for paying with LEBUPAY</p>";
										String subject = messageUtil.getBundle("transaction.email.subject");

										String jsonReqString = getFileString(jsonReqName, jsonReqPath);
										jsonReqString = jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");

										jsonReqString = jsonReqString.replace("replace_header_here", header);
										jsonReqString = jsonReqString.replace("replace_amount_here",
												"" + transactionModel.getGrossAmount());
										jsonReqString = jsonReqString.replace("replace_emailMessageBody_here",
												emailMessageBody);
										jsonReqString = jsonReqString.replace("replace_subject_here", subject);
										jsonReqString = jsonReqString.replace("replace_to_here",
												paymentModel.getEmailId());
										jsonReqString = jsonReqString.replace("replace_cc_here", "");
										jsonReqString = jsonReqString.replace("replace_bcc_here", "");
										jsonReqString = jsonReqString.replace("replace_templateID_here", templateID);

										spiderEmailSender.sendEmail(jsonReqString, true);

										// sendMail.send(paymentModel.getEmailId(), messageBody, subject);

									}

									if (Objects.nonNull(paymentModel.getMobileNumber())) {
										sendSMS.smsSend(paymentModel.getMobileNumber(),
												"We have received " + transactionModel.getGrossAmount()
														+ " BDT. Thank You for paying with LEBUPAY.");
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

			if (Objects.nonNull(transactionModel)) {

				PaymentModel paymentModel = new PaymentModel();
				Gson gson = new Gson();
				// JSON to Java object, read it from a Json String.
				paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
						PaymentModel.class);
				System.out.println("paymentModel success ==>> " + paymentModel);
				if (Objects.nonNull(transactionModel.getPaymentModel().getSuccessURL()))
					System.out.println("Success URL:" + transactionModel.getPaymentModel().getSuccessURL());
				if (!transactionModel.getPaymentModel().getSuccessURL().equals("None")) {
					response.sendRedirect(transactionModel.getPaymentModel().getSuccessURL());

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("success -- END");
		}

		return "payment-success";
	}

	
	//TODO SEBL STARTS HERE 
	
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
		argsMap.put("interaction.returnUrl", basePath + "retriveOrderSEBL");
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
			System.out.println("SEBl 1st url inside try respArr "+ respArr);
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
			final RedirectAttributes redirectAttributes) {
		System.out.println("SEBl retriveOrderSEBL entered");
		if (logger.isInfoEnabled()) {
			logger.info("retriveOrderSEBL -- START");
		}

		String returnURL = "";
		String resultIndicator = (String) request.getParameter("resultIndicator");
		System.out.println("resultIndicator ==>> " + resultIndicator);
		String orderId = String.valueOf(transactionModelSaltTracker.getTxnId());

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
									// JSON to Java object, read it from a Json String.
									paymentModel = gson.fromJson(
											transactionModel.getPaymentModel().getCustomerDetails(),
											PaymentModel.class);
									System.out.println("paymentModel ==>> " + paymentModel);
									if (Objects.nonNull(paymentModel.getEmailId())) {

										// Send Email
										String action = "transactionSuccess";
										String [] retval = spiderEmailSender.fetchTempConfig(action);
										
										String jsonReqName = retval[0];
										String jsonReqPath = retval[1];
										String templateID = retval[2];
										
										String header = "Transaction successful";
										String emailMessageBody = "<p>Dear Sir/Madam!</p><p>We have successfully received "
												+ transactionModel.getGrossAmount()
												+ " BDT. </p> <p>Thank You for paying with LEBUPAY</p>";
										String subject = messageUtil.getBundle("transaction.email.subject");

										String jsonReqString = getFileString(jsonReqName, jsonReqPath);
										jsonReqString = jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");

										jsonReqString = jsonReqString.replace("replace_header_here", header);
										jsonReqString = jsonReqString.replace("replace_amount_here",
												"" + transactionModel.getGrossAmount());
										jsonReqString = jsonReqString.replace("replace_emailMessageBody_here",
												emailMessageBody);
										jsonReqString = jsonReqString.replace("replace_subject_here", subject);
										jsonReqString = jsonReqString.replace("replace_to_here",
												paymentModel.getEmailId());
										jsonReqString = jsonReqString.replace("replace_cc_here", "");
										jsonReqString = jsonReqString.replace("replace_bcc_here", "");
										jsonReqString = jsonReqString.replace("replace_templateID_here", templateID);

										spiderEmailSender.sendEmail(jsonReqString, true);

										// sendMail.send(paymentModel.getEmailId(), messageBody, subject);

									}

									if (Objects.nonNull(paymentModel.getMobileNumber())) {
										sendSMS.smsSend(paymentModel.getMobileNumber(),
												"We have received " + transactionModel.getGrossAmount()
														+ " BDT. Thank You for paying with LEBUPAY.");
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

	/**
	 * This method is used to open success page after completion of SEBL payment.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return payment-success.jsp
	 */
	/*
	@RequestMapping(value = "/successSEBL", method = RequestMethod.GET)
	public String successSEBL(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("successSEBL -- START");
		}

		try {
			System.out.println("Coming In SUCCESS Section ==>> " + orderId);
			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);

			if (Objects.nonNull(transactionModel)) {

				PaymentModel paymentModel = new PaymentModel();
				Gson gson = new Gson();
				// JSON to Java object, read it from a Json String.
				paymentModel = gson.fromJson(transactionModel.getPaymentModel().getCustomerDetails(),
						PaymentModel.class);
				System.out.println("paymentModel success ==>> " + paymentModel);
				if (Objects.nonNull(transactionModel.getPaymentModel().getSuccessURL()))
					System.out.println("Success URL:" + transactionModel.getPaymentModel().getSuccessURL());
				if (!transactionModel.getPaymentModel().getSuccessURL().equals("None")) {
					response.sendRedirect(transactionModel.getPaymentModel().getSuccessURL());

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("successSEBL -- END");
		}

		return "payment-success";
	}
	/**/
	

	/**
	 * This method is used to open failure page if payment is failed.
	 * 
	 * @param request
	 * @param model
	 * @param response
	 * @param orderId
	 * @return payment-failure.jsp
	 */
	/*
	@RequestMapping(value = "/failureSEBL", method = RequestMethod.GET)
	public String failureSEBL(HttpServletRequest request, Model model, HttpServletResponse response,
			@RequestParam(required = false) String orderId) {

		if (logger.isInfoEnabled()) {
			logger.info("failureSEBL -- START");
		}

		try {

			System.out.println("Coming In FAILURE Section ==>> " + orderId);
			TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);

			if (Objects.nonNull(transactionModel)) {
				if (transactionModel.getTransactionStatus() != 0) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 3,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					if (Objects.nonNull(transactionModel.getPaymentModel().getFailureURL()))
						if (!transactionModel.getPaymentModel().getFailureURL().equals("None")) {
							response.sendRedirect(transactionModel.getPaymentModel().getFailureURL());
						}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("failureSEBL -- END");
		}

		return "payment-failure";
	}
	/**/

	/**
	 * This method is used for Hit from API. SEBL
	 * 
	 * @param paymentModel
	 * @param request
	 * @return PaymentModel
	 */
	/*
	@RequestMapping(value = "/check-paymentSEBL", method = RequestMethod.POST)
	public @ResponseBody PaymentModel checkPaymentSEBL(@RequestBody PaymentModel paymentModel, HttpServletRequest request) {

		if (logger.isInfoEnabled()) {
			logger.info("checkPaymentSEBL -- START");
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
			long result = transactionService.insertOrder(paymentModel);
			System.out.println("paymentModel.getMerchantModel().getCompanyModel().getIp() ==>> "
					+ paymentModel.getMerchantModel().getCompanyModel().getIp());
			if (Objects.nonNull(paymentModel))
				if (Objects.nonNull(paymentModel.getMerchantModel()))
					if (Objects.nonNull(paymentModel.getMerchantModel().getCompanyModel()))
						if (Objects.nonNull(paymentModel.getMerchantModel().getCompanyModel().getIp()))
							if (!ipAddress.equals(paymentModel.getMerchantModel().getCompanyModel().getIp()))
								throw new NumericException(messageUtil.getBundle("ip.not.matched"));

			paymentModel = new PaymentModel();
			paymentModel.setToken(token);
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
	}*/

	/**
	 * This method is used to execute payment after Hit from Api. SEBL
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @param token
	 * @param redirectAttributes
	 * @param response
	 * @return String
	 */
	/*
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

				CompanyModel companyModel = merchantService
						.fetchCompanyByMerchantId(paymentModel.getMerchantModel().getMerchantId());
				if (Objects.nonNull(companyModel))
					if (Objects.nonNull(companyModel.getIp()))
						if (!companyModel.getIp().equals(ipAddress))
							throw new NumericException(messageUtil.getBundle("ip.not.matched"));

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
				MerchantModel merchantModel = new MerchantModel();
				merchantModel.setMerchantId(paymentModel.getMerchantModel().getMerchantId());
				transactionModel.setMerchantModel(merchantModel);
				transactionModel.setAmount(paymentModel.getAmount());
				transactionModel.setOrder_id(paymentModel.getOrderID().toString());
				Long transactionId = transactionService.insertTransaction(transactionModel);
				if (transactionId > 0) {

					sessionKey = encryption.encode(key, String.valueOf(transactionId)); // Modified

					model.addAttribute("SESSIONKEY", sessionKey); // Modified

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
	}*/

	/**
	 * This method is used for Check Out Payment.
	 * 
	 * @param paymentModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	/*
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
					if (Objects.isNull(transactionModel))
						redirectAttributes.addFlashAttribute("transactionidInvalid",
								messageUtil.getBundle("transactionid.invalid"));
					else {
						paymentModel.setOrderID(Long.parseLong(transactionModel.getOrder_id()));
						if (httpServletRequest.getAttribute("SESSIONKEY") != null) {
							return "redirect:/link-pay?SESSIONKEY="
									+ URLEncoder.encode(paymentModel.getSESSIONKEY(), "UTF-8");
						} else {
							int result = transactionService.updateOrderCustomerDetails(paymentModel);
							if (result > 0)
								return "redirect:/link-pay?SESSIONKEY="
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
	/**/
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

			if (Objects.nonNull(transactionModel)) {
				if (transactionModel.getTransactionStatus() != 0) {
					int result = transactionService.updateTransaction(
							transactionModel.getMerchantModel().getMerchantId(), 3,
							transactionModel.getTransactionId());
					System.out.println("Result ==>> " + result);
					if (Objects.nonNull(transactionModel.getPaymentModel().getFailureURL()))
						if (!transactionModel.getPaymentModel().getFailureURL().equals("None")) {
							response.sendRedirect(transactionModel.getPaymentModel().getFailureURL());
						}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("failure -- END");
		}

		return "payment-failure";
	}

	/**
	 * This method is used for Hit from API.
	 * 
	 * @param paymentModel
	 * @param request
	 * @return PaymentModel
	 */
	@RequestMapping(value = "/check-payment", method = RequestMethod.POST)
	public @ResponseBody PaymentModel checkPayment(@RequestBody PaymentModel paymentModel, HttpServletRequest request) {

		if (logger.isInfoEnabled()) {
			logger.info("checkPayment -- START");
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
			long result = transactionService.insertOrder(paymentModel);
			System.out.println("paymentModel.getMerchantModel().getCompanyModel().getIp() ==>> "
					+ paymentModel.getMerchantModel().getCompanyModel().getIp());
			if (Objects.nonNull(paymentModel))
				if (Objects.nonNull(paymentModel.getMerchantModel()))
					if (Objects.nonNull(paymentModel.getMerchantModel().getCompanyModel()))
						if (Objects.nonNull(paymentModel.getMerchantModel().getCompanyModel().getIp()))
							if (!ipAddress.equals(paymentModel.getMerchantModel().getCompanyModel().getIp()))
								throw new NumericException(messageUtil.getBundle("ip.not.matched"));

			paymentModel = new PaymentModel();
			paymentModel.setToken(token);
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

				CompanyModel companyModel = merchantService
						.fetchCompanyByMerchantId(paymentModel.getMerchantModel().getMerchantId());
				if (Objects.nonNull(companyModel))
					if (Objects.nonNull(companyModel.getIp()))
						if (!companyModel.getIp().equals(ipAddress))
							throw new NumericException(messageUtil.getBundle("ip.not.matched"));

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
				MerchantModel merchantModel = new MerchantModel();
				merchantModel.setMerchantId(paymentModel.getMerchantModel().getMerchantId());
				transactionModel.setMerchantModel(merchantModel);
				transactionModel.setAmount(paymentModel.getAmount());
				transactionModel.setOrder_id(paymentModel.getOrderID().toString());
				Long transactionId = transactionService.insertTransaction(transactionModel);
				if (transactionId > 0) {

					sessionKey = encryption.encode(key, String.valueOf(transactionId)); // Modified

					model.addAttribute("SESSIONKEY", sessionKey); // Modified

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
					if (Objects.isNull(transactionModel))
						redirectAttributes.addFlashAttribute("transactionidInvalid",
								messageUtil.getBundle("transactionid.invalid"));
					else {
						paymentModel.setOrderID(Long.parseLong(transactionModel.getOrder_id()));
						if (httpServletRequest.getAttribute("SESSIONKEY") != null) {
							return "redirect:/link-pay?SESSIONKEY="
									+ URLEncoder.encode(paymentModel.getSESSIONKEY(), "UTF-8");
						} else {
							int result = transactionService.updateOrderCustomerDetails(paymentModel);
							if (result > 0)
								return "redirect:/link-pay?SESSIONKEY="
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

			} else if (transactionModel2.getTransactionStatus() == 4) { // Not Paid Yet

				transactionModelSaltTracker.setTxnId(transactionModel2.getTxnId());
				model.addAttribute("amount", transactionModel2.getAmount());
				model.addAttribute("transactionId", transactionId);
				model.addAttribute("wallet", messageUtil.getBundle("bKash.wallet"));
				model.addAttribute("reference", messageUtil.getBundle("bKash.reference"));
				model.addAttribute("counter", messageUtil.getBundle("bKash.counter"));
			}

		}

		if (logger.isInfoEnabled()) {
			logger.info("bkash -- END");
		}

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
			HttpServletResponse response, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("bkashCheck -- START");
		}

		String orderId = String.valueOf(transactionModelSaltTracker.getTxnId());
		String returnURL = "";
		String sessionId = request.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {
			salt = bKashModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				Long transactionId1 = 0L;
				TransactionModel transactionModel2 = null;
				try {

					System.out.println("SESSIONKEY Before Decode== >> " + bKashModel.getTransactionId1());
					String key = messageUtil.getBundle("secret.key");
					transactionId1 = Long.parseLong(encryption.decode(key, bKashModel.getTransactionId1()));

					transactionModel2 = transactionService.fetchTransactionById(transactionId1);

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

								redirectAttributes.addFlashAttribute("failure",
										messageUtil.getBundle("duplicate.bkash.id"));
								returnURL = "redirect:/bkash?transactionId=" + bKashModel.getTransactionId1();

							} else {
								String inputJsonString = "{\"user\":\"" + messageUtil.getBundle("bKash.username")
										+ "\", \"pass\":\"" + messageUtil.getBundle("bKash.password")
										+ "\", \"msisdn\":\"" + messageUtil.getBundle("bKash.wallet")
										+ "\", \"trxid\":\"" + bKashModel.getTrx_id() + "\"}";
								System.out.println("inputJsonString ==>> " + inputJsonString);
								ClientResponse client_response = payconnectApiAcess(messageUtil.getBundle("bKash.url"),
										inputJsonString, "post");
								String outputJsonString = client_response.getEntity(String.class);
								System.out.println("outputJsonString ==>> " + outputJsonString);
								Gson gson = new Gson();
								TransactionModel transactionModel3 = gson.fromJson(outputJsonString,
										TransactionModel.class);
								if (Objects.nonNull(transactionModel3)) {

									BKashModel bKashModel2 = transactionModel3.getTransaction();
									if (Objects.nonNull(bKashModel2)) {

										try {
											TransactionModel transactionModel = transactionService.fetchTransactionByTXNId(orderId);
											int result = 0;
											transactionModel.setResponseCode(bKashModel2.getTrxStatus());
											transactionModel.setSecureId(bKashModel2.getTrx_id());
											transactionModel.setBkashTrxId(bKashModel.getTrx_id());
											if (bKashModel2.getTrxStatus().equals("0000") && (bKashModel2.getAmount()
													.equals(transactionModel.getPaymentModel().getAmount()))) {

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
												
												//send email
												
												String action = "transactionSuccess";
												String [] retval = spiderEmailSender.fetchTempConfig(action);
												
												String jsonReqName = retval[0];
												String jsonReqPath = retval[1];
												String templateID = retval[2];
												
												PaymentModel paymentModel = transactionModel.getPaymentModel();
												String customerDetails = paymentModel.getCustomerDetails();
												String to = decode(customerDetails, "email");
												String firstName = decode(customerDetails, "firstName");
												String lastName = decode(customerDetails, "lastName");

//												if (logger.isInfoEnabled()) {
//													logger.info("customerDetails : " + customerDetails);
//													logger.info("customerEmail : " + customerEmail);
//												}
												String header = "Transaction successful";
												String emailMessageBody = "<p>Dear "+firstName+" "+lastName+",</p><p>We have successfully received "
														+ transactionModel.getGrossAmount()
														+ " BDT. </p> <p>Thank You for paying with LEBUPAY</p>";
												String subject = messageUtil.getBundle("transaction.email.subject");

												String jsonReqString = getFileString(jsonReqName, jsonReqPath);
												jsonReqString = jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");

												jsonReqString = jsonReqString.replace("replace_header_here", header);
												jsonReqString = jsonReqString.replace("replace_amount_here",
														"" + transactionModel.getGrossAmount());
												jsonReqString = jsonReqString.replace("replace_emailMessageBody_here",
														emailMessageBody);
												jsonReqString = jsonReqString.replace("replace_subject_here", subject);
												jsonReqString = jsonReqString.replace("replace_to_here",to);
												jsonReqString = jsonReqString.replace("replace_cc_here", "");
												jsonReqString = jsonReqString.replace("replace_bcc_here", "");
												jsonReqString = jsonReqString.replace("replace_templateID_here", templateID);

												spiderEmailSender.sendEmail(jsonReqString, true);
												
												returnURL = "redirect:/success?orderId=" + orderId;

											} else {

												System.out.println("Failure");
												redirectAttributes.addFlashAttribute("failure",
														messageUtil.getBundle("transaction.failed"));
												returnURL = "redirect:/failure?orderId=" + orderId;
											}
											System.out.println("transactionModel -----> "
													+ new ObjectMapper().writeValueAsString(transactionModel));
											result = transactionServiceImpl.updateTransactionAfterPayment(transactionModel);

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
						redirectAttributes.addFlashAttribute("failure", messageUtil.getBundle("transaction.failed"));
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

		if (logger.isInfoEnabled()) {
			logger.info("Get Order -- START");
		}

		System.out.println("Order Transaction ID-->" + paymentModel.getOrderTransactionID());
		System.out.println("Access Key -->" + paymentModel.getAccessKey());

		PaymentModel paymentModel2 = null;
		PaymentModel paymentModel3 = new PaymentModel();
		try {

			MerchantModel merchantModel = merchantService.fetchMerchantByAccessKey(paymentModel.getAccessKey());
			if (Objects.nonNull(merchantModel)) {
				paymentModel2 = transactionService.fetchTransactionByAccessKey(paymentModel.getAccessKey(),
						paymentModel.getOrderTransactionID());

				if (Objects.nonNull(paymentModel2)) {
					paymentModel2.setResponseCode("200");
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

	public String getFileString(String filename, String path) throws IOException {
		File fl = new File(path + "/" + filename);

		String targetFileStr = new String(Files.readAllBytes(Paths.get(fl.getAbsolutePath())));
		return targetFileStr;
	}

	public String decode(String message, String key) {
		Map<String, Object> mb;
		mb = new Gson().fromJson((String) message, new TypeToken<HashMap<String, Object>>() {
		}.getType());
		String keyMessage = mb.containsKey(key) ? (String) mb.get(key) : "";
		return keyMessage;
	}
}
