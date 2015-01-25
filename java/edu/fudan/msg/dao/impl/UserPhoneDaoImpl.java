package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.UserPhoneDao;
import edu.fudan.msg.pojo.UserPhone;

public class UserPhoneDaoImpl implements UserPhoneDao{

	@Override
	public int addOne(UserPhone t) {
		SqlSession session = factory.openSession(false);
		try {
			UserPhoneDao dao = session.getMapper(UserPhoneDao.class);
			int id = dao.addOne(t);
			session.commit(true);
			return id;
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return -1;
	}

	@Override
	public boolean deleteOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			UserPhoneDao dao = session.getMapper(UserPhoneDao.class);
			dao.deleteOne(id);
			session.commit(true);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public boolean updateOne(UserPhone t) {
		SqlSession session = factory.openSession(false);
		try {
			UserPhoneDao dao = session.getMapper(UserPhoneDao.class);
			dao.updateOne(t);
			session.commit(true);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public UserPhone findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			UserPhoneDao dao = session.getMapper(UserPhoneDao.class);
			return dao.findOne(id);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<UserPhone> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			UserPhoneDao dao = session.getMapper(UserPhoneDao.class);
			return dao.findAll();
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<UserPhone> getUserPhoneByUid(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			UserPhoneDao dao = session.getMapper(UserPhoneDao.class);
			return dao.getUserPhoneByUid(uid);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean deleteByUid(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			UserPhoneDao dao = session.getMapper(UserPhoneDao.class);
			dao.deleteByUid(uid);
			session.commit(true);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return false;
	}

}
