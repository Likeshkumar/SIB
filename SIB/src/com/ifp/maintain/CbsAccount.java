package com.ifp.maintain;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifd.fee.FeeConfigDAO;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.maintain.CbsAccountBeans;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.beans.AuditBeans;
import com.ifp.dao.CbsAccountDao;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;
import com.ifp.util.MailSender;
import com.ifp.util.XSSRequestingWrapper;

import connection.CBSConnection;
import sun.org.mozilla.javascript.internal.ast.WhileLoop;
import test.Validation;

public class CbsAccount extends BaseAction {

	/**
	 * kumar
	 */

	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();

	AuditBeans auditbean = new AuditBeans();
	public File walletupload;
	String walletuploadFileName;
	CbsAccountDao cbsdao = new CbsAccountDao();
	CbsAccountBeans cbsbean = new CbsAccountBeans();

	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	private List acctdetails;

	public List getAcctdetails() {
		return acctdetails;
	}

	public void setAcctdetails(List acctdetails) {
		this.acctdetails = acctdetails;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CbsAccountDao getCbsdao() {
		return cbsdao;
	}

	public void setCbsdao(CbsAccountDao cbsdao) {
		this.cbsdao = cbsdao;
	}

	public CbsAccountBeans getCbsbean() {
		return cbsbean;
	}

	public void setCbsbean(CbsAccountBeans cbsbean) {
		this.cbsbean = cbsbean;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId() {
		HttpSession session = getRequest().getSession();
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	public String comBranchId() {
		HttpSession session = getRequest().getSession();
		String br_id = (String) session.getAttribute("BRANCHCODE");
		return br_id;
	}

	public String comuserType() {
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");
		return usertype;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	private List authcardorder;

	public List getAuthcardorder() {
		return authcardorder;
	}

	public void setAuthcardorder(List authcardorder) {
		this.authcardorder = authcardorder;
	}

	private List authproductlist;

	public List getAuthproductlist() {
		return authproductlist;
	}

	public void setAuthproductlist(List authproductlist) {
		this.authproductlist = authproductlist;
	}

	String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	private List Authorder;

	public List getAuthorder() {
		return Authorder;
	}

	public void setAuthorder(List authorder) {
		Authorder = authorder;
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

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode(HttpSession session) {
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public String comUsername() {
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("USERNAME");
		return username;
	}

	public void getAccountTypeList() throws Exception {

		trace("getAccutSubTypeList: method called");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		trace("getAccountTypeList list...");
		StringBuilder accttyperesult = new StringBuilder();
		accttyperesult.append("<option value='-1'> -Select AccountType- </option>");
		try {
			List accounttypelist = cbsdao.getAcctTypeList(instid, jdbctemplate);
			Iterator itr = accounttypelist.iterator();

			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				String accounttypeid = (String) map.get("ACCTTYPEID");
				String accountypedesc = (String) map.get("ACCTTYPEDESC");
				System.out.println("accountypedesc:::" + accountypedesc);
				accttyperesult = accttyperesult
						.append("<option value='" + accounttypeid + "'>" + accountypedesc + "</option>");
				// result = result + max;
			}

		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception ..." + e.getMessage());
		}
		getResponse().getWriter().write(accttyperesult.toString());
	}

	public void getCurrencyList() throws Exception {

		trace("getCurrencyList: method called");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		trace("getCurrencyList list...");
		StringBuilder accttyperesult = new StringBuilder();
		accttyperesult.append("<option value='-1'> -Select Currency- </option>");
		try {
			List currencylist = commondesc.getCurList(instid, jdbctemplate);
			Iterator itr = currencylist.iterator();

			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				String numericcode = (String) map.get("NUMERIC_CODE");
				String currdesc = (String) map.get("CURRENCY_DESC");
				accttyperesult = accttyperesult.append("<option value='" + numericcode + "'>" + currdesc + "</option>");
				// result = result + max;
			}

		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception ..." + e.getMessage());
		}
		getResponse().getWriter().write(accttyperesult.toString());
	}

	public String addCbsAcctHome() {
		trace("addCbsAcctHome method called....");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String act = getRequest().getParameter("act");
		//String s=XSSRequestingWrapper.stripXSS(act);
		//System.out.println(" getitn act value------> " + s);
		
		//boolean b=XSSRequestingWrapper.checkWhiteSpaces(s);
		
		//System.out.println(" getitn act value------> " + b);
		
	/*	if(b==true){
			System.out.println("kkkkkkkk============");
			return "addcbsacct_home ";
			//addActionError("wrong input-----plz check");
		}*/
		
		/*if(s==""||s.equals("") ||s.contains("\\s")){
			return "addcbsacct_home";
		}*/
		
		if (act != null) {
			session.setAttribute("act", act);
		}

		try {

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to process...");
		}
		return "addcbsacct_home";
	}

	public void checkAccountNoExist() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String accountnumber = getRequest().getParameter("accountnumber");
		int res = this.validateActvatedAccount(instid, accountnumber, jdbctemplate);
		System.out.println("getting count res " + res);
		int res1 = this.validateActvatedAccoutinacctlink(instid, accountnumber, jdbctemplate);
		System.out.println("getting count res1 " + res1);

		// String deauthavailable = this.deauthorizedaccount(instid,
		// accountnumber, jdbctemplate);
		// System.out.println("getting count res1 " + res1);

		if (res > 0) {
			getResponse().getWriter().write("EXIST");
		} else if (res1 > 0) {

			getResponse().getWriter().write("ACCTLINK");
			System.out.println("ACCTLINK");
		}

		else {
			getResponse().getWriter().write("NEW");
		}

	}

	public int validateActvatedAccount(String instid, String accountno, JdbcTemplate jdbcTemplate) {
		int validacct = -1;
		StringBuilder valdacctqry = new StringBuilder();

		/*
		 * valdacctqry.append(
		 * "select COUNT(1) from ACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO FROM EZACCOUNTINFO WHERE ACCOUNTNO='"
		 * +accountno+"' AND INSTID='"+instid+"') ");
		 * enctrace("valdacctqry::"+valdacctqry.toString()); validacct =
		 * jdbcTemplate.queryForInt(valdacctqry.toString());
		 */

		// by gowtham-300819
		valdacctqry.append("select COUNT(1) from ACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO FROM EZACCOUNTINFO"
				+ " WHERE ACCOUNTNO=? AND INSTID=? ) ");
		enctrace("valdacctqry::" + valdacctqry.toString());
		validacct = jdbcTemplate.queryForInt(valdacctqry.toString(), new Object[] { accountno, instid });

		return validacct;
	}

	public int validateActvatedAccoutinacctlink(String instid, String accountno, JdbcTemplate jdbcTemplate) {
		int validacct = -1;
		StringBuilder valdacctqry = new StringBuilder();

		/*
		 * valdacctqry.append("select COUNT(1) from ACCT_LINK WHERE ACCT_NO='"
		 * +accountno+"' AND INST_ID='"+instid+"'");
		 * enctrace("valdacctqry::"+valdacctqry.toString()); validacct =
		 * jdbcTemplate.queryForInt(valdacctqry.toString());
		 */

		/// by gowtham-300819
		valdacctqry.append("select COUNT(1) from ACCT_LINK WHERE ACCT_NO=? AND INST_ID=? ");
		enctrace("valdacctqry::" + valdacctqry.toString());
		validacct = jdbcTemplate.queryForInt(valdacctqry.toString(), new Object[] { accountno, instid });

		return validacct;

	}

	public String deauthorizedaccount(String instid, String accountno, JdbcTemplate jdbctemplate) {
		String CHECKER_ID = null;
		try {

			/*
			 * String BRANCHCODE =
			 * "SELECT CHECKER_ID FROM ACCT_LINK  WHERE ACCT_NO='"+accountno+
			 * "' AND MKCK_STATUS='D' AND INST_ID='"+instid+"'";
			 * enctrace("deauthorizedaccount:" + BRANCHCODE ); CHECKER_ID =
			 * (String)jdbctemplate.queryForObject(BRANCHCODE, String.class);
			 */

			/// by gowtham-300819
			String BRANCHCODE = "SELECT CHECKER_ID FROM ACCT_LINK  WHERE ACCT_NO=? AND "
					+ "MKCK_STATUS=? AND INST_ID=?";
			enctrace("deauthorizedaccount:" + BRANCHCODE);
			CHECKER_ID = (String) jdbctemplate.queryForObject(BRANCHCODE, new Object[] { accountno, "D", instid },
					String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return CHECKER_ID;

	}

	public String generateAddonaccountAction() {
		trace("======  generateAddonaccountAction called.============....");
		HttpSession session = getRequest().getSession(false);

		String sessioncsrftoken = (String) session.getAttribute("token");
		System.out.println("Global session token---> " + sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("generateAddonaccountAction() method   token---->    " + jspcsrftoken);
		String custname="",prod_code="",sub_prod_id="",br_code="";
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}

		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String cardno = getRequest().getParameter("cardno");

		trace("comming card number checking -->"+cardno);

		String accounttype[] = getRequest().getParameterValues("accounttypevalue");
		String accountsubtype[] = getRequest().getParameterValues("accountsubtypevalue");
		String accountcurrency[] = getRequest().getParameterValues("accountccyvalue");
		String accountnumber[] = getRequest().getParameterValues("accountnovalue");
		String primarysecondary[] = getRequest().getParameterValues("primarysec");
		IfpTransObj transact = commondesc.myTranObject("saveacctno", txManager);
		
		
	
		try {
			String orderrefno = "", status = "", branchcode = "";
			String customerid = "";
			String limitid = "";
			String addedflag = "A";
			String username = comUsername();
			String remarks = getRequest().getParameter("reason");
			String order_refnum[] = getRequest().getParameterValues("personalrefnum");
			Personalizeorderdetails persorderdetails, bindetails, extradetails;
			String mkckstatus = (String) session.getAttribute("act");

			String tablename = "PERS_CARD_PROCESS";

			int checkcardexist = commondesc.checkCardExistInSystem(instid, cardno, tablename, jdbctemplate);

			// added by gowtham_220719
			String ip = (String) session.getAttribute("REMOTE_IP");

			if (checkcardexist > 0) {
				addActionError("Could not Add Account,Card is waiting under process");
				return this.addCbsAcctHome();

			}

			List accounttypedetails = null;
			String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

			String keyid = "";
			String CDMK = "", CDPK = "",encCardno="";
			Properties props=null;
			String EDPK="";
			// StringBuffer hcardno = new StringBuffer();
			PadssSecurity padsssec = new PadssSecurity();
			if (padssenable.equals("Y")) {
				props = getCommonDescProperty();
				EDPK = props.getProperty("EDPK");
				keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::" + keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				// System.out.println("secList::"+secList);
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						CDMK = ((String) map.get("DMK"));
						//eDPK = ((String) map.get("DPK"));
						CDPK = padsssec.decryptDPK(CDMK, EDPK);
						// hcardno = padsssec.getHashedValue(cardno+instid);
					}
				}
			}

			
			accounttypedetails = commondesc.getAccountDetails(instid, padssenable, cardno, "CARD_PRODUCTION",
					jdbctemplate);

			Iterator itr = accounttypedetails.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				orderrefno = (String) mp.get("ORDER_REF_NO");
				customerid = (String) mp.get("CIN");
				limitid = (String) mp.get("LIMIT_ID");
				status = (String) mp.get("STATUS_CODE");
				branchcode = (String) mp.get("BRANCH_CODE");
				encCardno=(String)mp.get("ORG_CHN");
			}

			String limitBasedon = commondesc.getLimitBasedOn(instid, limitid, jdbctemplate);
			String accflag = commondesc.getAccflagBylimitid(instid, limitid, jdbctemplate);
/*
			System.out.println("accounttypelength" + accounttype.length);
			System.out.println("accountsubtypelength" + accountsubtype.length);
			System.out.println("accountcurrencylength" + accountcurrency.length);
			System.out.println("accountnumberlength" + accountnumber.length);
			System.out.println("primarysecondarylength" + primarysecondary.length);
*/
			int insacinfo = 0;
			int insezacinfo = 0;
			int ezauthrel = 0;
			int  acct_priority=0;

			for (int i = 0; i < accounttype.length; i++) {
				  
				System.out.println("-------------------start-------------------");
				trace("accounttype----" + accounttype[i]);
			   trace("accountsubtype----" + accountsubtype[i]);
			   trace("accountcurrency----" + accountcurrency[i]);
			   trace("accountnumber----" + accountnumber[i]);
			   trace("primarysecondary----" + primarysecondary[i]);
			    acct_priority=++acct_priority;
			    String acct_priority_str=String.valueOf(acct_priority);
			   trace("acct_priority_str--->"+acct_priority_str);
			   
				System.out.println("-------------------end-------------------");

				int checkvalidacct = -1;

			/*	
				 * String accttypevalid =
				 * "SELECT COUNT(*) as exist from ACCTTYPE WHERE ACCTTYPEID='"
				 * +accounttype[i]+"'"; System.out.println(
				 * "getting accoun ttype values " + accttypevalid);
				 * checkvalidacct =
				 * jdbctemplate.queryForInt(accttypevalid.toString());
				 */

				/// by gowtham-300819
				String accttypevalid = "SELECT COUNT(*) as exist from ACCTTYPE WHERE ACCTTYPEID=? ";
			     trace("getting accoun ttype values " + accttypevalid);
				checkvalidacct = jdbctemplate.queryForInt(accttypevalid.toString(), new Object[] { accounttype[i] });

				if (checkvalidacct <= 0) {
					addActionError("Could not get Account type value ");
					return "addcbsacct_home";

				}

				String accttype = "(SELECT ACCTTYPEID from ACCTSUBTYPE where INST_ID='" + instid
						+ "' AND ACCTSUBTYPEID='" + accountsubtype[i] + "')";

				String acctsubtype = accountsubtype[i];
				// insacinfo = cbsdao.insertAccountInfo(instid, orderrefno,
				// customerid,accountnumber[i],accttype,
				// acctsubtype,accountcurrency[i],limitBasedon,
				// usercode,jdbctemplate) ;
				insacinfo = cbsdao.insertifdcardAcctlink(instid, orderrefno, cardno, customerid, accounttype[i],
						acctsubtype, accountcurrency[i], accountnumber[i], limitBasedon, usercode, mkckstatus,
						addedflag,acct_priority_str, jdbctemplate);

				try {

					String mcardno = commondesc.getMaskedCardbyproc(instid, encCardno, "CARD_PRODUCTION", "H",
							jdbctemplate);
					if (mcardno == null) {
						mcardno = cardno;
					}
					
					/*
					 * Values are getting from database to insert into auditran table.
					 * Modified date: 01-july-2021
					*/
					String CardDetails="SELECT PRODUCT_CODE,SUB_PROD_ID,BRANCH_CODE,EMB_NAME FROM CARD_PRODUCTION WHERE  ORG_CHN='"+encCardno+"'";
					enctrace("CardDetails-->"+CardDetails);
					List CardDetailsFromProd = jdbctemplate.queryForList(CardDetails);
					 Iterator itrdata=CardDetailsFromProd.iterator();
					 while  (itrdata.hasNext()){
						 Map mp = (Map) itrdata.next();
						   prod_code=(String) mp.get("PRODUCT_CODE");
						   sub_prod_id=(String) mp.get("SUB_PROD_ID");
						   br_code=(String) mp.get("BRANCH_CODE");
						   custname=(String) mp.get("EMB_NAME");
						 
					 };
					 
				
					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					trace("mcard number checking --->" + mcardno);
					auditbean.setActmsg(
							"Adding Account number  is " + accountnumber[i] + " to the card [ " + mcardno + " ] ");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("9091");
					auditbean.setCardno(mcardno);
					auditbean.setCin(customerid);
					auditbean.setApplicationid(orderrefno);
					auditbean.setAccoutnno(accountnumber[i]);
					auditbean.setCustname(custname);
					auditbean.setBin(prod_code.substring(0,8)); 
					auditbean.setSubproduct(sub_prod_id);
					auditbean.setProduct(prod_code);
					auditbean.setCardcollectbranch(br_code);
					auditbean.setActiontype("IM");
					
					// auditbean.setCardnumber(order_refnum[i]);
					// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
				 
					trace("Exception in auditran : " + audite.getMessage());
				}

				// --------------End on 11-12-15-------------//
				// insezacinfo=cbsdao.insertEzAccountifo(instid,
				// accountnumber[i], accttype, accountcurrency[i], accflag,
				// status, "01", branchcode, acctsubtype,usercode,jdbctemplate);
				// ezauthrel = cbsdao.insertEzauthrel(instid, cardno,
				// accountnumber[i], accttype, "S", "2",
				// accountcurrency[i],jdbctemplate);

				/*
				 * int update =
				 * cbsTableUpdateDetails(cardno,accountnumber[i],padssenable,
				 * instid,usercode,padsssec,eDMK,eDPK,"SA",jdbctemplate);
				 * 
				 * if(update == 1){ enctrace(
				 * " ----SECONDARY ACCOUNT - CBS UPDATE SUCCESS----"); }else{
				 * enctrace(" ----SECONDARY ACCOUNT - CBS UPDATE FAIL----"); }
				 */
			}
			if (insacinfo > 0) {
				txManager.commit(transact.status);
				addActionMessage("Account Added Successfully...Waiting for authorization");
				System.out.println(" account success message");
				// ---

			}

			else {
				txManager.rollback(transact.status);
				addActionError("Unable to Continue Add-on Account");
			}

		} catch (Exception e) {
			trace("Exception in add-on Account:" + e);
			txManager.rollback(transact.status);
			addActionError("Unable to Continue Add-on Account");
		}

		return "addcbsacct_home";

	}

	public String addAccountNumber() throws Exception {
		trace("=======saveAccountNumber method called=========....");
		HttpSession session = getRequest().getSession(false);

		String sessioncsrftoken = (String) session.getAttribute("token");
		System.out.println("Global session token---> " + sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("addAccountNumber() method   token---->    " + jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}

		boolean check;
		int checkvalue;
		StringBuilder sb = null;
		// System.out.println("getting value for add and remove " +
		// (String)getRequest().getParameter("findcard"));
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String cardno = getRequest().getParameter("cardnum");
		// System.out.println(cardno);

		checkvalue = Validation.number(cardno);
		if (checkvalue == 0) {
			addActionError("Enter Proper Card Number");
			return "required_home";
		}

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		String keyid = "";
		String EDMK = "", EDPK = "", getcin = "", maskedchn = "",encCardno="";
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		Properties props=new Properties();
	//	String EDPK=null;
		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			props = getCommonDescProperty();
			EDPK = props.getProperty("EDPK");
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::"+secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDMK = ((String) map.get("DMK"));
					String eDPK = ((String) map.get("DPK"));
					String CDPK = padsssec.decryptDPK(CDMK, EDPK);
					hcardno = padsssec.getHashedValue(cardno + instid);
					encCardno=padsssec.getECHN(CDPK, cardno);
				}
			}
			maskedchn = padsssec.getMakedCardno(cardno);
		}

		int checkvalid = -1;

		// trace("hcardno::"+hcardno);
		 trace("cardno::::"+encCardno);

		 /*
		  * Due to Bank requirement, branch validation removed on 29-06-2021
		 */
		 
		/*String validbranch = commondesc.getvalidbranchaddonaccount(instid, hcardno.toString(), jdbctemplate);

		if (validbranch == null) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " " + maskedchn + " Card Collect Branch Notvailable..");
			trace("[ " + maskedchn + " ]Card Collect Branch not Available ... ...");
			return "required_home";
		}
		trace("Got validbranch: " + validbranch);
		if (!comBranchId().equals("000")) {
			if (!validbranch.equalsIgnoreCase(comBranchId())) {
				// subproddesc = commondesc.getSubProductdesc(instid, subprodid,
				// jdbctemplate);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " " + maskedchn
						+ "  Card is not valid for this Branch User....Use Your " + comBranchId() + " Branch card..");
				trace("[ " + maskedchn + " ]Card is not valid for this Branch User. ...");
				return "required_home";
			}
		}
		*/

		try {
			/*
			 * if(padssenable.equals("Y")) {
			 */ checkvalid = cbsdao.validateActvatedCard(instid, padssenable, hcardno.toString(), jdbctemplate);
			/*
			 * }else{ checkvalid =
			 * cbsdao.validateActvatedCard(instid,padssenable, cardno,
			 * jdbctemplate); }
			 */

			if (checkvalid <= 0) {
				addActionError("Entered Card no Not Activated ...Enter Valid Card No.");
				return "addcbsacct_home";
			} else {
				List accounttypedetails = null;
				/*
				 * if(padssenable.equals("Y")) {
				 */
				accounttypedetails = cbsdao.getAccounttypeinfo(instid, padssenable, hcardno.toString(), jdbctemplate);
				/*
				 * }else{ accounttypedetails =
				 * cbsdao.getAccounttypeinfo(instid,padssenable,cardno,
				 * jdbctemplate); }
				 */

				if (accounttypedetails.isEmpty()) {
					addActionError("Card Details Not Found ,CLose this card And Map Again");
					return "addcbsacct_home";
				}
				cbsbean.setAccounttypedetails(accounttypedetails);
				Iterator itr = accounttypedetails.iterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					System.out.println("-----------------test--" + (String) mp.get("ACCOUNTTYPE"));
					cbsbean.setAccounttypevalue((String) mp.get("ACCOUNTTYPEDESC"));
					cbsbean.setAccountsubtypevalue((String) mp.get("PRODUCTCODEDESC"));
					cbsbean.setCurrencyvalue((String) mp.get("CURRCODEDESC"));
					cbsbean.setAccountnovalue((String) mp.get("ACCOUNTNO"));

					// getcin = this.getCustomerIdByCardNumberProcess(instid,
					// hcardno.toString(), jdbctemplate);

					String custname = "";
					/*
					 * if(padssenable.equals("Y")) {
					 */
					custname = this.getCustomerNameByCard(instid, hcardno.toString(), padssenable, jdbctemplate);
					/*
					 * }else{ custname =
					 * this.getCustomerNameByCard(instid,cardno,padssenable,
					 * jdbctemplate); }
					 */
					System.out.println("getting customername " + custname);
					cbsbean.setCustname(custname);
					cbsbean.setAccflag((String) mp.get("ACCTFLAG"));

					cbsbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));

					trace("setting accttypelist");
					List accttypelist = cbsdao.getAcctTypeListNew(instid,encCardno, jdbctemplate);
					cbsbean.setAccttypelist(accttypelist);

					trace("setting currencylist");
					List currencylist = commondesc.getCurList(instid, jdbctemplate);
					cbsbean.setCurrencylist(currencylist);

					if (padssenable.equals("Y")) {
						cbsbean.setCardno(hcardno.toString());
					} else {
						cbsbean.setCardno(cardno);
					}
				}
			}

			// System.out.println(cardno);
			cardno = "0000000000000000";
			sb = new StringBuilder(cardno);
			sb.setLength(0);

		} catch (Exception e) {
			// txManager.rollback(transact.status);
			trace("Exception : " + e.getMessage());
			addActionError("Unable to process...");

		} finally {
			cardno = null;
			sb = null;
		}

		// System.out.println(cardno+sb);
		return "addcbsacct_home";
	}

	public String editCbsAcctHome() {
		HttpSession session = getRequest().getSession();
		String act = (String) session.getAttribute("act");

		if (act != null) {
			session.setAttribute("act", act);
		}

		return "editcbsacct_home";
	}

	public String editAcctNumber() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String cardno = getRequest().getParameter("cardno");
		String act = (String) session.getAttribute("act");
		try {

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to process...");
		}
		return this.addCbsAcctHome();
	}

	public String viewCbsAcctHome() {

		HttpSession session = getRequest().getSession();
		String cbsaction = getRequest().getParameter("action");
		if (cbsaction.equals("VIEW")) {
			return "viewcbsacct_home";
		} else if (cbsaction.equals("AUTH")) {
			return "authcbsacct_home";
		} else {
			session.setAttribute("preverr", "E");
			session.setAttribute("preverr", "Unknown action type...could not identified the request");
			trace("action value should be VIEW OR AUTH....");
			return "required_home";
		}

	}

	public String authAcctDetails() throws Exception {
		/*
		 * HttpSession session = getRequest().getSession(); String instid =
		 * comInstId(session); String username=comUserCode(session); String
		 * cardno = getRequest().getParameter("cardno"); String cbsacctno =
		 * getRequest().getParameter("cbsacctno"); String authstatusflag =
		 * getRequest().getParameter("actionvalue"); String result = "";
		 * IfpTransObj transact = commondesc.myTranObject("AUTHACCT",
		 * txManager); String msg = "Updated";
		 * 
		 * try{
		 * 
		 * if( authstatusflag != null ){
		 * 
		 * } //String authstatus =
		 * cbsdao.getAuthStatus(instid,cardno,cbsacctno,jdbctemplate); //trace(
		 * "Got the authstatus...:"+authstatus);
		 * 
		 * 
		 * 
		 * 
		 * }catch(Exception e){ transact.txManager.rollback(transact.status);
		 * result = "Could not authorize..."+e.getMessage(); }
		 * getResponse().getWriter().write(result.toString()); }
		 */

		// Added by senthil

		List pers_prodlist = null, br_list = null;
		// String inst_id =comInstId(session);
		String usertype = comuserType();
		String branch = comBranchId();

		String instid = comInstId();
		String temp = act;
		System.out.println(temp);
		// session.setAttribute("CARDGEN_ACT", act);
		// String session_act = (String) session.getAttribute("CARDGEN_ACT");

		// System.out.println("session_act " + session_act);
		try {
			System.out.println("Inst Id===>" + instid + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = getBranchCodefmProcess(instid, jdbctemplate);
				// commondesc.generateBranchList(inst_id, jdbctemplate);

				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
				}

			}
			pers_prodlist = getProductListBySelected(instid, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setAuthproductlist(pers_prodlist);
			} else {
				System.out.println("No Product Details Found ");
				addActionError("No Product Details Found");
				trace(" No Product Details Found ");
			}

		} catch (Exception e) {
			addActionError(" Error While Fetching The Product Details  " + e.getMessage());
			trace("Exception : while fetching product details " + e.getMessage());
		}
		return "autheditacctlinkdetails";
	}

	// ended by senthil

	public void getCbsAcctDetails() throws Exception {

		getResponse().getWriter().write("");
	}

	public int cbsTableUpdateDetails(String cardno, String accountno, String padssenable, String instid,
			String maker_id, PadssSecurity padsssec, String eDMK, String eDPK, String cafflag,
			JdbcTemplate jdbctemplate) throws Exception {

		trace("Enter cbs update.... ");
		String cardcon = "", process = "";
		if ("SA".equalsIgnoreCase(cafflag)) {
			process = "Secondary Account";
		}
		if (padssenable.equals("Y")) {
			cardcon = "HCARD_NO";
		} else {
			cardcon = "CARD_NO";
		}
		;

		// CBS Update Details
		// Customized for Orient Bank by ramesh
		String cardNo = "", accno = "", documentno = "";
		String status = "", cbscardno = "", cbsstatus = "", cbstableupdate = "", refno = "", branchcode = "", cin = "",
				subprod = "", cbsusername = "", bin = "";
		String query = "SELECT p.CARD_NO,p.ACCOUNT_NO,p.BRANCH_CODE,p.CIN,p.BIN,p.SUB_PROD_ID,trim(nvl(c.DOCUMENT_NUMBER,0)) as DOCUMENT_NUMBER from CUSTOMERINFO c,CARD_PRODUCTION p where c.CIN = p.CIN and p."
				+ cardcon + "='" + cardno + "'";
		enctrace("cbs-selectquery : " + query);
		// System.out.println("cbs-selectquery--->" + query);
		List<Map<String, Object>> list = null;
		try {
			list = jdbctemplate.queryForList(query);
			if (!list.isEmpty()) {
				cardNo = (String) list.get(0).get("CARD_NO");
				accno = (String) list.get(0).get("ACCOUNT_NO");
				branchcode = (String) list.get(0).get("BRANCH_CODE");
				cin = (String) list.get(0).get("CIN");
				bin = (String) list.get(0).get("BIN");
				subprod = (String) list.get(0).get("SUB_PROD_ID");
				documentno = (String) list.get(0).get("DOCUMENT_NUMBER");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");

		subprod = subprod.substring(4, 8);
		String CDPK = padsssec.decryptDPK(eDMK, EDPK);

		/* String clearchn = padsssec.getCHN(eDMK, eDPK, cardNo); */
		String clearchn = padsssec.getCHN(CDPK, cardNo);
		int update = 0, res_1 = 0, cbscount = 0;
		IfpTransObj transact = commondesc.myTranObject("ISSUEAUTH", txManager);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rrs = null;

		CBSConnection cbscon = new CBSConnection();
		con = cbscon.getCBSDBConnection();
		if (!list.isEmpty() && con != null) {
			try {
				String cbscountqry = "select COUNT(1) as CNT from STTM_DEBIT_CARD_MASTER where CUST_AC_NO='" + accountno
						+ "' and CARD_NO='" + clearchn + "' ";
				enctrace("cbscountqry : " + cbscountqry);
				pstmt = con.prepareStatement(cbscountqry);
				rrs = pstmt.executeQuery(cbscountqry);
				while (rrs.next()) {
					cbscount = rrs.getInt("CNT");
				}

				if (cbscount == 0) {
					branchcode = accountno.substring(12, 14);
					refno = commondesc.fchCbsRefNoSeq(instid, jdbctemplate);
					refno = cafflag + refno;
					cbsusername = commondesc.getCbsUserName(instid, maker_id, jdbctemplate);
					cbstableupdate = "Insert into STTM_DEBIT_CARD_MASTER (BRANCH_CODE, REQUEST_REFERENCE_NO, CARD_NO, CUST_NO, CUST_AC_NO, CARD_PRODUCT, PRIMARY_CARD, CARD_STATUS, RECORD_STAT, AUTH_STAT, ONCE_AUTH, MOD_NO, MAKER_ID, MAKER_DT_STAMP, CHECKER_ID, CHECKER_DT_STAMP, CARD_BIN,DR_CR_INDICATOR) Values "
							+ " (LPAD('" + branchcode + "', 3,'0'), '" + refno + "', '" + clearchn + "', '" + cin
							+ "','" + accountno + "', '" + subprod + "', 'N', 'I', 'O', 'A', 'Y', 1, '" + cbsusername
							+ "', SYSDATE,'" + cbsusername + "', SYSDATE,'" + bin + "','D')";
					enctrace("cbstableupdate : " + cbstableupdate);
					pstmt = con.prepareStatement(cbstableupdate);
					update = pstmt.executeUpdate();
				} else {
					status = "Same account Number and card no already Exists in cbs..!!";
				}

				if (update == 1) {
					status = "Success";
					con.commit();
				} else {
					status = "Fail to update the CBS table, check account no and request reference no is correct?";
					con.rollback();
				}

			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
				trace("Exception in moving production cbscon :: 1:::::::" + e);
				// return -1;
			} finally {
				if (rrs != null) {
					rrs.close();
				}
				pstmt.close();
				con.close();
			}
		}
		String cbstabledetails = "INSERT INTO CBS_UPDATE_DETAILS(PROCESS, PROCESSED_DATE, PROCESSED_QUERY, STATUS, PROCESSED_USER,CBS_CARD_NO,CBS_CARD_STATUS) values"
				+ "('" + process + "',(SYSDATE),'" + cbstableupdate.replaceAll("'", "") + "','" + status
				+ "',(select username from USER_DETAILS where userid='" + maker_id + "'),'" + cbscardno + "','"
				+ cbsstatus + "')";
		enctrace("cbstabledetails : " + cbstabledetails);
		// System.out.println("cbstabledetails--->" + cbstabledetails);

		try {

			res_1 = jdbctemplate.update(cbstabledetails.toString());
		} catch (Exception e) {
			txManager.rollback(transact.status);
			e.printStackTrace();
			trace("Exception in moving production cbstabledetails :: 1:::::::" + e);
			// return -1;
		}
		/*
		 * if (res_1 == 1) { txManager.commit(transact.status); } else {
		 * txManager.rollback(transact.status); }
		 */
		// End Of CBS update
		trace("Exit cbs update.... ");
		return update;

	}

	public String getCustomerNameByCard(String instid, String cardno, String padssenable, JdbcTemplate jdbctemplate)
			throws Exception {
		String customername = null;
		String customerid = null;
		String cardcon = "";

		// BY SIVA
		/*
		 * if(padssenable.equals("Y")) { cardcon="HCARD_NO"; } else {
		 * cardcon="CARD_NO"; }; BY SIVA
		 */
		try {

			// String customernameqry = "SELECT CIN FROM CARD_PRODUCTION WHERE
			// INST_ID='"+instid+"' and ORDER_REF_NO=(SELECT ORDER_REF_NO WHERE
			// CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"' ";

			/*
			 * String customernameqry =
			 * "SELECT CIN FROM CARD_PRODUCTION WHERE INST_ID='"+instid+
			 * "' and ORDER_REF_NO=(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
			 * +cardno+"' ) "; enctrace("3030customernameqry :" +
			 * customernameqry); customerid =
			 * (String)jdbctemplate.queryForObject(customernameqry,
			 * String.class);
			 */

			// by gowtham-300819
			String customernameqry = "SELECT CIN FROM CARD_PRODUCTION WHERE INST_ID=? and ORDER_REF_NO=(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? ) ";
			enctrace("3030customernameqry :" + customernameqry);
			customerid = (String) jdbctemplate.queryForObject(customernameqry, new Object[] { instid, cardno },
					String.class);

			customername = commondesc.fchCustName(instid, customerid, jdbctemplate);

		} catch (Exception e) {
		}
		return customername;
	}

	public void getAccutSubTypeList() throws Exception {

		trace("getAccutSubTypeList: method called");
		FeeConfigDAO feedao = new FeeConfigDAO();
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String accounttypeid = getRequest().getParameter("accounttypeid");

		String result = "<option value='-1'> -SELECT- </option>";
		System.out.println("getAccutSubTypeList list...");
		StringBuilder subaccttyperesult = new StringBuilder();
		try {
			List subaccounttypelist = this.getAcctSubTypeList(instid, accounttypeid, jdbctemplate);
			Iterator itr = subaccounttypelist.iterator();

			subaccttyperesult.append("<option value='-1'> - Select - </option>");
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				String subAccounttypeid = (String) map.get("ACCTSUBTYPEID");
				String subAccountypedesc = (String) map.get("ACCTSUBTYPEDESC");
				subaccttyperesult = subaccttyperesult
						.append("<option value='" + subAccounttypeid + "'>" + subAccountypedesc + "</option>");
				// result = result + max;
			}

		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception ..." + e.getMessage());
		}

		getResponse().getWriter().write(subaccttyperesult.toString());
	}

	public List getAcctSubTypeList(String instid, String accountypeid, JdbcTemplate jdbctemplate) throws Exception {
		List acctypelist = null;

		/*
		 * String accsubtypelistqry =
		 * "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE AUTH_CODE = '1' AND INST_ID='"
		 * +instid+"' AND ACCTTYPEID='"+accountypeid+"'";
		 * enctrace("accsubtypelistqry:::"+accsubtypelistqry); acctypelist =
		 * jdbctemplate.queryForList(accsubtypelistqry);
		 */

		/// by gowtham-300819
		String accsubtypelistqry = "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE "
				+ "WHERE AUTH_CODE = ? AND INST_ID=? AND ACCTTYPEID=? ";
		enctrace("accsubtypelistqry:::" + accsubtypelistqry);
		acctypelist = jdbctemplate.queryForList(accsubtypelistqry, new Object[] { "1", instid, accountypeid });

		return acctypelist;
	}

	public List getBranchCodefmProcess(String instid, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();

		/*
		 * query.append(
		 * "select DISTINCT a.BRANCH_CODE,a.BRANCH_NAME FROM BRANCH_MASTER a,ACCT_LINK b "
		 * ); query.append(
		 * "  WHERE a.INST_ID=b.INST_ID and trim(BRANCH_CODE) in (select trim(BRANCH_CODE) FROM CARD_PRODUCTION WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO =b.CARD_NO)) and "
		 * ); query.append(" a.INST_ID='"+instid+
		 * "' AND b.FLAG IN('A','D') AND b.MKCK_STATUS IN('M') "); query.append(
		 * " ORDER BY a.BRANCH_CODE"); enctrace( " getBranchCodefmProcess : " +
		 * query.toString() ); List branchList =
		 * jdbctemplate.queryForList(query.toString());
		 */

		/// by gowtham-300819
		query.append("select DISTINCT a.BRANCH_CODE,a.BRANCH_NAME FROM BRANCH_MASTER a,ACCT_LINK b ");
		query.append(
				"  WHERE a.INST_ID=b.INST_ID and trim(BRANCH_CODE) in (select trim(BRANCH_CODE) FROM CARD_PRODUCTION WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO =b.CARD_NO)) and ");
		query.append(" a.INST_ID=? AND b.FLAG IN('A','D') AND b.MKCK_STATUS IN('M') ");
		query.append(" ORDER BY a.BRANCH_CODE");
		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString(), new Object[] { instid });

		return branchList;
	}

	public List getProductListBySelected(String instid, JdbcTemplate jdbctemplate) throws Exception {
		/*
		 * String query=
		 * "select DISTINCT b.PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER where PRODUCT_MASTER.INST_ID=b.INST_ID and PRODUCT_MASTER.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from CARD_PRODUCTION b,ACCT_LINK  c WHERE b.INST_ID=c.INST_ID and b.ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=c.CARD_NO) and c.INST_ID='"
		 * +instid+"' and FLAG IN('A','D')"; enctrace( " get prod list : " +
		 * query ); return(jdbctemplate.queryForList(query));
		 */

		String query = "select DISTINCT b.PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER where PRODUCT_MASTER.INST_ID=b.INST_ID and PRODUCT_MASTER.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from CARD_PRODUCTION b,ACCT_LINK  c WHERE b.INST_ID=c.INST_ID and b.ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=c.CARD_NO) and c.INST_ID=? and FLAG IN('A','D')";
		enctrace(" get prod list : " + query);
		return (jdbctemplate.queryForList(query, new Object[] { instid }));
	}

	public String authlistaccountdetails() {
		
		trace("================ secondary addon account authorization called ============================");
		HttpSession session = getRequest().getSession();


		String userid = comUserId();
		try {
			//String branch = getRequest().getParameter("branchcode");
			String branch="ALL";
			String cardtype = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String instid = comInstId();
			String acctcategory = getRequest().getParameter("acctcategory");
			
			trace("[Branch Code] "+branch+" [Cardtype] "+cardtype+" [Fromdate] "+fromdate+" [Todate] "+todate+" [instid]  "+instid);
			System.out.println("[Branch Code] "+branch+" [Cardtype] "+cardtype+" [Fromdate] "+fromdate+" [Todate] "+todate+" [instid]  "+instid);
			trace("[Acctcategory] " + acctcategory);
			System.out.println("[Acctcategory]" + acctcategory);
			
			// String mkckstatus = "P";
			List authcardorder;
			String dateflag = "A.MAKER_DATE";
			trace("Getting order list...");
			String condition = this.filterCondition(cardtype, branch, fromdate, todate, dateflag);
			trace("CONDITION ======== " + condition);
			authcardorder = personaliseCardeeditauthlist(instid, acctcategory, condition, userid, jdbctemplate);
			trace("authcardorder=====>" + authcardorder);
			if (authcardorder.isEmpty()) {
				addActionError("No Orders Found");
				trace("No order found...");
				return this.authAcctDetails();
			} else {
				setAuthcardorder(authcardorder);
				session.setAttribute("curerr", "S");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not continue the process....");
			trace("Exception : " + e.getMessage());
		}

		return "authacctlistorders";
	}

	public String authsaveacctdetails() throws Exception {

		trace("coming into acct link authdeauth details");
		HttpSession session = getRequest().getSession(false);
		String sessioncsrftoken = (String) session.getAttribute("token");
		System.out.println("Global session token---> " + sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("authsaveacctdetails() method   token---->    " + jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}

		String  prod_code="",sub_prod_id="";
		
		String userid = comUserId();
		String instid = comInstId();
		String username=comUsername();
		String authstatus = "", statusmsg = "", err_msg = "";
		// IfpTransObj tranact = commondesc.myTranObject("authdeauth",
		// txManager);
		// IfpTransObj tranact1 = commondesc.myTranObject("audit", txManager);
		IfpTransObj transact = commondesc.myTranObject("AUTHACCT", txManager);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			statusmsg = " Authorized Successfully...";
			err_msg = "Authorize";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			authstatus = "D";
			statusmsg = " Account is De-Authorized and Removed  Successfully ..";
			err_msg = "De-Authorize";
		}

		String remarks = getRequest().getParameter("reason");
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		System.out.println("Total Orders Selected ===> " + order_refnum.length);

		String cardcategory = getRequest().getParameter("cardcategory");
		System.out.println("cardcategory " + cardcategory);
		String reason = getRequest().getParameter("reason");
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		int insacinfo = -1, insezacinfo = -1, ezauthrel = -1, delifdacct = -1, delezauth = -1, insertifdacctback = -1,
				insertezauthback = -1, insertezacctback = -1;
		int cnt = 0;
		int udpateacct = 0;
		String updatemsg = "";
		int x=0;
		for (int i = 0; i < order_refnum.length; i++) {

			try {

				String limitid = "", status = "", branchcode = "", orgchn = "", accountnumber = "", orderrefno = "",
						embname = "", customerid = "", accttype = "", acctsubtype = "", accountcurrency = "",
						limitBasedon = "", cardno = "",encCard="";

				int check = 0, delcust = 0, checkdeauth = 0, checkdeauthcardproc = 0, checkdeauthcustproc = 0,
						updatecardprod = 0, delcardacctlink = 0;
				System.out.println("Selected Refnums ==>" + order_refnum[i]);
				List acctdetails = this.getcardAcctdetails(order_refnum[i], padssenable, cardcategory, jdbctemplate);
				
				trace("carddetails coming  "+acctdetails);
				
				if (!acctdetails.isEmpty()) {
					Iterator carddet = acctdetails.iterator();
					//trace("carddetails coming");

					while (carddet.hasNext()) {
						Map map = (Map) carddet.next();

						orderrefno = (String) map.get("ORDER_REF_NO");
						customerid = (String) map.get("CIN");
						accountnumber = (String) map.get("ACCT_NO");
						accttype = (String) map.get("ACCTTYPE_ID");
						acctsubtype = (String) map.get("ACCTSUB_TYPE_ID");
						accountcurrency = (String) map.get("ACCT_CCY");
						limitBasedon = (String) map.get("LIMITBASED_ON");
						cardno = (String) map.get("CARD_NO");
					}
					List accounttypedetails = commondesc.getAccountDetails(instid, padssenable, cardno,
							"CARD_PRODUCTION", jdbctemplate);

					Iterator itr = accounttypedetails.iterator();
					while (itr.hasNext()) {
						Map mp = (Map) itr.next();
						limitid = (String) mp.get("LIMIT_ID");
						status = (String) mp.get("STATUS_CODE");
						branchcode = (String) mp.get("BRANCH_CODE");
						orgchn = (String) mp.get("ORG_CHN");
						embname = (String) mp.get("EMB_NAME");
                       //encCard=(String)mp.get("ORG_CHN");
					}
					String req = getRequest().getParameter("authorize");

					trace("req" + req);
					String accflag = commondesc.getAccflagBylimitid(instid, limitid, jdbctemplate);

					if (getRequest().getParameter("authorize") != null) {
						if (cardcategory.equals("A")) {
							insacinfo = cbsdao.insertAccountInfo(instid, orderrefno, customerid, accountnumber,
									accttype, acctsubtype, accountcurrency, limitBasedon, userid, jdbctemplate);
							insezacinfo = cbsdao.insertEzAccountifo(instid, accountnumber, accttype, accountcurrency,
									accflag, status, "01", branchcode, acctsubtype, userid, jdbctemplate);
							ezauthrel = cbsdao.insertEzauthrel(instid, cardno, accountnumber, accttype, "S", "2",
									accountcurrency, jdbctemplate);

							if (insacinfo > 0 || insezacinfo > 0 || ezauthrel > 0) {
								delcardacctlink = cbsdao.delauthifdcardlink(instid, order_refnum[i], jdbctemplate);
								if (delcardacctlink > 0) {
									udpateacct = udpateacct + 1;
									updatemsg = "Added Account " + statusmsg;

									try {
										
										String mcardno = commondesc.getMaskedCardNoFromProd(instid, orgchn, "H",
												jdbctemplate);
										if (mcardno == null) {
											mcardno = cardno;
										}
										
										/*
										 * To insert auditran table productcode taken from cardproduction table
										 * Date: 01-july-2021
										*/
										String prod_cde="SELECT PRODUCT_CODE,SUB_PROD_ID FROM CARD_PRODUCTION WHERE ORG_CHN='"+orgchn+"'";
										enctrace("product_query-->"+prod_cde);
										List CardDetailsFromProd = jdbctemplate.queryForList(prod_cde);
										 Iterator itrdata=CardDetailsFromProd.iterator();
										 while  (itrdata.hasNext()){
											 Map mp = (Map) itrdata.next();
											   prod_code=(String) mp.get("PRODUCT_CODE");
											   sub_prod_id=(String) mp.get("SUB_PROD_ID");
									 };
										
										// added by gowtham_220719
										trace("ip address======>  " + ip);
										auditbean.setIpAdress(ip);

										auditbean.setActmsg("Added [" + accountnumber
												+ "] Add-On-Account for the  Card Number  [" + mcardno + " ] ");
										auditbean.setUsercode(userid);
										auditbean.setAuditactcode("9091");
										auditbean.setCardno(mcardno);
										auditbean.setRemarks(remarks);
										auditbean.setAccoutnno(accountnumber);
										auditbean.setCustname(embname);
										auditbean.setCin(customerid);
										auditbean.setCardcollectbranch(branchcode);
										auditbean.setProduct(prod_code); 
										auditbean.setBin(prod_code.substring(0,8));
										auditbean.setSubproduct(sub_prod_id);
										auditbean.setApplicationid(orderrefno);
										auditbean.setChecker(userid);
										auditbean.setActiontype("IM");
										
										// auditbean.setProduct(persorderdetails.product_code);
										commondesc.insertAuditTrail(instid,username , auditbean, jdbctemplate, txManager);
										// commondesc.insertAuditTrailPendingCommit(instid,
										// userid, auditbean, jdbctemplate,
										// txManager);

									} catch (Exception audite) {
										trace("Exception in auditran : " + audite.getMessage());
										addActionError("Unable to Continue the Process.....");
									}

								}
							}
						}

						else if (cardcategory.equals("D")) {
							
							
						String delete_secondary_acct_priority="SELECT ACCOUNTPRIORITY FROM EZAUTHREL WHERE   instid='"+instid+"' and  CHN='"+cardno+"'  and accountno='"+accountnumber+"'  and  accountflag !='P' " ;
									enctrace("Delete_Secondary_Acct_Count_Query-->"+delete_secondary_acct_priority);
									String delete_secondary_acct_priority_value=(String)jdbctemplate.queryForObject(delete_secondary_acct_priority,String.class);
									System.out.println("delete_secondary_acct_priority_value--->"+delete_secondary_acct_priority_value);
									int delete_secondary_acct_priority_val=Integer.parseInt(delete_secondary_acct_priority_value);
									

									String secondary_acct_count="SELECT MAX(ACCOUNTPRIORITY)  FROM EZAUTHREL WHERE instid='"+instid+"' and  CHN='"+cardno+"' and accountflag !='P' ";
									enctrace("Secondary_Acct_Count_Query-->"+secondary_acct_count);
									String  sec_acct_count =(String)jdbctemplate.queryForObject(secondary_acct_count,String.class);
									System.out.println("Secondary_account_count--->"+sec_acct_count);
									int sec_acct_cnt=Integer.parseInt(sec_acct_count);
									
							
							insertifdacctback = cbsdao.insertifdacctdel(instid, accountnumber, userid, jdbctemplate);
							insertezauthback = cbsdao.insertezauthdel(instid, cardno, accountnumber, userid,jdbctemplate);
							insertezacctback = cbsdao.insertEzAcctdel(instid, accountnumber, userid, jdbctemplate);
							delcardacctlink = cbsdao.delauthifdcardlink(instid, order_refnum[i], jdbctemplate);
							
						 
							
						/*	
						 * Added for after delete the secondary account number, required to change the account priority value
						 * DATE: 02-JULY-2021 
						*/
							
							trace("------------------ ACCOUNT PRIORITY CHANGING STARTED---------------");
							
						 
				 			int del_sec_acct_pri_cnt=-1;
				 			
							System.out.println("Initial value of del_sec_acct_pri_cnt-->"+del_sec_acct_pri_cnt);
							//sec_acct_count=Integer.parseInt(sec_acct_count);
							
							if(sec_acct_cnt>delete_secondary_acct_priority_val){
								System.out.println("inside if condition----->");
							for(del_sec_acct_pri_cnt=delete_secondary_acct_priority_val;del_sec_acct_pri_cnt<sec_acct_cnt;del_sec_acct_pri_cnt++)
							{
								System.out.println("inside for loop--->");
								System.out.println("del_sec_acct_pri_cnt-->"+del_sec_acct_pri_cnt);
								String existing_cnt="";
								String update_cnt="";
								update_cnt=String.valueOf(del_sec_acct_pri_cnt);
								existing_cnt=String.valueOf(del_sec_acct_pri_cnt);
								String update_sec_pri="UPDATE EZAUTHREL SET ACCOUNTPRIORITY='"+update_cnt+"' where  CHN='"+cardno+"' and accountpriority= '"+existing_cnt+"' +1";
								enctrace("update_sec_pri_query-->"+update_sec_pri);
								 x=jdbctemplate.update(update_sec_pri);
								System.out.println("update count-->"+x);
								}}
							
							if ((insertifdacctback > 0 || insertezauthback > 0 || insertezacctback > 0)
									&& delcardacctlink > 0 && x>=0) {
								udpateacct = udpateacct + 1;
								updatemsg = "Removing Account " + statusmsg;

								try {
								 
									String mcardno = commondesc.getMaskedCardNoFromProd(instid, orgchn, "H",
											jdbctemplate);
									if (mcardno == null) {
										mcardno = cardno;
									}
									
									String prod_cde_1="SELECT PRODUCT_CODE,SUB_PROD_ID FROM CARD_PRODUCTION WHERE ORG_CHN='"+orgchn+"'";
									enctrace("product_query_1-->"+prod_cde_1);
									List CardDetailsFromProd = jdbctemplate.queryForList(prod_cde_1);
									 Iterator itrdata=CardDetailsFromProd.iterator();
									 while  (itrdata.hasNext()){
										 Map mp = (Map) itrdata.next();
										   prod_code=(String) mp.get("PRODUCT_CODE");
										   sub_prod_id=(String) mp.get("SUB_PROD_ID");
								 };

									// added by gowtham_220719
									trace("ip address======>  " + ip);
									auditbean.setIpAdress(ip);

									auditbean.setActmsg("Remove  Add-On-Account [" + accountnumber
											+ " ]for the  Card Number [" + mcardno + "]  ");
									auditbean.setUsercode(userid);
									auditbean.setAuditactcode("9092");
									auditbean.setCardno(mcardno);
									auditbean.setRemarks(remarks);
									auditbean.setAccoutnno(accountnumber);
									auditbean.setProduct(prod_code);
								    auditbean.setBin(prod_code.substring(0,8));
									auditbean.setSubproduct(sub_prod_id);
									auditbean.setApplicationid(orderrefno);
									auditbean.setCustname(embname);
									auditbean.setCin(customerid);
									auditbean.setCardcollectbranch(branchcode);
									auditbean.setChecker(userid);
									auditbean.setActiontype("IM");
									
							
									// auditbean.setProduct(persorderdetails.product_code);
									commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
									// commondesc.insertAuditTrailPendingCommit(instid,
									// userid, auditbean, jdbctemplate,
									// txManager);

								} catch (Exception audite) {
									trace("Exception in auditran : " + audite.getMessage());
								}

							} else {
								addActionError("Could not continue process1");
							}
						}

						else {

							addActionError("Could not continue process2");
							trace("account information not found ");

						}

					} else {

						int insertdeauthlinktable = cbsdao.insertcardacctlink(instid, cardno, order_refnum[i],
								jdbctemplate);
						int deauthlinkdeleteaccount = cbsdao.deauthdelete(instid, cardno, order_refnum[i],
								jdbctemplate);

						System.out.println("deauthlinkdeleteaccount====>" + deauthlinkdeleteaccount);
						System.out.println("insertdeauthlinktable=====>" + insertdeauthlinktable);

						if (deauthlinkdeleteaccount > 0 && insertdeauthlinktable > 0) {
							udpateacct = udpateacct + 1;
							updatemsg = "Account " + statusmsg;

							try {
								String mcardno = commondesc.getMaskedCardNoFromProd(instid, orgchn, "H", jdbctemplate);
								if (mcardno == null) {
									mcardno = cardno;
								}

								String prod_cde_2="SELECT PRODUCT_CODE,SUB_PROD_ID FROM CARD_PRODUCTION WHERE ORG_CHN='"+orgchn+"'";
								enctrace("product_query_2-->"+prod_cde_2);
								List CardDetailsFromProd = jdbctemplate.queryForList(prod_cde_2);
								 Iterator itrdata=CardDetailsFromProd.iterator();
								 while  (itrdata.hasNext()){
									 Map mp = (Map) itrdata.next();
									   prod_code=(String) mp.get("PRODUCT_CODE");
									   sub_prod_id=(String) mp.get("SUB_PROD_ID");
							 };
								
								// added by gowtham_220719
								trace("ip address======>  " + ip);
								auditbean.setIpAdress(ip);

								auditbean.setActmsg("Deauthorized and  removed  Add-On-Account [ "+accountnumber+"] for the  Card Number ["+mcardno+"]");
								auditbean.setUsercode(userid);
								auditbean.setAuditactcode("9093");
								auditbean.setCardno(mcardno);
								auditbean.setRemarks(remarks);
								auditbean.setProduct(prod_code);
							    auditbean.setBin(prod_code.substring(0,8));
								auditbean.setSubproduct(sub_prod_id);
								auditbean.setApplicationid(orderrefno);
								auditbean.setCustname(embname);
								auditbean.setCin(customerid);
								auditbean.setCardcollectbranch(branchcode);
								auditbean.setAccoutnno(accountnumber);
								auditbean.setChecker(userid);
								auditbean.setActiontype("IM");
								
								// auditbean.setProduct(persorderdetails.product_code);
								commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
								// commondesc.insertAuditTrailPendingCommit(instid,
								// userid, auditbean, jdbctemplate, txManager);

							} catch (Exception audite) {
								trace("Exception in auditran : " + audite.getMessage());
							}

						} else {
							addActionError("Could not continue process3");

						}
					}

				}

			} catch (Exception e) {
				addActionError("Could not continue process4");
				trace("could not get details " + e.getMessage());
			}

		}
		if (udpateacct > 0) {
			addActionMessage(updatemsg);
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", " " + statusmsg);
		} else {
			addActionError("could not continue process");
			txManager.rollback(transact.status);
		}
		return this.authAcctDetails();
	}

	public List personaliseCardeeditauthlist(String instid, String acctcategory, String condition, String userid,
			JdbcTemplate jdbctemplate) throws Exception {

		String select_query = "select distinct(A.order_ref_no) as ORDER_REF_NO,B.MCARD_NO,A.CARD_NO,B.PRODUCT_CODE,B.EMB_NAME,B.SUB_PROD_ID, A.CIN, A.CARD_NO,A.ACCT_NO,DECODE(B.ACC_CCY,'971','Primary','840','Secondary') as CURRENCY,to_char(A.MAKER_DATE,'DD-MON-YY HH:MM:SS') as MAKER_DATE,A.MKCK_STATUS,B.MOBILENO,B.BRANCH_CODE,A.MAKER_ID,A.FLAG,A.ACCT_PRIORITY "
				+ " from ACCT_LINK A,CARD_PRODUCTION_HASH C, CARD_PRODUCTION B "
				+ " where A.INST_ID=B.INST_ID AND A.ORDER_REF_NO=B.ORDER_REF_NO AND A.ORDER_REF_NO=C.ORDER_REF_NO AND A.CARD_NO=C.HCARD_NO AND A.inst_id='"
				+ instid + "' and A.FLAG='" + acctcategory + "' and A.MAKER_ID!='" + userid
				+ "' AND  A.mkck_status IN ('P','M') " + condition + " order by B.branch_code, A.ACCT_PRIORITY";
		// AND CAF_REC_STATUS in ('S','A') This was added for maintenance
		// activity
		 enctrace("3030authcards_query:::: : "+select_query);
		// enctrace("checking select query for authlist" +select_query);
		List persorderlist = jdbctemplate.queryForList(select_query);

		/// by gowtham-300819
		/*
		 * String select_query=
		 * "select distinct(A.order_ref_no) as ORDER_REF_NO,B.MCARD_NO,A.CARD_NO,B.PRODUCT_CODE,"
		 * +
		 * "B.EMB_NAME,B.SUB_PROD_ID, A.CIN, A.CARD_NO,A.ACCT_NO,DECODE(B.ACC_CCY,'971','Primary','840','Secondary') as "
		 * +
		 * "CURRENCY,to_char(A.MAKER_DATE,'DD-MON-YY HH:MM:SS') as MAKER_DATE,A.MKCK_STATUS,B.MOBILENO,B.BRANCH_CODE,"
		 * + "A.MAKER_ID,A.FLAG " +
		 * " from ACCT_LINK A,CARD_PRODUCTION_HASH C, CARD_PRODUCTION B " +" " +
		 * "where A.INST_ID=B.INST_ID AND A.ORDER_REF_NO=B.ORDER_REF_NO AND A.ORDER_REF_NO=C.ORDER_REF_NO "
		 * + "AND A.CARD_NO=C.HCARD_NO AND A.inst_id=? and A.FLAG=? and " +
		 * "A.MAKER_ID!=? AND  A.mkck_status IN ('P','M') "+condition+
		 * " order by B.branch_code";
		 * 
		 * enctrace("checking select query for authlist" +select_query); List
		 * persorderlist = jdbctemplate.queryForList(select_query,new
		 * Object[]{instid,acctcategory,userid,userid});
		 */

		trace("The Query Result is ==========>" + persorderlist);
		ListIterator itr = persorderlist.listIterator();
		while (itr.hasNext()) {
			Map temp = (Map) itr.next();
			String refnum = (String) temp.get("ORDER_REF_NO");
			String prodcode = (String) temp.get("PRODUCT_CODE");
			String usercode = (String) temp.get("MAKER_ID");
			String binno = (String) temp.get("BIN");
			String embossname = (String) temp.get("EMB_NAME");

			// String cin = (String)temp.get("CIN");
			// String productdesc = getProductdesc(instid, binno,prodcode);
			String productdesc = commondesc.getProductdesc(instid, prodcode, jdbctemplate);
			trace("INstituoion ID " + instid + "User Code ===> " + usercode);
			String username = commondesc.getUserName(instid, usercode, jdbctemplate);
			// String count =
			// this.getCardcount(instid,refnum,cardstatus,"REFNUM",
			// jdbctemplate);
			String mobile = (String) temp.get("MOBILENO");

			// List
			// embossname=getEmbossingName(instid,(String)temp.get("CIN"),"CIN",
			// jdbctemplate);
			// ListIterator lit = embossname.listIterator();
			// while(lit.hasNext()){
			// Map hm = (Map) lit.next();
			// String embossname2 = (String)hm.get("EMB_NAME");
			// trace("EBOSSNAME========== " +embossname2);
			// hm.put("EMBOSSING_NAME", embossname2);
			// itr.add(hm);
			// }
			trace("111111111111111111");
			temp.put("SUBPRODDESC",
					commondesc.getSubProductdesc(instid, (String) temp.get("SUB_PROD_ID"), jdbctemplate));
			trace("22222222222222222222");
			temp.put("CARDNO", (String) temp.get("CARD_NO"));
			trace("3333333333333333");
			// temp.put("COUNT", count);
			trace("44444444444444444");
			temp.put("EMBOSSING_NAME", embossname);
			temp.put("PNAME", productdesc);
			trace("555555555555555555");
			temp.put("USERNAME", username);
			trace("66666666666666666");
			temp.put("MOBILE", mobile);
			itr.remove();
			itr.add(temp);

		}

		return persorderlist;
	}

	public String filterCondition(String productcode, String branch, String fromdate, String todate, String dateflag) {
		String bincond = "";
		String branchcond = "";

		String datecond = "";

		if ("ALL".equalsIgnoreCase(productcode)) {
			bincond = "";
		} else {
			bincond = " AND B.PRODUCT_CODE='" + productcode + "' ";
		}

		if ("ALL".equalsIgnoreCase(branch)) {
			branchcond = "";
		} else {
			branchcond = " AND trim(B.CARD_COLLECT_BRANCH)='" + branch.trim() + "'";
		}

		if (fromdate != null && todate != null) {
			datecond = "AND ( to_date('" + fromdate + "', 'dd-mm-yyyy') <= " + dateflag + " AND to_date('" + todate
					+ "', 'dd-mm-yyyy' )+1 >= " + dateflag + ") ";
		}

		String filtercond = bincond + branchcond + datecond;

		return filtercond;

	}

	public List getcardAcctdetails(String acctno, String padssenable, String cardcategory, JdbcTemplate jdbctemplate) {

		String embnameqry = "";
		String cond = "";
		/*
		 * if(padssenable.equals("Y")){ cond= "CARD_NO ='"+cardno+"'"; }else{
		 * cond= "CARD_NO ='"+cardno+"'"; }
		 */
		List acctdetails = null;

		try {

			/*
			 * //embnameqry =
			 * "SELECT EMB_NAME,MOBILENO,CIN FROM CARD_PRODUCTION WHERE "+cond+
			 * " AND ROWNUM=1"; embnameqry =
			 * "SELECT ORDER_REF_NO,CIN,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CCY,LIMITBASED_ON,CARD_NO FROM ACCT_LINK WHERE ACCT_NO='"
			 * +acctno+"' and FLAG='"+cardcategory+"'";
			 * 
			 * enctrace("cin customer name edit-->"+embnameqry); acctdetails =
			 * jdbctemplate.queryForList(embnameqry);
			 */

			// by gowtham-300819
			embnameqry = "SELECT ORDER_REF_NO,CIN,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CCY,LIMITBASED_ON,"
					+ "CARD_NO FROM ACCT_LINK WHERE ACCT_NO=? and FLAG=? ";
			enctrace("cin customer name edit-->" + embnameqry);
			acctdetails = jdbctemplate.queryForList(embnameqry, new Object[] { acctno, cardcategory });

		} catch (Exception e) {
			e.printStackTrace();
		}
		return acctdetails;
	}

	/****************************
	 * REMOVE CBS ACCOUNT START
	 **************************/

	public String padssenable;

	public String getPadssenable() {
		return padssenable;
	}

	public void setPadssenable(String padssenable) {
		this.padssenable = padssenable;
	}

	public String removeCbsAcctHome() {
		trace("removeCbsAcctHome method called....");
		try {
			String act = getRequest().getParameter("act");
			trace("Act Flag--->" + act);
			setAct(act);
		} catch (Exception e) {
			trace("Exception While getting act flag--->" + e.getMessage());
			e.printStackTrace();

		}
		return "removecbsaccthome";
	}

	public String removeAccountNumber() {
		trace("removeAccountNumber method called...");
		boolean check;
		int checkvalue;
		//HttpSession session = getRequest().getSession();
		HttpSession session = getRequest().getSession(false);
		String sessioncsrftoken = (String) session.getAttribute("token");
		System.out.println("Global session token---> " + sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("removeAccountNumber() method   token---->    " + jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}
		String instid = comInstId(session);
		String usercode = comUserCode(session);

		StringBuilder sb = null;

		String cardno = getRequest().getParameter("cardnum");
		// System.out.println(cardno);

		checkvalue = Validation.number(cardno);
		if (checkvalue == 0) {
			addActionError("Enter Proper Card Number");
			return removeCbsAcctHome();
		}

		try {
			String act = getRequest().getParameter("act");
			trace("Act Flag--->" + act);
			setAct(act);

			// trace("Entered Original Card Number---->"+cardno);

			String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
			setPadssenable(padssenable);
			String keyid = "";
			String EDMK = "", EDPK = "";
			StringBuffer hcardno = new StringBuffer();
			PadssSecurity padsssec = new PadssSecurity();
			if (padssenable.equals("Y")) {
				keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::" + keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				// System.out.println("secList::"+secList);
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String eDMK = ((String) map.get("DMK"));
						String eDPK = ((String) map.get("DPK"));
						hcardno = padsssec.getHashedValue(cardno + instid);
					}
				}
			}

			trace("generated hashed card number:::" + hcardno);
			int checkvalid = -1;
			if (padssenable.equals("Y")) {
				checkvalid = cbsdao.validateActvatedCard(instid, padssenable, hcardno.toString(), jdbctemplate);
			} else {
				checkvalid = cbsdao.validateActvatedCard(instid, padssenable, cardno, jdbctemplate);
			}

			if (checkvalid <= 0) {
				trace("Entered Card Number Not Activated....Enter Valid Card No...!");
				addActionError("Entered Card Number Not Activated....Enter Valid Card No...!");
				return "removecbsaccthome";
			}

			List accounttypedetails = null;

			if (padssenable.equals("Y")) {
				accounttypedetails = cbsdao.getAccounttypeinfo(instid, padssenable, hcardno.toString(), jdbctemplate);
			} else {
				accounttypedetails = cbsdao.getAccounttypeinfo(instid, padssenable, cardno, jdbctemplate);
			}

			cbsbean.setAccounttypedetails(accounttypedetails);

			if (padssenable.equals("Y")) {
				cbsbean.setCardno(hcardno.toString());
			} else {
				cbsbean.setCardno(cardno);
			}
			cardno = "0000000000000000";
			sb = new StringBuilder(cardno);
			sb.setLength(0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cardno = null;
			sb = null;
		}

		// System.out.println(cardno);
		return "removecbsaccthome";
	}

	
	public String removeAddonaccountno() {
		trace("***********removeAddonaccountno begin's************");
		IfpTransObj transact = commondesc.myTranObject("RMACCT", txManager);
		//HttpSession session = getRequest().getSession();
		
		HttpSession session = getRequest().getSession(false);
		String sessioncsrftoken = (String) session.getAttribute("token");
		System.out.println("Global session token---> " + sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("authsaveacctdetails() method   token---->    " + jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}
		
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String cardno = getRequest().getParameter("cardno");
		// trace("checking this card number format--."+cardno);

		StringBuilder sb = null;
		// System.out.println("cardno"+cardno);

		String act = getRequest().getParameter("act");
		String padssenable = getRequest().getParameter("padssenable");
		String[] acctnum = getRequest().getParameterValues("personalrefnum");
		String mkckstatus = "", msg = "", acctno = "";
		try {

			trace("Act Got ::" + act + " No of Account to be Remove " + acctnum.length);
			if (act.equals("M")) {
				mkckstatus = "M";
				msg = "Waiting for Authorization...";
			} else {
				mkckstatus = "P";
				msg = "";
			}

			String customerid = cbsdao.gettingCustomerID(instid, padssenable, "CARD_PRODUCTION", cardno, jdbctemplate);

			trace("Customer id got..." + customerid);

			int count = 0;
			for (int i = 0; i < acctnum.length; i++) {
				acctno = acctnum[i];
				trace(acctno + " ---- Account Number for Processing...");
				int checkvalid = cbsdao.validateaccountfordelinkaccount(instid, padssenable, acctno, jdbctemplate);
				if (checkvalid > 0) {
					trace("Selected Account number is Removed,Waitng for authorization...");
					addActionError(" Account number is Removed, Waitng for authorization.....!");
					return "removecbsaccthome";
				}

				trace("inserting into ifd_card_acct_link.....pocess....");
				int res = cbsdao.insertCardAcctLinkFromEzauthrel(instid, mkckstatus, cardno, acctno, usercode,
						customerid, jdbctemplate);

				trace("inserting result ::: " + res);
				if (res > 0) {
					count++;
				}
			}
			trace("Selected Acct Length " + acctnum.length + " Processed Length " + count);

			if (acctnum.length == count) {
				txManager.commit(transact.status);
				trace("Account removed " + msg + "Committed Successfully...");
				addActionMessage("Account Removed Successfully..." + msg);
			} else {
				txManager.rollback(transact.status);
				trace("Account removed " + msg + "...Rollbacked Successfully...");
				addActionError("Unable to continue the Process...!");
			}

			cardno = "0000000000000000";
			sb = new StringBuilder(cardno);
			sb.setLength(0);

		} catch (Exception e) {
			trace("Exception ::: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cardno = null;
			sb = null;
		}

		// System.out.println(cardno);

		return "removecbsaccthome";
	}

	/****************************
	 * REMOVE CBS ACCOUNT END
	 **************************/

}
