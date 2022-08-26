package com.ifp.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;
import com.ifp.beans.InstCardRegisterProcessBeans;


@Transactional
public class InstCardRegisterProcessDAO extends BaseAction 
{ 
	
	private static final long serialVersionUID = -8376161637970676446L;
	
	public int checkValidCardNumber(String instid, String cardno,	JdbcTemplate jdbctemplate) { 
		String checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("checexist : " + checexist );
		int x = jdbctemplate.queryForInt(checexist);
		return x;
	}

	public int checkValidAccNumber(String instid, String accountno,	JdbcTemplate jdbctemplate2) { 
		String checexist = "SELECT COUNT(*) AS CNT FROM CARD_ACCOUNT_INFO WHERE  trim(P_ACCT_NO)='"+accountno+"'";
	/*	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'*/
		enctrace("checexist : " + checexist );
		int x = jdbctemplate2.queryForInt(checexist);
		return x;
	}
	public List getaccountdetaCBS( String instid, String accountno,String cardregbean, JdbcTemplate jdbctemplate2  )  throws Exception  {
		List custdetail  = null;
		
		String custdetailsqry = "SELECT P_ACCT_NO, COD_PROD, CUSTOMERID, CUST_NAME, BRANCH_CODE, ADDRESS_1, ADDRESS_2, ADDRESS_3, CITY,STATE,ZIP,DOB,SEX,PHONENUMBER,EMAILID,MOTHERS_NAME  FROM CARD_ACCOUNT_INFO WHERE trim(P_ACCT_NO)='"+accountno+"' AND ROWNUM=1";
		enctrace( "custdetailsqry : " + custdetailsqry );
		custdetail = jdbctemplate2.queryForList(custdetailsqry); 
		return custdetail;
	}
	public int checkcustomeridexist(String instid, String accountno,	JdbcTemplate jdbctemplate) {
		int x=-1;
		String checexist = "SELECT COUNT(*) AS CNT FROM ACCOUNTINFO WHERE INST_ID='"+instid+"' AND ACCOUNTNO='"+accountno+"'";
	/*	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'*/
		enctrace("check account exist : " + checexist );
		x = jdbctemplate.queryForInt(checexist);
		return x;
	}
	public List getaccttype( String instid, JdbcTemplate jdbctemplate   ) throws Exception{ 
		List accprod = null;
		
		String query="SELECT DISTINCT ACCTTYPEID,ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"'";
		enctrace(  " get accprod list : " + query ); 
	    accprod = jdbctemplate.queryForList(query);
		
			
			return accprod;
		
	}
	public List getcurrency( String instid, JdbcTemplate jdbctemplate   ) throws Exception{ 
		List currcode = null;
		
		String query="SELECT NUMERIC_CODE,CURRENCY_DESC FROM GLOBAL_CURRENCY";
		enctrace(  " get currcodeList : " + query ); 
		currcode = jdbctemplate.queryForList(query);
		
			
			return currcode;
		
	}
	/**
	 *  PRASAD 
	 * @param instid
	 * @param cardno
	 * @param maptype
	 * @param jdbctemplate
	 * @return
	 */
	public int cardstatsusformap(String instid, String  cardno,String maptype,	JdbcTemplate jdbctemplate) {
		int x=-1;
		String condi="";
		if(maptype=="ORDERREFNO"){
			condi=" AND trim(ORDER_REF_NO)='"+cardno+"'";
		}else{
			condi=" AND trim(HCARD_NO)='"+cardno+"'";
		}
		//String checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and CARD_STATUS='06' AND MKCK_STATUS='M' "+condi+" ";
		
		String checexist="SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and CARD_STATUS='06' AND MKCK_STATUS='M' AND"
				+ " ORDER_REF_NO=(SELECT ORDER_REF_NO FROM INST_CARD_PROCESS_HASH WHERE HCARD_NO='"+cardno+"')";
	/*	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'*/
		
		x = jdbctemplate.queryForInt(checexist);
		if (x ==1)
		{
			System.out.println("income1");
			return x=1;
			
		}
		
		else if (x==0){
			 //checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and CARD_STATUS NOT IN('05') AND trim(HCARD_NO)='"+cardno+"'";
			x = jdbctemplate.queryForInt(checexist);
			
			
			if(x==1)
			{System.out.println("income2");
				return x=2;}
	
		
		}
		System.out.println("income5");
		enctrace("checkcardexist : " + checexist+"\n returnvalue "+x );
		return x;
	}
	public int cardavaialbel(String instid, String  cardno,String maptype,	JdbcTemplate jdbctemplate) {
		int x=-1;
		String condi="";
		if(maptype=="ORDERREFNO")
		{
			condi=" AND trim(ORDER_REF_NO)='"+cardno+"'";
		
		}
		
		else
		{
			condi=" AND trim(HCARD_NO)='"+cardno+"'";
		}
		String checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS_HASH WHERE INST_ID='"+instid+"' "+condi+" ";
	/*	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'*/
		
		trace("checexist ---->"+checexist);
		x = jdbctemplate.queryForInt(checexist);
		
		
		 if (x==0){
			//System.out.println("income1");
			return x=3;
		
		}
			return x;
	}
	public int cardcollectbranch(String instid, String  cardno,String maptype,	JdbcTemplate jdbctemplate) {
		int x=-1;
		
		System.out.println("cardnumber---->   "+cardno);
		
		String condi="";
		if(maptype=="ORDERREFNO"){
			condi=" AND trim(ORDER_REF_NO)='"+cardno+"'AND CARD_COLLECT_BRANCH is not null";
		}else{
			condi=" AND trim(HCARD_NO)='"+cardno+"' AND CARD_COLLECT_BRANCH is not null";
		}
		
		
		//String checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' "+condi+" ";
		
		String checexist = "SELECT COUNT(*) FROM INST_CARD_PROCESS INST_CARD_PROCESS  WHERE  INST_ID='"+instid+"' AND ORDER_REF_NO=(SELECT ORDER_REF_NO"
				+ " FROM INST_CARD_PROCESS_HASH WHERE  HCARD_NO='"+cardno+"') AND CARD_COLLECT_BRANCH is not null  ";
		
		x = jdbctemplate.queryForInt(checexist);
		
		
			return x;
	}
	
	public int samebranchcard(String instid, String  cardno,String branchid,	JdbcTemplate jdbctemplate) {
		int x=-1;
		
		
		String checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND trim(HCARD_NO)='"+cardno+"' AND TRIM(CARD_COLLECT_BRANCH)='"+branchid+"' ";
		enctrace("samebranchcardqry : " + checexist );
		x = jdbctemplate.queryForInt(checexist);
		
		return x;
		
	}	
	public int orderreadytomap(String instid, String  cardno,String maptype,	JdbcTemplate jdbctemplate) {
		int x=-1;
		String condi="";
		if(maptype=="ORDERREFNO"){
			condi=" AND trim(ORDER_REF_NO)='"+cardno+"'";
		}else{
			condi=" AND trim(CARD_NO)='"+cardno+"'";
		}
		String checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and CARD_STATUS='05' AND MKCK_STATUS='P' "+condi+" ";
	/*	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'*/
		enctrace("checkcardexist : " + checexist );
		x = jdbctemplate.queryForInt(checexist);
		
		return x;
	}
	
	

	public int cardstatusforReissu(String instid, String  hashcardno,JdbcTemplate jdbctemplate) {
		int x=-1;
		String checexist = "SELECT COUNT(*) AS CNT FROM EZCARDINFO WHERE INSTID='"+instid+"' and STATUS != '74' AND trim(CHN)='"+hashcardno+"'";
	/*	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'*/
		enctrace("reissucheckcardexist : " + checexist );
		
		String checexist1 = "SELECT COUNT(*) AS CNT FROM EZCARDINFO WHERE INSTID='"+instid+"' and STATUS= '62' AND trim(CHN)='"+hashcardno+"'";

		x = jdbctemplate.queryForInt(checexist);
		
		if(x !=1){
			enctrace("reiss x !=1 : "  );
			return 1;
		}else{
			x = jdbctemplate.queryForInt(checexist1);
			if(x >0){
				enctrace("x >0  for2: " );
				return 2;	
			}
			else{
				return x;
			}
		}
	}
	public List getReissucarddetails( String instid, String hcardno, JdbcTemplate jdbctemplate  )  throws Exception  {
		List custdetail  = null;
		String custdetailsqry =" select  TBL_A.ORDER_REF_NO,TBL_A.MCARD_NO,TBL_A.HCARD_NO,TBL_A.CARD_NO,TBL_A.ORG_CHN AS CHN,TBL_A.ACC_CCY,TBL_A.CARD_TYPE_ID, TBL_A.ACCOUNT_NO,TBL_A.BIN,TBL_A.PRODUCT_CODE,   TBL_A.WDL_AMT,TBL_A.WDL_CNT,TBL_A.PUR_AMT,TBL_A.PUR_CNT,TBL_A.SUB_PROD_ID,TBL_A.CARD_COLLECT_BRANCH,  TBL_B.FNAME AS  FNAME, TBL_B.LNAME AS LNAME,TBL_B.MOBILE,TBL_B.CIN,TBL_B.DOB,  TBL_B.P_PO_BOX,TBL_B.P_HOUSE_NO,TBL_B.P_STREET_NAME,TBL_B.P_WARD_NAME,TBL_B.P_CITY,TBL_B.P_DISTRICT  from  CARD_PRODUCTION TBL_A , CUSTOMERINFO TBL_B   WHERE  TBL_A.CIN = TBL_B.CIN AND  TBL_A.INST_ID = TBL_B.INST_ID AND  TBL_A.hcard_no='"+hcardno+"'";
				//" SELECT * from CARD_PRODUCTION where inst_id='"+instid+"' and CARD_NO='"+chn+"'"; 
		enctrace( "getcarddetailscustomermapReissue: " + custdetailsqry ); 
		custdetail = jdbctemplate.queryForList(custdetailsqry); 
		return custdetail;
	}
	public List getcarddetails( String instid, String chn,String maptype, JdbcTemplate jdbctemplate  )  throws Exception  {
		List custdetail  = null;
		String condi="";
		System.out.println("inside card get details");
		if(maptype=="ORDERREFNO")
		{
			condi=" AND trim(ORDER_REF_NO)='"+chn+"'";
		}
		else
		{
			condi=" AND ORDER_REF_NO=(SELECT ORDER_REF_NO FROM INST_CARD_PROCESS_HASH WHERE HCARD_NO='"+chn+"' )";
		}
		String custdetailsqry = " SELECT * from INST_CARD_PROCESS where CARD_STATUS='05' AND inst_id='"+instid+"' "+condi+" "; 
		enctrace("getcarddetailscustomermap : " + custdetailsqry); 
		custdetail = jdbctemplate.queryForList(custdetailsqry); 
		return custdetail;
	}
	
	public List getaccountdetails( String instid, String chn,String maptype, JdbcTemplate jdbctemplate2  )  throws Exception  {
		List accountdetails  = null;
		
		String custdetailsqry = " SELECT * from INST_CUST_DETAILS where ACCTNO='"+instid+"'"; 
		enctrace("getcarddetailscustomermap : " + custdetailsqry); 
		accountdetails = jdbctemplate2.queryForList(custdetailsqry); 
		return accountdetails;
	}
	public List getrejectcarddetailsforResissue( String instid, String chn, JdbcTemplate jdbctemplate  )  throws Exception  {
		List custdetailreissu  = null;
		/*String custdetailsqry = " SELECT ORDER_REF_NO,CARD_TYPE_ID,HCARD_NO,CARD_NO,TO_CHAR(EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE,PIN_OFFSET,ORG_CHN,USED_CHN from INST_CARD_PROCESS where inst_id='"+instid+"' and HCARD_NO='"+chn+"'";*/
		String custdetailsqry = " SELECT ORDER_REF_NO,CARD_TYPE_ID,(SELECT HCARD_NO FROM INST_CARD_PROCESS_HASH A WHERE A.ORDER_REF_NO=ORDER_REF_NO) as HCARD_NO,TO_CHAR(EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE,PIN_OFFSET,ORG_CHN,USED_CHN from INST_CARD_PROCESS where inst_id='"+instid+"' and ORG_CHN='"+chn+"'";
		enctrace( "getrejectcarddetailsforResissue1 : " + custdetailsqry ); 
		custdetailreissu = jdbctemplate.queryForList(custdetailsqry); 
		return custdetailreissu;
	}
	public int checkCardAlreadyRegistered(String instid, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		String checexist = "SELECT COUNT(*) AS CNT FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("checexist : " + checexist );
		int x = jdbctemplate.queryForInt(checexist);
		return x;
	}
	
	public List checkCardStatus( String instid, String ecardno, JdbcTemplate jdbctemplate  ) throws Exception {
		List statusrec  = null;
		String qry = "SELECT CARD_STATUS, MKCK_STATUS FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+ecardno+"'";
		enctrace("checkvalidCard qry__"+qry); 
		statusrec = jdbctemplate.queryForList(qry); 
		return statusrec;
	}
	
	
	public List getCustomerDetails( String instid, String customerid, JdbcTemplate jdbctemplate  )  throws Exception  {
		List custdetail  = null;
		/*String custdetailsqry = " SELECT FNAME, MNAME, LNAME, FATHER_NAME, MOTHER_NAME, MARITAL_STATUS, SPOUSE_NAME, GENDER, to_char(DOB,'dd-mm-yyyy') as DOB, NATIONALITY, EMAIL_ADDRESS, ";
		custdetailsqry += " MOBILE_NO, PHONE_NO, OCCUPATION, ID_NUMBER, ID_DOCUMENT, SECUTIRY_QUESTION, SECURITY_ANSWER, MAKER_DATE, MAKER_ID, CHECKER_DATE, ";
		custdetailsqry += " CHECKER_ID, MKCK_STATUS, CUSTOMER_STATUS, POST_ADDR1, POST_ADDR2, POST_ADDR3, POST_ADDR4, RES_ADDR1, RES_ADDR2, RES_ADDR3, RES_ADDR4, ";
		custdetailsqry += " KYC_FLAG, PHOTO_URL  FROM IFP_CUSTINFO_PRODUCTION WHERE INST_ID='"+instid+"' AND CIN='"+customerid+"' AND ROWNUM=1";*/
		String custdetailsqry = "SELECT FNAME, MNAME, LNAME, FATHER_NAME, MOTHER_NAME, MARITAL_STATUS, GENDER, DOB, NATIONALITY,DOCUMENT_NUMBER,MOBILE,DOCUMENT_PROVIDED,E_MAIL,(P_HOUSE_NO||','||P_STREET_NAME||','||P_CITY) as PA,(C_HOUSE_NO||','||C_STREET_NAME||','||C_CITY) as RA FROM CUSTOMERINFO WHERE INST_ID='"+instid+"' AND CIN='"+customerid+"' AND ROWNUM=1";
		enctrace( "custdetailsqry : " + custdetailsqry );
		custdetail = jdbctemplate.queryForList(custdetailsqry); 
		return custdetail;
	}
	
	public List getCustomerDetailsProcess( String instid, String customerid, JdbcTemplate jdbctemplate  )  throws Exception  {
		List custdetail  = null;
		String custdetailsqry = " SELECT FNAME, MNAME, LNAME, FATHER_NAME, MOTHER_NAME, MARITAL_STATUS, SPOUSE_NAME, GENDER, to_char(DOB,'dd-mm-yyyy') as DOB, NATIONALITY, EMAIL_ADDRESS, ";
		custdetailsqry += " MOBILE_NO, PHONE_NO, OCCUPATION, ID_NUMBER, ID_DOCUMENT, SECUTIRY_QUESTION, SECURITY_ANSWER, MAKER_DATE, MAKER_ID, CHECKER_DATE, ";
		custdetailsqry += " CHECKER_ID, MKCK_STATUS, CUSTOMER_STATUS, POST_ADDR1, POST_ADDR2, POST_ADDR3, POST_ADDR4, RES_ADDR1, RES_ADDR2, RES_ADDR3, RES_ADDR4, ";
		custdetailsqry += " KYC_FLAG, PHOTO_URL  FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+customerid+"' AND ROWNUM=1";
		enctrace( "custdetailsqry : " + custdetailsqry ); 
		custdetail = jdbctemplate.queryForList(custdetailsqry); 
		return custdetail;
	}
	
	public int insertCustomerDetails(String instid, InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate ) throws Exception {
		String insertcustinfo  = "";
		int x = -1;
		insertcustinfo += "INSERT INTO IFP_CUSTINFO_PROCESS (";
		insertcustinfo += "INST_ID, CIN, FNAME, MNAME, LNAME, FATHER_NAME, MOTHER_NAME, MARITAL_STATUS, SPOUSE_NAME, GENDER, DOB,";
		insertcustinfo += "NATIONALITY, EMAIL_ADDRESS, MOBILE_NO, PHONE_NO, OCCUPATION, ID_NUMBER, ID_DOCUMENT, POST_ADDR1, POST_ADDR2, POST_ADDR3, POST_ADDR4,";
		insertcustinfo += "RES_ADDR1, RES_ADDR2, RES_ADDR3, RES_ADDR4, CUSTOMER_STATUS, KYC_FLAG, PHOTO_URL  ) VALUES ( ";
		insertcustinfo += "'"+instid+"','"+cgbn.getCustid()+"','"+cgbn.getFirstname()+"','"+cgbn.getMidname()+"','"+cgbn.getLastname()+"', " ;
		insertcustinfo +=" '"+cgbn.getFahtername()+"','"+cgbn.getMothername()+"','"+cgbn.getMstatus()+"','"+cgbn.getSpname()+"','"+cgbn.getGender()+"',to_date('"+cgbn.getDob()+"','dd-mm-yyyy')";
		insertcustinfo += ",'"+cgbn.getNationality()+"','"+cgbn.getEmail()+"','"+cgbn.getPhoneno()+"','"+cgbn.getPhoneno()+"','"+cgbn.getOccupation()+"','"+cgbn.getDocumentid()+"', " ;
		insertcustinfo += " '"+cgbn.getReqdocuement()+"','"+cgbn.getPaddress1()+"','"+cgbn.getPaddress2()+"','"+cgbn.getPaddress3()+"','"+cgbn.getPaddress4()+"'";
		insertcustinfo += ",'"+cgbn.getResaddress1()+"', '"+cgbn.getResaddress2()+"', '"+cgbn.getResaddress3()+"', '"+cgbn.getResaddress4()+"', '1', '"+cgbn.getKycuser()+"', '"+cgbn.getPhotourl()+"'";
		insertcustinfo += ")";
		enctrace( "insertcustinfo : " + insertcustinfo );
		x = jdbctemplate.update(insertcustinfo);
		return x;
	}
	
	
	public int insertCustomerDetailsProduction(String instid, String customerid, InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate ) throws Exception {
		String insertcustinfo  = "";
		int x = -1;
		insertcustinfo += "INSERT INTO IFP_CUSTINFO_PRODUCTION (";
		insertcustinfo += "INST_ID, CIN, FNAME, MNAME, LNAME, FATHER_NAME, MOTHER_NAME, MARITAL_STATUS, SPOUSE_NAME, GENDER, DOB,";
		insertcustinfo += "NATIONALITY, EMAIL_ADDRESS, MOBILE_NO, PHONE_NO, OCCUPATION, ID_NUMBER, ID_DOCUMENT, POST_ADDR1, POST_ADDR2, POST_ADDR3, POST_ADDR4,";
		insertcustinfo += "RES_ADDR1, RES_ADDR2, RES_ADDR3, RES_ADDR4, CUSTOMER_STATUS, KYC_FLAG, PHOTO_URL, SIGNATURE_URL, IDPROOF_URL  ) VALUES ( ";
		insertcustinfo += "'"+instid+"','"+customerid+"','"+cgbn.getFirstname()+"','"+cgbn.getMidname()+"','"+cgbn.getLastname()+"', " ;
		insertcustinfo +=" '"+cgbn.getFahtername()+"','"+cgbn.getMothername()+"','"+cgbn.getMstatus()+"','"+cgbn.getSpname()+"','"+cgbn.getGender()+"',to_date('"+cgbn.getDob()+"','dd-mm-yyyy')";
		insertcustinfo += ",'"+cgbn.getNationality()+"','"+cgbn.getEmail()+"','"+cgbn.getPhoneno()+"','"+cgbn.getPhoneno()+"','"+cgbn.getOccupation()+"','"+cgbn.getDocumentid()+"', " ;
		insertcustinfo += " '"+cgbn.getReqdocuement()+"','"+cgbn.getPaddress1()+"','"+cgbn.getPaddress2()+"','"+cgbn.getPaddress3()+"','"+cgbn.getPaddress4()+"'";
		insertcustinfo += ",'"+cgbn.getResaddress1()+"', '"+cgbn.getResaddress2()+"', '"+cgbn.getResaddress3()+"', '"+cgbn.getResaddress4()+"', '1', '"+cgbn.getKycuser()+"', '"+cgbn.getPhotourl()+"', ";
		insertcustinfo += " '"+cgbn.getSignatureurl()+"', '"+cgbn.getIdproofurl()+"' )";
		enctrace( "insertcustinfo : " + insertcustinfo );
		x = jdbctemplate.update(insertcustinfo);
		return x;
	}
	
	
	public int insertCustToProduction( String instid, String custid, HttpSession session, JdbcTemplate jdbctemplate ) throws Exception{ 
		String custqry = ""; 
		custqry += " insert into IFP_CUSTINFO_PRODUCTION select * from IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
		enctrace( " custqry : " + custqry);
		int y  = jdbctemplate.update(custqry);  
		return y; 
	}
	
	
	public int moveCustProcessToProduction( String instid, String cardno, String custid, JdbcTemplate jdbctemplate,   HttpSession session ) throws Exception { 
		String chk_kyc_qry = "SELECT KYC_FLAG FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
		enctrace( "chk_kyc_qry__" + chk_kyc_qry);
		String kyc = (String)jdbctemplate.queryForObject(chk_kyc_qry, String.class);
		
		enctrace( "kyc value is__"+kyc);
		if( kyc.equals("0")){
			int newcustmove = this.insertCustToProduction( instid, custid, session, jdbctemplate );
			if( newcustmove != 1 ){
				return -1;
			}
		}else{
			int kyc_custmove = this.updateCustProduction( instid, custid, jdbctemplate );
			if( kyc_custmove > 0 ){
				return -1;
			}
		} 
		return 1; 
	}
	
	
	public int updateCustProduction( String instid, String custid, JdbcTemplate jdbctemplate  ) throws Exception { 
		
		String fchcustdata = "SELECT * FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"' ";
		List custdata = jdbctemplate.queryForList(fchcustdata);
		if( custdata.isEmpty() ){ 
			return -1;
		}
		else{
			
			Iterator itr = custdata.iterator();
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				String firstname =(String) mp.get("FNAME");
				String midname =(String) mp.get("MNAME");
				String lastname =(String) mp.get("LNAME");
				String fahtername =(String) mp.get("FATHER_NAME");;
				String mothername =(String) mp.get("MOTHER_NAME");
				String gender =(String) mp.get("GENDER");
				String mstatus =(String) mp.get("MARITAL_STATUS");
				String spname =(String) mp.get("SPOUSE_NAME");
				
				String nationality =(String) mp.get("NATIONALITY");
				
				Date dob =(Date) mp.get("DOB");
				String mobileno =(String) mp.get("MOBILE_NO");
				String phoneno =(String) mp.get("PHONE_NO");
				String email =(String) mp.get("EMAIL_ADDRESS");
				
				String occupation =(String) mp.get("OCCUPATION");
				
				String reqdocuement =(String) mp.get("ID_DOCUMENT");
				String documentid =(String) mp.get("ID_NUMBER");
				
				String makerid  =(String) mp.get("MAKER_DATE");
				String makerdate  =(String) mp.get("MAKER_DATE");
				
				String checkerid  =(String) mp.get("CHECKER_ID");
				String checkderdate  =(String) mp.get("CHECKER_DATE");
				
				String mkckstatus =(String) mp.get("MKCK_STATUS");
				
				String custstatus = (String)mp.get("CUSTOMER_STATUS");
				
				String paddress1 =(String) mp.get("POST_ADDR1");
				String paddress2 =(String) mp.get("POST_ADDR2");
				String paddress3 =(String) mp.get("POST_ADDR3");
				String paddress4 =(String) mp.get("POST_ADDR4");
				
				String resaddress1 =(String) mp.get("RES_ADDR1");
				String resaddress2 =(String) mp.get("RES_ADDR2");
				String resaddress3 =(String) mp.get("RES_ADDR3");
				String resaddress4 =(String) mp.get("RES_ADDR4");
				
				String kycflag = (String) mp.get("KYC_FLAG");
				
				String updqry = "UPDATE IFP_CUSTINFO_PRODUCTION SET ";
				updqry += "FNAME='"+firstname+"',MNAME='"+midname+"',LNAME='"+lastname+"',FATHER_NAME='"+fahtername+"',MOTHER_NAME='"+mothername+"',MARITAL_STATUS='"+mstatus+"',";
				updqry += "SPOUSE_NAME='"+spname+"',GENDER='"+gender+"',DOB='"+dob+"',NATIONALITY='"+nationality+"',EMAIL_ADDRESS='"+email+"',";
				updqry += "MOBILE_NO='"+mobileno+"',PHONE_NO='"+phoneno+"',OCCUPATION='"+occupation+"',ID_NUMBER='"+documentid+"' ,ID_DOCUMENT='"+reqdocuement+"',";
				updqry += "MAKER_DATE='"+makerdate+"',MAKER_ID='"+makerid+"',CHECKER_DATE='"+checkderdate+"',CHECKER_ID='"+checkerid+"',MKCK_STATUS='"+mkckstatus+"',";
				updqry += "CUSTOMER_STATUS='"+custstatus+"',POST_ADDR1='"+paddress1+"',POST_ADDR2='"+paddress2+"',POST_ADDR3='"+paddress3+"',POST_ADDR4='"+paddress4+"',";
				updqry += "RES_ADDR1='"+resaddress1+"',RES_ADDR2='"+resaddress2+"',RES_ADDR3='"+resaddress3+"',RES_ADDR4='"+resaddress4+"',KYC_FLAG='"+kycflag+"' ";
				updqry += "WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
				
				enctrace("updqry__"+updqry); 
				int upd_cnt = jdbctemplate.update(updqry);
				if( upd_cnt != 1 ){
					return -1;
				}
			}
		}
		 
		return 1;  
	}
	
	public int updateCustomerProduction(String instid, String customerid, InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate ) throws Exception { 
		int x = -1;
		String uploaduupdqry = "";
		
		System.out.println("uploaduupdqry : " + cgbn.getPhotourl() );
		if( cgbn.getPhotourl() != null  &&  !cgbn.getPhotourl().equals("")){
			uploaduupdqry += " , PHOTO_URL='"+cgbn.getPhotourl()+"' ";
		}
		
		if( cgbn.getSignatureurl() != null  &&  !cgbn.getSignatureurl().equals("") ){
			uploaduupdqry += " , SIGNATURE_URL='"+cgbn.getSignatureurl()+"' ";
		}
		
		if(cgbn.getIdproofurl() != null  &&  !cgbn.getIdproofurl().equals("")  ){
			uploaduupdqry += " , IDPROOF_URL='"+cgbn.getIdproofurl()+"' ";
		}
		System.out.println("uploaduupdqry : " + uploaduupdqry  );
		String updatecustinfo = "UPDATE IFP_CUSTINFO_PRODUCTION SET  ";
		updatecustinfo += "FNAME='"+cgbn.getFirstname()+"', MNAME='"+cgbn.getMidname()+"', LNAME='"+cgbn.getLastname()+"', FATHER_NAME='"+cgbn.getFahtername()+"', MOTHER_NAME='"+cgbn.getMothername()+"', MARITAL_STATUS='"+cgbn.getMstatus()+"', " ;
		updatecustinfo += " SPOUSE_NAME='"+cgbn.getSpname()+"', GENDER='"+cgbn.getGender()+"', DOB=to_date('"+cgbn.getDob()+"','dd-mm-yyyy'),NATIONALITY='"+cgbn.getNationality()+"', EMAIL_ADDRESS='"+cgbn.getEmail()+"',";
		updatecustinfo += " MOBILE_NO='"+cgbn.getPhoneno()+"', PHONE_NO='"+cgbn.getPhoneno()+"', OCCUPATION='"+cgbn.getOccupation()+"', ID_NUMBER='"+cgbn.getDocumentid()+"', ID_DOCUMENT='"+cgbn.getReqdocuement()+"', " ;
		updatecustinfo += " POST_ADDR1='"+cgbn.getPaddress1()+"', POST_ADDR2='"+cgbn.getPaddress2()+"', POST_ADDR3='"+cgbn.getPaddress3()+"', POST_ADDR4='"+cgbn.getPaddress4()+"',";
		updatecustinfo += "RES_ADDR1='"+cgbn.getResaddress1()+"', RES_ADDR2='"+cgbn.getResaddress2()+"', RES_ADDR3='"+cgbn.getResaddress3()+"', RES_ADDR4='"+cgbn.getResaddress4()+"', CUSTOMER_STATUS='1', KYC_FLAG='"+cgbn.getKycuser()+"' " + uploaduupdqry ;
		updatecustinfo += " WHERE INST_ID='"+instid+"' AND CIN='"+customerid+"'" ;  
		enctrace( "updatecustinfo : " + updatecustinfo );
		x  = jdbctemplate.update(updatecustinfo);
		
		return x;
		
	}
	
	public int updateApplicationData1(String instid, String cardno, String appno, String appdate, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String updateapplicationno = "UPDATE CARD_PRODUCTION SET APP_NO='"+appno+"', APP_DATE=to_date('"+appdate+"','dd-mm-yyyy') WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("updateapplicationno : "  + updateapplicationno );
		x = jdbctemplate.update(updateapplicationno);		 
		return x;		
	}
	
	public synchronized int updateCustomerIdCount(String instid, JdbcTemplate jdbctemplate) throws Exception 	{
		int x = -1;
		String updqry =  "update IFP_SEQUENCE_MASTER set CIN_NO=CIN_NO+1 where INST_ID='"+instid+"'";
		enctrace( "Customer upd qry : " + updqry);
		x = jdbctemplate.update(updqry);
		return x;
	}
	
	public int updateCardProcessStatus(String instid, String cardno, InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate) throws Exception 	{
		int x = -1;
		String updprocessqry = "UPDATE INST_CARD_PROCESS  SET CARD_STATUS='05', CIN='"+cgbn.getCustid()+"', MAKER_ID='"+cgbn.getMakerid()+"', CHECKER_ID='"+cgbn.getCheckerid()+"', MAKER_DATE="+cgbn.getMakerdate()+", CHECKER_DATE="+cgbn.getCheckerdate()+", MKCK_STATUS='"+cgbn.getMkckflag()+"', " ;
		updprocessqry += " REG_DATE=sysdate, APP_NO='"+cgbn.getAppno()+"', STATUS_CODE='01'  where INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' ";
		enctrace( "updprocessqry : "+updprocessqry); 
		x = jdbctemplate.update(updprocessqry);
		return x;
	}
	
	public int updateAuthStatus(String instid, String cardno, String nextstatus,  JdbcTemplate jdbctemplate) throws Exception 	{
		int x = -1;
		String updauthcustqry = "UPDATE INST_CARD_PROCESS SET MKCK_STATUS='P' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' and CARD_STATUS='"+nextstatus+"'";
		enctrace( "updauthcustqry : "+updauthcustqry); 
		x = jdbctemplate.update(updauthcustqry);
		return x;
	}
	
	public List otpcardslist (String instid,String chn,JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null; 
		String tablename = "";
		
		
		//String cardlistqry = "SELECT ORDER_REF_NO,MCARD_NO, HCARD_NO,CARD_NO,ACCOUNT_NO,EMB_NAME,CIN,ORG_CHN,BIN,MOBILENO FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"'  AND ORG_CHN='"+chn+"' AND CARD_STATUS='06' AND CAF_REC_STATUS NOT IN('A','BN','BR','S') AND MKCK_STATUS='P' ";
		String cardlistqry = "SELECT ORDER_REF_NO,MCARD_NO, ACCOUNT_NO,EMB_NAME,CIN,ORG_CHN,BIN,MOBILENO FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"'  AND ORG_CHN='"+chn+"' AND CARD_STATUS='06' AND CAF_REC_STATUS NOT IN('A','BN','BR','S') AND MKCK_STATUS='P' ";
		enctrace("otpcardslist " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	
	public List otpcardshcardnolist (String instid,String Encchn,String chn,JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null;
		String tablename = "";
		
		
	//	String cardlistqry = "SELECT A.ORDER_REF_NO,A.MCARD_NO,A.HCARD_NO,A.CARD_NO,A.ACCOUNT_NO,A.EMB_NAME,A.CIN,A.ORG_CHN,A.BIN,A.MOBILENO FROM CARD_PRODUCTION A,EZCARDINFO B WHERE A.INST_ID=B.INSTID AND A.INST_ID='"+instid+"' AND A.HCARD_NO=B.CHN AND B.CHN='"+chn+"' AND A.ORG_CHN='"+Encchn+"' AND B.STATUS='16' ";
		String cardlistqry = "SELECT A.ORDER_REF_NO,A.MCARD_NO,C.HCARD_NO,A.ORG_CHN,A.ACCOUNT_NO,A.EMB_NAME,A.CIN,A.ORG_CHN,A.BIN,A.MOBILENO FROM CARD_PRODUCTION A,EZCARDINFO B, CARD_PRODUCTION_HASH C WHERE A.INST_ID=B.INSTID AND A.INST_ID='"+instid+"' AND    a.cin=c.cin  AND C.HCARD_NO=B.CHN AND B.CHN='"+chn+"' AND A.ORG_CHN='"+Encchn+"' AND B.STATUS='16' ";
		enctrace("otpcardshcardnolist " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	public List otpforrepincardslist (String instid,JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null; 
		String tablename = "";
		
		/*String cardlistqry = "SELECT ORDER_REF_NO,MCARD_NO,HCARD_NO,CARD_NO,ACCT_NO AS ACCOUNT_NO,EMB_NAME,CIN,ORG_CHN,BIN,MOBILENO FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"'  AND CARD_STATUS='06' AND CAF_REC_STATUS NOT IN('A','BN','BR','S') AND MKCK_STATUS='P' ";*/
		String cardlistqry = "SELECT  A.ORDER_REF_NO,MCARD_NO,HCARD_NO,A.ACCOUNT_NO AS ACCOUNT_NO,EMB_NAME,A.CIN,ORG_CHN,A.BIN,MOBILENO FROM CARD_PRODUCTION A,CARD_PRODUCTION_HASH B, EZCARDINFO C WHERE A.INST_ID='"+instid+"'  AND A.CIN=B.CIN AND  B.HCARD_NO=C.CHN AND CARD_STATUS='06' AND C.STATUS='61'  AND CAF_REC_STATUS NOT IN('A','BN','BR','S') AND MKCK_STATUS='P' ";
		enctrace("otpforrepincardslist " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	public List otpcardsfornotactivehcardnolist (String instid,JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null; 
		String tablename = "";
		
		/*String cardlistqry = "SELECT A.ORDER_REF_NO,A.MCARD_NO,A.HCARD_NO,A.CARD_NO,A.ACCOUNT_NO,A.EMB_NAME,A.CIN,A.ORG_CHN,A.BIN,A.MOBILENO FROM CARD_PRODUCTION A,EZCARDINFO B WHERE A.INST_ID=B.INSTID AND A.INST_ID='"+instid+"' AND A.HCARD_NO=B.CHN  AND B.STATUS='16' ";*/
		//SELECT A.ORDER_REF_NO,A.MCARD_NO,B.CHN AS HCARD_NO, A.ACCOUNT_NO,A.EMB_NAME,A.CIN,A.ORG_CHN,A.BIN,A.MOBILENO FROM CARD_PRODUCTION A,EZCARDINFO B WHERE A.INST_ID=B.INSTID AND A.INST_ID='SIB' AND  A.CIN=B.CUSTID  AND A.STATUS_CODE=B.STATUS AND A.CARD_STATUS='09' AND B.STATUS='16' AND A.CAF_REC_STATUS='R';
		String cardlistqry = "SELECT A.ORDER_REF_NO, A.MCARD_NO, B.CHN AS HCARD_NO, A.ACCOUNT_NO, A.EMB_NAME, A.CIN, "
				+ " A.ORG_CHN, A.BIN,A.MOBILENO FROM CARD_PRODUCTION A,EZCARDINFO B WHERE A.INST_ID=B.INSTID AND "
				+ " A.INST_ID='"+instid+"' AND A.CIN=B.CUSTID AND A.STATUS_CODE=B.STATUS AND A.CARD_STATUS='09'"
				+ " AND B.STATUS='16' AND A.CAF_REC_STATUS='A'";
		enctrace("otpcardsfornotactivehcardnolist " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	public String getCustomerIdByCard(String instid, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		String customerid = null;
		try {
			String procescardqry = "SELECT CIN FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
			enctrace("procescardqry : " + procescardqry );
			customerid = (String)jdbctemplate.queryForObject(procescardqry, String.class);
		} catch (EmptyResultDataAccessException e) {
			
		}
		return customerid;
	}

	
	
	public int updateinstcardprocess (String instid,String usercode,InstCardRegisterProcessBeans cgbn,JdbcTemplate jdbctemplate ) throws Exception{
		
		int x= -1;
		
		
		try{
			StringBuilder strbuild = new StringBuilder();

			strbuild.append("UPDATE INST_CARD_PROCESS SET CARD_STATUS='06', ENC_NAME='"+cgbn.getCustname()+"',EMB_NAME='"+cgbn.getCustname()+"',CIN='"+cgbn.getCustid()+"', USED_CHN='"+cgbn.getOrg_chn()+"',CAF_REC_STATUS='NR',reg_date=sysdate,MKCK_STATUS='M',ISSUE_DATE=sysdate,maker_id='"+usercode+"',ACCT_NO='"+cgbn.getAccountno()+"',MOBILENO='"+cgbn.getPhoneno()+"' WHERE INST_ID='"+instid.trim()+"' and HCARD_NO='"+cgbn.getHascard().trim()+"'");
			enctrace( "updateinstcardprocess : " + strbuild.toString() );
			x = jdbctemplate.update(strbuild.toString());
		}
		catch (Exception e){
			
			trace("getting execptipn occured " +e.getMessage());
		}
		return x;	
			
		}
public int updatefornewcardreg (String instid, String usercode, InstCardRegisterProcessBeans cardregbean,
		String encryptedcard, JdbcTemplate jdbctemplate)throws Exception{
	
		int x= -1;
		
		
		try{
			StringBuilder strbuild = new StringBuilder();

			strbuild.append("UPDATE INST_CARD_PROCESS SET CARD_STATUS='06', ENC_NAME='"+cardregbean.getCustname()+"',"
			+ "EMB_NAME='"+cardregbean.getCustname()+"',CIN='"+cardregbean.getCustidno()+"',reg_date=sysdate,MKCK_STATUS='M',"
			+ "ISSUE_DATE=sysdate,maker_id='"+usercode+"',ACCT_NO='"+cardregbean.getAccountno()+"',MOBILENO='"+cardregbean.getPhoneno()+"',"
			+ "ACCTTYPE_ID=(select ACCTTYPEID from ACCTTYPE where ACCTTYPEID='"+cardregbean.getAccounttype()+"'),"
			+ "ACCTSUB_TYPE_ID=(select ACCTSUBTYPEID from ACCTSUBTYPE where ACCTSUBTYPEDESC='"+cardregbean.getAcctsubtype()+"'),"
			+ "ACC_CCY=(SELECT NUMERIC_CODE FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE='"+cardregbean.getCurrency1()+"') WHERE INST_ID='"+instid.trim()+"' and ORG_CHN='"+encryptedcard+"'");
			enctrace( "updatefornewcardreg : " + strbuild.toString() );
			x = jdbctemplate.update(strbuild.toString());
		}
		catch (Exception e){
			
			trace("getting execptipn updatefornewcardreg " +e.getMessage());
		}
		return x;	
			
		}
	
	
	public int insertinsacctdetails (String instid,InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate ) throws Exception{
		StringBuilder strbuild = new StringBuilder();
		int x= -1;
		
		
		/*strbuild.append("insert into ACCOUNTINFO(INST_ID,ORDER_REF_NO,CIN,ACCTTYPE_ID,ACCTSUB_TYPE_ID");
		strbuild.append(",ACCT_CURRENCY,ACCOUNTNO,ACCOUNTTYPE,AVAILBAL,LEDGERBAL,LIMITFLAG,STATUS,TXNGROUPID,LASTTXNDATE");
		strbuild.append(",LASTTXNTIME,BRANCHCODE,PRODUCTCODE,ADDEDBY,ADDED_DATE,AUTHBY,AUTHDATE,AUTH_CODE)");
		strbuild.append(" values ");
		strbuild.append("('"+instid+"','"+cgbn.getOrder_ref_no()+"','"+cgbn.getCustidno()+"','"+cgbn.getAccttype()+"','',");
		strbuild.append("'"+cgbn.getCard_ccy()+"','"+cgbn.getAccountno()+"','"+cgbn.lmt_based_on+"','0','0','','','','',");
		strbuild.append("'','"+cgbn.getBranch_code()+"','"+cgbn.getProd_code()+"','"+cgbn.getUsername()+"',");
		strbuild.append("sysdate,'','','')");*/
		
		strbuild.append("insert into ACCOUNTINFO(INST_ID,ORDER_REF_NO,CIN,ACCTTYPE_ID,ACCTSUB_TYPE_ID");
		strbuild.append(",ACCT_CURRENCY,ACCOUNTNO,ACCOUNTTYPE,AVAILBAL,LEDGERBAL,LIMITFLAG,STATUS,TXNGROUPID,LASTTXNDATE");
		strbuild.append(",LASTTXNTIME,BRANCHCODE,PRODUCTCODE,ADDEDBY,ADDED_DATE,AUTHBY,AUTHDATE,AUTH_CODE)");
		strbuild.append(" values ");
		strbuild.append("('"+instid+"','"+cgbn.getOrder_ref_no()+"','"+cgbn.getCustidno()+"',(select ACCTTYPEID from ACCTTYPE where ACCTTYPEID='"+cgbn.getAccounttype()+"'),(select ACCTSUBTYPEID from ACCTSUBTYPE where ACCTSUBTYPEDESC='"+cgbn.getAcctsubtype()+"') ,");
		strbuild.append("(SELECT NUMERIC_CODE FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE='"+cgbn.getCurrency1()+"'),'"+cgbn.getAccountno()+"','"+cgbn.lmt_based_on+"','0','0','1','50','','',");
		strbuild.append("'','"+cgbn.getBranchcode()+"','"+cgbn.getProd_code()+"','"+cgbn.getUsercode()+"',");
		strbuild.append("sysdate,'','','')");
		
		
		enctrace( "insertinsacctdetails : " + strbuild.toString() );
		
		 String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"+cgbn.getAccountno()+"' and cin='"+cgbn.getCustidno()+"'";
		 enctrace("acctexistqry-->"+acctexistqry);
		 String acctexist = (String)jdbctemplate.queryForObject(acctexistqry, String.class);
		try{
		 if( !"0".equalsIgnoreCase(acctexist)){
			
			x=1; 
		 }
		 else{
			 
			 x = jdbctemplate.update(strbuild.toString());
			 
			 
		 }	}
		catch(Exception e){
			trace("insatant resissuance account insert"+e.getMessage());
			
		}
		 trace("ACCOUNTINFO CUSTMEOR MAPPING"+x);
		return x;	
			
		}
	
	public int insertinscustdetails (String instid,InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate ) throws Exception{
		StringBuilder strbuild = new StringBuilder();
		int x= -1;
		
		/*strbuild.append("insert into CUSTOMERINFO(INST_ID,ORDER_REF_NO,CIN,FNAME,MNAME,LNAME,DOB,GENDER,MARITAL_STATUS");
		strbuild.append(",NATIONALITY,DOCUMENT_PROVIDED,DOCUMENT_NUMBER,SPOUCE_NAME,MOTHER_NAME,FATHER_NAME,MOBILE");
		strbuild.append(",E_MAIL,P_PO_BOX,P_HOUSE_NO,P_STREET_NAME,P_WARD_NAME,P_CITY,P_DISTRICT,P_PHONE1,P_PHONE2");
		strbuild.append(",C_PO_BOX,C_HOUSE_NO,C_STREET_NAME,C_WARD_NAME,C_CITY,C_DISTRICT,C_PHONE1,C_PHONE2");
		strbuild.append(",MAKER_DATE,MAKER_ID,CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS)");
		strbuild.append(" values ");
		strbuild.append("('"+instid+"','"+cgbn.getOrder_ref_no()+"','"+cgbn.getCustidno()+"','"+cgbn.getCustname()+"','"+cgbn.getMothername()+"','','"+cgbn.getDob()+"','"+cgbn.getGender()+"','',");
		strbuild.append("'','','','','"+cgbn.getMothername()+"','','"+cgbn.getPhoneno()+"',");
		strbuild.append("'"+cgbn.getEmail()+"','"+cgbn.getPaddress1()+"','"+cgbn.getPaddress2()+"','"+cgbn.getPaddress3()+"','"+cgbn.getPaddress4()+"','"+cgbn.getCity()+"','','','',");
		strbuild.append("'','','','','','','','',");
		strbuild.append("sysdate,'"+cgbn.getUsercode()+"','','','M','M')");*/
		
		strbuild.append("insert into CUSTOMERINFO(INST_ID,ORDER_REF_NO,CIN,FNAME,MNAME,LNAME,DOB,GENDER,MARITAL_STATUS");
		strbuild.append(",NATIONALITY,DOCUMENT_PROVIDED,DOCUMENT_NUMBER,SPOUCE_NAME,MOTHER_NAME,FATHER_NAME,MOBILE");
		strbuild.append(",E_MAIL,P_PO_BOX,P_HOUSE_NO,P_STREET_NAME,P_WARD_NAME,P_CITY,P_DISTRICT,P_PHONE1,P_PHONE2");
		strbuild.append(",C_PO_BOX,C_HOUSE_NO,C_STREET_NAME,C_WARD_NAME,C_CITY,C_DISTRICT,C_PHONE1,C_PHONE2");	
		strbuild.append(",MAKER_DATE,MAKER_ID,CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS)");
		strbuild.append(" values ");
		strbuild.append("('"+instid+"','"+cgbn.getOrder_ref_no()+"','"+cgbn.getCustidno()+"','"+cgbn.getFirstname()+"','"+cgbn.getMidname()+"','"+cgbn.getLastname()+"','"+cgbn.getDob()+"','"+cgbn.getGender()+"','"+cgbn.getMaritaldesc()+"',");
		strbuild.append("'"+cgbn.getNationality()+"','"+cgbn.getDocumentdesc()+"','"+cgbn.getDocumentid()+"','"+cgbn.getSpname()+"','"+cgbn.getMothername()+"','"+cgbn.getFahtername()+"','"+cgbn.getPhoneno()+"',");
		strbuild.append("'"+cgbn.getEmail()+"','"+cgbn.getP_poxbox()+"','"+cgbn.getP_houseno()+"','"+cgbn.getP_streetname()+"','"+cgbn.getP_wardnumber()+"','"+cgbn.getCity()+"','"+cgbn.getP_district()+"','"+cgbn.getP_phone1()+"','"+cgbn.getP_phone2()+"',");
		strbuild.append("'','','','','','','','',");
		strbuild.append("sysdate,'"+cgbn.getUsercode()+"','','','M','M')");
		
		
		
		enctrace( "insertcustdetails : " + strbuild.toString() );
		 String acctexistqry = "SELECT COUNT(1) AS CNT FROM CUSTOMERINFO WHERE CIN='"+cgbn.getCustidno()+"'";
		 enctrace("customerexistqry-->"+acctexistqry);
		 String acctexist = (String)jdbctemplate.queryForObject(acctexistqry, String.class);
		 if( !"0".equalsIgnoreCase(acctexist)){
				x=1; 
		 }
		 else{
			 try{
			 x = jdbctemplate.update(strbuild.toString());
			 }
			  catch(Exception e){
				  e.printStackTrace();
			  }
			 
		 }	
		 trace("CUSTOMERINFO CUSTMEOR MAPPING"+x);
		return x;	
			
			
		}
	
	public int insertcardaccountlink (String instid,InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate ) throws Exception{
		StringBuilder strbuild = new StringBuilder();
		int x= -1;
		strbuild.append("insert into IFD_CARD_ACCT_LINK(INST_ID,CARD_NO,CIN,ACCT_NO,MAKER_DATE,ACCT_CCY");
		
		strbuild.append(",ACCT_STATUS,ACCT_OLD_STATUS,MAKER_ID,ACCT_PRIORITY)");
		strbuild.append(" values ");
		strbuild.append("('"+instid+"','"+cgbn.getEncrptchn()+"','"+cgbn.getCustidno()+"','"+cgbn.getAccountno()+"',sysdate,'00','','','"+cgbn.getUsercode()+"','')");
		
		enctrace( "insertcustdetails : " + strbuild.toString() );
		x = jdbctemplate.update(strbuild.toString());
		return x;	
			
		}

	
	public int updateordernofornewcard(String instid,String rderrefno,String oldrefno,	JdbcTemplate jdbctemplate) throws Exception {  
		int x = -1;
		String updaterefno = "UPDATE CUSTOMERINFO SET ORDER_REF_NO='"+rderrefno+"' WHERE INST_ID='"+instid+"' and ORDER_REF_NO='"+oldrefno+"' ";
		enctrace( "updacctlinkqry : "+updaterefno); 
		x = jdbctemplate.update(updaterefno);
		
		if(x >0){
			String updaterefnoacct = "UPDATE ACCOUNTINFO SET ORDER_REF_NO='"+rderrefno+"' WHERE INST_ID='"+instid+"' and ORDER_REF_NO='"+oldrefno+"' ";
			enctrace( "updaterefnoacct : "+updaterefnoacct); 
			x = jdbctemplate.update(updaterefnoacct);
		}
		return x;
	}
	public int updateordeezauthrelfornewcard(String instid,String newhcardno,String oldhacrdno,	JdbcTemplate jdbctemplate) throws Exception {  
		int x = -1;
		//String updaterefno = "UPDATE EZAUTHREL SET CHN='"+newhcardno+"' WHERE INSTID='"+instid+"' and CHN='"+oldhacrdno+"' ";
		

		String updaterefno ="INSERT INTO  EZAUTHREL (INSTID,CHN,ACCOUNTNO,ACCOUNTTYPE,ACCOUNTFLAG,ACCOUNTPRIORITY,CURRCODE,ACCTSUBTYPE,CHNFLAG) SELECT INSTID,'"+newhcardno+"',ACCOUNTNO,ACCOUNTTYPE,ACCOUNTFLAG,ACCOUNTPRIORITY,CURRCODE,ACCTSUBTYPE,CHNFLAG FROM EZAUTHREL WHERE INSTID='"+instid+"' AND CHN='"+oldhacrdno+"'  ";

		enctrace( "insertreissucardno : "+updaterefno); 
		x = jdbctemplate.update(updaterefno);
		
		return x;
	}
	/*public int updateordeezauthrelfornewcard(String instid,String newhcardno,String oldhacrdno,	JdbcTemplate jdbctemplate) throws Exception {  
		int x = -1;
		String updaterefno = "UPDATE EZAUTHREL SET CHN='"+newhcardno+"' WHERE INSTID='"+instid+"' and CHN='"+oldhacrdno+"' ";
		enctrace( "updacctlinkqry : "+updaterefno); 
		x = jdbctemplate.update(updaterefno);
		
		return x;
	}*/
	public int updateordeezcardinfofornewcard(String instid,String oldhacrdno,	JdbcTemplate jdbctemplate) throws Exception {  
		int x = -1;
		String updaterefno = "UPDATE EZCARDINFO SET STATUS='62' WHERE INSTID='"+instid+"' and CHN='"+oldhacrdno+"' ";
		enctrace( "updacctlinkqry : "+updaterefno); 
		x = jdbctemplate.update(updaterefno);
		
		return x;
	}
	
	
	public int insertproduction(String instid,InstCardRegisterProcessBeans cgbn, JdbcTemplate jdbctemplate ) throws Exception{
		int x=-1;
		try{
			StringBuilder strbuild=new StringBuilder();
			strbuild.append("INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO,MCARD_NO");
			strbuild.append(",CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY");
			strbuild.append(",PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE");
			strbuild.append(",REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS");
			strbuild.append(",SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,ECOM_CNT");
			strbuild.append(",EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET");
			strbuild.append(",CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,APPTYPE,REMARKS,CARDISSUETYPE,COURIERMASTER_ID,COURIER_DATE,SENDINGADDRESS)");
		
			strbuild.append("SELECT INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO,MCARD_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE");
			strbuild.append(",CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE");
			strbuild.append(",APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE");
			strbuild.append(",CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT");
			strbuild.append(",PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG");
			strbuild.append(",OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,APPTYPE,REMARKS,CARDISSUETYPE,COURIERMASTER_ID,COURIER_DATE");
			strbuild.append(",SENDINGADDRESS FROM INST_CARD_PROCESS WHERE inst_id='"+instid+"' and"); 
			strbuild.append(" HCARD_NO='"+cgbn.getHascard().trim()+"'");
			
			x = jdbctemplate.update(strbuild.toString());	
			enctrace("insertproduction : " + strbuild );
		}
		
		
		catch (Exception e){
			
			trace("getting execptipn occured " +e.getMessage());
		}
		return x;
	}
	
	public List getApplicationNumber(String instid, String cardno, String tablename, JdbcTemplate jdbctemplate)  throws Exception { 
		List customerid = null;
		try {
			String procescardqry = "SELECT APP_NO, TO_CHAR(APP_DATE,'dd-mm-yyyy') as APP_DATE  FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
			enctrace("procescardqry : " + procescardqry );
			customerid = jdbctemplate.queryForList(procescardqry);
		} catch (EmptyResultDataAccessException e) { }
		return customerid;
	}


	public int updateCardAccountLink(String instid, String cardno, String customerid,	JdbcTemplate jdbctemplate) throws Exception {  
		int x = -1;
		String updacctlinkqry = "UPDATE IFP_CARD_ACCT_LINK SET CIN='"+customerid+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace( "updacctlinkqry : "+updacctlinkqry); 
		x = jdbctemplate.update(updacctlinkqry);
		return x;
	}

	public int updateCardOriginalCustid(String instid, String cardno, String customerid,JdbcTemplate jdbctemplate)  throws Exception  {  
		int x = -1;
		String updcardcustid = "UPDATE CARD_PRODUCTION SET CIN='"+customerid+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace( "updacctlinkqry : "+updcardcustid); 
		x = jdbctemplate.update(updcardcustid);
		return x;
	}
	
}