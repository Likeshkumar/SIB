package com.ifd.Report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.ifp.util.PDFReportGenerator;
import com.itextpdf.text.Document;

import connection.CBSConnection;

public class FeeReport extends BaseAction {
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager  = new DataSourceTransactionManager();
	
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	private ByteArrayOutputStream output_stream;
	private ByteArrayInputStream input_stream;
	private String report_name;
	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	CommonDesc commondesc = new CommonDesc();
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	
	
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

	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String feeReport;
	
	public String getFeeReport() {
		return feeReport;
	}

	public void setFeeReport(String feeReport) {
		this.feeReport = feeReport;
	}

	private List actionlist;
	
	
	public List getActionlist() {
		return actionlist;
	}

	public void setActionlist(List actionlist) {
		this.actionlist = actionlist;
	}
	
	public String comUserId(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}

	public String feeReportHome(){
		String instid = comInstId();
		try {
			List actionlist = getActionList(instid,jdbctemplate);
			setActionlist(actionlist);
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception : " +e);
		}
		return "feereporthome";
	}
	private List getfeeRecordList(String instid,String refnum,String qrycondition, JdbcTemplate jdbctemplate2) {
		List result=null;
		try {
			String qry = "SELECT MCARD_NO,ACCOUNT_NO,FEE_AMOUNT,CASE WHEN CARDACTIVITY IN ('RENEWAL','ADD-ON','REISSUE','DAMAGE') THEN 'CARD REISSUE CHARGE' WHEN CARDACTIVITY='REPIN' THEN 'PIN REISSUE CHARGE' WHEN CARDACTIVITY='NEWCARD' THEN 'NEW CARD CHARGE' END as CARDACTIVITY,BRANCH_CODE,TO_CHAR(FEE_GEN_DATE,'DD-MON-YYYY') AS FEE_GEN_DATE,(SELECT ACC_CCY FROM CARD_PRODUCTION WHERE HCARD_NO=A.HCARD_NO) AS CURRENCY FROM FEE_COLLECTION A WHERE "+qrycondition+" ";
			trace("final Qry is " +qry);
			result = jdbctemplate2.queryForList(qry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getcardcollect( String instid, String MAKER_ID, JdbcTemplate jdbcemplate ) throws Exception{   
		String qryproductdesc = "SELECT USERNAME FROM USER_DETAILS WHERE INSTID='"+instid+"' and USERID='"+MAKER_ID.trim()+"'";
		enctrace( " getbranchdetail " + qryproductdesc); 
		if ( jdbcemplate.getMaxRows() == 0 ) {
			String bin_desc = (String)jdbcemplate.queryForObject(qryproductdesc, String.class); 
			return bin_desc; 
		}else{
			return "UNKNOWN BRANCH_NAME";
		}
	}	
	
	public String getfeereport()
	{
		trace("Fee Report method called......... ");	
		String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	    trace("dateFormat.format(date)    --->  "+curdatetime);
		String instid = comInstId();
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		String propertyname ="feereport";
		
		String actioncode = getRequest().getParameter("actioncode");
		//System.out.println(actioncode.toString());
		 String qrycondition = "",subcond = "";
		 String fromdate =  getRequest().getParameter("fromdate");
		 String todate =  getRequest().getParameter("todate");
		 try{
			 List recordlist=null;	
			 int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;String refnum=null;
			 StringBuffer refno = new StringBuffer();
			 String comma= "";
				String reportname = "FeeReport_";
			 if( actioncode != null ){
				    String actioncodesplit[] = actioncode.split(",");
				    for( int i=0; i<actioncodesplit.length; i++ ){ 
						subcond += actioncodesplit[i].trim() +"','"; 
						System.out.println("subcondition " + subcond);
						}
						
						if (subcond.endsWith(",")) {
							subcond = subcond.substring(0, subcond.length() - 1);
							System.out.println("testing subcond " + subcond);
						}
				 	//actioncode ="'"+actioncode.trim().replaceAll(",","','")+"'";
				 	qrycondition = " CARDACTIVITY IN ('"+subcond+"') AND FEE_FLAG='P' AND ACCOUNT_NO not in ('312002005','222001009') AND TRUNC(FEE_GEN_DATE) BETWEEN TO_DATE('"+fromdate+"','dd-mm-yyyy') AND TO_DATE('"+todate+"','dd-mm-yyyy')  ";
					
				}
			 trace("cardno"+refno.toString());
				try {
					 recordlist = getfeeRecordList(instid,refno.toString(),qrycondition,jdbctemplate);
					 trace("List value " +recordlist);
					
					if(recordlist.isEmpty()){
						trace("No Records found");
						addActionError("No Records Found");
					}else{
						PDFReportGenerator pdfgen= new PDFReportGenerator(document, output_stream, propertyname, getRequest());
						String title = "FeeReport";
						pdfgen.addPDFTitles(document, title, ALIGN_CENTER);
						
						trace("Report Name is "+reportname+curdatetime+".pdf");
						setReport_name(reportname+curdatetime+".pdf");
						
						ListIterator litr = recordlist.listIterator();
						
						while(litr.hasNext()){
							Map mp = (Map) litr.next();
							mp.put("MCARD_NO", mp.get("MCARD_NO").toString());
							mp.put("ACCOUNT_NO", mp.get("ACCOUNT_NO").toString());
							mp.put("BRANCH_CODE", mp.get("BRANCH_CODE").toString());
						    mp.put("FEE_GEN_DATE", mp.get("FEE_GEN_DATE").toString());
					       // mp.put("CIN", mp.get("CIN").toString());
							//mp.put("MAKER_ID", mp.get("MAKER_ID").toString());
							//mp.put("EMB_NAME", mp.get("EMB_NAME").toString());
							mp.put("CARDACTIVITY", mp.get("CARDACTIVITY").toString());
							mp.put("FEE_AMOUNT", mp.get("FEE_AMOUNT").toString());
							mp.put("CURRENCY", mp.get("CURRENCY").toString());
							
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
					e.printStackTrace();
					addActionError("Unable to continue the report generation");
					trace("Exception in Loop ::: " + e.getMessage());
				
			}
				
			 
			}catch(Exception e){	
				e.printStackTrace();
				trace("Exception ::::: "+e.getMessage());
			}
		 return feeReportHome();
	}
	public String generateReport(){
		IfpTransObj transact = commondesc.myTranObject("UPDATECOMM", txManager);
		String instid = comInstId();
		String fromdate =  getRequest().getParameter("fromdate");
		trace("from date getting" + fromdate);
		String todate =  getRequest().getParameter("todate");
		trace("to date getting" + todate);
		//String actioncode =  getRequest().getParameter("actioncode");
		String[] actioncode = getRequest().getParameterValues("actioncode");
		String batchno=getRequest().getParameter("batchno");
		String[] cardno = getRequest().getParameterValues("instorderrefnum"); 
		//System.out.println(" action code values " + actioncode);
		String qrycondition = "";
		String subcond = "",cardcond="";
		try {
			
			
			if( cardno != null ){
				
				
				/*for( int i=0; i<actioncode.length; i++ ){ 
						subcond += actioncode[i] +"','"; 
						System.out.println("subcondition " + subcond);
				}
				
				if (subcond.endsWith(",")) {
					subcond = subcond.substring(0, subcond.length() - 1);
					System.out.println("testing subcond " + subcond);
				}*/
				
				for( int i=0; i<cardno.length; i++ ){ 
					cardcond += cardno[i] +"','"; 
					//System.out.println("cardcond " + cardcond);
				}
				
				if (cardcond.endsWith(",")) {
					cardcond = cardcond.substring(0, cardcond.length() - 1);
					//System.out.println("testing cardcond " + cardcond);
				}
				
				//qrycondition += "CARDACTIVITY IN ('"+subcond+"') ";
				
				qrycondition += " CARDNO IN ('"+cardcond+"') ";
				
				 
			}
			
			
			qrycondition += "AND TRUNC(FEE_GEN_DATE) BETWEEN TO_DATE('"+fromdate+"','dd-mm-yyyy') AND TO_DATE('"+todate+"','dd-mm-yyyy') ";
									
			/*	for( int i=0; i<actioncode.length; i++ ){
					 
						 		
						subcond += actioncode[i] +","; 
					 
					
					if (subcond.endsWith(",")) {
						subcond = subcond.substring(0, subcond.length() - 1);
					}
			
			
					qrycondition += " AND (  CARDACTIVITY IN ('"+subcond+"') ) ";
					//qrycondition += " AND CARDACTIVITY in ('"+actioncode+"')";
			}*/

			enctrace("final where clause " +qrycondition);
			List<Map<String,Object>> recordlist = getFeeDetailList(instid,qrycondition,batchno,jdbctemplate); 

			if(recordlist.isEmpty()){
				trace("No Records found");
				addActionError("No Records Found");
			}else{
				String result = excelReportGenMaster(recordlist);
				int updateqry = updateFeeFlag(instid,qrycondition,batchno,jdbctemplate);
				//System.out.println("recordlist..."+recordlist);
				if(updateqry>0){
					addActionMessage("Fee Flag Updated Successfully");
					trace("Fee updated successfully.....");
					//txManager.commit(transact.status);
					trace("Txn commit successfully");
				}else{
					addActionError("Failed to update Fee");
					trace("failed to update Fee....");
					//txManager.rollback(transact.status);
					trace("Txn rollback successfully");
				}
				
				// CBS insert starts
				String userid =comUserId();
				String username = commondesc.getCbsUserName(instid, userid, jdbctemplate);
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rrs = null;
				int insertdetail = 0,insertmaster = 0,localdetailinsert = 0,localmasterinsert = 0;
				String cbsdetailinsert = "",cbsmasterinsert = "",branchno="",sourcecode = "",addtext = "",txncode = "",amt="",totalcount="";
				CBSConnection cbscon = new CBSConnection();
				con = cbscon.getCBSDBConnection();
				if(con != null){
					
					try {
						
						for(int i = 0;i<recordlist.size();i++){
							
							txncode = (String) recordlist.get(i).get("TXN_CODE");
							branchno = (String) recordlist.get(i).get("BRANCH_CODE");
							sourcecode = (String) recordlist.get(i).get("SOURCE_CODE");
							addtext = (String) recordlist.get(i).get("ADDL_TEXT");
							amt = (String) recordlist.get(i).get("AMOUNT");
							//batchno = (String) recordlist.get(i).get("BATCH_NO");
							if(!"0".equals(amt)){
								cbsdetailinsert = "INSERT INTO DETB_UPLOAD_DETAIL(BRANCH_CODE,FIN_CYCLE, PERIOD_CODE,BATCH_NO, SOURCE_CODE,CURR_NO, UPLOAD_STAT, CCY_CD, INITIATION_DATE, AMOUNT,ACCOUNT, ACCOUNT_BRANCH, TXN_CODE, DR_CR, LCY_EQUIVALENT,EXCH_RATE, VALUE_DATE, ADDL_TEXT) Values " +
													"('003','"+recordlist.get(i).get("FIN_CYCLE")+"','"+recordlist.get(i).get("PERIOD_CODE")+"','"+batchno+"','"+recordlist.get(i).get("SOURCE_CODE")+"','"+(i+1)+"','"+recordlist.get(i).get("UPLOAD_STAT")+"'," +
													"'"+recordlist.get(i).get("CCY_CD")+"',TO_DATE('"+recordlist.get(i).get("INITIATION_DATE")+"','DD/MM/YYYY HH24:MI:SS'),'"+recordlist.get(i).get("AMOUNT")+"','"+recordlist.get(i).get("ACCOUNT_NO")+"','"+recordlist.get(i).get("ACCOUNT_BRANCH")+"','"+recordlist.get(i).get("TXN_CODE")+"'," +
													"'"+recordlist.get(i).get("DR_CR")+"','"+recordlist.get(i).get("LCY_EQUIVALENT")+"','"+recordlist.get(i).get("EXCH_RATE")+"',TO_DATE('"+recordlist.get(i).get("VALUE_DATE")+"','DD/MM/YYYY HH24:MI:SS'),'"+recordlist.get(i).get("ADDL_TEXT")+"')";
								//System.out.println("cbsdetailinsert..."+cbsdetailinsert);
								trace("cbsdetailinsert ---> "+cbsdetailinsert);
								pstmt = con.prepareStatement(cbsdetailinsert);
								insertdetail = pstmt.executeUpdate();
								localdetailinsert = jdbctemplate.update(cbsdetailinsert);
							}
						}
						if(!"0".equals(amt)){
							String batchnocountqry ="SELECT COUNT(1) AS CNT FROM FEE_COLLECTION WHERE INSTID='"+instid+"' AND "+qrycondition+"";
							trace("batchnocountqry ---> "+batchnocountqry);
							//System.out.println("batchnocountqry..."+batchnocountqry);
							totalcount =(String)jdbctemplate.queryForObject(batchnocountqry,String.class);
							
							cbsmasterinsert = "INSERT INTO DETB_UPLOAD_MASTER(BRANCH_CODE,SOURCE_CODE,BATCH_NO,TOTAL_ENTRIES,UPLOADED_ENTRIES,BALANCING,BATCH_DESC,MIS_REQUIRED,AUTO_AUTH,GL_OFFSET_ENTRY_REQD,UDF_UPLOAD_REQD,OFFSET_GL,USER_ID,ONCE_AUTH,RECORD_STAT,TXN_CODE) VALUES(" +
											  "'003','"+sourcecode+"','"+batchno+"','"+totalcount+"','','Y','"+addtext+"','','N','','','','"+username+"','Y','O','"+txncode+"')";
							//System.out.println("cbsmasterinsert..."+cbsmasterinsert);
							trace("cbsmasterinsert ---> "+cbsmasterinsert);
							pstmt = con.prepareStatement(cbsmasterinsert);
							insertmaster = pstmt.executeUpdate();
							localmasterinsert = jdbctemplate.update(cbsmasterinsert);
							/*String localinsertqry = "INSERT INTO IFD_FEE_CBSINSERT_DETAILS(INST_ID,INSERT_QUERY,PROCESSED_BY,PROCESSED_DATE,TOTAL_RECORDS,BATCH_NO,ACTION) VALUES(" +
													" '"+instid+"','"+cbsdetailinsert.replaceAll("'", "")+"---"+cbsmasterinsert.replaceAll("'", "")+"','"+username+"',SYSDATE,'"+totalcount+"','"+batchno+"','"+addtext+"')";
							trace("localinsertqry ---> "+localinsertqry);
							localinsert = jdbctemplate.update(localinsertqry);*/
						}
						if(insertdetail!=0 && insertmaster!=0 && localdetailinsert!=0 && localmasterinsert!=0){
							txManager.commit(transact.status);
						}else{
							txManager.rollback(transact.status);
						}
					}
					catch (Exception e) {
						con.rollback();
						e.printStackTrace();
						trace("Exception in Fee insert in cbs :: 1:::::::" + e);
					}finally {
						if(rrs != null){
							rrs.close();
						}
						pstmt.close();
						con.close();
					}
				}
				// CBS  insert end
				return feeReportHome();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception : While generating Report " +e);
		}
		return feeReportHome();
	}
	

	private int updateFeeFlag(String instid, String actionqry,String batchno, JdbcTemplate jdbctemplate) {
		int result = 0;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append("UPDATE FEE_COLLECTION SET FEE_FLAG='D',BATCH_NO='"+batchno+"' WHERE INSTID='"+instid+"' AND "+actionqry+" ");
			enctrace("getting action lst Qry " +strbuild);
			result = jdbctemplate.update(strbuild.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	private int updateFeeFlagByCardNo(String instid, String cardno,JdbcTemplate jdbctemplate) {
		int result = 0;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append("UPDATE FEE_COLLECTION SET FEE_FLAG='D' WHERE INSTID='"+instid+"' and CARDNO = '"+cardno+"'");
			enctrace("getting action by card " +strbuild);
			result = jdbctemplate.update(strbuild.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List getFeeDetailList(String instid, String qrycondition,String batchno, JdbcTemplate jdbctemplate) {
		List result = null;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append("SELECT 'FY'||TO_CHAR(SYSDATE, 'YYYY') AS FIN_CYCLE, 'M'||TO_CHAR(SYSDATE, 'MM') AS PERIOD_CODE,'"+batchno+"' as BATCH_NO,'003' as BRANCH_CODE,'VISAACQTXN' AS SOURCE_CODE,CURR_CODE,' ' AS UPLOAD_STAT, 'UGX' AS CCY_CD,TO_CHAR(SYSDATE, 'DD/MM/YYYY') AS INITIATION_DATE,FEE_AMOUNT as AMOUNT,ACCOUNT_NO,BRANCH_CODE AS ACCOUNT_BRANCH,'254' AS TXN_CODE,DR_CR,FEE_AMOUNT AS LCY_EQUIVALENT,'1'AS EXCH_RATE, TO_CHAR(SYSDATE, 'DD/MM/YYYY') AS VALUE_DATE,CASE WHEN CARDACTIVITY IN ('RENEWAL','ADD-ON','REISSUE','DAMAGE') THEN 'CARD REISSUE CHARGE' WHEN CARDACTIVITY='REPIN' THEN 'PIN REISSUE CHARGE' WHEN CARDACTIVITY='NEWCARD' THEN 'NEW CARD CHARGE' END AS ADDL_TEXT,'' AS INSTRUMNET_NO,'~~END~~' AS END FROM FEE_COLLECTION WHERE INSTID='"+instid+"' AND FEE_FLAG='P' AND "+qrycondition);
			//strbuild.append("UNION ALL ");
			//strbuild.append("SELECT DISTinct  '~~END~~' AS FIN_CYCLE, '~~END~~' AS PERIOD_CODE,'~~END~~' AS BATCH_NO,'~~END~~' AS BRANCH_CODE,'~~END~~' AS SOURCE_CODE,'~~END~~' AS CURR_CODE,'~~END~~' AS UPLOAD_STAT, '~~END~~' AS CCY_CD,'~~END~~' AS INITIATION_DATE,'~~END~~' AS AMOUNT,'~~END~~' AS ACCOUNT_NO,'~~END~~' AS ACCOUNT_BRANCH,'~~END~~' AS TXN_CODE,'~~END~~' AS DR_CR,'~~END~~' AS LCY_EQUIVALENT,'~~END~~' AS EXCH_RATE, '~~END~~' AS VALUE_DATE,'~~END~~' AS ADDL_TEXT,'~~END~~' AS INSTRUMNET_NO,'~~END~~' AS END FROM FEE_COLLECTION WHERE INSTID='"+instid+"' AND FEE_FLAG='P' AND "+qrycondition);
			enctrace("getting action lst Qry " +strbuild);
			result = jdbctemplate.queryForList(strbuild.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	private List getFeeViewList(String instid, String qrycondition,String batchno, JdbcTemplate jdbctemplate) {
		List result = null;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append("SELECT CARDNO,MCARD_NO,ACCOUNT_NO,nvl(CURR_CODE,'NA') AS CURR_CODE,FEE_AMOUNT,CASE WHEN CARDACTIVITY IN ('RENEWAL','ADD-ON','REISSUE','DAMAGE') THEN 'CARD REISSUE CHARGE' WHEN CARDACTIVITY='REPIN' THEN 'PIN REISSUE CHARGE' WHEN CARDACTIVITY='NEWCARD' THEN 'NEW CARD CHARGE' END as CARDACTIVITY,BRANCH_CODE,TO_CHAR(FEE_GEN_DATE,'DD-MON-YYYY') AS FEE_GEN_DATE," +
					"(SELECT ACC_CCY FROM CARD_PRODUCTION WHERE HCARD_NO=A.HCARD_NO) AS CURRENCY FROM FEE_COLLECTION A  WHERE INSTID='"+instid+"' AND FEE_FLAG='P' AND ACCOUNT_NO not in ('312002005','222001009') AND "+qrycondition);
			enctrace("getting getFeeViewList " +strbuild);
			result = jdbctemplate.queryForList(strbuild.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List getActionList(String instid, JdbcTemplate jdbctemplate) {
		List result = null;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append("SELECT ACTION_CODE,ACTION_DESC FROM ACTIONCODES WHERE FEE_REQ='1' AND INST_ID='"+instid+"'");
			enctrace("getting action lst Qry " +strbuild);
			result = jdbctemplate.queryForList(strbuild.toString());
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

		String filename = "FeeReport_";
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
				HSSFWorkbook workbook = new HSSFWorkbook();						
				HSSFSheet sheet = null;
				trace(" keyDesc "+keyDesc);
				//String keydesc_split[] = keyDesc.split("-");
				trace("Xls Sheet Name "+keyDesc);
				
				Iterator combined_itr = listqry.iterator();
				int i=0,sheetcount=1;
				sheet = workbook.createSheet(keyDesc+sheetcount);
				while(combined_itr.hasNext()){
					List lst = (List) combined_itr.next();
					trace(" List Size "+lst.size());
					
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
							int e = ++sheetcount;
							sheet = workbook.createSheet(keyDesc+e);
							rowno=0;
							continue;
						}
					}
					i++;
					trace("	value of i at the bottom --	"+i+"	rownum -- "+rowno);
				}				
				workbook.write(output_stream);
				setFeeReport(defaultname);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush();
				trace("Close file");
				return "transactionexcel";
			}catch (Exception e) {
				addActionError("Unable to continue the report generation");
				trace("Excption in Excelmethod ::: "+e.getMessage());	
				e.printStackTrace();	
			}
			return "globalreporterror";
		}
	
		public void batchNoValidation() throws Exception{
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rrs = null;
			CBSConnection cbscon = new CBSConnection();
			con = cbscon.getCBSDBConnection();
			try{
				if(con != null){
					String count = null;
					String batchno=getRequest().getParameter("batchno");
					String batchnoexistsqry = "SELECT COUNT(1) CNT FROM DETB_UPLOAD_DETAIL WHERE BATCH_NO='"+batchno+"'";
					pstmt = con.prepareStatement(batchnoexistsqry);
					rrs = pstmt.executeQuery(batchnoexistsqry);
					while (rrs.next()) {
						count = rrs.getString("CNT");
					}
					if(!"0".equalsIgnoreCase(count)){
						getResponse().getWriter().write("1");
						return ;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(rrs != null){
					rrs.close();
				}
				pstmt.close();
				con.close();
			}
		}
		public String feeView(){
			
			IfpTransObj transact = commondesc.myTranObject("UPDATECOMM", txManager);
			String instid = comInstId();
			String frmdate =  getRequest().getParameter("fromdate");
			trace("from date getting" + fromdate);
			String tdate =  getRequest().getParameter("todate");
			trace("to date getting" + todate);
			String[] actioncode = getRequest().getParameterValues("actioncode");
			String batchno=getRequest().getParameter("batchno");
			String qrycondition = "";
			String subcond = "";
			try {
				if( actioncode != null ){
					for( int i=0; i<actioncode.length; i++ ){ 
							subcond += actioncode[i] +"','"; 
							System.out.println("subcondition " + subcond);
					}
					
					if (subcond.endsWith(",")) {
						subcond = subcond.substring(0, subcond.length() - 1);
						System.out.println("testing subcond " + subcond);
					}
					qrycondition += "CARDACTIVITY IN ('"+subcond+"') ";
					
				}
				qrycondition += "AND TRUNC(FEE_GEN_DATE) BETWEEN TO_DATE('"+frmdate+"','dd-mm-yyyy') AND TO_DATE('"+tdate+"','dd-mm-yyyy') ";
										
				enctrace("final where clause " +qrycondition);
				List<Map<String,Object>> recordlist = getFeeViewList(instid,qrycondition,batchno,jdbctemplate); 

				if(recordlist.isEmpty()){
					trace("No Records found");
					addActionError("No Records Found");
					return "required_home";
				}else{
					setFeelist(recordlist);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			fromdate = frmdate;
			todate = tdate;
			actioncode = this.actioncode;
			batchno = this.batchno;
			
			
			return "feelist";
		}
		public String deleteFee(){
			IfpTransObj transact = commondesc.myTranObject("UPDATECOMM", txManager);
			try{
				String instid = comInstId();
				int updateqry = 0;
				String[] cardno = getRequest().getParameterValues("instorderrefnum"); 
				for(int i=0;i<cardno.length;i++){
					trace("updateqry fee flag delete cardno" +cardno[i]);
					updateqry = updateFeeFlagByCardNo(instid,cardno[i],jdbctemplate);
					enctrace("updateqry fee flag delete " +updateqry);
				}
				if(updateqry > 0){
					txManager.commit(transact.status);
				}else{
					txManager.rollback(transact.status);
					addActionError("Unable to continue");
					return "required_home";
				}
			}
			catch (Exception e) {
				txManager.rollback(transact.status);
				e.printStackTrace();
			}
			return "deletesuccess";
		}
		
		public List feelist;
		public String fromdate;
		public String todate;
		public String batchno;
		public String[] actioncode; 
		public String[] cardno;
		
		public String[] getCardno() {
			return cardno;
		}

		public void setCardno(String[] cardno) {
			this.cardno = cardno;
		}

		public String getFromdate() {
			return fromdate;
		}

		public void setFromdate(String fromdate) {
			this.fromdate = fromdate;
		}

		public String getTodate() {
			return todate;
		}

		public void setTodate(String todate) {
			this.todate = todate;
		}

		public String getBatchno() {
			return batchno;
		}

		public void setBatchno(String batchno) {
			this.batchno = batchno;
		}

		public String[] getActioncode() {
			return actioncode;
		}

		public void setActioncode(String[] actioncode) {
			this.actioncode = actioncode;
		}

		public List getFeelist() {
			return feelist;
		}

		public void setFeelist(List feelist) {
			this.feelist = feelist;
		}
}
