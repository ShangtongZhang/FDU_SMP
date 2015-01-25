package edu.fudan.msg.constant;

/**
 * all kinds of user role
 * */
public enum UserRole {
	SUPER_MANAGER("学校管理员"),
	SECONDARY_MANAGER("院系管理员"),
	LOWEST_MANAGER("辅导员");
	
	private String title;
	private UserRole(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
}
