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

	@Override
	public String toString() {
		return "EmailInvoicingModel [firstName=" + firstName + ", lastName="
				+ lastName + ", emailId=" + emailId + ", mobileNumber="
				+ mobileNumber + ", invoiceNo=" + invoiceNo + ", type=" + type
				+ ", amount=" + amount + ", BDT=" + BDT + ", subject="
				+ subject + ", description=" + description
				+ ", merchantEmailId=" + merchantEmailId + ", plugger="
				+ plugger + ", pluggers=" + pluggers + "]";
	}

}
