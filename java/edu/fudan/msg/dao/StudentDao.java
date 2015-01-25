package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.Student;

public interface StudentDao extends GenericDao<Student> {

	//public List<Student> getStudentsByOrg(int oid);
	
	public List<Student> getStudentsByOrg(int oid, int start, int offset);
	
	public Student getStudentByNo(String studentNo);
	
	public List<Student> searchStudentsByName(int oid, String namePattern);
	
	public List<Student> searchStudentsByNo(int oid, String noPattern);
	
	public int countStudentsByOrg(int oid);
	
}
