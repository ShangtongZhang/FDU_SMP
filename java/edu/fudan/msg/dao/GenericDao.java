package edu.fudan.msg.dao;


import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import edu.fudan.msg.db.DBManager;


public interface GenericDao <T>{
	public static SqlSessionFactory factory = DBManager.getFactory();
	
	public int addOne(T t);
	public boolean deleteOne(int id);
	public boolean updateOne(T t);
	public T findOne(int id);
	public List<T> findAll();
}
