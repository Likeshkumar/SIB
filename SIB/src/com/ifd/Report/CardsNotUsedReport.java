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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ifp.util.PDFReportGenerator;
import com.itextpdf.text.Document;

public class CardsNotUsedReport extends BaseAction{
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
	private PlatformTransactionManager  txManager  = new DataSourceTransactionManager();
	MasterReportGenerationDAO dao = new MasterReportGenerationDAO();
	MasterReportGenerationBean cardbean = new MasterReportGenerationBean();
	private List masterReportList;
	private static String webURL;	
	private FileInputStream file_inputstream;
	private String report_name;
	public String cardsNotUsedReport;

	
	public String getCardsNotUsedReport() {
		return cardsNotUsedReport;
	}

	public void setCardsNotUsedReport(String cardsNotUsedReport) {
		this.cardsNotUsedReport = cardsNotUsedReport;
	}

	public MasterReportGenerationBean getCardbean() {
		return cardbean;
	}

	public void setCardbean(MasterReportGenerationBean cardbean) {
		this.cardbean = cardbean;
	}

	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	
	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public String comUserId( HttpSession session ){
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}

	public String cardsNotUsedHome() throws Exception
	{
		trace("cardsNotUsedHome method called ......");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
			
		return "cardsNotUsedHome";
	}

	public String generatereport() throws IOException
	{
		trace("cardsNotUsed Report method called......... ");	
		String instid = comInstId();
		
		
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String propertyname ="cardsnotusedreport";
		DateFormat dateFormat =	new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date date 			=	new Date();
		String curdatetime	=	dateFormat.format(date);
		String period =  getRequest().getParameter("period");    
		trace("period:::"+period);    
	

		String reporttype=getRequest().getParameter("REPORTTYPE");
		
			List recordlist=null;					
			if(reporttype.equals("PDF")){
				trace("I am PDF Generator Loop");
				int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
				String reportname = "CardsNotUsedReport_";
				try {
				    recordlist = getRecordList(period,jdbctemplate);

					trace("List value " +recordlist);
					if(recordlist.isEmpty()){
						trace("No Records found");
						addActionError("No Records Found");
					}else{
						PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertyname, getRequest());
						String title = "CardsNotUsedReport";
						pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
						
						trace("Report Name is "+reportname+curdatetime+".pdf");
						setReport_name(reportname+curdatetime+".pdf");
						
						//pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
						
						ListIterator litr = recordlist.listIterator();
						
						while(litr.hasNext()){
							Map mp = (Map) litr.next();
					
							mp.put("MCARD_NO", mp.get("MCARD_NO").toString());
							mp.put("ORDER_REF_NO", mp.get("ORDER_REF_NO").toString());
							mp.put("ACCOUNT_NO", mp.get("ACCOUNT_NO").toString());
							mp.put("EMB_NAME", mp.get("EMB_NAME").toString());
							mp.put("EXPIRY_DATE", mp.get("EXPIRY_DATE").toString());
							mp.put("ISSUE_DATE", mp.get("ISSUE_DATE").toString());
							
							litr.remove();
							litr.add(mp);
						}
						pdfgen.createSimplePDF(document,title, pdfgen.reportheader,recordlist,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);							
						pdfgen.closePDF(document);
						input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
						output_stream.flush(); 
						return "itextpdfreport"; 
					}
				} catch (Exception e) {
					addActionError("Unable to continue the report generation");
					trace("Exception in Loop ::: " + e.getMessage());
				}
			}else{
				trace("Excel Report Loop");
				recordlist = getRecordList(period,jdbctemplate);
				trace("List value " +recordlist);
				if(recordlist.isEmpty()){
					trace("No Records found");
					addActionError("No Records Found");
				}else{
					return excelReportGenMaster(recordlist);
				}
			}
			
		 return "cardsNotUsedHome";
	}
	      
	private List getRecordList(String period,
			JdbcTemplate jdbctemplate2) {
		List result=null;
		try {
			String qry = "SELECT A.MCARD_NO,A.ORDER_REF_NO,A.ACCOUNT_NO,A.EMB_NAME,TO_CHAR(A.EXPIRY_DATE,'dd-mm-yyyy') AS EXPIRY_DATE,TO_CHAR(A.ISSUE_DATE,'dd-mm-yyyy') AS ISSUE_DATE "
					+ "FROM CARD_PRODUCTION A, CARD_PRODUCTION_HASH C "
					+ "WHERE A.INST_ID='SIB' AND A.ORDER_REF_NO=C.ORDER_REF_NO "
					+ "AND C.HCARD_NO IN (SELECT CHN FROM EZCARDINFO WHERE INSTID='SIB' AND STATUS='50' and LASTTXNDATE <= add_months(sysdate, -"+period+"))";
			trace("final Qry is " +qry);
			result = jdbctemplate2.queryForList(qry);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String excelReportGenMaster(List excelReportList) throws IOException
	{
		trace("*********Report Generated Method Begin***********");	
		String instid = comInstId();
		List result = excelReportList;

		String filename = "CardsNotUsedReport_";
		String excelparam = "Sheet";
		
		List combinedlist= new ArrayList();
		combinedlist.add(result);   
		String res = this.getExcelReport(combinedlist,excelparam,filename);		
		trace("Report Generation Master Result "+res);
		return res;
	}
	

	public String getExcelReport(List listqry,String keyDesc,String namestr){
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
			setCardsNotUsedReport(defaultname);
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

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}
	
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	private String condition;
	
	}
