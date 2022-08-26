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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.ifd.beans.ActiveCardDTO;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class BranchDispatchReport extends BaseAction {
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

	public String branchdispatchreporthome() throws Exception {
		System.out.println("cardproductionreporthome method called ......");
		HttpSession session = getRequest().getSession();
		List cardreportlist = dao.getActionList(jdbctemplate);
		cardbean.setCardreportlist(cardreportlist);
		String instid = (String) session.getAttribute("Instname");
		List branchList = commondesc.generateBranchList(instid, jdbctemplate);
		cardbean.setBranchList(branchList);
		List productList = commondesc.getProductListView(instid, jdbctemplate, session);
		cardbean.setProductList(productList);
		return "branchdispatchreporthome";
	}

	public void getsubActionList() throws IOException {
		String headcode = getRequest().getParameter("actionhead");
		String action[] = getRequest().getParameterValues("actionhead");
		for (int i = 0; i < action.length; i++) {
			System.out.println(action[i].toString());
		}

		List subactlist = dao.getAuditActionList(headcode, "PRODUCTION", jdbctemplate);
		if (subactlist.isEmpty()) {
			return;
		}
		Iterator itr = subactlist.iterator();
		String opt = "";
		while (itr.hasNext()) {
			Map mp = (Map) itr.next();
			opt += "<option value='" + (String) mp.get("ACTION") + "'> " + (String) mp.get("ACTION_DESC")
					+ " </option>";
		}
		getResponse().getWriter().write(opt.toString());
	}

	public String generateCardReport() throws Exception {
		trace("getCardReport method called .......");
		String reporttype = getRequest().getParameter("REPORTTYPE");
		String branchlist = getRequest().getParameter("branchlist");
		// String subaction[] = getRequest().getParameterValues("subaction");
		String productCode = getRequest().getParameter("productCode");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		
		String instid = comInstId();
		String cond = "";
		String status = "";
		String EDPK = "", CDPK = "", CDMK = "", clearCardNumber = "", encCardNumber = "";
		Properties props = null;

	//	String condition = this.getReportCondition(cond);
		
		String branch_Dispatch_query="select distinct MCARD_NO AS MASKED_CARDNUMBER, EMB_NAME as EMBOSSING_NAME," 
        +"(select branch_name from branch_master where branch_code=card_collect_branch) BRANCH_NAME FROM PERS_CARD_PROCESS " 
        +"WHERE INST_ID='SIB' AND CARD_STATUS='03' AND MKCK_STATUS='P' AND trunc(PRE_DATE) BETWEEN to_date('"+ fromdate + "','DD-MM-YY') and to_date('" + todate+ "','DD-MM-YY') ";
		

		trace("condition::::" + branch_Dispatch_query + " status:::::: " + status);

		if (!branchlist.equals("ALL")) {
			branch_Dispatch_query = branch_Dispatch_query + " AND CARD_COLLECT_BRANCH='" + branchlist + "'";
			trace("brach List ::::" + branchlist);
		}

		if (!productCode.equals("ALL")) {
			branch_Dispatch_query = branch_Dispatch_query + " AND PRODUCT_CODE='" + productCode + "'";
			trace("Product Code ::::" + productCode);
		}

		
		
		
		if (reporttype.equals("EXCEL")) {
			String Query = branch_Dispatch_query;
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
			trace("branch_dispatch_report_query_result: "+cardList);
			if(!cardList.isEmpty()){
			return ExcelReportGenMaster(cardList, status);}
		else{
			
			addActionError("No Records to generate Branch Dispatch Reports");
		
		return "globalreporterror";
		}}
		else {
			return reportGenMaster(branch_Dispatch_query, branchlist, status, productCode);
		}
	
			
		
		 }
			/*List<ActiveCardDTO> cardList=dao.processReportQuery(instid, Query, jdbctemplate);
					trace("cardList @@@ " + cardList);
			return ExcelReportGenMaster(cardList, status);
		} else {
			return reportGenMaster(cond, branchlist, status, productCode);
		}
*/	

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
		String datevalue = " AND trunc(a.PRE_DATE) BETWEEN to_date('" + fromdate + "','DD-MM-YY') and to_date('"
				+ todate + "','DD-MM-YY')";
		trace("getReportCondition called ...." + CONDITION);

		instid = "SIB";
		
		
		/*//result="select distinct (select branch_name from branch_master  where branch_code=a.card_collect_branch) BRANCH_NAME,a.order_ref_no, substr(b.track2,1,19) as card_number,a.MCARD_NO as MCARD_NO, a.cin as CIN, A.EMB_NAME,(select username from USER_DETAILS where userid=A.MAKER_ID) maker_id ,(select card_type_name from product_master where product_code=A.product_code) CARD_TYPE,NVL(BULK_REG_ID,'--')BULK_REG_ID,DECODE(A.CARD_FLAG,'P','PARENT CARD','C','CHILD CARD') CARD_FLAG,DECODE(A.PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG,to_char(A.PRE_DATE,'DD-MM-YY') ISSUE_DATE, c.p_account_no as P_Accountno,c.s1_acct_no as S1_Accountno,c.s2_acct_no as S2_Accountno,c.s3_acct_no as S3_Accountno,c.s4_acct_no as S4_Accountno,c.s5_acct_no as S5_Accountnofrom pers_card_process a,pers_pre_data b, customer_bulk_load C, accountinfo dWHERE A.CIN=B.CIN AND A.CIN= C.customer_id  and d.cin=a.cin and c.customer_id=d.cin and a.order_ref_no=b.order_ref_no and c.order_ref_no=a.order_ref_no and d.order_ref_no=c.order_ref_no and a.card_status='03'; and A.Mkck_Status='P' and c.reg_status='Success'";
*/		
		result="select distinct (select branch_name from branch_master where branch_code=a.card_collect_branch) BRANCH_NAME,"
				+ " a.order_ref_no, ORG_CHN,a.MCARD_NO as MCARD_NO, a.cin as CIN, A.EMB_NAME, A.ACCT_NO,"
				+ " (select username from USER_DETAILS where userid=A.MAKER_ID) maker_id , (select card_type_name from product_master where product_code=A.product_code) CARD_TYPE,"
				+ " NVL(BULK_REG_ID,'--')BULK_REG_ID,DECODE(A.CARD_FLAG,'P','PARENT CARD','C','CHILD CARD') CARD_FLAG,"
				+ " DECODE(A.PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG,to_char(A.PRE_DATE,'DD-MM-YY') ISSUE_DATE, "
				+ " c.p_account_no as P_ACCOUNTNO, "
				+ " c.s1_acct_no as S1_ACCOUNTNO,c.s2_acct_no as S2_ACCOUNTNO,c.s3_acct_no as S3_ACCOUNTNO,"
				+ " c.s4_acct_no as S4_ACCOUNTNO,c.s5_acct_no as S5_ACCOUNTNO "
				+ " from pers_card_process a,pers_pre_data b, customer_bulk_load C,accountinfo d "
				+ " WHERE A.CIN=B.CIN AND A.CIN=C.customer_id  and d.cin=a.cin and c.customer_id=d.cin and a.order_ref_no=b.order_ref_no and"
				+ " c.order_ref_no=a.order_ref_no and d.order_ref_no=c.order_ref_no and a.card_status='03' and A.Mkck_Status='P' and"
				+ " c.reg_status='Success' "+datevalue;
		
		
		/*result = "SELECT  (select branch_name from branch_master  where branch_code=card_collect_branch) BRANCH_NAME,ORDER_REF_NO ,ORG_CHN,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO ACCT_NO,"
				+ "(select username from USER_DETAILS where userid=MAKER_ID) maker_id,"
				+ "(select card_type_name from product_master where product_code=p.product_code) CARD_TYPE,"
				+ "NVL(BULK_REG_ID,'--')BULK_REG_ID,"
				+ "DECODE(CARD_FLAG,'P','PARENT CARD','C','CHILD CARD') CARD_FLAG,"
				+ "DECODE(PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG,"
				+ "to_char(PRE_DATE,'DD-MM-YY') ISSUE_DATE "
				+ "   FROM PERS_CARD_PROCESS p WHERE INST_ID='" + instid + "' AND"
						+ "  CARD_STATUS='03' AND MKCK_STATUS='P'" + datevalue;*/

		enctrace("Condition Query :::" + result);
		// System.out.println("result------>" + result);
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

		// String branchlist = getRequest().getParameter("branchlist");
		trace("branchcode:::" + branchcode);
		String cond = this.getReportCondition(CONDITION);
		String reportheader = "ActiveCard Report";
		String reporttitle = "Debit Cardman";
		enctrace("Got Condition Query ::::" + cond);

		String Query = cond;

		if (!branchcode.equals("ALL")) {
			Query = Query + " AND A.CARD_COLLECT_BRANCH='" + branchcode + "'";
			trace("brach List ::::" + branchcode);
		}

		if (!productCode.equals("ALL")) {
			Query = Query + " AND A.PRODUCT_CODE='" + productCode + "'";
			trace("Product Code ::::" + productCode);
		}

		trace("Query WITH Condition" + Query);
		try {
               List<ActiveCardDTO>  cardList= dao.processReportQuery(instid, Query, jdbctemplate);
			
			trace("cardlist data:::" + cardList);
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

	public String reportGenMaster(String condition, String branchcode, String status, String productCode)
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

		filename = "ActiveCardReport";

		String URLLINK = "cardreportpageActiveCardReport.do?INSTID=" + instid + "&CONDITION=" + condition
				+ "&BRANCHCODE=" + branchcode + "&PRODUCTCODE=" + productCode + "&fromdate=" + fromdate + "&todate="
				+ todate; // getRequest().getParameter("URL").trim();;
		trace("OLDURL:" + URLLINK);
		Properties prop = getCommonDescProperty();
		String serverPath = prop.getProperty("serverpath.location");
		trace("serverPath" + serverPath);
		String replaceUrl = serverPath + URLLINK.replaceAll("\\$", "&");
		trace("URLLINK::::" + replaceUrl);
		String reportCondition = getRequest().getParameter("CONDITION");
		List repconfig = dao.getReportConfigDetails(instid, reportno, jdbctemplate);
		Iterator iterator = repconfig.iterator();

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		while (iterator.hasNext()) {
			Map map = (Map) iterator.next();
			// filename = (String) map.get("REPORTNAME");
			passwdReq = (String) map.get("PASSWORD_REQ");
			pagenoreq = (String) map.get("PAGENO_REQ");
			footercontent = (String) map.get("FOOTERCONTENT");
		}
		java.util.Date dt = null;
		SimpleDateFormat sdt = null;

		String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		filename = filename + "_" + curdatetime + ".pdf";
		trace("filename ::::  " + filename);
		String res = this.generatePDFFile(replaceUrl, filename, passwdReq, pagenoreq, footercontent, username);
		trace("reportGenMaster():::reportGenMaster" + res);

		file_inputstream = new FileInputStream(new File(res));
		setReport_name(filename);

		return "getPDFReport";

	}

	public String ExcelReportGenMaster(List<ActiveCardDTO> excelReportList, String status) throws IOException {
		trace("reportGenMaster started ...");
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		String reportno = "5";
		String filename = "BranchDispatchReport";

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
		
		
		/*// List result = excelReportList;

		String excelparam = this.getExcelParam(instid, reportno, jdbctemplate);

		
		 * List combinedlist = new ArrayList(); combinedlist.add(result);
		 * combinedlist.add(result);
		 
		// String keyDesc = "successful - unsuccessful";
		String res = this.getExcelReport(excelReportList, excelparam, filename);
		// System.out.println("reportGenMaster():::reportGenMaster" + res);
*///		return res;
	}

	private List getExcelReport(String instid, String reportno, JdbcTemplate jdbctemplate) {

		String getrep = null;
		String getrepqry = null;
		try {
			getrepqry = "select REPORTQUERY from PDFREPORTGENRATOR where RNO='" + reportno + "' ";
			enctrace("getrepqry : " + getrepqry);
			getrepqry = (String) jdbctemplate.queryForObject(getrepqry, String.class);
		} catch (EmptyResultDataAccessException e) {
			getrep = null;
		}

		List repodet = null;
		enctrace("reportquery---> " + getrepqry);
		repodet = jdbctemplate.queryForList(getrepqry);
		return repodet;
	}

	private String getExcelParam(String instid, String reportno, JdbcTemplate jdbctemplate2) {
		String xlparam = null;
		try {
			String xlparamqry = "select EXCEL_PARAM from BRANCHWISEREPORT where RNO='" + reportno + "' ";
			enctrace("xlparamqry : " + xlparamqry);
			xlparam = (String) jdbctemplate.queryForObject(xlparamqry, String.class);
		} catch (EmptyResultDataAccessException e) {
			xlparam = null;
		}
		return xlparam;
	}

	private List getReportConfigDetails(String instid, String reportno, JdbcTemplate jdbctemplate) {
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

	/*public String getExcelReport(List<ActiveCardDTO> listqry, String keyDesc, String namestr) {
		trace("	 ***************** Transaction Details report ****************");
		enctrace("**************** Transaction Details report ****************");

		// HttpSession session = getRequest().getSession();
		try {
			output_stream = new ByteArrayOutputStream();
			String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			// trace("dateFormat.format(date) ---> " + curdatetime);
			String defaultname = namestr + curdatetime + ".xls";
			trace("default name" + defaultname);

			SXSSFWorkbook workbook = this.exportInExcel(listqry);
			workbook.write(output_stream);
			String fromdate = getRequest().getParameter("fromdate");
			trace("from date getting" + fromdate);
			String todate = getRequest().getParameter("todate");
			trace("todate  getting" + todate);
			setTransctionreport(defaultname);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			output_stream.flush();
			trace("Close file@@@@@@@ ");
			return "transactionexcel";
		} catch (Exception e) {
			trace("ERROR: ->" + e.getMessage());
			e.printStackTrace();
			addActionError("Could not generate report");
		}
		return "globalreporterror";
	}
*/
	public String generatePDFFile(String link_name_with_id, String filename, String passwdReq, String pagenoreq,
			String footercontent, String username) throws MalformedURLException {

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
			// pd4ml.generateOutlines(true);
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
				BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
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

	public SXSSFWorkbook exportInExcel(List<ActiveCardDTO> cardListDto) {

		SXSSFWorkbook workBook = null;

		try {
			workBook = new SXSSFWorkbook();
			trace("=====Preaparing Data export to Excel sheet======");
			Sheet sheet = workBook.createSheet("CardListData");

			setHeader(sheet);
			setBody(sheet, cardListDto);

			trace("=====Excel Written Success======");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return workBook;

	}

	public void setHeader(Sheet sheet) {
		// create heading
		trace("==creating Heading for Excel sheet starts===");
		Row rowHeading = sheet.createRow(0);

		rowHeading.createCell(0).setCellValue("MASKED_CARD_NUMBER");
		rowHeading.createCell(1).setCellValue("EMBOSSING_NAME");
		rowHeading.createCell(2).setCellValue("BRANCH_NAME");
		/*rowHeading.createCell(3).setCellValue("MCARD_NO");
		rowHeading.createCell(4).setCellValue("CIN");
		//rowHeading.createCell(5).setCellValue("ACCOUNT_NO");
		rowHeading.createCell(5).setCellValue("EMB_NAME");
		rowHeading.createCell(6).setCellValue("CARD_TYPE");
		rowHeading.createCell(7).setCellValue("MAKER_ID");
		rowHeading.createCell(8).setCellValue("BULK_REF_ID");
		rowHeading.createCell(9).setCellValue("CARD_FLAG");
		rowHeading.createCell(11).setCellValue("PC_FLAG");
		//rowHeading.createCell(11).setCellValue("CARDSTATUS");
		rowHeading.createCell(10).setCellValue("ISSUE_DATE");
		rowHeading.createCell(11).setCellValue("P_ACCOUNTNO");
		rowHeading.createCell(12).setCellValue("S1_ACCOUNTNO");
		rowHeading.createCell(13).setCellValue("S2_ACCOUNTNO");
		rowHeading.createCell(14).setCellValue("S3_ACCOUNTNO");
		rowHeading.createCell(15).setCellValue("S4_ACCOUNTNO");
		rowHeading.createCell(16).setCellValue("S5_ACCOUNTNO");*/
		
		trace("==creating Heading for Excel sheet ends here===");

	}

	public void setBody(Sheet sheet, List<ActiveCardDTO> cardListDto) {
		int r = 1;
		trace("==Setting Data to the excel sheet body starts===");
		for (ActiveCardDTO dto : cardListDto) {
			Row row = sheet.createRow(r);

			row.createCell(0).setCellValue(dto.getBranchName());
			row.createCell(1).setCellValue(dto.getOrderRefNo());
			row.createCell(2).setCellValue(dto.getOrgChn());
			row.createCell(3).setCellValue(dto.getMcardNo());
			row.createCell(4).setCellValue(dto.getCin());
			//row.createCell(5).setCellValue(dto.getAccountNo());
			row.createCell(5).setCellValue(dto.getEmbName());
			row.createCell(6).setCellValue(dto.getCardType());
			row.createCell(7).setCellValue(dto.getMakerId());
			row.createCell(8).setCellValue(dto.getBulkRefId());
			row.createCell(9).setCellValue(dto.getCardFalg());
			/*row.createCell(11).setCellValue(dto.getPcFlag());*/
			//row.createCell(11).setCellValue(dto.getCardStatus());
			row.createCell(10).setCellValue(dto.getIssueDate());
			row.createCell(11).setCellValue(dto.getpaccountno());
			row.createCell(12).setCellValue(dto.gets1accountno());
			row.createCell(13).setCellValue(dto.gets2accountno());
			row.createCell(14).setCellValue(dto.gets3accountno());
			row.createCell(15).setCellValue(dto.gets4accountno());
			row.createCell(16).setCellValue(dto.gets5accountno());

			r++;
		}
		trace("==Setting Data to the excel sheet body ends here===");

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
			sheet = workbook.createSheet("BranchDispatchReport");
			int i = 0;
			while (combined_itr.hasNext()) {
				List lst = (List) combined_itr.next();
				trace(" ----- lst  ---- " + lst.size());
				
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
	
}


