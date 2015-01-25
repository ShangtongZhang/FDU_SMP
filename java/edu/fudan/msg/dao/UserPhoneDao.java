package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.UserPhone;

public interface UserPhoneDao extends GenericDao<UserPhone>{

	List<UserPhone> getUserPhoneByUid(int uid);
	boolean deleteByUid(int uid);
}
