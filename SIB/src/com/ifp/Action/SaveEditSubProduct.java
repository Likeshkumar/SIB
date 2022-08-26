package com.ifp.Action;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class SaveEditSubProduct extends BaseAction {
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate();

	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}


	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}


	public String saveeditsubproduct() {
		
		String productcode= (getRequest().getParameter("productcode"));
		String productname= (getRequest().getParameter("productname"));
		
		
		System.out.println("Result is" +productcode); 
		
		/*String update_query = "UPDATE IFPRODUCT_SUBTYPE SET SUB_PROD_DESC= '"+productname+"' "
		+ "WHERE SUB_PROD_ID = '"+productcode+"'";
		jdbctemplate.update(update_query);*/
		
		///by gowtham
		String update_query = "UPDATE IFPRODUCT_SUBTYPE SET SUB_PROD_DESC= ? "
				+ "WHERE SUB_PROD_ID = ?";
				jdbctemplate.update(update_query,new Object[]{productname,productcode});
		
		System.out.println("Result is" +update_query);
		
		return "saveeditsubproduct";
		
	}

}
