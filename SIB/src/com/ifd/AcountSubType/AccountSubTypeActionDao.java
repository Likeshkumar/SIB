package com.ifd.AcountSubType;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class AccountSubTypeActionDao extends BaseAction {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	 

	public List getAcctTypeList( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND AUTH_CODE='1'";
		enctrace("acctypelistqry::::"+acctypelistqry);
		acctypelist = jdbctemplate.queryForList(acctypelistqry);
		return acctypelist;
	}
	
	

	
	
	
		
	public List instList( JdbcTemplate jdbcTemplate ){
		String allInst="select INST_ID, INST_NAME  from INSTITUTION where status =1 order by PREFERENCE"; 
		enctrace( " allInst "+allInst);
		List instlistdata =jdbcTemplate.queryForList(allInst); 
		return instlistdata;
	}
		
	/*		
	public String getCardTypeDesc(String instid, String cardtypeid, JdbcTemplate jdbctemplate){
		String cardtypename = null;
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID = '"+instid+"'  and  CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(" cardtypedesc : " + cardtypedesc);
		try{
			cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, String.class);
		}catch(EmptyResultDataAccessException e){}
		return cardtypename;}
	

	public int checkAccountSUBTypeExist( String accounttypeid, String instid,JdbcTemplate jdbctemplate ){
		String cardtypeqry = "SELECT COUNT(*) FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"' AND ACCTSUBTYPEID='"+accounttypeid+"'";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);
		return x;
	}
	
	public int checkAccountTypeNameExist( String accounttypeename, String instid,JdbcTemplate jdbctemplate ){
		if( accounttypeename == null ){ return -1;}
		String cardtypeqry = "SELECT COUNT(*) FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"' AND TRIM(UPPER(ACCTSUBTYPEDESC))=TRIM(UPPER('"+accounttypeename+"'))";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);
		return x;
	}*/
	
	
	
	
public String getCardTypeDesc(String instid, String cardtypeid, JdbcTemplate jdbctemplate){
	String cardtypename = null;
	
	/*String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID = '"+instid+"'  and  CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
	enctrace(" cardtypedesc : " + cardtypedesc);
	try{
		cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, String.class);*/
	
	//by gowtham-200819
	String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID =?  and  CARD_TYPE_ID=? and rownum<=?";
	enctrace(" cardtypedesc : " + cardtypedesc);
	try{
		cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, new Object[]{instid,cardtypeid,"1"},String.class);
		
		
	}catch(EmptyResultDataAccessException e){}
	return cardtypename;
}

public int checkAccountSUBTypeExist( String accounttypeid, String instid,JdbcTemplate jdbctemplate ){
	
	/*String cardtypeqry = "SELECT COUNT(*) FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"' AND ACCTSUBTYPEID='"+accounttypeid+"'";
	enctrace("cardtypeqry__" + cardtypeqry );
	int x = jdbctemplate.queryForInt(cardtypeqry);*/
	
	//by gowtham-200819
	String cardtypeqry = "SELECT COUNT(*) FROM ACCTSUBTYPE WHERE INST_ID=? AND ACCTSUBTYPEID=?";
	enctrace("cardtypeqry__" + cardtypeqry );
	int x = jdbctemplate.queryForInt(cardtypeqry,new Object[]{instid,accounttypeid});
	
	return x;
}

public int checkAccountTypeNameExist( String accounttypeename, String instid,JdbcTemplate jdbctemplate ){
	if( accounttypeename == null ){ return -1;}
	
	/*String cardtypeqry = "SELECT COUNT(*) FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"' AND TRIM(UPPER(ACCTSUBTYPEDESC))=TRIM(UPPER('"+accounttypeename+"'))";
	enctrace("cardtypeqry__" + cardtypeqry );
	int x = jdbctemplate.queryForInt(cardtypeqry);*/
	
	//by gowtham=200819
	String cardtypeqry = "SELECT COUNT(*) FROM ACCTSUBTYPE WHERE INST_ID=? AND TRIM(UPPER(ACCTSUBTYPEDESC))=TRIM(UPPER(?))";
	enctrace("cardtypeqry__" + cardtypeqry );
	int x = jdbctemplate.queryForInt(cardtypeqry,new Object[]{instid,accounttypeename});
	
	return x;
}

	
	
	
	
	
	
	public int sequenceAccountSubtype(String inst_id,JdbcTemplate jdbctemplate) {
		int x = -1;
		String AccoutSubtypeseq = "SELECT ACCT_SUB_SEQ_NO FROM SEQUENCE_MASTER WHERE INST_ID='"+inst_id+"'";
		x = jdbctemplate.queryForInt(AccoutSubtypeseq);
		return x;
	}
	   
	public synchronized String updateAccountSubtypeid(String instid)
	{
		return "update SEQUENCE_MASTER set ACCT_SUB_SEQ_NO=ACCT_SUB_SEQ_NO+1 where INST_ID='"+instid+"'";
	}
	 
	
	
	
	
	/*
	
	public List getsubaccounttypeList(String instid, JdbcTemplate jdbctemplate)
	{
		List accountsubtypelist = null;
		String accountsubtypeqry ="SELECT ACCTSUBTYPEID,ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"'";
		enctrace( "accounttypeqry--" + accountsubtypeqry);
		accountsubtypelist = jdbctemplate.queryForList(accountsubtypeqry);
		return accountsubtypelist;
	}
	
	public List getAccountsubtypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List accountsubtypelist = null;
		String accountsubtypeqry ="SELECT ACCTSUBTYPEID,ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"'";
		enctrace( "accounttypeqry--" + accountsubtypeqry);
		accountsubtypelist = jdbctemplate.queryForList(accountsubtypeqry);
		return accountsubtypelist;
		
	}
	
	public List geteditAccountSubTypeList(String instid,JdbcTemplate jdbctemplate,String acctsubtypeid)throws Exception
	{
		List editsubaccttypelist = null;
		String qryeditsubacctttypeqry ="select ACCTTYPEID,ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND ACCTSUBTYPEID='"+acctsubtypeid+"'";
		enctrace("geteditAcctSubTypeListBy : " + qryeditsubacctttypeqry );
		editsubaccttypelist = jdbctemplate.queryForList(qryeditsubacctttypeqry);
		return editsubaccttypelist;
		
	}
	
	public String updatesubaccounttypedetails(String instid,String accounttpeid,String acctsubtypeid,String accountsubtypedesc,String mkchkrstatus,String auth_code,String usercode, JdbcTemplate jdbctemplate)
	{
		String editsave_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS='"+mkchkrstatus+"', AUTH_CODE='"+auth_code+"',ADDED_DATE=sysdate,ADDEDBY='"+usercode+"',ACCTSUBTYPEDESC='"+accountsubtypedesc+"' WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accounttpeid+"' AND ACCTSUBTYPEID='"+acctsubtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+editsave_qury);
		return editsave_qury;
	}
	
	public List getSubAccountTypeListByCardID(String instid,JdbcTemplate jdbctemplate,String acctsubtypeid ) throws Exception{
		List subaccttypelist = null;
		String qrysubacctttypeqry ="select ACCTTYPEID,ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND ACCTSUBTYPEID='"+acctsubtypeid+"'";
		enctrace("getAcctSubTypeListByCardIDForauth : " + qrysubacctttypeqry );
		subaccttypelist = jdbctemplate.queryForList(qrysubacctttypeqry);
		return subaccttypelist;
	}
	
	public String getSaveAccountSUBTypeqry(String instid,String accounttpeid,String acctsubtypeid,String accountsubtypedesc,String usercode,String auth_code,String mkchkrstatus) 
	{                                             //instid,           accountid,       acctsubtypeid,       accountsubtypedesc,usercode, auth_code,mkchkrstatus
		String saveqry = null;
		saveqry ="INSERT INTO ACCTSUBTYPE (INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE , MKCK_STATUS)";
		saveqry +="VALUES('"+instid+"','"+accounttpeid+"','"+acctsubtypeid+"','"+accountsubtypedesc+"','"+usercode+"',sysdate,'','','"+auth_code+"' , '"+mkchkrstatus+"')";
		enctrace( " saveqry "+saveqry);
		return saveqry;
	}   
	
	public List getAllAcctSubTypeList(String instid,JdbcTemplate jdbctemplate ) throws Exception{
		List acctsubtype = null;
		String qrysubtypeeqry ="select INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE, MKCK_STATUS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("qrysubtypeeqry : " + qrysubtypeeqry );
		acctsubtype = jdbctemplate.queryForList(qrysubtypeeqry);
		return acctsubtype;
	}
	public List getCardTypeListByCardID(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from CARD_TYPE where INST_ID = '"+instid+"' AND AUTH_CODE='1' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	public List getAcctSubTypeListByCardIDForauth(String instid,JdbcTemplate jdbctemplate,String acctsubtypeid ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE, MKCK_STATUS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND ACCTSUBTYPEID='"+acctsubtypeid+"'";
		enctrace("getAcctSubTypeListByCardIDForauth : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	public String updateAuthAcctSubType(String authstatus,String userid,String instid,String acsubtypeid) {
		String update_authdeauth_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ACCTSUBTYPEID='"+acsubtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public String updateDeAuthAcctSubType(String authstatus,String userid,String remarks,String instid,String cardtypeid) {
		String update_authdeauth_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND ACCTSUBTYPEID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	
	public List getCardTypeListByMakerCardID(String instid,JdbcTemplate jdbctemplate) {
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from CARD_TYPE where INST_ID = '"+instid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	*/
	
	
	
	
	
	//-------------------------------------------------------------------------------------

	public List getsubaccounttypeList(String instid, JdbcTemplate jdbctemplate)
	{
		List accountsubtypelist = null;
		
		/*String accountsubtypeqry ="SELECT ACCTSUBTYPEID,ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"'";
		enctrace( "accounttypeqry--" + accountsubtypeqry);
		accountsubtypelist = jdbctemplate.queryForList(accountsubtypeqry);*/

		//by gowtham-200819
		String accountsubtypeqry ="SELECT ACCTSUBTYPEID,ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE INST_ID=?";
		enctrace( "accounttypeqry--" + accountsubtypeqry);
		accountsubtypelist = jdbctemplate.queryForList(accountsubtypeqry,new Object[]{instid});
		
		return accountsubtypelist;
	}
	
	public List getAccountsubtypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List accountsubtypelist = null;
		
		/*String accountsubtypeqry ="SELECT ACCTSUBTYPEID,ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE INST_ID='"+instid+"'";
		enctrace( "accounttypeqry--" + accountsubtypeqry);
		accountsubtypelist = jdbctemplate.queryForList(accountsubtypeqry);*/
		
		//by gowtham-200819
		String accountsubtypeqry ="SELECT ACCTSUBTYPEID,ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE INST_ID=?";
		enctrace( "accounttypeqry--" + accountsubtypeqry);
		accountsubtypelist = jdbctemplate.queryForList(accountsubtypeqry,new Object[]{instid});

		return accountsubtypelist;
		
	}
	
	public List geteditAccountSubTypeList(String instid,JdbcTemplate jdbctemplate,String acctsubtypeid)throws Exception
	{
		List editsubaccttypelist = null;
		
		/*String qryeditsubacctttypeqry ="select ACCTTYPEID,ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND ACCTSUBTYPEID='"+acctsubtypeid+"'";
		enctrace("geteditAcctSubTypeListBy : " + qryeditsubacctttypeqry );
		editsubaccttypelist = jdbctemplate.queryForList(qryeditsubacctttypeqry);*/
		
		//by gowtham-200819
		String qryeditsubacctttypeqry ="select ACCTTYPEID,ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from ACCTSUBTYPE where INST_ID = ? AND ACCTSUBTYPEID=?";
		enctrace("geteditAcctSubTypeListBy : " + qryeditsubacctttypeqry );
		editsubaccttypelist = jdbctemplate.queryForList(qryeditsubacctttypeqry,new Object[]{instid,acctsubtypeid});
		
		return editsubaccttypelist;
		
	}
	
	public String updatesubaccounttypedetails(String instid,String accounttpeid,String acctsubtypeid,String accountsubtypedesc,String mkchkrstatus,String auth_code,String usercode, JdbcTemplate jdbctemplate)
	{
		String editsave_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS=?,?,ADDED_DATE=?,ADDEDBY=?,ACCTSUBTYPEDESC=? WHERE INST_ID=? AND ACCTTYPEID=? AND ACCTSUBTYPEID=?";
		enctrace(" update_authdeauth_qury  === > "+editsave_qury);
		return editsave_qury;
	}
	
	public List getSubAccountTypeListByCardID(String instid,JdbcTemplate jdbctemplate,String acctsubtypeid ) throws Exception{
		List subaccttypelist = null;
		
		/*String qrysubacctttypeqry ="select ACCTTYPEID,ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND ACCTSUBTYPEID='"+acctsubtypeid+"'";
		enctrace("getAcctSubTypeListByCardIDForauth : " + qrysubacctttypeqry );
		subaccttypelist = jdbctemplate.queryForList(qrysubacctttypeqry);*/
		
		//by gowtham-200819
		String qrysubacctttypeqry ="select ACCTTYPEID,ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from ACCTSUBTYPE where INST_ID = ? AND ACCTSUBTYPEID=?";
		enctrace("getAcctSubTypeListByCardIDForauth : " + qrysubacctttypeqry );
		subaccttypelist = jdbctemplate.queryForList(qrysubacctttypeqry,new Object[]{instid,acctsubtypeid});
		
		return subaccttypelist;
	}
	
	public String getSaveAccountSUBTypeqry(String instid,String accounttpeid,String acctsubtypeid,String accountsubtypedesc,String usercode,String auth_code,String mkchkrstatus) 
	{                                             //instid,           accountid,       acctsubtypeid,       accountsubtypedesc,usercode, auth_code,mkchkrstatus
		String saveqry = null;
		
		saveqry ="INSERT INTO ACCTSUBTYPE (INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE , MKCK_STATUS)";
		saveqry +="VALUES(?,?,?,?,?,sysdate,?,?,? ,?)";
		
		enctrace( " saveqry "+saveqry);
		return saveqry;
	}   
	
	public List getAllAcctSubTypeList(String instid,JdbcTemplate jdbctemplate ) throws Exception{
		List acctsubtype = null;
		
		/*String qrysubtypeeqry ="select INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE, MKCK_STATUS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("qrysubtypeeqry : " + qrysubtypeeqry );
		acctsubtype = jdbctemplate.queryForList(qrysubtypeeqry);*/
		
		//by gowtham-200819
		String qrysubtypeeqry ="select INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE, MKCK_STATUS from ACCTSUBTYPE where INST_ID = ? AND AUTH_CODE=? AND MKCK_STATUS=?";
		enctrace("qrysubtypeeqry : " + qrysubtypeeqry );
		acctsubtype = jdbctemplate.queryForList(qrysubtypeeqry,new Object[]{instid,"0","M"});
		
		return acctsubtype;
	}
	public List getCardTypeListByCardID(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from CARD_TYPE where INST_ID = '"+instid+"' AND AUTH_CODE='1' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	public List getAcctSubTypeListByCardIDForauth(String instid,JdbcTemplate jdbctemplate,String acctsubtypeid ) throws Exception{
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE, MKCK_STATUS from ACCTSUBTYPE where INST_ID = '"+instid+"' AND ACCTSUBTYPEID='"+acctsubtypeid+"'";
		enctrace("getAcctSubTypeListByCardIDForauth : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/
		
		//by gowtham-200819
		String qrysubfeecodeqry ="select INST_ID, ACCTTYPEID, ACCTSUBTYPEID, ACCTSUBTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE, MKCK_STATUS from ACCTSUBTYPE where INST_ID = ? AND ACCTSUBTYPEID=?";
		enctrace("getAcctSubTypeListByCardIDForauth : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid,acctsubtypeid});
		
		return subfeelist;
	}
	public String updateAuthAcctSubType(String authstatus,String userid,String instid,String acsubtypeid) {
		
		String update_authdeauth_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ACCTSUBTYPEID='"+acsubtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		/*
		//by gowtham-200819
		String update_authdeauth_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=? WHERE INST_ID=? AND ACCTSUBTYPEID=?";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);*/
		
		return update_authdeauth_qury;
	}
	public String updateDeAuthAcctSubType(String authstatus,String userid,String remarks,String instid,String cardtypeid) {
		
		String update_authdeauth_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND ACCTSUBTYPEID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		
	/*	//by gowtham-200819
		String update_authdeauth_qury = "UPDATE ACCTSUBTYPE SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=?,REMARKS=? WHERE INST_ID=? AND ACCTSUBTYPEID=?";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);*/

		return update_authdeauth_qury;
	}
	
	public List getCardTypeListByMakerCardID(String instid,JdbcTemplate jdbctemplate) {
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from CARD_TYPE where INST_ID = '"+instid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/
		
		//by gowtham-200819
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from CARD_TYPE where INST_ID = ?";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid});
		
		return subfeelist;
	}
	
	
	
}
