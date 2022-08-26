package com.ifp.maintain;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.CallCenterBeans;
import com.ifp.beans.CardMaintainActionBeans;
import com.ifd.fee.FeeConfigBeans;
import com.ifp.dao.CardMaintainActionDAO;
import com.ifd.fee.FeeConfigDAO;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.dao.InstCardRegisterProcessDAO;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import connection.SwitchConnection;

public class CardMaintainAction2212 extends BaseAction {

	public String comBranchId() {
		HttpSession session = getRequest().getSession();
		String br_id = (String) session.getAttribute("BRANCHCOD" + "E");
		return br_id;
	}

	public String comuserType() {
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");
		return usertype;
	}

	private static final long serialVersionUID = -8376161637970676446L;

	CardMaintainActionDAO cardmaindao = new CardMaintainActionDAO();
	CardMaintainActionBeans cardmainbean = new CardMaintainActionBeans();
	final String ACTIVESTATUCODE = "01";
	final String TEMPSTATUSCODE = "02";
	final String LOSTSTOLENSTATUSCODE = "03";
	final String CLOSESTATUSCODE = "04";
	final String DAMAGESTATUSCODE = "10";
	final String REPINSTATUSCODE = "06";
	final String REISSUSTATUSCODE = "07";
	final String NOTACTIVATEDSTATUSCODE = "09";
	final String PINLOCKED = "11";
	final String RENEWAL = "12";
	String TABLENAME = "";

	CommonDesc commondesc = new CommonDesc();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	CallCenterBeans callbean = new CallCenterBeans();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();
	FeeConfigBeans feebean = new FeeConfigBeans();

	private List pinchange;
	private String nopinchange;
	private String wrongpin;
	private String pinblock;

	private List cardactivelist;

	public List getCardactivelist() {
		return cardactivelist;
	}

	public void setCardactivelist(List cardactivelist) {
		this.cardactivelist = cardactivelist;
	}

	public String getNopinchange() {
		return nopinchange;
	}

	public void setNopinchange(String nopinchange) {
		this.nopinchange = nopinchange;
	}

	public String getWrongpin() {
		return wrongpin;
	}

	public void setWrongpin(String wrongpin) {
		this.wrongpin = wrongpin;
	}

	public String getPinblock() {
		return pinblock;
	}

	public void setPinblock(String pinblock) {
		this.pinblock = pinblock;
	}

	public List getPinchange() {
		return pinchange;
	}

	public void setPinchange(List pinchangedate) {
		this.pinchange = pinchangedate;
	}

	private String mact;

	public String getMact() {
		return mact;
	}

	public void setMact(String mact) {
		this.mact = mact;
	}

	private String atmwithdrawlcount;
	private String visaatmwithdrawl;
	private String interswitchatmwithdr;
	private String totalatmwithdrawal;
	private String noofpostxns;
	private String noofonlinetxns;

	public String getTotalatmwithdrawal() {
		return totalatmwithdrawal;
	}

	public void setTotalatmwithdrawal(String totalatmwithdrawal) {
		this.totalatmwithdrawal = totalatmwithdrawal;
	}

	public String getNoofpostxns() {
		return noofpostxns;
	}

	public void setNoofpostxns(String noofpostxns) {
		this.noofpostxns = noofpostxns;
	}

	public String getNoofonlinetxns() {
		return noofonlinetxns;
	}

	public void setNoofonlinetxns(String noofonlinetxns) {
		this.noofonlinetxns = noofonlinetxns;
	}

	public String getInterswitchatmwithdr() {
		return interswitchatmwithdr;
	}

	public void setInterswitchatmwithdr(String interswitchatmwithdr) {
		this.interswitchatmwithdr = interswitchatmwithdr;
	}

	public String getVisaatmwithdrawl() {
		return visaatmwithdrawl;
	}

	public void setVisaatmwithdrawl(String visaatmwithdrawl) {
		this.visaatmwithdrawl = visaatmwithdrawl;
	}

	public String getAtmwithdrawlcount() {
		return atmwithdrawlcount;
	}

	public void setAtmwithdrawlcount(String atmwithdrawlcount) {
		this.atmwithdrawlcount = atmwithdrawlcount;
	}

	public FeeConfigBeans getFeebean() {
		return feebean;
	}

	public void setFeebean(FeeConfigBeans feebean) {
		this.feebean = feebean;
	}

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public CallCenterBeans getCallbean() {
		return callbean;
	}

	public void setCallbean(CallCenterBeans callbean) {
		this.callbean = callbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public CardMaintainActionDAO getCardmaindao() {
		return cardmaindao;
	}

	public void setCardmaindao(CardMaintainActionDAO cardmaindao) {
		this.cardmaindao = cardmaindao;
	}

	public CardMaintainActionBeans getCardmainbean() {
		return cardmainbean;
	}

	public void setCardmainbean(CardMaintainActionBeans cardmainbean) {
		this.cardmainbean = cardmainbean;
	}

	private String acccountno;
	private String acctsubtypeid;

	private List actypedescvalue;

	private List getacctnolist;

	public List getGetacctnolist() {
		return getacctnolist;
	}

	public void setGetacctnolist(List getacctnolist) {
		this.getacctnolist = getacctnolist;
	}

	public List getActypedescvalue() {
		return actypedescvalue;
	}

	public void setActypedescvalue(List actypedescvalue) {
		this.actypedescvalue = actypedescvalue;
	}

	String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	List<String> prodlist;

	public List<String> getProdlist() {
		return prodlist;
	}

	public void setProdlist(List<String> prodlist) {
		this.prodlist = prodlist;
	}

	List<String> branchlist;

	public List<String> getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List<String> branchlist) {
		this.branchlist = branchlist;
	}

	List instorderlist;

	public List getInstorderlist() {
		return instorderlist;
	}

	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	}

	List<String> prenamelist;

	public List<String> getPrenamelist() {
		return prenamelist;
	}

	public void setPrenamelist(List<String> prenamelist) {
		this.prenamelist = prenamelist;
	}

	public void validate() {
		HttpSession session = getRequest().getSession();
		String apptype = (String) session.getAttribute("APPLICATIONTYPE");
		if (apptype != null && apptype.equals("CREDIT")) {
			TABLENAME = " IFC_CARD_PRODUCTION ";
		} else if (apptype != null && apptype.equals("DEBIT")) {
			TABLENAME = " CARD_PRODUCTION ";
		} else {
			TABLENAME = " CARD_PRODUCTION ";
		}
	}

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public String comUsername() {
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("USERNAME");
		return username;
	}

	public String searchHome() {

		System.out.println("TABLENAME :" + TABLENAME);
		trace("*************** CardMaintainAction Begins **********");
		enctrace("*************** CardMaintainAction Begins **********");
		String instid = comInstId();
		HttpSession session = getRequest().getSession();

		try {
			int reqch = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if (reqch < 0) {
				return "required_home";
			}

			int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);
			if (maint < 0) {
				return "required_home";
			}

			String cardmaintdesc = "";

			String act = getRequest().getParameter("act");
			if (act != null) {
				session.setAttribute("act", act);
			}

			String mact = getRequest().getParameter("mact");
			setMact(mact);

			if (Integer.parseInt(mact) != 0) {
				cardmaintdesc = commondesc.getcardmaintaindesc(instid, mact, jdbctemplate);
				System.out.println("getting maintain desc value" + cardmaintdesc);

			}
			session.setAttribute("maintdesc", cardmaintdesc);
			session.setAttribute("maintcode", mact);
		}

		catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the process....");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "serach_home";
	}

	/*
	 * public void searchList() throws Exception{ trace("Searchig list....");
	 * enctrace("Searchig list...."); String instid = comInstId(); HttpSession
	 * session = getRequest().getSession();
	 * 
	 * String cardno = commondesc.escSql(
	 * getRequest().getParameter("cardno").trim() ); trace("cardno : " + cardno
	 * ); String accountno = "";//commondesc.escSql(
	 * getRequest().getParameter("accountno").trim() ); trace("accountno : " +
	 * accountno );
	 * 
	 * String custno = "";//commondesc.escSql(
	 * getRequest().getParameter("custno").trim() ); trace("custno::"+custno);
	 * String dob = "";//commondesc.escSql(
	 * getRequest().getParameter("dob").trim() ); trace("dob::"+dob); String
	 * custname = "";//commondesc.escSql(
	 * getRequest().getParameter("custname").trim() );
	 * trace("custname::"+custname); String mobileno = "";//commondesc.escSql(
	 * getRequest().getParameter("mobileno").trim() );
	 * trace("mobileno::"+mobileno); String qrycond = ""; String searchqry = "";
	 * Boolean crdbase = false, custbase = false; String cardnocond="",
	 * custnocond="", dobcond="", custnamecond="", orderrecond = "",
	 * mobilenocond="";
	 * 
	 * String branchcode = comBranchId();
	 * 
	 * String branchCondition =""; String usertype= comuserType();
	 * 
	 * System.out.println("usertype::"+usertype);
	 * 
	 * 
	 * String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
	 * 
	 * String keyid = ""; String EDMK="", EDPK=""; StringBuffer hcardno = new
	 * StringBuffer(); String ecardno =""; PadssSecurity padsssec = new
	 * PadssSecurity(); if(padssenable.equals("Y")) {
	 * 
	 * keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
	 * System.out.println("keyid::"+keyid); List secList =
	 * commondesc.getPADSSDetailById(keyid, jdbctemplate);
	 * System.out.println("secList::"+secList); Iterator secitr =
	 * secList.iterator(); if(!secList.isEmpty()){ while(secitr.hasNext()) { Map
	 * map = (Map) secitr.next(); String eDMK = ((String)map.get("DMK")); String
	 * eDPK = ((String)map.get("DPK")); hcardno =
	 * padsssec.getHashedValue(cardno+instid); ecardno = padsssec.getECHN(eDMK,
	 * eDPK, cardno); } } }
	 * 
	 * trace("hcardno :::"+hcardno); trace("ecardno :::"+ecardno);
	 * 
	 * 
	 * if( cardno != "" && !cardno.equals("null") ){ crdbase = true;
	 * if(usertype.equals("BRANCHUSER")) { if(padssenable.equals("Y")) {
	 * cardnocond = "AND ( HCARD_NO = '"+hcardno.toString()+
	 * "' AND BRANCH_CODE = '" +branchcode+"')"; } else { cardnocond =
	 * "AND ( HCARD_NO = '"+cardno+"' AND BRANCH_CODE = '"+branchcode+"')"; }
	 * 
	 * } else { cardnocond = "AND ( HCARD_NO = '"+hcardno+"' )"; } }
	 * 
	 * if( accountno != "" && !accountno.equals("null") ){
	 * 
	 * cardno = commondesc.getCardnofromprod(accountno, instid, jdbctemplate);
	 * crdbase = true; if(usertype.equals("BRANCHUSER")) { cardnocond =
	 * "AND ( ACCOUNT_NO LIKE '%" +accountno+"' AND BRANCH_CODE = '"
	 * +branchcode+"')"; } else { cardnocond = "AND ( ACCOUNT_NO LIKE '%"
	 * +accountno+"' )"; } }
	 * 
	 * if( custno != "" && !custno.equals("null") ){ crdbase = true; cardno =
	 * commondesc.getCardnofromprodUsingcustid(custno, instid, jdbctemplate);
	 * if(usertype.equals("BRANCHUSER")) { cardnocond = "AND CIN LIKE '%"
	 * +custno+"%' AND BRANCH_CODE = '"+branchcode+"')"; } else { custnocond =
	 * " AND CIN LIKE '%"+custno+"%'"; } }
	 * 
	 * if( mobileno != "" && !mobileno.equals("null") ){ crdbase =
	 * true;getcardmaintaindesc if(usertype.equals("BRANCHUSER")) { cardnocond =
	 * "AND MOBILENO LIKE '%"+mobileno+"' AND BRANCH_CODE = '"+branchcode+"')";
	 * } else { mobilenocond = "AND MOBILENO LIKE '%"+mobileno+"'"; }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * if( dob != "" && !dob.equals("null") ){ custbase = true; cardno =
	 * commondesc.getCardnofromprodUsingcustid(custno, instid, jdbctemplate);
	 * //dobcond = " AND DOB LIKE '%"+dob+"%'"; dobcond = " AND dob = to_date('"
	 * +dob+"', 'dd-mm-yyyy')"; }
	 * 
	 * 
	 * if( custname != "" && !custname.equals("null") ){ custbase = true;
	 * custnamecond = " AND   (  upper(FNAME) LIKE '%"+custname.toUpperCase()+
	 * "%'  OR upper(MNAME) LIKE '%" +custname.toUpperCase()+
	 * "%' OR upper(LNAME) LIKE '%" +custname.toUpperCase()+"%'  ) "; }
	 * 
	 * <div id="fw_container"> <table id="example" border="0" cellpadding="0"
	 * width="95%" cellspacing="0" class="pretty">
	 * 
	 * //class='searchres' String result_hd =
	 * "<div id='fw_container'>	<table border='0' cellpadding='0' cellspacing='0' width='100%' class='pretty' style='border: 1px solid #454454' >"
	 * ; result_hd +=
	 * "<tr><td colspan='11'><span style='color:gray;font-weight:bold'> Click on the card number to continue the maintenence activities </span> </td></tr>"
	 * ; result_hd +=
	 * "<tr> <th>Card No</th> <th>ACCOUNT NO</th><th>Mobile No.</th>  <th>Order Ref No</th> <th>EMBOSSING NAME</th> <th> DOB </th> <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRODUCT</th>    </tr>"
	 * ; //<th>Sub-Product</th> String trclass = ""; String result = ""; String
	 * finalresult = ""; Boolean header_val = false; String card_dataqry =
	 * " SELECT * FROM " +TABLENAME+"  WHERE ROWNUM <= 50 AND INST_ID='"
	 * +instid+"'" ; enctrace("card_dataqry is:"+card_dataqry); if( crdbase ){
	 * 
	 * searchqry = card_dataqry + cardnocond + custnocond + orderrecond +
	 * mobilenocond;
	 * 
	 * 
	 * 
	 * enctrace( "searchqry__kum" + searchqry); if(padssenable.equals("Y")) {
	 * result = this.generateSearchList(instid,
	 * hcardno.toString(),ecardno,searchqry, jdbctemplate, session); } else {
	 * result = this.generateSearchList(instid, cardno,"",searchqry,
	 * jdbctemplate, session); } System.out.println( "result__1" + result ); if(
	 * result.equals("NOREC")){ //result_hd +=
	 * "<thead><tr><th>Card No</th> <th>Mobile No</th> <th>Order Ref No</th> <th>CUST NAME</th>  <th> DOB </th>   <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRODUCT</th>   </tr></thead>"
	 * ; result =
	 * "<div style='text-align:center;color:red'>NO RECORDS FOUND </div>";
	 * 
	 * } else if(result.equals("SWITCHNOTMATCH")) { result =
	 * "<div style='text-align:center;color:red'>STATUS NOT MATCHED FROM SWITCH </div>"
	 * ; } else{ result = result_hd + result ; header_val = true;
	 * 
	 * } }
	 * 
	 * 
	 * if (custbase){ String custdataqry =
	 * "SELECT * FROM EZCUSTOMERINFO WHERE INSTID='"+instid+"'"; enctrace(
	 * "custdataqry is:"+custdataqry); searchqry = custdataqry + custnocond +
	 * dobcond + custnamecond ; enctrace("searchqry is:"+searchqry); List
	 * custdatalist = jdbctemplate.queryForList(searchqry); trace(
	 * "custdatalist is:"+custdatalist.size()); if( !custdatalist.isEmpty() ){
	 * Iterator custitr = custdatalist.iterator(); boolean alt = true; while(
	 * custitr.hasNext() ){ Map cmp = (Map)custitr.next(); custno =
	 * (String)cmp.get("CUSTID"); custnocond = " AND CIN LIKE '%"+custno+"%'";
	 * String searchqry_new = card_dataqry + custnocond; enctrace(
	 * "searchqry_new__" + searchqry_new); finalresult =
	 * this.generateSearchList(instid, cardno,"ecardno",searchqry_new,
	 * jdbctemplate, session); trace("result is:"+result); System.out.println(
	 * "result__2" + result );
	 * 
	 * if( !result.equals("NOREC") ){ if( !header_val){ result_hd +=
	 * "<tr> <th>Card No</th> <th>Order Ref No</th> <th>CUST NAME</th> <th> DOB </th> <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRODUCT</th>   </tr>"
	 * ; } result = result_hd + finalresult ; } else
	 * if(result.equals("SWITCHNOTMATCH")) { result =
	 * "<div style='text-align:center:color:red'>STATUS NO MATCHED FROM SWITCH </div>"
	 * ; } else{ result =
	 * "<div style='text-align:center:color:red'>NO RECORDS FOUND </div>"; }
	 * 
	 * trace("searchqry_new__"+searchqry_new); }
	 * 
	 * }else{ result =
	 * "<div style='text-align:center;color:red'>NO RECORDS FOUND </div>"; } }
	 * 
	 * result += "</table></div>";
	 * 
	 * System.out.println("^^^^^^^^^^^^^^^^^^^"); System.out.println(
	 * "Final Result : " + result ); System.out.println("^^^^^^^^^^^^^^^^^^^");
	 * getResponse().getWriter().write(result); trace("searchqry is:"
	 * +searchqry); //System.out.println(searchqry);
	 * 
	 * 
	 * }
	 */

	public void searchList() throws Exception {
		trace("======searching the list===========");
		String result = "";

		/*
		 * // by siva 210819 HttpSession ses = getRequest().getSession();
		 * System.out.println("check ok 1"); String sessioncsrftoken = (String)
		 * ses.getAttribute("csrfToken"); System.out.println("check ok 1"
		 * +sessioncsrftoken); String jspcsrftoken =
		 * getRequest().getParameter("token"); System.out.println("check ok 1"
		 * +jspcsrftoken); if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken))
		 * { ses.setAttribute("message", "CSRF Token Mismatch");
		 * System.out.println("check ok 1"); addActionError(
		 * "CSRF Token Mismatch"); System.out.println("check ok 2"); result =
		 * "<div style='text-align:center;color:red'>CSRF Token Mismatch </div>"
		 * ; getResponse().getWriter().write(result); System.out.println(
		 * "check ok 3"); } // by siva 210819
		 */

		PadssSecurity padsssec = new PadssSecurity();
		trace("Searchig list....");
		enctrace("Searchig list....");

		String instid = comInstId();
		HttpSession session = getRequest().getSession();
		// String mact = (String) session.getAttribute("mact");
		// trace("getting mact value " + mact);
		String selectedtype = (String) getRequest().getParameter("selectedtype");
		String maintdesc = (String) getRequest().getParameter("maintdesc");
		trace("card maintanence : " + maintdesc);
		String cardno = commondesc.escSql(getRequest().getParameter("cardno").trim());
		// trace("cardno::::::: : " + cardno + " selected type ::::::::::::: " +
		// selectedtype);
		mcardno = padsssec.getMakedCardno(cardno);
		trace("cardnumber :::::::  "+cardno);
		String accountno = "";
		trace("accountno : " + accountno);
		StringBuffer hcardno = new StringBuffer();
		String custno = "";
		trace("custno::" + custno);
		String dob = "";
		trace("dob::" + dob);
		// String custname = "";
		// trace("custname::" + custname);
		String mobileno = "";
		trace("mobileno::" + mobileno);
		String qrycond = "";
		String searchqry = "";
		Boolean crdbase = false, custbase = false;
		String cardnocond = "", custnocond = "", dobcond = "", custnamecond = "", orderrecond = "", mobilenocond = "";

		String branchcode = comBranchId();

		StringBuilder sb = null;

		String branchCondition = "";
		String usertype = comuserType();

		System.out.println("usertype::" + usertype);
		String ecardno = "";
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		// trace("checking edpk--->"+EDPK);

		// ACCT
		if (selectedtype.equals("acctnum")) {

			trace("====== searching for Account number based===== ");
			if (cardno != "" && !cardno.equals("null")) {

				List cardlist = commondesc.getCardnofromprod(cardno, instid, jdbctemplate);
				// System.out.println(cardno);
				if (!cardlist.isEmpty()) {
					HashMap cardmap = (HashMap) cardlist.get(0);
					ecardno = (String) cardmap.get("ORG_CHN");
					// trace("account number based ECARD" + ecardno);
					hcardno.append(cardmap.get("HCARD_NO"));
					trace("account number based hcardno" + hcardno);
					String mcard_no = (String) cardmap.get("MCARD_NO");
					trace("account number based MCARD " + mcard_no);
				}

				crdbase = true;
				if (usertype.equals("BRANCHUSER")) {
					if (maintdesc.equalsIgnoreCase("LOST")) {

						cardnocond = "AND ( ACCOUNT_NO LIKE '%" + cardno + "')";
					} else {

						cardnocond = "AND ( ACCOUNT_NO LIKE '%" + cardno + "' AND trim(BRANCH_CODE) = '" + branchcode
								+ "')";
					}

				} else {
					cardnocond = "AND ( ACCOUNT_NO LIKE '%" + cardno + "' )";
				}
			}
		}

		// CUST ID
		if (selectedtype.equals("custnum")) {
			trace("====== searching for CustID based===== ");
			if (cardno != "" && !cardno.equals("null")) {
				List cardlist = commondesc.getCardnofromprodUsingcustid(cardno, instid, jdbctemplate);
				trace("cardno---->" + cardlist);
				if (!cardlist.isEmpty()) {
					HashMap cardmap = (HashMap) cardlist.get(0);
					ecardno = (String) cardmap.get("ORG_CHN");
					/* ecardno = (String) cardmap.get("CARD_NO"); */
					hcardno.append(cardmap.get("HCARD_NO"));
				}
				crdbase = true;
				if (usertype.equals("BRANCHUSER")) {
					if (maintdesc.equalsIgnoreCase("LOST")) {
						cardnocond = "AND CIN='" + cardno + "'";
					} else {
						cardnocond = "AND CIN='" + cardno + "' AND TRIM(BRANCH_CODE) = '" + branchcode + "')";
					}
				}

				else {
					custnocond = " AND CIN='" + cardno + "'";
				}
			}
		}

		/*
		 * // not using this if (selectedtype.equals("Phone Number")) {
		 * System.out.println("ssssssss"); crdbase = true; if
		 * (usertype.equals("BRANCHUSER")) { System.out.println("dsad"); if
		 * (maintdesc.equalsIgnoreCase("LOST")) { cardnocond =
		 * "AND MOBILENO LIKE '%" + mobileno + "'"; } else {
		 * 
		 * cardnocond = "AND MOBILENO LIKE '%" + mobileno +
		 * "' AND TRIM(BRANCH_CODE) = '" + branchcode.trim() + "')"; }
		 * 
		 * } else { mobilenocond = "AND MOBILENO LIKE '%" + mobileno + "'"; }
		 * 
		 * }
		 */

		/*
		 * // in use if (selectedtype.equals("Phone Number")) {
		 * System.out.println("ssssssss"); crdbase = true;
		 * 
		 * if (maintdesc.equalsIgnoreCase("LOST")) { cardnocond =
		 * "AND MOBILENO LIKE '%" + cardno + "'"; } else {
		 * 
		 * cardnocond = "AND MOBILENO LIKE '%" + cardno + "' "; }
		 * 
		 * }
		 * 
		 * 
		 * if (custname != "" && !custname.equals("null")) { custbase = true;
		 * custnamecond = " AND   (  upper(FNAME) LIKE '%" +
		 * custname.toUpperCase() + "%'  OR upper(MNAME) LIKE '%" +
		 * custname.toUpperCase() + "%' OR upper(LNAME) LIKE '%" +
		 * custname.toUpperCase() + "%'  ) "; }
		 */

		// CARD NUM
		trace("inst id is ==========   " + instid);
		if (selectedtype.equals("cardnum")) {

			trace("====== searching for cardnum  based===== ");
			String keyid = "";
			// String EDMK = "", EDPK = "";

			if (padssenable.equals("Y")) {
				keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::" + keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				// System.out.println("secList::" + secList);
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String CDMK = ((String) map.get("DMK"));
						// String eDPK = ((String) map.get("DPK"));
						// trace("checking cdmk-->"+CDMK);
						hcardno = padsssec.getHashedValue(cardno + instid);
						// ecardno = padsssec.getECHN(eDMK, eDPK, cardno);
						trace("checking hcardno-->" + hcardno);
						String CDPK = padsssec.decryptDPK(CDMK, EDPK);
						// trace("checking CDPK-->"+CDPK);
						ecardno = padsssec.getECHN(CDPK, cardno);
						// trace("checking ecardno-->" + ecardno);
					}
				}
			}

			if (cardno != "" && !cardno.equals("null")) {
				crdbase = true;

				if (usertype.equals("BRANCHUSER")) {
					if (maintdesc.equalsIgnoreCase("LOST")) {
						if (padssenable.equals("Y")) {
							cardnocond = "AND ( order_ref_no =(select order_ref_no from card_production_hash where  HCARD_NO = '"
									+ hcardno.toString() + "'))";
						} else {
							cardnocond = "AND ( order_ref_no =(select order_ref_no from card_production_hash where  HCARD_NO = '"
									+ hcardno + "'))";
						}
					} else {

						if (padssenable.equals("Y")) {
							cardnocond = "AND ( order_ref_no =(select order_ref_no from card_production_hash where  HCARD_NO = '"
									+ hcardno.toString() + "') AND TRIM(BRANCH_CODE) = '" + branchcode + "')";
						} else {
							cardnocond = "AND ( order_ref_no =(select order_ref_no from card_production_hash where  HCARD_NO = '"
									+ hcardno + "') AND TRIM(BRANCH_CODE) = '" + branchcode + "')";
						}
					}
				}

				else {
					cardnocond = "AND order_ref_no =(select order_ref_no from card_production_hash where  HCARD_NO = '"
							+ hcardno + "' )";
				}
			}
		}

		if (maintdesc.equals("CLOSE")) {
			String cardstatus = commondesc.checkcardavailabelclose(instid, hcardno.toString(), jdbctemplate);
			if ((cardstatus != "LOST / STOLEN") || (cardstatus != "ACTIVE")) {
				System.out.println("cardstatus CLOSE" + cardstatus);
				// addActionError(" Card is Currently "+cardstatus+
				// "Mark Card As Lost/Stolen AND ACTIVE to Close the card");
				result = "<div style='text-align:center;color:red'>Card is Currently :" + cardstatus
						+ ",  Mark Card As Lost/Stolen OR ACTIVE to Close the card </div>";
				getResponse().getWriter().write(result);
			}

		}

		else if (maintdesc.equals("REISSUE")) {
			int checckcardcountforReissue = commondesc.checckcardcountforReissue(instid, hcardno.toString(),
					jdbctemplate);
			if (checckcardcountforReissue <= 0) {
				// addActionError("No Record Found");
				result = "<div style='text-align:center;color:red'>"

						+ " NO RECORD FOUND </div>";
				getResponse().getWriter().write(result);
			} else {
				String cardstatus = commondesc.checkcardavailabelclose(instid, hcardno.toString(), jdbctemplate);

				if ((cardstatus != "LOST / STOLEN")) {
					System.out.println("cardstatus Reissue" + cardstatus);
					// addActionError(" Card is Currently "+cardstatus+
					// "Mark Card As Lost/Stolen AND ACTIVE to Close the card");
					result = "<div style='text-align:center;color:red'>Card is Currently :" + cardstatus
							+ ",  Mark Card As Lost/Stolen  to REISSUE the card </div>";
					getResponse().getWriter().write(result);
				}
			}
		}

		else {
			// class='searchres'
			String result_hd = "<div id='fw_container'>	<table border='0' cellpadding='0' cellspacing='0' width='100%' class='pretty' style='border: 1px solid #454454' >";
			result_hd += "<tr><td colspan='11'><span style='color:gray;font-weight:bold'> Click on the card number to continue the maintenence activities </span> </td></tr>";
			if ("acctnum".equalsIgnoreCase(selectedtype)) {
				result_hd += "<tr> <th>Card No</th> <th>ACCOUNT NO</th><th>Mobile No.</th>  <th>CUST NAME</th> <th> CUSTOMER ID </th>  <th>CARD STATUS</th> <th>PRODUCT</th> <th>BRANCH</th><th>ACCOUNT</th>  </tr>";
			} else {
				result_hd += "<tr> <th>Card No</th> <th>ACCOUNT NO</th><th>Mobile No.</th>  <th>Order Ref No</th> <th>CUST NAME</th> <th> REG DATE </th> <th> CUSTOMER ID </th> <th>BIN</th> <th>BRANCH</th>  <th>CARD STATUS</th> <th>PRE FILE</th> <th> EXP DATE </th> </tr>";
			}
			// <th>Sub-Product</th>
			String trclass = "";

			String finalresult = "";
			Boolean header_val = false;
			String card_dataqry = " SELECT * FROM " + TABLENAME + "  WHERE ROWNUM <= 50 AND INST_ID='" + instid + "'";
			enctrace("card_dataqry is:" + card_dataqry);

			if (crdbase) {
				// searchqry = card_dataqry + cardnocond + custnocond +
				// orderrecond + mobilenocond;

				searchqry = card_dataqry + cardnocond + custnocond + orderrecond + mobilenocond;
				enctrace("searchqry__kum 11111111" + searchqry);

				/*
				 * if (padssenable.equals("Y")) {
				 */

				trace("hcardno :::" + hcardno);
				// trace("ecardno :::" + ecardno);

				if ("acctnum".equalsIgnoreCase(selectedtype)) {
					result = this.generateSearchListForAcctNo(instid, cardno, hcardno.toString(), ecardno, searchqry,
							selectedtype, jdbctemplate, session);
					trace("result account number  Based:::    " + result);
				}

				else {
					result = this.generateSearchList(instid, hcardno.toString(), ecardno, searchqry, selectedtype,
							jdbctemplate, session);
					trace("2");
				}

				// by siva 12 -07 -19
				/*
				 * }
				 * 
				 * else { if ("acctnum".equalsIgnoreCase(selectedtype)) { result
				 * = this.generateSearchListForAcctNo(instid, cardno,
				 * cardno,ecardno , searchqry, selectedtype, jdbctemplate,
				 * session); trace("3"); } else { result =
				 * this.generateSearchList(instid, cardno, "", searchqry,
				 * selectedtype, jdbctemplate, session); } }
				 */
				trace("result__1" + result);
				if (result.equals("NOREC")) {
					// result_hd +=
					// "<thead><tr><th>Card No</th> <th>Mobile No</th> <th>Order
					// Ref No</th> <th>CUST NAME</th> <th> DOB </th> <th>
					// CUSTOMER ID </th> <th>BIN</th> <th>CARD STATUS</th>
					// <th>PRODUCT</th> </tr></thead>";
					result = "<div style='text-align:center;color:red'>NO RECORDS FOUND </div>";

				} else if (result.equals("SWITCHNOTMATCH")) {
					result = "<div style='text-align:center;color:red'>STATUS NOT MATCHED FROM SWITCH </div>";
				} else {
					result = result_hd + result;
					header_val = true;

				}
			}

			if (custbase) {
				String custdataqry = "SELECT * FROM EZCUSTOMERINFO WHERE INSTID='" + instid + "'";
				enctrace("custdataqry is:" + custdataqry);
				searchqry = custdataqry + custnocond + dobcond + custnamecond;
				enctrace("searchqry is:" + searchqry);
				List custdatalist = jdbctemplate.queryForList(searchqry);
				trace("custdatalist is:" + custdatalist.size());
				if (!custdatalist.isEmpty()) {
					Iterator custitr = custdatalist.iterator();
					boolean alt = true;
					while (custitr.hasNext()) {
						Map cmp = (Map) custitr.next();
						custno = (String) cmp.get("CUSTID");
						custnocond = " AND CIN LIKE '%" + custno + "%'";
						String searchqry_new = card_dataqry + custnocond;
						enctrace("searchqry_new__" + searchqry_new);
						finalresult = this.generateSearchList(instid, cardno, "ecardno", searchqry_new, selectedtype,
								jdbctemplate, session);
						trace("result is:" + result);
						System.out.println("result__2" + result);

						if (!result.equals("NOREC")) {
							/*
							 * if( !header_val){ result_hd +=
							 * "<tr> <th>Card No</th> <th>Order Ref No</th> <th>CUST NAME</th> <th> DOB </th> <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRODUCT</th>   </tr>"
							 * ; }
							 */
							result = result_hd + finalresult;
						} else if (result.equals("SWITCHNOTMATCH")) {
							result = "<div style='text-align:center:color:red'>STATUS NO MATCHED FROM SWITCH </div>";
						} else {
							result = "<div style='text-align:center:color:red'>NO RECORDS FOUND </div>";
						}

						trace("searchqry_new__" + searchqry_new);
					}

				}
			}

			result += "</table></div>";

			System.out.println("^^^^^^^^^^^^^^^^^^^");
			System.out.println("Final Result : " + result);
			System.out.println("^^^^^^^^^^^^^^^^^^^");
			// response.("text/html; charset=UTF-8");

			getResponse().getWriter().write(result);
			trace("searchqry is:" + searchqry);
			// System.out.println(searchqry);

		}

		// added by gowtham_060819
		// nullify

		// System.out.println(cardno);

		cardno = "0000000000000000";
		sb = new StringBuilder(cardno);
		sb.setLength(0);
		cardno = null;
		sb = null;

		System.out.println(cardno);

	}

	/******************************
	 * SEARCHING TRANACTION
	 ***********************/
	public void searchListOfTxn() throws Exception {
		trace("Searchig list....");
		enctrace("Searchig list....");
		String instid = comInstId();
		HttpSession session = getRequest().getSession();

		String cardno = commondesc.escSql(getRequest().getParameter("cardno").trim());
		// trace("cardno : " + cardno);
		String orderrefno = commondesc.escSql(getRequest().getParameter("orderrefno").trim());
		trace("orderrefno : " + orderrefno);
		String custno = commondesc.escSql(getRequest().getParameter("custno").trim());
		String dob = commondesc.escSql(getRequest().getParameter("dob").trim());
		String custname = commondesc.escSql(getRequest().getParameter("custname").trim());
		String mobileno = commondesc.escSql(getRequest().getParameter("mobileno").trim());
		String qrycond = "";
		String searchqry = "";
		Boolean crdbase = false, custbase = false;
		String cardnocond = "", custnocond = "", dobcond = "", custnamecond = "", orderrecond = "", mobilenocond = "";

		/*
		 * if( cardno != "" && !cardno.equals("null") ){ crdbase = true;
		 * cardnocond = "AND (CARD_NO  = '"+cardno+"' )";
		 * 
		 * }
		 * 
		 * if( mobileno != "" && !mobileno.equals("null") ){ crdbase = true;
		 * mobilenocond = "AND MOBILENO LIKE '%"+mobileno+"'";
		 * 
		 * }
		 * 
		 * trace("searchqry__"+searchqry); //System.out.println( "searchqry__" +
		 * searchqry ); trace("orderrefno__"+orderrefno+"__end");
		 * //System.out.println( "orderrefno__"+orderrefno+"__end");
		 * 
		 * if( orderrefno != "" && !orderrefno.equals("null") ){ crdbase = true;
		 * orderrecond = " AND ORDER_REF_NO LIKE '%"+orderrefno+"%'"; }
		 * 
		 * if( custno != "" && !custno.equals("null") ){ crdbase = true;
		 * custnocond = " AND CIN LIKE '%"+custno+"%'"; }
		 * 
		 * if( dob != "" && !dob.equals("null") ){ custbase = true; //dobcond =
		 * " AND DOB LIKE '%"+dob+"%'"; dobcond = " AND dob = to_date('"+dob+
		 * "', 'dd-mm-yyyy')"; }
		 * 
		 * 
		 * if( custname != "" && !custname.equals("null") ){ custbase = true;
		 * custnamecond = " AND upper(FNAME) LIKE '%"
		 * +custname.toUpperCase()+"%'"; }
		 */
		/*
		 * <div id="fw_container"> <table id="example" border="0"
		 * cellpadding="0" width="95%" cellspacing="0" class="pretty">
		 */

		// class='searchres'
		String result_hd = "<div id='fw_container'>	<table border='0' cellpadding='0' cellspacing='0' width='100%' class='pretty' style='border: 1px solid #454454' >";
		result_hd += "<tr> <th>Card No</th> <th>Mobile No.</th>  <th>Order Ref No</th> <th>CUST NAME</th> <th> DOB </th> <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRODUCT</th> <th>Sub-Product</th>   </tr>";
		String trclass = "";
		String result = "";
		Boolean header_val = false;
		String card_dataqry = " SELECT * FROM " + TABLENAME + "  WHERE ROWNUM <= 50 AND INST_ID='" + instid + "'";
		enctrace("card_dataqry is:" + card_dataqry);

		String selectedtype = "";
		crdbase = true; // /checking......
		if (crdbase) {

			searchqry = card_dataqry + cardnocond + custnocond + orderrecond + mobilenocond;
			enctrace("searchqry__jo" + searchqry);

			result = this.generateSearchList(instid, cardno, "ecardno", searchqry, selectedtype, jdbctemplate, session);

			System.out.println("result__1" + result);
			if (!result.equals("NOREC")) {
				// result_hd +=
				// "<thead><tr><th>Card No</th> <th>Mobile No</th> <th>Order Ref
				// No</th> <th>CUST NAME</th> <th> DOB </th> <th> CUSTOMER ID
				// </th> <th>BIN</th> <th>CARD STATUS</th> <th>PRODUCT</th>
				// </tr></thead>";
				result = result_hd + result;
				header_val = true;

			} else {
				result = "<div style='text-align:center;color:red'>NO RECORDS FOUND </div>";
			}
		} else {
			result = "<div style='text-align:center;color:red'>NO RECORDS FOUND </div>";
		}

		if (custbase) {
			String custdataqry = "SELECT * FROM IFP_CUSTINFO_PRODUCTION WHERE INST_ID='" + instid + "'";
			enctrace("custdataqry is:" + custdataqry);
			searchqry = custdataqry + custnocond + dobcond + custnamecond;
			enctrace("searchqry is:" + searchqry);
			List custdatalist = jdbctemplate.queryForList(searchqry);
			trace("custdatalist is:" + custdatalist.size());
			if (!custdatalist.isEmpty()) {
				Iterator custitr = custdatalist.iterator();
				boolean alt = true;
				while (custitr.hasNext()) {
					Map cmp = (Map) custitr.next();
					custno = (String) cmp.get("CIN");
					custnocond = " AND CIN LIKE '%" + custno + "%'";
					String searchqry_new = card_dataqry + custnocond;
					enctrace("searchqry_new__" + searchqry_new);

					result = this.generateSearchList(instid, cardno, "ecardno", searchqry_new, selectedtype,
							jdbctemplate, session);

					trace("result is:" + result);
					System.out.println("result__2" + result);

					if (!result.equals("NOREC")) {
						/*
						 * if( !header_val){ result_hd +=
						 * "<tr> <th>Card No</th> <th>Order Ref No</th> <th>CUST NAME</th> <th> DOB </th> <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRODUCT</th>   </tr>"
						 * ; }
						 */
						result = result_hd + result;
					} else {
						result = "<div style='text-align:center:color:red'>NO RECORDS FOUND </div>";
					}

					trace("searchqry_new__" + searchqry_new);
				}

			} else {
				result = "<div style='text-align:center;color:red'>NO RECORDS FOUND </div>";
			}
		}

		result += "</table></div>";

		System.out.println("^^^^^^^^^^^^^^^^^^^");
		System.out.println("Final Result : " + result);
		System.out.println("^^^^^^^^^^^^^^^^^^^");
		getResponse().getWriter().write(result);
		trace("searchqry is:" + searchqry);
		// System.out.println(searchqry);

	}

	public String generateSearchList(String instid, String cardno, String ecardno, String qry, String selectedtype,
			JdbcTemplate jdbctemplate, HttpSession session) throws Exception {

		trace("search based on cardnumber or cin ::::  "+selectedtype);
		
		/*
		 * Properties props=getCommonDescProperty(); String
		 * EDPK=props.getProperty("EDPK"); trace("EDPK-->"+EDPK);
		 */
		PadssSecurity padsssec = new PadssSecurity();
		String keyid="",CDPK="";
		  Properties props = getCommonDescProperty();
			String EDPK = props.getProperty("EDPK");
		 trace("edpk---->" + EDPK);
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			// System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDMK = ((String) map.get("DMK"));
					// System.out.println("CDMK-->" + CDMK);
					// String eDPK = ((String) map.get("DPK"));
					 CDPK = padsssec.decryptDPK(CDMK, EDPK);
					trace("CDPK-->" + CDPK);
				}
			}

		int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);

		if (maint < 0) {
			return "required_home";
		}
		// System.out.println("coming cardno:::" + cardno);

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		System.out.println("padssenable:::" + padssenable);
	
		String datatable = "";
		String hashedCardno="";
		String clearCardNumber="";
		String card_status_desc = "";
		trace("query is :::  "+qry);
		String cardnumber = "", accountrelation = "";
		List card_data = jdbctemplate.queryForList(qry);

		if (!card_data.isEmpty()) {
			Iterator itr = card_data.iterator();
			boolean alt = true;
			int cnt = 0;
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				cardnumber = (String) mp.get("ORG_CHN");
				String mcardno = (String) mp.get("MCARD_NO");
			
				clearCardNumber = padsssec.getCHN(CDPK, cardnumber);		 
				hashedCardno = padsssec.getHashedValue(clearCardNumber + instid).toString();
			
				trace("cardnumber-->" + clearCardNumber  +"  hashedCardno  "+hashedCardno);
				String accountno = (String) mp.get("ACCOUNT_NO");
				String order_refno = (String) mp.get("ORDER_REF_NO");
				String bin = (String) mp.get("BIN");
				String brcode = (String) mp.get("CARD_COLLECT_BRANCH");
				String brdec = commondesc.getBranchDesc(instid, brcode, jdbctemplate);
				String brcodedes = brcode + "-" + brdec;
				Timestamp activedate = (Timestamp) mp.get("GENERATED_DATE");
				String prefile = (String) mp.get("PRE_FILE");
				if (prefile != null) {
					if (prefile.contains("_")) {
						String[] namesplit = prefile.split("_");
						String filedate = namesplit[2];
						String filetime = namesplit[3];
						filedate = filedate.substring(0, 4) + filedate.substring(6, 8);
						prefile = filedate + "-" + filetime;
					} else {
						prefile = "MIGRATED";
					}
				} else {
					prefile = "--";
				}

				SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-YYYY");
				StringBuilder actdate = new StringBuilder(fmt.format(activedate));
				String bindesc = commondesc.getCardTypeDesc(instid, bin, jdbctemplate);
				String status_code = commondesc.getSTATUSFromEZLINK(instid, hashedCardno, jdbctemplate);
				System.out.println("status_code---> " + status_code);
				if (status_code == null || status_code.equals("")) {
					datatable = "SWITCHNOTMATCH";
					return datatable;
				}

				// String clearchn = padsssec.getCHN(eDMK, eDPK, cardnumber);
				/*
				 * String CDPK = padsssec.decryptDPK(eDMK, EDPK); String
				 * clearchn = padsssec.getCHN(CDPK,dcardno );
				 */

				trace("cardnumber changed");
				String product_code = commondesc.getProductdesc(instid, (String) mp.get("PRODUCT_CODE"), jdbctemplate);
				String custid = (String) mp.get("CIN");
				// String cafrec = (String) mp.get("CAF_REC_STATUS");
				String mobileno = (String) mp.get("MOBILENO");
				// String org_chn = (String) mp.get("ORG_CHN");
				// String subproduct = (String)
				// commondesc.getSubProductdesc(instid, (String)
				// mp.get("SUB_PROD_ID"), jdbctemplate);
				if (mobileno == null) {
					mobileno = "--";
				}
				System.out.println("MOBILE NUMBER_____________" + mobileno);
				System.out.println("custid__" + custid);
				Object dateofbirth;
				// String customername;
				// String subproductid = (String) mp.get("SUB_PROD_ID");

				dateofbirth = (Object) commondesc.fchCustDOB(instid, custid, jdbctemplate);
				if (dateofbirth == null) {
					dateofbirth = "--";
				}
				customername = (String) commondesc.fchCustName(instid, custid, jdbctemplate);
				String embName = (String) commondesc.fchEmbName1(instid, cardnumber, jdbctemplate);
				String EXPIRYDATEqry = "SELECT EXPIRYDATE,CHN FROM EZCARDINFO" + " WHERE INSTID='" + instid
						+ "' AND CHN=(select hcard_no from card_production_hash PROCESS_HASH where ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN='"
						+ cardnumber + "'))";
				trace("EXPIRYDATE :" + EXPIRYDATEqry);
				// System.out.println("EXPIRYDATEqry"+EXPIRYDATEqry);
				List expiry = jdbctemplate.queryForList(EXPIRYDATEqry);
				Iterator expirydetails = expiry.iterator();
				Date expdate = null;
				Timestamp edate = null;
				while (expirydetails.hasNext()) {
					Map map = (Map) expirydetails.next();
					expdate = (Date) map.get("EXPIRYDATE");
					edate = (Timestamp) map.get("EXPIRYDATE");
				}
				StringBuilder exdate = new StringBuilder(fmt.format(edate));
				// new SimpleDateFormat("dd-MM-YYYY").parse(acctno);

				// Date expdate = (Date) mp.get("EXPIRY_DATE");
				// Timestamp edate = (Timestamp) mp.get("EXPIRY_DATE");
				// StringBuilder exdate = new StringBuilder(fmt.format(edate));

				String cardstatuscode = commondesc.getCardStatusCode(instid, status_code, jdbctemplate);
				trace("checking card status code from maintain table " + cardstatuscode);
				String cardstatusdesc = commondesc.getCardStatusDesc(instid, cardstatuscode, jdbctemplate);
				card_status_desc = "<span style='color:red'>" + cardstatusdesc + "</span>";

				List<Map<String, Object>> accountrel = commondesc.getAccountdesc(instid, hashedCardno, accountno,
						jdbctemplate);
				if (accountrel.isEmpty()) {
					accountrel = commondesc.getAccountdescFromProd1(instid, cardnumber, accountno, jdbctemplate);
					if (accountrel.isEmpty()) {
						accountrelation = "--";
					} else {
						accountrelation = (String) accountrel.get(0).get("ACCOUNTDESC");
					}
				} else {
					accountrelation = (String) accountrel.get(0).get("ACCOUNTDESC");
				}
				if ("null".equalsIgnoreCase(accountrelation)) {
					accountrelation = "";
				}
				Date now = new Date();

				if (expdate.after(now)) {
					System.out.println("Not expired__" + expdate);
				} else {
					System.out.println("expired__" + expdate);
					int switchCardInfoUpdateloststol = cardmaindao.switchCardInfoUpdate(instid, hashedCardno, jdbctemplate);
					if (switchCardInfoUpdateloststol > 0) {

						card_status_desc = "<span style='color:red'> Reissued ExpiryCard </span>";

					} else {

						card_status_desc = "<span style='color:red'> EXPIRED </span>";

					}
				}
				String trclass = "";
				if (alt) {
					trclass = "class='alt'";
					alt = false;
				} else {
					alt = true;
				}
				cnt++;
				// "+trclass+"
				// onclick='showMaintain('"+instid+"','"+cardnumber+"')
				if (!"acctnum".equalsIgnoreCase(selectedtype)) {
					trace("selected type is :::  "+selectedtype);
					if (padssenable.equals("Y")) {
						datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
								+ "  <td  onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value) onclick=showMaintain('"
								+ instid + "','" + hashedCardno + "','" + mcardno
								/*
								 * + "','" + dcardno.toString() + "')   ><td>"
								 */ + "','" + clearCardNumber.toString() + "')   >" + "<td>" + mcardno + "</td>  <td>" + accountno + "</td><td>" + mobileno + "</td> <td>" + order_refno + "</td>  <td>" + embName + "</td> <td>" + actdate + "</td> <td>" + custid + "</td>  <td>" + bindesc + "</td> <td>" + brcodedes + " </td> <td>" + card_status_desc + "</td> <td>" + prefile + "</td> <td>" + exdate + "</td></tr>";
					} else {
						datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
								+ " <td onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value)  onclick=showMaintain('"
								+ instid + "','" + cardno + "','" + mcardno + "')   ><td>" + ecardno + "</td>  <td>"
								+ mcardno + "</td><td>" + mobileno + "</td> <td>" + order_refno + "</td>  <td>"
								+ embName + "</td> <td>" + actdate + "</td> <td>" + custid + "</td>  <td>" + bindesc
								+ "</td>  <td>" + bindesc + "</td><td>" + card_status_desc + "</td> <td>" + prefile
								+ "</td>  <td>" + exdate + "</td>     </tr>";
					}
				}

				else {
					datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass + " <td><td>" + mcardno
							+ "</td>  <td>" + accountno + "</td><td>" + mobileno + "</td>  <td>" + embName
							+ "</td> <td>" + custid + "</td> <td>" + bindesc + "</td> <td>" + card_status_desc
							+ "</td> <td>" + product_code + "</td>  <td>" + accountrelation + "</td>     </tr>";
				}

				// <td>"+ subproduct +"</td>

			}

		} else {
			datatable = "NOREC";
		}
		trace("datatable:::::::datatable" + datatable);
		return datatable;
	}

	String order_refno;
	String cardno;
	String enccardno;

	public String getEnccardno() {
		return enccardno;
	}

	public void setEnccardno(String enccardno) {
		this.enccardno = enccardno;
	}

	String account_no;
	private String mcardno;
	private String hcardno;
	private String padssenable;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	String bindesc;

	String product_code;
	String custid;
	List actypedesc;

	String customername;
	String expdate;

	String status_code_desc;

	List applicable_act_list;

	List cardcollectbranchlist;

	public List getCardcollectbranchlist() {
		return cardcollectbranchlist;
	}

	public void setCardcollectbranchlist(List cardcollectbranchlist) {
		this.cardcollectbranchlist = cardcollectbranchlist;
	}

	Boolean maintainallowed = false;
	Boolean maintainrequired;

	public Boolean getMaintainrequired() {
		return maintainrequired;
	}

	public void setMaintainrequired(Boolean maintainrequired) {
		this.maintainrequired = maintainrequired;
	}

	public Boolean getMaintainallowed() {
		return maintainallowed;
	}

	public void setMaintainallowed(Boolean maintainallowed) {
		this.maintainallowed = maintainallowed;
	}

	public List getApplicable_act_list() {
		return applicable_act_list;
	}

	public void setApplicable_act_list(List applicable_act_list) {
		this.applicable_act_list = applicable_act_list;
	}

	public String getCrd_status_desc() {
		return status_code_desc;
	}

	public void setCrd_status_desc(String status_code_desc) {
		this.status_code_desc = status_code_desc;
	}

	public String getOrder_refno() {
		return order_refno;
	}

	public void setOrder_refno(String order_refno) {
		this.order_refno = order_refno;
	}

	public String getBindesc() {
		return bindesc;
	}

	public void setBindesc(String bindesc) {
		this.bindesc = bindesc;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getExpdate() {
		return expdate;
	}

	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}

	/*
	 * public String showMaintain() {
	 * 
	 * trace("********* Show maintainence ********* "); enctrace(
	 * "********* Show maintainence ********* "); System.out.println(
	 * "maitaining the screeen"); System.out.println( "instid__" +
	 * getRequest().getParameter("instid")); System.out.println( "cardno__" +
	 * getRequest().getParameter("cardno")); HttpSession session =
	 * getRequest().getSession(); String instid =
	 * getRequest().getParameter("instid"); String cardno =
	 * getRequest().getParameter("cardno"); setCardno(cardno);
	 * 
	 * 
	 * 
	 * try{ int reqch= commondesc.reqCheck().requiredCheck(instid, session,
	 * jdbctemplate); if( reqch < 0 ){ return "required_home"; }
	 * 
	 * int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session,
	 * jdbctemplate); if( maint < 0 ){ return "required_home"; }
	 * 
	 * 
	 * 
	 * 
	 * List cardreclist = cardmaindao.getProdcutionCardDetails(instid, cardno,
	 * TABLENAME, jdbctemplate); if( !cardreclist.isEmpty() ){ Iterator itr =
	 * cardreclist.iterator(); while( itr.hasNext() ){ Map mp = ( Map )
	 * itr.next();
	 * 
	 * order_refno = (String)mp.get("ORDER_REF_NO");
	 * setOrder_refno(order_refno);
	 * 
	 * String bin = (String)mp.get("BIN"); bindesc =
	 * commondesc.getCardTypeDesc(instid, bin, jdbctemplate);
	 * setBindesc(bindesc);
	 * 
	 * String switch_status_code = (String)mp.get("STATUS_CODE"); String
	 * status_code = commondesc.getCardStatusCode(instid, switch_status_code,
	 * jdbctemplate); status_code_desc = commondesc.getCardStatusDesc( instid,
	 * status_code, jdbctemplate );
	 * 
	 * String cafrec = (String)mp.get("CAF_REC_STATUS");
	 * 
	 * product_code = commondesc.getProductdesc(instid,
	 * (String)mp.get("PRODUCT_CODE"),jdbctemplate) ;
	 * setProduct_code(product_code);
	 * 
	 * custid = (String)mp.get("CIN"); setCustid(custid);
	 * 
	 * String subproductid = (String)mp.get("SUB_PROD_ID"); String
	 * registeration_req = commondesc.checkRegisterRequired(instid,
	 * subproductid, jdbctemplate);
	 * 
	 * if( registeration_req.equals("N") || registeration_req.equals("n")){
	 * customername = "--"; }else{ customername =
	 * (String)commondesc.fchCustName(instid, custid, jdbctemplate); }
	 * 
	 * 
	 * String maintainreq = commondesc.checkMaintainenceRequired(instid,
	 * subproductid, jdbctemplate); if( maintainreq.equals("N") ||
	 * maintainreq.equals("n")){ setMaintainrequired(false); }else{
	 * setMaintainrequired(true); }
	 * 
	 * 
	 * Date expdate1 = (Date)mp.get("EXPIRY_DATE"); SimpleDateFormat frm = new
	 * SimpleDateFormat( "dd-MM-yy"); expdate = frm.format(expdate1);
	 * setExpdate(expdate);
	 * 
	 * Date now = new Date(); Boolean expired_flag = false; if(
	 * !expdate1.after(now)){ System.out.println( "expired__" + expdate);
	 * expired_flag = true; status_code_desc = "Expired";
	 * setCrd_status_desc(status_code_desc); } else{
	 * 
	 * List applicable_list = cardmaindao.getApplicableActionList(instid,
	 * status_code, jdbctemplate); if( !applicable_list.isEmpty() ){
	 * ListIterator itrlist = applicable_list.listIterator(); maintainallowed =
	 * true; while( itrlist.hasNext() ){ Map tmp = (Map)itrlist.next(); String
	 * act_code = ( String ) tmp.get("APPLICABLE_ACTION"); String act_code_desc
	 * = commondesc.getCardStatusDesc( instid, act_code, jdbctemplate );
	 * 
	 * tmp.put("CODE_DESC", act_code_desc); itrlist.remove(); itrlist.add(tmp);
	 * 
	 * } System.out.println( "itrlist__" + itrlist); }
	 * 
	 * setApplicable_act_list(applicable_list); } } }
	 * 
	 * System.out.println( "cardno is__"+ getCardno()); }catch(Exception e ){
	 * session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",
	 * "Exception : Could not continue the process...."); trace("Exception : " +
	 * e.getMessage() ); e.printStackTrace(); } trace("\n\n");enctrace("\n\n");
	 * 
	 * return "maintain_home";
	 * 
	 * }
	 */

	public String showMaintain() throws Exception {
		trace("********* Show maintainence ********* ");
		enctrace("********* Show maintainence ********* ");
		System.out.println("maitaining the screeen");

		System.out.println("instid__" + getRequest().getParameter("instid"));
		// System.out.println("mcardno__" +
		// getRequest().getParameter("mcardno"));
		// System.out.println("hcardno__" +
		// getRequest().getParameter("cardno"));

		HttpSession session = getRequest().getSession();
		FeeConfigDAO feedao = new FeeConfigDAO();
		SearchTxn searchtxn = new SearchTxn();
		String instid = getRequest().getParameter("instid");

		String enccardno = getRequest().getParameter("cardnumber");
		cardcollectbranchlist = commondesc.generateBranchList(instid, jdbctemplate);
		// System.out.println("enccardno---> " + enccardno);

		String cardnumber = null;
		String mcardno = getRequest().getParameter("mcardno");
		String hcardno = getRequest().getParameter("cardno");

		trace("instid ---> " + instid + "   enccardno--->   " + enccardno + " mcardno---> " + mcardno
				+ "    hcardno--->" + hcardno);
		
		trace("clear cardno :: "+cardno);
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		String keyid = "" ,encCardNumber="";
		// String EDMK = "", EDPK = "";
		PadssSecurity padsssec = new PadssSecurity();

		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		// System.out.println("edok-->" + EDPK);
		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			// System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDMK = ((String) map.get("DMK"));
					// System.out.println("CDMK-->" + CDMK);
					// String eDPK = ((String) map.get("DPK"));
					String CDPK = padsssec.decryptDPK(CDMK, EDPK);
					// System.out.println("CDPK-->" + CDPK);
					encCardNumber = padsssec.getECHN(CDPK, enccardno);
					trace("cardnumber-->" + cardnumber);
					// hcardno = padsssec.getHashedValue(cardnumber +
					// instid).toString();
				}
			}
		}

		String getEcardNumberQry = "SELECT ORG_CHN FROM CARD_PRODUCTION  WHERE INST_ID='" + instid
				+ "' AND ORDER_REF_NO IN (" + "SELECT ORDER_REF_NO FROM  CARD_PRODUCTION_HASH  WHERE  HCARD_NO='"
				+ hcardno + "') ";

		enctrace("getEcardNumberQry  ---->  " + getEcardNumberQry);

		String org_chn = ((String) jdbctemplate.queryForObject(getEcardNumberQry, String.class));

		// System.out.println("cardno:::" + cardnumber);
		setCardno(enccardno);
		// System.out.println("enccardno:::" + org_chn);
		setEnccardno(org_chn);
		setMcardno(mcardno);
		setHcardno(hcardno);
		setPadssenable(padssenable);

		Connection conn = null;
		ResultSet rs = null;

		try {
			int reqch = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if (reqch < 0) {
				return "required_home";
			}

			int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);
			if (maint < 0) {
				return "required_home";
			}

			List cardreclist = null;
			// ("card no for fetching production :" + cardno);
			// ("card number for fetching production :" + cardnumber);

			// String Hcardno = padsssec.getHashedValue(cardnumber +
			// instid).toString();
			// trace("Hcardno is " + Hcardno);

			if (padssenable.equals("Y")) {
				cardreclist = cardmaindao.getProdcutionCardDetails(instid, hcardno, padssenable, TABLENAME,
						jdbctemplate);
			} else {
				cardreclist = cardmaindao.getProdcutionCardDetails(instid, cardnumber, padssenable, TABLENAME,
						jdbctemplate);
			}
			String switch_status_code = this.getSwitchCardStatusCode(instid, hcardno, jdbctemplate);
			if (!cardreclist.isEmpty()) {
				Iterator itr = cardreclist.iterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();

					order_refno = (String) mp.get("ORDER_REF_NO");
					setOrder_refno(order_refno);

					account_no = (String) mp.get("ACCOUNT_NO");
					setAcccountno(account_no);

					List getacctnolist = cardmaindao.getaccountlist(instid, order_refno, encCardNumber, jdbctemplate);

					if (getacctnolist.isEmpty()) {
						Map hmp = new HashMap();
						hmp.put("ACCOUNTNO", "NOREC");
						getacctnolist.add(hmp);
						setGetacctnolist(getacctnolist);
					}
					setGetacctnolist(getacctnolist);

					System.out.println("getting SECONDARY ACCOUNT LIST" + getacctnolist);

					acctsubtypeid = (String) mp.get("ACCTTYPE_ID");
					System.out.println("getting account subtypeid" + acctsubtypeid);
					List actypedescvalue = cardmaindao.getactypedesc(instid, acctsubtypeid, jdbctemplate);

					setActypedescvalue(actypedescvalue);

					String bin = (String) mp.get("BIN");
					bindesc = commondesc.getCardTypeDesc(instid, bin, jdbctemplate);
					setBindesc(bindesc);

					// String switch_status_code =
					// (String)mp.get("STATUS_CODE");
					String status_code = commondesc.getCardStatusCode(instid, switch_status_code, jdbctemplate);
					status_code_desc = commondesc.getCardStatusDesc(instid, status_code, jdbctemplate);

					String cafrec = (String) mp.get("CAF_REC_STATUS");

					product_code = commondesc.getProductdesc(instid, (String) mp.get("PRODUCT_CODE"), jdbctemplate);
					setProduct_code(product_code);

					custid = (String) mp.get("CIN");
					setCustid(custid);

					String subproductid = (String) mp.get("SUB_PROD_ID");

					customername = (String) commondesc.fchCustName(instid, custid, jdbctemplate);

					String maintainreq = commondesc.checkMaintainenceRequired(instid, subproductid, jdbctemplate);
					if (maintainreq.equals("N") || maintainreq.equals("n")) {
						setMaintainrequired(false);
					} else {
						setMaintainrequired(true);
						setMaintainallowed(true);
					}
					// SimpleDateFormat fmt = new
					// SimpleDateFormat("dd-MM-YYYY");

					String EXPIRYDATEqry = "SELECT EXPIRYDATE,CHN FROM EZCARDINFO WHERE INSTID='" + instid
							+ "' AND CHN='" + hcardno + "' AND ROWNUM=1";
					trace("EXPIRYDATE :" + EXPIRYDATEqry);
					// System.out.println("EXPIRYDATEqry"+EXPIRYDATEqry);
					List expiry = jdbctemplate.queryForList(EXPIRYDATEqry);
					Iterator expirydetails = expiry.iterator();
					Date expdate1 = null;
					Timestamp edate = null;
					while (expirydetails.hasNext()) {
						Map map = (Map) expirydetails.next();
						expdate1 = (Date) map.get("EXPIRYDATE");
						edate = (Timestamp) map.get("EXPIRYDATE");
					}
					// StringBuilder exdate = new
					// StringBuilder(fmt.format(edate));

					// Date expdate11 = (Date) mp.get("EXPIRY_DATE");
					SimpleDateFormat frm1 = new SimpleDateFormat("dd-MM-yy");
					expdate = frm1.format(expdate1);
					setExpdate(expdate);

					Date now = new Date();
					Boolean expired_flag = false;

					if (!expdate1.after(now)) {
						System.out.println("expired__" + expdate);
						expired_flag = true;

						String maintcode = "";
						int switchCardInfoUpdateloststol = cardmaindao.switchCardInfoUpdate(instid,
								getRequest().getParameter("Hcardno"), jdbctemplate);
						if (switchCardInfoUpdateloststol > 0) {

							status_code = "03";
							status_code_desc = "REissued With New Card";
							maintcode = "03";

						} else {

							status_code = "20";
							status_code_desc = "Expired";
							maintcode = "03";
						}

						setCrd_status_desc(status_code_desc);

						List applicable_list = new ArrayList();
						applicable_list = cardmaindao.getApplicableActionList(instid, status_code, jdbctemplate);

						// (String)session.getAttribute("maintcode");
						trace("getting maintanence code" + maintcode);

						List applicable_list1 = new ArrayList();
						boolean mactiveflag = true;
						if (Integer.parseInt(maintcode) != 0) {
							mactiveflag = false;
						}
						if (!applicable_list.isEmpty()) {
							ListIterator itrlist = applicable_list.listIterator();
							maintainallowed = true;
							while (itrlist.hasNext()) {
								Map tmp = (Map) itrlist.next();
								String act_code = (String) tmp.get("APPLICABLE_ACTION");

								String act_code_desc = commondesc.getCardStatusDesc(instid, act_code, jdbctemplate);
								tmp.put("CODE_DESC", act_code_desc);
								if (Integer.parseInt(act_code) == Integer.parseInt(maintcode)) {
									mactiveflag = true;
									applicable_list1 = new ArrayList();
									HashMap hMap = new HashMap();
									hMap.put("APPLICABLE_ACTION", maintcode);
									hMap.put("CODE_DESC", act_code_desc);
									// mcode.put("APPLICABLE_ACTION",
									// maintcode);
									applicable_list1.add(hMap);
									break;
								}

								applicable_list1.add(tmp);

							}
							System.out.println("itrlist__" + itrlist);
						}
						if (mactiveflag) {
							setApplicable_act_list(applicable_list1);
						} else {
							applicable_list1 = new ArrayList();
							setApplicable_act_list(applicable_list1);
						}
					}

					else {
						// List applicable_list1=new ArrayList();
						List applicable_list = new ArrayList();
						String maintcode = (String) session.getAttribute("maintcode");
						trace("getting maintanence code" + maintcode);

						applicable_list = cardmaindao.getApplicableActionList(instid, status_code, jdbctemplate);
						/*
						 * System.out.println(applicable_list1+""+maintcode); if
						 * (Integer.parseInt(maintcode)!=0) {
						 * if(applicable_list1.contains(maintcode)){
						 * 
						 * HashMap hMap = new HashMap();
						 * hMap.put("APPLICABLE_ACTION", maintcode);
						 * //mcode.put("APPLICABLE_ACTION", maintcode);
						 * applicable_list.add(hMap); } //applicable_list =
						 * cardmaindao.getApplicableActionList(instid,
						 * maintcode, jdbctemplate);
						 * 
						 * } else{
						 * 
						 * applicable_list=applicable_list1; }
						 */
						List applicable_list1 = new ArrayList();
						boolean mactiveflag = true;
						if (Integer.parseInt(maintcode) != 0) {
							mactiveflag = false;
						}
						if (!applicable_list.isEmpty()) {
							ListIterator itrlist = applicable_list.listIterator();
							maintainallowed = true;
							while (itrlist.hasNext()) {
								Map tmp = (Map) itrlist.next();
								String act_code = (String) tmp.get("APPLICABLE_ACTION");

								String act_code_desc = commondesc.getCardStatusDesc(instid, act_code, jdbctemplate);
								tmp.put("CODE_DESC", act_code_desc);
								if (Integer.parseInt(act_code) == Integer.parseInt(maintcode)) {
									mactiveflag = true;
									applicable_list1 = new ArrayList();
									HashMap hMap = new HashMap();
									hMap.put("APPLICABLE_ACTION", maintcode);
									hMap.put("CODE_DESC", act_code_desc);
									// mcode.put("APPLICABLE_ACTION",
									// maintcode);
									applicable_list1.add(hMap);
									break;
								}

								applicable_list1.add(tmp);

							}
							System.out.println("itrlist__" + itrlist);
						}
						if (mactiveflag) {
							setApplicable_act_list(applicable_list1);
						} else {
							applicable_list1 = new ArrayList();
							setApplicable_act_list(applicable_list1);
						}
					}

					String todaydate = commondesc.getDate("dd-MM-yyyy");
					callbean.setTxndate(todaydate);

					// LIMIT DETAILS...
					String limitid = (String) mp.get("LIMIT_ID");
					callbean.setLimitid(limitid);
					String limitdesc = commondesc.getLimitDesc(instid, limitid, jdbctemplate);
					callbean.setLimitdesc(limitdesc);
					List limitdata = commondesc.getLimitDetails(instid, limitid, jdbctemplate);

					trace("limitdata----------> " + limitid);
					if (limitdata != null) {
						ListIterator limititr = limitdata.listIterator();
						while (limititr.hasNext()) {
							Map limitmp = (Map) limititr.next();

							Number txncode = (Number) limitmp.get("TXNCODE");

							System.out.println("txncdoe --> " + txncode);

							/*
							 * String txndesc =
							 * commondesc.getTransactionDesc(instid,
							 * txncode.toString(), jdbctemplate);
							 */
							// String txndesc =
							// commondesc.getTransactionDesc(instid,
							// txncode.toString(), jdbctemplate);

							// String
							// pertxnamt=(String)(Object)limitmp.get("PERTXNAMT").toString();
							// System.out.println("pertxn amt------->
							// "+pertxnamt);
							String txndesc = commondesc.getTransactionDesc(instid, String.valueOf(txncode),
									jdbctemplate);
							String cur = commondesc
									.formatCurrency((String) (Object) limitmp.get("LIMIT_AMOUNT").toString());
							// String cfg=commondesc.formatCurrency((String)
							// (Object) limitmp.get("PERTXNAMT").toString());
							limitmp.put("TXNDESC", txndesc);
							// limitmp.put("LIMIT_AMOUNT",
							// commondesc.formatCurrency((String) (Object)
							// limitmp.get("LIMIT_AMOUNT").toString()));
							// limitmp.put("PERTXNAMT",commondesc.formatCurrency((String)
							// (Object) limitmp.get("PERTXNAMT").toString()));

							// trace("cur "+cur +" cfg"+cfg);
							limitmp.put("LIMIT_AMOUNT", cur);
							// limitmp.put("PERTXNAMT",cfg);

							/*
							 * List reachedlimit =
							 * commondesc.getLimitReachedDetails(instid, cardno,
							 * txncode.toString(),jdbctemplate);
							 */
							List reachedlimit = commondesc.getLimitReachedDetails(instid, cardno,
									String.valueOf(txncode), jdbctemplate);

							trace("Getting daily txn limit...got :" + reachedlimit.size());
							limitmp.put("TODAYTXNAMT", "--");
							limitmp.put("TODAYTXNCNT", "--");
							if (reachedlimit != null) {
								Iterator reachitr = reachedlimit.iterator();
								while (reachitr.hasNext()) {
									Map reachmp = (Map) reachitr.next();
									String reachedamt = (String) (Object) reachmp.get("ACCUM_AMOUNT").toString();
									String reachedcnt = (String) (Object) reachmp.get("ACCUM_COUNT").toString();
									limitmp.put("TODAYTXNAMT", reachedamt);
									limitmp.put("TODAYTXNCNT", reachedcnt);
								}
							}
							limititr.remove();
							limititr.add(limitmp);
						}
						callbean.setLimitdetails(limitdata);
					}

					// PIN MANAGEMENT DETAILS
					String nofopinchange = "0";
					String noofwrongpin = "0";
					String pinblockdate = null;
/*
					SwitchConnection swhconn = new SwitchConnection();
					conn = swhconn.getConnection();
					List cardactivelist = cardmaindao.getcardactivelist(instid, hcardno, jdbctemplate);

					List newcardactivelist = new ArrayList();

					if (!cardactivelist.isEmpty()) {

						HashMap cmp = (HashMap) cardactivelist.get(0);
						if (cmp.get("REPINDATE") == null) {
							cmp.put("REPINDATE", "N/A");

						}
						if (cmp.get("ACTIVEDATE") == null) {
							cmp.put("ACTIVEDATE", "N/A");

						}
						newcardactivelist.add(cmp);
						// setCardactivelist(cardactivelist);

					}
*/
					// ******Below switch connection code is commented by sardar
					// on 17-04-2018 ************//

					/*
					 * 
					 * 
					 * 
					 * String recordList =
					 * "SELECT DISTINCT to_char(TRANDATE,'DD-MON-YYYY') AS PINCHANGEDATE,RESPCODE,"
					 * +"case RESPCODE when 0 then 'NO_OF_PINCHANGED'" +
					 * "when 55 then 'NO_OF_WRONGPIN'" +
					 * "when 75 then 'PINLOCKDATE'" +"else 'NO_MSG' end " +
					 * "from EZTXNLOG B WHERE " +
					 * "TXNCODE='940006' AND MSGTYPE='210' " +"AND TCHN='"
					 * +hcardno+"'";
					 * 
					 * String recordList =
					 * "SELECT DISTINCT to_char(TRANDATE,'DD-MON-YYYY') AS PINCHANGEDATE,(SELECT  COUNT(TRANDATE) FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=0) AS NO_OF_PINCHANGED,"
					 * +
					 * "(SELECT  COUNT(TRANDATE) FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=55) AS NO_OF_WRONGPIN,"
					 * +
					 * "(SELECT  TRANDATE FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=75) AS PINLOCKDATE "
					 * +
					 * "FROM EZTXNLOG B WHERE TXNCODE='940006' AND MSGTYPE='210' AND TCHN='"
					 * +hcardno+"'";
					 */
					/*
					 * String recordList =
					 * "SELECT to_char(TRANDATE,'DD-MON-YYYY') AS PINCHANGEDATE FROM EZTXNLOG "
					 * + " WHERE TCHN='" + hcardno + "'  " +
					 * "AND TXNCODE='940006' AND MSGTYPE='210' ORDER by TRANDATE desc"
					 * ; // PINCHANGEDATE trace("PINCHANGEDATE Qry value " +
					 * recordList); PreparedStatement ps =
					 * conn.prepareStatement(recordList); rs =
					 * ps.executeQuery(recordList); List as = new ArrayList<>();
					 * while (rs.next()) {
					 * as.add(rs.getString("PINCHANGEDATE")); } //
					 * NO_OF_PINCHANGED recordList =
					 * "SELECT COUNT(TRANDATE) as NO_OF_PINCHANGED FROM EZTXNLOG "
					 * + " WHERE TCHN='" + hcardno + "'  " +
					 * "AND RESPCODE=0 AND TXNCODE='940006' AND MSGTYPE='210'";
					 * trace("NO_OF_PINCHANGED Qry value " + recordList); ps =
					 * conn.prepareStatement(recordList); rs =
					 * ps.executeQuery(recordList); while (rs.next()) {
					 * nofopinchange = rs.getString("NO_OF_PINCHANGED"); }
					 * 
					 * // NO_OF_WRONGPIN recordList =
					 * "SELECT COUNT(TRANDATE) as NO_OF_WRONGPIN FROM EZTXNLOG "
					 * + " WHERE TCHN='" + hcardno + "'  " +
					 * "AND RESPCODE=55 AND MSGTYPE='210'"; trace(
					 * "NO_OF_WRONGPIN Qry value " + recordList); ps =
					 * conn.prepareStatement(recordList); rs =
					 * ps.executeQuery(recordList); while (rs.next()) {
					 * noofwrongpin = rs.getString("NO_OF_WRONGPIN"); }
					 * 
					 * // PINLOCKDATE recordList =
					 * "SELECT to_char(TRANDATE,'DD-MON-YYYY') AS PINLOCKDATE FROM EZTXNLOG "
					 * + " WHERE TCHN='" + hcardno + "'  " +
					 * "AND RESPCODE=75 AND MSGTYPE='210' ORDER by TRANDATE asc"
					 * ; trace("PINLOCKDATE Qry value " + recordList); ps =
					 * conn.prepareStatement(recordList); rs =
					 * ps.executeQuery(recordList); while (rs.next()) {
					 * pinblockdate = rs.getString("PINLOCKDATE"); }
					 * 
					 * List cardactivelist =
					 * cardmaindao.getcardactivelist(instid, hcardno,
					 * jdbctemplate);
					 * 
					 * List newcardactivelist = new ArrayList();
					 * 
					 * if (!cardactivelist.isEmpty()) {
					 * 
					 * HashMap cmp = (HashMap) cardactivelist.get(0); if
					 * (cmp.get("REPINDATE") == null) { cmp.put("REPINDATE",
					 * "N/A");
					 * 
					 * } if (cmp.get("ACTIVEDATE") == null) {
					 * cmp.put("ACTIVEDATE", "N/A");
					 * 
					 * } newcardactivelist.add(cmp); //
					 * setCardactivelist(cardactivelist);
					 * 
					 * } System.out.println("List value " + newcardactivelist);
					 * setCardactivelist(newcardactivelist);
					 * 
					 * if (as.isEmpty()) { as.add("NOREC"); setPinchange(as); }
					 * else { setPinchange(as); } if (nofopinchange == "0") {
					 * setNopinchange("0"); } else {
					 * setNopinchange(nofopinchange); } //
					 * System.out.println("status_code--->"+switch_status_code);
					 * if (noofwrongpin == "0" ||
					 * "50".equalsIgnoreCase(switch_status_code)) {
					 * setWrongpin("0"); } else { setWrongpin(noofwrongpin); }
					 * 
					 * if (pinblockdate == null) { setPinblock("NA"); } else {
					 * setPinblock(pinblockdate); }
					 * 
					 * // CARD ACTIVITY DETAILS String atmwithdr =
					 * "SELECT COUNT(*) AS ORIENT_ATM_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='"
					 * + hcardno +
					 * "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='627443' AND ISSUERID='627443'"
					 * ; trace("Atm withdrawal Qry " + atmwithdr); ps =
					 * conn.prepareStatement(atmwithdr); rs =
					 * ps.executeQuery(atmwithdr); String atmcount = ""; while
					 * (rs.next()) { atmcount =
					 * rs.getString("ORIENT_ATM_WITHDRAWAL"); }
					 * setAtmwithdrawlcount(atmcount);
					 * 
					 * String visaatmwithdr =
					 * "SELECT COUNT(*) AS VISA_ATM_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='"
					 * + hcardno +
					 * "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='432283' AND ISSUERID='627443'"
					 * ; trace("Visa atm withdrawal Qry " + visaatmwithdr); ps =
					 * conn.prepareStatement(visaatmwithdr); rs =
					 * ps.executeQuery(visaatmwithdr); String visacount = "";
					 * while (rs.next()) { visacount =
					 * rs.getString("VISA_ATM_WITHDRAWAL"); }
					 * setVisaatmwithdrawl(visacount);
					 * 
					 * String interswtatmwithdr =
					 * "SELECT COUNT(*) AS INTERSWITCH_ATM_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='"
					 * + hcardno +
					 * "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='900001' AND ISSUERID='627443'"
					 * ; trace("Inter-switch atm withdrawal Qry " +
					 * interswtatmwithdr); ps =
					 * conn.prepareStatement(interswtatmwithdr); rs =
					 * ps.executeQuery(interswtatmwithdr); String interswtcount
					 * = ""; while (rs.next()) { interswtcount = rs
					 * .getString("INTERSWITCH_ATM_WITHDRAWAL"); }
					 * setInterswitchatmwithdr(interswtcount);
					 * 
					 * String totalwithdr =
					 * "SELECT COUNT(*) AS TOTAL_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='"
					 * + hcardno +
					 * "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011'"
					 * ; trace("Total withdrawal Qry " + totalwithdr); ps =
					 * conn.prepareStatement(totalwithdr); rs =
					 * ps.executeQuery(totalwithdr); String totalatmwith = "";
					 * while (rs.next()) { totalatmwith =
					 * rs.getString("TOTAL_WITHDRAWAL"); }
					 * setTotalatmwithdrawal(
					 * 
					 * 
					 * totalatmwith);
					 * 
					 * String totalpostxn =
					 * "SELECT COUNT(*) AS NO_OF_POSTXNS FROM EZTXNLOG WHERE TCHN='"
					 * + hcardno + "' AND DEVICETYPE='POS' AND RESPCODE=0";
					 * trace("Total Pos Txn Qry " + totalpostxn); ps =
					 * conn.prepareStatement(totalpostxn); rs =
					 * ps.executeQuery(totalpostxn); String totalpos = ""; while
					 * (rs.next()) { totalpos = rs.getString("NO_OF_POSTXNS"); }
					 * setNoofpostxns(totalpos);
					 * 
					 * String totalonlinetxn =
					 * "SELECT COUNT(*) AS NO_OF_ONLINETXNS FROM EZTXNLOG WHERE TCHN='"
					 * + hcardno +
					 * "' AND POSENTRYCODE='100' and MSGTYPE='210' and RESPCODE=0"
					 * ; trace("Total online txn " + totalonlinetxn); ps =
					 * conn.prepareStatement(totalonlinetxn); rs =
					 * ps.executeQuery(totalonlinetxn); String totalonline = "";
					 * while (rs.next()) { totalonline =
					 * rs.getString("NO_OF_ONLINETXNS"); }
					 * setNoofonlinetxns(totalonline);
					 */

					// ******above switch connection code is commented by sardar
					// on 17-04-2018 ************//
				}

			} // end while

			// System.out.println("cardno is__" + getCardno());
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the process....");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		} finally {
			// rs.close();
			//conn.close();
			trace("Connection closed");
		}
		trace("\n\n");
		enctrace("\n\n");

		return "maintain_home";

	}

	public String changeStatus() throws Exception {

		trace("changeStatus() method called-------");

		/*
		 * //added by prasad 31102019 HttpSession session =
		 * getRequest().getSession(false);
		 * 
		 * String sessioncsrftoken = (String) session.getAttribute("token");
		 * System.out.println("Global session token---> " + sessioncsrftoken);
		 * String jspcsrftoken = getRequest().getParameter("token");
		 * System.out.println("changeStatus() method   token---->    " +
		 * jspcsrftoken); if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken))
		 * { session.setAttribute("message", "CSRF Token Mismatch");
		 * addActionError("CSRF Token Mismatch"); return "invaliduser"; }
		 */
/*
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
*/		System.out.println("change card status");
		String instid = comInstId();
		String usercode = comUserCode();
		StringBuilder sb = null;

		String newmaskcardno = null;
		String cardno = (String) getRequest().getParameter("cardno").trim();
		trace("cardno__" + cardno);
		
		System.out.println("cardno__ is " + cardno);

		String enccardno = (String) getRequest().getParameter("enccardno");
		trace("encrypted card number__" + enccardno);
		System.out.println("enccardno is " + enccardno);
		String mcardno = (String) getRequest().getParameter("mcardno").trim();
		// System.out.println("mcardno__" + mcardno);
		PadssSecurity padsssec = new PadssSecurity();

		String hcardno = padsssec.getHashedValue(cardno + instid).toString();
		System.out.println("Hcardno is " + hcardno);
		
		
		
		
		//PadssSecurity padsssec = new PadssSecurity();
		//String keyid="";
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		// System.out.println("edok-->" + EDPK);
		if (padssenable.equals("Y")) {

		String 	keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			// System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDMK = ((String) map.get("DMK"));
					// System.out.println("CDMK-->" + CDMK);
					// String eDPK = ((String) map.get("DPK"));
					String CDPK = padsssec.decryptDPK(CDMK, EDPK);
					// System.out.println("CDPK-->" + CDPK);
					enccardno = padsssec.getECHN(CDPK, cardno);
					trace("clear cardnumber-->" + enccardno);
					// hcardno = padsssec.getHashedValue(cardnumber +
					// instid).toString();
				}
			}
		}

		

		/*
		 * String hcardno = (String)
		 * getRequest().getParameter("hcardno").trim();
		 * System.out.println("hcardno__" + hcardno);
		 */

		String accountno = (String) getRequest().getParameter("accountno").trim();
		// System.out.println("accountno__" + accountno);

		String padssenable = (String) getRequest().getParameter("padssenable").trim();
		System.out.println("padssenable__" + padssenable);

		String collectbranch = (String) getRequest().getParameter("collectbranch");

		String username = comUsername();
		// PadssSecurity padsssec = new PadssSecurity();
		String statuscode = (String) getRequest().getParameter("statuscode");
		System.out.println("statuscode__" + statuscode);
		IfpTransObj transact = commondesc.myTranObject("CHANGESTATUS", txManager);
		String changedmsg = null;
		String waitingmsg = null;
		String statusmsg = "";
		String order_ref_no = null;
		HttpSession session = getRequest().getSession();
		String orderrefno = null;

		String update_status_qry = "", mkck_status = "";
		String caf_recstatus = "A";
		String card_status = "";
		String switchcardstatus = "", newcardno = null, productcode = "",orderflag="NA";

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		// String mcardno="";
		try {

			String searchqry = "SELECT * FROM  CARD_PRODUCTION   WHERE ROWNUM <= 50 AND INST_ID='" + instid
					+ "'AND ORG_CHN = '" + enccardno + "'";
			List card_data = jdbctemplate.queryForList(searchqry);

			String cin = "", embname = "", acctno = "", cardcollectbranch = "", CHECKER_ID = "", PRODUCT_CODE = "",
					binno = "";

			if (card_data.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No data Available");
				return "serach_home";
			}

			Iterator orderno = card_data.iterator();
			if (!card_data.isEmpty()) {
				while (orderno.hasNext()) {
					Map map = (Map) orderno.next();
					cin = ((String) map.get("CIN"));
					embname = ((String) map.get("EMB_NAME"));
					acctno = ((String) map.get("ACCOUNT_NO"));

					cardcollectbranch = ((String) map.get("CARD_COLLECT_BRANCH"));
					CHECKER_ID = ((String) map.get("CHECKER_ID"));
					PRODUCT_CODE = ((String) map.get("PRODUCT_CODE"));
					binno = ((String) map.get("BIN"));
					caf_recstatus=(String) map.get("CAF_REC_STATUS");
					orderflag = (String) map.get("ORDER_FLAG");

				}

			}
			// cin,embname,acctno,cardcollectbranch,CHECKER_ID,
			auditbean.setCin(cin);
			auditbean.setCustname(embname);
			auditbean.setAccoutnno(acctno);
			auditbean.setCardcollectbranch(cardcollectbranch);
			auditbean.setChecker(CHECKER_ID);

			// added by gowtham_010819
			String pcode = null;
			auditbean.setProduct(pcode);

			auditbean.setBin(binno);

			/*
			 * if (padssenable.equals("Y")) {
			 * 
			 */ // productcode =
				// commondesc.getProductCodeByChn(instid,padssenable, enccardno,
				// TABLENAME, jdbctemplate);

			productcode = commondesc.getProductCodeByChn(instid, padssenable, enccardno, TABLENAME, jdbctemplate);

			/*
			 * } else { productcode =
			 * commondesc.getProductCodeByChn(instid,padssenable, cardno,
			 * TABLENAME, jdbctemplate); }
			 */

			if (productcode.equals("NOREC")) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Could not get productcode for the card " + mcardno);
				return "serach_home";
			}
			String bin = commondesc.getBin(instid, productcode, jdbctemplate);
			// String Hcardno=
			// padsssec.getHashedValue(cardno+instid).toString();
			// trace("Hcardno is "+Hcardno);

			String card_current_status = "";

			/*
			 * if (padssenable.equals("Y")) {
			 */

			card_current_status = commondesc.getCardCurrentStatus(instid, padssenable, hcardno, TABLENAME,
					jdbctemplate);

			/*
			 * }
			 * 
			 * else { card_current_status =
			 * commondesc.getCardCurrentStatus(instid,padssenable, cardno,
			 * TABLENAME, jdbctemplate); }
			 */

			if (card_current_status == null) {
				addActionError(" NO CURRENT STATUS FOUND FOR THE CODE [" + statuscode + "]");
				return "serach_home";
			}

			switchcardstatus = commondesc.getSwitchCardStatus(instid, statuscode, jdbctemplate);
			if (switchcardstatus == null) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " NO STATUS FOUND FOR THE CODE [" + statuscode + "]");
				return "serach_home";
			}
			if (statuscode.equals("01")) {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				System.out.println("card status changing as Normal");
				changedmsg = "Active";
				caf_recstatus="A";
				waitingmsg = "";
				auditbean.setAuditactcode("4107");

			} else if (statuscode.equals("05")) {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				// System.out.println(cardno);
				auditbean.setIpAdress(ip);
				
				if(!caf_recstatus.equals("AC")){
					caf_recstatus="A";
				}

				trace("card status changing as Normal");
				changedmsg = "Active";
				waitingmsg = "";
				auditbean.setAuditactcode("4107");

				/*
				 * String check =
				 * "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE INST_ID='" +
				 * instid + "' AND ORG_CHN='" + enccardno +
				 * "' and OTPSTATUS='S'"; String cnt = (String)
				 * jdbctemplate.queryForObject(check, String.class); if
				 * (cnt.equals("0")) { addActionError(
				 * "Please generate OTP before activate the card."); return
				 * "serach_home"; }
				 */

			} else if (statuscode.equals("02")) {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				System.out.println("card status changing as Temp Block");
				changedmsg = "TEMP BLOCK";
				waitingmsg = "";
				auditbean.setAuditactcode("4101");

			} else if (statuscode.equals("03")) {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				System.out.println("card status changing as lost stolen");
				changedmsg = "Permanently Blocked, Waiting in Re-Issue Card";
				waitingmsg = "";
				auditbean.setAuditactcode("4102");

				int updatestatus;

				/* if (padssenable.equals("Y")) { */

				updatestatus = cardmaindao.updateCardStatusDate(instid, enccardno, "BLOCK_DATE", TABLENAME,
						jdbctemplate);

				/*
				 * } else { updatestatus =
				 * cardmaindao.updateCardStatusDate(instid, cardno,
				 * "BLOCK_DATE", TABLENAME, jdbctemplate); }
				 */
				if (updatestatus < 0) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not continue the process... unable to update status");
					return this.searchHome();
				}
			} else if (statuscode.equals("04")) {

				System.out.println("card status changing as close");
				enctrace("close card query started ......");
				auditbean.setAuditactcode("4106");

				try {

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg("Status changed as [ CLOSED] ");
					auditbean.setUsercode(username);
					auditbean.setCardno(mcardno);
					// System.out.println("encrypted card number" +
					// enccardnumber.length());
					auditbean.setCardnumber(enccardno);
					// trace(auditbean.getCardnumber());

					// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}

				String mvcardprod = "";
				String delcardprod = "", Success = "", Failue = "";
				/// delete part

				int mv_Authrel_3 = 0, mvAcctinfo_2 = 0, mvcardprod_1 = 0, mvcardifo_5 = 0, mvcustinfo_4 = 0,
						mv_accountinfo = 0, mvIFDcustomerinfo = 0;

				int del_Authrel_3 = 0, delcardprod_1 = 0;
				String del_Authrel = "", delAcctinfo = "", delcardifo = "", ezcustid = "", delcustinfo = "",
						delACCOUNTINFO = "", delCUSTOMERINFO = "";

				int delcustinfo_4 = 0, delcustomerinfo = 0, delIfd_ACCOUNTINFO = 0, delAcctinfo_2 = 0, delcardifo_5 = 0;

				String customerid = this.getcustomeridtoclose(enccardno, instid, jdbctemplate);

				String normalcardqry = "SELECT COUNT(*) FROM CARD_PRODUCTION WHERE ACCOUNT_NO='" + accountno
						+ "'  AND CAF_REC_STATUS in ('A') AND INST_ID='" + instid + "'";
				int normcalcard = jdbctemplate.queryForInt(normalcardqry);
				String addoncard = "SELECT COUNT(*) FROM CARD_PRODUCTION WHERE ACCOUNT_NO='" + accountno
						+ "'  AND CAF_REC_STATUS in ('AC') AND INST_ID='" + instid + "'";
				int addconcardcheck = jdbctemplate.queryForInt(addoncard);

				if (normcalcard > 0 & addconcardcheck > 0) {
					System.out.println("sssssssssss");
					String checkaddoncardwithcardno = "SELECT COUNT(*) FROM CARD_PRODUCTION WHERE ACCOUNT_NO='"
							+ accountno + "' and CAF_REC_STATUS in ('A') AND ORG_CHN ='" + enccardno + "' AND INST_ID='"
							+ instid + "'";
					int cheaddoncwithcard = jdbctemplate.queryForInt(checkaddoncardwithcardno);

					if (cheaddoncwithcard > 0) {
						String orgchn = "SELECT ORG_CHN FROM CARD_PRODUCTION WHERE ACCOUNT_NO='" + accountno
								+ "' and CAF_REC_STATUS in ('AC')  AND INST_ID='" + instid + "'";
						String orgchncard = (String) jdbctemplate.queryForObject(orgchn, String.class);

						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg",
								" Kindly close the Member [" + orgchncard + " ]card to Close Parent Card..");
						return "serach_home";
					}
				}
				System.out.println("entering to the else part");
				/*
				 * String othercardcheckwithsameaccount =
				 * "SELECT COUNT(*) FROM CARD_PRODUCTION WHERE ACCOUNT_NO='"
				 * +accountno+"' and HCARD_NO ='"+hcardno+"' AND INST_ID='"
				 * +instid+"'";
				 */
				String othercardcheckwithsameaccount = "SELECT COUNT(*) FROM CARD_PRODUCTION WHERE ACCOUNT_NO='"
						+ accountno + "' and ORG_CHN ='" + enccardno + "' AND INST_ID='" + instid + "'";
				int productionwithaccount = jdbctemplate.queryForInt(othercardcheckwithsameaccount);

				String secacctcountqry = "SELECT COUNT(*) FROM EZAUTHREL WHERE ACCOUNTNO='" + accountno
						+ "' AND  CHN = '" + hcardno + "'AND INSTID='" + instid + "'";

				int auhtrelwithaccount = jdbctemplate.queryForInt(secacctcountqry);

				String checkohtercard = "SELECT COUNT(*) FROM EZAUTHREL WHERE ACCOUNTNO NOT IN ('" + accountno
						+ "') and CHN  IN('" + hcardno + "') AND INSTID='" + instid + "'";

				int othercard = jdbctemplate.queryForInt(checkohtercard);

				String chcksameaccountwithothercard = "SELECT COUNT(*) FROM EZAUTHREL WHERE ACCOUNTNO IN ('" + accountno
						+ "') and CHN NOT IN('" + hcardno + "') AND INSTID='" + instid + "'";
				
				/*String query = "SELECT COUNT(*) FROM EZAUTHREL WHERE CHN IN (SELECT HCARD_NO FROM CARD_PRODUCTION WHERE ACCOUNT_NO='"
						+ accountno + "' AND ORG_CHN ='" + enccardno + "' AND CAF_REC_STATUS ='AC' AND INST_ID='"
						+ instid + "')";*/
				
				//CHANGED ON 28/10/2020
				String query = "SELECT COUNT(*) FROM EZAUTHREL WHERE CHN IN (SELECT org_chn FROM CARD_PRODUCTION  WHERE ACCOUNT_NO='"
						+ accountno + "' AND ORG_CHN ='" + enccardno + "' AND CAF_REC_STATUS ='AC' AND INST_ID='"
						+ instid + "')";
				
				int ACcardsecond = jdbctemplate.queryForInt(query);
				System.out.println(chcksameaccountwithothercard);
				int othercardhavingaccounts = jdbctemplate.queryForInt(chcksameaccountwithothercard);

				if (productionwithaccount > 0 && auhtrelwithaccount > 0 && othercard >= 0
						&& othercardhavingaccounts == 0) {

					// by siva 12-07-19
					// delete records all
					// if(padssenable.equals("Y")){

					mvcardprod = "INSERT INTO CARD_PRODUCTION_CLOSE SELECT * FROM CARD_PRODUCTION WHERE ORG_CHN ='"
							+ enccardno + "' AND INST_ID='" + instid + "'";
					
					
					

					/*
					 * } else{ mvcardprod =
					 * "INSERT INTO CARD_PRODUCTION_CLOSE SELECT * FROM CARD_PRODUCTION WHERE CARD_NO = '"
					 * +cardno+"' AND INST_ID='"+instid+"'"; }
					 */

					enctrace("mvcardprod:::" + mvcardprod);

					String mvAcctinfo = "INSERT INTO EZACCOUNTINFO_CLOSE select * from EZACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO from EZAUTHREL where chn='"
							+ hcardno + "' AND INSTID='" + instid + "')";
					enctrace("mvAcctinfo:::" + mvAcctinfo);

					String mvaccountinfo = "INSERT INTO ACCOUNTINFO_CLOSE select * from ACCOUNTINFO where   ACCOUNTNO in (select ACCOUNTNO from EZAUTHREL where chn='"
							+ hcardno + "' AND INSTID='" + instid + "')";
					enctrace("mvaccountinfo:::" + mvaccountinfo);

					String mvcustomerinfo = "INSERT INTO CUSTOMERINFO_CLOSE select * from CUSTOMERINFO  where CIN in (select custid from ezcardinfo where chn='"
							+ hcardno + "' AND INST_ID='" + instid + "')";
					enctrace("mvIFDcustomerinfo:::" + mvcustomerinfo);

					String mv_Authrel = "INSERT INTO EZAUTHREL_CLOSE SELECT * FROM EZAUTHREL WHERE CHN = '" + hcardno
							+ "' AND INSTID='" + instid + "'";
					enctrace("mv_Authrel:::" + mv_Authrel);

					String mvcustinfo = "INSERT INTO EZCUSTOMERINFO_CLOSE select * from EZCUSTOMERINFO where custid in (select custid from ezcardinfo where chn='"
							+ hcardno + "' AND INSTID='" + instid + "')";
					enctrace("mvcustinfo:::" + mvcustinfo);

					String mvcardifo = "INSERT INTO ezcardinfo_CLOSE select * from ezcardinfo where chn='" + hcardno
							+ "' AND INSTID='" + instid + "'";
					enctrace("mvcardifo:::" + mvcardifo);

					mvIFDcustomerinfo = jdbctemplate.update(mvcustomerinfo);
					mv_accountinfo = jdbctemplate.update(mvaccountinfo);// 3
					System.out.println("mvIFDcustomerinfo" + mvIFDcustomerinfo);
					System.out.println("mv_accountinfo" + mv_accountinfo);
					mv_Authrel_3 = jdbctemplate.update(mv_Authrel);
					System.out.println("mv_Authrel_3" + mv_Authrel_3);
					mvAcctinfo_2 = jdbctemplate.update(mvAcctinfo);// 2
					System.out.println("mvAcctinfo_2" + mvAcctinfo_2);
					mvcardprod_1 = jdbctemplate.update(mvcardprod);// 1
					System.out.println("mvcardprod_1" + mvcardprod_1);
					mvcardifo_5 = jdbctemplate.update(mvcardifo);
					System.out.println("mvcardifo_5" + mvcardifo_5);
					mvcustinfo_4 = jdbctemplate.update(mvcustinfo);
					System.out.println("mvcustinfo_4" + mvcustinfo_4);

					// delete

					// if(padssenable.equals("Y")){
					// delcardprod = "DELETE FROM CARD_PRODUCTION WHERE HCARD_NO
					// = '"+hcardno+"' AND ACCOUNT_NO ='"+accountno+"'AND
					// INST_ID='"+instid+"'";
					delcardprod = "DELETE FROM CARD_PRODUCTION WHERE ORG_CHN ='" + enccardno + "' AND ACCOUNT_NO ='"
							+ accountno + "'AND INST_ID='" + instid + "'";
					
					enctrace("delcardprod--->"+delcardprod);

					/*
					 * } else{ delcardprod =
					 * "DELETE FROM CARD_PRODUCTION WHERE CARD_NO = '"+cardno+
					 * "' AND ACCOUNT_NO ='"+accountno+"' AND INST_ID='"
					 * +instid+"'"; }
					 */

					delAcctinfo = "DELETE from EZACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO from EZAUTHREL where chn='"
							+ hcardno + "' AND INSTID='" + instid + "')";// ACCOUNTNO
																			// ='"+accountno+"'
																			// AND
																			// INSTID='"+instid+"'
																			// ";
					enctrace("delAcctinfo:::" + delAcctinfo);

					del_Authrel = "DELETE FROM EZAUTHREL WHERE CHN = '" + hcardno + "' AND INSTID='" + instid + "'";
					enctrace("del_Authrel:::" + del_Authrel);

					delCUSTOMERINFO = "DELETE from CUSTOMERINFO where  CIN='" + cin + "' AND INST_ID='" + instid + "'";
					enctrace("delCUSTOMERINFO:::" + delCUSTOMERINFO);

					delcardifo = "DELETE from ezcardinfo where chn='" + hcardno + "' AND INSTID='" + instid + "'";
					enctrace("delcardifo:::" + delcardifo);

					delcustinfo = "DELETE from EZCUSTOMERINFO where custid ='" + cin + "' AND INSTID='" + instid + "'";
					enctrace("delcustinfo:::" + delcustinfo);

					/*delACCOUNTINFO = "DELETE from ACCOUNTINFO WHERE select * from ACCOUNTINFO where   ACCOUNTNO in (select ACCOUNTNO from EZAUTHREL where chn='"
							+ hcardno + "' AND INSTID='" + instid + "')";
					// ACCOUNTNO ='"+accountno+"'AND  INST_ID='"+instid+"'";*/	
					
					delACCOUNTINFO = "DELETE from ACCOUNTINFO WHERE ACCOUNTNO in (select ACCOUNTNO from EZAUTHREL where chn='"
							+ hcardno + "' AND INSTID='" + instid + "')";
					// ACCOUNTNO ='"+accountno+"'AND  INST_ID='"+instid+"'";
					
					enctrace("delACCOUNTINFO:::" + delACCOUNTINFO);

					delcardifo_5 = jdbctemplate.update(delcardifo);
					delcustinfo_4 = jdbctemplate.update(delcustinfo);
					delAcctinfo_2 = jdbctemplate.update(delAcctinfo);

					/*del_Authrel_3 = jdbctemplate.update(del_Authrel);*/

					delcardprod_1 = jdbctemplate.update(delcardprod);

					delcustomerinfo = jdbctemplate.update(delCUSTOMERINFO);
					delIfd_ACCOUNTINFO = jdbctemplate.update(delACCOUNTINFO);
					del_Authrel_3 = jdbctemplate.update(del_Authrel);

					trace(mvIFDcustomerinfo + "-" + mv_accountinfo + "-" + mvAcctinfo_2 + "-" + mvcardprod_1 + "-"
							+ mv_Authrel_3 + "-" + mvcardifo_5 + "-" + mvcustinfo_4 + "-" + delcardifo_5 + "-"
							+ del_Authrel_3 + "-" + delcardprod_1 + "-" + delcustinfo_4 + "-" + delAcctinfo_2 + "-"
							+ delcustomerinfo + "-" + delIfd_ACCOUNTINFO);

					if (mvIFDcustomerinfo > 0 && mv_accountinfo > 0 && mvAcctinfo_2 > 0 && mvcardprod_1 > 0
							&& mv_Authrel_3 > 0 && mvcardifo_5 > 0 && mvcustinfo_4 > 0 && delcardifo_5 > 0
							&& del_Authrel_3 > 0 && delcardprod_1 > 0 && delcustinfo_4 > 0 && delAcctinfo_2 > 0
							&& delcustomerinfo > 0 && delIfd_ACCOUNTINFO > 0) {

						System.out.println("success from all tab");
						Success = "SC000";
					}

					else {
						Success = "FAIL000";
						System.out.println("failue  from all tab");
					}

				}

				if (othercardhavingaccounts > 0) {
					trace("dont dlete from ifdAccount and ezcaccount and custoemr info");

					// if(padssenable.equals("Y")){

					/*
					 * mvcardprod =
					 * "INSERT INTO CARD_PRODUCTION_CLOSE SELECT * FROM CARD_PRODUCTION WHERE HCARD_NO = '"
					 * +hcardno+"' AND INST_ID='"+instid+"'";
					 */
					mvcardprod = "INSERT INTO CARD_PRODUCTION_CLOSE SELECT * FROM CARD_PRODUCTION WHERE ORG_CHN ='"
							+ enccardno + "' AND INST_ID='" + instid + "'";

					/*
					 * } else{ mvcardprod =
					 * "INSERT INTO CARD_PRODUCTION_CLOSE SELECT * FROM CARD_PRODUCTION WHERE CARD_NO = '"
					 * +cardno+"' AND INST_ID='"+instid+"'"; }
					 * enctrace("mvcardprod:::"+mvcardprod);
					 */

					String mv_Authrel = "INSERT INTO EZAUTHREL_CLOSE SELECT * FROM EZAUTHREL WHERE CHN = '" + hcardno
							+ "' AND INSTID='" + instid + "'";
					enctrace("mv_Authrel:::" + mv_Authrel);

					String mvcardifo = "INSERT INTO ezcardinfo_CLOSE select * from ezcardinfo where chn='" + hcardno
							+ "' AND INSTID='" + instid + "'";
					enctrace("mvcardifo:::" + mvcardifo);

					mvcardprod_1 = jdbctemplate.update(mvcardprod);// 1
					mv_Authrel_3 = jdbctemplate.update(mv_Authrel);
					mvcardifo_5 = jdbctemplate.update(mvcardifo);
					System.out.println("mvcardifo_5" + mvcardifo_5);

					// by siva 12-07-19
					// if(padssenable.equals("Y")){

					// delcardprod = "DELETE FROM CARD_PRODUCTION WHERE HCARD_NO
					// = '"+hcardno+"' AND ACCOUNT_NO ='"+accountno+"'AND
					// INST_ID='"+instid+"'";
					delcardprod = "DELETE FROM CARD_PRODUCTION WHERE ORG_CHN ='" + enccardno + "' AND ACCOUNT_NO ='"
							+ accountno + "'AND INST_ID='" + instid + "'";
					enctrace("delcardprod---->"+delcardprod);
					/*
					 * } else{ delcardprod =
					 * "DELETE FROM CARD_PRODUCTION WHERE CARD_NO = '"+cardno+
					 * "' AND ACCOUNT_NO ='"+accountno+"' AND INST_ID='"
					 * +instid+"'"; }
					 */
					del_Authrel = "DELETE FROM EZAUTHREL WHERE CHN = '" + hcardno + "' AND INSTID='" + instid + "'";
					enctrace("del_Authrel:::" + del_Authrel);

					delcardifo = "DELETE from ezcardinfo where chn='" + hcardno + "' AND INSTID='" + instid + "'";
					enctrace("delcardifo:::" + delcardifo);

					delcardifo_5 = jdbctemplate.update(delcardifo);

					del_Authrel_3 = jdbctemplate.update(del_Authrel);

					delcardprod_1 = jdbctemplate.update(delcardprod);

					trace("mvcardprod_1" + mvcardprod_1 + " \n mv_Authrel_3" + mv_Authrel_3 + "\n mvcardifo_5"
							+ mvcardifo_5 + "\n delcardifo_5 " + delcardifo_5 + "\n del_Authrel_3 " + del_Authrel_3
							+ "\n delcardprod_1" + delcardprod_1);
					if (mvcardprod_1 > 0 && mv_Authrel_3 > 0 && mvcardifo_5 > 0 && delcardifo_5 > 0 && del_Authrel_3 > 0
							&& delcardprod_1 > 0) {

						System.out.println("success from three");
						Success = "SC000";
					}

					else {
						System.out.println("failue from three");
						Success = "FAIL000";
					}

				}

				System.out.println("respose" + Success);
				if (Success.equalsIgnoreCase("SC000"))

				{
					txManager.commit(transact.status);
					addActionMessage(mcardno + " Has been Marked as CLOSE");
					trace("closed Successfully");

				}

				else {
					txManager.rollback(transact.status);
					addActionError(" Could not moved to CLOSE Status");
					trace("closed failure");
					return "serach_home";

				}

				/*
				 * if(insertezauthback != 3){ trace(
				 * "Deleting From ACCOUNTINFO and Ezacount");
				 * 
				 * insertifdacctback
				 * =cbsdao.insertifdacctdel(instid,accountnumber,userid,
				 * jdbctemplate);
				 * 
				 * insertezacctback=cbsdao.insertEzAcctdel(instid,
				 * accountnumber,userid,jdbctemplate);
				 * 
				 * }
				 */

				/*
				 * //int cardprod = cardmaindao.del String mvcardprod="";
				 * 
				 * /// moving part if(padssenable.equals("Y")){ mvcardprod =
				 * "INSERT INTO CARD_PRODUCTION_CLOSE SELECT * FROM CARD_PRODUCTION WHERE HCARD_NO = '"
				 * +hcardno+"' AND INST_ID='"+instid+"'"; } else{ mvcardprod =
				 * "INSERT INTO CARD_PRODUCTION_CLOSE SELECT * FROM CARD_PRODUCTION WHERE CARD_NO = '"
				 * +cardno+"' AND INST_ID='"+instid+"'"; }
				 * enctrace("mvcardprod:::"+mvcardprod);
				 * 
				 * 
				 * String mvAcctinfo =
				 * "INSERT INTO EZACCOUNTINFO_CLOSE select * from EZACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO from EZAUTHREL where chn='"
				 * +hcardno+"' AND INSTID='"+instid+"')";
				 * enctrace("mvAcctinfo:::"+mvAcctinfo);
				 * 
				 * String mvaccountinfo =
				 * "INSERT INTO ACCOUNTINFO_CLOSE select * from ACCOUNTINFO where   ACCOUNTNO in (select ACCOUNTNO from EZAUTHREL where chn='"
				 * +hcardno+"' AND INSTID='"+instid+"')";
				 * enctrace("mvaccountinfo:::"+mvaccountinfo);
				 * 
				 * String mvcustomerinfo =
				 * "INSERT INTO CUSTOMERINFO_CLOSE select * from CUSTOMERINFO  where CIN in (select custid from ezcardinfo where chn='"
				 * +hcardno+"' AND INST_ID='"+instid+"')";
				 * enctrace("mvIFDcustomerinfo:::"+mvcustomerinfo);
				 * 
				 * 
				 * 
				 * String mv_Authrel =
				 * "INSERT INTO EZAUTHREL_CLOSE SELECT * FROM EZAUTHREL WHERE CHN = '"
				 * +hcardno+"' AND INSTID='"+instid+"'";
				 * enctrace("mv_Authrel:::"+mv_Authrel);
				 * 
				 * String mvcustinfo =
				 * "INSERT INTO EZCUSTOMERINFO_CLOSE select * from EZCUSTOMERINFO where custid in (select custid from ezcardinfo where chn='"
				 * +hcardno+"' AND INSTID='"+instid+"')";
				 * enctrace("mvcustinfo:::"+mvcustinfo);
				 * 
				 * String mvcardifo =
				 * "INSERT INTO ezcardinfo_CLOSE select * from ezcardinfo where chn='"
				 * +hcardno+"' AND INSTID='"+instid+"'";
				 * enctrace("mvcardifo:::"+mvcardifo); //moving part
				 * 
				 * ///deletetion part String delcardprod = "";
				 * if(padssenable.equals("Y")){ delcardprod =
				 * "DELETE FROM CARD_PRODUCTION WHERE HCARD_NO = '"+hcardno+
				 * "' AND ACCOUNT_NO ='"+accountno+"'AND INST_ID='"+instid+"'";
				 * } else{ delcardprod =
				 * "DELETE FROM CARD_PRODUCTION WHERE CARD_NO = '"+cardno+
				 * "' AND ACCOUNT_NO ='"+accountno+"' AND INST_ID='"+instid+"'";
				 * } enctrace("delcardprod:::"+delcardprod);
				 * 
				 * String noOfaccounts=
				 * "select ACCOUNTNO from EZAUTHREL where chn='"+hcardno+
				 * "' AND INSTID='"+instid+"'";
				 * 
				 * // String account = (String)jdbctemplate.queryForObject(
				 * "select ACCOUNTNO from EZAUTHREL where chn='"+hcardno+
				 * "' AND INSTID='"+instid+"'", String.class) ;
				 * 
				 * int delcustinfo_4=0,delcustomerinfo=0,delIfd_ACCOUNTINFO=0,
				 * delAcctinfo_2=0,delcardifo_5=0;
				 * 
				 * String noaccounts=
				 * "select count(*)  from CARD_PRODUCTION where  ACCOUNT_NO ='"
				 * +accountno+"' AND INST_ID='"+instid+"'"; int cardsavailable =
				 * jdbctemplate.queryForInt(noaccounts);
				 * 
				 * String customerid=
				 * "select count(*)  from CARD_PRODUCTION where  CIN ='"+custid+
				 * "' AND INST_ID='"+instid+"'"; int customeridval =
				 * jdbctemplate.queryForInt(customerid);
				 * 
				 * 
				 * List ezacctno = jdbctemplate.queryForList(noOfaccounts) ;
				 * String del_Authrel="", delAcctinfo
				 * ="",delcardifo="",ezcustid="",delcustinfo="",delACCOUNTINFO=
				 * "",delCUSTOMERINFO=""; Iterator nofaccoutns =
				 * ezacctno.iterator();
				 * 
				 * while(nofaccoutns.hasNext()) { Map map = (Map)
				 * nofaccoutns.next(); String ACCOUNTNO =
				 * ((String)map.get("ACCOUNTNO"));
				 * System.out.println("ACCOUNTNOACCOUNTNO"+ACCOUNTNO);
				 * 
				 * 
				 * if(cardsavailable ==1){
				 * 
				 * delAcctinfo = "DELETE from EZACCOUNTINFO where ACCOUNTNO ='"
				 * +ACCOUNTNO+"' AND INSTID='"+instid+"' ";
				 * enctrace("delAcctinfo:::"+delAcctinfo); delAcctinfo_2 =
				 * jdbctemplate.update(delAcctinfo); }
				 * 
				 * //} delAcctinfo =
				 * "DELETE from EZACCOUNTINFO where ACCOUNTNO ='"+ACCOUNTNO+
				 * "' AND INSTID='"+instid+"' ";
				 * enctrace("delAcctinfo:::"+delAcctinfo);
				 * 
				 * del_Authrel = "DELETE FROM EZAUTHREL WHERE CHN = '"+hcardno+
				 * "' AND INSTID='"+instid+"'";
				 * enctrace("del_Authrel:::"+del_Authrel);
				 * 
				 * delcardifo = "DELETE from ezcardinfo where chn='"+hcardno+
				 * "' AND INSTID='"+instid+"'";
				 * enctrace("delcardifo:::"+delcardifo);
				 * 
				 * 
				 * String nocustid="select custid from ezcardinfo where chn='"
				 * +hcardno+"' AND INSTID='"+instid+"'";
				 * enctrace("nocustid:::"+nocustid); // ezcustid =
				 * (String)jdbctemplate.queryForObject(
				 * "select custid from ezcardinfo where chn='"+hcardno+
				 * "' AND INSTID='"+instid+"'", String.class) ;
				 * 
				 * 
				 * List accounts = jdbctemplate.queryForList(nocustid) ;
				 * Iterator Noofcustid = accounts.iterator();
				 * if(!accounts.isEmpty()){ while(Noofcustid.hasNext()) {
				 * enctrace("inside custid **************:::"); Map map1 = (Map)
				 * Noofcustid.next(); String custid =
				 * ((String)map1.get("CUSTID")); if(cardsavailable ==1 &&
				 * customeridval ==1){
				 * 
				 * delcustomerinfo = jdbctemplate.update(delCUSTOMERINFO);
				 * delIfd_ACCOUNTINFO = jdbctemplate.update(delACCOUNTINFO);
				 * 
				 * } }
				 * 
				 * } } if(cardsavailable ==1 && customeridval ==1){
				 * System.out.println(" if condition delete order delcustinfo_4"
				 * +delcardifo+" delcustinfo_4 \n"+delcustinfo);
				 * 
				 * delcardifo_5 = jdbctemplate.update(delcardifo);
				 * System.out.println("cardinfo"+delcardifo_5);
				 * 
				 * delcustinfo_4 = jdbctemplate.update(delcustinfo);
				 * System.out.println("custinfo"+delcustinfo_4);
				 * 
				 * } else{ trace("else decondition");
				 * 
				 * delcardifo_5 = jdbctemplate.update(delcardifo);
				 * 
				 * } /// delete part
				 * 
				 * int
				 * mv_Authrel_3=0,mvAcctinfo_2=0,mvcardprod_1=0,mvcardifo_5=0,
				 * mvcustinfo_4=0,mv_accountinfo=0,mvIFDcustomerinfo=0;
				 * 
				 * int del_Authrel_3=0,delcardprod_1=0;
				 * 
				 * try{
				 * 
				 * mvIFDcustomerinfo = jdbctemplate.update(mvcustomerinfo);
				 * mv_accountinfo = jdbctemplate.update(mvaccountinfo);
				 * System.out.println("mvIFDcustomerinfo"+mvIFDcustomerinfo);
				 * System.out.println("mv_accountinfo"+mv_accountinfo);
				 * mv_Authrel_3 = jdbctemplate.update(mv_Authrel);
				 * System.out.println("mv_Authrel_3"+mv_Authrel_3); mvAcctinfo_2
				 * = jdbctemplate.update(mvAcctinfo);
				 * System.out.println("mvAcctinfo_2"+mvAcctinfo_2); mvcardprod_1
				 * = jdbctemplate.update(mvcardprod);
				 * System.out.println("mvcardprod_1"+mvcardprod_1); mvcardifo_5
				 * = jdbctemplate.update(mvcardifo);
				 * System.out.println("mvcardifo_5"+mvcardifo_5); mvcustinfo_4 =
				 * jdbctemplate.update(mvcustinfo);
				 * System.out.println("mvcustinfo_4"+mvcustinfo_4);
				 * 
				 * 
				 * del_Authrel_3 = jdbctemplate.update(del_Authrel);
				 * System.out.println("del_Authrel_3"+del_Authrel_3);
				 * 
				 * 
				 * System.out.println("delAcctinfo_2"+delAcctinfo_2);
				 * delcardprod_1 = jdbctemplate.update(delcardprod);
				 * System.out.println("delcardprod_1"+delcardprod_1);
				 * System.out.println("mvcardifo_5"+delcardifo_5);
				 * 
				 * System.out.println("delcustinfo_4"+delcustinfo_4);
				 * System.out.println("delIfd_ACCOUNTINFO"+delIfd_ACCOUNTINFO);
				 * System.out.println("mvIFDcustomerinfo"+mvIFDcustomerinfo);
				 * System.out.println("mv_accountinfo"+mv_accountinfo);
				 * System.out.println("delcustomerinfo"+delcustomerinfo);
				 * 
				 * 
				 * }catch (Exception e) { txManager.rollback(transact.status);
				 * addActionError(" Could not moved to CLOSE Status:::"); trace(
				 * "Could not moved to CLOSE Status::"+e); return "serach_home";
				 * }
				 * 
				 * System.out.println(mvcardprod_1+"-"+mvAcctinfo_2+"-"+
				 * mv_Authrel_3+"-"+mvcustinfo_4+"-"+mvcardifo_5);
				 * 
				 * System.out.println(delcardprod_1+"-"+delAcctinfo_2+"-"+
				 * del_Authrel_3+"-"+delcustinfo_4+"-"+delcardifo_5+"_"+
				 * +mv_accountinfo+"_"+delIfd_ACCOUNTINFO+"_"+mvIFDcustomerinfo+
				 * "_"+delcustomerinfo);
				 * 
				 * if(mvcardprod_1 >= 1 && mvAcctinfo_2 >=0 && mv_Authrel_3 >=1
				 * && mvcustinfo_4 >=0 && mvcardifo_5 >=0 && delcardprod_1>=1 &&
				 * delAcctinfo_2>=0 && del_Authrel_3>=1 && delcustinfo_4>=0 &&
				 * delcardifo_5 >=1 && mv_accountinfo >=0 && delIfd_ACCOUNTINFO
				 * >=0 && mvIFDcustomerinfo >=0 && delcustomerinfo >=0) {
				 * txManager.commit(transact.status); addActionMessage(mcardno+
				 * " Has been Marked as CLOSE"); trace("closed Successfully");
				 * 
				 * 
				 * }
				 * 
				 * 
				 * 
				 * else { txManager.rollback(transact.status); addActionError(
				 * " Could not moved to CLOSE Status"); trace("closed failure");
				 * return "serach_home";
				 * 
				 * }
				 */

				// EZACCUMINFO

				enctrace("....................close card query ended ");
				changedmsg = "CLOSE";
				waitingmsg = "";
				auditbean.setAuditactcode("4106");

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				return "serach_home";

			} else if (statuscode.equals("10")) {
				caf_recstatus = "";
				card_status = "";
				mkck_status = "P";
				System.out.println("card status changing as damage");
				System.out.println("changeExpiryDate::::::::::" + getRequest().getParameter("changeExpiryDate"));

				String expirydateReq = getRequest().getParameter("changeExpiryDate") == null ? ""
						: getRequest().getParameter("changeExpiryDate");

				auditbean.setAuditactcode("4103");
				String encchn = "";

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				if (padssenable.equals("Y")) {

					String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					System.out.println("keyid::" + keyid);
					List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
					// System.out.println("secList::" + secList);
					Iterator secitr = secList.iterator();
					if (!secList.isEmpty()) {
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							String CDMK = ((String) map.get("DMK"));
							// String eDPK = ((String) map.get("DPK"));
							String CDPK = padsssec.decryptDPK(CDMK, EDPK);
							cardno = padsssec.getECHN(CDPK, cardno);
						}
					}
				}

				String changeexpreq = "", expperiod = "";

				if (expirydateReq.equals("Y")) {
					// String getExpPeriod=
					if (padssenable.equals("Y")) {
						String subprodid = commondesc.getSubProductByCHN(instid, cardno, jdbctemplate, "PROD");
						expperiod = commondesc.getSubProductExpPeriod(instid, subprodid, jdbctemplate);
						changeexpreq = "Y";
						caf_recstatus = "DE";
						waitingmsg = " Waiting For Security Data Generation";
						card_status = "01";
					}
				} else {
					changeexpreq = "N";
					caf_recstatus = "D";
					waitingmsg = " Waiting For PRE File Generation";
					card_status = "02";
				}

				// System.exit(0);

				int moveprocess = cardmaindao.moveCardToProcess(instid, padssenable, accountno, hcardno, mcardno,
						cardno, cardno, caf_recstatus, card_status, expperiod, changeexpreq, TABLENAME, usercode,
						collectbranch, jdbctemplate);

				trace("count process " + moveprocess);

				if (moveprocess != 2) {
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Could not move the production records to process");
					return "serach_home";
				}
				/*
				 * int movepinprocess = this.movePintoProcess( instid, cardno,
				 * cardno, session); if( movepinprocess != 1 ){
				 * txManager.rollback(transact.status);
				 * session.setAttribute("preverr","E");
				 * session.setAttribute("prevmsg" ,
				 * " Could not move the Pin records to process"); return
				 * "serach_home"; }
				 */
				changedmsg = "DAMAGE";

			} else if (statuscode.equals("06")) {
				
				System.out.println("card status changing as repin");
				caf_recstatus = "R";
				card_status = "01";
				auditbean.setAuditactcode("4105");

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				if (padssenable.equals("Y")) {

					String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					System.out.println("keyid::" + keyid);
					List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
					// System.out.println("secList::" + secList);
					Iterator secitr = secList.iterator();
					if (!secList.isEmpty()) {
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							String CDMK = ((String) map.get("DMK"));
							// String eDPK = ((String) map.get("DPK"));
							String CDPK = padsssec.decryptDPK(CDMK, EDPK);
							cardno = padsssec.getECHN(CDPK, cardno);
						}
					}
				}
				System.out.println("orderflag-->"+orderflag+"  enccardno--->"+enccardno);
				if(orderflag.equals("P")){
					int moveprocess = moveCardToProcess(instid, padssenable, accountno, cardno, cardno, hcardno, hcardno,
							mcardno, caf_recstatus, card_status, usercode, collectbranch, jdbctemplate);
					trace(" move to process tables result  is ::::    "+moveprocess);
					
					if (moveprocess != 1) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", " Could not move the production records to process");
						txManager.rollback(transact.status);
						return "serach_home";
					}
	
					
					 /* int movepinprocess = this.movePintoProcess(instid, cardno,cardno, jdbctemplate, session); 
					  if (movepinprocess != 1) {
						  txManager.rollback(transact.status);
						  session.setAttribute("preverr", "E");
						  session.setAttribute("prevmsg"," Could not move the Pin records to process"); 
						  return "serach_home"; 
					  }*/
					 
					String readytoprocessqry = "";
					String readytoProcessHashQry="";
					if (padssenable.equals("Y")) {
						readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='" + instid
								+ "' AND ORG_CHN='" + cardno + "'";
						readytoProcessHashQry="UPDATE PERS_CARD_PROCESS_HASH  SET GENERATED_DATE=SYSDATE WHERE INST_ID='"+instid+"' "
								+ " AND  HCARD_NO='"+hcardno+"'";
					} else {
						readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='" + instid
								+ "' AND CARD_NO='" + cardno + "'";
					}
					enctrace("readytoprocessqry__" + readytoprocessqry);
					enctrace("readytoProcessHashQry :::: " + readytoProcessHashQry);
					int ucnt = commondesc.executeTransaction(readytoprocessqry, jdbctemplate);
					
					int count=jdbctemplate.update(readytoProcessHashQry);
					trace("update  in process : : "+ucnt+"    update in process hash ::: "+count);
					
					if (ucnt != 1 && count !=1) {
						txManager.rollback(transact.status);
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "COULD NOT UPDATE GENERATED DATE TO PROCESS");
						return "serach_home";
					}
					waitingmsg = "Card is waiting for CVV Data Generation";
				}else{
					waitingmsg = "Card is waiting for OTP Generation..";
				}

				changedmsg = "RE PIN";
				//waitingmsg = "Card is waiting for OTP Generation..";
				
			} else if (statuscode.equals("07")) {
				System.out.println("card status changing as reissue");

				// TEMP BLOCK to REISSUE - For Orient Start
				// System.exit(0);
				List<Map<String, Object>> cardstatuslist = null;

				cardstatuslist = commondesc.getCardStatus(instid, enccardno, jdbctemplate);

				String cardstatus = (String) cardstatuslist.get(0).get("CARD_STATUS");
				if ("02".equalsIgnoreCase(cardstatus)) {

					int switchupdate = cardmaindao.switchCardInfoUpdate(instid, hcardno, jdbctemplate);
					int produpdate = cardmaindao.updateCardProduction(instid, hcardno, jdbctemplate);
					if (switchupdate != 1 && produpdate != 1) {
						txManager.rollback(transact.status);
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", " COULD NOT UDPATE SWITCH TABLE FOR TEMP BLOCK.");
						return "serach_home";
					}
					statusmsg = " TEMP BLOCK ";
				} else {
					statusmsg = " LOST/STOLEN ";
				}
				// TEMP BLOCK to REISSUE - For Orient End

				if (padssenable.equals("Y")) {
					orderrefno = commondesc.getOrderRefNo(instid, padssenable, hcardno, TABLENAME, jdbctemplate);
				} else {
					orderrefno = commondesc.getOrderRefNo(instid, padssenable, cardno, TABLENAME, jdbctemplate);
				}

				System.out.println("order ref number ---> " + orderrefno);

				// String branchattch = commondesc.checkBranchattached(instid,
				// jdbctemplate);
				// System.out.println( "branch attached === >" +branchattch);
				card_status = "01";
				caf_recstatus = "S";
				if (orderrefno.equals("NOREC")) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Could not get order ref no for the card " + cardno);
					return "serach_home";
				}
				/*
				 * int persocalizeReissuecheck =
				 * cardmaindao.checkPersonalizecard(instid, cardno,
				 * jdbctemplate); if( persocalizeReissuecheck != 1 ){
				 * session.setAttribute("preverr", "E");
				 * session.setAttribute("prevmsg", " Given Card number [ "
				 * +cardno +"] is not a personalized card..."); return
				 * this.searchHome(); }
				 */
				int updatestatus = -1;
				String condition = "";
				if (padssenable.equals("Y")) {
					updatestatus = cardmaindao.updateCardStatusDate(instid, hcardno, "REISSUE_DATE", TABLENAME,
							jdbctemplate);
					condition = "AND HCARD_NO='" + hcardno + "'";
				} else {
					updatestatus = cardmaindao.updateCardStatusDate(instid, cardno, "REISSUE_DATE", TABLENAME,
							jdbctemplate);
					condition = "AND CARD_NO='" + cardno + "'";
				}
				if (updatestatus < 0) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not continue the process... unable to update status");
					return this.searchHome();
				}
				System.out.println("bin__" + bin);
				String branchattch = "";
				String prodcardtype_attach = "";
				List attachedtype = commondesc.checkattachedtype(instid, bin, jdbctemplate);
				Iterator attype = attachedtype.iterator();
				if (!attachedtype.isEmpty()) {
					while (attype.hasNext()) {
						Map map = (Map) attype.next();
						prodcardtype_attach = ((String) map.get("ATTACH_PRODTYPE_CARDTYPE"));
						branchattch = ((String) map.get("ATTACH_BRCODE"));
					}
				}

				Personalizeorderdetails bindetails = commondesc.gettingBindetails(instid, bin, jdbctemplate);
				// (doubt)
				Personalizeorderdetails personlizeorderdetails = commondesc
						.gettingPersonalizeorderDetailsFromProd(instid, orderrefno, condition, jdbctemplate);
				trace("instantorderdetails-------" + personlizeorderdetails);
				String breakupvalue = commondesc.getChnbreakupvalues(instid, bindetails.prodcard_expiry,
						bindetails.brcode_servicecode, personlizeorderdetails.card_type_id,
						personlizeorderdetails.sub_prod_id, personlizeorderdetails.product_code,
						personlizeorderdetails.branch_code, bindetails.apptypelen, bindetails.apptypevalue);
				trace("breakupvalue__" + breakupvalue);
				String newchn = personlizeorderdetails.bin + breakupvalue;
				trace("newchn Generated is ===>" + newchn);
				// String sequncenumber =
				// commondesc.gettingSequnceNumber(instid,personlizeorderdetails.bin,personlizeorderdetails.branch_code,bindetails.baselen_feecode,personlizeorderdetails.card_type_id
				// ,jdbctemplate, branchattch);
				String sequncenumber = commondesc.gettingSequnceNumberNew(instid, personlizeorderdetails.bin,
						personlizeorderdetails.branch_code, bindetails.baselen_feecode,
						personlizeorderdetails.card_type_id, personlizeorderdetails.sub_prod_id, jdbctemplate,
						branchattch, prodcardtype_attach);

				long sequn_no = Long.parseLong(sequncenumber);
				String strseq = Long.toString(sequn_no);
				newcardno = commondesc.generateCHN(newchn.trim(), strseq, Integer.parseInt(bindetails.baselen_feecode),
						Integer.parseInt(bindetails.chnbaseno_limitid));
				// System.out.println("newcardno --- > " + newcardno);
				if (newcardno.equals("N")) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Could not generate new card number..");
					txManager.rollback(transact.status);
					return "serach_home";
				}
				// System.out.println("NEW CARD NO __" + newcardno);
				System.out.println("caf_recstatus__" + caf_recstatus);

				// reissue move to process

				String keyid = "";
				String EDMK = "", EDPK1 = "", newenccardno = "";
				StringBuffer newhcardno = new StringBuffer();
				Properties props1 = getCommonDescProperty();
				String EDPK11 = props1.getProperty("EDPK");
				if (padssenable.equals("Y")) {

					keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					System.out.println("keyid::" + keyid);
					List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
					// System.out.println("secList::" + secList);
					Iterator secitr = secList.iterator();
					if (!secList.isEmpty()) {
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							String CDMK = ((String) map.get("DMK"));
							// String eDPK = ((String) map.get("DPK"));
							newhcardno = padsssec.getHashedValue(newcardno + instid);
							String CDPK = padsssec.decryptDPK(CDMK, EDPK11);
							newenccardno = padsssec.getECHN(CDPK, newcardno);
						}
					}
				}

				newmaskcardno = padsssec.getMakedCardno(newcardno);

				int moveprocess = -1;

				if (padssenable.equals("Y")) {
					moveprocess = moveCardToProcessForReissue(instid, padssenable, accountno, cardno, newenccardno,
							hcardno, newhcardno.toString(), newmaskcardno, caf_recstatus, card_status, usercode,
							collectbranch, jdbctemplate);
				} else {
					moveprocess = moveCardToProcessForReissue(instid, padssenable, accountno, cardno, newcardno, "", "",
							newmaskcardno, caf_recstatus, card_status, usercode, collectbranch, jdbctemplate);
				}

				if (moveprocess != 1) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Could not move the production records to process");
					txManager.rollback(transact.status);
					return "serach_home";
				}

				int reissucnt = -1;

				if (padssenable.equals("Y")) {
					reissucnt = updateReIssueCount(instid, padssenable, hcardno, jdbctemplate);
				} else {
					reissucnt = updateReIssueCount(instid, padssenable, cardno, jdbctemplate);
				}

				if (reissucnt != 1) {
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " COULD NOT UDPATE RE ISSUE COUNT.");
					return "serach_home";
				}

				trace("personlizeorderdetails.cardtypeid::::::::::" + personlizeorderdetails.card_type_id);
				String updateseq = null;
				int update_seq = 0;

				sequn_no++;
				if (branchattch.equals("Y")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND BIN='" + personlizeorderdetails.bin + "' AND BASENO_CODE='"
							+ personlizeorderdetails.branch_code + "' ";
				} else if (prodcardtype_attach.equals("C")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND BIN='" + personlizeorderdetails.bin + "' AND BASENO_CODE='"
							+ personlizeorderdetails.card_type_id + "' ";
				} else if (prodcardtype_attach.equals("P")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND BIN='" + personlizeorderdetails.bin + "' AND BASENO_CODE='"
							+ personlizeorderdetails.sub_prod_id + "' ";
				} else {
					updateseq = "UPDATE PRODUCTINFO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND PRD_CODE='" + personlizeorderdetails.bin + "'";
				}

				enctrace(" updateseqreissue : " + updateseq);
				int incseq = jdbctemplate.update(updateseq);

				System.out.println("updateseq-----> " + updateseq);
				System.out.println("updateseq-----> " + update_seq);
				String readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='"
						+ instid + "' AND ORG_CHN='" + newenccardno + "'";
				System.out.println("readytoprocessqry__" + readytoprocessqry);
				int ucnt = jdbctemplate.update(readytoprocessqry);
				if (ucnt != 1 && update_seq != 1 && incseq != 1) {
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "COULD NOT UPDATE GENERATED DATE TO PROCESS");
					return "serach_home";
				}
				changedmsg = "RE-ISSUED";
				waitingmsg = "Card Is Waiting For Authorization";
				auditbean.setRemarks("Generated New Card Is [ " + newmaskcardno + " ] ");

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

			} /*
				 * else if( statuscode.equals("10")){ System.out.println(
				 * "card status changing as damaged"); orderrefno =
				 * commondesc.getOrderRefNo(instid, cardno, jdbctemplate);
				 * System.out.println( "order ref number ---> " +orderrefno);
				 * String branchattch = commondesc.checkBranchattached(instid,
				 * jdbctemplate); System.out.println( "branch attached === >"
				 * +branchattch); card_status = "01"; caf_recstatus = "S"; if(
				 * orderrefno.equals("NOREC")){
				 * session.setAttribute("preverr","E"); session
				 * .setAttribute("prevmsg",
				 * " Could not get order ref no for the card " + cardno); return
				 * "serach_home"; } changedmsg = "DAMAGED"; waitingmsg =
				 * "Card is waiting to Authorize PRE process"; STATUS_CODE=51
				 * CARD_STATUS=10 CAF_REC_STATUS='D' select
				 * distinct(order_ref_no) as ORDER_REF_NO, CARD_NO,
				 * bin,card_type_id as CARDTYPE,sub_prod_id as
				 * SUBPRODID,product_code,EMB_NAME,to_char
				 * (generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code
				 * from PERS_CARD_PROCESS where inst_id='PRABU' and
				 * CARD_STATUS='02' and mkck_status='P' AND
				 * PRODUCT_CODE='555555142' AND BRANCH_CODE='000'AND (
				 * to_date('01-01-2014', 'dd-mm-yyyy') <= PIN_DATE AND
				 * to_date('10-09-2014', 'dd-mm-yyyy' )+1 >= PIN_DATE) order by
				 * order_ref_no }
				 */

			if (!statuscode.equals("04")) {
				// update ezcardinfo
				int updatecardinfo = -1, updatestatus = -1;
				updatecardinfo = cardmaindao.updateezcardinfo(instid, hcardno, switchcardstatus, jdbctemplate);

				// updating cardproduction
				if (padssenable.equals("Y")) {
					updatestatus = cardmaindao.updateCardStatus(instid, padssenable, enccardno, statuscode,
							switchcardstatus, caf_recstatus, usercode, TABLENAME, collectbranch, jdbctemplate);
				} else {
					updatestatus = cardmaindao.updateCardStatus(instid, padssenable, cardno, statuscode,
							switchcardstatus, caf_recstatus, usercode, TABLENAME, collectbranch, jdbctemplate);
				}

				if (updatestatus < 0 && updatecardinfo < 0) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not continue the process... unable to update status");
					return this.searchHome();
				}
				int inserthist = cardmaindao.insertMainintainHistory(instid, cardno, card_current_status, statuscode,
						usercode, jdbctemplate);
				if (inserthist < 0) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not continue the process... Could not insert history..");
					return this.searchHome();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to update the status");
			e.printStackTrace();
			return "serach_home";
		}

		/************* AUDIT BLOCK **************/
		try {

			// added by gowtham_220719
			trace("ip address======>  " + ip);
			auditbean.setIpAdress(ip);

			auditbean.setActmsg("Status changed as [ " + changedmsg + " ] ");
			auditbean.setUsercode(username);
			auditbean.setCardno(mcardno);
			// System.out.println("encrypted card number" +
			// enccardnumber.length());
			auditbean.setCardnumber(enccardno);
			// trace(auditbean.getCardnumber());

			// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
			// jdbctemplate, txManager);
			commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
		} catch (Exception audite) {
			trace("Exception in auditran : " + audite.getMessage());
		}

		/************* AUDIT BLOCK **************/

		txManager.commit(transact.status);
		session.setAttribute("preverr", "S");
		if (statuscode.equals("07")) {
			session.setAttribute("prevmsg", "New Card Generated [ " + padsssec.getMakedCardno(newcardno) + " ] For "
					+ statusmsg + " Card [ " + mcardno + " ] ." + waitingmsg);

			/************* AUDIT BLOCK **************/
			try {
				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg(
						" New Card Generated [ " + newmaskcardno + " ] For Lost/Stolen Card [ " + mcardno + " ] ");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("4104");
				auditbean.setCardno(mcardno);
				// auditbean.setCardnumber(enccardno);
				// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
				// jdbctemplate, txManager);
				commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} else {
			session.setAttribute("prevmsg",
					mcardno + " Card Status has been marked as " + changedmsg + "." + waitingmsg);

		}

		// added by gowtham_060819
		// nullify

		// System.out.println(cardno);
		cardno = "0000000000000000";
		sb = new StringBuilder(cardno);
		sb.setLength(0);
		cardno = null;
		sb = null;

		System.out.println(cardno);

		return "serach_home";

	}

	public String rePinHome() {
		HttpSession session = getRequest().getSession();
		try {
			return "instantrepin_home";
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			return "required_home";
		}
	}

	public String rePinView() {
		System.out.println("Repin -view");

		HttpSession session = getRequest().getSession();

		String instid = comInstId();
		String cardno = getRequest().getParameter("cardno").trim();
		try {
			int cardexist = cardmaindao.checkCardExist(instid, cardno, jdbctemplate);
			if (cardexist <= 0) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "[" + cardno + "] is not a valid card...");
				return this.rePinHome();
			}

			List custdata = cardmaindao.getCardDetails(instid, cardno, jdbctemplate);
			if (custdata == null || custdata.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not get data for the card [" + cardno + "]...");
				return this.rePinHome();
			} else {
				ListIterator itr = custdata.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String statuscode = (String) mp.get("CARD_STATUS");
					System.out.println("Card status is " + statuscode);
					if (!statuscode.equals(REPINSTATUSCODE)) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Card number [ " + cardno
								+ "] Not marked as Repin. Change the status and Try again...");
						return this.rePinHome();
					}

					int persocalizecheck = cardmaindao.checkCardExistinPersonalization(instid, cardno, jdbctemplate);
					if (persocalizecheck == 1) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Card number [ " + cardno + "] under process...");
						return this.rePinHome();
					}

					cardmainbean.setCardno(cardno);

					String customerid = (String) mp.get("CIN");
					cardmainbean.setCustomerid(customerid);
					cardmainbean.setProductcode((String) mp.get("PRODUCT_CODE"));
					cardmainbean.setSubproductcode((String) mp.get("SUB_PROD_ID"));
					cardmainbean.setCardstatus((String) mp.get("CARD_STATUS"));
					cardmainbean.setExpiredate((String) mp.get("EXPIRY_DATE"));
					String username = commondesc.getUserName(instid, (String) mp.get("MAKER_ID"), jdbctemplate);

					if (username == null) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg",
								"Unable to process. User name not found [ " + (String) mp.get("MAKER_ID") + "]...");
						return this.rePinHome();
					}

					cardmainbean.setUsername(username);
					cardmainbean.setStatusmakreddate((String) mp.get("MAKER_DATE"));

					InstCardRegisterProcessDAO custregdao = new InstCardRegisterProcessDAO();
					List customerdetails = custregdao.getCustomerDetails(instid, customerid, jdbctemplate);
					if (customerdetails == null) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg",
								"Could not get customer details for the Card number [ " + cardno + "]...");
						return this.rePinHome();
					} else {
						ListIterator custitr = customerdetails.listIterator();
						while (custitr.hasNext()) {
							Map custmp = (Map) custitr.next();
							String fname = custmp.get("FNAME").toString();
							String mname = custmp.get("MNAME").toString();
							String lname = custmp.get("LNAME").toString();
							String customername = fname + " " + mname + " " + lname;
							cardmainbean.setCustomername(customername);
							cardmainbean.setPhoneno((String) custmp.get("MOBILE_NO"));
							cardmainbean.setMobileno((String) custmp.get("PHONE_NO"));
						}
					}
				}
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			return "required_home";
		}

		return "instantrepin_view";
	}

	public String rePinAction() {
		trace("****rePinAction****");
		enctrace("****rePinAction****");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("REPINACT", txManager);

		String instid = comInstId();
		String usercode = comUserCode();
		String cardno = getRequest().getParameter("cardno").trim();

		HttpServletRequest request = getRequest();
		try {

			String switchcardstatus = "50";// commondesc.getSwitchCardStatus(instid,
											// DAMAGESTATUSCODE, jdbctemplate);
			trace("Getting switch card status..." + switchcardstatus);
			String cafrecstatus = "A";

			String curruntstatus = commondesc.getCardCurrentStatus(instid, padssenable, cardno, TABLENAME,
					jdbctemplate);
			trace("Getting current card status..." + curruntstatus);
			if (curruntstatus == null) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the process..unable to get card status...");
				trace("Could not continue the process..unable to get card status...");
				return this.rePinHome();
			}

			/*
			 * int updatestatus = cardmaindao.updateCardStatus( instid, cardno,
			 * DAMAGESTATUSCODE, switchcardstatus,cafrecstatus, usercode,
			 * jdbctemplate ); trace("Updated status...:"+updatestatus); if(
			 * updatestatus < 0 ){ txManager.rollback(transact.status);
			 * session.setAttribute("preverr", "E");
			 * session.setAttribute("prevmsg",
			 * "Could not continue the process..unable update the status..");
			 * return this.rePinHome(); }
			 */
			int inserthist = cardmaindao.insertMainintainHistory(instid, cardno, curruntstatus, ACTIVESTATUCODE,
					usercode, jdbctemplate);
			trace("Inserted history  status...:" + inserthist);
			if (inserthist < 0) {
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the process..unable insert history records...");
				return this.rePinHome();
			}

			// RepinService repinservince = new RepinService();
			int sendpin = -1;
			// repinservince.sendRepin(instid, cardno, session, request,
			// jdbctemplate);
			trace("Sending pin request got...:" + sendpin);
			if (sendpin != 1) {
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the proces. ");
				return this.rePinHome();
			}

			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Pin has changed succesfully for the card [ " + cardno
					+ " ]. New pin has sent to the customer mobile number..");
			trace("Pin has changed succesfully for the card [ " + cardno
					+ " ]. New pin has sent to the customer mobile number..");
			txManager.commit(transact.status);

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			e.printStackTrace();
			return "required_home";
		}

		return this.rePinHome();
	}

	public String reIssueCardHome() {
		HttpSession session = getRequest().getSession();
		try {
			return "instantreissue_home";
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			return "required_home";
		}
	}

	public String reIssuCardView() {
		System.out.println("Reissue -view");

		HttpSession session = getRequest().getSession();

		String instid = comInstId();
		String cardno = getRequest().getParameter("cardno").trim();
		try {
			int cardexist = cardmaindao.checkCardExist(instid, cardno, jdbctemplate);
			if (cardexist <= 0) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "[" + cardno + "] is not a valid card...");
				return this.reIssueCardHome();
			}

			List custdata = cardmaindao.getCardDetails(instid, cardno, jdbctemplate);
			if (custdata == null || custdata.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not get data for the card [" + cardno + "]...");
				return this.reIssueCardHome();
			} else {
				ListIterator itr = custdata.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String statuscode = (String) mp.get("CARD_STATUS");
					System.out.println("Card status is " + statuscode);
					if (!statuscode.equals(REISSUSTATUSCODE)) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Card number [ " + cardno
								+ "] Not marked as Re-Issue. Change the status and Try again...");
						return this.reIssueCardHome();
					}

					int persocalizecheck = cardmaindao.checkCardExistinPersonalization(instid, cardno, jdbctemplate);
					if (persocalizecheck == 1) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Card number [ " + cardno + "] under process...");
						return this.reIssueCardHome();
					}

					cardmainbean.setCardno(cardno);

					String customerid = (String) mp.get("CIN");
					cardmainbean.setCustomerid(customerid);
					cardmainbean.setProductcode((String) mp.get("PRODUCT_CODE"));
					cardmainbean.setSubproductcode((String) mp.get("SUB_PROD_ID"));
					cardmainbean.setCardstatus((String) mp.get("CARD_STATUS"));
					cardmainbean.setExpiredate((String) mp.get("EXPIRY_DATE"));
					String username = commondesc.getUserName(instid, (String) mp.get("MAKER_ID"), jdbctemplate);

					if (username == null) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg",
								"Unable to process. User name not found [ " + (String) mp.get("MAKER_ID") + "]...");
						return this.reIssueCardHome();
					}

					cardmainbean.setUsername(username);
					cardmainbean.setStatusmakreddate((String) mp.get("MAKER_DATE"));

					InstCardRegisterProcessDAO custregdao = new InstCardRegisterProcessDAO();
					List customerdetails = custregdao.getCustomerDetails(instid, customerid, jdbctemplate);
					if (customerdetails == null) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg",
								"Could not get customer details for the Card number [ " + cardno + "]...");
						return this.reIssueCardHome();
					} else {
						ListIterator custitr = customerdetails.listIterator();
						while (custitr.hasNext()) {
							Map custmp = (Map) custitr.next();
							String fname = (String) custmp.get("FNAME");
							String mname = (String) custmp.get("MNAME");
							String lname = (String) custmp.get("LNAME");
							String customername = fname + " " + mname + " " + lname;
							cardmainbean.setCustomername(customername);
							cardmainbean.setPhoneno((String) custmp.get("MOBILE_NO"));
							cardmainbean.setMobileno((String) mp.get("PHONE_NO"));
						}
					}
				}
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			return "required_home";
		}
		return "instantreissue_view";
	}

	public String reIssuCardAction() {
		System.out.println("Re-issue action.........");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("REISSUACT", txManager);

		String instid = comInstId();
		String usercode = comUserCode();
		String oldcardno = getRequest().getParameter("cardno");
		String newcardno = getRequest().getParameter("newcardno");
		try {

			// trace("Comparing old card number first 9 digit[" +
			// oldcardno.substring(0, 9)
			// + "] and new Card number first 9 digit [ " +
			// newcardno.substring(0, 9) + " ] ");
			if (!oldcardno.substring(0, 9).equals(newcardno.substring(0, 9))) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "OldCard Number and New Cardnumber is not same product.");
				// trace("OldCard Number and New Cardnumber is not same
				// product.");
				return this.reIssueCardHome();
			}

			/*
			 * String acctno = cardmaindao.getAccountNumberByCard(instid,
			 * newcardno, jdbctemplate); if( acctno == null ){
			 * session.setAttribute("preverr", "E");
			 * session.setAttribute("prevmsg",
			 * "Unable to contine the process...Could not get the account number."
			 * ); return this.reIssueCardHome(); }
			 * 
			 * int deletecard = cardmaindao.deleteCardFromProduction(instid,
			 * newcardno, jdbctemplate); if( deletecard < 0 ){
			 * txManager.rollback(transact.status);
			 * session.setAttribute("preverr", "E");
			 * session.setAttribute("prevmsg",
			 * "Unable to contine the process...Could not remove the existing card."
			 * ); return this.reIssueCardHome(); }
			 * 
			 * int deleteacctno = cardmaindao.deleteAccountNumber(instid,
			 * acctno, jdbctemplate); if( deleteacctno < 0 ){
			 * txManager.rollback(transact.status);
			 * session.setAttribute("preverr", "E");
			 * session.setAttribute("prevmsg",
			 * "Unable to contine the process...Could not remove exising the acct number."
			 * ); return this.reIssueCardHome(); }
			 * 
			 * int delteacctlink = cardmaindao.deleteCardAcctLink(instid,
			 * newcardno, jdbctemplate); if( delteacctlink < 0 ){
			 * txManager.rollback(transact.status);
			 * session.setAttribute("preverr", "E");
			 * session.setAttribute("prevmsg",
			 * "Unable to contine the process...Could not remove account link.."
			 * ); return this.reIssueCardHome(); }
			 * 
			 * String switchardstatus = commondesc.getSwitchCardStatus(instid,
			 * NOTACTIVATEDSTATUSCODE, jdbctemplate); String cafrecstatus = "A";
			 */

			int delteacctlink = cardmaindao.deleteCardAcctLink(instid, newcardno, jdbctemplate);
			if (delteacctlink < 0) {
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Unable to contine the process...Could not remove account link..");
				return this.reIssueCardHome();
			}

			String customerid = commondesc.getCustomerIdByCardNumber(instid, oldcardno, jdbctemplate);
			int reissuecard = cardmaindao.reIssueCardProduction(instid, oldcardno, newcardno, customerid, jdbctemplate);
			if (reissuecard < 0) {
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Unable to contine the process...Re issue the card.");
				return this.reIssueCardHome();
			}

			int acctlink = cardmaindao.updateCardAccountLink(instid, oldcardno, newcardno, jdbctemplate);
			if (acctlink < 0) {
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Unable to contine the process...Could not update the account status");
				return this.reIssueCardHome();
			}

			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Card Re-issued succesfully with the cardnumber [ " + newcardno + " ]");

		} catch (Exception e) {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			return "required_home";
		}

		return this.reIssueCardHome();
	}

	public void checkValidForReIssueCard() throws IOException {
		trace("validating  card....");

		String instid = comInstId();
		String cardno = getRequest().getParameter("cardno");
		String result = "0";
		try {
			trace("Checking card no is valid : " + cardno);
			int checkactivecard = cardmaindao.checkValidCardForReissue(instid, cardno, NOTACTIVATEDSTATUSCODE,
					jdbctemplate);

			if (checkactivecard == 1) {
				result = "1";
			} else {
				result = "0";
			}
		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
			result = "0";
		}
		getResponse().getWriter().write(result);
	}

	public String reDamageCardHome() {
		HttpSession session = getRequest().getSession();
		try {
			return "instantdamage_home";
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			return "required_home";
		}
	}

	public int updateReIssueCount(String instid, String padssenable, String chn, JdbcTemplate jdbctemplate) {

		String padsscon = "";
		/*
		 * if (padssenable.equals("Y")) { padsscon = "HCARD_NO"; } else {
		 * padsscon = "CARD_NO"; }
		 */

		String fchupdcntqry = "SELECT REISSUE_CNT FROM " + TABLENAME + " WHERE INST_ID='" + instid
				+ "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + chn + "')";
		enctrace("fchupdcntqry::" + fchupdcntqry);
		int updcnt = 0;
		try {
			updcnt = jdbctemplate.queryForInt(fchupdcntqry);
		} catch (EmptyResultDataAccessException e) {
			updcnt = 0;
		}

		int newcnt = updcnt + 1;
		String cntqry = "UPDATE " + TABLENAME + " SET REISSUE_CNT= '" + newcnt
				+ "', REISSUE_DATE=sysdate WHERE INST_ID='" + instid
				+ "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + chn + "')";
		enctrace("REISSUE UPDATE QRY __" + cntqry);
		int x = commondesc.executeTransaction(cntqry, jdbctemplate);
		return x;

	}

	public int movePintoProcess(String instid, String cardno, String newcardno, JdbcTemplate jdbctemplate,
			HttpSession session) {
		String pinqry = "";

		try {
			pinqry += "INSERT INTO IFP_PIN_PROCESS (";
			pinqry += "INST_ID, CARD_NO, PIN_OFFSET, CVV1, CVV2, PVV, PIN_DATE, ICVV, OLD_PIN_OFFSET, OLD_PIN_DATE, ORDER_FLAG, USER_CODE )";
			pinqry += "SELECT INST_ID, '" + newcardno
					+ "', PIN_OFFSET, CVV1, CVV2, PVV, PIN_DATE, ICVV, OLD_PIN_OFFSET, OLD_PIN_DATE, 'M', USER_CODE FROM  IFP_PIN_PRODUCTION";
			pinqry += " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno + "'";
			System.out.println(pinqry);
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error while insert pin data to process");
			return -1;

		}

		int x = commondesc.executeTransaction(pinqry, jdbctemplate);
		return x;
	}

	public int moveCardToProcessForReissue(String instid, String padssenable, String accountno, String cardno,
			String newcardno, String hashcardno, String newhashcardno, String newmaskcardno, String caf_recstatus,
			String card_status, String usercode, String collectbranch, JdbcTemplate jdbctemplate) throws Exception {
		int result = 0;
		String cond = "";
		String keyid = "";
		String EDMK = "", EDPK = "", clearchn = "";
		PadssSecurity padsssec = new PadssSecurity();

		// System.out.println("newcardno------>" + newcardno);
		// System.out.println("newhashcardno-->" + newhashcardno);
		System.out.println("newmaskcardno----->" + newmaskcardno);

		// BY SIVA
		/*
		 * Properties props=getCommonDescProperty(); String
		 * EdPK=props.getProperty("EDPK"); if (padssenable.equals("Y")) {
		 * 
		 * keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		 * System.out.println("keyid::" + keyid); List secList =
		 * commondesc.getPADSSDetailById(keyid, jdbctemplate);
		 * System.out.println("secList::" + secList); Iterator secitr =
		 * secList.iterator(); if (!secList.isEmpty()) { while
		 * (secitr.hasNext()) { Map map = (Map) secitr.next(); String CDMK =
		 * ((String) map.get("DMK")); //String eDPK = ((String) map.get("DPK"));
		 * String CDPK = padsssec.decryptDPK(CDMK, EdPK); clearchn =
		 * padsssec.getCHN(CDPK, newcardno); } }
		 * 
		 * 
		 * }
		 */ /*
			 * else { cond = "AND CARD_NO='" + cardno + "'"; }
			 */

		cond = "AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + hashcardno + "')";

		String orderrefno = commondesc.generateorderRefno(instid, jdbctemplate);
		trace("Generated order reference number is : " + orderrefno);
		
		//added on 20/05/2021 for cardrefno generation
		String cardrefno="SELECT SUB_PROD_ID FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + hashcardno + "'";
		enctrace("existing card sub product id-->"+cardrefno);
		String subprodid=(String) jdbctemplate.queryForObject(cardrefno,String.class);
		trace("Existing card sub product id -->"+subprodid);
		
		String cardrefnulen = commondesc.getCardReferenceNumberLen(instid, jdbctemplate);
		trace("Got : " + cardrefnulen);
		if (cardrefnulen == null) {
			addActionError("Could not generate card... Card Referene number length is empty...");
			trace("Could not generate card... Card Referene number length is empty...");
			return result;
		}
		
String card_ref_no = commondesc.generateCardRefNumber(instid, subprodid, cardrefnulen,
				jdbctemplate);
		trace("Got cardrefno : " + card_ref_no);
		if (card_ref_no == null) {
		   addActionError("Could not generate card... Got Card Referene number  is empty...");
			trace("Could not generate card... Card Referene number  is empty...");
			 return result;
		}
		
		System.out.println("card_ref_no-->"+card_ref_no);

		String expperiod = commondesc.getSubProductExpPeriodwithCardno(instid, cond, jdbctemplate);
		String expirydatecond = "add_months(sysdate," + expperiod + ")";

		String query = "";
		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,PANSEQ_NO,CARD_COLLECT_BRANCH,CARD_REF_NO)";

		query += " SELECT INST_ID, '" + newmaskcardno + "','" + accountno
				+ "',ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, '" + orderrefno + "', '" + card_status
				+ "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, TRIM(BRANCH_CODE), PC_FLAG, CARD_CCY,";
		query += "GENERATED_DATE, " + expirydatecond + ", PRE_DATE, '" + usercode
				+ "', MAKER_DATE, CHECKER_ID, CHECKER_DATE, 'M', SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '" + caf_recstatus
				+ "', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, '" + newcardno
				+ "', USED_CHN,COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,'01',CARD_COLLECT_BRANCH,'"+card_ref_no+"'  FROM " + TABLENAME + " WHERE INST_ID='"
				+ instid + "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
				+ hashcardno + "') ";

		enctrace("moveCardToProcessreissucard : " + query);
		int x = jdbctemplate.update(query);

		// decode(REISSUE_CNT,'0',REISSUE_CNT+2,REISSUE_CNT+1
		/*
		 * if (padssenable.equals("Y")) { query += "AND HCARD_NO='" + hashcardno
		 * + "'"; } else {
		 */

		// query += "AND ORG_CHN='" + cardno + "'";

		// }

		String query1 = "";
		query1 = "INSERT INTO  PERS_CARD_PROCESS_HASH (";
		query1 += "INST_ID, HCARD_NO,ACCT_NO, CIN, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, ";
		query1 += "GENERATED_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ";
		query1 += "BIN)";

		query1 += " SELECT INST_ID,'" + newhashcardno + "','" + accountno + "', CIN, '" + orderrefno
				+ "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, TRIM(BRANCH_CODE), ";
		query1 += "GENERATED_DATE, MAKER_DATE, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ";
		query1 += "BIN FROM " + TABLENAME + " WHERE INST_ID='" + instid
				+ "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + hashcardno
				+ "') ";

		enctrace("moveCardToProcessreissucard : " + query1);
		int x1 = jdbctemplate.update(query1);

		if (x == 1 && x1 == 1) {
			result = 1;
		}

		return result;
	}

	public int moveCardToProcess(String instid, String padssenable, String accountno, String cardno, String newcardno,
			String hashcardno, String newhashcardno, String newmaskcardno, String caf_recstatus, String card_status,
			String usercode, String collectbranch, JdbcTemplate jdbctemplate) throws Exception {

		String query = "";

		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH)";

		query += " SELECT INST_ID,'" + newmaskcardno + "', '" + accountno
				+ "',ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, '" + card_status
				+ "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "GENERATED_DATE,EXPIRY_DATE, PRE_DATE, '" + usercode
				+ "', MAKER_DATE, CHECKER_ID, CHECKER_DATE, 'P', SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '" + caf_recstatus
				+ "', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH FROM " + TABLENAME + "  WHERE INST_ID='"
				+ instid + "' ";

		String productionHashQuery = "INSERT INTO  PERS_CARD_PROCESS_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,CIN,CARD_TYPE_ID,"
				+ "SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE) "
				+ " SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,"
				+ "GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+newhashcardno+"'";

		if (padssenable.equals("Y")) {
			query += "AND ORG_CHN='" + newcardno + "'";
		} else {
			query += "AND CARD_NO='" + cardno + "'";
		}
		enctrace("moveCardToProcess : " + query);
		enctrace("productionHashQuery  :: "+productionHashQuery);

		int result=0;
		int x = jdbctemplate.update(query);
		int y= jdbctemplate.update(productionHashQuery);
		
		if(x==1 && y==1){
			result=1;
		}
		
		return result;
	}

	public String getAcccountno() {
		return acccountno;
	}

	public void setAcccountno(String acccountno) {
		this.acccountno = acccountno;
	}

	public String getMcardno() {
		return mcardno;
	}

	public void setMcardno(String mcardno) {
		this.mcardno = mcardno;
	}

	public String getHcardno() {
		return hcardno;
	}

	public void setHcardno(String hcardno) {
		this.hcardno = hcardno;
	}

	public String getPadssenable() {
		return padssenable;
	}

	public void setPadssenable(String padssenable) {
		this.padssenable = padssenable;
	}

	public String getSwitchCardStatusCode(String instid, String hcardno, JdbcTemplate jdbcTemplate) {

		String statusqry = "SELECT STATUS FROM EZCARDINFO WHERE CHN='" + hcardno + "'  AND INSTID='" + instid
				+ "' AND ROWNUM<=1";
		trace("getSwitchCardStatusCodeQry***" + statusqry);
		String statusdesc = (String) jdbcTemplate.queryForObject(statusqry, String.class);
		return statusdesc;
	}

	public String generateSearchListForAcctNo(String instid, String cardno, String h_cardno, String ecardno, String qry,
			String selectedtype, JdbcTemplate jdbctemplate, HttpSession session) throws Exception {
		trace(":::generateSearchListForAcctNo()  :::::::::  starts "+selectedtype);

				
		PadssSecurity padsssec = new PadssSecurity();
		String keyid="",clearCardnumber="",CDPK="",hashedCardNumber="";
		  Properties props = getCommonDescProperty();
			String EDPK = props.getProperty("EDPK");
		 trace("edpk---->" + EDPK);


				keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				// System.out.println("keyid::" + keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				// System.out.println("secList::" + secList);
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String CDMK = ((String) map.get("DMK"));
						 CDPK = padsssec.decryptDPK(CDMK, EDPK);
						trace("CDPK-->" + CDPK);
					}
				}

		int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);
		if (maint < 0) {
			return "required_home";
		}
		
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		System.out.println("padssenable:::" + padssenable);
		String datatable = "";
		String card_status_desc = "";
		String cardnumber = "", accountrelation = ""  ,Hcardno="";

		boolean alt = true;
		int cnt = 0;

		List accountrel = commondesc.getAccountdesc(instid, cardno, jdbctemplate);
		trace("accountauhrel result=== " + accountrel);
		if (!accountrel.isEmpty()) {
			Iterator acctitr = accountrel.iterator();
			while (acctitr.hasNext()) {
				Map acctmp = (Map) acctitr.next();
				// qry = "SELECT * FROM CARD_PRODUCTION WHERE ORG_CHN='" +
				// ecardno + "' ";
				qry = "SELECT * FROM CARD_PRODUCTION WHERE ORDER_REF_NO  IN (SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE  HCARD_NO='"
						+ (String) acctmp.get("CHN") + "') ";
				 Hcardno = (String) acctmp.get("CHN");
				// trace(" Hcardnoquery is " + Hcardno);
				trace(" account mapping query is ::::  " + qry);
				List card_data = jdbctemplate.queryForList(qry);
				if (!card_data.isEmpty()) {
					Iterator itr = card_data.iterator();
					while (itr.hasNext()) {

						Map mp = (Map) itr.next();

						cardnumber = (String) mp.get("ORG_CHN");
						// trace(" cardnumber is " + cardnumber);
						String mcardno = (String) mp.get("MCARD_NO");
						// String hcardno = (String) mp.get("HCARD_NO");
						
						clearCardnumber = padsssec.getCHN(CDPK, cardnumber);
						hashedCardNumber = padsssec.getHashedValue(clearCardnumber +instid).toString();
				
						trace("clearCardnumber:::" + clearCardnumber  +"   ecardno:::     " + cardnumber+"   cardno:::" + hashedCardNumber);				
						String accountno = (String) mp.get("ACCOUNT_NO");
						String order_refno = (String) mp.get("ORDER_REF_NO");
						String bin = (String) mp.get("BIN");
						// String activedate = (String)mp.get("ACTIVE_DATE");

						String brcode = (String) mp.get("CARD_COLLECT_BRANCH");
						String brdec = commondesc.getBranchDesc(instid, brcode, jdbctemplate);
						String brcodedes = brcode + "-" + brdec;
						String bindesc = commondesc.getCardTypeDesc(instid, bin, jdbctemplate);
						String status_code = commondesc.getSTATUSFromEZLINK(instid, hashedCardNumber, jdbctemplate);
						System.out.println("status_code---> " + status_code + "hcardno" + Hcardno);

						if (status_code == null || status_code.equals("")) {
							datatable = "SWITCHNOTMATCH";
							return datatable;
						}

						String product_code = commondesc.getProductdesc(instid, (String) mp.get("PRODUCT_CODE"),
								jdbctemplate);
						String custid = (String) mp.get("CIN");
						String cafrec = (String) mp.get("CAF_REC_STATUS");
						String mobileno = (String) mp.get("MOBILENO");
						String org_chn = (String) mp.get("ORG_CHN");
						String subproduct = (String) commondesc.getSubProductdesc(instid,
								(String) mp.get("SUB_PROD_ID"), jdbctemplate);
						if (mobileno == null) {
							mobileno = "--";
						}
						System.out.println("MOBILE NUMBER_____________" + mobileno);
						System.out.println("custid__" + custid);
						Object dateofbirth;
						String customername;
						String subproductid = (String) mp.get("SUB_PROD_ID");
						dateofbirth = (Object) commondesc.fchCustDOB(instid, custid, jdbctemplate);
						if (dateofbirth == null) {
							dateofbirth = "--";
						}
						// customername = (String)
						// commondesc.fchCustName(instid,custid, jdbctemplate);

						String embName = (String) commondesc.fchEmbName1(instid, cardnumber, jdbctemplate);

						Date expdate = (Date) mp.get("EXPIRY_DATE");

						String cardstatuscode = commondesc.getCardStatusCode(instid, status_code, jdbctemplate);
						String cardstatusdesc = commondesc.getCardStatusDesc(instid, cardstatuscode, jdbctemplate);
						card_status_desc = "<span style='color:red'>" + cardstatusdesc + "</span>";

						List accountrellist = commondesc.getAccountdesc(instid, hashedCardNumber, accountno, jdbctemplate);
						trace("checking account desc-->" + accountrellist);
						if (accountrellist.isEmpty()) {
							accountrel = commondesc.getAccountdescFromProd1(instid, cardnumber, accountno, jdbctemplate);
							if (accountrellist.isEmpty()) {
								accountrelation = "--";
							} else {
								accountrelation = (String) acctmp.get("ACCOUNTDESC");
							}
						}

						else {
							accountrelation = (String) acctmp.get("ACCOUNTDESC");
							trace("getting account desc-->" + accountrelation);
						}

						if ("null".equalsIgnoreCase(accountrelation)) {
							accountrelation = "";
						}
						Date now = new Date();

						if (expdate.after(now)) {
							System.out.println("Not expired__" + expdate);
						} else {
							System.out.println("expired__" + expdate);
							card_status_desc = "<span style='color:red'> EXPIRED </span>";
						}

						String trclass = "";
						if (alt) {
							trclass = "class='alt'";
							alt = false;
						} else {
							alt = true;
						}
						cnt++;
						// "+trclass+"
						// onclick='showMaintain('"+instid+"','"+cardnumber+"')
						if (!"acctnum".equalsIgnoreCase(selectedtype)) {
							if (padssenable.equals("Y")) {
								datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
										+ "  <td  onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value) onclick=showMaintain('"
										+ instid + "','" + hashedCardNumber + "','" + mcardno + "','" + clearCardnumber.toString()
										+ "')   ><td>" + mcardno + "</td>  <td>" + accountno + "</td><td>" + mobileno
										+ "</td> <td>" + order_refno + "</td>  <td>" + embName + "</td> <td>"
										+ dateofbirth + "</td> <td>" + custid + "</td>  <td>" + bindesc + "</td>  <td>"
										+ brcodedes + " </td> <td>" + card_status_desc + "</td> <td>" + product_code
										+ "</td></tr>";
							} else {
								datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
										+ " <td onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value)  onclick=showMaintain('"
										+ instid + "','" + clearCardnumber + "','" + Hcardno + "')   ><td>" + mcardno
										+ "</td>  <td>" + accountno + "</td><td>" + mobileno + "</td> <td>"
										+ order_refno + "</td>  <td>" + embName + "</td> <td>" + dateofbirth
										+ "</td> <td>" + custid + "</td>  <td>" + bindesc + "</td>   <td>" + brcodedes
										+ " </td><td>" + card_status_desc + "</td> <td>" + product_code
										+ "</td>       </tr>";
							}
						} else {
							datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
									+ " <td onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value) onclick=showMaintain('"
									+ instid + "','" + hashedCardNumber + "','" + mcardno + "','" + clearCardnumber.toString()
									+ "')   > <td>" + mcardno + "</td>  <td>" + accountno + "</td><td>" + mobileno
									+ "</td>  <td>" + embName + "</td> <td>" + custid + "</td>  <td>" + card_status_desc
									+ "</td> <td>" + product_code + "</td> <td>" + brcodedes + " </td> <td>"
									+ accountrelation + "</td>     </tr>";
						}

						/*
						 * if(!"acctnum".equalsIgnoreCase(selectedtype)){
						 * datatable += "<tr class='rowrec' id='recordrow"+cnt+
						 * "' "+trclass+
						 * "  <td  onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value) onclick=showMaintain('"
						 * +instid+"','"+hcardno+"','"+mcardno+"','"+cardnumber.
						 * toString ()+"')   ><td>"+mcardno+"</td>  <td>"
						 * +accountno +"</td><td>" +mobileno+"</td> <td>"
						 * +order_refno+"</td>  <td>" +embName +"</td> <td>"
						 * +dateofbirth+"</td> <td>"+custid+ "</td>  <td>"
						 * +bindesc+"</td>  <td>"+card_status_desc+"</td> <td>"+
						 * product_code +"</td></tr>"; } else { datatable +=
						 * "<tr class='rowrec' id='recordrow"+cnt+"' "+trclass+
						 * " <td onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value)  onclick=showMaintain('"
						 * + instid+"','"+cardno+"','"+mcardno+"')   ><td>"
						 * +mcardno + "</td>  <td>"
						 * +accountno+"</td><td>"+mobileno+"</td> <td>"
						 * +order_refno+"</td>  <td>"+embName+"</td> <td>"+
						 * dateofbirth +"</td> <td>"+custid+"</td>  <td>"
						 * +bindesc+ "</td>  <td>"+card_status_desc+"</td> <td>"
						 * + product_code +"</td>       </tr>"; }
						 */
						// <td>"+ subproduct +"</td>
					}
				}

			}

		} else {
			datatable = "NOREC";
		}
		trace("datatable:::::::datatable" + datatable);
		trace(":::generateSearchListForAcctNo()  :::::::::  Ends ");
		return datatable;
	}

	public String viewsearchHome() {

		System.out.println("TABLENAME :" + TABLENAME);
		trace("*************** CardMaintainAction Begins **********");
		enctrace("*************** CardMaintainAction Begins **********");
		String instid = comInstId();
		HttpSession session = getRequest().getSession();

		try {
			int reqch = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if (reqch < 0) {
				return "required_home";
			}

			int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);
			if (maint < 0) {
				return "required_home";
			}

			String cardmaintdesc = "";

			String act = getRequest().getParameter("act");
			if (act != null) {
				session.setAttribute("act", act);
			}

			String mact = getRequest().getParameter("mact");

			if (Integer.parseInt(mact) != 0) {
				cardmaintdesc = commondesc.getcardmaintaindesc(instid, mact, jdbctemplate);
				System.out.println("getting maintain desc value" + cardmaintdesc);

			}
			session.setAttribute("maintdesc", cardmaintdesc);
			session.setAttribute("maintcode", mact);
		}

		catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the process....");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "viewserach_home";

	}

	public void viewsearchList() throws Exception {
		trace("Searchig list....");
		enctrace("Searchig list....");
		String result = "";
		String instid = comInstId();
		HttpSession session = getRequest().getSession();
		String selectedtype = (String) getRequest().getParameter("selectedtype");

		String maintdesc = (String) getRequest().getParameter("maintdesc");
		trace("card maintanence : " + maintdesc);
		String cardno = commondesc.escSql(getRequest().getParameter("cardno").trim());
		// trace("cardno : " + cardno);
		String accountno = "";// commondesc.escSql(
								// getRequest().getParameter("accountno").trim()
								// );
		trace("accountno : " + accountno);
		StringBuffer hcardno = new StringBuffer();
		String custno = "";// commondesc.escSql(
							// getRequest().getParameter("custno").trim() );
		trace("custno::" + custno);
		String dob = "";// commondesc.escSql(
						// getRequest().getParameter("dob").trim() );
		trace("dob::" + dob);
		String custname = "";// commondesc.escSql(
								// getRequest().getParameter("custname").trim()
								// );
		trace("custname::" + custname);
		String mobileno = "";// commondesc.escSql(
								// getRequest().getParameter("mobileno").trim()
								// );
		trace("mobileno::" + mobileno);
		String qrycond = "";
		String searchqry = "";
		Boolean crdbase = false, custbase = false;
		String cardnocond = "", custnocond = "", dobcond = "", custnamecond = "", orderrecond = "", mobilenocond = "";

		String branchcode = comBranchId();

		String branchCondition = "";
		String usertype = comuserType();

		System.out.println("usertype::" + usertype);
		String ecardno = "";
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		if (selectedtype.equals("acctnum")) {
			if (cardno != "" && !cardno.equals("null")) {

				List cardlist = commondesc.getCardnofromprod(cardno, instid, jdbctemplate);
				if (!cardlist.isEmpty()) {
					HashMap cardmap = (HashMap) cardlist.get(0);
					ecardno = (String) cardmap.get("CARD_NO");
					hcardno.append(cardmap.get("HCARD_NO"));

				}

				crdbase = true;
				if (usertype.equals("BRANCHUSER")) {
					cardnocond = "AND ( ACCOUNT_NO LIKE '%" + cardno + "')";
				} else {
					cardnocond = "AND ( ACCOUNT_NO LIKE '%" + cardno + "' )";
				}
			}

		}
		if (selectedtype.equals("custnum")) {
			if (cardno != "" && !cardno.equals("null")) {

				List cardlist = commondesc.getCardnofromprodUsingcustid(cardno, instid, jdbctemplate);
				if (!cardlist.isEmpty()) {
					HashMap cardmap = (HashMap) cardlist.get(0);
					ecardno = (String) cardmap.get("CARD_NO");
					hcardno.append(cardmap.get("HCARD_NO"));
				}
				crdbase = true;
				if (usertype.equals("BRANCHUSER")) {
					cardnocond = "AND CIN LIKE '%" + cardno + "%'";
				} else {
					custnocond = " AND CIN LIKE '%" + cardno + "%'";
				}
			}
		}

		if (mobileno != "" && !mobileno.equals("null")) {
			crdbase = true;
			if (usertype.equals("BRANCHUSER")) {
				cardnocond = "AND MOBILENO LIKE '%" + mobileno + "')";
			} else {
				mobilenocond = "AND MOBILENO LIKE '%" + mobileno + "'";
			}

		}

		/*
		 * if( dob != "" && !dob.equals("null") ){ custbase = true; // cardno =
		 * commondesc.getCardnofromprodUsingcustid(custno, instid,
		 * jdbctemplate); //dobcond = " AND DOB LIKE '%"+dob+"%'"; dobcond =
		 * " AND dob = to_date('"+dob+"', 'dd-mm-yyyy')"; }
		 */

		if (custname != "" && !custname.equals("null")) {
			custbase = true;
			custnamecond = " AND   (  upper(FNAME) LIKE '%" + custname.toUpperCase() + "%'  OR upper(MNAME) LIKE '%"
					+ custname.toUpperCase() + "%' OR upper(LNAME) LIKE '%" + custname.toUpperCase() + "%'  ) ";
		}

		if (selectedtype.equals("cardnum")) {

			String keyid = "";
			String EDMK = "", EDPK = "";

			PadssSecurity padsssec = new PadssSecurity();
			Properties props = getCommonDescProperty();
			String EDPK1 = props.getProperty("EDPK");
			if (padssenable.equals("Y")) {

				keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::" + keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				// System.out.println("secList::" + secList);
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String CDMK = ((String) map.get("DMK"));
						// String eDPK = ((String) map.get("DPK"));

						hcardno = padsssec.getHashedValue(cardno + instid);
						String CDPK = padsssec.decryptDPK(CDMK, EDPK1);
						// trace("cardno"+cardno);
						ecardno = padsssec.getECHN(CDPK, cardno);
						// System.out.println(cardno);
						// trace("cardno1"+ecardno);
					}
				}
			}

			if (cardno != "" && !cardno.equals("null")) {
				crdbase = true;
				if (usertype.equals("BRANCHUSER")) {
					if (padssenable.equals("Y")) {
						cardnocond = "AND ( HCARD_NO = '" + hcardno.toString() + "')";
					} else {
						cardnocond = "AND ( HCARD_NO = '" + hcardno + "')";
					}

				} else {
					cardnocond = "AND ( HCARD_NO = '" + hcardno + "' )";
				}
			}
		}

		/*
		 * <div id="fw_container"> <table id="example" border="0"
		 * cellpadding="0" width="95%" cellspacing="0" class="pretty">
		 */

		// class='searchres'
		String result_hd = "<div id='fw_container'>	<table border='0' cellpadding='0' cellspacing='0' width='100%' class='pretty' style='border: 1px solid #454454' >";
		result_hd += "<tr><td colspan='11'><span style='color:gray;font-weight:bold'> Click on the card number to continue the maintenence activities </span> </td></tr>";
		if ("acctnum".equalsIgnoreCase(selectedtype)) {
			result_hd += "<tr> <th>Card No</th> <th>ACCOUNT NO</th><th>Mobile No.</th>  <th>CUST NAME</th> <th> CUSTOMER ID </th>  <th>CARD STATUS</th> <th>PRODUCT</th> <th>ACCOUNT</th>  </tr>";
		} else {
			result_hd += "<tr> <th>Card No</th> <th>ACCOUNT NO</th><th>Mobile No.</th>  <th>Order Ref No</th> <th>CUST NAME</th> <th> REG DATE </th> <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRE FILE</th> <th> EXP DATE </th> </tr>";
		}
		// <th>Sub-Product</th>
		String trclass = "";

		String finalresult = "";
		Boolean header_val = false;
		String card_dataqry = " SELECT * FROM " + TABLENAME + "  WHERE ROWNUM <= 50 AND INST_ID='" + instid + "'";
		enctrace("card_dataqry is:" + card_dataqry);
		if (crdbase) {

			// searchqry = card_dataqry + cardnocond + custnocond + orderrecond
			// + mobilenocond;
			searchqry = card_dataqry + cardnocond + custnocond + orderrecond + mobilenocond;

			enctrace("searchqry__kum" + searchqry);
			if (padssenable.equals("Y")) {
				trace("hcardno :::" + hcardno);
				// trace("ecardno :::" + ecardno);
				if ("acctnum".equalsIgnoreCase(selectedtype)) {
					result = this.viewgenerateSearchListForAcctNo(instid, cardno, hcardno.toString(), ecardno,
							searchqry, selectedtype, jdbctemplate, session);
					trace("trace1");
				} else {
					result = this.viewgenerateSearchList(instid, hcardno.toString(), ecardno, searchqry, selectedtype,
							jdbctemplate, session);
					trace("trace2");
				}
			} else {
				if ("acctnum".equalsIgnoreCase(selectedtype)) {
					result = this.viewgenerateSearchListForAcctNo(instid, cardno, cardno, "", searchqry, selectedtype,
							jdbctemplate, session);
					trace("trace3");
				} else {
					result = this.viewgenerateSearchList(instid, cardno, "", searchqry, selectedtype, jdbctemplate,
							session);
					trace("trace4");
				}
			}
			System.out.println("result__1" + result);
			if (result.equals("NOREC")) {
				// result_hd +=
				// "<thead><tr><th>Card No</th> <th>Mobile No</th> <th>Order Ref
				// No</th> <th>CUST NAME</th> <th> DOB </th> <th> CUSTOMER ID
				// </th> <th>BIN</th> <th>CARD STATUS</th> <th>PRODUCT</th>
				// </tr></thead>";
				result = "<div style='text-align:center;color:red'>NO RECORDS FOUND </div>";

			} else if (result.equals("SWITCHNOTMATCH")) {
				result = "<div style='text-align:center;color:red'>STATUS NOT MATCHED FROM SWITCH </div>";
			} else {
				result = result_hd + result;
				header_val = true;

			}

		}

		if (custbase) {
			String custdataqry = "SELECT * FROM EZCUSTOMERINFO WHERE INSTID='" + instid + "'";
			enctrace("custdataqry is:" + custdataqry);
			searchqry = custdataqry + custnocond + dobcond + custnamecond;
			enctrace("searchqry is:" + searchqry);
			List custdatalist = jdbctemplate.queryForList(searchqry);
			trace("custdatalist is:" + custdatalist.size());
			if (!custdatalist.isEmpty()) {
				Iterator custitr = custdatalist.iterator();
				boolean alt = true;
				while (custitr.hasNext()) {
					Map cmp = (Map) custitr.next();
					custno = (String) cmp.get("CUSTID");
					custnocond = " AND CIN LIKE '%" + custno + "%'";
					String searchqry_new = card_dataqry + custnocond;
					enctrace("searchqry_new__" + searchqry_new);
					finalresult = this.viewgenerateSearchList(instid, cardno, "ecardno", searchqry_new, selectedtype,
							jdbctemplate, session);
					trace("result is:" + result);
					System.out.println("result__2" + result);

					if (!result.equals("NOREC")) {
						/*
						 * if( !header_val){ result_hd +=
						 * "<tr> <th>Card No</th> <th>Order Ref No</th> <th>CUST NAME</th> <th> DOB </th> <th> CUSTOMER ID </th> <th>BIN</th>  <th>CARD STATUS</th> <th>PRODUCT</th>   </tr>"
						 * ; }
						 */
						result = result_hd + finalresult;
					} else if (result.equals("SWITCHNOTMATCH")) {
						result = "<div style='text-align:center:color:red'>STATUS NO MATCHED FROM SWITCH </div>";
					} else {
						result = "<div style='text-align:center:color:red'>NO RECORDS FOUND </div>";
					}

					trace("searchqry_new__" + searchqry_new);
				}

			}
		}

		result += "</table></div>";

		System.out.println("^^^^^^^^^^^^^^^^^^^");
		System.out.println("Final Result : " + result);
		System.out.println("^^^^^^^^^^^^^^^^^^^");
		getResponse().getWriter().write(result);
		trace("searchqry is:" + searchqry);

		// System.out.println(searchqry);

	}

	public String viewgenerateSearchListForAcctNo(String instid, String cardno, String h_cardno, String ecardno,
			String qry, String selectedtype, JdbcTemplate jdbctemplate, HttpSession session) throws Exception {

		int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);
		if (maint < 0) {
			return "required_home";
		}

		// System.out.println("coming cardno:::" + cardno);

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		System.out.println("padssenable:::" + padssenable);
		String keyid = "";
		String dcardno = "";
		PadssSecurity padsssec = new PadssSecurity();

		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");

		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDMK = ((String) map.get("DMK"));
					// String eDPK = ((String) map.get("DPK"));
					String CDPK = padsssec.decryptDPK(CDMK, EDPK);
					dcardno = padsssec.getCHN(CDPK, ecardno);
				}
			}
		}
		// System.out.println("dcardno::::serxh" + dcardno);
		String datatable = "";

		String card_status_desc = "";
		String cardnumber = "", accountrelation = "";

		boolean alt = true;
		int cnt = 0;

		List accountrel = commondesc.getAccountdesc(instid, cardno, jdbctemplate);
		if (!accountrel.isEmpty()) {
			Iterator acctitr = accountrel.iterator();
			while (acctitr.hasNext()) {
				Map acctmp = (Map) acctitr.next();
				qry = "SELECT * FROM CARD_PRODUCTION WHERE HCARD_NO='" + (String) acctmp.get("CHN") + "'";
				List card_data = jdbctemplate.queryForList(qry);
				if (!card_data.isEmpty()) {
					Iterator itr = card_data.iterator();
					while (itr.hasNext()) {

						Map mp = (Map) itr.next();

						cardnumber = (String) mp.get("CARD_NO");

						String mcardno = (String) mp.get("MCARD_NO");
						String hcardno = (String) mp.get("HCARD_NO");
						String accountno = (String) mp.get("ACCOUNT_NO");
						String order_refno = (String) mp.get("ORDER_REF_NO");
						String bin = (String) mp.get("BIN");
						// String activedate = (String)mp.get("ACTIVE_DATE");

						String bindesc = commondesc.getCardTypeDesc(instid, bin, jdbctemplate);
						String status_code = commondesc.getSTATUSFromEZLINK(instid, hcardno, jdbctemplate);
						System.out.println("status_code---> " + status_code);

						if (status_code == null || status_code.equals("")) {
							datatable = "SWITCHNOTMATCH";
							return datatable;
						}

						String product_code = commondesc.getProductdesc(instid, (String) mp.get("PRODUCT_CODE"),
								jdbctemplate);
						String custid = (String) mp.get("CIN");
						String cafrec = (String) mp.get("CAF_REC_STATUS");
						String mobileno = (String) mp.get("MOBILENO");
						String org_chn = (String) mp.get("ORG_CHN");
						String subproduct = (String) commondesc.getSubProductdesc(instid,
								(String) mp.get("SUB_PROD_ID"), jdbctemplate);
						if (mobileno == null) {
							mobileno = "--";
						}
						System.out.println("MOBILE NUMBER_____________" + mobileno);
						System.out.println("custid__" + custid);
						Object dateofbirth;
						String customername;
						String subproductid = (String) mp.get("SUB_PROD_ID");
						dateofbirth = (Object) commondesc.fchCustDOB(instid, custid, jdbctemplate);
						if (dateofbirth == null) {
							dateofbirth = "--";
						}
						customername = (String) commondesc.fchCustName(instid, custid, jdbctemplate);

						String embName = (String) commondesc.fchEmbName1(instid, h_cardno, jdbctemplate);

						Date expdate = (Date) mp.get("EXPIRY_DATE");

						String cardstatuscode = commondesc.getCardStatusCode(instid, status_code, jdbctemplate);
						String cardstatusdesc = commondesc.getCardStatusDesc(instid, cardstatuscode, jdbctemplate);
						card_status_desc = "<span style='color:red'>" + cardstatusdesc + "</span>";
						List accountrellist = commondesc.getAccountdesc(instid, hcardno, accountno, jdbctemplate);
						if (accountrellist.isEmpty()) {
							accountrel = commondesc.getAccountdescFromProd1(instid, hcardno, accountno, jdbctemplate);
							if (accountrellist.isEmpty()) {
								accountrelation = "--";
							} else {
								accountrelation = (String) acctmp.get("ACCOUNTDESC");
							}
						} else {
							accountrelation = (String) acctmp.get("ACCOUNTDESC");
						}
						if ("null".equalsIgnoreCase(accountrelation)) {
							accountrelation = "";
						}
						Date now = new Date();

						if (expdate.after(now)) {
							System.out.println("Not expired__" + expdate);
						} else {
							System.out.println("expired__" + expdate);
							card_status_desc = "<span style='color:red'> EXPIRED </span>";
						}

						String trclass = "";
						if (alt) {
							trclass = "class='alt'";
							alt = false;
						} else {
							alt = true;
						}
						cnt++;
						// "+trclass+"
						// onclick='showMaintain('"+instid+"','"+cardnumber+"')
						if (!"acctnum".equalsIgnoreCase(selectedtype)) {
							if (padssenable.equals("Y")) {
								datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
										+ "  <td  onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value) onclick=viewshowMaintain('"
										+ instid + "','" + hcardno + "','" + mcardno + "','" + cardnumber.toString()
										+ "')   ><td>" + mcardno + "</td>  <td>" + accountno + "</td><td>" + mobileno
										+ "</td> <td>" + order_refno + "</td>  <td>" + embName + "</td> <td>"
										+ dateofbirth + "</td> <td>" + custid + "</td>  <td>" + bindesc + "</td>  <td>"
										+ card_status_desc + "</td> <td>" + product_code + "</td></tr>";
							} else {
								datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
										+ " <td onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value)  onclick=viewshowMaintain('"
										+ instid + "','" + cardno + "','" + mcardno + "')   ><td>" + mcardno
										+ "</td>  <td>" + accountno + "</td><td>" + mobileno + "</td> <td>"
										+ order_refno + "</td>  <td>" + embName + "</td> <td>" + dateofbirth
										+ "</td> <td>" + custid + "</td>  <td>" + bindesc + "</td>  <td>"
										+ card_status_desc + "</td> <td>" + product_code + "</td>       </tr>";
							}
						} else {
							datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass + " <td><td>"
									+ mcardno + "</td>  <td>" + accountno + "</td><td>" + mobileno + "</td>  <td>"
									+ embName + "</td> <td>" + custid + "</td>  <td>" + card_status_desc + "</td> <td>"
									+ product_code + "</td>  <td>" + accountrelation + "</td>     </tr>";
						}

						// <td>"+ subproduct +"</td>
					}
				}

			}

		} else {
			datatable = "NOREC";
		}
		System.out.println("datatable:::::::datatable" + datatable);

		// added by gowtham_060819
		// nullify
		cardno = "0000000000000000";
		cardno = null;
		return datatable;
	}

	public String viewgenerateSearchList(String instid, String cardno, String ecardno, String qry, String selectedtype,
			JdbcTemplate jdbctemplate, HttpSession session) throws Exception {

		int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);
		if (maint < 0) {
			return "required_home";
		}
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");

		// System.out.println("coming cardno:::" + cardno);

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		System.out.println("padssenable:::" + padssenable);
		String keyid = "";
		String dcardno = "";
		String eDMK = "", eDPK = "";
		PadssSecurity padsssec = new PadssSecurity();
		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDMK = ((String) map.get("DMK"));
					// eDPK = ((String) map.get("DPK"));
					String CDPK = padsssec.decryptDPK(CDMK, EDPK);
					dcardno = padsssec.getCHN(CDPK, ecardno);
				}
			}
		}
		// System.out.println("dcardno::::serxh" + dcardno);
		String datatable = "";

		String card_status_desc = "";
		String cardnumber = "", accountrelation = "";
		List card_data = jdbctemplate.queryForList(qry);
		if (!card_data.isEmpty()) {
			Iterator itr = card_data.iterator();

			boolean alt = true;
			int cnt = 0;
			while (itr.hasNext()) {

				Map mp = (Map) itr.next();

				cardnumber = (String) mp.get("CARD_NO");

				String mcardno = (String) mp.get("MCARD_NO");
				String hcardno = (String) mp.get("HCARD_NO");
				String accountno = (String) mp.get("ACCOUNT_NO");
				String order_refno = (String) mp.get("ORDER_REF_NO");
				String bin = (String) mp.get("BIN");
				Timestamp activedate = (Timestamp) mp.get("GENERATED_DATE");

				String prefile = (String) mp.get("PRE_FILE");
				if (prefile != null) {
					if (prefile.contains("_")) {
						String[] namesplit = prefile.split("_");
						String filedate = namesplit[2];
						String filetime = namesplit[3];
						filedate = filedate.substring(0, 4) + filedate.substring(6, 8);
						prefile = filedate + "-" + filetime;
					} else {
						prefile = "MIGRATED";
					}
				} else {
					prefile = "--";
				}

				SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-YYYY");
				StringBuilder actdate = new StringBuilder(fmt.format(activedate));
				String bindesc = commondesc.getCardTypeDesc(instid, bin, jdbctemplate);
				String status_code = commondesc.getSTATUSFromEZLINK(instid, hcardno, jdbctemplate);
				// System.out.println("status_code---> "+status_code);

				if (status_code == null || status_code.equals("")) {
					datatable = "SWITCHNOTMATCH";
					return datatable;
				}
				Properties props1 = getCommonDescProperty();
				String EDPK1 = props1.getProperty("EDPK");
				String CDPK = padsssec.decryptDPK(eDMK, EDPK1);
				String clearchn = padsssec.getCHN(CDPK, cardnumber);

				String product_code = commondesc.getProductdesc(instid, (String) mp.get("PRODUCT_CODE"), jdbctemplate);
				String custid = (String) mp.get("CIN");
				String cafrec = (String) mp.get("CAF_REC_STATUS");
				String mobileno = (String) mp.get("MOBILENO");
				String org_chn = (String) mp.get("ORG_CHN");
				String subproduct = (String) commondesc.getSubProductdesc(instid, (String) mp.get("SUB_PROD_ID"),
						jdbctemplate);
				if (mobileno == null) {
					mobileno = "--";
				}
				System.out.println("MOBILE NUMBER_____________" + mobileno);
				System.out.println("custid__" + custid);
				Object dateofbirth;
				String customername;
				String subproductid = (String) mp.get("SUB_PROD_ID");

				dateofbirth = (Object) commondesc.fchCustDOB(instid, custid, jdbctemplate);
				if (dateofbirth == null) {
					dateofbirth = "--";
				}
				customername = (String) commondesc.fchCustName(instid, custid, jdbctemplate);

				String embName = (String) commondesc.fchEmbName1(instid, hcardno, jdbctemplate);

				Date expdate = (Date) mp.get("EXPIRY_DATE");
				Timestamp edate = (Timestamp) mp.get("EXPIRY_DATE");
				StringBuilder exdate = new StringBuilder(fmt.format(edate));

				String cardstatuscode = commondesc.getCardStatusCode(instid, status_code, jdbctemplate);
				trace("checking card status code from maintain table " + cardstatuscode);
				String cardstatusdesc = commondesc.getCardStatusDesc(instid, cardstatuscode, jdbctemplate);
				card_status_desc = "<span style='color:red'>" + cardstatusdesc + "</span>";

				List<Map<String, Object>> accountrel = commondesc.getAccountdesc(instid, hcardno, accountno,
						jdbctemplate);
				if (accountrel.isEmpty()) {
					accountrel = commondesc.getAccountdescFromProd1(instid, hcardno, accountno, jdbctemplate);
					if (accountrel.isEmpty()) {
						accountrelation = "--";
					} else {
						accountrelation = (String) accountrel.get(0).get("ACCOUNTDESC");
					}
				} else {
					accountrelation = (String) accountrel.get(0).get("ACCOUNTDESC");
				}
				if ("null".equalsIgnoreCase(accountrelation)) {
					accountrelation = "";
				}
				Date now = new Date();

				if (expdate.after(now)) {
					System.out.println("Not expired__" + expdate);
				} else {
					System.out.println("expired__" + expdate);
					card_status_desc = "<span style='color:red'> EXPIRED </span>";
				}
				String trclass = "";
				if (alt) {
					trclass = "class='alt'";
					alt = false;
				} else {
					alt = true;
				}
				cnt++;
				// "+trclass+"
				// onclick='showMaintain('"+instid+"','"+cardnumber+"')
				if (!"acctnum".equalsIgnoreCase(selectedtype)) {
					if (padssenable.equals("Y")) {
						datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
								+ "  <td  onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value) onclick=viewshowMaintain('"
								+ instid + "','" + hcardno + "','" + mcardno + "','" + cardnumber.toString()
								+ "')   ><td>" + clearchn + "</td>  <td>" + accountno + "</td><td>" + mobileno
								+ "</td> <td>" + order_refno + "</td>  <td>" + embName + "</td> <td>" + actdate
								+ "</td> <td>" + custid + "</td>  <td>" + bindesc + "</td>  <td>" + card_status_desc
								+ "</td> <td>" + prefile + "</td> <td>" + exdate + "</td></tr>";
					} else {
						datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass
								+ " <td onmouseover=showSelect(this.value)  onmouseout=showDeSelect(this.value)  onclick=viewshowMaintain('"
								+ instid + "','" + cardno + "','" + mcardno + "')   ><td>" + clearchn + "</td>  <td>"
								+ accountno + "</td><td>" + mobileno + "</td> <td>" + order_refno + "</td>  <td>"
								+ embName + "</td> <td>" + actdate + "</td> <td>" + custid + "</td>  <td>" + bindesc
								+ "</td>  <td>" + card_status_desc + "</td> <td>" + prefile + "</td>  <td>" + exdate
								+ "</td>     </tr>";
					}
				} else {
					datatable += "<tr class='rowrec' id='recordrow" + cnt + "' " + trclass + " <td><td>" + clearchn
							+ "</td>  <td>" + accountno + "</td><td>" + mobileno + "</td>  <td>" + embName
							+ "</td> <td>" + custid + "</td>  <td>" + card_status_desc + "</td> <td>" + product_code
							+ "</td>  <td>" + accountrelation + "</td>     </tr>";
				}

				// <td>"+ subproduct +"</td>

			}

		} else {
			datatable = "NOREC";
		}
		// System.out.println("datatable:::::::datatable" + datatable);

		// nullify
		cardno = "0000000000000000";
		cardno = null;

		return datatable;
	}

	public String viewshowMaintain() throws Exception {

		trace("********* Show maintainence ********* ");
		enctrace("********* Show maintainence ********* ");
		System.out.println("maitaining the screeen");
		System.out.println("instid__" + getRequest().getParameter("instid"));

		System.out.println("mcardno__" + getRequest().getParameter("mcardno"));
		System.out.println("hcardno__" + getRequest().getParameter("hcardno"));

		HttpSession session = getRequest().getSession();
		FeeConfigDAO feedao = new FeeConfigDAO();
		SearchTxn searchtxn = new SearchTxn();
		String instid = getRequest().getParameter("instid");

		String enccardno = getRequest().getParameter("cardnumber");

		cardcollectbranchlist = commondesc.generateBranchList(instid, jdbctemplate);

		System.out.println("enccardno11111" + enccardno);

		String cardnumber = null;
		String mcardno = getRequest().getParameter("mcardno");
		String hcardno = getRequest().getParameter("hcardno");

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		String keyid = "";
		String EDMK = "", EDPK = "";

		Properties props = getCommonDescProperty();
		String EdPK = props.getProperty("EDPK");

		PadssSecurity padsssec = new PadssSecurity();
		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDPK = ((String) map.get("DMK"));
					// String eDPK = ((String) map.get("DPK"));
					// cardnumber = padsssec.getCHN(eDMK, eDPK, enccardno);
					CDPK = padsssec.decryptDPK(CDPK, EdPK);
					cardnumber = padsssec.getCHN(CDPK, cardno);
				}
			}
		}
		// System.out.println("cardno:::" + cardnumber);
		setCardno(cardnumber);
		// System.out.println("enccardno:::" + enccardno);
		setEnccardno(enccardno);
		setMcardno(mcardno);
		setHcardno(hcardno);
		setPadssenable(padssenable);

		Connection conn = null;
		ResultSet rs = null;

		try {
			int reqch = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if (reqch < 0) {
				return "required_home";
			}

			int maint = commondesc.reqCheck().reqcheckMaintainMap(instid, session, jdbctemplate);
			if (maint < 0) {
				return "required_home";
			}

			List cardreclist = null;

			if (padssenable.equals("Y")) {
				cardreclist = cardmaindao.getProdcutionCardDetails(instid, hcardno, padssenable, TABLENAME,
						jdbctemplate);
			} else {
				cardreclist = cardmaindao.getProdcutionCardDetails(instid, cardnumber, padssenable, TABLENAME,
						jdbctemplate);
			}
			String switch_status_code = this.getSwitchCardStatusCode(instid, hcardno, jdbctemplate);
			if (!cardreclist.isEmpty()) {
				Iterator itr = cardreclist.iterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();

					order_refno = (String) mp.get("ORDER_REF_NO");
					setOrder_refno(order_refno);

					account_no = (String) mp.get("ACCOUNT_NO");
					setAcccountno(account_no);

					List getacctnolist = cardmaindao.getaccountlist(instid, order_refno, hcardno, jdbctemplate);

					if (getacctnolist.isEmpty()) {
						Map hmp = new HashMap();
						hmp.put("ACCOUNTNO", "NOREC");
						getacctnolist.add(hmp);
						setGetacctnolist(getacctnolist);
					}
					setGetacctnolist(getacctnolist);

					System.out.println("getting SECONDARY ACCOUNT LIST" + getacctnolist);

					acctsubtypeid = (String) mp.get("ACCTTYPE_ID");
					System.out.println("getting account subtypeid" + acctsubtypeid);
					List actypedescvalue = cardmaindao.getactypedesc(instid, acctsubtypeid, jdbctemplate);

					setActypedescvalue(actypedescvalue);

					String bin = (String) mp.get("BIN");
					bindesc = commondesc.getCardTypeDesc(instid, bin, jdbctemplate);
					setBindesc(bindesc);

					// String switch_status_code =
					// (String)mp.get("STATUS_CODE");
					String status_code = commondesc.getCardStatusCode(instid, switch_status_code, jdbctemplate);
					status_code_desc = commondesc.getCardStatusDesc(instid, status_code, jdbctemplate);

					String cafrec = (String) mp.get("CAF_REC_STATUS");

					product_code = commondesc.getProductdesc(instid, (String) mp.get("PRODUCT_CODE"), jdbctemplate);
					setProduct_code(product_code);

					custid = (String) mp.get("CIN");
					setCustid(custid);

					String subproductid = (String) mp.get("SUB_PROD_ID");

					customername = (String) commondesc.fchCustName(instid, custid, jdbctemplate);

					String maintainreq = commondesc.checkMaintainenceRequired(instid, subproductid, jdbctemplate);
					if (maintainreq.equals("N") || maintainreq.equals("n")) {
						setMaintainrequired(false);
					} else {
						setMaintainrequired(true);
						setMaintainallowed(true);
					}

					Date expdate1 = (Date) mp.get("EXPIRY_DATE");
					SimpleDateFormat frm = new SimpleDateFormat("dd-MM-yy");
					expdate = frm.format(expdate1);
					setExpdate(expdate);

					Date now = new Date();
					Boolean expired_flag = false;
					if (!expdate1.after(now)) {
						System.out.println("expired__" + expdate);
						expired_flag = true;
						status_code_desc = "Expired";
						setCrd_status_desc(status_code_desc);
					} else {
						// List applicable_list1=new ArrayList();
						List applicable_list = new ArrayList();
						String maintcode = (String) session.getAttribute("maintcode");
						trace("getting maintanence code" + maintcode);

						applicable_list = cardmaindao.getApplicableActionList(instid, status_code, jdbctemplate);
						/*
						 * System.out.println(applicable_list1+""+maintcode); if
						 * (Integer.parseInt(maintcode)!=0) {
						 * if(applicable_list1.contains(maintcode)){
						 * 
						 * HashMap hMap = new HashMap();
						 * hMap.put("APPLICABLE_ACTION", maintcode);
						 * //mcode.put("APPLICABLE_ACTION", maintcode);
						 * applicable_list.add(hMap); } //applicable_list =
						 * cardmaindao.getApplicableActionList(instid,
						 * maintcode, jdbctemplate);
						 * 
						 * } else{
						 * 
						 * applicable_list=applicable_list1; }
						 */
						List applicable_list1 = new ArrayList();
						boolean mactiveflag = true;
						if (Integer.parseInt(maintcode) != 0) {
							mactiveflag = false;
						}
						if (!applicable_list.isEmpty()) {
							ListIterator itrlist = applicable_list.listIterator();
							maintainallowed = true;
							while (itrlist.hasNext()) {
								Map tmp = (Map) itrlist.next();
								String act_code = (String) tmp.get("APPLICABLE_ACTION");

								String act_code_desc = commondesc.getCardStatusDesc(instid, act_code, jdbctemplate);
								tmp.put("CODE_DESC", act_code_desc);
								if (Integer.parseInt(act_code) == Integer.parseInt(maintcode)) {
									mactiveflag = true;
									applicable_list1 = new ArrayList();
									HashMap hMap = new HashMap();
									hMap.put("APPLICABLE_ACTION", maintcode);
									hMap.put("CODE_DESC", act_code_desc);
									// mcode.put("APPLICABLE_ACTION",
									// maintcode);
									applicable_list1.add(hMap);
									break;
								}

								applicable_list1.add(tmp);

							}
							System.out.println("itrlist__" + itrlist);
						}
						if (mactiveflag) {
							setApplicable_act_list(applicable_list1);
						} else {
							applicable_list1 = new ArrayList();
							setApplicable_act_list(applicable_list1);
						}
					}

					String todaydate = commondesc.getDate("dd-MM-yyyy");
					callbean.setTxndate(todaydate);

					// LIMIT DETAILS...
					String limitid = (String) mp.get("LIMIT_ID");
					callbean.setLimitid(limitid);
					String limitdesc = commondesc.getLimitDesc(instid, limitid, jdbctemplate);
					callbean.setLimitdesc(limitdesc);

					List limitdata = commondesc.getLimitDetails(instid, limitid, jdbctemplate);
					if (limitdata != null) {
						ListIterator limititr = limitdata.listIterator();
						while (limititr.hasNext()) {
							Map limitmp = (Map) limititr.next();
							String txncode = (String) limitmp.get("TXNCODE");
							String txndesc = commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
							limitmp.put("TXNDESC", txndesc);
							limitmp.put("LIMIT_AMOUNT", commondesc
									.formatCurrency((String) (Object) limitmp.get("LIMIT_AMOUNT").toString()));
							limitmp.put("PERTXNAMT",
									commondesc.formatCurrency((String) (Object) limitmp.get("PERTXNAMT").toString()));

							List reachedlimit = commondesc.getLimitReachedDetails(instid, cardno, txncode,
									jdbctemplate);
							trace("Getting daily txn limit...got :" + reachedlimit.size());
							limitmp.put("TODAYTXNAMT", "--");
							limitmp.put("TODAYTXNCNT", "--");
							if (reachedlimit != null) {
								Iterator reachitr = reachedlimit.iterator();
								while (reachitr.hasNext()) {
									Map reachmp = (Map) reachitr.next();
									String reachedamt = (String) (Object) reachmp.get("ACCUM_AMOUNT").toString();
									String reachedcnt = (String) (Object) reachmp.get("ACCUM_COUNT").toString();
									limitmp.put("TODAYTXNAMT", reachedamt);
									limitmp.put("TODAYTXNCNT", reachedcnt);
								}
							}

							limititr.remove();
							limititr.add(limitmp);
						}
						callbean.setLimitdetails(limitdata);
					}

					// PIN MANAGEMENT DETAILS
					String nofopinchange = "0";
					String noofwrongpin = "0";
					String pinblockdate = null;

					SwitchConnection swhconn = new SwitchConnection();
					conn = swhconn.getConnection();

					/*
					 * String recordList =
					 * "SELECT DISTINCT to_char(TRANDATE,'DD-MON-YYYY') AS PINCHANGEDATE,RESPCODE,"
					 * +"case RESPCODE when 0 then 'NO_OF_PINCHANGED'" +
					 * "when 55 then 'NO_OF_WRONGPIN'" +
					 * "when 75 then 'PINLOCKDATE'" +"else 'NO_MSG' end " +
					 * "from EZTXNLOG B WHERE " +
					 * "TXNCODE='940006' AND MSGTYPE='210' " +"AND TCHN='"
					 * +hcardno+"'";
					 * 
					 * String recordList =
					 * "SELECT DISTINCT to_char(TRANDATE,'DD-MON-YYYY') AS PINCHANGEDATE,(SELECT  COUNT(TRANDATE) FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=0) AS NO_OF_PINCHANGED,"
					 * +
					 * "(SELECT  COUNT(TRANDATE) FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=55) AS NO_OF_WRONGPIN,"
					 * +
					 * "(SELECT  TRANDATE FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=75) AS PINLOCKDATE "
					 * +
					 * "FROM EZTXNLOG B WHERE TXNCODE='940006' AND MSGTYPE='210' AND TCHN='"
					 * +hcardno+"'";
					 */

					String recordList = "SELECT to_char(TRANDATE,'DD-MON-YYYY') AS PINCHANGEDATE FROM EZTXNLOG "
							+ " WHERE TCHN='" + hcardno + "'  "
							+ "AND TXNCODE='940006' AND MSGTYPE='210' ORDER by TRANDATE desc";
					// PINCHANGEDATE
					trace("PINCHANGEDATE Qry value " + recordList);
					PreparedStatement ps = conn.prepareStatement(recordList);
					rs = ps.executeQuery(recordList);
					List as = new ArrayList<>();
					while (rs.next()) {
						as.add(rs.getString("PINCHANGEDATE"));
					}
					// NO_OF_PINCHANGED
					recordList = "SELECT COUNT(TRANDATE) as NO_OF_PINCHANGED FROM EZTXNLOG " + " WHERE TCHN='" + hcardno
							+ "'  " + "AND RESPCODE=0 AND TXNCODE='940006' AND MSGTYPE='210'";
					trace("NO_OF_PINCHANGED Qry value " + recordList);
					ps = conn.prepareStatement(recordList);
					rs = ps.executeQuery(recordList);
					while (rs.next()) {
						nofopinchange = rs.getString("NO_OF_PINCHANGED");
					}

					// NO_OF_WRONGPIN
					recordList = "SELECT COUNT(TRANDATE) as NO_OF_WRONGPIN FROM EZTXNLOG " + " WHERE TCHN='" + hcardno
							+ "'  " + "AND RESPCODE=55 AND MSGTYPE='210'";
					trace("NO_OF_WRONGPIN Qry value " + recordList);
					ps = conn.prepareStatement(recordList);
					rs = ps.executeQuery(recordList);
					while (rs.next()) {
						noofwrongpin = rs.getString("NO_OF_WRONGPIN");
					}

					// PINLOCKDATE
					recordList = "SELECT to_char(TRANDATE,'DD-MON-YYYY') AS PINLOCKDATE FROM EZTXNLOG "
							+ " WHERE TCHN='" + hcardno + "'  "
							+ "AND RESPCODE=75 AND MSGTYPE='210' ORDER by TRANDATE asc";
					trace("PINLOCKDATE Qry value " + recordList);
					ps = conn.prepareStatement(recordList);
					rs = ps.executeQuery(recordList);
					while (rs.next()) {
						pinblockdate = rs.getString("PINLOCKDATE");
					}

					List cardactivelist = cardmaindao.getcardactivelist(instid, hcardno, jdbctemplate);

					List newcardactivelist = new ArrayList();

					if (!cardactivelist.isEmpty()) {

						HashMap cmp = (HashMap) cardactivelist.get(0);
						if (cmp.get("REPINDATE") == null) {
							cmp.put("REPINDATE", "N/A");

						}
						if (cmp.get("ACTIVEDATE") == null) {
							cmp.put("ACTIVEDATE", "N/A");

						}
						newcardactivelist.add(cmp);
						// setCardactivelist(cardactivelist);

					}
					System.out.println("List value " + newcardactivelist);
					setCardactivelist(newcardactivelist);

					if (as.isEmpty()) {
						as.add("NOREC");
						setPinchange(as);
					} else {
						setPinchange(as);
					}
					if (nofopinchange == "0") {
						setNopinchange("0");
					} else {
						setNopinchange(nofopinchange);
					}
					// System.out.println("status_code--->"+switch_status_code);
					if (noofwrongpin == "0" || "50".equalsIgnoreCase(switch_status_code)) {
						setWrongpin("0");
					} else {
						setWrongpin(noofwrongpin);
					}

					if (pinblockdate == null) {
						setPinblock("NA");
					} else {
						setPinblock(pinblockdate);
					}

					// CARD ACTIVITY DETAILS
					String atmwithdr = "SELECT COUNT(*) AS ORIENT_ATM_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='" + hcardno
							+ "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='627443' AND ISSUERID='627443'";
					trace("Atm withdrawal Qry " + atmwithdr);
					ps = conn.prepareStatement(atmwithdr);
					rs = ps.executeQuery(atmwithdr);
					String atmcount = "";
					while (rs.next()) {
						atmcount = rs.getString("ORIENT_ATM_WITHDRAWAL");
					}
					setAtmwithdrawlcount(atmcount);

					String visaatmwithdr = "SELECT COUNT(*) AS VISA_ATM_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='" + hcardno
							+ "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='432283' AND ISSUERID='627443'";
					trace("Visa atm withdrawal Qry " + visaatmwithdr);
					ps = conn.prepareStatement(visaatmwithdr);
					rs = ps.executeQuery(visaatmwithdr);
					String visacount = "";
					while (rs.next()) {
						visacount = rs.getString("VISA_ATM_WITHDRAWAL");
					}
					setVisaatmwithdrawl(visacount);

					String interswtatmwithdr = "SELECT COUNT(*) AS INTERSWITCH_ATM_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='"
							+ hcardno
							+ "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='900001' AND ISSUERID='627443'";
					trace("Inter-switch atm withdrawal Qry " + interswtatmwithdr);
					ps = conn.prepareStatement(interswtatmwithdr);
					rs = ps.executeQuery(interswtatmwithdr);
					String interswtcount = "";
					while (rs.next()) {
						interswtcount = rs.getString("INTERSWITCH_ATM_WITHDRAWAL");
					}
					setInterswitchatmwithdr(interswtcount);

					String totalwithdr = "SELECT COUNT(*) AS TOTAL_WITHDRAWAL FROM EZTXNLOG WHERE TCHN='" + hcardno
							+ "' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011'";
					trace("Total withdrawal Qry " + totalwithdr);
					ps = conn.prepareStatement(totalwithdr);
					rs = ps.executeQuery(totalwithdr);
					String totalatmwith = "";
					while (rs.next()) {
						totalatmwith = rs.getString("TOTAL_WITHDRAWAL");
					}
					setTotalatmwithdrawal(totalatmwith);

					String totalpostxn = "SELECT COUNT(*) AS NO_OF_POSTXNS FROM EZTXNLOG WHERE TCHN='" + hcardno
							+ "' AND DEVICETYPE='POS' AND RESPCODE=0";
					trace("Total Pos Txn Qry " + totalpostxn);
					ps = conn.prepareStatement(totalpostxn);
					rs = ps.executeQuery(totalpostxn);
					String totalpos = "";
					while (rs.next()) {
						totalpos = rs.getString("NO_OF_POSTXNS");
					}
					setNoofpostxns(totalpos);

					String totalonlinetxn = "SELECT COUNT(*) AS NO_OF_ONLINETXNS FROM EZTXNLOG WHERE TCHN='" + hcardno
							+ "' AND POSENTRYCODE='100' and MSGTYPE='210' and RESPCODE=0";
					trace("Total online txn " + totalonlinetxn);
					ps = conn.prepareStatement(totalonlinetxn);
					rs = ps.executeQuery(totalonlinetxn);
					String totalonline = "";
					while (rs.next()) {
						totalonline = rs.getString("NO_OF_ONLINETXNS");
					}
					setNoofonlinetxns(totalonline);
				}

			} // end while

			// System.out.println("cardno is__" + getCardno());
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the process....");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		} finally {
			rs.close();
			conn.close();
			trace("Connection closed");
		}
		trace("\n\n");
		enctrace("\n\n");

		// nullify
		cardno = "0000000000000000";
		cardno = null;

		return "viewmaintain_home";

	}

	public String getcustomeridtoclose(String Ecardno, String instid, JdbcTemplate jdbctemplate) {
		String cinvalue = null;
		try {
			String getcinqry = "SELECT CIN FROM CARD_PRODUCTION WHERE ORG_CHN='" + Ecardno + "' and INST_ID='" + instid
					+ "'";
			trace("getcinqry" + getcinqry);
			cinvalue = (String) jdbctemplate.queryForObject(getcinqry, String.class);
		} catch (EmptyResultDataAccessException e) {
			cinvalue = "NOREC";
			e.printStackTrace();
		}
		return cinvalue;
	}
}
