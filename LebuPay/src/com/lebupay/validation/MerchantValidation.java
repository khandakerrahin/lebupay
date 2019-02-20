/**
 * @formatter:off
 *
 */
package com.lebupay.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.lebupay.common.MessageUtil;
import com.lebupay.common.Util;
import com.lebupay.dao.MerchantDao;
import com.lebupay.dao.ParameterDAO;
import com.lebupay.dao.TransactionDAO;
import com.lebupay.exception.EmptyValueException;
import com.lebupay.exception.FormExceptions;
import com.lebupay.exception.ImageFormatMismatch;
import com.lebupay.model.CheckoutModel;
import com.lebupay.model.CompanyModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.EmailInvoicingModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.ParameterModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.QuickPayModel;
import com.lebupay.model.TicketModel;

/**
 * This is MerchantValidation Class is used to validate all operations performed by Merchant Module.
 * @author Java Team
 *
 */
@Component
public class MerchantValidation {
	
	private static Logger logger = Logger.getLogger(MerchantValidation.class);

	private static final Tika TIKA = new Tika();
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private TransactionDAO transactionDao;
	
	@Autowired
	private ParameterDAO parameterDao;
	
	/**
	 * This method is use to validated sign up details of the Merchant.
	 * @param merchantModel
	 * @param httpServletRequest
	 * @throws Exception
	 */
	public void signUpValidation(MerchantModel merchantModel, HttpServletRequest httpServletRequest) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant signUpValidation -- START");
		}

		List<String> exe = new ArrayList<String>();

		if (Util.isEmpty(merchantModel.getFirstName())) {
			
			exe.add(messageUtil.getBundle("merchant.first.name.required"));
			
		} else{
			
			merchantModel.setFirstName(Util.strip_html_tags(merchantModel.getFirstName(), "url"));

			if(Util.isEmpty(merchantModel.getFirstName())) {
				
				exe.add(messageUtil.getBundle("merchant.first.name.required"));
			
			}
		}

		if (Util.isEmpty(merchantModel.getLastName())) {
			
			exe.add(messageUtil.getBundle("merchant.last.name.required"));
			
		} else{
			
			merchantModel.setLastName(Util.strip_html_tags(merchantModel.getLastName(), "url"));

			if(Util.isEmpty(merchantModel.getLastName())) {
				
				exe.add(messageUtil.getBundle("merchant.last.name.required"));
			
			}
		}

		if (Util.isEmpty(merchantModel.getEmailId())) {
			
			exe.add(messageUtil.getBundle("merchant.email.required"));
			
		} else{
			
			merchantModel.setEmailId(Util.strip_html_tags(merchantModel.getEmailId(), "url"));

			if(Util.isEmpty(merchantModel.getEmailId())) {
				
				exe.add(messageUtil.getBundle("merchant.email.required"));
			
			} else {
				
					if(Util.emailValidator(merchantModel.getEmailId())) {
					
						exe.add(messageUtil.getBundle("merchant.email.invalid"));
						
					} else {
						
						try{
							
							MerchantModel checkModel = merchantDao.createEmailCheck(merchantModel.getEmailId());
							
							if(Objects.nonNull(checkModel)){
								
								exe.add(messageUtil.getBundle("merchant.email.exist"));
							}
							
						} catch (Exception e) {
							
							exe.add(messageUtil.getBundle("merchant.email.exist"));
						}
					}
			}
		}
		
		if (Util.isEmpty(merchantModel.getMobileNo())) {
			
			exe.add(messageUtil.getBundle("merchant.mobile.no.required"));
			
		} else {
			
			merchantModel.setMobileNo(Util.strip_html_tags(merchantModel.getMobileNo(), "url"));
			if (Util.isEmpty(merchantModel.getMobileNo())) {
				
				exe.add(messageUtil.getBundle("merchant.mobile.no.required"));
				
			} else {
				
				if (merchantModel.getMobileNo().length() != 14 || !merchantModel.getMobileNo().contains("+880")) {
					
					exe.add(messageUtil.getBundle("merchant.mobile.no.length"));
					
				} else {
				
					try{
						
						MerchantModel checkModel = merchantDao.createPhoneNoCheck(merchantModel.getMobileNo());
						
						if(Objects.nonNull(checkModel)){
							
							exe.add(messageUtil.getBundle("merchant.mobile.no.already.exist"));
						}
						
					} catch (Exception e) {
						
						exe.add(messageUtil.getBundle("merchant.mobile.no.already.exist"));
					}
				}
			}
		}

		if (Util.isEmpty(merchantModel.getPassword())) {
			
			exe.add(messageUtil.getBundle("merchant.password.required"));
			
		} else {
			
			merchantModel.setPassword(Util.strip_html_tags(merchantModel.getPassword(), "url"));
			if (Util.isEmpty(merchantModel.getPassword())) {
				
				exe.add(messageUtil.getBundle("merchant.password.required"));
				
			}
		}
		
		if (Util.isEmpty(merchantModel.getConfirmPassword())) {
			
			exe.add(messageUtil.getBundle("merchant.password.required"));
			
		} else {
			
			merchantModel.setConfirmPassword(Util.strip_html_tags(merchantModel.getConfirmPassword(), "url"));
			if (Util.isEmpty(merchantModel.getConfirmPassword())) {
				
				exe.add(messageUtil.getBundle("merchant.password.required"));
				
			}
		}

		if (!(Util.isEmpty(merchantModel.getPassword()) && !(Util.isEmpty(merchantModel.getConfirmPassword())))) {

			if (!(merchantModel.getPassword().equals((merchantModel.getConfirmPassword())))) {

				exe.add(messageUtil.getBundle("merchant.password.and.confirm-password.not.matching"));
			} else {
				
				if(!Util.checkPasswordPolicy(merchantModel.getConfirmPassword())) {
					
					exe.add(messageUtil.getBundle("merchant.password.format"));

				} 
			}
		}
		
		if(Util.isEmpty(merchantModel.getCaptcha())){
			
			exe.add(messageUtil.getBundle("captcha.null"));
			
		} else {
			
			merchantModel.setCaptcha(Util.strip_html_tags(merchantModel.getCaptcha(), "url"));
			if (Util.isEmpty(merchantModel.getCaptcha())) {
				
				exe.add(messageUtil.getBundle("captcha.null"));
				
			}
			
			HttpSession httpSession = httpServletRequest.getSession(false);

			String captcha = (String) httpSession.getAttribute("captcha");
			String code = merchantModel.getCaptcha();
			
			if (captcha.equals(code)) {
				
				httpSession.removeAttribute("captcha");
				
			} else {
				
				httpSession.removeAttribute("captcha");
				exe.add(messageUtil.getBundle("captcha.failed"));
			}
		}

		if (exe.size() > 0)
			throw new FormExceptions(exe);

		if (logger.isInfoEnabled()) {
			logger.info("Merchant signUpValidation -- END");
		}
	}
	
	/**
	 * This method is use to validated login credential of Merchant.
	 * @param merchantModel
	 * @throws Exception
	 */
	public void loginValidation(MerchantModel merchantModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant loginValidation -- START");
		}

		if (Util.isEmpty(merchantModel.getUserName())) {
			
			throw new Exception(messageUtil.getBundle("merchant.user.name.required"));
			
		} else {

			merchantModel.setUserName(Util.strip_html_tags(merchantModel.getUserName(), "url"));
			if (Util.isEmpty(merchantModel.getUserName())) {
				
				throw new Exception(messageUtil.getBundle("merchant.user.name.required"));
				
			} else {
				
				if (merchantModel.getUserName().contains("@")) {
					
					try {
						
						MerchantModel emailCheck = merchantDao.createEmailCheck(merchantModel.getUserName());
						
						if(Objects.isNull(emailCheck)){
							
							throw new Exception(messageUtil.getBundle("merchant.email.not.exist"));
						}
						
					} catch(Exception e) {
						
						throw new Exception(messageUtil.getBundle("merchant.email.not.exist"));
					}
					
					
				} else {

					try {
						
						MerchantModel mobileCheck = merchantDao.createPhoneNoCheck(merchantModel.getUserName());
						
						if(Objects.isNull(mobileCheck)){
							
							throw new Exception(messageUtil.getBundle("merchant.mobile.not.exist"));
						}
						
					} catch(Exception e) {
						
						throw new Exception(messageUtil.getBundle("merchant.mobile.not.exist"));
					}
				}
			}
			
			if (Util.isEmpty(merchantModel.getPassword())) {
				
				throw new Exception("Password Required");
				
			} else {
				
				merchantModel.setPassword(Util.strip_html_tags(merchantModel.getPassword(), "url"));
				if (Util.isEmpty(merchantModel.getPassword())) {
					
					throw new Exception("Password Required");
					
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("Merchant loginValidation -- END");
			}
		}
	}
	
	
	/**
	 * This method is use to validated forgot password details.
	 * @param merchantModel
	 * @throws FormExceptions
	 */
	public void forgotPassword(MerchantModel merchantModel) throws FormExceptions {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant forgotPassword -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();

		if (Util.isEmpty(merchantModel.getMerchantId())) {

			exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.id.required")));
			
		} else{
			
			if(!Util.isNumeric(merchantModel.getMerchantId().toString())) {
				
				exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.id.required")));
			}
		}

		if (Util.isEmpty(merchantModel.getPassword())) {
			
			exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.password.required")));
			
		} else{
			
			merchantModel.setPassword(Util.strip_html_tags(merchantModel.getPassword(), "url"));

			if(Util.isEmpty(merchantModel.getPassword())) {
				
				exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.password.required")));
			
			}
			
		}

		if (Util.isEmpty(merchantModel.getConfirmPassword())) {
			
			exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.password.required")));
			
		} else {
			
			merchantModel.setConfirmPassword(Util.strip_html_tags(merchantModel.getConfirmPassword(), "url"));
			if (Util.isEmpty(merchantModel.getConfirmPassword())) {
				
				exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.password.required")));
				
			}
		}
		
		if (!(Util.isEmpty(merchantModel.getPassword()) && !(Util.isEmpty(merchantModel.getConfirmPassword())))) {
			
			if (!(merchantModel.getPassword().equals((merchantModel.getConfirmPassword())))) {
				
				exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.password.and.confirm-password.not.matching")));
				
			} else {
				
				if(!Util.checkPasswordPolicy(merchantModel.getConfirmPassword())) {
					
					exceptions.put("error", new EmptyValueException(messageUtil.getBundle("merchant.password.format")));

				} 
			}
		} 
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("Merchant forgotPassword -- END");
		}
	}
	
	/**
	 * This method is use to validated change password details.
	 * @param merchantModel
	 * @throws Exception
	 */
	public void changePassword(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- START");
		}

		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();

		if (Util.isEmpty(merchantModel.getMerchantId())) {

			exceptions.put("merchant.id.required", new EmptyValueException(messageUtil.getBundle("merchant.id.required")));
			
		} else {
			
			if (!Util.isNumeric(merchantModel.getMerchantId().toString())) {

				exceptions.put("merchant.id.required", new EmptyValueException(messageUtil.getBundle("merchant.id.required")));
				
			}
		}

		if (Util.isEmpty(merchantModel.getPassword())) {
			
			exceptions.put("merchant.old.password.required",new EmptyValueException(messageUtil.getBundle("merchant.old.password.required")));
			
		} else{
			
			merchantModel.setPassword(Util.strip_html_tags(merchantModel.getPassword(), "url"));

			if(Util.isEmpty(merchantModel.getPassword())) {
				
				exceptions.put("merchant.old.password.required",new EmptyValueException(messageUtil.getBundle("merchant.old.password.required")));

			}
		}
		
		if (Util.isEmpty(merchantModel.getConfirmPassword())) {
			
			exceptions.put("merchant.new.password.required",new EmptyValueException(messageUtil.getBundle("merchant.new.password.required")));
			
		} else{
			
			merchantModel.setConfirmPassword(Util.strip_html_tags(merchantModel.getConfirmPassword(), "url"));

			if(Util.isEmpty(merchantModel.getConfirmPassword())) {
				
				exceptions.put("merchant.new.password.required",new EmptyValueException(messageUtil.getBundle("merchant.new.password.required")));

			}

			}

		if (Util.isEmpty(merchantModel.getConfirmNewPassword())) {
			
			exceptions.put("merchant.confirm.password.required",new EmptyValueException(messageUtil.getBundle("merchant.confirm.password.required")));
			
		} else{
			
			merchantModel.setConfirmNewPassword(Util.strip_html_tags(merchantModel.getConfirmNewPassword(), "url"));

			if(Util.isEmpty(merchantModel.getConfirmNewPassword())) {
				
				exceptions.put("merchant.confirm.password.required",new EmptyValueException(messageUtil.getBundle("merchant.confirm.password.required")));

			}

		}

		if (!(Util.isEmpty(merchantModel.getConfirmPassword()) && !(Util.isEmpty(merchantModel.getConfirmNewPassword())))) {

			if (!(merchantModel.getConfirmPassword().equals((merchantModel.getConfirmNewPassword())))) {

				exceptions.put("merchant.password.and.confirm-password.not.matching",new EmptyValueException(messageUtil.getBundle("merchant.password.and.confirm-password.not.matching")));
			
			} else {
				
				if(!Util.checkPasswordPolicy(merchantModel.getConfirmNewPassword())) {
					
					exceptions.put("merchant.password.format",new EmptyValueException(messageUtil.getBundle("merchant.password.format")));

				}
			}
		}

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- END");
		}
	}
	
	
	/**
	 * This method is use to validate the Profile Management Of Merchant.
	 * @param merchantModel
	 * @throws Exception
	 */
	public void merchantProfileValidation(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant merchantProfileValidation -- START");
		}

		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if (Util.isEmpty(merchantModel.getFirstName())) {
			
			exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
			
		} else{
			
			merchantModel.setFirstName(Util.strip_html_tags(merchantModel.getFirstName(), "url"));

			if(Util.isEmpty(merchantModel.getFirstName())) {
				
				exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
			
			}
		}

		if (Util.isEmpty(merchantModel.getLastName())) {
			
			exceptions.put("merchant.last.name.required",new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
		} else{
			
			merchantModel.setLastName(Util.strip_html_tags(merchantModel.getLastName(), "url"));

			if(Util.isEmpty(merchantModel.getLastName())) {
				
				exceptions.put("merchant.last.name.required",new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
			}
		}

		if (Util.isEmpty(merchantModel.getEmailId())) {
			
			exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
			
		} else{
			
			merchantModel.setEmailId(Util.strip_html_tags(merchantModel.getEmailId(), "url"));

			if(Util.isEmpty(merchantModel.getEmailId())) {
				
				exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
			
			} else {
				
					if(Util.emailValidator(merchantModel.getEmailId())) {
					
						exceptions.put("merchant.email.invalid", new EmptyValueException(messageUtil.getBundle("merchant.email.invalid")));
						
					} else {
						
						try{
							
							MerchantModel checkModel = merchantDao.updateEmailCheck(merchantModel.getEmailId(),merchantModel.getMerchantId());
							
							if(Objects.nonNull(checkModel)){
								
								exceptions.put("merchant.email.exist", new Exception(messageUtil.getBundle("merchant.email.exist")));
							}
							
						} catch (Exception e) {
							
							exceptions.put("merchant.email.exist", new Exception(messageUtil.getBundle("merchant.email.exist")));
						}
					}
			}
		}
		
		if (Util.isEmpty(merchantModel.getMobileNo())) {
			
			exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
			
		} else {
			
			merchantModel.setMobileNo(Util.strip_html_tags(merchantModel.getMobileNo(), "url"));
			if (Util.isEmpty(merchantModel.getMobileNo())) {
				
				exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
				
			} else {
				
				if (merchantModel.getMobileNo().length() != 14 || !merchantModel.getMobileNo().contains("+880")) {
					
					exceptions.put("merchant.mobile.no.length", new Exception(messageUtil.getBundle("merchant.mobile.no.length")));
					
				} else {
					
					try{
						
						MerchantModel checkModel = merchantDao.updatePhoneNoCheck(merchantModel.getMobileNo(),merchantModel.getMerchantId());
						
						if(Objects.nonNull(checkModel)){
							
							exceptions.put("merchant.mobile.no.already.exist",new Exception(messageUtil.getBundle("merchant.mobile.no.already.exist")));
						}
						
					} catch (Exception e) {
						
						exceptions.put("merchant.mobile.no.already.exist",new Exception(messageUtil.getBundle("merchant.mobile.no.already.exist")));
					}
				}
				
			}
		}
		
		if(Util.isEmpty(merchantModel.getPhoneCode())){
			
			exceptions.put("common.phone.code.required", new EmptyValueException(messageUtil.getBundle("common.phone.code.required")));
			
		} else {
			
			if(!Util.isNumeric(merchantModel.getPhoneCode().toString())){
				
				exceptions.put("common.phone.code.required", new EmptyValueException(messageUtil.getBundle("common.phone.code.required")));
				
			} else {
				
				if(!(merchantModel.getPhoneCode().equals(merchantDao.fetchActiveMerchantById(merchantModel.getMerchantId()).getPhoneCode()))){
					
					exceptions.put("common.invalid.phone.code", new EmptyValueException(messageUtil.getBundle("common.invalid.phone.code")));
				}
			}
		}

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("Merchant merchantProfileValidation -- END");
		}
	}
	
	/**
	 * This method is used for Company Info Validation.
	 * @param companyModel
	 * @throws Exception
	 */
	public void companyInfoValidation(CompanyModel companyModel) throws Exception {

	  if (logger.isInfoEnabled()) {
	   logger.info("Merchant companyInfoValidation -- START");
	  }
	  
	  Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
	  
	  if (Util.isEmpty(companyModel.getCodeSec())) {
		  
	   exceptions.put("merchant.companycategoryCode.required", new EmptyValueException("merchant.companycategoryCode.required"));
	   
	  } else {
		  
		  companyModel.setCodeSec(Util.strip_html_tags(companyModel.getCodeSec(), "url"));
		  if (Util.isEmpty(companyModel.getCodeSec())) {
			  
			   exceptions.put("merchant.companycategoryCode.required", new EmptyValueException("merchant.companycategoryCode.required"));
			   
		  }
	  }
	  
	  String servicetype = "";
	  if(Objects.nonNull(companyModel.getServices())){
	   for(String service:companyModel.getServices()){
	    
	    servicetype += service + "|||";
	    
	   }
	   
	   servicetype = servicetype.substring(0, servicetype.length() - 3);
	   companyModel.setService(servicetype);
	  }
	  String otherBankType="";
	  if(Objects.nonNull(companyModel.getOtherBanks())){
	   for(String otherBank:companyModel.getOtherBanks()){
	    
	    otherBankType += otherBank + "|||";
	    
	   }
	   otherBankType = otherBankType.substring(0, otherBankType.length() - 3);
	   
	   if(companyModel.getOthers()!=null){
	    
	    otherBankType = otherBankType +"|||"+"("+companyModel.getOthers()+")";
	   }
	   
	   
	   companyModel.setOtherBank(otherBankType);
	  }
	  
	  
	  if (Util.isEmpty(companyModel.getCompanyName())) {
		  
	   exceptions.put("merchant.companyname.required", new EmptyValueException("merchant.companyname.required"));
	   
	  } else {
		  
		  companyModel.setCompanyName(Util.strip_html_tags(companyModel.getCompanyName(), "url"));

		  if(Util.isEmpty(companyModel.getCompanyName())) {
			  
			  exceptions.put("merchant.companyname.required", new EmptyValueException("merchant.companyname.required"));
			   
			 }
	  }

	  if (Util.isEmpty(companyModel.getDba())) {
		  
	   exceptions.put("merchant.company.dba.required", new EmptyValueException("merchant.company.dba.required"));
	   
	  } else {
		  
		  companyModel.setDba(Util.strip_html_tags(companyModel.getDba(), "url"));

		  if(Util.isEmpty(companyModel.getDba())) {
			  
			  exceptions.put("merchant.company.dba.required", new EmptyValueException("merchant.company.dba.required"));
			   
			 }
	  }

	  if (Util.isEmpty(companyModel.getService())) {
		  
	   exceptions.put("merchant.company.service.required", new EmptyValueException("merchant.company.service.required"));
	   
	  } else {
		  
		  companyModel.setService(Util.strip_html_tags(companyModel.getService(), "url"));

		  if(Util.isEmpty(companyModel.getService())) {
			  
			  exceptions.put("merchant.company.service.required", new EmptyValueException("merchant.company.service.required"));
			   
			 }
	  } 

	  if (Util.isEmpty(companyModel.getYearsInBusiness())) {
		  
	   exceptions.put("merchant.company.yearsinbusiness.required", new EmptyValueException("merchant.company.yearsinbusiness.required"));
	   
	  } else {
		  
		  if(!Util.isNumeric(companyModel.getYearsInBusiness().toString())) {
			  
			  exceptions.put("merchant.company.yearsinbusiness.required", new EmptyValueException("merchant.company.yearsinbusiness.required"));
			   
			 }
	  }

	  
	  if (Util.isEmpty(companyModel.getPhone())) {
		  
	   exceptions.put("merchant.company.phone.required", new EmptyValueException("merchant.company.phone.required"));
	   
	  } else {
		  
		  companyModel.setPhone(Util.strip_html_tags(companyModel.getPhone(), "url"));

		  if(Util.isEmpty(companyModel.getPhone())) {
			  
			  exceptions.put("merchant.company.phone.required", new EmptyValueException("merchant.company.phone.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getFax())) {
		  
	   exceptions.put("merchant.company.fax.required", new EmptyValueException("merchant.company.fax.required"));
	   
	  } else {
		  
		  companyModel.setFax(Util.strip_html_tags(companyModel.getFax(), "url"));

		  if(Util.isEmpty(companyModel.getFax())) {
			  
			  exceptions.put("merchant.company.fax.required", new EmptyValueException("merchant.company.fax.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getWebsite())) {
		  
	   exceptions.put("merchant.company.website.required", new EmptyValueException("merchant.company.website.required"));
	   
	  } else {
		  
		  companyModel.setWebsite(Util.strip_html_tags(companyModel.getWebsite(), "url"));

		  if(Util.isEmpty(companyModel.getWebsite())) {
			  
			  exceptions.put("merchant.company.website.required", new EmptyValueException("merchant.company.website.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getIp())) {
		  
	   exceptions.put("merchant.company.ip.required", new EmptyValueException("merchant.company.ip.required"));
	   
	  } else {
		  
		  companyModel.setIp(Util.strip_html_tags(companyModel.getIp(), "url"));

		  if(Util.isEmpty(companyModel.getIp())) {
			  
			  exceptions.put("merchant.company.ip.required", new EmptyValueException("merchant.company.ip.required"));
			   
		  } else {
			  
			  if(!Util.validateIP(companyModel.getIp())) {
				  
				  exceptions.put("merchant.company.ip.required", new EmptyValueException("merchant.company.ip.invalid"));
			  }
		  }
	  }
	  
	  if (Util.isEmpty(companyModel.getAddress())) {
		  
	   exceptions.put("merchant.company.address.required", new EmptyValueException("merchant.company.address.required"));
	   
	  } else {
		  
		  companyModel.setAddress(Util.strip_html_tags(companyModel.getAddress(), "url"));

		  if(Util.isEmpty(companyModel.getAddress())) {
			  
			  exceptions.put("merchant.company.address.required", new EmptyValueException("merchant.company.address.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getNetWorth())) {
		  
	   exceptions.put("merchant.networth.required", new EmptyValueException("merchant.networth.required"));
	   
	  } else {
		  
		  companyModel.setNetWorth(Double.parseDouble(Util.strip_html_tags(companyModel.getNetWorth().toString(), "url")));
		  if (Util.isEmpty(companyModel.getNetWorth())) {
			  
			  exceptions.put("merchant.networth.required", new EmptyValueException("merchant.networth.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getOtherBank())) {
		  
	   exceptions.put("merchant.otherBank.required", new EmptyValueException("merchant.otherBank.required"));
	   
	  } else {
		  
		  companyModel.setOtherBank(Util.strip_html_tags(companyModel.getOtherBank(), "url"));

		  if(Util.isEmpty(companyModel.getOtherBank())) {
			  
			  exceptions.put("merchant.otherBank.required", new EmptyValueException("merchant.otherBank.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getNid())) {
		  
	   exceptions.put("merchant.nid.required", new EmptyValueException("merchant.nid.required"));
	   
	  } else {
		  
		  companyModel.setNid(Util.strip_html_tags(companyModel.getNid(), "url"));

		  if(Util.isEmpty(companyModel.getNid())) {
			  
			  exceptions.put("merchant.nid.required", new EmptyValueException("merchant.nid.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getContactPerson())) {
		  
	   exceptions.put("merchant.contactperson.required", new EmptyValueException("merchant.contactperson.required"));
	   
	  } else {
		  
		  companyModel.setContactPerson(Util.strip_html_tags(companyModel.getContactPerson(), "url"));

		  if(Util.isEmpty(companyModel.getContactPerson())) {
			  
			  exceptions.put("merchant.contactperson.required", new EmptyValueException("merchant.contactperson.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getDesignation())) {
		  
	   exceptions.put("merchant.designation.required", new EmptyValueException("merchant.designation.required"));
	   
	  } else {
		  
		  companyModel.setDesignation(Util.strip_html_tags(companyModel.getDesignation(), "url"));

		  if(Util.isEmpty(companyModel.getDesignation())) {
			  
			  exceptions.put("merchant.designation.required", new EmptyValueException("merchant.designation.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getEmail())) {
		  
	   exceptions.put("merchant.email.required", new EmptyValueException("merchant.email.required"));
	   
	  } else {
		  
		  companyModel.setEmail(Util.strip_html_tags(companyModel.getEmail(), "url"));

		  if(Util.isEmpty(companyModel.getEmail())) {
			  
			  exceptions.put("merchant.email.required", new EmptyValueException("merchant.email.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getMobile())) {
		  
	   exceptions.put("merchant.mobile.required", new EmptyValueException("merchant.mobile.required"));
	   
	  } else {
		  
		  companyModel.setMobile(Util.strip_html_tags(companyModel.getMobile(), "url"));

		  if(Util.isEmpty(companyModel.getMobile())) {
			  
			  exceptions.put("merchant.mobile.required", new EmptyValueException("merchant.mobile.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getPanNo())) {
		  
	   exceptions.put("merchant.panNo.required", new EmptyValueException("merchant.panNo.required"));
	   
	  } else {
		  
		  companyModel.setPanNo(Util.strip_html_tags(companyModel.getPanNo(), "url"));

		  if(Util.isEmpty(companyModel.getPanNo())) {
			  
			  exceptions.put("merchant.panNo.required", new EmptyValueException("merchant.panNo.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getProjectNoOfTpm())) {
		  
	   exceptions.put("merchant.projectNoOfTpm.required", new EmptyValueException("merchant.projectNoOfTpm.required"));
	   
	  } else {
		  
		  if(!Util.isNumeric(companyModel.getProjectNoOfTpm().toString())) {
			  
			  exceptions.put("merchant.projectNoOfTpm.required", new EmptyValueException("merchant.projectNoOfTpm.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getProjectVolOfTpm())) {
		  
	   exceptions.put("merchant.projectVolOfTpm.required", new EmptyValueException("merchant.projectVolOfTpm.required"));
	   
	  } else {
		  
		  if(!Util.isNumeric(companyModel.getProjectVolOfTpm().toString())) {
			  
			  exceptions.put("merchant.projectVolOfTpm.required", new EmptyValueException("merchant.projectVolOfTpm.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getMaxAmtSt())) {
		  
	   exceptions.put("merchant.maxAmtSt.required", new EmptyValueException("merchant.maxAmtSt.required"));
	   
	  } else {
		  
		  if(!Util.isNumeric(companyModel.getMaxAmtSt().toString())) {
			  
			  exceptions.put("merchant.maxAmtSt.required", new EmptyValueException("merchant.maxAmtSt.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getMaxVolTpd())) {
		  
	   exceptions.put("merchant.maxVolTpd.required", new EmptyValueException("merchant.maxVolTpd.required"));
	   
	  } else {
		  
		  if(!Util.isNumeric(companyModel.getMaxVolTpd().toString())) {
			  
			  exceptions.put("merchant.maxVolTpd.required", new EmptyValueException("merchant.maxVolTpd.required"));
			   
			 }
	  }
	  
	  if (Util.isEmpty(companyModel.getMaxNoTpd())) {
		  
	   exceptions.put("merchant.maxNoTpd.required", new EmptyValueException("merchant.maxNoTpd.required"));
	   
	  } else {
		  
		  if(!Util.isNumeric(companyModel.getMaxNoTpd().toString())) {
			  
			  exceptions.put("merchant.maxNoTpd.required", new EmptyValueException("merchant.maxNoTpd.required"));
			   
			 }
	  }

	  if (exceptions.size() > 0)
	   throw new FormExceptions(exceptions);

	  if (logger.isInfoEnabled()) {
	   logger.info("Merchant companyInfoValidation -- END");
	  }
		  
	}	
	
	/**
	 * This method is used to validate the save ticket form.
	 * @param ticketModel
	 * @throws Exception
	 */
	public void saveTicketValidation(TicketModel ticketModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant saveTicketValidation -- START");
		}

		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();

		if (Util.isEmpty(ticketModel.getSubject())) {
			
			exceptions.put("merchant.ticket.subject.required",new EmptyValueException(messageUtil.getBundle("merchant.ticket.subject.required")));
			
		 } else {
			  
			 ticketModel.setSubject(Util.strip_html_tags(ticketModel.getSubject(), "url"));

			  if(Util.isEmpty(ticketModel.getSubject())) {
				  
				  exceptions.put("merchant.ticket.subject.required",new EmptyValueException(messageUtil.getBundle("merchant.ticket.subject.required")));
				   
				 }
		  }

		if (Util.isEmpty(ticketModel.getTicketMessage())) {
			
			exceptions.put("merchant.ticket.details.required",new EmptyValueException(messageUtil.getBundle("merchant.ticket.details.required")));
			
		 } else {
			  
			 ticketModel.setTicketMessage(Util.strip_html_tags(ticketModel.getTicketMessage(), "url"));

			  if(Util.isEmpty(ticketModel.getTicketMessage())) {
				  
				  exceptions.put("merchant.ticket.details.required",new EmptyValueException(messageUtil.getBundle("merchant.ticket.details.required")));
				   
				 }
		  }

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("Merchant saveTicketValidation -- START");
		}
	}
	
	
	/**
	 * This method is used for Merchant Parameter insert.
	 * @param parameterModel
	 * @throws Exception
	 */
	public void merchantParameterCreateValidation(ParameterModel parameterModel)throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant merchantParameterCreateValidation -- START");
		}

		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();

		if (Util.isEmpty(parameterModel.getParameterName())) {
			
			exceptions.put("merchant.checkout.parameterName.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterName.required")));
			
		 } else {
			  
			 parameterModel.setParameterName(Util.strip_html_tags(parameterModel.getParameterName(), "url"));

			  if(Util.isEmpty(parameterModel.getParameterName())) {
				  
				  exceptions.put("merchant.checkout.parameterName.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterName.required")));
				   
			  } else {
				  
				  if(Objects.nonNull(parameterDao.fetchParameterById(parameterModel.getParameterName(), parameterModel.getCreatedBy()))) {
					  
					  exceptions.put("merchant.checkout.parameterName.duplicate",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterName.duplicate")));
				  }	
			  }
		  }

		if (Util.isEmpty(parameterModel.getParameterType())) {
			
			exceptions.put("merchant.checkout.parameterType.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterType.required")));
			
		 } else {
			  
			 parameterModel.setParameterType(Util.strip_html_tags(parameterModel.getParameterType(), "url"));

			  if(Util.isEmpty(parameterModel.getParameterType())) {
				  
				  exceptions.put("merchant.checkout.parameterType.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterType.required")));
				   
				 }
		  }

		if (Util.isEmpty(parameterModel.getVisible())) {
			
			exceptions.put("merchant.checkout.visible.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.visible.required")));
			
		 } else {
			  
			 parameterModel.setVisible(Util.strip_html_tags(parameterModel.getVisible(), "url"));

			  if(Util.isEmpty(parameterModel.getVisible())) {
				  
				  exceptions.put("merchant.checkout.visible.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.visible.required")));
				   
				 }
		  }

		if (Util.isEmpty(parameterModel.getPersistent())) {
			
			exceptions.put("merchant.checkout.persistent.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.persistent.required")));
			
		 } else {
			  
			 parameterModel.setPersistent(Util.strip_html_tags(parameterModel.getPersistent(), "url"));

			  if(Util.isEmpty(parameterModel.getPersistent())) {
				  
				  exceptions.put("merchant.checkout.persistent.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.persistent.required")));
				   
				 }
		  }

		if (Util.isEmpty(parameterModel.getMandatory())) {
			
			exceptions.put("merchant.checkout.mandatory.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.mandatory.required")));
			
		 } else {
			  
			 parameterModel.setMandatory(Util.strip_html_tags(parameterModel.getMandatory(), "url"));

			  if(Util.isEmpty(parameterModel.getMandatory())) {
				  
				  exceptions.put("merchant.checkout.mandatory.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.mandatory.required")));
				   
				 }
		  }

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("Merchant merchantParameterCreateValidation -- END");
		}
	}
	
	/**
	 * This method is used to Validate Merchant Parameter Update.
	 * @param parameterModel
	 * @throws Exception
	 */
	public void merchantParameterUpdateValidation(ParameterModel parameterModel)throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant merchantParameterUpdateValidation -- START");
		}

		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();

		if (Util.isEmpty(parameterModel.getParameterId())) {
			
			exceptions.put("merchant.checkout.parameterId.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterId.required")));
			
		} else if (!Util.isNumeric(parameterModel.getParameterId().toString())) {
			
			exceptions.put("merchant.checkout.parameterId.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterId.required")));
			
		}

		if (Util.isEmpty(parameterModel.getParameterName())) {
			
			exceptions.put("merchant.checkout.parameterName.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterName.required")));
			
		 } else {
			  
			 parameterModel.setParameterName(Util.strip_html_tags(parameterModel.getParameterName(), "url"));
			 
			 if(Util.isEmpty(parameterModel.getParameterName())) {
				  
				  exceptions.put("merchant.checkout.parameterName.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterName.required")));
				   
			  } else {
				  
				  ParameterModel parameterModel2 = parameterDao.fetchParameterById(parameterModel.getParameterName(), parameterModel.getModifiedBy());
				  if(Objects.nonNull(parameterModel2)) {
					  
					  if(!String.valueOf(parameterModel.getParameterId()).equalsIgnoreCase(String.valueOf(parameterModel2.getParameterId()))) {
						  exceptions.put("merchant.checkout.parameterName.duplicate",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterName.duplicate")));
					  }
						  
						  
				  }	
			  }
		  }

		if (Util.isEmpty(parameterModel.getParameterType())) {
			
			exceptions.put("merchant.checkout.parameterType.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterType.required")));
			
		 } else {
			  
			 parameterModel.setParameterType(Util.strip_html_tags(parameterModel.getParameterType(), "url"));

			  if(Util.isEmpty(parameterModel.getParameterType())) {
				  
				  exceptions.put("merchant.checkout.parameterType.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.parameterType.required")));
				   
				 }
		  }

		if (Util.isEmpty(parameterModel.getVisible())) {
			
			exceptions.put("merchant.checkout.visible.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.visible.required")));
			
		 } else {
			  
			 parameterModel.setVisible(Util.strip_html_tags(parameterModel.getVisible(), "url"));

			  if(Util.isEmpty(parameterModel.getVisible())) {
				  
				  exceptions.put("merchant.checkout.visible.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.visible.required")));
				   
				 }
		  }

		if (Util.isEmpty(parameterModel.getPersistent())) {
			
			exceptions.put("merchant.checkout.persistent.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.persistent.required")));
			
		 } else {
			  
			 parameterModel.setPersistent(Util.strip_html_tags(parameterModel.getPersistent(), "url"));

			  if(Util.isEmpty(parameterModel.getPersistent())) {
				  
				  exceptions.put("merchant.checkout.persistent.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.persistent.required")));
				   
				 }
		  }

		if (Util.isEmpty(parameterModel.getMandatory())) {
			
			exceptions.put("merchant.checkout.mandatory.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.mandatory.required")));
			
		 } else {
			  
			 parameterModel.setMandatory(Util.strip_html_tags(parameterModel.getMandatory(), "url"));

			  if(Util.isEmpty(parameterModel.getMandatory())) {
				  
				  exceptions.put("merchant.checkout.mandatory.required",new EmptyValueException(messageUtil.getBundle("merchant.checkout.mandatory.required")));
				   
				 }
		  }

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("Merchant merchantParameterUpdateValidation -- END");
		}
	}
	
	/**
	 * This method is use to validate create Quick Pay details.
	 * @param quickPayModel
	 * @throws Exception
	 */
	public void createQuickPayValidation(QuickPayModel quickPayModel) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("createQuickPayValidation -- START ");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(quickPayModel.getProductSKU())){
			
			exceptions.put("quickpay.product.sku.required", new EmptyValueException(messageUtil.getBundle("quickpay.product.sku.required")));
			
		 } else {
			  
			 quickPayModel.setProductSKU(Util.strip_html_tags(quickPayModel.getProductSKU(), "url"));

			  if(Util.isEmpty(quickPayModel.getProductSKU())) {
				  
				  exceptions.put("quickpay.product.sku.required", new EmptyValueException(messageUtil.getBundle("quickpay.product.sku.required")));
				   
				 }
		  }
		
		if(Util.isEmpty(quickPayModel.getProductName())){
			
			exceptions.put("quickpay.product.name.required", new EmptyValueException(messageUtil.getBundle("quickpay.product.name.required")));
			
		 } else {
			  
			 quickPayModel.setProductName(Util.strip_html_tags(quickPayModel.getProductName(), "url"));

			  if(Util.isEmpty(quickPayModel.getProductName())) {
				  
				  exceptions.put("quickpay.product.name.required", new EmptyValueException(messageUtil.getBundle("quickpay.product.name.required")));
				   
				 }
		  }
		
		if(Util.isEmpty(quickPayModel.getOrderAmount())){
			
			exceptions.put("quickpay.order.amount.required", new EmptyValueException(messageUtil.getBundle("quickpay.order.amount.required")));
			
		} else {
			
			if (!Util.isNumeric(quickPayModel.getOrderAmount())){
				
				exceptions.put("quickpay.order.amount.not.number", new EmptyValueException(messageUtil.getBundle("quickpay.order.amount.not.number")));
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if (logger.isInfoEnabled()) {
			logger.info("createQuickPayValidation -- END");
		}
	}
	
	/**
	 * This method is used for Email Invoicing Insert Form.
	 * @param emailInvoicingModel
	 * @throws Exception
	 */
	public void createemailInvoicingValidation(EmailInvoicingModel emailInvoicingModel) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("createemailInvoicingValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if (Util.isEmpty(emailInvoicingModel.getFirstName())) {
			
			exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
			
		} else{
			
			emailInvoicingModel.setFirstName(Util.strip_html_tags(emailInvoicingModel.getFirstName(), "url"));

			if(Util.isEmpty(emailInvoicingModel.getFirstName())) {
				
				exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
			
			}
		}

		if (Util.isEmpty(emailInvoicingModel.getLastName())) {
			
			exceptions.put("merchant.last.name.required",new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
		} else{
			
			emailInvoicingModel.setLastName(Util.strip_html_tags(emailInvoicingModel.getLastName(), "url"));

			if(Util.isEmpty(emailInvoicingModel.getLastName())) {
				
				exceptions.put("merchant.last.name.required",new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
			}
		}

		if (Util.isEmpty(emailInvoicingModel.getEmailId())) {
			
			exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
			
		} else{
			
			emailInvoicingModel.setEmailId(Util.strip_html_tags(emailInvoicingModel.getEmailId(), "url"));

			if(Util.isEmpty(emailInvoicingModel.getEmailId())) {
				
				exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
			
			} else {
				
					if(Util.emailValidator(emailInvoicingModel.getEmailId())) {
					
						exceptions.put("merchant.email.invalid", new EmptyValueException(messageUtil.getBundle("merchant.email.invalid")));
						
					}
			}
		}
		
		if (Util.isEmpty(emailInvoicingModel.getMobileNumber())) {
			
			exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
			
		} else {
			
			emailInvoicingModel.setMobileNumber(Util.strip_html_tags(emailInvoicingModel.getMobileNumber(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getMobileNumber())) {
				
				exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
				
			} 
		}
		
		if(Util.isEmpty(emailInvoicingModel.getInvoiceNo())){
			
			exceptions.put("invoiceno.required", new EmptyValueException(messageUtil.getBundle("invoiceno.required")));
			
		} else {
			
			emailInvoicingModel.setInvoiceNo(Util.strip_html_tags(emailInvoicingModel.getInvoiceNo(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getInvoiceNo())) {
				
				exceptions.put("invoiceno.required", new EmptyValueException(messageUtil.getBundle("invoiceno.required")));
				
			} 
		}
		
		if(Util.isEmpty(emailInvoicingModel.getType())){
			
			exceptions.put("type.required", new EmptyValueException(messageUtil.getBundle("type.required")));
			
		} else {
			
			emailInvoicingModel.setType(Util.strip_html_tags(emailInvoicingModel.getType(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getType())) {
				
				exceptions.put("type.required", new EmptyValueException(messageUtil.getBundle("type.required")));
				
			} 
		}
		
		if(Util.isEmpty(emailInvoicingModel.getBDT())){
			
			exceptions.put("bdt.required", new EmptyValueException(messageUtil.getBundle("bdt.required")));
			
		} else {
			
			emailInvoicingModel.setBDT(Util.strip_html_tags(emailInvoicingModel.getBDT(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getBDT())) {
				
				exceptions.put("bdt.required", new EmptyValueException(messageUtil.getBundle("bdt.required")));
				
			} 
		}
		
		if(Util.isEmpty(emailInvoicingModel.getAmount())){
			
			exceptions.put("amount.required", new EmptyValueException(messageUtil.getBundle("amount.required")));
			
		} else {
			
			emailInvoicingModel.setAmount(Util.strip_html_tags(emailInvoicingModel.getAmount(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getAmount())) {
				
				exceptions.put("amount.required", new EmptyValueException(messageUtil.getBundle("amount.required")));
				
			} else {
				
				try {
					
					double amount = Double.parseDouble(emailInvoicingModel.getAmount());
					String amountVerify = String.valueOf(amount);
					if(amountVerify.contains("-"))
						exceptions.put("amount.negative.error", new EmptyValueException(messageUtil.getBundle("amount.negative.error")));
					if(amountVerify.contains(".")){
						
						int index = amountVerify.indexOf(".");
						int length = amountVerify.length();
						if(index + 2 != length)
							exceptions.put("amount.decimal.error", new EmptyValueException(messageUtil.getBundle("amount.decimal.error")));
						
					}
							
					
				} catch(Exception e) {
					exceptions.put("amount.numeric", new EmptyValueException(messageUtil.getBundle("amount.numeric")));
					
				}
			}
		}
		
		if(Util.isEmpty(emailInvoicingModel.getSubject())){
			
			exceptions.put("subject.required", new EmptyValueException(messageUtil.getBundle("subject.required")));
			
		} else {
			
			emailInvoicingModel.setSubject(Util.strip_html_tags(emailInvoicingModel.getSubject(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getSubject())) {
				
				exceptions.put("subject.required", new EmptyValueException(messageUtil.getBundle("subject.required")));
				
			} 
		}
		
		if(Util.isEmpty(emailInvoicingModel.getDescription())){
			
			exceptions.put("description.required", new EmptyValueException(messageUtil.getBundle("description.required")));
			
		} else {
			
			emailInvoicingModel.setDescription(Util.strip_html_tags(emailInvoicingModel.getDescription(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getDescription())) {
				
				exceptions.put("description.required", new EmptyValueException(messageUtil.getBundle("description.required")));
				
			} 
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if (logger.isInfoEnabled()) {
			logger.info("createemailInvoicingValidation -- END ");
		}
	}
	
	/**
	 * This method is used to Validate Link.
	 * @param emailInvoicingModel
	 * @throws Exception
	 */
	public void createLinkValidation(EmailInvoicingModel emailInvoicingModel) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("createLinkValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if (Util.isEmpty(emailInvoicingModel.getFirstName())) {
			
			exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
			
		} else{
			
			emailInvoicingModel.setFirstName(Util.strip_html_tags(emailInvoicingModel.getFirstName(), "url"));

			if(Util.isEmpty(emailInvoicingModel.getFirstName())) {
				
				exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
			
			}
		}

		if (Util.isEmpty(emailInvoicingModel.getLastName())) {
			
			exceptions.put("merchant.last.name.required",new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
		} else{
			
			emailInvoicingModel.setLastName(Util.strip_html_tags(emailInvoicingModel.getLastName(), "url"));

			if(Util.isEmpty(emailInvoicingModel.getLastName())) {
				
				exceptions.put("merchant.last.name.required",new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
			}
		}

		if (Util.isEmpty(emailInvoicingModel.getEmailId())) {
			
			exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
			
		} else{
			
			emailInvoicingModel.setEmailId(Util.strip_html_tags(emailInvoicingModel.getEmailId(), "url"));

			if(Util.isEmpty(emailInvoicingModel.getEmailId())) {
				
				exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
			
			} else {
				
					if(Util.emailValidator(emailInvoicingModel.getEmailId())) {
					
						exceptions.put("merchant.email.invalid", new EmptyValueException(messageUtil.getBundle("merchant.email.invalid")));
						
					}
			}
		}
		
		if (Util.isEmpty(emailInvoicingModel.getMobileNumber())) {
			
			exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
			
		} else {
			
			emailInvoicingModel.setMobileNumber(Util.strip_html_tags(emailInvoicingModel.getMobileNumber(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getMobileNumber())) {
				
				exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
				
			} 
		}
		
/*		if (emailInvoicingModel.getMobileNumber().length() != 14 || !emailInvoicingModel.getMobileNumber().contains("+880")) {
			
			exceptions.put("merchant.mobile.no.invalid.format",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.invalid.format")));
		}*/
		
		if(Util.isEmpty(emailInvoicingModel.getPluggers())){
			
			exceptions.put("pluggers.required", new EmptyValueException(messageUtil.getBundle("pluggers.required")));
		}
		
		if(Util.isEmpty(emailInvoicingModel.getBDT())){
			
			exceptions.put("bdt.required", new EmptyValueException(messageUtil.getBundle("bdt.required")));
			
		} else {
			
			emailInvoicingModel.setBDT(Util.strip_html_tags(emailInvoicingModel.getBDT(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getBDT())) {
				
				exceptions.put("bdt.required", new EmptyValueException(messageUtil.getBundle("bdt.required")));
				
			} 
		}
		
		if(Util.isEmpty(emailInvoicingModel.getAmount())){
			
			exceptions.put("amount.required", new EmptyValueException(messageUtil.getBundle("amount.required")));
			
		} else {
			
			emailInvoicingModel.setAmount(Util.strip_html_tags(emailInvoicingModel.getAmount(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getAmount())) {
				
				exceptions.put("amount.required", new EmptyValueException(messageUtil.getBundle("amount.required")));
				
			} else {
				
				try {
					
					String amountVerify = emailInvoicingModel.getAmount();
					if(amountVerify.contains("-"))
						exceptions.put("amount.negative.error", new EmptyValueException(messageUtil.getBundle("amount.negative.error")));
					
					if(!Util.checkDecialDigit(amountVerify)){
						exceptions.put("amount.decimal.error", new EmptyValueException(messageUtil.getBundle("amount.decimal.error")));
					}
							
					
				} catch(Exception e) {
					exceptions.put("amount.numeric", new EmptyValueException(messageUtil.getBundle("amount.numeric")));
					
				}
			}
		}
		
		if(Util.isEmpty(emailInvoicingModel.getSubject())){
			
			exceptions.put("subject.required", new EmptyValueException(messageUtil.getBundle("subject.required")));
			
		} else {
			
			emailInvoicingModel.setSubject(Util.strip_html_tags(emailInvoicingModel.getSubject(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getSubject())) {
				
				exceptions.put("subject.required", new EmptyValueException(messageUtil.getBundle("subject.required")));
				
			} 
		}
		
		if(Util.isEmpty(emailInvoicingModel.getDescription())){
			
			exceptions.put("description.required", new EmptyValueException(messageUtil.getBundle("description.required")));
			
		} else {
			
			emailInvoicingModel.setDescription(Util.strip_html_tags(emailInvoicingModel.getDescription(), "url"));
			if (Util.isEmpty(emailInvoicingModel.getDescription())) {
				
				exceptions.put("description.required", new EmptyValueException(messageUtil.getBundle("description.required")));
				
			} 
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if (logger.isInfoEnabled()) {
			logger.info("createLinkValidation -- END ");
		}
	}
	
	/**
	 * This method is used to validate Create Order.
	 * @param paymentModel
	 * @throws Exception
	 */
	public void createOrderValidation(PaymentModel paymentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("createOrderValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(paymentModel.getAccessKey())) {
			
			exceptions.put(messageUtil.getBundle("accessKey.null.code"), new EmptyValueException(messageUtil.getBundle("accessKey.null")));
			
		} else {
			
			paymentModel.setAccessKey(Util.strip_html_tags(paymentModel.getAccessKey(), "url"));
			if(Util.isEmpty(paymentModel.getAccessKey())) {
				
				exceptions.put(messageUtil.getBundle("accessKey.null.code"), new EmptyValueException(messageUtil.getBundle("accessKey.null")));
				
			} 
			
			try{
				
				MerchantModel merchantModel = merchantDao.fetchActiveMerchantByAccessKey(paymentModel.getAccessKey());
				if(Objects.isNull(merchantModel))
					exceptions.put(messageUtil.getBundle("accessKey.invalid.code"), new EmptyValueException(messageUtil.getBundle("accessKey.invalid")));
				else
					paymentModel.setMerchantModel(merchantModel);
				
			} catch (Exception e) {
				
				exceptions.put(messageUtil.getBundle("accessKey.invalid.code"), new EmptyValueException(messageUtil.getBundle("accessKey.invalid")));
			}
		}
		
		if(Util.isEmpty(paymentModel.getAmount())) {
			
			exceptions.put(messageUtil.getBundle("amount.null.code"), new EmptyValueException(messageUtil.getBundle("amount.null")));
			
		} else {

			paymentModel.setAmount(Double.parseDouble(Util.strip_html_tags(paymentModel.getAmount().toString(), "url")));
			if(Util.isEmpty(paymentModel.getAmount())) {
				
				exceptions.put(messageUtil.getBundle("amount.null.code"), new EmptyValueException(messageUtil.getBundle("amount.null")));
			}
		}
		
		if(Util.isEmpty(paymentModel.getSuccessURL())) {
			
			exceptions.put(messageUtil.getBundle("successURL.null.code"), new EmptyValueException(messageUtil.getBundle("successURL.null")));
			
		} else {
			
			paymentModel.setSuccessURL(Util.strip_html_tags(paymentModel.getSuccessURL(), "url"));
			if(Util.isEmpty(paymentModel.getSuccessURL())) {
				
				exceptions.put(messageUtil.getBundle("successURL.null.code"), new EmptyValueException(messageUtil.getBundle("successURL.null")));
			} 
		}
		
		if(Util.isEmpty(paymentModel.getFailureURL())) {
			
			exceptions.put(messageUtil.getBundle("failureURL.null.code"), new EmptyValueException(messageUtil.getBundle("failureURL.null")));
			
		} else {
			
			paymentModel.setFailureURL(Util.strip_html_tags(paymentModel.getFailureURL(), "url"));
			if(Util.isEmpty(paymentModel.getFailureURL())) {
				
				exceptions.put(messageUtil.getBundle("failureURL.null.code"), new EmptyValueException(messageUtil.getBundle("failureURL.null")));
			} 
		}

		if(Util.isEmpty(paymentModel.getOrderTransactionID())) {
			
			exceptions.put(messageUtil.getBundle("orderTransactionId.null.code"), new EmptyValueException(messageUtil.getBundle("orderTransactionId.null")));
			
		} else {
			
			paymentModel.setOrderTransactionID(Util.strip_html_tags(paymentModel.getOrderTransactionID(), "url"));
			if(Util.isEmpty(paymentModel.getOrderTransactionID())) {
				
				exceptions.put(messageUtil.getBundle("orderTransactionId.null.code"), new EmptyValueException(messageUtil.getBundle("orderTransactionId.null")));
				
			} else {
				
				try{
					
					PaymentModel paymentModel2 = transactionDao.fetchOrderByOrderTransactionId(paymentModel.getOrderTransactionID());
					if(Objects.nonNull(paymentModel2)) {
						if(!paymentModel2.getOrderTransactionID().equals("None"))
							exceptions.put(messageUtil.getBundle("orderTransactionId.repeat.code"), new EmptyValueException(messageUtil.getBundle("orderTransactionId.repeat")));
					}
						
				} catch (Exception e){
					exceptions.put(messageUtil.getBundle("orderTransactionId.repeat.code"), new EmptyValueException(messageUtil.getBundle("orderTransactionId.repeat")));
				}
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if (logger.isInfoEnabled()) {
			logger.info("createOrderValidation -- END ");
		}
	}
	
	/**
	 * This method is used to validate Update Order.
	 * @param paymentModel
	 * @throws Exception
	 */
	public void updateOrderValidation(PaymentModel paymentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateOrderValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(paymentModel.getToken())) {
			
			exceptions.put(messageUtil.getBundle("token.null.code"), new EmptyValueException(messageUtil.getBundle("token.null")));
			
		} else {
			
			paymentModel.setToken(Util.strip_html_tags(paymentModel.getToken(), "url"));
			if(Util.isEmpty(paymentModel.getToken())) {
				
				exceptions.put(messageUtil.getBundle("token.null.code"), new EmptyValueException(messageUtil.getBundle("token.null")));
			} 
			
			try{
				
				PaymentModel paymentModel2 = transactionDao.fetchOrderByToken(paymentModel.getToken());
				if(Objects.isNull(paymentModel2))
						exceptions.put(messageUtil.getBundle("token.invalid.code"), new EmptyValueException(messageUtil.getBundle("token.invalid")));
					else{
						
						paymentModel.setOrderID(paymentModel2.getOrderID());
						paymentModel.setMerchantModel(paymentModel2.getMerchantModel());
						paymentModel.setAmount(paymentModel2.getAmount());
						
						String customerDetails = paymentModel2.getCustomerDetails();
						Gson gson = new Gson();
						paymentModel2 = gson.fromJson(customerDetails, PaymentModel.class);
						paymentModel.setFirstName(paymentModel2.getFirstName());
						paymentModel.setLastName(paymentModel2.getLastName());
						paymentModel.setMobileNumber(paymentModel2.getMobileNumber());
						paymentModel.setEmailId(paymentModel2.getEmailId());
						
					}
				
			} catch (Exception e) {
				
				exceptions.put(messageUtil.getBundle("token.invalid.code"), new EmptyValueException(messageUtil.getBundle("token.invalid")));
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if (logger.isInfoEnabled()) {
			logger.info("updateOrderValidation -- END ");
		}
	}
	
	/**
	 * Validate Date for transaction search report.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void searchTransacionValidation(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("searchTransacionValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Objects.nonNull(dataTableModel)) {
			
			if(Util.isEmpty(dataTableModel.getToDate())) {
				exceptions.put("startdate.required", new EmptyValueException(messageUtil.getBundle("startdate.required")));
			} else {
				dataTableModel.setToDate(Util.strip_html_tags(dataTableModel.getToDate(), "url"));
				if(Util.isEmpty(dataTableModel.getToDate())) {
					
					exceptions.put("startdate.required", new EmptyValueException(messageUtil.getBundle("startdate.required")));
				} else {
					dataTableModel.setToDate(dataTableModel.getToDate());
				}
			}
			
			if(Util.isEmpty(dataTableModel.getFromDate())) {
				exceptions.put("enddate.required", new EmptyValueException(messageUtil.getBundle("enddate.required")));
			} else {
				dataTableModel.setFromDate(Util.strip_html_tags(dataTableModel.getFromDate(), "url"));
				if(Util.isEmpty(dataTableModel.getFromDate())) {
					exceptions.put("enddate.required", new EmptyValueException(messageUtil.getBundle("enddate.required")));
				} else {
					dataTableModel.setFromDate(dataTableModel.getFromDate());
				}
			}
		
			if(Util.getDayDiff(dataTableModel.getToDate(), dataTableModel.getFromDate()) <= -1) {
				
				exceptions.put("startdate.lesser", new EmptyValueException(messageUtil.getBundle("startdate.lesser")));
			}
		
		} else {
			
			exceptions.put("form.required", new EmptyValueException(messageUtil.getBundle("form.required")));
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if (logger.isInfoEnabled()) {
			logger.info("searchTransacionValidation -- END ");
		}
	}
	/**
	 * This method is used to Validate Image Format.
	 * @param checkoutModel
	 * @throws ImageFormatMismatch
	 * @throws IOException
	 */
	public void imageFormatValidation(CheckoutModel checkoutModel) throws ImageFormatMismatch, IOException {
		
		String imageType = TIKA.detect(checkoutModel.getFile().getBytes());
		if (StringUtils.equals(imageType, "image/jpeg")
				|| StringUtils.equals(imageType, "image/jpg")
				|| StringUtils.equals(imageType, "image/jif")
				|| StringUtils.equals(imageType, "image/png")
				|| StringUtils.equals(imageType, "image/gif")
				|| StringUtils.equals(imageType, "image/bmp")) {

		} else {
			throw new ImageFormatMismatch(messageUtil.getBundle("image.format.mismatch"));
		}
	}
}