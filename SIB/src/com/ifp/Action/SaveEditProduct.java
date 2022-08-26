package com.ifp.Action;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class SaveEditProduct extends BaseAction {

	private JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public String saveeditproduct()
	{
		
		String productid= (getRequest().getParameter("productid"));
		String productname= (getRequest().getParameter("productname"));
		String productdesc= (getRequest().getParameter("productdesc"));
		
		//System.out.println("Result is" +productid);
 
		/*String update_query = "UPDATE IFPRODUCT_MASTER SET CARD_TYPE_NAME= '"+productname+"',"
		+ " CARD_TYPE_DESC = '"+productdesc+"' WHERE CARD_TYPE_ID = '"+productid+"'";
		jdbctemplate.update(update_query);
		System.out.println("Result is" +update_query);*/
		
		///by gowtham
		String update_query = "UPDATE IFPRODUCT_MASTER SET CARD_TYPE_NAME= ?,"
				+ " CARD_TYPE_DESC = ? WHERE CARD_TYPE_ID = ?";
				jdbctemplate.update(update_query,new Object[]{productname,productdesc,productid});
				System.out.println("Result is" +update_query);
	
		return "saveeditproduct";
		
	}
	
	
}
