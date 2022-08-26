package com.ifp.personalize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.ifp.util.newPDFReportGenerator;

import connection.CBSConnection;
import connection.Dbcon;

/**
 * SRNP0004
 * 
 * @author CGSPL
 *
 */
public class PersonalCardReceiveIssueAction_01mar22 extends BaseAction {
	private static final long serialVersionUID = 1L;

	CommonDesc commondesc = new CommonDesc();
	JdbcTemplate jdbctemplate = new JdbcTemplate();

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	private String hcardno;

	public void setAuditbean(AuditBeans auditbean) {														
		this.auditbean = auditbean;
	}

	public String getHcardno() {
		return hcardno;
	}

	public void setHcardno(String hcardno) {
		this.hcardno = hcardno;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	final String CARDACTIVATEDCODE = CommonDesc.ACTIVECARDSTATUS;

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	private String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
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

	private List branch_list;
	private List personalproductlist;
	List courierlist;
	private String padssenabled;
	Boolean iscouriertrackenabled = false;

	public Boolean getIscouriertrackenabled() {
		return iscouriertrackenabled;
	}

	public void setIscouriertrackenabled(Boolean iscouriertrackenabled) {
		this.iscouriertrackenabled = iscouriertrackenabled;
	}

	public List getCourierlist() {
		return courierlist;
	}

	public void setCourierlist(List courierlist) {
		this.courierlist = courierlist;
	}

	public List getBranch_list() {
		return branch_list;
	}

	public void setBranch_list(List branch_list) {
		this.branch_list = branch_list;
	}

	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}

	PersionalizedcardCondition brcodecon = new PersionalizedcardCondition();

	public void getPREFilenameByBranchAndProduct() throws Exception {
		trace("calling ..." + Thread.currentThread().getStackTrace()[1].getMethodName());
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String branchcode = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		String act = getRequest().getParameter("act");
		trace("calling ..." + Thread.currentThread().getStackTrace()[1].getMethodName() + "---" + "istid:" + instid
				+ "act--" + act);
		String condition = "";
		StringBuilder result = new StringBuilder();
		String mkckstatus = "";
		String statuscode = "";
		if (act.equals("M")) {
			mkckstatus = "P";
			statuscode = "03";
		} else {
			statuscode = "04";
			mkckstatus = "M";
		}
		String branchcond = "", productcond = "";
		if ("ALL".equalsIgnoreCase(cardtype)) {
			productcond = "";
		} else {
			productcond = " AND PRODUCT_CODE='" + cardtype + "'";
		}
		String usertype = comuserType();
		if (usertype.equals("INSTADMIN")) {
			condition = "" + productcond + " AND CARD_STATUS = '" + statuscode + "' AND MKCK_STATUS = '" + mkckstatus
					+ "' ";
		} else {
			if ("ALL".equalsIgnoreCase(branchcode)) {
				branchcond = "";
			} else {
				branchcond = " AND BRANCH_CODE='" + branchcode + "'";
			}

			condition = " " + productcond + " AND CARD_STATUS = '" + statuscode + "'  " + branchcond
					+ "  AND MKCK_STATUS = '" + mkckstatus + "'";
		}
		result.append("<option value='-1'> Select Prefile </option>");
		try {

			List productlist = commondesc.getPREFilenameByBranchAndProduct(instid, condition, jdbctemplate);

			if (productlist.isEmpty()) {
				getResponse().getWriter().write("No PreFile found...");
				return;
			}
			Iterator itr = productlist.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				result = result
						.append("<option value='" + mp.get("PRE_FILE") + "'> " + mp.get("PRE_FILE") + " </option>");

			}
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception ...||" + Thread.currentThread().getStackTrace()[1].getMethodName() + "--"
					+ e.getMessage());
		}

		getResponse().getWriter().write(result.toString());
	}

	public String personalPlasticcardreceivedhome() {
		//// System.out.println("The User is---->"+act+" Type user");
		List pers_prodlist, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		HttpSession session = getRequest().getSession();
		String cardStatus = "03", mkrstatus = "P";
		session.setAttribute("ACTIONTYPE", act);
		try {
			if (usertype.equals("INSTADMIN")) {
				//// System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				if (!(br_list.isEmpty())) {
					setBranch_list(br_list);
					trace("Branch list is not empty");
				}

			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				//// System.out.println("Product List is ===> "+pers_prodlist);
			} else {
				//// System.out.println("No Product Details Found ");
				/*
				 * session.setAttribute("curerr", "E");
				 * session.setAttribute("curmsg"," No Product Details Found ");
				 */
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
		}
		//// System.out.println("return plasticcardreceivehome");
		return "plasticcardreceivehome";
	}

	private List plasticcardreceivedlist;

	public List getPlasticcardreceivedlist() {
		return plasticcardreceivedlist;
	}

	public void setPlasticcardreceivedlist(List plasticcardreceivedlist) {
		this.plasticcardreceivedlist = plasticcardreceivedlist;
	}

	public String personalPlasticreceivedlist() {

		HttpSession session = getRequest().getSession();

		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String prefilename = getRequest().getParameter("prefilename");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		//// System.out.println("branch==> "+branch+" binno==>"+binno+"
		//// prefilename ==>"+prefilename+" fromdate===> "+fromdate+"
		//// todate===>"+todate);
		List plasticreceivedorders = null;
		String instid = comInstId();
		String dateflag = "PRE_DATE", cardstatus = "03";
		String mkckstatus = "";

		mkckstatus = "P";

		// mkckstatus = "M";

		String cafstatus = "N";
		try {
			String condition = commondesc.filterConditionWithPreFile(binno, branch, prefilename, fromdate, todate,
					dateflag);
			//// System.out.println("Condition Value-----> "+condition);
			// plasticreceivedorders = commondesc.personaliseCardauthlist(
			//// instid,cardstatus, mkckstatus,condition);
			plasticreceivedorders = commondesc.maintenanceCardslist(instid, cardstatus, mkckstatus, cafstatus,
					condition, jdbctemplate);
			if (!(plasticreceivedorders.isEmpty())) {
				setPlasticcardreceivedlist(plasticreceivedorders);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
			} else {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No Orders To Receive Plastic Cards ");
				setAct((String) session.getAttribute("ACTIONTYPE"));
				return personalPlasticcardreceivedhome();
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error While get the Platic Card Received orders " + e.getMessage());
			e.printStackTrace();
		}

		return "plasticcardreceivedorders";
	}

	/**
	 * This is to update pers_card_process tables status as card_status='04' and
	 * mkck_status='M'
	 * 
	 * @return
	 */
	public String confirmPlasticcardreceivedorders() {
		HttpSession session = getRequest().getSession();
		String actiontype = (String) session.getAttribute("ACTIONTYPE");
		IfpTransObj transact = commondesc.myTranObject("RECVORDER", txManager);
		//// System.out.println("Action Type is ----> "+actiontype);
		String instid = comInstId();
		String userid = comUserId();
		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		String authmsg = "";
		String update_qury = "";
		Personalizeorderdetails persorderdetails, bindetails, extradetails;

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		if (actiontype.equals("M")) {
			update_qury = " UPDATE PERS_CARD_PROCESS SET CARD_STATUS='04',RECV_DATE=(sysdate),MAKER_ID='" + userid
					+ "',MAKER_DATE=(sysdate),MKCK_STATUS='M' WHERE INST_ID='" + instid + "' ";
			authmsg = " Waiting For Authorization ";
		} else if (actiontype.equals("D")) {
			update_qury = "UPDATE PERS_CARD_PROCESS SET CARD_STATUS='04',RECV_DATE=(sysdate),MAKER_ID='" + userid
					+ "',MAKER_DATE=(sysdate),CHECKER_ID='" + userid
					+ "',CHECKER_DATE=(sysdate),MKCK_STATUS='P' WHERE INST_ID='" + instid + "'";
			authmsg = "";
		}

		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		int ordercount = order_refnum.length;
		int updatecount = 0;
		try {
			for (int i = 0; i < ordercount; i++) {
				int update_status = 0;
				//// System.out.println("Order Selected==> "+order_refnum[i]);
				String updatequry = update_qury + " AND ORG_CHN='" + order_refnum[i] + "'";
				//// System.out.println("UPdate Query is===> "+updatequry);
				enctrace("UPdate Query is===> " + updatequry);
				update_status = jdbctemplate.update(updatequry);
				if (update_status == 1) {
					updatecount = updatecount + 1;
				} else {
					break;
				}
			}
			if (ordercount == updatecount) {
				// commondesc.commitTxn(jdbctemplate);
				txManager.commit(transact.status);
				//// System.out.println("Card Status Updated Successfully and
				//// Commited ");
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", ordercount + " Card Status Updated Sucessfully. " + authmsg);

				// ---------------audit code edited by sardar on
				// 11-12-15---------//
				try {
					for (int i = 0; i < order_refnum.length; i++) {
						trace("---------------" + (i + 1) + " Card Processing..... Processing order [ CHN  ] : "
								+ order_refnum[i]);
						// persorderdetails =
						// commondesc.gettingOrderdetailsByCin(instid,
						// order_refnum[i].trim(),
						// jdbctemplate);

						String mcardno = commondesc.getMaskedCardNo(instid, order_refnum[i], "C", jdbctemplate);
						String cin = commondesc.getCinFromProcess(order_refnum[i], instid, "", jdbctemplate);
						if (mcardno == null) {
							mcardno = order_refnum[i];
						}

						// added by gowtham_220719
						trace("ip address======>  " + ip);
						auditbean.setActmsg(
								"Recieced card  " + mcardno + " Card Status Updated Sucessfully. " + authmsg);
						auditbean.setIpAdress(ip);
						auditbean.setCin(cin);
						// auditbean.setActmsg("Recieved Card [ " + mcardno + "
						// ] ");
						auditbean.setUsercode(username);
						auditbean.setAuditactcode("0105");
						auditbean.setCardno(mcardno);
						// auditbean.setCardnumber(order_refnum[i]);
						// commondesc.insertAuditTrail(in_name, Maker_id,
						// auditbean, jdbctemplate, txManager);
						commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
					}
				} catch (Exception audite) {
					audite.printStackTrace();
					trace("Exception in auditran : " + audite.getMessage());
				}

			}

			// --------------End on 11-12-15-------------//
			else {
				// commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				//// System.out.println("Txn Got Rollbacked ---->");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Card Status not updated ");
			}
		} catch (Exception e) {
			// commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			//// System.out.println("Error While Execute the Query
			//// ---->"+e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error While Update The Card Status: Error " + e.getMessage());
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
		 * order_refnum); trace("proc args-->"+array+"--"+instid+"--"+userid);
		 * cstmt.setString(1, table); cstmt.setArray(2, array);
		 * cstmt.setString(3, instid); // columns = " MKCK_STATUS='"+authstatus+
		 * "', REMARKS='"+remarks+"', CHECKER_ID='"
		 * +usercode+"',CHECKER_DATE=(sysdate)"; condition = " WHERE INST_ID='"
		 * +instid+"' AND CARD_NO IN"; cstmt.setString(4, update_qury);
		 * cstmt.setString(5, condition);
		 * cstmt.registerOutParameter(6,java.sql.Types.VARCHAR);
		 * cstmt.execute(); result=cstmt.getString(6);
		 * trace("result--->"+result);
		 * 
		 *//************* AUDIT BLOCK **************/
		/*
		 * try{ auditbean.setActmsg("Card  [ "+order_refnum.length+
		 * " ] Received Successfully "); auditbean.setActiontype("IC");
		 * auditbean.setUsercode(userid); auditbean.setAuditactcode("0102");
		 * commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate,
		 * txManager); }catch(Exception audite ){ trace(
		 * "Exception in auditran : "+ audite.getMessage()); }
		 *//************* AUDIT BLOCK **************//*
													 * 
													 * 
													 * if(result.contains(
													 * "successfully")){
													 * addActionMessage(
													 * "Cards Recieve "+result);
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

		setAct((String) session.getAttribute("ACTIONTYPE"));
		return personalPlasticcardreceivedhome();
	}

	public String personalAuthorizecardreceivedhome() {

		List pers_prodlist, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		HttpSession session = getRequest().getSession();
		String cardStatus = "04", mkrstatus = "M";
		try {
			if (usertype.equals("INSTADMIN")) {
				//// System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);

				if (!br_list.isEmpty()) {
					setBranch_list(br_list);
				}
			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				//// System.out.println("Product List is ===> "+pers_prodlist);
			} else {
				//// System.out.println("No Product Details Found ");
				/*
				 * session.setAttribute("curerr", "E");
				 * session.setAttribute("curmsg"," No Product Details Found ");
				 */
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
		}

		return "authorizecardreceivedhome";
	}

	private List plasticreceivedlist;

	public List getPlasticreceivedlist() {
		return plasticreceivedlist;
	}

	public void setPlasticreceivedlist(List plasticreceivedlist) {
		this.plasticreceivedlist = plasticreceivedlist;
	}

	public String authorizePlasticcardreceivedorders() {
		HttpSession session = getRequest().getSession();

		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String prefilename = getRequest().getParameter("prefilename");

		//// System.out.println("branch==> "+branch+" binno==>"+binno+"
		//// fromdate===> "+fromdate+" todate===>"+todate);
		String inst_id = comInstId(), dateflag = "RECV_DATE", cardstatus = "04", mkckstatus = "M", cafstatus = "N";
		List authplasticcardlist = null;

		try {
			String condition = commondesc.filterConditionWithPreFile(binno, branch, prefilename, fromdate, todate,
					dateflag);
			//// System.out.println("Condition Value-----> "+condition);
			// authplasticcardlist = commondesc.personaliseCardauthlist(
			//// inst_id,cardstatus, mkckstatus,condition);
			authplasticcardlist = commondesc.maintenanceCardslist(inst_id, cardstatus, mkckstatus, cafstatus, condition,
					jdbctemplate);

			// authplasticcardlist =
			// commondesc.personaliseCardauthlist(dateflag, inst_id, fromdate,
			// todate, cardstatus, mkckstatus, branch, binno);
			if (!(authplasticcardlist.isEmpty())) {
				setPlasticreceivedlist(authplasticcardlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
			} else {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No Plastic Card Orders to Authorize ");
				return personalAuthorizecardreceivedhome();
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error While get Orders " + e.getMessage());
		}

		return "authplasticcardreceivedorder";
	}

	public String receivedPlasticcardsauthorize() {
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("RECVAUTH", txManager);
		String instid = comInstId();
		String usercode = comUserId();
		String username = comUsername();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		String remarks = getRequest().getParameter("reason");
		System.out.println("remarks--->"+remarks);
		String authcode =getRequest().getParameter("authorize");
		String deauthcode =getRequest().getParameter("deauthorize");
		System.out.println("authcode--->"+authcode);
		System.out.println("deauthcode--->"+deauthcode);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		//// System.out.println("Total Orders Selected ===>
		//// "+order_refnum.length);
		if (getRequest().getParameter("authorize") != null) {
			//// System.out.println( "AUTHORIZE..........." );
			authstatus = "P";
			statusmsg = " Authorized ";
			err_msg = "Authorize";
		} else if (getRequest().getParameter("deauthorize") != null) { 
			//// System.out.println( "DE AUTHORIZE..........." );
			authstatus = "D";
			statusmsg = " De-Authorized ";
			err_msg = "De-Authorize";
		}

		try {
			int check = 0;
			int cardcnt = 0;
			int ordercount = order_refnum.length;
			for (int i = 0; i < ordercount; i++) {

				trace("cardno is ====> " + order_refnum[i]);
				int update_cnt = 0;
				/*
				 * String update_authdeauth_qury =
				 * "UPDATE PERS_CARD_PROCESS SET MKCK_STATUS='" + authstatus +
				 * "', REMARKS='" + remarks + "', CHECKER_ID='" + usercode +
				 * "',CHECKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' ";
				 * 
				 * String updatequry = update_authdeauth_qury + " AND ORG_CHN='"
				 * + order_refnum[i] + "'"; enctrace("updatequry :" +
				 * updatequry); check = jdbctemplate.update(updatequry);
				 */

				// by gowtham-270819
				System.out.println("recieve desuthorize-->"+"authstatus-- "+authstatus+"remarks-- "+remarks);
				String update_authdeauth_qury = "UPDATE PERS_CARD_PROCESS SET MKCK_STATUS=?, REMARKS=?, CHECKER_ID=?,CHECKER_DATE=(sysdate) WHERE INST_ID=? ";
				String updatequry = update_authdeauth_qury + " AND ORG_CHN=?";
				enctrace("updatequry :" + updatequry);
				check = jdbctemplate.update(updatequry,
						new Object[] { authstatus, remarks, usercode, instid, order_refnum[i] });

				if (check > 0) {
					// update_cnt = update_cnt + 1;
					cardcnt++;

				} else {
					addActionError("Unable to continue process");
					break;
				}

				/************* AUDIT BLOCK **************/
				try {
					String mcardno = commondesc.getMaskedCardNo(instid, order_refnum[i], "C", jdbctemplate);
					String cin = commondesc.getCinFromProcess(order_refnum[i], instid, "", jdbctemplate);
					if (mcardno == null) {
						mcardno = order_refnum[i];
					}

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);
					auditbean.setCin(cin);
					auditbean.setActmsg("Recieved Card  " + statusmsg + " [ " + mcardno + " ] ");
					auditbean.setUsercode(usercode);
					auditbean.setAuditactcode("0105");
					auditbean.setCardno(mcardno);
					auditbean.setRemarks(remarks);
					// auditbean.setCardnumber(order_refnum[i]);
					// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					audite.printStackTrace();
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			}

			trace("commit check issue auth ::" + cardcnt + ordercount);
			if (cardcnt == ordercount) {
				// commondesc.commitTxn(jdbctemplate);
				txManager.commit(transact.status);
				//// System.out.println("Order Status Updated Successfully and
				//// Commited ");
				session.setAttribute("preverr", "S");
				
				if(authstatus.equals("D")){
				session.setAttribute("prevmsg", ordercount + " Card De-Authorized Successfully");}
				else{
				session.setAttribute("prevmsg", ordercount + " Card Authorized Successfully and Waiting For Issuance");}
					
			} else {
				// commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				//// System.out.println("Txn Got Rollbacked ---->");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Orders not updated ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			//// System.out.println("Txn Got Rollbacked ---->");
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error While Authorize " + e.getMessage());
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
		 * cstmt.setString(3, instid); columns = " MKCK_STATUS='"+authstatus+
		 * "', REMARKS='"+remarks+"', CHECKER_ID='"
		 * +usercode+"',CHECKER_DATE=(sysdate)"; condition = " WHERE INST_ID='"
		 * +instid+"' AND CARD_NO IN"; cstmt.setString(4, columns);
		 * cstmt.setString(5, condition);
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
													 * addActionMessage(
													 * "Cards  "+result+
													 * "  Authorized."); }else{
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

		return personalAuthorizecardreceivedhome();
	}

	public String personalCardissuehome() {
		//// System.out.println("The User is---->"+act+" Type user");
		List pers_prodlist, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String cardStatus = "04", mkrstatus = "P";
		HttpSession session = getRequest().getSession();
		act = getRequest().getParameter("act");

		try {
			if (usertype.equals("INSTADMIN")) {
				//// System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				if (!(br_list.isEmpty())) {
					setBranch_list(br_list);
					trace("Branch list is not empty");
				}

			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				//// System.out.println("Product List is ===> "+pers_prodlist);
			} else {
				//// System.out.println("No Product Details Found ");
				/*
				 * session.setAttribute("curerr", "E");
				 * session.setAttribute("curmsg"," No Product Details Found ");
				 */
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
		}
		return "cardissuehome";
	}

	private List cardissueorderlist;

	public List getCardissueorderlist() {
		return cardissueorderlist;
	}

	public void setCardissueorderlist(List cardissueorderlist) {
		this.cardissueorderlist = cardissueorderlist;
	}

	public String getCardissuenceorders() {

		HttpSession session = getRequest().getSession();

		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		//// System.out.println("branch==> "+branch+" binno==>"+binno+"
		//// fromdate===> "+fromdate+" todate===>"+todate);
		List cardissueorders = null;
		String instid = comInstId();
		String dateflag = "RECV_DATE", cardstatus = "04", mkckstatus = "P", cafstatus = "A";
		try {

			String couriertrackenabled = commondesc.getCourierTrackEnabled(instid, jdbctemplate);
			trace("Checking couriertrackenabled .. got :" + couriertrackenabled);
			if (couriertrackenabled.equals("1")) {
				iscouriertrackenabled = true;

				/*
				 * String courierlistqry =
				 * "SELECT COURIERMASTER_ID, COURIER_NAME FROM IFP_COURIER_MASTER WHERE INST_ID='"
				 * + instid + "' AND COURIER_STATUS ='1'"; enctrace(
				 * "courierlistqry  :" + courierlistqry); List courierlist =
				 * jdbctemplate.queryForList(courierlistqry);
				 */

				String courierlistqry = "SELECT COURIERMASTER_ID, COURIER_NAME FROM IFP_COURIER_MASTER WHERE INST_ID=? AND COURIER_STATUS =?";
				enctrace("courierlistqry  :" + courierlistqry);
				List courierlist = jdbctemplate.queryForList(courierlistqry, new Object[] { instid, "1" });

				setCourierlist(courierlist);
			}

			String condition = commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			//// System.out.println("Condition Value-----> "+condition);
			// cardissueorders = commondesc.personaliseCardauthlist(
			//// instid,cardstatus, mkckstatus,condition);
			cardissueorders = commondesc.maintenanceCardslist(instid, cardstatus, mkckstatus, cafstatus, condition,
					jdbctemplate);
			// cardissueorders = commondesc.personaliseCardauthlist(dateflag,
			// instid, fromdate, todate, cardstatus, mkckstatus, branch, binno);
			//// System.out.println("cardissueorders=====> "+cardissueorders);
			// wait

			if (!(cardissueorders.isEmpty())) {
				setCardissueorderlist(cardissueorders);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
			} else {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No Card Issuence Orders Found ");
				setAct((String) session.getAttribute("ACTIONTYPE"));
				System.err.println("This is Erorr SANKAR Written");
				return personalCardissuehome();
			}

		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error While get the Card Issue orders " + e.getMessage());
		}
		return "cardissuenceorders";
	}

	public synchronized String confirmCardissueorders() {
		trace("*****confirmCardissueorders*****");
		enctrace("*****confirmCardissueorders*****");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("CONFIRMISSUE", txManager);
		String actiontype = (String) session.getAttribute("ACTIONTYPE");
		//// System.out.println("Action Type is ----> "+actiontype);
		String instid = comInstId();
		String userid = comUserId();
		String username = comUsername();
		String cardstatus = CARDACTIVATEDCODE;
		String switchstatus = commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
		String update_qury = "";

		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		int ordercount = order_refnum.length;
		int updatecount = 0;
		int personalcardissue = 0;
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		PadssSecurity padsssec = new PadssSecurity();
		String eDMK = "", eDPK = "",cdpd="",clearCardNumber="NA",CDPK="";
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		try {
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			Iterator secitr = secList.iterator();
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				eDMK = ((String) map.get("DMK"));
				eDPK = ((String) map.get("DPK"));
				CDPK = padsssec.decryptDPK(eDMK, EDPK);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return personalCardissuehome();
		}
		
		String cafflag = "";
		String hcardNumber = "";
		int update_status = 0, del_process = 0, del_process1 = 0, del_pin = 0, update_product = 0, updezcardinfo = 0;
		try {
			for (int i = 0; i < ordercount; i++) {
				clearCardNumber = padsssec.getCHN(CDPK, order_refnum[i]);

				hcardNumber = commondesc.getHcardNo(order_refnum[i], jdbctemplate);

				/* hcardNumber=padsssec.getHashedValue(cardno + instid); */

				trace("card number is=====>  " + order_refnum[i]);
				cafflag = commondesc.gettingCAFstatus(order_refnum[i], padssenable, instid, jdbctemplate);
				trace("CAF REC STATUS : " + cafflag);
				trace("hashedacardbo:::" + order_refnum[i]);

				if (cafflag.equals("D")) {
					trace("DAMAGE CARD PART...................");
					StringBuilder update_production = new StringBuilder();

					update_production.append(
							"UPDATE CARD_PRODUCTION SET CARD_STATUS='" + cardstatus + "', STATUS_CODE='" + switchstatus
									+ "',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"
									+ userid + "', ");
					update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' ");

					if (padssenable.equals("Y")) {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' ");
					} else {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' ");
					}

					enctrace("update_production :" + update_production.toString());
					update_product = jdbctemplate.update(update_production.toString());

					StringBuilder updEzcard = new StringBuilder();

					
					 updEzcard.append("UPDATE EZCARDINFO set ");
					  updEzcard.append("PANSEQNO = '00' "); 
					  updEzcard.append(",STATUS = '" + switchstatus + "' WHERE CHN = '" +hcardNumber + "' AND INSTID='"+ instid + "'");
					  
					  trace("updEzcard :" + updEzcard.toString());
					  updezcardinfo = jdbctemplate.update(updEzcard.toString());
					 

				/*	// by gowtham-270819
					updEzcard.append("UPDATE EZCARDINFO set ");
					updEzcard.append("PANSEQNO = '00' ");
					updEzcard.append(",STATUS = ? WHERE CHN = ? AND INSTID=?");

					trace("updEzcard :" + updEzcard.toString());
					updezcardinfo = jdbctemplate.update(updEzcard.toString(),
							new Object[] { "00", switchstatus, hcardNumber, instid });

*/					StringBuilder deletefromProcess = new StringBuilder();
					StringBuilder deletefromProcess1 = new StringBuilder();
					/*
					 * deletefromProcess.append(
					 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid
					 * + "' AND ORG_CHN='" + order_refnum[i] + "' ");
					 * deletefromProcess1.append(
					 * "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" +
					 * instid +
					 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN='"
					 * + order_refnum[i] + "') ");
					 */

					// by gowtham-270819
					deletefromProcess.append("DELETE FROM PERS_CARD_PROCESS WHERE INST_ID=? AND ORG_CHN=? ");
					deletefromProcess1.append(
							"DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID=? AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN=?) ");

					/*
					 * if (padssenable.equals("Y")) { deletefromProcess.append(
					 * "AND ORG_CHN='" + order_refnum[i] + "' "); } else {
					 * deletefromProcess.append("AND ORG_CHN='" +
					 * order_refnum[i] + "' "); }
					 */

					enctrace("1-deletefromProcess::::::::" + deletefromProcess.toString());
					enctrace("2-deletefromProcess::::::::" + deletefromProcess1.toString());
					/*
					 * trace("update_product :" + update_product +
					 * " del_process :" + del_process); del_process1 =
					 * jdbctemplate.update(deletefromProcess1.toString());
					 * del_process =
					 * jdbctemplate.update(deletefromProcess.toString());
					 */

					// by gowtham-270819
					trace("update_product :" + update_product + " del_process :" + del_process);
					del_process1 = jdbctemplate.update(deletefromProcess1.toString(),
							new Object[] { instid, order_refnum[i] });
					del_process = jdbctemplate.update(deletefromProcess.toString(),
							new Object[] { instid, order_refnum[i] });

					// del_pin = jdbctemplate.update(delete_pin);

					if (update_product > 0 && del_process > 0 && del_process > 0 && updezcardinfo > 0) {
						String mifpcode = "DAMAGE";
						updatecount = updatecount + 1;
					} else {
						//// System.out.println("Process Breaked ====> 1");
						addActionError("Unable to Continue process");
						break;
					}

					trace("..................DAMAGE CARD PART");

				}
				/// DAMAGE WITH EXPIRY DATE
				if (cafflag.equals("DE")) {

					trace("DAMAGE CARD ATE WITH EXPIRY DATE...................");

					StringBuilder update_production = new StringBuilder();
					update_production.append(
							"UPDATE CARD_PRODUCTION SET CARD_STATUS='" + cardstatus + "', STATUS_CODE='" + switchstatus
									+ "',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"
									+ userid + "', ");
					update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' ");
					if (padssenable.equals("Y")) {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' AND INST_ID='" + instid + "'");
					} else {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' AND INST_ID='" + instid + "'");
					}

					trace("update_production :" + update_production.toString());
					update_product = jdbctemplate.update(update_production.toString());

					StringBuilder updEzcard = new StringBuilder();

					/*
					 * updEzcard.append("UPDATE EZCARDINFO set ");
					 * updEzcard.append(
					 * "EXPIRYDATE = (SELECT EXPIRY_DATE from PERS_CARD_PROCESS where "
					 * ); updEzcard.append("ORG_CHN='" + order_refnum[i] +
					 * "' AND INSTID='" + instid + "')"); updEzcard.append(
					 * ",STATUS = '" + switchstatus +
					 * "',PANSEQNO = '00'  WHERE CHN = '" + hcardNumber +
					 * "' AND INSTID='" + instid + "'");
					 * 
					 * trace("updEzcard :" + updEzcard.toString());
					 * updezcardinfo =
					 * jdbctemplate.update(updEzcard.toString());
					 */

					// by gowtham-270819
					updEzcard.append("UPDATE EZCARDINFO set ");
					updEzcard.append("EXPIRYDATE = (SELECT EXPIRY_DATE from PERS_CARD_PROCESS where ");
					updEzcard.append("ORG_CHN=? AND INSTID=?)");
					updEzcard.append(",STATUS = ?,PANSEQNO = ?  WHERE CHN =? AND INSTID=?");

					trace("updEzcard :" + updEzcard.toString());
					updezcardinfo = jdbctemplate.update(updEzcard.toString(),
							new Object[] { order_refnum[i], instid, switchstatus, "00", hcardNumber, instid });

					StringBuilder deletefromProcess = new StringBuilder();
					StringBuilder deletefromProcess1 = new StringBuilder();

					/*
					 * deletefromProcess.append(
					 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid
					 * + "' AND ORG_CHN='" + order_refnum[i] + "' ");
					 * deletefromProcess1.append(
					 * "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" +
					 * instid +
					 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN='"
					 * + order_refnum[i] + "') ");
					 */

					// BY GOWTHAM-270819
					deletefromProcess.append("DELETE FROM PERS_CARD_PROCESS WHERE INST_ID=? AND ORG_CHN=? ");
					deletefromProcess1.append(
							"DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID=? AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN=?) ");

					// by siva 18-07-2019
					/*
					 * if (padssenable.equals("Y")) { deletefromProcess.append(
					 * "AND ORG_CHN='" + order_refnum[i] + "' "); } else {
					 * deletefromProcess.append("AND ORG_CHN='" +
					 * order_refnum[i] + "' "); }
					 */
					/*
					 * trace("update_product :" + update_product +
					 * " del_process :" + del_process); del_process1 =
					 * jdbctemplate.update(deletefromProcess1.toString());
					 * del_process =
					 * jdbctemplate.update(deletefromProcess.toString());
					 */
					
					

					// BY GOWTHAM-270819
					trace("update_product :" + update_product + " del_process :" + del_process);
					del_process1 = jdbctemplate.update(deletefromProcess1.toString(),
							new Object[] { instid, order_refnum[i] });
					del_process = jdbctemplate.update(deletefromProcess.toString(),
							new Object[] { instid, order_refnum[i] });

					// del_pin = jdbctemplate.update(delete_pin);

					if (update_product > 0 && del_process > 0 && del_process1 > 0 && updezcardinfo > 0) {
						String mifpcode = "DAMAGE";
						updatecount = updatecount + 1;
					} else {
						//// System.out.println("Process Breaked ====> 1");
						addActionError("Unable to continue process");
						break;
					}

					trace("..................DAMAGE CARD PART");

				}

				else if (cafflag.equals("A")) {

					//// System.out.println("############################################################################"+cafflag);
					StringBuilder update_production = new StringBuilder();
					update_production.append("UPDATE PERS_CARD_PROCESS SET CARD_STATUS='" + cardstatus
							+ "', STATUS_CODE='" + switchstatus
							+ "',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='" + userid
							+ "', ");
					update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' ");
					if (padssenable.equals("Y")) {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' ");
					} else {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' ");
					}
					int upd_process = jdbctemplate.update(update_production.toString());

					String tablename = "PERS_CARD_PROCESS";

					try {
						personalcardissue = personalCardIssuence(order_refnum[i], padssenable, instid, userid,
								tablename, hcardNumber, jdbctemplate);
						/*
						 * int update =
						 * cbsTableUpdateDetails(order_refnum[i],padssenable,
						 * instid,userid,padsssec,eDMK,eDPK,cafflag,jdbctemplate
						 * ); if(update == 1){ enctrace(
						 * " ----NEW CARD - CBS UPDATE SUCCESS----"); }else{
						 * enctrace(" ----NEW CARD - CBS UPDATE FAIL----"); }
						 */

					} catch (Exception e) {
						txManager.rollback(transact.status);
						trace("Exception in personalcardissue::::" + e);
						e.printStackTrace();
						break;
					}
					trace("Issuing the card....got : " + personalcardissue);

					if (personalcardissue > 0) {
						updatecount = updatecount + 1;

					} else {
						trace("Process Breaked ====> in CAF REC STATUS A 2");
						addActionError("Unable to Continue...");
						txManager.rollback(transact.status);
						return personalCardissuehome();
					}
					//// System.out.println("############################################################################");
				}

				else if (cafflag.equals("S")) {
					PadssSecurity sec = new PadssSecurity();

					enctrace("##############REISSSUE  CARD ###################"+order_refnum[i]);
					trace("####### REISSSUE CARD ###" + order_refnum[i]);

					// BY SIVA 18-07-2019

					String padsscond = "";
					if (padssenable.equals("Y")) {
						padsscond = "ORG_CHN='" + order_refnum[i] + "'";
					} else {
						padsscond = "ORG_CHN='" + order_refnum[i] + "'";
					}

					String custcin = commondesc.fchCustomerId(instid, padssenable, order_refnum[i], "PERS_CARD_PROCESS",
							jdbctemplate);
					trace("Got the customer id : " + custcin);
					//// System.out.println("CUSTOMERN NUMBER IS =====>
					//// "+custcin);
					int res_1 = 0;
					int res_2 = 0;
					int res_3 = 0;
					int res_4 = 0;
					int res_5 = 0;

					String deletefromProcess = "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid
							+ "' AND ORG_CHN='" + order_refnum[i] + "' ";
					String deletefromProcess1 = "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" + instid
							+ "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN='"
							+ order_refnum[i] + "') ";

					enctrace("deletefromProcess1" + deletefromProcess1);
					enctrace("deletefromProcess" + deletefromProcess);
					String reissuedate = "''", reissue_count = "0", repindate = "''", repincount = "0",
							damgedate = "''", blockdate = "''", hotdate = "''", closedte = "''", pinretry_count = "0";
					String active_date = "", USEDCHN = "";
					StringBuffer usedhcardno = new StringBuffer();

					String status_code = commondesc.getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate);
					/*
					 * String movetoproduction =
					 * "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,"
					 * +
					 * "EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
					 * +
					 * "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH)"
					 * +
					 * "(SELECT INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"
					 * + CARDACTIVATEDCODE + "',CAF_REC_STATUS," + status_code +
					 * ",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"
					 * + "PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'" +
					 * userid +
					 * "',(SYSDATE),CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
					 * +
					 * "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"
					 * + reissuedate + ",'" + reissue_count + "'," + repindate +
					 * ",'" + repincount + "'," + damgedate + "," + blockdate +
					 * "," + hotdate + "," + closedte + ",PIN_OFFSET,'" +
					 * pinretry_count +
					 * "',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH FROM PERS_CARD_PROCESS "
					 * + "WHERE INST_ID='" + instid + "' AND " + padsscond +
					 * ")"; enctrace("movetoproduction---- > " +
					 * movetoproduction);
					 */

					String INSTID_1 = "", CHN_1 = "", ACCOUNTNO_1 = "", ACCOUNTTYPE_1 = "", ACCOUNTFLAG_1 = "",
							ACCOUNTPRIORITY_1 = "", CURRCODE_1 = "";
					String INSTID_3 = "", CHN_3 = "", CARDTYPE_3 = "", CUSTID_3 = "", TXNGROUPID_3 = "",
							LIMITFLAG_3 = "", EXPIRYDATE_3 = "", STATUS = "", PINOFFSET_3 = "", OLDPINOFFSET_3 = "",
							TPINOFFSET_3 = "", OLDTPINOFFSET_3 = "", PINRETRYCOUNT_3 = "", TPINRETRYCOUNT_3 = "",
							PVKI_3 = "", LASTTXNDATE_3 = "", LASTTXNTIME_3 = "", PANSEQNO_3 = "";

					StringBuilder mv = new StringBuilder();

					mv.append("SELECT ");
					// --EZAUTHREL start
					// --INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG,
					// ACCOUNTPRIORITY, CURRCODE,
					mv.append("'" + instid + "' INSTID_1, '" + order_refnum[i]
							+ "' CHN_1,PCP.ACCT_NO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
					// --EZAUTHREL end

					// -- EZCARDINFO start
					// --INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG,
					// EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET,
					// OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI,
					// LASTTXNDATE, LASTTXNTIME, PANSEQNO
					if (padssenable.equals("Y")) {
						mv.append("'" + instid
								+ "' INSTID_3, PCP.ORG_CHN CHN_3,PCP.USED_CHN , PCP.CARD_TYPE_ID CARDTYPE_3,'" + custcin
								+ "' CUSTID_3,'01' TXNGROUPID_3, ");
					} else {
						mv.append("'" + instid
								+ "' INSTID_3, PCP.ORG_CHN CHN_3,PCP.USED_CHN, PCP.CARD_TYPE_ID CARDTYPE_3,'" + custcin
								+ "' CUSTID_3,'01' TXNGROUPID_3, ");
					}
					mv.append(
							"(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
					mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'" + status_code
							+ "' STATUS , NVL(PCP.PIN_OFFSET,0)  PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, ");
					mv.append(
							"'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
					mv.append(
							"TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3 ");
					// -- EZCARDINFO end

					mv.append(" FROM CUSTOMERINFO CI ,ACCOUNTINFO AI ,PERS_CARD_PROCESS PCP ");
					mv.append("WHERE ");
					// mv.append("WHERE (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND
					// AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO =
					// CI.ORDER_REF_NO) AND ");
					mv.append(
							"(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN and pcp.acct_no=ai.ACCOUNTNO) AND ");
					mv.append("(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID) ");
					mv.append("AND CI.INST_ID='" + instid + "' AND AI.INST_ID='" + instid + "' AND PCP.INST_ID='"
							+ instid + "'  ");
					mv.append("AND CI.CIN='" + custcin + "' AND AI.CIN='" + custcin + "' AND PCP.CIN='" + custcin
							+ "' AND " + padsscond + " ");

					enctrace("Move to Production CafRec status SS-----------------------------------\n");
					enctrace(mv.toString());

					List movetoSwitchP = jdbctemplate.queryForList(mv.toString());
					Iterator custitr = movetoSwitchP.iterator();
					while (custitr.hasNext()) {
						Map mp = (Map) custitr.next();
						// fname = (String)mp.get("FNAME");
						INSTID_1 = (String) mp.get(" INSTID_1 ");
						CHN_1 = (String) mp.get("CHN_1");
						ACCOUNTNO_1 = (String) mp.get("ACCOUNTNO_1");
						ACCOUNTTYPE_1 = (String) mp.get("ACCOUNTTYPE_1");
						ACCOUNTFLAG_1 = (String) mp.get("ACCOUNTFLAG_1");
						ACCOUNTPRIORITY_1 = (String) mp.get("ACCOUNTPRIORITY_1");
						CURRCODE_1 = (String) mp.get("CURRCODE_1");
						USEDCHN = (String) mp.get("USED_CHN");

						CHN_3 = mp.get("CHN_3").toString();
						CARDTYPE_3 = mp.get("CARDTYPE_3").toString();
						CUSTID_3 = mp.get("CUSTID_3").toString();
						TXNGROUPID_3 = mp.get("TXNGROUPID_3").toString();
						LIMITFLAG_3 = mp.get("LIMITFLAG_3").toString();
						EXPIRYDATE_3 = mp.get("EXPIRYDATE_3").toString();
						STATUS = mp.get("STATUS").toString();
						PINOFFSET_3 = mp.get("PINOFFSET_3").toString();
						OLDPINOFFSET_3 = mp.get("OLDPINOFFSET_3").toString();
						TPINOFFSET_3 = mp.get("TPINOFFSET_3").toString();
						OLDTPINOFFSET_3 = mp.get("OLDTPINOFFSET_3").toString();
						PINRETRYCOUNT_3 = mp.get("PINRETRYCOUNT_3").toString();
						TPINRETRYCOUNT_3 = mp.get("TPINRETRYCOUNT_3").toString();
						PVKI_3 = mp.get("PVKI_3").toString();
						LASTTXNDATE_3 = mp.get("LASTTXNDATE_3").toString();
						LASTTXNTIME_3 = mp.get("LASTTXNTIME_3").toString();
						PANSEQNO_3 = mp.get("PANSEQNO_3").toString();
					}

					// usedhcardno = sec.getHashedValue(USEDCHN+instid);
					trace("USEDCHN ::: "+USEDCHN);
					usedhcardno = padsssec.getHashedValue(USEDCHN + instid);
					trace("USEDCHN     " + USEDCHN + "        usedhcardno----->  " + usedhcardno + "     CHN_3"
							+ hcardNumber + "order_refnum[i]" + order_refnum[i]);

					// // added by prasad
					// Getting old hcardnumber

					/*
					 * String queryForGettingOldHashCardNo=
					 * "SELECT HCARD_NO FROM CARD_PRODUCTION_HASH  WHERE ORDER_REF_NO=(SELECT ORDER_REF_NO FROM CARD_PRODUCTION "
					 * + " WHERE USED_CHN='"+USEDCHN+"')"; enctrace(
					 * " query --->  "+queryForGettingOldHashCardNo);
					 * 
					 * String oldHcardNumber= (String)
					 * jdbctemplate.queryForObject(queryForGettingOldHashCardNo,
					 * String.class); trace("oldHcardNumber is ---->  "
					 * +oldHcardNumber);
					 */

					// by gowtham-270819
				//	String queryForGettingOldHashCardNo = "SELECT HCARD_NO FROM CARD_PRODUCTION_HASH  WHERE ORDER_REF_NO=(SELECT ORDER_REF_NO FROM CARD_PRODUCTION "
				//			+ " WHERE USED_CHN='"+USEDCHN+"')";
				//	enctrace(" query --->  " + queryForGettingOldHashCardNo);

					//String oldHcardNumber = (String) jdbctemplate.queryForObject(queryForGettingOldHashCardNo,new Object[] { USEDCHN }, String.class);
					
					//String  oldHcardNumber=(String) jdbctemplate.queryForObject(queryForGettingOldHashCardNo,String.class);
					//trace("oldHcardNumber is ---->  " + oldHcardNumber);

					String movetoproduction = "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,"
							+ "EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
							+ "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH)"
							+ "(SELECT INST_ID,ORDER_REF_NO,BIN, MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"
							+ CARDACTIVATEDCODE + "',CAF_REC_STATUS," + status_code
							+ ",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"
							+ "PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'" + userid
							+ "',(SYSDATE),CHECKER_ID,CHECKER_DATE,'P',SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
							+ "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0'," + reissuedate + ",'"
							+ reissue_count + "'," + repindate + ",'" + repincount + "'," + damgedate + "," + blockdate
							+ "," + hotdate + "," + closedte + ",PIN_OFFSET,'" + pinretry_count
							+ "',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH FROM PERS_CARD_PROCESS "
							+ "WHERE INST_ID='" + instid + "' AND " + padsscond + ")";
					enctrace("movetoproduction---- > " + movetoproduction);

					String movetoproduction1 = "INSERT INTO CARD_PRODUCTION_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,"
							+ "PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)  (SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,"
							+ "CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE, " + userid
							+ ",(SYSDATE),CHECKER_ID,CHECKER_DATE FROM " + "PERS_CARD_PROCESS_HASH WHERE INST_ID='"
							+ instid + "'AND HCARD_NO='" + hcardNumber + "')";

					trace("movetoproduction1 query---->   " + movetoproduction1);

					// StringBuilder crdinf_3 = new StringBuilder();

					// crdinf_3.append("INSERT INTO EZCARDINFO ");
					/*
					 * crdinf_3.append(
					 * "(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) "
					 * ); crdinf_3.append("VALUES "); crdinf_3.append("('" +
					 * instid + "','" + hcardNumber + "','" + CARDTYPE_3 + "','"
					 * + CUSTID_3 + "','" + TXNGROUPID_3 + "','" + LIMITFLAG_3 +
					 * "',TO_DATE('" + EXPIRYDATE_3 + "','MM/DD/YYYY'),'" +
					 * STATUS + "','" + PINOFFSET_3 + "','" + OLDPINOFFSET_3 +
					 * "',"); crdinf_3.append("'" + TPINOFFSET_3 + "','" +
					 * OLDTPINOFFSET_3 + "','" + PINRETRYCOUNT_3 + "','" +
					 * TPINRETRYCOUNT_3 + "','" + PVKI_3 + "',TO_DATE('" +
					 * LASTTXNDATE_3 + "','MM/DD/YYYY'),'" + LASTTXNTIME_3 +
					 * "'  ,'0' ,"); crdinf_3.append("'" + PANSEQNO_3 + "' )");
					 * 
					 * enctrace("crdinf_3:::::" + crdinf_3.toString());
					 * 
					 * String updaterefnoauthrel =
					 * "INSERT INTO  EZAUTHREL (INSTID,CHN,ACCOUNTNO,ACCOUNTTYPE,ACCOUNTFLAG,ACCOUNTPRIORITY,CURRCODE,ACCTSUBTYPE,CHNFLAG)"
					 * + " SELECT INSTID,'" + hcardNumber +
					 * "',ACCOUNTNO,ACCOUNTTYPE,ACCOUNTFLAG,ACCOUNTPRIORITY,CURRCODE,ACCTSUBTYPE,CHNFLAG FROM EZAUTHREL WHERE INSTID='"
					 * + instid + "' AND CHN='" + oldHcardNumber + "'  ";
					 */

					// by gowtham-270819
					StringBuilder crdinf_3 = new StringBuilder();
					crdinf_3.append("INSERT INTO EZCARDINFO ");
					crdinf_3.append(
							"(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
					crdinf_3.append("VALUES ");
					crdinf_3.append("(?,?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?,?,?,");
					crdinf_3.append("?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?  ,? ,");
					crdinf_3.append("? )");

					enctrace("crdinf_3:::::" + crdinf_3.toString());

					//String updaterefnoauthrel = "INSERT INTO  EZAUTHREL (INSTID,CHN,ACCOUNTNO,ACCOUNTTYPE,ACCOUNTFLAG,ACCOUNTPRIORITY,CURRCODE,ACCTSUBTYPE,CHNFLAG)"
						//	+ " SELECT INSTID,?,ACCOUNTNO,ACCOUNTTYPE,ACCOUNTFLAG,ACCOUNTPRIORITY,CURRCODE,ACCTSUBTYPE,CHNFLAG FROM EZAUTHREL WHERE INSTID=? AND CHN=?  ";

					 StringBuilder authrel_1 = new StringBuilder();
					 authrel_1.append("INSERT INTO EZAUTHREL ");
                   authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE,ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
					authrel_1.append("VALUES ");
					authrel_1.append("('"+instid+"','"+hcardNumber+"','"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+CURRCODE_1+"') ");
					enctrace("authrel_1::::" + authrel_1.toString());

					/*
					 * res_3 = jdbctemplate.update(crdinf_3.toString());
					 * 
					 * res_1 =
					 * jdbctemplate.update(updaterefnoauthrel.toString());
					 */

					res_3 = jdbctemplate.update(crdinf_3.toString(),
							new Object[] { instid, hcardNumber, CARDTYPE_3, CUSTID_3, TXNGROUPID_3, LIMITFLAG_3,
									EXPIRYDATE_3, STATUS, PINOFFSET_3, OLDPINOFFSET_3, TPINOFFSET_3, OLDTPINOFFSET_3,
									PINRETRYCOUNT_3, TPINRETRYCOUNT_3, PVKI_3, LASTTXNDATE_3, LASTTXNTIME_3, "0",
									PANSEQNO_3 });

					res_1=jdbctemplate.update(authrel_1.toString());
					///res_1 = jdbctemplate.update(updaterefnoauthrel.toString(),
						// Object[] { hcardNumber, instid, oldHcardNumber });

					int production_insert = jdbctemplate.update(movetoproduction);
					int production_insert1 = jdbctemplate.update(movetoproduction1);
					int deletefromprocess1 = jdbctemplate.update(deletefromProcess1);
					int deletefromprocess = jdbctemplate.update(deletefromProcess);

					enctrace("\n--------------------------------------Move to Production Query CAF REC S");
					trace("result :::::::::::" + res_1 + res_3 + production_insert + deletefromprocess);
					if (res_1 > 0 && res_3 > 0 && production_insert > 0 && deletefromprocess > 0
							&& deletefromprocess1 > 0 && production_insert1 > 0) {
						trace("PROCESS COMPLETED");
						updatecount = updatecount + 1;
					} else {
						//// System.out.println("Error While Insert and Delete
						//// ");

						trace("error while inserting into process ");
						addActionError("Unable to continue process");
						break;
					}

					/*
					 * int update =
					 * cbsTableUpdateDetails(order_refnum[i],padssenable,instid,
					 * userid,padsssec,eDMK,eDPK,cafflag,jdbctemplate);
					 * 
					 * if(update == 1){ enctrace(
					 * " ----REISSSUE CARD - CBS UPDATE SUCCESS----"); }else{
					 * enctrace(" ----REISSSUE CARD - CBS UPDATE FAIL----"); }
					 */
				}

				else if (cafflag.equals("AC")) {

					enctrace("###########ADD ON  CARD #####################"+order_refnum[i]);
					trace("####### ADD ON  CARD ###" + order_refnum[i]);

					String custcin = commondesc.fchCustomerId(instid, padssenable, order_refnum[i], "PERS_CARD_PROCESS",
							jdbctemplate);
					trace("Got the customer id : " + custcin);
					//// System.out.println("CUSTOMERN NUMBER IS =====>
					//// "+custcin);

					int res_1 = 0;
					int res_2 = 0;
					int res_3 = 0;
					int res_4 = 0;
					int res_5 = 0;

					String deletefromProcess = "";
					String deletefromProcess1 = "";

					/*
					 * if (padssenable.equals("Y")) { deletefromProcess =
					 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid
					 * + "' AND ORG_CHN='" + order_refnum[i] + "' "; } else {
					 * deletefromProcess =
					 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid
					 * + "' AND ORG_CHN='" + order_refnum[i] + "' "; }
					 */

					deletefromProcess = "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid + "' AND ORG_CHN='"
							+ order_refnum[i] + "' ";
					deletefromProcess1 = "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" + instid
							+ "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN='"
							+ order_refnum[i] + "') ";

					enctrace("deletefromProcess::" + deletefromProcess);
					String reissuedate = "''", reissue_count = "0", repindate = "''", repincount = "0",
							damgedate = "''", blockdate = "''", hotdate = "''", closedte = "''", pinretry_count = "0";
					String active_date = "''";
					String status_code = commondesc.getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate);
					StringBuilder movetoproduction = new StringBuilder();
					movetoproduction.append(
							"INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,");
					movetoproduction.append(
							"EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,");
					movetoproduction.append(
							"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH,CARD_FLAG,BULK_REG_ID)");
					movetoproduction
							.append("(SELECT INST_ID,ORDER_REF_NO,BIN, MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"
									+ CARDACTIVATEDCODE + "',CAF_REC_STATUS," + status_code
									+ ",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,");
					movetoproduction.append("PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'" + userid
							+ "',(SYSDATE),CHECKER_ID,CHECKER_DATE,'P',SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,");
					movetoproduction.append("ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0'," + reissuedate
							+ ",'" + reissue_count + "'," + repindate + ",'" + repincount + "'," + damgedate + ","
							+ blockdate + "," + hotdate + "," + closedte + ",PIN_OFFSET,'" + pinretry_count
							+ "',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH,CARD_FLAG,BULK_REG_ID FROM PERS_CARD_PROCESS ");

					trace("movetoproduction query---->   " + movetoproduction);
					String movetoproduction1 = "INSERT INTO CARD_PRODUCTION_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,"
							+ "PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)  (SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,"
							+ "CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE, " + userid
							+ ",(SYSDATE),CHECKER_ID,CHECKER_DATE FROM " + "PERS_CARD_PROCESS_HASH WHERE INST_ID='"
							+ instid + "'AND HCARD_NO='" + hcardNumber + "')";

					trace("movetoproduction1 query---->   " + movetoproduction1);

					/*
					 * movetoproduction.append(
					 * "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,"
					 * ); movetoproduction.append(
					 * "EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
					 * ); movetoproduction.append(
					 * "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH)"
					 * ); movetoproduction .append(
					 * "(SELECT INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"
					 * + CARDACTIVATEDCODE + "',CAF_REC_STATUS," + status_code +
					 * ",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"
					 * ); movetoproduction.append(
					 * "PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'" +
					 * userid +
					 * "',(SYSDATE),CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
					 * ); movetoproduction.append(
					 * "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"
					 * + reissuedate + ",'" + reissue_count + "'," + repindate +
					 * ",'" + repincount + "'," + damgedate + "," + blockdate +
					 * "," + hotdate + "," + closedte + ",PIN_OFFSET,'" +
					 * pinretry_count +
					 * "',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,'',CARD_REF_NO,CARD_COLLECT_BRANCH FROM PERS_CARD_PROCESS "
					 * );
					 * 
					 */

					if (padssenable.equals("Y")) {
						movetoproduction
								.append("WHERE INST_ID='" + instid + "' AND ORG_CHN='" + order_refnum[i] + "')");
					} else {
						movetoproduction
								.append("WHERE INST_ID='" + instid + "' AND ORG_CHN='" + order_refnum[i] + "')");
					}
					enctrace("movetoproduction---- > " + movetoproduction);

					String INSTID_1 = "", CHN_1 = "", ACCOUNTNO_1 = "", ACCOUNTTYPE_1 = "", ACCOUNTFLAG_1 = "",
							ACCOUNTPRIORITY_1 = "", CURRCODE_1 = "";
					String INSTID_3 = "", CHN_3 = "", CARDTYPE_3 = "", CUSTID_3 = "", TXNGROUPID_3 = "",
							LIMITFLAG_3 = "", EXPIRYDATE_3 = "", STATUS = "", PINOFFSET_3 = "", OLDPINOFFSET_3 = "",
							TPINOFFSET_3 = "", OLDTPINOFFSET_3 = "", PINRETRYCOUNT_3 = "", TPINRETRYCOUNT_3 = "",
							PVKI_3 = "", LASTTXNDATE_3 = "", LASTTXNTIME_3 = "", PANSEQNO_3 = "";
					String CUSTID_4 = "", NAME_4 = "", DOB_4 = "", SPOUSENAME_4 = "", ADDRESS1_4 = "", ADDRESS2_4 = "",
							ADDRESS3_4 = "", OFFPHONE_4 = "", MOBILE_4 = "", EMAIL_4 = "", RESPHONE_4 = "";

					StringBuilder mv = new StringBuilder();

					mv.append("SELECT ");
					// --EZAUTHREL start
					// --INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG,
					// ACCOUNTPRIORITY, CURRCODE,
					mv.append("'" + instid + "' INSTID_1, '" + order_refnum[i]
							+ "' CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
					// --EZAUTHREL end

					// -- EZCARDINFO start
					// --INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG,
					// EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET,
					// OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI,
					// LASTTXNDATE, LASTTXNTIME, PANSEQNO
					mv.append("'" + instid + "' INSTID_3, '" + order_refnum[i]
							+ "' CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'" + custcin + "' CUSTID_3,'01' TXNGROUPID_3, ");
					mv.append(
							"(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
					mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'" + status_code
							+ "' STATUS , NVL(PCP.PIN_OFFSET,0) PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, ");
					mv.append(
							"'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
					mv.append(
							"TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3, ");
					// -- EZCARDINFO end

					// ezcustomerinfo start
					mv.append("' " + custcin
							+ "' CUSTID_4, CI.FNAME NAME_4, CI.DOB  DOB_4,CI.SPOUCE_NAME SPOUSENAME_4, ");
					mv.append(
							"CI.C_HOUSE_NO ADDRESS1_4, CI.C_STREET_NAME ADDRESS2_4,CI.C_CITY ADDRESS3_4, CI.C_PHONE1 OFFPHONE_4,CI.MOBILE MOBILE_4, ");
					mv.append("CI.E_MAIL  EMAIL_4 ,CI.C_PHONE2 RESPHONE_4");
					// end

					mv.append(" FROM CUSTOMERINFO CI ,ACCOUNTINFO AI ,PERS_CARD_PROCESS PCP ");
					// mv.append("WHERE (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND
					// AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO =
					// CI.ORDER_REF_NO) AND ");
					mv.append(" WHERE (CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN) AND ");
					mv.append(
							"(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID and pcp.acct_no=ai.ACCOUNTNO) ");
					mv.append("AND CI.INST_ID='" + instid + "' AND AI.INST_ID='" + instid + "' AND PCP.INST_ID='"
							+ instid + "'  ");
					mv.append(
							"AND CI.CIN='" + custcin + "' AND AI.CIN='" + custcin + "' AND PCP.CIN='" + custcin + "' ");
					if (padssenable.equals("Y")) {
						mv.append("AND PCP.INST_ID='" + instid + "' AND PCP.ORG_CHN='" + order_refnum[i] + "'");
					} else {
						mv.append("AND PCP.INST_ID='" + instid + "' AND PCP.ORG_CHN='" + order_refnum[i] + "'");
					}

					enctrace("Move to Production CafRec status SS-----------------------------------\n");
					enctrace(mv.toString());
					enctrace("Move to Production CafRec status SS New-----------------------------------\n"
							+ mv.toString());

					List movetoSwitchP = jdbctemplate.queryForList(mv.toString());
					Iterator custitr = movetoSwitchP.iterator();
					while (custitr.hasNext()) {
						Map mp = (Map) custitr.next();
						// fname = (String)mp.get("FNAME");
						INSTID_1 = (String) mp.get(" INSTID_1 ");
						CHN_1 = (String) mp.get("CHN_1");
						ACCOUNTNO_1 = (String) mp.get("ACCOUNTNO_1");
						ACCOUNTTYPE_1 = (String) mp.get("ACCOUNTTYPE_1");
						ACCOUNTFLAG_1 = (String) mp.get("ACCOUNTFLAG_1");
						ACCOUNTPRIORITY_1 = (String) mp.get("ACCOUNTPRIORITY_1");
						CURRCODE_1 = (String) mp.get("CURRCODE_1");

						CHN_3 = mp.get("CHN_3").toString();
						CARDTYPE_3 = mp.get("CARDTYPE_3").toString();
						CUSTID_3 = mp.get("CUSTID_3").toString();
						TXNGROUPID_3 = mp.get("TXNGROUPID_3").toString();
						LIMITFLAG_3 = mp.get("LIMITFLAG_3").toString();
						EXPIRYDATE_3 = mp.get("EXPIRYDATE_3").toString();
						STATUS = mp.get("STATUS").toString();
						PINOFFSET_3 = mp.get("PINOFFSET_3").toString();
						OLDPINOFFSET_3 = mp.get("OLDPINOFFSET_3").toString();
						TPINOFFSET_3 = mp.get("TPINOFFSET_3").toString();
						OLDTPINOFFSET_3 = mp.get("OLDTPINOFFSET_3").toString();
						PINRETRYCOUNT_3 = mp.get("PINRETRYCOUNT_3").toString();
						TPINRETRYCOUNT_3 = mp.get("TPINRETRYCOUNT_3").toString();
						PVKI_3 = mp.get("PVKI_3").toString();
						LASTTXNDATE_3 = mp.get("LASTTXNDATE_3").toString();
						LASTTXNTIME_3 = mp.get("LASTTXNTIME_3").toString();
						PANSEQNO_3 = mp.get("PANSEQNO_3").toString();

						CUSTID_4 = mp.get("CUSTID_4").toString();
						NAME_4 = (String) mp.get("NAME_4");
						DOB_4 = (String) mp.get("DOB_4");
						SPOUSENAME_4 = (String) mp.get("SPOUSENAME_4");
						ADDRESS1_4 = (String) mp.get("ADDRESS1_4");
						ADDRESS2_4 = (String) mp.get("ADDRESS2_4");
						ADDRESS3_4 = (String) mp.get("ADDRESS3_4");
						OFFPHONE_4 = (String) mp.get("OFFPHONE_4");
						MOBILE_4 = (String) mp.get("MOBILE_4");
						EMAIL_4 = (String) mp.get("EMAIL_4");
						RESPHONE_4 = (String) mp.get("RESPHONE_4");
					}

					/*
					 * String custexistqry =
					 * "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO WHERE trim(CUSTID)= '"
					 * + custcin + "'"; String custcount = (String)
					 * jdbctemplate.queryForObject(custexistqry, String.class);
					 */

					// by gowtham-270819
					String custexistqry = "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO WHERE trim(CUSTID)=?";
					String custcount = (String) jdbctemplate.queryForObject(custexistqry, new Object[] { custcin },
							String.class);

					trace("Avialbe EZCUSTOMERINFO" + custcount);
					StringBuilder cinf_4 = new StringBuilder();
					/*
					 * cinf_4.append("INSERT INTO EZCUSTOMERINFO ");
					 * cinf_4.append(
					 * "(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) "
					 * ); cinf_4.append("VALUES "); cinf_4.append("('" + instid
					 * + "','" + CUSTID_3 + "','" + NAME_4 + "','" + DOB_4 +
					 * "','" + SPOUSENAME_4 + "', "); cinf_4.append("'" +
					 * ADDRESS1_4 + "','" + ADDRESS2_4 + "','" + ADDRESS3_4 +
					 * "','" + OFFPHONE_4 + "','" + MOBILE_4 + "','" + EMAIL_4 +
					 * "','" + RESPHONE_4 + "') ");
					 * 
					 * enctrace("cinf_4:::::" + cinf_4.toString());
					 * 
					 * if ("0".equalsIgnoreCase(custcount)) { res_4 =
					 * jdbctemplate.update(cinf_4.toString());
					 */

					// BY GOWTHAM-270819

					cinf_4.append("INSERT INTO EZCUSTOMERINFO ");
					cinf_4.append(
							"(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) ");
					cinf_4.append("VALUES ");
					cinf_4.append("(?,?,?,?,?, ");
					cinf_4.append("?,?,?,?,?,?,?) ");

					enctrace("cinf_4:::::" + cinf_4.toString());
					if ("0".equalsIgnoreCase(custcount)) {
						res_4 = jdbctemplate.update(cinf_4.toString(),
								new Object[] { instid, CUSTID_3, NAME_4, DOB_4, SPOUSENAME_4, ADDRESS1_4, ADDRESS2_4,
										ADDRESS3_4, OFFPHONE_4, MOBILE_4, EMAIL_4, RESPHONE_4 });

					} else {
						res_4 = 1;
					}

					/*
					 * String ezcustinfoqry =
					 * "SELECT COUNT(*) as cnt FROM EZCARDINFO WHERE trim(chn)= '"
					 * + CHN_3 + "'"; String ezcustinfo = (String)
					 * jdbctemplate.queryForObject(ezcustinfoqry, String.class);
					 */

					/// by gowtham-270819
					String ezcustinfoqry = "SELECT COUNT(*) as cnt FROM EZCARDINFO WHERE trim(chn)=?";
					String ezcustinfo = (String) jdbctemplate.queryForObject(ezcustinfoqry, new Object[] { CHN_3 },
							String.class);

					trace("Avialbe EZCARDINFO" + ezcustinfo);

					StringBuilder crdinf_3 = new StringBuilder();
					/*
					 * crdinf_3.append("INSERT INTO EZCARDINFO ");
					 * crdinf_3.append(
					 * "(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) "
					 * ); crdinf_3.append("VALUES "); crdinf_3.append("('" +
					 * instid + "','" + hcardNumber + "','" + CARDTYPE_3 + "','"
					 * + CUSTID_3 + "','" + TXNGROUPID_3 + "','" + LIMITFLAG_3 +
					 * "',TO_DATE('" + EXPIRYDATE_3 + "','MM/DD/YYYY'),'" +
					 * STATUS + "','" + PINOFFSET_3 + "','" + OLDPINOFFSET_3 +
					 * "',"); crdinf_3.append("'" + TPINOFFSET_3 + "','" +
					 * OLDTPINOFFSET_3 + "','" + PINRETRYCOUNT_3 + "','" +
					 * TPINRETRYCOUNT_3 + "','" + PVKI_3 + "',TO_DATE('" +
					 * LASTTXNDATE_3 + "','MM/DD/YYYY'),'" + LASTTXNTIME_3 +
					 * "'  ,'0' ,"); crdinf_3.append("'" + PANSEQNO_3 + "' )");
					 */

					// BY GOWTHAM-270819

					crdinf_3.append("INSERT INTO EZCARDINFO ");
					crdinf_3.append(
							"(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
					crdinf_3.append("VALUES ");
					crdinf_3.append("(?,?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?,?,?,");
					crdinf_3.append("?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?  ,? ,");
					crdinf_3.append("? )");

					enctrace("crdinf_3:::::" + crdinf_3.toString());

					if ("0".equalsIgnoreCase(ezcustinfo)) {
						/* res_3 = jdbctemplate.update(crdinf_3.toString()); */
						res_3 = jdbctemplate.update(crdinf_3.toString(),
								new Object[] { instid, hcardNumber, CARDTYPE_3, CUSTID_3, TXNGROUPID_3, LIMITFLAG_3,
										EXPIRYDATE_3, STATUS, PINOFFSET_3, OLDPINOFFSET_3, TPINOFFSET_3,
										OLDTPINOFFSET_3, PINRETRYCOUNT_3, TPINRETRYCOUNT_3, PVKI_3, LASTTXNDATE_3,
										LASTTXNTIME_3, "0", PANSEQNO_3 });
					} else {
						res_3 = 1;
					}

					/*
					 * String EZAUTHRELqry =
					 * "SELECT COUNT(*) as cnt FROM EZAUTHREL WHERE trim(chn)= '"
					 * + CHN_3 + "'"; String EZAUTHREL = (String)
					 * jdbctemplate.queryForObject(EZAUTHRELqry, String.class);
					 */

					// by gowtham-270819
					String EZAUTHRELqry = "SELECT COUNT(*) as cnt FROM EZAUTHREL WHERE trim(chn)= ?";
					String EZAUTHREL = (String) jdbctemplate.queryForObject(EZAUTHRELqry, new Object[] { CHN_3 },
							String.class);

					trace("Avialbe EZCUSTOMERINFO" + EZAUTHREL);

					StringBuilder authrel_1 = new StringBuilder();
					/*
					 * authrel_1.append("INSERT INTO EZAUTHREL ");
					 * authrel_1.append(
					 * "(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) "
					 * ); authrel_1.append("VALUES "); authrel_1.append("('" +
					 * instid + "','" + hcardNumber + "','" + ACCOUNTNO_1 +
					 * "','" + ACCOUNTTYPE_1 + "','" + ACCOUNTFLAG_1 + "','" +
					 * ACCOUNTPRIORITY_1 + "','" + CURRCODE_1 + "') ");
					 * enctrace("authrel_1::::" + authrel_1.toString());
					 * 
					 * if ("0".equalsIgnoreCase(EZAUTHREL)) { res_1 =
					 * jdbctemplate.update(authrel_1.toString());
					 */

					// by gowtham-270819

					authrel_1.append("INSERT INTO EZAUTHREL ");
					authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
					authrel_1.append("VALUES ");
					authrel_1.append("(?,?,?,?,?,?,?) ");
					enctrace("authrel_1::::" + authrel_1.toString());

					if ("0".equalsIgnoreCase(EZAUTHREL)) {
						res_1 = jdbctemplate.update(authrel_1.toString(), new Object[] { instid, hcardNumber,
								ACCOUNTNO_1, ACCOUNTTYPE_1, ACCOUNTFLAG_1, ACCOUNTPRIORITY_1, CURRCODE_1 });

					} else {
						res_1 = 1;
					}

					int production_insert = jdbctemplate.update(movetoproduction.toString());
					int production_insert1 = jdbctemplate.update(movetoproduction1);
					int deletefromprocess1 = jdbctemplate.update(deletefromProcess1);
					int deletefromprocess = jdbctemplate.update(deletefromProcess);

					enctrace("\n--------------------------------------Move to Production Query CAF REC S");
					trace("result :::::::::::" + res_1 + res_3 + res_4 + production_insert + deletefromprocess);
					if (res_1 > 0 && res_3 > 0 && res_4 > 0 && production_insert > 0 && deletefromprocess > 0
							&& deletefromprocess1 > 0 && production_insert1 > 0) {
						trace("PROCESS COMPLETED");
						updatecount = updatecount + 1;
					} else {
						//// System.out.println("Error While Insert and Delete
						//// ");
						addActionError("AC Unable to continue process ....");
						return personalCardissuehome();
					}

					/*
					 * int update =
					 * cbsTableUpdateDetails(order_refnum[i],padssenable,instid,
					 * userid,padsssec,eDMK,eDPK,cafflag,jdbctemplate);
					 * 
					 * if(update == 1){ enctrace(
					 * " ----ADDON CARD - CBS UPDATE SUCCESS----"); }else{
					 * enctrace(" ----ADDON CARD - CBS UPDATE FAIL----"); }
					 */

				}

				else if (cafflag.equals(
						"BN")) {/*
								 * 
								 * 
								 * enctrace("#################################")
								 * ; enctrace(
								 * "####### BULK RENUED WITH NEW card ###"
								 * +order_refnum[i]); trace(
								 * "####### BULK RENUED WITH NEW card ###"
								 * +order_refnum[i]);
								 * 
								 * String custcin =
								 * commondesc.fchCustomerId(instid,padssenable,
								 * order_refnum[i], "PERS_CARD_PROCESS",
								 * jdbctemplate); trace("Got the customer id : "
								 * + custcin); ////System.out.println(
								 * "CUSTOMERN NUMBER IS =====> "+custcin);
								 * 
								 * int res_1 =0;int res_2 =0;int res_3=0;int
								 * res_4=0;int res_5 = 0;
								 * 
								 * String deletefromProcess = "";
								 * if(padssenable.equals("Y")){
								 * deletefromProcess =
								 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='"
								 * +instid+"' AND HCARD_NO='"+order_refnum[i]+
								 * "' "; }else { deletefromProcess =
								 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='"
								 * +instid+"' AND CARD_NO='"+order_refnum[i]+
								 * "' "; } enctrace("deletefromProcess::"+
								 * deletefromProcess);
								 * 
								 * String reissuedate="''",reissue_count =
								 * "0",repindate="''",repincount="0",damgedate=
								 * "''",blockdate="''",hotdate="''",closedte=
								 * "''",pinretry_count="0"; String
								 * active_date="''"; String status_code=
								 * commondesc.getSwitchCardStatus(instid,
								 * CARDACTIVATEDCODE, jdbctemplate);
								 * StringBuilder movetoproduction = new
								 * StringBuilder(); movetoproduction.append(
								 * "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,"
								 * ); movetoproduction.append(
								 * "EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
								 * ); movetoproduction.append(
								 * "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARD_COLLECT_BRANCH)"
								 * ); movetoproduction.append(
								 * "(SELECT INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"
								 * +CARDACTIVATEDCODE+"',CAF_REC_STATUS,"+
								 * status_code+
								 * ",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"
								 * ); movetoproduction.append(
								 * "PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'"
								 * +userid+
								 * "',(SYSDATE),CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
								 * ); movetoproduction.append(
								 * "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"
								 * +reissuedate+",'"+reissue_count+"',"+
								 * repindate+",'"+repincount+"',"+damgedate+","+
								 * blockdate+","+hotdate+","+closedte+
								 * ",PIN_OFFSET,'"+pinretry_count+
								 * "',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,'',CARD_REF_NO,CARD_COLLECT_BRANCH FROM PERS_CARD_PROCESS "
								 * ); if(padssenable.equals("Y")){
								 * movetoproduction.append("WHERE INST_ID='"
								 * +instid+"' AND HCARD_NO='"
								 * +order_refnum[i]+"')"); }else{
								 * movetoproduction.append("WHERE INST_ID='"
								 * +instid+"' AND CARD_NO='"
								 * +order_refnum[i]+"')"); } enctrace(
								 * "movetoproduction---- > "+movetoproduction);
								 * 
								 * 
								 * 
								 * String INSTID_1="" ,CHN_1="" ,ACCOUNTNO_1=""
								 * ,ACCOUNTTYPE_1="" ,ACCOUNTFLAG_1=""
								 * ,ACCOUNTPRIORITY_1="" ,CURRCODE_1="" ; String
								 * INSTID_3="" ,CHN_3="" ,CARDTYPE_3=""
								 * ,CUSTID_3="" ,TXNGROUPID_3="" ,LIMITFLAG_3=""
								 * ,EXPIRYDATE_3="" ,STATUS="" ,PINOFFSET_3=""
								 * ,OLDPINOFFSET_3="" ,TPINOFFSET_3=""
								 * ,OLDTPINOFFSET_3="" ,PINRETRYCOUNT_3=""
								 * ,TPINRETRYCOUNT_3="" ,PVKI_3=""
								 * ,LASTTXNDATE_3="" ,LASTTXNTIME_3=""
								 * ,PANSEQNO_3="";
								 * 
								 * 
								 * 
								 * StringBuilder mv = new StringBuilder();
								 * 
								 * mv.append("SELECT "); //--EZAUTHREL start
								 * //--INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE,
								 * ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,
								 * mv.append("'"+instid+"' INSTID_1, '"
								 * +order_refnum[i]+
								 * "' CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, "
								 * ); //--EZAUTHREL end
								 * 
								 * //-- EZCARDINFO start //--INSTID, CHN,
								 * CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG,
								 * EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET,
								 * TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT,
								 * TPINRETRYCOUNT, PVKI, LASTTXNDATE,
								 * LASTTXNTIME, PANSEQNO mv.append("'"+instid+
								 * "' INSTID_3, '"+order_refnum[i]+
								 * "' CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"
								 * +custcin+"' CUSTID_3,'01' TXNGROUPID_3, ");
								 * mv.append(
								 * "(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  "
								 * ); mv.append(
								 * "TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'"
								 * +status_code+
								 * "' STATUS , NVL(PCP.PIN_OFFSET,0) PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, "
								 * ); mv.append(
								 * "'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  "
								 * ); mv.append(
								 * "TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3 "
								 * ); //-- EZCARDINFO end
								 * 
								 * mv.append(
								 * " FROM CUSTOMERINFO CI ,ACCOUNTINFO AI ,PERS_CARD_PROCESS PCP "
								 * ); mv.append("WHERE"); //mv.append(
								 * "WHERE (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND "
								 * ); mv.append(
								 * "(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN) AND "
								 * ); mv.append(
								 * "(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID and pcp.acct_no=ai.ACCOUNTNO) "
								 * ); mv.append("AND CI.INST_ID='"+instid+
								 * "' AND AI.INST_ID='"+instid+
								 * "' AND PCP.INST_ID='"+instid+"'  ");
								 * mv.append("AND CI.CIN='"+custcin+
								 * "' AND AI.CIN='"+custcin+"' AND PCP.CIN='"
								 * +custcin+"' "); if(padssenable.equals("Y")){
								 * mv.append("AND PCP.INST_ID='"+instid+
								 * "' AND PCP.HCARD_NO='"+order_refnum[i]+"'");
								 * }else{ mv.append("AND PCP.INST_ID='"+instid+
								 * "' AND PCP.CARD_NO='"+order_refnum[i]+"'"); }
								 * 
								 * enctrace(
								 * "Move to Production CafRec status BN-----------------------------------\n"
								 * ); enctrace(mv.toString()); enctrace(
								 * "Move to Production CafRec status BN New-----------------------------------\n"
								 * +mv.toString());
								 * 
								 * List movetoSwitchP =
								 * jdbctemplate.queryForList(mv.toString());
								 * Iterator custitr = movetoSwitchP.iterator();
								 * while(custitr.hasNext()) { Map mp =
								 * (Map)custitr.next(); //fname =
								 * (String)mp.get("FNAME"); INSTID_1 = (String)
								 * mp.get(" INSTID_1 "); CHN_1 = (String)
								 * mp.get("CHN_1"); ACCOUNTNO_1 = (String)
								 * mp.get("ACCOUNTNO_1"); ACCOUNTTYPE_1
								 * =(String) mp.get("ACCOUNTTYPE_1");
								 * ACCOUNTFLAG_1 =(String)
								 * mp.get("ACCOUNTFLAG_1"); ACCOUNTPRIORITY_1
								 * =(String) mp.get("ACCOUNTPRIORITY_1");
								 * CURRCODE_1 = (String) mp.get("CURRCODE_1");
								 * 
								 * CHN_3 = mp.get("CHN_3").toString();
								 * CARDTYPE_3 = mp.get("CARDTYPE_3").toString();
								 * CUSTID_3 = mp.get("CUSTID_3").toString();
								 * TXNGROUPID_3 =
								 * mp.get("TXNGROUPID_3").toString();
								 * LIMITFLAG_3 =
								 * mp.get("LIMITFLAG_3").toString();
								 * EXPIRYDATE_3 =
								 * mp.get("EXPIRYDATE_3").toString(); STATUS =
								 * mp.get("STATUS").toString(); PINOFFSET_3 =
								 * mp.get("PINOFFSET_3") .toString();
								 * OLDPINOFFSET_3 =
								 * mp.get("OLDPINOFFSET_3").toString();
								 * TPINOFFSET_3 =
								 * mp.get("TPINOFFSET_3").toString();
								 * OLDTPINOFFSET_3 =
								 * mp.get("OLDTPINOFFSET_3").toString();
								 * PINRETRYCOUNT_3 =
								 * mp.get("PINRETRYCOUNT_3").toString();
								 * TPINRETRYCOUNT_3 =
								 * mp.get("TPINRETRYCOUNT_3").toString(); PVKI_3
								 * = mp.get("PVKI_3").toString(); LASTTXNDATE_3
								 * = mp.get("LASTTXNDATE_3").toString();
								 * LASTTXNTIME_3 =
								 * mp.get("LASTTXNTIME_3").toString();
								 * PANSEQNO_3 = mp.get("PANSEQNO_3")
								 * .toString(); }
								 * 
								 * StringBuilder crdinf_3 = new StringBuilder();
								 * crdinf_3.append("INSERT INTO EZCARDINFO ");
								 * crdinf_3.append(
								 * "(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) "
								 * ); crdinf_3.append("VALUES ");
								 * crdinf_3.append("('"+instid+"','"+CHN_3+"','"
								 * +CARDTYPE_3+"','"+CUSTID_3+"','"+TXNGROUPID_3
								 * +"','"+LIMITFLAG_3+"',TO_DATE('"+EXPIRYDATE_3
								 * +"','MM/DD/YYYY'),'"+STATUS+"','"+PINOFFSET_3
								 * +"','"+OLDPINOFFSET_3+"',");
								 * crdinf_3.append("'"+TPINOFFSET_3+"','"+
								 * OLDTPINOFFSET_3+"','"+PINRETRYCOUNT_3+"','"+
								 * TPINRETRYCOUNT_3+"','"+PVKI_3+"',TO_DATE('"+
								 * LASTTXNDATE_3+"','MM/DD/YYYY'),'"+
								 * LASTTXNTIME_3+"'  ,'0' ,");
								 * crdinf_3.append("'"+PANSEQNO_3+"' )");
								 * 
								 * enctrace("crdinf_3:::::"+crdinf_3.toString())
								 * ;
								 * 
								 * res_3 =
								 * jdbctemplate.update(crdinf_3.toString());
								 * 
								 * StringBuilder authrel_1 = new
								 * StringBuilder(); authrel_1.append(
								 * "INSERT INTO EZAUTHREL "); authrel_1.append(
								 * "(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) "
								 * ); authrel_1.append("VALUES ");
								 * authrel_1.append("('"+instid+"','"+CHN_1+
								 * "','"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+
								 * ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+
								 * CURRCODE_1+"') ");
								 * enctrace("authrel_1::::"+authrel_1.toString()
								 * );
								 * 
								 * 
								 * res_1 =
								 * jdbctemplate.update(authrel_1.toString());
								 * 
								 * 
								 * 
								 * 
								 * 
								 * int production_insert =
								 * jdbctemplate.update(movetoproduction.toString
								 * ()); int deletefromprocess =
								 * jdbctemplate.update(deletefromProcess);
								 * 
								 * 
								 * 
								 * 
								 * enctrace(
								 * "\n--------------------------------------Move to Production Query CAF REC S"
								 * ); trace("result :::::::::::"
								 * +res_1+res_3+production_insert+
								 * deletefromprocess); if(res_1>0 && res_3>0 &&
								 * production_insert>0 && deletefromprocess>0) {
								 * trace("PROCESS COMPLETED"); updatecount =
								 * updatecount + 1; } else {
								 * ////System.out.println(
								 * "Error While Insert and Delete ");
								 * addActionError(
								 * "Unable to continue process ...."); return
								 * personalCardissuehome(); }
								 * 
								 * int update =
								 * cbsTableUpdateDetails(order_refnum[i],
								 * padssenable,instid,userid,padsssec,eDMK,eDPK,
								 * cafflag,jdbctemplate);
								 * 
								 * if(update >0){ enctrace(
								 * " ----BULK RENEWAL CARD - CBS UPDATE SUCCESS----"
								 * ); }else{ enctrace(
								 * " ----BULK RENEWAL CARD - CBS UPDATE FAIL----"
								 * ); }
								 * 
								 */

					trace(" RENUAL CARD ATE WITH  expDATE...................");

					StringBuilder update_production = new StringBuilder();
					update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS='" + cardstatus
							+ "', EXPIRY_DATE=(SELECT EXPIRY_DATE from PERS_CARD_PROCESS where ORG_CHN='"
							+ order_refnum[i] + "' AND INST_ID='" + instid + "'),STATUS_CODE='" + switchstatus
							+ "',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='" + userid
							+ "', ");

					//trace("update_production ---->   " + update_production);

					update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' ");
					if (padssenable.equals("Y")) {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' AND INST_ID='" + instid + "'");
					} else {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' AND INST_ID='" + instid + "'");
					}

					trace("update_production :" + update_production.toString());
					update_product = jdbctemplate.update(update_production.toString());

					StringBuilder updEzcard = new StringBuilder();

					/*
					 * updEzcard.append("UPDATE EZCARDINFO set ");
					 * updEzcard.append(
					 * "EXPIRYDATE = (SELECT EXPIRY_DATE from PERS_CARD_PROCESS where "
					 * ); updEzcard.append("ORG_CHN='" + order_refnum[i] +
					 * "' AND INSTID='" + instid + "')"); updEzcard.append(
					 * ",STATUS = '" + switchstatus + "' WHERE CHN = '" +
					 * hcardNumber + "' AND INSTID='" + instid + "'");
					 * 
					 * trace("updEzcard :" + updEzcard.toString());
					 * updezcardinfo =
					 * jdbctemplate.update(updEzcard.toString());
					 */

					// by gowtham-270819

					updEzcard.append("UPDATE EZCARDINFO set ");
					updEzcard.append("EXPIRYDATE = (SELECT EXPIRY_DATE from PERS_CARD_PROCESS where ");
					updEzcard.append("ORG_CHN=? AND INSTID=?)");
					updEzcard.append(",STATUS = ? WHERE CHN =? AND INSTID=? ");

					trace("updEzcard :" + updEzcard.toString());
					updezcardinfo = jdbctemplate.update(updEzcard.toString(),
							new Object[] { order_refnum[i], instid, switchstatus, hcardNumber, instid });

					StringBuilder deletefromProcess = new StringBuilder();
					StringBuilder deletefromProcess1 = new StringBuilder();

					
					  deletefromProcess.append(
					  "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid + "' AND ORG_CHN='" + order_refnum[i] + "' ");
					  deletefromProcess1.append( "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" +instid +"' AND ORDER_REF_NO IN (SELECT ORDER_REF_NO FROM "
					  		+ "PERS_CARD_PROCESS WHERE ORG_CHN='" + order_refnum[i] + "') ");
					 
					  enctrace("deletefromProcess====  "+deletefromProcess);
					  enctrace("deletefromProcess1========  "+deletefromProcess1);

				/*	deletefromProcess.append("DELETE FROM PERS_CARD_PROCESS WHERE INST_ID=? AND ORG_CHN=? ");
					deletefromProcess1.append(
							"DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID=? AND ORDER_REF_NO(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN=?) ");*/

					// BY SIVA
					/*
					 * if (padssenable.equals("Y")) { deletefromProcess.append(
					 * "AND ORG_CHN='" + order_refnum[i] + "' "); } else {
					 * deletefromProcess.append("AND ORG_CHN='" +
					 * order_refnum[i] + "' "); }
					 */

					
				/*	 trace("update_product :" + update_product + " del_process :" + del_process); del_process = jdbctemplate.update(deletefromProcess.toString());
					 * del_process1 =
					 * jdbctemplate.update(deletefromProcess1.toString());*/
					  
						del_process1 = jdbctemplate.update(deletefromProcess1.toString());
						del_process = jdbctemplate.update(deletefromProcess.toString());
				

					// by gowtham-270819
						
					trace("del_process :" + del_process + " del_process1 :" + del_process1);
				

			/*		del_process = jdbctemplate.update(deletefromProcess.toString(),
							new Object[] { instid, order_refnum[i] });
					del_process1 = jdbctemplate.update(deletefromProcess1.toString(),
							new Object[] { instid, order_refnum[i] });
*/
					// del_pin = jdbctemplate.update(delete_pin);

					trace("update_product === "+update_product  +"     updezcardinfo======== "+updezcardinfo);
					if (update_product > 0 && del_process > 0 && del_process1 > 0 && updezcardinfo > 0) {
						trace("in true case");
						updatecount = updatecount + 1;
					} else {
						//// System.out.println("Process Breaked ====> 1");
						break;
					}

					trace("..................BULK RENUAL EXPIRY DATE ");

				}

				if (cafflag.equals("BR")) {

					trace("BULK RENUAL CARD ATE WITH EXPIRY DATE...................");

					StringBuilder update_production = new StringBuilder();
					/*
					 * update_production.append(
					 * "UPDATE CARD_PRODUCTION SET CARD_STATUS='"+cardstatus+
					 * "', STATUS_CODE='"+switchstatus+
					 * "',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"
					 * +userid+"', " ); update_production.append(
					 * "MAKER_DATE=(sysdate) WHERE INST_ID='"+instid+"' ");
					 * if(padssenable.equals("Y")){ update_production.append(
					 * "AND HCARD_NO='"+order_refnum[i]+"' AND INST_ID='"
					 * +instid+"'"); }else{ update_production.append(
					 * "AND CARD_NO='"+order_refnum[i]+"' AND INST_ID='"
					 * +instid+"'"); }
					 */
					update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS='" + cardstatus
							+ "', EXPIRY_DATE=(SELECT EXPIRY_DATE from PERS_CARD_PROCESS where HCARD_NO='"
							+ order_refnum[i] + "' AND INST_ID='" + instid + "'),STATUS_CODE='" + switchstatus
							+ "',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='" + userid
							+ "', ");
					update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' ");
					if (padssenable.equals("Y")) {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' AND INST_ID='" + instid + "'");
					} else {
						update_production.append("AND ORG_CHN='" + order_refnum[i] + "' AND INST_ID='" + instid + "'");
					}

					trace("update_production :" + update_production.toString());
					update_product = jdbctemplate.update(update_production.toString());

					StringBuilder updEzcard = new StringBuilder();
					/*
					 * updEzcard.append("UPDATE EZCARDINFO set ");
					 * updEzcard.append(
					 * "EXPIRYDATE = (SELECT EXPIRY_DATE from PERS_CARD_PROCESS where "
					 * ); updEzcard.append("ORG_CHN='" + hcardNumber +
					 * "' AND INSTID='" + instid + "')"); updEzcard.append(
					 * ",STATUS = '" + switchstatus + "' WHERE CHN = '" +
					 * hcardNumber + "' AND INSTID='" + instid + "'");
					 * 
					 * trace("updEzcard :" + updEzcard.toString());
					 * updezcardinfo =
					 * jdbctemplate.update(updEzcard.toString());
					 */

					// by gowtham270819
					updEzcard.append("UPDATE EZCARDINFO set ");
					updEzcard.append("EXPIRYDATE = (SELECT EXPIRY_DATE from PERS_CARD_PROCESS where ");
					updEzcard.append("ORG_CHN=? AND INSTID=?)");
					updEzcard.append(",STATUS = ? WHERE CHN =? AND INSTID=?");
					trace("updEzcard :" + updEzcard.toString());
					updezcardinfo = jdbctemplate.update(updEzcard.toString(),
							new Object[] { hcardNumber, instid, switchstatus, hcardNumber, instid });

					StringBuilder deletefromProcess = new StringBuilder();

					deletefromProcess.append("DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid + "' ");

					if (padssenable.equals("Y")) {
						deletefromProcess.append("AND ORG_CHN='" + order_refnum[i] + "' ");
					} else {
						deletefromProcess.append("AND ORG_CHN='" + order_refnum[i] + "' ");
					}
					/*
					 * String hashdelete =
					 * "DELETE FROM PERS_CARD_PROCESS_HASH WHERE HCARD_NO='" +
					 * hcardNumber + "' ";
					 */
					String hashdelete = "DELETE FROM PERS_CARD_PROCESS_HASH WHERE HCARD_NO=? ";

					trace("update_product :" + update_product + " del_process :" + del_process);
					del_process = jdbctemplate.update(deletefromProcess.toString());
					/* int hash_table_del = jdbctemplate.update(hashdelete); */

					int hash_table_del = jdbctemplate.update(hashdelete, new Object[] { hcardNumber });
					// del_pin = jdbctemplate.update(delete_pin);

					if (update_product > 0 && del_process > 0 && updezcardinfo > 0 && hash_table_del == 1) {

						updatecount = updatecount + 1;
					} else {
						//// System.out.println("Process Breaked ====> 1");
						break;
					}

					trace("..................BULK RENUAL EXPIRY DATE ");

				}

				/******************** fee insert ***********************/

				String F_cardno = "";
				String F_hcardno = hcardNumber;
				String F_mcardno = "";
				String F_accountno = "";
				String F_caf_rec_status = "";
				String F_feeid = "";
				String Fee_cardno = "";
				if (padssenable.equals("Y")) {
					Fee_cardno = "ORG_CHN='" + order_refnum[i] + "'";
				} else {
					Fee_cardno = "ORG_CHN='" + order_refnum[i] + "'";
				}
				String BRANCH_CODE = "";
				List feedetails = commondesc.getfeedetails(instid, Fee_cardno, jdbctemplate);
				Iterator custitr = feedetails.iterator();
				while (custitr.hasNext()) {
					Map mp = (Map) custitr.next();
					F_cardno = (String) mp.get("ORG_CHN");
					// F_hcardno = (String) mp.get("HCARD_NO");
					F_mcardno = (String) mp.get("MCARD_NO");
					F_accountno = (String) mp.get("ACCOUNT_NO");
					BRANCH_CODE = (String) mp.get("BRANCH_CODE");

					F_caf_rec_status = (String) mp.get("CAF_REC_STATUS");
					F_feeid = (String) mp.get("FEE_CODE");
				}
				String acctsubtype = F_accountno.substring(6, 8);

				/*
				 * String checkvalidqry =
				 * "SELECT COUNT(1) as CNT FROM FEE_SKIP_DETAILS WHERE ACCT_SUBTYPE_ID='"
				 * + acctsubtype + "'"; String cnt = (String)
				 * jdbctemplate.queryForObject(checkvalidqry, String.class);
				 */

				// by gowtham-270819
				String checkvalidqry = "SELECT COUNT(1) as CNT FROM FEE_SKIP_DETAILS WHERE ACCT_SUBTYPE_ID=?";
				String cnt = (String) jdbctemplate.queryForObject(checkvalidqry, new Object[] { acctsubtype },
						String.class);

				if ("0".equalsIgnoreCase(cnt)) {
					String renewalflag = "";
					List<Map<String, Object>> getrenewalflag = commondesc.getRenewalFlagByProd(instid, Fee_cardno,
							jdbctemplate);
					if (!getrenewalflag.isEmpty()) {
						renewalflag = (String) getrenewalflag.get(0).get("RENEWALFLAG");
						if (renewalflag == null) {
							renewalflag = "N";
						}
					}
					if (renewalflag.equalsIgnoreCase("Y")) {
						F_caf_rec_status = "BN";
					}
					trace("renewalflag" + renewalflag);
					Properties prop = getCommonDescProperty();
					String F_GLACCOUNTNO = prop.getProperty("fee.glaccountno");
					String F_TAXGLACCOUNTNO = prop.getProperty("fee.taxglaccountno");
					// String F_GLACCOUNTNO ="34643634643643";
					// String batchno = commondesc.getBatchSeqNo(instid,
					// jdbctemplate);
					List checkfeedetails = commondesc.feeinsertactivity(instid, F_cardno, F_hcardno, F_mcardno,
							F_accountno, F_caf_rec_status, F_feeid, userid, jdbctemplate);
					if (!checkfeedetails.isEmpty()) {
						trace("checkfeedetails" + checkfeedetails);
						Iterator itr = checkfeedetails.iterator();
						HashMap mp = (HashMap) itr.next();

						String actualamt = (String) mp.get("FEEAMT");
						int taxpercent = 10;
						int taxamt = (Integer.parseInt((actualamt)) / 100) * taxpercent;
						int custdebitamt = Integer.parseInt(actualamt) - taxamt;

						String custamt = String.valueOf(custdebitamt);
						String taxamount = String.valueOf(taxamt);
						String branchcode = BRANCH_CODE;// F_accountno.substring(12,14);
						int checkinsertfeedet = commondesc.insertfeedetails(instid, F_cardno, F_hcardno, F_mcardno,
								F_accountno, F_caf_rec_status, F_feeid, userid, mp, BRANCH_CODE, jdbctemplate);
						if (checkinsertfeedet != 1) {
							txManager.rollback(transact.status);
							trace("Fee Details rollbacked Successfully");
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "Fee Details not updated ");
							return "authcardissuehome";
						}

						int glcheckinsertfeedet = commondesc.glinsertfeedetails(instid, F_cardno, F_hcardno, F_mcardno,
								F_GLACCOUNTNO, F_caf_rec_status, F_feeid, userid, mp, custamt, branchcode,
								jdbctemplate);
						if (glcheckinsertfeedet != 1) {
							txManager.rollback(transact.status);
							trace("Fee Details rollbacked Successfully");
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "Fee Details not updated ");
							return "authcardissuehome";
						}
						int taxinsertfeedet = commondesc.taxinsertfeedetails(instid, F_cardno, F_hcardno, F_mcardno,
								F_TAXGLACCOUNTNO, F_caf_rec_status, F_feeid, userid, mp, taxamount, branchcode,
								jdbctemplate);
						if (taxinsertfeedet != 1) {
							txManager.rollback(transact.status);
							trace("Fee Details rollbacked Successfully");
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "Fee Details not updated ");
							return "authcardissuehome";
						}
					}
				}

				/********************* end fee insert *****************/

				/************* AUDIT BLOCK **************/
				try {
					String cond = "";
					if (padssenable.equals("Y")) {
						cond = "H";
					} else {
						cond = "C";
					}
					
					String mcardno = commondesc.getMaskedCardNoFromProd(instid, order_refnum[i], cond, jdbctemplate);
					String cin=commondesc.getCinFromProcess(order_refnum[i], instid, "", jdbctemplate);
					String accountno=commondesc.getAccountFromProcess(order_refnum[i], instid, jdbctemplate);
					if (mcardno == null) {
						mcardno = order_refnum[i];
					}

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);
                   auditbean.setCin(cin);
                   auditbean.setAccoutnno(accountno);
					auditbean.setActmsg("Issued Card [ " + mcardno + " ]");
					auditbean.setUsercode(userid);
					auditbean.setAuditactcode("0106");
					auditbean.setCardno(mcardno);
					// auditbean.setCardnumber(order_refnum[i]);
					// commondesc.insertAuditTrail(instid, username, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}

			}

		} catch (Exception e) {
			// txManager.rollback(transact.status);
			e.printStackTrace();
			trace("Error While Execute the Query ---->" + e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to continue the process");
			
		}

		trace("ordercount == updatecount:::" + ordercount + " == " + updatecount);
		if (ordercount == updatecount) {
			// if(ordercount>0){
			txManager.commit(transact.status);
			trace("Order Status Updated Successfully and Commited ");
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", ordercount + " Card Issued Sucessfully");

		}

		/************* AUDIT BLOCK **************/

		else {
			txManager.rollback(transact.status);
			trace("Txn Got Rollbacked ---->");
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Card Status not updated ");
		}
		
		//Added by Ramesh - Link Card & Account to CBS
		cardLinkWithCbs(order_refnum,instid,username,transact,"P",clearCardNumber);

		setAct((String) session.getAttribute("ACTIONTYPE"));
		return personalCardissuehome();
	}
	
	@Async
	public void cardLinkWithCbs(String[] order_refnum, String instid, String username, IfpTransObj transact, String type, String clearCardNumber1) {
	    // to be executed in the background
		
		
		IfpTransObj transact1 = commondesc.myTranObject("ISSUE", txManager);
		HttpServletRequest req = ServletActionContext.getRequest();
		  HttpServletResponse res = ServletActionContext.getResponse();
		  
		  

		  final AsyncContext asyncContext = req.startAsync(req, res);
		  asyncContext.start(new Runnable() {
		    @Override
		    public void run() {
		      try {
		        // doing some work asynchronously ...
		    	  int ordercount = order_refnum.length;
		    	  int insert = 0;
		    	  
		    	  
		    	  PadssSecurity padsssec = new PadssSecurity();
					String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					String eDMK = "", eDPK = "",cdpd="",CDPK="";
					Properties props1 = getCommonDescProperty();
					String EDPK = props1.getProperty("EDPK");
					try {
						List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
						Iterator secitr = secList.iterator();
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							eDMK = ((String) map.get("DMK"));
							eDPK = ((String) map.get("DPK"));
							CDPK = padsssec.decryptDPK(eDMK, EDPK);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						//return "required_home";
					}
				  
		    	  
		    	  String clearCardNumber="NA";
		    	  for (int i = 0; i < ordercount; i++) {
		  			//String hcardNumber = commondesc.getHcardNo(order_refnum[i], jdbctemplate);
		    		  
		    		  clearCardNumber = padsssec.getCHN(CDPK, order_refnum[i]);
		    		  
		  			System.out.println(" Inside Active Card Report Post To CBS ==> ");
		  			trace(" Inside Active Card Report Post To CBS ==> ");
		  			
		  			String docu_qury = "select BRANCH_CODE,ORG_CHN,CIN,ACCOUNT_NO,EMB_NAME,to_char(ISSUE_DATE,'yyyy-mm-dd') as ISSUE_DATE,"
		  					+ "ORDER_REF_NO,MCARD_NO,ORDER_REF_NO,to_char(EXPIRY_DATE,'yyyy-mm-dd') as EXPIRY_DATE,BULK_REG_ID "
		  					+ "from CARD_PRODUCTION WHERE INST_ID='"+instid+"' and org_chn='"+order_refnum[i]+"'";
		  			System.out.println(docu_qury);
		  			trace(docu_qury);
		  			
		  			List carddetlist = jdbctemplate.queryForList(docu_qury);
		  			String request = "";
		  			Iterator custitr = carddetlist.iterator();
		  			while (custitr.hasNext()) {
		  				Map mp = (Map) custitr.next();
		  				System.out.println(mp);
		  				trace("mp ===>"+mp);
		  				request= "{ \"activeCards\":[{" +
		  				   		"\"branch\":\""+(String) mp.get("BRANCH_CODE")+"\"," +
		  				   		"\"cardNumber\":\""+clearCardNumber+"\"," +
		  				   		"\"customerNumber\":\""+(String) mp.get("CIN")+"\"," +
		  				   		"\"accountNumber\":\""+(String) mp.get("ACCOUNT_NO")+"\"," +
		  				   		"\"emborsedName\":\""+(String) mp.get("EMB_NAME")+"\"," +
		  				   		"\"cardType\":\"INDIVIDUAL\"," +
		  				   		"\"issueDate\":\""+(String) mp.get("ISSUE_DATE")+"\"," +
		  				   		"\"orderRefNo\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
		  				   		"\"maskedCardNumber\":\""+(String) mp.get("MCARD_NO")+"\"," +
		  				   		"\"makerId\":\""+username+"\"," +
		  				   		"\"bulkRefId\":\""+(String) mp.get("BULK_REG_ID")+"\"," +
		  				   		"\"cardFlag\":\"\"," +
		  				   		"\"expiryDate\":\""+(String) mp.get("EXPIRY_DATE")+"\"}]}";
		  				
		  			}
		  			
		  			String result = "",ret=""; 
		  			JSONObject json = null;
		  			Properties props = getCommonDescProperty();
		  			String cbsapiurl = props.getProperty("cbs.api.url");
		  			String cbsapikey = props.getProperty("cbs.api.key");
		  			String cbsapisecret = props.getProperty("cbs.api.secret");
		  			String forwd = props.getProperty("cbs.api.fwd");
		  			try {
		  					String url = cbsapiurl+"core/api/v1.0/account/activeCardReport";
		  					System.out.println("url-->"+url);
		  					trace("url-->"+url);
		  					HttpPost post = new HttpPost(url);
		  					HttpClient client = HttpClientBuilder.create().build();
		  					post.setHeader("Content-Type", "application/json");
		  					post.setHeader("x-api-key", cbsapikey);
		  					post.setHeader("x-api-secret", cbsapisecret);
		  					post.setHeader("X-FORWARDED-FOR", forwd);
		  					
		  					System.out.println(request);
		  					trace("request-->"+request);
		  					
		  					StringEntity params =new StringEntity(request);
		  					post.setEntity(params);
		  					HttpResponse response = client.execute(post);
		  					int responseCode = response.getStatusLine().getStatusCode();
		  					System.out.println("Response Code : " + responseCode);
		  					trace("Response Code : " + responseCode);
		  					
		  					BufferedReader rd = new BufferedReader(
		  				                new InputStreamReader(response.getEntity().getContent()));
		  					String line = "";
		  					while ((line = rd.readLine()) != null) {
		  						result+= line;
		  					}
		  					ret = result;
		  				    System.out.println(ret);
		  				    trace("Response  : " + ret);
		  				    json = new JSONObject(ret);
		  				       
		  	        } catch (Exception e) {
		  				e.printStackTrace();
		  				System.out.println("Exception");
		  			}
		  			
		  			String cbslinkinsert = "INSERT INTO CBS_CARD_LINK(INSTID,CARDNO,MCARDNO,ACCT_NO,BRANCH,CIN,EMB_NAME,"
		  					+ "ORDER_REF_NO,UPLOAD_BY,UPLOAD_STATUS,UPLOAD_RESPCODE,ADDED_DATE,TYPE,CBS_BULK_REFID) SELECT INST_ID,ORG_CHN,MCARD_NO,"
		  					+ "ACCOUNT_NO,BRANCH_CODE,CIN,EMB_NAME,ORDER_REF_NO,'"+username+"','"+json.getString("message")+"',"
		  							+ "'"+json.getString("responseCode")+"',SYSDATE,'"+type+"',BULK_REG_ID "
		  					+ "FROM CARD_PRODUCTION WHERE ORG_CHN='"+order_refnum[i]+"'";
		  			System.out.println(cbslinkinsert);
		  			trace("===> CBS_CARD_LINK insert qry : "+cbslinkinsert);
		  			insert = jdbctemplate.update(cbslinkinsert);
		  			
		  		}
		    	  
		    	  if(insert > 0){
		  				System.out.println("cbs card link insert success");
		  				txManager.commit(transact1.status);
		  			}else{
		  				System.out.println("cbs card link insert fail");
		  				txManager.rollback(transact1.status);
		  			}
		    	  
		      } catch (KeyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		      finally {
		        asyncContext.complete();
		      }
		    }
		  });

		
		
		
	}

	public synchronized int personalCardIssuence(String cardno, String padssenable, String instid, String maker_id,
			String tablename, String hcardNumber, JdbcTemplate jdbctemplate) throws Exception {
		trace("**************personalCardIssuence**************");
		enctrace("**************personalCardIssuence**************");
		int issue_status = -1, custinfo_move = -1, delete_status = -1, custinfo_status = -1;

		String hashChn = hcardNumber;

		String cardstatus = CARDACTIVATEDCODE;
		String switchstatus = commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
		String userid = comUserId();

		trace("Activation the card number : " + cardno);
		String reissuedate = "''", reissue_count = "0", repindate = "''", repincount = "0", damgedate = "''",
				blockdate = "''", hotdate = "''", closedte = "''", pinretry_count = "0";
		String active_date = "''";
		String status_code = commondesc.getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate);

		String deletefromProcess = "";
		String deletefromProcess1 = "";

		/*
		 * if (padssenable.equals("Y")) { deletefromProcess =
		 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid +
		 * "' AND ORG_CHN='" + cardno+ "' "; deletefromProcess =
		 * "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" + instid +
		 * "' AND HCARD_NO='"+ hashChn + "' "; } else { deletefromProcess =
		 * "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid +
		 * "' AND ORG_CHN='" + cardno+ "' "; deletefromProcess =
		 * "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" + instid +
		 * "' AND HCARD_NO='"+ hashChn + "' "; }
		 */

		deletefromProcess = "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid + "' AND ORG_CHN='" + cardno
				+ "' ";
		deletefromProcess1 = "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" + instid + "' AND HCARD_NO='"
				+ hashChn + "' ";

		String cardissuetype = "";

		if (padssenable.equals("Y")) {
			cardissuetype = commondesc.getCardIssueTypeByCard(instid, padssenable, cardno, "PERSONAL", jdbctemplate);
		} else {
			cardissuetype = commondesc.getCardIssueTypeByCard(instid, padssenable, cardno, "PERSONAL", jdbctemplate);
		}

		trace("Getting cardissuetype...got :  " + cardissuetype);

		String custcin = "";
		if (padssenable.equals("Y")) {
			custcin = commondesc.persfchCustomerId(instid, padssenable, cardno, "PERS_CARD_PROCESS", jdbctemplate);
		} else {
			custcin = commondesc.persfchCustomerId(instid, padssenable, cardno, "PERS_CARD_PROCESS", jdbctemplate);
		}

		trace("Got the customer id : " + custcin);
		//// System.out.println("CUSTOMERN NUMBER IS =====> "+custcin);

		String acctno = null;
		int acctinfo_insert = -1;
		int acctpriority = -1;
		if (cardissuetype != null && cardissuetype.equals("$SUPLIMENT")) {
			acctpriority = 2;
			// String parentcardno =
			// CommonDesc.getParentCardNumberByCard(instid, cardno, "PERSONAL",
			// jdbctemplate);
			// acctno = commondesc.getCardPrimaryAccount(instid, parentcardno,
			// "currency", jdbctemplate);
			// trace("Got the acct number from parent card : " + acctno );
			// commondesc.updateAddonCountToParent(instid,parentcardno,jdbctemplate);
			acctinfo_insert = 1;
		} else {
			acctpriority = 1;
			// acctno =
			// curcode+commondesc.paddingZero(Integer.toString(curacctseq),
			// Integer.parseInt(acctnolen)-curcode.length());
			// trace("Generated account number : " + acctno );
			String INSTID_1 = "", CHN_1 = "", ACCOUNTNO_1 = "", ACCOUNTTYPE_1 = "", ACCOUNTFLAG_1 = "",
					ACCOUNTPRIORITY_1 = "", CURRCODE_1 = "";
			String INSTID_2 = "", ACCOUNTNO_2 = "", ACCOUNTTYPE_2 = "", CURRCODE_2 = "", AVAILBAL_2 = "",
					LEDGERBAL_2 = "", LIMITFLAG_2 = "", STATUS_2 = "", TXNGROUPID_2 = "", LASTTXNDATE_2 = "",
					LASTTXNTIME_2 = "", BRANCHCODE_2 = "", PRODUCTCODE_2 = "";
			String INSTID_3 = "", CHN_3 = "", CARDTYPE_3 = "", CUSTID_3 = "", TXNGROUPID_3 = "", LIMITFLAG_3 = "",
					EXPIRYDATE_3 = "", STATUS = "", PINOFFSET_3 = "", OLDPINOFFSET_3 = "", TPINOFFSET_3 = "",
					OLDTPINOFFSET_3 = "", PINRETRYCOUNT_3 = "", TPINRETRYCOUNT_3 = "", PVKI_3 = "", LASTTXNDATE_3 = "",
					LASTTXNTIME_3 = "", PANSEQNO_3 = "";
			String INSTID_4 = "", CUSTID_4 = "", NAME_4 = "", DOB_4 = "", SPOUSENAME_4 = "", ADDRESS1_4 = "",
					ADDRESS2_4 = "", ADDRESS3_4 = "", OFFPHONE_4 = "", MOBILE_4 = "", EMAIL_4 = "", RESPHONE_4 = "";
			String LIMITRECID_5 = "", cardcon1 = "";
			int res_1 = 0;
			int res_2 = 0;
			int res_3 = 0;
			int res_4 = 0;
			int res_5 = 0;
			StringBuilder mv = new StringBuilder();

			mv.append("SELECT ");
			// --EZAUTHREL start
			// --INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG,
			// ACCOUNTPRIORITY, CURRCODE,
			if (padssenable.equals("Y")) {
				mv.append("'" + instid
						+ "' INSTID_1, PCP.ORG_CHN CHN_1,PCP.ACCT_NO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.CARDISSUETYPE ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
			} else {
				mv.append("'" + instid
						+ "' INSTID_1, PCP.ORG_CHN CHN_1,PCP.ACCT_NO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.CARDISSUETYPE ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
			}
			if (padssenable.equals("Y")) {
				cardcon1 = "ORG_CHN";
			} else {
				cardcon1 = "ORG_CHN";
			}
			;
			// --EZAUTHREL end
			// --EZACCOUNTINFO start
			// --INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL,
			// LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME,
			// BRANCHCODE, PRODUCTCODE
			mv.append("'" + instid
					+ "' INSTID_2, PCP.ACCT_NO ACCOUNTNO_2, AI.ACCTTYPE_ID ACCOUNTTYPE_2, AI.ACCT_CURRENCY CURRCODE_2, '0' AVAILBAL_2, '0' LEDGERBAL_2,  ");
			mv.append("(SELECT ACCT_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_2, '"
					+ status_code + "' STATUS_2,  ");
			mv.append(
					"'01' TXNGROUPID_2, TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_2,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_2,  ");
			mv.append("PCP.BRANCH_CODE BRANCHCODE_2, AI.ACCTSUB_TYPE_ID PRODUCTCODE_2, ");
			// -- EZACCOUNTINFO end
			// -- EZCARDINFO start
			// --INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG,
			// EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET,
			// OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE,
			// LASTTXNTIME, PANSEQNO
			if (padssenable.equals("Y")) {
				mv.append("'" + instid + "' INSTID_3, PCP.ORG_CHN CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'" + custcin
						+ "' CUSTID_3,'01' TXNGROUPID_3, ");
			} else {
				mv.append("'" + instid + "' INSTID_3, PCP.ORG_CHN CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'" + custcin
						+ "' CUSTID_3,'01' TXNGROUPID_3, ");
			}
			mv.append("(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
			mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'" + status_code
					+ "' STATUS , NVL(PCP.PIN_OFFSET,0)  PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, ");
			mv.append(
					"'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
			mv.append(
					"TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3, ");
			// -- EZCARDINFO end
			// --EZCUSTOMERINFO start
			// --INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2,
			// ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE
			mv.append("'" + instid + "' INSTID_4,'" + custcin
					+ "' CUSTID_4, CI.FNAME NAME_4, CI.DOB  DOB_4,CI.SPOUCE_NAME SPOUSENAME_4, ");
			mv.append(
					"CI.C_HOUSE_NO ADDRESS1_4, CI.C_STREET_NAME ADDRESS2_4,CI.C_CITY ADDRESS3_4, CI.C_PHONE1 OFFPHONE_4,CI.MOBILE MOBILE_4, ");
			mv.append("CI.E_MAIL  EMAIL_4 ,CI.C_PHONE2 RESPHONE_4, PCP.LIMIT_ID LIMIT_RECORDID_5 FROM ");
			// --EZCUSTOMERINFO end
			mv.append(" CUSTOMERINFO CI ,ACCOUNTINFO AI ,PERS_CARD_PROCESS PCP ");
			mv.append("WHERE  ");
			// modifed by senthil
			// uncommented
			mv.append(
					" (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND");
			// commened
			// mv.append("(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN =
			// CI.CIN) AND ");

			mv.append(
					"(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID and pcp.acct_no=ai.ACCOUNTNO) ");
			mv.append("AND CI.INST_ID='" + instid + "' AND AI.INST_ID='" + instid + "' AND PCP.INST_ID='" + instid
					+ "'  ");
			// commented
			// mv.append("AND CI.CIN='"+custcin+"' AND AI.CIN='"+custcin+"' AND
			// PCP.CIN='"+custcin+"' AND "+cardcon1+"='"+cardno+"' ");
			mv.append("AND " + cardcon1 + "='" + cardno + "' ");
			// end
			enctrace("Move to Production Query-----------------------------------\n");
			enctrace(mv.toString());

			List movetoSwitchP = jdbctemplate.queryForList(mv.toString());
			Iterator custitr = movetoSwitchP.iterator();
			while (custitr.hasNext()) {
				Map mp = (Map) custitr.next();
				// fname = (String)mp.get("FNAME");
				INSTID_1 = (String) mp.get(" INSTID_1 ");
				CHN_1 = (String) mp.get("CHN_1");
				ACCOUNTNO_1 = (String) mp.get("ACCOUNTNO_1");
				ACCOUNTTYPE_1 = (String) mp.get("ACCOUNTTYPE_1");
				ACCOUNTFLAG_1 = (String) mp.get("ACCOUNTFLAG_1");
				ACCOUNTPRIORITY_1 = (String) mp.get("ACCOUNTPRIORITY_1");
				CURRCODE_1 = (String) mp.get("CURRCODE_1");
				INSTID_2 = (String) mp.get("INSTID_2");
				ACCOUNTNO_2 = (String) mp.get("ACCOUNTNO_2");
				ACCOUNTTYPE_2 = (String) mp.get("ACCOUNTTYPE_2");
				CURRCODE_2 = (String) mp.get("CURRCODE_2");
				AVAILBAL_2 = (String) mp.get("AVAILBAL_2");
				LEDGERBAL_2 = (String) mp.get("LEDGERBAL_2");
				LIMITFLAG_2 = (String) mp.get("LIMITFLAG_2");
				STATUS_2 = (String) mp.get("STATUS_2");
				TXNGROUPID_2 = (String) mp.get("TXNGROUPID_2");
				LASTTXNDATE_2 = (String) mp.get("LASTTXNDATE_2");
				LASTTXNTIME_2 = (String) mp.get("LASTTXNTIME_2");
				BRANCHCODE_2 = (String) mp.get("BRANCHCODE_2");
				PRODUCTCODE_2 = (String) mp.get("PRODUCTCODE_2");

				//// System.out.println("1"+mp.get("INSTID_3").toString());
				////// System.out.println("2"+mp.get("INSTID_3").toString());
				//// System.out.println("4"+mp.get("CHN_3").toString());
				//// System.out.println("5"+mp.get("CARDTYPE_3").toString());
				//// System.out.println("6"+mp.get("CUSTID_3").toString());
				//// System.out.println("7"+mp.get("TXNGROUPID_3").toString());
				//// System.out.println("8"+mp.get("LIMITFLAG_3").toString());
				//// System.out.println("8"+mp.get("EXPIRYDATE_3").toString());
				//// System.out.println("9"+mp.get("STATUS").toString());
				//// System.out.println("0"+mp.get("PINOFFSET_3") .toString());
				//// System.out.println("1"+mp.get("OLDPINOFFSET_3").toString());
				//// System.out.println("3"+mp.get("TPINOFFSET_3").toString());
				//// System.out.println("5"+mp.get("OLDTPINOFFSET_3").toString());
				//// System.out.println("6"+mp.get("PINRETRYCOUNT_3").toString());
				//// System.out.println("7"+mp.get("TPINRETRYCOUNT_3").toString());

				CHN_3 = mp.get("CHN_3").toString();
				CARDTYPE_3 = mp.get("CARDTYPE_3").toString();
				CUSTID_3 = mp.get("CUSTID_3").toString();
				TXNGROUPID_3 = mp.get("TXNGROUPID_3").toString();
				LIMITFLAG_3 = mp.get("LIMITFLAG_3").toString();
				EXPIRYDATE_3 = mp.get("EXPIRYDATE_3").toString();
				STATUS = mp.get("STATUS").toString();
				PINOFFSET_3 = mp.get("PINOFFSET_3").toString();
				OLDPINOFFSET_3 = mp.get("OLDPINOFFSET_3").toString();
				TPINOFFSET_3 = mp.get("TPINOFFSET_3").toString();
				OLDTPINOFFSET_3 = mp.get("OLDTPINOFFSET_3").toString();
				PINRETRYCOUNT_3 = mp.get("PINRETRYCOUNT_3").toString();
				TPINRETRYCOUNT_3 = mp.get("TPINRETRYCOUNT_3").toString();
				PVKI_3 = mp.get("PVKI_3").toString();
				LASTTXNDATE_3 = mp.get("LASTTXNDATE_3").toString();
				LASTTXNTIME_3 = mp.get("LASTTXNTIME_3").toString();
				PANSEQNO_3 = mp.get("PANSEQNO_3").toString();

				INSTID_4 = mp.get("INSTID_4").toString();
				CUSTID_4 = mp.get("CUSTID_4").toString();
				NAME_4 = (String) mp.get("NAME_4");
				DOB_4 = (String) mp.get("DOB_4");
				SPOUSENAME_4 = (String) mp.get("SPOUSENAME_4");
				ADDRESS1_4 = (String) mp.get("ADDRESS1_4");
				ADDRESS2_4 = (String) mp.get("ADDRESS2_4");
				ADDRESS3_4 = (String) mp.get("ADDRESS3_4");
				OFFPHONE_4 = (String) mp.get("OFFPHONE_4");
				MOBILE_4 = (String) mp.get("MOBILE_4");
				EMAIL_4 = (String) mp.get("EMAIL_4");
				RESPHONE_4 = (String) mp.get("RESPHONE_4");
				LIMITRECID_5 = (String) mp.get("LIMIT_RECORDID_5");

			}
			String cardcon = "";
			if (padssenable.equals("Y")) {
				cardcon = "ORG_CHN";
			} else {
				cardcon = "ORG_CHN";
			}
			;

			// String deletefromProcess ="";
			// deletefromProcess = "DELETE FROM "+tablename+" WHERE
			// INST_ID='"+instid+"' and "+cardcon+"='"+cardno+"'";
			// deletefromProcess = "INSERT INTO issue_log(chn) VALUES
			// ('"+cardno+"')";
			enctrace("DELETE TABLE QUERY--> " + deletefromProcess);
			enctrace("DELETE TABLE QUERY--> " + deletefromProcess1);
			String movetoproduction = "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,"
					+ "EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
					+ "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE,RENEWALFLAG,CARD_COLLECT_BRANCH,CARD_FLAG,BULK_REG_ID)"
					+ "(SELECT INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY, CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"
					+ CARDACTIVATEDCODE + "',CAF_REC_STATUS," + status_code
					+ ",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"
					+ "PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'" + maker_id
					+ "',(SYSDATE),CHECKER_ID,CHECKER_DATE,'P',SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
					+ "ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0'," + reissuedate + ",'" + reissue_count
					+ "'," + repindate + ",'" + repincount + "'," + damgedate + "," + blockdate + "," + hotdate + ","
					+ closedte + ",PIN_OFFSET,'" + pinretry_count
					+ "',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE,RENEWALFLAG,CARD_COLLECT_BRANCH,'P',BULK_REG_ID FROM "
					+ tablename + " " + "WHERE INST_ID='" + instid + "' AND " + cardcon + "='" + cardno + "')";
			trace("movetoproduction : " + movetoproduction);
			

			// 1

			String movetoproduction1 = "INSERT INTO CARD_PRODUCTION_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,"
					+ "PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)  (SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,"
					+ "CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE, " + userid
					+ ",(SYSDATE),CHECKER_ID,CHECKER_DATE FROM " + "PERS_CARD_PROCESS_HASH WHERE INST_ID='" + instid
					+ "'AND HCARD_NO='" + hashChn + "')";

			trace("movetoproduction1 query---->   " + movetoproduction1);

			/*
			 * String custexistqry =
			 * "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO WHERE CUSTID= '" +
			 * custcin + "'"; String custcount = (String)
			 * jdbctemplate.queryForObject(custexistqry, String.class);
			 */

			// by gowtham-270819
			String custexistqry = "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO WHERE CUSTID= ?";
			String custcount = (String) jdbctemplate.queryForObject(custexistqry, new Object[] { custcin },
					String.class);

			StringBuilder cinf_4 = new StringBuilder();

			/*
			 * cinf_4.append("INSERT INTO EZCUSTOMERINFO "); cinf_4.append(
			 * "(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) "
			 * ); cinf_4.append("VALUES "); cinf_4.append( "('" + instid + "','"
			 * + CUSTID_4 + "','" + NAME_4 + "','" + DOB_4 + "','" +
			 * SPOUSENAME_4 + "', "); cinf_4.append("'" + ADDRESS1_4 + "','" +
			 * ADDRESS2_4 + "','" + ADDRESS3_4 + "','" + OFFPHONE_4 + "','" +
			 * MOBILE_4 + "','" + EMAIL_4 + "','" + RESPHONE_4 + "') ");
			 * 
			 * enctrace("cinf_4:::::" + cinf_4.toString() + "custexistqry" +
			 * custexistqry + "custcount" + custcount); trace(
			 * "coming to else condition for customer"); try { if
			 * ("0".equalsIgnoreCase(custcount)) { trace(
			 * "coming to else condition for customer id not available "); res_4
			 * = jdbctemplate.update(cinf_4.toString());
			 */

			// by gowtham-270819
			cinf_4.append("INSERT INTO EZCUSTOMERINFO ");
			cinf_4.append(
					"(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) ");
			cinf_4.append("VALUES ");
			cinf_4.append("(?,?,?,?,?, ");
			cinf_4.append("?,?,?,?,?,?,?) ");

			enctrace("cinf_4:::::" + cinf_4.toString() + "custexistqry" + custexistqry + "custcount" + custcount);
			trace("coming to else condition for customer");
			try {
				if ("0".equalsIgnoreCase(custcount)) {
					trace("coming to else condition for customer id not available ");
					res_4 = jdbctemplate.update(cinf_4.toString(),
							new Object[] { instid, CUSTID_4, NAME_4, DOB_4, SPOUSENAME_4, ADDRESS1_4, ADDRESS2_4,
									ADDRESS3_4, OFFPHONE_4, MOBILE_4, EMAIL_4, RESPHONE_4 });

				}

				else {
					trace("coming to else condition for customer id check");
					res_4 = 1;
				}
			} catch (Exception e) {
				trace("Exception in moving production :: 1:::::::" + e);
				return -1;
			}
			// 2
			StringBuilder crdinf_3 = new StringBuilder();

			/*
			 * crdinf_3.append("INSERT INTO EZCARDINFO "); crdinf_3.append(
			 * "(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) "
			 * ); crdinf_3.append("VALUES "); crdinf_3.append("('" + instid +
			 * "','" + hashChn + "','" + CARDTYPE_3 + "','" + CUSTID_3 + "','" +
			 * TXNGROUPID_3 + "','" + LIMITFLAG_3 + "',TO_DATE('" + EXPIRYDATE_3
			 * + "','MM/DD/YYYY'),'" + STATUS + "','" + PINOFFSET_3 + "','" +
			 * OLDPINOFFSET_3 + "',"); crdinf_3.append("'" + TPINOFFSET_3 +
			 * "','" + OLDTPINOFFSET_3 + "','" + PINRETRYCOUNT_3 + "','" +
			 * TPINRETRYCOUNT_3 + "','" + PVKI_3 + "',TO_DATE('" + LASTTXNDATE_3
			 * + "','MM/DD/YYYY'),'" + LASTTXNTIME_3 + "'  ,'0' ,");
			 * crdinf_3.append("'" + PANSEQNO_3 + "' )");
			 * 
			 * enctrace("crdinf_3:::::" + crdinf_3.toString());
			 * 
			 * try {
			 * 
			 * res_3 = jdbctemplate.update(crdinf_3.toString());
			 */

			crdinf_3.append("INSERT INTO EZCARDINFO ");
			crdinf_3.append(
					"(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
			crdinf_3.append("VALUES ");
			crdinf_3.append("(?,?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?,?,?,");
			crdinf_3.append("?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),? ,? ,");
			crdinf_3.append("? )");
			enctrace("crdinf_3:::::" + crdinf_3.toString());
			try {
				res_3 = jdbctemplate.update(crdinf_3.toString(),
						new Object[] { instid, hashChn, CARDTYPE_3, CUSTID_3, TXNGROUPID_3, LIMITFLAG_3, EXPIRYDATE_3,
								STATUS, PINOFFSET_3, OLDPINOFFSET_3, TPINOFFSET_3, OLDTPINOFFSET_3, PINRETRYCOUNT_3,
								TPINRETRYCOUNT_3, PVKI_3, LASTTXNDATE_3, LASTTXNTIME_3, "0", PANSEQNO_3 });

			} catch (Exception e) {
				trace("Exception in moving production :: 1:::::::" + e);
				return -1;
			}
			// 3

			/*
			 * String customerinfo = "SELECT * FROM ACCOUNTINFO WHERE CIN='" +
			 * CUSTID_4 + "' "; List ACCOUNTS =
			 * jdbctemplate.queryForList(customerinfo);
			 */

			// by gowtham-270819
			String customerinfo = "SELECT * FROM ACCOUNTINFO WHERE CIN=? ";
			List ACCOUNTS = jdbctemplate.queryForList(customerinfo, new Object[] { CUSTID_4 });

			String accountno = "", ORDER_REF_NO = "", CIN = "", ACCTTYPE_ID = "", ACCTSUB_TYPE_ID = "",
					ACCT_CURRENCY = "", ACCOUNTTYPEcdtp = "";
			Iterator allaccounts = ACCOUNTS.iterator();
			while (allaccounts.hasNext()) {
				Map mp1 = (Map) allaccounts.next();
				// fname = (String)mp.get("FNAME");
				ORDER_REF_NO = (String) mp1.get("ORDER_REF_NO");
				accountno = (String) mp1.get("ACCOUNTNO");
				CIN = (String) mp1.get("CIN");
				ACCTTYPE_ID = (String) mp1.get("ACCTTYPE_ID");
				ACCTSUB_TYPE_ID = (String) mp1.get("ACCTSUB_TYPE_ID");
				ACCT_CURRENCY = (String) mp1.get("ACCT_CURRENCY");

				ACCOUNTTYPEcdtp = (String) mp1.get("ACCOUNTTYPE");

				/*
				 * String acctexistqry =
				 * "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO WHERE ACCOUNTNO= '"
				 * + accountno + "'"; String acctexist = (String)
				 * jdbctemplate.queryForObject(acctexistqry, String.class);
				 */

				// BY GOWWTHAM-270819
				String acctexistqry = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO WHERE ACCOUNTNO= ?";
				String acctexist = (String) jdbctemplate.queryForObject(acctexistqry, new Object[] { accountno },
						String.class);

				StringBuilder ezac_2 = new StringBuilder();

				/*
				 * ezac_2.append("INSERT INTO EZACCOUNTINFO "); ezac_2.append(
				 * "(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) "
				 * ); ezac_2.append("VALUES "); ezac_2.append("('" + instid +
				 * "','" + accountno + "','" + ACCTTYPE_ID + "','" +
				 * ACCT_CURRENCY + "','" + AVAILBAL_2 + "','" + LEDGERBAL_2 +
				 * "','" + LIMITFLAG_2 + "','" + STATUS_2 + "',");
				 * ezac_2.append("'" + TXNGROUPID_2 + "',TO_DATE('" +
				 * LASTTXNDATE_2 + "','MM/DD/YYYY'),'" + LASTTXNTIME_2 + "','" +
				 * BRANCHCODE_2 + "','" + PRODUCTCODE_2 + "' )");
				 * enctrace("ezac_2::::" + ezac_2.toString());
				 * 
				 * try { if ("0".equalsIgnoreCase(acctexist)) { res_2 =
				 * jdbctemplate.update(ezac_2.toString());
				 */

				// by gowtham-270819
				ezac_2.append("INSERT INTO EZACCOUNTINFO ");
				ezac_2.append(
						"(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
				ezac_2.append("VALUES ");
				ezac_2.append("(?,?,?,?,?,?,?,?,");
				ezac_2.append("?,TO_DATE(?,'MM/DD/YYYY'),?,?,? )");
				enctrace("ezac_2::::" + ezac_2.toString());

				try {
					if ("0".equalsIgnoreCase(acctexist)) {
						res_2 = jdbctemplate.update(ezac_2.toString(),
								new Object[] { instid, accountno, ACCTTYPE_ID, ACCT_CURRENCY, AVAILBAL_2, LEDGERBAL_2,
										LIMITFLAG_2, STATUS_2, TXNGROUPID_2, LASTTXNDATE_2, LASTTXNTIME_2, BRANCHCODE_2,
										PRODUCTCODE_2 });

					} else {
						res_2 = 1;
					}

				} catch (Exception e) {
					trace("Exception in moving production :: 1:::::::" + e);
					return -1;
				}

				/*
				 * String authrelcountqry3 =
				 * "select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='" +
				 * CHN_1 + "' "; int ezauthcount3 =
				 * jdbctemplate.queryForInt(authrelcountqry3);
				 */

				/// BY GOWTHAM-270819
				String authrelcountqry3 = "select count(ACCOUNTNO) as cnt from EZAUTHREL where chn=? ";
				//int ezauthcount3 = jdbctemplate.queryForInt(authrelcountqry3, new Object[] { CHN_1 });
				//modified by gowtham on 18nov2020
				int ezauthcount3 = jdbctemplate.queryForInt(authrelcountqry3, new Object[] { hashChn });

				// 4
				if (ezauthcount3 == 0) {
					StringBuilder authrel_1 = new StringBuilder();
					/*
					 * authrel_1.append("INSERT INTO EZAUTHREL ");
					 * authrel_1.append(
					 * "(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) "
					 * ); authrel_1.append("VALUES "); authrel_1.append("('" +
					 * instid + "','" + hashChn + "','" + accountno + "','" +
					 * ACCTTYPE_ID + "','" + ACCOUNTFLAG_1 + "','" +
					 * ACCOUNTPRIORITY_1 + "','" + ACCT_CURRENCY + "') ");
					 * enctrace("authrel_1::::" + authrel_1.toString());
					 * 
					 * try {
					 * 
					 * res_1 = jdbctemplate.update(authrel_1.toString());
					 */

					// BY GOWTHAM-270819
					authrel_1.append("INSERT INTO EZAUTHREL ");
					authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
					authrel_1.append("VALUES ");
					authrel_1.append("(?,?,?,?, ?,?,?) ");
					enctrace("authrel_1::::" + authrel_1.toString());

					try {

						res_1 = jdbctemplate.update(authrel_1.toString(), new Object[] { instid, hashChn, accountno,
								ACCTTYPE_ID, ACCOUNTFLAG_1, ACCOUNTPRIORITY_1, ACCT_CURRENCY });

					} catch (Exception e) {
						trace("Exception in moving production :: 1:::::::" + e);
						return -1;
					}

				} else {

					StringBuilder authrel_1 = new StringBuilder();

					/*
					 * authrel_1.append("INSERT INTO EZAUTHREL ");
					 * authrel_1.append(
					 * "(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) "
					 * ); authrel_1.append("VALUES "); authrel_1.append("('" +
					 * instid + "','" + hashChn + "','" + accountno + "','" +
					 * ACCTTYPE_ID + "','S','2','" + ACCT_CURRENCY + "') ");
					 * enctrace("authrel_1::::" + authrel_1.toString());
					 * 
					 * try {
					 * 
					 * res_1 = jdbctemplate.update(authrel_1.toString());
					 */

					// by gowtham-270819
					authrel_1.append("INSERT INTO EZAUTHREL ");
					authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
					authrel_1.append("VALUES ");
					authrel_1.append("(?,?,?, ?,?,?,?) ");
					enctrace("authrel_1::::" + authrel_1.toString());

					try {
						res_1 = jdbctemplate.update(authrel_1.toString(),
								new Object[] { instid, hashChn, accountno, ACCTTYPE_ID, "S", "2", ACCT_CURRENCY, });

					} catch (Exception e) {
						trace("Exception in moving production :: 1:::::::" + e);
						return -1;
					}

				}
				// 5

			}

			String INSTID_5 = "", LIMITTYPE_5 = "", LIMITID_5 = "", TXNCODE_5 = "", CURRCODE_5 = "", AMOUNT_5 = "",
					COUNT_5 = "", WAMOUNT_5 = "", WCOUNT_5 = "", MAMOUNT_5 = "", MCOUNT_5 = "", YAMOUNT_5 = "",
					YCOUNT_5 = "", LIMITDATE_5 = "";

			StringBuilder accinfo_5 = new StringBuilder();
			accinfo_5.append("SELECT ");
			// --EZACCUMINFO start
			// --INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT,
			// WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE
			accinfo_5.append("INSTID INSTID_5, CASE  LIMITTYPE ");
			accinfo_5.append("WHEN 'CDTP'  THEN 'CARD' ");
			accinfo_5.append("WHEN 'CARD' THEN 'CARD' ");
			accinfo_5.append("WHEN 'ACTP'  THEN 'ACCT' ");
			accinfo_5.append("WHEN 'ACCT' THEN 'ACCT' END LIMITTYPE_5 ,");
			accinfo_5.append("CASE  LIMITTYPE ");
			if (padssenable.equals("Y")) {
				accinfo_5.append(" WHEN 'CDTP'  THEN '" + cardno + "'");
				accinfo_5.append(" WHEN 'CARD' THEN '" + cardno + "' ");
			}
			// wait i wil come
			{
			}
			accinfo_5.append(" WHEN 'ACTP'  THEN '" + ACCOUNTNO_2 + "' ");
			accinfo_5.append(" WHEN 'ACCT' THEN '" + ACCOUNTNO_2 + "' END LIMITID_5 ,");
			accinfo_5.append(" TXNCODE TXNCODE_5, CURRCODE CURRCODE_5, AMOUNT AMOUNT_5, COUNT COUNT_5, ");
			accinfo_5.append(
					"WAMOUNT WAMOUNT_5, WCOUNT WCOUNT_5, MAMOUNT MAMOUNT_5, MCOUNT MCOUNT_5, YAMOUNT YAMOUNT_5, YCOUNT YCOUNT_5, ");
			accinfo_5.append("(select TO_CHAR(AUTH_DATE,'DD-MON-YYYY') from LIMIT_DESC where INSTID='" + instid
					+ "' AND LIMIT_ID='" + LIMITRECID_5 + "') LIMITDATE_5 FROM LIMITINFO ");
			accinfo_5.append("WHERE INSTID='" + instid + "' AND LIMIT_RECID = '" + LIMITRECID_5 + "'");
			// --EZACCUMINFO end
			enctrace("accinfo_5:::::" + accinfo_5.toString());

			List movetoAccuminfo = jdbctemplate.queryForList(accinfo_5.toString());
			trace("movetoAccuminfo:::::::::::" + movetoAccuminfo.size() + ":::::::" + movetoAccuminfo);
			Iterator accitr = movetoAccuminfo.iterator();
			int as = 0;
			int incCount = 0;
			while (accitr.hasNext()) {
				//// System.out.println("testing
				//// :::::::::::::::::::::::1"+as++);
				incCount = incCount + 1;

				Map mp2 = (Map) accitr.next();
				INSTID_5 = mp2.get("INSTID_5").toString();
				//// System.out.println("testing
				//// :::::::::::::::::::::::2"+as++);
				LIMITTYPE_5 = mp2.get("LIMITTYPE_5").toString();
				//// System.out.println("testing
				//// :::::::::::::::::::::::3"+as++);
				LIMITID_5 = mp2.get("LIMITID_5").toString();
				//// System.out.println("testing
				//// :::::::::::::::::::::::4"+mp2.get("LIMITID_5").toString());
				TXNCODE_5 = mp2.get("TXNCODE_5").toString();
				;
				//// System.out.println("testing
				//// :::::::::::TXNCODE_5::::::::::::4"+mp2.get("TXNCODE_5").toString());

				CURRCODE_5 = mp2.get("CURRCODE_5").toString();
				;
				//// System.out.println("testing
				//// :::::::::::currr::::::::::"+mp2.get("CURRCODE_5").toString());
				AMOUNT_5 = mp2.get("AMOUNT_5").toString();
				;
				//// System.out.println("testing :::::::::::::::::::::::"+as++);
				COUNT_5 = mp2.get("COUNT_5").toString();
				//// System.out.println("testing :::::::::::::::::::::::"+as++);
				WAMOUNT_5 = mp2.get("WAMOUNT_5").toString();
				//// System.out.println("testing :::::::::::::::::::::::"+as++);
				WCOUNT_5 = mp2.get("WCOUNT_5").toString();
				//// System.out.println("testing :::::::::::::::::::::::"+as++);
				MAMOUNT_5 = mp2.get("MAMOUNT_5").toString();
				//// System.out.println("testing :::::::::::::::::::::::"+as++);
				MCOUNT_5 = mp2.get("MCOUNT_5").toString();
				//// System.out.println("testing :::::::::::::::::::::::"+as++);
				YAMOUNT_5 = mp2.get("YAMOUNT_5").toString();
				YCOUNT_5 = mp2.get("YCOUNT_5").toString();
				//// System.out.println("testing :::::::::::::::::::::::"+as++);
				LIMITDATE_5 = mp2.get("LIMITDATE_5").toString();
				//// System.out.println("testing :::::::::::::::::::::::"+as++);

				String getres_5Query = getres_5Query(instid, LIMITTYPE_5, LIMITID_5, TXNCODE_5, CURRCODE_5, AMOUNT_5,
						COUNT_5, WAMOUNT_5, WCOUNT_5, MAMOUNT_5, MCOUNT_5, YAMOUNT_5, YCOUNT_5, LIMITDATE_5);

				enctrace("getres_5Query::" + getres_5Query);
				try {

					res_5 = jdbctemplate.update(getres_5Query.toString());
				} catch (Exception e) {
					trace("Exception in moving production :: 1:::::::" + e);
					return -1;
				}

				// AccinfiInsert = "";
			}

			StringBuilder update_production = new StringBuilder();

			/*
			 * update_production.append(
			 * "UPDATE PERS_CARD_PROCESS  SET CARD_STATUS='" + cardstatus +
			 * "', STATUS_CODE='" + switchstatus +
			 * "',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"
			 * + userid + "', "); update_production .append(
			 * "MAKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' and " +
			 * cardcon + "='" + cardno + "'");
			 */

			// by gowtham-270819
			update_production.append(
					"UPDATE PERS_CARD_PROCESS  SET CARD_STATUS=?, STATUS_CODE=?,MKCK_STATUS = ? ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID=? , ");
			update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID=? and " + cardcon + "=?");

			// enctrace("update query for instcard processs
			// "+update_production);

			int production_insert = -1;
			int deletefromprocess1 = -1;
			int deletefromprocess = -1, upd_process = -1;
			;
			int production_insert1 = -1;

			try {

				/*
				 * upd_process =
				 * jdbctemplate.update(update_production.toString());
				 */

				/// by gowtham270819
				/*
				 * upd_process =
				 * jdbctemplate.update(update_production.toString());
				 */
				upd_process = jdbctemplate.update(update_production.toString(),
						new Object[] { cardstatus, switchstatus, "P", userid, instid, cardno });
				production_insert = jdbctemplate.update(movetoproduction);
				production_insert1 = jdbctemplate.update(movetoproduction1);
				deletefromprocess = jdbctemplate.update(deletefromProcess);
				deletefromprocess1 = jdbctemplate.update(deletefromProcess1);
			} catch (Exception e) {
				trace("Exception in moving production :: 2:::" + e);
				return -1;
			}

			enctrace("\n--------------------------------------Move to Production Query");
			// trace("####### custinfo_status : "+custinfo_status);
			trace("result :::::::::::" + res_1 + res_2 + res_3 + res_4 + incCount + production_insert
					+ deletefromprocess);
			// if(res_1==1 && res_2 ==1 && res_3 ==1 && res_4 ==1 && incCount>0
			// && production_insert >0 && deletefromprocess >0 && upd_process
			// >0)
			if (res_1 == 1 && res_2 == 1 && res_3 == 1 && res_4 == 1 && incCount > 0 && deletefromprocess1 > 0
					&& production_insert > 0 && deletefromprocess > 0 && upd_process > 0 && production_insert1 > 0) {
				trace("PROCESS COMPLETED");
				issue_status = 1;
			} else {
				issue_status = -1;
			}

		}

		return issue_status;
	}

	public String getres_5Query(String INSTID_5, String LIMITTYPE_5, String LIMITID_5, String TXNCODE_5,
			String CURRCODE_5, String AMOUNT_5, String COUNT_5, String WAMOUNT_5, String WCOUNT_5, String MAMOUNT_5,
			String MCOUNT_5, String YAMOUNT_5, String YCOUNT_5, String LIMITDATE_5) {
		StringBuilder AccinfiInsert = new StringBuilder();
		AccinfiInsert.append("INSERT INTO EZACCUMINFO ");
		AccinfiInsert.append(
				"(INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE) ");
		AccinfiInsert.append("VALUES ");
		AccinfiInsert.append("('" + INSTID_5 + "','" + LIMITTYPE_5 + "','" + LIMITID_5 + "','" + TXNCODE_5 + "','"
				+ CURRCODE_5 + "','0',");
		AccinfiInsert.append("'0','0','0','0','0','0','0','" + LIMITDATE_5 + "' )");

		return AccinfiInsert.toString();
	}

	public int updateCustomerdetailsinproduction(String instid, String cinno, JdbcTemplate jdbctemplate) {
		int update_staus = -1;
		/*
		 * String getcust_qury =
		 * "SELECT INST_ID,CIN,FNAME,MNAME,LNAME,FATHER_NAME,MOTHER_NAME,MARITAL_STATUS,SPOUSE_NAME,GENDER,to_char(DOB,'DD-MON-RR') AS DOB,NATIONALITY,EMAIL_ADDRESS,MOBILE_NO,PHONE_NO,OCCUPATION,ID_NUMBER,ID_DOCUMENT,SECUTIRY_QUESTION,SECURITY_ANSWER,to_char(MAKER_DATE,'DD-MON-RR') as MAKER_DATE,MAKER_ID,to_char(CHECKER_DATE,'DD-MON-RR') as CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS,POST_ADDR1,POST_ADDR2,POST_ADDR3,POST_ADDR4,RES_ADDR1,RES_ADDR2,RES_ADDR3,RES_ADDR4,KYC_FLAG FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"
		 * + instid + "' AND CIN='" + cinno + "'"; //// System.out.println(
		 * "getcust_qury===> "+getcust_qury); List custinfolist =
		 * jdbctemplate.queryForList(getcust_qury);
		 */

		// by gowtham270819
		String getcust_qury = "SELECT INST_ID,CIN,FNAME,MNAME,LNAME,FATHER_NAME,MOTHER_NAME,MARITAL_STATUS,SPOUSE_NAME,GENDER,to_char(DOB,'DD-MON-RR') AS DOB,NATIONALITY,EMAIL_ADDRESS,MOBILE_NO,PHONE_NO,OCCUPATION,ID_NUMBER,ID_DOCUMENT,SECUTIRY_QUESTION,SECURITY_ANSWER,to_char(MAKER_DATE,'DD-MON-RR') as MAKER_DATE,MAKER_ID,to_char(CHECKER_DATE,'DD-MON-RR') as CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS,POST_ADDR1,POST_ADDR2,POST_ADDR3,POST_ADDR4,RES_ADDR1,RES_ADDR2,RES_ADDR3,RES_ADDR4,KYC_FLAG FROM IFP_CUSTINFO_PROCESS WHERE INST_ID=? AND CIN=?";
		//// System.out.println("getcust_qury===> "+getcust_qury);
		List custinfolist = jdbctemplate.queryForList(getcust_qury, new Object[] { instid, cinno });

		//// System.out.println("Customer Details List ====> "+custinfolist);
		if (!(custinfolist.isEmpty())) {
			String dob = "NO_DATA", fname = "NO_DATA", mname = "NO_DATA", lname = "NO_DATA", father_name = "NO_DATA",
					mother_name = "NO_DATA", marital_status = "NO_DATA", spouse_name = "NO_DATA", gender = "NO_DATA",
					nationality = "NO_DATA", email_address = "NO_DATA", mobile_no = "NO_DATA", phone_no = "NO_DATA",
					occupation = "NO_DATA", id_number = "NO_DATA", id_document = "NO_DATA",
					secutiry_question = "NO_DATA", security_answer = "NO_DATA", maker_id = "NO_DATA",
					checker_id = "NO_DATA", mkck_status = "NO_DATA", customer_status = "NO_DATA",
					post_addr1 = "NO_DATA", post_addr2 = "NO_DATA", post_addr3 = "NO_DATA", post_addr4 = "NO_DATA",
					res_addr1 = "NO_DATA", res_addr2 = "NO_DATA", res_addr3 = "NO_DATA", res_addr4 = "NO_DATA",
					kyc_flag = "1";
			String maker_date = null, checker_date = null;
			Iterator custitr = custinfolist.iterator();
			while (custitr.hasNext()) {
				Map citmap = (Map) custitr.next();
				fname = (String) citmap.get("FNAME");
				mname = (String) citmap.get("MNAME");
				lname = (String) citmap.get("LNAME");
				father_name = (String) citmap.get("FATHER_NAME");
				mother_name = (String) citmap.get("MOTHER_NAME");
				marital_status = (String) citmap.get("MARITAL_STATUS");
				spouse_name = (String) citmap.get("SPOUSE_NAME");
				gender = (String) citmap.get("GENDER");
				dob = (String) citmap.get("DOB");
				nationality = (String) citmap.get("NATIONALITY");
				email_address = (String) citmap.get("EMAIL_ADDRESS");
				mobile_no = (String) citmap.get("MOBILE_NO");
				phone_no = (String) citmap.get("PHONE_NO");
				occupation = (String) citmap.get("OCCUPATION");
				id_number = (String) citmap.get("ID_NUMBER");
				id_document = (String) citmap.get("ID_DOCUMENT");
				secutiry_question = (String) citmap.get("SECUTIRY_QUESTION");
				security_answer = (String) citmap.get("SECURITY_ANSWER");
				maker_date = (String) citmap.get("MAKER_DATE");
				maker_id = (String) citmap.get("MAKER_ID");
				checker_date = (String) citmap.get("CHECKER_DATE");
				checker_id = (String) citmap.get("CHECKER_ID");
				mkck_status = (String) citmap.get("MKCK_STATUS");
				customer_status = (String) citmap.get("CUSTOMER_STATUS");
				post_addr1 = (String) citmap.get("POST_ADDR1");
				post_addr2 = (String) citmap.get("POST_ADDR2");
				post_addr3 = (String) citmap.get("POST_ADDR3");
				post_addr4 = (String) citmap.get("POST_ADDR4");
				res_addr1 = (String) citmap.get("RES_ADDR1");
				res_addr2 = (String) citmap.get("RES_ADDR2");
				res_addr3 = (String) citmap.get("RES_ADDR3");
				res_addr4 = (String) citmap.get("RES_ADDR4");
			}
			/*
			 * String custinfo_updatetoprod =
			 * "UPDATE IFP_CUSTINFO_PRODUCTION SET FNAME='" + fname +
			 * "',MNAME='" + mname + "',LNAME='" + lname + "',FATHER_NAME='" +
			 * father_name + "',MOTHER_NAME='" + mother_name +
			 * "',MARITAL_STATUS='" + marital_status + "',SPOUSE_NAME='" +
			 * spouse_name + "',GENDER='" + gender + "',DOB='" + dob +
			 * "',NATIONALITY='" + nationality + "',EMAIL_ADDRESS='" +
			 * email_address + "',MOBILE_NO='" + mobile_no + "',PHONE_NO='" +
			 * phone_no + "',OCCUPATION='" + occupation + "',ID_NUMBER='" +
			 * id_number + "',ID_DOCUMENT='" + id_document +
			 * "',SECUTIRY_QUESTION='" + secutiry_question +
			 * "',SECURITY_ANSWER='" + security_answer + "',MAKER_DATE='" +
			 * maker_date + "',MAKER_ID='" + maker_id + "',CHECKER_DATE='" +
			 * checker_date + "',CHECKER_ID='" + checker_id + "',MKCK_STATUS='"
			 * + mkck_status + "',CUSTOMER_STATUS='" + customer_status +
			 * "',POST_ADDR1='" + post_addr1 + "',POST_ADDR2='" + post_addr2 +
			 * "',POST_ADDR3='" + post_addr3 + "',POST_ADDR4='" + post_addr4 +
			 * "',RES_ADDR1='" + res_addr1 + "',RES_ADDR2='" + res_addr2 +
			 * "',RES_ADDR3='" + res_addr3 + "',RES_ADDR4='" + res_addr4 +
			 * "',KYC_FLAG='" + kyc_flag + "' WHERE INST_ID='" + instid +
			 * "' AND CIN='" + cinno + "'"; //// System.out.println("UPDATE
			 * CUSTINFPO QURY ====> //// "+custinfo_updatetoprod);
			 * 
			 * update_staus = jdbctemplate.update(custinfo_updatetoprod); ////
			 * System.out.println("CUST INFO UPDATE STATUS =====> ////
			 * "+update_staus);
			 */

			String custinfo_updatetoprod = "UPDATE IFP_CUSTINFO_PRODUCTION SET FNAME=?,MNAME=?,LNAME=?,FATHER_NAME=?,MOTHER_NAME=?,MARITAL_STATUS=?,SPOUSE_NAME=?,GENDER=?,DOB=?,NATIONALITY=?,EMAIL_ADDRESS=?,MOBILE_NO=?,PHONE_NO=?,OCCUPATION=?,ID_NUMBER=?,ID_DOCUMENT=?,SECUTIRY_QUESTION=?,SECURITY_ANSWER=?,MAKER_DATE=?,MAKER_ID=?,CHECKER_DATE=?,CHECKER_ID=?,MKCK_STATUS=?,CUSTOMER_STATUS=?,POST_ADDR1=post_addr1,post_addr2,post_addr3,post_addr4,res_addr1,res_addr2,res_addr3,res_addr4,KYC_FLAG=? WHERE INST_ID=?";

			update_staus = jdbctemplate.update(custinfo_updatetoprod,
					new Object[] { fname, lname, father_name, mother_name, marital_status, spouse_name, gender, dob,
							nationality, email_address, mobile_no, phone_no, occupation, id_number, id_document,
							secutiry_question, security_answer, maker_date, maker_id, checker_date, checker_id,
							mkck_status, customer_status, post_addr1, post_addr2, post_addr3, post_addr4, res_addr1,
							res_addr2, res_addr3, res_addr4, kyc_flag, instid, cinno });

		}

		return update_staus;
	}

	public String authorizeCardissuehome() {
		List pers_prodlist, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		HttpSession session = getRequest().getSession();
		String cardStatus = "05", mkrstatus = "M";
		session.setAttribute("ACTIONTYPE", act);
		try {
			if (usertype.equals("INSTADMIN")) {
				//// System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				if (!(br_list.isEmpty())) {
					setBranch_list(br_list);

				} else {
					addActionError("No Cards Waiting to Authorize...");
					//// System.out.println("Branch List is empty ");
					return "required_home";
				}
			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			trace("pers_prodlist---->" + pers_prodlist);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				//// System.out.println("Product List is ===> "+pers_prodlist);
			} else {
				//// System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
		}

		return "authcardissuehome";
	}

	private List cardissueauthlist;

	public List getCardissueauthlist() {
		return cardissueauthlist;
	}

	public void setCardissueauthlist(List cardissueauthlist) {
		this.cardissueauthlist = cardissueauthlist;
	}

	public String cardIssueorderauthlist() {
		HttpSession session = getRequest().getSession();

		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		//// System.out.println("branch==> "+branch+" binno==>"+binno+"
		//// fromdate===> "+fromdate+" todate===>"+todate);
		String inst_id = comInstId(), dateflag = "ISSUE_DATE", cardstatus = "05", mkckstatus = "M", cafstatus = "N";
		List authcardissuelist = null;
		this.fromdate = fromdate;
		this.todate = todate;
		try {

			String condition = commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			//// System.out.println("Condition Value-----> "+condition);
			// authcardissuelist = commondesc.personaliseCardauthlist(
			//// inst_id,cardstatus, mkckstatus,condition);
			authcardissuelist = commondesc.maintenanceCardslist(inst_id, cardstatus, mkckstatus, cafstatus, condition,
					jdbctemplate);
			// authcardissuelist = commondesc.personaliseCardauthlist(dateflag,
			// inst_id, fromdate, todate, cardstatus, mkckstatus, branch,
			// binno);
			if (!(authcardissuelist.isEmpty())) {
				setCardissueauthlist(authcardissuelist);
				String padssenable = commondesc.checkPadssEnable(inst_id, jdbctemplate);
				setPadssenabled(padssenable);
				//// System.out.println("padssenablepadssenablepadssenable::"+getPadssenabled());
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");

			} else {
				addActionError(" No Issue Card Orders to Authorize ");
				return authorizeCardissuehome();
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error While get Orders " + e.getMessage());
		}
		return "authcardissueorders";
	}

	public String cardIssueorderauthorize() {
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("ISSUEAUTH", txManager);
		String instid = comInstId();
		String userid = comUserId();
		String username = comUsername();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		//// System.out.println("Total Orders Selected ===>
		//// "+order_refnum.length);
		if (getRequest().getParameter("authorize") != null) {
			//// System.out.println( "AUTHORIZE..........." );
			authstatus = "P";
			statusmsg = " Authorized ";
			err_msg = "Authorize";
		} else if (getRequest().getParameter("deauthorize") != null) {
			//// System.out.println( "DE AUTHORIZE..........." );
			authstatus = "D";
			statusmsg = " De-Authorized ";
			err_msg = "De-Authorize";
		}

		try {
			int update_cnt = 0;
			int ordercount = order_refnum.length;
			for (int i = 0; i < ordercount; i++) {
				int check = 0;

				/*
				 * String update_authdeauth_qury =
				 * "UPDATE PERS_CARD_PROCESS SET MKCK_STATUS='" + authstatus +
				 * "',CHECKER_ID='" + userid +
				 * "',CHECKER_DATE=(sysdate) WHERE INST_ID='" + instid + "' ";
				 * String updatequry = update_authdeauth_qury + " AND CARD_NO='"
				 * + order_refnum[i] + "'"; check =
				 * jdbctemplate.update(updatequry);
				 */

				// by gowtham-260819
				String update_authdeauth_qury = "UPDATE PERS_CARD_PROCESS SET MKCK_STATUS=?,CHECKER_ID=?,CHECKER_DATE=(sysdate) WHERE INST_ID=? ";
				String updatequry = update_authdeauth_qury + " AND CARD_NO=?";
				check = jdbctemplate.update(updatequry, new Object[] { authstatus, userid, instid, order_refnum[i] });

				if (check > 0) {
					update_cnt = update_cnt + 1;

					try {

						String mcardno = commondesc.getMaskedCardNoFromProd(instid, order_refnum[i], "C", jdbctemplate);
						if (mcardno == null) {
							mcardno = order_refnum[i];
						}

						// added by gowtham_220719
						trace("ip address======>  " + ip);
						auditbean.setIpAdress(ip);

						auditbean.setActmsg("card Issued   " + update_cnt + "   [ " + statusmsg + " ]");
						auditbean.setUsercode(username);
						auditbean.setAuditactcode("0106");
						// auditbean.setCardno(mcardno);
						auditbean.setRemarks(statusmsg);
						// auditbean.setCardnumber(order_refnum[i]);
						// auditbean.setProduct(persorderdetails.product_code);
						// commondesc.insertAuditTrail(instid, userid,
						// auditbean, jdbctemplate, txManager);
						commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
					} catch (Exception audite) {
						trace("Exception in auditran : " + audite.getMessage());
					}

				} else {
					addActionError("Unable to continue process");
					break;
				}
			}

			if (update_cnt == ordercount) {
				// commondesc.commitTxn(jdbctemplate);
				txManager.commit(transact.status);
				//// System.out.println("Order Status Updated Successfully and
				//// Commited ");
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", ordercount + " Order Updated Sucessfully");
			} else {
				// commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				//// System.out.println("Txn Got Rollbacked ---->");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Orders not updated ");
			}

		} catch (Exception e) {
			// commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			//// System.out.println("Txn Got Rollbacked ---->");
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error While Authorize " + e.getMessage());
		}

		return authorizeCardissuehome();
	}

	public int insertCourierTrackDetails(String instid, String corier_refid, String couriermasterid, String courierid,
			int noofcards, String courierdate, String agentid, String todaddress, String usercode,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		/*
		 * String couriterinsertqry =
		 * "INSERT INTO IFP_COURIER_TRACK (INST_ID, COURIERTRACKID, COURIERMASTER_ID, COURIERID, NOOFCARDS, ISSUEDBY, COURIERDATE, TOADDRESS, AGENTID ) VALUES "
		 * ; couriterinsertqry += "('" + instid + "','" + corier_refid + "','" +
		 * couriermasterid + "','" + courierid + "','" + noofcards + "','" +
		 * usercode + "',TO_DATE('" + courierdate + "','DD-MM-YYYY'),'" +
		 * todaddress + "','" + agentid + "')"; enctrace("couriterinsertqry  :"
		 * + couriterinsertqry); x = jdbctemplate.update(couriterinsertqry);
		 */

		// by gowtham-260819
		String couriterinsertqry = "INSERT INTO IFP_COURIER_TRACK (INST_ID, COURIERTRACKID, COURIERMASTER_ID, COURIERID, NOOFCARDS, ISSUEDBY, COURIERDATE, TOADDRESS, AGENTID ) VALUES ";
		couriterinsertqry += "(?,?,?,?,?,?,TO_DATE('" + courierdate + "','DD-MM-YYYY'),?,?)";
		enctrace("couriterinsertqry  :" + couriterinsertqry);
		x = jdbctemplate.update(couriterinsertqry, new Object[] { instid, corier_refid, couriermasterid, courierid,
				noofcards, usercode, todaddress, agentid });

		return x;
	}

	public String issueCardProcessAction() {
		HttpSession session = getRequest().getSession();
		String CARD_STATUS = "05";
		IfpTransObj transact = commondesc.myTranObject("ISSUE", txManager);
		String instid = comInstId();
		String usercode = comUserId();
		String couriercompanyid = getRequest().getParameter("couriercompanyid");
		String courierid = getRequest().getParameter("courierid");
		String sendingaddress = getRequest().getParameter("sendingaddress");
		String courierdate = getRequest().getParameter("courierdate");
		String[] cardslist = getRequest().getParameterValues("personalrefnum");
		String act = "M"; // (String)session.getAttribute("ACTIONTYPE");
		String mkckstatus = null, authmsg = null;
		String remarks = getRequest().getParameter("reason");
		String username = comUsername();

		try {

			if (cardslist == null) {
				addActionError("No Cards Selected");
				return "required_home";
			}

			if (act.equals("M")) {
				mkckstatus = "M";
				authmsg = " Waiting For Authorization";
			} else {
				mkckstatus = "P";
				authmsg = "";
			}

			String cardno = null;
			int updcnt = 0;
			String courierrefid = "";
			String iscourierenabled = commondesc.getCourierTrackEnabled(instid, jdbctemplate);
			trace("Getting Courier Enabled....got : " + iscourierenabled);
			if (iscourierenabled != null && iscourierenabled.equals("1")) {
				courierrefid = instid + couriercompanyid + courierid;
				this.insertCourierTrackDetails(instid, courierrefid, couriercompanyid, courierid, cardslist.length,
						courierdate, "", sendingaddress, usercode, jdbctemplate);
			}

			System.out.println("courierredfid===>" + courierrefid);

			for (int i = 0; i < cardslist.length; i++) {
				cardno = cardslist[i];

				/*
				 * String updatequry =
				 * "UPDATE PERS_CARD_PROCESS SET  CARD_STATUS='" + CARD_STATUS +
				 * "', MKCK_STATUS='" + mkckstatus + "',COURIER_ID='" +
				 * courierrefid + "', ISSUE_DATE=SYSDATE WHERE INST_ID='" +
				 * instid + "' AND ORG_CHN='" + cardno + "'";
				 * enctrace("updatequry===>"+updatequry); int upd =
				 * jdbctemplate.update(updatequry);
				 */

				/// by gowtham
				String updatequry = "UPDATE PERS_CARD_PROCESS SET  CARD_STATUS=?," + " MKCK_STATUS=?,COURIER_ID=? ,"
						+ " ISSUE_DATE=SYSDATE WHERE INST_ID=? AND ORG_CHN=? ";
				int upd = jdbctemplate.update(updatequry,
						new Object[] { CARD_STATUS, mkckstatus, courierrefid, instid, cardno });
				System.out.println("upd===>" + upd);
				if (upd == 1) {
					updcnt = updcnt + 1;

				} else {
					break;
				}
				String mcardno = commondesc.getMaskedCardNo(instid, cardno, "C", jdbctemplate);
				String cin=commondesc.getCinFromProcess(cardno, instid, "", jdbctemplate);
				auditbean.setActmsg(" Card(s) Issued Successfully " + authmsg + "  [ " + mcardno + " ]");
				auditbean.setCin(cin);
				auditbean.setCardno(mcardno);
			}
			
			System.out.println("updcnt===>" + updcnt);
			System.out.println("cardlist===>" + cardslist);

			if (cardslist.length != updcnt) {
				addActionError("Unable to Continue the process");
				txManager.rollback(transact.status);
				return this.personalCardissuehome();
			}

			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", updcnt + " Card(s) Issued Successfully." + authmsg);
			txManager.commit(transact.status);
			
			/************* AUDIT BLOCK **************/
			try {
			
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("0106");
				//auditbean.setProduct(bin);
				// auditbean.setCardnumber(order_refnum[i].toString());
				// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
				// jdbctemplate, txManager);
				commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				audite.printStackTrace();
				trace("Exception in auditran : " + audite.getMessage());
			}

			/************* AUDIT BLOCK **************/

		

			/*
			 * DefaultTransactionDefinition def = new
			 * DefaultTransactionDefinition();
			 * def.setPropagationBehavior(TransactionDefinition.
			 * PROPAGATION_REQUIRED); TransactionStatus status =
			 * txManager.getTransaction(def); String columns = "", condition =
			 * "", table = "PERS_CARD_PROCESS", result = ""; Connection conn =
			 * null; Dbcon dbcon = new Dbcon(); conn = dbcon.getDBConnection();
			 * CallableStatement cstmt = null; cstmt = conn.prepareCall(
			 * "call SP_COMMON_UPDATE(?,?,?,?,?,?)"); trace(
			 * "procedure--->call SP_COMMON_UPDATE(?,?,?,?,?)"); ArrayDescriptor
			 * arrDesc = ArrayDescriptor.createDescriptor("TVARCHAR2ARRAY",
			 * conn); System.out.println("check"); ARRAY array = new
			 * ARRAY(arrDesc, conn, cardslist); trace("proc args-->"
			 * +array+"--"+instid+"--"+usercode); cstmt.setString(1, table);
			 * cstmt.setArray(2, array); cstmt.setString(3, instid); columns =
			 * "CARD_STATUS='"+CARD_STATUS+"', MKCK_STATUS='"
			 * +mkckstatus+"',COURIER_ID='"+courierrefid+"', ISSUE_DATE=SYSDATE"
			 * ; condition = " WHERE INST_ID='"+instid+"' AND CARD_NO IN";
			 * cstmt.setString(4, columns); cstmt.setString(5, condition);
			 * cstmt.registerOutParameter(6,java.sql.Types.VARCHAR);
			 * cstmt.execute(); result=cstmt.getString(6);
			 * trace("result--->"+result);
			 * 
			 *//************* AUDIT BLOCK **************/
			/*
			 * try{ auditbean.setActmsg("Card  [ "+order_refnum.length+
			 * " ] Authorized Successfully "); auditbean.setActiontype("IC");
			 * auditbean.setUsercode(usercode);
			 * auditbean.setAuditactcode("0102");
			 * commondesc.insertAuditTrail(instid, usercode, auditbean,
			 * jdbctemplate, txManager); }catch(Exception audite ){ trace(
			 * "Exception in auditran : "+ audite.getMessage()); }
			 *//************* AUDIT BLOCK **************//*
														 * 
														 * 
														 * if(result.contains(
														 * 
														 * "successfully")){
														 * addActionMessage(
														 * "Cards Issue "
														 * +result+
														 * " ,Waiting for Issuance Authorization.."
														 * ); }else{
														 * addActionError(
														 * "unable to continue the process"
														 * ); }
														 * 
														 * 
														 * try{ //String mcardno
														 * = commondesc.
														 * getMaskedCardNo(
														 * instid,cardno,"C",
														 * jdbctemplate);
														 * //if(mcardno==null){
														 * mcardno=cardno;}
														 * auditbean.setActmsg(
														 * "card Issued   "
														 * +updcnt + "   [ "
														 * +authmsg+" ]");
														 * auditbean.setUsercode
														 * (username);
														 * auditbean.
														 * setAuditactcode(
														 * "0106");
														 * //auditbean.setCardno
														 * (mcardno);
														 * auditbean.setRemarks(
														 * authmsg);
														 * //auditbean.
														 * setCardnumber(cardno)
														 * ; //
														 * auditbean.setProduct(
														 * persorderdetails.
														 * product_code);
														 * //commondesc.
														 * insertAuditTrail(
														 * instid, userid,
														 * auditbean,
														 * jdbctemplate,
														 * txManager);
														 * commondesc.
														 * insertAuditTrailPendingCommit
														 * (instid, username,
														 * auditbean,
														 * jdbctemplate,
														 * txManager);
														 * }catch(Exception
														 * audite ){ trace(
														 * "Exception in auditran : "
														 * +
														 * audite.getMessage());
														 * }
														 * 
														 */

		} catch (Exception e) {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to Continue the process");
			trace("Txn Got Rollbacked ---->" + e.getMessage());
			e.printStackTrace();
		}

		return this.personalCardissuehome();
	}

	public void addCourierMaster() throws IOException {
		IfpTransObj transact = commondesc.myTranObject("COURIERADD", txManager);
		String instid = comInstId();
		String couriername = getRequest().getParameter("COURIERNAME");
		String hoffice = getRequest().getParameter("HOFFICE");
		String contactno = getRequest().getParameter("CONTACTNO");

		try {

			/*
			 * String couriernameqry =
			 * "INSERT INTO IFP_COURIER_MASTER ( INST_ID, COURIERMASTER_ID, COURIER_NAME, COURIER_HOFFICE, COURIER_CONTACTNO, COURIER_STATUS ) VALUE "
			 * ; couriernameqry += "('" + instid + "',SEQ.NEXTVAL,'" +
			 * couriername + "','" + hoffice + "','" + contactno + "','1')";
			 * jdbctemplate.update(couriernameqry);
			 */

			// by gowthqm-260819
			String couriernameqry = "INSERT INTO IFP_COURIER_MASTER ( INST_ID, COURIERMASTER_ID, COURIER_NAME, COURIER_HOFFICE, COURIER_CONTACTNO, COURIER_STATUS ) VALUE ";
			couriernameqry += "(?,SEQ.NEXTVAL,?,?,?,?)";
			jdbctemplate.update(couriernameqry, new Object[] { instid, couriername, hoffice, contactno, "1" });

			transact.txManager.commit(transact.status);
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			getResponse().getWriter().write("Unable to add courier details");
			return;
		}
		getResponse().getWriter().write("Added Successfully");
		return;

	}

	/**
	 * 
	 * @param cardno
	 * @param padssenable
	 * @param instid
	 * @param maker_id
	 * @param padsssec
	 * @param eDMK
	 * @param eDPK
	 * @param cafflag
	 * @param jdbctemplate
	 * @return int
	 * @throws Exception
	 */

	public int cbsTableUpdateDetails(String cardno, String padssenable, String instid, String maker_id,
			PadssSecurity padsssec, String eDMK, String eDPK, String cafflag, JdbcTemplate jdbctemplate)
			throws Exception {

		trace("Enter cbs update.... ");
		String cardcon = "", process = "";
		if ("S".equalsIgnoreCase(cafflag)) {
			process = "Re - Issuance";
		} else if ("A".equalsIgnoreCase(cafflag)) {
			process = "New Card Issuance";
		} else if ("AC".equalsIgnoreCase(cafflag)) {
			process = "Add - On Card";
		} else if ("BN".equalsIgnoreCase(cafflag)) {
			process = "Renewal Card";
		} else {
			process = "process not defined";
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

		/*
		 * String query =
		 * "SELECT p.CARD_NO,p.ACCOUNT_NO,p.BRANCH_CODE,p.CIN,p.BIN,p.SUB_PROD_ID,trim(nvl(c.DOCUMENT_NUMBER,0)) as DOCUMENT_NUMBER from CUSTOMERINFO c,CARD_PRODUCTION p where c.CIN = p.CIN and p."
		 * + cardcon + "='" + cardno + "'"; enctrace("cbs-selectquery : " +
		 * query); ////// System.out.println("cbs-selectquery--->" + query);
		 * List<Map<String, Object>> list = null; try { list =
		 * jdbctemplate.queryForList(query);
		 */

		String query = "SELECT p.CARD_NO,p.ACCOUNT_NO,p.BRANCH_CODE,p.CIN,p.BIN,p.SUB_PROD_ID,trim(nvl(c.DOCUMENT_NUMBER,0)) as DOCUMENT_NUMBER from CUSTOMERINFO c,CARD_PRODUCTION p where c.CIN = p.CIN and p."
				+ cardcon + "=? ";
		enctrace("cbs-selectquery : " + query);
		////// System.out.println("cbs-selectquery--->" + query);
		List<Map<String, Object>> list = null;
		try {
			list = jdbctemplate.queryForList(query, new Object[] { cardno });

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

		subprod = subprod.substring(4, 8);
		String clearchn = padsssec.getCHN(eDMK, cardNo);

		int update = 0, res_1 = 0, cbscount = 0;
		IfpTransObj transact = commondesc.myTranObject("ISSUEAUTH", txManager);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rrs = null;

		CBSConnection cbscon = new CBSConnection();
		con = cbscon.getCBSDBConnection();
		if (!list.isEmpty() && con != null) {
			try {
				accno = accno.trim();
				if ("S".equalsIgnoreCase(cafflag)) {

					String cbsdtls = "select CARD_NO,CARD_STATUS from STTM_DEBIT_CARD_MASTER where CUST_AC_NO='" + accno
							+ "' and REQUEST_REFERENCE_NO='" + documentno.trim() + "'";
					enctrace("cbsdtls : " + cbsdtls);
					pstmt = con.prepareStatement(cbsdtls);
					rrs = pstmt.executeQuery(cbsdtls);

					////// System.out.println("cbsdtls--->"+cbsdtls);

					while (rrs.next()) {
						cbscardno = rrs.getString("CARD_NO");
						cbsstatus = rrs.getString("CARD_STATUS");
					}

				}

				String cbscountqry = "select COUNT(1) as CNT from STTM_DEBIT_CARD_MASTER where CUST_AC_NO='" + accno
						+ "' and REQUEST_REFERENCE_NO='" + documentno.trim() + "'";
				enctrace("cbscountqry : " + cbscountqry);
				pstmt = con.prepareStatement(cbscountqry);
				rrs = pstmt.executeQuery(cbscountqry);
				while (rrs.next()) {
					cbscount = rrs.getInt("CNT");
				}

				if (cbscount == 0) {
					branchcode = "";// ;accno.substring(12,14);
					refno = commondesc.fchCbsRefNoSeq(instid, jdbctemplate);
					refno = cafflag + refno;
					cbsusername = commondesc.getCbsUserName(instid, maker_id, jdbctemplate);
					cbstableupdate = "Insert into STTM_DEBIT_CARD_MASTER (BRANCH_CODE, REQUEST_REFERENCE_NO, CARD_NO, CUST_NO, CUST_AC_NO, CARD_PRODUCT, PRIMARY_CARD, CARD_STATUS, RECORD_STAT, AUTH_STAT, ONCE_AUTH, MOD_NO, MAKER_ID, MAKER_DT_STAMP, CHECKER_ID, CHECKER_DT_STAMP, CARD_BIN,DR_CR_INDICATOR) Values "
							+ " (LPAD('" + branchcode + "', 3,'0'), '" + refno + "', '" + clearchn + "', '" + cin
							+ "','" + accno + "', '" + subprod + "', 'N', 'I', 'O', 'A', 'Y', 1, '" + cbsusername
							+ "', SYSDATE,'" + cbsusername + "', SYSDATE,'" + bin + "','D')";
					enctrace("cbstableupdate : " + cbstableupdate);
					pstmt = con.prepareStatement(cbstableupdate);
					update = pstmt.executeUpdate();
				} else {
					cbstableupdate = "UPDATE STTM_DEBIT_CARD_MASTER SET CARD_NO='" + clearchn
							+ "',CARD_STATUS='I' where CUST_AC_NO='" + accno + "' and REQUEST_REFERENCE_NO='"
							+ documentno.trim() + "'";
					enctrace("cbstableupdate : " + cbstableupdate);
					pstmt = con.prepareStatement(cbstableupdate);
					update = pstmt.executeUpdate();
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

		/*
		 * String cbstabledetails =
		 * "INSERT INTO CBS_UPDATE_DETAILS(PROCESS, PROCESSED_DATE, PROCESSED_QUERY, STATUS, PROCESSED_USER,CBS_CARD_NO,CBS_CARD_STATUS) values"
		 * + "('" + process + "',(SYSDATE),'" + cbstableupdate.replaceAll("'",
		 * "") + "','" + status +
		 * "',(select username from USER_DETAILS where userid='" + maker_id +
		 * "'),'" + cbscardno + "','" + cbsstatus + "')"; enctrace(
		 * "cbstabledetails : " + cbstabledetails); //////
		 * System.out.println("cbstabledetails--->" + cbstabledetails);
		 * 
		 * try {
		 * 
		 * res_1 = jdbctemplate.update(cbstabledetails.toString());
		 * 
		 */

		/// by gowtham-260819

		String cbstabledetails = "INSERT INTO CBS_UPDATE_DETAILS(PROCESS, PROCESSED_DATE, PROCESSED_QUERY, STATUS, PROCESSED_USER,CBS_CARD_NO,CBS_CARD_STATUS) values"
				+ "(?,(SYSDATE),?,?,(select username from USER_DETAILS where userid=?),?,?)";
		enctrace("cbstabledetails : " + cbstabledetails);
		////// System.out.println("cbstabledetails--->" + cbstabledetails);

		try {

			/* res_1 = jdbctemplate.update(cbstabledetails.toString()); */

			res_1 = jdbctemplate.update(cbstabledetails.toString(), new Object[] { process,
					cbstableupdate.replaceAll("'", ""), status, maker_id, cbscardno, cbsstatus });

		} catch (Exception e) {
			txManager.rollback(transact.status);
			e.printStackTrace();
			trace("Exception in moving production cbstabledetails :: 1:::::::" + e);
			// return -1;
		}
		if (res_1 == 1) {
			txManager.commit(transact.status);
		} else {
			txManager.rollback(transact.status);
		}
		// End Of CBS update
		trace("Exit cbs update.... ");
		return update;

	}

	public String getPadssenabled() {
		return padssenabled;
	}

	public void setPadssenabled(String padssenabled) {
		this.padssenabled = padssenabled;
	}

	public String fromdate;
	public String todate;

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

}// end class
