package com.ifd.fee;

import java.util.List;

import com.ifp.Action.BaseAction;



public class FeeConfigBeans extends BaseAction{
	private String feename;
	private String feecode;
	private String numericCode;
	private String currecyCode;
	
	private List feeLisConfig;  
	
	
	
	
	
	public String getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}

	public String getCurrecyCode() {
		return currecyCode;
	}

	public void setCurrecyCode(String currecyCode) {
		this.currecyCode = currecyCode;
	}

	public List getFeeLisConfig() {
		return feeLisConfig;
	}
	
	
	
	public String curcode;
	
	
	public String getCurcode() {
		return curcode;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	public void setFeeLisConfig(List feeLisConfig) {
		this.feeLisConfig = feeLisConfig;
	}

	public String getFeename() {
		return feename;
	}

	public void setFeename(String feename) {
		this.feename = feename;
	}

	public String getFeecode() {
		return feecode;
	}

	public void setFeecode(String feecode) {
		this.feecode = feecode;
	}

 
	
} //end class
