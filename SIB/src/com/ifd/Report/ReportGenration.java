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
import java.net.InetAddress;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;


import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

import javax.servlet.ServletContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReportGenration extends BaseAction{
	
	private ByteArrayOutputStream output_stream;
	private ByteArrayInputStream input_stream;
	
	private FileInputStream file_inputstream;
	private String report_name;
	
	
	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}
	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}
	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}
	
	
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	public String comUserId( HttpSession session ){
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}

	private JdbcTemplate jdbctemplate = new JdbcTemplate();	
	private PlatformTransactionManager  txManager  = new DataSourceTransactionManager();
	
	private List reportList;
	private String rno;
	private String dispName;
	private String columnName;
	private String maxlength;
	private String minlength;
	private String validType;
	private String dateField;
	
	
	public String getRno() {
		return rno;
	}
	public void setRno(String rno) {
		this.rno = rno;
	}
	public String getDispName() {
		return dispName;
	}
	public void setDispName(String dispName) {
		this.dispName = dispName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getMaxlength() {
		return maxlength;
	}
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	public String getMinlength() {
		return minlength;
	}
	public void setMinlength(String minlength) {
		this.minlength = minlength;
	}
	public String getValidType() {
		return validType;
	}
	public void setValidType(String validType) {
		this.validType = validType;
	}
	public String getDateField() {
		return dateField;
	}
	public void setDateField(String dateField) {
		this.dateField = dateField;
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
	
	private String transctionreport;
	public String getTransctionreport() {
		return transctionreport;
	}
	public void setTransctionreport(String transctionreport) {
		this.transctionreport = transctionreport;
	}
	
	
	CommonDesc commondesc = new CommonDesc();
	private static String webURL;	
	private String reportlocation;
	
	
	public String reportGenMasterHome()
	{
		
		System.out.println("reportGenMasterHome method called ...");
		return "reportGenMasterHome";
	}
	
	public String reportGenHome()
	{
		trace("reportGenHome started ...");
		HttpSession session = getRequest().getSession();	
		String instid = (String)session.getAttribute("Instname");
		
		String reportno=getRequest().getParameter("RNO");
		setRno(reportno);
		System.out.println("getRno():::"+getRno());
		List result = this.getReportDetails(instid,reportno,jdbctemplate); 
		setReportList(result);  
		
		Properties prop = commondesc.getCommonDescProperty();
		String reportlocation = prop.getProperty("REPORTLOCATION");
		
		setReportlocation(reportlocation);
		
		/*   
		Iterator iterator=result.iterator();
		
		while(iterator.hasNext())	{
			Map map = (Map)iterator.next();
			setRno((String)map.get("RNO"));
			setDispName((String)map.get("DISP_NAME"));
			setColumnName((String)map.get("COLUMN_NAME"));
			setMaxlength((String)map.get("MAXLENGTH"));
			setMinlength((String)map.get("MINLENGTH"));
			setValidType((String)map.get("VALID_TYPE"));
			setDateField((String)map.get("DATE_FIELD"));
		}
		
		*/
		return "reportGenHome";
	}
	
	
	public String reportGenMaster() throws IOException
	{
		trace("reportGenMaster started ...");
		
		
		//System.out.println("asdf;;;;;;;;;;;;;"+getRequest().getContextPath());       
		
	//	InetAddress IPAddress = InetAddress.getLocalHost();

		//System.out.println("asdf;;;;;;;;;;;;;"+IPAddress+getRequest().getContextPath());      
		
		HttpSession session = getRequest().getSession();	
		String instid = (String)session.getAttribute("Instname");
		String userid = comUserId(session);
		String reportno=getRequest().getParameter("RNO");
		String URLLINK=getRequest().getParameter("URL").trim();;
		trace("OLDURL:"+URLLINK);   
		Properties prop = getCommonDescProperty();
		String serverPath = prop.getProperty("serverpath.location");
		
		trace("serverPath"+serverPath);    
		
		String replaceUrl = serverPath+URLLINK.replaceAll("\\$", "&");   
		
		
		
		trace("URLLINK::::"+replaceUrl);
		
		String reportCondition=getRequest().getParameter("CONDITION");
		List repconfig = this.getReportConfigDetails(instid,reportno,jdbctemplate); 
		Iterator iterator=repconfig.iterator();
		String filename="",passwdReq="",pagenoreq="",footercontent="";
		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		while(iterator.hasNext())	{
			Map map = (Map)iterator.next();
			filename = (String)map.get("REPORTNAME");
			passwdReq = (String)map.get("PASSWORD_REQ");     
			pagenoreq = (String)map.get("PAGENO_REQ");
			footercontent = (String)map.get("FOOTERCONTENT");
		}
		java.util.Date dt=null;
		SimpleDateFormat sdt=null;

		String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		filename=filename+"_"+curdatetime+".pdf";       
		String res = this.generatePDFFile(replaceUrl,filename,passwdReq,pagenoreq,footercontent,username);		
		System.out.println("reportGenMaster():::reportGenMaster"+res);  
		
		file_inputstream = new FileInputStream(new File(res));
		setReport_name(filename);
		
		return "getPDFReport";
		
	}
	
	public String ExcelReportGenMaster() throws IOException
	{
		trace("reportGenMaster started ...");
		HttpSession session = getRequest().getSession();	
		String instid = (String)session.getAttribute("Instname");
		String reportno="3";
		
		System.out.println("reportno::"+reportno);   
		
		List repconfig = this.getReportConfigDetails(instid,reportno,jdbctemplate); 
		Iterator iterator=repconfig.iterator();
		String filename="",passwdReq="";
		while(iterator.hasNext())	{
			Map map = (Map)iterator.next();
			filename = (String)map.get("REPORTNAME");
			}
		
		List result = this.getExcelReport(instid,reportno,jdbctemplate);
		
		String excelparam = this.getExcelParam(instid,reportno,jdbctemplate);
		
		List combinedlist= new ArrayList();
		combinedlist.add(result);   
		combinedlist.add(result);
		String keyDesc = "successful - unsuccessful";
		String res = this.getExcelReport(combinedlist,excelparam,filename);		
		System.out.println("reportGenMaster():::reportGenMaster"+res);
		return res;
	}
	
	
	private String getExcelParam(String instid, String reportno,JdbcTemplate jdbctemplate2) {
		String xlparam = null;
		try {
			String xlparamqry= "select EXCEL_PARAM from PDFREPORTGENRATOR where RNO='"+reportno+"' ";
			enctrace("xlparamqry : " + xlparamqry );
			xlparam = (String)jdbctemplate.queryForObject(xlparamqry,String.class);
		} catch (EmptyResultDataAccessException e) { xlparam=null; }
		return xlparam;
	}
	public String getExcelReport(List listqry,String keyDesc,String namestr){
		trace("	 ***************** Transaction Details report ****************");
		enctrace("**************** Transaction Details report ****************");
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
				System.out.println("keydesc_split[] ==== "+keydesc_split[0]);
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
				setTransctionreport(defaultname);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush();
				trace("Close file");
				return "transactionexcel";
			}catch (Exception e) {
				trace("ERROR: ->"+e.getMessage());	
				e.printStackTrace();
				addActionError("Could not generate report");
			}
			return "globalreporterror";
		}	
	
	
	
public  String generatePDFFile(String link_name_with_id,String filename,String passwdReq,String pagenoreq,String footercontent ,String username) throws MalformedURLException	{
		
		
		System.out.println(link_name_with_id+"nameeeee:0"+filename);
		FileOutputStream output_stream =null;
		File dst=null;
		URL url=null;       
		String line="";                 
		String FileName = "";        
		String reportlocation="";

		try
		{
			
			FileName=filename;

			
			PD4ML pd4ml = new PD4ML();	
			pd4ml.adjustHtmlWidth();
			pd4ml.setHtmlWidth(950);
			//pd4ml.addStyle("report.css",true);
			//pd4ml.resetAddedStyles();
			//pd4ml.fitPageVertically();
			pd4ml.generateOutlines(true);
			pd4ml.enableTableBreaks(true);    
			   
			
			/* setting pdf page footer */
			PD4PageMark pgmark=new PD4PageMark();
			pgmark.setInitialPageNumber(1);
			pgmark.setPageNumberAlignment(PD4PageMark.RIGHT_ALIGN);
			
			if(pagenoreq.equals("Y")){
			pgmark.setPageNumberTemplate("${page} - ${total}");
			}
			if(!footercontent.equals("N")){
			pgmark.setTitleTemplate(footercontent);      
			}
			pgmark.setTitleAlignment(PD4PageMark.CENTER_ALIGN);  
			if(passwdReq.equals("Y")){
			pd4ml.setPermissions(username, 0xffffffff, true);  
			}
			pgmark.setAreaHeight(500);
		    
			pd4ml.setPageFooter(pgmark);
			pd4ml.adjustHtmlWidth();  
			//pd4ml.addStyle(url, true);         
   
			//http://127.0.0.1:8080/BBMISTEST
			Properties prop = commondesc.getCommonDescProperty();
			reportlocation = prop.getProperty("REPORTLOCATION");

			//setting reportname
			dst=new File(reportlocation+"/"+FileName);    
			output_stream=new FileOutputStream(dst);

			webURL = link_name_with_id;

			//String urlStr=webURL+"/PDFSource.jsp?jspName="+ling_name_with_id;			
			//String urlStr=link_name_with_id;
			System.out.println("URLSTRING:::"+webURL);  
			String urlStr=webURL;
			try
			{  
				url=new URL(urlStr);
				java.net.URLConnection urlCon=url.openConnection();
				BufferedReader br=new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
				InputStream fileCon=null;
				synchronized(fileCon=urlCon.getInputStream())
				{
					br=new BufferedReader(new InputStreamReader(fileCon));								
					while((line=br.readLine()) != null)
					{
						line=line.trim();
						
					}
			
				}
			}
			catch(Exception e)
			{
				 System.out.println("error in connnection");

			}			
			try
			{
				synchronized(pd4ml){  
				pd4ml.render(url, output_stream);
				//input_stream = new ByteArrayInputStream(output_stream.to);	
				
				output_stream.flush();
				trace("Close file");
				output_stream.close();
				}
				
			}
			catch(Exception e)   
			{
				throw e;
			}

		}
		catch (Exception e)
		{    
			System.out.println(e.toString());
		}
		return reportlocation+"/"+FileName;   
	}					
	
public static void main(String[] args) throws MalformedURLException				
{
	ReportGenration test = new ReportGenration();						
//test.generatePDFFile("http://localhost:9090/APP_VIEW/ReportGenerator.jsp","TESTPDF.pdf");		
}	
	
	
	private List getReportDetails(String instid, String reportno,JdbcTemplate jdbctemplate) {
			List repodet = null;
			String reportquery = "SELECT RNO, DISP_NAME, COLUMN_NAME, MAXLENGTH, MINLENGTH, VALID_TYPE, DATE_FIELD FROM PDFREPORTCONFIG  WHERE RNO='"+reportno+"' ORDER BY ORDER_BY";
			enctrace("reportquery---> "+reportquery);
			repodet = jdbctemplate.queryForList(reportquery);
			return repodet;
		}
	
	private List getExcelReport(String instid, String reportno,JdbcTemplate jdbctemplate) {
		
		
		String getrep = null;
		String getrepqry = null;
		try {
			getrepqry= "select REPORTQUERY from PDFREPORTGENRATOR where RNO='"+reportno+"' ";
			enctrace("getrepqry : " + getrepqry );
			getrepqry = (String)jdbctemplate.queryForObject(getrepqry,String.class);
		} catch (EmptyResultDataAccessException e) { getrep=null; }
				
		List repodet = null;
		enctrace("reportquery---> "+getrepqry);
		repodet = jdbctemplate.queryForList(getrepqry);
		return repodet;
	}
	
private List getReportConfigDetails(String instid, String reportno,JdbcTemplate jdbctemplate) {
		
		
		List getrep = null;
		try {
			String getrepqry= "select REPORTNAME,PASSWORD_REQ,PAGENO_REQ,FOOTERCONTENT from PDFREPORTGENRATOR where RNO='"+reportno+"' ";
			enctrace("getrepqry : " + getrepqry );
			getrep = jdbctemplate.queryForList(getrepqry);
		} catch (EmptyResultDataAccessException e) { getrep=null; }
				    
		
		return getrep;
	}

	
	public List getReportList() {
		return reportList;
	}
	public void setReportList(List reportList) {
		this.reportList = reportList;
	}
	public String getReportlocation() {
		return reportlocation;
	}
	public void setReportlocation(String reportlocation) {
		this.reportlocation = reportlocation;
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

}
