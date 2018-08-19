package com.lebupay.model;

public class TicketModel extends CommonModel {

	private Long ticketId;
	private String subject;
	private String ticketMessage;
	private String reply;
	private TypeModel typeModel;

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTicketMessage() {
		return ticketMessage;
	}

	public void setTicketMessage(String ticketMessage) {
		this.ticketMessage = ticketMessage;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public TypeModel getTypeModel() {
		return typeModel;
	}

	public void setTypeModel(TypeModel typeModel) {
		this.typeModel = typeModel;
	}

	@Override
	public String toString() {
		return "TicketModel [ticketId=" + ticketId + ", subject=" + subject
				+ ", ticketMessage=" + ticketMessage + ", reply=" + reply
				+ ", typeModel=" + typeModel + "]";
	}

}
