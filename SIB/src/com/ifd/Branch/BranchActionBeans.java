package com.ifd.Branch;
 
import java.util.List;

public class BranchActionBeans {
	public String branchname;
	public String branchcode;
	public String formaction;
	public String br_addr1;
	public String br_addr2;
	public String br_addr3;
	public String br_city;
	public String br_state;
	public String br_phone;
	public String br_fax_num;
	public String br_email;
	public String br_manager;
	public Boolean readonly = false;
	public List productlist; 
	public List configuredmaplist;
	
	
	public List getConfiguredmaplist() {
		return configuredmaplist;
	}
	public void setConfiguredmaplist(List configuredmaplist) {
		this.configuredmaplist = configuredmaplist;
	}
	public List getProductlist() {
		return productlist;
	}
	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}
	public Boolean getReadonly() {
		return readonly;
	}
	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}
	public String getBr_addr1() {
		return br_addr1;
	}
	public void setBr_addr1(String br_addr1) {
		this.br_addr1 = br_addr1;
	}
	public String getBr_addr2() {
		return br_addr2;
	}
	public void setBr_addr2(String br_addr2) {
		this.br_addr2 = br_addr2;
	}
	public String getBr_addr3() {
		return br_addr3;
	}
	public void setBr_addr3(String br_addr3) {
		this.br_addr3 = br_addr3;
	}
	public String getBr_city() {
		return br_city;
	}
	public void setBr_city(String br_city) {
		this.br_city = br_city;
	}
	public String getBr_state() {
		return br_state;
	}
	public void setBr_state(String br_state) {
		this.br_state = br_state;
	}
	public String getBr_phone() {
		return br_phone;
	}
	public void setBr_phone(String br_phone) {
		this.br_phone = br_phone;
	}
	public String getBr_fax_num() {
		return br_fax_num;
	}
	public void setBr_fax_num(String br_fax_num) {
		this.br_fax_num = br_fax_num;
	}
	public String getBr_email() {
		return br_email;
	}
	public void setBr_email(String br_email) {
		this.br_email = br_email;
	}
	public String getBr_manager() {
		return br_manager;
	}
	public void setBr_manager(String br_manager) {
		this.br_manager = br_manager;
	}
	public String getBranchname() {
		return branchname;
	}
	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}
	public String getBranchcode() {
		return branchcode;
	}
	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}
	public String getFormaction() {
		return formaction;
	}
	public void setFormaction(String formaction) {
		this.formaction = formaction;
	}
	
	
}
