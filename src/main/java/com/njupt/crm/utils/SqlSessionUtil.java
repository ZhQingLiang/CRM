package com.njupt.crm.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionUtil {
//	构造方法私有化
	private SqlSessionUtil(){}
	
	private static SqlSessionFactory factory;
	
	static{

//		注意！！！！！！！！！！！
//		java.lang.NoClassDefFoundError: Could not initialize class xxx
//		"mybatis-config.xml"
		String resource = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		factory =
		 new SqlSessionFactoryBuilder().build(inputStream);
		
	}
	
	private static ThreadLocal<SqlSession> t = new ThreadLocal<SqlSession>();
	
	public static SqlSession getSqlSession(){

		SqlSession session = t.get();
		
		if(session==null){
			
			session = factory.openSession();
			t.set(session);
		}
		
		return session;
		
	}
	
	public static void myClose(SqlSession session){
		
		if(session!=null){
			session.close();
			t.remove();
		}
		
	}
	
	
}
