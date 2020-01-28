package com.saaty.models;


import com.google.gson.annotations.SerializedName;


public class RebliesArrayItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_message_id")
	private int userMessageId;

	@SerializedName("read_at")
	private Object readAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("usmr_id")
	private int usmrId;

	@SerializedName("reply")
	private String reply;

	@SerializedName("sender_user_id")
	private int senderUserId;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserMessageId(int userMessageId){
		this.userMessageId = userMessageId;
	}

	public int getUserMessageId(){
		return userMessageId;
	}

	public void setReadAt(Object readAt){
		this.readAt = readAt;
	}

	public Object getReadAt(){
		return readAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setUsmrId(int usmrId){
		this.usmrId = usmrId;
	}

	public int getUsmrId(){
		return usmrId;
	}

	public void setReply(String reply){
		this.reply = reply;
	}

	public String getReply(){
		return reply;
	}

	public void setSenderUserId(int senderUserId){
		this.senderUserId = senderUserId;
	}

	public int getSenderUserId(){
		return senderUserId;
	}

	@Override
 	public String toString(){
		return 
			"RebliesArrayItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",user_message_id = '" + userMessageId + '\'' + 
			",read_at = '" + readAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",usmr_id = '" + usmrId + '\'' + 
			",reply = '" + reply + '\'' + 
			",sender_user_id = '" + senderUserId + '\'' + 
			"}";
		}
}