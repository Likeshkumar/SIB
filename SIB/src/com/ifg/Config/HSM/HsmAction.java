package com.ifg.Config.HSM;
import com.ifp.util.IPvalidation;
import test.Validation;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifg.Config.HSM.HsmActionDao;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.ifg.Bean.ServerValidationBean;
public class HsmAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	AuditBeans auditbean = new AuditBeans();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	ServerValidationBean bean = new ServerValidationBean();
	public ServerValidationBean getBean() {
		return bean;
	}
	Validation validation = new Validation();
	public void setBean(ServerValidationBean bean) {
		this.bean = bean;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	CommonDesc commondesc = new CommonDesc();
	IPvalidation ip = new IPvalidation();
	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	private JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	AuditBeans beans = new AuditBeans();
	HsmActionDao hsmdao = new HsmActionDao();

	public AuditBeans getBeans() {
		return beans;
	}

	public void setBeans(AuditBeans beans) {
		this.beans = beans;
	}

	public String comAdminUserCode(HttpSession session) {
		String adminUser = (String) session.getAttribute("SS_USERNAME");
		return adminUser;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String addHSM() {
		trace("*************** Adding HSM Begins **********");
		enctrace("*************** Adding HSM Begins **********");
		HttpSession session = getRequest().getSession();
		try {
			String adminmkrchkr = commondesc.getAdminMkrChkrStatus(jdbctemplate);
			System.out.println("adminmkrchkr -- " + adminmkrchkr);
			if (adminmkrchkr != null) {
				String mkrchkr_status;
				if (adminmkrchkr.equals("Y")) {
					mkrchkr_status = "Y";
				} else {
					mkrchkr_status = "N";
				}
				beans.setMkrchkrstatus(mkrchkr_status);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " Configure Admin Maker Checker status");
				return "required_home";
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Exception : While configuring Admin Maker Checker status");
			return "required_home";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "addHsm";
	}

	public String saveadd() 
	{
		trace("*************** Saving HSM Begins **********");
		enctrace("*************** Saving HSM Begins **********");
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		String hsmname = getRequest().getParameter("hsmname");
		String hsmip = getRequest().getParameter("hsmip");
		String hsmport = getRequest().getParameter("hsmport");
		String hsmprotocol = getRequest().getParameter("hsmprotocol");
		String hsmheadertype = getRequest().getParameter("hsmheadertype");
		String hsmhedlen = getRequest().getParameter("hsmhedlen");
		String hsmtimeout = getRequest().getParameter("hsmtimeout");
		String hsmconinterval = getRequest().getParameter("hsmconinterval");
		String headlength = getRequest().getParameter("headlength");
		String hsmtype = getRequest().getParameter("hsmtype");
		String mkrchkr = getRequest().getParameter("mkrchkr");
		String hsmstatus = getRequest().getParameter("hsmstatus");
		trace("mkrchkr   ---- " + mkrchkr);
		
		
		bean.setHsmname(hsmname);
		bean.setHsmip(hsmip);
		bean.setHsmport(hsmport);
		bean.setHsmprotocol(hsmprotocol);
		bean.setHsmheadertype(hsmheadertype);
		bean.setHsmhedlen(hsmhedlen);
		bean.setHsmtimeout(hsmtimeout);
		bean.setHsmconinterval(hsmconinterval);
		bean.setHeadlength(headlength);
		bean.setHsmtype(hsmtype);
		
		
		check=Validation.charcter(hsmname);
		if(!check)
		{
			addFieldError("hsmname"," HSM CANNOT BE EMPTY AND ALLOW CHAR");
			return addHSM();
		}
		
		if(bean.getHsmip()=="")
		{
			addFieldError("hsmip","HSM IP ADDRESS CANNOT BE EMPTY");
			return addHSM();
		}
		if (!IPvalidation.isValidInet4Address(bean.getHsmip())) 
		{
			addFieldError("hsmip","HSM IP ADDRESS IS INVALID");
			return addHSM();
		}
		
		checkvalue=Validation.number(hsmport);
		if(checkvalue==0)
		{
			addFieldError("hsmport","HSM PORT CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
			return addHSM();
		}
		if(bean.getHsmprotocol().equals("-1"))
		{
			addFieldError("hsmprotocol","PLEASE SELECT HSM PROTOCOL");
			return "addHsm";
		}	
		if(bean.getHsmtype().equals("-1"))
		{
			addFieldError("hsmtype","PLEASE SELECT HSM TYPE");
			return "addHsm";
		}
		
		if(bean.getHsmheadertype().equals("-1"))
		{
			addFieldError("hsmheadertype","PLEASE SELECT HEADER TYPE");
			return "addHsm";
		}
		if(bean.getHsmhedlen().equals("-1"))
		{
			addFieldError("hsmhedlen","PLEASE SELECT HSM HEADER LENGTH");
			return "addHsm";
		}
		
		
		checkvalue=Validation.number(hsmtimeout);
		if(checkvalue==0)
		{
			addFieldError("hsmtimeout","HSM TIMEOUT CANNOT BE EMPTY");
			return "addHsm";
		}
		
		checkvalue=Validation.number(hsmconinterval);
		if(checkvalue==0)
		{
			addFieldError("hsmconinterval","HSM CONNECTION INTERVAL CANNOT BE EMPTY");
			return "addHsm";
		}
		
		checkvalue=Validation.number(headlength);
		if(checkvalue==0)
		{
			addFieldError("headlength","HEADER LENGTH CANNOT BE EMPTY");
			return "addHsm";
		}
		
		
		//added_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");//------------------
		trace("mkrchkr   ---- " + ip);
		
		String usercode = comAdminUserCode(session);
		IfpTransObj transact1 = commondesc.myTranObject("SAVEHSM", txManager);
		try {
			trace("Getting exist HsmName count.. ");
			int save_hsm = hsmdao.HsmNameExist(hsmname, jdbctemplate);
			trace("Got hsmName count .. " + save_hsm);
			if (save_hsm > 0) {
				trace("Hsm Name count greater than zero.. ");
				addActionError("HSM With Name '" + hsmname + "' Already Exists");
				trace("HSM With Name '" + hsmname + "' Already Exists");
				return "addHsm";
			} else {
				trace("Getting Max Hsm ID count .. ");
				int hsmid = hsmdao.getMaxHsmID(jdbctemplate);
				trace("Got Max Hsm ID count .. " + hsmid);
				if (hsmid == 0) {
					hsmid = 1;
				} else {
					hsmid = hsmid + 1;
				}
				String authcode, errmsg;
				int hsm_main_result = 1, hsm_temp_result;
				if (mkrchkr.equals("N")) {
					authcode = "1";
					errmsg = "";
				} else {
					authcode = "0";
					errmsg = "Waiting for authorization";
				}
				if (mkrchkr.equals("N")) {
					trace("Inserting hsm details in Main .. ");
					hsm_main_result = hsmdao.insertHsmDetailsMain(hsmname, hsmprotocol, hsmtype, hsmip, hsmport,
							headlength, hsmheadertype, hsmhedlen, hsmtimeout, hsmconinterval, hsmstatus, hsmid,
							jdbctemplate, authcode, usercode);
					trace("Got Inserting main table hsm details result.. " + hsm_main_result);
				}
				trace("Inserting hsm details in Temp .. ");
				hsm_temp_result = hsmdao.insertHsmDetailsTemp(hsmname, hsmprotocol, hsmtype, hsmip, hsmport, headlength,
						hsmheadertype, hsmhedlen, hsmtimeout, hsmconinterval, hsmstatus, hsmid, jdbctemplate, authcode,
						usercode);
				trace("Got Inserting temp table hsm details result.. " + hsm_temp_result);
				if (hsm_main_result <= 0 || hsm_temp_result <= 0) {
					txManager.rollback(transact1.status);
					addActionError(" Exception : While Inserting HSM " + hsmname);
					trace("Exception : While Inserting HSM " + hsmname);
					return "required_home";
				}

				//added_220919
				trace("ip address======>  "+ip);//-----------------
				auditbean.setIpAdress(ip);//----------------------------
				
				auditbean.setActmsg("hsm added sucessfully by the user "+usercode+ " and Waiting for authorization...");
				auditbean.setUsercode(usercode);
				auditbean.setAuditactcode("01330");
				
				commondesc.insertAuditTrailPendingCommit("SIB", usercode, auditbean, jdbctemplate, txManager);
                
				trace("Audit logs inserted successfully");
				
				txManager.commit(transact1.status);
				addActionMessage(" HSM '" + hsmname + "' Added Sucessfully." + errmsg + ".. ");
				trace("HSM '" + hsmname + "' Added Sucessfully");
				trace("\n\n");
				enctrace("\n\n");
				return "required_home";
			}
		} catch (Exception e) {
			addActionMessage("Exception While Adding HSM.. ");
			trace("Error While Adding HSM " + e.getMessage());
			txManager.rollback(transact1.status);
			trace("\n\n");
			enctrace("\n\n");
			return "addHsm";
		}
	}

	public String display() {
		trace("*************** Viewing HSM Begins **********");
		enctrace("*************** Viewing HSM Begins **********");
		HttpSession session = getRequest().getSession();
		try {
			String adminmkrchkr = commondesc.getAdminMkrChkrStatus(jdbctemplate);
			System.out.println("adminmkrchkr -- " + adminmkrchkr);
			if (adminmkrchkr != null) {
				String mkrchkr_status;
				if (adminmkrchkr.equals("Y")) {
					mkrchkr_status = "Y";
				} else {
					mkrchkr_status = "N";
				}
				session.setAttribute("makercheckerstatus", mkrchkr_status);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " Configure Admin Maker Checker status");
				return "required_home";
			}

			List hsmdetailslist = hsmdao.gethsmDetailsTemp(jdbctemplate);
			System.out.println("hsmlist" + hsmdetailslist.size());
			if (hsmdetailslist.isEmpty()) {
				trace("No HSM Exists. Please Add It. ");
				session.setAttribute("curmsg", "No Hsm details exists. Please Add It. ");
				session.setAttribute("curerr", "E");
			} else {
				beans.setHsmlist(hsmdetailslist);
			}
		} catch (Exception e) {
			session.setAttribute("curmsg", "Exception : While Getting HSM Details ");
			session.setAttribute("curerr", "E");
			trace("Exception : While Getting HSM Details " + e.getMessage());
		}
		trace("*************** Viewing HSM Ends **********");
		enctrace("*************** Viewing HSM Ends **********");
		trace("\n\n");
		enctrace("\n\n");
		return "displayHSM";
	}

	public String view() {
		trace("*************** Viewing HSM Begins **********");
		enctrace("*************** Viewing HSM Begins **********");
		HttpSession session = getRequest().getSession();
		String hsm_id = getRequest().getParameter("hsmid");
		try {
			trace("Got hsm ID .." + hsm_id);
			
			if(hsm_id.equalsIgnoreCase("-1"))
			{
				trace("inside comming");
				addActionError("SELECT HSM");
				return display();
			}
			
			List hsmdetail = hsmdao.gethsmDetailsTempByHsmID(hsm_id, jdbctemplate);
			trace("Getting Hsm Details List .. " + hsmdetail);
			if (!(hsmdetail.isEmpty())) {
				beans.setHsmdetails(hsmdetail);
				beans.setHsm_idno(hsm_id);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curerr", " No Hsm details found ..");
				trace(" No Hsm details found ..");
				return display();
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curerr", "Exception While Fetching HSM Detail " + hsm_id + " ");
			trace("Exception While Fetching HSM Detail '" + hsm_id + "' " + e.getMessage());
		}
		trace("*************** Viewing HSM Ends **********");
		enctrace("*************** Viewing HSM Ends **********");
		return "viewHsm";
	}

	public String edit() {
		trace("*************** Edit HSM Begins **********");
		enctrace("*************** Edit HSM Begins **********");
		HttpSession session = getRequest().getSession();
		try {
			String hsm_id = getRequest().getParameter("hsmid").trim();
			trace("Getting hsm id-->" + hsm_id);
			List hsmdetail = hsmdao.gethsmDetailsTempEditByHsmID(hsm_id, jdbctemplate);
			if (!hsmdetail.isEmpty()) {
				beans.setHsmdetails(hsmdetail);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curerr", "No records found..");
				trace("No records found..");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curerr", "Exception while fetching HSM Details..");
			trace("Exception while fetching HSM Details.." + e.getMessage());
		}
		trace("*************** Edit HSM ends **********");
		enctrace("*************** Edit HSM ends **********");
		return "editHsm";
	}
	// Pavithra ,Should not delete HSM Details bcoz,we have a Enable,Disable
	// option

	public String update() {
		trace("*************** Updating HSM Begins **********");
		enctrace("*************** Updating HSM Begins **********");
		IfpTransObj transact1 = commondesc.myTranObject("EDITHSM", txManager);
		HttpSession session = getRequest().getSession();
		String mkrchkr = (String) session.getAttribute("makercheckerstatus");
		System.out.println(" ------ makercheckerstatus -----  " + mkrchkr);
		trace(" ------ makercheckerstatus -----  " + mkrchkr);
		
		boolean check;
		int checkvalue;
		
		try 
		{
			String HSM_ID = getRequest().getParameter("HSM_ID");
			String HSMNAME = getRequest().getParameter("HSMNAME");
			String HSMPROTOCOL = getRequest().getParameter("HSMPROTOCOL");
			String HSMTYPE = getRequest().getParameter("HSMTYPE");
			String HSMADDRESS = getRequest().getParameter("HSMADDRESS");
			String HSMPORT = getRequest().getParameter("HSMPORT");
			String HEADERLEN = getRequest().getParameter("HEADERLEN");
			String HEADERTYPE = getRequest().getParameter("HEADERTYPE");
			String HSMHEADERLEN = getRequest().getParameter("HSMHEADERLEN");
			String HSMTIMEOUT = getRequest().getParameter("HSMTIMEOUT");
			String CONNECTIONINTERVAL = getRequest().getParameter("CONNECTIONINTERVAL");
			String HSMSTATUS = getRequest().getParameter("HSMSTATUS");
			String usercode = comAdminUserCode(session);
			
			
			bean.setHsmname(HSMNAME);
			bean.setHsmip(HSMADDRESS);
			bean.setHsmport(HSMPORT);
			bean.setHsmprotocol(HSMPROTOCOL);
			bean.setHsmheadertype(HEADERTYPE);
			bean.setHsmhedlen(HSMHEADERLEN);
			bean.setHsmtimeout(HSMTIMEOUT);
			bean.setHsmconinterval(CONNECTIONINTERVAL);
			bean.setHeadlength(HEADERLEN);
			bean.setHsmtype(HSMTYPE);
			
			
			/*if(bean.getHsmname()=="")
			{
				addFieldError("hsmname"," HSM NAME CANNOT BE EMPTY");
				return addHSM();
			}*/
			check=Validation.charcter(HSMNAME);
			if(!check)
			{
				addFieldError("hsmname"," HSM CANNOT BE EMPTY AND ALLOW CHAR");
				return addHSM();
			}
			
			
			if(bean.getHsmip()=="")
			{
				addFieldError("hsmip","HSM IP ADDRESS CANNOT BE EMPTY");
				return addHSM();
			}
			if (!IPvalidation.isValidInet4Address(bean.getHsmip())) 
			{
				addFieldError("hsmip","HSM IP ADDRESS IS INVALID");
				return addHSM();
			}
		
			
			
			checkvalue=Validation.number(HSMPORT);
			if(checkvalue==0)
			{
				addFieldError("hsmport","HSM PORT CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return addHSM();
			}
			
			if(bean.getHsmprotocol().equals("-1"))
			{
				addFieldError("hsmprotocol","PLEASE SELECT HSM PROTOCOL");
				return "addHsm";
			}	
			if(bean.getHsmtype().equals("-1"))
			{
				addFieldError("hsmtype","PLEASE SELECT HSM TYPE");
				return "addHsm";
			}
			
			if(bean.getHsmheadertype().equals("-1"))
			{
				addFieldError("hsmheadertype","PLEASE SELECT HEADER TYPE");
				return "addHsm";
			}
			if(bean.getHsmhedlen().equals("-1"))
			{
				addFieldError("hsmhedlen","PLEASE SELECT HSM HEADER LENGTH");
				return "addHsm";
			}
			
			
			
			checkvalue=Validation.number(HSMTIMEOUT);
			if(checkvalue==0)
			{
				addFieldError("hsmtimeout","HSM TIMEOUT CANNOT BE EMPTY");
				return "addHsm";
			}
			
			checkvalue=Validation.number(CONNECTIONINTERVAL);
			if(checkvalue==0)
			{
				addFieldError("hsmconinterval","HSM CONNECTION INTERVAL CANNOT BE EMPTY");
				return "addHsm";
			}
			
			checkvalue=Validation.number(HEADERLEN);
			if(checkvalue==0)
			{
				addFieldError("headlength","HEADER LENGTH CANNOT BE EMPTY");
				return "addHsm";
			}
			
			
			//Added by Gowtham_200719
			String  ip=(String) session.getAttribute("REMOTE_IP");//------------------
			
			trace("HSMNAME   " + HSMNAME + "   HSMPROTOCOL   " + HSMPROTOCOL + "  HSMTYPE   " + HSMTYPE
					+ "   HSMADDRESS   " + HSMADDRESS + "   HSMPORT   " + HSMPORT + "   HEADERLEN   " + HEADERLEN);
			String authcode, errmsg;
			if (mkrchkr.equals("N")) {
				authcode = "1";
				errmsg = "";
			} else {
				authcode = "0";
				errmsg = "Waiting for authorization..";
			}
			int result_main = 1, result_temp;
			if (mkrchkr.equals("N")) {
				trace("updating the Hsm details in main..");
				result_main = hsmdao.updateHsmMain(HSMNAME, HSMPROTOCOL, HSMTYPE, HSMADDRESS, HSMPORT, HEADERLEN,
						HEADERTYPE, HSMHEADERLEN, HSMTIMEOUT, CONNECTIONINTERVAL, HSMSTATUS, HSM_ID, jdbctemplate,
						authcode, usercode);
				trace("Got updated result.." + result_main);
			}
			trace("updating the Hsm details in temp..");
			result_temp = hsmdao.updateHsmTemp(HSMNAME, HSMPROTOCOL, HSMTYPE, HSMADDRESS, HSMPORT, HEADERLEN,
					HEADERTYPE, HSMHEADERLEN, HSMTIMEOUT, CONNECTIONINTERVAL, HSMSTATUS, HSM_ID, jdbctemplate, authcode,
					usercode);
			trace("Got updated result.." + result_temp);
			if (result_main <= 0 || result_temp <= 0) {
				addActionError("Exception While Editing The HSM");
				trace("Exception While Editing The HSM");
				txManager.rollback(transact1.status);
				return "required_home";
			}
			 
			//Added by Gowtham_200719
			trace("ip address======>  "+ip);
			auditbean.setIpAdress(ip);
			
			auditbean.setUsercode(usercode);
			auditbean.setActmsg("HSM UPDATED SUCCESSFULLY BY THE USER "+usercode);
			auditbean.setAuditactcode("01331");
			
			commondesc.insertAuditTrailPendingCommit("SIBL", usercode, auditbean, jdbctemplate, txManager);
            
			txManager.commit(transact1.status);
			addActionMessage("HSM Edited Sucessfully.." + errmsg + "");
			trace("HSM Edited Sucessfully..");
		} catch (Exception e) {
			txManager.rollback(transact1.status);
			addActionError("Exception While Editing The HSM..");
			trace("Exception While Editing The HSM.." + e.getMessage());
		}
		trace("*************** Updating HSM Ends **********");
		enctrace("*************** Updating HSM Ends**********");
		return display();
	}

	public String authdeauth() {
		trace("*************** Auth Deauth HSM Begins **********");
		enctrace("*************** Auth Deauth HSM Begins  **********");
		HttpSession session = getRequest().getSession();
		String usercode = comAdminUserCode(session);
		try {
			List hsmdetailslist = hsmdao.gethsmDetailsTempByUsername(usercode, jdbctemplate);
			System.out.println("hsmlist" + hsmdetailslist.size());
			if (hsmdetailslist.isEmpty()) {
				trace("No records found..");
				session.setAttribute("curmsg", " No records found.. ");
				session.setAttribute("curerr", "E");
			} else {
				beans.setHsmlist(hsmdetailslist);
			}
		} catch (Exception e) {
			session.setAttribute("curmsg", "Exception : While Getting HSM Details ");
			session.setAttribute("curerr", "E");
			trace("Exception : While Getting HSM Details " + e.getMessage());
		}
		trace("*************** Viewing HSM Ends **********");
		enctrace("*************** Viewing HSM Ends **********");
		trace("\n\n");
		enctrace("\n\n");
		return "authdeauthHSM";
	}

	public String viewAuth() {
		trace("*************** Viewing HSM Begins **********");
		enctrace("*************** Viewing HSM Begins **********");
		HttpSession session = getRequest().getSession();
		String hsm_id = getRequest().getParameter("hsmid");
		try 
		{
			trace("Got hsm ID .." + hsm_id);		
			if(hsm_id.equalsIgnoreCase("-1"))
			{
				trace("inside comming");
				addActionError("SELECT HSM");
				return authdeauth();
			}
			
			List hsmdetail = hsmdao.gethsmDetailsTempByHsmID(hsm_id, jdbctemplate);
			trace("Getting Hsm Details List .. " + hsmdetail);
			if (!(hsmdetail.isEmpty())) {
				beans.setHsmdetails(hsmdetail);
				beans.setHsm_idno(hsm_id);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curerr", " No Hsm details found ..");
				trace(" No Hsm details found ..");
				return display();
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curerr", "Exception While Fetching HSM Detail " + hsm_id + " ");
			trace("Exception While Fetching HSM Detail '" + hsm_id + "' " + e.getMessage());
		}
		trace("*************** Viewing HSM Ends **********");
		enctrace("*************** Viewing HSM Ends **********");
		return "viewAuth";
	}

	public String authorizedeauthorize() {
		trace("************* HSM authorization *****************");
		enctrace("************* HSm authorization *****************");
		IfpTransObj transact1 = commondesc.myTranObject("EDITHSM", txManager);
		HttpSession session = getRequest().getSession();
		String statusmsg = "";
		try {
			
			
			//added by gowtham_220719
			String  ip=(String) session.getAttribute("REMOTE_IP");
			
			String userid = comAdminUserCode(session);
			String hsmid = getRequest().getParameter("hsmid").trim();
			String auth = getRequest().getParameter("auth");
			int move_main_table_result = 1, delete_main_table_result = 1, update_authdeauth;
			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				statusmsg = " Authorized ";
				trace("Updating Authoze status ..");
				update_authdeauth = hsmdao.updateAuthhsm(userid, hsmid, jdbctemplate);
				trace("Got result .. " + update_authdeauth);

				/*
				 * trace("Getting count in main table .. "); int maintablestatus
				 * = hsmdao.getmaintablestatus(hsmid,jdbctemplate); trace(
				 * "Got Count in main menu .. "+maintablestatus);
				 * if(maintablestatus>=1){ trace("Delete from main table ..");
				 * delete_main_table_result =
				 * hsmdao.deleteHsmMaintable(hsmid,jdbctemplate); trace(
				 * "Result after Delete from main table.. "
				 * +delete_main_table_result); } trace(
				 * "Inserting in main menu .. "); move_main_table_result=
				 * hsmdao.insertHsmMaintable(hsmid,jdbctemplate); trace(
				 * "Got result while Inserting in main menu .. "
				 * +move_main_table_result);
				 */
			} else {
				statusmsg = " De-Authorized ";
				String remarks = getRequest().getParameter("remarks");
				// System.out.println(" ---- remarks --- "+remarks);
				update_authdeauth = hsmdao.updateDeAuthHsm(userid, remarks, hsmid, jdbctemplate);

			}
			
			//added by gowtham_220719
			trace("ip address======>  "+ip);
			auditbean.setIpAdress(ip);
			 
			auditbean.setUsercode(userid);
			auditbean.setActmsg("HSM deauthorize  SUCCESSFULLY BY THE USER "+userid);
			auditbean.setAuditactcode("01332");
			
			commondesc.insertAuditTrailPendingCommit("UTBSL", userid, auditbean, jdbctemplate, txManager);
			// if(update_authdeauth <=0 || delete_main_table_result<=0 ||
			// move_main_table_result<=0){
			if (update_authdeauth <= 0) {
				txManager.rollback(transact1.status);
				addActionError("could not " + statusmsg + "");
				trace("could not " + statusmsg + "");
				return "required_home";
			}
			txManager.commit(transact1.status);
			addActionMessage("Hsm " + statusmsg + " Sucessfully..");
			trace("Hsm " + statusmsg + " Sucessfully");
		} catch (Exception e) {
			txManager.rollback(transact1.status);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Exception while " + statusmsg + " the Hsm ");
			trace("Error while " + statusmsg + " the Hsm " + e.getMessage());
		}
		return "required_home";
	}

}
