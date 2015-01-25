package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.MessageDao;
import edu.fudan.msg.pojo.Message;

public class MessageDaoImpl implements MessageDao{

	@Override
	public int addOne(Message t) {
		SqlSession session = factory.openSession(false);
		
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
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
			MessageDao dao = session.getMapper(MessageDao.class);
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
	public boolean updateOne(Message t) {
		SqlSession session = factory.openSession(false);
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
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
	public Message findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
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
	public List<Message> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
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
	public List<Message> getMsgsBySender(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
			return dao.getMsgsBySender(uid);
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
	public List<Message> getMsgsByReceiver(int sid) {
		SqlSession session = factory.openSession(false);
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
			return dao.getMsgsByReceiver(sid);
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
	public List<Message> getMsgsBySenderReceiver(int uid, int sid) {
		SqlSession session = factory.openSession(false);
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
			return dao.getMsgsBySenderReceiver(uid, sid);
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
	public List<Message> getMsgsBySenderStatus(int uid, int status) {
		SqlSession session = factory.openSession(false);
		try {
			MessageDao dao = session.getMapper(MessageDao.class);
			return dao.getMsgsBySenderStatus(uid, status);
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
