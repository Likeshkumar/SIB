package com.ifg.passwordpolicy;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import test.Date;

public class PasswordPolicyAction extends BaseAction {

	static final String INSTID = "NOTREQ";
	CommonUtil comutil = new CommonUtil();

	// added by gowtham220719
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

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

	CommonDesc commondesc = new CommonDesc();

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

	public String comUserCode(HttpSession session) {

		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public List getPassPolicyList() {
		return passPolicyList;
	}

	private List passPolicyList;

	private List institutionList;

	public List getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(List institutionList) {
		this.institutionList = institutionList;
	}

	public String passwordPolicyHome() {

		// List passwordPolicyList = this.getPolicyDetails();
		// System.out.println("------"+passwordPolicyList);

		/// setPassPolicyList(passwordPolicyList);
		// HttpSession session = getRequest().getSession();
		// System.out.println(session);
		//HttpSession session = getRequest().getSession();
		//String username = (String) session.getAttribute("SS_USERNAME");
		//System.out.println(username);
		
		List institutes = this.getInstitutes();
		setInstitutionList(institutes);
		return "changePolicy";
	}

	public List getInstitutes() {
		List list = null;

		/*
		 * String instQry =
		 * "SELECT INST_ID,INST_NAME  FROM INSTITUTION WHERE  STATUS !='2'  ORDER BY PREFERENCE "
		 * ; list = jdbctemplate.queryForList(instQry);
		 */

		// by gowtham020919
		String instQry = "SELECT INST_ID,INST_NAME  FROM INSTITUTION WHERE  STATUS !=?  ORDER BY PREFERENCE ";
		list = jdbctemplate.queryForList(instQry, new Object[] { "2" });

		return list;
	}

	public String authpasswordPolicyHome() {
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");

		List passwordPolicyList = this.getPolicyDetails(username);
		trace("------" + passwordPolicyList);

		setPassPolicyList(passwordPolicyList);

		return "authchangePolicy";
	}

	public String passwordPolicyInsert() {

		trace("password policy calledd------------------------");
		HttpSession session = getRequest().getSession();
		String USERTYPE = (String) session.getAttribute("USERTYPE");
		String username = (String) session.getAttribute("SS_USERNAME");
		IfpTransObj transact = commondesc.myTranObject("SAVEPASSPOLICY", txManager);

		trace("password policy calledd-- USERTYPE ------" + USERTYPE + " ========SS_USERNAME ===== " + username);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String instId = getRequest().getParameter("instname");
		String sLowerCase = getRequest().getParameter("LowerCaseAlphabetsAllowed");
		String sUpperCase = getRequest().getParameter("UpperCaseAlphabetsAllowed");
		String sNumbers = getRequest().getParameter("NumbersAllowed");
		String sSpecial = getRequest().getParameter("SpecialCharactersAllowed");
		String sFirstChar = getRequest().getParameter("FirstCharacter");
		String sLastChar = getRequest().getParameter("LastCharacter");

		String sTotalCount = getRequest().getParameter("TotalCount");
		int insertCnt = 0;
		// int updStatus = 0;
		try {
			// updStatus = this.updateStatus(instid);
			if (sLastChar.equals("-1")) {
				sLastChar = "-1";
			}
			if (sFirstChar.equals("-1")) {
				sLastChar = "-1";
			}
			insertCnt = this.savePassPolicy(instId, username, sLowerCase, sUpperCase, sNumbers, sSpecial, sFirstChar,
					sLastChar, sTotalCount);
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception while inserting" + e);
		}
		if (insertCnt == 1) {

			// added by gowtham_220719
			/************* AUDIT BLOCK **************/
			try {

				auditbean.setActmsg("Password Changed Successfully Waiting For Authorization..");
				auditbean.setUsercode(USERTYPE);
				auditbean.setAuditactcode("3001");

				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				commondesc.insertAuditTrail(instId, USERTYPE, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				audite.printStackTrace();
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/
			txManager.commit(transact.status);
			session.setAttribute("prevmsg", " Password Changed Successfully Waiting For Authorization..");
			session.setAttribute("preverr", "S");
			return "changePolicy";

		} else {
			txManager.rollback(transact.status);
			trace("Not Crated Password Policy");
			session.setAttribute("prevmsg", "Not Crated Password Policy");
			session.setAttribute("preverr", "E");
		}
		/*
		 * System.out.println(sLowerCase); System.out.println(sUpperCase);
		 * System.out.println(sNumbers); System.out.println(sSpecial);
		 * System.out.println(sFirstChar); System.out.println(sLastChar);
		 * System.out.println(sTotalCount);
		 */
		System.out.println();

		return "changePolicy";
	}

	private int updateStatus(String instid) {
		StringBuilder updStat = new StringBuilder();
		updStat.append("UPDATE PASSWORDPOLICY ");
		updStat.append("SET STATUS = '0' WHERE STATUS = '1' and INST_ID = '" + instid + "' ");
		System.out.println("--- updStat --- " + updStat.toString());
		int updStatCount = jdbctemplate.update(updStat.toString());
		return updStatCount;

	}

	/*
	 * public List passwordPolicyList() { String
	 * sLowerCase="",sUpperCase="",sNumbers="",sSpecial ="",sFirstChar = "",
	 * sLastChar = "", sTotalCount = "" ,sFirstCharString = "",sLastCharString =
	 * ""; List getPolicyList = this.getPolicyDetails();
	 * if(!(getPolicyList.isEmpty())){ Iterator itr=getPolicyList.iterator();
	 * while (itr.hasNext()) { Map map = (Map) itr.next(); sLowerCase =
	 * (String)map.get("LOWERCASE"); sUpperCase = (String)map.get("UPPERCASE");
	 * sNumbers = (String)map.get("NUMBERS"); sSpecial =
	 * (String)map.get("SPECIAL"); sFirstChar = (String)map.get("FIRSTCHAR");
	 * sLastChar = (String)map.get("LASTCHAR"); sFirstCharString =
	 * (String)map.get("FIRSTCHAR_STRING"); sLastCharString =
	 * (String)map.get("LASTCHAR_STRING"); sTotalCount =
	 * (String)map.get("TOTALCOUNT"); } } return getPolicyList;
	 * 
	 * }
	 */

	public String authorizePasswordPolicy() {
		System.out.println("authorizePasswordPolicy policy calledd------------------------");
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");
		String listid = getRequest().getParameter("listid");
		String auth = getRequest().getParameter("auth");
		String reason = getRequest().getParameter("reason");
		String instid = getRequest().getParameter("instid");
		System.out.println(reason + "-----------------");

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		IfpTransObj transact = commondesc.myTranObject("AUTHPASSPOLICY", txManager);
		int updStatus = 0;
		try {
			updStatus = this.authorizeePassPolicy(instid, username, listid, auth, reason);
			trace("authorize Password policy status:" + updStatus);
			if (updStatus == 1) {
				txManager.commit(transact.status);
				session.setAttribute("prevmsg", " Password Policy " + auth + " Successfully ");
				session.setAttribute("preverr", "S");

				/************* AUDIT BLOCK **************/
				try {

					auditbean.setActmsg(" Password Policy authorized Successfully");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("3001");

					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

				return "authchangePolicy";
			} else {
				txManager.rollback(transact.status);
				trace("Not Crated Password Policy");
				session.setAttribute("prevmsg", "Not Authorzed Password Policy");
				session.setAttribute("preverr", "E");
			}

		} catch (Exception e) {
			txManager.rollback(transact.status);
			trace("Not Crated Password Policy");
			session.setAttribute("prevmsg", "Not Authorized Password Policy");
			session.setAttribute("preverr", "E");
		}
		return "authchangePolicy";
	}

	private int authorizeePassPolicy(String instid, String username, String listid, String auth, String reason)
			throws Exception {
		StringBuilder updpass = new StringBuilder();
		StringBuilder authpass = new StringBuilder();
		StringBuilder delpass = new StringBuilder();
		StringBuilder updUser = new StringBuilder();
		int cntupd = -1;
		try {

			if (auth.equals("DeAuthorize")) {
				/*
				 * updpass.append(
				 * "update PASSWORDPOLICY set AUTH_STATUS = '0' , REASON='"
				 * +reason+"' WHERE INST_ID = '"+instid+"' and ID = '"
				 * +listid+"'"); System.out.println("--- updpass --- "
				 * +updpass.toString()); cntupd =
				 * jdbctemplate.update(updpass.toString());
				 */

				// by gowtham030919
				updpass.append("update PASSWORDPOLICY set AUTH_STATUS = ? , REASON=? WHERE INST_ID = ? and ID = ? ");
				System.out.println("--- updpass --- " + updpass.toString());
				cntupd = jdbctemplate.update(updpass.toString(), new Object[] { "0", reason, instid, listid });

			} else {
				updpass.append("update PASSWORDPOLICY set AUTH_STATUS = '1' WHERE INST_ID = '" + instid + "' and ID = '"
						+ listid + "'");
				System.out.println("--- updpass --- " + updpass.toString());
				cntupd = jdbctemplate.update(updpass.toString());

				/*
				 * delpass.append("delete from PASSWORDPOLICY");
				 * System.out.println("--- delpass --- "+authpass.toString());
				 * cntupd = jdbctemplate.update(delpass.toString());
				 * authpass.append(
				 * "INSERT INTO PASSWORDPOLICY  (SELECT * FROM  PASSWORDPOLICY WHERE AUTH_STATUS='1' and INST_ID = '"
				 * +instid+"' and ID = '"+listid+"' )"); System.out.println(
				 * "--- authpass --- "+authpass.toString()); cntupd =
				 * jdbctemplate.update(authpass.toString());
				 * 
				 */

				/*
				 * updUser.append("update USER_DETAILS set FIRSTTIME = 9  ");
				 * System.out.println("--- updUser --- "+updUser.toString());
				 * cntupd = jdbctemplate.update(updUser.toString());
				 */

				// by gowtham030919
				updUser.append("update USER_DETAILS set FIRSTTIME = ?  ");
				System.out.println("--- updUser --- " + updUser.toString());
				cntupd = jdbctemplate.update(updUser.toString(), new Object[] { "9" });

			}
			// IFP_USER_DETAILS

		} catch (Exception e) {
			cntupd = 0;
			trace("Authorizing Password Policy Exception" + e);
		}
		cntupd = 1;

		return cntupd;
	}

	public void getPasswordPolicy() throws IOException {
		trace("getPasswordPolicy metho called....");
		try {
			HttpSession session = getRequest().getSession();
			String username = (String) session.getAttribute("SS_USERNAME");
			String rPassword = getRequest().getParameter("password");
			System.out.println("rPassword---" + rPassword);
			StringBuilder res = new StringBuilder();
			StringBuilder res2 = new StringBuilder();
			String sLowerCase = "", sUpperCase = "", sNumbers = "", sSpecial = "", sFirstChar = "", sLastChar = "",
					sTotalCount = "", sFirstCharString = "", sLastCharString = "";

			List getPolicyList = this.getChangedPolicyDetails(INSTID);
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

				char FIRST = rPassword.charAt(0);
				char LAST = rPassword.charAt(rPassword.length() - 1);

				int sTOTAL = rPassword.length();
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
				for (int i = 0; i < rPassword.length(); i++) {
					// System.out.println("Invalid :"+ch.charAt(i));
					if (Character.isDigit(rPassword.charAt(i))) {
						sDIGIT++;
					}
					if (Character.isUpperCase(rPassword.charAt(i))) {
						sUPPER++;
					}
					if (Character.isLowerCase(rPassword.charAt(i))) {
						sLOWER++;
					}
					if ((!Character.isDigit(rPassword.charAt(i)) && !Character.isLetter(rPassword.charAt(i)))) {
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
						&& (Integer.parseInt(sTotalCount) <= sTOTAL)) {
					System.out.println("SUCCESS");
					res.append("SUCCESS");
					getResponse().getWriter().write(res.toString());
				} else {
					res.append("Your Password Length Should be " + sTotalCount + "Digit <br/> ");
					res.append("Your Password Should Contain : " + sLowerCase + " * Lower Case " + sUpperCase
							+ " * Upper Case " + sNumbers + " * Numbers " + sSpecial + "-Special Charectar ");
					int i = 0;
					if (!sFirstChar.equals("-1")) {
						res.append(" and First Lettar Should be : " + sFirstCharString);
					}
					if (!sLastChar.equals("-1")) {
						res.append(" and Last Lettar Should be : " + sLastCharString);
					}
					trace("resssss---" + res.toString());
					getResponse().getWriter().write(res.toString());
				}
				trace("getPasswordPolicy metho called....end");

				System.out.println("resssss2---" + res2.toString());
			}
		} catch (Exception e) {
			trace("Exception getPasswordPolicy---" + e);
		}
		// getResponse().getWriter().write(res.toString());
	}

	private List getPolicyDetails(String username) {
		List x = null;
		StringBuilder s = new StringBuilder();
		s.append("select ID,INST_ID, LOWERCASE, UPPERCASE,NUMBERS, SPECIAL, ");
		s.append(
				"FIRSTCHAR, DECODE(FIRSTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')FIRSTCHAR_STRING,  ");
		s.append("LASTCHAR, DECODE(LASTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')LASTCHAR_STRING, ");
		s.append(
				"TOTALCOUNT, CREATED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, AUTH_STATUS,REASON from PASSWORDPOLICY WHERE AUTH_STATUS = '0' and CREATED_BY!='"
						+ username + "'  ");
		enctrace("getPolicyDetails::" + s.toString());
		x = jdbctemplate.queryForList(s.toString());
		return x;
	}

	private List getChangedPolicyDetails(String instid) throws Exception {
		List x = null;
		StringBuilder s = new StringBuilder();
		s.append("select ID,INST_ID, LOWERCASE, UPPERCASE,NUMBERS, SPECIAL, ");
		s.append(
				"FIRSTCHAR, DECODE(FIRSTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')FIRSTCHAR_STRING,  ");
		s.append("LASTCHAR, DECODE(LASTCHAR,'U','Upper Case','N','Number','S','Special','L','Lower')LASTCHAR_STRING, ");
		s.append(
				"TOTALCOUNT, CREATED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, AUTH_STATUS , REASON from PASSWORDPOLICY WHERE AUTH_STATUS = '1' AND ROWNUM=1 ");
		x = jdbctemplate.queryForList(s.toString());
		enctrace("getChangedPolicyDetails:" + s.toString());
		return x;
	}

	private int savePassPolicy(String instid, String username, String sLowerCase, String sUpperCase, String sNumbers,
			String sSpecial, String sFirstChar, String sLastChar, String sTotalCount) {
		StringBuilder inspass = new StringBuilder();
		Date date = new Date();

		//trace("username is ======== " + username);
		inspass.append("insert into PASSWORDPOLICY ");
		inspass.append(
				"(INST_ID ,ID ,LOWERCASE, UPPERCASE, NUMBERS, SPECIAL, FIRSTCHAR, LASTCHAR, TOTALCOUNT, CREATED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, AUTH_STATUS )");
		inspass.append(" values ");
		inspass.append("('" + instid + "',(select NVL(max(to_number(ID))+1,'1') from PASSWORDPOLICY) ,'" + sLowerCase
				+ "','" + sUpperCase + "','" + sNumbers + "','" + sSpecial + "','" + sFirstChar + "','" + sLastChar
				+ "','" + sTotalCount + "',sysdate,sysdate,'" + username + "','--','0')");
		System.out.println("--- inspass --- " + inspass.toString());
		int cntinst = jdbctemplate.update(inspass.toString());

	

		/*
		 * // by gowtham030919 inspass.append("insert into PASSWORDPOLICY ");
		 * inspass.append(
		 * "(INST_ID ,ID ,LOWERCASE, UPPERCASE, NUMBERS, SPECIAL, FIRSTCHAR, LASTCHAR, TOTALCOUNT, CREATED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, AUTH_STATUS )"
		 * ); inspass.append(" values "); inspass.append(
		 * "(?,(select NVL(max(to_number(ID))+1,'1') from PASSWORDPOLICY) ,?,?,?,?,?,?,?,?,?,?,?,?)"
		 * ); System.out.println("--- inspass --- " + inspass.toString()); int
		 * cntinst = jdbctemplate.update(inspass.toString(), new Object[] {
		 * instid, sLowerCase, sUpperCase, sNumbers, sSpecial, sFirstChar,
		 * sLastChar, sTotalCount, date.getCurrentDate(), date.getCurrentDate(),
		 * username, "--", "0" });
		 */

		enctrace("inpassQuery=======   " + inspass);

		return cntinst;

	}

	public void setPassPolicyList(List passPolicyList) {
		this.passPolicyList = passPolicyList;
	}

}
