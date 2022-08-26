package com.ifd.BulkReg;

import java.util.List;

public class BulkCustomerRegisterBean {
	
	public List BatchID;
	public List BulkRegCustlist,productlist;
	public String embossingname;
	public String custid;
	public String cardno;
	public String mobileno;
	public String fname;
	public String lname;
	public String mname;
	public String dob;
	public String martialstatus;
	public String nationality;
	public String spousename;
	
	public String batch_id;
	public List regbulktype;
	public String regbulkchecktype;
	
	public String getRegbulkchecktype() {
		return regbulkchecktype;
	}
	public void setRegbulkchecktype(String regbulkchecktype) {
		this.regbulkchecktype = regbulkchecktype;
	}
	public List getRegbulktype() {
		return regbulktype;
	}
	public void setRegbulktype(List regbulktype) {
		this.regbulktype = regbulktype;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getBlkproduct_code() {
		return blkproduct_code;
	}
	public void setBlkproduct_code(String blkproduct_code) {
		this.blkproduct_code = blkproduct_code;
	}
	public String getBlksubproduct() {
		return blksubproduct;
	}
	public void setBlksubproduct(String blksubproduct) {
		this.blksubproduct = blksubproduct;
	}

	public String blkproduct_code;
	public String blksubproduct;
	
	
	public List getProductlist() {
		return productlist;
	}
	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}
	public List getBulkRegCustlist() {
		return BulkRegCustlist;
	}

	public void setBulkRegCustlist(List bulkRegCustlist) {
		BulkRegCustlist = bulkRegCustlist;
		
	}

	public List getBatchID() {
		return BatchID;
	}

	public void setBatchID(List batchID) {
		BatchID = batchID;
	}

	public String getMartialstatus() {
		return martialstatus;
	}

	public void setMartialstatus(String martialstatus) {
		this.martialstatus = martialstatus;
	}

	public String getSpousename() {
		return spousename;
	}

	public void setSpousename(String spousename) {
		this.spousename = spousename;
	}

	public String mothername;
	public String fathername;
	public String email;
	public String pobox;
	public String phouseno;
	public String pstname;
	public String pwardname;
	public String pcity;
	public String pdist;
	
	
	public String getFname() {
		return fname;
	}

	public String getPobox() {
		return pobox;
	}

	public void setPobox(String pobox) {
		this.pobox = pobox;
	}

	public String getPhouseno() {
		return phouseno;
	}

	public void setPhouseno(String phouseno) {
		this.phouseno = phouseno;
	}

	public String getPstname() {
		return pstname;
	}

	public void setPstname(String pstname) {
		this.pstname = pstname;
	}

	public String getPwardname() {
		return pwardname;
	}

	public void setPwardname(String pwardname) {
		this.pwardname = pwardname;
	}

	public String getPcity() {
		return pcity;
	}

	public void setPcity(String pcity) {
		this.pcity = pcity;
	}

	public String getPdist() {
		return pdist;
	}

	public void setPdist(String pdist) {
		this.pdist = pdist;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}



	public String getMothername() {
		return mothername;
	}

	public void setMothername(String mothername) {
		this.mothername = mothername;
	}

	public String getFathername() {
		return fathername;
	}

	public void setFathername(String fathername) {
		this.fathername = fathername;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getEmbossingname() {
		return embossingname;
	}

	public void setEmbossingname(String embossingname) {
		this.embossingname = embossingname;
	}
	
	public List acctno;

	public List getAcctno() {
		return acctno;
	}

	public void setAcctno(List acctno) {
		this.acctno = acctno;
	}

	public String acctflag;
	public String getAcctflag() {
		return acctflag;
	}

	public void setAcctflag(String acctflag) {
		this.acctflag = acctflag;
	}
	
	
}
