package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.UserDao;
import edu.fudan.msg.pojo.User;

public class UserDaoImpl implements UserDao{
	@Override
	public int addOne(User t) {
		SqlSession session = factory.openSession(false);
		try {
			UserDao dao = session.getMapper(UserDao.class);
			dao.addOne(t);
			session.commit(true);
			return t.getId();
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
			UserDao dao = session.getMapper(UserDao.class);
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
	public boolean updateOne(User t) {
		SqlSession session = factory.openSession(false);
		try {
			UserDao dao = session.getMapper(UserDao.class);
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
	public User findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			UserDao dao = session.getMapper(UserDao.class);
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
	public List<User> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			UserDao dao = session.getMapper(UserDao.class);
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
	public int countUsername(String username) {
		SqlSession session = factory.openSession(false);
		try {
			UserDao dao = session.getMapper(UserDao.class);
			return dao.countUsername(username);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return 0;
	}

	@Override
	public User getUserByUsername(String username) {
		SqlSession session = factory.openSession(false);
		try {
			UserDao dao = session.getMapper(UserDao.class);
			return dao.getUserByUsername(username);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

}
