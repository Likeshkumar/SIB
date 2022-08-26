package com.ifp.beans;

import java.text.SimpleDateFormat;

public class PersonalCardProcessBean {
	
	private String instid;
	private String cardno;
	private String hcardno;
	private String mcardno;
	private String accountno;
	private String accttypeid;
	private String accsubtypeid;
	private String accccy;
	private String cardstatus;
	private String CAF_REC_STATUS;
	private String STATUS_CODE;
	private String parentcardno;
	private String expirydate;
	private String defaultdate;
	private String makerid;
	private String makerdate;
	private String checkerid;
	private String checkerdate;
	private String mkckstatus;
	private String servicecode;
	private String cardrefno;
	private String cardissuetype;
	private String customermobile;
	private String renewalflag;
	private String collectbranch;
     private static String PRIVILEGE_CODE="0";
	 private static String WDL_AMT="0";
	 private static String WDL_CNT="0";
	 private static String PUR_AMT="0";
	 private static String PUR_CNT="0";
	private static String ECOM_AMT="0";
	 private static String ECOM_CNT="0";

	public static String getCurrentDate() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy ");
		return sdf.format(date);
	}

	public String getInstid() {
		return instid;
	}

	public void setInstid(String instid) {
		this.instid = instid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getHcardno() {
		return hcardno;
	}

	public void setHcardno(String hcardno) {
		this.hcardno = hcardno;
	}

	public String getMcardno() {
		return mcardno;
	}

	public void setMcardno(String mcardno) {
		this.mcardno = mcardno;
	}

	public String getAccountno() {
		return accountno;
	}

	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}

	public String getAccttypeid() {
		return accttypeid;
	}

	public void setAccttypeid(String accttypeid) {
		this.accttypeid = accttypeid;
	}

	public String getAccsubtypeid() {
		return accsubtypeid;
	}

	public void setAccsubtypeid(String accsubtypeid) {
		this.accsubtypeid = accsubtypeid;
	}

	public String getAccccy() {
		return accccy;
	}

	public void setAccccy(String accccy) {
		this.accccy = accccy;
	}

	public String getCardstatus() {
		return cardstatus;
	}

	public void setCardstatus(String cardstatus) {
		this.cardstatus = cardstatus;
	}

	public String getCAF_REC_STATUS() {
		return CAF_REC_STATUS;
	}

	public void setCAF_REC_STATUS(String cAF_REC_STATUS) {
		CAF_REC_STATUS = cAF_REC_STATUS;
	}

	public String getSTATUS_CODE() {
		return STATUS_CODE;
	}

	public void setSTATUS_CODE(String sTATUS_CODE) {
		STATUS_CODE = sTATUS_CODE;
	}

	public String getParentcardno() {
		return parentcardno;
	}

	public void setParentcardno(String parentcardno) {
		this.parentcardno = parentcardno;
	}

	public String getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}

	public String getDefaultdate() {
		return defaultdate;
	}

	public void setDefaultdate(String defaultdate) {
		this.defaultdate = defaultdate;
	}

	public String getMakerid() {
		return makerid;
	}

	public void setMakerid(String makerid) {
		this.makerid = makerid;
	}

	public String getMakerdate() {
		return makerdate;
	}

	public void setMakerdate(String makerdate) {
		this.makerdate = makerdate;
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

	public String getMkckstatus() {
		return mkckstatus;
	}

	public void setMkckstatus(String mkckstatus) {
		this.mkckstatus = mkckstatus;
	}

	public String getServicecode() {
		return servicecode;
	}

	public void setServicecode(String servicecode) {
		this.servicecode = servicecode;
	}

	public String getCardrefno() {
		return cardrefno;
	}

	public void setCardrefno(String cardrefno) {
		this.cardrefno = cardrefno;
	}

	public String getCardissuetype() {
		return cardissuetype;
	}

	public void setCardissuetype(String cardissuetype) {
		this.cardissuetype = cardissuetype;
	}

	public String getCustomermobile() {
		return customermobile;
	}

	public void setCustomermobile(String customermobile) {
		this.customermobile = customermobile;
	}

	public String getRenewalflag() {
		return renewalflag;
	}

	public void setRenewalflag(String renewalflag) {
		this.renewalflag = renewalflag;
	}

	public String getCollectbranch() {
		return collectbranch;
	}

	public void setCollectbranch(String collectbranch) {
		this.collectbranch = collectbranch;
	}

	@Override
	public String toString() {
		return "PersonalCardProcessBean [instid=" + instid + ", cardno=" + cardno + ", hcardno=" + hcardno
				+ ", mcardno=" + mcardno + ", accountno=" + accountno + ", accttypeid=" + accttypeid + ", accsubtypeid="
				+ accsubtypeid + ", accccy=" + accccy + ", cardstatus=" + cardstatus + ", CAF_REC_STATUS="
				+ CAF_REC_STATUS + ", STATUS_CODE=" + STATUS_CODE + ", parentcardno=" + parentcardno + ", expirydate="
				+ expirydate + ", defaultdate=" + defaultdate + ", makerid=" + makerid + ", makerdate=" + makerdate
				+ ", checkerid=" + checkerid + ", checkerdate=" + checkerdate + ", mkckstatus=" + mkckstatus
				+ ", servicecode=" + servicecode + ", cardrefno=" + cardrefno + ", cardissuetype=" + cardissuetype
				+ ", customermobile=" + customermobile + ", renewalflag=" + renewalflag + ", collectbranch="
				+ collectbranch + "]";
	}

}
