package com.ifp.Action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

//import com.ifp.beans.loginbean;
//import com.ifp.beans.menubean;

public class makercheckerAction {
	
	Logger logger = Logger.getLogger(this.getClass());
//	loginbean logbean=new loginbean();
//	public menubean mbean=new menubean();
	private static final long serialVersionUID = 1L;
	HttpServletRequest request ; 
		
	
	public String getMenuslist()
	{
		
		
		return "enablemakerchecker";
	}
	
	


}
