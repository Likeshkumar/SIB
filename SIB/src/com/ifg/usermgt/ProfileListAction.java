package com.ifg.usermgt;
import com.ifg.Bean.ServerValidationBean;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Date;
import test.Validation;

/**
 * SRNP0003
 * @author CGSPL
 *
 */
public class ProfileListAction extends BaseAction {
	CommonDesc commondesc = new CommonDesc();

	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	ServerValidationBean bean =  new ServerValidationBean();
	public ServerValidationBean getBean() {
		return bean;
	}

	public void setBean(ServerValidationBean bean) {
		this.bean = bean;
	}

	public String comUserCode(HttpSession session) {
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	private static String display;

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	Logger logger = Logger.getLogger(this.getClass());
	private String prof_name, prof_desc, inst_name, loginipaddress, loginexpiary, loginretrycount, loginbranchcode,
			userpswdrpt, usrpswexp;

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public String getInst_name() {
		return inst_name;
	}

	public void setInst_name(String inst_name) {
		this.inst_name = inst_name;
	}

	public String getLoginipaddress() {
		return loginipaddress;
	}

	public void setLoginipaddress(String loginipaddress) {
		this.loginipaddress = loginipaddress;
	}

	public String getLoginexpiary() {
		return loginexpiary;
	}

	public void setLoginexpiary(String loginexpiary) {
		this.loginexpiary = loginexpiary;
	}

	public String getLoginretrycount() {
		return loginretrycount;
	}

	public void setLoginretrycount(String loginretrycount) {
		this.loginretrycount = loginretrycount;
	}

	public String getLoginbranchcode() {
		return loginbranchcode;
	}

	public void setLoginbranchcode(String loginbranchcode) {
		this.loginbranchcode = loginbranchcode;
	}

	public String getUserpswdrpt() {
		return userpswdrpt;
	}

	public void setUserpswdrpt(String userpswdrpt) {
		this.userpswdrpt = userpswdrpt;
	}

	public String getUsrpswexp() {
		return usrpswexp;
	}

	public void setUsrpswexp(String usrpswexp) {
		this.usrpswexp = usrpswexp;
	}

	public String getProf_name() {
		return prof_name;
	}

	public void setProf_name(String prof_name) {
		this.prof_name = prof_name;
	}

	public String getProf_desc() {
		return prof_desc;
	}

	public void setProf_desc(String prof_desc) {
		this.prof_desc = prof_desc;
	}

	private static final long serialVersionUID = 1L;
	HttpServletRequest request;

	public String gettingMenu() {
		/*
		 * List parentmenu_list; JdbcTemplate jdbcTemplate = new
		 * JdbcTemplate(dataSource); System.out.println(
		 * "Inside the gettingMenu() "); String menuselect =
		 * SQLQueries.MASTER_MENU; parentmenu_list =
		 * jdbcTemplate.queryForList(menuselect);
		 * profilebean.setMenulist(parentmenu_list); System.out.println(
		 * "Result set parentmenu_list" + parentmenu_list);
		 */
		return "success";

	}

	public String flagprofile;

	public String getFlagprofile() {
		return flagprofile;
	}

	public void setFlagprofile(String flagprofile) {
		this.flagprofile = flagprofile;
	}

	public String insertProfile() 
	{
		HttpSession session = getRequest().getSession();
		try 
		{
			trace("*************** Insert Profile Begins **********");
			enctrace("*************** Insert Profile Begins **********");
			String loginipaddress, loginexpiary, loginretrycount, userpswdrpt, usrpswexp;
			String prof_name = (getRequest().getParameter("profilename"));
			String prof_desc = (getRequest().getParameter("profiledesc"));
			boolean check;
			int checkvalue;
			
			bean.setProfilename(prof_name);
			check=Validation.charcter(prof_name);
			if(!check)
			{
				addActionError("Kindly Enter Proper Profile Name");
				return "addprevilegefailed";
			}
			check=Validation.NumberCharcter(prof_desc);
			if(!check)
			{
				addActionError("Kindly Enter Profile Description");
				return "addprevilegefailed";
			}
			
			
			String loginbranchcode = (getRequest().getParameter("branch"));
			trace("loginbranchcode==> " + loginbranchcode);
			String i_Name = (String) session.getAttribute("Instname");
			String i_name = i_Name.toUpperCase();
			
			/*String profilenm_query = "select count(PROFILE_NAME) from " + getProfilelistTemp()
			+ " where trim(PROFILE_NAME)='" + prof_name + "' and INSTID ='" + i_name + "' ";
			enctrace("profilenm_query is:" + profilenm_query);
			// System.out.println("profilenm_query==> "+profilenm_query);
			int countprofilenm = jdbctemplate.queryForInt(profilenm_query);*/
			
			///by gowtham
			String profilenm_query = "select count(PROFILE_NAME) from " + getProfilelistTemp()
			+ " where trim(PROFILE_NAME)=? and INSTID =? ";
			enctrace("profilenm_query is:" + profilenm_query);
			// System.out.println("profilenm_query==> "+profilenm_query);
			int countprofilenm = jdbctemplate.queryForInt(profilenm_query,new Object[]{prof_name,i_name});
			
			if (countprofilenm != 0) {
				addActionError("Profile Name Already In Use");
				trace("Profile Name Already In Use");
				return "addprevilegefailed";
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

			loginipaddress = (getRequest().getParameter("ipaddress"));
			if (loginipaddress != null) {
				loginipaddress = "1";
			} else {
				loginipaddress = "0";
			}

			usrpswexp = (getRequest().getParameter("userexpdate"));
			if (usrpswexp != null) {
				usrpswexp = "1";
			} else {
				usrpswexp = "0";
			}

			loginretrycount = "0";

			userpswdrpt = (getRequest().getParameter("pswreptable"));
			if (userpswdrpt != null) {
				userpswdrpt = "1";
			} else {
				userpswdrpt = "0";
			}

			loginexpiary = (getRequest().getParameter("pswexpdate"));
			if (loginexpiary != null) {
				loginexpiary = "1";
			} else {
				loginexpiary = "0";
			}
			trace("Brach added==> " + loginbranchcode);
			trace("Login IP Address==> " + loginipaddress);
			trace("User Expiry Date==> " + usrpswexp);
			trace("Password Repeatable==> " + userpswdrpt);
			trace("Password Expiry Date==> " + loginexpiary);

			session.setAttribute("prof_name", prof_name);
			session.setAttribute("prof_desc", prof_desc);

			session.setAttribute("loginbranchcode", loginbranchcode);
			session.setAttribute("loginipaddress", loginipaddress);
			session.setAttribute("usrpswexp", usrpswexp);

			session.setAttribute("userpswdrpt", userpswdrpt);
			session.setAttribute("loginretrycount", loginretrycount);
			session.setAttribute("loginexpiary", loginexpiary);

			String profileflag = (String) session.getAttribute("PROFILE_FLAG");
			System.out.println("profileflag-----" + profileflag);
			setFlagprofile(profileflag);
			trace("All Values Set into Sessions ");

		} catch (Exception e) {
			addActionError("Error : ");
			trace("Error :" + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "addprevilege";
	}

	private String menu_id;

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	private String instid;

	public String getInstid() {
		return instid;
	}

	public void setInstid(String instid) {
		this.instid = instid;
	}

	private String mkckflag;

	public String getMkckflag() {
		return mkckflag;
	}

	public void setMkckflag(String mkckflag) {
		this.mkckflag = mkckflag;
	}

	public void makerchekstatus() {
		// trace("*************** makerchekstatus Begins **********");
		// enctrace("*************** makerchekstatus Begins **********");
		String mkck_menulist, enable_menu_id, disable_menu_id, enable = "E", disable = "D";

		String query_makerchecker;
		String instid;
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		instid = i_Name.toUpperCase();
		menu_id = getMenu_id();
		enable_menu_id = ", " + enable + "-" + menu_id + ",";
		disable_menu_id = ", " + disable + "-" + menu_id + ",";
		// trace("menu_id "+menu_id);
		// trace("disable_menu_id "+disable_menu_id);
		// trace("enable_menu_id "+ enable_menu_id);

		/*query_makerchecker = "select trim(MENUID) as MENUID from MKRCHKR_CONFIG where INSTID='" + instid + "'";
		enctrace("query_makerchecker is:" + query_makerchecker);
		// System.out.println("query_makerchecker "+query_makerchecker);
		try {
		mkck_menulist = (String) jdbctemplate.queryForObject(query_makerchecker, String.class);*/
		
		///by gowtham
		query_makerchecker = "select trim(MENUID) as MENUID from MKRCHKR_CONFIG where INSTID=? ";
		enctrace("query_makerchecker is:" + query_makerchecker);
		// System.out.println("query_makerchecker "+query_makerchecker);
		try {
		mkck_menulist = (String) jdbctemplate.queryForObject(query_makerchecker,new Object[]{instid}, String.class);
		
			if (mkck_menulist.isEmpty()) {
				trace("no maker cher config found for inst");
			} else {
				if (mkck_menulist.contains(enable_menu_id)) {
					setMkckflag("enable");
					// trace("enable");
				} else if (mkck_menulist.contains(disable_menu_id)) {
					setMkckflag("disabled");
					// trace("disabled");
				}
			}

		} catch (Exception e) {
			trace("Error while macker checker enable " + e.getMessage());
			e.printStackTrace();
		}
		// trace("\n\n");
		// enctrace("\n\n");

	}

	private String inst_id;

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public void makerchekSuperadminstatus() {
		// trace("*************** makerchekstatus Begins **********");
		// enctrace("*************** makerchekstatus Begins **********");
		String mkck_menulist, enable_menu_id, disable_menu_id, enable = "E", disable = "D";

		String query_makerchecker;
		/*
		 * String instid; HttpSession session = getRequest().getSession();
		 * String i_Name = (String)session.getAttribute("Instname");
		 * instid=i_Name.toUpperCase();
		 */
		inst_id = getInst_id();
		String instid = inst_id.toUpperCase();
		menu_id = getMenu_id();
		enable_menu_id = ", " + enable + "-" + menu_id + ",";
		disable_menu_id = ", " + disable + "-" + menu_id + ",";
		// trace("menu_id "+menu_id);
		// trace("disable_menu_id "+disable_menu_id);
		// trace("enable_menu_id "+ enable_menu_id);

		/*query_makerchecker = "select trim(MENUID) as MENUID from MKRCHKR_CONFIG where INSTID='" + inst_id + "'";
		enctrace("query_makerchecker is:" + query_makerchecker);
		// System.out.println("query_makerchecker "+query_makerchecker);
		try {
		mkck_menulist = (String) jdbctemplate.queryForObject(query_makerchecker, String.class);*/
		
		//byh gowtham
		query_makerchecker = "select trim(MENUID) as MENUID from MKRCHKR_CONFIG where INSTID=? ";
		enctrace("query_makerchecker is:" + query_makerchecker);
		// System.out.println("query_makerchecker "+query_makerchecker);
		try {
		mkck_menulist = (String) jdbctemplate.queryForObject(query_makerchecker,new Object[]{inst_id}, String.class);
		
			if (mkck_menulist.isEmpty()) {
				trace("no maker cher config found for inst");
			} else {
				if (mkck_menulist.contains(enable_menu_id)) {
					setMkckflag("enable");
					// trace("enable");
				} else if (mkck_menulist.contains(disable_menu_id)) {
					setMkckflag("disabled");
					// trace("disabled");
				}
			}

		} catch (Exception e) {
			trace("Error while macker checker enable " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");

	}

	public String addPrivelage() throws Exception // FOR BOTH DIRECT AND MAKER
	{

		// *************************************************************************
		// ~~~~~~~ Getting the Current Profile & Profile Description
		// ~~~~~~~~~~~!
		// *************************************************************************
		HttpSession session = getRequest().getSession();

		trace("*************** AddPrivelage Begins **********");
		enctrace("*************** AddPrivelage Begins **********");
		String usercode = comUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("INSTADMIN", txManager);
		String profileflag = getRequest().getParameter("profileflag");
		System.out.println(" ----- profileflag ----- " + profileflag);
		trace(" ----- profileflag ----- " + profileflag);
		Date date=new Date();
		
		/*
		 * String authcode = "1"; String act = "M";
		 * //(String)session.getAttribute("act"); if( act.equals("D")){ authcode
		 * = "1"; }else { authcode = "0"; }
		 */
		String authcode;
		if (profileflag.equals("D")) {
			authcode = "1";
		} else {
			authcode = "0";
		}
		try {
			String P_name, P_desc, i_name, l_branch, l_ip, l_retry, l_exp, u_pswexp, u_pswrpt;
			int countprofilenm = -1, profile_count = -1, insertprofile_status = -1;
			P_name = (String) session.getAttribute("prof_name");
			P_desc = (String) session.getAttribute("prof_desc");
			l_branch = (String) session.getAttribute("loginbranchcode");
			System.out.println("l_branch:::::::::::::" + l_branch);
			l_ip = (String) session.getAttribute("loginipaddress");

			// l_retry = (String)session.getAttribute("loginretrycount");
			l_exp = (String) session.getAttribute("loginexpiary");
			u_pswrpt = (String) session.getAttribute("userpswdrpt");
			u_pswexp = (String) session.getAttribute("usrpswexp");

			String i_Name = (String) session.getAttribute("Instname");
			System.out.println(" branch ::" + l_branch + "login_ip" + l_ip + "l_exp" + l_exp + "u_pswexp" + u_pswexp
					+ "u_pswrpt" + u_pswrpt);

			i_name = i_Name.toUpperCase();
			ArrayList<String> Allmenu = new ArrayList<String>();
			String C_fvalue[] = (getRequest().getParameterValues("mainmenu"));
			trace("C_fvalue.length length length   " + C_fvalue.length);
			String userid = (String) session.getAttribute("USERID");

			int i;
			int submenulength = 0;
			if (C_fvalue != null) {
				Allmenu.add("00");
				// System.out.println(" Profile priviledge value --->"
				// +Allmenu);
				for (i = 0; i < C_fvalue.length; i++) {
					System.out.println("C_fvalue[i].trim()" + C_fvalue[i]);
					Allmenu.add(C_fvalue[i].trim());

					// System.out.println (" Profile priviledge value --->"
					// +Allmenu);

					String submenuid = "mainmenu" + C_fvalue[i];

					System.out.println("submenuid=====>" + submenuid);
					String submenu[] = (getRequest().getParameterValues(submenuid));
					System.out.println("submenu======>" + submenu);
					if (submenu != null) {
						submenulength = submenu.length;
					}
					for (int j = 0; j < submenulength; j++) {
						System.out.println("submenu======>" + submenu[j]);
						Allmenu.add(submenu[j]);
						String mkckflag = submenuid + "Sel" + submenu[j];
						String mkck_id[] = getRequest().getParameterValues(mkckflag);

						if (mkck_id != null) {
							for (int k = 0; k < mkck_id.length; k++) {
								System.out.println("Mk Ck Flag====>" + mkck_id[k]);
								if (!(mkck_id[k].equals("-1"))) {
									Allmenu.add(mkck_id[k]);
								}
							}
						}
					}
				}
				Allmenu.add("00");
			}

			// System.out.println("Allmenu"+Allmenu);

			String profileid = "X";
			synchronized (this) {
				profileid = commondesc.getProfileid(i_name, jdbctemplate);
				System.out.println("Current Profile ID is ===> " + profileid);
			}

			if ("X".equals(profileid)) {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Error While Add The Profile " + P_name);
				session.removeAttribute("prof_name");
				session.removeAttribute("prof_desc");
				session.removeAttribute("loginbranchcode");
				session.removeAttribute("loginipaddress");
				session.removeAttribute("loginretrycount");
				session.removeAttribute("loginexpiary");
				session.removeAttribute("usrpswexp");
				session.removeAttribute("userpswdrpt");
				return "profilesuccess";
			}
			int insetrprofilelist = 1;
			int menupriv_status = 1;
			// INSERTING IN MIAN TABLES // branch
			// ::0login_ip1l_exp1u_pswexp0u_pswrpt1

			if (profileflag.equals("D")) {
				
				String ProfileList_insert = "INSERT INTO " + getProfilelistMain()
				+ "(PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINBRANCHCODEREQUIRED,LOGINIPADDRESSREQUIRED,"
				+ "USERPASSWORDEXPIRYCHECK,LOGINEXPIRYDATEREQUIRED,USERPASSWORDREPEATABLE,LOGINRETRYCOUNTREQUIRED,USERTYPE,"
				+ "USER_CODE,AUTH_CODE,ADDED_DATE,ADDED_BY,DELETED_FLAG) VALUES ('"
				+ profileid + "','" + P_name + "','" + P_desc + "','" + i_name + "','" + l_branch + "','" + l_ip
				+ "','" + l_exp + "','" + u_pswexp + "','" + u_pswrpt + "','" + u_pswexp + "','B','" + userid
				+ "','" + authcode + "',SYSDATE,'" + userid + "','0')";
				enctrace("ProfileList_insert_temp is.fromd..:" + ProfileList_insert);
				insetrprofilelist = jdbctemplate.update(ProfileList_insert); 
				
				///by gowtham
			/*	String ProfileList_insert = "INSERT INTO " + getProfilelistMain()
				+ "(PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINBRANCHCODEREQUIRED,LOGINIPADDRESSREQUIRED,"
				+ "USERPASSWORDEXPIRYCHECK,LOGINEXPIRYDATEREQUIRED,USERPASSWORDREPEATABLE,LOGINRETRYCOUNTREQUIRED,USERTYPE,"
				+ "USER_CODE,AUTH_CODE,ADDED_DATE,ADDED_BY,DELETED_FLAG) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				enctrace("ProfileList_insert_temp is.fromd..:" + ProfileList_insert);
				insetrprofilelist = jdbctemplate.update(ProfileList_insert,new Object[]{profileid,P_name,P_desc,i_name,l_branch,l_ip
				,l_exp,u_pswexp,u_pswrpt,u_pswexp,"B",userid,authcode,date.getCurrentDate(),userid,"0"}); 
			*/	
				trace("Insertin profilelist main table...got : " + insetrprofilelist);

				String menupriv_insert = "INSERT INTO " + getPROFILE_PRIVILEGE()
				+ "(PROFILE_ID,MENU_LIST,INST_ID) VALUES ('" + profileid + "','" + Allmenu + "','" + i_name+ "')";
				enctrace("menupriv_insert is:" + menupriv_insert);
				menupriv_status = jdbctemplate.update(menupriv_insert);
				
				//by gowtham
				/*String menupriv_insert = "INSERT INTO " + getPROFILE_PRIVILEGE()
				+ "(PROFILE_ID,MENU_LIST,INST_ID) VALUES (?,?,?)";
				enctrace("menupriv_insert is:" + menupriv_insert);
				menupriv_status = jdbctemplate.update(menupriv_insert,new Object[]{profileid,Allmenu,i_name});*/
				
				trace("Insertin ifprofile privilage table...got : " + menupriv_status);
			}
			// INSERTING IN TEMP TABLES
			
			String ProfileList_insert_temp = "INSERT INTO " + getProfilelistTemp()
			+ "(PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINBRANCHCODEREQUIRED,LOGINIPADDRESSREQUIRED,USERPASSWORDEXPIRYCHECK,LOGINEXPIRYDATEREQUIRED,USERPASSWORDREPEATABLE,LOGINRETRYCOUNTREQUIRED,USERTYPE,USER_CODE,AUTH_CODE,ADDED_DATE,ADDED_BY,DELETED_FLAG) VALUES ('"
			+ profileid + "','" + P_name + "','" + P_desc + "','" + i_name + "','" + l_branch + "','" + l_ip
			+ "','" + l_exp + "','" + u_pswexp + "','" + u_pswrpt + "','" + u_pswexp + "','B','" + userid
			+ "','" + authcode + "',SYSDATE,'" + userid + "','0')";
			enctrace("ProfileList_insert_temp is...:" + ProfileList_insert_temp);
			int insetrprofilelist_temp = jdbctemplate.update(ProfileList_insert_temp);

			///by gowtham
			/*String ProfileList_insert_temp = "INSERT INTO " + getProfilelistTemp()
			+ "(PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINBRANCHCODEREQUIRED,LOGINIPADDRESSREQUIRED,"
			+ "USERPASSWORDEXPIRYCHECK,LOGINEXPIRYDATEREQUIRED,USERPASSWORDREPEATABLE,LOGINRETRYCOUNTREQUIRED,"
			+ "USERTYPE,USER_CODE,AUTH_CODE,ADDED_DATE,ADDED_BY,DELETED_FLAG) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			enctrace("ProfileList_insert_temp is...:" + ProfileList_insert_temp);
		int insetrprofilelist_temp = jdbctemplate.update(ProfileList_insert_temp,new Object[]{profileid,P_name,
		P_desc,i_name,l_branch,l_ip,l_exp,u_pswexp,u_pswrpt,u_pswexp,"B",userid,authcode,date.getCurrentDate(),userid ,"0"});
			*/
			
			
			trace("Insertin profilelist temp table...got : " + insetrprofilelist_temp);

			String menupriv_insert_temp = "INSERT INTO " + getPROFILE_PRIVILEGE_TEMP()+ "(PROFILE_ID,MENU_LIST,INST_ID) "
			+ "VALUES ('" + profileid + "','" + Allmenu + "','" + i_name + "')";
			enctrace("ifprofile privilage temp query is:" + menupriv_insert_temp);
			int menupriv_status_temp = jdbctemplate.update(menupriv_insert_temp);
			
			/*///by gowtham
			String menupriv_insert_temp = "INSERT INTO " + getPROFILE_PRIVILEGE_TEMP()+ "(PROFILE_ID,MENU_LIST,INST_ID) "
					+ "VALUES (?,?,?)";
					enctrace("ifprofile privilage temp query is:" + menupriv_insert_temp);
					int menupriv_status_temp = jdbctemplate.update(menupriv_insert_temp,new Object[]{profileid,Allmenu,i_name});
			
			trace("Inserting ifprofile privilage temp table...got : " + menupriv_status_temp); */

			int updateprofilestatus = commondesc.updateProfileId(i_name, jdbctemplate);		
	        trace("updating sequance...got : " + updateprofilestatus);

	        
/*   	if (insetrprofilelist == 1 && menupriv_status == 1 && insetrprofilelist_temp == 1
					&& menupriv_status_temp == 1 && updateprofilestatus == 1) {*/
	        
	        
			if (insetrprofilelist == 1 && menupriv_status == 1 && insetrprofilelist_temp == 1
					&& menupriv_status_temp == 1 ) {
				trace("Commiting The Txn ");
				transact.txManager.commit(transact.status);

				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", "New Profile \"" + P_name + "\" Added Successfully ");

				// addActionMessage( " New Profile \""+P_name+"\" Added
				// Successfully ");
			} else {
				System.out.println("TXN ROLL BACK");
				transact.txManager.rollback(transact.status);
				addActionError("Error While Add The Profile " + P_name);
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			transact.txManager.rollback(transact.status);
			addActionError(" Error While Insert the Profile Privileges ");
			System.out.println(" Error While Insert the Profile Privileges" + e.getMessage());
			e.printStackTrace();
			// return "profilesuccess";
		}

		session.removeAttribute("prof_name");
		session.removeAttribute("prof_desc");
		session.removeAttribute("loginbranchcode");
		session.removeAttribute("loginipaddress");
		session.removeAttribute("loginretrycount");
		session.removeAttribute("loginexpiary");
		session.removeAttribute("usrpswexp");
		session.removeAttribute("userpswdrpt");

		return "profilesuccess";
	}

	public String updateProfprev() {
		trace("Welcome To Edit Page");
		trace("*************** updateProfprev Begins **********");
		enctrace("*************** updateProfprev Begins **********");
		// *************************************************************************
		// ~~~~~~~ Getting the Current Profile & Profile Description
		// ~~~~~~~~~~~!
		// *************************************************************************
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("UPDPRIV", txManager);
		String usertype = (String) session.getAttribute("USERTYPE");
		try {

			String P_name, P_desc, i_name, l_branch, l_ip, l_retry, l_exp, u_pswexp, u_pswrpt;
			int countprofilenm = -1, profile_count = -1, profile_id = -1, insertprofile_status = -1;
			P_name = (String) session.getAttribute("prof_name");
			P_desc = (String) session.getAttribute("prof_desc");
			l_branch = (String) session.getAttribute("loginbranchcode");
			trace("l_branch   " + l_branch);
			l_ip = (String) session.getAttribute("loginipaddress");
			trace("l_ip	" + l_ip);
			l_retry = (String) session.getAttribute("loginretrycount");
			trace("l_retry		" + l_retry);
			l_exp = (String) session.getAttribute("psswd_expdate");
			trace("l_exp	" + l_exp);
			u_pswexp = (String) session.getAttribute("usrpswexp");
			u_pswrpt = (String) session.getAttribute("userpswdrpt");
			String i_Name = (String) session.getAttribute("Instname");
			i_name = i_Name.toUpperCase();

			ArrayList<String> Allmenu = new ArrayList<String>();
			String C_fvalue[] = (getRequest().getParameterValues("mainmenu"));
			trace("C_fvalue.length length length   " + C_fvalue.length);
			String selectprofileedit = (getRequest().getParameter("prof_id"));
			String userid = (String) session.getAttribute("USERID");
			String flag = (String) session.getAttribute("PROFILE_FLAG");
			System.out.println("----- flag ----- " + flag);
			trace("----- flag ----- " + flag);
			int i;
			if (C_fvalue != null) {
				Allmenu.add("00");
				// trace (" Profile priviledge value --->" +Allmenu);
				for (i = 0; i < C_fvalue.length; i++) {
					// trace ("C_fvalue[i].trim()"+C_fvalue[i]);
					Allmenu.add(C_fvalue[i].trim());

					// trace (" Profile priviledge value --->" +Allmenu);

					String submenuid = "mainmenu" + C_fvalue[i];

					// trace("submenuid=====>"+submenuid);
					String submenu[] = (getRequest().getParameterValues(submenuid));
					for (int j = 0; j < submenu.length; j++) {
						// trace("submenu======>"+submenu[j]);
						Allmenu.add(submenu[j]);
						// String mkckflag=submenuid+"Sel"+j;
						String mkckflag = submenuid + "Sel" + submenu[j];
						// trace("mkckflag=======>"+mkckflag);
						String mkck_id[] = getRequest().getParameterValues(mkckflag);
						// System.out.println("mkck_id===>"+mkck_id.length);
						for (int k = 0; k < mkck_id.length; k++) {
							// trace("Mk Ck Flag====>"+mkck_id[k]);
							if (!(mkck_id[k].equals("-1"))) {
								Allmenu.add(mkck_id[k]);
							}
						}
					}
				}
				Allmenu.add("00");
				// trace ("Allmenu"+Allmenu);
			}
			/*
			 * String externalusername = ""; String authcode = "1"; if(
			 * usertype.equals("SUPERADMIN")){ externalusername =
			 * (String)session.getAttribute("EXTERNALUSER"); String dualauth =
			 * commondesc.getDualAuthEnabledForSuperAdmin( jdbctemplate ); if(
			 * dualauth.equals("Y")){ authcode = "0"; } }else{ String usercode =
			 * comUserCode(session); externalusername =
			 * commondesc.getUserName(i_name, usercode, jdbctemplate);
			 * if(flag.equals("D")){authcode = "1";}else{authcode = "0";}
			 * 
			 * }
			 */
			String authcode;
			String usercode = comUserCode(session);
			String externalusername = commondesc.getUserName(i_name, usercode, jdbctemplate);
			if (flag.equals("D")) {
				authcode = "1";
			} else {
				authcode = "0";
			}
			if (flag.equals("D")) {
				// UPDATING MAIN TABLE
				
				/*String query = "update " + getProfilelistMain() + " set PROFILE_DESC = '" + P_desc
				+ "',LOGINBRANCHCODEREQUIRED='" + l_branch + "',LOGINIPADDRESSREQUIRED='" + l_ip
				+ "',LOGINEXPIRYDATEREQUIRED='" + l_exp + "',LOGINRETRYCOUNTREQUIRED='" + l_retry
				+ "',USERPASSWORDREPEATABLE='" + u_pswrpt + "',USERPASSWORDEXPIRYCHECK='" + u_pswexp
				+ "', ADDED_BY='" + usercode + "',AUTH_CODE='" + authcode
				+ "', ADDED_DATE=SYSDATE,AUTH_BY='',AUTH_DATE='',REMARKS='' where PROFILE_ID='"
				+ selectprofileedit + "' and INSTID='" + i_name + "'";
				enctrace("update profile" + query);
				int res = commondesc.executeTransaction(query, jdbctemplate);*/
				
				///by gowtham
				String query = "update " + getProfilelistMain() + " set PROFILE_DESC = ?,LOGINBRANCHCODEREQUIRED=?,"
						+ "LOGINIPADDRESSREQUIRED=?,LOGINEXPIRYDATEREQUIRED=?,LOGINRETRYCOUNTREQUIRED=?,"
						+ "USERPASSWORDREPEATABLE=?,USERPASSWORDEXPIRYCHECK=?, ADDED_BY=?,AUTH_CODE=?, ADDED_DATE=SYSDATE,AUTH_BY='',AUTH_DATE='',REMARKS='' "
						+ "where PROFILE_ID=? and INSTID=?";
						enctrace("update profile" + query);
						int res =jdbctemplate.update(query,new Object[]{P_desc,l_branch,l_ip,l_exp,l_retry,u_pswrpt,u_pswexp,
								usercode,authcode,selectprofileedit,i_name});
				
				// System.out.println("update profile" +query);

				/*String menupriv_update = "update " + getPROFILE_PRIVILEGE() + " set MENU_LIST='" + Allmenu
				+ "' where PROFILE_ID='" + selectprofileedit + "' AND INST_ID='" + i_name + "'";
				trace("menupriv_update==> " + menupriv_update);
				int menu_result = commondesc.executeTransaction(menupriv_update, jdbctemplate);*/

				//by gowtham
				String menupriv_update = "update " + getPROFILE_PRIVILEGE() + " set MENU_LIST=? where PROFILE_ID=? AND INST_ID=?";
						trace("menupriv_update==> " + menupriv_update);
						int menu_result =jdbctemplate.update(menupriv_update,new Object[]{Allmenu,selectprofileedit,i_name});
				
				trace("menu_result" + menu_result);
			}
			
			/*// UPDATING TEMP TABLE
			String query = "update " + getProfilelistTemp() + " set PROFILE_DESC = '" + P_desc
			+ "',LOGINBRANCHCODEREQUIRED='" + l_branch + "',LOGINIPADDRESSREQUIRED='" + l_ip
			+ "',LOGINEXPIRYDATEREQUIRED='" + l_exp + "',LOGINRETRYCOUNTREQUIRED='" + l_retry
			+ "',USERPASSWORDREPEATABLE='" + u_pswrpt + "',USERPASSWORDEXPIRYCHECK='" + u_pswexp
			+ "', ADDED_BY='" + usercode + "',AUTH_CODE='" + authcode
			+ "', ADDED_DATE=SYSDATE,AUTH_BY='',AUTH_DATE='',REMARKS='' where PROFILE_ID='" + selectprofileedit
			+ "' and INSTID='" + i_name + "'";
			enctrace("update profile" + query);
			int res = commondesc.executeTransaction(query, jdbctemplate);*/
			
			
			
			////by gowtham
			
			// UPDATING TEMP TABLE
						String query = "update " + getProfilelistTemp() + " set PROFILE_DESC =?,LOGINBRANCHCODEREQUIRED=?,"
						+ "LOGINIPADDRESSREQUIRED=?,LOGINEXPIRYDATEREQUIRED=?,LOGINRETRYCOUNTREQUIRED=?,"
						+ "USERPASSWORDREPEATABLE=?,USERPASSWORDEXPIRYCHECK=?, ADDED_BY=?,AUTH_CODE=?,"
						+ " ADDED_DATE=SYSDATE,AUTH_BY='',AUTH_DATE='',REMARKS='' where PROFILE_ID=? and INSTID=?";
						enctrace("update profile" + query);
						int res =jdbctemplate.update(query,new Object[]{P_desc,l_branch,l_ip,l_exp,l_retry,u_pswrpt,u_pswexp,usercode,authcode,selectprofileedit,i_name});
			
			// System.out.println("update profile" +query);

			String menupriv_update = "update " + getPROFILE_PRIVILEGE_TEMP() + " set MENU_LIST='" + Allmenu
			+ "' where PROFILE_ID='" + selectprofileedit + "' AND INST_ID='" + i_name + "'";
			trace("menupriv_update==> " + menupriv_update);
			//int menu_result = commondesc.executeTransaction(menupriv_update, jdbctemplate);
			int menu_result =jdbctemplate.update(menupriv_update);

			////by gowtham
			/*String menupriv_update = "update " + getPROFILE_PRIVILEGE_TEMP() + " set MENU_LIST=? where PROFILE_ID=? AND INST_ID=? ";
					trace("menupriv_update==> " + menupriv_update);
					int menu_result =jdbctemplate.update(menupriv_update,new Object[]{Allmenu,selectprofileedit,i_name});
			*/
			trace("menu_result" + menu_result);
			if (menu_result <= 0 && res <= 0) {
				transact.txManager.rollback(transact.status);
				addActionError(" Could not update Profile details ");
				trace("roll backed successfully");
				return "AdminInstProfileSuccess";
			}
			transact.txManager.commit(transact.status);
			addActionMessage(" Profile details updated successfully");
			trace("Committed successfully");
			// display = "Profile Update Successfully";
		}

		catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Error in currency details ");
			trace(" Error in currency details " + e);
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	private String submenu_id;

	public String getSubmenu_id() {
		return submenu_id;
	}

	public void setSubmenu_id(String submenu_id) {
		this.submenu_id = submenu_id;
	}

	private String mkck_flag;

	public String getMkck_flag() {
		return mkck_flag;
	}

	public void setMkck_flag(String mkck_flag) {
		this.mkck_flag = mkck_flag;
	}

	private String chkr_flag;

	public String getChkr_flag() {
		return chkr_flag;
	}

	public void setChkr_flag(String chkr_flag) {
		this.chkr_flag = chkr_flag;
	}

	private String withoutmkr_chkr;

	public String getWithoutmkr_chkr() {
		return withoutmkr_chkr;
	}

	public void setWithoutmkr_chkr(String withoutmkr_chkr) {
		this.withoutmkr_chkr = withoutmkr_chkr;
	}

	public void selectedMkckcheck() {
		// trace("*************** selectedMkckcheck Begins **********");
		// enctrace("*************** selectedMkckcheck Begins **********");
		HttpSession session = getRequest().getSession();
		// trace("SUbemru from Action Void====> "+getSubmenu_id());
		String mkck = getSubmenu_id().trim();
		String maker = mkck + "-M";
		// trace("Maker===> "+maker);

		String checker = mkck + "-C";
		// trace("Checker===> "+checker);

		String woutmckrchkr = mkck + "-D";
		// trace("WithoutMakerChecker===> "+woutmckrchkr);

		String profile_edit = (String) session.getAttribute("EDIT_PROFILEPREV");
		// trace("PROFILE PREVILAGE FROM SESSION==> "+profile_edit);

		if (profile_edit.contains(maker)) {
			setMkck_flag("MAKER");
			// trace("MAKER");
		} else if (profile_edit.contains(checker)) {
			setMkck_flag("CHECKER");
			// trace("CHECKER");
		} else if (profile_edit.contains(woutmckrchkr)) {
			setMkck_flag("WITHOUTMCKRCHK");
			// trace("WITHOUTMCKRCHK");
		} else {
			setMkck_flag("NO");
			// trace("NO");
		}
		// trace("\n\n");
		// enctrace("\n\n");
	}

}
