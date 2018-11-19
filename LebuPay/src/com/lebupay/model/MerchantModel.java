package com.lebupay.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class MerchantModel extends CommonModel {

	private Long merchantId;
	private TypeModel typeModel;
	private String firstName;
	private String lastName;
	private String emailId;
	private String userName;
	private String mobileNo;
	private String password;
	private String confirmPassword;
	private String confirmNewPassword;
	private String uniqueKey;
	private String accessKey;
	private String secretKey;
	private Integer phoneCode;
	private String phoneVerified;
	private String captcha;
	private Double transactionAmount;
	private Double loyaltyPoint;
	private String logoName;
	private MultipartFile logo;
	private List<TicketModel> ticketModels;
	private List<QuickPayModel> quickPayModels;
	private List<TransactionModel> transactionModels;
	private CompanyModel companyModel;
	private CheckoutModel checkoutModel;
	private String eblPassword;
	private String eblUserName;
	private String eblId;
	private String cityMerchantId;
	//WASIF 20181114
	private String seblPassword;
	private String seblUserName;
	private String seblId;

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public TypeModel getTypeModel() {
		return typeModel;
	}

	public void setTypeModel(TypeModel typeModel) {
		this.typeModel = typeModel;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Integer getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(Integer phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(String phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Double getLoyaltyPoint() {
		return loyaltyPoint;
	}

	public void setLoyaltyPoint(Double loyaltyPoint) {
		this.loyaltyPoint = loyaltyPoint;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public MultipartFile getLogo() {
		return logo;
	}

	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}

	public List<TicketModel> getTicketModels() {
		return ticketModels;
	}

	public void setTicketModels(List<TicketModel> ticketModels) {
		this.ticketModels = ticketModels;
	}

	public List<QuickPayModel> getQuickPayModels() {
		return quickPayModels;
	}

	public void setQuickPayModels(List<QuickPayModel> quickPayModels) {
		this.quickPayModels = quickPayModels;
	}

	public List<TransactionModel> getTransactionModels() {
		return transactionModels;
	}

	public void setTransactionModels(List<TransactionModel> transactionModels) {
		this.transactionModels = transactionModels;
	}

	public CompanyModel getCompanyModel() {
		return companyModel;
	}

	public void setCompanyModel(CompanyModel companyModel) {
		this.companyModel = companyModel;
	}

	public CheckoutModel getCheckoutModel() {
		return checkoutModel;
	}

	public void setCheckoutModel(CheckoutModel checkoutModel) {
		this.checkoutModel = checkoutModel;
	}

	public String getEblPassword() {
		return eblPassword;
	}

	public void setEblPassword(String eblPassword) {
		this.eblPassword = eblPassword;
	}

	public String getEblUserName() {
		return eblUserName;
	}

	public void setEblUserName(String eblUserName) {
		this.eblUserName = eblUserName;
	}

	public String getEblId() {
		return eblId;
	}

	public void setEblId(String eblId) {
		this.eblId = eblId;
	}

	public String getSeblPassword() {
		return seblPassword;
	}

	public void setSeblPassword(String seblPassword) {
		this.seblPassword = seblPassword;
	}

	public String getSeblUserName() {
		return seblUserName;
	}

	public void setSeblUserName(String seblUserName) {
		this.seblUserName = seblUserName;
	}

	public String getSeblId() {
		return seblId;
	}

	public void setSeblId(String seblId) {
		this.seblId = seblId;
	}

	public String getCityMerchantId() {
		return cityMerchantId;
	}

	public void setCityMerchantId(String cityMerchantId) {
		this.cityMerchantId = cityMerchantId;
	}

	@Override
	public String toString() {
		return "MerchantModel [merchantId=" + merchantId + ", typeModel="
				+ typeModel + ", firstName=" + firstName + ", lastName="
				+ lastName + ", emailId=" + emailId + ", userName=" + userName
				+ ", mobileNo=" + mobileNo + ", password=" + password
				+ ", confirmPassword=" + confirmPassword
				+ ", confirmNewPassword=" + confirmNewPassword + ", uniqueKey="
				+ uniqueKey + ", accessKey=" + accessKey + ", secretKey="
				+ secretKey + ", phoneCode=" + phoneCode + ", phoneVerified="
				+ phoneVerified + ", captcha=" + captcha
				+ ", transactionAmount=" + transactionAmount
				+ ", loyaltyPoint=" + loyaltyPoint + ", logoName=" + logoName
				+ ", logo=" + logo + ", ticketModels=" + ticketModels
				+ ", quickPayModels=" + quickPayModels + ", transactionModels="
				+ transactionModels + ", companyModel=" + companyModel
				+ ", checkoutModel=" + checkoutModel + ", eblPassword="
				+ eblPassword + ", eblUserName=" + eblUserName + ", eblId="
				+ eblId + ",seblPassword="+seblPassword+ ", seblUserName="+ seblUserName+ ", seblId="+seblId+ ",cityMerchantId=" + cityMerchantId + "]";
	}
	/*public String toString() {
	return "MerchantModel [merchantId=" + merchantId + ", typeModel="
			+ typeModel + ", firstName=" + firstName + ", lastName="
			+ lastName + ", emailId=" + emailId + ", userName=" + userName
			+ ", mobileNo=" + mobileNo + ", password=" + password
			+ ", confirmPassword=" + confirmPassword
			+ ", confirmNewPassword=" + confirmNewPassword + ", uniqueKey="
			+ uniqueKey + ", accessKey=" + accessKey + ", secretKey="
			+ secretKey + ", phoneCode=" + phoneCode + ", phoneVerified="
			+ phoneVerified + ", captcha=" + captcha
			+ ", transactionAmount=" + transactionAmount
			+ ", loyaltyPoint=" + loyaltyPoint + ", logoName=" + logoName
			+ ", logo=" + logo + ", ticketModels=" + ticketModels
			+ ", quickPayModels=" + quickPayModels + ", transactionModels="
			+ transactionModels + ", companyModel=" + companyModel
			+ ", checkoutModel=" + checkoutModel + ", eblPassword="
			+ eblPassword + ", eblUserName=" + eblUserName + ", eblId="
			+ eblId + ", cityMerchantId=" + cityMerchantId + "]";
}*/
	
	

}
