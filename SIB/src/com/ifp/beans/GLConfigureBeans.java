package com.ifp.beans;

import java.util.List;

public class GLConfigureBeans {
	List glgroup;
	String noglgroup;
	String glgrpcode; 
	Boolean flag; 
	List instcurrencylist;
	Boolean hodisable = false;
	Boolean feedisable = false;
	String txnactiondesc; 
	List mappedscheme;
	Boolean headerreq = false;
	String txnactioncode;
	List schemelist;
	String schemecdoe;
	String schemedesc;
	private List lst_gldetails;
	public List glkeys;
	List childschemelist;
	List actionlist ;
	List glkeyslist;
	List accoutrulelist;
	
	
	public List getAccoutrulelist() {
		return accoutrulelist;
	}

	public void setAccoutrulelist(List accoutrulelist) {
		this.accoutrulelist = accoutrulelist;
	}

	public List getGlkeyslist() {
		return glkeyslist;
	}

	public void setGlkeyslist(List glkeyslist) {
		this.glkeyslist = glkeyslist;
	}

	public List getActionlist() {
		return actionlist;
	}

	public void setActionlist(List actionlist) {
		this.actionlist = actionlist;
	}
	
	
	public List getChildschemelist() {
		return childschemelist;
	}
	public void setChildschemelist(List childschemelist) {
		this.childschemelist = childschemelist;
	}
	public List getGlkeys() {
		return glkeys;
	}
	public void setGlkeys(List glkeys) {
		this.glkeys = glkeys;
	}
	public List getLst_gldetails() {
		return lst_gldetails;
	} 
	public void setLst_gldetails(List lst_gldetails) {
		this.lst_gldetails = lst_gldetails;
	}
	
	public List getGlgroup() {
		return glgroup;
	}
	public void setGlgroup(List glgroup) {
		this.glgroup = glgroup;
	}
	public String getNoglgroup() {
		return noglgroup;
	}
	public void setNoglgroup(String noglgroup) {
		this.noglgroup = noglgroup;
	}
	public String getGlgrpcode() {
		return glgrpcode;
	}
	public void setGlgrpcode(String glgrpcode) {
		this.glgrpcode = glgrpcode;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public List getInstcurrencylist() {
		return instcurrencylist;
	}
	public void setInstcurrencylist(List instcurrencylist) {
		this.instcurrencylist = instcurrencylist;
	}
	public Boolean getHodisable() {
		return hodisable;
	}
	public void setHodisable(Boolean hodisable) {
		this.hodisable = hodisable;
	}
	public Boolean getFeedisable() {
		return feedisable;
	}
	public void setFeedisable(Boolean feedisable) {
		this.feedisable = feedisable;
	}
	public String getTxnactiondesc() {
		return txnactiondesc;
	}
	public void setTxnactiondesc(String txnactiondesc) {
		this.txnactiondesc = txnactiondesc;
	}
	public List getMappedscheme() {
		return mappedscheme;
	}
	public void setMappedscheme(List mappedscheme) {
		this.mappedscheme = mappedscheme;
	}
	public Boolean getHeaderreq() {
		return headerreq;
	}
	public void setHeaderreq(Boolean headerreq) {
		this.headerreq = headerreq;
	}
	public String getTxnactioncode() {
		return txnactioncode;
	}
	public void setTxnactioncode(String txnactioncode) {
		this.txnactioncode = txnactioncode;
	}
	public List getSchemelist() {
		return schemelist;
	}
	public void setSchemelist(List schemelist) {
		this.schemelist = schemelist;
	}
	public String getSchemecdoe() {
		return schemecdoe;
	}
	public void setSchemecdoe(String schemecdoe) {
		this.schemecdoe = schemecdoe;
	}
	public String getSchemedesc() {
		return schemedesc;
	}
	public void setSchemedesc(String schemedesc) {
		this.schemedesc = schemedesc;
	}
	
	
}
