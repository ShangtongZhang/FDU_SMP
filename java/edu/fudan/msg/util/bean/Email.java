package edu.fudan.msg.util.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.fudan.msg.util.EmailUtil;


/**
 * 邮件信息实体类
 * @author tom
 * @since  2014-7-18
 */
public class Email {
	
	/**
	 * 发送人 
	 */
	private String from;
	
	/**
	 * 收件人，以逗号分隔多人 
	 */
	private String to;
	
	/**
	 * 抄送人，以逗号分隔多人 
	 */
	private String copyTo;
	
	/**
	 * 密送人，以逗号分隔多人 
	 */
	private String secretlyCopyTo;
	
	private String title;
	private String content;
	private Set<String> files = new HashSet<String>(0);
	private Date sendDate;
	
	
	public Email() {
		this.sendDate = new Date();
		this.from = EmailUtil.username;
	}
	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCopyTo() {
		return copyTo;
	}
	public void setCopyTo(String copyTo) {
		this.copyTo = copyTo;
	}
	public String getSecretlyCopyTo() {
		return secretlyCopyTo;
	}
	public void setSecretlyCopyTo(String secretlyCopyTo) {
		this.secretlyCopyTo = secretlyCopyTo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Set<String> getFiles() {
		return files;
	}
	public void setFiles(Set<String> files) {
		this.files = files;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
}
