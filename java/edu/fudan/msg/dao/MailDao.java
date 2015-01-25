package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.Mail;

public interface MailDao extends GenericDao<Mail>{

	List<Mail> getMailsBySender(int uid);
	//List<Mail> getMailsByReceiver(int sid);
	//List<Mail> getMailsBySenderReceiver(int uid, int sid);
	List<Mail> getMailsBySenderStatus(int uid, int status);
	
}
