package com.ifp.beans;

import java.util.List;

public class CallCenterBeans {
	String limitid;
	String limitdesc;
	List limitdetails;
	String transactiondata;
	List transactionlist;
	String availbal;
	String customertype;
	String webusername;
	String vasotp;
	String vasotpstatus;
	String vasotpgendate;
	String txndate;
	
	
	
	public String getTxndate() {
		return txndate;
	}

	public void setTxndate(String txndate) {
		this.txndate = txndate;
	}

	public String getVasotpstatus() {
		return vasotpstatus;
	}

	public void setVasotpstatus(String vasotpstatus) {
		this.vasotpstatus = vasotpstatus;
	}

	public String getVasotpgendate() {
		return vasotpgendate;
	}

	public void setVasotpgendate(String vasotpgendate) {
		this.vasotpgendate = vasotpgendate;
	}

	public String getVasotp() {
		return vasotp;
	}

	public void setVasotp(String vasotp) {
		this.vasotp = vasotp;
	}

	public String getWebusername() {
		return webusername;
	}

	public void setWebusername(String webusername) {
		this.webusername = webusername;
	}

	public String getCustomertype() {
		return customertype;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}

	public String getAvailbal() {
		return availbal;
	}

	public void setAvailbal(String availbal) {
		this.availbal = availbal;
	}

	public String getTransactiondata() {
		return transactiondata;
	}

	public void setTransactiondata(String transactiondata) {
		this.transactiondata = transactiondata;
	}

	public List getTransactionlist() {
		return transactionlist;
	}

	public void setTransactionlist(List transactionlist) {
		this.transactionlist = transactionlist;
	}
	 
	public List getLimitdetails() {
		return limitdetails;
	}
	public void setLimitdetails(List limitdetails) {
		this.limitdetails = limitdetails;
	}
	public String getLimitid() {
		return limitid;
	}
	public void setLimitid(String limitid) {
		this.limitid = limitid;
	}
	public String getLimitdesc() {
		return limitdesc;
	}
	public void setLimitdesc(String limitdesc) {
		this.limitdesc = limitdesc;
	}
	
	
}
