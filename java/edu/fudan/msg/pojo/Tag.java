package edu.fudan.msg.pojo;

public class Tag {
	private int id;
	private String tagName;
	private int creator;
	
	public Tag() {
		
	}
	
	public Tag(String tagName, int creator) {
		this.tagName = tagName;
		this.creator = creator;
	}
	
	@Override
	public String toString() {
		return tagName;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagName() {
		return tagName;
	}
	
	public void setCreater(int creator) {
		this.creator = creator;
	}
	public int getCreater() {
		return creator;
	}

}
