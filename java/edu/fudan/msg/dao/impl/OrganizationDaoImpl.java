package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import edu.fudan.msg.dao.OrganizationDao;
import edu.fudan.msg.pojo.Organization;

public class OrganizationDaoImpl implements OrganizationDao {

	@Override
	public int addOne(Organization t) {
		SqlSession session = factory.openSession(false);
		try {
			OrganizationDao dao = session.getMapper(OrganizationDao.class);
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
			OrganizationDao dao = session.getMapper(OrganizationDao.class);
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
	public boolean updateOne(Organization t) {
		SqlSession session = factory.openSession(false);
		try {
			OrganizationDao dao = session.getMapper(OrganizationDao.class);
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
	public Organization findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			OrganizationDao dao = session.getMapper(OrganizationDao.class);
			Organization org = dao.findOne(id);
			session.commit(true);
			return org;
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
	public List<Organization> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			OrganizationDao dao = session.getMapper(OrganizationDao.class);
			List<Organization> orgs = dao.findAll();
			session.commit(true);
			return orgs;
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
	public List<Organization> getOrgsByUser(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			OrganizationDao dao = session.getMapper(OrganizationDao.class);
			List<Organization> orgs = dao.getOrgsByUser(uid);
			session.commit(true);
			return orgs;
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
	public int countStu(int oid) {
		SqlSession session = factory.openSession(false);
		try {
			OrganizationDao dao = session.getMapper(OrganizationDao.class);
			int cnt = dao.countStu(oid);
			session.commit(true);
			return cnt;
		} catch (Exception e) {
			// TODO: handle exception
			session.rollback();
			System.out.println(e.toString());
		} finally {
			session.close();
		}
		return -1;
	}

}
