package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.CommentDao;
import edu.fudan.msg.pojo.Comment;

public class CommentDaoImpl implements CommentDao{

	@Override
	public int addOne(Comment c) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			dao.addOne(c);
			session.commit(true);
			return c.getId();
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
			CommentDao dao = session.getMapper(CommentDao.class);
			dao.deleteOne(id);
			session.commit(true);
			return true;
		} catch (Exception e) {
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public boolean updateOne(Comment c) {
		// TODO Auto-generated method stub
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			dao.updateOne(c);
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
	public Comment findOne(int id) {
		// TODO Auto-generated method stub
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findOne(id);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findAll() {
		// TODO Auto-generated method stub
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findAll();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsByTagUser(int tid, int uid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsByTagUser(tid, uid);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsByStudentUser(int sid, int uid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsByStudentUser(sid, uid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsByUser(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsByUser(uid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsByStudentUserTag(int sid, int uid, int tid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsByStudentUserTag(sid, uid, tid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public boolean deleteBySidUid(int sid, int uid) {

		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			dao.deleteBySidUid(sid, uid);
			session.commit(true);
			return true;
		} catch (Exception e) {
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public List<Comment> findCommentsBySidTid(int sid, int tid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsBySidTid(sid, tid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsBySid(int sid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsBySid(sid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> searchCommentsByContentSid(int sid, String contPattern) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.searchCommentsByContentSid(sid, contPattern);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsByTidOid(int tid, int oid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsByTidOid(tid, oid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsBySidOid(int sid, int oid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsBySidOid(sid, oid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> searchCommentsByContentOid(String contPattern, int oid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.searchCommentsByContentOid(contPattern, oid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsByStuAndOrgsOfUser(int sid, int uid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsByStuAndOrgsOfUser(sid, uid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> findCommentsByTagAndOrgsOfUser(int tid, int uid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.findCommentsByTagAndOrgsOfUser(tid, uid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Comment> searchCommentsByContentAndOrgsOfUser(
			String contPattern, int uid) {
		SqlSession session = factory.openSession(false);
		try {
			CommentDao dao = session.getMapper(CommentDao.class);
			return dao.searchCommentsByContentAndOrgsOfUser(contPattern, uid);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return null;
	}
	
	
}
