package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.TagDao;
import edu.fudan.msg.pojo.Tag;

public class TagDaoImpl implements TagDao{

	@Override
	public int addOne(Tag t) {
		SqlSession session = factory.openSession(false);
		try {
			TagDao dao = session.getMapper(TagDao.class);
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
			TagDao dao = session.getMapper(TagDao.class);
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
	public boolean updateOne(Tag t) {
		SqlSession session = factory.openSession(false);
		try {
			TagDao dao = session.getMapper(TagDao.class);
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
	public Tag findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			TagDao dao = session.getMapper(TagDao.class);
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
	public List<Tag> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			TagDao dao = session.getMapper(TagDao.class);
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
	public List<Tag> getTagsByComment(int cid) {
		SqlSession session = factory.openSession(false);
		try {
			TagDao dao = session.getMapper(TagDao.class);
			return dao.getTagsByComment(cid);
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
