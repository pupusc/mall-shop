package com.wanmi.sbc.goods.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;


/**
 * @author chenzhen
 */
@Service
public class MarkingTemplate {

	JdbcTemplate jdbcTemplate;

	private static MarkingTemplate instance = new MarkingTemplate();

	public static MarkingTemplate getInstance() {
		return instance;
	}

	public void init(DataSource dataSource){
		System.out.println("init,markingJdbcTemplate");
		instance.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	private MarkingTemplate() {
		super();
	}


	public int update(String arg0,Object[] arg1){
		return getJdbcTemplate().update(arg0,arg1);
	}

	public int update_throws(String arg0,Object[] arg1){

		return getJdbcTemplate().update(arg0,arg1);

	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}