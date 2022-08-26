package com.ifp.beans;

import java.util.List;

public class AuditBeans {
	 
	String cardno;
	String product;
	String subproduct;
	String usercode; 
	String bin;
	String actmsg;
	
	public String mkrchkrstatus;
	public String remarks;
	public String auditactcode;
	public String applicationid;
	public String prefilename;
	public String apptype;
	public String cardnumber;
	public String cin;
	public String checker;
	public String actiontype;
	private String ipAdress;
	
	
	
	
	
	public String getIpAdress() {
		return ipAdress;
	}
	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public String accoutnno;
	public String custname;
	public String getAccoutnno() {
		return accoutnno;
	}
	public void setAccoutnno(String accoutnno) {
		this.accoutnno = accoutnno;
	}
	public String getCardcollectbranch() {
		return cardcollectbranch;
	}
	public void setCardcollectbranch(String cardcollectbranch) {
		this.cardcollectbranch = cardcollectbranch;
	}
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	public String cardcollectbranch;
	
	public String getApptype() {
		return apptype;
	}
	public void setApptype(String apptype) {
		this.apptype = apptype;
	}
	public String getPrefilename() {
		return prefilename;
	}
	public void setPrefilename(String prefilename) {
		this.prefilename = prefilename;
	}
	public String getApplicationid() {
		return applicationid;
	}
	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}
	public String getAuditactcode() {
		return auditactcode;
	}
	public void setAuditactcode(String auditactcode) {
		this.auditactcode = auditactcode;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getMkrchkrstatus() {
		return mkrchkrstatus;
	}
	public void setMkrchkrstatus(String mkrchkrstatus) {
		this.mkrchkrstatus = mkrchkrstatus;
	}
	public String getActmsg() {
		return actmsg;
	}
	public void setActmsg(String actmsg) {
		this.actmsg = actmsg;
	}
	public String getBin() {
		return bin;
	}
	public void setBin(String bin) {
		this.bin = bin;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getSubproduct() {
		return subproduct;
	}
	public void setSubproduct(String subproduct) {
		this.subproduct = subproduct;
	}
	
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber){
		this.cardnumber=cardnumber;
	}
//---------------------------HSM BEANS---------------------
	private List hsmdetails;
	public List getHsmdetails() {
		return hsmdetails;
	}
	public void setHsmdetails(List hsmdetails) {
		this.hsmdetails = hsmdetails;
	}

	private List hsmlist;
	public List getHsmlist() {
		return hsmlist;
	}
	public void setHsmlist(List hsmlist) {
		this.hsmlist = hsmlist;
	}

	private String hsm_idno;
	public String getHsm_idno() {
		return hsm_idno;
	}
	public void setHsm_idno(String hsm_idno) {
		this.hsm_idno = hsm_idno;
	}

	
}
