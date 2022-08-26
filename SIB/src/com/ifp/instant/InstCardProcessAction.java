package com.ifp.instant;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

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
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import connection.Dbcon;

/**
 * SRNP0004
 * 
 * @author CGSPL
 *
 */
public class InstCardProcessAction extends BaseAction {

	List prodlist;
	List branchlist;
	List instorderlist;

	private static Boolean initmail = true;
	private static String parentid = "000";

	CommonUtil comutil = new CommonUtil();
	CommonDesc commondesc = new CommonDesc();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

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

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public List getInstorderlist() {
		return instorderlist;
	}

	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	}

	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public List getProdlist() {
		return prodlist;
	}

	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}

	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
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
		HttpSession sessiosn = getRequest().getSession();
		String username = (String) session.getAttribute("USERNAME");
		return username;
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

	public String cardGenHome() {
		trace("******* Card generation home *******\n");
		enctrace("******* Card generation home  *******\n");

		String instid = comInstId();
		String act = getRequest().getParameter("act");
		System.out.println("act-->"+act);
		HttpSession session = getRequest().getSession();
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		String orderstatus = "01";
		String mkrstatus = "P";

		/*
		 * int x= commondesc.reqCheck().requiredCheck(instid, session,
		 * jdbctemplate); if( x < 0 ){ return "required_home"; }
		 */
		if (act != null) {
			session.setAttribute("act", act);
		}
		try {

			/*
			 * prodlist = commondesc.getProductList( instid, jdbctemplate,
			 * session); setProdlist(prodlist);
			 * 
			 * branchlist = this.commondesc.generateBranchList(instid,
			 * jdbctemplate); setBranchlist(branchlist);
			 * session.setAttribute("curerr", "S");
			 */

			int x = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if (x < 0) {
				return "required_home";
			}

			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.Instgetorderbybranch(inst_id, orderstatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					// setCardgenstatus('Y');
				} else {
					addActionError("No Cards Waiting For Card Issuance ... ");
					System.out.println("Branch List is empty ");
					return "required_home";

				}
			}
			pers_prodlist = commondesc.InstgetorderbyProductList(inst_id, orderstatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setProdlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				System.out.println("Product List is ===> " + pers_prodlist);
				// setCardgenstatus('Y');
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Product Details Found ");
				// setCardgenstatus('N');
			}

			/*** MAIL BLOCK ****/
			System.out.println("initmail--" + initmail + " parentid :  " + this.parentid);
			if (initmail) {
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId(req, jdbctemplate);
				if (!menuid.equals("NOREC")) {
					System.out.println("parentid--" + menuid);
					this.parentid = menuid;
				} else {
					this.parentid = "000";
				}
				initmail = false;
			}
			/*** MAIL BLOCK ****/

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Unable to continue the process");
			trace("Error while execute the instantOrder" + e.getMessage());
		}
		return "instcardgen_home";
	}

	public String viewProcessedOrder() throws Exception {
		trace("******* View process order *******\n");
		enctrace("******* View process order *******\n");

		HttpSession session = getRequest().getSession();

		String instid = comInstId();

		String datefld = "ORDERED_DATE";
		String branch = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		String[] bin = cardtype.split("~");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");

		String filtercond = commondesc.filterCondition(bin[0], branch, fromdate, todate, datefld);

		List waitingforcardgen = this.commondesc.waitingForInstCardGen(instid, filtercond, jdbctemplate);
		setInstorderlist(waitingforcardgen);

		if (waitingforcardgen.isEmpty()) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " No records found ");
			trace(" No records found ");
			return this.cardGenHome();
		}

		return "instcardgen_list";

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateInstCard() throws Exception {
		trace("******* Generate instant card *******\n");
		enctrace("******* Generate instant card *******\n");
	 
		
		HttpSession ses = getRequest().getSession();
		/*String sessioncsrftoken = (String) ses.getAttribute("csrfToken");
		System.out.println("TTTTTTTTTTTTTT"+sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("jspcsrftoken----->    "+jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			ses.setAttribute("message", "CSRF Token Mismatch");
			 addActionError("CSRF Token Mismatch"); 
			return "invaliduser";
		}*/
		

		String cardno = null;

		String bin = null;
		StringBuilder sb = null;
		String checkerid = null;
		String makerid = null;
		String mkckflag = null;
		String ckdate = null;

		
		String username = comUserCode();
		//String username = comUsername();
		 System.out.println("username-->"+username);
		String authmsg = null;
		String keyid = null;
		String mcardno = null;
		String actiontypye = null;
		StringBuffer hcardno = new StringBuffer();
		String ecardno = null;
		PadssSecurity padsssec = null;
		String EDPK = null;
		List secList = null;

		Personalizeorderdetails instantorderdetails = null;
		Personalizeorderdetails bindetails = null;
		Personalizeorderdetails extradetails = null;

	
		IfpTransObj transact = commondesc.myTranObject("INSTCARDGEN", txManager);

		String instid = comInstId();
		String usercode = comUserCode();
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		trace("order_refnum----->>"+order_refnum);
		// String order_refnum = getRequest().getParameter("instorderrefnum");
		System.out.println("instid------>"+instid);
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		System.out.println("padss--->"+padssenable);
		String productcode = getRequest().getParameter("productcode");
		//System.out.println("productcode--->"+productcode);
		//added by gowtham_220719
		
		String  ip=(String) ses.getAttribute("REMOTE_IP");
		System.out.println("IP--->"+ip);
		int incseq=0;
		try {
		
		//Date date =  new Date();
	
		
		
			System.out.println("qqqqqqqqqqqqqqq");
			bin = commondesc.getBin(instid, productcode, jdbctemplate);
			trace("bin----->"+bin);
		} catch (Exception e1) {
			trace("Exception : while getteing bin : " + e1.getMessage());
			e1.printStackTrace();
		}

		if (padssenable.equals("Y")) {
			System.out.println("tttttttttttttttt");
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			Properties props = getCommonDescProperty();
			EDPK = props.getProperty("EDPK");
		}

		bindetails = commondesc.gettingBindetails(instid, bin, jdbctemplate);
		if (bindetails == null) {

			session.setAttribute("prevmsg", "  No Records found for the bin " + bin);
			session.setAttribute("preverr", "E");
			return this.cardGenHome();
		}

		String act = (String) ses.getAttribute("act");
		String default_date = commondesc.default_date_query;
		trace("checking the session value act " + act);
		if (act.equals("M")) {
			makerid = usercode;
			checkerid = usercode;
			actiontypye = "IM";
			mkckflag = "M";
			ckdate = commondesc.default_date_query;
			authmsg = " Waiting For Authorization ";
		} else { // D
			actiontypye = "ID";
			makerid = usercode;
			checkerid = usercode;
			mkckflag = "P";
			ckdate = "sysdate";
			authmsg = "";
		}

		trace("bindetails" + bindetails.chnlen_glcode + " Attch prod card==> " + bindetails.prodcard_expiry
				+ " attche branch==>" + bindetails.brcode_servicecode + " BaseLen " + bindetails.baselen_feecode
				+ " Cards Count " + bindetails.cardscount_cardccy + " Chn Base No===>" + bindetails.chnbaseno_limitid);

		Boolean isallcardprocessed = true;
		int cardcount = 0;
		int cardtoprocesscnt = order_refnum.length;
		trace("cardtoprocesscnt---->>"+cardtoprocesscnt);
		trace("Getting branch attached....");
		// String branchattch =
		// commondesc.checkBranchattached(instid,jdbctemplate);
		// trace("branchattch : " + branchattch );
		// trace("Got instant card genearaion order count : " + order_refnum );
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
		try {
			System.out.println("Legth of order ref value " + order_refnum.length);

			for (int i = 0; i < order_refnum.length; i++) {

				padsssec = new PadssSecurity();

				trace("Generating order : " + order_refnum[i].trim());
				instantorderdetails = commondesc.gettingInstantorderDeatils(instid, order_refnum[i], jdbctemplate);

				extradetails = commondesc.getCcyexpiry(instid, instantorderdetails.bin,
						instantorderdetails.card_type_id, instantorderdetails.sub_prod_id,
						instantorderdetails.product_code, jdbctemplate);

				trace("getting extra details " + extradetails + "\n branchcodecc" + instantorderdetails.branch_code);
				String breakupvalue = commondesc.getChnbreakupvalues(instid, bindetails.prodcard_expiry,
						bindetails.brcode_servicecode, instantorderdetails.card_type_id,
						instantorderdetails.sub_prod_id, instantorderdetails.product_code,
						instantorderdetails.branch_code, bindetails.apptypelen, bindetails.apptypevalue);

				int expprd = Integer.parseInt(extradetails.prodcard_expiry);
				String expirydate = "add_months(sysdate," + expprd + ")";
				String newchn = instantorderdetails.bin + breakupvalue;
				trace("bin and breakupvalue" + instantorderdetails.bin + "adsadsa" + breakupvalue);
				trace("newchn :: " + newchn);

				// String sequncenumber =
				// commondesc.gettingSequnceNumber(instid,instantorderdetails.bin,instantorderdetails.branch_code,bindetails.baselen_feecode,instantorderdetails.card_type_id,jdbctemplate,branchattch);

				String sequncenumber = commondesc.gettingSequnceNumberNew(instid, instantorderdetails.bin,
						instantorderdetails.branch_code, bindetails.baselen_feecode, instantorderdetails.card_type_id,
						instantorderdetails.sub_prod_id, jdbctemplate, branchattch, prodcardtype_attach);

				long sequn_no = Long.parseLong(sequncenumber);

				trace("Getting card reference number length ....");
				String cardrefnulen = commondesc.getCardReferenceNumberLen(instid, jdbctemplate);
				trace("Got : " + cardrefnulen);
				if (cardrefnulen == null) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",
							"Could not generate card... Card Referene number length is empty...");
					trace("Could not generate card... Card Referene number length is empty...");
					return this.cardGenHome();
				}
				/// start
				/*
				 * for( int card=1;card<=cardtoprocesscnt;card++ ) {
				 */

				String pcflag = "C";
				int quryresult = 0;
				int quryresult1 = 0;
				String strseq = Long.toString(sequn_no);
				
				System.out.println("newchn--->"+newchn+"strseq-->"+strseq);
				
				cardno = commondesc.generateCHN(newchn, strseq, Integer.parseInt(bindetails.baselen_feecode),
						Integer.parseInt(bindetails.chnlen_glcode));
				
			
				
				mcardno = padsssec.getMakedCardno(cardno);

				if (padssenable.equals("Y")) {
					Iterator secitr = secList.iterator();
					if (!secList.isEmpty()) {
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							String CDMK = ((String) map.get("DMK"));
							// String eDPK = ((String) map.get("DPK"));
							String CDPK = padsssec.decryptDPK(CDMK, EDPK);
							hcardno = padsssec.getHashedValue(cardno + instid);
							ecardno = padsssec.getECHN(CDPK, cardno);
						}
					}
				}

				trace("ecardno is ------->  "+ecardno);
			

				if (cardno.equals("N")) {

					session.setAttribute("prevmsg", "Card Generation Failed");
					session.setAttribute("preverr", "E");
					cardcount = -2;
					break;
				}

				if (cardno.equals("M")) {

					session.setAttribute("prevmsg", " Card Number Sequnce Reached Maximum Length ");
					trace(" Card Number Sequnce Reached Maximum Length ");
					session.setAttribute("preverr", "E");
					cardcount = -3;
					break;
				}
				
				
			/*	// nullify
				cardno = "0000000000000000";
				cardno = null;
				sb=new StringBuilder();
				sb.append(cardno);
				sb.setLength(0);
				sb=null;*/

				trace("Generating card reference number....");
				String cardrefno = commondesc.generateCardRefNumber(instid, instantorderdetails.sub_prod_id,
						cardrefnulen, jdbctemplate);
				trace("Got cardrefno : " + cardrefno);
				if (cardrefno == null) {
					txManager.rollback(transact.status);
					ses.setAttribute("preverr", "E");
					ses.setAttribute("prevmsg", "Could not generate card... Got Card Referene number  is empty...");
					trace("Could not generate card... Card Referene number length is empty...");
					return this.cardGenHome();
				}

				//trace("generating card " + order_refnum[i]);
				String encname = commondesc.getEncEmbName(instid, order_refnum[i], jdbctemplate);
				String CIN = "", STATUS_CODE = "", PC_FLAG = "P", ORDER_FLAG = "I", USED_CHN = "", APP_NO = "",
						PRE_FILE = "", COURIER_ID = "", PRIVILEGE_CODE = "";
				trace("Inserting into db....");

				
				/*
				 * Connection connection =
				 * jdbctemplate.getDataSource().getConnection();
				 * CallableStatement callableStatement = connection.prepareCall(
				 * "{ call testprocebysardar(?, ?, ?)}");
				 * 
				 * callableStatement.setString(1,instid);
				 * callableStatement.setString(2, " sardar");
				 * callableStatement.registerOutParameter(3,3 );
				 * callableStatement.executeUpdate(); trace("cfdfdfd : " +
				 * callableStatement.executeUpdate());
				 */

				/*
				 * String cardinsrt_qury="INSERT INTO INST_CARD_PROCESS" +
				 * "(INST_ID,ORDER_REF_NO,BIN,CARD_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,"
				 * +
				 * "CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,"
				 * +
				 * "ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,"
				 * +
				 * "WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE, CARD_REF_NO,HCARD_NO,MCARD_NO,CARDISSUETYPE ) VALUES"
				 * + "('"+instid+"','"+order_refnum[i]+"','"+bin+"','"+ecardno+
				 * "','"+CIN+"','"+instantorderdetails.card_type_id+"','"+
				 * instantorderdetails.sub_prod_id+"','"+instantorderdetails.
				 * product_code+"','"+instantorderdetails.branch_code+
				 * "','01','A','"+STATUS_CODE+"'" +
				 * ",'"+extradetails.cardscount_cardccy+"','"+PC_FLAG+"','"+
				 * ORDER_FLAG+"','"+cardno+"','"+cardno+"',sysdate,"+expirydate+
				 * ",'"+APP_NO+"',"+default_date+","+default_date+","+
				 * default_date+",'"+PRE_FILE+"',"+default_date+","+
				 * default_date+ ","+default_date+","+default_date+",'"+makerid+
				 * "',(sysdate),'"+checkerid+"',"+ckdate+",'"+mkckflag+"','"+
				 * extradetails.brcode_servicecode+"','"+extradetails.
				 * baselen_feecode+"','"+extradetails.chnbaseno_limitid+"','"+
				 * PRIVILEGE_CODE+"','0'," +
				 * "'0','0','0','0','0','"+encname+"','"+encname+"','"+
				 * COURIER_ID+"',"+ckdate+", '"
				 * +cardrefno+"','"+hcardno+"','"+mcardno+"','P')";
				 */

				/*// added by prasad
				String cardinsrt_qury = "INSERT INTO INST_CARD_PROCESS"
						+ "(INST_ID,ORDER_REF_NO,BIN,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,"
						+ "CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,"
						+ "ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,"
						+ "WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE, CARD_REF_NO,MCARD_NO,CARDISSUETYPE ) VALUES"
						+ "('" + instid + "','" + order_refnum[i] + "','" + bin + "','" + CIN + "','"
						+ instantorderdetails.card_type_id + "','" + instantorderdetails.sub_prod_id + "','"
						+ instantorderdetails.product_code + "','" + instantorderdetails.branch_code + "','01','A','"
						+ STATUS_CODE + "'" + ",'" + extradetails.cardscount_cardccy + "','" + PC_FLAG + "','"
						+ ORDER_FLAG + "','" + ecardno + "','" + ecardno + "',sysdate," + expirydate + ",'" + APP_NO
						+ "'," + default_date + "," + default_date + "," + default_date + ",'" + PRE_FILE + "',"
						+ default_date + "," + default_date + "," + default_date + "," + default_date + ",'" + makerid
						+ "',(sysdate),'" + checkerid + "'," + ckdate + ",'" + mkckflag + "','"
						+ extradetails.brcode_servicecode + "','" + extradetails.baselen_feecode + "','"
						+ extradetails.chnbaseno_limitid + "','" + PRIVILEGE_CODE + "','0'," + "'0','0','0','0','0','"
						+ encname + "','" + encname + "','" + COURIER_ID + "'," + ckdate + ", '" + cardrefno + "','"
						+ mcardno + "','P')";

				String cardinsrt_qury1 = "INSERT INTO INST_CARD_PROCESS_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE) VALUES"
						+ "('" + instid + "','" + order_refnum[i] + "','" + bin + "','" + hcardno + "','" + CIN + "','"
						+ instantorderdetails.card_type_id + "','" + instantorderdetails.sub_prod_id + "','"
						+ instantorderdetails.product_code + "','" + instantorderdetails.branch_code + "',sysdate"
						+ ",'" + makerid + "',(sysdate),'" + checkerid + "'," + ckdate + ")";

				enctrace("cardgenquery" + cardinsrt_qury);
				enctrace("cardinsrt_qury1" + cardinsrt_qury1);

				try {
					quryresult = commondesc.executeTransaction(cardinsrt_qury, jdbctemplate);
					quryresult1 = commondesc.executeTransaction1(cardinsrt_qury1, jdbctemplate);
				} catch (Exception e) {
					e.printStackTrace();
					trace("Insert Qury is::" + e);
				}*/
				
				
				
				
				////by gowtham-300819
				

				// added by prasad
				String cardinsrt_qury = "INSERT INTO INST_CARD_PROCESS"
				+ "(INST_ID,ORDER_REF_NO,BIN,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,"
				+ "CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,"
				+ "ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,"
				+ "WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE, CARD_REF_NO,MCARD_NO,CARDISSUETYPE ) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate," + expirydate + ",?," + default_date + "," + default_date + "," + default_date + ",?,"
				+ default_date + "," + default_date + "," + default_date + "," + default_date + ",?,(sysdate),?," + ckdate + ",?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?," + ckdate + ", ?,?,?)";
		

				String cardinsrt_qury1 = "INSERT INTO INST_CARD_PROCESS_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,sysdate,?,(sysdate),?," + ckdate + ")";

				enctrace("cardgenquery" + cardinsrt_qury);
				enctrace("cardinsrt_qury1" + cardinsrt_qury1);
                System.out.println("mcardno-->"+mcardno);
				quryresult =  jdbctemplate.update(cardinsrt_qury,new Object[]{
				instid,order_refnum[i],bin,CIN,instantorderdetails.card_type_id,instantorderdetails.sub_prod_id ,instantorderdetails.product_code,
				instantorderdetails.branch_code,"01","A",STATUS_CODE,extradetails.cardscount_cardccy,PC_FLAG,
				ORDER_FLAG,ecardno,ecardno,APP_NO,PRE_FILE, makerid,checkerid,mkckflag,
				extradetails.brcode_servicecode,extradetails.baselen_feecode,extradetails.chnbaseno_limitid,
				PRIVILEGE_CODE,"0","0","0","0","0","0",encname,encname,COURIER_ID,cardrefno,mcardno,"P"});
				
				quryresult1 =  jdbctemplate.update(cardinsrt_qury1,new Object[]{instid,order_refnum[i],bin,hcardno,CIN,instantorderdetails.card_type_id,instantorderdetails.sub_prod_id,
				instantorderdetails.product_code,instantorderdetails.branch_code,makerid,checkerid});
		
		
			
		
		////modified by gowtham-300819
				
				
				
				
				
				
				trace("Got : " + quryresult);
				if (quryresult > 0 && quryresult1 > 0) {
					cardcount++;
					sequn_no++;
				} else {
					cardcount = -1;
					trace("CHN Insert Failed, Process Break");
					isallcardprocessed = false;
					break;
				}

				trace("checking card rpocess count " + cardtoprocesscnt + "\n checking card count" + cardcount);

				if (cardtoprocesscnt > 0) {
					trace("All the Cards Generated Succesfully ");
					String updateseq = "";

					//// Changed For update last Sequance in Baseno table

				/*	
					
					if (branchattch.equals("Y")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
								+ "' AND BIN='" + instantorderdetails.bin + "' AND BASENO_CODE='"
								+ instantorderdetails.branch_code + "' ";
					} else if (prodcardtype_attach.equals("C")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
								+ "' AND BIN='" + instantorderdetails.bin + "' AND BASENO_CODE='"
								+ instantorderdetails.card_type_id + "' ";
					} else if (prodcardtype_attach.equals("P")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
								+ "' AND BIN='" + instantorderdetails.bin + "' AND BASENO_CODE='"
								+ instantorderdetails.sub_prod_id + "' ";
					} else {
						updateseq = "UPDATE PRODUCTINFO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
								+ "' AND BIN='" + instantorderdetails.bin + "'";
					}
					*/
					
					
				////by gowtham-300819

					if (branchattch.equals("Y")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? "+ "AND BASENO_CODE=? ";
						
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,instantorderdetails.bin,instantorderdetails.branch_code});
						
					} else if (prodcardtype_attach.equals("C")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
						
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,instantorderdetails.bin,instantorderdetails.card_type_id});
						
					} else if (prodcardtype_attach.equals("P")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
						
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,instantorderdetails.bin,instantorderdetails.sub_prod_id});
						
					} else {
						updateseq = "UPDATE PRODUCTINFO SET CHN_BASE_NO=? WHERE INST_ID=? AND PRD_CODE=? ";
						
						enctrace(" updateseq : " + updateseq);
						incseq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,instantorderdetails.bin });
					}
					
					
					

					/*
					 * if(branchattch.equals("Y")){ updateseq =
					 * "UPDATE IFD_BRANCH_MASTER SET \""
					 * +instantorderdetails.card_type_id+"_CHN_BASE_NO\"='"+
					 * sequn_no+"' WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"
					 * +instantorderdetails.branch_code+"'"; enctrace(
					 * "updateseq Qury is : "+updateseq); }else
					 * if(branchattch.equals("N")){ updateseq =
					 * "UPDATE IFD_PRODUCTINFO SET CHN_BASE_NO='"+sequn_no+
					 * "' WHERE INST_ID='"+instid+"' AND BIN='"
					 * +instantorderdetails.bin+"'"; }
					 */

					enctrace(" updateseq : " + updateseq);
					
					/*incseq = jdbctemplate.update(updateseq);*/
					
					if (incseq == 1) {
						
						/*String updateorderstatus = "UPDATE INST_CARD_ORDER SET ORDER_STATUS='02',MAKER_DATE=sysdate, MAKER_ID='"
								+ usercode + "' WHERE INST_ID='" + instid + "' AND ORDER_REF_NO='" + order_refnum[i]
								+ "'";
						enctrace(" Card Order Status QUry " + updateorderstatus);
						int order = jdbctemplate.update(updateorderstatus);*/
						
						
						//by gowtham-270819
						String updateorderstatus = "UPDATE INST_CARD_ORDER SET ORDER_STATUS=?,MAKER_DATE=sysdate, MAKER_ID=? WHERE INST_ID=? AND ORDER_REF_NO=? ";
						enctrace(" Card Order Status QUry " + updateorderstatus);
						int order = jdbctemplate.update(updateorderstatus,new Object[]{"02",usercode,instid,order_refnum[i]});
						
						
						trace("Updating Card Order Status...got :" + order);
						if (order == 0) {
							addActionError("Unable to continue the process");
							trace("Order Status Update Failed, Txn Got Rollback");
							isallcardprocessed = false;
							break;
						}
					} else {
						addActionError("Unable to continue the process");
						trace("CHN BASE NO Update Failed,Txn got Rollback ");
						isallcardprocessed = false;
						break;
					}
				}

				/************* AUDIT BLOCK **************/
				try {

					//added by gowtham_220719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);

					
					auditbean.setActmsg("Card [ " + mcardno + " ] Generated For the Order [ "
							+ instantorderdetails.order_ref_no + " ]");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0202");
					auditbean.setActiontype(actiontypye);
					auditbean.setCardcollectbranch(instantorderdetails.branch_code);
					
					//added by gowtham_010819
					
					 String product_code=null;
					//auditbean.setProduct(instantorderdetails.product_code);
					//auditbean.setProduct(instantorderdetails.product_code);
					auditbean.setSubproduct(instantorderdetails.sub_prod_id);
					auditbean.setBin(instantorderdetails.bin);
					auditbean.setApplicationid(instantorderdetails.order_ref_no);
					auditbean.setCardno(mcardno);
					// auditbean.setCardnumber(cardno);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/
			} // end for loop

			if (cardtoprocesscnt == cardcount) {
				txManager.commit(transact.status);
				trace("Committed...for " + cardcount);
				ses.setAttribute("prevmsg", order_refnum.length + " Card(s) Generated Successfully. " + authmsg);
				ses.setAttribute("preverr", "S");
			} else {
				isallcardprocessed = false;
				trace(" Card Geneartion Process failed txn got roll back ,,,End of Loop.... Boolean isallcardprocess : "
						+ isallcardprocessed);
				addActionError("Unable to continue the process");
				txManager.rollback(transact.status);
				// trace(" Card Geneartion Process failed txn got roll back");

			}

		} catch (Exception e) {
			txManager.rollback(transact.status);
			// gensuc = false;
			ses.setAttribute("preverr", "E");
			ses.setAttribute("prevmsg", "Exception : Could not generate cards...");
			trace("Exception : Could not generate cards..." + e.getMessage());
			e.printStackTrace();
			// break;
		} finally {

			// added by Prasad 10-07-2019
			// Nullify the Objects

			cardno = "0000000000000000";
			cardno = null;
			sb = null;
			
			bin = null;
			checkerid = null;
			makerid = null;
			mkckflag = null;
			ckdate = null;

			//username = comUsername();
			authmsg = null;
			keyid = null;
			mcardno = null;
			actiontypye = null;
			hcardno = null;
			ecardno = null;
			padsssec = null;
			EDPK = null;
			secList = null;

			instantorderdetails = null;
			bindetails = null;
			extradetails = null;
			
			trace("after buffer values is ====> " + sb);

		}

		// ordcnt++;
		/*
		 * } /*if( gensuc ) { transact.txManager.commit(transact.status);
		 * session.setAttribute("preverr", "S"); int selectedorder =
		 * order_refnum.length; session.setAttribute("prevmsg", ordcnt+
		 * " Order(s) cards generated succesfully. "); trace(ordcnt+
		 * " Order(s) cards generated succesfully.Got commtted ");
		 *//*** MAIL BLOCK ****/
		/*
		 * IfpTransObj transactmail = commondesc.myTranObject(); try { String
		 * alertid = this.parentid; if( alertid != null && !
		 * alertid.equals("000")){ String keymsg =
		 * "Instant card generated for  " + ordcnt + " Order(s)"; int mail =
		 * comutil.sendMail( instid, alertid, keymsg, jdbctemplate, session,
		 * getMailSender() ); System.out.println( "mail return__" + mail); } }
		 * catch (Exception e) { trace("Exception : " + e.getMessage() );
		 * e.printStackTrace(); } finally{
		 * transactmail.txManager.commit(transactmail.status);
		 * System.out.println( "mail commit successfully"); }
		 *//*** MAIL BLOCK ****//*
								 * 
								 * 
								 * trace(
								 * " Order status updated txn got commited");
								 * return this.cardGenHome(); }else{ if(
								 * !transact.status.isCompleted() ) {
								 * transact.txManager.rollback(transact.status)
								 * ; } return this.cardGenHome(); }
								 */

		return this.cardGenHome();
	}

	// by siva 18-07-2019

	/*
	 * public String generateInstCard() throws Exception { trace(
	 * "******* Generate instant card *******\n"); enctrace(
	 * "******* Generate instant card *******\n");
	 * 
	 * HttpSession session = getRequest().getSession(); IfpTransObj transact =
	 * commondesc.myTranObject("INSTCARDGEN", txManager);
	 * 
	 * int cnt = 0; String cardno = "0";
	 * 
	 * String instid = comInstId(); String usercode = comUserCode(); String[]
	 * order_refnum = getRequest().getParameterValues("instorderrefnum"); //
	 * String order_refnum = getRequest().getParameter("instorderrefnum");
	 * 
	 * String productcode = getRequest().getParameter("productcode"); String bin
	 * = null; try { bin = commondesc.getBin(instid, productcode, jdbctemplate);
	 * } catch (Exception e1) { trace("Exception : while getteing bin : " +
	 * e1.getMessage()); e1.printStackTrace(); }
	 * 
	 * String checkerid = ""; String makerid = ""; String mkckflag;
	 * 
	 * String ckdate = "";
	 * 
	 * String username = comUsername(); String padssenable =
	 * commondesc.checkPadssEnable(instid, jdbctemplate); String authmsg = "";
	 * String keyid = ""; String mcardno = "", actiontypye = ""; StringBuffer
	 * hcardno = new StringBuffer(); String ecardno = ""; PadssSecurity padsssec
	 * = new PadssSecurity(); List secList = null; String EDPK=""; ; if
	 * (padssenable.equals("Y")) {
	 * 
	 * keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
	 * System.out.println("keyid::" + keyid); secList =
	 * commondesc.getPADSSDetailById(keyid, jdbctemplate); Properties
	 * props=getCommonDescProperty(); EDPK=props.getProperty("EDPK");
	 * System.out.println("secList::" + secList);
	 * 
	 * }
	 * 
	 * Personalizeorderdetails instantorderdetails, bindetails, extradetails;
	 * bindetails = commondesc.gettingBindetails(instid, bin, jdbctemplate); if
	 * (bindetails == null) {
	 * 
	 * session.setAttribute("prevmsg", "  No Records found for the bin " + bin);
	 * session.setAttribute("preverr", "E"); return this.cardGenHome(); }
	 * 
	 * String act = (String) session.getAttribute("act"); String default_date =
	 * commondesc.default_date_query; trace("checking the session value act " +
	 * act); if (act.equals("M")) { makerid = usercode; actiontypye = "IM";
	 * mkckflag = "M"; ckdate = commondesc.default_date_query; authmsg =
	 * " Waiting For Authorization "; } else { // D actiontypye = "ID"; makerid
	 * = usercode; checkerid = makerid; mkckflag = "P"; ckdate = "sysdate";
	 * authmsg = ""; }
	 * 
	 * trace("bindetails" + bindetails.chnlen_glcode + " Attch prod card==> " +
	 * bindetails.prodcard_expiry + " attche branch==>" +
	 * bindetails.brcode_servicecode + " BaseLen " + bindetails.baselen_feecode
	 * + " Cards Count " + bindetails.cardscount_cardccy + " Chn Base No===>" +
	 * bindetails.chnbaseno_limitid);
	 * 
	 * Boolean isallcardprocessed = true; int cardcount = 0; int
	 * cardtoprocesscnt = order_refnum.length; trace(
	 * "Getting branch attached...."); // String branchattch = //
	 * commondesc.checkBranchattached(instid,jdbctemplate); // trace(
	 * "branchattch : " + branchattch ); // trace(
	 * "Got instant card genearaion order count : " + order_refnum ); String
	 * branchattch = ""; String prodcardtype_attach = ""; List attachedtype =
	 * commondesc.checkattachedtype(instid, bin, jdbctemplate); Iterator attype
	 * = attachedtype.iterator(); if (!attachedtype.isEmpty()) { while
	 * (attype.hasNext()) { Map map = (Map) attype.next(); prodcardtype_attach =
	 * ((String) map.get("ATTACH_PRODTYPE_CARDTYPE")); branchattch = ((String)
	 * map.get("ATTACH_BRCODE")); } } try { System.out.println(
	 * "Legth of order ref value " + order_refnum.length); for (int i = 0; i <
	 * order_refnum.length; i++) {
	 * 
	 * trace("Generating order : " + order_refnum[i].trim());
	 * instantorderdetails = commondesc.gettingInstantorderDeatils(instid,
	 * order_refnum[i], jdbctemplate);
	 * 
	 * extradetails = commondesc.getCcyexpiry(instid, instantorderdetails.bin,
	 * instantorderdetails.card_type_id, instantorderdetails.sub_prod_id,
	 * instantorderdetails.product_code, jdbctemplate);
	 * 
	 * trace("getting extra details " + extradetails + "\n branchcodecc" +
	 * instantorderdetails.branch_code); String breakupvalue =
	 * commondesc.getChnbreakupvalues(instid, bindetails.prodcard_expiry,
	 * bindetails.brcode_servicecode, instantorderdetails.card_type_id,
	 * instantorderdetails.sub_prod_id, instantorderdetails.product_code,
	 * instantorderdetails.branch_code, bindetails.apptypelen,
	 * bindetails.apptypevalue);
	 * 
	 * int expprd = Integer.parseInt(extradetails.prodcard_expiry); String
	 * expirydate = "add_months(sysdate," + expprd + ")"; String newchn =
	 * instantorderdetails.bin + breakupvalue; trace("bin and breakupvalue" +
	 * instantorderdetails.bin + "adsadsa" + breakupvalue); trace("newchn :: " +
	 * newchn);
	 * 
	 * // String sequncenumber = //
	 * commondesc.gettingSequnceNumber(instid,instantorderdetails.bin,
	 * instantorderdetails.branch_code,bindetails.baselen_feecode,
	 * instantorderdetails.card_type_id,jdbctemplate,branchattch);
	 * 
	 * String sequncenumber = commondesc.gettingSequnceNumberNew(instid,
	 * instantorderdetails.bin, instantorderdetails.branch_code,
	 * bindetails.baselen_feecode, instantorderdetails.card_type_id,
	 * instantorderdetails.sub_prod_id, jdbctemplate, branchattch,
	 * prodcardtype_attach);
	 * 
	 * long sequn_no = Long.parseLong(sequncenumber);
	 * 
	 * trace("Getting card reference number length ...."); String cardrefnulen =
	 * commondesc.getCardReferenceNumberLen(instid, jdbctemplate); trace(
	 * "Got : " + cardrefnulen); if (cardrefnulen == null) {
	 * session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",
	 * "Could not generate card... Card Referene number length is empty...");
	 * trace(
	 * "Could not generate card... Card Referene number length is empty...");
	 * return this.cardGenHome(); } /// start
	 * 
	 * for( int card=1;card<=cardtoprocesscnt;card++ ) {
	 * 
	 * 
	 * String pcflag = "C"; int quryresult = 0; int quryresult1 = 0; String
	 * strseq = Long.toString(sequn_no); cardno = commondesc.generateCHN(newchn,
	 * strseq, Integer.parseInt(bindetails.baselen_feecode),
	 * Integer.parseInt(bindetails.chnlen_glcode));
	 * 
	 * if (cardno.equals("N")) {
	 * 
	 * session.setAttribute("prevmsg", "Card Generation Failed");
	 * session.setAttribute("preverr", "E"); cardcount = -2; break; }
	 * 
	 * if (cardno.equals("M")) {
	 * 
	 * session.setAttribute("prevmsg",
	 * " Card Number Sequnce Reached Maximum Length "); trace(
	 * " Card Number Sequnce Reached Maximum Length ");
	 * session.setAttribute("preverr", "E"); cardcount = -3; break; }
	 * 
	 * int chnlen = cardno.length();
	 * 
	 * // trace( " Generated Card Number[ " + cardno + " ] ");
	 * 
	 * trace("Generating card reference number...."); String cardrefno =
	 * commondesc.generateCardRefNumber(instid, instantorderdetails.sub_prod_id,
	 * cardrefnulen, jdbctemplate); trace("Got cardrefno : " + cardrefno); if
	 * (cardrefno == null) { txManager.rollback(transact.status);
	 * session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",
	 * "Could not generate card... Got Card Referene number  is empty...");
	 * trace(
	 * "Could not generate card... Card Referene number length is empty...");
	 * return this.cardGenHome(); }
	 * 
	 * trace("generating card " + order_refnum[i]); String encname =
	 * commondesc.getEncEmbName(instid, order_refnum[i], jdbctemplate); String
	 * CIN = "", STATUS_CODE = "", PC_FLAG = "P", ORDER_FLAG = "I", USED_CHN =
	 * "", APP_NO = "", PRE_FILE = "", COURIER_ID = "", PRIVILEGE_CODE = "";
	 * trace("Inserting into db....");
	 * 
	 * mcardno = padsssec.getMakedCardno(cardno);
	 * 
	 * if (padssenable.equals("Y")) { Iterator secitr = secList.iterator(); if
	 * (!secList.isEmpty()) { while (secitr.hasNext()) { Map map = (Map)
	 * secitr.next(); String CDMK = ((String) map.get("DMK")); //String eDPK =
	 * ((String) map.get("DPK")); String CDPK=padsssec.decryptDPK(CDMK, EDPK);
	 * hcardno = padsssec.getHashedValue(cardno + instid); ecardno =
	 * padsssec.getECHN(CDPK, cardno); } } }
	 * 
	 * 
	 * Connection connection = jdbctemplate.getDataSource().getConnection();
	 * CallableStatement callableStatement = connection.prepareCall(
	 * "{ call testprocebysardar(?, ?, ?)}");
	 * 
	 * callableStatement.setString(1,instid); callableStatement.setString(2,
	 * " sardar"); callableStatement.registerOutParameter(3,3 );
	 * callableStatement.executeUpdate(); trace("cfdfdfd : " +
	 * callableStatement.executeUpdate());
	 * 
	 * 
	 * 
	 * String cardinsrt_qury="INSERT INTO INST_CARD_PROCESS" +
	 * "(INST_ID,ORDER_REF_NO,BIN,CARD_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,"
	 * +
	 * "CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,"
	 * +
	 * "ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,"
	 * +
	 * "WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE, CARD_REF_NO,HCARD_NO,MCARD_NO,CARDISSUETYPE ) VALUES"
	 * + "('"+instid+"','"+order_refnum[i]+"','"+bin+"','"+ecardno+
	 * "','"+CIN+"','"+instantorderdetails.card_type_id+"','"+
	 * instantorderdetails.sub_prod_id+"','"+instantorderdetails.
	 * product_code+"','"+instantorderdetails.branch_code+
	 * "','01','A','"+STATUS_CODE+"'" +
	 * ",'"+extradetails.cardscount_cardccy+"','"+PC_FLAG+"','"+
	 * ORDER_FLAG+"','"+cardno+"','"+cardno+"',sysdate,"+expirydate+
	 * ",'"+APP_NO+"',"+default_date+","+default_date+","+
	 * default_date+",'"+PRE_FILE+"',"+default_date+","+ default_date+
	 * ","+default_date+","+default_date+",'"+makerid+
	 * "',(sysdate),'"+checkerid+"',"+ckdate+",'"+mkckflag+"','"+
	 * extradetails.brcode_servicecode+"','"+extradetails.
	 * baselen_feecode+"','"+extradetails.chnbaseno_limitid+"','"+
	 * PRIVILEGE_CODE+"','0'," +
	 * "'0','0','0','0','0','"+encname+"','"+encname+"','"+
	 * COURIER_ID+"',"+ckdate+", '"
	 * +cardrefno+"','"+hcardno+"','"+mcardno+"','P')";
	 * 
	 * 
	 * String cardinsrt_qury = "INSERT INTO INST_CARD_PROCESS" +
	 * "(INST_ID,ORDER_REF_NO,BIN,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,"
	 * +
	 * "CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,"
	 * +
	 * "ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,"
	 * +
	 * "WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE, CARD_REF_NO,MCARD_NO,CARDISSUETYPE ) VALUES"
	 * + "('" + instid + "','" + order_refnum[i] + "','" + bin + "','" + CIN +
	 * "','" + instantorderdetails.card_type_id + "','" +
	 * instantorderdetails.sub_prod_id + "','" +
	 * instantorderdetails.product_code + "','" +
	 * instantorderdetails.branch_code + "','01','A','" + STATUS_CODE + "'" +
	 * ",'" + extradetails.cardscount_cardccy + "','" + PC_FLAG + "','" +
	 * ORDER_FLAG + "','" + ecardno + "','" + ecardno + "',sysdate," +
	 * expirydate + ",'" + APP_NO + "'," + default_date + "," + default_date +
	 * "," + default_date + ",'" + PRE_FILE + "'," + default_date + "," +
	 * default_date + "," + default_date + "," + default_date + ",'" + makerid +
	 * "',(sysdate),'" + checkerid + "'," + ckdate + ",'" + mkckflag + "','" +
	 * extradetails.brcode_servicecode + "','" + extradetails.baselen_feecode +
	 * "','" + extradetails.chnbaseno_limitid + "','" + PRIVILEGE_CODE +
	 * "','0'," + "'0','0','0','0','0','" + encname + "','" + encname + "','" +
	 * COURIER_ID + "'," + ckdate + ", '" + cardrefno + "','" + mcardno +
	 * "','P')";
	 * 
	 * String cardinsrt_qury1 =
	 * "INSERT INTO INST_CARD_PROCESS_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE) VALUES"
	 * + "('" + instid + "','" + order_refnum[i] + "','" + bin + "','" + hcardno
	 * + "','" + CIN + "','" + instantorderdetails.card_type_id + "','" +
	 * instantorderdetails.sub_prod_id + "','" +
	 * instantorderdetails.product_code + "','" +
	 * instantorderdetails.branch_code + "',sysdate" + ",'" + makerid +
	 * "',(sysdate),'" + checkerid + "'," + ckdate + ")";
	 * 
	 * enctrace("cardgenquery" + cardinsrt_qury); enctrace("cardinsrt_qury1" +
	 * cardinsrt_qury1);
	 * 
	 * try { quryresult = commondesc.executeTransaction(cardinsrt_qury,
	 * jdbctemplate); quryresult1 =
	 * commondesc.executeTransaction1(cardinsrt_qury1, jdbctemplate); } catch
	 * (Exception e) { e.printStackTrace(); trace("Insert Qury is::" + e); }
	 * trace("Got : " + quryresult); if (quryresult > 0 && quryresult1 > 0) {
	 * cardcount++; sequn_no++; } else { cardcount = -1; trace(
	 * "CHN Insert Failed, Process Break"); isallcardprocessed = false; break; }
	 * 
	 * trace("checking card rpocess count " + cardtoprocesscnt +
	 * "\n checking card count" + cardcount);
	 * 
	 * if (cardtoprocesscnt > 0) { trace("All the Cards Generated Succesfully "
	 * ); String updateseq = "";
	 * 
	 * //// Changed For update last Sequance in Baseno table
	 * 
	 * if (branchattch.equals("Y")) { updateseq =
	 * "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" +
	 * instid + "' AND BIN='" + instantorderdetails.bin + "' AND BASENO_CODE='"
	 * + instantorderdetails.branch_code + "' "; } else if
	 * (prodcardtype_attach.equals("C")) { updateseq =
	 * "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" +
	 * instid + "' AND BIN='" + instantorderdetails.bin + "' AND BASENO_CODE='"
	 * + instantorderdetails.card_type_id + "' "; } else if
	 * (prodcardtype_attach.equals("P")) { updateseq =
	 * "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" +
	 * instid + "' AND BIN='" + instantorderdetails.bin + "' AND BASENO_CODE='"
	 * + instantorderdetails.sub_prod_id + "' "; } else { updateseq =
	 * "UPDATE PRODUCTINFO SET CHN_BASE_NO='" + sequn_no +
	 * "' WHERE INST_ID='" + instid + "' AND BIN='" + instantorderdetails.bin +
	 * "'"; }
	 * 
	 * 
	 * if(branchattch.equals("Y")){ updateseq =
	 * "UPDATE IFD_BRANCH_MASTER SET \""
	 * +instantorderdetails.card_type_id+"_CHN_BASE_NO\"='"+ sequn_no+
	 * "' WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"
	 * +instantorderdetails.branch_code+"'"; enctrace( "updateseq Qury is : "
	 * +updateseq); }else if(branchattch.equals("N")){ updateseq =
	 * "UPDATE IFD_PRODUCTINFO SET CHN_BASE_NO='"+sequn_no+
	 * "' WHERE INST_ID='"+instid+"' AND BIN='" +instantorderdetails.bin+"'"; }
	 * 
	 * 
	 * enctrace(" updateseq : " + updateseq); int incseq =
	 * jdbctemplate.update(updateseq); if (incseq == 1) { String
	 * updateorderstatus =
	 * "UPDATE INST_CARD_ORDER SET ORDER_STATUS='02',MAKER_DATE=sysdate, MAKER_ID='"
	 * + usercode + "' WHERE INST_ID='" + instid + "' AND ORDER_REF_NO='" +
	 * order_refnum[i] + "'"; enctrace(" Card Order Status QUry " +
	 * updateorderstatus); int order = jdbctemplate.update(updateorderstatus);
	 * trace("Updating Card Order Status...got :" + order); if (order == 0) {
	 * addActionError("Unable to continue the process"); trace(
	 * "Order Status Update Failed, Txn Got Rollback"); isallcardprocessed =
	 * false; break; } } else { addActionError("Unable to continue the process"
	 * ); trace("CHN BASE NO Update Failed,Txn got Rollback ");
	 * isallcardprocessed = false; break; } }
	 * 
	 *//************* AUDIT BLOCK **************/
	/*
	 * try {
	 * 
	 * auditbean.setActmsg("Card [ " + mcardno + " ] Generated For the Order [ "
	 * + instantorderdetails.order_ref_no + " ]");
	 * auditbean.setUsercode(username); auditbean.setAuditactcode("0202");
	 * auditbean.setActiontype(actiontypye);
	 * auditbean.setCardcollectbranch(instantorderdetails.branch_code);
	 * 
	 * auditbean.setProduct(instantorderdetails.product_code);
	 * auditbean.setSubproduct(instantorderdetails.sub_prod_id);
	 * auditbean.setBin(instantorderdetails.bin);
	 * auditbean.setApplicationid(instantorderdetails.order_ref_no);
	 * auditbean.setCardno(mcardno); // auditbean.setCardnumber(cardno);
	 * commondesc.insertAuditTrailPendingCommit(instid, username, auditbean,
	 * jdbctemplate, txManager); } catch (Exception audite) { trace(
	 * "Exception in auditran : " + audite.getMessage()); }
	 *//************* AUDIT BLOCK **************/
	/*
	 * } // end for loop
	 * 
	 * if (cardtoprocesscnt == cardcount) { txManager.commit(transact.status);
	 * trace("Committed...for " + cardcount); session.setAttribute("prevmsg",
	 * order_refnum.length + " Card(s) Generated Successfully. " + authmsg);
	 * session.setAttribute("preverr", "S"); } else { isallcardprocessed =
	 * false; trace(
	 * " Card Geneartion Process failed txn got roll back ,,,End of Loop.... Boolean isallcardprocess : "
	 * + isallcardprocessed); addActionError("Unable to continue the process");
	 * txManager.rollback(transact.status); // trace(
	 * " Card Geneartion Process failed txn got roll back");
	 * 
	 * }
	 * 
	 * } catch (Exception e) { transact.txManager.rollback(transact.status); //
	 * gensuc = false; session.setAttribute("preverr", "E");
	 * session.setAttribute("prevmsg", "Exception : Could not generate cards..."
	 * ); trace("Exception : Could not generate cards..." + e.getMessage());
	 * e.printStackTrace(); // break; }
	 * 
	 * // ordcnt++;
	 * 
	 * } /*if( gensuc ) { transact.txManager.commit(transact.status);
	 * session.setAttribute("preverr", "S"); int selectedorder =
	 * order_refnum.length; session.setAttribute("prevmsg", ordcnt+
	 * " Order(s) cards generated succesfully. "); trace(ordcnt+
	 * " Order(s) cards generated succesfully.Got commtted ");
	 *//*** MAIL BLOCK ****/

	/*
	 * 
	 * IfpTransObj transactmail = commondesc.myTranObject(); try { String
	 * alertid = this.parentid; if( alertid != null && ! alertid.equals("000")){
	 * String keymsg = "Instant card generated for  " + ordcnt + " Order(s)";
	 * int mail = comutil.sendMail( instid, alertid, keymsg, jdbctemplate,
	 * session, getMailSender() ); System.out.println( "mail return__" + mail);
	 * } } catch (Exception e) { trace("Exception : " + e.getMessage() );
	 * e.printStackTrace(); } finally{
	 * transactmail.txManager.commit(transactmail.status); System.out.println(
	 * "mail commit successfully"); }
	 *//*** MAIL BLOCK ****//*
							 * 
							 * 
							 * trace( " Order status updated txn got commited");
							 * return this.cardGenHome(); }else{ if(
							 * !transact.status.isCompleted() ) {
							 * transact.txManager.rollback(transact.status) ; }
							 * return this.cardGenHome(); }
							 * 
							 * 
							 * return this.cardGenHome(); }
							 */

	public String cardAuthHome() {
		trace("card authorization home....");
		String instid = comInstId();
		/*
		 * HttpSession session = getRequest().getSession();
		 * 
		 * int reqch= commondesc.reqCheck().requiredCheck(instid, session,
		 * jdbctemplate); if( reqch < 0 ){ return "required_home"; }
		 * 
		 * 
		 * try {
		 * 
		 * prodlist = commondesc.getProductList( instid, jdbctemplate, session
		 * ); setProdlist(prodlist);
		 * 
		 * branchlist = this.commondesc.generateBranchList(instid,
		 * jdbctemplate); setBranchlist(branchlist);
		 * session.setAttribute("curerr", "S"); } catch (Exception e) {
		 * session.setAttribute("curerr", "E"); session.setAttribute("curmsg",
		 * "Error while execute the instantOrder" + e.getMessage()); }
		 */
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();

		// String temp = act;
		// System.out.println(temp);

		// session.setAttribute("PINGEN_ACT", act);
		String cardStatus = "01", mkrstatus = "M";
		String session_act = (String) session.getAttribute("PINGEN_ACT");
		System.out.println("session_act " + session_act);
		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.InstgetBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					// setCardgenstatus('Y');
				} else {
					addActionError("No Cards Waiting For Card Authorization ... ");
					System.out.println("Branch List is empty ");
					return "required_home";

				}
			}
			pers_prodlist = commondesc.InstgetProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setProdlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				System.out.println("Product List is ===> " + pers_prodlist);
				// setCardgenstatus('Y');
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Product Details Found ");
				// setCardgenstatus('N');
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
			// setCardgenstatus('N');
		}

		return "instcardauth_home";
	}

	public String viewGenereaetdCards() throws Exception { // view authorzie
															// cards
		HttpSession session = getRequest().getSession();

		String instid = comInstId();
		String card_status = "01";
		String mkckstatus = "M";
		String datefld = "GENERATED_DATE";
		String branch = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		String[] bin = cardtype.split("~");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");

		String filtercond = commondesc.filterCondition(bin[0], branch, fromdate, todate, datefld);
		trace("filter condition " + filtercond);

		List waitingforcardauth = this.commondesc.waitingForInstCardProcess(instid, card_status, mkckstatus, filtercond,
				jdbctemplate);
		setInstorderlist(waitingforcardauth);

		if (waitingforcardauth.isEmpty()) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " No records found ");
			return this.cardGenHome();
		}

		return "instcardauth_list";
	}

	public String authourizeInstCard() throws Exception {
		trace("****** Authorize card ******* ");
		enctrace("****** Authorize card ******* ");
		HttpSession session = getRequest().getSession();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		IfpTransObj transact = commondesc.myTranObject("INSTCARDGEN", txManager);
		String usercode = comUserCode();
		String instid = comInstId();
		String table = "INST_CARD_PROCESS";
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		
		
		int size = 0;
		int[] updateCounts = new int[size];
		Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();
		PreparedStatement pstmt = null;
		
		// String remarks = getRequest().getParameter("reason");
		
		//added by gowtham_220719
				String  ip=(String) session.getAttribute("REMOTE_IP");
		
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

		
	 	try{
			
			if(order_refnum== null ){
				addActionError("No card number selected ....");
					return "required_home";
			}
			int crdcnt = 0;
		
			System.out.println("authstatus--->"+authstatus);
			System.out.println("SYSDATE--->"+test.Date.getCurrentDate());
			System.out.println("instid-->"+instid);
			System.out.println("order_refnum-->"+order_refnum.length);
			
		/*	String cardauthqry = "UPDATE INST_CARD_PROCESS SET   MKCK_STATUS=?, AUTH_DATE=? WHERE INST_ID=? AND ORDER_REF_NO=? ";
			enctrace("cardgenqry : " + cardauthqry);
			int x =jdbctemplate.update(cardauthqry,new Object[]{"P",test.Date.getCurrentDate(),instid,order_refnum[i]});
		*/	
			
			if (conn != null) {
				System.out.println(" connection opend----->\n");
				trace("connectionopend---\n");
				enctrace("connectionopend---\n");
				conn.setAutoCommit(false);
				String sql = "UPDATE INST_CARD_PROCESS SET MKCK_STATUS=?, AUTH_DATE=SYSDATE WHERE INST_ID=? AND ORDER_REF_NO=? ";
				pstmt = conn.prepareStatement(sql);
				trace("sql qry for update : " + sql);
				
				for (int i = 0; i < order_refnum.length; i++) {
					pstmt.setString(1,authstatus );
					pstmt.setString(2,instid );
					pstmt.setString(3, order_refnum[i]);
					pstmt.addBatch();
		 		}
				long start = System.currentTimeMillis();
				updateCounts = pstmt.executeBatch();
				long end = System.currentTimeMillis();
				System.out.println("updateCounts--->"+updateCounts.length);
				System.out.println("awaitcardlist.length---->"+order_refnum.length);
				trace("awaitcardlist.length---->"+order_refnum.length);
				
				if(updateCounts.length==order_refnum.length){
				conn.commit();
				//addActionMessage( updateCounts.length + "    Card(s) Authorized Successfully...");
				
				session.setAttribute("prevmsg",  updateCounts.length + "    Card(s) Order " + statusmsg +" Successfully...");
				session.setAttribute("preverr", "S");
				
				System.out.append("Update records-----------> /n" + updateCounts.length);
				trace(updateCounts.length + " Card(s) Authorized Successfully..got committed...");
				trace(order_refnum.length + " Card(s) Authorized Successfully..got committed...");
				return cardAuthHome();}
				else{
					conn.rollback();
					pstmt.clearBatch() ;
					System.out.println("could not continue the process.../n--");
					trace("could not continue the process...---");
					addActionError("could not continue the process...!!!");
					return cardAuthHome();
					}
		
				
			}}catch(Exception e){
				txManager.rollback(status);
				addActionError("Exception : could not continue the process...");
				trace("Exception : could not continue the process..."+ e.getMessage());
				e.printStackTrace();
				return "required_home";
			}finally {
				try {
					if (pstmt != null) {
						pstmt.close();
						System.out.println("prepare stmt closed-----");
					}
					if (conn != null) {
						conn.close();
						System.out.println("Jdbc connection closed-----> ");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		
		
		
	 	//COMMENTED ON 19-FEB-2021	
		
		/*
		try {
			int cnt = 0;
			trace("Getting the order-ref count : " + order_refnum.length);
			for (int i = 0; i < order_refnum.length; i++) {
				
				String cardauthqry = "UPDATE INST_CARD_PROCESS SET   MKCK_STATUS='P', AUTH_DATE=sysdate WHERE INST_ID='"
						+ instid + "' AND ORDER_REF_NO='" + order_refnum[i] + "' ";
				enctrace("cardgenqry : " + cardauthqry);
				int x = commondesc.executeTransaction(cardauthqry, jdbctemplate);
				
				
	///by gowtham-300819
				
				String cardauthqry = "UPDATE INST_CARD_PROCESS SET   MKCK_STATUS=?, AUTH_DATE=? WHERE INST_ID=? AND ORDER_REF_NO=? ";
				enctrace("cardgenqry : " + cardauthqry);
				int x =jdbctemplate.update(cardauthqry,new Object[]{"P",test.Date.getCurrentDate(),instid,order_refnum[i]});
				
				
				trace("Got : " + x);
				cnt++;

				*//************* AUDIT BLOCK **************//*
				try {
					String mcardno = commondesc.getMaskedCardbyproc(instid, order_refnum[i], table, "O", jdbctemplate);
					if (mcardno == null) {
						mcardno = order_refnum[i];
					}
					//added by gowtham_220719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);

					
					auditbean.setActmsg(
							" Authorized Successfully and Waiting In Security Data Generation ...+  Card Number [ "
									+ mcardno + " ]");
					auditbean.setUsercode(usercode);
					auditbean.setActiontype("IC");
					auditbean.setAuditactcode("0202");
					auditbean.setCardno(mcardno);
					auditbean.setApplicationid(order_refnum[i].toString().trim());
					// auditbean.setRemarks(remarks);
					// auditbean.setProduct(persorderdetails.product_code);
					// commondesc.insertAuditTrail(instid, userid, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				*//************* AUDIT BLOCK **************//*

			}

			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", cnt + " Ordred authorized successfully");
			transact.txManager.commit(transact.status);
			trace(cnt + " Ordred authorized successfully .... committed success");

		} catch (Exception e) {
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Exception : Could not authorize order...");
			transact.txManager.rollback(transact.status);
			trace("Exception : while authorize the order " + e.getMessage());
		}
		// Added by ramesh procedure call
		
		 * String authstatus = ""; String statusmsg = ""; String err_msg="";
		 * //String remarks = getRequest().getParameter("reason"); if (
		 * getRequest().getParameter("authorize") != null ){ System.out.println(
		 * "AUTHORIZE..........." ); authstatus = "P"; statusmsg =
		 * " Authorized Successfully and Waiting In Security Data Generation ..."
		 * ; err_msg="Authorize"; }else if (
		 * getRequest().getParameter("deauthorize") != null ){
		 * System.out.println( "DE AUTHORIZE..........." ); authstatus = "D";
		 * statusmsg = " De-Authorized Successfully .."; err_msg="De-Authorize";
		 * }
		 

		
		 * try{ String columns = "", condition = "", table =
		 * "INST_CARD_PROCESS", result = ""; if(Integer.parseInt(totalcount) ==
		 * (order_refnum.length)){ String authorderqry =
		 * "UPDATE INST_CARD_ORDER SET MKCK_STATUS='"+authstatus+
		 * "', CHECKER_DATE=SYSDATE, CHECKER_ID='"+checkdrid+"' WHERE INST_ID='"
		 * +instid+"' "+filtercond+" "; enctrace("instant card auth all --->"
		 * +authorderqry); int x = commondesc.executeTransaction(authorderqry,
		 * jdbctemplate); trace("Got : " + x ); txManager.commit( status );
		 * }else{ Connection conn = null; Dbcon dbcon = new Dbcon(); conn =
		 * dbcon.getDBConnection(); CallableStatement cstmt = null; cstmt =
		 * conn.prepareCall("call SP_COMMON_UPDATE(?,?,?,?,?,?)"); trace(
		 * "procedure--->call SP_COMMON_UPDATE(?,?,?,?,?)"); ArrayDescriptor
		 * arrDesc = ArrayDescriptor.createDescriptor("TVARCHAR2ARRAY", conn);
		 * System.out.println("check"); ARRAY array = new ARRAY(arrDesc, conn,
		 * order_refnum); trace("proc args-->"+array+"--"+instid+"--"+usercode);
		 * cstmt.setString(1, table); cstmt.setArray(2, array);
		 * cstmt.setString(3, instid); columns =
		 * "  MKCK_STATUS='P', CHECKER_ID='"+usercode+
		 * "',CHECKER_DATE=(sysdate), AUTH_DATE=sysdate"; condition =
		 * " WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN"; cstmt.setString(4,
		 * columns); cstmt.setString(5, condition);
		 * cstmt.registerOutParameter(6,java.sql.Types.VARCHAR);
		 * cstmt.execute(); result=cstmt.getString(6);
		 * trace("result--->"+result); // conn.commit(); //}
		 * 
		 *//************* AUDIT BLOCK **************//*
		
		 * try{ auditbean.setActmsg("Card  [ "+order_refnum.length+
		 * " ] Authorized Successfully "); auditbean.setActiontype("IC");
		 * auditbean.setUsercode(usercode); auditbean.setAuditactcode("0202");
		 * commondesc.insertAuditTrail(instid, usercode, auditbean,
		 * jdbctemplate, txManager); }catch(Exception audite ){ trace(
		 * "Exception in auditran : "+ audite.getMessage()); }
		 *//************* AUDIT BLOCK **************//*
													 * 
													 * session.setAttribute(
													 * "preverr", "S");
													 * session.setAttribute(
													 * "prevmsg", result );
													 * 
													 * if(result.contains(
													 * "successfully")){
													 * addActionMessage("Cards "
													 * +result); }else{
													 * addActionError(
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
	 	
	 	//COMMENTED ON 19-FEB-2021
	 	
	 	
		return this.cardAuthHome();
	}

}