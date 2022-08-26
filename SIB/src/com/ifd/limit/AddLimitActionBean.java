package com.ifd.limit;

import java.util.List;

public class AddLimitActionBean {
	
	
	
	
	private String limitmastercode;
	public List getGlobalcurrcy() {
		return globalcurrcy;
	}
	public void setGlobalcurrcy(List globalcurrcy) {
		this.globalcurrcy = globalcurrcy;
	}
	private List globalcurrcy;
	private String feenamebean;
	private List masterfeelist;
	private String limitType;
	private String limitName;
	private String limitCurrency;
	private String limitBasedOn;
	private List masterlimitlist;
	private List currencyList;
	
	private List<String> limitmasterlis;
	   
	public String getLimitType() {
		return limitType;
	}
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}
	public String getLimitName() {
		return limitName;
	}
	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}
	public String getLimitCurrency() {
		return limitCurrency;
	}
	public void setLimitCurrency(String limitCurrency) {
		this.limitCurrency = limitCurrency;
	}
	public String getLimitBasedOn() {
		return limitBasedOn;
	}
	public void setLimitBasedOn(String limitBasedOn) {
		this.limitBasedOn = limitBasedOn;
	}
	public List getMasterlimitlist() {
		return masterlimitlist;
	}
	public void setMasterlimitlist(List masterlimitlist) {
		this.masterlimitlist = masterlimitlist;
	}
	public List getCurrencyList() {
		return currencyList;
	}
	public void setCurrencyList(List currencyList) {
		this.currencyList = currencyList;
	}
	public List<String> getLimitmasterlis() {
		return limitmasterlis;
	}
	public void setLimitmasterlis(List<String> limitmasterlis) {
		this.limitmasterlis = limitmasterlis;
	}
	public String getLimitmastercode() {
		return limitmastercode;
	}
	public void setLimitmastercode(String limitmastercode) {
		this.limitmastercode = limitmastercode;
	}
	public List getMasterfeelist() {
		return masterfeelist;
	}
	public void setMasterfeelist(List masterfeelist) {
		this.masterfeelist = masterfeelist;
	}
	public String getFeenamebean() {
		return feenamebean;
	}
	public void setFeenamebean(String feenamebean) {
		this.feenamebean = feenamebean;
	}

}
