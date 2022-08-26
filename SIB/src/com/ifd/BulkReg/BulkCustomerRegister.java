package com.ifd.BulkReg;

import java.io.BufferedReader;
import connection.Dbcon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifd.personalize.DebitBulkCustomerRegister;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.personalize.PersionalizedcardCondition;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.ifp.util.PDFReportGenerator;
import com.itextpdf.text.Document;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.jmx.trace.TraceImplementation;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import sun.security.util.Length;

import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import jxl.read.biff.BiffException;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class BulkCustomerRegister extends BaseAction implements ModelDriven<BulkCustomerRegisterBean> {

	private static final long serialVersionUID = -2701974637566770853L;
	BulkCustomerRegisterBean Bulkcustregbean = new BulkCustomerRegisterBean();
	BulkCustomerRegisterDAO dao = new BulkCustomerRegisterDAO();
	CommonDesc commondesc = new CommonDesc();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	PadssSecurity padsssec = new PadssSecurity();

	public BulkCustomerRegisterBean getBulkcustregbean() {
		return Bulkcustregbean;
	}

	public void setBulkcustregbean(BulkCustomerRegisterBean bulkcustregbean) {
		Bulkcustregbean = bulkcustregbean;
	}

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

	public BulkCustomerRegisterBean getModel() {
		return Bulkcustregbean;
	}

	PersionalizedcardCondition brcodecon = new PersionalizedcardCondition();

	private String act;

	private List branchlist;

	private List customerauthproductlist;
	private List uploadStatus;

	public List getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(List uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

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

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
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

	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public String comInstId(HttpSession session2) {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId(HttpSession session2) {
		HttpSession session = getRequest().getSession();
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	private File UploadSQLFile;
	private String UploadSQLFileContentType;
	private String UploadSQLFileName;
	private ProcessBuilder processBuilder;

	public File getUploadSQLFile() {
		return UploadSQLFile;
	}

	public void setUploadSQLFile(File UploadSQLFile) {
		this.UploadSQLFile = UploadSQLFile;
	}

	public String getUploadSQLFileContentType() {
		return UploadSQLFileContentType;
	}

	public void setUploadSQLFileContentType(String UploadSQLFileContentType) {
		this.UploadSQLFileContentType = UploadSQLFileContentType;
	}

	public String getUploadSQLFileName() {
		return UploadSQLFileName;
	}

	public void setUploadSQLFileName(String UploadSQLFileName) {
		this.UploadSQLFileName = UploadSQLFileName;
	}

	public String chnageloststolen() {
		System.out.println("incomde chnageloststolen");

		return "changeLostStolen";
	}

	public String customerbulkregister() {
		return "customerbulkregister";
	}

	public String customerbulkregistermass() {
		return "customerbulkregister1";
	}

	private List personalproductlist;

	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}

	public String checkLoadSQLFileHome() {

		List pers_prodlist = null, br_list = null;
		trace("*********** Personalization card generation ************ ");
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		String act = getRequest().getParameter("act");
		if (act != null) {
			session.setAttribute("CARDGEN_ACT", act);
		}

		trace("User type is : " + usertype);
		try {
			trace("Getting branchlist....");
			br_list = commondesc.generateBranchList(inst_id, jdbctemplate);

			if (!(br_list.isEmpty())) {
				setBranchlist(br_list);
			} else {
				System.out.println("No Branch Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Branch Details Found ");
				trace("No Branch Details Found ");
			}

			trace("Getting product list....");
			pers_prodlist = commondesc.getProductListCarGen(inst_id, jdbctemplate);
			trace("pers_prodlist" + pers_prodlist.size());
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Product Details Found ");
				trace("No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: Error While Fetching The Product Details");
			trace("Exception : While Fetching The Product Details" + e.getMessage());
			e.printStackTrace();

		}

		return "checkbulkloadedcardsHOME";

	}

	public String ViewBulklaodedcardsloststolen() throws Exception {
		trace("### ViewBulklaodedcards Method Called ###");
		IfpTransObj transact = commondesc.myTranObject("CHECKBULKUPLOAD", txManager);

		HttpSession session = getRequest().getSession();
		String usercode = comUserId(session);
		String instid = comInstId(session);
		// String batchId = (String)session.getAttribute("BatchID");

		String branchcode = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		trace("barnch code-->" + branchcode + "cardtype-->" + cardtype);

		/*
		 * String productcode = getRequest().getParameter("product_code");
		 * String subproduct = getRequest().getParameter("subproduct");
		 * 
		 */
		List batchCustomer = dao.getbulkreissuecustomerdetaisl(instid, cardtype, branchcode, jdbctemplate);
		if (!(batchCustomer.isEmpty())) {
			Bulkcustregbean.setBulkRegCustlist(batchCustomer);
		} else {
			System.out.println("No Records Found ");
			addActionError("No Records Found");
			return "checkbulkloadedcardsHOME";
		}
		System.out.println("UploadSQLFile" + batchCustomer);
		return "checkbulkloadedcardsHOME";
	}

	public String LoadCustomerDataHome() {
		HttpSession session = getRequest().getSession();
		// String instid = (String)session.getAttribute("Instname");
		String instid = comInstId(session);
		try {
			List batchId = dao.GetBatchIDList(instid, jdbctemplate);

			Bulkcustregbean.setBatchID(batchId);
			List productlist = dao.getProductList(instid, jdbctemplate);

			if (batchId.isEmpty()) {
				addActionError("No Customers Waiting for Authorization....");
				return "required_home";
			}
			Bulkcustregbean.setProductlist(productlist);

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return "DebitBulk_SQLLoadHome";
	}

	public String LoadCustomerDataHome1() {
		HttpSession session = getRequest().getSession();
		// String instid = (String)session.getAttribute("Instname");
		String instid = comInstId(session);
		try {
			List batchId = dao.GetBatchIDList1(instid, jdbctemplate);

			Bulkcustregbean.setBatchID(batchId);
			List productlist = dao.getProductList1(instid, jdbctemplate);

			if (batchId.isEmpty()) {
				addActionError("No Customers Waiting for Authorization....");
				return "required_home";
			}
			Bulkcustregbean.setProductlist(productlist);

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return "DebitBulk_SQLLoadHome1";
	}

	private List uploadFileFileName;
	private List uploadFile;

	public List getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(List uploadFile) {
		this.uploadFile = uploadFile;
	}

	@SuppressWarnings("resource")
	public String Insertbulkregrecords() throws Exception {
		System.out.println("PROP");
		trace("### Insertbulkregrecords Method Called ###");
		IfpTransObj transact = commondesc.myTranObject("Insertbulkregrecords", txManager);
		trace("trace 1");
		Iterator filenames = uploadFileFileName.iterator();
		Iterator upfiles = uploadFile.iterator();

		HttpSession session = getRequest().getSession();
		String usercode = comUserId(session);
		String instid = comInstId(session);
		int count = 0, failure = 0, i = 0, x = 0, total = 0;
		String InsertQry = "", CUSTOMERID = "";
		String Header = "";
		Workbook workbook = null;
		try {
			int startnumber = 12;
			long randomreportid = this.generateRandom(startnumber);

			String accounttype = "SELECT ACCTTYPEID FROM ACCTTYPE WHERE INST_ID='" + instid + "' ";
			trace("account type is -->" + accounttype);

			List allaccounttype = jdbctemplate.queryForList(accounttype);
			Iterator all = allaccounttype.listIterator();
			String ACCTTYPEID = "";
			while (all.hasNext()) {
				Map temp = (Map) all.next();
				ACCTTYPEID = (String) temp.get("ACCTTYPEID");
				System.out.println("ACCTTYPEID" + ACCTTYPEID);
			}

			/*
			 * String basecrrency=
			 * "SELECT CURRENCY_CODE FROM PRODUCT_MASTER WHERE INST_ID='"
			 * +instid+"' AND BIN_FLAG='1' "; trace("base currency-->"
			 * +basecrrency); String
			 * basecurrcode=(String)jdbctemplate.queryForObject(basecrrency,
			 * String.class);
			 */

			String basecrrency = "SELECT BASE_CURRENCY FROM INSTITUTION WHERE INST_ID='" + instid + "' ";
			trace("base currency-->" + basecrrency);
			String basecurrcode = (String) jdbctemplate.queryForObject(basecrrency, String.class);

			while (filenames.hasNext()) {
				String filedata = (String) upfiles.next();
				String filename = (String) filenames.next();

				String filenamecheck = "SELECT COUNT(*) FROM BULKFAIL_REG_STATUS WHERE INST_ID='" + instid
						+ "' AND FILENAME ='" + filename + "' ";
				trace("filenamecheck-->" + filenamecheck);
				// String
				// existfilename=(String)jdbctemplate.queryForObject(filenamecheck,
				// String.class);
				int existfilename = jdbctemplate.queryForInt(filenamecheck);
				if (existfilename > 0) {
					addActionError("File is Already Exists,Use Diffrent File name..");
					return "customerbulkregister";

				}
				// System.out.println("sa"+filename+"\n "+filedata+"new
				// FileInputStream((filename))"+new
				// FileInputStream((filedata)));
				// new BufferedReader(new InputStreamReader(new FileInputStream(
				File file = new File(filename);
				// System.out.println("\t" + "Path : " +
				// file.getAbsolutePath());
				InputStream inputStream = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(filedata)));
				// System.out.println(inputStream);
				workbook = Workbook.getWorkbook(new FileInputStream((filedata)));
				Sheet sheet = workbook.getSheet(0);
				String[] sheetName = workbook.getSheetNames();
				int totalNoOfRows = sheet.getRows();
				int totalNoOfCols = sheet.getColumns();

				// System.out.println( "Sheet Name: "+sheetName[0] ); /* Table
				// Name Must be the Xls Sheet Name */

				/* Composing Header data */
				String InsertQryHeader = "INSERT INTO \"" + sheetName[0] + "\" (";
				int ReplacementTypeColumn = 0, Column1 = 0, P_CURRENCY_CODEcolumn = 0, CUSTOMER_IDcolumn = 0,
						DOBcolumn = 0, BATCH_IDcolumn = 0, CUSTIDcolumn = 0, BRANCHcolumn = 0, MOBILEcolumn = 0,
						P_ACCOUNT_NOEcolumn = 0, S1_ACCT_NOcolumn = 0, S2_ACCT_NOcolumn = 0, S3_ACCT_NOcolumn = 0,
						S4_ACCT_NOcolumn = 0, S5_ACCT_NOcolumn = 0, P_CURRENCY_TYPEcolumn = 0, S1curcodecolumn = 0,
						S2curcodecolumn = 0, S3curcodecolumn = 0, S4curcodecolumn = 0, S5curcodecolumn = 0;

				String ReplacementType = "";
				String P_CURRENCY_CODE = "", DOB = "", branch = "", pacct = "", s1acct = "", s2acct = "", s3acct = "",
						s4acct = "", s5acct = "", pcurr = "", p1curr = "", BATCH_ID = "", p2curr = "", p3curr = "",
						p4curr = "", p5curr = "", mobile = "", batchname = "", custid = "";

				for (int FirstColumnHeaders = 0; FirstColumnHeaders < totalNoOfCols; FirstColumnHeaders++) {
					if (!sheet.getCell(FirstColumnHeaders, 0).getContents().isEmpty()) {
						Cell cell1 = sheet.getCell(FirstColumnHeaders, 0);
						InsertQryHeader += "\"" + cell1.getContents() + "\"";
						Header = cell1.getContents();
						if (Header.equalsIgnoreCase("BATCH_ID")) {
							BATCH_IDcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("CUSTOMER_ID")) {
							CUSTIDcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("P_CURRENCY_CODE")) {
							P_CURRENCY_CODEcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("P_ACCOUNT_NO")) {
							P_ACCOUNT_NOEcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S1_ACCT_NO")) {
							S1_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S2_ACCT_NO")) {
							S2_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S3_ACCT_NO")) {
							S3_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S4_ACCT_NO")) {
							S4_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S5_ACCT_NO")) {
							S5_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("CUSTOMER_ID")) {
							CUSTOMER_IDcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("DOB")) {
							DOBcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("BRANCH")) {
							BRANCHcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("MOBILE")) {
							MOBILEcolumn = FirstColumnHeaders;
						}

						if (Header.equalsIgnoreCase("P_CURRENCY_TYPE")) {
							P_CURRENCY_TYPEcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S1_CURRENCY_TYPE")) {
							S1curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S2_CURRENCY_TYPE")) {
							S2curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S3_CURRENCY_TYPE")) {
							S3curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S4_CURRENCY_TYPE")) {
							S4curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S5_CURRENCY_TYPE")) {
							S5curcodecolumn = FirstColumnHeaders;
						}

						if (FirstColumnHeaders != totalNoOfCols - 1)
							InsertQryHeader += ", ";

					}

				}
				InsertQryHeader += ", FILE_NAME ) ";
				enctrace("Header: " + InsertQryHeader);

				/* Composing Values for the available rows */
				for (i = 0; i < totalNoOfRows; i++) {

					String InsertQryValues = "VALUES ( "; /*
															 * Iteration of Values
															 */

					Column1 = BATCH_IDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						BATCH_ID = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						BATCH_ID = cell.getContents();
					}

					Column1 = P_CURRENCY_CODEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						P_CURRENCY_CODE = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						P_CURRENCY_CODE = cell.getContents();
					}

					Column1 = P_ACCOUNT_NOEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pacct = cell.getContents();
					}

					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pacct = cell.getContents();
					}
					Column1 = S1_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s1acct = cell.getContents();
					}

					Column1 = S2_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s2acct = cell.getContents();
					}

					Column1 = S3_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s3acct = cell.getContents();
					}

					Column1 = S4_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s4acct = cell.getContents();
					}

					Column1 = S5_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s5acct = cell.getContents();
					}

					Column1 = DOBcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						DOB = cell.getContents();
						// System.out.println("finid DBO"+DOB);
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						DOB = cell.getContents();
						// System.out.println("finid DBO"+DOB);
					}

					Column1 = BRANCHcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						branch = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						branch = cell.getContents();
					}
					Column1 = CUSTOMER_IDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						CUSTOMERID = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						CUSTOMERID = cell.getContents();
					}

					Column1 = P_CURRENCY_TYPEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pcurr = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pcurr = cell.getContents();
					}
					Column1 = S1curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p1curr = cell.getContents();
					}

					Column1 = S2curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p2curr = cell.getContents();
					}
					Column1 = S3curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p3curr = cell.getContents();
					}

					Column1 = S4curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p4curr = cell.getContents();
					}

					Column1 = S5curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p5curr = cell.getContents();
					}
					Column1 = MOBILEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						mobile = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						mobile = cell.getContents();
					}

					Column1 = BATCH_IDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						batchname = cell.getContents();

					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						batchname = cell.getContents();

					}
					Column1 = CUSTIDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						custid = cell.getContents();

					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						custid = cell.getContents();

					}

					for (int Column = 0; Column < totalNoOfCols; Column++) {
						if (!sheet.getCell(Column, i).getContents().isEmpty()) {

							Cell cell = sheet.getCell(Column, i);

							InsertQryValues += "\'" + cell.getContents() + "\'";
							if (Column != totalNoOfCols - 1)
								InsertQryValues += ", ";
							// Card Number

						} else {
							InsertQryValues += "\'\'";
							if (Column != totalNoOfCols - 1)
								InsertQryValues += ", ";
						}

						/*
						 * Column = CardNumColumn; // Card Number if(
						 * !sheet.getCell( Column, i ).getContents().isEmpty()
						 * ){ Cell cell = sheet.getCell( Column , i );
						 * CardNumber = cell.getContents(); }
						 */
					}

					InsertQryValues += ", '" + filename
							+ "') "; /* Iteration of Values End */

					/* Adding Header and Value of each row */
					InsertQry = InsertQryHeader + InsertQryValues;
					// System.out.println("P_CURRENCY_CODE"+P_CURRENCY_CODE);
					// System.out.println( i+" query: "+InsertQry );

					String finalgotvalue = "", respmsg = "";
					// System.out.println("pacct"+pacct+"- CUSTOMERID
					// "+CUSTOMERID+" -"+DOB+"mobile - "+mobile+" s3acct
					// -"+s3acct);

					/*
					 * if(i !=0 && !P_CURRENCY_CODE.equals(basecurrcode.trim())
					 * ){ finalgotvalue="1"; respmsg=
					 * "PRIMARY CURRENCY MUST BE -"+basecurrcode; }
					 */

					System.out.println("CHECK6-->" + i);

					if (i == 1) {
						System.out.println("tttt iii---->" + i);
						System.out.println("tttt before---->" + BATCH_ID);
						String result1 = BATCH_ID.substring(0, 10);
						System.out.println("tttt after---->" + result1);
						if (!result1.equalsIgnoreCase("BATCH_PROP")) {
							addActionError("Upload Only Prop General Card File In This Module");
							return "customerbulkregister";
						}
					}

					if (i != 0 && pacct.equalsIgnoreCase("P_ACCOUNT_NO") || pacct.length() != 9) {
						finalgotvalue = "2";
						respmsg = "P_ACCOUNT_NO IS EMPTY/LENGTH IS NOT 9";
					} else if (i != 0 && !s1acct.equalsIgnoreCase("") && !s1acct.equalsIgnoreCase("S1_ACCT_NO")
							&& s1acct.length() != 9) {
						System.out.println("s1acct" + s1acct);
						finalgotvalue = "3";
						respmsg = "S1_ACCT_NO IS EMPTY/LENGTH IS NOT 9";
					} else if (i != 0 && !s2acct.equalsIgnoreCase("") && !s2acct.equalsIgnoreCase("S2_ACCT_NO")
							&& s2acct.length() != 9) {
						System.out.println("s1acct" + s2acct);
						finalgotvalue = "4";
						respmsg = "S2_ACCT_NO IS EMPTY/LENGTH IS NOT 9";
					} else if (i != 0 && !s3acct.equalsIgnoreCase("") && s3acct.equalsIgnoreCase("S3_ACCT_NO")
							&& s3acct.length() != 9) {
						finalgotvalue = "5";
						System.out.println("income" + s3acct);
						respmsg = "S3_ACCT_NO IS EMPTY/LENGTH IS NOT 9";
					}

					else if (i != 0 && !s4acct.equalsIgnoreCase("") && !s4acct.equalsIgnoreCase("S4_ACCT_NO")
							&& s4acct.length() != 9) {
						finalgotvalue = "6";
						respmsg = "S4_ACCT_NO IS EMPTY/LENGTH IS NOT 9";
					} else if (i != 0 && !s5acct.equalsIgnoreCase("") && !s5acct.equalsIgnoreCase("S5_ACCT_NO")
							&& s5acct.length() != 9) {
						finalgotvalue = "7";
						respmsg = "S5_ACCT_NO IS EMPTY/LENGTH IS NOT 9";
					}

					/*
					 * else if(i !=0 && s6acct.length() !=3){ finalgotvalue="8";
					 * respmsg="S6_ACCT_NO IS EMPTY/LENGTH IS NOT 9"; }
					 */

					else if (i != 0 && !CUSTOMERID.equalsIgnoreCase("") && CUSTOMERID.equalsIgnoreCase("CUSTOMER_ID")) {
						System.out.println("CUSTOMERID" + CUSTOMERID);
						finalgotvalue = "9";
						respmsg = "CUSTOMER_ID IS EMPTY";
					}
					/*
					 * else if(DOB==DOB1){ respmsg="DOB IS EMPTY/Duplciation";
					 * finalgotvalue="10"; }
					 */
					else if (i != 0 && DOB.isEmpty()) {

						respmsg = "DOB IS EMPTY";
						finalgotvalue = "10";
				 	} else if (i != 0 && !DOB.isEmpty()) {
						try {
							int doblength = DOB.length();
							String valuefordob = DOB.substring(0, 2);
							int firstdate = 0, monthval = 0;
							String symblvalidation = DOB.substring(2, 3);
							String monthvalidation = DOB.substring(3, 5);
							// System.out.println("valuefordob"+valuefordob+"\n
							// symblvalidation"+symblvalidation+"\n
							// monthvalidation"+monthvalidation);
							if (valuefordob.contains("/") || monthvalidation.contains("/") || doblength > 10
									|| doblength < 10) {
								respmsg = "DOB formate is wrong PLEASE PROVIDE 01/12/2018 format";
								finalgotvalue = "10";

							} else {
								firstdate = Integer.parseInt(valuefordob);
								monthval = Integer.parseInt(monthvalidation);
							}
							if (firstdate > 31 || !symblvalidation.equalsIgnoreCase("/") || monthval > 12) {

								respmsg = "DOB formate is wrong PLEASE PROVIDE 01/12/2018 format";
								finalgotvalue = "10";
							} else {
								finalgotvalue = "0000";
							}
						} catch (Exception e) {
							respmsg = "DOB formate is wrong PLEASE PROVIDE 01/12/2018 format";
							finalgotvalue = "10";

						}

					} else if (i != 0 && !branch.equalsIgnoreCase("") && branch.length() != 3) {
						// System.out.println("branch.length()"+branch.length());
						respmsg = "branch Length is not 3 Digit";
						finalgotvalue = "11";
					}
					/*
					 * else if(i !=0 &&
					 * pcurr.equalsIgnoreCase("10")||pcurr.equalsIgnoreCase("20"
					 * )){ finalgotvalue="0000"; }
					 */
					else if (i != 0 && !pcurr.equalsIgnoreCase("") && !pcurr.equalsIgnoreCase("P_CURRENCY_TYPE")
							&& pcurr.length() != 2) {
						finalgotvalue = "12";
						respmsg = "P_CURRENCY_TYPE is Empty/Length is not 2 Digit";

					}
					/*
					 * else if(i !=0 &&
					 * p1curr.equalsIgnoreCase("10")||pcurr.equalsIgnoreCase(
					 * "20")){ finalgotvalue="0000"; }
					 */
					else if (i != 0 && !p1curr.equalsIgnoreCase("") && !p1curr.equalsIgnoreCase("S1_CURRENCY_TYPE")
							&& p1curr.length() != 2) {
						finalgotvalue = "13";
						respmsg = "S1_CURRENCY_TYPE is Empty/Length is not 2 Digit";
					}
					/*
					 * else if(i !=0 &&
					 * p2curr.equalsIgnoreCase("10")||pcurr.equalsIgnoreCase(
					 * "20")){ finalgotvalue="0000"; }
					 */
					else if (i != 0 && !p2curr.equalsIgnoreCase("") && !p2curr.equalsIgnoreCase("S2_CURRENCY_TYPE")
							&& p2curr.length() != 2) {
						respmsg = "S2_CURRENCY_TYPE is Empty/Length is not 2 Digit";
						finalgotvalue = "14";
					}
					/*
					 * else if(i !=0 &&
					 * p3curr.equalsIgnoreCase("10")||pcurr.equalsIgnoreCase(
					 * "20")){ finalgotvalue="0000"; }
					 */
					else if (i != 0 && !p3curr.equalsIgnoreCase("") && !p3curr.equalsIgnoreCase("S3_CURRENCY_TYPE")
							&& p3curr.length() != 2) {
						finalgotvalue = "15";
						respmsg = "S3_CURRENCY_TYPE is Empty/Length is not 2 Digit";
					}
					/*
					 * else if(i !=0 &&
					 * p4curr.equalsIgnoreCase("10")||pcurr.equalsIgnoreCase(
					 * "20")){ finalgotvalue="0000"; }
					 */
					else if (i != 0 && !p4curr.equalsIgnoreCase("") && !p4curr.equalsIgnoreCase("S4_CURRENCY_TYPE")
							&& p4curr.length() != 2) {
						finalgotvalue = "16";
						respmsg = "S4_CURRENCY_TYPE is Empty/Length is not 2 Digit";
					}
					/*
					 * else if(i !=0 &&
					 * p5curr.equalsIgnoreCase("10")||pcurr.equalsIgnoreCase(
					 * "20")){ finalgotvalue="0000"; }
					 */
					else if (i != 0 && !p5curr.equalsIgnoreCase("") && !p5curr.equalsIgnoreCase("S5_CURRENCY_TYPE")
							&& p5curr.length() != 2) {
						finalgotvalue = "17";
						respmsg = "S5_CURRENCY_TYPE is Empty/Length is not 2 Digit";
					} else if (i != 0 && mobile.equalsIgnoreCase("") && mobile.length() != 15) {
						// System.out.println("mobile"+mobile);
						finalgotvalue = "18";
						respmsg = "Mobile NUmber is Empty/Length is not 15 Digit";
					}

					else if (i != 0 && batchname.equalsIgnoreCase("") && !batchname.equalsIgnoreCase("BATCH_ID")) {
						// System.out.println("mobile"+mobile);
						finalgotvalue = "19";
						respmsg = "Batchname is NOT Mentioned";
					}

					else {
						if (i != 0) {
							finalgotvalue = "0000";
						}
					}
					if (i != 0) {
						if (finalgotvalue.equalsIgnoreCase("0000")) {
							List accountdupAvailable = dao.findaccountduplication(instid, pacct, s1acct, s2acct, s3acct,
									s4acct, s5acct, jdbctemplate);

							// System.out.println("accountdupAvailable"+accountdupAvailable);
							if (accountdupAvailable == null) {
								finalgotvalue = "0000";
							} else {
								finalgotvalue = "23";
								respmsg = "Duplicated Accounts Are " + accountdupAvailable;
							}
						}

						if (finalgotvalue.equalsIgnoreCase("0000")) {
							String checkbatchid = "SELECT COUNT(BATCH_ID) FROM CUSTOMER_BULK_LOAD WHERE  BATCH_ID ='"
									+ batchname + "' ";
							// String
							// existfilename=(String)jdbctemplate.queryForObject(filenamecheck,
							// String.class);
							int checkbid = jdbctemplate.queryForInt(checkbatchid);

							if (checkbid > 1000) {
								finalgotvalue = "19";
								respmsg = "Batch ID Limit Exceeded More than 1000/Kindly use other Batch ID Name";
							} else {

								finalgotvalue = "0000";

							}
						}

						/*
						 * if(finalgotvalue.equalsIgnoreCase("0000")){ String
						 * checkbatchid=
						 * "SELECT COUNT(*) FROM CUSTOMER_BULK_LOAD WHERE  BATCH_ID ='"
						 * +batchname+"' AND  FILE_NAME !='"
						 * +filename.trim()+"'"; //String
						 * existfilename=(String)jdbctemplate.queryForObject(
						 * filenamecheck, String.class); int
						 * checkbid=jdbctemplate.queryForInt(checkbatchid);
						 * 
						 * if(checkbid>0){ finalgotvalue="19"; respmsg=
						 * "Batch ID Found Duplication../Use Other Name.."; }
						 * else{
						 * 
						 * finalgotvalue="0000";
						 * 
						 * } }
						 */

						if (finalgotvalue.equalsIgnoreCase("0000")) {
							String cinaccountdup = "SELECT COUNT(*) FROM CUSTOMER_BULK_LOAD WHERE  CUSTOMER_ID='"
									+ custid.trim() + "' AND P_ACCOUNT_NO ='" + pacct.trim() + "' ";
							int cinACcheck = jdbctemplate.queryForInt(cinaccountdup);
							if (cinACcheck > 0) {
								finalgotvalue = "20";
								respmsg = "CustomerId & PrimayAccount NUmbers Duplicated New Reg ";
							} else {

								finalgotvalue = "0000";

							}
						}
						if (finalgotvalue.equalsIgnoreCase("0000")) {
							String cinaccountdup = "SELECT COUNT(*) FROM ACCOUNTINFO WHERE  CIN='" + custid.trim()
									+ "' AND ACCOUNTNO ='" + pacct.trim() + "' ";
							int cinACcheck = jdbctemplate.queryForInt(cinaccountdup);
							if (cinACcheck > 0) {
								finalgotvalue = "21";
								respmsg = "CustomerId & PrimayAccount NUmbers Duplicated Old Reg";
							} else {

								finalgotvalue = "0000";

							}
						}
						if (finalgotvalue.equalsIgnoreCase("0000")) {
							String branchvalidation = "SELECT COUNT(*) FROM BRANCH_MASTER WHERE  BRANCH_CODE ='"
									+ branch.trim() + "'";
							// String
							// existfilename=(String)jdbctemplate.queryForObject(filenamecheck,
							// String.class);
							// System.out.println("branchvalidation"+branchvalidation);
							int brcheck = jdbctemplate.queryForInt(branchvalidation);

							if (brcheck == 0) {
								finalgotvalue = "22";
								respmsg = "[ " + branch + " ] - Branch Is Not Configured,Kinldy Configure the Branch";
							} else {

								finalgotvalue = "0000";

							}
						}
					}
					System.out.println("finalgotvalue" + finalgotvalue);
					if (i != 0) {
						total++;
					}
					if (i != 0 && finalgotvalue.equalsIgnoreCase("0000")) {

						x = jdbctemplate.update(InsertQry);
						if (x > 0) {
							count++;
						}
						//
					} else {
						if (i != 0) {
							int insertfail = dao.insertbulkregcardsstatus(instid, filename, CUSTOMERID, i, usercode,
									randomreportid, finalgotvalue, respmsg, jdbctemplate);
							if (insertfail > 0) {
								failure++;
							}
						}
					}

					List finalstatus = new ArrayList();
					List statuslist = this.getUploadedStatus(instid, randomreportid, usercode, jdbctemplate);
					Iterator itr = statuslist.iterator();
					while (itr.hasNext()) {
						Map mp = (Map) itr.next();
						mp.put("REPORTRANDOMNO", randomreportid);
						mp.put("FILENAME", filename);
						mp.put("UPLOADEDBY", ((String) mp.get("UPLOADEDBY")));
						mp.put("UPLOADDATE", ((String) mp.get("UPLOADDATE")));
						mp.put("TOTALRECORD", total);
						mp.put("SUCCESS", count);
						mp.put("FAIL", failure);
						finalstatus.add(mp);
					}
					setUploadStatus(finalstatus);

					P_CURRENCY_CODE = "";
					DOB = "";
					branch = "";
					pacct = "";
					s1acct = "";
					s2acct = "";
					s3acct = "";
					s4acct = "";
					s5acct = "";
					pcurr = "";
					p1curr = "";
					p2curr = "";
					p3curr = "";
					p4curr = "";
					p5curr = "";
					mobile = "";
					batchname = "";
					custid = "";

					/************* AUDIT BLOCK **************/
					try {
						auditbean.setActmsg(" inserted query " + count);
						auditbean.setUsercode(usercode);
						auditbean.setAuditactcode("9102");
						// auditbean.setCardno(padsssec.getMakedCardno(CardNumber));
						// auditbean.setCardnumber(enccardno);
						// commondesc.insertAuditTrail(in_name, Maker_id,
						// auditbean, jdbctemplate, txManager);
						commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean, jdbctemplate, txManager);
					} catch (Exception audite) {
						trace("Exception in auditran : " + audite.getMessage());
					}
					/************* AUDIT BLOCK **************/

				}
			}
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			addActionMessage("Successfully Registred Customer [ " + count + "] : Failed to Registred Customer ["
					+ failure + "] ");

		}

		catch (Exception e) {
			e.printStackTrace();
			if (i == 0) {
				addActionError("File Format Not-Matched (or) Sheet Name Not-Matched (or) Columns are Not-Matched...  ");
			} else {
				addActionError("Check with excelfile line NUMBER " + i + " query: " + InsertQry);
			}

			txManager.rollback(transact.status);
			trace("Could not insert the  bulk Process records" + e.getMessage());

		}
		/*
		 * finally {
		 * 
		 * workbook.close(); }
		 */

		System.out.println("UploadSQLFile" + "called");

		return "customerbulkregister";

	}

	public String Insertbulkregrecords1() throws Exception {
		System.out.println("INSIDE MASTER");
		trace("### Insertbulkregrecords Method Called ###");
		IfpTransObj transact = commondesc.myTranObject("Insertbulkregrecords", txManager);
		Iterator filenames = uploadFileFileName.iterator();
		Iterator upfiles = uploadFile.iterator();

		HttpSession session = getRequest().getSession();
		String usercode = comUserId(session);
		String instid = comInstId(session);
		int count = 0, failure = 0, i = 0, x = 0, total = 0;
		String InsertQry = "", CUSTOMERID = "";
		String Header = "";
		Workbook workbook = null;
		try {
			int startnumber = 12;
			long randomreportid = this.generateRandom(startnumber);

			String accounttype = "SELECT ACCTTYPEID FROM ACCTTYPE WHERE INST_ID='" + instid + "' ";

			List allaccounttype = jdbctemplate.queryForList(accounttype);
			Iterator all = allaccounttype.listIterator();
			String ACCTTYPEID = "";
			while (all.hasNext()) {
				Map temp = (Map) all.next();
				ACCTTYPEID = (String) temp.get("ACCTTYPEID");
			    System.out.println("AccountTypeID-:: "+ACCTTYPEID);
			}

			while (filenames.hasNext()) {
				String filedata = (String) upfiles.next();
				String filename = (String) filenames.next();

				String filenamecheck = "SELECT COUNT(*) FROM BULKFAIL_REG_STATUS WHERE INST_ID='" + instid+ "' AND FILENAME ='" + filename + "' ";
				int existfilename = jdbctemplate.queryForInt(filenamecheck);
				if (existfilename > 0) {
					addActionError("File is Already Exists,Use Diffrent File name..");
					return "customerbulkregister1";}
				File file = new File(filename);
				InputStream inputStream = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(filedata)));
				// System.out.println(inputStream);
				workbook = Workbook.getWorkbook(new FileInputStream((filedata)));
				Sheet sheet = workbook.getSheet(0);
				String[] sheetName = workbook.getSheetNames();
				trace("sheetName-->"+sheetName[0]);
				System.out.println("sheetName-->"+sheetName[0]);
				String[] tablename={"CUSTOMER_BULK_LOAD"};
				int totalNoOfRows = sheet.getRows();
				int totalNoOfCols = sheet.getColumns();
				System.out.println("totalNoOfRows-->"+totalNoOfRows);
				System.out.println("totalNoOfCols-->"+totalNoOfCols);
				
				if(totalNoOfCols != 37){
					addActionError("LENGTH of personalized bulkupload file COLUMN should be 37 ");
					return "customerbulkregister1";}
				
				if(!sheetName[0].equalsIgnoreCase(tablename[0])){
					addActionError("Sheet Name Not-Matched");
					return "customerbulkregister1";}
				

			     // File Format Should be .xls */
				/* Composing Header data */
				
				String InsertQryHeader = "INSERT INTO \"" + sheetName[0] + "\" (";
				int ReplacementTypeColumn = 0, Column1 = 0, P_CURRENCY_CODEcolumn = 0, CUSTOMER_IDcolumn = 0,
						DOBcolumn = 0, BATCH_IDcolumn = 0, CUSTIDcolumn = 0, BRANCHcolumn = 0, MOBILEcolumn = 0,
						P_ACCOUNT_NOEcolumn = 0, S1_ACCT_NOcolumn = 0, S2_ACCT_NOcolumn = 0, S3_ACCT_NOcolumn = 0,
						S4_ACCT_NOcolumn = 0, S5_ACCT_NOcolumn = 0, P_CURRENCY_TYPEcolumn = 0, S1curcodecolumn = 0,
						S2curcodecolumn = 0, S3curcodecolumn = 0, S4curcodecolumn = 0, S5curcodecolumn = 0,productcodecolumn=0,
						Pcurrencycolumn=0,S1currencycolumn=0,S2currencycolumn=0,S3currencycolumn=0,S4currencycolumn=0,S5currencycolumn=0,NAMEColumn=0;

				String ReplacementType = "";
				String P_CURRENCY_CODE = "", BATCH_ID = "", DOB = "", branch = "", pacct = "", s1acct = "", s2acct = "",
						s3acct = "", s4acct = "", s5acct = "", pcurr = "", p1curr = "", p2curr = "", p3curr = "",
						p4curr = "", p5curr = "", mobile = "", batchname = "", custid = "",product_code="",
						Pcurrcode="",S1currcode="",S2currcode="",S3currcode="",S4currcode="",S5currcode="",NAME="";
				boolean ans=false;
				
				System.out.println("CHECK1");
				
				for (int FirstColumnHeaders = 0; FirstColumnHeaders < totalNoOfCols; FirstColumnHeaders++) {
					if (!sheet.getCell(FirstColumnHeaders, 0).getContents().isEmpty()) {
						Cell cell1 = sheet.getCell(FirstColumnHeaders, 0);
						InsertQryHeader += "\"" + cell1.getContents() + "\"";
						Header = cell1.getContents();
						if (Header.equalsIgnoreCase("BATCH_ID")) {
							BATCH_IDcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("CUSTOMER_ID")) {
							CUSTIDcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("P_CURRENCY_CODE")) {
							P_CURRENCY_CODEcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("P_ACCOUNT_NO")) {
							P_ACCOUNT_NOEcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S1_ACCT_NO")) {
							S1_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S2_ACCT_NO")) {
							S2_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S3_ACCT_NO")) {
							S3_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S4_ACCT_NO")) {
							S4_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S5_ACCT_NO")) {
							S5_ACCT_NOcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("CUSTOMER_ID")) {
							CUSTOMER_IDcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("DOB")) {
							DOBcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("BRANCH")) {
							BRANCHcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("MOBILE")) {
							MOBILEcolumn = FirstColumnHeaders;
						}

						if (Header.equalsIgnoreCase("P_ACCT_TYPE")) {
							P_CURRENCY_TYPEcolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S1_ACCT_TYPE")) {
							S1curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S2_ACCT_TYPE")) {
							S2curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S3_ACCT_TYPE")) {
							S3curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S4_ACCT_TYPE")) {
							S4curcodecolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S5_ACCT_TYPE")) {
							S5curcodecolumn = FirstColumnHeaders;
						}
						
//----*********------ADDED 
						
						if (Header.equalsIgnoreCase("P_CURRENCY_CODE")) {
							Pcurrencycolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S1_CURRENCY_CODE")) {
							S1currencycolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S2_CURRENCY_CODE")) {
							S2currencycolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S3_CURRENCY_CODE")) {
							S3currencycolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S4_CURRENCY_CODE")) {
							S4currencycolumn = FirstColumnHeaders;
						}
						if (Header.equalsIgnoreCase("S5_CURRENCY_CODE")) {
							S5currencycolumn = FirstColumnHeaders;
						}
						
						if (Header.equalsIgnoreCase("PRODUCT_TYPE")) {
							productcodecolumn = FirstColumnHeaders;
						}
					////ADDED ON 10-MAR-2021
						if (Header.equalsIgnoreCase("NAME")) {
							NAMEColumn = FirstColumnHeaders;
						}
						
						

						if (FirstColumnHeaders != totalNoOfCols - 1)
							InsertQryHeader += ", ";

					}

				}
				System.out.println("CHECK2");
				InsertQryHeader += " ,FILE_NAME ) ";
				enctrace("Header: " + InsertQryHeader);

				/* Composing Values for the available rows */
				for (i = 0; i < totalNoOfRows; i++) {

					String InsertQryValues = "VALUES ( "; /*
															 * Iteration of Values
															 */

//////added on 19-nov-2020
					Column1 = productcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						product_code=  this.filterUnwantedSpaces(cell.getContents().trim());
						//BATCH_ID = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						product_code=  this.filterUnwantedSpaces(cell.getContents().trim());
						//BATCH_ID = cell.getContents();
					}
//added on 19-nov-2020	
					Column1 = BATCH_IDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						BATCH_ID=  this.filterUnwantedSpaces(cell.getContents().trim());
						//BATCH_ID = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						BATCH_ID=  this.filterUnwantedSpaces(cell.getContents().trim());
						//BATCH_ID = cell.getContents();
					}

					Column1 = P_CURRENCY_CODEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						P_CURRENCY_CODE=  this.filterUnwantedSpaces(cell.getContents().trim());
						//P_CURRENCY_CODE = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						P_CURRENCY_CODE=  this.filterUnwantedSpaces(cell.getContents().trim());
						//P_CURRENCY_CODE = cell.getContents();
					}

					Column1 = P_ACCOUNT_NOEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pacct=  this.filterUnwantedSpaces(cell.getContents().trim());
						//pacct = cell.getContents();
					}

					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pacct=  this.filterUnwantedSpaces(cell.getContents().trim());
						//pacct = cell.getContents();
					}
					Column1 = S1_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s1acct=  this.filterUnwantedSpaces(cell.getContents().trim());
						//s1acct = cell.getContents();
					}

					Column1 = S2_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s2acct=  this.filterUnwantedSpaces(cell.getContents().trim());
						//s2acct = cell.getContents();
					}

					Column1 = S3_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s3acct=  this.filterUnwantedSpaces(cell.getContents().trim());
						//s3acct = cell.getContents();
					}

					Column1 = S4_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s4acct=  this.filterUnwantedSpaces(cell.getContents().trim());
						//s4acct = cell.getContents();
					}

					Column1 = S5_ACCT_NOcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						s5acct=  this.filterUnwantedSpaces(cell.getContents().trim());
						//s5acct = cell.getContents();
					}
					
//******CUSSRENCY CODE VALIDATION START****		
					
					Column1 = Pcurrencycolumn; // Card Number
					 if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
					 Cell cell = sheet.getCell(Column1, i);
					 Pcurrcode=  this.filterUnwantedSpaces(cell.getContents().trim());
					 }
					 
					  Column1 = S1currencycolumn; // Card Number
					 if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
					 Cell cell = sheet.getCell(Column1, i);
					 S1currcode=  this.filterUnwantedSpaces(cell.getContents().trim());
					 }


					 Column1 = S2currencycolumn; // Card Number
					 if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
					 Cell cell = sheet.getCell(Column1, i);
					 S2currcode=  this.filterUnwantedSpaces(cell.getContents().trim());
					 }


					 Column1 = S3currencycolumn; // Card Number
					 if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
					 Cell cell = sheet.getCell(Column1, i);
					 S3currcode=  this.filterUnwantedSpaces(cell.getContents().trim());
					 }


					 Column1 = S4currencycolumn; // Card Number
					 if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
					 Cell cell = sheet.getCell(Column1, i);
					 S4currcode=  this.filterUnwantedSpaces(cell.getContents().trim());
					 }

					 Column1 = S5currencycolumn; // Card Number
					 if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
					 Cell cell = sheet.getCell(Column1, i);
					 S5currcode=  this.filterUnwantedSpaces(cell.getContents().trim());
					 }
//********CURRENCY CODE VALIDATION END**************888
					
					
					Column1 = DOBcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						DOB=  this.filterUnwantedSpaces(cell.getContents().trim());
						//DOB = cell.getContents();
					 System.out.println("finid DOB"+DOB);
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						DOB=  this.filterUnwantedSpaces(cell.getContents().trim());
						DOB = cell.getContents();
						 System.out.println("finid DOB"+DOB);
					}

					Column1 = BRANCHcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						branch=  this.filterUnwantedSpaces(cell.getContents().trim());
						//branch = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						branch=  this.filterUnwantedSpaces(cell.getContents().trim());
						//branch = cell.getContents();
					}
					Column1 = CUSTOMER_IDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						CUSTOMERID=  this.filterUnwantedSpaces(cell.getContents().trim());
						//CUSTOMERID = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						CUSTOMERID=  this.filterUnwantedSpaces(cell.getContents().trim());
						//CUSTOMERID = cell.getContents();
					}

					Column1 = P_CURRENCY_TYPEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pcurr=  this.filterUnwantedSpaces(cell.getContents().trim());
						//pcurr = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						pcurr=  this.filterUnwantedSpaces(cell.getContents().trim());
						//pcurr = cell.getContents();
					}
					Column1 = S1curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p1curr =  this.filterUnwantedSpaces(cell.getContents().trim());
						//p1curr = cell.getContents();
					}

					Column1 = S2curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p2curr=  this.filterUnwantedSpaces(cell.getContents().trim());
						//p2curr = cell.getContents();
					}
					Column1 = S3curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p3curr=  this.filterUnwantedSpaces(cell.getContents().trim());
						//p3urr = cell.getContents();
					}

					Column1 = S4curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p4curr=  this.filterUnwantedSpaces(cell.getContents().trim());
						//p4curr = cell.getContents();
					}

					Column1 = S5curcodecolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						p5curr=  this.filterUnwantedSpaces(cell.getContents().trim());
						//p5curr = cell.getContents();
					}
					Column1 = MOBILEcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						mobile=  this.filterUnwantedSpaces(cell.getContents().trim());
						//mobile = cell.getContents();
					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						mobile=  this.filterUnwantedSpaces(cell.getContents().trim());
						//mobile = cell.getContents();
					}

					Column1 = BATCH_IDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						batchname=  this.filterUnwantedSpaces(cell.getContents().trim());
						//batchname = cell.getContents();

					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						batchname=  this.filterUnwantedSpaces(cell.getContents().trim());
						//batchname = cell.getContents();

					}
					Column1 = CUSTIDcolumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						custid=  this.filterUnwantedSpaces(cell.getContents().trim());
						//custid = cell.getContents();

					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						custid=  this.filterUnwantedSpaces(cell.getContents().trim());
						//custid = cell.getContents();

					}
					
					////ADDED ON 10-MAR-2021
					Column1 = NAMEColumn; // Card Number
					if (!sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						NAME=  this.filterUnwantedSpaces(cell.getContents().trim());
						//custid = cell.getContents();

					}
					if (sheet.getCell(Column1, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column1, i);
						NAME=  this.filterUnwantedSpaces(cell.getContents().trim());
						//custid = cell.getContents();

					}
					
					 

					System.out.println("CHECK3");
					for (int Column = 0; Column < totalNoOfCols; Column++) {
						if (!sheet.getCell(Column, i).getContents().isEmpty()) {

							Cell cell = sheet.getCell(Column, i);

							//InsertQryValues += "\'" + cell.getContents() + "\'";
							
							InsertQryValues += "\'" +filterUnwantedSpaces( cell.getContents().trim() )+ "\'";
							if (Column != totalNoOfCols - 1)
								InsertQryValues += ", ";
							// Card Number

						} else {
							InsertQryValues += "\'\'";
							if (Column != totalNoOfCols - 1)
								InsertQryValues += ", ";
						}

					 }

					System.out.println("CHECK5");
					InsertQryValues += ", '" + filename
							+ "') "; /* Iteration of Values End */

					/* Adding Header and Value of each row */
					InsertQry = InsertQryHeader + InsertQryValues;
					// System.out.println("P_CURRENCY_CODE"+P_CURRENCY_CODE);
					// System.out.println( i+" query: "+InsertQry );

					String finalgotvalue = "", respmsg = "";
					 					System.out.println("CHECK6-->" + i);
					
					 					if (i == 1) {
						System.out.println("tttt iii---->" + i);
						System.out.println("tttt before---->" + BATCH_ID);
						String result1 = BATCH_ID.substring(0, 12);
						System.out.println("tttt after---->" + result1);
						 
					}

					if (i != 0 && pacct.equalsIgnoreCase("P_ACCOUNT_NO") || pacct.length() != 18) {
						finalgotvalue = "2";
						respmsg = "P_ACCOUNT_NO IS EMPTY/LENGTH IS NOT 18";
					} else if (i != 0 && !s1acct.equalsIgnoreCase("") && !s1acct.equalsIgnoreCase("S1_ACCT_NO")
							&& s1acct.length() != 18) {
						System.out.println("s1acct" + s1acct);
						finalgotvalue = "3";
						respmsg = "S1_ACCT_NO IS EMPTY/LENGTH IS NOT 18";
					} else if (i != 0 && !s2acct.equalsIgnoreCase("") && !s2acct.equalsIgnoreCase("S2_ACCT_NO")
							&& s2acct.length() != 18) {
						System.out.println("s1acct" + s2acct);
						finalgotvalue = "4";
						respmsg = "S2_ACCT_NO IS EMPTY/LENGTH IS NOT 18";
					} else if (i != 0 && !s3acct.equalsIgnoreCase("") && s3acct.equalsIgnoreCase("S3_ACCT_NO")
							&& s3acct.length() != 18) {
						finalgotvalue = "5";
						System.out.println("income" + s3acct);
						respmsg = "S3_ACCT_NO IS EMPTY/LENGTH IS NOT 18";
					}

					else if (i != 0 && !s4acct.equalsIgnoreCase("") && !s4acct.equalsIgnoreCase("S4_ACCT_NO")
							&& s4acct.length() != 18) {
						finalgotvalue = "6";
						respmsg = "S4_ACCT_NO IS EMPTY/LENGTH IS NOT 18";
					} else if (i != 0 && !s5acct.equalsIgnoreCase("") && !s5acct.equalsIgnoreCase("S5_ACCT_NO")
							&& s5acct.length() != 18) {
						finalgotvalue = "7";
						respmsg = "S5_ACCT_NO IS EMPTY/LENGTH IS NOT 18";
					}
					
					else if (i != 0 && !CUSTOMERID.equalsIgnoreCase("") && CUSTOMERID.equalsIgnoreCase("CUSTOMER_ID") || CUSTOMERID.trim().length() != 6) {
						System.out.println("CUSTOMERID " + CUSTOMERID);
						finalgotvalue = "9";
						respmsg = "CUSTOMER_ID IS EMPTY /  LENGTH IS NOT 6";
						trace("CUSTOMERID ::::  "+CUSTOMERID  +" respmsg :: "+respmsg);
					}
					 else if (i != 0 && DOB.isEmpty()&&DOB.trim().length()!=11) {
						 //Modified on 10-mar-2021
			 			 finalgotvalue = "10";
						respmsg = "Date Of Birth is Empty or Wrong Format // Date Of Birth Format 10-DEC-2020";}
					
					
		 			else if (i != 0 && !branch.equalsIgnoreCase("") && branch.length() != 3) {
						// System.out.println("branch.length()"+branch.length());
						respmsg = "branch Length is not 3 Digit";
						finalgotvalue = "11";
					}
					 else if (i != 0 && !pcurr.equalsIgnoreCase("") && !pcurr.equalsIgnoreCase("P_ACCT_TYPE")
							&& pcurr.length() != 2) {
						finalgotvalue = "12";
						respmsg = "P_ACCOUNT_TYPE is Empty/Length is not 2 Digit";

					}
					 else if (i != 0 && !p1curr.equalsIgnoreCase("") && !p1curr.equalsIgnoreCase("S1_ACCT_TYPE")
							&& p1curr.length() != 2) {
						finalgotvalue = "13";
						respmsg = "S1_ACCOUNT_TYPE is Empty/Length is not 2 Digit";
					}
					 else if (i != 0 && !p2curr.equalsIgnoreCase("") && !p2curr.equalsIgnoreCase("S2_ACCT_TYPE")
							&& p2curr.length() != 2) {
						respmsg = "S2_ACCOUNT_TYPE is Empty/Length is not 2 Digit";
						finalgotvalue = "14";
					}
					 else if (i != 0 && !p3curr.equalsIgnoreCase("") && !p3curr.equalsIgnoreCase("S3_ACCT_TYPE")
							&& p3curr.length() != 2) {
						finalgotvalue = "15";
						respmsg = "S3_ACCOUNT_TYPE is Empty/Length is not 2 Digit";
					}
					 else if (i != 0 && !p4curr.equalsIgnoreCase("") && !p4curr.equalsIgnoreCase("S4_ACCT_TYPE")
							&& p4curr.length() != 2) {
						finalgotvalue = "16";
						respmsg = "S4_ACCOUNT_TYPE is Empty/Length is not 2 Digit";
					}
					 else if (i != 0 && !p5curr.equalsIgnoreCase("") && !p5curr.equalsIgnoreCase("S5_ACCT_TYPE")
							&& p5curr.length() != 2) {
						finalgotvalue = "17";
						respmsg = "S5_ACCOUNT_TYPE is Empty/Length is not 2 Digit";
					} 
					
					//Pcurrcode  Pcurrcode   P_CURRENCY_CODE  S1_CURRENCY_CODE
					else if (i != 0 && !Pcurrcode.equalsIgnoreCase("") && !Pcurrcode.equalsIgnoreCase("P_CURRENCY_CODE")
							&& Pcurrcode.length() != 3) {
						finalgotvalue = "17";
						respmsg = "P_CURRENCY_CODE is Empty/Length is not 3 Digit";
					} else if (i != 0 && !S1currcode.equalsIgnoreCase("") && !S1currcode.equalsIgnoreCase("S1_CURRENCY_CODE")
							&& S1currcode.length() != 3) {
						finalgotvalue = "17";
						respmsg = "S1_CURRENCY_CODE is Empty/Length is not 3 Digit";
					} else if (i != 0 && !S2currcode.equalsIgnoreCase("") && !S2currcode.equalsIgnoreCase("S2_CURRENCY_CODE")
							&& S2currcode.length() != 3) {
						finalgotvalue = "17";
						respmsg = "S2_CURRENCY_CODE is Empty/Length is not 3 Digit";
					} else if (i != 0 && !S3currcode.equalsIgnoreCase("") && !S3currcode.equalsIgnoreCase("S3_CURRENCY_CODE")
							&& S3currcode.length() != 3) {
						finalgotvalue = "17";
						respmsg = "S3_CURRENCY_CODE is Empty/Length is not 3 Digit";
					} else if (i != 0 && !S4currcode.equalsIgnoreCase("") && !S4currcode.equalsIgnoreCase("S4_CURRENCY_CODE")
							&& S4currcode.length() != 3) {
						finalgotvalue = "17";
						respmsg = "S4_CURRENCY_CODE is Empty/Length is not 3 Digit";
					} else if (i != 0 && !S5currcode.equalsIgnoreCase("") && !S5currcode.equalsIgnoreCase("S5_CURRENCY_CODE")
							&& S5currcode.length() != 3) {
						finalgotvalue = "17";
						respmsg = "S4_CURRENCY_CODE is Empty/Length is not 3 Digit";
					} 
					
					//if ( i!=0 && acctnumber.length()<=13 && acctnumber.equalsIgnoreCase("") && acctnumber==null && acctnumber.equals("NULL"))
					//else if (i != 0 && mobile.length()<=13 || mobile.equalsIgnoreCase("") || mobile==null || mobile.equals("NULL"))  {
					//modified on 10-MAR-2021 
					//else if ( i!=0 && mobile.length()<=13 && mobile.equalsIgnoreCase("") && mobile==null && mobile.equals("NULL"))  {
					
					//Mobile number validation removed as per bank request
					//Command Starts 13-aug-2021
					/*else if(i!=0&&mobile.isEmpty()){
						//modified on 10-MAR-2021 
						trace("mobile number--->>"+mobile+"mobile number length-->>"+mobile.length());
						finalgotvalue = "18";
						respmsg = "Mobile Number is Empty/Length is not 12 OR 13 Digit";
					}*/
					//Command Ends 13-aug-2021
					
 				else if (i != 0 && batchname.equalsIgnoreCase("") && !batchname.equalsIgnoreCase("BATCH_ID")) {
						// System.out.println("mobile"+mobile);
						finalgotvalue = "19";
						respmsg = "Batchname is NOT Mentioned";
					}
					 
					else if(i!=0&&NAME.trim().length()<26&&NAME.trim().length()<1){
						trace("Customer Name-->"+NAME.trim().length());
						finalgotvalue="24";
						respmsg="Customer Name Length (character)  greatethan 1  and lessthan 26 Required";
					}

					 else {
							if (i != 0) {
								finalgotvalue = "0000";
							}
					   }
		 					
					trace("<--Customer Name-->"+NAME+"<--Customer Name Length-->"+NAME.trim().length());
					
					//Customer ID Length Validation
					trace("customer id-->"+CUSTOMERID.trim().length()+"<--finalgotvalue-->"+finalgotvalue);
					if(i!=0&&CUSTOMERID.trim().length()!=6&&finalgotvalue== "0000"){
						trace("Customer ID-->>"+CUSTOMERID+"<<--Customer ID Length-->>"+CUSTOMERID.trim().length()+"<<--Finalgotvalue-->>"+finalgotvalue);
						finalgotvalue = "9";
						respmsg = "Customer ID is Empty / Length is Not 6";
						
					}
					
					//Mobile number validation removed as per bank request
					//Command Starts 13-aug-2021
					//Mobile Number validation
					/*if(i!=0&&!mobile.isEmpty()){
						mobile=mobile.trim();
						trace("<<--mobile number-->>"+mobile+"<<-- mobile number length-->>"+mobile.length());
						if(mobile.length()<14){
							trace("mobile numner length less than 14 digit condition passed");
							if(mobile.length()>10){
								trace("mobile numner length greater than 10  digit condition passed");
					 		}else{
								finalgotvalue = "18";
								respmsg = "Mobile Number is Empty/Length is not 12 OR 13 Digit";
								trace("Mobile Number is Empty/Length is not 12 OR 13 Digit");
			 				}
						}else{
							trace("mobile number length less than 14 digit condition failed");
							finalgotvalue = "18";
							respmsg = "Mobile Number is Empty/Length is not 12 OR 13 Digit";
						}
					}*/
					//Command Ends 13-aug-2021
					
					 if(i != 0 && !DOB.isEmpty()&&finalgotvalue=="0000") {
	 					 //Modified in 10-MAR-2021
						    trace("Before trim"+DOB+"--Date Of Birth-->"+DOB.length());
							DOB=DOB.trim();
							trace("After trim"+DOB+"--Date Of Birth-->"+DOB.length());
						 
							try{
							 String datevalidation=DOB.substring(0,2);
							 String monthvalidation=DOB.substring(3,6);
							 String yearvalidation=DOB.substring(7,11);
							 String symblvalidation = DOB.substring(2, 3);
							 String symblvalidation1 = DOB.substring(6, 7);
				 			 trace("<<--datevalidation-->"+datevalidation+"<<--monthvalidation-->>"+monthvalidation+"<<--yearvalidation-->"+yearvalidation);
						     trace("<<--Symbolvalidation-->> "+symblvalidation+" <<--Symbolvalidation1-->> "+symblvalidation1);
							 
							 int presentyear = Calendar.getInstance().get(Calendar.YEAR);
							trace("Present Year--->"+presentyear);
							 int firstdate = Integer.parseInt(datevalidation);
							 int yearval = Integer.parseInt(yearvalidation);
							
								List<String> arr = new ArrayList<String>(12);
								 arr.add("JAN"); 					 	arr.add("FEB"); 				arr.add("MAR"); 			arr.add("APR"); 
								 arr.add("MAY"); 					arr.add("JUN"); 				arr.add("JUL"); 			arr.add("AUG"); 
								 arr.add("SEP"); 						arr.add("OCT"); 				arr.add("NOV"); 			arr.add("DEC"); 
								 ans=arr.contains(monthvalidation.toUpperCase());
							   
							if(firstdate<=31 &&yearval<presentyear){
								trace("date and year validation passed");
								if(symblvalidation.equalsIgnoreCase("-")&&symblvalidation1.equalsIgnoreCase("-")){
								trace("symbol validation passed");
										if(ans){
											finalgotvalue="0000";
										trace("month validation passed");
									}else {
										 	finalgotvalue = "10";
											respmsg = "Date Of Birth is Empty or Wrong Format // Sample Date Of Birth Format 10-DEC-2020";
											trace("month validation failed-->"+DOB);}
									}else{
										finalgotvalue = "10";
										respmsg = "Date Of Birth is Empty or Wrong Format // Sample  Date Of Birth Format 10-DEC-2020";
										trace("symbol validation failed-->"+DOB);}
							}else{
									finalgotvalue = "10";
									respmsg = "Date Of Birth is Empty or Wrong Format // Sample Date Of Birth Format 10-DEC-2020";
									trace("date and year validation failed-->"+DOB);
							}}catch(Exception e){
								trace("Date of Birth Validaation Failed-->>"+DOB);
								finalgotvalue = "10";
								respmsg = "Date Of Birth is Empty or Wrong Format // Sample Date Of Birth Format 10-DEC-2020";
					 }}/*}else {
						 trace("Date of Birth  Format Wrong-->>"+DOB);
							finalgotvalue = "10";
							respmsg = "Date Of Birth is Empty or Wrong Format // Date Of Birth Format 10-DEC-2020";
					 }*/
						 
				 		/* 
						 System.out.println("DATE OF BIRTH VALIDATION LOOP-->"+DOB);
						 System.out.println("DATE OF BIRTH VALIDATION LOOP-->"+DOB.length());
						 DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				 try {
					     df.parse(DOB);
						     trace("DOB format is correct"+DOB);
						     System.out.println("DOB format is correct"+DOB);
						} catch (Exception e) {
							finalgotvalue = "10";
							 respmsg = "DOB FROMAT IS WRONG..PROVIDE DD-MON-YYYY FORMAT ONLY ";
						     System.out.println("DOB format is WRONG"+DOB);
						     trace("DOB format is WRONG"+DOB);
						} }else {
							
							finalgotvalue = "10";
							 respmsg = "DOB IS EMPTY OR DOB FROMAT IS WRONG..PROVIDE DD-MON-YYYY FORMAT ONLY ";
						}*/
					 
 
					trace("************ACCOUNTNUMBER // ACCOUNT TYPE // ACCOUNT TYPE VALIDATION STARTS**********"+i);
					
					System.out.println("ACCOUNT DETAILS CHECK 000");
					
					
					if(i!=0 && finalgotvalue.equals("0000")){
					if(!pacct.isEmpty() && !pacct.equalsIgnoreCase("") && !pacct.equalsIgnoreCase("NULL") && pacct.trim().length()!=0)
					{         System.out.println("Primary account number available for rownumber---->"+i);
					        if(!pcurr.isEmpty() && !pcurr.equalsIgnoreCase("") && !pcurr.equalsIgnoreCase("NULL") && pcurr.trim().length()!=0)
						{     System.out.println("Prinamry accounttype available for rownumber --->"+i);
							if(!Pcurrcode.isEmpty() && !Pcurrcode.equalsIgnoreCase("") && !Pcurrcode.equalsIgnoreCase("NULL") && Pcurrcode.trim().length()!=0)
						{     System.out.println("Primary accountcurrency available--->"+i);finalgotvalue="0000";
						}else{System.out.println("PRIMARY ACCOUNT CURRENCY NOT AVAILABLE FOR ROW NUMBER--->"+i);
						respmsg="Pimary account currencncy not available "+i;
						finalgotvalue="pppp";}
						}else{System.out.println("PRIMARY ACCOUNT TYPE NOT AVAILABLE FOR ROW NUMBER --->"+i);
						respmsg="Pimary account type not available "+i;
						finalgotvalue="pppp";}
					    }else{ System.out.println("PRIMARY ACCOUNT NUMBER NOT AVAILABLE");
					    finalgotvalue="PPPP";
					    respmsg="Primary account number details must required";
				}}

					 
					 
					if(i!=0 && finalgotvalue.equals("0000")){
						if(!s1acct.isEmpty() && !s1acct.equalsIgnoreCase("") && !s1acct.equalsIgnoreCase("NULL") && s1acct.trim().length()!=0)
							{         System.out.println("S1 account number available for row number---->"+i);
							        if(!p1curr.isEmpty() && !p1curr.equalsIgnoreCase("") && !p1curr.equalsIgnoreCase("NULL")  && p1curr.trim().length()!=0)
								{     System.out.println("S1 accounttype available for row number--->"+i);
									if(!S1currcode.isEmpty() && !S1currcode.equalsIgnoreCase("") && !S1currcode.equalsIgnoreCase("NULL") && S1currcode.trim().length()!=0)
								{     System.out.println("S1 accountcurrency available for row number--->"+i);
									  finalgotvalue="0000";
								}else{System.out.println("S1 accountcurrency not available for row number--->"+i);
								respmsg="SECONDARY1 account currencncy not available "+i;
								finalgotvalue="pppp";}
								}else{System.out.println("S1 accounttype not available for row number--->"+i);
								respmsg="SECONDARY5 account type not available "+i;
								finalgotvalue="pppp";}
							    }else{System.out.println("S1 account number not available for row number---->"+i);
							        if(!p1curr.isEmpty() && !p1curr.equalsIgnoreCase("") && !p1curr.equalsIgnoreCase("NULL") && p1curr.trim().length()!=0)
								     {System.out.println("S1 account type number available for row number---->"+i);
								     respmsg="SECONDARY1 account type is available but accountnumber is available "+i;
								     finalgotvalue="ssss";
								}else{ System.out.println("S1 accounttype is not available for row number--->"+i);
								    if(!S1currcode.isEmpty() && !S1currcode.equalsIgnoreCase("") && !S1currcode.equalsIgnoreCase("NULL") && S1currcode.trim().length()!=0)	
									 {System.out.println("S1 accountcurrency available for row number--->"+i);
									 respmsg="SECONDARY1 account currencncy is available but account number & accttype is availabe "+i;
									 finalgotvalue="ssss";
								}else{System.out.println("S1 accountcurrency not available for row number--->"+i);finalgotvalue="0000";}}}
						}
					
				 
					if(i!=0 && finalgotvalue.equals("0000")){
						if(!s2acct.isEmpty() && !s2acct.equalsIgnoreCase("") && !s2acct.equalsIgnoreCase("NULL") && s2acct.trim().length()!=0)
							{         System.out.println("S2 account number available for row number---->"+i);
							        if(!p2curr.isEmpty() && !p2curr.equalsIgnoreCase("") && !p2curr.equalsIgnoreCase("NULL")  && p2curr.trim().length()!=0)
								{     System.out.println("S2 accounttype available for row number--->"+i);
									if(!S2currcode.isEmpty() && !S2currcode.equalsIgnoreCase("") && !S2currcode.equalsIgnoreCase("NULL") && S2currcode.trim().length()!=0)
								{     System.out.println("S2 accountcurrency available for row number--->"+i);
									  finalgotvalue="0000";
								}else{System.out.println("S2 accountcurrency not available for row number--->"+i);
								respmsg="SECONDARY2 account currencncy not available "+i;
								finalgotvalue="pppp";}
								}else{System.out.println("S2 accounttype not available for row number--->"+i);
								respmsg="SECONDARY2 account type not available "+i;
								finalgotvalue="pppp";}
							    }else{System.out.println("S2 account number not available for row number---->"+i);
							        if(!p2curr.isEmpty() && !p2curr.equalsIgnoreCase("") && !p2curr.equalsIgnoreCase("NULL") && p2curr.trim().length()!=0)
								     {System.out.println("S2 account type number available for row number---->"+i);
								     respmsg="SECONDARY2 account type is available but accountnumber is available "+i;finalgotvalue="ssss";
								}else{ System.out.println("S2 accounttype is not available for row number--->"+i);
								    if(!S2currcode.isEmpty() && !S2currcode.equalsIgnoreCase("") && !S2currcode.equalsIgnoreCase("NULL") && S2currcode.trim().length()!=0)	
									 {System.out.println("S2 accountcurrency available for row number--->"+i);
									 respmsg="SECONDARY2 account currencncy is available but account number & accttype is availabe "+i;
									 finalgotvalue="ssss";
								}else{System.out.println("S2 accountcurrency not available for row number--->"+i);finalgotvalue="0000";}}}
						}
				 
					if(i!=0 && finalgotvalue.equals("0000")){
						if(!s3acct.isEmpty() && !s3acct.equalsIgnoreCase("") && !s3acct.equalsIgnoreCase("NULL") && s3acct.trim().length()!=0)
							{         System.out.println("S3 account number available for row number---->"+i);
							        if(!p3curr.isEmpty() && !p3curr.equalsIgnoreCase("") && !p3curr.equalsIgnoreCase("NULL")  && p3curr.trim().length()!=0)
								{     System.out.println("S3 accounttype available for row number--->"+i);
									if(!S3currcode.isEmpty() && !S3currcode.equalsIgnoreCase("") && !S3currcode.equalsIgnoreCase("NULL") && S3currcode.trim().length()!=0)
								{     System.out.println("S3 accountcurrency available for row number--->"+i);
									  finalgotvalue="0000";
								}else{System.out.println("S3 accountcurrency not available for row number--->"+i);
								respmsg="SECONDARY3 account currencncy not available "+i;
								finalgotvalue="pppp";}
								}else{System.out.println("S3 accounttype not available for row number--->"+i);
								respmsg="SECONDARY3 account type not available "+i;
								finalgotvalue="pppp";}
							    }else{System.out.println("S3 account number not available for row number---->"+i);
							        if(!p1curr.isEmpty() && !p3curr.equalsIgnoreCase("") && !p3curr.equalsIgnoreCase("NULL") && p3curr.trim().length()!=0)
								     {System.out.println("S3 account type number available for row number---->"+i);
								     respmsg="SECONDARY3 account type is available but accountnumber is available "+i;
								     finalgotvalue="ssss";
								}else{ System.out.println("S3 accounttype is not available for row number--->"+i);
								    if(!S3currcode.isEmpty() && !S3currcode.equalsIgnoreCase("") && !S3currcode.equalsIgnoreCase("NULL") && S3currcode.trim().length()!=0)	
									 {System.out.println("S3 accountcurrency available for row number--->"+i);
									 respmsg="SECONDARY3 account currencncy is available but account number & accttype is availabe "+i;
									 finalgotvalue="ssss";
								}else{System.out.println("S3 accountcurrency not available for row number--->"+i);finalgotvalue="0000";}}}
						}
				 
					if(i!=0 && finalgotvalue.equals("0000")){
						if(!s4acct.isEmpty() && !s4acct.equalsIgnoreCase("") && !s4acct.equalsIgnoreCase("NULL") && s4acct.trim().length()!=0)
							{         System.out.println("S4 account number available for row number---->"+i);
							        if(!p4curr.isEmpty() && !p4curr.equalsIgnoreCase("") && !p4curr.equalsIgnoreCase("NULL")  && p4curr.trim().length()!=0)
								{     System.out.println("S4 accounttype available for row number--->"+i);
									if(!S4currcode.isEmpty() && !S4currcode.equalsIgnoreCase("") && !S4currcode.equalsIgnoreCase("NULL") && S4currcode.trim().length()!=0)
								{     System.out.println("S4 accountcurrency available for row number--->"+i);
									  finalgotvalue="0000";
								}else{System.out.println("S4 accountcurrency not available for row number--->"+i);
								respmsg="SECONDARY4 account currencncy not available "+i;finalgotvalue="pppp";}
								}else{System.out.println("S4 accounttype not available for row number--->"+i);
								respmsg="SECONDARY4 account type not available "+i;
								finalgotvalue="pppp";}
							    }else{System.out.println("S4 account number not available for row number---->"+i);
							        if(!p4curr.isEmpty() && !p4curr.equalsIgnoreCase("") && !p4curr.equalsIgnoreCase("NULL") && p4curr.trim().length()!=0)
								     {System.out.println("S4 account type number available for row number---->"+i);
								     respmsg="SECONDARY4 account type is available but accountnumber is available "+i;
								     finalgotvalue="ssss";
								}else{ System.out.println("S4 accounttype is not available for row number--->"+i);
								    if(!S4currcode.isEmpty() && !S4currcode.equalsIgnoreCase("") && !S4currcode.equalsIgnoreCase("NULL") && S4currcode.trim().length()!=0)	
									 {System.out.println("S4 accountcurrency available for row number--->"+i);
									 respmsg="SECONDARY4 account currencncy is available but account number & accttype is availabe "+i;
									 finalgotvalue="ssss";
								}else{System.out.println("S4 accountcurrency not available for row number--->"+i);finalgotvalue="0000";}}}
						}
				 
					
					if(i!=0 && finalgotvalue.equals("0000")){
						if(!s5acct.isEmpty() && !s5acct.equalsIgnoreCase("") && !s5acct.equalsIgnoreCase("NULL") && s5acct.trim().length()!=0)
							{         System.out.println("S5 account number available for row number---->"+i);
							        if(!p5curr.isEmpty() && !p5curr.equalsIgnoreCase("") && !p5curr.equalsIgnoreCase("NULL")  && p5curr.trim().length()!=0)
								{     System.out.println("S5 accounttype available for row number--->"+i);
									if(!S5currcode.isEmpty() && !S5currcode.equalsIgnoreCase("") && !S5currcode.equalsIgnoreCase("NULL") && S5currcode.trim().length()!=0)
								{     System.out.println("S5 accountcurrency available for row number--->"+i);
									  finalgotvalue="0000";
								}else{System.out.println("S5 accountcurrency not available for row number--->"+i);
								respmsg="SECONDARY5 account currencncy not available "+i;
								finalgotvalue="pppp";}
								}else{System.out.println("S5 accounttype not available for row number--->"+i);
								respmsg="SECONDARY5 account type not available "+i;
								finalgotvalue="pppp";}
							    }else{System.out.println("S5 account number not available for row number---->"+i);
							        if(!p5curr.isEmpty() && !p5curr.equalsIgnoreCase("") && !p5curr.equalsIgnoreCase("NULL") && p5curr.trim().length()!=0)
								     {System.out.println("S5 account type number available for row number---->"+i);
										respmsg="SECONDARY5 account type is available but accountnumber is available "+i;
								     finalgotvalue="ssss";
								}else{ System.out.println("S5 accounttype is not available for row number--->"+i);
								    if(!S5currcode.isEmpty() && !S5currcode.equalsIgnoreCase("") && !S5currcode.equalsIgnoreCase("NULL") && S5currcode.trim().length()!=0)	
									 {System.out.println("S5 accountcurrency available for row number--->"+i);
										respmsg="SECONDARY5 account currencncy is available but account number & accttype is availabe "+i;
									 finalgotvalue="ssss";
								}else{System.out.println("S5 accountcurrency not available for row number--->"+i);finalgotvalue="0000";}}}
						}

 	
					trace("************ACCOUNTNUMBER // ACCOUNT TYPE // ACCOUNT TYPE VALIDATION ENDS**********"+i);			   
					
					System.out.println("CHECK9");
					if (i != 0) {
						if (finalgotvalue.equalsIgnoreCase("0000")) {
							List accountdupAvailable = dao.findaccountduplication(instid, pacct, s1acct, s2acct, s3acct,
									s4acct, s5acct, jdbctemplate);

							// System.out.println("accountdupAvailable"+accountdupAvailable);
							if (accountdupAvailable == null) {
								finalgotvalue = "0000";
							} else {
								finalgotvalue = "23";
								respmsg = "Duplicated Accounts Are " + accountdupAvailable;
							}
						}

						if (finalgotvalue.equalsIgnoreCase("0000")) {
							String checkbatchid = "SELECT COUNT(BATCH_ID) FROM CUSTOMER_BULK_LOAD WHERE  BATCH_ID ='"
									+ batchname + "' ";
							// String
							// existfilename=(String)jdbctemplate.queryForObject(filenamecheck,
							// String.class);
							int checkbid = jdbctemplate.queryForInt(checkbatchid);

							if (checkbid > 1000) {
								finalgotvalue = "19";
								respmsg = "Batch ID Limit Exceeded More than 1000/Kindly use other Batch ID Name";
							} else {

								finalgotvalue = "0000";

							}
						}

	 					if (finalgotvalue.equalsIgnoreCase("0000")) {
							String cinaccountdup = "SELECT COUNT(*) FROM CUSTOMER_BULK_LOAD WHERE  CUSTOMER_ID='"
									+ custid.trim() + "' OR   P_ACCOUNT_NO ='" + pacct.trim() + "' ";
							int cinACcheck = jdbctemplate.queryForInt(cinaccountdup);
							if (cinACcheck > 0) {
								finalgotvalue = "20";
								respmsg = "CustomerId  or PrimayAccount Numbers Duplicated New Reg ";
							} else {

								finalgotvalue = "0000";

							}
						}
						if (finalgotvalue.equalsIgnoreCase("0000")) {
							String cinaccountdup = "SELECT COUNT(*) FROM ACCOUNTINFO WHERE  CIN='" + custid.trim()
									+ "' AND ACCOUNTNO ='" + pacct.trim() + "' ";
							int cinACcheck = jdbctemplate.queryForInt(cinaccountdup);
							if (cinACcheck > 0) {
								finalgotvalue = "21";
								respmsg = "CustomerId & PrimayAccount NUmbers Duplicated Old Reg";
							} else {

								finalgotvalue = "0000";

							}
						}
						if (finalgotvalue.equalsIgnoreCase("0000")) {
							String branchvalidation = "SELECT COUNT(*) FROM BRANCH_MASTER WHERE  BRANCH_CODE ='"+ branch.trim() + "' and  "
									+ "branch_code !='000' and auth_code='1' ";
							int brcheck = jdbctemplate.queryForInt(branchvalidation);

							if (brcheck == 0) {
								finalgotvalue = "22";
								respmsg = "[ " + branch + " ] - Branch Is Not Configured or Not Authorized, Kinldy Configure the Branch";
							} else {

								finalgotvalue = "0000";

							}
						}
						
						
						if(finalgotvalue.equalsIgnoreCase("0000")) {
							
							String productvalidation = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE  PRODUCT_CODE ='"+ product_code.trim() + "'";
							int productcheck = jdbctemplate.queryForInt(productvalidation);
							System.out.println("product check count--->"+productcheck);
							if (productcheck == 0) {
							finalgotvalue = "23";
							respmsg = "[ " + product_code + " ] - PRODUCT_CODE Is Not Configured,Kinldy  check the product_code";
							 } else {
							finalgotvalue = "0000";
							}
						}
						
						
					}
	 	
					System.out.println("finalgotvalue " + finalgotvalue);
					if (i != 0) {
						total++;
					}
					if (i != 0 && finalgotvalue.equalsIgnoreCase("0000")) {

						x = jdbctemplate.update(InsertQry);
						if (x > 0) {
							count++;
						}
						//
					} else {
						if (i != 0) {
							int insertfail = dao.insertbulkregcardsstatus(instid, filename, CUSTOMERID, i, usercode,
									randomreportid, finalgotvalue, respmsg, jdbctemplate);
							if (insertfail > 0) {
								failure++;
							}
						}
					}

					List finalstatus = new ArrayList();
					List statuslist = this.getUploadedStatus(instid, randomreportid, usercode, jdbctemplate);
					Iterator itr = statuslist.iterator();
					while (itr.hasNext()) {
						Map mp = (Map) itr.next();
						mp.put("REPORTRANDOMNO", randomreportid);
						mp.put("FILENAME", filename);
						mp.put("UPLOADEDBY", ((String) mp.get("UPLOADEDBY")));
						mp.put("UPLOADDATE", ((String) mp.get("UPLOADDATE")));
						mp.put("TOTALRECORD", total);
						mp.put("SUCCESS", count);
						mp.put("FAIL", failure);
						finalstatus.add(mp);
					}
					setUploadStatus(finalstatus);

					P_CURRENCY_CODE = "";
					DOB = "";
					branch = "";
					pacct = "";
					s1acct = "";
					s2acct = "";
					s3acct = "";
					s4acct = "";
					s5acct = "";
					pcurr = "";
					p1curr = "";
					p2curr = "";
					p3curr = "";
					p4curr = "";
					p5curr = "";
					mobile = "";
					batchname = "";
					custid = "";
					Pcurrcode="";
					S1currcode="";
					S2currcode="";
					S3currcode="";
					S4currcode="";
					S5currcode="";
					NAME="";

					/************* AUDIT BLOCK **************/
					try {
						auditbean.setActmsg(" inserted query " + count);
						auditbean.setUsercode(usercode);
						auditbean.setAuditactcode("9102");
						// auditbean.setCardno(padsssec.getMakedCardno(CardNumber));
						// auditbean.setCardnumber(enccardno);
						// commondesc.insertAuditTrail(in_name, Maker_id,
						// auditbean, jdbctemplate, txManager);
						commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean, jdbctemplate, txManager);
					} catch (Exception audite) {
						trace("Exception in auditran : " + audite.getMessage());
					}
					/************* AUDIT BLOCK **************/

				}
			}
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			addActionMessage("Successfully Registred Customer [ " + count + "] : Failed to Registred Customer ["
					+ failure + "] ");

		}

		catch (Exception e) {
			e.printStackTrace();
			if (i == 0) {
				addActionError("File Format Not-Matched (or) Sheet Name Not-Matched (or) Columns are Not-Matched...  ");
			} else {
				//addActionError("Check with excelfile line NUMBER " + i + " query: " + InsertQry);
				addActionError("Check with excelfile line NUMBER  " + i);
				trace("Check with excelfile line NUMBER " + i + " query: " + InsertQry);
			}

			txManager.rollback(transact.status);
			trace("Could not insert the  bulk Process records" + e.getMessage());

		}
		/*
		 * finally {
		 * 
		 * workbook.close(); }
		 */

		System.out.println("UploadSQLFile" + "called");

		return "customerbulkregister1";

	}
	
	 	
	
	private static Object length() {
		// TODO Auto-generated method stub
		return null;
	}

				///COmmented on 11-NOV-2020 BY gowtham
/*
	public String changetoloststone() throws Exception {
		trace("### UploadSQLFile Method Called ###");
		IfpTransObj transact = commondesc.myTranObject("BULKUPLOAD", txManager);
		Iterator filenames = uploadFileFileName.iterator();
		Iterator upfiles = uploadFile.iterator();

		HttpSession session = getRequest().getSession();
		String usercode = comUserId(session);
		String instid = comInstId(session);
		int count = 0, failure = 0, i = 0;
		String InsertQry = "";
		String auditmsg = "", message = "";
		try {
			while (filenames.hasNext()) {
				String filedata = (String) upfiles.next();
				String filename = (String) filenames.next();
				String filenamecheck = "SELECT COUNT(*) FROM BULKFAIL_REG_STATUS WHERE INST_ID='" + instid
						+ "' AND FILENAME ='" + filename + "' ";
				// String
				// existfilename=(String)jdbctemplate.queryForObject(filenamecheck,
				// String.class);
				int existfilename = jdbctemplate.queryForInt(filenamecheck);
				if (existfilename > 0) {
					addActionError("File is Already Exists,Use Diffrent File name..");
					return "changeLostStolen";

				}
				System.out.println("sa" + filename + "\n " + filedata + "new FileInputStream((filename))"
						+ new FileInputStream((filedata)));
				// new BufferedReader(new InputStreamReader(new FileInputStream(
				File file = new File(filename);
				System.out.println("\t" + "Path : " + file.getAbsolutePath());
				InputStream inputStream = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(filedata)));
				System.out.println(inputStream);
				Workbook workbook = Workbook.getWorkbook(new FileInputStream((filedata)));

				Sheet sheet = workbook.getSheet(0);
				String[] sheetName = workbook.getSheetNames();
				int totalNoOfRows = sheet.getRows();
				int totalNoOfCols = sheet.getColumns();

				// System.out.println( "Sheet Name: "+sheetName[0] ); /* Table
				// Name Must be the Xls Sheet Name 

				 Composing Header data 
				String Header = "";
				int ReplacementTypeColumn = 0, CardNumColumn = 0, Column = 0, Cardcollectbranch = 0,
						MobileNumColumn = 0;
				String ReplacementType = "";
				String CardNumber = "", mobileNumber = "", crdcltBranch = "";

				 Searching Header to find CardNumber & Replacement type 
				for (int FirstColumnHeaders = 0; FirstColumnHeaders < totalNoOfCols; FirstColumnHeaders++) {
					if (!sheet.getCell(FirstColumnHeaders, 0).getContents().isEmpty()) {
						Cell cell1 = sheet.getCell(FirstColumnHeaders, 0);
						Header = cell1.getContents();
						if (Header.equalsIgnoreCase("CARD_NUMBER"))
							CardNumColumn = FirstColumnHeaders;
						if (Header.equalsIgnoreCase("REPLACEMENT_TYPE"))
							ReplacementTypeColumn = FirstColumnHeaders;
						if (Header.equalsIgnoreCase("MOBILE"))
							MobileNumColumn = FirstColumnHeaders;
						if (Header.equalsIgnoreCase("BRANCH"))
							Cardcollectbranch = FirstColumnHeaders;
					}
				}
				int startnumber = 54;
				String notype = "";
				long randomreportid = this.generateRandom(startnumber);
				// System.out.println("randomreportid::"+randomreportid);

				String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
				String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);

				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				String eDMK = "", eDPK = "";
				Iterator secitr = secList.iterator();
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					eDMK = ((String) map.get("DMK"));
					eDPK = ((String) map.get("DPK"));
				}

				 Composing Values for the available rows 
				int total = 0;
				for (i = 1; i < totalNoOfRows; i++) {
					total++;
					Column = CardNumColumn; // Card Number
					if (!sheet.getCell(Column, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column, i);
						CardNumber = cell.getContents();
					}
					Column = ReplacementTypeColumn; // Replacement Type [
													// Re-Issue / Re-New ]
					if (!sheet.getCell(Column, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column, i);
						ReplacementType = cell.getContents();
					}
					Column = MobileNumColumn; // Card Number
					if (!sheet.getCell(Column, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column, i);
						mobileNumber = cell.getContents();
					}
					Column = Cardcollectbranch; // Replacement Type [ Re-Issue /
												// Re-New ]
					if (!sheet.getCell(Column, i).getContents().isEmpty()) {
						Cell cell = sheet.getCell(Column, i);
						crdcltBranch = cell.getContents();
					}

					// validateNumeric

					
					 * if(CardNumber.matches("[a-zA-Z]+")){ System.out.println(
					 * "income for alphabete check .."); addActionError(
					 * "This file has the AlPHBATE in card number/Kinldy use correct file or check the file data"
					 * ); return chnageloststolen();
					 * 
					 * }
					 
					trace(" Card Number: " + CardNumber + ", Replacement Type: " + ReplacementType);

					// System.out.println("before TO ***************REISSUE ");
					int totalcount = 0;

					if (ReplacementType.trim().equalsIgnoreCase("REISSUE")
							|| ReplacementType.trim().equalsIgnoreCase("RENEW")) {
						if (ReplacementType.trim().equalsIgnoreCase("REISSUE")) {
							// lost stolen
							trace("COMING TO ***************REISSUE ");
							auditmsg = "Marked Lost Stolen[ " + padsssec.getECHN(eDMK, eDPK, CardNumber) + " ]";
							message = "Records Successfully Changed to Lost Stolen Status";
							int checkcardcount = dao.checkcardcount(instid,
									padsssec.getHashedValue(CardNumber + instid).toString(), jdbctemplate);
							int updatedcards = 0;

							String checkbranchqry = "select count(*) from  BRANCH_MASTER WHERE BRANCH_CODE='"
									+ crdcltBranch.trim() + "' AND INST_ID='" + instid + "' ";

							int branchcount = jdbctemplate.queryForInt(checkbranchqry);

							int result = 0;
							if (checkcardcount > 0) {
								String hcardno = padsssec.getHashedValue(CardNumber + instid).toString();
								String query1 = "select product_code FROM CARD_PRODUCTION WHERE HCARD_NO='" + hcardno
										+ "' ";
								String productcode = (String) jdbctemplate.queryForObject(query1, String.class);
								trace("product code checking -->" + productcode);
								if ("5391711101".equalsIgnoreCase(productcode)) {
									trace("INSIDE product code checking -->" + productcode);
									long millis = System.currentTimeMillis();
									java.sql.Date date = new java.sql.Date(millis);
									System.out.println("TTTTTTTTT--->" + date);
									trace("TTTTTTTTT--->" + date);

									String query = "select a.EXPIRY_DATE, a.org_chn,a.emb_name,NVL(a.mobileno,0) as mobileno,NVL(b.e_mail,0) as e_mail from card_production a, customerinfo b where a.INST_ID='"
											+ instid + "' AND a.HCARD_NO = '" + hcardno + "'and a.cin=b.CIN";
									List card_data = jdbctemplate.queryForList(query);

									String mobileNo = "", embname = "", clearCard = "", emailId = "", EXPDATE = "";

									if (card_data.isEmpty()) {
										session.setAttribute("preverr", "E");
										session.setAttribute("prevmsg", "No data Available");
										return "serach_home";
									}

									Iterator orderno = card_data.iterator();
									if (!card_data.isEmpty()) {
										while (orderno.hasNext()) {
											Map map = (Map) orderno.next();
											mobileNo = ((String) map.get("MOBILENO"));
											clearCard = (String) map.get("ORG_CHN");
											embname = ((String) map.get("EMB_NAME"));
											emailId = ((String) map.get("E_MAIL"));
											EXPDATE = ((String) map.get("EXPIRY_DATE"));
										}
									}

									// String customerDataReportQuery="INSERT
									// INTO CARD_REPORT
									// (INST_ID,ACTION_CODE,CHN,EMB_NAME,MOBILE,DOWN_CNT,EMAIL,USER_ID,ACTION_DATE,EXPDATE)
									// "
									String customerDataReportQuery = "INSERT INTO CARD_REPORT (INST_ID,ACTION_CODE,CHN,EMB_NAME,MOBILE,DOWN_CNT,EMAIL,USER_ID,ACTION_DATE) "
											+ " values(?,?,?,?,?,?,?,?)";
									result = jdbctemplate.update(customerDataReportQuery, new Object[] { instid, "06",
											clearCard, embname, mobileNo, "0", emailId, usercode, date });
									trace("result ::: " + result);
								}

								updatedcards = dao.updateproductionezcardinfo(instid,
										padsssec.getHashedValue(CardNumber + instid).toString(), usercode, mobileNumber,
										crdcltBranch, jdbctemplate);
								if (updatedcards > 0 && result > 0) {
									count++;
									trace(" Succcess records " + checkcardcount + "\n updatedcards " + updatedcards);
								}
							}

							else {
								if (branchcount == 0) {
									notype = "110";
								}
								int insertfail = dao.insertnotavailable(instid, filename, CardNumber, i, usercode,
										randomreportid, padsssec.getHashedValue(CardNumber + instid).toString(), notype,
										"LS", crdcltBranch, jdbctemplate);
								if (insertfail > 0) {
									failure++;
								}
							}
						}

						if (ReplacementType.trim().equalsIgnoreCase("RENEW")) {
							// bulk renewal

							trace("COMING TO ***************RENEW ");
							auditmsg = "Marked Renewal Status[ " + padsssec.getECHN(eDMK, eDPK, CardNumber)
									+ " ] AND MObiel No" + mobileNumber + " And branch" + crdcltBranch;
							message = "Records Successfully Changed to Renewal Status";
							String renewalperiods = this.getRenewalPeriods(instid, jdbctemplate);
							int checkcardcount = dao.checkcardexpiry(instid,
									padsssec.getHashedValue(CardNumber + instid).toString(), renewalperiods,
									jdbctemplate);

							if (checkcardcount != 0) {
								List checkcardcount1 = dao.chekcardsforbulkrenewal(instid,
										padsssec.getHashedValue(CardNumber + instid).toString(), renewalperiods,
										jdbctemplate);
								// System.out.println("checkcardcount1+checkcardcount1"+checkcardcount1);
								String subprodid = "";
								int Aailable = -1;

								if (checkcardcount1 != null) {
									Iterator listcards = checkcardcount1.iterator();

									while (listcards.hasNext()) {
										Map map = (Map) listcards.next();
										Aailable = ((BigDecimal) map.get("COUNT")).intValue();

										subprodid = ((String) map.get("SUB_PROD_ID"));

									}
									// System.out.println("Aailable"+Aailable);

									String changeexpreq = "Y";
									String caf_recstatus = "BR";
									// String waitingmsg=" Waiting For Security
									// Data Generation";
									String card_status = "01";

									// System.out.println("Aailablesssssssssssssssssss"+Aailable);
									if (Aailable == 1) {
										String checkbranchqry = "select count(*) from  BRANCH_MASTER WHERE BRANCH_CODE='"
												+ crdcltBranch.trim() + "' AND INST_ID='" + instid + "' ";

										int branchcount = jdbctemplate.queryForInt(checkbranchqry);
										if (branchcount != 0) {
											String expperiod = commondesc.getSubProductExpPeriod(instid, subprodid,
													jdbctemplate);
											int moveprocess = this.movebulkrenuallistinsert(instid, caf_recstatus,
													card_status, expperiod, changeexpreq, "CARD_PRODUCTION",
													padsssec.getHashedValue(CardNumber + instid).toString(),
													mobileNumber, crdcltBranch, jdbctemplate);
											int x = -1;
											if (moveprocess > 0) {
												x = jdbctemplate.update("UPDATE CARD_PRODUCTION SET CAF_REC_STATUS='"
														+ caf_recstatus + "',BRANCH_CODE='" + crdcltBranch
														+ "' ,CARD_COLLECT_BRANCH='" + crdcltBranch + "' ,MOBILENO='"
														+ mobileNumber
														+ "',STATUS_CODE='78',CARD_STATUS='12' WHERE HCARD_NO='"
														+ padsssec.getHashedValue(CardNumber + instid).toString()
														+ "' and INST_ID = '" + instid + "'");
												jdbctemplate.update("UPDATE EZCARDINFO SET STATUS='78' WHERE CHN='"
														+ padsssec.getHashedValue(CardNumber + instid).toString()
														+ "'");
												jdbctemplate.update("UPDATE EZCUSTOMERINFO SET MOBILE='" + mobileNumber
														+ "' WHERE INSTID='" + instid
														+ "' AND CUSTID IN (SELECT CIN FROM CARD_PRODUCTION WHERE HCARD_NO='"
														+ padsssec.getHashedValue(CardNumber + instid).toString()
														+ "' AND INST_ID='" + instid + "' AND ROWNUM=1)");
											}

											if (x > 0) {

												String hcardno = padsssec.getHashedValue(CardNumber + instid)
														.toString();
												String query1 = "select product_code FROM CARD_PRODUCTION WHERE HCARD_NO='"
														+ hcardno + "' ";
												String productcode = (String) jdbctemplate.queryForObject(query1,
														String.class);
												trace("product code checking -->" + productcode);
												if ("5391711101".equalsIgnoreCase(productcode)) {
													trace("INSIDE product code checking -->" + productcode);
													long millis = System.currentTimeMillis();
													java.sql.Date date = new java.sql.Date(millis);
													System.out.println("TTTTTTTTT--->" + date);
													trace("TTTTTTTTT--->" + date);

													String query = "select a.EXPIRY_DATE, a.org_chn,a.emb_name,NVL(a.mobileno,0) as mobileno,NVL(b.e_mail,0) as e_mail from card_production a, customerinfo b where a.INST_ID='"
															+ instid + "' AND a.HCARD_NO = '" + hcardno
															+ "'and a.cin=b.CIN";
													List card_data = jdbctemplate.queryForList(query);

													String mobileNo = "", embname = "", clearCard = "", emailId = "",
															EXPDATE = "";

													if (card_data.isEmpty()) {
														session.setAttribute("preverr", "E");
														session.setAttribute("prevmsg", "No data Available");
														return "serach_home";
													}

													Iterator orderno = card_data.iterator();
													if (!card_data.isEmpty()) {
														while (orderno.hasNext()) {
															Map map = (Map) orderno.next();
															mobileNo = ((String) map.get("MOBILENO"));
															clearCard = (String) map.get("ORG_CHN");
															embname = ((String) map.get("EMB_NAME"));
															emailId = ((String) map.get("E_MAIL"));
															EXPDATE = ((String) map.get("EXPIRY_DATE"));
														}
													}

													// String
													// customerDataReportQuery="INSERT
													// INTO CARD_REPORT
													// (INST_ID,ACTION_CODE,CHN,EMB_NAME,MOBILE,DOWN_CNT,EMAIL,USER_ID,ACTION_DATE,
													// EXPDATE) "
													String customerDataReportQuery = "INSERT INTO CARD_REPORT (INST_ID,ACTION_CODE,CHN,EMB_NAME,MOBILE,DOWN_CNT,EMAIL,USER_ID,ACTION_DATE) "
															+ " values(?,?,?,?,?,?,?,?)";
													int result = jdbctemplate.update(customerDataReportQuery,
															new Object[] { instid, "04", clearCard, embname, mobileNo,
																	"0", emailId, usercode, date });
													trace("result ::: " + result);
												}

												trace(" Succcess bulk records for cards "
														+ padsssec.getHashedValue(CardNumber + instid).toString());
												count++;
											} else {
												trace(" Failure bulk records for cards "
														+ padsssec.getHashedValue(CardNumber + instid).toString());
											}
										}

										else {
											notype = "110";
											// System.out.println("200
											// ins"+Aailable);
											int insertfail = dao.insertnotavailable(instid, filename, CardNumber, i,
													usercode, randomreportid,
													padsssec.getHashedValue(CardNumber + instid).toString(), notype,
													"RN", crdcltBranch, jdbctemplate);
											if (insertfail > 0) {
												failure++;
											}
										}

									} else {
										notype = "200";
										// System.out.println("200
										// ins"+Aailable);
										int insertfail = dao.insertnotavailable(instid, filename, CardNumber, i,
												usercode, randomreportid,
												padsssec.getHashedValue(CardNumber + instid).toString(), notype, "RN",
												crdcltBranch, jdbctemplate);
										if (insertfail > 0) {
											failure++;
										}
									}
								}
							}

							else {
								// System.out.println("Aailablesssssnnnnnnnn"+Aailable);
								int insertfail = dao.insertnotavailable(instid, filename, CardNumber, i, usercode,
										randomreportid, padsssec.getHashedValue(CardNumber + instid).toString(), notype,
										"RN", crdcltBranch, jdbctemplate);
								if (insertfail > 0) {
									failure++;
								}
							}

						}

					} else {
						// System.out.println("Before coming to
						// RENEW/RESSUE"+CardNumber);
						notype = "100";
						int insertfail = dao.insertnotavailable(instid, filename, CardNumber, i, usercode,
								randomreportid, padsssec.getHashedValue(CardNumber + instid).toString(), notype, "RN",
								crdcltBranch, jdbctemplate);
						if (insertfail > 0) {
							failure++;
						}
					}

					
					 * if(!ReplacementType.trim().matches("REISSUE-RENEW")){
					 * 
					 * System.out.println(
					 * "coming to RENEW/RESSUE mmmmmmmmmmmmmm"+ReplacementType);
					 * notype="100"; message=
					 * "Replacement type of the cards is not in [ RENEW/RESSUE ]"
					 * ; int insertfail = dao.insertnotavailable( instid,
					 * filename,CardNumber ,
					 * i,usercode,randomreportid,padsssec.getHashedValue(
					 * CardNumber+instid).toString(),notype,jdbctemplate);
					 * if(insertfail>0){ failure++; } }
					 
				}

				List finalstatus = new ArrayList();
				List statuslist = this.getUploadedStatus(instid, randomreportid, usercode, jdbctemplate);
				Iterator itr = statuslist.iterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					mp.put("REPORTRANDOMNO", randomreportid);
					mp.put("FILENAME", filename);
					mp.put("UPLOADEDBY", ((String) mp.get("UPLOADEDBY")));
					mp.put("UPLOADDATE", ((String) mp.get("UPLOADDATE")));
					mp.put("TOTALRECORD", total);
					mp.put("SUCCESS", count);
					mp.put("FAIL", failure);
					finalstatus.add(mp);
				}
				setUploadStatus(finalstatus);

				*//************* AUDIT BLOCK ************//*

				*//************* AUDIT BLOCK **************//*
				try {
					for (int k = 0; k < 3; k++) {

						auditbean.setActmsg(auditmsg);
						auditbean.setUsercode(usercode);
						auditbean.setAuditactcode("4102");
						auditbean.setAuditactcode("0110");
						auditbean.setCardno(padsssec.getMakedCardno(CardNumber));

						// auditbean.setCardnumber(enccardno);
						// commondesc.insertAuditTrail(in_name, Maker_id,
						// auditbean, jdbctemplate, txManager);
						commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean, jdbctemplate, txManager);
					}
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				*//************* AUDIT BLOCK **************//*

			}

			txManager.commit(transact.status);
			addActionMessage("  " + message + "  Success " + count + "  Failure cards '" + failure + "' ");
			trace(count + " Records status changed and Filed for '" + failure + "' ");

		}

		catch (Exception e) {
			e.printStackTrace();
			if (i == 0) {
				addActionError("File Format Not-Matched (or) Sheet Name Not-Matched (or) Columns are Not-Matched...  ");
			} else {
				addActionError("Check with excelfile line NUMBER " + i + " query: " + InsertQry);
			}

			txManager.rollback(transact.status);

			trace("Could not insert the  bulk Process records" + e.getMessage());

		}

		System.out.println("UploadSQLFile" + "called");
		return "changeLostStolen";

	}*/

	private ByteArrayOutputStream output_stream;
	private ByteArrayInputStream input_stream;
	private String report_name;

	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}

	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}

	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}

	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	private List getUploadedStatus(String instid, long randomreportid, String userid, JdbcTemplate jdbctemplate2) {
		List result = null;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append(
					"SELECT DISTINCT TO_CHAR(ADDED_DATE,'DD-MON-YYYY') UPLOADDATE,(SELECT USERNAME FROM USER_DETAILS WHERE USERID IN (SELECT ADDED_BY FROM BULKFAIL_REG_STATUS WHERE REPORTRANDOMNO='"
							+ randomreportid + "' )) UPLOADEDBY ");
			strbuild.append("FROM BULKFAIL_REG_STATUS WHERE INST_ID='" + instid + "' AND REPORTRANDOMNO='"
					+ randomreportid + "' ");
			enctrace("Get Final Status Qry ::: " + strbuild);
			result = jdbctemplate.queryForList(strbuild.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static long generateRandom(int prefix) {
		Random rand = new Random();

		long x = (long) (rand.nextDouble() * 100000000000L);

		String s = String.valueOf(prefix) + String.format("%011d", x);
		return Long.valueOf(s);
	}

	public String viewFailedRecords() {
		trace("*********Generate report for uploaded failed status********");
		String usercode = comUserId(session);
		String instid = comInstId(session);

		String filename = getRequest().getParameter("filename");

		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String curdatetime = new SimpleDateFormat("dd-MM-yyyy_HH:MM:SS").format(new Date());
		String propertyname = "cardreceivefailedreport";
		List recordlist = null;
		try {
			int ALIGN_LEFT = 0, ALIGN_CENTER = 1, ALIGN_RIGHT = 2;
			String reportname = "Bulk_Lost-Stolen(or)Re-new_FailReport_";

			PDFReportGenerator pdfgen = new PDFReportGenerator(document, output_stream, propertyname, getRequest());
			String title = "BULK LOST-STOLEN (OR)RE-NEW_FAILREPORT_";
			pdfgen.addPDFTitles(document, title, ALIGN_CENTER);

			trace("Report Name is " + reportname + curdatetime + ".pdf");
			setReport_name(reportname + curdatetime + ".pdf");

			recordlist = this.gettingUploadedFailedDetails(instid, filename, jdbctemplate);

			if (recordlist.isEmpty()) {
				trace("No Records found");
				addActionError("No Records Found");
			} else {

				pdfgen.createSimplePDF(document, title, pdfgen.reportheader, recordlist, pdfgen.reportsumfield,
						ALIGN_CENTER, ALIGN_CENTER, 100);
				pdfgen.closePDF(document);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());
				output_stream.flush();
				return "itextpdfreport";
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Unable to continue the report generation");
			trace("Exception in Loop ::: " + e.getMessage());
		}

		return "changeLostStolen";
	}

	public String viewFailedRecordsbulkreg() {
		trace("*********Generate report for uploaded failed status********");
		String usercode = comUserId(session);
		String instid = comInstId(session);

		String filename = getRequest().getParameter("filename");

		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String curdatetime = new SimpleDateFormat("dd-MM-yyyy_HH:MM:SS").format(new Date());
		String propertyname = "cardreceivefailedreport";
		List recordlist = null;
		try {
			int ALIGN_LEFT = 0, ALIGN_CENTER = 1, ALIGN_RIGHT = 2;
			String reportname = "Bulk_Customer_Registration_FailReport_";

			PDFReportGenerator pdfgen = new PDFReportGenerator(document, output_stream, propertyname, getRequest());
			String title = "BULK CUSTOMER REGISTRATION";
			pdfgen.addPDFTitles(document, title, ALIGN_CENTER);

			trace("Report Name is " + reportname + curdatetime + ".pdf");
			setReport_name(reportname + curdatetime + ".pdf");

			recordlist = this.gettingUploadedFailedDetails(instid, filename, jdbctemplate);

			if (recordlist.isEmpty()) {
				trace("No Records found");
				addActionError("No Records Found");
			} else {

				pdfgen.createSimplePDF(document, title, pdfgen.reportheader, recordlist, pdfgen.reportsumfield,
						ALIGN_CENTER, ALIGN_CENTER, 100);
				pdfgen.closePDF(document);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());
				output_stream.flush();
				return "itextpdfreport";
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Unable to continue the report generation");
			trace("Exception in Loop ::: " + e.getMessage());
		}

		return "changeLostStolen";
	}

	private List gettingUploadedFailedDetails(String instid, String reportno, JdbcTemplate jdbctemplate2) {
		List res = null;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append(
					"SELECT INST_ID,FILENAME,LINE_NO,REASON,(SELECT USERNAME FROM USER_DETAILS WHERE USERID IN (SELECT ADDED_BY FROM BULKFAIL_REG_STATUS WHERE REPORTRANDOMNO='"
							+ reportno + "' )) UPDATED_BY,");
			strbuild.append(
					"TO_CHAR(ADDED_DATE,'DD-MON-YYYY') UPDATED_DATE,DECODE(FAIL_REJECT,'F','FAIL','S','SUCCESS') FAIL_REJECT FROM BULKFAIL_REG_STATUS WHERE INST_ID='"
							+ instid + "' AND REPORTRANDOMNO='" + reportno + "' ");
			System.out.println("select failed status qry " + strbuild);
			res = jdbctemplate2.queryForList(strbuild.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(List uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public String deletebatch() throws Exception {
		trace("### delete record batch id Called ###");
		IfpTransObj transact = commondesc.myTranObject("deletebatch", txManager);

		HttpSession session = getRequest().getSession();
		String usercode = comUserId(session);
		String instid = comInstId(session);
		String batchId = getRequest().getParameter("BatchID");
		int batchiddelete = dao.deleterecords(instid, batchId, jdbctemplate);

		if (batchiddelete > 0) {

			transact.txManager.commit(transact.status);
			addActionMessage(batchId + " Batch ID Deleted Successfully");
			trace(batchId + " Batch ID Deleted Successfully");
		} else {
			transact.txManager.rollback(transact.status);
			addActionError("Unable to Delete batch ID...." + batchId);
			trace(" Unable to Delete batch ID...." + batchId);
		}
		return LoadCustomerDataHome();
	}

	public String ViewRegisteredCustomerData1() throws Exception {
		trace("### ViewRegisteredCustomerData Method Called ###");
		IfpTransObj transact = commondesc.myTranObject("BULKUPLOAD", txManager);

		HttpSession session = getRequest().getSession();
		String usercode = comUserId(session);
		String instid = comInstId(session);
		// String batchId = (String)session.getAttribute("BatchID");

		if (getRequest().getParameter("BatchID").equalsIgnoreCase("")
				|| getRequest().getParameter("product_code").equalsIgnoreCase("")
				|| getRequest().getParameter("subproduct").equalsIgnoreCase("")) {
			addActionError("NO Batch is Available..");
			trace(" batchid,product,subprod is null ");
		}
		String batchId = getRequest().getParameter("BatchID");

		String productcode = getRequest().getParameter("product_code");
		String subproduct = getRequest().getParameter("subproduct");

		List batchCustomer = dao.GetBatchCustomer(instid, batchId, jdbctemplate);
		Bulkcustregbean.setBulkRegCustlist(batchCustomer);

		Bulkcustregbean.setBatch_id(batchId);
		Bulkcustregbean.setBlkproduct_code(productcode);
		Bulkcustregbean.setBlksubproduct(subproduct);

		System.out.println("UploadSQLFile" + batchCustomer);
		return "DebitBulk_SQLLoadHome";
	}

	public int getsucccount(String batchId, JdbcTemplate jdbcTemplate) {
		int sccccount = 0;
		String cond = "select count(*) from CUSTOMER_BULK_LOAD where batch_id='" + batchId
				+ "' and reg_status='Success'";

		enctrace("cond" + cond);
		sccccount = jdbcTemplate.queryForInt(cond);
		return sccccount;
	}

	public String LoadCustomerData() throws Exception {
		trace("### LoadCustomerData Method Called ###");
		IfpTransObj transact = commondesc.myTranObject("BULKUPLOAD", txManager);

		HttpSession session = getRequest().getSession();
		String usercode = comUserId(session);
		String instid = comInstId(session);
		String batchId = getRequest().getParameter("BatchID");
		String productcode = getRequest().getParameter("product_code");
		String subproduct = getRequest().getParameter("subproduct");
		System.out.println("batchId" + batchId);
		System.out.println("productcode" + productcode);

		// p_bin varchar2, p_cardtype varchar2, p_sub_prod_id

		DefaultTransactionDefinition def1 = new DefaultTransactionDefinition();
		def1.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status1 = txManager.getTransaction(def1);

		String columns = "", condition = "", table1 = "CUTOMER_BULK_LOAD";
		String result = "";
		Connection conn = null;
		Dbcon dbcon = new Dbcon();
		conn = dbcon.getDBConnection();
		CallableStatement cstmt = null;

		try {
			int succ_count = -1;

			System.out.println("calling procedure:"+" instid :"+instid+" batchId :"+batchId+" productcode :"+productcode+"subproduct :"+subproduct+" usercode :"+usercode);
			trace("calling procedure:"+" instid :"+instid+" batchId :"+batchId+" productcode :"+productcode+" subproduct :"+subproduct+" usercode :"+usercode);
			// String SQL = "{call SP_CURRENTUSER_INSERT (?,?,?)}";
			conn = dbcon.getDBConnection();
			cstmt = conn.prepareCall("{call BULK_CUSTOMR_REGISTRATION(?,?,?,?,?,?)}");
			System.out.println("Called procedure --- >" + cstmt);
			enctrace("Called procedure --- >" + cstmt);
			cstmt.setString(1, instid);
			cstmt.setString(2, batchId);
			cstmt.setString(3, productcode);
			cstmt.setString(4, subproduct);
			cstmt.setString(5, usercode);
			cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
			cstmt.execute();
			result = cstmt.getString(6);
			trace("result--->" + result);

			if (result.contains("successfully")) {
				succ_count = this.getsucccount(batchId, jdbctemplate);
				transact.txManager.commit(transact.status);
				addActionMessage((succ_count) + " Records Authorized Successfully");
				trace(" Uploaded Successfully");
				
				return "DebitBulk_SQLLoadHome";
			} else {
				transact.txManager.rollback(transact.status);
				addActionError("Unable to continue...." + result);
				trace(" Unable to continue....");
				return "DebitBulk_SQLLoadHome";
			}

		} catch (Exception e) {
			e.printStackTrace();
			// txManager.rollback(transact.status);
			addActionError("Unable to Continue");
			// return "required_home";
		} finally {
			cstmt.close();
			conn.close();
		}

		// exec BULK_CUSTOMR_REGISTRATION('1', 'BATCH_17NOV_0002', '539171',
		// '101', '00000002');

		/*
		 * cstmt = conn.prepareCall("call SP BULKCUSTOMRREGISTRATION(?,?,?,?,?)"
		 * ); trace("procedure--->call BULKUCUSTOMRREGISTRATION(?,?,?,?,?)");
		 * ArrayDescriptor arrDesc =
		 * ArrayDescriptor.createDescriptor("TVARCHAR2ARRAY", conn);
		 * System.out.println("check"); // ARRAY array = new ARRAY(arrDesc,
		 * conn, order_refnum); trace("proc args-->"+instid+"--"+usercode);
		 * cstmt.setString(1, table1); cstmt.setString(2, batchId);
		 * cstmt.setString(3, instid);
		 */
		// columns = " CARD_STATUS='03',MKCK_STATUS='"+mkckflag+"', REMARKS='',
		// MAKER_ID='"+usercode+"',MAKER_DATE=(sysdate),PRE_DATE=(SYSDATE)";
		// condition = " WHERE INST_ID='"+instid+"' AND CARD_NO IN";

		/*
		 * cstmt.registerOutParameter(4,java.sql.Types.VARCHAR);
		 * cstmt.execute(); result=cstmt.getString(4);
		 */

		trace("failename:::" + this.getUploadSQLFileName());
		String todaydate = commondesc.getDate("ddMMyyyy");

		System.out.println("UploadSQLFile" + "called");
		return "DebitBulk_SQLLoadHome";
	}

	public boolean validateNumeric(String val) {
		boolean result = true;
		// System.out.println(val);
		for (int i = 1; i < val.length(); i++) {
			// System.out.println(i);
			if (!Character.isDigit(val.charAt(i))) {
				return false;
			}

		}
		return result;
	}

	public String getOrderRefNo(String instid, String padssenable, String cardno, JdbcTemplate jdbctemplate) {

		String orderrefqry = "";
		if (padssenable.equals("Y")) {
			orderrefqry = "SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE INST_ID='" + instid + "' AND HCARD_NO='"
					+ cardno + "' AND ROWNUM<=1";
		} else {
			orderrefqry = "SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE INST_ID='" + instid + "' AND CARD_NO='"
					+ cardno + "' AND ROWNUM<=1";
		}
		trace("orderrefqry__" + orderrefqry);
		String orderrfno = null;
		try {
			orderrfno = (String) jdbctemplate.queryForObject(orderrefqry, String.class);
			trace("orderrfno__" + orderrfno);
		} catch (EmptyResultDataAccessException e) {
			orderrfno = "NOREC";
		}
		return orderrfno;

	}

	public String getaccountno(String instid, String cardnumbers, JdbcTemplate jdbcTemplate) {
		String getaccountno_qury = "SELECT ACCOUNT_NO FROM CARD_PRODUCTION  WHERE HCARD_NO='" + cardnumbers
				+ "' AND INST_ID='" + instid + "'";
		enctrace("attchbranch_qury" + getaccountno_qury);
		return ((String) jdbcTemplate.queryForObject(getaccountno_qury, String.class));
	}

	//COmmented on 11-nov-2020 by gowtham
	/*
	public synchronized String issubulkcardaction() {
		trace("change card status");
		String instid = comInstId();
		String usercode = comUserId();

		String reissueorderno = "", wpsno = "", CARD_AGENT_NO = "";
		String EMPLOYEEID = "", COMPANYNAME = "", BATCHID = "", EMPMOLID = "", BIN = "", SUB_PROD_ID = "",
				PRODUCT_CODE = "", COMPANYID = "", EMPLOYEENAME = "", COMPMOLID = "";

		String cardnumbers[] = getRequest().getParameterValues("personalrefnum");

		StringBuffer hcardno = new StringBuffer();
		System.out.println("hcardno__" + hcardno);

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		System.out.println("padssenable__" + padssenable);

		String collectbranch = (String) getRequest().getParameter("collectbranch");

		String username = comUsername();
		PadssSecurity padsssec = new PadssSecurity();
		String statuscode = "07";// (String)getRequest().getParameter("statuscode");
		System.out.println("statuscode__" + statuscode);
		IfpTransObj transact = commondesc.myTranObject("BULKREISSUE", txManager);
		String changedmsg = null;
		String waitingmsg = null;
		String statusmsg = "";
		String order_ref_no = null;
		HttpSession session = getRequest().getSession();
		String orderrefno = null;

		String update_status_qry = "", mkck_status = "";
		String caf_recstatus = "A";
		String card_status = "";
		String switchcardstatus = "", ACCOUNT_NO = "", newcardno = null, productcode = "";

		try {
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			String eDMK = "", eDPK = "";
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					eDMK = ((String) map.get("DMK"));
					eDPK = ((String) map.get("DPK"));

				}
			}

			int cardcount = cardnumbers.length;
			trace("cardcount  length->:" + cardcount);

			int update_seq = 0, totalsuccesscount = 0, succcardcount = 0;
			for (int i = 0; i < cardcount; i++) {
				trace("check card number-->" + cardnumbers[i]);
				String ecardno = padsssec.getECHN(eDMK, eDPK, cardnumbers[i]);
				trace("encrypted cardno" + ecardno);
				String orgcarno = cardnumbers[i];
				hcardno = padsssec.getHashedValue(cardnumbers[i] + instid);
				// orgcarno = padsssec.getECHN(eDMK, eDPK, newcardno);
				System.out.println("cardnumbers[i]::" + cardnumbers[i]);
				String TABLENAME = "CARD_PRODUCTION";
				String accountno = this.getaccountno(instid, cardnumbers[i], jdbctemplate);

				if (padssenable.equals("Y")) {
					productcode = commondesc.getProductCodeByChn(instid, padssenable, cardnumbers[i].toString(),
							TABLENAME, jdbctemplate);
				} else {
					productcode = commondesc.getProductCodeByChn(instid, padssenable, orgcarno, TABLENAME,
							jdbctemplate);
				}
				if (productcode.equals("NOREC")) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",
							" Could not get productcode for the card " + cardnumbers[i].toString());
					return "checkbulkloadedcardsHOME";
				}

				String bin = commondesc.getBin(instid, productcode, jdbctemplate);

				String card_current_status = "";
				if (padssenable.equals("Y")) {
					card_current_status = commondesc.getCardCurrentStatus(instid, padssenable,
							cardnumbers[i].toString(), TABLENAME, jdbctemplate);
				} else {
					card_current_status = commondesc.getCardCurrentStatus(instid, padssenable, orgcarno, TABLENAME,
							jdbctemplate);
				}

				if (card_current_status == null) {
					addActionError(" NO CURRENT STATUS FOUND FOR THE CODE [" + statuscode + "]");
					return "checkbulkloadedcardsHOME";
				}

				switchcardstatus = commondesc.getSwitchCardStatus(instid, statuscode, jdbctemplate);
				if (switchcardstatus == null) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " NO STATUS FOUND FOR THE CODE [" + statuscode + "]");
					return "checkbulkloadedcardsHOME";
				}

				trace("card status changing as reissue");

				// TEMP BLOCK to REISSUE - For ALJABER Start
				// System.exit(0);

				List<Map<String, Object>> cardstatuslist = null;
				cardstatuslist = commondesc.getCardStatus(instid, cardnumbers[i].toString(), jdbctemplate);
				String cardstatus = (String) cardstatuslist.get(0).get("CARD_STATUS");

				// TEMP BLOCK to REISSUE - For Orient End

				if (padssenable.equals("Y")) {
					orderrefno = commondesc.getOrderRefNo(instid, padssenable, cardnumbers[i].toString(), TABLENAME,
							jdbctemplate);
				} else {
					orderrefno = commondesc.getOrderRefNo(instid, padssenable, orgcarno, TABLENAME, jdbctemplate);
				}

				System.out.println("order ref number ---> " + orderrefno);

				String branchattch = commondesc.checkBranchattached(instid, jdbctemplate);
				System.out.println("branch attached === >" + branchattch);
				card_status = "01";
				caf_recstatus = "S";
				if (orderrefno.equals("NOREC")) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",
							" Could not get order ref no for the card " + cardnumbers[i].toString());
					return "checkbulkloadedcardsHOME";
				}

				
				 * int persocalizeReissuecheck =
				 * cardmaindao.checkPersonalizecard(instid, cardno,
				 * jdbctemplate); if( persocalizeReissuecheck != 1 ){
				 * session.setAttribute("preverr", "E");
				 * session.setAttribute("prevmsg", " Given Card number [ "
				 * +cardno+"] is not a personalized card..."); return
				 * this.searchHome(); }
				 
				int updatestatus = -1;
				String condition = "";
				if (padssenable.equals("Y")) {
					updatestatus = this.updateCardStatusDate(instid, cardnumbers[i].toString(), "REISSUE_DATE",
							TABLENAME, jdbctemplate);
					condition = "AND HCARD_NO='" + cardnumbers[i].toString() + "'";
				} else {
					updatestatus = this.updateCardStatusDate(instid, orgcarno, "REISSUE_DATE", TABLENAME, jdbctemplate);
					condition = "AND CARD_NO='" + orgcarno + "'";
				}
				if (updatestatus < 0) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not continue the process... unable to update status");
					return "checkbulkloadedcardsHOME";
				}
				System.out.println("bin__" + bin);
				Personalizeorderdetails bindetails = commondesc.gettingBindetails(instid, bin, jdbctemplate);
				Personalizeorderdetails personlizeorderdetails = commondesc
						.gettingPersonalizeorderDetailsFromProd(instid, orderrefno, condition, jdbctemplate);
				System.out.println("instantorderdetails-------" + personlizeorderdetails);
				String breakupvalue = commondesc.getChnbreakupvalues(instid, bindetails.prodcard_expiry,
						bindetails.brcode_servicecode, personlizeorderdetails.card_type_id,
						personlizeorderdetails.sub_prod_id, personlizeorderdetails.product_code,
						personlizeorderdetails.branch_code, bindetails.apptypelen, bindetails.apptypevalue);
				System.out.println("breakupvalue__" + breakupvalue);
				String newchn = personlizeorderdetails.bin + breakupvalue;
				System.out.println("newchn Generated is ===>" + newchn);

				String prodcardtype_attach = "";
				List attachedtype = commondesc.checkattachedtype(instid, bin, jdbctemplate);
				Iterator attype = attachedtype.iterator();
				if (!attachedtype.isEmpty()) {
					while (attype.hasNext()) {
						Map map = (Map) attype.next();
						prodcardtype_attach = ((String) map.get("ATTACH_PRODTYPE_CARDTYPE"));
						branchattch = ((String) map.get("ATTACH_BRCODE"));
					}
				}
				System.out.println("newchn Generated is ===>" + newchn);
				String sequncenumber = commondesc.gettingSequnceNumberNew(instid, personlizeorderdetails.bin,
						personlizeorderdetails.branch_code, bindetails.baselen_feecode,
						personlizeorderdetails.card_type_id, personlizeorderdetails.sub_prod_id, jdbctemplate,
						branchattch, prodcardtype_attach);

				// String sequncenumber =
				// commondesc.gettingSequnceNumber(instid,personlizeorderdetails.bin,personlizeorderdetails.branch_code,bindetails.baselen_feecode,personlizeorderdetails.card_type_id
				// ,jdbctemplate, branchattch);
				long sequn_no = Long.parseLong(sequncenumber);
				String strseq = Long.toString(sequn_no);
				newcardno = commondesc.generateCHN(newchn.trim(), strseq, Integer.parseInt(bindetails.baselen_feecode),
						Integer.parseInt(bindetails.chnbaseno_limitid));
				System.out.println("newcardno --- > " + newcardno);
				if (newcardno.equals("N")) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Could not generate new card number..");
					txManager.rollback(transact.status);
					return "checkbulkloadedcardsHOME";
				}
				System.out.println("NEW CARD NO __" + newcardno);
				System.out.println("caf_recstatus__" + caf_recstatus);

				// reissue move to process

				String newenccardno = "";
				StringBuffer newhcardno = new StringBuffer();

				if (padssenable.equals("Y")) {

					newhcardno = padsssec.getHashedValue(newcardno + instid);
					newenccardno = padsssec.getECHN(eDMK, eDPK, newcardno);

				}
				String newmaskcardno = padsssec.getMakedCardno(newcardno);

				int moveprocess = -1;

				if (padssenable.equals("Y")) {
					moveprocess = moveCardToProcessForReissue(instid, padssenable, accountno, orgcarno, newenccardno,
							cardnumbers[i].toString(), newhcardno.toString(), newmaskcardno, caf_recstatus, card_status,
							usercode, collectbranch, jdbctemplate);
				} else {
					moveprocess = moveCardToProcessForReissue(instid, padssenable, accountno, orgcarno, newcardno, "",
							"", newmaskcardno, caf_recstatus, card_status, usercode, collectbranch, jdbctemplate);
				}

				int reissucnt = -1;

				if (padssenable.equals("Y")) {
					reissucnt = updateReIssueCount(instid, padssenable, cardnumbers[i].toString(), jdbctemplate);
				} else {
					reissucnt = updateReIssueCount(instid, padssenable, cardnumbers[i].toString(), jdbctemplate);
				}

				if (reissucnt != 1) {
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " COULD NOT UDPATE RE ISSUE COUNT.");
					return "checkbulkloadedcardsHOME";
				}

				trace("personlizeorderdetails.cardtypeid::::::::::" + personlizeorderdetails.card_type_id);
				String updateseq = null;

				if (moveprocess != 1) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Could not move the production records to process");
					txManager.rollback(transact.status);
					return "checkbulkloadedcardsHOME";
				}

				if (moveprocess > 0) {
					trace("inserted card number" + newcardno);
					succcardcount++;
					sequn_no++;
				}

				if (branchattch.equals("Y")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND BIN='" + personlizeorderdetails.bin + "' AND BASENO_CODE='"
							+ personlizeorderdetails.branch_code + "' ";
				} else if (prodcardtype_attach.equals("C")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND BIN='" + personlizeorderdetails.bin + "' AND BASENO_CODE='"
							+ personlizeorderdetails.card_type_id + "' ";
				} else if (prodcardtype_attach.equals("P")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND BIN='" + personlizeorderdetails.bin + "' AND BASENO_CODE='"
							+ personlizeorderdetails.sub_prod_id + "' ";
				} else {
					updateseq = "UPDATE PRODUCTBIN_REL SET CHN_BASE_NO='" + sequn_no + "' WHERE INST_ID='" + instid
							+ "' AND BIN='" + personlizeorderdetails.bin + "'";
				}

				enctrace(" updateseqreissue : " + updateseq);
				int incseq = jdbctemplate.update(updateseq);

				System.out.println("updateseq-----> " + updateseq);
				System.out.println("updateseq-----> " + update_seq);
				String readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='"
						+ instid + "' AND CARD_NO='" + newenccardno + "'";
				System.out.println("readytoprocessqry__" + readytoprocessqry);
				int ucnt = jdbctemplate.update(readytoprocessqry);
				if (ucnt != 1 && update_seq != 1 && incseq != 1) {
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "COULD NOT UPDATE GENERATED DATE TO PROCESS");
					return "checkbulkloadedcardsHOME";
				}

				int updatecardinfo = -1, updatestatus1 = -1;
				updatecardinfo = this.updateezcardinfo(instid, cardnumbers[i].toString(), switchcardstatus,
						jdbctemplate);

				trace("product code checking -->" + productcode);
				if ("5391711101".equalsIgnoreCase(productcode)) {
					trace("INSIDE product code checking -->" + productcode);
					long millis = System.currentTimeMillis();
					java.sql.Date date = new java.sql.Date(millis);
					System.out.println("TTTTTTTTT--->" + date);
					trace("TTTTTTTTT--->" + date);

					String query = "select a.EXPIRY_DATE, a.org_chn,a.emb_name,NVL(a.mobileno,0) as mobileno,NVL(b.e_mail,0) as e_mail from card_production a, customerinfo b where a.INST_ID='"
							+ instid + "' AND a.HCARD_NO = '" + hcardno + "'and a.cin=b.CIN";
					List card_data = jdbctemplate.queryForList(query);

					String mobileNo = "", embname = "", clearCard = "", emailId = "", EXPDATE = "";

					if (card_data.isEmpty()) {
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "No data Available");
						return "serach_home";
					}

					Iterator orderno = card_data.iterator();
					if (!card_data.isEmpty()) {
						while (orderno.hasNext()) {
							Map map = (Map) orderno.next();
							mobileNo = ((String) map.get("MOBILENO"));
							clearCard = (String) map.get("ORG_CHN");
							embname = ((String) map.get("EMB_NAME"));
							emailId = ((String) map.get("E_MAIL"));
							EXPDATE = ((String) map.get("EXPIRY_DATE"));
						}
					}

					// String customerDataReportQuery="INSERT INTO CARD_REPORT
					// (INST_ID,ACTION_CODE,CHN,EMB_NAME,MOBILE,DOWN_CNT,EMAIL,USER_ID,ACTION_DATE,
					// EXPDATE) "
					String customerDataReportQuery = "INSERT INTO CARD_REPORT (INST_ID,ACTION_CODE,CHN,EMB_NAME,MOBILE,DOWN_CNT,EMAIL,USER_ID,ACTION_DATE) "
							+ " values(?,?,?,?,?,?,?,?)";
					int result = jdbctemplate.update(customerDataReportQuery,
							new Object[] { instid, "03", clearCard, embname, mobileNo, "0", emailId, usercode, date });
					trace("result ::: " + result);
				}

				// updating cardproduction
				if (padssenable.equals("Y")) {
					updatestatus1 = this.updateCardStatus(instid, padssenable, cardnumbers[i].toString(), statuscode,
							switchcardstatus, caf_recstatus, usercode, TABLENAME, collectbranch, jdbctemplate);
				} else {
					updatestatus = this.updateCardStatus(instid, padssenable, orgcarno, statuscode, switchcardstatus,
							caf_recstatus, usercode, TABLENAME, collectbranch, jdbctemplate);
				}

				int inserthist = this.insertMainintainHistory(instid, orgcarno, card_current_status, statuscode,
						usercode, jdbctemplate);
				if (inserthist < 0) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not continue the process... Could not insert history..");
					return "checkbulkloadedcardsHOME";
				}

				*//************* AUDIT BLOCK **************//*
				try {
					auditbean.setActmsg(" New Card Generated [ " + newcardno + " ] For Lost/Stolen Card [ "
							+ personlizeorderdetails.mcardno + " ] ");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("4104");
					auditbean.setCardno(personlizeorderdetails.mcardno);
					// auditbean.setCardnumber(enccardno);
					// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}
				*//************* AUDIT BLOCK **************//*

				System.out.println("updatecardinfo" + updatecardinfo + "updatestatus1" + updatestatus1);

				if (updatecardinfo > 0 && updatestatus1 > 0) {
					totalsuccesscount = totalsuccesscount + 1;

				} else {
					break;
				}
			}

			if (succcardcount == totalsuccesscount) {

				txManager.commit(transact.status);
				trace("cardcount" + cardcount + "totalsuccesscount" + totalsuccesscount);
				trace("Transaction commited successfully...");
				changedmsg = "RE-ISSUED";
				waitingmsg = "Card Is Waiting For Card Generation Authorization...";
				addActionMessage("New Card Generated for [ " + totalsuccesscount + " ] Cards From  [ " + cardcount
						+ " ] ." + waitingmsg);

			} else {
				// commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				System.out.println("Txn Got Rollbacked ---->");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Card Status not updated ");
			}
		} catch (Exception e) {
			// commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			System.out.println("Error While Execute the Query ---->" + e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error While Update The Card Status: Error " + e.getMessage());
		}
		setAct((String) session.getAttribute("ACTIONTYPE"));

		return "checkbulkloadedcardsHOME";

	}*/

	public int updateCardStatusDate(String instid, String cardno, String dbcolumnfld, String TABLENAME,
			JdbcTemplate jdbctemplate) {
		int x = -1;
		String qry = "UPDATE " + TABLENAME + " SET " + dbcolumnfld + "=SYSDATE WHERE INST_ID='" + instid
				+ "' AND CARD_NO='" + cardno + "'";
		enctrace("updateCardStatusDate::" + qry);
		x = jdbctemplate.update(qry);
		return x;
	}

	
	//Commented on 11-nov-2020 by gotham
/*	public int moveCardToProcessForReissue(String instid, String padssenable, String accountno, String cardno,
			String newcardno, String hashcardno, String newhashcardno, String newmaskcardno, String caf_recstatus,
			String card_status, String usercode, String collectbranch, JdbcTemplate jdbctemplate) throws Exception {
		String cond = "";
		String keyid = "", reissueorderno = "", wpsno = "", CARD_AGENT_NO = "";
		String EDMK = "", EDPK = "", clearchn = "";
		PadssSecurity padsssec = new PadssSecurity();
		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String eDMK = ((String) map.get("DMK"));
					String eDPK = ((String) map.get("DPK"));
					clearchn = padsssec.getCHN(eDMK, eDPK, newcardno);
				}
			}

			cond = "AND HCARD_NO='" + hashcardno + "'";
		} else {
			cond = "AND CARD_NO='" + cardno + "'";
		}
		// String
		// accountnumber=commondesc.getaccountnumberforreissue(instid,clearchn,jdbctemplate);

		String expperiod = commondesc.getSubProductExpPeriodwithCardno(instid, cond, jdbctemplate);

		String expirydatecond = "add_months(sysdate," + expperiod + ")";

		String query = "";

		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,CARDISSUETYPE,MOBILENO,PANSEQ_NO,CARD_COLLECT_BRANCH)";

		query += " SELECT INST_ID, '" + newcardno + "','" + newhashcardno + "','" + newmaskcardno + "','" + accountno
				+ "',ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, '" + card_status
				+ "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "SYSDATE, " + expirydatecond + ", PRE_DATE, '" + usercode
				+ "', MAKER_DATE, CHECKER_ID, CHECKER_DATE, 'M', SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '" + caf_recstatus
				+ "', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, '" + clearchn
				+ "', USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,'P',MOBILENO,'01',CARD_COLLECT_BRANCH FROM CARD_PRODUCTION WHERE INST_ID='"
				+ instid + "' ";

		// decode(REISSUE_CNT,'0',REISSUE_CNT+2,REISSUE_CNT+1
		if (padssenable.equals("Y")) {
			query += "AND HCARD_NO='" + hashcardno + "'";
		} else {
			query += "AND CARD_NO='" + cardno + "'";
		}
		enctrace("moveCardToProcessBUlkReiisuance : " + query);

		int x = jdbctemplate.update(query);
		return x;
	}*/

	public int updateezcardinfo(String instid, String cardno, String cardstatus, JdbcTemplate jdbctemplate) {
		int x = -1;
		String qry = "UPDATE EZCARDINFO SET STATUS='" + cardstatus + "' WHERE INSTID='" + instid + "' AND CHN='"
				+ cardno + "'";
		enctrace("updateezcardinfo::" + qry);
		x = jdbctemplate.update(qry);
		return x;
	}

	public int updateCardStatus(String instid, String padssenable, String cardno, String cardstatus,
			String switchcardstatus, String cafrecstatus, String usercode, String TABLENAME, String collectbranch,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String update_status_qry = "";
		if (padssenable.equals("Y")) {
			update_status_qry = "UPDATE " + TABLENAME + " SET  CARD_STATUS='" + cardstatus + "', STATUS_CODE='"
					+ switchcardstatus + "', CAF_REC_STATUS='" + cafrecstatus + "', MAKER_ID='" + usercode
					+ "', MAKER_DATE=sysdate  WHERE INST_ID='" + instid + "' AND HCARD_NO='" + cardno + "'";
		} else {
			update_status_qry = "UPDATE " + TABLENAME + " SET  CARD_STATUS='" + cardstatus + "', STATUS_CODE='"
					+ switchcardstatus + "', CAF_REC_STATUS='" + cafrecstatus + "', MAKER_ID='" + usercode
					+ "', MAKER_DATE=sysdate  WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno + "'";
		}
		enctrace("update_status_qry :" + update_status_qry);
		x = jdbctemplate.update(update_status_qry);
		return x;
	}

	public int insertMainintainHistory(String instid, String cardno, String prevstatus, String currentstatus,
			String usercode, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		CommonDesc commondesc = new CommonDesc();
		int binlen = commondesc.getBinLen(instid, cardno, jdbctemplate);

		String maintain_history_qry = "INSERT INTO MAINTAIN_HISTORY(INST_ID, BIN, CARD_NO, PREV_STATUS, CHANGED_STATUS, ACTION_DATE, CHANGED_BY)";
		maintain_history_qry += "VALUES ('" + instid + "', '" + cardno.substring(0, binlen) + "', '" + cardno + "', '"
				+ prevstatus + "','" + currentstatus + "', SYSDATE, '" + usercode + "')";
		enctrace("maintain_history_qry :" + maintain_history_qry);
		x = jdbctemplate.update(maintain_history_qry);
		return x;
	}

	public int updateReIssueCount(String instid, String padssenable, String chn, JdbcTemplate jdbctemplate) {

		String padsscon = "";
		if (padssenable.equals("Y")) {
			padsscon = "HCARD_NO";
		} else {
			padsscon = "CARD_NO";
		}
		String fchupdcntqry = "SELECT REISSUE_CNT FROM CARD_PRODUCTION WHERE INST_ID='" + instid + "' AND " + padsscon
				+ "='" + chn + "'";
		enctrace("fchupdcntqry::" + fchupdcntqry);
		int updcnt = 0;
		try {
			updcnt = jdbctemplate.queryForInt(fchupdcntqry);
		} catch (EmptyResultDataAccessException e) {
			updcnt = 0;
		}

		int newcnt = updcnt + 1;
		String cntqry = "UPDATE CARD_PRODUCTION SET REISSUE_CNT= '" + newcnt + "', REISSUE_DATE=sysdate WHERE INST_ID='"
				+ instid + "' AND " + padsscon + "='" + chn + "' ";
		enctrace("REISSUE UPDATE QRY __" + cntqry);
		int x = commondesc.executeTransaction(cntqry, jdbctemplate);
		return x;

	}

	public String getRenewalPeriods(String instid, JdbcTemplate jdbcTemplate) {
		String renewalqry = "SELECT RENEWAL_PERIODS FROM INSTITUTION WHERE INST_ID='" + instid + "'";
		enctrace("renewalqry::" + renewalqry);
		return ((String) jdbcTemplate.queryForObject(renewalqry, String.class));
	}

	public int movebulkrenuallistinsert(String instid, String caf_recstatus, String card_status, String expperiod,
			String changeexpreq, String TABLENAME, String hcardno, String mobileNumber, String crdcltBranch,
			JdbcTemplate jdbctemplate) {

		// String padsscon = "";
		String expreqcond = "";

		if (changeexpreq.equals("Y")) {
			expreqcond = "add_months(sysdate," + expperiod + ")";
		} else {
			expreqcond = "EXPIRY_DATE";
		}

		String query = "";

		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH)";

		query += " SELECT INST_ID,CARD_NO,HCARD_NO, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, '"
				+ card_status + "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, '" + crdcltBranch
				+ "', PC_FLAG, CARD_CCY,";
		query += "SYSDATE, " + expreqcond
				+ ", PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '" + caf_recstatus
				+ "', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,'" + mobileNumber + "','" + crdcltBranch + "' FROM " + TABLENAME
				+ "  WHERE INST_ID='" + instid + "' AND HCARD_NO='" + hcardno + "' ";

		enctrace("moveCardToProcess : " + query);

		int x = jdbctemplate.update(query);
		return x;
	}

	public String filterUnwantedSpaces(String values){
		String 	res = values.replaceAll("[^a-zA-Z0-9_\\-]", " ");
		return res.trim();
	}
	

}
