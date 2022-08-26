package com.ifd.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifd.beans.CbsCustomerRegBeans;
import com.ifd.beans.CustomerRegisterationBeans;
import com.ifp.Action.BaseAction;

import test.Date;

public class CbsCustomerRegisterDAO extends BaseAction{

	
	
	
	public List getAcctTypeList( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		/*String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE AUTH_CODE='1' AND INST_ID='"+instid+"'";
		acctypelist = jdbctemplate.queryForList(acctypelistqry);*/
		
		
		//by gowtham-220819
				String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE AUTH_CODE=? AND INST_ID=?";
				acctypelist = jdbctemplate.queryForList(acctypelistqry,new Object[]{"1",instid});
		return acctypelist;
	}
	
	public List getAcctSubTypeList( String instid,String accountypeid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		String accsubtypelistqry = "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE AUTH_CODE = '1' AND INST_ID='"+instid+"' AND ACCTTYPEID='"+accountypeid+"'";
		enctrace("accsubtypelistqry:::"+accsubtypelistqry);
		acctypelist = jdbctemplate.queryForList(accsubtypelistqry);
		return acctypelist;
	}
		
	
	public int insertCardOrderPersonal(String instid, String orderrefno, String customerid, CbsCustomerRegBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		//int x = -1;
		trace("hello");
		String insertcardorderqry = "INSERT INTO PERS_CARD_ORDER(" +
				"INST_ID,ORDER_REF_NO ,CARD_TYPE_ID,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY,ACCOUNT_NO,SUB_PROD_ID, PRODUCT_CODE,LIMIT_ID ,FEE_CODE,CARD_QUANTITY," +
				"ORDER_STATUS, ORDER_TYPE,ORDERED_DATE,EMBOSSING_NAME,MKCK_STATUS,REMARKS,ENCODE_DATA,MAKER_DATE,MAKER_TIME,"+
				"MAKER_ID,CHECKER_DATE,CHECKER_ID,BRANCH_CODE,BIN,APP_DATE,"+  
				"CIN,APP_NO,KYC_FLAG,APPTYPE,CARDISSUETYPE,PARENTCARD, LIMIT_BASEDON , REG_LEVEL,RENEWALFLAG,CARD_COLLECT_BRANCH"+
				") VALUES ( '"+instid+"', '"+orderrefno+"','"+dbean.getCardtypeid()+"', "+
				"'"+dbean.getAccounttypevalue()+"','"+dbean.getAccountsubtypevalue()+"','"+dbean.getTab2_currency()+"','"+dbean.getAccountnovalue()+"', "+
				"'"+dbean.getSubproduct()+"','"+dbean.getProductcode()+"','"+dbean.getLimitid()+"','"+dbean.getFeecode()+"','1', " +
				"'01','P',SYSDATE,UPPER('"+dbean.getEmbname()+"'),'M','--',UPPER('"+dbean.getEncname()+"'),SYSDATE,'000000'," +
				"'"+dbean.getUsercode()+"',SYSDATE,'"+dbean.getUsercode()+"','"+dbean.getBranchcode()+"','"+dbean.getBin()+"',SYSDATE " +
				",'"+customerid+"','000','0','$DEBIT','P','0000','"+dbean.getLimitbasedon()+"','4','"+dbean.getRenewalflag()+"','"+dbean.getCollectbranch()+"')";
		enctrace("insertcardorderqry :" + insertcardorderqry );
		x = jdbctemplate.update(insertcardorderqry);    
		/*return x;  */ 
		
		/*//BY GOWTHAM-220819
				int x = -1;
				Date date =  new Date();
				String insertcardorderqry = "INSERT INTO PERS_CARD_ORDER(" +
				"INST_ID,          ORDER_REF_NO ,     CARD_TYPE_ID,      ACCTTYPE_ID,  ACCTSUB_TYPE_ID,  "
				+ "ACC_CCY,        ACCOUNT_NO,       SUB_PROD_ID,        PRODUCT_CODE,  LIMIT_ID ,"
				+ "FEE_CODE,        CARD_QUANTITY,"  +"ORDER_STATUS,     ORDER_TYPE,    ORDERED_DATE,"
				+ "EMBOSSING_NAME,  MKCK_STATUS,       REMARKS,          ENCODE_DATA,   MAKER_DATE,"
				+ "MAKER_TIME,"+   "MAKER_ID         ,CHECKER_DATE,      CHECKER_ID,     BRANCH_CODE,"
				+ "BIN,APP_DATE,"+  "CIN,             APP_NO,            KYC_FLAG,       APPTYPE,"
				+ "CARDISSUETYPE    ,PARENTCARD,      LIMIT_BASEDON ,    REG_LEVEL,      RENEWALFLAG,"
				+ "CARD_COLLECT_BRANCH"+")   "
				
			//43	
				
				+ " VALUES ( ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,? , UPPER(?),?,?,UPPER(?),?,	?,?,?,?,?,"
				+ "  ?,?,?,?,?,  ?,?,?,?,?,  ?,?)";	
				enctrace("insertcardorderqry :" + insertcardorderqry );
				x = jdbctemplate.update(insertcardorderqry,new Object[]{
						
						
						instid, 							orderrefno,							dbean.getCardtypeid(),				dbean.getAccounttypevalue(),			dbean.getAccountsubtypevalue(),
						dbean.getTab2_currency(),			dbean.getAccountnovalue(), 			dbean.getSubproduct(),		 		dbean.getProductcode(),					dbean.getLimitid(),
						dbean.getFeecode(),					"1",								"01",								"P",									date.getCurrentDate(),
						dbean.getEmbname(),					"M",								"--",								dbean.getEncname(),						date.getCurrentDate(),								
						"000000",							dbean.getUsercode(),				date.getCurrentDate(),				dbean.getUsercode(),					dbean.getBranchcode(),
						dbean.getBin(),						date.getCurrentDate(),				customerid	,						"000",									"0",
						"$DEBIT",							"P",								"0000",								dbean.getLimitbasedon(),				"4",								
						dbean.getRenewalflag(),				dbean.getCollectbranch()});    
		String insertcardorderqry = "INSERT INTO PERS_CARD_ORDER(" +
				"INST_ID,ORDER_REF_NO ,CARD_TYPE_ID,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY,ACCOUNT_NO,SUB_PROD_ID, PRODUCT_CODE,LIMIT_ID ,FEE_CODE,CARD_QUANTITY," +
				"ORDER_STATUS, ORDER_TYPE,ORDERED_DATE,EMBOSSING_NAME,MKCK_STATUS,REMARKS,ENCODE_DATA,MAKER_DATE,MAKER_TIME,"+
				"MAKER_ID,CHECKER_DATE,CHECKER_ID,BRANCH_CODE,BIN,APP_DATE,"+  
				"CIN,APP_NO,KYC_FLAG,APPTYPE,CARDISSUETYPE,PARENTCARD, LIMIT_BASEDON , REG_LEVEL,RENEWALFLAG,CARD_COLLECT_BRANCH"+
				") VALUES ( ?,?,?, ?,?,?,?,?,?,?,?,?,?,?,SYSDATE,UPPER(?),?,?,UPPER(?), SYSDATE,?," +"?,SYSDATE,?,?,? ,SYSDATE " +",?,?,?,?,?,?,?,?,?,?)";
				enctrace("insertcardorderqry :" + insertcardorderqry );
		
		
x = jdbctemplate.update(insertcardorderqry,new Object[]{
instid,orderrefno,dbean.getCardtypeid(), 
dbean.getAccounttypevalue(),dbean.getAccountsubtypevalue(),dbean.getTab2_currency(),
dbean.getAccountnovalue(),dbean.getSubproduct(),dbean.getProductcode(),
dbean.getLimitid(),dbean.getFeecode(),"1",
"01","P",dbean.getEmbname(),"M","--",
dbean.getEncname(),"000000",
dbean.getUsercode(),dbean.getUsercode(),
dbean.getBranchcode(),dbean.getBin(),
customerid,"000","0",
"$DEBIT","P","0000",
dbean.getLimitbasedon(),"4",dbean.getRenewalflag(),
dbean.getCollectbranch()});    */
		
		
								
								
				return x;   
	}
	      
	public int insertCustomerRegisterationLevel(String instid, String applicationid, String regvalue, CbsCustomerRegBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder insertcardorderqry = new StringBuilder();
		insertcardorderqry.append("");
		insertcardorderqry.append("INSERT INTO CUSTOMER_APP_REGLEVEL " );
		insertcardorderqry.append("( INST_ID, APPLICATIONID, REGLEVEL )" );
		insertcardorderqry.append(" VALUES " );
		
		/*insertcardorderqry.append("('"+instid+"', '"+applicationid+"','"+regvalue+"' )");		
		enctrace("insertCustomerRegisterationLevel :" + insertcardorderqry.toString() );
		x = jdbctemplate.update(insertcardorderqry.toString());*/
		
		//by gowtham-220819
				insertcardorderqry.append("(?,?,? )");		
				enctrace("insertCustomerRegisterationLevel :" + insertcardorderqry.toString() );
				x = jdbctemplate.update(insertcardorderqry.toString(),new Object[]{instid,applicationid,regvalue,});
		return x; 
	}
	
	
	public int updateCustomerRegisterationLevel(String instid, String applicationid, String regvalue, CbsCustomerRegBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder insertcardorderqry = new StringBuilder();
		insertcardorderqry.append("UPDATE CUSTOMER_APP_REGLEVEL SET ");
		insertcardorderqry.append("REGLEVEL = '"+regvalue+"'  ");
		insertcardorderqry.append("WHERE INST_ID = '"+instid+"' AND APPLICATIONID = '"+applicationid+"' ");
		enctrace("updateCustomerRegisterationLevel :" + insertcardorderqry.toString() );
		x = jdbctemplate.update(insertcardorderqry.toString());
		return x; 
	}
	 
	
	public int updateOrderStatus(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder updateOrderStatus = new StringBuilder();
		updateOrderStatus.append("UPDATE PERS_CARD_ORDER SET ");
		updateOrderStatus.append("auth_code = '1',auth_date=sysdate  ");
		updateOrderStatus.append("WHERE INST_ID = '"+instid+"' AND APPLICATIONID = '"+applicationid+"' ");
		enctrace("updateOrderStatus :" + updateOrderStatus.toString() );
		x = jdbctemplate.update(updateOrderStatus.toString());
		return x; 
	}
	 
	
	public int updateCustomerContactPersionalDetails(String instid, String applicationid,String usercode, String regvalue, CbsCustomerRegBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		Date date =  new Date();
		StringBuilder updcustdetailquery = new StringBuilder(); 
		
		
		//by gowtham-220819
				//INST_ID, ORDER_REF_NO, CIN, 
						updcustdetailquery.append("UPDATE CUSTOMERINFO SET ");
					
						updcustdetailquery.append("FNAME = ?, ");
						updcustdetailquery.append("MNAME = ?, ");
						updcustdetailquery.append("LNAME  = ?, ");
						updcustdetailquery.append("DOB = TO_DATE(?,'DD-MM-YYYY'), ");
						updcustdetailquery.append("GENDER = ?, "); 
						
						updcustdetailquery.append("MARITAL_STATUS = ?,");  
						updcustdetailquery.append("NATIONALITY = ?, "); 
						updcustdetailquery.append("DOCUMENT_PROVIDED =?, "); 
						updcustdetailquery.append("DOCUMENT_NUMBER = ?, "); 
						updcustdetailquery.append("SPOUCE_NAME = ?, "); 
						
						
						updcustdetailquery.append("MOTHER_NAME = ?, "); 
						updcustdetailquery.append("FATHER_NAME = ? ,"); 
						updcustdetailquery.append("MOBILE = ?,"); 
						updcustdetailquery.append("E_MAIL = ?,"); 
						updcustdetailquery.append("P_PO_BOX =?, "); 
						
						updcustdetailquery.append("P_HOUSE_NO =? ,"); 
						updcustdetailquery.append("P_STREET_NAME =?,"); 
						updcustdetailquery.append("P_WARD_NAME = ?, "); 
						updcustdetailquery.append("P_CITY =?,"); 
						updcustdetailquery.append("P_DISTRICT = ?, "); 
						
						updcustdetailquery.append("P_PHONE1 =?, ");   
						updcustdetailquery.append("P_PHONE2 = ?, "); 
						updcustdetailquery.append("C_PO_BOX = ?, "); 
						updcustdetailquery.append("C_HOUSE_NO =?, "); 
						updcustdetailquery.append("C_STREET_NAME = ?, "); 
						
						updcustdetailquery.append("C_WARD_NAME =?, "); 
						updcustdetailquery.append("C_CITY =?, "); 
						updcustdetailquery.append("C_DISTRICT =?, "); 
						updcustdetailquery.append("C_PHONE1 = ?, "); 
						updcustdetailquery.append("C_PHONE2 = ?, "); 
						
						updcustdetailquery.append("MAKER_DATE =?,"); 
						updcustdetailquery.append("MAKER_ID = ?, "); 
						updcustdetailquery.append("CHECKER_DATE = ? ,"); 
						updcustdetailquery.append("CHECKER_ID =?,"); 
						updcustdetailquery.append("MKCK_STATUS =? ,"); 
						
						updcustdetailquery.append("CUSTOMER_STATUS =? ");
						updcustdetailquery.append("WHERE INST_ID = ? and ORDER_REF_NO = ? ");
						
						enctrace("updcustdetailquery :" + updcustdetailquery.toString() );
						x = jdbctemplate.update(updcustdetailquery.toString(),new Object[]{dbean.getFirstname(),dbean.getMiddlename(),dbean.getLastname(),dbean.getDob(),dbean.getGender(),dbean.getMstatus(),  dbean.getNationality(), dbean.getDocumentprovided(), dbean.getDocumentnumber(), dbean.getSpousename(), dbean.getMothername(),dbean.getFathername(),dbean.getMobile(), dbean.getEmail(), dbean.getP_poxbox(), dbean.getP_houseno(),dbean.getP_streetname(),dbean.getP_wardnumber(),dbean.getP_city(),dbean.getP_district(),dbean.getP_phone1(),  dbean.getP_phone2(),dbean.getC_poxbox(),dbean.getC_houseno(),dbean.getC_streetname(),dbean.getC_wardnumber(),dbean.getC_city(),dbean.getC_district(),dbean.getC_phone1(),dbean.getC_phone2(),date.getCurrentDate(), usercode ,""  ,""  ,"M" ,"0",instid,applicationid});
				
		
/*		//INST_ID, ORDER_REF_NO, CIN, 
		updcustdetailquery.append("UPDATE CUSTOMERINFO SET ");
		updcustdetailquery.append("FNAME = '"+dbean.getFirstname()+"', ");
		updcustdetailquery.append("MNAME = '"+dbean.getMiddlename()+"', ");
		updcustdetailquery.append("LNAME  = '"+dbean.getLastname()+"', ");
		updcustdetailquery.append("DOB = TO_DATE('"+dbean.getDob()+"','DD-MM-YYYY'), ");
		updcustdetailquery.append("GENDER = '"+dbean.getGender()+"', "); 
		updcustdetailquery.append("MARITAL_STATUS = '"+dbean.getMstatus()+"' ,");  
		updcustdetailquery.append("NATIONALITY = '"+dbean.getNationality()+"', "); 
		updcustdetailquery.append("DOCUMENT_PROVIDED = '"+dbean.getDocumentprovided()+"', "); 
		updcustdetailquery.append("DOCUMENT_NUMBER = '"+dbean.getDocumentnumber()+"', "); 
		updcustdetailquery.append("SPOUCE_NAME = '"+dbean.getSpousename()+"', "); 
		updcustdetailquery.append("MOTHER_NAME = '"+dbean.getMothername()+"', "); 
		updcustdetailquery.append("FATHER_NAME = '"+dbean.getFathername()+"' ,"); 
		updcustdetailquery.append("MOBILE = '"+dbean.getMobile()+"' ,"); 
		updcustdetailquery.append("E_MAIL = '"+dbean.getEmail()+"' ,"); 
		updcustdetailquery.append("P_PO_BOX = '"+dbean.getP_poxbox()+"', "); 
		updcustdetailquery.append("P_HOUSE_NO = '"+dbean.getP_houseno()+"' ,"); 
		updcustdetailquery.append("P_STREET_NAME = '"+dbean.getP_streetname()+"' ,"); 
		updcustdetailquery.append("P_WARD_NAME = '"+dbean.getP_wardnumber()+"', "); 
		updcustdetailquery.append("P_CITY = '"+dbean.getP_city()+"' ,"); 
		updcustdetailquery.append("P_DISTRICT = '"+dbean.getP_district()+"', "); 
		updcustdetailquery.append("P_PHONE1 = '"+dbean.getP_phone1()+"', ");   
		updcustdetailquery.append("P_PHONE2 = '"+dbean.getP_phone2()+"', "); 
		updcustdetailquery.append("C_PO_BOX = '"+dbean.getC_poxbox()+"', "); 
		updcustdetailquery.append("C_HOUSE_NO = '"+dbean.getC_houseno()+"', "); 
		updcustdetailquery.append("C_STREET_NAME = '"+dbean.getC_streetname()+"', "); 
		updcustdetailquery.append("C_WARD_NAME = '"+dbean.getC_wardnumber()+"', "); 
		updcustdetailquery.append("C_CITY = '"+dbean.getC_city()+"', "); 
		updcustdetailquery.append("C_DISTRICT = '"+dbean.getC_district()+"', "); 
		updcustdetailquery.append("C_PHONE1 = '"+dbean.getC_phone1()+"', "); 
		updcustdetailquery.append("C_PHONE2 = '"+dbean.getC_phone2()+"', "); 
		updcustdetailquery.append("MAKER_DATE = SYSDATE ,"); 
		updcustdetailquery.append("MAKER_ID = '"+usercode+"', "); 
		updcustdetailquery.append("CHECKER_DATE = '' ,"); 
		updcustdetailquery.append("CHECKER_ID = '' ,"); 
		updcustdetailquery.append("MKCK_STATUS = 'M' ,"); 
		updcustdetailquery.append("CUSTOMER_STATUS = '0' ");
		updcustdetailquery.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+applicationid+"' ");
		
		enctrace("updcustdetailquery :" + updcustdetailquery.toString() );
		x = jdbctemplate.update(updcustdetailquery.toString());*/
		
		return x; 
	}
	
	
	
	
	
	public List getApplicationList(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List applist = null;
		String applistqry = "SELECT ORDER_REF_NO, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT  FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND ORDER_STATUS='01' AND MKCK_STATUS='M' AND REG_LEVEL='4' ORDER BY ORDERED_DATE DESC";
		enctrace("applistqry:::"+applistqry);
		applist = jdbctemplate.queryForList(applistqry); 
		return applist;
	}
	
	public List getApplicationListForEdit(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List applist = null;
		String applistqry = "SELECT ORDER_REF_NO, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT  FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND ORDER_STATUS='01' AND MKCK_STATUS='M' AND REG_LEVEL!='4' ORDER BY ORDERED_DATE DESC";
		enctrace("getApplicationListForEdit:::"+applistqry);
		applist = jdbctemplate.queryForList(applistqry); 
		return applist;
	}
	
	public List getCustomerData(String instid, String condqry, String suplimenttype, JdbcTemplate jdbctemplate ) throws Exception {
		List customerdata =null;
		String customerdataqry = "SELECT ORDER_REF_NO as APPLICATIONID,CIN AS CUSTOMERID, EMBOSSING_NAME, TO_CHAR(ORDERED_DATE,'DD-MM-YYYY') AS DOB,   MAKER_ID, TO_CHAR(ORDERED_DATE,'DD-MM-YYYY') AS ADDED_DATE, CHECKER_ID, TO_CHAR(CHECKER_DATE,'DD-MM-YYYY') AS AUTH_DATE , MKCK_STATUS, NVL(REMARKS,'--') AS REMARKS   FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"'" + condqry ;
		enctrace("customerdataqry :" + customerdataqry);
		customerdata = jdbctemplate.queryForList(customerdataqry);
		return customerdata ;
	}
	
	public List getApplicationData( String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		List applicationlist = null;
		String applicationlistqry = "SELECT CIN, BRANCH_CODE, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT ,ORDER_REF_NO, EMBOSSING_NAME,ENCODE_DATA,LIMIT_ID, FEE_CODE, PRODUCT_CODE, SUB_PROD_ID , LIMIT_BASEDON FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+applicationid+"' ";
		enctrace("applicationlistqry :" + applicationlistqry );
		applicationlist = jdbctemplate.queryForList(applicationlistqry);
		return applicationlist ;
	}
	
	public List getApplicationDataByBranch( String instid, String condition,  JdbcTemplate jdbctemplate ) throws Exception {
		List applicationlist = null;
		String applicationlistqry = "SELECT CIN, BRANCH_CODE, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT, ORDER_REF_NO, EMBOSSING_NAME,ENCODE_DATA,LIMIT_ID, FEE_CODE, PRODUCT_CODE, SUB_PROD_ID , LIMIT_BASEDON FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"'  "+condition;
		enctrace("applicationlistqry :" + applicationlistqry );
		applicationlist = jdbctemplate.queryForList(applicationlistqry);
		return applicationlist ;
	}
	
	public List getCustomerData( String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		List cstdetlist = null;
		StringBuilder custdetqry = new StringBuilder();
		custdetqry.append("SELECT INST_ID, ORDER_REF_NO, CIN, FNAME, MNAME, LNAME, ");
		custdetqry.append("DOB, GENDER, MARITAL_STATUS, NATIONALITY, DOCUMENT_PROVIDED, "); 
		custdetqry.append(" DOCUMENT_NUMBER, SPOUCE_NAME, MOTHER_NAME, FATHER_NAME, MOBILE, "); 
		custdetqry.append("E_MAIL, P_PO_BOX, P_HOUSE_NO, P_STREET_NAME, P_WARD_NAME, P_CITY,  ");
		custdetqry.append(" P_DISTRICT, P_PHONE1, P_PHONE2, C_PO_BOX, C_HOUSE_NO, C_STREET_NAME,  ");
		custdetqry.append(" C_WARD_NAME, C_CITY, C_DISTRICT, C_PHONE1, C_PHONE2, MAKER_DATE, MAKER_ID, "); 
		custdetqry.append(" CHECKER_DATE, CHECKER_ID, MKCK_STATUS, CUSTOMER_STATUS ");
		custdetqry.append(" FROM CUSTOMERINFO ");
		custdetqry.append(" WHERE ");
		custdetqry.append(" INST_ID = '"+instid+"' AND ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("custdetqry :" + custdetqry.toString() );
		cstdetlist = jdbctemplate.queryForList(custdetqry.toString());
		return cstdetlist ;
	}
	
	public List getAccounttypeinfo( String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		List cstdetlist = null;
		StringBuilder custdetqry = new StringBuilder();
		custdetqry.append("SELECT INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE ");
		custdetqry.append(" FROM ACCOUNTINFO ");
		custdetqry.append(" WHERE ");
		custdetqry.append(" INST_ID = '"+instid+"' AND ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("getAccounttypeinfo :" + custdetqry.toString() );
		cstdetlist = jdbctemplate.queryForList(custdetqry.toString());
		return cstdetlist ;
	}


	public int insertCustomerDetails(String instid, String orderrefno, String customerid, String usercode , JdbcTemplate jdbctemplate) {
		 int x = -1;
		 /*String insertcustdata = " INSERT INTO CUSTOMERINFO (" +
		 		"INST_ID, ORDER_REF_NO, CIN, MAKER_ID ) VALUES ( " +
		 		"'"+instid+"', '"+orderrefno+"','"+customerid+"', '"+usercode+"') ";
		 enctrace("insertcustdata :" + insertcustdata );
		 x=jdbctemplate.update(insertcustdata);*/
		 
		 
		//by gowtham-220819
		 String insertcustdata = " INSERT INTO CUSTOMERINFO (INST_ID, ORDER_REF_NO, CIN, MAKER_ID ) VALUES (?,?,?,?) ";
		 enctrace("insertcustdata :" + insertcustdata );
		 x=jdbctemplate.update(insertcustdata,new Object[]{instid,orderrefno,customerid,usercode,});

		 return x;
	}

	public int insertAccountInfo(String instid, String orderrefno,String accunttype,String customerid, String usercode,CbsCustomerRegBeans dbean,JdbcTemplate jdbctemplate) {
		int x = -1;
		/* String insertcustdata = " INSERT INTO ACCOUNTINFO (" +
		 		"INST_ID, ORDER_REF_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CURRENCY,ACCOUNTNO,ACCOUNTTYPE, CIN, ADDEDBY ) VALUES ( " +
		 		"'"+instid+"', '"+orderrefno+"',"+
		 		"'"+dbean.getAccounttypevalue()+"','"+dbean.getAccountsubtypevalue()+"','"+dbean.getTab2_currency()+"','"+dbean.getAccountnovalue()+"', "+
		 		"'"+accunttype+"' ,'"+customerid+"', '"+usercode+"') ";
		 enctrace("insertcustdata :" + insertcustdata );
		 x=jdbctemplate.update(insertcustdata);*/
		
		
		
		//by gowtham-220819
		 String insertcustdata = " INSERT INTO ACCOUNTINFO (" +
				 "INST_ID, ORDER_REF_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CURRENCY,ACCOUNTNO,ACCOUNTTYPE, CIN, ADDEDBY ) "
				 + "VALUES (?,?,?,?,?,?,?,?,?) ";
				 enctrace("insertcustdata :" + insertcustdata );
				 x=jdbctemplate.update(insertcustdata,new Object[]{instid,orderrefno,dbean.getAccounttypevalue(),dbean.getAccountsubtypevalue(),dbean.getTab2_currency(),dbean.getAccountnovalue(),accunttype,customerid,usercode});
		 
		 return x;
		  
		
	}

	public int updateAccountDetails(String instid, String applicationid,String usercode, String acctypeid, String accoutsubtypeid,String acctCCY,String acctno,CustomerRegisterationBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder updateAccountDetails = new StringBuilder(); 
		
		//INST_ID, ORDER_REF_NO, CIN, 
		updateAccountDetails.append("UPDATE ACCOUNTINFO SET ");
		updateAccountDetails.append("ACCTTYPE_ID = '"+acctypeid+"' ,");
		updateAccountDetails.append("ACCTSUB_TYPE_ID= '"+accoutsubtypeid+"' ,");
		updateAccountDetails.append("ACCT_CURRENCY= '"+acctCCY+"' ,");
		updateAccountDetails.append("ACCOUNTNO= '"+acctno+"' ");
		updateAccountDetails.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("updateAccountDetails :" + updateAccountDetails.toString() );
		x = jdbctemplate.update(updateAccountDetails.toString());
		return x; 
	} 
	
	
	public int authorizeCardOrder(String instid,String productcode,String subproduct,String limitid,String feecode,  String applicationid,String usercode,String mkrchkstatus,  JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder authorizeCardOrder = new StringBuilder(); 
		
		//INST_ID, ORDER_REF_NO, CIN, 
		authorizeCardOrder.append("UPDATE PERS_CARD_ORDER SET ");
		authorizeCardOrder.append("BIN = '"+productcode.substring(0,6)+"' ,");
		authorizeCardOrder.append("PRODUCT_CODE = '"+productcode+"' ,");
		authorizeCardOrder.append("SUB_PROD_ID = '"+subproduct+"' ,");
		authorizeCardOrder.append("LIMIT_ID = '"+limitid+"' ,");
		authorizeCardOrder.append("FEE_CODE = '"+feecode+"', ");
		authorizeCardOrder.append("CHECKER_ID = '"+usercode+"' ,");
		authorizeCardOrder.append("CHECKER_DATE= sysdate, ");
		authorizeCardOrder.append("MKCK_STATUS= '"+mkrchkstatus+"' ");
		authorizeCardOrder.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("authorizeCardOrder :" + authorizeCardOrder.toString() );
		x = jdbctemplate.update(authorizeCardOrder.toString());
		return x; 
	}  
	
	   
	public int ConfirmCardOrder(String instid, String applicationid,String usercode,JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder ConfirmCardOrder = new StringBuilder(); 
		
		//INST_ID, ORDER_REF_NO, CIN, 
		ConfirmCardOrder.append("UPDATE PERS_CARD_ORDER SET ");
		ConfirmCardOrder.append("REG_LEVEL = '4' ");
		ConfirmCardOrder.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("ConfirmCardOrder :" + ConfirmCardOrder.toString() );
		x = jdbctemplate.update(ConfirmCardOrder.toString());
		return x; 
	}  
	/*
	public int insertCustomerDetails(String instid, String usercode, String customerid,	CustomerRegisterationBeans dbean, JdbcTemplate jdbctemplate) {
		 int x = -1;
		 String insertcustdata = " INSERT INTO CUSTOMERINFO (" +
		 		"INST_ID, CIN, FNAME, MNAME, LNAME, FATHER_NAME, MOTHER_NAME, MARITAL_STATUS, " +
		 		"SPOUSE_NAME, GENDER, DOB, NATIONALITY,MAKER_DATE, MAKER_ID  ) VALUES ( " +
		 		"'"+instid+"', '"+customerid+"', '"+dbean.getFirstname()+"', '"+dbean.getMiddlename()+"', '"+dbean.getLastname()+"', '"+dbean.getFathername()+"', '"+dbean.getMiddlename()+"', '"+dbean.getMstatus()+"' "  +
		 		"'"+dbean.getSpousename()+"','"+dbean.getGender()+"',TO_DATE('"+dbean.getDob()+"','dd-mm-yyyy'),'"+dbean.getNationality()+"','"+usercode+"') ";
		 enctrace("insertcustdata :" + insertcustdata );
		 x=jdbctemplate.update(insertcustdata);
		 return x;
	}
	*/

	public int updateProductDetails(String instid, String productcode,String subproduct, String limitid, String feecode,String orderrefno, String usercode,JdbcTemplate jdbctemplate) {
		int x = -1;
		StringBuilder changeProduct = new StringBuilder(); 
		changeProduct.append("UPDATE PERS_CARD_ORDER SET ");
		changeProduct.append("PRODUCT_CODE = '"+productcode+"' ,");
		changeProduct.append("SUB_PROD_ID = '"+subproduct+"' ,");
		changeProduct.append("LIMIT_ID = '"+limitid+"' ,");
		changeProduct.append("FEE_CODE = '"+feecode+"' ");
		changeProduct.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+orderrefno+"' ");
		enctrace("changeProductQry :" + changeProduct.toString() );
		x = jdbctemplate.update(changeProduct.toString());
		return x; 
	}
	
}
