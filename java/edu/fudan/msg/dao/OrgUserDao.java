package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.OrganizationUser;

public interface OrgUserDao extends GenericDao<OrganizationUser>{
	public List<OrganizationUser> findByOid(int oid);
	public List<OrganizationUser> findByUid(int uid);
	public boolean deleteByOidUid(int oid, int uid);
	public boolean deleteByUid(int uid);
	public OrganizationUser findByOidUid(int oid, int uid);
}
