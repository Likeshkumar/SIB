package com.ifg.usermgt;

import it.sauronsoftware.base64.Base64;

/**
 * 
 * SRNP0003
 * @author CGSPL
 */

import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifg.usermgt.userManagementAction;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.ContextUpdater;
import com.ifp.util.IfpTransObj;
import com.ifp.util.PasswordHashing;
import com.ifp.beans.AuditBeans;

public class ChangePasswordAction extends BaseAction {

	userManagementAction usermanage_class = new userManagementAction();

	PasswordHashing pswd_hash_class = new PasswordHashing();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	CommonDesc commondesc = new CommonDesc();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	// added by gowtham220719
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

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

	LogoutAction logout = new LogoutAction();

	public String changepassword() 
	{
		trace("change password home method called");
		return "userchangepassword";
	}

	public String changepasswordTest() {
		HttpSession session = getRequest().getSession();
		
		// by siva 210819
				HttpSession ses = getRequest().getSession();
				String sessioncsrftoken = (String) ses.getAttribute("token");
				String jspcsrftoken = getRequest().getParameter("token");
				
				System.out.println("session token---> "+sessioncsrftoken);
				System.out.println("jspcsrftoken ---> "+jspcsrftoken);
				
				if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) 
				{
					/*ses.setAttribute("message", "CSRF Token Mismatch");*/
					addActionError("CSRF Token Mismatch Please contact Admin");
					return changepassword();
				}
				// by siva 210819
				
				
		IfpTransObj transact = commondesc.myTranObject("changepassword", txManager);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		String usertext = "";

		String InstId = "BIC";

		try {

			LoginAction loginActionClass = new LoginAction();
			String oldpassword;
			String newpassword, PSWREPEATCOUNT;
			oldpassword = (getRequest().getParameter("oldpassword"));
			System.out.println("old password-->"+oldpassword);
			newpassword = (getRequest().getParameter("newpassword"));
			System.out.println("new password-->"+newpassword);
			String user_name = (String) session.getAttribute("SS_USERNAME");
			System.out.println("user_name==> " + user_name);
			String userid = (String) session.getAttribute("USERID");
			String db_password = null, db_salt = null;
			String usertype = (String) session.getAttribute("USERTYPE");
			trace("User type is : " + usertype);

			String inst_id = (String) session.getAttribute("Instname");
			inst_id = inst_id.trim();
			
			StringBuilder res = new StringBuilder();
			StringBuilder res2 = new StringBuilder();
			String sLowerCase = "", sUpperCase = "", sNumbers = "", sSpecial = "", sFirstChar = "", sLastChar = "",
					sTotalCount = "", sFirstCharString = "", sLastCharString = "";

			List getPolicyList = this.getChangedPolicyDetails(inst_id);
			System.out.println("getPolicyList-" + getPolicyList);

			if (!(getPolicyList.isEmpty())) {
				Iterator itr = getPolicyList.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					sLowerCase = (String) map.get("LOWERCASE");
					sUpperCase = (String) map.get("UPPERCASE");
					sNumbers = (String) map.get("NUMBERS");
					sSpecial = (String) map.get("SPECIAL");
					sFirstChar = (String) map.get("FIRSTCHAR");
					sLastChar = (String) map.get("LASTCHAR");
					sFirstCharString = (String) map.get("FIRSTCHAR_STRING");
					sLastCharString = (String) map.get("LASTCHAR_STRING");
					sTotalCount = (String) map.get("TOTALCOUNT");
				}

				
				System.out.println("sLowerCase-->"+sLowerCase);
				System.out.println("sUpperCase-->"+sUpperCase);
				System.out.println("sNumbers-->"+sNumbers);
				System.out.println("sSpecial-->"+sSpecial);
				System.out.println("sFirstChar-->"+sFirstChar);
				System.out.println("sLastChar-->"+sLastChar);
				System.out.println("sFirstCharString-->"+sFirstCharString);
				System.out.println("sLastCharString-->"+sLastCharString);
				System.out.println("sTotalCount-->"+sTotalCount);
				
				char FIRST = newpassword.charAt(0);
				char LAST = newpassword.charAt(newpassword.length() - 1);

				int sTOTAL = newpassword.length();
				char sFIRST;
				sFIRST = Character.isDigit(FIRST) == true ? 'N'
						: (!Character.isDigit(FIRST) && !Character.isLetter(FIRST)) == true ? 'S'
								: Character.isUpperCase(FIRST) == true ? 'U'
										: Character.isLowerCase(FIRST) == true ? 'L' : ' ';
				char sLAST;
				sLAST = Character.isDigit(LAST) == true ? 'N'
						: (!Character.isDigit(LAST) && !Character.isLetter(LAST)) == true ? 'S'
								: Character.isUpperCase(LAST) == true ? 'U'
										: Character.isLowerCase(LAST) == true ? 'L' : ' ';

				int sDIGIT = 0;
				int sUPPER = 0;
				int sLOWER = 0;
				int sSPECIAL = 0;
				for (int i = 0; i < newpassword.length(); i++) {
					trace("inside for loop :" + newpassword.length());
					// System.out.println("Invalid :"+ch.charAt(i));
					if (Character.isDigit(newpassword.charAt(i))) {
						sDIGIT++;
					}
					if (Character.isUpperCase(newpassword.charAt(i))) {
						sUPPER++;
					}
					if (Character.isLowerCase(newpassword.charAt(i))) {
						sLOWER++;
					}
					if ((!Character.isDigit(newpassword.charAt(i)) && !Character.isLetter(newpassword.charAt(i)))) {
						sSPECIAL++;
					}
				}

				System.out.println(sLowerCase + sLOWER + sUpperCase + sUPPER + sNumbers + sDIGIT + sSpecial + sSPECIAL
						+ sFirstChar + sFIRST + sLastChar + sLAST + sTotalCount + sTOTAL);

				System.out.println(Integer.parseInt(sLowerCase) >= sLOWER);

				if ((Integer.parseInt(sLowerCase) <= sLOWER) && (Integer.parseInt(sUpperCase) <= sUPPER)
						&& (Integer.parseInt(sNumbers) <= sDIGIT) && (Integer.parseInt(sSpecial) <= sSPECIAL)
						&& (sFirstChar.equals(String.valueOf(sFIRST)) || sFirstChar.equals("-1"))
						&& (sLastChar.equals(String.valueOf(sLAST)) || sLastChar.equals("-1"))
						&& (Integer.parseInt(sTotalCount) <= sTOTAL)) 
				{
					trace("inside for if con :" + sLowerCase + sLOWER + sUpperCase + sUPPER + sNumbers + sDIGIT
							+ sSpecial + sSPECIAL + sFirstChar + sFIRST + sLastChar + sLAST + sTotalCount + sTOTAL);
		
					
					
				if (usertype.equals("SUPERADMIN")) {
				trace("SUPER ADMIN===>" + oldpassword + "newpassword===> " + newpassword);
				String password_match = checkOldnewpasswords(oldpassword, newpassword);
				if (password_match.equals("PASSED")) {
					System.out.println("PASSWORD CAN BE CHANGED");
					
					/*String admindet_qury = "select USERPASSWORD from ADMIN_USER where USERNAME='" + user_name + "'";
					System.out.println("admindet_qury===> " + admindet_qury);
					List admininfo = jdbctemplate.queryForList(admindet_qury);*/
					
					////BY GOWTHAM
					String admindet_qury = "select USERPASSWORD from ADMIN_USER where USERNAME=? ";
					System.out.println("admindet_qury===> " + admindet_qury);
					List admininfo = jdbctemplate.queryForList(admindet_qury,new Object[]{user_name});
					
					System.out.println("admininfo===> " + admininfo);
					Iterator admin_itr = admininfo.iterator();
					while (admin_itr.hasNext()) {
						Map admin_map = (Map) admin_itr.next();
						db_password = ((String) admin_map.get("USERPASSWORD"));
					}
					String oldandnewpass = checkOldnewpasswords(db_password, newpassword);
					if (oldandnewpass.equals("PASSED")) {
						System.out.println("PASSWORD CAN BE CHANGED GOING TO UPDATE");
						String newpass_encode = getBase64value(newpassword);
						System.out.println("NEW ENCODED VALUE IS ===> " + newpass_encode);
						
/*String admin_pass_update = "UPDATE ADMIN_USER SET USERPASSWORD='" + newpass_encode+ "' WHERE  USERNAME='" + user_name + "'";
System.out.println("admin_pass_update===> " + admin_pass_update);
int update_status = jdbctemplate.update(admin_pass_update);*/
						
String admin_pass_update = "UPDATE ADMIN_USER SET USERPASSWORD=? WHERE  USERNAME=? ";
System.out.println("admin_pass_update===> " + admin_pass_update);
int update_status = jdbctemplate.update(admin_pass_update,new Object[]{newpass_encode,user_name,});
						
						System.out.println("update_status===> " + update_status);
						if (update_status == 1) {
							txManager.commit(transact.status);
							System.out.println("TXN UPDATED");
							session.setAttribute("curerr", "S");
							session.setAttribute("curmsg",
									"Password has been changed successfully. Logout and login again");
							
							/************* AUDIT BLOCK **************/
							try {

								auditbean.setActmsg("Password has been changed successfully");
								auditbean.setUsercode(user_name);
								auditbean.setAuditactcode("3001");

								// added by gowtham_220719
								trace("ip address======>  " + ip);
								auditbean.setIpAdress(ip);

								commondesc.insertAuditTrail(InstId,user_name,auditbean, jdbctemplate, txManager);
							} catch (Exception audite) {
								trace("Exception in auditran : " + audite.getMessage());
							}
							/************* AUDIT BLOCK **************/
							
							return "required_home";

						} else {
							txManager.rollback(transact.status);
							session.setAttribute("curerr", "E");
							session.setAttribute("curmsg", "Password Not Changed");
							return changepassword();
						}

					} else if (password_match.equals("FAILED")) {
						System.out.println(" Old Password and New Password! Should Not Be Same ");
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", " Old Password and New Password! Should Not Be Same ");
						return changepassword();
					} else {
						System.out.println(" Password Not Changed ");
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", " Password Not Changed ");
						return changepassword();
					}
				} else if (password_match.equals("FAILED")) {
					System.out.println(" Old Password and New Password@ Should Not Be Same ");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", " Old Password and New Password@ Should Not Be Same ");
					return changepassword();
				} else {
					System.out.println(" Password Not Changed ");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", " Password Not Changed ");
					return changepassword();
				}

			}

			else 
			{
				//String inst_id = (String) session.getAttribute("Instname");
				System.out.println("INSTITUTION ADMIN");
				/*
				 * String query_getuserdatils=
				 * "select * FROM USER_DETAILS where username ='"+user_name+
				 * "' and INSTID='"+inst_id+"'";
				 */
	/*String query_getuserdatils = "SELECT USERID,USERNAME,USERPASSWORD,PWD_1,PWD_2,PWD_3,PWD_4,PROFILEID,FIRSTNAME,"
	+ "LASTNAME,EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,CREATIONDATE,LOGINSTATUS,"
	+ "FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,"
	+ "USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,(SELECT SALT_1 FROM USER_DETAILS_SALT "
	+ "WHERE USERNAME='"+ user_name + "' and INSTID='" + inst_id+ "') as SALT_1,(SELECT SALT_2 FROM USER_DETAILS_SALT"
	+ " WHERE USERNAME='" + user_name+ "' and INSTID='" + inst_id+ "') as SALT_2,(SELECT SALT_3 FROM USER_DETAILS_SALT "
	+ "WHERE USERNAME='" + user_name+ "' and INSTID='" + inst_id+ "') as SALT_3,(SELECT SALT_4 FROM USER_DETAILS_SALT"
	+ " WHERE USERNAME='" + user_name+ "' and INSTID='" + inst_id	+ "') as SALT_4,FORCEPSWEXP,CHANGPASS_DATE,"
	+ "(SELECT SALT_KEY FROM USER_DETAILS_SALT WHERE USERNAME='"+ user_name + "' and INSTID='" + inst_id+ "')"
	+ " as SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,UNBLOCK_AUTHBY,"
	+ "UNBLOCK_AUTHDATE,PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,DELETED_BY,DELETED_DATE,"
	+ "DELETED_FLAG,CBS_USERNAME FROM USER_DETAILS WHERE USERNAME='"+ user_name + "' and INSTID='" + inst_id + "'";
	List user_result = jdbctemplate.queryForList(query_getuserdatils);*/
				
				String query_getuserdatils = "SELECT USERID,USERNAME,USERPASSWORD,PWD_1,PWD_2,PWD_3,PWD_4,PROFILEID,FIRSTNAME,"
						+ "LASTNAME,EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,CREATIONDATE,LOGINSTATUS,"
						+ "FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,"
						+ "USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,(SELECT SALT_1 FROM USER_DETAILS_SALT "
						+ "WHERE USERNAME=? and INSTID=?) as SALT_1,(SELECT SALT_2 FROM USER_DETAILS_SALT"
						+ " WHERE USERNAME=? and INSTID=? ) as SALT_2,(SELECT SALT_3 FROM USER_DETAILS_SALT "
						+ "WHERE USERNAME=? and INSTID=? ) as SALT_3,(SELECT SALT_4 FROM USER_DETAILS_SALT"
						+ " WHERE USERNAME=? and INSTID=? ) as SALT_4,FORCEPSWEXP,CHANGPASS_DATE,"
						+ "(SELECT SALT_KEY FROM USER_DETAILS_SALT WHERE USERNAME=? and INSTID=? )"
						+ " as SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,UNBLOCK_AUTHBY,"
						+ "UNBLOCK_AUTHDATE,PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,DELETED_BY,DELETED_DATE,"
						+ "DELETED_FLAG,CBS_USERNAME FROM USER_DETAILS WHERE USERNAME=? and INSTID=? ";
				enctrace("query_getuserdatils-->"+user_name+inst_id);
				enctrace("query_getuserdatils-->"+query_getuserdatils);
						List user_result = jdbctemplate.queryForList(query_getuserdatils,new Object[]{
						user_name,inst_id,	user_name,inst_id,	user_name,inst_id,	
						user_name,inst_id,	user_name,inst_id,user_name,inst_id,});
				
				System.out.println("password change function  ::" + user_result);
				Iterator iterator_userresult = user_result.iterator();
				while (iterator_userresult.hasNext()) {
					Map mapper_userdetails = (Map) iterator_userresult.next();
					db_password = ((String) mapper_userdetails.get("USERPASSWORD"));
					db_salt = ((String) mapper_userdetails.get("SALT_KEY"));
					PSWREPEATCOUNT = ((String) mapper_userdetails.get("PSWREPEATCOUNT"));
					System.out.println("userpassword-->"+db_password);
					System.out.println("user salt-->"+db_salt);
					System.out.println("PSWREPEATCOUNT-->"+PSWREPEATCOUNT);
				}
				
				
				boolean pswdcheck_result = loginActionClass.checkPassword(db_password, db_salt, oldpassword, inst_id);
				System.out.println("pswdcheck_result====>  " + pswdcheck_result);
				if (pswdcheck_result) {

					String password_compare = checkUserpassword(db_password, db_salt, newpassword, inst_id, userid,loginActionClass, jdbctemplate);
					System.out.println("password_compare===> " + password_compare);
					
					if (password_compare.equals("NO")) 
					{
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", " Password change Failed ");
						return changepassword();
					}
					if (password_compare.equals("FALSE")) {
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "New Password Cannot Be Same As Your Previous 4 Password");
						return changepassword();
					}
					if (password_compare.equals("UPDATED")) 
					{
						session.setAttribute("curerr", "S");
						session.setAttribute("curmsg"," Password has been changed successfully. Logout and Login again....");
						txManager.commit(transact.status);
						System.out.println("Commiting TXN");

						
						/************* AUDIT BLOCK **************/
						try {

							auditbean.setActmsg("Password updated and changed successfully");
							auditbean.setUsercode(user_name);
							auditbean.setAuditactcode("3001");

							// added by gowtham_220719
							trace("ip address======>  " + ip);
							auditbean.setIpAdress(ip);

							commondesc.insertAuditTrail(InstId,user_name,auditbean, jdbctemplate, txManager);
						} catch (Exception audite) {
							trace("Exception in auditran : " + audite.getMessage());
						}
						/************* AUDIT BLOCK **************/				
						return "required_home";
					}

					
					if (password_compare.equals("ROLLBACK")) 
					{
						txManager.rollback(transact.status);
						System.out.println("ROLL BACK TXN");
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "Password Not Changed ");
						return changepassword();
					}

				} 
				else 
				{
					System.out.println("OLD PASSWORD IS WRONG");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "Entered Old password is wrong");
				}
			}
		}	
				else {
					trace("inside else part :" + sTotalCount);
					String result = "";
					result = "Your Password Length Should be " + sTotalCount + " Digit <br/> ";
				
					result = result + "Your Password Should Contain : <br/> Lower Case - " + sLowerCase
							+ " , Upper Case - " + sUpperCase + ", Numbers - " + sNumbers + ", Special Character - "
							+ sSpecial;
					int i = 0;
					if (!sFirstChar.equals("-1")) 
					{
						result = result + "<br/> First Letter Should be : " + sFirstCharString;
					}
					if (!sLastChar.equals("-1")) 
					{
						result = result + "<br/> Last Letter Should be : " + sLastCharString;
					}
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", result);
					trace("resssss---" + result.toString());
					getResponse().getWriter().write(res.toString());
					return changepassword();
				}
			}
		}
			catch (Exception e) 
		{
			e.printStackTrace();
			txManager.rollback(transact.status);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Unable to Reset the password");
			trace("Exception ...could not change the password..." + e.getMessage());
			e.printStackTrace();
		}
		return changepassword();
	}

	public String checkOldnewpasswords(String oldpassword, String newpassword) {
		String result_val = "NO";
		byte[] oldpass_encoded = Base64.encode(oldpassword.trim().getBytes());
		byte[] newpass_encoded = Base64.encode(newpassword.trim().getBytes());
		String oldpass = new String(oldpass_encoded);
		String newpass = new String(newpass_encoded);
		System.out.println("BEFORE IF===> " + oldpass.equals(newpass));
		if (oldpass.equals(newpass)) {
			result_val = "FAILED";
		} else {
			result_val = "PASSED";
		}
		return result_val;
	}

	public String getBase64value(String newpassword) {
		String basevale = "NO";
		byte[] new_encoded = Base64.encode(newpassword.trim().getBytes());
		basevale = new String(new_encoded);
		return basevale;
	}

	public String checkUserpassword(String db_password, String db_salt, String newpassword, String inst_id,
			String userid, LoginAction loginActionClass, JdbcTemplate jdbctemplate) throws Exception {
		String ORG_PWD = null, ORG_SALT = null, PWD_REPEAT_COUNT = null, PWD_CHANGED_COUNT = null, PWD_STATUS = null,
				PWD_1 = null, PWD_2 = null, PWD_3 = null, PWD_4 = null, SALT_1 = null, SALT_2 = null, SALT_3 = null,
				SALT_4 = null;
		Iterator iterator_userresult;
		String return_val = "NO";
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		// Salt generation 64 bits long
		byte[] bSalt = new byte[8];
		random.nextBytes(bSalt);
		// Digest computation
		byte[] bDigest = pswd_hash_class.getHash(pswd_hash_class.ITERATION_NUMBER, newpassword, bSalt);
		String hashed_password = pswd_hash_class.byteToBase64(bDigest);
		String sSalt = pswd_hash_class.byteToBase64(bSalt);

		/*String query_getuserdatils = "SELECT USERID,PWD_REPEAT_COUNT,PWD_CHANGED_COUNT,PWD_STATUS,USERNAME,USERPASSWORD,"
		+ "PROFILEID,FIRSTNAME,LASTNAME,EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,"
		+ "EXPIRYDATE,CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,"
		+ "CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,"
		+ "CHANGPASS_DATE,(SELECT SALT_KEY FROM USER_DETAILS_SALT WHERE USERID='"+ userid + "' and INSTID='" + inst_id+ "')"
		+ " as SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,"
		+ "UNBLOCK_AUTHBY,UNBLOCK_AUTHDATE,PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,"
		+ "DELETED_BY,DELETED_DATE,DELETED_FLAG,CBS_USERNAME,PWD_1,PWD_2,PWD_3,PWD_4,(SELECT SALT_1 FROM "
		+ "USER_DETAILS_SALT WHERE USERID='"+ userid + "' and INSTID='" + inst_id+ "') as SALT_1,(SELECT SALT_2 FROM "
		+ "USER_DETAILS_SALT WHERE USERID='" + userid + "' and INSTID='"+ inst_id + "') as SALT_2,(SELECT SALT_3 FROM "
		+ "USER_DETAILS_SALT WHERE USERID='" + userid+ "' and INSTID='" + inst_id + "') as SALT_3,(SELECT SALT_4 FROM "
		+ "USER_DETAILS_SALT WHERE USERID='"+ userid + "' and INSTID='" + inst_id + "') as SALT_4 FROM USER_DETAILS "
		+ "WHERE USERID='" + userid+ "' and INSTID='" + inst_id + "'";
		enctrace("query_getuserdatils: " + query_getuserdatils);
		List user_result = jdbctemplate.queryForList(query_getuserdatils);*/
		
		///by gowtham
		String query_getuserdatils = "SELECT USERID,PWD_REPEAT_COUNT,PWD_CHANGED_COUNT,PWD_STATUS,USERNAME,USERPASSWORD,"
				+ "PROFILEID,FIRSTNAME,LASTNAME,EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,"
				+ "EXPIRYDATE,CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,"
				+ "CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,"
				+ "CHANGPASS_DATE,(SELECT SALT_KEY FROM USER_DETAILS_SALT WHERE USERID=? and INSTID=? )"
				+ " as SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,"
				+ "UNBLOCK_AUTHBY,UNBLOCK_AUTHDATE,PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,"
				+ "DELETED_BY,DELETED_DATE,DELETED_FLAG,CBS_USERNAME,PWD_1,PWD_2,PWD_3,PWD_4,(SELECT SALT_1 FROM "
				+ "USER_DETAILS_SALT WHERE USERID=? and INSTID=?) as SALT_1,(SELECT SALT_2 FROM "
				+ "USER_DETAILS_SALT WHERE USERID=? and INSTID=? ) as SALT_2,(SELECT SALT_3 FROM "
				+ "USER_DETAILS_SALT WHERE USERID=? and INSTID=?) as SALT_3,(SELECT SALT_4 FROM "
				+ "USER_DETAILS_SALT WHERE USERID=? and INSTID=?) as SALT_4 FROM USER_DETAILS "
				+ "WHERE USERID=?  and INSTID=? ";
				enctrace("query_getuserdatils: " + query_getuserdatils);
				List user_result = jdbctemplate.queryForList(query_getuserdatils,new Object[]{
						userid,inst_id,userid,inst_id,userid,inst_id,
						userid,inst_id,userid,inst_id,userid,inst_id,});
		
		trace("Password requst ...got ::" + user_result);

		iterator_userresult = user_result.iterator();
		while (iterator_userresult.hasNext()) {
			Map mapper_userdetails = (Map) iterator_userresult.next();
			ORG_PWD = ((String) mapper_userdetails.get("USERPASSWORD"));
			ORG_SALT = ((String) mapper_userdetails.get("SALT_KEY"));
			PWD_REPEAT_COUNT = ((String) mapper_userdetails.get("PWD_REPEAT_COUNT"));
			trace("PWD_REPEAT_COUNT-->" + PWD_REPEAT_COUNT);
			PWD_CHANGED_COUNT = ((String) mapper_userdetails.get("PWD_CHANGED_COUNT"));
			PWD_STATUS = ((String) mapper_userdetails.get("PWD_STATUS"));
			PWD_1 = ((String) mapper_userdetails.get("PWD_1"));
			PWD_2 = ((String) mapper_userdetails.get("PWD_2"));
			PWD_3 = ((String) mapper_userdetails.get("PWD_3"));
			PWD_4 = ((String) mapper_userdetails.get("PWD_4"));
			SALT_1 = ((String) mapper_userdetails.get("SALT_1"));
			SALT_2 = ((String) mapper_userdetails.get("SALT_2"));
			SALT_3 = ((String) mapper_userdetails.get("SALT_3"));
			SALT_4 = ((String) mapper_userdetails.get("SALT_4"));
		}
		trace("trace checking here-->" + SALT_1);
		if (("1".equalsIgnoreCase(PWD_REPEAT_COUNT))) {
			boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword, inst_id);
			if ((chck_newpaswd_oldpswd)) {
				System.out.println("New Password Cannot Be Same As Your Previous Password");
				return_val = "FALSE";
			}
			if (return_val != "FALSE") {
				String condition1 = null;
				String condition2 = null;
				if ("PWD_1".equalsIgnoreCase(PWD_STATUS)) {
					condition1 = "PWD_1= '" + hashed_password + "',PWD_STATUS='PWD_1' ";
					condition2 = "SALT_1='" + sSalt + "' ";
				}
				/*
				 * if("PWD_2".equalsIgnoreCase(PWD_STATUS)) { condition1=
				 * "PWD_3= '"+hashed_password+"',PWD_STATUS='PWD_3' ";
				 * condition2="SALT_3='"+sSalt+"' "; }
				 * if("PWD_3".equalsIgnoreCase(PWD_STATUS)) { condition1=
				 * "PWD_4= '"+hashed_password+"',PWD_STATUS='PWD_4' ";
				 * condition2="SALT_4='"+sSalt+"' "; }
				 * if("PWD_4".equalsIgnoreCase(PWD_STATUS)) { condition1=
				 * "PWD_1= '"+hashed_password+"',PWD_STATUS='PWD_1' ";
				 * condition2="SALT_1='"+sSalt+"' "; }
				 */
				int pwd_change_count = Integer.parseInt(PWD_CHANGED_COUNT);
				
				/*String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= '" + hashed_password + "'," + condition1+ ","
				+ "PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0'"
				+ " WHERE  USERID='" + userid+ "' and INSTID='" + inst_id + "'";
				enctrace(" query for change password " + update_query);
				int update_res = jdbctemplate.update(update_query);*/
				
				
				///by gowtham
				String update_query = "UPDATE USER_DETAILS SET USERPASSWORD=? ," + condition1+ ","
				+ "PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT=?,FIRSTTIME =?,LOGINSTATUS =? "
				+ " WHERE  USERID=? and INSTID=?";
				enctrace(" query for change password " + update_query);
				int update_res = jdbctemplate.update(update_query,new Object[]{hashed_password,"0","0","0",userid,inst_id});
				
				trace("Updating new password . main..got :" + update_res);

				/*update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY='" + sSalt + "'," + condition2
				+ ",RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='" + userid + "' and "
				+ "INSTID='"+ inst_id + "'";
				enctrace(" query for change password " + update_query);
				update_res = jdbctemplate.update(update_query);*/
				
				///by gowtham
				String update_query1 = "UPDATE USER_DETAILS_SALT SET SALT_KEY=? ," + condition2
						+ ",RETRYCOUNT=?,FIRSTTIME =?,LOGINSTATUS = ? WHERE  USERID=? and "
						+ "INSTID=? ";
						enctrace(" query for change password " + update_query1);
						int update_res1 = jdbctemplate.update(update_query1,new Object[]{sSalt,"0","0","0",userid,inst_id});
				
				trace("Updating new password ...temp..got :" + update_res1);

				System.out.println("UPDATED RESULT===> " + update_res1);
				if (update_res == 1) {
					return_val = "UPDATED";
				} else {
					return_val = "ROLLBACK";
				}
			}
		}

		if ("2".equalsIgnoreCase(PWD_REPEAT_COUNT)) {
			trace("2 times password check");
			if ((PWD_1 != null && SALT_1 != null)) {
				trace("pwd1");
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword, inst_id);
				trace("password check result-->" + chck_newpaswd_oldpswd);
				if ((chck_newpaswd_oldpswd)) {
					trace("pwd1");
					System.out.println("New Password Cannot Be Same As Your Previous Passwords");
					return_val = "FALSE";
				}
			}

			if ((PWD_2 != null && SALT_2 != null)) {
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_2, SALT_2, newpassword, inst_id);
				trace("password check result-->" + chck_newpaswd_oldpswd);
				if ((chck_newpaswd_oldpswd)) {
					trace("pwd2");
					System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
					return_val = "FALSE";
				}
			}

			if (return_val != "FALSE") {
				String condition1 = null;
				String condition2 = null;
				if ("PWD_1".equalsIgnoreCase(PWD_STATUS)) {
					condition1 = "PWD_2= '" + hashed_password + "',PWD_STATUS='PWD_2' ";
					condition2 = "SALT_2='" + sSalt + "' ";
				}
				if ("PWD_2".equalsIgnoreCase(PWD_STATUS)) {
					condition1 = "PWD_1= '" + hashed_password + "',PWD_STATUS='PWD_1' ";
					condition2 = "SALT_1='" + sSalt + "' ";
				}
				/*
				 * if("PWD_3".equalsIgnoreCase(PWD_STATUS)) { condition1=
				 * "PWD_4= '"+hashed_password+"',PWD_STATUS='PWD_4' ";
				 * condition2="SALT_4='"+sSalt+"' "; }
				 * if("PWD_4".equalsIgnoreCase(PWD_STATUS)) { condition1=
				 * "PWD_1= '"+hashed_password+"',PWD_STATUS='PWD_1' ";
				 * condition2="SALT_1='"+sSalt+"' "; }
				 */
				int pwd_change_count = Integer.parseInt(PWD_CHANGED_COUNT);
				
				/*String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= '" + hashed_password + "'," + condition1
				+ ",PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' "
				+ "WHERE  USERID='" + userid+ "' and INSTID='" + inst_id + "'";
				enctrace(" query for change password " + update_query);
				int update_res = jdbctemplate.update(update_query);*/
				
				///by gowtham
				String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= ? ," + condition1
				+ ",PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT=? ,FIRSTTIME =? ,LOGINSTATUS =?  "
				+ "WHERE  USERID=? and INSTID=? ";
				enctrace(" query for change password " + update_query);
				int update_res = jdbctemplate.update(update_query,new Object[]{hashed_password,"0","0","0",userid,inst_id});
				
				trace("Updating new password . main..got :" + update_res);

				/*update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY='" + sSalt + "'," + condition2
				+ ",RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='" + userid + "' and INSTID='"
				+ inst_id + "'";
				enctrace(" query for change password " + update_query);
				update_res = jdbctemplate.update(update_query);*/
				
				///byg owtham
			String 	update_query1 = "UPDATE USER_DETAILS_SALT SET SALT_KEY=? ," + condition2
						+ ",RETRYCOUNT=? ,FIRSTTIME =? ,LOGINSTATUS =? WHERE  USERID=? and INSTID=? ";
						enctrace(" query for change password " + update_query1);
						int update_res1 = jdbctemplate.update(update_query,new Object[]{sSalt,"0","0","0",userid,inst_id});
				
				trace("Updating new password ...temp..got :" + update_res1);

				System.out.println("UPDATED RESULT===> " + update_res1);
				if (update_res == 1) {
					return_val = "UPDATED";
				} else {
					return_val = "ROLLBACK";
				}
			}
		}

		if ("3".equalsIgnoreCase(PWD_REPEAT_COUNT)) {
			if ((PWD_3 != null && SALT_3 != null))

			{
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_3, SALT_3, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd)) {
					System.out.println("New Password Cannot Be Same As Your Previous Passwords");
					return_val = "FALSE";
				}
			}

			if ((PWD_2 != null && SALT_2 != null))

			{
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_2, SALT_2, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd)) {
					System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
					return_val = "FALSE";
				}
			}

			if ((PWD_1 != null && SALT_1 != null))

			{
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd)) {
					System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
					return_val = "FALSE";
				}
			}

		/*	if (return_val != "FALSE") {*/
				String condition1 = null;
				String condition2 = null;
				if ("PWD_1".equalsIgnoreCase(PWD_STATUS)) {
					condition1 = "PWD_2= '" + hashed_password + "',PWD_STATUS='PWD_2' ";
					condition2 = "SALT_2='" + sSalt + "' ";
				}
				if ("PWD_2".equalsIgnoreCase(PWD_STATUS)) {
					condition1 = "PWD_3= '" + hashed_password + "',PWD_STATUS='PWD_3' ";
					condition2 = "SALT_3='" + sSalt + "' ";
				}
				if ("PWD_3".equalsIgnoreCase(PWD_STATUS)) {
					condition1 = "PWD_1= '" + hashed_password + "',PWD_STATUS='PWD_1' ";
					condition2 = "SALT_1='" + sSalt + "' ";
				}
				/*
				 * if("PWD_4".equalsIgnoreCase(PWD_STATUS)) { condition1=
				 * "PWD_1= '"+hashed_password+"',PWD_STATUS='PWD_1' ";
				 * condition2="SALT_1='"+sSalt+"' "; }
				 */
				int pwd_change_count = Integer.parseInt(PWD_CHANGED_COUNT);
				
				System.out.println("change password is ----> "+condition1);
				
		/*String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= '" + hashed_password + "'," + condition1
		+ ",PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' "
		+ "WHERE  USERID='" + userid	+ "' and INSTID='" + inst_id + "'";
		enctrace(" query for change password " + update_query);
		int update_res = jdbctemplate.update(update_query);*/
				
				
	///by gowtham
		String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= ? ," + condition1
		+ ",PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT=?,FIRSTTIME =?,LOGINSTATUS = ? "
		+ "WHERE  USERID=? and INSTID=? ";
		enctrace(" query for change password " + update_query);
		int update_res = jdbctemplate.update(update_query,new Object[]{hashed_password,"0","0","0",userid,inst_id});
		
				trace("Updating new password . main..got :" + update_res);

		/*update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY='" + sSalt + "'," + condition2
		+ ",RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='" + userid + "' and INSTID='"+ inst_id + "'";
		enctrace(" query for change password " + update_query);
		update_res = jdbctemplate.update(update_query);*/
				
		///by gowtham
		String update_query1= "UPDATE USER_DETAILS_SALT SET SALT_KEY=? ," + condition2
		+ ",RETRYCOUNT=? ,FIRSTTIME =?,LOGINSTATUS = ? WHERE  USERID=? and INSTID=? ";
		enctrace(" query for change password " + update_query1);
		int update_res1 = jdbctemplate.update(update_query1,new Object[]{sSalt,"0","0","0",userid,inst_id});
		
				trace("Updating new password ...temp..got :" + update_res1);

				System.out.println("UPDATED RESULT===> " + update_res1);
				if (update_res == 1) {
					return_val = "UPDATED";
				} else {
					return_val = "ROLLBACK";
				}
			}
	/*	}*/

		if ("4".equalsIgnoreCase(PWD_REPEAT_COUNT)) {
			trace("inside 4 th password repeat count");
			if ((PWD_4 != null && SALT_4 != null)) {
				trace("inside PWD_4");
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_4, SALT_4, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd)) {
					System.out.println("New Password Cannot Be Same As Your Previous Passwords");
					return_val = "FALSE";
				}
			}

			if ((PWD_3 != null && SALT_3 != null)) {
				trace("inside PWD_3");
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_3, SALT_3, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd)) {
					System.out.println("New Password Cannot Be Same As Your Previous Passwords");
					return_val = "FALSE";
				}
			}

			if ((PWD_2 != null && SALT_2 != null)) {
				trace("inside PWD_2");
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_2, SALT_2, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd)) {
					System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
					return_val = "FALSE";
				}
			}
			if ((PWD_1 != null && SALT_1 != null)) {
				trace("inside PWD_1");
				boolean chck_newpaswd_oldpswd = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd)) {
					System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
					return_val = "FALSE";
				}
			}

			if (return_val != "FALSE") {
				String condition1 = null;
				String condition2 = null;
				if ("PWD_1".equalsIgnoreCase(PWD_STATUS)) {
					trace("PWD_1");
					condition1 = "  PWD_2= '" + hashed_password + "',PWD_STATUS='PWD_2'  ";
					condition2 = "SALT_2='" + sSalt + "' ";
				}
				if ("PWD_2".equalsIgnoreCase(PWD_STATUS)) {
					trace("PWD_2");
					condition1 = " PWD_3= '" + hashed_password + "',PWD_STATUS='PWD_3' ";
					condition2 = "SALT_3='" + sSalt + "' ";
				}
				if ("PWD_3".equalsIgnoreCase(PWD_STATUS)) {
					trace("PWD_3");
					condition1 = " PWD_4= '" + hashed_password + "',PWD_STATUS='PWD_4' ";
					condition2 = "SALT_4='" + sSalt + "' ";
				}
				if ("PWD_4".equalsIgnoreCase(PWD_STATUS)) {
					trace("PWD_4");
					condition1 = "PWD_1= '" + hashed_password + "' ,PWD_STATUS='PWD_1' ";
					condition2 = "SALT_1='" + sSalt + "' ";
				}
				int pwd_change_count = Integer.parseInt(PWD_CHANGED_COUNT);
				trace("pwd_change_count-->" + pwd_change_count);
				
			/*String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= '" + hashed_password + "'," + condition1
			+ ",PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0'"
			+ " WHERE  USERID='" + userid+ "' and INSTID='" + inst_id + "'";
				enctrace(" query for change password " + update_query);
				int update_res = jdbctemplate.update(update_query);*/
				
				
				///by gowtham
				String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= ? ," + condition1
						+ ",PWD_CHANGED_COUNT='" + pwd_change_count+ "'+1, RETRYCOUNT=?,FIRSTTIME =?,LOGINSTATUS =? "
						+ " WHERE  USERID=? and INSTID=?";
							enctrace(" query for change password " + update_query);
							int update_res = jdbctemplate.update(update_query,new Object[]{hashed_password,"0","0","0",userid,inst_id});
				
				trace("Updating new password . main..got :" + update_res);

				/*update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY='" + sSalt + "'," + condition2
				+ ",RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='" + userid + "' and INSTID='"+ inst_id + "'";
				enctrace(" query for change password " + update_query);
				update_res = jdbctemplate.update(update_query);*/
				
				///byg owtham
				String update_query1 = "UPDATE USER_DETAILS_SALT SET SALT_KEY=? ," + condition2
						+ ",RETRYCOUNT=? ,FIRSTTIME =?,LOGINSTATUS = ? WHERE  USERID=? and INSTID=? ";
						enctrace(" query for change password " + update_query1);
						int update_res1= jdbctemplate.update(update_query1,new Object[]{sSalt,"0","0","0",userid,inst_id});
				
				trace("Updating new password ...temp..got :" + update_res1);

				System.out.println("UPDATED RESULT===> " + update_res1);
				if (update_res == 1) {
					return_val = "UPDATED";
				} else {
					return_val = "ROLLBACK";
				}
			}
		}

		trace("final return value comming is-->" + return_val);
		return return_val;
	}

	private String userpasswordchangeresult;
	private char resultstatus;

	public char getResultstatus() {
		return resultstatus;
	}

	public void setResultstatus(char resultstatus) {
		this.resultstatus = resultstatus;
	}

	public String getUserpasswordchangeresult() {
		return userpasswordchangeresult;
	}

	public void setUserpasswordchangeresult(String userpasswordchangeresult) {
		this.userpasswordchangeresult = userpasswordchangeresult;
	}

	public String changepsswordAction() {

		HttpSession session = getRequest().getSession();
		try {
			trace("comInstId()===> " + comInstId());
			trace("comUserId()===> " + comUserId());
			trace("comBranchId()==> " + comBranchId());
			trace("comuserType() ==> " + comuserType());
			trace("comUsername() ==> " + comUsername());
			String username = (String) session.getAttribute("SS_USERNAME");
			trace("USER NAME IS _---< " + username);
			if (session != null) {
				ContextUpdater conup = new ContextUpdater();
				conup.removeMapvalue(session);
				trace("session not expired");
				Enumeration e = session.getAttributeNames();
				while (e.hasMoreElements()) {
					session.removeAttribute((String) e.nextElement());
				}
				session.invalidate();
			}

		} catch (Exception e) {
			trace("Exception : ERROR " + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "login";

	}
	
	private List getChangedPolicyDetails(String instid) throws Exception {
		List x = null;
		StringBuilder s = new StringBuilder();
		
		/*s.append("select ID,INST_ID, LOWERCASE, UPPERCASE,NUMBERS, SPECIAL, ");
		s.append("FIRSTCHAR, DECODE(FIRSTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')FIRSTCHAR_STRING,  ");
		s.append("LASTCHAR, DECODE(LASTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')LASTCHAR_STRING, ");
		s.append("TOTALCOUNT, CREATED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, AUTH_STATUS , REASON from PASSWORDPOLICY WHERE AUTH_STATUS = '1' AND ROWNUM=1 ");
		x = jdbctemplate.queryForList(s.toString());*/
		
		///by gowtham
		s.append("select ID,INST_ID, LOWERCASE, UPPERCASE,NUMBERS, SPECIAL, ");
		s.append("FIRSTCHAR, DECODE(FIRSTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')FIRSTCHAR_STRING,  ");
		s.append("LASTCHAR, DECODE(LASTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')LASTCHAR_STRING, ");
		s.append("TOTALCOUNT, CREATED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, AUTH_STATUS , REASON from PASSWORDPOLICY WHERE AUTH_STATUS = ? AND ROWNUM=? ");
		x = jdbctemplate.queryForList(s.toString(),new Object[]{"1","1"});
		
		enctrace("getChangedPolicyDetails:" + s.toString());
		return x;
	}

}
