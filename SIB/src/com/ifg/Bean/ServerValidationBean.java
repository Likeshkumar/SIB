package com.ifg.Bean;

public class ServerValidationBean 
{
	
	// hsm bean
	String hsmname;
	String hsmip;
	String hsmport;
	String hsmprotocol;
	String hsmheadertype;
	String hsmhedlen;
	String hsmtimeout;
	String hsmconinterval;
	String headlength;
	String hsmtype;
	
	
	// padss bean
	String KEYDESC;
	
	//forget password
	String usertext;
	
	
	public String getUsertext() {
		return usertext;
	}
	public void setUsertext(String usertext) {
		this.usertext = usertext;
	}
	//inst bean
	String instid;
	String instname;
	String deploymenttype;
	String orderref;
	String branchattched;
	String brcodelen;
	String countrycode;
	String accnumleng;
	String cid;
	String cidbasedon;
	String mailalertreq;
	String smsalertreq;
	String accttypelength;
	String acctsubtypelength;
	String cardtypelength;
	String login_retry_cnt;
	String pin_retry_cnt;
	String maxaddoncards;
	String maxaddonaccounts;
	String pcadssenble;
	String seqkeyvalue;
	String renewalperiods;
	String currencytype;
	
	// add prof
	
	String profilename;
	String profiledesc;
	
	
	
	//add user 
	
	
	String username;
	String cbsusername;
	String fname;
	String lname;
	String email;
	String status;
	String profile;
	//
	
	
	
	//
	String cardno;
	
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCbsusername() {
		return cbsusername;
	}
	public void setCbsusername(String cbsusername) {
		this.cbsusername = cbsusername;
	}
	public String getFname() {
		return fname;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public String getProfilename() {
		return profilename;
	}
	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}
	public String getProfiledesc() {
		return profiledesc;
	}
	public void setProfiledesc(String profiledesc) {
		this.profiledesc = profiledesc;
	}
	public String getInstname() {
		return instname;
	}
	public void setInstname(String instname) {
		this.instname = instname;
	}
	public String getDeploymenttype() {
		return deploymenttype;
	}
	public void setDeploymenttype(String deploymenttype) {
		this.deploymenttype = deploymenttype;
	}
	public String getOrderref() {
		return orderref;
	}
	public void setOrderref(String orderref) {
		this.orderref = orderref;
	}
	public String getBranchattched() {
		return branchattched;
	}
	public void setBranchattched(String branchattched) {
		this.branchattched = branchattched;
	}
	public String getBrcodelen() {
		return brcodelen;
	}
	public void setBrcodelen(String brcodelen) {
		this.brcodelen = brcodelen;
	}
	public String getCountrycode() {
		return countrycode;
	}
	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}
	public String getAccnumleng() {
		return accnumleng;
	}
	public void setAccnumleng(String accnumleng) {
		this.accnumleng = accnumleng;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCidbasedon() {
		return cidbasedon;
	}
	public void setCidbasedon(String cidbasedon) {
		this.cidbasedon = cidbasedon;
	}
	public String getMailalertreq() {
		return mailalertreq;
	}
	public void setMailalertreq(String mailalertreq) {
		this.mailalertreq = mailalertreq;
	}
	public String getSmsalertreq() {
		return smsalertreq;
	}
	public void setSmsalertreq(String smsalertreq) {
		this.smsalertreq = smsalertreq;
	}
	public String getAccttypelength() {
		return accttypelength;
	}
	public void setAccttypelength(String accttypelength) {
		this.accttypelength = accttypelength;
	}
	public String getAcctsubtypelength() {
		return acctsubtypelength;
	}
	public void setAcctsubtypelength(String acctsubtypelength) {
		this.acctsubtypelength = acctsubtypelength;
	}
	public String getCardtypelength() {
		return cardtypelength;
	}
	public void setCardtypelength(String cardtypelength) {
		this.cardtypelength = cardtypelength;
	}
	public String getLogin_retry_cnt() {
		return login_retry_cnt;
	}
	public void setLogin_retry_cnt(String login_retry_cnt) {
		this.login_retry_cnt = login_retry_cnt;
	}
	public String getPin_retry_cnt() {
		return pin_retry_cnt;
	}
	public void setPin_retry_cnt(String pin_retry_cnt) {
		this.pin_retry_cnt = pin_retry_cnt;
	}
	public String getMaxaddoncards() {
		return maxaddoncards;
	}
	public void setMaxaddoncards(String maxaddoncards) {
		this.maxaddoncards = maxaddoncards;
	}
	public String getMaxaddonaccounts() {
		return maxaddonaccounts;
	}
	public void setMaxaddonaccounts(String maxaddonaccounts) {
		this.maxaddonaccounts = maxaddonaccounts;
	}
	public String getPcadssenble() {
		return pcadssenble;
	}
	public void setPcadssenble(String pcadssenble) {
		this.pcadssenble = pcadssenble;
	}
	public String getSeqkeyvalue() {
		return seqkeyvalue;
	}
	public void setSeqkeyvalue(String seqkeyvalue) {
		this.seqkeyvalue = seqkeyvalue;
	}
	public String getRenewalperiods() {
		return renewalperiods;
	}
	public void setRenewalperiods(String renewalperiods) {
		this.renewalperiods = renewalperiods;
	}
	public String getCurrencytype() {
		return currencytype;
	}
	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}
	public String getInstid() {
		return instid;
	}
	public void setInstid(String instid) {
		this.instid = instid;
	}
	public String getKEYDESC() {
		return KEYDESC;
	}
	public void setKEYDESC(String KEYDESC) {
		this.KEYDESC = KEYDESC;
	}
	public String getHsmport() {
		return hsmport;
	}
	public void setHsmport(String hsmport) {
		this.hsmport = hsmport;
	}
	public String getHsmprotocol() {
		return hsmprotocol;
	}
	public void setHsmprotocol(String hsmprotocol) {
		this.hsmprotocol = hsmprotocol;
	}
	public String getHsmheadertype() {
		return hsmheadertype;
	}
	public void setHsmheadertype(String hsmheadertype) {
		this.hsmheadertype = hsmheadertype;
	}
	public String getHsmhedlen() {
		return hsmhedlen;
	}
	public void setHsmhedlen(String hsmhedlen) {
		this.hsmhedlen = hsmhedlen;
	}
	public String getHsmtimeout() {
		return hsmtimeout;
	}
	public void setHsmtimeout(String hsmtimeout) {
		this.hsmtimeout = hsmtimeout;
	}
	public String getHsmconinterval() {
		return hsmconinterval;
	}
	public void setHsmconinterval(String hsmconinterval) {
		this.hsmconinterval = hsmconinterval;
	}
	public String getHeadlength() {
		return headlength;
	}
	public void setHeadlength(String headlength) {
		this.headlength = headlength;
	}
	public String getHsmtype() {
		return hsmtype;
	}
	public void setHsmtype(String hsmtype) {
		this.hsmtype = hsmtype;
	}
	public String getHsmname() {
		return hsmname;
	}
	public void setHsmname(String hsmname) {
		this.hsmname = hsmname;
	}
	public String getHsmip() {
		return hsmip;
	}
	public void setHsmip(String hsmip) {
		this.hsmip = hsmip;
	}
}
