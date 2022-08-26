package com.ifp.Action;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class SaveEditLimitDetails extends BaseAction {
	
 
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
 
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}


	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}


	public String saveeditlimit()
	{
		
		String limitid= (getRequest().getParameter("limitid"));
		String productname= (getRequest().getParameter("productname"));
		String subproductname= (getRequest().getParameter("subproductname"));
		String maxamt= (getRequest().getParameter("maxamt"));
		String txnamt= (getRequest().getParameter("txnamt"));
		String txncnt= (getRequest().getParameter("txncnt"));
		String reloadcnt= (getRequest().getParameter("reloadcnt"));
		
		//System.out.println("Result is" +productid);
		 
		/*String update_query = "UPDATE LIMIT_DETAILS SET MAX_AMT= '"+maxamt+"',"
		+ " DAILY_TXN_AMT = '"+txnamt+"',DAILY_TXN_CNT='"+txncnt+"',RELOAD_COUNT='"+reloadcnt+"' "
		+ "WHERE LIMIT_ID = '"+limitid+"'";
		jdbctemplate.update(update_query);*/
		
		///by gowtham
		String update_query = "UPDATE LIMIT_DETAILS SET MAX_AMT= ?,"
				+ " DAILY_TXN_AMT = ?,DAILY_TXN_CNT=?,RELOAD_COUNT=? "
				+ "WHERE LIMIT_ID = ?";
				jdbctemplate.update(update_query,new Object[]{maxamt,txnamt,txncnt,reloadcnt,limitid});
		
		System.out.println("Result is" +update_query);
		
		return "saveeditlimit";
		
	}

}
