package com.ifp.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.Action.ReportgenerationAction;
import com.ifp.beans.ReportActionBeans;
import com.ifp.dao.ReportActionDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.PDFReportGenerator;
import com.itextpdf.text.Document;

public class ReportAction extends BaseAction {
	
	ReportActionDAO reportdao = new ReportActionDAO();
	ReportActionBeans reportbean = new ReportActionBeans();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	CommonDesc commondesc = new CommonDesc(); 
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


	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	
	public CommonDesc getCommondesc() {
		return commondesc;
	}


	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}


	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	
	public ReportActionBeans getReportbean() {
		return reportbean;
	}


	public void setReportbean(ReportActionBeans reportbean) {
		this.reportbean = reportbean;
	}


	public ReportActionDAO getReportdao() {
		return reportdao;
	}


	public void setReportdao(ReportActionDAO reportdao) {
		this.reportdao = reportdao;
	}


	public String getAuditreportname() {
		return auditreportname;
	}


	public void setAuditreportname(String auditreportname) {
		this.auditreportname = auditreportname;
	}

	
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	public String comUserId( HttpSession session ){
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	public String auditTraialReportHome(){
		reportbean.actionheadlist =  reportdao.getAuditActionHeadList(jdbctemplate);
		if( reportbean.actionheadlist.isEmpty() ){
			addActionError("Audit Action Code Not Configured.");
			return "required_home";
		}
		return "auditreport_home";
	}
	
	public void getsubActionList() throws IOException{
		String headcode = getRequest().getParameter("actionhead");
		 List subactlist = reportdao.getAuditActionList(headcode, jdbctemplate);
		 if( subactlist.isEmpty()  ){
			 return ;
		 }
		 Iterator itr = subactlist.iterator();
		 String opt = "";
		 while( itr.hasNext() ){
			 Map mp = (Map)itr.next();
			 opt += "<option value='"+(String)mp.get("AUDIT_ACTIONCODE")+"'> "+(String)mp.get("AUDIT_ACTIONDESC")+" </option>";
		 }
		 getResponse().getWriter().write(opt.toString());
	}
	
	public String generateAuditReport(){		
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String propertyname ="auditreport"; 
		HttpSession session = getRequest().getSession();
		String instid =comInstId(session);
		String loginuser = comUserId(session);
		String cardno = getRequest().getParameter("cardno");
		String cardprocess = getRequest().getParameter("cardprocess");
		
		String usercode = getRequest().getParameter("usercode");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		
		//String[] actionhead= getRequest().getParameterValues("actionhead");
		String[] subactionhead = getRequest().getParameterValues("actionhead");
		String orderby = getRequest().getParameter("orderby");
		String ascdesc = getRequest().getParameter("ascdesc");
		String querycondition = "";
		String orderbycondition = "";
		String reportheader=" From Date , : , "+fromdate +",";
		reportheader +=" To Date , : , "+todate +",";
		
		
		
		int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
		String reportname = "UserActivity_";
		try{
			
			//System.out.println("cardno :" + cardno );
			System.out.println("usercode :" + usercode );
			System.out.println("subactionhead :" + subactionhead );
			
			if( cardno != null ){
				PadssSecurity padss = new PadssSecurity();
				cardno = padss.getMakedCardno(cardno);
				System.out.println("Masked cardno===>"+cardno);
				
				reportheader="Card Number ,:,"+cardno+ ", ";
				reportname +=cardno+"_";
				querycondition = " AND  CARDNO = '"+cardno+"'";
			}
			
			if( usercode != null ){
				String username = commondesc.getUserNameFromTemp(instid, usercode, jdbctemplate);
				if( username == null ){ username = usercode; }
				
				reportheader +=  "User Name ,:,"+username+ ", ";
				String userid = commondesc.getUserIdFromTemp(instid, usercode, jdbctemplate);
				if( userid  == null ){ userid  = usercode; }
				querycondition += " AND (  USERCODE = '"+username+"' OR USERCODE ='"+userid+"' ) ";
				
				reportname +=username+"_";
			}
			
			if(cardprocess.equals("INST")){
				
			}
			String condition="";
			if( subactionhead != null ){
				reportheader +=  "Action ,:,Action Based , ";
				String subcond = "";
				for( int i=0; i<subactionhead.length; i++ ){ 
						subcond += subactionhead[i] +","; 
				}
				
				if (subcond.endsWith(",") && cardprocess.equals("INST")) {
					subcond = subcond.substring(0, subcond.length() - 1);
					
					
					 
                    if(subcond.equals("0101")){
                    	subcond="0201";
					}
				  else if(subcond.equals("0102")){
						subcond="0202";					
					}					
			   else if(subcond.equals("0103")){
					subcond="0203";
					}
			  else if(subcond.equals("0104")){
					subcond="0204";	
					}
			 else if(subcond.equals("0105")){
					subcond="0205";
					}
			 else if(subcond.equals("0106")){
					subcond="0206";
					
					}
                   
			 else if(subcond.equals("41")){//maintenace
					subcond="4107,4101,4102,4106,4103,4105";
					 condition="";
					}
			 else if(subcond.equals("30")){//configuration
					subcond="3040,30,3030,51,6060,7070,8080,3050,3060";
					 condition="";
					}
				}
				else{
					
					if (subcond.endsWith(",")) {
						subcond = subcond.substring(0, subcond.length() - 1);
					}
						
					
				}
				
				
				//querycondition += " AND (  AUDITACTCODE IN ("+subcond+") ) ";
				
				reportname +="ActionBased_";
			}
			
			querycondition += " "+condition+" AND TRUNC(ACTIONDATE) BETWEEN TO_DATE('"+fromdate+"','dd-mm-yyyy') AND TO_DATE('"+todate+"','dd-mm-yyyy') ";
			
			
			if( orderby != null ){
				if( orderby.equals("$ACTDATE")){
					orderbycondition = " ORDER BY ACTIONDATE "+ascdesc;
				}else  if( orderby.equals("$ACTIONWISE")){
						orderbycondition += " ORDER BY AUDITACTCODE "+ascdesc;
					 
				}
			}
			
			//System.out.println("reportheader :"+ reportheader);
			
			 
			
		 	//trace("Formed Condition : " + querycondition );
			//trace("Order By Condition : " + orderbycondition );
			List recordslist = reportdao.getAuditReport(instid,loginuser,querycondition,orderbycondition,jdbctemplate);
			//trace("Generating audit report...got : " + recordslist.size() );
			if( recordslist.isEmpty() ){
				addActionError("No Records Found");
			}
			PadssSecurity sec = new PadssSecurity();
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			//System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			String dcardno="",eDMK="",eDPK="";			
			if(!secList.isEmpty()){
				while(secitr.hasNext())        
				{
					Map map = (Map) secitr.next(); 
					eDMK = ((String)map.get("DMK"));
					eDPK = ((String)map.get("DPK"));
					
				}  
			}
			ListIterator itr = recordslist.listIterator();
			String dbuser = "";
			//String orgchn="";
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				dbuser = commondesc.getUserNameFromTemp(instid, (String)mp.get("USERCODE"), jdbctemplate);
				if( dbuser == null ){ dbuser = (String)mp.get("USERCODE"); }
				
				mp.put("USERCODE", dbuser);
				String cardno_new = (String)mp.get("CARDNO");				
				String auditmsg = (String)mp.get("AUDITMSG");
				trace("getting cardno in audit trial" + cardno_new);
				if (cardno_new.equals("--")|| cardno_new ==null){
					System.out.println("coming inside");
					mp.put("CARDNO","Card N/A");
					//mp.put("AUDITMSG","Remarks N/A");	
					mp.put("AUDITMSG",auditmsg);	
				}
				else{
					System.out.println("coming outside");
					//String orgchn = sec.getCHN(eDMK, eDPK,cardno_new);
					//String newauditmsg=auditmsg.replaceAll(cardno_new, orgchn);
					mp.put("CARDNO",cardno_new);
					mp.put("AUDITMSG",auditmsg);					
				}																							
				itr.remove();
				itr.add(mp);
			}
			
			
			
			PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertyname, getRequest());
			String title = "User Activity Report";
			pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
			
			setAuditreportname(reportname+".pdf");
			
			
			ReportgenerationAction excel = new ReportgenerationAction();
			
			
			 
			if (reportheader.endsWith(",")) {
				reportheader =reportheader.substring(0, reportheader.length() - 1);
			}
			pdfgen.addSingleHeader(document,3,reportheader,ALIGN_LEFT,50);
			
			
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
		return "auditreport_home";
	}
}
