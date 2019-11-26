package com.saaty.models;

import com.google.gson.annotations.SerializedName;


public class Data{

	@SerializedName("name")
	private Object name;

	@SerializedName("id")
	private int id;

	@SerializedName("token")
	private String token;

	@SerializedName("email")
	private  String email;

	@SerializedName("mobile")
	private  String mobile;

	public String getEmail() {
		return email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setName(Object name){
		this.name = name;
	}

	public Object getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}