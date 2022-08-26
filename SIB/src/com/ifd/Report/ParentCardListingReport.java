package com.ifd.Report;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class ParentCardListingReport extends BaseAction {
	private JdbcTemplate jdbctemplate = new JdbcTemplate();

	public List getMasterReportList() {
		return masterReportList;
	}

	private String transctionreport;
	private ByteArrayOutputStream output_stream;
	private ByteArrayInputStream input_stream;

	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}

	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}

	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}

	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}

	public void setMasterReportList(List masterReportList) {
		this.masterReportList = masterReportList;
	}

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	MasterReportGenerationDAO dao = new MasterReportGenerationDAO();
	MasterReportGenerationBean cardbean = new MasterReportGenerationBean();
	private List masterReportList;
	private static String webURL;
	private FileInputStream file_inputstream;
	private String report_name;

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public FileInputStream getFile_inputstream() {
		return file_inputstream;
	}

	public void setFile_inputstream(FileInputStream file_inputstream) {
		this.file_inputstream = file_inputstream;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public MasterReportGenerationBean getCardbean() {
		return cardbean;
	}

	CommonDesc commondesc = new CommonDesc();

	public void setCardbean(MasterReportGenerationBean cardbean) {
		this.cardbean = cardbean;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	public String  parentCardReportHome() throws Exception {
		System.out.println("parentCardReportHome method called ......");
		HttpSession session = getRequest().getSession();
		List cardreportlist = dao.getActionList(jdbctemplate);
		cardbean.setCardreportlist(cardreportlist);
		String instid = (String) session.getAttribute("Instname");
		List branchList = commondesc.generateBranchList(instid, jdbctemplate);
		cardbean.setBranchList(branchList);
		List productList = commondesc.getProductListView(instid, jdbctemplate,
				session);
		cardbean.setProductList(productList);
		return "parentCardReportHome";
	}

	public void getsubActionList() throws IOException {
		String headcode = getRequest().getParameter("actionhead");
		String action[] = getRequest().getParameterValues("actionhead");
		for (int i = 0; i < action.length; i++) {
			System.out.println(action[i].toString());
		}

		List subactlist = dao.getAuditActionList(headcode, "PRODUCTION",
				jdbctemplate);
		if (subactlist.isEmpty()) {
			return;
		}
		Iterator itr = subactlist.iterator();
		String opt = "";
		while (itr.hasNext()) {
			Map mp = (Map) itr.next();
			opt += "<option value='" + (String) mp.get("ACTION") + "'> "
					+ (String) mp.get("ACTION_DESC") + " </option>";
		}
		getResponse().getWriter().write(opt.toString());
	}

	public String generateCardReport() throws Exception {
		trace("getCardReport method called .......");
		String reporttype = getRequest().getParameter("REPORTTYPE");
		String branchlist = getRequest().getParameter("branchlist");
//		String subaction[] = getRequest().getParameterValues("subaction");
		String productCode = getRequest().getParameter("productCode");
		String instid = comInstId();
		String cond = "";
		String status = "";
		
	   String condition = this.getReportCondition(cond);

		trace("condition::::" + condition + " status:::::: " + status);

		if (!branchlist.equals("ALL")) {
			condition = condition + " AND CARD_COLLECT_BRANCH='" + branchlist + "'";
			trace("brach List ::::" + branchlist);
		}

		if (!productCode.equals("ALL")) {
			condition = condition + " AND PRODUCT_CODE='" + productCode + "'";
			trace("Product Code ::::" + productCode);
		}

		if (reporttype.equals("EXCEL")) {
			String Query = condition;
			enctrace("Query WITH EXCEL Condition" + Query);
			PadssSecurity sec = new PadssSecurity();
			CommonDesc commondesc = new CommonDesc();
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			// System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::"+secList);
			Iterator secitr = secList.iterator();
			String dcardno = "", eDMK = "", eDPK = "";
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					eDMK = ((String) map.get("DMK"));
					eDPK = ((String) map.get("DPK"));

				}
			}
			List cardList = dao.getMasterReportList(instid, eDMK, eDPK, sec,
					Query, jdbctemplate);

			return ExcelReportGenMaster(cardList, status);
		} else {
			return reportGenMaster(cond, branchlist,status,productCode);
		}
	}

	/*
	 * public String generateCardReport() throws IOException {
	 * System.out.println("getCardReport method called ......."); String
	 * subaction[] = getRequest().getParameterValues("subaction"); String cond =
	 * ""; for ( int i=0; i<subaction.length;i++){
	 * System.out.println(subaction[i].toString());
	 * if(subaction[i].toString().equals("$CARDGENAUTH")) { cond = cond +
	 * " OR (CARD_STATUS='01' and mkck_status='M')"; }
	 * if(subaction[i].toString().equals("$SECREG")) { cond = cond +
	 * " OR (CARD_STATUS='01' and mkck_status='P')"; }
	 * 
	 * if(subaction[i].toString().equals("$SECAUTH")){ cond = cond +
	 * " OR (CARD_STATUS='02' and mkck_status='M') ";}
	 * 
	 * if(subaction[i].toString().equals("$PREREG")){ cond = cond +
	 * " OR (CARD_STATUS='02' and mkck_status='P') "; }
	 * if(subaction[i].toString().equals("$PREAUTH")){ cond = cond +
	 * " OR (CARD_STATUS='03' and mkck_status='M') "; }
	 * if(subaction[i].toString().equals("$RECIEVEREG")){ cond = cond +
	 * " OR (CARD_STATUS='03'  and mkck_status='P') "; }
	 * if(subaction[i].toString().equals("$RECIEVEAUTH")){ cond = cond +
	 * " OR (CARD_STATUS='04'  and mkck_status='M')"; }
	 * if(subaction[i].toString().equals("$ISSUEREG")){ cond = cond +
	 * " OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') "
	 * ; } if(subaction[i].toString().equals("$ISSUEAUTH")){ cond = cond +
	 * " OR (CARD_STATUS='05'  and mkck_status='M') "; } }
	 * 
	 * return reportGenMaster(cond); }
	 */

	public String getReportCondition(String CONDITION) {
		String cond = "", result = "";
		
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String datevalue = " AND trunc(ISSUE_DATE) BETWEEN to_date('"
				+ fromdate + "','DD-MM-YY') and to_date('" + todate
				+ "','DD-MM-YY')";
		trace("getReportCondition called ...." + CONDITION);
		
		//instid="UTBSL";
		instid="SIB";
		result = "SELECT  (select branch_name from branch_master  where branch_code=card_collect_branch) BRANCH_NAME,ORDER_REF_NO ,MCARD_NO, CIN ,EMB_NAME ,ACCOUNT_NO ACCT_NO,"
				+ "(select username from USER_DETAILS where userid=MAKER_ID) maker_id,"
				+ "DECODE(PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG,"
				+ "to_char(ISSUE_DATE,'DD-MM-YY') ISSUE_DATE,DECODE(ADDON_FLAG,'A','ADDON CARD') CARDSTATUS "
				+ "   FROM CARD_PRODUCTION p WHERE INST_ID='"
				+ instid + "' AND ADDON_FLAG='A'  " + datevalue;
		
		enctrace("Condition Query :::" + result);
		//System.out.println("result------>" + result);
		trace("getReportCondition endes ....");
		return result;
	}

	public String cardreportpage() {
		trace("cardreportpage method called ........");
		HttpSession session = getRequest().getSession();
		String instid = getRequest().getParameter("INSTID");
		String fromdate = getRequest().getParameter("fromdate");
		trace("from date getting" + fromdate);
		String todate = getRequest().getParameter("todate");
		trace("to date getting" + todate);
		System.out.println("checking instid" + instid);
		String CONDITION = getRequest().getParameter("CONDITION");
		trace("asdf:::CONDITION::::::" + CONDITION);
		String branchcode = getRequest().getParameter("BRANCHCODE");
		
		
		String productCode = getRequest().getParameter("PRODUCTCODE");
		
	//	String branchlist = getRequest().getParameter("branchlist");
		trace("branchcode:::" + branchcode);
		String cond = this.getReportCondition(CONDITION);
		String reportheader = "Parent Card List Report";
		String reporttitle = "Debit Cardman";
		enctrace("Got Condition Query ::::" + cond);

		String Query = cond;

		if (!branchcode.equals("ALL")) {
			Query = Query + " AND CARD_COLLECT_BRANCH='" + branchcode + "'";
			trace("brach List ::::" + branchcode);
		}

		if (!productCode.equals("ALL")) {
			Query = Query + " AND PRODUCT_CODE='" + productCode + "'";
			trace("Product Code ::::" + productCode);
		}


		trace("Query WITH Condition" + Query);
		try {
			PadssSecurity sec = new PadssSecurity();
			CommonDesc commondesc = new CommonDesc();
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			// System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::"+secList);
			Iterator secitr = secList.iterator();
			String dcardno = "", eDMK = "", eDPK = "";
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					eDMK = ((String) map.get("DMK"));
					eDPK = ((String) map.get("DPK"));

				}
			}
			List cardList = dao.getMasterReportList(instid, eDMK, eDPK, sec,
					Query, jdbctemplate);
			System.out.println("cardlist:::::" + cardList);
			setMasterReportList(cardList);
		} catch (Exception e) {
			trace("Exception getting card list " + e);
		}
		session.setAttribute("reportheader", reportheader);
		session.setAttribute("reporttitle", reporttitle);
		session.setAttribute("fromdate", fromdate);
		session.setAttribute("todate", todate);
		if ("K".equalsIgnoreCase(CONDITION)) {
			setCondition(CONDITION);
		}
		System.err.println("cardreportpage method endes ........");
		return "cardreportpage";
	}

	public String reportGenMaster(String condition, String branchcode,String status,String productCode )
			throws IOException {
		trace("reportGenMaster started ...");
		String fromdate = getRequest().getParameter("fromdate");
		trace("from date getting" + fromdate);

		String todate = getRequest().getParameter("todate");
		trace("to date getting" + todate);
		trace("conditioncondition:" + condition + "-brachcode::" + branchcode);
		String qwer = "CARD_STATUS=03";
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String userid = comUserId(session);
		String reportno = "4"; // getRequest().getParameter("RNO");
		String filename = "", passwdReq = "", pagenoreq = "", footercontent = "";
		

			filename = "ParentCard _List_Report";

		
		String URLLINK = "cardreportpageParentCardListingReport.do?INSTID="
				+ instid + "&CONDITION=" + condition + "&BRANCHCODE="+ branchcode +"&PRODUCTCODE="+productCode  +"&fromdate=" + fromdate + "&todate=" + todate; // getRequest().getParameter("URL").trim();;
		trace("OLDURL:" + URLLINK);
		Properties prop = getCommonDescProperty();
		String serverPath = prop.getProperty("serverpath.location");
		trace("serverPath" + serverPath);
		String replaceUrl = serverPath + URLLINK.replaceAll("\\$", "&");
		trace("URLLINK::::" + replaceUrl);
		String reportCondition = getRequest().getParameter("CONDITION");
		List repconfig = dao.getReportConfigDetails(instid, reportno,
				jdbctemplate);
		Iterator iterator = repconfig.iterator();

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();
		//	filename = (String) map.get("REPORTNAME");
			passwdReq = (String) map.get("PASSWORD_REQ");
			pagenoreq = (String) map.get("PAGENO_REQ");
			footercontent = (String) map.get("FOOTERCONTENT");
		}
		java.util.Date dt = null;
		SimpleDateFormat sdt = null;

		String curdatetime = new SimpleDateFormat("dd-MM-yyyy")
				.format(new Date());
		filename = filename + "_" + curdatetime + ".pdf";
		trace("filename ::::  "+filename);
		String res = this.generatePDFFile(replaceUrl, filename, passwdReq,
				pagenoreq, footercontent, username);
		trace("reportGenMaster():::reportGenMaster" + res);

		file_inputstream = new FileInputStream(new File(res));
		setReport_name(filename);

		return "getPDFReport";

	}

	public String ExcelReportGenMaster(List excelReportList, String status)
			throws IOException {
		trace("reportGenMaster started ...");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String reportno = "4";
		String filename = "ParentCard _List_Report"; 
		String passwdReq = "";
		System.out.println("reportno::" + reportno);

		

		List result = excelReportList;

		String excelparam = this.getExcelParam(instid, reportno, jdbctemplate);

		List combinedlist = new ArrayList();
		combinedlist.add(result);
		combinedlist.add(result);
		String keyDesc = "successful - unsuccessful";
		String res = this.getExcelReport(combinedlist, excelparam, filename);
		System.out.println("reportGenMaster():::reportGenMaster" + res);
		return res;
	}

	private List getExcelReport(String instid, String reportno,
			JdbcTemplate jdbctemplate) {

		String getrep = null;
		String getrepqry = null;
		try {
			getrepqry = "select REPORTQUERY from PDFREPORTGENRATOR where RNO='"
					+ reportno + "' ";
			enctrace("getrepqry : " + getrepqry);
			getrepqry = (String) jdbctemplate.queryForObject(getrepqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
			getrep = null;
		}

		List repodet = null;
		enctrace("reportquery---> " + getrepqry);
		repodet = jdbctemplate.queryForList(getrepqry);
		return repodet;
	}

	private String getExcelParam(String instid, String reportno,
			JdbcTemplate jdbctemplate2) {
		String xlparam = null;
		try {
			String xlparamqry = "select EXCEL_PARAM from BRANCHWISEREPORT where RNO='"
					+ reportno + "' ";
			enctrace("xlparamqry : " + xlparamqry);
			xlparam = (String) jdbctemplate.queryForObject(xlparamqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
			xlparam = null;
		}
		return xlparam;
	}

	private List getReportConfigDetails(String instid, String reportno,
			JdbcTemplate jdbctemplate) {
		List getrep = null;
		try {
			String getrepqry = "select REPORTNAME,PASSWORD_REQ,PAGENO_REQ,FOOTERCONTENT from PDFREPORTGENRATOR where RNO='"
					+ reportno + "' ";
			enctrace("getrepqry : " + getrepqry);
			getrep = jdbctemplate.queryForList(getrepqry);
		} catch (EmptyResultDataAccessException e) {
			getrep = null;
		}
		return getrep;
	}

	public String getExcelReport(List listqry, String keyDesc, String namestr) {
		trace("	 ***************** Transaction Details report ****************");
		enctrace("**************** Transaction Details report ****************");

		trace("listqry:::::" + listqry);
		trace(":::::keyDesc" + keyDesc);
		trace(":::::namestr" + namestr);

		HttpSession session = getRequest().getSession();
		try {
			output_stream = new ByteArrayOutputStream();
			String curdatetime = new SimpleDateFormat("dd-MM-yyyy")
					.format(new Date());
			trace("dateFormat.format(date)    --->  " + curdatetime);
			String defaultname = namestr + curdatetime + ".xls";
			trace("default name" + defaultname);
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			Sheet sheet = null;
			trace(" keyDesc " + keyDesc);
			String keydesc_split[] = keyDesc.split("-");
			System.out.println("keydesc_split[] ==== " + keydesc_split[0]);
			Iterator combined_itr = listqry.iterator();
			int i = 0;
			while (combined_itr.hasNext()) {
				List lst = (List) combined_itr.next();
				trace(" ----- lst  ---- " + lst.size());
				sheet = workbook.createSheet(keydesc_split[i]);
				int rownum = 0;
				int cellnum = 0;
				Iterator itr1 = lst.iterator();
				int rowno = 0;
				Row rowheading = null;
				while (itr1.hasNext()) {
					trace("rowno = ******** = " + rowno);
					Map map = (Map) itr1.next();
					Iterator keyItr = map.keySet().iterator();
					if (rowno == 0) {
						rowheading = sheet.createRow((short) rowno++);
						trace("*******rowheading *****");
					}
					Row row = sheet.createRow((short) rowno++);
					String key = null;
					int cellno = 0;
					while (keyItr.hasNext()) {
						trace("rowno inside second while loop = ******** = "
								+ rowno);
						Cell cell = null;
						key = (String) keyItr.next();
						if (rowno == 2) {
							cell = rowheading.createCell((short) cellno);
							cell.setCellValue(key);
							trace("Key .........." + key);
						}
						cell = row.createCell((short) cellno);
						cell.setCellValue((String) map.get(key));
						trace(" value .... " + (String) map.get(key));
						cellno++;
					}
					trace("rowno outside while loop " + row.getRowNum());
					if (row.getRowNum() > 49998) {
						trace("entered rownum ");
						sheet = workbook.createSheet(keydesc_split[i]);
						rowno = 0;
						continue;
					}
				}
				i++;
				trace("	value of i at the bottom --	" + i + "	rownum -- "
						+ rowno);
			}
			workbook.write(output_stream);
			String fromdate = getRequest().getParameter("fromdate");
			trace("from date getting" + fromdate);
			String todate = getRequest().getParameter("todate");
			trace("todate  getting" + todate);
			setTransctionreport(defaultname);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			output_stream.flush();
			trace("Close file");
			return "transactionexcel";
		} catch (Exception e) {
			trace("ERROR: ->" + e.getMessage());
			e.printStackTrace();
			addActionError("Could not generate report");
		}
		return "globalreporterror";
	}

	public String generatePDFFile(String link_name_with_id, String filename,
			String passwdReq, String pagenoreq, String footercontent,
			String username) throws MalformedURLException {

		trace(link_name_with_id + "nameeeee:0" + filename);
		FileOutputStream output_stream = null;
		File dst = null;
		URL url = null;
		String line = "";
		String FileName = "";
		String reportlocation = "";

		try {

			FileName = filename;

			PD4ML pd4ml = new PD4ML();
			pd4ml.adjustHtmlWidth();
			pd4ml.setHtmlWidth(950);
			// pd4ml.addStyle("report.css",true);
			// pd4ml.resetAddedStyles();
			// pd4ml.fitPageVertically();
			//pd4ml.generateOutlines(true);
			pd4ml.enableTableBreaks(false);

			/* setting pdf page footer */
			PD4PageMark pgmark = new PD4PageMark();
			pgmark.setInitialPageNumber(1);
			pgmark.setPageNumberAlignment(PD4PageMark.RIGHT_ALIGN);

			if (pagenoreq.equals("Y")) {
				pgmark.setPageNumberTemplate("${page} - ${total}");
			}
			if (!footercontent.equals("N")) {
				pgmark.setTitleTemplate(footercontent);
			}
			pgmark.setTitleAlignment(PD4PageMark.CENTER_ALIGN);
			if (passwdReq.equals("Y")) {
				pd4ml.setPermissions(username, 0xffffffff, true);
			}
			pgmark.setAreaHeight(100);

			pd4ml.setPageFooter(pgmark);
			pd4ml.adjustHtmlWidth();
			// pd4ml.addStyle(url, true);

			// http://127.0.0.1:8080/BBMISTEST
			Properties prop = commondesc.getCommonDescProperty();
			reportlocation = prop.getProperty("REPORTLOCATION");

			// setting reportname
			dst = new File(reportlocation + "/" + FileName);
			output_stream = new FileOutputStream(dst);

			webURL = link_name_with_id;
			System.out.println("WEBURL============>" + webURL);
			trace("WEBURL============>" + webURL);
			// String urlStr=webURL+"/PDFSource.jsp?jspName="+ling_name_with_id;
			// String urlStr=link_name_with_id;
			System.out.println("URLSTRING:::" + webURL);
			String urlStr = webURL;
			try {
				url = new URL(urlStr);
				java.net.URLConnection urlCon = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlCon.getInputStream()));
				InputStream fileCon = null;
				synchronized (fileCon = urlCon.getInputStream()) {
					br = new BufferedReader(new InputStreamReader(fileCon));
					while ((line = br.readLine()) != null) {
						line = line.trim();

					}

				}
			} catch (Exception e) {
				System.out.println("error in connnection");

			}
			try {
				synchronized (pd4ml) {
					System.out.println("FILNALURL" + url);
					pd4ml.render(url, output_stream);
					// input_stream = new ByteArrayInputStream(output_stream);

					output_stream.flush();
					trace("Close file");
					output_stream.close();
				}

			} catch (Exception e) {
				throw e;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return reportlocation + "/" + FileName;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public String getTransctionreport() {
		return transctionreport;
	}

	public void setTransctionreport(String transctionreport) {
		this.transctionreport = transctionreport;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	private String condition;

}
