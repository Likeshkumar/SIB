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

public class MasterReportGeneration extends BaseAction{
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
	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		//String instid = getRequest().getParameter("INSTID");
		String instid = (String)session.getAttribute("Instname");
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

	public String comUserCode(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	public String cardreporthome() throws Exception
	{
		System.out.println("cardreporthome method called ......");
		HttpSession session = getRequest().getSession();    
		List cardreportlist = dao.getActionList(jdbctemplate); 
		cardbean.setCardreportlist(cardreportlist);
		String instid = (String)session.getAttribute("Instname");
		List branchList = commondesc.generateBranchList(instid, jdbctemplate);
		cardbean.setBranchList(branchList);
		List productList =   commondesc.getProductListView(instid, jdbctemplate, session);
		cardbean.setProductList(productList);
		return "cardreporthome";
	}
	
	
	public void getsubActionList() throws IOException{
		String headcode = getRequest().getParameter("actionhead");
		String  action[] = getRequest().getParameterValues("actionhead");
		for ( int i=0; i<action.length;i++){
			System.out.println(action[i].toString());
		}        
		
		 List<Map<String,Object>> subactlist = dao.getAuditActionList(headcode,"PROCESS", jdbctemplate);
		 if( subactlist.isEmpty()  ){
			 return ;
		 }
		 Iterator itr = subactlist.iterator();
		 String opt = "";  
		 if(!subactlist.get(0).get("ACTION").equals("REGN")){
			 opt = "<option value='ALL'> ALL </option>";
		 }
		 while( itr.hasNext() ){
			 Map mp = (Map)itr.next();
			 opt += "<option value='"+(String)mp.get("ACTION")+"'> "+(String)mp.get("ACTION_DESC")+" </option>";
		 }
		 System.out.println("opt-->"+opt);
		 getResponse().getWriter().write(opt.toString());
	}
	
	public String generateCardReport() throws Exception
	{
		System.out.println("getCardReport method called .......");
		String reporttype = getRequest().getParameter("REPORTTYPE");
		String branchlist = getRequest().getParameter("branchlist");
		String productCode = getRequest().getParameter("productCode");
		String  subaction = getRequest().getParameter("subaction");
		String  actionhead = getRequest().getParameter("actionhead");
		
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		System.out.println("--instid -- "+instid);
		String fromdate =  getRequest().getParameter("fromdate");
		//System.out.println("From date ===>"+fromdate);
		//session.setAttribute("fromdate", fromdate);
		String todate =  getRequest().getParameter("todate");
		//session.setAttribute("todate",todate );
		String cond = "",tableName="";
		//enctrace("2:Got Condition::::"+cond);
		String cardprocess = getRequest().getParameter("cardprocess");
		trace("subaction"+subaction+"actionhead0"+actionhead);
		instid="SIB";
		
		String condition="";
		  if(actionhead.equals("CUSTMAP") ){
			  String condi="";
				  if(!branchlist.equals("ALL")){
				   condi="AND TRIM(CARD_COLLECT_BRANCH)='"+branchlist+"' ";
				  }else{condi="";}
				  
					  if(subaction.equals("CUSTMAPNOT")){
					    condition=" SELECT CARD_COLLECT_BRANCH,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Customer Registration','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='05' "+condi+" AND MKCK_STATUS='P'  AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
					   }else if(subaction.equals("CUSTMAP")){
					    condition="SELECT CARD_COLLECT_BRANCH,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For  Customer Registration Authorization','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='06'  "+condi+" AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
					  
					   }
					   else{
	//ALL Records				
						  	  
						   condition=" SELECT CARD_COLLECT_BRANCH,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Customer Registration','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='05' "+condi+" AND MKCK_STATUS='P'  AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') UNION ALL SELECT CARD_COLLECT_BRANCH,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For  Customer Registration Authorization','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='06'  "+condi+" AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
						   //trace("dgdfggggggggggg"+condition);
					    }
			  }else{

		 condition = this.getReportCondition(subaction,actionhead,instid,fromdate,todate,cardprocess,branchlist);
		 enctrace("report under process" +reporttype+"\n actionhead"+actionhead+"\n cardprocess"+cardprocess+"\n condition"+condition);

	
		
		
	   
		

		//condition = cond;
		if(!branchlist.equals("ALL")){
		condition = condition.replaceAll("union all", " AND BRANCH_CODE='"+branchlist+"' union all ")+" AND BRANCH_CODE='"+branchlist+"'";    
		trace("4: Adding brach List ::::"+condition);    
		}
		
		if(!productCode.equals("ALL")){
			condition = condition.replaceAll("union all", " AND PRODUCT_CODE='"+productCode+"' union all ")+" AND PRODUCT_CODE='"+productCode+"'"; 
			trace("4: productCode ::::"+productCode);    
		}
			  }
		if(reporttype.equals("EXCEL"))
		{
			String Query = condition;       
			enctrace("Query WITH EXCEL Condition"+Query);   
			PadssSecurity sec = new PadssSecurity();
				CommonDesc commondesc = new CommonDesc();
				String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				//System.out.println("keyid::"+keyid);
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
				List cardList = dao.getMasterReportList(instid,eDMK,eDPK,sec,Query, jdbctemplate);
			
			return ExcelReportGenMaster(subaction,cardList);  
		}
		else 
		{
		return reportGenMaster(subaction,actionhead,branchlist,productCode,cardprocess);
		}
	}
	
	/*
	public String generateCardReport() throws IOException
	{
		System.out.println("getCardReport method called .......");   
		String  subaction[] = getRequest().getParameterValues("subaction");
		String cond = "";
		for ( int i=0; i<subaction.length;i++){
			System.out.println(subaction[i].toString());
			if(subaction[i].toString().equals("$CARDGENAUTH"))
			{
				cond = cond + " OR (CARD_STATUS='01' and mkck_status='M')";
			}
			if(subaction[i].toString().equals("$SECREG"))
			{ cond = cond + " OR (CARD_STATUS='01' and mkck_status='P')"; 	}
			
			if(subaction[i].toString().equals("$SECAUTH")){
				cond = cond + " OR (CARD_STATUS='02' and mkck_status='M') ";}
			
			if(subaction[i].toString().equals("$PREREG")){   
				cond = cond + " OR (CARD_STATUS='02' and mkck_status='P') ";
			}
			if(subaction[i].toString().equals("$PREAUTH")){
				cond = cond + " OR (CARD_STATUS='03' and mkck_status='M') ";
			}
			if(subaction[i].toString().equals("$RECIEVEREG")){
				cond = cond + " OR (CARD_STATUS='03'  and mkck_status='P') ";
			}
			if(subaction[i].toString().equals("$RECIEVEAUTH")){
				cond = cond + " OR (CARD_STATUS='04'  and mkck_status='M')";
			}
			if(subaction[i].toString().equals("$ISSUEREG")){
				cond = cond + " OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') ";
			}
			if(subaction[i].toString().equals("$ISSUEAUTH")){
				cond = cond + " OR (CARD_STATUS='05'  and mkck_status='M') ";    
			}
		}              
		
		return reportGenMaster(cond);           
	}
	*/
	
	public String getReportCondition(String subaction,String actionhead,String instid,String fromdate,String todate,String cardprocess,String Branchcode)
	{
		String cond = "",status=null,tableName="";
		trace("cardprocess_sardar"+cardprocess+"\n sa"+subaction);
		if (cardprocess.equalsIgnoreCase("PERS")){
			
		
		if("REGN".equalsIgnoreCase(subaction) || "CARDGENREG".equalsIgnoreCase(subaction)){
			tableName = "PERS_CARD_ORDER";
		}else{
			tableName = "PERS_CARD_PROCESS";
		}
		if("REGN".equalsIgnoreCase(subaction)){
			cond = " OR (ORDER_STATUS='01' and mkck_status='M')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
		}else if("CARDGENREG".equalsIgnoreCase(subaction)){
			cond = " OR (ORDER_STATUS='01' and mkck_status='P')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
		}else if("CARDGENAUTH".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='01' and mkck_status='M')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
		}else if("CARDGEN".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
			cond = "SELECT BRANCH_CODE,ORDER_REF_NO ,remarks as MCARD_NO, CIN ,EMBOSSING_NAME as EMB_NAME,ACCOUNT_NO as ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Generation','','')mkck_status " +
					" FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 OR (ORDER_STATUS='01' and mkck_status='P')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all  " +
					" SELECT BRANCH_CODE, ORDER_REF_NO ,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Card Generation Authorization','','')mkck_status" +
					"  FROM PERS_CARD_PROCESS WHERE INST_ID='ORBL' AND 1!=1 OR (CARD_STATUS='01' and mkck_status='M')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') ";
			return cond;
		}else if("SECREG".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='01' and mkck_status='P')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("SECAUTH".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='02' and mkck_status='M') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("SEC".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
			cond = "OR (CARD_STATUS='01' and mkck_status='P')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') ";
			cond = cond +"  OR (CARD_STATUS='02' and mkck_status='M') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			status = " ,DECODE(mkck_status,'P','Waiting For Security Generation','M','Waiting For Security Authorization') status";
		}else if("PREREG".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='02' and mkck_status='P')  AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("PREAUTH".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='03' and mkck_status='M') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("PRE".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='02' and mkck_status='P')  AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			cond = cond + " OR (CARD_STATUS='03' and mkck_status='M') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			status = " ,DECODE(mkck_status,'P','Waiting For Personalization','M','Waiting For Personalization Authorization') status";
		}else if("RECIEVEREG".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='03'  and mkck_status='P') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("RECIEVEAUTH".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='04'  and mkck_status='M') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("RECIEVE".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='03'  and mkck_status='P') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			cond = cond +"  OR (CARD_STATUS='04'  and mkck_status='M') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			status = " ,DECODE(mkck_status,'P','Waiting to Receive','M','Waiting for Receive Authorization') status";
		}else if("ISSUEREG".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') and trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("ISSUEAUTH".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='05'  and mkck_status='M') AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
		}else if("ISSUE".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
			cond = " OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') and trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			cond = cond +" OR (CARD_STATUS='05'  and mkck_status='M') AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			status = " ,DECODE(mkck_status,'P','Waiting for Issuance','M','Waiting for Issuance Authorization') status";
		}else if(("null".equalsIgnoreCase(subaction) || subaction==null )  && "ALL".equalsIgnoreCase(actionhead)){
			String getallQry=this.getAllQry(fromdate,todate,instid,tableName,cardprocess);
			return getallQry;
		}
		if("PERS_CARD_PROCESS".equalsIgnoreCase(tableName)){
			if(status != null){
				cond = "SELECT BRANCH_CODE,ORDER_REF_NO ,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG "+status+"  FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 "+cond;
			}else{
				cond = "SELECT BRANCH_CODE,ORDER_REF_NO ,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG  FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 "+cond;
			}
		 }else{
			 cond = "SELECT BRANCH_CODE,ORDER_REF_NO ,remarks as MCARD_NO, CIN ,EMBOSSING_NAME as EMB_NAME,ACCOUNT_NO as ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id  FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 "+cond;
		 }
		 
		if("SECREG".equalsIgnoreCase(subaction) || "PREAUTH".equalsIgnoreCase(subaction)){
			cond = cond.replace("MCARD_NO", "ORG_CHN as MCARD_NO");
		}
		
		}else{
			
			if("REGN".equalsIgnoreCase(subaction) || "CARDGENREG".equalsIgnoreCase(subaction)){
				tableName = "INST_CARD_ORDER";
			}else{
				tableName = "INST_CARD_PROCESS";
			}
			if("REGN".equalsIgnoreCase(subaction)){
				cond = " OR (ORDER_STATUS='01' and mkck_status='M')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			}else if("CARDGENREG".equalsIgnoreCase(subaction)){
				cond = " OR (ORDER_STATUS='01' and mkck_status='P')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			}else if("CARDGENAUTH".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='01' and mkck_status='M')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
			}else if("CARDGEN".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
				cond = "SELECT BRANCH_CODE,ORDER_REF_NO ,remarks as MCARD_NO, '0' as CIN,EMBOSSING_NAME as EMB_NAME,'0' as ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Generation','','')mkck_status " +
						" FROM INST_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 OR (ORDER_STATUS='01' and mkck_status='P')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all  " +
						" SELECT BRANCH_CODE, ORDER_REF_NO  ,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Card Generation Authorization','','')mkck_status" +
						"  FROM INST_CARD_PROCESS WHERE INST_ID='ORBL' AND 1!=1 OR (CARD_STATUS='01' and mkck_status='M')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') ";
				return cond;
			}else if("SECREG".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='01' and mkck_status='P')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
				status = " ,DECODE(PRIVILEGE_CODE,'','Waiting For Cvv Generation') status";

			}else if("SECAUTH".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='02' and mkck_status='M') AND PRIVILEGE_CODE='02M' AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
				status = " ,DECODE(PRIVILEGE_CODE,'02M','Waiting For Cvv Authorization') status";

			}else if("SEC".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
				cond = "OR (CARD_STATUS='01' and mkck_status='P')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') ";
				cond = cond +"  OR (CARD_STATUS='02' and mkck_status='M') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				status = " ,DECODE(mkck_status,'P','Waiting For Security Generation','M','Waiting For Security Authorization') status";
			}else if("PREREG".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='02' and mkck_status='P')  AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}else if("PREAUTH".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='03' and mkck_status='M') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}else if("PRE".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='02' and mkck_status='P')  AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				cond = cond + " OR (CARD_STATUS='03' and mkck_status='M') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				status = " ,DECODE(mkck_status,'P','Waiting For Personalization','M','Waiting For Personalization Authorization') status";
			}
			else if("PINONLYGEN".equalsIgnoreCase(subaction)){
				cond = " OR (PRIVILEGE_CODE='01P' )  AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}else if("PINONLYAUTH".equalsIgnoreCase(subaction)){
				cond = " OR (PRIVILEGE_CODE='02M' ) AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}
			else if("PINONLY".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
				cond = "OR (PRIVILEGE_CODE='01M' )  AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') ";
				cond = cond +"  OR (PRIVILEGE_CODE='01P' ) AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				status = " ,DECODE(PRIVILEGE_CODE,'01P','Waiting For Pin Generation','02M','Waiting For Pin Authorization') status";
			}
			
			else if("RECIEVEREG".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='03'  and mkck_status='P') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}else if("RECIEVEAUTH".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='04'  and mkck_status='M') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}else if("RECIEVE".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='03'  and mkck_status='P') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				cond = cond +"  OR (CARD_STATUS='04'  and mkck_status='M') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				status = " ,DECODE(mkck_status,'P','Waiting to Receive','M','Waiting for Receive Authorization') status";
			}else if("ISSUEREG".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') and trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}else if("ISSUEAUTH".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='04'  and mkck_status='M') AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')"; 
			}else if("ISSUE".equalsIgnoreCase(actionhead) && "ALL".equalsIgnoreCase(subaction)){
				cond = " OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') and trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				cond = cond +" OR (CARD_STATUS='05'  and mkck_status='M') AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
				status = " ,DECODE(mkck_status,'P','Waiting for Issuance','M','Waiting for Issuance Authorization') status";
			}
			
			else if(actionhead.equals("CUSTMAP") ){
				  String condi="";
					  /*if(!branchlist.equals("ALL")){
					   condi="AND CARD_COLLECT_BRANCH='"+branchlist+"' ";
					  }else{condi="";}*/
					  
						  if(subaction.equals("CUSTMAPNOT")){
						    status=" ,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,DECODE(mkck_status,'P','Waiting For Card Customer Registration','','')status " ;
						    		cond="  OR CARD_STATUS='05'  AND MKCK_STATUS='P'  AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
						    		
						   }else if(subaction.equals("CUSTMAP")){
						    status=",to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,DECODE(mkck_status,'M','Waiting For  Customer Registration Authorization','','')status " ;
						    		cond="  OR CARD_STATUS='06'   AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
						  
						   }
						   else{
		//ALL Records				
							  	  
							  // condi=" SELECT CARD_COLLECT_BRANCH AS BRANCH_CODE,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Customer Registration','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='05'  AND MKCK_STATUS='P'  AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') AND trim(CARD_COLLECT_BRANCH)='"+Branchcode+"'  UNION ALL SELECT CARD_COLLECT_BRANCH AS BRANCH_CODE,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For  Customer Registration Authorization','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='06'  AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
							   condi=" SELECT CARD_COLLECT_BRANCH AS BRANCH_CODE,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Customer Registration','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='05'  AND MKCK_STATUS='P'  AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')  UNION ALL SELECT CARD_COLLECT_BRANCH AS BRANCH_CODE,ORDER_REF_NO ,ORG_CHN, CIN ,EMB_NAME as EMB_NAME,ACCT_NO as ACCT_NO,to_char(ISSUE_DATE, 'dd-MON-yyyy') as ISSUE_DATE,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For  Customer Registration Authorization','','')mkck_status FROM INST_CARD_PROCESS where  CARD_STATUS='06'  AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";

						    return condi;
						   }
				  }	
			
			
			else if(("null".equalsIgnoreCase(subaction) || subaction==null )  && "ALL".equalsIgnoreCase(actionhead)){
				String getallQry=this.getAllQry(fromdate,todate,instid,tableName,cardprocess);
				return getallQry;
			}
			if("INST_CARD_PROCESS".equalsIgnoreCase(tableName)){
				if(status != null ){
					trace("condi"+cond);
					cond = "SELECT CARD_COLLECT_BRANCH ||BRANCH_CODE AS BRANCH_CODE,ORDER_REF_NO ,ORG_CHN,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG "+status+"  FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 "+cond;
				}
				
				else{
					cond = "SELECT CARD_COLLECT_BRANCH ||BRANCH_CODE AS BRANCH_CODE,ORDER_REF_NO ,ORG_CHN,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(PC_FLAG,'P','PRIMARY','S','SECONDARY')PC_FLAG  FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 "+cond;
				}
			 }
			//System.out.println("dsadsadsadsa"+status   +"sdsadasds"  +subaction);
			if ( "REGN".equalsIgnoreCase(subaction)||"CARDGENREG".equalsIgnoreCase(subaction) ){
				trace("condi"+cond);
				 cond = "SELECT  BRANCH_CODE AS BRANCH_CODE,ORDER_REF_NO ,remarks as MCARD_NO ,EMBOSSING_NAME as EMB_NAME,(select username from USER_DETAILS where userid=MAKER_ID) maker_id  FROM INST_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 "+cond;
			}
			if("SECREG".equalsIgnoreCase(subaction) || "PREAUTH".equalsIgnoreCase(subaction)){
				cond = cond.replace("MCARD_NO", "ORG_CHN as MCARD_NO");
			}
			
		}
		   
		   trace("getReportCondition endes ....");   
		return cond;              
	}
	
	
	public String getRenewalReportCondition(String instid,String fromdate,String todate)
	{
		String cond = "";
		cond = "(CARD_STATUS='05' AND trunc(EXPIRY_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY'))";
		cond = "SELECT BRANCH_CODE,ORDER_REF_NO ,MCARD_NO, CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id  FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND 1!=1 "+cond;						
		//return getallQry;		   
		return cond;              
	}
	
	
	public String cardreportpage()
	{
		System.err.println("cardreportpage method called ........"); 
		HttpSession session = getRequest().getSession();
		String userid = comUserCode();
		//String instid = (String)session.getAttribute("Instname");
		String instid = getRequest().getParameter("INSTID");
		System.out.println("checking instiution id in card report page"+instid);
		String fromdate =  getRequest().getParameter("fromdate");
		trace("from date getting" + fromdate);
		String todate =  getRequest().getParameter("todate");
		trace("to date getting" + todate);
		
		System.out.println("checking instid"+instid);
		 //String CONDITION = getRequest().getParameter("CONDITION");                 
		//trace("asdf:::CONDITION::::::"+CONDITION);     
		String branchcode =  getRequest().getParameter("BRANCHCODE");    
		trace("branchcode:::"+branchcode); 
		String tableName="";
		String act = getRequest().getParameter("act");
		String subact = getRequest().getParameter("subact");
		String cardprocess="";
		
		cardprocess =getRequest().getParameter("cardprocess");
		
		String cond = this.getReportCondition(subact,act,instid,fromdate,todate,cardprocess,branchcode);
		
		String reportheader="Card Process Report";
		String reporttitle="Debit Cardman";
		enctrace ("Got Condition Query ::::"+cond);    
		String productCode = getRequest().getParameter("productCode");
		if(subact.equals("CUSTMAPNOT")){
			if(branchcode.equals("ALL")){
				reporttitle="All Branches";
			}else{
				reporttitle="Generated For :"+branchcode+" ";
			}
			reportheader="Instant Cards Not Mapped";
			 
		}
		else if(subact.equals("CUSTMAP")){
			if(branchcode.equals("ALL")){
				reporttitle="All Branches";
			}else{
				reporttitle="Generated For :"+branchcode+" ";
			}
			reportheader="Mapped Instant Cards";
		}
		
		 String Query = cond;
		 
		/* if(branchcode.equals("ALL")){
			 Query = Query + "AND BRANCH_CODE IN (SELECT BRANCH_CODE FROM BRANCH_MASTER WHERE AUTH_CODE='1') ";    
		 }else    
		 {
			 Query = Query + "AND BRANCH_CODE = '"+branchcode+"' ";     
		 }*/
		 
		 if(!branchcode.equals("ALL")){
			 
			 if(subact.equals("CUSTMAPNOT") ||subact.equals("CUSTMAP")){
				 Query = Query.replaceAll("union all", " AND BRANCH_CODE='"+branchcode+"' union all ")+" AND trim(CARD_COLLECT_BRANCH)='"+branchcode+"'";    
 
			 }else if(subact.equals("CUSTMAPNOT")){
				 Query = Query.replaceAll("union all", " AND BRANCH_CODE='"+branchcode+"' union all ")+" AND BRANCH_CODE='"+branchcode+"'";    
				trace("4: Adding brach List ::::"+Query);    
				}
			 }
				
		if(!productCode.equals("ALL")){
			Query = Query.replaceAll("union all", " AND PRODUCT_CODE='"+productCode+"' union all ")+" AND PRODUCT_CODE='"+productCode+"'"; 
			trace("4: productCode ::::"+productCode);    
		}
		 
		enctrace("Query WITH Condition"+Query);   
		try {
			PadssSecurity sec = new PadssSecurity();
			CommonDesc commondesc = new CommonDesc();
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			//System.out.println("keyid::"+keyid);
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
			List cardList = dao.getMasterReportList(instid,eDMK,eDPK,sec,Query, jdbctemplate);
			System.out.println("cardlist:::::"+cardList);
			setMasterReportList(cardList);         
		}catch(Exception e)
		{
			trace("Exception getting card list "+e);
		}
		           
		System.err.println("cardreportpage method endes ........");
		session.setAttribute("reportheader", reportheader);
		session.setAttribute("reporttitle", reporttitle);
		session.setAttribute("fromdate", fromdate);
		session.setAttribute("todate", todate);
		session.setAttribute("userid", comUserCode());
		return "cardreportpage";
	}
	      
	
	public String reportGenMaster(String subact,String act,String branchcode,String productCode,String cardprocess) throws IOException
	{
		trace("reportGenMaster started ...");       
		
		//trace("conditioncondition:"+condition+"-brachcode::"+branchcode);    
		String qwer = "CARD_STATUS=03";
		HttpSession session = getRequest().getSession();	
		String instid = (String)session.getAttribute("Instname");
		String fromdate =  getRequest().getParameter("fromdate");
		trace("from date getting" + fromdate);
		
		String todate =  getRequest().getParameter("todate");
		trace("to date getting" + todate);
		String userid = comUserCode();
		String reportno= "3"; //getRequest().getParameter("RNO");
		String URLLINK="cardreportpageMasterReportGeneration.do?INSTID="+instid+"&BRANCHCODE="+branchcode+"&fromdate="+fromdate+"&todate="+todate+"&act="+act+"&subact="+subact+"&productCode="+productCode+"&cardprocess="+cardprocess;  //getRequest().getParameter("URL").trim();;    
		trace("OLDURL:"+URLLINK);   
		Properties prop = getCommonDescProperty();
		String serverPath = prop.getProperty("serverpath.location");
		trace("serverPath"+serverPath);    
		String replaceUrl = serverPath+URLLINK.replaceAll("\\$", "&");   
		trace("URLLINK::::"+replaceUrl);
		String reportCondition=getRequest().getParameter("CONDITION");
		
		List repconfig = dao.getReportConfigDetails(instid,reportno,jdbctemplate); 
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
		
		if(subact.equals("CUSTMAPNOT")){
			filename="InstantCardsNotMapped";
			
		}
		else if(subact.equals("CUSTMAP")){
			filename="MappedInstantCards";
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
	
	
	public String ExcelReportGenMaster(String subaction,List excelReportList) throws IOException
	{
		trace("reportGenMaster started ..."+subaction);
		HttpSession session = getRequest().getSession();	
		String instid = getRequest().getParameter("INSTID");
		String reportno="3";
		String filename="";
		if(subaction.equals("CUSTMAPNOT")){
			filename="InstantCardsNotmMapped";
			
		}
		else if(subaction.equals("CUSTMAP")){
			filename="MappedInstantCards";
		}
		
		else{
		
		
		System.out.println("reportno::"+reportno);   
		
		List repconfig = this.getReportConfigDetails(instid,reportno,jdbctemplate); 
		Iterator iterator=repconfig.iterator();
		String passwdReq="";
		while(iterator.hasNext())	{
			Map map = (Map)iterator.next();
			filename = (String)map.get("REPORTNAME");
			}
		}
		List result = excelReportList;
		
		String excelparam = this.getExcelParam(instid,reportno,jdbctemplate);
		
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
			    trace("dateFormat.format(date)    --->  "+curdatetime);
			    String defaultname = namestr+curdatetime+".xls";
			    trace("default name"+ defaultname);
			SXSSFWorkbook workbook = new SXSSFWorkbook();						
			Sheet sheet = null;
			trace(" keyDesc111 "+keyDesc);
			String keydesc_split[] = keyDesc.split("-");
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
				Row rowheading=null;
				while( itr1.hasNext() ){
					trace("rowno = ******** = "+rowno);
					Map map = (Map)itr1.next();
					Iterator keyItr = map.keySet().iterator();
					if(rowno==0){
						rowheading = sheet.createRow((short)rowno++);
						trace("*******rowheading *****");
					}
					Row row = sheet.createRow((short) rowno++);
					String key = null;
					int cellno=0;
					while(keyItr.hasNext()){	
						trace("rowno inside second while loop = ******** = "+rowno);
						Cell cell = null;
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
			//pd4ml.generateOutlines(true);
			pd4ml.enableTableBreaks(false);    
			   
			
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
			pgmark.setAreaHeight(100);
		    
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
			System.out.println("WEBURL============>"+webURL);
			trace("WEBURL============>"+webURL);

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
				System.out.println("FILNALURL"+url);	
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
public String getAllQry(String fromdate,String todate,String instid,String tableName,String cardprocess){
	String qry="";
	if(cardprocess.equalsIgnoreCase("PERS")){
		qry = "SELECT BRANCH_CODE,remarks as MCARD_NO,ORDER_REF_NO , CIN ,EMBOSSING_NAME as EMB_NAME,ACCOUNT_NO as ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Registration Authorization','','') status " +
						" FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 OR (ORDER_STATUS='01' and mkck_status='M')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,remarks as MCARD_NO,ORDER_REF_NO , CIN ,EMBOSSING_NAME as EMB_NAME,ACCOUNT_NO as ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Generation','','') status " +
						" FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 OR (ORDER_STATUS='01' and mkck_status='P')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Card Generation Authorization','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='01' and mkck_status='M')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all" +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Security Generation','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='01' and mkck_status='P')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Security Generation','','') status FROM PERS_CARD_PROCESS WHERE INST_ID='ORBL' AND 1!=1 " +
						" OR (CARD_STATUS='02' and mkck_status='M') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username " +
						" from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Security Authorization','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='02' and mkck_status='M') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Personalization','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='02' and mkck_status='P') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all" +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Personalization Authorization','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='03' and mkck_status='M') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting to Receive','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='03'  and mkck_status='P') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting for Receive Authorization','','') status FROM PERS_CARD_PROCESS WHERE INST_ID='ORBL' AND 1!=1 " +
						" OR (CARD_STATUS='04'  and mkck_status='M') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting for Issuance','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
						" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting for Issuance Authorization','','') status " +
						" FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='05'  and mkck_status='M') AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
	}else{
		
		qry = "SELECT BRANCH_CODE,remarks as MCARD_NO,ORDER_REF_NO , CIN ,EMBOSSING_NAME as EMB_NAME,ACCOUNT_NO as ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Registration Authorization','','') status " +
				" FROM INST_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 OR (ORDER_STATUS='01' and mkck_status='M')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,remarks as MCARD_NO,ORDER_REF_NO , CIN ,EMBOSSING_NAME as EMB_NAME,ACCOUNT_NO as ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Card Generation','','') status " +
				" FROM INST_CARD_ORDER WHERE INST_ID='"+instid+"' AND 1!=1 OR (ORDER_STATUS='01' and mkck_status='P')  AND trunc(ORDERED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Card Generation Authorization','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='01' and mkck_status='M')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all" +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Security Generation','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='01' and mkck_status='P')  AND trunc(GENERATED_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Security Generation','','') status FROM PERS_CARD_PROCESS WHERE INST_ID='ORBL' AND 1!=1 " +
				" OR (CARD_STATUS='02' and mkck_status='M') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username " +
				" from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Security Authorization','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='02' and mkck_status='M') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting For Personalization','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='02' and mkck_status='P') AND trunc(PIN_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all" +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting For Personalization Authorization','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='03' and mkck_status='M') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting to Receive','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='03'  and mkck_status='P') AND trunc(PRE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting for Receive Authorization','','') status FROM PERS_CARD_PROCESS WHERE INST_ID='ORBL' AND 1!=1 " +
				" OR (CARD_STATUS='04'  and mkck_status='M') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'P','Waiting for Issuance','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='04' AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','A') and mkck_status='P') AND trunc(RECV_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY') union all " +
				" SELECT BRANCH_CODE,MCARD_NO, ORDER_REF_NO , CIN ,EMB_NAME ,ACCT_NO,(select username from USER_DETAILS where userid=MAKER_ID) maker_id,DECODE(mkck_status,'M','Waiting for Issuance Authorization','','') status " +
				" FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND 1!=1 OR (CARD_STATUS='05'  and mkck_status='M') AND trunc(ISSUE_DATE) BETWEEN to_date('"+fromdate+"','DD-MM-YY') and to_date('"+todate+"','DD-MM-YY')";
	}
	return qry;
}	
}
