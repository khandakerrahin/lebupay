package com.lebupay.model;

import java.util.List;

public class EmailInvoicingModel extends CommonModel {

	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNumber;
	private String invoiceNo;
	private String type;
	private String amount;
	private String BDT;
	private String subject;
	private String description;
	private String merchantEmailId;
	private String plugger;
	
//	added by Shaker on 16.04.2019
	private String emailMessageBody;
	private String cc;
	private String bcc;
	private String action; 
	private String header;
	private String resetLink;
	private String paymentLink;
	private String otp;
	private String query;
	private String reply;
	private String cardType;
	private String cardNumber;
	private String bankTrxID;
	private String trxID;
	private String IPAddress;
	private String transactionType;
	private String billingAddress;
	private String currency;
	private String nameOnCard;
	private Boolean isTemplate = false;
	
	private List<String> pluggers;

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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBDT() {
		return BDT;
	}

	public void setBDT(String bDT) {
		BDT = bDT;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchantEmailId() {
		return merchantEmailId;
	}

	public void setMerchantEmailId(String merchantEmailId) {
		this.merchantEmailId = merchantEmailId;
	}

	public String getPlugger() {
		return plugger;
	}

	public void setPlugger(String plugger) {
		this.plugger = plugger;
	}

	public List<String> getPluggers() {
		return pluggers;
	}
	   
	public void setPluggers(List<String> pluggers) {
		this.pluggers = pluggers;
	}
	
	public String getEmailMessageBody() {
		return emailMessageBody;
	}

	public void setEmailMessageBody(String emailMessageBody) {
		this.emailMessageBody = emailMessageBody;
	}
	
	public String getCC() {
		return cc;
	}

	public void setCC(String cc) {
		this.cc = cc;
	}
	
	public String getBCC() {
		return bcc;
	}

	public void setBCC(String bcc) {
		this.bcc = bcc;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getResetLink() {
		return resetLink;
	}

	public void setResetLink(String resetLink) {
		this.resetLink = resetLink;
	}
	
	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}
	
	public String getOTP() {
		return otp;
	}

	public void setOTP(String otp) {
		this.otp = otp;
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
	
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String IPAddress) {
		this.IPAddress = IPAddress;
	}
	
	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	
	public String getBankTrxID() {
		return bankTrxID;
	}

	public void setBankTrxID(String bankTrxID) {
		this.bankTrxID = bankTrxID;
	}
	
	public String getTrxID() {
		return trxID;
	}

	public void setTrxID(String trxID) {
		this.trxID = trxID;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}
	
	public Boolean getIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	@Override
	public String toString() {
		return "EmailInvoicingModel [firstName=" + firstName + ", lastName="
				+ lastName + ", emailId=" + emailId + ", mobileNumber="
				+ mobileNumber + ", invoiceNo=" + invoiceNo + ", type=" + type
				+ ", amount=" + amount + ", BDT=" + BDT + ", subject="
				+ subject + ", description=" + description
				+ ", merchantEmailId=" + merchantEmailId + ", plugger="
				+ plugger + ", pluggers=" + pluggers + ", emailMessageBody=" + emailMessageBody
				+ ", cc=" + cc + ", bcc=" + bcc
				+ ", action=" + action + ", header=" + header
				+ ", resetLink=" + resetLink + ", paymentLink=" + paymentLink
				+ ", otp=" + otp + ", query=" + query
				+ ", reply=" + reply + ", cardType=" + cardType + ", cardNumber=" + cardNumber 
				+ ", bankTrxID=" + bankTrxID + ", trxID=" + trxID + ", IPAddress=" + IPAddress
				+ ", transactionType=" + transactionType + ", billingAddress=" + billingAddress
				+ ", currency=" + currency
				+ ", nameOnCard=" + nameOnCard
				+ ", isTemplate=" + isTemplate + "]";       
	}
}
