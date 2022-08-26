package com.ifd.AcountType;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

@Transactional
public class AccountTypeActionDao extends BaseAction {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	 

	
	public List instList( JdbcTemplate jdbcTemplate ){
		String allInst="select INST_ID, INST_NAME  from INSTITUTION where status =1 order by PREFERENCE"; 
		enctrace( " allInst "+allInst);
		List instlistdata =jdbcTemplate.queryForList(allInst); 
		return instlistdata;
	}

/*	public String fchCardtypeDesc(String cardtypeid){
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM IFP_GLOBAL_CARD_TYPE WHERE CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(cardtypedesc + " --- --- cardtypedesc ---- ");
		return cardtypedesc;
	}
	
	
	public String getAccountTypeDesc(String instid, String accttypeid, JdbcTemplate jdbctemplate){
		String accounttypename = null;
		String accounttypedesc = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID = '"+instid+"'  and  ACCTTYPEID='"+accttypeid+"' and rownum<=1";
		enctrace(" accounttypedesc : " + accounttypedesc);
		try{
			accounttypename  = (String)jdbctemplate.queryForObject(accounttypedesc, String.class);
		}catch(EmptyResultDataAccessException e){}
		return accounttypename;
	}
	
	public String getCardTypeDesc(String instid, String cardtypeid, JdbcTemplate jdbctemplate){
		String cardtypename = null;
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID = '"+instid+"'  and  CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(" cardtypedesc : " + cardtypedesc);
		try{
			cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, String.class);
		}catch(EmptyResultDataAccessException e){}
		return cardtypename;
	}
	
	public int checkAccountTypeExist( String accounttypeid, String instid,JdbcTemplate jdbctemplate ){
		String cardtypeqry = "SELECT COUNT(*) FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accounttypeid+"'";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);
		return x;
	}
	
	public int checkAccountTypeNameExist( String accounttypeename, String instid,JdbcTemplate jdbctemplate ){
		if( accounttypeename == null ){ return -1;}
		String cardtypeqry = "SELECT COUNT(*) FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND TRIM(UPPER(ACCTTYPEDESC))=TRIM(UPPER('"+accounttypeename+"'))";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);
		return x;
	}
	 
	
	public String getSaveAccountTypeqry(String instid,String accounttpeidnew,String accounttypedesc,String usercode,String auth_code,String mkchkrstatus) 
	{
		String saveqry = null;
		saveqry ="INSERT INTO ACCTTYPE (INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE,MKCK_STATUS)";
		saveqry +="VALUES('"+instid+"','"+accounttpeidnew+"','"+accounttypedesc+"','"+usercode+"',sysdate,'','','"+auth_code+"','"+mkchkrstatus+"')";
		enctrace( " saveqry "+saveqry);
		return saveqry;
	}     
	public List getAccounttypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List accounttypelist = null;
		String accounttypeqry ="SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "accounttypeqry--" + accounttypeqry);
		accounttypelist = jdbctemplate.queryForList(accounttypeqry);
		return accounttypelist;
	}
	public List getAllAccountTypeList(String instid,JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE from ACCTTYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("authListAccountType : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	
	public List getAccountTypeListByAcctID(String instid,JdbcTemplate jdbctemplate,String accttypeid ) throws Exception{
		List accounttypelist = null;
		String qrygetaccounttypelist ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE from ACCTTYPE where INST_ID = '"+instid+"'  AND ACCTTYPEID='"+accttypeid+"'";
		enctrace("resaccountypelist : " + qrygetaccounttypelist );
		accounttypelist = jdbctemplate.queryForList(qrygetaccounttypelist);
		return accounttypelist;
		
	}
	
	public List getAccountTypeList(String instid,JdbcTemplate jdbctemplate,String accttypeid ) throws Exception{
		List accounttypelist = null;
		String qrygetaccounttypelist ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC from ACCTTYPE where INST_ID = '"+instid+"'  AND ACCTTYPEID='"+accttypeid+"'";
		enctrace("resaccountypelist : " + qrygetaccounttypelist );
		accounttypelist = jdbctemplate.queryForList(qrygetaccounttypelist);
		return accounttypelist;
		
	}
	
	public List getAcctTYpeListByAcctID(String instid,JdbcTemplate jdbctemplate,String accttypeid ) throws Exception{
		List actypelist = null;
		String qryactypeeqry ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE from ACCTTYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND ACCTTYPEID='"+accttypeid+"'";
		enctrace("getAcctTYpeListByAcctID : " + qryactypeeqry );
		actypelist = jdbctemplate.queryForList(qryactypeeqry);
		return actypelist;
	}
	public List getCardTypeListByCardIDForauth(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID from IFP_GLOBAL_CARD_TYPE where INST_ID = '"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	
	
	public String updateaccounttypedetails(String instid,String accttypeid,String accounttypename,String mkchkrstatus,String auth_code,String usercode,JdbcTemplate jdbctemplate)
	{
		String updateqry=null;
		updateqry="UPDATE ACCTTYPE SET ACCTTYPEDESC='"+accounttypename+"',MKCK_STATUS='"+mkchkrstatus+"', AUTH_CODE='"+auth_code+"',ADDEDBY='"+usercode+"',ADDED_DATE=sysdate WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accttypeid+"'";
		enctrace(" updateqry card type details  === > "+updateqry);		
		return updateqry;
	}
	
	public String updateAuthAcctType(String authstatus,String userid,String instid,String accttypeid) {
		String update_authdeauth_qury = "UPDATE ACCTTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accttypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public String updateDeAuthAcctType(String authstatus,String userid,String remarks,String instid,String accttypeid) {
		String update_authdeauth_qury = "UPDATE ACCTTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accttypeid+"'";
		enctrace(" updateDeAuthAcctType  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  IFP_GLOBAL_CARD_TYPE  where INST_ID='"+instid+"' and AUTH_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public List getCardTypeListByMakerCardID(String instid,JdbcTemplate jdbctemplate) {
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from IFP_GLOBAL_CARD_TYPE where INST_ID = '"+instid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}*/
	
	
	
	////BY GOWTHAM-300819
	
	public String fchCardtypeDesc(String cardtypeid){
		
		/*String cardtypedesc = "SELECT CARD_TYPE_DESC FROM IFP_GLOBAL_CARD_TYPE WHERE CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(cardtypedesc + " --- --- cardtypedesc ---- ");*/
		
		//by gowtham-200819
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM IFP_GLOBAL_CARD_TYPE WHERE CARD_TYPE_ID=?and rownum<=?";
		enctrace(cardtypedesc + " --- --- cardtypedesc ---- ");
		
		return cardtypedesc;
	}
	
	
	public String getAccountTypeDesc(String instid, String accttypeid, JdbcTemplate jdbctemplate){
		String accounttypename = null;
		
		
		//by gowtham-200819
		/*String accounttypedesc = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID = '"+instid+"'  and  ACCTTYPEID='"+accttypeid+"' and rownum<=1";
		enctrace(" accounttypedesc : " + accounttypedesc);*/
		
		//by gowtham-200819
		String accounttypedesc = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID = ? and  ACCTTYPEID=?and rownum<=?";
		enctrace(" accounttypedesc : " + accounttypedesc);
		
		try{
			
			/*accounttypename  = (String)jdbctemplate.queryForObject(accounttypedesc, String.class);*/
			accounttypename  = (String)jdbctemplate.queryForObject(accounttypedesc,new Object[]{instid,accttypeid,"1"},String.class);
			
		}catch(EmptyResultDataAccessException e){}
		return accounttypename;
	}
	
	public String getCardTypeDesc(String instid, String cardtypeid, JdbcTemplate jdbctemplate){
		String cardtypename = null;
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID = '"+instid+"'  and  CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(" cardtypedesc : " + cardtypedesc);
		try{
			cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, String.class);
		}catch(EmptyResultDataAccessException e){}
		return cardtypename;
	}
	
	public int checkAccountTypeExist( String accounttypeid, String instid,JdbcTemplate jdbctemplate ){
		
		/*String cardtypeqry = "SELECT COUNT(*) FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accounttypeid+"'";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);*/

		//BY GOWTHAM-190819
		String cardtypeqry = "SELECT COUNT(*) FROM ACCTTYPE WHERE INST_ID=? AND ACCTTYPEID=?";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry,new Object[]{instid,accounttypeid});
		return x;
	}
	
	public int checkAccountTypeNameExist( String accounttypeename, String instid,JdbcTemplate jdbctemplate ){
		if( accounttypeename == null ){ return -1;}
		
		/*String cardtypeqry = "SELECT COUNT(*) FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND TRIM(UPPER(ACCTTYPEDESC))=TRIM(UPPER('"+accounttypeename+"'))";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);*/
		
		//by gowtham-190819
		String cardtypeqry = "SELECT COUNT(*) FROM ACCTTYPE WHERE INST_ID=? AND TRIM(UPPER(ACCTTYPEDESC))=TRIM(UPPER(?))";
		enctrace("cardtypeqry__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry,new Object[]{instid,accounttypeename});
		
		return x;
	}
	 
	
	public String getSaveAccountTypeqry(String instid,String accounttpeidnew,String accounttypedesc,String usercode,String auth_code,String mkchkrstatus) 
	{
		String saveqry = null;
		
		/*saveqry ="INSERT INTO ACCTTYPE (INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE,MKCK_STATUS)";
		saveqry +="VALUES('"+instid+"','"+accounttpeidnew+"','"+accounttypedesc+"','"+usercode+"',sysdate,'','','"+auth_code+"','"+mkchkrstatus+"')";
		enctrace( " saveqry "+saveqry);*/
		
		//BY GOWTHAM20819
		saveqry ="INSERT INTO ACCTTYPE (INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE,MKCK_STATUS)";
		saveqry +="VALUES(?,?,?,?,?,?,?,?,?)";
		enctrace( " saveqry "+saveqry);
		
		return saveqry;
	}     
	public List getAccounttypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List accounttypelist = null;
		
		/*String accounttypeqry ="SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "accounttypeqry--" + accounttypeqry);
		accounttypelist = jdbctemplate.queryForList(accounttypeqry);*/
		
		//BY GOWTHAM-200819
		String accounttypeqry ="SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID=? and AUTH_CODE=?";
		enctrace( "accounttypeqry--" + accounttypeqry);
		accounttypelist = jdbctemplate.queryForList(accounttypeqry,new Object[]{instid,"1"});
		
		return accounttypelist;
	}
	public List getAllAccountTypeList(String instid,JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE from ACCTTYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("authListAccountType : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/

		//by gowtham-200819
		String qrysubfeecodeqry ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE, AUTH_CODE from ACCTTYPE where INST_ID = ? AND AUTH_CODE=? AND MKCK_STATUS=?";
		enctrace("authListAccountType : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid,"0","M"});
		return subfeelist;
	}
	
	public List getAccountTypeListByAcctID(String instid,JdbcTemplate jdbctemplate,String accttypeid ) throws Exception{
		List accounttypelist = null;
		
		/*String qrygetaccounttypelist ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE from ACCTTYPE where INST_ID = '"+instid+"'  AND ACCTTYPEID='"+accttypeid+"'";
		enctrace("resaccountypelist : " + qrygetaccounttypelist );
		accounttypelist = jdbctemplate.queryForList(qrygetaccounttypelist);*/
		
		//by gowtham-200819
		String qrygetaccounttypelist ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE from ACCTTYPE where INST_ID = ?  AND ACCTTYPEID=?";
		enctrace("resaccountypelist : " + qrygetaccounttypelist );
		accounttypelist = jdbctemplate.queryForList(qrygetaccounttypelist,new Object[]{instid,accttypeid});
		
		return accounttypelist;
		
	}
	
	public List getAccountTypeList(String instid,JdbcTemplate jdbctemplate,String accttypeid ) throws Exception{
		List accounttypelist = null;
		
		/*String qrygetaccounttypelist ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC from ACCTTYPE where INST_ID = '"+instid+"'  AND ACCTTYPEID='"+accttypeid+"'";
		enctrace("resaccountypelist : " + qrygetaccounttypelist );
		accounttypelist = jdbctemplate.queryForList(qrygetaccounttypelist);*/
		
		//by gowtham200819
		String qrygetaccounttypelist ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC from ACCTTYPE where INST_ID =?  AND ACCTTYPEID=?";
		enctrace("resaccountypelist : " + qrygetaccounttypelist );
		accounttypelist = jdbctemplate.queryForList(qrygetaccounttypelist,new Object[]{instid,accttypeid});
		
		return accounttypelist;
		
	}
	
	public List getAcctTYpeListByAcctID(String instid,JdbcTemplate jdbctemplate,String accttypeid ) throws Exception{
		List actypelist = null;
		
		/*String qryactypeeqry ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE from ACCTTYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND ACCTTYPEID='"+accttypeid+"'";
		enctrace("getAcctTYpeListByAcctID : " + qryactypeeqry );
		actypelist = jdbctemplate.queryForList(qryactypeeqry);*/
		
		//by gowtham200819
		String qryactypeeqry ="select INST_ID, ACCTTYPEID, ACCTTYPEDESC, ADDEDBY, ADDED_DATE, AUTHBY, AUTHDATE,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE from ACCTTYPE where INST_ID = ? AND AUTH_CODE=? AND ACCTTYPEID=?";
		enctrace("getAcctTYpeListByAcctID : " + qryactypeeqry );
		actypelist = jdbctemplate.queryForList(qryactypeeqry,new Object[]{instid,"0",accttypeid});
		
		return actypelist;
	}
	public List getCardTypeListByCardIDForauth(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID from IFP_GLOBAL_CARD_TYPE where INST_ID = '"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	
	
	public String updateaccounttypedetails(String instid,String accttypeid,String accounttypename,String mkchkrstatus,String auth_code,String usercode,JdbcTemplate jdbctemplate)
	{
		String updateqry=null;
		
		updateqry="UPDATE ACCTTYPE SET ACCTTYPEDESC=?, MKCK_STATUS=?, AUTH_CODE=?,ADDEDBY=?, ADDED_DATE=? WHERE INST_ID=? AND ACCTTYPEID=?";
		enctrace(" updateqry card type details  === > "+updateqry);
		
		return updateqry;
	}
	
	public String updateAuthAcctType(String authstatus,String userid,String instid,String accttypeid) {
		
		/*String update_authdeauth_qury = "UPDATE ACCTTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accttypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);*/
		
		//BY GOWTHAM-200819
		String update_authdeauth_qury = "UPDATE ACCTTYPE SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=? WHERE INST_ID=? AND ACCTTYPEID=?";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		
		return update_authdeauth_qury;
	}
	public String updateDeAuthAcctType(String authstatus,String userid,String remarks,String instid,String accttypeid) {
		
		/*String update_authdeauth_qury = "UPDATE ACCTTYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accttypeid+"'";
		enctrace(" updateDeAuthAcctType  === > "+update_authdeauth_qury);*/
		
		//by gowtham-200819
		String update_authdeauth_qury = "UPDATE ACCTTYPE SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=?,REMARKS=? WHERE INST_ID=? AND ACCTTYPEID=?";
		enctrace(" updateDeAuthAcctType  === > "+update_authdeauth_qury);
		
		return update_authdeauth_qury;
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  IFP_GLOBAL_CARD_TYPE  where INST_ID='"+instid+"' and AUTH_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public List getCardTypeListByMakerCardID(String instid,JdbcTemplate jdbctemplate) {
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from IFP_GLOBAL_CARD_TYPE where INST_ID = '"+instid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/
		
		//by gowtham200819
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from IFP_GLOBAL_CARD_TYPE where INST_ID = ?";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid});
		
		return subfeelist;
	}
	
	
	
	
	
	
	
}
