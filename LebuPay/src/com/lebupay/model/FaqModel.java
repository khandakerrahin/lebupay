package com.lebupay.model;

public class FaqModel extends CommonModel {

	private Long faqId;
	private String question;
	private String answer;
	private TypeModel typeModel;

	public Long getFaqId() {
		return faqId;
	}

	public void setFaqId(Long faqId) {
		this.faqId = faqId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public TypeModel getTypeModel() {
		return typeModel;
	}

	public void setTypeModel(TypeModel typeModel) {
		this.typeModel = typeModel;
	}

	@Override
	public String toString() {
		return "FaqModel [faqId=" + faqId + ", question=" + question
				+ ", answer=" + answer + ", typeModel=" + typeModel + "]";
	}

}
