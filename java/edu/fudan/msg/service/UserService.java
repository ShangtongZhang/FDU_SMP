package edu.fudan.msg.service;

import java.util.ArrayList;
import java.util.List;

import edu.fudan.msg.dao.OrgUserDao;
import edu.fudan.msg.dao.UserDao;
import edu.fudan.msg.dao.UserEmailDao;
import edu.fudan.msg.dao.UserPhoneDao;
import edu.fudan.msg.dao.impl.OrgUserDaoImpl;
import edu.fudan.msg.dao.impl.UserDaoImpl;
import edu.fudan.msg.dao.impl.UserEmailDaoImpl;
import edu.fudan.msg.dao.impl.UserPhoneDaoImpl;
import edu.fudan.msg.pojo.OrganizationUser;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.pojo.UserEmail;
import edu.fudan.msg.pojo.UserPhone;

public class UserService {

	private UserDao userDao = new UserDaoImpl();
	private UserPhoneDao phoneDao = new UserPhoneDaoImpl();
	private UserEmailDao emailDao = new UserEmailDaoImpl();
	private OrgUserDao orgUserDao = new OrgUserDaoImpl();
	
	//获取一个org下所有的user，现在好像用不到，
	//但我猜将来管理员那个面板是会改的，现在的任何一个管理员的面板都会显示出所有user，明显是不科学的。
	//将来应该会有一些各种层级的管理员
	public List<User> getByOid(int oid) {
		List<OrganizationUser> ous = orgUserDao.findByOid(oid);
		List<User> us = new ArrayList<User>();
		for (OrganizationUser ou : ous) {
			us.add(findUser(ou.getUid()));
		}
		return us;
	}
	
	public int addUser(User u) {
		int uid = userDao.addOne(u);
		if (uid < 0) {
			return -1;
		}
		
		for (String phone : u.getPhones()) {
			int id = phoneDao.addOne(new UserPhone(uid, phone));
			System.out.println(id);
		}
		
		for (String email : u.getEmails()) {
			emailDao.addOne(new UserEmail(uid, email));
		}
		return uid;
	}
	
	public boolean deleteUser(int uid) {
		userDao.deleteOne(uid);
		phoneDao.deleteByUid(uid);
		emailDao.deleteByUid(uid);
		return true;
	}
	
	public boolean updateUser(User u) {
		userDao.updateOne(u);
		
		phoneDao.deleteByUid(u.getId());
		emailDao.deleteByUid(u.getId());
		
		for (String phone : u.getPhones()) {
			phoneDao.addOne(new UserPhone(u.getId(), phone));
		}
		
		for (String email : u.getEmails()) {
			emailDao.addOne(new UserEmail(u.getId(), email));
		}
		
		return true;
	}
	
	public User findUser(int uid) {
		User u = userDao.findOne(uid);
		
		for (UserEmail ue : emailDao.getUserEmailByUid(uid)) {
			u.emails.add(ue.getEmail());
		}
		
		for (UserPhone up : phoneDao.getUserPhoneByUid(uid)) {
			u.phones.add(up.getPhone());
		}
		return u;
	}
	
	public List<User> findAllUsers() {
		ArrayList<User> allUsers = new ArrayList<User>();
		for (User u : userDao.findAll()) {
			allUsers.add(findUser(u.getId()));
		}
		return allUsers;
	}
	
	//返回一个用户名在数据库中已经出现的次数，因为用户名是唯一的，正常情况下只会返回0或者1
	//用于注册时检验一个用户名是否合法
	public int countUsername(String username) {
		return userDao.countUsername(username);
	}
	
	//通过username取回一个User
	public User getUserByUsername(String username) {
		return userDao.getUserByUsername(username);
	}
	
	public int addUserEmail(UserEmail e) {
		return emailDao.addOne(e);
	}
	
	public boolean deleteUserEmail(int id) {
		return emailDao.deleteOne(id);
	}
	
	public boolean updateUserEmail(UserEmail e) {
		return emailDao.updateOne(e);
	}
	
	public UserEmail getUserEmail(int id) {
		return emailDao.findOne(id);
	}
	
	public List<UserEmail> getAllUserEmails() {
		return emailDao.findAll();
	}
	
	public List<UserEmail> getUserEmailsByUid(int uid) {
		return emailDao.getUserEmailByUid(uid);
	}
	
	public int addUserPhone(UserPhone e) {
		return phoneDao.addOne(e);
	}
	
	public boolean deleteUserPhone(int id) {
		return phoneDao.deleteOne(id);
	}
	
	public boolean updateUserPhone(UserPhone e) {
		return phoneDao.updateOne(e);
	}
	
	public UserPhone getUserPhone(int id) {
		return phoneDao.findOne(id);
	}
	
	public List<UserPhone> getAllUserPhones() {
		return phoneDao.findAll();
	}
	
	public List<UserPhone> getUserPhonesByUid(int uid) {
		return phoneDao.getUserPhoneByUid(uid);
	}
}
