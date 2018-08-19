package com.lebupay.model;

public class ContentModel extends CommonModel {

	private Long contentId;
	private String path;
	private String content;
	private TypeModel typeModel;

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TypeModel getTypeModel() {
		return typeModel;
	}

	public void setTypeModel(TypeModel typeModel) {
		this.typeModel = typeModel;
	}

	@Override
	public String toString() {
		return "ContentModel [contentId=" + contentId + ", path=" + path
				+ ", content=" + content + ", typeModel=" + typeModel + "]";
	}

}
