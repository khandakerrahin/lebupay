package com.lebupay.model;

public class TransactionModel extends CommonModel {

	private Long transactionId;
	private Double amount;
	private Double balance;
	private MerchantModel merchantModel;
	private String txnId;
	private String responseMessage;
	private Double loyaltyPoint;
	private int transactionStatus;
	private String order_id;
	private String authorizationResponse_date;
	private String totalCapturedAmount;
	private String fundingMethod;
	private String acquirerMessage;
	private String financialNetworkCode;
	private String transactionIdentifier;
	private String nameOnCard;
	private String card_expiry_year;
	private String authorizationResponse_time;
	private String transaction_currency;
	private String SecureId;
	private String acquirerCode;
	private String authorizationResponse_stan;
	private String merchantId;
	private String totalAuthorizedAmount;
	private String provided_card_number;
	private String cardSecurityCode_gatewayCode;
	private String authenticationToken;
	private String transaction_receipt;
	private String response_gatewayCode;
	private String order_status;
	private String acquirer_date;
	private String transaction_id;
	private String version;
	private String commercialCardIndicator;
	private String card_brand;
	private String sourceOfFunds_type;
	private String customer_firstName;
	private String device_browser;
	private String device_ipAddress;
	private String acsEci_value;
	private String acquirer_id;
	private String settlementDate;
	private String transaction_source;
	private String result;
	private String creationTime;
	private String customer_lastName;
	private String totalRefundedAmount;
	private String acquirer_batch;
	private String description;
	private String transaction_type;
	private String financialNetworkDate;
	private String responseCode;
	private String transaction_frequency;
	private String transaction_terminal;
	private String authorizationCode;
	private String authenticationStatus;
	private String processingCode;
	private String expiry_month;
	private String secure_xid;
	private String enrollmentStatus;
	private String cardSecurityCodeError;
	private String timeZone;
	private String gatewayEntryPoint;
	private String successIndicator;
	private PaymentModel paymentModel;
	private BKashModel transaction;
	private String cardPercentage;
	private String type;
	private Double grossAmount;
	private String bkashTrxId;
	private String bank;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public MerchantModel getMerchantModel() {
		return merchantModel;
	}

	public void setMerchantModel(MerchantModel merchantModel) {
		this.merchantModel = merchantModel;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Double getLoyaltyPoint() {
		return loyaltyPoint;
	}

	public void setLoyaltyPoint(Double loyaltyPoint) {
		this.loyaltyPoint = loyaltyPoint;
	}

	public int getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(int transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getAuthorizationResponse_date() {
		return authorizationResponse_date;
	}

	public void setAuthorizationResponse_date(String authorizationResponse_date) {
		this.authorizationResponse_date = authorizationResponse_date;
	}

	public String getTotalCapturedAmount() {
		return totalCapturedAmount;
	}

	public void setTotalCapturedAmount(String totalCapturedAmount) {
		this.totalCapturedAmount = totalCapturedAmount;
	}

	public String getFundingMethod() {
		return fundingMethod;
	}

	public void setFundingMethod(String fundingMethod) {
		this.fundingMethod = fundingMethod;
	}

	public String getAcquirerMessage() {
		return acquirerMessage;
	}

	public void setAcquirerMessage(String acquirerMessage) {
		this.acquirerMessage = acquirerMessage;
	}

	public String getFinancialNetworkCode() {
		return financialNetworkCode;
	}

	public void setFinancialNetworkCode(String financialNetworkCode) {
		this.financialNetworkCode = financialNetworkCode;
	}

	public String getTransactionIdentifier() {
		return transactionIdentifier;
	}

	public void setTransactionIdentifier(String transactionIdentifier) {
		this.transactionIdentifier = transactionIdentifier;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getCard_expiry_year() {
		return card_expiry_year;
	}

	public void setCard_expiry_year(String card_expiry_year) {
		this.card_expiry_year = card_expiry_year;
	}

	public String getAuthorizationResponse_time() {
		return authorizationResponse_time;
	}

	public void setAuthorizationResponse_time(String authorizationResponse_time) {
		this.authorizationResponse_time = authorizationResponse_time;
	}

	public String getTransaction_currency() {
		return transaction_currency;
	}

	public void setTransaction_currency(String transaction_currency) {
		this.transaction_currency = transaction_currency;
	}

	public String getSecureId() {
		return SecureId;
	}

	public void setSecureId(String secureId) {
		SecureId = secureId;
	}

	public String getAcquirerCode() {
		return acquirerCode;
	}

	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}

	public String getAuthorizationResponse_stan() {
		return authorizationResponse_stan;
	}

	public void setAuthorizationResponse_stan(String authorizationResponse_stan) {
		this.authorizationResponse_stan = authorizationResponse_stan;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTotalAuthorizedAmount() {
		return totalAuthorizedAmount;
	}

	public void setTotalAuthorizedAmount(String totalAuthorizedAmount) {
		this.totalAuthorizedAmount = totalAuthorizedAmount;
	}

	public String getProvided_card_number() {
		return provided_card_number;
	}

	public void setProvided_card_number(String provided_card_number) {
		this.provided_card_number = provided_card_number;
	}

	public String getCardSecurityCode_gatewayCode() {
		return cardSecurityCode_gatewayCode;
	}

	public void setCardSecurityCode_gatewayCode(
			String cardSecurityCode_gatewayCode) {
		this.cardSecurityCode_gatewayCode = cardSecurityCode_gatewayCode;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getTransaction_receipt() {
		return transaction_receipt;
	}

	public void setTransaction_receipt(String transaction_receipt) {
		this.transaction_receipt = transaction_receipt;
	}

	public String getResponse_gatewayCode() {
		return response_gatewayCode;
	}

	public void setResponse_gatewayCode(String response_gatewayCode) {
		this.response_gatewayCode = response_gatewayCode;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getAcquirer_date() {
		return acquirer_date;
	}

	public void setAcquirer_date(String acquirer_date) {
		this.acquirer_date = acquirer_date;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCommercialCardIndicator() {
		return commercialCardIndicator;
	}

	public void setCommercialCardIndicator(String commercialCardIndicator) {
		this.commercialCardIndicator = commercialCardIndicator;
	}

	public String getCard_brand() {
		return card_brand;
	}

	public void setCard_brand(String card_brand) {
		this.card_brand = card_brand;
	}

	public String getSourceOfFunds_type() {
		return sourceOfFunds_type;
	}

	public void setSourceOfFunds_type(String sourceOfFunds_type) {
		this.sourceOfFunds_type = sourceOfFunds_type;
	}

	public String getCustomer_firstName() {
		return customer_firstName;
	}

	public void setCustomer_firstName(String customer_firstName) {
		this.customer_firstName = customer_firstName;
	}

	public String getDevice_browser() {
		return device_browser;
	}

	public void setDevice_browser(String device_browser) {
		this.device_browser = device_browser;
	}

	public String getDevice_ipAddress() {
		return device_ipAddress;
	}

	public void setDevice_ipAddress(String device_ipAddress) {
		this.device_ipAddress = device_ipAddress;
	}

	public String getAcsEci_value() {
		return acsEci_value;
	}

	public void setAcsEci_value(String acsEci_value) {
		this.acsEci_value = acsEci_value;
	}

	public String getAcquirer_id() {
		return acquirer_id;
	}

	public void setAcquirer_id(String acquirer_id) {
		this.acquirer_id = acquirer_id;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getTransaction_source() {
		return transaction_source;
	}

	public void setTransaction_source(String transaction_source) {
		this.transaction_source = transaction_source;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCustomer_lastName() {
		return customer_lastName;
	}

	public void setCustomer_lastName(String customer_lastName) {
		this.customer_lastName = customer_lastName;
	}

	public String getTotalRefundedAmount() {
		return totalRefundedAmount;
	}

	public void setTotalRefundedAmount(String totalRefundedAmount) {
		this.totalRefundedAmount = totalRefundedAmount;
	}

	public String getAcquirer_batch() {
		return acquirer_batch;
	}

	public void setAcquirer_batch(String acquirer_batch) {
		this.acquirer_batch = acquirer_batch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getFinancialNetworkDate() {
		return financialNetworkDate;
	}

	public void setFinancialNetworkDate(String financialNetworkDate) {
		this.financialNetworkDate = financialNetworkDate;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getTransaction_frequency() {
		return transaction_frequency;
	}

	public void setTransaction_frequency(String transaction_frequency) {
		this.transaction_frequency = transaction_frequency;
	}

	public String getTransaction_terminal() {
		return transaction_terminal;
	}

	public void setTransaction_terminal(String transaction_terminal) {
		this.transaction_terminal = transaction_terminal;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getAuthenticationStatus() {
		return authenticationStatus;
	}

	public void setAuthenticationStatus(String authenticationStatus) {
		this.authenticationStatus = authenticationStatus;
	}

	public String getProcessingCode() {
		return processingCode;
	}

	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	public String getExpiry_month() {
		return expiry_month;
	}

	public void setExpiry_month(String expiry_month) {
		this.expiry_month = expiry_month;
	}

	public String getSecure_xid() {
		return secure_xid;
	}

	public void setSecure_xid(String secure_xid) {
		this.secure_xid = secure_xid;
	}

	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	public String getCardSecurityCodeError() {
		return cardSecurityCodeError;
	}

	public void setCardSecurityCodeError(String cardSecurityCodeError) {
		this.cardSecurityCodeError = cardSecurityCodeError;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getGatewayEntryPoint() {
		return gatewayEntryPoint;
	}

	public void setGatewayEntryPoint(String gatewayEntryPoint) {
		this.gatewayEntryPoint = gatewayEntryPoint;
	}

	public String getSuccessIndicator() {
		return successIndicator;
	}

	public void setSuccessIndicator(String successIndicator) {
		this.successIndicator = successIndicator;
	}

	public PaymentModel getPaymentModel() {
		return paymentModel;
	}

	public void setPaymentModel(PaymentModel paymentModel) {
		this.paymentModel = paymentModel;
	}

	public BKashModel getTransaction() {
		return transaction;
	}

	public void setTransaction(BKashModel transaction) {
		this.transaction = transaction;
	}

	public String getCardPercentage() {
		return cardPercentage;
	}

	public void setCardPercentage(String cardPercentage) {
		this.cardPercentage = cardPercentage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public String getBkashTrxId() {
		return bkashTrxId;
	}

	public void setBkashTrxId(String bkashTrxId) {
		this.bkashTrxId = bkashTrxId;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Override
	public String toString() {
		return "TransactionModel [transactionId=" + transactionId + ", amount="
				+ amount + ", balance=" + balance + ", merchantModel="
				+ merchantModel + ", txnId=" + txnId + ", responseMessage="
				+ responseMessage + ", loyaltyPoint=" + loyaltyPoint
				+ ", transactionStatus=" + transactionStatus + ", order_id="
				+ order_id + ", authorizationResponse_date="
				+ authorizationResponse_date + ", totalCapturedAmount="
				+ totalCapturedAmount + ", fundingMethod=" + fundingMethod
				+ ", acquirerMessage=" + acquirerMessage
				+ ", financialNetworkCode=" + financialNetworkCode
				+ ", transactionIdentifier=" + transactionIdentifier
				+ ", nameOnCard=" + nameOnCard + ", card_expiry_year="
				+ card_expiry_year + ", authorizationResponse_time="
				+ authorizationResponse_time + ", transaction_currency="
				+ transaction_currency + ", SecureId=" + SecureId
				+ ", acquirerCode=" + acquirerCode
				+ ", authorizationResponse_stan=" + authorizationResponse_stan
				+ ", merchantId=" + merchantId + ", totalAuthorizedAmount="
				+ totalAuthorizedAmount + ", provided_card_number="
				+ provided_card_number + ", cardSecurityCode_gatewayCode="
				+ cardSecurityCode_gatewayCode + ", authenticationToken="
				+ authenticationToken + ", transaction_receipt="
				+ transaction_receipt + ", response_gatewayCode="
				+ response_gatewayCode + ", order_status=" + order_status
				+ ", acquirer_date=" + acquirer_date + ", transaction_id="
				+ transaction_id + ", version=" + version
				+ ", commercialCardIndicator=" + commercialCardIndicator
				+ ", card_brand=" + card_brand + ", sourceOfFunds_type="
				+ sourceOfFunds_type + ", customer_firstName="
				+ customer_firstName + ", device_browser=" + device_browser
				+ ", device_ipAddress=" + device_ipAddress + ", acsEci_value="
				+ acsEci_value + ", acquirer_id=" + acquirer_id
				+ ", settlementDate=" + settlementDate
				+ ", transaction_source=" + transaction_source + ", result="
				+ result + ", creationTime=" + creationTime
				+ ", customer_lastName=" + customer_lastName
				+ ", totalRefundedAmount=" + totalRefundedAmount
				+ ", acquirer_batch=" + acquirer_batch + ", description="
				+ description + ", transaction_type=" + transaction_type
				+ ", financialNetworkDate=" + financialNetworkDate
				+ ", responseCode=" + responseCode + ", transaction_frequency="
				+ transaction_frequency + ", transaction_terminal="
				+ transaction_terminal + ", authorizationCode="
				+ authorizationCode + ", authenticationStatus="
				+ authenticationStatus + ", processingCode=" + processingCode
				+ ", expiry_month=" + expiry_month + ", secure_xid="
				+ secure_xid + ", enrollmentStatus=" + enrollmentStatus
				+ ", cardSecurityCodeError=" + cardSecurityCodeError
				+ ", timeZone=" + timeZone + ", gatewayEntryPoint="
				+ gatewayEntryPoint + ", successIndicator=" + successIndicator
				+ ", paymentModel=" + paymentModel + ", transaction="
				+ transaction + ", cardPercentage=" + cardPercentage
				+ ", type=" + type + ", grossAmount=" + grossAmount
				+ ", bkashTrxId=" + bkashTrxId + ", bank=" + bank + "]";
	}

}
