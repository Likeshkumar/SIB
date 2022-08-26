package com.ifd.Report;

import java.util.List;

public class MasterReportGenerationBean {

	private List cardreportlist;
	private List branchList;
	private List productList;
	private List branchListview;
	private List productListview;
	public List getProductList() {
		return productList;
	}

	public void setProductList(List productList) {
		this.productList = productList;
	}

	public List getBranchListview() {
		return branchListview;
	}

	public void setBranchListview(List branchListview) {
		this.branchListview = branchListview;
	}

	public List getProductListview() {
		return productListview;
	}

	public void setProductListview(List productListview) {
		this.productListview = productListview;
	}

	public List getBranchList() {
		return branchList;
	}
   
	public void setBranchList(List branchList) {
		this.branchList = branchList;
	}

	public List getCardreportlist() {
		return cardreportlist;
	}

	public void setCardreportlist(List cardreportlist) {
		this.cardreportlist = cardreportlist;
	}
}
