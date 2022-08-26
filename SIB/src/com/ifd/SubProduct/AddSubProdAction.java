package com.ifd.SubProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifd.SubProduct.AddSubProdActionBean;
import com.ifp.beans.AuditBeans;
import com.ifd.SubProduct.AddSubProdActionDao;
import com.ifd.beans.PersonalizeBean;
import com.ifp.dao.GLConfigureDAO;
import com.ifp.instant.RequiredCheck;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import test.Date;
import test.Validation;

import com.ifg.Config.Licence.Licensemanager;

public class AddSubProdAction extends BaseAction implements ServletResponseAware {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());
	public String subprodid;

	Licensemanager licensemanager = new Licensemanager();

	PersonalizeBean bean = new PersonalizeBean();

	public PersonalizeBean getBean() {
		return bean;
	}

	public void setBean(PersonalizeBean bean) {
		this.bean = bean;
	}

	private static Boolean initmail = true;
	private static String parentid = "000";

	AddSubProdActionDao addSubProdActionDao = new AddSubProdActionDao();
	RequiredCheck reqdao = new RequiredCheck();
	CommonUtil comutil = new CommonUtil();
	CommonDesc commondesc = new CommonDesc();
	AddSubProdActionBean subproductbean = new AddSubProdActionBean();
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public AddSubProdActionBean getSubproductbean() {
		return subproductbean;
	}

	public void setSubproductbean(AddSubProdActionBean subproductbean) {
		this.subproductbean = subproductbean;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public String getSubprodid() {
		return subprodid;
	}

	public void setSubprodid(String subprodid) {
		this.subprodid = subprodid;
	}

	private JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CommonUtil comUtil() {
		CommonUtil comutil = new CommonUtil();
		return comutil;
	}

	public String statusflag;

	public String getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(String statusflag) {
		this.statusflag = statusflag;
	}

	public String actflag;

	public String getActflag() {
		return actflag;
	}

	public void setActflag(String actflag) {
		this.actflag = actflag;
	}

	List gllistbean;
	List glkeyslist;
	List subgllist;
	List mappedgl_list;
	Boolean ismappedavailable = true;

	public List getMappedgl_list() {
		return mappedgl_list;
	}

	public void setMappedgl_list(List mappedgl_list) {
		this.mappedgl_list = mappedgl_list;
	}

	public Boolean getIsmappedavailable() {
		return ismappedavailable;
	}

	public void setIsmappedavailable(Boolean ismappedavailable) {
		this.ismappedavailable = ismappedavailable;
	}

	public List getSubgllist() {
		return subgllist;
	}

	public void setSubgllist(List subgllist) {
		this.subgllist = subgllist;
	}

	public List getGlkeyslist() {
		return glkeyslist;
	}

	public void setGlkeyslist(List glkeyslist) {
		this.glkeyslist = glkeyslist;
	}

	public List getGllistbean() {
		return gllistbean;
	}

	public void setGllistbean(List gllistbean) {
		this.gllistbean = gllistbean;
	}

	private String prodid;
	public Boolean enableauth;

	public Boolean getEnableauth() {
		return enableauth;
	}

	public void setEnableauth(Boolean enableauth) {
		this.enableauth = enableauth;
	}

	public String getProdid() {
		return prodid;
	}

	public void setProdid(String prodid) {
		this.prodid = prodid;
	}

	class Myproduct {
		private String productid;
		private String productname;

		public String getProductid() {
			return productid;
		}

		public void setProductid(String productid) {
			this.productid = productid;
		}

		public String getProductname() {
			return productname;
		}

		public void setProductname(String productname) {
			this.productname = productname;
		}

		public Myproduct(String productid, String productname) {
			super();
			this.productid = productid;
			this.productname = productname;
		}
	}

	List<Myproduct> productlist = new ArrayList<Myproduct>();

	public List<Myproduct> getProductlist() {
		return productlist;
	}

	public void setProductlist(List<Myproduct> productlist) {
		this.productlist = productlist;
	}

	private List scheme_list;

	List servicecodelist;
	private List curencyvalues;

	public List getCurencyvalues() {
		return curencyvalues;
	}

	public void setCurencyvalues(List curencyvalues) {
		this.curencyvalues = curencyvalues;
	}

	public List getServicecodelist() {
		return servicecodelist;
	}

	public void setServicecodelist(List servicecodelist) {
		this.servicecodelist = servicecodelist;
	}

	public List getScheme_list() {
		return scheme_list;
	}

	public void setScheme_list(List scheme_list) {
		this.scheme_list = scheme_list;
	}

	List staticcardtypelist;

	public List getStaticcardtypelist() {
		return staticcardtypelist;
	}

	public void setStaticcardtypelist(List staticcardtypelist) {
		this.staticcardtypelist = staticcardtypelist;
	}

	List bincurrencylist;

	public List getBincurrencylist() {
		return bincurrencylist;
	}

	public void setBincurrencylist(List bincurrencylist) {
		this.bincurrencylist = bincurrencylist;
	}

	private List schemecode;

	public List getSchemecode() {
		return schemecode;
	}

	public void setSchemecode(List schemecode) {
		this.schemecode = schemecode;
	}

	private List bincur;

	public List getBincur() {
		return bincur;
	}

	public void setBincur(List bincur) {
		this.bincur = bincur;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode(HttpSession session) {

		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	// HttpSession session = getRequest().getSession();
	// private String instid1= inbean.getInstid();
	// String i_Name = (String)session.getAttribute("Instname");
	/*
	 * String card_currency;
	 * 
	 * 
	 * public String getCard_currency() { return card_currency; }
	 * 
	 * public void setCard_currency(String card_currency) { this.card_currency =
	 * card_currency; }
	 */
	List limit_list;

	public List getLimit_list() {
		return limit_list;
	}

	public void setLimit_list(List limit_list) {
		this.limit_list = limit_list;
	}

	public String display() {
		trace("*************** Adding Subproduct Begins **********");
		enctrace("*************** Adding Subproduct Begins **********");
		String prodcode = null;
		HttpSession session = getRequest().getSession();
		try {
			String instid = (String) session.getAttribute("Instname");
			// setInstid(i_Name);
			List cardtype_productlist;
			String prodname, prodid, binno, prod_bin;
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
			 * this.parentid = menuid; }else{ this.parentid = "000"; } initmail
			 * = false; }
			 */
			/*** MAIL BLOCK ****/

			int subcnt = reqdao.reqSubProduct(instid, session, jdbctemplate);
			trace("subcnt req check result : " + subcnt);
			if (subcnt < 0) {
				return "required_home";
			}

			String query_scheme = addSubProdActionDao.getQueryScheme(instid, jdbctemplate);
			String query_currency = addSubProdActionDao.getQueryCurrency(instid, jdbctemplate);
			// String bincurrency="select CUR_CODE from IFP_BIN_CURRENCY where
			// INST_ID='"+i_Name+"'";

			String query_forcurrency = addSubProdActionDao.getQueryCurrency1(instid, jdbctemplate);
			/*
			 * List staticcardtypelist =
			 * addSubProdActionDao.getStaticCardTypeList(instid,jdbctemplate);
			 * trace("Got the static card type... :"+
			 * staticcardtypelist.size()); if( staticcardtypelist.isEmpty() ){
			 * session.setAttribute("dispsubprod_localMessage",
			 * "Unable to process...check the log"); trace(
			 * "Static Card type not found on table IFP_GLOBAL_STATIC_CARD_TYPE"
			 * ); session.setAttribute("dispsubprod_localError", "E"); return
			 * "addsubproddisplay"; } setStaticcardtypelist(staticcardtypelist);
			 */
			// List bin_currency =jdbctemplate.queryForList(bincurrency);
			/*
			 * System.out.println("bin_currency :  " + bin_currency);
			 * if(bin_currency.isEmpty()) { addActionError(
			 * " No Bin currency exist Exists, Please Add Bin First ");
			 * 
			 * return "required_home"; }
			 * 
			 * setBincur(bin_currency); System.out.println("step4 ");
			 */
			/*
			 * String
			 * servicecodeqry=addSubProdActionDao.getServiceCodelist(instid,
			 * jdbctemplate); servicecodelist
			 * =jdbctemplate.queryForList(servicecodeqry);
			 * if(servicecodelist.isEmpty()) { addActionError(
			 * "NO SERVICE CODE CONFIGURED."); trace(
			 * "NO SERVICE CODE CONFIGURED."); session.setAttribute("preverr",
			 * "E"); return "required_home";
			 * 
			 * } setServicecodelist(servicecodelist); trace(
			 * "SERVICE CODE LIST : " + getServicecodelist() );
			 * 
			 */

			List currencyvalues = jdbctemplate.queryForList(query_forcurrency);

			if (currencyvalues.isEmpty()) {
				addActionError("No Currency Configured, Please Add currency and Try again");
				trace("No Scheme Exists for currency, Please Add Currency Scheme First");
				session.setAttribute("preverr", "E");
				return "required_home";

			}
			setCurencyvalues(currencyvalues);

			List scheme_exists = jdbctemplate.queryForList(query_scheme);
			if (scheme_exists.isEmpty()) {
				addActionError("No Fee Configured, Please Add Fee and Try again");
				trace("No Scheme Exists, Please Add Scheme First");
				session.setAttribute("preverr", "E");
				return "required_home";

			}
			setScheme_list(scheme_exists);

			String limitcodeqry = addSubProdActionDao.getLimitExistlist(instid, jdbctemplate);
			List limitexistlist = jdbctemplate.queryForList(limitcodeqry);

			if (!limitexistlist.isEmpty()) {
				setLimit_list(limitexistlist);
			}

			/*
			 * GLConfigureDAO gldao = new GLConfigureDAO(); List gllist =
			 * gldao.fchGlSchemeList(instid, jdbctemplate); if( gllist.isEmpty()
			 * ){ addActionError("No sub gl not yet configured...."); return
			 * "required_home"; }
			 * 
			 * setGllistbean(gllist);
			 */

			cardtype_productlist = commondesc.getProductListView(instid, jdbctemplate, session);
			trace("prodlist==>" + cardtype_productlist.size());
			if ((cardtype_productlist.isEmpty())) {
				addActionError("No Product Exists ");
				trace("No Product Exists ");
				session.setAttribute("preverr", "E");
				return "required_home";

			} else {
				Iterator itr = cardtype_productlist.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					prodname = ((String) map.get("CARD_TYPE_NAME"));
					prodid = ((String) map.get("PRODUCT_CODE"));

					trace("prodname : " + prodname + " prodid : " + prodid);
					productlist.add(new Myproduct(prodname, prodid));
				}
				session.setAttribute("dispsubprod_localError", "S");
			}

		} catch (Exception e) {
			session.setAttribute("dispsubprod_localMessage",
					" Error While Getting the Product Details For Adding Sub-Product ");
			session.setAttribute("dispsubprod_localError", "E");
			trace("Exception : Error While Getting the Product Details For Adding Sub-Product " + e.getMessage());
			e.printStackTrace();
			trace("\n\n");
			enctrace("\n\n");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "addsubproddisplay";
	}

	public void callAJax() {
		System.out.println("Welcome to callAJax");
		HttpSession session = getRequest().getSession();
		try {
			String i_Name = (String) session.getAttribute("Instname");
			List suproduct_select_result;
			String prodcode, subprodid, product_bin;

			String sel = null;
			product_bin = prodid.trim();
			String[] prodbin = product_bin.split("-");
			System.out.println("prodbin[0] =========== > " + prodbin[0]);
			System.out.println("prodbin[1] ============> " + prodbin[1]);

			suproduct_select_result = addSubProdActionDao.getSuproductSelectResult(prodbin, i_Name, jdbctemplate);
			System.out.println("The Result is  " + suproduct_select_result);
			if (suproduct_select_result.isEmpty()) {
				getResponse().getWriter()
						.write("<select name='subproductlist' id='subproductlist' onchange=\"return rename_subproduct();\">"
								+ "<option value=\"-2\">--No Sub-Product to ADD--</option>");
			} else {
				Iterator itr = suproduct_select_result.iterator();
				sel = "<select name='subproductlist' id='subproductlist' onchange=\"return rename_subproduct();\">"
						+ "<option value=\"-1\">--Select SUB Product--</option>";
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					prodcode = ((String) map.get("SUB_PROD_ID"));
					subprodid = ((String) map.get("SUB_PROD_DESC"));
					sel = sel + "<option value=\" " + prodcode + "\">" + subprodid + "</option>";
				}
				sel = sel + "</select>";
				System.out.println("THE SUB PRODIUCT SELCT AJAX OS ----> " + sel);
				getResponse().getWriter().write(sel);

			}

		} catch (Exception e) {
			try {
				getResponse().getWriter()
						.write("<select name='subproductlist' id='subproductlist' onchange=\"return rename_subproduct();\">"
								+ "<option value=\"-2\">-- Error --</option>");
			} catch (Exception ioe) {
				System.out.println("Erro WHile Send Error MEssage to Ajax ");
			}
		}

	}

	String bincurlist;

	public String getBincurlist() {
		return bincurlist;
	}

	public void setBincurlist(String bincurlist) {
		this.bincurlist = bincurlist;
	}

	public String saveData() throws Throwable {
		trace("************Adding sup product ***********");
		enctrace("************Adding sup product ***********");
		HttpSession session = getRequest().getSession();

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();

		String instid = comInstId(session);
		String usercode = comUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("ADDSUBPROD", txManager);
		try {
			String productCode = getRequest().getParameter("product").trim();

			String subProductName = getRequest().getParameter("subproductname").trim();
			String feeCode = getRequest().getParameter("scheme_scode").trim();
			String limitCode = getRequest().getParameter("limit_code");

			String expiryDate = getRequest().getParameter("expiry").trim();
			String personalizedCard = getRequest().getParameter("personalized").trim();
			String pinRequired = "Z";
			String maintainRequired = getRequest().getParameter("maintain_required");
			String currencymulti = getRequest().getParameter("currencymulti");
			String userid = comUserCode(session);

			String username = commondesc.getUserName(instid, userid, jdbctemplate);
			System.out.println("Testing user name " + username);

			bean.setCurrencymulti(currencymulti);
			bean.setExpiry(expiryDate);
			bean.setPersonalized(personalizedCard);
			bean.setMaintain_required(maintainRequired);
			bean.setLimit_code(limitCode);
			bean.setScheme_scode(feeCode);
			bean.setProduct(productCode);
			bean.setSubproductname(subProductName);

			System.out.println("after Testing user name " + username);

			if (instid == "") {
				System.out.println("validation part Institution");
				addActionError(" Please enter Institution Name");
				return display();
			}

			if (productCode.equals("-1")) {
				System.out.println("validation for produc tcode" + productCode);
				addFieldError("product", " Please select product");
				return display();
			}
			System.out.println("after productCode " + username);

			boolean subProductNameval = Validation.Spccharcter(subProductName);
			if (!subProductNameval) {
				System.out.println("validation part subProduct");
				addFieldError("subproductname", " Please enter sub product Name");
				return display();
			}

			System.out.println("validation for currency");
			if (currencymulti.equals("-1")) {

				addFieldError("currencymulti", " Please select currency");
				return display();
			}

			System.out.println("validation for Limit");
			if (limitCode.equals("-1")) {

				addFieldError("limit_code", " Please select Limit Code");
				return display();
			}

			System.out.println("validation for Fee");
			if (feeCode.equals("-1")) {
				System.out.println("validation for Fee");
				addFieldError("scheme_scode", " Please select Fee code");
				return display();
			}
			boolean expiryDateval = Validation.Number(expiryDate);
			if (!expiryDateval) {
				System.out.println("validation part Expiry");
				addFieldError("expiry", " Please enter expiryDate");
				return display();
			}
			if (personalizedCard.equals("-1")) {
				System.out.println("validation for card generation type ");
				addFieldError("personalized", " Please  card generation type");
				return display();
			}

			if (maintainRequired.equals("-1")) {

				addFieldError("maintain_required", " Please  card generation type");
				return display();
			}
			String subProductId = commondesc.fchSubProductSeq(instid, jdbctemplate);
			System.out.println("subProductId   " + subProductId);
			String bin = commondesc.getBin(instid, productCode, jdbctemplate);
			String serviceCode = commondesc.getServiceCode(instid, bin, jdbctemplate);

			String flag = getRequest().getParameter("act");
			System.out.println("flag ---- " + flag);
			String auth_code = "1";
			String mkchkrstatus = "D", authmsg = "";
			if (flag.equals("D")) {
				auth_code = "1";
				mkchkrstatus = "D";
			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = "Waiting for Authorization";
			}

			String sql = "", sql1 = "";
			int productdet_insertstatus = 1, productdet_insertstatus_temp, productdet_insertstatus1;
			if (flag.equals("D")) {
				trace("Inserting main table subproduct...");
				sql = addSubProdActionDao.getProductdetInsertstatus(instid, productCode, subProductId, subProductName,
						feeCode, limitCode, serviceCode, expiryDate, personalizedCard, currencymulti, pinRequired,
						maintainRequired, usercode, auth_code, mkchkrstatus);

				enctrace("query_insrt_subproduct : " + sql);
				/* productdet_insertstatus = jdbctemplate.update(sql); */
				// by gowtham 210819
				productdet_insertstatus = jdbctemplate.update(sql,
						new Object[] { instid, productCode, subProductId, subProductName, feeCode, limitCode,
								serviceCode, expiryDate, personalizedCard, currencymulti, pinRequired, maintainRequired,
								auth_code, usercode, date.getCurrentDate(), "", "", "", "0" });

				trace("Got result_insrt_cur : " + productdet_insertstatus);

				//// Added For CHN Sequence
				List binlist = addSubProdActionDao.gettingBinList(instid, jdbctemplate);
				trace("bin list size subprod " + binlist);
				if (!binlist.isEmpty()) {
					ListIterator lstitr = binlist.listIterator();
					int insertswitchstatus[] = new int[binlist.size()];
					String ctbaseinsert[] = new String[binlist.size()];
					int i = 0;
					while (lstitr.hasNext()) {
						Map mp = (Map) lstitr.next();
						ctbaseinsert[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES('"
								+ instid + "','" + subProductId + "','1','" + mp.get("BASELEN") + "','" + mp.get("BIN")
								+ "')";
						enctrace("insert baseno query branch" + ctbaseinsert[i]);
						i++;
					}
					insertswitchstatus = jdbctemplate.batchUpdate(ctbaseinsert);
					trace("batch update got subprod..." + insertswitchstatus.length);
				} else {
					trace("NO BINS ARE THERE BASED ON SUBPROD...FOR CARD GENERATED SEQUANCE");
				}

			}
			trace("Inserting temp subproduct...");

			/*
			 * sql = addSubProdActionDao.getProductdetTempInsertstatus(instid,
			 * productCode, subProductId ,subProductName ,feeCode ,limitCode
			 * ,serviceCode ,expiryDate ,personalizedCard
			 * ,currencymulti,pinRequired ,maintainRequired,usercode,
			 * auth_code,mkchkrstatus); enctrace(
			 * "query_insrt_temp_subproduct : " + sql);
			 * productdet_insertstatus_temp = jdbctemplate.update(sql); sql1 =
			 * addSubProdActionDao.inserttempname(instid, productCode,
			 * subProductId ); productdet_insertstatus1 =
			 * jdbctemplate.update(sql1); trace(
			 * "Got resulttemporary_insrt_cur : " +
			 * productdet_insertstatus_temp);
			 */

			// by gowtham-210819
			sql = addSubProdActionDao.getProductdetTempInsertstatus(instid, productCode, subProductId, subProductName,
					feeCode, limitCode, serviceCode, expiryDate, personalizedCard, currencymulti, pinRequired,
					maintainRequired, usercode, auth_code, mkchkrstatus);
			enctrace("query_insrt_temp_subproduct : " + sql);
			productdet_insertstatus_temp = jdbctemplate.update(sql,
					new Object[] { instid, productCode, subProductId, subProductName, feeCode, limitCode, serviceCode,
							expiryDate, personalizedCard, currencymulti, pinRequired, maintainRequired, auth_code,
							usercode, date.getCurrentDate(), "", "", "", "0" });
			sql1 = addSubProdActionDao.inserttempname(instid, productCode, subProductId);
			// sql.append("'"+instid+"','"+productCode+"','"+subProductId+"','INSTANT
			// CARD','INSTANT CARD') ");
			productdet_insertstatus1 = jdbctemplate.update(sql1,
					new Object[] { instid, productCode, subProductId, "INSTANT CARD", "INSTANT CARD" });

			if (productdet_insertstatus > 0 && productdet_insertstatus_temp > 0) {

				txManager.commit(transact.status);
				addActionMessage(" Sub-Product \"" + subProductName + "\" Added Successfully " + authmsg);

				/************* AUDIT BLOCK **************/
				try {

					// added by gowtham_230719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg("Sub-Product [ " + subProductName + " ] Added");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("3060");

					// added by gowtham_010819
					String pcode = null;
					auditbean.setProduct(productCode);

					auditbean.setSubproduct(subProductId);
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				System.out.println(" No Insrt Happened ");
				txManager.rollback(transact.status);
				addActionError(" Sub-Product Not Added ");

			}
		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			txManager.rollback(transact.status);
			System.out.println("Error WHile Executijng the Insert " + e.getMessage());
			addActionError("Exception : Could not insert sub-product");
			e.printStackTrace();

		}
		return "required_home";
	}

	public String flag_changer(String flag) {
		String modify_flag = "";
		if (flag.equals("1"))
			modify_flag = "Y";
		else if (flag.equals("0"))
			modify_flag = "N";
		else
			modify_flag = "B";
		return modify_flag;
	}

	private List<String> prod_idlist;

	public List<String> getProd_idlist() {
		return prod_idlist;
	}

	public void setProd_idlist(List<String> prod_idlist) {
		this.prod_idlist = prod_idlist;
	}

	List prodlist;

	public List getProdlist() {
		return prodlist;
	}

	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}

	public String authsubprod() {
		setEnableauth(true);
		trace("*************** authorize subproduct Begins **********");
		enctrace("*************** authorize subproduct Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("act", act);
		}
		List View_prod_result;

		try {

			prodlist = commondesc.getProductListView(instid, jdbctemplate, session);
			trace("prodlist__  :::: " + prodlist);

			if (prodlist.isEmpty()) {
				addActionError("No Records Found");
				return "required_home";
			} else {
				setProdlist(prodlist);
			}
		} catch (Exception e) {
			session.setAttribute("viewsubProdErrorStatus", "E");
			session.setAttribute("viewsubProdErrorMessage", " Error While Fetching The Sub-Product Details ");
			trace(" Error While Fetching The Sub-Product Details---> " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "auth_subproduct";
	}

	public String authorizeAction() {
		trace("*********Sub product authorizeAction");
		enctrace("*********Sub product authorizeAction");
		HttpSession session = getRequest().getSession();

		IfpTransObj transact = commondesc.myTranObject("AUTHSUBPROD", txManager);

		String productcode = getRequest().getParameter("product_code");
		trace("productcode === " + productcode);
		String subproductid = getRequest().getParameter("sub_prod_id");
		trace("subproductid === " + subproductid);
		String auth = getRequest().getParameter("auth");
		System.out.println("auth ===" + auth);
		String instid = comInstId(session);
		String usercode = comUserCode(session);

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String userid = comUserCode(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		/*
		 * String prodcode = getRequest().getParameter("product"); String
		 * subprodcode = getRequest().getParameter("sub_product"); String
		 * subproductname=getRequest().getParameter("subproductname");
		 * System.out.println("subproductname ---"+subproductname); String
		 * scheme_scode=getRequest().getParameter("scheme_scode");
		 * System.out.println("scheme_scode ---"+scheme_scode); String
		 * limit_code=getRequest().getParameter("limit_code");
		 * System.out.println("limit_code ---"+limit_code); String
		 * card_currency=getRequest().getParameter("card_currency");
		 * System.out.println("card_currency ---"+card_currency); String
		 * scode=getRequest().getParameter("scode"); System.out.println(
		 * "scode ---"+scode); String
		 * expiry=getRequest().getParameter("expiry"); System.out.println(
		 * "expiry ---"+expiry); String
		 * reload=getRequest().getParameter("reload"); System.out.println(
		 * "reload ---"+reload); String
		 * atmallow=getRequest().getParameter("atmallow"); System.out.println(
		 * "atmallow ---"+atmallow); String
		 * pin_required=getRequest().getParameter("pin_required");
		 * System.out.println("pin_required ---"+pin_required); String
		 * min_loadamt=getRequest().getParameter("min_loadamt");
		 * System.out.println("min_loadamt ---"+min_loadamt); String
		 * max_loadamt=getRequest().getParameter("max_loadamt");
		 * System.out.println("max_loadamt ---"+max_loadamt); String
		 * register_required=getRequest().getParameter("register_required");
		 * System.out.println("register_required ---"+register_required); String
		 * maintain_required=getRequest().getParameter("maintain_required");
		 * System.out.println("maintain_required ---"+maintain_required); String
		 * corprateproduct=getRequest().getParameter("corprateproduct");
		 * System.out.println("corprateproduct ---"+corprateproduct); String
		 * corporatecardno=getRequest().getParameter("corporatecardno");
		 * System.out.println("corporatecardno ---"+corporatecardno); String
		 * kyclimitlevel=getRequest().getParameter("kyclimitlevel");
		 * System.out.println("kyclimitlevel ---"+kyclimitlevel); String
		 * maxallowamt=getRequest().getParameter("maxallowamt");
		 * System.out.println("maxallowamt ---"+maxallowamt); String
		 * maxallowcnt=getRequest().getParameter("maxallowcnt");
		 * System.out.println("maxallowcnt ---"+maxallowcnt); String
		 * shiftintervel=getRequest().getParameter("shiftintervel");
		 * System.out.println("shiftintervel ---"+shiftintervel); String
		 * mcc_required=getRequest().getParameter("mcc_required");
		 * System.out.println("mcc_required ---"+mcc_required); String
		 * merchant_required=getRequest().getParameter("merchant_required");
		 * System.out.println("merchant_required ---"+merchant_required); String
		 * network_required=getRequest().getParameter("network_required");
		 * System.out.println("network_required ---"+network_required); String
		 * merchreg=getRequest().getParameter("merchreg"); System.out.println(
		 * "merchreg ---"+merchreg); String
		 * mccreg=getRequest().getParameter("mccreg"); System.out.println(
		 * "mccreg ---"+mccreg); String personalized =
		 * getRequest().getParameter("personalized"); System.out.println(
		 * "personalized ---"+personalized); String networkreg=
		 * getRequest().getParameter("networkreg");
		 */

		try {

			try {
				int updstatus;
				String statuserr;
				String move_main_table_qury;
				int insertedmain = 1;
				int del_result = 1;
				String authstatus = addSubProdActionDao.getAuthSubqry(productcode, subproductid, jdbctemplate);
				if (authstatus.equals("0")) {
					if (auth.equals("De-Authorize")) {
						String remarks = getRequest().getParameter("reason");
						statuserr = "De-authorized";
						updstatus = addSubProdActionDao.updateDeAuthStatus(instid, productcode, subproductid, usercode,
								remarks, jdbctemplate);
						trace("Updating Deauthozation status...." + updstatus);
					} else {
						statuserr = "Authorized";
						updstatus = addSubProdActionDao.updateAuthStatus(instid, productcode, subproductid, usercode,
								jdbctemplate);
						trace("Updating authozation status...." + updstatus);
						/*
						 * int maintablestatus =
						 * addSubProdActionDao.getmaintablestatus(instid,
						 * productcode, subproductid,jdbctemplate);
						 * if(maintablestatus<1){ move_main_table_qury =
						 * addSubProdActionDao.movetomaintable(productcode,
						 * subproductid,instid,jdbctemplate); insertedmain =
						 * jdbctemplate.update(move_main_table_qury); }else{
						 * String delete_qry =
						 * addSubProdActionDao.deleteSubProductInMainTable(
						 * instid,productcode,subproductid,jdbctemplate);
						 * del_result = jdbctemplate.update(delete_qry);
						 * move_main_table_qury =
						 * addSubProdActionDao.movetomaintable(productcode,
						 * subproductid,instid,jdbctemplate); insertedmain =
						 * jdbctemplate.update(move_main_table_qury); }
						 */
						//// Added For CHN Sequence

						List binlist = addSubProdActionDao.gettingBinList(instid, jdbctemplate);
						trace("bin list size subprod " + binlist);
						if (!binlist.isEmpty()) {
							ListIterator lstitr = binlist.listIterator();
							int insertswitchstatus[] = new int[binlist.size()];
							String ctbaseinsert[] = new String[binlist.size()];
							int i = 0;
							while (lstitr.hasNext()) {
								Map mp = (Map) lstitr.next();
								ctbaseinsert[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES('"
										+ instid + "','" + subproductid + "','1','" + mp.get("BASELEN") + "','"
										+ mp.get("BIN") + "')";
								enctrace("insert baseno query branch" + ctbaseinsert[i]);
								i++;
							}
							insertswitchstatus = jdbctemplate.batchUpdate(ctbaseinsert);
							trace("batch update got subprod..." + insertswitchstatus.length);
						} else {
							trace("NO BINS ARE THERE BASED ON SUBPROD...FOR CARD GENERATED SEQUANCE");
						}
					}
					System.out.println("UOPDATE AUTH STATUS === " + updstatus);
					System.out.println("INSERT MOVE TO MAIN TABLE STATUS=== " + insertedmain);
					System.out.println("DELETE MAIN TABLE STATUS === " + del_result);
					if (updstatus <= 0 || insertedmain <= 0 || del_result <= 0) {
						txManager.rollback(transact.status);
						addActionError("Could not authorize....");
						return authsubprod();
					}
					txManager.commit(transact.status);
					addActionMessage("Sub-product " + statuserr + " successfully.");

					/************* AUDIT BLOCK **************/
					try {

						// added by gowtham_230719
						trace("ip address======>  " + ip);
						auditbean.setIpAdress(ip);

						auditbean.setActmsg("Sub-product [ " + subproductid + " ] is " + statuserr);
						auditbean.setUsercode(username);
						auditbean.setAuditactcode("3060");

						// added by gowtham_010819
						String pcode = null;
						auditbean.setProduct(productcode);

						auditbean.setSubproduct(subproductid);
						commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
					} catch (Exception audite) {
						trace("Exception in auditran : " + audite.getMessage());
					}
					/************* AUDIT BLOCK **************/

					return "required_home";
				}

				if (authstatus.equals("1")) {
					addActionError("Sub-Product already authorized...");
					return authsubprod();
				}

			} catch (EmptyResultDataAccessException e) {
				transact.txManager.rollback(transact.status);
				trace("Exception: " + e.getMessage());
				addActionError("Exception : Could not authorize");
			}

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			trace("Exception : " + e.getMessage());
			addActionError("Exception : Could not authorize");
		}
		return "required_home";
	}

	public String viewsubprod() {
		trace("*************** viewing subproduct Begins **********");
		enctrace("*************** viewing subproduct Begins **********");

		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		List View_prod_result;

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

			setEnableauth(false);
			/*
			 * if(flag.equals("D")){ setEnableauth(false); }else
			 * if(flag.equals("M")){ setEnableauth(true); }
			 */

			prodlist = commondesc.getProductListView(i_Name, jdbctemplate, session);
			trace("prodlist__" + prodlist);
			setProdlist(prodlist);
		} catch (Exception e) {
			session.setAttribute("viewsubProdErrorStatus", "E");
			session.setAttribute("viewsubProdErrorMessage", " Error While Fetching The Sub-Product Details ");
			trace(" Error While Fetching The Sub-Product Details--> " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "viewsubprod";
	}

	private List subprod_list;

	public List getSubprod_list() {
		return subprod_list;
	}

	public void setSubprod_list(List subprod_list) {
		this.subprod_list = subprod_list;
	}

	String produtid;
	String bin_id;

	public String getProdutid() {
		return produtid;
	}

	public void setProdutid(String produtid) {
		this.produtid = produtid;
	}

	public String getBin_id() {
		return bin_id;
	}

	public void setBin_id(String bin_id) {
		this.bin_id = bin_id;
	}

	public String mccdescription;

	public String getMccdescription() {
		return mccdescription;
	}

	public void setMccdescription(String mccdescription) {
		this.mccdescription = mccdescription;
	}

	public String product_name;
	public String sub_product;

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getSub_product() {
		return sub_product;
	}

	public void setSub_product(String sub_product) {
		this.sub_product = sub_product;
	}

	public String generateSubprodlist() {
		
		System.out.println("calling ================  ");
		trace("*************** View Subproduct Begins **********");
		enctrace("*************** View Subproduct Begins **********");
		HttpSession session = getRequest().getSession();
		String nextvalue = getRequest().getParameter("next");
		System.out.println("nextvalue---- " + nextvalue);

		String inst_mcclist = null;

		/*
		 * String flag=getRequest().getParameter("act"); trace(
		 * "got act is ..........: "+ flag ); if( flag != null ){
		 * session.setAttribute("act", flag); }
		 */

		String act = getRequest().getParameter("act");
		trace("request act value : " + act);
		setActflag(act);

		if (act == null) {
			act = (String) session.getAttribute("act");
		}
		if (act != null) {
			session.getAttribute("act");
		} else {
			addActionError("Could not understand privilage level");
			return "required_home";
		}
		trace("Processing....." + act);

		if (getRequest().getParameter("auth") != null) {
			setEnableauth(true);
		} else {
			setEnableauth(false);
		}

		subproductbean.doact = getRequest().getParameter("doact");
		try {
			String inst_Name = (String) session.getAttribute("Instname");

			String productname = getRequest().getParameter("cardtype");

			String subproduct = getRequest().getParameter("subproduct");
			
			trace("calling ======= productname "+productname);
			trace("calling ======= subproduct "+subproduct);
			

			if (productname.equals("-1")) {
				System.out.println("validation part product");
				addActionError("Please select product");
				return authsubprod();
			}
			if (subproduct.equals("-1")) {
				System.out.println("validation part sub product");
				addActionError(" Please select sub product");
				return authsubprod();
			}

			if (subproductbean.doact != null && subproductbean.doact.equals("$DELAUTH")) {
				String subproductlist[] = getRequest().getParameter("subproductlist").split("-");
				productname = subproductlist[0];
				subproduct = subproductlist[1];
			}

			setProduct_name(productname);
			setSub_product(subproduct);
			trace("subproduct===> " + subproduct);
			trace("productname==> " + productname);
			trace("INSTITUTION ID GET FROM SESSION =======> " + inst_Name);
			String product_query = addSubProdActionDao.getProductinfoList(inst_Name, productname, subproduct,
					jdbctemplate);
			/*
			 * List productinfo_list = jdbctemplate.queryForList(product_query);
			 */
			List productinfo_list = jdbctemplate.queryForList(product_query);
			trace("productinfo_list--> " + productinfo_list.size());
			if (!(productinfo_list.isEmpty())) {

				ListIterator itr = productinfo_list.listIterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					String feecode = (String) map.get("FEE_CODE");
					trace("feecode---> " + feecode);
					String limitcode = (String) map.get("LIMIT_ID");
					trace("limitcode---> " + limitcode);
					String schemecode = (String) map.get("GL_SCHEME_CODE");
					trace("schemecode---> " + schemecode);
					String cardcurdesc = (String) map.get("CARD_CCY");
					trace("cardcurdesc ==== " + cardcurdesc);
					inst_mcclist = (String) map.get("MCC_LIST");
					trace("mcclist---> " + inst_mcclist);

					trace("feecode==> " + feecode + "       limitcode==>   " + limitcode + "schemecode===> "
							+ schemecode);
					String feedesc = getCodedesc(inst_Name, feecode, "F", jdbctemplate);
					String limitdesc = getCodedesc(inst_Name, limitcode, "L", jdbctemplate);
					String schemedesc = "";// getCodedesc(inst_Name, schemecode,
											// "S", jdbctemplate);
					String product_name = addSubProdActionDao.getProductdesc(inst_Name, productname, jdbctemplate);

					trace("feedesc==> " + feedesc + "limitdesc==> " + limitdesc + "schemedesc==> " + schemedesc);
					String configuredby = (String) map.get("CONFIG_BY");
					;
					String username = commondesc.getUserName(inst_Name, configuredby, jdbctemplate);
					map.put("CONFIG_BY", username);
					map.put("FEE_DESC", feedesc);
					map.put("LIMIT_DESC", limitdesc);
					map.put("SCH_NAME", schemedesc);
					map.put("CARD_TYPE_NAME", product_name);

					itr.remove();
					itr.add(map);
					trace("itr==> " + map);
				}
				setSubprod_list(productinfo_list);

			}
		} catch (Exception e) {
			session.setAttribute("curerr", " Error while fetching values:" + e.getMessage());
			session.setAttribute("curerr", "E");
			trace("Exception: Error while fetching values" + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		if (subproductbean.doact.equals("$DEL")) {
			return "deletesubproduct_view";
		} else if (subproductbean.doact.equals("$DELAUTH")) {

			return "deletesubproduct_authview";
		}

		if (nextvalue.equals("viewauth")) {
			return "viewsubproductdetails";
		} else {
			return "editsubproductdetails";
		}
	}

	public String getCodedesc(String instid, String code, String flag, JdbcTemplate jdbctemplate) {
		String ret_val = "NO", qury_str = "";
		HttpSession session = getRequest().getSession();
		if (flag.equals("F")) {

			// System.out.println("flag===>"+flag);
			qury_str = addSubProdActionDao.getFeedescList(instid, code, jdbctemplate);
			List feedesclist = jdbctemplate.queryForList(qury_str);
			if (feedesclist.isEmpty()) {
				addActionError("Error while fetching datas from FEE DESC ");
				trace(" Error while fetching datas from FEE DESC ");
			}
		}
		if (flag.equals("L")) {
			// System.out.println("flag===>"+flag);
			qury_str = addSubProdActionDao.getLimitdescList(instid, code, jdbctemplate);
			List limitdesclist = jdbctemplate.queryForList(qury_str);
			if (limitdesclist.isEmpty()) {
				addActionError(" Error while fetching datas from LIMIT DESC ");
				trace(" Error while fetching datas from LIMIT DESC ");
			}
		}
		if (flag.equals("S")) {
			// System.out.println("flag===>"+flag);
			qury_str = addSubProdActionDao.getSchemedescList(instid, code, jdbctemplate);
			List schemedesclist = jdbctemplate.queryForList(qury_str);
			if (schemedesclist.isEmpty()) {
				addActionError("Error while fetching datas from SCHEME MASTER ");
				trace(" Error while fetching datas from SCHEME MASTER ");
			}
		}
		trace("qury_str==> " + qury_str);
		ret_val = (String) jdbctemplate.queryForObject(qury_str, String.class);
		trace("ret_val--> " + ret_val);
		return ret_val;

	}

	private List subprod_detail;

	public List getSubprod_detail() {
		return subprod_detail;
	}

	public void setSubprod_detail(List subprod_detail) {
		this.subprod_detail = subprod_detail;
	}

	public String subproddetails() {
		HttpSession session = getRequest().getSession();

		String inst_Name = (String) session.getAttribute("Instname");
		String product_code = getRequest().getParameter("product_code");
		trace("product_code==> " + product_code);
		String productname = getRequest().getParameter("product_name");
		trace("productname==> " + productname);
		try {

			List prod_det_result = addSubProdActionDao.getProdDetResult(inst_Name, product_code, jdbctemplate);
			trace("RESULT " + prod_det_result.size());
			setSubprod_detail(prod_det_result);
		} catch (Exception e) {
			System.out.println("viewsubproddetails " + e.getMessage());
		}
		return "subproddetails";
	}

	public String deleteSubproddetails() {
		HttpSession session = getRequest().getSession();

		String inst_Name = (String) session.getAttribute("Instname");
		String product_code = getRequest().getParameter("product_code");

		IfpTransObj transact = commondesc.myTranObject("SUBPROD", txManager);

		try {
			int prod_del_result = addSubProdActionDao.getProdDelResult(inst_Name, product_code, jdbctemplate);
			System.out.println("RESULT " + prod_del_result);
			if (prod_del_result != -1)
				session.setAttribute("viewsubProdErrorMessage", " Sub Product Deleted");
			txManager.commit(transact.status);
		} catch (Exception e) {
			addActionError("Unable to continue the process");
			trace(" Error While Deleting The Sub Product " + e.getMessage());
			txManager.rollback(transact.status);
		}
		return viewsubprod();
	}

	public void glSchemeCode() throws Exception {
		System.out.println("gl scheme code ");
		String opt = "";
		HttpSession session = getRequest().getSession();
		try {
			String instid = comInstId(session);
			String prodcode_bin = (String) getRequest().getParameter("prodcode");
			System.out.println("prodcode==> " + prodcode_bin);
			String[] productcode = prodcode_bin.split("~");
			String prodcode = productcode[0];

			String glcode = addSubProdActionDao.getGLcode(instid, prodcode, jdbctemplate);
			List glschemelist = addSubProdActionDao.getGlschemeList(instid, glcode, jdbctemplate);
			if (!glschemelist.isEmpty()) {
				opt += "<option value='-1'> - SELECT GL SCHEME - </option>";
				Iterator itr = glschemelist.iterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String schemecode = (String) mp.get("SCH_CODE");
					String schemedesc = (String) mp.get("SCH_NAME");
					opt += "<option value='" + schemecode + "'> " + schemedesc + " </option>";
				}
			} else {
				opt += "<option value='-1'> NO GL SCHEME CONFIGURED </option>";
			}
		} catch (Exception e) {
			opt += "<option value='-1'>-- ERROR ---</option>";
			e.printStackTrace();
		}
		System.out.println("opt===> " + opt);
		getResponse().getWriter().write(opt);

	}

	public List mcc_list;

	public List getMcc_list() {
		return mcc_list;
	}

	public void setMcc_list(List mcc_list) {
		this.mcc_list = mcc_list;
	}

	public void retrivemcclist() throws Exception {
		HttpSession session = getRequest().getSession();
		//

		String mcc_req = getRequest().getParameter("mccrequired");
		System.out.println("mcc_req--> " + mcc_req);
		String tablers = "";
		try {
			if (mcc_req.equals("1")) {
				List selectlist = addSubProdActionDao.getSelectList(jdbctemplate);
				System.out.println("List From DB===>" + selectlist);

				if (!(selectlist.isEmpty())) {
					Iterator ccyItr = selectlist.iterator();
					tablers = "<table style='border:1px solid #efefef;padding:0 auto' cellpadding='0' border='0' align='center' width='100%' cellspacing='0'>";
					tablers += "<tr><td>MCC LIST:</td><td><select  multiple name=\'mccreg\' id=\'mccreg\' multiple style='height: 80px'>";

					while (ccyItr.hasNext()) {

						Map ccymap = (Map) ccyItr.next();
						String mccdesc = (String) ccymap.get("MCC_DESC");
						String mcccode = (String) ccymap.get("MCC_CODE");
						tablers += "<option value='" + mcccode + "'>" + mccdesc + "</option>";
					}
					tablers = tablers
							+ "</select><br/> <small style='font-size:10px;color:#000'> &nbsp;&nbsp;&nbsp; press ctrl to select multiple</small></td></tr></table>";
					getResponse().getWriter().write(tablers);

				}
				System.out.println("####################tablers     " + tablers);
			} else {
				getResponse().getWriter().write(tablers);
			}
		} catch (Exception e) {
			tablers += "<option value='-1'>-- ERROR ---</option>";
			e.printStackTrace();
		}
	}

	public void retrivenetworklist() throws Exception {
		HttpSession session = getRequest().getSession();

		// JdbcTemplate merchjdbctemplate = new
		// JdbcTemplate(merchantdataSource);
		String network_req = getRequest().getParameter("networkrequired");
		String instid = comInstId(session);
		System.out.println("network_req--> " + network_req);
		String tablers = "";
		try {
			if (network_req.equals("1")) {
				List selectlist = addSubProdActionDao.getSelectList(instid, jdbctemplate);
				System.out.println("List From DB===>" + selectlist);

				if (!(selectlist.isEmpty())) {
					Iterator ccyItr = selectlist.iterator();
					tablers = "<table style='border:1px solid #efefef;padding:0 auto' cellpadding='0' border='0' align='center' width='100%' cellspacing='0'>";
					tablers += "<tr><td>NETWORK LIST:</td><td><select multiple name=\'networkreg\' id=\'networkreg\' multiple style='height: 80px'>";
					while (ccyItr.hasNext()) {

						Map ccymap = (Map) ccyItr.next();
						String networkdesc = (String) ccymap.get("NETWORK_DESC");
						String networkcode = (String) ccymap.get("NETWORK_ID");
						tablers += "<option value='" + networkcode + "'>" + networkdesc + "</option>";
					}
					tablers = tablers
							+ "</select><br/> <small style='font-size:10px;color:#000'> &nbsp;&nbsp;&nbsp; press ctrl to select multiple</small></td></tr></table>";
					getResponse().getWriter().write(tablers);

				}
				System.out.println("####################tablers     " + tablers);
			} else {
				getResponse().getWriter().write(tablers);
			}
		} catch (Exception e) {
			tablers += "<option value='-1'>-- ERROR ---</option>";
			e.printStackTrace();
		}
	}

	public void retrivemerchantlist() throws Exception {
		HttpSession session = getRequest().getSession();

		String merchant_req = getRequest().getParameter("merchantrequired");
		String instid = getRequest().getParameter("instid");
		System.out.println("instid--> " + instid);
		System.out.println("merchant_req--> " + merchant_req);
		String tablers = "";
		try {
			if (merchant_req.equals("1")) {
				List selectlist = addSubProdActionDao.getSelectListMerchant(instid, jdbctemplate);
				System.out.println("List From DB===>" + selectlist);

				if (!(selectlist.isEmpty())) {
					int i = 0, j = 0;
					Iterator ccyItr = selectlist.iterator();

					tablers = "<table style='border:1px solid #efefef;padding:0 auto' cellpadding='0' border='0' align='center' width='80%' cellspacing='0'>";
					while (ccyItr.hasNext()) {
						System.out.println("i++--->" + i);
						Map ccymap = (Map) ccyItr.next();
						String merchantdesc = (String) ccymap.get("MERCH_NAME");
						String merchantcode = (String) ccymap.get("MERCH_ID");
						if (i == 0) {
							System.out.println("Test ----> 1");
							tablers = tablers + "<tr>";
						}
						tablers = tablers + "<td><input type=\"checkbox\" name=\"seccurrency\" id=\"sec_currency" + j
								+ "\" value=\"" + merchantcode + "\"/>" + merchantdesc + "</td>";
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
			} else {
				getResponse().getWriter().write(tablers);
			}
		} catch (Exception e) {
			tablers += "<option value='-1'>-- ERROR ---</option>";
			e.printStackTrace();
		}
	}

	public String mapSubProductGL() {
		trace("*************** mapSubProductGL **********");
		enctrace("*************** mapSubProductGL **********");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");

		try {
			prodlist = commondesc.getProductListView(instid, jdbctemplate, session);
			trace("Got prodlist :" + prodlist.size());
			setProdlist(prodlist);
		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			addActionError("Exception while adding sub product");
			return "required_home";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "subprod_glmaphome";
	}

	public String subProdGlMapList() {
		trace("*************** subProdGlMapList **********");
		enctrace("*************** subProdGlMapList **********");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");

		String product = getRequest().getParameter("cardtype");
		if (product == null) {
			product = (String) session.getAttribute("PROD_CODE");
		}
		String subproductid = getRequest().getParameter("subproduct");
		if (subproductid == null) {
			subproductid = (String) session.getAttribute("SUBPROD_CODE");
		}
		GLConfigureDAO gldao = new GLConfigureDAO();
		try {
			trace("Got the request : prduct[" + product + "] subproduct[" + subproductid + "]");
			String productname = commondesc.getProductdesc(instid, product, jdbctemplate);
			trace("Getting product desc...got : " + productname);

			String subproductname = commondesc.getSubproductname(instid, product, subproductid, jdbctemplate);
			trace("Getting subproduct desc...got : " + subproductname);
			session.setAttribute("PROD_CODE", product);
			session.setAttribute("SUBPROD_CODE", subproductid);
			session.setAttribute("PROD_DESC", productname);
			session.setAttribute("SUBPROD_DESC", subproductname);

			List glkeylist = addSubProdActionDao.getGlKeys(instid, jdbctemplate);
			trace("Getting gl keys....got : " + glkeylist.size());
			if (glkeylist == null || glkeylist.isEmpty()) {
				addActionError("Unable to configure the gl mapping");
				trace("No records found in IFP_GL_KEYS table...");
				return "required_home";
			}
			setGlkeyslist(glkeylist);

			List subgllist = gldao.fchGlSchemeList(instid, jdbctemplate);
			if (subgllist.isEmpty()) {
				addActionError("Unable to configure the gl mapping....no sub-gl configured");
				trace("No records found in IFP_GL_SCHEME_MASTER table...");
				return "required_home";
			}
			setSubgllist(subgllist);

			List mappedgl = addSubProdActionDao.getSubProdMappedGl(instid, product, subproductid, jdbctemplate);
			trace("Getting sub-product mapped gl ...got : " + mappedgl.size());
			if (mappedgl == null || mappedgl.isEmpty()) {
				setIsmappedavailable(false);
			} else {
				ListIterator mapitr = mappedgl.listIterator();
				while (mapitr.hasNext()) {

					Map mp = (Map) mapitr.next();
					String glkey = (String) mp.get("GL_TYPE");
					String subglcode = (String) mp.get("GL_CODE");
					mp.put("GL_TYPE", addSubProdActionDao.getKeyDesc(glkey, jdbctemplate));
					String subgldesc = commondesc.getSubGlescription(instid, subglcode, jdbctemplate);
					if (subgldesc.equals("NOREC")) {
						addActionError("Unable to get description for the sub gl code");
						trace("Could not get subgl description for [ " + subglcode + " ] ");
					}
					mp.put("GL_CODE", subgldesc);
					mapitr.remove();
					mapitr.add(mp);
				}
				setMappedgl_list(mappedgl);
			}

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			addActionError("Exception :Could not continue the proces");
			e.printStackTrace();
			return "required_home";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "subprod_glmaplist";
	}

	public String subProdGlMapAction() {
		trace("*************** subProdGlMapList **********");
		enctrace("*************** subProdGlMapList **********");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");

		IfpTransObj transact = commondesc.myTranObject("SUBGLMAP", txManager);

		String product = getRequest().getParameter("productcode");
		String subproductid = getRequest().getParameter("subproduct");
		String glkey = getRequest().getParameter("glkey");
		String subglcode = getRequest().getParameter("subglcode");
		try {

			String recordid = commondesc.generateRecordSequance(instid, jdbctemplate);
			trace("Generating record id ...got : " + recordid);
			if (recordid == null) {
				trace("Could not generate record id ....");
				addActionError("Unable to process....could not generate record id");
				return "required_home";
			}
			int x = addSubProdActionDao.insertSubGlMapping(instid, recordid, product, subproductid, glkey, subglcode,
					jdbctemplate);
			trace("Inserting into subproduct gl mapping code...got : " + x);
			if (x < 0) {
				transact.txManager.rollback(transact.status);
				trace("Could not insert the records into IFP_GL_KEYS_MAPPING");
				addActionError("Unable to process....");
			}

			int updrec = commondesc.updateReocrdid(instid, jdbctemplate);
			trace("Updating record id seq...got : " + updrec);
			if (updrec <= 0) {
				transact.txManager.rollback(transact.status);
				;
				trace("Could not update the record id sequance in ifp_sequance_master table...");
				addActionError("Unable to process....");
			}
			transact.txManager.commit(transact.status);
			trace("Inserted successfully");
			addActionMessage("Mapping configured successfully");

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			trace("Exception : " + e.getMessage());
			addActionError("Exception : Could not continue the process");
		}
		return this.subProdGlMapList();
	}

	public String deleteMappedRecord() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);

		IfpTransObj transact = commondesc.myTranObject("DELMAPRECO", txManager);

		String recordid = getRequest().getParameter("recordid");
		try {

			int delrec = addSubProdActionDao.deleteMappedRecord(instid, recordid, jdbctemplate);
			trace("Deleting record id...got : " + delrec);
			if (delrec <= 0) {
				transact.txManager.rollback(transact.status);
				trace("Could not delete the record id...");
				addActionError("Unable to process....");
			}
			transact.txManager.commit(transact.status);
			trace("Record Deleted successfully");
			addActionMessage("Mapping Deleted successfully");

		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("Unable to process....");
			trace("Exceptin : " + e.getMessage());
		}
		return this.subProdGlMapList();
	}

	public String dispeditsubprod() {
		setEnableauth(false);
		String status = "edit";
		setStatusflag(status);
		trace("*************** edit subproduct Begins **********");
		enctrace("*************** edit subproduct Begins **********");
		HttpSession session = getRequest().getSession();
		String i_Name = (String) session.getAttribute("Instname");
		String flag = getRequest().getParameter("act");
		System.out.println("flag ---- " + flag);
		setActflag(flag);
		List View_prod_result;
		try {

			prodlist = commondesc.getProductListView(i_Name, jdbctemplate, session);
			trace("prodlist__" + prodlist);
			setProdlist(prodlist);
		} catch (Exception e) {
			session.setAttribute("viewsubProdErrorStatus", "E");
			session.setAttribute("viewsubProdErrorMessage", " Error While Fetching The Sub-Product Details ");
			trace(" Error While Fetching The Sub-Product Details---> " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "viewsubprod";
	}

	public String updateSubproductDetails() {
		trace("*************** Update subproduct Begins **********");
		enctrace("*************** Update subproduct Begins **********");
		IfpTransObj transact = commondesc.myTranObject("UPDSUB", txManager);
		HttpSession session = getRequest().getSession();

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();
		try {
			String instid = comInstId(session);
			String usercode = comUserCode(session);
			String productCode = getRequest().getParameter("productid").trim();
			System.out.println("productCode:::" + productCode);
			String subProductId = getRequest().getParameter("sub_product").trim();
			System.out.println("subProductId:::" + subProductId);
			String subProductName = getRequest().getParameter("subproductname").trim();
			System.out.println("subProductName:::" + subProductName);
			String feeCode = getRequest().getParameter("scheme_scode").trim();
			System.out.println("feeCode:::" + feeCode);
			String limitCode = getRequest().getParameter("limit_code");
			System.out.println("limitCode:::" + limitCode);
			String serviceCode = getRequest().getParameter("scode");
			System.out.println("serviceCode:::" + serviceCode);
			String expiryDate = getRequest().getParameter("expiry").trim();
			System.out.println("expiryDate:::" + expiryDate);
			String personalizedCard = getRequest().getParameter("personalized").trim();
			System.out.println("personalizedCard:::" + personalizedCard);
			String pinRequired = getRequest().getParameter("pin_required");
			System.out.println("pinRequired:::" + pinRequired);
			String maintainRequired = getRequest().getParameter("maintain_required");
			System.out.println("maintainRequired:::" + maintainRequired);
			String currencymulti = getRequest().getParameter("currencymulti");
			System.out.println("productCode:::" + productCode);
			System.out.println("subProductId:::" + subProductId);

			/*
			 * bean.setProduct(productCode);
			 * bean.setSubproductname(subProductName);
			 * bean.setScheme_scode(feeCode); bean.setLimit_code(limitCode);
			 * bean.setCurrencymulti(currencymulti);
			 * bean.setMaintain_required(maintainRequired);
			 * bean.setPersonalized(personalizedCard);
			 * bean.setScode(serviceCode); bean.setPinRequired(pinRequired);
			 * bean.setSubProductId(subProductId);
			 */
			System.out.println("validation part Institution" + instid);
			if (instid == "") {
				System.out.println("validation part Institution");
				addActionError(" Please enter Institution Name");
				return dispeditsubprod();
			}

			if (productCode.equals("-1")) {
				System.out.println("validation for produc tcode" + productCode);
				addFieldError("product", " Please select product");
				return dispeditsubprod();
			}

			boolean subProductNameval = Validation.Spccharcter(subProductName);
			if (!subProductNameval) {
				System.out.println("validation part subProduct");
				addFieldError("subproductname", " Please enter sub product Name");
				return dispeditsubprod();
			}

			/*
			 * if(currencymulti.equals("-1")) {
			 * 
			 * addFieldError("currencymulti"," Please select currency"); return
			 * dispeditsubprod(); } System.out.println("validation for currency"
			 * );
			 */
			if (pinRequired.equals("-1")) {
				System.out.println("validation for pinRequired");
				/*
				 * addActionError("pin_required"," Please select pin Required");
				 */
				addActionError(" Please select pin Required");
				return dispeditsubprod();
			}

			if (limitCode.equals("-1")) {
				System.out.println("validation for Limit");
				addActionError(" Please select Limit Code");
				return dispeditsubprod();
			}

			/*
			 * System.out.println("validation for Fee");
			 * if(feeCode.equals("-1")) { System.out.println(
			 * "validation for Fee"); addFieldError("scheme_scode",
			 * " Please select Fee code"); return dispeditsubprod(); }
			 */

			boolean expiryDateval = Validation.Number(expiryDate);
			if (!expiryDateval) {
				System.out.println("validation part Expiry");
				addFieldError("expiry", " Please enter expiryDate");
				return dispeditsubprod();
			}

			boolean serviceCodeval = Validation.Number(serviceCode);
			if (!serviceCodeval) {
				System.out.println("validation part service code");
				addFieldError("expiry", " Please enter service code");
				return dispeditsubprod();
			}

			if (personalizedCard.equals("-1")) {
				System.out.println("validation for card generation type ");
				addFieldError("personalized", " Please  card generation type");
				return dispeditsubprod();
			}

			if (maintainRequired.equals("-1")) {

				addFieldError("maintain_required", " Please  card generation type");
				return dispeditsubprod();
			}

			String userid = comUserCode(session);

			String username = commondesc.getUserName(instid, userid, jdbctemplate);
			System.out.println("Testing user name " + username);

			String mkrchkr = getRequest().getParameter("actflag");
			System.out.println("actflag ---- " + mkrchkr);
			int result_update_subprod = 0;
			int result_update_subprod_temp = 1;
			String authcode;
			if (mkrchkr.equals("D")) {
				authcode = "1";
			} else {
				authcode = "0";
			}

			if (mkrchkr.equals("D")) {
				trace("Updating main table currency...");

				/*
				 * String query_update_cur=addSubProdActionDao.
				 * updateSubProductInMainTable(instid, productCode, subProductId
				 * ,subProductName ,feeCode ,limitCode ,serviceCode ,expiryDate
				 * ,personalizedCard ,pinRequired
				 * ,maintainRequired,usercode,authcode); enctrace(
				 * "query_update_cur : " + query_update_cur);
				 * result_update_subprod =jdbctemplate.update(query_update_cur);
				 * trace("Updating main records...got  : " +
				 * result_update_subprod);
				 */

				String query_update_cur = addSubProdActionDao.updateSubProductInMainTable(instid, productCode,
						subProductId, subProductName, feeCode, limitCode, serviceCode, expiryDate, personalizedCard,
						pinRequired, maintainRequired, usercode, authcode);
				enctrace("query_update_cur : " + query_update_cur);
				result_update_subprod = jdbctemplate.update(query_update_cur,
						new Object[] { subProductName, personalizedCard, expiryDate, serviceCode, feeCode, limitCode,
								pinRequired, maintainRequired, authcode, usercode, date.getCurrentDate(), productCode,
								subProductId, instid });
				trace("Updating main records...got  : " + result_update_subprod);

				if (result_update_subprod <= 0) {
					txManager.rollback(transact.status);
					addActionError("Could not contitnue the process...");
					return "required_home";
				}

			}
			trace("Updating temp currency...");

			/*
			 * String query_update_temp_cur=addSubProdActionDao.
			 * updateSubProductInTempTable(instid, productCode, subProductId
			 * ,subProductName ,feeCode ,limitCode ,serviceCode ,expiryDate
			 * ,personalizedCard ,pinRequired
			 * ,maintainRequired,usercode,authcode); enctrace(
			 * "query_update_temp_cur : " + query_update_temp_cur);
			 * result_update_subprod_temp
			 * =jdbctemplate.update(query_update_temp_cur);
			 */

			//// by gowtham-300819
			String query_update_temp_cur = addSubProdActionDao.updateSubProductInTempTable(instid, productCode,
					subProductId, subProductName, feeCode, limitCode, serviceCode, expiryDate, personalizedCard,
					pinRequired, maintainRequired, usercode, authcode);
			enctrace("query_update_temp_cur : " + query_update_temp_cur);
			result_update_subprod_temp = jdbctemplate.update(query_update_temp_cur,
					new Object[] { subProductName, personalizedCard, expiryDate, serviceCode, feeCode, limitCode,
							pinRequired, maintainRequired, authcode, usercode, date.getCurrentDate(), productCode,
							subProductId, instid });
			trace("Updating temp records...got :  " + result_update_subprod_temp);
			if (result_update_subprod_temp <= 0) {
				txManager.rollback(transact.status);
				addActionError("Could not contitnue the process...");
				return "required_home";
			}
			txManager.commit(transact.status);
			addActionMessage("Sub-Product details updated successfully");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg("Sub-Product Updated");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3060");
				// added by gowtham_010819
				String pcode = null;
				auditbean.setProduct(productCode);

				auditbean.setSubproduct(subProductName);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(transact.status);
			addActionError(" Error While updating The Sub-Product Details ");
			trace(" Error While updating The Sub-Product Details---> " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "editsubproductdetails";
	}

	public List editdetailsList;

	public List getEditdetailsList() {
		return editdetailsList;
	}

	public void setEditdetailsList(List editdetailsList) {
		this.editdetailsList = editdetailsList;
	}

	public List kycdetailsList;

	public List getKycdetailsList() {
		return kycdetailsList;
	}

	public void setKycdetailsList(List kycdetailsList) {
		this.kycdetailsList = kycdetailsList;
	}

	public String subprodname;

	public String getSubprodname() {
		return subprodname;
	}

	public void setSubprodname(String subprodname) {
		this.subprodname = subprodname;
	}

	public List mcclist;

	public List getMcclist() {
		return mcclist;
	}

	public void setMcclist(List mcclist) {
		this.mcclist = mcclist;
	}

	public String actsubprod;

	public String getActsubprod() {
		return actsubprod;
	}

	public void setActsubprod(String actsubprod) {
		this.actsubprod = actsubprod;
	}

	public String editdetails() {
		trace("*************** Edit Subproduct Begins **********");
		enctrace("*************** Edit Subproduct Begins **********");
		String prodcode = null;
		HttpSession session = getRequest().getSession();
		try {
			String instid = (String) session.getAttribute("Instname");
			String actflag = getRequest().getParameter("actflag");
			System.out.println("actflag --- " + actflag);
			setActsubprod(actflag);
			String productid = getRequest().getParameter("cardtype");
			String subproductid = getRequest().getParameter("subproduct");

			if (productid.equals("-1")) {
				System.out.println("validation part product");
				addActionError("Please select product");
				return dispeditsubprod();
			}
			if (subproductid.equals("-1")) {
				System.out.println("validation part sub product");
				addActionError(" Please select sub product");
				return dispeditsubprod();
			}

			setSubprodname(subproductid);
			List cardtype_productlist;
			String prodname, prodid, binno, prod_bin;
			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
			}

			/*** MAIL BLOCK ****/
			trace("initmail--" + initmail + " parentid :  " + this.parentid);
			if (initmail) {
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId(req, jdbctemplate);
				if (!menuid.equals("NOREC")) {
					trace("parentid--" + menuid);
					this.parentid = menuid;
				} else {
					this.parentid = "000";
				}
				initmail = false;
			}
			/*** MAIL BLOCK ****/

			int subcnt = reqdao.reqSubProduct(instid, session, jdbctemplate);
			trace("subcnt req check result : " + subcnt);
			if (subcnt < 0) {
				return "required_home";
			}
			String query_scheme = addSubProdActionDao.getQueryScheme(instid, jdbctemplate);
			String bin = commondesc.getBin(instid, productid, jdbctemplate);

			String servicecodeqry = addSubProdActionDao.getServiceCodelist(instid, productid, subproductid,
					jdbctemplate);
			servicecodelist = jdbctemplate.queryForList(servicecodeqry);
			if (servicecodelist.isEmpty()) {
				addActionError("NO SERVICE CODE CONFIGURED.");
				trace("NO SERVICE CODE CONFIGURED.");
				return "required_home";
			}
			setServicecodelist(servicecodelist);

			trace("SERVICE CODE LIST : " + getServicecodelist());
			List scheme_exists = jdbctemplate.queryForList(query_scheme);
			if (scheme_exists.isEmpty()) {
				addActionError("No Scheme Exists, Please Add Scheme First");
				trace("No Scheme Exists, Please Add Scheme First");
				return "required_home";

			}
			setScheme_list(scheme_exists);
			String limitcodeqry = addSubProdActionDao.getLimitExistlist(instid, jdbctemplate);
			List limitexistlist = jdbctemplate.queryForList(limitcodeqry);
			if (!limitexistlist.isEmpty()) {
				setLimit_list(limitexistlist);
			}
			/*
			 * GLConfigureDAO gldao = new GLConfigureDAO(); List gllist =
			 * gldao.fchGlSchemeList(instid, jdbctemplate); if( gllist.isEmpty()
			 * ){ addActionError("No sub gl not yet configured...."); return
			 * "required_home"; } setGllistbean(gllist);
			 */

			cardtype_productlist = commondesc.getProductListView(instid, jdbctemplate, session);
			trace("prodlist==>" + cardtype_productlist.size());
			if ((cardtype_productlist.isEmpty())) {
				addActionError("No Product Exists ");
				trace("No Product Exists ");
				return "required_home";
			} else {
				Iterator itr = cardtype_productlist.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					prodname = ((String) map.get("CARD_TYPE_NAME"));
					prodid = ((String) map.get("PRODUCT_CODE"));
					trace("prodname : " + prodname + " prodid : " + prodid);
					productlist.add(new Myproduct(prodname, prodid));
				}

			}
			List selectlist = addSubProdActionDao.getSelectList(jdbctemplate);
			setMcclist(selectlist);
			String subproddetails = addSubProdActionDao.getsubproddetails(productid, subproductid, instid,
					jdbctemplate);
			List subproddetails_list = jdbctemplate.queryForList(subproddetails);
			if (!subproddetails_list.isEmpty()) {
				ListIterator itr = subproddetails_list.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					prodid = ((String) mp.get("PRODUCT_CODE"));
					trace(" prodid : " + prodid);
					mp.put("PRODUCT_NAME", commondesc.getProductdesc(instid, prodid, jdbctemplate));
					itr.remove();
					itr.add(mp);
				}
				setEditdetailsList(subproddetails_list);
			}

		} catch (Exception e) {
			session.setAttribute("dispsubprod_localMessage",
					" Error While Getting the Product Details For Adding Sub-Product ");
			session.setAttribute("dispsubprod_localError", "E");
			trace("Exception : Error While Getting the Product Details For Adding Sub-Product " + e.getMessage());
			e.printStackTrace();
			trace("\n\n");
			enctrace("\n\n");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "editsubproductdetails";
	}

	public String deleteSubProductHome() {
		trace("*************** delete subproduct Begins **********");
		enctrace("*************** delete subproduct Begins **********");
		HttpSession session = getRequest().getSession();
		subproductbean.formaction = "generateSubprodlistAddSubProdAction.do";
		subproductbean.doact = "$DEL";
		String instid = comInstId(session);
		List View_prod_result;
		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("act", act);
		}
		trace("Processing..." + session.getAttribute("act"));
		try {
			prodlist = commondesc.getProductListView(instid, jdbctemplate, session);
			setProdlist(prodlist);
		} catch (Exception e) {
			addActionError(" Unable to continue the process");
			trace(" Error While Fetching The Sub-Product Details--> " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n");
		enctrace("\n");
		return "deletesubprod_home";
	}

	public String deleteSubProducAction() {
		trace("*************** deleteSubProducAction Begins **********");
		enctrace("*************** deleteSubProducAction Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String productcode = getRequest().getParameter("productcode");
		String subproduct = getRequest().getParameter("subproductcode");
		String reason = getRequest().getParameter("reason");
		String act = (String) session.getAttribute("act");

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String userid = comUserCode(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		IfpTransObj transact = commondesc.myTranObject("DELSUBPROD", txManager);
		if (act == null) {
			addActionError("Could not understand privilage level");
			return "required_home";
		}
		trace("Processing act....." + act);
		int result = -1;
		try {
			if (act.equals("D")) {
				String subproddelqry = addSubProdActionDao.deleteSubProductInMainTable(instid, productcode, subproduct,
						jdbctemplate);
				result = jdbctemplate.update(subproddelqry);
				trace("Deleting sub product from main...got : " + result);
				addActionMessage("Sub Product Details deleted Successfully.");
				auditbean.setActmsg("Sub-Product Deleted.");
			} else {
				addActionMessage("Sub Product Details deleted Successfully. Waiting for Authorization");
				auditbean.setActmsg("Sub-Product Deleted. Waiting for Authorization");
			}

			result = addSubProdActionDao.updateDeletedStatus(instid, "2", productcode, subproduct, "0", usercode,
					reason, jdbctemplate);
			trace("Deleting updating status...got : " + result);

			txManager.commit(transact.status);

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3060");

				// added by gowtham_010819
				String pcode = null;
				auditbean.setProduct(productcode);
				auditbean.setSubproduct(subproduct);

				auditbean.setRemarks(reason);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (Exception e) {
			addActionError(" Unable to continue the process");
			trace(" Error While Fetching The Sub-Product Details--> " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n");
		enctrace("\n");
		return "required_home";
	}

	public String deleteSubProductAuthHome() {
		trace("*************** deleteSubProductAuthHome Begins **********");
		enctrace("*************** deleteSubProductAuthHome Begins **********");
		HttpSession session = getRequest().getSession();
		subproductbean.formaction = "generateSubprodlistAddSubProdAction.do";
		subproductbean.doact = "$DELAUTH";
		String instid = comInstId(session);
		List View_prod_result;
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
			prodlist = this.getDeletedProductList(instid, jdbctemplate);
			if (prodlist.isEmpty()) {
				addActionError("No Records Available");
				return "required_home";
			}
			setProdlist(prodlist);
		} catch (Exception e) {
			addActionError(" Unable to continue the process");
			trace(" Error While Fetching The Sub-Product Details--> " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n");
		enctrace("\n");
		return "deletesubprod_authhome";
	}

	public List getDeletedProductList(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List subprodlist = null;

		/*
		 * String subprodlistqry =
		 * "SELECT ( PRODUCT_CODE||'-'||SUB_PROD_ID) AS SUB_PROD_ID, SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID='"
		 * +instid+"' AND DELETED_FLAG='2' AND AUTH_STATUS='0'  "; enctrace(
		 * "subprodlistqry :"+subprodlistqry ); subprodlist =
		 * jdbctemplate.queryForList(subprodlistqry);
		 */

		// by gowtham-210819
		String subprodlistqry = "SELECT ( PRODUCT_CODE||'-'||SUB_PROD_ID) AS SUB_PROD_ID, SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=? AND DELETED_FLAG=? AND AUTH_STATUS=?  ";
		enctrace("subprodlistqry :" + subprodlistqry);
		subprodlist = jdbctemplate.queryForList(subprodlistqry, new Object[] { instid, "2", "0" });

		return subprodlist;
	}

	public String deleteSubProducAuthAction() {
		trace("*************** deleteSubProducAction Auth Begins **********");
		enctrace("*************** deleteSubProducAction Auth Begins **********");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("DELSUBPROD", txManager);
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String productcode = getRequest().getParameter("productcode");
		String subproduct = getRequest().getParameter("subproductcode");
		String reason = getRequest().getParameter("reason");
		String act = getRequest().getParameter("act");
		String authbtn = getRequest().getParameter("doact");

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String userid = comUserCode(session);

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		if (act == null) {
			act = (String) session.getAttribute("act");
		}
		if (act != null) {
			session.setAttribute("act", act);
		} else {
			addActionError("Could not understand privilage level");
			return "required_home";
		}
		trace("Processing....." + act + " ..got doact : " + authbtn);
		int result = -1;
		try {
			if (authbtn.equals("AUTH")) { // Authorize
				trace("Authorizing.....");
				String subproddelqry = addSubProdActionDao.deleteSubProductInMainTable(instid, productcode, subproduct,
						jdbctemplate);
				result = jdbctemplate.update(subproddelqry);
				trace("Deleting sub product from main...got : " + result);

				result = addSubProdActionDao.updateDeletedAuthStatus(instid, "2", productcode, subproduct, "1",
						usercode, reason, jdbctemplate);
				trace("Updating...Deleted auth status...got : " + result);

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				addActionMessage("Authorized Successfully.");
				auditbean.setActmsg("Deleted SubProduct Approved");
			} else { // De-Authorize

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				trace("De-Authorizing.....");
				result = addSubProdActionDao.updateDeletedAuthStatus(instid, "0", productcode, subproduct, "1",
						usercode, reason, jdbctemplate);
				trace("Updating...Deleted auth status...got : " + result);
				addActionMessage("De-Authorized Successfully.");
				auditbean.setActmsg("Deleted SubProduct Rejected");
			}

			txManager.commit(transact.status);
			trace("Committed...");

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3060");

				// added by gowtham_010819
				String pcode = null;
				auditbean.setProduct(productcode);

				auditbean.setSubproduct(subproduct);
				auditbean.setRemarks(reason);
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}

		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError(" Unable to continue the process");
			trace(" Error While Fetching The Sub-Product Details--> " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n");
		enctrace("\n");
		return "required_home";
	}
}
