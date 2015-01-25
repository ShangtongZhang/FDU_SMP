package edu.fudan.msg.pojo;

public class OrganizationStudent {
	private int id;
	private int sid;
	private int oid;
	private String createdTime;
	
	public OrganizationStudent() {
		
	}
	
	public OrganizationStudent(int sid, int oid, String createdTime) {
		this.sid = sid;
		this.oid = oid;
		this.createdTime = createdTime;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getSid() {
		return sid;
	}
	
	public void setOid(int oid) {
		this.oid = oid;
	}
	public int getOid() {
		return oid;
	}
	
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getCreatedTime() {
		return createdTime;
	}

}
