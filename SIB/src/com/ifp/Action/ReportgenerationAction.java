package com.ifp.Action;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException; 
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.ifp.beans.EODActionBeans;
import com.ifd.SubProduct.AddSubProdActionDao;
import com.ifp.dao.GLConfigureDAO;
import com.ifp.dao.ReportgenerationActionDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.PDFReportGenerator;
import com.ifp.util.newPDFReportGenerator;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
// Convert Collaction to String 
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
// Run time Call Methods 
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;





public class ReportgenerationAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private HashMap reportParams = new HashMap();	
	private HttpServletRequest servletRequest;
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
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

	CommonDesc commondesc = new CommonDesc();
	String amountformat = "999G999G999G999G999G999D99MI";
	String TABLENAME = null;
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	 
	private String report_name;
	public String getReportname() {
		return report_name;
	}
	
	private String Fee_report_name;		
	public String getFee_report_name() {
		return Fee_report_name;
	}
	public void setFee_report_name(String fee_report_name) {
		Fee_report_name = fee_report_name;
	}
	
	private String vas_report_name;		
	public String getVas_report_name() {
		return vas_report_name;
	}
	public void setVas_report_name(String vas_report_name) {
		this.vas_report_name = vas_report_name;
	}

	List currencylist;
	public List getCurrencylist() {
		return currencylist;
	}
	public void setCurrencylist(List currencylist) {
		this.currencylist = currencylist;
	}
	public CommonUtil comUtil(){
			CommonUtil  comutil = new CommonUtil();
			return comutil;
	} 
	
	private List stment_report_list;	
	public List getStment_report_list() {
		return stment_report_list;
	}
	public void setStment_report_list(List stment_report_list) {
		this.stment_report_list = stment_report_list;
	}
	
	private ByteArrayInputStream input_stream;
	private ByteArrayOutputStream output_stream;
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

	public void setReportname(String reportname) {
		this.report_name = reportname;
	}
	List fraudreportlist;
	public List getFraudreportlist() {
		return fraudreportlist;
	}
	public void setFraudreportlist(List fraudreportlist) {
		this.fraudreportlist = fraudreportlist;
	}
	List prodlist;
	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}
	public List getProdlist() {
		return prodlist;
	}
	List branchlist;
	public List getBranchlist() {
		return branchlist;
	}	
	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}
	
	private List Fraudreport_list;
	public List getFraudreport_list() {
		return Fraudreport_list;
	}
	public void setFraudreport_list(List fraudreport_list) {
		Fraudreport_list = fraudreport_list;
	}
	
	private List reports_name_list;	
	public List getReports_name_list() {
		return reports_name_list;
	}
	public void setReports_name_list(List reports_name_list) {
		this.reports_name_list = reports_name_list;
	}

	
	
	public void validate(){
		HttpSession session = getRequest().getSession();
		String apptype = (String)session.getAttribute("APPLICATIONTYPE");
		if( apptype != null && apptype.equals("CREDIT")){
			TABLENAME = " IFC_CARD_PRODUCTION ";
		}else{
			TABLENAME = " CARD_PRODUCTION ";
		}
	}
	public String cardReportproperty(){
		HttpSession session = getRequest().getSession();
		trace("Card Based Reports Start ");
		String ReportType = "CARD",Report_catogry = "GEN"; 
		String query_report_names = "select * from IFP_REPORT_MASTER where REPORT_TYPE='"+ReportType+"' and REPORT_CATOGERY='"+Report_catogry+"' and REPORT_ACTION='1' order by report_order";
		trace("query_report_names " +query_report_names);
		try{
			List report_list = jdbctemplate.queryForList(query_report_names);
			if(!(report_list.isEmpty())){
				trace("report_list " +report_list);
				setReports_name_list(report_list);
				session.setAttribute("Errorstat", "S");
			}else{
				session.setAttribute("Errorstat", "E");
				session.setAttribute("ErrorMessage", "No Reports Configured");			
			}
		}catch (Exception ex) {
			trace("Error While Fetching "+ex);
			session.setAttribute("Errorstat", "E");
			session.setAttribute("ErrorMessage", "Error While Fetching the Report Names " +ex.getMessage());
		}		
		return "cardreportproperties";
	}
	
	public String txnReportproperty(){
		HttpSession session = getRequest().getSession();
		trace("Card Based Reports Start ");
		String ReportType = "TXN",Report_catogry = "GEN";
		 
		String query_report_names = "select * from IFP_REPORT_MASTER where REPORT_TYPE='"+ReportType+"' and REPORT_CATOGERY='"+Report_catogry+"' and REPORT_ACTION='1' order by report_order";
		trace("query_report_names " +query_report_names);
		try{
			List report_list = jdbctemplate.queryForList(query_report_names);
			if(!(report_list.isEmpty())){
				trace("report_list " +report_list);
				setReports_name_list(report_list);
				session.setAttribute("Errorstat", "S");
			}else{
				session.setAttribute("Errorstat", "E");
				session.setAttribute("ErrorMessage", "No Reports Configured");			
			}
		}catch (Exception ex) {
			trace("Error While Fetching "+ex);
			session.setAttribute("Errorstat", "E");
			session.setAttribute("ErrorMessage", "Error While Fetching the Report Names " +ex.getMessage());
		}		
		return "txnreportproperties";
	}	
	
	public String userReportproperty(){
		HttpSession session = getRequest().getSession();
		trace("User Based Reports Start ");
		//String ReportId = "UR",Report_catogry = "GEN";
		String ReportType = "USR",Report_catogry = "GEN"; 
		//String report_names = "select (REPORT_ORDER||'~'||REPORT_CATOGERY||'~'||REPORT_ID) as REPORTID,trim(report_name) as REPORTNAME from IFPREPORT_PROPERTY where report_id='"+ReportId+"' and report_catogery='"+Report_catogry+"' order by report_order";
		String query_report_names = "select * from IFP_REPORT_MASTER where REPORT_TYPE='"+ReportType+"' and REPORT_CATOGERY='"+Report_catogry+"' and REPORT_ACTION='1' order by report_order";
		trace("UEr Reports Qury===> "+query_report_names);
		String Reportname=null,Reportorder=null;
		try{
			List reportsname = jdbctemplate.queryForList(query_report_names);
			if(!(reportsname.isEmpty())){
				setReports_name_list(reportsname);				
				session.setAttribute("Errorstat", "S");
			}else{
				session.setAttribute("Errorstat", "E");
				session.setAttribute("ErrorMessage", "No Reports Configured ");
			}
		}catch (Exception ex){
			trace("Error While Fetching "+ex);
			session.setAttribute("Errorstat", "E");
			session.setAttribute("ErrorMessage", "Error While Fetching the Report Names ");
		}
		return "userreportproperties";
	}
	
	public String selectedReport(){
		HttpSession session 		= 	getRequest().getSession(); 
		String i_Name 				= 	(String)session.getAttribute("Instname");
		String in_name 				= 	i_Name.toUpperCase();
		String reportType			=	(getRequest().getParameter("reportvalue"));
		String from_date			=	(getRequest().getParameter("fromdate"));
		String to_date				=	(getRequest().getParameter("todate"));
		trace("=======================================================================================");
		trace("The Seleted Report is "+reportType+"Institution is "+in_name);
		trace("The Seleted From Date is "+from_date);
		trace("The Seleted To Date is "+to_date);
		trace("=======================================================================================");
		/*String[] toKens = reportType.split("~");
		for(int j=0;j<3;j++)
		{
			trace("The Token "+j+"is "+toKens[j]);
		}*/		
		String Query_toProcess 	= 	"NO";
		String messges 			= 	"",title="";
		String repor_tname 		= 	"DefaultReport.pdf";
		//int Option = Integer.parseInt(toKens[0]);
		int option 				= 	Integer.parseInt(reportType);
		try {
			switch(option) {
				case 1:
					trace("User Added Report");
					Query_toProcess = "select USERNAME,decode(USERSTATUS,'1','Active') as USERSTATUS,USERID,FIRSTNAME,LASTNAME,EMAILID,CREATIONDATE,CREATEDUSERID,(select PROFILE_NAME from "+getProfilelistMain()+" where PROFILE_ID=PROFILEID and INSTID='"+in_name+"') as PROFILENAME,(select BRANCH_NAME from BRANCH_MASTER where BRANCH_CODE=BRANCHCODE and INST_ID='"+in_name+"') as BRANCHNAME  from IFP_USER_DETAILS where INSTID='"+in_name+"' and userstatus='1' and " +
					"USERBLOCK='0' and trunc(creationdate) BETWEEN to_date('"+from_date+"','DD-MM-YY') and to_date('"+to_date+"','DD-MM-YY')";
					messges 	= " NO RECORD FOUND";
					title 		= " USER CREATED REPORT ";
					reportParams.put("reporttitle",title);
					repor_tname = "Usercreatedreport.pdf";
					break;
				case 2:
					trace("User Deleted Report");
					Query_toProcess = "select USERNAME,decode(USERSTATUS,'2','Deleted') as USERSTATUS,USERID,FIRSTNAME,LASTNAME,EMAILID,CREATIONDATE,CREATEDUSERID,(select PROFILE_NAME from "+getProfilelistMain()+" where PROFILE_ID=PROFILEID and INSTID='"+in_name+"') as PROFILENAME,(select BRANCH_NAME from BRANCH_MASTER where BRANCH_CODE=BRANCHCODE and INST_ID='"+in_name+"') as BRANCHNAME   from IFP_USER_DETAILS where userstatus='2' and INSTID='"+in_name+"'";
					messges 	= " NO RECORD FOUND ";
					title 		= " USER DELETED REPORT ";
					reportParams.put("reporttitle",title);
					repor_tname = "Userdeletedreport.pdf";
					break;
				case 3:
					trace("User BLOCKED Report");
					Query_toProcess = "select USERNAME,decode(USERBLOCK,'2','Blocked') as USERSTATUS,USERID,FIRSTNAME,LASTNAME,EMAILID,CREATIONDATE,CREATEDUSERID,(select PROFILE_NAME from "+getProfilelistMain()+" where PROFILE_ID=PROFILEID and INSTID='"+in_name+"') as PROFILENAME,(select BRANCH_NAME from BRANCH_MASTER where BRANCH_CODE=BRANCHCODE and INST_ID='"+in_name+"') as BRANCHNAME  from IFP_USER_DETAILS where userstatus='1' and USERBLOCK='1' and INSTID='"+in_name+"'";
					messges 	= " NO RECORD FOUND ";
					title 		= " USER BLOCKED REPORT ";
					reportParams.put("reporttitle",title);
					repor_tname = "Userblockedreport.pdf";
					break;				
				default:
					Query_toProcess="ERR";
					break;
					
			}
			Properties prop 			= 	comUtil().getPropertyFile();		
			//GenericPackager packager	= 	new GenericPackager(prop.getProperty("IS0.PACKAGER"));
			String reporticon = prop.getProperty("IS0.PDFREPORTICON");
			trace("Report Icon Path "+ reporticon);
			reportParams.put("logo",reporticon);
			String reportwatermrk = prop.getProperty("IS0.PDFREPORTWATERMRK");
			trace("Report water Mark"+ reportwatermrk);
			reportParams.put("watermrk",reportwatermrk);
			
			if(Query_toProcess.equals("NO") || Query_toProcess.equals("ERR")) {
				trace("No Details Query Created ");
				return userReportproperty();
			}
			trace("QUery To execute is "+Query_toProcess);			
			List queryResult = reportResult(Query_toProcess,jdbctemplate);
			trace("queryResult====>? "+queryResult);
			if((queryResult.isEmpty())) {
				
				return "no_data_period";
			}else {
				Iterator itr_card_list	=	queryResult.iterator();
				String username,userstatus,userid;
				trace("result in iterator ===> "+itr_card_list);
				while(itr_card_list.hasNext()) {
					String usercreator_name ="Super Admin";					
	   				Map mapper_orderdetails		=	(Map)itr_card_list.next();
	   				username=((String)mapper_orderdetails.get("USERNAME"));
	   				Object ustatus =mapper_orderdetails.get("USERSTATUS");
	   				userstatus 	= 	ustatus.toString();
	   				Object uid 	= 	mapper_orderdetails.get("USERID");
	   				userid=uid.toString();
	   				String creatorid 	= 	(String) mapper_orderdetails.get("CREATEDUSERID");
	   				if (!creatorid.equals("SU"))
	   					usercreator_name=this.getCreatorName(creatorid,jdbctemplate,in_name);
	   				Object ucreateddate =mapper_orderdetails.get("CREATIONDATE");
	   				String usercreateddate=ucreateddate.toString();	
	   				Object ufirstname =mapper_orderdetails.get("FIRSTNAME");
	   				String userfirstname=ufirstname.toString();
	   				Object ulastname =mapper_orderdetails.get("LASTNAME");
	   				String userlastname=ulastname.toString();
	   				Object umail =mapper_orderdetails.get("EMAILID");
	   				String usermail=umail.toString();
	   				Object uprofilename =mapper_orderdetails.get("PROFILENAME");
	   				String userprofilename=uprofilename.toString();
	   				Object ubranchname =mapper_orderdetails.get("BRANCHNAME");
	   				String userbranchname=ubranchname.toString();
					
								
				}
				// String path = location.substring(0, location.indexOf("/tomcat")) + "/data/events/images/";
				trace("Report Name is ===> "+repor_tname);
				setReportname(repor_tname);
				return "userdatareports";
			}			
		}catch (Exception e) { 
			trace("Error while generating Report " + e);
			e.printStackTrace();
			return "no_data_period";
		}
	}
	
	
	public List reportResult(String Query_toProcess,JdbcTemplate jdbcTemplate){
		List querylist = null;
		querylist = jdbcTemplate.queryForList(Query_toProcess);
		return querylist;
	}
	
	public void sampleReports(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		trace(dateFormat.format(date));
		String pdfname = "CardReport"+dateFormat.format(date);
		//Document document = new Document(PageSize.LETTER.rotate());
		Document document = new Document();
		try{
		trace(" Sample Report Generation Starts ");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		//String qury = "select co.order_ref_no as REFNUM,pd.product_name as NAME,cp.fname as FNAME,co.CIN as CIN,co.card_quantity as CNT from PERS_CARD_ORDER co,IFP_CUSTINFO_PROCESS cp,IFP_INSTPROD_DETAILS pd where co.inst_id = pd.inst_id and co.product_code = pd.product_code and co.cin = cp.cin";
		String qury = "select cp.card_no as CARDNO, cp.cin as CIN, cp.order_ref_no as REFNUM, to_char(cp.generated_date,'DD-MON-YY') as GENDAT, cp.branch_code as BRCODE, pd.SUB_PRODUCT_NAME as PNAME, cu.fname as FNAME from PERS_CARD_PROCESS cp,IFP_INSTPROD_DETAILS pd,IFP_CUSTINFO_PROCESS cu where cp.inst_id = pd.inst_id and cp.cin = cu.cin and cp.product_code = pd.product_code and cp.inst_id='BUCB' order by REFNUM,CARDNO";
	 
		List sampellist = null;
	
		sampellist = jdbctemplate.queryForList(qury);
		trace("sampellist===>"+sampellist);
			if(!(sampellist.isEmpty())){
				String[] headers = new String[] {"Card No", "Customer No", "Reference No","Generated Date" ,"Branch Code","Product Name","Customer Name"};
				trace("Headers Lenght=====> "+headers.length);
				PdfWriter.getInstance(document, new FileOutputStream(new File("d://PDF_TEST//"+pdfname+".pdf")));
				document.open();
				document.addTitle("Table Demo");
				PdfPTable table = new PdfPTable(headers.length);
				table.setWidthPercentage(110);
				float[] columnWidths = new float[] {20f, 20f, 18f, 20f,20f,25f,20f};
				table.setWidths(columnWidths);
				
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	            for (int i = 0; i < headers.length; i++){
	            	//trace(" headers[i]===> "+headers[i]);
	                String header = headers[i];
	                PdfPCell cell = new PdfPCell();
	                cell.setGrayFill(0.9f);
	                cell.setPhrase(new Phrase(header.toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN,8, Font.NORMAL)));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                //cell.setVerticalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	            }
	            table.completeRow();
	            String valuedata[] = new String[headers.length];
				Iterator itr = sampellist.iterator();
				while(itr.hasNext()){
					int i=0,column=0;
					Map map = (Map)itr.next();
					Iterator keyItr = map.keySet().iterator();				
					while(keyItr.hasNext()){
						String key = (String) keyItr.next();
						//trace("Key " + key);
						valuedata[i] =(String)map.get(key);
						i++;
						column++;
					}
					 for(int j=0;j<column;j++){
						 //trace("valuedata[j].toUpperCase()====>"+valuedata[j].toUpperCase());
						 PdfPCell cell = new PdfPCell();
						 cell.setPhrase(new Phrase( valuedata[j].toUpperCase(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL)));
						 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						 table.addCell(cell);
					 }					
				}
				 table.completeRow();			
				 document.add(table);
			}
		}catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
			trace(" Sample Report Generation Ends ");		
	}
	
public String commanonCardStatusReport(){
	String instid 		=  comInstId();  			
	HttpSession session = getRequest().getSession();  
 	
	int x= commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
	trace("Required checking..got : " + x );
	if( x < 0 ){
		return null;
	}
	try {					
		prodlist = commondesc.getProductList( instid, jdbctemplate, session ); 
		trace(" prodlist====> "+prodlist);
		if( prodlist.isEmpty()){
			addActionError("No Products Available");
			return "required_home";
		}
		setProdlist(prodlist);
		branchlist =  this.commondesc.generateBranchList(instid,jdbctemplate);
		if( branchlist.isEmpty() ){
			addActionError("No Branch Configured");
			return "required_home";
		}
		setBranchlist(branchlist); 
		session.setAttribute("curerr", "S"); 
	} catch (Exception e) {
		session.setAttribute("curerr", "E");
		session.setAttribute("curmsg", "Error while execute the instantOrder" + e.getMessage()); 
	}
		 
	return SUCCESS;
}	
public String transactionstatus(){		 
	return "transactionstatus";
}

public String cardReportStatus() {
	String result = commanonCardStatusReport();
	if( result == null ){
		return "required_home";
	}
	return "cardreportstatus";			
}

public String InstcardReportStatus(){
	cardReportStatus();
	return "instcardreportstatus";
}

public void sampleReports1() 
{
	Document document = new Document();
	try{
		PdfWriter.getInstance(document,new FileOutputStream("D://PDF_TEST//TableColumnWidth.pdf"));
		document.open();
		PdfPTable table = new PdfPTable(4);
		table.addCell(new PdfPCell(new Phrase("Cell 1")));
		table.addCell(new PdfPCell(new Phrase("Cell 2")));
		table.addCell(new PdfPCell(new Phrase("Cell 3")));
		table.addCell(new PdfPCell(new Phrase("Cell 4")));
		float[] columnWidths = new float[] {10f, 20f, 30f, 10f};
		table.setWidths(columnWidths);
		document.add(table);
	}catch (DocumentException | FileNotFoundException e){
		e.printStackTrace();
	}finally{
		document.close();
	}	
}

public String CommangenCardReportStatus(String tablename ){
	trace("**************** Instant Card Status Report  ****************");
	enctrace("**************** Instant Card Status Report  ****************");
	// Get Values From Form 
	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
	HttpSession session 		= 	getRequest().getSession();
 
	output_stream = new ByteArrayOutputStream();
	Document document = new Document();
	HttpServletRequest request= getRequest();
	String ses_insname 			= 	(String)session.getAttribute("Instname");
	String in_name 				= 	ses_insname.toUpperCase();
	String fromfrm_branchcode	=	(getRequest().getParameter("branchcode")).trim();
	String fromfrm_cardtype		=	(getRequest().getParameter("cardtype")).trim();
	String fromfrm_subproductlist=	(getRequest().getParameter("subproductlist")).trim();
	String fromfrm_fromdate		=	(getRequest().getParameter("fromdate"));
	String fromfrm_todate		=	(getRequest().getParameter("todate"));
	String fromfrm_cardstatus	=	(getRequest().getParameter("cardstatus")).trim();
	DateFormat dateFormat		= 	new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
	Date date					=	new Date();
	String propertiesname		= 	"CardStatus";
	String product_qry="",subproduct_qry="";
	trace("======================================================================================");
	trace("Institution is "+in_name);
	trace("The Seleted Ffromfrm_branchcode "+fromfrm_branchcode);
	trace("The Seleted fromfrm_cardtype is "+fromfrm_cardtype);
	trace("The Seleted fromfrm_subproductlist "+fromfrm_subproductlist);
	
	
	trace("The Seleted From Date is "+fromfrm_fromdate);
	trace("The Seleted To Date is "+fromfrm_todate);
	trace("The Product  ID   "+fromfrm_cardtype);
	trace("The SUB Product ID     "+fromfrm_subproductlist);
	trace("=======================================================================================");		
	if (!fromfrm_cardtype.equals("000"))
	product_qry="' AND PRODUCT_CODE='"+fromfrm_cardtype;		
	
	if (!fromfrm_subproductlist.equals("000"))
	subproduct_qry="' AND SUB_PROD_ID='"+fromfrm_subproductlist;		
	
	String Query_toProcess 	= 	"NO";
	String messges 			= 	"NO RECORD FOUND",title="";
	String repor_tname 		= 	"DefaultReport.pdf";
	//int Option = Integer.parseInt(toKens[0]);
	int option 				= 	Integer.parseInt(fromfrm_cardstatus);
	try {
		switch(option) {
			case 1:
				trace("CARD GENERETED REPORT");
				Query_toProcess = "select rownum,ORDER_REF_NO,CARD_NO,PRODUCT_CODE,SUB_PROD_ID,decode(CARD_STATUS,'01','Waiting For Pin','02','Waiting For PRE','03','Waiting For Recieved','04','Waiting For Issue','05','Activated') as action,GENERATED_DATE,MAKER_ID from "+tablename+" where INST_ID='"+in_name+"' and CARD_STATUS='"+fromfrm_cardstatus+"' and " +
						" BRANCH_CODE='"+fromfrm_branchcode+product_qry+subproduct_qry+"' and trunc(GENERATED_DATE) BETWEEN to_date('"+fromfrm_fromdate+"','DD-MM-YY') and to_date('"+fromfrm_todate+"','DD-MM-YY')";
				title 		= " CARD GENERETED REPORT ";					
				repor_tname = "CardGenratedReport";
				break;
			case 2:
				trace(" PIN GENERETED REPORT");
				Query_toProcess = "select rownum,ORDER_REF_NO,CARD_NO,PRODUCT_CODE,SUB_PROD_ID,decode(CARD_STATUS,'01','Waiting For Pin','02','Waiting For PRE','03','Waiting For Recieved','04','Waiting For Issue','05','Activated') as action,GENERATED_DATE,MAKER_ID from "+tablename+" where INST_ID='"+in_name+"' and CARD_STATUS='"+fromfrm_cardstatus+"' and " +
						" BRANCH_CODE='"+fromfrm_branchcode+product_qry+subproduct_qry+"' and trunc(PIN_DATE) BETWEEN to_date('"+fromfrm_fromdate+"','DD-MM-YY') and to_date('"+fromfrm_todate+"','DD-MM-YY') order by PRODUCT_CODE ";
				title 		= " PIN GENERETED REPORT ";					
				repor_tname = "PINGENERETEDREPORT";
				break;
			case 3:
				trace("PRE GENERETED REPORT");
				Query_toProcess = "select rownum,ORDER_REF_NO,CARD_NO,PRODUCT_CODE,SUB_PROD_ID,decode(CARD_STATUS,'01','Waiting For Pin','02','Waiting For PRE','03','Waiting For Recieved','04','Waiting For Issue','05','Activated') as action,GENERATED_DATE,MAKER_ID from "+tablename+" where INST_ID='"+in_name+"' and CARD_STATUS='"+fromfrm_cardstatus+"' and " +
						" BRANCH_CODE='"+fromfrm_branchcode+product_qry+subproduct_qry+"' and trunc(PRE_DATE) BETWEEN to_date('"+fromfrm_fromdate+"','DD-MM-YY') and to_date('"+fromfrm_todate+"','DD-MM-YY')";
				title 		= " PRE GENERETED REPORT ";					
				repor_tname = "PREGENERETEDREPORT";
				break;
			case 4:
				trace("RECEIVED CARD REPORT");
				Query_toProcess = "select rownum,ORDER_REF_NO,CARD_NO,PRODUCT_CODE,SUB_PROD_ID,decode(CARD_STATUS,'01','Waiting For Pin','02','Waiting For PRE','03','Waiting For Recieved','04','Waiting For Issue','05','Activated') as action,GENERATED_DATE,MAKER_ID from "+tablename+" where INST_ID='"+in_name+"' and CARD_STATUS='"+fromfrm_cardstatus+"' and " +
						" BRANCH_CODE='"+fromfrm_branchcode+product_qry+subproduct_qry+"' and trunc(RECV_DATE) BETWEEN to_date('"+fromfrm_fromdate+"','DD-MM-YY') and to_date('"+fromfrm_todate+"','DD-MM-YY')";
				title 		= " RECEIVED CARD REPORT ";					
				repor_tname = "RECEIVEDCARDREPORT";
				break;		
			case 5:
				trace("ISSUED CARD REPORT");
				Query_toProcess = "select rownum,ORDER_REF_NO,CARD_NO,PRODUCT_CODE,SUB_PROD_ID,decode(CARD_STATUS,'02','TEMP BLOCK','03','LOST / STOLEN','04','CLOSE','05','ACTIVE','06','REPIN','07','RE-ISSUED','08','FIRST USE STATUS','09','NOT ACTIVATED','10','DAMAGE',CARD_STATUS) as action,GENERATED_DATE,MAKER_ID from  "+TABLENAME+" where INST_ID='"+in_name+"' and " +
						" BRANCH_CODE='"+fromfrm_branchcode+product_qry+subproduct_qry+"' and trunc(RECV_DATE) BETWEEN to_date('"+fromfrm_fromdate+"','DD-MM-YY') and to_date('"+fromfrm_todate+"','DD-MM-YY') ORDER BY SUB_PROD_ID" ;
				title 		= " ISSUED CARD REPORT ";					
				repor_tname = "ISSUEDCARDREPORT"; 
				break;		
			default:	
				Query_toProcess="ERR";
				break;
		}
		Properties prop 			= 	comUtil().getPropertyFile();		
		//GenericPackager packager	= 	new GenericPackager(prop.getProperty("IS0.PACKAGER"));
		// Attach Report Parameters 
		enctrace("query "+Query_toProcess);
		String curdatetime			=	dateFormat.format(date);			
		//reportParams.put("reporttitle",title);
		//	String iconurl 		=	comUtil().GetUrlReportIcon(getRequest());
		//	trace("Report Icon Path "+ iconurl);
		//reportParams.put("logo",iconurl);
		String reportwatermrk = prop.getProperty("IS0.PDFREPORTWATERMRK");		
		trace("Report water Mark"+ reportwatermrk);
		//reportParams.put("watermrk",reportwatermrk);
		String username = (String)session.getAttribute("SS_USERNAME");
		trace("User Name  ============="+username);
		//reportParams.put("username",username);
		String branch_name 		=	this.commondesc.genBranchDesc(fromfrm_branchcode,in_name,jdbctemplate);
		//reportParams.put("branchname",branch_name);
		trace(" Branch Name "+ branch_name);
		//reportParams.put("fromdate",fromfrm_fromdate);
		trace("From Date "+ fromfrm_fromdate);
		//reportParams.put("todate",fromfrm_todate);
		trace("From Date "+ fromfrm_todate);
		repor_tname		=	repor_tname+".pdf";
		trace("Report Name is ===> "+repor_tname);
		setReportgendownloadfilename(repor_tname);
		trace("QUery To execute is "+Query_toProcess);			
		List queryResult = reportResult(Query_toProcess,jdbctemplate);
		trace("queryResult size  "+queryResult.size());
			if((queryResult.isEmpty())) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "  Record Not Found " );
				trace(" Error while generating Report  : Record Not Found ");		
				trace("\n\n");
				enctrace("\n\n");
				return "globalreporterror";
			}else {				
				String reporttitle="";
				reporttitle+="Branch Name ,:,"+branch_name+",From Date,:,"+fromfrm_fromdate+",To Date,:,"+fromfrm_todate;
				ListIterator itr_card_list	=	queryResult.listIterator();
				String resultqry_cardno,resultqry_product,resultqry_subproduct,resultqry_rownum,resultqry_referno;
				trace("result in iterator ===> "+itr_card_list);				
				PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertiesname, request);				
				trace("report Title Values "+reporttitle);
				pdfgen.addPDFTitles(document, title,ALIGN_CENTER);
				pdfgen.addSingleHeader(document, 3, reporttitle, ALIGN_LEFT, 50);
				String previousproduct="NO_PRODUCT";	
				int cnt = 1;
				while(itr_card_list.hasNext()) {			
					Map mapper_orderdetails		=	(Map)itr_card_list.next();	   				   				
					resultqry_referno			=	((String)mapper_orderdetails.get("ORDER_REF_NO"));
					resultqry_cardno			=	((String)mapper_orderdetails.get("CARD_NO"));
					Object obj_rownum 			=	mapper_orderdetails.get("ROWNUM");
					resultqry_rownum			= 	obj_rownum.toString();	   			
					resultqry_product 		    = 	commondesc.getProductdesc(in_name,((String)mapper_orderdetails.get("PRODUCT_CODE")) , jdbctemplate);
					//resultqry_product 		= (String)mapper_orderdetails.get("PRODUCT_CODE");
					resultqry_subproduct		=	commondesc.getSubProductdesc(in_name,((String)mapper_orderdetails.get("SUB_PROD_ID")) , jdbctemplate );
					String resultqry_action		= 	(String) mapper_orderdetails.get("ACTION");	   				
					Object obj_actiondate 		= 	mapper_orderdetails.get("GENERATED_DATE");
					String resultqry_actiondate	=	obj_actiondate.toString();	
					Object obj_generatedby 		=	mapper_orderdetails.get("MAKER_ID");	   				
					String resultqry_generatedby= 	obj_generatedby.toString();	
					String generatedby			= 	getCreatorName(resultqry_generatedby,jdbctemplate,in_name);
					mapper_orderdetails.put("ROWNUM", Integer.toString(cnt));
					mapper_orderdetails.put("ORDER_REF_NO", resultqry_referno);
					mapper_orderdetails.put("CARD_NO", resultqry_cardno);	   				
					mapper_orderdetails.put("PRODUCT_CODE", resultqry_product);
					mapper_orderdetails.put("SUB_PROD_ID", resultqry_subproduct);
					mapper_orderdetails.put("ACTION", resultqry_action);
					mapper_orderdetails.put("GENERATED_DATE", resultqry_actiondate);
					mapper_orderdetails.put("MAKER_ID", generatedby);
					mapper_orderdetails.remove("mapper_orderdetails");
					itr_card_list.remove();
					itr_card_list.add(mapper_orderdetails);
					cnt++;
				}
				pdfgen.addPDFDataByGroup(document, pdfgen.reportsumfield,pdfgen.reportheader,queryResult, "PRODUCT_CODE");						
				pdfgen.closePDF(document);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush();
				trace("\n\n");
				enctrace("\n\n");
				return "itextpdfreport2";
			}			
	}catch (Exception e) { 
		session.setAttribute("preverr", "E");
		session.setAttribute("prevmsg", " Error while generating Report" );
		trace("Error while generating Report " + e.getMessage());	
		e.printStackTrace();
		trace("\n\n");
		enctrace("\n\n");
		return "globalreporterror";
	}
}
	
public String genCardReportStatus(){
	String cardtable = "PERS_CARD_PROCESS";
	return CommangenCardReportStatus(cardtable); 
}

public String genInstCardReportStatus(){
	String cardtable = "INST_CARD_PROCESS";
	return CommangenCardReportStatus(cardtable); 
}
public void getPersonalSubProductetails() {
	 
	String subproduct, productname,product_qry="";
	List result;
	String instid =comInstId();
	String prodid=getRequest().getParameter("prodid").trim();
	String cardtype=getRequest().getParameter("cardtype").trim();
	if(!prodid.equals("000"))
		product_qry = "' and PRODUCT_CODE='"+prodid;
	try{
		String subproduct_qury = "select SUB_PRODUCT_NAME, SUB_PROD_ID from IFP_INSTPROD_DETAILS where inst_id='"+instid+product_qry+"' "; //and  PERSONALIZED in ('"+cardtype+"','B')
		trace("Sub product list query ===> "+subproduct_qury);
		result = jdbctemplate.queryForList(subproduct_qury);				
		trace("Java List===> "+result);
		if (!(result.isEmpty())) {	 
			String sel =  "<select name='subproductlist' id='subproductlist'><option value=\"-1\">--Select Sub-Product--</option>";
			sel = sel+ "<option value=\"000\">ALL</option>";
			Iterator itr = result.iterator();
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				subproduct = ((String) map.get("SUB_PROD_ID"));
				productname = ((String) map.get("SUB_PRODUCT_NAME"));
				sel = sel + "<option value=\" " + subproduct.trim() + "\">"+ productname + "</option>";
			}
			sel = sel + "</select>";
			getResponse().getWriter().write(sel);
		} else { 
			String noproduct =  "<select name='subproductlist' id='subproductlist'  ><option value=\"-1\">--No Sub-Product--</option></select>";						
			getResponse().getWriter().write(noproduct);	
		}
	}catch(Exception e){
		trace("Error");
	}
}
public void getPersonalSubProductetailsNew() {
	 
	String subproduct, productname,product_qry="";
	List result;
	String instid =comInstId();
	String prodid=getRequest().getParameter("prodid").trim();
	String cardtype=getRequest().getParameter("cardtype").trim();
	if(!prodid.equals("000"))
		product_qry = "' and PRODUCT_CODE='"+prodid;				
	try{
		String subproduct_qury = "select SUB_PRODUCT_NAME, SUB_PROD_ID from IFP_INSTPROD_DETAILS where inst_id='"+instid+product_qry+"'";
		trace("Sub product list query ===> "+subproduct_qury);
		result = jdbctemplate.queryForList(subproduct_qury);				
		trace("Java List===> "+result);
			if (!(result.isEmpty())) {
				String sel = "<select name='subproductlist' id='subproductlist' onchange='return Gettingfeecodelist();'>";
				sel = sel+ "<option value=\"-1\">--Select Sub-Product--</option>";
				sel = sel+ "<option value=\"000\">ALL</option>";
				Iterator itr = result.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					subproduct = ((String) map.get("SUB_PROD_ID"));
					productname = ((String) map.get("SUB_PRODUCT_NAME"));
					sel = sel + "<option value=\" " + subproduct.trim() + "\">"+ productname + "</option>";
				}
				sel = sel + "</select>";
				getResponse().getWriter().write(sel);
			}else {
				String noproduct = "<select name='subproductlist' id='subproductlist' onchange='return Gettingfeecodelist();'>";
				noproduct = noproduct+ "<option value=\"-1\">--No Sub-Product--</option></select>";						
				getResponse().getWriter().write(noproduct);	
			}
	}catch(Exception e){
		trace("Error");
	}
}
public Map<String, Object> getReportParams() {
	return reportParams;
}
public String getCreatorName(String creatorid,JdbcTemplate jdbctemplate,String in_name){
	String creatorname = "Select USERNAME from IFP_USER_DETAILS where USERID='"+creatorid+"' AND INSTID='"+in_name+"'";
	trace("creatorname==> "+creatorname);
	String ret_val = (String)jdbctemplate.queryForObject(creatorname, String.class);
	return ret_val;
}

public String comInstId(){
	HttpSession session = getRequest().getSession();
	String instid = (String)session.getAttribute("Instname"); 
	return instid;
}

public String comInstId( HttpSession session ){			 
	String instid = (String)session.getAttribute("Instname"); 
	return instid;
}
	 
	 
	 //###########################################
	 	// Fraud Report   
	 //##########################################
	
	public String FraudmgmtReportHome(){
		HttpSession session = getRequest().getSession();
		 
		trace(" FraudmgmtReportHome");	
		List fraudreport_list = null;
		try{
			fraudreport_list = commondesc.getKeydesc(jdbctemplate);
				if(!(fraudreport_list .isEmpty())){
					trace("report_list " +fraudreport_list );
					setFraudreport_list(fraudreport_list);
					session.setAttribute("Errorstat", "S");
				}else{
					session.setAttribute("Errorstat", "E");
					session.setAttribute("ErrorMessage", "No Key Description Configured");			
			}
		}catch (Exception ex){
			trace("Error While Fetching "+ex);
			session.setAttribute("Errorstat", "E");
			session.setAttribute("ErrorMessage", "Error While Fetching the Data " +ex.getMessage());
		}		
		return "FraudmgmtReportHome";
	}
	
	private String reporttitle;	
	public String getReporttitle() {
		return reporttitle;
	}
	public void setReporttitle(String reporttitle) {
		this.reporttitle = reporttitle;
	}
	
	public String FraudmgmtGenReport(){
		trace(" FraudmgmtGenReport ");
		HttpSession session 		= 	getRequest().getSession();
	 
		String sucreturnstring="";
		String failreturnstring="";
		try {
			DateFormat dateFormat		= 	new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
			Date date					=	new Date();
			Properties prop 			= 	comUtil().getPropertyFile();	
			String Query_toProcess 	= 	"NO";
			String messges 			= 	"NO RECORD FOUND";
			String repor_tname 		= 	"DefaultReport.pdf";			
			String resultqry_cardno	;
			String resultqry_txtcode;
			String resultqry_referno;
			String resultqry_trandate;
			String resultqry_trantime;
			Object resultqry_amt;
			String where ="";
			String report_id =(getRequest().getParameter("fraudid"));
			String fromfrm_fromdate=(getRequest().getParameter("fromdate"));
			String fromfrm_todate=(getRequest().getParameter("todate"));
			String gentype=(getRequest().getParameter("gentype"));
			if(report_id.equals("AMTBASED")){
				String amt=(getRequest().getParameter("excamt"));
				where = " AND TRANSACTION_AMOUNT >"+amt;
			}
			trace("return type"+gentype);
			if(gentype.equals("view")){
				failreturnstring="no_data_period";
				sucreturnstring="FraudMgmt_reportview";
			}else{
				failreturnstring="required_home";
				sucreturnstring="FraudMgmt_report";
			}
			
			trace("succes url"+sucreturnstring);
			trace("fail url"+failreturnstring);
			trace("Report id ==> "+report_id+" Form Date ==> "+fromfrm_fromdate+" To Date ===> "+fromfrm_todate);
			String dynamic_qry ="SELECT CARD_NO,TXN_CODE,TXN_REF_NO,to_char(TRAN_DATE,'DD-MON-YY') as TRANDATE,to_char(TRAN_TIME) as TRAN_TIME,TRANSACTION_AMOUNT FROM EZMMS_TXNLOG WHERE trunc(TRAN_DATE) BETWEEN to_date('"+fromfrm_fromdate+"','DD-MM-YY') and to_date('"+fromfrm_todate+"','DD-MM-YY') AND FRAUD_REASON='"+report_id+"'"+where;
			trace("QUery To execute is "+dynamic_qry );			
			String titlename= getReportTitle(report_id,jdbctemplate);
			if(titlename.equals("X")){
				trace("Report Title Not Found");
			} if(!gentype.equals("view")){
				List queryResult = reportResult(dynamic_qry,jdbctemplate);
				trace("List Value ===== >  "+queryResult );
				if(!(queryResult.isEmpty())){
						Iterator itr_card_list	=	queryResult.iterator();
						while(itr_card_list.hasNext()) {			
			   				Map mapper_orderdetails		=	(Map)itr_card_list.next();
			   				resultqry_cardno			=	((String)mapper_orderdetails.get("CARD_NO"));
			   				resultqry_txtcode			=	((String)mapper_orderdetails.get("TXN_CODE"));
			   				resultqry_referno			=	((String)mapper_orderdetails.get("TXN_REF_NO"));
			   				resultqry_trandate			=	((String)mapper_orderdetails.get("TRANDATE"));
			   				resultqry_trantime			=	((String)mapper_orderdetails.get("TRAN_TIME"));
			   				resultqry_amt				=	((Object)mapper_orderdetails.get("TRANSACTION_AMOUNT"));		   				
			   				}				
							reportParams.put("reporttitle",titlename);
							String iconurl 		=	comUtil().GetUrlReportIcon(getRequest());
							trace("Report Icon Path "+ iconurl);
							reportParams.put("logo",iconurl);
							String reportwatermrk = prop.getProperty("IS0.FRAUDREPORTWATERMRK");		
							trace("Report water Mark"+ reportwatermrk);
							reportParams.put("watermrk",reportwatermrk);
							reportParams.put("fromdate",fromfrm_fromdate);
							trace("From Date "+ fromfrm_fromdate);
							reportParams.put("todate",fromfrm_todate);
							trace("From Date "+ fromfrm_todate);		
							String curdatetime			=	dateFormat.format(date);				
							repor_tname		=	repor_tname+curdatetime+".pdf";
							trace("Report Name is ===> "+repor_tname);
							setReportname(repor_tname);				
							//trace("Data List "+Fraud_source);				
					}else{
						trace("Error while generating Report " );					
						return failreturnstring;
					}
			 	}else{
					String keydesc = "SELECT DESCRIPTION FROM EZFMS_KEYDESC WHERE REASON_KEY='"+report_id+"'"; 
					String Listgetkeydesc=(String)jdbctemplate.queryForObject(keydesc, String.class);
					setReporttitle(Listgetkeydesc+" Report");
				    fraudreportlist = reportResult(dynamic_qry,jdbctemplate);				    
				    trace("view");
				}
		} catch (Exception e) {
			trace("Error while generating Report " + e);
			e.printStackTrace();
			trace("Error==>"+e.getMessage());
			return failreturnstring;
		}
		return sucreturnstring;
	}
	
	//cashScrollReportHome
	public String statisticReport(){
		trace("**************** CARD STATISTIC REPORT  begin ****************");
		enctrace("**************** CARD STATISTIC REPORT  begin ****************");
		trace("\n\n");
		enctrace("\n\n");
		return "cardStatisticReportHome";
	}
	
	public String cardScrollReport(){
		trace("**************** card scroll summary report begin ****************");
		enctrace("**************** card scroll summary report  begin ****************");
		trace("\n\n");
		enctrace("\n\n");
		return "cardscrollreporthome";
	}
	
	public String NFSReport(){
		trace("**************** nfs report begin ****************");
		enctrace("**************** nfs report begin ****************");
		trace("\n\n");
		enctrace("\n\n");
		return "nfsreport";
	}
	
	public String gencardStatisticReport(){
		trace("**************** card statistic report   ****************");
		enctrace("**************** card statistic report  ****************");
		HttpSession session = getRequest().getSession();
		try{
			int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;			
			 
			HttpServletRequest request= getRequest();
			Document document = new Document();
			output_stream = new ByteArrayOutputStream();
			String instid = comInstId(); 
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy h:mm a");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String username = (String)session.getAttribute("SS_USERNAME");			
			trace("User Name  ============="+username);
			String propertyname ="gencardStatisticReport";
			String fromdate= (getRequest().getParameter("fromdate"));
			String todate= (getRequest().getParameter("todate"));
			String chn= (getRequest().getParameter("chn"));
			trace(" User Inputs  : ");
			trace("  fromdate :"+fromdate);
			trace("  todate   :"+todate);
			trace(" CHN :"+ chn);
			String txncode_query = "SELECT TO_CHAR(TXN_CODE) as TXN_CODE ,TO_CHAR(ACTION_DESC) as ACTION_DESC FROM IFACTIONCODES WHERE INST_ID='"+instid+"'";
			enctrace(" txncode query ........ "+ txncode_query);
			List txncodelist	= jdbctemplate.queryForList(txncode_query);
			trace(" List Value"+ txncodelist.size());
			if(!txncodelist.isEmpty()){	
					report_name="cardStatisticReport.xls";
					String txndes_query = "SELECT TO_CHAR(CHN) as CHN,TO_CHAR(TRANDATE) as TRANDATE FROM IFP_TXN_LOG WHERE INST_ID='"+instid+"' AND CHN ='"+chn+"' AND TRUNC(TRANDATE) BETWEEN TO_DATE('"+fromdate+"','DD-MM-YY') AND TO_DATE('"+todate+"','DD-MM-YY') group by CHN,TRANDATE ";
					enctrace(" transaction statistic query.......... "+ txndes_query);
					List txndesclist	= jdbctemplate.queryForList(txndes_query);
					ListIterator itrdeslog = txndesclist.listIterator();	
					int i=0;
					String tableth="";
					while(itrdeslog.hasNext()){
						HashMap mapdes = (HashMap)itrdeslog.next();
						String strchn = (String)mapdes.get("CHN");
						String trandate = (String) mapdes.get("TRANDATE");
						int cunt=0;
						String txndes="";
						Iterator itrcodelog = txncodelist.iterator();
						int j=0;
						while(itrcodelog.hasNext()){							
							Map maptxncode = (Map)itrcodelog.next();
							String code = (String)maptxncode.get("TXN_CODE");
							String des = (String) maptxncode.get("ACTION_DESC");
							//trace(code+" *********************** "+des);
							String txnlog_query ="SELECT count(CHN) FROM IFP_TXN_LOG WHERE INST_ID='"+instid+"' AND TXNCODE='"+code+"' AND  CHN ='"+chn+"' AND TRUNC(TRANDATE) BETWEEN TO_DATE('"+fromdate+"','DD-MM-YY') AND TO_DATE('"+todate+"','DD-MM-YY')";
							enctrace("transaction count query ........"+txnlog_query );
							int txnloglist=jdbctemplate.queryForInt(txnlog_query);
							String trancount = Integer.valueOf(txnloglist).toString();
							mapdes.put(code, trancount);
							if(i==0){
								if(j==0)
									tableth=des;
								else
									tableth=tableth+","+des;
							}
							j++;
						}
						itrdeslog.remove();
						itrdeslog.add(mapdes);
						i++;
					}
				String Reportheader ="CHN,TRAN DATE,"+tableth;
				trace(" Table header names .......... "+tableth);
				trace("txndesclist size   ........... "+txndesclist.size());
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				//String reportitlestring = pdfgen.reporttitles+", From Date:"+fromdate+" & ToDate :"+todate+" Generated by : "+username+" , Date : " + strdate;
				String reportitlestring = pdfgen.reporttitles;
				String reportheader="From Date ,:,"+fromdate+ ", To Date ,:,"+todate+", Generated by,:,"+username+", Date,:,"+strdate;
				pdfgen.addPDFTitles(document,pdfgen.reporttitles, ALIGN_CENTER);	
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				pdfgen.createSimplePDF(document,reportitlestring,Reportheader,txndesclist,pdfgen.reportsumfield,ALIGN_CENTER, ALIGN_CENTER,100);
				pdfgen.closePDF(document);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush();
			}else{
				trace(" ERROR: EMPTY RECORD ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Error while execute the Report" );
				trace("\n\n");
				enctrace("\n\n");
				return "globalreporterror";
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: could not process to generate card statistic report ");
			trace("Exception: could not process to generate card statistic report " + e.getMessage());
			trace("\n\n");
			enctrace("\n\n");
			return "globalreporterror";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "itextpdfreport";
	}
	public String gencardScrollReport(){
		trace("**************** card scroll summary report ****************");
		enctrace("**************** card scroll summary report ****************");		
		HttpSession session = getRequest().getSession();
		try{
			int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;			
			 
			HttpServletRequest request= getRequest();
			Document document = new Document();
			output_stream = new ByteArrayOutputStream();
			String instid = comInstId(); 
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy:HH:mm");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String username = (String)session.getAttribute("SS_USERNAME");			
			trace("User Name  ============="+username);
			//String instname=((getRequest().getParameter("instname"))).trim();
			String propertyname ="gencardScrollReport";			
			String fromdate= (getRequest().getParameter("fromdate"));
			String reportname=" card scroll summary report "+strdate;
			String todate= (getRequest().getParameter("todate"));
			String chn= (getRequest().getParameter("chn"));
			trace(" User Inputs  : ");
			trace("  fromdate :"+fromdate);
			trace("  todate   :"+todate);
			trace(" CHN :"+ chn);
			String txnlog_query ="SELECT TO_CHAR(CHN) AS CHN,TO_CHAR( NVL(ACCOUNTNO, '--') ) AS ACCOUNTNO,TO_CHAR(TERMINALID) AS TERMINALID,TO_CHAR(TXNCODE) as TXNCODE,TO_CHAR(TRANDATE) AS TRANDATE,TO_CHAR(REFNUM) AS REFNUM,TO_CHAR(TRACENO) AS TRACENO,TO_CHAR(TRANTIME) AS TRANTIME ,TO_CHAR(TXNAMOUNT) AS TXNAMOUNT,TO_CHAR(TXNCURRENCY) AS TXNCURRENCY FROM IFP_TXN_LOG WHERE  CHN ='"+chn+"' AND TRUNC(TRANDATE) BETWEEN TO_DATE('"+fromdate+"','DD-MM-YY') AND TO_DATE('"+todate+"','DD-MM-YY') AND INST_ID='"+instid+"'";
			enctrace(" txnlog_query "+txnlog_query);
			List txnloglist=jdbctemplate.queryForList(txnlog_query);
			if((txnloglist.isEmpty()) || (txnloglist==null)){
				trace("List Is Empty or NULL");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " Error : Records not Found"); 
				return "globalreporterror";
			}else{
				setReportname(reportname);
				ListIterator itrtxnlog = txnloglist.listIterator();
				String txndatetime="";
				while(itrtxnlog.hasNext()){
					Map txnlogmap = (Map) itrtxnlog.next();
					String txncurrency = (String) txnlogmap.get("TXNCURRENCY");
					String txnamount = (String) txnlogmap.get("TXNAMOUNT");
					trace("Amount .............. "+ txnamount);
					String txndate  = (String)txnlogmap.get("TRANDATE");
					String time  = (String)txnlogmap.get("TRANTIME");
					String txncode = (String)txnlogmap.get("TXNCODE");
					DecimalFormat d= commondesc.currencyFormatter(txncurrency, jdbctemplate );
					BigDecimal txnamt = new BigDecimal(txnamount);					
					if(time.length()==6)
						txndatetime = txndate +"  "+time.substring(0, 2)+":"+time.substring(2,4)+":"+time.substring(4,6);
					else
						txndatetime = txndate +"  "+time.substring(0, 1)+":"+time.substring(1,3)+":"+time.substring(3,5);					
					String actiondesc = commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
					trace(" transaction list........... "+actiondesc);
					txnlogmap.put("TXNCODE", actiondesc);
					txnlogmap.put("TRANDATE", txndatetime);
					txnlogmap.put("TXNAMOUNT",d.format(txnamt));
					txnlogmap.remove("TXNCURRENCY");
					txnlogmap.remove("TRANTIME");
					itrtxnlog.remove();
					itrtxnlog.add(txnlogmap);
				}
				trace(" Values in List size..........."+ txnloglist.size());
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				//String reportheaderstring = pdfgen.reporttitles+", From Date:"+fromdate+" & ToDate :"+todate+" Generated by : "+username+" ,CHN : "+chn+"  Date : " + strdate;
				String reportheaderstring = pdfgen.reporttitles;
				String reportheader="From Date ,:,"+fromdate+ ", To Date ,:,"+todate+", Generated by,:,"+username+", Date,:,"+strdate+", CHN,:,"+chn;
				pdfgen.addPDFTitles(document,pdfgen.reporttitles, ALIGN_CENTER);	
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				pdfgen.createSimplePDF(document,reportheaderstring, pdfgen.reportheader, txnloglist,pdfgen.reportsumfield,ALIGN_CENTER, ALIGN_CENTER,100);
				//report_name=pdfgen.filename+".pdf";
				pdfgen.closePDF(document);
				setReportname(pdfgen.filename+strdate+".pdf");
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush();
			}
			
		}catch(Exception e){
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: could not generate card scroll summary report");
			trace("Exception: could not generate card scroll summary report"+e.getMessage());	
			trace("\n\n");
			enctrace("\n\n");
			return "globalreporterror";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "itextpdfreport";
	}
	public String genNFSReport(){
		trace("**************** nfs report  ****************");
		enctrace("**************** nfs report  ****************");
		int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
		trace("NFs Report PDF Called ");
		HttpSession session 		= 	getRequest().getSession();
		 
		try{
			HttpServletRequest request= getRequest();
			Document document = new Document();
			output_stream = new ByteArrayOutputStream();
			String instid = comInstId(); 
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy h:mm a");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String username = (String)session.getAttribute("SS_USERNAME");			
			trace("User Name  ============="+username);
			//String instname=((getRequest().getParameter("instname"))).trim();
			String propertyname ="gencardScrollReport";
			
			Calendar calendar = new GregorianCalendar();
			String i_Name	  =(String)session.getAttribute("Instname");
			String splitdate[];
			int frommonth=0,tomonth=11;
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String selectcrit=request.getParameter("nfsrad");
			String getfieldval="",aquirerinst="",issuerinst="";
			int ann= Integer.valueOf(request.getParameter("annualy"));
			String txnnetwork= request.getParameter("network");	
			
			if(selectcrit.equals("quartely"))
				getfieldval= request.getParameter("quartely");
			if(selectcrit.equals("halfyearly"))
				getfieldval= request.getParameter("halfyearly");
			
			if(!("".equals(getfieldval))){
				splitdate= getfieldval.split("-");			
				frommonth = Integer.valueOf(splitdate[0]);
				tomonth = Integer.valueOf(splitdate[1]);
			}
			
			calendar.set(Calendar.YEAR, ann);
			calendar.set(Calendar.MONTH, frommonth); 			
			int minday = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DATE,minday);			
			String formattedfromdate = df.format(calendar.getTime());
			
			calendar.set(Calendar.YEAR, ann);
			calendar.set(Calendar.MONTH, tomonth); 
			int maxDay1 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);		
			calendar.set(Calendar.DATE,maxDay1 );
			String formattedtodate = df.format(calendar.getTime());
			
			trace(" formattedfromdate........... got "+formattedfromdate);
			trace(" formattedtodate......... got  "+formattedtodate);
			
			if(txnnetwork.equals("NFS")){				
				String inst[]= (request.getParameter("instid")).split("-");
				aquirerinst=inst[0];
				issuerinst=inst[1];
			}else{
				aquirerinst=txnnetwork;
				issuerinst=txnnetwork;
			}
			String reportheader="From Date ,:,"+formattedfromdate+ ",To Date ,:,"+formattedtodate+", Aquirer Inst,:,"+aquirerinst+", Issuer Inst,:,"+issuerinst;
			String reporttitles = " NFS Report";
			trace("aquirerinst ............... "+aquirerinst);
			trace("issuerinst ................ "+issuerinst);
			String Action_query ="select ACTION_CODE,ACTION_DESC from ifactioncodes where INST_ID='"+i_Name+"' and ACTION_STATUS=1";
			enctrace("transaction action query  "+ Action_query );
			List action_list =  jdbctemplate.queryForList(Action_query);
			trace(" action_list  size ............. "+ action_list.size());
			
			PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertyname, request);
			pdfgen.addPDFTitles(document, reporttitles, ALIGN_CENTER);
			pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
			String reportheading="APPROVED TRANSACTION DESCRIPTION,TOTAL TXNS";
			if(!action_list.isEmpty()){
				ListIterator itr_action = action_list.listIterator();
				trace(" size "+action_list.size());
			    pdfgen.addPDFHeading(document, reportheading,ALIGN_CENTER,100);
			    int i=0,totalcnt=0;
				while(itr_action.hasNext()){
					HashMap action_map = (HashMap) itr_action.next();
					String result_actioncode = (String) action_map.get("ACTION_CODE");
					String result_actiondes = (String) action_map.get("ACTION_DESC");
					String txncode = commondesc.getTxcCodeByAction(i_Name,result_actioncode,jdbctemplate);
					action_map.put("ACTION_DESC", result_actiondes);
					if(!txncode.equals("NOREC")){
						String transcnt ="SELECT count(TXNCODE)as txncnt FROM IFP_TXN_LOG WHERE TRUNC(TRANDATE) BETWEEN TO_DATE('"+formattedfromdate+"','DD-MM-YY') AND TO_DATE('"+formattedtodate+"','DD-MM-YY') AND issuerid='"+issuerinst+"' and acquirerid='"+aquirerinst+"' and respcode=0 AND TXNCODE='"+txncode+"'";
						enctrace(" transaction counnt query "+ transcnt);
						int result_cnt = jdbctemplate.queryForInt(transcnt);
						action_map.put("totalcnt", Integer.valueOf(result_cnt).toString());
						action_map.remove("ACTION_CODE");
						itr_action.remove();
						totalcnt+=result_cnt;
						itr_action.add(action_map);
					}
				}
			    Map total	= 	new LinkedHashMap();
				total.put("ACTION_DESC","Approved Txns Total");
				total.put("totalcnt",Integer.valueOf(totalcnt).toString());
				itr_action.add(total);
				//action_map.put("ACTION_CODE", Integer.valueOf(result_cnt).toString());					
			}else {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " Record Not Found ");
				trace(" Record Not Found ");
				return "globalreporterror";				
			}
			pdfgen.addPDFData(document, pdfgen.reportsumfield,reportheading,action_list);
			pdfgen.closePDF(document);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			output_stream.flush();			
		}
		catch(Exception e){
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " could not process to generate nfs report");
			trace(" could not process to generate nfs report" + e.getMessage());
			return "globalreporterror";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "itextpdfreport";
	}
	public void  dynamicReport2(){
		trace("called dynamic report" );
		HttpSession session 		= 	getRequest().getSession();
				
		try {
			String dynamic_qry ="SELECT TXN_REF_NO as REFERENCENO,CARD_NO as CARDNUMBER,TXN_CODE as TXNCODE ,MERCH_ID as MERCHID ,STORE_ID as STOREID  FROM EZMMS_TXNLOG";
			trace("QUery To execute is "+dynamic_qry );			
			List dynamicqueryResult = reportResult(dynamic_qry ,jdbctemplate);
			trace("List Value ===== >  "+dynamicqueryResult );
			if(!(dynamicqueryResult.isEmpty())){
				
				trace("Value Is There");
					Iterator itr = dynamicqueryResult.iterator();
					Iterator parameterkey=null;
					trace("Iterator ==> "+itr);
					int loop = 0;
					int i=0,column=0;
					
					while(itr.hasNext()){
						Map map = (Map)itr.next();						
						Iterator keyItr = map.keySet().iterator();
						if(loop==0){
							parameterkey = map.keySet().iterator();
						}
						List fieldvalues = new ArrayList();
						while(keyItr.hasNext()){
							String key = (String)keyItr.next();
							String value=(String)map.get(key);							
							trace("values 		"+value);
							fieldvalues.add(value);
						}						
						trace("Field values ###################################  "+fieldvalues);
						String fieldasstring = StringUtils.collectionToDelimitedString(fieldvalues, "#$");
						//f1[] = fieldasstring.split("#$");
						trace("Field sring ###################################  "+fieldasstring);
						String passval ="String f1[],String f2,String f3,String f4, String f5,String f6,String f7";
						
						Class cls = Class.forName("model.CardReportModel");
						Object obj = cls.newInstance();
						Class[] paramString = new Class[1];
						paramString[0] =String.class;						
						Method method = cls.getDeclaredMethod("setResultqry_txtcode", paramString);
						method.invoke(obj, new String("mkyong"));													
						loop = loop + 1;
					}					
					trace("parameterkey =====>    "+parameterkey);
					int j=1;
					while(parameterkey.hasNext()){
						String key = (String) parameterkey.next();
						trace("reportParams.put(P"+j+","+key+");");
						j++;
					}			
			}
		} catch (Exception e) {
			trace("Error==>"+e.getMessage());
		}
		//return "gendynamicreport";
	}
	
	public String customerstmtReport(){
		trace("**************** customer statement report begin ****************");
		enctrace("**************** customer statement report begin ****************");
		trace("\n\n");
		enctrace("\n\n");
		return "customerstmthome";
	}
	
	public String gencustomerstmtReport(){
		trace("**************** customer statement report ****************");
		enctrace("**************** customer statement report ****************");
		HttpSession session = getRequest().getSession();		 
		try{
			String reportname="statement_"+commondesc.getDateTimeStamp()+".pdf";
			int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;			
			
			HttpServletRequest request= getRequest();
			Document document = new Document();
			output_stream = new ByteArrayOutputStream();
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String username = (String)session.getAttribute("SS_USERNAME");			
			trace("User Name  ============="+username);
			
			String propertyname ="gencustomerstmtReport";
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");			
			String chn = getRequest().getParameter("chn");
			String query_str= "SELECT TO_CHAR(INST_ID) as INST_ID  , TO_CHAR(TXNDESC) as TXNDESC, TO_CHAR(TRANDATE) as TRANDATE, TO_CHAR(OPTYPE) as OPTYPE , TO_CHAR(NVL (  DECODE(OPTYPE, 'DR', TXNAMOUNT), '-' )) AS WITHDRAW, TO_CHAR(NVL ( DECODE(OPTYPE, 'CR', TXNAMOUNT), '-' )) AS DEPOSIT , TO_CHAR(SUM (DECODE(OPTYPE, 'CR', TXNAMOUNT, -1 * TXNAMOUNT)) OVER (ORDER BY CHN ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)) AS BALANCE FROM IFP_PL_TXN WHERE CHN='"+chn+"' AND  TO_DATE('"+fromdate+"','dd-MM-yyyy') <=TRANDATE AND  TO_DATE('"+todate+"','dd-MM-yyyy')+1 >TRANDATE  GROUP BY CHN, OPTYPE, TXNAMOUNT, INST_ID,TXNDESC, TRANDATE ";
			enctrace(" customer statement report query : "+query_str);
			List listqry = jdbctemplate.queryForList(query_str);
			trace(" List value "+listqry  );
			

			if(!(listqry.isEmpty())){
			//ListIterator itrqry = listqry.listIterator();
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				String reporttitles= pdfgen.reporttitles;
				//String reportheader= pdfgen.reporttitles+", From Date : "+fromdate+" To Date : "+ todate+" CHN : "+ chn;
				//reporttitles+= ", Date : "+strdate+" Generated By  : "+ username;
				setReportname(reportname);
				String reportheader="From Date ,:,"+fromdate+ ",To Date ,:,"+todate+", CHN,:,"+chn;
				pdfgen.addPDFTitles(document, reporttitles, ALIGN_CENTER);
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				pdfgen.createSimplePDF(document, reporttitles, pdfgen.reportheader, listqry, pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);
				pdfgen.closePDF(document);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			}
			else{
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "ERROR : Record Not Found ");
				trace("ERROR : Record Not Found ");
				trace("\n\n");
				enctrace("\n\n");
				return "globalreporterror";
			}
			output_stream.flush();

		}
		catch(Exception e){			
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: could not process to generate  customer statement report ");
			trace( "Exception: could not process to generate  customer statement report " + e.getMessage());
			e.printStackTrace();
			trace("\n\n");
			enctrace("\n\n");
			return "globalreporterror";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "itextpdfreport";
	}
	public String generatetransactionstatus()
	 {		 
			trace("***************************Generate Transaction Status Begins***************************");
			enctrace("***************************Generate Transaction Status Begins***************************");
		 	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
			HttpSession session 		= 	getRequest().getSession();
				
			String searchby ="",searchval="";
			try
			{
				Document document = new Document();
				HttpServletRequest request= getRequest();
				output_stream = new ByteArrayOutputStream();
				SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
				Date cur_date = new Date();
				String strdate =date_format.format(cur_date).toString();
				String username = (String)session.getAttribute("SS_USERNAME");			
				trace("User Name  ============="+username);
				String propertyname ="generatetransactionstatus";
				String chnum="";
				String criteria = getRequest().getParameter("radname");
				trace(criteria);
				String fromdate = getRequest().getParameter("fromdate");
				String todate = getRequest().getParameter("todate");
				String instid =  comInstId();
				List chnrad=null;
				trace(instid);
				String transcode,respcode,msgtype,traceno,authnum;
				report_name="TransactionStatusReport.pdf";
				String criteriatype="";
				String criteriavalue = "",dynamicquery="";
				 
				if(criteria.equals("accrad"))
				{ 
				  searchby= " ACCOUNT NO ";
				  searchval = getRequest().getParameter("acc");
				  dynamicquery = " ACCOUNTNO='"+searchval+"'";
				}
				else{
				  searchby  = " CHN ";
				  searchval = getRequest().getParameter("chn");
				  dynamicquery = " CHN='"+searchval+"'";
				}
					 String accradquery = "SELECT TO_CHAR(TRANDATE) AS TRANDATE,TO_CHAR(TERMINALID) AS TERMINALID,TO_CHAR(TRACENO) AS TRACENO,TO_CHAR(ACCOUNTNO) AS ACCOUNTNO,TO_CHAR(CHN) AS CHN,TO_CHAR(MSGTYPE) AS MSGTYPE , TO_CHAR(TXNCODE) AS TXNCODE, TO_CHAR(RESPCODE) as RESPCODE FROM IFP_TXN_LOG where "+dynamicquery+" and INST_ID='"+instid+"' AND trunc(TRANDATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
					 enctrace(" query for status report " + accradquery);
					 chnrad = jdbctemplate.queryForList(accradquery);
					 trace("acquad   "+chnrad);
					 if(!chnrad.isEmpty())
					 { 
						 ListIterator itr_card_list	=	chnrad.listIterator();
						 while(itr_card_list.hasNext())
						 {
							 String success="0",fullreversal="0",suspectreversal="0",failure="0",partialreversal="0";
							 Map map = (Map)itr_card_list.next(); 
							 transcode = (String) map.get("TXNCODE");
							 respcode = (String) map.get("RESPCODE");
							 trace("**************"+respcode+"**************");
							 traceno = (String) map.get("TRACENO");
							 trace("**************"+traceno+"**************");
							 msgtype = (String) map.get("MSGTYPE");
							 trace("**************"+msgtype+"**************");
							 trace("transcode   ============> "+transcode);
							 String startletter = transcode.substring(0, 1);
							 trace("startletter================ > "+ startletter);
							 if(respcode.equals("103") && msgtype.equals("420") && startletter.equals("1"))
							 {
								 fullreversal = "1";
							 }
							 else if(respcode.equals("112") && msgtype.equals("420") && startletter.equals("1"))
							 {
								suspectreversal = "1";
							 }
							 else if(respcode.equals("0") && msgtype.equals("210"))
							 {
								 success = "1";
							 }
							 else if((!respcode.equals("0")) && msgtype.equals("210"))
							 {
								failure = "1";
							 }
							 else if(respcode.equals("102") && msgtype.equals("420") && startletter.equals("1"))
							 {
								partialreversal = "1";
							 }							
								 map.put("FULLREVERSAL", fullreversal);
								 map.put("SUSPECTREVERSAL", suspectreversal);
								 map.put("SUCCESS", success);
								 map.put("FAILURE", failure);
								 map.put("PARTIALREVERSAL", partialreversal);
								 itr_card_list.remove();
								 itr_card_list.add(map);
						 }
					 }
					 else
					 {
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", " List is Empty"); 
							trace("/n/n");
							enctrace("/n/n");
							return "globalreporterror";
					 } 
				 //From Date,To Date,Generated Date,Generated By
				 PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				 //String title = pdfgen.reporttitles+", From Date :"+fromdate+" To Date :"+todate/*+" Generated By :"+username*/;
				 //title = title +"," +searchby+ " :"+searchval+" Date :"+strdate;	
				 String reportheader="From Date ,:,"+fromdate+ ",To Date ,:,"+todate+", Generated By,:,"+username;
				pdfgen.addPDFTitles(document,pdfgen.reporttitles, ALIGN_CENTER);	
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);String title = pdfgen.reporttitles;
				 //Table with records
				 pdfgen.createSimplePDF(document,title, pdfgen.reportheader,chnrad,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);
				 setReportname(pdfgen.filename+strdate+".pdf");
				 pdfgen.closePDF(document);
				 input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				 output_stream.flush();
				 trace("/n/n");
				 enctrace("/n/n");
			}
			catch (Exception e) 
			{
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Error while generate the PDF Report"); 
				trace("Exception :Error while generate the PDF Report "+e.getMessage());
				e.printStackTrace();
				trace("/n/n");
				enctrace("/n/n");
				return "globalreporterror";
			}
			trace("/n/n");
			enctrace("/n/n");
		   return "itextpdfreport";
	 }
	 public String getReportTitle(String report_id,JdbcTemplate jdbctemplate){
		 	String rept_name = "X";
			String keydesc = "SELECT DESCRIPTION FROM EZFMS_KEYDESC WHERE REASON_KEY='"+report_id+"'";
			rept_name = (String)jdbctemplate.queryForObject(keydesc, String.class);
			return rept_name;
	 }
	 
	 
	 public String glViewReport(){
		 return "glview_list";
	 }
	
	 List glrecordlist;
	 
	 public List getGlrecordlist() {
		return glrecordlist;
	}
	public void setGlrecordlist(List glrecordlist) {
		this.glrecordlist = glrecordlist;
	}
	public String glViewReportAction(){
		 
		 trace("*******glViewReportAction******");enctrace("*******glViewReportAction******");
		 HttpSession session 		= 	getRequest().getSession();
		 
		 String instid = comInstId();
		 ReportgenerationActionDAO reportdao = new ReportgenerationActionDAO();
		 try{
			 List glgrplist = reportdao.getGlGroup(instid, jdbctemplate);
			 if( glgrplist.isEmpty() ){
				 trace("No gl group configured....");
				 session.setAttribute("preverr", "E");
				 session.setAttribute("prevmsg", "No gl records found...");
			 }
			 ListIterator glgrpitr = glgrplist.listIterator();
			 while( glgrpitr.hasNext() ){
				 Map glgrpmp = (Map)glgrpitr.next();
				 String grpcode = (String)glgrpmp.get("GROUP_CODE");
				
				 List gllist = reportdao.getList(instid, grpcode, jdbctemplate);
				 if( gllist==null || gllist.isEmpty() ){
					 trace("No gl configured....");
					 session.setAttribute("preverr", "E");
					 session.setAttribute("prevmsg", "No gl records found...");
					 return glViewReport();
				 } 
				 
				ListIterator glitr = gllist.listIterator();
				 while( glitr.hasNext() ){
					 Map glmp = (Map)glitr.next();
					 String glcode = (String)glmp.get("GL_CODE");
					 String glbaltype = reportdao.getGloableDescription((String)glmp.get("GL_BAL_TYPE"), jdbctemplate); 
					 glmp.put("GL_BAL_TYPEDESC", glbaltype);
					 
					 String aliedesc = reportdao.getGloableDescription((String)glmp.get("GL_ALIE"), jdbctemplate); 
					 trace("aliedesc : " + aliedesc );
					 glmp.put("GL_ALIEDESC", aliedesc);
					 
					 String glposdesc = reportdao.getGloableDescription((String)glmp.get("GL_POSITION"), jdbctemplate); 
					 glmp.put("GL_POSITIONDESC", glposdesc);
					 
					  
					 
					 String glstmttypedesc =reportdao.getGloableDescription((String)glmp.get("GL_STATEMENT_TYPE"), jdbctemplate);
					 glmp.put("GL_STATEMENT_TYPEDESC", glstmttypedesc);
					 
					 String curdesc =commondesc.getCurDesc((String)glmp.get("CUR_CODE"), jdbctemplate);
					 glmp.put("CUR_CODEDESC", curdesc);
					 
					 List sugllist = reportdao.getSubList(instid, glcode, jdbctemplate);
					 if( sugllist.isEmpty() ){
						 trace("No sub gl configured....");
						 session.setAttribute("preverr", "E");
						 session.setAttribute("prevmsg", "No sub  gl records found...");
						 return glViewReport();
					 }
					 
					 glmp.put("SUBGLLIST", sugllist);
					 glitr.remove();
					 glitr.add(glmp);
				 } 
				 glgrpmp.put("GLLIST", gllist);
				 setGlrecordlist(glgrplist);
				  
			 }
			 
		 }catch(Exception e){
			 trace("Exception : Could not continue...."+e.getMessage());
			 session.setAttribute("preverr", "E");
			 session.setAttribute("prevmsg", "Exception : Could not continue....");
		 }
	 
		 trace("\n\n");enctrace("\n\n");
		 return "glview_report";
		  	
	 }
	
	public String merchantdizereport(){
		trace("*******merchantdize report Begins******");
		enctrace("*******merchantdize report Begins******");
		trace("\n\n");enctrace("\n\n");
		 return "merchantdizereport";
	 }
	
	public String getMerchantCard(String instid, String merchantid, JdbcTemplate jdbctemplate ) throws Exception {
		String merchantcard = null;
		try {
			String merchatcardqry = "SELECT CARDNO FROM IFP_CARD_ENTITY_MAP WHERE INST_ID='"+instid+"' AND ENTITY_TYPE='$MC' AND ENTITY_ID='"+merchantid+"'";
			merchantcard = (String)jdbctemplate.queryForObject(merchatcardqry, String.class);
		} catch (EmptyResultDataAccessException e) {}
		return merchantcard;
	}
	public String reportMerchandize(){
		trace("*******merchantdize report Next Begins******");
		enctrace("*******merchantdize report Next Begins******");
	 	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
		HttpSession session=getRequest().getSession();
		
		
		try{
			String instid = comInstId(session); 
			String merchid = getRequest().getParameter("merchid");
			
			 String mercahntcardno = this.getMerchantCard(instid, merchid, jdbctemplate);
			if( mercahntcardno == null ){
				session.setAttribute("curmsg", "Mercahnt not linked with Entity-Mapping....");
				session.setAttribute("curerr", "E");
				return "globalreporterror";
			} 
			 
			Document document = new Document();
			HttpServletRequest request= getRequest();
			output_stream = new ByteArrayOutputStream();
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String propertyname ="merchandizerprt";
			String reportname=merchid+".pdf";
			setReportgendownloadfilename(reportname);
			 
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			trace("Fromdate :"+todate +" Todate : " + todate );
			
			trace(merchid);
			String mechndizequrty = "SELECT TO_CHAR(CHN) AS CHN,TO_CHAR(TXNDESC) AS TXNDESC,TO_CHAR(TRANDATE,'DD-MON-YY') as TRANDATE,TO_CHAR(TRANTIME) AS TRANTIME,TO_CHAR(TERMINALID) AS TERMINALID,TO_CHAR(REFNUM) AS REFNUM, TO_CHAR(TXNTYPE) AS TXNTYPE,TO_CHAR(AMOUNT) AS AMOUNT,TO_CHAR(SETTCURRENCY) AS SETTCURRENCY FROM IFP_TRANSACTION_MASTER WHERE INST_ID='"+instid+"' AND ACCEPTORID='"+merchid+"' AND TRANDATE BETWEEN  TO_DATE('"+fromdate+"','DD-MM-YYYY') and TO_DATE('"+todate+"','DD-MM-YYYY') ORDER BY TXNMASTERSEQNO DESC";
			enctrace(" qurty Query "+mechndizequrty);
			List mechndizequrty_result = jdbctemplate.queryForList(mechndizequrty);
			trace("List mechndizequrty_result-->"+mechndizequrty_result);
			
			if(!mechndizequrty_result.isEmpty()){
				trace("NOT EMPTY");
				
				//From Date,To Date,Generated Date
				
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				String title = pdfgen.reporttitles;
				pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
				String reportheader="Merchant Id ,:,"+merchid+",CardNumber ,:,"+mercahntcardno+",From Date ,:,"+fromdate+",To Date ,:,"+todate+",Generated Date ,:,"+strdate;
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				
				 DecimalFormat d=new DecimalFormat();
				 ListIterator itrval = mechndizequrty_result.listIterator();
				 BigDecimal summarytotal = new BigDecimal("0");
				 BigDecimal bigtransamt;					
				 while(itrval.hasNext()){
					Map mapval = (Map) itrval.next();
					String txntype = (String)mapval.get("TXNTYPE");
					bigtransamt    = new BigDecimal((String)mapval.get("AMOUNT"));
					String setcurrency    = (String)mapval.get("SETTCURRENCY");
					if(txntype.equals("DR")) // if DR minus  TXN amount
					{
						summarytotal = summarytotal.subtract(bigtransamt);							
					} else 
					{															
						summarytotal = summarytotal.add(bigtransamt);		
					}
					d= commondesc.currencyFormatter(setcurrency,jdbctemplate);
					mapval.remove("SETTCURRENCY");
					itrval.remove();
					itrval.add(mapval);
				}
				 //Table with records
				 pdfgen.createSimplePDF(document,title, pdfgen.reportheader,mechndizequrty_result,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);
				// pdfgen.addPDFTitles(document,"TOTAL TXN AMOUNT : "+d.format(summarytotal), ALIGN_RIGHT);
				 //To generate summary
				 //List summlist= new ArrayList();
				 //Map summarymap = new LinkedHashMap();
				 //summarymap.put("Amount",pdfgen.getTotalAmtbyKey("AMOUNT"));
				 //summlist.remove(summarymap);
				 //summlist.add(summarymap);
				 //trace("List values of summlist == "+summlist);
				 //pdfgen.addPDFSummary(document, "AMOUNT","TOTAL TRANSACTION AMOUNT",summlist , "Summary",50);
				 
				 pdfgen.closePDF(document);
				 
				 input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				 output_stream.flush();		
			}
			else{
				session.setAttribute("curmsg", "No Details Found ");
				session.setAttribute("curerr", "E");
				return "globalreporterror";
			}
		}
		catch(Exception e){
			trace("Exception : ERROR :" +e.getMessage());
			e.printStackTrace();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error : "+e.getMessage());
		}
		trace("\n\n");enctrace("\n\n");
		return "itextpdfreport2";
 }
	public String txnFailerHome()
	{
		String instid 		=  comInstId();  			
		HttpSession session = getRequest().getSession();  
						
		try {					
				prodlist =  commondesc.getListOfBins(instid, jdbctemplate) ;
				trace(" prodlist====> "+prodlist);
				setProdlist(prodlist);
				session.setAttribute("curerr", "S"); 
		} catch (Exception e) {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Error while execute the " + e.getMessage()); 
		}	
		return "txnfailerhome";
	}
	public String gentxnFailerHome(){
		trace("*******txn failuer  report Next Begins******");
		enctrace("*******txn failuer  report Next Begins******");
	 	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
		HttpSession session=getRequest().getSession();
		 
		try{
			 
			Document document = new Document();
			HttpServletRequest request= getRequest();
			output_stream = new ByteArrayOutputStream();
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String propertyname ="TransactionFailreport";
			String reportname="Txn_Failure_"+strdate+".pdf";			
			String instid = comInstId(); 
			String bin = getRequest().getParameter("bin");
			String bincond = "";
			if( !bin.equals("ALL")){
				int binlen = commondesc.getBinLen(instid, bin, jdbctemplate);
				bincond = " AND SUBSTR(CHN, 1,"+binlen+")='"+bin+"'";
			}
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");	 
		 		
			String txnfailqurty ="SELECT TO_CHAR(CHN) as CHN ,TO_CHAR(TRANDATE) as TRANDATE,TO_CHAR(TRANTIME) as TRANTIME,TO_CHAR(TXNDESC) as TXNDESC, REFNUM, TO_CHAR(RESPCODE) as RESPCODE ,TO_CHAR(REASONCODE) AS REASONCODE, TO_CHAR(TXNAMOUNT) as TXNAMOUNT  FROM IFP_TXN_LOG WHERE INST_ID='"+instid+"'  "+bincond+"  AND MSGTYPE='210' AND RESPCODE != 0 AND TRANDATE BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') ORDER BY TXNLOGSEQNO DESC";
			enctrace(" qurty Query "+txnfailqurty);
			List txnfailurequrty_result = jdbctemplate.queryForList(txnfailqurty);
			trace(" txnfailurequrty_result-->"+txnfailurequrty_result.size());
			
			if(!txnfailurequrty_result.isEmpty()){
				trace("NOT EMPTY");
				
				//From Date,To Date,Generated Date
				
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				String title = pdfgen.reporttitles;
				pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
				String productdes = commondesc.getBinDesc(instid,bin, jdbctemplate);
				String reportheader="BIN ,:,"+productdes+ ",From Date  ,:,"+fromdate+",To Date,:,"+todate;
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				
				 DecimalFormat d=new DecimalFormat();
				 ListIterator itrval = txnfailurequrty_result.listIterator();
				 BigDecimal summarytotal = new BigDecimal("0");
				// BigDecimal bigtransamt;
				 
				while(itrval.hasNext()){
					Map mapval = (Map) itrval.next();
					String txntype = (String)mapval.get("TXNTYPE");
					//bigtransamt    = new BigDecimal((String)mapval.get("TXNAMOUNT"));			
					String bigtransamt    = (String)mapval.get("TXNAMOUNT");		
					String curcode = (String)mapval.get("TXNCURRENCY");
					String rescode = (String)mapval.get("RESPCODE");
					String reasoncode = (String)mapval.get("REASONCODE");
					//String currency=commondesc.getCurDesc(curcode, jdbctemplate);
					
					//d= commondesc.currencyFormatter(curcode,jdbctemplate);
					/*if(txntype.equals("DR")) // if DR minus  TXN amount
					{
						summarytotal = summarytotal.subtract(bigtransamt);							
					} else 
					{															
						summarytotal = summarytotal.add(bigtransamt);		
					}	*/	
					String resdescr;
					try {
						String resdes="SELECT TO_CHAR(DESCRIPTION) as DESCRIPTION  FROM IFP_RESPCODE WHERE RESPCODE='"+rescode+"'";
						trace("resdes :" + resdes);
						resdescr = (String)jdbctemplate.queryForObject(resdes, String.class);
					} catch (EmptyResultDataAccessException e) { 
						trace("Could not get the description for the resp code [ "+rescode+" ] ");
						resdescr = "Undefined [ "+rescode+" ]";
					}
					
					String reasondesc;
					try {
						String reasoncodeqry="SELECT DECODE(REASON_DESC, 'Error', '--', REASON_DESC ) as REASON_DESC  FROM IFP_REASONCODE WHERE REASON_CODE='"+reasoncode+"'";
						trace("reasoncodeqry :" + reasoncodeqry);
						reasondesc = (String)jdbctemplate.queryForObject(reasoncodeqry, String.class);
					} catch (EmptyResultDataAccessException e) { 
						trace("Could not get the description for the resp code [ "+rescode+" ] ");
						reasondesc = "Undefined [ "+rescode+" ]";
					}
					
					
					mapval.put("RESPCODE",resdescr);
					mapval.put("REASONCODE",reasondesc);
					//mapval.put("TXNCURRENCY", currency);		
					mapval.put("TXNAMOUNT", bigtransamt );
					itrval.remove();
					itrval.add(mapval);
				}
				 //Table with records
				 pdfgen.createSimplePDF(document,title, pdfgen.reportheader,txnfailurequrty_result,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);				
				 //To generate summary
				 //List summlist= new ArrayList();
				 //Map summarymap = new LinkedHashMap();
				 //summarymap.put("Amount",pdfgen.getTotalAmtbyKey("AMOUNT"));
				 //summlist.remove(summarymap);
				 //summlist.add(summarymap);
				 //trace("List values of summlist == "+summlist);
				 //pdfgen.addPDFSummary(document, "AMOUNT","TOTAL TRANSACTION AMOUNT",summlist , "Summary",50);
				 setReportgendownloadfilename(reportname);
				 pdfgen.closePDF(document);				 
				// setReportname(reportname);
				 input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				 output_stream.flush();		
			}
			else{
				session.setAttribute("curmsg", "No Details Found ");
				session.setAttribute("curerr", "E");
				return "globalreporterror";
			}
		}
		catch(Exception e){
			trace("Exception : ERROR :" +e.getMessage());
			e.printStackTrace();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error : "+e.getMessage());
		}
		trace("\n\n");enctrace("\n\n");
		return "itextpdfreport2";
 }
	
	public String cardMaintainStatus()
	{		 
			 return "cardmaintainstatus";
	}
	
	public String gencardMaintainStatus(){
		trace("*******txn failuer  report Next Begins******");
		enctrace("*******txn failuer  report Next Begins******");
	 	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
		HttpSession session=getRequest().getSession();
		
		try{
			
			Document document = new Document();
			HttpServletRequest request= getRequest();
			output_stream = new ByteArrayOutputStream();
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String propertyname ="cardstatusreport";
			String reportname="cardstatusreport.pdf"+strdate;			
			String instid = comInstId();			
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String cat = getRequest().getParameter("radname");
			String dynamic ="";
			String dynamicheader ="";
			if(cat.equals("bin")){
				String bin = getRequest().getParameter("bin");
				dynamic=" AND BIN ='"+bin+"'";
				dynamicheader="BIN ,:,"+bin+ "";
			}else{
				String chn = getRequest().getParameter("chn");
				dynamic=" AND CARD_NO ='"+chn+"'";
				dynamicheader="CARD NO ,:,"+chn+ "";
			}
				
			String txnfailqurty =" SELECT TO_CHAR(CARD_NO) as CARD_NO, TO_CHAR(PREV_STATUS) as PREV_STATUS, TO_CHAR(CHANGED_STATUS) as CHANGED_STATUS, TO_CHAR(ACTION_DATE) as ACTION_DATE, TO_CHAR(CHANGED_BY) as CHANGED_BY  FROM IFP_MAINTAIN_HISTORY WHERE ACTION_DATE BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"+dynamic+"ORDER BY  ACTION_DATE ASC";
			enctrace(" qurty Query "+txnfailqurty);
			List txnfailurequrty_result = jdbctemplate.queryForList(txnfailqurty);
			trace(" txnfailurequrty_result-->"+txnfailurequrty_result.size());
			
			if(!txnfailurequrty_result.isEmpty()){
				trace("NOT EMPTY");
				
				//From Date,To Date,Generated Date
				
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				String title = pdfgen.reporttitles;
				pdfgen.addPDFTitles(document, title, ALIGN_CENTER);				
				String reportheader=dynamicheader+ ",From Date  ,:,"+fromdate+",To Date,:,"+todate;
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				
				 DecimalFormat d=new DecimalFormat();
				 ListIterator itrval = txnfailurequrty_result.listIterator();
				 BigDecimal summarytotal = new BigDecimal("0");
				 BigDecimal bigtransamt;					
				while(itrval.hasNext()){
					Map mapval = (Map) itrval.next();
					String prev = (String)mapval.get("PREV_STATUS");									
					String changed = (String)mapval.get("CHANGED_STATUS");
					String rescode = (String)mapval.get("RESPCODE");
					
					String prevdes="select STATUS_DESC from MAINTAIN_DESC WHERE INST_ID='"+instid+"' and CARD_ACT_CODE='"+prev+"'";
					String prevdescr = (String)jdbctemplate.queryForObject(prevdes, String.class);
					
					String chnageddes="select STATUS_DESC from MAINTAIN_DESC WHERE INST_ID='"+instid+"' and CARD_ACT_CODE='"+changed+"'";
					String chnageddescr = (String)jdbctemplate.queryForObject(chnageddes, String.class);
					
					mapval.put("PREV_STATUS",prevdescr);
					mapval.put("CHANGED_STATUS", chnageddescr);							
					itrval.remove();
					itrval.add(mapval);
				}
				 //Table with records
				 pdfgen.createSimplePDF(document,title, pdfgen.reportheader,txnfailurequrty_result,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);				
				 //To generate summary
				 //List summlist= new ArrayList();
				 //Map summarymap = new LinkedHashMap();
				 //summarymap.put("Amount",pdfgen.getTotalAmtbyKey("AMOUNT"));
				 //summlist.remove(summarymap);
				 //summlist.add(summarymap);
				 //trace("List values of summlist == "+summlist);
				 //pdfgen.addPDFSummary(document, "AMOUNT","TOTAL TRANSACTION AMOUNT",summlist , "Summary",50);
				 
				 pdfgen.closePDF(document);				 
				 setReportname(pdfgen.filename+strdate+".pdf");
				 input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				 output_stream.flush();		
			}
			else{
				session.setAttribute("curmsg", "No Details Found ");
				session.setAttribute("curerr", "E");
				return "globalreporterror";
			}
		}
		catch(Exception e){
			trace("Exception : ERROR :" +e.getMessage());
			e.printStackTrace();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error : "+e.getMessage());
		}
		trace("\n\n");enctrace("\n\n");
		return "itextpdfreport";
 }
	private String reportgendownloadfilename;	
	
	public String getReportgendownloadfilename() {
		return reportgendownloadfilename;
	}
	public void setReportgendownloadfilename(String reportgendownloadfilename) {
		this.reportgendownloadfilename = reportgendownloadfilename;
	}

 public String glReportHome(){		 
		 return "glreporthome";
	 }
	 public String genGLReport() throws IOException{
		 String returnresult = "";
		 HttpSession session 		= 	getRequest().getSession();
		 int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
		 String propertyname="GlReports";
		 List gllist=null;
		 String reportname = "GLREPORT";
		 reportname = reportname +getDatetime()+".pdf";
		 try {
			 //report_name = repor_tname +getDatetime() +".pdf";
			 trace("Report Name is ===> "+reportname);			 
			 trace("trying");
			 Document document = new Document();
			 output_stream = new ByteArrayOutputStream();
			 HttpServletRequest request= getRequest();	 
			 EODActionBeans eodbean = new EODActionBeans();
			 String instid = comInstId();			 
			 String busindate = getRequest().getParameter("business");
			 			 
			 if(returnresult.equals("eod_mismatch_list")){
				 PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				 pdfgen.addPDFTitles(document, "GL REPORT", ALIGN_CENTER);				
				 pdfgen.addPDFHeading(document, "PDF TITLE ADDED", ALIGN_CENTER, 50);
				 gllist=eodbean.getSubglamountlist();
				 ListIterator gllistitr = gllist.listIterator();
				 while(gllistitr.hasNext()){					
					String tableheader="Sub-Gl Code :,";
				    Map gllistmap = (Map) gllistitr.next();
				    String glcode= (String) gllistmap.get("SCH_CODE");				    
				    tableheader +=glcode+",Sub-Gl Description :,"+(String) gllistmap.get("SCH_NAME");				    
				    List subglamtlist = (List) gllistmap.get("SUBGLAMT");
				    tableheader+=",,,";	
				    ListIterator subglamtlistitr = subglamtlist.listIterator();
				    trace("SUBGLAMT-->"+subglamtlist);
				    while(subglamtlistitr.hasNext()){
				    	Map subglamtlistmap = (Map) subglamtlistitr.next();				       	
				       	tableheader += "Sum of Debit Amount :,"+subglamtlistmap.get("DR_AMT");
				       	tableheader += ",Sum of Credit Amount :,"+subglamtlistmap.get("CR_AMT");
				    	tableheader += ",Sum of Total Amountt :,"+subglamtlistmap.get("TOTAL_AMT");				    	
				    }		    
				    pdfgen.addSingleHeader(document,6, tableheader, ALIGN_CENTER, 100);
				    int txnflag=Integer.parseInt((String)gllistmap.get("ISSUBGLTXN"));
				    if(txnflag!=0){
				    	List subgltxnlist = (List) gllistmap.get("SUBGLTXN");
				    	ListIterator subgltxnlistitr = subgltxnlist.listIterator();
				    	while(subgltxnlistitr.hasNext()){
				    		Map subgltxnlistmap = (Map) subgltxnlistitr.next();
				    		String trandate = subgltxnlistmap.get("TRANDATE").toString();
				    		subgltxnlistmap.remove("TRANDATE");
				    		subgltxnlistmap.remove("TXNDESC");
				    		subgltxnlistmap.remove("TRANTIME");
				    		subgltxnlistmap.put("TRANDATE", trandate); 
				    		subgltxnlistitr.remove();
				    		subgltxnlistitr.add(subgltxnlistmap);
				    	}
				    	pdfgen.createSimplePDF(document, pdfgen.reporttitles, pdfgen.reportheader, subgltxnlist, pdfgen.reportsumfield,ALIGN_CENTER, ALIGN_CENTER, 100);
				      List gltotallist = (List)gllistmap.get("SUBGLTXNSUM");
				      ListIterator gltotallistitr = gltotallist.listIterator();
				      //trace("SUBGLTXNSUM->"+gltotallist);
				      String totalstr="",txnamount,optype;
				      while(gltotallistitr.hasNext()){
				    	  Map gltotallistmap = (Map) gltotallistitr.next();
				    	  txnamount = (String) gltotallistmap.get("TXNAMOUNT").toString();
				    	  optype = (String) gltotallistmap.get("OPTYPE");
				    	  trace("TXNAMOUNT-->"+ txnamount);
				    	  totalstr += "Transaction Sum of "+ optype.toUpperCase()+" : "+txnamount+"  ";
				      }
				      pdfgen.addPDFTitles(document,totalstr, ALIGN_CENTER);
				    }
				    else{
				    	pdfgen.addPDFTitles(document,"No Transactions Found for "+ glcode+" GL", ALIGN_CENTER);
				    }			    
				    
				 }
				 setReportgendownloadfilename(reportname);
				 pdfgen.closePDF(document);		
				 input_stream = new ByteArrayInputStream(output_stream.toByteArray());	
				 output_stream.flush();	
				 returnresult =  "itextpdfreport2";
			 }
			 else{
				 session.setAttribute("preverr", "E");
				 session.setAttribute("prevmsg", "GL List Empty");
				 trace("GL List Empty");
				 return "glreporthome";
			 }
			
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception occured while generating Report");
			trace("Exception : "+e.getMessage());
			return "glreporthome";
		}
		 trace("trying6");
		 return returnresult;
	 }



	 public String serviceReport(){
			return "servicereport";
	 }		
		 
public void callAJaxservicereport() throws IOException 
{
		trace("***************** Called AJax ***********************");
		HttpSession session = getRequest().getSession();
		try
		{
		String i_Name = (String)session.getAttribute("Instname");
		
		String cardnum = getRequest().getParameter("ch_no");
		
		String actvedate = null;String inputtxt_active = null,inputtxt_exp=null,expdte = null,acctno = null;
		String availbalance = null;String table_formation = "";String txndecs = null,trandate = null,trantime = null,refnum = null,merchantname=null,remainingbal=null,txncur = null,optype=null,txnamt = null;String txncur_desc = null;
		String prevstatus="",accfivetxn="",changedstatus="",actiondate="",changeddate="",changedby="",prevstatusdesc="",changedstatusdesc="",username="";
		List account_details_result = null,lsttxn_result=null;
		
		//TO GET ACTIVE DATE
		String active_date = "SELECT TO_CHAR(ACTIVE_DATE) AS ACTIVE_DATE ,TO_CHAR(EXPIRY_DATE) AS EXPIRY_DATE FROM "+TABLENAME+" WHERE INST_ID='"+i_Name+"' AND CARD_NO='"+cardnum+"'";
		//String qury="select PRODUCT_CODE,PRODUCT_DESC from IFPRODUCT_SUBTYPE where PRODUCT_ID='"+prodid+"'";
		trace("The active_date is  " + active_date);

		List active_date_result =jdbctemplate.queryForList(active_date);
		trace("The Result is  " + active_date_result);
		Iterator itr = active_date_result.iterator();
		if(!active_date_result.isEmpty()){
			while(itr.hasNext())
			{
				Map map = (Map) itr.next();
				trace("Nect to map");
				actvedate = ((String)map.get("ACTIVE_DATE"));
				expdte = ((String)map.get("EXPIRY_DATE"));
				trace("actvedate	"+actvedate);
			}	
			
			String actvieexp = "<br/><p class='textcolor'><b>CARD DETAILS</b> </p><table width='50%'><tr><td><b>Active Date:</b>"+actvedate+"</td><td><b>Expiry Date:</b>"+expdte+"</td></tr></table>";
			//CARD DETAILS
			String card_details = "select TO_CHAR(PREV_STATUS) AS PREV_STATUS, TO_CHAR(CHANGED_STATUS) AS CHANGED_STATUS, TO_CHAR(ACTION_DATE, 'DD-MON-YYYY HH:MI:SS') AS ACTION_DATE, TO_CHAR(CHANGED_BY) AS CHANGED_BY from IFP_MAINTAIN_HISTORY  WHERE INST_ID='"+i_Name+"' AND CARD_NO='"+cardnum+"' order by ACTION_DATE asc ";
			trace("The card_details is  " + card_details);

			List card_details_result =jdbctemplate.queryForList(card_details);
			trace("The Result is  " + card_details_result);				
			Iterator itrcard = card_details_result.iterator();
			String carddetails = "<table width='90%' cellpadding='0'  border='1'  cellspacing='0'  class='formtable'><tr><th>PREV STATUS</th><th>CHANGED STATUS</th><th>ACTION DATE</th><th>CHANGED BY</th></tr>";
			if(!card_details_result.isEmpty()){
				while(itrcard.hasNext())
				{
					Map map = (Map) itrcard.next();
					trace("Nect to map");
					prevstatus = ((String)map.get("PREV_STATUS"));
					prevstatusdesc = commondesc.getCardStatusDesc(i_Name, prevstatus, jdbctemplate);
					trace(prevstatusdesc);
					changedstatus = ((String)map.get("CHANGED_STATUS"));
					changedstatusdesc = commondesc.getCardStatusDesc(i_Name, changedstatus, jdbctemplate);
					trace(changedstatusdesc);
					actiondate = ((String)map.get("ACTION_DATE"));
					changedby = ((String)map.get("CHANGED_BY"));
					trace("CHANGED_BY--->"+changedby);
					username = commondesc.getUserName(i_Name,changedby,jdbctemplate);
					if(username==null){
						username ="SYSTEM";
						trace("username SYSTEM");
					}
					carddetails+="<tr><td  style='text-align:left' >"+prevstatusdesc+"</td><td  style='text-align:left' >"+changedstatusdesc+"</td><td  style='text-align:left' >"+actiondate+"</td><td  style='text-align:left'>"+username+"</td></tr>";
				}
				carddetails+="</table>";
				
			}else{
				carddetails+="<table><tr><td><b><font color='RED'>No Card Details Found... </font></b></td></tr></table>";			
			}
				
				/*EXPIRY DATE
				String exp_date = "SELECT TO_CHAR(EXPIRY_DATE) AS EXPIRY_DATE FROM "+TABLENAME+" WHERE INST_ID='"+i_Name+"' AND CARD_NO='"+cardnum+"'";
				//String qury="select PRODUCT_CODE,PRODUCT_DESC from IFPRODUCT_SUBTYPE where PRODUCT_ID='"+prodid+"'";
				trace("The exp_date is  " + exp_date);
				List exp_date_result =jdbctemplate.queryForList(exp_date);
				trace("not empty");
				Iterator itrexp = exp_date_result.iterator();
				while(itrexp.hasNext())
				{
					Map map = (Map) itrexp.next();
					trace("Nect to map");
					expdte = ((String)map.get("EXPIRY_DATE"));
					trace("expdte	"+expdte);
				}*/	
					String accdetails="";					
					String accnt = "select ACCT_NO from IFD_CARD_ACCT_LINK where card_no='"+cardnum+"' AND INST_ID='"+i_Name+"'";
					trace("The accnt is  " + accnt);	
					List accnt_result =jdbctemplate.queryForList(accnt);
					trace("accnt_result.size()  " + accnt_result.size()+"/n  accnt_result.size()"+ accnt_result);			
					Iterator itraccnt = accnt_result.iterator();
					if(!accnt_result.isEmpty()){
						while(itraccnt.hasNext())
						{
							Map map = (Map) itraccnt.next();
							trace("Nect to map");
							acctno = ((String)map.get("ACCT_NO"));
							trace("acctno	"+acctno);
							trace("account Number "+acctno);
							//AVAILABLE BALANCE
							String account_details ="SELECT TO_CHAR(AVAIL_BALANCE) AS  AVAIL_BALANCE,ACCT_CCY FROM ACCOUNTINFO WHERE ACCT_NO='"+acctno+"' AND INST_ID='"+i_Name+"'";
							trace(account_details);
							account_details_result =jdbctemplate.queryForList(account_details);
							trace("account_details_result length "+account_details_result.size());
							Iterator itr_account_details = account_details_result.iterator();
							while(itr_account_details.hasNext())
							{
								Map mapaccount_details = (Map) itr_account_details.next();
								availbalance = ((String)mapaccount_details.get("AVAIL_BALANCE"));
								txncur = ((String)mapaccount_details.get("ACCT_CCY"));
								txncur_desc = commondesc.getGlobalcurrencydesc(txncur,jdbctemplate);
								if(txncur_desc.equals("TANZANIA SHILLINGS"))
								{
									txncur_desc ="TZS";
								}
							}						
							accdetails+= "<br/><p class='textcolor'><b> ACCOUNT DETAILS </b></p><table width='50%'><tr><td><b>TXNCURRENCY:</b>"+txncur_desc+"</td><td><b>AVAIL BALANCE:</b>"+availbalance+"</td></tr></table>";						
							String tableheader = "<table width='90%' cellpadding='0'  border='0'  cellspacing='0'  class='formtable'><tr><th class='textcolor' align='center'>TRANSACTION DESCRIPTION</th><th class='textcolor' align='center'>TRAN DATE</th><th class='textcolor' align='center'>TRAN TIME</th><th class='textcolor' align='center'>REF NUM</th><th class='textcolor' align='left'>MERCHANT NAME</th><th class='textcolor' align='center'>TXN TYPE</th><th class='textcolor' align='right'>TXN AMOUNT</th><th class='textcolor' align='right'>REMAINING BALANCE</th></tr>";						
							//LAST 5 TRANSATION
							//String lsttxn = "SELECT TXNDESC, TO_CHAR(TRANDATE) AS TRANDATE,TO_CHAR(TRANTIME) AS TRANTIME, REFNUM,TXNCURRENCY,ACCEPTORNAME,OPTYPE, TXNAMOUNT,to_char(ENTITYBALANCE,'"+amountformat+"') AS REMAINIINGBALANCE ";
							//lsttxn += "  FROM IFP_PL_TXN WHERE  CHN='"+cardnum+"'  AND ACCOUNTNO='"+acctno+"' AND  INST_ID='"+i_Name+"'   AND ( ENTRYTYPE ='$FC' OR ENTRYTYPE ='$TC' OR ENTRYTYPE ='$MC' ) AND ROWNUM < 30 ORDER BY PLSEQNO DESC  ";//(entrytype='$FC' or entrytype='$MC' ) AND
							//lsttxn += "  WHERE ROWNUM < 100  ";
							String lsttxn = "SELECT TXNDESC, TO_CHAR(TRANDATE) AS TRANDATE, TO_CHAR(TRANTIME) AS TRANTIME, REFNUM,TXNCURRENCY,ACCEPTORNAME,OPTYPE, TXNAMOUNT,";
							lsttxn += " to_char(ENTITYBALANCE,'999G999G999G999G999G999D99MI') AS REMAINIINGBALANCE   FROM ( ";
							lsttxn += " select * from IFP_PL_TXN WHERE  CHN='"+cardnum+"'  AND ACCOUNTNO='"+acctno+"'  AND  INST_ID='"+i_Name+"'   AND ( ENTRYTYPE ='$FC' OR ENTRYTYPE ='$TC' OR ENTRYTYPE ='$MC' ) ";
							lsttxn += " ORDER BY PLSEQNO DESC  ) where rownum <=30 ";
							
							enctrace("ServiceReport " + lsttxn);
							lsttxn_result =jdbctemplate.queryForList(lsttxn);
							trace("Las 5 transaction List "+lsttxn_result);
							Iterator itr_lsttxn_result = lsttxn_result.iterator();
							if(!lsttxn_result.isEmpty()){
								while(itr_lsttxn_result.hasNext())
								{
									Map mapaccount_details = (Map) itr_lsttxn_result.next();
									txndecs = ((String)mapaccount_details.get("TXNDESC"));
									trandate = ((String)mapaccount_details.get("TRANDATE"));
									trantime = ((String)mapaccount_details.get("TRANTIME"));
									refnum = ((String)mapaccount_details.get("REFNUM"));
									merchantname = ((String)mapaccount_details.get("ACCEPTORNAME"));
									remainingbal = ((String)mapaccount_details.get("REMAINIINGBALANCE"));
									//txncur = ((String)mapaccount_details.get("TXNCURRENCY"));
									//txncur_desc = commondesc.getGlobalcurrencydesc(txncur,jdbctemplate);
									optype = ((String)mapaccount_details.get("OPTYPE"));
									txnamt = ((String)mapaccount_details.get("TXNAMOUNT"));
									table_formation+= "<tr><td style='text-aling:left;width:100px'>"+txndecs+"</td><td>"+trandate+"</td>" +
									    "<td>"+trantime+"</td><td>"+refnum+"</td> <td>"+refnum+"</td>" +
									    "<td>"+optype+"</td><td align='right'>"+txnamt+"</td><td align='right'>"+remainingbal+"</td></tr>";
								}					
								accfivetxn+=accdetails+tableheader+table_formation;
							}else{
								accfivetxn+=accdetails+"<table><tr><td><b><font color='RED'>No Transaction Found </font></b></td></tr></table>";
								trace("No transaction found for  This Account no : "+acctno);
							}
							String total = accfivetxn+"</table>"+actvieexp+carddetails;
							getResponse().getWriter().write(total);
						}							
				}else{
					String cardstatus = "";
					if( acctno == null ){
						 
					}
					getResponse().getWriter().write("<table><tr><td><b><font color='RED'> No Records Found </font></b></td></tr></table>");
					
				}
			
			}else{
				getResponse().getWriter().write("<table><tr><td><b><font color='RED'>Card Not Found... </font></b></td></tr></table>");
			}
		}
		catch (Exception e) 
		{
				String err = "Erro WHile Send Error MEssage to Ajax ";
	 			trace("Erro WHile Send Error MEssage to Ajax "+e.getMessage());
	 			getResponse().getWriter().write(err);
		}
		  
}
		
		public String downloadservicereport() throws IOException 
		{
			trace("***************** Called download ***********************");
			int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
			HttpSession session = getRequest().getSession();
			output_stream = new ByteArrayOutputStream();
			Document document = new Document();
			HttpServletRequest request= getRequest();
			String currentdatetime = getDatetime();
			String propertyname ="downloadservicereport";
			try
			{
			String i_Name = (String)session.getAttribute("Instname");
			
			String cardnum = getRequest().getParameter("ch_no");
			String txnheader="";
			String actvedate = null;String inputtxt_active = null,inputtxt_exp=null,expdte = null,acctno = null;
			String availbalance = null;String table_formation = "";String txndecs = null,trandate = null,trantime = null,refnum = null,txncur = null,optype=null,txnamt = null;String txncur_desc = null;
			String prevstatus="",accfivetxn="",changedstatus="",actiondate="",changeddate="",changedby="",prevstatusdesc="",changedstatusdesc="",username="";
			List account_details_result = null,lsttxn_result=null;
			
			//TO GET ACTIVE DATE
			String active_date = "SELECT TO_CHAR(ACTIVE_DATE) AS ACTIVE_DATE ,TO_CHAR(EXPIRY_DATE) AS EXPIRY_DATE FROM "+TABLENAME+" WHERE INST_ID='"+i_Name+"' AND CARD_NO='"+cardnum+"'";
			//String qury="select PRODUCT_CODE,PRODUCT_DESC from IFPRODUCT_SUBTYPE where PRODUCT_ID='"+prodid+"'";
			trace("The active_date is  " + active_date);

			List active_date_result =jdbctemplate.queryForList(active_date);
			trace("The Result is  " + active_date_result);
			Iterator itr = active_date_result.iterator();
			if(!active_date_result.isEmpty()){
				while(itr.hasNext())
				{
					Map map = (Map) itr.next();
					trace("Nect to map");
					actvedate = ((String)map.get("ACTIVE_DATE"));
					expdte = ((String)map.get("EXPIRY_DATE"));
					trace("actvedate	"+actvedate);
				}	
			
				String pdfheader = "Card Number : "+cardnum;
				
				//CARD DETAILS
				String card_details = "select TO_CHAR(PREV_STATUS) AS PREV_STATUS, TO_CHAR(CHANGED_STATUS) AS CHANGED_STATUS, TO_CHAR(ACTION_DATE) AS ACTION_DATE, TO_CHAR(CHANGED_BY) AS CHANGED_BY from IFP_MAINTAIN_HISTORY  WHERE INST_ID='"+i_Name+"' AND CARD_NO='"+cardnum+"' order by ACTION_DATE ";
				trace("The card_details is  " + card_details);
	
				List card_details_result =jdbctemplate.queryForList(card_details);
				trace("The Result is  " + card_details_result);				
				ListIterator itrcard = card_details_result.listIterator();
				String carddetailsheader = "PREV STATUS,CHANGED STATUS,ACTION DATE,CHANGED BY";
				if(!card_details_result.isEmpty()){
					while(itrcard.hasNext())
					{
						Map map = (Map) itrcard.next();
						trace("Nect to map");
						prevstatus = ((String)map.get("PREV_STATUS"));
						prevstatusdesc = commondesc.getCardStatusDesc(i_Name, prevstatus, jdbctemplate);
						trace(prevstatusdesc);
						changedstatus = ((String)map.get("CHANGED_STATUS"));
						changedstatusdesc = commondesc.getCardStatusDesc(i_Name, changedstatus, jdbctemplate);
						trace(changedstatusdesc);
						actiondate = ((String)map.get("ACTION_DATE"));
						changedby = ((String)map.get("CHANGED_BY"));
						username = commondesc.getUserName(i_Name,changedby,jdbctemplate);
						if(username==null){
							username ="SYSTEM";
							trace("username SYSTEM");
						} 
						map.put("PREV_STATUS", prevstatusdesc);
						map.put("CHANGED_STATUS", changedstatusdesc);
						map.put("CHANGED_BY", username);
						itrcard.remove();
						itrcard.add(map);
						//carddetails+="<tr><td>"+prevstatusdesc+"</td><td>"+changedstatusdesc+"</td><td>"+actiondate+"</td><td>"+username+"</td></tr>";
					}
					}
					//carddetails+="</table>";	
				
					
						String accdetailsheader="";					
						String accnt = "select ACCT_NO from IFD_CARD_ACCT_LINK where card_no='"+cardnum+"' AND INST_ID='"+i_Name+"'";
						trace("The accnt is  " + accnt);	
						List accnt_result =jdbctemplate.queryForList(accnt);
						trace("accnt_result.size()  " + accnt_result.size()+"/n  accnt_result.size()"+ accnt_result);			
						Iterator itraccnt = accnt_result.iterator();
						if(!accnt_result.isEmpty()){
								while(itraccnt.hasNext())
								{
									Map map = (Map) itraccnt.next();
									trace("Nect to map");
									acctno = ((String)map.get("ACCT_NO"));
									trace("acctno	"+acctno);
									trace("account Number "+acctno);
									//AVAILABLE BALANCE
									String account_details ="SELECT TO_CHAR(AVAIL_BALANCE, '"+amountformat+"') AS  AVAIL_BALANCE,ACCT_CCY FROM ACCOUNTINFO WHERE ACCT_NO='"+acctno+"' AND INST_ID='"+i_Name+"'";
									trace(account_details);
									account_details_result =jdbctemplate.queryForList(account_details);
									trace("account_details_result length "+account_details_result.size());
									Iterator itr_account_details = account_details_result.iterator();
									while(itr_account_details.hasNext())
									{
										Map mapaccount_details = (Map) itr_account_details.next();
										availbalance = ((String)mapaccount_details.get("AVAIL_BALANCE"));
										txncur = ((String)mapaccount_details.get("ACCT_CCY"));
										txncur_desc = commondesc.getGlobalcurrencydesc(txncur,jdbctemplate);
										if(txncur_desc.equals("TANZANIA SHILLINGS"))
										{
											txncur_desc ="TZS";
										}
									}						
									accdetailsheader= "Available-Balance :" + availbalance.replace(",", "") + txncur_desc ;					
									txnheader = "TXN DESCRIPTION,TRAN DATE,TRAN TIME,REF NUM,MERCHANT NAME,TXN TYPE,TXN AMOUNT,REMAINING BALANCE";						
									//LAST 5 TRANSATION
									//String lsttxn = "SELECT TXNDESC, TO_CHAR(TRANDATE) AS TRANDATE,TO_CHAR(TRANTIME) AS TRANTIME, REFNUM,OPTYPE, TXNAMOUNT FROM IFP_PL_TXN WHERE CHN='"+cardnum+"'  AND ACCOUNTNO='"+acctno+"' AND INST_ID='"+i_Name+"' AND ROWNUM <=100 ORDER BY PLSEQNO DESC";
									//String lsttxn = "SELECT TXNDESC, TO_CHAR(TRANDATE) AS TRANDATE,TO_CHAR(TRANTIME) AS TRANTIME, REFNUM,ACCEPTORNAME,OPTYPE, to_char(TXNAMOUNT,'"+amountformat+"') as TXNAMOUNT,  to_char(ENTITYBALANCE,'"+amountformat+"') AS REMAINIINGBALANCE  ";
									//lsttxn += "  FROM IFP_PL_TXN  WHERE CHN='"+cardnum+"'  AND ACCOUNTNO='"+acctno+"'     AND ( ENTRYTYPE ='$FC' OR ENTRYTYPE ='$TC' OR ENTRYTYPE ='$MC' ) AND INST_ID='"+i_Name+"' ORDER BY PLSEQNO DESC  "; //(entrytype='$FC' or entrytype='$MC' ) AND
									//lsttxn += "  WHERE ROWNUM < 100  ";
									String lsttxn = "SELECT TXNDESC, TO_CHAR(TRANDATE) AS TRANDATE, TO_CHAR(TRANTIME) AS TRANTIME, REFNUM,ACCEPTORNAME,OPTYPE, TXNAMOUNT, ";
									lsttxn += " to_char(ENTITYBALANCE,'999G999G999G999G999G999D99MI') AS REMAINIINGBALANCE   FROM ( ";
									lsttxn += " select * from IFP_PL_TXN WHERE  CHN='"+cardnum+"'  AND ACCOUNTNO='"+acctno+"'  AND  INST_ID='"+i_Name+"'   AND ( ENTRYTYPE ='$FC' OR ENTRYTYPE ='$TC' OR ENTRYTYPE ='$MC' ) ";
									lsttxn += " ORDER BY PLSEQNO DESC  ) where rownum <=30 ";
									trace(lsttxn);
									lsttxn_result =jdbctemplate.queryForList(lsttxn);
									trace("Las 100 transaction List "+lsttxn_result);
									ListIterator itr_lsttxn_result = lsttxn_result.listIterator();
									if(!lsttxn_result.isEmpty()){
										while(itr_lsttxn_result.hasNext())
										{
											Map mapaccount_details = (Map) itr_lsttxn_result.next();
											//txncur = ((String)mapaccount_details.get("TXNCURRENCY"));
											//txncur_desc = commondesc.getGlobalcurrencydesc(txncur,jdbctemplate);
											//mapaccount_details.remove("TXNCURRENCY");
											//mapaccount_details.put("TXNCURRENCY",txncur_desc);
											itr_lsttxn_result.remove();
											itr_lsttxn_result.add(mapaccount_details);
										}				
									
									}else{
										session.setAttribute("preverr", "E");
										session.setAttribute("prevmsg", "No Transaction Found This Account no : "+acctno);
										return "globalreporterror";
									}
								 }
								//String total = pdfheader+carddetails+accfivetxn+"</table>";
								PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);								
								pdfgen.addPDFTitles(document, pdfgen.reporttitles, ALIGN_CENTER);								
								pdfgen.addSingleHeader(document, 1, pdfheader, ALIGN_LEFT,50);
								
								
								pdfgen.addPDFTitles(document, "ACCOUNT DETAILS ",ALIGN_LEFT);								
								pdfgen.reportheader=txnheader;
								pdfgen.createSimplePDFwithtitle(document, accdetailsheader, pdfgen.reportheader, lsttxn_result, "", ALIGN_CENTER, ALIGN_CENTER, 100);
								pdfgen.reportheader=carddetailsheader;
								if(!card_details_result.isEmpty()){
									pdfgen.createSimplePDFwithtitle(document, "CARD DETAILS ", pdfgen.reportheader, card_details_result, "", ALIGN_LEFT, ALIGN_CENTER, 100);
								} else {
									pdfgen.addPDFTitles(document, "No Card Maintenance Details Found....", ALIGN_CENTER);
								}
								pdfgen.closePDF(document);
								setReportgendownloadfilename(pdfgen.filename+currentdatetime+".pdf");
								input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
								output_stream.flush();	
								return "itextpdfreport2";
							  }else{
									session.setAttribute("preverr", "E");
									session.setAttribute("prevmsg", "Account not configure this account no .. "+acctno);
									return "globalreporterror";
								  
							 }
					}else{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "No Card Details Found... ");
						return "globalreporterror";
					}				
			}
			catch (Exception e) 
			{		
		 			session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Exception While generating Pdf file : "+e.getMessage());
					return "globalreporterror";
			}			  
	}  

	 public String stmtReport(){
			trace("**************** customer statement report begin ****************");
			enctrace("**************** customer statement report begin ****************");
			trace("\n\n");
			enctrace("\n\n");
			String instid = comInstId();
			HttpSession session = getRequest().getSession();
			
			try{
				List curlist = commondesc.fchInstCurrencyList(instid, jdbctemplate);
				setCurrencylist(curlist);
			}catch(Exception e){
				trace("Exception: Unable to get the institution currency list ");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Unable to continue the process");
				
			}
			return "stmtreporthome";
		}
	 
	public String stmtReportView(List listqry){
		trace("****************  statement report View begin ****************");
		enctrace("****************  statement report  View begin ****************");
		trace("\n\n");
		enctrace("\n\n");
		HttpSession session = getRequest().getSession();
		try {
			if(!(listqry.isEmpty())){
				trace("List size"+listqry.size());
				setStment_report_list(listqry);
				return "statementview";
			}
			else{
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Result found for this chn");
				trace("No Result found for this chn");
				return "globalreporterror";
			}
			
		}
		catch(Exception e){
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: could not process to generate  statement report View");
			trace( "Exception: could not process to generate  statement report View" + e.getMessage());
			e.printStackTrace();
			trace("\n\n");
			enctrace("\n\n");
			return "globalreporterror";
		}
	}	

	public String chnstmtReport(String instid, List listqry,String fromdate,String todate,String chn){
		trace("**************** Statement report ****************");
		enctrace("**************** statement report ****************");
		HttpSession session = getRequest().getSession();		 
		try{
			String reportname=chn+"_statement_"+commondesc.getDateTimeStamp()+".pdf";
			int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;			
			
			HttpServletRequest request= getRequest();
			Document document = new Document();
			output_stream = new ByteArrayOutputStream();
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String username = (String)session.getAttribute("SS_USERNAME");			
			trace("User Name  ============="+username);
			String propertyname ="chnstmtReport";
			trace(" List value "+listqry  );
			if(!(listqry.isEmpty())){
				String customername = "";
				String custid = commondesc.getCustomerIdByCardNumber(instid, chn, jdbctemplate);				
				if( custid == null ){ 
					customername = "-NOT REGISTERED-";
				}else{
					customername = commondesc.fchCustName(instid, custid, jdbctemplate);
				}
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);				
				String reporttitles= pdfgen.reporttitles;
				String cardholdertype = commondesc.getCardHolderType(instid, chn, jdbctemplate);
				if( cardholdertype.equals("$MC")){
					reporttitles ="Merchant Statement Report";
				}else{
					reporttitles ="CardHolder Statement Report";
				}
				setReportgendownloadfilename(reportname);
				String reportheader="From Date ,:,"+fromdate+ ",To Date ,:,"+todate+",Card Number,:,"+chn+",CardHolder Name,:,"+customername;
				pdfgen.addPDFTitles(document, reporttitles, ALIGN_CENTER);
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				pdfgen.createSimplePDF(document, reporttitles, pdfgen.reportheader, listqry, pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);
				pdfgen.closePDF(document);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			}
			else{
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "ERROR : Record Not Found ");
				trace("ERROR : Record Not Found ");
				trace("\n\n");
				enctrace("\n\n");
				return "globalreporterror";
			}
			output_stream.flush();

		}catch(Exception e){			
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception: could not process to generate  customer statement report ");
			trace( "Exception: could not process to generate  customer statement report " + e.getMessage());
			e.printStackTrace();
			trace("\n\n");
			enctrace("\n\n");
			return "globalreporterror";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "itextpdfreport2";
	}

	public String downloadStmReport(){
	    HttpSession session = getRequest().getSession();
		try{
		trace("Generate Statement report begin");
		enctrace("Generate Statement report begin");
		trace("\n\n");
		enctrace("\n\n");
		
		String submit = getRequest().getParameter("submit");			
		String i_Name = (String)session.getAttribute("Instname");
		String fromdate = getRequest().getParameter("fromdate").trim();
		String todate = getRequest().getParameter("todate").trim();			
		String chn = getRequest().getParameter("chn").trim();
		
		//String curcode = "834";
		String curcode =commondesc.fchDefaultCurrency(i_Name,jdbctemplate);
		
		String txncode = "", customerqry="",customerdetails="",refnum="";
		String acctno = commondesc.getAccountNumberByCardNo(i_Name, chn, curcode, jdbctemplate);
		trace("Account number : " + acctno);
		if( acctno == null ){
			trace("Unable to continue...could not get acct no...got : "+acctno);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Unable to continue the process" );
			return "globalreporterror";
		}
		
		String datetimeformat = "to_char( trandate || ' ' || substr(lpad(trantime,6, 0),0,2)|| ':' || substr(lpad(trantime,6, 0),3,2)|| ':' || substr(lpad(trantime,6, 0),5,2) ) as trandate ";
		
		String query_str= " select CHN, TXNDESC, "+datetimeformat+", to_char( REFNUM ) as REFNUM,  ACCEPTORNAME, TERMLOC, TXNCODE, TO_CHAR(TXNAMOUNT ) AS TXNAMOUNT, DECODE(OPTYPE,'CR','--',OPTYPE) AS OPTYPEDR, DECODE(OPTYPE,'DR','--',OPTYPE) AS OPTYPECR, to_char(ENTITYBALANCE,'99999999G999D99MI')  AS REMAINIINGBALANCE from IFP_PL_TXN where ACCOUNTNO='"+acctno+"'   AND ( ENTRYTYPE ='$FC' OR ENTRYTYPE ='$TC' OR ENTRYTYPE ='$MC' ) AND trandate between TO_DATE('"+fromdate+"','dd-MM-yyyy') AND  TO_DATE('"+todate+"','dd-MM-yyyy')+1 order by plseqno desc ";// AND (entrytype='$FC' or entrytype='$MC' )
		enctrace(" statement report query : "+ query_str);
		List listqry = jdbctemplate.queryForList(query_str);
		trace(" List value "+listqry  );
		if( listqry==null || listqry.isEmpty() ){
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","NO RECORDS FOR GIVEN CARD NUMBER  " );
			trace("NO RECORDS FOR GIVEN CHN " );
			return "globalreporterror";
		}else{
			Iterator itr = listqry.iterator();
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				txncode =(String) mp.get("TXNCODE");
				refnum=(String) mp.get("REFNUM");
				customerdetails="";
				if( txncode.equals("400001") ||  txncode.equals("820000")){
					try {
						customerqry = "SELECT ( TOCHN || '-' || FNAME || ' ' || MNAME || ' ' || LNAME ) AS DETAILS FROM IFP_TRANSACTION_MASTER A, IFD_CARD_ACCT_LINK B, IFP_CUSTINFO_PRODUCTION C WHERE A.INST_ID=B.INST_ID AND B.INST_ID=C.INST_ID AND REFNUM='"+refnum+"' AND A.TOCHN=B.CARD_NO AND B.CIN=C.CIN AND ROWNUM <=1";
						customerdetails = (String)jdbctemplate.queryForObject(customerqry, String.class);
					} catch (EmptyResultDataAccessException e) { 
						customerdetails="--";
						customerqry = "SELECT  TOCHN AS DETAILS FROM IFP_TRANSACTION_MASTER A, IFD_CARD_ACCT_LINK B WHERE A.INST_ID=B.INST_ID AND REFNUM='"+refnum+"' AND A.TOCHN=B.CARD_NO  AND ROWNUM <=1";
						customerdetails = (String)jdbctemplate.queryForObject(customerqry, String.class)+"-NA";
					}
				}
				mp.put("TXNCODE", customerdetails);
			}
		}
		
		
		if(submit.equals("View") && !listqry.isEmpty()){
			return stmtReportView(listqry);
		}
		else if(submit.equals("PDF") && !listqry.isEmpty()){
			return chnstmtReport(i_Name,listqry,fromdate,todate,chn); 
		}
		else if(submit.equals("Excel") && !listqry.isEmpty())
		{		
			output_stream = new ByteArrayOutputStream();
		    String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	        trace("dateFormat.format(date)    --->  "+curdatetime);
	        String defaultname = "StatementReport"+curdatetime+".xls";
	
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("Sheet1");	
				int rownum = 0;int cellnum = 0;
				Iterator  itr1 = listqry.iterator();
				int rowno=0;
				HSSFRow rowheading=null;
				while( itr1.hasNext() ){					
					Map map = (Map)itr1.next();
					Iterator keyItr = map.keySet().iterator();
					if(rowno==0){
						rowheading = sheet.createRow((short)rowno++);
					}
					HSSFRow row = sheet.createRow((short) rowno++);
					String key = null;
					int cellno=0;
					while(keyItr.hasNext())
					{
						
						HSSFCell cell = null;
						key = (String) keyItr.next();
						if(rowno==2){
							cell = rowheading.createCell((short)cellno);
							cell.setCellValue(key);
						}                                           
						cell = row.createCell((short)cellno);
						cell.setCellValue((String)map.get(key));
						cellno++;
					}	
					
				}	
				workbook.write(output_stream);
				setReportgendownloadfilename(defaultname);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush();	
				return "stmtexcel";
			}			
		}
		catch (Exception e) {
			trace("ERROR: ->"+e.getMessage());	
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to generate Report");
			trace("Exception : "+e.getMessage() );
			e.printStackTrace();
		}
		return "globalreporterror";
   }

             
///////////////////////FEE REPORT 
             
 			public String feeReportHome(){
 				String instid 		=  comInstId(); 
 				String subprodcode = getRequest().getParameter("subproductlist");
 				HttpSession session = getRequest().getSession();  
 								
 					try {					
 						//********************* GET Product List  ***********************
 						prodlist = commondesc.getProductList( instid, jdbctemplate, session ); 
 						trace(" prodlist====> "+prodlist);
 						setProdlist(prodlist);

 					} 
 					catch (Exception e)	{
 						session.setAttribute("curerr", "E");
 						session.setAttribute("curmsg", "Error while fetching product list" + e.getMessage()); 
 					}
 				return "feereporthome";
 			}
 			
 			
 			 public void getFeecode() {
 					
 					String feecode, feedesc="";
 					List result;
 					String instid =comInstId();
 					String subprodid=getRequest().getParameter("subprodid").trim();		
 					//FeeConfigDAO feedao = new FeeConfigDAO();
 					try{						
 						List feeDescription = getMasterFeeList(instid, subprodid, jdbctemplate);
 						trace(" feeDes====> "+feeDescription);
 						
 						if (!(feeDescription.isEmpty())) {
 							String sel = "<select name='feelist' id='feelist'>";
 							sel = sel+ "<option value=\"-1\">--Select Fee--</option>";
 							sel = sel+ "<option value=\"000\">ALL</option>";
 							Iterator itr = feeDescription.iterator();
 							while (itr.hasNext()) {
 								Map map = (Map) itr.next();
 								feecode = ((String) map.get("FEE_CODE"));
 								feedesc = ((String) map.get("FEE_DESC"));
 								sel = sel + "<option value=\" " + feecode.trim() + "\">"+ feedesc + "</option>";
 							}
 							sel = sel + "</select>";
 							getResponse().getWriter().write(sel);
 						} else {
 								String noproduct = "<select name='feelist' id='feelist'>";
 								noproduct = noproduct+ "<option value=\"-1\">--No Fee--</option></select>";						
 								getResponse().getWriter().write(noproduct);
 						
 						}
 						
 					
 					}
 					catch(Exception e){
 						trace("Error");
 						}
 			 	}
 				
 			 public List getMasterFeeList(String instid,String subprodid,JdbcTemplate jdbctemplate ) throws Exception {
 					List masterfeelist = null;
 					String masterfeelistqry="";
 					if(subprodid.equals("000")){
 						masterfeelistqry ="SELECT FEE_CODE,  FEE_DESC from IFP_FEE_DESC WHERE INST_ID = '"+instid+"' ";
 					}
 					else{
 						masterfeelistqry ="SELECT FEE_CODE,  FEE_DESC from IFP_FEE_DESC WHERE FEE_CODE='"+subprodid+"' AND INST_ID = '"+instid+"' ";
 					}
 					enctrace("masterfeelistqry : " + masterfeelistqry );
 					masterfeelist = jdbctemplate.queryForList(masterfeelistqry);
 					return masterfeelist;
 				}
 			 public String feeReportpdfgenerator(){
 					trace("**************** Fee Report Begins ****************");
 					enctrace("**************** Fee Report Begins ****************");
 					HttpSession session = getRequest().getSession();		
 					GLConfigureDAO glkeys = new GLConfigureDAO();
 					
 					try{
 						/*String reportname="FeeReport"+commondesc.getDateTimeStamp()+".pdf";
 						trace("reportname----> "+reportname);*/
 						int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;			
 						
 						HttpServletRequest request= getRequest();
 						Document document = new Document();
 						output_stream = new ByteArrayOutputStream();
 						SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
 						Date cur_date = new Date();
 						String strdate =date_format.format(cur_date).toString();
 						/*String username = (String)session.getAttribute("SS_USERNAME");			
 						trace("User Name  ============="+username);*/
 						String i_Name 				= 	(String)session.getAttribute("Instname");
 						String in_name 				= 	i_Name.toUpperCase();
 						
 						String propertyname ="feeReport";
 						String feelist = getRequest().getParameter("feelist").trim();
 						//IFP_FEE_DESC
 						String query_str= "select FEE_CODE,FEE_DESC,TO_CHAR(INTRODATE) AS INTRODATE,USER_NAME,DECODE('1','ACTIVE','0','INACTIVE') AS STATUS_FLAG from IFP_FEE_DESC where fee_code='"+feelist+"' AND INST_ID='"+i_Name+"'";
 						trace("query_str---> "+query_str);
 						enctrace(" Fee Description query : "+query_str);
 						List listqry = jdbctemplate.queryForList(query_str);
 						trace(" List value "+listqry  );
 						
 						String feecode="",feedesc="",username="",feeconfigdate="",fee_user_name="",status_flag="";
 						String subfee_code="",subfee_desc="",subfee_type="",fromdate="",todate="",subfee_usercode="",configbydate="",fee_code="",subfee_username="";
 						PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);							
 						String reporttitles= pdfgen.reporttitles;
 						pdfgen.addPDFTitles(document, reporttitles, ALIGN_CENTER);
 						int fee_master_size=0;
 						if(!(listqry.isEmpty())){
 							ListIterator itrqry = listqry.listIterator();
 							while(itrqry.hasNext()){
 								Map map = (Map) itrqry.next();
 								feecode = ((String) map.get("FEE_CODE"));
 								
 								
 								//*****************************************************************
 								//IFP_FEE_DESC
 								String subfeedetails = "select FEE_SUBCODE,SUBFEE_DESC,DECODE('S','SPECIAL FEE','D','DEFAULT FEE') AS SUBFEE_TYPE,TO_CHAR(FROMDATE) AS FROMDATE,TO_CHAR(TODATE) AS TODATE,FEE_CODE,USER_CODE,TO_CHAR(CONFIG_DATE) AS CONFIG_DATE FROM IFP_FEE_SUBCODE WHERE FEE_CODE ='"+feecode+"'";
 								trace("subfeedetails---> "+subfeedetails);
 								enctrace(" Fee Description query    : "+subfeedetails);
 								List subfeelistqry = jdbctemplate.queryForList(subfeedetails);
 								trace("SUBFEE List value "+subfeelistqry  );
 								trace("SUBFEE List value "+subfeelistqry );
 								feedesc = ((String) map.get("FEE_DESC"));
 								feeconfigdate = ((String) map.get("INTRODATE"));
 								fee_user_name = ((String) map.get("USER_NAME"));
 								status_flag = ((String) map.get("STATUS_FLAG"));
 								username = commondesc.getUserName(i_Name,fee_user_name,jdbctemplate);
 								map.put("USERNAME", username);
 								itrqry.remove();
 								itrqry.add(map);
 								trace("TOTAL FEE DESCRIPTION ITERATOR---> "+map);
 								String reportheader="Fee Descripton ,:,"+feedesc+ ",Fee Config Date ,:,"+feeconfigdate+", Configured by,:,"+username+", Status,:,"+status_flag;						
 								pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
 								if(!(subfeelistqry.isEmpty())){
 									ListIterator subitrqry = subfeelistqry.listIterator();
 									while(subitrqry.hasNext()){		
 										ArrayList subindividual_itrqry = new ArrayList();
 										Map submap = (Map) subitrqry.next();
 										subfee_code = ((String) submap.get("FEE_SUBCODE"));
 										subfee_desc = ((String) submap.get("SUBFEE_DESC"));
 										subfee_type = ((String) submap.get("SUBFEE_TYPE"));
 										fromdate = ((String) submap.get("FROMDATE"));
 										todate = ((String) submap.get("TODATE"));
 										fee_code = ((String) submap.get("FEE_CODE"));
 										subfee_usercode = ((String) submap.get("USER_CODE"));
 										configbydate = ((String) submap.get("CONFIG_DATE"));
 										subfee_username = commondesc.getUserName(i_Name,subfee_usercode,jdbctemplate);										
 										submap.put("USERNAME", subfee_username);
 										submap.remove("FEE_CODE");
 										submap.remove("USER_CODE");
 										submap.remove("FEE_SUBCODE");
 										subitrqry.remove();
 										subindividual_itrqry.add(submap);										
 										trace("TOTAL SUBFEE_DESCRIPTION DETAILS ITERATOR---> "+submap);
 										pdfgen.addPDFTitles(document, " Sub Fee desc : "+subfee_desc, ALIGN_LEFT);		
 										String reportheader1=" Sub Fee desc : "+subfee_desc+" Sub Fee type : "+subfee_type+" From Date : "+fromdate+" To Date : "+todate+", Configured By : "+subfee_username+" Configured Date : "+configbydate;									
 										pdfgen.createSimplePDF(document, reporttitles, pdfgen.reportheader, subindividual_itrqry, pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);										
 										//pdfgen.addSingleHeader(document,2,reportheader1,ALIGN_LEFT,100);
 										
 										//*****************************************************************
 										//IFP_FEE_MASTER
 										String feemaster_action,feemaster_terminal,feemaster_feetype,feemaster_minamt,feemaster_maxamt,feemaster_feemode,feeamount,feemaster_recordid;
 										String fee_master ="select FEE_ACTION,TERMINAL_TYPE,FEE_TYPE,TO_CHAR(MIN_AMOUNT) AS MIN_AMOUNT,TO_CHAR(MAX_AMOUNT) AS MAX_AMOUNT,FEE_AMOUNT,DECODE(FEE_MODE,'F','FLAT','P','PERCENTAGE','C','COMBINED',FEE_MODE) AS FEE_MODE,RECORD_ID from IFP_FEE_MASTER where fee_code='"+fee_code+"' and fee_subcode='"+subfee_code+"' ORDER BY FEE_ACTION,  TO_NUMBER(MIN_AMOUNT)";
 										trace("fee_master---> "+fee_master);
 										List fee_masteristqry = jdbctemplate.queryForList(fee_master);
 										if(!(fee_masteristqry.isEmpty())){
 											ListIterator feemasteritrqry = fee_masteristqry.listIterator();
 											fee_master_size += fee_masteristqry.size();
 											while(feemasteritrqry.hasNext()){												
 												Map feemastermap = (Map) feemasteritrqry.next();
 												feemaster_action = ((String) feemastermap.get("FEE_ACTION"));
 												feemaster_terminal = ((String) feemastermap.get("TERMINAL_TYPE"));
 												feemaster_feetype = ((String) feemastermap.get("FEE_TYPE"));
 												feemaster_minamt = ((String) feemastermap.get("MIN_AMOUNT"));
 												feemaster_maxamt = ((String) feemastermap.get("MAX_AMOUNT"));
 												feeamount= ((String) feemastermap.get("FEE_AMOUNT"));
 												feemaster_feemode = ((String) feemastermap.get("FEE_MODE"));
 												feemaster_recordid = ((String) feemastermap.get("RECORD_ID"));
 												String feemaster_reportheader="Fee Action : "+feemaster_action+",Terminal Type : "+feemaster_terminal+",Fee type : "+feemaster_feetype+",Minimum Amount: "+feemaster_minamt+",Maximum Amount : "+feemaster_maxamt+",Fee Amount : "+feeamount+",Fee Mode : "+feemaster_feemode;
 												pdfgen.addSingleHeader(document,6,feemaster_reportheader,ALIGN_LEFT,100);
 												
 												//*****************************************************************
 												//IFP_FEE_MASTER_REVENUE
 												String revenue_master_action,revenue_master_terminal,revenue_master_feetype,revenue_entity_desc;
 												String fee_revenuemaster ="select ENTITY_ID,TO_CHAR(REVENUE_AMT) AS REVENUE_AMT,DECODE(REV_MODE,'F','FLAT','P','PERCENTAGE',REV_MODE) AS REV_MODE from IFP_FEE_MASTER_REVENUE where fee_code='"+fee_code+"' and fee_subcode='"+subfee_code+"' AND record_id='"+feemaster_recordid+"' ORDER BY ENTITY_ID";
 												trace("fee_revenuemaster---> "+fee_revenuemaster);
 												List fee_revenuemasteristqry = jdbctemplate.queryForList(fee_revenuemaster);
 												trace("<------- fee_revenuemasteristqry----->    "+fee_revenuemasteristqry);
 												String revenue_master_reportheader="Revenue Holders ,Revenue Amount ,Revenue Mode";
 												if(!(fee_revenuemasteristqry.isEmpty())){
 													ListIterator feerevenue_masteritrqry = fee_revenuemasteristqry.listIterator();
 													
 													while(feerevenue_masteritrqry.hasNext()){												
 														Map revenue_mastermap = (Map) feerevenue_masteritrqry.next();
 														revenue_master_action = ((String) revenue_mastermap.get("ENTITY_ID"));
 														revenue_master_terminal = ((String) revenue_mastermap.get("REVENUE_AMT"));
 														revenue_master_feetype = ((String) revenue_mastermap.get("REV_MODE"));
 														
 														revenue_entity_desc = glkeys.getGlKeyDesc(revenue_master_action,jdbctemplate);
 														revenue_mastermap.put("ENTITYDESC", revenue_entity_desc);
 														revenue_mastermap.remove("ENTITY_ID");
 														feerevenue_masteritrqry.remove();														
 														revenue_master_reportheader+=","+revenue_entity_desc+","+revenue_master_terminal+","+revenue_master_feetype;													
 													}
 												}
 												pdfgen.addSingleHeader(document,3,revenue_master_reportheader,ALIGN_CENTER,50);
 											}
 										}
 									 }
 								}
 							}
 							trace("fee_master_size-----LENGTH -----  "+fee_master_size);
 							pdfgen.noofRecords(document,fee_master_size, ALIGN_RIGHT);
 							pdfgen.closePDF(document);
 							input_stream = new ByteArrayInputStream(output_stream.toByteArray());
 							setFee_report_name(pdfgen.filename+getDateTimeStamp()+".pdf");
 						}
 						else{
 							session.setAttribute("curerr", "E");
 							session.setAttribute("curmsg", "ERROR : Record Not Found ");
 							trace("ERROR : Record Not Found ");
 							trace("\n\n");
 							enctrace("\n\n");
 							return "globalreporterror";
 						}					
 						output_stream.flush();
 						return "itextfeepdfreport";
 					}
 				catch (Exception e) { 
 					session.setAttribute("preverr", "E");
 					session.setAttribute("prevmsg", " Error while generating Report" );
 					trace("Error while generating Report " + e.getMessage());	
 					e.printStackTrace();
 					trace("\n\n");
 					enctrace("\n\n");
 					return "globalreporterror";
 				}
 			 }
	 
 			public List cardprocess_report_list;
 			public List getCardprocess_report_list(){
 				 return cardprocess_report_list;
 			}
 			public void setCardprocess_report_list(List cardprocess_report_list){
 				this.cardprocess_report_list=cardprocess_report_list;
 			}
 			public List transanalyses_report_list;
 			
 			public List getTransanalyses_report_list(){
 				return transanalyses_report_list;
 			}
 			
 			public void setTransanalyses_report_list(List transanalayses){
 				this.transanalyses_report_list= transanalayses;			
 			}
 			
 		
 			List binlist;
 			
 			public List getBinlist() {
 				return binlist;
 			}
 			public void setBinlist(List binlist) {
 				this.binlist = binlist;
 			}
 			public String transAnalysesIndex(){
 				HttpSession session = getRequest().getSession();
 				try{
 					
 					String i_Name			  =(String)session.getAttribute("Instname");				
 					binlist=commondesc.getListOfBins(i_Name,"",jdbctemplate,session);				
 					return "transanalysesindex";
 				}
 				catch(Exception e)
 				{
 					trace("ERROR: ->"+e.getMessage());	
 					session.setAttribute("preverr", "E");
 					session.setAttribute("prevmsg", "Could not unblocked the merchant");
 					return "globalreporterror";
 				}
 			}
 			public String cardProcessIndex(){
 				HttpSession session = getRequest().getSession();
 				try{
 					
 					String i_Name			  =(String)session.getAttribute("Instname");				
 					binlist=commondesc.getListOfBins(i_Name,"",jdbctemplate,session);				
 					return "cardprocessindex";
 				}
 				catch(Exception e)
 				{
 					trace("ERROR: ->"+e.getMessage());	
 					session.setAttribute("preverr", "E");
 					session.setAttribute("prevmsg", "Could not unblocked the merchant");
 					return "globalreporterror";
 				}
 			}
 			public String downloadTransanalysesReport(){
 				    HttpSession session = getRequest().getSession();
 					try{
 					trace("Generate transanction analyses Report report begin");
 					enctrace("Generate transanction analyses Report begin");
 					trace("\n\n");
 					enctrace("\n\n");
 					
 					String downloadname="Transaction_Analyses_Report";
 					String submit = getRequest().getParameter("submit");			
 					String bin = getRequest().getParameter("bin");
 					String product = getRequest().getParameter("prodid");
 					String subproduct = getRequest().getParameter("subprodid"); 					
 					String fromdate = getRequest().getParameter("fromdate");
 					String todate = getRequest().getParameter("todate");
 					String instid = comInstId(); 
 					
					
 					// String chn = getRequest().getParameter("chn");
 					String subproductqry ="";
 					if(!subproduct.equals("ALL")){
 						subproductqry=" and SUBPRODUCT='"+subproduct+"'";
 						subproduct = commondesc.getSubProductdesc(instid, subproduct, jdbctemplate);
 					}
 					if(!product.equals("ALL")){ 						
 						product= commondesc.getProductdesc(instid, product, jdbctemplate);
 					}
 					if(!bin.equals("ALL")){ 						
 						bin = commondesc.getBinDesc(instid, bin, jdbctemplate);
 					}
 					
								
					
					
 					String query_str= "SELECT TXNTYPE,  CHN,  TO_CHAR(SUM(AMOUNT)) AS SUMAMOUNT,  TXNCODE, SUBPRODUCT  from IFP_TRANSACTION_MASTER where INST_ID='"+instid+"'"+subproductqry+" and TRANDATE between TO_DATE('"+fromdate+"','dd-MM-yyyy')AND  TO_DATE('"+todate+"','dd-MM-yyyy') GROUP BY CHN, TXNTYPE,TXNCODE,SUBPRODUCT ORDER BY SUBPRODUCT ";
 					enctrace(" transanction analyses Report query : "+ query_str);
 					List listqry = jdbctemplate.queryForList(query_str);
 					trace(" List value "+listqry  );				
 					if( listqry==null || listqry.isEmpty() ){
 						session.setAttribute("curerr", "E");
 						session.setAttribute("curmsg","NO RECORDS FOR GIVEN Inputs  " );
 						trace("NO RECORDS FOUND" );
 						return "globalreporterror";
 					} 					
 					else if(submit.equals("PDF") && !listqry.isEmpty()){
 						List headerlist= new ArrayList();
 						
 						Map headerlist_map = new HashMap();
 						headerlist_map.put("Bin", bin);
 						headerlist_map.put("Product", product);
 						headerlist_map.put("Subproduct", subproduct);
 						headerlist.add(headerlist_map);
 						
 						String propertyname="downloadTransanalysesReport";
 						return genPdfReport(instid,listqry,downloadname,fromdate, todate,headerlist,propertyname);
 					}
 					else if(submit.equals("Excel") && !listqry.isEmpty())
 					{		
 						//String downloadname="Card"
 						return genExcelReport(listqry,downloadname);
 					}			
 					}
 					catch (Exception e) {
 					trace("ERROR: ->"+e.getMessage());	
 					session.setAttribute("preverr", "E");
 					session.setAttribute("prevmsg", "Could not generate Report");
 					e.printStackTrace();
 					}
 					return "globalreporterror";
 				}
 			public String downloadCardProcessReport(){
 				    HttpSession session = getRequest().getSession();
 					try{
 					trace("Generate card process Report report begin");
 					enctrace("Generate card process  Report begin");
 					trace("\n\n");
 					enctrace("\n\n");
 					
 					String downloadname="CARD_PROCESSING_REPORT";
 					String submit = getRequest().getParameter("submit");			
 					String bin = getRequest().getParameter("bin");
 					String product = getRequest().getParameter("prodid");
 					String subproduct = getRequest().getParameter("subprodid"); 					
 					String fromdate = getRequest().getParameter("fromdate");
 					String todate = getRequest().getParameter("todate");
 					String reporttype = getRequest().getParameter("reporttype");
 					String instid = comInstId(); 
 					// String chn = getRequest().getParameter("chn"); 
 					String cardstatus="";
 					if(!reporttype.equals("ALL")){
 					 cardstatus=" and STATUS_CODE='"+reporttype+"'";
 					}
 					String subproductqry ="";
 					if(!subproduct.equals("ALL")){
 						subproductqry=" and SUB_PROD_ID='"+subproduct+"'";
 					}
 					String query_str= "SELECT CARD_NO,CIN,CARD_STATUS,PRODUCT_CODE,SUB_PROD_ID,BIN FROM "+TABLENAME+"  where INST_ID='"+instid+"'"+subproductqry+cardstatus +" and ISSUE_DATE between TO_DATE('"+fromdate+"','dd-MM-yyyy')AND  TO_DATE('"+todate+"','dd-MM-yyyy')";
 					enctrace(" card process   query : "+ query_str);
 					List listqry = jdbctemplate.queryForList(query_str);
 					trace(" List value "+listqry  );
 					ListIterator itr = listqry.listIterator();	
 					String productdes="",subproductdes="",bindes="";
					while(itr.hasNext()){
						Map newmap = (Map)itr.next();
						String cin = (String) newmap.get("CIN");						
						String custname=commondesc.fchCustName(instid, cin, jdbctemplate);
						String productcode = (String) newmap.get("PRODUCT_CODE");
						productdes = commondesc.getProductdesc(instid, productcode, jdbctemplate);
						String subproductcode = (String) newmap.get("SUB_PROD_ID");
						subproductdes = commondesc.getSubProductdesc(instid, subproductcode, jdbctemplate);
						String bincode = (String) newmap.get("BIN");
						bindes = commondesc.getBinDesc(instid, bincode, jdbctemplate);
						String cardno = (String) newmap.get("CARD_NO");											
						String cardstatuscode = (String) newmap.get("CARD_STATUS");
						String prevdes="select STATUS_DESC from MAINTAIN_DESC WHERE INST_ID='"+instid+"' and CARD_ACT_CODE='"+cardstatuscode+"'";
						String prevdescr = (String)jdbctemplate.queryForObject(prevdes, String.class);
						newmap.put("CARD NO", cardno);
						newmap.put("CUSTOMER NAME", custname);	
						newmap.put("CARD STATUS", prevdescr);
						newmap.put("SUBPRODUCT_NAME", subproductdes);
						newmap.put("PRODUCT_NAME", productdes);
						newmap.put("BIN", bindes);
						newmap.remove("CARD_NO");
						newmap.remove("CARD_STATUS");
						newmap.remove("CIN");
						newmap.remove("PRODUCT_CODE");
						newmap.remove("SUB_PROD_ID");						
						itr.remove();
						itr.add(newmap);
						
					}					
 					if( listqry==null || listqry.isEmpty() ){
 						session.setAttribute("curerr", "E");
 						session.setAttribute("curmsg","NO RECORDS FOR GIVEN Inputs  " );
 						trace("NO RECORDS FOUND" );
 						return "globalreporterror";
 					} 					
 					else if(submit.equals("PDF") && !listqry.isEmpty()){
 						List headerlist= new ArrayList(); 						
 						Map headerlist_map = new HashMap();
 						headerlist_map.put("Bin", bin);
 						headerlist_map.put("Product", productdes);
 						headerlist_map.put("Subproduct", subproductdes);
 						headerlist_map.put("reporttype", reporttype);
 						headerlist.add(headerlist_map); 						
 						String propertyname="downloadCardProcessReport";
 						return genPdfReport(instid,listqry,downloadname,fromdate, todate,headerlist,propertyname);
 					}
 					else if(submit.equals("Excel") && !listqry.isEmpty()) 					{		
 						//String downloadname="Card"
 						return genExcelReport(listqry,downloadname);
 					}			
 				}catch (Exception e) {
 					trace("ERROR: ->"+e.getMessage());	
 					session.setAttribute("preverr", "E");
 					session.setAttribute("prevmsg", "Could not generate Report");
 					e.printStackTrace();
 				}
 					return "globalreporterror";
 			}			
 			public void getProductListbybin() throws IOException{
 				 
 				String bin = (String)getRequest().getParameter("binvalue");
 				String instid = (String)getRequest().getParameter("instid");
 				String result=commondesc.getProductListbybin(jdbctemplate,bin,instid);
 				getResponse().getWriter().write(result);
 			} 			
 			public void getSubProductList() throws IOException{
 				 
 				String prodid = (String)getRequest().getParameter("prodid");
 				String instid = (String)getRequest().getParameter("instid");
 				List subprodlist=commondesc.getSubProductList(instid, prodid, jdbctemplate);
 				Iterator  itr = subprodlist.iterator();
 				String result = "<option value='-1'> - select - </option>";
 				result += "<option value='ALL'> - ALL - </option>";	
 				while( itr.hasNext() ){
 					Map map = (Map)itr.next();
 					String maxallowedcard = (String)map.get("NO_NONPERCARD_ALLOWED"); 
 					String subprodname = (String)map.get("SUB_PRODUCT_NAME");
 					String subprodid = (String)map.get("SUB_PROD_ID");
 					result += "<option value='"+subprodid+"'>"+subprodname+"</option>";
 					//result = result + max;
 				}
 				trace("result======>"+result);
 				getResponse().getWriter().write(result);
 			} 			
 				
 			public String genPdfReport(String username, List listqry,String pdftitle,String fromdate,String todate,List headerlist,String Propertyname) throws MalformedURLException, DocumentException, IOException{
 					trace("**************** Statement report ****************");
 					enctrace("**************** statement report ****************");
 					String reportname=pdftitle+"_"+getDateTimeStamp()+".pdf";
 					int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;			
 					 
 					HttpServletRequest request= getRequest();
 					Document document = new Document();
 					output_stream = new ByteArrayOutputStream();
 					SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
 					Date cur_date = new Date();
 					String strdate =date_format.format(cur_date).toString();
 					//String username = (String)session.getAttribute("SS_USERNAME");			
 					trace("User Name  ============="+username);
 					String propertyname =Propertyname;
 					trace(" List value "+listqry  );
 					if(!(listqry.isEmpty())){
 					//ListIterator itrqry = listqry.listIterator();
 						PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
 						String reporttitles= pdfgen.reporttitles;
 						//String reportheader= pdfgen.reporttitles+", From Date : "+fromdate+" To Date : "+ todate+" CHN : "+ chn;
 						//reporttitles+= ", Date : "+strdate+" Generated By  : "+ username;
 						setReportgendownloadfilename(reportname);
 						String reportheader="From Date ,:,"+fromdate+ ",To Date ,:,"+todate;
 						Iterator herader_itr = headerlist.iterator();
 						String key =null;
 						while(herader_itr.hasNext()){
 							Map headermap = (Map)herader_itr.next();
 							Iterator headerkeyitr = headermap.keySet().iterator();
 							while(headerkeyitr.hasNext()){	
 								key = (String) headerkeyitr.next();
 								reportheader+=","+key+",:,"+(String)headermap.get(key);
 							}
 						}					
 						pdfgen.addPDFTitles(document, reporttitles, ALIGN_CENTER);
 						pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
 						pdfgen.createSimplePDF(document, reporttitles, pdfgen.reportheader, listqry, pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);
 						pdfgen.closePDF(document);
 						input_stream = new ByteArrayInputStream(output_stream.toByteArray());
 					}
 					else{
 						session.setAttribute("curerr", "E");
 						session.setAttribute("curmsg", "ERROR : Record Not Found ");
 						trace("ERROR : Record Not Found ");
 						trace("\n\n");
 						enctrace("\n\n");
 						return "globalreporterror";
 					}
 					output_stream.flush();		
 					trace("\n\n");
 					enctrace("\n\n");
 					return "itextpdfreport2";
 			}
 			
 			public String genExcelReport(List listqry,String namestr){
 				try {
	 				output_stream = new ByteArrayOutputStream();
	 			    String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	 			    trace("dateFormat.format(date)    --->  "+curdatetime);
	 			    String defaultname = namestr+curdatetime+".xls";
	 			    trace("default name"+ defaultname);
 					HSSFWorkbook workbook = new HSSFWorkbook();
 					HSSFSheet sheet = workbook.createSheet("Sheet1");	
 					int rownum = 0;int cellnum = 0;
 					Iterator  itr1 = listqry.iterator();
 					int rowno=0;
 					HSSFRow rowheading=null;
 					while( itr1.hasNext() ){					
 						Map map = (Map)itr1.next();
 						Iterator keyItr = map.keySet().iterator();
 						if(rowno==0){
 							rowheading = sheet.createRow((short)rowno++);
 						}
 						HSSFRow row = sheet.createRow((short) rowno++);
 						String key = null;
 						int cellno=0;
 						while(keyItr.hasNext())
 						{
 							
 							HSSFCell cell = null;
 							key = (String) keyItr.next();
 							if(rowno==2){
 								cell = rowheading.createCell((short)cellno);
 								cell.setCellValue(key);
 								trace("Key .........."+key);
 							}                                           
 							cell = row.createCell((short)cellno);
 							cell.setCellValue((String)map.get(key));
 							trace(" value .... "+ (String)map.get(key));
 							cellno++;
 						}	
 						
 					}	
 					workbook.write(output_stream);
 					setReportgendownloadfilename(defaultname);
 					input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
 					output_stream.flush();
 					trace("Close file");
 					return "stmtexcel";
 				}
 				catch (Exception e) {
 					trace("ERROR: ->"+e.getMessage());	
 					session.setAttribute("preverr", "E");
 					session.setAttribute("prevmsg", "Could not unblocked the merchant");
 				}
 					return "globalreporterror";
 			}
 			List Bin_name_list;
 			public List getBin_name_list() {
				return Bin_name_list;
			}
			public void setBin_name_list(List bin_name_list) {
				Bin_name_list = bin_name_list;
			}
			
			public String cardActivation(){
	 			trace("*************************** CardActivation Report Begins ********************* " );
	 			enctrace("*************************** CardActivation Report Begins ********************* " );
	 			HttpSession session=getRequest().getSession();
	 			try{
	 				 
	 				String instid = comInstId();
	 				trace("Getting bin and bin desc " );
	 				String query_bin_names = "select PRD_CODE,PRD_DESC from PRODUCTINFO where INST_ID='"+instid+"'";
	 				enctrace("query_report_names " +query_bin_names);

	 					List bin_list = jdbctemplate.queryForList(query_bin_names);
	 					if(!(bin_list.isEmpty()))
	 					{
	 						trace("List if bin and bin desc " +bin_list);
	 						setBin_name_list(bin_list);
	 						session.setAttribute("Errorstat", "S");
	 					}
	 					else
	 					{
	 						session.setAttribute("Errorstat", "E");
	 						session.setAttribute("ErrorMessage", "No Bin Configured");
	 						trace("No Bin Configured");
	 					}
	 				}
	 				catch (Exception ex) 
	 				{
	 					trace("Error While Fetching "+ex);
	 					session.setAttribute("Errorstat", "E");
	 					session.setAttribute("ErrorMessage", "Error While Fetching the Bin Description " +ex.getMessage());
	 					trace("Exception :Error While Fetching the Bin Description " +ex.getMessage());
	 				}
	 				trace("\n\n");
	 				enctrace("\n\n");
	 				return "cardactivation";
	 			}
			

	private String cardact_report_name;

	public String getCardact_report_name() {
		return cardact_report_name;
	}
	public void setCardact_report_name(String cardact_report_name) {
		this.cardact_report_name = cardact_report_name;
	}
			public String cardActivationgenreport(){
				trace("*************************** On submiting CardActivation Report generation Begins ********************* " );
				enctrace("*************************** On submiting CardActivation Report generation Begins ********************* " );
		
				int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
				HttpSession session = getRequest().getSession();
				try{
					 
					Document document = new Document();
					HttpServletRequest request= getRequest();
					output_stream = new ByteArrayOutputStream();
					String instid = comInstId();
					SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
					Date cur_date = new Date();
					String strdate =date_format.format(cur_date).toString();
					String propertyname ="cardactivationrprt";
					String namereprt ="cardactivationreport.pdf";
					setCardact_report_name(namereprt);
					String username = (String)session.getAttribute("SS_USERNAME");
					trace("User Name  ============="+username);
					String fromdate = getRequest().getParameter("fromdate");
					trace("fromdate "+fromdate);
					String todate = getRequest().getParameter("todate");
					trace("todate"+todate);
					String bin = getRequest().getParameter("bin");
					trace("bin"+bin);
					String bincond = "";String bindescription="ALL";
					if( !bin.equals("ALL")){
						bincond = " AND BIN='"+bin+"'";
						String bindec = "select PRD_DESC from PRODUCTINFO where INST_ID='"+instid+"' AND PRD_CODE='"+bin+"'";
						trace(" bin Query description"+bindec);
						bindescription =(String)jdbctemplate.queryForObject(bindec,String.class);
					}
					String getreport = getRequest().getParameter("getreport");
					System.out.println("getreport--- "+getreport); 
					String merchantid = getRequest().getParameter("merchidtxt");
					trace("merchantid   "+merchantid);
					String cardactquery="select txn_code from IFACTIONCODES where action_code='CARDACT' AND INST_ID='"+instid+"'";
					String txncodeExist =(String)jdbctemplate.queryForObject(cardactquery,String.class);
					trace("txncodeExist "+txncodeExist);
					String txndesc = null;
					List sucesscardactivation = getCardActivationdetails(instid,"110","=0",txncodeExist,fromdate,todate,merchantid,bincond,jdbctemplate);
					List unsucesscardactivation = getCardActivationdetails(instid,"110","!=0",txncodeExist,fromdate,todate,merchantid,bincond,jdbctemplate);
					System.out.println("sucesscardactivation"+sucesscardactivation);
					System.out.println(" unsucesscardactivation "+unsucesscardactivation);				
						if(sucesscardactivation.isEmpty()&& unsucesscardactivation.isEmpty()){
							output_stream.flush();
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "Error : No Records found");
							trace("Error : No Records found");
							return "globalreporterror";
						}else{
							
							if(!sucesscardactivation.isEmpty()){
								ListIterator txnItr = sucesscardactivation.listIterator();
								int rowcount =0;	
								while(txnItr.hasNext()){
									Map txnMap = (Map)txnItr.next();
									txndesc =commondesc.getTransactionDesc(instid, txncodeExist,jdbctemplate);
									String txndate  = (String)txnMap.get("TRANDATE");
									String time  = (String)txnMap.get("TRANTIME");
									String chn  = (String)txnMap.get("CHN");
									String cardholdername = commondesc.getCustomerNameByCard(instid,chn,jdbctemplate);
									System.out.println("cardholdername === "+cardholdername);
									//String cardholdername = commondesc.checkCustincardproduction(instid, cin, jdbctemplate);
									System.out.println(time);
									System.out.println(time.length());
									if(time.length()<=6){
										time="0"+time;
									}
									String txndatetime = txndate +"  "+time.substring(0, 2)+":"+time.substring(2,4)+":"+time.substring(4,6);
									System.out.println("txndatetime -- "+txndatetime);
									txnMap.put("TRANDATE",txndatetime);
									txnMap.put("TXNCODE", txndesc);
									txnMap.put("CUSNAME", cardholdername);	 								 							
									txnMap.remove("TXNLOGSEQNO");
									txnMap.remove("TRANTIME");
									txnMap.remove("TXNCODE");
									txnMap.remove("CUSTNAME");						
									txnItr.remove();
									txnItr.add(txnMap);
								
								}
								System.out.println("sucesscardactivation******* "+sucesscardactivation);					
							}
							if(!unsucesscardactivation.isEmpty()){
								ListIterator untxnItr = unsucesscardactivation.listIterator();
								int rowcount =0;	
								while(untxnItr.hasNext()){
									Map txnMap = (Map)untxnItr.next();
									txndesc =commondesc.getTransactionDesc(instid, txncodeExist,jdbctemplate);
									String txndate  = (String)txnMap.get("TRANDATE");
									String time  = (String)txnMap.get("TRANTIME");
									String chn  = (String)txnMap.get("CHN");
									String cardholdername = commondesc.getCustomerNameByCard(instid,chn,jdbctemplate);
									System.out.println("cardholdername === "+cardholdername);
									System.out.println(time);
									System.out.println(time.length());
									if(time.length()<=6){
										time="0"+time;
									}
									String txndatetime = txndate +"  "+time.substring(0, 2)+":"+time.substring(2,4)+":"+time.substring(4,6);
									System.out.println("txndatetime -- "+txndatetime);
									txnMap.put("TRANDATE",txndatetime);
									txnMap.put("TXNCODE", txndesc);	 
									txnMap.put("CUSNAME", cardholdername);
									txnMap.remove("TXNLOGSEQNO");
									txnMap.remove("TRANTIME");
									txnMap.remove("TXNCODE");
									txnMap.remove("CUSTNAME");
									untxnItr.remove();
									untxnItr.add(txnMap);
								
								}
								System.out.println(" unsucesscardactivation***** "+unsucesscardactivation);					
							}
							if(getreport.equals("PDF")){	
								PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
								String pdfreporttitle= pdfgen.reporttitles;
								pdfgen.addPDFTitles(document,pdfreporttitle,ALIGN_CENTER);
								String reporttableheader="Bin,:,"+bindescription+",Statement Date,:,"+strdate+",From Date,:,"+fromdate+",To Date,:,"+todate+",Currency,:,TZS";			
								pdfgen.addSingleHeader(document, 6, reporttableheader, ALIGN_LEFT, 70);
								String pdftitle= txndesc+" SUCCESSFUL TRANSACTION ";
								pdfgen.addPDFTableWithTitle(document, pdftitle, pdfgen.reportheader, sucesscardactivation, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_CENTER, 100);
								pdftitle= txndesc+" UNSUCCESSFUL TRANSACTION ";
								String reportheader = "CHN,MERCHANTNAME,MOBILENO,BIN,DEVICETYPE,TERMINALID,TRANDATE,REFNUM,CUSTNAME";
								pdfgen.addPDFTableWithTitle(document, pdftitle, reportheader, unsucesscardactivation, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_CENTER, 100);
								pdfgen.closePDF(document);
								setReportname(pdfgen.filename+strdate+".pdf");
								input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
								output_stream.flush();
							}else if(getreport.equals("EXCEL")){
								List combinedlist= new ArrayList();
								combinedlist.add(sucesscardactivation);
								combinedlist.add(unsucesscardactivation);
								String keyDesc = "successful - unsuccessful";
								System.out.println(" --- combinedlist --- combinedlist ---   "+combinedlist);
								return getExcelReport(combinedlist,keyDesc,propertyname);
						}		
					}
					
				} catch(Exception e){			
					trace("Error"+e.getMessage());
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Error Occure While Generating Report");
					trace("Exception: Error Occure While Generating Report");
					e.printStackTrace();
				}
				trace("\n\n");
				enctrace("\n\n");
				return "itextcardactpdfreport";
			}
			public List getCardActivationdetails(String instid,String msgtype,String respcond,String txncode,String from_date,String to_date ,String merchantid,String bincond,JdbcTemplate jdbctemplate)
			{
				List gettxnsummary = null;String summarytxn_qury;  
				trace(merchantid);
				if(merchantid.equals("")){
					summarytxn_qury = "select distinct CHN,ACCEPTORNAME as MERCHANTNAME,B.MOBILENO, ISSUERID AS BIN, DEVICETYPE, TERMINALID, TO_CHAR(TRANDATE) AS TRANDATE,TO_CHAR(TRANTIME) AS TRANTIME, TO_CHAR(A.TXNLOGSEQNO) AS TXNLOGSEQNO, REFNUM, ( C.FNAME||' '|| C.LNAME ) AS CUSTNAME from IFP_TXN_LOG A, "+TABLENAME+" B,IFP_CUSTINFO_PRODUCTION C where A.INST_ID = '"+instid+"' "+bincond+" AND TXNCODE='"+txncode+"' AND msgtype ='"+msgtype+"' AND RESPCODE "+respcond+" AND A.CHN=B.CARD_NO AND B.CIN=C.CIN AND TRUNC(A.TRANDATE) BETWEEN to_date('"+from_date+"','DD-MM-YY') AND to_date('"+to_date+"','DD-MM-YY') ORDER BY A.TXNLOGSEQNO DESC";
				}																																																																																 
				else{
					summarytxn_qury = "select distinct CHN,ACCEPTORNAME as MERCHANTNAME,B.MOBILENO, ISSUERID AS BIN, DEVICETYPE, TERMINALID, TO_CHAR(TRANDATE) AS TRANDATE,TO_CHAR(TRANTIME) AS TRANTIME, TO_CHAR(A.TXNLOGSEQNO) AS TXNLOGSEQNO, REFNUM, ( C.FNAME||' '|| C.LNAME ) AS CUSTNAME from IFP_TXN_LOG A, "+TABLENAME+" B,IFP_CUSTINFO_PRODUCTION C where A.INST_ID = '"+instid+"' "+bincond+" AND TXNCODE='"+txncode+"' AND msgtype ='"+msgtype+"' AND RESPCODE "+respcond+" AND MERCHANTID='"+merchantid+"'AND A.CHN=B.CARD_NO AND B.CIN=C.CIN AND TRUNC(A.TRANDATE) BETWEEN to_date('"+from_date+"','DD-MM-YY') AND to_date('"+to_date+"','DD-MM-YY') ORDER BY A.TXNLOGSEQNO DESC";
				}
				enctrace("summarytxn_qury ====>  "+summarytxn_qury);
				gettxnsummary = jdbctemplate.queryForList(summarytxn_qury);
				return gettxnsummary;
			}
			public String feeReportstatus()
			{
				return "feereportstatus";
			}
			
			public String generatefeestatus(){
				trace("*******Fee status report Next Begins******");
				enctrace("*******Fee status report Next Begins******");
			 	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
				HttpSession session=getRequest().getSession();
				
				try{
					 
					Document document = new Document();
					HttpServletRequest request= getRequest();
					output_stream = new ByteArrayOutputStream();
					SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
					Date cur_date = new Date();
					String strdate =date_format.format(cur_date).toString();
					String propertyname ="feestatusreport";
					String reportname="Feestatusreport"+strdate+".pdf";
					
					String instid = comInstId(); 
					String bin = getRequest().getParameter("bin");
					trace(bin);
					String type = getRequest().getParameter("type");
					trace(type);
					String fromdate = getRequest().getParameter("fromdate");
					String todate = getRequest().getParameter("todate");

					String feestatusqurty = "Select TO_CHAR(CHN) as CHN, TO_CHAR(TXNDESC) AS TXNDESC, TO_CHAR(TRANDATE) AS TRANDATE, TO_CHAR(TRANTIME) AS TRANTIME, TO_CHAR(REFNUM) AS REFNUM, TO_CHAR(TERMINALID) AS TERMINALID,TO_CHAR(OPTYPE) AS OPTYPE,TO_CHAR(TXNAMOUNT) AS TXNAMOUNT from IFP_PL_TXN where INST_ID='"+instid+"' AND ISSUERID='"+bin+"' and  ENTRYTYPE='"+type+"' and TRANDATE between to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') order by TRANDATE asc";
					trace(feestatusqurty);
					enctrace(" feestatusqurty "+feestatusqurty);
					List feestatusqurty_result = jdbctemplate.queryForList(feestatusqurty);
					trace("List feestatusqurty_result-->"+feestatusqurty_result);
					
					if(!feestatusqurty_result.isEmpty()){
						trace("NOT EMPTY");
						
						//From Date,To Date,Generated Date
						setReportgendownloadfilename(reportname);
						PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
						String title = pdfgen.reporttitles;
						pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
						String reportheader="Bin ,:,"+bin+ ",Report Generated Date ,:,"+strdate+ ",From Date ,:,"+fromdate+ ",To Date ,:,"+todate+",Type ,:,"+type;
						pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
						
						 DecimalFormat d=new DecimalFormat();
						 ListIterator itrval = feestatusqurty_result.listIterator();
						 BigDecimal summarytotal = new BigDecimal("0");
						 BigDecimal bigtransamt;					
						 while(itrval.hasNext()){
							Map mapval = (Map) itrval.next();
							String txntype = (String)mapval.get("OPTYPE");
							bigtransamt    = new BigDecimal((String)mapval.get("TXNAMOUNT"));
							//String setcurrency    = (String)mapval.get("SETTCURRENCY");
							if(txntype.equals("DR")) // if DR minus  TXN amount
							{
								summarytotal = summarytotal.subtract(bigtransamt);							
							}
							else if(txntype.equals("CR"))
							{															
								summarytotal = summarytotal.add(bigtransamt);		
							}
							//d= comInsntance().currencyFormatter(setcurrency);
							//mapval.remove("SETTCURRENCY");
							mapval.put("TXNAMOUNT",d.format(bigtransamt));
							itrval.remove();
							itrval.add(mapval);
						}
						 //Table with records
						 pdfgen.createSimplePDF(document,title, pdfgen.reportheader,feestatusqurty_result,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);
						 pdfgen.addPDFTitles(document,"TOTAL TXN AMOUNT : "+d.format(summarytotal), ALIGN_RIGHT);				 
						 pdfgen.closePDF(document);				 
						 input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
						 output_stream.flush();		
					}
					else{
						trace( "No Details Found ");
						session.setAttribute("curmsg", "No Details Found ");
						session.setAttribute("curerr", "E");
						trace("\n\n");enctrace("\n\n");
						return "globalreporterror";
					}
				}
				catch(Exception e){
					trace("Exception : ERROR :" +e.getMessage());
					e.printStackTrace();
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Error : "+e.getMessage());
				}
				trace("\n\n");enctrace("\n\n");
				return "itextpdffeereport";
		 }
		
		private List vastxnList;
		public List getVastxnList() {
			return vastxnList;
		}
		public void setVastxnList(List vastxnList) {
			this.vastxnList = vastxnList;
		}
		
		private List binList;		
		public List getBinList() {
			return binList;
		}
		public void setBinList(List binList) {
			this.binList = binList;
		}
		public String vasTransaction(){
			HttpSession session = getRequest().getSession();
			 
			try{
				String instid = comInstId();
				String selectentry_type = "SELECT * FROM IFP_GL_KEYS WHERE VAS_REP_ALLOWED='1'";
				List listentry_type = jdbctemplate.queryForList(selectentry_type);
				trace("listentry_type---->   "+listentry_type);
				if(!listentry_type.isEmpty()){
					setVastxnList(listentry_type);
				}else{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " No records found ");
				}	
				List bin = commondesc.getListOfBins(instid, jdbctemplate);
				setBinList(bin);					
			}catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Error : "+e.getMessage());
			}
			return "vasreport";
		}
		public String getVastxnreport(){
			trace("******* VAS Transaction report Begins ******");
			enctrace("******* VAS Transaction report Begins ******");
		 	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
			HttpSession session=getRequest().getSession();
			AddSubProdActionDao addsubprd = new AddSubProdActionDao();
			try{
				 
				Document document = new Document();
				HttpServletRequest request= getRequest();
				output_stream = new ByteArrayOutputStream();
				SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
				Date cur_date = new Date();
				String strdate =date_format.format(cur_date).toString();
				String reportname="VAS_transaction_report"+strdate+".pdf";
				String downloadname="VAS_Transaction_Report";
				String submit = getRequest().getParameter("submit");				
				String instid = comInstId(); 
				String fromdate = getRequest().getParameter("fromdate");
				String todate = getRequest().getParameter("todate");
				String glkeys[] = getRequest().getParameterValues("glkeys");
				trace("  glkeys.length----   >  "+glkeys.length);
				String curcode = null;String cnt_glkey = "",glkeys_code="",keyDesc="";String vastxn_qury = null;
				List vastxn_qury_result = null;List combinedlist= new ArrayList();
				String Propertyname="vastxnreport";String bindesc="";
				int addedlist = 0; 
				for( int cnt=0; cnt<glkeys.length; cnt++ ){
					glkeys_code = glkeys[cnt];
					if( cnt == glkeys.length-1 ){
						cnt_glkey += "'"+glkeys[cnt]+"'";
						keyDesc += addsubprd.getKeyDesc(glkeys_code, jdbctemplate);
					}else{
						cnt_glkey += "'"+glkeys[cnt]+"',";
						keyDesc += addsubprd.getKeyDesc(glkeys_code, jdbctemplate)+" - ";
					}if(glkeys_code.equals("$MC")){
						vastxn_qury = "SELECT CHN,TXNDESC,ACCEPTORNAME AS MERCHANTNAME,TERMLOC,TXNAMOUNT,to_char(TRANDATE,'DD-MON-YY') as TRANDATE, REFNUM,TO_CHAR(OPTYPE) AS OPTYPE from IFP_PL_TXN WHERE INST_ID='"+instid+"' AND ( TXNCODE LIKE '83%' OR TXNCODE LIKE '84%' ) AND  ENTRYTYPE='$MC'  AND TRUNC(TRANDATE) BETWEEN TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TO_DATE('"+todate+"','DD-MM-YYYY') GROUP BY  CHN,TXNDESC,ACCEPTORNAME,TERMLOC,TXNAMOUNT,TRANDATE,REFNUM,OPTYPE";
						trace("MC  MC vastxn_qury====> "+vastxn_qury );
					}else{
						vastxn_qury = "SELECT CHN,TXNDESC,ACCEPTORNAME AS MERCHANTNAME,TERMLOC,TXNAMOUNT,to_char(TRANDATE,'DD-MON-YY') as TRANDATE,REFNUM,TO_CHAR(OPTYPE) AS OPTYPE FROM IFP_PL_TXN WHERE INST_ID='"+instid+"' AND ( TXNCODE LIKE '83%' OR TXNCODE LIKE '84%' ) AND ENTRYTYPE='"+glkeys_code+"' AND TRANDATE BETWEEN TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TO_DATE('"+todate+"','DD-MM-YYYY') ORDER BY  PLSEQNO DESC";
						trace("OTHERS  vastxn_qury====> "+vastxn_qury );
					}
					enctrace(" vastxn_qury "+vastxn_qury);
					vastxn_qury_result = jdbctemplate.queryForList(vastxn_qury);
					trace("List vastxn_qury_result --> "+vastxn_qury_result.size());
					if(!vastxn_qury_result.isEmpty()){
						addedlist=1;
					}
					combinedlist.add(vastxn_qury_result);
				}		
				trace("keyDesc 			=== >   "+keyDesc);				
				trace("combinedlist        === >   "+combinedlist);
				trace("vastxn_qury_result  === >   "+vastxn_qury_result);
				trace("cnt_glkey           === >   "+cnt_glkey);
				if(submit.equals("EXCEL")){	
					if(addedlist!=0){
						return getExcelReport(combinedlist,keyDesc,downloadname);
					}else{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "No records found");
						return "globalreporterror";
					}					
				}else{
					if(addedlist!=0){
						String propertyname ="vastxnreport";
						List headerlist= new ArrayList();
						return getPdfReport(combinedlist,bindesc,keyDesc,fromdate,todate,Propertyname,downloadname);
					}else{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "No records found");
						return "globalreporterror";
					}
				}
			}catch(Exception e){
				trace("Exception : ERROR :" +e.getMessage());
				e.printStackTrace();
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Error : "+e.getMessage());
				//return "globalreporterror";
			}
			trace("\n\n");enctrace("\n\n");
			return "itextvaspdfreport";
		}	
		
		public String getPdfReport(List combinedlist,String bindesc,String keyDesc,String fromdate,String todate,String Propertyname,String pdfname) throws MalformedURLException, DocumentException, IOException{
				trace("**************** PDF VAS/Transaction Details report ****************");
				enctrace("**************** PDF VAS/Transaction Details report ****************");
				HttpSession session=getRequest().getSession();
				vas_report_name=pdfname+"_"+getDateTimeStamp()+".pdf";
				int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;			
				 
				HttpServletRequest request= getRequest();
				Document document = new Document();
				output_stream = new ByteArrayOutputStream();
				SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
				Date cur_date = new Date();
				String strdate =date_format.format(cur_date).toString();		
				String propertyname =Propertyname;
				//trace(" List value "+listqry  );
				PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
				String pdfreporttitle= pdfgen.reporttitles;
				pdfgen.addPDFTitles(document,pdfreporttitle,ALIGN_CENTER);
				String reporttableheader="From Date,:,"+fromdate+",To Date,:,"+todate;		
				pdfgen.addSingleHeader(document, 3, reporttableheader, ALIGN_LEFT, 70);
				trace("combinedlist --- "+combinedlist);
				Iterator combined_itr = combinedlist.iterator();
				int i=0;
				while(combined_itr.hasNext()){
						String keydesc_split[] = keyDesc.split(" - ");
 						List lst = (List) combined_itr.next();
 						trace("LIST --- >  "+lst);
 						if(!lst.isEmpty()){
	 						ListIterator txnItr = lst.listIterator();
	 						int rowcount =0;		
	 						while(txnItr.hasNext()){
	 							LinkedHashMap txnMap = (LinkedHashMap)txnItr.next();
	 							/*String txndesc = "Trandesc" ;
	 							String txndate  = (String)txnMap.get("TRANDATE");
	 							String time  = (String)txnMap.get("TRANTIME");						
	 							String txndatetime = txndate +"  "+time.substring(0, 2)+":"+time.substring(2,4)+":"+time.substring(4,6);
	 							txnMap.put("TRANDATE",txndatetime);
	 							txnMap.put("TXNCODE", txndesc);
	 							txnMap.remove("ACCOUNTNO");
	 							txnMap.remove("RESPCODE");
	 							txnMap.remove("TRANTIME");
	 							txnMap.remove("REASONCODE");
	 							txnItr.remove();
	 							txnItr.add(txnMap);*/		 							
	 						}
	 						String pdftitle= keydesc_split[i]+" :";
	 						pdfgen.addPDFTableWithTitle(document, pdftitle, pdfgen.reportheader, lst, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_CENTER, 100);	 						
 						}else{
 							pdfgen.addPDFTitles(document,keydesc_split[i]+" :",ALIGN_LEFT);
 							pdfgen.addPDFTitles(document,"No records found",ALIGN_CENTER);
 						}
 						i++;
				    }			 					
 					pdfgen.closePDF(document);
 					setReportname(pdfgen.filename+strdate+".pdf");
 					input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
 					output_stream.flush();
		 			trace("\n\n");
		 			enctrace("\n\n");
		 			return "itextvaspdfreport";
			}	
		
		public String getExcelReport(List listqry,String keyDesc,String namestr){
			trace("**************** EXCEL VAS/Transaction Details report ****************");
			enctrace("**************** EXCEL VAS/Transaction Details report ****************");
			HttpSession session=getRequest().getSession();
				try {
	 				output_stream = new ByteArrayOutputStream();
	 			    String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	 			    trace("dateFormat.format(date)    --->  "+curdatetime);
	 			    String defaultname = namestr+curdatetime+".xls";
	 			    trace("default name"+ defaultname);
					HSSFWorkbook workbook = new HSSFWorkbook();						
					HSSFSheet sheet = null;
					trace(" keyDesc "+keyDesc);
					String keydesc_split[] = keyDesc.split(" - ");
					Iterator combined_itr = listqry.iterator();
					int i=0;
					while(combined_itr.hasNext()){
							List lst = (List) combined_itr.next();
							trace(" ----- lst  ---- "+lst.size());
							sheet = workbook.createSheet(keydesc_split[i]);
							int rownum = 0;int cellnum = 0;
							Iterator  itr1 = lst.iterator();
							int rowno=0;
							HSSFRow rowheading=null;
							while( itr1.hasNext() ){
								trace("rowno = ******** = "+rowno);
								Map map = (Map)itr1.next();
								Iterator keyItr = map.keySet().iterator();
								if(rowno==0){
									rowheading = sheet.createRow((short)rowno++);
									trace("*******rowheading *****");
								}
								HSSFRow row = sheet.createRow((short) rowno++);
								String key = null;
								int cellno=0;
								while(keyItr.hasNext()){	
									trace("rowno inside second while loop = ******** = "+rowno);
									HSSFCell cell = null;
									key = (String) keyItr.next();
									if(rowno==2){
										cell = rowheading.createCell((short)cellno);
										cell.setCellValue(key);
										trace("Key .........."+key);
									}                                           
									cell = row.createCell((short)cellno);
									cell.setCellValue((String)map.get(key));
									trace(" value .... "+ (String)map.get(key));
									cellno++;
								}
								trace("rowno outside while loop "+row.getRowNum());
								if(row.getRowNum() > 49998){
									trace("entered rownum ");
									sheet = workbook.createSheet(keydesc_split[i]);
									rowno=0;
									continue;
								}
							}
							i++;
							trace("	value of i at the bottom --	"+i+"	rownum -- "+rowno);
						}				
					workbook.write(output_stream);
					setReportgendownloadfilename(defaultname);
					//setVas_report_name(reportname);
					input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
					output_stream.flush();
					trace("Close file");
					return "stmtexcel";
				}catch (Exception e) {
					trace("ERROR: ->"+e.getMessage());	
					e.printStackTrace();
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not generate report");
				}
				return "globalreporterror";
			}
		
		public String getTxndetailsreport(){
			trace("******* Transaction Details report Begins ******");
			enctrace("******* Transaction Details report Begins ******");
		 	int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
			HttpSession session=getRequest().getSession();
			AddSubProdActionDao addsubprd = new AddSubProdActionDao();
			String propertyname ="transactiondetailsreport";
			try{
				 
				Document document = new Document();
				HttpServletRequest request= getRequest();
				output_stream = new ByteArrayOutputStream();
				SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
				Date cur_date = new Date();
				String strdate =date_format.format(cur_date).toString();
				String reportname="Transaction_report"+strdate+".pdf";
				String downloadname="Transaction_report";
				String submit = getRequest().getParameter("submit");				
				String instid = comInstId(); 
				String fromdate = getRequest().getParameter("txnfromdate");
				String todate = getRequest().getParameter("txntodate");
				String bin = getRequest().getParameter("bin");
				String glkeys[] = getRequest().getParameterValues("txnglkeys");
				String vastxn_qury = null;List vastxn_qury_result = null;String qry_concat;String bindesc;
				if(bin.equals("ALL")){
					bindesc = "ALL";
					qry_concat = "SELECT  CHN, TXNDESC, REFNUM,TXNAMOUNT, OPTYPE, ACCEPTORNAME AS MERCHANTNAME,TRANDATE||':'||TRANTIME AS DATETIME FROM  IFP_PL_TXN WHERE INST_ID='"+instid+"' AND TRANDATE BETWEEN TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TO_DATE('"+todate+"','DD-MM-YYYY') AND ";
				}else{
					bindesc = commondesc.getBinDesc(instid, bin, jdbctemplate);
					qry_concat = "SELECT  CHN, TXNDESC, REFNUM,TXNAMOUNT, OPTYPE, ACCEPTORNAME AS MERCHANTNAME,TRANDATE||':'||TRANTIME AS DATETIME FROM  IFP_PL_TXN WHERE INST_ID='"+instid+"' AND TRANDATE BETWEEN TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TO_DATE('"+todate+"','DD-MM-YYYY') AND ISSUERID='"+bin+"' AND ";
				}
				int addedlist = 0;String cnt_glkey = "";List combinedlist= new ArrayList();String keyDesc="";
				for( int cnt=0; cnt<glkeys.length; cnt++ ){
					String glkeys_code = glkeys[cnt];
					trace(" glkeys_code --- > "+glkeys_code);					
					if( cnt == glkeys.length-1 ){
						if(glkeys_code.equals("$TXN")){
							keyDesc += "Transaction";
						}else{
							cnt_glkey += "'"+glkeys[cnt]+"'";
							keyDesc += addsubprd.getKeyDesc(glkeys_code, jdbctemplate);
						}
					}else{
						if(glkeys_code.equals("$TXN")){
							keyDesc += "Transaction"+" - ";
						}else{
							trace("HAI entered else part");
							cnt_glkey += "'"+glkeys[cnt]+"',";
							trace("cnt_glkey cnt_glkey "+cnt_glkey);
							keyDesc += addsubprd.getKeyDesc(glkeys_code, jdbctemplate)+" - ";
						}
					}					
					trace("keyDesc  ===  >  "+keyDesc);
					if(glkeys_code.equals("$TXN")){
						if(bin.equals("ALL")){
							vastxn_qury = "SELECT CHN, TXNDESC, REFNUM, AMOUNT, TXNTYPE, ACCEPTORNAME AS MERCHANTNAME,TRANDATE||':'||TRANTIME AS DATETIME FROM IFP_TRANSACTION_MASTER WHERE INST_ID='"+instid+"' AND TRANDATE BETWEEN TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TO_DATE('"+todate+"','DD-MM-YYYY') ORDER BY TXNMASTERSEQNO ASC";
							trace("TRANSACTION vastxn_qury====> "+vastxn_qury );
						}else{
							vastxn_qury = "SELECT CHN, TXNDESC, REFNUM, AMOUNT, TXNTYPE, ACCEPTORNAME AS MERCHANTNAME,TRANDATE||':'||TRANTIME AS DATETIME FROM IFP_TRANSACTION_MASTER WHERE INST_ID='"+instid+"' AND TRANDATE BETWEEN TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TO_DATE('"+todate+"','DD-MM-YYYY')  AND  ISSUERID='"+bin+"' ORDER BY TXNMASTERSEQNO ASC";
							trace("TRANSACTION vastxn_qury====> "+vastxn_qury );	
						}
					}
					
					vastxn_qury =  qry_concat+"ENTRYTYPE ='"+glkeys_code+"' ORDER BY PLSEQNO ASC";
					enctrace("vastxn_qury  "+vastxn_qury );
					
					/*if(glkeys_code.equals("$AMB-EXP")){
						vastxn_qury =  qry_concat+"ENTRYTYPE ='$AMB-EXP' ORDER BY PLSEQNO ASC";
						trace("MEMBER EXPENSE vastxn_qury====> "+vastxn_qury );
					}if(glkeys_code.equals("$ABK-EXP")){
						vastxn_qury = qry_concat+"ENTRYTYPE ='$ABK-EXP' ORDER BY PLSEQNO ASC";
						trace("BANK EXPENSE vastxn_qury====> "+vastxn_qury );
					}if(glkeys_code.equals("$MB-NIR")){
						vastxn_qury = qry_concat+"ENTRYTYPE ='$MB-NIR' ORDER BY PLSEQNO ASC";
						trace("MEMBER INCOME vastxn_qury====> "+vastxn_qury );
					}if(glkeys_code.equals("$BK-NIR")){
						vastxn_qury = qry_concat+"ENTRYTYPE ='$BK-NIR' ORDER BY PLSEQNO ASC";
						trace(" BANK INCOME vastxn_qury====> "+vastxn_qury );
					}*/
					enctrace(" vastxn_qury "+vastxn_qury);
					vastxn_qury_result = jdbctemplate.queryForList(vastxn_qury);
					trace("List vastxn_qury_result --> "+vastxn_qury_result);
					if(!vastxn_qury_result.isEmpty()){
						addedlist=1;
					}
					combinedlist.add(vastxn_qury_result);
				}if(submit.equals("EXCEL")){					
					if(addedlist!=0){
						return getExcelReport(combinedlist,keyDesc,downloadname);	
					}else{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "No records found");
						return "globalreporterror";
					}					
				}else{
					if(addedlist!=0){
						return getPdfReport(combinedlist,bindesc,keyDesc,fromdate,todate,propertyname,downloadname);
		 			}else{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "No records found");
						return "globalreporterror";	
		 			}
				}
			}catch(Exception e){
				trace("Exception : ERROR :" +e.getMessage());
				e.printStackTrace();
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Error : "+e.getMessage());
			}
			trace("\n\n");enctrace("\n\n");
			return "itextvaspdfreport";
	}
		
		
	////CREDIT CARD REPORT STARTS
			private String creditcard_report_name;

					public String getCreditcard_report_name() {
				return creditcard_report_name;
			}
			public void setCreditcard_report_name(String creditcard_report_name) {
				this.creditcard_report_name = creditcard_report_name;
			}
					public String creditcardstatementreport(){
						trace("*************************** credit card statement Report generation Begins ********************* " );
						enctrace("*************************** credit card statement Report generation Begins ********************* " );
				
						int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
						HttpSession session = getRequest().getSession();
						try{
							 
							Document document = new Document();
							document.open();
							HttpServletRequest request= getRequest();
							output_stream = new ByteArrayOutputStream();
							String instid = comInstId();
							SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
							Date cur_date = new Date();
							String strdate =date_format.format(cur_date).toString();
							String propertyname ="creditcardstatementrprt";
							String namereprt ="creditcardstatementreport.pdf";
							setCreditcard_report_name(namereprt);
							String username = (String)session.getAttribute("SS_USERNAME");
							trace("User Name  ============="+username);
							String fromdate = getRequest().getParameter("fromdate");
							trace("fromdate "+fromdate);
							String todate = getRequest().getParameter("todate");
							trace("todate"+todate);
							String getreport = getRequest().getParameter("getreport");
							System.out.println("getreport--- "+getreport); 
							
							String bin = getRequest().getParameter("bin");
							trace("bin"+bin);
							String bincond = "";String bindescription="ALL";
							if( !bin.equals("ALL")){
								bincond = " AND BIN='"+bin+"'";
								String bindec = "select PRD_DESC from PRODUCTINFO where INST_ID='"+instid+"' AND PRD_CODE='"+bin+"'";
								trace(" bin Query description"+bindec);
								bindescription =(String)jdbctemplate.queryForObject(bindec,String.class);
							}
							String merchantid = getRequest().getParameter("merchidtxt");
							trace("merchantid   "+merchantid);
							String cardactquery="select txn_code from IFACTIONCODES where action_code='CARDACT' AND INST_ID='"+instid+"'";
							String txncodeExist =(String)jdbctemplate.queryForObject(cardactquery,String.class);
							trace("txncodeExist "+txncodeExist);
							
							//Use Single header query to get the below details
							String cardno = "4660 4400 0000 0050";
							String totalpaymentdue = "0.00";
							String minimumpaymentdue = "0.00";
							
							//Use Address query to get the below details
							String custname 	= "BAL KUMAR PANDEY";
							String houseno		= "H.NO 87";
							String streetname 	= "Hanumnastan marga";
							String city 		= "Katmandu";
							String country 		= "Nepal";
							String pincode 		= "P.O.Box 8975";
							
							
							//PLACE UR QUERY 
							String Creditlimitqry = "SELECT ACQUIRERID,ACCOUNTNO,ACCTCURRENCY,ACQCOUNTRYCODE,to_char(LOCALDATE,'DD-MON-YY') as LOCALDATE,to_char(TRANDATE,'DD-MON-YY')  as TRANDATE FROM IFP_TXN_LOG where INST_ID='PBL' and TXNCODE='400001' and CHN='6376762000200000012'";
							List Creditlimitlist=jdbctemplate.queryForList(Creditlimitqry); 
							trace("Getting Creditlimitqry .."+Creditlimitqry);
							//PLACE UR QUERY 
							String openingbalancelist = "Select TO_CHAR(CHN) as CHN, TO_CHAR(TXNDESC) AS TXNDESC, TO_CHAR(TRANDATE) AS TRANDATE, TO_CHAR(TRANTIME) AS TRANTIME, TO_CHAR(REFNUM) AS REFNUM,TO_CHAR(OPTYPE) AS OPTYPE,TO_CHAR(TXNAMOUNT) AS TXNAMOUNT from IFP_PL_TXN where INST_ID='MBL' and CHN='9999990300000029'";
							List openingbalanceqry=jdbctemplate.queryForList(openingbalancelist); 
							System.out.println("Getting openingbalanceqry .."+openingbalanceqry);
							//PLACE UR QUERY 
							String merchantcitylist = "SELECT SETTCURRENCY,SUBPRODUCT,TERMINALID,TRANSFERID,TXNAMOUNT,TXNCODE,TXNCURRENCY,TXNDESC FROM IFP_TXN_LOG where INST_ID='PBL' and TXNCODE='400001' and CHN='6376762000200000012'";
							List merchantcityqry =jdbctemplate.queryForList(merchantcitylist);
							System.out.println(" Getting merchantcityqry .. "+merchantcityqry);
							//PLACE UR SUB HEADING QUERY
							String adminuserqry = "SELECT FIRSTNAME,EMAILID,DESCRIPTION from IFP_ADMIN_USER where USERID='1'";
							List subtitle_list = jdbctemplate.queryForList(adminuserqry);
							System.out.println(" Getting adminuser_list .. "+subtitle_list);
							
								if(Creditlimitlist.isEmpty()/*&& openingbalanceqry.isEmpty() && merchantcityqry.isEmpty()*/){
									output_stream.flush();
									session.setAttribute("preverr", "E");
									session.setAttribute("prevmsg", "Error : No Records found");
									trace("Error : No Records found");
									return "globalreporterror";
								}else{								
									if(!Creditlimitlist.isEmpty()){																	
										System.out.println(" **************Creditlimitqry******* "+Creditlimitqry);	
										trace(" Getting Creditlimitqry List .. "+Creditlimitqry);	
									}
									if(!openingbalanceqry.isEmpty()){
										DecimalFormat d=new DecimalFormat();
										 ListIterator itrval = openingbalanceqry.listIterator();
										 BigDecimal summarytotal = new BigDecimal("0");
										 BigDecimal bigtransamt;					
										 while(itrval.hasNext()){
											Map mapval = (Map) itrval.next();
											String txntype = (String)mapval.get("OPTYPE");
											bigtransamt    = new BigDecimal((String)mapval.get("TXNAMOUNT"));
											//String setcurrency    = (String)mapval.get("SETTCURRENCY");
											if(txntype.equals("DR")) // if DR minus  TXN amount
											{
												summarytotal = summarytotal.subtract(bigtransamt);							
											}
											else if(txntype.equals("CR"))
											{															
												summarytotal = summarytotal.add(bigtransamt);		
											}
											//d= comInsntance().currencyFormatter(setcurrency);
											//mapval.remove("SETTCURRENCY");
											//mapval.put("TXNAMOUNT",d.format(bigtransamt));
											mapval.put("TXNAMOUNT",d.format(summarytotal));
											itrval.remove();
											itrval.add(mapval);
										 }
										System.out.println(" ***************openingbalanceqry***** "+openingbalanceqry);
										trace(" Getting Opening Balance List .. "+openingbalanceqry);
									}
									if(!merchantcityqry.isEmpty()){
										System.out.println(" ***************merchantcityqry***** "+merchantcityqry);
										trace(" Getting merchant city List .. "+merchantcityqry);
									}
									if(getreport.equals("PDF")){	
										//PDFReportGenerator pdfgen=new PDFReportGenerator(document, output_stream, propertyname, request);
										newPDFReportGenerator pdfgen=new newPDFReportGenerator(output_stream, propertyname, request);
										
										//HEADER..
										String pdfreporttitle= pdfgen.reporttitles;
										pdfgen.addPDFTitles(pdfreporttitle,ALIGN_CENTER);
										
										//ADDRESS ..
										String reporttableheader=""+custname+","+houseno+","+streetname+","+city+","+country+","+pincode;
										String reporttableheader2="CARD NUMBER,TOTAL MINIMUM DUE,TOTAL PAYMENT,94014012323,6575756700,76767";			
										
										//pdfgen.addSingleHeader(document, 2, reporttableheader, ALIGN_LEFT, 1);
										pdfgen.addLeftRightHeadertemplate1(3,reporttableheader,reporttableheader2);
										//RIGHT TITLE ..
										/*String pdftitle= "";
										pdfgen.addPDFTableWithTitle(pdftitle, pdfgen.reportheader, subtitle_list, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_RIGHT, 50);
										*/
										String pdftitle= "";
										pdfgen.addPDFTableWithTitle(pdftitle, pdfgen.reportheader, Creditlimitlist, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_CENTER, 100);
										
										pdftitle= "";
										String reportheader = "OPENINGBALANCE,-PAYMENT,-CREDIT,+PURCHASE,+CASHADVANCE,+CHARGES,-TOTALPAYMNTDUE";
										pdfgen.addPDFTableWithTitle(pdftitle, reportheader, openingbalanceqry, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_CENTER, 100);
										
										pdftitle= "";
										String reportheaderthird = "DATE,MERCHANTNAME&CITY,REFERENCENUMBER,TRANSACTIONAMOUNT,BILLINGAMOUNT,DETAILS,SETTLEMENTDATE,TXNTYPE";
										pdfgen.addPDFTableWithTitle(pdftitle, reportheaderthird, merchantcityqry, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_CENTER, 100);
										
										pdfgen.closePDF();
										setReportname(pdfgen.filename+strdate+".pdf");
										input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
										output_stream.flush();
									}else if(getreport.equals("EXCEL")){
										List combinedlist= new ArrayList();
										combinedlist.add(subtitle_list);
										combinedlist.add(subtitle_list);
										String keyDesc = "successful - unsuccessful";
										System.out.println(" --- combinedlist --- combinedlist ---   "+combinedlist);
										return getExcelReport(combinedlist,keyDesc,propertyname);
								}		
							}
							
						} catch(Exception e){			
							trace("Error"+e.getMessage());
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", " Error Occure While Generating Report");
							trace("Exception: Error Occure While Generating Report");
							e.printStackTrace();
						}
						trace("\n\n");
						enctrace("\n\n");
						return "itextcreditstatementpdfreport";
					}
					//CREDIT CARD REPORT ENDS
}
	
