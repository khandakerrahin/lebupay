/**
 * @formatter:off
 *
 */

package com.lebupay.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lebupay.common.CSVUtils;
import com.lebupay.common.Encryption;
import com.lebupay.common.MessageUtil;
import com.lebupay.common.SaltTracker;
import com.lebupay.common.Util;
import com.lebupay.exception.DBConnectionException;
import com.lebupay.exception.FormExceptions;
import com.lebupay.exception.ImageFormatMismatch;
import com.lebupay.model.CardTypeModel;
import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CheckoutModel;
import com.lebupay.model.CommonModel;
import com.lebupay.model.CompanyModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.EmailInvoicingModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.MerchantSessionModel;
import com.lebupay.model.ParameterModel;
import com.lebupay.model.QuickPayModel;
import com.lebupay.model.Status;
import com.lebupay.model.TicketModel;
import com.lebupay.model.TransactionModel;
import com.lebupay.service.AdminService;
import com.lebupay.service.CheckOutService;
import com.lebupay.service.CreateExcelService;
import com.lebupay.service.FaqService;
import com.lebupay.service.MerchantService;
import com.lebupay.service.QuickPayService;
import com.lebupay.service.TransactionService;
import com.lebupay.serviceImpl.TransactionServiceImpl;

/**
 * This class is related to all operation of Merchant.
 * 
 * @author Java-Team
 *
 */

@Controller
@RequestMapping(value = "/merchant")
public class MerchantController implements SaltTracker {

	private static Logger logger = Logger.getLogger(MerchantController.class);

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private MessageUtil messageUtil;

	@Autowired
	private Encryption encryption;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionServiceImpl transactionServiceImpl;

	@Autowired
	private CheckOutService checkOutService;

	@Autowired
	private QuickPayService quickPayService;

	@Autowired
	private CreateExcelService createExcelService;

	@Autowired
	private FaqService faqService;

	@Autowired
	private AdminService adminService;

	// Added by Ajmain 20190425

	/**
	 * This method is use for to check whether merchant is registered or not.
	 * 
	 * @param httpServletRequest
	 * @param merchantModel
	 * @return String
	 */
	@RequestMapping(value = "/login1", method = RequestMethod.POST)
	public @ResponseBody String checkMerchant(HttpServletRequest httpServletRequest, HttpServletResponse response,
			MerchantModel merchantModel) {

		if (logger.isInfoEnabled()) {
			logger.info("checkMerchant -- START: ");
		}

		String retVal = "3";
		System.out.println("In checkMerchant method");

		String username = (String) httpServletRequest.getParameter("username");

		if (!username.contains("@")) {
			if (!phoneNumberFormated(username)) {
				return retVal;
			}
		}

		merchantModel.setUserName(username);

		CommonModel commonModel = new CommonModel();
		String newSalt = "";
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {
			salt = merchantModel.getCsrfPreventionSalt();

			// if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)&
			// activeSalts.contains(salt)) {

			MerchantModel merchantDetails = merchantService.checkMerchant(merchantModel);

			if (Objects.nonNull(merchantDetails)) {
				System.out.println("Email or phone is found");
				retVal = "1";
			} else {
				System.out.println("Email or phone not found");
				retVal = "2";
			}

		} catch (Exception e) {
			retVal = "2";
			e.printStackTrace();
//			commonModel.setStatus(Status.DELETE);
//			commonModel.setMessage(e.getMessage());
		} finally {
			activeSalts.remove(salt);
			newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			commonModel.setCsrfPreventionSalt(newSalt);
			activeSalts.add(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("checkMerchant -- END");
		}

		return retVal + "," + newSalt;
	}

	// Added by Ajmain 20190425

	/**
	 * This method is use for to check whether mobile number is valid or not.
	 * 
	 * @param String
	 * @return boolean
	 */

	private boolean phoneNumberFormated(String phoneNumber) {

		boolean valid = false;
		try {
			phoneNumber = phoneNumber.replace("+", "").replaceAll("-", "").replaceAll(" ", "");
			if (phoneNumber.matches("^((880)|(0))?(1[3-9]{1}|35|44|66){1}[0-9]{8}$")) {
				valid = true;
			} else {
				valid = false;
			}
			return valid;
		} catch (Exception e) {
			return valid;
		}
	}

	/**
	 * This method is use to registration merchant details
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @return CommonModel
	 */
	// commented by Ajmain 20190425

	/*
	 * @RequestMapping(value = "/registration", method = RequestMethod.POST)
	 */
	/*
	 * public @ResponseBody CommonModel merchantRegistration(@RequestBody
	 * MerchantModel merchantModel, HttpServletRequest httpServletRequest) {
	 * 
	 * if (logger.isInfoEnabled()) { logger.info("merchantRegistration -- START"); }
	 * System.out.println("registration started");
	 * System.out.println(merchantModel.getFirstName());
	 * 
	 * String name = merchantModel.getFirstName(); String firstName = ""; String
	 * lastName = ""; if (name.contains(" ")) { String[] nameList = name.split(" ");
	 * lastName = nameList[nameList.length - 1]; for (int i = 0; i < nameList.length
	 * - 1; ++i) { firstName += nameList[i]; }
	 * merchantModel.setFirstName(firstName); } merchantModel.setLastName(lastName);
	 * CommonModel commonModel = new CommonModel();
	 * 
	 * String sessionId = httpServletRequest.getSession().getId(); List<String>
	 * activeSalts = SALT_TRACKER.get(sessionId); String salt = "";
	 * 
	 * try {
	 * 
	 * salt = merchantModel.getCsrfPreventionSalt();
	 * 
	 * if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) &
	 * activeSalts.contains(salt)) {
	 * 
	 * long result = merchantService.create(merchantModel, httpServletRequest);
	 * 
	 * if (result > 0) { merchantModel.setMerchantId(result);
	 * 
	 *//**
		 * added merchant session value from merchant model
		 */

	/*
	 * MerchantSessionModel merchantSessionModel = new MerchantSessionModel();
	 * 
	 * merchantSessionModel.setMerchantId(result);
	 * merchantSessionModel.setEmailId(merchantModel.getEmailId());
	 * merchantSessionModel.setMobileNo(merchantModel.getMobileNo());
	 * 
	 * httpServletRequest.getSession().invalidate(); HttpSession httpSession =
	 * httpServletRequest.getSession(true);
	 * httpSession.setAttribute("merchantModel", merchantSessionModel);
	 * 
	 * commonModel.setStatus(Status.ACTIVE);
	 * commonModel.setMessage(messageUtil.getBundle(
	 * "common.registration.successfully"));
	 * 
	 * } else { throw new Exception(); } } else { throw new
	 * ServletException(messageUtil.getBundle("CSRF")); }
	 * 
	 * } catch (FormExceptions e1) {
	 * 
	 * commonModel.setStatus(Status.INACTIVE); List<String> exe = new
	 * ArrayList<String>(); for (String e : e1.getExe()) { exe.add(e); }
	 * commonModel.setMultipleMessage(exe);
	 * commonModel.setMessage(messageUtil.getBundle(
	 * "common.registration.not.successfully"));
	 * 
	 * logger.debug(e1.getMessage(), e1);
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * 
	 * commonModel.setStatus(Status.INACTIVE);
	 * commonModel.setMessage(messageUtil.getBundle(
	 * "common.registration.not.successfully")); } finally {
	 * activeSalts.remove(salt); String newSalt = (String)
	 * httpServletRequest.getAttribute("csrfPreventionSaltPage");
	 * commonModel.setCsrfPreventionSalt(newSalt); activeSalts.add(newSalt); }
	 * 
	 * if (logger.isInfoEnabled()) { logger.info("merchantRegistration -- END"); }
	 * 
	 * return commonModel; }
	 */

	// Added by Ajmain 20190425
	/**
	 * This method is use to registration merchant details
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @return CommonModel
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public @ResponseBody String merchantRegistration(HttpServletRequest httpServletRequest,
			HttpServletResponse response, MerchantModel merchantModel) {

		if (logger.isInfoEnabled()) {
			logger.info("merchantRegistration -- START");
		}

		String retVal = "<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;'>&#9888;</b>Please fill out form properly</div>";

		String name = (String) httpServletRequest.getParameter("fullName");
		String csrf = (String) httpServletRequest.getParameter("csrf");
		String email = (String) httpServletRequest.getParameter("email");
		String phone = (String) httpServletRequest.getParameter("phone");
		String password = (String) httpServletRequest.getParameter("password");
		String confirmPassword = (String) httpServletRequest.getParameter("confirmPass");
		String captcha = (String) httpServletRequest.getParameter("capcha");
		if (!phoneNumberFormated(phone)) {
			retVal = "<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;'>&#9888;</b>Invalid phone number</div>";
			return retVal;
		}
		System.out.println("captcha: " + captcha);
		if (phone.startsWith("0")) {
			phone = "+88" + phone;
		} else if (phone.startsWith("880")) {
			phone = "+" + phone;
		} else if (phone.startsWith("+880")) {

		} else {
			phone = "+880" + phone;
		}

		String firstName = "";
		String lastName = "";
		if (name.contains(" ")) {
			String[] nameList = name.split(" ");
			lastName = nameList[nameList.length - 1];
			for (int i = 0; i < nameList.length - 1; ++i) {
				firstName += nameList[i];
			}
			merchantModel.setFirstName(firstName);
		} else {
			merchantModel.setFirstName(name);
		}
		merchantModel.setLastName(lastName);
		merchantModel.setEmailId(email);
		merchantModel.setMobileNo(phone);
		merchantModel.setPassword(password);
		merchantModel.setConfirmPassword(confirmPassword);
		merchantModel.setCaptcha(captcha);
		String newSalt = "";
		CommonModel commonModel = new CommonModel();
		merchantModel.setCsrfPreventionSalt(csrf);
		System.out.println(merchantModel.getCsrfPreventionSalt());
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {

			salt = merchantModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				long result = merchantService.create(merchantModel, httpServletRequest);
				if (result > 0) {
					merchantModel.setMerchantId(result);

					/**
					 * added merchant session value from merchant model
					 */
					// MerchantSessionModel merchantSessionModel = new MerchantSessionModel();
					MerchantSessionModel merchantSessionModel = new MerchantSessionModel();

					merchantSessionModel.setMerchantId(result);
					merchantSessionModel.setEmailId(merchantModel.getEmailId());
					merchantSessionModel.setMobileNo(merchantModel.getMobileNo());

					httpServletRequest.getSession().invalidate();
					HttpSession httpSession = httpServletRequest.getSession(true);
					httpSession.setAttribute("merchantModelOtp", merchantSessionModel);

					// commonModel.setStatus(Status.ACTIVE);
					commonModel.setMessage(messageUtil.getBundle("common.registration.successfully"));
					retVal = "<div class='msg msg-success z-depth-3'>Registration successful</div>";

				} else {
					retVal = "<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;'>&#9888;</b>Please fill out form properly</div>";
					throw new Exception();
				}
			} else {
				retVal = "<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;'>&#9888;</b>Please fill out form properly</div>";
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (FormExceptions e1) {
			retVal = "<div class='msg msg-error z-depth-3'><ul>";
			commonModel.setStatus(Status.INACTIVE);
			List<String> exe = new ArrayList<String>();
			for (String e : e1.getExe()) {
				retVal += "<li> <b style='font-size: 20px;'>&#9888;</b>" + e + "</li>";
				exe.add(e);
			}
			retVal += "</ul></div>";
			commonModel.setMultipleMessage(exe);
			commonModel.setMessage(messageUtil.getBundle("common.registration.not.successfully"));

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {
			e.printStackTrace();
			retVal = "<div class='msg msg-error z-depth-3'> <b style='font-size: 20px;'>&#9888;</b>" + e.getMessage()
					+ "</div>";
			commonModel.setStatus(Status.INACTIVE);
			commonModel.setMessage(messageUtil.getBundle("common.registration.not.successfully"));
		} finally {
			activeSalts.remove(salt);
			newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			commonModel.setCsrfPreventionSalt(newSalt);
			activeSalts.add(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("merchantRegistration -- END");
		}

		return retVal + "|" + newSalt;
	}

	// creates a copy of registration method in case necessary and commented by
	// Ajmain 20190425
	/*
	 * public @ResponseBody CommonModel merchantRegistration1(MerchantModel
	 * merchantModel, HttpServletRequest httpServletRequest) {
	 * 
	 * if (logger.isInfoEnabled()) { logger.info("merchantRegistration -- START"); }
	 * System.out.println("registration started");
	 * System.out.println(merchantModel.getFirstName());
	 * 
	 * String name = merchantModel.getFirstName(); String firstName = ""; String
	 * lastName = ""; if (name.contains(" ")) { String[] nameList = name.split(" ");
	 * lastName = nameList[nameList.length - 1]; for (int i = 0; i < nameList.length
	 * - 1; ++i) { firstName += nameList[i]; }
	 * merchantModel.setFirstName(firstName); } merchantModel.setLastName(lastName);
	 * 
	 * CommonModel commonModel = new CommonModel();
	 * System.out.println(merchantModel.getCsrfPreventionSalt()); String sessionId =
	 * httpServletRequest.getSession().getId(); List<String> activeSalts =
	 * SALT_TRACKER.get(sessionId); String salt = "";
	 * 
	 * try {
	 * 
	 * salt = merchantModel.getCsrfPreventionSalt();
	 * 
	 * if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) &
	 * activeSalts.contains(salt)) {
	 * 
	 * long result = merchantService.create(merchantModel, httpServletRequest);
	 * 
	 * if (result > 0) { merchantModel.setMerchantId(result);
	 * 
	 *//**
		 * added merchant session value from merchant model
		 *//*
			 * MerchantSessionModel merchantSessionModel = new MerchantSessionModel();
			 * 
			 * merchantSessionModel.setMerchantId(result);
			 * merchantSessionModel.setEmailId(merchantModel.getEmailId());
			 * merchantSessionModel.setMobileNo(merchantModel.getMobileNo());
			 * 
			 * httpServletRequest.getSession().invalidate(); HttpSession httpSession =
			 * httpServletRequest.getSession(true);
			 * httpSession.setAttribute("merchantModel", merchantSessionModel);
			 * 
			 * commonModel.setStatus(Status.ACTIVE);
			 * commonModel.setMessage(messageUtil.getBundle(
			 * "common.registration.successfully"));
			 * 
			 * } else { throw new Exception(); } } else { throw new
			 * ServletException(messageUtil.getBundle("CSRF")); }
			 * 
			 * } catch (FormExceptions e1) {
			 * 
			 * commonModel.setStatus(Status.INACTIVE); List<String> exe = new
			 * ArrayList<String>(); for (String e : e1.getExe()) { exe.add(e); }
			 * commonModel.setMultipleMessage(exe);
			 * commonModel.setMessage(messageUtil.getBundle(
			 * "common.registration.not.successfully"));
			 * 
			 * logger.debug(e1.getMessage(), e1);
			 * 
			 * } catch (Exception e) { e.printStackTrace();
			 * 
			 * commonModel.setStatus(Status.INACTIVE);
			 * commonModel.setMessage(messageUtil.getBundle(
			 * "common.registration.not.successfully")); } finally {
			 * activeSalts.remove(salt); String newSalt = (String)
			 * httpServletRequest.getAttribute("csrfPreventionSaltPage");
			 * commonModel.setCsrfPreventionSalt(newSalt); activeSalts.add(newSalt); }
			 * 
			 * if (logger.isInfoEnabled()) { logger.info("merchantRegistration -- END"); }
			 * 
			 * return commonModel; }
			 */

	/**
	 * This method is use to verified merchant phone.
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/phone-validation", method = RequestMethod.POST)
	public @ResponseBody CommonModel phoneValidation(@RequestBody MerchantModel merchantModel,
			HttpServletRequest httpServletRequest) {

		if (logger.isInfoEnabled()) {
			logger.info("phoneValidation -- START");
		}

		CommonModel commonModel = new CommonModel();
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		HttpSession httpSession = null;
		try {

			salt = merchantModel.getCsrfPreventionSalt();

			// if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)&
			// activeSalts.contains(salt)) {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModelOtp");
			merchantModel.setMerchantId(merchantSessionModel.getMerchantId());
			merchantModel.setMobileNo(merchantSessionModel.getMobileNo());
			merchantModel.setEmailId(merchantSessionModel.getEmailId());

			long result = merchantService.phoneVerification(merchantModel);

			if (result == 1) {
				httpSession = httpServletRequest.getSession(true);
				httpSession.setAttribute("merchantModel", merchantModel);
				commonModel.setStatus(Status.ACTIVE);

			} else if (result == 2) {
				System.out.println("result = 2");
				commonModel.setStatus(Status.INACTIVE);
				commonModel.setMessage(messageUtil.getBundle("common.invalid.phone.code"));

			} else {
				throw new Exception();
			}
			/*
			 * } else { throw new ServletException(messageUtil.getBundle("CSRF")); }
			 */

		} catch (Exception e) {

			e.printStackTrace();
			commonModel.setStatus(Status.INACTIVE);
			commonModel.setMessage(e.getMessage());
		} finally {
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			commonModel.setCsrfPreventionSalt(newSalt);
			activeSalts.add(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("phoneValidation -- END");
		}
		return commonModel;
	}

	/**
	 * This method is use for merchant login purpose.
	 * 
	 * @param httpServletRequest
	 * @param merchantModel
	 * @return CommonModel
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody CommonModel merchantLogin(HttpServletRequest httpServletRequest,
			@RequestBody MerchantModel merchantModel) {

		if (logger.isInfoEnabled()) {
			logger.info("merchantLogin -- START: ");
		}
		System.out.println("In log in method");
		CommonModel commonModel = new CommonModel();

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = merchantModel.getCsrfPreventionSalt();

			// if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)&
			// activeSalts.contains(salt)) {

			MerchantModel merchantDetails = merchantService.login(merchantModel);

			if (Objects.nonNull(merchantDetails)) {
				System.out.println("Success cred");
				httpServletRequest.getSession().invalidate();
				HttpSession httpSession = httpServletRequest.getSession(true);

				System.out.println("Setting value");
				MerchantSessionModel merchantSessionModel = new MerchantSessionModel();

				merchantSessionModel.setMerchantId(merchantDetails.getMerchantId());
				merchantSessionModel.setFirstName(merchantDetails.getFirstName());
				merchantSessionModel.setLastName(merchantDetails.getLastName());
				merchantSessionModel.setEmailId(merchantDetails.getEmailId());
				merchantSessionModel.setMobileNo(merchantDetails.getMobileNo());
				// merchantSessionModel.setTransactionAmount(merchantDetails.getTransactionAmount());
				merchantSessionModel.setCreatedBy(String.valueOf(merchantDetails.getCreatedBy()));
				merchantSessionModel.setCompanyId(merchantDetails.getCompanyModel().getCompanyId());
				merchantSessionModel.setLogoName(merchantDetails.getLogoName());
				// merchantSessionModel.setLoyaltyPoint(merchantDetails.getLoyaltyPoint());
				merchantSessionModel.setSecretKey(merchantDetails.getSecretKey());
				merchantSessionModel.setAccessKey(merchantDetails.getAccessKey());
				merchantSessionModel.setUniqueKey(merchantDetails.getUniqueKey());

				httpSession.setAttribute("merchantModel", merchantSessionModel);

				if (merchantDetails.getPhoneVerified().equals("0")) {
					System.out.println("phone verified");
					commonModel.setStatus(Status.ACTIVE);
					commonModel.setMessage(messageUtil.getBundle("common.login.successfull"));

				} else {
					System.out.println("phone not verified");
					int result = merchantService.resend(merchantDetails);
					System.out.println("Result for resend ==>> " + result);
					commonModel.setStatus(Status.INACTIVE);
					commonModel.setMessage(messageUtil.getBundle("common.login.successfull"));
				}

			} else {
				System.out.println("login failed");
				commonModel.setStatus(Status.DELETE);
				commonModel.setMessage(messageUtil.getBundle("common.invalid.credential"));
			}
			/*
			 * } else {
			 * 
			 * throw new ServletException(messageUtil.getBundle("CSRF")); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus(Status.DELETE);
			commonModel.setMessage(e.getMessage());
		} finally {
			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			commonModel.setCsrfPreventionSalt(newSalt);
			activeSalts.add(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("merchantLogin -- END");
		}

		return commonModel;
	}

	/**
	 * This method is use for logout purpose.
	 * 
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {

		if (logger.isDebugEnabled()) {
			logger.debug("Merchant logout - Start");
		}

		HttpSession httpSession = httpServletRequest.getSession(false);
		httpSession.removeAttribute("merchantModel");
		httpSession.invalidate();

		redirectAttributes.addFlashAttribute("logout", messageUtil.getBundle("logout"));

		if (logger.isDebugEnabled()) {
			logger.debug("Merchant logout - End");
		}

		return "redirect:/index";
	}

	/**
	 * This method is used for Skip Company after Merchant Login.
	 * 
	 * @param request
	 * @param merchantID
	 * @return String
	 */
	@RequestMapping(value = "/skip-company", method = RequestMethod.GET)
	public String skipCompany(HttpServletRequest request, @RequestParam Long merchantID) {

		if (logger.isInfoEnabled()) {
			logger.info("skipCompany -- START");
		}

		try {

			int result = merchantService.skipCompany(merchantID, 1);
			System.out.println("Skipping Company Details ==>> " + result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("skipCompany -- END");
		}

		return "redirect:/merchant/dashboard";
	}

	/**
	 * This method is use to open merchant-home page.
	 * 
	 * @param request
	 * @param model
	 * @return merchant-home.jsp
	 */
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard(HttpServletRequest request, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("dashboard -- START");
		}

		HttpSession httpSession = request.getSession(true);
		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpSession.getAttribute("merchantModel");

		if (Objects.isNull(merchantSessionModel)) {
			return "redirect:/index";
		}

		try {

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			if (merchantModel.getCreatedBy() == 0) { // No Company Skip

				if (merchantModel.getCompanyModel().getCompanyId() != 0) {

					model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
					model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

					List<TransactionModel> transactionModels = transactionService
							.fetchAllTransactionByMerchantId(merchantSessionModel.getMerchantId());
					model.addAttribute("transactionModels", transactionModels);

					System.out.println("Dashboard Trasnsactions-->" + transactionModels);
					return "merchant-home";
				}

			} else { // Company Skip

				model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
				model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

				List<TransactionModel> transactionModels = transactionService
						.fetchAllTransactionByMerchantId(merchantSessionModel.getMerchantId());
				model.addAttribute("transactionModels", transactionModels);
				return "merchant-home";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("dashboard -- END");
		}

		return "redirect:/merchant/company-info";
	}

	/**
	 * This method is used to Show List of Transaction for Pagination.
	 * 
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/list-transaction", method = RequestMethod.POST)
	public String listTransactionSearch(@ModelAttribute DataTableModel dataTableModel,
			HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant listTransaction -- START");
		}

		HttpSession httpSession = httpServletRequest.getSession(true);
		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpSession.getAttribute("merchantModel");

		if (Objects.isNull(merchantSessionModel)) {
			return "redirect:/index";
		}

		try {

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());

			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

			List<TransactionModel> transactionModels = transactionService
					.fetchAllTransactionsByMerchantIdNew(dataTableModel, merchantSessionModel.getMerchantId());

			model.addAttribute("transactionModels", transactionModels);

			model.addAttribute("dataTableModel", dataTableModel);
		} catch (DBConnectionException e) {

			e.printStackTrace();
			model.addAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

		} catch (SQLException e) {

			e.printStackTrace();
			model.addAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("common.error", messageUtil.getBundle("common.error"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant listTransaction -- END");
		}

		return "merchant-home";
	}

	/**
	 * This method is used to redirect merchant dashboard page.
	 * 
	 * @param request
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/list-transaction", method = RequestMethod.GET)
	public String listTransaction(HttpServletRequest request, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("listTransaction -- START");
		}

		if (logger.isInfoEnabled()) {
			logger.info("listTransaction -- END");
		}

		return "redirect:/merchant/dashboard";
	}
	/**
	 * This method is used to Show List of Transaction for Pagination.
	 * 
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @param model
	 * @return String
	 */
	/*
	 * @RequestMapping(value = "/list-transaction", method = RequestMethod.POST)
	 * public @ResponseBody DataTableModel listTransaction(@ModelAttribute
	 * DataTableModel dataTableModel, HttpServletRequest httpServletRequest, Model
	 * model) {
	 * 
	 * if (logger.isInfoEnabled()) {
	 * logger.info("Merchant listTransaction -- START"); } try {
	 * MerchantSessionModel merchantSessionModel = (MerchantSessionModel)
	 * httpServletRequest.getSession().getAttribute("merchantModel");
	 * transactionService.fetchAllTransactionsByMerchantId(dataTableModel,
	 * merchantSessionModel.getMerchantId());
	 * 
	 * } catch (DBConnectionException e) {
	 * 
	 * e.printStackTrace(); model.addAttribute("database.not.found",
	 * messageUtil.getBundle("database.not.found"));
	 * 
	 * } catch (SQLException e) {
	 * 
	 * e.printStackTrace(); model.addAttribute("general.pblm",
	 * messageUtil.getBundle("general.pblm")); } catch (Exception e) {
	 * 
	 * e.printStackTrace(); model.addAttribute("common.error",
	 * messageUtil.getBundle("common.error")); }
	 * 
	 * if (logger.isInfoEnabled()) { logger.info("Merchant listTransaction -- END");
	 * }
	 * 
	 * return dataTableModel; }
	 */

	/**
	 * This method is used to download excel of Transaction List.
	 * 
	 * @param httpServletResponse
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadListTransactionExcel", method = RequestMethod.GET)
	public void downloadListDisburseMerchantExcel(HttpServletResponse httpServletResponse,
			@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {

		Object[][] bookData = null;
		try {
			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			bookData = transactionService.exportAllTransactionsForExcelByMerchantId(dataTableModel,
					dataTableModel.getNoOfColumns(), merchantSessionModel.getMerchantId());

		} catch (Exception e) {

			e.printStackTrace();
		}

		XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);

		String mimeType = "application/vnd.ms-excel";

		System.out.println("MIME type: " + mimeType);

		httpServletResponse.setContentType(mimeType);

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"downloadListTransactionExcel.xlsx\"", "excel file");
		httpServletResponse.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = httpServletResponse.getOutputStream();

		byte[] buffer = new byte[1024];
		int bytesRead = -1;

		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		xssfWorkbook.write(boas);

		ByteArrayInputStream b = new ByteArrayInputStream(boas.toByteArray());

		while ((bytesRead = b.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		// workbook.close();
		b.close();
		outStream.close();

	}

	/**
	 * This method is use for merchant forgot password purpose.
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
	public @ResponseBody CommonModel forgetPassword(@RequestBody MerchantModel merchantModel,
			HttpServletRequest httpServletRequest) {
		if (logger.isInfoEnabled()) {
			logger.info("forgetPassword -- START");
		}

		CommonModel commonModel = new CommonModel();

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = merchantModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				int result = merchantService.forgotPassword(merchantModel, httpServletRequest);

				if (result > 0) {
					commonModel.setStatus(Status.ACTIVE);
					commonModel.setMessage(messageUtil.getBundle("common.email.send"));

				} else
					throw new Exception();
			} else {
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (ServletException e) {

			e.printStackTrace();
			commonModel.setStatus(Status.DELETE);
			commonModel.setMessage(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus(Status.INACTIVE);
			commonModel.setMessage(e.getMessage());

		} finally {

			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			commonModel.setCsrfPreventionSalt(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("forgetPassword -- END");
		}

		return commonModel;
	}

	/**
	 * This method is use for mail forgot password purpose.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @param userId
	 * @return merchant-mail-forgot-password.jsp
	 */
	@RequestMapping(value = "/mail-forgot-password", method = RequestMethod.GET)
	public String mailForgotPasswordPage(HttpServletRequest httpServletRequest, Model model,
			@RequestParam String userId) {

		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordPage -- START");
		}

		String adminId1 = null;
		String adminId2 = httpServletRequest.getQueryString();
		String id = adminId2.split("=")[1] + "======";
		try {

			String key = messageUtil.getBundle("secret.key");

			adminId1 = encryption.decode(key, id);

			if (userId.length() > id.length())
				throw new Exception();

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("/mail-forgot-password-post get", e);
			return "redirect:/error404";
		}

		model.addAttribute("merchantId", adminId1);

		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordPage -- END");
		}

		return "merchant-mail-forgot-password";
	}

	/**
	 * This method is use for mail forgot password purpose.
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/mail-forgot-password", method = RequestMethod.POST)
	public String mailForgotPassword(@ModelAttribute MerchantModel merchantModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant mailForgotPassword -- START");
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = merchantModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				int result = merchantService.forgotPasswordChange(merchantModel);

				if (result > 0) {
					redirectAttributes.addFlashAttribute("password.change.success",
							messageUtil.getBundle("password.change.success"));
					return "redirect:/index";
				} else
					throw new Exception();
			} else {
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			logger.error("/mail-forgot-password-post Error", e);
		} finally {
			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			merchantModel.setCsrfPreventionSalt(newSalt);
		}

		String id = "";
		try {
			String key = messageUtil.getBundle("secret.key");
			id = encryption.encode(key, String.valueOf(merchantModel.getMerchantId()));
		} catch (Exception e) {

			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant mailForgotPassword -- END");
		}

		return "redirect:/merchant/mail-forgot-password?userId=" + id;
	}

	/**
	 * This method is use for change password purpose.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-change-password.jsp
	 */
	@RequestMapping(value = "/change-password", method = RequestMethod.GET)
	public String changePassword(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant changePassword -- START");
		}

		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
				.getAttribute("merchantModel");

		if (Objects.isNull(merchantSessionModel)) {
			return "redirect:/index";
		}

		try {

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
			model.addAttribute("link", "Change Password");

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("ERROR /company-info", e);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant changePassword -- END");
		}

		return "merchant-change-password";
	}

	/**
	 * This method is used for change password purpose.
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String changePassword(@ModelAttribute MerchantModel merchantModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes) {
		if (logger.isInfoEnabled()) {
			logger.info("Merchant changePassword -- START");
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = merchantModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
						.getAttribute("merchantModel");
				merchantModel.setMerchantId(merchantSessionModel.getMerchantId());
				int result = merchantService.changePassword(merchantModel);
				if (result == 1) {

					redirectAttributes.addFlashAttribute("password.change.success",
							messageUtil.getBundle("password.change.success"));

					HttpSession httpSession = httpServletRequest.getSession(false);
					httpSession.removeAttribute("merchantModel");
					httpSession.invalidate();

					return "redirect:/index";
				} else if (result == 0) {
					redirectAttributes.addFlashAttribute("merchant.password.wrong",
							messageUtil.getBundle("merchant.password.wrong"));

					HttpSession httpSession = httpServletRequest.getSession(false);
					httpSession.removeAttribute("merchantModel");
					httpSession.invalidate();
				} else
					throw new Exception();
			} else {

				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("/change-password Error", e);
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} finally {

			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			merchantModel.setCsrfPreventionSalt(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant changePassword -- END");
		}

		return "redirect:/merchant/change-password";
	}

	/**
	 * This method is use to view merchant profile.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-edit-profile.jsp
	 */
	@RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
	public String editMerchant(HttpServletRequest httpServletRequest, Model model) {
		if (logger.isInfoEnabled()) {
			logger.info("editMerchant -- START");
		}

		try {

			HttpSession httpSession = httpServletRequest.getSession(true);
			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpSession
					.getAttribute("merchantModel");

			if (Objects.isNull(merchantSessionModel)) {
				return "redirect:/index";
			}
			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
			model.addAttribute("merchantModel", merchantModel);
			model.addAttribute("link", "Edit Profile");

		} catch (DBConnectionException e) {

			e.printStackTrace();
			model.addAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

		} catch (SQLException e) {

			e.printStackTrace();
			model.addAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("common.error", messageUtil.getBundle("common.error"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("editMerchant -- END");
		}

		return "merchant-edit-profile";
	}

	/**
	 * This method is use to update merchant profile.
	 * 
	 * @param merchantModel
	 * @param bindingResult
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/update-profile", method = RequestMethod.POST)
	public String editProfile(@ModelAttribute MerchantModel merchantModel, BindingResult bindingResult,
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("editProfile -- START");
		}

		if (bindingResult.hasErrors())
			return "redirect:/error";

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = merchantModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				try {

					MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
							.getAttribute("merchantModel");
					merchantModel.setMerchantId(merchantSessionModel.getMerchantId());

					long result = merchantService.updateProfile(merchantModel);

					if (result > 0) {

						redirectAttributes.addFlashAttribute("merchant.profile.update.success",
								messageUtil.getBundle("merchant.profile.update.success"));
						return "redirect:/merchant/edit-profile";

					} else
						throw new Exception();

				} catch (FormExceptions e1) {

					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						if (entry.getKey().equals("common.invalid.phone.code")
								|| entry.getKey().equals("common.phone.code.required")) {

							MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest
									.getSession().getAttribute("merchantModel");
							MerchantModel merchant = merchantService
									.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
							model.addAttribute("merchantModel", merchant);

							model.addAttribute(entry.getKey(), entry.getValue().getMessage());
							return "merchant-otp-check";
						}
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());

					}

					logger.debug(e1.getMessage(), e1);

				} catch (DBConnectionException e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("database.not.found",
							messageUtil.getBundle("database.not.found"));

				} catch (SQLException e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("merchant.profile.update.failed",
							messageUtil.getBundle("merchant.profile.update.failed"));
				}
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("common.not.updated", messageUtil.getBundle("common.not.updated"));
		} finally {
			activeSalts.remove(salt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("editProfile -- END");
		}

		return "redirect:/merchant/edit-profile";
	}

	/**
	 * This method is use to re send phone code.
	 * 
	 * @param httpServletRequest
	 * @return String
	 */
	@RequestMapping(value = "/resend", method = RequestMethod.GET)
	public @ResponseBody CommonModel resend(HttpServletRequest httpServletRequest) {
		if (logger.isInfoEnabled()) {
			logger.info("Merchant resend -- START");
		}

		CommonModel commonModel = new CommonModel();

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = new MerchantModel();
			merchantModel.setMerchantId(merchantSessionModel.getMerchantId());
			int result = merchantService.resend(merchantModel);

			if (result > 0) {
				commonModel.setStatus(Status.ACTIVE);
				commonModel.setMessage(messageUtil.getBundle("common.phone.code.resend.successfully"));
			} else {
				commonModel.setStatus(Status.INACTIVE);
				commonModel.setMessage(messageUtil.getBundle("common.phone.code.resend.not.successfully"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus(Status.INACTIVE);
			commonModel.setMessage(e.getMessage());
		}

		return commonModel;
	}

	/**
	 * This method is used to resend code.
	 * 
	 * @param model
	 * @param httpServletRequest
	 * @return merchant-otp-check.jsp
	 */
	@RequestMapping(value = "/resend-otp", method = RequestMethod.GET)
	public String resend(Model model, HttpServletRequest httpServletRequest) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant Edit Profile resend OTP -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = new MerchantModel();
			merchantModel.setMerchantId(merchantSessionModel.getMerchantId());
			merchantModel.setEmailId(merchantSessionModel.getEmailId());
			int result = merchantService.resend(merchantModel);

			if (result > 0) {

				merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
				model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
				model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
				model.addAttribute("merchantId", merchantModel.getMerchantId());
				model.addAttribute("merchantModelProfile", merchantModel);
				model.addAttribute("common.phone.code.resend.successfully",
						messageUtil.getBundle("common.phone.code.resend.successfully"));

			}

		} catch (DBConnectionException e) {

			e.printStackTrace();
			model.addAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

		} catch (SQLException e) {

			e.printStackTrace();
			model.addAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("common.error", messageUtil.getBundle("common.error"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant Edit Profile resend OTP -- END");
		}

		return "merchant-otp-check";
	}

	/**
	 * This method is used to check OTP.
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/otp-check", method = RequestMethod.POST)
	public String otpCheck(@ModelAttribute MerchantModel merchantModel, HttpServletRequest httpServletRequest,
			Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("otpCheck -- START:" + merchantModel);
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			merchantModel.setMerchantId(merchantSessionModel.getMerchantId());

			model.addAttribute("merchantModelProfile", merchantModel);

			long result = merchantService.resend(merchantModel);

			if (result > 0) {

				merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
				model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
				model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
				model.addAttribute("merchantId", merchantModel.getMerchantId());
				model.addAttribute("merchantModelProfile", merchantModel);
				model.addAttribute("common.phone.code.resend.successfully",
						messageUtil.getBundle("common.phone.code.successfully"));
				return "merchant-otp-check";

			}

		} catch (DBConnectionException e) {

			e.printStackTrace();
			model.addAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

		} catch (SQLException e) {

			e.printStackTrace();
			model.addAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("common.error", messageUtil.getBundle("common.error"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("otpCheck -- END");
		}

		return "redirect:/merchant/edit-profile";
	}

	/**
	 * This method is used to View Company Info Page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-company.jsp
	 */
	@RequestMapping(value = "/company-info", method = RequestMethod.GET)
	public String companyInfo(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant companyInfo -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
			model.addAttribute("merchantId", merchantModel.getMerchantId());

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("ERROR /company-info", e);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant companyInfo -- END");
		}

		return "merchant-company";
	}

	/**
	 * This method is used to Update Add Company Info.
	 * 
	 * @param companyModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/company-info", method = RequestMethod.POST)
	public String createCompanyInfo(@ModelAttribute CompanyModel companyModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant createCompanyInfo -- START");
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {

			salt = companyModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
						.getAttribute("merchantModel");

				MerchantModel merchantModel = new MerchantModel();
				merchantModel.setMerchantId(merchantSessionModel.getMerchantId());
				companyModel.setMerchantModel(merchantModel);

				long result = merchantService.createCompany(companyModel);
				if (result > 0) {

					int result2 = merchantService.skipCompany(merchantSessionModel.getMerchantId(), 0);
					System.out.println("Updating Company in Created By For Merchant ==>> " + result2);
					redirectAttributes.addFlashAttribute("merchant.company.successfully",
							messageUtil.getBundle("merchant.company.successfully"));
					return "redirect:/merchant/dashboard";

				} else
					throw new Exception();
			} else {
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			redirectAttributes.addFlashAttribute("companyModel", companyModel);

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} finally {
			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			companyModel.setCsrfPreventionSalt(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant createCompanyInfo -- END");
		}

		return "redirect:/merchant/company-info";
	}

	// ============================================================ customer care
	// ==================================================================================================

	/**
	 * This method is use to open customer care page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-customer-care.jsp
	 */
	@RequestMapping(value = "/customer-care", method = RequestMethod.GET)
	public String customerCarePage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("customerCarePage -- START");
		}

		model.addAttribute("link", "Customer Care");

		/**
		 * added transaction amount and layalty point in UI
		 */
		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("ERROR /customer-care", e);
		}

		if (logger.isInfoEnabled()) {
			logger.info("customerCarePage -- END");
		}

		return "merchant-customer-care";
	}

	/**
	 * ADD TICKET CONTROLLER this method is use to add ticket details
	 * 
	 * @param ticketModel
	 * @param bindingResult
	 * @param httpServletRequest
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/customer-care", method = RequestMethod.POST)
	public String addTicket(@ModelAttribute TicketModel ticketModel, BindingResult bindingResult,
			HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("addTicket -- START");
		}

		if (bindingResult.hasErrors())
			return "redirect:/error";

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = ticketModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				try {
					MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
							.getAttribute("merchantModel");

					MerchantModel merchantModel = merchantService
							.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
					model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
					model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

					long result = merchantService.saveTicket(ticketModel, merchantSessionModel.getMerchantId());

					if (result > 0) {
						model.addAttribute("link", "Customer Care");
						model.addAttribute("common.successfully", messageUtil.getBundle("common.successfully"));
						return "merchant-customer-care";
					} else
						throw new Exception();

				} catch (FormExceptions e1) {

					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						model.addAttribute(entry.getKey(), entry.getValue().getMessage());
					}

					logger.debug(e1.getMessage(), e1);

				} catch (DBConnectionException e) {
					e.printStackTrace();
					model.addAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

				} catch (SQLException e) {
					e.printStackTrace();
					model.addAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("common.error", messageUtil.getBundle("common.error"));
				}
			}
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		} finally {
			activeSalts.remove(salt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("addTicket -- END");
		}

		return "merchant-customer-care";
	}

	/**
	 * This method is use to view ticket details.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-view-ticket.jsp
	 */
	@RequestMapping(value = "/view-ticket", method = RequestMethod.GET)
	public String viewTicketPage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant viewTicketPage -- START");
		}

		List<TicketModel> ticketModels = null;

		try {
			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

			model.addAttribute("link", "View Ticket");

			ticketModels = merchantService.fetchTickets(merchantSessionModel.getMerchantId());
			int size = ticketModels.size();
			int subSize = size > 10 ? 10 : size;
			model.addAttribute("ticketModels", ticketModels.subList(0, subSize));
			model.addAttribute("totalSize", ticketModels.size());
		} catch (DBConnectionException e) {

			e.printStackTrace();
			model.addAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

		} catch (SQLException e) {

			e.printStackTrace();
			model.addAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("common.error", messageUtil.getBundle("common.error"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant viewTicketPage -- END");
		}

		return "merchant-view-ticket";
	}

	/**
	 * This method is use to view ticket details.
	 * 
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/view-ticket", method = RequestMethod.POST)
	public @ResponseBody DataTableModel viewTicket(@ModelAttribute DataTableModel dataTableModel,
			HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant viewTicket -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

			merchantService.fetchTickets(dataTableModel, merchantSessionModel.getMerchantId());

		} catch (DBConnectionException e) {

			e.printStackTrace();
			model.addAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

		} catch (SQLException e) {

			e.printStackTrace();
			model.addAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("common.error", messageUtil.getBundle("common.error"));
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant viewTicket -- END");
		}

		return dataTableModel;
	}

	/**
	 * Download it into excel this method is use for down load excel purpose.
	 * 
	 * @param httpServletResponse
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	public void downloadExcel(HttpServletResponse httpServletResponse, @ModelAttribute DataTableModel dataTableModel,
			HttpServletRequest httpServletRequest) throws IOException {

		Object[][] bookData = null;
		try {
			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			bookData = merchantService.exportTicketsForExcel(dataTableModel, dataTableModel.getNoOfColumns(),
					merchantSessionModel.getMerchantId());
		} catch (Exception e) {

			e.printStackTrace();
		}

		XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);

		String mimeType = "application/vnd.ms-excel";

		httpServletResponse.setContentType(mimeType);

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"marchentTicket.xls\"", "excel file");
		httpServletResponse.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = httpServletResponse.getOutputStream();

		byte[] buffer = new byte[1024];
		int bytesRead = -1;

		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		xssfWorkbook.write(boas);

		ByteArrayInputStream b = new ByteArrayInputStream(boas.toByteArray());

		while ((bytesRead = b.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		// workbook.close();
		b.close();
		outStream.close();

	}

	// ====================================================================== Check
	// Out parameter
	// =====================================================================

	/**
	 * This method is use to open checkout page.
	 * 
	 * @param request
	 * @param model
	 * @return merchant-checkout-page.jsp
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout(HttpServletRequest request, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("checkout -- START");
		}

		HttpSession httpSession = request.getSession(true);
		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpSession.getAttribute("merchantModel");

		if (Objects.isNull(merchantSessionModel)) {
			return "redirect:/index";
		}

		try {

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
			model.addAttribute("link", "Checkout");

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("ERROR /checkout", e);
		}

		try {
			model.addAttribute("parameterModels",
					checkOutService.fetchParametersById(merchantSessionModel.getMerchantId()));
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			model.addAttribute("checkoutModel",
					checkOutService.fetchCheckoutById(merchantSessionModel.getMerchantId()));
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("checkout -- END");
		}

		return "merchant-checkout-page";
	}

	/**
	 * This method is used to Restore Default Layout.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/restore-default-layout", method = RequestMethod.GET)
	public String restoreDefaultLayout(HttpServletRequest httpServletRequest, Model model,
			RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant restoreDefaultLayout -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			CheckoutModel checkoutModel = new CheckoutModel();
			checkoutModel.setModifiedBy(merchantSessionModel.getMerchantId());
			checkoutModel.setBackgroundColour(messageUtil.getBundle("merchant.checkout.bgcolour"));
			checkoutModel.setBannerName(null);
			checkoutModel.setFile(null);
			long result = checkOutService.updateCheckout(checkoutModel, httpServletRequest);
			if (result > 0) {

				redirectAttributes.addFlashAttribute("restore.successfully",
						messageUtil.getBundle("restore.successfully"));

			} else {
				redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant restoreDefaultLayout -- END");
		}

		return "redirect:/merchant/checkout";
	}

	/**
	 * This method is used to Update Merchant Checkout Layout.
	 * 
	 * @param checkoutModel
	 * @param httpServletRequest
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/update-checkout-layout", method = RequestMethod.POST)
	public String updateCheckOutLayout(@ModelAttribute CheckoutModel checkoutModel,
			HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant updateCheckOutLayout -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			checkoutModel.setModifiedBy(merchantSessionModel.getMerchantId());

			long result = checkOutService.updateCheckout(checkoutModel, httpServletRequest);

			if (result > 0) {

				redirectAttributes.addFlashAttribute("checkoutModel", "");

				redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));

			} else {
				redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
			}
		} catch (ImageFormatMismatch e1) {

			redirectAttributes.addFlashAttribute("image.format.mismatch", e1.getMessage());

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant updateCheckOutLayout -- END");
		}

		return "redirect:/merchant/checkout";
	}

	/**
	 * This method is use to add Chekout Parameter details
	 * 
	 * @param httpServletRequest
	 * @param parameterModel
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/add-checkout-parameter", method = RequestMethod.POST)
	public String addCheckoutParameter(HttpServletRequest httpServletRequest,
			@ModelAttribute ParameterModel parameterModel, RedirectAttributes redirectAttributes) {
		if (logger.isInfoEnabled()) {
			logger.info("addCheckoutParameter -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			parameterModel.setCreatedBy(merchantSessionModel.getMerchantId());

			long result = checkOutService.insertParameter(parameterModel);
			if (result > 0) {

				redirectAttributes.addFlashAttribute("common.successfully",
						messageUtil.getBundle("common.successfully"));
			} else {
				redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("addCheckoutParameter -- END");
		}

		return "redirect:/merchant/checkout";
	}

	/**
	 * This method is use to update Checkout Parameter.
	 * 
	 * @param httpServletRequest
	 * @param parameterModel
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/update-checkout-parameter", method = RequestMethod.POST)
	public String updateCheckoutParameter(HttpServletRequest httpServletRequest,
			@ModelAttribute ParameterModel parameterModel, RedirectAttributes redirectAttributes) {
		if (logger.isInfoEnabled()) {
			logger.info("updateCheckoutParameter -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			parameterModel.setModifiedBy(merchantSessionModel.getMerchantId());

			long result = checkOutService.updateParameter(parameterModel);

			if (result > 0) {

				redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));

			} else {
				redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateCheckoutParameter -- END");
		}

		return "redirect:/merchant/checkout";
	}

	/**
	 * This method is use to delete Checkout Parameter.
	 * 
	 * @param httpServletRequest
	 * @param parameterModel
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/delete-checkout-parameter", method = RequestMethod.POST)
	public String deleteCheckoutParameter(HttpServletRequest httpServletRequest,
			@ModelAttribute ParameterModel parameterModel, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("deleteCheckoutParameter -- START");
		}

		try {
			int result = checkOutService.deleteParameter(parameterModel);

			if (result > 0) {

				redirectAttributes.addFlashAttribute("common.deleted", messageUtil.getBundle("common.deleted"));
			} else {

				redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("deleteCheckoutParameter -- END");
		}

		return "redirect:/merchant/checkout";
	}

	/**
	 * This method is used to get CheckOut Parameter.
	 * 
	 * @param httpServletRequest
	 * @param parameterModel
	 * @param redirectAttributes
	 * @return ParameterModel
	 */
	@RequestMapping(value = "/get-checkout-parameter", method = RequestMethod.POST)
	public @ResponseBody ParameterModel getChekoutParameterById(HttpServletRequest httpServletRequest,
			@RequestBody ParameterModel parameterModel, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("getChekoutParameterById -- START");
		}

		try {
			parameterModel = checkOutService.fetchParameterById(parameterModel.getParameterId());
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("getChekoutParameterById -- END");
		}

		return parameterModel;
	}

	// ---------------------------------------------------------------- Email
	// Invoicing --------------------------------------------

	/**
	 * This method is used to open email invoicing page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-email-invoicing.jsp
	 */
	@RequestMapping(value = "/email-invoicing", method = RequestMethod.GET)
	public String emailInvoicingPage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("emailInvoicingPage -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
			model.addAttribute("link", "Email Invoicing");

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("ERROR /email-invoicing", e);
		}

		if (logger.isInfoEnabled()) {
			logger.info("emailInvoicingPage -- END");
		}

		return "merchant-email-invoicing";
	}

	/**
	 * This method is used to Email Invoicing.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @param emailInvoicingModel
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/email-invoicing", method = RequestMethod.POST)
	public String emailInvoicing(HttpServletRequest httpServletRequest, Model model,
			@ModelAttribute EmailInvoicingModel emailInvoicingModel, final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("emailInvoicing -- START");
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {

			salt = emailInvoicingModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {
				try {

					MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
							.getAttribute("merchantModel");

					MerchantModel merchantModel = merchantService
							.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
					model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
					model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
					model.addAttribute("link", "Email Invoicing");

					emailInvoicingModel.setMerchantEmailId(merchantModel.getEmailId());

					int result = merchantService.emailInvoicing(emailInvoicingModel);
					if (result > 0)
						redirectAttributes.addFlashAttribute("invoice.send.success",
								messageUtil.getBundle("invoice.send.success"));
					else
						redirectAttributes.addFlashAttribute("invoice.send.failed",
								messageUtil.getBundle("invoice.send.failed"));

				} catch (FormExceptions e1) {

					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						break;
					}

					redirectAttributes.addFlashAttribute("emailInvoicingModel", emailInvoicingModel);

				} catch (Exception e) {
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("invoice.send.failed",
							messageUtil.getBundle("invoice.send.failed"));
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
			logger.info("emailInvoicing -- END");
		}

		return "redirect:/merchant/email-invoicing";
	}

	// ============================================= Quick Pay
	// =====================================================================

	/**
	 * This method is use to open quick pay page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-quickpay.jsp
	 */
	@RequestMapping(value = "/quickpay", method = RequestMethod.GET)
	public String addQuickpayPage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("addQuickpayPage -- START");
		}

		model.addAttribute("link", "Quick Pay");

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("ERROR /quickpay", e);
		}

		if (logger.isInfoEnabled()) {
			logger.info("addQuickpayPage -- END");
		}

		return "merchant-quickpay";
	}

	/**
	 * This method is use to add quickpay details.
	 * 
	 * @param quickPayModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/add-quickpay", method = RequestMethod.POST)
	public String createQuickpay(@ModelAttribute QuickPayModel quickPayModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes) {
		if (logger.isInfoEnabled()) {
			logger.info("Merchant create createQuickpay -- START");
		}

		try {

			String generatedHTML = quickPayService.createQuickPay(quickPayModel, httpServletRequest);

			if (!Util.isEmpty(generatedHTML)) {
				redirectAttributes.addFlashAttribute("generatedHTML", generatedHTML);
			}

		} catch (FormExceptions e1) {

			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}

			logger.debug(e1.getMessage(), e1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant create createQuickpay -- END");
		}

		return "redirect:/merchant/quickpay";
	}

	/**
	 * This method is use to download quickpay csv file.
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 */

	@RequestMapping("/download-quickpay")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "fileName", required = false) String fileName) {

		try {

			if (Util.isEmpty(fileName))
				fileName = messageUtil.getBundle("quickpay.sample.csv");

			ServletContext context = request.getServletContext();
			String appPath = context.getRealPath("");

			// construct the complete absolute path of the file
			String fullPath = appPath + "resources" + File.separator + "quick-pay" + File.separator + fileName;
			// Authorized user will download the file
			File downloadFile = new File(fullPath);
			FileInputStream inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = context.getMimeType(fullPath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			// System.out.println("MIME type: " + mimeType);

			// set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[(int) downloadFile.length()];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.flush();
			outStream.close();

			if (!Util.isEmpty(fileName)) {

				if (!fileName.equalsIgnoreCase(messageUtil.getBundle("quickpay.sample.csv")))
					downloadFile.delete();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * This method is use to upload csv file.
	 * 
	 * @param request
	 * @param file
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/upload-csv", method = RequestMethod.POST)
	public String uploadQuickpayFile(HttpServletRequest request, @RequestParam("file") MultipartFile file, Model model,
			final RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("uploadQuickpayFile -- START");
		}

		try {

			String type = FilenameUtils.getExtension(file.getOriginalFilename());
			if (Objects.nonNull(type)) {
				if (!type.equals("csv")) {
					redirectAttributes.addFlashAttribute("csv.format.mismatch",
							messageUtil.getBundle("csv.format.mismatch"));
				}
			} else {
				redirectAttributes.addFlashAttribute("csv.format.mismatch", messageUtil.getBundle("file.null"));
			}

		} catch (Exception e1) {

			return "redirect:/merchant/quickpay";
		}

		String fileName = new Date().getTime() + "1_" + file.getOriginalFilename();
		String fileName1 = new Date().getTime() + "2_" + file.getOriginalFilename();

		try {

			ServletContext context = request.getServletContext();
			String appPath = context.getRealPath("");

			// construct the complete absolute path of the file
			String fullPath = appPath + "resources" + File.separator + "quick-pay" + File.separator + fileName;
			String fullPath1 = appPath + "resources" + File.separator + "quick-pay" + File.separator + fileName1;
			FileWriter writer = new FileWriter(fullPath1);

			File file2 = new File(fullPath);

			file.transferTo(file2);

			String line = "";
			String cvsSplitBy = ",";

			try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {

				int j = 1;
				while ((line = br.readLine()) != null) {

					QuickPayModel quickPayModel = new QuickPayModel();
					String[] columns = line.split(cvsSplitBy);

					if (j == 1) { // First Row

						if (Util.isEmpty(columns[0]))
							throw new Exception("");
						if (Util.isEmpty(columns[1]))
							throw new Exception("");
						if (Util.isEmpty(columns[2]))
							throw new Exception("");

						quickPayModel.setProductSKU(columns[0]);
						quickPayModel.setProductName(columns[1]);
						quickPayModel.setOrderAmount(columns[2]);
						if (!Util.isEmpty(columns[3])) {

							quickPayModel.setCustom1(columns[3]);
							CSVUtils.writeLine(writer,
									Arrays.asList(quickPayModel.getProductSKU(), quickPayModel.getProductName(),
											quickPayModel.getOrderAmount(), quickPayModel.getCustom1(), "Link"));

						} else if (!Util.isEmpty(columns[4])) {

							quickPayModel.setCustom2(columns[4]);
							CSVUtils.writeLine(writer,
									Arrays.asList(quickPayModel.getProductSKU(), quickPayModel.getProductName(),
											quickPayModel.getOrderAmount(), quickPayModel.getCustom1(),
											quickPayModel.getCustom2(), "Link"));

						} else if (!Util.isEmpty(columns[5])) {

							quickPayModel.setCustom3(columns[5]);
							CSVUtils.writeLine(writer,
									Arrays.asList(quickPayModel.getProductSKU(), quickPayModel.getProductName(),
											quickPayModel.getOrderAmount(), quickPayModel.getCustom1(),
											quickPayModel.getCustom2(), quickPayModel.getCustom3(), "Link"));

						} else {

							CSVUtils.writeLine(writer, Arrays.asList(quickPayModel.getProductSKU(),
									quickPayModel.getProductName(), quickPayModel.getOrderAmount(), "Link"));
						}

					} else { // Other Rows

						if (Util.isEmpty(columns[0]))
							continue;
						if (Util.isEmpty(columns[1]))
							continue;
						if (Util.isEmpty(columns[2]))
							continue;

						quickPayModel.setProductSKU(columns[0]);
						quickPayModel.setProductName(columns[1]);
						quickPayModel.setOrderAmount(columns[2]);
						if (!Util.isEmpty(columns[3])) {

							quickPayModel.setCustom1(columns[3]);
							quickPayModel.setKeys(quickPayService.createQuickPay(quickPayModel, request));
							CSVUtils.writeLine(writer,
									Arrays.asList(quickPayModel.getProductSKU(), quickPayModel.getProductName(),
											quickPayModel.getOrderAmount(), quickPayModel.getCustom1(),
											quickPayModel.getKeys()));

						} else if (!Util.isEmpty(columns[4])) {

							quickPayModel.setCustom2(columns[4]);
							quickPayModel.setKeys(quickPayService.createQuickPay(quickPayModel, request));
							CSVUtils.writeLine(writer,
									Arrays.asList(quickPayModel.getProductSKU(), quickPayModel.getProductName(),
											quickPayModel.getOrderAmount(), quickPayModel.getCustom1(),
											quickPayModel.getCustom2(), quickPayModel.getKeys()));

						} else if (!Util.isEmpty(columns[5])) {

							quickPayModel.setCustom3(columns[5]);
							quickPayModel.setKeys(quickPayService.createQuickPay(quickPayModel, request));
							CSVUtils.writeLine(writer,
									Arrays.asList(quickPayModel.getProductSKU(), quickPayModel.getProductName(),
											quickPayModel.getOrderAmount(), quickPayModel.getCustom1(),
											quickPayModel.getCustom2(), quickPayModel.getCustom3(),
											quickPayModel.getKeys()));

						} else {

							quickPayModel.setKeys(quickPayService.createQuickPay(quickPayModel, request));
							CSVUtils.writeLine(writer,
									Arrays.asList(quickPayModel.getProductSKU(), quickPayModel.getProductName(),
											quickPayModel.getOrderAmount(), quickPayModel.getKeys()));
						}

					}

					j++;

				}

				writer.flush();
				writer.close();

				if (file2 != null) {
					file2.delete();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			fileName1 = URLEncoder.encode(fileName1, "UTF-8");

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("uploadQuickpayFile -- END");
		}

		return "redirect:/merchant/download-quickpay?fileName=" + fileName1;
	}

	/**
	 * This method is used to preview Quickpay Checkout Page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-preview-checkout.jsp
	 */
	@RequestMapping(value = "/preview-quickpay-checkout", method = RequestMethod.GET)
	public String getPreviewQuickpaydetails(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("getPreviewQuickpaydetails -- START");
		}

		try {
			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());

			try {
				CheckoutModel checkoutModel = checkOutService.fetchCheckoutById(merchantModel.getMerchantId());
				model.addAttribute("checkoutModel", checkoutModel);

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error in fetchCheckoutById");
			}

			try {
				List<ParameterModel> parameterModels = checkOutService
						.fetchParametersById(merchantModel.getMerchantId());

				model.addAttribute("parameterModels", parameterModels);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error in fetchParametersById");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in fetchActiveMerchantById");
		}

		if (logger.isInfoEnabled()) {
			logger.info("getQuickpaydetails -- END");
		}

		return "merchant-preview-checkout";
	}

	// ============================================= Transaction
	// =====================================================================

	/**
	 * This method is used to open Insert Transaction Page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-email-invoicing.jsp
	 */
	@RequestMapping(value = "/insert-transaction", method = RequestMethod.GET)
	public String insertTransaction(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("insertTransaction -- START");
		}

		try {

			TransactionModel transactionModel = new TransactionModel();
			transactionModel.setAmount(100.00);
			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			MerchantModel merchantModel = new MerchantModel();
			merchantModel.setMerchantId(merchantSessionModel.getMerchantId());
			transactionModel.setMerchantModel(merchantModel);

			long result = transactionService.insertTransaction(transactionModel);
			System.out.println("Result is ==>> " + result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("insertTransaction -- END");
		}

		return "merchant-email-invoicing";
	}

	/**
	 * This method is used to open Update Transaction Page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-email-invoicing.jsp
	 */
	@RequestMapping(value = "/update-transaction", method = RequestMethod.GET)
	public String updateTransaction(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- START");
		}

		try {

			TransactionModel transactionModel = new TransactionModel();
			transactionModel.setTransactionId(3L);
			transactionModel.setAmount(100.00);
			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			MerchantModel merchantModel = new MerchantModel();
			merchantModel.setMerchantId(merchantSessionModel.getMerchantId());
			transactionModel.setMerchantModel(merchantModel);

			int result = transactionServiceImpl.updateTransactionAfterPayment(transactionModel);

			System.out.println("Result is ==>> " + result);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error /update-transaction: ", e);
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- END");
		}

		return "merchant-email-invoicing";
	}

	/**
	 * This method is used to open edit company page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/edit-company", method = RequestMethod.GET)
	public String editConpanyDetails(HttpServletRequest httpServletRequest, Model model) {
		if (logger.isInfoEnabled()) {
			logger.info("editConpanyDetails -- START");
		}

		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
				.getAttribute("merchantModel");

		if (Objects.isNull(merchantSessionModel)) {
			return "redirect:/index";
		}

		try {

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			if (merchantModel.getCreatedBy() == 0) { // No Company Skip

				if (merchantModel.getCompanyModel().getCompanyId() != 0) {

					model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
					model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
					model.addAttribute("companyModel",
							merchantService.fetchCompanyByMerchantId(merchantSessionModel.getMerchantId()));
					model.addAttribute("merchantId", merchantModel.getMerchantId());
					model.addAttribute("link", "Edit Company");

					return "merchant-company-update";
				}

			} else { // Company Skip

				return "redirect:/merchant/company-info";

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error /edit-company: ", e);
		}

		if (logger.isInfoEnabled()) {
			logger.info("editConpanyDetails -- END");
		}

		return "redirect:/merchant/dashboard";
	}

	/**
	 * This method is used to Update Company.
	 * 
	 * @param companyModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/edit-company", method = RequestMethod.POST)
	public String editConpanyDetails(@ModelAttribute CompanyModel companyModel, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes) {
		if (logger.isInfoEnabled()) {
			logger.info("editConpanyDetails POST -- START");
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {

			salt = companyModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				try {
					MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
							.getAttribute("merchantModel");
					companyModel.setModifiedBy(merchantSessionModel.getMerchantId());

					long result = merchantService.updateCompany(companyModel);

					if (result > 0) {
						redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
						return "redirect:/merchant/dashboard";
					}
				} catch (FormExceptions e1) {

					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
					}

					redirectAttributes.addFlashAttribute("companyModel", companyModel);

				} catch (DBConnectionException e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("database.not.found",
							messageUtil.getBundle("database.not.found"));

				} catch (SQLException e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
				}
			} else {
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error /edit-company Post: ", e);

		} finally {
			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			activeSalts.add(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("editConpanyDetails POST -- END");
		}

		return "redirect:/merchant/edit-company";
	}

	/**
	 * This method is used to Claimed Transaction.
	 * 
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @param transactionId
	 * @return String
	 */
	@RequestMapping(value = "/claim-transaction", method = RequestMethod.POST)
	public String claimTransaction(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
			@RequestParam String transactionId) {
		if (logger.isInfoEnabled()) {
			logger.info("claimTransaction POST -- START");
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {

			salt = httpServletRequest.getParameter("csrfPreventionSalt");

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				try {

					MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
							.getAttribute("merchantModel");

					if (!Objects.isNull(transactionId)) {

						if (transactionId.equalsIgnoreCase("ALL")) {

							List<TransactionModel> transactionModels = transactionService
									.fetchAllTransactionByMerchantId(merchantSessionModel.getMerchantId());

							for (TransactionModel transactionModel : transactionModels) {

								if (transactionModel.getTransactionStatus() == 0) {
									int result = transactionService.updateTransaction(
											merchantSessionModel.getMerchantId(), 1,
											transactionModel.getTransactionId());
								}
							}

						} else {

							String transactionIds[] = transactionId.split(",");

							for (String tId : transactionIds) {
								int result = transactionService.updateTransaction(merchantSessionModel.getMerchantId(),
										1, Long.parseLong(tId));
							}

						}

					} else {
						throw new FormExceptions(
								Arrays.asList(messageUtil.getBundle("merchant.transactionid.required")));
					}

					redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));

				} catch (FormExceptions e1) {

					redirectAttributes.addFlashAttribute(e1.getMessage());

					logger.debug(e1.getMessage(), e1);

				} catch (DBConnectionException e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("database.not.found",
							messageUtil.getBundle("database.not.found"));

				} catch (SQLException e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {

					e.printStackTrace();
					redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
				}
			} else {
				throw new ServletException(messageUtil.getBundle("CSRF"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error /claim-transaction Post: ", e);

		} finally {
			activeSalts.remove(salt);
			String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
			activeSalts.add(newSalt);
		}

		if (logger.isInfoEnabled()) {
			logger.info("claimTransaction POST -- END");
		}

		return "redirect:/merchant/dashboard";
	}

	/**
	 * This method is used to Open FAQ Page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-faq.jsp
	 */
	@RequestMapping(value = "/faq", method = RequestMethod.GET)
	public String faq(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("faq -- START");
		}

		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
				.getAttribute("merchantModel");

		if (Objects.isNull(merchantSessionModel)) {
			return "redirect:/index";
		}

		try {

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());

			if (merchantModel.getCompanyModel().getCompanyId() != 0) {
				model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
				model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
				model.addAttribute("faqModels", faqService.fetchAllActiveFaqsByType("Merchant"));
				model.addAttribute("link", "FAQ");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("faq -- END");
		}

		return "merchant-faq";
	}

	/**
	 * This method is used to Open Link Pay Page.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-link.jsp
	 */
	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public String linkPage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("linkPage -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");

			MerchantModel merchantModel = merchantService.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
			model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
			model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
			model.addAttribute("link", "Link For Payment");

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("linkPage -- END");
		}

		return "merchant-link";
	}

	/**
	 * This method is used for Merchant Link Pay.
	 * 
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @param emailInvoicingModel
	 * @return String
	 */
	@RequestMapping(value = "/link", method = RequestMethod.POST)
	public String link(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
			@ModelAttribute EmailInvoicingModel emailInvoicingModel) {

		if (logger.isInfoEnabled()) {
			logger.info("link -- START");
		}

		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";

		try {

			salt = emailInvoicingModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {

				try {

					MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
							.getAttribute("merchantModel");
					emailInvoicingModel.setCreatedBy(merchantSessionModel.getMerchantId());
					MerchantModel merchantModel = merchantService
							.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
					redirectAttributes.addFlashAttribute("transactionAmount", merchantModel.getTransactionAmount());
					redirectAttributes.addFlashAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());
					redirectAttributes.addFlashAttribute("link", "Link For Payment");

					HashMap<String, Object> result = merchantService.link(emailInvoicingModel, httpServletRequest);
					if (Integer.parseInt(result.get("result").toString()) > 0 && result.containsKey("plugger")) {

						redirectAttributes.addFlashAttribute("link.send.success",
								messageUtil.getBundle("link.send.success"));
						redirectAttributes.addFlashAttribute("basePaymentLink", result.get("baseLink"));
					}

				} catch (FormExceptions e1) {

					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						break;
					}

					redirectAttributes.addFlashAttribute("emailInvoicingModel", emailInvoicingModel);

				} catch (Exception e) {
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("invoice.send.failed",
							messageUtil.getBundle("link.send.failed"));
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
			logger.info("link -- END");
		}

		return "redirect:/merchant/link";
	}

	/**
	 * This method is used to show Merchant Card Percentage.
	 * 
	 * @param httpServletRequest
	 * @param model
	 * @return merchant-card-percentage.jsp
	 */
	@RequestMapping(value = "/card-percentage", method = RequestMethod.GET)
	public String cardPercentagePage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("Card Percentage Page -- START");
		}

		try {

			MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession()
					.getAttribute("merchantModel");
			List<CardTypePercentageModel> cardTypePercentageModels = merchantService
					.getAllCardPercentageByMerchantId(merchantSessionModel.getMerchantId());
			model.addAttribute("cardTypePercentageModels", cardTypePercentageModels);
			model.addAttribute("link", "Card Percentage");
			try {

				MerchantModel merchantModel = merchantService
						.fetchActiveMerchantById(merchantSessionModel.getMerchantId());
				model.addAttribute("transactionAmount", merchantModel.getTransactionAmount());
				model.addAttribute("loyaltyPoint", merchantModel.getLoyaltyPoint());

				List<CardTypeModel> cardTypeModels = adminService.fetchCardTypeList();
				model.addAttribute("cardTypeModels", cardTypeModels);

			} catch (Exception e) {

				e.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (logger.isInfoEnabled()) {
			logger.info("Card Percentage Page -- END");
		}

		return "merchant-card-percentage";
	}
}
