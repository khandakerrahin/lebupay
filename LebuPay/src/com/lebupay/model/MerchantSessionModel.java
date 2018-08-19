package com.lebupay.model;

public class MerchantSessionModel {

	private long merchantId;
	private String firstName;
	private String lastName;
	private Double transactionAmount;
	private String emailId;
	private String mobileNo;
	private String createdBy;
	private long companyId;
	private String logoName;
	private Double loyaltyPoint;
	private String uniqueKey;
	private String accessKey;
	private String secretKey;

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	
	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
	
	public Double getLoyaltyPoint() {
		return loyaltyPoint;
	}

	public void setLoyaltyPoint(Double loyaltyPoint) {
		this.loyaltyPoint = loyaltyPoint;
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

	@Override
	public String toString() {
		return "MerchantSessionModel [merchantId=" + merchantId
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", transactionAmount=" + transactionAmount + ", emailId="
				+ emailId + ", mobileNo=" + mobileNo + ", createdBy="
				+ createdBy + ", companyId=" + companyId + ", logoName="
				+ logoName + ", loyaltyPoint=" + loyaltyPoint + ", uniqueKey="
				+ uniqueKey + ", accessKey=" + accessKey + ", secretKey="
				+ secretKey + "]";
	}

}
