package com.ifp.personalize;

import com.ifp.util.LogoutCSRF;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.ifg.usermgt.LogoutAction;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import test.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.DebugWriter;
import com.ifp.util.IfpTransObj;
import com.ifp.util.SQLConstants;

import connection.Dbcon;

/**
 * SRNP0004
 * 
 * @author CGSPL
 *
 */
@SuppressWarnings("deprecation")
public class PersonalCardprocessAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	Boolean carddebug = false;
	LogoutAction logout = new LogoutAction();
	CommonDesc commondesc = new CommonDesc();
	PadssSecurity sec = new PadssSecurity();
	// SQLConstants sqlConstants=new SQLConstants();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	PersionalizedcardCondition brcodecon = new PersionalizedcardCondition();

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

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

	public String comUsername() {
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("USERNAME");
		return username;
	}

	private List branchlist;

	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}

	private List personalproductlist;

	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}

	/*
	 * private char cardgenstatus; public char getCardgenstatus() { return
	 * cardgenstatus; } public void setCardgenstatus(char cardgenstatus) {
	 * this.cardgenstatus = cardgenstatus; }
	 */

	DebugWriter debugwriter = new DebugWriter();

	public String personalCardgenerationhome() {
		List pers_prodlist = null, br_list = null;
		trace("*********** Personalization card generation ************ ");
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("CARDGEN_ACT", act);
		}

		trace("User type is : " + usertype);
		try {

			if (usertype.equals("INSTADMIN")) {
				trace("Getting branchlist....");
				br_list = brcodecon.getBranchCodefmOrder(inst_id, "01", "P", jdbctemplate);

				// commondesc.generateBranchList(inst_id, jdbctemplate);

				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
				} else {
					setBranchlist(br_list);
					addActionError("No Cards Waiting for Generation .....");
					trace("Branch list is empty....");
					return "required_home";
				}
			}
			trace("Getting product list....");
			pers_prodlist = commondesc.getProductListCarGen(inst_id, jdbctemplate);
			trace("pers_prodlist" + pers_prodlist.size());
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Product Details Found ");
				trace("No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: Error While Fetching The Product Details");
			trace("Exception : While Fetching The Product Details" + e.getMessage());
			e.printStackTrace();

		}
		return "personalcardgenhome";
	}

	private List perscardgenlist;

	public List getPerscardgenlist() {
		return perscardgenlist;
	}

	public void setPerscardgenlist(List perscardgenlist) {
		this.perscardgenlist = perscardgenlist;
	}

	public String getCardgenerationorder() {

		// HttpSession ses = getRequest().getSession();
		// by siva 210819
		HttpSession ses = getRequest().getSession(false);
		String sessioncsrftoken = (String) ses.getAttribute("token");
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("jspcsrftoken----->    " + jspcsrftoken);
		System.out.println("sessioncsrftoken--------->" + sessioncsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			// ses.setAttribute("message", "CSRF Token Mismatch");
			addActionError("CSRF Token Mismatch");
			return "required_home";
		}
		// by siva 210819

		HttpSession session = getRequest().getSession();
		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String dateflag = "ORDERED_DATE";
		String inst_id = comInstId();
		List authorizeorderlist = null;
		String session_act = (String) session.getAttribute("CARDGEN_ACT");

		System.out.println("session_act " + session_act);
		try {

			trace("Checking generation status...");
			
				System.out.println("branch-->"+branch);
				System.out.println("binno-->"+binno);
			int cardgen = commondesc.checkCardgenerationstatus(inst_id, branch, jdbctemplate);
			trace("Got : " + cardgen);
			if (cardgen > 0) {
				setAct((String) session.getAttribute("CARDGEN_ACT"));
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",
						"Another User Processing Card Generation, Please Wait....<a href='removeProcessingPersonalCardprocess.do'> Remove </a> ");
				trace("Another User Processing Card Generation, Please Wait.... ");
				return personalCardgenerationhome();
			}

			String condition1 = " AND ORDER_STATUS='01' AND MKCK_STATUS='P'";
			String condtion = condition1 + commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			System.out.println("condtion==>" + condtion);
			authorizeorderlist = commondesc.getPersonalOrderList(inst_id, condtion, jdbctemplate);
			trace("authorizeorderlist===> " + authorizeorderlist);
			if (!(authorizeorderlist.isEmpty())) {
				setPerscardgenlist(authorizeorderlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");

			} else {
				setPerscardgenlist(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No Orders To Generate Card");
				return personalCardgenerationhome();

			}
		} catch (Exception e) {
			System.out.println("Exception--->" + e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Error while getting the cards....");
			trace("Error While Fetching The Orders To Card Genetaion " + e.getMessage());

		}
		return "personalcardgenorders";
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	public String personalCardgenerationprocess() throws Exception {
		trace("*********Card Generation Starts  *********** \n");
		enctrace("*********Card Generation Starts *********** \n");

		String keyid = null;
		String EDPK = null;
		String CDPK = null;
		String cardno = null;
		String parentcardno = null;
		String mcardno = null;

		List secList = null;
		int incseq = 0;

		String makerid = null;
		String checkerid = null;
		String makerdate = null;
		String checkerdate = null;
		String mkckstatus = null;

		StringBuilder sb = null;
		StringBuffer hcardno = null;
		PadssSecurity padsssec = null;
		Properties props = null;
		Personalizeorderdetails persorderdetails = null;
		Personalizeorderdetails bindetails = null;
		Personalizeorderdetails extradetails = null;

		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("PERSONAL", txManager);
		String instid = comInstId();
		String userid = comUserId();
		String branch = getRequest().getParameter("branchcode");
		String bin = getRequest().getParameter("binno");
		trace("Branch Id Selected is : " + branch + "\n Bin Number is : " + bin);
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		trace("Total Orders Selected : " + order_refnum.length);
		String CAF_REC_STATUS = "A", STATUS_CODE = "0";
		String authmsg = "";
		int cardgen = commondesc.checkCardgenerationstatus(instid, branch, jdbctemplate);
		trace("Card Gen Status" + cardgen);
		Boolean isallcardprocessed = true;

		String actiontype = (String) session.getAttribute("CARDGEN_ACT");
		trace("Action Type is ==>" + actiontype);
		String defaultdate = CommonDesc.default_date_query;

		String username = comUsername();
		
		trace("user id is ===== "+userid +"username===   "+username);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();

		if (actiontype.equals("M")) {
			makerid = userid;
			makerdate = "(sysdate)";
			mkckstatus = "M";
			checkerid = userid;
			checkerdate = defaultdate;
			authmsg = " Waiting For Authorization ";
		} else if (actiontype.equals("D")) {
			makerid = userid;
			checkerid = userid;
			makerdate = "(sysdate)";
			checkerdate = "(sysdate)";
			mkckstatus = "P";
			authmsg = "";
		}
		// String branchattch =
		// commondesc.checkBranchattached(instid,jdbctemplate);
		// System.out.println("Branch Attached For this INSTITUION IS ===>
		// "+branchattch);
		/*
		 * String branchattch=""; String prodcardtype=""; List attachedtype =
		 * commondesc.checkattachedtype(instid,bin,jdbctemplate); Iterator
		 * attype = attachedtype.iterator(); if(!attachedtype.isEmpty()){
		 * while(attype.hasNext()) { Map map = (Map) attype.next();
		 * prodcardtype= ((String)map.get("ATTACH_PRODTYPE_CARDTYPE"));
		 * branchattch = ((String)map.get("ATTACH_BRCODE")); } }
		 */

		
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

		trace("Getting bin details for the bin [ " + bin + " ] ");
		bindetails = commondesc.gettingBindetails(instid, bin, jdbctemplate);
		trace("Processing order reference count : " + order_refnum.length);

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		;
		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			props = getCommonDescProperty();
			EDPK = props.getProperty("EDPK");
		}

		try {

			padsssec = new PadssSecurity();
			hcardno = new StringBuffer();
			sb = new StringBuilder();

			for (int i = 0; i < order_refnum.length; i++) {
				trace("---------------" + (i + 1) + " Card Processing..... Processing order [ customer id ] : "
						+ order_refnum[i]);
				persorderdetails = commondesc.gettingOrderdetailsByCin(instid, order_refnum[i].trim(), jdbctemplate);
				if (carddebug) {
					trace("Got personal order details ");
				}
				extradetails = commondesc.getCcyexpiry(instid, persorderdetails.bin, persorderdetails.card_type_id,
						persorderdetails.sub_prod_id, persorderdetails.product_code, jdbctemplate);
				if (carddebug) {
					trace("Getting card curreny data...got :  " + extradetails);
				}
				// trace("Got : " + extradetails );
				
				
				
				String bulkRefId = commondesc.getBulkRefID(order_refnum[i], instid, jdbctemplate);
				String cardFlag=commondesc.getCardFlag(order_refnum[i], instid, jdbctemplate);
			    //trace(" first :: "+bulkRegID.get(0)+"   second "+bulkRegID.get(1));
				
				
				
				

				trace(bindetails.prodcard_expiry + "||" + bindetails.brcode_servicecode + "||"
						+ persorderdetails.card_type_id + "||" + persorderdetails.sub_prod_id + "||"
						+ persorderdetails.product_code + "||" + persorderdetails.branch_code + "||"
						+ bindetails.apptypelen + "||" + bindetails.apptypevalue);

				String breakupvalue = commondesc.getChnbreakupvalues(instid, bindetails.prodcard_expiry,
			
						bindetails.brcode_servicecode, persorderdetails.card_type_id, persorderdetails.sub_prod_id,
						persorderdetails.product_code, persorderdetails.branch_code, bindetails.apptypelen,
						bindetails.apptypevalue);
				trace("Getting card breakup details...got : " + breakupvalue);
				int expprd = Integer.parseInt(extradetails.prodcard_expiry);

				trace("product expiry date is--->  " + extradetails.prodcard_expiry);

				String expirydate = "add_months(sysdate," + expprd + ")";

				trace("expiry date is --->   " + expirydate);
				String newchn = persorderdetails.bin + breakupvalue;
				// trace("newchn:::" + newchn);
				// String sequncenumber =
				// commondesc.gettingSequnceNumberNew(instid,persorderdetails.bin,persorderdetails.branch_code,bindetails.baselen_feecode,persorderdetails.card_type_id,persorderdetails.sub_prod_id,jdbctemplate,branchattch,prodcardtype_attach);

				String sequncenumber = commondesc.gettingSequnceNumberNew(instid, persorderdetails.bin,
						persorderdetails.branch_code, bindetails.baselen_feecode, persorderdetails.card_type_id,
						persorderdetails.sub_prod_id, jdbctemplate, branchattch, prodcardtype_attach);

				trace("sequncenumber:::" + sequncenumber);

				int cardcount = 0;
				int cardtoprocess = Integer.parseInt(persorderdetails.card_quantity);
				long sequn_no = Long.parseLong(sequncenumber);

				trace("Getting card reference number length ....");
				String cardrefnulen = commondesc.getCardReferenceNumberLen(instid, jdbctemplate);
				trace("Got : " + cardrefnulen);
				if (cardrefnulen == null) {
					addActionError("Could not generate card... Card Referene number length is empty...");
					trace("Could not generate card... Card Referene number length is empty...");
					return personalCardgenerationhome();
				}

				String cardissuetype = commondesc.getCardIssueTypeByOrder(instid, persorderdetails.order_ref_no,
						"PERSONAL", jdbctemplate);

				if (cardissuetype != null && cardissuetype.equals("$SUPLIMENT")) {
					parentcardno = commondesc.getParentCardNumberByOrder(instid, persorderdetails.order_ref_no,
							"PERSONAL", jdbctemplate);
				}

				String pcflag = "P";
				int quryresult = 0;
				int quryresult1 = 0;

				String strseq = Long.toString(sequn_no);
				trace("Generating card number....");
				/*
				 * cardno = commondesc.generateCHN(newchn, strseq,
				 * Integer.parseInt(bindetails.baselen_feecode),
				 * Integer.parseInt(bindetails.chnlen_glcode));
				 */
				cardno = commondesc.generateCHN(newchn, strseq, Integer.parseInt(bindetails.baselen_feecode),
						Integer.parseInt(bindetails.chnlen_glcode));

				if (cardno.equals("N")) {
					session.setAttribute("prevmsg", "Card Generation Failed");
					session.setAttribute("preverr", "E");
					cardcount = -2;
					trace("Card Number Generated is Invalid,Process Skipped");
					isallcardprocessed = false;
					break;
				}
				if (cardno.equals("M")) {
					session.setAttribute("prevmsg", " Card Number Sequnce Reached Maximum Length ");
					session.setAttribute("preverr", "E");
					cardcount = -3;
					trace("Card Number Sequnce Reached Maximum Length,Process Skipped");
					isallcardprocessed = false;
					break;
				}
				trace("Generating card reference number....");
				trace("instid, persorderdetails.sub_prod_id" + instid + "::" + persorderdetails.sub_prod_id);

				String cardrefno = commondesc.generateCardRefNumber(instid, persorderdetails.sub_prod_id, cardrefnulen,
						jdbctemplate);
				trace("Got cardrefno : " + cardrefno);
				if (cardrefno == null) {
					txManager.rollback(transact.status);
					addActionError("Could not generate card... Got Card Referene number  is empty...");
					trace("Could not generate card... Card Referene number length is empty...");
					isallcardprocessed = false;
					break;
				}

				if (parentcardno == null) {
					parentcardno = cardno;
				}

				// trace("Parent card number...." + parentcardno);

				String customermobile = commondesc.getMobileNumberByCustomerProcess(instid, persorderdetails.cin,
						jdbctemplate);
				trace("Getting Customer Mobile Number : " + customermobile);

				trace("Getting Account no .....");

				List accountdetails = commondesc.getAccountNoDetailsAcctinfo(instid, persorderdetails.cin,
						jdbctemplate);

				Iterator itr = accountdetails.iterator();
				String accttypeid = "", accsubtypeid = "", accccy = "", accountno = "";
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();

					accttypeid = (String) mp.get("ACCTTYPE_ID");
					accsubtypeid = (String) mp.get("ACCTSUB_TYPE_ID");
					accccy = (String) mp.get("ACCT_CURRENCY");
					accountno = (String) mp.get("ACCOUNTNO");

				}
				String renewalflag = "", collectbranch = "";
				List<Map<String, Object>> getrenewalflag = commondesc.getRenewalFlag(instid,
						persorderdetails.order_ref_no, jdbctemplate);
				if (!getrenewalflag.isEmpty()) {
					renewalflag = (String) getrenewalflag.get(0).get("RENEWALFLAG");
					collectbranch = (String) getrenewalflag.get(0).get("CARD_COLLECT_BRANCH");
				}
				
				trace (" secList=====  "+secList);
				enctrace("renewalflag is : " + renewalflag);
				mcardno = padsssec.getMakedCardno(cardno);

				trace("mcardno--->  " + mcardno );

				if (padssenable.equals("Y")) {
					Iterator secitr = secList.iterator();
					if (!secList.isEmpty()) {
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							String CDMK = ((String) map.get("DMK"));
							CDPK = padsssec.decryptDPK(CDMK, EDPK);
							hcardno = padsssec.getHashedValue(cardno + instid);
							cardno = padsssec.getECHN(CDPK, cardno);
						}
					}	
				}

				
				trace("====cardno =====  "+cardno  +"          hcardno==== "+hcardno);
				int cardnoexists = commondesc.checkCardNoExists(instid, hcardno, jdbctemplate);
				if (cardnoexists > 0) {
					txManager.rollback(transact.status);
					trace("duplicate card no generated for this card--->" + hcardno);
					addActionError("Unable To Continue Process...!!!");
					return "required_home";
				}

				String servicecode = commondesc.getServiceCode(instid, bin, jdbctemplate);

				String cardstatus = "01";
				trace("Inserting personal card data");

				// added by prasad
				String cardinsrt_qury = "INSERT INTO PERS_CARD_PROCESS(INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO,ACCTTYPE_ID,"
						+ "ACCTSUB_TYPE_ID,ACC_CCY,CIN,CARD_TYPE_ID,"
						+ "SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,"
						+ "ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,"
						+ "ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,"
						+ "LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,"
						+ "AUTH_DATE, CARD_REF_NO, CARDISSUETYPE, MOBILENO ,PANSEQ_NO,RENEWALFLAG,CARD_COLLECT_BRANCH,CARD_FLAG,BULK_REG_ID) VALUES ('"
						+ instid + "','" + persorderdetails.order_ref_no + "','" + persorderdetails.bin + "','"
						+ mcardno + "','" + accountno + "' ,'" + accttypeid + "','" + accsubtypeid + "'  ,'" + accccy
						+ "' ,'" + persorderdetails.cin + "','" + persorderdetails.card_type_id + "'," + "'"
						+ persorderdetails.sub_prod_id + "','" + persorderdetails.product_code + "','"
						+ persorderdetails.branch_code + "','" + cardstatus + "','" + CAF_REC_STATUS + "','"
						+ STATUS_CODE + "','" + extradetails.cardscount_cardccy + "','P','P'," + "'" + cardno + "','"
						+ cardno + "',(sysdate)," + expirydate + ",'" + persorderdetails.appno + "',SYSDATE,"
						+ defaultdate + "," + defaultdate + ",''," + defaultdate + "," + defaultdate + "," + defaultdate
						+ "," + defaultdate + ",'" + makerid + "'," + makerdate + ",'" + checkerid + "'," + checkerdate
						+ ",'" + mkckstatus + "','" + servicecode + "','" + extradetails.baselen_feecode.trim() + "',"
						+ "'" + extradetails.chnbaseno_limitid.trim() + "','0','0','0','0','0','0','0','"
						+ persorderdetails.embossing_name + "','" + persorderdetails.encode_data + "','','', '"
						+ cardrefno + "','" + cardissuetype + "','" + customermobile + "','00','" + renewalflag + "','"
						+ collectbranch + "','"+cardFlag + "','"+bulkRefId+"')";
				enctrace("Insert Qury is : " + cardinsrt_qury);

				String cardinsrt_qury1 = "INSERT INTO PERS_CARD_PROCESS_HASH (INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,CIN,CARD_TYPE_ID,"
						+ "SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)  VALUES ('"
						+ instid + "','" + persorderdetails.order_ref_no + "','" + persorderdetails.bin + "','"
						+ hcardno.toString() + "','" + accountno + "' ,'" + persorderdetails.cin + "','"
						+ persorderdetails.card_type_id + "','" + persorderdetails.sub_prod_id + "','"
						+ persorderdetails.product_code + "','" + persorderdetails.branch_code + "',SYSDATE,'" + makerid
						+ "'," + makerdate + ", '" + checkerid + "', " + checkerdate + ")";

				// nullify
				cardno = "0000000000000000";
				parentcardno = "0000000000000000";
				cardno = null;
				parentcardno = null;
				sb.append(cardno);
				sb.setLength(0);

				enctrace("cardinsrt_qury1 : " + cardinsrt_qury1);

				try {
					quryresult = commondesc.executeTransaction(cardinsrt_qury, jdbctemplate);
					quryresult1 = commondesc.executeTransaction1(cardinsrt_qury1, jdbctemplate);
				} catch (Exception e) {
					trace("Insert Qury is::" + e);
					e.printStackTrace();
				}

				trace("Got : " + quryresult);
				if (quryresult == 1 && quryresult1 == 1) {
					cardcount++;
					sequn_no++;
				} else {
					cardcount = -1;
					trace("CHN Insert Failed, Process Break");
					isallcardprocessed = false;
					break;
				}

				/************* AUDIT BLOCK **************/
				try {

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg("Card [ " + mcardno + " ] Generated For the Order [ "
							+ persorderdetails.order_ref_no + " ]");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0102");
					String product_code = null;
					// auditbean.setProduct(persorderdetails.product_code);
					auditbean.setSubproduct(persorderdetails.sub_prod_id);
					auditbean.setBin(persorderdetails.bin);
					auditbean.setApplicationid(persorderdetails.order_ref_no);
					auditbean.setCardno(mcardno.toString());
					auditbean.setCin(persorderdetails.cin);
					auditbean.setAccoutnno(accountno);
					auditbean.setApptype("IFD");
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}

				/************* AUDIT BLOCK **************/
				trace("checking cardtoproccess count " + cardtoprocess);
				trace("checking card count " + cardcount);
				if (cardtoprocess == cardcount) {
					trace("All the Cards Generated Succesfully ");
					String updateseq = "";

					//// Changed For update last Sequance in Baseno table

					/*
					 * if (branchattch.equals("Y")) { updateseq =
					 * "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no +
					 * "' WHERE INST_ID='" + instid + "' AND BIN='" +
					 * persorderdetails.bin + "' AND BASENO_CODE='" +
					 * persorderdetails.branch_code + "' "; } else if
					 * (prodcardtype_attach.equals("C")) { updateseq =
					 * "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no +
					 * "' WHERE INST_ID='" + instid + "' AND BIN='" +
					 * persorderdetails.bin + "' AND BASENO_CODE='" +
					 * persorderdetails.card_type_id + "' "; } else if
					 * (prodcardtype_attach.equals("P")) { updateseq =
					 * "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no +
					 * "' WHERE INST_ID='" + instid + "' AND BIN='" +
					 * persorderdetails.bin + "' AND BASENO_CODE='" +
					 * persorderdetails.sub_prod_id + "' "; } else { updateseq =
					 * "UPDATE PRODUCTINFO SET CHN_BASE_NO='" + sequn_no +
					 * "' WHERE INST_ID='" + instid + "' AND BIN='" +
					 * persorderdetails.bin + "'"; }
					 */

					/// by gowtham260819
					if (branchattch.equals("Y")) {
						trace("1");
						updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq,
								new Object[] { sequn_no, instid, persorderdetails.bin, persorderdetails.branch_code });

					} else if (prodcardtype_attach.equals("C")) {
						trace("2");
						updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq, new Object[] { sequn_no, instid, persorderdetails.bin,
								persorderdetails.card_type_id, });

					} else if (prodcardtype_attach.equals("P")) {
						trace("3");
						updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq,
								new Object[] { sequn_no, instid, persorderdetails.bin, persorderdetails.sub_prod_id });

					} else {
						trace("4");
						updateseq = "UPDATE PRODUCTINFO SET CHN_BASE_NO=? WHERE INST_ID=? AND PRD_CODE=?";
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq,
								new Object[] { sequn_no, instid, persorderdetails.bin });

					}

					/*
					 * if(prodcardtype.equals("C")){
					 * 
					 * updateseq = "UPDATE BRANCH_MASTER SET \""
					 * +persorderdetails.card_type_id+"_CHN_BASE_NO\"='"+
					 * sequn_no+"' WHERE INST_ID='"+instid+
					 * "' AND BRANCH_CODE='000'"; }else{
					 * if(branchattch.equals("Y")){ updateseq =
					 * "UPDATE BRANCH_MASTER SET \""
					 * +persorderdetails.card_type_id+"_CHN_BASE_NO\"='"+
					 * sequn_no+"' WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"
					 * +persorderdetails.branch_code+"'"; enctrace(
					 * "updateseq Qury is : "+updateseq); }else
					 * if(branchattch.equals("N")){ updateseq =
					 * "UPDATE PRODUCTINFO SET CHN_BASE_NO='"+sequn_no+
					 * "' WHERE INST_ID='"+instid+"' AND BIN='"
					 * +persorderdetails.bin+"'"; } }
					 */
					enctrace(" updateseq : " + updateseq);
					/* incseq = jdbctemplate.update(updateseq); */

					/*
					 * if (incseq > 0) { String updateorderstatus = "UPDATE
					 * PERS_CARD_ORDER SET
					 * ORDER_STATUS='02',MAKER_DATE=(sysdate), MAKER_TIME=
					 * "+ commondesc.def_time + ",MAKER_ID='" + userid + "'
					 * WHERE INST_ID='" + instid + "' AND CIN='" +
					 * order_refnum[i] + "'"; enctrace(
					 * " Card Order Status QUry " + updateorderstatus); int
					 * order = jdbctemplate.update(updateorderstatus);
					 */

					/// by gowtham260819
					if (incseq > 0) {
						String updateorderstatus = "UPDATE PERS_CARD_ORDER SET ORDER_STATUS=?,MAKER_DATE=?,MAKER_TIME="
								+ commondesc.def_time + ",MAKER_ID=? WHERE INST_ID=? AND CIN=? ";
						enctrace(" Card Order Status QUry " + updateorderstatus);
						int order = jdbctemplate.update(updateorderstatus,
								new Object[] { "02", date.getCurrentDate(), userid, instid, order_refnum[i] });

						trace("Updating Card Order Status...got :" + order);
						if (order > 0) {
							trace("Order Status Update successfully,order count " + order);
							isallcardprocessed = true;

						}
					} else {
						addActionError("Unable to continue the process");
						trace("CHN BASE NO Update Failed,Txn got Rollback ");
						isallcardprocessed = false;
						break;
					}

				}
			} // end for loop

			trace("End of Loop.... Boolean isallcardprocess : " + isallcardprocessed);
			if (!isallcardprocessed) {
				addActionError("Unable to continue the process");
				txManager.rollback(transact.status);
				trace(" Card Geneartion Process failed txn got roll back");

			} else {
				txManager.commit(transact.status);
				trace("Committed...");
				session.setAttribute("prevmsg", order_refnum.length + " Card(s) Generated Successfully. " + authmsg);
				session.setAttribute("preverr", "S");
			}

			/*
			 * //---------------- edited by sardar on 11-12-15 ---------// try{
			 * for(int i=0;i<order_refnum.length;i++) { trace("---------------"+
			 * (i+1) +
			 * " Card Processing..... Processing order [ customer id ] : " +
			 * order_refnum[i] ); persorderdetails =
			 * commondesc.gettingOrderdetailsByCin(instid,order_refnum[i].trim()
			 * ,jdbctemplate);
			 * 
			 * try{ auditbean.setActmsg("Card Generated For the Order [ "
			 * +persorderdetails.order_ref_no+" ]");
			 * auditbean.setUsercode(username);
			 * auditbean.setAuditactcode("0102");
			 * auditbean.setProduct(persorderdetails.product_code);
			 * auditbean.setSubproduct(persorderdetails.sub_prod_id);
			 * auditbean.setBin(persorderdetails.bin);
			 * auditbean.setApplicationid(persorderdetails.order_ref_no); //
			 * auditbean.setCardno(mcardno);
			 * commondesc.insertAuditTrailPendingCommit(instid, username,
			 * auditbean, jdbctemplate, txManager); }catch(Exception audite ){
			 * trace("Exception in auditran : "+ audite.getMessage()); }
			 * 
			 * } } catch (Exception e) { trace("Could not continue the process"
			 * + e.getMessage()); e.printStackTrace(); if(
			 * !transact.status.isCompleted() ) {
			 * txManager.rollback(transact.status) ; } //addActionError(
			 * "Error While Process Card Geneartion");
			 * session.setAttribute("prevmsg","Unable To Continue The Process");
			 * session.setAttribute("preverr","E"); }
			 * setAct((String)session.getAttribute("CARDGEN_ACT"));
			 */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// added by prasad
			// nullify the objects

			persorderdetails = null;
			bindetails = null;
			extradetails = null;
			keyid = null;
			EDPK = null;
			CDPK = null;
			props = null;

			cardno = "0000000000000000";
			cardno = null;
			parentcardno = null;
			sb.setLength(0);
			sb = null;

			hcardno = null;
			padsssec = null;
			secList = null;
			incseq = 0;

			makerid = null;
			checkerid = null;
			makerdate = null;
			checkerdate = null;
			mkckstatus = null;
			mcardno = null;
		}
		return personalCardgenerationhome();
	}

	// BY SIVA 17-07-2019 07:45 PM

	/*
	 * public String personalCardgenerationprocess() throws Exception { trace(
	 * "*********Registering peronalized card *********** \n"); enctrace(
	 * "*********Registering peronalized card *********** \n"); HttpSession
	 * session = getRequest().getSession(); IfpTransObj transact =
	 * commondesc.myTranObject("PERSONAL", txManager); String instid =
	 * comInstId(); String userid = comUserId(); String branch =
	 * getRequest().getParameter("branchcode"); String bin =
	 * getRequest().getParameter("binno"); trace("Branch Id Selected is : " +
	 * branch + "\n Bin Number is : " + bin); String order_refnum[] =
	 * getRequest().getParameterValues("personalrefnum"); trace(
	 * "Total Orders Selected : " + order_refnum.length); String CAF_REC_STATUS
	 * = "A", STATUS_CODE = "0"; String authmsg = ""; Personalizeorderdetails
	 * persorderdetails, bindetails, extradetails; int cardgen =
	 * commondesc.checkCardgenerationstatus(instid, branch, jdbctemplate);
	 * trace("Card Gen Status" + cardgen); Boolean isallcardprocessed = true;
	 * int incseq = 0; String actiontype = (String)
	 * session.getAttribute("CARDGEN_ACT"); trace("Action Type is ==>" +
	 * actiontype); String defaultdate = CommonDesc.default_date_query; String
	 * makerid = "", checkerid = "", makerdate = "", checkerdate = "",
	 * mkckstatus = "";
	 * 
	 * String username = comUsername();
	 * 
	 * if (actiontype.equals("M")) { makerid = userid; makerdate = "(sysdate)";
	 * mkckstatus = "M"; checkerdate = defaultdate; authmsg =
	 * " Waiting For Authorization "; } else if (actiontype.equals("D")) {
	 * makerid = userid; checkerid = userid; makerdate = "(sysdate)";
	 * checkerdate = "(sysdate)"; mkckstatus = "P"; authmsg = ""; } // String
	 * branchattch = // commondesc.checkBranchattached(instid,jdbctemplate); //
	 * System.out.println("Branch Attached For this INSTITUION IS ===> //
	 * "+branchattch);
	 * 
	 * String branchattch=""; String prodcardtype=""; List attachedtype =
	 * commondesc.checkattachedtype(instid,bin,jdbctemplate); Iterator attype =
	 * attachedtype.iterator(); if(!attachedtype.isEmpty()){
	 * while(attype.hasNext()) { Map map = (Map) attype.next(); prodcardtype=
	 * ((String)map.get("ATTACH_PRODTYPE_CARDTYPE")); branchattch =
	 * ((String)map.get("ATTACH_BRCODE")); } }
	 * 
	 * 
	 * String branchattch = ""; String prodcardtype_attach = ""; List
	 * attachedtype = commondesc.checkattachedtype(instid, bin, jdbctemplate);
	 * Iterator attype = attachedtype.iterator(); if (!attachedtype.isEmpty()) {
	 * while (attype.hasNext()) { Map map = (Map) attype.next();
	 * prodcardtype_attach = ((String) map.get("ATTACH_PRODTYPE_CARDTYPE"));
	 * branchattch = ((String) map.get("ATTACH_BRCODE")); } }
	 * 
	 * trace("Getting bin details for the bin [ " + bin + " ] "); bindetails =
	 * commondesc.gettingBindetails(instid, bin, jdbctemplate); trace(
	 * "Processing order reference count : " + order_refnum.length);
	 * 
	 * String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
	 * 
	 * String keyid = ""; String EDMK = "", EDPK = ""; String CDPK=""; String
	 * cardno=""; //StringBuilder cardno=new StringBuilder(); StringBuffer
	 * hcardno = new StringBuffer(); PadssSecurity padsssec = new
	 * PadssSecurity(); List secList = null; ; if (padssenable.equals("Y")) {
	 * 
	 * keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
	 * System.out.println("keyid::" + keyid); secList =
	 * commondesc.getPADSSDetailById(keyid, jdbctemplate); Properties
	 * props=getCommonDescProperty(); EDPK=props.getProperty("EDPK");
	 * 
	 * System.out.println("secList::" + secList);
	 * 
	 * } try { for (int i = 0; i < order_refnum.length; i++) {
	 * trace("---------------" + (i + 1) +
	 * " Card Processing..... Processing order [ customer id ] : " +
	 * order_refnum[i]); persorderdetails =
	 * commondesc.gettingOrderdetailsByCin(instid, order_refnum[i].trim(),
	 * jdbctemplate); if (carddebug) { trace("Got personal order details "); }
	 * extradetails = commondesc.getCcyexpiry(instid, persorderdetails.bin,
	 * persorderdetails.card_type_id, persorderdetails.sub_prod_id,
	 * persorderdetails.product_code, jdbctemplate); if (carddebug) { trace(
	 * "Getting card curreny data...got :  " + extradetails); } // trace(
	 * "Got : " + extradetails );
	 * 
	 * trace(bindetails.prodcard_expiry + "||" + bindetails.brcode_servicecode +
	 * "||" + persorderdetails.card_type_id + "||" +
	 * persorderdetails.sub_prod_id + "||" + persorderdetails.product_code +
	 * "||" + persorderdetails.branch_code + "||" + bindetails.apptypelen + "||"
	 * + bindetails.apptypevalue);
	 * 
	 * String breakupvalue = commondesc.getChnbreakupvalues(instid,
	 * bindetails.prodcard_expiry, bindetails.brcode_servicecode,
	 * persorderdetails.card_type_id, persorderdetails.sub_prod_id,
	 * persorderdetails.product_code, persorderdetails.branch_code,
	 * bindetails.apptypelen, bindetails.apptypevalue); trace(
	 * "Getting card breakup details...got : " + breakupvalue); int expprd =
	 * Integer.parseInt(extradetails.prodcard_expiry);
	 * 
	 * trace("product expiry date is--->  " + extradetails.prodcard_expiry);
	 * 
	 * String expirydate = "add_months(sysdate," + expprd + ")";
	 * 
	 * trace("expiry date is --->   " + expirydate); String newchn =
	 * persorderdetails.bin + breakupvalue; // trace("newchn:::" + newchn); //
	 * String sequncenumber = //
	 * commondesc.gettingSequnceNumberNew(instid,persorderdetails.bin,
	 * persorderdetails.branch_code,bindetails.baselen_feecode,persorderdetails.
	 * card_type_id,persorderdetails.sub_prod_id,jdbctemplate,branchattch,
	 * prodcardtype_attach);
	 * 
	 * String sequncenumber = commondesc.gettingSequnceNumberNew(instid,
	 * persorderdetails.bin, persorderdetails.branch_code,
	 * bindetails.baselen_feecode, persorderdetails.card_type_id,
	 * persorderdetails.sub_prod_id, jdbctemplate, branchattch,
	 * prodcardtype_attach);
	 * 
	 * trace("sequncenumber:::" + sequncenumber);
	 * 
	 * int cardcount = 0; int cardtoprocess =
	 * Integer.parseInt(persorderdetails.card_quantity); long sequn_no =
	 * Long.parseLong(sequncenumber);
	 * 
	 * trace("Getting card reference number length ...."); String cardrefnulen =
	 * commondesc.getCardReferenceNumberLen(instid, jdbctemplate); trace(
	 * "Got : " + cardrefnulen); if (cardrefnulen == null) { addActionError(
	 * "Could not generate card... Card Referene number length is empty...");
	 * trace(
	 * "Could not generate card... Card Referene number length is empty...");
	 * return personalCardgenerationhome(); }
	 * 
	 * String cardissuetype = commondesc.getCardIssueTypeByOrder(instid,
	 * persorderdetails.order_ref_no, "PERSONAL", jdbctemplate);
	 * 
	 * String parentcardno = null; if (cardissuetype != null &&
	 * cardissuetype.equals("$SUPLIMENT")) { parentcardno =
	 * commondesc.getParentCardNumberByOrder(instid,
	 * persorderdetails.order_ref_no, "PERSONAL", jdbctemplate); trace(
	 * "generating parentcardno..got  :" + parentcardno); }
	 * 
	 * String pcflag = "P"; int quryresult = 0; int quryresult1 = 0;
	 * 
	 * String strseq = Long.toString(sequn_no); trace(
	 * "Generating card number...."); cardno = commondesc.generateCHN(newchn,
	 * strseq, Integer.parseInt(bindetails.baselen_feecode),
	 * Integer.parseInt(bindetails.chnlen_glcode)); String mcardno = "";
	 * 
	 * // trace("Got : " + cardno); if (cardno.equals("N")) {
	 * session.setAttribute("prevmsg", "Card Generation Failed");
	 * session.setAttribute("preverr", "E"); cardcount = -2; trace(
	 * "Card Number Generated is Invalid,Process Skipped"); isallcardprocessed =
	 * false; break; } if (cardno.equals("M")) { session.setAttribute("prevmsg",
	 * " Card Number Sequnce Reached Maximum Length ");
	 * session.setAttribute("preverr", "E"); cardcount = -3; trace(
	 * "Card Number Sequnce Reached Maximum Length,Process Skipped");
	 * isallcardprocessed = false; break; } trace(
	 * "Generating card reference number...."); trace(
	 * "instid, persorderdetails.sub_prod_id" + instid + "::" +
	 * persorderdetails.sub_prod_id);
	 * 
	 * String cardrefno = commondesc.generateCardRefNumber(instid,
	 * persorderdetails.sub_prod_id, cardrefnulen, jdbctemplate); trace(
	 * "Got cardrefno : " + cardrefno); if (cardrefno == null) {
	 * txManager.rollback(transact.status); addActionError(
	 * "Could not generate card... Got Card Referene number  is empty...");
	 * trace(
	 * "Could not generate card... Card Referene number length is empty...");
	 * isallcardprocessed = false; break; }
	 * 
	 * if (parentcardno == null) { parentcardno = cardno; }
	 * 
	 * //trace("Parent card number...." + parentcardno);
	 * 
	 * String customermobile =
	 * commondesc.getMobileNumberByCustomerProcess(instid, persorderdetails.cin,
	 * jdbctemplate); trace("Getting Customer Mobile Number : " +
	 * customermobile);
	 * 
	 * trace("Getting Account no .....");
	 * 
	 * List accountdetails = commondesc.getAccountNoDetailsAcctinfo(instid,
	 * persorderdetails.cin, jdbctemplate);
	 * 
	 * Iterator itr = accountdetails.iterator(); String accttypeid = "",
	 * accsubtypeid = "", accccy = "", accountno = ""; while (itr.hasNext()) {
	 * Map mp = (Map) itr.next();
	 * 
	 * accttypeid = (String) mp.get("ACCTTYPE_ID"); accsubtypeid = (String)
	 * mp.get("ACCTSUB_TYPE_ID"); accccy = (String) mp.get("ACCT_CURRENCY");
	 * accountno = (String) mp.get("ACCOUNTNO");
	 * 
	 * } String renewalflag = "", collectbranch = ""; List<Map<String, Object>>
	 * getrenewalflag = commondesc.getRenewalFlag(instid,
	 * persorderdetails.order_ref_no, jdbctemplate); if
	 * (!getrenewalflag.isEmpty()) { renewalflag = (String)
	 * getrenewalflag.get(0).get("RENEWALFLAG"); collectbranch = (String)
	 * getrenewalflag.get(0).get("CARD_COLLECT_BRANCH"); } enctrace(
	 * "renewalflag is : " + renewalflag); mcardno =
	 * padsssec.getMakedCardno(cardno);
	 * 
	 * if (padssenable.equals("Y")) { Iterator secitr = secList.iterator(); if
	 * (!secList.isEmpty()) { while (secitr.hasNext()) { Map map = (Map)
	 * secitr.next(); String CDMK = ((String) map.get("DMK")); //String eDPK =
	 * ((String) map.get("DPK")); CDPK=padsssec.decryptDPK(CDMK, EDPK); hcardno
	 * = padsssec.getHashedValue(cardno + instid); cardno =
	 * padsssec.getECHN(CDPK, cardno); } } }
	 * 
	 * int cardnoexists = commondesc.checkCardNoExists(instid, hcardno,
	 * jdbctemplate); if (cardnoexists > 0) {
	 * txManager.rollback(transact.status); trace(
	 * "duplicate card no generated for this card--->" + hcardno);
	 * addActionError("Unable To Continue Process...!!!"); return
	 * "required_home"; }
	 * 
	 * String servicecode = commondesc.getServiceCode(instid, bin,
	 * jdbctemplate);
	 * 
	 * String cardstatus = "01"; trace("Inserting personal card data"); String
	 * cardinsrt_qury =
	 * "INSERT INTO PERS_CARD_PROCESS(INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO,ACCTTYPE_ID,"
	 * + "ACCTSUB_TYPE_ID,ACC_CCY,CIN,CARD_TYPE_ID," +
	 * "SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,"
	 * +
	 * "ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,"
	 * +
	 * "ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,"
	 * +
	 * "LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,"
	 * +
	 * "AUTH_DATE, CARD_REF_NO, CARDISSUETYPE, MOBILENO ,PANSEQ_NO,RENEWALFLAG,CARD_COLLECT_BRANCH) VALUES ('"
	 * + instid + "','" + persorderdetails.order_ref_no + "','" +
	 * persorderdetails.bin + "','" + mcardno + "','" + accountno + "' ,'" +
	 * accttypeid + "','" + accsubtypeid + "'  ,'" + accccy + "' ,'" +
	 * persorderdetails.cin + "','" + persorderdetails.card_type_id + "'," + "'"
	 * + persorderdetails.sub_prod_id + "','" + persorderdetails.product_code +
	 * "','" + persorderdetails.branch_code + "','" + cardstatus + "','" +
	 * CAF_REC_STATUS + "','" + STATUS_CODE + "','" +
	 * extradetails.cardscount_cardccy + "','P','P'," + "'" + cardno + "','" +
	 * cardno + "',(sysdate)," + expirydate + ",'" + persorderdetails.appno +
	 * "',SYSDATE," + defaultdate + "," + defaultdate + ",''," + defaultdate +
	 * "," + defaultdate + "," + defaultdate + "," + defaultdate + ",'" +
	 * makerid + "'," + makerdate + ",'" + checkerid + "'," + checkerdate + ",'"
	 * + mkckstatus + "','" + servicecode + "','" +
	 * extradetails.baselen_feecode.trim() + "'," + "'" +
	 * extradetails.chnbaseno_limitid.trim() + "','0','0','0','0','0','0','0','"
	 * + persorderdetails.embossing_name + "','" + persorderdetails.encode_data
	 * + "','','', '" + cardrefno + "','" + cardissuetype + "','" +
	 * customermobile + "','00','" + renewalflag + "','" + collectbranch + "')";
	 * enctrace("Insert Qury is : " + cardinsrt_qury);
	 * 
	 * String cardinsrt_qury1 =
	 * "INSERT INTO PERS_CARD_PROCESS_HASH (INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,CIN,CARD_TYPE_ID,"
	 * +
	 * "SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)  VALUES ('"
	 * + instid + "','" + persorderdetails.order_ref_no + "','" +
	 * persorderdetails.bin + "','" + hcardno.toString() + "','" + accountno +
	 * "' ,'" + persorderdetails.cin + "','" + persorderdetails.card_type_id +
	 * "','"+ persorderdetails.sub_prod_id + "','" +
	 * persorderdetails.product_code + "','" + persorderdetails.branch_code +
	 * "',SYSDATE,'" + makerid + "'," + makerdate + ", '" + checkerid +"', " +
	 * checkerdate + ")";
	 * 
	 * try { quryresult = commondesc.executeTransaction(cardinsrt_qury,
	 * jdbctemplate); quryresult1 =
	 * commondesc.executeTransaction1(cardinsrt_qury1, jdbctemplate); } catch
	 * (Exception e) { trace("Insert Qury is::" + e); e.printStackTrace(); }
	 * trace("Got : " + quryresult); if (quryresult == 1 && quryresult1==1 ) {
	 * cardcount++; sequn_no++; } else { cardcount = -1; trace(
	 * "CHN Insert Failed, Process Break"); isallcardprocessed = false; break; }
	 * 
	 *//************* AUDIT BLOCK **************/

	/*
	 * try {
	 * 
	 * auditbean.setActmsg("Card [ " + mcardno + " ] Generated For the Order [ "
	 * + persorderdetails.order_ref_no + " ]"); auditbean.setUsercode(username);
	 * auditbean.setAuditactcode("0102");
	 * auditbean.setProduct(persorderdetails.product_code);
	 * auditbean.setSubproduct(persorderdetails.sub_prod_id);
	 * auditbean.setBin(persorderdetails.bin);
	 * auditbean.setApplicationid(persorderdetails.order_ref_no);
	 * auditbean.setCardno(mcardno); // auditbean.setCardnumber(cardno);
	 * commondesc.insertAuditTrailPendingCommit(instid, username, auditbean,
	 * jdbctemplate, txManager); } catch (Exception audite) { trace(
	 * "Exception in auditran : " + audite.getMessage()); }
	 *//************* AUDIT BLOCK **************//*
												 * trace(
												 * "checking cardtoproccess count "
												 * + cardtoprocess); trace(
												 * "checking card count " +
												 * cardcount); if (cardtoprocess
												 * == cardcount) { trace(
												 * "All the Cards Generated Succesfully "
												 * ); String updateseq = "";
												 * 
												 * //// Changed For update last
												 * Sequance in Baseno table
												 * 
												 * if (branchattch.equals("Y"))
												 * { updateseq =
												 * "UPDATE BASENO SET CHN_BASE_NO='"
												 * + sequn_no +
												 * "' WHERE INST_ID='" + instid
												 * + "' AND BIN='" +
												 * persorderdetails.bin +
												 * "' AND BASENO_CODE='" +
												 * persorderdetails.branch_code
												 * + "' "; } else if
												 * (prodcardtype_attach.equals(
												 * "C")) { updateseq =
												 * "UPDATE BASENO SET CHN_BASE_NO='"
												 * + sequn_no +
												 * "' WHERE INST_ID='" + instid
												 * + "' AND BIN='" +
												 * persorderdetails.bin +
												 * "' AND BASENO_CODE='" +
												 * persorderdetails.card_type_id
												 * + "' "; } else if
												 * (prodcardtype_attach.equals(
												 * "P")) { updateseq =
												 * "UPDATE BASENO SET CHN_BASE_NO='"
												 * + sequn_no +
												 * "' WHERE INST_ID='" + instid
												 * + "' AND BIN='" +
												 * persorderdetails.bin +
												 * "' AND BASENO_CODE='" +
												 * persorderdetails.sub_prod_id
												 * + "' "; } else { updateseq =
												 * "UPDATE PRODUCTINFO SET CHN_BASE_NO='"
												 * + sequn_no +
												 * "' WHERE INST_ID='" + instid
												 * + "' AND BIN='" +
												 * persorderdetails.bin + "'"; }
												 * 
												 * 
												 * if(prodcardtype.equals("C")){
												 * 
												 * updateseq =
												 * "UPDATE BRANCH_MASTER SET \""
												 * +persorderdetails.
												 * card_type_id+
												 * "_CHN_BASE_NO\"='"+ sequn_no+
												 * "' WHERE INST_ID='"+instid+
												 * "' AND BRANCH_CODE='000'";
												 * }else{
												 * if(branchattch.equals("Y")){
												 * updateseq =
												 * "UPDATE BRANCH_MASTER SET \""
												 * +persorderdetails.
												 * card_type_id+
												 * "_CHN_BASE_NO\"='"+ sequn_no+
												 * "' WHERE INST_ID='"+instid+
												 * "' AND BRANCH_CODE='"
												 * +persorderdetails.branch_code
												 * +"'"; enctrace(
												 * "updateseq Qury is : "
												 * +updateseq); }else
												 * if(branchattch.equals("N")){
												 * updateseq =
												 * "UPDATE PRODUCTINFO SET CHN_BASE_NO='"
												 * +sequn_no+
												 * "' WHERE INST_ID='"+instid+
												 * "' AND BIN='"
												 * +persorderdetails.bin+"'"; }
												 * }
												 * 
												 * enctrace(" updateseq : " +
												 * updateseq); incseq =
												 * jdbctemplate.update(updateseq
												 * );
												 * 
												 * if (incseq > 0) { String
												 * updateorderstatus =
												 * "UPDATE PERS_CARD_ORDER SET ORDER_STATUS='02',MAKER_DATE=(sysdate),MAKER_TIME="
												 * + commondesc.def_time +
												 * ",MAKER_ID='" + userid +
												 * "' WHERE INST_ID='" + instid
												 * + "' AND CIN='" +
												 * order_refnum[i] + "'";
												 * enctrace(
												 * " Card Order Status QUry " +
												 * updateorderstatus); int order
												 * = jdbctemplate.update(
												 * updateorderstatus); trace(
												 * "Updating Card Order Status...got :"
												 * + order); if (order > 0) {
												 * trace(
												 * "Order Status Update successfully,order count "
												 * + order); isallcardprocessed
												 * = true;
												 * 
												 * } } else { addActionError(
												 * "Unable to continue the process"
												 * ); trace(
												 * "CHN BASE NO Update Failed,Txn got Rollback "
												 * ); isallcardprocessed =
												 * false; break; }
												 * 
												 * }
												 * 
												 * } // end for loop
												 * 
												 * trace(
												 * "End of Loop.... Boolean isallcardprocess : "
												 * + isallcardprocessed); if
												 * (!isallcardprocessed) {
												 * addActionError(
												 * "Unable to continue the process"
												 * );
												 * txManager.rollback(transact.
												 * status); trace(
												 * " Card Geneartion Process failed txn got roll back"
												 * );
												 * 
												 * } else {
												 * txManager.commit(transact.
												 * status);
												 * trace("Committed...");
												 * session.setAttribute(
												 * "prevmsg",
												 * order_refnum.length +
												 * " Card(s) Generated Successfully. "
												 * + authmsg);
												 * session.setAttribute(
												 * "preverr", "S"); }
												 * 
												 * 
												 * //---------------- edited by
												 * sardar on 11-12-15
												 * ---------// try{ for(int
												 * i=0;i<order_refnum.length;i++
												 * ) { trace("---------------"+
												 * (i+1) +
												 * " Card Processing..... Processing order [ customer id ] : "
												 * + order_refnum[i] );
												 * persorderdetails =
												 * commondesc.
												 * gettingOrderdetailsByCin(
												 * instid,order_refnum[i].trim()
												 * ,jdbctemplate);
												 * 
												 * try{ auditbean.setActmsg(
												 * "Card Generated For the Order [ "
												 * +persorderdetails.
												 * order_ref_no+" ]");
												 * auditbean.setUsercode(
												 * username);
												 * auditbean.setAuditactcode(
												 * "0102");
												 * auditbean.setProduct(
												 * persorderdetails.product_code
												 * ); auditbean.setSubproduct(
												 * persorderdetails.sub_prod_id)
												 * ; auditbean.setBin(
												 * persorderdetails.bin);
												 * auditbean.setApplicationid(
												 * persorderdetails.order_ref_no
												 * ); //
												 * auditbean.setCardno(mcardno);
												 * commondesc.
												 * insertAuditTrailPendingCommit
												 * (instid, username, auditbean,
												 * jdbctemplate, txManager);
												 * }catch(Exception audite ){
												 * trace(
												 * "Exception in auditran : "+
												 * audite.getMessage()); }
												 * 
												 * } } catch (Exception e) {
												 * trace(
												 * "Could not continue the process"
												 * + e.getMessage());
												 * e.printStackTrace(); if(
												 * !transact.status.isCompleted(
												 * ) ) {
												 * txManager.rollback(transact.
												 * status) ; } //addActionError(
												 * "Error While Process Card Geneartion"
												 * ); session.setAttribute(
												 * "prevmsg",
												 * "Unable To Continue The Process"
												 * ); session.setAttribute(
												 * "preverr","E"); }
												 * setAct((String)session.
												 * getAttribute("CARDGEN_ACT"));
												 * 
												 * 
												 * } catch (Exception e) {
												 * e.printStackTrace(); } return
												 * personalCardgenerationhome();
												 * 
												 * }
												 */
	// --------------------code is updated by sardar on 11-12-15 ended
	// -----------------------//

	/*
	 * public int deleteQuery(String qury){ int updateStop = -1; IfpTransObj
	 * transact = commondesc.myTranObject("DELORDER", txManager);
	 * System.out.println(
	 * "Deleteting The Branch Details inserted into CARDGEN STATUS TABLE"); try
	 * { int updated = jdbctemplate.update(qury); System.out.println(
	 * "Delete Status "+updated); if(updated == 1) {
	 * //commondesc.commitTxn(jdbctemplate); txManager.commit(transact.status);
	 * updateStop = 1; } } catch (Exception de) {
	 * //commondesc.rollbackTxn(jdbctemplate);
	 * txManager.rollback(transact.status); updateStop = 0; trace("Exception : "
	 * + de.getMessage()); }
	 * 
	 * return updateStop; }
	 */

	public String personalAuthcardgenerationhome() {
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();

		String temp = act;
		System.out.println(temp);
		session.setAttribute("CARDGEN_ACT", act);
		String session_act = (String) session.getAttribute("CARDGEN_ACT");
		String orderStatus = "01", mkrstatus = "M";
		System.out.println("session_act " + session_act);
		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, orderStatus, mkrstatus, jdbctemplate);
				// commondesc.generateBranchList(inst_id, jdbctemplate);

				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
				}

			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, orderStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Product Details Found ");
				trace(" No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Error While Fetching The Product Details  " + e.getMessage());
			trace("Exception : while fetching product details " + e.getMessage());
		}
		return "authcardgenrationhome";
	}

	private List personalcardauthlist;

	public List getPersonalcardauthlist() {
		return personalcardauthlist;
	}

	public void setPersonalcardauthlist(List personalcardauthlist) {
		this.personalcardauthlist = personalcardauthlist;
	}

	public String authCardgenorders() {
		HttpSession session = getRequest().getSession();

		try {
			String branch = getRequest().getParameter("branchcode");
			String cardtype = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String instid = comInstId();
			String cardstatus = "01";
			String mkckstatus = "M";
			List authcardorder = null;
			String dateflag = "GENERATED_DATE";
			trace("Getting order list...");
			String condition = commondesc.filterCondition(cardtype, branch, fromdate, todate, dateflag);
			trace("CONDITION ======== " + condition);
			authcardorder = commondesc.personaliseCardauthlist(instid, cardstatus, mkckstatus, condition, jdbctemplate);

			trace("authcardorder=====>" + authcardorder);
			if (authcardorder.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Orders Found");
				trace("No order found...");
				return personalAuthcardgenerationhome();
			} else {
				setPersonalcardauthlist(authcardorder);
				session.setAttribute("curerr", "S");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not continue the processsssssss....");
			trace("Exception : " + e.getMessage());
		}

		return "authcardgenorders";
	}

	public String cardGenerationauthorize() {
		trace("*************card generation authorization *****************");
		enctrace("*************card generation authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("CARDAUTH", txManager);
		String instid = comInstId();
		String usercode = comUserId();

		Personalizeorderdetails persorderdetails, bindetails, extradetails;
		String username = comUsername();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		String remarks = getRequest().getParameter("reason");
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		System.out.println("Total Orders Selected ===> " + order_refnum.length);

		trace("username is ======  "+username);
		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();

		// persorderdetails =
		// commondesc.gettingOrderdetailsByCin(instid,order_refnum[i].trim(),jdbctemplate);
		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			statusmsg = " Authorized Successfully and Waiting In Security Data Generation ...";
			err_msg = "Authorize";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			authstatus = "D";
			statusmsg = " De-Authorized Successfully ..";
			err_msg = "De-Authorize";

		}
		/*
		 * //-------------------------------------Aduit
		 * Trail-----------------------// try{ for(int
		 * j=0;j<order_refnum.length;j++) { trace("---------------"+ (j+1) +
		 * " Card Processing..... Processing order [ customer id ] : " +
		 * order_refnum[j] ); persorderdetails =
		 * commondesc.gettingOrderdetailsByCin(instid,order_refnum[j].trim(),
		 * jdbctemplate);
		 * 
		 * 
		 * try{ String mcardno = commondesc.getMaskedCardNo(instid,
		 * order_refnum[j],"C", jdbctemplate);
		 * if(mcardno==null){mcardno=order_refnum[j];} auditbean.setActmsg(
		 * "card genneration is "+statusmsg + "  Card Number [ "+mcardno+" ]");
		 * auditbean.setUsercode(username); auditbean.setAuditactcode("0102");
		 * auditbean.setCardno(mcardno); auditbean.setRemarks(remarks);
		 * //auditbean.setCardnumber(order_refnum[j]); //
		 * auditbean.setProduct(persorderdetails.product_code);
		 * //commondesc.insertAuditTrail(instid, userid, auditbean,
		 * jdbctemplate, txManager);
		 * commondesc.insertAuditTrailPendingCommit(instid, username, auditbean,
		 * jdbctemplate, txManager); }catch(Exception audite ){ trace(
		 * "Exception in auditran : "+ audite.getMessage()); }
		 * 
		 * 
		 * }
		 * 
		 * 
		 * } catch(Exception e){
		 * 
		 * }
		 * //-------------------------------------------------------------------
		 * -------------------------//
		 */
		try {

			int cnt = 0;

			System.out.println("order reference length---->  " + order_refnum.length);

			for (int i = 0; i < order_refnum.length; i++) {
				int check = 0;
				System.out.println("Selected Refnums ==>" + order_refnum[i]);

				/*
				 * String update_authdeauth_qury =
				 * "UPDATE PERS_CARD_PROCESS SET MKCK_STATUS='" + authstatus +
				 * "', REMARKS='" + remarks + "',  CHECKER_ID='" + usercode +
				 * "',CHECKER_DATE=(sysdate) WHERE INST_ID='" + instid +
				 * "' AND ORG_CHN='" + order_refnum[i].trim() + "'";
				 * System.out.println("update_authdeauth_qury======>" +
				 * update_authdeauth_qury);
				 * trace("update_authdeauth_qury======>"+update_authdeauth_qury)
				 * ; System.out.println("Before Update ===> " + cnt); check =
				 * jdbctemplate.update(update_authdeauth_qury);
				 * System.out.println(" QUery Executed ==check===>    " +
				 * check);
				 */

				/// by gowtham260819
				String update_authdeauth_qury = "UPDATE PERS_CARD_PROCESS SET MKCK_STATUS=?, REMARKS=?,  CHECKER_ID=?,CHECKER_DATE=(?) WHERE INST_ID=? AND ORG_CHN=?";
				System.out.println("update_authdeauth_qury======>" + update_authdeauth_qury);
				trace("update_authdeauth_qury======>" + update_authdeauth_qury);
				System.out.println("Before Update ===> " + cnt);
				check = jdbctemplate.update(update_authdeauth_qury, new Object[] { authstatus, remarks, usercode,
						date.getCurrentDate(), instid, order_refnum[i].trim() });

				if (check > 0) {
					cnt = cnt + 1;
					System.out.println("After Update ===> " + cnt);
				}

			//	System.out.println("chn --->  " + order_refnum[i]);
				/************* AUDIT BLOCK **************/
				try {
					String mcardno = commondesc.getMaskedCardNo(instid, order_refnum[i], "C", jdbctemplate);
					if (mcardno == null) {
						mcardno = order_refnum[i];
					}
					auditbean.setActmsg("card genneration is " + statusmsg + "  Card Number [ " + mcardno + " ]");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0102");
					auditbean.setCardno(mcardno);
					auditbean.setRemarks(remarks);
					// auditbean.setProduct(persorderdetails.product_code);
					// commondesc.insertAuditTrail(instid, userid, auditbean,
					// jdbctemplate, txManager);

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);
				//	auditbean.setApplicationid(order_refnum[i]);

					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			}
			if (order_refnum.length == cnt) {
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", cnt + "  Order " + statusmsg);
				txManager.commit(transact.status);
				System.out.println(" Committed success ");
			} else {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Selected Orders Not " + err_msg + " Successfully");
				txManager.rollback(transact.status);
				System.out.println(" Rollback success ");
			}

		} catch (Exception e) {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error While" + err_msg + " The Orders " + e.getMessage());
			trace("Exception : while process the card generation authorization : " + e.getMessage());
		}
		/*
		 * DefaultTransactionDefinition def = new
		 * DefaultTransactionDefinition();
		 * def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED
		 * ); TransactionStatus status = txManager.getTransaction(def); try{
		 * String columns = "", condition = "", table = "PERS_CARD_PROCESS",
		 * result = ""; Connection conn = null; Dbcon dbcon = new Dbcon(); conn
		 * = dbcon.getDBConnection(); CallableStatement cstmt = null;
		 * System.out.println("Get Data base connection"+conn); cstmt =
		 * conn.prepareCall("call SP_COMMON_UPDATE_1(?,?,?,?,?,?)"); trace(
		 * "procedure--->call SP_COMMON_UPDATE_1(?,?,?,?,?)"); ArrayDescriptor
		 * arrDesc = ArrayDescriptor.createDescriptor("TEST1", conn);
		 * System.out.println("check"); Array array = null; try{ array = new
		 * ARRAY(arrDesc, conn, order_refnum); } catch (Exception e) {
		 * e.printStackTrace(); } trace("proc args-->"
		 * +array+"--"+instid+"--"+usercode); cstmt.setString(1, table);
		 * cstmt.setArray(2, array); cstmt.setString(3, instid); columns =
		 * "  MKCK_STATUS='"+authstatus+"', REMARKS='"+remarks+
		 * "',  CHECKER_ID='"+usercode+"',CHECKER_DATE=(sysdate)"; condition =
		 * " WHERE INST_ID='"+instid+"' AND CARD_NO IN"; cstmt.setString(4,
		 * columns); cstmt.setString(5, condition);
		 * cstmt.registerOutParameter(6,java.sql.Types.VARCHAR);
		 * cstmt.execute(); result=cstmt.getString(6);
		 * trace("result--->"+result);
		 * 
		 *//************* AUDIT BLOCK **************/
		/*
		 * try{ auditbean.setActmsg("Card  [ "+order_refnum.length+
		 * " ] Authorized Successfully "); auditbean.setActiontype("IC");
		 * auditbean.setUsercode(usercode); auditbean.setAuditactcode("0102");
		 * commondesc.insertAuditTrail(instid, usercode, auditbean,
		 * jdbctemplate, txManager); }catch(Exception audite ){ trace(
		 * "Exception in auditran : "+ audite.getMessage()); }
		 *//************* AUDIT BLOCK **************//*
													 * 
													 * 
													 * if(result.contains(
													 * "successfully")){
													 * addActionMessage("Cards "
													 * +result+" Authorized .");
													 * }else{ addActionError(
													 * "unable to continue the process"
													 * ); }
													 * 
													 * } catch (Exception e) {
													 * e.printStackTrace();
													 * session.setAttribute(
													 * "preverr", "E");
													 * session.setAttribute(
													 * "prevmsg",
													 * " Exception: Unable to continue the process "
													 * ); addActionError(
													 * "Unable to continue the process !!!"
													 * ); txManager.rollback(
													 * status ); trace(
													 * "Could not insert the order details "
													 * + e.getMessage());
													 * 
													 * }
													 */

		setAct((String) session.getAttribute("CARDGEN_ACT"));
		return personalAuthcardgenerationhome();
	}

	public void removeProcessing() {
		String instid = comInstId();
		String usercode = comUserId();
		IfpTransObj transact = commondesc.myTranObject("REMOVEPROCESS", txManager);
		try {
			removeProcessingAction(instid, usercode, jdbctemplate);
		} catch (Exception e) {
			trace("Could not remove processing...." + e.getMessage());
			e.printStackTrace();
		} finally {
			// commondesc.commitTxn(jdbctemplate);
			txManager.rollback(transact.status);
		}
	}

	public int removeProcessingAction(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		/*
		 * String deleteprocessqry =
		 * "DELETE FROM CARDGEN_STATUS WHERE INST_ID='" + instid +
		 * "' AND USER_ID='" + usercode + "' "; enctrace("deleteprocessqry :" +
		 * deleteprocessqry); x = jdbctemplate.update(deleteprocessqry);
		 */

		// by gowtham-260819
		String deleteprocessqry = "DELETE FROM CARDGEN_STATUS WHERE INST_ID=? AND USER_ID=? ";
		enctrace("deleteprocessqry :" + deleteprocessqry);
		x = jdbctemplate.update(deleteprocessqry, new Object[] { instid, usercode });
		return x;
	}

	// ***************************************************PERSONALIZE
	// REISSUE-PAVITHRA ***************************************
	public String reissueCardgenerationhome() {
		List pers_prodlist = null, br_list = null;
		trace("*********** Personalization card generation ************ ");
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();

		session.setAttribute("CARDGEN_ACT", act);
		String session_act = (String) session.getAttribute("CARDGEN_ACT");
		System.out.println("session_act  -> " + session_act);
		trace("User type is : " + usertype);
		try {

			if (usertype.equals("INSTADMIN")) {
				trace("Getting branchlist....");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate);

				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
				} else {
					setBranchlist(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", " No Branch Details Found ");
					System.out.println("Branch List is empty ");
					trace("Branch list is empty....");
					return "personalproducts";
				}
			}
			trace("Getting product list....");
			pers_prodlist = commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Product Details Found ");
				trace("No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: Error While Fetching The Product Details");
			trace("Exception : While Fetching The Product Details" + e.getMessage());
			e.printStackTrace();

		}
		return "reissue_cardgeneration";
	}

	public String getPersonalreissuecard() {
		HttpSession session = getRequest().getSession();
		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String dateflag = "REISSUE_DATE";
		String inst_id = comInstId();
		List authorizeorderlist = null;
		String session_act = (String) session.getAttribute("CARDGEN_ACT");

		System.out.println("session_act " + session_act);
		try {

			trace("Checking generation status...");
			int cardgen = commondesc.checkCardgenerationstatus(inst_id, branch, jdbctemplate);
			trace("Got : " + cardgen);
			if (cardgen > 0) {
				setAct((String) session.getAttribute("CARDGEN_ACT"));
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",
						"Another User Processing Card Generation, Please Wait....<a href='removeProcessingPersonalCardprocess.do'> Remove </a> ");
				trace("Another User Processing Card Generation, Please Wait.... ");
				return reissueCardgenerationhome();
			}

			String condition1 = " AND CARD_STATUS='07' AND MKCK_STATUS='P' AND CAF_REC_STATUS='S'";
			String condtion = condition1 + commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			System.out.println("condtion==>" + condtion);
			authorizeorderlist = commondesc.getPersonalReissueList(inst_id, condtion, jdbctemplate);
			System.out.println("authorizeorderlist===> " + authorizeorderlist);
			if (!(authorizeorderlist.isEmpty())) {
				setPerscardgenlist(authorizeorderlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");

			} else {
				setPerscardgenlist(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No Orders To Generate Card");
				return reissueCardgenerationhome();

			}
		} catch (Exception e) {
			System.out.println("Exception--->" + e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Error while getting the cards....");
			trace("Error While Fetching The Orders To Card Genetaion " + e.getMessage());

		}
		return "personalcardgenorders";
	}

}
