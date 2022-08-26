package com.ifp.instant;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import test.Date;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;
import com.ifp.instant.RequiredCheck;
import com.opensymphony.xwork2.Preparable;

import connection.Dbcon;

public class InstCardorderAction extends BaseAction implements Preparable {

	RequiredCheck req = null;
	private static final long serialVersionUID = 1L;

	public void prepare() {

	}

	private static Boolean initmail = true;
	private static String parentid = "000";

	CommonUtil comutil = new CommonUtil();
	CommonDesc commondesc = new CommonDesc();

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

	AuditBeans auditbean = new AuditBeans();
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

	String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	List prodlist;

	public List getProdlist() {
		return prodlist;
	}

	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}

	List branchlist;

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
	/*	String sessioncsrftoken = (String) session.getAttribute("csrfToken");
		System.out.println("tokencsrf----->    " + sessioncsrftoken);*/
		return instid;
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

	public String cominstid(HttpSession session) {
		String username = (String) session.getAttribute("SS_USERNAME");
		System.out.println("this is user name" + username);
		String sessioncsrftoken = (String) session.getAttribute("csrfToken");
		System.out.println("tokencsrf----->    " + sessioncsrftoken);
		return username;
	}

	public String instantOrder() {

		trace("******* Instant order *******");
		enctrace("******* Instant order *******");

		HttpSession session = getRequest().getSession(false);

		String token = (String) session.getAttribute("token");
		System.out.println("token----->    " + token);

		String USERTYPE = (String) session.getAttribute("USERTYPE");
		System.out.println("USERTYPE----->    " + USERTYPE);

		/*
		 * String sessioncsrftoken = (String) session.getAttribute("csrfToken");
		 * System.out.println("tokencsrf----->    "+sessioncsrftoken);
		 */

		try {

			String instid = comInstId();

			int x = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if (x < 0) {
				return "required_home";
			}

			System.out.println(" inst id is prito__" + instid);

			String act = getRequest().getParameter("act");
			System.out.println("act-->>"+act);
			if (act != null) {
				session.setAttribute("act", act);
			}

			trace("Getting product list");
			prodlist = commondesc.getProductListView(instid, jdbctemplate, session);
			System.out.println("john__" + prodlist);
			setProdlist(prodlist);
			trace("Getting branch list");
			branchlist = commondesc.generateBranchList(instid, jdbctemplate);
			setBranchlist(branchlist);
			System.out.println("Branch list  " + branchlist);
			session.setAttribute("curerr", "S");

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
			session.setAttribute("curmsg", "Error while execute the instantOrder" + e.getMessage());
			trace(" Error while execute the instantOrder : " + e.getMessage());
			e.printStackTrace();
		}
		return "instantorder_home";
	}

	List instorderlist;

	public List getInstorderlist() {
		return instorderlist;
	}

	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	}

	public String authorizeOrderHome() throws Exception {

		trace("******* Instant order authorize *******\n");
		enctrace("******* Instant order authorize *******\n");

		String instid = comInstId();

		HttpSession session = getRequest().getSession(false);
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		String orderstatus = "01";
		String mkrstatus = "M";

		try {

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

			/*
			 * int x= commondesc.reqCheck().requiredCheck(instid, session,
			 * jdbctemplate); if( x < 0 ){ return "required_home"; }
			 * 
			 * 
			 * System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"
			 * +branch); if (usertype.equals("INSTADMIN")) { System.out.println(
			 * "Branch list start"); br_list =
			 * commondesc.InstgetBranchCodefmProcess(inst_id, CARDAWAINGCODE,
			 * mkrstatus, jdbctemplate); System.out.println("Branch list "
			 * +br_list); if(!(br_list.isEmpty())){
			 * cardactbean.setBranchlist(br_list); System.out.println(
			 * "Branch list is not empty"); //setCardgenstatus('Y'); } else{
			 * addActionError("No Cards Waiting For Card Issuance ... ");
			 * System.out.println("Branch List is empty "); return
			 * "required_home";
			 * 
			 * } } pers_prodlist=commondesc.getProductListBySelected(inst_id,
			 * CARDAWAINGCODE, mkrstatus, jdbctemplate); if
			 * (!(pers_prodlist.isEmpty())){
			 * cardactbean.setProdlist(pers_prodlist);
			 * session.setAttribute("curerr", "S");
			 * session.setAttribute("curmsg",""); System.out.println(
			 * "Product List is ===> "+pers_prodlist); //setCardgenstatus('Y');
			 * } else{ System.out.println("No Product Details Found ");
			 * session.setAttribute("curerr", "E");
			 * session.setAttribute("curmsg"," No Product Details Found ");
			 * //setCardgenstatus('N'); }
			 * 
			 * 
			 * trace("Getting branch list"); branchlist =
			 * this.commondesc.generateBranchList(instid, jdbctemplate);
			 * setBranchlist(branchlist); trace("Getting product list");
			 * prodlist = commondesc.getProductListView( instid, jdbctemplate ,
			 * session); setProdlist(prodlist);
			 */
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception: Unable to continue the process");
			trace("Exception : in instant card order " + e.getMessage());
		}

		return "instauthorder_home";
	}

	public String orderView() {

		trace("********** Instant order view *********** \n");
		enctrace("********** Instant order view *********** \n");

		// System.out.println( "order view ...");
		HttpSession session = getRequest().getSession(false);
		String instid = comInstId();

		String datefld = "MAKER_DATE";
		String branch = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		String[] bin = cardtype.split("~");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		try {
			int x = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if (x < 0) {
				return "required_home";
			}

			String filtercond = commondesc.filterCondition(cardtype, branch, fromdate, todate, datefld);
			List instantorderlist = this.commondesc.getInstantOrderList(instid, filtercond, jdbctemplate);
			setInstorderlist(instantorderlist);
			if (instantorderlist.isEmpty()) {
				// session.setAttribute("preverr", "E");
				// session.setAttribute("prevmsg", " No records found " );
				addActionError("No Records Found for the selected input");
				trace("No orders found");
				return this.authorizeOrderHome();
			}

			/*
			 * String countqry =
			 * "SELECT COUNT(1) AS CNT FROM INST_CARD_ORDER WHERE INST_ID='"
			 * +instid+"'  AND MKCK_STATUS='M' "+ filtercond +""; totalcount =
			 * (String) jdbctemplate.queryForObject(countqry, String.class);
			 */

			// by gowtham-270819
			String countqry = "SELECT COUNT(1) AS CNT FROM INST_CARD_ORDER WHERE INST_ID=?  AND MKCK_STATUS=? "
					+ filtercond + "";
			totalcount = (String) jdbctemplate.queryForObject(countqry, new Object[] { instid, "M" }, String.class);

			filtercondition = filtercond;

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Exception : unable to continue the process");
			trace("Exception : while order view : " + e.getMessage());
		}
		return "instauthorder_view";
	}

	public String instantsaveOrder() {
		trace("******* Save order ******** \n");
		enctrace("******* Save order ******** \n");

		HttpSession session = getRequest().getSession(false);

		/*
		 * // by siva 210819 HttpSession ses = getRequest().getSession(); String
		 * sessioncsrftoken = (String) ses.getAttribute("csrfToken"); String
		 * jspcsrftoken = getRequest().getParameter("token");
		 * System.out.println("jspcsrftoken----->    "+jspcsrftoken); if
		 * (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
		 * ses.setAttribute("message", "CSRF Token Mismatch"); addActionError(
		 * "CSRF Token Mismatch"); return "invaliduser"; }
		 */
		// by siva 210819

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		String username = (String) session.getAttribute("SS_USERNAME");
		String instid = comInstId();
		String branchcode = getRequest().getParameter("branchcode");
		String productcode = getRequest().getParameter("cardtype");
		String subproduct = getRequest().getParameter("subproduct");

		String ordercount = getRequest().getParameter("count");
		int count = Integer.parseInt(ordercount);
		int y = 0, x = 0;
		String authmsg = "";
		String maker_id = this.comUserCode();

		// added by gowtham_260719
		String ip = (String) session.getAttribute("REMOTE_IP");

		Date date = new Date();

		try {
			for (int i = 0; i < count; i++) {
				trace("getting ordercount value " + ordercount);
				// String order_refno="";

				/*
				 * synchronized(this){
				 * 
				 * trace("sychronized block started..."); order_refno =
				 * commondesc.generateorderRefno(instid, jdbctemplate); trace(
				 * "Generated order reference number is : " + order_refno); }
				 */
				String order_refno = "";
				synchronized (this) {
					order_refno = this.commondesc.generateorderRefno(instid, jdbctemplate);
				}
				trace("getting order ref number " + order_refno);
				String bin = commondesc.getBin(instid, productcode, jdbctemplate);

				String cardtype = commondesc.getCardType(instid, productcode, jdbctemplate);
				String product = getRequest().getParameter("cardtype");
				System.out.println("product__" + product + "bin" + bin);

				String subproductstatus = commondesc.checkValidSubProduct(instid, product, subproduct, jdbctemplate);
				trace("Checking valid sub-product...got : " + subproductstatus);
				if (subproductstatus == null || !subproductstatus.equals("1")) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not continue the process...Selected product not authorized");
					return this.instantOrder();
				}

				/*
				 * int glkeysconfigured =
				 * commondesc.checkGLKeysConfigured(instid, product, subproduct,
				 * jdbctemplate); if( glkeysconfigured <= 0 ){
				 * session.setAttribute("preverr", "E");
				 * session.setAttribute("prevmsg",
				 * "Could not generate card...GL Keys not properly mapped with sub-product"
				 * ); trace(
				 * "No records found ins IFP_GL_KEYS_MAPPING for the product["
				 * +product+"] and subproduct["+subproduct+"]"); return
				 * this.instantOrder(); }
				 */

				trace("Getting embossing name for product : " + productcode + ", subproduct : " + subproduct);
				String emb_name = commondesc.getIntantEmbName(instid, product, subproduct, jdbctemplate);
				trace("Got : " + emb_name);
				if (emb_name == null) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",
							"Unable to continue the proces...No embossing name found the selected product ");
					trace("No embossing name found for product : " + productcode + ", subproduct : " + subproduct);
					return this.instantOrder();
				}

				trace("Getting encoding name for product : " + productcode + ", subproduct : " + subproduct);
				String encname = commondesc.getIntantEncName(instid, product, subproduct, jdbctemplate);
				trace("Got : " + encname);
				if (encname == null) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "No encoding name found for " + productcode);
					trace("No embossing name found for product : " + productcode + ", subproduct : " + subproduct);
					return this.instantOrder();
				}

				String checkerid = "";
				String mkckflag;
				String act = (String) session.getAttribute("act");
				System.out.println(" act value is ____" + act);

				String ckdate = "";

				if (act.equals("M")) {
					String makerid = maker_id;
					mkckflag = "M";
					ckdate = commondesc.default_date_query;
					authmsg = " Waiting for authourization..";
				} else { // D
					String makerid = maker_id;
					checkerid = maker_id;
					mkckflag = "P";
					ckdate = "sysdate";
				}

				trace("Inserting order ");

				String cardorderqry = "INSERT INTO INST_CARD_ORDER (INST_ID, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, CARD_QUANTITY, ORDER_STATUS,";
				cardorderqry += "ORDER_TYPE, ORDERED_DATE, ORDERED_TIME, EMBOSSING_NAME, MKCK_STATUS, REMARKS,ENCODE_DATA,MAKER_DATE,MAKER_TIME,MAKER_ID,CHECKER_DATE,CHECKER_TIME,";
				cardorderqry += "CHECKER_ID,BRANCH_CODE,BIN) VALUES ";

				/*
				 * cardorderqry +=
				 * "('"+instid+"','"+order_refno+"','"+cardtype+"','"+subproduct
				 * +"','"+product+"','"+ordercount+"','01',"; cardorderqry +=
				 * "'I',sysdate,'000000','"+emb_name+"','"+mkckflag+
				 * "', '','',sysdate,'000000','"
				 * +maker_id+"',"+ckdate+",'000000',"; cardorderqry +=
				 * "'"+checkerid+"','"+branchcode+"',"+bin+")"; enctrace(
				 * "ORDER INSERT QUERY " + cardorderqry);
				 * 
				 * 
				 * int hhy = this.commondesc.executeTransaction(cardorderqry,
				 * jdbctemplate);
				 */

				cardorderqry += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + ckdate + ",?,?,?," + bin + ")";
				enctrace("ORDER INSERT QUERY " + cardorderqry);

				/// modified by gowtham300819
				/*
				 * int hhy = this.commondesc.executeTransaction(cardorderqry,new
				 * Object[]{}, jdbctemplate);
				 */
				int hhy = jdbctemplate.update(cardorderqry, new Object[] {

						instid, order_refno, cardtype, subproduct, product, ordercount, "01", "I",
						date.getCurrentDate(), "000000", emb_name, mkckflag, "", "", date.getCurrentDate(), "000000",
						maker_id, "000000", checkerid, branchcode });

				trace("Got : " + y);
				if (hhy > 0) {
					y++;
					String updrefcntqry = this.commondesc.updateOrderrefnumcount(instid);
					int vcvx = this.commondesc.executeTransaction(updrefcntqry, jdbctemplate);
					session.setAttribute("preverr", "E");
					if (vcvx > 0) {
						x++;
					}

					/************* AUDIT BLOCK **************/
					try {

						// added by gowtham_265719
						trace("ip address======>  " + ip);
						auditbean.setIpAdress(ip);

						auditbean.setActmsg("Order [ " + order_refno + " ] generated successfully");
						auditbean.setUsercode(username);
						auditbean.setAuditactcode("0201");

						// added by gowtham_010819
						String pcode = null;
						auditbean.setProduct(pcode);

						auditbean.setBin(bin);
						auditbean.setActiontype("IM");
						auditbean.setSubproduct(subproduct);
						auditbean.setCardcollectbranch(branchcode);
						auditbean.setApplicationid(order_refno);
						commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
					} catch (Exception audite) {
						trace("Exception in auditran : " + audite.getMessage());
					}
					/************* AUDIT BLOCK **************/

					/*
					 * if ( x > 0 ){
					 * 
					 * //txManager.commit( status );
					 * session.setAttribute("preverr", "S");
					 * session.setAttribute("prevmsg",
					 * "  Card ordered successfully. Order Ref num is " +
					 * order_refno +". "+ authmsg ); trace(
					 * "Card ordered successfully. Order Ref num is " +
					 * order_refno +". "+ authmsg);
					 *//*** MAIL BLOCK ****/
					/*
					 * IfpTransObj transactmail = commondesc.myTranObject(); try
					 * { String alertid = this.parentid; if( alertid != null &&
					 * ! alertid.equals("000")){ String keymsg =
					 * "Instant Card order generated successfully with Order Ref-no "
					 * +order_refno; int mail = comutil.sendMail( instid,
					 * alertid, keymsg, jdbctemplate, session, getMailSender()
					 * ); System.out.println( "mail return__" + mail); } } catch
					 * (Exception e) { e.printStackTrace(); } finally{
					 * transactmail.txManager.commit(transactmail.status);
					 * System.out.println( "mail commit successfully"); }
					 *//*** MAIL BLOCK ****//*
											 * 
											 * }else{ //txManager.rollback(
											 * status );
											 * session.setAttribute("preverr",
											 * "E");
											 * session.setAttribute("prevmsg",
											 * " Could not insert the order details. Error while update the order ref num "
											 * ); trace(
											 * "Could not insert the order details   "
											 * ); }
											 */

				} else {
					// txManager.rollback( status );
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "  Could not insert the order details. ");
					trace("Could not insert the order details.");
				}
			}
			if (x == count && y == count) {

				txManager.commit(status);
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", "  Card ordered successfully." + authmsg);
				trace("Card ordered successfully." + authmsg);

			} else {
				txManager.rollback(status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "  Could not insert the order details. ");
				trace("Could not insert the order details.");
			}
		} catch (Exception e) {
			// txManager.rollback( status );
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the process ");
			trace("Exception : Could not insert the order details " + e.getMessage());
			e.printStackTrace();
		}

		return this.instantOrder();

	}

	public synchronized String saveOrder() {
		trace("******* Save order ******** \n");
		enctrace("******* Save order ******** \n");

		 HttpSession ses = getRequest().getSession();

		// by siva 210819
		String sessioncsrftoken = (String) ses.getAttribute("token");
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("sessioncsrftoken----->    " + sessioncsrftoken);
		System.out.println("jspcsrftoken----->    " + jspcsrftoken);

		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			ses.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}

		// by siva 210819

		System.out.println("success token-----> ");
	//	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		//def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		//TransactionStatus status = txManager.getTransaction(def);
		
		//System.out.println("status -----> "+status);
		String username = (String) ses.getAttribute("SS_USERNAME");
		System.out.println("username -----> "+username);
		String instid = comInstId();
		String branchcode = getRequest().getParameter("branchcode");
		String productcode = getRequest().getParameter("cardtype");
		String subproduct = getRequest().getParameter("subproduct");

		String ordercount = getRequest().getParameter("count");
		int count = Integer.parseInt(ordercount);
		int y = 0, x = 0;
		String authmsg = "";
		String maker_id = this.comUserCode();

		String checkerid = "";
		String mkckflag;
		String act = (String) ses.getAttribute("act");
		System.out.println(" act value is ____" + act);

		String ckdate = "";

		if (act.equals("M")) {
			String makerid = maker_id;
			mkckflag = "M";
			ckdate = commondesc.default_date_query;
			authmsg = " Waiting for authourization..";
		} else { // D
			String makerid = maker_id;
			checkerid = maker_id;
			mkckflag = "P";
			ckdate = "sysdate";
		}

		/*
		 * String checkcurrentuser =
		 * "SELECT COUNT(*) AS CNT FROM CURRENTUSER WHERE INSTID='"+instid+
		 * "' AND MKCK_STATUS= '"+mkckflag+
		 * "' AND CARD_STATUS='01' and USER_FLAG='S'";
		 * System.out.println("checkcurrentuser--->"+checkcurrentuser); String
		 * checkuser = (String) jdbctemplate.queryForObject(checkcurrentuser,
		 * String.class); trace("checkcurrentuser--->"+checkuser);
		 * if(!"0".equalsIgnoreCase(checkuser)){ trace("user processing");
		 * addActionError(
		 * "Some user is processing , Please try again after sometime...");
		 * return "required_home"; }else{ String currentuser =
		 * "INSERT INTO CURRENTUSER(INSTID,USERID,MKCK_STATUS,CARD_STATUS,USER_DATETIME,USER_FLAG) VALUES('"
		 * +instid+"','"+maker_id+"','"+mkckflag+"','01',SYSDATE,'S')"; int
		 * currentuserinsert = jdbctemplate.update(currentuser);
		 * trace("currentuserinsert--->"+currentuserinsert);
		 * 
		 * Connection conn = null; CallableStatement cstmt = null;
		 * 
		 * Dbcon dbcon = new Dbcon(); try { String SQL =
		 * "{call SP_CURRENTUSER_INSERT (?,?,?)}"; conn =
		 * dbcon.getDBConnection(); cstmt = conn.prepareCall (SQL);
		 * System.out.println("Called procedure --- >"+SQL); enctrace(
		 * "Called procedure in Audit insert --- >"+SQL); cstmt.setString(1,
		 * instid); cstmt.setString(2, maker_id);cstmt.setString(3, mkckflag);
		 * cstmt.execute(); } catch (SQLException e) { e.printStackTrace();
		 * //txManager.rollback(transact.status); addActionError(
		 * "Unable to Continue"); //return "required_home"; } finally {
		 * cstmt.close(); conn.close(); }
		 * 
		 * }
		 */

		try {
	    	trace("productcode" + productcode + "subproduct" + subproduct);
	    	trace("instid====  "+instid +"   ");
			trace("getting ordercount value " + ordercount);
			if(Integer.valueOf(ordercount) > 100){
				addActionError("You Can Generate Maximum Of 100 Cards Only !!!");
				return "required_home";
			}
			Connection conn = null;
			CallableStatement cstmt = null;

			Dbcon dbcon = new Dbcon();
			try {
				String SQL = "{call SP_INSTANTORDER_INSERT_1 (?,?,?,?,?,?,?,?,?)}";
				conn = dbcon.getDBConnection();
				if (conn != null) {
					System.out.println("conn---> " + conn);
					cstmt = conn.prepareCall(SQL);
					System.out.println("Called procedure --- >" + SQL);
					enctrace("Called procedure in Audit insert --- >" + SQL);
					cstmt.setString(1, instid);
					cstmt.setString(2, maker_id);
					cstmt.setString(3, mkckflag);
					cstmt.setString(4, productcode);
					cstmt.setString(5, "");
					cstmt.setString(6, subproduct);
					cstmt.setString(7, ordercount);
					cstmt.setString(8, ckdate);
					cstmt.setString(9, branchcode);
					cstmt.execute();
					//cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
					System.out.println("instid-->"+instid+"maker_id-->"+maker_id+"mkckflag-->"+mkckflag+"productcode-->"+productcode+"emptystring"
							+"subproduct-->"+subproduct+"ordercount-->"+ordercount+"ckdate-->"+ckdate+"branchcode-->"+branchcode);
				 	
					/*String result = cstmt.getString(6);
					trace("result--->" + result); */
				} else {
					System.out.println("conn---> " + conn);
				}
			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
				addActionError("Unable to Continue");
				return "required_home";
			} finally {
				// cstmt.close();
				conn.close();
			}

		} catch (Exception e) {
			// txManager.rollback( status );
			ses.setAttribute("preverr", "E");
			ses.setAttribute("prevmsg", "Exception : Could not continue the process ");

			trace("Exception : Could not insert the order details " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			//conn.close();
		}
		ses.setAttribute("preverr", "S");
		ses.setAttribute("prevmsg", "  Card ordered successfully." + authmsg);
		return this.instantOrder();

	}
	
	public String authorizeOrder() throws Exception {
		trace("******* Instant order authorize *******\n");
		enctrace("******* Instant order authorize *******\n");

		HttpSession session = getRequest().getSession();
		

		String sessioncsrftoken = (String) session.getAttribute("token");
			String jspcsrftoken = getRequest().getParameter("token");
			System.out.println("sessioncsrftoken----->    " + sessioncsrftoken);
			System.out.println("jspcsrftoken----->    " + jspcsrftoken);

			if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
				session.setAttribute("message", "CSRF Token Mismatch");
				/* addActionError("CSRF Token Mismatch"); */
				return "invaliduser";
			}
		
		
		System.out.println("success token ---->  ");
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		String username = (String) session.getAttribute("SS_USERNAME");
		trace("username -->"+username);
		String instid = comInstId();
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		String checkdrid = this.comUserCode();

		String authstatus = "", actiontype = "";
		String statusmsg = "";

		// added by gowtham_260719
		String ip = (String) session.getAttribute("REMOTE_IP");
		trace("ip -->"+ip);
		
		int size = 0;
		int[] updateCounts = new int[size];
		Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();
		PreparedStatement pstmt = null;


		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			actiontype = "IC";
			statusmsg = " Authorized ";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			authstatus = "D";
			actiontype = "ID";
			statusmsg = " De-Authorized ";
		}

		System.out.println("selected order _ ref num is " + order_refnum.length);
		int cnt = 0;
		
	 	try{
			
			if(order_refnum== null ){
				addActionError("No card number selected ....");
					return "required_home";
			}
			int crdcnt = 0;
		
			System.out.println("authstatus--->"+authstatus);
			System.out.println("checkdrid--->"+checkdrid);
			System.out.println("instid-->"+instid);
			System.out.println("order_refnum-->"+order_refnum.length);
			
			if (conn != null) {
				System.out.println(" connection opend----->\n");
				trace("connectionopend---\n");
				enctrace("connectionopend---\n");
				conn.setAutoCommit(false);
				String sql = "UPDATE INST_CARD_ORDER SET MKCK_STATUS=?, CHECKER_DATE=SYSDATE, CHECKER_ID=? WHERE INST_ID=? AND ORDER_REF_NO=? ";
				pstmt = conn.prepareStatement(sql);
				trace("sql qry for update : " + sql);
				
				for (int i = 0; i < order_refnum.length; i++) {
					pstmt.setString(1, authstatus);
					pstmt.setString(2, checkdrid);
					pstmt.setString(3, instid);
					pstmt.setString(4, order_refnum[i].toString());
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
				addActionMessage( updateCounts.length + "    Card(s) Order " + statusmsg +" Successfully...");
				
				/*session.setAttribute("prevmsg",  updateCounts.length + "    Card(s) Order " + statusmsg +" Successfully...");
				session.setAttribute("preverr", "S");*/
				
				System.out.append("Update records-----------> /n" + updateCounts.length);
				trace(updateCounts.length + " Card(s) "+statusmsg+" Successfully..got committed...");
				trace(order_refnum.length + " Card(s) "+statusmsg+" Successfully..got committed...");
				return authorizeOrderHome();}
				else{
					conn.rollback();
					pstmt.clearBatch() ;
					System.out.println("could not continue the process.../n--");
					trace("could not continue the process...---");
					addActionError("could not continue the process...!!!");
					return authorizeOrderHome();
					}
		
				
			}}catch(Exception e){
				txManager.rollback(status);
				addActionError("Exception : could not continue the issuance process...");
				trace("Exception : could not continue the issuance process..."+ e.getMessage());
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
		
		/*
		try {

			for (int i = 0; i < order_refnum.length; i++) {
				trace("Updating the status...");

				String authorderqry = "UPDATE INST_CARD_ORDER SET MKCK_STATUS=?, CHECKER_DATE=SYSDATE, CHECKER_ID=? WHERE INST_ID=? AND ORDER_REF_NO=? ";
				enctrace("authorderqry " + authorderqry);
				System.out.println("checkdrid  "+checkdrid);
				System.out.println("instid  "+instid);
				System.out.println("oder  "+order_refnum[i].toString().trim());
				int x = jdbctemplate.update(authorderqry,
						new Object[] { authstatus, checkdrid, instid, order_refnum[i].toString().trim() });

				trace("Got : " + x);
				cnt++;
  		}
				//newly added starts on 10/02/2021
				
				 if(order_refnum.length==cnt)
				{			
					System.out.println("order_count-->"+order_refnum.length);
					System.out.println("update_count-->"+cnt);
				txManager.commit(status);			
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", cnt + "  Order " + statusmsg + " successfully." );			
				trace( cnt + "  Order " + statusmsg + " successfully. Got committed ");
				}
				else
				{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Exception: Generated Card Count Not Matching " );
					txManager.rollback(status);	
				} 
				
				//newly added end on 10/02/2021
			 

		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Exception: Unable to continue the process ");
			txManager.rollback(status);
			trace("Could not insert the order details " + e.getMessage());
		}*/

		return this.authorizeOrderHome();
	}


	public String authorizeOrder1() throws Exception {
		trace("******* Instant order authorize *******\n");
		enctrace("******* Instant order authorize *******\n");

		HttpSession session = getRequest().getSession();
		

		String sessioncsrftoken = (String) session.getAttribute("token");
			String jspcsrftoken = getRequest().getParameter("token");
			System.out.println("sessioncsrftoken----->    " + sessioncsrftoken);
			System.out.println("jspcsrftoken----->    " + jspcsrftoken);

			if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
				session.setAttribute("message", "CSRF Token Mismatch");
				/* addActionError("CSRF Token Mismatch"); */
				return "invaliduser";
			}
		
		
		System.out.println("success token ---->  ");
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		String username = (String) session.getAttribute("SS_USERNAME");
		trace("username -->"+username);
		String instid = comInstId();
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		String checkdrid = this.comUserCode();

		String authstatus = "", actiontype = "";
		String statusmsg = "";

		// added by gowtham_260719
		String ip = (String) session.getAttribute("REMOTE_IP");
		trace("ip -->"+ip);

		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			actiontype = "IC";
			statusmsg = " Authorized ";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			authstatus = "D";
			actiontype = "ID";
			statusmsg = " De-Authorized ";
		}

		System.out.println("selected order _ ref num is " + order_refnum.length);
		int cnt = 0;
		try {

			for (int i = 0; i < order_refnum.length; i++) {
				trace("Updating the status...");

				/*
				 * String authorderqry =
				 * "UPDATE INST_CARD_ORDER SET MKCK_STATUS='"+authstatus+
				 * "', CHECKER_DATE=SYSDATE, CHECKER_ID='"+checkdrid+
				 * "' WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"
				 * +order_refnum[i].toString().trim()+"'"; enctrace(
				 * "authorderqry " + authorderqry ); int x =
				 * commondesc.executeTransaction(authorderqry, jdbctemplate);
				 */

				// modified by gowtham-300819
				String authorderqry = "UPDATE INST_CARD_ORDER SET MKCK_STATUS=?, CHECKER_DATE=SYSDATE, CHECKER_ID=? WHERE INST_ID=? AND ORDER_REF_NO=? ";
				enctrace("authorderqry " + authorderqry);
				int x = jdbctemplate.update(authorderqry,
						new Object[] { authstatus, checkdrid, instid, order_refnum[i].toString().trim() });

				trace("Got : " + x);
				cnt++;

				/************* AUDIT BLOCK **************/
				try {

					// added by gowtham_265719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					String subproduct = commondesc.getSubProductByOrder(instid, order_refnum[i], jdbctemplate,
							"INSTANT");
					auditbean.setActmsg("Order [ " + order_refnum[i].toString().trim() + " ] Authorized Successfully ");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0201");
					// auditbean.setBin(bin);
					auditbean.setActiontype(actiontype);
					auditbean.setSubproduct(subproduct);
					auditbean.setApplicationid(order_refnum[i].toString().trim());
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

				//newly added starts on 10/02/2021
				
				/*if(order_refnum.length==cnt)
				{			
					System.out.println("order_count-->"+order_refnum.length);
					System.out.println("update_count-->"+cnt);
				txManager.commit(status);			
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", cnt + "  Order " + statusmsg + " successfully." );			
				trace( cnt + "  Order " + statusmsg + " successfully. Got committed ");
				}
				else
				{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Exception: Generated Card Count Not Matching " );
					txManager.rollback(status);	
				}*/
				
				//newly added end on 10/02/2021
			}
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", cnt + "  Order " + statusmsg + " successfully.");
			txManager.commit(status);
			trace(cnt + "  Order " + statusmsg + " successfully. Got committed ");

		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Exception: Unable to continue the process ");
			txManager.rollback(status);
			trace("Could not insert the order details " + e.getMessage());
		}

		/*
		 * String totalcount = getRequest().getParameter("totalcount"); String
		 * filtercond = getRequest().getParameter("filtercondition"); trace(
		 * "selected order _ ref num is " +
		 * order_refnum.length+"--totalcount-->"+totalcount+"---filter cond-->"
		 * +filtercond); try{ String columns = "", condition = "", table =
		 * "INST_CARD_ORDER", result = ""; if(Integer.parseInt(totalcount) ==
		 * (order_refnum.length)){ String authorderqry =
		 * "UPDATE INST_CARD_ORDER SET MKCK_STATUS='"+authstatus+
		 * "', CHECKER_DATE=SYSDATE, CHECKER_ID='"+checkdrid+"' WHERE INST_ID='"
		 * +instid+"' "+filtercond+" "; enctrace("instant card auth all --->"
		 * +authorderqry); int x = commondesc.executeTransaction(authorderqry,
		 * jdbctemplate); trace("Got : " + x ); txManager.commit( status );
		 * }else{ Connection conn = null; Dbcon dbcon = new Dbcon(); conn =
		 * dbcon.getDBConnection();
		 * 
		 * CallableStatement cstmt = null; cstmt = conn.prepareCall(
		 * "call SP_COMMON_UPDATE(?,?,?,?,?,?)"); trace(
		 * "procedure--->call SP_COMMON_UPDATE(?,?,?,?,?)"); ArrayDescriptor
		 * arrDesc = ArrayDescriptor.createDescriptor("TVARCHAR2ARRAY", conn);
		 * System.out.println("check"); ARRAY array = new ARRAY(arrDesc, conn,
		 * order_refnum); trace("proc args-->"
		 * +array+"--"+instid+"--"+authstatus+"--"+checkdrid);
		 * cstmt.setString(1, table); cstmt.setArray(2, array);
		 * cstmt.setString(3, instid); columns =
		 * " MKCK_STATUS = 'P',CHECKER_ID='"+checkdrid+"',CHECKER_DATE=SYSDATE";
		 * condition = " WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN";
		 * cstmt.setString(4, columns); cstmt.setString(5, condition);
		 * cstmt.registerOutParameter(6,java.sql.Types.VARCHAR);
		 * cstmt.execute(); result=cstmt.getString(6);
		 * trace("result--->"+result); // conn.commit(); //}
		 * 
		 *//************* AUDIT BLOCK **************/
		/*
		 * try{ auditbean.setActmsg("Order [ "+order_refnum.length+
		 * " ] Authorized Successfully "); auditbean.setUsercode(checkdrid);
		 * auditbean.setAuditactcode("0201");
		 * commondesc.insertAuditTrail(instid, checkdrid, auditbean,
		 * jdbctemplate, txManager); }catch(Exception audite ){ trace(
		 * "Exception in auditran : "+ audite.getMessage()); }
		 *//************* AUDIT BLOCK **************//*
													 * 
													 * if(result.contains(
													 * "successfully")){
													 * //addActionMessage(
													 * "Order "+result);
													 * session.setAttribute(
													 * "preverr", "S");
													 * session.setAttribute(
													 * "prevmsg", "Order "
													 * +result );
													 * 
													 * }else{
													 * session.setAttribute(
													 * "preverr", "E");
													 * session.setAttribute(
													 * "prevmsg",
													 * "unable to continue the process"
													 * ); addActionError(
													 * "unable to continue the process"
													 * ); } } catch (Exception
													 * e) { e.printStackTrace();
													 * addActionError(
													 * "Exception: Unable to continue the process"
													 * ); txManager.rollback(
													 * status ); trace(
													 * "Could not insert the order details "
													 * + e.getMessage());
													 * 
													 * }
													 */

		return this.authorizeOrderHome();
	}

	public String viewOrder() throws Exception {
		String instid = comInstId();

		HttpSession session = getRequest().getSession();

		int x = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
		if (x < 0) {
			return "required_home";
		}

		trace("Getting branch...");
		branchlist = this.commondesc.generateBranchList(instid, jdbctemplate);
		setBranchlist(branchlist);

		trace("Getting product...");
		prodlist = commondesc.getProductListView(instid, jdbctemplate, session);
		System.out.println("john__" + prodlist);
		setProdlist(prodlist);

		return "instvieworder_home";
	}

	public String orderViewOnly() throws Exception {
		HttpSession session = getRequest().getSession();

		String instid = comInstId();

		int x = commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
		if (x < 0) {
			return "required_home";
		}

		String datefld = "MAKER_DATE";
		String branch = getRequest().getParameter("branchcode");
		String product = getRequest().getParameter("cardtype");
		String bin = commondesc.getBin(instid, product, jdbctemplate);
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String status = getRequest().getParameter("ordstatus");
		String filtercond = commondesc.filterCondition(product, branch, fromdate, todate, datefld);
		System.out.println("filter condition " + filtercond);

		if (status.equals("ALL")) {
			status = "";
		}
		System.out.println("Status value is :" + status);
		List instantorderlist = this.commondesc.getInstantOrderViewOnlyList(instid, status, filtercond, jdbctemplate);
		setInstorderlist(instantorderlist);

		if (instantorderlist.isEmpty()) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " No records found ");

			return this.viewOrder();
		}

		return "instvieworder_list";
	}

	public void getSubProductList() throws Exception {

		String instid = (String) getRequest().getParameter("instid");
		String prodid = (String) getRequest().getParameter("prodid");
		String authstatus = (String) getRequest().getParameter("AUTHSTATUS");

		/*
		 * String query=
		 * "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
		 * +instid+"' and PRODUCT_CODE='"+prodid+"' AND AUTH_STATUS=1"; trace(
		 * query + " __  get sub prod list "); List subprodlist =
		 * jdbctemplate.queryForList(query);
		 */

		// by gowtham-270819
		String query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id=? and PRODUCT_CODE=? AND AUTH_STATUS=?";
		trace(query + " __  get sub prod list ");
		List subprodlist = jdbctemplate.queryForList(query, new Object[] { instid, prodid, "1" });

		Iterator itr = subprodlist.iterator();
		String result = "<option value='-1'> - select - </option>";

		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			String maxallowedcard = (String) map.get("NO_NONPERCARD_ALLOWED");
			String subprodname = (String) map.get("SUB_PRODUCT_NAME");
			String subprodid = (String) map.get("SUB_PROD_ID");
			result += "<option value='" + subprodid + "'>" + subprodname + "</option>";
			// result = result + max;
		}
		trace("result======>" + result);
		getResponse().getWriter().write(result);
		// return(jdbcTemplate.queryForList(query));
	}

	public void getAuthSubProductList() throws Exception {
		String instid = (String) getRequest().getParameter("instid");
		String prodid = (String) getRequest().getParameter("prodid");
		String authstatus = (String) getRequest().getParameter("AUTHSTATUS");
		String userid = (String) getRequest().getParameter("userid");
		// String commondesc.getUserName(instid, userid, jdbctemplate);
		String auth;
		// if(authstatus.equals("0")){ auth = "AND AUTH_STATUS=0 AND
		// CONFIG_BY!='"+userid+"'"; }else{auth = "AND CONFIG_BY='"+userid+"'";}
		if (authstatus.equals("0")) {
			auth = " AND AUTH_STATUS=0 AND DELETED_FLAG != '2' ";
		} else {
			auth = " AND DELETED_FLAG != '2' ";
		}

		/*
		 * String query=
		 * "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
		 * +instid+"' and PRODUCT_CODE='"+prodid+"'  "+auth+""; trace( query +
		 * " __  get sub prod list "); List subprodlist =
		 * jdbctemplate.queryForList(query);
		 */

		/// by gowtham-270819
		String query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id=? and PRODUCT_CODE=?  "
				+ auth + "";
		trace(query + " __  get sub prod list ");
		List subprodlist = jdbctemplate.queryForList(query, new Object[] { instid, prodid });

		Iterator itr = subprodlist.iterator();
		String result = "<option value='-1'> - select - </option>";

		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			String maxallowedcard = (String) map.get("NO_NONPERCARD_ALLOWED");
			String subprodname = (String) map.get("SUB_PRODUCT_NAME");
			String subprodid = (String) map.get("SUB_PROD_ID");
			result += "<option value='" + subprodid + "'>" + subprodname + "</option>";
			// result = result + max;
		}
		trace("result======>" + result);
		getResponse().getWriter().write(result);
		// return(jdbcTemplate.queryForList(query));
	}

	public void maxAllowedCard() throws Exception {
		String instid = (String) getRequest().getParameter("instid");
		String bin = (String) getRequest().getParameter("bin");
		String subprodcode = (String) getRequest().getParameter("subprodid");
		String max = null;

		/*
		 * String query=
		 * "select NO_NONPERCARD_ALLOWED from INSTPROD_DETAILS where inst_id='"
		 * +instid+"' and BIN='"+bin+"' AND SUB_PROD_ID='"+subprodcode+"'"; //
		 * AND PERSONALIZED in ('N','B')
		 * max=(String)jdbctemplate.queryForObject(query,String.class);
		 */

		// by gowtham-27019
		String query = "select NO_NONPERCARD_ALLOWED from INSTPROD_DETAILS where inst_id=? and BIN=? AND SUB_PROD_ID=?"; // AND
																															// PERSONALIZED
																															// in
																															// ('N','B')
		max = (String) jdbctemplate.queryForObject(query, new Object[] { instid, bin, subprodcode }, String.class);

		getResponse().getWriter().write(max);
	}

	public String totalcount;
	public String filtercondition;

	public String getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(String totalcount) {
		this.totalcount = totalcount;
	}

	public String getFiltercondition() {
		return filtercondition;
	}

	public void setFiltercondition(String filtercondition) {
		this.filtercondition = filtercondition;
	}
}