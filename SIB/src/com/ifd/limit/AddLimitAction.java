package com.ifd.limit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifd.fee.FeeConfigBeans;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import test.Validation;

public class AddLimitAction extends BaseAction {

	private static String display;
	static Boolean initmail = true;
	static String parentid = "000";

	// private AddSubProductAction addsubProductInstance;
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		AddLimitAction.display = display;
	}

	AddLimitActionDao dao = new AddLimitActionDao();

	CommonUtil comutil = new CommonUtil();
	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
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

	private static final long serialVersionUID = 1L;

	public List getMasterfeelist() {
		return masterfeelist;
	}

	public void setMasterfeelist(List masterfeelist) {
		this.masterfeelist = masterfeelist;
	}

	private List limitmasterlis;
	private List masterfeelist;
	private String limitidid;
	private String limitype;
	private String Limitname;
	private String limitcurrency;
	private String limitcurrencycode;
	private List limitvalueDetails;

	private List glLimitType;
	private List cardtypedetails;
	private List acctypedetails;
	private String cardtypeid;
	private String cardtypename;
	private String accttypeid;
	private String accttypename;
	private String limittypedesc;
	private String cardno;
	private String custName;
	
	

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public List getGlobalcurrcy() {
		return globalcurrcy;
	}

	public void setGlobalcurrcy(List globalcurrcy) {
		this.globalcurrcy = globalcurrcy;
	}

	private String accountnumber;

	private List globalcurrcy;

	private String fromdate;
	private String todate;

	public String getCardtypeid() {
		return cardtypeid;
	}

	public void setCardtypeid(String cardtypeid) {
		this.cardtypeid = cardtypeid;
	}

	public String getCardtypename() {
		return cardtypename;
	}

	public void setCardtypename(String cardtypename) {
		this.cardtypename = cardtypename;
	}

	public String getAccttypeid() {
		return accttypeid;
	}

	public void setAccttypeid(String accttypeid) {
		this.accttypeid = accttypeid;
	}

	public String getAccttypename() {
		return accttypename;
	}

	public void setAccttypename(String accttypename) {
		this.accttypename = accttypename;
	}

	public List getLimitvalueDetails() {
		return limitvalueDetails;
	}

	public void setLimitvalueDetails(List limitvalueDetails) {
		this.limitvalueDetails = limitvalueDetails;
	}

	private String doact;

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	private String instid;

	public String getInstid() {
		return instid;
	}

	public void setInstid(String instid) {
		this.instid = instid;
	}

	AddLimitActionBean limitbean = new AddLimitActionBean();

	class Myproduct {
		private String deployid;
		private String deployname;

		public String getProductid() {
			return deployid;
		}

		public void setProductid(String productid) {
			this.deployid = deployid;
		}

		public String getProductname() {
			return deployname;
		}

		public void setProductname(String productname) {
			this.deployname = deployname;
		}

		public Myproduct(String deployid, String deployname) {
			super();
			this.deployid = deployid;
			this.deployname = deployname;
		}

	}

	private JavaMailSender mailSender;

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	List<Myproduct> productlist = new ArrayList<Myproduct>();
	List limitcurlist;

	public List getLimitcurlist() {
		return limitcurlist;
	}

	public void setLimitcurlist(List limitcurlist) {
		this.limitcurlist = limitcurlist;
	}

	public List<Myproduct> getProductlist() {
		return productlist;
	}

	public void setProductlist(List<Myproduct> productlist) {
		this.productlist = productlist;
	}

	private JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public void checkCardNoExist() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String cardno = getRequest().getParameter("cardno");
		StringBuffer hcardno = new StringBuffer();
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		String keyid = "";
		String EDMK = "", EDPK = "";
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

		int res = dao.validateActvatedCardNew(instid, hcardno, jdbctemplate);
		
		
		
		if (res == 0) {
			getResponse().getWriter().write("NEW");
		} else {
			getResponse().getWriter().write("EXIST");
		}

	}

	public void checkAccountNoExist() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String accountnumber = getRequest().getParameter("accountnumber");
		int res = dao.validateActvatedAccount(instid, accountnumber, jdbctemplate);
		if (res > 0) {
			getResponse().getWriter().write("EXIST");
		} else {
			getResponse().getWriter().write("NEW");
		}

	}

	public String updateLimit() {
		trace("**********Update txn limit info begins**********");
		enctrace("**********Update txn limit info begins**********");
		HttpSession session = getRequest().getSession();
		String act = (String) session.getAttribute("act");
		IfpTransObj transact = commondesc.myTranObject("SAVELIMIT", txManager);
		String instid = (String) session.getAttribute("Instname");
		String usercode = comUserId(session);
		String limit_desc = getRequest().getParameter("limitname");
		String limit_id = getRequest().getParameter("limitidid");

		String limitype = getRequest().getParameter("limitype");

		String limitcode = getRequest().getParameter("limitmastercode");
		System.out.println("limitype   ---- " + limitype);
		trace("limit_id===>  " + limit_desc);
		String flag = getRequest().getParameter("act");
		trace("Got Flag :: " + flag);
		String auth_code = "0";
		String mkchkrstatus = "D";
		String authmsg = "";

		String currencycode = getRequest().getParameter("limitcurrencycode");
		String limittype = getRequest().getParameter("limittype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String txnperiod = getRequest().getParameter("txnperiod");

		String inst_id = null, mkckrstatus = null;
		String userid = comUserId(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		String checkdaily = getRequest().getParameter("checkdaily") == null ? "0"
				: getRequest().getParameter("checkdaily");
		String checkweekly = getRequest().getParameter("checkweekly") == null ? "0"
				: getRequest().getParameter("checkweekly");
		String checkmonthly = getRequest().getParameter("checkmonthly") == null ? "0"
				: getRequest().getParameter("checkmonthly");
		String checkyearly = getRequest().getParameter("checkyearly") == null ? "0"
				: getRequest().getParameter("checkyearly");

		System.out.println("checkdaily::::" + checkdaily);
		System.out.println("checkweekly::::" + checkweekly);
		System.out.println("checkmonthly::::" + checkmonthly);
		System.out.println("checkyearly::::" + checkyearly);

		String limittxntype = checkyearly + "|" + checkmonthly + "|" + checkweekly + "|" + checkdaily;

		trace("limittxntype:::" + limittxntype);

		// Y M W D o o o 1 then acc flag =1
		// Y M W D 0 0 1 1 then acc flag =3
		// Y M W D 0 1 1 1 then acc flag =7
		// Y M W D 1 1 1 1 then acc flag =15
		// Y M W D 1 0 1 1 then acc flag =11
		int accflag = Integer.parseInt(checkyearly) + Integer.parseInt(checkmonthly) + Integer.parseInt(checkweekly)
				+ Integer.parseInt(checkdaily);
		trace("got acctfllag ::::" + accflag);

		String limitidbyLimittype = "";

		if (txnperiod == null) {
			txnperiod = "A";
		}
		if (limittype != null && limittype.equals("D")) {
			fromdate = "01-01-1950";
			todate = "01-01-1950";
		}
		try {

			if (flag.equals("D")) {
				auth_code = "1";
				mkchkrstatus = "D";
			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = ". Waiting for authorization";
			}
			trace("chekcing limit id already exist...");

			if (limitype.equals("CDTP")) {
				limitidbyLimittype = getRequest().getParameter("cardtypeid");
				trace("limitidbyLimittype:CDTP" + limitidbyLimittype);
				fromdate = "01-01-1950";
				todate = "01-01-1950";
			} else if (limitype.equals("ACTP")) {
				limitidbyLimittype = getRequest().getParameter("accttypeid");
				trace("limitidbyLimittype::::ACTP" + limitidbyLimittype);
				fromdate = "01-01-1950";
				todate = "01-01-1950";
			} else if (limitype.equals("CARD")) {
				limitidbyLimittype = getRequest().getParameter("cardno");
				trace("limitidbyLimittype::::CARD" + limitidbyLimittype);
				fromdate = getRequest().getParameter("fromdate");
				todate = getRequest().getParameter("todate");
			} else if (limitype.equals("ACCT")) {
				limitidbyLimittype = getRequest().getParameter("accountnumber");
				trace("limitidbyLimittype::::ACTP" + limitidbyLimittype);
				fromdate = getRequest().getParameter("fromdate");
				todate = getRequest().getParameter("todate");
			}
			trace("limitidbyLimittype :" + limitidbyLimittype);

			trace("Getting limit master description ");
			List limitdesc = commondesc.limitMasterdesc(jdbctemplate, instid);
			trace("Got : " + limitdesc);

			
			trace("fromdate:::::  "+fromdate +"   todate:: "+todate);
			
			String limitGencode = commondesc.generateLimitSequance(instid, jdbctemplate);

			//
			// int insert_lmt_cur_result =
			// dao.insert_limitcurrency_result(instid,currencycode,limitcode,currencycode,limittype,fromdate,todate,txnperiod,usercode,auth_code,jdbctemplate);
			// trace("Got insert_limitcurrency_result + " +
			// insert_lmt_cur_result );

			// int exist = dao.checkLimitExistForCurrency(instid, limitcode,
			// jdbctemplate);
			// if( exist == 0 ){
			String qryupdate = dao.update_limitTempdesc(instid, limit_id, currencycode, auth_code, usercode,
					mkchkrstatus, limittxntype);

			 int update_result = jdbctemplate.update(qryupdate); 

			// by gowtham-200819
			// int update_result = jdbctemplate.update(qryupdate);

			/*int update_result = jdbctemplate.update(qryupdate, new Object[] { inst_id, currencycode, mkckrstatus,
					auth_code, usercode, limittxntype, inst_id, limit_id });
*/
			trace("Got update_result  +  " + update_result);
			// }

			int limitdesccount = limitdesc.size();
			Iterator itr = limitdesc.iterator();

			String TXN_CODE = "";
			int updateCount = 0;
			int updSeqno = 0;
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				TXN_CODE = ((String) map.get("TXN_CODE"));
				System.out.println("TXN_CODE:" + TXN_CODE);

				String d_perday = getRequest().getParameter("d_perday" + TXN_CODE);
				String d_count = getRequest().getParameter("d_count" + TXN_CODE);

				String w_perday = getRequest().getParameter("w_perday" + TXN_CODE);
				String w_count = getRequest().getParameter("w_count" + TXN_CODE);

				String m_perday = getRequest().getParameter("m_perday" + TXN_CODE);
				String m_count = getRequest().getParameter("m_count" + TXN_CODE);

				String y_perday = getRequest().getParameter("y_perday" + TXN_CODE);
				String y_count = getRequest().getParameter("y_count" + TXN_CODE);

				int updateLimitInfo = dao.updateLimitInfo(instid, limitype, limitidbyLimittype, TXN_CODE,
						currencycode, d_perday, d_count, w_perday, w_count, m_perday, m_count, y_perday, y_count,
						String.valueOf(accflag), limit_id,fromdate,todate,  auth_code, jdbctemplate);
				
				int updateezLimitInfo = dao.updateezLimitInfo(instid, limitype, limitidbyLimittype, TXN_CODE,
						currencycode, d_perday, d_count, w_perday, w_count, m_perday, m_count, y_perday, y_count,
						String.valueOf(accflag), limit_id, fromdate, todate, auth_code, jdbctemplate);
				updateCount++;

				trace("d_perday:::" + d_perday + "---" + updateezLimitInfo  +" updateLimitInfo "+updateLimitInfo);

			}
			trace("----updateCount-----" + updateCount + "==" + update_result + "---" + update_result + updSeqno);

			if (limitdesccount == updateCount && update_result == 1) {

				// updSeqno =
				// commondesc.updateLimitSequance(instid,jdbctemplate);
				// trace("limit Seqno Updated..."+updSeqno);

				txManager.commit(transact.status);
				// trace( "Committed successfully" );
				addActionMessage("Limit \"" + limit_desc + "\" Updated Successfully " + authmsg);
				trace("Limit \"" + limit_desc + "\" Updated Successfully " + authmsg);

			}

			/*
			 * 
			 * trace("Getting the limit id...got : " + limitcode ); if(
			 * limitcode== null ){ addActionError(
			 * "Unable to processs.....could not generate the limit sequance");
			 * trace(
			 * "Unable to processs.....could not generate the limit sequance");
			 * return this.addLimit(); }
			 * 
			 */
			// int insert_lmt_cur_result =
			// dao.insert_limitcurrency_result(instid,curKey,limitcode,currencycode,limittype,fromdate,todate,txnperiod,usercode,auth_code,jdbctemplate);

			// trace("Got insert_limitcurrency_result + " +
			// insert_lmt_cur_result );
			/*
			 * 
			 * String qryschemeinsert = dao.schemeinsert_result(instid,
			 * limitcode, maxlimitamt, maxlimitcnt, txncode, txnperiod,
			 * pertxnamt,curKey); schemeinsert_result =
			 * jdbctemplate.update(qryschemeinsert); trace(
			 * "Got schemeinsert_result :" + schemeinsert_result);
			 * System.out.println( "schemeinsert_result  "+schemeinsert_result);
			 * if(schemeinsert_result==1){ chkstatus++; } }
			 */
			// int updtxnseq = commondesc.updateLimitSequance(instid,
			// jdbctemplate);
			// if( txn.length==chkstatus && updtxnseq==1 &&
			// insert_lmt_cur_result==1){
			/*
			 * txManager.commit(transact.status); trace(
			 * "Committed successfully" ); addActionError(
			 * "Limit Configured Successfully "); trace(
			 * "Limit configured Successfully " );
			 */
			/************* AUDIT BLOCK **************/
			try {
				// auditbean.setActmsg("Limit [ "+ limit_desc +" ] configured
				// for the currency [ "+currencycode+" ] ");
				// auditbean.setUsercode(username);
				// auditbean.setAuditactcode("7070");
				// commondesc.insertAuditTrail(instid, username, auditbean,
				// jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

			/*
			 * }else{ txManager.rollback(transact.status); trace(
			 * "save limit infor got Rolled back"); addActionError( Error while
			 * insert records..." ); trace("Error while insert records..."); }
			 */
		} catch (Exception e) {
			System.out.println("ROLL BACKED" + e.getMessage());
			// txManager.rollback(status);
			txManager.rollback(transact.status);
			addActionError("Exception : Could not update the limit ");
			trace("Exception : could not update the limit info : " + e.getMessage());
			e.printStackTrace();
		}
		trace("**********save txn limit info end**********\n\n");
		enctrace("**********save txn limit info end**********\n\n");
		return "required_home";
	}

	public String editLimitdetails() {
		trace("**********Editing limit details **********");
		enctrace("**********Editing limit details**********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);

		String limitid = getRequest().getParameter("limit_id");

		if (limitid.equals("-1")) {
			System.out.println("validation part limitid");
			addActionError(" Please select limit");
			return editLimitHome();
		}

		setLimitidid(limitid);
		String accttypeid = "";

		try {

			// String limitdesc = dao.limitlistbylimitid(instid,limitid,
			// jdbctemplate);
			// trace("Getting limit desc for [ "+limitid+"]...got: " +
			// limitdesc);

			String limitDesc = commondesc.getLimitDesc(instid, limitid, jdbctemplate);
			trace("Got Limit Desc" + limitDesc);
			setLimitname(limitDesc);

			String accountflag = commondesc.getLimitAccountFlag(instid, limitid, jdbctemplate);
			trace("Got getLimitAccountFlag" + accountflag);

			StringTokenizer accflag = new StringTokenizer(accountflag, "|");
			if (!accflag.nextElement().equals("0")) {
				setYearaccflag("checked");
			}
			if (!accflag.nextElement().equals("0")) {
				setMonthaccflag("checked");
			}
			if (!accflag.nextElement().equals("0")) {
				setWeekaccflag("checked");
			}
			if (!accflag.nextElement().equals("0")) {
				setDailyaccflag("checked");
			}

			System.out.println("2:" + getYearaccflag());
			System.out.println("2:" + getMonthaccflag());
			System.out.println("3:" + getWeekaccflag());
			System.out.println("4:" + getDailyaccflag());

			setLimitAccountFlag(accountflag);

			String currencycode = commondesc.getLimitCurrency(instid, limitid, jdbctemplate);
			setLimitcurrencycode(currencycode);

			String limitCurrDesc = commondesc.getCurDesc(currencycode, jdbctemplate);
			trace("Got limitCurrDesc " + limitDesc);
			setLimitcurrency(limitCurrDesc);

			List limtvalue = commondesc.getLimitValueDetailsForedit(instid, limitid, currencycode, jdbctemplate);
			trace("Got Limit value ::" + limtvalue);
			setLimitvalueDetails(limtvalue);

			String fromdate = "", todate = "";
			Iterator itr = limtvalue.iterator();
			String limittype = "";
			String cardtypeid = "";
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				limittype = (String) mp.get("LIMITTYPE");
				cardtypeid = (String) mp.get("LIMITID");
				accttypeid = (String) mp.get("LIMITID");
				fromdate = (String) mp.get("FROMDATE");
				todate = (String) mp.get("TODATE");

			}

			trace("limittype---" + limittype);
			setLimitype(limittype);
			if (limittype.equals("CDTP")) {
				setCardtypeid(cardtypeid);
				setCardtypename(dao.getCardTypeDesc(instid, cardtypeid, jdbctemplate));
				trace("cardtypeid:" + cardtypeid + "|||cardtypename:" + getCardtypename());
			} else if (limittype.equals("ACTP")) {
				setAccttypeid(accttypeid);
				setAccttypename(dao.getAcctTypeDesc(instid, accttypeid, jdbctemplate));
				trace("accttypeid:" + accttypeid + "|||accttypename:" + getAccttypename());
			}

			else if (limittype.equals("CARD")) {
		      trace("cardtypeid :: "+cardtypeid);
				String mcardno=dao.getMcardNo(instid, cardtypeid, jdbctemplate);
				StringBuffer hcardno=new StringBuffer();
				hcardno.append(cardtypeid);
			String cin=	dao.getCinNumber(instid, hcardno, jdbctemplate);
		String custName=	dao.getCustomerName(instid, cin, jdbctemplate);
		setCustName(custName);
				
				setAccttypeid(accttypeid);
				setCardno(cardtypeid);
				setCardno(mcardno);
				setFromdate(fromdate);
				setTodate(todate);
			}

			else if (limittype.equals("ACCT")) {
				setAccttypeid(accttypeid);
				setCardno(cardtypeid);
				setFromdate(fromdate);
				setTodate(todate);
			}

			setLimitname(limitid);
			trace("limitid::" + limitid + "----" + getLimitname());

			setLimittypedesc(dao.getLimitTypeDesc(instid, limittype, jdbctemplate));
			trace("limittypename:" + getLimittypedesc());

			/*
			 * session.setAttribute("limitdesc",limitdesc); String curdesc =
			 * commondesc.getCurDesc(currencycode, jdbctemplate); trace(
			 * "Getting currency description for [ "+currencycode+" ] ...got : "
			 * + curdesc ); setCurcode(currencycode); setCurrecnydesc(curdesc);
			 * 
			 * List limitcurlist = dao.getCurrencyLimitDetails(instid, limitid,
			 * curcode, jdbctemplate); trace("Currency limit details...got : " +
			 * limitcurlist ); if( limitcurlist == null ){
			 * session.setAttribute("curerr", "E");
			 * session.setAttribute("curmsg",
			 * "No records currency limit details found."); return
			 * "required_home"; } ListIterator itr1 =
			 * limitcurlist.listIterator(); while ( itr1.hasNext() ){ Map mp =
			 * (Map)itr1.next(); String addedby = (String)mp.get("ADDED_BY");
			 * addedby = commondesc.getUserName(instid, addedby, jdbctemplate);
			 * if( addedby == null ){ addedby= "--"; } mp.put("ADDED_BY",
			 * addedby);
			 * 
			 * String authby= (String)mp.get("AUTH_BY"); authby =
			 * commondesc.getUserName(instid, authby, jdbctemplate); if( authby
			 * == null ){ authby= "--"; } mp.put("AUTH_BY", authby);
			 * 
			 * String authstatus = (String)mp.get("AUTH_STATUS"); if(
			 * authstatus.equals("0")){ authstatus = "Waiting for Authorize";
			 * }else if( authstatus.equals("1")){ authstatus = "Authorized";
			 * }else if( authstatus.equals("9")){ authstatus = "De-Authorized";
			 * } mp.put("AUTH_STATUS", authstatus);
			 * 
			 * itr1.remove(); itr1.add( mp ); } setLimitcurlist(limitcurlist);
			 * trace("Getting from bean :" + getLimitcurlist() );
			 * 
			 * trace("Getting limit information...");
			 */
			/*
			 * List limit_result=dao.getLimitResult(instid, limitid,
			 * limitid+currencycode, jdbctemplate); if( !limit_result.isEmpty()
			 * ){ String limittypedesc="",txndesc=""; ListIterator itr =
			 * limit_result.listIterator(); while ( itr.hasNext() ){ Map mp =
			 * (Map)itr.next(); String limittype = (String)mp.get("LIMIT_TYPE");
			 * if( limittype.equals("CDTP")){ limittypedesc =
			 * "Product Based Limit "; }else if( limittype.equals("CRD")){
			 * limittypedesc ="Card Based Limit "; }else{ limittypedesc ="--"; }
			 * mp.put("LIMIT_TYPE", limittypedesc);
			 * 
			 * String txncode = (String)mp.get("TXNCODE"); txndesc =
			 * commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
			 * mp.put("TXNCODE", txndesc);
			 * 
			 * itr.remove(); itr.add(mp); } } setLimit_detail(limit_result);
			 */
		} catch (Exception e) {
			System.out.println("Error while etching values from Limit Description==>");
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error while fetching values from Limit Description Table" + e.getMessage());
		}

		return "editlimitdeltails";
	}

	public String addLimitView() {
		trace("**********Add limit info **********");
		enctrace("**********Add limit info**********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String flag = getRequest().getParameter("act");
		trace("got act is ..........: " + flag);
		if (flag != null) {
			session.setAttribute("act", flag);
		}

		String limittype = getRequest().getParameter("limittype");
		System.out.println("limittype::" + limittype);
		setLimitype(limittype);

		if (limittype == "" || limittype.equals("-1")) {
			addFieldError("limittype", " Please select limittype");
			return addLimit();
		}

		String limitname = getRequest().getParameter("limitname");

		boolean limitname_val = Validation.Spccharcter(limitname);
		System.out.println("limitname::" + limitname);
		if (!limitname_val) {
			addActionError(" Please enter limitname with characters");
			return addLimit();
		}

		trace("limitname::" + limitname);
		setLimitname(limitname);
		String fromdate = "";
		String todate = "";

		try {

			String limitcode = "";
			String limitCurrency = "";

			trace("Getting limittype " + limittype);

			String cardtypeid, cardtypename, limitcurrncy;
			String accttypeid, accttypename;
			String cardno = "", accountno = "",encCard="";
			PadssSecurity padsssec=new PadssSecurity();
			if (limittype.equals("ind")) {
				limitcode = getRequest().getParameter("cardnumber");
				
				String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				Properties props = getCommonDescProperty();
				String EDPK = props.getProperty("EDPK");
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String CDMK = ((String) map.get("DMK"));
					    String	CDPK = padsssec.decryptDPK(CDMK, EDPK);
					    encCard = padsssec.getECHN(CDPK, limitcode);
					}
				}	
				
				int isvalid = commondesc.checkCardNumberValid(instid, encCard, jdbctemplate);
				if (isvalid <= 0) {
					addActionError("Invalid/In-Active Card Number");
					return "required_home";
				}
				limitbean.setLimitmastercode(limitcode);
			} else if (limittype.equals("CDTP")) {
				cardtypeid = getRequest().getParameter("cardtypedetails");
				limitcurrncy = getRequest().getParameter("limitcurrncy");

				setCardtypeid(cardtypeid);
				if (cardtypeid == "" || cardtypeid.equals("-1")) {
					addActionError(" Please select cardtypeid");
					return addLimit();
				}

				if (limitcurrncy == "" || limitcurrncy.equals("-1")) {
					addActionError(" Please select limit currency");
					return addLimit();
				}

				setCardtypename(dao.getCardTypeDesc(instid, cardtypeid, jdbctemplate));
				trace("cardtypeid:" + cardtypeid + "|||cardtypename:" + getCardtypename());
				fromdate = "01-01-1950";
				setFromdate(fromdate);
				todate = "01-01-1950";
				setTodate(todate);
			} else if (limittype.equals("ACTP")) {
				accttypeid = getRequest().getParameter("acctypedetails");
				setAccttypeid(accttypeid);
				limitcurrncy = getRequest().getParameter("limitcurrncy");

				if (accttypeid == "" || accttypeid.equals("-1")) {
					addActionError(" Please select accttype id");
					return addLimit();
				}

				if (limitcurrncy == "" || limitcurrncy.equals("-1")) {
					addActionError(" Please select limit currency");
					return addLimit();
				}

				setAccttypename(dao.getAcctTypeDesc(instid, accttypeid, jdbctemplate));
				trace("accttypeid:" + accttypeid + "|||accttypename:" + getAccttypename());
				fromdate = "01-01-1950";
				setFromdate(fromdate);
				todate = "01-01-1950";
				setTodate(todate);

			} else if (limittype.equals("CARD")) {
				accttypeid = getRequest().getParameter("acctypedetails");
				setAccttypeid(accttypeid);
				setAccttypename(dao.getAcctTypeDesc(instid, accttypeid, jdbctemplate));
				trace("accttypeid:" + accttypeid + "|||accttypename:" + getAccttypename());
				cardno = getRequest().getParameter("cardnumber");
				if (cardno == "" || cardno.equals("-1")) {
					addActionError(" Please enter card number");
					return addLimit();
				}

				StringBuffer hcardno = new StringBuffer();
				String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
				String keyid = "";
				String EDMK = "", EDPK = "";
				//PadssSecurity padsssec = new PadssSecurity();
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

				int checkvalid = dao.validateActvatedCardNew(instid, hcardno, jdbctemplate);
				if (checkvalid == 0) {
					addActionError("Entered Card no Not Activated ...Enter Valid Card No.");
					return "addlimithome";
				}
				
				String cin=dao.getCinNumber(instid, hcardno, jdbctemplate);
				
				String custName=dao.getCustomerName(instid, cin, jdbctemplate);
				
				setCustName(custName);
				setCardno(cardno);
				fromdate = getRequest().getParameter("fromdate");
				setFromdate(fromdate);
				todate = getRequest().getParameter("todate");
				setTodate(todate);
				limitcurrncy = getRequest().getParameter("limitcurrncy");
				if (limitcurrncy == "" || limitcurrncy.equals("-1")) {
					addActionError(" Please select limit currency");
					return addLimit();
				}

			} else if (limittype.equals("ACCT")) {
				accttypeid = getRequest().getParameter("acctypedetails");
				setAccttypeid(accttypeid);
				setAccttypename(dao.getAcctTypeDesc(instid, accttypeid, jdbctemplate));
				trace("accttypeid:" + accttypeid + "|||accttypename:" + getAccttypename());
				accountno = getRequest().getParameter("accountnumber");
				if (accountno == "" || accountno.equals("-1")) {
					addActionError(" Please enter account number");
					return addLimit();
				}

				int checkvalid = dao.validateActvatedAccount(instid, accountno, jdbctemplate);
				if (checkvalid <= 0) {
					addActionError("Entered Account no Not Activated ...Enter Valid Account No.");
					return "addlimithome";
				}
				setAccountnumber(accountno);
				fromdate = getRequest().getParameter("fromdate");
				setFromdate(fromdate);
				todate = getRequest().getParameter("todate");
				setTodate(todate);
				limitcurrncy = getRequest().getParameter("limitcurrncy");
				if (limitcurrncy == "" || limitcurrncy.equals("-1")) {
					addActionError(" Please select limit currency");
					return addLimit();
				}

			}

			setLimittypedesc(dao.getLimitTypeDesc(instid, limittype, jdbctemplate));
			trace("limittypename:" + getLimittypedesc());

			limitCurrency = getRequest().getParameter("limitcurrncy");
			setLimitcurrencycode(limitCurrency);

			String getCurDetails = commondesc.getGlobalcurrencydesc(limitCurrency, jdbctemplate);
			if (!(getCurDetails.isEmpty())) {
				setLimitcurrency(getCurDetails);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No records");
				trace("No records");

			}
			/*
			 * else{ String limitid = getRequest().getParameter("limitcode");
			 * limitbean.setLimitmastercode(limitid); String limitname =
			 * dao.getLimitName(instid, limitid, jdbctemplate);
			 * limitbean.setLimitname(limitname); System.out.println(
			 * "limit id : " + limitbean.getLimitmastercode() ); List
			 * limitdesclist =
			 * dao.getEditLimitDescp(instid,limitid,jdbctemplate); if(
			 * !limitdesclist.isEmpty() ){ Iterator itr =
			 * limitdesclist.iterator(); while(itr.hasNext()){ Map map =
			 * (Map)itr.next(); limitcode=((String)map.get("LIMIT_DESC")); } } }
			 */
			System.out.println("limit name   " + limitcode);
			limitbean.setFeenamebean(limitcode);

			trace("Getting limit master description ");
			List limit_masterlist = commondesc.limitMasterdesc(jdbctemplate, instid);
			trace("Got limit_masterlist===> " + limit_masterlist);
			if (!(limit_masterlist.isEmpty())) {
				setLimitmasterlis(limit_masterlist);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Limit Master Table Values Not Configured ");
				trace("Limit Master Table Values Not Configured");
			}

			/*
			 * List limitmasterdetails =
			 * commondesc.getCurrencyLimitDetails(instid,limitid,curcode,
			 * jdbctemplate); trace("Got limit details from limit master tables"
			 * + limitmasterdetails) if (!(limitmasterdetails.isEmpty())){
			 * setLimistmasterdetails(limitmasterdetails); }else{
			 * session.setAttribute("curerr", "E");
			 * session.setAttribute("curmsg",
			 * "Limit Master Table Details Details Not Configured "); trace(
			 * "Limit Master Table Details Not Configured"); }
			 */
			/*
			 * List limitcurdata= dao.getCurrencyLimitDetails(instid, limitid,
			 * curcode, jdbctemplate); trace("Currency limit details...got : " +
			 * limitcurlist ); if( limitcurdata== null ){
			 * session.setAttribute("curerr", "E");
			 * session.setAttribute("curmsg",
			 * "No records currency limit details found."); return
			 * "required_home"; }
			 */
			/*
			 * ListIterator itr = limitcurdata.listIterator(); while (
			 * itr.hasNext() ){ Map mp = (Map)itr.next(); String addedby =
			 * (String)mp.get("ADDED_BY"); addedby =
			 * commondesc.getUserName(instid, addedby, jdbctemplate); if(
			 * addedby == null ){ addedby= "--"; } mp.put("ADDED_BY", addedby);
			 * 
			 * String authby= (String)mp.get("AUTH_BY"); authby =
			 * commondesc.getUserName(instid, authby, jdbctemplate); if( addedby
			 * == null ){ authby= "--"; } mp.put("AUTH_BY", authby);
			 * 
			 * String authstatus = (String)mp.get("AUTH_STATUS"); if(
			 * authstatus.equals("0")){ authstatus = "Waiting for Authorize";
			 * }else if( authstatus.equals("1")){ authstatus = "Authorized";
			 * }else if( authstatus.equals("9")){ authstatus = "De-Authorized";
			 * } mp.put("AUTH_STATUS", authstatus); itr.remove(); itr.add( mp );
			 * }
			 */

			/*** MAIL BLOCK ****/
			System.out.println("initmail--" + initmail + " parentid :  " + this.parentid);
			if (initmail) {
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId(req, jdbctemplate);
				if (!menuid.equals("NOREC")) {
					System.out.println("parentid--" + menuid);
					// session.setAttribute("URLMENUID", menuid);
					this.parentid = menuid;
				} else {
					// session.setAttribute("URLMENUID", "000");
					this.parentid = "000";
				}
				initmail = false;
			}

			/****************************/
			// List limitlist = dao.getListOfLimits(instid,currencycode);

		} catch (Exception e) {
			trace(" Error while fetching data " + e.getMessage());
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error while fetching data ");
		}
		/*** MAIL BLOCK ****/

		trace("**********Add limit info end**********\n\n");
		enctrace("**********Add limit info end**********\n\n");

		return "addlimit_view";
	}

	public String addLimit() {
		trace("************ ADD LIMIT HOME BEGINS ********");
		enctrace("************ ADD LIMIT HOME BEGINS ********");
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		System.out.println("instid---->"+instid);
		//instid="SIB";

		try {
			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " +  instid  );

			if (flag != null) {
				session.setAttribute("act", flag);
			}
			String auth_code = "1";
			String mkchkrstatus = "D", authmsg = "";
			if (flag.equals("D")) {
				auth_code = "1";
				mkchkrstatus = "D";
			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = "Waiting For Auturization...";
			}

			trace("Getting Global Limit Details");
			List globalLimitDetails = dao.getGlobalLimitDetails(jdbctemplate);
			trace("Got Global Limit Details:" + globalLimitDetails);
			setGlLimitType(globalLimitDetails);

			trace("Getting CardTYpe Limit Details");
			List cardtyperesult = dao.getCardtypeList(instid, jdbctemplate);
			trace("Got CardTYpe Details:" + cardtyperesult);
			setCardtypedetails(cardtyperesult);

			trace("Getting AccountType Limit Details");
			List accttyperesult = dao.getAcctTypeList(instid, jdbctemplate);
			trace("Got accttyperesult Details:" + accttyperesult);
			setAcctypedetails(accttyperesult);

			/*trace("Getting master Limit list");
			List masterlimitlist = dao.getMasterLimitList(instid, auth_code, mkchkrstatus, jdbctemplate);
			trace("Got master limit list count : " + masterlimitlist.size());
			if (masterlimitlist.isEmpty()) {
				// limitbean.setMasterlimitlist(masterlimitlist);
				return "required_home";
			}*/

			String getCurDetails = commondesc.getCurDetails();
			List currencyList = jdbctemplate.queryForList(getCurDetails);
			System.out.println("currencyList::" + currencyList);
			if (!(currencyList.isEmpty())) {
				setGlobalcurrcy(currencyList);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No records");
				trace("No records");
			}

		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Could not continue the process");
			trace("Exception : Could not continue the process : " + e.getMessage());
		}
		trace("************ ADD LIMIT HOME END ********\n\n");
		enctrace("************ ADD LIMIT HOME END********\n\n");
		return "addlimithome";
	}

	public String saveTxnLimit() {
		trace("**********save txn limit info begins**********");
		enctrace("**********save txn limit info begins**********");
		HttpSession session = getRequest().getSession();
		String act = (String) session.getAttribute("act");
		IfpTransObj transact = commondesc.myTranObject("SAVELIMIT", txManager);
		String instid = (String) session.getAttribute("Instname");
		String usercode = comUserId(session);
		String limit_desc = getRequest().getParameter("limitname");

		String limitype = getRequest().getParameter("limitype");

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String limitcode = getRequest().getParameter("limitmastercode");
		System.out.println("limitype   ---- " + limitype);
		trace("limit_id===>  " + limit_desc);
		String flag = getRequest().getParameter("act");
		trace("Got Flag :: " + flag);
		String auth_code = "1";
		String mkchkrstatus = "D";
		String authmsg = "";
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		String currencycode = getRequest().getParameter("limitcurrencycode");
		String limittype = getRequest().getParameter("limittype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String txnperiod = getRequest().getParameter("txnperiod");

		String userid = comUserId(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		String checkdaily = getRequest().getParameter("checkdaily") == null ? "0"
				: getRequest().getParameter("checkdaily");
		String checkweekly = getRequest().getParameter("checkweekly") == null ? "0"
				: getRequest().getParameter("checkweekly");
		String checkmonthly = getRequest().getParameter("checkmonthly") == null ? "0"
				: getRequest().getParameter("checkmonthly");
		String checkyearly = getRequest().getParameter("checkyearly") == null ? "0"
				: getRequest().getParameter("checkyearly");

		System.out.println("checkdaily::::" + checkdaily);
		System.out.println("checkweekly::::" + checkweekly);
		System.out.println("checkmonthly::::" + checkmonthly);
		System.out.println("checkyearly::::" + checkyearly);

		String limittxntype = checkyearly + "|" + checkmonthly + "|" + checkweekly + "|" + checkdaily;

		trace("limittxntype:::" + limittxntype);

		// Y M W D o o o 1 then acc flag =1
		// Y M W D 0 0 1 1 then acc flag =3
		// Y M W D 0 1 1 1 then acc flag =7
		// Y M W D 1 1 1 1 then acc flag =15
		// Y M W D 1 0 1 1 then acc flag =11
		int accflag = Integer.parseInt(checkyearly) + Integer.parseInt(checkmonthly) + Integer.parseInt(checkweekly)
				+ Integer.parseInt(checkdaily);
		trace("got acctfllag ::::" + accflag);

		String limitidbyLimittype = "";

		if (txnperiod == null) {
			txnperiod = "A";
		}
		if (limittype != null && limittype.equals("D")) {
			fromdate = "01-01-1950";
			todate = "01-01-1950";
		}
		try {

			if (flag.equals("D")) {
				auth_code = "1";
				mkchkrstatus = "D";
			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = ". Waiting for authorization";
			}
			trace("chekcing limit id already exist...");

			if (limitype.equals("CDTP")) {
				limitidbyLimittype = getRequest().getParameter("cardtypeid");
				trace("limitidbyLimittype:CDTP" + limitidbyLimittype);
				fromdate = "01-01-1950";
				todate = "01-01-1950";
			} else if (limitype.equals("ACTP")) {
				limitidbyLimittype = getRequest().getParameter("accttypeid");
				trace("limitidbyLimittype::::ACTP" + limitidbyLimittype);
				fromdate = "01-01-1950";
				todate = "01-01-1950";
			} else if (limitype.equals("CARD")) {
				limitidbyLimittype = getRequest().getParameter("cardno");
				session.setAttribute("CLEAR_CARD", limitidbyLimittype);
				trace("limitidbyLimittype::::CARD" + limitidbyLimittype);
				hcardno = padsssec.getHashedValue(limitidbyLimittype + instid);
				limitidbyLimittype=hcardno.toString();
				trace("limitidbyLimittype::::hcardno:: " + hcardno);
				fromdate = getRequest().getParameter("fromdate");
				todate = getRequest().getParameter("todate");
			} else if (limitype.equals("ACCT")) {
				limitidbyLimittype = getRequest().getParameter("accountnumber");
				trace("limitidbyLimittype::::ACTP" + limitidbyLimittype);
				fromdate = getRequest().getParameter("fromdate");
				todate = getRequest().getParameter("todate");
			}
			trace("limitidbyLimittype :" + limitidbyLimittype);

			trace("Getting limit master description ");
			List limitdesc = commondesc.limitMasterdesc(jdbctemplate, instid);
			trace("Got : " + limitdesc);

			String limitGencode = commondesc.generateLimitSequance(instid, jdbctemplate);

			//
			// int insert_lmt_cur_result =
			// dao.insert_limitcurrency_result(instid,currencycode,limitcode,currencycode,limittype,fromdate,todate,txnperiod,usercode,auth_code,jdbctemplate);
			// trace("Got insert_limitcurrency_result + " +
			// insert_lmt_cur_result );

			// int exist = dao.checkLimitExistForCurrency(instid, limitcode,
			// jdbctemplate);
			// if( exist == 0 ){
			String qryinsert = dao.insert_limitTempdesc(instid, limitGencode, "P", limit_desc, currencycode,
					String.valueOf(accflag), auth_code, usercode, mkchkrstatus, limittxntype);
			int insert_result = jdbctemplate.update(qryinsert);
			trace("Got insert_result  +  " + insert_result);
			// }

			int limitdesccount = limitdesc.size();
			Iterator itr = limitdesc.iterator();

			String TXN_CODE = "";
			int insertCount = 0;
			int updSeqno = 0;
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				TXN_CODE = ((String) map.get("TXN_CODE"));
				// System.out.println("TXN_CODE:"+TXN_CODE);

				String d_perday = getRequest().getParameter("d_perday" + TXN_CODE);
				String d_count = getRequest().getParameter("d_count" + TXN_CODE);

				String w_perday = getRequest().getParameter("w_perday" + TXN_CODE);
				String w_count = getRequest().getParameter("w_count" + TXN_CODE);

				String m_perday = getRequest().getParameter("m_perday" + TXN_CODE);
				String m_count = getRequest().getParameter("m_count" + TXN_CODE);

				String y_perday = getRequest().getParameter("y_perday" + TXN_CODE);
				String y_count = getRequest().getParameter("y_count" + TXN_CODE);

				int insertezLimitInfo = dao.insertezLimitInfo(instid, limitype, limitidbyLimittype, TXN_CODE,
						currencycode, d_perday, d_count, w_perday, w_count, m_perday, m_count, y_perday, y_count,
						String.valueOf(accflag), limitGencode, fromdate, todate, jdbctemplate);
				insertCount++;

				System.out.println("d_perday:::" + d_perday + "---" + insertezLimitInfo);

			}
			System.out.println("----------" + limitdesccount + "==" + insertCount + "---" + insert_result + updSeqno);

			if (limitdesccount == insertCount && insert_result == 1) {

				updSeqno = commondesc.updateLimitSequance(instid, jdbctemplate);
				trace("limit Seqno Updated..." + updSeqno);

				txManager.commit(transact.status);
				trace("Committed successfully");
				addActionMessage("Limit \"" + limit_desc + "\" Configured Successfully " + authmsg);
				trace("Limit \"" + limit_desc + "\" configured Successfully " + authmsg);

			}

			/*
			 * 
			 * trace("Getting the limit id...got : " + limitcode ); if(
			 * limitcode== null ){ addActionError(
			 * "Unable to processs.....could not generate the limit sequance");
			 * trace(
			 * "Unable to processs.....could not generate the limit sequance");
			 * return this.addLimit(); }
			 * 
			 */
			// int insert_lmt_cur_result =
			// dao.insert_limitcurrency_result(instid,curKey,limitcode,currencycode,limittype,fromdate,todate,txnperiod,usercode,auth_code,jdbctemplate);

			// trace("Got insert_limitcurrency_result + " +
			// insert_lmt_cur_result );
			/*
			 * 
			 * String qryschemeinsert = dao.schemeinsert_result(instid,
			 * limitcode, maxlimitamt, maxlimitcnt, txncode, txnperiod,
			 * pertxnamt,curKey); schemeinsert_result =
			 * jdbctemplate.update(qryschemeinsert); trace(
			 * "Got schemeinsert_result :" + schemeinsert_result);
			 * System.out.println( "schemeinsert_result  "+schemeinsert_result);
			 * if(schemeinsert_result==1){ chkstatus++; } }
			 */
			// int updtxnseq = commondesc.updateLimitSequance(instid,
			// jdbctemplate);
			// if( txn.length==chkstatus && updtxnseq==1 &&
			// insert_lmt_cur_result==1){
			/*
			 * txManager.commit(transact.status); trace(
			 * "Committed successfully" ); addActionError(
			 * "Limit Configured Successfully "); trace(
			 * "Limit configured Successfully " );
			 */
			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg(
						"Limit [ " + limit_desc + " ] configured for the currency [ " + currencycode + " ] ");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("7070");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

			/*
			 * }else{ txManager.rollback(transact.status); trace(
			 * "save limit infor got Rolled back"); addActionError( Error while
			 * insert records..." ); trace("Error while insert records..."); }
			 */
		} catch (Exception e) {
			System.out.println("ROLL BACKED" + e.getMessage());
			// txManager.rollback(status);
			txManager.rollback(transact.status);
			addActionError("Exception : Could not configure the limit ");
			trace("Exception : could not configure the limit info : " + e.getMessage());
			e.printStackTrace();
		}
		trace("**********save txn limit info end**********\n\n");
		enctrace("**********save txn limit info end**********\n\n");
		return "required_home";
	}

	public void getCardTypeBaseValue() throws Exception {
		trace("**********getCardTypeBaseValue begins**********");
		enctrace("**********getCardTypeBaseValue begins**********");

		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		getResponse().setContentType("text/html");
		getResponse().setHeader("Cache-Control", "no-cache");
		List result;
		String cardtype_id, cardtype_desc;
		// String qury="select * from IFP_INSTPROD_DETAILS where
		// INST_ID='"+i_Name+"' and BIN='"+bin+"' PRODUCT_ID='"+productId+"'";
		trace("Getting getCardTypeBaseValue details");
		result = dao.getCardtypeList(i_Name, jdbctemplate);
		trace("Got getCardTypeBaseValue  :" + result);

		Iterator itr = result.iterator();
		String cardtype = null;
		cardtype = "<select name=\"cardtypevalue\" id=\"cardtypevalue\" >";
		cardtype = cardtype + "<option value='-1'>SELECT</option>";
		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			cardtype_id = ((String) map.get("CARD_TYPE_ID"));
			cardtype_desc = ((String) map.get("CARD_TYPE_DESC"));
			cardtype = cardtype + "<option value=\"" + cardtype_id + "\">" + cardtype_desc + "</option>";

		}
		cardtype = cardtype + "</select>";
		try {

			getResponse().getWriter().write(cardtype);
			trace("**********getCardTypeBaseValue list end**********\n\n");
			enctrace("**********getCardTypeBaseValue list end**********\n\n");

		} catch (IOException ioe) {
			trace("Exception : " + ioe.getMessage());
			ioe.printStackTrace();
		}

		// return null;

	}

	public void subProduct() throws Exception {
		trace("**********Getting subproduct list begins**********");
		enctrace("**********Getting subproduct list begins**********");

		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		setInstid(i_Name);
		String prodid = (getRequest().getParameter("prodid"));
		System.out.println("prodid Selected is " + prodid);
		String[] product_binid = prodid.split("~");
		String bin = product_binid[0];
		String productId = product_binid[1];

		getResponse().setContentType("text/html");
		getResponse().setHeader("Cache-Control", "no-cache");
		List result;
		String product_code, subprodid;
		// String qury="select * from IFP_INSTPROD_DETAILS where
		// INST_ID='"+i_Name+"' and BIN='"+bin+"' PRODUCT_ID='"+productId+"'";
		trace("Getting subproduct details");
		result = dao.getResult(i_Name, bin, productId, jdbctemplate);
		trace("Got result  :" + result);

		Iterator itr = result.iterator();
		String subproduct = null;
		// Str sun=new ObjArray();
		subproduct = "<select name=\"subproduct\" id=\"subproduct\" onchange=\"reloadCount();\">";
		subproduct = subproduct + "<option value='00'>SELECT</option>";
		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			subprodid = ((String) map.get("PRODUCT_NAME"));
			product_code = ((String) map.get("PRODUCT_CODE"));
			subproduct = subproduct + "<option value=\"" + product_code + "\">" + subprodid + "</option>";

		}

		subproduct = subproduct + "</select>";
		try {

			getResponse().getWriter().write(subproduct);
			trace("**********Getting subproduct list end**********\n\n");
			enctrace("**********Getting subproduct list end**********\n\n");

		} catch (IOException ioe) {
			trace("Exception : " + ioe.getMessage());
			ioe.printStackTrace();
		}

		// return null;

	}

	public void reloadCount() throws Exception {
		trace("**********Getting reload count begins **********");
		enctrace("**********Getting reload count begins**********");
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		setInstid(i_Name);
		String subprod = (getRequest().getParameter("subprodid"));
		// String prodid= (getRequest().getParameter("prodid"));
		System.out.println(" Product Id Calling is  " + subprod);
		// System.out.println(" Product Id Calling is " +prodid);
		getResponse().setContentType("text/html");
		getResponse().setHeader("Cache-Control", "no-cache");

		String[] prod_binid = subprod.split("~");
		String bin = prod_binid[0];
		String productId = prod_binid[1];
		String productcode = prod_binid[2];

		List result;
		String cardtype, subprodid;
		String card_id = productId.trim() + productcode.trim();
		System.out.println("card_id is" + card_id.trim());

		result = dao.result(i_Name, bin, card_id, jdbctemplate);

		trace("Got suproduct data : " + result);

		Iterator itr = result.iterator();
		String reload = null;
		String reload1 = null;
		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			String RELOADABLE = ((String) map.get("RELOADABLE"));
			String maxamount = ((String) map.get("AMOUNT"));
			if (RELOADABLE.equals("Y")) {
				reload1 = "<table border='0' width='100%'><tr><td style='width:210px'>Maximum Reload Amount</td><td><input type='text' name='maxamnt'  id='maxamnt' value='Not Applicable' disabled='true'><input type='hidden' name='maxamnthid' id='maxamnthid' value='"
						+ maxamount + "'></td></tr>";
				reload = "<tr><td>Maximum Reload Count</td><td><input type='text' name='reloadcount' id='reloadcount' value='Not Applicable' disabled='true'></td></tr></table>";
			} else {
				reload1 = "<table border='0' width='100%'><tr><td style='width:210px'>Maximum Reload Amount</td><td><input type='text' name='maxamnt' onkeypress='return numberOnly(event)' maxlength='6' id='maxamnt' value='"
						+ maxamount + "'><input type='hidden' name='maxamnthid' id='maxamnthid' value='" + maxamount
						+ "'></td></tr>";
				reload = "<tr><td>Maximum Reload Count</td><td><input type='text' onkeypress='return numberOnly(event)' maxlength='1' name='reloadcount' id='reloadcount' value=''></td></tr></table>";
			}
		}
		try {
			getResponse().getWriter().write(reload1);
			getResponse().getWriter().write(reload);
		} catch (IOException ioe) {
			trace("Exception : " + ioe.getMessage());
			ioe.printStackTrace();
		}
		trace("**********Getting reload count end **********\n\n");
		enctrace("**********Getting reload count end**********\n\n");
	}

	public String addLimitRange() {
		trace("**********Adding limit range begins **********");
		enctrace("**********Adding limit range begins **********");

		HttpSession session = getRequest().getSession();
		String userid = (String) session.getAttribute("USERID");
		String instid = (getRequest().getParameter("instid"));
		// String deployment = (getRequest().getParameter("deployment"));
		String subproduct = (getRequest().getParameter("subproduct"));
		String[] binProduct = subproduct.split("~");
		String binno = binProduct[0];
		String produid = binProduct[1];
		String productcode = binProduct[2];
		String cardtype = produid.trim() + productcode.trim();
		String limitid = instid.trim() + binno.trim() + cardtype.trim();
		String maxamnt = (getRequest().getParameter("maxamnt"));
		String txnamt = (getRequest().getParameter("txnamt"));
		String txncount = (getRequest().getParameter("txncount"));
		String reload = (getRequest().getParameter("reloadcount"));

		try {

			String activetime = dao.activetime();
			String activedate = dao.activedate();

			int count = dao.count(instid, produid, productcode, limitid, jdbctemplate);
			trace("Got  count of limit master...count : " + count);

			if (count == 0) {
				int result = dao.result(instid, produid, productcode, limitid, cardtype, userid, activedate, activetime,
						binno, jdbctemplate);

				trace("Got result : " + result);

				int insert_result = dao.insert_result(limitid, maxamnt, txnamt, txncount, reload, activedate,
						activetime, jdbctemplate);
				trace("Inserting limit details got insert_result : " + insert_result);
				System.out.println(" INSERT INTO LIMIT_DETAILS " + insert_result);
				display = "Data Inserted Successfully";
				return "success";
			}
			if (count != 0) {
				display = "Limit Already Exist For This...Please Check";
				trace(display);
				return "success";
			}
		} catch (Exception e) {
			addActionError("Exception : could not add limit info ");
			trace("Erro while inserting limi details " + e.getMessage());
		}

		trace("**********Adding limit range end **********\n\n");
		enctrace("**********Adding limit range end**********\n\n");

		return "success";
	}

	private char resultstatus;

	public char getResultstatus() {
		return resultstatus;
	}

	public void setResultstatus(char resultstatus) {
		this.resultstatus = resultstatus;
	}

	List sproductlist;

	public List getSproductlist() {
		return sproductlist;
	}

	public void setSproductlist(List sproductlist) {
		this.sproductlist = sproductlist;
	}

	private List limitdesclist;

	public List getLimitdesclist() {
		return limitdesclist;
	}

	public void setLimitdesclist(List limitdesclist) {
		this.limitdesclist = limitdesclist;
	}

	public String viewLimit() {
		trace("**********View limit begins **********");
		enctrace("**********Adding limit begins**********");

		HttpSession session = getRequest().getSession();
		String act = (String) getRequest().getAttribute("act");
		if (act != null) {
			session.setAttribute("act", act);
		}
		try {
			String instid = (String) session.getAttribute("Instname");
			trace("Getting limit datas...");
			List limitdesc = dao.getLimitDesc(instid, "D", jdbctemplate);
			trace("Got limit datas limitdesc : " + limitdesc);
			if ((!limitdesc.isEmpty())) {
				setLimitdesclist(limitdesc);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Limit Configured ");
				trace("No Limit Configure ");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not get the limit info");
			trace("Error While Getting the Limit : " + e.getMessage());
		}
		trace("**********View limit end **********");
		enctrace("**********Adding limit end**********");
		return "viewlimit";
	}

	public String editLimitHome() {
		trace("Edit limit begins home **********");
		enctrace("Edit limit begins home**********");

		HttpSession session = getRequest().getSession();
		String act = (String) getRequest().getAttribute("act");
		if (act != null) {
			session.setAttribute("act", act);
		}
		try {
			String instid = (String) session.getAttribute("Instname");
			trace("Getting limit datas...");
			trace("Getting Global Limit Details");
			List globalLimitDetails = dao.getGlobalLimitDetails(jdbctemplate);
			trace("Got Global Limit Details:" + globalLimitDetails);
			setGlLimitType(globalLimitDetails);

			trace("Getting CardTYpe Limit Details");
			List cardtyperesult = dao.getCardtypeList(instid, jdbctemplate);
			trace("Got CardTYpe Details:" + cardtyperesult);
			setCardtypedetails(cardtyperesult);

			trace("Getting AccountType Limit Details");
			List accttyperesult = dao.getAcctTypeList(instid, jdbctemplate);
			trace("Got accttyperesult Details:" + accttyperesult);
			setAcctypedetails(accttyperesult);

			List limitdesc = dao.getLimitDesc(instid, "D", jdbctemplate);
			trace("Got limit datas limitdesc : " + limitdesc);
			if (!limitdesc.isEmpty()) {
				setLimitdesclist(limitdesc);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Limit Configured ");
				trace("No Limit Configure ");
			}
			List masterlimitlist = dao.getLimitDescForedit(instid, jdbctemplate);
			if (!masterlimitlist.isEmpty()) {
				ListIterator itr = masterlimitlist.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String limitid = (String) mp.get("LIMIT_ID");
					String limitname = dao.limitlistbylimitid(instid, limitid, jdbctemplate);
					mp.put("LIMIT_DESC", limitname);
					itr.remove();
					itr.add(mp);
				}
				setMasterfeelist(masterlimitlist);
			} else {
				addActionError("No records found");
				return "required_home";
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not get the limit info");
			trace("Error While Getting the Limit : " + e.getMessage());
		}
		trace("View limit end **********");
		enctrace("Adding limit end**********");
		return "editlimithome";
	}

	private List viewdesc;

	public List getViewdesc() {
		return viewdesc;
	}

	public void setViewdesc(List viewdesc) {
		this.viewdesc = viewdesc;
	}

	private List viewlmtdesc;

	public List getViewlmtdesc() {
		return viewlmtdesc;
	}

	public void setViewlmtdesc(List viewlmtdesc) {
		this.viewlmtdesc = viewlmtdesc;
	}

	public String viewLimitdetails() {
		trace("**********View limit details begins **********");
		enctrace("**********Adding limit details begins**********");

		HttpSession session = getRequest().getSession();
		System.out.println("CALLING VIEW LIMNIT DETAILS FUCNTION ");
		String instid = (String) session.getAttribute("Instname");
		String limitid = (getRequest().getParameter("limitid"));
		trace("Getting limit description datas");

		List viewlimitdescp = dao.getViewLimitDescp(instid, limitid, jdbctemplate);

		trace("Got : viewlimitdescp : " + viewlimitdescp);
		if (!(viewlimitdescp.isEmpty())) {
			setViewlmtdesc(viewlimitdescp);
		}

		trace("Instituon Selected is ---> " + instid + " Limit ID to Virew is ---> " + limitid);
		trace("Getting limit information records ");
		List viewlimitdesc = dao.getViewLimitDesc(instid, limitid, jdbctemplate);
		trace("Got viewlimitdesc :" + viewlimitdesc);
		if (!(viewlimitdesc.isEmpty())) {
			setViewdesc(viewlimitdesc);
		}
		trace("Getting limit master description ");
		List limitdesc = commondesc.limitMasterdesc(jdbctemplate, instid);
		trace("Got : " + limitdesc);
		if (!(limitdesc.isEmpty())) {
			setLimitmasterlis(limitdesc);

		} else {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Limit Master Table Values Not Configured ");
			trace("Limit Master Table Values Not Configured");
		}
		session.setAttribute("EVENT", "view");

		trace("**********View limit details end **********");
		enctrace("**********Adding limit details end**********");
		return "viewlimitdetails";
	}

	public String ViewEditLimit() {
		trace("**********View edit limit info begins **********");
		enctrace("**********View edit limit  info begins**********");

		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		List result;
		// limitbean limit = new limitbean();
		String limitid = (getRequest().getParameter("limitid"));
		System.out.println("query is" + limitid);

		trace("Getting institution datas...");

		result = dao.getInstitutionDatas(instid, jdbctemplate);

		trace("Got result : " + result);
		// setLimitname(result);
		if (result.isEmpty())
			setResultstatus('N');

		trace("**********View edit limit info end  **********\n\n");
		enctrace("**********View edit limit  info end**********\n\n");
		return "editlimitpage";
	}

	List limtdet;

	public List getLimtdet() {
		return limtdet;
	}

	public void setLimtdet(List limtdet) {
		this.limtdet = limtdet;
	}

	public String limitDetals() {
		trace("**********View limitDetals  begins **********");
		enctrace("**********View limitDetals begins**********");

		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		System.out.println("QUERY------------|>" + instid);
		String limitid = getRequest().getParameter("limitid");
		System.out.println("limitid==> " + limitid);
		String txn[] = getRequest().getParameterValues("txn");
		System.out.println("transaction==> " + txn);
		String inst;
		List result;

		trace("getting limit info...");
		result = dao.getLimitinfo(instid, limitid, txn, jdbctemplate);

		trace("Got limit info result : " + result);
		System.out.println("QUERY------------|>" + result);
		setLimtdet(result);

		trace("**********View limitDetals  end **********\n\n");
		enctrace("**********View limitDetals end **********\n\n");

		return "editlimitpage";
	}

	private List limit_detail;

	public List getLimit_detail() {
		return limit_detail;
	}

	public void setLimit_detail(List limit_detail) {
		this.limit_detail = limit_detail;
	}

	private String limitid;

	public String getLimitid() {
		return limitid;
	}

	public void setLimitid(String limitid) {
		this.limitid = limitid;
	}

	String curcode;
	String currecnydesc;
	private String limitAccountFlag;

	public String getDailyaccflag() {
		return dailyaccflag;
	}

	public void setDailyaccflag(String dailyaccflag) {
		this.dailyaccflag = dailyaccflag;
	}

	public String getWeekaccflag() {
		return weekaccflag;
	}

	public void setWeekaccflag(String weekaccflag) {
		this.weekaccflag = weekaccflag;
	}

	public String getMonthaccflag() {
		return monthaccflag;
	}

	public void setMonthaccflag(String monthaccflag) {
		this.monthaccflag = monthaccflag;
	}

	public String getYearaccflag() {
		return yearaccflag;
	}

	public void setYearaccflag(String yearaccflag) {
		this.yearaccflag = yearaccflag;
	}

	private String dailyaccflag;
	private String weekaccflag;
	private String monthaccflag;
	private String yearaccflag;

	public String getCurrecnydesc() {
		return currecnydesc;
	}

	public void setCurrecnydesc(String currecnydesc) {
		this.currecnydesc = currecnydesc;
	}

	public String getCurcode() {
		return curcode;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	public String ajaxlimit() throws Exception {
		trace("**********Limit authorize de authorize view **********");
		enctrace("**********Limit authorize de authorize view **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		
		//String cardno=(String) session.getAttribute("CLEAR_CARD");
		
		//trace("cardno :::  "+cardno);
		
		//PadssSecurity padsssec=new PadssSecurity();
		
		//String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		//List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
	//	Properties props = getCommonDescProperty();
	   // String	EDPK = props.getProperty("EDPK");
	   // String  mcardno = padsssec.getMakedCardno(cardno);
	/*Iterator secitr = secList.iterator();
	if (!secList.isEmpty()) {
		while (secitr.hasNext()) {
			Map map = (Map) secitr.next();
			String CDMK = ((String) map.get("DMK"));
			String CDPK = padsssec.decryptDPK(CDMK, EDPK);
			//hcardno = padsssec.getHashedValue(cardno + instid);
			cardno = padsssec.getECHN(CDPK, cardno);
		  
		}
	}
	*/
	
	
	
	
	//String CDPK = padsssec.decryptDPK(CDMK, EDPK);
		//cardno = padsssec.getECHN(CDPK, cardno);

		String limitid = getRequest().getParameter("limit_id");
		setLimitidid(limitid);
		String accttypeid = "";

		try {

			// String limitdesc = dao.limitlistbylimitid(instid,limitid,
			// jdbctemplate);
			// trace("Getting limit desc for [ "+limitid+"]...got: " +
			// limitdesc);

			String limitDesc = commondesc.getLimitDesc(instid, limitid, jdbctemplate);
			trace("Got Limit Desc" + limitDesc);
			setLimitname(limitDesc);

			String accountflag = commondesc.getLimitAccountFlag(instid, limitid, jdbctemplate);
			trace("Got getLimitAccountFlag" + accountflag);

			StringTokenizer accflag = new StringTokenizer(accountflag, "|");
			if (!accflag.nextElement().equals("0")) {
				setYearaccflag("checked");
			}
			if (!accflag.nextElement().equals("0")) {
				setMonthaccflag("checked");
			}
			if (!accflag.nextElement().equals("0")) {
				setWeekaccflag("checked");
			}
			if (!accflag.nextElement().equals("0")) {
				setDailyaccflag("checked");
			}

			System.out.println("2:" + getYearaccflag());
			System.out.println("2:" + getMonthaccflag());
			System.out.println("3:" + getWeekaccflag());
			System.out.println("4:" + getDailyaccflag());

			setLimitAccountFlag(accountflag);

			String currencycode = commondesc.getLimitCurrency(instid, limitid, jdbctemplate);
			setLimitcurrencycode(currencycode);

			String limitCurrDesc = commondesc.getCurDesc(currencycode, jdbctemplate);
			trace("Got limitCurrDesc " + limitDesc);
			setLimitcurrency(limitCurrDesc);

			List limtvalue = commondesc.getLimitValueDetails(instid, limitid, currencycode, jdbctemplate);
			trace("Got Limit value for Details ::" + limtvalue);
			setLimitvalueDetails(limtvalue);

			String fromdate = "", todate = "";
			Iterator itr = limtvalue.iterator();
			String limittype = "";
			String cardtypeid = "";
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				limittype = (String) mp.get("LIMITTYPE");
				cardtypeid = (String) mp.get("LIMITID");
				accttypeid = (String) mp.get("LIMITID");
				fromdate = (String) mp.get("FROMDATE");
				todate = (String) mp.get("TODATE");

			}

			trace("limittype---" + limittype);
			setLimitype(limittype);
			if (limittype.equals("CDTP")) {
				setCardtypeid(cardtypeid);
				setCardtypename(dao.getCardTypeDesc(instid, cardtypeid, jdbctemplate));
				trace("cardtypeid:" + cardtypeid + "|||cardtypename:" + getCardtypename());
			} else if (limittype.equals("ACTP")) {
				setAccttypeid(accttypeid);
				setAccttypename(dao.getAcctTypeDesc(instid, accttypeid, jdbctemplate));
				trace("accttypeid:" + accttypeid + "|||accttypename:" + getAccttypename());
			}

			else if (limittype.equals("CARD")) {
				

				trace("cardtypeid :: "+cardtypeid);
				
				String mcardno=dao.getMcardNo(instid, cardtypeid, jdbctemplate);
				setAccttypeid(accttypeid);
				//setCardno(cardtypeid);
			    setCardno(mcardno);
				setFromdate(fromdate);
				setTodate(todate);
			}

			else if (limittype.equals("ACCT")) {
				setAccttypeid(accttypeid);
				setCardno(cardtypeid);
				setFromdate(fromdate);
				setTodate(todate);
			}

			setLimitname(limitid);
			trace("limitid::" + limitid + "----" + getLimitname());

			setLimittypedesc(dao.getLimitTypeDesc(instid, limitid, jdbctemplate));
			trace("limittypename:" + getLimittypedesc());

			/*
			 * session.setAttribute("limitdesc",limitdesc); String curdesc =
			 * commondesc.getCurDesc(currencycode, jdbctemplate); trace(
			 * "Getting currency description for [ "+currencycode+" ] ...got : "
			 * + curdesc ); setCurcode(currencycode); setCurrecnydesc(curdesc);
			 * 
			 * List limitcurlist = dao.getCurrencyLimitDetails(instid, limitid,
			 * curcode, jdbctemplate); trace("Currency limit details...got : " +
			 * limitcurlist ); if( limitcurlist == null ){
			 * session.setAttribute("curerr", "E");
			 * session.setAttribute("curmsg",
			 * "No records currency limit details found."); return
			 * "required_home"; } ListIterator itr1 =
			 * limitcurlist.listIterator(); while ( itr1.hasNext() ){ Map mp =
			 * (Map)itr1.next(); String addedby = (String)mp.get("ADDED_BY");
			 * addedby = commondesc.getUserName(instid, addedby, jdbctemplate);
			 * if( addedby == null ){ addedby= "--"; } mp.put("ADDED_BY",
			 * addedby);
			 * 
			 * String authby= (String)mp.get("AUTH_BY"); authby =
			 * commondesc.getUserName(instid, authby, jdbctemplate); if( authby
			 * == null ){ authby= "--"; } mp.put("AUTH_BY", authby);
			 * 
			 * String authstatus = (String)mp.get("AUTH_STATUS"); if(
			 * authstatus.equals("0")){ authstatus = "Waiting for Authorize";
			 * }else if( authstatus.equals("1")){ authstatus = "Authorized";
			 * }else if( authstatus.equals("9")){ authstatus = "De-Authorized";
			 * } mp.put("AUTH_STATUS", authstatus);
			 * 
			 * itr1.remove(); itr1.add( mp ); } setLimitcurlist(limitcurlist);
			 * trace("Getting from bean :" + getLimitcurlist() );
			 * 
			 * trace("Getting limit information...");
			 */
			/*
			 * List limit_result=dao.getLimitResult(instid, limitid,
			 * limitid+currencycode, jdbctemplate); if( !limit_result.isEmpty()
			 * ){ String limittypedesc="",txndesc=""; ListIterator itr =
			 * limit_result.listIterator(); while ( itr.hasNext() ){ Map mp =
			 * (Map)itr.next(); String limittype = (String)mp.get("LIMIT_TYPE");
			 * if( limittype.equals("CDTP")){ limittypedesc =
			 * "Product Based Limit "; }else if( limittype.equals("CRD")){
			 * limittypedesc ="Card Based Limit "; }else{ limittypedesc ="--"; }
			 * mp.put("LIMIT_TYPE", limittypedesc);
			 * 
			 * String txncode = (String)mp.get("TXNCODE"); txndesc =
			 * commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
			 * mp.put("TXNCODE", txndesc);
			 * 
			 * itr.remove(); itr.add(mp); } } setLimit_detail(limit_result);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception : " + e.getMessage());
		}
		return "ajaxlimit";
	}

	List listlimitdesc;

	public List getListlimitdesc() {
		return listlimitdesc;
	}

	public void setListlimitdesc(List listlimitdesc) {
		this.listlimitdesc = listlimitdesc;
	}

	private List editlimitdesc;

	public List getEditlimitdesc() {
		return editlimitdesc;
	}

	public void setEditlimitdesc(List editlimitdesc) {
		this.editlimitdesc = editlimitdesc;
	}

	/*
	 * public String editLimitdetails() { trace(
	 * "**********Editing limit details **********"); enctrace(
	 * "**********Editing limit details**********"); HttpSession session =
	 * getRequest().getSession();
	 * 
	 * String instid = (String)session.getAttribute("Instname"); String limitid
	 * = getRequest().getParameter("limitid"); String curcode =
	 * getRequest().getParameter("curcode");
	 * 
	 * try{ setCurcode(curcode); String curdesc = commondesc.getCurDesc(curcode,
	 * jdbctemplate); trace("Getting currency description...got : " + curdesc );
	 * setCurrecnydesc(curdesc);
	 * 
	 * trace("Getting limit code "); List limit_masterlist =
	 * commondesc.limitMasterdesc(jdbctemplate,instid); trace(
	 * "Got limit desc  limit_masterlist : "+limit_masterlist);
	 * if(!(limit_masterlist.isEmpty())) { setLimitmasterlis(limit_masterlist);
	 * }else{ session.setAttribute("curerr", "E");
	 * session.setAttribute("curmsg","Limit Master Table Values Not Configured "
	 * ); trace("Limit Master Table Values Not Configured "); return
	 * "editlimitdeltails"; }
	 * 
	 * 
	 * 
	 * trace("Getting limit description for the code : " + limitid); List
	 * editlimitdescp = dao.getEditLimitDescp(instid, limitid,jdbctemplate);
	 * trace("Got editlimitdescp : " + editlimitdescp); if(!
	 * editlimitdescp.isEmpty() ) { setEditlimitdesc(editlimitdescp); }else{
	 * session.setAttribute("curerr", "E"); session.setAttribute("curmsg",
	 * " Limit Description not found"); return "editlimitdeltails"; }
	 * 
	 * List limit_desc = dao.getIpfLimitDesc(limitid,jdbctemplate);
	 * 
	 * if( ! limit_desc.isEmpty() ){ setListlimitdesc(limit_desc); }else{
	 * session.setAttribute("curerr", "E"); session.setAttribute("curmsg",
	 * "Error while fetching values from Limit Description Table"); return
	 * "editlimitdeltails"; }
	 * 
	 * } catch(Exception e) { System.out.println(
	 * "Error while etching values from Limit Description==>" );
	 * session.setAttribute("curerr", "E"); session.setAttribute("curmsg",
	 * "Error while fetching values from Limit Description Table"
	 * +e.getMessage()); }
	 * 
	 * return "editlimitdeltails"; }
	 */
	public String transdesclist() {
		trace("*******transdesclist begins*******");
		enctrace("*******transdesclist begins*******");
		HttpSession session = getRequest().getSession();

		String inst_Name = (String) session.getAttribute("Instname");
		String txn[] = getRequest().getParameterValues("txn");
		String limitdesc = getRequest().getParameter("limitdesc");
		System.out.println("To retrive the bean   " + limitdesc);
		try {
			trace("Getting limit info...");
			List transdec_result = dao.getTransdecResult(inst_Name, limitdesc, jdbctemplate);
			trace("Got limit info...transdec_result : " + transdec_result);
		} catch (Exception e) {
			trace("Exception : while fetching the values from ipflimitinfo : " + e.getMessage());
		}
		trace("*******transdesclist end*******\n\n");
		enctrace("*******transdesclist end*******\n\n");
		return "editlimitdeltails";
	}

	private String txncode;

	public String getTxncode() {
		return txncode;
	}

	public void setTxncode(String txncode) {
		this.txncode = txncode;
	}

	private List limitinfolist;

	public List getLimitinfolist() {
		return limitinfolist;
	}

	public void setLimitinfolist(List limitinfolist) {
		this.limitinfolist = limitinfolist;
	}

	public String trxnsactioncode() {
		trace("*******getting trxnsactioncode begins*******");
		enctrace("*******getting trxnsactioncode begins******");

		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		String txn[] = getRequest().getParameterValues("txn");
		String limitid = getRequest().getParameter("limitid");
		String curcode = getRequest().getParameter("curcode");
		String limittype = getRequest().getParameter("limittype");
		String period = getRequest().getParameter("period");
		if (period == null || period.equals("-1")) {
			period = "A";
		}
		System.out.println("To retrive the bean" + getTxncode());
		String txncode = getTxncode();
		System.out.println("txncode--- " + txncode);
		try {

			String keycode = limittype + limitid + curcode + period;
			trace("Getting limit info..." + keycode);
			List transdec_result = dao.getTransdecResult(instid, limitid, keycode, txncode, jdbctemplate);
			trace("Got transdec_result  : " + transdec_result);
			setLimitinfolist(transdec_result);
			trace("transdec : " + transdec_result);
		} catch (Exception e) {
			trace("Error while fetching values from IFPLIMITINFO==> " + e.getMessage());
			trace("Exception : while fetching values from IFPLIMITINFO :  " + e.getMessage());
		}
		trace("*******getting trxnsactioncode end*******\n\n");
		enctrace("*******getting trxnsactioncode end******\n\n");
		return "viewlimitdetails";
	}

	public String deleteLimitdetails() {
		trace("deleting limit details begin*******");
		enctrace("deleting limit details begin******");
		IfpTransObj trasact = commondesc.myTranObject("DELETELIMIT", txManager);
		HttpSession session = getRequest().getSession();
		String inst_Name = (String) session.getAttribute("Instname");
		String limitid = getRequest().getParameter("limitdesc");
		String instid = comUserId(session);

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		System.out.println("To retrive the bean");

		String userid = comUserId(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		try {
			trace("Deleting limit descripiont INST_ID : " + inst_Name + " limit id: " + limitid);
			int del = dao.getDeletingLimitDesc(inst_Name, limitid, jdbctemplate);
			trace("Deleting limit desc got  : " + del);
			trace("Deleting limit info  INST_ID : " + inst_Name + " limit id: " + limitid);
			int delinfo = dao.getDelListinfo(inst_Name, limitid, jdbctemplate);
			trace("Deleting limit info got  : " + delinfo);
			if (del > 0 && delinfo > 0) {
				txManager.commit(trasact.status);
				addActionMessage("Limit Deleted Successfully ");
				trace("Limit Deleted Successfully. got committed ");

				/************* AUDIT BLOCK **************/
				try {

					// added by gowtham_230719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					String limitdesc = dao.getLimitName(inst_Name, limitid, jdbctemplate);
					auditbean.setActmsg("Limit [ " + limitdesc + " ] Deleted");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("3020");
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				txManager.rollback(trasact.status);
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Error while deleting Limit");
				trace("Error while deleting Limit, got rolled back");
			}
		} catch (Exception e) {
			txManager.rollback(trasact.status);
			session.setAttribute("curmsg", "Error while deleting Limit==> " + e.getMessage());
			trace("Exception while deleting Limit : " + e.getMessage());
		}
		return viewLimit();
	}

	public String updateTxnLimit() {
		trace("*******update txn limit begins*******");
		enctrace("*******update txn limit begins*****");
		HttpSession session = getRequest().getSession();
		String act = (String) session.getAttribute("act");
		String inst_id = comInstId(session);
		String usercode = comUserId(session);
		String limitdesc = getRequest().getParameter("limitdesc");
		String curcode = getRequest().getParameter("curcode");
		String curkey = getRequest().getParameter("templlimitid");
		String txn[] = getRequest().getParameterValues("txn");
		System.out.println("Transaction lenth : " + txn.length);
		IfpTransObj transact = commondesc.myTranObject("UPDATELIMIT", txManager);
		int chkstatus = 0;

		try {
			trace("limit_id : " + limitid);
			trace("updating limit desc");
			String limitid = dao.getLimitId(inst_id, curkey, jdbctemplate);
			int update_result = dao.getUpdatingLimitDesc(limitdesc, inst_id, limitid, jdbctemplate);
			trace("updating limit desc got : " + update_result);

			String authcode = "", authmsg = "";
			if (act != null && act.equals("D")) {
				authcode = "1";
			} else {
				authcode = "0";
				authmsg = ". Waiting for authorization ";
			}
			// update_result = dao.updateLimitCurrecyStatus(authcode, usercode,
			// inst_id, limitid, curcode, "", jdbctemplate);
			update_result = dao.updateLimitCurrecyStatusWhileEdit(authcode, usercode, inst_id, limitid, curkey, "",
					jdbctemplate);
			trace("updating limit currency details....got  : " + update_result);

			for (int i = 0; i < txn.length; i++) {
				int updateresult = 0;
				String txncode = txn[i].toString();
				System.out.println("txncode==> " + txncode);
				String maxlimitamt = getRequest().getParameter("limitval_" + txn[i]);
				String maxlimitcnt = getRequest().getParameter("limitcnt_" + txn[i]);
				String txnperiod = getRequest().getParameter("txnperiod_" + txn[i]);
				String pertxnamt = getRequest().getParameter("pertxnamt_" + txn[i]);

				trace("updating limit info ");
				updateresult = dao.getUpdatingLimitInfo(maxlimitamt, maxlimitcnt, txnperiod, pertxnamt, inst_id,
						limitid, curkey, txncode, jdbctemplate);
				trace("updating limit info got : " + updateresult);
				if (updateresult == 1) {
					chkstatus++;
				}
			}
			trace("chkstatus====>" + chkstatus + "!!txn.length!!" + txn.length);
			if (update_result == 1 && txn.length == chkstatus) {
				txManager.commit(transact.status);
				trace("COMMITED");
				addActionMessage("Limit Updated Successfully" + authmsg);
				trace("Limit Updated Successfully .. got committed");
			} else {
				txManager.rollback(transact.status);
				trace("ROLL BACKED");
				addActionError(" Error while updating details ");
				trace("Error while updating details..got rolled back");
			}

		} catch (Exception e) {
			txManager.rollback(transact.status);
			trace("ROLL BACKED" + e.getMessage());
			addActionError("Unable to update the limit");
			trace("Exception : Error while updating the limit info " + e.getMessage());
			e.printStackTrace();
		}
		return "required_home";
	}

	public String Authlimit() {
		trace("Auth Limit HOME BEGINS ********");
		enctrace("Auth Limit HOME BEGINS ********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {
			String flag = getRequest().getParameter("act");
			trace("Getting limit list");
			List masterlimitlist = dao.getLimitDescForchecker(instid, jdbctemplate);
			trace("Got master limit list count : " + masterlimitlist.size());
			if (!masterlimitlist.isEmpty()) {
				ListIterator itr = masterlimitlist.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String limitid = (String) mp.get("LIMIT_ID");
					String limitname = dao.limitlistbylimitid(instid, limitid, jdbctemplate);
					mp.put("LIMIT_DESC", limitname);
					itr.remove();
					itr.add(mp);
				}
				setMasterfeelist(masterlimitlist);
			} else {
				addActionError("No records found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Could not continue the process");
			trace("Exception : Could not continue the process : " + e.getMessage());
		}
		trace("************ Auth Limit HOME BEGINS ********\n\n");
		enctrace("************ Auth Limit HOME BEGINS********\n\n");
		return "auth_limithome";
	}

	public String AuthConfiglimit() {

		trace("************ Auth Limit HOME BEGINS ********");
		enctrace("************ Auth Limit HOME BEGINS ********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {
			trace("Getting limit list");
			List masterlimitlist = dao.getLimitDesc(instid, "M", jdbctemplate);
			trace("Got master limit list count : " + masterlimitlist.size());
			if (!masterlimitlist.isEmpty()) {
				limitbean.setMasterfeelist(masterlimitlist);
			}
		} catch (Exception e) {
			addActionError("Could not continue the process");
			trace("Exception : Could not continue the process : " + e.getMessage());
		}
		trace("************ Auth Limit HOME BEGINS ********\n\n");
		enctrace("************ Auth Limit HOME BEGINS********\n\n");
		return "auth_limithome";
	}

	public String authorizeDeauthorize() {
		trace("*************product authorization *****************");
		enctrace("*************product authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHLIMIT", txManager);
		String statusmsg = "";
		String instid = comInstId(session);
		String userid = comUserId(session);
		String authstatus = "";
		String auth = getRequest().getParameter("auth");
		trace("auth value ::" + auth);
		String reason = getRequest().getParameter("reason");
		trace("deauthauth reason ::" + reason);

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String usercode = comUserId(session);
		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		String limitid = getRequest().getParameter("limitname");
		trace("limitid::" + limitid);
		int authlimit = -1;
		int deauthlimit = -1;
		int movetoAuthlimit = -1;
		int authlimitezlink = -1;
		int movetoprodu = -1, deletefromprodswitch = 0, limitinfohist = 0;
		String remarks = "";
		String mkchstatus = "";
		try {
			if (auth != null && auth.equals("Authorize")) {
				trace("LIMIT AUTHORIZING...........");
				authstatus = "1";
				statusmsg = " Authorized ";

				authlimit = dao.updateAuthLimit(authstatus, reason, userid, instid, limitid, jdbctemplate);
				// movetoAuthlimit = dao.moveLimitDescToProduction(instid,
				// limitid, jdbctemplate);

				authlimitezlink = dao.updateAuthLimitEzlink(instid, limitid, jdbctemplate);

				// int movelimit
				limitinfohist = dao.limitHistory(instid, limitid, username, jdbctemplate);
				deletefromprodswitch = dao.deleteLimitDetailsFromSwitch(instid, limitid, jdbctemplate);
				movetoprodu = dao.moveToEzlinkProduction(instid, limitid, jdbctemplate);

				trace("deletefromprodswitch--->" + deletefromprodswitch);

			} else {
				trace("LIMIT DE-AUTHORIZING...........");
				authstatus = "9";
				statusmsg = " De-Authorized ";
				authlimit = dao.updateAuthLimit(authstatus, reason, userid, instid, limitid, jdbctemplate);
				remarks = getRequest().getParameter("reason");
				addActionMessage("Limit De-Authorized Successfully");
			}

			if (auth != null && auth.equals("Authorize")) {
				trace("result:" + authlimit + authlimitezlink);
				if (authlimit == 1 && authlimitezlink > 0 && movetoprodu > 0) {
					txManager.commit(transact.status);
					addActionMessage("Limit Authorized Successfully");

				} else {
					addActionError("Could not " + statusmsg + "");
					txManager.rollback(transact.status);
				}
			} else {
				if (authlimit == 1) {
					txManager.commit(transact.status);
					// addActionMessage("Limit De-authorized Successfully");

				} else {
					addActionError("Could not " + statusmsg + "");
					txManager.rollback(transact.status);
				}
			}

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				String limitdesc = dao.getLimitName(instid, limitid, jdbctemplate);
				auditbean.setActmsg("Limit [ " + limitdesc + " ]  " + statusmsg + " Successfully");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("7070");
				auditbean.setRemarks(remarks);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError("Exception...could not continue the process");
			trace("Error while deleting the Fee " + e.getMessage());
			addActionError("Could not continue the process");
			e.printStackTrace();
		}
		return "required_home";
	}

	public void listofLimitCurrency() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String limitid = getRequest().getParameter("limitid");
		String doaction = getRequest().getParameter("doaction") == null ? "" : getRequest().getParameter("doaction");
		String option = "<option value='-1'>-SELECT-</option>";
		try {
			String condition = "";
			if (!doaction.equals("EDIT")) {
				condition = " AND AUTH_STATUS='0'";
			}

			/*
			 * String curlistqry =
			 * "SELECT DISTINCT CUR_CODE FROM LIMIT_DESC WHERE INST_ID='"
			 * +instid+"' AND LIMIT_ID='"+limitid+"'" + condition; List curlist
			 * = jdbctemplate.queryForList(curlistqry);
			 */

			// by gowtham-200819
			String curlistqry = "SELECT DISTINCT CUR_CODE FROM LIMIT_DESC WHERE INST_ID=? AND LIMIT_ID=? AND AUTH_STATUS=?";
			List curlist = jdbctemplate.queryForList(curlistqry, new Object[] { instid, limitid, "0" });

			if (!curlist.isEmpty()) {
				Iterator itr = curlist.iterator();
				String curdesc = "", curcode = "";
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					curcode = (String) mp.get("CUR_CODE");
					curdesc = commondesc.getCurDesc(curcode, jdbctemplate);
					option += "<option value='" + curcode + "'> " + curdesc + " </option>";
				}
			}
		} catch (Exception e) {
			trace("Exception....: " + e.getMessage());
			e.printStackTrace();
		}

		getResponse().getWriter().write(option);
	}

	public void listofLimitType() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String limitid = getRequest().getParameter("limitid");
		String curcode = getRequest().getParameter("curcode");

		String doaction = getRequest().getParameter("doaction") == null ? "" : getRequest().getParameter("doaction");
		String option = "<option value='-1'>-SELECT-</option>";
		try {
			String condition = "";
			if (!doaction.equals("EDIT")) {
				condition = " AND AUTH_STATUS='0'";
			}

			/*
			 * String curlistqry =
			 * "SELECT DISTINCT LIMITTYPE FROM IFP_LIMIT_CURRENCY_TEMP WHERE INST_ID='"
			 * +instid+"' AND LIMIT_ID='"+limitid+"' AND CUR_CODE='"+curcode+"'"
			 * + condition; List curlist =
			 * jdbctemplate.queryForList(curlistqry);
			 */

			// by gowtham-200819
			String curlistqry = "SELECT DISTINCT LIMITTYPE FROM IFP_LIMIT_CURRENCY_TEMP WHERE INST_ID=? AND LIMIT_ID=? AND CUR_CODE=?  AND AUTH_STATUS=? ";
			List curlist = jdbctemplate.queryForList(curlistqry, new Object[] { instid, limitid, curcode, "0" });

			if (!curlist.isEmpty()) {
				Iterator itr = curlist.iterator();
				String limittype = "";
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					limittype = (String) mp.get("LIMITTYPE");
					if (limittype.equals("D")) {
						option += "<option value='" + limittype + "'> DEFAULT </option>";
					} else {
						option += "<option value='" + limittype + "'> SPECIAL </option>";
					}
				}
			}
		} catch (Exception e) {
			trace("Exception....: " + e.getMessage());
			e.printStackTrace();
		}

		getResponse().getWriter().write(option);
	}

	public void listofLimitPeriods() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String limitid = getRequest().getParameter("limitid");
		String curcode = getRequest().getParameter("curcode");
		String limittype = getRequest().getParameter("limittype");

		String doaction = getRequest().getParameter("doaction") == null ? "" : getRequest().getParameter("doaction");
		String option = "<option value='-1'>-SELECT-</option>";
		try {
			String condition = "";
			if (!doaction.equals("EDIT")) {
				condition = " AND AUTH_STATUS='0'";
			}

			/*
			 * String curlistqry =
			 * "SELECT DISTINCT PERIOD FROM IFP_LIMIT_CURRENCY_TEMP WHERE INST_ID='"
			 * +instid+"' AND LIMIT_ID='"+limitid+"'  AND CUR_CODE='"+curcode+
			 * "' AND LIMITTYPE='"+limittype+"'" + condition; List curlist =
			 * jdbctemplate.queryForList(curlistqry);
			 */

			// by gowtham-200819
			String curlistqry = "SELECT DISTINCT PERIOD FROM IFP_LIMIT_CURRENCY_TEMP WHERE INST_ID=? AND LIMIT_ID=?  AND CUR_CODE=? AND LIMITTYPE=?  AND AUTH_STATUS=?";
			List curlist = jdbctemplate.queryForList(curlistqry,
					new Object[] { instid, limitid, curcode, limittype, "0" });
			if (!curlist.isEmpty()) {
				Iterator itr = curlist.iterator();
				String period = "";
				String descperiod = "";
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					period = (String) mp.get("PERIOD");
					if (period.equals("D")) {
						option += "<option value='" + period + "'> DAILY </option>";
					} else if (period.equals("M")) {
						option += "<option value='" + period + "'> MONTHLY </option>";
					} else if (period.equals("W")) {
						option += "<option value='" + period + "'> WEEKLY </option>";
					} else if (limittype.equals("Y")) {
						option += "<option value='" + period + "'> YEARLY </option>";
					}
				}
			}
		} catch (Exception e) {
			trace("Exception....: " + e.getMessage());
			e.printStackTrace();
		}

		getResponse().getWriter().write(option);
	}

	public String getListOfTransaction() {
		HttpSession session = getRequest().getSession();
		// System.out.println("list of limit........."+limitidbean);
		String instid = comInstId(session);
		try {
			String limitsubqry = "";
			// "SELECT TXNCODE,LIMIT_AMOUNT,LIMIT_COUNT,PERTXNAMT FROM
			// IFP_LIMITINFO_TEMP WHERE INST_ID='"+instid+"' AND
			// LIMIT_ID='"+limitidbean+"' AND CURKEY='"+keycodebean+"'";;
			enctrace("limitsubqry:" + limitsubqry);
			List txnlist = jdbctemplate.queryForList(limitsubqry);
			if (!txnlist.isEmpty()) {
				ListIterator itr = txnlist.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String txndesc = commondesc.getTransactionDesc(instid, (String) mp.get("TXNCODE"), jdbctemplate);
					mp.put("TXNCODE", txndesc);
					itr.remove();
					itr.add(mp);
				}
				System.out.println("itr :" + itr);
			}
			// txnlistbean = txnlist;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void validateAmountRange() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String txncode = getRequest().getParameter("txncode");
		// String limitamt = getRequest().get
	}

	/*
	 * public void deleteSubLimit(){ HttpSession session =
	 * getRequest().getSession(); String instid=comInstId(session); try{ String
	 * limitsubqry="DELETE FROM AND CURKEY='"+keycodebean+"'";;
	 * enctrace("limitsubqry:"+limitsubqry); List txnlist =
	 * jdbctemplate.queryForList(limitsubqry);
	 * 
	 * 
	 * 
	 * }catch(Exception e ){ e.printStackTrace(); }
	 * 
	 * }
	 */
	public String getLimitype() {
		return limitype;
	}

	public void setLimitype(String limitype) {
		this.limitype = limitype;
	}

	public String getLimitname() {
		return Limitname;
	}

	public void setLimitname(String limitname) {
		Limitname = limitname;
	}

	public String getLimitcurrency() {
		return limitcurrency;
	}

	public void setLimitcurrency(String limitcurrency) {
		this.limitcurrency = limitcurrency;
	}

	public String getLimitcurrencycode() {
		return limitcurrencycode;
	}

	public void setLimitcurrencycode(String limitcurrencycode) {
		this.limitcurrencycode = limitcurrencycode;
	}

	public List getGlLimitType() {
		return glLimitType;
	}

	public void setGlLimitType(List glLimitType) {
		this.glLimitType = glLimitType;
	}

	public List getCardtypedetails() {
		return cardtypedetails;
	}

	public void setCardtypedetails(List cardtypedetails) {
		this.cardtypedetails = cardtypedetails;
	}

	public List getAcctypedetails() {
		return acctypedetails;
	}

	public void setAcctypedetails(List acctypedetails) {
		this.acctypedetails = acctypedetails;
	}

	public String getLimittypedesc() {
		return limittypedesc;
	}

	public void setLimittypedesc(String limittypedesc) {
		this.limittypedesc = limittypedesc;
	}

	public String getLimitAccountFlag() {
		return limitAccountFlag;
	}

	public void setLimitAccountFlag(String limitAccountFlag) {
		this.limitAccountFlag = limitAccountFlag;
	}

	public String getLimitidid() {
		return limitidid;
	}

	public void setLimitidid(String limitidid) {
		this.limitidid = limitidid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
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

	public List getLimitmasterlis() {
		return limitmasterlis;
	}

	public void setLimitmasterlis(List limitmasterlis) {
		this.limitmasterlis = limitmasterlis;
	}
}// end class
