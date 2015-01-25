package edu.fudan.msg.pojo;

import java.util.ArrayList;

public class Mail {

	private int id;
	private int sender;
	private String receiver;
	private String content;
	private String topic;
	private String sendTime;
	private int status;

	public void setReceivers(ArrayList<Integer> receivers) {
		receiver = "";
		for (Integer id : receivers) {
			receiver += String.valueOf(id) + " ";
		}
	}

	public ArrayList<Integer> getReceivers() {
		ArrayList<Integer> receivers = new ArrayList<Integer>();
		String[] items = receiver.split(" ");
		for (String item : items) {
			receivers.add(Integer.valueOf(item));
		}
		return receivers;
	}

	@Override
	public String toString() {
		return content;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public int getSender() {
		return sender;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
