package edu.fudan.msg.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.fudan.msg.dao.OrgUserDao;
import edu.fudan.msg.pojo.OrganizationUser;

public class OrgUserDaoImpl implements OrgUserDao{

	@Override
	public int addOne(OrganizationUser t) {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
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
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
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
	public boolean updateOne(OrganizationUser t) {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
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
	public OrganizationUser findOne(int id) {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
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
	public List<OrganizationUser> findAll() {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
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
	public List<OrganizationUser> findByOid(int oid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
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
	public List<OrganizationUser> findByUid(int uid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
			return dao.findByUid(uid);
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
	public boolean deleteByOidUid(int oid, int uid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
			dao.deleteByOidUid(oid, uid);
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
	public OrganizationUser findByOidUid(int oid, int uid) {
		SqlSession session = factory.openSession(false);
		try {
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
			return dao.findByOidUid(oid, uid);
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
			OrgUserDao dao = session.getMapper(OrgUserDao.class);
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
