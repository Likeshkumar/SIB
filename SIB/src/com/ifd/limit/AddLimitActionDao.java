package com.ifd.limit;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

import test.Date;

@Transactional
public class AddLimitActionDao extends BaseAction {

	private String txncode;
	
	
	public String getTxncode() {
		return txncode;
	}
	public void setTxncode(String txncode) {
		this.txncode = txncode;
	}
	/*
	public int validateActvatedCard(String instid,String cardno,JdbcTemplate jdbcTemplate)
	{
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select count(1) from CARD_PRODUCTION where card_no in (select chn from EZCARDINFO ) and card_no='"+cardno+"' AND INST_ID='"+instid+"' ");
		enctrace("valdcardqry::"+valdcardqry.toString());  
		validcard = jdbcTemplate.queryForInt(valdcardqry.toString());
		return validcard;
	}
	
	public int validateActvatedAccount(String instid,String accountno,JdbcTemplate jdbcTemplate)
	{
		int validacct = -1;
		StringBuilder valdacctqry = new StringBuilder();
		valdacctqry.append("select COUNT(1) from ACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO FROM EZACCOUNTINFO WHERE ACCOUNTNO='"+accountno+"' AND INSTID='"+instid+"') ");
		enctrace("valdacctqry::"+valdacctqry.toString());  
		validacct = jdbcTemplate.queryForInt(valdacctqry.toString());
		return validacct;
	}
	
	
	public String hasprod(String inst_id, String limit_desc)  throws Exception
	{
		String prodexist = "SELECT count(*) FROM LIMIT_DESC WHERE INST_ID='"+inst_id+"' AND UPPER(LIMIT_DESC)=UPPER('"+limit_desc+"')";
		enctrace("prodexist : " + prodexist );
		return prodexist;
	}
	public String insert_limitTempdesc(String inst_id,String limitcode,String limitbase,String limit_desc,String curcode , String accstatuscode ,String auth_code,String usercode,String mkchkrstatus,String limittxntype) throws Exception
	{
	//int insert_result = -1; 
	String qryinsert = "INSERT INTO LIMIT_DESC (INST_ID, LIMIT_ID, LIMIT_DESC,CUR_CODE,STATUS_CODE,LIMIT_BASE,CONFIG_BY,CONFIG_DATE,MKCK_STATUS,AUTH_STATUS,LIMITTXNTYPE)";
	qryinsert += " values ( '"+inst_id+"','"+limitcode+"','"+limit_desc+"','"+curcode+"','"+accstatuscode+"','"+limitbase+"','"+usercode+"',sysdate,'"+mkchkrstatus+"','"+auth_code+"','"+limittxntype+"') ";
	enctrace( "limit info qryinsert :" + qryinsert);
	trace("Inserting limit description");
	return qryinsert;
	}
	
	public String update_limitTempdesc(String inst_id,String limit_id, String currencycode, String auth_code,String usercode,String mkchkrstatus,String limittxntype)
	{
		String updateqry = "UPDATE LIMIT_DESC SET INST_ID='"+inst_id+"',CUR_CODE='"+currencycode+"',MKCK_STATUS='"+mkchkrstatus+"',AUTH_STATUS='"+auth_code+"',CONFIG_BY='"+usercode+"',LIMITTXNTYPE='"+limittxntype+"' WHERE INST_ID='"+inst_id+"' AND LIMIT_ID='"+limit_id+"'";
		enctrace( "limit info qryUPDATE :" + updateqry);
		System.out.println("Updatelimittemp:"+updateqry);
		trace("updating limit description");
		return updateqry;
	}
	public int checkLimitExistForCurrency(String inst_id,String limitcode, JdbcTemplate jdbctemplate ) throws Exception {
		//int insert_result = -1;
		int x = -1;
		String limitqry = "SELECT COUNT(*) FROM IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+inst_id+"' AND CURKEY='"+limitcode+"'"; 
		enctrace( "limitqry :" + limitqry);
		x = jdbctemplate.queryForInt(limitqry);
		return x;
	}
	
	
	public String schemeinsert_result(String inst_id, String limitcode, String maxlimitamt, String maxlimitcnt, String txncode, String txnperiod, String pertxnamt,String curKey)  throws Exception
	{
	//int schemeinsert_result = -1; 
		trace("Inserting limit info");
		String qryschemeinsert = "INSERT INTO LIMITINFO ( INST_ID, LIMIT_TYPE,  LIMIT_ID, LIMIT_AMOUNT,LIMIT_COUNT, TXNCODE, PERIOD, PERTXNAMT,CURKEY )";
		qryschemeinsert += " values ( '"+inst_id+"','CDTP','"+limitcode+"','"+maxlimitamt+"','"+maxlimitcnt+"','"+txncode+"','"+txnperiod+"','"+pertxnamt+"','"+curKey+"' ) ";
		enctrace( "qryschemeinsert  :" + qryschemeinsert);
		return qryschemeinsert;
	}
	
	public int chklimExist( String instid, JdbcTemplate jdbctemplate )  throws Exception
	{
		String limitidexist = "SELECT COUNT(LIMIT_ID) FROM LIMIT_DESC WHERE INST_ID='"+instid+"'";
		enctrace("limitidexist===> " + limitidexist );
		int exist= (Integer)jdbctemplate.queryForInt(limitidexist);
		return exist;
	}
	public int limitidExist( String instid, JdbcTemplate jdbctemplate ,String limitdesc)  throws Exception
	{
		String limitexist = "select count(LIMIT_ID) as cnt from LIMIT_DESC where inst_id='"+instid+"' and LIMIT_DESC='"+limitdesc+"'";
		enctrace("limitidexist===> " + limitexist );
		int existid= (Integer)jdbctemplate.queryForInt(limitexist);
		return existid;
	}
	
	public List result(String i_Name,String bin, String card_id,JdbcTemplate jdbctemplate) throws Exception
	{
		List result= null;
		trace("Getting sub product datas");
		String qury="select * from IFP_INSTPROD_DETAILS where INST_ID='"+i_Name+"' and BIN='"+bin+"' and CARDTYPE_ID ='"+card_id+"'";
		enctrace("Getting subproduct datas qury : " + qury); 
		result =jdbctemplate.queryForList(qury); 
		return result;
		
	}
	public String activetime()  throws Exception
	{
		String activetime = null;
		activetime = "SELECT UPPER(REPLACE(CONVERT(varchar,GETDATE(),108),'', ''))";
		return activetime;
	}
	public String  activedate ()  throws Exception
	{
		String activedate = null; 
		//activedate = "(select to_date(trunc(sysdate)) from dual)";
		activedate ="select REPLACE(CONVERT(VARCHAR(9), GETDATE(), 6), ' ', '-')"; 
		return activedate;
	}
	public int count (String instid, String produid, String productcode, String limitid,JdbcTemplate jdbctemplate)  throws Exception
	{
		int count = -1;
		trace("Getting count of limit master...");
		String count_query = "SELECT count(*) from LIMIT_MASTER where INST_ID='"+instid+"' and PRODUCT_ID='"+produid+"' and PRODUCT_CODE='"+productcode+"' and LIMIT_ID='"+limitid+"'";
		enctrace("Getting limit master count_query : " +count_query);
		count = jdbctemplate.queryForInt(count_query);
		return count;
	}
	public int result(String instid ,String produid,String productcode, String limitid, String cardtype, String userid, String activedate, String activetime, String binno,JdbcTemplate jdbctemplate)  throws Exception
	{
		int result = -1;
		trace("Inserting limit master");
		String query ="INSERT INTO LIMIT_MASTER(INST_ID, PRODUCT_ID, PRODUCT_CODE, LIMIT_ID, CARDTYPE_ID,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, MAKER_TIME, CHECKER_TIME,BIN) VALUES ('"+instid+"', '"+produid+"', '"+productcode+"', '"+limitid+"', '"+cardtype+"','"+userid+"',"+activedate+",'"+userid+"', "+activedate+", 'M', "+activetime+","+activetime+",'"+binno+"')";
		enctrace("inserting limit master query: " +query);
		result = jdbctemplate.update(query);
		return result;
	}
	public int insert_result(String limitid, String maxamnt, String txnamt, String txncount, String reload, String activedate, String activetime,JdbcTemplate jdbctemplate)
	{
		int insert_result = -1;
		trace("Inserting limit details");
		String insert_query ="INSERT INTO LIMIT_DETAILS (LIMIT_ID, MAX_AMT, DAILY_TXN_AMT, DAILY_TXN_CNT, RELOAD_COUNT, LIMIT_FLAG, MAKER_DATE, MAKER_TIME) VALUES ('"+limitid+"', '"+maxamnt+"', '"+txnamt+"', '"+txncount+"', '"+reload+"', 'Y',"+activedate+","+activetime+")";
		enctrace("insert_query : " +insert_query );
		insert_result = jdbctemplate.update(insert_query);
		return insert_result;
		
	}
	public List getLimitDesc(String instid,String mkrchkr,JdbcTemplate jdbctemplate) throws Exception
	{
		List limitdesc = null;
		String limitdesc_qury = "select LIMIT_DESC,LIMIT_ID,INST_ID from LIMIT_DESC where INST_ID='"+instid+"'" ;//AND STATUS_CODE='1' AND MKCK_STATUS='"+mkrchkr+"'
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury);
		return limitdesc;
	}
	
	public String getLimitId(String instid,String keycode,JdbcTemplate jdbctemplate) throws Exception{
		String limitid = null;
		String limitdesc_qury = "select LIMIT_ID from IFD_LIMIT_CURRENCY_TEMP where INST_ID='"+instid+"' AND CURKEY='"+keycode+"' AND ROWNUM <=1";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		try{
			limitid= (String)jdbctemplate.queryForObject(limitdesc_qury, String.class);
		}catch(EmptyResultDataAccessException e){}
		return limitid;
	}
	
	
	
	public String getLimitName(String instid, String limitid,JdbcTemplate jdbctemplate){
		String limitname=null;
		String limitdescp = "select LIMIT_DESC from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limitdescp=> "+limitdescp);
		try{
			limitname= (String)jdbctemplate.queryForObject(limitdescp, String.class);
		}catch(EmptyResultDataAccessException e){}
		return limitname;
	}
	
	
	public List getViewLimitDescp(String instid, String limitid,JdbcTemplate jdbctemplate) 
	{
		List viewlimitdescp =null;
		String limitdescp = "select LIMIT_DESC,LIMIT_ID from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limitdescp=> "+limitdescp);
		viewlimitdescp = jdbctemplate.queryForList(limitdescp);
		return viewlimitdescp;
	}
	public List getResult(String i_Name, String bin, String productId,JdbcTemplate jdbctemplate) throws Exception
	{
	List result = null;	
	String qury ="select PRODUCT_NAME as PRODUCT_NAME,(Convert(nvarchar(50),BIN)+'~'+Convert(nvarchar(50),PRODUCT_ID)+'~'+Convert(nvarchar(50),PRODUCT_CODE)) as PRODUCT_CODE from IFP_INSTPROD_DETAILS where  INST_ID='"+i_Name+"' and BIN='"+bin+"' and PRODUCT_ID='"+productId+"'";
	enctrace("getting subrpdouct qury :" + qury);
	result =jdbctemplate.queryForList(qury);
	return result;
	}
	public List getViewLimitDesc(String instid, String limitid,JdbcTemplate jdbctemplate)
	{
		List viewlimitdesc=null;
		String viewlimit="select LIMIT_AMOUNT,LIMIT_COUNT,PERIOD,PERTXNAMT from LIMITINFO where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limit inrfo records viewlimit : " + viewlimit ); 
		viewlimitdesc = jdbctemplate.queryForList(viewlimit);
		return viewlimitdesc;
	}
	public List getInstitutionDatas(String instid,JdbcTemplate jdbctemplate)
	{
		List result=null;
		String qury = "select * from IFP_INSTPROD_DETAILS where INST_ID='"+instid+"'"; 
		enctrace("Instittuion datas qry : "+qury);
		result =jdbctemplate.queryForList(qury);
		return result;
	}
	public List getLimitinfo(String instid,String limitid,String[] txn ,JdbcTemplate jdbctemplate) 
	{
		List result=null;
		String qury="select * from LIMITINFO where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"' and TXNCODE='"+txn+"'";
		result =jdbctemplate.queryForList(qury);
		return result;
	}
	public List getLimitResult(String inst_Name, String limit_id, String curkey, JdbcTemplate jdbctemplate)
	{
		List limit_result = null;
		String limit_query="select INST_ID,LIMIT_TYPE,LIMIT_ID,LIMIT_AMOUNT,LIMIT_COUNT,TXNCODE,DECODE(PERIOD,'D','DAILY','W','WEEKLY','M','MONTHLY') as PERIOD,PERTXNAMT,CURKEY from LIMITINFO where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limit_id+"' AND CURKEY='"+curkey+"'";
		enctrace("limint info limit_query : "+limit_query);
		limit_result =jdbctemplate.queryForList(limit_query);
		return limit_result;
	}
	public List getEditLimitDescp(String instid,String limitid,JdbcTemplate jdbctemplate)
	{
		List editlimitdescp = null;
		String limitdescp = "select LIMIT_DESC from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limitdescp=> "+limitdescp);
		editlimitdescp = jdbctemplate.queryForList(limitdescp);
		return editlimitdescp;
	}
	public List getIpfLimitDesc(String limitid,JdbcTemplate jdbctemplate)
	{
		List limit_desc = null;
		String listdesc="select * from LIMIT_DESC where LIMIT_ID='"+limitid+"'";
		limit_desc =jdbctemplate.queryForList(listdesc);
		return limit_desc;
	}
	public List getTransdecResult(String inst_Name, String limitdesc,JdbcTemplate jdbctemplate)
	{
		List transdec_result = null;
		String transdec="select * from LIMITINFO where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limitdesc+"'";
		enctrace("transdec : "+transdec);
		transdec_result =jdbctemplate.queryForList(transdec);
		return transdec_result;
	}
	public List getTransdecResult(String inst_Name,String limitid,String curkey,String txncode,JdbcTemplate jdbctemplate)
	{
		List transdec_result = null;
		String transdec="select LIMIT_AMOUNT,LIMIT_COUNT,DECODE(PERIOD,'D','DAILY','W','WEEKLY','M','MONTHLY') AS PERIOD,PERTXNAMT,TXNCODE FROM LIMITINFO WHERE INST_ID='"+inst_Name+"' AND LIMIT_ID='"+limitid+"' AND TXNCODE='"+txncode+"' AND CURKEY='"+curkey+"'";
		enctrace("transdec : " + transdec);	 
		transdec_result =jdbctemplate.queryForList(transdec);
		return transdec_result;
	}
	public int getDeletingLimitDesc(String inst_Name , String limitid,JdbcTemplate jdbctemplate)
	{
		int del = -1;
		String dellist = "delete from LIMIT_DESC where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limitid+"'";
		enctrace("dellis  : "+dellist);
		del = jdbctemplate.update(dellist);
		return del;
	}
	public int getDelListinfo(String inst_Name,String limitid,JdbcTemplate jdbctemplate) throws Exception
	{
		int delinfo = -1;
		String dellistinfo = "delete from LIMITINFO where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limitid+"'";
		enctrace("dellistinfo : "+dellistinfo);
		delinfo = jdbctemplate.update(dellistinfo); 
		return delinfo;
	}
	public int getUpdatingLimitDesc(String limitdesc,String inst_id,String limitid,JdbcTemplate jdbctemplate){
		int update_result=-1;
		String qryupdate = "update LIMIT_DESC set LIMIT_DESC='"+limitdesc+"' where INST_ID='"+inst_id+"' and LIMIT_ID='"+limitid+"'";
		enctrace( "qryupdate : " + qryupdate);
		update_result = jdbctemplate.update(qryupdate);
		return update_result;
	}
	public int getUpdatingLimitInfo(String maxlimitamt, String maxlimitcnt, String txnperiod, String pertxnamt,String inst_id, String limitid, String curkey, String txncode,JdbcTemplate jdbctemplate)
	{
		int updateresult = -1;
		String queryupdate = "update LIMITINFO set LIMIT_AMOUNT='"+maxlimitamt+"',LIMIT_COUNT='"+maxlimitcnt+"',PERTXNAMT='"+pertxnamt+"' where INST_ID='"+inst_id+"' and TXNCODE='"+txncode+"' AND CURKEY='"+curkey+"'";
		enctrace( "updating limit info queryupdate  : " + queryupdate);
		updateresult = jdbctemplate.update(queryupdate);
		return updateresult;
	}
	public String updateLimitDetails(String maxamt,String txnamt,String txncnt,String reloadcnt,String limitid) {
		String update_query = "UPDATE LIMIT_DETAILS SET MAX_AMT= '"+maxamt+"', DAILY_TXN_AMT = '"+txnamt+"',DAILY_TXN_CNT='"+txncnt+"',RELOAD_COUNT='"+reloadcnt+"' WHERE LIMIT_ID = '"+limitid+"'";
		return update_query;
	}
	public List getMasterLimitList(String instid,String statuscode,String mkrchkr,JdbcTemplate jdbctemplate ) throws Exception {
		List masterfeelist = null;
		String masterfeelistqry ="SELECT LIMIT_ID,  LIMIT_DESC from LIMIT_DESC WHERE INST_ID = '"+instid+"' order by limit_base DESC";//AND LIMIT_BASE='P'
		enctrace("masterfeelistqry : " + masterfeelistqry );
		masterfeelist = jdbctemplate.queryForList(masterfeelistqry);
		return masterfeelist;
	}
	
	public  List getLimitCurrency( String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		List limitcurrency =null;
		String limitcurqry = "SELECT * FROM IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		limitcurrency = jdbctemplate.queryForList(limitcurqry);
		return limitcurrency ;
	}
	
	

	public String limitlistbylimitid(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		String limitname = null;
		try{
			String masterfeelistqry ="SELECT LIMIT_DESC from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
			enctrace("masterfeelistqry : " + masterfeelistqry );
			limitname = (String)jdbctemplate.queryForObject(masterfeelistqry, String.class);
		}catch(Exception e){}
		return limitname;
	}
	
	public int updateAuthLimit(String authstatus,String reason ,String userid,String instid,String limitid, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		String update_authdeauth_qury = "UPDATE LIMIT_DESC SET REMARKS='"+reason+"' ,MKCK_STATUS='"+authstatus+"', AUTH_STATUS='"+authstatus+"',AUTH_DATE=sysdate,AUTH_BY='"+userid+"' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		enctrace("update_authdeauth_qury:::"+update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	public int updateAuthLimitEzlink(String instid,String limitid, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		String update_authdeauth_qury = "UPDATE LIMITINFO SET AUTH_CODE='1' WHERE INSTID='"+instid+"' AND LIMIT_RECID='"+limitid+"'";
		x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	public int updateLimitCurrecyStatus(String authstatus,String userid,String instid,String limitid, String keycode, String remarks, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		String update_authdeauth_qury = "UPDATE IFD_LIMIT_CURRENCY_TEMP SET AUTH_STATUS='"+authstatus+"', AUTH_BY='"+userid+"',AUTH_DATE=sysdate, REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+keycode+"'";
		enctrace("update_authdeauth_qury :" + update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	public int updateLimitCurrecyStatusWhileEdit(String authstatus,String userid,String instid,String limitid, String keycode, String remarks, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		String update_authdeauth_qury = "UPDATE IFD_LIMIT_CURRENCY_TEMP SET AUTH_STATUS='"+authstatus+"', ADDED_BY='"+userid+"',ADDED_DATE=sysdate, REMARKS='"+remarks+"', AUTH_BY='', AUTH_DATE='' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'  AND CURKEY='"+keycode+"'";
		enctrace("update currency status while edit :" + update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	
	public int updateDeAuthLimit(String authstatus,String userid,String remarks,String instid,String limitid,  JdbcTemplate jdbctemplate ) throws Exception  {
		int x =-1;
		String update_authdeauth_qury = "UPDATE LIMIT_DESC SET MKCK_STATUS='"+authstatus+"', STATUS_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	
	
	public int getAuthexistLimitList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  LIMIT_DESC  where INST_ID='"+instid+"' and STATUS_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public List getLimitDescForchecker(String instid,JdbcTemplate jdbctemplate) throws Exception{
		List limitdesc = null;
		//String limitdesc_qury = "select LIMIT_DESC,LIMIT_ID,INST_ID from LIMIT_DESC where INST_ID='"+instid+"'and STATUS_CODE='0' AND LIMIT_BASE='P' AND MKCK_STATUS='M'";
		String limitdesc_qury = "SELECT DISTINCT LIMIT_ID FROM LIMIT_DESC WHERE INST_ID='"+instid+"' AND AUTH_STATUS='0'";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury);
		return limitdesc;
	}
	
	public List getLimitDescForedit(String instid,JdbcTemplate jdbctemplate) throws Exception{
		List limitdesc = null;
		//String limitdesc_qury = "select LIMIT_DESC,LIMIT_ID,INST_ID from LIMIT_DESC where INST_ID='"+instid+"'and STATUS_CODE='0' AND LIMIT_BASE='P' AND MKCK_STATUS='M'";
		String limitdesc_qury = "SELECT DISTINCT LIMIT_ID FROM LIMIT_DESC WHERE INST_ID='"+instid+"' ";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury);
		return limitdesc;
	}
	
	public int deleteLimitDescFromProduction(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String dellimitqry = "DELETE FROM LIMIT_DESC WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		enctrace("dellimitqry  :" + dellimitqry );
		x = jdbctemplate.update(dellimitqry);
		return x;
	}  
	
	public int moveLimitDescToProduction(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String movelimitqry = "INSERT INTO LIMIT_DESC ( INST_ID, LIMIT_ID, LIMIT_DESC, CUR_CODE, STATUS_CODE, LIMIT_BASE, MKCK_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, ADDED_BY, ADDED_DATE, AUTH_STATUS,LIMITTXNTYPE )";
		movelimitqry += "SELECT  INST_ID, LIMIT_ID, LIMIT_DESC, CUR_CODE, STATUS_CODE, LIMIT_BASE, MKCK_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, ADDED_BY, ADDED_DATE, AUTH_STATUS,LIMITTXNTYPE FROM LIMIT_DESC WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";  
		enctrace("movelimitqry :" + movelimitqry );
		x = jdbctemplate.update(movelimitqry);
		return x; 
	}
	
	public int moveToEzlinkProduction(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveToEzlinkProduction = "INSERT INTO EZLIMITINFO ( INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE )";
		moveToEzlinkProduction += "select INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE from LIMITINFO  WHERE INSTID='"+instid+"' AND LIMIT_RECID='"+limitid+"'";  
		enctrace("moveToEzlinkProduction :" + moveToEzlinkProduction );
		x = jdbctemplate.update(moveToEzlinkProduction);
		return x;   
	}
	      
	public int deleteLimitCurrencyFromProduction(String instid, String limitid, String keycode, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String deletelimitcurqry = "DELETE FROM IFD_LIMIT_CURRENCY WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+keycode+"'";
		enctrace("deletelimitcurqry :" + deletelimitcurqry );
		x = jdbctemplate.update(deletelimitcurqry);
		return x;
	} 
	public int moveLimitCurrencyToProduction(String instid, String limitid, String keycode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String movelimitqry = "INSERT INTO IFD_LIMIT_CURRENCY ";
		movelimitqry += "SELECT  * FROM IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+keycode+"'";  
		enctrace("movelimitqry1 :" + movelimitqry );
		x = jdbctemplate.update(movelimitqry);
		return x;
	}
	
	public int deleteLimitDetailsFromProduction(String instid, String limitid, String curkey, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String deletelimitcurqry = "DELETE FROM LIMITINFO WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+curkey+"'";
		enctrace("deletelimitcurqry :" + deletelimitcurqry );
		x = jdbctemplate.update(deletelimitcurqry);
		return x;
	} 
	
	public int moveLimitDetailsToProduction(String instid, String limitid, String curkey, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String movelimitdetqry = "INSERT INTO LIMITINFO ";
		movelimitdetqry += "SELECT  * FROM LIMITINFO WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+curkey+"'";  
		enctrace("movelimitdetqry :" + movelimitdetqry );
		x = jdbctemplate.update(movelimitdetqry);
		return x;
	}
	
	public List getCurrencyLimitDetails(String instid, String limitid, String curcode, JdbcTemplate jdbctemplate ) throws Exception {
		List limitlist = null;
		String limitlistqry ="SELECT ADDED_BY, TO_CHAR( ADDED_DATE,'DD-MON-YYYY' ) AS ADDED_DATE , AUTH_STATUS, AUTH_BY, TO_CHAR( AUTH_DATE,'DD-MON-YYYY' ) AS AUTH_DATE, REMARKS FROM  IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CUR_CODE='"+curcode+"'";
		enctrace("limitlistqry :"+limitlistqry);
		limitlist = jdbctemplate.queryForList(limitlistqry);
		return limitlist;
	}
	
	public int validateLimitRange(String instid, String limitid, String keycode, String txncode, String txnperiod, String maxlimitamount, String maxlimitcnt, String pertxnamount,
			JdbcTemplate jdbctemplate) throws Exception{
		int x = -1,range=-1;
		System.out.println("txnperiod [ "+txnperiod+" ] ");
		if( txnperiod == null ){ enctrace("Got limit type null "); return -1 ; }
		List periodlist = null;
		if( txnperiod.equals("W")){
			
			periodlist = this.getPeriodLimitFromTemp(instid, limitid, keycode, txncode, "D", jdbctemplate);
			System.out.println("Getting data for Daily [ "+txnperiod+" ] txncode["+txncode+"]");
			range = this.checkRangeAmount(txncode, maxlimitamount, maxlimitcnt, pertxnamount, periodlist);
			enctrace("range : " + range );
		}else if( txnperiod.equals("M")){
			periodlist = this.getPeriodLimitFromTemp(instid, limitid, keycode, txncode, "D", jdbctemplate);
			periodlist = this.getPeriodLimitFromTemp(instid, limitid, keycode, txncode, "W", jdbctemplate);
		}  
		return x; 
	}
	
	public int checkRangeAmount( String txncode, String maxlimitamount, String maxlimitcnt, String pertxnamount, List periodlist ){
		int x =-1; 
		if( !periodlist.isEmpty()  ){
			Iterator itr = periodlist.iterator(); 
			while( itr.hasNext() ){
				Map mp = (Map)itr.next(); 
				//Double dbval = Double.valueOf(arg0)
				Double val = Double.parseDouble( (String)(Object)mp.get("LIMIT_AMOUNT").toString() );//new BigInteger(  );//BigInteger() ;
				Double givenval = Double.parseDouble(maxlimitamount);
				System.out.println("txn [ "+txncode+" ]...configured[ "+ val +" ....newly adding limit [ "+maxlimitamount+" ]   "); 
				if( val > givenval ){
					addActionError("Withdrawal Limit Amount Should Greater Than Daily WithDraw Limit. Configured Daily Limit [ "+ val +" ] ");
					System.out.println("Withdrawal Limit Amount Should Greater Than Daily WithDraw Limit. Configured Daily Limit [ "+val+" ] ");
					x = 1;
				}
			} 
		}
		return x;
	}
	 
	private List getPeriodLimitFromTemp(String instid, String limitid, String keycode, String txncode, String period, JdbcTemplate jdbctemplate ) throws Exception {
		List periodlimit=null;
		String periodlimitqry = "SELECT PERTXNAMT, LIMIT_AMOUNT, LIMIT_COUNT FROM IFD_LIMIT_CURRENCY_TEMP A, LIMITINFO B WHERE A.INST_ID=B.INST_ID AND A.CURKEY=B.CURKEY AND A.INST_ID='"+instid+"'  AND A.PERIOD='"+period+"' AND B.TXNCODE='"+txncode+"'" ;
		enctrace("periodlimitqry :" + periodlimitqry );
		periodlimit=jdbctemplate.queryForList(periodlimitqry);
		return periodlimit;

	}
	
	public int updateIndividualCardLimit(String instid, String tablename, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String limitupdqry = "UPDATE "+tablename+" SET LIMIT_ID='"+limitid+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+limitid+"'";
		enctrace("limitupdqry  :"+ limitupdqry  );
		x = jdbctemplate.update(limitupdqry);
		return x;
	}  
	
	public int insert_limitcurrency_result(String inst_id,String curKey, String limitcode, String limit_currncy, 
			String limittype, String fromdate,String todate,String txnperiod, String usercode, String userstatus, JdbcTemplate jdbctemplate)  throws Exception	{
		int x =-1;
		String qryschemeinsert = "INSERT INTO IFD_LIMIT_CURRENCY_TEMP ( INST_ID,CURKEY,LIMIT_ID,CUR_CODE, ADDED_BY, AUTH_STATUS, PERIOD, LIMITTYPE, FROMDATE, TODATE )";
		qryschemeinsert += " values ( '"+inst_id+"','"+curKey+"','"+limitcode+"','"+limit_currncy+"','"+usercode+"','"+userstatus+"','"+txnperiod+"','"+limittype+"',TO_DATE('"+fromdate+"','DD-MM-YYYY'),TO_DATE('"+todate+"','DD-MM-YYYY')) ";
		enctrace( "qryschemeinsert  :" + qryschemeinsert);
		x = jdbctemplate.update(qryschemeinsert);
		return x;
	}
	
	public int insertezLimitInfo(String instid, String limittype, String limitid, String txncode, String currencycode, String d_perday, String d_count, String w_perday, String w_count, String m_perday, String m_count, String y_perday, String y_count, String string,String limitgenCode,String fromdate,String todate, JdbcTemplate jdbctemplate) 
	{
		int x = -1;
		StringBuilder insertezLimitInfo = new StringBuilder();
		insertezLimitInfo.append("INSERT INTO LIMITINFO ");
		insertezLimitInfo.append("(INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG , AUTH_CODE ,LIMIT_RECID ,FROMDATE,TODATE) ");
		insertezLimitInfo.append("VALUES ");
		insertezLimitInfo.append("('"+instid+"', '"+limittype+"', '"+limitid+"', '"+txncode+"', '"+currencycode+"', '"+d_perday+"', '"+d_count+"', '"+w_perday+"', '"+w_count+"', '"+m_perday+"', '"+m_count+"', '"+y_perday+"', '"+y_count+"', '"+string+"','0','"+limitgenCode+"',TO_DATE('"+fromdate+"','DD-MM-YYYY'),TO_DATE('"+todate+"','DD-MM-YYYY') )");
		enctrace("insertezLimitInfo  :"+ insertezLimitInfo.toString()  );
		x = jdbctemplate.update(insertezLimitInfo.toString());
		return x;
		
		    
	}
	public int updateezLimitInfo(String instid, String limittype, String limitid, String txncode, String currencycode, String d_perday, String d_count, String w_perday, String w_count, String m_perday, String m_count, String y_perday, String y_count, String string,String limitgenCode,String fromdate,String todate,String auth_code, JdbcTemplate jdbctemplate)
	{
		int x = -1;
		String updatelimitqry ="UPDATE EZLIMITINFO SET TXNCODE='"+txncode+"',AUTH_CODE='"+auth_code+"',CURRCODE='"+currencycode+"',AMOUNT='"+d_perday+"',COUNT='"+d_count+"',WAMOUNT='"+w_perday+"',WCOUNT='"+w_count+"',MAMOUNT='"+m_perday+"',MCOUNT='"+m_count+"',YAMOUNT='"+y_perday+"',YCOUNT='"+y_count+"',FROMDATE=TO_DATE('"+fromdate+"','DD-MM-YYYY'),TODATE=TO_DATE('"+todate+"','DD-MM-YYYY') WHERE INSTID='"+instid+"' AND LIMIT_RECID='"+limitgenCode+"' AND TXNCODE='"+txncode+"'";
		enctrace("updateezLimitInfo  :"+ updatelimitqry.toString()  );
		x = jdbctemplate.update(updatelimitqry.toString());
		return x;
	}
	
	
	public List getGlobalLimitDetails(JdbcTemplate jdbctemplate) {
		List globalLmtDet=null;
		String globalLmtDetQry = "SELECT RECID, LIMIT_TYPE, LIMIT_DESC, CARD_FLAG, ACCT_FLAG, ACT_FLAG FROM GLOBAL_CARDDETAILS WHERE ACT_FLAG = '1'" ;
		enctrace("globalLmtDet :" + globalLmtDetQry );
		globalLmtDet=jdbctemplate.queryForList(globalLmtDetQry);
		return globalLmtDet;
	}
	
	public List getCardtypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List cardtypelist = null;
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		cardtypelist = jdbctemplate.queryForList(cardtypeqry);
		return cardtypelist;
	}
	
	public List getAcctTypeList( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"'";
		acctypelist = jdbctemplate.queryForList(acctypelistqry);
		return acctypelist;
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
	
	public String getAcctTypeDesc(String instid, String accttypeid, JdbcTemplate jdbctemplate){
		String accttypename = null;
		String accttypedesc = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID = '"+instid+"'  and  ACCTTYPEID='"+accttypeid+"' and rownum<=1";
		enctrace(" accttypedesc : " + accttypedesc);
		try{
			accttypename  = (String)jdbctemplate.queryForObject(accttypedesc, String.class);
		}catch(EmptyResultDataAccessException e){}
		return accttypename;
	}
	
	public String getLimitTypeDesc(String instid, String limitype, JdbcTemplate jdbctemplate){
		String limittypename = null;
		String limittypedesc = "SELECT LIMIT_DESC FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE='"+limitype+"' and rownum<=1";
		enctrace(" limittypedesc : " + limittypedesc);
		try{
			limittypename  = (String)jdbctemplate.queryForObject(limittypedesc, String.class);
		}catch(EmptyResultDataAccessException e){}
		return limittypename;
	}
	public int validateActvatedCardNew(String instid, StringBuffer hcardno,JdbcTemplate jdbctemplate) {
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select count(1) from EZCARDINFO where CHN='"+hcardno+"' AND INSTID='"+instid+"' and STATUS in ('50','53') ");
		enctrace("valdcardqryNew::"+valdcardqry.toString());  
		validcard = jdbctemplate.queryForInt(valdcardqry.toString());
		return validcard;
	}
	public int deleteLimitDetailsFromSwitch(String instid, String limitid, JdbcTemplate jdbctemplate) {
		int x =-1;
		String deletelimitswitchqry = "DELETE FROM EZLIMITINFO WHERE INSTID='"+instid+"' AND LIMITID IN(SELECT DISTINCT LIMITID FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"') AND CURRCODE IN(SELECT CURRCODE FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"')";
		enctrace("deletelimitswitchqry :" + deletelimitswitchqry );
		x = jdbctemplate.update(deletelimitswitchqry);
		return x;
	}
	public int limitHistory(String instid, String limitid, String username, JdbcTemplate jdbctemplate) {
		int x = -1;
		String moveToEzlinkProduction = "INSERT INTO LIMITINFOHIST ( INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,PROCESSED_BY,PROCESSED_DATE )";
		moveToEzlinkProduction += "select INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,'"+username+"',SYSDATE from EZLIMITINFO  WHERE INSTID='"+instid+"' AND LIMITID IN(SELECT DISTINCT LIMITID FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"')" +
				" AND CURRCODE IN(SELECT CURRCODE FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"')";  
		enctrace("moveToEzlinkProduction :" + moveToEzlinkProduction );
		x = jdbctemplate.update(moveToEzlinkProduction);
		return x;   
	}
	
	*/
	
	
	public int validateActvatedCard(String instid,String cardno,JdbcTemplate jdbcTemplate)
	{
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select count(1) from CARD_PRODUCTION where card_no in (select chn from EZCARDINFO ) and card_no='"+cardno+"' AND INST_ID='"+instid+"' ");
		enctrace("valdcardqry::"+valdcardqry.toString());  
		validcard = jdbcTemplate.queryForInt(valdcardqry.toString());
		return validcard;
	}
	
	public int validateActvatedAccount(String instid,String accountno,JdbcTemplate jdbcTemplate)
	{
		int validacct = -1;
		StringBuilder valdacctqry = new StringBuilder();
		
		/*valdacctqry.append("select COUNT(1) from ACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO FROM EZACCOUNTINFO WHERE ACCOUNTNO='"+accountno+"' AND INSTID='"+instid+"') ");
		enctrace("valdacctqry::"+valdacctqry.toString());  
		validacct = jdbcTemplate.queryForInt(valdacctqry.toString());*/
		
		//by gowtham-200819
		valdacctqry.append("select COUNT(1) from ACCOUNTINFO where ACCOUNTNO in (select ACCOUNTNO FROM EZACCOUNTINFO WHERE ACCOUNTNO=? AND INSTID=?) ");
		enctrace("valdacctqry::"+valdacctqry.toString());  
		validacct = jdbcTemplate.queryForInt(valdacctqry.toString(),new Object[]{accountno,instid});
		
		return validacct;
	}
	
	
	public String hasprod(String inst_id, String limit_desc)  throws Exception
	{
		String prodexist = "SELECT count(*) FROM LIMIT_DESC WHERE INST_ID='"+inst_id+"' AND UPPER(LIMIT_DESC)=UPPER('"+limit_desc+"')";
		enctrace("prodexist : " + prodexist );
		return prodexist;
	}
	public String insert_limitTempdesc(String inst_id,String limitcode,String limitbase,String limit_desc,String curcode , String accstatuscode ,String auth_code,String usercode,String mkchkrstatus,String limittxntype) throws Exception
	{
	//int insert_result = -1; 
		
	String qryinsert = "INSERT INTO LIMIT_DESC (INST_ID, LIMIT_ID, LIMIT_DESC,CUR_CODE,STATUS_CODE,LIMIT_BASE,CONFIG_BY,CONFIG_DATE,MKCK_STATUS,AUTH_STATUS,LIMITTXNTYPE)";
	qryinsert += " values ( '"+inst_id+"','"+limitcode+"','"+limit_desc+"','"+curcode+"','"+accstatuscode+"','"+limitbase+"','"+usercode+"',sysdate,'"+mkchkrstatus+"','"+auth_code+"','"+limittxntype+"') ";
		
	/*	//by gowtham-200819
		String qryinsert = "INSERT INTO LIMIT_DESC (INST_ID, LIMIT_ID, LIMIT_DESC,CUR_CODE,STATUS_CODE,LIMIT_BASE,CONFIG_BY,CONFIG_DATE,MKCK_STATUS,AUTH_STATUS,LIMITTXNTYPE)";
		qryinsert += " values (?,?,?,?,?,?,?,?,?,?,?)";*/
	
	enctrace( "limit info qryinsert :" + qryinsert);
	trace("Inserting limit description");
	return qryinsert;
	}
	
	public String update_limitTempdesc(String inst_id,String limit_id, String currencycode, String auth_code,String usercode,String mkchkrstatus,String limittxntype)
	{
		//by gowtham-200819
		   String updateqry = "UPDATE LIMIT_DESC SET INST_ID='"+inst_id+"',CUR_CODE='"+currencycode+"',MKCK_STATUS='"+mkchkrstatus+"',AUTH_STATUS='0'  ,CONFIG_BY='"+usercode+"',LIMITTXNTYPE='"+limittxntype+"' WHERE INST_ID='"+inst_id+"' AND LIMIT_ID='"+limit_id+"'";
		//String updateqry = "UPDATE LIMIT_DESC SET INST_ID=?,CUR_CODE=?,MKCK_STATUS=?,AUTH_STATUS=?,CONFIG_BY=?,LIMITTXNTYPE=? WHERE INST_ID=? AND LIMIT_ID=?";
		
		enctrace( "limit info qryUPDATE :" + updateqry);
		//System.out.println("Updatelimittemp:"+updateqry);
		trace("updating limit description");
		return updateqry;
	}
	public int checkLimitExistForCurrency(String inst_id,String limitcode, JdbcTemplate jdbctemplate ) throws Exception {
		//int insert_result = -1;
		int x = -1;
		String limitqry = "SELECT COUNT(*) FROM IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+inst_id+"' AND CURKEY='"+limitcode+"'"; 
		enctrace( "limitqry :" + limitqry);
		x = jdbctemplate.queryForInt(limitqry);
		return x;
	}
	
	
	public String schemeinsert_result(String inst_id, String limitcode, String maxlimitamt, String maxlimitcnt, String txncode, String txnperiod, String pertxnamt,String curKey)  throws Exception
	{
	//int schemeinsert_result = -1; 
		trace("Inserting limit info");
		String qryschemeinsert = "INSERT INTO LIMITINFO ( INST_ID, LIMIT_TYPE,  LIMIT_ID, LIMIT_AMOUNT,LIMIT_COUNT, TXNCODE, PERIOD, PERTXNAMT,CURKEY )";
		qryschemeinsert += " values ( '"+inst_id+"','CDTP','"+limitcode+"','"+maxlimitamt+"','"+maxlimitcnt+"','"+txncode+"','"+txnperiod+"','"+pertxnamt+"','"+curKey+"' ) ";
		enctrace( "qryschemeinsert  :" + qryschemeinsert);
		return qryschemeinsert;
	}
	
	public int chklimExist( String instid, JdbcTemplate jdbctemplate )  throws Exception
	{
		String limitidexist = "SELECT COUNT(LIMIT_ID) FROM LIMIT_DESC WHERE INST_ID='"+instid+"'";
		enctrace("limitidexist===> " + limitidexist );
		int exist= (Integer)jdbctemplate.queryForInt(limitidexist);
		return exist;
	}
	public int limitidExist( String instid, JdbcTemplate jdbctemplate ,String limitdesc)  throws Exception
	{
		String limitexist = "select count(LIMIT_ID) as cnt from LIMIT_DESC where inst_id='"+instid+"' and LIMIT_DESC='"+limitdesc+"'";
		enctrace("limitidexist===> " + limitexist );
		int existid= (Integer)jdbctemplate.queryForInt(limitexist);
		return existid;
	}
	
	public List result(String i_Name,String bin, String card_id,JdbcTemplate jdbctemplate) throws Exception
	{
		List result= null;
		trace("Getting sub product datas");
		
		/*String qury="select * from IFP_INSTPROD_DETAILS where INST_ID='"+i_Name+"' and BIN='"+bin+"' and CARDTYPE_ID ='"+card_id+"'";
		enctrace("Getting subproduct datas qury : " + qury); 
		result =jdbctemplate.queryForList(qury); */
		
		//by gowtham-200819
		String qury="select * from IFP_INSTPROD_DETAILS where INST_ID=? and BIN=? and CARDTYPE_ID =?";
		enctrace("Getting subproduct datas qury : " + qury); 
		result =jdbctemplate.queryForList(qury,new Object[]{i_Name,bin,card_id}); 
		
		return result;
		
	}
	public String activetime()  throws Exception
	{
		String activetime = null;
		activetime = "SELECT UPPER(REPLACE(CONVERT(varchar,GETDATE(),108),'', ''))";
		return activetime;
	}
	public String  activedate ()  throws Exception
	{
		String activedate = null; 
		//activedate = "(select to_date(trunc(sysdate)) from dual)";
		activedate ="select REPLACE(CONVERT(VARCHAR(9), GETDATE(), 6), ' ', '-')"; 
		return activedate;
	}
	public int count (String instid, String produid, String productcode, String limitid,JdbcTemplate jdbctemplate)  throws Exception
	{
		int count = -1;
		trace("Getting count of limit master...");
		
		/*String count_query = "SELECT count(*) from LIMIT_MASTER where INST_ID='"+instid+"' and PRODUCT_ID='"+produid+"' and PRODUCT_CODE='"+productcode+"' and LIMIT_ID='"+limitid+"'";
		enctrace("Getting limit master count_query : " +count_query);
		count = jdbctemplate.queryForInt(count_query);*/
		
		//by gowtham-200819
		String count_query = "SELECT count(*) from LIMIT_MASTER where INST_ID=? and PRODUCT_ID=? and PRODUCT_CODE=? and LIMIT_ID=?";
		enctrace("Getting limit master count_query : " +count_query);
		count = jdbctemplate.queryForInt(count_query,new Object[]{instid,produid,productcode,limitid});
		
		return count;
	}
	public int result(String instid ,String produid,String productcode, String limitid, String cardtype, String userid, String activedate, String activetime, String binno,JdbcTemplate jdbctemplate)  throws Exception
	{
		int result = -1;
		trace("Inserting limit master");
		
		/*String query ="INSERT INTO LIMIT_MASTER(INST_ID, PRODUCT_ID, PRODUCT_CODE, LIMIT_ID, CARDTYPE_ID,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, MAKER_TIME, CHECKER_TIME,BIN) VALUES ('"+instid+"', '"+produid+"', '"+productcode+"', '"+limitid+"', '"+cardtype+"','"+userid+"',"+activedate+",'"+userid+"', "+activedate+", 'M', "+activetime+","+activetime+",'"+binno+"')";
		enctrace("inserting limit master query: " +query);
		result = jdbctemplate.update(query);*/

		//by gowtham-200819
		String query ="INSERT INTO LIMIT_MASTER(INST_ID, PRODUCT_ID, PRODUCT_CODE, LIMIT_ID, CARDTYPE_ID,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, MAKER_TIME, CHECKER_TIME,BIN) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		enctrace("inserting limit master query: " +query);
		result = jdbctemplate.update(query,new Object[]{instid,produid,productcode,limitid,cardtype,userid,activedate,userid,activedate,"M",activetime,activetime,binno});
		
		return result;
	}
	public int insert_result(String limitid, String maxamnt, String txnamt, String txncount, String reload, String activedate, String activetime,JdbcTemplate jdbctemplate)
	{
		int insert_result = -1;
		trace("Inserting limit details");
		
		/*String insert_query ="INSERT INTO LIMIT_DETAILS (LIMIT_ID, MAX_AMT, DAILY_TXN_AMT, DAILY_TXN_CNT, RELOAD_COUNT, LIMIT_FLAG, MAKER_DATE, MAKER_TIME) VALUES ('"+limitid+"', '"+maxamnt+"', '"+txnamt+"', '"+txncount+"', '"+reload+"', 'Y',"+activedate+","+activetime+")";
		enctrace("insert_query : " +insert_query );
		insert_result = jdbctemplate.update(insert_query);*/
		
		//by gowtham-200819
		String insert_query ="INSERT INTO LIMIT_DETAILS (LIMIT_ID, MAX_AMT, DAILY_TXN_AMT, DAILY_TXN_CNT, RELOAD_COUNT, LIMIT_FLAG, MAKER_DATE, MAKER_TIME) VALUES (?,?,?,?,?,?,?,?)";
		enctrace("insert_query : " +insert_query );
		insert_result = jdbctemplate.update(insert_query,new Object[]{limitid,maxamnt,txnamt,txncount,reload,"Y",activedate,activetime});
		
		return insert_result;
		
	}
	public List getLimitDesc(String instid,String mkrchkr,JdbcTemplate jdbctemplate) throws Exception
	{
		List limitdesc = null;
		
		/*String limitdesc_qury = "select LIMIT_DESC,LIMIT_ID,INST_ID from LIMIT_DESC where INST_ID='"+instid+"'" ;//AND STATUS_CODE='1' AND MKCK_STATUS='"+mkrchkr+"'
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury);*/
		
		///by gowtham-200819
		String limitdesc_qury = "select LIMIT_DESC,LIMIT_ID,INST_ID from LIMIT_DESC where INST_ID=?" ;//AND STATUS_CODE='1' AND MKCK_STATUS='"+mkrchkr+"'
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury,new Object[]{instid});

		return limitdesc;
	}
	
	public String getLimitId(String instid,String keycode,JdbcTemplate jdbctemplate) throws Exception{
		String limitid = null;
		
		/*String limitdesc_qury = "select LIMIT_ID from IFD_LIMIT_CURRENCY_TEMP where INST_ID='"+instid+"' AND CURKEY='"+keycode+"' AND ROWNUM <=1";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		try{
		limitid= (String)jdbctemplate.queryForObject(limitdesc_qury, String.class);*/
		
		//by gowtham-200819
		String limitdesc_qury = "select LIMIT_ID from IFD_LIMIT_CURRENCY_TEMP where INST_ID=? AND CURKEY=? AND ROWNUM <=?";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		try{
		limitid= (String)jdbctemplate.queryForObject(limitdesc_qury, new Object[]{instid,keycode,"1"},String.class);
			
		}catch(EmptyResultDataAccessException e){}
		return limitid;
	}
	
	
	
	public String getLimitName(String instid, String limitid,JdbcTemplate jdbctemplate){
		String limitname=null;
		
	String limitdescp = "select LIMIT_DESC from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limitdescp=> "+limitdescp);
		try{
			limitname= (String)jdbctemplate.queryForObject(limitdescp, String.class); 
		
		//by gowtham200819
			/*	String limitdescp = "select LIMIT_DESC from LIMIT_DESC where INST_ID=? and LIMIT_ID=?";
		enctrace("limitdescp=> "+limitdescp);
		try{
			limitname= (String)jdbctemplate.queryForObject(limitdescp,new Object[]{instid,limitid}, String.class);  */
			
		}catch(EmptyResultDataAccessException e){}
		return limitname;
	}
	
	
	public List getViewLimitDescp(String instid, String limitid,JdbcTemplate jdbctemplate) 
	{
		List viewlimitdescp =null;
		
		/*String limitdescp = "select LIMIT_DESC,LIMIT_ID from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limitdescp=> "+limitdescp);
		viewlimitdescp = jdbctemplate.queryForList(limitdescp);*/
		
		//by gowtham-200819
		String limitdescp = "select LIMIT_DESC,LIMIT_ID from LIMIT_DESC where INST_ID=? and LIMIT_ID=?";
		enctrace("limitdescp=> "+limitdescp);
		viewlimitdescp = jdbctemplate.queryForList(limitdescp,new Object[]{instid,limitid});
		
		return viewlimitdescp;
	}
	public List getResult(String i_Name, String bin, String productId,JdbcTemplate jdbctemplate) throws Exception
	{
	List result = null;	
	
	/*String qury ="select PRODUCT_NAME as PRODUCT_NAME,(Convert(nvarchar(50),BIN)+'~'+Convert(nvarchar(50),PRODUCT_ID)+'~'+Convert(nvarchar(50),PRODUCT_CODE)) as PRODUCT_CODE from IFP_INSTPROD_DETAILS where  INST_ID='"+i_Name+"' and BIN='"+bin+"' and PRODUCT_ID='"+productId+"'";
	enctrace("getting subrpdouct qury :" + qury);
	result =jdbctemplate.queryForList(qury);*/

	//by gowtham-200819
	String qury ="select PRODUCT_NAME as PRODUCT_NAME,(Convert(nvarchar(50),BIN)+'~'+Convert(nvarchar(50),PRODUCT_ID)+'~'+Convert(nvarchar(50),PRODUCT_CODE)) as PRODUCT_CODE from IFP_INSTPROD_DETAILS where  INST_ID=? and BIN=? and PRODUCT_ID=?";
	enctrace("getting subrpdouct qury :" + qury);
	result =jdbctemplate.queryForList(qury,new Object[]{i_Name,bin,productId});
	
	return result;
	}
	public List getViewLimitDesc(String instid, String limitid,JdbcTemplate jdbctemplate)
	{
		List viewlimitdesc=null;
		
		/*String viewlimit="select LIMIT_AMOUNT,LIMIT_COUNT,PERIOD,PERTXNAMT from LIMITINFO where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limit inrfo records viewlimit : " + viewlimit ); 
		viewlimitdesc = jdbctemplate.queryForList(viewlimit);*/
		
		//by gowtham-200819
		String viewlimit="select LIMIT_AMOUNT,LIMIT_COUNT,PERIOD,PERTXNAMT from LIMITINFO where INST_ID=? and LIMIT_ID=? ";
		enctrace("limit inrfo records viewlimit : " + viewlimit ); 
		viewlimitdesc = jdbctemplate.queryForList(viewlimit,new Object[]{instid,limitid});
		
		return viewlimitdesc;
	}
	public List getInstitutionDatas(String instid,JdbcTemplate jdbctemplate)
	{
		List result=null;
		
		/*String qury = "select * from IFP_INSTPROD_DETAILS where INST_ID='"+instid+"'"; 
		enctrace("Instittuion datas qry : "+qury);
		result =jdbctemplate.queryForList(qury);*/
		
		//by gowtham-200819
		String qury = "select * from IFP_INSTPROD_DETAILS where INST_ID=?"; 
		enctrace("Instittuion datas qry : "+qury);
		result =jdbctemplate.queryForList(qury,new Object[]{instid});
		
		return result;
	}
	public List getLimitinfo(String instid,String limitid,String[] txn ,JdbcTemplate jdbctemplate) 
	{
		List result=null;
		
		/*String qury="select * from LIMITINFO where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"' and TXNCODE='"+txn+"'";
		result =jdbctemplate.queryForList(qury);*/
		
		//by gowtham200819
		String qury="select * from LIMITINFO where INST_ID=? and LIMIT_ID=? and TXNCODE=? ";
		result =jdbctemplate.queryForList(qury,new Object[]{instid,limitid,txn});
		
		return result;
	}
	public List getLimitResult(String inst_Name, String limit_id, String curkey, JdbcTemplate jdbctemplate)
	{
		List limit_result = null;
		String limit_query="select INST_ID,LIMIT_TYPE,LIMIT_ID,LIMIT_AMOUNT,LIMIT_COUNT,TXNCODE,DECODE(PERIOD,'D','DAILY','W','WEEKLY','M','MONTHLY') as PERIOD,PERTXNAMT,CURKEY from LIMITINFO where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limit_id+"' AND CURKEY='"+curkey+"'";
		enctrace("limint info limit_query : "+limit_query);
		limit_result =jdbctemplate.queryForList(limit_query);
		return limit_result;
	}
	public List getEditLimitDescp(String instid,String limitid,JdbcTemplate jdbctemplate)
	{
		List editlimitdescp = null;
		String limitdescp = "select LIMIT_DESC from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
		enctrace("limitdescp=> "+limitdescp);
		editlimitdescp = jdbctemplate.queryForList(limitdescp);
		return editlimitdescp;
	}
	public List getIpfLimitDesc(String limitid,JdbcTemplate jdbctemplate)
	{
		List limit_desc = null;
		String listdesc="select * from LIMIT_DESC where LIMIT_ID='"+limitid+"'";
		limit_desc =jdbctemplate.queryForList(listdesc);
		return limit_desc;
	}
	public List getTransdecResult(String inst_Name, String limitdesc,JdbcTemplate jdbctemplate)
	{
		List transdec_result = null;
		
		/*String transdec="select * from LIMITINFO where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limitdesc+"'";
		enctrace("transdec : "+transdec);
		transdec_result =jdbctemplate.queryForList(transdec);*/
		
		//by gowtham-200819
		String transdec="select * from LIMITINFO where INST_ID=? and LIMIT_ID=?";
		enctrace("transdec : "+transdec);
		transdec_result =jdbctemplate.queryForList(transdec,new Object[]{inst_Name,limitdesc});
		
		return transdec_result;
	}
	public List getTransdecResult(String inst_Name,String limitid,String curkey,String txncode,JdbcTemplate jdbctemplate)
	{
		List transdec_result = null;
		
		/*String transdec="select LIMIT_AMOUNT,LIMIT_COUNT,DECODE(PERIOD,'D','DAILY','W','WEEKLY','M','MONTHLY') AS PERIOD,PERTXNAMT,TXNCODE FROM LIMITINFO WHERE INST_ID='"+inst_Name+"' AND LIMIT_ID='"+limitid+"' AND TXNCODE='"+txncode+"' AND CURKEY='"+curkey+"'";
		enctrace("transdec : " + transdec);	 
		transdec_result =jdbctemplate.queryForList(transdec);*/
		
		//by gowtham-200819
		String transdec="select LIMIT_AMOUNT,LIMIT_COUNT,DECODE(PERIOD,'D','DAILY','W','WEEKLY','M','MONTHLY') AS PERIOD,PERTXNAMT,TXNCODE FROM LIMITINFO WHERE INST_ID=? AND LIMIT_ID=? AND TXNCODE=? AND CURKEY=?";
		enctrace("transdec : " + transdec);	 
		transdec_result =jdbctemplate.queryForList(transdec,new Object[]{inst_Name,limitid,txncode,curkey});
		
		return transdec_result;
	}
	public int getDeletingLimitDesc(String inst_Name , String limitid,JdbcTemplate jdbctemplate)
	{
		int del = -1;
		
		/*String dellist = "delete from LIMIT_DESC where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limitid+"'";
		enctrace("dellis  : "+dellist);
		del = jdbctemplate.update(dellist);*/
		
		//by gowtham-200819
		String dellist = "delete from LIMIT_DESC where INST_ID=? and LIMIT_ID=?";
		enctrace("dellis  : "+dellist);
		del = jdbctemplate.update(dellist,new Object[]{inst_Name,limitid});
		
		return del;
	}
	public int getDelListinfo(String inst_Name,String limitid,JdbcTemplate jdbctemplate) throws Exception
	{
		int delinfo = -1;
		
		/*String dellistinfo = "delete from LIMITINFO where INST_ID='"+inst_Name+"' and LIMIT_ID='"+limitid+"'";
		enctrace("dellistinfo : "+dellistinfo);
		delinfo = jdbctemplate.update(dellistinfo); */
		
		//by gowtham-200819
		String dellistinfo = "delete from LIMITINFO where INST_ID=? and LIMIT_ID=?";
		enctrace("dellistinfo : "+dellistinfo);
		delinfo = jdbctemplate.update(dellistinfo,new Object[]{inst_Name,limitid}); 
		
		return delinfo;
	}
	public int getUpdatingLimitDesc(String limitdesc,String inst_id,String limitid,JdbcTemplate jdbctemplate){
		int update_result=-1;
		
		/*String qryupdate = "update LIMIT_DESC set LIMIT_DESC='"+limitdesc+"' where INST_ID='"+inst_id+"' and LIMIT_ID='"+limitid+"'";
		enctrace( "qryupdate : " + qryupdate);
		update_result = jdbctemplate.update(qryupdate);*/
		
		//by gowtham-200819
		String qryupdate = "update LIMIT_DESC set LIMIT_DESC=? where INST_ID=? and LIMIT_ID=?";
		enctrace( "qryupdate : " + qryupdate);
		update_result = jdbctemplate.update(qryupdate,new Object[]{limitdesc,inst_id,limitid});
		
		return update_result;
	}
	public int getUpdatingLimitInfo(String maxlimitamt, String maxlimitcnt, String txnperiod, String pertxnamt,String inst_id, String limitid, String curkey, String txncode,JdbcTemplate jdbctemplate)
	{
		int updateresult = -1;
		
		/*String queryupdate = "update LIMITINFO set LIMIT_AMOUNT='"+maxlimitamt+"',LIMIT_COUNT='"+maxlimitcnt+"',PERTXNAMT='"+pertxnamt+"' where INST_ID='"+inst_id+"' and TXNCODE='"+txncode+"' AND CURKEY='"+curkey+"'";
		enctrace( "updating limit info queryupdate  : " + queryupdate);
		updateresult = jdbctemplate.update(queryupdate);*/
		
		//by gowtham200819
		String queryupdate = "update LIMITINFO set LIMIT_AMOUNT=?,LIMIT_COUNT=?,PERTXNAMT=? where INST_ID=? and TXNCODE=? AND CURKEY=?";
		enctrace( "updating limit info queryupdate  : " + queryupdate);
		updateresult = jdbctemplate.update(queryupdate,new Object[]{maxlimitamt,maxlimitcnt,pertxnamt,inst_id,txncode,curkey});
		
		return updateresult;
	}
	public String updateLimitDetails(String maxamt,String txnamt,String txncnt,String reloadcnt,String limitid) {
		String update_query = "UPDATE LIMIT_DETAILS SET MAX_AMT= '"+maxamt+"', DAILY_TXN_AMT = '"+txnamt+"',DAILY_TXN_CNT='"+txncnt+"',RELOAD_COUNT='"+reloadcnt+"' WHERE LIMIT_ID = '"+limitid+"'";
		return update_query;
	}
	public List getMasterLimitList(String instid,String statuscode,String mkrchkr,JdbcTemplate jdbctemplate ) throws Exception {
		List masterfeelist = null;
		
		/*String masterfeelistqry ="SELECT LIMIT_ID,  LIMIT_DESC from LIMIT_DESC WHERE INST_ID = '"+instid+"' order by limit_base DESC";//AND LIMIT_BASE='P'
		enctrace("masterfeelistqry : " + masterfeelistqry );
		masterfeelist = jdbctemplate.queryForList(masterfeelistqry);*/
		
		//by gowtham=200819
		String masterfeelistqry ="SELECT LIMIT_ID,  LIMIT_DESC from LIMIT_DESC WHERE INST_ID =? order by limit_base DESC";//AND LIMIT_BASE='P'
		enctrace("masterfeelistqry : " + masterfeelistqry );
		masterfeelist = jdbctemplate.queryForList(masterfeelistqry,new Object[]{instid});
		
		return masterfeelist;
	}
	
	public  List getLimitCurrency( String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		List limitcurrency =null;
		String limitcurqry = "SELECT * FROM IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		limitcurrency = jdbctemplate.queryForList(limitcurqry);
		return limitcurrency ;
	}
	
	

	public String limitlistbylimitid(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		String limitname = null;
		try{
			
		String masterfeelistqry ="SELECT LIMIT_DESC from LIMIT_DESC where INST_ID='"+instid+"' and LIMIT_ID='"+limitid+"'";
			enctrace("masterfeelistqry : " + masterfeelistqry );
			limitname = (String)jdbctemplate.queryForObject(masterfeelistqry, String.class);
			
			//by gowtham200819
			/*	String masterfeelistqry ="SELECT LIMIT_DESC from LIMIT_DESC where INST_ID=? and LIMIT_ID=?";
			enctrace("masterfeelistqry : " + masterfeelistqry );
			limitname = (String)jdbctemplate.queryForObject(masterfeelistqry,new Object[]{instid,limitid} ,String.class); */
			
		}catch(Exception e){}
		return limitname;
	}
	
	public int updateAuthLimit(String authstatus,String reason ,String userid,String instid,String limitid, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		Date date =  new Date();
		/*String update_authdeauth_qury = "UPDATE LIMIT_DESC SET REMARKS='"+reason+"' ,MKCK_STATUS='"+authstatus+"', AUTH_STATUS='"+authstatus+"',AUTH_DATE=sysdate,AUTH_BY='"+userid+"' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		enctrace("update_authdeauth_qury:::"+update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury);*/

		//by gowtham200819
		String update_authdeauth_qury = "UPDATE LIMIT_DESC SET REMARKS=? ,MKCK_STATUS=?, AUTH_STATUS=?,AUTH_DATE=?,AUTH_BY=? WHERE INST_ID=? AND LIMIT_ID=?";
		enctrace("update_authdeauth_qury:::"+update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{reason,authstatus,authstatus,date.getCurrentDate(),userid,instid,limitid});
		
		return x;
	}
	
	public int updateAuthLimitEzlink(String instid,String limitid, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		
		
		/*String update_authdeauth_qury = "UPDATE LIMITINFO SET AUTH_CODE='1' WHERE INSTID='"+instid+"' AND LIMIT_RECID='"+limitid+"'";
		x = jdbctemplate.update(update_authdeauth_qury);*/
		
		//by gowtham200819
		String update_authdeauth_qury = "UPDATE LIMITINFO SET AUTH_CODE=?  WHERE INSTID=? AND LIMIT_RECID=?";
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{"1",instid,limitid});
		
		enctrace("updateAuthLimitEzlink ===== :::    "+update_authdeauth_qury);
		return x;
	}
	
	public int updateLimitCurrecyStatus(String authstatus,String userid,String instid,String limitid, String keycode, String remarks, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		String update_authdeauth_qury = "UPDATE IFD_LIMIT_CURRENCY_TEMP SET AUTH_STATUS='"+authstatus+"', AUTH_BY='"+userid+"',AUTH_DATE=sysdate, REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+keycode+"'";
		enctrace("update_authdeauth_qury :" + update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	public int updateLimitCurrecyStatusWhileEdit(String authstatus,String userid,String instid,String limitid, String keycode, String remarks, JdbcTemplate jdbctemplate ) throws Exception  {
		int x=-1;
		
		Date date =  new Date();
		/*String update_authdeauth_qury = "UPDATE IFD_LIMIT_CURRENCY_TEMP SET AUTH_STATUS='"+authstatus+"', ADDED_BY='"+userid+"',ADDED_DATE=sysdate, REMARKS='"+remarks+"', AUTH_BY='', AUTH_DATE='' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'  AND CURKEY='"+keycode+"'";
		enctrace("update currency status while edit :" + update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury);*/
		
		//by gowtham-200819
		String update_authdeauth_qury = "UPDATE IFD_LIMIT_CURRENCY_TEMP SET AUTH_STATUS=?, ADDED_BY=?,ADDED_DATE=?, REMARKS=?, AUTH_BY=?, AUTH_DATE=? WHERE INST_ID=? AND LIMIT_ID=?  AND CURKEY=?";
		enctrace("update currency status while edit :" + update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{authstatus,userid,date.getCurrentDate(),remarks,"","",instid,limitid,keycode});
		
		return x;
	}
	
	
	public int updateDeAuthLimit(String authstatus,String userid,String remarks,String instid,String limitid,  JdbcTemplate jdbctemplate ) throws Exception  {
		int x =-1;
		String update_authdeauth_qury = "UPDATE LIMIT_DESC SET MKCK_STATUS='"+authstatus+"', STATUS_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	
	
	public int getAuthexistLimitList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  LIMIT_DESC  where INST_ID='"+instid+"' and STATUS_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public List getLimitDescForchecker(String instid,JdbcTemplate jdbctemplate) throws Exception{
		List limitdesc = null;
		//String limitdesc_qury = "select LIMIT_DESC,LIMIT_ID,INST_ID from LIMIT_DESC where INST_ID='"+instid+"'and STATUS_CODE='0' AND LIMIT_BASE='P' AND MKCK_STATUS='M'";
		
		String limitdesc_qury = "SELECT DISTINCT LIMIT_ID FROM LIMIT_DESC WHERE INST_ID='"+instid+"' AND AUTH_STATUS='0'";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury);
		
		
	/*	//by gowtham-200819
		String limitdesc_qury = "SELECT DISTINCT LIMIT_ID FROM LIMIT_DESC WHERE INST_ID=? AND AUTH_STATUS=?";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury,new Object[]{instid,"0"});
		*/
		
		return limitdesc;
	}
	
	public List getLimitDescForedit(String instid,JdbcTemplate jdbctemplate) throws Exception{
		List limitdesc = null;
		//String limitdesc_qury = "select LIMIT_DESC,LIMIT_ID,INST_ID from LIMIT_DESC where INST_ID='"+instid+"'and STATUS_CODE='0' AND LIMIT_BASE='P' AND MKCK_STATUS='M'";
		
		/*String limitdesc_qury = "SELECT DISTINCT LIMIT_ID FROM LIMIT_DESC WHERE INST_ID='"+instid+"' ";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury);*/
		
		//by gowtham-200819
		String limitdesc_qury = "SELECT DISTINCT LIMIT_ID FROM LIMIT_DESC WHERE INST_ID=? ";
		enctrace("limitdesc_qury : " +limitdesc_qury);
		limitdesc = jdbctemplate.queryForList(limitdesc_qury,new Object[]{instid});
		
		
		return limitdesc;
	}
	
	public int deleteLimitDescFromProduction(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String dellimitqry = "DELETE FROM LIMIT_DESC WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";
		enctrace("dellimitqry  :" + dellimitqry );
		x = jdbctemplate.update(dellimitqry);
		return x;
	}  
	
	public int moveLimitDescToProduction(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String movelimitqry = "INSERT INTO LIMIT_DESC ( INST_ID, LIMIT_ID, LIMIT_DESC, CUR_CODE, STATUS_CODE, LIMIT_BASE, MKCK_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, ADDED_BY, ADDED_DATE, AUTH_STATUS,LIMITTXNTYPE )";
		movelimitqry += "SELECT  INST_ID, LIMIT_ID, LIMIT_DESC, CUR_CODE, STATUS_CODE, LIMIT_BASE, MKCK_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, ADDED_BY, ADDED_DATE, AUTH_STATUS,LIMITTXNTYPE FROM LIMIT_DESC WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"'";  
		enctrace("movelimitqry :" + movelimitqry );
		x = jdbctemplate.update(movelimitqry);
		return x; 
	}
	
	public int moveToEzlinkProduction(String instid, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String moveToEzlinkProduction = "INSERT INTO EZLIMITINFO ( INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,LIMIT_RECID,AUTH_CODE )";
		moveToEzlinkProduction += "select INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,LIMIT_RECID,AUTH_CODE  from LIMITINFO  WHERE INSTID='"+instid+"' AND LIMIT_RECID='"+limitid+"'";  
		enctrace("moveToEzlinkProduction :" + moveToEzlinkProduction );
		x = jdbctemplate.update(moveToEzlinkProduction);
		return x;   
	}
	      
	public int deleteLimitCurrencyFromProduction(String instid, String limitid, String keycode, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String deletelimitcurqry = "DELETE FROM IFD_LIMIT_CURRENCY WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+keycode+"'";
		enctrace("deletelimitcurqry :" + deletelimitcurqry );
		x = jdbctemplate.update(deletelimitcurqry);
		return x;
	} 
	public int moveLimitCurrencyToProduction(String instid, String limitid, String keycode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String movelimitqry = "INSERT INTO IFD_LIMIT_CURRENCY ";
		movelimitqry += "SELECT  * FROM IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+keycode+"'";  
		enctrace("movelimitqry1 :" + movelimitqry );
		x = jdbctemplate.update(movelimitqry);
		return x;
	}
	
	public int deleteLimitDetailsFromProduction(String instid, String limitid, String curkey, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		String deletelimitcurqry = "DELETE FROM LIMITINFO WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+curkey+"'";
		enctrace("deletelimitcurqry :" + deletelimitcurqry );
		x = jdbctemplate.update(deletelimitcurqry);
		return x;
	} 
	
	public int moveLimitDetailsToProduction(String instid, String limitid, String curkey, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String movelimitdetqry = "INSERT INTO LIMITINFO ";
		movelimitdetqry += "SELECT  * FROM LIMITINFO WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CURKEY='"+curkey+"'";  
		enctrace("movelimitdetqry :" + movelimitdetqry );
		x = jdbctemplate.update(movelimitdetqry);
		return x;
	}
	
	public List getCurrencyLimitDetails(String instid, String limitid, String curcode, JdbcTemplate jdbctemplate ) throws Exception {
		List limitlist = null;
		String limitlistqry ="SELECT ADDED_BY, TO_CHAR( ADDED_DATE,'DD-MON-YYYY' ) AS ADDED_DATE , AUTH_STATUS, AUTH_BY, TO_CHAR( AUTH_DATE,'DD-MON-YYYY' ) AS AUTH_DATE, REMARKS FROM  IFD_LIMIT_CURRENCY_TEMP WHERE INST_ID='"+instid+"' AND LIMIT_ID='"+limitid+"' AND CUR_CODE='"+curcode+"'";
		enctrace("limitlistqry :"+limitlistqry);
		limitlist = jdbctemplate.queryForList(limitlistqry);
		return limitlist;
	}
	
	public int validateLimitRange(String instid, String limitid, String keycode, String txncode, String txnperiod, String maxlimitamount, String maxlimitcnt, String pertxnamount,
			JdbcTemplate jdbctemplate) throws Exception{
		int x = -1,range=-1;
		System.out.println("txnperiod [ "+txnperiod+" ] ");
		if( txnperiod == null ){ enctrace("Got limit type null "); return -1 ; }
		List periodlist = null;
		if( txnperiod.equals("W")){
			
			periodlist = this.getPeriodLimitFromTemp(instid, limitid, keycode, txncode, "D", jdbctemplate);
			System.out.println("Getting data for Daily [ "+txnperiod+" ] txncode["+txncode+"]");
			range = this.checkRangeAmount(txncode, maxlimitamount, maxlimitcnt, pertxnamount, periodlist);
			enctrace("range : " + range );
		}else if( txnperiod.equals("M")){
			periodlist = this.getPeriodLimitFromTemp(instid, limitid, keycode, txncode, "D", jdbctemplate);
			periodlist = this.getPeriodLimitFromTemp(instid, limitid, keycode, txncode, "W", jdbctemplate);
		}  
		return x; 
	}
	
	public int checkRangeAmount( String txncode, String maxlimitamount, String maxlimitcnt, String pertxnamount, List periodlist ){
		int x =-1; 
		if( !periodlist.isEmpty()  ){
			Iterator itr = periodlist.iterator(); 
			while( itr.hasNext() ){
				Map mp = (Map)itr.next(); 
				//Double dbval = Double.valueOf(arg0)
				Double val = Double.parseDouble( (String)(Object)mp.get("LIMIT_AMOUNT").toString() );//new BigInteger(  );//BigInteger() ;
				Double givenval = Double.parseDouble(maxlimitamount);
				System.out.println("txn [ "+txncode+" ]...configured[ "+ val +" ....newly adding limit [ "+maxlimitamount+" ]   "); 
				if( val > givenval ){
					addActionError("Withdrawal Limit Amount Should Greater Than Daily WithDraw Limit. Configured Daily Limit [ "+ val +" ] ");
					System.out.println("Withdrawal Limit Amount Should Greater Than Daily WithDraw Limit. Configured Daily Limit [ "+val+" ] ");
					x = 1;
				}
			} 
		}
		return x;
	}
	 
	private List getPeriodLimitFromTemp(String instid, String limitid, String keycode, String txncode, String period, JdbcTemplate jdbctemplate ) throws Exception {
		List periodlimit=null;
		String periodlimitqry = "SELECT PERTXNAMT, LIMIT_AMOUNT, LIMIT_COUNT FROM IFD_LIMIT_CURRENCY_TEMP A, LIMITINFO B WHERE A.INST_ID=B.INST_ID AND A.CURKEY=B.CURKEY AND A.INST_ID='"+instid+"'  AND A.PERIOD='"+period+"' AND B.TXNCODE='"+txncode+"'" ;
		enctrace("periodlimitqry :" + periodlimitqry );
		periodlimit=jdbctemplate.queryForList(periodlimitqry);
		return periodlimit;

	}
	
	public int updateIndividualCardLimit(String instid, String tablename, String limitid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String limitupdqry = "UPDATE "+tablename+" SET LIMIT_ID='"+limitid+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+limitid+"'";
		enctrace("limitupdqry  :"+ limitupdqry  );
		x = jdbctemplate.update(limitupdqry);
		return x;
	}  
	
	public int insert_limitcurrency_result(String inst_id,String curKey, String limitcode, String limit_currncy, 
			String limittype, String fromdate,String todate,String txnperiod, String usercode, String userstatus, JdbcTemplate jdbctemplate)  throws Exception	{
		int x =-1;
		String qryschemeinsert = "INSERT INTO IFD_LIMIT_CURRENCY_TEMP ( INST_ID,CURKEY,LIMIT_ID,CUR_CODE, ADDED_BY, AUTH_STATUS, PERIOD, LIMITTYPE, FROMDATE, TODATE )";
		qryschemeinsert += " values ( '"+inst_id+"','"+curKey+"','"+limitcode+"','"+limit_currncy+"','"+usercode+"','"+userstatus+"','"+txnperiod+"','"+limittype+"',TO_DATE('"+fromdate+"','DD-MM-YYYY'),TO_DATE('"+todate+"','DD-MM-YYYY')) ";
		enctrace( "qryschemeinsert  :" + qryschemeinsert);
		x = jdbctemplate.update(qryschemeinsert);
		return x;
	}
	
	public int insertezLimitInfo(String instid, String limittype, String limitid, String txncode, String currencycode, String d_perday, String d_count, String w_perday, String w_count, String m_perday, String m_count, String y_perday, String y_count, String string,String limitgenCode,String fromdate,String todate, JdbcTemplate jdbctemplate) 
	{
		int x = -1;
		StringBuilder insertezLimitInfo = new StringBuilder();
		insertezLimitInfo.append("INSERT INTO LIMITINFO ");
		insertezLimitInfo.append("(INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG , AUTH_CODE ,LIMIT_RECID ,FROMDATE,TODATE) ");
		insertezLimitInfo.append("VALUES ");
		
		insertezLimitInfo.append("('"+instid+"', '"+limittype+"', '"+limitid+"', '"+txncode+"', '"+currencycode+"', '"+d_perday+"', '"+d_count+"', '"+w_perday+"', '"+w_count+"', '"+m_perday+"', '"+m_count+"', '"+y_perday+"', '"+y_count+"', '"+string+"','0','"+limitgenCode+"',TO_DATE('"+fromdate+"','DD-MM-YYYY'),TO_DATE('"+todate+"','DD-MM-YYYY') )");
		enctrace("insertezLimitInfo  :"+ insertezLimitInfo.toString()  );
		x = jdbctemplate.update(insertezLimitInfo.toString());
		
		/*//by gowtham-200819
		insertezLimitInfo.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),TO_DATE(?,'DD-MM-YYYY') )");
		enctrace("insertezLimitInfo  :"+ insertezLimitInfo.toString()  );
		x = jdbctemplate.update(insertezLimitInfo.toString(),new Object[]{instid,limittype,limitid,txncode,currencycode,d_perday,d_count,w_perday,w_count,m_perday,m_count,y_perday,y_count,string,"0",limitgenCode,fromdate,todate,});
		*/
		return x;
		
		    
	}
	public int updateezLimitInfo(String instid, String limittype, String limitid, String txncode, String currencycode, String d_perday, String d_count, String w_perday, String w_count, String m_perday, String m_count, String y_perday, String y_count, String string,String limitgenCode,String fromdate,String todate,String auth_code, JdbcTemplate jdbctemplate)
	{
		int x = -1;
		
	/*	String updatelimitqry ="UPDATE EZLIMITINFO SET TXNCODE='"+txncode+"',AUTH_CODE='"+auth_code+"',CURRCODE='"+currencycode+"',AMOUNT='"+d_perday+"',COUNT='"+d_count+"',WAMOUNT='"+w_perday+"',WCOUNT='"+w_count+"',MAMOUNT='"+m_perday+"',MCOUNT='"+m_count+"',YAMOUNT='"+y_perday+"',YCOUNT='"+y_count+"',"
				+ "FROMDATE=TO_DATE('"+fromdate+"','DD-MM-YYYY'),TODATE=TO_DATE('"+todate+"','DD-MM-YYYY')  WHERE INSTID='"+instid+"' AND LIMIT_RECID='"+limitgenCode+"' AND TXNCODE='"+txncode+"'";
		enctrace("updateezLimitInfo  :"+ updatelimitqry.toString()  );
		x = jdbctemplate.update(updatelimitqry.toString());*/
		
		//by gowtham-200819
		String updatelimitqry ="UPDATE EZLIMITINFO SET TXNCODE=?,AUTH_CODE=?,CURRCODE=?,AMOUNT=?,COUNT=?,WAMOUNT=?,WCOUNT=?,MAMOUNT=?,MCOUNT=?,YAMOUNT=?,YCOUNT=? WHERE INSTID=? AND LIMIT_RECID=? AND TXNCODE=?";
		enctrace("updateezLimitInfo  :"+ updatelimitqry.toString()  );
		x = jdbctemplate.update(updatelimitqry.toString(),new Object[]{txncode,auth_code,currencycode,d_perday,d_count,w_perday,w_count,m_perday,m_count,y_perday,y_count,instid,limitgenCode,txncode});
		
		
		return x;
	}
	
	
	public int updateLimitInfo(String instid, String limittype, String limitid, String txncode, String currencycode, String d_perday, String d_count, String w_perday, String w_count, String m_perday, String m_count, String y_perday, String y_count, String string,String limitgenCode,String fromdate,String todate,String auth_code, JdbcTemplate jdbctemplate)
	{
		int x = -1;
		
	/*	String updatelimitqry ="UPDATE EZLIMITINFO SET TXNCODE='"+txncode+"',AUTH_CODE='"+auth_code+"',CURRCODE='"+currencycode+"',AMOUNT='"+d_perday+"',COUNT='"+d_count+"',WAMOUNT='"+w_perday+"',WCOUNT='"+w_count+"',MAMOUNT='"+m_perday+"',MCOUNT='"+m_count+"',YAMOUNT='"+y_perday+"',YCOUNT='"+y_count+"',"
				+ "FROMDATE=TO_DATE('"+fromdate+"','DD-MM-YYYY'),TODATE=TO_DATE('"+todate+"','DD-MM-YYYY')  WHERE INSTID='"+instid+"' AND LIMIT_RECID='"+limitgenCode+"' AND TXNCODE='"+txncode+"'";
		enctrace("updateezLimitInfo  :"+ updatelimitqry.toString()  );
		x = jdbctemplate.update(updatelimitqry.toString());*/
		
		//by gowtham-200819
		String updatelimitqry ="UPDATE LIMITINFO SET TXNCODE=?,AUTH_CODE=?,CURRCODE=?,AMOUNT=?,COUNT=?,WAMOUNT=?,WCOUNT=?,MAMOUNT=?,MCOUNT=?,YAMOUNT=?,YCOUNT=? WHERE INSTID=? AND LIMIT_RECID=? AND TXNCODE=?";
		enctrace("updateezLimitInfo  :"+ updatelimitqry.toString()  );
		x = jdbctemplate.update(updatelimitqry.toString(),new Object[]{txncode,auth_code,currencycode,d_perday,d_count,w_perday,w_count,m_perday,m_count,y_perday,y_count,instid,limitgenCode,txncode});
		
		
		return x;
	}
	
	
	public List getGlobalLimitDetails(JdbcTemplate jdbctemplate) {
		List globalLmtDet=null;
		
		/*String globalLmtDetQry = "SELECT RECID, LIMIT_TYPE, LIMIT_DESC, CARD_FLAG, ACCT_FLAG, ACT_FLAG FROM GLOBAL_CARDDETAILS WHERE ACT_FLAG = '1'" ;
		enctrace("globalLmtDet :" + globalLmtDetQry );
		globalLmtDet=jdbctemplate.queryForList(globalLmtDetQry);*/
		
		//by gowtham-200819
		String globalLmtDetQry = "SELECT RECID, LIMIT_TYPE, LIMIT_DESC, CARD_FLAG, ACCT_FLAG, ACT_FLAG FROM GLOBAL_CARDDETAILS WHERE ACT_FLAG =?" ;
		enctrace("globalLmtDet :" + globalLmtDetQry );
		globalLmtDet=jdbctemplate.queryForList(globalLmtDetQry,new Object[]{"1"});
		
		return globalLmtDet;
	}
	
	public List getCardtypeList(String instid,JdbcTemplate jdbctemplate)
	{
		List cardtypelist = null;
		
		/*String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' and AUTH_CODE='1'";
		enctrace( "cardtypeqry--" + cardtypeqry);
		cardtypelist = jdbctemplate.queryForList(cardtypeqry);*/
		
		//by gowtham-200819
		String cardtypeqry ="SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID=? and AUTH_CODE=?";
		enctrace( "cardtypeqry--" + cardtypeqry);
		cardtypelist = jdbctemplate.queryForList(cardtypeqry,new Object[]{instid,"1"});
		
		return cardtypelist;
	}
	
	public List getAcctTypeList( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		
		/*String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"'";
		acctypelist = jdbctemplate.queryForList(acctypelistqry);*/
		
		//by gowtham-200819
		String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID=?";
		acctypelist = jdbctemplate.queryForList(acctypelistqry,new Object[]{instid});
		
		return acctypelist;
	}
	
	
	public String getCardTypeDesc(String instid, String cardtypeid, JdbcTemplate jdbctemplate){
		String cardtypename = null;
		
		/*String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID = '"+instid+"'  and  CARD_TYPE_ID='"+cardtypeid+"' and rownum<=1";
		enctrace(" cardtypedesc : " + cardtypedesc);
		try{
		cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc, String.class);*/
		
		//by gowtham-200819
		String cardtypedesc = "SELECT CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID =? and  CARD_TYPE_ID=? and rownum<=?";
		enctrace(" cardtypedesc : " + cardtypedesc);
		try{
			//cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc,String.class);
		cardtypename  = (String)jdbctemplate.queryForObject(cardtypedesc,new Object[]{instid,cardtypeid,"1"}, String.class);
			
		}catch(EmptyResultDataAccessException e){}
		return cardtypename;
	}
	
	public String getAcctTypeDesc(String instid, String accttypeid, JdbcTemplate jdbctemplate){
		String accttypename = null;
		
		String accttypedesc = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID =?  and  ACCTTYPEID=?and rownum<=?";
		//String accttypedesc = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID = '"+instid+"'  and  ACCTTYPEID='"+accttypeid+"' and rownum<=1";
		enctrace(" accttypedesc : " + accttypedesc);
		try{
			
			accttypename  = (String)jdbctemplate.queryForObject(accttypedesc,new Object[]{instid,accttypeid,"1"}, String.class);
			//accttypename  = (String)jdbctemplate.queryForObject(accttypedesc, String.class);
			
		}catch(EmptyResultDataAccessException e){}
		return accttypename;
	}
	
	public String getLimitTypeDesc(String instid, String limitype, JdbcTemplate jdbctemplate){
		String limittypename = null;
		
		//by gowtham-200819
	/*	String limittypedesc = "SELECT LIMIT_DESC FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE='"+limitype+"' and rownum<=1";*/
		String limittypedesc = "SELECT LIMIT_DESC FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE=? and rownum<=?";
		
		enctrace(" limittypedesc : " + limittypedesc);
		try{
			
			/*limittypename  = (String)jdbctemplate.queryForObject(limittypedesc, String.class);*/
			limittypename  = (String)jdbctemplate.queryForObject(limittypedesc, new Object[]{limitype,"1"},String.class);
			
		}catch(EmptyResultDataAccessException e){}
		return limittypename;
	}
	public int validateActvatedCardNew(String instid, StringBuffer hcardno,JdbcTemplate jdbctemplate) {
		int validcard = -1;
		
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select count(1) from EZCARDINFO where CHN='"+hcardno+"' AND INSTID='"+instid+"' and STATUS in ('50','53') ");
		enctrace("valdcardqryNew::"+valdcardqry.toString());  
		validcard = jdbctemplate.queryForInt(valdcardqry.toString());

	/*	//by gowtham-200819
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select count(1) from EZCARDINFO where CHN=? AND INSTID=? and STATUS in (?,?) ");
		enctrace("valdcardqryNew::"+valdcardqry.toString());  
		validcard = jdbctemplate.queryForInt(valdcardqry.toString(),new Object[]{hcardno,instid,"50","53"});
		*/
		return validcard;
	}
	public int deleteLimitDetailsFromSwitch(String instid, String limitid, JdbcTemplate jdbctemplate) {
		int x =-1;
		String deletelimitswitchqry = "DELETE FROM EZLIMITINFO WHERE INSTID='"+instid+"' AND LIMITID IN(SELECT DISTINCT LIMITID FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"') AND CURRCODE IN(SELECT CURRCODE FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"')";
		enctrace("deletelimitswitchqry :" + deletelimitswitchqry );
		x = jdbctemplate.update(deletelimitswitchqry);
		return x;
	}
	public int limitHistory(String instid, String limitid, String username, JdbcTemplate jdbctemplate) {
		int x = -1;
		
		String moveToEzlinkProduction = "INSERT INTO LIMITINFOHIST ( INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,PROCESSED_BY,PROCESSED_DATE )";
		moveToEzlinkProduction += "select INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,'"+username+"',SYSDATE from EZLIMITINFO  WHERE INSTID='"+instid+"' AND LIMITID IN(SELECT DISTINCT LIMITID FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"')" +
				" AND CURRCODE IN(SELECT CURRCODE FROM LIMITINFO WHERE LIMIT_RECID = '"+limitid+"')";  
		enctrace("moveToEzlinkProduction :" + moveToEzlinkProduction );
		x = jdbctemplate.update(moveToEzlinkProduction);
		
	/*	//by gowtham-200819
		String moveToEzlinkProduction = "INSERT INTO LIMITINFOHIST ( INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,PROCESSED_BY,PROCESSED_DATE )";
		moveToEzlinkProduction += "select INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,FROMDATE,TODATE,'"+username+"',SYSDATE from EZLIMITINFO  WHERE INSTID=? AND LIMITID IN(SELECT DISTINCT LIMITID FROM LIMITINFO WHERE LIMIT_RECID = ?)" +
				" AND CURRCODE IN(SELECT CURRCODE FROM LIMITINFO WHERE LIMIT_RECID = ?)";  */
		enctrace("moveToEzlinkProduction :" + moveToEzlinkProduction );
		x = jdbctemplate.update(moveToEzlinkProduction);
		
		return x;   
	}
	
	
	
	public String getMcardNo(String instid, String hcardno, JdbcTemplate jdbctemplate){
		String mcardno = null;
		
		String getMaskedCardNo = "SELECT MCARD_NO FROM CARD_PRODUCTION WHERE ORDER_REF_NO IN"
				+ " (SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH  WHERE HCARD_NO='"+hcardno+"')";
		//String accttypedesc = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID = '"+instid+"'  and  ACCTTYPEID='"+accttypeid+"' and rownum<=1";
		enctrace(" getMaskedCardNo : " + getMaskedCardNo);
		try{
			
			//accttypename  = (String)jdbctemplate.queryForObject(accttypedesc,new Object[]{instid,accttypeid,"1"}, String.class);
			mcardno  = (String)jdbctemplate.queryForObject(getMaskedCardNo, String.class);
			
		}catch(EmptyResultDataAccessException e){}
		return mcardno;
	}
		
	public String  getCinNumber(String instid, StringBuffer hcardno,JdbcTemplate jdbctemplate) {
		String cin = "";
		
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select custid  from EZCARDINFO where CHN='"+hcardno+"' AND INSTID='"+instid+"' ");
		enctrace("valdcardqryNew::"+valdcardqry.toString());  
		cin = (String) jdbctemplate.queryForObject(valdcardqry.toString(), String.class);

	/*	//by gowtham-200819
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select count(1) from EZCARDINFO where CHN=? AND INSTID=? and STATUS in (?,?) ");
		enctrace("valdcardqryNew::"+valdcardqry.toString());  
		validcard = jdbctemplate.queryForInt(valdcardqry.toString(),new Object[]{hcardno,instid,"50","53"});
		*/
		return cin;
	}

	
	public String  getCustomerName(String instid, String cin,JdbcTemplate jdbctemplate) {
		String custName = "";
		
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select name  from EZCUSTOMERINFO  where custid='"+cin+"' AND INSTID='"+instid+"' ");
		enctrace("valdcardqryNew::"+valdcardqry.toString());  
		custName = (String) jdbctemplate.queryForObject(valdcardqry.toString(), String.class);

	/*	//by gowtham-200819
		StringBuilder valdcardqry = new StringBuilder();
		valdcardqry.append("select count(1) from EZCARDINFO where CHN=? AND INSTID=? and STATUS in (?,?) ");
		enctrace("valdcardqryNew::"+valdcardqry.toString());  
		validcard = jdbctemplate.queryForInt(valdcardqry.toString(),new Object[]{hcardno,instid,"50","53"});
		*/
		return custName;
	}

	
}
