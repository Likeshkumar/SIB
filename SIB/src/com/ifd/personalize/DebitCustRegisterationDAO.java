package com.ifd.personalize;

 
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate; 
 
import com.ifp.Action.BaseAction;
import com.ifd.personalize.DebitCustRegBean;

public class DebitCustRegisterationDAO extends BaseAction {
	
	
	public int deletePersonalDetails(String instid, String tablename, String applicationid, String customerid, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String deleteqry = "DELETE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' AND customerid='"+customerid+"'";
		enctrace("deleteqry :" + deleteqry );
		x = jdbctemplate.update(deleteqry);
		return x;
	}
	
	public int intinsertPersonalDetails( String instid, String applicationid, String customerid, String tablename, DebitCustRegBean creditbean, String suplimenttype, String addedby, String authstatus, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, customerid, firstname, middlename, lastname, encname, embname, nationality, " ;
		personalqry += " dob, gender, mstatus, residence, vehicle , vehicleno, idproof, idproofno, dateofissue, districofissue, typeofcard, spousename, " ;
		personalqry += " mothername, fathername, grandfathername,stmtdelivery, smsalert,suppstatus,added_by,added_date, auth_status ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"','"+customerid+"','"+creditbean.firstname+"','"+creditbean.middlename+"','"+creditbean.lastname+"','"+creditbean.encname+"','"+creditbean.embname+"','"+creditbean.nationality+"', " ;
		personalqry += " TO_DATE('"+creditbean.dob+"','dd-mm-yyyy'),'"+creditbean.gender+"','"+creditbean.mstatus+"','"+creditbean.residence+"','"+creditbean.vehicle+"','"+creditbean.vehicleno+"','"+creditbean.idproof+"','"+creditbean.idproofno+"',to_date('"+creditbean.dateofissue+"','DD-MM-YYYY'),'"+creditbean.districofissue+"','"+creditbean.typeofcard+"','"+creditbean.spousename+"', ";
		personalqry += " '"+creditbean.mothername+"','"+creditbean.fathername+"','"+creditbean.grandfathername+"', '"+creditbean.stmtdelivery+"','"+creditbean.smsalert+"','"+suplimenttype+"','"+addedby+"',SYSDATE,'"+authstatus+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}
	
	public int intinsertPersonalDetailsSupliment( String instid, String applicationid, String customerid, String tablename, DebitCustRegBean creditbean, String suplimenttype, String addedby, String authstatus, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, customerid, firstname, middlename, lastname, encname, embname, nationality, " ;
		personalqry += " dob, gender, dateofissue,  " ;
		personalqry += "stmtdelivery, smsalert,suppstatus,added_by,added_date, auth_status  ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"','"+customerid+"','"+creditbean.supfirstname+"','"+creditbean.supmidname+"','"+creditbean.suplastname+"','"+creditbean.supnameoncard+"','"+creditbean.supnameoncard+"','"+creditbean.supnationality+"', " ;
		personalqry += " TO_DATE('"+creditbean.supdob+"','dd-mm-yyyy'),'"+creditbean.supgender+"',  SYSDATE ,";
		personalqry += "'$MAIL','Y','"+suplimenttype+"','"+addedby+"',SYSDATE,'"+authstatus+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}
	
	
	public int insertCardInitialize( String instid, String applicationid, String customerid, String orderid, String tablename, String processstatus, String  relationship, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, customerid, orderid, process_status, entrydate, relationship ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"','"+customerid+"','"+orderid+"','"+processstatus+"',SYSDATE,'"+relationship+"') "; 
		enctrace("initialize card :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}
	
	
	public int deleteCustomerAddress( String instid, String applicationid, String tablename, DebitCustRegBean creditbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"'";
		enctrace("deleteqry :" + deleteqry );
		x = jdbctemplate.update(deleteqry);
		return x;
		
	}
	public int insertCustomerAddress( String instid, String applicationid, String tablename, DebitCustRegBean creditbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, prpobox, prhouseno, prstreetname, prwardname, prcity, prdistrict, " ;
		personalqry += " prphone1, prphone2, prfax, primaryemail, prmobileno , " ;
		personalqry += " copobox, cohouseno, costreetname, cowardname, cocity, codistrict, cophone1, " ;
		personalqry += " cophone2, cofax, secemail,comobileno ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"','"+creditbean.prpobox+"','"+creditbean.prhouseno+"','"+creditbean.prstreetname+"','"+creditbean.prwardname+"','"+creditbean.prcity+"','"+creditbean.prdistrict+"', " ;
		personalqry += " '"+creditbean.prphone1+"','"+creditbean.prphone2+"','"+creditbean.prfax+"','"+creditbean.primaryemail+"','"+creditbean.prmobileno+"', " ;
		personalqry += " '"+creditbean.copobox+"','"+creditbean.cohouseno+"','"+creditbean.costreetname+"','"+creditbean.cowardname+"','"+creditbean.cocity+"','"+creditbean.codistrict+"', '"+creditbean.cophone1+"', ";
		personalqry += " '"+creditbean.cophone2+"','"+creditbean.cofax+"','"+creditbean.secemail+"', '"+creditbean.comobileno+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}
	
	
	public int deleteCustomerBusinessInfo(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"'";
		enctrace("deleteqry :" + deleteqry );
		x = jdbctemplate.update(deleteqry);
		return x;
	}
	
	public int insertBusinessDetails( String instid, String applicationid, String tablename, DebitCustRegBean creditbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, occupation, workfor, companyname, designation, natofbusiness, " ;
		personalqry +=  " employedsince, emppobox, emphouseno, empstreetname, empwardname, empcity, empdistrict , " ; 
		personalqry += " empphone1, empphone2, empfax, empemail, empmobileno ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"','"+creditbean.occupation+"','"+creditbean.workfor+"','"+creditbean.companyname+"','"+creditbean.designation+"','"+creditbean.natofbusiness+"', " ;
		personalqry += " '"+creditbean.employedsince+"','"+creditbean.emppobox+"', '"+creditbean.emphouseno+"','"+creditbean.empstreetname+"','"+creditbean.empwardname+"','"+creditbean.empcity+"','"+creditbean.empdistrict+"', " ;
		personalqry += " '"+creditbean.empphone1+"','"+creditbean.empphone2+"','"+creditbean.empfax+"','"+creditbean.empemail+"','"+creditbean.empmobileno+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}


	public int insertReference(String instid, String applicationid, String tablename,  String referncename, String referencephone,	JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String inserrefqry = "INSERT INTO "+tablename+" (inst_id, applicationid, referenceid, referncename, referencephone ) VALUES " ; 
		inserrefqry += " ('"+instid+"', '"+applicationid+"', IFC_REFERENCESEQ.nextval, '"+referncename+"', '"+referencephone+"') " ;
		enctrace("inserrefqry :" + inserrefqry );
		x = jdbctemplate.update(inserrefqry);
		return x;
	}
	
	public int checkValidReferenceName(String instid, String applicationid, String tablename, String refname, JdbcTemplate jdbctemplate  ) throws Exception {
		int x =-1;
		String validrefqry = "SELECT COUNT(*) FROM  "+tablename+" WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' AND TRIM( UPPER(REFERNCENAME) ) = TRIM(UPPER('"+refname+"')) ";
		enctrace("validrefqry :" + validrefqry );
		x = jdbctemplate.queryForInt(validrefqry);
		return x;
	}
	
	public int checkValidMobile(String instid, String applicationid, String tablename, String phone, JdbcTemplate jdbctemplate  ) throws Exception {
		int x =-1;
		String validrefqry = "SELECT COUNT(*) FROM  "+tablename+" WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' AND TRIM( UPPER(REFERENCEPHONE) ) = TRIM(UPPER('"+phone+"')) ";
		enctrace("validrefqry :" + validrefqry );
		x = jdbctemplate.queryForInt(validrefqry);
		return x;
	}
	
	public int checkDocuemntConfigured(String instid, String applicationid, String tablename, String documenttype, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String validrefqry = "SELECT COUNT(*) FROM  "+tablename+" WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' AND TRIM( UPPER(DOCUMENTTYPE) ) = TRIM(UPPER('"+documenttype+"')) ";
		enctrace("validrefqry :" + validrefqry );
		x = jdbctemplate.queryForInt(validrefqry);
		return x;
	} 
	
	public List getListOfReferece(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List reflist = null;
		String reflistqry = "SELECT referenceid, referncename, referencephone FROM "+tablename + " WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' ";
		enctrace("reflistqry :"+reflistqry);
		reflist = jdbctemplate.queryForList(reflistqry);
		return reflist;
	}


	public int deleteReference(String instid, String applicationid, String refid,  String tablename, JdbcTemplate jdbctemplate) {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+ " WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' AND REFERENCEID='"+refid+"'" ;		 
		enctrace("deleteqry :" + deleteqry);
		x = jdbctemplate.update(deleteqry);
		return x; 
	}
	
	
	public int insertDocument(String instid, String applicationid, String tablename,  String documenttype, String documentnumber,	JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String inserrefqry = "INSERT INTO "+tablename+" (inst_id, applicationid, documentseq, documenttype, documentnumber ) VALUES " ; 
		inserrefqry += " ('"+instid+"', '"+applicationid+"', IFC_REFERENCESEQ.nextval, '"+documenttype+"', '"+documentnumber+"') " ;
		enctrace("inserrefqry :" + inserrefqry );
		x = jdbctemplate.update(inserrefqry);
		return x;
	}
	
	public List getListOfDocument(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List doclist = null;
		String doclistqry = "SELECT documentseq, DOCUMENTTYPE, documentnumber FROM "+tablename + " WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' ";
		enctrace("doclistqry :"+doclistqry);
		doclist = jdbctemplate.queryForList(doclistqry);
		return doclist;
	}


	public int deleteDocument(String instid, String applicationid,	String documentid, String tablename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+ " WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"' AND DOCUMENTSEQ='"+documentid+"'" ;		 
		enctrace("deleteqry :" + deleteqry);
		x = jdbctemplate.update(deleteqry);
		return x; 
	}
	
	public List getCreditMasterLimits(String instid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List limitlist = null;
		String limitlistqry = "SELECT CRLIMITID, CRLIMITNAME  FROM "+tablename + "  WHERE INST_ID='"+instid+"' and CRSTATUS='1'";
		enctrace("limitlistqry :"+limitlistqry);
		limitlist = jdbctemplate.queryForList( limitlistqry );
		return limitlist;
	}
	
	public List getPayType(String instid,  JdbcTemplate jdbctemplate ) throws Exception {
		List paylist = null;
		String paylistqry = "SELECT PAYTYPEID, PAYTYPEVALUE  FROM  IFC_PAYTYPE  WHERE INST_ID='"+instid+"' and PAYSTATUS='1'";
		enctrace("paylistqry :"+paylistqry);
		paylist= jdbctemplate.queryForList( paylistqry );
		return paylist;
	}
	
	public List getAccountType(String instid,  JdbcTemplate jdbctemplate ) throws Exception {
		List accttypelist = null;
		String accttypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC  FROM  IFC_ACCTTYPE  WHERE INST_ID='"+instid+"' and ACCTTYPESTATUS='1'";
		enctrace("accttypelistqry :"+accttypelistqry);
		accttypelist= jdbctemplate.queryForList( accttypelistqry );
		return accttypelist;
	}
	
	 
	
	 
	 
	public int deleteIncomeDetails(String instid, String applicationid,String tablename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+ " WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"'" ;		 
		enctrace("deleteqry :" + deleteqry);
		x = jdbctemplate.update(deleteqry);
		return x; 
	}
	
	
	public int insertIncomeDetails( String instid, String applicationid, String tablename, DebitCustRegBean creditbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, annualsal, annualbonus, annualbusinessincome, rentalincome, agriculture, " ;
		personalqry +=  " otherincome, totannualincome  ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"','"+creditbean.annualsal+"','"+creditbean.annualbonus+"','"+creditbean.annualbusinessincome+"','"+creditbean.rentalincome+"','"+creditbean.agriculture+"', " ;
		personalqry += " '"+creditbean.otherincome+"','"+creditbean.totannualincome+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}
	
	public int deleteCustomerExistingBankDet(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+ " WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"'" ;		 
		enctrace("deleteqry :" + deleteqry);
		x = jdbctemplate.update(deleteqry);
		return x; 
	}
	
	
 
	public int insertCustomerExistCrediBankDetails(String instid, String applicationid, String tablename, DebitCustRegBean creditbean, JdbcTemplate jdbctemplate) throws Exception{
		int x=-1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, existcreditcard, excrcardbank, excrcardbranch, excrcardacctype ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"','"+creditbean.existcreditcard+"','"+creditbean.excrcardbank+"','"+creditbean.excrcardbranch+"','"+creditbean.excrcardacctype+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}
	
	 
	public int deleteStandingDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+ " WHERE INST_ID='"+instid+"' AND applicationid='"+applicationid+"'" ;		 
		enctrace("deleteqry :" + deleteqry);
		x = jdbctemplate.update(deleteqry);
		return x; 
	} 
	
	public int insertStandingDetails( String instid, String customerid, String tablename, DebitCustRegBean creditbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, validfrom, validto, acctwithprimarybank, primarybankacctno, primarybankbranch, " ;
		personalqry +=  " primaryacctcur, paytype, payableamt,payableday  ) VALUES ( "; 
		personalqry += " '"+instid+"','"+customerid+"',TO_DATE('"+creditbean.validfrom+"','DD-MM-YYYY'),TO_DATE('"+creditbean.validto+"','DD-MM-YYYY'),'"+creditbean.acctwithprimarybank+"', '"+creditbean.primarybankacctno+"', '"+creditbean.primarybankbranch+"'," ;
		personalqry += " '"+creditbean.primaryacctcur+"', '"+creditbean.paytype+"','"+creditbean.payableamt+"','"+creditbean.payableday+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	} 
	
	public int insertSupplimentaryDetails( String instid, String applicationid, String tablename, DebitCustRegBean creditbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String personalqry = "INSERT INTO "+tablename+" (inst_id, applicationid, supfirstname, supmidname, suplastname, supdob, supnationality, " ;
		personalqry +=  " supnameoncard, supgender, suprelationship  ) VALUES ( "; 
		personalqry += " '"+instid+"','"+applicationid+"', '"+creditbean.supfirstname+"',  '"+creditbean.supmidname+"',  '"+creditbean.suplastname+"', TO_DATE('"+creditbean.supdob+"','DD-MM-YYYY'), '"+creditbean.supnationality+"'," ;
		personalqry += " '"+creditbean.supnameoncard+"', '"+creditbean.supgender+"','"+creditbean.suprelationship+"') "; 
		enctrace("personalqry :" + personalqry );
		x = jdbctemplate.update(personalqry);
		return x;
	}
	
	/*public List getListOfSuplimentaryCards(String instid, String customerid, String tablename, JdbcTemplate jdbctemplate ) throws Exception{
		String supplimentcardlist = null;
		String supplimentcardlistqry ="SELECT "
	}
	*/
	 

	public List getApplicationList(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List applist = null;
		String applistqry = "SELECT APPLICATIONID, ( APPLICATIONID || ' - ' ||FIRSTNAME || ' ' || LASTNAME ) AS APPLICANT  FROM IFC_CUSTOMERINFO_TEMP WHERE INST_ID='"+instid+"' AND SUPPSTATUS='$OWN' ORDER BY ADDED_DATE DESC";
		applist = jdbctemplate.queryForList(applistqry); 
		return applist;
	}
	
	public String getApplicationByCustomer(String instid, String customerid, JdbcTemplate jdbctemplate) throws Exception {
		String appcode = null;
		String appcodeqry = "SELECT  ( APPLICATIONID || ' - ' ||FIRSTNAME || ' ' || LASTNAME ) AS APPLICANT  FROM IFC_CUSTOMERINFO_TEMP WHERE INST_ID='"+instid+"' AND  CUSTOMERID='"+customerid+"'";
		enctrace("appcodeqry :"+ appcodeqry);
		try{
			appcode = (String)jdbctemplate.queryForObject(appcodeqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return appcode;
	}
	
	public List getApplicationListForAuth(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List applist = null;
		String applistqry = "SELECT APPLICATIONID, ( APPLICATIONID || ' - ' ||FIRSTNAME || ' ' || LASTNAME ) AS APPLICANT  FROM IFC_CUSTOMERINFO_TEMP WHERE INST_ID='"+instid+"' AND SUPPSTATUS='$OWN' AND AUTH_STATUS='0' ORDER BY ADDED_DATE DESC";
		applist = jdbctemplate.queryForList(applistqry); 
		return applist;
	}
	
	public List getCustomerData(String instid, String condqry, String suplimenttype, JdbcTemplate jdbctemplate ) throws Exception {
		List customerdata =null;
		String customerdataqry = "SELECT APPLICATIONID,CUSTOMERID, FIRSTNAME, MIDDLENAME, LASTNAME, TO_CHAR(DOB,'DD-MM-YYYY') AS DOB, DECODE(GENDER,'M','MALE','F','FEMALE','O','OTHER',GENDER) GENDER, ADDED_BY, TO_CHAR(ADDED_DATE,'DD-MM-YYYY') AS ADDED_DATE, AUTH_BY, TO_CHAR(AUTH_DATE,'DD-MM-YYYY') AS AUTH_DATE , AUTH_STATUS, NVL(REMARKS,'--') AS REMARKS   FROM IFC_CUSTOMERINFO_TEMP WHERE INST_ID='"+instid+"'" + condqry +" AND SUPPSTATUS='"+suplimenttype+"' AND STATUS='1'" ;
		enctrace("customerdataqry :" + customerdataqry);
		customerdata = jdbctemplate.queryForList(customerdataqry);
		return customerdata ;
	}
	
	public List getCustomerDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List customerdetails = null;
		String customerdetailsqry = "SELECT  firstname, middlename, lastname, encname, embname, nationality, to_char(dob,'dd-mm-yyyy') as dob, gender, mstatus, residence, vehicle , vehicleno, idproof, idproofno, " ;
		customerdetailsqry += " to_char(dateofissue,'dd-mm-yyyy') as dateofissue , districofissue, typeofcard, spousename, mothername, fathername, grandfathername,stmtdelivery, smsalert FROM "+tablename+" WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'   ";
		enctrace("customerdetailsqry  :"+ customerdetailsqry );
		customerdetails = jdbctemplate.queryForList(customerdetailsqry);
		return customerdetails ;
	}
	
	public List getCustomerDetailsByCustomerId(String instid, String customerid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List customerdetails = null;
		String customerdetailsqry = "SELECT  firstname, middlename, lastname, encname, embname, nationality, to_char(dob,'dd-mm-yyyy') as dob, gender, mstatus, residence, vehicle , vehicleno, idproof, idproofno, " ;
		customerdetailsqry += " to_char(dateofissue,'dd-mm-yyyy') as dateofissue , districofissue, typeofcard, spousename, mothername, fathername, grandfathername,stmtdelivery, smsalert FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CUSTOMERID='"+customerid+"'   ";
		enctrace("customerdetailsqry  :"+ customerdetailsqry );
		customerdetails = jdbctemplate.queryForList(customerdetailsqry);
		return customerdetails ;
	}
	
	public List selectCustomerAddress(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List customerdetails = null;
		String customerdetailsqry = "SELECT PRPOBOX, PRHOUSENO,PRHOUSENO,PRSTREETNAME,PRWARDNAME,PRCITY, PRDISTRICT , prphone1, prphone2, prfax, primaryemail, prmobileno, copobox, cohouseno, costreetname, cowardname, cocity, codistrict, cophone1, COMOBILENO " ;
		customerdetailsqry += "  cophone2, cofax, secemail,comobileno FROM "+tablename+" WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'  ";
		enctrace("customerdetailsqry  :"+ customerdetailsqry );
		customerdetails = jdbctemplate.queryForList(customerdetailsqry);
		return customerdetails ;
	}
	
	
	
	public List getCustomerBusinessDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List customerdetails = null;
		String customerdetailsqry = "SELECT  OCCUPATION, WORKFOR, COMPANYNAME, DESIGNATION, NATOFBUSINESS, EMPLOYEDSINCE, EMPPOBOX, EMPHOUSENO, EMPSTREETNAME,  " ;
		customerdetailsqry += "EMPWARDNAME, EMPCITY, EMPDISTRICT, EMPPHONE1, EMPPHONE2, EMPFAX, EMPEMAIL, EMPMOBILENO FROM "+tablename ; 
		customerdetailsqry += "  WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'  ";
		enctrace("customerdetailsqry  :"+ customerdetailsqry );
		customerdetails = jdbctemplate.queryForList(customerdetailsqry);
		return customerdetails ;
	}
	
	
	public List getIncominDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List incomedetail = null;
		String incomedetailqry ="SELECT ANNUALSAL, ANNUALBONUS, ANNUALBUSINESSINCOME, RENTALINCOME, AGRICULTURE,OTHERINCOME, TOTANNUALINCOME FROM "+tablename ; 
		incomedetailqry += "  WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'  ";
		enctrace("incomedetailqry  :"+ incomedetailqry );
		incomedetail = jdbctemplate.queryForList(incomedetailqry);
		return incomedetail;
	}
	
	
	public List getExistingCreditCardDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List existcarddet = null;
		String existcarddetqry ="SELECT EXISTCREDITCARD, EXCRCARDBANK, EXCRCARDBRANCH, EXCRCARDACCTYPE  FROM "+tablename ; 
		existcarddetqry += "  WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'  ";
		enctrace("incomedetailqry   :"+ existcarddetqry   );
		existcarddet = jdbctemplate.queryForList(existcarddetqry  );
		return existcarddet;
	}
	 
	public List getStandingDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List standingdetail = null;
		String standingdetailqry ="SELECT TO_CHAR(VALIDFROM,'DD-MM-YYYY') AS VALIDFROM, TO_CHAR(VALIDTO,'DD-MM-YYYY') AS VALIDTO, ACCTWITHPRIMARYBANK, PRIMARYBANKACCTNO, PRIMARYBANKBRANCH,PAYTYPE,PAYABLEAMT, PAYABLEDAY,PRIMARYACCTCUR FROM "+tablename ; 
		standingdetailqry += "  WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'  ";
		enctrace("standingdetailqry :"+ standingdetailqry);
		standingdetail = jdbctemplate.queryForList(standingdetailqry);
		return standingdetail;
	}
 
	
	public List getSupplimentCustomerDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		List supplimentlist = null;
		String supplimentlistqry = "SELECT FIRSTNAME, MIDDLENAME, LASTNAME, ENCNAME, NATIONALITY, TO_CHAR(DOB,'DD-MM-YYYY') AS DOB, DECODE(GENDER,'M','MALE','F','FEMALE','O','OTHER',GENDER) AS GENDER, DECODE(MSTATUS,'M','MARRIED','U','UN-MARRIED',MSTATUS) AS MSTATUS, "; 
		supplimentlistqry += " RESIDENCE, VEHICLE, VEHICLENO, IDPROOF, IDPROOFNO, TO_CHAR(DATEOFISSUE,'DD-MM-YYYY') AS DATEOFISSUE FROM  "+tablename + "  WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"' AND SUPPSTATUS !='$OWN' ";
		enctrace("supplimentlistqry :" + supplimentlistqry );
		supplimentlist = jdbctemplate.queryForList(supplimentlistqry);
		return supplimentlist;
	}

	public String getApplicationIdByCustomerid(String instid, String customerid, JdbcTemplate jdbctemplate) {
		String applicationid = null;
		String applicationidqry = "SELECT APPLICATIONID FROM IFC_CUSTOMERINFO_TEMP WHERE INST_ID='"+instid+"' AND CUSTOMERID='"+customerid+"' AND STATUS='1' ";
		enctrace("applicationidqry :" + applicationidqry);
		try{
			applicationid = (String)jdbctemplate.queryForObject(applicationidqry, String.class);
		}catch(EmptyResultDataAccessException e ){}
	 
		return applicationid;
	} 
	
	public int deleteSupplimenaryUser(String instid, String applicationid, String customerid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String deleteqry = "DELETE FROM "+tablename+"  WHERE INST_ID='"+instid+"' AND CUSTOMERID='"+customerid+"'";
		enctrace("deleteqry :" + deleteqry );
		x = jdbctemplate.update(deleteqry);
		return x;
	}

	public String getGenderDesc(String gender) { 
		if( gender == null ){
			gender = " -- ";
		}else if( gender.equals("M") ){
			gender = "Male";
		}else if( gender.equals("F") ){
			gender = "Female";
		}else if( gender.equals("O") ){
			gender = "Other";
		}
		return gender;
	}

	public String getMaritalDesc(String mstatus) {
		if( mstatus == null ){
			mstatus= " -- ";
		}else if( mstatus.equals("M") ){
			mstatus = "Married";
		}else if( mstatus.equals("U") ){
			mstatus = "Un-Married";
		}else if( mstatus.equals("O") ){
			mstatus = "Other";
		}
		return mstatus;
	}
	
	public String getOwnVehiclDesc(String ownvehicle) {
		if( ownvehicle == null ){
			ownvehicle= " -- ";
		}else if( ownvehicle.equals("Y") ){
			ownvehicle = "Yes, Available";
		}else if( ownvehicle.equals("N") ){
			ownvehicle = "No, Not Available";
		} 
		return ownvehicle;
	}
	
	public String getResidenceDesc(String residence) {
		if(  residence == null ){
			 residence= " -- ";
		}else if(  residence.equals("W") ){
			residence = "Own";
		}else if( residence.equals("R") ){
			residence = "Rental";
		} 
		return residence;
	}
	
	public String getStatementDeliveryDesc( String deliverykey ) throws Exception {
		if(  deliverykey == null ){
			deliverykey= " -- ";
		}else if(  deliverykey.equals("$MAIL") ){
			deliverykey = " Through Mail ";
		}else if( deliverykey.equals("$DIRECT") ){
			deliverykey= "Direct ";
		} 
		return deliverykey;
	}
	
	public String smsDeliveryRequired(String deliverystatus ) {
		if( deliverystatus == null ){
			deliverystatus = " -- ";
		}else if( deliverystatus.equals("Y") ){
			deliverystatus= "Yes";
		}else if( deliverystatus.equals("N") ){
			deliverystatus= "No";
		} 
		return deliverystatus;
	}
	
	public String getTypeofCardDesc(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		String limitdesc = null;
		String creditlimitdescqry = "SELECT CRLIMITDESC FROM IFC_CREDITLIMIT WHERE inst_id='"+instid+"' and CRLIMITID='"+limitid+"' ";
		enctrace("creditlimitdescqry :" + creditlimitdescqry);
		try{
			if( limitid == null) { return "--"; }
			limitdesc = (String)jdbctemplate.queryForObject(creditlimitdescqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return limitdesc ;
	}
	
	public String getOccupationDesc(String instid, String occupationid, JdbcTemplate jdbctemplate ) throws Exception {
		String occupationdesc = null;
		String occupationdescqry = "SELECT OCCUPAITONNAME FROM IFC_OCCUPATION WHERE inst_id='"+instid+"' and OCCUPATIONID='"+occupationid+"' ";
		enctrace("occupationdescqry :" + occupationdescqry);
		try{
			if( occupationid == null) { return "--"; }
			occupationdesc = (String)jdbctemplate.queryForObject(occupationdescqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return occupationdesc ;
	}
	

	public String getOccupationWorkForDesc(String instid, String occupationworkfor, JdbcTemplate jdbctemplate ) throws Exception {
		String occupationdesc = null;
		String occupationdescqry = "SELECT WORKFORDESC FROM IFC_OCCUPATIONWORKFOR WHERE inst_id='"+instid+"' and WORKFORID='"+occupationworkfor+"' ";
		enctrace("occupationdescqry :" + occupationdescqry);
		try{
			if( occupationworkfor == null) { return "--"; }
			occupationdesc = (String)jdbctemplate.queryForObject(occupationdescqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return occupationdesc ;
	}
	
	public String getAccountTypeDesc(String instid, String acctypeid, JdbcTemplate jdbctemplate ) throws Exception {
		String acctypeiddesc = null;
		String acctypeiddescqry = "SELECT ACCTTYPEDESC FROM IFC_ACCTTYPE WHERE inst_id='"+instid+"' and ACCTTYPEID='"+acctypeid+"' ";
		enctrace("occupationdescqry :" + acctypeiddescqry);
		try{
			if( acctypeid == null) { return "--"; }
			acctypeiddesc = (String)jdbctemplate.queryForObject(acctypeiddescqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return acctypeiddesc ;
	}
	
	public String getPayDetailsDesc(String instid, String paydetail, JdbcTemplate jdbctemplate ) throws Exception {
		String paydetaildesc = null;
		String paydetaildescqry = "SELECT PAYTYPEVALUE FROM IFC_PAYTYPE  WHERE inst_id='"+instid+"' and PAYTYPEID='"+paydetail+"' ";
		enctrace("paydetaildescqry :" + paydetaildescqry);
		try{
			if( paydetail == null) { return "--"; }
			paydetaildesc = (String)jdbctemplate.queryForObject(paydetaildescqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return paydetaildesc ;
	}
	
	
	public String getExistCreditCardDesc(String existstatus ) {
		if( existstatus == null ){
			existstatus = " -- ";
		}else if( existstatus.equals("$Y") ){
			existstatus= "Yes";
		}else if( existstatus.equals("$N") ){
			existstatus= "No";
		} 
		return existstatus;
	}
	
	public String getAccountWithPrimBankDesc(String acctwithstatus ) {
		if( acctwithstatus == null ){
			acctwithstatus = " -- ";
		}else if( acctwithstatus.equals("$Y") ){
			acctwithstatus= "Yes";
		}else if( acctwithstatus.equals("$N") ){
			acctwithstatus= "No";
		} 
		return acctwithstatus;
	}
	

	public int updateCustomerAuthStatus(String instid, String applicationid, String tablename, String authstatus, String authby, String reason, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updateauthqry = "UPDATE  "+tablename+" SET AUTH_STATUS='"+authstatus+"', AUTH_BY='"+authby+"', AUTH_DATE=SYSDATE, REMARKS='"+reason+"' WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"' ";
		enctrace("updateauthqry :"+  updateauthqry);
		x = jdbctemplate.update(updateauthqry);
		return x;
	}
	
	public int updateCustomerMakeStatus(String instid, String applicationid, String tablename, String authstatus, String authby, String reason, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updateauthqry = "UPDATE  "+tablename+" SET AUTH_STATUS='"+authstatus+"', ADDED_BY='"+authby+"',  ADDED_DATE=SYSDATE,  AUTH_BY='', AUTH_DATE='', REMARKS='"+reason+"' WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"' ";
		enctrace("updateauthqry :"+  updateauthqry);
		x = jdbctemplate.update(updateauthqry);
		return x;
	}
	
	public int deleteCustomerApplicationDetails(String instid, String applicationid, String tablename, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "DELETE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerPersonalDetailToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMERINFO   SELECT * FROM IFC_CUSTOMERINFO_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerBusinessToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMERBUSINESS   SELECT * FROM IFC_CUSTOMERBUSINESS_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerReferenceToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMER_REF   SELECT * FROM IFC_CUSTOMER_REF_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerAddressToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMERADDRESS   SELECT * FROM IFC_CUSTOMERADDRESS_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerDocuemntToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMERDOC   SELECT * FROM IFC_CUSTOMERDOC_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerIncomeDetailsToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMERINCOME   SELECT * FROM IFC_CUSTOMERINCOME_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerExistCardDetailsToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMEREXISTCRCARD   SELECT * FROM IFC_CUSTOMEREXISTCRCARD_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerSupplimentToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMERSUPPLIMENT   SELECT * FROM IFC_CUSTOMERSUPPLIMENT_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	public int moveCustomerStandingToMain(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveqry = "INSERT INTO IFC_CUSTOMERSTANDINGINFO   SELECT * FROM IFC_CUSTOMERSTANDINGINFO_TEMP WHERE INST_ID='"+instid+"' AND APPLICATIONID='"+applicationid+"'";
		enctrace("moveqry :"+  moveqry );
		x = jdbctemplate.update(moveqry);
		return x;
	}
	
	

	public int updateRegisterationLevel(String instid, String applicationid, String reglevel, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1, result =-1;
		String reglevelqry = "";
		
		String existreglevel = this.getCustomerRegLevel(instid, applicationid, jdbctemplate);
		if( existreglevel != null ){
			if(  Integer.parseInt(existreglevel) > Integer.parseInt(reglevel) ) {
				return 2;
			}
		}  
		
		String existcustidqry = "SELECT COUNT(*) FROM CUSTOMER_APP_REGLEVEL  WHERE inst_id='"+instid+"' AND applicationid='"+applicationid+"'";
		result = jdbctemplate.queryForInt(existcustidqry);
		if( result == 0 ){
			reglevelqry = "INSERT INTO CUSTOMER_APP_REGLEVEL(inst_id, APPLICATIONID, reglevel) VALUES ('"+instid+"', '"+applicationid+"', '"+reglevel+"')";
			enctrace("Inserting Registeration level :"+  reglevelqry );			
		}else{
			reglevelqry = "UPDATE CUSTOMER_APP_REGLEVEL SET reglevel='"+reglevel+"' WHERE inst_id='"+instid+"' AND applicationid='"+applicationid+"'";
			enctrace("Updating Registeration level :"+  reglevelqry );
		} 
		x = jdbctemplate.update(reglevelqry);
		return x;
	}
	
	public String getCustomerRegLevel(String instid, String applicationid, JdbcTemplate jdbctemplate ) throws Exception {
		String reglevel = null;
		String reglvelqry= "SELECT reglevel FROM CUSTOMER_APP_REGLEVEL WHERE inst_id='"+instid+"' and applicationid='"+applicationid+"' ";
		enctrace("reglvelqry :" + reglvelqry);
		try{
			if( applicationid == null) { trace("applicationid id got as null...so returning 1");return "0"; }
			reglevel = (String)jdbctemplate.queryForObject(reglvelqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return reglevel;
	}
	
	public String getEmbossingName(String instid, String customerid, JdbcTemplate jdbctemplate ) throws Exception{
		String embosename = null;
		String embosenameqry= "SELECT EMBNAME FROM IFC_CUSTOMERINFO WHERE inst_id='"+instid+"' and CUSTOMERID='"+customerid+"' ";
		enctrace("embosenameqry :" + embosenameqry);
		try{ 
			embosename = (String)jdbctemplate.queryForObject(embosenameqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return embosename;
	}
	
	public String getEncodingName(String instid, String customerid, JdbcTemplate jdbctemplate ) throws Exception{
		String encname = null;
		String encnameqry= "SELECT ENCNAME FROM IFC_CUSTOMERINFO WHERE inst_id='"+instid+"' and CUSTOMERID='"+customerid+"' ";
		enctrace("encnameqry :" + encnameqry);
		try{ 
			encname = (String)jdbctemplate.queryForObject(encnameqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return encname;
	}
	
}
