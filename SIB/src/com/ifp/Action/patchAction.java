package com.ifp.Action; 

//import com.ifp.beans.BranchBean;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifg.Config.Licence.Licensemanager;
import com.ifp.util.CommonUtil;


public class patchAction extends BaseAction   {
	
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());
	Licensemanager chcbin=new Licensemanager(); 
	static Boolean  initmail = true; 
	static  String parentid = "000";
	CommonUtil comutil= new CommonUtil(); 
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}	
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	 
	private List productlist;	
	public List getProductlist() {
		return productlist;
	}
	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}



} // end class

