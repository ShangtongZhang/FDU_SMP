package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.UserEmail;

public interface UserEmailDao extends GenericDao<UserEmail>{

	List<UserEmail> getUserEmailByUid(int uid);
	boolean deleteByUid(int uid);
}
