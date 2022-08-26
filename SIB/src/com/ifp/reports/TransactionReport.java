package com.ifp.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.Action.ReportgenerationAction;
import com.ifp.beans.ReconBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.PDFReportGenerator;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.reports.dao.TransactionReportDao;

public class TransactionReport extends BaseAction
{
	ReconBeans reconbean = new ReconBeans();
	CommonDesc commondesc = new CommonDesc();
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
	public String comInstId(HttpSession session){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session ){ 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	private ByteArrayInputStream input_stream;
	private String report_name;
	
	
	public String getReport_name() {
		return report_name;
	}
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}
	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}
	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}
	private ByteArrayOutputStream output_stream;
	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}
	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}
	 
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	public ReconBeans getReconbean() {
		return reconbean;
	}
	public void setReconbean(ReconBeans reconbean) {
		this.reconbean = reconbean;
	}
	TransactionReportDao txndao = new TransactionReportDao();
	public List txn_list;
	public List getTxn_list() {
		return txn_list;
	}
	public void setTxn_list(List txn_list) {
		this.txn_list = txn_list;
	}	
	public String tranactionReportHome(){		 
		trace("********* tranactionReportHome ***********");enctrace("********* tranactionReportHome ***********");
		HttpSession session = getRequest().getSession();
		 
		String instid = comInstId(session);
		try{
			List binlist = commondesc.getListOfBins(instid, "", jdbctemplate, session);
			if( binlist.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not get bin list....");
				return "required_home";
			}
			reconbean.setBinlist(binlist);
			List txnlist = commondesc.txncodeList(instid,jdbctemplate);
			if( txnlist.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not get txn list....");
				return "required_home";
			}
			setTxn_list(txnlist);
		}catch(Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : could not continue the process..."); 
			trace("Exception : " + e.getMessage());
		}
		
		return "txnreport_home";
	}	
	public String genTxnReport(){
		int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
		HttpSession session=getRequest().getSession();
		try{ 
			String bincond="";
			String instid = comInstId(session);
			String bin = getRequest().getParameter("bin");
			String datetype =  getRequest().getParameter("datefld"); 
			System.out.println("datetype :" + datetype );
			String submit = getRequest().getParameter("generate");		
			 
			Document document = new Document();
			HttpServletRequest request= getRequest();
			output_stream = new ByteArrayOutputStream();
			SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
			Date cur_date = new Date();
			String strdate =date_format.format(cur_date).toString();
			String reportname="transacrion_report"+commondesc.getDateTimeStamp()+".pdf";
			setReport_name(reportname);
			String downloadname="Transaction_Report";
			String txn_list[] = getRequest().getParameterValues("txnlist");
			ReportgenerationAction reprtgen = new ReportgenerationAction();
			trace("  txnlist.length----   >  "+txn_list.length);
			BigDecimal summarytotal = new BigDecimal("0");
			DecimalFormat d=new DecimalFormat();
			String summary = "";
			String bindesc=null;
			if( bin.equals("ALL")){
				bindesc = "ALL";
				bincond = "";
			}else{
				bindesc= commondesc.getBinDesc(instid,bin, jdbctemplate);
				System.out.println("  bindesc---  "+bindesc);
				bincond = " AND BIN='"+bin+"'";
			}
			String datecond = "";String reportheader="Bin ,:,"+bin;String fromdate = null;String todate = null;
			if( datetype.equals("busdate")){
				String businessdate = getRequest().getParameter("businessdate");
				datecond = "AND BUSINESSDATE = TO_DATE('"+businessdate+"','DD-MM-YYYY') ";
				reportheader="Business Date ,:,"+businessdate+ ",Bin ,:,"+bindesc;
			}else{				
				fromdate = getRequest().getParameter("fromdate");
				todate = getRequest().getParameter("todate");
				//datecond = "AND TRANDATE <= TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TRANDATE >= TO_DATE('"+fromdate+"','DD-MM-YYYY')  ";
				datecond = "AND TRUNC(TRANDATE) BETWEEN TO_DATE('"+fromdate+"','DD-MM-YY') AND TO_DATE('"+todate+"','DD-MM-YY')";
				reportheader="From Date ,:,"+fromdate+",To Date ,:,"+todate+",Bin ,:,"+bindesc;
			}	
			String cnt_txn_list = "",txn_list_code="",keyDesc="";int addedlist = 0;List combinedlist= new ArrayList();
			for( int cnt=0; cnt<txn_list.length; cnt++ ){
				txn_list_code = txn_list[cnt];
				if( cnt == txn_list.length-1 ){
					cnt_txn_list += "'"+txn_list[cnt]+"'";
					keyDesc += commondesc.getTransactionDesc(instid,txn_list_code, jdbctemplate);
				}else{
					cnt_txn_list += "'"+txn_list[cnt]+"',";
					keyDesc += commondesc.getTransactionDesc(instid,txn_list_code, jdbctemplate)+" - ";
				}	
				//String vastxn_qury = "SELECT CHN,TXNDESC,ACCEPTORNAME AS MERCHANTNAME,TERMLOC,TXNAMOUNT,to_char(TRANDATE,'DD-MON-YY') as TRANDATE,REFNUM,TO_CHAR(OPTYPE) AS OPTYPE FROM IFP_PL_TXN WHERE INST_ID='"+instid+"' AND ( TXNCODE LIKE '83%' OR TXNCODE LIKE '84%' ) AND ENTRYTYPE='"+glkeys_code+"' AND TRANDATE BETWEEN TO_DATE('"+fromdate+"','DD-MM-YYYY') AND TO_DATE('"+todate+"','DD-MM-YYYY') ORDER BY  PLSEQNO DESC";
				String vastxn_qury = "SELECT TO_CHAR(CHN) AS CHN, TO_CHAR(TXNDESC) AS TXNDESC,  TO_CHAR(TRANDATE, 'DD-MON-YYYY') ||' ' || SUBSTR(LPAD(TRANTIME,6,0),1,2)||':'||SUBSTR(LPAD(TRANTIME,6,0),3,2)||':'||SUBSTR(LPAD(TRANTIME,6,0),5,2) AS TRANDATE , TO_CHAR(TERMINALID) AS TERMINALID, TO_CHAR(REFNUM) AS REFNUM, TO_CHAR(TRACENO) AS TRACENO,TO_CHAR(TXNTYPE) AS TXNTYPE, TO_CHAR(AMOUNT) AS AMOUNT, TO_CHAR(SETTCURRENCY) AS SETTCURRENCY  FROM IFP_TRANSACTION_MASTER WHERE INST_ID='"+instid+"' AND TXNCODE='"+txn_list_code+"' "+datecond+"  "+ bincond;
				trace("OTHERS  vastxn_qury====> "+vastxn_qury );				
				enctrace(" vastxn_qury "+vastxn_qury);
				List vastxn_qury_result = jdbctemplate.queryForList(vastxn_qury);
				trace("List vastxn_qury_result --> "+vastxn_qury_result.size());
				if(!vastxn_qury_result.isEmpty()){
					ListIterator txnItr = vastxn_qury_result.listIterator();
					BigDecimal bigtransamt;		
					while(txnItr.hasNext()){
						LinkedHashMap txnMap = (LinkedHashMap)txnItr.next();
						String txntype = (String)txnMap.get("TXNTYPE");
						bigtransamt    = new BigDecimal((String)txnMap.get("AMOUNT"));
						System.out.println("bigdecimal amount --- "+bigtransamt);
						String setcurrency    = (String)txnMap.get("SETTCURRENCY");
						if(txntype.equals("DR")){ // if DR minus  TXN amount
							summarytotal = summarytotal.subtract(bigtransamt);							
						} else{															
							summarytotal = summarytotal.add(bigtransamt);		
						}
						d= commondesc.currencyFormatter(setcurrency, jdbctemplate);
						summary = d.format(summarytotal);
						txnMap.remove("TXNDESC");
						txnMap.remove("SETTCURRENCY");
						txnMap.remove("TRANTIME");
						txnItr.remove();
						txnItr.add(txnMap);				
					}
					addedlist=1;
				}
				combinedlist.add(vastxn_qury_result);
			}if(submit.equals("Excel")){	
				if(addedlist!=0){					
					return getExcelReport(combinedlist,keyDesc,downloadname);
				}else{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "No records found");
					return "globalreporterror";
				}					
			}else{
				if(addedlist!=0){
					String propertyname ="prepaidtxnreport";
					List headerlist= new ArrayList();
					//return getPdfReport(combinedlist,keyDesc,reportheader,propertyname,downloadname,summary);
					return getPdfReport(combinedlist,keyDesc,reportheader,propertyname,downloadname, jdbctemplate);
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
		return "itextpdfreport";		 
	}	
	private String vas_report_name;		
	public String getVas_report_name() {
		return vas_report_name;
	}
	public void setVas_report_name(String vas_report_name) {
		this.vas_report_name = vas_report_name;
	}
	public String getPdfReport(List combinedlist,String keyDesc,String reporttableheader,String Propertyname,String pdfname, JdbcTemplate jdbctemplate) throws MalformedURLException, DocumentException, IOException{
		trace("**************** PDF VAS/Transaction Details report ****************");
		enctrace("**************** PDF VAS/Transaction Details report ****************");
		vas_report_name=pdfname+"_"+getDateTimeStamp()+".pdf";
		System.out.println("vas_report_name === "+vas_report_name);
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
		//String reporttableheader="From Date,:,"+fromdate+",To Date,:,"+todate;		
		pdfgen.addSingleHeader(document, 3, reporttableheader, ALIGN_LEFT, 70);
		trace("combinedlist --- "+combinedlist);
		Iterator combined_itr = combinedlist.iterator();
		int i=0;String pdftitle = null;
		while(combined_itr.hasNext()){
			List lst = (List) combined_itr.next();
			trace("LIST --- >  "+lst);
			String[] keydesc_split = null;
			if(keyDesc.contains(" - ")){
				keydesc_split = keyDesc.split(" - ");
				pdftitle= keydesc_split[i];
			}else{
				pdftitle = keyDesc;
				System.out.println("pdftitle --- "+pdftitle);
			}if(!lst.isEmpty()){
				System.out.println("entred if the value of pdftitle --- "+pdftitle);
				pdfgen.addPDFTableWithTitle(document, pdftitle+" :", pdfgen.reportheader, lst, pdfgen.reportsumfield, ALIGN_LEFT, ALIGN_CENTER, 100);
				/*To get Total amount
				 * if(!summarytotal.equals("")){
					pdfgen.addPDFTitles(document," Total Amount : "+summarytotal, ALIGN_RIGHT);
				}*/
			}else{
				pdfgen.addPDFTitles(document,keydesc_split[i]+" :",ALIGN_LEFT);
				pdfgen.addPDFTitles(document,"No records found",ALIGN_CENTER);
			}
				i++;
		}			 					
		pdfgen.closePDF(document);
		setReport_name(pdfgen.filename+strdate+".pdf");
		input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
		output_stream.flush();
 		trace("\n\n");
 		enctrace("\n\n");
 		return "itextpdfreport";
	}	
	private String transctionreport;
	public String getTransctionreport() {
		return transctionreport;
	}
	public void setTransctionreport(String transctionreport) {
		this.transctionreport = transctionreport;
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
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not generate report");
			}
			return "globalreporterror";
		}	
	private List productlist;	
	public List getProductlist() {
		return productlist;
	}
	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}
	public String corporateReport(){
		trace("********* corporate report Home ***********");enctrace("********* corporate report Home ***********");
		HttpSession session = getRequest().getSession();
	 
		String instid = comInstId(session);
		List prod_list;
		try {
			prod_list = commondesc.getProductList(instid,jdbctemplate, session);
			if (!(prod_list.isEmpty())){
				setProductlist(prod_list);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+prod_list);
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}		
		return "corporateTxnReport";	
	}
	public String genCorporateReport(){
		trace("Corporate report begins...");
		enctrace("Corporate report begins ...");
		HttpSession session = getRequest().getSession();
	 
		String instid = comInstId(session);
		String productid = getRequest().getParameter("productid");
		String proddesc = commondesc.getProductdesc(instid, productid, jdbctemplate);
		try{
			String submit = getRequest().getParameter("getreport");
			List prod_list = commondesc.getProductList(instid,jdbctemplate, session);
			String downloadname = "corporateReport";
/*			String keyDesc = null;
 * 			if (!(prod_list.isEmpty())){
				Iterator itr = prod_list.iterator();
				while (itr.hasNext()) {
					Map type = (Map)itr.next();		
					keyDesc    = (String)type.get("CARD_TYPE_NAME");
				}
			}*/
			List combinedlist= new ArrayList();String mblnum = "--";String cusname = "--";
			List gencorp_list = txndao.corporateList(productid,instid,jdbctemplate);
			System.out.println("Getting corporate list.... "+gencorp_list);
			if(gencorp_list.isEmpty()){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No records found");
				return "globalreporterror";
			}else{
				Iterator corpitr = gencorp_list.iterator();
				while (corpitr.hasNext()) {
					Map corpmap = (Map)corpitr.next();		
					String chn = (String)corpmap.get("CARDNO");
					trace("got chn ..."+chn);
					String cinnumber = txndao.getCinByCard(instid,chn,jdbctemplate);
					trace("got cinnumber..."+cinnumber);
					List cusname_contactnumber = txndao.fchCustNameMblNum(instid,cinnumber,jdbctemplate);
					Iterator name_mblitr = cusname_contactnumber.iterator();
					while (name_mblitr.hasNext()) {
						Map type = (Map)name_mblitr.next();		
						mblnum = (String)type.get("MOBILE_NO");
						System.out.println("mblnum --- "+mblnum);
						if(mblnum==null){
							mblnum="--";
						}
						cusname = (String)type.get("NAME");
						if(cusname==null){
							cusname="--";
						}
					}
					corpmap.put("CUSTNAME", cusname);
					corpmap.put("CONTACTNUMBER", mblnum);							
				}
				combinedlist.add(gencorp_list);
				
			}if(submit.equals("EXCEL")){	
				return getExcelReport(combinedlist,proddesc,downloadname);
			}else{
				String propertyname ="corporatereport";
				List headerlist= new ArrayList();
				String reportheader="Product ,:,"+proddesc;
				return getPdfReport(combinedlist,proddesc,reportheader,propertyname,downloadname, jdbctemplate);
			}
		}catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error: "+e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "itextpdfreport";
	}
}
