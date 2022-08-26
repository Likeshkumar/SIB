package com.ifp.beans;

import java.util.List;

public class SMSSenderBeans {
	List binlist;
	List smslist;	
	String bulksmsid;
	List smsgrouplist;
	String smssenderlist;
	String smssubject;
	String smscontent;
	String smsdate;
	String makerid;
	String smsstatus;
	String checkerid;
	String checkerdate;
	String smsexceptionlist;
	List exceptlist;
	Boolean verifyneeded = false;
	Boolean sendsmsnow = false;
	
	
	public String getBulksmsid() {
		return bulksmsid;
	}

	public void setBulksmsid(String bulksmsid) {
		this.bulksmsid = bulksmsid;
	}

	public Boolean getSendsmsnow() {
		return sendsmsnow;
	}

	public void setSendsmsnow(Boolean sendsmsnow) {
		this.sendsmsnow = sendsmsnow;
	}

	public Boolean getVerifyneeded() {
		return verifyneeded;
	}

	public void setVerifyneeded(Boolean verifyneeded) {
		this.verifyneeded = verifyneeded;
	}

	public List getExceptlist() {
		return exceptlist;
	}

	public void setExceptlist(List exceptlist) {
		this.exceptlist = exceptlist;
	}

	public List getSmsgrouplist() {
		return smsgrouplist;
	}

	public void setSmsgrouplist(List smsgrouplist) {
		this.smsgrouplist = smsgrouplist;
	}

	public String getSmsdate() {
		return smsdate;
	}

	public void setSmsdate(String smsdate) {
		this.smsdate = smsdate;
	}

	public String getSmssenderlist() {
		return smssenderlist;
	}

	public void setSmssenderlist(String smssenderlist) {
		this.smssenderlist = smssenderlist;
	}

	public String getSmssubject() {
		return smssubject;
	}

	public void setSmssubject(String smssubject) {
		this.smssubject = smssubject;
	}

	public String getSmscontent() {
		return smscontent;
	}

	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}

	 
	public String getMakerid() {
		return makerid;
	}

	public void setMakerid(String makerid) {
		this.makerid = makerid;
	}

	public String getSmsstatus() {
		return smsstatus;
	}

	public void setSmsstatus(String smsstatus) {
		this.smsstatus = smsstatus;
	}

	public String getCheckerid() {
		return checkerid;
	}

	public void setCheckerid(String checkerid) {
		this.checkerid = checkerid;
	}

	public String getCheckerdate() {
		return checkerdate;
	}

	public void setCheckerdate(String checkerdate) {
		this.checkerdate = checkerdate;
	}

	public String getSmsexceptionlist() {
		return smsexceptionlist;
	}

	public void setSmsexceptionlist(String smsexceptionlist) {
		this.smsexceptionlist = smsexceptionlist;
	}

	public List getSmslist() {
		return smslist;
	}

	public void setSmslist(List smslist) {
		this.smslist = smslist;
	}

	public List getBinlist() {
		return binlist;
	}

	public void setBinlist(List binlist) {
		this.binlist = binlist;
	}
	
	
}
