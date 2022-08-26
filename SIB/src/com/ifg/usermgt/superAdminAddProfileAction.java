package com.ifg.usermgt;

import com.ifg.Bean.ServerValidationBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.exceptions.exception;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Validation;
import com.ifg.usermgt.userManagementAction;

/**
 * SRNP0003
 * 
 * @author CGSPL
 *
 */
public class superAdminAddProfileAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	ServerValidationBean bean = new ServerValidationBean();

	public ServerValidationBean getBean() {
		return bean;
	}

	public void setBean(ServerValidationBean bean) {
		this.bean = bean;
	}

	userManagementAction ref = new userManagementAction();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	CommonDesc commondesc = new CommonDesc();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public String superAddProf() {
		trace("*************** Profile Privilage Begins **********");
		enctrace("*************** Profile Privilage Begins **********");
		HttpSession session = getRequest().getSession();
		boolean check;
		int checkvalue;
		try {
			String instname = (getRequest().getParameter("instname"));
			String prof_name = (getRequest().getParameter("profilename"));
			String prof_desc = (getRequest().getParameter("profiledesc"));

			/*
			 * String profilename=getRequest().getParameter("profilename");
			 * String profiledesc=getRequest().getParameter("profiledesc");
			 */

			/*
			 * bean.setProfilename(profilename);
			 * bean.setProfiledesc(profiledesc);
			 */

			/*
			 * check=Validation.charcter(profilename); if(!check) {
			 * addActionError("ENTER profilename "); return addProfile(); }
			 */

			/*
			 * check=Validation.NumberCharcter(profiledesc); if(!check) {
			 * addFieldError("profiledesc","ENTER profiledesc"); return
			 * addProfile(); }
			 */

			/*
			 * String profilenm_query = "select count(PROFILE_NAME) from "
			 * +getProfilelistTemp()+" where trim(PROFILE_NAME)='"+prof_name+
			 * "' and INSTID ='"+instname+"' ";
			 * enctrace("profilenm_query:"+profilenm_query);
			 * 
			 * System.out.println("profilenm_query:"+profilenm_query);
			 * 
			 * //String profilenm_query = "select count(PROFILE_NAME) from "
			 * +getProfilelistTemp()+" where trim(PROFILE_NAME)='"
			 * +prof_name.trim()+"'"; int countprofilenm =
			 * jdbctemplate.queryForInt(profilenm_query);
			 */

			// added by gowtham-130819
			String profilenm_query = "select count(PROFILE_NAME) from " + getProfilelistTemp()
					+ " where trim(PROFILE_NAME)=? and INSTID =? ";
			enctrace("profilenm_query:" + profilenm_query);
			System.out.println("profilenm_query:" + profilenm_query);
			// String profilenm_query = "select count(PROFILE_NAME) from
			// "+getProfilelistTemp()+" where
			// trim(PROFILE_NAME)='"+prof_name.trim()+"'";
			int countprofilenm = jdbctemplate.queryForInt(profilenm_query, new Object[] { prof_name, instname });
			if (countprofilenm != 0) {
				addActionError("'" + prof_name + "'  Profile Name Already Exists ");
				trace("'" + prof_name + "'  Profile Name Already Exists ");
				return "SuperAdminProfileAddUnsucces";
			}
			// String branch_validation ="0";
			String branch_validation = (getRequest().getParameter("branch"));

			System.out.println(" branch validation:::" + branch_validation);

			if (branch_validation != null) {
				branch_validation = "1";
			} else {
				branch_validation = "0";
			}

			String login_retry = "0";
			String login_ip = (getRequest().getParameter("ipaddress"));
			if (login_ip != null) {
				login_ip = "1";
			} else {
				login_ip = "0";
			}

			String user_expdate = (getRequest().getParameter("userexpdate"));
			if (user_expdate != null) {
				user_expdate = "1";
			} else {
				user_expdate = "0";
			}

			String psswd_reptable = (getRequest().getParameter("pswreptable"));
			if (psswd_reptable != null) {
				psswd_reptable = "1";
			} else {
				psswd_reptable = "0";
			}

			String psswd_expdate = (getRequest().getParameter("pswexpdate"));
			if (psswd_expdate != null) {
				psswd_expdate = "1";
			} else {
				psswd_expdate = "0";
			}
			System.out.println(" branch validation::" + branch_validation + "login_ip" + login_ip + "user_expdate"
					+ user_expdate + "psswd_expdate" + psswd_expdate);
			session.setAttribute("instname", instname);
			session.setAttribute("prev_prof_name", prof_name);
			session.setAttribute("prof_desc", prof_desc);
			session.setAttribute("loginbranchcode", branch_validation);
			session.setAttribute("loginretrycount", login_retry);
			session.setAttribute("loginipaddress", login_ip);
			session.setAttribute("loginexpiary", user_expdate);
			session.setAttribute("userpswdrpt", psswd_reptable);
			session.setAttribute("usrpswexp", psswd_expdate);

		} catch (Exception ex) {
			addActionError(" Error ");
			trace("Exception :  Error  " + ex.getMessage());
			ex.printStackTrace();
			return "SuperAdminProfileAddUnsucces";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "SuperAdminAddProfileNext";
	}

	List superAddProfilePrevList;

	public List getSuperAddProfilePrevList() {
		return superAddProfilePrevList;
	}

	public void setSuperAddProfilePrevList(List superAddProfilePrev) {
		this.superAddProfilePrevList = superAddProfilePrev;
	}

	public static String proflist;

	public String superAddProfilePrev() {
		trace("*************** Adding Profile Previlage Begins **********");
		enctrace("*************** Adding Profile Previlage Begins **********");

		String menuid = null;
		String mainmenuid = null;
		List<List> mstrmenuqryrsult = new ArrayList<List>();
		HttpSession session = getRequest().getSession();
		String instname = (String) session.getAttribute("instname");
		// setInstname(instname);
		List result;
		String qury = "select PROFILE_LIST from " + getADMIN_PROFILE_PRIVILEGE() + "";
		result = jdbctemplate.queryForList(qury);
		enctrace("query __" + qury);
		Iterator itr = result.iterator();
		Map map;
		// String proflist=null;
		while (itr.hasNext()) {
			map = (Map) itr.next();
			proflist = (map.get("PROFILE_LIST")).toString();
		}
		trace(" Profile List  ----->>>>>" + proflist);
		/*
		 * String masterqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+
		 * " where MENUVISIBILITY ='1' and parentid='0' ORDER BY MENUORDER ASC";
		 * List mstrqryrsult = jdbctemplate.queryForList(masterqury);
		 */

		try {
			// added by gowtham130819
			String masterqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
					+ " where MENUVISIBILITY =?and parentid=? ORDER BY MENUORDER ASC";
			List mstrqryrsult = jdbctemplate.queryForList(masterqury, new Object[] { "1", "0" });
			List rsult1 = new ArrayList<List>();
			Iterator mstrqryitr = mstrqryrsult.iterator();
			mstrmenuqryrsult.clear();
			while (mstrqryitr.hasNext()) {
				map = (Map) mstrqryitr.next();
				mainmenuid = (map.get("MENUID")).toString();
				menuid = "," + mainmenuid + ",";
				rsult1.clear();
				if (proflist.contains(menuid)) {
					/*
					 * String mastermenuqury=
					 * "SELECT MENUNAME,MENUID,ACTION FROM " +getMENU()+
					 * " where MENUVISIBILITY ='1' and MENUID='" +mainmenuid+
					 * "' ORDER BY MENUORDER ASC"; rsult1 =
					 * jdbctemplate.queryForList(mastermenuqury);
					 */

					// by gowtham-130819
					String mastermenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
							+ " where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
					rsult1 = jdbctemplate.queryForList(mastermenuqury, new Object[] { "1", mainmenuid });
					// trace("test"+mastermenuqury)
				}
				mstrmenuqryrsult.addAll(rsult1);
			}

			trace("MENU LIST for previlegde" + mstrmenuqryrsult);
			// System.out.println("length " +mstrmenuqryrsult.size());
			setSuperAddProfilePrevList(mstrmenuqryrsult);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return "AdminProfileview";
		session.setAttribute("prof_list_menu", proflist);
		return "SuperAdminAddProfPrev";
	}

	String menuid;

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	private List superAddProfSubmenuList;

	public List getSuperAddProfSubmenuList() {
		return superAddProfSubmenuList;
	}

	public void setSuperAddProfSubmenuList(List superAddProfSubmenuList) {
		this.superAddProfSubmenuList = superAddProfSubmenuList;
	}

	public void superSubMenuList() {
		List<List> submenulist;
		List<List> submenuqryrsult = new ArrayList<List>();
		String menu1 = getMenuid();
		// System.out.println("proflist ++++++++++++++++++++++++; "+proflist );
		String menu2 = "," + menu1 + ",";
		trace("menuid in  superSubMenuList :  " + menu1);
		String submenuid;
		// HttpSession session = getRequest().getSession();
		// List proflist=(List)session.getAttribute("prof_list_menu");
		trace("proflist  :  " + proflist);
		// System.out.println("superProfAddList : "+superProfAddList);

		if (proflist.contains(menu2)) {
			////// System.out.println("Main menu within list is" +menu1);
			/*
			 * String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+
			 * " where MENUVISIBILITY ='1' and parentid='"+menu1+
			 * "' ORDER BY MENUORDER ASC"; List rsult=
			 * jdbctemplate.queryForList(submenu);
			 */

			// by gowtham-130819
			String submenu = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
					+ " where MENUVISIBILITY =?and parentid=? ORDER BY MENUORDER ASC";
			List rsult = jdbctemplate.queryForList(submenu, new Object[] { "1", menu1 });
			List rsult1 = new ArrayList<List>();
			trace("Submenu query" + rsult);
			Iterator subqryitr = rsult.iterator();
			while (subqryitr.hasNext()) {
				Map map = (Map) subqryitr.next();
				submenuid = (map.get("MENUID")).toString();
				submenu = "," + submenuid + ",";
				// System.out.println("menuid : "+submenu);
				rsult1.clear();
				if (proflist.contains(submenu)) {
					/*
					 * String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM "
					 * +getMENU()+" where MENUVISIBILITY ='1' and MENUID='"
					 * +submenuid+"' ORDER BY MENUORDER ASC"; rsult1 =
					 * jdbctemplate.queryForList(subrmenuqury);
					 */

					// BY GOWTHAM-130819
					String subrmenuqury = "SELECT MENUNAME,MENUID,ACTION FROM " + getMENU()
							+ " where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
					rsult1 = jdbctemplate.queryForList(subrmenuqury, new Object[] { "1", submenuid });
					// System.out.println("Main menu result" +subrmenuqury);
				}
				submenuqryrsult.addAll(rsult1);
			}
			setSuperAddProfSubmenuList(submenuqryrsult);
			// session.setAttribute("submenulist", submenuqryrsult);
			trace("submenuqryrsult " + submenuqryrsult);
		}
	}

	public String superSaveProf() {
		trace("*************** Saving Profile Previlage **********");
		enctrace("*************** Saving Profile Previlage **********");
		HttpSession session = getRequest().getSession();

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		IfpTransObj transact = commondesc.myTranObject("SUPERADMSAVE", txManager);
		String authmsg = "", instname = "";
		try {
			String prof_name = (String) session.getAttribute("prev_prof_name");
			String prof_desc = (String) session.getAttribute("prof_desc");
			String branch_validation = (String) session.getAttribute("loginbranchcode");
			System.out.println("::::::::::::::branch_validation" + branch_validation);

			String login_retry = (String) session.getAttribute("loginretrycount");
			String login_ip = (String) session.getAttribute("loginipaddress");
			String user_expdate = (String) session.getAttribute("loginexpiary");
			String psswd_reptable = (String) session.getAttribute("userpswdrpt");
			String psswd_expdate = (String) session.getAttribute("usrpswexp");
			instname = (String) session.getAttribute("instname");
			String ssname = (String) session.getAttribute("SS_USERNAME");
			String profileid = "X";
			synchronized (this) {
				profileid = commondesc.getProfileid(instname, jdbctemplate);
				if (profileid == null) {
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", "Could not generate profile id...");
					trace("Could not generate profile id...");
					return "required_home";
				}
				trace("Current Profile ID is ===> " + profileid);
			}
			if ("X".equals(profileid)) {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Exception : Error While Add The Profile " + prof_name);
				trace("Exception : Error While Add The Profile " + prof_name);
				session.removeAttribute("prev_prof_name");
				session.removeAttribute("prof_desc");
				session.removeAttribute("loginbranchcode");
				session.removeAttribute("loginretrycount");
				session.removeAttribute("loginipaddress");
				session.removeAttribute("loginexpiary");
				session.removeAttribute("userpswdrpt");
				session.removeAttribute("usrpswexp");
				session.removeAttribute("instname");
				return "SuperAdminProfileAddUnsucces";
			}

			String dualauth = commondesc.getDualAuthEnabledForSuperAdmin(jdbctemplate);
			String tablename = "";
			String privtablename = "";
			String authcode = "1";
			if (dualauth.equals("Y")) {
				tablename = getProfilelistTemp();
				authcode = "0";
				privtablename = " " + getPROFILE_PRIVILEGE_TEMP() + " ";
				authmsg = " Waiting for Authorization ";
			} else {
				tablename = "" + getProfilelistMain() + "";
				privtablename = " " + getPROFILE_PRIVILEGE() + "";

			}

			trace("branch_validation::" + branch_validation);
			String externalusername = (String) session.getAttribute("EXTERNALUSER");

			String query = "INSERT INTO " + tablename
					+ " (PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED,LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE,ADDED_BY,AUTH_CODE,ADDED_DATE,DELETED_FLAG)"
					+ " VALUES ('" + profileid + "','" + prof_name + "','" + prof_desc + "','" + instname + "'," + "'"
					+ login_ip + "','" + user_expdate + "','" + login_retry + "','" + branch_validation + "'," + "'"
					+ psswd_reptable + "','" + psswd_expdate + "','A','SU','" + externalusername + "','" + authcode
					+ "',SYSDATE,'0')";

			/*
			 * //// by gowtham String query = "INSERT INTO " + tablename +
			 * " (PROFILE_ID,PROFILE_NAME,PROFILE_DESC,INSTID,LOGINIPADDRESSREQUIRED,LOGINEXPIRYDATEREQUIRED,LOGINRETRYCOUNTREQUIRED,LOGINBRANCHCODEREQUIRED,USERPASSWORDREPEATABLE,USERPASSWORDEXPIRYCHECK,USERTYPE,USER_CODE,ADDED_BY,AUTH_CODE,ADDED_DATE,DELETED_FLAG)"
			 * + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)";
			 * 
			 */ ArrayList<String> Allmenu = new ArrayList<String>();
			String mainmenus[] = (getRequest().getParameterValues("mainmenu"));
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
					// trace("Selected Submenus===>"+submenus.length);
					if (submenus != null) {
						submenulength = submenus.length;
					}
					trace("submenulength     " + submenulength);
					// To find submenu length
					for (int s = 0; s < submenulength; s++) {
						String subucnt[] = (getRequest().getParameterValues(submenuname));
						trace("selected submenus " + subucnt[s]);
					}
					if (submenus != null) {
						for (int i = 0; i < submenus.length; i++) {
							Allmenu.add(submenus[i].trim());
							// ONLY FOR SELCTBOX IN SUPERADMIN STARTS
							String mkckflag = submenuname + "Sel" + submenus[i];
							System.out.println("mkckflag=======>" + mkckflag);
							String mkck_id[] = getRequest().getParameterValues(mkckflag);
							// System.out.println("mkck_id===>"+mkck_id.length);
							if (mkck_id != null) {
								for (int k = 0; k < mkck_id.length; k++) {
									System.out.println("Mk Ck Flag====>" + mkck_id[k]);
									if (!(mkck_id[k].equals("-1"))) {
										Allmenu.add(mkck_id[k]);
									}
								}
							}
							// ONLY FOR SELCTBOX IN SUPERADMIN ENDS
							// trace(" Profile priviledge value --->" +Allmenu);
						}
					}
				}
				Allmenu.add("00");
			} else {
				trace("THE VALUE from the checkbox<<<<<== none");
			}
			
			
			
			int profile_listinst_status=jdbctemplate.update(query);
			// trace(" Profile priviledge value --->" +Allmenu);

		/*	int profile_listinst_status = jdbctemplate.update(query,
					new Object[] { profileid, prof_name, prof_desc, instname, login_ip, user_expdate, login_retry,
							branch_validation, psswd_reptable, psswd_expdate, "A", "SU", externalusername, authcode,
							"0" });
*/
			
			  String menupriv_insert="INSERT INTO "+privtablename+" (PROFILE_ID,MENU_LIST,INST_ID) VALUES " +"('"+profileid+"','"+Allmenu+"','"+instname+"')";
			 

		/*	// by gowtham
			String menupriv_insert = "INSERT INTO " + privtablename + " (PROFILE_ID,MENU_LIST,INST_ID) VALUES (?,'"
					+ Allmenu + "',?)";
*/
			enctrace("menupriv_insert=======> " + menupriv_insert);

			System.out.println("profileid=====>" + profileid);
			System.out.println("Allmenu====>" + Allmenu);
			System.out.println("instname====>" + instname);
			int menupriv = jdbctemplate.update(menupriv_insert);

			// by gowtham
			// int profile_listinst_status = jdbctemplate.update(query);

			/// by gowtham
			// int menupriv = jdbctemplate.update(menupriv_insert);

			/*
			 * System.out.println("profileid=====>"+profileid);
			 * System.out.println("Allmenu====>"+Allmenu);
			 * System.out.println("instname====>"+instname); int menupriv =
			 * jdbctemplate.update(menupriv_insert,new
			 * Object[]{profileid,Allmenu,instname});
			 */

			int updateprofilestatus = commondesc.updateProfileId(instname, jdbctemplate);

			trace("profile_listinst_status" + profile_listinst_status + "  menupriv===> " + menupriv
					+ " Update Status ===> " + updateprofilestatus);
			if (profile_listinst_status == 1 && menupriv == 1 && updateprofilestatus == 1) {
				transact.txManager.commit(transact.status);
				addActionMessage(" Profile \"" + prof_name + "\"  Added Successfully. " + authmsg);
				trace(" Profile Added Successfully ");

				/************* AUDIT BLOCK **************/
				try {
					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg("Profile  [ " + prof_name + " ] Added Successfully. " + authmsg);
					auditbean.setUsercode(externalusername);
					auditbean.setAuditactcode("3001");
					commondesc.insertAuditTrail(instname, externalusername, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran :" + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				transact.txManager.rollback(transact.status);
				addActionError(" Profile Adding Failed  ");
				trace(" Profile Adding Failed  ");
			}
		} catch (Exception e) {

			transact.txManager.rollback(transact.status);
			e.printStackTrace();
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error while 'INSERT INTO " + getPROFILE_PRIVILEGE_TEMP() + "' table ");
			trace("Exception : Error while 'INSERT INTO " + getPROFILE_PRIVILEGE_TEMP() + "' table " + e.getMessage());

			session.removeAttribute("prev_prof_name");
			session.removeAttribute("prof_desc");
			session.removeAttribute("loginbranchcode");
			session.removeAttribute("loginretrycount");
			session.removeAttribute("loginipaddress");
			session.removeAttribute("loginexpiary");
			session.removeAttribute("userpswdrpt");
			session.removeAttribute("usrpswexp");
			session.removeAttribute("instname");

			return "SuperAdminProfileAddUnsucces";
		}
		session.removeAttribute("prev_prof_name");
		session.removeAttribute("prof_desc");
		session.removeAttribute("loginbranchcode");
		session.removeAttribute("loginretrycount");
		session.removeAttribute("loginipaddress");
		session.removeAttribute("loginexpiary");
		session.removeAttribute("userpswdrpt");
		session.removeAttribute("usrpswexp");
		session.removeAttribute("instname");

		return "required_home";

	}
}
