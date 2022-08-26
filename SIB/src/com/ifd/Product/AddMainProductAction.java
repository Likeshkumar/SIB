package com.ifd.Product;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.instant.RequiredCheck;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import test.Date;

public class AddMainProductAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private List deployment_list;
	private List<String> prods;
	private List card_type_list;
	static Boolean initmail = true;
	static String parentid = "000";

	CommonDesc commondesc = new CommonDesc();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();

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

	RequiredCheck reqdao = new RequiredCheck();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	String doact;

	public String getDoact() {
		return doact;
	}

	public void setDoact(String doact) {
		this.doact = doact;
	}

	CommonUtil comutil = new CommonUtil();

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	AddMainProductActionDao addMainProductActionDao = new AddMainProductActionDao();
	AddMainProductActionBean productbean = new AddMainProductActionBean();

	public AddMainProductActionBean getProductbean() {
		return productbean;
	}

	public void setProductbean(AddMainProductActionBean productbean) {
		this.productbean = productbean;
	}

	public List getDeployment_list() {
		return deployment_list;
	}

	public void setDeployment_list(List deployment_list) {
		this.deployment_list = deployment_list;
	}

	public List<String> getProds() {
		return prods;
	}

	public void setProds(List<String> prods) {
		this.prods = prods;
	}

	public List getCard_type_list() {
		return card_type_list;
	}

	public void setCard_type_list(List card_type_list) {
		this.card_type_list = card_type_list;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode(HttpSession session) {
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public String cominstid(HttpSession session) {
		String username = (String) session.getAttribute("SS_USERNAME");
		System.out.println("this is user name" + username);

		return username;
	}

	List cardtypelist;
	List binlist;
	List gl_list;

	public List getGl_list() {
		return gl_list;
	}

	public void setGl_list(List gl_list) {
		this.gl_list = gl_list;
	}

	public List getBinlist() {
		return binlist;
	}

	public void setBinlist(List binlist) {
		this.binlist = binlist;
	}

	public List getCardtypelist() {
		return cardtypelist;
	}

	public void setCardtypelist(List cardtypelist) {
		this.cardtypelist = cardtypelist;
	}

	private String apptype;

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String display() {
		trace("*************** Adding Product Begins **********");
		enctrace("*************** Adding Product Begins **********");
		HttpSession session = getRequest().getSession();
		productbean.formaction = "savedataAddMainProductAction";
		try {
			String instid = comInstId(session);
			String globalcardtypeqry = addMainProductActionDao.getGlobalCardtypeqry(instid);
			enctrace(globalcardtypeqry);

			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
			}
			/*** MAIL BLOCK ****/
			/*
			 * trace( "initmail--" + initmail +" parentid :  " + this.parentid
			 * ); if( initmail ){ HttpServletRequest req = getRequest(); String
			 * menuid = comutil.getUrlMenuId( req, jdbctemplate ); if(
			 * !menuid.equals("NOREC")){ trace( "parentid--"+menuid);
			 * //session.setAttribute("URLMENUID", menuid); this.parentid =
			 * menuid; }else{ //session.setAttribute("URLMENUID", "000");
			 * this.parentid = "000"; } initmail = false; }
			 */
			/*** MAIL BLOCK ****/

			if (getRequest().getParameter("type") != null) {
				if (getRequest().getParameter("type").equals("MERCH")) {
					globalcardtypeqry = addMainProductActionDao.getGlobalCardtypeqry(instid);
				}
			}
			int reqchk = reqdao.reqProduct(instid, session, jdbctemplate);
			if (reqchk < 0) {
				return "required_home";
			}

			String aptype = getRequest().getParameter("type");
			if (aptype != null) {
				session.setAttribute("apptype", getRequest().getParameter("type"));
			}

			trace("globalcardtypeqry__" + globalcardtypeqry);

			String globalcardqry = addMainProductActionDao.getGlobalcardtpelist(instid);
			List globalcardtpelist = jdbctemplate.queryForList(globalcardtypeqry);
			if (!globalcardtpelist.isEmpty()) {
				setCardtypelist(globalcardtpelist);
			} else {
				addActionError("NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR");
				trace("NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR");
				return "required_home";
			}

			List binlist = commondesc.getListOfBins(instid, jdbctemplate);
			if (binlist.isEmpty()) {
				addActionError("No Bin Details Found. Configure the bin and try again");
				return "required_home";
			}
			setBinlist(binlist);

			/*
			 * String gl_listqry = addMainProductActionDao.getGlList(instid);
			 * List gl_list = jdbctemplate.queryForList(gl_listqry);
			 * if(!gl_list.isEmpty()){ setGl_list(gl_list); }
			 */
			String qury = addMainProductActionDao.getDeplymnt();
			List deplymnt = jdbctemplate.queryForList(qury);
			trace("deplymnt==> " + deplymnt.size());
			try {
				setDeployment_list(deplymnt);
			} catch (Exception e) {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " Error While getting Deployment Details ");
				trace("Error While getting Deployment Details " + e.getMessage());
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While getting Deployment Details ");
			trace("Error While getting Deployment Details " + e.getMessage());
		}
		trace("\n\n");
		enctrace("\n\n");
		return "sucess";
	}

	public String displayConfig() {
		trace("*************** Adding Product Begins **********");
		enctrace("*************** Adding Product Begins **********");

		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String globalcardtypeqry = addMainProductActionDao.getGlobalCardtypeqry(instid);
		enctrace(globalcardtypeqry);

		try {
			/*** MAIL BLOCK ****/
			trace("initmail--" + initmail + " parentid :  " + this.parentid);
			if (initmail) {
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId(req, jdbctemplate);
				if (!menuid.equals("NOREC")) {
					trace("parentid--" + menuid);
					// session.setAttribute("URLMENUID", menuid);
					this.parentid = menuid;
				} else {
					// session.setAttribute("URLMENUID", "000");
					this.parentid = "000";
				}
				initmail = false;
			}
			/*** MAIL BLOCK ****/

			if (getRequest().getParameter("type") != null) {
				if (getRequest().getParameter("type").equals("MERCH")) {
					globalcardtypeqry = addMainProductActionDao.getGlobalCardtypeqry(instid);
				}
			}
			int reqchk = reqdao.reqProduct(instid, session, jdbctemplate);
			if (reqchk < 0) {
				return "required_home";
			}

			String aptype = getRequest().getParameter("type");
			if (aptype != null) {
				session.setAttribute("apptype", getRequest().getParameter("type"));
			}

			trace("globalcardtypeqry__" + globalcardtypeqry);

			String globalcardqry = addMainProductActionDao.getGlobalcardtpelist(instid);

			/*
			 * List globalcardtpelist =
			 * jdbctemplate.queryForList(globalcardtypeqry);
			 */
			List globalcardtpelist = jdbctemplate.queryForList(globalcardtypeqry, new Object[] { instid, "1" });

			if (!globalcardtpelist.isEmpty()) {
				setCardtypelist(globalcardtpelist);
			} else {
				addActionError("NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR");
				trace("NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR");
				return "required_home";
			}

			List binlist = commondesc.getListOfBins(instid, jdbctemplate);
			if (!binlist.isEmpty()) {
				setBinlist(binlist);
			}

			String qury = addMainProductActionDao.getDeplymnt();
			List deplymnt = jdbctemplate.queryForList(qury);
			trace("deplymnt==> " + deplymnt.size());

			setDeployment_list(deplymnt);
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Unable to continue the process");
			trace("Error While getting Deployment Details " + e.getMessage());
		}
		session.setAttribute("curerr", "S");
		trace("\n\n");
		enctrace("\n\n");
		return "sucess";
	}

	public String savedata() {
		trace("*************** Saving product Begins **********");
		enctrace("*************** Saving product Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String productid = (getRequest().getParameter("globcardtype"));
		String productdesc = (getRequest().getParameter("cardtypedesc"));
		String prodsubtype = (getRequest().getParameter("prodsubtype"));
		String bin = (getRequest().getParameter("bin"));
		String glcode = (getRequest().getParameter("glcode"));

		System.out.println("productid " + productid);
		productbean.setCardtypeid(productid);
		productbean.setBin(bin);
		productbean.setProductname(productdesc);

		if (productid.equals("-1")) {
			System.out.println("validation part productid");
			addFieldError("globcardtype", " Please select product Name");
			return display();
		}

		if (bin.equals("-1")) {
			System.out.println("validation part bin");
			addFieldError("bin", " Please select bin");
			return display();
		}

		if (productdesc == "" || productdesc == null) {
			System.out.println("validation part cardtypedesc");
			addFieldError("cardtypedesc", " Please select product name");
			return display();
		}

		if (prodsubtype == "" || prodsubtype == null) {
			System.out.println("validation part product name");
			addActionError(" Please select product name ");
			return display();
		}

		if (glcode == "") {
			System.out.println("validation part branchcode");
			addActionError(" Please select branch");
			return display();
		}

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		String mkckrstatus = "";
		Date date = new Date();

		String userid = comUserCode(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		String flag = getRequest().getParameter("act");
		if (flag == null) {
			flag = (String) session.getAttribute("act");
		}
		String authmsg = "";
		String auth_code = "1";
		String mkchkrstatus = "D";
		IfpTransObj trnasct = commondesc.myTranObject("ADDPROD", txManager);
		// doact = getRequest().getParameter("doact");
		trace("doact : " + doact);
		String appcodeval = "";
		try {

			String appcode = commondesc.getAppTypeValue(instid, jdbctemplate);
			trace("Got Appcode : " + appcode);
			if (appcode != null) {
				appcodeval = appcode;
			}
			String prodseq = new String(bin) + appcodeval + new String(productid);
			trace("Product code is :  " + prodseq);

			if (doact != null && doact.equals("EDIT")) {
				int x = addMainProductActionDao.deleteProductFromTable(instid, "PRODUCT_MASTER", prodseq, jdbctemplate);
				trace("Deleting from temp table....got : " + x);
			}

			if (flag.equals("D")) {
				auth_code = "1";
				mkchkrstatus = "D";
			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = ". Waiting for Authorization";
			}
			if (addMainProductActionDao.checkProductExist(instid, prodseq, jdbctemplate) != 0) {
				addActionError(
						" PRODUCT ALREADY CONFIGURED WITH THE  GIVEN CARD TYPE AND BIN. CONFIGURE WITH DIFFERENT COMBINATION ");
				trace("PRODUCT ALREADY CONFIGURED WITH THE  GIVEN CARD TYPE AND BIN. CONFIGURE WITH DIFFERENT COMBINATION ");
				return this.displayConfig();
			}

			trace("Getting Bin Mapping..");
			List mappinglist = addMainProductActionDao.fchBinMapping(instid, jdbctemplate);
			trace("Got Bin Mapping..");
			Iterator itr = mappinglist.iterator();
			String binmap = null, cardtypemap = null;
			while (itr.hasNext()) {
				Map mpl = (Map) itr.next();
				binmap = (String) mpl.get("BIN");
				cardtypemap = (String) mpl.get("CARD_TYPE");
			}

			if (binmap.equals("1")) {
				trace("Checking bin exsist");
				int bincnt = addMainProductActionDao.checkBinExist(instid, bin, jdbctemplate);
				if (bincnt != 0) {
					addActionError("COULD NOT CONFIGURE. BIN ALREADY EXIST");
					trace("COULD NOT CONFIGURE. BIN ALREADY EXIST");
					return this.displayConfig();
				}

			}

			if (cardtypemap.equals("1")) {
				int cardtypecnt = addMainProductActionDao.checkCardtypeExist(instid, productid, jdbctemplate);
				if (cardtypecnt != 0) {
					addActionError("COULD NOT CONFIGURE. CARD TYPE ALREADY EXIST");
					trace("COULD NOT CONFIGURE. CARD TYPE ALREADY EXIST");
					return this.displayConfig();
				}
			}

			String columname = productid + "_CHN_BASE_NO";
			trace("columname====> " + columname);
			String field_qury = addMainProductActionDao.getFieldQury(columname);
			String addfeild_qury = addMainProductActionDao.getAddfeild_qury(columname);
			enctrace("field_qury===>" + field_qury);
			enctrace("addfeild_qury===>" + addfeild_qury);

			String productquery = addMainProductActionDao.getProductQuery(instid, prodseq, productid, productdesc,
					prodsubtype, bin, usercode, glcode, auth_code, mkchkrstatus);
			enctrace("productquery :  " + productquery);

			/*
			 * int insertstatus = commondesc.executeTransaction(productquery,
			 * jdbctemplate);
			 */

			int insertstatus = jdbctemplate.update(productquery);;

			trace("insertstatus__" + insertstatus);
			if (insertstatus < 0) {

				txManager.rollback(trnasct.status);
				trace("Rollbacked success 1");
				addActionError("Product Add Failed ");
				trace("Product Add Failed");
				return this.displayConfig();
			}
			int filedcount = jdbctemplate.queryForInt(field_qury);
			if (filedcount == 0) {
				jdbctemplate.execute(addfeild_qury);
			}

			if (doact != null && doact.equals("EDIT")) {
				addActionMessage("Product \"" + productdesc + "\" Updated Successfully. " + authmsg);
				auditbean.setActmsg("Product [ " + productdesc + " ] Updated. " + authmsg);
				// return "required_home";
			} else {
				addActionMessage("Product \"" + productdesc + "\" Added Successfully. " + authmsg);
				auditbean.setActmsg("Product  [ " + productdesc + " ] Added");
			}

			txManager.commit(trnasct.status);
			trace("+++++ Data inserted in db +++++");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3050");

				// added by gowtham_010819
				String pcode = null;
				auditbean.setProduct(pcode);

				auditbean.setBin(bin);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			txManager.rollback(trnasct.status);
			addActionError("Could not add product . ");
			trace("Could not add product . " + e.getMessage());
			e.printStackTrace();
		}

		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String viewProduct() {
		trace("*************** view product Begins **********");
		enctrace("*************** view product Begins **********");

		List result;
		String productname;
		String productid;
		HttpSession session = getRequest().getSession();
		productbean.formaction = "proddetailsAddMainProductAction.do";
		try {
			String instid = comInstId(session);
			String flag = getRequest().getParameter("act");
			String mappingqry = null;
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
				if (flag.equals("D")) {
					mappingqry = addMainProductActionDao.getProductForView(instid, "$VIEW");
				} else if (flag.equals("C")) {
					mappingqry = addMainProductActionDao.getProductForChecker(instid);
				}
			}
			enctrace("qury : " + mappingqry);
			List qury = jdbctemplate.queryForList(mappingqry);
			trace("Getting product list : got : " + qury);

			if (!qury.isEmpty()) {
				setCard_type_list(qury);
			} else {
				addActionError("No records found");
				return "required_home";
			}
			return "viewmainproduct";
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While getting Card Type Details ");
			trace("EXCEPTION :Error While getting Card Type Details " + e.getMessage());
			return "viewmainproduct";
		}
	}

	private List prod_detail;

	public List getProd_detail() {
		return prod_detail;
	}

	public void setProd_detail(List prod_detail) {
		this.prod_detail = prod_detail;
	}

	private char flag;

	public char getFlag() {
		return flag;
	}

	public void setFlag(char flag) {
		this.flag = flag;
	}

	public String proddetails() {
		trace("*************** Next Page of View Begins **********");
		enctrace("*************** Next Page of View Begins **********");

		HttpSession session = getRequest().getSession();

		String inst_Name = (String) session.getAttribute("Instname");
		String product_code = getRequest().getParameter("cardtypeid");
		trace("product_code==> " + product_code);
		String cardtypeid = getRequest().getParameter("cardtypeid");
		trace("cardtypeid==> " + cardtypeid);
		try {
			String act = (String) session.getAttribute("act");
			System.out.println("act --- " + act);
			char a = act.charAt(0);
			setFlag(a);

			String prod_det_query = addMainProductActionDao.getProductdetails(inst_Name, product_code);
			/*
			 * List prod_det_result = jdbctemplate.queryForList(prod_det_query);
			 */
			List prod_det_result = jdbctemplate.queryForList(prod_det_query, new Object[] { inst_Name, product_code });

			trace("QUERY generateprodlist " + prod_det_result);
			trace("RESULT " + prod_det_result.size());
			if (!(prod_det_result.isEmpty())) {
				ListIterator itr = prod_det_result.listIterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					String bin = (String) map.get("BIN");
					String glcode = (String) map.get("GL_CODE");
					String configby = (String) map.get("CONFIGURED_BY");
					String username = commondesc.getUserName(inst_Name, configby, jdbctemplate);
					trace("bin---> " + bin);
					String bindesc = addMainProductActionDao.getbindesc(inst_Name, bin, jdbctemplate);
					trace("bindesc---> " + bindesc);
					String gldesc = "";// addMainProductActionDao.getgldesc(inst_Name,glcode,jdbctemplate);
					trace("gldesc---> " + gldesc);
					map.put("BIN_DESC", bindesc);
					map.put("GL_NAME", gldesc);
					map.put("CONFIG_BY", username);
					itr.remove();
					itr.add(map);
					trace("itr---> " + map);
				}
				setProd_detail(prod_det_result);
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " CONFIGURE GLCODE IN GL MASTER. ");
			trace("EXCEPTION : CONFIGURE GLCODE IN GL MASTER. " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "proddetails";
	}

	public String authDeauthProduct() {
		trace("*************product authorization *****************");
		enctrace("*************product authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTH", txManager);
		String statusmsg = "";

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();

		int result = -1;
		try {
			String instid = comInstId(session);
			String userid = comUserCode(session);
			String username = commondesc.getUserName(instid, userid, jdbctemplate);

			System.out.println("Testing user name " + username);

			String authstatus = "", remarks = "";
			String update_authdeauth_qury;
			String auth = getRequest().getParameter("auth");
			String cardtype = getRequest().getParameter("cardtype");
			String prodcode = getRequest().getParameter("prodcode");
			if (auth.equals("Authorize")) {
				trace("AUTHORIZE...........");
				authstatus = "P";
				statusmsg = " Authorized ";

				/*
				 * update_authdeauth_qury
				 * =addMainProductActionDao.updateAuthProduct(authstatus,
				 * userid,instid, cardtype,prodcode); result =
				 * jdbctemplate.update(update_authdeauth_qury); trace(
				 * "Updating authorized status...got : " + result );
				 * 
				 * result =
				 * addMainProductActionDao.deleteProductFromTable(instid,
				 * "PRODUCT_MASTER", prodcode, jdbctemplate); trace(
				 * "Deleting existing proudct from table...got :  " + result );
				 * result =
				 * addMainProductActionDao.moveProductToProduction(instid,
				 * prodcode, jdbctemplate); trace(
				 * "Moving to production....got : " + result ); }else{
				 * authstatus = "D"; statusmsg = " De-Authorized "; remarks =
				 * getRequest().getParameter("reason"); update_authdeauth_qury
				 * =addMainProductActionDao.updateDeAuthProduct(authstatus,
				 * userid, remarks, instid, cardtype,prodcode); result =
				 * jdbctemplate.update(update_authdeauth_qury); trace(
				 * "Updating de-authorized status...got : " + result ); }
				 */

				update_authdeauth_qury = addMainProductActionDao.updateAuthProduct(authstatus, userid, instid, cardtype,
						prodcode);
				/* result = jdbctemplate.update(update_authdeauth_qury); */

				result = jdbctemplate.update(update_authdeauth_qury,
						new Object[] { authstatus, "1", date.getCurrentDate(), userid, instid, cardtype, prodcode });

				trace("Updating authorized status...got : " + result);

				/*
				 * result =
				 * addMainProductActionDao.deleteProductFromTable(instid,
				 * "PRODUCT_MASTER", prodcode, jdbctemplate); trace(
				 * "Deleting existing proudct from table...got :  " + result );
				 * result =
				 * addMainProductActionDao.moveProductToProduction(instid,
				 * prodcode, jdbctemplate); trace(
				 * "Moving to production....got : " + result );
				 */
			} else {
				authstatus = "D";
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				update_authdeauth_qury = addMainProductActionDao.updateDeAuthProduct(authstatus, userid, remarks,
						instid, cardtype, prodcode);

				// result = jdbctemplate.update(update_authdeauth_qury);
				result = jdbctemplate.update(update_authdeauth_qury, new Object[] { authstatus, "9",
						date.getCurrentDate(), userid, remarks, instid, cardtype, prodcode });
				trace("Updating de-authorized status...got : " + result);
			}

			trace("Final result : " + result);
			if (result < 0) {
				txManager.rollback(transact.status);
				addActionError("Unable to continue the process");
				return "required_home";
			}

			addActionMessage("Product " + statusmsg + " Sucessfully");
			txManager.commit(transact.status);

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				String productname = commondesc.getProductDesc(instid, prodcode, jdbctemplate);
				auditbean.setActmsg("Product [ " + productname + " ] " + statusmsg);
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3050");

				// added by gowtham_010819
				String pcode = null;
				auditbean.setProduct(pcode);

				// auditbean.setBin(cardtype);
				auditbean.setRemarks(remarks);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);

			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError(" Unable to continue the process");
			trace("Error while deleting the Fee " + e.getMessage());
			e.printStackTrace();
		}
		return "required_home";
	}

	public String deleteProduct() {
		trace("*************** delete product Begins **********");
		enctrace("*************** delete product Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		productbean.formaction = "deleteProductViewAddMainProductAction.do";
		productbean.act = getRequest().getParameter("act");
		productbean.doact = getRequest().getParameter("doact");
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
		trace("Processing....." + act + " and doact :" + productbean.doact);

		session.setAttribute("act", productbean.act);
		String prodlistqry = "";
		if (productbean.doact == null) {
			addActionError("No Action identified.");
			return "required_home";
		}
		try {

			prodlistqry = addMainProductActionDao.getProductForView(instid, "$DEL");

			List prodlist = jdbctemplate.queryForList(prodlistqry);
			if (prodlist.isEmpty()) {
				addActionError("No Product Available for Delete");
				return "required_home";
			}
			setCard_type_list(prodlist);

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While getting Card Type Details ");
			trace("EXCEPTION :Error While getting Card Type Details " + e.getMessage());

		}

		return "viewmainproduct_delete";
	}

	public List getProductDetailsForView(String inst_Name, String product_code, JdbcTemplate jdbctemplate)
			throws Exception {
		String prod_det_query = addMainProductActionDao.getProductdetails(inst_Name, product_code);
		List prod_det_result = jdbctemplate.queryForList(prod_det_query, new Object[] { inst_Name, product_code });
		/* List prod_det_result = jdbctemplate.queryForList(prod_det_query); */
		trace("QUERY generateprodlist " + prod_det_result);
		trace("RESULT " + prod_det_result.size());
		if (!(prod_det_result.isEmpty())) {
			ListIterator itr = prod_det_result.listIterator();
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				String bin = (String) map.get("BIN");
				String glcode = (String) map.get("GL_CODE");
				String configby = (String) map.get("CONFIGURED_BY");
				String username = commondesc.getUserName(inst_Name, configby, jdbctemplate);
				trace("bin---> " + bin);
				String bindesc = addMainProductActionDao.getbindesc(inst_Name, bin, jdbctemplate);
				trace("bindesc---> " + bindesc);
				String gldesc = "";// addMainProductActionDao.getgldesc(inst_Name,glcode,jdbctemplate);
				trace("gldesc---> " + gldesc);
				map.put("BIN_DESC", bindesc);
				map.put("GL_NAME", gldesc);
				map.put("CONFIG_BY", username);
				itr.remove();
				itr.add(map);
				trace("itr---> " + map);
			}
		}
		return prod_det_result;
	}

	public String deleteProductView() {
		trace("*************** Next Page of View Begins **********");
		enctrace("*************** Next Page of View Begins **********");
		productbean.formaction = "#.do";
		HttpSession session = getRequest().getSession();
		productbean.doact = getRequest().getParameter("doact");
		String inst_Name = (String) session.getAttribute("Instname");
		String product_code = getRequest().getParameter("cardtypeid");
		trace("product_code==> " + product_code);
		String cardtypeid = getRequest().getParameter("cardtypeid");
		trace("cardtypeid==> " + cardtypeid);
		try {
			String act = (String) session.getAttribute("act");
			System.out.println("act --- " + act);
			char a = act.charAt(0);
			setFlag(a);

			List prod_det_result = this.getProductDetailsForView(inst_Name, product_code, jdbctemplate);
			setProd_detail(prod_det_result);

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " CONFIGURE GLCODE IN GL MASTER. ");
			trace("Exception...Unable to continue the process" + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "proddetails_delete";
	}

	public String deleteProductAction() {
		trace("Delete bin action");
		enctrace("Delete bin action");
		IfpTransObj transact = commondesc.myTranObject("DELBIN", txManager);
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String productcode = getRequest().getParameter("productcode");
		String reason = getRequest().getParameter("reason");
		int result = -1;

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

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

		String userid = comUserCode(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		try {
			if (act.equals("D")) {
				mkckstatus = "D";
				authstatus = "1";
				result = addMainProductActionDao.deleteProductFromTable(instid, "PRODUCT_MASTER", productcode,
						jdbctemplate);
				trace("Deleting from main table...got : " + result);
				addActionMessage("Product Deleted Successfully.");
				auditbean.setActmsg("Product Deleted.");
			} else {
				addActionMessage("Product Deleted Successfully. Waiting for Authourization");
				auditbean.setActmsg("Product Deleted. Waiting for Authourization");
			}

			result = addMainProductActionDao.deleteProductFromTemp(instid, productcode, usercode, authstatus,
					mkckstatus, reason, jdbctemplate);
			trace("Deleting from temp table...got : " + result);

			txManager.commit(transact.status);
			trace("Committed....");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3050");

				// added by gowtham_010819
				String pcode = null;
				auditbean.setProduct(pcode);

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
		return "required_home";
	}

	public String deleteProductAuth() {
		trace("*************** delete product auth Begins **********");
		enctrace("*************** delete product auth Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		productbean.formaction = "deleteProducAuthtViewAddMainProductAction.do";
		productbean.act = getRequest().getParameter("act");
		productbean.doact = getRequest().getParameter("doact");
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
		trace("Processing....." + act + " and doact :" + productbean.doact);
		session.setAttribute("act", productbean.act);
		String prodlistqry = "";
		if (productbean.doact == null) {
			addActionError("No Action identified.");
			return "required_home";
		}
		try {
			prodlistqry = addMainProductActionDao.getProductForView(instid, "$DELAUTH");
			List prodlist = jdbctemplate.queryForList(prodlistqry);
			if (prodlist.isEmpty()) {
				addActionError("No Product Available for Delete");
				return "required_home";
			}
			setCard_type_list(prodlist);
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While getting Card Type Details ");
			trace("EXCEPTION :Error While getting Card Type Details " + e.getMessage());

		}

		return "viewmainproduct_deleteauth";
	}

	public String deleteProducAuthtView() {
		trace("*************** Next Page of View Begins **********");
		enctrace("*************** Next Page of View Begins **********");
		productbean.formaction = "#.do";
		HttpSession session = getRequest().getSession();
		productbean.doact = getRequest().getParameter("doact");
		String inst_Name = (String) session.getAttribute("Instname");
		String product_code = getRequest().getParameter("cardtypeid");
		trace("product_code==> " + product_code);
		String cardtypeid = getRequest().getParameter("cardtypeid");
		trace("cardtypeid==> " + cardtypeid);
		try {
			List prod_det_result = this.getProductDetailsForView(inst_Name, product_code, jdbctemplate);
			setProd_detail(prod_det_result);
		} catch (Exception e) {
			addActionError("Unable to continue the process.");
			trace("Exception...Unable to continue the process" + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "proddetails_delete";
	}

	public String deleteProductAuthAction() {
		IfpTransObj transact = commondesc.myTranObject("DELAUTH", txManager);
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String productcode = getRequest().getParameter("productcode");
		String doact = getRequest().getParameter("doact");
		int result = -1;

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String userid = comUserCode(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		String authmsg = "";
		if (doact == null) {
			addActionError("Unable to identify the action");
			return "required_home";
		}
		try {
			if (doact.equals("AUTH")) {
				result = addMainProductActionDao.deleteProductFromTable(instid, "PRODUCT_MASTER", productcode,
						jdbctemplate);
				trace("Deleting product from main table....got : " + result);
				result = addMainProductActionDao.deleteAuthStatusProductFromTemp(instid, productcode, "2", usercode,
						"1", jdbctemplate);
				trace("Updating deleted status into temp ...got : " + result);
				authmsg = "Authorized";
				auditbean.setActmsg("Product Deleted got Rejected");
			} else if (doact.equals("DEAUTH")) {
				result = addMainProductActionDao.deleteAuthStatusProductFromTemp(instid, productcode, "0", usercode,
						"1", jdbctemplate);
				trace("Updating de auth  status into temp ...got : " + result);
				authmsg = "De-Authorized";
				auditbean.setActmsg("Product Deleted got Approved");
			}
			addActionMessage(authmsg + " Successfully.");
			transact.txManager.commit(transact.status);
			trace("Committed");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3050");

				// added by gowtham_010819
				String pcode = null;
				auditbean.setProduct(pcode);

				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("Unable to continue the process...");
			trace("EXCEPTION :Error While getting Card Type Details..got rolledback " + e.getMessage());
			e.printStackTrace();
		}
		return "required_home";
	}

	public String editProductHome() {
		trace("*************** delete product Begins **********");
		enctrace("*************** delete product Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		productbean.formaction = "editProductViewAddMainProductAction.do";
		productbean.act = getRequest().getParameter("act");
		productbean.doact = "$EDIT";
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
		trace("Processing....." + act + " and doact :" + productbean.doact);

		session.setAttribute("act", productbean.act);
		String prodlistqry = "";
		if (productbean.doact == null) {
			addActionError("No Action identified.");
			return "required_home";
		}
		try {
			prodlistqry = addMainProductActionDao.getProductForView(instid, "$EDIT");
			List prodlist = jdbctemplate.queryForList(prodlistqry);
			if (prodlist.isEmpty()) {
				addActionError("No Product Available");
				return "required_home";
			}
			setCard_type_list(prodlist);
		} catch (Exception e) {
			addActionError("Unable to continue the process");
			trace("EXCEPTION :Error While getting Card Type Details " + e.getMessage());

		}
		return "viewmainproduct_edit";
	}

	public String editProductView() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String productcode = getRequest().getParameter("cardtypeid");
		String inst_Name = "";
		productbean.formaction = "editProductActionAddMainProductAction.do";
		try {
			String proddetailsqry = addMainProductActionDao.getProductdetails(instid, productcode);
			/*
			 * List productdetails = jdbctemplate.queryForList(proddetailsqry);
			 */
			List productdetails = jdbctemplate.queryForList(proddetailsqry, new Object[] { inst_Name, productcode });
			if (!productdetails.isEmpty()) {
				Iterator itr = productdetails.iterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					productbean.productname = (String) mp.get("CARD_TYPE_NAME");
					productbean.bin = (String) mp.get("BIN");
					productbean.cardtypeid = (String) mp.get("CARD_TYPE_ID");
				}
			}

			List binlist = commondesc.getListOfBins(instid, jdbctemplate);
			if (binlist.isEmpty()) {
				addActionError("No Bin Details Found. Configure the bin and try again");
				return "required_home";
			}
			setBinlist(binlist);

			String qury = addMainProductActionDao.getDeplymnt();
			List deplymnt = jdbctemplate.queryForList(qury);
			trace("deplymnt :" + deplymnt);
			setDeployment_list(deplymnt);

			String globalcardqry = addMainProductActionDao.getGlobalcardtpelist(instid);
			/*
			 * List globalcardtpelist =
			 * jdbctemplate.queryForList(globalcardqry);
			 */

			List globalcardtpelist = jdbctemplate.queryForList(globalcardqry, new Object[] { instid, "1" });
			if (!globalcardtpelist.isEmpty()) {
				setCardtypelist(globalcardtpelist);
			} else {
				addActionError("NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR");
				trace("NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Unable to continue the process");
			trace("EXCEPTION :Unable to process " + e.getMessage());
		}
		return "edit_view";
	}

	public String editProductAction() {
		trace("Edit Product Action method initiated.");
		return "edit_data";
	}

}
