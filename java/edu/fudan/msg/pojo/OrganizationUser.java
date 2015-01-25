package edu.fudan.msg.pojo;

public class OrganizationUser {
	private int id;
	private int uid;
	private int oid;
	private String createTime;
	
	public OrganizationUser(int uid, int oid, String createTime) {
		this.uid = uid;
		this.oid = oid;
		this.createTime = createTime;
	}
	
	public OrganizationUser() {
		
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
	
	public void setOid(int oid) {
		this.oid = oid;
	}
	public int getOid() {
		return oid;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateTime() {
		return createTime;
	}
}
