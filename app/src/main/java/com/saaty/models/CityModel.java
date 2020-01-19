package com.saaty.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CityModel{

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private List<CitydatamodelItem> citydatamodel;

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

	public void setCitydatamodel(List<CitydatamodelItem> citydatamodel){
		this.citydatamodel = citydatamodel;
	}

	public List<CitydatamodelItem> getCitydatamodel(){
		return citydatamodel;
	}

	@Override
 	public String toString(){
		return 
			"CityModel{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",citydatamodel = '" + citydatamodel + '\'' + 
			"}";
		}
}