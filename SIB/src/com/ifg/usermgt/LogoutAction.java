package com.ifg.usermgt;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.ContextUpdater;
import com.ifp.util.IfpTransObj;

/**
 * SRNP0003
 * 
 * @author CGSPL
 *
 */
public class LogoutAction extends BaseAction {
	private static final long serialVersionUID = 1L;

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

	public synchronized int updateUserStatus(String instid, String usertype, String username, JdbcTemplate jdbctemplate)
			throws Exception {
		String update_status_qury = "";
		int update_result;

		/*
		 * if(usertype.equals("SUPERADMIN") ) { update_status_qury=
		 * "update ADMIN_USER set LASTLOGIN = to_char(sysdate,'DD-MON-YY hh24:mm:ss'),LOGINSTATUS='0' where username='"
		 * +username+"' "; } else { update_status_qury=
		 * "update USER_DETAILS set LASTLOGIN = to_char(sysdate,'DD-MON-YY hh24:mm:ss'),LOGINSTATUS='0' where username='"
		 * +username+"' and INSTID='"+instid+"'"; } enctrace("Query is ----> "
		 * +update_status_qury); int update_result =
		 * jdbctemplate.update(update_status_qury); return update_result;
		 */

		/// by gowtham
		if (usertype.equals("SUPERADMIN")) {

			update_status_qury = "update ADMIN_USER set LASTLOGIN = to_char(sysdate,'DD-MON-YY hh24:mm:ss'),LOGINSTATUS='0' where username=? ";
			update_result = jdbctemplate.update(update_status_qury, new Object[] { username });
			enctrace("Query is ----> " + update_status_qury);

		} else {
			update_status_qury = "update USER_DETAILS set LASTLOGIN = to_char(sysdate,'DD-MON-YY hh24:mm:ss'),LOGINSTATUS='0' where username=? and INSTID=?";
			update_result = jdbctemplate.update(update_status_qury, new Object[] { username, instid });
			enctrace("Query is ----> " + update_status_qury);
		}

		return update_result;

	}

	public String logoutAction() {
		trace("*************** LOgout Action Begins **********");
		enctrace("*************** LOgout Action Begins **********");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("LOGOUT", txManager);

		// addSessionintrace(dataSource, jdbctemplate, session);

		try {
			trace("instid :" + comInstId(session));
			trace("loginuserid :  " + comUserId(session));
			trace("branchcode :  " + comBranchId());
			trace("comusertype :  " + comuserType());
			String username = (String) session.getAttribute("SS_USERNAME");
			trace("USER NAME IS  : " + username);
			String usertype = (String) session.getAttribute("USERTYPE");
			trace("User type is : " + usertype);

			String update_status_qury = "X";
			int update_result = this.updateUserStatus(comInstId(session), usertype, username, jdbctemplate);
			trace("UPdate Result ==" + update_result);

			if (update_result == 1) {
				trace("update_result-->commit");
				transact.txManager.commit(transact.status);
			}

			else {
				trace("update_result-->rollback");
				transact.txManager.rollback(transact.status);
				return "login";
			}

			if (session != null) {
				ContextUpdater conup = new ContextUpdater();
				// conup.removeMapvalue(session);
				trace("session not expired");
				Enumeration e = session.getAttributeNames();
				session.removeAttribute("USERID");
				session.removeAttribute("APPLICATIONTYPE");
				// System.out.println("---->logout"+e.nextElement());
				while (e.hasMoreElements()) {
					System.out.println("---->logout" + e.nextElement());
					session.removeAttribute((String) e.nextElement());
				}

				session.invalidate();
			}

			// transact.txManager.commit(transact.status);
			trace("logout....Commit success");
		} catch (Exception e) {
			e.printStackTrace();
			if (!transact.status.isCompleted()) {
				transact.txManager.rollback(transact.status);
				trace("Exception rolledback.....");
			}

			trace("ERROR : ==> " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
			return "login";
		}

		finally {
			trace("Finally....Transaction status..." + transact.status.isCompleted());
			if (!transact.status.isCompleted()) {
				transact.txManager.commit(transact.status);
				trace("logoutAction...Transaction forcefully committed.....");
			}
		}
		trace("\n\n");
		enctrace("\n\n");
		return "login";
	}

	public String CSRFlogoutAction() {
		trace("*************** LOgout Action Begins **********");
		enctrace("*************** LOgout Action Begins **********");
		HttpSession session = getRequest().getSession();
		String msg = (String) session.getAttribute("message");
		IfpTransObj transact = commondesc.myTranObject("LOGOUT", txManager);

		// addSessionintrace(dataSource, jdbctemplate, session);

		try {
			trace("instid :" + comInstId(session));
			trace("loginuserid :  " + comUserId(session));
			trace("branchcode :  " + comBranchId());
			trace("comusertype :  " + comuserType());
			String username = (String) session.getAttribute("SS_USERNAME");
			trace("USER NAME IS  : " + username);
			String usertype = (String) session.getAttribute("USERTYPE");
			trace("User type is : " + usertype);

			String update_status_qury = "X";
			int update_result = this.updateUserStatus(comInstId(session), usertype, username, jdbctemplate);
			trace("UPdate Result ==" + update_result);

			if (update_result == 1) {
				trace("update_result-->commit");
				transact.txManager.commit(transact.status);
			}

			else {
				trace("update_result-->rollback");
				transact.txManager.rollback(transact.status);
				return "login";
			}
		} catch (Exception e) {
			if (!transact.status.isCompleted()) {
				transact.txManager.rollback(transact.status);
				trace("Exception rolledback.....");
			}

			trace("ERROR : ==> " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
			return "login";
		}

		finally {
			trace("Finally....Transaction status..." + transact.status.isCompleted());
			if (!transact.status.isCompleted()) {
				transact.txManager.commit(transact.status);
				trace("logoutAction...Transaction forcefully committed.....");
			}
		}
		trace("\n\n");
		enctrace("\n\n");
		return "login";
	}

}
