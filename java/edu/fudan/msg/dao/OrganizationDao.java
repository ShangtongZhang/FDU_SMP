package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.Organization;

public interface OrganizationDao extends GenericDao<Organization>{
	List<Organization> getOrgsByUser(int uid);
	public int countStu(int oid);
}
