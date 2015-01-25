package edu.fudan.msg.dao;

import edu.fudan.msg.pojo.User;

public interface UserDao extends GenericDao<User>{
	
	int countUsername(String username);
	User getUserByUsername(String username);
	
}
