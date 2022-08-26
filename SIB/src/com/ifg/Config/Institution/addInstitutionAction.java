package com.ifg.Config.Institution;
import com.ifg.Bean.ServerValidationBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import test.Date;
import test.Validation;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.ifg.Config.Licence.Licensemanager;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifg.Config.Institution.addInstitutionActionDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class addInstitutionAction extends BaseAction implements ServletResponseAware {
	private static final long serialVersionUID = 1L;

	private List deploymentlist;
	private List currencylist;
	Validation validation = new Validation();
	CommonDesc commondesc = new CommonDesc();

	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	AuditBeans auditbean = new AuditBeans();
	ServerValidationBean bean = new ServerValidationBean();
	public ServerValidationBean getBean() {
		return bean;
	}

	public void setBean(ServerValidationBean bean) {
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

	addInstitutionActionDAO instdao = new addInstitutionActionDAO();

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

	public List getCurrencylist() {
		return currencylist;
	}

	public void setCurrencylist(List currencylist) {
		this.currencylist = currencylist;
	}

	private List instlist;
	private List institutiondetail;

	Licensemanager chckinstlic = new Licensemanager();

	public List getInstitutiondetail() {
		return institutiondetail;
	}

	public void setInstitutiondetail(List institutiondetail) {
		this.institutiondetail = institutiondetail;
	}

	private List currency;

	public List getCurrency() {
		return currency;
	}

	public void setCurrency(List currency) {
		this.currency = currency;
	}

	private List curencydesc;

	public List getCurencydesc() {
		return curencydesc;
	}

	public void setCurencydesc(List curencydesc) {
		this.curencydesc = curencydesc;
	}

	public List getInstlist() {
		return instlist;
	}

	public void setInstlist(List instlist) {
		this.instlist = instlist;
	}

	public List getDeploymentlist() {
		return deploymentlist;
	}

	public void setDeploymentlist(List deploymentlist) {
		this.deploymentlist = deploymentlist;
	}

	public String comInstId(HttpSession session) {
		// HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode(HttpSession session) {
		// HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public String comAdminUserCode(HttpSession session) {
		// HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("SS_USERNAME");
		return instid;
	}

	// For displaying the menu to select deployment type and currency type
	AuditBeans beans = new AuditBeans();

	public AuditBeans getBeans() {
		return beans;
	}

	public void setBeans(AuditBeans beans) {
		this.beans = beans;
	}

	public String instid;

	public String getInstid() {
		return instid;
	}

	public void setInstid(String instid) {
		this.instid = instid;
	}

	public String display() {
		trace("*****Institution Adding home*******\n");
		HttpSession session = getRequest().getSession();
		List result;
		List result_currency;

		int instconfig = commondesc.reqCheck().reqCheckStaticTable(session, jdbctemplate);
		if (instconfig <= 0) {
			return "required_home";
		}

		String qury = "select * from DEPLOYMENT ";
		enctrace("qury  : " + qury);
		String def_currency_qury = "select * from GLOBAL_CURRENCY";
		enctrace("def_currency_qury: " + def_currency_qury);
		try {

			/*
			 * List padsslist = commondesc.getPADSSKEY(jdbctemplate);
			 * System.out.println("padsslist empty"+padsslist);
			 * if(padsslist==null) { addActionError(
			 * "Not Configured PADSS Key Configure Key"); return
			 * "required_home"; }
			 */

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
				addActionError("Configure Admin Maker Checker status");
				return "required_home";
			}

			trace("Getting the deployment");
			result = jdbctemplate.queryForList(qury);
			setDeploymentlist(result);
			session.setAttribute("addInstErrorStatus", "S");
			trace("addInstErrorStatus is S");
			result_currency = jdbctemplate.queryForList(def_currency_qury);
			setCurrencylist(result_currency);
			session.setAttribute("addCurrencyStatus", "S");
			trace("addCurrencyStatus is S");
		} catch (Exception e) {
			trace("\nError WHile Getting the DEPLOYMENTS " + e.getMessage());
			session.setAttribute("addInstErrorStatus", "E");
		}

		return "diplayinst";

	}

	// checking license for the institution and save in database
	String curcode;

	public String getCurcode() {
		return curcode;
	}

	public void setCurcode(String curcode) {
		this.curcode = curcode;
	}

	@Transactional()
	public String addInst() throws Throwable 
	{
		System.out.println("--- ADDING INSTITUTION----");
		trace("*********SAVING INSTITUTION BEGINS************");
		enctrace("*********SAVING INSTITUTION BEGINS************");
		int inst_insert;
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		IfpTransObj transact = commondesc.myTranObject("ADDINST", txManager);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		 Date date =  new Date();
		String instid = (getRequest().getParameter("instid"));
		String instname = (getRequest().getParameter("instname"));
		String deploymenttype = (getRequest().getParameter("deploymenttype"));
		String orderref = (getRequest().getParameter("orderref"));
		String branchattched = (getRequest().getParameter("branchattched"));
		String brcodelen = (getRequest().getParameter("brcodelen"));
		String countrycode = (getRequest().getParameter("countrycode"));
		String accnumleng = (getRequest().getParameter("accnumleng"));
		String cid = (getRequest().getParameter("cid"));
		String cidbasedon = (getRequest().getParameter("cidbasedon"));
		String mailalertreq = (getRequest().getParameter("mailalertreq"));
		String smsalertreq = (getRequest().getParameter("smsalertreq"));
		String accttypelength = (getRequest().getParameter("accttypelength"));
		String acctsubtypelength = (getRequest().getParameter("acctsubtypelength"));
		String cardtypelength = (getRequest().getParameter("cardtypelength"));
		String login_retry_cnt = (getRequest().getParameter("login_retry_cnt"));
		String pin_retry_cnt = (getRequest().getParameter("pin_retry_cnt"));
		String preference = "1";// (getRequest().getParameter("preference"));
		String maxaddoncards = getRequest().getParameter("maxaddoncards");
		String maxaddonaccounts = getRequest().getParameter("maxaddonaccounts");
		String pcadssenble = getRequest().getParameter("pcadssenble");
		String seqkeyvalue = getRequest().getParameter("seqkeyvalue");
		String renewalperiods = getRequest().getParameter("renewalperiods");
		String currencytype = (getRequest().getParameter("defaultcurrency"));
		String usercode = comAdminUserCode(session);
		System.out.println("usercode --- " + usercode);
		String mkrchkr = getRequest().getParameter("mkrchkr");
		System.out.println("mkrchkr   ---- " + mkrchkr);

		bean.setInstid(instid);
		bean.setInstname(instname);
		bean.setDeploymenttype(deploymenttype);
		bean.setOrderref(orderref);
		bean.setBranchattched(branchattched);
		bean.setBrcodelen(brcodelen);
		bean.setCountrycode(countrycode);
		bean.setAccnumleng(accnumleng);
		bean.setCid(cid);
		bean.setCidbasedon(cidbasedon);
		bean.setMailalertreq(mailalertreq);
		bean.setSmsalertreq(smsalertreq);
		bean.setAcctsubtypelength(acctsubtypelength);
		bean.setAccttypelength(accttypelength);
		bean.setCardtypelength(cardtypelength);
		bean.setLogin_retry_cnt(login_retry_cnt);
		bean.setPin_retry_cnt(pin_retry_cnt);
		bean.setMaxaddonaccounts(maxaddonaccounts);
		bean.setMaxaddoncards(maxaddoncards);
		bean.setPcadssenble(pcadssenble);
		bean.setSeqkeyvalue(seqkeyvalue);
		bean.setRenewalperiods(renewalperiods);
		bean.setCurrencytype(currencytype);
		
		try 
		{
			/*
			check=Validation.NumberCharcter(instid);
			if(!check)
			{
				addFieldError("instid","ENTER instid ID");
				return display();
			}
			
			check=Validation.charcter(instname);
			if(!check)
			{
				addFieldError("instname","ENTER instname");
				return display();
			}
			*/
			if(deploymenttype.equals("-1"))
			{
				addFieldError("deploymenttype","Select deploymenttype");
				return display();
			}
			
			
			checkvalue=Validation.number(orderref);
			if(checkvalue==0)
			{
				System.out.println("DFSGFDGDF");
				addFieldError("orderref","ENTER orderref");
				return display();
			}
			
			if(branchattched.equals("-1"))
			{
				addFieldError("branchattched","Select branchattched");
				return display();
			}
			
			checkvalue=Validation.number(brcodelen);
			if(checkvalue==0)
			{
				addFieldError("brcodelen","ENTER brcodelen");
				return display();
			}
			
			checkvalue=Validation.number(countrycode);
			if(checkvalue==0)
			{
				System.out.println("DFSGFDGDF");
				addFieldError("countrycode","ENTER countrycode");
				return display();
			}
			
			checkvalue=Validation.number(accnumleng);
			if(checkvalue==0)
			{
				System.out.println("DFSGFDGDF");
				addFieldError("accnumleng"," 	ACC NUM LENGTH");
				return display();
			}
			
			
			checkvalue=Validation.number(cid);
			if(checkvalue==0)
			{
				addFieldError("cid","CUST ID LENGTH ");
				return display();
			}
			
			if(cidbasedon.equals("-1"))
			{
				addFieldError("cidbasedon","Select a value");
				return display();
			}
			if(mailalertreq.equals("-1"))
			{
				addFieldError("mailalertreq","Select a value");
				return display();
			}
			if(smsalertreq.equals("-1"))
			{
				addFieldError("smsalertreq","Select a value");
				return display();
			}
			
			checkvalue=Validation.number(accttypelength);
			if(checkvalue==0)
			{
				addFieldError("accttypelength","ENTER accttypelength");
				return display();
			}
			
			checkvalue=Validation.number(acctsubtypelength);
			if(checkvalue==0)
			{
				addFieldError("acctsubtypelength","ENTER acctsubtypelength");
				return display();
			}
			
			checkvalue=Validation.number(cardtypelength);
			if(checkvalue==0)
			{
				addFieldError("cardtypelength","ENTER cardtypelength");
				return display();
			}
			
			
			checkvalue=Validation.number(login_retry_cnt);
			if(checkvalue==0)
			{
				addFieldError("login_retry_cnt","ENTER login_retry_cnt");
				return display();
			}
			checkvalue=Validation.number(pin_retry_cnt);
			if(checkvalue==0)
			{
				addFieldError("pin_retry_cnt","ENTER pin_retry_cnt");
				return display();
			}
		
			checkvalue=Validation.number(maxaddoncards);
			if(checkvalue==0)
			{
				addFieldError("maxaddoncards","ENTER maxaddoncards");
				return display();
			}
		
			checkvalue=Validation.number(maxaddonaccounts);
			if(checkvalue==0)
			{
				addFieldError("maxaddonaccounts","ENTER maxaddonaccounts");
				return display();
			}
			
			
			checkvalue=Validation.number(renewalperiods);
			if(checkvalue==0)
			{
				addFieldError("renewalperiods","ENTER renewalperiods");
				return display();
			}
			
			if(pcadssenble.equals("-1"))
			{
				addFieldError("pcadssenble","Select apcadssenble");
				return display();
			}
			if(seqkeyvalue.equals("-1"))
			{
				addFieldError("seqkeyvalue","Select seqkeyvalue");
				return display();
			}
			if(currencytype.equals("-1"))
			{
				addFieldError("currencytype","Select currencytype");
				return display();
			}
			
			
			
			instid = instid.toUpperCase();
			
			
			
			/*trace("Getting the inst_count from DEPLOYMENT");
			String inst_count_frm_dep_query = "select INST_COUNT from DEPLOYMENT where DEPLOYID='" + deploymenttype
					+ "'";
			enctrace("inst_count_frm_dep_query : " + inst_count_frm_dep_query);
			int depcount_deptable = jdbctemplate.queryForInt(inst_count_frm_dep_query);
			trace("Got depcount_deptable [ " + depcount_deptable + " ]");

			String inst_count_frm_inst_query = "select count(*) from INSTITUTION where DEPLOY_ID='" + deploymenttype
					+ "'";
			enctrace("inst_count_frm_inst_query : " + inst_count_frm_inst_query);

			trace("Counting the deployment");
			int depcount_insttable = jdbctemplate.queryForInt(inst_count_frm_inst_query);
			enctrace("Deployment count got [ " + depcount_insttable + " ] ");

			if (depcount_insttable < depcount_deptable) {
				trace("Checking institution exist...");
				String inst_exist_query = "select count(*) from INSTITUTION where INST_ID='" + instid + "'";
				enctrace("inst_exist_query : " + inst_exist_query);
				int res = jdbctemplate.queryForInt(inst_exist_query);
				trace("Got ...res [" + res + "] ");*/
			
			
			trace("Getting the inst_count from DEPLOYMENT");
			/*String inst_count_frm_dep_query = "select INST_COUNT from DEPLOYMENT where DEPLOYID='" + deploymenttype+ "'";
			enctrace("inst_count_frm_dep_query : " + inst_count_frm_dep_query);
			int depcount_deptable = jdbctemplate.queryForInt(inst_count_frm_dep_query);*/
			
			//added by gowtham130819
			String inst_count_frm_dep_query = "select INST_COUNT from DEPLOYMENT where DEPLOYID=?";
			enctrace("inst_count_frm_dep_query : " + inst_count_frm_dep_query);
			int depcount_deptable = jdbctemplate.queryForInt(inst_count_frm_dep_query,new Object[]{deploymenttype});
			
			trace("Got depcount_deptable [ " + depcount_deptable + " ]");

			/*String inst_count_frm_inst_query = "select count(*) from INSTITUTION where DEPLOY_ID='" + deploymenttype+ "'";
			enctrace("inst_count_frm_inst_query : " + inst_count_frm_inst_query);
			trace("Counting the deployment");
			int depcount_insttable = jdbctemplate.queryForInt(inst_count_frm_inst_query);
			enctrace("Deployment count got [ " + depcount_insttable + " ] ");*/
			
			//added by gowtham-120819
			String inst_count_frm_inst_query = "select count(*) from INSTITUTION where DEPLOY_ID=?";
			enctrace("inst_count_frm_inst_query : " + inst_count_frm_inst_query);
			trace("Counting the deployment");
			int depcount_insttable = jdbctemplate.queryForInt(inst_count_frm_inst_query,new Object[]{deploymenttype});
			
			enctrace("Deployment count got [ " + depcount_insttable + " ] ");
			
			if (depcount_insttable < depcount_deptable) {
				/*trace("Checking institution exist...");
				String inst_exist_query = "select count(*) from INSTITUTION where INST_ID='" + instid + "'";
				enctrace("inst_exist_query : " + inst_exist_query);
				int res = jdbctemplate.queryForInt(inst_exist_query);
				trace("Got ...res [" + res + "] ");*/
				
				//added by gowtham-130819
				trace("Checking institution exist...");
				String inst_exist_query = "select count(*) from INSTITUTION where INST_ID=?";
				enctrace("inst_exist_query : " + inst_exist_query);
				int res = jdbctemplate.queryForInt(inst_exist_query,new Object[]{instid});
				trace("Got ...res [" + res + "] ");
				
				
				
				
				if (res >= 1) {
					trace("Institution id already exist");
					session.setAttribute("addInstErrormsg", "INST ID already exist");
					session.setAttribute("addInstErrorStatus", "E");
					return "SucessInstAdd";
				} else {
					String instLicence_check = chckinstlic.chckInstLicence(instid, deploymenttype, commondesc);
					System.out.println("THE LICENCE CHECKING " + instLicence_check);
					trace("THE LICENCE CHECKING " + instLicence_check);
					// invalid - License vlaues Not Matched
					// noinstid - Inst IS not Matched
					// nodeplyid - Deploy ID not matched
					// binexceed - BIN count Exceed
					// nofile - No Licence File matched
					// Error - Error While cheking licence file
					if (instLicence_check == "nofile") {
						trace("Licence File Not Found");
						session.setAttribute("addInstErrormsg", "Licence File Not Found");
						session.setAttribute("addInstErrorStatus", "E");
						return "SucessInstAdd";
					}
					if (instLicence_check == "noinstid") {
						trace("Institution Id Not Found In License");
						session.setAttribute("addInstErrormsg", "Institution Id Not Found In License");
						session.setAttribute("addInstErrorStatus", "E");
						return "SucessInstAdd";
					}
					/*
					 * if(instLicence_check=="nodeplyid") { trace(
					 * "Deployment Id Not Found In License" );
					 * session.setAttribute("addInstErrormsg",
					 * "Deployment Id Not Found In License");
					 * session.setAttribute("addInstErrorStatus","E"); return
					 * "SucessInstAdd"; } if(instLicence_check=="binexceed") {
					 * trace( "BIN Count Not Matched With License" );
					 * session.setAttribute("addInstErrormsg",
					 * "BIN Count Not Matched With License");
					 * session.setAttribute("addInstErrorStatus","E"); return
					 * "SucessInstAdd"; }
					 */
					if (instLicence_check == "invalid") {
						trace("Licence Parameter is Invalid");
						session.setAttribute("addInstErrormsg", "Licence Parameter is Invalid");
						session.setAttribute("addInstErrorStatus", "E");
						return "SucessInstAdd";
					}
					if (instLicence_check == "Error") {
						trace("Error While Checking The License File");
						session.setAttribute("addInstErrormsg", "Error While Checking The License File ");
						session.setAttribute("addInstErrorStatus", "E");
						return "SucessInstAdd";
					}
					if (instLicence_check == "LICENSE_MATCHED") {

						trace("Inserting branch mastere...");
						
						/*String query_insrt_branch = "INSERT INTO BRANCH_MASTER (INST_ID, BRANCH_CODE, BRANCH_NAME, BR_ADDR1, BR_ADDR2, BR_ADDR3, BR_CITY, BR_STATE, BR_PHONE, BR_FAX_NUM, BR_EMAIL, BR_MANAGER, BRANCHIP, BRANCHUNAME, BRANCHPWD, MAKER_ID, MAKEDATE, CHEKR_ID, CHCKDATE, MKCK_STATUS,AUTH_CODE)"
								+ " values('" + instid + "','000','" + instid
								+ "_HeadOffice', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', 'Default_Branch', sysdate , 'Default_Branch', sysdate , 'M','1')";
						enctrace("query_insrt_branch : " + query_insrt_branch);
						int result_insrt_branch = jdbctemplate.update(query_insrt_branch);
						*/
						

						String query_insrt_branch = "INSERT INTO BRANCH_MASTER (INST_ID, BRANCH_CODE, BRANCH_NAME, BR_ADDR1, BR_ADDR2, BR_ADDR3, BR_CITY, BR_STATE, BR_PHONE, BR_FAX_NUM, BR_EMAIL, BR_MANAGER, BRANCHIP, BRANCHUNAME, BRANCHPWD, MAKER_ID, MAKEDATE, CHEKR_ID, CHCKDATE, MKCK_STATUS,AUTH_CODE)"+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						enctrace("query_insrt_branch : " + query_insrt_branch);
						//trace("Got result_insrt_branch : " + result_insrt_branch);
						//int cntinst = jdbctemplate.queryForInt(query_insrt_branch,new Object[]{instid,"000",instid+"_HeadOffice","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch",date.getCurrentDate(),"Default_Branch",date.getCurrentDate(),"M","1"});
						int result_insrt_branch = jdbctemplate.update(query_insrt_branch,new Object[]{instid,"000",instid+"_HeadOffice","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch","Default_Branch",date.getCurrentDate(),"Default_Branch",date.getCurrentDate(),"M","1"});
						
						trace("Got result_insrt_branch : " + result_insrt_branch);

						trace("Inserting institution...");
						String authcode;
						int result_insrt_inst = 1;
						if (mkrchkr.equals("N")) {
							authcode = "1";
						} else {
							authcode = "0";
						}
						
						
					/*	
						if (mkrchkr.equals("N")) {
							String query_insrt_inst = instdao.insertInstitutionInMainTable(instid, instname,
									deploymenttype, orderref, branchattched, brcodelen, countrycode, accnumleng, cid,
									cidbasedon, mailalertreq, smsalertreq, accttypelength, acctsubtypelength,
									login_retry_cnt, pin_retry_cnt, preference, maxaddoncards, maxaddonaccounts,
									pcadssenble, seqkeyvalue, currencytype, authcode, usercode, renewalperiods,
									cardtypelength);
							enctrace("query_insrt_inst : " + query_insrt_inst);
							result_insrt_inst = jdbctemplate.update(query_insrt_inst);
							trace("Got result_insrt_inst : " + result_insrt_inst);
						}
						trace("Inserting temp institution...");

						String query_insrt_temp_inst = instdao.insertInstitutionInTempTable(instid, instname,
								deploymenttype, orderref, branchattched, brcodelen, countrycode, accnumleng, cid,
								cidbasedon, mailalertreq, smsalertreq, accttypelength, acctsubtypelength,
								login_retry_cnt, pin_retry_cnt, preference, maxaddoncards, maxaddonaccounts,
								pcadssenble, seqkeyvalue, currencytype, authcode, usercode, renewalperiods,
								cardtypelength);
						enctrace("query_insrt_inst : " + query_insrt_temp_inst);
						int result_insrt_inst_temp = jdbctemplate.update(query_insrt_temp_inst);
						trace("Got resulttemporary_insrt_inst : " + result_insrt_inst_temp);
						trace("Inserting institution currencies..");

						String qry_defcurrency = "insert into INSTITUTION_CURRENCY (INST_ID,CUR_CODE,CUR_CONF_DATE, PREFERENCE)"
								+ "values('" + instid + "','" + currencytype + "',sysdate, 'P')";
						enctrace("qry_defcurrency : " + qry_defcurrency);
						// System.out.println (" Currency Code --->"
						// +qry_defcurrency);
						int insertedcur = jdbctemplate.update(qry_defcurrency);
						trace("Got insertedcur : " + insertedcur);*/
						
						
						
						
						
						if (mkrchkr.equals("N")) {
							String query_insrt_inst = instdao.insertInstitutionInMainTable(instid, instname,
									deploymenttype, orderref, branchattched, brcodelen, countrycode, accnumleng, cid,
									cidbasedon, mailalertreq, smsalertreq, accttypelength, acctsubtypelength,
									login_retry_cnt, pin_retry_cnt, preference, maxaddoncards, maxaddonaccounts,
									pcadssenble, seqkeyvalue, currencytype, authcode, usercode, renewalperiods,
									cardtypelength);
							enctrace("query_insrt_inst : " + query_insrt_inst);
							
							result_insrt_inst = jdbctemplate.update(query_insrt_inst,new Object[]{instid.toUpperCase(),instname.toUpperCase(),deploymenttype,deploymenttype,"0",
									preference,branchattched,"16","","",
									login_retry_cnt,pin_retry_cnt,brcodelen,date.getCurrentDate(),currencytype,
									countrycode,orderref,cid,cidbasedon,accnumleng,"1",
									"N","N","1",mailalertreq,smsalertreq,
									"N","10","1","1","1",
									"",date.getCurrentDate(),usercode,authcode,date.getCurrentDate(),
									usercode,"N","N","N","N",
									"N","N","6",cardtypelength,"N",
									accttypelength,acctsubtypelength,maxaddoncards,maxaddonaccounts,pcadssenble,
									seqkeyvalue,renewalperiods });
							
							trace("Got result_insrt_inst : " + result_insrt_inst);
							
						}
						trace("Inserting temp institution...");

						String query_insrt_temp_inst = instdao.insertInstitutionInTempTable(instid, instname,
								deploymenttype, orderref, branchattched, brcodelen, countrycode, accnumleng, cid,
								cidbasedon, mailalertreq, smsalertreq, accttypelength, acctsubtypelength,
								login_retry_cnt, pin_retry_cnt, preference, maxaddoncards, maxaddonaccounts,
								pcadssenble, seqkeyvalue, currencytype, authcode, usercode, renewalperiods,
								cardtypelength);
						enctrace("query_insrt_inst : " + query_insrt_temp_inst);
						int result_insrt_inst_temp = jdbctemplate.update(query_insrt_temp_inst,new Object[]{instid.toUpperCase(),instname.toUpperCase(),deploymenttype,deploymenttype,"0" ,
								preference,branchattched,"16","","",
								login_retry_cnt,pin_retry_cnt,brcodelen,date.getCurrentDate(),currencytype,
								countrycode,orderref,cid,cidbasedon,accnumleng,"1","N",
								"N","1",mailalertreq,smsalertreq,"N",
								"10","1","1","1","",
								date.getCurrentDate(),usercode,authcode,date.getCurrentDate(),usercode,
								"N","N","N","N","N",
								"N","8",cardtypelength,"N",accttypelength,acctsubtypelength,
								maxaddoncards,maxaddonaccounts,pcadssenble,seqkeyvalue,renewalperiods});
						trace("Got resulttemporary_insrt_inst : " + result_insrt_inst_temp);
						trace("Inserting institution currencies..");

						/*String qry_defcurrency = "insert into INSTITUTION_CURRENCY (INST_ID,CUR_CODE,CUR_CONF_DATE, PREFERENCE)"
							+ "values('" + instid + "','" + currencytype + "',sysdate, 'P')";
						enctrace("qry_defcurrency : " + qry_defcurrency);
						// System.out.println (" Currency Code --->"
						// +qry_defcurrency);
						int insertedcur = jdbctemplate.update(qry_defcurrency);
						trace("Got insertedcur : " + insertedcur);*/
						
						//by gowtham-130819
						String qry_defcurrency = "insert into INSTITUTION_CURRENCY (INST_ID,CUR_CODE,CUR_CONF_DATE, PREFERENCE)"
								+ "values(?,?,?,?)";
							enctrace("qry_defcurrency : " + qry_defcurrency);
							int insertedcur = jdbctemplate.update(qry_defcurrency,new Object[]{instid,currencytype,date.getCurrentDate(),"P"});
							trace("Got insertedcur : " + insertedcur);
						
						
						
						
						
						if (insertedcur < 0) {
							transact.txManager.rollback(transact.status);
							trace("Institution got rolled back ");
							// System.out.println("TXN NOT COMMITED");
							trace("Could not add primary currency...");
							session.setAttribute("addInstErrormsg", "COULD NOT ADD PRIMARY CURRENCY.");
							session.setAttribute("addInstErrorStatus", "E");
							return "SucessInstAdd";
						}

						ArrayList<String> Add_secCurrency = new ArrayList<String>();
						String sel_currency_code[] = (getRequest().getParameterValues("seccurrency"));

						int i = 0, ccyinsert_status = 0;
						int allccy_inert = 0;
						if (sel_currency_code != null) {
							for (i = 0; i < sel_currency_code.length; i++) {
								int ccy_insert = 0;
								String secondary = "S" + ccy_insert + 1;
								String curcode = sel_currency_code[i];
								System.out.println("selected currency code is __" + curcode);
								trace("Inserting institution currency...");
								
								
								/*String query_curntcy_inst = "insert into INSTITUTION_CURRENCY (INST_ID,CUR_CODE,CUR_CONF_DATE, PREFERENCE)"
										+ "values('" + instid + "','" + curcode + "',sysdate, '" + secondary + "')";
								enctrace("query_curntcy_inst : " + query_curntcy_inst);
								System.out.println(" Currency Code --->" + Add_secCurrency);
								trace("query_curntcy_inst : " + query_curntcy_inst);
								ccy_insert = jdbctemplate.update(query_curntcy_inst);
								trace("Got ccy_insert : " + ccy_insert);*/
								
								//by gowtham-130819
								String query_curntcy_inst = "insert into INSTITUTION_CURRENCY (INST_ID,CUR_CODE,CUR_CONF_DATE, PREFERENCE)"
									+ "values(?,?,?,?)";
									enctrace("query_curntcy_inst : " + query_curntcy_inst);
									System.out.println(" Currency Code --->" + Add_secCurrency);
									trace("query_curntcy_inst : " + query_curntcy_inst);
									ccy_insert = jdbctemplate.update(query_curntcy_inst,new Object[]{instid,curcode,date.getCurrentDate(),secondary});
									trace("Got ccy_insert : " + ccy_insert);
								
								
								if (ccy_insert == 1) {
									allccy_inert = allccy_inert + 1;
								}
							}
						}
						trace("i value====> " + i + " allccy_inert====> " + allccy_inert);
						System.out.println("i value====> " + i + " allccy_inert====> " + allccy_inert);
						if (i == allccy_inert) {
							ccyinsert_status = 1;
						}
						System.out.println("ccyinsert_status=====> " + ccyinsert_status);
						trace("ccyinsert_status=====> " + ccyinsert_status);

						trace("Inserting...dependencies.....");
						int insertdepends = instdao.afterInsertTheInsutition(instid, jdbctemplate);
						trace("Inserting...dependencies.....got..." + insertdepends);

						if (result_insrt_branch == 1 && result_insrt_inst == 1 && result_insrt_inst_temp == 1
								&& ccyinsert_status == 1) {
							transact.txManager.commit(transact.status);
							trace("Added institution committed succesfully");
							trace("Institution  " + instname.toUpperCase() + " Added Successfully ");
							/*
							 * session.setAttribute("addInstErrormsg",
							 * "Institution  "+instname.toUpperCase()+
							 * " Added Successfully ");
							 * session.setAttribute("addInstErrorStatus","S");
							 */
							session.setAttribute("curerr", "S");
							session.setAttribute("curmsg", "Institution  " + instname.toUpperCase()
									+ " Added Successfully Waiting for Authorization ...");

							/************* AUDIT BLOCK **************/
							try {
								// added by gowtham_220719
								trace("ip address======>  " + ip);
								auditbean.setIpAdress(ip);

								auditbean
										.setActmsg(" Institution  " + instname.toUpperCase() + " Added Successfully  ");
								auditbean.setUsercode(usercode);
								auditbean.setAuditactcode("30");
								commondesc.insertAuditTrail("", usercode, auditbean, jdbctemplate, txManager);
							} catch (Exception audite) {
							}
							/************* AUDIT BLOCK **************/

						} else {
							System.out.println("TXN NOT COMMITED");
							transact.txManager.rollback(transact.status);
							trace("Insititution got rolled back");
							/*
							 * session.setAttribute("addInstErrormsg",
							 * "Instituion  "+instname.toUpperCase()+
							 * " Not Added ");
							 * session.setAttribute("addInstErrorStatus","E");
							 */
							session.setAttribute("curerr", "E");
							session.setAttribute("curmsg", "Instituion  " + instname.toUpperCase() + " Not Added ");
						}

					}
				}

			}

			else {
				trace("You Dont Have License to Add More Institution of Deployement Type: ' " + deploymenttype + "'");
				session.setAttribute("addInstErrormsg",
						"You Dont Have License to Add More Institution of Deployement Type: ' " + deploymenttype + "'");
				session.setAttribute("addInstErrorStatus", "E");
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("TXN NOT COMMITED" + e.getMessage());
			transact.txManager.rollback(transact.status);
			trace("Exception : got rolledback");
			trace("Error While Adding Institution " + instname.toUpperCase() + " Error" + e.fillInStackTrace());
			session.setAttribute("addInstErrormsg", "Error While Adding Institution " + instname.toUpperCase() + " ");
			session.setAttribute("addInstErrorStatus", "E");
		}

		trace("*********ADDING INSTITUTION END************\n\n");
		enctrace("*********ADDING INSTITUTION END************\n\n");
		return "SucessInstAdd";
	}

	public String flag_status;

	public String getFlag_status() {
		return flag_status;
	}

	public void setFlag_status(String flag_status) {
		this.flag_status = flag_status;
	}

	public String viewInst() {
		trace("*****view inistitution*********************");
		enctrace("*****view inistitution*********************");

		HttpSession session = getRequest().getSession();
		String status = "view";
		setFlag_status(status);
		int instconfig = commondesc.reqCheck().reqCheckStaticTable(session, jdbctemplate);
		if (instconfig <= 0) {
			return "required_home";
		}

		trace("Getting institution records...");

		instlist = instdao.getListOfInstitution("$VIEW", jdbctemplate);
		trace("Got instlist : " + instlist);
		System.out.println(instlist);
		if (instlist.isEmpty()) {
			trace("No institution configured...");
			session.setAttribute("curmsg", "Please Add Institution To View");
			session.setAttribute("curerr", "E");
		}
		return "viewInst";
	}

	public String instDetals() {
		System.out.println("QUERY------------|>");
		String inst;
		List ifpinst_result;
		List ifpinstcurrency_result;
		List defaultCurrency_result;
		inst = (getRequest().getParameter("instid"));
		System.out.println(inst);
		HttpSession session = getRequest().getSession();
		try {

			trace("Getting institution data...");

			ifpinst_result = instdao.getInstitutionDetails(inst, jdbctemplate);
			trace("Got ifpinst_result : " + ifpinst_result);
			if (ifpinst_result.isEmpty()) {
				trace("No Details found");
				session.setAttribute("curmsg", "No Details Found ");
				session.setAttribute("curerr", "E");
				return "required_home";
			}
			Iterator itr = ifpinst_result.iterator();
			String basecur = null;
			while (itr.hasNext()) {
				Map temp = (Map) itr.next();
				basecur = (String) temp.get("BASE_CURRENCY");
				System.out.println("FROM TABLE ----> " + basecur);
				trace("Getting currency description for the curcode [ " + basecur + " ] ...");
				String ccy_desc = getbaseCCYdesc(basecur, "NUMERIC_CODE", jdbctemplate);
				trace("Got ccy_desc " + ccy_desc);
				temp.put("BASECCY", ccy_desc);
			}
			setInstitutiondetail(ifpinst_result);
			System.out.println("INSTITUTION QUERY------------|>" + ifpinst_result);

			// For Secondary Currency Select Box
			trace("Getting currency codes...");
			
		/*	String qury_ifpinstitutioncurrency = "select CUR_CODE from INSTITUTION_CURRENCY where INST_ID='" + inst
					+ "' ";// AND CUR_CODE!='"+basecur+"'";
			enctrace("qury_ifpinstitutioncurrency : " + qury_ifpinstitutioncurrency);
			ifpinstcurrency_result = jdbctemplate.queryForList(qury_ifpinstitutioncurrency);*/
			
			
			String qury_ifpinstitutioncurrency = "select CUR_CODE from INSTITUTION_CURRENCY where INST_ID=? ";// AND CUR_CODE!='"+basecur+"'";
			enctrace("qury_ifpinstitutioncurrency : " + qury_ifpinstitutioncurrency);
			ifpinstcurrency_result = jdbctemplate.queryForList(qury_ifpinstitutioncurrency,new Object[]{inst});
			
			trace("Got ifpinstcurrency_result : " + ifpinstcurrency_result);
			// System.out.println("SEcONDRY CCCY
			// BEFORE"+ifpinstcurrency_result);
			if (ifpinstcurrency_result.isEmpty()) {
				trace("No currency details found...");
				session.setAttribute("curmsg", "No Details Found ");
				session.setAttribute("curerr", "E");
				return "required_home";
			}
			Iterator sec_cur_itr = ifpinstcurrency_result.iterator();
			while (sec_cur_itr.hasNext()) {
				Map ccymap = (Map) sec_cur_itr.next();
				String ccycode = (String) ccymap.get("CUR_CODE");
				String desc_code = getbaseCCYdesc(ccycode, "NUMERIC_CODE", jdbctemplate);
				ccymap.put("CUR_DESC", desc_code);
			}
			System.out.println("SEcONDRY CCCY AFTER" + ifpinstcurrency_result);
			setCurrency(ifpinstcurrency_result);
		} catch (Exception e) {
			trace("Error while fetching values from INSTITUTION" + e.getMessage());
			addActionError("Error while getting institution data");
		}
		return "instdetals";
	}

	public String getbaseCCYdesc(String basecur, String field, JdbcTemplate jdbctemplate) {
		String desc = "X";
		
		/*String desc_qury = "select CURRENCY_DESC from GLOBAL_CURRENCY where " + field + "='" + basecur + "'";
		enctrace("Currency description desc_qury  : " + desc_qury);
		System.out.println("desc_qury===> " + desc_qury);
		desc = (String) jdbctemplate.queryForObject(desc_qury, String.class);*/
		
		
		//by gowtham 180919
				String desc_qury = "select CURRENCY_DESC from GLOBAL_CURRENCY where NUMERIC_CODE=?";
				enctrace("Currency description desc_qury  : " + desc_qury);
				System.out.println("desc_qury===> " + desc_qury);
				desc = (String) jdbctemplate.queryForObject(desc_qury,new Object[]{basecur}, String.class);
		
		return desc;
	}

	public void ajax_InstLicence_Handler() throws IOException {
		HttpSession session = getRequest().getSession();
		String ins = getRequest().getParameter("inst_id");
		System.out.println("  Institution Is   " + ins);
		trace("Institution Is   " + ins);
		Licensemanager licencemgr = new Licensemanager();
		trace("Checking the licence...");
		String inst_licence_result = licencemgr.checkfile(ins.toUpperCase(), commondesc);
		trace("Got inst_licence_result " + inst_licence_result);
		System.out.println("Check FIle Returns " + inst_licence_result);
		if (inst_licence_result.equals("matched")) {
			System.out.println("Matched");
			session.setAttribute("license_status", "Y");
			trace("License File Exists");
			getResponse().getWriter().write("");
		} else {
			System.out.println("Not Matched");
			session.setAttribute("license_status", "N");
			trace("Licence Not Available For This Institution");
			getResponse().getWriter().write("Licence Not Available For This Institution");
			System.out.println("Return ajax");
		}
	}

	public void getsecurityKey() throws IOException {
		List seqkeylist = instdao.getSeqKeyList(jdbctemplate);
		System.out.println(seqkeylist);
		String opt = "<select id = 'seqkeyvalue' name='seqkeyvalue' > <option value='-1'>-SELECT KEY-</option>";

		if (!seqkeylist.isEmpty()) {
			Iterator itr = seqkeylist.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String keyid = (String) mp.get("KEYID");
				String keydesc = (String) mp.get("KEYDESC");
				opt += "<option value=" + keyid + ">" + keydesc + "</option>";
			}
		} else {

		}

		getResponse().getWriter().write(opt + "</select>");
	}

	private List subcurrencylist;

	public List getSubcurrencylist() {
		return subcurrencylist;
	}

	public void setSubcurrencylist(List subcurrencylist) {
		this.subcurrencylist = subcurrencylist;
	}

	// To retrive checkbox from database
	public void ajax_default_currency() throws IOException {
		trace("**********Getting the default currency value **********");
		enctrace("**********Getting the default currency value **********");
		System.out.println("default");
		HttpSession session = getRequest().getSession();
		String currency = getRequest().getParameter("default_curr");
		String checkbox = "", chkbox;
		if (currency.equals("-1")) {
			System.out.println("Currency is not selected");
			// String sel_qury="select * from GLOBAL_CURRENCY where
			// CURRENCY_CODE='"+currency+"'";
			getResponse().getWriter().write(checkbox);
		} else {
			trace("Default currency is : " + currency);
			trace("Getting the global currency values ");
			
			/*String sel_qury = "select * from GLOBAL_CURRENCY where NUMERIC_CODE!='" + currency
					+ "' and CURRENCY_STATUS='1'";
			enctrace("sel_qury : " + sel_qury);
			List selectlist = jdbctemplate.queryForList(sel_qury);*/
			
			//by gowtham-170819
			String sel_qury = "select * from GLOBAL_CURRENCY where NUMERIC_CODE!=? and CURRENCY_STATUS=?";
			enctrace("sel_qury : " + sel_qury);
			List selectlist = jdbctemplate.queryForList(sel_qury,new Object[]{currency,"1"});
			
			trace("Got: " + selectlist);
			System.out.println("List From DB===>" + selectlist);
			String tablers = "";
			if (!(selectlist.isEmpty())) {
				int i = 0, j = 0;
				Iterator ccyItr = selectlist.iterator();

				tablers = "<table border='1' cellpadding='0' cellspacing='0' width='100%' rules='none' frame='box'>";
				while (ccyItr.hasNext()) {
					System.out.println("i++--->" + i);
					Map ccymap = (Map) ccyItr.next();
					String ccydesc = (String) ccymap.get("CURRENCY_DESC");
					String ccycode = (String) ccymap.get("CURRENCY_CODE");
					String numcode = (String) ccymap.get("NUMERIC_CODE");
					if (i == 0) {
						System.out.println("Test ----> 1");
						tablers = tablers + "<tr>";
					}
					tablers = tablers + "<td><input type=\"checkbox\" name=\"seccurrency\" id=\"sec_currency" + j
							+ "\" value=\"" + numcode + "\"/>" + ccydesc + "</td>";
					i++;
					if (i == 3) {
						System.out.println("Test ----> 2");
						i = 0;
						tablers = tablers + "</tr>";
					}
					j++;
				}
				tablers = tablers + "</table>";
				getResponse().getWriter().write(tablers);
			}
			System.out.println("####################tablers     " + tablers);
		}
	}

	private List hsmlist;
	private List institutionlist;

	public List getInstitutionlist() {
		return institutionlist;
	}

	public void setInstitutionlist(List institutionlist) {
		this.institutionlist = institutionlist;
	}

	public List getHsmlist() {
		return hsmlist;
	}

	public void setHsmlist(List hsmlist) {
		this.hsmlist = hsmlist;
	}

	public String mapHsm() {
		trace("**********Mapping hsm begin**********");
		enctrace("**********Mapping hsm begin**********");

		HttpSession session = getRequest().getSession();
		String instid = this.comInstId(session);
		int hsmcnt = commondesc.reqCheck().reqCheckHsmMap(instid, session, jdbctemplate);
		if (hsmcnt < 0) {
			return "required_home";
		}
		trace("Getting Institution name and id...");
		List instlist = instdao.getListOfInstitution("$AUTHORIZED", jdbctemplate);

		trace("Got instlist : " + instlist);
		if (!instlist.isEmpty()) {
			setInstitutionlist(instlist);
		}

		trace("Getting hsm list ");
		List hsmlist = this.hsmList(jdbctemplate);
		trace("Got hsmlist  : " + hsmlist);

		if (!hsmlist.isEmpty()) {
			setHsmlist(hsmlist);
		}

		trace("**********Mapping hsm end**********\n\n");
		enctrace("**********Mapping hsm end**********\n\n");
		return "insthsmmap_home";
	}

	public List hsmList(JdbcTemplate jdbctemplate) {
		
		/*String hsmlistqry = "SELECT HSMNAME, HSM_ID FROM HSM_DETAILS WHERE AUTH_CODE='1' AND HSMSTATUS='1'";
		enctrace("hsmlistqry  : " + hsmlistqry);
		List hsmlist = jdbctemplate.queryForList(hsmlistqry);*/
		
		//by gowtham-170819
				String hsmlistqry = "SELECT HSMNAME, HSM_ID FROM HSM_DETAILS WHERE AUTH_CODE=? AND HSMSTATUS=?";
				enctrace("hsmlistqry  : " + hsmlistqry);
				List hsmlist = jdbctemplate.queryForList(hsmlistqry,new Object[]{"1","1"});
		
		return hsmlist;
	}

	public int hsmExist(String instid, String hsmid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		
		/*String hsmexist = "SELECT COUNT(*) FROM INST_HSM_MAP WHERE INST_ID='" + instid + "' AND HSM_ID='" + hsmid + "'";
		enctrace("hsmexist :" + hsmexist);
		x = jdbctemplate.queryForInt(hsmexist);*/
		
		
		//by gowtham-170819
				String hsmexist = "SELECT COUNT(*) FROM INST_HSM_MAP WHERE INST_ID=? AND HSM_ID=?";
				enctrace("hsmexist :" + hsmexist);
				x = jdbctemplate.queryForInt(hsmexist,new Object[]{instid,hsmid});	
		
		return x;
	}

	public String saveHsmMapping() {
		trace("**********save hsm mapping begins **********");
		enctrace("**********save hsm mapping begins**********");

		HttpSession session = getRequest().getSession();
		String instid = getRequest().getParameter("instidlist");
		String[] hsmlist = getRequest().getParameterValues("hsm");
		String usercode = comAdminUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("SAVEHSM", txManager);
		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String[] hsmid_name = null;
		try {
			trace("hsm list : " + hsmlist);

			if (hsmlist != null) {
				Boolean mapped = false;
				int priority = 0, i = 0;
				for (i = 0; i < hsmlist.length; i++) {
					String hsmid = hsmlist[i];
					int hsmmap_insert = 0;
					System.out.println("hsmid===> " + hsmid);
					hsmid_name = hsmid.split("~");

					trace("Hsmid ===> " + hsmid_name[0] + " Name===> " + hsmid_name[1]);

					int hsmexist = this.hsmExist(instid, hsmid_name[0], jdbctemplate);
					trace("Checking hsm [ " + hsmid + " ] confiured for the instid [ " + instid + " ]...got :  "
							+ hsmexist);
					if (hsmexist > 0) {
						continue;
					}

					trace("Inserting hsm mapping");
					
				/*	String mapqry = "INSERT INTO INST_HSM_MAP (INST_ID,HSM_ID,PRIORITY,HSMNAME) VALUES";
					mapqry += "('" + instid + "','" + hsmid_name[0] + "','" + priority + "','" + hsmid_name[1] + "')";
					enctrace("mapqry_" + mapqry);
					hsmmap_insert = jdbctemplate.update(mapqry);
					*/
					
					//by gowtham-170819
					String mapqry = "INSERT INTO INST_HSM_MAP (INST_ID,HSM_ID,PRIORITY,HSMNAME) VALUES";
					mapqry += "(?,?,?,?)";
					enctrace("mapqry_" + mapqry);
					hsmmap_insert = jdbctemplate.update(mapqry,new Object[]{instid,hsmid_name[0],priority,hsmid_name[1]});
					
					trace("Inserting hsm Got hsmmap_insert : " + hsmmap_insert);
					
					
					if (hsmmap_insert == 1) {
						priority = priority + 1;
					} else {

						trace("Breaking THE LOOP");
						break;
					}

				}
				trace("Priority ===> " + priority);

				if (i == priority) {
					// transact.txManager.commit(transact.status);
					transact.txManager.commit(transact.status);
					trace("Committed successfully");
					addActionMessage("HSM MAPPED SUCCESSFULLY");
					trace("Hsm mapped successfully");

					/************* AUDIT BLOCK **************/
					try {
						// added by gowtham_220719
						trace("ip address======>  " + ip);
						auditbean.setIpAdress(ip);
						auditbean.setActmsg(
								"HSM " + hsmid_name[1] + " Mapped to the Institution " + instid + " Successfully");
						auditbean.setUsercode(usercode);
						auditbean.setAuditactcode("30");
						commondesc.insertAuditTrail("", usercode, auditbean, jdbctemplate, txManager);
					} catch (Exception audite) {
					}
					/************* AUDIT BLOCK **************/

				} else {
					// transact.txManager.rollback(transact.status);
					transact.txManager.rollback(transact.status);
					trace("hsm mapping to rollbacked");
					addActionError("COULD NOT MAP HSM. ");
					trace("COULD NOT MAP HSM.");
				}
			}
		} catch (Exception e) {
			// transact.txManager.rollback(transact.status);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			addActionError("Exception : Unable to map hsm with institution");
			e.printStackTrace();
		}
		trace("**********save hsm mapping end **********\n\n");
		enctrace("**********save hsm mapping end**********\n\n");
		return this.mapHsm();
	}

	public String editInst() {
		trace("*****edit inistitution*********************");
		enctrace("*****edit inistitution*********************");

		HttpSession session = getRequest().getSession();
		String usercode = comAdminUserCode(session);
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

			instlist = instdao.getListOfInstitution("$EDIT", jdbctemplate);
			trace("Got instlist : " + instlist);
			System.out.println(instlist);

			if (instlist.isEmpty()) {
				trace("No institution configured...");
				session.setAttribute("curmsg", "No institution available for edit");
				session.setAttribute("curerr", "E");
				return "required_home";
			}
		} catch (Exception e) {
			trace("No institution configured...");
			session.setAttribute("curmsg", "Exception ....could not continue..." + e.getMessage());
			session.setAttribute("curerr", "E");
		}
		int instconfig = commondesc.reqCheck().reqCheckStaticTable(session, jdbctemplate);
		if (instconfig <= 0) {
			return "required_home";
		}
		return "editInst";
	}

	public String authInst() {
		trace("***** auth inistitution*********************");
		enctrace("***** auth inistitution*********************");

		HttpSession session = getRequest().getSession();
		String status = "auth";
		setFlag_status(status);
		String usercode = comAdminUserCode(session);
		try {
			System.out.println("usercode --- " + usercode);
			int instconfig = commondesc.reqCheck().reqCheckStaticTable(session, jdbctemplate);
			if (instconfig <= 0) {
				return "required_home";
			}
			String adminmkrchkr = commondesc.getAdminMkrChkrStatus(jdbctemplate);
			System.out.println("adminmkrchkr -- " + adminmkrchkr);
			if (adminmkrchkr != null) {
				String mkrchkr_status;
				if (adminmkrchkr.equals("Y")) {
					mkrchkr_status = "Y";
				} else {
					mkrchkr_status = "N";
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", " No records found ");
					return "required_home";
				}
				beans.setMkrchkrstatus(mkrchkr_status);
			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " Configure Admin Maker Checker status");
				return "required_home";
			}
			trace("Getting institution records...");
			
			/*String qury = "select INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authorized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS  from INSTITUTION where AUTH_CODE=0 and ADDED_BY!='"
					+ usercode + "' order by PREFERENCE";
			enctrace("qury : " + qury);
			instlist = jdbctemplate.queryForList(qury);*/
			
			
			//by gowtham-130819
			String qury = "select INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authorized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS  from INSTITUTION"
					+ " where AUTH_CODE=?and ADDED_BY!=? order by PREFERENCE";
			enctrace("qury : " + qury);
			instlist = jdbctemplate.queryForList(qury,new Object[]{"0",usercode});	
			
			trace("Got instlist : " + instlist);
			System.out.println(instlist);
			if (instlist.isEmpty()) {
				trace("No institution configured...");
				session.setAttribute("curmsg", "No institution available for authorization");
				session.setAttribute("curerr", "E");
				return "required_home";
			}
		} catch (Exception e) {
			trace("Exception...." + e.getMessage());
			session.setAttribute("curmsg", "Exception...could not continue..." + e.getMessage());
			session.setAttribute("curerr", "E");
		}
		return "viewInst";
	}

	public String editinstDetals() {
		System.out.println("QUERY------------|>");
		List ifpinst_result;
		List ifpinstcurrency_result;
		List defaultCurrency_result;
		String inst = (getRequest().getParameter("instid"));
		setInstid(inst);
		System.out.println(inst);
		HttpSession session = getRequest().getSession();
		String mkrchkr = getRequest().getParameter("mkrchkr");
		System.out.println("mkrchkr   ---- " + mkrchkr);
		beans.setMkrchkrstatus(mkrchkr);
		try {

			trace("Getting institution data...");
			/*String qury_ifpinstitution = "select * from INSTITUTION where INST_ID='" + inst + "'";
			enctrace("qury_ifpinstitution : " + qury_ifpinstitution);
			ifpinst_result = jdbctemplate.queryForList(qury_ifpinstitution);
			trace("Got ifpinst_result : " + ifpinst_result);*/
			
			//by gowtham-130819
			String qury_ifpinstitution = "select * from INSTITUTION where INST_ID=?";
			enctrace("qury_ifpinstitution : " + qury_ifpinstitution);
			ifpinst_result = jdbctemplate.queryForList(qury_ifpinstitution,new Object[]{inst});
			setInstitutiondetail(ifpinst_result);
			System.out.println("INSTITUTION QUERY------------|>" + ifpinst_result);
		} catch (Exception e) {
			trace("Error while fetching values from INSTITUTION" + e.getMessage());
			addActionError("Error while getting institution data");
		}
		return "editinstdetals";
	}

	public String editInstValues() {
		IfpTransObj transact = commondesc.myTranObject("EDITINST", txManager);
		HttpSession session = getRequest().getSession();
		String instname = getRequest().getParameter("instname");
		String instid = getRequest().getParameter("instid");
		String countrycode = getRequest().getParameter("countrycode");
		String mailalertreq = getRequest().getParameter("mailalertreq");
		String smsalertreq = getRequest().getParameter("smsalertreq");
		String status = getRequest().getParameter("inststatus");
		String usercode = comAdminUserCode(session);
		System.out.println("usercode --- " + usercode);
		String mkrchkr = getRequest().getParameter("mkrchkr");
		System.out.println("mkrchkr   ---- " + mkrchkr);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
Date date=new Date();
		try {

			trace("Updating institution...");
			String authcode, errormsg;
			int result_update_inst = 1;
			if (mkrchkr.equals("N")) {
				authcode = "1";
				errormsg = "Institution Details Updated successfully";
			} else {
				authcode = "0";
				errormsg = "Institution edited successfully.Waiting for authorization";
			}
			if (mkrchkr.equals("N")) {
				
				/*String query_update_inst = instdao.updateInstitutionInMainTable(instid, instname, countrycode,
						mailalertreq, smsalertreq, status, authcode, usercode);
				enctrace("query_insrt_inst : " + query_update_inst);
				result_update_inst = jdbctemplate.update(query_update_inst);
				trace("Got result_insrt_inst : " + result_update_inst);*/
				
				//by gowtham-170819
				String query_update_inst = instdao.updateInstitutionInMainTable(instid, instname, countrycode,
						mailalertreq, smsalertreq, status, authcode, usercode);
				enctrace("query_insrt_inst : " + query_update_inst);
				result_update_inst = jdbctemplate.update(query_update_inst,new Object[]{instname,countrycode,mailalertreq,smsalertreq,status,authcode,usercode,date.getCurrentDate(),instid});
				trace("Got result_insrt_inst : " + result_update_inst);
			}
			trace("Updating temp institution...");
			String query_update_temp_inst = instdao.updateInstitutionInTempTable(instid, instname, countrycode,
					mailalertreq, smsalertreq, status, authcode, usercode);
			enctrace("query_update_inst : " + query_update_temp_inst);
			/*int result_update_inst_temp = jdbctemplate.update(query_update_temp_inst);*/
			
			int result_update_inst_temp = jdbctemplate.update(query_update_temp_inst,new Object[]{instname,countrycode,mailalertreq,smsalertreq,status,authcode,usercode,date.getCurrentDate(),"","","",instid});
			trace("Got resulttemporary_update_inst : " + result_update_inst_temp);

			if (result_update_inst > 0 && result_update_inst_temp > 0) {
				transact.txManager.commit(transact.status);
				addActionError("" + errormsg + "");

				/************* AUDIT BLOCK **************/
				try {

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg(instname.toUpperCase() + " " + errormsg);
					auditbean.setUsercode(usercode);
					auditbean.setAuditactcode("30");
					commondesc.insertAuditTrail("", usercode, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
				}
				/************* AUDIT BLOCK **************/

			} else {
				transact.txManager.rollback(transact.status);
				addActionError("ERROR WHILE UPDATING VALUES.. ");
				return editinstDetals();
			}
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" ERROR WHILE UPDATING VALUES.. ");
			e.printStackTrace();
		}
		return editInst();
	}

	public String authinstDetals() {
		System.out.println("AUTH QUERY------------|>");
		String inst;
		List ifpinst_result;
		List ifpinstcurrency_result;
		List defaultCurrency_result;
		inst = (getRequest().getParameter("instid"));
		System.out.println(inst);
		HttpSession session = getRequest().getSession();
		try {

			/*trace("Getting institution data...");
			String qury_ifpinstitution = "select INST_ID,INST_NAME,DEPLOY_ID,DEPLOYMENT_TYPE,BIN_COUNT,PREFERENCE,DECODE(BRANCHATTCHED,'Y','YES','N','NO') as BRANCHATTCHED,CHNLEN,ATTACH_BRCODE_CARDTYPE,BASELEN,LOGIN_RETRY_CNT,PIN_RETRY_CNT,BRCODELEN,INST_ADDDATE,BASE_CURRENCY,COUNTRY_CODE,ORD_REF_LEN,CIN_LEN,ACCT_LEN,ACCT_SEQ_NO,DECODE(GL_CODE_GEN,'AUTO','AUTOMATIC','MAN','MANUAL') AS GL_CODE_GEN,INVOICE_TRACE,INVOICE_LEN,DECODE(MAIL_ALERT_REQ,'N','NO','Y','YES') AS MAIL_ALERT_REQ,DECODE(SMS_ALERT_REQ,'N','NO','Y','YES') AS SMS_ALERT_REQ,PHOTOUPLOAD_REQ,CARDREFNO_LEN,PREDISPLAYDAYS,APP_CODE,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS,STATUS,ACCOUNT_TYPE_LENGTH,ACCTSUBTYPE_LENGTH,CUSTID_BASEDON,MAXALWD_ADDCARD, MAXALWD_ADDACC, DECODE(PADSS_ENABLE,'Y','YES','N','NO')PADSS_ENABLE,PADSS_ENABLE PADSS_ENABLEVALUE,(SELECT KEYDESC FROM PADSSKEY WHERE KEYID=PADSS_KEY)PADSS_KEY,RENEWAL_PERIODS from INSTITUTION where INST_ID='"
					+ inst + "'";
			enctrace("qury_ifpinstitution : " + qury_ifpinstitution);
			ifpinst_result = jdbctemplate.queryForList(qury_ifpinstitution);
			trace("Got ifpinst_result : " + ifpinst_result);*/
			
			
			//by gowtham-170819
			String qury_ifpinstitution = "select INST_ID,INST_NAME,DEPLOY_ID,DEPLOYMENT_TYPE,BIN_COUNT,PREFERENCE,DECODE(BRANCHATTCHED,'Y','YES','N','NO') as BRANCHATTCHED,CHNLEN,ATTACH_BRCODE_CARDTYPE,BASELEN,LOGIN_RETRY_CNT,PIN_RETRY_CNT,BRCODELEN,INST_ADDDATE,BASE_CURRENCY,COUNTRY_CODE,ORD_REF_LEN,CIN_LEN,ACCT_LEN,ACCT_SEQ_NO,DECODE(GL_CODE_GEN,'AUTO','AUTOMATIC','MAN','MANUAL') AS GL_CODE_GEN,INVOICE_TRACE,INVOICE_LEN,DECODE(MAIL_ALERT_REQ,'N','NO','Y','YES') AS MAIL_ALERT_REQ,DECODE(SMS_ALERT_REQ,'N','NO','Y','YES') AS SMS_ALERT_REQ,PHOTOUPLOAD_REQ,CARDREFNO_LEN,PREDISPLAYDAYS,APP_CODE,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS,STATUS,ACCOUNT_TYPE_LENGTH,ACCTSUBTYPE_LENGTH,CUSTID_BASEDON,MAXALWD_ADDCARD, MAXALWD_ADDACC, DECODE(PADSS_ENABLE,'Y','YES','N','NO')PADSS_ENABLE,PADSS_ENABLE PADSS_ENABLEVALUE,(SELECT KEYDESC FROM PADSSKEY WHERE KEYID=PADSS_KEY)PADSS_KEY,RENEWAL_PERIODS from INSTITUTION where INST_ID=?";
			enctrace("qury_ifpinstitution : " + qury_ifpinstitution);
			ifpinst_result = jdbctemplate.queryForList(qury_ifpinstitution,new Object[]{instid});
			if (ifpinst_result.isEmpty()) {
				trace("No Details found");
				session.setAttribute("curmsg", "No Details Found ");
				session.setAttribute("curerr", "E");
				return "required_home";
			}
			Iterator itr = ifpinst_result.iterator();
			String basecur = null;
			while (itr.hasNext()) {
				Map temp = (Map) itr.next();
				basecur = (String) temp.get("BASE_CURRENCY");
				System.out.println("FROM TABLE ----> " + basecur);
				trace("Getting currency description for the curcode [ " + basecur + " ] ...");
				String ccy_desc = getbaseCCYdesc(basecur, "NUMERIC_CODE", jdbctemplate);
				trace("Got ccy_desc " + ccy_desc);
				temp.put("BASECCY", ccy_desc);
			}
			setInstitutiondetail(ifpinst_result);
			System.out.println("INSTITUTION QUERY------------|>" + ifpinst_result);

			// For Secondary Currency Select Box
			trace("Getting currency codes...");
			
			/*String qury_ifpinstitutioncurrency = "select CUR_CODE from INSTITUTION_CURRENCY where INST_ID='" + inst
					+ "' AND CUR_CODE='" + basecur + "'";
			enctrace("qury_ifpinstitutioncurrency : " + qury_ifpinstitutioncurrency);
			ifpinstcurrency_result = jdbctemplate.queryForList(qury_ifpinstitutioncurrency);*/
			
			//by gowtham-170819
			String qury_ifpinstitutioncurrency = "select CUR_CODE from INSTITUTION_CURRENCY where INST_ID=? AND CUR_CODE=?";
			enctrace("qury_ifpinstitutioncurrency : " + qury_ifpinstitutioncurrency);
			ifpinstcurrency_result = jdbctemplate.queryForList(qury_ifpinstitutioncurrency,new Object[]{inst,basecur});
			
			trace("Got ifpinstcurrency_result : " + ifpinstcurrency_result);
			// System.out.println("SEcONDRY CCCY
			// BEFORE"+ifpinstcurrency_result);
			if (ifpinstcurrency_result.isEmpty()) {
				trace("No currency details found...");
				session.setAttribute("curmsg", "No Details Found ");
				session.setAttribute("curerr", "E");
				return "required_home";
			}
			Iterator sec_cur_itr = ifpinstcurrency_result.iterator();
			while (sec_cur_itr.hasNext()) {
				Map ccymap = (Map) sec_cur_itr.next();
				String ccycode = (String) ccymap.get("CUR_CODE");
				String desc_code = getbaseCCYdesc(ccycode, "NUMERIC_CODE", jdbctemplate);
				ccymap.put("CUR_DESC", desc_code);
			}
			System.out.println("SEcONDRY CCCY AFTER" + ifpinstcurrency_result);
			setCurrency(ifpinstcurrency_result);
		} catch (Exception e) {
			trace("Error while fetching values from INSTITUTION" + e.getMessage());
			addActionError("Error while getting institution data");
		}
		return "authinstdetals";
	}

	public String authdeauthinstlist() {
		trace("*************Institution authorization *****************");
		enctrace("*************Institution authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHINST", txManager);
		String statusmsg = "";
		String auth = getRequest().getParameter("auth");
		String userid = comAdminUserCode(session);
		String instid = getRequest().getParameter("instid");
		int update_authdeauth = 0;
		String remarks = "";

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		try {
			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				statusmsg = " Authorized ";
				update_authdeauth = instdao.updateAuthInst(userid, instid, "", jdbctemplate);
				trace("Updating auth institution temp status...got : " + update_authdeauth);
				// removed temp table
				/*
				 * update_authdeauth = instdao.deleteProductionInstitute(instid,
				 * jdbctemplate); trace("Deleting institution...got : "
				 * +update_authdeauth ); update_authdeauth =
				 * instdao.insertInstMaintable(instid, jdbctemplate ); trace(
				 * "Moving institution temp to production...got : " +
				 * update_authdeauth );
				 */
			} else {
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				update_authdeauth = instdao.updateDeAuthInst(userid, instid, remarks, jdbctemplate);
				trace("Updating de-auth institution temp status...got : " + update_authdeauth);
			}

			if (update_authdeauth < 0) {
				transact.txManager.rollback(transact.status);
				addActionError("Unable to continue the process ");
				return "required_home";
			}

			transact.txManager.commit(transact.status);
			addActionMessage("Institution \"" + instid + "\"   " + statusmsg + " Successfully");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg(instid + " Institution " + statusmsg + " Successfully");
				auditbean.setUsercode(userid);
				auditbean.setAuditactcode("30");
				auditbean.setRemarks(remarks);
				commondesc.insertAuditTrail("", userid, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("Could not continue the process");
			trace("Error while adding the Institution " + e.getMessage());
			e.printStackTrace();
		}
		return "required_home";

	}

}
