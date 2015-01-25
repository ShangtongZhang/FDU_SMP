package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.MailDao;
import edu.fudan.msg.pojo.Mail;

public class MailDaoImpl implements MailDao{

	@Override
	public int addOne(Mail t) {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
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
			MailDao dao = session.getMapper(MailDao.class);
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
	public boolean updateOne(Mail t) {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
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
	public Mail findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
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
	public List<Mail> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
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
	public List<Mail> getMailsBySender(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
			return dao.getMailsBySender(uid);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	/*@Override
	public List<Mail> getMailsByReceiver(int sid) {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
			return dao.getMailsByReceiver(sid);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}*/

	/*@Override
	public List<Mail> getMailsBySenderReceiver(int uid, int sid) {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
			return dao.getMailsBySenderReceiver(uid, sid);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}*/

	@Override
	public List<Mail> getMailsBySenderStatus(int uid, int status) {
		SqlSession session = factory.openSession(false);
		try {
			MailDao dao = session.getMapper(MailDao.class);
			return dao.getMailsBySenderStatus(uid, status);
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
