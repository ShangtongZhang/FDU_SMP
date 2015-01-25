package edu.fudan.msg.pojo;

import java.io.Serializable;

public class Organization implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private int pid;
	private String description;
	private int stuCount = 500;
	
	public Organization(String title, int pid, String description) {
		this.title = title;
		this.pid = pid;
		this.description = description;
	}
	
	public Organization() {
		
	}
	
	@Override
	public String toString() {
		return title + ":" + id;
	}
	
	public void setStuCount(int stuCount) {
		this.stuCount = stuCount;
	}
	public int getStuCount() {
		return stuCount;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPid() {
		return pid;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
}

