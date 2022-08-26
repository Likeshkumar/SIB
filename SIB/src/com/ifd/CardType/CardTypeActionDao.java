package com.ifd.CardType;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class CardTypeActionDao extends BaseAction {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	
	
	   

	/*
	public List instList( JdbcTemplate jdbcTemplate ){
		String allInst="select INST_ID, INST_NAME  from INSTITUTION where status =1 order by PREFERENCE"; 
		enctrace( " allInst "+allInst);
		List instlistdata =jdbcTemplate.queryForList(allInst); 
		return instlistdata;
	}

	public String fchCardtypeDesc(String cardtypeid){
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(cardtypedesc + " --- --- cardtypedesc ---- ");
		return cardtypedesc;
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
	
	public int checkCardTypeExist(String cardtypeid, String instid,JdbcTemplate jdbctemplate ){
		String cardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("checkCardTypeExist____:" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);
		return x;
	}
	    
	public int checkCardTypeNameExist( String cardtypename, String instid,JdbcTemplate jdbctemplate ){
		if( cardtypename == null ){ return -1;}
		String cardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE WHERE INST_ID='"+instid+"' AND TRIM(UPPER(CARD_TYPE_DESC))=TRIM(UPPER('"+cardtypename+"'))";
		enctrace("checkCardTypeNameExist____:" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);
		return x;
	}
	
	
	public List editcardtypeResult(String instid, String cardtypeid,JdbcTemplate jdbctemplate)
	{
		List editcardtypelist = null;
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and CARD_TYPE_ID='"+cardtypeid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		editcardtypelist = jdbctemplate.queryForList(cardtypeqry);
		return editcardtypelist;
		
	}
	public List getcardtypeDetailResult(String instid,JdbcTemplate jdbctemplate)
	{
		List editcardtypelist = null;
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		editcardtypelist = jdbctemplate.queryForList(cardtypeqry);
		return editcardtypelist;		
	}
	
	
	public String updatecardtypedetails(String instid,String cardtpeid, String cardtypedesc,String mkchkrstatus,String auth_code,String usercode, JdbcTemplate jdbctemplate)
	{
		String updateqry=null;
		updateqry="UPDATE CARD_TYPE SET CARD_TYPE_DESC='"+cardtypedesc+"',MKCK_STATUS='"+mkchkrstatus+"', AUTH_CODE='"+auth_code+"',MAKER_ID='"+usercode+"',MAKER_DATE=sysdate WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtpeid+"'";
		enctrace(" updateqry card type details  === > "+updateqry);		
		return updateqry;
	}
	
	public String getSaveqry(String instid,String cardtpeidnew,String cardtypedesc,String merchallowed,String prepaidallowed,String usercode,String auth_code,String mkchkrstatus) 
	{
		String saveqry = null;
		saveqry ="INSERT INTO CARD_TYPE (INST_ID, CARD_TYPE_ID, CARD_TYPE_DESC, MERCHANT_ALLOWED, PREPAID_ALLOWED,MAKER_DATE,MAKER_ID,MKCK_STATUS,AUTH_CODE)";
		saveqry +="VALUES('"+instid+"','"+cardtpeidnew+"','"+cardtypedesc+"','"+merchallowed+"','"+prepaidallowed+"',sysdate,'"+usercode+"','"+mkchkrstatus+"','"+auth_code+"')";
		enctrace( " getSaveqry____: "+saveqry);
		return saveqry;  
	}
	public List getCardtypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List cardtypelist = null;
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		cardtypelist = jdbctemplate.queryForList(cardtypeqry);
		return cardtypelist;
	}
	public List getAllCardTypeList(String instid,JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED from CARD_TYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	public List getCardTypeListByCardID(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from CARD_TYPE where INST_ID = '"+instid+"' AND AUTH_CODE='1' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	public List getCardTypeListByCardIDForauth(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID from CARD_TYPE where INST_ID = '"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	public int getcardreissucardvalid(String instid, String cardtypeid, JdbcTemplate jdbctemplate) {
		int x =0;
		String cardvalid = "SELECT COUNT(*) FROM BASENO WHERE INST_ID='"+instid+"' AND BASENO_CODE='"+cardtypeid+"' ";
		enctrace("CARD_TYPEqueryexist:" + cardvalid );
		x = jdbctemplate.queryForInt(cardvalid);
		return x;
	}
	public String updateAuthCardType(String authstatus,String userid,String instid,String cardtypeid) {
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public String updateDeAuthCardType(String authstatus,String userid,String remarks,String instid,String cardtypeid) {
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	
	public String updateauthcodecardtype(String authstatus,String userid,String remarks,String instid,String cardtypeid) {
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  CARD_TYPE  where INST_ID='"+instid+"' and AUTH_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public List getCardTypeListByMakerCardID(String instid,JdbcTemplate jdbctemplate) {
		
		System.out.println("getCardTypeTableName::::CARD_TYPE");  
		List subfeelist = null;
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from CARD_TYPE where INST_ID = '"+instid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);
		return subfeelist;
	}
	
	
	
	public List gettingBinList(String instid, JdbcTemplate jdbctemplate) {
		List res = null;
		try {
			String qry = "SELECT BIN,BASELEN FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND ATTACH_PRODTYPE_CARDTYPE='C'";
			enctrace("Query for binlist ::: "+qry);
			res = jdbctemplate.queryForList(qry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}*/
	
	
	
	
	
	
	
	public List instList( JdbcTemplate jdbcTemplate ){
		String allInst="select INST_ID, INST_NAME  from INSTITUTION where status =1 order by PREFERENCE"; 
		enctrace( " allInst "+allInst);
		List instlistdata =jdbcTemplate.queryForList(allInst); 
		return instlistdata;
	}

	public String fchCardtypeDesc(String cardtypeid){
		
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(cardtypedesc + " --- --- cardtypedesc ---- ");
		
		/*//BY GOWTHAM-190819
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE CARD_TYPE_ID=? and rownum<=?";
		enctrace(cardtypedesc + " --- --- cardtypedesc ---- ");*/
		
		return cardtypedesc;
	}
	
	
	public String getCardTypeDesc(String instid, String cardtypeid, JdbcTemplate jdbctemplate){
		String cardtypename = null;
		
		/*String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID = '"+instid+"'  and  CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(" cardtypedesc : " + cardtypedesc);
		try{
			cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, String.class);
		}catch(EmptyResultDataAccessException e){}*/
		
		//by gowtham-190819
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID =? and CARD_TYPE_ID=? and rownum<=?";
		enctrace(" cardtypedesc : " + cardtypedesc);
		try{
			cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, new Object[]{instid,cardtypeid,"1"},String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return cardtypename;
	}
	
	public int checkCardTypeExist(String cardtypeid, String instid,JdbcTemplate jdbctemplate ){
		
		/*String cardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("checkCardTypeExist____:" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);*/
		
		//by gowtham-190819
		String cardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE WHERE INST_ID=? AND CARD_TYPE_ID=?";
		enctrace("checkCardTypeExist____:" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry,new Object[]{instid,cardtypeid});
		
		return x;
	}
	    
	public int checkCardTypeNameExist( String cardtypename, String instid,JdbcTemplate jdbctemplate ){
		if( cardtypename == null ){ return -1;}
		
		/*String cardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE WHERE INST_ID='"+instid+"' AND TRIM(UPPER(CARD_TYPE_DESC))=TRIM(UPPER('"+cardtypename+"'))";
		enctrace("checkCardTypeNameExist____:" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);*/

		//by gowtham-190819
		String cardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE WHERE INST_ID=? AND TRIM(UPPER(CARD_TYPE_DESC))=TRIM(UPPER(?))";
		enctrace("checkCardTypeNameExist____:" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry,new Object[]{instid,cardtypename});
		
		return x;
	}
	
	
	private Object TRIM(Object upper) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object UPPER(String cardtypename) {
		// TODO Auto-generated method stub
		return null;
	}

	public List editcardtypeResult(String instid, String cardtypeid,JdbcTemplate jdbctemplate)
	{
		List editcardtypelist = null;
		
		/*String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and CARD_TYPE_ID='"+cardtypeid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		editcardtypelist = jdbctemplate.queryForList(cardtypeqry);*/
		
		
		//by gowtham-190819
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID=?and CARD_TYPE_ID=? and AUTH_CODE=?";
		enctrace( "cardtypeqry--" + cardtypeqry);
		editcardtypelist = jdbctemplate.queryForList(cardtypeqry,new Object[]{instid,cardtypeid,"1"});
			
		return editcardtypelist;
		
	}
	public List getcardtypeDetailResult(String instid,JdbcTemplate jdbctemplate)
	{
		List editcardtypelist = null;
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		editcardtypelist = jdbctemplate.queryForList(cardtypeqry);
		return editcardtypelist;		
	}
	
	
	public String updatecardtypedetails(String instid,String cardtpeid, String cardtypedesc,String mkchkrstatus,String auth_code,String usercode, JdbcTemplate jdbctemplate)
	{
		String updateqry=null;
		
		//by gowtham-190819
		//updateqry="UPDATE CARD_TYPE SET CARD_TYPE_DESC='"+cardtypedesc+"',MKCK_STATUS='"+mkchkrstatus+"', AUTH_CODE='"+auth_code+"',MAKER_ID='"+usercode+"',MAKER_DATE=sysdate WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtpeid+"'";
		
		updateqry="UPDATE CARD_TYPE SET CARD_TYPE_DESC=?,MKCK_STATUS=?, AUTH_CODE=?,MAKER_ID=?,MAKER_DATE=? WHERE INST_ID=? AND CARD_TYPE_ID=?";
		enctrace(" updateqry card type details  === > "+updateqry);	
		
		return updateqry;
	}
	
	public String getSaveqry(String instid,String cardtpeidnew,String cardtypedesc,String merchallowed,String prepaidallowed,String usercode,String auth_code,String mkchkrstatus) 
	{
		String saveqry = null;
		
		saveqry ="INSERT INTO CARD_TYPE (INST_ID, CARD_TYPE_ID, CARD_TYPE_DESC, MERCHANT_ALLOWED, PREPAID_ALLOWED,MAKER_DATE,MAKER_ID,MKCK_STATUS,AUTH_CODE)";
		saveqry +="VALUES('"+instid+"','"+cardtpeidnew+"','"+cardtypedesc+"','"+merchallowed+"','"+prepaidallowed+"',sysdate,'"+usercode+"','"+mkchkrstatus+"','"+auth_code+"')";
		enctrace( " getSaveqry____: "+saveqry);
		
	/*	//by gowtham-190819
		saveqry ="INSERT INTO CARD_TYPE (INST_ID, CARD_TYPE_ID, CARD_TYPE_DESC, MERCHANT_ALLOWED, PREPAID_ALLOWED,MAKER_DATE,MAKER_ID,MKCK_STATUS,AUTH_CODE)";
		saveqry +="VALUES(?,?,?,?,?,?,?,?,?)";
		enctrace( " getSaveqry____: "+saveqry);*/
		
		return saveqry;  
	}
	public List getCardtypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List cardtypelist = null;
		
		/*String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		cardtypelist = jdbctemplate.queryForList(cardtypeqry);*/
		
		//BY GOWTHAM-190819
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID=? and AUTH_CODE=?";
		enctrace( "cardtypeqry--" + cardtypeqry);
		cardtypelist = jdbctemplate.queryForList(cardtypeqry,new Object[]{instid,"1"});
		
		return cardtypelist;
	}
	public List getAllCardTypeList(String instid,JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED from CARD_TYPE where INST_ID = '"+instid+"' AND AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/
		
		//by gowtham-190819
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED from CARD_TYPE where INST_ID = ? AND AUTH_CODE=? AND MKCK_STATUS=?";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid,"0","M"});
		
		return subfeelist;
	}
	public List getCardTypeListByCardID(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from CARD_TYPE where INST_ID = '"+instid+"' AND AUTH_CODE='1' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/
		
		//by gowtham-190819
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from CARD_TYPE where INST_ID = ?AND AUTH_CODE=? AND CARD_TYPE_ID=?";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid,"1",cardtypeid});
		
		return subfeelist;
	}
	public List getCardTypeListByCardIDForauth(String instid,JdbcTemplate jdbctemplate,String cardtypeid ) throws Exception{
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID from CARD_TYPE where INST_ID = '"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/
		
		//by gowtham-190819
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID from CARD_TYPE where INST_ID =? AND CARD_TYPE_ID=?";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid,cardtypeid});

		return subfeelist;
	}
	public int getcardreissucardvalid(String instid, String cardtypeid, JdbcTemplate jdbctemplate) {
		int x =0;
		
		/*String cardvalid = "SELECT COUNT(*) FROM BASENO WHERE INST_ID='"+instid+"' AND BASENO_CODE='"+cardtypeid+"' ";
		enctrace("CARD_TYPEqueryexist:" + cardvalid );
		x = jdbctemplate.queryForInt(cardvalid);*/
		
		//by gowtham-190891
		String cardvalid = "SELECT COUNT(*) FROM BASENO WHERE INST_ID=? AND BASENO_CODE=? ";
		enctrace("CARD_TYPEqueryexist:" + cardvalid );
		x = jdbctemplate.queryForInt(cardvalid,new Object[]{instid,cardtypeid});
		
		return x;
	}
	public String updateAuthCardType(String authstatus,String userid,String instid,String cardtypeid) {
		
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		
		return update_authdeauth_qury;
	}
	public String updateDeAuthCardType(String authstatus,String userid,String remarks,String instid,String cardtypeid) {
		
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		
		/*//by gowtham-190819
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=?,REMARKS=? WHERE INST_ID=? AND CARD_TYPE_ID=?";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		*/
		return update_authdeauth_qury;
	}
	
	public String updateauthcodecardtype(String authstatus,String userid,String remarks,String instid,String cardtypeid) {
		
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtypeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		
		/*//by gowtham-190819
		String update_authdeauth_qury = "UPDATE CARD_TYPE SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=?,REMARKS=? WHERE INST_ID=? AND CARD_TYPE_ID=?";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);*/
		
		return update_authdeauth_qury;
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  CARD_TYPE  where INST_ID='"+instid+"' and AUTH_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public List getCardTypeListByMakerCardID(String instid,JdbcTemplate jdbctemplate) {
		
		System.out.println("getCardTypeTableName::::CARD_TYPE");  
		List subfeelist = null;
		
		/*String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from CARD_TYPE where INST_ID = '"+instid+"'";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry);*/

		//by gowtham-190819
		String qrysubfeecodeqry ="select CARD_TYPE_ID,CARD_TYPE_DESC,MERCHANT_ALLOWED,PREPAID_ALLOWED,MAKER_ID,DECODE(AUTH_CODE,'1','Authorized','0','Waiting for Authorize','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS  from CARD_TYPE where INST_ID = ?";
		enctrace("qrysubfeecodeqry : " + qrysubfeecodeqry );
		subfeelist = jdbctemplate.queryForList(qrysubfeecodeqry,new Object[]{instid});
		return subfeelist;
	}
	
	
	
	public List gettingBinList(String instid, JdbcTemplate jdbctemplate) {
		List res = null;
		try {
			
			/*String qry = "SELECT BIN,BASELEN FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND ATTACH_PRODTYPE_CARDTYPE='C'";
			enctrace("Query for binlist ::: "+qry);
			res = jdbctemplate.queryForList(qry);*/
			
			//by gowtham-190819
			String qry = "SELECT PRD_CODE,BASE_NO_LEN FROM PRODUCTINFO WHERE INST_ID=?";
			enctrace("Query for binlist ::: "+qry);
			res = jdbctemplate.queryForList(qry,new Object[]{instid,"C"});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
} 
