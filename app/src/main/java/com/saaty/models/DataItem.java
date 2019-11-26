package com.saaty.models;


import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("store_id")
	private int storeId;

	@SerializedName("store_ar_name")
	private String storeArName;

	@SerializedName("store_en_description")
	private String storeEnDescription;

	@SerializedName("store_en_name")
	private String storeEnName;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("store_ar_description")
	private String storeArDescription;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("category_ar_name")
	private String categoryArName;

	@SerializedName("category_en_name")
	private String cateogryEnName;

	@SerializedName("category_icon_link")
	private  String categoryIconLink;

	public String getCategoryId() {
		return categoryId;
	}

	public String getCategoryArName() {
		return categoryArName;
	}

	public String getCateogryEnName() {
		return cateogryEnName;
	}

	public String getCategoryIconLink() {
		return categoryIconLink;
	}

	public void setStoreId(int storeId){
		this.storeId = storeId;
	}

	public int getStoreId(){
		return storeId;
	}

	public void setStoreArName(String storeArName){
		this.storeArName = storeArName;
	}

	public String getStoreArName(){
		return storeArName;
	}

	public void setStoreEnDescription(String storeEnDescription){
		this.storeEnDescription = storeEnDescription;
	}

	public String getStoreEnDescription(){
		return storeEnDescription;
	}

	public void setStoreEnName(String storeEnName){
		this.storeEnName = storeEnName;
	}

	public String getStoreEnName(){
		return storeEnName;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setStoreArDescription(String storeArDescription){
		this.storeArDescription = storeArDescription;
	}

	public String getStoreArDescription(){
		return storeArDescription;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"store_id = '" + storeId + '\'' + 
			",store_ar_name = '" + storeArName + '\'' + 
			",store_en_description = '" + storeEnDescription + '\'' + 
			",store_en_name = '" + storeEnName + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",store_ar_description = '" + storeArDescription + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			"}";
		}
}