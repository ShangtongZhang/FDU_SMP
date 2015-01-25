package edu.fudan.msg.service;
import java.util.List;

import edu.fudan.msg.dao.MailDao;
import edu.fudan.msg.dao.impl.MailDaoImpl;
import edu.fudan.msg.pojo.Mail;

public class MailService {
	
	private MailDao mailDao = new MailDaoImpl();
	
	public int addMail(Mail m) {
		return mailDao.addOne(m);
	}
	
	public boolean deleteMail(int id) {
		return mailDao.deleteOne(id);
	}
	
	public boolean updateMail(Mail m) {
		return mailDao.updateOne(m);
	}
	
	public Mail getMail(int id) {
		return mailDao.findOne(id);
	}
	
	public List<Mail> getAllMails() {
		return mailDao.findAll();
	}
	
	public List<Mail> getMailsBySender(int uid) {
		return mailDao.getMailsBySender(uid);
	}
	
	/*public List<Mail> getMailsByReceiver(int sid) {
		return mailDao.getMailsByReceiver(sid);
	}
	
	public List<Mail> getMailsBySenderReceiver(int uid, int sid) {
		return mailDao.getMailsBySenderReceiver(uid, sid);
	}*/
	
	public List<Mail> getMailsBySenderStatus(int uid, int status) {
		return mailDao.getMailsBySenderStatus(uid, status);
	}
}
