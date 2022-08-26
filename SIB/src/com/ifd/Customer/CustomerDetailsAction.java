package com.ifd.Customer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.personalize.PersionalizedcardCondition;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.opensymphony.xwork2.ModelDriven;

public class CustomerDetailsAction extends BaseAction implements ModelDriven<CustomerDetailBean> {

	private static final long serialVersionUID = -2701974637566770853L;
	CustomerDetailBean bean = new CustomerDetailBean();
	CustomerDetailDAO dao = new CustomerDetailDAO();
	CommonDesc commondesc = new CommonDesc();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
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

	public CustomerDetailBean getModel() {
		return bean;
	}

	PersionalizedcardCondition brcodecon = new PersionalizedcardCondition();

	private String act;

	private List branchlist;

	private List customerauthproductlist;

	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}

	public List getCustomerauthproductlist() {
		return customerauthproductlist;
	}

	public void setCustomerauthproductlist(List customerauthproductlist) {
		this.customerauthproductlist = customerauthproductlist;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public List authcardorder;

	public List getAuthcardorder() {
		return authcardorder;
	}

	public void setAuthcardorder(List authcardorder) {
		this.authcardorder = authcardorder;
	}

	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String branchID() {
		HttpSession session = getRequest().getSession();

		String instid = (String) session.getAttribute("BRANCHCODE");
		return instid;
	}

	public String comUserId() {
		HttpSession session = getRequest().getSession();
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	public String comBranchId() {
		HttpSession session = getRequest().getSession();
		String br_id = (String) session.getAttribute("BRANCHCODE");
		return br_id;
	}
	

	public String comUsername() {
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("USERNAME");
		return username;
	}

	public String comuserType() {
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");

		return usertype;
	}

	public String editCustomerHome() {

		return "editcustomerhome";
	}

	public String editCustomer() throws Exception {
		trace("=====Get cutomer data  process begin=========");
		// String cardno = "";
		HttpSession session = getRequest().getSession();
		String instid = comInstId();
		String userid = comUserId();
		String branchID = branchID();

		String selectedtype = getRequest().getParameter("selectedtype");
		String phoneno = getRequest().getParameter("phoneno");
		String cardno = getRequest().getParameter("cardno");
		String custid = getRequest().getParameter("custid");
		String orderrefno = getRequest().getParameter("orderrefno");

		trace("phoneno type" + phoneno);
		trace("cardno type" + cardno);
		trace("custid type" + custid);
		trace("orderrefno type" + orderrefno);
		trace("selectedtype=== " + selectedtype);

		/*
		 * String valid=null; String check=
		 * " SELECT count(*) FROM PERS_CARD_PROCESS WHERE ORG_CHN='"+cardno+
		 * "' OR CIN='"+custid+"' OR ORDER_REF_NO='"+orderrefno+
		 * "' OR MOBILENO='"+phoneno+"'   "; trace("GGGGG"+check);
		 * enctrace("GGGGG"+check); valid =
		 * (String)jdbctemplate.queryForObject(check, String.class);
		 * if(!valid.equalsIgnoreCase("0")) { addActionError(
		 * "Entered Input is  Available in Process so You Can't Edit..!!");
		 * return "required_home"; }
		 * 
		 * String valid1=null; String check1=
		 * " SELECT count(*) FROM CARD_PRODUCTION WHERE ORG_CHN='"+cardno+
		 * "' OR CIN='"+custid+"' OR ORDER_REF_NO='"+orderrefno+
		 * "' OR MOBILENO='"+phoneno+"'   "; trace("GGGGG"+check1);
		 * enctrace("GGGGG"+check1); valid1 =
		 * (String)jdbctemplate.queryForObject(check1, String.class);
		 * if(valid1.equalsIgnoreCase("0")) { addActionError(
		 * "Entered Input is Not Available in Process and Production !!");
		 * return "required_home"; }
		 */

		String EDPK = "";
		Properties prop = null;
		String encCardno = "";
		StringBuffer hcardno = null;
		String CDMK = "";
		String CDPK = "";

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		PadssSecurity padsssec = new PadssSecurity();
		
		if (padssenable.equals("Y")) {

			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		
			String eDMK = "", eDPK = "";

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			prop = getCommonDescProperty();
			EDPK = prop.getProperty("EDPK");

			try {
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				Iterator secitr = secList.iterator();
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					/*
					 * eDMK = ((String) map.get("DMK")); eDPK = ((String)
					 * map.get("DPK")); cardno = padsssec.getECHN(eDMK, eDPK,
					 * cardno); hcardno = padsssec.getHashedValue(cardno +
					 * instid);
					 */

					CDMK = ((String) map.get("DMK"));
					CDPK = padsssec.decryptDPK(CDMK, EDPK);
					hcardno = padsssec.getHashedValue(cardno + instid);
					encCardno = padsssec.getECHN(CDPK, cardno);

				}
			} catch (Exception e1) {
				e1.printStackTrace();
				return "editcustomer";
			}
		}

		if (!cardno.equalsIgnoreCase("")) {

			trace("==== inside cardno ======");
			int checkvcardproc = dao.checkcardproc(encCardno, "CARD", jdbctemplate);
			if (checkvcardproc > 0) {
				if (!branchID.trim().equals("000")) {
					String validbranch = dao.getbrachuser(instid, encCardno, "CARD", jdbctemplate);
					if (!validbranch.equals(branchID)) {
						// if (!branchid.equals(validbranch)){

						trace("editvalidbranch" + validbranch + "loginbranchID" + branchID);

						addActionError("This " + cardno + " Card is edited by " + validbranch
								+ " User & Kindly Authorize by same branch user...");
						trace("[ " + cardno + " ]Card Collect Branch not Configured. ...");
						return "required_home";

					}

					else {
						addActionError("This " + cardno + "card is edited ,Card is waiting for  " + validbranch
								+ " Branch user authorization ..!!");
						return "required_home";
					}
				}
			}
		}

		// order
		if (!orderrefno.equalsIgnoreCase("")) {
			trace("=====  inside order =========");
			int CHECKORDER = dao.checkcardproc(orderrefno, "ORDER", jdbctemplate);
			if (CHECKORDER > 0) {

				String validbranch = dao.getbrachuser(instid, orderrefno, "ORDER", jdbctemplate);
				if (!validbranch.equals(branchID)) {
					// if (!branchid.equals(validbranch)){

					trace("editvalidbranch" + validbranch + "loginbranchID" + branchID);

					addActionError("This " + cardno + " order is edited by " + validbranch
							+ " User & Kindly Authorize by same branch user...");
					trace("[ " + cardno + " ]Card Collect Branch not Configured. ...");
					return "required_home";

				}

				else {
					addActionError("This " + cardno + "card is edited ,Card is waiting for  " + validbranch
							+ " Branch user authorization ..!!");
					return "required_home";
				}

			}
			if (!branchID.trim().equals("000")) {
				String checkvalidbranch = dao.checkValidbranchcard(orderrefno, "order", branchID, jdbctemplate);
				if ("0".equalsIgnoreCase(checkvalidbranch)) {
					addActionError(" Entered ORDERNO is some other branch / Invalid Card number..  ");
					return "required_home";
				}
			}
		}

		// phone
		if (!phoneno.equalsIgnoreCase("")) {

			trace("==== inside phonwe ====");
			int CHECKphone = dao.checkcardproc(phoneno, "PHONE", jdbctemplate);
			if (CHECKphone > 0) {

				String validbranch = dao.getbrachuser(instid, phoneno, "PHONE", jdbctemplate);
				if (!validbranch.equals(branchID)) {
					// if (!branchid.equals(validbranch)){

					trace("editvalidbranch" + validbranch + "loginbranchID" + branchID);

					addActionError("This " + cardno + " Card is edited by " + validbranch
							+ " User & Kindly Authorize by same branch user...");
					trace("[ " + cardno + " ]Card Collect Branch not Configured. ...");
					return "required_home";

				}

				else {
					addActionError("This " + cardno + "Phone number card is edited ,Card is waiting for  " + validbranch
							+ " Branch user authorization ..!!");
					return "required_home";
				}

			}
			if (!branchID.trim().equals("000")) {
				String checkvalidbranch = dao.checkValidbranchcard(phoneno, "PHONE", branchID, jdbctemplate);
				if ("0".equalsIgnoreCase(checkvalidbranch)) {
					addActionError(" Entered Phone Number  is some other branch / Invalid Card number..  ");
					return "required_home";
				}
			}
		}
		// phone end

		// cin start

		if (!custid.equalsIgnoreCase("")) {

			trace("=====  inside cin =======");
			int CHECKphone = dao.checkcardproc(custid, "CIN", jdbctemplate);
			if (CHECKphone > 0) {

				String validbranch = dao.getbrachuser(instid, custid, "CIN", jdbctemplate);
				if (!validbranch.equals(branchID)) {
					// if (!branchid.equals(validbranch)){

					trace("editvalidbranch" + validbranch + "loginbranchID" + branchID);

					addActionError("This " + cardno + " Card is edited by " + validbranch
							+ " User & Kindly Authorize by same branch user.../Card is in another process");
					trace("[ " + cardno + " ]Card Collect Branch not Configured. ...");
					return "required_home";

				}

				else {
					addActionError("This " + cardno + "Customer number card is edited ,Card is waiting for  "
							+ validbranch + " Branch user authorization ..!!");
					return "required_home";
				}

			}
			if (!branchID.trim().equals("000")) {
				String checkvalidbranch = dao.checkValidbranchcard(custid, "CIN", branchID, jdbctemplate);
				if ("0".equalsIgnoreCase(checkvalidbranch)) {
					addActionError(" Entered Customer Number  is some other branch / Invalid Card number..  ");
					return "required_home";
				}
			}
		}
		// System.out.println("branch_code "+
		// branchID+"selectedtype"+selectedtype+"phoneno"+phoneno
		// +"\n"+cardno+"\n custid"+custid);

		if (!cardno.equalsIgnoreCase("") && phoneno == "" && custid == "" && orderrefno == "") {

			trace("============= Editing with the card number  =============");

			if (!branchID.trim().equals("000")) {
				String checkvalidbranch = dao.checkValidbranchcard(encCardno, "CARD", branchID, jdbctemplate);
				if ("0".equalsIgnoreCase(checkvalidbranch)) {
					addActionError(" Entered CHN is some other branch / Invalid Card number..  ");
					return "required_home";
				}
			}

			int checkcardexist = dao.checkcardinprocess("CARD", encCardno, branchID, jdbctemplate);
			if (checkcardexist > 0) {
				addActionError(" Card is Edited Waiting for customer Edit Authorization   ");
				return "required_home";
			}

			/*
			 * String checkvalid =
			 * dao.checkValid(hcardno.toString(),"CARD",jdbctemplate);
			 */

			String checkvalid = dao.checkValid1(hcardno.toString(), jdbctemplate);

			if (!"0".equalsIgnoreCase(checkvalid)) {
				List<Map<String, Object>> custdetails = dao.getEmbossingName(encCardno, "CARD", jdbctemplate);
				if (!custdetails.isEmpty()) {
					bean.setEmbossingname((String) custdetails.get(0).get("EMB_NAME"));
					bean.setMobileno((String) custdetails.get(0).get("MOBILENO"));
					bean.setCustid((String) custdetails.get(0).get("CIN"));
					bean.setCardno((String) custdetails.get(0).get("ORG_CHN"));
				}
				trace("here they check the beasn is");
				if (bean.custid != null || bean.custid != "") {
					trace("jsakfjsakjfkasf");
					List<Map<String, Object>> custdetails1 = dao.getCustDetails(bean.getCustid(), "CARD", encCardno,
							jdbctemplate);
					if (!custdetails1.isEmpty()) {
						System.out.println("sfdasfsa" + (String) custdetails1.get(0).get("MARITAL_STATUS"));
						bean.setEmbossingname((String) custdetails1.get(0).get("EMB_NAME"));

						bean.setMobileno((String) custdetails1.get(0).get("MOBILENO"));
						bean.setFname((String) custdetails1.get(0).get("FNAME"));
						bean.setMname((String) custdetails1.get(0).get("MNAME"));
						bean.setLname((String) custdetails1.get(0).get("LNAME"));
						bean.setDob((String) custdetails1.get(0).get("DOB"));
						bean.setMartialstatus((String) custdetails1.get(0).get("MARITAL_STATUS"));
						bean.setNationality((String) custdetails1.get(0).get("NATIONALITY"));
						bean.setSpousename((String) custdetails1.get(0).get("SPOUCE_NAME"));
						bean.setMothername((String) custdetails1.get(0).get("MOTHER_NAME"));
						bean.setFathername((String) custdetails1.get(0).get("FATHER_NAME"));
						bean.setEmail((String) custdetails1.get(0).get("E_MAIL"));
						bean.setPobox((String) custdetails1.get(0).get("P_PO_BOX"));
						bean.setPhouseno((String) custdetails1.get(0).get("P_HOUSE_NO"));
						bean.setPstname((String) custdetails1.get(0).get("P_STREET_NAME"));
						bean.setPwardname((String) custdetails1.get(0).get("P_WARD_NAME"));
						bean.setPcity((String) custdetails1.get(0).get("P_CITY"));
						bean.setPdist((String) custdetails1.get(0).get("P_DISTRICT"));
						String acctno = "", acctflag = "";
						List acctnum = this.getacctnumber(instid, hcardno.toString(), jdbctemplate);
						if (acctnum.isEmpty()) {
							Map hmp = new HashMap();
							hmp.put("ACCOUNTNO", "NOREC");
							acctnum.add(hmp);
							bean.setAcctno(acctnum);
						}
						bean.setAcctno(acctnum);
					}
				}
			} else {
				addActionError("Cannot edit this  Details,  Card Must be Active or Not Active Status !!");
				return "required_home";
			}
		}

		// phone number
		if (!phoneno.equalsIgnoreCase("") && cardno == "" && custid == "" && orderrefno == "") {
			trace("**************editing with phone number*****" + phoneno);

			int checkcardexist = dao.checkcardinprocess("PH", phoneno, branchID, jdbctemplate);
			if (checkcardexist > 0) {
				addActionError(" Card is Edited Waiting for customer Edit Authorization   ");
				return "required_home";
			}

			String phonecheck = dao.checkphoneno(phoneno, jdbctemplate);
			trace("phonecheck  " + phonecheck);
			Integer result1111 = Integer.valueOf(phonecheck);
			String encCardno1="";
			trace("level 555555");

			if (result1111 >= 2) {
				trace(" Mobile Number getting  Two or Above  Records Kindly Use Card Number");
				addActionError(" Mobile Number getting  Two or Above  Records Kindly Use Card Number");
				return "required_home";
			}

			if (!"0".equalsIgnoreCase(phonecheck)) {

				List<Map<String, Object>> custdetails = dao.getEmbossingName(phoneno, "phoneno", jdbctemplate);
				if (!custdetails.isEmpty()) {
					bean.setEmbossingname((String) custdetails.get(0).get("EMB_NAME"));
					bean.setMobileno((String) custdetails.get(0).get("MOBILENO"));
					bean.setCustid((String) custdetails.get(0).get("CIN"));
					encCardno1=(String) custdetails.get(0).get("ORG_CHN");
					bean.setCardno(encCardno1);
				}
				
			
				String clrCardno=padsssec.getCHN(CDPK, encCardno1);
				hcardno = padsssec.getHashedValue(clrCardno + instid);
				
				trace("here they check the beasn is");
				if (bean.custid != null || bean.custid != "") {
					trace("phone details found" + phoneno);
					List<Map<String, Object>> custdetails1 = dao.getCustDetails(bean.getCustid(), "PHONE",
							bean.getCardno(), jdbctemplate);
					if (!custdetails1.isEmpty()) {
						System.out.println("sfdasfsa" + (String) custdetails1.get(0).get("MARITAL_STATUS"));
						bean.setEmbossingname((String) custdetails1.get(0).get("EMB_NAME"));
						bean.setMobileno((String) custdetails1.get(0).get("MOBILENO"));
						bean.setFname((String) custdetails1.get(0).get("FNAME"));
						bean.setMname((String) custdetails1.get(0).get("MNAME"));
						bean.setLname((String) custdetails1.get(0).get("LNAME"));
						bean.setDob((String) custdetails1.get(0).get("DOB"));
						bean.setMartialstatus((String) custdetails1.get(0).get("MARITAL_STATUS"));
						bean.setNationality((String) custdetails1.get(0).get("NATIONALITY"));
						bean.setSpousename((String) custdetails1.get(0).get("SPOUCE_NAME"));
						bean.setMothername((String) custdetails1.get(0).get("MOTHER_NAME"));
						bean.setFathername((String) custdetails1.get(0).get("FATHER_NAME"));
						bean.setEmail((String) custdetails1.get(0).get("E_MAIL"));
						bean.setPobox((String) custdetails1.get(0).get("P_PO_BOX"));
						bean.setPhouseno((String) custdetails1.get(0).get("P_HOUSE_NO"));
						bean.setPstname((String) custdetails1.get(0).get("P_STREET_NAME"));
						bean.setPwardname((String) custdetails1.get(0).get("P_WARD_NAME"));
						bean.setPcity((String) custdetails1.get(0).get("P_CITY"));
						bean.setPdist((String) custdetails1.get(0).get("P_DISTRICT"));
						String acctno = "", acctflag = "";
						List acctnum = this.getacctnumber(instid, hcardno.toString(), jdbctemplate);
						if (acctnum.isEmpty()) {
							Map hmp = new HashMap();
							hmp.put("ACCOUNTNO", "NOREC");
							acctnum.add(hmp);
							bean.setAcctno(acctnum);
						}
						bean.setAcctno(acctnum);
					}

				}

			}

			else {
				addActionError("Cannot edit this  Details,  Card Must be Active or Not Active Status !!");
				return "required_home";
			}
		}

		// orderrefNO
		if (!orderrefno.equalsIgnoreCase("") && cardno == "" && custid == "" && phoneno == "") {
			trace("======= editing  with orderref number =======");
			int checkcardexist = dao.checkcardinprocess("OREF", orderrefno, branchID, jdbctemplate);
			if (checkcardexist > 0) {
				addActionError(" Card is Edited Waiting for customer Edit Authorization   ");
				return "required_home";
			}
			trace("orderrefNO details found" + orderrefno);
			String encCardno2="";
			String ordercheck = dao.checkorderrefno(orderrefno, jdbctemplate);
			Integer result111 = Integer.valueOf(ordercheck);
			trace("level 5555");

			if (result111 >= 2) {
				trace(" Order Reference Number getting  Two or Above  Records Kindly Use Card Number");
				addActionError(" Order Reference Number getting  Two or Above  Records Kindly Use Card Number ");
				return "required_home";
			}
			if (!"0".equalsIgnoreCase(ordercheck)) {

				List<Map<String, Object>> custdetails = dao.getEmbossingName(orderrefno, "orderNO", jdbctemplate);
				if (!custdetails.isEmpty()) {
					bean.setEmbossingname((String) custdetails.get(0).get("EMB_NAME"));
					bean.setMobileno((String) custdetails.get(0).get("MOBILENO"));
					bean.setCustid((String) custdetails.get(0).get("CIN"));
					 encCardno2=(String) custdetails.get(0).get("ORG_CHN");
					bean.setCardno(encCardno2);
				}
				
				String clrCardno=padsssec.getCHN(CDPK, encCardno2);
				hcardno = padsssec.getHashedValue(clrCardno + instid);
				trace("here they check the beasn is ordercheck");
				if (bean.custid != null || bean.custid != "") {
					trace("ordercheck");
					List<Map<String, Object>> custdetails1 = dao.getCustDetails(bean.getCustid(), "order",
							bean.getCardno(), jdbctemplate);
					if (!custdetails1.isEmpty()) {
						System.out.println("sfdasfsa" + (String) custdetails1.get(0).get("MARITAL_STATUS"));
						bean.setEmbossingname((String) custdetails1.get(0).get("EMB_NAME"));
						bean.setMobileno((String) custdetails1.get(0).get("MOBILENO"));
						bean.setFname((String) custdetails1.get(0).get("FNAME"));
						bean.setMname((String) custdetails1.get(0).get("MNAME"));
						bean.setLname((String) custdetails1.get(0).get("LNAME"));
						bean.setDob((String) custdetails1.get(0).get("DOB"));
						bean.setMartialstatus((String) custdetails1.get(0).get("MARITAL_STATUS"));
						bean.setNationality((String) custdetails1.get(0).get("NATIONALITY"));
						bean.setSpousename((String) custdetails1.get(0).get("SPOUCE_NAME"));
						bean.setMothername((String) custdetails1.get(0).get("MOTHER_NAME"));
						bean.setFathername((String) custdetails1.get(0).get("FATHER_NAME"));
						bean.setEmail((String) custdetails1.get(0).get("E_MAIL"));
						bean.setPobox((String) custdetails1.get(0).get("P_PO_BOX"));
						bean.setPhouseno((String) custdetails1.get(0).get("P_HOUSE_NO"));
						bean.setPstname((String) custdetails1.get(0).get("P_STREET_NAME"));
						bean.setPwardname((String) custdetails1.get(0).get("P_WARD_NAME"));
						bean.setPcity((String) custdetails1.get(0).get("P_CITY"));
						bean.setPdist((String) custdetails1.get(0).get("P_DISTRICT"));
						String acctno = "", acctflag = "";
						List acctnum = this.getacctnumber(instid, hcardno.toString(), jdbctemplate);
						if (acctnum.isEmpty()) {
							Map hmp = new HashMap();
							hmp.put("ACCOUNTNO", "NOREC");
							acctnum.add(hmp);
							bean.setAcctno(acctnum);
						}
						bean.setAcctno(acctnum);
					}

				}

			} else {
				addActionError("Cannot edit this  Details,  Card Must be Active or Not Active Status !!");
				return "required_home";
			}

		}

		if (!custid.equalsIgnoreCase("") && cardno == "" && phoneno == "" && orderrefno == "") {
			trace("====== Editing with cin number =========");
			int checkcardexist = dao.checkcardinprocess("CUST", custid, branchID, jdbctemplate);
			if (checkcardexist > 0) {
				addActionError(" Card is Edited Waiting for customer Edit Authorization   ");
				return "required_home";
			}

			String validcheck = dao.checkValidWithCustid(custid, jdbctemplate);
			Integer result1111 = Integer.valueOf(validcheck);
			trace("level 5555");
			
			String hcardno1=dao.getValidHcardNoWithCustid(custid, jdbctemplate);

			if (result1111 >= 2) {
				trace(" Customer ID Number getting  Two or Above  Records Kindly Use Card Number");
				addActionError(" Customer ID Number getting  Two or Above  Records Kindly Use Card Number ");
				return "required_home";
			}
			if (!"0".equalsIgnoreCase(validcheck)) {
				List<Map<String, Object>> custdetails1 = dao.getCustDetails(custid, "CIN", bean.getCardno(),
						jdbctemplate);
				trace("custdetails1======  "+custdetails1);
				if (!custdetails1.isEmpty()) {
					bean.setCardno((String) custdetails1.get(0).get("ORG_CHN"));
					System.out.println("sfdasfsa" + (String) custdetails1.get(0).get("MARITAL_STATUS"));
					bean.setEmbossingname((String) custdetails1.get(0).get("EMB_NAME"));
					bean.setMobileno((String) custdetails1.get(0).get("MOBILENO"));
					bean.setFname((String) custdetails1.get(0).get("FNAME"));
					bean.setMname((String) custdetails1.get(0).get("MNAME"));
					bean.setLname((String) custdetails1.get(0).get("LNAME"));
					bean.setDob((String) custdetails1.get(0).get("DOB"));
					bean.setMartialstatus((String) custdetails1.get(0).get("MARITAL_STATUS"));
					bean.setNationality((String) custdetails1.get(0).get("NATIONALITY"));
					bean.setSpousename((String) custdetails1.get(0).get("SPOUCE_NAME"));
					bean.setMothername((String) custdetails1.get(0).get("MOTHER_NAME"));
					bean.setFathername((String) custdetails1.get(0).get("FATHER_NAME"));
					bean.setEmail((String) custdetails1.get(0).get("E_MAIL"));
					bean.setPobox((String) custdetails1.get(0).get("P_PO_BOX"));
					bean.setPhouseno((String) custdetails1.get(0).get("P_HOUSE_NO"));
					bean.setPstname((String) custdetails1.get(0).get("P_STREET_NAME"));
					bean.setPwardname((String) custdetails1.get(0).get("P_WARD_NAME"));
					bean.setPcity((String) custdetails1.get(0).get("P_CITY"));
					bean.setPdist((String) custdetails1.get(0).get("P_DISTRICT"));
					bean.setEncardno((String) custdetails1.get(0).get("ORG_CHN"));
					String acctno = "", acctflag = "";
					List acctnum = this.getacctnumber(instid, hcardno1, jdbctemplate);
					if (acctnum.isEmpty()) {
						Map hmp = new HashMap();
						hmp.put("ACCOUNTNO", "NOREC");
						acctnum.add(hmp);
						bean.setAcctno(acctnum);
					}
					bean.setAcctno(acctnum);
				}

			} else {
				addActionError("Cannot edit this  Details,  Card Must be Active or Not Active Status !!");
				return "required_home";
			}
		}
		return "editcustomer";
	}

	public String updateCustomerDetails() {
		trace("========= Coming to update the updateCustomerDetails() ==========");

		IfpTransObj transact = commondesc.myTranObject("CHANGEPASS", txManager);
		String cardno = bean.getCardno();

		trace(" update t ==== " + cardno);

		HttpSession session = getRequest().getSession();
		String userid = comUserId();
		String username=comUsername();
		String instid = comInstId();
		trace("cardnumber ===="+bean.getCardno());
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		StringBuffer hcardno = null;
		System.out.println("instid" + instid);

		String keyid = "";
		String EDPK = "";
		String CDPK = "";
		String clearCardno = "";
		List secList = null;
		String encCardno = "";
		Properties props = null;

		PadssSecurity padsssec = new PadssSecurity();

		try {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			props = getCommonDescProperty();
			EDPK = props.getProperty("EDPK");

			if (padssenable.equals("Y")) {
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String CDMK = ((String) map.get("DMK"));
						CDPK = padsssec.decryptDPK(CDMK, EDPK);

						clearCardno = padsssec.getCHN(CDPK, cardno);
						hcardno = padsssec.getHashedValue(clearCardno + instid);
						encCardno = padsssec.getECHN(CDPK, clearCardno);
					}
				}
			}

		} catch (Exception exce) {
			exce.printStackTrace();
		}

		/*
		 * if (bean.getCardno().length() == 16) { String keyid =
		 * commondesc.getSecurityKeyid(instid, jdbctemplate); PadssSecurity
		 * padsssec = new PadssSecurity();
		 * 
		 * Properties props = getCommonDescProperty(); String EDPK =
		 * props.getProperty("EDPK");
		 * 
		 * // String eDMK = "",eDPK=""; try { List secList =
		 * commondesc.getPADSSDetailById(keyid, jdbctemplate); Iterator secitr =
		 * secList.iterator(); while (secitr.hasNext()) { Map map = (Map)
		 * secitr.next(); String CDMK = ((String) map.get("DMK")); // eDPK =
		 * ((String)map.get("DPK")); String CDPK = padsssec.decryptDPK(CDMK,
		 * EDPK); hcardno = padsssec.getHashedValue(cardno + instid); cardno =
		 * padsssec.getECHN(CDPK, cardno); //hcardno =
		 * this.gethashedcardno(instid, hcardno.toString(), jdbctemplate);
		 * 
		 * } } catch (Exception e1) { e1.printStackTrace(); return
		 * "editcustomer"; } }
		 */
		try {
			trace("customerdetaisl  fname " + bean.getFname() + "\n  bean.getMname()" + bean.getMname()
					+ " \n bean.getLname() " + bean.getLname() + "\n dob" + bean.getDob() + " \n marrystatus "
					+ bean.getMartialstatus() + "\n bean.getNationality()" + bean.getNationality()
					+ "\n bean.getSpousename()" + bean.getSpousename() + " \n mohtername " + bean.getMothername()
					+ "\n bean.getFathername()" + bean.getFathername() + " \n" + bean.getMobileno()
					+ "\n bean.getEmail()" + bean.getEmail() + "\n bean.getPobox()" + bean.getPobox() + "\n phone no"
					+ bean.getPhouseno() + "\n pstname" + bean.getPstname() + "\n " + bean.getPwardname()
					+ "\n bean.getPcity()" + bean.getPcity() + "\n " + bean.getPdist());

			int count = dao.checkInprocessTable(bean, jdbctemplate);

			trace("count value:: " + count);

			if (count > 0) {
				addActionError(" Card is Edited Waiting for Customer Edit Authorization   ");
				return "required_home";
			}

			String acctno = "", cin = "", brcode = "", procode = "", SUB_PROD_ID = "", bin = "";
			List auditdetails = dao.getcardeditdetailsforauditlog(instid, encCardno, jdbctemplate);
			Iterator secitr = auditdetails.iterator();
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				acctno = ((String) map.get("ACCOUNT_NO"));
				cin = ((String) map.get("CIN"));
				brcode = ((String) map.get("BRANCH_CODE"));
				procode = ((String) map.get("PRODUCT_CODE"));
				SUB_PROD_ID = ((String) map.get("SUB_PROD_ID"));
				bin = ((String) map.get("BIN"));
			}

			trace("encCardno   " + encCardno + " hcardno  " + hcardno + "  clear card " + clearCardno);
			int updatename = dao.updateEmbNameinproc(bean, encCardno, userid, hcardno.toString(), jdbctemplate);
			// int updatenameinswitch =
			// dao.updateEmbNameinswitch(bean,cardno,hcardno,padssenable,jdbctemplate);

			int updatemobileno = dao.updatedetailincust(bean, encCardno, userid, jdbctemplate);
			// int updatemobilenoinswitch =
			// dao.updateMobileNoinswitch(bean,cardno,hcardno,padssenable,jdbctemplate);
			// if(updatename > 0 && updatemobileno > 0 && updatenameinswitch >0
			// && updatenameinswitch > 0){

			trace("updatename value " + updatename + "  updatemobileno ====  " + updatemobileno);
			if (updatename > 0 && updatemobileno > 0) {
				trace("====**********");
				try {
					String cond = "";
					if (padssenable.equals("Y")) {
						cond = "C";
					} else {
						cond = "C";
					}
					String mcardno = commondesc.getMaskedCardNoFromProd(instid, encCardno, cond, jdbctemplate);
					if (mcardno == null) {
						mcardno = cardno;
					}
					auditbean.setActmsg(
							"Modified the card number [ " + mcardno + " ] And Mobile Number" + bean.getMobileno());
					auditbean.setUsercode(userid);

					// added by gowtham_010819
					String pcode = null;
         
					auditbean.setProduct(pcode);
					auditbean.setSubproduct(SUB_PROD_ID);
					auditbean.setBin(bin);
					auditbean.setAuditactcode("0110");
					auditbean.setCardno(mcardno);
					String halfname = "";
					String fullname = bean.getFname() + bean.getLname();
					if (fullname.length() > 21) {
						halfname = fullname.substring(0, 20);
					}
					auditbean.setCustname(halfname);
					auditbean.setCin(cin);
					auditbean.setChecker("--");
					auditbean.setAccoutnno(acctno);
					auditbean.setCardcollectbranch(brcode);
					// auditbean.setCardnumber(order_refnum[i]);
					// commondesc.insertAuditTrail(instid, username, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in Customerupdate : " + audite.getMessage());
				}

				transact.txManager.commit(transact.status);
				addActionMessage("Customer Details Edited  successfully and Waiting for Authorization...");
			} else {
				transact.txManager.rollback(transact.status);
				addActionError("Fail to update the name...");

			}
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			e.printStackTrace();
		}
		return "required_home";
	}

	public String getCustomerDetails() {
		bean.setEmbossingname(getRequest().getParameter("embname"));
		bean.setMobileno(getRequest().getParameter("mobileno"));
		bean.setCardno(getRequest().getParameter("cardno"));
		return "editcustomer";
	}

	public String gethashedcardno(String instid, String hcardno, JdbcTemplate jdbcTemplate) {

		String getcustdetails = null;
		String getcarddetails = "select HCARD_NO from CARD_PRODUCTION_HASH  where INST_ID='" + instid
				+ "' and CARD_NO='" + hcardno + "'";
		enctrace(" get card list query : " + getcarddetails);
		getcustdetails = (String) jdbcTemplate.queryForObject(getcarddetails, String.class);
		return getcustdetails;

	}

	public String authCustDetails() {
		List pers_prodlist = null, br_list = null;
		// String inst_id =comInstId(session);
		String usertype = comuserType();
		String branch = comBranchId();

		String instid = comInstId();
		String temp = act;
		System.out.println(temp);
//		 session.setAttribute("CARDGEN_ACT", act);
	//	String session_act = (String) session.getAttribute("CARDGEN_ACT");
		String orderStatus = "15";
		// System.out.println("session_act " + session_act);
		try {
			System.out.println("Inst Id===>" + instid + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = getBranchCodefmProcess(instid, orderStatus, jdbctemplate);
				// commondesc.generateBranchList(inst_id, jdbctemplate);

				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
				}

			}
			pers_prodlist = getProductListBySelected(instid, orderStatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setCustomerauthproductlist(pers_prodlist);
				
			} else {
				System.out.println("No Product Details Found ");
				addActionError("No Product Details Found");
				trace(" No Product Details Found ");
			}

		} catch (Exception e) {
			addActionError(" Error While Fetching The Product Details  " + e.getMessage());
			trace("Exception : while fetching product details " + e.getMessage());
		}
		return "autheditcustdetails";
	}

	public String autheditgenorders() {

		HttpSession session = getRequest().getSession();

		String userid = comUserId();
		String branchID = branchID();
		try {
			String branch = getRequest().getParameter("branchcode");
			String cardtype = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String instid = comInstId();
			// String session_act = (String)
			// session.getAttribute("CARDGEN_ACT");
			// System.out.println("sss"+branch);
			String condi = "";
			if (!branchID.trim().equals("000")) {
				condi = " AND trim(CARD_COLLECT_BRANCH)='" + branchID.trim() + "'";
			} else if (!branch.equals("ALL")) {

				condi = "AND trim(BRANCH_CODE)='" + branch.trim() + "'";
			} else {
				condi = "";
			}

			String cardstatus = "15";
			// String mkckstatus = "P";
			List authcardorder;
			String dateflag = "MAKER_DATE";
			trace("Getting order list...");
			String condition = commondesc.filterCondition(cardtype, branch, fromdate, todate, dateflag);
			trace("CONDITION ======== " + condition);

			authcardorder = personaliseCardeeditauthlist(instid, cardstatus, condition, userid, condi, jdbctemplate);

			trace("authcardorder=====>" + authcardorder);
			if (authcardorder.isEmpty()) {
				addActionError("No Orders Found");
				trace("No order found...");
				// return autheditgenorders();
			} else {
				setAuthcardorder(authcardorder);
				session.setAttribute("curerr", "S");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not continue the processsssssss....");
			e.printStackTrace();
			trace("Exception : " + e.getMessage());

		}

		return "autheditcustlistorders";
	}

	public String authsavecustdetails() {
		trace("*************Customer Details  authorization *****************");
		enctrace("*************Customer Details   authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("CARDAUTH", txManager);
		String instid = comInstId();
		String userid = comUserId();

		// Personalizeorderdetails persorderdetails,bindetails,extradetails;
		String username = comUserId();
		String username1=comUsername();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		String remarks = getRequest().getParameter("reason");
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		trace("Total Orders Selected ===> " + order_refnum.length);

		// persorderdetails =
		// commondesc.gettingOrderdetailsByCin(instid,order_refnum[i].trim(),jdbctemplate);
		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			statusmsg = " Authorized Successfully...";
			err_msg = "Authorize";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			authstatus = "D";
			statusmsg = " De-Authorized Successfully ..";
			err_msg = "De-Authorize";
		}

		try {

			int cnt = 0;
			for (int i = 0; i < order_refnum.length; i++) {
				int check = 0, delcust = 0, checkdeauth = 0, checkdeauthcardproc = 0, checkdeauthcustproc = 0,
						updatecardprod = 0;
				trace("Selected Refnums ==>" + order_refnum[i]);
				bean.setCardno(order_refnum[i]);
				List getcarddetails = this.getCustomerinfo(order_refnum[i], jdbctemplate);
				if (!getcarddetails.isEmpty()) {
					Iterator carddet = getcarddetails.iterator();
					trace("carddetails coming" + order_refnum[i]);

					while (carddet.hasNext()) {
						Map map = (Map) carddet.next();

						bean.setEmbossingname((String) map.get("EMB_NAME"));
						bean.setMobileno((String) map.get("MOBILENO"));
						bean.setCustid((String) map.get("CIN"));
						bean.setFname((String) map.get("FNAME"));
						bean.setMname((String) map.get("MNAME"));
						bean.setLname((String) map.get("LNAME"));
						bean.setDob((String) map.get("DOB"));
						bean.setMartialstatus((String) map.get("MARITAL_STATUS"));
						bean.setNationality((String) map.get("NATIONALITY"));
						bean.setSpousename((String) map.get("SPOUCE_NAME"));
						bean.setMothername((String) map.get("MOTHER_NAME"));
						bean.setFathername((String) map.get("FATHER_NAME"));
						bean.setEmail((String) map.get("E_MAIL"));
						bean.setPobox((String) map.get("P_PO_BOX"));
						bean.setPhouseno((String) map.get("P_HOUSE_NO"));
						bean.setPstname((String) map.get("P_STREET_NAME"));
						bean.setPwardname((String) map.get("P_WARD_NAME"));
						bean.setPcity((String) map.get("P_CITY"));
						bean.setPdist((String) map.get("P_DISTRICT"));
					}
				} else {
					addActionError("Could not get Customer information");
					return authCustDetails();
				}

				StringBuffer hcardno = null;
				String encCardno = "";
				String clearCardNumber = null;
				String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
				if (padssenable.equals("Y")) {

					Properties props = getCommonDescProperty();
					String EDPK = props.getProperty("EDPK");

					String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					PadssSecurity padsssec = new PadssSecurity();
					// String eDMK = "",eDPK="";
					try {
						List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
						Iterator secitr = secList.iterator();
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							String CDMK = ((String) map.get("DMK"));
							String CDPK = padsssec.decryptDPK(CDMK, EDPK);
							// eDPK = ((String)map.get("DPK"));
							clearCardNumber = padsssec.getCHN(CDPK, order_refnum[i]);
							// encCardno =
							// padsssec.getECHN(CDPK,clearCardNumber);
							// System.out.println("getting cardnumber values " +
							// cardno);
							hcardno = padsssec.getHashedValue(clearCardNumber + instid);
							// hcardno = this.gethashedcardno(instid,
							// order_refnum[i], jdbctemplate);
						}
					} catch (Exception e1) {
						addActionError("Could not get Padss enable card number");
						return authCustDetails();
					}
				}
				trace("clear cardnumber === " + clearCardNumber + " hcarfnumber ===  " + hcardno
						+ " encCardnumber====   " + order_refnum[i]);
				System.out.println("gettin authorize details " + getRequest().getParameter("authorize"));
				if (getRequest().getParameter("authorize") != null) {

					trace("==== coming into authorize part======");
					int updatenameinprod = dao.updateEmbNameinproduction(bean, order_refnum[i], userid, jdbctemplate);
					int updatemobilenoincustinfo = dao.updatedetailincustinfo(bean, order_refnum[i], userid,
							padssenable, hcardno.toString(), jdbctemplate);
					int updatenameinswitch = 0;

					String custstatuscode = commondesc.getstatusfromezcardinfo(instid, hcardno.toString(),
							jdbctemplate);

					if ((custstatuscode.equals("99") || custstatuscode.equals("98") || custstatuscode.equals("263"))) {

						updatenameinswitch = dao.updatestatusinezcard(bean, order_refnum[i], hcardno.toString(),
								padssenable, jdbctemplate);

					}

					int updateezcustinswitch = dao.updatezcustinfoswitch(bean, order_refnum[i], hcardno.toString(),
							padssenable, jdbctemplate);
					trace("authCountValues" + updatenameinprod + "\n" + updatemobilenoincustinfo + "\n"
							+ updateezcustinswitch + " not otp gen \n" + updatenameinswitch + "for the order ref no"
							+ order_refnum[i]);
					if (updatenameinprod > 0 && updatemobilenoincustinfo > 0 && updateezcustinswitch > 0) {
						trace("authorize for custdetais income");

						String update_authdeauth_qury = "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid
								+ "' AND ORG_CHN='" + order_refnum[i].trim() + "'";
						// System.out.println("update_authdeauth_qury======>"+update_authdeauth_qury);
						// System.out.println("Before Update ===> "+cnt);
						enctrace("deleteing cardprocesss " + update_authdeauth_qury);
						// check = jdbctemplate.update(update_authdeauth_qury);

						String update_authPersHashTableQry = "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='"
								+ instid + "' AND HCARD_NO='" + hcardno.toString() + "' ";

						enctrace("deleteing  cardprocesss_hash " + update_authPersHashTableQry);

						String update_authdeauthcustinfo = "DELETE FROM CUSTOMERINFO_PROCESS WHERE INST_ID='" + instid
								+ "' AND CIN='" + bean.getCustid() + "'";
						enctrace("deleteing  custprocesss " + update_authdeauthcustinfo);
						// System.out.println("update_authdeauth_qury======>"+update_authdeauth_qury);
						// System.out.println("Before Update ===> "+cnt);
						int hashTableResult = jdbctemplate.update(update_authPersHashTableQry);
						check = jdbctemplate.update(update_authdeauth_qury);
						delcust = jdbctemplate.update(update_authdeauthcustinfo);
						System.out.println(" QUery Executed ==check===>    " + check);
						if (check > 0 && delcust > 0 && hashTableResult > 0) {
							cnt = cnt + 1;
							trace("After Update ===> " + cnt);
						}

					}

				} else {

					String update_deauth_perscard = "DELETE FROM  PERS_CARD_PROCESS WHERE INST_ID='" + instid
							+ "' AND ORG_CHN='" + order_refnum[i].trim() + "'";
					String update_deauth_custinfo = "DELETE FROM CUSTOMERINFO_PROCESS WHERE INST_ID='" + instid
							+ "' AND CIN='" + bean.getCustid() + "'";
					String update_ifdcard_remarks = "UPDATE CARD_PRODUCTION SET REMARKS='" + remarks + "',MAKER_ID='"
							+ userid + "' WHERE ORG_CHN='" + order_refnum[i].trim() + "'";
					String update_authPersHashTableQry = "DELETE FROM PERS_CARD_PROCESS_HASH WHERE INST_ID='" + instid
							+ "' AND HCARD_NO='" + hcardno.toString() + "' ";

					enctrace("deleteing  cardprocesss_hash " + update_authPersHashTableQry);

					// String update_deauth_qury = "UPDATE PERS_CARD_PROCESS SET
					// mkck_status='"+authstatus+"',REMARKS='"+remarks+"' WHERE
					// INST_ID='"+instid+"' AND
					// CARD_NO='"+order_refnum[i].trim()+"'";
					// System.out.println("update_authdeauth_qury======>"+update_authdeauth_qury);
					enctrace("deauthorized the cardproces " + update_deauth_perscard);
					enctrace("deauthorized the cust processs " + update_deauth_custinfo);
					enctrace("UPDATING INTO the card production " + update_ifdcard_remarks);

					// System.out.println("Before Update ===> "+cnt);
					int checkHashResult = jdbctemplate.update(update_authPersHashTableQry);
					checkdeauthcardproc = jdbctemplate.update(update_deauth_perscard);
					checkdeauthcustproc = jdbctemplate.update(update_deauth_custinfo);
					updatecardprod = jdbctemplate.update(update_ifdcard_remarks);
					if (checkdeauthcardproc > 0 && checkdeauthcustproc > 0 && updatecardprod > 0
							&& checkHashResult > 0) {
						cnt = cnt + 1;
						trace("After Update ===> " + cnt);
					}
				}

				/************* AUDIT BLOCK **************/
				try {
					String acctno = "", cin = "", brcode = "", procode = "", SUB_PROD_ID = "", bin = "";
					List auditdetails = dao.getcardeditdetailsforauditlog(instid, order_refnum[i], jdbctemplate);
					Iterator secitr = auditdetails.iterator();
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						acctno = ((String) map.get("ACCOUNT_NO"));
						// cin = ((String)map.get("CIN"));
						brcode = ((String) map.get("BRANCH_CODE"));
						procode = ((String) map.get("PRODUCT_CODE"));
						SUB_PROD_ID = ((String) map.get("SUB_PROD_ID"));
						bin = ((String) map.get("BIN"));
					}
					String mcardno = commondesc.getMaskedCardNoFromProd(instid, order_refnum[i], "C", jdbctemplate);
					if (mcardno == null) {
						mcardno = order_refnum[i];
					}
					auditbean.setActmsg("card modified is " + statusmsg + "  Card Number [ " + mcardno + " ]");
					// auditbean.setUsercode(username);
					auditbean.setAuditactcode("0110");

					// added by gowtham_010819
					String pcode = null;

					auditbean.setProduct(procode);
					auditbean.setSubproduct(SUB_PROD_ID);
					auditbean.setBin(bin);
					auditbean.setCardno(mcardno);
					auditbean.setRemarks(remarks);
					String halfname = "";
					String fullname = bean.getFname() + bean.getLname();
					if (fullname.length() > 21) {
						halfname = fullname.substring(0, 20);
					}
					auditbean.setCustname(halfname);
					auditbean.setCin(bean.getCustid());
					auditbean.setChecker(username1);
					auditbean.setAccoutnno(acctno);
					auditbean.setCardcollectbranch(brcode);
					// auditbean.setProduct(persorderdetails.product_code);
					// commondesc.insertAuditTrail(instid, userid, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username1, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/
			}
			trace("checking the orderreflength" + order_refnum.length);
			trace("checking the count" + cnt);
			if (order_refnum.length == cnt) {
				 //addActionMessage(String.valueOf(cnt) + " Card Registered Successfully .Waiting for Authorization ");
                 trace("statusmsg ====commit  "+statusmsg);
				addActionMessage(cnt + "  Order " + statusmsg);
				txManager.commit(transact.status);
				setAct((String) session.getAttribute("CARDGEN_ACT"));
				 return "required_home";
				//return authCustDetails();

			} else {
				addActionError(" Selected Orders Not " + err_msg + " Successfully");
				txManager.rollback(transact.status);
				setAct((String) session.getAttribute("CARDGEN_ACT"));
				return "required_home";

			}

		} catch (Exception e) {
			txManager.rollback(transact.status);
			addActionError(" Error While" + err_msg + " The Orders " + e.getMessage());
			trace("Exception : while process the card EDIT authorization : " + e.getMessage());
			setAct((String) session.getAttribute("CARDGEN_ACT"));
			return "required_home";
		}

	}

	public List getProductListBySelected(String instid, String cardStatus, JdbcTemplate jdbctemplate) throws Exception {
		// Condition For Each Stages
		String condition = null;

		if ("15".equals(cardStatus)) {
			condition = " AND CARD_STATUS='15' AND MKCK_STATUS IN ('M' ,'P')";
		}
	/*	} else if ("15".equals(cardStatus)) {
			condition = " AND CARD_STATUS='15' AND MKCK_STATUS='M' ";
		}*/
		String query = "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID='"
				+ instid + "' " + condition + " GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE ";
		enctrace(" get prod list : " + query);
		return (jdbctemplate.queryForList(query));
	}

	public List getBranchCodefmProcess(String instid, String orderStatus, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();
		query.append("select DISTINCT a.BRANCH_CODE,a.BRANCH_NAME FROM BRANCH_MASTER a,PERS_CARD_PROCESS b ");
		query.append("  WHERE a.INST_ID=b.INST_ID and trim(a.BRANCH_CODE)=trim(B.BRANCH_CODE) and ");
		query.append(
				" a.INST_ID='" + instid + "' AND b.CARD_STATUS='" + orderStatus + "' AND b.MKCK_STATUS IN('P','M') ");
		query.append(" ORDER BY a.BRANCH_CODE");

		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString());
		return branchList;
	}

	public List personaliseCardeeditauthlist(String instid, String cardstatus, String condition, String userid,
			String condi, JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String authcards_query =
		 * "select distinct(cp.order_ref_no) as ORDER_REF_NO,to_char(cp."
		 * +dateflag+
		 * ",'DD-MON-YYYY') as GENDATE,ud.USERNAME as USERNAME,co.card_quantity as COUNT,co.card_type_id as CARDID,co.sub_prod_id as SUBID,"
		 * +
		 * "co.product_code as PCODE,co.BRANCH_CODE as BRANCH_CODE,co.BIN as BIN,pd.product_name as PNAME from PERS_CARD_PROCESS cp,PERS_CARD_ORDER co,INSTPROD_DETAILS pd, IFG_USER_DETAILS ud "
		 * + "where cp.inst_id='"+instid+"' and cp.card_status ='"+cardstatus+
		 * "' and cp.mkck_status='"+mkckstatus+"' and cp.branch_code='"+brcode+
		 * "' and cp.bin='"+bin+
		 * "' and ud.userid = cp.maker_id and cp.order_ref_no = co.order_ref_no and "
		 * +
		 * "cp.inst_id = co.inst_id and  co.inst_id = pd.inst_id and cp.product_code = co.product_code and co.product_code = pd.product_code and "
		 * + "(to_date('"+fromdate+"','dd-mm-yyyy') <= cp."+dateflag+
		 * ") AND (to_date('"+todate+"','dd-mm-yyyy')+1 >= cp."+dateflag+
		 * ") order  by cp.order_ref_no";
		 * 
		 * 
		 * String authcards_query =
		 * "select ORDER_REF_NO,count(*) as COUNT,CART_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,MAKER_ID,to_char("
		 * +dateflag+",'DD-MON-YYYY') as  GENDATE," +
		 * "BIN from PERS_CARD_PROCESS where inst_id='"+instid+
		 * "' and CARD_STATUS='"+cardstatus+"' and MKCK_STATUS='"+mkckstatus+
		 * "' and (to_date('"+fromdate+"','dd-mm-yyyy') <= "+dateflag+
		 * ") AND (to_date('"+todate+"','dd-mm-yyyy')+1 >= "+dateflag+
		 * ") and bin='"+bin+"' " +
		 * "group by order_ref_no,CART_TYPE_ID, sub_prod_id, product_code,MAKER_ID,to_char("
		 * +dateflag+",'DD-MON-YYYY'),bin order by order_ref_no";
		 */

		/*
		 * String pingen = (String)session.getAttribute("PINGEN_TYPE"); trace(
		 * " PinGen Type-----> "+pingen);
		 */

		// String field_1="CARD_NO",orderflag="CARD_NO";

		// and MAKER_ID!='"+userid+"'
		String select_query = "select distinct(order_ref_no) as ORDER_REF_NO,ORG_CHN, CIN, MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,to_char(MAKER_DATE,'DD-MON-YY HH:MM:SS') as MAKER_DATE,MKCK_STATUS,branch_code,MOBILENO,MAKER_ID"
				+ " from PERS_CARD_PROCESS " + " where inst_id='" + instid + "' and CARD_STATUS='" + cardstatus + "' "
				+ condi + "AND  mkck_status IN ('P','M') " + condition + " order by order_ref_no";

		/*
		 * String select_query =
		 * "select distinct(order_ref_no) as ORDER_REF_NO, CIN, CARD_NO,HCARD_NO,MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,to_char(MAKER_DATE,'DD-MON-YY HH:MM:SS') as MAKER_DATE,MKCK_STATUS,branch_code,MOBILENO,MAKER_ID"
		 * + " from PERS_CARD_PROCESS " + " where inst_id='" + instid +
		 * "' and CARD_STATUS='" + cardstatus + "' " + condi +
		 * "AND  mkck_status IN ('P','M') " + condition +
		 * " order by order_ref_no";
		 */

		// AND CAF_REC_STATUS in ('S','A') This was added for maintenance
		// activity
		//// enctrace("3030authcards_query : "+select_query);
		enctrace("checking select query for authlist" + select_query);
		List persorderlist = jdbctemplate.queryForList(select_query);
		trace("The Query Result is ==========>" + persorderlist);
		ListIterator itr = persorderlist.listIterator();
		while (itr.hasNext()) {
			Map temp = (Map) itr.next();
			String refnum = (String) temp.get("ORDER_REF_NO");
			String prodcode = (String) temp.get("PRODUCT_CODE");
			String usercode = (String) temp.get("MAKER_ID");
			String binno = (String) temp.get("BIN");
			String embossname = (String) temp.get("EMB_NAME");

			// String cin = (String)temp.get("CIN");
			// String productdesc = getProductdesc(instid, binno,prodcode);
			String productdesc = commondesc.getProductdesc(instid, prodcode, jdbctemplate);
			trace("INstituoion ID " + instid + "User Code ===> " + usercode);
			String username = commondesc.getUserName(instid, usercode, jdbctemplate);
			String count = this.getCardcount(instid, refnum, cardstatus, "REFNUM", jdbctemplate);
			String mobile = (String) temp.get("MOBILENO");
			/*
			 * List
			 * embossname=getEmbossingName(instid,(String)temp.get("CIN"),"CIN",
			 * jdbctemplate); ListIterator lit = embossname.listIterator();
			 * while(lit.hasNext()){ Map hm = (Map) lit.next(); String
			 * embossname2 = (String)hm.get("EMB_NAME"); trace(
			 * "EBOSSNAME========== " +embossname2); hm.put("EMBOSSING_NAME",
			 * embossname2); itr.add(hm); }
			 */
			trace("111111111111111111");
			temp.put("SUBPRODDESC", commondesc.getSubProductdesc(instid, (String) temp.get("SUBPRODID"), jdbctemplate));
			trace("22222222222222222222");
			temp.put("CARDNO", (String) temp.get("CARD_NO"));
			trace("3333333333333333");
			temp.put("COUNT", count);
			trace("44444444444444444");
			temp.put("EMBOSSING_NAME", embossname);
			temp.put("PNAME", productdesc);
			trace("555555555555555555");
			temp.put("USERNAME", username);
			trace("66666666666666666");
			temp.put("MOBILE", mobile);
			itr.remove();
			itr.add(temp);

		}

		return persorderlist;
	}

	public List getCustomerinfo(String cardno, JdbcTemplate jdbctemplate) {

		String embnameqry = "";
		List custdetails = null;
		try {

			// embnameqry = "SELECT EMB_NAME,MOBILENO,CIN FROM CARD_PRODUCTION
			// WHERE "+cond+" AND ROWNUM=1";
			embnameqry = "SELECT A.EMB_NAME AS EMB_NAME,NVL(A.MOBILENO,'--') AS MOBILENO,NVL(B.FNAME,'--') AS FNAME,NVL(B.MNAME,'--') AS MNAME,NVL(B.LNAME,'--') AS LNAME,B.DOB AS DOB,NVL(B.MARITAL_STATUS,'--') AS MARITAL_STATUS,"
					+ "NVL(B.NATIONALITY,'--') AS NATIONALITY,NVL(B.DOCUMENT_PROVIDED,'--') AS DOCUMENT_PROVIDED ,NVL(B.DOCUMENT_NUMBER,'--') AS DOCUMENT_NUMBER,NVL(B.SPOUCE_NAME,'--') AS SPOUCE_NAME,"
					+ "NVL(B.MOTHER_NAME,'--') AS MOTHER_NAME,NVL(B.FATHER_NAME,'--') AS FATHER_NAME,NVL(B.E_MAIL,'--') AS E_MAIL,"
					+ "NVL(B.P_PO_BOX,'--') AS P_PO_BOX, NVL(B.P_HOUSE_NO,'--') AS P_HOUSE_NO,NVL(B.P_STREET_NAME,'--') AS P_STREET_NAME,"
					+ "NVL(B.P_WARD_NAME,'--') AS P_WARD_NAME,NVL(B.P_CITY,'--') AS P_CITY,NVL(B.P_DISTRICT,'--') AS P_DISTRICT,B.P_PHONE1"
					+ ",B.P_PHONE2,B.CIN FROM PERS_CARD_PROCESS A,CUSTOMERINFO_PROCESS B WHERE A.ORG_CHN='" + cardno
					+ "' AND B.CIN=A.CIN";

			enctrace("cin customer name edit-->" + embnameqry);
			custdetails = jdbctemplate.queryForList(embnameqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;
	}

	public String getCardcount(String instid, String refnum, String cardstatus, String flag,
			JdbcTemplate jdbcTemplate) {
		// String cafstatus = " AND CAF_REC_STATUS in('S','A','D')";
		String condtion = "ORDER_REF_NO";
		if (flag.equals("CARD")) {
			condtion = "CARD_NO";
			// cafstatus ="";
		}

		String count = "";

		String countqury = "select count(*) from PERS_CARD_PROCESS where INST_ID='" + instid + "' and " + condtion
				+ "='" + refnum + "' and CARD_STATUS='" + cardstatus + "' and MKCK_STATUS IN('P','M')";// +cafstatus;
		trace("=countqury====> " + countqury);
		int cardcount = jdbcTemplate.queryForInt(countqury);
		count = Integer.toString(cardcount);
		return count;
	}

	public List getacctnumber(String instid, String hcardno, JdbcTemplate jdbctemplate) throws Exception {
		List acctypelist = null;
		String acctypelistqry = "SELECT ACCOUNTNO,ACCOUNTFLAG FROM EZAUTHREL WHERE INSTID='" + instid + "' AND CHN='"
				+ hcardno + "'";
		enctrace("acctypelistqry::" + acctypelistqry);
		acctypelist = jdbctemplate.queryForList(acctypelistqry);
		return acctypelist;
	}

}
