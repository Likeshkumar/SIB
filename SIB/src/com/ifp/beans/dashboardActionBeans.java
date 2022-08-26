package com.ifp.beans;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class dashboardActionBeans {
	List maxsalemerchantlist;
	List maxtxncountlist;
	String blacklistedmerchantcount;
	String compliantcount;
	List txngrptransactionlist;
	List lasttransactionlist;
	List cashinoutlist;
	
	
	public List getCashinoutlist() {
		return cashinoutlist;
	}

	public void setCashinoutlist(List cashinoutlist) {
		this.cashinoutlist = cashinoutlist;
	}

	public List getLasttransactionlist() {
		return lasttransactionlist;
	}

	public void setLasttransactionlist(List lasttransactionlist) {
		this.lasttransactionlist = lasttransactionlist;
	}

	public List getTxngrptransactionlist() {
		return txngrptransactionlist;
	}

	public void setTxngrptransactionlist(List txngrptransactionlist) {
		this.txngrptransactionlist = txngrptransactionlist;
	}

	public String getCompliantcount() {
		return compliantcount;
	}

	public void setCompliantcount(String compliantcount) {
		this.compliantcount = compliantcount;
	}

	public String getBlacklistedmerchantcount() {
		return blacklistedmerchantcount;
	}

	public void setBlacklistedmerchantcount(String blacklistedmerchantcount) {
		this.blacklistedmerchantcount = blacklistedmerchantcount;
	}

	public List getMaxtxncountlist() {
		return maxtxncountlist;
	}

	public void setMaxtxncountlist(List maxtxncountlist) {
		this.maxtxncountlist = maxtxncountlist;
	}

	public List getMaxsalemerchantlist() {
		return maxsalemerchantlist;
	}

	public void setMaxsalemerchantlist(List maxsalemerchantlist) {
		this.maxsalemerchantlist = maxsalemerchantlist;
	}
	
	
}
