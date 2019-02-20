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
	//WASIF 20190110
    private String card_brand;
    private String provided_card_number;
    private String bank_merchant_id;
    private String transaction_type;
    private String bkash_payment_number;
    private String billing_name;
    private String device_ipaddress;
    private String bank;
    //WASIF 20190210
    /*
    private String notification_email;
    private String notification_sms;
   /**/ 

	
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
    public String getCard_brand() {
		return card_brand;
	}

	public void setCard_brand(String card_brand) {
		this.card_brand = card_brand;
	}

	public String getProvided_card_number() {
		return provided_card_number;
	}

	public void setProvided_card_number(String provided_card_number) {
		this.provided_card_number = provided_card_number;
	}

	public String getBank_merchant_id() {
		return bank_merchant_id;
	}

	public void setBank_merchant_id(String bank_merchant_id) {
		this.bank_merchant_id = bank_merchant_id;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getBkash_customer() {
		return bkash_payment_number;
	}

	public void setBkash_customer(String bkash_payment_number) {
		this.bkash_payment_number = bkash_payment_number;
	}

	public String getBilling_name() {
		return billing_name;
	}

	public void setBilling_name(String billing_name) {
		this.billing_name = billing_name;
	}

	public String getDevice_ipaddress() {
		return device_ipaddress;
	}

	public void setDevice_ipaddress(String device_ipaddress) {
		this.device_ipaddress = device_ipaddress;
	}

//TODO
    
    
	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}
	//TODO
/*
	public String getNotification_email() {
		if(notification_email != null && !notification_email.isEmpty())
			notification_email="0";
		return notification_email;
	}

	public void setNotification_email(String notification_email) {
		this.notification_email = notification_email;
	}

	public String getNotification_sms() {

			if(notification_sms != null && !notification_sms.isEmpty())
				notification_sms="0";
			
		return notification_sms;
	}

	public void setNotification_sms(String notification_sms) {
		this.notification_sms = notification_sms;
	}/**/

	@Override

	public String toString() {
		return "PaymentModel [orderID=" + orderID + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", name=" + name + ", email="
				+ email + ", mobileNumber=" + mobileNumber + ", successURL="
				+ successURL + ", failureURL=" + failureURL + ", amount="
				+ amount + ", accessKey=" + accessKey + ", orderTransactionID="
				+ orderTransactionID + ", token=" + token + ", responseCode="
				+ responseCode + ", responseMessage=" + responseMessage
				+ ", card_brand=" + card_brand + ", provided_card_number="+provided_card_number
				+ ", bank_merchant_id=" + bank_merchant_id + ", transaction_type="+transaction_type
				+ ", bkash_payment_number=" + bkash_payment_number + ", billing_name="+billing_name
				+ ", device_ipaddress=" + device_ipaddress 
				//TODO
				/*
				+ ", notification_sms=" +notification_sms
				+ ", notification_email=" +notification_email/**/
				+ ", merchantModel=" + merchantModel + ", customerDetails="
				+ customerDetails + ", SESSIONKEY=" + SESSIONKEY + ",transactionDate=" + transactionDate
				+ ", transactionModel=" + transactionModel + ", transactionStatus="
				+ transactionStatus + "]";
	}
	/*
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
	}/**/

}
