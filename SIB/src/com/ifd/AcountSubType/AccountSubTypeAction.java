package com.ifd.AcountSubType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.CardActivationBeans;
import com.ifd.AcountType.AccountTypeActionDao;
import com.ifd.beans.PersonalizeBean;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import oracle.sql.DATE;
import test.Date;
import test.Validation;

public class AccountSubTypeAction extends BaseAction {

	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	PersonalizeBean bean = new PersonalizeBean();

	public PersonalizeBean getBean() {
		return bean;
	}

	public void setBean(PersonalizeBean bean) {
		this.bean = bean;
	}

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

	public String cominstid(HttpSession session) {
		String username = (String) session.getAttribute("SS_USERNAME");
		System.out.println("this is user name" + username);

		return username;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	AccountSubTypeActionDao accountActionDao = new AccountSubTypeActionDao();
	CardActivationBeans cardbean = new CardActivationBeans();

	public CardActivationBeans getCardbean() {
		return cardbean;
	}

	public void setCardbean(CardActivationBeans cardbean) {
		this.cardbean = cardbean;
	}

	public List getAcctsubtypelist() {
		return acctsubtypelist;
	}

	public void setAcctsubtypelist(List acctsubtypelist) {
		this.acctsubtypelist = acctsubtypelist;
	}

	List instlist;

	public List getInstlist() {
		return instlist;
	}

	public void setInstlist(List instlist) {
		this.instlist = instlist;
	}

	private List acctsubtypelist;

	private List accttypelist;

	private String accountsubtypelenval;

	public String addAccountSubtype() {
		trace("*************** addAccount sub type Begins **********");
		enctrace("*************** addAccount sub type Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {
			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
			}

			if (accountActionDao.getAcctTypeList(instid, jdbctemplate).size() == 0) {
				addActionError(
						" You are Not Configured Account Type , Configure Account Type and Try Account Sub Type ");
				return "required_home";
			}
			List accouttypelist = accountActionDao.getAcctTypeList(instid, jdbctemplate);

			setAccttypelist(accouttypelist);

			trace("checking accctsub type list " + getAcctsubtypelist());

			setAccountsubtypelenval(commondesc.getAccountSubTypeLen(instid, jdbctemplate));

		} catch (Exception e) {
			addActionError(" Error ");
			enctrace("addAccounttype Error ::::::" + e);

		}
		trace("\n\n");
		enctrace("\n\n");
		return "add_accountsubtype_home";

	}

	public String addCardtypeConfig() {
		trace("*************** Adding Card Type Begins **********");
		enctrace("*************** Adding Card Type Begins **********");
		HttpSession session = getRequest().getSession();

		try {
			if (getRequest().getParameter("type") == "MERCH") {
				session.setAttribute("CURAPPTYPE", "MERCHANT");
				trace("Session Type.. MERCHANT");
			}
			// instlist = accountActionDao.instList( jdbctemplate );

		} catch (Exception e) {
			addActionError(" Error ");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "add_accounttype_home";

	}

	public String saveAccountSubType() {
		trace("***************  saveAccountSubType**********");
		enctrace("***************  saveAccountSubType **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");
		String usercode = comUserId(session);
		String externaluser = (String) session.getAttribute("EXTERNALUSER");

		System.out.println("user name is" + externaluser);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();
		String acctsubtypeid = null;
		String accounttpeid = null;

		IfpTransObj transact = commondesc.myTranObject("SAVEACCTSUBTYPE", txManager);
		// String cardtpeid = commondesc.fchCardTypeSequance(instid,
		// jdbctemplate);
		 accounttpeid = getRequest().getParameter("accounttypeid");
		 acctsubtypeid = getRequest().getParameter("accountsubtypeid");
		String accountsubtypedesc = getRequest().getParameter("accountsubtypename");
		String accountsubtypelenval = getRequest().getParameter("accountsubtypelenval");

		if (accounttpeid.equals("-1") || acctsubtypeid == "") {
			addFieldError("accounttypeid", " Please enter account type id in number");
			return addAccountSubtype();
		}

		bean.setAccounttypeid(acctsubtypeid);
		System.out.println("accountid " + acctsubtypeid);
		bean.setAccountsubtypeid(acctsubtypeid);
		bean.setAccountsubtypename(accountsubtypedesc);

		int accountsubtypeid_val = Validation.number(acctsubtypeid);
		if (accountsubtypeid_val == 0) {
			addFieldError("accountsubtypeid", " Please enter account type id in number");
			return addAccountSubtype();
		}

		int accountsubtypeid_len = acctsubtypeid.length();
		int accountsubtypelenval_len = Integer.parseInt(accountsubtypelenval);
		System.out.println("accounttypelenval_len " + accountsubtypelenval_len);

		if (accountsubtypeid_len != accountsubtypelenval_len) {
			System.out.println("accountsubtypelenval_len " + accountsubtypelenval_len);
			addActionError("account type value length should be " + accountsubtypelenval_len + " digit");
			return addAccountSubtype();
		}

		boolean accountsubdesc = Validation.Spccharcter(accountsubtypedesc);
		if (!accountsubdesc) {
			addFieldError("accountsubtypename", " Please enter account type name in characters");
			return addAccountSubtype();
		}

		String flag = getRequest().getParameter("act");
		System.out.println("flag == " + flag);
		String auth_code = "1";
		String mkchkrstatus = "D";
		String authmsg = "";
		if (flag.equals("D")) {
			auth_code = "1";
			mkchkrstatus = "D";
		} else {
			auth_code = "0";
			mkchkrstatus = "M";
			authmsg = ". Waiting for Authorization";
		}

		String userid = comUserId(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		trace("Checking account type existance..");

		if (accountActionDao.checkAccountSUBTypeExist(acctsubtypeid, instid, jdbctemplate) != 0) {
			addActionError(acctsubtypeid + " Account SUB type ID Already exist");
			trace(accountsubtypedesc + " Account Sub type Already exist");
			return addAccountSubtype();
		}

		int acctsubtypenameexist = accountActionDao.checkAccountTypeNameExist(accountsubtypedesc, instid, jdbctemplate);
		trace("Checking acctsubtypenameexist existance..got : " + acctsubtypenameexist);
		if (acctsubtypenameexist != 0) {
			addActionError(
					"  Account Sub Type type name [ " + accountsubtypedesc + " ] Alredy exist. Try with another name");
			trace(accountsubtypedesc + " Account Sub Type Already exist");
			return addAccountSubtype();
		}

		try {

			trace("accountid:" + accounttpeid + "accountsubtypeid:::" + acctsubtypeid + "accountsubtypedesc"
					+ accountsubtypedesc);
			// int acctsubtypeid =
			// accountActionDao.sequenceAccountSubtype(instid, jdbctemplate);
			String saveqry = accountActionDao.getSaveAccountSUBTypeqry(instid, accounttpeid, acctsubtypeid,
					accountsubtypedesc, usercode, auth_code, mkchkrstatus);

			/*
			 * enctrace("save qry __" + saveqry ); int acctsubtypeidsaveqry =
			 * commondesc.executeTransaction(saveqry, jdbctemplate);
			 */

			/// by gowtham-290819
			/*
			 * int acctsubtypeidsaveqry = commondesc.executeTransaction(saveqry,
			 * jdbctemplate);
			 */
			int acctsubtypeidsaveqry = jdbctemplate.update(saveqry, new Object[] { instid, accounttpeid, acctsubtypeid,
					accountsubtypedesc, usercode, "", "", auth_code, mkchkrstatus });

			String updseqQuery = accountActionDao.updateAccountSubtypeid(instid);
			int updateacctsubtype = commondesc.executeTransaction(updseqQuery, jdbctemplate);
			System.out.println("---------" + acctsubtypeidsaveqry + updateacctsubtype);
			if (acctsubtypeidsaveqry == 1 && updateacctsubtype == 1) {
				transact.txManager.commit(transact.status);
				addActionMessage("Account Sub Type \"" + accountsubtypedesc + "\"  configured successfully" + authmsg);
				trace("Account Sub Type saved successfully");
			}

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg(
						"Acccount Sub Type [ " + accountsubtypedesc + " - " + accounttpeid + " ] configured. " + authmsg);
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3030");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}

			/************* AUDIT BLOCK **************/

		} catch (NumberFormatException ne) {
			ne.printStackTrace();
			transact.txManager.rollback(transact.status);
			addActionError("Invalid Account type id...");
			trace(" Could not save Account type. " + ne.getMessage());
			ne.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			transact.txManager.rollback(transact.status);
			addActionError(" Could not Account type. ");
			trace(" Could not save card type. " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String editAccountSubtype() {
		trace("*************** Edit Account Sub Type Begins **********");
		enctrace("*************** Edit Account Sub Type Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");
		String usercode = comUserId(session);
		String accounttypeqry = "";
		String type = getRequest().getParameter("type");

		instlist = accountActionDao.instList(jdbctemplate);
		try {

			List accountsubtype_detail_result = accountActionDao.getAccountsubtypeList(instid, jdbctemplate);
			System.out.println("result EditcardtypeDetail      " + accountsubtype_detail_result);
			setAcctsubtypelist(accountsubtype_detail_result);
		} catch (Exception e) {
			addActionError("Error while getting  the account sub type list " + e);
			trace("Error while getting account sub type list " + e.getMessage());
		}

		trace("\n\n");
		enctrace("\n\n");
		return "edit_Accountsubtype_home";

	}

	public String editAccountSubTypeDetails() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String accountsubtype = getRequest().getParameter("accountsubtype");

		if (accountsubtype.equals("-1")) {
			System.out.println("validation part branchcode");
			addActionError(" Please select Sub account type");
			return editAccountSubtype();
		}

		trace("getting edit accounttypeid" + accountsubtype);
		// setAccounttypelenval(commondesc.getAccountTypeLen(instid,
		// jdbctemplate));
		try {
			List accountype_detail_result = accountActionDao.geteditAccountSubTypeList(instid, jdbctemplate,
					accountsubtype);
			System.out.println("result viewcardtypeDetail      " + accountype_detail_result);
			setAcctsubtypelist(accountype_detail_result);
		} catch (Exception e) {
			addActionError("Error while getting  the card type list " + e);
			trace("Error while getting card type list " + e.getMessage());
		}

		trace("*************** Edit Account Type Begins **********");
		enctrace("*************** Edit Account Type Begins **********");
		trace("\n\n");
		enctrace("\n\n");
		return "edit_accountsubtype_view";

	}

	public String editsaveAccountSubType() throws Exception {
		trace("*************** update account sub  Type Begins **********");
		enctrace("*************** update account sub  Type Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		String accountsubtypedesc = null;
		String accounttpeid = null;
		String acctsubtypeid = null;

		String userid = comUserId(session);
		String usercode = comUserId(session);
		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		String accounttypeid = getRequest().getParameter("accounttypeid");
		String accountsubtypeid = getRequest().getParameter("accountsubtypeid");
		String accountsubtypename = getRequest().getParameter("accountsubtypename");

		if (accounttypeid.equals("-1") || accounttypeid == "") {
			addActionError(" Please enter account type id in number");
			return editAccountSubtype();
		}

		System.out.println("accountid " + accounttypeid);

		int accountsubtypeid_val = Validation.number(accountsubtypeid);
		if (accountsubtypeid_val == 0) {
			addFieldError("accountsubtypeid", " Please enter account sub type id in number");
			return editAccountSubtype();
		}

		/*
		 * int accountsubtypeid_len=accountsubtypeid.length(); int
		 * accountsubtypelenval_len=Integer.parseInt(accountsubtypelenval);
		 * System.out.println("accounttypelenval_len "
		 * +accountsubtypelenval_len);
		 * 
		 * if(accountsubtypeid_len !=accountsubtypelenval_len) {
		 * System.out.println("accountsubtypelenval_len "
		 * +accountsubtypelenval_len); addActionError(
		 * "account type value length should be "+accountsubtypelenval_len+
		 * " digit"); return addAccountSubtype(); }
		 */
		boolean accountsubdesc = Validation.Spccharcter(accountsubtypename);
		if (!accountsubdesc) {
			addActionError(" Please enter account type sub name in characters");
			return editAccountSubtype();
		}

		IfpTransObj transact = commondesc.myTranObject("UPDATECARDTYPE", txManager);
		String flag = getRequest().getParameter("act");
		System.out.println("flag == " + flag);
		// String externaluser = (String)session.getAttribute("EXTERNALUSER");
		String usertext = "";
		usertext = getRequest().getParameter("usertext");

		System.out.println("user text name and user name is" + usertext);

		String auth_code = "1";
		String mkchkrstatus = "D";
		String authmsg = "";

		if (flag.equals("D")) {
			auth_code = "1";
			mkchkrstatus = "D";
		} else {
			auth_code = "0";
			mkchkrstatus = "M";
			authmsg = ". Waiting for Authorization";
		}

		try {

			String updateqry = accountActionDao.updatesubaccounttypedetails(instid, accounttypeid, accountsubtypeid,
					accountsubtypename, mkchkrstatus, auth_code, usercode, jdbctemplate);
			enctrace("update qry __" + updateqry);

			/* commondesc.executeTransaction(updateqry, jdbctemplate); */
			// added by gowtam290819
			jdbctemplate.update(updateqry, new Object[] { mkchkrstatus, auth_code, DATE.getCurrentDate(), usercode,
					accountsubtypedesc, instid, accounttpeid, acctsubtypeid });
			/* commondesc.executeTransaction(updateqry, jdbctemplate); */

			transact.txManager.commit(transact.status);
			addActionMessage("Account Sub type \"" + accountsubtypename + "\" Updated successfully" + authmsg);
			trace("account sub type updated successfully");

			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg(
						"account Type [ " + accountsubtypename + " - " + accountsubtypeid + " ] updated. " + authmsg);
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("6061");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (NumberFormatException ne) {
			transact.txManager.rollback(transact.status);
			addActionError("Invalid update type id...");
			trace(" Could not update account sub type. " + ne.getMessage());
			ne.printStackTrace();
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Could not update account sub type. ");
			trace(" Could not update account sub type. " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String viewCardtype() {
		trace("*************** Viewing Card Type Begins **********");
		enctrace("*************** Viewing Card Type Begins **********");
		HttpSession session = getRequest().getSession();
		String cardtypeqry = "";
		String type = getRequest().getParameter("type");
		// instlist = accountActionDao.instList( jdbctemplate );
		trace("\n\n");
		enctrace("\n\n");
		return "view_cardtype_home";
	}

	public void listOfsubaccounttype() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = getRequest().getParameter("instid");

		List subaccounttypelist;
		subaccounttypelist = accountActionDao.getsubaccounttypeList(instid, jdbctemplate);
		String opt = "<option value='-1'>-SELECT-</option>";

		if (!subaccounttypelist.isEmpty()) {
			Iterator itr = subaccounttypelist.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String subaccounttypeid = (String) mp.get("ACCTSUBTYPEID");
				String subaccounttypedesc = (String) mp.get("ACCTSUBTYPEDESC");
				opt += "<option value=" + subaccounttypeid + ">" + subaccounttypedesc + "</option>";
			}
		}
		getResponse().getWriter().write(opt);
	}

	public String authAccountsubtype() {
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		try {
			String acctsubtypeid = getRequest().getParameter("acctsubtype");
			List getallcardtype_qury = accountActionDao.getAcctSubTypeListByCardIDForauth(instid, jdbctemplate,
					acctsubtypeid);
			if (!getallcardtype_qury.isEmpty()) {
				ListIterator itr = getallcardtype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("ADDEDBY");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("ADDEDBY", username);
					itr.remove();
					itr.add(mp);
				}
				System.out.println(getallcardtype_qury + "   ---getallsubfee_qury---   ");
				setAcctsubtypelist(getallcardtype_qury);
			} else {
				addActionError("Records not found");
			}
		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authAccountsubtype";
	}

	public String authListAccountSubType() {
		trace("authListAccountSubType calledd");
		HttpSession session = getRequest().getSession();

		String SS_USERNAME = comInstId(session);
		System.out.println("this is user namdse132" + SS_USERNAME);
		String instid = comInstId(session);
		try {

			List accountsubtypelist = accountActionDao.getAllAcctSubTypeList(instid, jdbctemplate);
			if (!accountsubtypelist.isEmpty()) {
				setAcctsubtypelist(accountsubtypelist);
				trace("Get Acctsub typelist ::" + getAcctsubtypelist());
			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authListAccountSubType";
	}

	public String authdeauthacctsubtype() {
		trace("*************authdeauthacctsubtypeAccountSubTypeAction *****************");
		enctrace("*************authdeauthacctsubtypeAccountSubTypeAction *****************");
		HttpSession session = getRequest().getSession();

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		int update_authdeauth = 0;
		Date date = new Date();
		String acsubtypeid = null;
		String cardtypeid = null;

		IfpTransObj transact = commondesc.myTranObject("AUTHCARDTYPE", txManager);
		String statusmsg = "", remarks = "";
		try {
			String instid = comInstId(session);
			String userid = comUserId(session);

			String username = commondesc.getUserName(instid, userid, jdbctemplate);
			System.out.println("Testing user name " + username);

			String acctsubtypeid = getRequest().getParameter("acctsubtypeid");
			System.out.println("acctsubtypeid === " + acctsubtypeid);
			String authstatus = "";
			String update_authdeauth_qury = null;
			String auth = getRequest().getParameter("auth");
			System.out.println("auth-- " + auth);

			/*
			 * if(auth.equals("Authorize")){ System.out.println(
			 * "AUTHORIZE..........." ); authstatus = "P"; statusmsg =
			 * " Authorized "; update_authdeauth_qury
			 * =accountActionDao.updateAuthAcctSubType(authstatus,userid,instid,
			 * acctsubtypeid); }else{ authstatus = "D"; statusmsg =
			 * " De-Authorized "; remarks = getRequest().getParameter("reason");
			 * System.out.println("remarks -- "+remarks); update_authdeauth_qury
			 * =accountActionDao.updateDeAuthAcctSubType(authstatus, userid,
			 * remarks, instid, acctsubtypeid); } //int update_authdeauth =
			 * commondesc.executeTransaction(update_authdeauth_qury,
			 * jdbctemplate); int update_authdeauth =
			 * jdbctemplate.update(update_authdeauth_qury);
			 */

			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				authstatus = "P";
				statusmsg = " Authorized ";

				// added by gowtham-200819
				update_authdeauth_qury = accountActionDao.updateAuthAcctSubType(authstatus, userid, instid,
						acctsubtypeid);
				
				
				update_authdeauth=jdbctemplate.update(update_authdeauth_qury);
				/*update_authdeauth = jdbctemplate.update(update_authdeauth_qury,
						new Object[] { authstatus, "1", date.getCurrentDate(), userid, instid, acsubtypeid });*/

			} else {
				authstatus = "D";
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				System.out.println("remarks -- " + remarks);
				update_authdeauth_qury = accountActionDao.updateDeAuthAcctSubType(authstatus, userid, remarks, instid,
						acctsubtypeid);

				update_authdeauth=	jdbctemplate.update(update_authdeauth_qury);
				// by gowtham-200819
				// int update_authdeauth =
				// jdbctemplate.update(update_authdeauth_qury);
			/*	update_authdeauth = jdbctemplate.update(update_authdeauth_qury,
						new Object[] { authstatus, "9", date.getCurrentDate(), userid, remarks, instid, cardtypeid });*/
			}

			// int update_authdeauth =
			// commondesc.executeTransaction(update_authdeauth_qury,
			// jdbctemplate);
			// by gowtham-200819
			// int update_authdeauth =
			// jdbctemplate.update(update_authdeauth_qury);

			if (update_authdeauth >= 1) {
				txManager.commit(transact.status);
				addActionMessage("Acct Sub type " + statusmsg + " Successfully");

				/************* AUDIT BLOCK **************/
				try {
					String cardtyepdesc = accountActionDao.getCardTypeDesc(instid, acctsubtypeid, jdbctemplate);
					auditbean.setActmsg("Acct Sub Type [ " + acctsubtypeid + " ] is " + statusmsg + "");

					// added by gowtham_230719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setUsercode(username);
					auditbean.setAuditactcode("6061");
					auditbean.setRemarks(remarks);
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

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

	public String viewAccountSubType() {

		trace("*************** Viewing SubAccount Type Begins **********");
		enctrace("*************** Viewing SubAccount Type Begins **********");
		HttpSession session = getRequest().getSession();
		String accounttypeqry = "";
		String type = getRequest().getParameter("type");
		instlist = accountActionDao.instList(jdbctemplate);
		trace("\n\n");
		enctrace("\n\n");
		return "view_Accountsubtype_home";
	}

	public String viewAccountSubTypeDetails() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		// ArrayList al= new ArrayList();
		try {
			String acctsubtypeid = getRequest().getParameter("subaccounttype");
			System.out.println("acctsubtypeid-----  - - -" + acctsubtypeid);
			List getallsubaccounttype_qury = accountActionDao.getSubAccountTypeListByCardID(instid, jdbctemplate,
					acctsubtypeid);
			System.out.println("--------getallsubaccounttype_qury-----  - - -" + getallsubaccounttype_qury);
			if (!getallsubaccounttype_qury.isEmpty()) {
				ListIterator itr = getallsubaccounttype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("ADDEDBY");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("USER_NAME", username);
					itr.remove();
					itr.add(mp);
				}
				cardbean.setSubaccounttype(getallsubaccounttype_qury);
				// System.out.println(cardbean.getSubaccounttype());

			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while fetching the cardtype " + e);
			trace("Error while fetching the cardtype " + e.getMessage());
			return "required_home";
		}
		return "view_AccountSubType_Details";
	}

	public String viewCardtypeMaker() {
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		System.out.println("session:" + session + "|||instid::" + instid);
		try {
			List getallcardtype_qury = accountActionDao.getCardTypeListByMakerCardID(instid, jdbctemplate);
			System.out.println("--------getallcardtype_qury-----  - - -" + getallcardtype_qury);
			if (!getallcardtype_qury.isEmpty()) {
				ListIterator itr = getallcardtype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("MAKER_ID");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("USER_NAME", username);
					itr.remove();
					itr.add(mp);
				}
				cardbean.setCardtype(getallcardtype_qury);
			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while fetching the cardtype " + e);
			trace("Error while fetching the cardtype " + e.getMessage());
		}
		return "viewCardTypeDetails";
	}

	public List getAccttypelist() {
		return accttypelist;
	}

	public void setAccttypelist(List accttypelist) {
		this.accttypelist = accttypelist;
	}

	public String getAccountsubtypelenval() {
		return accountsubtypelenval;
	}

	public void setAccountsubtypelenval(String accountsubtypelenval) {
		this.accountsubtypelenval = accountsubtypelenval;
	}

} // end class
