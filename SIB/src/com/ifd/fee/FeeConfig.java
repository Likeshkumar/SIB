package com.ifd.fee;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;

import com.ifd.fee.FeeConfigBeans;
import com.ifd.fee.FeeConfigDAO;

import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import test.Date;
import test.Validation;

public class FeeConfig extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	FeeConfigDAO feedao = new FeeConfigDAO();
	FeeConfigBeans feebean = new FeeConfigBeans();
	static Boolean initmail = true;
	static String menuid = "000";
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public FeeConfigDAO getFeedao() {
		return feedao;
	}

	public void setFeedao(FeeConfigDAO feedao) {
		this.feedao = feedao;
	}

	public FeeConfigBeans getFeebean() {
		return feebean;
	}

	public void setFeebean(FeeConfigBeans feebean) {
		this.feebean = feebean;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	private List Globcurrcy;

	public List getGlobcurrcy() {
		return Globcurrcy;
	}

	public void setGlobcurrcy(List globcurrcy) {
		Globcurrcy = globcurrcy;
	}

	public String addFeeHome() {
		trace("************ ADD FEE HOME BEGINS ********");
		enctrace("************ ADD FEE HOME BEGINS ********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {

			String getCurDetails = commondesc.getCurDetails();
			List currencyList = jdbctemplate.queryForList(getCurDetails);
			trace("currencyList::" + currencyList);
			if (!(currencyList.isEmpty())) {
				setGlobcurrcy(currencyList);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No records Found for Currency");
				trace("No records");
			}

		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Could not continue the process");
			trace("Exception : Could not continue the process : " + e.getMessage());

		}
		trace("************ ADD FEE HOME END ********\n\n");
		enctrace("************ ADD FEE HOME END********\n\n");
		return "addfee_home";
	}

	public void checkFeenameExist() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String feename = getRequest().getParameter("feename");
		String result = "";
		int res = commondesc.checkFeenameexist(instid, feename, jdbctemplate);
		if (res > 0) {
			getResponse().getWriter().write("EXIST");
		} else {
			getResponse().getWriter().write("NEW");
		}
	}

	public String addFeeView() throws Exception {
		trace("************ AaddFeeView BEGINS ********");
		enctrace("************ addFeeView BEGINS ********");
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);

		String feename = getRequest().getParameter("feename");
		System.out.println("feename::" + feename);
		feebean.setFeename(feename);
		System.out.println(" after feename::" + feename);

		boolean feeval = Validation.Spccharcter(feename);
		if (!feeval) {
			System.out.println("validation part Feename");
			addFieldError("feename", " Please enter Fee Name with characters");
			return addFeeHome();
		}

		String numericode = getRequest().getParameter("limitcurrncy");
		System.out.println("limitcurrncy" + numericode);

		if (numericode.equals("-1")) {
			System.out.println("validation part branchcode");
			addFieldError("limitcurrncy", " Please select currency");
			return addFeeHome();
		}

		// String currencyco=getRequest().getParameter("currencyco");
		// System.out.println("currencyco"+currencyco);

		List feeLisConfig = feedao.getFeeMaintainConfig(instid, jdbctemplate);
		feebean.setFeeLisConfig(feeLisConfig);

		List instCurrList = feedao.getInstCurrency(numericode, jdbctemplate);
		Iterator secitr = instCurrList.iterator();
		if (!instCurrList.isEmpty()) {
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				feebean.setNumericCode((String) map.get("NUMERIC_CODE"));
				feebean.setCurrecyCode((String) map.get("CURRENCY_CODE"));
			}
		}

		trace("************ ADD FEE HOME END ********");
		enctrace("************ ADD FEE HOME END********");

		return "addfee_view";
	}

	public String addFeeAction() {

		trace("************ Saving fee details begins ********");
		enctrace("************ Saving fee details begins ********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String userid = comUserId(session);
		String remarks = getRequest().getParameter("reason");
		String curcode = getRequest().getParameter("currencycode");
		String masterfeename = getRequest().getParameter("masterfeename");
		String usercode = comUserId(session);

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		IfpTransObj transact = commondesc.myTranObject("ADDFEE", txManager);
		try {
			String auth_code = "1";
			String mkchkrstatus = "D";
			String authmsg = "";
			String flag = getRequest().getParameter("act");
			trace("Got Flag :: " + flag);
			if (flag.equals("D")) {
				auth_code = "1";
				mkchkrstatus = "D";
			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = ". Waiting for authorization";
			}

			String TXN_CODE = "";
			int insertCount = 0, insertFeelist = 0;
			int updSeqno = 0;
			List limitdesc = commondesc.FeeMasterdesc(jdbctemplate, instid);
			trace("Got : " + limitdesc);
			String feeGencode = commondesc.generateFeeSequance(instid, jdbctemplate);

			int feedesccount = limitdesc.size();
			Iterator itr = limitdesc.iterator();
			String feeAmt = "";
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				TXN_CODE = ((String) map.get("TXN_CODE"));
				feeAmt = getRequest().getParameter("feeamount_" + TXN_CODE);
				System.out.println("TXN_CODE:" + TXN_CODE + ":" + feeAmt + "curcode:" + curcode);

				insertFeelist = feedao.insertFeeList(feeGencode, instid, curcode, TXN_CODE, feeAmt, "0", jdbctemplate);
				insertCount = insertCount + insertFeelist;
			}

			int insertfeeDesc = feedao.insertFeeDesc(feeGencode, instid, "1", usercode, masterfeename, auth_code,
					jdbctemplate);

			System.out.println("insertFeelist::" + insertFeelist + "limitdesccount:" + feedesccount + insertfeeDesc);
			if (feedesccount == insertCount && insertfeeDesc == 1) {

				updSeqno = commondesc.UpdateFeeSequance(instid, jdbctemplate);
				trace("feee Seqno Updated..." + updSeqno);

				txManager.commit(transact.status);
				trace("Committed successfully");
				addActionMessage("FEE \"" + masterfeename + "\" Configured Successfully " + authmsg);
				trace("Limit \"" + masterfeename + "\" configured Successfully " + authmsg);

			} else {
				txManager.rollback(transact.status);
				trace("Committed successfully");
				addActionMessage("FEE \"" + masterfeename + "\" Configured Successfully " + authmsg);
				trace("FEE \"" + masterfeename + "\" configured Successfully " + authmsg);
			}

			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg("Fee [ " + masterfeename + " ]  " + authmsg + "");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("8080");
				auditbean.setRemarks(remarks);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}

		} catch (Exception e) {
			System.out.println("ROLL BACKED" + e.getMessage());
			// txManager.rollback(status);
			txManager.rollback(transact.status);
			addActionError("Exception : Could not configure the Fee ");
			trace("Exception : could not configure the limit info : " + e.getMessage());
			e.printStackTrace();
		}
		trace("************ Saving fee details begins ********\n\n");
		enctrace("************ Saving fee details begins ********\n\n");
		return "required_home";
	}

	public String editSubFeeHome() {
		trace("************ Edit sub fee begins********");
		enctrace("************ Edit sub fee begins********");
		HttpSession session = getRequest().getSession();

		return "deletefee_home";

	}

	public String editFeeAction() {
		trace("************ Edit sub fee action begins********");
		enctrace("************ Edit sub action begins********");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("ADDFEE", txManager);

		trace("************ Edit sub fee action end********");
		enctrace("************ Edit sub fee action end********");
		return editSubFeeHome();
	}

	public String deleteFeeInfo() {
		trace("************ Deleteing fee info********");
		enctrace("************ Deleteing fee info ********");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("DELETEFEE", txManager);
		String masterfeecode = getRequest().getParameter("masterfeecode");
		String instid = comInstId(session);
		String subfeecode = getRequest().getParameter("subfeecode");
		String recordid = getRequest().getParameter("recordid");
		// String recordcancat="AND RECORD_ID='"+recordid+"'";
		String act = getRequest().getParameter("act");
		System.out.println("act ---- " + act);
		int temp_deletefeerevenue, temp_deletesubfee = 1;
		try {
			String errmsg = "";
			String auth_code = "0";
			if (act.equals("D")) {
				auth_code = "0";
				errmsg = " Waiting for authorization ";
			}

			txManager.commit(transact.status);
			addActionMessage("Deleted successfully..." + errmsg + "");
		} catch (Exception e) {
			// transact.txManager.rollback(transact.status);
			txManager.rollback(transact.status);
			addActionError("Unable to delete...");
			trace("Exception : Unable to delete the fee...." + e.getMessage());
			e.printStackTrace();
		}
		trace("************ Deleteing fee info end********");
		enctrace("************ Deleteing fee info end********");
		return "required_home";
	}

	public String deleteFee() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String subfeecode = getRequest().getParameter("feeid");
		String act = getRequest().getParameter("act");
		System.out.println("----- act ----- " + act);

		return "required_home";
	}

	public String authDeleteFeeHome() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {

		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authdeletefeehome";
	}

	List revelist;
	String recordid;

	public String getRecordid() {
		return recordid;
	}

	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}

	public List getRevelist() {
		return revelist;
	}

	public void setRevelist(List revelist) {
		this.revelist = revelist;
	}

	public String authFeeHome() {

		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {

			List feeLisConfig = feedao.getFeeAuthList(instid, jdbctemplate);
			feebean.setFeeLisConfig(feeLisConfig);
			System.out.println("fee config values" + feeLisConfig);
		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authfeehome";
	}

	public String authviewfeehome() {

		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String masterfeecode = getRequest().getParameter("feecode");
		feebean.setFeecode(masterfeecode);
		try {

			List feeLisConfig = feedao.getFeeAuthListValue1(instid, masterfeecode, jdbctemplate);
			feebean.setFeeLisConfig(feeLisConfig);

		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authviewfeehome";
	}

	public String authorizeDeauthorize() {
		trace("************* FEE authorization *****************");
		enctrace("*************FEE authorization *****************");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String userid = comUserId(session);
		String masterfeecode = getRequest().getParameter("feecode");
		IfpTransObj transact = commondesc.myTranObject("AUTHFEE", txManager);

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		int update_authdeauth = 0;
		int updateauthdeauthfeemaster = 0;
		int feeid = 0;
		Date date = new Date();

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		String statusmsg = "";
		String authstatus = "";
		String update_authdeauth_qury = null, remarks = "", updateFeeMaster = "";
		String auth = getRequest().getParameter("auth");
		System.out.println("auth-- " + auth);

		/*
		 * try{ if(auth.equals("Authorize")){ System.out.println(
		 * "AUTHORIZE..........." ); authstatus = "P"; statusmsg =
		 * " Authorized ";
		 * 
		 * update_authdeauth_qury
		 * =feedao.updateAuthFee(authstatus,userid,instid,masterfeecode);
		 * updateFeeMaster
		 * =feedao.updateAuthFeeMaster("1",instid,masterfeecode); }else{
		 * authstatus = "D"; statusmsg = " De-Authorized "; remarks =
		 * getRequest().getParameter("reason"); System.out.println("remarks -- "
		 * +remarks); update_authdeauth_qury =feedao.updateDeAuthFee(authstatus,
		 * userid, remarks, instid, masterfeecode); updateFeeMaster
		 * =feedao.updateAuthFeeMaster("9",instid,masterfeecode); } //int
		 * update_authdeauth =
		 * commondesc.executeTransaction(update_authdeauth_qury, jdbctemplate);
		 * int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);
		 * int updateauthdeauthfeemaster = jdbctemplate.update(updateFeeMaster);
		 */

		try {
			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				authstatus = "P";
				statusmsg = " Authorized ";

				// by gowtham-210819
				update_authdeauth_qury = feedao.updateAuthFee(authstatus, userid, instid, masterfeecode);
				update_authdeauth = jdbctemplate.update(update_authdeauth_qury);

				updateFeeMaster = feedao.updateAuthFeeMaster("1", instid, masterfeecode);
				updateauthdeauthfeemaster = jdbctemplate.update(updateFeeMaster);

			} else {
				authstatus = "D";
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				System.out.println("remarks -- " + remarks);

				// by gowtham-210819
				update_authdeauth_qury = feedao.updateDeAuthFee(authstatus, userid, remarks, instid, masterfeecode);
				update_authdeauth = jdbctemplate.update(update_authdeauth_qury);

				updateFeeMaster = feedao.updateAuthFeeMaster("9", instid, masterfeecode);
				updateauthdeauthfeemaster = jdbctemplate.update(updateFeeMaster);
			}

			// int update_authdeauth =
			// commondesc.executeTransaction(update_authdeauth_qury,
			// jdbctemplate);
			/*
			 * int update_authdeauth =
			 * jdbctemplate.update(update_authdeauth_qury); int
			 * updateauthdeauthfeemaster = jdbctemplate.update(updateFeeMaster);
			 */

			if (update_authdeauth >= 1 && updateauthdeauthfeemaster >= 1) {
				txManager.commit(transact.status);
				addActionMessage("Fee " + statusmsg + " Successfully");

				try {

					// added by gowtham_230719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					// String limitdesc = dao.getLimitName(instid, limitid,
					// jdbctemplate);
					auditbean.setActmsg("Fee [ " + masterfeecode + " ]  " + statusmsg + " Successfully");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("8080");
					auditbean.setRemarks(remarks);
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					audite.printStackTrace();
					trace("Exception in auditran : " + audite.getMessage());
				}

			} else {
				txManager.rollback(transact.status);
				addActionError("could not " + statusmsg + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(transact.status);
			addActionError(" Error while " + statusmsg + " the Card type " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "required_home";
	}

	// ----------Fee config is edit methods created by sardar on
	// 12-12-15-----------//

	public String EditFeeconfig() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {

			List feeLisConfig = feedao.getfeevalues(instid, jdbctemplate);
			feebean.setFeeLisConfig(feeLisConfig);

			System.out.println("fee config values" + feeLisConfig);

		} catch (Exception e) {
			addActionError("Error while Fetching the Fee " + e);
			trace("Error while Fetching the Fee " + e.getMessage());
		}
		return "View_Fee_Config";
	}

	public String EditViewFee() throws Exception {

		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String userid = comInstId(session);
		String mfeecode = getRequest().getParameter("feecode");

		feebean.setFeecode(mfeecode);
		System.out.println("this is my fee code 1" + mfeecode);

		String feename = commondesc.getfeename(instid, mfeecode, userid, jdbctemplate);

		feebean.setFeecode(feename);
		System.out.println("this is my fee code 2455" + feename);

		try {
			List fc = feedao.getfeevalues(instid, jdbctemplate);
			feebean.setFeeLisConfig(fc);

			List feeLisConfig = feedao.getFeeAuthListValue1(instid, mfeecode, jdbctemplate);
			feebean.setFeeLisConfig(feeLisConfig);
			System.out.println("this is fee values" + feeLisConfig);

			Iterator secitr = feeLisConfig.iterator();

			if (!feeLisConfig.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					feebean.setNumericCode((String) map.get("TXNCODE"));
					feebean.setCurrecyCode((String) map.get("CURR_CODE"));

					String feenam = (String) map.get("FEE_DESC");
					feebean.setFeename((String) map.get("FEE_DESC"));
					feebean.setFeename(feename);
					System.out.println("this is current fee name " + feenam);

				}
			}

		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "EditViewFee";
	}

	public String UpdateFeeConfg() {
		trace("************ Saving fee details begins ********");
		enctrace("************ Saving fee details begins ********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String userid = comUserId(session);

		String remarks = getRequest().getParameter("reason");

		String curcode1 = getRequest().getParameter("currencycode");

		String masterfeename = getRequest().getParameter("masterfeename");
		String usercode = comUserId(session);

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String mfeecode = getRequest().getParameter("feevalue");

		feebean.setFeecode(mfeecode);
		System.out.println("this is my fee name is" + masterfeename);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		IfpTransObj transact = commondesc.myTranObject("AUTHFEE", txManager);

		String feeamount = getRequest().getParameter("Feeamount");

		System.out.println("this is fee Fee_desc name check it" + masterfeename);
		// System.out.println("this is fee CUrrency code check it"+curcode);

		String Feeamount = getRequest().getParameter("Feeamount");

		try {
			String auth_code = "1";
			String mkchkrstatus = "D";
			String authmsg = "";
			String flag = getRequest().getParameter("act");
			trace("Got Flag :: " + flag);

			System.out.println("this is flag value here :" + flag);

			System.out.println("try is calling here ");

			if (flag.equals("D"))

			{
				auth_code = "1";
				mkchkrstatus = "D";

			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = ". Waiting for authorization";

			}

			System.out.println("after if condition here ");

			String TXN_CODE = "";
			String Currency = "";
			int UpdateCount = 0, Feelistupdate = 0;
			int updSeqno = 0;

			String mfeecode1 = commondesc.generatefeeid1(instid, masterfeename, jdbctemplate);

			System.out.println("THIs is new fee code do not change it" + mfeecode1);

			List feeLisConfig1 = feedao.getFeeAuthListValue1(instid, mfeecode1, jdbctemplate);

			// List limitdesc =
			// feedao.getFeeAuthListValue2(mfeecode1,instid,jdbctemplate);

			System.out.println("this is institution id " + feeLisConfig1);

			System.out.println("this is institution id " + instid);

			trace("Got : " + feeLisConfig1);

			int feedesccount = feeLisConfig1.size();
			Iterator itr = feeLisConfig1.iterator();
			String feeAmt = "";
			String curcode = "";
			String instid1 = "";
			String Feeid = "";
			if (!feeLisConfig1.isEmpty()) {
				while (itr.hasNext()) {
					Map map = (Map) itr.next();

					TXN_CODE = ((String) map.get("TXNCODE"));

					feeAmt = getRequest().getParameter("feeamount_" + TXN_CODE);
					curcode = ((String) map.get("CURR_CODE"));

					System.out.println("TXN_CODE:" + TXN_CODE + ":" + feeAmt + "curcode:" + curcode);

					instid1 = ((String) map.get("INST_ID"));
					Feeid = ((String) map.get("FEE_ID"));

					Feelistupdate = feedao.updatefeeDesc(feeAmt, instid, curcode, TXN_CODE, Feeid, auth_code,
							jdbctemplate);
					UpdateCount = UpdateCount + Feelistupdate;
				}
			}

			int UpdatefeeDesc = feedao.UPDATEFeeList(instid, auth_code, Feeid, jdbctemplate);

			System.out.println("insertFeelist::" + Feelistupdate + "limitdesccount:" + feedesccount + UpdatefeeDesc);
			if (feedesccount == UpdateCount && UpdatefeeDesc == 1) {

				updSeqno = commondesc.UpdateFeeSequance(instid, jdbctemplate);
				trace("feee Seqno Updated..." + updSeqno);

				txManager.commit(transact.status);
				trace("commited successfully");
				addActionMessage("FEE \"" + masterfeename + "\" Updated Successfully " + authmsg);
				trace("Limit \"" + masterfeename + "\" Updated Successfully " + authmsg);

			} else {
				txManager.rollback(transact.status);
				trace("rollbacked successfully");
				addActionMessage("FEE \"" + masterfeename + "\" updated Successfully " + authmsg);
				trace("FEE \"" + masterfeename + "\" updated Successfully " + authmsg);
			}
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg("Fee [ " + masterfeename + " ]  " + authmsg + "");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("8080");
				auditbean.setRemarks(remarks);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}

		} catch (Exception e) {
			System.out.println("ROLL BACKED" + e.getMessage());
			// txManager.rollback(status);
			txManager.rollback(transact.status);
			addActionError("Exception : Could not update the Fee ");
			trace("Exception : could not update the limit info : " + e.getMessage());
			e.printStackTrace();
		}
		trace("************ Saving fee details begins ********\n\n");
		enctrace("************ Saving fee details begins ********\n\n");
		return "required_home";
	}

	// ----------Fee config is edit methods Ended by sardar on
	// 12-12-15-----------//

	public String feeamountDeauthorize() {
		trace("************* FEE authorization *****************");
		enctrace("*************FEE authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("FEEDEAUTH", txManager);
		String statusmsg = "";
		try {

		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError(" Error while " + statusmsg + " the Fee " + e);
			trace("Error while " + statusmsg + " the Fee " + e.getMessage());
			e.printStackTrace();
		}
		return "authfeehome";
	}

	public String deleteauthorizeDeauthorize() {
		trace("************* Delete product authorization *****************");
		enctrace("************* Delete Product authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("DELETEFEEAUTH", txManager);
		String statusmsg = "";
		String authcode;

		return "required_home";
	}

	public String authorizeDeauthorizeDeletedRecords() {
		trace("************* FEE authorization/deauthorize *****************");
		enctrace("************* FEE authorization/deauthorize *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHFEE", txManager);
		String statusmsg = "";
		try {

		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError("Unable to Authorize....");
			trace("Error while " + statusmsg + " the Fee " + e.getMessage());
		}
		return "authfeehome";
	}
}// end class
