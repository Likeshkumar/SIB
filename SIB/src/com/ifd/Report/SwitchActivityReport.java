package com.ifd.Report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import com.ifp.Action.BaseAction;
import com.ifp.Action.ReportgenerationAction;
import com.ifp.beans.ReportActionBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.PDFReportGenerator;
import com.itextpdf.text.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import connection.SwitchConnection;
import com.ifg.Config.padss.PadssSecurity;



public class SwitchActivityReport extends BaseAction{
	CommonDesc commondesc = new CommonDesc();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	ReportActionBeans reportbean = new ReportActionBeans();
	PadssSecurity padsssec = new PadssSecurity();
	SwitchActivityReportDAO swtdao = new SwitchActivityReportDAO();
	
	public ReportActionBeans getReportbean() {
		return reportbean;
	}


	public void setReportbean(ReportActionBeans reportbean) {
		this.reportbean = reportbean;
	}
	public SwitchActivityReportDAO getSwtdao() {
		return swtdao;
	}

	public void setSwtdao(SwitchActivityReportDAO swtdao) {
		this.swtdao = swtdao;
	}

	public HttpSession getSession() {
		return session;
	}


	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	String auditreportname ;
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
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	public String getAuditreportname() {
		return auditreportname;
	}


	public void setAuditreportname(String auditreportname) {
		this.auditreportname = auditreportname;
	}
	private String report_name;
	public String getReport_name() {
		return report_name;
	}
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	
	public String transactionReport(){
		return "transactionreporthome";
	}
	
	
	public String transactionViewReport() throws Exception
	{
		trace("inside TransactionviewReport method");
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String propertyname ="TransactionReport";
		HttpSession session = getRequest().getSession();
		String instid =comInstId(session);
		DateFormat dateFormat =	new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date date 			=	new Date();
		String curdatetime	=	dateFormat.format(date);
		String REPORTTYPE = getRequest().getParameter("REPORTTYPE");				
		String selectedtype = getRequest().getParameter("selectedtype").trim();
		trace("selected type" + selectedtype);
		String searchtxnvalue=getRequest().getParameter("searchtxnvalue").trim();
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		
		String keyid = "";
		String EDMK="", EDPK="";
			
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			//System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			//System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String eDMK = ((String)map.get("DMK"));
					String eDPK = ((String)map.get("DPK"));
					hcardno = padsssec.getHashedValue(searchtxnvalue+instid);
				}      
				}

		System.out.println("selectedtype....."+searchtxnvalue);
		System.out.println("hcardno....."+hcardno);
		
		//String cardno = getRequest().getParameter("cardno").trim();
		//String txndate = getRequest().getParameter("txndate").trim();
		//String traceno = getRequest().getParameter("tracenoval").trim();
		//String txnrefno = getRequest().getParameter("txnrefno").trim();
		 
		String searchcond = "";
		if( selectedtype.equals("cardnum")){
			//hcardno = padsssec.getHashedValue(selectedtype+instid);
			trace("hashed value is " +hcardno);
			String cardno = hcardno.toString();
			searchcond += " TCHN like '%"+cardno+"'";
		}	
		
		if( selectedtype.equals("txndate")){ 
			searchcond += "  TRANDATE=TO_DATE('"+searchtxnvalue+"','dd-mm-yyyy')";
		}
		
		if( selectedtype.equals("traceno")){  
			searchcond += " TRACENO='"+searchtxnvalue+"'";
		}
		
		if( selectedtype.equals("txnrefno")){   
			searchcond += " REFNUM='"+searchtxnvalue+"'";
		}
		
		trace("search condition" + searchcond);
		//searchcond += "  ORDER BY TXNLOGSEQNO DESC";
		trace("Getting records....");
		
		
		
		
		Connection conn = null;
		ResultSet rs = null;
		//PreparedStatement ps = null;
		
		//String reportheader=" From Date , : , "+fromdate +", To Date , : , "+todate +", Card Number , : ,"+cardno+ ", ";
		String reportheader= selectedtype;
		int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
		String reportname = "Transaction History_";
		
		try {
			 
			trace(" Qry Condition "+searchcond);
			
			SwitchConnection swhconn = new SwitchConnection();
			conn = swhconn.getConnection();
			
			String recordlist = "SELECT CHN, TRACENO, TXNCODE, NVL(REFNUM,'--') as REFNUM, ";
			recordlist += " to_char(TRANDATE,'dd-mon-yyyy ') || ' ' ||  (substr(lpad(trantime,6,0),0,2) || ':' || substr(lpad(trantime,6,0),3,2) || ':' ||  substr(lpad(trantime,6,0),5,2)) as TRANDATE ";
			recordlist +=", AMOUNT, NVL(DECODE(ACCEPTORNAME,'000000000000000', 'WEB-APP', ACCEPTORNAME ) , '--') as ACCEPTORNAME, " ;
			recordlist += " TO_CHAR(RESPCODE) as RESPCODE,TO_CHAR(REASONCODE) AS REASONCODE  FROM  EZTXNLOG ";
			recordlist += " WHERE "+ searchcond;
			enctrace( "txnqry : " + recordlist);
			
			trace("select qry "+recordlist);

			PreparedStatement ps = conn.prepareStatement(recordlist);
			
			rs = ps.executeQuery(recordlist);
			
			trace("resultset row list length ========= "+rs.getRow());
			
			if(!rs.next()){
				addActionError("No Records Found");
				trace("No Records Found");
				return "transactionreporthome";
			}
			
				
				String chn="";
				String traceno="";
				String txncode="";
				String refnum="";
				String txndate="";
				String amount="";
				String acceptorname="";
				String respcode="";
				String reasoncode="";
				String rescode="";
				String reason="";
				String txndesc="";
				
				List finalreportlist = new ArrayList();
				
				
				while(rs.next()){
					trace("Inside while loop");
					chn = (String)rs.getString("CHN");
					traceno = (String)rs.getString("TRACENO");
					txndesc = (String)rs.getString("TXNCODE");					
					refnum = (String)rs.getString("REFNUM");
					txndate = (String)rs.getString("TRANDATE");
					amount = (String)rs.getString("AMOUNT");
					acceptorname = (String)rs.getString("ACCEPTORNAME");
					rescode = (String)rs.getString("RESPCODE");					
					reason = (String)rs.getString("REASONCODE");
					
					
					respcode = getRespDesc(rescode,conn);
					txncode = getTransdesc(txndesc,conn);
					reasoncode = getReasonDesc(reason,conn);
					 
					 Map mp = new LinkedHashMap();
					 
					 mp.put("CHN", chn);
					 mp.put("TRACENO", traceno);
					 mp.put("TXNCODE", txncode);
					 mp.put("REFNUM", refnum);
					 mp.put("TXNDATE", txndate);
					 mp.put("AMOUNT", amount);
					 mp.put("ACCEPTORNAME", acceptorname);
					 mp.put("RESPCODE", respcode);
					 mp.put("REASCONCODE", reasoncode);
					 finalreportlist.add(mp);
				}
				
			if (REPORTTYPE.equals("PDFREPORT")){
						
				PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertyname, getRequest());
				String title = "Transaction Report";
				pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
				
				trace("Report Name is "+reportname+curdatetime+".pdf");
				setReport_name(reportname+curdatetime+".pdf");
				
				pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
				pdfgen.createSimplePDF(document,title, pdfgen.reportheader,finalreportlist,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);							
				pdfgen.closePDF(document);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush(); 
				return "itextpdfreport";
				
			}else{
				 
				return ExcelReportGenMaster(finalreportlist);  
				
			}
		} catch (Exception e) {
			addActionError("Unable to continue the report generation ");
			trace("Exception : " + e.getMessage());
			try {
				output_stream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			e.printStackTrace();
		}
		finally{
			conn.close();
			trace("ResultSet and Connection are Closed");
		}
		
		return "transactionreporthome";
	}
	
	private String getReasonDesc(String reasoncode, Connection conn) {
		String result = null;
		try {
			String selectqry="SELECT TO_CHAR(REASONDESCRIPTION) as REASONDESCRIPTION  FROM EZREASONCODE WHERE REASONCODE='"+reasoncode+"'";
		    PreparedStatement ps = conn.prepareStatement(selectqry);
		    ResultSet rs = ps.executeQuery(selectqry);
		    while(rs.next()){
		    	result = (String)rs.getString("REASONDESCRIPTION");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	 
	private String getRespDesc(String respcode, Connection conn) {
		String res=null;
		try {
			String selectqry="SELECT TO_CHAR(DESCRIPTION) as DESCRIPTION  FROM EZRESPCODE WHERE RESPCODE='"+respcode+"'";
		    PreparedStatement ps = conn.prepareStatement(selectqry);
		    ResultSet rs = ps.executeQuery(selectqry);
		    while(rs.next()){
		    	res = (String)rs.getString("DESCRIPTION");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res ;
	} 
	
	
	private String getTransdesc(String transdesc, Connection conn) {
		String result = null;
		try {
			String selectqry="SELECT TO_CHAR(TXNDESC) as TXNDESC  FROM EZTXNDESC WHERE TXNCODE='"+transdesc+"'";
		    PreparedStatement ps = conn.prepareStatement(selectqry);
		    ResultSet rs = ps.executeQuery(selectqry);
		    while(rs.next()){
		    	result = (String)rs.getString("TXNDESC");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String generatePinManageReport() throws SQLException{
		trace("inside generate method");
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String propertyname ="pinmanagementreport";
		HttpSession session = getRequest().getSession();
		String instid =comInstId(session);
		DateFormat dateFormat =	new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date date 			=	new Date();
		String curdatetime	=	dateFormat.format(date);
		String cardno = getRequest().getParameter("cardno");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		
		Connection conn = null;
		ResultSet rs = null;
		//PreparedStatement ps = null;
		
		String querycondition = "";
		String reportheader=" From Date , : , "+fromdate +", To Date , : , "+todate +", Card Number , : ,"+cardno+ ", ";
		
		int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
		String reportname = "SwitchActivity_";
		
		try {
			if( cardno != null ){
			    hcardno = padsssec.getHashedValue(cardno+instid);
			    trace("hashed value is " +hcardno);
			    //cardno = hcardno.toString();
				querycondition = " AND TCHN ='"+hcardno+"' ";
			}
			
			//querycondition += " AND TRUNC(ACTIONDATE) BETWEEN TO_DATE('"+fromdate+"','dd-mm-yyyy') AND TO_DATE('"+todate+"','dd-mm-yyyy') ";
			
			
			trace(" Qry Condition "+querycondition);
			
			SwitchConnection swhconn = new SwitchConnection();
			conn = swhconn.getConnection();
			
			String recordList = "SELECT DISTINCT TRANDATE AS PINCHANGEDATE,(SELECT  COUNT(TRANDATE) FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=0) AS NO_OF_PINCHANGED,"
+"(SELECT  COUNT(TRANDATE) FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=55) AS NO_OF_WRONGPIN,"
+"(SELECT  TRANDATE FROM  EZTXNLOG WHERE TCHN = B.TCHN AND RESPCODE=75) AS PINLOCKDATE "
+"FROM EZTXNLOG B WHERE TXNCODE='940006' AND MSGTYPE='210'" +querycondition;
			
			trace("select qry "+recordList);

			PreparedStatement ps = conn.prepareStatement(recordList);
			
			rs = ps.executeQuery(recordList);
			
			trace("resultset row list length ========= "+rs.getRow());
			
			if(!rs.next()){
				addActionError("No Records Found");
				trace("No Records Found");
				return "pinmanagementhome";
			}
			
			PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertyname, getRequest());
			String title = "Pin Management Report";
			pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
			
			trace("Report Name is "+reportname+curdatetime+".pdf");
			setReport_name(reportname+curdatetime+".pdf");
			
			pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
			
			String pinchangedate="";
			String nofopinchange="";
			String noofwrongpin="";
			String pinblockdate="";
			 
			List finalreportlist = new ArrayList();
			
			
			while(rs.next()){
				trace("Inside while loop");
				 pinchangedate = (String)rs.getString("PINCHANGEDATE");
				 nofopinchange = (String)rs.getString("NO_OF_PINCHANGED");
				 noofwrongpin = (String)rs.getString("NO_OF_WRONGPIN");
				 pinblockdate = (String)rs.getString("PINLOCKDATE");
				 
				 
				 Map mp = new LinkedHashMap();
				 
				 mp.put("CHN", cardno);
				 mp.put("PINCHADATE", pinchangedate);
				 mp.put("NOOFPINCHANGED", nofopinchange);
				 mp.put("NOOFWRONGPIN", noofwrongpin);
				 if(pinblockdate == null){
					 mp.put("PINLODATE", "NA");
				 }else{
					 mp.put("PINLODATE", pinblockdate);
				 }
				 
				 finalreportlist.add(mp);
			}
			
			
			
			pdfgen.createSimplePDF(document,title, pdfgen.reportheader,finalreportlist,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);							
			pdfgen.closePDF(document);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
			output_stream.flush(); 
			return "itextpdfreport"; 	
		} catch (Exception e) {
			addActionError("Unable to continue the report generation ");
			trace("Exception : " + e.getMessage());
			try {
				output_stream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			e.printStackTrace();
		}
		finally{
			conn.close();
			trace("ResultSet and Connection are Closed");
		}
		
		return "pinmanagementhome";
	}
	
	
	
	public String cardActivityHome(){
		return "cardactivityhome";
 	}
   public String searchCardAct() throws SQLException
   {
	 trace("reportGenHome started ...");
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId(session);
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String propertyname ="cardactivityreport"; 
		String cardno = getRequest().getParameter("cardno");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		StringBuffer hcard  = new StringBuffer();
		String hcquery="";
		String newcard = "";
		Connection con = null;
		ResultSet rs = null;
		SwitchConnection swhcon = new SwitchConnection();
		con = swhcon.getConnection();
		try{
		    hcard=padsssec.getHashedValue(cardno+instid);
		    trace("hashed value is==="+hcard);
		    hcquery="SELECT CHN AS CARD_NO FROM EZTXNLOG WHERE TCHN = '"+hcard+"'";
		    PreparedStatement pscard = con.prepareStatement(hcquery);
		    rs = pscard.executeQuery(hcquery);
		    if(rs.next())
		    {
		    	newcard = rs.getString("CARD_NO");
		    	System.out.println("card n0 is"+newcard);
		    	trace("the new card no is in txnlog==="+newcard);
		    }
		    else
		    {
		    	trace("Card No. is not available");
		    	session.setAttribute("errmsg", "E");
		    	session.setAttribute("E", "Card No. is not available");
		    	addActionError("Card No. is not available");
		    	return "required_home";
		    }
		    }
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		cardno = newcard.toString();
		trace("converted card no in eztxnlog=="+cardno);
		String reportname = "Card_Activity_Report_";
		int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;
		String reportheader = "";
		try
		{
			
			reportheader = "Card no :"+cardno;
			reportname += cardno+"_"+fromdate+"_"+todate;
		    String carddir = "SELECT COUNT(*) AS ORIENT_ATM_WITHDRAWAL FROM EZTXNLOG WHERE CHN='"+cardno+"' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='627443' AND ISSUERID='627443'";
		    //trace("carddir===="+carddir);
		    String carddirec="";
		    String dis = "";
		    PreparedStatement ps1 = con.prepareStatement(carddir);
		    ps1 = con.prepareStatement(carddir);
			rs = ps1.executeQuery(carddir);
		    if(rs.next())
			{
		    	dis = rs.getString("ORIENT_ATM_WITHDRAWAL");
		    	//System.out.println("carddirec=="+dis);
		    }
		    else
		    {
		    	trace("No Orient ATM Withdrawal can be done");
		    	carddirec = "Total No. Of Transaction in Orient ATM "+carddirec;
		    }
		    rs.close();
		    carddirec = "Total No. Of Transaction in Orient ATM "+dis;
		    trace("Orient ATM Withdrawal==="+carddirec);
		    
		    String carddir1 = "SELECT COUNT(*) AS VISA_ATM_WITHDRAWAL FROM EZTXNLOG WHERE CHN='"+cardno+"' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='432283' AND ISSUERID='627443'";
		    String carddir2="";
		    String dis1="";
		    //PreparedStatement ps2 = con.prepareStatement(carddir1);
		    ps1= con.prepareStatement(carddir1);
			rs = ps1.executeQuery(carddir1);
		    if(rs.next())
			{
		    	dis1=rs.getString("VISA_ATM_WITHDRAWAL");
		    	//System.out.println("VISA_ATM_WITHDRAWAL==="+dis1);
		    }
		    else
		    {
		    	trace("No Visa ATM Withdrawal can be done");
		    	carddir2 = "Total No. of Transaction in VISA ATM"+carddir1;
		    }
		    rs.close();
		    carddir2 = "Total No. of Transaction in VISA ATM"+dis1;
		    trace("Visa ATM Withdrawal==="+dis1);
		    
		    String cardswitch = "SELECT COUNT(*) AS INTERSWITCH_ATM_WITHDRAWAL FROM EZTXNLOG WHERE CHN='"+cardno+"' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011' AND ACQUIRERID='900001' AND ISSUERID='627443'";
		    String cardswit = "";
		    String dis2="";
		    //PreparedStatement ps3 = con.prepareStatement(cardswitch);
		    ps1 = con.prepareStatement(cardswitch);
			rs = ps1.executeQuery(cardswitch);
		    if(rs.next())
			{
		    	dis2=rs.getString("INTERSWITCH_ATM_WITHDRAWAL");
		    	//System.out.println("INTERSWITCH_ATM_WITHDRAWAL=="+dis2);
		    }
		    else
		    {
		    	trace("No Inter Switch ATM Withdrawal can be done");
		    	cardswit = "Total No. of Transaction in InterSwitch ATM Withdrawal"+cardswitch;
		    }
		    rs.close();
		    cardswit = "Total No. of Transaction in InterSwitch ATM Withdrawal"+dis2;
		    trace("Inter Switch ATM Withdrawal==="+dis2);
		    
		    String totaldraw = "SELECT COUNT(*) AS TOTAL_WITHDRAWAL FROM EZTXNLOG WHERE CHN='"+cardno+"' AND TXNCODE IN ('11000','12000') AND MSGTYPE='210' AND RESPCODE=0 AND MERCHANTTYPE='6011'";
		    String totalwith = "";
		    String dis3 = "";
		    //PreparedStatement ps4 = con.prepareStatement(totaldraw);
		    ps1 = con.prepareStatement(totaldraw);
			rs = ps1.executeQuery(totaldraw);
		    if(rs.next())
			{
		    	dis3=rs.getString("TOTAL_WITHDRAWAL");
		    	//System.out.println("TOTAL_WITHDRAWAL"+dis3);
		    }
		    else
		    {
		    	trace("No Transaction Withdrawal can be done");
		    	totalwith = "Total No. of Cash Withdrawal "+totaldraw;
		    }
		    rs.close();
		    totalwith = "Total No. of Cash Withdrawal "+dis3;
		    trace("Total Withdrawal==="+dis3);
		    
		    String postxn = "SELECT COUNT(*) AS NO_OF_POSTXNS FROM EZTXNLOG WHERE CHN='"+cardno+"' AND DEVICETYPE='POS' AND RESPCODE=0";
		    String pos = "";
		    String dis4 = "";
		    //PreparedStatement ps5 = con.prepareStatement(postxn);
		    ps1 = con.prepareStatement(postxn);
			rs = ps1.executeQuery(postxn);
		    if(rs.next())
			{
		    	dis4=rs.getString("NO_OF_POSTXNS");
		    	//System.out.println("NO_OF_POSTXNS"+dis4);
		    }
		    else
		    {
		    	trace("No POS Machine Transaction can be done");
		    	pos = "Total No. of POS Transaction"+postxn;
		    }
		    rs.close();
		    pos = "Total No. of POS Transaction"+dis4;
		    trace("POS Transaction==="+dis4);
		    
		    String onlineTrans = "SELECT COUNT(*) AS NO_OF_ONLINETXNS FROM EZTXNLOG WHERE CHN='"+cardno+"' AND POSENTRYCODE='100' and MSGTYPE='210' and RESPCODE=0";
		    String online = "";
		    String dis5 = "";
		    //PreparedStatement ps6 = con.prepareStatement(onlineTrans);
		    ps1 = con.prepareStatement(onlineTrans);
			rs = ps1.executeQuery(onlineTrans);
		    if(rs.next())
			{
		    	dis5=rs.getString("NO_OF_ONLINETXNS");
		    	//System.out.println("NO_OF_ONLINETXNS"+dis5);
		    }
		    else
		    {
		    	trace("No Online Transaction can be done");
		    	online = "Total No. of Online Transaction"+onlineTrans;
		    }
		    rs.close();
		    online = "Total No. of Online Transaction"+dis5;
		    trace("online Transaction==="+dis5);
		    PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertyname, getRequest());
			String title = "Card Activity Report";
			pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
			
			setAuditreportname(reportname+".pdf");
			
			
			ReportgenerationAction excel = new ReportgenerationAction();
			
			Map map = new LinkedHashMap<>();
			map.put("CARDNO",cardno);
			map.put("ORIENTATM", carddirec);
			map.put("VISAATM", carddir2);
			map.put("INTERSWITCHATM", cardswit);
			map.put("CASHWITHDRAW", totalwith);
			map.put("POSMACHINE", pos);
			map.put("ONLINETRANS", online);
			
			List recordslist = new ArrayList<>();
			recordslist.add(map);
			
			
			 
			if (reportheader.endsWith(",")) {
				reportheader =reportheader.substring(0, reportheader.length() - 1);
			}
		    //pdfgen.addPDFSummary(document, "",pdfgen.reportheader, recordslist, title, 100);
			pdfgen.createSimplePDF(document,title, pdfgen.reportheader,recordslist,pdfgen.reportsumfield, ALIGN_CENTER, ALIGN_CENTER, 100);	
			pdfgen.closePDF(document);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
			output_stream.flush(); 
			return "itextpdfreport";  
		}catch(Exception e ){
			addActionError("Unable to continue the report generation ");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}	
         finally{
        	 con.close();
         }
	
		
	return "cardactivityhome";	
 }
   public String accountStatusHome(){
		return "accountstatushome";
	}
	
   
   public String ExcelReportGenMaster(List excelReportList) throws IOException
	{
		trace("reportGenMaster started ...");
		//HttpSession session = getRequest().getSession();	
		String instid = getRequest().getParameter("INSTID");
		//String instid = (String)session.getAttribute("Instname");
		String reportno="3";
		
		System.out.println("reportno::"+reportno);   
		
		/*List repconfig = this.getReportConfigDetails(instid,reportno,jdbctemplate); 
		Iterator iterator=repconfig.iterator();
		String filename="",passwdReq="";
		while(iterator.hasNext())	{
			Map map = (Map)iterator.next();
			filename = (String)map.get("REPORTNAME");
			}*/
		
		List result = excelReportList;
		
		//String excelparam = this.getExcelParam(instid,reportno,jdbctemplate);
		String excelparam="sheet";
		String filename="Transaction Report_";
		List combinedlist= new ArrayList();
		combinedlist.add(result);   
		combinedlist.add(result);
		String keyDesc = "successful - unsuccessful";
		String res = this.getExcelReport(combinedlist,excelparam,filename);		
		System.out.println("reportGenMaster():::reportGenMaster"+res);
		return res;
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

	private String getExcelParam(String instid, String reportno,JdbcTemplate jdbctemplate2) {
		String xlparam = null;
		try {
			String xlparamqry= "select EXCEL_PARAM from PDFREPORTGENRATOR where RNO='"+reportno+"' ";
			enctrace("xlparamqry : " + xlparamqry );
			xlparam = (String)jdbctemplate.queryForObject(xlparamqry,String.class);
		} catch (EmptyResultDataAccessException e) { xlparam=null; }
		return xlparam;
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
public String getExcelReport(List listqry,String keyDesc,String namestr){
	trace("	 ***************** Transaction Details report ****************");
	enctrace("**************** Transaction Details report ****************");
	
	trace("listqry:::::"+listqry);
	trace(":::::keyDesc"+keyDesc);
	trace(":::::namestr"+namestr);     
	
	HttpSession session=getRequest().getSession();
		try {
				output_stream = new ByteArrayOutputStream();
			    String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			    trace("dateFormat.format String resp = getRespDesc(respcode,conn);(date)    --->  "+curdatetime);
			    String defaultname = namestr+curdatetime+".xls";
			    trace("default name"+ defaultname);
			HSSFWorkbook workbook = new HSSFWorkbook();						
			HSSFSheet sheet = null;
			trace(" keyDesc "+keyDesc);
			String keydesc_split[] = keyDesc.split("-");
			System.out.println("keydesc_split[] ==== "+keyDesc);
			//sheet = workbook.createSheet(keydesc_split[i]);
			
			Iterator combined_itr = listqry.iterator();
			int i=0,sheetcount=1;
			sheet = workbook.createSheet(keyDesc+sheetcount);
			while(combined_itr.hasNext()){
				List lst = (List) combined_itr.next();
				trace(" ----- lst  ---- "+lst.size());
				
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
					if(row.getRowNum() > 49999){
						trace("entered rownum ");
						int a = ++sheetcount;
						sheet = workbook.createSheet(keyDesc+a);
						rowno=0;
						continue;
					}
				}
				i++;
				trace("	value of i at the bottom --	"+i+"	rownum -- "+rowno);
			}				
			workbook.write(output_stream);String fromdate =  getRequest().getParameter("fromdate");
			trace("from date getting" + fromdate);
			String todate =  getRequest().getParameter("todate");
			trace("from date getting" + fromdate);
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
   
   
	private void setTransctionreport(String defaultname) {
	// TODO Auto-generated method stub
	
}


	public void generateAcctStatus() throws IOException{
		trace("****************Account Status Methos Begin**************");

		HttpSession session = getRequest().getSession();
		String instid =comInstId(session);
		JSONObject json = new JSONObject();
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		String querycondition = "";
		
		String cardno = getRequest().getParameter("cardno");
		trace("cardno is " +cardno.toString());
		json.put("CHN", cardno);
		
		//String tablerec = "";
		int count=0;
		try {
			if(cardno != null){
				hcardno = padsssec.getHashedValue(cardno+instid);
			    trace("hashed value is " +hcardno);
				querycondition = " HCARD_NO ='"+hcardno+"' ";
			}
			
			List recordlist = swtdao.getAccountStatusList(querycondition,jdbctemplate);
			
			if(recordlist.isEmpty()){
				json.put("RESP", 1);
				json.put("REASON", "NO RECOREDS FOUND");	
			}else{
				 
				ListIterator ltr = recordlist.listIterator();
				
				
				while(ltr.hasNext()){
					json.put("RESP", 0);
					
					Map mp = (Map) ltr.next();

					json.put("ORDER_REF_NUM", mp.get("ORDER_REF_NO"));
					json.put("ACCOUNT_NO", mp.get("ACCOUNT_NO"));
					json.put("ACCTTYPE_ID", mp.get("ACCTTYPE_ID") );
					
					count++;
				}
				
				json.put("COUNT", count);
				trace("Record Count is " +count);
			}
			
		} catch (Exception e) {
			addActionError("Unable to continue the process ");
			trace("Exception : " + e.getMessage());
			try {
				output_stream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			e.printStackTrace();
		}
		
		getResponse().getWriter().write(json.toString());
	}
	
	
   
}
