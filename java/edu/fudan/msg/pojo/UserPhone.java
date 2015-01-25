package edu.fudan.msg.pojo;

public class UserPhone {
	private int id;
	private int uid;
	private String phone;
	
	public UserPhone() {
		
	}
	public UserPhone(int uid, String phone) {
		this.uid = uid;
		this.phone = phone;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUid() {
		return uid;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
	}
}
