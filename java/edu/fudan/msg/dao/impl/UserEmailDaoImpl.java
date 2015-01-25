package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.UserEmailDao;
import edu.fudan.msg.pojo.UserEmail;

public class UserEmailDaoImpl implements UserEmailDao{

	@Override
	public int addOne(UserEmail t) {
		SqlSession session = factory.openSession(false);
		try {
			UserEmailDao dao = session.getMapper(UserEmailDao.class);
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
			UserEmailDao dao = session.getMapper(UserEmailDao.class);
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
	public boolean updateOne(UserEmail t) {
		SqlSession session = factory.openSession(false);
		try {
			UserEmailDao dao = session.getMapper(UserEmailDao.class);
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
	public UserEmail findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			UserEmailDao dao = session.getMapper(UserEmailDao.class);
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
	public List<UserEmail> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			UserEmailDao dao = session.getMapper(UserEmailDao.class);
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
	public List<UserEmail> getUserEmailByUid(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			UserEmailDao dao = session.getMapper(UserEmailDao.class);
			return dao.getUserEmailByUid(uid);
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
			UserEmailDao dao = session.getMapper(UserEmailDao.class);
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
