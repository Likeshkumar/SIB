package com.ifp.beans;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class PinGenerationBeans extends BaseAction{
	List prodlist;
	List branchlist;
	
	 
	public List getProdlist() {
		return prodlist;
	}
	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}
	public List getBranchlist() {
		return branchlist;
	}
	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}
	 
	
}//end class
