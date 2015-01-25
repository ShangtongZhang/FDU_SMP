package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.Message;

public interface MessageDao extends GenericDao<Message>{

	List<Message> getMsgsBySender(int uid);
	//List<Message> getMsgsByReceiver(int sid);
	//List<Message> getMsgsBySenderReceiver(int uid, int sid);
	List<Message> getMsgsBySenderStatus(int uid, int status);
	
}
