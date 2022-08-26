package com.ifp.maintain;

import java.util.List;

public class CbsAccountBeans {
	
	private String accountnolength;
	private String cinidbasedon;
	private String cinnoLength;
	private List accttypelist;  
	private List productlist;
	private List currencylist;
	
	private String accounttypevalue;
	private String accountsubtypevalue;
	private String currencyvalue;
	private String accountnovalue;
	private String accflag;
	private String cardno;
	private List accounttypedetails;
	  
	public String getAccounttypevalue() {
		return accounttypevalue;
	}
	public void setAccounttypevalue(String accounttypevalue) {
		this.accounttypevalue = accounttypevalue;
	}
	public String getAccountsubtypevalue() {
		return accountsubtypevalue;
	}
	public void setAccountsubtypevalue(String accountsubtypevalue) {
		this.accountsubtypevalue = accountsubtypevalue;
	}
	public String getCurrencyvalue() {
		return currencyvalue;
	}
	public void setCurrencyvalue(String currencyvalue) {
		this.currencyvalue = currencyvalue;
	}
	public String getAccountnovalue() {
		return accountnovalue;
	}
	public void setAccountnovalue(String accountnovalue) {
		this.accountnovalue = accountnovalue;
	}
	public String getAccountnolength() {  
		return accountnolength;  
	}
	public void setAccountnolength(String accountnolength) {
		this.accountnolength = accountnolength;
	}
	public String getCinidbasedon() {
		return cinidbasedon;
	}
	public void setCinidbasedon(String cinidbasedon) {
		this.cinidbasedon = cinidbasedon;
	}
	public String getCinnoLength() {
		return cinnoLength;
	}
	public void setCinnoLength(String cinnoLength) {
		this.cinnoLength = cinnoLength;
	}
	
	
	public List getAccttypelist() {
		return accttypelist;
	}
	public void setAccttypelist(List accttypelist) {
		this.accttypelist = accttypelist;
	}
	public List getProductlist() {
		return productlist;
	}
	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}
	public String getAccflag() {
		return accflag;
	}
	public void setAccflag(String accflag) {
		this.accflag = accflag;
	}
	public List getCurrencylist() {
		return currencylist;
	}
	public void setCurrencylist(List currencylist) {
		this.currencylist = currencylist;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public List getAccounttypedetails() {
		return accounttypedetails;
	}
	public void setAccounttypedetails(List accounttypedetails) {
		this.accounttypedetails = accounttypedetails;
	}
	
	public String custname;

	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
}
