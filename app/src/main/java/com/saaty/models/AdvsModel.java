package com.saaty.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvsModel{

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private List<AdvDataItem> advData;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setAdvData(List<AdvDataItem> advData){
		this.advData = advData;
	}

	public List<AdvDataItem> getAdvData(){
		return advData;
	}

	@Override
 	public String toString(){
		return 
			"AdvsModel{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",advData = '" + advData + '\'' + 
			"}";
		}
}