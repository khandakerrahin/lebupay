package com.lebupay.model;

public class ContactUsModel extends CommonModel {

	private Long contactUsId;
	private String subject;
	private String contactUsMessage;
	private String reply;
	private String emailId;
	private String name;

	public Long getContactUsId() {
		return contactUsId;
	}

	public void setContactUsId(Long contactUsId) {
		this.contactUsId = contactUsId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContactUsMessage() {
		return contactUsMessage;
	}

	public void setContactUsMessage(String contactUsMessage) {
		this.contactUsMessage = contactUsMessage;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ContactUsModel [contactUsId=" + contactUsId + ", subject="
				+ subject + ", contactUsMessage=" + contactUsMessage
				+ ", reply=" + reply + ", emailId=" + emailId + ", name="
				+ name + "]";
	}

}
