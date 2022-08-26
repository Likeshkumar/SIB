package com.ifp.personalize;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.Action.ThalesAction;
import com.ifp.instant.HSMParameter;
import com.ifp.instant.InstCardPinProcess;
import com.ifp.util.CommonDesc;
import com.ifp.util.DebugWriter;
import com.ifp.util.IfpTransObj;
import com.ifp.beans.AuditBeans;

import connection.Dbcon;

/**
 * SRNP0004
 */
public class PingenerationAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	PadssSecurity sec = new PadssSecurity();

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

	CommonDesc commondesc = new CommonDesc();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
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

	// added by gowtham220719
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
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

	private String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
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

	PersionalizedcardCondition brcodecon = new PersionalizedcardCondition();

	public String personalpingenerationhome() {
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();

		String temp = act;
		System.out.println(temp);
		session.setAttribute("PINGEN_ACT", act);
		String cardStatus = "01", mkrstatus = "P";
		String session_act = (String) session.getAttribute("PINGEN_ACT");
		System.out.println("session_act " + session_act);

		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("act", act);
		}

		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					// setCardgenstatus('Y');
				} else {
					addActionError("No Cards Waiting For Security Data Generation ... ");
					System.out.println("Branch List is empty ");
					return "required_home";

				}
			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
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
			/*
			 * String condition = "CAF_REC_STATUS IN('R','S')"; int cafrecord =
			 * commondesc.maintenanceRecordscheck(inst_id,condition);
			 * if(cafrecord !=0 ) { setMaintenancestatus('Y'); }
			 */

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
			// setCardgenstatus('N');
		}

		return "pingenerationhome";
	}

	private List perspingenorders;

	public List getPerspingenorders() {
		return perspingenorders;
	}

	public void setPerspingenorders(List perspingenorders) {
		this.perspingenorders = perspingenorders;
	}

	private String cardtype_selected;

	public String getCardtype_selected() {
		return cardtype_selected;
	}

	public void setCardtype_selected(String cardtype_selected) {
		this.cardtype_selected = cardtype_selected;
	}

	public String getPingenerationorders() {
		HttpSession session = getRequest().getSession();
		// String pingentype = getRequest().getParameter("pingentype");

		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String inst_id = comInstId();

		System.out.println(
				"branch==> " + branch + "   binno==>" + binno + "  fromdate===> " + fromdate + "  todate===>" + todate);
		String dateflag = "GENERATED_DATE";

		String cardstatus = "01", mkckstatus = "P";
		List authorizeorderlist = null;
		String session_act = (String) session.getAttribute("PINGEN_ACT");
		System.out.println("session_act " + session_act);
		try {

			int pingen = commondesc.checkPingenerationstatus(inst_id, branch, jdbctemplate);
			System.out.println("Card Gen Status" + pingen);
			if (pingen > 0) {
				setAct((String) session.getAttribute("PINGEN_ACT"));
				System.out.println("Another uSer Processing Card Generation");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Another User Processing Pin Generation, Please Wait.... ");
				return personalpingenerationhome();
			}
			this.fromdate = fromdate;
			this.todate = todate;
			String condition = commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			System.out.println("Condition Value----->  " + condition);
			authorizeorderlist = commondesc.personaliseCardauthlistpin(inst_id, cardstatus, mkckstatus, condition,
					jdbctemplate);
			trace("authorizeorderlist===>  " + authorizeorderlist);
			System.out.println("authorizeorderlist===> " + authorizeorderlist);
			if (!(authorizeorderlist.isEmpty())) {
				setPerspingenorders(authorizeorderlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				setCardtype_selected(binno);

			} else {
				setPerspingenorders(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No Orders To Generate Pin");
				return personalpingenerationhome();

			}
		} catch (Exception e) {
			System.out.println("Exception--->" + e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg",
					"Error While Fetching The Orders To Card Genetaion ,ERROR:" + e.getMessage());

		}

		return "pingenerationorders";
	}

	public String pinGenerationprocess() {
		trace("****** Generating personalize card pin/cvv begins  ******");
		enctrace("****** Generating personalize card pin/cvv begins ******");
		HttpSession session = getRequest().getSession();

		String bin = getRequest().getParameter("binno");
		String brcode = getRequest().getParameter("branch_code");

		String cardtypesel = getRequest().getParameter("cardtype_sel");
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");

		String generationtypeforonlycvv = getRequest().getParameter("generationtypeforonlycvv");
		trace("generationtypeforonlycvv" + generationtypeforonlycvv);
		IfpTransObj transact = commondesc.myTranObject("PERSPIN", txManager);

		String usertext = "";
		String instid = comInstId();
		String userid = comUserId();
		String username = comUsername();
		String processtype = "PERSONAL";
		String act = (String) session.getAttribute("act");
		String authmsg = "", mkckstatus = "";

		// added by gowtham_250719
		String ip = (String) session.getAttribute("REMOTE_IP");

		try {

			if (act != null && act.equals("M")) {
				authmsg = " Wating For Authorization";
				mkckstatus = "M";
			} else {
				mkckstatus = "P";
			}

			InstCardPinProcess pinobj = new InstCardPinProcess();

			int perscvv = pinobj.pinCvvGenerationAction(instid, generationtypeforonlycvv, brcode, cardtypesel,
					order_refnum, username, processtype, mkckstatus, bin, txManager, jdbctemplate, transact.status,
					session);

			if (perscvv < 0) {

				txManager.rollback(transact.status);
				// session.setAttribute("preverr", "E");
				// session.setAttribute("prevmsg", "Could not continue the pin
				// generation....");
				addActionError("Could not continue the pin generation....");
				trace("Could not continue the cvv generation....got rolled back");
				return this.personalpingenerationhome();
			}

			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Security Data Generated Successfully. " + authmsg);
			trace("Cvv generated successfully[ Personalize ]...got committed...");

			/*
			 * //added by gowtham_250719
			 *//************* AUDIT BLOCK **************/
			/*
			 * try{
			 * 
			 * //auditbean.setActmsg(
			 * "Cvv generated successfully[ Personalize ]..." );
			 * auditbean.setUsercode(usertext);
			 * auditbean.setAuditactcode("0103");
			 * 
			 * 
			 * trace("ip address======>  "+ip); auditbean.setIpAdress(ip);
			 * 
			 * 
			 * commondesc.insertAuditTrail(instid, usertext, auditbean,
			 * jdbctemplate, txManager); }catch(Exception audite ){ trace(
			 * "Exception in auditran : "+ audite.getMessage()); }
			 *//************* AUDIT BLOCK **************//*
														*/

		} catch (Exception e) {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Could not continue the pin generation...");
			trace("Could not continue the cvv generation : " + e.getMessage());
			e.printStackTrace();
		}

		trace("****** Generating personalize card pin/cvv end  ******\n\n");
		enctrace("****** Generating personalize card pin/cvv end******\n\n");

		return this.personalpingenerationhome();
	}

	/*
	 * public void deletePingenerationStatus(String instid,String brcode,
	 * JdbcTemplate jdbctemplate) { String deleteCardgenstatus =
	 * "DELETE FROM CARDGEN_STATUS WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"
	 * +brcode+"' AND CARDGEN_STATUS='P'"; System.out.println(
	 * "Delete Qury uis ==> "+deleteCardgenstatus); IfpTransObj deletestatus =
	 * commondesc.myTranObject(dataSource); try{
	 * 
	 * int delete_status = commondesc.executeTransaction(deleteCardgenstatus,
	 * jdbctemplate); System.out.println("delete_status ====> "+delete_status);
	 * if(delete_status == 1) {
	 * deletestatus.txManager.commit(deletestatus.status); System.out.println(
	 * "delete_status Comitted"); }else{
	 * deletestatus.txManager.rollback(deletestatus.status); System.out.println(
	 * "delete_status Rollback"); } } catch (Exception e) {
	 * deletestatus.txManager.rollback(deletestatus.status); System.out.println(
	 * "Error While Deleting==> "+e); } System.out.println(
	 * "Delete Tnasaction Completed ===> "+deletestatus.status.isCompleted()); }
	 */

	public String checkPingenerationrequired(String instid, String cardno, String tabletype,
			JdbcTemplate jdbctemplate) {
		String req_pin = "X";
		String table_name = "INST_CARD_PROCESS";
		if (tabletype.equals("P")) {
			table_name = "PERS_CARD_PROCESS";
		}

		/*
		 * String cardinfo = "SELECT SUB_PROD_ID,PRODUCT_CODE FROM " +
		 * table_name + " WHERE INST_ID='" + instid + "' and CARD_NO='" + cardno
		 * + "'";
		 * 
		 * List carddet = jdbctemplate.queryForList(cardinfo);
		 */

		// by gowtham260819
		String cardinfo = "SELECT SUB_PROD_ID,PRODUCT_CODE FROM " + table_name + " WHERE INST_ID=? and CARD_NO=?";
		List carddet = jdbctemplate.queryForList(cardinfo, new Object[] { instid, cardno });

		String subtype = "0", prodcode = "0";
		if (!(carddet.isEmpty())) {
			Iterator citr = carddet.iterator();
			while (citr.hasNext()) {
				Map cmap = (Map) citr.next();
				subtype = (String) cmap.get("SUB_PROD_ID");
				prodcode = (String) cmap.get("PRODUCT_CODE");
			}
			System.out.println("SubProd ===> " + subtype + " Prod Code ===> " + prodcode);
			req_pin = getPingenerationFlag(instid, subtype, prodcode, jdbctemplate);
		}
		return req_pin;
	}

	public String getPingenerationFlag(String inst_id, String subprod, String product_code, JdbcTemplate jdbctemplate) {
		String flag = "X";

		/*
		 * String pinflag_qury =
		 * "SELECT PIN_GEN_REQ FROM IFP_INSTPROD_DETAILS WHERE INST_ID='" +
		 * inst_id + "' AND PRODUCT_CODE='" + product_code +
		 * "' AND SUB_PROD_ID='" + subprod + "'"; flag = (String)
		 * jdbctemplate.queryForObject(pinflag_qury, String.class);
		 */

		String pinflag_qury = "SELECT PIN_GEN_REQ FROM IFP_INSTPROD_DETAILS WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=?";
		flag = (String) jdbctemplate.queryForObject(pinflag_qury, new Object[] { inst_id, product_code, subprod },
				String.class);

		return flag;
	}

	// #####################################################################
	// ####################################################################

	public String authPingenerationhome() {
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();

		String temp = act;
		System.out.println(temp);
		session.setAttribute("PINGEN_ACT", act);
		String cardStatus = "02", mkrstatus = "M";
		String session_act = (String) session.getAttribute("PINGEN_ACT");
		System.out.println("session_act " + session_act);
		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch + "usertype" + usertype);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					// setCardgenstatus('Y');
				}

			} else if (usertype.equals("BRANCHUSER")) {
				System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
				}
			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				System.out.println("Product List is ===> " + pers_prodlist);
				// setCardgenstatus('Y');
			} else {
				System.out.println("No Product Details Found ");
				/*
				 * session.setAttribute("curerr", "E");
				 * session.setAttribute("curmsg"," No Product Details Found ");
				 */
				// setCardgenstatus('N');
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
			// setCardgenstatus('N');
		}

		return "authpingenrationhome";
	}

	private List perspingenauthlist;

	public List getPerspingenauthlist() {
		return perspingenauthlist;
	}

	public void setPerspingenauthlist(List perspingenauthlist) {
		this.perspingenauthlist = perspingenauthlist;
	}

	public String authPingenorders() {
		HttpSession session = getRequest().getSession();

		try {
			String branch = getRequest().getParameter("branchcode");
			String cardtype = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String instid = comInstId();
			String cardstatus = "02";
			String mkckstatus = "M";
			System.out.println("Branch====> " + branch + " Card Type====> " + cardtype + " From Date ====> " + fromdate
					+ "  To Date ====> " + todate);
			List authcardorder = null;
			String dateflag = "PIN_DATE";
			String condition = commondesc.filterCondition(cardtype, branch, fromdate, todate, dateflag);
			authcardorder = commondesc.personaliseCardauthlist(instid, cardstatus, mkckstatus, condition, jdbctemplate);
			System.out.println("authcardorder=====>" + authcardorder);
			if (authcardorder.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Orders Found");
				return authPingenerationhome();
			} else {
				setPerspingenauthlist(authcardorder);
				session.setAttribute("curerr", "S");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error :" + e.getMessage());
		}

		return "authpingenauthorders";
	}

	public String pinGenerationauthorize() {
		trace("==========coming to pinGen Authorization==========  ");
		HttpSession session = getRequest().getSession();

		IfpTransObj transact = commondesc.myTranObject("PINAUTH", txManager);

		String instid = comInstId();
		String usercode = comUserId();
		String username = comUsername();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		String authmsg = "";
		String cardstatus = null;
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		System.out.println("Total Orders Selected ===> " + order_refnum.length);

		// added by gowtham_250719
		String ip = (String) session.getAttribute("REMOTE_IP");

		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			cardstatus = "02";
			statusmsg = " Authorized ";
			err_msg = "Authorize";
			authmsg = " .Waiting in Personalization Process ";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			cardstatus = "01";
			authstatus = "P";
			statusmsg = " De-Authorized ";
			err_msg = "De-Authorize";
			authmsg = " Generated Pin Cancelled. Waiting For Pin Generation ";
		}

		String mcardno = "";

		try {

			int cnt = 0;
			for (int i = 0; i < order_refnum.length; i++) {
				int check = 0;
		      trace("Selected Refnums ==>" + order_refnum[i]);

				/*
				 * String update_authdeauth_qury =
				 * "UPDATE PERS_CARD_PROCESS SET CARD_STATUS='" + cardstatus +
				 * "', MKCK_STATUS='" + authstatus + "',CHECKER_ID='" + usercode
				 * + "',CHECKER_DATE=(sysdate) WHERE INST_ID='" + instid +
				 * "' AND ORDER_REF_NO='" + order_refnum[i].trim() + "'";
				 * System.out.println("update_authdeauth_qury======>" +
				 * update_authdeauth_qury); System.out.println(
				 * "Before Update ===> " + cnt); check =
				 * jdbctemplate.update(update_authdeauth_qury);
				 */

				// by gowtham260819
				String update_authdeauth_qury = "UPDATE PERS_CARD_PROCESS SET CARD_STATUS=?, MKCK_STATUS=?,CHECKER_ID=?,CHECKER_DATE=(sysdate) WHERE INST_ID=? AND ORDER_REF_NO=? ";
				System.out.println("update_authdeauth_qury======>" + update_authdeauth_qury);
				System.out.println("Before Update ===> " + cnt);
				check = jdbctemplate.update(update_authdeauth_qury,
						new Object[] { cardstatus, authstatus, usercode, instid, order_refnum[i].trim() });

				System.out.println(" QUery Executed ==check===>    " + check);
				if (check > 0) {
					cnt = cnt + 1;
					System.out.println("After Update ===> " + cnt);
				}

				String cin = commondesc.getCinFromProcess(order_refnum[i],instid,"PERS_CIN", jdbctemplate);
				mcardno = commondesc.getMaksedCardFromProcess1(instid, order_refnum[i], jdbctemplate);
				auditbean.setActmsg("pin and cvv authorization done  sucessfully  for the card [ " + mcardno + " ]");
				auditbean.setCardno(mcardno);
				auditbean.setCin(cin);

			}
			if (order_refnum.length == cnt) {
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", cnt + "  Order " + statusmsg + " successfully." + authmsg);

				txManager.commit(transact.status);
				System.out.println(" Committed success ");
			} else {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Selected Orders Not " + statusmsg + " Successfully. " + authmsg);
				txManager.rollback(transact.status);
				System.out.println(" Rollback success ");
			}

			// added by gowtham_250719
			/************* AUDIT BLOCK **************/

			try {

				//auditbean.setActmsg("Card  [ " + order_refnum.length + " ] Authorized Successfully ");
				auditbean.setActiontype("IC");

				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(username);
				auditbean.setAuditactcode("0102");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			// ************* AUDIT BLOCK **************//*

		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error While" + err_msg + " The Orders " + e.getMessage());
			txManager.rollback(transact.status);
			System.out.println(" Rollback success ");
		}

		/*
		 * DefaultTransactionDefinition def = new
		 * DefaultTransactionDefinition();
		 * def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED
		 * ); TransactionStatus status = txManager.getTransaction(def); try{
		 * String columns = "", condition = "", table = "PERS_CARD_PROCESS",
		 * result = ""; Connection conn = null; Dbcon dbcon = new Dbcon(); conn
		 * = dbcon.getDBConnection(); CallableStatement cstmt = null; cstmt =
		 * conn.prepareCall("call SP_COMMON_UPDATE(?,?,?,?,?,?)"); trace(
		 * "procedure--->call SP_COMMON_UPDATE(?,?,?,?,?)"); ArrayDescriptor
		 * arrDesc = ArrayDescriptor.createDescriptor("TVARCHAR2ARRAY", conn);
		 * System.out.println("check"); ARRAY array = new ARRAY(arrDesc, conn,
		 * order_refnum); trace("proc args-->"+array+"--"+instid+"--"+usercode);
		 * cstmt.setString(1, table); cstmt.setArray(2, array);
		 * cstmt.setString(3, instid); columns = "  CARD_STATUS='"+cardstatus+
		 * "', MKCK_STATUS='"
		 * +authstatus+"',CHECKER_ID='"+usercode+"',CHECKER_DATE=(sysdate)";
		 * condition = " WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN";
		 * cstmt.setString(4, columns); cstmt.setString(5, condition);
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
													 * addActionMessage("CVV "
													 * +result+" Authorized .");
													 * //addActionMessage("Cvv "
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

		setAct((String) session.getAttribute("PINGEN_ACT"));
		return authPingenerationhome();

	}

	@SuppressWarnings("unchecked")
	public String getExcel() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId();
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		String cardcond = "";
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String type = getRequest().getParameter("type");
		String dateflag = "GENERATED_DATE";
		String mkckstatus = "P", cardstatus = "01";
		if ("PRE".equals(type)) {
			dateflag = "PIN_DATE";
			mkckstatus = "P";
			cardstatus = "02";
		} else if ("ISSUE".equals(type)) {
			dateflag = "ISSUE_DATE";
			mkckstatus = "M";
			cardstatus = "05";
		}

		String datecondition = "AND ( to_date('" + fromdate + "', 'dd-mm-yyyy') <= " + dateflag + " AND to_date('"
				+ todate + "', 'dd-mm-yyyy' )+1 >= " + dateflag + ")";
		for (int i = 0; i < order_refnum.length; i++) {
			cardcond += order_refnum[i] + "','";
		}

		if (cardcond.endsWith(",")) {
			cardcond = cardcond.substring(0, cardcond.length() - 1);
		}
		cardcond = " ORDER_REF_NO IN ('" + cardcond + "') ";
		if ("ISSUE".equals(type)) {
			cardcond = cardcond.replace("ORDER_REF_NO", "HCARD_NO");
		}
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		PadssSecurity padsssec = new PadssSecurity();
		String eDMK = "", eDPK = "";
		try {
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			Iterator secitr = secList.iterator();
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				eDMK = ((String) map.get("DMK"));
				// eDPK = ((String)map.get("DPK"));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return "required_home";
		}
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");

		List<Map<String, Object>> recordlist = this.getSelectedList(instid, cardcond, datecondition, mkckstatus,
				cardstatus, jdbctemplate);
		String clearchn = null;
		for (int j = 0; j < recordlist.size(); j++) {
			String CDPK = padsssec.decryptDPK(eDMK, EDPK);
			clearchn = padsssec.getCHN(CDPK, (String) recordlist.get(j).get("ORG_CHN"));
			recordlist.get(j).put("CARD_NO", clearchn);
			recordlist.get(j).remove("CARDNO");
		}
		String result = excelReportGenMaster(recordlist, type);

		return result;
	}

	public List<Map<String, Object>> getSelectedList(String instid, String cardcond, String condition,
			String mkckstatus, String cardstatus, JdbcTemplate jdbctemplate2) {
		List list = null;

		/*
		 * String pinlistqry =
		 * "select distinct(order_ref_no) as ORDER_REF_NO,ORG_CHN, CIN,MCARD_NO AS CARDNO,ACCT_NO AS ACCOUNTNO, (select SUB_PRODUCT_NAME from INSTPROD_DETAILS where SUB_PROD_ID=a.sub_prod_id) as PRODUCT,EMB_NAME,MOBILENO,to_char(generated_date,'DD-MON-YY') as GENDATE,(SELECT USERNAME FROM USER_DETAILS WHERE USERID=A.MAKER_ID) AS MAKER,branch_code AS BRANCH  "
		 * +
		 * ", (SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=A.CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH,(SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=A.CHECKER_ID) AS CHECKER from PERS_CARD_PROCESS A "
		 * + " where inst_id='" + instid + "' and " + cardcond +
		 * " AND MKCK_STATUS='" + mkckstatus + "' and CARD_STATUS='" +
		 * cardstatus + "' " + condition + " order by order_ref_no";
		 * enctrace("pinlistqry--->" + pinlistqry); list =
		 * jdbctemplate2.queryForList(pinlistqry);
		 */

		// by gowtham-260819
		String pinlistqry = "select distinct(order_ref_no) as ORDER_REF_NO,ORG_CHN, CIN,MCARD_NO AS CARDNO,ACCT_NO AS ACCOUNTNO, (select SUB_PRODUCT_NAME from INSTPROD_DETAILS where SUB_PROD_ID=a.sub_prod_id) as PRODUCT,EMB_NAME,MOBILENO,to_char(generated_date,'DD-MON-YY') as GENDATE,(SELECT USERNAME FROM USER_DETAILS WHERE USERID=A.MAKER_ID) AS MAKER,branch_code AS BRANCH  "
				+ ", (SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=A.CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH,(SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=A.CHECKER_ID) AS CHECKER from PERS_CARD_PROCESS A "
				+ " where inst_id=? and " + cardcond + " AND MKCK_STATUS=? and CARD_STATUS=? " + condition
				+ " order by order_ref_no";
		enctrace("pinlistqry--->" + pinlistqry);
		list = jdbctemplate2.queryForList(pinlistqry, new Object[] { instid, mkckstatus, cardstatus });

		return list;
	}

	public String excelReportGenMaster(List excelReportList, String type) throws IOException {
		trace("*********Report Generated Method Begin***********");
		String instid = comInstId();
		List result = excelReportList;
		String filename = "";
		if ("PIN".equals(type)) {
			filename = "PinReport_";
		} else if ("PRE".equals(type)) {
			filename = "PREReport_";
		} else if ("ISSUE".equals(type)) {
			filename = "ISSUEReport_";
		}

		String excelparam = "Sheet";

		List combinedlist = new ArrayList();
		combinedlist.add(result);
		String res = this.getExcelReport(combinedlist, excelparam, filename);
		trace("Report Generation Master Result " + res);
		return res;
	}

	public String getExcelReport(List listqry, String keyDesc, String namestr) {
		trace("	 ***************** Transaction Details report ****************");
		enctrace("**************** Transaction Details report ****************");

		trace("listqry:::::" + listqry);
		trace(":::::keyDesc" + keyDesc);
		trace(":::::namestr" + namestr);

		try {
			output_stream = new ByteArrayOutputStream();
			String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			trace("dateFormat.format(date)    --->  " + curdatetime);
			String defaultname = namestr + curdatetime + ".xls";
			trace("default name" + defaultname);
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			Sheet sheet = null;
			trace(" keyDesc " + keyDesc);
			// String keydesc_split[] = keyDesc.split("-");
			trace("Xls Sheet Name " + keyDesc);

			Iterator combined_itr = listqry.iterator();
			int i = 0, sheetcount = 1;
			sheet = workbook.createSheet(keyDesc + sheetcount);
			while (combined_itr.hasNext()) {
				List lst = (List) combined_itr.next();
				trace(" List Size " + lst.size());

				int rownum = 0;
				int cellnum = 0;
				Iterator itr1 = lst.iterator();
				int rowno = 0;
				Row rowheading = null;
				while (itr1.hasNext()) {
					trace("rowno = ******** = " + rowno);
					Map map = (Map) itr1.next();
					Iterator keyItr = map.keySet().iterator();
					if (rowno == 0) {
						rowheading = sheet.createRow((short) rowno++);
						trace("*******rowheading *****");
					}
					Row row = sheet.createRow((short) rowno++);
					String key = null;
					int cellno = 0;
					while (keyItr.hasNext()) {
						trace("rowno inside second while loop = ******** = " + rowno);
						Cell cell = null;
						key = (String) keyItr.next();
						if (rowno == 2) {
							cell = rowheading.createCell((short) cellno);
							cell.setCellValue(key);
							trace("Key .........." + key);
						}
						cell = row.createCell((short) cellno);
						cell.setCellValue((String) map.get(key));
						trace(" value .... " + (String) map.get(key));
						cellno++;
					}
					trace("rowno outside while loop " + row.getRowNum());
					if (row.getRowNum() > 49999) {
						trace("entered rownum ");
						int e = ++sheetcount;
						sheet = workbook.createSheet(keyDesc + e);
						rowno = 0;
						continue;
					}
				}
				i++;
				trace("	value of i at the bottom --	" + i + "	rownum -- " + rowno);
			}
			workbook.write(output_stream);
			setPinReport(defaultname);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			output_stream.flush();
			trace("Close file");
			return "transactionexcel";
		} catch (Exception e) {
			addActionError("Unable to continue the report generation");
			trace("Excption in Excelmethod ::: " + e.getMessage());
			e.printStackTrace();
		}
		return "globalreporterror";
	}

	private ByteArrayOutputStream output_stream;
	private ByteArrayInputStream input_stream;
	public String pinReport;
	public String fromdate;
	public String todate;
	public String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getPinReport() {
		return pinReport;
	}

	public void setPinReport(String pinReport) {
		this.pinReport = pinReport;
	}

	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}

	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}

	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}

	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}

}
