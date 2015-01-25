package edu.fudan.msg.service;

import java.util.List;

import edu.fudan.msg.dao.StudentDao;
import edu.fudan.msg.dao.impl.StudentDaoImpl;
import edu.fudan.msg.pojo.Student;

public class StudentService {
	
	private StudentDao stuDao = new StudentDaoImpl();
	
	public int addStu(Student s) {
		return stuDao.addOne(s);
	}
	
	public boolean deleteStu(int sid) {
		return stuDao.deleteOne(sid);
	}
	
	public boolean updateStu(Student s) {
		return stuDao.updateOne(s);
	}
	
	public Student findStu(int sid) {
		return stuDao.findOne(sid);
	}
	
	public List<Student> findAll() {
		return stuDao.findAll();
	}
	
	public Student getStudentByNo(String studentNo) {
		return stuDao.getStudentByNo(studentNo);
	}
}
