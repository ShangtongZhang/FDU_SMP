package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.StudentDao;
import edu.fudan.msg.pojo.Student;

public class StudentDaoImpl implements StudentDao{

	@Override
	public int addOne(Student t) {
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
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
			StudentDao dao = session.getMapper(StudentDao.class);
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
	public boolean updateOne(Student t) {
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
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
	public Student findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
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
	public List<Student> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
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
	public List<Student> getStudentsByOrg(int oid, int start, int length) {
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
			return dao.getStudentsByOrg(oid, start, length);
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
	public Student getStudentByNo(String studentNo) {
		// TODO Auto-generated method stub
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
			return dao.getStudentByNo(studentNo);
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
	public List<Student> searchStudentsByName(int oid, String namePattern) {
		// TODO Auto-generated method stub
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
			return dao.searchStudentsByName(oid, namePattern);
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
	public List<Student> searchStudentsByNo(int oid, String noPattern) {
		// TODO Auto-generated method stub
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
			return dao.searchStudentsByNo(oid, noPattern);
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
	public int countStudentsByOrg(int oid) {
		// TODO Auto-generated method stub
		SqlSession session = factory.openSession(false);
		try {
			StudentDao dao = session.getMapper(StudentDao.class);
			return dao.countStudentsByOrg(oid);
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return 0;
	}

}
