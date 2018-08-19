package com.lebupay.model;

public class TypeModel extends CommonModel {

	private Long typeId;
	private String typeName;

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "TypeModel [typeId=" + typeId + ", typeName=" + typeName + "]";
	}

}
