package com.ifp.beans;

import java.util.List;

public class RevenueSharingBean {

	List binlist;
	List commissionexpenselist;
	String commissiontypedesc;
	String bincode;
	String bindesc;
	String commissiontype;
	String commissionfor;	
	String commissionfordesc;	
	String currency;
	
	
	
	 
	public String getBincode() {
		return bincode;
	}

	public void setBincode(String bincode) {
		this.bincode = bincode;
	}

	public String getCommissionfor() {
		return commissionfor;
	}

	public void setCommissionfor(String commissionfor) {
		this.commissionfor = commissionfor;
	}

	public String getCommissiontype() {
		return commissiontype;
	}

	public void setCommissiontype(String commissiontype) {
		this.commissiontype = commissiontype;
	}

	public String getBindesc() {
		return bindesc;
	}

	public void setBindesc(String bindesc) {
		this.bindesc = bindesc;
	} 

	public String getCommissiontypedesc() {
		return commissiontypedesc;
	}

	public void setCommissiontypedesc(String commissiontypedesc) {
		this.commissiontypedesc = commissiontypedesc;
	}

	public String getCommissionfordesc() {
		return commissionfordesc;
	}

	public void setCommissionfordesc(String commissionfordesc) {
		this.commissionfordesc = commissionfordesc;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
 
	public List getCommissionexpenselist() {
		return commissionexpenselist;
	}

	public void setCommissionexpenselist(List commissionexpenselist) {
		this.commissionexpenselist = commissionexpenselist;
	}

	public List getBinlist() {
		return binlist;
	}

	public void setBinlist(List binlist) {
		this.binlist = binlist;
	}
	
}
