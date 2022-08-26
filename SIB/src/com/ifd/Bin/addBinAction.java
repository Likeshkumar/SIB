package com.ifd.Bin;

//import com.ifp.beans.BranchBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifd.Bin.AddBinActionBeans;
import com.ifp.beans.AuditBeans;
import com.ifd.Bin.addBinActionDao;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import test.Validation;

import com.ifg.Config.Licence.Licensemanager;

public class addBinAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());
	Licensemanager chcbin = new Licensemanager();
	static Boolean initmail = true;
	static String parentid = "000";

	CommonUtil comutil = new CommonUtil();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AddBinActionBeans binbean = new AddBinActionBeans();
	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public AddBinActionBeans getBinbean() {
		return binbean;
	}

	public void setBinbean(AddBinActionBeans binbean) {
		this.binbean = binbean;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	private List hsmlist;
	private List pinmailerlist;
	private String brcodlen;
	private String branchattached;
	private List lst_bindetails;
	List gllistbean;

	addBinActionDao dao = new addBinActionDao();

	public List getGllistbean() {
		return gllistbean;
	}

	public void setGllistbean(List gllistbean) {
		this.gllistbean = gllistbean;
	}

	public List getLst_bindetails() {
		return lst_bindetails;
	}

	public void setLst_bindetails(List lst_bindetails) {
		this.lst_bindetails = lst_bindetails;
	}

	public List getHsmlist() {
		return hsmlist;
	}

	public void setHsmlist(List hsmlist) {
		this.hsmlist = hsmlist;
	}

	public List getPinmailerlist() {
		return pinmailerlist;
	}

	public void setPinmailerlist(List pinmailerlist) {
		this.pinmailerlist = pinmailerlist;
	}

	public String getBranchattached() {
		return branchattached;
	}

	public void setBranchattached(String branchattached) {
		this.branchattached = branchattached;
	}

	public String getBrcodlen() {
		return brcodlen;
	}

	public void setBrcodlen(String brcodlen) {
		this.brcodlen = brcodlen;
	}

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

	String apptype;

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String display() {
		trace("*************** Adding BIN Begins **********");
		enctrace("*************** Adding BIN Begins **********");
		HttpSession session = getRequest().getSession();
		binbean.formaction = "saveDataAddBinAction.do";
		binbean.act = getRequest().getParameter("act");
		trace("Processing..." + binbean.act);
		try 
		{
			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
			}
			
			
			/*** MAIL BLOCK ****/
			/*
			 * System.out.println( "initmail--" + initmail +" parentid :  " +
			 * this.parentid ); if( initmail ){ HttpServletRequest req =
			 * getRequest(); String menuid = comutil.getUrlMenuId( req,
			 * jdbctemplate ); if( !menuid.equals("NOREC")){ trace(
			 * "parentid--"+menuid); this.parentid = menuid; }else{
			 * this.parentid = "000"; } initmail = false; }
			 */
			/*** MAIL BLOCK ****/

			
			
			if (getRequest().getParameter("type") != null) {
				// System.out.println( "APP TYPE IS " +
				// getRequest().getParameter("type"));
				// setApptype( getRequest().getParameter("type") );
				session.setAttribute("apptype", getRequest().getParameter("type"));
			} else {
				session.setAttribute("apptype", "PREPAID");
				// setApptype("PREPAID");
			}

			String instid = (String) session.getAttribute("Instname");
			String prodname;
			String productid;
			String hsmname;
			String hsmid;
			String pinmailer_name;
			String pinmailer_id;
			String dep_type = (String) session.getAttribute("deploy_id");
			String deployid = dep_type.toUpperCase();
			// String pinmailerid = getRequest().getParameter("pinmailerid");

			/*
			 * trace("Getting service code");
			 * 
			 * 
			 * List service_code = dao.getservicecode(instid,jdbctemplate);
			 * trace("Got service_code---> "+service_code.size());
			 * if(!service_code.isEmpty() ){ dao.setServicecode(service_code);
			 * }else{ session.setAttribute("displayBinErrStat", "E");
			 * session.setAttribute("displayBinErrormessage",
			 * "No service code configured."); trace(
			 * "No service code conifigured."); return "display"; }
			 */

			List HSM_result = dao.getHSM_result(instid, jdbctemplate);
			trace("Getting hsm config details...got : " + HSM_result);

			/*
			 * if( HSM_result.isEmpty() ) { // archana trace(
			 * "HSM_result IS EMPTY"); addActionError(
			 * "No HSM Mapped with this institution."); trace(
			 * "HSM Not Configured"); return "required_home"; }
			 */
			setHsmlist(HSM_result);
			trace("Getting Pinmailerdesc...");
			List pinmailername = dao.getpinmailerdesc(instid, jdbctemplate);
			trace("Got pinmailername.. " + pinmailername);
			if (pinmailername.isEmpty()) {
				session.setAttribute("displayBinErrStat", "E");
				session.setAttribute("displayBinErrormessage", "PinMailer Configuration Not Available");
				trace("PinMailer Configuration Not Available");
				return "display";
			}
			setPinmailerlist(pinmailername);
			settingBrlen();
			session.setAttribute("displayBinErrStat", "S");

			/*
			 * trace("Getting accttypes..."); List accttypeslist =
			 * dao.getAcctTypes(instid,jdbctemplate); trace("Got accttypes.. "
			 * +accttypeslist.size()); if(pinmailername.isEmpty()) {
			 * session.setAttribute("displayBinErrStat", "E");
			 * session.setAttribute("displayBinErrormessage",
			 * "accttypes Not Available"); trace("accttypes Not Available");
			 * return "display"; } binbean.setAccttypeslist(accttypeslist);
			 */

			binbean.setPanoffset("4");
			binbean.setDecimilsationtable("0123456789012345");
			binbean.setPinlength("4");
			binbean.setPinoffsetlength("4");
			binbean.setPinpadchar("F");
			binbean.setPingentype("IBMDES");
			binbean.setCvvrequired("Y");

			binbean.setAttacheappcode("N");
			binbean.setAttachproductcode("N");
			binbean.setAttachbranchcode("N");
			binbean.setAttachaccttype("N");
			// binbean.setAttachedaccttype("S");

			binbean.binlen = commondesc.getBinLen(instid, jdbctemplate);
			trace("Getting bin length ..got : " + binbean.binlen);
			if (binbean.binlen == null) {
				addActionError("Bin Length Not configured....");
				return "required_home";
			}

			binbean.cardtypelenval = commondesc.getCardTypeLen(instid, jdbctemplate);
			trace("Getting bin length ..got : " + binbean.cardtypelenval);
			if (binbean.cardtypelenval == null) {
				addActionError("Card Type Length Not configured....");
				return "required_home";
			}

			binbean.accttypelen = commondesc.getAcctTypeLen(instid, jdbctemplate);
			trace("Getting accttype length ..got with cardtype : " + binbean.accttypelen);
			if (binbean.accttypelen == null) {
				addActionError("Acct Type Length Not configured....");
				return "required_home";
			}

			return "display";
		} catch (Exception e) {
			session.setAttribute("displayBinErrStat", "E");
			session.setAttribute("displayBinErrormessage", "Exception: Could not continue the process");
			e.printStackTrace();
			trace("Error While Getting The Resources For Page " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
			return "display";
		}
	}

	public void settingBrlen() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String BRANCHATTCHED = null, BRCODELEN = null;

		List<?> result = dao.viewbranchDetail(instid, jdbctemplate);
		Iterator<?> iterator = result.iterator();
		while (iterator.hasNext()) {
			Map<?, ?> map = (Map<?, ?>) iterator.next();
			BRANCHATTCHED = ((String) map.get("BRANCHATTCHED"));
			BRCODELEN = ((String) map.get("BRCODELEN"));
		}
		setBrcodlen(BRCODELEN);
		setBranchattached(BRANCHATTCHED);
		trace("BRCODELEN" + BRCODELEN);
		trace("BRANCHATTCHED" + BRANCHATTCHED);
	}

	public void ajax_BinLicence_Handler() throws Throwable {
		String ins = getRequest().getParameter("inst_id");
		String bin = getRequest().getParameter("bin");
		String product = getRequest().getParameter("product");

		trace("  ins  " + ins + "  bin   " + bin + "  Product " + product);
		Licensemanager licencemgr = new Licensemanager();
		String licence_check_result = licencemgr.chckBinLicence(ins, bin);
		trace("licence_check_result return " + licence_check_result);
		if (licence_check_result.equals("nofile")) {
			getResponse().getWriter().write(" No Licence File Found ");
		}
		if (licence_check_result.equals("noinst")) {
			getResponse().getWriter().write(" Institution Id Not Found In The License ");
		}
		if (licence_check_result.equals("invalid")) {
			getResponse().getWriter().write(" Bin And Product Match Not Exists In License ");
		}

		if (licence_check_result.equals("Error")) {
			getResponse().getWriter().write("Error While Checking The License ");
		}
		if (licence_check_result.equals("sucess")) {
			getResponse().getWriter().write("LICENSE MATCHED");
		}
	}

	/// pritto
	public String saveData() throws Throwable
	{
		trace("*************** Saving BIN Begins **********");
		enctrace("*************** Saving BIN Begins **********");

		System.out.println("@@@@@");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("ADDBIN", txManager);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String flag = getRequest().getParameter("act");
		trace("Processing..." + flag);
		String bankcardno = getRequest().getParameter("cardnumber");
		trace("Checking card number is valid....");

		try {
			String apptype = (String) session.getAttribute("apptype");
			String prodname = null;
			String instid = comInstId(session);
			String userid = comUserId(session);

			String username = commondesc.getUserName(instid, userid, jdbctemplate);
			System.out.println("Testing user name " + username);
			System.out.println("INSTA details" + getRequest().getParameter("instantmap1"));

			binbean.setInstantmapptype(getRequest().getParameter("instantmap1"));
			binbean.setBinvalue(getRequest().getParameter("bin"));
			binbean.setBindesc(getRequest().getParameter("bindesc"));
			binbean.setHsmid(getRequest().getParameter("hsmid"));
			binbean.setAttacheappcode(getRequest().getParameter("attach_appcode"));
			binbean.setAttachproductcode(getRequest().getParameter("attach_prodtype_cardtype"));
			binbean.setBaselength(getRequest().getParameter("baselen"));
			binbean.setAttachbranchcode(getRequest().getParameter("attach_brcode"));
			binbean.setNumberofcardsgen(getRequest().getParameter("nos_cards_gen"));
			binbean.setChnlenght(getRequest().getParameter("chnlen"));
			binbean.setPinenclength(getRequest().getParameter("deslength"));
			binbean.setPanvalidationlength(getRequest().getParameter("panvalidationlenght"));
			binbean.setPinlength(getRequest().getParameter("pinlenght"));
			binbean.setPinverificationkey1(getRequest().getParameter("pvk1"));
			binbean.setPinverificationkey2(getRequest().getParameter("pvk2"));
			binbean.setPinverificationkey(getRequest().getParameter("pvk"));
			binbean.setPinverificationkeyindex(getRequest().getParameter("pvki"));
			binbean.setPinoffsetlength(getRequest().getParameter("pinoffsetlenght"));
			binbean.setDecimilsationtable(getRequest().getParameter("decimilisation_table"));
			binbean.setCvk1(getRequest().getParameter("cvv1"));
			binbean.setCvk2(getRequest().getParameter("cvv2"));
			binbean.setPinpadchar(getRequest().getParameter("panpadchar"));
			binbean.setPingentype(getRequest().getParameter("pin_method"));
			binbean.setCvvrequired("D");
			binbean.setCvk1(getRequest().getParameter("cvv1"));
			binbean.setCvk2(getRequest().getParameter("cvv2"));
			binbean.setPinmailer(getRequest().getParameter("pinmailer"));
			binbean.setMulticurreq(getRequest().getParameter("multicurrency"));
			binbean.setPanoffset(getRequest().getParameter("panoffset"));
			binbean.setCvvlength("3");

			binbean.setServicecode(getRequest().getParameter("servicecode"));
			binbean.setMagcardorchip(getRequest().getParameter("cardtype"));
			binbean.setSecoption(getRequest().getParameter("seqoption"));
			binbean.setMaxpincnt(getRequest().getParameter("max_pincnt"));
			// binbean.setAttachaccttype(getRequest().getParameter("attach_accttype")==null?"N":getRequest().getParameter("attach_accttype")
			// );
			
			//Server side validations 
			
			if(getRequest().getParameter("bin")=="")
			{
				addActionError("BIN  CANNOT BE EMPTY");
				return 	display();
			}
		
	
			
			boolean bindesccheck=Validation.Spccharcter(getRequest().getParameter("bindesc"));
			
			System.out.println("bindesccheck" +bindesccheck);
			if(!bindesccheck)
			{
				addActionError("BIN DESC CANNOT BE EMPTY");
			return 	display();
			}
			
			if(getRequest().getParameter("chnlen") =="")
			{
				addActionError("PLEASE SELECT CHN Length");
				return 	display();
			}
			
		    if(getRequest().getParameter("servicecode")=="")
					{
						addActionError("PLEASE ENTER SERVICE CODE");
						return 	display();
					}
			
		    if(getRequest().getParameter("max_pincnt")=="")
		 			{
		 				addActionError("PLEASE ENTER MAX PIN COUNT");
		 				return 	display();
		 			}
		 		
		    
			
				
		    if(getRequest().getParameter("deslength").equals("-1"))
		{
			addActionError("PLEASE SELECT PIN ENCRYPTION KEY LENGTH");
			return 	display();
		}
	
			
		    if(getRequest().getParameter("pinoffsetlenght")=="")
		{
			addActionError("PANOFFSET CANNOT BE EMPTY");
			return 	display();
		} 
		    
		    String cardtype=getRequest().getParameter("cardtype");
		    System.out.println("cardtype "+cardtype);
		    
	    if(getRequest().getParameter("cardtype")==""||getRequest().getParameter("cardtype")==null)
		{
			addActionError(" select Card Type ...!!!");
			return 	display();
		}
		    if(getRequest().getParameter("seqoption")==""||getRequest().getParameter("seqoption")==null)
			{
				addActionError(" select Security Type ...!!!");
				return 	display();
			} 
			
		    if(getRequest().getParameter("panvalidationlenght")=="")
		{
			addActionError("PAN VALIDATION LENGHT CANNOT BE EMPTY");
			return 	display();
		}
			    
			  
			      if(getRequest().getParameter("pinlenght")=="")
		{
			addActionError("PIN LENGHT CANNOT BE EMPTY");
			return 	display();
		}
			    
				      if(getRequest().getParameter("decimilisation_table")=="")
		{
			addActionError("DECIMILISATION TABLE CANNOT BE EMPTY");
			return 	display();
		}
			      if(getRequest().getParameter("panpadchar")=="")
		{
			addActionError("PAN PAD CHARACTER CANNOT BE EMPTY");
			return 	display();
		} 
			    
			  	
				 
					
				    if(getRequest().getParameter("pinmailer").equals("-1"))
				{
					addActionError("SELECT PINMAILER");
					return 	display();
				}
			
					
				    if(getRequest().getParameter("pin_method")=="")
				{
					addActionError("pin Method CANNOT BE EMPTY");
					return 	display();
				}
					if(getRequest().getParameter("pin_method").equals("IBMDES"))	 
					{
						
						  if(getRequest().getParameter("pvk")=="")
							{
								addActionError("Enter  pvk");
								return 	display();
							}
					}
				    
				    
				    if(getRequest().getParameter("cvv1")=="")
					{
						addActionError("CVK1 CANNOT BE EMPTY");
						return 	display();
					}
				    
				    if(getRequest().getParameter("cvv2")=="")
					{
						addActionError("CVK2 CANNOT BE EMPTY");
						return 	display();
					}
			
			System.out.println("1111--->" + getRequest().getParameter("attach_accttype"));
			System.out.println("2222--->" + getRequest().getParameter("attached_accttype"));
			// System.out.println("3333--->"+getRequest().getParameter("attachaccttype"));
			// binbean.setAttachedaccttype(getRequest().getParameter("attached_accttype"));

			/*
			 * if("N".equalsIgnoreCase(getRequest().getParameter(
			 * "attach_accttype") )){ binbean.setAttachedaccttype("N"); }
			 */

			if (getRequest().getParameter("attach_appcode") == null) {
				binbean.setAttacheappcode("N");
			}

			
			
			
			
			
			
			
			
			if(binbean.getBinvalue()==null ||binbean.getBinvalue()=="")
			{
				addFieldError("bin", "Enter BIN Number11");
				return display();
			}
			/***************************************/

			// String servicecode, pinreq, maxpincnt = null;
			// servicecode = getRequest().getParameter("servicecode");
			// pinreq = getRequest().getParameter("pin_req");
			// maxpincnt = getRequest().getParameter("maxpincnt");

			/***************************************/

			int result = dao.checkBinExistTemp(instid, binbean.getBinvalue(), jdbctemplate);
			if (result > 0) {
				addActionError("Bin Value Already Configured.");
				return "required_home";
			}

			String sec_curr = "0";
			if (binbean.getMulticurreq() != null) {
				sec_curr = "1";

			}
			binbean.setSecondarycur(sec_curr);

			String panpadchar = binbean.getPinpadchar().toUpperCase();
			System.out.println("  instid :" + instid + "hsmid :" + binbean.getHsmid() + " deslength :"
					+ binbean.getPinenclength() + "  panvalidationlenght :" + binbean.getPanvalidationlength()
					+ "  pinlenght :" + binbean.getPinlength());
			System.out.println("pvk1 :" + binbean.getPinverificationkey1() + " pinoffsetlenght :"
					+ binbean.getPinoffsetlength() + "  pvk2 :" + binbean.getPinverificationkey2() + "  cvvlenght :"
					+ binbean.getCvk1() + "  decimilisation_table :" + binbean.getDecimilsationtable());
			System.out.println("  pvk " + binbean.getPinverificationkey() + " pin_method " + binbean.getPingentype()
					+ "   pvki :" + binbean.getPinverificationkeyindex() + " cvv1 :" + binbean.getCvk1()
					+ "  panoffset :" + binbean.getPinoffsetlength() + "  cvv2 :" + binbean.getCvk2() + "  panpadchar :"
					+ panpadchar + " attach_prodtype_cardtype :" + binbean.getAttachproductcode() + " attach_brcode :"
					+ binbean.getAttachbranchcode() + " baselen :" + binbean.getBaselength() + "  nos_cards_gen  "
					+ binbean.getNumberofcardsgen());
			System.out.println(" ======>cvv_req<=====" + binbean.getCvvrequired() + "    pinmailer_id           "
					+ binbean.getPinmailer());
			System.out.println(" ======>getServicecode<=====" + binbean.getServicecode()
					+ "    getMagcardorchip           " + binbean.getMagcardorchip());
			System.out.println(" ======>getSecoption<=====" + binbean.getSecoption());

			String qury, query;

			int bincount_binreltable = dao.bincount_binreltable(instid, jdbctemplate);

			int bin_licence_check = chcbin.chckBincountLicence(instid, commondesc);
			System.out.println("bin_licence_check---> " + bin_licence_check);

			if (bincount_binreltable < bin_licence_check) {
				String licence_check = chcbin.chckBinLicence(instid, binbean.getBinvalue());

				if (licence_check.equals("nofile")) {
					session.setAttribute("displayBinErrStat", "E");
					session.setAttribute("displayBinErrormessage", " Encrypted License File Not Exists");
					return "SucessAddBin";
				}
				if (licence_check.equals("noinst")) {
					session.setAttribute("displayBinErrStat", "E");
					session.setAttribute("displayBinErrormessage", " No Institution Id Found in License File");
					return "SucessAddBin";
				}
				if (licence_check.equals("invalid")) {
					session.setAttribute("displayBinErrStat", "E");
					session.setAttribute("displayBinErrormessage", " Bin And Product Match Not Exists In License ");
					return "SucessAddBin";
				}

				if (licence_check.equals("Error")) {
					session.setAttribute("displayBinErrStat", "E");
					session.setAttribute("displayBinErrormessage", " Error While Checking The License ");
					return "SucessAddBin";
				}
				if (licence_check == "sucess") {
					trace("Checking bin description exist.... " + binbean.getBindesc());
					int check = dao.checkValue(binbean.getBindesc(), instid, jdbctemplate);

					if (check >= 1) {
						session.setAttribute("displayBinErrStat", "E");
						session.setAttribute("displayBinErrormessage",
								" BIN ' " + binbean.getBinvalue() + " ' ALREADY EXISTS FOR INSTITUTION ");
						return "SucessAddBin";
					} else {
						System.out.println("get app type ___" + apptype);
						if (apptype.equals("MERCH")) {

						}

						String CVV_LENGTH = "3";
						String auth_code = "";
						String mkchkrstatus = "";
						String authmsg = "";
						;

						/*
						 * String totalbinqry =
						 * "SELECT COUNT(*) FROM PRODUCTINFO WHERE INST_ID='"
						 * +instid+"'"; int totalbin =
						 * jdbctemplate.queryForInt(totalbinqry); trace(
						 * "Getting bin"); if( totalbin == 0 ){ String propbin =
						 * commondesc.generateProperatoryBin(instid,
						 * jdbctemplate); if(flag.equals("D")){
						 * auth_code="1";mkchkrstatus="D";
						 * binbean.setAuthstatus(auth_code);
						 * binbean.setMkckstatus(mkchkrstatus); result =
						 * dao.addBinInfo(instid, userid, propbin,
						 * instid+"_FLOATBIN", "PRODUCTINFO", binbean,
						 * jdbctemplate); result = dao.addBinInfo(instid,
						 * userid, propbin, instid+"_FLOATBIN",
						 * "PRODUCTINFO", binbean, jdbctemplate); }else{
						 * auth_code="0";mkchkrstatus="M" ;
						 * binbean.setAuthstatus(auth_code);
						 * binbean.setMkckstatus(mkchkrstatus); result =
						 * dao.addBinInfo(instid, userid, propbin,
						 * instid+"_FLOATBIN", "PRODUCTINFO", binbean,
						 * jdbctemplate); }
						 * 
						 * 
						 * /*String addbin_qury=
						 * "INSERT INTO PRODUCTINFO(INST_ID,BIN,BIN_DESC,CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE,"
						 * +
						 * "BASELEN,NOS_CARDS_GEN,HSM_ID,PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE,"
						 * +
						 * "CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH,"
						 * +
						 * "EPVK,DESLENGTH,PINMAILER_ID,SEC_CUR,FLOAT_GL_CODE,CARDNO,CUSTOMER_ID,BK_NIR_GL_CODE,MB_NIR_GL_CODE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKER_DATE,ATTACH_APP_TYPE) VALUES "
						 * +
						 * "('"+instid+"','"+propbin+"','"+instid+"_FloatBin','"
						 * +chnlen+"','"+attach_prodtype_cardtype+"','"+
						 * attach_brcode+"'," +
						 * "'"+baselen+"','"+nos_cards_gen+"','"+hsmid+"','"+pvk
						 * +"','"+pinlenght+"','"+panoffset+"','"+
						 * pinoffsetlenght+"','"+panpadchar+"','"+
						 * decimilisation_table+"'," +
						 * "'"+cvv_req+"','"+CVV_LENGTH+"','"+cvk1+"','"+cvk2+
						 * "','"+pvk1+"','"+pvk2+"','"+pin_method.trim()+"','"+
						 * pvki+"','"+panvalidationlenght+"'," +
						 * "'','"+deslength+"','"+pinmailer_id+"','"+sec_curr+
						 * "','"+float_gl_code+"','"+bankcardno+"','"+customerid
						 * +"','"+bkrevenueglcode+"','"+mbrevglcode+
						 * "','1','P','"+userid+"',sysdate,'"+attach_app_code+
						 * "')";
						 * 
						 * jdbctemplate.update(addbin_qury);
						 */

						if (flag.equals("D")) {
							auth_code = "1";
							mkchkrstatus = "D";
							binbean.setAuthstatus(auth_code);
							binbean.setMkckstatus(mkchkrstatus);
							result = dao.addBinInfo(instid, userid, binbean.getBinvalue(), binbean.getBindesc(),
									"PRODUCTINFO", binbean, jdbctemplate);
							result = dao.addBinInfo(instid, userid, binbean.getBinvalue(), binbean.getBindesc(),
									"PRODUCTINFO", binbean, jdbctemplate);

							//// This is For Adding Bin in Base no table for
							//// Existing( Card Type/ Branch / Sub Product)
							trace("**** This is For Adding Bin in Base no table for Existing( Card Type/ Branch / Sub Product)************");
							String baseno_code = "";
							int countres = 0;
							List codelist = null;
							String bin = binbean.getBinvalue();
							String baselen = binbean.getBaselength();
							trace("Branch attch---" + binbean.getAttachbranchcode() + "card type-----"
									+ binbean.getAttachproductcode());

							if (binbean.getAttachbranchcode().equals("Y")) {
								trace("INSIDE BRANCH CODE Base no Insert for this bin...");
								
								/*String brcodeexist = "SELECT COUNT(BRANCH_CODE) FROM BRANCH_MASTER WHERE INST_ID='"
										+ instid + "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
								enctrace("branch code exist query ---->" + brcodeexist);
								countres = jdbctemplate.queryForInt(brcodeexist);*/
								
								
								
								//by gowtham-190819
								String brcodeexist = "SELECT COUNT(BRANCH_CODE) FROM BRANCH_MASTER WHERE INST_ID=? AND MKCK_STATUS=? AND AUTH_CODE=?";
								enctrace("branch code exist query ---->" + brcodeexist);
								countres = jdbctemplate.queryForInt(brcodeexist,new Object[]{instid,"P","1"});
								
								
								trace("branch code exist Count ---->" + countres);
								if (countres > 0) {
									
								/*	String brcodelistqry = "SELECT TRIM(BRANCH_CODE) AS BRANCH_CODE FROM BRANCH_MASTER WHERE INST_ID='"
											+ instid + "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
									codelist = jdbctemplate.queryForList(brcodelistqry);*/
									
									
									
									//BY GOWTHAM-190819
									String brcodelistqry = "SELECT TRIM(BRANCH_CODE) AS BRANCH_CODE FROM BRANCH_MASTER WHERE INST_ID=? AND MKCK_STATUS=? AND AUTH_CODE=?";
									codelist = jdbctemplate.queryForList(brcodelistqry,new Object[]{instid,"P","1"});
									
									
									trace("Branch Code ...list size----" + codelist.size());
									int insertstatus[] = new int[codelist.size()];
									String insertqry[] = new String[codelist.size()];
									ListIterator lstitr = codelist.listIterator();
									int i = 0;
									while (lstitr.hasNext()) {
										Map mp = (Map) lstitr.next();
										insertqry[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES ('"
												+ instid + "','" + mp.get("BRANCH_CODE") + "','1','" + baselen + "','"
												+ bin + "') ";
										enctrace("insert baseno query for existing branch code---- " + insertqry[i]);
										i++;
									}
									insertstatus = jdbctemplate.batchUpdate(insertqry);
									trace("batch update got...for existing branch code----" + insertstatus.length);
								}
							} else if (binbean.getAttachproductcode().equals("C")) {
								trace("INSIDE CARD TYPE Base no Insert for this bin...");
								
								/*String cardtypecodeexist = "SELECT COUNT(CARD_TYPE_ID) FROM CARD_TYPE WHERE INST_ID='"
										+ instid + "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
								enctrace("card type exist query ---->" + cardtypecodeexist);
								countres = jdbctemplate.queryForInt(cardtypecodeexist);*/
								
								
								
								//by gowtham-190819
								String cardtypecodeexist = "SELECT COUNT(CARD_TYPE_ID) FROM CARD_TYPE WHERE INST_ID=? AND MKCK_STATUS=? AND AUTH_CODE=?";
								enctrace("card type exist query ---->" + cardtypecodeexist);
								countres = jdbctemplate.queryForInt(cardtypecodeexist,new Object[]{instid,"P","1"});
								
								trace("card type id exist Count ---->" + countres);
								if (countres > 0) {
									
									/*String cardtypelistqry = "SELECT TRIM(CARD_TYPE_ID) AS CARD_TYPE_ID FROM CARD_TYPE WHERE INST_ID='"
											+ instid + "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
									codelist = jdbctemplate.queryForList(cardtypelistqry);*/
									
									//BY GOWTHAM-190819
									String cardtypelistqry = "SELECT TRIM(CARD_TYPE_ID) AS CARD_TYPE_ID FROM CARD_TYPE WHERE INST_ID=? AND MKCK_STATUS=? AND AUTH_CODE=?";
									codelist = jdbctemplate.queryForList(cardtypelistqry,new Object[]{instid,"P","1"});
									
									trace("Card type...list size----" + codelist.size());
									int insertstatus[] = new int[codelist.size()];
									String insertqry[] = new String[codelist.size()];
									ListIterator lstitr = codelist.listIterator();
									int i = 0;
									while (lstitr.hasNext()) {
										Map mp = (Map) lstitr.next();
										insertqry[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES ('"
												+ instid + "','" + mp.get("CARD_TYPE_ID") + "','1','" + baselen + "','"
												+ bin + "') ";
										enctrace("insert baseno query for existing card type---- " + insertqry[i]);
										i++;
									}
									insertstatus = jdbctemplate.batchUpdate(insertqry);
									trace("batch update got...for existing card typr----" + insertstatus.length);
								}
							} else if (binbean.getAttachproductcode().equals("P")) {
								
								trace("INSIDE SUB PRODUCT CODE Base no Insert for this bin...");
								
								
								/*String subprdcodeexist = "SELECT COUNT(SUB_PROD_ID) FROM INSTPROD_DETAILS WHERE INST_ID='"
										+ instid + "' AND AUTH_STATUS='1'";
								enctrace("card type exist query ---->" + subprdcodeexist);
								countres = jdbctemplate.queryForInt(subprdcodeexist);*/
								
								
								///by gowtham300819
								String subprdcodeexist = "SELECT COUNT(SUB_PROD_ID) FROM INSTPROD_DETAILS"
										+ " WHERE INST_ID=? AND AUTH_STATUS=?";
								enctrace("card type exist query ---->" + subprdcodeexist);
								countres = jdbctemplate.queryForInt(subprdcodeexist,new Object[]{instid,"1"});
								
							
								
								
								trace("card type id exist Count ---->" + countres);
								if (countres > 0) {
									
									/*String subprdlistqry = "SELECT TRIM(SUB_PROD_ID) AS SUB_PROD_ID FROM INSTPROD_DETAILS WHERE INST_ID='"
											+ instid + "' AND AUTH_STATUS='1'";
									codelist = jdbctemplate.queryForList(subprdlistqry);*/
									
									
									//by gowtham-190819
									String subprdlistqry = "SELECT TRIM(SUB_PROD_ID) AS SUB_PROD_ID FROM INSTPROD_DETAILS WHERE INST_ID=? AND AUTH_STATUS=?";
									codelist = jdbctemplate.queryForList(subprdlistqry,new Object[]{instid,"1"});
									
									trace("Sub Prod Code...list size----" + codelist.size());
									int insertstatus[] = new int[codelist.size()];
									String insertqry[] = new String[codelist.size()];
									ListIterator lstitr = codelist.listIterator();
									int i = 0;
									while (lstitr.hasNext()) {
										Map mp = (Map) lstitr.next();
										insertqry[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES ('"
												+ instid + "','" + mp.get("SUB_PROD_ID") + "','1','" + baselen + "','"
												+ bin + "') ";
										enctrace("insert baseno query for existing card type---- " + insertqry[i]);
										i++;
									}
									insertstatus = jdbctemplate.batchUpdate(insertqry);
									trace("batch update got...for existing sub prod----" + insertstatus.length);
								}
							} else {
								trace("NOTHING add in IFD_BSAENO condition not Matched....!!!!!");
							}

						} else {
							auth_code = "0";
							mkchkrstatus = "M";
							authmsg = "Waiting For Authorization ... ";
							binbean.setAuthstatus(auth_code);
							binbean.setMkckstatus(mkchkrstatus);
							result = dao.addBinInfo(instid, userid, binbean.getBinvalue(), binbean.getBindesc(),
									"PRODUCTINFO", binbean, jdbctemplate);
						}
						// int result = dao.addBinInfo(instid, bin, bindesc,
						// chnlen, attach_prodtype_cardtype, attach_brcode,
						// baselen, nos_cards_gen, hsmid, pvk, pinlenght,
						// panoffset, pinoffsetlenght, panpadchar,
						// decimilisation_table, cvv_req, CVV_LENGTH, cvk1,
						// cvk2, pvk1, pvk2, pin_method, pvki,
						// panvalidationlenght, deslength, pinmailer_id,
						// sec_curr, float_gl_code, bankcardno, customerid,
						// bkrevenueglcode,
						// mbrevglcode,jdbctemplate,userid,auth_code,mkchkrstatus);

						trace("got insert resutl :  " + result);
						if (result == 1) {
							if (sec_curr.equals("1")) {
								/*
								 * 
								 * String multi_currency_code[]=(getRequest().
								 * getParameterValues("curcode")); int i ; int x
								 * = 0; if (multi_currency_code != null ) { for
								 * ( i=0;i < multi_currency_code.length; i++) {
								 * int check_status=0; //String curcode =
								 * multi_currency_code[i]; //
								 * System.out.println(
								 * "selected currency code is __" + curcode );
								 * String
								 * minimum_amount=getRequest().getParameter(
								 * "minamt"+i); String
								 * maximum_amount=getRequest().getParameter(
								 * "maxamt"+i); trace(" Currency Code --->"
								 * +multi_currency_code[i]+
								 * " minimum amount====>"+minimum_amount+
								 * " maximum_amount======>"+maximum_amount);
								 * check_status = dao.ccy_inst_query(instid,
								 * binbean.getBinvalue(), multi_currency_code,
								 * i, minimum_amount,
								 * maximum_amount,jdbctemplate); if(check_status
								 * == 1){ x++; } } }
								 * 
								 * trace( "__multi_currency_code.length __" +
								 * multi_currency_code.length );
								 * 
								 */
							}

							transact.txManager.commit(transact.status);
							trace("Transaction Commited ");
							addActionMessage(
									" BIN  \"" + binbean.getBinvalue() + " \" Is Added Successfully " + authmsg);
							trace(" BIN ' " + binbean.getBinvalue() + " 'Is Added Successfully ");

							/************* AUDIT BLOCK **************/
							try {
								// added by gowtham_220719
								trace("ip address======>  " + ip);
								auditbean.setIpAdress(ip);
								
								auditbean.setActmsg("Bin [ " + binbean.getBindesc() + " - " + binbean.getBinvalue()
										+ " ] configured ");
								auditbean.setUsercode(username);
								auditbean.setAuditactcode("3040");
								auditbean.setBin(binbean.getBinvalue());
								commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
							} catch (Exception audite) {
								trace("Exception in auditran : " + audite.getMessage());
							}
							/************* AUDIT BLOCK **************/

							/*** MAIL BLOCK ****/
							try {
								String alertid = this.parentid;
								if (alertid != null && !alertid.equals("000")) {
									String keymsg = "The Bin " + binbean.getBindesc();
									int mail = comutil.sendMail(instid, alertid, keymsg, jdbctemplate, session,
											getMailSender());
									trace("mail return__" + mail);
								}
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								trace("mail commit successfully");
							}
							/*** MAIL BLOCK ****/

							return "required_home";

						} else {
							transact.txManager.rollback(transact.status);
							addActionError("Unable to configure the bin.");
							trace(" BIN Cannot Be Is Added ");
							return "SucessAddBin";
						}
					}
				}
			} else {
				transact.txManager.rollback(transact.status);
				addActionError(" You Have Attained Maximum No Of BIN For Your Institution , Please Get The Licence ");
				trace(" You Have Attained Maximum No Of BIN For Your Institution , Please Get The Licence ");
				return "SucessAddBin";
			}
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("Unable to continue the process");
			trace("Error While Add Bin " + e.getMessage());
			e.printStackTrace();
			trace("\n\n");
			enctrace("\n\n");
		}

		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public List<String> binlist;

	public List<String> getBinlist() {
		return binlist;
	}

	public void setBinlist(List<String> binlist) {
		this.binlist = binlist;
	}

	public List binlistnew;

	public List getBinlistnew() {
		return binlistnew;
	}

	public void setBinlistnew(List binlist) {
		this.binlistnew = binlistnew;
	}

	public String view() {
		List binlist;
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		String instid = i_Name.toUpperCase();
		String flag = getRequest().getParameter("act");
		System.out.println("flag --- " + flag);
		try {
			if (flag.equals("D")) {
				binlist = dao.binlist(instid, jdbctemplate);
			} else {
				binlist = dao.binlistForMaker(instid, jdbctemplate);
			}

			if ((binlist.isEmpty())) {
				// message="NO BIN TO DISPLAY";
				session.setAttribute("viewBinErrStat", "E");
				session.setAttribute("viewBinMessage", " NO BIN TO DISPLAY ");
				trace(" NO BIN TO DISPLAY ");
				return "viewbin";
			} else {
				setBinlist(binlist);
				session.setAttribute("viewBinErrStat", "S");
				trace("binlist  " + binlist);
				return "viewbin";
			}

		} catch (Exception e) {
			session.setAttribute("viewBinErrStat", "E");
			session.setAttribute("viewBinMessage", " Error While Getting the BIN Details ");
			trace(" Error While Getting the BIN Details " + e.getMessage());
		}
		return "viewbin";
	}

	private List binmulccylist;

	public List getBinmulccylist() {
		return binmulccylist;
	}

	public void setBinmulccylist(List binmulccylist) {
		this.binmulccylist = binmulccylist;
	}

	private String flg;

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	public String binDetails() {
		trace("Bin Details");
		HttpSession session = getRequest().getSession();
		binbean.doact = getRequest().getParameter("doact");
		System.out.println("bin doact : " + binbean.doact);
		try {
			String instid = (String) getRequest().getSession().getAttribute("Instname");
			String bin = getRequest().getParameter("bin");
			String flag = getRequest().getParameter("flag");
			setFlg(flag);
			 System.out.println("validation part flag "+flag);
			
			 if (flag.equals("auth") && bin.equals("-1"))
			
			 {
				
			 System.out.println("validation part bin");
			 addActionError(" Please select bin");
			 return AuthBinList(); 	
			 }
			 
		/*	 if (flag.equals("add") && bin.equals("-1"))
				 {
					 
				 System.out.println("validation part bin2");
				 addActionError(" Please select bin");
				 return view(); 	
				 }
			 */
			
			
			// String bindesc = dao.getbindesc(instid, bin, jdbctemplate);

			trace("bin==> " + bin);
			// Iterator for View BIN
			String multi_ccy = "X";

			List<?> bindetails = dao.bindetails(bin, instid, jdbctemplate);

			binbean.productcount = commondesc.getProductCountForBin(instid, bin, jdbctemplate);

			List multiccy_list;
			if (!(bindetails.isEmpty())) {
				ListIterator itr = bindetails.listIterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					String hsmid = (String) map.get("HSM_ID");
					trace("Getting hsmname...");
					String hsmname = dao.gethsmname(instid, hsmid, jdbctemplate);
					trace("Got hsmname... " + hsmname);
					if (hsmname.equals(null)) {
						session.setAttribute("preverr", "E");
						session.setAttribute("preverr", "Could not continue the process..could not get hsm name...");
						return "required_home";
					}
					String makerid = (String) map.get("MAKER_ID");
					String checkerid = (String) map.get("CHECKER_ID");
					String makername = commondesc.getUserName(instid, makerid, jdbctemplate);
					String checkername = commondesc.getUserName(instid, checkerid, jdbctemplate);
					if (checkername == null) {
						checkername = "--";
					}
					String pinmailerid = (String) map.get("PINMAILER_ID");
					trace("Got pinmailerid... " + pinmailerid);
					trace("Getting pinmailername...");
					String pinmailername = dao.getpinmailername(instid, pinmailerid, jdbctemplate);
					trace("Got pinmailername... " + pinmailername);
					if (pinmailername.equals("N")) {
						pinmailername = "NO PIN MAILER NAME";
						addActionError("Could not able to get mailer name...");
						return "required_home";
					}
					trace("pinmailername===> " + pinmailername);
					multi_ccy = (String) map.get("SEC_CUR");
					trace("multi_ccy==> " + multi_ccy);
					map.put("HSMNAME", hsmname);
					map.put("PINMAILER_NAME", pinmailername);
					map.put("MAKER_NAME", makername);
					map.put("CHECKER_NAME", checkername);
					itr.remove();
					itr.add(map);
					trace("itr==> " + map);
				}

				if (multi_ccy.equals("YES")) {

					multiccy_list = dao.multiccy_list(instid, bin, jdbctemplate);
					System.out.println("multiccy_list--- " + multiccy_list);
					if (!(multiccy_list.isEmpty())) {
						ListIterator ccyitr = multiccy_list.listIterator();
						while (ccyitr.hasNext()) {
							Map ccymap = (Map) ccyitr.next();
							String ccycode = (String) ccymap.get("CUR_CODE");
							String ccydesc = dao.getcurrencydesc(ccycode, jdbctemplate);
							trace("ccydesc==> " + ccydesc);
							ccymap.put("CURRENCY_DESC", ccydesc);
							ccyitr.remove();
							ccyitr.add(ccymap);
							trace("ccyitr===> " + ccymap);
						}
						setBinmulccylist(multiccy_list);
					} else {
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "No records found");
						return view();
					}
				}
				setLst_bindetails(bindetails);

			} else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No records found");
				return view();
			}
		} catch (Exception e) {
			trace("Exception : NO PINMAILERID FROM IFP PRODUCTBIN REL Table FOUND IN IFPINSTHSMMAP  " + e.getMessage());
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not continue the process...");
			e.printStackTrace();
		}

		if (binbean.doact != null && binbean.doact.equals("$DEL")) {
			return "bindetails_delete";
		} else if (binbean.doact != null && binbean.doact.equals("$DELAUTH")) {
			return "bindetails_deleteauth";
		}

		return "bindetails";
	}

	public String deleteBin() {
		IfpTransObj transact = commondesc.myTranObject("DELBIN", txManager);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		HttpSession session = getRequest().getSession();
		String bin = (String) getRequest().getParameter("bin");
		String instid = (String) getRequest().getSession().getAttribute("Instname");

		String userid = comUserId(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);

		try {
			int exists = dao.checkingBininproduction(instid, bin, jdbctemplate);
			if (exists == -1 || exists == -2) {
				session.setAttribute("viewBinMessage", " Error While Checking the BIN In Production ");
				return view();
			}
			if (exists == 1) {
				session.setAttribute("viewBinMessage",
						"BIN \"" + bin + "\" Is ALreay In Production It Should Not Be Deleted  ");
				return view();
			}

			dao.deleteBinFromProduction(instid, bin, jdbctemplate);
			transact.txManager.commit(transact.status);
			session.setAttribute("viewBinMessage", " BIN '" + bin + " Deleted Sucessfully ");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg("Bin [ " + bin + " Deleted ");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3040");
				auditbean.setBin(bin);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

			return view();
		} catch (Exception e) {
			session.setAttribute("viewBinMessage", "  BIN '" + bin + "' Cannot Be Deleted ");
			// trnasct.txManager.rollback( trnasct.status );
			transact.txManager.rollback(transact.status);
			trace("  BIN '" + bin + "' Cannot Be Deleted " + e.getMessage());
			return view();
		}
	}

	private List subcurrencylist;

	public List getSubcurrencylist() {
		return subcurrencylist;
	}

	public void setSubcurrencylist(List subcurrencylist) {
		this.subcurrencylist = subcurrencylist;
	}

	public void ajax_multiple_currency() throws IOException {
		HttpSession session = getRequest().getSession();
		System.out.println("currency detaill.............");
		String instid = (String) session.getAttribute("Instname");
		List<?> selectlist = dao.multicurrency_qury(instid, jdbctemplate);
		String checkbox = "";
		String ajresult = "";
		if (!(selectlist.isEmpty())) {
			int i = 0;
			Iterator ccyItr = selectlist.iterator();

			while (ccyItr.hasNext()) {

				Map ccymap = (Map) ccyItr.next();
				String ccycode = (String) ccymap.get("CUR_CODE");
				String cdesc = dao.getCurrencyDesc(ccycode, jdbctemplate);
				ajresult += ccycode + "-" + cdesc + "~";
				System.out.println(checkbox);
			}
		}
		getResponse().getWriter().write(ajresult);
	}

	public void checkBinLicence() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String bin = getRequest().getParameter("bin");
		System.out.println("instid-" + instid + "-bin" + bin);

		Licensemanager licence = new Licensemanager();
		String binlic = "norec";
		try {
			binlic = licence.chckBinAndProductLicence(instid, bin, commondesc);
			System.out.println("binlic : " + binlic);

		} catch (Throwable e) {
			e.printStackTrace();
		}

		getResponse().getWriter().write(binlic);

	}

	/*
	 * public String AuthBinList()
	 * 
	 * {
	 * 
	 * List data= dao.getDta("BIC",jdbctemplate); System.out.println("data "
	 * +data);
	 * 
	 * return ""; }
	 */

	public String authBinNew() {
		System.out.println("new check");
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		String instid = i_Name.toUpperCase();
		try {
			List<?> selectlist = dao.multicurrency_qurynew("BIC", jdbctemplate);
			if ((selectlist.isEmpty())) {
				addActionError(" NO Records found ");
				trace(" NO BIN TO DISPLAY ");
				return "required_home";
			} else {
				binlistnew = selectlist;
				trace("binlist  " + binlist);
				return "AuthBinList";
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(" Error While Getting the BIN Details ");
			trace(" Error While Getting the BIN Details " + e.getMessage());
		}
		return "AuthBinList";
	}

	public String AuthBinList() {
		List binlist1 = new ArrayList();
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		String instid = i_Name.toUpperCase();
		try {

			binlist1 = dao.binlistForChecker(instid, jdbctemplate);

			System.out.println("Getting bin values" + binlist1);
			if ((binlist1.isEmpty())) {
				addActionError(" NO Records found ");
				trace(" NO BIN TO DISPLAY ");
				return "required_home";
			} else {
				binlist = binlist1;
				trace("binlist  " + binlist);
				return "AuthBinList";
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(" Error While Getting the BIN Details ");
			trace(" Error While Getting the BIN Details " + e.getMessage());
		}
		return "AuthBinList";
	}

	public String authdeauthbinlist() {
		trace("*************bin authorization *****************");
		enctrace("*************bin authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHBIN", txManager);
		String statusmsg = "", remarks = "";

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		try {
			String instid = comInstId(session);
			String userid = comUserId(session);
			String username = commondesc.getUserName(instid, userid, jdbctemplate);
			String authstatus = "";
			String update_authdeauth_qury;
			int update_authdeauth = -1;
			String auth = getRequest().getParameter("auth");
			String bin = getRequest().getParameter("bin");
			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				authstatus = "P";
				statusmsg = " Authorized ";
				update_authdeauth = dao.updateAuthBin(authstatus, userid, instid, bin, "", jdbctemplate);
				trace("Authorizing bin..updating bin status in temp table.got : " + update_authdeauth);

				// hided for temporary table removed by ramesh

				/*
				 * int exist = dao.checkBinExist(instid, bin, jdbctemplate);
				 * trace("Checking bin already exist....got : " + exist ); if(
				 * exist == 0 ){ update_authdeauth = dao.moveToMainBin(instid,
				 * bin, jdbctemplate); trace(
				 * "Authorizing bin..moving temp table to primary table : " +
				 * update_authdeauth ); }else if ( exist > 0 ){ exist =
				 * dao.deleteBinFromProduction(instid, bin, jdbctemplate);
				 * trace("deletin existing bin....got : " + exist ); if( exist
				 * == 1 ){ update_authdeauth = dao.moveToMainBin(instid, bin,
				 * jdbctemplate); trace(
				 * "Moving updated bin to production....got : " +
				 * update_authdeauth ); } }
				 */

				//// This is For Adding Bin in Base no table for Existing( Card
				//// Type/ Branch / Sub Product)
				trace("**** This is For Adding Bin in Base no table for Existing( Card Type/ Branch / Sub Product)************");
				String baseno_code = "";
				int countres = 0;
				List codelist = null;
				// String bin = binbean.getBinvalue();
				String baselen = getRequest().getParameter("baselen");
				System.out.println("baselen--->" + baselen);
				trace("Branch attch---" + binbean.getAttachbranchcode() + "card type-----"
						+ binbean.getAttachproductcode());

				if (getRequest().getParameter("attachbranchcode").equals("Y")) {
					trace("INSIDE BRANCH CODE Base no Insert for this bin...");
					
					/*String brcodeexist = "SELECT COUNT(BRANCH_CODE) FROM BRANCH_MASTER WHERE INST_ID='" + instid
							+ "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
					enctrace("branch code exist query ---->" + brcodeexist);
					countres = jdbctemplate.queryForInt(brcodeexist);*/
					
					
					//by gowtham-170819
					String brcodeexist = "SELECT COUNT(BRANCH_CODE) FROM BRANCH_MASTER WHERE INST_ID=? AND MKCK_STATUS=? AND AUTH_CODE=?";
					enctrace("branch code exist query ---->" + brcodeexist);
					countres = jdbctemplate.queryForInt(brcodeexist,new Object[]{instid,"P","1"});
					
					trace("branch code exist Count ---->" + countres);
					if (countres > 0) {
						
						/*String brcodelistqry = "SELECT TRIM(BRANCH_CODE) AS BRANCH_CODE FROM BRANCH_MASTER WHERE INST_ID='"
								+ instid + "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
						codelist = jdbctemplate.queryForList(brcodelistqry);*/
						
						//BY GOWTHAM-170819
						String brcodelistqry = "SELECT TRIM(BRANCH_CODE) AS BRANCH_CODE FROM BRANCH_MASTER WHERE INST_ID=? AND MKCK_STATUS=?AND AUTH_CODE=?";
						codelist = jdbctemplate.queryForList(brcodelistqry,new Object[]{instid,"P","1"});
						
						trace("Branch Code ...list size----" + codelist.size());
						int insertstatus[] = new int[codelist.size()];
						String insertqry[] = new String[codelist.size()];
						ListIterator lstitr = codelist.listIterator();
						int i = 0;
						while (lstitr.hasNext()) {
							Map mp = (Map) lstitr.next();
							insertqry[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES ('"
									+ instid + "','" + mp.get("BRANCH_CODE") + "','1','" + baselen + "','" + bin
									+ "') ";
							enctrace("insert baseno query for existing branch code---- " + insertqry[i]);
							i++;
						}
						insertstatus = jdbctemplate.batchUpdate(insertqry);
						trace("batch update got...for existing branch code----" + insertstatus.length);
					}
				} else if (getRequest().getParameter("attachproductcode").equals("C")) {
					trace("INSIDE CARD TYPE Base no Insert for this bin...");
					
					
					/*String cardtypecodeexist = "SELECT COUNT(CARD_TYPE_ID) FROM CARD_TYPE WHERE INST_ID='" + instid
							+ "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
					enctrace("card type exist query ---->" + cardtypecodeexist);
					countres = jdbctemplate.queryForInt(cardtypecodeexist);*/
					
					//by gowtham-170819
					String cardtypecodeexist = "SELECT COUNT(CARD_TYPE_ID) FROM CARD_TYPE WHERE INST_ID=? AND MKCK_STATUS=? AND AUTH_CODE=?";
					enctrace("card type exist query ---->" + cardtypecodeexist);
					countres = jdbctemplate.queryForInt(cardtypecodeexist,new Object[]{instid,"P","1"});
					
					trace("card type id exist Count ---->" + countres);
					if (countres > 0) {
						
						
					/*	String cardtypelistqry = "SELECT TRIM(CARD_TYPE_ID) AS CARD_TYPE_ID FROM CARD_TYPE WHERE INST_ID='"
								+ instid + "' AND MKCK_STATUS='P' AND AUTH_CODE='1'";
						codelist = jdbctemplate.queryForList(cardtypelistqry);*/
						
						//BY GOWTHAM-170819
						String cardtypelistqry = "SELECT TRIM(CARD_TYPE_ID) AS CARD_TYPE_ID FROM CARD_TYPE WHERE INST_ID=? AND MKCK_STATUS=? AND AUTH_CODE=?";
						codelist = jdbctemplate.queryForList(cardtypelistqry,new Object[]{instid,"p","1"});
						
						
						trace("Card type...list size----" + codelist.size());
						int insertstatus[] = new int[codelist.size()];
						String insertqry[] = new String[codelist.size()];
						ListIterator lstitr = codelist.listIterator();
						int i = 0;
						while (lstitr.hasNext()) {
							Map mp = (Map) lstitr.next();
							insertqry[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES ('"
									+ instid + "','" + mp.get("CARD_TYPE_ID") + "','1','" + baselen + "','" + bin
									+ "') ";
							enctrace("insert baseno query for existing card type---- " + insertqry[i]);
							i++;
						}
						insertstatus = jdbctemplate.batchUpdate(insertqry);
						trace("batch update got...for existing card typr----" + insertstatus.length);
					}
				} else if (getRequest().getParameter("attachproductcode").equals("P")) {
					
					trace("INSIDE SUB PRODUCT CODE Base no Insert for this bin...");
					
					
					/*String subprdcodeexist = "SELECT COUNT(SUB_PROD_ID) FROM INSTPROD_DETAILS WHERE INST_ID='" + instid
							+ "' AND AUTH_STATUS='1'";
					enctrace("card type exist query ---->" + subprdcodeexist);
					countres = jdbctemplate.queryForInt(subprdcodeexist);*/
					
					
					//by gowtham-170819
					String subprdcodeexist = "SELECT COUNT(SUB_PROD_ID) FROM INSTPROD_DETAILS WHERE INST_ID=? AND AUTH_STATUS=?";
					enctrace("card type exist query ---->" + subprdcodeexist);
					countres = jdbctemplate.queryForInt(subprdcodeexist,new Object[]{instid,"1"});
					
					
					trace("card type id exist Count ---->" + countres);
					if (countres > 0) {
						
						/*String subprdlistqry = "SELECT TRIM(SUB_PROD_ID) AS SUB_PROD_ID FROM INSTPROD_DETAILS WHERE INST_ID='"
								+ instid + "' AND AUTH_STATUS='1'";
						codelist = jdbctemplate.queryForList(subprdlistqry);*/
						
						
						//by gowtham-170819
						String subprdlistqry = "SELECT TRIM(SUB_PROD_ID) AS SUB_PROD_ID FROM INSTPROD_DETAILS WHERE INST_ID=? AND AUTH_STATUS=?";
						codelist = jdbctemplate.queryForList(subprdlistqry,new Object[]{instid,"1"});
						
						trace("Sub Prod Code...list size----" + codelist.size());
						int insertstatus[] = new int[codelist.size()];
						String insertqry[] = new String[codelist.size()];
						ListIterator lstitr = codelist.listIterator();
						int i = 0;
						while (lstitr.hasNext()) {
							Map mp = (Map) lstitr.next();
							insertqry[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES ('"
									+ instid + "','" + mp.get("SUB_PROD_ID") + "','1','" + baselen + "','" + bin
									+ "') ";
							enctrace("insert baseno query for existing card type---- " + insertqry[i]);
							i++;
						}
						insertstatus = jdbctemplate.batchUpdate(insertqry);
						trace("batch update got...for existing sub prod----" + insertstatus.length);
					}
				} /*
					 * else if(getRequest().getParameter("attachaccttype").
					 * equalsIgnoreCase("Y")){ String basenoinsertqry = "",
					 * basenocode = "", basenoqry = ""; basenoqry =
					 * "SELECT ACCTTYPEID FROM ACCT_TYPES WHERE ACCT_FLAG='"
					 * +getRequest().getParameter("attachedaccttype")+"'";
					 * basenocode = (String)
					 * jdbctemplate.queryForObject(basenoqry, String.class);
					 * basenoinsertqry =
					 * "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES ('"
					 * +instid+"','"+basenocode+"','1','"+baselen+"','"+bin+
					 * "') "; jdbctemplate.update(basenoinsertqry); }
					 */

				else {
					trace("NOTHING add in IFD_BSAENO condition not Matched....!!!!!");
				}

			} else {
				authstatus = "D";
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				update_authdeauth = dao.updateDeAuthBin(authstatus, userid, remarks, instid, bin, jdbctemplate);
				trace("De-Authorizing bin...got : " + update_authdeauth);
			}

			if (update_authdeauth == 1) {
				transact.txManager.commit(transact.status);
				addActionMessage("Bin " + statusmsg + " Successfully");
				trace("Records updated and committed....");

				/************* AUDIT BLOCK **************/
				try {

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg("Bin [ " + bin + " ] " + statusmsg);
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("3040");
					auditbean.setBin(bin);
					auditbean.setRemarks(remarks);
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

				/*** MAIL BLOCK ****/
				/*
				 * try { String alertid = this.parentid; if( alertid != null &&
				 * ! alertid.equals("000")){ String keymsg = "The Bin " +
				 * binbean.getBindesc() + statusmsg ; int mail =
				 * comutil.sendMail( instid, alertid, keymsg, jdbctemplate,
				 * session, getMailSender() ); trace( "mail return__" + mail); }
				 * } catch (Exception e) { e.printStackTrace(); } finally{
				 * trace( "mail commit successfully"); }
				 */
				/*** MAIL BLOCK ****/

			} else {
				transact.txManager.rollback(transact.status);
				addActionError("Could not " + statusmsg + "");
				trace("Could not update records...got rolledback...");
			}
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Error while " + statusmsg + " the Bin " + e);
			trace("Error while deleting the Fee " + e.getMessage());
			e.printStackTrace();
		}
		return "required_home";

	}

	public String editBinList() {
		List binlist;
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		String instid = i_Name.toUpperCase();
		String usercode = comUserId(session);
		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("act", act);
		}

		try {
			binlist = dao.binlistForEdit(instid, usercode, jdbctemplate);
			if ((binlist.isEmpty())) {
				// message="NO BIN TO DISPLAY";
				addActionError(" No Records found ");
				trace(" NO BIN TO DISPLAY ");
				return "required_home";
			} else {
				setBinlist(binlist);
				trace("binlist  : " + binlist);
				return "editbinlist";
			}
		} catch (Exception e) {
			addActionError(" Error While Getting the BIN Details ");
			trace(" Error While Getting the BIN Details " + e.getMessage());
		}
		return "required_home";
	}

	public String editBinDetails() {
		trace("Bin Details");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String bin = getRequest().getParameter("bin");
		
		
		 if(bin.equals("-1"))
		 {
		 System.out.println("validation part bin");
		 addActionError(" Please select bin");
		 return editBinList(); 	
		 }
		
		 
		String act = getRequest().getParameter("act");
		binbean.formaction = "updateBinDataAddBinAction.do";

		if (act != null) {
			session.setAttribute("act", act);
		}

		try {
			List bindata = dao.getBinDetailsForEdit(instid, bin, jdbctemplate);
			trace("Getting bin details for the bin : " + bin + " .... got : " + bindata.size());
			if (bindata.isEmpty()) {
				trace("No records foound");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No bin details found for the bin [ " + bin + " ]");
				return "required_home";
			}

			@SuppressWarnings("rawtypes")
			List HSM_result = dao.getHSM_result(instid, jdbctemplate);
			if (HSM_result.isEmpty()) {
				trace("HSM_result IS EMPTY");
				session.setAttribute("displayBinErrStat", "E");
				session.setAttribute("displayBinErrormessage", " HSM Not Configured ");
				trace("HSM Not Configured");
				return "display";
			}

			setHsmlist(HSM_result);
			trace("Getting Pinmailerdesc...");
			List pinmailername = dao.getpinmailerdesc(instid, jdbctemplate);
			trace("Got pinmailername.. " + pinmailername);
			if (pinmailername.isEmpty()) {
				session.setAttribute("displayBinErrStat", "E");
				session.setAttribute("displayBinErrormessage", "PinMailer Configuration Not Available");
				trace("PinMailer Configuration Not Available");
				return "display";
			}
			setPinmailerlist(pinmailername);

			settingBrlen();

			Iterator binitr = bindata.iterator();
			while (binitr.hasNext()) {
				Map mp = (Map) binitr.next();
				binbean.setBinvalue((String) mp.get("BIN"));
				binbean.setBindesc((String) mp.get("BIN_DESC"));
				binbean.setHsmid((String) mp.get("HSM_ID"));
				binbean.setAttacheappcode((String) mp.get("ATTACH_APP_TYPE"));
				binbean.setAttachproductcode((String) mp.get("ATTACH_PRODTYPE_CARDTYPE"));
				binbean.setBaselength((String) mp.get("BASELEN"));
				binbean.setAttachbranchcode((String) mp.get("ATTACH_BRCODE"));
				binbean.setNumberofcardsgen((String) mp.get("NOS_CARDS_GEN"));
				binbean.setChnlenght((String) mp.get("CHNLEN"));
				binbean.setPinenclength((String) mp.get("DESLENGTH"));
				binbean.setPanoffset((String) mp.get("PAN_OFFSET"));
				binbean.setPanvalidationlength((String) mp.get("PANVALIDATION_LENGTH"));
				binbean.setPinlength((String) mp.get("PIN_LENGTH"));
				binbean.setPinverificationkey1((String) mp.get("PVK1"));
				binbean.setPinverificationkey2((String) mp.get("PVK2"));
				binbean.setPinverificationkey((String) mp.get("PVK"));
				binbean.setPinverificationkeyindex((String) mp.get("PVKI"));
				binbean.setPinoffsetlength((String) mp.get("PINOFFSET_LENGTH"));
				binbean.setDecimilsationtable((String) mp.get("DECIMILISATION_TABLE"));
				binbean.setCvk1((String) mp.get("CVK1"));
				binbean.setCvk2((String) mp.get("CVK2"));
				binbean.setPinpadchar((String) mp.get("PANPADCHAR"));
				binbean.setPingentype((String) mp.get("GEN_METHOD"));
				binbean.setCvvrequired((String) mp.get("CVV_REQUIRED"));
				binbean.setCvk1((String) mp.get("CVK1"));
				binbean.setCvk2((String) mp.get("CVK2"));
				binbean.setPinmailer((String) mp.get("PINMAILER_ID"));
				binbean.setMulticurreq((String) mp.get("SEC_CUR"));
				binbean.setPanoffset((String) mp.get("PAN_OFFSET"));
				binbean.setCvvlength("3");

				binbean.setServicecode((String) mp.get("SERVICE_CODE"));
				binbean.setMagcardorchip((String) mp.get("CARD_TYPE"));
				binbean.setSecoption((String) mp.get("SEQ_OPTION"));

				String authcode = (String) mp.get("AUTH_CODE");

				String remarks = (String) mp.get("REMARKS");
				String usercode = (String) mp.get("MAKER_ID");
				String userdesc = commondesc.getUserName(instid, usercode, jdbctemplate);
				binbean.setUsername(userdesc);
				binbean.setRemarks(remarks);
				if (authcode.equals("0")) {
					binbean.setMkckstatus("Waiting for authorization");
				} else if (authcode.equals("9")) {
					binbean.setMkckstatus("De-Authorized");
				}
			}

		} catch (Exception e) {
			trace("Exception : NO PINMAILERID FROM IFP PRODUCTBIN REL Table FOUND IN IFPINSTHSMMAP  " + e.getMessage());
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not continue the process...");
			e.printStackTrace();
		}

		return "display";
	}

	public String updateBinData() {

		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("UPDBIN", txManager);
		String instid = comInstId(session);
		String userid = comUserId(session);
		String act = (String) session.getAttribute("act");

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String username = commondesc.getUserName(instid, userid, jdbctemplate);

		trace("act value is : " + act);
		binbean.setBinvalue(getRequest().getParameter("bin"));
		binbean.setBindesc(getRequest().getParameter("bindesc"));
		binbean.setHsmid(getRequest().getParameter("hsmid"));
		binbean.setAttacheappcode(getRequest().getParameter("attach_appcode"));
		binbean.setAttachproductcode(getRequest().getParameter("attach_prodtype_cardtype"));
		binbean.setBaselength(getRequest().getParameter("baselen"));
		binbean.setAttachbranchcode(getRequest().getParameter("attach_brcode"));
		binbean.setNumberofcardsgen(getRequest().getParameter("nos_cards_gen"));
		binbean.setChnlenght(getRequest().getParameter("chnlen"));
		binbean.setPinenclength(getRequest().getParameter("deslength"));
		binbean.setPanvalidationlength(getRequest().getParameter("panvalidationlenght"));
		binbean.setPinlength(getRequest().getParameter("pinlenght"));
		binbean.setPinverificationkey1(getRequest().getParameter("pvk1"));
		binbean.setPinverificationkey2(getRequest().getParameter("pvk2"));
		binbean.setPinverificationkey(getRequest().getParameter("pvk"));
		binbean.setPinverificationkeyindex(getRequest().getParameter("pvki"));
		binbean.setPinoffsetlength(getRequest().getParameter("pinoffsetlenght"));
		binbean.setDecimilsationtable(getRequest().getParameter("decimilisation_table"));
		binbean.setCvk1(getRequest().getParameter("cvv1"));
		binbean.setCvk2(getRequest().getParameter("cvv2"));
		binbean.setPinpadchar(getRequest().getParameter("panpadchar"));
		binbean.setPingentype(getRequest().getParameter("pin_method"));
		binbean.setCvvrequired("N");
		binbean.setCvk1(getRequest().getParameter("cvv1"));
		binbean.setCvk2(getRequest().getParameter("cvv2"));
		binbean.setPinmailer(getRequest().getParameter("pinmailer"));
		binbean.setMulticurreq(getRequest().getParameter("multicurrency"));
		binbean.setPanoffset(getRequest().getParameter("panoffset"));
		binbean.setCvvlength("3");

		binbean.setServicecode(getRequest().getParameter("servicecode"));
		binbean.setMagcardorchip(getRequest().getParameter("cardtype"));
		binbean.setSecoption(getRequest().getParameter("seqoption"));

		
		
		//Server side validations 
		
		if(getRequest().getParameter("bin")=="")
		{
			addActionError("BIN  CANNOT BE EMPTY");
			return 	display();
		}
	

		
		boolean bindesccheck=Validation.Spccharcter(getRequest().getParameter("bindesc"));
		
		System.out.println("bindesccheck" +bindesccheck);
		if(!bindesccheck)
		{
			addActionError("BIN DESC CANNOT BE EMPTY");
		return 	display();
		}
		
		if(getRequest().getParameter("chnlen") =="")
		{
			addActionError("PLEASE SELECT CHN Length");
			return 	display();
		}
		
	    if(getRequest().getParameter("servicecode")=="")
				{
					addActionError("PLEASE ENTER SERVICE CODE");
					return 	display();
				}
		
	    if(getRequest().getParameter("max_pincnt")=="")
	 			{
	 				addActionError("PLEASE ENTER MAX PIN COUNT");
	 				return 	display();
	 			}
	 		
	    
		
			
	    if(getRequest().getParameter("deslength").equals("-1"))
	{
		addActionError("PLEASE SELECT PIN ENCRYPTION KEY LENGTH");
		return 	display();
	}

		
	    if(getRequest().getParameter("pinoffsetlenght")=="")
	{
		addActionError("PANOFFSET CANNOT BE EMPTY");
		return 	display();
	} 
	    
	    String cardtype=getRequest().getParameter("cardtype");
	    System.out.println("cardtype "+cardtype);
	    
    if(getRequest().getParameter("cardtype")==""||getRequest().getParameter("cardtype")==null)
	{
		addActionError(" select Card Type ...!!!");
		return 	display();
	}
	    if(getRequest().getParameter("seqoption")==""||getRequest().getParameter("seqoption")==null)
		{
			addActionError(" select Security Type ...!!!");
			return 	display();
		} 
		
	    if(getRequest().getParameter("panvalidationlenght")=="")
	{
		addActionError("PAN VALIDATION LENGHT CANNOT BE EMPTY");
		return 	display();
	}
		    
		  
		      if(getRequest().getParameter("pinlenght")=="")
	{
		addActionError("PIN LENGHT CANNOT BE EMPTY");
		return 	display();
	}
		    
			      if(getRequest().getParameter("decimilisation_table")=="")
	{
		addActionError("DECIMILISATION TABLE CANNOT BE EMPTY");
		return 	display();
	}
		      if(getRequest().getParameter("panpadchar")=="")
	{
		addActionError("PAN PAD CHARACTER CANNOT BE EMPTY");
		return 	display();
	} 
		    
		  	
			 
				
			    if(getRequest().getParameter("pinmailer").equals("-1"))
			{
				addActionError("SELECT PINMAILER");
				return 	display();
			}
		
				
			    if(getRequest().getParameter("pin_method")=="")
			{
				addActionError("pin Method CANNOT BE EMPTY");
				return 	display();
			}
				if(getRequest().getParameter("pin_method").equals("IBMDES"))	 
				{
					
					  if(getRequest().getParameter("pvk")=="")
						{
							addActionError("Enter  pvk");
							return 	display();
						}
				}
			    
			    
			    if(getRequest().getParameter("cvv1")=="")
				{
					addActionError("CVK1 CANNOT BE EMPTY");
					return 	display();
				}
			    
			    if(getRequest().getParameter("cvv2")=="")
				{
					addActionError("CVK2 CANNOT BE EMPTY");
					return 	display();
				}
		
		System.out.println("1111--->" + getRequest().getParameter("attach_accttype"));
		
		
		try {
			String dispmsg = "";
			if (act != null && act.equals("M")) {
				dispmsg = "Waiting for authrourization";
				binbean.setMkckstatus(act);
				binbean.setAuthstatus("0");
			}

			int updbindata = dao.updateBinRecords(instid, userid, binbean.getBinvalue(), binbean.getBindesc(),
					"PRODUCTINFO", binbean, jdbctemplate);
			trace("Updating bin data....got : " + updbindata);
			if (updbindata != 1) {
				transact.txManager.rollback(transact.status);
				trace("Could not udpate bin details....");
				addActionError("Could not update bin details...");
				return "required_home";
			}

			transact.txManager.commit(transact.status);
			trace("bin details updated succesfully....");
			addActionMessage("Bin details updated successfully...." + dispmsg);

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg("Bin [ " + binbean.getBindesc() + " ] Updated ");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3040");
				auditbean.setBin(binbean.getBinvalue());
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

			return "required_home";
		} catch (Exception e) {
			addActionError("Exception...could not update bin");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}

		return "required_home";
	}

	public String deleteBinHome() throws Exception {
		trace("Delete bin home");
		enctrace("Delete bin home");
		System.out.println("Delete bin home");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		binbean.formaction = "binDetailsAddBinAction.do";
		binbean.doact = getRequest().getParameter("doact");
		String act = getRequest().getParameter("act");
		if (act == null) {
			act = (String) session.getAttribute("act");
		}
		if (act != null) {
			session.setAttribute("act", act);
		} else {
			addActionError("Could not understand privilage level");
			return "required_home";
		}
		trace("Processing....." + act);
		try {

			List binlist = dao.binlistForEdit(instid, "", jdbctemplate);
			if (binlist.isEmpty()) {
				addActionError("No Bin Available");
				return "required_home";
			}
			binbean.setBinlist(binlist);

		} catch (Exception e) {
			addActionError("Unable to continue the process...");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return "bindel_home";
	}

	public String deleteBinAction() {
		trace("Delete bin action");
		enctrace("Delete bin action");
		IfpTransObj transact = commondesc.myTranObject("DELBIN", txManager);
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String usercode = comUserId(session);
		String bin = getRequest().getParameter("bin");
		String reason = getRequest().getParameter("reason");

		String userid = comUserId(session);
		String username = commondesc.getUserName(instid, userid, jdbctemplate);

		if (reason == null) {
			reason = "--";
		}
		int result = -1;
		String act = getRequest().getParameter("act");
		if (act == null) {
			act = (String) session.getAttribute("act");
		}
		if (act != null) {
			session.setAttribute("act", act);
		} else {
			addActionError("Could not understand privilage level");
			return "required_home";
		}
		trace("Processing....." + act);
		String authstatus = "0";
		String mkckstatus = "M";
		try {
			if (act.equals("D")) {
				authstatus = "1";
				mkckstatus = "D";
				result = dao.insertIntoBinHistory(instid, bin, jdbctemplate);
				if (result == 1) {
					result = dao.deleteBinFromProduction(instid, bin, jdbctemplate);
				}

				result = dao.deleteProduct(instid, "PRODUCT_MASTER", bin, mkckstatus, "2", authstatus, usercode, reason,
						jdbctemplate);
				trace("Updating deleted status to the product table status[ 2 ] ....got : " + result);

				addActionMessage("Deleted Successfully.");
				auditbean.setActmsg("Bin " + bin + " Deleted.");
			} else {
				addActionMessage("Deleted Successfully. Waiting for Authorization");
				auditbean.setActmsg(bin + " Deleted. Waiting for Authorization");

				result = dao.deleteProduct(instid, "PRODUCT_MASTER", bin, mkckstatus, "2M", authstatus, usercode,
						reason, jdbctemplate);
				trace("Updating deleted status to the product table status[ 2M ] ....got : " + result);
			}

			result = dao.deleteBin(instid, "PRODUCTINFO", bin, mkckstatus, authstatus, usercode, reason,
					jdbctemplate);
			trace("Updating temporary table status....got : " + result);
			txManager.commit(transact.status);
			trace("Committed....");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3040");
				auditbean.setBin(bin);
				auditbean.setRemarks(reason);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {

			txManager.rollback(transact.status);
			addActionError("Unable to continue the process...");
			trace("Exception ..got rolledback: " + e.getMessage());
			e.printStackTrace();
		}
		return "bindel_home";
	}

	public String deleteBinAuthHome() throws Exception {
		trace("Delete bin home");
		enctrace("Delete bin home");
		System.out.println("Delete bin home");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		binbean.formaction = "binDetailsAddBinAction.do";
		binbean.doact = getRequest().getParameter("doact");
		String act = getRequest().getParameter("act");
		if (act == null) {
			act = (String) session.getAttribute("act");
		}
		if (act != null) {
			session.setAttribute("act", act);
		} else {
			addActionError("Could not understand privilage level");
			return "required_home";
		}
		trace("Processing....." + act);
		try {
			List binlist = dao.binlistForDeleteAuth(instid, jdbctemplate);
			if (binlist.isEmpty()) {
				addActionError("No Bin Available for Authroization");
				return "required_home";
			}
			binbean.setBinlist(binlist);

		} catch (Exception e) {
			addActionError("Unable to continue the process...");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return "bindel_home";
	}

	public String deleteBinAuthAction() {
		trace("Delete deleteBinAuthAction");
		enctrace("deleteBinAuthAction");
		IfpTransObj transact = commondesc.myTranObject("DELBINAUTH", txManager);
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String usercode = comUserId(session);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String bin = getRequest().getParameter("bin");
		String reason = getRequest().getParameter("reason");
		int result = -1;
		String act = getRequest().getParameter("act");
		if (act == null) {
			act = (String) session.getAttribute("act");
		}
		if (act != null) {
			session.setAttribute("act", act);
		} else {
			addActionError("Could not understand privilage level");
			return "required_home";
		}
		trace("Processing....." + act);

		String mkckstatus = "M";
		try {

			result = dao.insertIntoBinHistory(instid, bin, jdbctemplate);
			trace("Moving to history...got : " + result);
			if (result == 1) {
				result = dao.deleteBinFromProduction(instid, bin, jdbctemplate);
				trace("Delete bin from production...got : " + result);
			}

			result = dao.deleteBin(instid, "PRODUCTINFO", bin, mkckstatus, "1", usercode, reason, jdbctemplate);
			trace("Updating temporary table status....got : " + result);

			result = dao.deleteProduct(instid, "PRODUCT_MASTER", bin, mkckstatus, "2", "1", usercode, reason,
					jdbctemplate);
			trace("Updating deleted status to the product table status[ 2 ] ....got : " + result);

			// added by gowtham_220719
			trace("ip address======>  " + ip);
			auditbean.setIpAdress(ip);

			auditbean.setActmsg(bin + " Bin Deleted. Approved");
			auditbean.setBin(bin);
			auditbean.setUsercode(usercode);
			auditbean.setRemarks(reason);

			txManager.commit(transact.status);
			addActionMessage(bin + " Bin Deleted Successfully....");

			trace("Committed....");

			/************* AUDIT BLOCK **************/
			try {
				
				//added by gowtham_220719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setAuditactcode("3040");
				commondesc.insertAuditTrail(instid, usercode, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {

			txManager.rollback(transact.status);
			addActionError("Unable to continue the process...");
			trace("Exception ..got rolledback: " + e.getMessage());
			e.printStackTrace();
		}
		return "bindel_home";
	}

} // end class
