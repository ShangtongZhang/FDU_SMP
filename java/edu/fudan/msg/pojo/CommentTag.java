package edu.fudan.msg.pojo;

public class CommentTag {

	private int id;
	private int tid;
	private int cid;
	
	public CommentTag() {
		
	}
	
	public CommentTag(int cid, int tid) {
		this.cid = cid;
		this.tid = tid;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId(){
		return id;
	}
	
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getTid() {
		return tid;
	}
	
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getCid(){
		return cid;
	}
}
