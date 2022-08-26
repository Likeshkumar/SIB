package com.ifg.Config.padss;
import com.ifg.Bean.ServerValidationBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import connection.Dbcon;
import test.Validation;
import com.ifg.Config.padss.KeyChnageProcessAction;
public class PadssConfigAction extends BaseAction {

	PadssSecurity sec = new PadssSecurity();
	CommonDesc commondesc = new CommonDesc();
	PadssSecurity padss = new PadssSecurity();
	AuditBeans auditbean = new AuditBeans();
	ServerValidationBean bean = new ServerValidationBean();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	KeyChnageProcessAction home = new KeyChnageProcessAction();
	PadssConfigActionDAO dao = new PadssConfigActionDAO();
	KeysEncryptAndDecrypt key = new KeysEncryptAndDecrypt();

	private List keylist;

	JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	/*
	 * public String comInstId(HttpSession session) { String instid = (String)
	 * session.getAttribute("Instname"); return instid; }
	 */

	public String comInstId(HttpSession session2) {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comAdminUserCode(HttpSession session) {
		// HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("SS_USERNAME");
		return instid;
	}

	public String padssConfigHome() {
		trace("padssConfigHome method called....");
		return "padssConfigHome";
	}

	
	public String keyChangeHome()
	{
		trace("method called");
		return "keychangehomepage";
	}
	
	
	public void getdatacheckdigit() throws Exception {

		String comp1value = getRequest().getParameter("comp1value");
		String comp2value = getRequest().getParameter("comp2value");
		String comp3value = getRequest().getParameter("comp3value");
		//trace("comp1value:" + comp1value);
		//trace("comp2value:" + comp2value);
		//trace("comp3value:" + comp3value);
		String DMK_KVC = null;
		try {
			String ClearDMK = sec.getFormattedKey(comp1value, comp2value, comp3value);
			//trace("ClearDMK--->" + ClearDMK);

			DMK_KVC = sec.getCheckDigit(ClearDMK);
			//trace("DMK KCV--->" + DMK_KVC); // For Display & Store in DB

			DMK_KVC = DMK_KVC.substring(0, 6);
		} catch (Exception e) {
			getResponse().getWriter().write("Exception in getting DMK Check Digit");
		}
		getResponse().getWriter().write(DMK_KVC);

	}

	public void getpincheckdigit() throws Exception {

		String comppin1value = getRequest().getParameter("comppin1value");
		String comppin2value = getRequest().getParameter("comppin2value");
		//trace("comppin1value:" + comppin1value);
		//trace("comppin2value:" + comppin2value);
		String DPK_KVC = null;
		try {
			String ClearDPK = sec.getFormattedKey(comppin1value, comppin2value);
			//trace("ClearDPK--->" + ClearDPK);

			DPK_KVC = sec.getCheckDigit(ClearDPK);
			//trace("DPK KCV--->" + DPK_KVC); // For Display & Store in DB
			DPK_KVC = DPK_KVC.substring(0, 6);

		} catch (Exception e) {
			getResponse().getWriter().write("Exception in getting DPK_KVC Check Digit");
		}
		getResponse().getWriter().write(DPK_KVC);

	}

	public String savesecurityKey() 
	{
		trace("savesecurityKey method called.............");
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		IfpTransObj transact = commondesc.myTranObject("SAVESEQKEY", txManager);
		String KEYDESC = getRequest().getParameter("keyname");

		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		String usercode = comAdminUserCode(session);

		String SALT_KEY = "SIB";
		String KEY_LENGTH_CHECK = "32";
		String KEY_LENGTH = "16";
		String comp1value = getRequest().getParameter("comp1value");
		String comp2value = getRequest().getParameter("comp2value");
		String comp3value = getRequest().getParameter("comp3value");

		String comppin1value = getRequest().getParameter("comppin1value");
		String comppin2value = getRequest().getParameter("comppin2value");

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String ClearDMK = null;
		String DMK_KVC = null;
		String EDMK = null;

		/*System.out.println("comp1value"+comp1value);
		System.out.println("comp2value"+comp2value);
		System.out.println("comp3value"+comp3value);
		
		System.out.println("comppin1value"+comppin1value);
		System.out.println("comppin2value"+comppin2value);*/
		
		bean.setKEYDESC(KEYDESC);
		
		try
		{
			check=Validation.charcter(KEYDESC);
			if(!check)
			{
				addFieldError("keyname","KEY NAME CANNOT BE EMPTY AND ALLOW CHAR");
				return padssConfigHome();
			}
			
			checkvalue=Validation.number(comp1value);
			if(checkvalue==0)
			{
				addActionError("DMK Component 1 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return padssConfigHome();
			}
			checkvalue=Validation.number(comp2value);
			if(checkvalue==0)
			{
				addActionError("DMK Component 2 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return padssConfigHome();
			}
			checkvalue=Validation.number(comp3value);
			if(checkvalue==0)
			{
				addActionError("DMK Component 3 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return padssConfigHome();
			}
			
			checkvalue=Validation.number(comppin1value);
			if(checkvalue==0)
			{
				addActionError("DPK Component 1 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return padssConfigHome();
			}
			checkvalue=Validation.number(comppin2value);
			if(checkvalue==0)
			{
				addActionError("DPK Component 2 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return padssConfigHome();
			}
			
					
			/*KEYDESC = getRequest().getParameter("keyname");
			trace("KEYDESC::" + KEYDESC);*/
			SALT_KEY = "SIB";
			ClearDMK = sec.getFormattedKey(comp1value, comp2value, comp3value);
			// trace("ClearDMK--->" + ClearDMK);

			String key1 = ClearDMK.substring(0, 16);

			//System.out.println("key1     " + key1);

			DMK_KVC = sec.getCheckDigit(ClearDMK);
			// trace("DMK KCV--->" + DMK_KVC); // For Display & Store in DB

			// EDMK = sec.getEDMK(ClearDMK);
			// trace("Encrpted DMK--->" + EDMK);// Store in DB

			String DPK_KVC = null;
			String ClearDPK = null;
			String EDPK = null;
			ClearDPK = sec.getFormattedKey(comppin1value, comppin2value);
			// trace("ClearDPK--->" + ClearDPK);

			String key2 = ClearDPK.substring(0, 16);
			//System.out.println("key2     " + key2);

			DPK_KVC = sec.getCheckDigit(ClearDPK);
			// trace("DPK KCV--->" + DPK_KVC); // For Display & Store in DB

			// EDPK = sec.getEDPK(EDMK, ClearDPK);

			// Getting encrypt dpk
			EDPK = sec.encryptDPK(key1, key2);

			// trace("Encrpted DPK--->" + EDPK);// Store in DB

			trace("Getting exist getMaxSecKeyName count.. ");
			int save_key = secKeyNameExist(KEYDESC, jdbctemplate);
			int keyid = 0;
			trace("Got getMaxSecKeyName count .. " + save_key);
			if (save_key > 0) {
				trace("getMaxSecKeyName count greater than zero.. ");
				addActionError("SECURITY KEY With Name '" + KEYDESC + "' Already Exists");
				trace("SECURITY KEY '" + KEYDESC + "' Already Exists");
				return "padssConfigHome";
			} else {
				trace("Getting Max Key ID count .. ");
				keyid = getMaxSecKeyID(jdbctemplate);
				trace("Got Max SEQ keyid count .. " + keyid);
				if (keyid == 0) {
					keyid = 1;
				} else {
					keyid = keyid + 1;
				}
			}

			StringBuilder padsskey = new StringBuilder();
			
			padsskey.append("INSERT INTO PADSSKEY(KEYID, KEYDESC, DMK, DMK_KVC, DPK, DPK_KVC, SALT_KEY, KEY_LENGTH,AUTH_CODE, AUTH_DATE, AUTH_BY, ADDED_BY, ADDED_DATE, REMARKS) ");
			padsskey.append("VALUES ");
			padsskey.append("('" + keyid + "', '" + KEYDESC + "', '" + key1 + "', '" + null + "', '" + null + "', '"
					+ null + "', '" + SALT_KEY + "', '" + KEY_LENGTH + "','0','','','" + usercode + "',sysdate,'') ");
		//	enctrace("padsskey ----->" + padsskey.toString());

			// Writing encryption dpk value into CommonDesc.properties
			//System.out.println("Writing encryption dpk into properties file value--->  " + EDPK);
			writeDPK(EDPK);
			//System.out.println("encryption --->  " + EDPK);

			int insertpadsskey = commondesc.executeTransaction(padsskey.toString(), jdbctemplate);
			

			// int updateacctsubtype = commondesc.executeTransaction("update
			// SEQUENCE_MASTER set PADSSSEQ=PADSSSEQ+1 where
			// INST_ID='"+instid+"'", jdbctemplate);

			//trace("---------" + insertpadsskey);
			if (insertpadsskey == 1) {
				transact.txManager.commit(transact.status);
				addActionMessage(
						"Security Key \"" + KEYDESC + "\" Added Successfully and Waiting For Authorization ....");
				trace("Security Key Added Successfully");

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setUsercode(usercode);
				auditbean.setActmsg("PADSS KEY  ADDED  SUCCESSFULLY BY THE USER " + usercode);
				auditbean.setAuditactcode("01336");

				commondesc.insertAuditTrailPendingCommit("SIB", usercode, auditbean, jdbctemplate, txManager);
				trace("audit logs insertion done");
				return "required_home";
			} else {
				transact.txManager.rollback(transact.status);
				addActionMessage("Security Key Not Added .....");
				trace("Security Key Not Added .....");
			}

		} catch (Exception e) {
			e.printStackTrace();
			transact.txManager.rollback(transact.status);
			addActionError("Security Key Not Added .....");
			trace("Security Key Not Added ....." + e);
		}

		trace("........................savesecurityKey method ended");

		return "padssConfigHome";
	}

	public ServerValidationBean getBean() {
		return bean;
	}

	public void setBean(ServerValidationBean bean) {
		this.bean = bean;
	}

	public String updatekey() 
	{
		trace("updatekey method called.............");
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		IfpTransObj transact = commondesc.myTranObject("SAVESEQKEY", txManager);
		String KEYDESC = getRequest().getParameter("keyname");

		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		String usercode = comAdminUserCode(session);
		String instid = "SIB";
		trace("checking instid11-->" + usercode);
		trace("checking instid22-->" + instid);

		String SALT_KEY = "SIB";
		// String KEY_LENGTH = "32";
		String KEY_LENGTH = "16";
		String comp1value = getRequest().getParameter("comp1value");
		String comp2value = getRequest().getParameter("comp2value");
		String comp3value = getRequest().getParameter("comp3value");

		System.out.println("%%%%%-->"+comp1value);
		
		String comppin1value = getRequest().getParameter("comppin1value");
		String comppin2value = getRequest().getParameter("comppin2value");

		String ClearDMK = null;
		String DMK_KVC = null;
		String EDMK = null;
		
		
		//added by gowtham_220719
				String  ip=(String) session.getAttribute("REMOTE_IP");

				
				bean.setKEYDESC(KEYDESC);
		try 
		{
			// by siva 
			
			check=Validation.charcter(KEYDESC);
			if(!check)
			{
				addActionError("KEY NAME CANNOT BE EMPTY AND ALLOW CHAR");
				return keyChangeHome();
			}
			
			checkvalue=Validation.number(comp1value);
			if(checkvalue==0)
			{
				addActionError("DMK Component 1 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return keyChangeHome();
			}
			checkvalue=Validation.number(comp2value);
			if(checkvalue==0)
			{
				addActionError("DMK Component 2 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return keyChangeHome();
			}
			checkvalue=Validation.number(comp3value);
			if(checkvalue==0)
			{
				addActionError("DMK Component 3 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return keyChangeHome();
			}
			
			checkvalue=Validation.number(comppin1value);
			if(checkvalue==0)
			{
				addActionError("DPK Component 1 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return keyChangeHome();
			}
			checkvalue=Validation.number(comppin2value);
			if(checkvalue==0)
			{
				addActionError("DPK Component 2 CANNOT BE EMPTY AND ALLOW ONLY NUMBER");
				return keyChangeHome();
			}
			
			//KEYDESC = getRequest().getParameter("keyname");
			//trace("KEYDESC::" + KEYDESC);
			SALT_KEY = "SIB";
			ClearDMK = sec.getFormattedKey(comp1value, comp2value, comp3value);
			// trace("ClearDMK--->" + ClearDMK);

			String key1 = ClearDMK.substring(0, 16);

			//System.out.println("key1     " + key1);

			DMK_KVC = sec.getCheckDigit(ClearDMK);
			// trace("DMK KCV--->" + DMK_KVC); // For Display & Store in DB

			// EDMK = sec.getEDMK(ClearDMK);
			// trace("Encrpted DMK--->" + EDMK);// Store in DB

			String DPK_KVC = null;
			String ClearDPK = null;
			String EDPK = null;
			ClearDPK = sec.getFormattedKey(comppin1value, comppin2value);
			// trace("ClearDPK--->" + ClearDPK);

			String key2 = ClearDPK.substring(0, 16);
			//System.out.println("key2     " + key2);

			DPK_KVC = sec.getCheckDigit(ClearDPK);
			// trace("DPK KCV--->" + DPK_KVC); // For Display & Store in DB

			// EDPK = sec.getEDPK(EDMK, ClearDPK);

			// Getting encrypt dpk
			EDPK = sec.encryptDPK(key1, key2);

			// trace("Encrpted DPK--->" + EDPK);// Store in DB

			trace("Getting exist getMaxSecKeyName count.. ");
			int save_key = secKeyNameExist(KEYDESC, jdbctemplate);
			int keyid = 0;
			trace("Got getMaxSecKeyName count .. " + save_key);
			if (save_key > 0) {
				trace("getMaxSecKeyName count greater than zero.. ");
				addActionError("SECURITY KEY With Name '" + KEYDESC + "' Already Exists");
				trace("SECURITY KEY '" + KEYDESC + "' Already Exists");
				return "padssConfigHome";
			} else {
				trace("Getting Max Key ID count .. ");
				keyid = getMaxSecKeyID(jdbctemplate);
				trace("Got Max SEQ keyid count .. " + keyid);
				if (keyid == 0) {
					keyid = 1;
				} else {
					keyid = keyid + 1;
				}
			}

			StringBuilder padsskey = new StringBuilder();
			padsskey.append(
					"INSERT INTO PADSSKEY(KEYID, KEYDESC, DMK, DMK_KVC, DPK, DPK_KVC, SALT_KEY, KEY_LENGTH,AUTH_CODE, AUTH_DATE, AUTH_BY, ADDED_BY, ADDED_DATE, REMARKS) ");
			padsskey.append("VALUES ");
			padsskey.append("('" + keyid + "', '" + KEYDESC + "', '" + key1 + "', '" + null + "', '" + null + "', '"
					+ null + "', '" + SALT_KEY + "', '" + KEY_LENGTH + "','2','','','" + usercode + "',sysdate,'') ");
			//enctrace("padsskey ----->" + padsskey.toString());

			String keyid1 = commondesc.getSecurityKeyid(instid, jdbctemplate);
			//trace("key id-->" + keyid1);
			Properties props = getCommonDescProperty();
			String EDPK1 = props.getProperty("EDPK");
			String update = "UPDATE PADSSKEY SET KEYID='TEMP',DPK='" + EDPK1 + "' WHERE KEYID='" + keyid1 + "'   ";
			int x = jdbctemplate.update(update);

			// Writing encryption dpk value into CommonDesc.properties
			//System.out.println("Writing encryption dpk into properties file value--->  " + EDPK);
			writeDPK(EDPK);
			//System.out.println("encryption --->  " + EDPK);

			int insertpadsskey = commondesc.executeTransaction(padsskey.toString(), jdbctemplate);

			// int updateacctsubtype = commondesc.executeTransaction("update
			// SEQUENCE_MASTER set PADSSSEQ=PADSSSEQ+1 where
			// INST_ID='"+instid+"'", jdbctemplate);

			trace("---------" + insertpadsskey);
			if (insertpadsskey == 1 && x == 1) {
				// if (insertpadsskey == 1) {
				transact.txManager.commit(transact.status);
				addActionMessage(
						"Security Key \"" + KEYDESC + "\" Updated Successfully and Waiting For Authorization ....");
				trace("Security Key Added Successfully");


				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setUsercode(usercode);
				auditbean.setActmsg("PADSS KEY  Updated  SUCCESSFULLY BY THE USER " + usercode);
				auditbean.setAuditactcode("01336");

				commondesc.insertAuditTrailPendingCommit("SIB", usercode, auditbean, jdbctemplate, txManager);
				trace("audit logs insertion done");
				return "required_home";
			} else {
				transact.txManager.rollback(transact.status);
				addActionMessage("Security Key Not Updated .....");
				trace("Security Key Not Updated .....");
			}

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("Security Key Not Updated .....");
			trace("Security Key Not Added ....." + e);
		}

		trace("........................savesecurityKey method ended");

		return "padssConfigHome";
	}

	public String padssConfigAuthorizeHome() {
		trace("*************** padssConfigAuthorizeHome **********");
		enctrace("*************** padssConfigAuthorizeHome  **********");
		HttpSession session = getRequest().getSession();
		String usercode = comAdminUserCode(session);
		try {
			List keydetailslist = dao.getPadssConfigByUsername(usercode, jdbctemplate);
			System.out.println("keylist" + keydetailslist.size());
			if (keydetailslist.isEmpty()) {
				trace("No records found..");
				addActionError(" No records found.. ");
				return "required_home";

			} else {
				setKeylist(keydetailslist);
			}
		} catch (Exception e) {
			addActionError("Exception : While Getting padssConfig Details ");

			trace("Exception : While Getting Key Details " + e.getMessage());
		}
		trace("*************** padssConfigAuthorizeHome Ends **********");
		enctrace("*************** padssConfigAuthorizeHome Ends **********");
		trace("\n\n");
		enctrace("\n\n");
		return "padssConfigAuthorizeHome";
	}

	public String keychangeAuthorizeHome() {
		trace("*************** padssConfigAuthorizeHome **********");
		enctrace("*************** padssConfigAuthorizeHome  **********");
		HttpSession session = getRequest().getSession();
		String usercode = comAdminUserCode(session);
		try {
			List keydetailslist = dao.getPadssConfigByUsername1(usercode, jdbctemplate);
			System.out.println("keylist" + keydetailslist.size());
			if (keydetailslist.isEmpty()) {
				trace("No records found..");
				addActionError(" No records found.. ");
				return "required_home";

			} else {
				setKeylist(keydetailslist);
			}
		} catch (Exception e) {
			addActionError("Exception : While Getting padssConfig Details ");

			trace("Exception : While Getting Key Details " + e.getMessage());
		}
		trace("*************** padssConfigAuthorizeHome Ends **********");
		enctrace("*************** padssConfigAuthorizeHome Ends **********");
		trace("\n\n");
		enctrace("\n\n");
		return "padssConfigAuthorizeHome1";
	}

	public String viewAuthpadssConfig() {
		trace("*************** Viewing viewAuthpadssConfig **********");
		enctrace("*************** Viewing viewAuthpadssConfig **********");
		HttpSession session = getRequest().getSession();
		String keyid = getRequest().getParameter("keyid");
		String keydesc = getRequest().getParameter("keydesc");
		try {
			trace("Got keyid ID .." + keyid);
			List keydetail = dao.getPadssConfigByKEYID(keyid, jdbctemplate);
			setKeylist(keydetail);
			trace("Getting keydetail List .. " + keydetail);
			if (!(keydetail.isEmpty())) {
				setKeylist(keydetail);
			} else {
				addActionError(" No Key details found ..");
				trace(" No Key details found ..");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Exception While Fetching Key Detail " + keydesc + " ");
			trace("Exception While Fetching Key Detaill '" + keydesc + "' :::" + e);
		}
		trace("*************** Viewing viewAuthpadssConfig **********");
		enctrace("*************** Viewing viewAuthpadssConfig **********");
		return "viewAuthpadssConfig";
	}

	public String viewAuthpadssConfig1() {
		trace("*************** Viewing viewAuthpadssConfig **********");
		enctrace("*************** Viewing viewAuthpadssConfig **********");
		HttpSession session = getRequest().getSession();
		String keyid = getRequest().getParameter("keyid");
		String keydesc = getRequest().getParameter("keydesc");
		try {
			trace("Got keyid ID .." + keyid);
			List keydetail = dao.getPadssConfigByKEYID1(keyid, jdbctemplate);
			setKeylist(keydetail);
			trace("Getting keydetail List .. " + keydetail);
			if (!(keydetail.isEmpty())) {
				setKeylist(keydetail);
			} else {
				addActionError(" No Key details found ..");
				trace(" No Key details found ..");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Exception While Fetching Key Detail " + keydesc + " ");
			trace("Exception While Fetching Key Detaill '" + keydesc + "' :::" + e);
		}
		trace("*************** Viewing viewAuthpadssConfig **********");
		enctrace("*************** Viewing viewAuthpadssConfig **********");
		return "viewAuthpadssConfig1";
	}

	public String authorizedeauthorize() {
		trace("************* KEY authorization *****************");
		enctrace("************* KEY authorization *****************");
		IfpTransObj transact1 = commondesc.myTranObject("AUTHKEY", txManager);
		HttpSession session = getRequest().getSession();
		String statusmsg = "";
		try {

			// added by gowtham_220719
			String ip = (String) session.getAttribute("REMOTE_IP");

			String userid = comAdminUserCode(session);
			String keyid = getRequest().getParameter("keyid").trim();
			String auth = getRequest().getParameter("auth");
			int update_authdeauth;
			if (auth.equals("Authorize")) {
				trace("AUTHORIZE...........");
				statusmsg = " Authorized ";
				trace("Updating Authoze status ..");
				update_authdeauth = dao.updateAuthKey(userid, keyid, jdbctemplate);
				trace("Got result .. " + update_authdeauth);
				auditbean.setUsercode(userid);
				auditbean.setActmsg("PADSS KEY  Autorization BY THE USER " + userid);
				auditbean.setAuditactcode("01337");
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);

				commondesc.insertAuditTrailPendingCommit("SIB", userid, auditbean, jdbctemplate, txManager);
				trace("audit logs authorization done");

			} else {
				statusmsg = " De-Authorized ";
				String remarks = getRequest().getParameter("remarks");
				System.out.println(" ---- remarks --- " + remarks);
				update_authdeauth = dao.updateDeAuthKey(userid, remarks, keyid, jdbctemplate);
				auditbean.setUsercode(userid);
				auditbean.setActmsg("PADSS KEY  De Autorization BY THE USER " + userid);
				auditbean.setAuditactcode("01338");
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				
				commondesc.insertAuditTrailPendingCommit("SIB", userid, auditbean, jdbctemplate, txManager);
				trace("audit logs authorization done");

			}
			if (update_authdeauth > 0) {
				txManager.commit(transact1.status);
				addActionMessage("Key " + statusmsg + " Sucessfully..");
				trace("key " + statusmsg + " Sucessfully");
				return "required_home";
			} else {
				txManager.rollback(transact1.status);
				addActionError("could not " + statusmsg + "");
				trace("could not " + statusmsg + "");
				return "required_home";
			}

		} catch (Exception e) {
			txManager.rollback(transact1.status);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Exception while " + statusmsg + " the Key ");
			trace("Error while " + statusmsg + " the Key " + e.getMessage());
		}
		return "required_home";
	}

	public String keyupdateauthorizedeauthorize() {
		trace("************* keyupdateauthorizedeauthorize *****************");
		enctrace("*************keyupdateauthorizedeauthorize*****************");
		IfpTransObj transact1 = commondesc.myTranObject("AUTHKEY", txManager);
		HttpSession session = getRequest().getSession();
		String statusmsg = "";
		String instid = "SIB";
		String key_id = "TEMP";
		String username = "superadmin2"; // temp hard code user name
		
		//added by gowtham_220719
				String  ip=(String) session.getAttribute("REMOTE_IP");
		
		try {
			String userid = comAdminUserCode(session);
			String keyid = getRequest().getParameter("keyid").trim();
			
			trace("new key id-->"+keyid);
			System.out.println("new key id-->"+keyid);
			
			String auth = getRequest().getParameter("auth");
			int update_authdeauth;

			if (auth.equals("Authorize")) {
				trace("AUTHORIZE...........");
				String OLD_DMK = null, OLD_EDPK = null, NEW_EDPK = null, NEW_DMK = null;
				statusmsg = " Authorized ";
				trace("Updating Authoze status ..");
				update_authdeauth = dao.updateAuthKey(userid, keyid, jdbctemplate);
				//trace("Got result .. " + update_authdeauth);

				List secList2 = commondesc.getPADSSDetailByIdd(keyid, jdbctemplate);
				//trace("list22-->" + secList2);
				Iterator secitr = secList2.iterator();
				while (secitr.hasNext()) 
				{
					Map map = (Map) secitr.next();
					NEW_DMK = ((String) map.get("DMK"));
					
					trace("new dmk-->" + NEW_DMK);
				}

				List secList1 = commondesc.getPADSSDetailById1(key_id, jdbctemplate);
				//trace("list11-->" + secList1);
				Iterator secitr1 = secList1.iterator();
				while (secitr1.hasNext()) 
				{
					Map map = (Map) secitr1.next();
					OLD_DMK = ((String) map.get("DMK"));
					
					trace("old dmk-->" + OLD_DMK);
					
					OLD_EDPK = ((String) map.get("DPK"));
					
					trace("old edpk-->" + OLD_EDPK);
				}

				Properties props = getCommonDescProperty();
				NEW_EDPK = props.getProperty("EDPK");
				trace("new edpk-->" + NEW_EDPK);

				String clear_old_dpk = padss.decryptDPK(OLD_DMK, OLD_EDPK);
				
				trace("clear_old_dpk-->" + clear_old_dpk);
				
				String clear_new_dpk = padss.decryptDPK(NEW_DMK, NEW_EDPK);
				
				trace("clear_new_dpk-->" + clear_new_dpk);
				
				trace("instid-->"+instid);
				trace("userid--->"+userid);
				trace("username--->"+username);

				Connection conn = null;
				Dbcon dbcon = new Dbcon();
				conn = dbcon.getDBConnection();
				CallableStatement cstmt = null;
				String SQL = "{call KEY_MIG(?,?,?,?,?)}";
				cstmt = conn.prepareCall(SQL);
				trace("procedure--->call KEY_MIG(?,?,?,?,?)");

				cstmt.setString(1, instid);
				cstmt.setString(2, clear_old_dpk);
				cstmt.setString(3, clear_new_dpk);
				cstmt.setString(4, userid);
				cstmt.setString(5, username);
				cstmt.execute();

				cstmt.close();
				conn.close();

				/*
				 * String result=
				 * "SELECT PERS_SUCC_COUNT,INST_SUCC_COUNT,PROD_SUCC_COUNT,PERS_COUNT,PROD_COUNT,INST_COUNT FROM key_process_mgnt"
				 * ; enctrace("result  ::"+result); List user_result
				 * =jdbctemplate.queryForList(result); trace("user_result  ::"
				 * +user_result); String PERS_SUCCESS_COUNT = null; String
				 * INST_SUCCESS_COUNT=null; String PROD_SUCCESS_COUNT =null;
				 * String pers_count = null; String prod_count=null; String
				 * inst_count=null; Iterator iterator_userresult =
				 * user_result.iterator(); while(iterator_userresult.hasNext())
				 * { Map mapper_userdetails=(Map)iterator_userresult.next();
				 * PERS_SUCCESS_COUNT =
				 * (mapper_userdetails.get("PERS_SUCC_COUNT")).toString();
				 * trace("pers success count --->"+PERS_SUCCESS_COUNT);
				 * INST_SUCCESS_COUNT =
				 * (mapper_userdetails.get("INST_SUCC_COUNT")).toString();
				 * PROD_SUCCESS_COUNT =
				 * (mapper_userdetails.get("PROD_SUCC_COUNT")).toString();
				 * pers_count =
				 * (mapper_userdetails.get("PERS_COUNT")).toString(); prod_count
				 * = (mapper_userdetails.get("PROD_COUNT")).toString();
				 * inst_count =
				 * (mapper_userdetails.get("INST_COUNT")).toString(); }
				 * 
				 * 
				 * if ((pers_count.equalsIgnoreCase(PERS_SUCCESS_COUNT)) &&
				 * (prod_count.equalsIgnoreCase(INST_SUCCESS_COUNT))
				 * &&(inst_count.equalsIgnoreCase(PROD_SUCCESS_COUNT))) { trace(
				 * " Card Migrated Successfully Based on New Key");
				 * addActionError("Card Migrated Successfully Based on New Key"
				 * ); //return "required_home"; } else { trace(
				 * "Exception occured in Key Change Process "); addActionError(
				 * "Exception Occured in Key Change Process"); //return
				 * "required_home"; }
				 */
				
				// added by gowtham_250719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(userid);
				auditbean.setActmsg("PADSS KEY  Autorization BY THE USER " + userid);
				auditbean.setAuditactcode("01337");
				commondesc.insertAuditTrailPendingCommit("SIB", userid, auditbean, jdbctemplate, txManager);
				trace("audit logs authorization done---Authorize");
			}

			else {
				
				
				// added by gowtham_250719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);
				
				statusmsg = " De-Authorized ";
				String remarks = getRequest().getParameter("remarks");
				System.out.println(" ---- remarks --- " + remarks);
				update_authdeauth = dao.updateDeAuthKey(userid, remarks, keyid, jdbctemplate);
				auditbean.setUsercode(userid);
				auditbean.setActmsg("PADSS KEY  De Autorization BY THE USER " + userid);
				auditbean.setAuditactcode("01338");
				commondesc.insertAuditTrailPendingCommit("SIB", userid, auditbean, jdbctemplate, txManager);
				trace("audit logs authorization done---Authorized");
			}

			String update_inst = "UPDATE INSTITUTION SET PADSS_KEY='" + keyid + "' WHERE INST_ID='" + instid + "'";
			enctrace("update to Authorize key .. " + update_inst);
			int update_inst_padss = jdbctemplate.update(update_inst);
			trace("update_inst_padss-->" + update_inst_padss);

			String update_inst11 = "DELETE FROM PADSSKEY WHERE KEYID='" + key_id + "'";
			enctrace("update to Authorize key .. " + update_inst11);
			int update_inst_padss11 = jdbctemplate.update(update_inst11);
			trace("update_inst_padss11-->" + update_inst_padss11);

			if (update_authdeauth > 0 && update_inst_padss == 1 && update_inst_padss11 == 1) {
				txManager.commit(transact1.status);
				addActionMessage("New Key " + statusmsg + " Sucessfully..");
				trace("key " + statusmsg + " Sucessfully");
				return "required_home";
			} else {
				txManager.rollback(transact1.status);
				addActionError("could not " + statusmsg + "");
				trace("could not " + statusmsg + "");
				return "required_home";
			}

		} catch (Exception e) {
			txManager.rollback(transact1.status);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Exception while " + statusmsg + " the Key ");
			trace("Error while " + statusmsg + " the Key " + e.getMessage());
		}
		return "required_home";
	}

	public String padssConfigViewHome() {
		trace("padssConfigViewHome called..");

		List keydetails = dao.getPadssConfig(jdbctemplate);
		setKeylist(keydetails);

		return "padssConfigViewHome";
	}

	public int secKeyNameExist(String keyname, JdbcTemplate jdbctemplate) {
		int x = -1;
		String secKeyNameExist = "select count(*) from PADSSKEY where KEYDESC='" + keyname.toUpperCase() + "' ";
		enctrace("secKeyNameExist ..." + secKeyNameExist);
		x = jdbctemplate.queryForInt(secKeyNameExist);
		return x;
	}

	public int getMaxSecKeyID(JdbcTemplate jdbctemplate) {
		int keyid;
		String max_id_query = "select max(to_number(KEYID)) from PADSSKEY";
		enctrace("Getting getMaxSecKeyID .." + max_id_query);
		keyid = jdbctemplate.queryForInt(max_id_query);
		return keyid;
	}

	public int sequencePADSSsequence(String inst_id, JdbcTemplate jdbctemplate) {
		int x = -1;
		String sequencePADSSsequence = "SELECT PADSSSEQ FROM SEQUENCE_MASTER WHERE INST_ID='" + inst_id + "'";
		enctrace("sequencePADSSsequence::" + sequencePADSSsequence);
		x = jdbctemplate.queryForInt(sequencePADSSsequence);
		return x;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public List getKeylist() {
		return keylist;
	}

	public void setKeylist(List keylist) {
		this.keylist = keylist;
	}

}
