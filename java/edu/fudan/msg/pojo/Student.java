package edu.fudan.msg.pojo;

public class Student {
	private int id;
	private String studentNo;
	private String email;
	private String phone;
	private String name;
	private String remark;
	private int oid;
	private String createdTime;
	
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	
	public String getCreatedTime() {
		return createdTime;
	}
	
	public int getOid() {
		return oid;
	}
	
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getStudentNo() {
		return studentNo;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	
	@Override
	public String toString() {
		return name + ":" + id;
	}
}
