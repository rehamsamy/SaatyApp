package com.saaty.models;

import com.google.gson.annotations.SerializedName;

public class AdvDataItem{

	@SerializedName("updated_at")
	private Object updatedAt;

	@SerializedName("value_4")
	private String value4;

	@SerializedName("created_at")
	private Object createdAt;

	@SerializedName("value_2")
	private String value2;

	@SerializedName("value_3")
	private String value3;

	@SerializedName("value_1")
	private String value1;

	@SerializedName("setting_id")
	private int settingId;

	@SerializedName("key")
	private String key;

	@SerializedName("group")
	private String group;

	public void setUpdatedAt(Object updatedAt){
		this.updatedAt = updatedAt;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public void setValue4(String value4){
		this.value4 = value4;
	}

	public String getValue4(){
		return value4;
	}

	public void setCreatedAt(Object createdAt){
		this.createdAt = createdAt;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public void setValue2(String value2){
		this.value2 = value2;
	}

	public String getValue2(){
		return value2;
	}

	public void setValue3(String value3){
		this.value3 = value3;
	}

	public String getValue3(){
		return value3;
	}

	public void setValue1(String value1){
		this.value1 = value1;
	}

	public String getValue1(){
		return value1;
	}

	public void setSettingId(int settingId){
		this.settingId = settingId;
	}

	public int getSettingId(){
		return settingId;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	public void setGroup(String group){
		this.group = group;
	}

	public String getGroup(){
		return group;
	}

	@Override
 	public String toString(){
		return 
			"AdvDataItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",value_4 = '" + value4 + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",value_2 = '" + value2 + '\'' + 
			",value_3 = '" + value3 + '\'' + 
			",value_1 = '" + value1 + '\'' + 
			",setting_id = '" + settingId + '\'' + 
			",key = '" + key + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}