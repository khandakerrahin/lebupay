package com.lebupay.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lebupay.common.Encryption;
import com.lebupay.common.MessageUtil;
import com.lebupay.common.SaltTracker;
import com.lebupay.exception.DBConnectionException;
import com.lebupay.exception.FormExceptions;
import com.lebupay.model.AdminModel;
import com.lebupay.model.BannerModel;
import com.lebupay.model.CardTypeModel;
import com.lebupay.model.CommonModel;
import com.lebupay.model.ContactUsModel;
import com.lebupay.model.ContentModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.FaqModel;
import com.lebupay.model.LoyaltyModel;
import com.lebupay.model.MerchantCardPercentageModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.Status;
import com.lebupay.model.TicketModel;
import com.lebupay.model.TransactionModel;
import com.lebupay.service.AdminService;
import com.lebupay.service.BannerService;
import com.lebupay.service.ContactUsService;
import com.lebupay.service.ContentService;
import com.lebupay.service.CreateExcelService;
import com.lebupay.service.FaqService;
import com.lebupay.service.LoyaltyService;
import com.lebupay.service.TransactionService;

/**
 * This is Admin Controller Class is used to do all functionality of Admin Module.
 * @author Java-Team
 *
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController implements SaltTracker{
	
private static Logger logger = Logger.getLogger(MerchantController.class);
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private Encryption encryption;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private FaqService faqService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private CreateExcelService createExcelService;
	
	@Autowired
	private LoyaltyService loyaltyService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private ContactUsService contactUsService;
	
	/**
	 * This method is used to Open Admin Login Page.
	 * @param request
	 * @param model
	 * @return admin-login.jsp
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String adminLoginPage(HttpServletRequest request, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("adminLoginPage -- START");
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("adminLoginPage -- END");
		}
		
		return "admin-login";
	}
	
	/**
	 * This method is used to Admin Login Checking.
	 * @param adminModel
	 * @param bindingResult
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String adminLogin(@ModelAttribute AdminModel adminModel, BindingResult bindingResult, 
			HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("adminLogin -- START");
		}
		
		if(bindingResult.hasErrors())
			return "redirect:/error";
		
		try {
			
			adminModel = adminService.login(adminModel);
			if (Objects.nonNull(adminModel)) {
				
				request.getSession().invalidate();
				HttpSession httpSession = request.getSession(true);
				httpSession.setAttribute("adminModel", adminModel);
				
				return "redirect:/admin/dashboard";
				
			} else {
				
				redirectAttributes.addFlashAttribute("admin.login.failed", messageUtil.getBundle("admin.login.failed"));
				return "redirect:/admin/login";
			}
		
		} catch (FormExceptions e1) {
		
			for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
				redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
			}
		
			redirectAttributes.addFlashAttribute("adminModel", adminModel);
			logger.debug(e1.getMessage(), e1);
		
			return "redirect:/admin/login";
		
		} catch (IndexOutOfBoundsException e) {
			
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("wrong.password", messageUtil.getBundle("wrong.password"));
			
		} catch (DBConnectionException e) {
			
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
		
		} catch (SQLException e) {
			
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
		} catch (Exception e) {
			
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("adminLogin -- END");
		}
		
		return "redirect:/admin/login";
	}
	
	/**
	 * This method is used to Open Dashboard Page.
	 * @param request
	 * @param model
	 * @return admin-dashboard.jsp
	 */
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard(HttpServletRequest request, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("dashboard -- START");
		}
		
		HttpSession httpSession = request.getSession(true);
		AdminModel adminModel = (AdminModel)httpSession.getAttribute("adminModel");
		
		if(Objects.isNull(adminModel)){
			return "redirect:/admin/login";
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("dashboard -- END");
		}
		
		return "admin-dashboard";
	}
	
	/**
	 * This method is used to Open Forgot Password Page.
	 * @param httpServletRequest
	 * @param model
	 * @return admin-forgot-password.jsp
	 */
	@RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	public String forgotPasswordPage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordPage -- START");
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordPage -- END");
		}
		
		return "admin-forgot-password";
	}
	
	
	/**
	 * This method is used to Forgot Password Checking.
	 * @param adminModel
	 * @param bindingResult
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
	public String forgotPassword(@ModelAttribute AdminModel adminModel, BindingResult bindingResult, 
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- START");
		}
		
		if(bindingResult.hasErrors())
			return "redirect:/error";
		
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = adminModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {
			try {
					int result = adminService.forgotPassword(adminModel, httpServletRequest);
					if(result > 0) {
						redirectAttributes.addFlashAttribute("password.change.success", messageUtil.getBundle("password.change.success"));
						return "redirect:/admin/login";
					} else
						throw new Exception();
					
			} catch (FormExceptions e1) {

				for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
					System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
					redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
				}

				logger.debug(e1.getMessage(), e1);

			} catch (DBConnectionException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));

			} catch (SQLException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
			} catch (Exception e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
			}
			}	
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} finally {
			activeSalts.remove(salt);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- END");
		}
		
		return "redirect:/admin/forgot-password";
	}
	
	/**
	 * This method is used to open link form mail for Forgot Password.
	 * @param httpServletRequest
	 * @param model
	 * @param adminId
	 * @return admin-mail-forgot-password.jsp
	 */
	@RequestMapping(value = "/mail-forgot-password", method = RequestMethod.GET)
	public String mailForgotPasswordPage(HttpServletRequest httpServletRequest, Model model, @RequestParam String adminId) {

		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordPage -- START");
		}
		
		model.addAttribute("adminId", adminId);
		
		if (logger.isInfoEnabled()) {
			logger.info("forgotPasswordPage -- END");
		}
		
		return "admin-mail-forgot-password";
	}
	
	/**
	 * This method is used to Mail Forgot Password. 
	 * @param adminModel
	 * @param bindingResult
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/mail-forgot-password", method = RequestMethod.POST)
	public String mailForgotPassword(@ModelAttribute AdminModel adminModel, BindingResult bindingResult, 
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("mailForgotPassword -- START");
		}
		
		if(bindingResult.hasErrors())
			return "redirect:/error";
		
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = adminModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {
				try {
					int result = adminService.forgotPasswordChange(adminModel);
					if(result > 0) {
						redirectAttributes.addFlashAttribute("password.change.success", messageUtil.getBundle("password.change.success"));
						return "redirect:/admin/login";
					} else
						throw new Exception();
						
				} catch (FormExceptions e1) {
					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
					}
	
					logger.debug(e1.getMessage(), e1);
	
				} catch (DBConnectionException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
	
				} catch (SQLException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
				}
			}	
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} finally {
			activeSalts.remove(salt);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("mailForgotPassword -- END");
		}
		
		return "redirect:/admin/mail-forgot-password";
	}
	
	/**
	 * This method is used to Open Edit Admin Page for Update Admin Profile Data. 
	 * @param httpServletRequest
	 * @param model
	 * @return admin-edit-profile.jsp
	 */
	@RequestMapping(value = "/edit-admin", method = RequestMethod.GET)
	public String editAdmin(HttpServletRequest httpServletRequest, Model model) {
		
		if (logger.isInfoEnabled()) {
			logger.info("editAdmin -- START");
		}
		
		try {
			
			HttpSession httpSession = httpServletRequest.getSession(true);
			AdminModel adminModel = (AdminModel)httpSession.getAttribute("adminModel");
			
			adminModel = adminService.fetchAdminById(adminModel.getAdminId());
			model.addAttribute("adminModel",adminModel);
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
			logger.info("editAdmin -- END");
		}
		
		return "admin-edit-profile";
	}
	
	/**
	 * This method is used to Update Admin Profile Data. 
	 * @param adminModel
	 * @param bindingResult
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/update-profile", method = RequestMethod.POST)
	public String updateProfile(@ModelAttribute AdminModel adminModel, BindingResult bindingResult, 
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- START");
		}
		
		if(bindingResult.hasErrors())
			return "redirect:/error";
		
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		
		try {
			
			salt = adminModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {
				try {
						AdminModel adminModel2 = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						adminModel.setAdminId(adminModel2.getAdminId());
						adminModel.setStatus(Status.ACTIVE);
						adminModel.setModifiedBy(adminModel2.getAdminId());
						long result = adminService.updateProfile(adminModel);
						if(result > 0) {
							httpServletRequest.getSession().setAttribute("adminModel", adminModel);
							redirectAttributes.addFlashAttribute("admin.profile.update.success", messageUtil.getBundle("admin.profile.update.success"));
						} else
							throw new Exception();
						
				} catch (FormExceptions e1) {
	
					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
					}
	
					logger.debug(e1.getMessage(), e1);
	
				} catch (DBConnectionException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
	
				} catch (SQLException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("admin.profile.update.failed", messageUtil.getBundle("admin.profile.update.failed"));
				}
			}	
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("common.not.updated", messageUtil.getBundle("common.not.updated"));
		} finally {
			activeSalts.remove(salt);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- END");
		}
		
		return "redirect:/admin/edit-admin";
	}
	
	/**
	 * This method is used to open Change Password page. 
	 * @param httpServletRequest
	 * @param model
	 * @return admin-change-password.jsp 
	 */
	@RequestMapping(value = "/change-password", method = RequestMethod.GET)
	public String changePasswordPage(HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("changePasswordPage -- START");
		}
		
		model.addAttribute("link", "Change Password");
		
		if (logger.isInfoEnabled()) {
			logger.info("changePasswordPage -- END");
		}
		
		return "admin-change-password";
	}
	
	/**
	 * This method is used to Password Update. 
	 * @param adminModel
	 * @param bindingResult
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String changePassword(@ModelAttribute AdminModel adminModel, BindingResult bindingResult, 
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {

		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- START");
		}
		
		if(bindingResult.hasErrors())
			return "redirect:/error";
		
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		try {
			salt = adminModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts) & activeSalts.contains(salt)) {
				try {
						AdminModel adminModel2 = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						adminModel.setAdminId(adminModel2.getAdminId());
						int result = adminService.changePassword(adminModel);
						if(result == 1) {
							redirectAttributes.addFlashAttribute("password.change.success", messageUtil.getBundle("password.change.success"));
							HttpSession httpSession = httpServletRequest.getSession(false);
							httpSession.removeAttribute("adminModel");
							httpSession.invalidate();
							return "redirect:/admin/login";
						} else if(result == 0) {
							redirectAttributes.addFlashAttribute("admin.password.wrong", messageUtil.getBundle("admin.password.wrong"));
						}else
							throw new Exception();
						
				} catch (FormExceptions e1) {
	
					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
					}
	
					logger.debug(e1.getMessage(), e1);
	
				} catch (DBConnectionException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
	
				} catch (SQLException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
				}
			}	
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} finally {
			activeSalts.remove(salt);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- END");
		}
		
		return "redirect:/admin/change-password";
	}
	
	
	/**
	 * This method is used to Admin Logout. 
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {

		if (logger.isDebugEnabled()) {
			logger.debug("logout - Start");
		}

		HttpSession httpSession = httpServletRequest.getSession(false);
		httpSession.invalidate();
		
		redirectAttributes.addFlashAttribute("logout", messageUtil.getBundle("logout"));

		if (logger.isDebugEnabled()) {
			logger.debug("logout - End");
		}

		return "redirect:/admin/login";
	}
	
	
	//================================================================== MERCHANT=================================================================
	
	
	/**
	 * This method is used to Show List of Deactive Merchant list.
	 * @param httpServletRequest
	 * @param model
	 * @return list-active-merchant.jsp
	 */
	@RequestMapping(value = "/list-active-merchant", method = RequestMethod.GET)
	public String activeMerchant(HttpServletRequest httpServletRequest, Model model) {
		if (logger.isInfoEnabled()) {
			logger.info("activeMerchant -- START");
		}
		
		try {
			List<MerchantModel> merchantModels = adminService.fetchAllActiveMerchants(Status.DELETE);
			int size = merchantModels.size();
			int subSize = size>10?10:size;
			model.addAttribute("activeMerchants",merchantModels.subList(0, subSize));
			model.addAttribute("totalSize",merchantModels.size());
			model.addAttribute("link", "Merchant List");
			
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
			logger.info("activeMerchant -- END");
		}
		
		return "list-active-merchant";
	}
	
	/**
	 * This method is used to Show List of Deactive Merchant list for Pagination.
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @param model
	 * @return DataTableModel
	 */
	@RequestMapping(value = "/list-active-merchant", method = RequestMethod.POST)
	public @ResponseBody DataTableModel activeMerchant(@ModelAttribute DataTableModel dataTableModel, 
			HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant activeMerchant -- START");
		}
		
		try {
			adminService.fetchActiveMerchantList(dataTableModel, Status.DELETE);
			
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
			logger.info("Merchant activeMerchant -- END");
		}
		
		return dataTableModel;
	}
	
	
	/**
	 * This method is used to Show List of Deactive Merchant list for download it into excel.
	 * @param httpServletResponse
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadActiveMerchantListExcel", method = RequestMethod.GET)
    public void downloadActiveMerchantListExcel(HttpServletResponse httpServletResponse, @ModelAttribute DataTableModel dataTableModel, 
    		HttpServletRequest httpServletRequest) throws IOException {
         
        Object[][] bookData = null;
		try {
			bookData = adminService.exportActiveMerchantListForExcel(dataTableModel, dataTableModel.getNoOfColumns(), Status.DELETE);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
 
        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
    	
    	String mimeType = "application/vnd.ms-excel";

        System.out.println("MIME type: " + mimeType);

        httpServletResponse.setContentType(mimeType);

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"downloadActiveMerchantList.xlsx\"",
          "excel file");
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

        //workbook.close();
        b.close();
        outStream.close();
    	
    }
	
	
	
	/**
	 * This method is used to open Edit Merchant Page in Admin Panel.
	 * @param httpServletRequest
	 * @param model
	 * @param merchantId
	 * @return admin-edit-merchant.jsp
	 */
	@RequestMapping(value = "/edit-merchant", method = RequestMethod.GET)
	public String editActivedMerchant(HttpServletRequest httpServletRequest, Model model, @RequestParam String merchantId) {
		if (logger.isInfoEnabled()) {
			logger.info("editActivedMerchant -- START");
		}
		try {
			
			MerchantModel merchantModel = adminService.fetchActiveAndActivateMerchantById(Long.valueOf(merchantId));
			
			if(Objects.isNull(merchantModel)){
				return "redirect:/error404";
			}
			
			model.addAttribute("merchantModel",merchantModel);
			model.addAttribute("link", "Edit Merchant");
			model.addAttribute("statusDetails",Status.values());
			
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
			logger.info("editActivedMerchant -- END");
		}
		
		return "admin-edit-merchant";
	}
	
	
	/**
	 * This method is used to Update Merchant from Admin Panel.
	 * @param merchantModel
	 * @param httpServletRequest
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value = "/edit-merchant", method = RequestMethod.POST)
	public String editMerchant(@ModelAttribute MerchantModel merchantModel, HttpServletRequest httpServletRequest, 
			RedirectAttributes redirectAttributes) {
		if (logger.isInfoEnabled()) {
			logger.info("editMerchant -- START");
		}
		
		
		String sessionId = httpServletRequest.getSession().getId();
		List<String> activeSalts = SALT_TRACKER.get(sessionId);
		String salt = "";
		
		try {
			
			salt = merchantModel.getCsrfPreventionSalt();

			if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
			
				try {
					AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
					merchantModel.setaModifiedBy(sessionAdminModel.getAdminId());
					
					
					long result = adminService.updateProfileByAdmin(merchantModel);
					if(result > 0){
						redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
						if(merchantModel.getStatus().name().equals("ACTIVATED")){
							return "redirect:/admin/list-actived-merchant";
							
						} else {
							return "redirect:/admin/list-active-merchant";
						}
					}
				} catch (FormExceptions e1) {

					for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
						System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
						redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
					}

					logger.debug(e1.getMessage(), e1);

				}catch (DBConnectionException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
			
				} catch (SQLException e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
				} catch (Exception e) {
					
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
				}
			}else {
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
			logger.info("editMerchant -- END");
		}
		
		return "redirect:/admin/edit-merchant?merchantId="+merchantModel.getMerchantId();
	}
	
	
	/**
	 * This method is used to Show Active Merchant list.
	 * @param httpServletRequest
	 * @param model
	 * @return list-actived-merchant.jsp
	 */
	@RequestMapping(value = "/list-actived-merchant", method = RequestMethod.GET)
	public String activedMerchant(HttpServletRequest httpServletRequest, Model model) {
		if (logger.isInfoEnabled()) {
			logger.info("activedMerchant -- START");
		}
		
		try {
			
			List<MerchantModel> merchantModels = adminService.fetchAllActiveMerchants(Status.ACTIVATED);
			int size = merchantModels.size();
			int subSize = size>10?10:size;
			model.addAttribute("activedMerchants",merchantModels.subList(0, subSize));
			model.addAttribute("totalSize",merchantModels.size());
			model.addAttribute("link", "List Actived Merchant");
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
			logger.info("activedMerchant -- END");
		}
		
		return "list-actived-merchant";
	}
	
	/**
	 * This method is used to Show Active Merchant list for Pagination. 
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @param model
	 * @return DataTableModel
	 */
	@RequestMapping(value = "/list-actived-merchant", method = RequestMethod.POST)
	public @ResponseBody DataTableModel activedMerchant(@ModelAttribute DataTableModel dataTableModel, 
			HttpServletRequest httpServletRequest, Model model) {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant activedMerchant -- START");
		}
		
		try {
			adminService.fetchActiveMerchantList(dataTableModel, Status.ACTIVATED);
			
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
			logger.info("Merchant activedMerchant -- END");
		}
		
		return dataTableModel;
	}
	
	
	/**
	 * This method is used to download excel of Active Merchant list.
	 * @param httpServletResponse
	 * @param dataTableModel
	 * @param httpServletRequest
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadActivedMerchantListExcel", method = RequestMethod.GET)
    public void downloadActivedMerchantListExcel(HttpServletResponse httpServletResponse, 
    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
    	System.out.println(new ObjectMapper().writeValueAsString(dataTableModel));
         
        Object[][] bookData = null;
		try {
			bookData = adminService.exportActiveMerchantListForExcel(dataTableModel, dataTableModel.getNoOfColumns(), Status.ACTIVATED);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
 
        System.out.println(new ObjectMapper().writeValueAsString(bookData));
        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
    	
    	String mimeType = "application/vnd.ms-excel";

        httpServletResponse.setContentType(mimeType);

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"downloadActivedMerchantList.xlsx\"",
          "excel file");
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

        //workbook.close();
        b.close();
        outStream.close();
    	
    }
	
	
	/**
	 * This method is used To do active a deactivate Merchant.
	 * @param merchantId
	 * @param httpServletRequest
	 * @return CommonModel
	 */
	@RequestMapping(value = "/active-merchant", method = RequestMethod.POST)
	public @ResponseBody CommonModel activateMerchant(@RequestBody Long merchantId,HttpServletRequest httpServletRequest) {

		if (logger.isInfoEnabled()) {
			logger.info("activateMerchant -- START");
		}

		CommonModel commonModel = new CommonModel();
		
		AdminModel adminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
		
		try {
				int result = adminService.activateMerchant(merchantId, adminModel.getAdminId(), Status.ACTIVATED);
				if(result > 0) {
					
					commonModel.setStatus(Status.ACTIVE);
					commonModel.setMessage(messageUtil.getBundle("common.updated"));
					
				} else {
					
					throw new Exception();
				}
		} catch (Exception e) {
			e.printStackTrace();

			commonModel.setStatus(Status.INACTIVE);
			commonModel.setMessage(messageUtil.getBundle("common.not.updated"));
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("activateMerchant -- END");
		}
		
		return commonModel;
	}
	
	
//##################################################################### FAQ START ################################################################
	
		/**
		 * This method is used to Open Add FAQ Page
		 * @param httpServletRequest
		 * @param model
		 * @return admin-add-faq.jsp
		 */
		@RequestMapping(value = "/add-faq", method = RequestMethod.GET)
		public String addFaqPage(HttpServletRequest httpServletRequest, Model model) {
			
			if (logger.isInfoEnabled()) {
				logger.info("addFaqPage -- START");
			}
			
			model.addAttribute("link", "Add FAQ");
			
			if (logger.isInfoEnabled()) {
				logger.info("addFaqPage -- END");
			}
			
			return "admin-add-faq";
		}
		
		/**
		 * This method is used to Save FAQ.
		 * @param faqModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/add-faq", method = RequestMethod.POST)
		public String addFaq(@ModelAttribute FaqModel faqModel, HttpServletRequest httpServletRequest, 
				RedirectAttributes redirectAttributes) {
			
			if (logger.isInfoEnabled()) {
				logger.info("addFaq -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = faqModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					try {
						
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						faqModel.setCreatedBy(sessionAdminModel.getAdminId());
						
						long result = faqService.saveFaq(faqModel);
						if(result>0){
							redirectAttributes.addFlashAttribute("common.successfully", messageUtil.getBundle("common.successfully"));
							return "redirect:/admin/list-faq";
						}
					}catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}
						
						redirectAttributes.addFlashAttribute("faqModel", faqModel);
						logger.debug(e1.getMessage(), e1);

					} catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
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
					
			} finally {
				activeSalts.remove(salt);
				String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
				activeSalts.add(newSalt);
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("addFaq -- END");
			}
			
			return "redirect:/admin/add-faq";
		}
		
		
		/**
		 * This method is used to Show the List of FAQ.
		 * @param httpServletRequest
		 * @param model
		 * @return list-faq.jsp
		 */
		@RequestMapping(value = "/list-faq", method = RequestMethod.GET)
		public String listFaq(HttpServletRequest httpServletRequest, Model model) {
			if (logger.isInfoEnabled()) {
				logger.info("listFaq -- START");
			}
			
			try {
				List<FaqModel> faqModels = faqService.fetchFaq();
				int size = faqModels.size();
				int subSize = size>10?10:size;
				model.addAttribute("faqModels",faqModels.subList(0, subSize));
				model.addAttribute("totalSize",faqModels.size());
				model.addAttribute("link", "List FAQ");
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
				logger.info("listFaq -- END");
			}
			
			return "list-faq";
		}
		
		
		/**
		 * This method is used to Show the List of FAQ for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return DataTableModel
		 */
		@RequestMapping(value = "/list-faq", method = RequestMethod.POST)
		public @ResponseBody DataTableModel viewFaq(@ModelAttribute DataTableModel dataTableModel, 
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("viewFaq -- START");
			}
			
			try {
				faqService.fetchFaq(dataTableModel);
				
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
				logger.info("viewFaq -- END");
			}
			
			return dataTableModel;
		}
		
		/**
		 * This method is used to Download excel of the List of FAQ.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadFaqListExcel", method = RequestMethod.GET)
	    public void downloadFaqListExcel(HttpServletResponse httpServletResponse, @ModelAttribute DataTableModel dataTableModel, 
	    		HttpServletRequest httpServletRequest) {
	         
	        Object[][] bookData = null;
			try {
				bookData = faqService.exportFaqsForExcel(dataTableModel, dataTableModel.getNoOfColumns());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadFaqList.xlsx\"","excel file");
	        httpServletResponse.setHeader(headerKey, headerValue);

	        // get output stream of the response
	        OutputStream outStream;
			try {
				outStream = httpServletResponse.getOutputStream();
				 byte[] buffer = new byte[1024];
			        int bytesRead = -1;

			        ByteArrayOutputStream boas = new ByteArrayOutputStream();
			        xssfWorkbook.write(boas);

			        ByteArrayInputStream b = new ByteArrayInputStream(boas.toByteArray());

			        while ((bytesRead = b.read(buffer)) != -1) {
			         outStream.write(buffer, 0, bytesRead);
			        }

			        //workbook.close();
			        b.close();
			        outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
		
		/**
		 * This method is used to Open edit Faq page.
		 * @param httpServletRequest
		 * @param model
		 * @param faqId
		 * @return admin-edit-faq.jsp
		 */
		@RequestMapping(value = "/edit-faq", method = RequestMethod.GET)
		public String editFaq(HttpServletRequest httpServletRequest, Model model, @RequestParam String faqId) {

			if (logger.isInfoEnabled()) {
				logger.info("editFaq -- START");
			}

			try {
				FaqModel faqModel = faqService.fetchActiveFaqByID(Long.valueOf(faqId));
				model.addAttribute("faqModel", faqModel);
				
				model.addAttribute("link", "Edit FAQ");
				List<Status> statusDetails = new ArrayList<Status>();
				for(Status status:Status.values()){
					
					if(status.name().toString() != Status.ACTIVATED.name().toString()){
						
						statusDetails.add(status);
					}
				}
				
				model.addAttribute("statusDetails",statusDetails);
				
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
				logger.info("editFaq -- END");
			}
			
			return "admin-edit-faq";
		}
		
		
		/**
		 * This method is used to Update the FAQ.
		 * @param faqModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/edit-faq", method = RequestMethod.POST)
		public String editFaq(@ModelAttribute FaqModel faqModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
			if (logger.isInfoEnabled()) {
				logger.info("editFaq -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = faqModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					try {
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						
						faqModel.setaModifiedBy(sessionAdminModel.getAdminId());
						
						long result = faqService.updateFaq(faqModel);
						if(result > 0){
							redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
							return "redirect:/admin/list-faq";
						}
					} catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}

						logger.debug(e1.getMessage(), e1);

					}catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
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
					
			} finally {
				activeSalts.remove(salt);
				String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
				activeSalts.add(newSalt);
			}
			
			
			
			if (logger.isInfoEnabled()) {
				logger.info("editFaq -- END");
			}
			
			return "redirect:/admin/edit-faq?faqId="+faqModel.getFaqId();
		}
		
		
		/**
		 * This method is used to delete the FAQ.
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @param faqId
		 * @return String
		 */
		@RequestMapping(value = "/delete-faq", method = RequestMethod.GET)
		public String deleteFaq(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, @RequestParam String faqId) {
			if (logger.isInfoEnabled()) {
				logger.info("deleteFaq -- START");
			}
			
			try {
				
				AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
				
				int result = faqService.deleteFaq(Long.valueOf(faqId),sessionAdminModel.getAdminId());
				if(result==1){
					redirectAttributes.addFlashAttribute("common.deleted", messageUtil.getBundle("common.deleted"));
				}
			} catch (DBConnectionException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
		
			} catch (SQLException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
			} catch (Exception e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("common.not.deleted",messageUtil.getBundle("common.not.deleted"));
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("deleteFaq -- END");
			}
			
			return "redirect:/admin/list-faq";
		}
		
//################################################################### FAQ END   ################################################################
		
// ################################################################### Content Management START ################################################################
		
		/**
		 * This method is used to Open Add Content Management Page.
		 * @param httpServletRequest
		 * @param model
		 * @return admin-add-content-management.jsp
		 */
		@RequestMapping(value = "/add-content-management", method = RequestMethod.GET)
		public String addContentManagementPage(HttpServletRequest httpServletRequest, Model model) {
			if (logger.isInfoEnabled()) {
				logger.info("addContentManagementPage -- START");
			}
			
			model.addAttribute("link", "Add Content Management");
			
			if (logger.isInfoEnabled()) {
				logger.info("addContentManagementPage -- END");
			}
			
			return "admin-add-content-management";
		}
		
		
		/**
		 * This method is used to Save Content Management.
		 * @param contentModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/add-content-management", method = RequestMethod.POST)
		public String addContentManagement(@ModelAttribute ContentModel contentModel, HttpServletRequest httpServletRequest, 
				RedirectAttributes redirectAttributes) {
			
			if (logger.isInfoEnabled()) {
				logger.info("addContentManagement -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = contentModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					
					try {
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						contentModel.setCreatedBy(sessionAdminModel.getAdminId());
						long result = contentService.saveContent(contentModel);
						if(result > 0){
							redirectAttributes.addFlashAttribute("common.successfully", messageUtil.getBundle("common.successfully"));
							return "redirect:/admin/list-content";
						}
					}catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}
						
						redirectAttributes.addFlashAttribute("contentModel", contentModel);

						logger.debug(e1.getMessage(), e1);

					} catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
					} catch (SQLException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
					} catch (Exception e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
					}
				}
				else {
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
				logger.info("addContentManagement -- END");
			}
			
			return "redirect:/admin/add-content-management";
		}
		
		
		/**
		 * This method is used to Show the List of Content.
		 * @param httpServletRequest
		 * @param model
		 * @return list-content.jsp
		 */
		@RequestMapping(value = "/list-content", method = RequestMethod.GET)
		public String listContent(HttpServletRequest httpServletRequest, Model model) {
			if (logger.isInfoEnabled()) {
				logger.info("listContent -- START");
			}
			
			try {
				
				List<ContentModel> contentModels = contentService.fetchContent();
				int size = contentModels.size();
				int subSize = size>10?10:size;
				model.addAttribute("contentModels",contentModels.subList(0, subSize));
				model.addAttribute("totalSize",contentModels.size());
				model.addAttribute("link", "List Content Management");
				
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
				logger.info("listContent -- END");
			}
			
			return "list-content";
		}
		
		/**
		 * This method is used to Show the List of Content for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return DataTableModel
		 */
		@RequestMapping(value = "/list-content", method = RequestMethod.POST)
		public @ResponseBody DataTableModel viewContent(@ModelAttribute DataTableModel dataTableModel,
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("viewContent -- START");
			}
			
			try {
				contentService.fetchContent(dataTableModel);
				
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
				logger.info("viewContent -- END");
			}
			
			return dataTableModel;
		}
		
		/**
		 * This method is used to download excel of Content List.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadContentListExcel", method = RequestMethod.GET)
	    public void downloadContentListExcel(HttpServletResponse httpServletResponse,
	    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
	    	System.out.println(new ObjectMapper().writeValueAsString(dataTableModel));
	         
	        Object[][] bookData = null;
			try {
				bookData = contentService.exportContentsForExcel(dataTableModel, dataTableModel.getNoOfColumns());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        System.out.println("MIME type: " + mimeType);

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadContentList.xlsx\"",
	          "excel file");
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

	        //workbook.close();
	        b.close();
	        outStream.close();
	    	
	    }
		
		/**
		 * This method is used to Edit Content.
		 * @param httpServletRequest
		 * @param model
		 * @param contentId
		 * @return admin-edit-content.jsp
		 */
		@RequestMapping(value = "/edit-content", method = RequestMethod.GET)
		public String editContent(HttpServletRequest httpServletRequest, Model model, @RequestParam String contentId) {

			if (logger.isInfoEnabled()) {
				logger.info("editContent -- START");
			}

			try {
				
				ContentModel contentModel = contentService.fetchContentById(Long.valueOf(contentId));
				model.addAttribute("contentModel", contentModel);
				model.addAttribute("link", "Edit Content Management");
				List<Status> statusDetails = new ArrayList<Status>();
				for(Status status:Status.values()){
					
					if(status.name().toString() != Status.ACTIVATED.name().toString()){
						
						statusDetails.add(status);
					}
				}
				
				model.addAttribute("statusDetails",statusDetails);
				
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
				logger.info("editContent -- END");
			}
			
			return "admin-edit-content";
		}
		
		
		/**
		 * This method is used to Update Content.
		 * @param contentModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/edit-content", method = RequestMethod.POST)
		public String editContent(@ModelAttribute ContentModel contentModel, HttpServletRequest httpServletRequest, 
				RedirectAttributes redirectAttributes) {
			if (logger.isInfoEnabled()) {
				logger.info("editContent -- START");
			}
			
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = contentModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					try {
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						contentModel.setaModifiedBy(sessionAdminModel.getAdminId());
						long result = contentService.updateContent(contentModel);
						if(result>0){
							redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
							return "redirect:/admin/list-content";
						}
					} catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}

						logger.debug(e1.getMessage(), e1);

					}catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
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
					
			} finally {
				activeSalts.remove(salt);
				String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
				activeSalts.add(newSalt);
			}
			
			
			if (logger.isInfoEnabled()) {
				logger.info("editContent -- END");
			}
			
			return "redirect:/admin/edit-content?contentId="+contentModel.getContentId();
		}
		
		/**
		 * This method is used to delete Content.
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @param contentId
		 * @return String
		 */
		@RequestMapping(value = "/delete-content", method = RequestMethod.GET)
		public String deleteContent(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
				@RequestParam String contentId) {
			if (logger.isInfoEnabled()) {
				logger.info("deleteContent -- START");
			}
			
			try {
				AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
				
				int result = contentService.deleteContent(Long.valueOf(contentId),sessionAdminModel.getAdminId());
				if(result==1){
					redirectAttributes.addFlashAttribute("common.deleted", messageUtil.getBundle("common.deleted"));
				}
			} catch (DBConnectionException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
		
			} catch (SQLException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
			} catch (Exception e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("common.not.deleted",messageUtil.getBundle("common.not.deleted"));
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("deleteContent -- END");
			}
			
			return "redirect:/admin/list-content";
		}
 //################################################################### Content Management END   ################################################################
		
		
//################################################################### Banner Management Start   ################################################################
			
			
		/**
		 * This method is used to Open Add banner Page.
		 * @param httpServletRequest
		 * @param model
		 * @return admin-add-banner.jsp
		 */
		@RequestMapping(value = "/add-banner", method = RequestMethod.GET)
		public String addBannerManagementPage(HttpServletRequest httpServletRequest, Model model) {
			
			if (logger.isInfoEnabled()) {
				logger.info("addBannerManagementPage -- START");
			}
			
			model.addAttribute("link", "Add banner");
			
			if (logger.isInfoEnabled()) {
				logger.info("addBannerManagementPage -- END");
			}
			
			return "admin-add-banner";
		}
		
		
		
		/**
		 * This method is used to Add Banner.
		 * @param bannerModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/add-banner", method = RequestMethod.POST)
		public String addBanner(@ModelAttribute BannerModel bannerModel, HttpServletRequest httpServletRequest,
				RedirectAttributes redirectAttributes) {
			
			if (logger.isInfoEnabled()) {
				logger.info("addBanner -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = bannerModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					try {
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						bannerModel.setCreatedBy(sessionAdminModel.getAdminId());
						
						
						long result = bannerService.saveBanner(bannerModel,httpServletRequest);
						if(result > 0){
							redirectAttributes.addFlashAttribute("common.successfully", messageUtil.getBundle("common.successfully"));
							return "redirect:/admin/list-banner";
						}
					}catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}

						logger.debug(e1.getMessage(), e1);

					} catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
					} catch (SQLException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
					} catch (Exception e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
					}
				}
				else {
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
				logger.info("addBanner -- END");
			}
			
			return "redirect:/admin/add-banner";
		}
		
		
		/**
		 * This method is used to Show list of Banners.
		 * @param httpServletRequest
		 * @param model
		 * @return list-banner.jsp 
		 */
		@RequestMapping(value = "/list-banner", method = RequestMethod.GET)
		public String listBanner(HttpServletRequest httpServletRequest, Model model) {
			if (logger.isInfoEnabled()) {
				logger.info("listBanner -- START");
			}
			
			try {
				
				List<BannerModel> bannerModels =bannerService.fetchBanner();
				int size = bannerModels.size();
				int subSize = size>10?10:size;
				model.addAttribute("bannerModels",bannerModels.subList(0, subSize));
				model.addAttribute("totalSize",bannerModels.size());
				model.addAttribute("link", "List banner");
				
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
				logger.info("listBanner -- END");
			}
			
			return "list-banner";
		}
		
		
		/**
		 * This method is used to Show List of Banners for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return DataTableModel
		 */
		@RequestMapping(value = "/list-banner", method = RequestMethod.POST)
		public @ResponseBody DataTableModel viewBanner(@ModelAttribute DataTableModel dataTableModel, 
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("viewBanner -- START");
			}
			
			try {
				bannerService.fetchBanner(dataTableModel);
				
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
				logger.info("viewBanner -- END");
			}
			
			return dataTableModel;
		}
		
		/**
		 * This method is used to download Excel of Banner List.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadBannerListExcel", method = RequestMethod.GET)
	    public void downloadBannerListExcel(HttpServletResponse httpServletResponse, 
	    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
	         
	        Object[][] bookData = null;
			try {
				bookData = bannerService.exportBannersForExcel(dataTableModel, dataTableModel.getNoOfColumns());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        System.out.println(new ObjectMapper().writeValueAsString(bookData));
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        System.out.println("MIME type: " + mimeType);

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadBannerList.xlsx\"",
	          "excel file");
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

	        //workbook.close();
	        b.close();
	        outStream.close();
	    	
	    }
		
		
		/**
		 * This method is used to Open Edit Banner Page.
		 * @param httpServletRequest
		 * @param model
		 * @param bannerId
		 * @return admin-edit-banner.jsp
		 */
		@RequestMapping(value = "/edit-banner", method = RequestMethod.GET)
		public String editBanner(HttpServletRequest httpServletRequest, Model model, @RequestParam String bannerId) {

			if (logger.isInfoEnabled()) {
				logger.info("editBanner -- START");
			}

			try {
				
				BannerModel bannerModel = bannerService.fetchBannerById(Long.valueOf(bannerId));
				model.addAttribute("bannerModel", bannerModel);
				
				model.addAttribute("link", "Edit banner");
				List<Status> statusDetails = new ArrayList<Status>();
				for(Status status:Status.values()){
					
					if(status.name().toString() != Status.ACTIVATED.name().toString()){
						
						statusDetails.add(status);
					}
				}
				
				model.addAttribute("statusDetails",statusDetails);
				
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
				logger.info("editBanner -- END");
			}
			
			return "admin-edit-banner";
		}
		
		
		/**
		 * This method is used to Update Banner.
		 * @param bannerModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/edit-banner", method = RequestMethod.POST)
		public String editBanner(@ModelAttribute BannerModel bannerModel, 
				HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
			if (logger.isInfoEnabled()) {
				logger.info("editBaner -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = bannerModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					try {
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						bannerModel.setaModifiedBy(sessionAdminModel.getAdminId());
						
						long result = bannerService.updateBanner(bannerModel,httpServletRequest);
						if(result>0){
							redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
							return "redirect:/admin/list-banner";
						}
					} catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}

						logger.debug(e1.getMessage(), e1);

					}catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
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
					
			} finally {
				activeSalts.remove(salt);
				String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
				activeSalts.add(newSalt);
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("editBaner -- END");
			}
			
			return "redirect:/admin/edit-banner?bannerId="+bannerModel.getBannerId();
		}
		
		
		
		/**
		 * This method is used to Delete Banner. 
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @param bannerId
		 * @return String
		 */
		@RequestMapping(value = "/delete-banner", method = RequestMethod.GET)
		public String deleteBanner(HttpServletRequest httpServletRequest, 
				RedirectAttributes redirectAttributes, @RequestParam String bannerId) {
			if (logger.isInfoEnabled()) {
				logger.info("deleteBanner -- START");
			}
			
			try {
				AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
				
				int result = bannerService.deleteBanner(Long.valueOf(bannerId),sessionAdminModel.getAdminId());
				if(result == 1){
					redirectAttributes.addFlashAttribute("common.deleted", messageUtil.getBundle("common.deleted"));
				}else{
					new Exception();
				}
			} catch (DBConnectionException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
		
			} catch (SQLException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
			} catch (Exception e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("common.not.deleted",messageUtil.getBundle("common.not.deleted"));
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("deleteBanner -- END");
			}
			
			return "redirect:/admin/list-banner";
		}
			
			
	//################################################################### Banner Management END   ################################################################
		
		
	//################################################################### Loyalty Management START   ################################################################
		
		/**
		 * This method is used to Open Amount to Point Page.
		 * @param httpServletRequest
		 * @param model
		 * @return amount-to-point.jsp
		 */
		@RequestMapping(value = "/amount-to-point", method = RequestMethod.GET)
		public String amountToPoint(HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("amountToPoint -- START");
			}
			
			try {
				LoyaltyModel loyaltyModel = loyaltyService.getA2P();
				if(Objects.nonNull(loyaltyModel)){
					model.addAttribute("link", "Add	Point For Amount");
					model.addAttribute("loyaltyModel", loyaltyModel);
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			
			if (logger.isInfoEnabled()) {
				logger.info("amountToPoint -- END");
			}
			
			return "amount-to-point";
		}
		
		/**
		 * This method is used to Open Point to Amount Page.
		 * @param httpServletRequest
		 * @param model 
		 * @return point-to-amount.jsp
		 */
		@RequestMapping(value = "/point-to-amount", method = RequestMethod.GET)
		public String pointToAmount(HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("pointToAmount -- START");
			}
			
			try {
				LoyaltyModel loyaltyModel = loyaltyService.getP2A();
				if(Objects.nonNull(loyaltyModel)){
					model.addAttribute("link", "Add Amount For Point");
					model.addAttribute("loyaltyModel", loyaltyModel);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("pointToAmount -- END");
			}
			
			return "point-to-amount";
		}
		
		
		/**
		 *  This method is used to Configure Amount to Point.
		 * @param loyaltyModel
		 * @param bindingResult
		 * @param httpServletRequest
		 * @param model
		 * @return String
		 */
		@RequestMapping(value = "/amount-to-point", method =RequestMethod.POST)
		public String amountToPoint(@ModelAttribute LoyaltyModel loyaltyModel, BindingResult bindingResult, 
				HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("amountToPoint -- START");
			}
			
			if(bindingResult.hasErrors())
				return "redirect:/error";
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			try {
				salt = loyaltyModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
			
					try {
						
						AdminModel adminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						
						loyaltyModel = loyaltyService.saveOrUpdateA2PLayalty(loyaltyModel,adminModel.getAdminId());
						if(Objects.nonNull(loyaltyModel)){
							model.addAttribute("link", "Add Amount For Point");
							model.addAttribute("loyaltyModel", loyaltyModel);
							model.addAttribute("common.message", loyaltyModel.getMessage());
							
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
				logger.info("amountToPoint -- END");
			}
			
			return "amount-to-point";
		}
		
		/**
		 * This method is used to Configure Point to Amount.
		 * @param loyaltyModel
		 * @param bindingResult
		 * @param httpServletRequest
		 * @param model
		 * @return String
		 */
		@RequestMapping(value = "/point-to-amount", method = RequestMethod.POST)
		public String pointToAmount(@ModelAttribute LoyaltyModel loyaltyModel, BindingResult bindingResult, 
				HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("pointToAmount -- START");
			}
			
			System.out.println("loyaltyModel"+loyaltyModel);
			
			if(bindingResult.hasErrors())
				return "redirect:/error";
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			try {
				salt = loyaltyModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					try {
						
						AdminModel adminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						
						loyaltyModel = loyaltyService.saveOrUpdateP2ALayalty(loyaltyModel,adminModel.getAdminId());
						if(Objects.nonNull(loyaltyModel)){
							model.addAttribute("link", "Add Point For Amount");
							model.addAttribute("loyaltyModel", loyaltyModel);
							model.addAttribute("common.message", loyaltyModel.getMessage());
							
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
				logger.info("pointToAmount -- END");
			}
			
			return "point-to-amount";
		}	
		
	//################################################################### Loyalty Management END   ################################################################
		
	//################################################################### Ticket Management START   ################################################################
	
		/**
		 * This method is used to Show Merchant Ticket List.
		 * @param httpServletRequest
		 * @param model
		 * @return list-ticket-merchant.jsp
		 */
		@RequestMapping(value = "/list-ticket-merchant", method = RequestMethod.GET)
		public String listTicketMerchant(HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("listTicketMerchant -- START");
			}
			
			try {
				List<TicketModel> ticketModels = adminService.fetchAllTickets();
				int size = ticketModels.size();
				int subSize = size>10?10:size;
				model.addAttribute("ticketModels",ticketModels.subList(0, subSize));
				model.addAttribute("totalSize",ticketModels.size());
				model.addAttribute("link", "List Ticket");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("listTicketMerchant -- END");
			}
			
			return "list-ticket-merchant";
		}
		
		/**
		 * This method is used to Show Merchant Ticket List for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return DataTableModel
		 */
		@RequestMapping(value = "/list-ticket-merchant", method = RequestMethod.POST)
		public @ResponseBody DataTableModel listTicketMerchant(@ModelAttribute DataTableModel dataTableModel,
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("Merchant listTicketMerchant -- START");
			}
			
			try {
				adminService.fetchTickets(dataTableModel, 0l);
				
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
				logger.info("Merchant listTicketMerchant -- END");
			}
			
			return dataTableModel;
		}
		
		
		/**
		 * This method is used to download excel of Merchant Ticket List.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadListTicketMerchantExcel", method = RequestMethod.GET)
	    public void downloadListTicketMerchantExcel(HttpServletResponse httpServletResponse, 
	    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
	    	System.out.println(new ObjectMapper().writeValueAsString(dataTableModel));
	         
	        Object[][] bookData = null;
			try {
				bookData = adminService.exportTicketsForExcel(dataTableModel, dataTableModel.getNoOfColumns(), 0l);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        System.out.println(new ObjectMapper().writeValueAsString(bookData));
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        System.out.println("MIME type: " + mimeType);

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadListTicketMerchantExcel.xlsx\"",
	          "excel file");
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

	        //workbook.close();
	        b.close();
	        outStream.close();
	    	
	    }
		
		/**
		 * This method is used to open Reply Ticket Page.
		 * @param httpServletRequest
		 * @param model
		 * @param ticketId
		 * @return admin-reply-ticket.jsp
		 */
		@RequestMapping(value = "/reply-ticket", method = RequestMethod.GET)
		public String replyTicketPage(HttpServletRequest httpServletRequest, Model model, @RequestParam String ticketId) {

			if (logger.isInfoEnabled()) {
				logger.info("replyTicketPage -- START");
			}

			try {
				
				TicketModel ticketModel = adminService.fetchTicketById(Integer.parseInt(ticketId));
				model.addAttribute("ticketModel", ticketModel);
				
				model.addAttribute("link", "Reply Ticket");
				List<Status> statusDetails = new ArrayList<Status>();
				for(Status status:Status.values()){
					
					if(status.name().toString() != Status.ACTIVATED.name().toString()){
						
						statusDetails.add(status);
					}
				}
				
				model.addAttribute("statusDetails",statusDetails);
				
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
				logger.info("replyTicketPage -- END");
			}
			
			return "admin-reply-ticket";
		}
		
		/**
		 * This method is used to Reply Ticket.
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @param ticketModel
		 * @return String
		 */
		@RequestMapping(value = "/reply-ticket", method = RequestMethod.POST)
		public String replyTicket(HttpServletRequest httpServletRequest, 
				RedirectAttributes redirectAttributes, @ModelAttribute TicketModel ticketModel) {

			if (logger.isInfoEnabled()) {
				logger.info("replyTicket -- START");
			}

			try {
				
				AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
				
				ticketModel.setaModifiedBy(sessionAdminModel.getAdminId());
				
				int result = adminService.replyTicket(ticketModel);
				if(result > 0) {
					
					redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
					return "redirect:/admin/list-ticket-merchant";
				}
				else {
					
					List<Status> statusDetails = new ArrayList<Status>();
					for(Status status:Status.values()){
						
						if(status.name().toString() != Status.ACTIVATED.name().toString()){
							
							statusDetails.add(status);
						}
					}
					
					redirectAttributes.addFlashAttribute("statusDetails",statusDetails);
				}
				
				
				
			} catch (DBConnectionException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
		
			} catch (SQLException e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
			} catch (Exception e) {
				
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("replyTicket -- END");
			}
			
			return "redirect:/reply-ticket?ticketId="+ticketModel.getTicketId();
		}
		
		
	//################################################################### Ticket Management END   ################################################################
			
	//################################################################### Transaction Management START   ################################################################
		
		/**
		 * This method is used to  Show List of All Disburse Transaction List.
		 * @param httpServletRequest
		 * @param model
		 * @return list-disburse-transaction.jsp
		 */
		@RequestMapping(value = "/list-disburse-transaction", method = RequestMethod.GET)
		public String listDisburseTransaction(HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("listDisburseTransaction -- START");
			}
			
			try {
					
					List<TransactionModel> transactionModels = transactionService.fetchAllTransactions(2);
					int size = transactionModels.size();
					int subSize = size>10?10:size;
					model.addAttribute("transactionModels",transactionModels.subList(0, subSize));
					model.addAttribute("totalSize",transactionModels.size());
					model.addAttribute("link", "List Disburse Transaction");
					
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("listDisburseTransaction -- END");
			}
			
			return "list-disburse-transaction";
		}
		
		/**
		 * This method is used to show All Disburse Transaction List for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return String
		 */
		@RequestMapping(value = "/list-disburse-merchant", method = RequestMethod.POST)
		public @ResponseBody DataTableModel listDisburseMerchant(@ModelAttribute DataTableModel dataTableModel, 
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("Merchant listDisburseMerchant -- START");
			}
			
			try {
				transactionService.fetchTransactions(dataTableModel, 2);
				
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
				logger.info("Merchant listDisburseMerchant -- END");
			}
			
			return dataTableModel;
		}
		
		
		/**
		 * This method is used to download excel of Transaction List.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadListDisburseMerchantExcel", method = RequestMethod.GET)
	    public void downloadListDisburseMerchantExcel(HttpServletResponse httpServletResponse, 
	    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
	    	System.out.println(new ObjectMapper().writeValueAsString(dataTableModel));
	         
	        Object[][] bookData = null;
			try {
				bookData = transactionService.exportTransactionsForExcel(dataTableModel, dataTableModel.getNoOfColumns(), 2);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        System.out.println(new ObjectMapper().writeValueAsString(bookData));
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        System.out.println("MIME type: " + mimeType);

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadListDisburseMerchantExcel.xlsx\"",
	          "excel file");
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

	        //workbook.close();
	        b.close();
	        outStream.close();
	    	
	    }
		
		/**
		 * This method is used to show Claimed Transaction List.
		 * @param httpServletRequest
		 * @param model
		 * @return list-claimed-transaction.jsp
		 */
		@RequestMapping(value = "/list-claimed-transaction", method = RequestMethod.GET)
		public String listClaimedTransaction(HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("listClaimedTransaction -- START");
			}
			
			try {
				
				List<TransactionModel> transactionModels = transactionService.fetchAllTransactions(1);
				int size = transactionModels.size();
				int subSize = size>10?10:size;
				model.addAttribute("transactionModels",transactionModels.subList(0, subSize));
				model.addAttribute("totalSize",transactionModels.size());
				model.addAttribute("link", "List Claimed Transaction");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("listClaimedTransaction -- END");
			}
			
			return "list-claimed-transaction";
		}
		
		/**
		 * This method is used to Show Claimed Transaction List for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return DataTableModel
		 */
		@RequestMapping(value = "/list-claimed-merchant", method = RequestMethod.POST)
		public @ResponseBody DataTableModel listClaimedMerchant(@ModelAttribute DataTableModel dataTableModel,
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("Merchant listClaimedMerchant -- START");
			}
			
			try {
				transactionService.fetchTransactions(dataTableModel, 1);
				
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
				logger.info("Merchant listClaimedMerchant -- END");
			}
			
			return dataTableModel;
		}
		
		
		/**
		 * This method is used to download excel of List Claimed Transaction.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadListClaimedMerchantExcel", method = RequestMethod.GET)
	    public void downloadListClaimedMerchantExcel(HttpServletResponse httpServletResponse, 
	    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
	    	System.out.println(new ObjectMapper().writeValueAsString(dataTableModel));
	         
	        Object[][] bookData = null;
			try {
				bookData = transactionService.exportTransactionsForExcel(dataTableModel, dataTableModel.getNoOfColumns(), 1);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        System.out.println(new ObjectMapper().writeValueAsString(bookData));
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        System.out.println("MIME type: " + mimeType);

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadListClaimedMerchantExcel.xlsx\"",
	          "excel file");
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

	        //workbook.close();
	        b.close();
	        outStream.close();
	    	
	    }
		
		/**
		 * This method is used to Show List Unclaimed Transaction.
		 * @param httpServletRequest
		 * @param model
		 * @return list-unclaimed-transaction.jsp
		 */
		@RequestMapping(value = "/list-unclaimed-transaction", method = RequestMethod.GET)
		public String listUnclaimedTransaction(HttpServletRequest httpServletRequest,Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("listUnclaimedTransaction -- START");
			}
			
			try {
				
				List<TransactionModel> transactionModels = transactionService.fetchAllTransactions(0);
				int size = transactionModels.size();
				int subSize = size>10?10:size;
				model.addAttribute("transactionModels",transactionModels.subList(0, subSize));
				model.addAttribute("totalSize",transactionModels.size());
				model.addAttribute("link", "List UnClaimed Transaction");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("listUnclaimedTransaction -- END");
			}
			
			return "list-unclaimed-transaction";
		}
		
		
		/**
		 * This method is used to Show List of UnClaimed Transaction for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return DataTableModel
		 */
		@RequestMapping(value = "/list-unclaimed-merchant", method = RequestMethod.POST)
		public @ResponseBody DataTableModel listUnclaimedMerchant(@ModelAttribute DataTableModel dataTableModel, 
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("Merchant listUnclaimedMerchant -- START");
			}
			
			try {
				transactionService.fetchTransactions(dataTableModel, 0);
				
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
				logger.info("Merchant listUnclaimedMerchant -- END");
			}
			
			return dataTableModel;
		}
		
		
		/**
		 * This method is used to download excel Merchant Unclaimed Transaction List.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadListUnclaimedMerchantExcel", method = RequestMethod.GET)
	    public void downloadListUnclaimedMerchantExcel(HttpServletResponse httpServletResponse, 
	    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
	    	System.out.println(new ObjectMapper().writeValueAsString(dataTableModel));
	         
	        Object[][] bookData = null;
			try {
				bookData = transactionService.exportTransactionsForExcel(dataTableModel, dataTableModel.getNoOfColumns(), 0);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        System.out.println(new ObjectMapper().writeValueAsString(bookData));
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        System.out.println("MIME type: " + mimeType);

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadListUnclaimedMerchantExcel.xls\"",
	          "excel file");
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

	        //workbook.close();
	        b.close();
	        outStream.close();
	    	
	    }
		
	//################################################################### Transaction Management END   ########################################################
		
	//############################################################# Contact Us START #########################################################################
		
		/**
		 * This method is used to Show List Contacts.
		 * @param httpServletRequest
		 * @param model
		 * @return list-contactus.jsp.
		 */
		@RequestMapping(value = "/list-contactus", method = RequestMethod.GET)
		public String listContactUs(HttpServletRequest httpServletRequest, Model model) {
			
			if (logger.isInfoEnabled()) {
				logger.info("listContactUs -- START");
			}
			
			try {
				
				List<ContactUsModel> contactUsModels = contactUsService.fetchAllContactUs();
				int size = contactUsModels.size();
				int subSize = size>10?10:size;
				model.addAttribute("contactUsModels",contactUsModels.subList(0, subSize));
				model.addAttribute("totalSize",contactUsModels.size());
				model.addAttribute("link", "List Contact Us");
				
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
				logger.info("listContactUs -- END");
			}
			
			return "list-contactus";
		}
		
		/**
		 * This method is used to Show List Contacts for Pagination.
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @param model
		 * @return DataTableModel.
		 */
		@RequestMapping(value = "/list-contactus", method = RequestMethod.POST)
		public @ResponseBody DataTableModel viewQuery(@ModelAttribute DataTableModel dataTableModel, 
				HttpServletRequest httpServletRequest, Model model) {

			if (logger.isInfoEnabled()) {
				logger.info("viewContactUs -- START");
			}
			
			try {
				contactUsService.fetchContactUsAdmin(dataTableModel);
				
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
				logger.info("viewContactUs -- END");
			}
			
			return dataTableModel;
		}
		
		/**
		 * This method is used to download excel for Contacts List.
		 * @param httpServletResponse
		 * @param dataTableModel
		 * @param httpServletRequest
		 * @throws IOException
		 */
		@RequestMapping(value = "/downloadContactUsListExcel", method = RequestMethod.GET)
	    public void downloadContactUsListExcel(HttpServletResponse httpServletResponse,
	    		@ModelAttribute DataTableModel dataTableModel, HttpServletRequest httpServletRequest) throws IOException {
	         
			if (logger.isInfoEnabled()) {
				logger.info("downloadContactUsListExcel -- START");
			}
			
	        Object[][] bookData = null;
			try {
				bookData = contactUsService.exportContactUsForExcelAdmin(dataTableModel, dataTableModel.getNoOfColumns());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 
	        XSSFWorkbook xssfWorkbook = createExcelService.getExcel(bookData);
	    	
	    	String mimeType = "application/vnd.ms-excel";

	        httpServletResponse.setContentType(mimeType);

	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"downloadContactUsList.xls\"", "excel file");
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

	        //workbook.close();
	        b.close();
	        outStream.close();
	        
	        if (logger.isInfoEnabled()) {
				logger.info("downloadContactUsListExcel -- START");
			}
	    	
	    }
		
		/**
		 * This method is used to Open Reply Contact Page.
		 * @param httpServletRequest
		 * @param model
		 * @param contactUsId
		 * @return admin-reply-contactus.jsp
		 */
		@RequestMapping(value = "/reply-contactus", method = RequestMethod.GET)
		public String replyContactUs(HttpServletRequest httpServletRequest, Model model, @RequestParam String contactUsId) {

			if (logger.isInfoEnabled()) {
				logger.info("replyContactUs -- START");
			}

			try {
				
				ContactUsModel contactUsModel = contactUsService.fetchContactUsByID(Long.valueOf(contactUsId));
				model.addAttribute("contactUsModel", contactUsModel);
				List<Status> statusDetails = new ArrayList<Status>();
				for(Status status:Status.values()){
					
					if(status.name().toString() != Status.ACTIVATED.name().toString()){
						
						statusDetails.add(status);
					}
				}
				model.addAttribute("link", "Reply Query");
				model.addAttribute("statusDetails",statusDetails);
				
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
				logger.info("replyContactUs -- END");
			}
			
			return "admin-reply-contactus";
		}
		
		/**
		 * This method is used to Reply Contact.
		 * @param contactUsModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/reply-contactus", method = RequestMethod.POST)
		public String replyContactUs(@ModelAttribute ContactUsModel contactUsModel, 
				HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
			
			if (logger.isInfoEnabled()) {
				logger.info("replyContactUs -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = contactUsModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
					
					try {
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						contactUsModel.setModifiedBy(sessionAdminModel.getAdminId());
						
						long result = contactUsService.replyContactUs(contactUsModel);
						if(result > 0){
							
							redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
							redirectAttributes.addFlashAttribute("link", "List Contact Us");
							return "redirect:/admin/list-contactus";
						}
					} catch (FormExceptions e1) {
		
						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}
		
						redirectAttributes.addFlashAttribute("contactUsModel", contactUsModel);
					}catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
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
				
			} finally {
				activeSalts.remove(salt);
				String newSalt = (String) httpServletRequest.getAttribute("csrfPreventionSaltPage");
				activeSalts.add(newSalt);
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("replyContactUs -- END");
			}
			
			return "redirect:/admin/reply-contactus?contactUsId="+contactUsModel.getContactUsId();
		}
		
//############################################################# Contact Us START ################################################
		
		/**
		 * This method is used to Open Page of Create UserId.
		 * @param httpServletRequest
		 * @param model
		 * @param merchantId
		 * @return admin-create-userid-password-merchant.jsp
		 */
		@RequestMapping(value = "/create-userid-password", method = RequestMethod.GET)
		public String createUserIdAndPassword(HttpServletRequest httpServletRequest, Model model, @RequestParam String merchantId) {
			if (logger.isInfoEnabled()) {
				logger.info("createUserIdAndPassword GET -- START");
			}
			try {
				
				MerchantModel merchantModel = adminService.fetchActiveAndActivateMerchantById(Long.valueOf(merchantId));
				
				if(Objects.isNull(merchantModel)){
					return "redirect:/error404";
				}
				
				model.addAttribute("merchantModel",merchantModel);
				model.addAttribute("link", "Merchant's Bank Details");
				
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
				logger.info("createUserIdAndPassword GET -- END");
			}
			
			return "admin-create-userid-password-merchant";
		}
		
		/**
		 * This method is used to Create UserId. 
		 * @param merchantModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/create-userid-password", method = RequestMethod.POST)
		public String createUserIdAndPassword(@ModelAttribute MerchantModel merchantModel, 
				HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
			if (logger.isInfoEnabled()) {
				logger.info("createUserIdAndPassword POST -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = merchantModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
				
					try {
						
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						merchantModel.setaModifiedBy(sessionAdminModel.getAdminId());
						
						
						long result = adminService.createUserIdAndPassword(merchantModel);
						if(result > 0){
							redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
							
								return "redirect:/admin/list-active-merchant";
							
						}
					} catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}

						logger.debug(e1.getMessage(), e1);

					}catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
					} catch (SQLException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
					} catch (Exception e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
					}
				}else {
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
				logger.info("createUserIdAndPassword POST -- END");
			}
			
			return "redirect:/admin/create-userid-password?merchantId="+merchantModel.getMerchantId();
		}
		
		/**
		 * This method is used to Open Set CardPercentage Page.
		 * @param httpServletRequest
		 * @param model
		 * @param merchantId
		 * @return admin-set-card-percentage.jsp
		 */
		@RequestMapping(value = "/set-card-percentage", method = RequestMethod.GET)
		public String setCardPercentageOfMerchant(HttpServletRequest httpServletRequest, Model model, 
				@RequestParam String merchantId) {
				
			if (logger.isInfoEnabled()) {
					logger.info("setCardPercentageOfMerchant -- START");
				}
				try {
					
					MerchantModel merchantModel = adminService.fetchActiveAndActivateMerchantById(Long.valueOf(merchantId));
					
					if(Objects.isNull(merchantModel)){
						return "redirect:/error404";
					}
					
					try {
						
						List<CardTypeModel> cardTypeModels = adminService.fetchCardTypeList();
						model.addAttribute("cardTypeModels",cardTypeModels);
						
					} catch(Exception e){
						e.printStackTrace();
						
					}
					
					try{
						
						List<MerchantCardPercentageModel> merchantCardPercentageModels =  adminService.fetchCardPercentageByMerchantId(merchantId);
						model.addAttribute("merchantCardPercentageModels",merchantCardPercentageModels);
						
					} catch(Exception e){
						e.printStackTrace();
					}

					
					model.addAttribute("merchantModel",merchantModel);
					
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
					logger.info("setCardPercentageOfMerchant -- END");
				}
				
				return "admin-set-card-percentage";
			}
		
		/**
		 * This method is used to Set Card Percentage.
		 * @param merchantCardPercentageModel
		 * @param httpServletRequest
		 * @param redirectAttributes
		 * @return String
		 */
		@RequestMapping(value = "/admin-set-card-percentage", method = RequestMethod.POST)
		public String setCardPercentageOfMerchant(@ModelAttribute MerchantCardPercentageModel merchantCardPercentageModel, 
				HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
			
			if (logger.isInfoEnabled()) {
				logger.info("setCardPercentageOfMerchant POST -- START");
			}
			
			String sessionId = httpServletRequest.getSession().getId();
			List<String> activeSalts = SALT_TRACKER.get(sessionId);
			String salt = "";
			
			try {
				
				salt = merchantCardPercentageModel.getCsrfPreventionSalt();

				if (!org.springframework.util.CollectionUtils.isEmpty(activeSalts)& activeSalts.contains(salt)) {
				
					try {
						
						long result = 0L;
						AdminModel sessionAdminModel = (AdminModel) httpServletRequest.getSession().getAttribute("adminModel");
						merchantCardPercentageModel.setCreatedBy(sessionAdminModel.getAdminId());
						
						List<MerchantCardPercentageModel> merchantCardPercentageModels =  adminService.fetchCardPercentageByMerchantId(merchantCardPercentageModel.getMerchantId());
						
						if (Objects.nonNull(merchantCardPercentageModels) && merchantCardPercentageModels != null && merchantCardPercentageModels.size() > 0){ // Update
							
							merchantCardPercentageModel.setaModifiedBy(sessionAdminModel.getAdminId());
							result = adminService.updateCardPercentageByMerchantId(merchantCardPercentageModel);
							
						} else { // insert
							
							result = adminService.saveCardPercentageByMerchantId(merchantCardPercentageModel);
							
						}
						
						if(result > 0){
							
							redirectAttributes.addFlashAttribute("common.updated", messageUtil.getBundle("common.updated"));
							
								return "redirect:/admin/list-active-merchant";
							
						}
					} catch (FormExceptions e1) {

						for (Entry<String, Exception> entry : ((FormExceptions) e1).getExceptions().entrySet()) {
							System.out.println("key :: " + entry.getKey()+ " value :: " + entry.getValue().getMessage());
							redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue().getMessage());
						}

						logger.debug(e1.getMessage(), e1);

					}catch (DBConnectionException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("database.not.found", messageUtil.getBundle("database.not.found"));
				
					} catch (SQLException e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("general.pblm", messageUtil.getBundle("general.pblm"));
					} catch (Exception e) {
						
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("common.error", messageUtil.getBundle("common.error"));
					}
				}else {
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
				logger.info("setCardPercentageOfMerchant POST -- END");
			}
			
			return "redirect:/admin/set-card-percentage?merchantId="+merchantCardPercentageModel.getMerchantId();
		}
}