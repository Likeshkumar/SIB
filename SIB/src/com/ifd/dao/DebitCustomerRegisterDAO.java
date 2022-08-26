package com.ifd.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifd.beans.CustomerRegisterationBeans;
import com.ifd.personalize.DebitCustRegBean;
import com.ifp.Action.BaseAction;

import test.Date;

public class DebitCustomerRegisterDAO extends BaseAction {
	public List getAcctTypeList( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		try{
			
		/*String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE AUTH_CODE='1' AND INST_ID='"+instid+"'";
		acctypelist = jdbctemplate.queryForList(acctypelistqry);*/
			

			//by gowtham-210819
			String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE AUTH_CODE=? AND INST_ID=?";
			acctypelist = jdbctemplate.queryForList(acctypelistqry,new Object[]{"1",instid});
		
		}catch(Exception e){trace("the exception for getting accttype list"+e.getMessage());}
		return acctypelist;
	}
	
	public List getAcctSubTypeList( String instid,String accountypeid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		try{
			
		/*String accsubtypelistqry = "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE AUTH_CODE = '1' AND INST_ID='"+instid+"' AND ACCTTYPEID='"+accountypeid+"'";
		enctrace("accsubtypelistqry:::"+accsubtypelistqry);
		acctypelist = jdbctemplate.queryForList(accsubtypelistqry);*/

			
			//by gowtham-210819
			String accsubtypelistqry = "SELECT ACCTSUBTYPEID, ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE AUTH_CODE = ? AND INST_ID=? AND ACCTTYPEID=?";
			enctrace("accsubtypelistqry:::"+accsubtypelistqry);
			acctypelist = jdbctemplate.queryForList(accsubtypelistqry,new Object[]{"1",instid,accountypeid});
		}catch(Exception e){trace("the exception is"+e.getMessage());}
		return acctypelist;
	}
	  
	
	
	public int insertCardOrderPersonal(String instid, String orderrefno, String customerid, CustomerRegisterationBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		Date date =  new Date();
		
		
		
		String insertcardorderqry = "INSERT INTO PERS_CARD_ORDER(" +
				"INST_ID,ORDER_REF_NO ,CARD_TYPE_ID,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY,ACCOUNT_NO,SUB_PROD_ID, PRODUCT_CODE,LIMIT_ID ,FEE_CODE,CARD_QUANTITY," +
				"ORDER_STATUS, ORDER_TYPE,ORDERED_DATE,EMBOSSING_NAME,MKCK_STATUS,REMARKS,ENCODE_DATA,MAKER_DATE,MAKER_TIME,"+
				"MAKER_ID,CHECKER_DATE,CHECKER_ID,BRANCH_CODE,BIN,APP_DATE,"+  
				"CIN,APP_NO,KYC_FLAG,APPTYPE,CARDISSUETYPE,PARENTCARD, LIMIT_BASEDON , REG_LEVEL,RENEWALFLAG,CARD_COLLECT_BRANCH"+
				") VALUES ("+ ""
				+ " '"+instid+"', '"+orderrefno+"','"+dbean.getCardtypeid()+"', "+
				"'"+dbean.getAccounttypevalue()+"','"+dbean.getAccountsubtypevalue()+"','"+dbean.getTab2_currency()+"',"
				+ "'"+dbean.getAccountnovalue()+"', "+"'"+dbean.getSubproduct()+"','"+dbean.getProductcode()+"',"
				+ "'"+dbean.getLimitid()+"','"+dbean.getFeecode()+"','1', "
				+ "" +"'01','P',SYSDATE,"
				+ "UPPER('"+dbean.getEmbname()+"'),'M','--',"
				+ "UPPER('"+dbean.getEncname()+"'),SYSDATE,"	+ "'000000',"
				+ "" +"'"+dbean.getUsercode()+"',SYSDATE,"+ "'"+dbean.getUsercode()+"',"
				+ "'"+dbean.getBranchcode()+"','"+dbean.getBin()+"',"+ "SYSDATE " +","
				+ "'"+customerid+"','000',"+ "'0',"
				+ "'$DEBIT','P',"+ "'0000',"
				+ "'"+dbean.getLimitbasedon()+"','1',"+ "'"+dbean.getRenewal()+"',"
				+ "'"+dbean.getCollectbranch()+"')";
		enctrace("insertcardorderqry =====    :" + insertcardorderqry );
		x = jdbctemplate.update(insertcardorderqry);    
		
		
		
		
	
		
	/*	String insertcardorderqry = "INSERT INTO PERS_CARD_ORDER(" +
				"INST_ID,ORDER_REF_NO ,CARD_TYPE_ID,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY,ACCOUNT_NO,SUB_PROD_ID, PRODUCT_CODE,LIMIT_ID ,FEE_CODE,CARD_QUANTITY," +
				"ORDER_STATUS, ORDER_TYPE,ORDERED_DATE,EMBOSSING_NAME,MKCK_STATUS,REMARKS,ENCODE_DATA,MAKER_DATE,MAKER_TIME,"+
				"MAKER_ID,CHECKER_DATE,CHECKER_ID,BRANCH_CODE,BIN,APP_DATE,"+  
				"CIN,APP_NO,KYC_FLAG,APPTYPE,CARDISSUETYPE,PARENTCARD, LIMIT_BASEDON , REG_LEVEL,RENEWALFLAG,CARD_COLLECT_BRANCH"+
				") VALUES ("+ "?,?,?, "
						+ "?,?,?,"
		+ "?,?,?,"
		+ "?,?,?,"
		+ "?,?,SYSDATE,"
		+ "UPPER(?),?,?,"
		+ "UPPER(?),SYSDATE,"	+ "?,"
		+ "" +"?,SYSDATE,"+ "?,"
		+ "?,?,"+ "SYSDATE " +","
		+ "?,?,"+ "?,"
		+ "?,?,"+ "?,"
		+ "?,?,"+ "?,"
		+ "?)";
enctrace("insertcardorderqry :" + insertcardorderqry );
x = jdbctemplate.update(insertcardorderqry,new Object[]{
		
		instid,orderrefno,dbean.getCardtypeid(), 
		dbean.getAccounttypevalue(),dbean.getAccountsubtypevalue(),dbean.getTab2_currency(),
		dbean.getAccountnovalue(),dbean.getSubproduct(),dbean.getProductcode(),
		dbean.getLimitid(),dbean.getFeecode(),"1",
		
		
				"01","P",
				dbean.getEmbname(),"M","--",dbean.getEncname(),"000000",dbean.getUsercode(),dbean.getUsercode(),
				dbean.getBranchcode(),dbean.getBin(),customerid,"000","0","$DEBIT","P","0000",
				dbean.getLimitbasedon(),"1",dbean.getRenewal(),dbean.getCollectbranch()});    
		
		*/
		

		
		return x;   
	}
	      
	public int insertCustomerRegisterationLevel(String instid, String applicationid, String regvalue, CustomerRegisterationBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
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
				x = jdbctemplate.update(insertcardorderqry.toString(),new Object[]{instid,applicationid,regvalue});
		
		return x; 
	}
	
	
	public int updateCustomerRegisterationLevel(String instid, String applicationid, String regvalue, CustomerRegisterationBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder insertcardorderqry = new StringBuilder();
		
		/*
		insertcardorderqry.append("UPDATE CUSTOMER_APP_REGLEVEL SET ");
		insertcardorderqry.append("REGLEVEL = '"+regvalue+"'  ");
		insertcardorderqry.append("WHERE INST_ID = '"+instid+"' AND APPLICATIONID = '"+applicationid+"' ");
		enctrace("updateCustomerRegisterationLevel :" + insertcardorderqry.toString() );
		x = jdbctemplate.update(insertcardorderqry.toString());*/

		//by gowtham220819

		insertcardorderqry.append("UPDATE CUSTOMER_APP_REGLEVEL SET ");
		insertcardorderqry.append("REGLEVEL = ?  ");
		insertcardorderqry.append("WHERE INST_ID =? AND APPLICATIONID =? ");
		enctrace("updateCustomerRegisterationLevel :" + insertcardorderqry.toString() );
		x = jdbctemplate.update(insertcardorderqry.toString(),new Object[]{regvalue,instid,applicationid,});
		
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
	 
	
	public int updateCustomerContactPersionalDetails(String instid, String applicationid,String usercode, String regvalue, CustomerRegisterationBeans dbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder updcustdetailquery = new StringBuilder(); 
		
		
		///by gowtham
		//INST_ID, ORDER_REF_NO, CIN, 
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
		x = jdbctemplate.update(updcustdetailquery.toString());
		
		
	/*	
		updcustdetailquery.append("UPDATE CUSTOMERINFO SET ");
		updcustdetailquery.append("FNAME = ?, ");
		updcustdetailquery.append("MNAME = ?, ");
		updcustdetailquery.append("LNAME  = ?, ");
		updcustdetailquery.append("DOB = TO_DATE(?,'DD-MM-YYYY'), ");
		updcustdetailquery.append("GENDER = ?, "); 
		
		updcustdetailquery.append("MARITAL_STATUS = ? ,");  
		updcustdetailquery.append("NATIONALITY = ?, "); 
		updcustdetailquery.append("DOCUMENT_PROVIDED = ?, "); 
		updcustdetailquery.append("DOCUMENT_NUMBER = ?, "); 
		updcustdetailquery.append("SPOUCE_NAME = ?, "); 
		
		updcustdetailquery.append("MOTHER_NAME = ?, "); 
		updcustdetailquery.append("FATHER_NAME = ? ,"); 
		updcustdetailquery.append("MOBILE = ? ,"); 
		updcustdetailquery.append("E_MAIL = ? ,"); 
		updcustdetailquery.append("P_PO_BOX = ?, "); 
		
		updcustdetailquery.append("P_HOUSE_NO = ? ,"); 
		updcustdetailquery.append("P_STREET_NAME =?,"); 
		updcustdetailquery.append("P_WARD_NAME = ?, "); 
		updcustdetailquery.append("P_CITY = ? ,"); 
		updcustdetailquery.append("P_DISTRICT = ?, "); 
		
		updcustdetailquery.append("P_PHONE1 = ?, ");   
		updcustdetailquery.append("P_PHONE2 = ?, "); 
		updcustdetailquery.append("C_PO_BOX = ?, "); 
		updcustdetailquery.append("C_HOUSE_NO =?, "); 
		updcustdetailquery.append("C_STREET_NAME = ?, "); 
		
		updcustdetailquery.append("C_WARD_NAME = ?, "); 
		updcustdetailquery.append("C_CITY = ?, "); 
		updcustdetailquery.append("C_DISTRICT =?, "); 
		updcustdetailquery.append("C_PHONE1 = ?, "); 
		updcustdetailquery.append("C_PHONE2 = ?, "); 
		
		updcustdetailquery.append("MAKER_DATE = SYSDATE ,"); 
		updcustdetailquery.append("MAKER_ID = ?, "); 
		updcustdetailquery.append("CHECKER_DATE = ? ,"); 
		updcustdetailquery.append("CHECKER_ID = ? ,"); 
		updcustdetailquery.append("MKCK_STATUS = ? ,"); 
		
		updcustdetailquery.append("CUSTOMER_STATUS = ? ");
		updcustdetailquery.append("WHERE INST_ID = ? and ORDER_REF_NO =? ");
		
		enctrace("updcustdetailquery :" + updcustdetailquery.toString() );
		
		x = jdbctemplate.update(updcustdetailquery.toString(),new Object[]{
				
			dbean.getFirstname(),dbean.getMiddlename(),dbean.getLastname(),dbean.getDob(),dbean.getGender(),
			dbean.getMstatus(),dbean.getNationality(),dbean.getDocumentprovided(),dbean.getDocumentnumber(),dbean.getSpousename(),
			dbean.getMothername(),dbean.getFathername(),dbean.getMobile(),dbean.getEmail(),dbean.getP_poxbox(),
			dbean.getP_houseno(),dbean.getP_streetname(),dbean.getP_wardnumber(),dbean.getP_city(),dbean.getP_district(),
			dbean.getP_phone1(),dbean.getP_phone2(),dbean.getC_poxbox(),dbean.getC_houseno(),dbean.getC_streetname(),
			dbean.getC_wardnumber(),dbean.getC_city(),dbean.getC_district(),dbean.getC_phone1(),dbean.getC_phone2(),
			usercode,"","",'M',"0",instid,applicationid});*/
		
		
		
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
		
		/*String applistqry = "SELECT ORDER_REF_NO, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT  FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND ORDER_STATUS='01' AND MKCK_STATUS='M' AND REG_LEVEL!='4' ORDER BY ORDERED_DATE DESC";
		enctrace("getApplicationListForEdit:::"+applistqry);
		applist = jdbctemplate.queryForList(applistqry); 
		*/
		
		//by gowtham-210819
		String applistqry = "SELECT ORDER_REF_NO, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT  FROM PERS_CARD_ORDER WHERE INST_ID=? AND ORDER_STATUS=? AND MKCK_STATUS=? AND REG_LEVEL=? ORDER BY ORDERED_DATE DESC";
		enctrace("applistqry:::"+applistqry);
		applist = jdbctemplate.queryForList(applistqry,new Object[]{instid,"O1","M","4"});
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
		try{
			
		/*String applicationlistqry = "SELECT CIN, BRANCH_CODE, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT ,ORDER_REF_NO, EMBOSSING_NAME,ENCODE_DATA,LIMIT_ID, FEE_CODE, PRODUCT_CODE, SUB_PROD_ID , LIMIT_BASEDON FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+applicationid+"' ";
		enctrace("applicationlistqry :" + applicationlistqry );
		applicationlist = jdbctemplate.queryForList(applicationlistqry);*/
			
			//by gowtham-220819
			String applicationlistqry = "SELECT CIN, BRANCH_CODE, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT ,ORDER_REF_NO, EMBOSSING_NAME,ENCODE_DATA,LIMIT_ID, FEE_CODE, PRODUCT_CODE, SUB_PROD_ID , LIMIT_BASEDON FROM PERS_CARD_ORDER WHERE INST_ID=? AND ORDER_REF_NO=? ";
			enctrace("applicationlistqry :" + applicationlistqry );
			applicationlist = jdbctemplate.queryForList(applicationlistqry,new Object[]{instid,applicationid});
		
		}catch(Exception e){
			trace("the exception for getting application data"+e.getMessage());
		}
		return applicationlist ;
	}
	
	public List getApplicationDataByBranch( String instid, String condition,  JdbcTemplate jdbctemplate ) throws Exception {
		List applicationlist = null;
		String applicationlistqry = "SELECT CIN, BRANCH_CODE, ( EMBOSSING_NAME||'-'||ORDER_REF_NO  ) AS APPLICANT, ORDER_REF_NO, EMBOSSING_NAME,ENCODE_DATA,LIMIT_ID, FEE_CODE, PRODUCT_CODE, SUB_PROD_ID , LIMIT_BASEDON,RENEWALFLAG,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"'  "+condition;
		enctrace("applicationlistqry :" + applicationlistqry );
		applicationlist = jdbctemplate.queryForList(applicationlistqry);
		return applicationlist ;
	}
	
	public List getCustomerData( String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		List cstdetlist = null;
		StringBuilder custdetqry = new StringBuilder();
		try{
		custdetqry.append("SELECT INST_ID, ORDER_REF_NO, CIN, FNAME, MNAME, LNAME, ");
		custdetqry.append("DOB, GENDER, MARITAL_STATUS, NATIONALITY, DOCUMENT_PROVIDED, "); 
		custdetqry.append(" DOCUMENT_NUMBER, SPOUCE_NAME, MOTHER_NAME, FATHER_NAME, MOBILE, "); 
		custdetqry.append("E_MAIL, P_PO_BOX, P_HOUSE_NO, P_STREET_NAME, P_WARD_NAME, P_CITY,  ");
		custdetqry.append(" P_DISTRICT, P_PHONE1, P_PHONE2, C_PO_BOX, C_HOUSE_NO, C_STREET_NAME,  ");
		custdetqry.append(" C_WARD_NAME, C_CITY, C_DISTRICT, C_PHONE1, C_PHONE2, MAKER_DATE, MAKER_ID, "); 
		custdetqry.append(" CHECKER_DATE, CHECKER_ID, MKCK_STATUS, CUSTOMER_STATUS ");
		custdetqry.append(" FROM CUSTOMERINFO ");
		custdetqry.append(" WHERE ");
		
		
		/*custdetqry.append(" INST_ID = '"+instid+"' AND ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("custdetqry :" + custdetqry.toString() );
		cstdetlist = jdbctemplate.queryForList(custdetqry.toString());*/
		
		//by gowtham-220819
				custdetqry.append(" INST_ID = ? AND ORDER_REF_NO = ? ");
				enctrace("custdetqry :" + custdetqry.toString() );
				cstdetlist = jdbctemplate.queryForList(custdetqry.toString(),new Object[]{instid,applicationid});
		
		
		}catch(Exception e){trace("the exception for getting customer data"+e.getMessage());
		}
		return cstdetlist ;
	}
	
	public List getAccounttypeinfo( String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		List cstdetlist = null;
		StringBuilder custdetqry = new StringBuilder();
		/*custdetqry.append("SELECT INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE ");
		custdetqry.append(" FROM ACCOUNTINFO ");
		custdetqry.append(" WHERE ");
		custdetqry.append(" INST_ID = '"+instid+"' AND ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("getAccounttypeinfo :" + custdetqry.toString() );
		cstdetlist = jdbctemplate.queryForList(custdetqry.toString());
		return cstdetlist ;*/
		
		//by gowtham-220819
		custdetqry.append("SELECT INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE ");
		custdetqry.append(" FROM ACCOUNTINFO ");
		custdetqry.append(" WHERE ");
		custdetqry.append(" INST_ID =? AND ORDER_REF_NO = ? ");
		enctrace("getAccounttypeinfo :" + custdetqry.toString() );
		cstdetlist = jdbctemplate.queryForList(custdetqry.toString(),new Object[]{instid,applicationid,});
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
	 		String insertcustdata = " INSERT INTO CUSTOMERINFO (" +"INST_ID, ORDER_REF_NO, CIN, MAKER_ID )"
			 + " VALUES (?,?,?,?) ";
			 enctrace("insertcustdata :" + insertcustdata );
			 x=jdbctemplate.update(insertcustdata,new Object[]{instid,orderrefno,customerid,usercode});
		 return x;
	}

	public int insertAccountInfo(String instid, String orderrefno,String accunttype,String customerid, String usercode,CustomerRegisterationBeans dbean,JdbcTemplate jdbctemplate) {
		int x = -1;
		 /*String insertcustdata = " INSERT INTO ACCOUNTINFO (" +
		 		"INST_ID, ORDER_REF_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CURRENCY,ACCOUNTNO,ACCOUNTTYPE, CIN, ADDEDBY ) VALUES ( " +
		 		"'"+instid+"', '"+orderrefno+"',"+
		 		"'"+dbean.getAccounttypevalue()+"','"+dbean.getAccountsubtypevalue()+"','"+dbean.getTab2_currency()+"','"+dbean.getAccountnovalue()+"', "+
		 		"'"+accunttype+"' ,'"+customerid+"', '"+usercode+"') ";
		 enctrace("insertcustdata :" + insertcustdata );
		 x=jdbctemplate.update(insertcustdata);*/
		
		
		//by gowthma220819
		String insertcustdata = " INSERT INTO ACCOUNTINFO (" +
		"INST_ID, ORDER_REF_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CURRENCY,ACCOUNTNO,ACCOUNTTYPE, CIN, ADDEDBY )"
		+ " VALUES "
		+ "(?,?,?,?,?,?,? ,?,?) ";
		enctrace("insertcustdata :" + insertcustdata );
		x=jdbctemplate.update(insertcustdata,new Object[]{
				instid,orderrefno,dbean.getAccounttypevalue(),dbean.getAccountsubtypevalue(),dbean.getTab2_currency(),
				dbean.getAccountnovalue(),accunttype ,customerid,usercode});
		 
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
	
	
	public String getCardtypeList1(String instid,String BIN, JdbcTemplate jdbctemplate)
	{
		String cardtypelist = "";
		String cardtypeqry ="SELECT CARD_TYPE_ID FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' and PRODUCT_CODE ='" + BIN +"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		cardtypelist=(String) jdbctemplate.queryForObject(cardtypeqry, String.class);
		return cardtypelist ;
	}

	public int authorizeCardOrder(String instid,String productcode,String subproduct,String limitid,String feecode,  String applicationid,String usercode,String mkrchkstatus,  JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String cardtypeid;
		Date date =  new Date();
		cardtypeid = this.getCardtypeList1(instid, productcode, jdbctemplate);
		StringBuilder authorizeCardOrder = new StringBuilder(); 
		String binNumber=productcode.substring(0, 8);
		trace(" bin number =====  "+binNumber);
		//INST_ID, ORDER_REF_NO, CIN, 
	/*	authorizeCardOrder.append("UPDATE PERS_CARD_ORDER SET ");
		authorizeCardOrder.append("BIN = '"+productcode.substring(0,6)+"' ,");
		authorizeCardOrder.append("PRODUCT_CODE = '"+productcode+"' ,");
		authorizeCardOrder.append("SUB_PROD_ID = '"+subproduct+"' ,");
		//authorizeCardOrder.append("CARD_TYPE_ID = '"+productcode.substring(7,10)+"' ,");
		authorizeCardOrder.append("CARD_TYPE_ID = '"+cardtypeid+"' ,");
		authorizeCardOrder.append("LIMIT_ID = '"+limitid.trim()+"' ,");
		authorizeCardOrder.append("FEE_CODE = '"+feecode.trim()+"', ");
		authorizeCardOrder.append("CHECKER_ID = '"+usercode+"' ,");
		authorizeCardOrder.append("CHECKER_DATE= sysdate, ");
		authorizeCardOrder.append("MKCK_STATUS= '"+mkrchkstatus+"' ");
		authorizeCardOrder.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("authorizeCardOrder :" + authorizeCardOrder.toString() );
		x = jdbctemplate.update(authorizeCardOrder.toString());
		return x; */
		
		//by gowtham220819
		authorizeCardOrder.append("UPDATE PERS_CARD_ORDER SET ");
		authorizeCardOrder.append("BIN = ? ,");
		authorizeCardOrder.append("PRODUCT_CODE =? ,");
		authorizeCardOrder.append("SUB_PROD_ID = ? ,");
		//authorizeCardOrder.append("CARD_TYPE_ID = '"+productcode.substring(7,10)+"' ,");
		authorizeCardOrder.append("CARD_TYPE_ID = ? ,");
		authorizeCardOrder.append("LIMIT_ID = ? ,");
		authorizeCardOrder.append("FEE_CODE = ?, ");
		authorizeCardOrder.append("CHECKER_ID =? ,");
		authorizeCardOrder.append("CHECKER_DATE=?, ");
		authorizeCardOrder.append("MKCK_STATUS= ? ");
		authorizeCardOrder.append("WHERE INST_ID =? and ORDER_REF_NO = ? ");
		enctrace("authorizeCardOrder :" + authorizeCardOrder.toString() );
		x = jdbctemplate.update(authorizeCardOrder.toString(),new Object[]{binNumber,productcode,subproduct,cardtypeid,limitid.trim(),feecode.trim(),usercode,date.getCurrentDate(),mkrchkstatus,instid,applicationid});
		
		return x; 
	}  
	
	   
	public int ConfirmCardOrder(String instid, String applicationid,String usercode,JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		StringBuilder ConfirmCardOrder = new StringBuilder(); 
		
		/*//INST_ID, ORDER_REF_NO, CIN, 
		ConfirmCardOrder.append("UPDATE PERS_CARD_ORDER SET ");
		ConfirmCardOrder.append("REG_LEVEL = '4' ");
		ConfirmCardOrder.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+applicationid+"' ");
		enctrace("ConfirmCardOrder :" + ConfirmCardOrder.toString() );
		x = jdbctemplate.update(ConfirmCardOrder.toString());*/
		
		//by gowtham220819
		ConfirmCardOrder.append("UPDATE PERS_CARD_ORDER SET ");
		ConfirmCardOrder.append("REG_LEVEL = ? ");
		ConfirmCardOrder.append("WHERE INST_ID =? and ORDER_REF_NO = ? ");
		enctrace("ConfirmCardOrder :" + ConfirmCardOrder.toString() );
		x = jdbctemplate.update(ConfirmCardOrder.toString(),new Object[]{"4",instid,applicationid});
		
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

	public List getAllRecords(String instid, String checkcondition, JdbcTemplate jdbctemplate) {
		List applicationlist = null;
		String applicationlistqry = "SELECT BRANCH_CODE, ORDER_REF_NO,ACCOUNT_NO, EMBOSSING_NAME,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE = A.PRODUCT_CODE) AS PRODUCT_CODE ,(SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE ACCTTYPEID=A.ACCTTYPE_ID) AS ACCTTYPE_ID,(SELECT ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE ACCTSUBTYPEID = A.ACCTSUB_TYPE_ID) AS ACCTSUB_TYPE_ID ,TO_CHAR(ORDERED_DATE,'DD-MM-YYYY') AS ORDERED_DATE,DECODE(RENEWALFLAG,'N','No','Y','Yes') AS RENEWALFLAG,(select username from USER_DETAILS where userid=MAKER_ID) as MAKER_ID,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH FROM PERS_CARD_ORDER A WHERE INST_ID='"+instid+"'  "+checkcondition;
		enctrace("applicationAlllistqry :" + applicationlistqry );
		applicationlist = jdbctemplate.queryForList(applicationlistqry);
		return applicationlist ;
	}

	public int authorizeAllCardOrder(String instid, String refno, String usercode, String mkrckr, JdbcTemplate jdbctemplate) {
int x = -1;
Date date =  new Date();
		StringBuilder authorizeCardOrder = new StringBuilder(); 
		/*
		//INST_ID, ORDER_REF_NO, CIN, 
		authorizeCardOrder.append("UPDATE PERS_CARD_ORDER SET ");
		authorizeCardOrder.append("CHECKER_ID = '"+usercode+"' ,");
		authorizeCardOrder.append("CHECKER_DATE= sysdate, ");
		authorizeCardOrder.append("MKCK_STATUS= '"+mkrckr+"' ");
		authorizeCardOrder.append("WHERE INST_ID = '"+instid+"' and ORDER_REF_NO = '"+refno+"' ");
		enctrace("authorizeAllCardOrder :" + authorizeCardOrder.toString() );
		x = jdbctemplate.update(authorizeCardOrder.toString());*/
		
		
		//by gowtham-220819
				authorizeCardOrder.append("CHECKER_ID =? ,");
				authorizeCardOrder.append("CHECKER_DATE=?, ");
				authorizeCardOrder.append("MKCK_STATUS= ? ");
				authorizeCardOrder.append("WHERE INST_ID =? and ORDER_REF_NO =? ");
				enctrace("authorizeAllCardOrder :" + authorizeCardOrder.toString() );
				x = jdbctemplate.update(authorizeCardOrder.toString(),new Object[]{usercode,date.getCurrentDate(),mkrckr,instid,refno});
				
		return x; 
	}
	
	
}
