package edu.fudan.msg.pojo;

public class Comment extends Entry{
	private int id;
	private int uid;
	private int sid;
	private int oid;
	private String topic;
	private String period;
	private String comment;
	private String createdTime;
	private int visibility;
	
	public Comment() {
		
	}
	
	public Comment(int uid, int sid, int oid, String topic, String period, String comment, String createdTime, int visibility) {
		this.uid = uid;
		this.sid = sid;
		this.topic = topic;
		this.period = period;
		this.comment = comment;
		this.createdTime = createdTime;
		this.visibility = visibility;
		this.oid = oid;
	}
	
	@Override
	public String toString() {
		return comment;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int getId() {
		return id;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUid() {
		return uid;
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
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getTopic() {
		return topic;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPeriod() {
		return period;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
	
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	
	public void setVisibility(int visilibity) {
		this.visibility = visilibity;
	}
	public int getVisibility() {
		return visibility;
	}
}
