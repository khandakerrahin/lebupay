/**
 * @formatter:off
 *
 */
package com.lebupay.validation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lebupay.common.MessageUtil;
import com.lebupay.common.Util;
import com.lebupay.dao.AdminDAO;
import com.lebupay.dao.MerchantDao;
import com.lebupay.exception.EmptyValueException;
import com.lebupay.exception.FormExceptions;
import com.lebupay.model.AdminModel;
import com.lebupay.model.ContentModel;
import com.lebupay.model.FaqModel;
import com.lebupay.model.MerchantCardPercentageModel;
import com.lebupay.model.MerchantModel;

/**
 * This is AdminValidation Class is used to validate all operations performed by Admin Module.
 * @author Java Team
 *
 */
@Component
public class AdminValidation {

	private static Logger logger = Logger.getLogger(AdminValidation.class);
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private AdminDAO adminDao;
	
	@Autowired
	private MerchantDao merchantDao;
	
	
	/**
	 * This method is use for login validation purpose
	 * @param adminModel
	 * @throws FormExceptions
	 */
	public void loginValidation(AdminModel adminModel) throws FormExceptions {
		
		if(logger.isInfoEnabled()){
			logger.info("loginValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(adminModel.getUserName())){
			
			exceptions.put("admin.user.name.required", new EmptyValueException(messageUtil.getBundle("admin.user.name.required")));
			
		} else {
			
			adminModel.setUserName(Util.strip_html_tags(adminModel.getUserName(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getUserName())) {
				
				exceptions.put("admin.user.name.required", new EmptyValueException(messageUtil.getBundle("admin.user.name.required")));
			
			} else {
					if(adminModel.getUserName().contains("@")) { // User has provided Email ID for login
						
						try {
							
							AdminModel emailCheck = adminDao.emailCheckCreate(adminModel.getUserName());
							if(Objects.isNull(emailCheck)){
								exceptions.put("admin.email.not.exist", new Exception(messageUtil.getBundle("admin.email.not.exist")));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					} else { // User has provided Mobile Number for login
						
						try {
							
							AdminModel mobileCheck = adminDao.mobileCheckCreate(adminModel.getUserName());
							
							if(Objects.isNull(mobileCheck)){
								exceptions.put("admin.phone.no.not.exist", new Exception(messageUtil.getBundle("admin.phone.no.not.exist")));
							}
						} catch (Exception e) {
						// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			}
			
		}
		
		if(Util.isEmpty(adminModel.getPassword())){
			
			exceptions.put("admin.password.required", new EmptyValueException(messageUtil.getBundle("admin.password.required")));
			
		} else {
			
			adminModel.setPassword(Util.strip_html_tags(adminModel.getPassword(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getPassword())) {
				
				exceptions.put("admin.password.required", new EmptyValueException(messageUtil.getBundle("admin.password.required")));
			
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		
		if(logger.isInfoEnabled()){
			logger.info("loginValidation -- END");
		}
	}
	
	/**
	 * This method is use for forgot password purpose
	 * @param adminModel
	 * @throws FormExceptions
	 */
	public void forgotPassword(AdminModel adminModel) throws FormExceptions {
		
		if(logger.isInfoEnabled()){
			logger.info("forgotPassword -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(adminModel.getAdminId())){
			
			exceptions.put("admin.id.required", new EmptyValueException(messageUtil.getBundle("admin.id.required")));
			
		} else{
			
			if(!Util.isNumeric(adminModel.getAdminId().toString())){
				
				exceptions.put("admin.id.required", new EmptyValueException(messageUtil.getBundle("admin.id.required")));
			}
		}
		
		if(Util.isEmpty(adminModel.getPassword())){
			
			exceptions.put("admin.password.required", new EmptyValueException(messageUtil.getBundle("admin.password.required")));
		
		} else {
			
			adminModel.setPassword(Util.strip_html_tags(adminModel.getPassword(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getPassword())) {
				
				exceptions.put("admin.password.required", new EmptyValueException(messageUtil.getBundle("admin.password.required")));
			
			}
		}
		
		if(Util.isEmpty(adminModel.getConfirmPassword())){
			
			exceptions.put("admin.confirm.password.required", new EmptyValueException(messageUtil.getBundle("admin.confirm.password.required")));
		
		} else {
			
			adminModel.setConfirmPassword(Util.strip_html_tags(adminModel.getConfirmPassword(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getConfirmPassword())) {
				
				exceptions.put("admin.confirm.password.required", new EmptyValueException(messageUtil.getBundle("admin.confirm.password.required")));
			
			}
		}
		
		if(!(Util.isEmpty(adminModel.getPassword()) && !(Util.isEmpty(adminModel.getConfirmPassword())))){
			
			if(!(adminModel.getPassword().equals((adminModel.getConfirmPassword()))) ){
				
				exceptions.put("admin.password.and.confirm-password.not.matching", new EmptyValueException(messageUtil.getBundle("admin.password.and.confirm-password.not.matching")));
			
			} else {
				
				if(!Util.checkPasswordPolicy(adminModel.getPassword())) {
					
					exceptions.put("merchant.password.format",new EmptyValueException(messageUtil.getBundle("merchant.password.format")));

				}
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		
		if(logger.isInfoEnabled()){
			logger.info("forgotPassword -- END");
		}
	}
	
	
	
	/**
	 * This method is use for profile update validation purpose
	 * @param adminModel
	 * @throws Exception
	 */
	public void profileUpdateValidation(AdminModel adminModel) throws Exception {
		
		if(logger.isInfoEnabled()){
			logger.info("profileUpdateValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(adminModel.getFirstName())){
			
			exceptions.put("admin.first.name.required", new EmptyValueException(messageUtil.getBundle("admin.first.name.required")));
		
		} else {
			
			adminModel.setFirstName(Util.strip_html_tags(adminModel.getFirstName(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getFirstName())) {
				
				exceptions.put("admin.first.name.required", new EmptyValueException(messageUtil.getBundle("admin.first.name.required")));
			
			}
		}
		
		if(Util.isEmpty(adminModel.getLastName())){
			
			exceptions.put("admin.last.name.required", new EmptyValueException(messageUtil.getBundle("admin.last.name.required")));
			
		} else {
			
			adminModel.setLastName(Util.strip_html_tags(adminModel.getLastName(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getLastName())) {
				
				exceptions.put("admin.last.name.required", new EmptyValueException(messageUtil.getBundle("admin.last.name.required")));
			
			}
		}
		
		if(Util.isEmpty(adminModel.getEmailId())){
			
			exceptions.put("admin.email.required", new EmptyValueException(messageUtil.getBundle("admin.email.required")));
			
		} else {
			
			adminModel.setEmailId(Util.strip_html_tags(adminModel.getEmailId(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getEmailId())) {
				
				exceptions.put("admin.email.required", new EmptyValueException(messageUtil.getBundle("admin.email.required")));
			
			} else {
			
				if(Util.emailValidator(adminModel.getEmailId())){
					exceptions.put("admin.email.invalid", new EmptyValueException(messageUtil.getBundle("admin.email.invalid")));
				}
			}
		} 
			
		
		try {
			AdminModel emailCheckUpdate = adminDao.emailCheckUpdate(adminModel.getEmailId(), adminModel.getAdminId());
		
			if(Objects.nonNull(emailCheckUpdate)){
				exceptions.put("admin.email.exist", new Exception(messageUtil.getBundle("admin.email.exist")));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if(Util.isEmpty(adminModel.getMobileNo())){
			
			exceptions.put("admin.mobileNumber.required", new EmptyValueException(messageUtil.getBundle("admin.mobileNumber.required")));
			
		} else {
			
			adminModel.setMobileNo(Util.strip_html_tags(adminModel.getMobileNo(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getMobileNo())) {
				
				exceptions.put("admin.mobileNumber.required", new EmptyValueException(messageUtil.getBundle("admin.mobileNumber.required")));
				
			} else {
				
				if(!Util.validatePhoneNumber(adminModel.getMobileNo())){
					exceptions.put("admin.mobileNumber.invalid", new EmptyValueException(messageUtil.getBundle("admin.mobileNumber.invalid")));
				}	
				
				try{
						AdminModel mobileCheckUpdate = adminDao.mobileCheckUpdate("+880"+adminModel.getMobileNo(), adminModel.getAdminId());
						if(Objects.nonNull(mobileCheckUpdate)){
						
							exceptions.put("admin.mobileNumber.exist", new Exception(messageUtil.getBundle("admin.mobileNumber.exist")));
						} else {
							
							adminModel.setMobileNo("+880"+adminModel.getMobileNo());
						}
					
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		
		if(logger.isInfoEnabled()){
			logger.info("profileUpdateValidation -- END");
		}
	}
	
	
	/**
	 * This method is use for change password purpose
	 * @param adminModel
	 * @throws Exception 
	 */
	public void changePassword(AdminModel adminModel) throws Exception {
		
		if(logger.isInfoEnabled()){
			logger.info("changePassword -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(adminModel.getAdminId())){
			
			exceptions.put("admin.id.required", new EmptyValueException(messageUtil.getBundle("admin.id.required")));
			
		}
		
		if(Util.isEmpty(adminModel.getPassword())){
			
			exceptions.put("admin.password.required", new EmptyValueException(messageUtil.getBundle("admin.password.required")));
			
		} else {
			
			adminModel.setPassword(Util.strip_html_tags(adminModel.getPassword(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getPassword())) {
				
				exceptions.put("admin.password.required", new EmptyValueException(messageUtil.getBundle("admin.password.required")));
			
			} else {
			
				if(Util.isEmpty(adminModel.getPassword())){
					exceptions.put("admin.password.required", new EmptyValueException(messageUtil.getBundle("admin.password.required")));
				}
			}
		}
		
		if(Util.isEmpty(adminModel.getConfirmPassword())){
			
			exceptions.put("admin.confirmpassword.required", new EmptyValueException(messageUtil.getBundle("admin.confirmpassword.required")));
			
		}  else {
			
			adminModel.setConfirmPassword(Util.strip_html_tags(adminModel.getConfirmPassword(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getConfirmPassword())) {
				
				exceptions.put("admin.confirmpassword.required", new EmptyValueException(messageUtil.getBundle("admin.confirmpassword.required")));
			
			} else {
			
				if(Util.isEmpty(adminModel.getConfirmPassword())){
					exceptions.put("admin.confirmpassword.required", new EmptyValueException(messageUtil.getBundle("admin.confirmpassword.required")));
				}
			}
		}
		
		
		if(Util.isEmpty(adminModel.getConfirmNewPassword())){
			
			exceptions.put("admin.new.password.required", new EmptyValueException(messageUtil.getBundle("admin.new.password.required")));
			
		} else {
			
			adminModel.setConfirmNewPassword(Util.strip_html_tags(adminModel.getConfirmNewPassword(), "url", exceptions));
			
			if(Util.isEmpty(adminModel.getConfirmNewPassword())) {
				
				exceptions.put("admin.new.password.required", new EmptyValueException(messageUtil.getBundle("admin.new.password.required")));
			
			} else {
			
				if(Util.isEmpty(adminModel.getConfirmNewPassword())){
					exceptions.put("admin.new.password.required", new EmptyValueException(messageUtil.getBundle("admin.new.password.required")));
				}
			}
		}
		
		
		if(!(Util.isEmpty(adminModel.getConfirmNewPassword()) && !(Util.isEmpty(adminModel.getConfirmPassword())))){
			
			if(!(adminModel.getConfirmNewPassword().equals((adminModel.getConfirmPassword()))) ){
				
				exceptions.put("admin.password.and.confirm-password.not.matching", new EmptyValueException(messageUtil.getBundle("admin.password.and.confirm-password.not.matching")));
			
			} else {
				
				if(!Util.checkPasswordPolicy(adminModel.getConfirmNewPassword())) {
					
					exceptions.put("merchant.password.format",new EmptyValueException(messageUtil.getBundle("merchant.password.format")));

				}
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		
		if(logger.isInfoEnabled()){
			logger.info("changePassword -- END");
		}
	}
	
	/**
	 * This method is used to validate Merchant Profile.
	 * @param merchantModel
	 * @throws Exception
	 */
	public void merchantProfileValidation(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Admin merchantProfileValidation -- START");
		}

		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();

		if (Util.isEmpty(merchantModel.getFirstName())) {
			
			exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
			
		} else{
			
			merchantModel.setFirstName(Util.strip_html_tags(merchantModel.getFirstName(), "url", exceptions));
			
			if(Util.isEmpty(merchantModel.getFirstName())) {
				
				exceptions.put("merchant.first.name.required", new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
				
			} else {
				
				if (Util.isEmpty(merchantModel.getFirstName())) {
					exceptions.put("merchant.first.name.required",new EmptyValueException(messageUtil.getBundle("merchant.first.name.required")));
				}
			}
		}

		if (Util.isEmpty(merchantModel.getLastName())) {
			
			exceptions.put("merchant.last.name.required",new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
		} else{
			merchantModel.setLastName(Util.strip_html_tags(merchantModel.getLastName(), "url", exceptions));
			
			if(Util.isEmpty(merchantModel.getLastName())) {
				
				exceptions.put("merchant.last.name.required", new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
			
			} else {
			
				if(Util.isEmpty(merchantModel.getLastName())){
					exceptions.put("merchant.last.name.required", new EmptyValueException(messageUtil.getBundle("merchant.last.name.required")));
				}
			}
		}

		if (Util.isEmpty(merchantModel.getEmailId())) {
			exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
		} else {
			
			merchantModel.setEmailId(Util.strip_html_tags(merchantModel.getEmailId(), "url", exceptions));
			
			if(Util.isEmpty(merchantModel.getEmailId())) {
				
				exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
			
			} else {
			
				if(Util.isEmpty(merchantModel.getEmailId())){
					exceptions.put("merchant.email.required", new EmptyValueException(messageUtil.getBundle("merchant.email.required")));
				} else{
					if (Util.emailValidator(merchantModel.getEmailId())) {
						exceptions.put("merchant.email.invalid", new EmptyValueException(messageUtil.getBundle("merchant.email.invalid")));
					} else{
						
						try {
							MerchantModel checkModel = merchantDao.updateEmailCheck(merchantModel.getEmailId(),merchantModel.getMerchantId());
							
							if(Objects.nonNull(checkModel)){
								exceptions.put("merchant.email.exist", new Exception(messageUtil.getBundle("merchant.email.exist")));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}


		if (Util.isEmpty(merchantModel.getMobileNo())) {
			
			exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
			
		} else {
			
			merchantModel.setMobileNo(Util.strip_html_tags(merchantModel.getMobileNo(), "url", exceptions));
			
			if(Util.isEmpty(merchantModel.getMobileNo())) {
				
				exceptions.put("merchant.mobile.no.required", new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
			
			} else {
			
				if (Util.isEmpty(merchantModel.getMobileNo())) {
					
					exceptions.put("merchant.mobile.no.required",new EmptyValueException(messageUtil.getBundle("merchant.mobile.no.required")));
					
				} else {
						try {
								MerchantModel checkModel = merchantDao.updatePhoneNoCheck(merchantModel.getMobileNo(),merchantModel.getMerchantId());
								if(Objects.nonNull(checkModel)){
									exceptions.put("merchant.mobile.no.already.exist",new Exception(messageUtil.getBundle("merchant.mobile.no.already.exist")));
								 }
							
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
			
		}

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("Admin merchantProfileValidation -- END");
		}
	}
	
	/**
	 * This method is use to validate FAQ.
	 * @param faqModel
	 * @throws FormExceptions
	 */
	public void faqValidation(FaqModel faqModel) throws FormExceptions {
		
		if(logger.isInfoEnabled()){
			logger.info("faqValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		
		if(Util.isEmpty(faqModel.getQuestion())){
			exceptions.put("faq.question.required", new EmptyValueException(messageUtil.getBundle("faq.question.required")));
		} else {
			
			faqModel.setQuestion(Util.strip_html_tags(faqModel.getQuestion(), "url", exceptions));
			
			if(Util.isEmpty(faqModel.getQuestion())) {
				
				exceptions.put("faq.question.required", new EmptyValueException(messageUtil.getBundle("faq.question.required")));
			
			} else {
			
				if(Util.isEmpty(faqModel.getQuestion())){
					exceptions.put("faq.question.required", new EmptyValueException(messageUtil.getBundle("faq.question.required")));
				}
			}
		}
		
		if(Util.isEmpty(faqModel.getAnswer())){
			
			exceptions.put("faq.ans.required", new EmptyValueException(messageUtil.getBundle("faq.ans.required")));
			
		} else {
			
			faqModel.setAnswer(Util.strip_html_tags(faqModel.getAnswer(), "url", exceptions));
			
			if(Util.isEmpty(faqModel.getAnswer())) {
				
				exceptions.put("faq.ans.required", new EmptyValueException(messageUtil.getBundle("faq.ans.required")));
			
			} else {
			
				if(Util.isEmpty(faqModel.getAnswer())){
					exceptions.put("faq.ans.required", new EmptyValueException(messageUtil.getBundle("faq.ans.required")));
				}
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if(logger.isInfoEnabled()){
			logger.info("faqValidation -- END");
		}
	}
	
	/**
	 * This method is used to validate Content.
	 * @param contentModel
	 * @throws FormExceptions
	 */
	public void contentValidation(ContentModel contentModel) throws FormExceptions {
		
		if(logger.isInfoEnabled()){
			logger.info("contentValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		
		if(Util.isEmpty(contentModel.getPath())){
			
			exceptions.put("content.path.required", new EmptyValueException(messageUtil.getBundle("content.path.required")));
		
		} else {
				
			contentModel.setPath(Util.strip_html_tags(contentModel.getPath(), "url", exceptions));
			
			if(Util.isEmpty(contentModel.getPath())) {
				
				exceptions.put("content.path.required", new EmptyValueException(messageUtil.getBundle("content.path.required")));
			
			} else {
			
				if(Util.isEmpty(contentModel.getPath())){
					exceptions.put("content.path.required", new EmptyValueException(messageUtil.getBundle("content.path.required")));
				}
			}
		}
		
		if(Util.isEmpty(contentModel.getContent())){
			
			exceptions.put("content.content.required", new EmptyValueException(messageUtil.getBundle("content.content.required")));
			
		} else {
			
			contentModel.setContent(Util.strip_html_tags(contentModel.getContent(), "url", exceptions));
		
		if(Util.isEmpty(contentModel.getContent())) {
			
			exceptions.put("content.content.required", new EmptyValueException(messageUtil.getBundle("content.content.required")));
		
		} else {
		
			if(Util.isEmpty(contentModel.getContent())){
				exceptions.put("content.content.required", new EmptyValueException(messageUtil.getBundle("content.content.required")));
			}
		}
	}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if(logger.isInfoEnabled()){
			logger.info("contentValidation -- END");
		}
	}
	
	/**
	 * This method is used to Validate Create UserId and Password.
	 * @param merchantModel
	 * @throws FormExceptions
	 */
	public void createUserIdAndPasswordValidation(MerchantModel merchantModel) throws FormExceptions {
		
		if(logger.isInfoEnabled()){
			logger.info("createUserIdAndPasswordValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(merchantModel.getEblId())){
			
			exceptions.put("ebl.id.required", new EmptyValueException(messageUtil.getBundle("ebl.id.required")));
			
		} else {
			
			merchantModel.setUserName(Util.strip_html_tags(merchantModel.getEblId(), "url", exceptions));
			
			if(Util.isEmpty(merchantModel.getEblId())) {
				
				exceptions.put("ebl.id.required", new EmptyValueException(messageUtil.getBundle("ebl.id.required")));
			
			}
		}
		
		
		if(Util.isEmpty(merchantModel.getEblUserName())){
			
			exceptions.put("ebl.user.name.required", new EmptyValueException(messageUtil.getBundle("ebl.user.name.required")));
			
		} else {
			
			merchantModel.setUserName(Util.strip_html_tags(merchantModel.getEblUserName(), "url", exceptions));
			
			if(Util.isEmpty(merchantModel.getEblUserName())) {
				
				exceptions.put("ebl.user.name.required", new EmptyValueException(messageUtil.getBundle("ebl.user.name.required")));
			
			}
		}
		
		
		
		if(Util.isEmpty(merchantModel.getEblPassword())){
			
			exceptions.put("ebl.password.required", new EmptyValueException(messageUtil.getBundle("ebl.password.required")));
			
		} else {
			
			merchantModel.setPassword(Util.strip_html_tags(merchantModel.getEblPassword(), "url", exceptions));
			
			if(Util.isEmpty(merchantModel.getEblPassword())) {
				
				exceptions.put("ebl.password.required", new EmptyValueException(messageUtil.getBundle("ebl.password.required")));
			
			}
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if(logger.isInfoEnabled()){
			logger.info("createUserIdAndPasswordValidation -- END");
		}
	}
	
	/**
	 * This method is used to Validate Card Percentage.
	 * @param cardPercentageModel
	 * @throws FormExceptions
	 */
	public void saveCardPercentageValidation(MerchantCardPercentageModel cardPercentageModel) throws FormExceptions {
		
		if(logger.isInfoEnabled()){
			logger.info("saveCardPercentageValidation -- START");
		}
		
		Map<String, Exception> exceptions = new LinkedHashMap<String, Exception>();
		
		if(Util.isEmpty(cardPercentageModel.getMerchantId())) {
			
			exceptions.put("merchant.id.required", new EmptyValueException(messageUtil.getBundle("merchant.id.required")));
			
		} else {
			
			cardPercentageModel.setMerchantId(Util.strip_html_tags(cardPercentageModel.getMerchantId(), "url", exceptions));
			
			if(Util.isEmpty(cardPercentageModel.getMerchantId())) {
				
				exceptions.put("merchant.id.required", new EmptyValueException(messageUtil.getBundle("merchant.id.required")));
			
			} else {
				
				try {
					
					MerchantModel merchantModel = merchantDao.fetchActiveMerchantById(Long.valueOf(cardPercentageModel.getMerchantId()));
				
					if(Objects.isNull(merchantModel)){
						
						exceptions.put("invalid.merchant", new EmptyValueException(messageUtil.getBundle("invalid.merchant")));
					}
					
				} catch (Exception e) {
						e.printStackTrace();
				}
			}
		}
		
		boolean flag = true;
	 	List<Long> listCardTypeIds = new ArrayList<Long>();
		List<String> percentage = new ArrayList<String>();
		List<String> flatFee = new ArrayList<String>();
		
		 if (Objects.nonNull(cardPercentageModel.getListCardTypeIds()) && cardPercentageModel.getListCardTypeIds() != null && cardPercentageModel.getListCardTypeIds().size() > 0) {
			 
			 for(int i = 0 ; i<cardPercentageModel.getListCardTypeIds().size();i++){

				 	if (Util.isEmpty(cardPercentageModel.getListCardTypeIds().get(i)) && flag) {
						
						flag = false;
						exceptions.put("card.typeid.required",new EmptyValueException(messageUtil.getBundle("card.typeid.required")));
						
					} else if(String.valueOf(cardPercentageModel.getListCardTypeIds().get(i)).length() > 10 && flag){
						
						flag = false;
						exceptions.put("card.typeid.maxlength", new EmptyValueException(messageUtil.getBundle("card.typeid.maxlength")));
						
					}else if(!Util.isSpecialChar(String.valueOf(cardPercentageModel.getListCardTypeIds().get(i))) && flag){
						
						flag = false;
						exceptions.put("card.typeid.specialChar",new EmptyValueException(messageUtil.getBundle("card.typeid.specialChar")));
						
					} else{
						
						String str = Util.strip_html_tags(String.valueOf(cardPercentageModel.getListCardTypeIds().get(i)), "url");
						if(str.equals("")){
							
							exceptions.put("card.typeid.required",new EmptyValueException(messageUtil.getBundle("card.typeid.required")));
						} else if(!Util.checkAlpha(String.valueOf(cardPercentageModel.getListCardTypeIds().get(i)))) {
							
							exceptions.put("card.typeid.numeric", new Exception(messageUtil.getBundle("card.typeid.numeric")));
						}
						
						listCardTypeIds.add(Long.valueOf(str));
					}
					
					
					if (Util.isEmpty(cardPercentageModel.getListPercentage().get(i)) && flag) {
						
						percentage.add(i, "0");
						
					} else if(String.valueOf(cardPercentageModel.getListPercentage().get(i)).length() > 6 && flag){
						
						flag = false;
						exceptions.put("percentage.maxlength", new EmptyValueException(messageUtil.getBundle("percentage.maxlength")));
						
					} else if(!Util.isSpecialChar(String.valueOf(cardPercentageModel.getListPercentage().get(i))) && flag){
						
						flag = false;
						exceptions.put("percentage.specialChar",new EmptyValueException(messageUtil.getBundle("percentage.specialChar")));
						
					} else{
						
						String str = Util.strip_html_tags(String.valueOf(cardPercentageModel.getListPercentage().get(i)), "url");
						if(str.equals("")){
							
							exceptions.put("percentage.required",new EmptyValueException(messageUtil.getBundle("percentage.required")));
							
						} else if(!Util.checkDecialDigit(cardPercentageModel.getListPercentage().get(i))) {
							
							exceptions.put("percentage.numeric", new Exception(messageUtil.getBundle("percentage.numeric")));
							
						} else if(cardPercentageModel.getListPercentage().get(i).contains("-")) {
							
							exceptions.put("percentage.negative.error", new EmptyValueException(messageUtil.getBundle("percentage.negative.error")));
							
						} else if(Double.valueOf(cardPercentageModel.getListPercentage().get(i)) > 100){
							
							exceptions.put("percentage.max.error", new EmptyValueException(messageUtil.getBundle("percentage.max.error")));
						}
						percentage.add(str);
					}
					
					
					if (Util.isEmpty(cardPercentageModel.getListFlatFees().get(i)) && flag) {
						
						flatFee.add(i, "0");
						
					} else if(String.valueOf(cardPercentageModel.getListFlatFees().get(i)).length() > 10 && flag){
						
						flag = false;
						exceptions.put("flatfee.maxlength", new EmptyValueException(messageUtil.getBundle("flatfee.maxlength")));
						
					}else if(!Util.isSpecialChar(String.valueOf(cardPercentageModel.getListFlatFees().get(i))) && flag){
						
						flag = false;
						exceptions.put("flatfee.specialChar",new EmptyValueException(messageUtil.getBundle("flatfee.specialChar")));
						
					} else{
						
						String str = Util.strip_html_tags(String.valueOf(cardPercentageModel.getListFlatFees().get(i)), "url");
						if(str.equals("")){
							
							exceptions.put("flatfee.required",new EmptyValueException(messageUtil.getBundle("flatfee.required")));
						} else if(!Util.checkDecialDigit(cardPercentageModel.getListFlatFees().get(i))) {
							
							exceptions.put("flatfee.numeric", new Exception(messageUtil.getBundle("flatfee.numeric")));
							
						} else if(cardPercentageModel.getListFlatFees().get(i).contains("-")) {
							
							exceptions.put("flatfee.negative.error", new EmptyValueException(messageUtil.getBundle("flatfee.negative.error")));
							
						}
						
						flatFee.add(str);
					}
				 
			 }
			 
			 	cardPercentageModel.setListCardTypeIds(listCardTypeIds);
				cardPercentageModel.setListPercentage(percentage);
				cardPercentageModel.setListFlatFees(flatFee);
		 }
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if(logger.isInfoEnabled()){
			logger.info("saveCardPercentageValidation -- END");
		}
	}
}
