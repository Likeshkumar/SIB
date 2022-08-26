package com.ifg.Config.Currency;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Date;

public class CurrencyAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8376161637970676446L;

	CommonDesc commondesc = new CommonDesc();

	AuditBeans auditbean = new AuditBeans();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

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

	AuditBeans beans = new AuditBeans();

	public AuditBeans getBeans() {
		return beans;
	}

	public void setBeans(AuditBeans beans) {
		this.beans = beans;
	}

	private List CurrencyStandardsList;

	public List getCurrencyStandardsList() {
		return CurrencyStandardsList;
	}

	public void setCurrencyStandardsList(List currencyStandardsList) {
		CurrencyStandardsList = currencyStandardsList;
	}

	private String selectedCurrDesc;
	private String selectedNumeric;

	public String getSelectedCurrDesc() {
		return selectedCurrDesc;
	}

	public void setSelectedCurrDesc(String selectedCurrDesc) {
		this.selectedCurrDesc = selectedCurrDesc;
	}

	public String getSelectedNumeric() {
		return selectedNumeric;
	}

	public void setSelectedNumeric(String selectedNumeric) {
		this.selectedNumeric = selectedNumeric;
	}

	private List currencydetails;

	public List getCurrencydetails() {
		return currencydetails;
	}

	public void setCurrencydetails(List currencydetails) {
		this.currencydetails = currencydetails;
	}

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public String comAdminUserCode(HttpSession session) {
		// HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("SS_USERNAME");
		return instid;
	}

	private Boolean flag;

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String addCurrency() throws Exception {
		trace("*************** Adding currency Begins **********");
		enctrace("*************** Adding currency Begins **********");
		HttpSession session = getRequest().getSession();
		session.setAttribute("act_type", "insert");
		setFlag(false);
		String adminmkrchkr = commondesc.getAdminMkrChkrStatus(jdbctemplate);
		System.out.println("adminmkrchkr -- " + adminmkrchkr);

		List getCurrencyStandardsList = getCurrencyStandards(jdbctemplate);
		this.setCurrencyStandardsList(getCurrencyStandardsList);
		trace("setting curensy standards here.....");
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
		trace("\n\n");
		enctrace("\n\n");
		return "currencyadd_home";
	}

	public void getCurencystd() throws IOException {

		HttpSession session = getRequest().getSession();
		String alphacode = getRequest().getParameter("alphacode");
		System.out.println("alphacode->" + alphacode);
		
		/*String currstdquery = "select * from CURRENCY_FUNDSCODE where ALPHABETIC_CODE='" + alphacode + "' and rownum=1";
		enctrace("currstdquery is:" + currstdquery);*/
		
		
		String currstdquery = "select * from CURRENCY_FUNDSCODE where ALPHABETIC_CODE=? and rownum=?";
		enctrace("currstdquery is:" + currstdquery);

		
		
		// System.out.println("query_makerchecker "+query_makerchecker);
		StringBuilder result = new StringBuilder();
		String currDesc = "";
		String numCode = "";
		String symbol = "";
		try {
			
			/*List currstdSelectedList = jdbctemplate.queryForList(currstdquery);
			System.out.println(currstdSelectedList);*/
			List currstdSelectedList = jdbctemplate.queryForList(currstdquery,new Object[]{alphacode,"1"});
			System.out.println(currstdSelectedList);
			if (!(currstdSelectedList.isEmpty())) {
				Iterator itr = currstdSelectedList.iterator();
				while (itr.hasNext()) {
					Map temp = (Map) itr.next();
					currDesc = (String) temp.get("CURRENCY");
					numCode = (String) temp.get("NUMERIC_CODE");
					symbol = (String) temp.get("CURRENCY_SYMBOL");
				}
			}

			result.append("<table border='0' cellpadding='0' cellspacing='0' width='40%' > ");
			result.append("<tr>");
			result.append("<td>");
			result.append("Currency Description");
			result.append("</td>");
			result.append("<td>");
			result.append("	<input type='text' name='currdesc' id='currdesc'  value='" + currDesc
					+ "' style='width:160px' readonly/>");
			result.append("</td>");
			result.append(" </tr>	");
			result.append("<tr> ");
			result.append("<td>");
			result.append(" Numeric Code");
			result.append("</td>");
			result.append("<td>  ");
			result.append("	<input type='text' name='numericcode' id='numericcode' value='" + numCode
					+ "' style='width:160px' readonly/>");
			result.append(" </td>");
			result.append("</tr>	");
			result.append("<tr> ");
			result.append("<td>");
			result.append("Currency Symbol");
			result.append("</td>");
			result.append("<td>  ");
			result.append("<input type=\'text\' name=\'currsymbol\' id=\'currsymbol\' value='" + symbol
					+ "' style=\'width:160px\' maxlength=\'5\'  onkeyup=\"chkChars('Currency Symbol',this.id,this.value)\" />");
			result.append("</td>");
			result.append("</tr>	");
			result.append("</table>");

		} catch (Exception e) {
			trace("Error while macker checker enable " + e.getMessage());
			e.printStackTrace();
		}
		getResponse().getWriter().write(result.toString());
	}

	public String saveCurrency() {
		trace("*************** Save Currency Begins **********");
		enctrace("*************** Save Currency Begins **********");

		String currdesc, currcode;
		IfpTransObj transact = commondesc.myTranObject("SAVECUR", txManager);
		HttpSession session = getRequest().getSession();
		currdesc = (getRequest().getParameter("currdesc"));
		currcode = (getRequest().getParameter("currcode")).toUpperCase();
		String numericcode = getRequest().getParameter("numericcode");
		String cursymbol = getRequest().getParameter("currsymbol");
		String actiontype = (getRequest().getParameter("submitcur"));


		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Date date =  new Date();
		
		String usercode = comAdminUserCode(session);
		System.out.println("usercode --- " + usercode);
		trace("actiontype__" + actiontype);
		String query_currency;
		String dispmsg;
		try 
		{
			
			if(currcode.equalsIgnoreCase("-1"))
			{
				addActionError("SELECT CURRENCY CODE");
				return addCurrency();
			}
			/*if(currdesc==null ||currdesc=="")
			{
				addActionError("ENTER CURRENCY DESC");
				return "currencyadd_home";
			}
			if(numericcode==null ||numericcode=="")
			{
				addActionError("ENTER CURRENCY NUMBER");
				return "currencyadd_home";
			}*/
			
			String mkrchkr = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
			System.out.println("mkrchkr   ---- " + mkrchkr);
			String code;
			int x;
			String authcode;
			String errormsg;
			int result_insrt_cur = 1;
			int result_insrt_cur_temp = 0;
			if (mkrchkr.equals("N")) {
				authcode = "1";
				errormsg = "";
			} else {
				authcode = "0";
				errormsg = "Waiting for authorization";
			}
			if (actiontype.equals("Save")) {
				trace("Checking Global curency code..");
				code = "CURRENCY_CODE='" + currcode + "'";
				int curexist = currencyCheck(code);
				trace("Checked Global curency.." + curexist);
				trace("");
				if (curexist != 0) {
					addActionError(" Currency code Already exist ");
					trace(" Currency code Already exist ");
					return addCurrency();
				}

				// query_currency="insert into
				// GLOBAL_CURRENCY(CURRENCY_DESC,CURRENCY_CODE, NUMERIC_CODE,
				// CUR_SYMBOL)" +
				// "values('"+currdesc+"','"+currcode+"','"+numericcode+"','"+cursymbol+"')";
				dispmsg = "Configured";
				if (mkrchkr.equals("N")) {
					trace("Inserting main table currency...");
					String query_insrt_cur = insertCurrencyInMainTable(currdesc, currcode, numericcode, cursymbol,
							authcode, usercode);
					enctrace("query_insrt_cur : " + query_insrt_cur);
					
			/*		
					
					result_insrt_cur = jdbctemplate.update(query_insrt_cur);
					trace("Got result_insrt_cur : " + result_insrt_cur);
				}
				trace("Inserting temp currency...");
				String query_insrt_temp_cur = insertCurrencyInTempTable(currdesc, currcode, numericcode, cursymbol,
						authcode, usercode);
				enctrace("query_insrt_temp_cur : " + query_insrt_temp_cur);
				result_insrt_cur_temp = jdbctemplate.update(query_insrt_temp_cur);
				trace("Got resulttemporary_insrt_cur : " + result_insrt_cur_temp);
			}

			else {
				// query_currency = "update GLOBAL_CURRENCY set
				// CURRENCY_DESC='"+currdesc+"', NUMERIC_CODE='"+numericcode+"',
				// CUR_SYMBOL='"+cursymbol+"' WHERE
				// CURRENCY_CODE='"+currcode+"'";
				dispmsg = "Updated";
				System.out.println("MAKR -- " + mkrchkr);
				if (mkrchkr.equals("N")) {
					trace("Updating main table currency...");
					String query_update_cur = updateCurrencyInMainTable(currdesc, numericcode, cursymbol, currcode,
							authcode, usercode);
					enctrace("query_update_cur : " + query_update_cur);
					result_insrt_cur = jdbctemplate.update(query_update_cur);
					trace("Got result_insrt_cur : " + result_insrt_cur);
				}
				trace("Updating temp currency...");
				String query_update_temp_cur = updateCurrencyInTempTable(currdesc, numericcode, cursymbol, currcode,
						authcode, usercode);
				enctrace("query_update_temp_cur : " + query_update_temp_cur);
				result_insrt_cur_temp = jdbctemplate.update(query_update_temp_cur);
				trace("Got resulttemporary_insrt_cur : " + result_insrt_cur_temp);
				
				*/
					
					
					
					//added by gowtham120819
					//result_insrt_cur = jdbctemplate.update(query_insrt_cur);
					result_insrt_cur = jdbctemplate.update(query_insrt_cur,new Object[]{currdesc, currcode, numericcode, cursymbol,
					authcode, usercode,date.getCurrentDate()});
					trace("Got result_insrt_cur : " + result_insrt_cur);
				}
				trace("Inserting temp currency...");
				String query_insrt_temp_cur = insertCurrencyInTempTable(currdesc, currcode, numericcode, cursymbol,authcode, usercode);
				enctrace("query_insrt_temp_cur : " + query_insrt_temp_cur);
				
				//added by gowtham120819
				//result_insrt_cur_temp = jdbctemplate.update(query_insrt_temp_cur);
				result_insrt_cur_temp = jdbctemplate.update(query_insrt_temp_cur,new Object[]{currdesc, currcode, numericcode, cursymbol,authcode, usercode,date.getCurrentDate()});
				trace("Got resulttemporary_insrt_cur : " + result_insrt_cur_temp);
			}

			else {
				// query_currency = "update GLOBAL_CURRENCY set
				// CURRENCY_DESC='"+currdesc+"', NUMERIC_CODE='"+numericcode+"',
				// CUR_SYMBOL='"+cursymbol+"' WHERE
				// CURRENCY_CODE='"+currcode+"'";
				dispmsg = "Updated";
				System.out.println("MAKR -- " + mkrchkr);
				if (mkrchkr.equals("N")) {
					trace("Updating main table currency...");
					String query_update_cur = updateCurrencyInMainTable(currdesc, numericcode, cursymbol, currcode,authcode, usercode);
					enctrace("query_update_cur : " + query_update_cur);
					
					//added by gowtham-120819
					result_insrt_cur = jdbctemplate.update(query_update_cur);
					result_insrt_cur = jdbctemplate.update(query_update_cur,new Object[]{currdesc, numericcode, cursymbol,authcode, usercode,date.getCurrentDate(),currcode});
					trace("Got result_insrt_cur : " + result_insrt_cur);
				}
				trace("Updating temp currency...");
				String query_update_temp_cur = updateCurrencyInTempTable(currdesc, numericcode, cursymbol, currcode,authcode, usercode);
				enctrace("query_update_temp_cur : " + query_update_temp_cur);

				//added by gowtham-120819
				//result_insrt_cur = jdbctemplate.update(query_update_temp_cur);
				result_insrt_cur_temp = jdbctemplate.update(query_update_temp_cur,new Object[]{currdesc, numericcode, cursymbol,authcode, usercode,date.getCurrentDate(),currcode});
				trace("Got resulttemporary_insrt_cur : " + result_insrt_cur_temp);
				
				
				
				
			}
			if (result_insrt_cur <= 0 || result_insrt_cur_temp <= 0) {
				transact.txManager.rollback(transact.status);
				addActionError(" Could not " + currdesc + " - " + dispmsg + " currency details ");
				trace(" Could not " + dispmsg + " currency details ");
				return "currencyadd_home";
			}
			
			
			//added by gowtham_220719
			auditbean.setActmsg("Currency updated SUCCESSFULLY BY THE USER......"+usercode+"  waiting for authorization ");
			
			auditbean.setAuditactcode("01333");
			trace("ip address======>  "+ip);
			auditbean.setIpAdress(ip);

			auditbean.setUsercode(usercode);
			auditbean.setActmsg("Currency  Added SUCCESSFULLY BY THE USER " + usercode);
			auditbean.setAuditactcode("01333");
			commondesc.insertAuditTrailPendingCommit("UTBSL", usercode, auditbean, jdbctemplate, txManager);
			transact.txManager.commit(transact.status);
			addActionMessage(" Currency \"" + currdesc + " \" - " + dispmsg + " successfully." + errormsg + "");
			trace(" Currency details " + dispmsg + " successfully");
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Error in currency details " + e);
			trace("Exception : Error in currency details " + e.getMessage());
			e.printStackTrace();

		}

		return "required_home";
	}

	public String viewCurrency() {
		trace("*************** View Currency Begins **********");
		enctrace("*************** View Currency Begins **********");
		HttpSession session = getRequest().getSession();
		try {
			String currecnystatus = "view";
			setStatus(currecnystatus);
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
			String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,decode(CURRENCY_STATUS,'1','Active','0','InActive',CURRENCY_STATUS) as CURRENCY_STATUS,decode(AUTH_CODE,'0','Waiting for auth','1','Authorized','9','Rejected') as AUTH_CODE,AUTH_DATE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS from GLOBAL_CURRENCY";
			List ccylist = jdbctemplate.queryForList(selectccy_query);
			if (!(ccylist.isEmpty())) {
				setCurrencydetails(ccylist);
				trace("Currency List ===> " + ccylist.size());
				session.setAttribute("curerr", "S");
			} else {
				addActionError("No records Found ");
				trace("No Currency Details Found ");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error While Fetching CURRENCY Detail");
			trace("Error While Fetching CURRENCY Detail" + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "currencyviewall";
	}

	public int currencyCheck(String curcode) {
		
		/*String curchkqry = "SELECT COUNT(*) FROM GLOBAL_CURRENCY WHERE " + curcode + " and AUTH_CODE!=9";
		System.out.println("select qry __" + curchkqry);
		int x = jdbctemplate.queryForInt(curchkqry);
*/
		
		//by gowtham160819
				String curchkqry = "SELECT COUNT(*) FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE=? and AUTH_CODE!=?";
				System.out.println("select qry __" + curchkqry);
				int x = jdbctemplate.queryForInt(curchkqry,new Object[]{curcode,"9"});
		return x;
	}

	String curdesc;
	String curcode;
	String actiontype;

	public String getCurdesc() {
		return curdesc;
	}

	public void setCurdesc(String curdesc) {
		this.curdesc = curdesc;
	}

	public String getCurcode() {
		return curcode;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	String cur_symbol;
	String number_code;

	public String getCur_symbol() {
		return cur_symbol;
	}

	public void setCur_symbol(String cur_symbol) {
		this.cur_symbol = cur_symbol;
	}

	public String getNumber_code() {
		return number_code;
	}

	public void setNumber_code(String number_code) {
		this.number_code = number_code;
	}

	public String editCurrency() {
		trace("edit");

		HttpSession session = getRequest().getSession();
		try {
			String CURRCODE = getRequest().getParameter("currcode");
			session.setAttribute("act_type", "edit");
			setFlag(true);
			String mkrchkr_status = getRequest().getParameter("mkrchkr");
			System.out.println("mkrchkr_status --- " + mkrchkr_status);
			beans.setMkrchkrstatus(mkrchkr_status);

			List getCurrencyStandardsList = getCurrencyStandards(jdbctemplate);
			this.setCurrencyStandardsList(getCurrencyStandardsList);

			/*String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,CUR_SYMBOL,NUMERIC_CODE,CURRENCY_STATUS  from GLOBAL_CURRENCY where CURRENCY_CODE='"
					+ CURRCODE + "'";

			enctrace(selectccy_query);
			List ccylist = jdbctemplate.queryForList(selectccy_query);*/
			
			
			//added by gowtham120819
			String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,CUR_SYMBOL,NUMERIC_CODE,CURRENCY_STATUS  from GLOBAL_CURRENCY where CURRENCY_CODE=?";
			enctrace(selectccy_query);
			List ccylist = jdbctemplate.queryForList(selectccy_query,new Object[]{CURRCODE});
			if (!(ccylist.isEmpty())) {
				Iterator itr = ccylist.iterator();
				while (itr.hasNext()) {
					Map temp = (Map) itr.next();
					String curcode = (String) temp.get("CURRENCY_CODE");
					String crdesc = (String) temp.get("CURRENCY_DESC");
					String cursymbol = (String) temp.get("CUR_SYMBOL");
					String numbercode = (String) temp.get("NUMERIC_CODE");

					trace("currency code" + curcode);
					trace("currency desc" + crdesc);
					setCurcode(curcode);
					setCurdesc(crdesc);
					setCur_symbol(cursymbol);
					setNumber_code(numbercode);
				}
			}
		} catch (Exception e) {
			// transobj.txManager.rollback(transobj.status);
			addActionError("Exception : Error while updating currency details ");
			trace("Exception : Error while updating currency details " + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "editcurrencydetails";
	}

	public String deleteCurrency() {
		String currcode = getRequest().getParameter("currcode");
		trace(currcode);
		IfpTransObj transobj = commondesc.myTranObject("DELCUR", txManager);
		try {

		/*	String del_qury = "delete from GLOBAL_CURRENCY where CURRENCY_CODE='" + currcode + "'";
			int del_status = jdbctemplate.update(del_qury);*/
			
			//added by gowtham120819
			String del_qury = "delete from GLOBAL_CURRENCY where CURRENCY_CODE=?'";
			int del_status = jdbctemplate.update(del_qury,new Object[]{currcode});
			if (del_status <= 0) {
				transobj.txManager.rollback(transobj.status);
				getRequest().getSession().setAttribute("preverr", "E");
				getRequest().getSession().setAttribute("prevmsg", "Error while deleting the Currency configuration ");
				trace(" Exception : Error while deleting the Currency configuration ");
				return "currencyadd_home";
			} else {
				transobj.txManager.commit(transobj.status);
				getRequest().getSession().setAttribute("preverr", "S");
				getRequest().getSession().setAttribute("prevmsg", "Currency Deleted Sucessfully");
				trace(" Currency Deleted Sucessfully");
			}
		} catch (Exception e) {
			transobj.txManager.rollback(transobj.status);
			getRequest().getSession().setAttribute("preverr", "E");
			getRequest().getSession().setAttribute("prevmsg",
					"Exception : Error while deleting the Currency configuration " + e);
			trace("Exception : Error while deleting the Currency configuration " + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return viewCurrency();
	}

	public String viewCurrencyDetail() {
		String currencycode = getRequest().getParameter("currencycode");
		trace("currencycode    " + currencycode);
		
		/*String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,DECODE(CURRENCY_STATUS,'1','ACTIVE','0','INACTIVE') AS CURRENCY_STATUS  from GLOBAL_CURRENCY where CURRENCY_CODE='"
				+ currencycode + "'";
		enctrace("selectccy_query" + selectccy_query);
		try {
			List ccylist = jdbctemplate.queryForList(selectccy_query);*/
		
		
		//by gowtham160819
				String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,DECODE(CURRENCY_STATUS,'1','ACTIVE','0','INACTIVE') AS CURRENCY_STATUS  from GLOBAL_CURRENCY where CURRENCY_CODE=?";
				enctrace("selectccy_query" + selectccy_query);
				try {
					List ccylist = jdbctemplate.queryForList(selectccy_query,new Object[]{currencycode});
			
			if (!(ccylist.isEmpty())) {
				Iterator itr = ccylist.iterator();
				while (itr.hasNext()) {
					Map temp = (Map) itr.next();
					String crcode = (String) temp.get("CURRENCY_CODE");
					String crdesc = (String) temp.get("CURRENCY_DESC");
					String statsus = (String) temp.get("STATUS");
					trace("currency code" + crcode);
					trace("currency desc" + crdesc);
					trace("currency statsus" + statsus);
					setCurcode(crcode);
					setCurdesc(crdesc);
				}
			}
		} catch (Exception e) {
			getRequest().getSession().setAttribute("preverr", "E");
			getRequest().getSession().setAttribute("prevmsg",
					"Exception : Error while deleting the Currency configuration ");
			trace("Exception : Error while deleting the Currency configuration " + e.getMessage());
		}
		return viewCurrency();
	}

	public String authCurrencydetails() {
		trace("*************** auth Currency Begins **********");
		enctrace("*************** auth Currency Begins **********");
		HttpSession session = getRequest().getSession();
		try {
			String currecnystatus = "auth";
			setStatus(currecnystatus);
			String usercode = comAdminUserCode(session);
			System.out.println("usercode -- " + usercode);
			String currcode = getRequest().getParameter("currcode");
			trace("currcode --- " + currcode);
			System.out.println("currcode --- " + currcode);
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
			
			/*String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,decode(CURRENCY_STATUS,'1','Active','0','InActive',CURRENCY_STATUS) as CURRENCY_STATUS,NUMERIC_CODE,CUR_SYMBOL,decode(AUTH_CODE,'0','Waiting for auth','1','Authorized','9','Rejected') as AUTH_CODE,AUTH_DATE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS from GLOBAL_CURRENCY where CURRENCY_CODE='"
					+ currcode + "'";
			System.out.println("selectccy_query --- " + selectccy_query);
			List ccylist = jdbctemplate.queryForList(selectccy_query);
			*/
			
			//by gowtham160819
			String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,decode(CURRENCY_STATUS,'1','Active','0','InActive',CURRENCY_STATUS) as CURRENCY_STATUS,NUMERIC_CODE,CUR_SYMBOL,decode(AUTH_CODE,'0','Waiting for auth','1','Authorized','9','Rejected') as AUTH_CODE,AUTH_DATE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS from GLOBAL_CURRENCY where CURRENCY_CODE=?";
			System.out.println("selectccy_query --- " + selectccy_query);
			List ccylist = jdbctemplate.queryForList(selectccy_query,new Object[]{currcode});
			if (!(ccylist.isEmpty())) {
				setCurrencydetails(ccylist);
				trace("Currency List ===> " + ccylist.size());
			} else {
				addActionError("No records Found ");
				trace("No Currency Details Found ");
				return "required_home";
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curerr", "Error While Fetching CURRENCY Detail");
			trace("Error While Fetching CURRENCY Detail" + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "authorizeCurrency";
	}

	public String authCurrency() {
		trace("*************** View Currency Begins **********");
		enctrace("*************** View Currency Begins **********");
		HttpSession session = getRequest().getSession();
		try {
			String usercode = comAdminUserCode(session);
			System.out.println("usercode --- " + usercode);
			String currecnystatus = "auth";
			setStatus(currecnystatus);
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
			/*String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,decode(CURRENCY_STATUS,'1','Active','0','InActive',CURRENCY_STATUS) as CURRENCY_STATUS,decode(AUTH_CODE,'0','Waiting for auth','1','Authorized','9','Rejected') as AUTH_CODE,AUTH_DATE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS from GLOBAL_CURRENCY where AUTH_CODE=0 and ADDED_BY!='"
					+ usercode + "'";
			List ccylist = jdbctemplate.queryForList(selectccy_query);*/
			
			
			//added by gowtham-120819
			String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,decode(CURRENCY_STATUS,'1','Active','0','InActive',CURRENCY_STATUS) as CURRENCY_STATUS,decode(AUTH_CODE,'0','Waiting for auth','1','Authorized','9','Rejected') as AUTH_CODE,AUTH_DATE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS from GLOBAL_CURRENCY where AUTH_CODE=?and ADDED_BY!=?";
			List ccylist = jdbctemplate.queryForList(selectccy_query,new Object[]{"0",usercode});
			if (!(ccylist.isEmpty())) {
				setCurrencydetails(ccylist);
				trace("Currency List ===> " + ccylist.size());
				session.setAttribute("curerr", "S");
			} else {
				addActionError("No records Found ");
				trace("No Currency Details Found ");
				return "required_home";
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curerr", "Error While Fetching CURRENCY Detail");
			trace("Error While Fetching CURRENCY Detail" + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "currencyviewall";
	}

	public String authorizeCurrency() {
		HttpSession session = getRequest().getSession();
		try {
			String CURRCODE = getRequest().getParameter("currcode");
			String mkrchkr_status = getRequest().getParameter("mkrchkr");
			System.out.println("mkrchkr_status --- " + mkrchkr_status);
			beans.setMkrchkrstatus(mkrchkr_status);
			/*
			String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,CUR_SYMBOL,NUMERIC_CODE,CURRENCY_STATUS  from GLOBAL_CURRENCY where CURRENCY_CODE='"
					+ CURRCODE + "'";
			enctrace(selectccy_query);
			List ccylist = jdbctemplate.queryForList(selectccy_query);*/
			//by gowtham160819
			String selectccy_query = "select CURRENCY_DESC,CURRENCY_CODE,CUR_SYMBOL,NUMERIC_CODE,CURRENCY_STATUS  from GLOBAL_CURRENCY where CURRENCY_CODE=?";
			enctrace(selectccy_query);
			List ccylist = jdbctemplate.queryForList(selectccy_query,new Object[]{CURRCODE});
			
			if (!(ccylist.isEmpty())) {
				Iterator itr = ccylist.iterator();
				while (itr.hasNext()) {
					Map temp = (Map) itr.next();
					String curcode = (String) temp.get("CURRENCY_CODE");
					String crdesc = (String) temp.get("CURRENCY_DESC");
					String cursymbol = (String) temp.get("CUR_SYMBOL");
					String numbercode = (String) temp.get("NUMERIC_CODE");

					trace("currency code" + curcode);
					trace("currency desc" + crdesc);
					setCurcode(curcode);
					setCurdesc(crdesc);
					setCur_symbol(cursymbol);
					setNumber_code(numbercode);
				}
			}
		} catch (Exception e) {
			// transobj.txManager.rollback(transobj.status);
			addActionError("Exception : Error while updating currency details ");
			trace("Exception : Error while updating currency details " + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "authorizeCurrency";
	}

	public String authdeauthcurrencylist() {
		trace("*************Currency authorization *****************");
		enctrace("*************Currency authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHCUR", txManager);
		String statusmsg = "";
		Date date =  new Date();
		try {
			// String instid = comInstId( session );
			String userid = comAdminUserCode(session);
			
			//added by gowtham_220719
			String  ip=(String) session.getAttribute("REMOTE_IP");
			String authstatus = "";
			String update_authdeauth_qury;
			String auth = getRequest().getParameter("auth");
			// String instid = getRequest().getParameter("instid");
			String currcode = getRequest().getParameter("currcode");
			String currdesc = getRequest().getParameter("currdesc");
			String numericcode = getRequest().getParameter("numericcode");
			String currsymbol = getRequest().getParameter("currsymbol");

			// String username =commondesc.getUserName(instid, userid,
			// jdbctemplate);
			int insertedmain = 1;
			String move_main_table_qury;
			int update_authdeauth;
			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				statusmsg = " Authorized ";
				update_authdeauth_qury = updateAuthCurrency(userid, currcode);
				
				/*update_authdeauth = jdbctemplate.update(update_authdeauth_qury);*/

				//added by gowtham-120819
				update_authdeauth = jdbctemplate.update(update_authdeauth_qury,new Object[]{"1",date.getCurrentDate(),userid,currcode});
				enctrace("Update authorized currency :  " + update_authdeauth);
				trace("Update authorized currency :  " + update_authdeauth);

				// no temp table
				/*
				 * int maintablestatus =
				 * getmaintablestatus(currcode,jdbctemplate);
				 * if(maintablestatus<1){ move_main_table_qury =
				 * insertCurrcenyMaintable(currcode); insertedmain =
				 * jdbctemplate.update(move_main_table_qury); trace(
				 * "Inserting main table...got :  " + insertedmain ); }else{
				 * move_main_table_qury =
				 * updateCurrencyInMainTable(currdesc,numericcode,currsymbol,
				 * currcode,"1",userid); insertedmain =
				 * jdbctemplate.update(move_main_table_qury); trace(
				 * "Update currency in main table...got :  " + insertedmain ); }
				 * 
				 * 
				 */
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setUsercode(userid);
				auditbean.setActmsg("Authorize Currency  By THE USER " + userid);
				auditbean.setAuditactcode("01334");
				commondesc.insertAuditTrailPendingCommit("UTBSL", userid, auditbean, jdbctemplate, txManager);
			} else {
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				statusmsg = " De-Authorized ";
				String remarks = getRequest().getParameter("reason");
				update_authdeauth_qury = updateDeAuthCurrency(userid, remarks, currcode);
				/*update_authdeauth = jdbctemplate.update(update_authdeauth_qury);*/
				update_authdeauth = jdbctemplate.update(update_authdeauth_qury,new Object[]{"9",date.getCurrentDate(),userid,remarks,currcode});
				auditbean.setUsercode(userid);
				auditbean.setActmsg("De Authorize Currency  By THE USER " + userid);
				auditbean.setAuditactcode("01335");
				commondesc.insertAuditTrailPendingCommit("UTBSL", userid, auditbean, jdbctemplate, txManager);
			}
			// int update_authdeauth =
			// commondesc.executeTransaction(update_authdeauth_qury,
			// jdbctemplate);
			// int update_authdeauth =
			// jdbctemplate.update(update_authdeauth_qury);
			if (update_authdeauth < 1 || insertedmain < 1) {
				txManager.rollback(transact.status);
				addActionError("could not " + statusmsg + "");
				trace("Got rolledback...");
				return "required_home";
			}

			if (currdesc == null) {
				currdesc = currcode;
			}

			txManager.commit(transact.status);
			addActionMessage("Currency \"" + currdesc + "\"" + statusmsg + " Sucessfully");
			trace("Got committed....");

		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError("Error while " + statusmsg + " the currency " + e);
			trace("Error while adding the curency " + e.getMessage());
		}
		return "required_home";

	}

	public String insertCurrencyInMainTable(String currdesc, String currcode, String numericcode, String cursymbol,
			String authcode, String usercode) {
		/*String query_currency = "INSERT INTO GLOBAL_CURRENCYGLOBAL_CURRENCY(CURRENCY_DESC,CURRENCY_CODE, NUMERIC_CODE, CUR_SYMBOL,AUTH_CODE,ADDED_BY,ADDED_DATE)"
				+ "values('" + currdesc + "','" + currcode + "','" + numericcode + "','" + cursymbol + "','" + authcode
				+ "','" + usercode + "',sysdate)";*/
		//ADDED BY GOWTHAM_120819
		
			String query_currency = "INSERT INTO GLOBAL_CURRENCY(CURRENCY_DESC,CURRENCY_CODE, NUMERIC_CODE, CUR_SYMBOL,AUTH_CODE,ADDED_BY,ADDED_DATE)"
			+ "values(?,?,?,?,?,?,?)";
		return query_currency;
	}

	public String insertCurrencyInTempTable(String currdesc, String currcode, String numericcode, String cursymbol,
			String authcode, String usercode) {
	/*	String query_currency = "INSERT INTO GLOBAL_CURRENCY(CURRENCY_DESC,CURRENCY_CODE, NUMERIC_CODE, CUR_SYMBOL,AUTH_CODE,ADDED_BY,ADDED_DATE)"
				+ "values('" + currdesc + "','" + currcode + "','" + numericcode + "','" + cursymbol + "','" + authcode
				+ "','" + usercode + "',sysdate)";*/
		//ADDED BY GOWTHAM_120819
		
		String query_currency = "INSERT INTO GLOBAL_CURRENCY(CURRENCY_DESC,CURRENCY_CODE, NUMERIC_CODE, CUR_SYMBOL,AUTH_CODE,ADDED_BY,ADDED_DATE)"
		+ "values(?,?,?,?,?,?,?)";
		return query_currency;
	}

	public String updateCurrencyInMainTable(String currdesc, String numericcode, String cursymbol, String currcode,
			String authcode, String usercode) {
		/*String query_currency = "update GLOBAL_CURRENCY set CURRENCY_DESC='" + currdesc + "', NUMERIC_CODE='"
				+ numericcode + "', CUR_SYMBOL='" + cursymbol + "',AUTH_CODE='" + authcode + "',ADDED_BY='" + usercode
				+ "',ADDED_DATE=sysdate  WHERE CURRENCY_CODE='" + currcode + "'";
		trace("query_currency  == " + query_currency);*/
		   
				 //ADDED BY GOWTHAM_120819
				 String query_currency = "update GLOBAL_CURRENCY set CURRENCY_DESC=?, NUMERIC_CODE=?, CUR_SYMBOL=?,AUTH_CODE=?,ADDED_BY=?,ADDED_DATE=? WHERE CURRENCY_CODE=?";
				   trace("query_currency  == " + query_currency);
		return query_currency;
	}

	public List getCurrencyStandards(JdbcTemplate jdbctemplate) {
		String currncystandardquery = "select * from CURRENCY_FUNDSCODE ORDER BY ALPHABETIC_CODE";
		enctrace("currncystandardquery->" + currncystandardquery);
		List currncystandardList = jdbctemplate.queryForList(currncystandardquery);

		return currncystandardList;
	}

	public String updateCurrencyInTempTable(String currdesc, String numericcode, String cursymbol, String currcode,
			String authcode, String usercode) {
		/*String query_currency = "update GLOBAL_CURRENCY set CURRENCY_DESC='" + currdesc + "', NUMERIC_CODE='"
				+ numericcode + "', CUR_SYMBOL='" + cursymbol + "',AUTH_CODE='" + authcode + "',ADDED_BY='" + usercode
				+ "',ADDED_DATE=sysdate  WHERE CURRENCY_CODE='" + currcode + "'";
		trace("query_currency  == " + query_currency);*/
		//ADDED BY GOWTHAM_120819
		   String query_currency = "update GLOBAL_CURRENCY set CURRENCY_DESC=?, NUMERIC_CODE=?, CUR_SYMBOL=?,AUTH_CODE=?,ADDED_BY=?,ADDED_DATE=?  WHERE CURRENCY_CODE=?";
			trace("query_currency  == " + query_currency);
		return query_currency;
	}

	public String updateAuthCurrency(String userid, String currcode) {
		/*String update_authdeauth_qury = "UPDATE GLOBAL_CURRENCY SET AUTH_CODE='1',AUTH_DATE=sysdate,AUTH_BY='" + userid
				+ "' WHERE CURRENCY_CODE='" + currcode + "'";
		return update_authdeauth_qury;*/
		//added by gowtham_120819
				String update_authdeauth_qury = "UPDATE GLOBAL_CURRENCY SET AUTH_CODE=?,AUTH_DATE=?,AUTH_BY=? WHERE CURRENCY_CODE=?";
				return update_authdeauth_qury;
	}

	public String updateDeAuthCurrency(String userid, String remarks, String currcode) {
		/*String update_authdeauth_qury = "UPDATE GLOBAL_CURRENCY SET AUTH_CODE='9',AUTH_DATE=sysdate,AUTH_BY='" + userid
				+ "',REMARKS='" + remarks + "'where CURRENCY_CODE='" + currcode + "'";
		return update_authdeauth_qury;*/
		//added by gowtham-120819
				String update_authdeauth_qury = "UPDATE GLOBAL_CURRENCY SET AUTH_CODE=?,AUTH_DATE=?,AUTH_BY=?,REMARKS=? where CURRENCY_CODE=?";
				return update_authdeauth_qury;
	}

	public String insertCurrcenyMaintable(String currcode) {
		/*String insert_main_qury = "INSERT INTO GLOBAL_CURRENCY (CURRENCY_DESC,CURRENCY_CODE,NUMERIC_CODE,CUR_SYMBOL,CURRENCY_STATUS,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS)"
				+ "(SELECT CURRENCY_DESC,CURRENCY_CODE,NUMERIC_CODE,CUR_SYMBOL,CURRENCY_STATUS,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE='"
				+ currcode + "')";
		System.out.println("--- insert_main_qury --- " + insert_main_qury);
		return insert_main_qury;*/
		//by gowtham160819
				String insert_main_qury = "INSERT INTO GLOBAL_CURRENCY (CURRENCY_DESC,CURRENCY_CODE,NUMERIC_CODE,CUR_SYMBOL,CURRENCY_STATUS,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS)"
						+ "(SELECT CURRENCY_DESC,CURRENCY_CODE,NUMERIC_CODE,CUR_SYMBOL,CURRENCY_STATUS,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE=?)";
				
				System.out.println("--- insert_main_qury --- " + insert_main_qury);
				return insert_main_qury;
	}

	public int getmaintablestatus(String currcode, JdbcTemplate jdbctemplate) {
	/*	String count_main_qury = "SELECT COUNT(*) FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE='" + currcode + "'";
		System.out.println("--- count_main_qury --- " + count_main_qury);
		int cntinst = jdbctemplate.queryForInt(count_main_qury);*/
		//by gowtham160819
				String count_main_qury = "SELECT COUNT(*) FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE=?";
				System.out.println("--- count_main_qury --- " + count_main_qury);
				int cntinst = jdbctemplate.queryForInt(count_main_qury,new Object[]{currcode});
		return cntinst;
	}
}