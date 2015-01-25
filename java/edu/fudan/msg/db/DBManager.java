package edu.fudan.msg.db;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBManager {
	private static Logger log = LogManager.getLogger(DBManager.class);
	private static SqlSessionFactory factory;
	
	static{
		try {
			Reader reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			factory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			log.error("Exception: ", e);
		}
	}
	
	public static SqlSessionFactory getFactory(){
		return factory;
	}
}
