package com.lebupay.model;

public class PaymentModel extends CommonModel {

	private Long orderID;
	private String firstName;
	private String lastName;
	private String name;
	private String email;
	private String mobileNumber;
	private String successURL;
	private String failureURL;
	private Double amount;
	private String accessKey;
	private String orderTransactionID;
	private String token;
	private String responseCode;
	private String responseMessage;
	private MerchantModel merchantModel;
	private String customerDetails;
	private String SESSIONKEY;
	private TransactionModel transactionModel;
	private String transactionStatus;
	private String transactionDate;

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return email;
	}

	public void setEmailId(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getSuccessURL() {
		return successURL;
	}

	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}

	public String getFailureURL() {
		return failureURL;
	}

	public void setFailureURL(String failureURL) {
		this.failureURL = failureURL;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getOrderTransactionID() {
		return orderTransactionID;
	}

	public void setOrderTransactionID(String orderTransactionID) {
		this.orderTransactionID = orderTransactionID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public MerchantModel getMerchantModel() {
		return merchantModel;
	}

	public void setMerchantModel(MerchantModel merchantModel) {
		this.merchantModel = merchantModel;
	}

	public String getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(String customerDetails) {
		this.customerDetails = customerDetails;
	}

	public String getSESSIONKEY() {
		return SESSIONKEY;
	}

	public void setSESSIONKEY(String sESSIONKEY) {
		SESSIONKEY = sESSIONKEY;
	}

	public TransactionModel getTransactionModel() {
		return transactionModel;
	}

	public void setTransactionModel(TransactionModel transactionModel) {
		this.transactionModel = transactionModel;
	}
	
	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "PaymentModel [orderID=" + orderID + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", name=" + name + ", email="
				+ email + ", mobileNumber=" + mobileNumber + ", successURL="
				+ successURL + ", failureURL=" + failureURL + ", amount="
				+ amount + ", accessKey=" + accessKey + ", orderTransactionID="
				+ orderTransactionID + ", token=" + token + ", responseCode="
				+ responseCode + ", responseMessage=" + responseMessage
				+ ", merchantModel=" + merchantModel + ", customerDetails="
				+ customerDetails + ", SESSIONKEY=" + SESSIONKEY + ",transactionDate=" + transactionDate
				+ ", transactionModel=" + transactionModel + ", transactionStatus="
				+ transactionStatus + "]";
	}

}
