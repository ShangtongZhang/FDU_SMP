package edu.fudan.msg.pojo;

import java.util.ArrayList;

public class User{
	private int id;
	private String username;
	private String password;
	private String name;
	private String phone;
	private String email;
	private String createdTime;
	private int loginCount;
	private String lastTime;
	private String lastIP;
	private String remark;
	private int isAuthenticated;
	
	public ArrayList<String> phones = new ArrayList<String>();
	public ArrayList<String> emails = new ArrayList<String>();
	
	public void setPhones(ArrayList<String> phones) {
		this.phones = phones;
	}
	public ArrayList<String> getPhones() {
		return phones;
	}
	
	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}
	public ArrayList<String> getEmails() {
		return emails;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatesTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public int getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getLastIP() {
		return lastIP;
	}
	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIsAuthenticated() {
		return isAuthenticated;
	}
	public void setIsAuthenticated(int isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
}

