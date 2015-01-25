package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.OrgStuDao;
import edu.fudan.msg.pojo.OrganizationStudent;

public class OrgStuDaoImpl implements OrgStuDao{

	@Override
	public int addOne(OrganizationStudent t) {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
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
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
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
	public boolean updateOne(OrganizationStudent t) {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
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
	public OrganizationStudent findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
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
	public List<OrganizationStudent> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
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
	public List<OrganizationStudent> findByOid(int oid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
			return dao.findByOid(oid);
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
	public List<OrganizationStudent> findBySid(int sid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
			return dao.findBySid(sid);
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
	public boolean deleteBySidOid(int sid, int oid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
			dao.deleteBySidOid(sid, oid);
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
	public OrganizationStudent findBySidOid(int sid, int oid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgStuDao dao = session.getMapper(OrgStuDao.class);
			return dao.findBySidOid(sid, oid);
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
