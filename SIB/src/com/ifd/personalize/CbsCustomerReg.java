package com.ifd.personalize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import com.ifp.beans.AuditBeans;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

//import com.ifc.dao.CreditCustRegisterationDAO;
import com.ifd.beans.CbsCustomerRegBeans;
import com.ifd.dao.CbsCustomerRegisterDAO;
import com.ifd.dao.DebitCustomerRegisterDAO;
import com.ifp.Action.BaseAction;
import com.ifp.Action.LnkBinAction;
import com.ifd.fee.FeeConfigDAO;
import com.ifp.mail.EmailService;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;
import com.ifp.util.newPDFReportGenerator;
import com.opensymphony.xwork2.ModelDriven;

import connection.CBSConnection;

public class CbsCustomerReg extends BaseAction implements ModelDriven<CbsCustomerRegBeans> {

	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();
	// CreditCustRegisterationDAO creditdao = new CreditCustRegisterationDAO();
	final String ERRORHOME = "required_home";

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

	@Override
	public CbsCustomerRegBeans getModel() {
		// TODO Auto-generated method stub
		return cbscustregbean;
	}

	CbsCustomerRegBeans cbscustregbean = new CbsCustomerRegBeans();

	CbsCustomerRegisterDAO cbscustdao = new CbsCustomerRegisterDAO();

	public CbsCustomerRegBeans getCbscustregbean() {
		return cbscustregbean;
	}

	public void setCbscustregbean(CbsCustomerRegBeans cbscustregbean) {
		this.cbscustregbean = cbscustregbean;
	}

	public CbsCustomerRegisterDAO getCbscustdao() {
		return cbscustdao;
	}

	public void setCbscustdao(CbsCustomerRegisterDAO cbscustdao) {
		this.cbscustdao = cbscustdao;
	}

	public String comUserCode(HttpSession session) {
		String instid = (String) session.getAttribute("USERID");
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

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String tab2PersionalDetails() {
		System.out.println("tab2PersionalDetails called");
		return "tab2_home";
	}

	public String tab3ContactDetails() {
		System.out.println("tab3ContactDetails called");
		return "tab3_home";
	}

	public String cbsRegHome() {

		return "cbsreg_home";
	}

	public String cbsCustomerRegEntryList() throws SQLException {
		String cbsacct = getRequest().getParameter("cbsacct");
		String cbsidnum = getRequest().getParameter("cbsidnum");
		String seltype = getRequest().getParameter("seltype");
		String cbsqry = "";
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		CBSConnection cbscon = new CBSConnection();
		conn = cbscon.getCBSDBConnection();
		List<HashMap<String, Object>> list = new ArrayList<>();
		;
		List<HashMap<String, Object>> list1 = new ArrayList<>();
		;
		try {
			if (seltype.equals("2")) {
				cbsqry = "select A.BRNCD as BRANCH_CODE,A.BRNCD||A.MODCD||LPAD(A.ACNO,6,'0')||'0'||A.CHKDGT as CUST_AC_NO,"
						+ " A.CIFKEY as CUST_NO,A.FCCD as CCY,"
						+ " B.fsname as FIRST_NAME,B.lsname as LAST_NAME,TO_CHAR(B.birthdt,'DD-MM-YY') as DATE_OF_BIRTH,B.SEX as SEX,"
						+ " B.nation as NATIONALITY,B.fsname as AC_DESC,B.mdname as MIDDLE_NAME,"
						+ " (select phonenum from SWCARDNM where other_id= (select CIFKEY from SA01MAST where ACNO=A.ACNO and rownum=1)) as TELEPHONE"
						+ " from SA01MAST A,cf01cif B where A.CIFKEY=B.CIFKEY and B.IDNO= ?  and A.ACSTS=?";
				trace("Qry value " + cbsqry);
				ps = conn.prepareStatement(cbsqry);
				ps.setString(1, cbsidnum);
				ps.setString(2, "A");
				rs = ps.executeQuery();
				System.out.println("rrs-->" + rs);

				if (!rs.next()) {
					addActionError("No Records Found");
					return "required_home";

				} else {
					do {
						list = convertResultSetToList(rs);

					} while (rs.next());
				}
				cbscustregbean.setCbsreglist(list);
			}
			if (seltype.equals("1")) {
				HttpSession session = getRequest().getSession();
				String instid = (String) session.getAttribute("Instname");

				cardcollectbranchlist = commondesc.generateBranchList(instid, jdbctemplate);

				cbscustregbean.setBranchlist(cardcollectbranchlist);

				trace("Getting Product List ....");
				List productlist = commondesc.getProductList(instid, jdbctemplate, cbscustregbean.getBranchcode());
				trace("Got Product List" + productlist);

				cbscustregbean.setProductlist(productlist);

				String cbsrecordList = "select A.BRNCD as BRANCH_CODE,A.BRNCD||A.MODCD||LPAD(A.ACNO,6,'0')||'0'||A.CHKDGT as CUST_AC_NO,"
						+ " A.CIFKEY as CUST_NO,DECODE(A.FCCD,'USD','840','LAK','418','EUR','978','THB','764') as CCY,"
						+ " DECODE(A.CRLINE,'SA','10','CA','20') as ACC_STMT_TYPE,A.ACTYP as ACCT_SUB_TYPE,"
						+ " B.fsname as FIRST_NAME,B.lsname as LAST_NAME,TO_CHAR(B.birthdt,'DD-MM-YY') as DATE_OF_BIRTH,B.SEX as SEX,"
						+ " B.nation as NATIONALITY,B.fsname as AC_DESC,B.mdname as MIDDLE_NAME,"
						+ " (select phonenum from SWCARDNM where other_id= (select CIFKEY from SA01MAST where ACNO=A.ACNO and rownum=1)) as TELEPHONE,"
						+ " (select addr1 from CF01ADDR where CIFKEY=A.CIFKEY and addrtyp='6' and rownum=1) as E_MAIL,"
						+ " (select addr1 from CF01ADDR where CIFKEY=A.CIFKEY and addrtyp='1' and rownum=1) as P_ADDRESS1,"
						+ " (select addr2 from CF01ADDR where CIFKEY=A.CIFKEY and rownum=1) as P_ADDRESS2"
						+ " from SA01MAST A,cf01cif B where A.CIFKEY=B.CIFKEY  " + " and A.ACNO=? and A.ACSTS=?";
				cbsacct = cbsacct.substring(7, 13);
				trace("Qry value " + cbsrecordList);
				ps = conn.prepareStatement(cbsrecordList);
				// ps.setString(1, cbsidnum);
				// ps.setString(2, cbsidnum);
				ps.setString(1, cbsacct);
				ps.setString(2, "A");

				rs = ps.executeQuery();

				if (!rs.next()) {
					addActionError("No Records Found");
					return "cbsreg_home";

				} else {

					do {

						cbscustregbean.setBranchlist(getBranchDesc(instid, rs.getString("BRANCH_CODE"), jdbctemplate));
						cbscustregbean.setEmbname(rs.getString("AC_DESC"));
						cbscustregbean.setEncname(rs.getString("AC_DESC"));
						cbscustregbean.setCustomerid(rs.getString("CUST_NO"));

						// System.out.println("cbscustregbean.getApplicationid()"
						// +cbscustregbean.getApplicationid() );
						trace("getting cust no in cbs" + rs.getString("CUST_NO"));
						// cbscustregbean.setAccttypel(rs.getString("AC_STMT_TYPE"));

						trace("setting accttypelist");
						String accttype = rs.getString("ACC_STMT_TYPE");
						/*
						 * if("S".equalsIgnoreCase(accttype.trim())){ accttype =
						 * "10"; }else{ accttype = "20"; }
						 */
						cbscustregbean.setAccttypelist(this.getAcctTypeList(instid, accttype, jdbctemplate));

						trace("setting getAcctSubTypeList ");
						List subaccounttypelist = this.getAcctSubTypeList(instid, rs.getString("ACCT_SUB_TYPE"),
								jdbctemplate);
						cbscustregbean.setAccuntsubtypelist(subaccounttypelist);

						trace("setting currency list");
						List currencylist = this.getCurList(instid, rs.getString("CCY"), jdbctemplate);
						cbscustregbean.setCurrencylist(currencylist);

						cbscustregbean.setAccountnovalue(rs.getString("CUST_AC_NO"));

						cbscustregbean.setFirstname(rs.getString("FIRST_NAME"));
						cbscustregbean.setMiddlename(rs.getString("MIDDLE_NAME"));
						cbscustregbean.setLastname(rs.getString("LAST_NAME"));
						cbscustregbean.setDob(rs.getString("DATE_OF_BIRTH"));
						cbscustregbean.setGender(rs.getString("SEX"));
						cbscustregbean.setNationality(rs.getString("NATIONALITY"));
						cbscustregbean.setMobile(rs.getString("TELEPHONE"));
						cbscustregbean.setEmail(rs.getString("E_MAIL"));
						cbscustregbean.setP_poxbox(rs.getString("P_ADDRESS1"));
						cbscustregbean.setP_houseno(rs.getString("P_ADDRESS2"));
						cbscustregbean.setDocumentnumber(rs.getString("CUST_NO"));

						// System.out.println("getting firstname " +
						// rs.getString("FIRST_NAME"));

						System.out.println("getting branchcode" + rs.getString("SEX"));
					} while (rs.next());
				}
				return "cbsreg_entry";
			}

			System.out.println(cbscustregbean.cbsreglist);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return "cbsreglist";
	}

	public List<HashMap<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		while (rs.next()) {
			HashMap<String, Object> row = new HashMap<String, Object>(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}

	public String cbsCustomerRegEntry() throws Exception {
		// getRequest().setCharacterEncoding("UTF-8");
		HttpSession session = getRequest().getSession();
		// String instid = (String)session.getAttribute("Instname");

		String instid = comInstId();

		String accountno = getRequest().getParameter("cbsacct");

		System.out.println("accountNumber ::::  " + accountno);

		trace("instid:::  " + instid);
		/*
		 * String orderrefno = commondesc.generateorderRefno(instid,
		 * jdbctemplate); trace("Generated order reference number is : " +
		 * orderrefno);
		 * 
		 * if(cbscustregbean.getCinidbasedon().equals("CBS")) { trace(
		 * "customerid based on cbs true");
		 * customerid=cbscustregbean.getCustomeridno(); } else { trace(
		 * "customerid based on cbs false"); customerid =
		 * commondesc.cinnumberGeneratoer(instid,jdbctemplate);
		 * 
		 * } trace("Generated customer id from  : "
		 * +cbscustregbean.getCinidbasedon()+"::" + customerid);
		 */

		cardcollectbranchlist = commondesc.generateBranchList(instid, jdbctemplate);

		cbscustregbean.setBranchlist(cardcollectbranchlist);
		trace("Getting Product List ....");
		List productlist = commondesc.getProductList(instid, jdbctemplate, cbscustregbean.getBranchcode());
		trace("Got Product List" + productlist);

		cbscustregbean.setProductlist(productlist);

		// String cbsrefnum[] = getRequest().getParameterValues("cbsrefnum");
		// System.out.println(cbsrefnum[0]);
		/*
		 * if(cbsrefnum.length > 1){ addActionError(
		 * "You have selected more than one account, Please select only one account for registration !!!"
		 * ); return "required_home"; }
		 */
		// String val[] = cbsrefnum[0].toString().split("~");
		// String cbsacct = val[0];
		// String cbsidnum = val[1];

		// cbsacct = cbsacct.substring(7, 13);
		// cbsacct = cbsacct.replace("0", "");

		// System.out.println("cbsacct modified--->"+cbsacct);

		URL url = new URL("http://localhost:9036/getCbsInfo?accountno=" + accountno);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/json");

		if (con.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP Error code : " + con.getResponseCode());
		}

		InputStreamReader input = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(input);

		String output = "";
		String reslut = "";

		StringBuilder sb = new StringBuilder();

		while ((output = br.readLine()) != null) {

		  //   reslut = output.replace("[", "").replace("]", "");

			sb.append(output);
		}

		String out = sb.toString();
		trace("json values ::::   " +  out);

		
	
	//	cbscustregbean.setBranchlist(getBranchDesc(instid, jdbctemplate));
		
		
		
		


		
/*
		Connection conn = null;
		ResultSet rs = null;
		CBSConnection cbscon = new CBSConnection();
		conn = cbscon.getCBSDBConnection();
		System.out.println("cbs connection getting");*/
		try {
			
			
			JSONObject json = new JSONObject(out);
			
			String cin=  (String) json.get("CIN");
			String accNo=  (String) json.get("AccountNumber");
			 String accCurrency=(String) json.get("AccountCurrency");
			String accountType= (String) json.get("AccountType");
			String  fname=(String) json.get("FirstName");
			String lanme=  (String) json.get("LastName");
			String add1=(String) json.get("Address1");
			String add2=  (String) json.get("Address2");
			String cardType= (String) json.get("CardType");
            String CCY=(String)json.get("CCY");
			trace("custNumber :::  " + json);
			
			cbscustregbean.setCustomerid(cin);
			cbscustregbean.setAccttypelist(this.getAcctTypeList(instid, accountType, jdbctemplate));
			cbscustregbean.setEmbname(fname);
			cbscustregbean.setEncname(lanme);
			cbscustregbean.setAccountnovalue(accNo);
			List currencylist = this.getCurList(instid, CCY, jdbctemplate);
			cbscustregbean.setCurrencylist(currencylist);
			
			
			List subaccounttypelist = this.getAcctSubTypeList(instid,accountType ,jdbctemplate);;
			cbscustregbean.setAccuntsubtypelist(subaccounttypelist);

			/*String cbsrecordList = "select A.BRNCD as BRANCH_CODE,A.BRNCD||A.MODCD||LPAD(A.ACNO,6,'0')||'0'||A.CHKDGT as CUST_AC_NO,"
					+ " A.CIFKEY as CUST_NO,DECODE(A.FCCD,'USD','840','LAK','418','EUR','978','THB','764') as CCY,"
					+ " DECODE(A.CRLINE,'SA','10','CA','20') as ACC_STMT_TYPE,A.ACTYP as ACCT_SUB_TYPE,"
					+ " B.fsname as FIRST_NAME,B.lsname as LAST_NAME,TO_CHAR(B.birthdt,'DD-MM-YY') as DATE_OF_BIRTH,B.SEX as SEX,"
					+ " B.nation as NATIONALITY,B.fsname as AC_DESC,B.mdname as MIDDLE_NAME,"
					+ " (select phonenum from SWCARDNM where other_id= ?) as TELEPHONE,"
					+ " (select addr1 from CF01ADDR where CIFKEY=A.CIFKEY and addrtyp='6' and rownum=1) as E_MAIL,"
					+ " (select addr1 from CF01ADDR where CIFKEY=A.CIFKEY and addrtyp='1' and rownum=1) as P_ADDRESS1,"
					+ " (select addr2 from CF01ADDR where CIFKEY=A.CIFKEY and rownum=1) as P_ADDRESS2"
					+ " from SA01MAST A,cf01cif B where A.CIFKEY=B.CIFKEY and B.IDNO= ? "
					+ " and A.ACNO=? and A.ACSTS=?";

			trace("Qry value " + cbsrecordList);
			PreparedStatement ps = conn.prepareStatement(cbsrecordList);
			
			 * ps.setString(1, cbsidnum); ps.setString(2, cbsidnum);
			 * ps.setString(3, cbsacct); ps.setString(4, "A");
			 
			rs = ps.executeQuery();

			if (!rs.next()) {
				addActionError("No Records Found");
				return "cbsreg_home";

			} else {

				do {

					cbscustregbean.setBranchlist(getBranchDesc(instid, rs.getString("BRANCH_CODE"), jdbctemplate));
					cbscustregbean.setEmbname(rs.getString("AC_DESC"));
					cbscustregbean.setEncname(rs.getString("AC_DESC"));
					cbscustregbean.setCustomerid(rs.getString("CUST_NO"));

					// System.out.println("cbscustregbean.getApplicationid()"
					// +cbscustregbean.getApplicationid() );
					trace("getting cust no in cbs" + rs.getString("CUST_NO"));
					// cbscustregbean.setAccttypel(rs.getString("AC_STMT_TYPE"));

					trace("setting accttypelist");
					String accttype = rs.getString("ACC_STMT_TYPE");
					
					 * if("S".equalsIgnoreCase(accttype.trim())){ accttype =
					 * "10"; }else{ accttype = "20"; }
					 
					cbscustregbean.setAccttypelist(this.getAcctTypeList(instid, accttype, jdbctemplate));

					trace("setting getAcctSubTypeList ");
					List subaccounttypelist = this.getAcctSubTypeList(instid, rs.getString("ACCT_SUB_TYPE"),
							jdbctemplate);
					cbscustregbean.setAccuntsubtypelist(subaccounttypelist);

					trace("setting currency list");
					List currencylist = this.getCurList(instid, rs.getString("CCY"), jdbctemplate);
					cbscustregbean.setCurrencylist(currencylist);

					cbscustregbean.setAccountnovalue(rs.getString("CUST_AC_NO"));

					cbscustregbean.setFirstname(rs.getString("FIRST_NAME"));
					cbscustregbean.setMiddlename(rs.getString("MIDDLE_NAME"));
					cbscustregbean.setLastname(rs.getString("LAST_NAME"));
					cbscustregbean.setDob(rs.getString("DATE_OF_BIRTH"));
					cbscustregbean.setGender(rs.getString("SEX"));
					cbscustregbean.setNationality(rs.getString("NATIONALITY"));
					cbscustregbean.setMobile(rs.getString("TELEPHONE"));
					cbscustregbean.setEmail(rs.getString("E_MAIL"));
					cbscustregbean.setP_poxbox(rs.getString("P_ADDRESS1"));
					cbscustregbean.setP_houseno(rs.getString("P_ADDRESS2"));
					cbscustregbean.setDocumentnumber(rs.getString("CUST_NO"));

					// System.out.println("getting firstname " +
					// rs.getString("FIRST_NAME"));
					List documentlist = commondesc.gettingDocumnettype(instid, jdbctemplate);
					if (!documentlist.isEmpty()) {
						trace("Got list of master document...:" + documentlist.size());
						cbscustregbean.setDocumentlist(documentlist);
					}

					System.out.println("getting branchcode" + rs.getString("SEX"));
				} while (rs.next());
			}*/
		} catch (SQLException e) {
			e.getMessage();
			e.printStackTrace();

		} finally {
			//conn.close();
			//System.out.println("here i am" + rs);
		}
		return "cbsreg_entry";
	}

	public void checkCustomerIdExist() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String customerid = getRequest().getParameter("customerid");
		String result = "";
		int res = commondesc.checkCustomerexist(instid, customerid, "PERS_CARD_ORDER", jdbctemplate);
		if (res > 0) {
			getResponse().getWriter().write("EXIST");
		} else {
			getResponse().getWriter().write("NEW");
		}
	}

	public void checkAccountNoExist() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String accountnovalue = getRequest().getParameter("accountnovalue");
		String result = "";
		int res = commondesc.checkAccountNoexist(instid, accountnovalue, jdbctemplate);
		if (res > 0) {
			getResponse().getWriter().write("EXIST");
		} else {
			getResponse().getWriter().write("NEW");
		}

	}

	public List getBranchDesc(String instid, String branchcode, JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String qrydesc =
		 * "SELECT BRANCH_CODE,BRANCH_NAME  FROM BRANCH_MASTER WHERE INST_ID='"
		 * +instid+"' and BRANCH_CODE='"+branchcode+"' and rownum <= 1"; trace(
		 * "Branch desc : " + qrydesc ); List desc_detail =
		 * jdbctemplate.queryForList(qrydesc);
		 */

		// by gowtham
		String qrydesc = "SELECT BRANCH_CODE,BRANCH_NAME  FROM BRANCH_MASTER WHERE INST_ID=?  and BRANCH_CODE=? and rownum <=?";
		trace("Branch desc : " + qrydesc);
		List desc_detail = jdbctemplate.queryForList(qrydesc, new Object[] { instid, branchcode, "1" });

		return desc_detail;
	}

	public List getAcctTypeList(String instid, String accounttypeid, JdbcTemplate jdbctemplate) throws Exception {
		List acctypelist = null;

		/*
		 * String acctypelistqry =
		 * "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE AUTH_CODE='1' AND INST_ID='"
		 * +instid+"' and ACCTTYPEID='"+accounttypeid+"'"; enctrace(
		 * "getting acctypedescription" + acctypelistqry); acctypelist =
		 * jdbctemplate.queryForList(acctypelistqry);
		 */

		/// by gowtham
		String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE AUTH_CODE=? AND INST_ID=? and ACCTTYPEID=? ";
		enctrace("getting acctypedescription" + acctypelistqry);
		acctypelist = jdbctemplate.queryForList(acctypelistqry, new Object[] { "1", instid, accounttypeid });

		return acctypelist;
	}

	public List getAcctSubTypeList(String instid, String accountsubtypeid, JdbcTemplate jdbctemplate) throws Exception {
		List acctypelist = null;

		/*
		 * String accsubtypelistqry =
		 * "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE AUTH_CODE = '1' AND INST_ID='"
		 * +instid+"' AND ACCTSUBTYPEID='"+accountsubtypeid+"'";
		 * enctrace("accsubtypelistqry:::"+accsubtypelistqry); acctypelist =
		 * jdbctemplate.queryForList(accsubtypelistqry);
		 */
		
		String accsubtypelistqry = "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE AUTH_CODE = '1' "
				+ "AND INST_ID='"+instid+"'  AND ACCTTYPEID='"+accountsubtypeid+"'";
		enctrace("accsubtypelistqry:::" + accsubtypelistqry);
		
		acctypelist=jdbctemplate.queryForList(accsubtypelistqry);
		// by gowtham-220819
/*		String accsubtypelistqry = "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE AUTH_CODE = ? AND INST_ID=? AND ACCTSUBTYPEID=?";
		enctrace("accsubtypelistqry:::" + accsubtypelistqry);
		acctypelist = jdbctemplate.queryForList(accsubtypelistqry, new Object[] { "1", instid, accountsubtypeid });*/

		return acctypelist;
	}

	public List getCurList(String instid, String currcode, JdbcTemplate jdbctemplate) throws Exception {
		List curlist = null;

		/*
		 * String curDetails_query =
		 * "SELECT NUMERIC_CODE, CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE='"
		 * +currcode+"' AND AUTH_CODE='1'  "; enctrace("curDetails_query :" +
		 * curDetails_query ); curlist =
		 * jdbctemplate.queryForList(curDetails_query );
		 */

		// by gowtham-220819
		String curDetails_query = "SELECT NUMERIC_CODE, CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE=? AND AUTH_CODE=?  ";
		enctrace("curDetails_query :" + curDetails_query);
		curlist = jdbctemplate.queryForList(curDetails_query, new Object[] { currcode, "1" });

		return curlist;
	}

	public String saveProductInformation() throws Exception {

		trace("saveProductInformation started....");
		HttpSession session = getRequest().getSession();

		String instid = (String) session.getAttribute("Instname");
		String usercode = comUserCode(session);
		String username = comUsername();
		IfpTransObj transact = commondesc.myTranObject("REF", txManager);
		int cardorderinsert = -1;
		int appidinsert = -1;
		int updateref = -1;
		int custinfoinsert = -1;
		int acctinfo = -1;

		int updatecustlevel = -1;
		int updcustdetails = -1;
		int updateacctdetails = -1;

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		Iterator itr1 = null;
		String customerid = getRequest().getParameter("customerid");
		/*
		 * String tabvalue = getRequest().getParameter("tabvalue1"); if
		 * (tabvalue.equals("2")); {
		 * 
		 * cbscustregbean.setReglevel("2");
		 * cbscustregbean.setTab2Status("true");
		 * 
		 * 
		 * }
		 */

		// String productcode =getRequest().getParameter("productcode");
		// System.out.println("cbscustregbean.getProductcode() "+
		// cbscustregbean.getProductcode());

		/*
		 * cbscustregbean.setTab3Status("true");
		 * cbscustregbean.setReglevel("3");
		 * cbscustregbean.setTab4Status("true");
		 */

		try {

			String orderrefno = commondesc.generateorderRefno(instid, jdbctemplate);
			trace("Generated order reference number is : " + orderrefno);

			/*
			 * if(cbscustregbean.getCinidbasedon().equals("CBS")) { trace(
			 * "customerid based on cbs true");
			 * customerid=cbscustregbean.getCustomeridno(); } else { trace(
			 * "customerid based on cbs false"); customerid =
			 * commondesc.cinnumberGeneratoer(instid,jdbctemplate);
			 * 
			 * }
			 */
			// trace("Generated customer id from :" + customerid);

			customerid = commondesc.cinnumberGeneratoer(instid, jdbctemplate);
			String cardtypeid = commondesc.getCardType(instid, cbscustregbean.getProductcode(), jdbctemplate);
			trace("Getting cardtype id ...got : " + cardtypeid);
			if (cardtypeid == null) {
				addActionError("Could not get card type id .....");
				return ERRORHOME;
			}
			cbscustregbean.setCardtypeid(cardtypeid);

			String bin = commondesc.getBin(instid, cbscustregbean.getProductcode(), jdbctemplate);
			if (bin == null) {
				addActionError("Could not get bin code for the product");
				return ERRORHOME;
			}
			cbscustregbean.setBin(bin);
			// String applicationid =
			// cbscustregbean.getEmbname()+"-"+orderrefno;
			String applicationid = orderrefno;
			trace("applicationid---" + applicationid);
			trace("hello");
			cbscustregbean.setUsercode(usercode);

			String limitBasedon = commondesc.getLimitBasedOn(instid, cbscustregbean.getLimitid(), jdbctemplate);
			trace("limit based on ....." + limitBasedon);
			cbscustregbean.setLimitbasedon(limitBasedon);

			cbscustregbean.setRenewalflag(getRequest().getParameter("renewalflag"));
			trace("renewalflag ....." + getRequest().getParameter("renewalflag"));
			// String reglevel = creditdao.getCustomerRegLevel(instid,
			// orderrefno, jdbctemplate);
			// trace("Getting reglevel....got : " + reglevel );

			appidinsert = cbscustdao.insertCustomerRegisterationLevel(instid, orderrefno, "4", cbscustregbean,
					jdbctemplate);

			cardorderinsert = cbscustdao.insertCardOrderPersonal(instid, orderrefno, customerid, cbscustregbean,
					jdbctemplate);

			trace("Inserting order reference details...got : " + cardorderinsert);

			/*
			 * String custexistqry =
			 * "SELECT COUNT(1) AS CNT FROM CUSTOMERINFO WHERE CIN='"
			 * +customerid+"'"; enctrace("custexistqry-->"+custexistqry); String
			 * custexist = (String)jdbctemplate.queryForObject(custexistqry,
			 * String.class);
			 */

			// BY GOWTHAM-220819
			String custexistqry = "SELECT COUNT(1) AS CNT FROM CUSTOMERINFO WHERE CIN='" + customerid + "'";
			enctrace("custexistqry-->" + custexistqry);
			String custexist = (String) jdbctemplate.queryForObject(custexistqry, new Object[] { customerid },
					String.class);

			if ("0".equalsIgnoreCase(custexist)) {
				custinfoinsert = cbscustdao.insertCustomerDetails(instid, orderrefno, customerid, usercode,
						jdbctemplate);
				updcustdetails = cbscustdao.updateCustomerContactPersionalDetails(instid, orderrefno, usercode, "4",
						cbscustregbean, jdbctemplate);
			}

			/*
			 * String acctexistqry =
			 * "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"
			 * +cbscustregbean.getAccountnovalue()+"' AND CIN='"+customerid+"'";
			 * enctrace("acctexistqry-->"+acctexistqry); String acctexist =
			 * (String)jdbctemplate.queryForObject(acctexistqry, String.class);
			 */

			// by gowtham-220819
			String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO=? AND CIN=?'";
			enctrace("acctexistqry-->" + acctexistqry);
			String acctexist = (String) jdbctemplate.queryForObject(acctexistqry,
					new Object[] { cbscustregbean.getAccountnovalue(), customerid }, String.class);
			if ("0".equalsIgnoreCase(acctexist)) {
				acctinfo = cbscustdao.insertAccountInfo(instid, orderrefno, limitBasedon, customerid, usercode,
						cbscustregbean, jdbctemplate);
			}

			cbscustregbean.setUsercode(usercode);

			String updaterefqry = commondesc.updateOrderrefnumcount(instid);
			updateref = jdbctemplate.update(updaterefqry, new Object[] { instid });

			System.out.println(
					"referance::::" + cardorderinsert + updateref + custinfoinsert + acctinfo + updcustdetails);

			if (cardorderinsert == 1 && appidinsert == 1 && updateref == 1 && acctinfo == 1) {

				/*
				 * addActionMessage(
				 * "Success !!...Generate Card Reference Number is : "
				 * +orderrefno );
				 */
				// cbscustregbean.setReglevel("2");
				/* cbscustregbean.setApplicationid(applicationid); */

				/*
				 * cbscustregbean.setBranchcode(cbscustregbean.getBranchcode());
				 * cbscustregbean.setEmbname(cbscustregbean.getEmbname());
				 * cbscustregbean.setEncname(cbscustregbean.getEncname());
				 * cbscustregbean.setProductcode(cbscustregbean.getProductcode()
				 * );
				 * cbscustregbean.setSubproduct(cbscustregbean.getSubproduct());
				 * cbscustregbean.setLimitbasedon(cbscustregbean.getLimitbasedon
				 * ()); cbscustregbean.setLimitid(cbscustregbean.getLimitid());
				 * cbscustregbean.setFeecode(cbscustregbean.getFeecode());
				 * cbscustregbean.setCustomeridno(cbscustregbean.getCustomeridno
				 * ());
				 * 
				 * trace("getBranchcode::"+cbscustregbean.getBranchcode());
				 * trace("getEmbname::"+cbscustregbean.getEmbname());
				 * trace("getEncname:::"+cbscustregbean.getEncname());
				 * trace("getProductcode:::"+cbscustregbean.getProductcode());
				 * trace("getSubproduct:::"+cbscustregbean.getSubproduct());
				 * trace("getLimitbasedon:::"+cbscustregbean.getLimitbasedon());
				 * trace("getFeecode:::"+cbscustregbean.getFeecode());
				 * trace("getCustomeridno:::"+cbscustregbean.getCustomeridno());
				 * 
				 * trace("getAccounttypevalue:::"+cbscustregbean.
				 * getAccounttypevalue());
				 * trace("getAccountsubtypevalue:::"+cbscustregbean.
				 * getAccountsubtypevalue());
				 * trace("getTab2_currency:::"+cbscustregbean.getTab2_currency()
				 * );
				 * trace("getAccountnovalue:::"+cbscustregbean.getAccountnovalue
				 * ());
				 */

				List branchlist = commondesc.generateBranchList(instid, jdbctemplate);
				if (branchlist.isEmpty()) {
					addActionError("Could not get branch list");
					return "required_home";
				}
				cbscustregbean.setBranchlist(branchlist);

				/*
				 * trace("Getting Product List ...."); List productlist =
				 * this.getProductList(instid, jdbctemplate,
				 * cbscustregbean.getBranchcode()); trace("Got Product List"
				 * +productlist);
				 * 
				 * cbscustregbean.setProductlist(productlist);
				 */

				trace("Getting sub-Product List ....");
				List suproductlist = commondesc.getSubProductList(instid, cbscustregbean.getProductcode(),
						jdbctemplate);
				trace("Got Sub - Product List" + suproductlist);

				cbscustregbean.setSubproductlist(suproductlist);

				String limitname = commondesc.getLimitDesc(instid, cbscustregbean.getLimitid(), jdbctemplate);
				cbscustregbean.setLimitname(limitname);

				String feename = commondesc.getFeeDescription(instid, cbscustregbean.getFeecode(), jdbctemplate);
				cbscustregbean.setFeename(feename);

				trace("setting accttypelist");
				List accttypelist = cbscustdao.getAcctTypeList(instid, jdbctemplate);
				cbscustregbean.setAccttypelist(accttypelist);

				trace("set account no length");
				cbscustregbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));

				List documentlist = commondesc.gettingDocumnettype(instid, jdbctemplate);
				if (!documentlist.isEmpty()) {
					trace("Got list of master document...:" + documentlist.size());
					cbscustregbean.setDocumentlist(documentlist);
				}

				trace("setting currency list");

				List currencylist = commondesc.getCurList(instid, jdbctemplate);
				cbscustregbean.setCurrencylist(currencylist);

				transact.txManager.commit(transact.status);
				addActionMessage(
						orderrefno + " order reference number Generated successfully, Card Waiting for Authorization ");
				trace(orderrefno + " order reference number Generated successfully, Card Waiting for Authorization ");

				try {

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg(
							"Register CBS Details for Card Refference Number[ " + orderrefno + " ] is Authorized ");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("9009");
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}

				return "cbsreg_home";

				// cbscustregbean.setTab1Status("");
				// cbscustregbean.setTab2Status("");
				/*
				 * cbscustregbean.setTab3Status("true");
				 * cbscustregbean.setTab4Status("true");
				 */

			} else {
				transact.txManager.rollback(transact.status);
				trace("cardorderinsert:::: " + cardorderinsert);
				trace("appidinsert:::: " + appidinsert);
				trace("updateref:::: " + updateref);
				addActionError("Unable to continue the process");
			}

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			addActionError("Unable to continue the process");
			transact.txManager.rollback(transact.status);
			e.printStackTrace();

		}

		trace("saveProductInformation ended....");
		return "cbsreg_home";
	}

	/*
	 * public String customerDetailsAdd() throws Exception {
	 * 
	 * 
	 * System.out.println("customerDetailsAdd calledd ......."); trace(
	 * "customerDetailsAdd calledd .......");
	 * 
	 * HttpSession session = getRequest().getSession();
	 * 
	 * String instid = (String)session.getAttribute("Instname"); String usercode
	 * = comUserCode(session); IfpTransObj transact =
	 * commondesc.myTranObject("REF", txManager); int result =-1; String
	 * orderrefno = getRequest().getParameter("applicationid"); String
	 * customerid = getRequest().getParameter("customerid");
	 * 
	 * String accounttypevalue = getRequest().getParameter("accounttypevalue");
	 * String accountsubtypevalue =
	 * getRequest().getParameter("accountsubtypevalue"); String tab2_currency =
	 * getRequest().getParameter("tab2_currency"); String accountnovalue =
	 * getRequest().getParameter("accountnovalue");
	 * 
	 * 
	 * trace("accounttypevalue::{"+accounttypevalue+"}:::accountsubtypevalue{"+
	 * accountsubtypevalue+"tab2_currency:"+tab2_currency+"accountnovalue"+
	 * accountnovalue);
	 * 
	 * 
	 * String accounttypevalue[] =
	 * getRequest().getParameterValues("accounttypevalue"); String
	 * accountnovalue[] = getRequest().getParameterValues("accountnovalue");
	 * 
	 * for (int i = 0; i < accounttypevalue.length; i++) {
	 * 
	 * System.out.println("accounttypevalue----"+accounttypevalue[i]);
	 * System.out.println("accountnovalue----"+accountnovalue[i]);
	 * 
	 * }
	 * 
	 * 
	 * int updatecustlevel = -1; int updcustdetails = -1; int updateacctdetails
	 * = -1; Iterator itr1 = null; try{
	 * 
	 * 
	 * 
	 * 
	 * System.out.println("===getFirstname==="+cbscustregbean.getMiddlename());
	 * System.out.println("===getFirstname==="+cbscustregbean.getAccountnovalue(
	 * ));
	 * 
	 * System.out.println("orderrefno ::"+orderrefno);
	 * 
	 * updatecustlevel = cbscustdao.updateCustomerRegisterationLevel(instid,
	 * orderrefno, "3", cbscustregbean, jdbctemplate);
	 * 
	 * updcustdetails = cbscustdao.updateCustomerContactPersionalDetails(instid,
	 * orderrefno,usercode, "4", cbscustregbean, jdbctemplate);
	 * 
	 * // updateacctdetails = cbscustdao.updateAccountDetails(instid,
	 * orderrefno,usercode, accounttypevalue,
	 * accountsubtypevalue,tab2_currency,accountnovalue, cbscustregbean,
	 * jdbctemplate);
	 * 
	 * //result = cbscustdao.insertCustomerDetails(instid, usercode, customerid,
	 * cbscustregbean, jdbctemplate); trace("customerDetailsAdd : " +
	 * updatecustlevel+updcustdetails+updateacctdetails );
	 * 
	 * if(updatecustlevel==1 && updcustdetails ==1 ) {
	 * cbscustregbean.setReglevel("3");
	 * cbscustregbean.setApplicationid(orderrefno);
	 * 
	 * 
	 * trace("getting product details........"); List productDetails =
	 * cbscustdao.getApplicationData(instid,orderrefno, jdbctemplate); Iterator
	 * itr = productDetails.iterator(); while( itr.hasNext() ){ Map mp =
	 * (Map)itr.next();
	 * 
	 * 
	 * 
	 * //cbscustregbean.setBranchcode((String)mp.get("BRANCH_CODE"));
	 * cbscustregbean.setEmbname((String) mp.get("EMBOSSING_NAME"));
	 * cbscustregbean.setEncname((String) mp.get("ENCODE_DATA"));
	 * cbscustregbean.setProductcode((String) mp.get("PRODUCT_CODE"));
	 * cbscustregbean.setSubproduct((String) mp.get("SUB_PROD_ID"));
	 * cbscustregbean.setLimitbasedon((String) mp.get("LIMIT_BASEDON"));
	 * cbscustregbean.setLimitid((String) mp.get("LIMIT_ID"));
	 * cbscustregbean.setFeecode((String) mp.get("FEE_CODE"));
	 * if(session.getAttribute("USERTYPE")!="INSTADMIN") {
	 * cbscustregbean.setCustomeridno((String) mp.get("CIN")); } }
	 * 
	 * trace("getting Customer details........"); List customerDetails =
	 * cbscustdao.getCustomerData(instid,orderrefno, jdbctemplate); itr1 =
	 * customerDetails.iterator(); while( itr1.hasNext() ){ Map mp =
	 * (Map)itr1.next(); System.out.println("====f"+(String) mp.get("MOBILE"));
	 * cbscustregbean.setMobile((String) mp.get("MOBILE"));
	 * cbscustregbean.setEmail((String) mp.get("E_MAIL"));
	 * cbscustregbean.setP_poxbox((String) mp.get("P_PO_BOX"));
	 * cbscustregbean.setP_houseno((String) mp.get("P_HOUSE_NO"));
	 * cbscustregbean.setP_streetname((String) mp.get("P_STREET_NAME"));
	 * cbscustregbean.setP_wardnumber((String) mp.get("P_WARD_NAME"));
	 * cbscustregbean.setP_city((String) mp.get("P_CITY"));
	 * cbscustregbean.setP_district((String) mp.get("P_DISTRICT"));
	 * cbscustregbean.setP_phone1((String) mp.get("P_PHONE1"));
	 * cbscustregbean.setP_phone2((String) mp.get("P_PHONE2"));
	 * 
	 * cbscustregbean.setC_poxbox((String) mp.get("C_PO_BOX"));
	 * cbscustregbean.setC_houseno((String) mp.get("C_HOUSE_NO"));
	 * cbscustregbean.setC_streetname((String) mp.get("C_STREET_NAME"));
	 * cbscustregbean.setC_wardnumber((String) mp.get("C_WARD_NAME"));
	 * cbscustregbean.setC_city((String) mp.get("C_CITY"));
	 * cbscustregbean.setC_district((String) mp.get("C_DISTRICT"));
	 * cbscustregbean.setC_phone1((String) mp.get("C_PHONE1"));
	 * cbscustregbean.setC_phone2((String) mp.get("C_PHONE2"));
	 * //cbscustregbean.setMiddlename((String) mp.get("FATHER_NAME"));
	 * 
	 * }
	 * 
	 * 
	 * List branchlist = commondesc.generateBranchList(instid, jdbctemplate);
	 * if( branchlist.isEmpty() ){ addActionError("Could not get branch list");
	 * return "required_home"; } cbscustregbean.setBranchlist(branchlist);
	 * 
	 * String acctnolen = commondesc.getAccountNoLength(instid, jdbctemplate);
	 * trace("Getting acct number length ...got : " + acctnolen ); if( acctnolen
	 * == null ){ addActionError(
	 * "Account Number length not set for institution "); return
	 * "required_home"; } cbscustregbean.setAcctnolen(acctnolen);
	 * 
	 * 
	 * trace("setting accttypelist"); List accttypelist =
	 * cbscustdao.getAcctTypeList(instid, jdbctemplate);
	 * cbscustregbean.setAccttypelist(accttypelist);
	 * 
	 * 
	 * trace("Getting Product List ...."); List productlist =
	 * this.getProductList(instid, jdbctemplate,
	 * cbscustregbean.getBranchcode()); trace("Got Product List"+productlist);
	 * 
	 * cbscustregbean.setProductlist(productlist);
	 * 
	 * trace("Getting sub-Product List ...."); List suproductlist =
	 * commondesc.getSubProductList(instid, cbscustregbean.getProductcode(),
	 * jdbctemplate); trace("Got Sub - Product List"+suproductlist);
	 * 
	 * cbscustregbean.setSubproductlist(suproductlist);
	 * 
	 * String limitname = commondesc.getLimitDesc(instid,
	 * cbscustregbean.getLimitid(), jdbctemplate);
	 * cbscustregbean.setLimitname(limitname);
	 * 
	 * String feename = commondesc.getFeeDescription(instid,
	 * cbscustregbean.getFeecode(), jdbctemplate);
	 * cbscustregbean.setFeename(feename);
	 * 
	 * 
	 * trace("setting getAcctSubTypeList "); List subaccounttypelist =
	 * cbscustdao.getAcctSubTypeList(instid,
	 * cbscustregbean.getAccounttypevalue(), jdbctemplate);
	 * cbscustregbean.setAccuntsubtypelist(subaccounttypelist);
	 * 
	 * trace("setting currency list"); List currencylist =
	 * commondesc.getCurList(instid, jdbctemplate);
	 * cbscustregbean.setCurrencylist(currencylist);
	 * 
	 * List documentlist = commondesc.gettingDocumnettype(instid, jdbctemplate);
	 * if( !documentlist.isEmpty() ){ trace("Got list of master document...:" +
	 * documentlist.size()); cbscustregbean.setDocumentlist(documentlist); }
	 * 
	 * 
	 * trace("set account no length");
	 * cbscustregbean.setAccountnolength(commondesc.getAccountNoLength(instid,
	 * jdbctemplate));
	 * 
	 * trace("setting currency list");
	 * cbscustregbean.setCurrencylist(commondesc.getCurList(instid,
	 * jdbctemplate));
	 * 
	 * //cbscustregbean.setTab1Status(""); //cbscustregbean.setTab2Status("");
	 * //cbscustregbean.setTab3Status(""); cbscustregbean.setTab4Status("true");
	 * 
	 * transact.txManager.commit(transact.status);
	 * 
	 * } else { transact.txManager.rollback(transact.status); addActionError(
	 * "Unable to continue the process"); }
	 * 
	 * }catch(Exception e ){ transact.txManager.rollback(transact.status);
	 * e.printStackTrace(); trace("Exception : " + e.getMessage());
	 * addActionError("Unable to continue the process"); }
	 * 
	 * System.out.println("customerDetailsAdd ended ......."); trace(
	 * "customerDetailsAdd ended ......."); return "debitreg_entry";
	 * 
	 * }
	 * 
	 * 
	 * 
	 * public String contactDetailsAdd() throws Exception {
	 * 
	 * HttpSession session = getRequest().getSession();
	 * 
	 * System.out.println("contactDetailsAdd calledd ......."); trace(
	 * "contactDetailsAdd calledd .......");
	 * 
	 * String instid = (String)session.getAttribute("Instname"); String usercode
	 * = comUserCode(session); IfpTransObj transact =
	 * commondesc.myTranObject("REF", txManager); int result =-1; String
	 * orderrefno = getRequest().getParameter("applicationid"); String
	 * customerid = getRequest().getParameter("customerid"); int updatecustlevel
	 * = -1; int updcustdetails = -1; Iterator itr1 = null; try{
	 * 
	 * 
	 * 
	 * 
	 * System.out.println("======"+cbscustregbean.getFirstname());
	 * System.out.println("==midddd===="+cbscustregbean.getMiddlename());
	 * 
	 * System.out.println("orderrefno ::"+orderrefno);
	 * 
	 * 
	 * trace("getting Customer details........"); List customerDetails =
	 * cbscustdao.getCustomerData(instid,orderrefno, jdbctemplate); itr1 =
	 * customerDetails.iterator(); while( itr1.hasNext() ){ Map mp =
	 * (Map)itr1.next(); System.out.println("====f"+(String) mp.get("MNAME"));
	 * cbscustregbean.setFirstname((String) mp.get("FNAME"));
	 * cbscustregbean.setMiddlename((String) mp.get("MNAME"));
	 * cbscustregbean.setLastname((String) mp.get("LNAME"));
	 * cbscustregbean.setDob((String) mp.get("DOB"));
	 * cbscustregbean.setGender((String) mp.get("GENDER"));
	 * cbscustregbean.setMstatus((String) mp.get("MARITAL_STATUS"));
	 * cbscustregbean.setNationality((String) mp.get("NATIONALITY"));
	 * cbscustregbean.setDocumentprovided((String) mp.get("DOCUMENT_PROVIDED"));
	 * cbscustregbean.setDocumentnumber((String) mp.get("DOCUMENT_NUMBER"));
	 * cbscustregbean.setSpousename((String) mp.get("SPOUCE_NAME"));
	 * cbscustregbean.setMothername((String) mp.get("MOTHER_NAME"));
	 * cbscustregbean.setFathername((String) mp.get("FATHER_NAME"));
	 * //cbscustregbean.setMiddlename((String) mp.get("FATHER_NAME"));
	 * 
	 * }
	 * 
	 * 
	 * 
	 * updatecustlevel = cbscustdao.updateCustomerRegisterationLevel(instid,
	 * orderrefno, "3", cbscustregbean, jdbctemplate);
	 * 
	 * updcustdetails = cbscustdao.updateCustomerContactPersionalDetails(instid,
	 * orderrefno,usercode, "4", cbscustregbean, jdbctemplate);
	 * 
	 * //result = cbscustdao.insertCustomerDetails(instid, usercode, customerid,
	 * cbscustregbean, jdbctemplate); trace("updatecustlevel : " +
	 * updatecustlevel );
	 * 
	 * if(updatecustlevel==1 && updcustdetails ==1) {
	 * cbscustregbean.setReglevel("4");
	 * cbscustregbean.setApplicationid(orderrefno);
	 * 
	 * 
	 * trace("getting product details........"); List productDetails =
	 * cbscustdao.getApplicationData(instid,orderrefno, jdbctemplate); Iterator
	 * itr = productDetails.iterator(); while( itr.hasNext() ){ Map mp =
	 * (Map)itr.next();
	 * 
	 * //cbscustregbean.setBranchcode((String) mp.get("BRANCH_CODE"));
	 * cbscustregbean.setEmbname((String) mp.get("EMBOSSING_NAME"));
	 * cbscustregbean.setEncname((String) mp.get("ENCODE_DATA"));
	 * cbscustregbean.setProductcode((String) mp.get("PRODUCT_CODE"));
	 * cbscustregbean.setSubproduct((String) mp.get("SUB_PROD_ID"));
	 * cbscustregbean.setLimitbasedon((String) mp.get("LIMIT_BASEDON"));
	 * cbscustregbean.setLimitid((String) mp.get("LIMIT_ID"));
	 * cbscustregbean.setFeecode((String) mp.get("FEE_CODE"));
	 * cbscustregbean.setCustomeridno((String) mp.get("CIN")); }
	 * System.out.println(cbscustregbean.getBranchcode());
	 * System.out.println(cbscustregbean.getEmbname());
	 * System.out.println(cbscustregbean.getEncname());
	 * System.out.println(cbscustregbean.getProductcode());
	 * System.out.println(cbscustregbean.getSubproduct());
	 * System.out.println(cbscustregbean.getLimitbasedon());
	 * System.out.println(cbscustregbean.getFeecode());
	 * 
	 * 
	 * trace("getting product details........"); List accounttypedetails =
	 * cbscustdao.getAccounttypeinfo(instid,orderrefno, jdbctemplate); itr =
	 * accounttypedetails.iterator(); while( itr.hasNext() ){ Map mp =
	 * (Map)itr.next(); System.out.println("-----------------test--"+(String)
	 * mp.get("ACCTTYPE_ID")); cbscustregbean.setAccounttypevalue((String)
	 * mp.get("ACCTTYPE_ID")); cbscustregbean.setAccountsubtypevalue((String)
	 * mp.get("ACCTSUB_TYPE_ID")); cbscustregbean.setTab2_currency((String)
	 * mp.get("ACCT_CURRENCY")); cbscustregbean.setAccountnovalue((String)
	 * mp.get("ACCOUNTNO"));
	 * cbscustregbean.setAccountnolength(commondesc.getAccountNoLength(instid,
	 * jdbctemplate));
	 * 
	 * } List branchlist = commondesc.generateBranchList(instid, jdbctemplate);
	 * if( branchlist.isEmpty() ){ addActionError("Could not get branch list");
	 * return "required_home"; } cbscustregbean.setBranchlist(branchlist);
	 * 
	 * trace("Getting Product List ...."); List productlist =
	 * this.getProductList(instid, jdbctemplate,
	 * cbscustregbean.getBranchcode()); trace("Got Product List"+productlist);
	 * 
	 * cbscustregbean.setProductlist(productlist);
	 * 
	 * trace("Getting sub-Product List ...."); List suproductlist =
	 * commondesc.getSubProductList(instid, cbscustregbean.getProductcode(),
	 * jdbctemplate); trace("Got Sub - Product List"+suproductlist);
	 * 
	 * cbscustregbean.setSubproductlist(suproductlist);
	 * 
	 * String limitname = commondesc.getLimitDesc(instid,
	 * cbscustregbean.getLimitid(), jdbctemplate);
	 * cbscustregbean.setLimitname(limitname);
	 * 
	 * String feename = commondesc.getFeeDescription(instid,
	 * cbscustregbean.getFeecode(), jdbctemplate);
	 * cbscustregbean.setFeename(feename);
	 * 
	 * trace("setting accttypelist"); List accttypelist =
	 * cbscustdao.getAcctTypeList(instid, jdbctemplate);
	 * cbscustregbean.setAccttypelist(accttypelist);
	 * 
	 * trace("setting getAcctSubTypeList "); List subaccounttypelist =
	 * cbscustdao.getAcctSubTypeList(instid,
	 * cbscustregbean.getAccounttypevalue(), jdbctemplate);
	 * cbscustregbean.setAccuntsubtypelist(subaccounttypelist);
	 * 
	 * trace("setting currency list"); List currencylist =
	 * commondesc.getCurList(instid, jdbctemplate);
	 * cbscustregbean.setCurrencylist(currencylist);
	 * 
	 * 
	 * 
	 * trace("set account no length");
	 * cbscustregbean.setAccountnolength(commondesc.getAccountNoLength(instid,
	 * jdbctemplate));
	 * 
	 * 
	 * //cbscustregbean.setTab1Status("true");
	 * //cbscustregbean.setTab2Status("true");
	 * //cbscustregbean.setTab3Status("true");
	 * //cbscustregbean.setTab4Status("true");
	 * 
	 * // trace("setting currency list"); //
	 * cbscustregbean.setCurrencylist(commondesc.getCurList(instid,
	 * jdbctemplate));
	 * 
	 * 
	 * transact.txManager.commit(transact.status); } else {
	 * transact.txManager.rollback(transact.status); addActionError(
	 * "Unable to continue the process"); }
	 * 
	 * }catch(Exception e ){ transact.txManager.rollback(transact.status);
	 * e.printStackTrace(); trace("Exception : " + e.getMessage());
	 * addActionError("Unable to continue the process"); } System.out.println(
	 * "contactDetailsAdd ended ......."); trace(
	 * "contactDetailsAdd ended ......."); return "debitreg_entry";
	 * 
	 * }
	 */

	public List getProductList(String instid, JdbcTemplate jdbctemplate, List branchcode) throws Exception {
		return getProductList(instid, branchcode, jdbctemplate);
	}

	public List getProductList(String instid, List branchcode, JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String query=
		 * "SELECT PRODUCTCODE as PRODUCT_CODE FROM PRODUCT_MASTER WHERE INST_ID='"
		 * +instid+"' AND BRANCHCODE='"+branchcode+"' AND STATUS='1'"; enctrace(
		 * " get prod list : " + query ); List prodlist =
		 * jdbctemplate.queryForList(query);
		 */

		// by gowtham-220819
		String query = "SELECT PRODUCTCODE as PRODUCT_CODE FROM PRODUCT_MASTER WHERE INST_ID=? AND BRANCHCODE=? AND STATUS=?";
		enctrace(" get prod list : " + query);
		List prodlist = jdbctemplate.queryForList(query, new Object[] { instid, branchcode, "1" });

		if (!prodlist.isEmpty()) {
			ListIterator itr = prodlist.listIterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String productcode = (String) mp.get("PRODUCT_CODE");
				if (!productcode.equals("$ALL")) {
					String productdesc = this.getProductdesc(instid, productcode, jdbctemplate);
					mp.put("CARD_TYPE_NAME", productdesc);
					itr.remove();
					itr.add(mp);
				} else {
					return getProductListView(instid, jdbctemplate);
				}
			}
		} else {
			return getProductListView(instid, jdbctemplate);
		}
		return prodlist;
	}

	public String getProductdesc(String instid, String prodcode, JdbcTemplate jdbctemplate) {
		String bin_desc = null;

		// by gowtham
		/*
		 * String qryproductdesc =
		 * "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"+ instid +
		 * "' and PRODUCT_CODE='" + prodcode + "'";
		 */
		String qryproductdesc = "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=? and PRODUCT_CODE=?";

		trace(" getProductdesc " + qryproductdesc + "jdbctemplate.getMaxRows()===> " + jdbctemplate.getMaxRows());
		if (jdbctemplate.getMaxRows() == 0) {
			trace(" Entered IF condition ");
			try {

				/*
				 * bin_desc = (String)
				 * jdbctemplate.queryForObject(qryproductdesc, String.class);
				 */
				bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc, new Object[] { instid, prodcode, },
						String.class);

				trace(" BIN DESCRIPTION " + bin_desc);
			} catch (EmptyResultDataAccessException e) {
			}
			return bin_desc;
		} else {
			return "UNKNOWN PRODUCT";
		}
	}

	public List getProductListView(String instid, JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String query=
		 * "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID='"
		 * +instid+"' AND AUTH_CODE=1 AND DELETED_FLAG !='2'"; enctrace(
		 * " get prod list : " + query );
		 * return(jdbctemplate.queryForList(query));
		 */

		// by gowtham-220819
		String query = "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID=? AND AUTH_CODE=? AND DELETED_FLAG !=?";
		enctrace(" get prod list : " + query);
		return (jdbctemplate.queryForList(query, new Object[] { instid, "1", "2" }));

	}

	List cardcollectbranchlist;

	public List getCardcollectbranchlist() {
		return cardcollectbranchlist;
	}

	public void setCardcollectbranchlist(List cardcollectbranchlist) {
		this.cardcollectbranchlist = cardcollectbranchlist;
	}
}
