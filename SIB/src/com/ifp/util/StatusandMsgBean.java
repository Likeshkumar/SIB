package com.ifp.util;

public class StatusandMsgBean {
	
	private String CurrStatus;
	private String statusCode;
	private String statusMsg;
	private String mkrckrStatus;
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public String getMkrckrStatus() {
		return mkrckrStatus;
	}
	public void setMkrckrStatus(String mkrckrStatus) {
		this.mkrckrStatus = mkrckrStatus;
	}
	public String getCurrStatus() {
		return CurrStatus;
	}
	public void setCurrStatus(String currStatus) {
		CurrStatus = currStatus;
	}

}
