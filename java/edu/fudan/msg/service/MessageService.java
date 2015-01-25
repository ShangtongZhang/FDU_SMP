package edu.fudan.msg.service;

import java.util.List;

import edu.fudan.msg.dao.MessageDao;
import edu.fudan.msg.dao.impl.MessageDaoImpl;
import edu.fudan.msg.pojo.Message;

public class MessageService {
	
	private MessageDao msgDao;
	
	public MessageService() {
		try {
			msgDao = new MessageDaoImpl();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
	
	public int addMsg(Message m) {
		return msgDao.addOne(m);
	}
	
	public boolean deleteMsg(int id) {
		return msgDao.deleteOne(id);
	}
	
	public boolean updateMsg(Message m) {
		return msgDao.updateOne(m);
	}
	
	public Message getMsg(int id) {
		return msgDao.findOne(id);
	}
	
	public List<Message> getAllMsgs() {
		return msgDao.findAll();
	}
	
	public List<Message> getMsgsBySender(int uid) {
		return msgDao.getMsgsBySender(uid);
	}
	
	public List<Message> getMsgsBySenderStatus(int uid, int status) {
		return msgDao.getMsgsBySenderStatus(uid, status);
	}
}
