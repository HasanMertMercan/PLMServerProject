package com.properties;

import java.util.ArrayList;

public class UpdateInstructionListProperties {
	
	private String operationId;
	private String operationInstructionListId;
	private ArrayList<String> operationInstructionList;
	private Boolean changeListType;
	
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getOperationInstructionListId() {
		return operationInstructionListId;
	}
	public void setOperationInstructionListId(String operationInstructionListId) {
		this.operationInstructionListId = operationInstructionListId;
	}
	public ArrayList<String> getOperationInstructionList() {
		return operationInstructionList;
	}
	public void setOperationInstructionList(ArrayList<String> operationInstructionList) {
		this.operationInstructionList = operationInstructionList;
	}
	public Boolean getChangeListType() {
		return changeListType;
	}
	public void setChangeListType(Boolean changeListType) {
		this.changeListType = changeListType;
	}

}
