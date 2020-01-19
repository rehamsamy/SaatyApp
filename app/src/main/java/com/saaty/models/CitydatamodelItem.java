package com.saaty.models;

import com.google.gson.annotations.SerializedName;

public class CitydatamodelItem{

	@SerializedName("city_name_ar")
	private String cityNameAr;

	@SerializedName("city_name_en")
	private String cityNameEn;

	@SerializedName("city_id")
	private int cityId;

	public void setCityNameAr(String cityNameAr){
		this.cityNameAr = cityNameAr;
	}

	public String getCityNameAr(){
		return cityNameAr;
	}

	public void setCityNameEn(String cityNameEn){
		this.cityNameEn = cityNameEn;
	}

	public String getCityNameEn(){
		return cityNameEn;
	}

	public void setCityId(int cityId){
		this.cityId = cityId;
	}

	public int getCityId(){
		return cityId;
	}

	@Override
 	public String toString(){
		return 
			"CitydatamodelItem{" + 
			"city_name_ar = '" + cityNameAr + '\'' + 
			",city_name_en = '" + cityNameEn + '\'' + 
			",city_id = '" + cityId + '\'' + 
			"}";
		}
}