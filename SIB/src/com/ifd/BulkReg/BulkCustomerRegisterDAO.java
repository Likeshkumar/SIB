package com.ifd.BulkReg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class BulkCustomerRegisterDAO extends BaseAction{

	

	public String getbrachuser(String instid, String cardno,  JdbcTemplate jdbctemplate ) {
		String BRANCH_CODE = null;
		try{
			String BRANCHCODE = "SELECT trim(CARD_COLLECT_BRANCH) FROM PERS_CARD_PROCESS WHERE ORG_CHN='"+cardno+"' AND  INST_ID='"+instid+"' ";
			enctrace("editcard :" + BRANCHCODE );
			BRANCH_CODE = (String)jdbctemplate.queryForObject(BRANCHCODE, String.class);
		}catch(EmptyResultDataAccessException e){}
		return BRANCH_CODE;
		
	}
	
	public List<Map<String, Object>> getEmbossingName(String cardno, String type, JdbcTemplate jdbctemplate) {
		String  embnameqry = "", cond = "";
		List custdetails = null;
		try { 
			if("CARD".equals(type)){
				cond = "CARD_NO='"+cardno+"'";
				
			}else if("phoneno".equals(type)){
				cond = "MOBILENO='"+cardno+"'";
				
			}
			else{
				cond = "CIN='"+cardno+"'";
				
			}
		
			embnameqry = "SELECT EMB_NAME,MOBILENO,CIN FROM CARD_PRODUCTION WHERE "+cond+" AND ROWNUM=1";
			
			//embnameqry = "SELECT A.EMB_NAME,A.MOBILENO,B.FNAME,B.MNAME,B.LNAME,TO_CHAR(B.DOB,'DD/MM/YYYY') AS DOB,B.MARITAL_STATUS,B.NATIONALITY,B.DOCUMENT_PROVIDED,B.DOCUMENT_NUMBER,B.SPOUCE_NAME,B.MOTHER_NAME,B.FATHER_NAME,B.E_MAIL,B.P_PO_BOX,B.P_HOUSE_NO,B.P_STREET_NAME,B.P_WARD_NAME,B.P_CITY,B.P_DISTRICT,B.P_PHONE1,B.P_PHONE2 FROM CARD_PRODUCTION A,CUSTOMERINFO B WHERE "+cond+" AND "+cond2+"";
			enctrace("embnameqry customer name edit-->"+embnameqry);
			custdetails = jdbctemplate.queryForList(embnameqry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;
	}

	public String checkValidWithCustid(String custid,JdbcTemplate jdbctemplate) {
		String validcustid = "", validcustidqry = "";
		try { 
			validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE CIN='"+custid+"' AND CARD_STATUS IN ('09','05')";
			enctrace("validcustidqry customer name edit-->"+validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			enctrace("validcustid in process-->"+validcustid);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return validcustid;
	}
	public String checkphoneno(String phoneno,JdbcTemplate jdbctemplate) {
		String validphoneno = "", validmobnoqry = "";
		try { 
			validmobnoqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE MOBILENO='"+phoneno+"' AND CARD_STATUS IN ('09','05')";
			enctrace("validcustidqry customer name edit-->"+validmobnoqry);
			validphoneno = (String) jdbctemplate.queryForObject(validmobnoqry, String.class);
			enctrace("validphoneno in process-->"+validphoneno);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return validphoneno;
	}

	public int updatedetailincust(BulkCustomerRegisterBean bean, String cardno,String userid, JdbcTemplate jdbctemplate) {
		int update=0;
		String cond = "",mobileupdateqry = "",validcustidqry = "",validcustid = "";
		if(bean.getCardno().length() > 6){
			cond = "CARD_NO = '"+cardno+"'";
		}else{
			cond = "CIN = '"+bean.getCustid()+"'";
		}
		try{
			validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE "+cond+" ";
			enctrace("validcustidqry customer name edit-->"+validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
		
			mobileupdateqry="INSERT INTO CUSTOMERINFO_PROCESS (INST_ID,ORDER_REF_NO,CIN,FNAME,MNAME,LNAME,DOB,GENDER,MARITAL_STATUS,NATIONALITY,";
			mobileupdateqry+="DOCUMENT_PROVIDED,DOCUMENT_NUMBER,SPOUCE_NAME,MOTHER_NAME,FATHER_NAME,MOBILE,E_MAIL,P_PO_BOX,P_HOUSE_NO,";
			mobileupdateqry+="P_STREET_NAME,P_WARD_NAME,P_CITY,P_DISTRICT,P_PHONE1,P_PHONE2,C_PO_BOX,C_HOUSE_NO,C_STREET_NAME,C_WARD_NAME";
			mobileupdateqry+=",C_CITY,C_DISTRICT,C_PHONE1,C_PHONE2,MAKER_DATE,MAKER_ID,CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS)";
			mobileupdateqry+=" SELECT ";
			mobileupdateqry+="INST_ID,ORDER_REF_NO,CIN,'"+bean.getFname()+"','"+bean.getMname()+"','"+bean.getLname()+"',TO_DATE('"+bean.getDob()+"','DD-MM-YY'),GENDER,'"+bean.getMartialstatus()+"',";
			mobileupdateqry+="'"+bean.getNationality()+"',DOCUMENT_PROVIDED,DOCUMENT_NUMBER,'"+bean.getSpousename()+"','"+bean.getMothername()+"',";
			mobileupdateqry+="'"+bean.getFathername()+"','"+bean.getMobileno()+"','"+bean.getEmail()+"','"+bean.getPobox()+"','"+bean.getPhouseno()+"','"+bean.getPstname()+"','"+bean.getPwardname()+"','"+bean.getPcity()+"','"+bean.getPdist()+"',";
			mobileupdateqry+="P_PHONE1,P_PHONE2,C_PO_BOX,C_HOUSE_NO,C_STREET_NAME,C_WARD_NAME,C_CITY,C_DISTRICT,C_PHONE1,";
			mobileupdateqry+="C_PHONE2,MAKER_DATE,'"+userid+"',CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS";
			mobileupdateqry+=" FROM CUSTOMERINFO WHERE CIN=(SELECT CIN FROM CARD_PRODUCTION WHERE "+cond+" AND ROWNUM=1)";
					 
			enctrace("mobileupdateqry-->"+mobileupdateqry);
			update = jdbctemplate.update(mobileupdateqry);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}
	
	
	public int updatezcustinfoswitch(BulkCustomerRegisterBean bean, String cardno,String hcardno,String padssenable, JdbcTemplate jdbctemplate) {
		int update=0;
		String cond = "",mobileupdateqry1 = "",validcustidqry = "",validcustid = "";
		if(padssenable.equals("Y")){
			if(bean.getCardno().length() > 6){
				cond = "CHN = '"+hcardno+"'";
			}else{
				cond = "CHN = '"+cardno+"'";			
			}
		}	
		
		
		try{
			validcustidqry = "SELECT COUNT(1) FROM EZCARDINFO WHERE "+cond+"";
			enctrace("validcustidqry customer name edit-->"+validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			if (!"0".equalsIgnoreCase(validcustid))					
			mobileupdateqry1 = "UPDATE EZCUSTOMERINFO SET MOBILE='"+bean.getMobileno()+"',NAME='"+bean.getEmbossingname()+"',DOB= TO_DATE('"+bean.getDob()+"','DD-MM-YY'),ADDRESS1='"+bean.getPobox()+"',ADDRESS2='"+bean.getPhouseno()+"',ADDRESS3='"+bean.getPstname()+"'";
			mobileupdateqry1+= "WHERE CUSTID=(SELECT CUSTID FROM EZCARDINFO WHERE "+cond+" AND ROWNUM=1)";
			enctrace("mobileupdateqry-->"+mobileupdateqry1);
			update = jdbctemplate.update(mobileupdateqry1);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}

	public List<Map<String, Object>> getCustDetails(String cin, String string, JdbcTemplate jdbctemplate) {

		String  embnameqry = "";
		List custdetails = null;
		try { 
			
			//embnameqry = "SELECT EMB_NAME,MOBILENO,CIN FROM CARD_PRODUCTION WHERE "+cond+" AND ROWNUM=1";
			embnameqry = "SELECT A.EMB_NAME AS EMB_NAME,NVL(A.MOBILENO,'--') AS MOBILENO,NVL(B.FNAME,'--') AS FNAME,NVL(B.MNAME,'--') AS MNAME,NVL(B.LNAME,'--') AS LNAME,B.DOB AS DOB,NVL(B.MARITAL_STATUS,'--') AS MARITAL_STATUS,NVL(B.NATIONALITY,'--') AS NATIONALITY,NVL(B.DOCUMENT_PROVIDED,'--') AS DOCUMENT_PROVIDED ,NVL(B.DOCUMENT_NUMBER,'--') AS DOCUMENT_NUMBER,NVL(B.SPOUCE_NAME,'--') AS SPOUCE_NAME,NVL(B.MOTHER_NAME,'--') AS MOTHER_NAME,NVL(B.FATHER_NAME,'--') AS FATHER_NAME,NVL(B.E_MAIL,'--') AS E_MAIL,NVL(B.P_PO_BOX,'--') AS P_PO_BOX, NVL(B.P_HOUSE_NO,'--') AS P_HOUSE_NO,NVL(B.P_STREET_NAME,'--') AS P_STREET_NAME,NVL(B.P_WARD_NAME,'--') AS P_WARD_NAME,NVL(B.P_CITY,'--') AS P_CITY,NVL(B.P_DISTRICT,'--') AS P_DISTRICT,B.P_PHONE1,B.P_PHONE2,A.HCARD_NO as HCARD_NO FROM CARD_PRODUCTION A,CUSTOMERINFO B WHERE A.CIN='"+cin+"' AND B.CIN=A.CIN";
			
			enctrace("cin customer name edit-->"+embnameqry);
			custdetails = jdbctemplate.queryForList(embnameqry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;
	
	}

	public int updatedetailincustinfo(BulkCustomerRegisterBean bean, String cardno,String userid,String padssenable,String hcardno, JdbcTemplate jdbctemplate) {
		int update=0;
		String cond = "",mobileupdateqry = "",validcustidqry = "",validcustid = "";
		 
		try{
			validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE CARD_NO='"+cardno+"'";
			enctrace("validcustidqry customer name edit-->"+validcustidqry);
									
			
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			if(!"0".equalsIgnoreCase(validcustid)){
				
				//mobileupdateqry = "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+"', WHERE CIN=(SELECT CIN FROM CARD_PRODUCTION WHERE "+cond+" AND ROWNUM=1)";
				mobileupdateqry = "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+"',FNAME='"+bean.getFname()+"',MNAME='"+bean.getMname()+"',LNAME='"+bean.getLname()+"',DOB=TO_DATE('"+bean.getDob()+"','DD-MM-YY'),MARITAL_STATUS='"+bean.getMartialstatus()+"',NATIONALITY='"+bean.getNationality()+"',SPOUCE_NAME='"+bean.getSpousename()+"',";
				mobileupdateqry +="MOTHER_NAME='"+bean.getMothername()+"',FATHER_NAME='"+bean.getFathername()+"',E_MAIL='"+bean.getEmail()+"',P_PO_BOX='"+bean.getPobox()+"',P_HOUSE_NO='"+bean.getPhouseno()+"',P_STREET_NAME='"+bean.getPstname()+"',P_WARD_NAME='"+bean.getPwardname()+"',P_CITY='"+bean.getPcity()+"',P_DISTRICT='"+bean.getPcity()+"'";
				mobileupdateqry +=" WHERE CIN=(SELECT CIN FROM CARD_PRODUCTION WHERE CARD_NO='"+cardno+"' AND ROWNUM=1)";
			}
														
			enctrace("authcustdetails-->"+mobileupdateqry);
			update = jdbctemplate.update(mobileupdateqry);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}
	
	
	public int updatecustoemrinfo(BulkCustomerRegisterBean bean, String cardno,String userid,String padssenable,String hcardno, JdbcTemplate jdbctemplate) {
		int update=0;
		String cond = "",mobileupdateqry = "",validcustidqry = "",validcustid = "";
		 
		try{
			validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE CARD_NO='"+cardno+"'";
			enctrace("validcustidqry customer name edit-->"+validcustidqry);
									
			
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			if(!"0".equalsIgnoreCase(validcustid)){
				
				//mobileupdateqry = "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+"', WHERE CIN=(SELECT CIN FROM CARD_PRODUCTION WHERE "+cond+" AND ROWNUM=1)";
				mobileupdateqry = "UPDATE EZCUSTOMERINFO SET MOBILE='"+bean.getMobileno()+"',NAME='"+bean.getMname()+"',DOB=TO_DATE('"+bean.getDob()+"','DD-MM-YY'),";
				mobileupdateqry +="EMAIL='"+bean.getEmail()+"',ADDRESS1='"+bean.getPobox()+"',ADDRESS2='"+bean.getPhouseno()+"',ADDRESS3='"+bean.getPstname()+"'";
				mobileupdateqry +=" WHERE CUSTID=(SELECT CIN FROM CARD_PRODUCTION WHERE CARD_NO='"+cardno+"' AND ROWNUM=1)";
			}
														
			enctrace("authcustdetails-->"+mobileupdateqry);
			update = jdbctemplate.update(mobileupdateqry);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}
	
	public int getBulkfilenameCount(String instid, String uploadFileFileName,JdbcTemplate jdbctemplate) {
		int result= -1;
		trace("Getting getBulkfilenameCount ");
		String qury="select COUNT(1) from BULK_REG_STATUS where inst_id='"+instid+"' AND FILENAME = '"+uploadFileFileName+"'";
		enctrace("getBulkfilenameCount : " + qury); 
		result =jdbctemplate.queryForInt(qury);     
		return result;
		
	}
	

	public List<Map<String, Object>> GetBatchCustomer(String instid, String BatchID, JdbcTemplate jdbctemplate) 
	{
		String  BatchCustQry = "";
		List custdetails = null;
		try { 
			
			/*BatchCustQry = "SELECT DISTINCT BATCH_ID,BULK_REG_REF_ID,CUSTOMER_ID,MOBILE,P_ACCOUNT_NO,TO_DATE(DOB,'MM-DD-YY')AS DOB,NAME,REG_STATUS,(SELECT CARD_TYPE_NAME "
					+ " FROM PRODUCT_MASTER   WHERE  PRODUCT_CODE=PRODUCT_TYPE)PRODUCT_TYPE,SUBSTR(PRODUCT_TYPE,1,10)PRODUCTCODE , "
					+ "DECODE(ACCT_TYPE,'01','SAVINGS','02','CURRENT') ACCOUNT_TYPE  FROM CUSTOMER_BULK_LOAD WHERE BATCH_ID='"+BatchID+"' AND REG_STATUS NOT IN('Success')";
			*/
			BatchCustQry = "SELECT DISTINCT BATCH_ID,BULK_REG_REF_ID,CUSTOMER_ID,MOBILE,P_ACCOUNT_NO,TO_DATE(DOB,'DD-MM-YY')AS DOB,NAME,REG_STATUS,(SELECT CARD_TYPE_NAME "
					+ " FROM PRODUCT_MASTER   WHERE  PRODUCT_CODE=PRODUCT_TYPE)PRODUCT_TYPE,SUBSTR(PRODUCT_TYPE,1,10)PRODUCTCODE , "
					+ "DECODE(ACCT_TYPE,'01','SAVINGS','02','CURRENT') ACCOUNT_TYPE  FROM CUSTOMER_BULK_LOAD WHERE BATCH_ID='"+BatchID+"' AND REG_STATUS NOT IN('Success')";
			
			enctrace("Batch Id Based Customer List------>"+BatchCustQry);
			custdetails = jdbctemplate.queryForList(BatchCustQry);
			enctrace("Batch Id List-->"+custdetails);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;
	}
	
	
	public List  getBatchRecords(String instid,String batchId,JdbcTemplate jdbctemplate){
	
		String  BatchCustQry = "";
		List custdetails = null;
		try { 
			
			BatchCustQry = "SELECT DISTINCT BATCH_ID,BULK_REG_REF_ID,CUSTOMER_ID,MOBILE,P_ACCOUNT_NO,DOB,NAME,REG_STATUS FROM CUSTOMER_BULK_LOAD WHERE BATCH_ID='"+batchId+"' AND REG_STATUS NOT IN('Success')";
			
			enctrace("Batch Id Based Customer List-->"+BatchCustQry);
			custdetails = jdbctemplate.queryForList(BatchCustQry);
			enctrace("Batch Id List-->"+custdetails);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;
	
	}
	
	
	
	
	
	public List<Map<String, Object>> getbulkreissuecustomerdetaisl(String instid, String PRODCUT, String BRANCH,JdbcTemplate jdbctemplate) 
	{
		String  allcardsforreissu = "";
		List custdetails = null;String condi="";
		if(PRODCUT.equalsIgnoreCase("ALL")){
			condi="";
		}
		else{
			
			condi="AND A.PRODUCT_CODE='"+PRODCUT+"'";
		}
		try { 
			
			allcardsforreissu = "SELECT A.HCARD_NO,A.ORDER_REF_NO,A.MCARD_NO,A.ACCOUNT_NO,A.CIN,A.EMB_NAME,to_char(A.MAKER_DATE, 'dd-MON-yyyy') as LSDATE  from CARD_PRODUCTION A,EZCARDINFO B WHERE A.INST_ID=B.INSTID  AND a.HCARD_NO= b.CHN "+condi+" AND  B.STATUS='74'";
			
			enctrace("allcardsforreissu Based Customer List-->"+allcardsforreissu);
			custdetails = jdbctemplate.queryForList(allcardsforreissu);
			enctrace("allcardsforreissu>"+custdetails);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;
	}
	public int deleterecords(String instid, String BatchID, JdbcTemplate jdbctemplate) 
	{
		int x=0;
		String  Batchdelete = "";
		
		try { 
			
			Batchdelete = "DELETE FROM CUSTOMER_BULK_LOAD WHERE BATCH_ID='"+BatchID+"' AND REG_STATUS NOT IN('Success')";
			
			enctrace("Batch Id DELETE List-->"+Batchdelete);
			x = jdbctemplate.update(Batchdelete);
			enctrace("Batch Id delete List-->"+x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	
	public int updateproductionezcardinfo(String instid, String cardno, String usercode,String  mobileNumber,String crdcltBranch,JdbcTemplate jdbctemplate) 
	{
		int x=0;
		String  Batchdelete = "";
		
		try { 
    String production ="UPDATE CARD_PRODUCTION SET CARD_STATUS='03',STATUS_CODE='74',MAKER_DATE=SYSDATE,BRANCH_CODE='"+crdcltBranch+"' ,CARD_COLLECT_BRANCH='"+crdcltBranch+"' ,MOBILENO='"+mobileNumber+"',MAKER_ID='"+usercode+"' WHERE HCARD_NO='"+cardno+"'AND INST_ID='"+instid+"' ";

			
			
			enctrace("update production List-->"+production);
			x = jdbctemplate.update(production);
			if(x>0){
			    String ezcardinfoqry ="UPDATE EZCARDINFO SET STATUS='74' WHERE CHN='"+cardno+"'AND INSTID='"+instid+"' ";
				x = jdbctemplate.update(ezcardinfoqry);
			}else{
				trace("not updating "+x);
				x=0;
			}
			if(x>0){
			    String ezcardinfoqry ="UPDATE EZCUSTOMERINFO SET MOBILE='"+mobileNumber+"' WHERE INSTID='"+instid+"' AND CUSTID IN (SELECT CIN FROM CARD_PRODUCTION WHERE HCARD_NO='"+cardno+"'AND INST_ID='"+instid+"') ";
				x = jdbctemplate.update(ezcardinfoqry);
			}else{
				trace("not updating "+x);
				x=0;
			}
			enctrace("ezcardinfoqry List-->"+x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	
	public int insertnotavailable (String instid, String filename,String cardno,int lineno, String userid,long randomreportid,String hcardno,String notype,String REISSUE,String crdcltBranch,JdbcTemplate jdbctemplate) 
	{
		int x=0,y=0,branchcount=0;
		String errmsg = "";
		String res ="";
		
		
		  String checkqry1 ="select COUNT(*) FROM  EZCARDINFO WHERE CHN='"+hcardno+"'AND INSTID='"+instid+"' ";
		  y= jdbctemplate.queryForInt(checkqry1);
		  System.out.println("cardincome befpore"+cardno+" \n status "+ y+"\n checkqry1"+checkqry1);
		  if(y>0){
		System.out.println("cardincome"+cardno);
	    String checkqry ="select STATUS FROM  EZCARDINFO WHERE CHN='"+hcardno+"'AND INSTID='"+instid+"' ";
	  
	  
	    
	    
	  	res = jdbctemplate.queryForObject(checkqry.toString(), String.class).toString();
	 
		if(res.equals("74")){
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD Already Markd to lost stolen] "+lineno;
		}else if (res.equals("61")) {
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD  Markd to Repin ] Waiting For PIN Generation:"+lineno;
		}else if (res.equals("62")) {
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD  Markd to Reissue]:"+lineno;
		}else if (res.equals("77")) {
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD  Markd to Damage] Waiting For PRE Generation:"+lineno;
		}else if (res.equals("16") ) {
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD is Not Active] :"+lineno;
		}else if (res.equals("75")) {
			errmsg = "ERROR-["+cardno+"]-This Card is [Card PinLocked]:"+lineno;
		}
		else if (res.equals("78")) {
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD  Markd to Renewal]:"+lineno;
		}
		 else if (notype.equals("110")){
			  errmsg = "ERROR-["+crdcltBranch+"]-This Branch Id Is not Configured:"+lineno;
		  }
		/*else if (res.equals("97")&& REISSUE.equals("LS")) {
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD Is Not Activated  ]:"+lineno;
		}*/
		/*else if (notype.equals("200")) {
			errmsg = "ERROR-["+cardno+"]-This Card is [CARD  Not Eligible for Expiry]:"+lineno;
		}*/
		
		else{
			errmsg = "Card Not expired"+cardno;
		}		
		  }else if (notype.equals("100")){
			  errmsg = "ERROR-["+cardno+"]-This card is Not Either in[ RENEW/RESSUE ]:"+lineno;
		  }
	
		 
		 /* else if (notype.equals("200")){
			  errmsg = "ERROR-["+cardno+"]-This card is Not Expiredd]:"+lineno;
		  }*/
		  else{
			  errmsg="NO card found in DB -[ "+cardno+" ]"; 
		  }
	    
	    
	    
		
		try { 
       String insertfail ="INSERT INTO BULKFAIL_REG_STATUS (inst_id,filename,line_no,reason,added_by,added_date,REPORTRANDOMNO,FAIL_REJECT)VALUES ('"+instid+"','"+filename+"','"+lineno+"','"+errmsg+"','"+userid+"',SYSDATE,'"+randomreportid+"','F')";

			
			
			
			x = jdbctemplate.update(insertfail);
			
			enctrace("insertfail List-->"+x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	
	public int insertbulkregcardsstatus (String instid, String filename,String custid,int lineno, String userid,long randomreportid,String finalgotvalue,String resperrmsg,JdbcTemplate jdbctemplate) 
	{
		int x=0,y=0;
		String totalfinalerrormsg=" ["+resperrmsg+" ] ";
		
		try { 
       String insertfail ="INSERT INTO BULKFAIL_REG_STATUS (inst_id,filename,line_no,reason,added_by,added_date,REPORTRANDOMNO,FAIL_REJECT)VALUES ('"+instid+"','"+filename+"','"+lineno+"','"+totalfinalerrormsg+"','"+userid+"',SYSDATE,'"+randomreportid+"','F')";

			
			
			
			x = jdbctemplate.update(insertfail);
			
			enctrace("insertbulkregcardsstatus -->"+x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	
	public List findaccountduplication (String instid,String pacct,String s1acct,String s2acct,String s3acct,String s4acct,String s5acct,JdbcTemplate jdbctemplate) 
	{
		int x=0,y=0;
		List allacount=null;
		try { 
			
			String n="DUP";
			int i=0,k,t;
			
			
				  String insertfail ="SELECT COUNT(*) FROM ACCOUNTINFO WHERE ACCOUNTNO='"+pacct+"' OR ACCOUNTNO='"+s1acct+"' OR ACCOUNTNO='"+s2acct+"' OR ACCOUNTNO='"+s3acct+"' OR ACCOUNTNO='"+s4acct+"' OR ACCOUNTNO='"+s5acct+"'";
					
					x = jdbctemplate.queryForInt(insertfail);
					
						if(x>0 ){
							  String allaccountnumbers ="SELECT ACCOUNTNO FROM ACCOUNTINFO WHERE ACCOUNTNO='"+pacct+"' OR ACCOUNTNO='"+s1acct+"' OR ACCOUNTNO='"+s2acct+"' OR ACCOUNTNO='"+s3acct+"' OR ACCOUNTNO='"+s4acct+"' OR ACCOUNTNO='"+s5acct+"'";
							  allacount=jdbctemplate.queryForList(allaccountnumbers);
						}
						else{
							return allacount;
						}
				//System.out.println("dups"+x);
				
			
			enctrace("insertbulkregcardsstatus -->"+x);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		return allacount;
	}
	
	public int checkcardcount(String instid, String cardno, JdbcTemplate jdbctemplate) 
	{
		int x=0;
		
		
		try { 
    String checkqry ="select count(*) from card_production a,ezcardinfo b where a.inst_id=b.instid and a.hcard_no=b.chn and b.status in ('50','53','70','97','75','16') AND  a.HCARD_NO='"+cardno+"'AND a.INST_ID='"+instid+"' ";

			
			
			enctrace("update checkcardsforbulkloststolen List-->"+checkqry);
			x = jdbctemplate.queryForInt(checkqry);
		
			enctrace("checkcardsforbulkloststolen -->"+x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	public int checkbranch(String instid, String crdcltBranch, JdbcTemplate jdbctemplate) 
	{
		int x=0;
		
		
		try { 
    String checkqry ="select count(*) from  BRANCH_MASTER WHERE BRANCH_CODE='"+crdcltBranch+"' AND INST_ID='"+instid+"' ";

			
			
			enctrace(" checkbranch COUNT FOR LOSTSTOLEN/REISSUE-->"+checkqry);
			x = jdbctemplate.queryForInt(checkqry);
		
			enctrace("checkbranch-->"+x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	public int checkcardexpiry(String instid, String cardno,String renewalperiods, JdbcTemplate jdbctemplate) 
	{
		int x=0;
		
		try { 
           String checkqry ="select COUNT(*) from card_production a,ezcardinfo b where a.inst_id=b.instid and a.hcard_no=b.chn  AND A.CAF_REC_STATUS NOT IN ('BN','S','D','DE') AND b.status not in ('70','62','78') AND  a.HCARD_NO='"+cardno+"'AND   EXPIRYDATE between add_months(sysdate,-120) and add_months(sysdate,"+renewalperiods+") AND a.INST_ID='"+instid+"' ";

			
			
			enctrace("update checkcardsforbulkloststolen List-->"+checkqry);
			x = jdbctemplate.queryForInt(checkqry);
		
			enctrace("checkcardsforbulkloststolen -->"+x);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	public List chekcardsforbulkrenewal(String instid, String cardno,String renewalperiods, JdbcTemplate jdbctemplate) 
	{
		int x=0,x1=0;
		List subprod =null;
		
		try { 
			String chckcardinprocess="SELECT COUNT(*) FROM PERS_CARD_PROCESS WHERE HCARD_NO='"+cardno+"'AND INST_ID='"+instid+"' ";

			x = jdbctemplate.queryForInt(chckcardinprocess);
			if(x > 0){
				System.out.println("coming to process");
				return subprod;
			}
			
			
			else{
				
    String checkqry ="select count(org_chn)AS COUNT,SUB_PROD_ID from card_production a,ezcardinfo b where a.inst_id=b.instid and a.hcard_no=b.chn  AND A.CAF_REC_STATUS NOT IN ('BN','S','D','DE') AND b.status not in ('70','62','78') AND  a.HCARD_NO='"+cardno+"'AND   EXPIRYDATE between add_months(sysdate,-120) and add_months(sysdate,"+renewalperiods+") AND a.INST_ID='"+instid+"' group by (org_chn,sub_prod_id) ";

			
			
			enctrace(" chekcardsforbulkrenewal List-->"+checkqry);
			subprod = jdbctemplate.queryForList(checkqry);
		
			//enctrace("chekcardsforbulkrenewal -->"+x);
		}
			}
		catch (Exception e) {
			e.printStackTrace();
		}
		return subprod;
	}
	
	public int checkcardsavaiable(String instid, String cardno,String renewalperiods, JdbcTemplate jdbctemplate) 
	{
		int x=0;
		List subprod = null;
		
		try { 
			
    String checkqry ="select COUNT(*) from card_production a,ezcardinfo b where a.inst_id=b.instid and a.hcard_no=b.chn  AND A.CAF_REC_STATUS NOT IN ('BN','S','D','DE') AND b.status not in ('70','62','78') AND  a.HCARD_NO='"+cardno+"'AND   EXPIRYDATE between add_months(sysdate,-120) and add_months(sysdate,"+renewalperiods+") AND a.INST_ID='"+instid+"' ";

			
			
			enctrace(" chekcardsforbulkrenewal List-->"+checkqry);
			x = jdbctemplate.queryForInt(checkqry);
		
			//enctrace("chekcardsforbulkrenewal -->"+x);
		
			}
		catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	public List<Map<String, Object>> GetBatchIDList(String instid, JdbcTemplate jdbctemplate) 
	{
		String  BatchIDGroupQry = "";
		List Batchdetails = null;
		try { 
			//SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where REG_STATUS='Pending'  GROUP BY BATCH_ID
			//BatchIDGroupQry = "SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where BATCH_ID LIKE'BATCH_%' AND REG_STATUS='Pending' GROUP BY BATCH_ID";
			
			BatchIDGroupQry = "SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where BATCH_ID LIKE'CBSREG_%' AND REG_STATUS='Pending' GROUP BY BATCH_ID";
			
			enctrace("Batch Id List-->"+BatchIDGroupQry);
			Batchdetails = jdbctemplate.queryForList(BatchIDGroupQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Batchdetails;
	}
	
	public List<Map<String, Object>> GetBatchIDList1(String instid, JdbcTemplate jdbctemplate) 
	{
		String  BatchIDGroupQry = "";
		List Batchdetails = null;
		try { 
			//SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where REG_STATUS='Pending'  GROUP BY BATCH_ID
			BatchIDGroupQry = "SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where BATCH_ID LIKE'BATCH_MASTER_%' AND REG_STATUS='Pending' GROUP BY BATCH_ID";
			
			enctrace("Batch Id List-->"+BatchIDGroupQry);
			Batchdetails = jdbctemplate.queryForList(BatchIDGroupQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Batchdetails;
	}
	public List<Map<String, Object>> getuplaodedfile(String instid, JdbcTemplate jdbctemplate) 
	{
		String  BatchIDGroupQry = "";
		List Batchdetails = null;
		try { 
			//SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where REG_STATUS='Pending'  GROUP BY BATCH_ID
			BatchIDGroupQry = "SELECT BATCH_ID FROM CARD_BULK_REPLACEMENT where REG_STATUS='Pending' GROUP BY BATCH_ID";
			
			enctrace("Batch Id List-->"+BatchIDGroupQry);
			Batchdetails = jdbctemplate.queryForList(BatchIDGroupQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Batchdetails;
	}
	public List<Map<String, Object>> getallbrach(String instid, JdbcTemplate jdbctemplate) 
	{
		String  BatchIDGroupQry = "";
		List Batchdetails = null;
		try { 
			//SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where REG_STATUS='Pending'  GROUP BY BATCH_ID
			BatchIDGroupQry = "SELECT BATCH_ID FROM BRANCH_INFO where REG_STATUS='Pending' GROUP BY BATCH_ID";
			
			enctrace("Batch Id List-->"+BatchIDGroupQry);
			Batchdetails = jdbctemplate.queryForList(BatchIDGroupQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Batchdetails;
	}
	public List<Map<String, Object>> getregstatusofcards(String instid, JdbcTemplate jdbctemplate) 
	{
		String reptypeGroupQry = "";
		List Batchdetails = null;
		try { 
			//SELECT BATCH_ID FROM CUSTOMER_BULK_LOAD where REG_STATUS='Pending'  GROUP BY BATCH_ID
			reptypeGroupQry = "SELECT DISTINCT REPLACEMENT_TYPE FROM CARD_BULK_REPLACEMENT where REG_STATUS='Pending' GROUP BY REPLACEMENT_TYPE";
			
			enctrace("Batch Id List-->"+reptypeGroupQry);
			Batchdetails = jdbctemplate.queryForList(reptypeGroupQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Batchdetails;
	}
	
	public List<Map<String, Object>> getProductList(String instid, JdbcTemplate jdbctemplate) 
	{
		String  BatchIDGroupQry = "";
		List proddetails = null;
		try 
		{ 
			
			BatchIDGroupQry = "SELECT PRODUCT_CODE, CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE BIN_FLAG='1' AND INST_ID='"+instid+"'  GROUP BY PRODUCT_CODE,CARD_TYPE_NAME ";			
			enctrace("proddetails List-->"+BatchIDGroupQry);
			proddetails = jdbctemplate.queryForList(BatchIDGroupQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return proddetails;
	}
	
	public List<Map<String, Object>> getProductList1(String instid, JdbcTemplate jdbctemplate) 
	{
		String  BatchIDGroupQry = "";
		List proddetails = null;
		try 
		{ 
			
			BatchIDGroupQry = "SELECT PRODUCT_CODE, CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE BIN_FLAG='2' AND INST_ID='"+instid+"'  GROUP BY PRODUCT_CODE,CARD_TYPE_NAME ";		
			enctrace("proddetails List-->"+BatchIDGroupQry);
			proddetails = jdbctemplate.queryForList(BatchIDGroupQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return proddetails;
	}
	
	public  String  getAccountSubType(String instid, String acctTpyeId,JdbcTemplate jdbctemplate) 
	{
		String  subTypeIdQuery = "";
		String  subType = null;
		try 
		{ 
			
			subTypeIdQuery = "SELECT ACCTSUBTYPEID  FROM  ACCTSUBTYPE WHERE INST_ID='"+instid+"'  AND ACCTTYPEID='"+acctTpyeId+"' ";		
			enctrace("subTypeIdQuery-->"+subTypeIdQuery);
			subType = (String) jdbctemplate.queryForObject(subTypeIdQuery, String.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return subType;
	}
	


	public  List   getSubProdDetails(String instid, String productCode,JdbcTemplate jdbctemplate) 
	{
		String  subProductDetailsQry = "";
		List  subProdDetails = null;
		try 
		{ 
			
			subProductDetailsQry = "SELECT SUB_PROD_ID,SUB_PRODUCT_NAME,FEE_CODE,LIMIT_ID  FROM   INSTPROD_DETAILS  WHERE INST_ID='"+instid+"'  AND PRODUCT_CODE='"+productCode+"' ";
			
			enctrace("subProductDetailsQry-->"+subProductDetailsQry);
			subProdDetails =jdbctemplate.queryForList(subProductDetailsQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return subProdDetails;
	}
	

	public List getCustomerDetails(String cin,String accountNumber,JdbcTemplate jdbctemplate){
		
		
		String  custDetailsQry = "";
		List  custDetails = null;
		try 
		{ 
			
			custDetailsQry = "SELECT   CUSTOMER_ID,NAME,DOB,NVL(GENDER,'--')GENDER,NVL(MARITAL_STATUS,'--')MARITAL_STATUS,BULK_REG_REF_ID,NVL(NATIONALITY,'--')NATIONALITY,"
					+ "NVL(DOCUMENT_PROVIDED,'--')DOCUMENT_PROVIDED,NVL(DOCUMENT_NUMBER,'--')DOCUMENT_NUMBER,NVL(MOBILE,'--')MOBILE,"
					+ "NVL(E_MAIL,'--')E_MAIL,NVL(P_PO_BOX,'--')P_PO_BOX,NVL(P_HOUSE_NO,'--')P_HOUSE_NO,NVL(P_STREET_NAME,'--')P_STREET_NAME ,"
					+ "NVL(P_DISTRICT,'--')P_DISTRICT,P_ACCOUNT_NO,P_CURRENCY_CODE,REG_STATUS,BRANCH,PRODUCT_TYPE,ACCT_TYPE  FROM CUSTOMER_BULK_LOAD "
					+ " WHERE CUSTOMER_ID='"+cin+"' ";
			
			enctrace("custDetailsQry-->"+custDetailsQry);
			custDetails =jdbctemplate.queryForList(custDetailsQry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return custDetails;
	}
	
	
	
}
