package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.CommentTagDao;
import edu.fudan.msg.pojo.CommentTag;

public class CommentTagDaoImpl implements CommentTagDao{

	@Override
	public int addOne(CommentTag t) {
		SqlSession session = factory.openSession(false);
		try {
			CommentTagDao dao = session.getMapper(CommentTagDao.class);
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
			CommentTagDao dao = session.getMapper(CommentTagDao.class);
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
	public boolean updateOne(CommentTag t) {
		SqlSession session = factory.openSession(false);
		try {
			CommentTagDao dao = session.getMapper(CommentTagDao.class);
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
	public CommentTag findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			CommentTagDao dao = session.getMapper(CommentTagDao.class);
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
	public List<CommentTag> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			CommentTagDao dao = session.getMapper(CommentTagDao.class);
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

}
