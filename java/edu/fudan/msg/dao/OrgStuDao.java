package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.OrganizationStudent;

public interface OrgStuDao extends GenericDao<OrganizationStudent>{
	public List<OrganizationStudent> findByOid(int oid);
	public List<OrganizationStudent> findBySid(int sid);
	public boolean deleteBySidOid(int sid, int oid);
	public OrganizationStudent findBySidOid(int sid, int oid);
}
