package com.ifd.AcountType;

import java.io.IOException;
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

import test.Date;
import test.Validation;

public class AccountTypeAction extends BaseAction {

	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	PersonalizeBean bean =new PersonalizeBean();

	public PersonalizeBean getBean() {
		return bean;
	}

	public void setBean(PersonalizeBean bean) {
		this.bean = bean;
	}

	public List list;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
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

	AccountTypeActionDao accountActionDao = new AccountTypeActionDao();
	CardActivationBeans cardbean = new CardActivationBeans();

	public CardActivationBeans getCardbean() {
		return cardbean;
	}

	public void setCardbean(CardActivationBeans cardbean) {
		this.cardbean = cardbean;
	}

	List instlist;
	List accounttypelist;

	public List getaccounttypelist() {
		return accounttypelist;
	}

	public void setaccounttypelist(List accounttypelist) {
		this.accounttypelist = accounttypelist;
	}

	public List getInstlist() {
		return instlist;
	}

	public void setInstlist(List instlist) {
		this.instlist = instlist;
	}

	private String accounttypelenval;

	private List authaccttypeList;

	public String addAccounttype() {
		trace("*************** addAccounttype Begins **********");
		enctrace("*************** addAccounttype Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {
			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
			}

			setAccounttypelenval(commondesc.getAccountTypeLen(instid, jdbctemplate));

		} catch (Exception e) {
			addActionError(" Error ");
			enctrace("addAccounttype Error ::::::" + e);

		}
		trace("\n\n");
		enctrace("\n\n");
		return "add_accounttype_home";

	}

	public String addCardtypeConfig() {
		trace("*************** Adding Card Type Begins **********");
		enctrace("*************** Adding Card Type Begins **********");
		HttpSession session = getRequest().getSession();

		try {
			if (getRequest().getParameter("type") == "MERCH") {
				session.setAttribute("CURAPPTYPE", "MERCHANT");
				trace("Session Type..instlist MERCHANT");
			}
			// instlist = accountActionDao.instList( jdbctemplate );

		} catch (Exception e) {
			addActionError(" Error ");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "add_accounttype_home";

	}

	public String saveAccountType() {
		trace("***************saveAccountType Begins **********");
		enctrace("*************** saveAccountType Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");
		String usercode = comUserId(session);

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		String userid = comUserId(session);
		Date date =  new Date();
		String accounttpeidnew=null;

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		IfpTransObj transact = commondesc.myTranObject("SAVECARDTYPE", txManager);
		// String cardtpeid = commondesc.fchCardTypeSequance(instid,
		// jdbctemplate);
		String accountid = getRequest().getParameter("accounttypeid");
		String accounttypedesc = getRequest().getParameter("accounttypename");
		String accounttypelenval = getRequest().getParameter("accounttypelenval");
		
		
		bean.setAccounttypeid(accountid);
		System.out.println("accountid "+accountid);
		bean.setAccounttypename(accounttypedesc);
		
	int accountid_val=Validation.number(accountid);
	    if(accountid_val==0)
	{
	    	 addFieldError("accounttypeid"," Please enter account type id in number");
	    	 return 	addAccounttype();
	}
		
	  int accountid_len=accountid.length();
	  int accounttypelenval_len=Integer.parseInt(accounttypelenval);
	  System.out.println("accounttypelenval_len "+accounttypelenval_len);
	  
	    if(accountid_len !=accounttypelenval_len)
	    {
	    	System.out.println("accountid_len111 "+accounttypelenval_len);
	    	addActionError( "account type value length should be "+accounttypelenval_len+" digit");
	    	return 	addAccounttype();
	    }
	    
	    boolean accountdesc=Validation.Spccharcter(accounttypedesc);
	    if(!accountdesc)
	{
	    	 addFieldError("accounttypename"," Please enter account type name in characters");
	    	 return 	addAccounttype();
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

		trace("Checking account type existance..");

		if (accountActionDao.checkAccountTypeExist(accountid, instid, jdbctemplate) != 0) {
			addActionError(accounttypedesc + " Account type Already exist");
			trace(accounttypedesc + " Account type Already exist");
			return this.addCardtypeConfig();
		}

		int cardtypenameexist = accountActionDao.checkAccountTypeNameExist(accounttypedesc, instid, jdbctemplate);
		trace("Checking card type name existance..got : " + cardtypenameexist);
		if (cardtypenameexist != 0) {
			addActionError(" Account Type type name [ " + accounttypedesc + " ] Alredy exist. Try with another name");
			trace(accounttypedesc + " Card type Already exist");
			return this.addCardtypeConfig();
		}

		try {
			
			trace(" acctypeid ====> "+accountid  +"  accounttypedesc===  "+accounttypedesc);

			String saveqry = accountActionDao.getSaveAccountTypeqry(instid, accountid, accounttypedesc, usercode,
					auth_code, mkchkrstatus);
			enctrace("save qry __" + saveqry);
			
			//BY GOWTHAM30819	
		/*	commondesc.executeTransaction(saveqry, jdbctemplate);*/
			jdbctemplate.update(saveqry,new Object[]{instid,accountid,accounttypedesc,usercode,date.getCurrentDate(),"","",auth_code,mkchkrstatus				});
			
			
			transact.txManager.commit(transact.status);
			addActionMessage("Account type \"" + accounttypedesc + "\" configured successfully" + authmsg);
			trace("Card type saved successfully");

			/************* AUDIT BLOCK **************/
			try {
				
				//added by gowtham_230719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setActmsg(
						"Acccount Type [ " + accounttypedesc + " - " + accountid + " ] configured. " + authmsg);
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("6060");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (NumberFormatException ne) {
			transact.txManager.rollback(transact.status);
			addActionError("Invalid Account type id...");
			trace(" Could not save Account type. " + ne.getMessage());
			ne.printStackTrace();
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Could not Account type. ");
			trace(" Could not save card type. " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String editAccounttype() {
		trace("*************** Edit Account Type Begins **********");
		enctrace("*************** Edit Account Type Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");
		String usercode = comUserId(session);
		String accounttypeqry = "";
		String type = getRequest().getParameter("type");

		instlist = accountActionDao.instList(jdbctemplate);
		try {

			List accounttype_detail_result = accountActionDao.getAccounttypeList(instid, jdbctemplate);
			System.out.println("result EditcardtypeDetail      " + accounttype_detail_result);
			setaccounttypelist(accounttype_detail_result);
		} catch (Exception e) {
			addActionError("Error while getting  the card type list " + e);
			trace("Error while getting card type list " + e.getMessage());
		}

		trace("\n\n");
		enctrace("\n\n");
		return "edit_Accounttype_home";

	}

	public String editAccounttypeDetails() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String accountid = getRequest().getParameter("accounttype");
		
		 if(accountid.equals("-1"))
		 {
		 System.out.println("validation part accountid");
		 addActionError(" Please select account type");
		 return editAccounttype(); 	
		 }
		
		
		
		
		trace("getting edit accounttypeid" + accountid);
		setAccounttypelenval(commondesc.getAccountTypeLen(instid, jdbctemplate));
		try {
			List accountype_detail_result = accountActionDao.getAccountTypeList(instid, jdbctemplate, accountid);
			System.out.println("result viewcardtypeDetail      " + accountype_detail_result);
			setaccounttypelist(accountype_detail_result);
		} catch (Exception e) {
			addActionError("Error while getting  the card type list " + e);
			trace("Error while getting card type list " + e.getMessage());
		}

		trace("*************** Edit Account Type Begins **********");
		enctrace("*************** Edit Account Type Begins **********");
		trace("\n\n");
		enctrace("\n\n");
		return "edit_accounttype_view";

	}

	public String editsaveAccountType() throws Exception {
		trace("*************** update Card Type Begins **********");
		enctrace("*************** update Card Type Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");
		String usercode = comUserId(session);
		String accounttypeid = getRequest().getParameter("accounttypeid");
		String accounttypename = getRequest().getParameter("accounttypename");
		
		  System.out.println("accounttypeid "+accounttypeid);	
	
		bean.setAccounttypename(accounttypename);
		
	/*int accountid_val=Validation.number(accounttypeid);
	    if(accountid_val==0)
	{
	    	 addFieldError("accounttypeid"," Please enter account type id in number");
	    	 return editAccounttypeDetails();
	}
		
	  int accountid_len=accounttypeid.length();
	  int accounttypelenval_len=Integer.parseInt(accounttypelenval);
	  System.out.println("accounttypelenval_len "+accounttypelenval_len);
	  
	    if(accountid_len !=accounttypelenval_len)
	    {
	    	System.out.println("accountid_len111 "+accounttypelenval_len);
	    	addActionError( "account type value length should be "+accounttypelenval_len+" digit");
	    	return editAccounttypeDetails();
	    }
	    */
	  boolean accountdesc=Validation.Spccharcter(accounttypename);
	    if(!accountdesc)
	{    System.out.println("accountdesc "+accountdesc);
	    	/* addFieldError("accounttypename","Please enter account type name in characters");*/
	addActionError("ACCOUNT TYPE Name Should Be Characters");
	    	return editAccounttype();
	    	
	}
		
		
		
		
		IfpTransObj transact = commondesc.myTranObject("UPDATECARDTYPE", txManager);
		String flag = getRequest().getParameter("act");
		System.out.println("flag == " + flag);
		
		//added by gowtham_230719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Date date =  new Date();
		String userid = comUserId(session);
		String accttypeid=null;

		//String userid = comUserId(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

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

			String updateqry = accountActionDao.updateaccounttypedetails(instid, accounttypeid, accounttypename,
			mkchkrstatus, auth_code, usercode, jdbctemplate);
			enctrace("update qry __" + updateqry);
			
			/*commondesc.executeTransaction(updateqry, jdbctemplate);*/
			///BY GOWTHAM-300819
			jdbctemplate.update(updateqry,new Object[]{accounttypename,mkchkrstatus,auth_code,usercode,date.getCurrentDate(),instid,accttypeid});
			
			transact.txManager.commit(transact.status);
			addActionMessage("Account type \"" + accounttypename + "\" Updated successfully" + authmsg);
			trace("account type updated successfully");

			try {
				
				//added by gowtham_230719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setActmsg(
						"account Type [ " + accounttypename + " - " + accounttypeid + " ] updated. " + authmsg);
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("6060");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (NumberFormatException ne) {
			transact.txManager.rollback(transact.status);
			addActionError("Invalid update type id...");
			trace(" Could not update account type. " + ne.getMessage());
			ne.printStackTrace();
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Could not update account type. ");
			trace(" Could not update account type. " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String ViewAccounttype() {
		trace("*************** Viewing Account Type Begins **********");
		enctrace("*************** Viewing Account Type Begins **********");
		HttpSession session = getRequest().getSession();
		String accounttypeqry = "";
		String type = getRequest().getParameter("type");
		instlist = accountActionDao.instList(jdbctemplate);
		trace("\n\n");
		enctrace("\n\n");
		return "view_Accounttype_home";
	}

	public void listOfAccountType() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = getRequest().getParameter("instid");
		List accounttypelist;
		accounttypelist = accountActionDao.getAccounttypeList(instid, jdbctemplate);
		String opt = "<option value='-1'>-SELECT-</option>";

		if (!accounttypelist.isEmpty()) {
			Iterator itr = accounttypelist.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String accounttypeid = (String) mp.get("ACCTTYPEID");
				String accounttypedesc = (String) mp.get("ACCTTYPEDESC");
				opt += "<option value=" + accounttypeid + ">" + accounttypedesc + "</option>";
			}
		}
		getResponse().getWriter().write(opt);
	}

	public void fchCardtypeDesc() throws IOException {

		String accttypeid = getRequest().getParameter("accttypeid");
		String accounttypedesc = accountActionDao.fchCardtypeDesc(accttypeid);
		String accounttype = (String) jdbctemplate.queryForObject(accounttypedesc, String.class);
		getResponse().getWriter().write(accounttype);
	}

	public String authAccountype() {
		trace("authAccountype method called....");
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		try {
			String accounttypeid = getRequest().getParameter("authaccttypeList");
			trace("authaccttypeList:" + accounttypeid);
			List getallcardtype_qury = accountActionDao.getAcctTYpeListByAcctID(instid, jdbctemplate, accounttypeid);
			if (!getallcardtype_qury.isEmpty()) {
				ListIterator itr = getallcardtype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("ADDEDBY");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("USER_NAME", username);
					itr.remove();
					itr.add(mp);
				}
				System.out.println(getallcardtype_qury + "   ---getallsubfee_qury---   ");
				setAuthaccttypeList(getallcardtype_qury);
			} else {
				addActionError("Records not found");
			}
		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authaccounttype";
	}

	public String authListAccountType() {
		trace("authListAccountType started...");
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		try {
			List getallaccttype_qury = accountActionDao.getAllAccountTypeList(instid, jdbctemplate);
			if (!getallaccttype_qury.isEmpty()) {
				trace(getallaccttype_qury + "   ---getallaccttype_qury---   ");
				setAuthaccttypeList(getallaccttype_qury);
			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authselectaccounttype";
	}

	public String authdeauthaccount() {
		trace("*************authdeauthaccounttype authorization *****************");
		enctrace("*************authdeauthaccounttype authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHCARDTYPE", txManager);
		String statusmsg = "", remarks = "";
		

		//added by gowtham_230719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Date date =  new Date();
		int update_authdeauth =0;
		
		try {
			String instid = comInstId(session);
			String userid = comUserId(session);
			// String username = commondesc.getUserName(instid, userid,
			// jdbctemplate);
			String accttypeid = getRequest().getParameter("accttypeid");
			System.out.println("accttypeid === " + accttypeid);
			String authstatus = "";
			String update_authdeauth_qury = null;
			String auth = getRequest().getParameter("auth");
			System.out.println("auth-- " + auth);

			String username = commondesc.getUserName(instid, userid, jdbctemplate);
			System.out.println("Testing user name " + username);

			
			
		/*	
			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				authstatus = "P";
				statusmsg = " Authorized ";
				update_authdeauth_qury = accountActionDao.updateAuthAcctType(authstatus, userid, instid, accttypeid);
			} else {
				authstatus = "D";
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				System.out.println("remarks -- " + remarks);
				update_authdeauth_qury = accountActionDao.updateDeAuthAcctType(authstatus, userid, remarks, instid,
						accttypeid);
			}
			// int update_authdeauth =
			// commondesc.executeTransaction(update_authdeauth_qury,
			// jdbctemplate);
			int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);*/
			
			

			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				authstatus = "P";
				statusmsg = " Authorized ";
				update_authdeauth_qury = accountActionDao.updateAuthAcctType(authstatus, userid, instid, accttypeid);
				 update_authdeauth = jdbctemplate.update(update_authdeauth_qury,new Object[]{authstatus,"1",date.getCurrentDate(),userid,instid,accttypeid});
			} else {
				authstatus = "D";
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				System.out.println("remarks -- " + remarks);
				update_authdeauth_qury = accountActionDao.updateDeAuthAcctType(authstatus, userid, remarks, instid,accttypeid);
				 update_authdeauth = jdbctemplate.update(update_authdeauth_qury,new Object[]{authstatus,"9",date.getCurrentDate(),userid,remarks,instid,accttypeid});
			}
			// int update_authdeauth =
			// commondesc.executeTransaction(update_authdeauth_qury,
			// jdbctemplate);
			/*int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);*/
			
			
			
			if (update_authdeauth >= 1) {
				txManager.commit(transact.status);
				addActionMessage("Account type " + statusmsg + " Successfully");

				/************* AUDIT BLOCK **************/
				try {
					String accounttyepdesc = accountActionDao.getAccountTypeDesc(instid, accttypeid, jdbctemplate);
					auditbean.setActmsg("Account type [ " + accounttyepdesc + " ] is " + statusmsg + "");
					
					//added by gowtham_230719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);
					
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("6060");
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
			txManager.rollback(transact.status);
			addActionError(" Error while " + statusmsg + " the Card type " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "required_home";
	}

	public String viewAccounttypeDetails() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {
			String accttypeid = getRequest().getParameter("accounttype");
			System.out.println("accttypeid-----  - - -" + accttypeid);
			List getallaccounttype_qury = accountActionDao.getAccountTypeListByAcctID(instid, jdbctemplate, accttypeid);
			System.out.println("--------getallaccounttype_qury-----  - - -" + getallaccounttype_qury);
			if (!getallaccounttype_qury.isEmpty()) {
				ListIterator itr = getallaccounttype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("ADDEDBY");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("USER_NAME", username);
					itr.remove();
					itr.add(mp);
				}
				setList(getallaccounttype_qury);
				cardbean.setAccounttype(getallaccounttype_qury);
			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while fetching the cardtype " + e);
			trace("Error while fetching the cardtype " + e.getMessage());
		}

		// System.out.println("I am Tired of Watching you i will Love you ");
		return "view_Accounttype_Details";
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

	public List getAuthaccttypeList() {
		return authaccttypeList;
	}

	public void setAuthaccttypeList(List authaccttypeList) {
		this.authaccttypeList = authaccttypeList;
	}

	public String getAccounttypelenval() {
		return accounttypelenval;
	}

	public void setAccounttypelenval(String accounttypelenval) {
		this.accounttypelenval = accounttypelenval;
	}
} // end class
