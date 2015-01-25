package edu.fudan.msg.pojo;

public class UserEmail {
	private int id;
	private int uid;
	private String email;
	
	public UserEmail() {
		
	}
	public UserEmail(int uid, String email) {
		this.uid = uid;
		this.email = email;
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
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
}
