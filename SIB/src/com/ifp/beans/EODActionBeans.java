package com.ifp.beans;

import java.util.List;

public class EODActionBeans {
	String curbusinessdate;
	List subglamountlist;
	Boolean issubglamtlistexist = true;
	List instidlist;
	
	
	public List getInstidlist() {
		return instidlist;
	}

	public void setInstidlist(List instidlist) {
		this.instidlist = instidlist;
	}

	public Boolean getIssubglamtlistexist() {
		return issubglamtlistexist;
	}

	public void setIssubglamtlistexist(Boolean issubglamtlistexist) {
		this.issubglamtlistexist = issubglamtlistexist;
	}

	public List getSubglamountlist() {
		return subglamountlist;
	}

	public void setSubglamountlist(List subglamountlist) {
		this.subglamountlist = subglamountlist;
	}

	public String getCurbusinessdate() {
		return curbusinessdate;
	}

	public void setCurbusinessdate(String curbusinessdate) {
		this.curbusinessdate = curbusinessdate;
	}
	
}
