package com.ifp.beans;
 
import java.util.List;

import com.ifp.Action.BaseAction;

 
 

public class InstCardActivateProcessBeans extends BaseAction 
{  
	public List <String> prodlist;
	public List <String> branchlist;
	public List <String>prenamelist; 
	public String cardno;
	public String type; 
	public List ordernolist;
	String curamountfieldbean;
	String feeamountbean; 
	List orderedcardlist; 
	String cardact_msg ;
	String cardact_amount; 
	String loadingamtlist;
	Double activationfeebean;
	List instorderlist; 
	String summerytablenew;
	
	
	
	public String getSummerytablenew() {
		return summerytablenew;
	}

	public void setSummerytablenew(String summerytablenew) {
		this.summerytablenew = summerytablenew;
	}
	
	public String getCardact_msg() {
		return cardact_msg;
	}

	public void setCardact_msg(String cardact_msg) {
		this.cardact_msg = cardact_msg;
	}

	public String getCardact_amount() {
		return cardact_amount;
	}

	public void setCardact_amount(String cardact_amount) {
		this.cardact_amount = cardact_amount;
	}

	public String getLoadingamtlist() {
		return loadingamtlist;
	}

	public void setLoadingamtlist(String loadingamtlist) {
		this.loadingamtlist = loadingamtlist;
	}

	
	public Double getActivationfeebean() {
		return activationfeebean;
	}

	public void setActivationfeebean(Double activationfeebean) {
		this.activationfeebean = activationfeebean;
	} 
	
	
	public List getOrderedcardlist() {
		return orderedcardlist; 
	}
	public void setOrderedcardlist(List orderedcardlist) {
		this.orderedcardlist = orderedcardlist;
	}
	
	public String getCuramountfieldbean() {
		return curamountfieldbean;
	}

	public void setCuramountfieldbean(String curamountfieldbean) {
		this.curamountfieldbean = curamountfieldbean;
	}

	public String getFeeamountbean() {
		return feeamountbean;
	}

	public void setFeeamountbean(String feeamountbean) {
		this.feeamountbean = feeamountbean;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	
	
	public String getType() {
		return type;
	} 
	public void setType(String type) {
		this.type = type;
	}
	
	
	public List getOrdernolist() {
		return ordernolist;
	}

	public void setOrdernolist(List ordernolist) {
		this.ordernolist = ordernolist;
	} 
	
	public List <String> getProdlist() {
		return prodlist;
	}

	public void setProdlist(List<String> prodlist) {
		this.prodlist = prodlist;
	} 

	public List <String> getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List<String>	 branchlist) {
		this.branchlist = branchlist ;
	}
 
	public List getInstorderlist() {
		return instorderlist;
	}

	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	} 
	public List<String> getPrenamelist() {
		return prenamelist;
	}

	public void setPrenamelist(List<String> prenamelist) {
		this.prenamelist = prenamelist;
	}

	
	 
	
} // class end
 
 
