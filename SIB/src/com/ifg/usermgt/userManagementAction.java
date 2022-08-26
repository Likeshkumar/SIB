package com.ifg.usermgt;

import com.ifg.Bean.ServerValidationBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifg.usermgt.LoginAction;
import com.ifp.beans.AuditBeans;
import com.ifg.Config.Institution.addInstitutionActionDAO;
import com.ifg.usermgt.userManagementActionDAO;
import com.ifp.mail.EmailService;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.ifp.util.MailSender;
import com.ifp.util.PasswordHashing;

import test.Date;
import test.Validation;
import com.ifg.Bean.ServerValidationBean;
/**
 * SRNP0003
 * 
 * @author CGSPL
 *
 */
public class userManagementAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	PasswordHashing pswd_hash_class = new PasswordHashing();
	ServerValidationBean bean = new ServerValidationBean();
	public ServerValidationBean getBean() {
		return bean;
	}

	public void setBean(ServerValidationBean bean) {
		this.bean = bean;
	}

	CommonDesc commondesc = new CommonDesc();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();
	addInstitutionActionDAO instdao = new addInstitutionActionDAO();
	private JavaMailSender mailSender;

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public addInstitutionActionDAO getInstdao() {
		return instdao;
	}

	public void setInstdao(addInstitutionActionDAO instdao) {
		this.instdao = instdao;
	}

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
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

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	Boolean issuperadmin = false;
	private String menuid;
	String doact;

	List userlist;

	public String getDoact() {
		return doact;
	}

	public void setDoact(String doact) {
		this.doact = doact;
	}

	public List getUserlist() {
		return userlist;
	}

	public void setUserlist(List userlist) {
		this.userlist = userlist;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	private List profiledetail;

	public List getProfiledetail() {
		return profiledetail;
	}

	public void setProfiledetail(List profiledetail) {
		this.profiledetail = profiledetail;
	}

	private char view_profile;

	public char getView_profile() {
		return view_profile;
	}

	public void setView_profile(char view_profile) {
		this.view_profile = view_profile;
	}

	private String user_mgnt_message;
	static String display = "";

	// private AddSubProductAction addsubProductInstance;
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		userManagementAction.display = display;
	}

	public Boolean getIssuperadmin() {
		return issuperadmin;
	}

	public void setIssuperadmin(Boolean issuperadmin) {
		this.issuperadmin = issuperadmin;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode(HttpSession session) {
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	List forgotuserreq;

	public List getForgotuserreq() {
		return forgotuserreq;
	}

	public void setForgotuserreq(List forgotuserreq) {
		this.forgotuserreq = forgotuserreq;
	}

	List institutionlist;

	public List getInstitutionlist() {
		return institutionlist;
	}

	public void setInstitutionlist(List institutionlist) {
		this.institutionlist = institutionlist;
	}

	private HttpServletResponse response;

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	private List profilename;
	private List branchlist;

	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}

	public List getProfilename() {
		return profilename;
	}

	public void setProfilename(List profilename) {
		this.profilename = profilename;
	}

	String institution;

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String addProfile() {
		// loginbean inst = new loginbean();
		trace("*************** Adding Profile Begins **********");
		enctrace("*************** Adding Profile Begins **********");
		String retunrto = null, branch_req = null;
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");
		// addSessionintrace(dataSource, jdbctemplate, session);
		String usertype = (String) session.getAttribute("USERTYPE");
		trace("User type is : " + usertype);
		String profileflag = getRequest().getParameter("act");
		if (profileflag != null) {
			session.setAttribute("PROFILE_FLAG", profileflag);
		}
		if (usertype.equals("SUPERADMIN")) {

			List institutes = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			trace("institutes.." + institutes.size());
			trace("got institutes");
			// System.out.println("Selected Instituion"+institutes);
			setInstitutionlist(institutes);
			trace("getting institutionlist");
			trace("Institutionlist" + getInstitutionlist().size());
			trace("got institutionlist");
			// System.out.println("setInstitutionlist===>"
			// +getInstitutionlist());
			// retunrto = "addprofile";
			return "SuperAdminAddProfile";
		} else {
			return "InstAddprofile";
		}
	}

	String instname;

	public String getInstname() {
		return instname;
	}

	public void setInstname(String instname) {
		this.instname = instname;
	}

	static String proflist;

	// private String
	// prof_name,prof_desc,inst_name,loginipaddress,loginexpiary,loginretrycount,loginbranchcode,userpswdrpt,usrpswexp;
	int profile_id;
	private String prof_name, prof_desc, inst_name, branch_validation, login_retry, login_ip, user_expdate,
			psswd_reptable, psswd_expdate;

	public String getProf_name() {
		return prof_name;
	}

	public void setProf_name(String prof_name) {
		this.prof_name = prof_name;
	}

	
	public int getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(int profile_id) {
		this.profile_id = profile_id;
	}

	public String getProfileName(String instid, String profileid, JdbcTemplate jdbctemplate) {
		
		/*String statusqry = "SELECT PROFILE_NAME FROM " + getProfilelistTemp() + " WHERE INST_ID='" + instid
				+ "' AND PROFILE_ID='" + profileid + "'  AND ROWNUM<=1";
		String statusdesc = (String) jdbctemplate.queryForObject(statusqry, String.class);*/

		//by gowtham-140819
				String statusqry = "SELECT PROFILE_NAME FROM " + getProfilelistTemp() + " WHERE INST_ID=?AND PROFILE_ID='?  AND ROWNUM<=?";
				String statusdesc = (String) jdbctemplate.queryForObject(statusqry,new Object[]{instid,profileid}, String.class);
		
		return statusdesc;
	}

	// function called before displaying the Previlage of Inst Admin
	public String adminProf() {
		trace("*************** Adding adminProf Begins **********");
		enctrace("*************** Adding adminProf Begins **********");
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		instname = (getRequest().getParameter("instname"));
		setInstname(instname);
		session.setAttribute("instname", instname);
		System.out.println("instname   " + instname);
		prof_name = (getRequest().getParameter("profilename"));
		prof_desc = (getRequest().getParameter("profiledesc"));
		inst_name = (getRequest().getParameter("instname"));
		
		String profilename=getRequest().getParameter("profilename");
		String profiledesc=getRequest().getParameter("profiledesc");
		
		bean.setProfilename(profilename);
		bean.setProfiledesc(profiledesc);
		
		check=Validation.charcter(profilename);
		if(!check)
		{
			addFieldError("profilename","ENTER profilename ");
			return addProfile();
		}
				
		check=Validation.NumberCharcter(profiledesc);
		if(!check)
		{
			addFieldError("profiledesc","ENTER profiledesc");
			return addProfile();
		}
		
		
		
		setProf_name(prof_name);
		// loginbranchcode = "0";
		branch_validation = "0";
		// loginretrycount="0";
		login_retry = "0";

		login_ip = (getRequest().getParameter("ipaddress"));
		if (login_ip != null) {
			login_ip = "1";
		} else {
			login_ip = "0";
		}

		user_expdate = (getRequest().getParameter("userexpdate"));
		if (user_expdate != null) {
			user_expdate = "1";
		} else {
			user_expdate = "0";
		}

		psswd_reptable = (getRequest().getParameter("pswreptable"));
		if (psswd_reptable != null) {
			psswd_reptable = "1";
		} else {
			psswd_reptable = "0";
		}

		psswd_expdate = (getRequest().getParameter("pswexpdate"));
		if (psswd_expdate != null) {
			psswd_expdate = "1";
		} else {
			psswd_expdate = "0";
		}
		session.setAttribute("prev_prof_name", prof_name);
		session.setAttribute("prof_desc", prof_desc);
		session.setAttribute("loginbranchcode", branch_validation);
		session.setAttribute("loginretrycount", login_retry);
		session.setAttribute("loginipaddress", login_ip);
		session.setAttribute("loginexpiary", user_expdate);
		session.setAttribute("userpswdrpt", psswd_reptable);
		session.setAttribute("usrpswexp", psswd_expdate);

		// addSessionintrace(dataSource, jdbctemplate, session);
		/*String profilenm_query = "select count(PROFILE_NAME) from " + getProfilelistTemp()
				+ " where trim(PROFILE_NAME)='" + prof_name + "' and INSTID ='" + instname + "' ";
		int countprofilenm = jdbctemplate.queryForInt(profilenm_query);
		System.out.println(countprofilenm);*/
		
		
		//by gowtham-140819
				String profilenm_query = "select count(PROFILE_NAME) from " + getProfilelistTemp()+ " where trim(PROFILE_NAME)=? and INSTID =?";
				int countprofilenm = jdbctemplate.queryForInt(profilenm_query,new Object[]{prof_name,instname});
				System.out.println(countprofilenm);
		if (countprofilenm == 0) {
			try {
				String count_query = "SELECT count(*) from " + getProfilelistTemp() + "";
				int count = jdbctemplate.queryForInt(count_query);
				if (count > 0) {
					String profileid = "select MAX(PROFILE_ID)+1 as PROFILE_ID  from " + getProfilelistTemp() + "";
					profile_id = jdbctemplate.queryForInt(profileid);
					setProfile_id(profile_id);
					session.setAttribute("prev_prof_id", profile_id);
				} else {
					profile_id = 1;
					setProfile_id(profile_id);
					session.setAttribute("prev_prof_id", profile_id);

				}
			} catch (Exception e) {
				return "Exception";
			}
		} else {
			session.setAttribute(user_mgnt_message, "Profile Already Exists ");
			// setDisplay("Profile Already Exists ");
			return "adminprofilesuccess";
		}
		return "adminprof";
	}

	public String adminProfileview() {
		String menuid = null;
		String mainmenuid = null;
		List<List> mstrmenuqryrsult = new ArrayList<List>();
		instname = (getRequest().getParameter("instname"));
		setInstname(instname);
		List result;
		trace("*************** Adding adminProfileview Begins **********");
		enctrace("*************** Adding adminProfileview  Begins **********");

		// addSessionintrace(dataSource, jdbctemplate, session);
		String qury = "select PROFILE_LIST from " + getADMIN_PROFILE_PRIVILEGE() + "";
		enctrace("qury is:" + qury);
		result = jdbctemplate.queryForList(qury);
		trace("result List is:" + result.size());
		// System.out.println(result);
		Iterator itr = result.iterator();
		Map map;
		while (itr.hasNext()) {
			map = (Map) itr.next();
			proflist = (map.get("PROFILE_LIST")).toString();
		}
		trace(" Profile List  ----->>>>>" + proflist);
		
		/*String masterqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
				+ " where MENUVISIBILITY ='1' and parentid='0' ORDER BY MENUORDER ASC";
		enctrace("masterqury is:" + masterqury);
		List mstrqryrsult = jdbctemplate.queryForList(masterqury);*/
		
		
		//by gowtham140819
				String masterqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()+ " where MENUVISIBILITY =? and parentid=? ORDER BY MENUORDER ASC";
				enctrace("masterqury is:" + masterqury);
				List mstrqryrsult = jdbctemplate.queryForList(masterqury,new Object[]{"1","0"});
		
		List rsult1 = new ArrayList<List>();
		trace("rsult1 is:" + rsult1);
		Iterator mstrqryitr = mstrqryrsult.iterator();
		mstrmenuqryrsult.clear();
		while (mstrqryitr.hasNext()) {
			map = (Map) mstrqryitr.next();
			mainmenuid = (map.get("MENUID")).toString();
			menuid = "," + mainmenuid + ",";
			rsult1.clear();
			if (proflist.contains(menuid)) {
				
				/*String mastermenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
						+ " where MENUVISIBILITY ='1' and MENUID='" + mainmenuid + "' ORDER BY MENUORDER ASC";
				enctrace("mastermenuqury is:" + mastermenuqury);
				rsult1 = jdbctemplate.queryForList(mastermenuqury);*/
				
				//by gowtham-140819
				String mastermenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()+ " where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
				enctrace("mastermenuqury is:" + mastermenuqury);
				rsult1 = jdbctemplate.queryForList(mastermenuqury,new Object[]{"1",mainmenuid});
				
				trace("rsult1 list is " + rsult1);
			}
			mstrmenuqryrsult.addAll(rsult1);
		}
		trace("MENU LIST" + mstrmenuqryrsult);
		trace("length  " + mstrmenuqryrsult.size());
		setAdminproflist(mstrmenuqryrsult);
		trace("\n\n\n");
		enctrace("\n\n\n");
		return "AdminProfileview";
	}

	public String instAdmnPrev() {
		trace("*************** Adding instAdmnPrev Begins **********");
		enctrace("*************** Adding instAdmnPrev  Begins **********");
		HttpSession session = getRequest().getSession();
		prof_name = (String) session.getAttribute("prev_prof_name");
		prof_desc = (String) session.getAttribute("prof_desc");
		branch_validation = (String) session.getAttribute("loginbranchcode");
		login_retry = (String) session.getAttribute("loginretrycount");
		login_ip = (String) session.getAttribute("loginipaddress");
		user_expdate = (String) session.getAttribute("loginexpiary");
		psswd_reptable = (String) session.getAttribute("userpswdrpt");
		psswd_expdate = (String) session.getAttribute("usrpswexp");
		instname = (String) session.getAttribute("instname");
		String usercode = comUserCode(session);
		Date date=new Date();
		IfpTransObj transact = commondesc.myTranObject("INSTADMIN", txManager);
		String authcode = "1";
		String act = (String) session.getAttribute("act");
		if (act.equals("D")) {
			authcode = "1";
		} else {
			authcode = "0";
		}
		try {
			
			/*String profilenm_query = "select count(PROFILE_NAME) from " + getProfilelistTemp()
					+ " where trim(PROFILE_NAME)='" + prof_name + "' and INSTID ='"+ instname + "' ";
			int countprofilenm = jdbctemplate.queryForInt(profilenm_query);*/
			
			//by gowtham=140819
			String profilenm_query = "select count(PROFILE_NAME) from " + getProfilelistTemp()
			+ " where trim(PROFILE_NAME)=?and INSTID =?";
			int countprofilenm = jdbctemplate.queryForInt(profilenm_query,new Object[]{prof_name,instname});			
			
			if (countprofilenm > 0) {
				session.setAttribute("user_mgnt_message", "'" + prof_name + "'  Profile Already Exists ");
				return "adminprofilesuccess";
			}
			if (countprofilenm == 0) {
				String count_query = "SELECT count(*) from " + getProfilelistTemp() + "";
				int count = jdbctemplate.queryForInt(count_query);
				System.out.println("test   --------------- " + count);
				if (count > 0) {
					String profileid = "select MAX(PROFILE_ID) as PROFILE_ID  from " + getProfilelistTemp() + "";
					profile_id = jdbctemplate.queryForInt(profileid);
					profile_id = profile_id + 1;
				} else {
					profile_id = 1;
				}
				
				/*String query = "INSERT INTO " + getProfilelistTemp()
						+ "(PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED,LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,ADDED_BY,ADDED_DATE,AUTH_CODE,DELETED_FLAG)"
						+ " VALUES ('" + profile_id + "','" + prof_name + "','" + prof_desc + "','" + instname + "','"
						+ login_ip + "','" + user_expdate + "','" + login_retry + "','" + branch_validation + "','"
						+ psswd_reptable + "','" + psswd_expdate + "','A','" + usercode + "',SYSDATE,'" + authcode
						+ "','0')";
				enctrace(" Update query for add profile" + query);
				int res = jdbctemplate.update(query);*/
				
				//by gowtham-140819
				String query = "INSERT INTO " + getProfilelistTemp()
				+ "(PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED,LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,ADDED_BY,ADDED_DATE,AUTH_CODE,DELETED_FLAG)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				enctrace(" Update query for add profile" + query);
			int res = jdbctemplate.update(query,new Object[]{profile_id,prof_name,prof_desc,instname,login_ip,
					user_expdate,login_retry,branch_validation,psswd_reptable,psswd_expdate,"A",usercode,date.getCurrentDate(),authcode,"0"});
				
				
				
				trace("Inserting profile list ...got : " + res);
				if (res == 1) {
					ArrayList<String> Allmenu = new ArrayList<String>();
					String C_fvalue[] = (getRequest().getParameterValues("mainmenu"));
					int selectprofile = (Integer) (session.getAttribute("prev_prof_id"));
					int i;
					if (C_fvalue != null) {
						Allmenu.add("00");
						for (i = 0; i < C_fvalue.length; i++) {
							Allmenu.add(C_fvalue[i].trim());
						}
						Allmenu.add("00");
					} else {
						System.out.println("THE VALUE from the checkbox<<<<<== none");
					}
					// System.out.println (" Profile priviledge value --->"
					// +Allmenu);
					/*String menupriv_insert = "INSERT INTO " + getPROFILE_PRIVILEGE_TEMP()
							+ "(PROFILE_ID,MENU_LIST) VALUES ('" + selectprofile + "','" + Allmenu + "')";
					enctrace("menupriv_insert :" + menupriv_insert);
					int menupriv = jdbctemplate.update(menupriv_insert);*/
					
////by gowtham
					String menupriv_insert = "INSERT INTO " + getPROFILE_PRIVILEGE_TEMP()+ "(PROFILE_ID,MENU_LIST) VALUES (?,?)";
					enctrace("menupriv_insert :" + menupriv_insert);
					int menupriv = jdbctemplate.update(menupriv_insert,new Object[]{selectprofile,Allmenu});
					
					trace("Inserting profile privalage temp table...got : " + menupriv);
					if (menupriv == 1) {
						transact.txManager.commit(transact.status);
						addActionError("Profile Added Successfully");
						return "adminprofilesuccess";
					} else {
						transact.txManager.rollback(transact.status);
						addActionError("  Erorr : while adding Prifile privilege");
						trace("Erorr : while adding Prifile privilege");
						return "required_home";
					}
				} else {
					session.setAttribute("preverr", "E");
					addActionError(" Error : Prifile Not Added ");
					trace("Error : Profile not added ");
					return "required_home";
				}
			}
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("  Exception : While Adding Prifile");
			trace("Exception : While Adding Prifile" + e.getMessage());
			return "required_home";
		}
		return "required_home";
	}

	public void subMenuList() {
		List<List> submenulist;
		List<List> submenuqryrsult = new ArrayList<List>();
		String menu1 = getMenuid();
		// System.out.println("proflist ++++++++++++++++++++++++; "+proflist );
		String menu2 = "," + menu1 + ",";
		System.out.println("menuid  :  " + menu1);
		trace("*************** Adding subMenuList Begins **********");
		enctrace("*************** Adding subMenuList  Begins **********");

		// addSessionintrace(dataSource, jdbctemplate, session);
		String submenuid;
		System.out.println("proflist  :  " + proflist);
		if (proflist.contains(menu2)) {
			
			/*////// System.out.println("Main menu within list is" +menu1);
			String submenu = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
					+ " where MENUVISIBILITY ='1' and parentid='" + menu1 + "' ORDER BY MENUORDER ASC";
			System.out.println("SUB MENU LIST QURY===>" + submenu);
			List rsult = jdbctemplate.queryForList(submenu);*/
			
			//by gowtham-140819
			String submenu = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()+ " where MENUVISIBILITY =? and parentid=? ORDER BY MENUORDER ASC";
			System.out.println("SUB MENU LIST QURY===>" + submenu);
			List rsult = jdbctemplate.queryForList(submenu,new Object[]{"1",menu1});
			
			List rsult1 = new ArrayList<List>();
			System.out.println("Submenu query" + rsult);
			Iterator subqryitr = rsult.iterator();
			while (subqryitr.hasNext()) {
				Map map = (Map) subqryitr.next();
				submenuid = (map.get("MENUID")).toString();
				submenu = "," + submenuid + ",";
				System.out.println("menuid  :  " + submenu);
				rsult1.clear();
				if (proflist.contains(submenu)) {
					/*String subrmenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
							+ " where MENUVISIBILITY ='1' and MENUID='" + submenuid + "' ORDER BY MENUORDER ASC";
					System.out.println("subrmenuqury QUry" + subrmenuqury);
					rsult1 = jdbctemplate.queryForList(subrmenuqury);
					System.out.println("Main menu result" + rsult1);*/
					
					// by gowtham140819
					String subrmenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
					+ " where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
					System.out.println("subrmenuqury QUry" + subrmenuqury);
					rsult1 = jdbctemplate.queryForList(subrmenuqury,new Object[]{"1",submenuid});
					System.out.println("Main menu result" + rsult1);
				}
				submenuqryrsult.addAll(rsult1);
			}
			setAdminsubmenulist(submenuqryrsult);
			// session.setAttribute("submenulist", submenuqryrsult);
			System.out.println("submenuqryrsult " + submenuqryrsult);
			trace("\n\n\n");
			enctrace("\n\n\n");
		}
	}

	private List adminsubmenulist;

	public List getAdminsubmenulist() {
		return adminsubmenulist;
	}

	public void setAdminsubmenulist(List adminsubmenulist) {
		this.adminsubmenulist = adminsubmenulist;
	}

	List adminproflist;

	public List getAdminproflist() {
		return adminproflist;
	}

	public void setAdminproflist(List adminproflist) {
		this.adminproflist = adminproflist;
	}

	public String profileview() {
		trace("*************** profileview Begins **********");
		enctrace("*************** profileview Begins **********");
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		String instname = i_Name.toUpperCase();
		String branch_req = null;
		// addprofilebean profile = new addprofilebean();
		List result;
		setInstname(instname);

		// addSessionintrace(dataSource, jdbctemplate, session);
		/*
		 * String qury="select * from INSTITUTION where INST_ID='"+instname+"' "
		 * ; enctrace("qury is:"+qury);
		 */
		result = instdao.getListOfInstitutionFromProduction(jdbctemplate);
		trace("result list is:" + result.size());
		// System.out.println(result);
		setAdminproflist(result);
		Iterator brattch_itr = result.iterator();
		while (brattch_itr.hasNext()) {
			Map map = (Map) brattch_itr.next();
			branch_req = (map.get("BRANCHATTCHED")).toString();
		}
		brattched = branch_req.charAt(0);
		trace("brattched   ---> " + brattched);
		trace("\n\n");
		enctrace("\n\n");
		return "profileview";
	}

	char profile_exist = 'U';
	char brattched;

	public char getBrattched() {
		return brattched;
	}

	public void setBrattched(char brattched) {
		this.brattched = brattched;
	}

	public char getProfile_exist() {
		return profile_exist;
	}

	public void setProfile_exist(char profile_exist) {
		this.profile_exist = profile_exist;
	}

	public List allinst;

	public List getAllinst() {
		return allinst;
	}

	public void setAllinst(List allinst) {
		this.allinst = allinst;
	}

	public String addUserDet() {
		HttpSession session = getRequest().getSession();
		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("act", act);
		}

		try {

			/****************************/
			System.out.println("Sending Mail");
			MailSender mailnew = new MailSender(getMailSender());
			mailnew.mailSendingEngine("pritto2000@gmail.com", "pritto2012@gmail.com", "Sending Subject",
					"Test content");
			System.out.println("Mail sent successfully....");
			/****************************/

			trace("*************** Add User Details Begins **********");
			enctrace("*************** Add User Details Begins **********");
			String username = (String) session.getAttribute("SS_USERNAME");
			String userid = (String) session.getAttribute("USERID");

			String usertype = (String) session.getAttribute("USERTYPE");
			trace("User type is : " + usertype);
			if (usertype.equals("SUPERADMIN")) {

				List inst_list = instdao.getListOfInstitutionFromProduction(jdbctemplate);
				setAllinst(inst_list);

				/*String count_query = "SELECT count(*) from " + getProfilelistMain() + " where usertype='A'";
				enctrace("count_query is.." + count_query);
				int count = jdbctemplate.queryForInt(count_query);*/
				
				//by gowtham140819
				String count_query = "SELECT count(*) from " + getProfilelistMain() + " where usertype=?";
				enctrace("count_query is.." + count_query);
				int count = jdbctemplate.queryForInt(count_query,new Object[]{"A"});
				
				
				trace("count value is:" + count);
				// System.out.println(count);
				if (count == 0) {
					trace("count from Profile list" + count);
					// System.out.println("count from Profile list"+count);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", " NO PROFILE ADDED. ");
					trace(" NO PROFILE ADDED. ");
					return "adduserdetails";
				}
				if (count > 0) {
					// addprofilebean profile = new addprofilebean();
					List result;
					// String qury="select distinct(PROFILE_ID),PROFILE_NAME
					// from "+getProfilelistTemp()+" ";
					/*String qury = "select PROFILE_ID,PROFILE_NAME from " + getProfilelistMain()
							+ " where usertype ='A' and USER_CODE='SU' AND AUTH_CODE='1' order by PROFILE_ID";
					enctrace("qury is:" + qury);
					result = jdbctemplate.queryForList(qury);
					trace("result list is:" + result.size());*/
					
					//BY GOWTHAM-140819
					String qury = "select PROFILE_ID,PROFILE_NAME from " + getProfilelistMain()
					+ " where usertype =? and USER_CODE=? AND AUTH_CODE=?order by PROFILE_ID";
					enctrace("qury is:" + qury);
					result = jdbctemplate.queryForList(qury,new Object[]{"A","SU","1"});
					// System.out.println("my result"+result);
					setProfilename(result);
				} else {
					profile_exist = 'N';
					// System.out.println("No Profile Exist");
					trace("No Profile Exist");
				}
				trace("\n\n");
				enctrace("\n\n");
				return "SaveAdminUserDet";
			} else {
				instname = (String) session.getAttribute("Instname");
				instname = instname.toUpperCase();
				trace("INSTITUTION NAME IS " + instname);
				// System.out.println("INSTITUTION NAME IS "+instname);

				/*String count_query = "SELECT count(*) FROM " + getProfilelistMain() + " WHERE INSTID='" + instname
						+ "' AND USER_CODE != 'SU'";
				enctrace("count_query is:" + count_query);
				int count = jdbctemplate.queryForInt(count_query);*/
				
				
				
				//BY GOWTHAM-140819
				String count_query = "SELECT count(*) FROM " + getProfilelistMain() + " WHERE INSTID=? AND USER_CODE !=?";
				enctrace("count_query is:" + count_query);
				int count = jdbctemplate.queryForInt(count_query,new Object[]{instname,"SU"});
				trace("count value is:" + count);
				// System.out.println(count);
				if (count == 0) {
					trace("count from Profile list" + count);
					// System.out.println("count from Profile list"+count);
					addActionError(" NO PROFILE ADDED BY THIS USER. ");
					trace(" NO PROFILE ADDED. ");
					return "required_home";
				}

				if (count > 0) {
					List result;
					// String qury="select PROFILE_ID,PROFILE_NAME from
					// "+getProfilelistTemp()+" where INSTID='"+instname+"'";
					/*String qury = "SELECT * FROM " + getProfilelistMain() + " WHERE INSTID='" + instname
							+ "' AND USER_CODE != 'SU'";
					enctrace("qury is:" + qury);
					result = jdbctemplate.queryForList(qury);*/
					
					
					//BY GOWTHAM-140819
					String qury = "SELECT * FROM " + getProfilelistMain() + " WHERE INSTID=? AND USER_CODE != ? ";
					enctrace("qury is:" + qury);
					result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU"});
					trace("result list is:" + result.size());
					// System.out.println(result);
					setProfilename(result);
				}
				trace("\n\n");
				enctrace("\n\n");
			}

		} catch (Exception e) {
			addActionError(" Exception...unable to process");
			e.printStackTrace();

		}
		return "SaveAdminUserDet";
	}

	public String SaveUserDet() {
		trace("*************** Saving User Begins **********");
		enctrace("*************** Saving User Begins **********");
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		String username1 = (String) session.getAttribute("SS_USERNAME");
		trace("################he USER WHO IS ADDING UISER I$################### " + username1);
		IfpTransObj transact = commondesc.myTranObject("SAVEUSER", txManager);
		String username;
		String password;
		String cpassword;
		String fname;
		String lname;
		String email;
		String status;
		String profile;
		String ipaddress;
		String pwd_check_count;
		String loginbrchreq;
		String loginexpreq;
		String pswexpchck;
		String instid;
		username = (getRequest().getParameter("username"));
		String cbsusername = (getRequest().getParameter("cbsusername"));
		password = commondesc.generateRandomNumber(8);
		fname = (getRequest().getParameter("fname"));
		lname = (getRequest().getParameter("lname"));
		email = (getRequest().getParameter("email"));
		status = (getRequest().getParameter("status"));
		profile = (getRequest().getParameter("profile"));
		instid = (getRequest().getParameter("instid"));
		//pwd_check_count = (getRequest().getParameter("pwd"));
		//trace("pwd_check_count-->" + pwd_check_count);
		String authcode = "1";
		
		
		//session.setAttribute("SS_USERNAME", username1);
		
		trace("username1 ====    "+ username1   +"   username===== "+username);

		bean.setInstid(instid);
		bean.setProfile(profile);
		bean.setUsername(username);
		bean.setCbsusername(cbsusername);
		bean.setFname(fname);
		bean.setLname(lname);
		bean.setEmail(email);
		bean.setStatus(status);
		
		System.out.println("fdgfdsgdfsgdfsgds---->"+profile);
		if(instid.equals("-1"))
		{
			addFieldError("instid","Please Select Institution Values");
			return addUserDet();
		}
		if(profile.equals("-1"))
		{
			System.out.println("inside false con");
			addFieldError("profile","Please Select  Profile Values");
			return addUserDet();
		}
		
		check=Validation.charcter(username);
		if(!check)
		{
			System.out.println("inside false con111111111");
			addFieldError("username","Enter Username With Character");
			return addUserDet();
		}
		
		check=Validation.charcter(cbsusername);
		if(!check)
		{
			addFieldError("cbsusername","Enter Cbsusername With Character");
			return addUserDet();
		}
		
		check=Validation.charcter(fname);
		if(!check)
		{
			addFieldError("fname","Enter First Name With Character");
			return addUserDet();
		}
		
		check=Validation.charcter(lname);
		if(!check)
		{
			System.out.println("inside false con11111221111");
			addFieldError("lname","Enter Last Name With Character");
			return addUserDet();
		}
		
		check=Validation.email(email);
		if(!check)
		{
			addFieldError("email","Enter Proper Email ID");
			return addUserDet();
		}
		
		if(status.equals("-1"))
		{
			addFieldError("status","Please Select Status Values");
			return addUserDet();
		}
		
		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date=new Date();
		String authmsg = "", cuserid = "", userid = "", in_name = "";
		String act = getRequest().getParameter("act");
		if (act == null) {
			act = (String) session.getAttribute("act");
		}
		if (act == null) {
			addActionError("Unable to identify the user privilage");
			return "required_home";
		}
		String externaluser = (String) session.getAttribute("EXTERNALUSER");
		String usertype = (String) session.getAttribute("USERTYPE");
		trace("usettype is : " + usertype + " act value is : " + act);

		try {

			if (usertype.equals("SUPERADMIN")) {
				String dualath = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
				if (dualath != null && dualath.equals("Y")) {
					authcode = "0";
					authmsg = " Waiting for Authorization";
				} else {
					authcode = "1";

				}

			} else {
				if (act != null && act.equals("M")) {
					authcode = "0";
					authmsg = " Waiting for Authorization";
				} else {
					authcode = "1";
				}
			}

			if (usertype.equals("SUPERADMIN")) {
				in_name = (getRequest().getParameter("instid"));
				String modifydate = "";
				/*
				 * String qury="select INSTID from "+getProfilelistTemp()+
				 * " where PROFILE_ID='"+profile.trim()+"'"; enctrace(
				 * "SaveUserDet qury is:"+qury); String
				 * in_name=(String)jdbctemplate.queryForObject(qury,
				 * String.class);
				 */
				trace("INSTID ::::::::::::::::::::::::::::::::::::   " + in_name);
				usertype = "A";
				ipaddress = (getRequest().getParameter("ipaddress"));
				if (ipaddress == null) {
					ipaddress = "N";
				} else {
					ipaddress = (getRequest().getParameter("ipaddress"));
				}

				loginbrchreq = (getRequest().getParameter("loginbrchreq"));
				if (loginbrchreq == null) {
					loginbrchreq = "000";
				} else {
					loginbrchreq = (getRequest().getParameter("loginbrchreq"));
				}

				loginexpreq = (getRequest().getParameter("loginexpreq"));
				if (loginexpreq == null) {
					loginexpreq = "'N'";
				} else {
					loginexpreq = "( SELECT TO_CHAR(TO_DATE('" + loginexpreq
							+ "', 'DD-MM-YY') ,'DD-MON-YYYY') FROM DUAL )";
					enctrace("loginexpreq is:" + loginexpreq);
				}
				pswexpchck = (getRequest().getParameter("pswexpchck"));
				if (pswexpchck == null) {
					pswexpchck = "'N'";
				} else {
					pswexpchck = "( SELECT TO_CHAR(TO_DATE('" + pswexpchck
							+ "', 'DD-MM-YY') ,'DD-MON-YYYY') FROM DUAL )";
					enctrace("pswexpchck is:" + pswexpchck);
				}
				String passwordrpt = (getRequest().getParameter("passwordrpt"));
				if (passwordrpt == null) {
					passwordrpt = "N";
				} else {
					passwordrpt = (getRequest().getParameter("passwordrpt"));
				}
				cuserid = "SU";
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				byte[] bSalt = new byte[8];
				random.nextBytes(bSalt);

				byte[] bDigest = pswd_hash_class.getHash(pswd_hash_class.ITERATION_NUMBER, password, bSalt);

				String hashed_password = pswd_hash_class.byteToBase64(bDigest);
				String sSalt = pswd_hash_class.byteToBase64(bSalt);
				int usercount = -1;
				
			/*	String query_userexist = "select count(*) from USER_DETAILS where USERNAME='" + username
						+ "' and INSTID='" + in_name + "'";
				enctrace("query_userexist  ;:::" + query_userexist);
				
				usercount = jdbctemplate.queryForInt(query_userexist);*/
				
				//BY GOWTHAM140819
				String query_userexist = "select count(*) from USER_DETAILS where USERNAME=? and INSTID=?";
				enctrace("query_userexist  ;:::" + query_userexist);
				usercount = jdbctemplate.queryForInt(query_userexist,new Object[]{username,in_name});
				trace("USer Exsist Query Count############# " + usercount);
				if (usercount > 0) {
					trace("User : ' " + username + " ' Already Exists");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "User : ' " + username + " ' Already Exists");

				}
				userid = commondesc.generateUserIdSeq(in_name, jdbctemplate);
				trace("userid :  " + userid);
				String insertuser = "";
				
				insertuser = "insert into USER_DETAILS(USERID,USERNAME,USERPASSWORD,PROFILEID,FIRSTNAME,LASTNAME,";
				insertuser = insertuser+ "EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,";
				insertuser = insertuser+ " CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,";
				insertuser = insertuser+ " INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,ADDED_BY,ADDED_DATE,AUTH_STATUS,CBS_USERNAME,PWD_REPEAT_COUNT,PWD_CHANGED_COUNT,PWD_STATUS)VALUES ";
				
				insertuser = insertuser + "('" + userid + "','" + username + "','" + hashed_password + "','" + profile
				+ "','" + fname + "','" + lname + "','" + email + "','" + status + "',";
				insertuser = insertuser + " 'null','" + loginbrchreq.trim() + "','" + ipaddress + "','0','0',"
				+ loginexpreq + ",sysdate,'0','1'," + pswexpchck + ",";
				insertuser = insertuser + " '0','" + modifydate + "','" + in_name + "','" + cuserid
				+ "','0','lstlogin','" + usertype + "','" + passwordrpt + "','frstlogin','F','N','" + username1
					/*+ "',SYSDATE,'" + authcode + "','" + cbsusername + "','" + pwd_check_count + "','0','0')";*/
				    + "',SYSDATE,'" + authcode + "','" + cbsusername + "','4','0','0')";
				enctrace("insertuser query is:" + insertuser);
				enctrace("+++++ query is +++++\n" + insertuser);
				trace("insertuser  ---->" + insertuser);
				int result1 = jdbctemplate.update(insertuser);
				
						/*insertuser = insertuser + "(?,?,?,?,?,?,?,?,";
						insertuser = insertuser + " ?,?,?,?,?,"+ loginexpreq + ",?,?,?," + pswexpchck + ",";
						insertuser = insertuser + " ?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?)";
						enctrace("insertuser query is:" + insertuser);
						enctrace("+++++ query is +++++\n" + insertuser);
						trace("insertuser  ---->" + insertuser);
						int result1 = jdbctemplate.update(insertuser,new Object[]{userid,username,hashed_password,profile,fname,lname,email,status,
							"null",loginbrchreq.trim(),ipaddress,"0","0",date.getCurrentDate(),"0","1","0","lstlogin",usertype,passwordrpt,"frstlogin","F","N",username1,authcode,cbsusername,"4","0","0"  });
				*/
				int updateuserid_result = commondesc.updateUserIdSeq(in_name, jdbctemplate);

				insertuser = "insert into USER_DETAILS_SALT(USERID,USERNAME,PROFILEID,FIRSTNAME,LASTNAME,";
				insertuser = insertuser
						+ "EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,";
				insertuser = insertuser
						+ " CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,";
				insertuser = insertuser
						+ " INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,CBS_USERNAME)VALUES ";
				insertuser = insertuser + "('" + userid + "','" + username + "','" + profile + "','" + fname + "','"
						+ lname + "','" + email + "','" + status + "',";
				insertuser = insertuser + " 'null','" + loginbrchreq.trim() + "','" + ipaddress + "','0','0',"
						+ loginexpreq + ",sysdate,'0','1'," + pswexpchck + ",";
				insertuser = insertuser + " '0','" + modifydate + "','" + in_name + "','" + cuserid
						+ "','0','lstlogin','" + usertype + "','" + passwordrpt + "','frstlogin','F','N','" + sSalt
						+ "','" + username1 + "',SYSDATE,'" + authcode + "','" + cbsusername + "')";
				enctrace("insertuser query is:" + insertuser);
				enctrace("+++++ query is +++++\n" + insertuser);
				trace("insertuser  ---->" + insertuser);
				int result2 = jdbctemplate.update(insertuser);
				
				/*
				//by gowtham-140819
				insertuser = "insert into USER_DETAILS_SALT(USERID,USERNAME,PROFILEID,FIRSTNAME,LASTNAME,";
				insertuser = insertuser
						+ "EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,";
				insertuser = insertuser
						+ " CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,";
				insertuser = insertuser
						+ " INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,CBS_USERNAME)VALUES ";
				insertuser = insertuser + "(?,?,?,?,?,?,?,";
				insertuser = insertuser + " ?,?,?,?,?,?,?,?,?,?,";
				insertuser = insertuser + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				enctrace("insertuser query is:" + insertuser);
				enctrace("+++++ query is +++++\n" + insertuser);
				trace("insertuser  ---->" + insertuser);
				int result2 = jdbctemplate.update(insertuser,new Object[]{userid ,username,profile,fname,lname,email,status,"null",loginbrchreq.trim(),ipaddress,"0","0",loginexpreq,
						date.getCurrentDate(),"0","1",pswexpchck,"0",modifydate,in_name,cuserid,"0","lstlogin",usertype,passwordrpt,"frstlogin",
						"F","N",sSalt,username1,date.getCurrentDate(),authcode,cbsusername});
				*/
				
				int updateuserid_result1 = commondesc.updateUserIdSeq(in_name, jdbctemplate);

				if (result1 < 1) {
					transact.txManager.rollback(transact.status);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "User Cannot be Added");
					trace("Error While Adding User");
					return this.addUserDet();
				}

				if (result2 < 1) {
					transact.txManager.rollback(transact.status);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "User Cannot be Added");
					trace("Error While Adding User");
					return this.addUserDet();
				}
			}

			else {
				String modifydate = "";
				// HttpSession session = getRequest().getSession();
				String i_Name = (String) session.getAttribute("Instname");
				in_name = i_Name.toUpperCase();
				String user_id;
				usertype = "B";
				ipaddress = (getRequest().getParameter("ipaddress"));
				if (ipaddress == null) {
					ipaddress = "N";
				}

				loginbrchreq = (getRequest().getParameter("loginbrchreq"));
				if (loginbrchreq == null) {
					loginbrchreq = "000";
					usertype = "A";
				}

				loginexpreq = (getRequest().getParameter("loginexpreq"));
				if (loginexpreq == null) {
					loginexpreq = "'N'";
				} else {
					loginexpreq = "( SELECT TO_CHAR(TO_DATE('" + loginexpreq
							+ "', 'DD-MM-YY') ,'DD-MON-YYYY') FROM DUAL )";
					enctrace("loginexpreq is:" + loginexpreq);
				}

				pswexpchck = (getRequest().getParameter("pswexpchck"));
				if (pswexpchck == null) {
					pswexpchck = "'N'";
				} else {
					pswexpchck = "( SELECT TO_CHAR(TO_DATE('" + pswexpchck
							+ "', 'DD-MM-YY') ,'DD-MON-YYYY') FROM DUAL )";
					enctrace("pswexpchck is:" + pswexpchck);
				}

				String passwordrpt = (getRequest().getParameter("passwordrpt"));
				if (passwordrpt == null) {
					passwordrpt = "N";
				}

				cuserid = comUserCode(session);
				enctrace("cuserid is:" + cuserid);
				String count_query = "SELECT count(*) from USER_DETAILS";
				enctrace("count_query is:" + count_query);
				int count = jdbctemplate.queryForInt(count_query);
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				// Salt generation 64 bits long
				byte[] bSalt = new byte[8];
				random.nextBytes(bSalt);
				// Digest computation
				byte[] bDigest = pswd_hash_class.getHash(pswd_hash_class.ITERATION_NUMBER, password, bSalt);
				// convert byte Digest to Base64 String
				String hashed_password = pswd_hash_class.byteToBase64(bDigest);
				String sSalt = pswd_hash_class.byteToBase64(bSalt);
				String user_expirydate = "N";

				/*String query_userexist = "select count(*) from USER_DETAILS "
				+ "where USERNAME='" + username	+ "' and INSTID='" + in_name + "'";
				enctrace("query_userexist  ;:::" + query_userexist);
				int usercount = jdbctemplate.queryForInt(query_userexist);*/
				
				///by  gowtham
				String query_userexist = "select count(*) from USER_DETAILS "
						+ "where USERNAME=? and INSTID=? ";
						enctrace("query_userexist  ;:::" + query_userexist);
						int usercount = jdbctemplate.queryForInt(query_userexist,new Object[]{username,in_name});
				
				if (usercount > 0) {
					trace("User : ' " + username + " ' Already Exists");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "User : ' " + username + " ' Already Exists");
				}

				userid = commondesc.generateUserIdSeq(in_name, jdbctemplate);
				trace("userid :  " + userid);
				String creationdate = "(select to_date(trunc(sysdate)) from dual)";
				enctrace("creationdate is:" + creationdate);
				int retrycount = 0;
				String insertuser = "";
				
				insertuser = "insert into USER_DETAILS(USERID,USERNAME,USERPASSWORD,PROFILEID,FIRSTNAME,LASTNAME,";
				insertuser = insertuser+ "EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,";
				insertuser = insertuser+ " CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,";
				insertuser = insertuser+ " INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,ADDED_BY,ADDED_DATE,AUTH_STATUS,CBS_USERNAME,PWD_STATUS,PWD_CHANGED_COUNT,PWD_REPEAT_COUNT)VALUES ";
			
				
				///by gowtham
				/*insertuser = insertuser + "('" + userid + "','" + username + "','" + hashed_password + "','" + profile+ "',"
				+ "'" + fname + "','" + lname + "','" + email + "','" + status + "',";
				insertuser = insertuser + " 'null','" + loginbrchreq.trim() + "','" + ipaddress + "','0','0',"
				+ loginexpreq + ",sysdate,'0','1'," + pswexpchck + ",";
				insertuser = insertuser + " '0','" + modifydate + "','" + in_name + "','" + cuserid
				+ "','0','lstlogin','" + usertype + "','" + passwordrpt + "','frstlogin','F','N','" + username1
				+ "',SYSDATE,'" + authcode + "','" + cbsusername + "','0','0')";
				enctrace("insertuser query is:" + insertuser);
				enctrace("+++++ query is +++++\n" + insertuser);
				trace("insertuser  ---->" + insertuser);
				int result1 = jdbctemplate.update(insertuser);*/
				
						insertuser = insertuser + "(?,?,?,?,?,?,?,?,";
						insertuser = insertuser + " ?,?,?,?,?,"+ loginexpreq + ",?,?,?," + pswexpchck + ",";
						insertuser = insertuser + " ?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?)";
						enctrace("insertuser query is:" + insertuser);
						enctrace("+++++ query is +++++\n" + insertuser);
						trace("insertuser  ---->" + insertuser);
						int result1 = jdbctemplate.update(insertuser,new Object[]{userid,username,hashed_password,profile,
						fname,lname,email,status,"null",loginbrchreq.trim(),ipaddress,"0","0",date.getCurrentDate(),"0",
						"1","0",modifydate,in_name,cuserid,"0","1stlogin",usertype,passwordrpt,"frstlogin","F","N",username,
						authcode,cbsusername,"0","0","4"});
				
				

				insertuser = "insert into USER_DETAILS_SALT(USERID,USERNAME,PROFILEID,FIRSTNAME,LASTNAME,";
				insertuser = insertuser+ "EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,";
				insertuser = insertuser+ " CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,";
				insertuser = insertuser+ " INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,CBS_USERNAME)VALUES ";
				
				/*insertuser = insertuser + "('" + userid + "','" + username + "','" + profile + "','" + fname + "','"
				+ lname + "','" + email + "','" + status + "',";
				insertuser = insertuser + " 'null','" + loginbrchreq.trim() + "','" + ipaddress + "','0','0',"
				+ loginexpreq + ",sysdate,'0','1'," + pswexpchck + ",";
				insertuser = insertuser + " '0','" + modifydate + "','" + in_name + "','" + cuserid
				+ "','0','lstlogin','" + usertype + "','" + passwordrpt + "','frstlogin','F','N','" + sSalt
				+ "','" + username1 + "',SYSDATE,'" + authcode + "','" + cbsusername + "')";
				enctrace("insertuser query is:" + insertuser);
				enctrace("+++++ query is +++++\n" + insertuser);
				trace("insertuser  ---->" + insertuser);
				int result2 = jdbctemplate.update(insertuser);*/
				
				
						////by gowtham
						insertuser = insertuser + "(?,?,?,?,?,?,?,";
						insertuser = insertuser + " ?,?,?,?,?,"
						+ loginexpreq + ",?,?,?," + pswexpchck + ",";
						insertuser = insertuser + " ?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?)";
						enctrace("insertuser query is:" + insertuser);
						enctrace("+++++ query is +++++\n" + insertuser);
						trace("insertuser  ---->" + insertuser);
						int result2 = jdbctemplate.update(insertuser,new Object[]{
						userid,username,profile,fname,lname,email,status,"null",loginbrchreq.trim(),ipaddress,"0","0",date.getCurrentDate(),
						"0","1","0",modifydate,in_name,cuserid,"0","1stlogin",usertype,passwordrpt,"frstlogin","F","N",sSalt,
						username1,authcode,cbsusername});
				

				if (act.equals("D")) {
					insertuser = "insert into USER_DETAILS(USERID,USERNAME,USERPASSWORD,PROFILEID,FIRSTNAME,LASTNAME,";
					insertuser = insertuser+ "EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,";
					insertuser = insertuser+ " CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,";
					insertuser = insertuser	+ " INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,ADDED_BY,ADDED_DATE,AUTH_STATUS,CBS_USERNAME,PWD_STATUS,PWD_CHANGED_COUNT,PWD_REPEAT_COUNT)VALUES ";
				
					/*insertuser = insertuser + "('" + userid + "','" + username + "','" + hashed_password + "','"
					+ profile + "','" + fname + "','" + lname + "','" + email + "','" + status + "',";
					insertuser = insertuser + " 'null','" + loginbrchreq.trim() + "','" + ipaddress + "','0','0',"
					+ loginexpreq + ",sysdate,'0','1'," + pswexpchck + ",";
					insertuser = insertuser + " '0','" + modifydate + "','" + in_name + "','" + cuserid
					+ "','0','lstlogin','" + usertype + "','" + passwordrpt + "','frstlogin','F','N','"
					+ username1 + "',SYSDATE,'" + authcode + "','" + cbsusername + "','0','0')";
					enctrace("insertuser query is:" + insertuser);
					enctrace("+++++ query is +++++\n" + insertuser);
					trace("insertuser  ---->" + insertuser);
					int result3 = jdbctemplate.update(insertuser);*/
					
					///by gowtham
							insertuser = insertuser + "(?,?,?,?,?,?,?,?,";
							insertuser = insertuser + " ?,?,?,?,?,"
							+ loginexpreq + ",?,?,?," + pswexpchck + ",";
							insertuser = insertuser + " ?,?,?,?,?,?,?,?,?,?,?,'?,SYSDATE,?,?,?,?,?)";
							enctrace("insertuser query is:" + insertuser);
							enctrace("+++++ query is +++++\n" + insertuser);
							trace("insertuser  ---->" + insertuser);
							int result3 = jdbctemplate.update(insertuser,new Object[]{
							userid,username,hashed_password,profile,fname,lname,email,status,"null",loginbrchreq.trim(),ipaddress,"0","0",
							date.getCurrentDate(),"0","1","0",modifydate,in_name,cuserid,"0","1stlogin",usertype,passwordrpt,"frstlogin",
							"F","N",username1,authcode,cbsusername,"0","0","4"});
					

					insertuser = "insert into USER_DETAILS_SALT(USERID,USERNAME,PROFILEID,FIRSTNAME,LASTNAME,";
					insertuser = insertuser+ "EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,";
					insertuser = insertuser+ " CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,";
					insertuser = insertuser+ " INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,CBS_USERNAME)VALUES ";
					
					/*insertuser = insertuser + "('" + userid + "','" + username + "','" + profile + "','" + fname + "','"+ lname + "',"
					+ "'" + email + "','" + status + "',";
					insertuser = insertuser + " 'null','" + loginbrchreq.trim() + "','" + ipaddress + "','0','0',"+ loginexpreq + ","
					+ "sysdate,'0','1'," + pswexpchck + ",";
					insertuser = insertuser + " '0','" + modifydate + "','" + in_name + "','" + cuserid+ "','0','lstlogin',"
					+ "'" + usertype + "','" + passwordrpt + "','frstlogin','F','N','" + sSalt+ "','" + username1 + "',SYSDATE,"
					+ "'" + authcode + "','" + cbsusername + "')";
					enctrace("insertuser query is:" + insertuser);
					enctrace("+++++ query is +++++\n" + insertuser);
					trace("insertuser  ---->" + insertuser);
					int result4 = jdbctemplate.update(insertuser);*/
					
					///by gowtham
					insertuser = insertuser + "(?,?,?,?,?,?,?,";
							insertuser = insertuser + " ?,?,?,?,?,"+ loginexpreq + ","
							+ "?,?,?," + pswexpchck + ",";
							insertuser = insertuser + " ?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?)";
							enctrace("insertuser query is:" + insertuser);
							enctrace("+++++ query is +++++\n" + insertuser);
							trace("insertuser  ---->" + insertuser);
							int result4 = jdbctemplate.update(insertuser,new Object[]{
							userid,username,profile,fname,lname,email,status,"null",loginbrchreq.trim(),ipaddress,"0","0",date.getCurrentDate(),
							"0","1","0",modifydate,"in_name",cuserid,"0","1stlogin",usertype,passwordrpt,"frstlogin","F","N",sSalt,username,
							authcode,cbsusername});

					int updateuserid_result = commondesc.updateUserIdSeq(in_name, jdbctemplate);
					if (result3 < 0) {
						transact.txManager.rollback(transact.status);
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "User Cannot be Added");
						trace("Error While Adding User");
					}

					if (result4 < 1) {
						transact.txManager.rollback(transact.status);
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "User Cannot be Added");
						trace("Error While Adding User");
						return this.addUserDet();
					}
				}

				int updateuserid_result = commondesc.updateUserIdSeq(in_name, jdbctemplate);
				if (result1 < 0) {
					transact.txManager.rollback(transact.status);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "User Cannot be Added");
					trace("Error While Adding User");
				}

				if (result2 < 1) {
					transact.txManager.rollback(transact.status);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "User Cannot be Added");
					trace("Error While Adding User");
					return this.addUserDet();
				}

			}

			transact.txManager.commit(transact.status);
			session.setAttribute("curerr", "S");
			session.setAttribute("curmsg", "User \"" + username + "\" Added Successfully " + authmsg);
			trace(" User Added and committed....");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg("User " + username + "  Added Successfully. " + authmsg);
				auditbean.setUsercode(externaluser);
				auditbean.setAuditactcode("3001");
				commondesc.insertAuditTrail(in_name, externaluser, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception..User Cannot be Added");
			trace("Error While Adding User" + e.getMessage());
			e.printStackTrace();

		}

		return "required_home";
	}

	public String callUserEnable() {
		trace("*************** Adding call User Enable Begins **********");
		enctrace("*************** Adding call User Enable  Begins **********");
		HttpSession session = getRequest().getSession();
		// String inst_id=(String)session.getAttribute("Instname");
		String inst_id = getRequest().getParameter("instid");
		System.out.println("inst_name---> " + inst_id);
		String profile = (getRequest().getParameter("profile"));
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		List result;
		String LOGINIPADDRESSREQUIRED = "0";
		String LOGINEXPIRYDATEREQUIRED = "0";
		String LOGINRETRYCOUNTREQUIRED = "0";
		String LOGINBRANCHCODEREQUIRED = "0";
		String USERPASSWORDREPEATABLE = "0";
		String USERPASSWORDEXPIRYCHECK = "0";

		/*String qury = "select * from " + getProfilelistTemp() + " where PROFILE_ID ='" + profile + "' and INSTID='"
				+ inst_id + "'";
		enctrace("callUserEnable() --------->" + qury);
		result = jdbctemplate.queryForList(qury);*/
		
		//by gowtham-140819
				String qury = "select * from " + getProfilelistTemp() + " where PROFILE_ID =?and INSTID=?";
				enctrace("callUserEnable() --------->" + qury);
				result = jdbctemplate.queryForList(qury,new Object[]{profile,inst_id});
		System.out.println("Result is Admin user" + result);

		Iterator itr = result.iterator();
		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			LOGINIPADDRESSREQUIRED = ((String) map.get("LOGINIPADDRESSREQUIRED"));
			System.out.println("LOGINIPADDRESSREQUIRED  " + LOGINIPADDRESSREQUIRED);
			LOGINEXPIRYDATEREQUIRED = ((String) map.get("LOGINEXPIRYDATEREQUIRED"));
			LOGINRETRYCOUNTREQUIRED = ((String) map.get("LOGINRETRYCOUNTREQUIRED"));
			LOGINBRANCHCODEREQUIRED = ((String) map.get("LOGINBRANCHCODEREQUIRED"));
			USERPASSWORDREPEATABLE = ((String) map.get("USERPASSWORDREPEATABLE"));
			USERPASSWORDEXPIRYCHECK = ((String) map.get("USERPASSWORDEXPIRYCHECK"));
		}
		
		
		//comment by siva 31-10-19 
		
		
		/*String reload = null;
		String expreload = null;
		String loginretrycount = null;
		String loginbrchreq = null;
		String uswpswrpt = null;
		String pswexpchckr = null;
		String passwordrpt = null;
		// Str sun=new ObjArray();
		if (LOGINBRANCHCODEREQUIRED.equals("1")) {
			// userbean branchdetails = new userbean();
			
			String branch_query = "SELECT BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID = '" + inst_id
					+ "' AND BRANCH_CODE!='000'";
			List branch_result = jdbctemplate.queryForList(branch_query);
			
			
			//by gowtham-140819
			String branch_query = "SELECT BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID =? AND BRANCH_CODE!=?";
			List branch_result = jdbctemplate.queryForList(branch_query,new Object[]{inst_id,"000"});
			
			
			System.out.println("Branch result" + branch_query);
			if (!(branch_result.isEmpty())) {
				Iterator branch_itr = branch_result.iterator();

				// Str sun=new ObjArray();
				loginbrchreq = "<div align='center'><table border='0' cellpadding='0' cellspacing='0' width='100%'><tr><td>Branch</td><td><select name=\"loginbrchreq\" id=\"loginbrchreq\" >";
				loginbrchreq = loginbrchreq + "<option value='00'>SELECT</option>";
				while (branch_itr.hasNext()) {
					Map map = (Map) branch_itr.next();
					String branchcode = ((String) map.get("BRANCH_CODE"));
					String branchname = ((String) map.get("BRANCH_NAME"));
					loginbrchreq = loginbrchreq + "<option value=\" " + branchcode + "\">" + branchname + "</option>";

				}

				loginbrchreq = loginbrchreq + "</select></td></tr>";
				// loginbrchreq = "<tr><td>Branch</td><td><input type='text'
				// name='loginbrchreq' id='loginbrchreq' value=''></td></tr>";
			} else {

				loginbrchreq = "<div align='center'><table border='0' cellpadding='0' cellspacing='0' width='100%'><tr><td>Branch</td><td><select name=\"loginbrchreq\" id=\"loginbrchreq\" ><option>No Branch Details Found</option></select>";

			}

		} else {
			loginbrchreq = "<div align='center'><table border='0' cellpadding='0' cellspacing='0' width='100%'><tr><td>Branch</td><td><input type='text' name='loginbrchreq' id='loginbrchreq' value='Not Applicable' disabled></td></tr>";
		}
		if (LOGINIPADDRESSREQUIRED.equals("1")) {
			System.out.println("LOGINIPADDRESSREQUIRED  " + LOGINIPADDRESSREQUIRED);
			// reload = "<table border='0' width='100%'><tr><td
			// style='width:176px'>Ip Address </td><td><input type='text'
			// name='ipaddress' id='ipaddress' value=''
			// enable='true'></td></tr>";
			reload = "<tr><td style='width:176px' >IP Address   </td><td><input type='text' name='ipaddress' id='ipaddress' value='' enable='true' onchange='checkipformat(this);' maxlength='35'></td></tr>";

		} else {
			reload = "<tr><td style='width:176px'>IP Address   </td><td><input type='text' name='ipaddress' id='ipaddress' value='Not Applicable' disabled></td></tr>";
		}
		if (LOGINEXPIRYDATEREQUIRED.equals("1")) {
			expreload = "<tr><td>User Expiry Date </td><td><input type='text' name='loginexpreq' id='loginexpreq' value='' readonly><img src='images/cal.gif' id='image' onclick=\"displayCalendar(document.AddAdminUserForm.loginexpreq,'dd-mm-yy',this);\" title='Click Here to Pick up the date' border='0' width='15' height='17'></td></tr>";
		} else {
			expreload = "<tr><td>User Expiry Date </td><td><input type='text' name='loginexpreq' id='loginexpreq' value='Not Applicable' disabled></td></tr>";
		}

		if (USERPASSWORDREPEATABLE.equals("1")) {
			// reload = "<table border='0' width='100%'><tr><td
			// style='width:176px'>Ip Address </td><td><input type='text'
			// name='ipaddress' id='ipaddress' value=''
			// enable='true'></td></tr>";
			uswpswrpt = "<tr><td style='width:176px' align='left'>Password Repeat Days Count</td><td><input type='text' name='passwordrpt' id='passwordrpt' value='' enable='true' onKeyPress='return numerals(event);' maxlength='2'></td></tr>";

		} else {
			uswpswrpt = "<tr><td style='width:176px' align='left'>Password Repeat Days Count</td><td><input type='text' name='passwordrpt' id='passwordrpt' value='Not Applicable' disabled></td></tr>";
		}
		
		 * if(LOGINRETRYCOUNTREQUIRED.equals("1")){ //reload =
		 * "<table border='0' width='100%'><tr><td style='width:176px'>Ip Address   </td><td><input type='text' name='ipaddress' id='ipaddress' value='' enable='true'></td></tr>"
		 * ; loginretrycount =
		 * "<tr><td style='width:176px' align='left'>Login Retry Count</td><td><input type='text' name='loginrty' id='loginrty' value='' enable='true' onKeyPress='return numerals(event);' maxlength='2'></td></tr>"
		 * ;
		 * 
		 * } else{ loginretrycount =
		 * "<tr><td style='width:176px' align='left'>Login Retry Count</td><td><input type='text' name='loginrty' id='loginrty' value='Not Applicable' disabled></td></tr>"
		 * ; }
		 
		if (USERPASSWORDEXPIRYCHECK.equals("1")) {
			pswexpchckr = "<tr><td>Password Expiry Date </td><td><input type='text' name='pswexpchck' id='pswexpchck' readonly style='width:180px'><img src=\"images/cal.gif\" id=\"image\" onclick=\"displayCalendar(document.AddAdminUserForm.pswexpchck,'dd-mm-yy',this);\" title=\"Click Here to Pick up the date\" border=\"0\" width=\"15\" height=\"17\"></td></tr>";

		} else {
			pswexpchckr = "<tr><td>Password Expiry Date </td><td><input type='text' name='pswexpchck' id='pswexpchck' value='Not Applicable' disabled></td></tr></table></div>";
		}
		try {
			response.getWriter().write(loginbrchreq);
			response.getWriter().write(reload);
			response.getWriter().write(expreload);
			response.getWriter().write(uswpswrpt);
			response.getWriter().write(pswexpchckr);
			
			 * response.getWriter().write(loginretrycount);
			 
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}*/
		return null;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String chanePsw() {
		trace("***************** Change Password Begin ******************");
		enctrace("***************** Change Password Begin ******************");
		HttpSession session = getRequest().getSession();
		Iterator iterator_userresult;
		LoginAction loginActionClass = new LoginAction();
		String oldpassword;
		String newpassword;
		oldpassword = (getRequest().getParameter("oldpassword"));
		newpassword = (getRequest().getParameter("newpassword"));
		String user_name = (String) session.getAttribute("SS_USERNAME");
		String USERID = (String) session.getAttribute("USERID");
		String inst_id = (String) session.getAttribute("Instname"), db_password = null, db_salt = null;
		inst_id = inst_id.trim();
		
		
		//jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj
		//HttpSession ses = getRequest().getSession();
		String sessioncsrftoken = (String) session.getAttribute("token");
	
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("sessioncsrftoken ---->  "+sessioncsrftoken);
		System.out.println("jspcsrftoken----> "+jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) 
		{
			System.out.println("check2");
			session.setAttribute("message", "CSRF Token Mismatch");
			addActionError("CSRF Token Mismatch");
			return "invaliduser";
		}

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		/*
		 * String query_getuserdatils=
		 * "select * FROM USER_DETAILS where username ='"+user_name+
		 * "' and INSTID='"+inst_id+"'";
		 */
		/*String query_getuserdatils = "SELECT USERID,USERNAME,USERPASSWORD,PROFILEID,FIRSTNAME,LASTNAME,EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,CHANGPASS_DATE,(SELECT SALT_KEY FROM USER_DETAILS_SALT WHERE USERNAME='"
		+ user_name + "' and INSTID='" + inst_id
		+ "') as SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,UNBLOCK_AUTHBY,UNBLOCK_AUTHDATE,PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,DELETED_BY,DELETED_DATE,DELETED_FLAG,CBS_USERNAME,PWD_1,PWD_2,PWD_3,PWD_4,(SELECT SALT_1 FROM USER_DETAILS_SALT WHERE USERNAME='"
		+ user_name + "' and INSTID='" + inst_id
		+ "') as SALT_1,(SELECT SALT_2 FROM USER_DETAILS_SALT WHERE USERNAME='" + user_name + "' and INSTID='"
		+ inst_id + "') as SALT_2,(SELECT SALT_3 FROM USER_DETAILS_SALT WHERE USERNAME='" + user_name
		+ "' and INSTID='" + inst_id + "') as SALT_3,(SELECT SALT_4 FROM USER_DETAILS_SALT WHERE USERNAME='"
		+ user_name + "' and INSTID='" + inst_id + "') as SALT_4 FROM USER_DETAILS WHERE USERNAME='" + user_name
		+ "' and INSTID='" + inst_id + "'";*/
		
		///by gowtham
		String query_getuserdatils = "SELECT USERID,USERNAME,USERPASSWORD,PROFILEID,FIRSTNAME,LASTNAME,EMAILID,USERSTATUS,"
		+ "DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,CREATIONDATE,LOGINSTATUS,FIRSTTIME,"
		+ "PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,"
		+ "PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,CHANGPASS_DATE,(SELECT SALT_KEY FROM USER_DETAILS_SALT"
		+ " WHERE USERNAME=? and INSTID=?) as SALT_KEY,ADDED_BY,ADDED_DATE,"
		+ "AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,UNBLOCK_AUTHBY,UNBLOCK_AUTHDATE,"
		+ "PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,DELETED_BY,DELETED_DATE,DELETED_FLAG,"
		+ "CBS_USERNAME,PWD_1,PWD_2,PWD_3,PWD_4,(SELECT SALT_1 FROM USER_DETAILS_SALT WHERE USERNAME=? "
		+ "and INSTID=?) as SALT_1,(SELECT SALT_2 FROM USER_DETAILS_SALT WHERE USERNAME=?"
		+ "and INSTID=? ) as SALT_2,(SELECT SALT_3 FROM USER_DETAILS_SALT WHERE"
		+ " USERNAME=? and INSTID=?) as SALT_3,(SELECT SALT_4 FROM USER_DETAILS_SALT "
		+ "WHERE USERNAME=? and INSTID=?) as SALT_4 FROM USER_DETAILS WHERE "
		+ " USERNAME=? and INSTID=? ";
		
		
		enctrace("query_getuserdatils: " + query_getuserdatils);
		IfpTransObj transact = commondesc.myTranObject("CHANGEPASS", txManager);
		String ORG_PWD = null, ORG_SALT = null, PWD_REPEAT_COUNT = null, PWD_CHANGED_COUNT = null, PWD_STATUS = null,
				PWD_1 = null, PWD_2 = null, PWD_3 = null, PWD_4 = null, SALT_1 = null, SALT_2 = null, SALT_3 = null,
				SALT_4 = null;
		String return_val = "NO";
		try {
			
			/*List user_result = jdbctemplate.queryForList(query_getuserdatils);*/
			List user_result = jdbctemplate.queryForList(query_getuserdatils,new Object[]{user_name,inst_id,user_name,inst_id,
					
			user_name,inst_id,user_name,inst_id,user_name,inst_id,user_name,inst_id});
			
			trace("Password requst ...got ::" + user_result.size());

			iterator_userresult = user_result.iterator();
			while (iterator_userresult.hasNext()) {
				Map mapper_userdetails = (Map) iterator_userresult.next();
				db_password = ((String) mapper_userdetails.get("USERPASSWORD"));
				db_salt = ((String) mapper_userdetails.get("SALT_KEY"));
			}

			boolean pswdcheck_result = loginActionClass.checkPassword(db_password, db_salt, oldpassword, inst_id);
			trace("Checking existing password valid...got " + pswdcheck_result);
			if (!pswdcheck_result) {
				System.out.println("  Old Password Is Wrong  ");
				addActionError(" Old Password Is Wrong ");
				trace("Old Password Is Wrong ");
				return "login";
			}
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] bSalt = new byte[8];
			random.nextBytes(bSalt);
			// Digest computation
			byte[] bDigest = pswd_hash_class.getHash(pswd_hash_class.ITERATION_NUMBER, newpassword, bSalt);
			// convert byte Digest to Base64 String
			String hashed_password = pswd_hash_class.byteToBase64(bDigest);
			String sSalt = pswd_hash_class.byteToBase64(bSalt);

			/*
			 * boolean
			 * chck_newpaswd_oldpswd=loginActionClass.checkPassword(db_password,
			 * db_salt,newpassword,inst_id); trace(
			 * "Checking new password and old password...same or not ");
			 * 
			 * if((chck_newpaswd_oldpswd)) { trace(
			 * " New Password Cannot Be Same As Old Password "); addActionError(
			 * "New Password Cannot Be Same As Old Password"); return "login"; }
			 */

				String query_getuserdatils1 = "SELECT USERID,PWD_REPEAT_COUNT,PWD_CHANGED_COUNT,PWD_STATUS,USERNAME,USERPASSWORD,PROFILEID,FIRSTNAME,LASTNAME,EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,CHANGPASS_DATE,(SELECT SALT_KEY FROM USER_DETAILS_SALT WHERE USERID='"
					+ USERID + "' and INSTID='" + inst_id
					+ "') as SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,UNBLOCK_AUTHBY,UNBLOCK_AUTHDATE,PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,DELETED_BY,DELETED_DATE,DELETED_FLAG,CBS_USERNAME,PWD_1,PWD_2,PWD_3,PWD_4,(SELECT SALT_1 FROM USER_DETAILS_SALT WHERE USERID='"
					+ USERID + "' and INSTID='" + inst_id
					+ "') as SALT_1,(SELECT SALT_2 FROM USER_DETAILS_SALT WHERE USERID='" + USERID + "' and INSTID='"
					+ inst_id + "') as SALT_2,(SELECT SALT_3 FROM USER_DETAILS_SALT WHERE USERID='" + USERID
					+ "' and INSTID='" + inst_id + "') as SALT_3,(SELECT SALT_4 FROM USER_DETAILS_SALT WHERE USERID='"
					+ USERID + "' and INSTID='" + inst_id + "') as SALT_4 FROM USER_DETAILS WHERE USERID='" + USERID
					+ "' and INSTID='" + inst_id + "'";
					enctrace("query_getuserdatils: " + query_getuserdatils1);

						List user_result1 = jdbctemplate.queryForList(query_getuserdatils1);
							trace("Password requst ...got ::" + user_result1);

			
		/*	//by gowtham140819
			String query_getuserdatils1 = "SELECT USERID,PWD_REPEAT_COUNT,PWD_CHANGED_COUNT,PWD_STATUS,USERNAME,USERPASSWORD,PROFILEID,FIRSTNAME,LASTNAME,EMAILID,USERSTATUS,DESCRIPTION,BRANCHCODE,IPADDRESS,RETRYCOUNT,USERBLOCK,EXPIRYDATE,CREATIONDATE,LOGINSTATUS,FIRSTTIME,PASSWORDEXPIRYDATE,PASSWORDEXPIRYFLAG,MODIFIEDDATE,INSTID,CREATEDUSERID,FORGOTPASSWORDFLAG,LASTLOGIN,USERTYPE,PSWREPEATCOUNT,FRSTLOGINDATE,FRSTLOGIN,FORCEPSWEXP,CHANGPASS_DATE,(SELECT SALT_KEY FROM USER_DETAILS_SALT WHERE USERID='"
					+ USERID + "' and INSTID='" + inst_id
					+ "') as SALT_KEY,ADDED_BY,ADDED_DATE,AUTH_STATUS,AUTH_BY,AUTH_DATE,REMARKS,UNBLOCKED_BY,UNBLOCKED_DATE,UNBLOCK_AUTHBY,UNBLOCK_AUTHDATE,PASSWDRESET_BY,PASSWDRESET_DATE,PASSWD_RESETAUTHBY,PASSWD_RESETDATE,DELETED_BY,DELETED_DATE,DELETED_FLAG,CBS_USERNAME,PWD_1,PWD_2,PWD_3,PWD_4,(SELECT SALT_1 FROM USER_DETAILS_SALT WHERE USERID='"
					+ USERID + "' and INSTID='" + inst_id
					+ "') as SALT_1,(SELECT SALT_2 FROM USER_DETAILS_SALT WHERE USERID=? and INSTID=?) as SALT_2,(SELECT SALT_3 FROM USER_DETAILS_SALT WHERE USERID=? and INSTID=?) as SALT_3,(SELECT SALT_4 FROM USER_DETAILS_SALT WHERE USERID=? and INSTID=?) as SALT_4 FROM USER_DETAILS WHERE USERID=? and INSTID=?";
					enctrace("query_getuserdatils: " + query_getuserdatils1);
					List user_result1 = jdbctemplate.queryForList(query_getuserdatils1,new Object[]{USERID,inst_id,inst_id,USERID,inst_id,USERID,inst_id,USERID,inst_id});
					trace("Password requst ...got ::" + user_result1);
					trace("Password requst ...got ::" + user_result1);*/
			
			
			iterator_userresult = user_result1.iterator();
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

			if (("1".equalsIgnoreCase(PWD_REPEAT_COUNT))) {
				boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword, inst_id);
				if ((chck_newpaswd_oldpswd1)) {
					trace(" New Password Cannot Be Same As Your 4 Old Password ");
					addActionError("New Password Cannot Be Same As Your Previous 4 Password");
					return "forcepasswordpage";
				}
			}

			else if ("2".equalsIgnoreCase(PWD_REPEAT_COUNT)) {
				trace("2 times password check");
				if ((PWD_1 != null && SALT_1 != null)) {
					trace("pwd1");
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword,
							inst_id);
					trace("password check result-->" + chck_newpaswd_oldpswd1);
					if ((chck_newpaswd_oldpswd1)) {
						trace("pwd1");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						return "forcepasswordpage";
					}
				}

				if ((PWD_2 != null && SALT_2 != null)) {
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_2, SALT_2, newpassword,
							inst_id);
					trace("password check result-->" + chck_newpaswd_oldpswd1);
					if ((chck_newpaswd_oldpswd1)) {
						trace("pwd2");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						return "forcepasswordpage";
					}
				}
			}

			else if ("3".equalsIgnoreCase(PWD_REPEAT_COUNT)) {
				if ((PWD_3 != null && SALT_3 != null))

				{
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_3, SALT_3, newpassword,
							inst_id);
					if ((chck_newpaswd_oldpswd1)) {
						System.out.println("New Password Cannot Be Same As Your Previous Passwords");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						return "forcepasswordpage";
					}
				}

				if ((PWD_2 != null && SALT_2 != null))

				{
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_2, SALT_2, newpassword,
							inst_id);
					if ((chck_newpaswd_oldpswd1)) {
						System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						return "forcepasswordpage";
					}
				}

				if ((PWD_1 != null && SALT_1 != null))

				{
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword,
							inst_id);
					if ((chck_newpaswd_oldpswd1)) {
						System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						return "forcepasswordpage";
					}
				}
			}

			else if ("4".equalsIgnoreCase(PWD_REPEAT_COUNT)) {
				trace("inside 4 th password repeat count");
				
				if ((PWD_4 != null && SALT_4 != null)) {
					trace("inside PWD_4");
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_4, SALT_4, newpassword,
							inst_id);
					if ((chck_newpaswd_oldpswd1)) {
						System.out.println("New Password Cannot Be Same As Your Previous Passwords");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						transact.txManager.rollback(transact.status);
						//session.setAttribute("preverr", "S");
						session.setAttribute("message", "New Password Cannot Be Same As Your Previous 4 Password");
						//return "forcepasswordpage";
						return "login";
					}
				}

				if ((PWD_3 != null && SALT_3 != null)) {
					trace("inside PWD_3");
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_3, SALT_3, newpassword,
							inst_id);
					if ((chck_newpaswd_oldpswd1)) {
						System.out.println("New Password Cannot Be Same As Your Previous Passwords");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						transact.txManager.rollback(transact.status);
						//session.setAttribute("preverr", "S");
						session.setAttribute("message", "New Password Cannot Be Same As Your Previous 4 Password");
						return "login";
					}
				}

				if ((PWD_2 != null && SALT_2 != null)) {
					trace("inside PWD_2");
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_2, SALT_2, newpassword,
							inst_id);
					if ((chck_newpaswd_oldpswd1)) {
						System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						transact.txManager.rollback(transact.status);
						//session.setAttribute("preverr", "S");
						session.setAttribute("message", "New Password Cannot Be Same As Your Previous 4 Password");
						return "login";
					}
				}
				if ((PWD_1 != null && SALT_1 != null)) {
					trace("inside PWD_1");
					boolean chck_newpaswd_oldpswd1 = loginActionClass.checkPassword(PWD_1, SALT_1, newpassword,
							inst_id);
					if ((chck_newpaswd_oldpswd1)) 
					{
						System.out.println("New Password Cannot Be Same As Your 4 Old Passwords");
						trace(" New Password Cannot Be Same As Your 4 Old Password ");
						addActionError("New Password Cannot Be Same As Your Previous 4 Password");
						transact.txManager.rollback(transact.status);
						//session.setAttribute("preverr", "S");
						session.setAttribute("message", "New Password Cannot Be Same As Your Previous 4 Password");
						return "login";
					}
				}
			}

			System.out.println("rPassword---" + newpassword);
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
						&& (Integer.parseInt(sTotalCount) <= sTOTAL)) {
					trace("inside for if con :" + sLowerCase + sLOWER + sUpperCase + sUPPER + sNumbers + sDIGIT
							+ sSpecial + sSPECIAL + sFirstChar + sFIRST + sLastChar + sLAST + sTotalCount + sTOTAL);
					int chpwdupdatestatus = 0;
					synchronized (this) {

						trace("TTTTTTTTTTTT" + PWD_CHANGED_COUNT);
						if (("0".equalsIgnoreCase(PWD_CHANGED_COUNT))) {
							trace("UUUUUUU");
							
							/*String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= '" + hashed_password
							+ "', PWD_1= '" + hashed_password+ "',PWD_STATUS='PWD_1',PWD_CHANGED_COUNT='1',RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='"
							+ USERID + "' and INSTID='" + inst_id + "'";
							enctrace(" update_query-update_query " + update_query);
							int chpwdupdatestatus1 = jdbctemplate.update(update_query);*/
							
							/*String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= '" + hashed_password
							+ "', PWD_1= '" + hashed_password+ "',PWD_STATUS='PWD_1',PWD_CHANGED_COUNT='1',RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='"
							+ USERID + "' and INSTID='" + inst_id + "'";
							enctrace(" update_query-update_query " + update_query);
							int chpwdupdatestatus1 = jdbctemplate.update(update_query);*/
							
							///by gowtham
		String update_query = "UPDATE USER_DETAILS SET USERPASSWORD=?, PWD_1=?, PWD_STATUS='PWD_1',PWD_CHANGED_COUNT=?,"
				+ "RETRYCOUNT=?,FIRSTTIME =?,LOGINSTATUS =? WHERE  USERID=? and INSTID=?";
		enctrace(" update_query-update_query " + update_query);
		int chpwdupdatestatus1 = jdbctemplate.update(update_query,new Object[]{
		hashed_password,hashed_password,"1","0","0","0",USERID,inst_id});
							
							trace("chpwdupdatestatus1*chpwdupdatestatus1" + chpwdupdatestatus1);

							/*update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY='" + sSalt + "',SALT_1='" + sSalt
									+ "',RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='" + USERID
									+ "' and INSTID='" + inst_id + "'";
							enctrace(" update_query+update_query " + update_query);
							chpwdupdatestatus1 = jdbctemplate.update(update_query);*/
							
							update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY=? ,SALT_1=? ,RETRYCOUNT=? ,FIRSTTIME =?,LOGINSTATUS = ? WHERE  USERID=? and INSTID=? ";
							enctrace(" update_query+update_query " + update_query);
							chpwdupdatestatus1 = jdbctemplate.update(update_query,new Object[]{sSalt,sSalt,"0","0","0",USERID,inst_id});
							
							trace("chpwdupdatestatus1&chpwdupdatestatus1" + chpwdupdatestatus1);

							if (chpwdupdatestatus1 == 1) {
								transact.txManager.commit(transact.status);
								session.setAttribute("preverr", "S");
								session.setAttribute("prevmsg", "Password has been Changed Sucessfully");
								trace("Password has been Changed Sucessfully Committed");
								return "login";
							}
						}

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
						
						/*String update_query = "UPDATE USER_DETAILS SET USERPASSWORD= '" + hashed_password + "',"
								+ condition1 + ",PWD_CHANGED_COUNT='" + pwd_change_count
								+ "'+1, RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='" + USERID
								+ "' and INSTID='" + inst_id + "'";
						enctrace(" query for change password " + update_query);
						chpwdupdatestatus = jdbctemplate.update(update_query);
						
						trace("Updating new password . main..got :" + chpwdupdatestatus);

						update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY='" + sSalt + "'," + condition2
								+ ",RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='" + USERID
								+ "' and INSTID='" + inst_id + "'";
						enctrace(" query for change password " + update_query);
						chpwdupdatestatus = jdbctemplate.update(update_query);*/
						
						///by gowtham
						String update_query = "UPDATE USER_DETAILS SET USERPASSWORD=? ,"+ condition1 + ",PWD_CHANGED_COUNT='" +
						pwd_change_count+ "'+1, RETRYCOUNT='0',FIRSTTIME ='0',"
						+ "LOGINSTATUS = '0' WHERE  USERID=? and INSTID=?";
						enctrace(" query for change password " + update_query);
						chpwdupdatestatus = jdbctemplate.update(update_query,new Object[]{hashed_password,USERID,inst_id});
						
						trace("Updating new password . main..got :" + chpwdupdatestatus);

						update_query = "UPDATE USER_DETAILS_SALT SET SALT_KEY=?," + condition2
						+ ",RETRYCOUNT=?,FIRSTTIME =?,LOGINSTATUS =? WHERE  USERID=? and INSTID=? ";
						enctrace(" query for change password " + update_query); 
						chpwdupdatestatus = jdbctemplate.update(update_query,new Object[]{sSalt,"0","0","0",USERID,inst_id});
						
						trace("Updating new password ...temp..got :" + chpwdupdatestatus);

						/*
						 * String update_query =
						 * "UPDATE USER_DETAILS SET USERPASSWORD= '"
						 * +hashed_password+"', PWD_1= '"+hashed_password+
						 * "',PWD_STATUS='PWD_1',PWD_CHANGED_COUNT='1',RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='"
						 * +USERID+"' and INSTID='"+inst_id+"'"; enctrace(
						 * " query for change password "+update_query);
						 * chpwdupdatestatus =
						 * jdbctemplate.update(update_query); trace(
						 * "Updating new password . main..got :" +
						 * chpwdupdatestatus );
						 * 
						 * update_query =
						 * "UPDATE USER_DETAILS_SALT SET SALT_KEY='"
						 * +sSalt+"',SALT_1='"+sSalt+
						 * "',RETRYCOUNT='0',FIRSTTIME ='0',LOGINSTATUS = '0' WHERE  USERID='"
						 * +USERID+"' and INSTID='"+inst_id+"'"; enctrace(
						 * " query for change password "+update_query);
						 * chpwdupdatestatus =
						 * jdbctemplate.update(update_query); trace(
						 * "Updating new password ...temp..got :" +
						 * chpwdupdatestatus );
						 */
					}
					if (chpwdupdatestatus < 0) {
						trace(" Error While Update password ");
						transact.txManager.rollback(transact.status);
						addActionError("Error While Update password");
						return "login";
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
					if (!sFirstChar.equals("-1")) {
						result = result + "<br/> First Letter Should be : " + sFirstCharString;
					}
					if (!sLastChar.equals("-1")) {
						result = result + "<br/> Last Letter Should be : " + sLastCharString;
					}
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", result);
					trace("resssss---" + result.toString());
					getResponse().getWriter().write(res.toString());
					return "changepasswordpage";
				}

			}

			transact.txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Password has been Changed Sucessfully");
			trace("Password has been Changed Sucessfully Committed");

			/************* AUDIT BLOCK **************/
			try {
				auditbean.setAuditactcode("30");

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg(" Password has been forcefully changed ");
				auditbean.setUsercode(user_name);
				auditbean.setAuditactcode("30");
				commondesc.insertAuditTrail(inst_id, user_name, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} 
		catch (Exception e) 
		{
			transact.txManager.rollback(transact.status);
			addActionError("Could not change the password");
			trace("Data Base Error has been Occured while changing password.." + e);
			e.printStackTrace();
		}
		return "login";
	}

	/*
	 * private static String passwordchangeresult;
	 * 
	 * 
	 * public String getPasswordchangeresult() { return passwordchangeresult; }
	 * public void setPasswordchangeresult(String passwordchangeresult) {
	 * this.passwordchangeresult = passwordchangeresult; }
	 */

	private List mkrchkrmenu;
	private String profilelist;

	public String getProfilelist() {
		return profilelist;
	}

	public void setProfilelist(String profilelist) {
		this.profilelist = profilelist;
	}

	public List getMkrchkrmenu() {
		return mkrchkrmenu;
	}

	public void setMkrchkrmenu(List mkrchkrmenu) {
		this.mkrchkrmenu = mkrchkrmenu;
	}

	public String viewmkckr() {
		trace("*************** Adding viewmkckr Begins **********");
		enctrace("*************** Adding viewmkckr  Begins **********");
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		String in_name = i_Name.toUpperCase();
		try {
			
			/*String mkck_countquery = "SELECT count(*) from MKRCHKR_CONFIG where INSTID='" + in_name + "'";
			System.out.println("mkck_countquery==> " + mkck_countquery);
			int count = jdbctemplate.queryForInt(mkck_countquery);*/

			
			//by gowtham
			String mkck_countquery = "SELECT count(*) from MKRCHKR_CONFIG where INSTID=? ";
			System.out.println("mkck_countquery==> " + mkck_countquery);
			int count = jdbctemplate.queryForInt(mkck_countquery,new Object[]{in_name});
			System.out.println(" list befor initialization result" + count);
			if (count != 0) {
				System.out.println("Maker Checker Configured");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Maker Checker Already Configured");
			} else {
				
		/*String mkck_query = "select MENUNAME,MENUID from " + getMENU()+ " where MAK_CHCK_VISIBILITY='1' ORDER BY MENUORDER ASC";
		System.out.println("mkck_query===> " + mkck_query);
		List mkcklist = jdbctemplate.queryForList(mkck_query);*/
//by gowtham
	String mkck_query = "select MENUNAME,MENUID from " + getMENU()+ ""
	+ " where MAK_CHCK_VISIBILITY=? ORDER BY MENUORDER ASC";
	System.out.println("mkck_query===> " + mkck_query);
	List mkcklist = jdbctemplate.queryForList(mkck_query,new Object[]{"1"});
				
				
				System.out.println("mkcklist==> " + mkcklist);
				if (!(mkcklist.isEmpty())) {
					setMkrchkrmenu(mkcklist);
					session.setAttribute("curerr", "S");
				} else {
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "No Menu Found to Configure Maker Checker");
				}
			}
		} catch (DataAccessException e) {
			System.out.println("Error==> " + e.getMessage());
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error: " + e.getMessage());
		}

		trace("\n\n\n");
		enctrace("\n\n\n");
		return "mkrchkr";
	}

	private char mkrchkr_exists;

	public char getMkrchkr_exists() {
		return mkrchkr_exists;
	}

	public void setMkrchkr_exists(char mkrchkr_exists) {
		this.mkrchkr_exists = mkrchkr_exists;
	}

	public String insertmkckr() {
		trace("*************** Insert Maker Checker Begins **********");
		enctrace("*************** Insert Maker Checker Begins **********");
		IfpTransObj transact = commondesc.myTranObject("INSMKCK", txManager);

		ArrayList<String> Allmenu = new ArrayList<String>();
		String C_fvalue[] = (getRequest().getParameterValues("mainmenu"));
		try {
			int i;
			if (C_fvalue != null) {

				Allmenu.add("00");
				for (i = 0; i < C_fvalue.length; i++) {
					Allmenu.add(C_fvalue[i]);
					System.out.println("C_fvalue[i]   ----->" + C_fvalue[i]);

				}
				Allmenu.add("D-1, D-2, 00");
			} else {
				System.out.println("THE VALUE from the checkbox<<<<<== none");

			}
			// System.out.println (" Profile priviledge value --->" +Allmenu);

			HttpSession session = getRequest().getSession();

			String i_Name = (String) session.getAttribute("Instname");
			String in_name = i_Name.toUpperCase();

			/*String inst_name = "SELECT count(*) from MKRCHKR_CONFIG where INSTID='" + in_name + "'";
			trace("instance name " + inst_name);
			int count = jdbctemplate.queryForInt(inst_name);*/

//by gowtham
			String inst_name = "SELECT count(*) from MKRCHKR_CONFIG where INSTID=? ";
			trace("instance name " + inst_name);
			int count = jdbctemplate.queryForInt(inst_name,new Object[]{in_name});
			
			if (count == 0) {
				
				/*String menupriv_insert = "INSERT INTO MKRCHKR_CONFIG(INSTID,MENUID) VALUES ('" + in_name + "','"
						+ Allmenu + "')";
				int menupriv = jdbctemplate.update(menupriv_insert);*/
	
				//byg owtham
				String menupriv_insert = "INSERT INTO MKRCHKR_CONFIG(INSTID,MENUID) VALUES (?,?)";
				int menupriv = jdbctemplate.update(menupriv_insert,new Object[]{in_name,Allmenu});
				
				if (menupriv == 1) {
					transact.txManager.commit(transact.status);
					System.out.println("+++++Insert Query Return Values is +++++\n" + menupriv);
					maker_checker_display = "Maker Checker Configuration has been Configured Sucessfully";
					trace("Maker Checker Configuration has been Configured Sucessfully");
					trace("\n\n\n");
					enctrace("\n\n\n");
					return "insmkrchkr";
				} else {
					transact.txManager.rollback(transact.status);
					System.out.println("+++++Insert Query Return Values is +++++\n" + menupriv);
					maker_checker_display = "Maker Checker Configuration Failure ";
					trace("Maker Checker Configuration Failure ");
					trace("\n\n\n");
					enctrace("\n\n\n");
					return "insmkrchkr";
				}

			}
			if (count != 0) {
				maker_checker_display = "Maker Checker already Configured For This Institute...Plz Check";
				trace("Maker Checker already Configured For This Institute...Plz Check ");
				trace("\n\n\n");
				enctrace("\n\n\n");
				return "insmkrchkr";
			}
		} catch (Exception e) {
			maker_checker_display = "Exception occure While Maker Checker Configured..";
			trace("\n\n\n");
			enctrace("\n\n\n");
		}
		return "insmkrchkr";
	}

	private String maker_checker_display;

	public String getMaker_checker_display() {
		return maker_checker_display;
	}

	public void setMaker_checker_display(String maker_checker_display) {
		this.maker_checker_display = maker_checker_display;
	}

	private List username;

	public List getUsername() {
		return username;
	}

	public void setUsername(List username) {
		this.username = username;
	}

	private List allusersangeetha;

	public List getAllusersangeetha() {
		return allusersangeetha;
	}

	public void setAllusersangeetha(List allusersangeetha) {
		this.allusersangeetha = allusersangeetha;
	}

	public String viewUserDet() {
		trace("*************** view User Details Action Begins **********");
		enctrace("*************** view User Details Action Begins **********");
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");

		try {

			String usercodecond = "", instidcond = "";
			if (usertype.equals("SUPERADMIN")) {
				usercodecond = " and usr.createduserid='SU' ";
			} else {
				String cuserid = (String) session.getAttribute("USERID");
				trace("cuserid is:" + cuserid);
				// usercodecond = " and usr.createduserid='"+cuserid+"' ";
				instname = comInstId(session);
				instidcond = "  AND usr.INSTID='" + instname + "'";
			}

			String qury = "select brch.BRANCH_NAME,usr.USERNAME,usr.INSTID,usr.PROFILEID,usr.USERID,usr.USERSTATUS,"
			+ "decode(usr.USERBLOCK,'1','Blocked','0','Not Blocked') as USERBLOCK,prof.PROFILE_ID,prof.PROFILE_NAME, "
			+ "usr.added_by, usr.auth_by, to_char(usr.added_date,'dd-mon-yyyy') as added_date,  ";
			qury += "  to_char(usr.auth_date,'dd-mon-yyyy') as auth_date, usr.REMARKS,  usr.AUTH_STATUS from "
			+ getProfilelistTemp()+ " prof,USER_DETAILS usr,BRANCH_MASTER brch where usr.instid = prof.instid and"
			+ " brch.INST_ID=usr.instid and prof.PROFILE_ID = usr.PROFILEID and usr.USERSTATUS !='2' and "
			+ "brch.BRANCH_CODE=usr.BRANCHCODE "+ usercodecond + " " + instidcond + " order by instid,USERID";
			enctrace("qury is:" + qury);
			List data = jdbctemplate.queryForList(qury);
			
			
			trace("result list is:" + data.size());
			if (data.isEmpty()) {
				trace("No Records found for view ");
				addActionError("No Records Found ");

				return "required_home";
			}
			String addedby = "", authby = "", authstatus = "", userstatus = "", userstatusdesc = "";
			ListIterator itr = data.listIterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				if (!usertype.equals("SUPERADMIN")) {
					addedby = commondesc.getUserName(instname, (String) mp.get("ADDED_BY"), jdbctemplate);
					trace("Getting added by username ...got : " + addedby);
					authby = commondesc.getUserName(instname, (String) mp.get("AUTH_BY"), jdbctemplate);
					trace("Getting auth by username ...got : " + authby);
				} else {
					addedby = (String) mp.get("ADDED_BY");
					authby = (String) mp.get("AUTH_BY");
				}

				authstatus = (String) mp.get("AUTH_STATUS");

				if (authstatus.equals("0")) {
					authstatus = "Waiting for authorization";
				} else if (authstatus.equals("1")) {
					authstatus = "Authorized";
				} else if (authstatus.equals("9")) {
					authstatus = "De-Authorized";
				}
				mp.put("AUTH_STATUS", authstatus);

				userstatus = (String) mp.get("USERSTATUS");
				if (userstatus.equals("1")) {
					userstatusdesc = "Active";
				} else if (userstatus.equals("0")) {
					userstatusdesc = "In-Active";
				} else if (userstatus.equals("2M")) {
					userstatusdesc = "Deleted ";
				}
				trace("username : " + username + " : status  " + userstatusdesc);
				mp.put("USERSTATUS", userstatusdesc);

				mp.put("ADDED_BY", addedby);
				mp.put("AUTH_BY", authby);
				itr.remove();
				itr.add(mp);
			}
			setUsername(data);

		} catch (Exception e) {
			trace("Exception...." + e.getMessage());
			addActionError("Could not get records ");
			e.printStackTrace();
		}

		return "viewuser";

	}

	public String delUser() {
		trace("delUser Method Begins **********");
		enctrace("delUser Method Begins **********");
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");
		// String PROFILEID = (String)session.getAttribute("PROFILEID");
		// String PROFILE_ID = PROFILEID.toUpperCase();
		String cuserid = (String) session.getAttribute("USERID");
		String act = getRequest().getParameter("act");
		String usertype = (String) session.getAttribute("USERTYPE");
		if (act != null) {
			session.setAttribute("act", act);
		}

		try {
			if (usertype.equals("SUPERADMIN")) {

				List institutes_list = instdao.getListOfInstitutionFromProduction(jdbctemplate);
				trace("institutes_list List is:" + institutes_list.size());
				if (!institutes_list.isEmpty()) {
					setInstitutionlist(institutes_list);
					session.setAttribute("Err_status", "S");
				}
				return "deleteinstadminuser";
			} else {
				instname = (String) session.getAttribute("Instname");
				
				/*String del_qury = "select count(*) from USER_DETAILS where instid='" + instname
						+ "' and CREATEDUSERID='" + cuserid + "'";
				enctrace("del_qury is:" + del_qury);
				int delqry = jdbctemplate.queryForInt(del_qury);*/
				
				//by gowtham-140819
				String del_qury = "select count(*) from USER_DETAILS where instid=? and CREATEDUSERID=?";
				enctrace("del_qury is:" + del_qury);
				int delqry = jdbctemplate.queryForInt(del_qury,new Object[]{instname,cuserid});
				
				if (delqry == 0) {
					trace("Count From DELETE User is Zero" + delqry);
					setDel_user('Y');
				} else {
					setDel_user('N');
					trace("Count From DELETE User is not Zero" + delqry);
				}
			/*	String instqury = "select * from USER_DETAILS where instid='" + instname
						+ "'  and ( USERSTATUS != '2' AND USERSTATUS != '2M' )"; // and
																					// CREATEDUSERID='"+cuserid+"'
				enctrace("instqury isJOHN:" + instqury);
				List users_list = jdbctemplate.queryForList(instqury);
				trace("users_list is:" + users_list.size());*/
				
				//by gowtham-140819
				String instqury = "select * from USER_DETAILS where instid=?and ( USERSTATUS !=?AND USERSTATUS != ?)"; // and
				// CREATEDUSERID='"+cuserid+"'
				enctrace("instqury isJOHN:" + instqury);
				List users_list = jdbctemplate.queryForList(instqury,new Object[]{instname,"2","2M"});
				trace("users_list is:" + users_list.size());
				if (!users_list.isEmpty()) {
					setInstusername(users_list);
					session.setAttribute("Eror_status", "S");
					trace("User is Not EMpty " + users_list);
				}
			}
		} catch (Exception e) {
			addActionError("Exception...could not continue the process");
			trace("Exception....." + e.getMessage());
			return "required_home";
		}
		return "getinstuser";
	}

	private char del_user;

	public char getDel_user() {
		return del_user;
	}

	public void setDel_user(char del_user) {
		this.del_user = del_user;
	}

	public String inst_bean;

	public String getInst_bean() {
		return inst_bean;
	}

	public void setInst_bean(String inst_bean) {
		this.inst_bean = inst_bean;
	}

	public String getInstadminuser() {
		trace("*************** Adding getInstadminuser Begins **********");
		enctrace("*************** Adding getInstadminuser  Begins **********");
		HttpSession session = getRequest().getSession();
		String instname = (getRequest().getParameter("instname"));
		setInst_bean(instname);
		
		/*String delqury = "Select count(*) from USER_DETAILS where INSTID = '" + instname
				+ "' and USERSTATUS !='2'and CREATEDUSERID='SU'";
		int del = jdbctemplate.queryForInt(delqury);*/
		
		String delqury = "Select count(*) from USER_DETAILS where INSTID = ? and USERSTATUS !=?and CREATEDUSERID=?";
		int del = jdbctemplate.queryForInt(delqury,new Object[]{instname,"2","SU"});
		
		if (del == 0) {
			trace("Count From DELETE User is Zero" + del);
			setDel_user('Y');
		} else {
			setDel_user('N');
			trace("Count From DELETE User is not Zero" + del);
		}

		try {
			/*String instqury = "Select USERID,USERNAME from USER_DETAILS where INSTID = '" + instname
					+ "' and USERSTATUS !='2'and CREATEDUSERID='SU'";
			enctrace("Uinstqury:" + instqury);
			List inst_users_list = jdbctemplate.queryForList(instqury);*/
			
			
			//by gowtham-140819
			String instqury = "Select USERID,USERNAME from USER_DETAILS where INSTID = ? and USERSTATUS !=?and CREATEDUSERID=?";
			enctrace("Uinstqury:" + instqury);
			List inst_users_list = jdbctemplate.queryForList(instqury,new Object[]{instname,"2","SU"});
			System.out.println("User NAme IS:" + inst_users_list);
			if (!(inst_users_list.isEmpty())) {
				setInstusername(inst_users_list);
				session.setAttribute("Eror_status", "S");
			}
		} catch (Exception e) {
			session.setAttribute("adminusersdetails", "Error While Fetching the Users");
			session.setAttribute("Eror_status", "E");
		}

		trace("\n\n\n");
		enctrace("\n\n\n");
		return "getadmininstuser";
	}

	public String getInstadminuserdetails() {
		trace("*************** getInstadminuserdetails Begins **********");
		enctrace("*************** getInstadminuserdetails Begins **********");
		HttpSession session = getRequest().getSession();
		String username = (getRequest().getParameter("username"));
		String ss_username = (String) session.getAttribute("SS_USERNAME");
		String institution_id;
		String usertype = (String) session.getAttribute("USERTYPE");
		if (usertype.equals("SUPERADMIN")) {
			institution_id = getRequest().getParameter("inst_id");
			System.out.println("institution_id---> " + institution_id);
			setInstitutionid(institution_id);
		} else {
			institution_id = (String) session.getAttribute("Instname");
			System.out.println("institution_id---> " + institution_id);
			setInstitutionid(institution_id);
			System.out.println("institution_id---> " + institution_id);
		}

		// String userqury="select
		// usr.USERID,usr.USERNAME,usr.PROFILEID,usr.FIRSTNAME,usr.LASTNAME,usr.EMAILID,decode(usr.USERSTATUS,'1','Active','0','In
		// Activate') as USERSTATUS,prof.PROFILE_ID,prof.PROFILE_NAME from
		// USER_DETAILS usr,"+getProfilelistTemp()+" prof where usr.USERID =
		// '"+username+"' and usr.PROFILEID=prof.PROFILE_ID ";
		
		
		String userqury = "select USERNAME,USERID,INSTID,FIRSTNAME,LASTNAME,EMAILID,decode(USERSTATUS,'1','Active','0','In Active','2M','Deleted,  Waiting for authorization') as USERSTATUS,PROFILEID from USER_DETAILS where USERID='"
				+ username + "' and INSTID='" + institution_id + "'";
		System.out.println("userqury---> " + userqury);
		enctrace("User NAme IS:" + userqury);
		
		
		//by gowtham-140819
				/*String userqury = "select USERNAME,USERID,INSTID,FIRSTNAME,LASTNAME,EMAILID,decode(USERSTATUS,'1','Active','0','In Active','2M','Deleted, Waiting for authorization') as USERSTATUS,PROFILEID from USER_DETAILS where USERID=? and INSTID=?";
				System.out.println("userqury---> " + userqury);
				enctrace("User NAme IS:" + userqury);*/
		
		try {
			List userdetails_list = jdbctemplate.queryForList(userqury);
			trace("userdetails_list is:" + userdetails_list.size());
			if (!(userdetails_list.isEmpty())) {
				ListIterator iterator_userdetails = userdetails_list.listIterator();
				System.out.println("iterator_userdetails----> " + iterator_userdetails);
				while (iterator_userdetails.hasNext()) {
					Map userdetails = (Map) iterator_userdetails.next();
					BigDecimal profileid = (BigDecimal) userdetails.get("PROFILEID");
					
					System.out.println("profile_id----- > " + profileid);
					String profilename = commondesc.getProfileDesc(institution_id, profileid, jdbctemplate);
					System.out.println("profilename----> " + profilename);
					userdetails.put("PROFILE_NAME", profilename);
				}
				setUserdetail(userdetails_list);
				session.setAttribute("Errstatus", "S");
				trace("userdetails_list is Not Empty");
			} else {
				session.setAttribute("instadminuserinfo", "User Details Not Found");
				session.setAttribute("Errstatus", "E");
				trace("userdetails_list is Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			trace("Error While Fetching the User Details ");
			session.setAttribute("instadminuserinfo", "Error While Fetching the User Details ");
			session.setAttribute("Errstatus", "E");
		}
		return "getinstadminuserdetail";
	}

	private List userdetail;

	public List getUserdetail() {
		return userdetail;
	}

	public void setUserdetail(List userdetail) {
		this.userdetail = userdetail;
	}

	public String getDeleteStatus(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		String deletestatus = null;
		try {
			
			/*String deletestatusqry = "SELECT USERSTATUS FROM USER_DETAILS WHERE INSTID='" + instid + "' AND USERID='"
					+ usercode + "'";
			enctrace("deletestatusqry :" + deletestatusqry);
			deletestatus = (String) jdbctemplate.queryForObject(deletestatusqry, String.class);*/
			
			//by gowtham-140819
			String deletestatusqry = "SELECT USERSTATUS FROM USER_DETAILS WHERE INSTID=? AND USERID=?";
			enctrace("deletestatusqry :" + deletestatusqry);
			deletestatus = (String) jdbctemplate.queryForObject(deletestatusqry, new Object[]{instid,usercode},String.class);
			
		} catch (EmptyResultDataAccessException e) {
		}
		return deletestatus;
	}

	public String getDeletedUser(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		String deletestatus = null;
		try {
			/*String deletestatusqry = "SELECT DELETED_BY FROM USER_DETAILS WHERE INSTID='" + instid + "' AND USERID='"
					+ usercode + "'";
			enctrace("deletestatusqry :" + deletestatusqry);
			deletestatus = (String) jdbctemplate.queryForObject(deletestatusqry, String.class);*/
			
			String deletestatusqry = "SELECT DELETED_BY FROM USER_DETAILS WHERE INSTID=? AND USERID=?";
			enctrace("deletestatusqry :" + deletestatusqry);
			deletestatus = (String) jdbctemplate.queryForObject(deletestatusqry,new Object[]{instid,usercode}, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return deletestatus;
	}

	public String deleteInstUserAuthHome() {
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");
		if (usertype.equals("SUPERADMIN")) {

			List instidlist = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			setInstitutionlist(instidlist);
			return "inst_deleteauthhome";
		} else {
			return deleteUserAuthHome();
		}
	}

	public String deleteUserAuthHome() {
		List deleteuserlist = null;
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");
		String instid = getRequest().getParameter("instid");
		if (instid == null) {
			instid = comInstId(session);
		}

		String condition = "";
		if (usertype.equals("SUPERADMIN")) {
			String loginuser = (String) session.getAttribute("EXTERNALUSER");
			condition = " AND DELETED_BY != '" + loginuser + "' ";
		}
		try {

			String instname = commondesc.getInstDesc(instid, jdbctemplate);
			trace("Getting instid...got : " + instname);
			setInstname(instname);
			setInstitutionid(instid);

			/*String deletestatusqry = "SELECT USERNAME,USERID FROM USER_DETAILS WHERE INSTID='" + instid
					+ "' AND USERSTATUS='2M'" + condition;
			enctrace("deletestatusqry :" + deletestatusqry);
			deleteuserlist = jdbctemplate.queryForList(deletestatusqry);*/
			
			
			//by gowtham-140819
			String deletestatusqry = "SELECT USERNAME,USERID FROM USER_DETAILS WHERE INSTID='" + instid+ "' AND USERSTATUS='2M'" + condition;
			enctrace("deletestatusqry :" + deletestatusqry);
			deleteuserlist = jdbctemplate.queryForList(deletestatusqry,new Object[]{instid,"2M",condition});
			if (deleteuserlist.isEmpty()) {
				addActionError("No records available ");
				trace("No records availabled....");
				return "required_home";
			}
			setUsername(deleteuserlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "deleteuser_authhome";
	}

	public String deleleteInstadminuser() {
		trace("*************** deleleteInstadminuser Begins **********");
		enctrace("*************** deleleteInstadminuser Begins **********");
		HttpSession session = getRequest().getSession();

		IfpTransObj transact = commondesc.myTranObject("DELINSTADMIN", txManager);
		String loginusercode = "";
		String userid = (getRequest().getParameter("userid"));
		trace("userid==> " + userid);
		String username = (getRequest().getParameter("username"));
		String instid = (getRequest().getParameter("instid"));
		String usertype = (String) session.getAttribute("USERTYPE");
		String authcode = "", authmsg = "", remarks = "";
		loginusercode = (String) session.getAttribute("EXTERNALUSER");
		
		
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Date date=new Date();
		try {

			String deletestatus = this.getDeleteStatus(instid, userid, jdbctemplate);
			trace("Getting delete status...got : " + deletestatus);

			String deleteduser = this.getDeletedUser(instid, userid, jdbctemplate);
			trace("Getting deleted by user code ...got ; " + deleteduser);

			if (usertype.equals("SUPERADMIN")) {

				String dualauthenabled = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
				trace("Getting dual auth need for superadmin : " + dualauthenabled);
				if (dualauthenabled.equals("Y")) {
					authcode = "2M";
					authmsg = ". Waiting for authorization";
				} else {
					authcode = "2";
				}
			} else {
				String act = (String) session.getAttribute("act");
				System.out.println("act---->"+act);
				if (act != null && act.equals("D")) {
					authcode = "2";
				} else {
					authcode = "2";
					authmsg = " Sucessfully";
				}
			}

			/*
			 * if( deletestatus.equals("2M") &&
			 * !deleteduser.equals(loginusercode) ){ authcode="2"; authmsg = "";
			 * }
			 */
			//added by gowtham_220719
			trace("ip address======>  "+ip);
			auditbean.setIpAdress(ip);
			auditbean.setActmsg("User " + username + " Deleted. " + authmsg);

		/*	String user_delete_query = "update USER_DETAILS set USERSTATUS = '" + authcode + "', DELETED_BY='"
					+ loginusercode + "', DELETED_DATE=sysdate  where USERID='" + userid + "' and INSTID='" + instid
					+ "'";
			trace("User Delete query:" + user_delete_query);
			int res = jdbctemplate.update(user_delete_query);*/
			
			
			
			//by gowtham-140819
			String user_delete_query = "update USER_DETAILS set USERSTATUS =?, DELETED_BY=?, DELETED_DATE=?where USERID=? and INSTID=?";
			trace("User Delete query:" + user_delete_query);
			int res = jdbctemplate.update(user_delete_query,new Object[]{authcode,loginusercode,date.getCurrentDate(),userid,instid});
			if (res != 0) {
				txManager.commit(transact.status);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "User [" + username + " ] Deleted " + authmsg);
				trace("Selected User  " + username + " Deleted ");

				/************* AUDIT BLOCK **************/
				try {
					
					//added by gowtham_220719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);

					auditbean.setUsercode(loginusercode);
					auditbean.setRemarks(remarks);
					auditbean.setAuditactcode("3001");
					commondesc.insertAuditTrail(instid, loginusercode, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				txManager.rollback(transact.status);
				addActionError("User [ " + username + " ]  could not delete...");
				trace("Selected User  " + username + " not  Deleted ");
			}
		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError("Error While Delete the User " + username);
			trace("Exception...." + e.getMessage());
			e.printStackTrace();
		}
		return "deluserdet";
	}

	public String editUserDet() {
		trace("*************** ENTER INTO THE editUserDet function of usermanagment **********");
		enctrace("*************** ENTER INTO THE editUserDet function of usermanagment **********");
		// System.out.println("ENTER INTO THE editUserDet function of
		// usermanagment ");
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");
		// String PROFILEID = (String)session.getAttribute("PROFILEID");
		// String PROFILE_ID = PROFILEID.toUpperCase();

		String usertype = (String) session.getAttribute("USERTYPE");

		if (usertype.equals("SUPERADMIN")) {

			try {
				List institutes = instdao.getListOfInstitutionFromProduction(jdbctemplate);
				trace("institutes List is:" + institutes.size());
				if (!institutes.isEmpty()) {
					setInstitutionlist(institutes);
					session.setAttribute("Error_status", "S");
				} else {
					session.setAttribute("usereditstatus", "No Institution Found");
					session.setAttribute("Error_status", "E");
				}
			} catch (Exception e) {
				session.setAttribute("usereditstatus", "Error While Fetching the Instituion");
				session.setAttribute("Error_status", "E");
			}
			return "EditAdminUserDet";
		} else {
			String cuserid = (String) session.getAttribute("USERID");
			trace("cuserid is:" + cuserid);
			// System.out.println("cuseridcuserid====>"+cuserid);
			instname = (String) session.getAttribute("Instname");
			String inst_qury = "select count(*) from USER_DETAILS where instid='" + instname + "'  and USERID !='"
					+ cuserid + "' and usertype !='S'";
			enctrace("inst_qury is:" + inst_qury);
			System.out.println("inst_qury is:" + inst_qury);
			int edit_query = jdbctemplate.queryForInt(inst_qury);
			
			/*String inst_qury = "select count(*) from USER_DETAILS where instid=?  and USERID !=?and usertype !=?";
			enctrace("inst_qury is:" + inst_qury);
			int edit_query = jdbctemplate.queryForInt(inst_qury,new Object[]{instname,cuserid,"S"});*/
			if (edit_query == 0) {
				trace("Count From Edit User is Zero" + edit_query);
				setEdit_user('Y');
			} else {
				setEdit_user('N');
				trace("Count From Edit User is not Zero" + edit_query);
				// System.out.println("Count From Edit User is not
				// Zero"+edit_query);
			}

			String instqury = "select * from USER_DETAILS where instid='" + instname
					+ "' and ( USERSTATUS != '2' and USERSTATUS != '2M'  )  and USERID !='" + cuserid
					+ "' and usertype !='S'";
			 enctrace("instqury is:" + instqury);
			// System.out.println("User instqury:"+instqury);
			
			
	/*String instqury = "select * from USER_DETAILS where instid='" + instname+ "' and ( USERSTATUS != '2' and USERSTATUS != '2M'  )  and USERID !='" + cuserid+ "' and usertype !='S'";*/
			
			/*//by gowtham-140819
			String instqury = "select * from USER_DETAILS where instid='? and ( USERSTATUS != ? and USERSTATUS != ?  )  and USERID !=? and usertype !=? ";
			enctrace("instqury is:" + instqury);*/
			// System.out.println("User instqury:"+instqury);
			try {
				
				List result = jdbctemplate.queryForList(instqury);
				
				//by gowtham-140819
				//List result = jdbctemplate.queryForList(instqury,new Object[]{instname,"2","2M",cuserid,"S"});
				trace("result list is:" + result.size());
				// System.out.println("User NAme IS:"+result);
				if (!(result.isEmpty())) {
					setInstusername(result);
					session.setAttribute("EdituserDetailsError", "S");
				} else {
					session.setAttribute("EdituserDetailsError", "E");
					session.setAttribute("EdituserDetailsMessage", " No User Details Found ");
					trace("No User Details Found ");
				}
			} catch (Exception e) {
				session.setAttribute("EdituserDetailsError", "E");
				session.setAttribute("EdituserDetailsMessage", " Error While Fetching The User Details ");
				trace(" Error While Fetching The User Details " + e.getMessage());
			}
			trace("\n");
			enctrace("\n");
			return "editinstuser";
		}
	}

	private char edit_user;

	public char getEdit_user() {
		return edit_user;
	}

	public void setEdit_user(char edit_user) {
		this.edit_user = edit_user;
	}

	public String institutionid;

	public String getInstitutionid() {
		return institutionid;
	}

	public void setInstitutionid(String institutionid) {
		this.institutionid = institutionid;
	}

	public String getEditinstuser() {
		trace("*************** getEditinstuser Begins **********");
		enctrace("*************** getEditinstuser Begins **********");
		HttpSession session = getRequest().getSession();
		String instname = (getRequest().getParameter("instname"));
		setInstitutionid(instname);

		/*String qury = "Select count(*) from USER_DETAILS where INSTID = '" + instname
				+ "' and CREATEDUSERID='SU' and USERSTATUS !='2'";
		enctrace("qury is:" + qury);
		int count = jdbctemplate.queryForInt(qury);*/
		
		//by gowtham-140819
				String qury = "Select count(*) from USER_DETAILS where INSTID = ? and CREATEDUSERID=?and USERSTATUS !=?";
				enctrace("qury is:" + qury);
				int count = jdbctemplate.queryForInt(qury,new Object[]{instname,"SU","2"});
		if (count == 0) {
			trace("Count From Edit User is Zero" + count);
			setEdit_user('Y');
		} else {
			setEdit_user('N');
			trace("Count From Edit User is not Zero" + count);
		}

		try {
			/*String instqury = "Select USERID,USERNAME from USER_DETAILS where INSTID = '" + instname
					+ "' and CREATEDUSERID='SU' and USERSTATUS !='2'";
			enctrace("instqury" + instqury);
			System.out.println("instqury--->  " + instqury);
			List userList = jdbctemplate.queryForList(instqury);*/
			
			

			//by gowtham-140819
			String instqury = "Select USERID,USERNAME from USER_DETAILS where INSTID = ? and CREATEDUSERID=? and USERSTATUS !=?";
			enctrace("instqury" + instqury);
			System.out.println("instqury--->  " + instqury);
			List userList = jdbctemplate.queryForList(instqury,new Object[]{instname,"SU","2"});
			
			
			trace("User NAme IS:" + userList.size());
			if (!(userList.isEmpty())) {
				setInstusername(userList);
				session.setAttribute("Errorstatus", "S");
				trace("List is Not Empty");
			} else {
				session.setAttribute("useredit_status", "No User Found");
				session.setAttribute("Errorstatus", "E");
				trace("List is  Empty");
			}
		} catch (Exception e) {
			trace("Exception is " + e);
			session.setAttribute("useredit_status", "Error While Fetching the List of Users");
			session.setAttribute("Errorstatus", "E");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "editinstuser";
	}

	private List instusername;

	public List getInstusername() {
		return instusername;
	}

	public void setInstusername(List instusername) {
		this.instusername = instusername;
	}

	public String getUsereditdetails() {

		trace("*************** getUsereditdetails Begins **********");
		enctrace("*************** getUsereditdetails Begins **********");
		HttpSession session = getRequest().getSession();
		String username = (getRequest().getParameter("username"));
		System.out.println();
		String ss_username = (String) session.getAttribute("SS_USERNAME");
		String institution_id;
		String usertype = (String) session.getAttribute("USERTYPE");
		if (usertype.equals("SUPERADMIN")) {
			institution_id = getRequest().getParameter("institution_id");
			System.out.println("institution_id for superadmin---> " + institution_id + "user type" + usertype);
			setInstitutionid(institution_id);

		} else {
			institution_id = (String) session.getAttribute("Instname");
			System.out.println("institution_id---> " + institution_id);
			setInstitutionid(institution_id);
		}
		session.setAttribute("USERTYPE", usertype);
		/*String userqury = "Select usr.USERID,usr.USERNAME,usr.PROFILEID,usr.FIRSTNAME,usr.LASTNAME,usr.EMAILID,usr.USERSTATUS,prof.PROFILE_ID,prof.PROFILE_NAME,prof.LOGINIPADDRESSREQUIRED,usr.IPADDRESS,prof.LOGINEXPIRYDATEREQUIRED,usr.EXPIRYDATE,prof.LOGINRETRYCOUNTREQUIRED,usr.RETRYCOUNT,prof.LOGINBRANCHCODEREQUIRED,usr.BRANCHCODE,prof.USERPASSWORDREPEATABLE,usr.PSWREPEATCOUNT,prof.USERPASSWORDEXPIRYCHECK,usr.PASSWORDEXPIRYDATE from USER_DETAILS  usr,"
				+ getProfilelistTemp() + "  prof where usr.USERID = '" + username
				+ "' and usr.PROFILEID=prof.PROFILE_ID  and prof.INSTID='" + institution_id
				+ "' and usr.INSTID=prof.INSTID";
		enctrace("userqury ==>" + userqury);
		try {
			List userDetails_list = jdbctemplate.queryForList(userqury);*/
		
		String userqury = "Select usr.USERID,usr.USERNAME,usr.PROFILEID,usr.FIRSTNAME,usr.LASTNAME,usr.EMAILID,usr.USERSTATUS,prof.PROFILE_ID,prof.PROFILE_NAME,prof.LOGINIPADDRESSREQUIRED,usr.IPADDRESS,prof.LOGINEXPIRYDATEREQUIRED,usr.EXPIRYDATE,prof.LOGINRETRYCOUNTREQUIRED,usr.RETRYCOUNT,prof.LOGINBRANCHCODEREQUIRED,usr.BRANCHCODE,prof.USERPASSWORDREPEATABLE,usr.PSWREPEATCOUNT,prof.USERPASSWORDEXPIRYCHECK,usr.PASSWORDEXPIRYDATE from USER_DETAILS  usr,"
				+ getProfilelistTemp() + "  prof where usr.USERID =? and  usr.PROFILEID=prof.PROFILE_ID and prof.INSTID=? and usr.INSTID=prof.INSTID";
		enctrace("userqury ==>" + userqury);
		try {
			List userDetails_list = jdbctemplate.queryForList(userqury,new Object[]{username,institution_id});
			trace("User NAme IS:" + userqury);
			trace("userDetails_list is:" + userDetails_list.size());
			if (!(userDetails_list.isEmpty())) {
				setEdituserdetail(userDetails_list);
				session.setAttribute("Error_status", "S");
				trace("List is Not Empty");
			} else {
				session.setAttribute("usereditstatus", "No User Details Found");
				session.setAttribute("Error_status", "E");
				trace("List is  Empty");
				trace("\n\n");
				enctrace("\n\n");
				return "EditAdminUserDet";
			}
			List result = null;
			// profile AND USER_CODE != 'SU'
			/*String qury = "SELECT * FROM " + getProfilelistMain() + " WHERE INSTID='" + institution_id + "'";
			enctrace("qury is:" + qury);
			result = jdbctemplate.queryForList(qury);*/
			
			//by gowtham-140819
			String qury = "SELECT * FROM " + getProfilelistMain() + " WHERE INSTID=?";
			enctrace("qury is:" + qury);
			result = jdbctemplate.queryForList(qury,new Object[]{institution_id});
			trace("result list is:" + result.size());
			// System.out.println(result);
			setProfilename(result);
			// branch list
			List brnchlist = null;
			/*String brnchqury = "SELECT BRANCH_CODE,BRANCH_NAME FROM BRANCH_MASTER WHERE INST_ID='" + institution_id
					+ "' ";
			enctrace("qury forb:" + qury);
			brnchlist = jdbctemplate.queryForList(brnchqury);*/
			
			//by gowtham-140819
			String brnchqury = "SELECT BRANCH_CODE,BRANCH_NAME FROM BRANCH_MASTER WHERE INST_ID=? ";
			enctrace("qury forb:" + qury);
			brnchlist = jdbctemplate.queryForList(brnchqury,new Object[]{institution_id});
			trace("result list is:" + brnchlist.size());
			// System.out.println(result);
			setBranchlist(brnchlist);

		} catch (Exception e) {
			trace("QUery Have Exception " + e);
			session.setAttribute("usereditstatus", "Error While Fetching the List of Users");
			session.setAttribute("Error_status", "E");
			trace("\n\n");
			enctrace("\n\n");
			return "EditAdminUserDet";
		}
		return "getusereditdetails";
	}

	private List edituserdetail;

	public List getEdituserdetail() {
		return edituserdetail;
	}

	public void setEdituserdetail(List edituserdetail) {
		this.edituserdetail = edituserdetail;
	}

	private List updateduser_detail;

	public List getUpdateduser_detail() {
		return updateduser_detail;
	}

	public void setUpdateduser_detail(List updateduser_detail) {
		this.updateduser_detail = updateduser_detail;
	}

	public String editUserdetailsupdate() {
		trace("*************** editUserdetailsupdate Begins **********");
		enctrace("*************** editUserdetailsupdate Begins **********");
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");

		String userid = (getRequest().getParameter("userid"));
		String username = (getRequest().getParameter("username"));
		String branchcode = (getRequest().getParameter("branch"));
		String firstname = (getRequest().getParameter("firstname"));
		String lastname = (getRequest().getParameter("lastname"));
		String email = (getRequest().getParameter("email"));
		String userstatus = (getRequest().getParameter("userstatus"));
		String instid = getRequest().getParameter("instid");
		String profile = getRequest().getParameter("profile");
		System.out.println("instid--- > " + instid);
		String externaluser = (String) session.getAttribute("EXTERNALUSER");
		IfpTransObj transact = commondesc.myTranObject("EDITUSER", txManager);
		String passwordexpiryflag = "0";
		
	

bean.setFname(firstname);
bean.setLname(lastname);
bean.setEmail(email);

System.out.println("1111--->"+firstname);
System.out.println("2222--->"+lastname);
System.out.println("3333--->"+email);

check=Validation.charcter(firstname);
System.out.println("1111--->"+check);
if(!check)
{
	addActionError("Enter Fname, Lname, Email With Proper Format");
	return editUserDet();
}

check=Validation.charcter(lastname);
System.out.println("2222--->"+check);
if(!check)
{
	addActionError("Enter Fname, Lname, Email With Proper Format");
	return editUserDet();
}
check=Validation.email(email);
System.out.println("3333--->"+check);
if(!check)
{
	addActionError("Enter Fname, Lname, Email With Proper Format");
	return editUserDet();
}

		
		int updated_status = 0;
		if ((branchcode == null)) {
			branchcode = "000";
		}
		try {
			String loginadrs = null, loginexpry = null, loginretrycount = null, userpasswrdrepeat = null,
					userpasswrdexiry = null;
			/*
			 * String query_userdtails = "update USER_DETAILS set USERNAME = '"
			 * +username+"',FIRSTNAME='"+firstname+"',LASTNAME='"+lastname+
			 * "',EMAILID='"+email+"',USERSTATUS='"+userstatus+"'";
			 * System.out.println("query_userdtails==> "+query_userdtails);
			 * String condtn = "where USERID='"+userid+"'"; String userqury=
			 * "Select usr.USERID,usr.USERNAME,usr.PROFILEID,usr.FIRSTNAME,usr.LASTNAME,usr.EMAILID,decode(usr.USERSTATUS,'1','Active','0','In-Active') as USERSTATUS,prof.PROFILE_ID,prof.PROFILE_NAME from USER_DETAILS  usr,"
			 * +getProfilelistTemp()+"  prof where usr.USERID = '"+userid+
			 * "' and usr.PROFILEID=prof.PROFILE_ID";
			 */

			// String userqury="Select
			// usr.USERID,usr.USERNAME,usr.PROFILEID,usr.FIRSTNAME,usr.LASTNAME,usr.EMAILID,usr.USERSTATUS,prof.PROFILE_ID,prof.PROFILE_NAME,prof.LOGINIPADDRESSREQUIRED,usr.IPADDRESS,prof.LOGINEXPIRYDATEREQUIRED,usr.EXPIRYDATE,prof.LOGINRETRYCOUNTREQUIRED,usr.RETRYCOUNT,prof.LOGINBRANCHCODEREQUIRED,usr.BRANCHCODE,prof.USERPASSWORDREPEATABLE,usr.PSWREPEATCOUNT,prof.USERPASSWORDEXPIRYCHECK,usr.PASSWORDEXPIRYDATE
			// from USER_DETAILS usr,"+getProfilelistTemp()+" prof where
			// usr.USERID = '"+userid+"' and usr.PROFILEID=prof.PROFILE_ID ";
			/*String userqury = "select * from USER_DETAILS where INSTID='" + instid + "' and USERID='" + userid + "'";
			enctrace("userqury==> " + userqury);
			List userqury_list = jdbctemplate.queryForList(userqury);*/
			
			//by gowtham-140819
			String userqury = "select * from USER_DETAILS where INSTID=? and USERID=?";
			enctrace("userqury==> " + userqury);
			List userqury_list = jdbctemplate.queryForList(userqury,new Object[]{instid,userid});
			setUpdateduser_detail(userqury_list);
			trace("userqury_list is:" + userqury_list.size());
			Iterator itr = userqury_list.iterator();
			while (itr.hasNext()) {
				Map temp = (Map) itr.next();
				trace(" --Entered itereator profileid-- ");
				Object PROFILEID_obj = (temp.get("PROFILEID"));
				String PROFILEID_str = PROFILEID_obj.toString();
				int profid = Integer.parseInt(PROFILEID_str);
				trace(" --profileid-- " + profid);
				List getloginadrs = commondesc.getlogindetails(profid, instid, jdbctemplate);
				Iterator itrprofile = getloginadrs.iterator();
				while (itrprofile.hasNext()) {
					trace(" --Entered itereator second-- ");
					Map tempprofile = (Map) itrprofile.next();
					loginadrs = (String) tempprofile.get("LOGINIPADDRESSREQUIRED");
					trace("loginadrs==> " + loginadrs);
					loginexpry = (String) tempprofile.get("LOGINEXPIRYDATEREQUIRED");
					trace("loginexpry==> " + loginexpry);
					loginretrycount = (String) tempprofile.get("LOGINRETRYCOUNTREQUIRED");
					trace("loginretrycount==> " + loginretrycount);
					userpasswrdrepeat = (String) tempprofile.get("USERPASSWORDREPEATABLE");
					trace("userpasswrdrepeat==> " + userpasswrdrepeat);
					userpasswrdexiry = (String) tempprofile.get("USERPASSWORDEXPIRYCHECK");
					trace("userpasswrdexiry==> " + userpasswrdexiry);
				}
			}
			String ipard = null, exprydte = null, retycnt = null, pwdrepeatcnt = null, pwdexprycnt = null;
			if (loginadrs.equals("1")) {
				String ipaddress = (getRequest().getParameter("ipadres"));
				trace("ipaddress==> " + ipaddress);
				ipard = ",IPADDRESS='" + ipaddress + "'";
			} else {
				ipard = "";
			}
			if (loginexpry.equals("1")) {
				String expirydate = (getRequest().getParameter("userexpate"));
				trace("expirydate==> " + expirydate);
				String loginexpreq = " TO_DATE('" + expirydate + "', 'DD-MM-YY') ";
				enctrace("loginexpreq" + loginexpreq);
				exprydte = ",EXPIRYDATE=" + loginexpreq + "";
				enctrace("exprydte" + exprydte);
			} else {
				exprydte = "";
			}
			if (loginretrycount.equals("1")) {
				String retrycount = (getRequest().getParameter("loginretrycount"));
				trace("retrycount==> " + retrycount);
				retycnt = ",RETRYCOUNT='" + retrycount + "'";
			} else {
				retycnt = "";
			}
			if (userpasswrdrepeat.equals("1")) {
				String pwdrepeatcount = (getRequest().getParameter("passwrdrepeatcount"));
				trace("pwdrepeatcount==> " + pwdrepeatcount);
				pwdrepeatcnt = ",PSWREPEATCOUNT='" + pwdrepeatcount + "'";
			} else {
				pwdrepeatcnt = "";
			}
			if (userpasswrdexiry.equals("1")) {
				passwordexpiryflag = "1";
				String passwrdexpirydate = (getRequest().getParameter("passwrdexpirydate"));
				trace("passwrdexpirydate==> " + passwrdexpirydate);
				String pswexpchck = " TO_DATE('" + passwrdexpirydate + "', 'DD-MM-YY')  ";
				pwdexprycnt = ",PASSWORDEXPIRYDATE=" + pswexpchck + "";
				enctrace("pwdexprycnt is:" + pwdexprycnt);
			} else {
				pwdexprycnt = "";
			}

			String query_userdtails = "update USER_DETAILS set PROFILEID = '" + profile + "',BRANCHCODE='" + branchcode
			+ "',USERNAME = '" + username + "',FIRSTNAME='" + firstname + "',LASTNAME='" + lastname
			+ "',EMAILID='" + email + "',USERSTATUS='" + userstatus + "',AUTH_STATUS='0',ADDED_BY='"
			+ externaluser + "', PASSWORDEXPIRYFLAG='" + passwordexpiryflag + "'";
			enctrace("query_userdtails update is:" + query_userdtails);
			String cond = " where USERID='" + userid + "' and INSTID='" + instid + "'";
			enctrace("cond is:" + cond);
			String query_userdetail = query_userdtails + ipard + exprydte + retycnt + pwdrepeatcnt + pwdexprycnt + cond;
			enctrace("query_userdetail==> " + query_userdetail);
			int update_result = jdbctemplate.update(query_userdetail);
			trace("update_result==> " + update_result);
			if (update_result == 1) {
				transact.txManager.commit(transact.status);
				trace("Commited");
				// session.setAttribute("userEditdetailsMessage", " User Details
				// Updated Successfully ");
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "User Details Updated Successfully...Waiting for authorization");

				/************* AUDIT BLOCK **************/
				try {
					
					//added by gowtham_220719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);
					
					auditbean.setActmsg(
							"User [ " + username + " ] Details Updated Successfully...Waiting for authorization");
					auditbean.setUsercode(externaluser);
					auditbean.setAuditactcode("3001");
					commondesc.insertAuditTrail(instid, externaluser, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				transact.txManager.rollback(transact.status);
				trace("Roll Backed inside try");
				addActionError(" Exception while udpate the status from audit");
			}

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			trace("Roll Backed");
			addActionError("Exception while udpate the status" + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "editInstuserSucess";
	}

	public String viewProfile() {
		// loginbean inst = new loginbean();
		trace("*************** viewProfile Begins **********");
		enctrace("*************** viewProfile Begins **********");
		HttpSession session = getRequest().getSession();
		String retunrto = null;
		String username = (String) session.getAttribute("SS_USERNAME");
		String userid = (String) session.getAttribute("USERID");
		String usertype = (String) session.getAttribute("USERTYPE");
		trace("User type is : " + usertype);
		// doact = getRequest().getParameter("doact");
		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("act", act);
		}
		// trace("Do Action : " + doact );
		// if( doact == null ){
		// addActionError("Unable to understand the actiion");
		// return "required_home";
		// }
		if (usertype.equals("SUPERADMIN")) {

			List institutes = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			trace("institutes List is:" + institutes.size());
			if (institutes.isEmpty()) {
				addActionError(" No Institution ID ");
			} else {
				setInstitutionlist(institutes);
			}
			issuperadmin = true;

		} else {

			instname = (String) session.getAttribute("Instname");
			/*String countqury = "SELECT count(*) FROM " + getProfilelistTemp() + " WHERE INSTID='" + instname
					+ "' AND ADDED_BY ='" + userid + "' AND USER_CODE='" + userid + "'";
			enctrace("countqury is:" + countqury);
			int countinstqury = jdbctemplate.queryForInt(countqury);*/
			
			//by gowtham-140819
			String countqury = "SELECT count(*) FROM " + getProfilelistTemp() + " WHERE INSTID=? AND ADDED_BY =? AND USER_CODE=?";
			enctrace("countqury is:" + countqury);
			int countinstqury = jdbctemplate.queryForInt(countqury,new Object[]{instname,userid,userid});
			
			if (countinstqury == 0) {
				trace("count from Profile list" + countinstqury);
				// System.out.println("count from Profile list"+countinstqury);
				setView_profile('Y');
			} else {
				setView_profile('N');
				trace("count from Profile list" + countinstqury);
			}

			String filtercond = "";
			if (doact.equals("$VIEW")) {
				filtercond = " AND DELETED_FLAG !='2'";
			} else if (doact.equals("$DEL")) {
				filtercond = " AND DELETED_FLAG !='2'";
			} else if (doact.equals("$DELAUTH")) {
				filtercond = "   AND AUTH_CODE='0' AND   DELETED_FLAG ='2'";
			} else if (doact.equals("$AUTH")) {
				filtercond = "  AND ADDED_BY !='" + userid + "' AND AUTH_CODE='0'  AND DELETED_FLAG !='2'";
			}

			String externalusername = (String) session.getAttribute("EXTERNALUSER");
			String instqury = "SELECT * FROM " + getProfilelistTemp() + " WHERE INSTID='" + instname
					+ "'  AND USER_CODE !='SU'" + filtercond;
			enctrace("instqury is:" + instqury);
			List result = jdbctemplate.queryForList(instqury);
			trace("result List is:" + result.size());
			setProfiledetail(result);
			issuperadmin = false;
			setInstname(instname);
			// return "getviewprofile";
		}

		if (doact.equals("$VIEW")) {
			trace("returning viewprofile_view");
			return "viewprofile_view";
		} else if (doact.equals("$DEL")) {
			trace("returning viewprofile_del");
			return "viewprofile_del";
		} else if (doact.equals("$DELAUTH")) {
			trace("returning viewprofile_delauth");
			return "viewprofile_delauth";
		}

		return "viewprofile";

	}

	public String getViewProfile() {
		trace("*************** View profile for authorization **********");
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");

		String act = getRequest().getParameter("act");
		String usertype = (String) session.getAttribute("USERTYPE");
		if (act != null) {
			session.setAttribute("act", act);
		}
		doact = getRequest().getParameter("doact");
		trace("Do Action : " + doact);
		if (doact == null) {
			addActionError("Unable to understand the action");
			return "required_home";
		}

		if (usertype.equals("SUPERADMIN")) {
			String profileid = (getRequest().getParameter("subproduct")).trim();
			instname = (getRequest().getParameter("instname")).trim();
			setInstname(instname);
			trace("Subproduct" + profileid);
			// userbean editprofiledetail = new userbean();
			List result;
			String branch_req = null;

			result = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			trace("reulst list is:" + result.size());
			setAdminproflist(result);
			Iterator brattch_itr = result.iterator();
			while (brattch_itr.hasNext()) {
				Map map = (Map) brattch_itr.next();
				branch_req = (map.get("BRANCHATTCHED")).toString();
			}
			brattched = branch_req.charAt(0);
			trace("brattched   ---> " + brattched);

			// String instqury="Select
			// PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID from
			// "+getProfilelistTemp()+" where PROFILE_ID ='"+profileid+"'";
			// String instqury="Select * from "+getProfilelistTemp()+" where
			// PROFILE_ID ='"+profileid+"' and INSTID='"+instname+"'";
			/*String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			instqury += " from " + getProfilelistTemp() + "  where PROFILE_ID ='" + profileid + "' and INSTID='"
					+ instname + "'";

			enctrace("superadmin auth list qry :" + instqury);
			List result1 = jdbctemplate.queryForList(instqury);*/
			
			
			//by gowtham-140819
			String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			instqury += " from " + getProfilelistTemp() + "  where PROFILE_ID =?and INSTID=?";
			enctrace("superadmin auth list qry :" + instqury);
			List result1 = jdbctemplate.queryForList(instqury,new Object[]{profileid,instname});
			trace("result1 list is:" + result1);
			setEditprofiledetail(result1);
			trace("detail result" + instqury);

			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID ='"
					+ profileid + "' and INST_ID='" + instname + "'";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query, String.class);
			

			/*//by gowtham-140819
			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID =? and INST_ID=?";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query,new Object[]{profileid,instname}, String.class);
			*/
			trace("PROFILE PREVILAGE=========> " + profile_result);
			session.setAttribute("EDIT_PROFILEPREV", profile_result);

			// To retrive values for the checkboxes
			String loginipadrs = null;
			String loginexpiry = null;
			// String loginentrycount = null;
			String loginbranchcode = null;
			String userpassrepeat = null;
			String userpasswordexp = null;

			// String editcheckbox ="select * from "+getProfilelistTemp()+"
			// where INSTID='"+instname+"' and PROFILE_ID='"+profileid+"'";
			/*String editcheckbox = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			editcheckbox += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			editcheckbox += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			editcheckbox += " from " + getProfilelistTemp() + " where PROFILE_ID ='" + profileid + "' and INSTID='"
					+ instname + "'";

			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox);*/
			
			//by gowtham-140819
			String editcheckbox = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			editcheckbox += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			editcheckbox += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			editcheckbox += " from " + getProfilelistTemp() + " where PROFILE_ID =?and INSTID=?";
			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox,new Object[]{profileid,instname});
			trace("resulteditcheckbox===> " + resulteditcheckbox.size());
			if (!resulteditcheckbox.isEmpty()) {
				trace("Values are not empty in Database");
				ListIterator itr = resulteditcheckbox.listIterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					String addedby = (String) map.get("ADDED_BY");
					System.out.println(" == addedby == " + addedby);
					if (addedby == null) {
						addedby = "--";
					}

					String authby = (String) map.get("AUTH_BY");
					String authdate = (String) map.get("AUTH_DATE");

					System.out.println(" == authdate == " + authdate);
					System.out.println(" == authby == " + authby);

					if (authby == null) {
						authby = "--";
					}
					if (authdate == null) {
						authdate = "--";
					}

					System.out.println(" == authdate == " + authdate);
					loginipadrs = map.get("LOGINIPADDRESSREQUIRED").toString();
					loginexpiry = map.get("LOGINEXPIRYDATEREQUIRED").toString();
					// loginentrycount =
					// map.get("LOGINRETRYCOUNTREQUIRED").toString();
					loginbranchcode = map.get("LOGINBRANCHCODEREQUIRED").toString();
					userpassrepeat = map.get("USERPASSWORDREPEATABLE").toString();
					userpasswordexp = map.get("USERPASSWORDEXPIRYCHECK").toString();
					map.put("ADDED_BY", addedby);
					map.put("AUTH_BY", authby);
					map.put("AUTH_DATE", authdate);
					itr.remove();
					itr.add(map);
				}
				if (loginipadrs.equals("1")) {
					trace("Login IP is ONE");
					setLoginipadd('Y');
				} else {
					setLoginipadd('N');
					trace("Login Ip is ZERO");
				}
				if (loginexpiry.equals("1")) {
					trace("Login EXPIRY is ZERO");
					setLoginexpry('Y');
				} else {
					setLoginexpry('N');
					trace("Login EXPIRY is ONE");
				}

				if (loginbranchcode.equals("1")) {
					trace("Login Branch is ZERO");
					setLoginbrnch('Y');
				} else {
					setLoginbrnch('N');
					trace("Login Branch is ONE");
				}

				if (userpassrepeat.equals("1")) {
					trace("Password Repeatable is ZERO");
					setUserpassreapat('Y');
				} else {
					setUserpassreapat('N');
					trace("Password Repeatableis ONE");
				}

				if (userpasswordexp.equals("1")) {
					trace("User Password expiry is ZERO");
					setUserpassexp('Y');
				} else {
					setUserpassexp('N');
					trace("User Password expiry is ONE");
				}

				/*
				 * trace("loginipadrs==> "+loginipadrs); trace("loginexpiry==> "
				 * +loginexpiry); trace("loginbranchcode==> "+loginbranchcode);
				 * trace("userpassrepeat==> "+userpassrepeat); trace(
				 * "userpasswordexp==> "+userpasswordexp);
				 */
			}
			trace("resulteditcheckbox===> " + resulteditcheckbox);

		} else {
			String profileid = (getRequest().getParameter("subproduct")).trim();
			instname = (getRequest().getParameter("instname")).trim();
			setInstname(instname);
			trace("instname=====>"+instname);
			trace("Subproduct" + profileid);
			String loginipadrs = null;
			String loginexpiry = null;
			// String loginentrycount = null;
			String loginbranchcode = null;
			String userpassrepeat = null;
			String userpasswordexp = null;
			// userbean editprofiledetail = new userbean();
			List result;
			String branch_req = null;

			result = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			trace("result list is:" + result.size());
			setAdminproflist(result);
			Iterator brattch_itr = result.iterator();
			while (brattch_itr.hasNext()) {
				Map map = (Map) brattch_itr.next();
				branch_req = (map.get("BRANCHATTCHED")).toString();
			}
			brattched = branch_req.charAt(0);
			trace("brattched   ---> " + brattched);
			// String instqury="Select * from "+getProfilelistTemp()+" where
			// PROFILE_ID ='"+profileid+"' and INSTID='"+instname+"' AND
			// AUTH_CODE='0'";
			
			
			String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += "NVL( TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY, NVL(ADDED_BY,'--') AS ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			instqury += " from " + getProfilelistTemp() + "  where PROFILE_ID ='" + profileid + "' and INSTID='"+ instname + "'";

			enctrace("instqury is:" + instqury);
			
			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID ='"
					+ profileid + "' and INST_ID='" + instname + "'";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query, String.class);
			
			trace("PROFILE PREVILAGE=========> " + profile_result);
			session.setAttribute("EDIT_PROFILEPREV", profile_result);
			List result1 = jdbctemplate.queryForList(instqury);
			
		/*	//by gowtham-140819
			String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += "NVL( TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY, NVL(ADDED_BY,'--') AS ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			instqury += " from " + getProfilelistTemp() + "  where PROFILE_ID ='" + profileid + "' and INSTID='"+ instname + "'";
			enctrace("instqury is:" + instqury);
			
			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID =? and INST_ID=? ";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query,new Object[]{profileid,instname} ,String.class);
			
			trace("PROFILE PREVILAGE=========> " + profile_result);
			session.setAttribute("EDIT_PROFILEPREV", profile_result);
			List result1 = jdbctemplate.queryForList(instqury,new Object[]{profileid,instname});*/
			
			trace("got the values");
			
			
			trace("result1 list is:" + result1.size());
			ListIterator litr = result1.listIterator();
			while (litr.hasNext()) {
				Map mp = (Map) litr.next();
				String added_user = (String) mp.get("ADDED_BY");
				added_user = commondesc.getUserName(instname, added_user, jdbctemplate);
				if (added_user == null) {
					added_user = "--";
				}
				mp.put("ADDED_BY", added_user);

				String auth_user = (String) mp.get("AUTH_BY");
				auth_user = commondesc.getUserName(instname, auth_user, jdbctemplate);
				if (auth_user == null) {
					auth_user = "--";
				}
				mp.put("AUTH_BY", auth_user);
				String auth_date = (String) mp.get("AUTH_DATE");
				if (auth_date == null) {
					auth_date = "--";
				}
				mp.put("AUTH_DATE", auth_date);
				litr.remove();
				litr.add(mp);
			}

			setEditprofiledetail(result1);
			trace("detail result" + instqury);
			// To retrive values for the checkboxes
			// String editcheckbox ="select * from "+getProfilelistTemp()+"
			// where INSTID='"+instname+"' and PROFILE_ID='"+profileid+"'";
			/*String editcheckbox = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			editcheckbox += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			editcheckbox += " TO_CHAR(AUTH_DATE,'DD-MM-YYYY') AS AUTH_DATE, AUTH_BY, ADDED_BY, TO_CHAR(ADDED_DATE,'DD-MM-YYYY') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			editcheckbox += " from " + getProfilelistTemp() + " where PROFILE_ID ='" + profileid + "' and INSTID='"
					+ instname + "'";

			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox);*/
			
			
			//by gowtha,-140819
			String editcheckbox = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			editcheckbox += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			editcheckbox += " TO_CHAR(AUTH_DATE,'DD-MM-YYYY') AS AUTH_DATE, AUTH_BY, ADDED_BY, TO_CHAR(ADDED_DATE,'DD-MM-YYYY') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			editcheckbox += " from " + getProfilelistTemp() + " where PROFILE_ID =? and INSTID=?";

			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox,new Object[]{profileid,instname});
			trace("resulteditcheckbox===> " + resulteditcheckbox.size());
			if (!resulteditcheckbox.isEmpty()) {
				trace("Values are not empty in Database");
				Iterator itr = resulteditcheckbox.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					loginipadrs = map.get("LOGINIPADDRESSREQUIRED").toString();
					loginexpiry = map.get("LOGINEXPIRYDATEREQUIRED").toString();
					// loginentrycount =
					// map.get("LOGINRETRYCOUNTREQUIRED").toString();
					loginbranchcode = map.get("LOGINBRANCHCODEREQUIRED").toString();
					userpassrepeat = map.get("USERPASSWORDREPEATABLE").toString();
					userpasswordexp = map.get("USERPASSWORDEXPIRYCHECK").toString();
				}
				if (loginipadrs.equals("1")) {
					trace("Login IP is ONE");
					setLoginipadd('Y');
				} else {
					setLoginipadd('N');
					trace("Login IP is ZERO");
				}

				if (loginexpiry.equals("1")) {
					trace("Login EXPIRY is ZERO");
					setLoginexpry('Y');
				} else {
					setLoginexpry('N');
					trace("Login IP is ONE");
				}
				if (loginbranchcode.equals("1")) {
					trace("Login IP is ZERO");
					setLoginbrnch('Y');
				} else {
					setLoginbrnch('N');
					trace("Login IP is ONE");
				}
				if (userpassrepeat.equals("1")) {
					trace("Login IP is ZERO");
					setUserpassreapat('Y');
				} else {
					setUserpassreapat('N');
					trace("Login IP is ONE");
				}
				if (userpasswordexp.equals("1")) {
					trace("Login IP is ZERO");
					setUserpassexp('Y');
				} else {
					setUserpassexp('N');
					trace("Login IP is ONE");
				}

				trace("loginipadrs==> " + loginipadrs);
				trace("loginexpiry==> " + loginexpiry);
				trace("loginbranchcode==> " + loginbranchcode);
				trace("userpassrepeat==> " + userpassrepeat);
				trace("userpasswordexp==> " + userpasswordexp);
			}
		}

		String returnstring = "";
		if (doact.equals("$VIEW")) {
			returnstring = "adminsaveview_view";
		} else if (doact.equals("$DEL")) {
			returnstring = "adminsaveview_del";
		} else if (doact.equals("$DELAUTH")) {
			returnstring = "adminsaveview_delauth";
		} else if (doact.equals("$AUTH")) {
			returnstring = "adminsaveview";
		}
		trace("Returning..." + returnstring);
		return returnstring;

	}

	public String editProfile() {
		// loginbean inst = new loginbean();
		trace("*************** Edit Profile Begins **********");
		enctrace("*************** Edit Profile Begins **********");
		HttpSession session = getRequest().getSession();
		String userid = (String) session.getAttribute("USERID");
		String retunrto = null;
		String username = (String) session.getAttribute("SS_USERNAME");
		String usertype = (String) session.getAttribute("USERTYPE");
		trace("User type is : " + usertype);
		String profileflag = getRequest().getParameter("act");
		if (profileflag != null) {
			session.setAttribute("PROFILE_FLAG", profileflag);
		}
		System.out.println(" -- Session set -- " + profileflag);
		if (usertype.equals("SUPERADMIN")) {

			List institutes = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			trace("institutes" + institutes.size());
			trace("Got institutes List");
			// System.out.println("Selected Instituion"+institutes);
			setInstitutionlist(institutes);
			// retunrto = "addprofile";
			return "AdminEditProfile";
		} else {
			instname = (String) session.getAttribute("Instname");
			trace("instname is:" + instname);
			setInstname(instname);
			/*String qury = "SELECT * FROM " + getProfilelistTemp() + " WHERE INSTID='" + instname
					+ "'  AND USER_CODE!='SU' AND DELETED_FLAG !='2'";
			enctrace("qury.." + qury);

			List result = jdbctemplate.queryForList(qury);
			// trace("result list is:"+result.size());
*/
			
			
			//added by gowtham-130819
			String qury = "SELECT * FROM " + getProfilelistTemp() + " WHERE INSTID=?  AND USER_CODE!=? AND DELETED_FLAG !=?";
			enctrace("qury.." + qury);
			List result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU","2"});
			setInstproflist(result);

			trace("\n\n");
			enctrace("\n\n");
			return "editprofile";

		}

	}

	private List instproflist;

	public List getInstproflist() {
		return instproflist;
	}

	public void setInstproflist(List instproflist) {
		this.instproflist = instproflist;
	}

	private char edit_profile;

	public char getEdit_profile() {
		return edit_profile;
	}

	public void setEdit_profile(char edit_profile) {
		this.edit_profile = edit_profile;
	}

	public String editProfileView() {
		trace("*********editProfileView");
		enctrace("*********editProfileView");
		HttpSession session = getRequest().getSession();
		String instname = (getRequest().getParameter("instname"));
		String doaction = (getRequest().getParameter("doaction"));

		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		List result=null;
		int profileid;
		String profilename;
		String qury="";

		trace("doaction : " + doaction);
		if (doaction == null) {
			addActionError("Unable to understand the action...");
			return "required_home";
		}

		// String qury="select * from "+getProfilelistTemp()+" where
		// INSTID='"+instname+"' and ";
		String externalusername = (String) session.getAttribute("EXTERNALUSER");
		System.out.println(" ==== externalusername === " + externalusername);
		String fileterqry = "";

		/*if (doaction.equals("auth")) {
			fileterqry = " AND ADDED_BY != '" + externalusername + "'  AND AUTH_CODE = '0' ";
		} else if (doaction.equals("$VIEW")) {
			fileterqry = " AND DELETED_FLAG !='2' OR ( DELETED_FLAG ='2' AND AUTH_CODE = '0' ) ";
		} else if (doaction.equals("$DEL")) {
			fileterqry = " AND DELETED_FLAG != '2'  ";
		} else if (doaction.equals("$DELAUTH")) {
			fileterqry = " AND ADDED_BY != '" + externalusername + "' AND AUTH_CODE = '0' AND DELETED_FLAG='2' ";
		} else if (doaction.equals("$AUTH")) {
			fileterqry = " AND ADDED_BY != '" + externalusername + "' AND AUTH_CODE = '0' AND DELETED_FLAG !='2' ";
		} else if (doaction.equals("$EDIT") || doaction.equals("edit")) {
			fileterqry = " AND DELETED_FLAG !='2' ";
		}

		String qury = "select * from " + getProfilelistTemp() + " where INSTID='" + instname + "' and user_code='SU' "
				+ fileterqry;
		enctrace("qury :" + qury);
		result = jdbctemplate.queryForList(qury);*/
		
		
		if (doaction.equals("auth")) {
			trace("1");
			//fileterqry = " AND ADDED_BY != '" + externalusername + "'  AND AUTH_CODE = '0' ";
			qury = "select * from " + getProfilelistTemp() + " where INSTID=?and user_code=? AND ADDED_BY != ?  AND AUTH_CODE =? ";
			result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU",externalusername,"0"});
		} 
		else if (doaction.equals("$VIEW")) {
			
			trace("2");
			//fileterqry = " AND DELETED_FLAG !='2' OR ( DELETED_FLAG ='2' AND AUTH_CODE = '0' ) ";
			 qury = "select * from " + getProfilelistTemp() + " where INSTID=? and user_code=? AND DELETED_FLAG !=? OR ( DELETED_FLAG =? AND AUTH_CODE = ? ) ";
			result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU","2","2","0"});
		} 
		else if (doaction.equals("$DEL")) {
			trace("3");
			//fileterqry = " AND DELETED_FLAG != '2'  ";
			qury = "select * from " + getProfilelistTemp() + " where INSTID=? and user_code=? AND DELETED_FLAG != ?  ";
			result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU","2"});
		} 
		else if (doaction.equals("$DELAUTH")) {
			trace("4");
			//fileterqry = " AND ADDED_BY != '" + externalusername + "' AND AUTH_CODE = '0' AND DELETED_FLAG='2' ";
			qury = "select * from " + getProfilelistTemp() + " where INSTID=? and user_code=? AND ADDED_BY !=? AND AUTH_CODE = ? AND DELETED_FLAG=? ";
			result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU",externalusername,"0","2"});
		} 
		else if (doaction.equals("$AUTH")) {
			trace("5");
			//fileterqry = " AND ADDED_BY != '" + externalusername + "' AND AUTH_CODE = '0' AND DELETED_FLAG !='2' ";
			qury = "select * from " + getProfilelistTemp() + " where INSTID=? and user_code=?AND ADDED_BY != ?AND AUTH_CODE = ? AND DELETED_FLAG !=? ";
			result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU",externalusername,"0","2"});
		} 
		else if (doaction.equals("$EDIT") || doaction.equals("edit")) {
			trace("6");
		    //fileterqry = " AND DELETED_FLAG !='2' ";
			 qury = "select * from " + getProfilelistTemp() + " where INSTID=? and user_code=?  AND DELETED_FLAG !=? ";
			result = jdbctemplate.queryForList(qury,new Object[]{instname,"SU","2"});
		}
		System.out.println("edit profile Result is" + qury);

		Iterator itr = result.iterator();
		String subproduct = null;
		// Str sun=new ObjArray();
		subproduct = "<select name='subproduct' id='profileid' >"; // id='subproduct'
		subproduct = subproduct + "<option value='00'>SELECT</option>";
		while (itr.hasNext()) {
			System.out.println("PROFILE_ID +++++++++++++++ ");
			Map map = (Map) itr.next();
			Object profid_obj = map.get("PROFILE_ID");
			String s_profileid = profid_obj.toString();
			profileid = Integer.parseInt(s_profileid);
			System.out.println("PROFILE_ID +++++++++++++++ " + profileid);
			// profileid=);
			// String profileid = Integer.toString(map.get("PROFILE_ID"));

			profilename = ((String) map.get("PROFILE_NAME"));

			subproduct = subproduct + "<option value=\" " + profileid + "\">" + profilename + "</option>";

		}

		subproduct = subproduct + "</select>";
		try {

			response.getWriter().write(subproduct);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	List editprofiledetail;

	public List getEditprofiledetail() {
		return editprofiledetail;
	}

	public void setEditprofiledetail(List editprofiledetail) {
		this.editprofiledetail = editprofiledetail;
	}

	private char loginipadd;

	public char getLoginipadd() {
		return loginipadd;
	}

	public void setLoginipadd(char loginipadd) {
		this.loginipadd = loginipadd;
	}

	private char loginexpry;

	public char getLoginexpry() {
		return loginexpry;
	}

	public void setLoginexpry(char loginexpry) {
		this.loginexpry = loginexpry;
	}

	private char loginbrnch;

	public char getLoginbrnch() {
		return loginbrnch;
	}

	public void setLoginbrnch(char loginbrnch) {
		this.loginbrnch = loginbrnch;
	}

	private char userpassreapat;

	public char getUserpassreapat() {
		return userpassreapat;
	}

	public void setUserpassreapat(char userpassreapat) {
		this.userpassreapat = userpassreapat;
	}

	private char userpassexp;

	public char getUserpassexp() {
		return userpassexp;
	}

	public void setUserpassexp(char userpassexp) {
		this.userpassexp = userpassexp;
	}

	// To retrive values for the checkboxes and profile name and description
	public String flagprofile;

	public String getFlagprofile() {
		return flagprofile;
	}

	public void setFlagprofile(String flagprofile) {
		this.flagprofile = flagprofile;
	}

	public String saveEdit() {
		trace("saveEdit method Begins **********");
		enctrace("saveEdit method Begins **********");
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");
		System.out.println(" - USENAME - " + username);
		String usertype = (String) session.getAttribute("USERTYPE");
		if (usertype.equals("SUPERADMIN")) {
			String profileid = (getRequest().getParameter("subproduct")).trim();
			instname = (getRequest().getParameter("instname")).trim();
			setInstname(instname);
			trace("Subproduct" + profileid);
			List result;
			String branch_req = null;

			result = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			trace("reulst list is:" + result.size());
			setAdminproflist(result);
			Iterator brattch_itr = result.iterator();
			while (brattch_itr.hasNext()) {
				Map map = (Map) brattch_itr.next();
				branch_req = (map.get("BRANCHATTCHED")).toString();
			}
			brattched = branch_req.charAt(0);
			trace("brattched   ---> " + brattched);

			// String instqury="Select
			// PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID from
			// "+getProfilelistTemp()+" where PROFILE_ID ='"+profileid+"'";
			// String instqury="Select * from "+getProfilelistTemp()+" where
			// PROFILE_ID ='"+profileid+"' and INSTID='"+instname+"'";
		/*	String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') as REMARKS ";
			instqury += " from " + getProfilelistTemp() + " where PROFILE_ID ='" + profileid + "' and INSTID='"
					+ instname + "'";

			enctrace("instqury is:" + instqury);
			List result1 = jdbctemplate.queryForList(instqury);*/
			
			
			//BY GOWTHAM-130819
			String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') as REMARKS ";
			instqury += " from " + getProfilelistTemp() + " where PROFILE_ID =? and INSTID=?";
			enctrace("instqury is:" + instqury);
			List result1 = jdbctemplate.queryForList(instqury,new Object[]{profileid,instname});
			trace("result1 list is:" + result1.size());
			ListIterator litr = result1.listIterator();

			while (litr.hasNext()) {
				Map prmp = (Map) litr.next();
				String addedby = (String) prmp.get("ADDED_BY");
				System.out.println(" == addedby == " + addedby);
				if (addedby == null) {
					addedby = "--";
				}
				prmp.put("ADDED_BY", addedby);
				String authby = (String) prmp.get("AUTH_BY");
				System.out.println(" == authby == " + authby);
				if (authby == null) {
					authby = "--";
				}
				prmp.put("AUTH_BY", authby);
				litr.remove();
				litr.add(prmp);
			}
			setEditprofiledetail(result1);
			
			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID ='"
					+ profileid + "' and INST_ID='" + instname + "'";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query, String.class);
			
			
		/*	//by gowtham_130819
			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID =?and INST_ID=?";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query,new Object[]{profileid,instname}, String.class);
			*/
			
			trace("PROFILE PREVILAGE=========> " + profile_result);
			session.setAttribute("EDIT_PROFILEPREV", profile_result);

			// To retrive values for the checkboxes
			String loginipadrs = null;
			String loginexpiry = null;
			// String loginentrycount = null;
			String loginbranchcode = null;
			String userpassrepeat = null;
			String userpasswordexp = null;

			/*String editcheckbox = "select * from " + getProfilelistTemp() + " where INSTID='" + instname
					+ "' and PROFILE_ID='" + profileid + "'";
			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox);*/
			
			
			//by gowtham-130819
			String editcheckbox = "select * from " + getProfilelistTemp() + " where INSTID=? and PROFILE_ID=?";
			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox,new Object[]{instname,profileid});
			trace("resulteditcheckbox===> " + resulteditcheckbox.size());
			if (!(resulteditcheckbox.isEmpty())) {
				trace("Values are not empty in Database");
				Iterator itr = resulteditcheckbox.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					loginipadrs = map.get("LOGINIPADDRESSREQUIRED").toString();
					loginexpiry = map.get("LOGINEXPIRYDATEREQUIRED").toString();
					// loginentrycount =
					// map.get("LOGINRETRYCOUNTREQUIRED").toString();
					loginbranchcode = map.get("LOGINBRANCHCODEREQUIRED").toString();
					userpassrepeat = map.get("USERPASSWORDREPEATABLE").toString();
					userpasswordexp = map.get("USERPASSWORDEXPIRYCHECK").toString();
				}

				if (loginipadrs.equals("1")) {
					trace("Login IP is ONE");
					setLoginipadd('Y');
				} else {
					setLoginipadd('N');
					trace("Login IP is ZERO");
				}

				if (loginexpiry.equals("1")) {
					trace("Login EXPIRY is ZERO");
					setLoginexpry('Y');
				} else {
					setLoginexpry('N');
					trace("Login IP is ONE");
				}
				if (loginbranchcode.equals("1")) {
					trace("Login IP is ZERO");
					setLoginbrnch('Y');
				} else {
					setLoginbrnch('N');
					trace("Login IP is ONE");
				}

				if (userpassrepeat.equals("1")) {
					trace("Login IP is ZERO");
					setUserpassreapat('Y');
				} else {
					setUserpassreapat('N');
					trace("Login IP is ONE");
				}
				if (userpasswordexp.equals("1")) {
					trace("Login IP is ZERO");
					setUserpassexp('Y');
				} else {
					setUserpassexp('N');
					trace("Login IP is ONE");
				}

				trace("loginipadrs==> " + loginipadrs);
				trace("loginexpiry==> " + loginexpiry);
				trace("loginbranchcode==> " + loginbranchcode);
				trace("userpassrepeat==> " + userpassrepeat);
				trace("userpasswordexp==> " + userpasswordexp);
			}
			return "adminsaveedit";
		} else {
			String profileid = (getRequest().getParameter("subproduct")).trim();
			instname = (getRequest().getParameter("instname")).trim();
			String profileflag = (String) session.getAttribute("PROFILE_FLAG");
			System.out.println(" ----- profileflag ----- " + profileflag);
			trace(" ----- profileflag ----- " + profileflag);
			setInstname(instname);
			trace("Subproduct" + profileid);
			String loginipadrs = null;
			String loginexpiry = null;
			// String loginentrycount = null;
			String loginbranchcode = null;
			String userpassrepeat = null;
			String userpasswordexp = null;

			// userbean editprofiledetail = new userbean();

			List result;
			String branch_req = null;

			result = instdao.getListOfInstitutionFromProduction(jdbctemplate);
			trace("result list is:" + result.size());
			setAdminproflist(result);
			Iterator brattch_itr = result.iterator();
			while (brattch_itr.hasNext()) {
				Map map = (Map) brattch_itr.next();
				branch_req = (map.get("BRANCHATTCHED")).toString();
			}
			brattched = branch_req.charAt(0);
			trace("brattched   ---> " + brattched);

			// String instqury="Select
			// PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID from
			// "+getProfilelistTemp()+" where PROFILE_ID ='"+profileid+"'";
			/*String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			instqury += " from " + getProfilelistTemp() + " where PROFILE_ID ='" + profileid + "' and INSTID='"
					+ instname + "'";
			enctrace("instqury is:" + instqury);
			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID ='"
					+ profileid + "' and INST_ID='" + instname + "'";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query, String.class);
			trace("PROFILE PREVILAGE=========> " + profile_result);
			session.setAttribute("EDIT_PROFILEPREV", profile_result);
			List result1 = jdbctemplate.queryForList(instqury);*/
			
			
			//added by gowtham130819
			String instqury = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			instqury += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			instqury += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE, NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS REMARKS ";
			instqury += " from " + getProfilelistTemp() + " where PROFILE_ID =?and INSTID=?";
			enctrace("instqury is:" + instqury);
			
			String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID ='"
					+ profileid + "' and INST_ID='" + instname + "'";
			enctrace("profilepre_query is:" + profilepre_query);
			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query, String.class);
			
			/*String profilepre_query = "Select MENU_LIST from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID =?and INST_ID=?";
			enctrace("profilepre_query is:" + profilepre_query);
			
			//String profile_result = (String) jdbctemplate.queryForObject(profilepre_query, String.class);

			String profile_result = (String) jdbctemplate.queryForObject(profilepre_query,new Object[]{profileid,instname} ,String.class);
			*/
			trace("PROFILE PREVILAGE=========> " + profile_result);
			session.setAttribute("EDIT_PROFILEPREV", profile_result);
			
			List result1 = jdbctemplate.queryForList(instqury,new Object[]{profileid,instname});
			trace("result1 list is:" + result1.size());
			trace("result1 list is:" + result1.size());
			ListIterator litr = result1.listIterator();

			while (litr.hasNext()) {
				/*
				 * Map map = (Map)litr.next(); String addedby =
				 * (String)map.get("ADDED_BY"); String addedname =
				 * commondesc.getUserName(instname, addedby, jdbctemplate);
				 * trace("addedname :" + addedname); if( addedname == null ){
				 * addedname="--"; } map.put("ADDED_BY", addedname);
				 * 
				 * String authby = (String)map.get("AUTH_BY"); String authname =
				 * commondesc.getUserName(instname, authby, jdbctemplate);
				 * trace("authname :" + authname); if( authname == null ){
				 * authname="--";} map.put("AUTH_BY", authname); litr.remove();
				 * litr.add(map);
				 */
				Map map = (Map) litr.next();
				String addedby = (String) map.get("ADDED_BY");
				addedby = commondesc.getUserName(instname, addedby, jdbctemplate);
				System.out.println(" == addedby == " + addedby);
				if (addedby == null) {
					addedby = "--";
				}
				map.put("ADDED_BY", addedby);
				String authby = (String) map.get("AUTH_BY");
				System.out.println(" == authby == " + authby);
				authby = commondesc.getUserName(instname, authby, jdbctemplate);
				if (authby == null) {
					authby = "--";
				}
				map.put("AUTH_BY", authby);
				litr.remove();
				litr.add(map);
			}
			setEditprofiledetail(result1);
			trace("detail result" + instqury);
			// To retrive values for the checkboxes
			// String ="select * from "+getProfilelistTemp()+" where
			// INSTID='"+instname+"' and PROFILE_ID='"+profileid+"'";
/*
			String editcheckbox = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			editcheckbox += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			editcheckbox += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY, NVL(ADDED_BY,'--') as ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS  REMARKS";
			editcheckbox += " from " + getProfilelistTemp() + " where PROFILE_ID ='" + profileid + "' and INSTID='"
					+ instname + "'";

			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox);*/
			
			
			//by gowtham-130819
			String editcheckbox = "Select PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED, ";
			editcheckbox += " LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE, DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZE','1','AUTHORIZED','9','DE-AUTHORIZED') AS AUTH_CODE, ";
			editcheckbox += " NVL(TO_CHAR(AUTH_DATE,'DD-MM-YYYY'),'--') AS AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY, NVL(ADDED_BY,'--') as ADDED_BY, NVL(TO_CHAR(ADDED_DATE,'DD-MM-YYYY'),'--') AS ADDED_DATE ,NVL(REMARKS,'--') AS  REMARKS";
			editcheckbox += " from " + getProfilelistTemp() + " where PROFILE_ID =? and INSTID=?";
			enctrace("editcheckbox==> " + editcheckbox);
			List resulteditcheckbox = jdbctemplate.queryForList(editcheckbox,new Object[]{profileid,instname});
			trace("resulteditcheckbox===> " + resulteditcheckbox.size());
			if (!(resulteditcheckbox.isEmpty())) {
				trace("Values are not empty in Database");
				Iterator itr = resulteditcheckbox.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					loginipadrs = map.get("LOGINIPADDRESSREQUIRED").toString();
					loginexpiry = map.get("LOGINEXPIRYDATEREQUIRED").toString();
					// loginentrycount =
					// map.get("LOGINRETRYCOUNTREQUIRED").toString();
					loginbranchcode = map.get("LOGINBRANCHCODEREQUIRED").toString();
					userpassrepeat = map.get("USERPASSWORDREPEATABLE").toString();
					userpasswordexp = map.get("USERPASSWORDEXPIRYCHECK").toString();

				}

				if (loginipadrs.equals("1")) {
					trace("Login IP is ONE");
					setLoginipadd('Y');
				} else {
					setLoginipadd('N');
					trace("Login IP is ZERO");
				}

				if (loginexpiry.equals("1")) {
					trace("Login EXPIRY is ZERO");
					setLoginexpry('Y');
				} else {
					setLoginexpry('N');
					trace("Login IP is ONE");
				}
				if (loginbranchcode.equals("1")) {
					trace("Login IP is ZERO");
					setLoginbrnch('Y');
				} else {
					setLoginbrnch('N');
					trace("Login IP is ONE");
				}

				if (userpassrepeat.equals("1")) {
					trace("Login IP is ZERO");
					setUserpassreapat('Y');
				} else {
					setUserpassreapat('N');
					trace("Login IP is ONE");
				}
				if (userpasswordexp.equals("1")) {
					trace("Login IP is ZERO");
					setUserpassexp('Y');
				} else {
					setUserpassexp('N');
					trace("Login IP is ONE");
				}

				trace("loginipadrs==> " + loginipadrs);
				trace("loginexpiry==> " + loginexpiry);
				trace("loginbranchcode==> " + loginbranchcode);
				trace("userpassrepeat==> " + userpassrepeat);
				trace("userpasswordexp==> " + userpasswordexp);
			}

			trace("\n\n");
			enctrace("\n\n");
			return "saveedit";
		}
	}

	private List selectedmenulst;

	public List getSelectedmenulst() {
		return selectedmenulst;
	}

	public void setSelectedmenulst(List selectedmenulst) {
		this.selectedmenulst = selectedmenulst;
	}

	private List selectedinstmenulist;

	public List getSelectedinstmenulist() {
		return selectedinstmenulist;
	}

	public void setSelectedinstmenulist(List selectedinstmenulist) {
		this.selectedinstmenulist = selectedinstmenulist;
	}

	public String saveEditDetails() {
		trace("*************** saveEditDetails Begins **********");
		enctrace("*************** saveEditDetails Begins **********");

		String profileid = (getRequest().getParameter("profileid")).trim();
		String profiledesc = (getRequest().getParameter("profiledesc")).trim();
		String profname = (getRequest().getParameter("profname")).trim();
		String instid = (getRequest().getParameter("instname")).trim();
		trace("The INST NAME IS   %$^%$^$^%$^%$ " + instid);
		setProf_name(profname);
		setInstname(instname);
		setProfile_id(Integer.parseInt(profileid));
		String loginbranchcode = (getRequest().getParameter("branch"));
		trace("loginbranchcode      " + loginbranchcode);
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");

		String usertype = (String) session.getAttribute("USERTYPE");
		trace("User type is : " + usertype);

		if (loginbranchcode != null) {
			if (loginbranchcode.equals("1")) {
				loginbranchcode = "1";
			} else {
				loginbranchcode = "0";
			}
		} else {
			loginbranchcode = "0";
		}

		String loginretrycount = (getRequest().getParameter("retrycount"));
		if (loginretrycount != null) {
			loginretrycount = "1";
		} else {
			loginretrycount = "0";
		}
		String loginipaddress = (getRequest().getParameter("ipaddress"));
		if (loginipaddress != null) {
			loginipaddress = "1";
		} else {
			loginipaddress = "0";
		}

		String userexp_date = (getRequest().getParameter("userexpdate"));
		if (userexp_date != null) {
			userexp_date = "1";
		} else {
			userexp_date = "0";
		}

		String userpswdrpt = (getRequest().getParameter("pswreptable"));
		if (userpswdrpt != null) {
			userpswdrpt = "1";
		} else {
			userpswdrpt = "0";
		}

		String psswd_expdate = (getRequest().getParameter("pswexpdate"));
		if (psswd_expdate != null) {
			psswd_expdate = "1";
		} else {
			psswd_expdate = "0";
		}
		// session.setAttribute("prof_name", prof_name);
		trace("profiledesc  " + profiledesc);
		trace("loginbranchcode		" + loginbranchcode);
		trace("loginipaddress		" + loginipaddress);
		trace("userexp_date		" + userexp_date);
		trace("loginretrycount		" + loginretrycount);
		trace("userpswdrpt		" + userpswdrpt);
		trace("psswd_expdate		" + psswd_expdate);

		session.setAttribute("prof_desc", profiledesc);
		session.setAttribute("loginipaddress", loginipaddress);
		session.setAttribute("usrpswexp", userexp_date);
		session.setAttribute("userpswdrpt", userpswdrpt);
		session.setAttribute("psswd_expdate", psswd_expdate);
		session.setAttribute("loginbranchcode", loginbranchcode);
		session.setAttribute("loginretrycount", loginretrycount);
		session.setAttribute("Instname", instid);
		String profileflag = (String) session.getAttribute("PROFILE_FLAG");
		System.out.println("profileflag-----" + profileflag);
		setFlagprofile(profileflag);

		if (usertype.equals("SUPERADMIN")) {
			String menuid = null;
			String mainmenuid = null;
			List<List> mstrmenuqryrsult = new ArrayList<List>();
			// instname=(getRequest().getParameter("instname"));
			setInstname(instid);
			List result;
			// JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);

			String qury = "select PROFILE_LIST from " + getADMIN_PROFILE_PRIVILEGE() + "";
			result = jdbctemplate.queryForList(qury);
			System.out.println(result);
			Iterator itr = result.iterator();

			Map map;
			while (itr.hasNext()) {

				map = (Map) itr.next();
				proflist = (map.get("PROFILE_LIST")).toString();

			}
			System.out.println(" Profile List  ----->>>>>" + proflist);
			/*String masterqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
					+ " where MENUVISIBILITY ='1' and parentid='0' ORDER BY MENUORDER ASC";
			List mstrqryrsult = jdbctemplate.queryForList(masterqury);*/
			
			//by gowtham-130819
			String masterqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()+ " where MENUVISIBILITY =?and parentid=? ORDER BY MENUORDER ASC";
			List mstrqryrsult = jdbctemplate.queryForList(masterqury,new Object[]{"1","0"});
			List rsult1 = new ArrayList<List>();
			Iterator mstrqryitr = mstrqryrsult.iterator();
			mstrmenuqryrsult.clear();
			while (mstrqryitr.hasNext()) {
				map = (Map) mstrqryitr.next();
				mainmenuid = (map.get("MENUID")).toString();
				menuid = "," + mainmenuid + ",";
				rsult1.clear();
				if (proflist.contains(menuid)) {
					/*String mastermenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
							+ " where MENUVISIBILITY ='1' and MENUID='" + mainmenuid + "' ORDER BY MENUORDER ASC";
					rsult1 = jdbctemplate.queryForList(mastermenuqury);*/
					//by gowtham-130819
					String mastermenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()+ " where MENUVISIBILITY =?and MENUID=? ORDER BY MENUORDER ASC";
					rsult1 = jdbctemplate.queryForList(mastermenuqury,new Object[]{"1",mainmenuid});
				}
				mstrmenuqryrsult.addAll(rsult1);
			}
			System.out.println("MENU LIST" + mstrmenuqryrsult);
			trace("length  " + mstrmenuqryrsult.size());
			setAdminproflist(mstrmenuqryrsult);

			// String selectcheck_query="select * from
			// "+getPROFILE_PRIVILEGE_TEMP()+" where PROFILE_ID='"+profileid+"'
			// ";
			/*String selectcheck_query = "select * from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID='"
					+ profileid + "' AND INST_ID='" + instid + "'";
			enctrace(selectcheck_query);
			List checklist = jdbctemplate.queryForList(selectcheck_query);*/
			//by gowtham-130819
			String selectcheck_query = "select * from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID=? AND INST_ID=?";
			enctrace(selectcheck_query);
			List checklist = jdbctemplate.queryForList(selectcheck_query,new Object[]{profileid,instid});
			if (!(checklist.isEmpty())) {
				Iterator itr1 = checklist.iterator();
				while (itr1.hasNext()) {
					Map temp = (Map) itr1.next();
					String menulist = (String) temp.get("MENU_LIST");
					System.out.println("MENULIST =====>" + menulist);
					String listvalue = menulist.replace("[", "").replace("]", "");
					String[] newmenulist = listvalue.split(",");
					List nwmenlist = new ArrayList();
					System.out.println("splitted rec__" + newmenulist);
					for (int i = 0; i < newmenulist.length; i++) {
						System.out.println("-" + newmenulist[i].trim() + "-");
						int arrpos = 0;
						if (!newmenulist[i].trim().equals("00")) {

							nwmenlist.add(arrpos, newmenulist[i].trim());
							arrpos++;
						}

					}
					trace("New formed menu is " + nwmenlist);
					setSelectedmenulst(nwmenlist);

				}
			}
			trace("\n\n\n");
			enctrace("\n\n\n");
			return "AdminSaveEditDetails";

		} else {
			trace("Welcome to instituion edit Next Page");
			/*String selectinst_query = "select * from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID='" + profileid
					+ "'AND INST_ID='" + instid + "'";
			enctrace("selectinst_query is:" + selectinst_query);
			List checkinstlist = jdbctemplate.queryForList(selectinst_query);*/
			//by gowtham-130819
			String selectinst_query = "select * from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID=?AND INST_ID=?";
			enctrace("selectinst_query is:" + selectinst_query);
			List checkinstlist = jdbctemplate.queryForList(selectinst_query,new Object[]{profileid,instid});
			trace("checkinstlist is:" + checkinstlist.size());
			if (!(checkinstlist.isEmpty())) {
				Iterator itrinst = checkinstlist.iterator();
				while (itrinst.hasNext()) {
					Map temp = (Map) itrinst.next();
					String menulist = (String) temp.get("MENU_LIST");
					trace("MENULIST =====>" + menulist);
					String listvalue = menulist.replace("[", "").replace("]", "");
					String[] newmenulistinst = listvalue.split(",");
					List nwmenlistinst = new ArrayList();
					// trace( "splitted rec__" + newmenulistinst);
					// trace("nwmenlistinst is"+nwmenlistinst.size());
					for (int i = 0; i < newmenulistinst.length; i++) {
						trace("-" + newmenulistinst[i].trim() + "-");
						int arrpos = 0;
						if (!newmenulistinst[i].trim().equals("00")) {

							nwmenlistinst.add(arrpos, newmenulistinst[i].trim());
							arrpos++;
						}

					}
					// trace( "New formed menu is ==> " + nwmenlistinst);
					setSelectedinstmenulist(nwmenlistinst);

				}
			}
			trace("\n\n");
			enctrace("\n\n");
			return "InstAdminSaveEditDetails";
		}
	}

	public String saveMenuEditDetails() {
		trace("*************** Edit Profile Details Begins **********");
		enctrace("*************** Edit Profile Details Begins **********");
		HttpSession session = getRequest().getSession();

		IfpTransObj transact = commondesc.myTranObject("SAVEEDITMENU", txManager);
		String authmsg = "";
		
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		try {

			String profileid = (getRequest().getParameter("prof_id")).trim();
			String instid = getRequest().getParameter("instid").trim();
			trace("instid	" + instid);
			trace("profileid==> " + profileid);

			String profiledesc = (String) session.getAttribute("prof_desc");
			trace("profiledesc	" + profiledesc);
			String loginbranchcode = (String) session.getAttribute("loginbranchcode");
			String loginipaddress = (String) session.getAttribute("loginipaddress");
			String loginretrycount = (String) session.getAttribute("loginretrycount");
			String loginexp_date = (String) session.getAttribute("psswd_expdate");
			trace("loginexp_date	" + loginexp_date);
			String u_pswexp = (String) session.getAttribute("usrpswexp");
			String userpswdrpt = (String) session.getAttribute("userpswdrpt");
			String i_Name = (String) session.getAttribute("Instname");

			ArrayList<String> Allmenu = new ArrayList<String>();
			String mainmenus[] = (getRequest().getParameterValues("mainmenu"));
			trace("Total Mainmenus===> " + mainmenus.length);
			int submenulength = 0;
			if (mainmenus != null) {
				Allmenu.add("00");
				// To find Mainmenu length
				for (int j = 0; j < mainmenus.length; j++) {
					trace("selected main menus " + mainmenus[j]);
					Allmenu.add(mainmenus[j].trim());
					String submenuname = "mainmenu" + mainmenus[j];
					trace("submenuname        " + submenuname);
					String submenus[] = (getRequest().getParameterValues(submenuname));
					trace("Selected Submenus===>" + submenus.length);
					if (submenus != null) {
						submenulength = submenus.length;
					}
					trace("submenulength     " + submenulength);
					// To find submenu length
					String[] subucnt = null;
					for (int s = 0; s < submenulength; s++) {
						subucnt = (getRequest().getParameterValues(submenuname));
						trace("selected submenus " + subucnt[s]);
					}
					if (submenus != null) {
						for (int i = 0; i < submenus.length; i++) {
							Allmenu.add(submenus[i].trim());
							// trace(" Profile priviledge value --->" +Allmenu);
							String mkckflag = submenuname + "Sel" + subucnt[i];
							trace("mkckflag=======>" + mkckflag);
							String mkck_id[] = getRequest().getParameterValues(mkckflag);
							// System.out.println("mkck_id===>"+mkck_id.length);
							for (int k = 0; k < mkck_id.length; k++) {
								trace("Mk Ck Flag====>" + mkck_id[k]);
								if (!(mkck_id[k].equals("-1"))) {
									Allmenu.add(mkck_id[k]);
								}
							}
						}
					}
				}
				Allmenu.add("00");
			} else {
				trace("THE VALUE from the checkbox<<<<<== none");
			}

			String externalusername = (String) session.getAttribute("EXTERNALUSER");
			String dualauth = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
			String authcode = "1";
			if (dualauth.equals("Y")) {
				authcode = "0";
				authmsg = " Waiting for Authorization ";
			}

			/*String query = "update " + getProfilelistTemp() + " set PROFILE_DESC = '" + profiledesc
					+ "',LOGINBRANCHCODEREQUIRED='" + loginbranchcode + "',LOGINIPADDRESSREQUIRED='" + loginipaddress
					+ "',LOGINEXPIRYDATEREQUIRED='" + loginexp_date + "',LOGINRETRYCOUNTREQUIRED='" + loginretrycount
					+ "',USERPASSWORDREPEATABLE='" + userpswdrpt + "',USERPASSWORDEXPIRYCHECK='" + u_pswexp
					+ "', ADDED_BY='" + externalusername + "',AUTH_CODE='" + authcode
					+ "',AUTH_BY='',AUTH_DATE='',REMARKS=''  where PROFILE_ID='" + profileid + "' and INSTID='" + instid
					+ "'";
			trace("update profile" + query);
			int res = jdbctemplate.update(query);

			String menupriv_update = "update " + getPROFILE_PRIVILEGE_TEMP() + " SET  MENU_LIST='" + Allmenu
					+ "' where PROFILE_ID='" + profileid + "' AND INST_ID='" + instid + "'";

			trace("menupriv_update :" + menupriv_update);
			int menupriv = jdbctemplate.update(menupriv_update);*/
			
			
			//by gowtham-130819
			String query = "update " + getProfilelistTemp() +" set PROFILE_DESC =?,LOGINBRANCHCODEREQUIRED=?,"
					+ "LOGINIPADDRESSREQUIRED=?,LOGINEXPIRYDATEREQUIRED=?,LOGINRETRYCOUNTREQUIRED=?,USERPASSWORDREPEATABLE=?,"
					+ "USERPASSWORDEXPIRYCHECK=?,ADDED_BY=?,AUTH_CODE=?,AUTH_BY=?,AUTH_DATE=?,REMARKS=?  "
					+ "where PROFILE_ID=?and INSTID=?";
					trace("update profile" + query);
					
					int res = jdbctemplate.update(query,new Object[]{profiledesc,loginbranchcode,loginipaddress,
					loginexp_date,loginretrycount,userpswdrpt,u_pswexp,externalusername,authcode,"","","",profileid,instid});
			System.out.println(res+"res");
			
			String menupriv_update = "update " + getPROFILE_PRIVILEGE_TEMP() + " SET  MENU_LIST='" + Allmenu
					+ "' where PROFILE_ID='" + profileid + "' AND INST_ID='" + instid + "'";

			trace("menupriv_update :" + menupriv_update);
			int menupriv = jdbctemplate.update(menupriv_update);
			
		/*			
			//by gowtham-140819
			String menupriv_update = "update " + getPROFILE_PRIVILEGE_TEMP() + " SET  MENU_LIST=? where PROFILE_ID=? AND INST_ID=?";
			trace("menupriv_update :" + menupriv_update);*/
			
			trace("ttttttt"+Allmenu);
			trace("tttfdgfgtttt"+profileid);
			trace("ttttsdgdfghfhgfhgfttt"+instid);
		//	int menupriv = jdbctemplate.update(menupriv_update);
			 System.out.println("menupriv "+menupriv);
			// trace("menupriv "+menupriv);
			if (menupriv >= 1 && res >= 1) {
				transact.txManager.commit(transact.status);

				addActionMessage("Profile Edited Successfully. " + authmsg);

				/************* AUDIT BLOCK **************/
				try {
					
					//added by gowtham_220719
				 	trace("ip address======>  "+ip);
				 	auditbean.setIpAdress(ip);

					
					String profilename = commondesc.getProfileDesc(instid, profileid, jdbctemplate);
					auditbean.setActmsg("Profile  [ " + profilename + " ] Edited Successfully. " + authmsg);
					auditbean.setUsercode(externalusername);
					auditbean.setAuditactcode("3001");
					commondesc.insertAuditTrail(instid, externalusername, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				transact.txManager.rollback(transact.status);
				addActionError(" ERROR WHILE EDITING PROFILE ");
				trace("List is  Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception occure while Edit Profile details " + e.getMessage());
			transact.txManager.rollback(transact.status);
			addActionError(" ERROR WHILE EDITING PROFILE ");
		}
		trace("\n\n\n");
		enctrace("\n\n\n");
		return "required_home";
	}

	public String genrateEditInstProfMenu() {
		return "AdminProfileview";
	}

	private List blockuserdetail;

	public List getBlockuserdetail() {
		return blockuserdetail;
	}

	public void setBlockuserdetail(List blockuserdetail) {
		this.blockuserdetail = blockuserdetail;
	}

	private char unblocklist_status;

	public char getUnblocklist_status() {
		return unblocklist_status;
	}

	public void setUnblocklist_status(char unblocklist_status) {
		this.unblocklist_status = unblocklist_status;
	}

	public String unblockuser() {
		trace("*************** unblockuser method Begins **********");
		enctrace("*************** unblockuser method Begins **********");
		// userbean blockuserdetail = new userbean();
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");

		if (usertype.equals("SUPERADMIN")) {
			// String instqury="Select
			// usr.USERID,usr.USERNAME,usr.PROFILEID,usr.INSTID,usr.FIRSTNAME,usr.LASTNAME,usr.EMAILID,decode(usr.USERSTATUS,'1','Active','0','In
			// Activate') as USERSTATUS,prof.PROFILE_ID,prof.PROFILE_NAME from
			// USER_DETAILS usr,"+getProfilelistTemp()+" prof where
			// usr.USERBLOCK = '1' and usr.PROFILEID=prof.PROFILE_ID and
			// usr.createduserid='SU' ";
			/*String instqury = "Select USERID,USERNAME,PROFILEID,INSTID,FIRSTNAME,LASTNAME,EMAILID,decode(USERSTATUS,'1','Active','0','In Active') as USERSTATUS, decode(USERBLOCK, '0','ACTIVE','1','BLOCKED','0M','UNBLOCKED...WAITING FOR AUTHORIZATION',USERBLOCK) AS USERBLOCK, INSTID, UNBLOCKED_BY, TO_CHAR(UNBLOCKED_DATE ,'DD-MM-YYYY') AS UNBLOCKED_DATE  from USER_DETAILS  where USERBLOCK != '0' AND ( USERSTATUS != '2' AND USERSTATUS != '2M' ) and  createduserid='SU' ";
			enctrace("instqury for unblock user is:" + instqury);
			System.out.println(" Blocked Users ====>  " + instqury);
			try {
				List result = jdbctemplate.queryForList(instqury);*/
			
			
			//by gowtham-130819
			String instqury = "Select USERID,USERNAME,PROFILEID,INSTID,FIRSTNAME,LASTNAME,EMAILID,decode(USERSTATUS,'1','Active','0','In Active') as USERSTATUS, decode(USERBLOCK, '0','ACTIVE','1','BLOCKED','0M','UNBLOCKED...WAITING FOR AUTHORIZATION',USERBLOCK) AS USERBLOCK, INSTID, UNBLOCKED_BY, TO_CHAR(UNBLOCKED_DATE ,'DD-MM-YYYY') AS UNBLOCKED_DATE  from USER_DETAILS  where USERBLOCK !=? AND ( USERSTATUS !=? AND USERSTATUS !=? ) and  createduserid=? ";
			enctrace("instqury for unblock user is:" + instqury);
			
			System.out.println(" Blocked Users ====>  " + instqury);
			try {
				/*List result = jdbctemplate.queryForList(instqury);*/
				List result = jdbctemplate.queryForList(instqury,new Object[]{"0","2","2M","SU"});
				trace("result list is:" + result.size());
				// System.out.println (" Result Found Details is "+result);
				if (!(result.isEmpty())) {
					trace("Unblock User Detaila found ------->");
					ListIterator iterator_unblkuser = result.listIterator();
					System.out.println("iterator_unblkuser----> " + iterator_unblkuser);
					while (iterator_unblkuser.hasNext()) {
						Map unblkuser = (Map) iterator_unblkuser.next();
						BigDecimal profileid = (BigDecimal) unblkuser.get("PROFILEID");
						System.out.println("profile_id----- > " + profileid);
						String inst_id = (String) unblkuser.get("INSTID");
						String profilename = commondesc.getProfileDesc(inst_id, profileid, jdbctemplate);
						System.out.println("profilename----> " + profilename);
						unblkuser.put("PROFILE_NAME", profilename);
						iterator_unblkuser.remove();
						iterator_unblkuser.add(unblkuser);
						System.out.println("block user details " + result);
					}
					setBlockuserdetail(result);
					setUnblocklist_status('Y');
				} else {
					setUnblocklist_status('N');
				}
			} catch (Exception e) {
				setUnblocklist_status('E');
				trace("Error while getting the Use details ");
				// System.out.println("Error while getting the Use details ");
			}

			// List result =jdbctemplate.queryForList(instqury);
			// setBlockuserdetail(result);

			trace("\n\n\n");
			enctrace("\n\n\n");
			return "unblockuser";
		} else {
			String userid = (String) session.getAttribute("USERID");
			instname = (String) session.getAttribute("Instname");
			// String instqury="Select
			// usr.USERID,usr.USERNAME,usr.PROFILEID,usr.INSTID,usr.FIRSTNAME,usr.LASTNAME,usr.EMAILID,decode(usr.USERSTATUS,'1','Active','0','In
			// Activate') as USERSTATUS,prof.PROFILE_ID,prof.PROFILE_NAME from
			// USER_DETAILS usr,"+getProfilelistTemp()+" prof where
			// usr.USERBLOCK = '1' and usr.PROFILEID=prof.PROFILE_ID and
			// usr.INSTID='"+instname+"' and usr.createduserid='"+userid+"'";
		/*	String blockuserqry = "Select USERID,USERNAME,PROFILEID,INSTID,FIRSTNAME,LASTNAME,EMAILID,decode(USERSTATUS,'1','Active','0','In Active') as USERSTATUS, decode(USERBLOCK, '0','ACTIVE','1','BLOCKED','0M','UNBLOCKED...WAITING FOR AUTHORIZATION',USERBLOCK) AS USERBLOCK, INSTID , UNBLOCKED_BY, TO_CHAR(UNBLOCKED_DATE ,'DD-MM-YYYY') AS UNBLOCKED_DATE  from USER_DETAILS  where     USERBLOCK != '0'   and INSTID='"
					+ instname + "' AND ( USERSTATUS != '2' AND USERSTATUS != '2M' ) ";
			// String instqury="Select
			// USERID,USERNAME,PROFILEID,INSTID,FIRSTNAME,LASTNAME,EMAILID,decode(USERSTATUS,'1','Active','0','In
			// Active') as USERSTATUS from USER_DETAILS where USERBLOCK != '0'
			// and INSTID='"+instname+"'";
			enctrace("Blocked user list qry :" + blockuserqry);

			try {
				List resultblocked = jdbctemplate.queryForList(blockuserqry);*/
			
			//by gowtham130819
			String blockuserqry = "Select USERID,USERNAME,PROFILEID,INSTID,FIRSTNAME,LASTNAME,EMAILID,decode(USERSTATUS,'1','Active','0','In Active') as USERSTATUS, decode(USERBLOCK, '0','ACTIVE','1','BLOCKED','0M','UNBLOCKED...WAITING FOR AUTHORIZATION',USERBLOCK) AS USERBLOCK, INSTID , UNBLOCKED_BY, TO_CHAR(UNBLOCKED_DATE ,'DD-MM-YYYY') AS UNBLOCKED_DATE  from USER_DETAILS  where     USERBLOCK != ?  and INSTID=	?AND ( USERSTATUS !=? AND USERSTATUS != ? ) ";
			
			enctrace("Blocked user list qry :" + blockuserqry);

			try {
				/*List resultblocked = jdbctemplate.queryForList(blockuserqry);*/
				
				List resultblocked = jdbctemplate.queryForList(blockuserqry,new Object[]{"0",instname,"2","2M"});
				System.out.println("block user details " + resultblocked);
				trace("Result Found Details is:" + resultblocked.size());
				// System.out.println (" Result Found Details is "+result);
				if (!(resultblocked.isEmpty())) {
					trace("Unblock User Detaila found ------->");
					ListIterator iterator_unblkuser = resultblocked.listIterator();
					System.out.println("iterator_unblkuser----> " + iterator_unblkuser);
					while (iterator_unblkuser.hasNext()) {
						Map unblkuser = (Map) iterator_unblkuser.next();
						BigDecimal profileid = (BigDecimal) unblkuser.get("PROFILEID");
						System.out.println("profile_id----- > " + profileid);
						String inst_id = (String) unblkuser.get("INSTID");
						String unblockby = (String) commondesc.getUserName(inst_id,
								(String) unblkuser.get("UNBLOCKED_BY"), jdbctemplate);
						if (unblockby == null) {
							unblockby = "--";
						}
						unblkuser.put("UNBLOCKED_BY", unblockby);
						String profilename = commondesc.getProfileDesc(inst_id, profileid, jdbctemplate);
						System.out.println("profilename----> " + profilename);
						unblkuser.put("PROFILE_NAME", profilename);
						iterator_unblkuser.remove();
						iterator_unblkuser.add(unblkuser);
						System.out.println("block user details " + resultblocked.size());
					}
					setBlockuserdetail(resultblocked);
					setUnblocklist_status('Y');

				} else {
					setUnblocklist_status('N');
				}
			} catch (Exception e) {
				setUnblocklist_status('E');
				trace("Error while getting the Use details ");
				// System.out.println("Error while getting the Use details ");
			}
			trace("\n\n");
			enctrace("\n\n");
			return "unblockuser";

		}
	}

	public String getUnblockedBy(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		String username = null;
		try {
			/*String userblokcqry = "SELECT UNBLOCKED_BY FROM USER_DETAILS WHERE INSTID='" + instid + "' AND USERID='"
					+ usercode + "'";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry, String.class);*/
			
			
			//by gowtham130819
			String userblokcqry = "SELECT UNBLOCKED_BY FROM USER_DETAILS WHERE INSTID=?AND USERID=?";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry, new Object[]{instid,usercode,},String.class);
		} catch (Exception e) {
		}
		return username;
	}

	public String getCurentBlockStatus(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		String username = null;
		try {
			/*String userblokcqry = "SELECT USERBLOCK FROM USER_DETAILS WHERE INSTID='" + instid + "' AND USERID='"
					+ usercode + "'";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry, String.class);*/
			
			//by gowtham130819
			String userblokcqry = "SELECT USERBLOCK FROM USER_DETAILS WHERE INSTID=?AND USERID=?";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry,new Object[]{instid,usercode}, String.class);
		} catch (Exception e) {
		}
		return username;
	}

	public String getPasswordResetBy(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		String username = null;
		try {
			/*String userblokcqry = "SELECT PASSWDRESET_BY FROM USER_DETAILS WHERE INSTID='" + instid + "' AND USERID='"
					+ usercode + "'";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry, String.class);*/
			
			//by gowtham130819
			String userblokcqry = "SELECT PASSWDRESET_BY FROM USER_DETAILS WHERE INSTID=? AND USERID=?";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry,new Object[]{instid,usercode}, String.class);
		} catch (Exception e) {
		}
		return username;
	}

	public String getPasswordResetStatus(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		String username = null;
		try {
			/*String userblokcqry = "SELECT FORGOTPASSWORDFLAG FROM USER_DETAILS WHERE INSTID='" + instid
					+ "' AND USERID='" + usercode + "'";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry, String.class);*/
			
			
			//by gowtham130819
			String userblokcqry = "SELECT FORGOTPASSWORDFLAG FROM USER_DETAILS WHERE INSTID=? AND USERID=?";
			enctrace("userblokcqry :" + userblokcqry);
			username = (String) jdbctemplate.queryForObject(userblokcqry,new Object[]{instid,usercode}, String.class);
		} catch (Exception e) {
		}
		return username;
	}

	public String unblckusr() {
		trace("*************** Adding unblckuser Begins **********");
		enctrace("*************** Adding unblckuser  Begins **********");
		HttpSession session = getRequest().getSession();

		IfpTransObj transact = commondesc.myTranObject("UNBLOCK", txManager);
		String userid = getRequest().getParameter("userid");
		String instid = getRequest().getParameter("instid");

		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
Date date=new Date();
		
		String usertype = (String) session.getAttribute("USERTYPE");
		try {
			trace("*************** unblock User begin ***************");
			enctrace("*************** unblock User begin ***************");

			String userblock = "0", authmsg = "", externaluser = "";
			String unblockedby = this.getUnblockedBy(instid, userid, jdbctemplate);
			trace("Getting unblocked by....got : " + unblockedby);
			String curuserblockstatus = this.getCurentBlockStatus(instid, userid, jdbctemplate);
			trace("Getting currenty unblock status...got : " + curuserblockstatus);
			trace("Unblocking user type is : " + usertype);
			if (usertype.equals("SUPERADMIN")) {
				String dualauth = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
				externaluser = (String) session.getAttribute("EXTERNALUSER");
				trace("Dual auth enabled for super admin : " + dualauth);
				if (dualauth.equals("Y")) {
					if (unblockedby == null || curuserblockstatus == null) {
						userblock = "0M";
						authmsg = " .Waiting for authorization ";
					} else if (unblockedby.equals(externaluser)) {
						userblock = "0M";
						authmsg = " .Waiting for authorization ";
					} else if (!unblockedby.equals(externaluser) && curuserblockstatus.equals("0M")) {
						userblock = "0";
						authmsg = "";
					}
				} else {
					userblock = "0";
					authmsg = "";
				}
			} else {
				String act = "M";// getRequest().getParameter("act");
				externaluser = comUserCode(session);
				if (unblockedby == null || curuserblockstatus == null) {
					userblock = "0M";
					authmsg = " .Waiting for authorization ";
				} else if (unblockedby.equals(externaluser)) {
					userblock = "0M";
					authmsg = " .Waiting for authorization ";
				} else if (!unblockedby.equals(externaluser) && curuserblockstatus.equals("0M")) {
					userblock = "0";
					authmsg = "";
				}
			}

			auditbean.setActmsg("Deleting the user [ " + userid + " ] unblocked.... " + authmsg);
			auditbean.setAuditactcode("30");
			

			//added by gowtham_220719
			trace("ip address======>  "+ip);
			auditbean.setIpAdress(ip);
			
			
			commondesc.insertAuditTrail(instid, externaluser, auditbean, jdbctemplate, txManager);

			System.out.println("instid----> userblock--> " + instid);
			
			/*String query = "update USER_DETAILS  set USERBLOCK = '" + userblock + "',RETRYCOUNT='0', UNBLOCKED_BY='"
			+ externaluser + "', UNBLOCKED_DATE=sysdate  where USERID='" + userid + "' and INSTID='" + instid+ "'";
			enctrace("update Query ........." + query);
			int res = jdbctemplate.update(query);*/
			
			//byg owtham
			String query = "update USER_DETAILS  set USERBLOCK = ?,RETRYCOUNT=?, UNBLOCKED_BY=?, UNBLOCKED_DATE=?  where USERID=? and INSTID=? ";
					enctrace("update Query ........." + query);
					int res = jdbctemplate.update(query,new Object[]{userblock,"0",externaluser,date.getCurrentDate(),userid,instid});
			
			trace("update Status ........" + res);
			if (res == 1) {
				txManager.commit(transact.status);
				addActionMessage("User successfully unblocked" + authmsg);
				auditbean.setActmsg("User code [" + userid + "] User successfully unblocked" + authmsg);
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				commondesc.insertAuditTrail(instid, externaluser, auditbean, jdbctemplate, txManager);
				trace("unblockuser" + res);
				return "required_home";
			} else {
				txManager.rollback(transact.status);
				addActionError("Could not unblocked the user");
				trace("Could not unblocked the user");
				return "required_home";
			}
		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError(" Exception occure While Unblock user");
			trace("Exception occure While Unblock user" + e.getMessage());
			e.printStackTrace();
			return "required_home";
		}

		// return "unblckeduserdetail";
	}

	public String forgotpassword() {
		trace("*************** reset password Request begin ***************");
		enctrace("*************** reset password Request begin ***************");
		String sqlqry = "", instid = null;
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");
		trace("User type is : " + usertype);
		try {
			String username = (String) session.getAttribute("SS_USERNAME");
			instid = (getRequest().getParameter("instids"));
			trace("Get user name " + username);

			List institutes = null, requestlist = null;
			String filter = "";
			String userid;
			if (!usertype.equals("SUPERADMIN")) {
				System.out.println("get instid is " + instid);
				String createbyuseradmin = "SU";
				userid = (String) session.getAttribute("USERID");
				instid = comInstId(session);
				/*filter = " AND CREATEDUSERID !='SU' AND ( USERSTATUS !='2' AND USERSTATUS !='2M')";
				trace(createbyuseradmin);

			} else {
				filter = " AND CREATEDUSERID ='SU'";
			}

			sqlqry = "SELECT USERID,USERNAME, FIRSTNAME,LASTNAME,EMAILID,INSTID,USERTYPE,LASTLOGIN,FORGOTPASSWORDFLAG,PASSWDRESET_BY,TO_CHAR(PASSWDRESET_DATE,'dd-mon-yyyy' ) as PASSWDRESET_DATE FROM USER_DETAILS  WHERE FORGOTPASSWORDFLAG !='0' "
					+ filter + " ";
			enctrace(" select query :   " + sqlqry);
			enctrace("Query for user list " + sqlqry);
			requestlist = jdbctemplate.queryForList(sqlqry);*/
				

				sqlqry = "SELECT USERID,USERNAME, FIRSTNAME,LASTNAME,EMAILID,INSTID,USERTYPE,LASTLOGIN,FORGOTPASSWORDFLAG,PASSWDRESET_BY,TO_CHAR(PASSWDRESET_DATE,'dd-mon-yyyy' ) as PASSWDRESET_DATE FROM USER_DETAILS  WHERE FORGOTPASSWORDFLAG !=? AND CREATEDUSERID !=? AND ( USERSTATUS !=? AND USERSTATUS !=?)";
				enctrace(" select query :   " + sqlqry);
				enctrace("Query for user list " + sqlqry);
				requestlist = jdbctemplate.queryForList(sqlqry,new Object[]{"0","SU","2","2M"});
				
				trace(createbyuseradmin);

			} else {
				filter = " AND CREATEDUSERID ='SU'";
				
				sqlqry = "SELECT USERID,USERNAME, FIRSTNAME,LASTNAME,EMAILID,INSTID,USERTYPE,LASTLOGIN,FORGOTPASSWORDFLAG,PASSWDRESET_BY,TO_CHAR(PASSWDRESET_DATE,'dd-mon-yyyy' ) as PASSWDRESET_DATE FROM USER_DETAILS  WHERE FORGOTPASSWORDFLAG !=? AND CREATEDUSERID =?";
				enctrace(" select query :   " + sqlqry);
				enctrace("Query for user list " + sqlqry);
				requestlist = jdbctemplate.queryForList(sqlqry,new Object[]{"0","SU"});
			}
			if (requestlist.isEmpty()) {
				addActionError("No User waiting for forgot password generation");
				return "required_home";
			}

			trace("Got the forgot password list : " + requestlist.iterator());
			ListIterator itr = requestlist.listIterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String passwordresetby = (String) mp.get("PASSWDRESET_BY");
				String resetpasswdstatus = (String) mp.get("FORGOTPASSWORDFLAG");
				if (!usertype.equals("SUPERADMIN")) {
					passwordresetby = commondesc.getUserName(instid, passwordresetby, jdbctemplate);
					if (passwordresetby == null) {
						passwordresetby = "--";
					}
				}

				if (resetpasswdstatus != null && resetpasswdstatus.equals("1")) {
					resetpasswdstatus = "Waiting for password generation";
				} else if (resetpasswdstatus != null && resetpasswdstatus.equals("0M")) {
					resetpasswdstatus = "Password generation verified...Waiting for Authorization";
				}
				mp.put("FORGOTPASSWORDFLAG", resetpasswdstatus);
				mp.put("PASSWDRESET_BY", passwordresetby);
				itr.remove();
				itr.add(mp);
			}
			setForgotuserreq(requestlist);
			System.out.println("requestlist :" + requestlist);

		} catch (Exception e) {
			addActionError("Exception : could not process reset Forgot password ");
			trace("Exception: could not process reset Forgot password " + e.getMessage());
			e.printStackTrace();
		}
		return "resetforgotpassword";
	}

	public String resetForgotpwd() {
		trace("*************** reset password Request inprocess  ***************");
		enctrace("*************** reset password Request inprocess ***************");
		HttpSession session = getRequest().getSession();
		String user, inst, userdetail;
		String saltkey = "";
		int totalchecklist = 0, updatecnt = 0;
		int updatestatus = 0;
		String usertype = (String) session.getAttribute("USERTYPE");
		userdetail = (String) getRequest().getParameter("checkeduser");
		String[] splitdetails = userdetail.split("-");
		inst = splitdetails[1];
		user = splitdetails[0];

		//added by gowtham_220719
		 String  ip=(String) session.getAttribute("REMOTE_IP");
		 Date date=new Date();
		
		IfpTransObj transact = commondesc.myTranObject("RESETPASS", txManager);
		String externaluser = (String) session.getAttribute("EXTERNALUSER");
		try {
			String passwdresetby = this.getPasswordResetBy(inst, user, jdbctemplate);
			trace("passwdresetby :" + passwdresetby);
			String passwdresetstatus = this.getPasswordResetStatus(inst, user, jdbctemplate);
			trace("passwdresetstatus :" + passwdresetstatus);
			String passwdstatus = "0M", resetmsg = "";
			Boolean directrest = false;
			trace("usertype is : " + usertype);
			String authmsg = "";

			trace("Logged in user : " + externaluser);

			if (usertype.equals("SUPERADMIN")) {
				String dualauth = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
				trace("Dual auth enabled for super admin : " + dualauth);
				if (dualauth.equals("Y")) {
					if (passwdresetby == null || passwdresetstatus == null) {
						directrest = false;
						passwdstatus = "0M";
						authmsg = ". Waiting for Authorization ";
					} else if (passwdresetby.equals(externaluser)) {
						directrest = false;
						passwdstatus = "0M";
						authmsg = ". Waiting for Authorization ";
					} else if (!passwdresetby.equals(externaluser) && passwdresetstatus.equals("0M")) {
						directrest = true;
						passwdstatus = "0";

					}
				}
			} else {
				String act = "M";
				if (act.equals("M")) {
					if (passwdresetby == null || passwdresetstatus == null) {
						directrest = false;
						passwdstatus = "0M";
						authmsg = ". Waiting for Authorization ";
					} else if (passwdresetby.equals(externaluser)) {
						directrest = false;
						passwdstatus = "0M";
						authmsg = ". Waiting for Authorization ";
					} else if (!passwdresetby.equals(externaluser) && passwdresetstatus.equals("0M")) {
						directrest = true;
						passwdstatus = "0";
					}
				}
			}

			String updatepwd = "";
			trace("Generating password : " + directrest);
			if (directrest) {

				String password = commondesc.generateRandomNumber(8);
				String subject = "Reset Password";
				String content = " Your Forgot Password Request has been processed. Your New Password is " + password;
				content += " \n Regards \n Technical Support ";

				System.out.println("content::" + content);

				addActionMessage(content);

				String sendmail = this.sendPasswordAsEmail(inst, user, password, subject, content, jdbctemplate);
				trace("Forgot password request....Sending New Password in mail..got : " + sendmail);
				if (sendmail == null || !sendmail.equals("SUCCESS")) {
					updatestatus = -1;
					addActionError("Unable to Generate New Password ..." + sendmail);
				}
				/*updatepwd = "UPDATE USER_DETAILS set  RETRYCOUNT=0,USERBLOCK=0,LOGINSTATUS=0,FIRSTTIME=1,FRSTLOGIN='F', FORGOTPASSWORDFLAG='0',CHANGPASS_DATE=SYSDATE where USERID="
						+ user + " AND INSTID='" + inst + "'";
				// resetmsg =" .Password is :"+password ;
				resetmsg = "";
				updatestatus = jdbctemplate.update(updatepwd);
				trace("Updating forgot password...production...got : " + updatestatus);
				updatepwd = "UPDATE USER_DETAILS set  RETRYCOUNT=0,USERBLOCK=0,LOGINSTATUS=0,FIRSTTIME=1,FRSTLOGIN='F', FORGOTPASSWORDFLAG='0',CHANGPASS_DATE=SYSDATE where USERID="
						+ user + " AND INSTID='" + inst + "'";
				updatestatus = jdbctemplate.update(updatepwd);
			} else {
				updatepwd = "UPDATE USER_DETAILS  set FORGOTPASSWORDFLAG='" + passwdstatus + "', PASSWDRESET_BY='"
						+ externaluser + "', PASSWDRESET_DATE=SYSDATE  where USERID=" + user + " AND INSTID='" + inst
						+ "'";
				resetmsg = " .Waiting for authorization";
				updatestatus = jdbctemplate.update(updatepwd);
				updatepwd = "UPDATE USER_DETAILS  set FORGOTPASSWORDFLAG='" + passwdstatus + "', PASSWDRESET_BY='"
						+ externaluser + "', PASSWDRESET_DATE=SYSDATE  where USERID=" + user + " AND INSTID='" + inst
						+ "'";
				updatestatus = jdbctemplate.update(updatepwd);
			}*/
				
				/*updatepwd = "UPDATE USER_DETAILS set  RETRYCOUNT=0,USERBLOCK=0,LOGINSTATUS=0,FIRSTTIME=1,FRSTLOGIN='F', FORGOTPASSWORDFLAG='0',CHANGPASS_DATE=SYSDATE where USERID="
				+ user + " AND INSTID='" + inst + "'";
		// resetmsg =" .Password is :"+password ;
		resetmsg = "";
		updatestatus = jdbctemplate.update(updatepwd);*/ 
		
		//by gowtham130819
		updatepwd = "UPDATE USER_DETAILS set  RETRYCOUNT=?,USERBLOCK=?,LOGINSTATUS=?,FIRSTTIME=?,FRSTLOGIN=?, FORGOTPASSWORDFLAG=?,CHANGPASS_DATE=? where USERID=? AND INSTID=?";
		// resetmsg =" .Password is :"+password ;
		resetmsg = "";
		updatestatus = jdbctemplate.update(updatepwd,new Object[]{"0","0","0","1","F","0",date.getCurrentDate(),user,inst});
		
		trace("Updating forgot password...production...got : " + updatestatus);
		
		/*updatepwd = "UPDATE USER_DETAILS set  RETRYCOUNT=0,USERBLOCK=0,LOGINSTATUS=0,FIRSTTIME=1,FRSTLOGIN='F', FORGOTPASSWORDFLAG='0',CHANGPASS_DATE=SYSDATE where USERID="
				+ user + " AND INSTID='" + inst + "'";
		updatestatus = jdbctemplate.update(updatepwd);*/
		
		//by gowtham130819
		//updatepwd = "UPDATE USER_DETAILS set  RETRYCOUNT=?,USERBLOCK=?,LOGINSTATUS=?,FIRSTTIME=?,FRSTLOGIN=?, FORGOTPASSWORDFLAG=?,CHANGPASS_DATE=? where USERID=? AND INSTID=?";
	//	updatestatus = jdbctemplate.update(updatepwd,new Object[]{"0","0","0","1","F","0",date.getCurrentDate(),user,inst});
		
	} else {
		
		/*updatepwd = "UPDATE USER_DETAILS  set FORGOTPASSWORDFLAG='" + passwdstatus + "', PASSWDRESET_BY='"
				+ externaluser + "', PASSWDRESET_DATE=SYSDATE  where USERID=" + user + " AND INSTID='" + inst+ "'";
		resetmsg = " .Waiting for authorization";
		updatestatus = jdbctemplate.update(updatepwd);
		
		updatepwd = "UPDATE USER_DETAILS  set FORGOTPASSWORDFLAG='" + passwdstatus + "', PASSWDRESET_BY='"
				+ externaluser + "', PASSWDRESET_DATE=SYSDATE  where USERID=" + user + " AND INSTID='" + inst+ "'";
		updatestatus = jdbctemplate.update(updatepwd);*/
		
		//by gowtham140819
		updatepwd = "UPDATE USER_DETAILS  set FORGOTPASSWORDFLAG=?, PASSWDRESET_BY=?, PASSWDRESET_DATE=?  where USERID=? AND INSTID=?";
		resetmsg = " .Waiting for authorization";
		updatestatus = jdbctemplate.update(updatepwd,new Object[]{passwdstatus,externaluser,date.getCurrentDate(),user,inst});
		
		//updatepwd = "UPDATE USER_DETAILS  set FORGOTPASSWORDFLAG=?, PASSWDRESET_BY=?, PASSWDRESET_DATE=? where USERID=? AND INSTID=?";
		//updatestatus = jdbctemplate.update(updatepwd,new Object[]{passwdstatus,externaluser,date.getCurrentDate(),user,inst});
		
		
				
			enctrace("Query for update " + updatepwd);

			trace(" updating status...got :  " + updatestatus);
	}
			if (updatestatus == 1) {
				String seleteceduser = commondesc.getUserName(inst, user, jdbctemplate);
				txManager.commit(transact.status);
				addActionMessage("Password SuccessFully Reset for user : " + seleteceduser + resetmsg);
				trace("Password SuccessFully Reset for user : " + seleteceduser + resetmsg);
				trace("/n/n");
				enctrace("/n/n");

				/************* AUDIT BLOCK **************/
				try {
					
					
					//added by gowtham_220719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);
					auditbean.setActmsg("Password SuccessFully Reset for user : " + seleteceduser + "" + authmsg);
					auditbean.setUsercode(externaluser);
					auditbean.setAuditactcode("3001");
					commondesc.insertAuditTrail(inst, externaluser, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				txManager.rollback(transact.status);
				addActionError("could n't reset Password ");
				trace("could n't reset Password ");
				trace("/n/n");
				enctrace("/n/n");
			}
			return "required_home";
	} catch (Exception e) {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			addActionError("Could Not Continue The Process");
			trace("Exception: could not process reset Forgot password " + e.getMessage());
			e.printStackTrace();
			trace("/n");
			enctrace("/n");
		}
		return forgotpassword();
	}

	public void callProfileList() {
		String instid = getRequest().getParameter("instid");
		String profilelist_qry = "select PROFILE_ID,PROFILE_NAME from " + getProfilelistMain()
				+ " where USER_CODE='SU'and  INSTID='" + instid + "'";
		enctrace("profilelist : " + profilelist_qry);
		List profilelist_result = jdbctemplate.queryForList(profilelist_qry);
		trace("profilelist Result" + profilelist_result);
		String profilelist;
		if (!(profilelist_result.isEmpty())) {
			Iterator profilelist_itr = profilelist_result.iterator();
			profilelist = "<div align='center'><table border='0' cellpadding='0' cellspacing='0' width='100%'><tr><td>Profile</td><td><select name=\"profile\" id=\"profile\" >";
			profilelist = profilelist + "<option value='00'>SELECT</option>";
			while (profilelist_itr.hasNext()) {
				Map map = (Map) profilelist_itr.next();
				BigDecimal profileid = (BigDecimal) map.get("PROFILE_ID");
				String profilename = ((String) map.get("PROFILE_NAME"));
				profilelist = profilelist + "<option value=\" " + profileid + "\">" + profilename + "</option>";
			}
			profilelist = profilelist + "</select></td></tr>";
		} else {
			profilelist = "<div align='center'><table border='0' cellpadding='0' cellspacing='0' width='100%'><tr><td>Profile</td><td><select name=\"profile\" id=\"profile\" >";
		}
		try {
			System.out.println("profilelist---->  " + profilelist);
			response.getWriter().write(profilelist);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Transactional
	public String authorizeProfileDetails() throws Exception {
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("UPDPROFILE", txManager);
		String errormsg = "";
		userManagementActionDAO userdao = new userManagementActionDAO();
		String usertype = (String) session.getAttribute("USERTYPE");
		String profileid = getRequest().getParameter("prof_id");
		String auth = getRequest().getParameter("auth");
		doact = getRequest().getParameter("doact");
		String externalusername = "", instid = "";
		
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Date date=new Date();
		String remarks = getRequest().getParameter("reason") == null ? "" : getRequest().getParameter("reason");
		if (usertype != null && usertype.equals("SUPERADMIN")) {
			externalusername = (String) session.getAttribute("EXTERNALUSER");
			instid = getRequest().getParameter("instid");
		} else {
			externalusername = comUserCode(session);
			instid = comInstId(session);
		}
		try {

			System.out.println("instid --- " + instid);
			System.out.println("profileid : " + profileid);
			String updateprofileqry;
			int result = -1;
			if (auth != null && auth.equals("Authorize")) {
				errormsg = "authorized";
				/*updateprofileqry = "UPDATE " + getProfilelistTemp() + " SET  AUTH_CODE='1', AUTH_BY='"
						+ externalusername + "', AUTH_DATE=SYSDATE,REMARKS='" + remarks + "' WHERE INSTID='" + instid
						+ "' AND PROFILE_ID='" + profileid + "'";
				result = jdbctemplate.update(updateprofileqry);*/
				
				
				//by gowtham-140819
				updateprofileqry = "UPDATE " + getProfilelistTemp() + " SET  AUTH_CODE=?, AUTH_BY=?, AUTH_DATE=?,REMARKS=? WHERE INSTID=? AND PROFILE_ID=?";
				result = jdbctemplate.update(updateprofileqry,new Object[]{"1",externalusername,date.getCurrentDate(),remarks,instid,profileid});
				
				trace("Updating authorize status....got : " + result);
				/*
				 * result = userdao.deleteProfileFromProduction(instid,
				 * profileid, jdbctemplate); trace(
				 * "Delete profile from production....got : " + result ); result
				 * = userdao.deleteProfilePrivilageFromProduct(instid,
				 * profileid, jdbctemplate); trace(
				 * "Delete profile from production....got : " + result ); result
				 * = userdao.moveProfileToProduction(instid, profileid,
				 * jdbctemplate); trace(
				 * "Move profile list to prodution....got : " + result ); result
				 * = userdao.moveProfilePrivtoProduction(instid, profileid,
				 * jdbctemplate); trace(
				 * "move profile privilage to production....got : " + result );
				 */
			} else {
				errormsg = "De-authorized";
				/*updateprofileqry = "UPDATE " + getProfilelistTemp() + " SET  AUTH_CODE='9', AUTH_BY='"
						+ externalusername + "', AUTH_DATE=SYSDATE,REMARKS='" + remarks + "' WHERE INSTID='" + instid
						+ "' AND PROFILE_ID='" + profileid + "'";
				result = jdbctemplate.update(updateprofileqry);
			}
*/
				
			/*	//added on 26-02-2021
				String usercheck="SELECT COUNT(*) FROM USER_DETAILS WHERE PROFILE_ID='"+profileid+"' ";
				enctrace("usercheckquery--->"+usercheck);
				String usercheckcount = (String) jdbctemplate.queryForObject(usercheck, String.class);
				System.out.println("usercheckcount-->"+usercheckcount);
				if(usercheckcount!="0"){
					addActionError("User is Exist, Kindly Delete the User then Remove the Profile");
					}*/
				
				
				//by gowtham-140819
updateprofileqry = "UPDATE " + getProfilelistTemp() + " SET  AUTH_CODE=?, AUTH_BY=?, AUTH_DATE=?,REMARKS=? WHERE INSTID=? AND PROFILE_ID=?";
result = jdbctemplate.update(updateprofileqry,new Object[]{"9",externalusername,date.getCurrentDate(),remarks,instid,profileid});
}
			enctrace("updateprofileqry :" + updateprofileqry);

			if (result < 0) {
				transact.txManager.rollback(transact.status);
				addActionError("Could not " + errormsg + " profile");
				return "requird_home";
			}
			transact.txManager.commit(transact.status);
			addActionMessage("Profile " + errormsg + " successfully");

			/************* AUDIT BLOCK **************/
			try {
				String profiledesc = commondesc.getProfileDesc(instid, profileid, jdbctemplate);
				auditbean.setActmsg("Profile  [" + profiledesc + "] " + errormsg + " successfully");
				auditbean.setUsercode(externalusername);
				
			 	
			 	//added by gowtham_220719
			 	trace("ip address======>  "+ip);
			 	auditbean.setIpAdress(ip);

				auditbean.setAuditactcode("3001");
				auditbean.setRemarks(remarks);
				commondesc.insertAuditTrail(instid, externalusername, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("Could not " + errormsg + " profile " + e.getMessage());
			e.printStackTrace();
		}
		return "required_home";
	}

	public String saveViewDetails() {
		trace("*************** saveEditDetails Begins **********");
		enctrace("*************** saveEditDetails Begins **********");

		String profileid = (getRequest().getParameter("profileid")).trim();
		String profiledesc = (getRequest().getParameter("profiledesc")).trim();
		String profname = (getRequest().getParameter("profname")).trim();
		String instid = (getRequest().getParameter("instname")).trim();
		trace("The INST NAME IS   %$^%$^$^%$^%$ " + instid);
		setProf_name(profname);
		setInstname(instname);
		setProfile_id(Integer.parseInt(profileid));
		String loginbranchcode = (getRequest().getParameter("branch"));
		trace("loginbranchcode      " + loginbranchcode);
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("SS_USERNAME");

		String usertype = (String) session.getAttribute("USERTYPE");
		trace("User type is : " + usertype);
		doact = getRequest().getParameter("doact");
		trace("Do Action : " + doact);
		if (doact == null) {
			addActionError("Unable to identified the action");
			return "required_home";
		}

		if (loginbranchcode != null) {
			if (loginbranchcode.equals("1")) {
				loginbranchcode = "1";
			} else {
				loginbranchcode = "0";
			}
		} else {
			loginbranchcode = "0";
		}

		String loginretrycount = (getRequest().getParameter("retrycount"));
		if (loginretrycount != null) {
			loginretrycount = "1";
		} else {
			loginretrycount = "0";
		}
		String loginipaddress = (getRequest().getParameter("ipaddress"));
		if (loginipaddress != null) {
			loginipaddress = "1";
		} else {
			loginipaddress = "0";
		}

		String userexp_date = (getRequest().getParameter("userexpdate"));
		if (userexp_date != null) {
			userexp_date = "1";
		} else {
			userexp_date = "0";
		}

		String userpswdrpt = (getRequest().getParameter("pswreptable"));
		if (userpswdrpt != null) {
			userpswdrpt = "1";
		} else {
			userpswdrpt = "0";
		}

		String psswd_expdate = (getRequest().getParameter("pswexpdate"));
		if (psswd_expdate != null) {
			psswd_expdate = "1";
		} else {
			psswd_expdate = "0";
		}
		// session.setAttribute("prof_name", prof_name);
		trace("profiledesc  " + profiledesc);
		trace("loginbranchcode		" + loginbranchcode);
		trace("loginipaddress		" + loginipaddress);
		trace("userexp_date		" + userexp_date);
		trace("loginretrycount		" + loginretrycount);
		trace("userpswdrpt		" + userpswdrpt);
		trace("psswd_expdate		" + psswd_expdate);

		session.setAttribute("prof_desc", profiledesc);
		session.setAttribute("loginipaddress", loginipaddress);
		session.setAttribute("usrpswexp", userexp_date);
		session.setAttribute("userpswdrpt", userpswdrpt);
		session.setAttribute("psswd_expdate", psswd_expdate);
		session.setAttribute("loginbranchcode", loginbranchcode);
		session.setAttribute("loginretrycount", loginretrycount);
		session.setAttribute("Instname", instid);

		if (usertype.equals("SUPERADMIN")) {
			String menuid = null;
			String mainmenuid = null;
			List<List> mstrmenuqryrsult = new ArrayList<List>();
			// instname=(getRequest().getParameter("instname"));
			setInstname(instid);
			List result;
			// JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
			String qury = "select PROFILE_LIST from " + getADMIN_PROFILE_PRIVILEGE() + "";
			result = jdbctemplate.queryForList(qury);
			System.out.println(result);
			Iterator itr = result.iterator();

			Map map;
			while (itr.hasNext()) {

				map = (Map) itr.next();
				proflist = (map.get("PROFILE_LIST")).toString();

			}
			System.out.println(" Profile List  ----->>>>>" + proflist);
			/*String masterqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
					+ " where MENUVISIBILITY ='1' and parentid='0' ORDER BY MENUORDER ASC";
			List mstrqryrsult = jdbctemplate.queryForList(masterqury);*/
			//by gowtham140819
			String masterqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()+ " where MENUVISIBILITY =? and parentid=? ORDER BY MENUORDER ASC";
			List mstrqryrsult = jdbctemplate.queryForList(masterqury,new Object[]{"1","0"});
			List rsult1 = new ArrayList<List>();
			Iterator mstrqryitr = mstrqryrsult.iterator();
			mstrmenuqryrsult.clear();
			while (mstrqryitr.hasNext()) {
				map = (Map) mstrqryitr.next();
				mainmenuid = (map.get("MENUID")).toString();
				menuid = "," + mainmenuid + ",";
				rsult1.clear();
				if (proflist.contains(menuid)) {
					/*String mastermenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
							+ " where MENUVISIBILITY ='1' and MENUID='" + mainmenuid + "' ORDER BY MENUORDER ASC";
					rsult1 = jdbctemplate.queryForList(mastermenuqury);*/
					
					//by gowtham-140819
					String mastermenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()+ " where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
					rsult1 = jdbctemplate.queryForList(mastermenuqury,new Object[]{"1",mainmenuid});
					
				}
				mstrmenuqryrsult.addAll(rsult1);
			}
			System.out.println("MENU LIST" + mstrmenuqryrsult);
			trace("length  " + mstrmenuqryrsult.size());
			setAdminproflist(mstrmenuqryrsult);

			// String selectcheck_query="select * from
			// "+getPROFILE_PRIVILEGE_TEMP()+" where PROFILE_ID='"+profileid+"'
			// ";
			String selectcheck_query = "select * from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID='"
					+ profileid + "' AND INST_ID='" + instid + "'";
			enctrace(selectcheck_query);
			List checklist = jdbctemplate.queryForList(selectcheck_query);
			if (!(checklist.isEmpty())) {
				Iterator itr1 = checklist.iterator();
				while (itr1.hasNext()) {
					Map temp = (Map) itr1.next();
					String menulist = (String) temp.get("MENU_LIST");
					System.out.println("MENULIST =====>" + menulist);
					String listvalue = menulist.replace("[", "").replace("]", "");
					String[] newmenulist = listvalue.split(",");
					List nwmenlist = new ArrayList();
					System.out.println("splitted rec__" + newmenulist);
					for (int i = 0; i < newmenulist.length; i++) {
						System.out.println("-" + newmenulist[i].trim() + "-");
						int arrpos = 0;
						if (!newmenulist[i].trim().equals("00")) {

							nwmenlist.add(arrpos, newmenulist[i].trim());
							arrpos++;
						}

					}
					trace("New formed menu is " + nwmenlist);
					setSelectedmenulst(nwmenlist);

				}
			}

			String returnstring = "";
			if (doact.equals("$VIEW")) {
				returnstring = "AdminSaveViewDetails_view";
			} else if (doact.equals("$DEL")) {
				returnstring = "AdminSaveViewDetails_del";
			} else if (doact.equals("$DELAUTH")) {
				returnstring = "AdminSaveViewDetails_delauth";
			} else if (doact.equals("$AUTH")) {
				returnstring = "AdminSaveViewDetails";
			} else {
				returnstring = "AdminSaveViewDetails";
			}

			trace("Returingin string john..." + returnstring);
			return returnstring;
		} else {
			trace("Welcome to instituion edit Next Page");
			/*String selectinst_query = "select * from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID='" + profileid
					+ "'AND INST_ID='" + instid + "'";
			enctrace("selectinst_query is:" + selectinst_query);
			List checkinstlist = jdbctemplate.queryForList(selectinst_query);*/
			
			//by gowtham-140819
			String selectinst_query = "select * from " + getPROFILE_PRIVILEGE_TEMP() + " where PROFILE_ID=?AND INST_ID=?";
			enctrace("selectinst_query is:" + selectinst_query);
			List checkinstlist = jdbctemplate.queryForList(selectinst_query,new Object[]{profileid,instid});
			trace("checkinstlist is:" + checkinstlist.size());
			if (!(checkinstlist.isEmpty())) {
				Iterator itrinst = checkinstlist.iterator();
				while (itrinst.hasNext()) {
					Map temp = (Map) itrinst.next();
					String menulist = (String) temp.get("MENU_LIST");
					trace("MENULIST =====>" + menulist);
					String listvalue = menulist.replace("[", "").replace("]", "");
					String[] newmenulistinst = listvalue.split(",");
					List nwmenlistinst = new ArrayList();
					trace("splitted rec__" + newmenulistinst);
					trace("nwmenlistinst is" + nwmenlistinst.size());
					for (int i = 0; i < newmenulistinst.length; i++) {
						trace("-" + newmenulistinst[i].trim() + "-");
						int arrpos = 0;
						if (!newmenulistinst[i].trim().equals("00")) {

							nwmenlistinst.add(arrpos, newmenulistinst[i].trim());
							arrpos++;
						}
					}
					trace("New formed menu is ==> " + nwmenlistinst);
					setSelectedinstmenulist(nwmenlistinst);

				}
			}

			String returnstring = "";
			if (doact.equals("$VIEW")) {
				returnstring = "InstAdminSaveViewDetails_view";
			} else if (doact.equals("$DEL")) {
				returnstring = "InstAdminSaveViewDetails_del";
			} else if (doact.equals("$DELAUTH")) {
				returnstring = "InstAdminSaveViewDetails_delauth";
			} else if (doact.equals("$AUTH")) {
				returnstring = "InstAdminSaveViewDetails";
			} else {
				returnstring = "InstAdminSaveViewDetails";
			}

			trace("Returingin string instuser..." + returnstring);
			return returnstring;

		}
	}

	public String authUserHome() {
		trace("Auth user home...");
		enctrace("Auth user home..");
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");
		String username=(String)session.getAttribute("SS_USERNAME");
	/*	
		trace(" usertype ====  "+usertype  +" username=====  "+username);
		
		session.setAttribute("USERNAME", username);*/
		
		String instid = null;
		try {
			String instcond = "";
			if (usertype.equals("SUPERADMIN")) {
				instid = "";
			} else {
				instid = comInstId(session);
			}

			List instlist = instdao.getListOfInstitutionFromProductionByInsit(instid, jdbctemplate);
			trace("got instlist : " + instlist.size());
			if (instlist.isEmpty()) {
				addActionError("No Institution configured....");
				return "required_home";
			}
			setInstitutionlist(instlist);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "userauth_home";
	}

	public void viewUserAtuhList() throws Exception {
		trace("View user auth list...");
		enctrace("View user auth list...");
		HttpSession session = getRequest().getSession();
		userManagementActionDAO userdao = new userManagementActionDAO();
		String usertype = (String) session.getAttribute("USERTYPE");
		String instid = "";
		System.out.println("usertype--->"+usertype);
		
		String authlistqry="";
		List authlist = null;
		try {/*
			String condition = "", externalusername = "";
			if (usertype.equals("SUPERADMIN")) {
				instid = getRequest().getParameter("instid");
				externalusername = (String) session.getAttribute("EXTERNALUSER");
				condition = " AND CREATEDUSERID='SU' AND ADDED_BY !='" + externalusername + "'";
			} else {
				condition = " AND CREATEDUSERID !='SU'";
				externalusername = comUserCode(session);
				instid = comInstId(session);
			}

			String authlistqry = "SELECT USERID,USERNAME FROM USER_DETAILS WHERE INSTID='" + instid
					+ "' and  auth_status='0' " + condition + " ";
			enctrace("authlistqry :" + authlistqry);
			List authlist = jdbctemplate.queryForList(authlistqry);
			trace("Getting auth user list : " + authlist.size());
			if (authlist.isEmpty()) {
				// addActionError("No Records waiting for authorization");
				getResponse().getWriter().write("<option value='-1'>No Records waiting for authorization<option>");
				return;
			}

			Iterator itr = authlist.iterator();
			String option = "";
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				option += "<option value='" + (String) (Object) mp.get("USERID").toString() + "' > "
						+ (String) mp.get("USERNAME") + " </option>";
			}
			getResponse().getWriter().write(option);
			return;
		*/
			
		
		
		
		
		

			String condition = "", externalusername = "";
			if (usertype.equals("SUPERADMIN")) 
			{
				instid = getRequest().getParameter("instid");
				externalusername = (String) session.getAttribute("EXTERNALUSER");
				//condition = " AND CREATEDUSERID='SU' AND ADDED_BY !='" + externalusername + "'";
				authlistqry = "SELECT USERID,USERNAME FROM USER_DETAILS WHERE INSTID=? and  auth_status=? AND CREATEDUSERID=?AND ADDED_BY !=?";
				enctrace("authlistqry :" + authlistqry);
				authlist = jdbctemplate.queryForList(authlistqry,new Object[]{instid,"0","SU",externalusername});
			} 
			
			else  
			{
				
				externalusername = comUserCode(session);
				instid = comInstId(session);
				//condition = " AND CREATEDUSERID !='SU'";
				authlistqry = "SELECT USERID,USERNAME FROM USER_DETAILS WHERE INSTID=? and  auth_status=? AND CREATEDUSERID !=? ";
				enctrace("authlistqry :" + authlistqry);
				authlist = jdbctemplate.queryForList(authlistqry,new Object[]{instid,"0","SU"});
			}
			
			 
			

			/*String authlistqry = "SELECT USERID,USERNAME FROM USER_DETAILS WHERE INSTID='" + instid+ "' and  auth_status='0' " + condition + " ";
			enctrace("authlistqry :" + authlistqry);
			List authlist = jdbctemplate.queryForList(authlistqry);*/
			
			//by gowtham-140819
			/*String authlistqry = "SELECT USERID,USERNAME FROM USER_DETAILS WHERE INSTID='" + instid+ "' and  auth_status='0' " + condition + " ";*/
			//enctrace("authlistqry :" + authlistqry);
			/*List authlist = jdbctemplate.queryForList(authlistqry,new Object[]{instid,"0",});*/
			
			trace("Getting auth user list : " + authlist.size());
			
			if (authlist.isEmpty()) {
				// addActionError("No Records waiting for authorization");
				getResponse().getWriter().write("<option value='-1'>No Records waiting for authorization<option>");
				return;
			}

			Iterator itr = authlist.iterator();
			String option = "";
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				option += "<option value='" + (String) (Object) mp.get("USERID").toString() + "' > "
						+ (String) mp.get("USERNAME") + " </option>";
			}
			getResponse().getWriter().write(option);
			return;
	
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception..." + e.getMessage());
			// addActionError("Exception....Could not get the details for
			// authorization");
			getResponse().getWriter().write("Exception....Could not get the details for authorization");
			return;
		}

	}

	public String authViewData() {
		
		trace("View user auth data...called");
		enctrace("View user auth data...");
		HttpSession session = getRequest().getSession();
		userManagementActionDAO userdao = new userManagementActionDAO();
		String usertype = (String) session.getAttribute("USERTYPE");
		String usercode = getRequest().getParameter("usercode");
		String username=(String) session.getAttribute("USERNAME");
		
	/*	session.setAttribute("USERTYPE", usertype);
		session.setAttribute("USERCODE", usercode);
		session.setAttribute("USERNAME1", username);*/
		
		trace("usertype ====   "+usertype   +"   USERNAME1"+username);
		
		String instid = "";
		try {
			String condition = "";
			if (usertype.equals("SUPERADMIN")) {
				condition = " AND CREATEDUSERID='SU'";
				instid = getRequest().getParameter("instid");
			} else {
				instid = comInstId(session);
			}
			setInstitutionid(instid);
			String authlistqry = "SELECT * FROM USER_DETAILS WHERE INSTID='" + instid + "' and auth_status='0' "
					+ condition + " AND USERID='" + usercode + "' ";
			enctrace("authlistqry :" + authlistqry);
			List authlist = jdbctemplate.queryForList(authlistqry);
			trace("Getting auth user list : " + authlist.size());
			if (authlist.isEmpty()) {
				addActionError("No Records waiting for authorization");
				return "required_home";
			}
			Iterator itr = authlist.iterator();
			String statusdesc = "";
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String profilid = (String) (Object) mp.get("PROFILEID").toString();
				String authcode = (String) mp.get("AUTH_STATUS");
				if (authcode.equals("0")) {
					statusdesc = "Waiting for authorization";
				} else if (authcode.equals("1")) {
					statusdesc = "Authourized";
				} else if (authcode.equals("9")) {
					statusdesc = "De-Authorized";
				}
				String profildesc = commondesc.getProfileDesc((String) mp.get("INSTID"), profilid, jdbctemplate);
				mp.put("PROFILEDESC", profildesc);
				mp.put("AUTH_STATUS", statusdesc);
				String branchcode = (String) mp.get("BRANCHCODE");
				System.out.println("branchcode" + branchcode);

				String branchdesc = commondesc.getBranchDesc(instid, (String) mp.get("BRANCHCODE"), jdbctemplate);
				mp.put("BRANCHDESC", branchdesc);

			}

			setUserdetail(authlist);
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception..." + e.getMessage());
			addActionError("Exception....Could not get the details for authorization");
		}
		return "userauth_data";
	}

	public String deleteauthViewData() {
		trace("Delete View user auth data...");
		enctrace("Delete View user auth data...");
		HttpSession session = getRequest().getSession();
		userManagementActionDAO userdao = new userManagementActionDAO();
		String usertype = (String) session.getAttribute("USERTYPE");
		String usercode = getRequest().getParameter("usercode");
		String instid = "";
		try {
			String condition = "";
			if (usertype.equals("SUPERADMIN")) {
				condition = " AND CREATEDUSERID='SU'";
				instid = getRequest().getParameter("instid");
			} else {
				instid = comInstId(session);
			}
			setInstitutionid(instid);
			String authlistqry = "SELECT * FROM USER_DETAILS WHERE INSTID='" + instid + "' " + condition
					+ " AND USERID='" + usercode + "' ";
			enctrace("authlistqry :" + authlistqry);
			List authlist = jdbctemplate.queryForList(authlistqry);
			trace("Getting auth user list : " + authlist.size());
			if (authlist.isEmpty()) {
				addActionError("No Records waiting for authorization");
				return "required_home";
			}
			ListIterator itr = authlist.listIterator();
			String statusdesc = "";
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String profilid = (String) (Object) mp.get("PROFILEID").toString();
				String authcode = (String) mp.get("AUTH_STATUS");
				if (authcode.equals("0")) {
					statusdesc = "Waiting for authorization";
				} else if (authcode.equals("1")) {
					statusdesc = "Authourized";
				} else if (authcode.equals("9")) {
					statusdesc = "De-Authorized";
				}

				String userstatus = (String) (Object) mp.get("USERSTATUS");

				if (userstatus.equals("2M")) {
					userstatus = "Deleted, Waiting for authorization";
				}
				mp.put("USERSTATUS", userstatus);

				String profildesc = commondesc.getProfileDesc((String) mp.get("INSTID"), profilid, jdbctemplate);
				trace("Getting profile descrition for [ " + profilid + " ]...got :  " + profildesc);
				mp.put("PROFILEDESC", profildesc);
				mp.put("AUTH_STATUS", statusdesc);
				itr.remove();
				itr.add(mp);
			}

			setUserdetail(authlist);
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception..." + e.getMessage());
			addActionError("Exception....Could not get the details for authorization");
		}
		return "userdetails_deleteview_auth";
	}

	public String authorizeDeletedUser() {
		HttpSession session = getRequest().getSession();
		String instid = getRequest().getParameter("instid");
		String usercode = getRequest().getParameter("userid");
		String reason = getRequest().getParameter("reason");
		String authbtn = getRequest().getParameter("submit");
		IfpTransObj transact = commondesc.myTranObject("AUTHDEL", txManager);
		String usertype = (String) session.getAttribute("USERTYPE");
		String loggedinuser = (String) session.getAttribute("EXTERNALUSER");
		
		//added by gowtham_220719
				String  ip=(String) session.getAttribute("REMOTE_IP");

		
		try {
			int result = -1;
			String authmsg = "";
			if (authbtn != null) {
				trace("Authorizing....");
				result = this.deleteUser(instid, "USER_DETAILS", "2", usercode, reason, jdbctemplate);
				trace("updating user main table....got : " + result);
				result = this.deleteUser(instid, "USER_DETAILS", "2", usercode, reason, jdbctemplate);
				trace("updating user temp table....got : " + result);
				authmsg = "Deleted user authorized succesfully ";
			} else {
				result = this.deleteUser(instid, "USER_DETAILS", "1", usercode, reason, jdbctemplate);
				trace("updating user temp table....got : " + result);
				authmsg = "Deleted user de-authrozed succesfully ";
			}

			String username = commondesc.getUserNameFromTemp(instid, usercode, jdbctemplate);
			auditbean.setActmsg(username + " " + authmsg);
			transact.txManager.commit(transact.status);
			addActionMessage(authmsg);

			/************* AUDIT BLOCK **************/
			try {
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(loggedinuser);
				auditbean.setAuditactcode("3001");
				auditbean.setRemarks(reason);
				commondesc.insertAuditTrail(instid, loggedinuser, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("Exception....got " + e.getMessage());
			e.printStackTrace();
		}
		return "required_home";
	}

	public int deleteUser(String instid, String tablename, String userstatus, String usercode, String remarks,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteqry = "UPDATE " + tablename + " SET USERSTATUS='" + userstatus + "', REMARKS='" + remarks
				+ "', AUTH_BY='" + usercode + "',AUTH_DATE=SYSDATE WHERE INSTID='" + instid + "' AND USERID='"
				+ usercode + "' ";
		x = jdbctemplate.update(deleteqry);
		return x;
	}

	public String authDeauthActionUser() {
		trace("Auth user action...*************");
		enctrace("Auth user action...*************");
		IfpTransObj transact = commondesc.myTranObject("authuser", txManager);
		HttpSession session = getRequest().getSession();
		userManagementActionDAO userdao = new userManagementActionDAO();
		String usertype = (String) session.getAttribute("USERTYPE");
		String usercode = getRequest().getParameter("userid");
		String auth = getRequest().getParameter("auth");
		String remarks = getRequest().getParameter("reason");
		String instid = "";
		

		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");


		try {
			trace("user type is : " + usertype);
			String condition = "", respmsg = "", authusercode = "";
			if (usertype.equals("SUPERADMIN")) {
				condition = " AND CREATEDUSERID='SU'";
				authusercode = (String) session.getAttribute("EXTERNALUSER");
				instid = getRequest().getParameter("instid");
			} else {
				instid = comInstId(session);
				condition = " AND INSTID='" + instid + "'";
				authusercode = comUserCode(session);
			}

			String username = commondesc.getUserNameFromTemp(instid, usercode, jdbctemplate);
			if (username == null) {
				username = usercode;
			}

			trace("Auth/De-auth user is : " + authusercode);
			int result = -1;
			if (auth != null) {
				trace("Authorising....");
				result = userdao.updateUserAuthStatus(instid, condition, usercode, "1", authusercode, "", jdbctemplate);
				/*
				 * trace("Updating auth status...got : " + result ); result =
				 * userdao.deleteFromProduction(instid, condition, usercode,
				 * jdbctemplate); trace("Deleting from production...got : " +
				 * result ); result = userdao.moveUserToProduction(instid,
				 * condition, usercode, jdbctemplate); trace(
				 * "Move user to production...got : " + result );
				 */

				// setting for audit traial
				auditbean.setActmsg("User [" + username + "]" + username + " Authorized ");

				String password = commondesc.generateRandomNumber(8);

				String content = "", subject = "", mailreq = "";
				/*
				 * List mailconfiglistlist =jdbctemplate.queryForList(
				 * "SELECT * FROM IFD_MAILALERTCONFIG WHERE ACTIONNAME='ADDUSER' and inst_id='"
				 * +instid+"'"); if(!(mailconfiglistlist.isEmpty())) { Iterator
				 * itr1 = mailconfiglistlist.iterator(); while( itr1.hasNext()){
				 * Map temp=(Map) itr1.next(); content=(String)
				 * temp.get("MAIL_DATA"); subject=(String) temp.get("SUBJECT");
				 * mailreq=(String) temp.get("MAIL_REQ"); } } content =
				 * content.replaceAll("#LB#", "\n"); content =
				 * content.replaceAll("#LT#", "\t"); content =
				 * content.replaceAll("#PASSWORD#", password);
				 */

				content = " Dear User, \n\t You have been successfully added as a StarCARDMAN Appplication User. Your Login Password is "
						+ password + "\n";
				content += " Contact Your Administrator in case of any Clarifications.";
				content += " \n\t Regards - \n Technical Support";

				subject = "Login Credential For StarCARDMAN Application";

				String passwdmail = "";

				passwdmail = this.sendPasswordAsEmail(instid, usercode, password, subject, content, jdbctemplate);

				System.out.println("password---" + content);

				if (!passwdmail.equals("SUCCESS")) {
					result = -1;
					addActionError("Could Not Send Password to Email." + passwdmail);
				}

				respmsg = "" + username + "- User Authorized Successfully  and password is " + password;

			} else {
				trace("De-Authorising....");
				result = userdao.updateUserAuthStatus(instid, condition, usercode, "9", authusercode, remarks,
						jdbctemplate);
				trace("Updating de-auth status...got : " + result);
				respmsg = " User De-Authorized Successfully";

				auditbean.setActmsg(username + "  De-Authorized ");
			}

			trace("got result : " + result);
			if (result == 1) {
				transact.txManager.commit(transact.status);
				addActionMessage(respmsg);

				/************* AUDIT BLOCK **************/
				try {
					//added by gowtham_220719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);

					auditbean.setUsercode(authusercode);
					auditbean.setAuditactcode("3001");
					auditbean.setRemarks(remarks);
					commondesc.insertAuditTrail(instid, authusercode, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				transact.txManager.rollback(transact.status);
				addActionError("Could not continue the process");
			}

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			e.printStackTrace();
			trace("Exception..." + e.getMessage());
			addActionError("Exception....Could not get the details for authorization");
		}
		return "required_home";
	}

	public String sendPasswordAsEmail(String instid, String usercode, String password, String subject, String content,
			JdbcTemplate jdbctemplate) throws Exception {
		trace("Got the password : " + password);
		EmailService emailservice = new EmailService();
		String email = commondesc.getUserEmailFromTemp(instid, usercode, jdbctemplate);
		trace("Getting email for the usercode....got : " + email);
		if (email == null) {
			return null;
		}
		String sendmail = "SUCCESS";// emailservice.sendMail(email, subject,
									// content);

		// String sendmail = "SUCCESS";
		trace("Sending Mail....got : " + sendmail);
		if (sendmail != null && sendmail.equals("SUCCESS")) {

			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] bSalt = new byte[8];
			random.nextBytes(bSalt);

			byte[] bDigest = pswd_hash_class.getHash(pswd_hash_class.ITERATION_NUMBER, password, bSalt);

			String hashed_password = pswd_hash_class.byteToBase64(bDigest);
			String sSalt = pswd_hash_class.byteToBase64(bSalt);

/*String passwdupqrytemp = "UPDATE USER_DETAILS SET USERPASSWORD='" + hashed_password + "' WHERE INSTID='"+ instid + "' and USERID='" + usercode + "'";
enctrace("passwdupqrytemp :" + passwdupqrytemp);
jdbctemplate.update(passwdupqrytemp);

String passwdupqry = "UPDATE USER_DETAILS_SALT SET SALT_KEY='" + sSalt + "' WHERE INSTID='" + instid+ "' and USERID='" + usercode + "'";
enctrace("passwdupqry :" + passwdupqry);
jdbctemplate.update(passwdupqry);*/
			
			trace("new password hasing---->   "+hashed_password);
			
			////by gowtham
			String passwdupqrytemp = "UPDATE USER_DETAILS SET USERPASSWORD=? WHERE INSTID=? and USERID=? ";
			enctrace("passwdupqrytemp :" + passwdupqrytemp);
			jdbctemplate.update(passwdupqrytemp,new Object[]{hashed_password,instid,usercode});

			
			trace("new salt key----> "+sSalt);
			
			String passwdupqry = "UPDATE USER_DETAILS_SALT SET SALT_KEY=?  WHERE INSTID=? and USERID=? ";
			enctrace("passwdupqry :" + passwdupqry);
			jdbctemplate.update(passwdupqry,new Object[]{sSalt,instid,usercode});
		}
		return sendmail;
	}

	public void checkUserNameExist() throws Exception {
		String instid = getRequest().getParameter("instid");
		String username = getRequest().getParameter("username");

		/*String userexistqry = "SELECT COUNT(*) FROM USER_DETAILS WHERE INSTID='" + instid + "' AND USERNAME='"+ username + "'";
		enctrace("userexistqry:" + userexistqry);
		int x = jdbctemplate.queryForInt(userexistqry);*/
		
		///by gowtham
		String userexistqry = "SELECT COUNT(*) FROM USER_DETAILS WHERE INSTID=? AND USERNAME=? ";
		enctrace("userexistqry:" + userexistqry);
		int x = jdbctemplate.queryForInt(userexistqry,new Object[]{instid,username,});
		
		if (x != 0) {
			getResponse().getWriter()
					.write("<span style='color:red'>Username Already Exist. Try different name</span>~1");
		} else {
			getResponse().getWriter().write("<span style='color:green'>Username available</span>~0");
		}
	}

	public int countUserUnderProfile(String instid, String profileid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		/*String profileqry = "SELECT COUNT(*) FROM USER_DETAILS WHERE INSTID='" + instid + "' AND PROFILEID='"
				+ profileid + "' AND DELETED_FLAG != '2'";
		enctrace("profileqry :" + profileqry);
		x = jdbctemplate.queryForInt(profileqry);*/
		
		//by gowtham-140819
				String profileqry = "SELECT COUNT(*) FROM USER_DETAILS WHERE INSTID=? AND PROFILEID=? AND DELETED_FLAG != ?";
				enctrace("profileqry :" + profileqry);
				trace("instid"+instid);
				trace(profileid+"profileid");
				
				x = jdbctemplate.queryForInt(profileqry,new Object[]{instid,profileid,"2"});

		return x;
	}

	public String deleteProfile() {
		IfpTransObj transact = commondesc.myTranObject("DELPROFILE", txManager);
		HttpSession session = getRequest().getSession();
		userManagementActionDAO userdao = new userManagementActionDAO();
		String instid = getRequest().getParameter("instid");
		String profileid = getRequest().getParameter("profileid");
		String reason = getRequest().getParameter("reason");
		String usertype = (String) session.getAttribute("USERTYPE");
		String authusercode = (String) session.getAttribute("EXTERNALUSER");
		String act = (String) session.getAttribute("act");

		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");

		
		int result = -1;
		int count = -1;
		try {
			if (usertype.equals("SUPERADMIN")) {

				count = this.countUserUnderProfile(instid, profileid, jdbctemplate);
				if (count > 0) {
					addActionError("User Available For This Profile. Could Not Delete");
					return "required_home";
				}
				String dualauthenabled = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
				trace("Dual Authentication Mode checking...got : " + dualauthenabled);
				if (dualauthenabled.equals("N")) { // direct
					result = userdao.deleteProfileFromProduction(instid, profileid, jdbctemplate);
					trace("Deleted from production....got ; " + result);

					result = userdao.updateDeletedStatusToTemp(instid, profileid, "1", authusercode, reason,
							jdbctemplate);
					trace("Profile Deleted status update in temp....got : " + result);
					addActionMessage("Profile Deleted Successfully.");
					auditbean.setActmsg("Profile ( " + profileid + " ) Deleted Successfully.");
				} else {
					result = userdao.updateDeletedStatusToTemp(instid, profileid, "0", authusercode, reason,
							jdbctemplate);
					trace("Profile Deleted status update in temp....got : " + result);
					addActionMessage("Profile Deleted Successfully. Waiting for Authorization");
					auditbean
							.setActmsg("Profile ( " + profileid + " ) Deleted Successfully. Waiting for Authorization");
				}
			} else {
				if (act == null) {
					addActionError("Could not understand privilage level");
					return "required_home";
				}
				instid = comInstId(session);

				count = this.countUserUnderProfile(instid, profileid, jdbctemplate);
				if (count > 0) {
					addActionError("User Available For This Profile. Could Not Delete");
					return "required_home";
				}

				if (act.equals("D")) {
					result = userdao.deleteProfileFromProduction(instid, profileid, jdbctemplate);
					trace("Deleted from production....got ; " + result);

					result = userdao.updateDeletedStatusToTemp(instid, profileid, "1", authusercode, reason,
							jdbctemplate);
					trace("Profile Deleted status update in temp....got : " + result);
					addActionMessage("Profile Deleted Successfully.");
					auditbean.setActmsg("Profile ( " + profileid + " ) Deleted Successfully.");
				} else {
					result = userdao.updateDeletedStatusToTemp(instid, profileid, "0", authusercode, reason,
							jdbctemplate);
					trace("Profile Deleted status update in temp....got : " + result);
					addActionMessage("Profile Deleted Successfully. Waiting for Authorization");
					auditbean
							.setActmsg("Profile ( " + profileid + " ) Deleted Successfully. Waiting for Authorization");
				}
			}

			txManager.commit(transact.status);
			trace("Committed...");

			/************* AUDIT BLOCK **************/
			try {
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
			 auditbean.setIpAdress(ip);
				
				auditbean.setUsercode(authusercode);
				auditbean.setAuditactcode("3001");
				auditbean.setRemarks(authusercode);
				auditbean.setRemarks(reason);
				commondesc.insertAuditTrail(instid, authusercode, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			txManager.rollback(transact.status);
			trace("Exception...." + e.getMessage());
			addActionError("Unable to delete the profile....");
			e.printStackTrace();
		}
		return "required_home";
	}

	public String deleteProfileAtuhAction() {
		trace("********deleteProfileAtuhAction");
		enctrace("********deleteProfileAtuhAction");
		IfpTransObj transact = commondesc.myTranObject("DELPROFILE", txManager);
		HttpSession session = getRequest().getSession();
		userManagementActionDAO userdao = new userManagementActionDAO();
		String instid = getRequest().getParameter("instid");
		String profileid = getRequest().getParameter("profileid");
		String reason = getRequest().getParameter("reason");
		String usertype = (String) session.getAttribute("USERTYPE");
		String authusercode = (String) session.getAttribute("EXTERNALUSER");
		String authtype = getRequest().getParameter("authtype");
		int result = -1;
		

		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		trace("instid...in request : " + instid);
		if (instid == "") {
			instid = comInstId(session);
			trace("instid...in session : " + instid);
		}

		try {
			if (authtype.equals("AUTH")) { // authorizing...
				trace("Authorizing.....");
				result = userdao.deleteProfileFromProduction(instid, profileid, jdbctemplate);
				trace("Deleted from production....got ; " + result);

				result = userdao.updateDeletedAuthStatus(instid, profileid, "2", "1", authusercode, "", jdbctemplate);
				trace("Updating deleted auth status to temp...got : " + result);
				addActionMessage("Authorized Successfully...");
				auditbean.setActmsg("Profile  (" + profileid + ") Deleted Authorized Successfully.");
			} else {
				trace("De-Authorizing.....");
				result = userdao.updateDeletedAuthStatus(instid, profileid, "0", "1", authusercode, reason,
						jdbctemplate);
				trace("Updating deleted de auth status to temp...got : " + result);
				addActionMessage("De-Authorized Successfully...");
				auditbean.setActmsg("Profile  (" + profileid + ") Deleted Process De-Authorized Successfully.");
			}

			txManager.commit(transact.status);
			trace("Committed...");

			/************* AUDIT BLOCK **************/
			try {

				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setUsercode(authusercode);
				auditbean.setAuditactcode("3001");
				auditbean.setRemarks(reason);
				commondesc.insertAuditTrail(instid, authusercode, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			txManager.rollback(transact.status);
			trace("Exception...." + e.getMessage());
			addActionError("Unable to delete the profile....");
			e.printStackTrace();
		}

		return "required_home";
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
