package com.ifd.fee;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

import test.Date;

@Transactional
public class FeeConfigDAO extends BaseAction 
{ 
	public String getFeeMaintainConfig1(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		String feedesc = null;
		try {
			String feedescqry= "SELECT CARD_ACT_CODE,STATUS_DESC FROM MAINTAIN_DESC WHERE FEE_REQ='1' AND INST_ID='"+instid+"'";
			enctrace("feedescqry : " + feedescqry );
			feedesc = (String)jdbctemplate.queryForObject(feedescqry,String.class);
		} catch (EmptyResultDataAccessException e) { feedesc=null; }
		return feedesc;
	}
	
/*	
	public String updateAuthFee(String authstatus,String userid,String instid,String feeid) {
		String update_authdeauth_qury = "UPDATE FEE_DESC SET AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND FEE_CODE='"+feeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	
	public String updateAuthFeeMaster(String authstatus,String instid,String feeid) {
		String update_authdeauth_qury = "UPDATE FEE_MASTER SET AUTH_CODE='"+authstatus+"'WHERE INST_ID='"+instid+"' AND FEE_ID='"+feeid+"'";
		enctrace(" updateAuthFeeMaster  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	
	
	public String updateDeAuthFee(String authstatus,String userid,String remarks,String instid,String feeid) {
		String update_authdeauth_qury = "UPDATE FEE_DESC SET AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND FEE_CODE='"+feeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	
	
	public List getFeeMaintainConfig(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		String feedescqry= "SELECT TXN_CODE,ACTION_DESC FROM ACTIONCODES WHERE FEE_REQ='1' AND INST_ID='"+instid+"'";
		enctrace("feedescqry : " + feedescqry );
		subfeelist = jdbctemplate.queryForList(feedescqry);
		return subfeelist;
	}     
	
	public List getFeeAuthListValue(String instid,String masterfeecode,String feecode, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID='"+instid+"' AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, "); 
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='"+instid+"' AND TXN_CODE =TXNCODE)TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='"+instid+"' AND FEE_CODE =FEE_ID)FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID='"+feecode+"' AND INST_ID='"+instid+"'");
		//sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND FEE_MASTER.FEE_ID='"+feecode+"' AND FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		enctrace("getFeeAuthListValue : " + sb.toString() );
		subfeelist = jdbctemplate.queryForList(sb.toString());
		return subfeelist;
	}
	
	//-----------Added by sardar on 14-12-15-------//////
	
	public List getfeevalues(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List Feedesc = null;
		String feedescqry= "SELECT FEE_DESC,FEE_CODE from FEE_DESC WHERE INST_ID='"+instid+"'and AUTH_CODE=1";
		enctrace("feedescqry : " + feedescqry );
		Feedesc = jdbctemplate.queryForList(feedescqry);
		return Feedesc;
	}   

		
	
	public List getFeeAuthListValue1(String instid,String mfeecode, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID='"+instid+"' AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, "); 
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='"+instid+"' AND TXN_CODE =TXNCODE)TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='"+instid+"' AND FEE_CODE =FEE_ID)FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID='"+mfeecode+"' AND INST_ID='"+instid+"'");
		//sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND FEE_MASTER.FEE_ID='"+feecode+"' AND FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		enctrace("getFeeAuthListValue : " + sb.toString() );
		subfeelist = jdbctemplate.queryForList(sb.toString());
		return subfeelist;
	}
	public List getFeeAuthListValue2(String instid,String mfeecode1, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID='"+instid+"' AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, "); 
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='"+instid+"' AND TXN_CODE =TXNCODE)TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='"+instid+"' AND FEE_CODE =FEE_ID)FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID='"+mfeecode1+"' AND INST_ID='"+instid+"'");
		//sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND FEE_MASTER.FEE_ID='"+feecode+"' AND FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		enctrace("getFeeAuthListValue : " + sb.toString() );
		subfeelist = jdbctemplate.queryForList(sb.toString());
		return subfeelist;
	}
	

	//-----------Ended by sardar on 14-12-15-------//////
	
	 
	
	public List getFeeAuthList(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		String feedescqry= "select FEE_CODE,FEE_DESC from FEE_DESC where AUTH_CODE='0' and INST_ID='"+instid+"'";
		enctrace("getFeeAuthList : " + feedescqry );
		subfeelist = jdbctemplate.queryForList(feedescqry);
		return subfeelist;
	}
	
	
	
	public List getInstCurrency(String numericode, JdbcTemplate jdbctemplate ) throws Exception{
		List instcurr = null;
		String instcurrQry= "select NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE='"+numericode+"'";
		enctrace("instcurrQry : " + instcurrQry );
		instcurr = jdbctemplate.queryForList(instcurrQry);
		return instcurr;
	} 
	
	public List getInstCurrency1(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List instcurr = null;
		String instcurrQry= "select NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE in (select BASE_CURRENCY from INSTITUTION where INST_ID='"+instid+"') and rownum=1";
		enctrace("instcurrQry : " + instcurrQry );
		instcurr = jdbctemplate.queryForList(instcurrQry);
		return instcurr;
	} 
	
	
	public int insertFeeList(String feeid,String instid, String currcode,  String txncode, String feeamt, String authcode, JdbcTemplate jdbctemplate) 
	{
		int x = -1;
		StringBuilder insertfee = new StringBuilder();
		insertfee.append("INSERT INTO FEE_MASTER ");
		insertfee.append("(INST_ID,FEE_ID, CURR_CODE, TXNCODE, FEEAMT, AUTH_CODE) ");
		insertfee.append("VALUES ");
		insertfee.append("('"+instid+"', '"+feeid+"', '"+currcode+"', '"+txncode+"', '"+feeamt+"', '"+authcode+"' )");
		enctrace("insertfee  :"+ insertfee.toString()  );
		x = jdbctemplate.update(insertfee.toString());
		return x;
		
		    
	}
	
	public int insertFeeDesc(String feeid,String instid, String status,String usercode,String feename, String authcode, JdbcTemplate jdbctemplate) 
	{
		int x = -1;
		StringBuilder insertfee = new StringBuilder();
		insertfee.append("INSERT INTO FEE_DESC ");
		insertfee.append("(INST_ID, FEE_CODE, STATUS_FLAG, USER_NAME, INTRODATE, FEE_DESC, AUTH_CODE) ");
		insertfee.append("VALUES ");
		insertfee.append("('"+instid+"', '"+feeid+"', '"+status+"', '"+usercode+"',sysdate, '"+feename+"', '"+authcode+"' )");
		enctrace("insertFeeDesc  :"+ insertfee.toString()  );
		x = jdbctemplate.update(insertfee.toString());
		return x;
		
		    
	}
	
	//-----------------------------------------UPDATE METHOD CREATED BY SARDAR ON 12-12-15------------------------//
	
	

                              
	
	
	public int updatefeeDesc(String feeAmt,String instid1,String curcode, String TXN_CODE,String Feeid,String authcode, JdbcTemplate jdbctemplate) 
	{
		
		
		int x = -1;
		StringBuilder updatefeeconfig = new StringBuilder();
	
		
	//	updateqry="UPDATE ACCTTYPE SET ACCTTYPEDESC='"+accounttypename+"',MKCK_STATUS='"+mkchkrstatus+"', AUTH_CODE='"+authcode+"',ADDEDBY='"+usercode+"',ADDED_DATE=sysdate WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accttypeid+"'";

		updatefeeconfig.append("update FEE_MASTER set FEEAMT='"+feeAmt+"',auth_code='"+authcode+"' where INST_ID='"+instid1+"' and CURR_CODE='"+curcode+"'  and  TXNCODE='"+TXN_CODE+"' and FEE_ID='"+Feeid+"'"  );
		
	//	update FEE_DESC  set AUTH_CODE='1',CHECKER_DATE=sysdate  where INST_ID='ORBL' and FEE_CODE='9'and FEE_DESC='test123';

		
		enctrace("UpdateFeeDesc  :"+ updatefeeconfig.toString()  );
		x = jdbctemplate.update(updatefeeconfig.toString());
		return x;
		
		    
	}
	
	public int UPDATEFeeList(String instid, String authcode,String Feeid,JdbcTemplate jdbctemplate) 
	{
		int x = -1;
		StringBuilder updatefeelist1 = new StringBuilder();
		updatefeelist1.append("UPDATE FEE_DESC SET INTRODATE=sysdate,auth_code='"+authcode+"' where   INST_ID='"+instid+"' and FEE_CODE='"+Feeid+"'");
		
		enctrace("insertfee  :"+ updatefeelist1.toString()  );
		x = jdbctemplate.update(updatefeelist1.toString());
		return x;
		
		    
	}
	
	*/

	
	public String updateAuthFee(String authstatus,String userid,String instid,String feeid) {
		
		String update_authdeauth_qury = "UPDATE FEE_DESC SET AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND FEE_CODE='"+feeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);

	/*	//by gowtham210819
		String update_authdeauth_qury = "UPDATE FEE_DESC SET AUTH_CODE=?,CHECKER_DATE=?,CHECKER_CHECKER_ID=? WHERE INST_ID=? AND FEE_CODE=?";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);*/
		
		return update_authdeauth_qury;
	}
	
	public String updateAuthFeeMaster(String authstatus,String instid,String feeid) {
		
		String update_authdeauth_qury = "UPDATE FEE_MASTER SET AUTH_CODE='"+authstatus+"'WHERE INST_ID='"+instid+"' AND FEE_ID='"+feeid+"'";
		enctrace(" updateAuthFeeMaster  === > "+update_authdeauth_qury);
	/*	
		//by gowtham210819
		String update_authdeauth_qury = "UPDATE FEE_MASTER SET AUTH_CODE=? WHERE INST_ID=? AND FEE_ID=? ";
		enctrace(" updateAuthFeeMaster  === > "+update_authdeauth_qury);*/
		
		return update_authdeauth_qury;
	}
	
	
	public String updateDeAuthFee(String authstatus,String userid,String remarks,String instid,String feeid) {
		
		String update_authdeauth_qury = "UPDATE FEE_DESC SET AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND FEE_CODE='"+feeid+"'";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);
		
	/*	//by gowtham-210819
		String update_authdeauth_qury = "UPDATE FEE_DESC SET AUTH_CODE=?,CHECKER_DATE=?,CHECKER_CHECKER_ID=? ,REMARKS=? WHERE INST_ID=? AND FEE_CODE=?";
		enctrace(" update_authdeauth_qury  === > "+update_authdeauth_qury);*/
		
		return update_authdeauth_qury;
	}
	
	
	public List getFeeMaintainConfig(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		
		/*String feedescqry= "SELECT TXN_CODE,ACTION_DESC FROM ACTIONCODES WHERE FEE_REQ='1' AND INST_ID='"+instid+"'";
		enctrace("feedescqry : " + feedescqry );
		subfeelist = jdbctemplate.queryForList(feedescqry);*/
		
		//BY GOWHTHAM200819
		String feedescqry= "SELECT TXN_CODE,ACTION_DESC FROM ACTIONCODES WHERE FEE_REQ=? AND INST_ID=?";
		enctrace("feedescqry : " + feedescqry );
		
		subfeelist = jdbctemplate.queryForList(feedescqry,new Object[]{"1",instid});
		return subfeelist;
	}     
	
	public List getFeeAuthListValue(String instid,String masterfeecode,String feecode, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID='"+instid+"' AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, "); 
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='"+instid+"' AND TXN_CODE =TXNCODE)TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='"+instid+"' AND FEE_CODE =FEE_ID)FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID='"+feecode+"' AND INST_ID='"+instid+"'");
		//sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND FEE_MASTER.FEE_ID='"+feecode+"' AND FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		enctrace("getFeeAuthListValue : " + sb.toString() );
		subfeelist = jdbctemplate.queryForList(sb.toString());
		return subfeelist;
	}
	
	//-----------Added by sardar on 14-12-15-------//////
	
	public List getfeevalues(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List Feedesc = null;
		
		/*String feedescqry= "SELECT FEE_DESC,FEE_CODE from FEE_DESC WHERE INST_ID='"+instid+"'and AUTH_CODE=1";
		enctrace("feedescqry : " + feedescqry );
		Feedesc = jdbctemplate.queryForList(feedescqry);*/
		
		//by gowtham-210819
		String feedescqry= "SELECT FEE_DESC,FEE_CODE from FEE_DESC WHERE INST_ID=? and AUTH_CODE=? ";
		enctrace("feedescqry : " + feedescqry );
		Feedesc = jdbctemplate.queryForList(feedescqry,new Object[]{instid,"1"});
		
		return Feedesc;
	}   

		
	
	public List getFeeAuthListValue1(String instid,String mfeecode, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		StringBuilder sb = new StringBuilder();
		
		/*sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID='"+instid+"' AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, "); 
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='"+instid+"' AND TXN_CODE =TXNCODE)TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='"+instid+"' AND FEE_CODE =FEE_ID)FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID='"+mfeecode+"' AND INST_ID='"+instid+"'");
		//sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND FEE_MASTER.FEE_ID='"+feecode+"' AND FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		enctrace("getFeeAuthListValue : " + sb.toString() );
		subfeelist = jdbctemplate.queryForList(sb.toString());*/
		
		// by gowtham-210819
		sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID=? AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, "); 
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID=? AND TXN_CODE =TXNCODE)TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID=? AND FEE_CODE =FEE_ID)FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID=? AND INST_ID=? ");
		//sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND FEE_MASTER.FEE_ID='"+feecode+"' AND FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		enctrace("getFeeAuthListValue : " + sb.toString() );
		subfeelist = jdbctemplate.queryForList(sb.toString(),new Object[]{instid,instid,instid,mfeecode,instid});
		
		return subfeelist;
	}
	public List getFeeAuthListValue2(String instid,String mfeecode1, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID='"+instid+"' AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, "); 
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='"+instid+"' AND TXN_CODE =TXNCODE)TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='"+instid+"' AND FEE_CODE =FEE_ID)FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID='"+mfeecode1+"' AND INST_ID='"+instid+"'");
		//sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND FEE_MASTER.FEE_ID='"+feecode+"' AND FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		enctrace("getFeeAuthListValue : " + sb.toString() );
		subfeelist = jdbctemplate.queryForList(sb.toString());
		return subfeelist;
	}
	

	//-----------Ended by sardar on 14-12-15-------//////
	
	 
	
	public List getFeeAuthList(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List subfeelist = null;
		
		/*String feedescqry= "select FEE_CODE,FEE_DESC from FEE_DESC where AUTH_CODE='0' and INST_ID='"+instid+"'";
		enctrace("getFeeAuthList : " + feedescqry );
		subfeelist = jdbctemplate.queryForList(feedescqry);*/
		
		//by gowtham-200819
		String feedescqry= "select FEE_CODE,FEE_DESC from FEE_DESC where AUTH_CODE=? and INST_ID=?";
		enctrace("getFeeAuthList : " + feedescqry );
		subfeelist = jdbctemplate.queryForList(feedescqry,new Object[]{"0",instid});
		
		return subfeelist;
	}
	
	
	
	public List getInstCurrency(String numericode, JdbcTemplate jdbctemplate ) throws Exception{
		List instcurr = null;
		
		String instcurrQry= "select NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE='"+numericode+"'";
		enctrace("instcurrQry : " + instcurrQry );
		instcurr = jdbctemplate.queryForList(instcurrQry);

		/*
		//by gowtham200819
		String instcurrQry= "select NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE?";
		enctrace("instcurrQry : " + instcurrQry );
		instcurr = jdbctemplate.queryForList(instcurrQry,new Object[]{numericode});
		*/
		return instcurr;
	} 
	
	public List getInstCurrency1(String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List instcurr = null;
		String instcurrQry= "select NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE in (select BASE_CURRENCY from INSTITUTION where INST_ID='"+instid+"') and rownum=1";
		enctrace("instcurrQry : " + instcurrQry );
		instcurr = jdbctemplate.queryForList(instcurrQry);
		return instcurr;
	} 
	
	
	public int insertFeeList(String feeid,String instid, String currcode,  String txncode, String feeamt, String authcode, JdbcTemplate jdbctemplate) 
	{
		int x = -1;
		StringBuilder insertfee = new StringBuilder();
		insertfee.append("INSERT INTO FEE_MASTER ");
		insertfee.append("(INST_ID,FEE_ID, CURR_CODE, TXNCODE, FEEAMT, AUTH_CODE) ");
		insertfee.append("VALUES ");
		
		/*insertfee.append("('"+instid+"', '"+feeid+"', '"+currcode+"', '"+txncode+"', '"+feeamt+"', '"+authcode+"' )");
		enctrace("insertfee  :"+ insertfee.toString()  );
		x = jdbctemplate.update(insertfee.toString());*/
		
		//by gowtham-200819
		insertfee.append("(?,?,?,?,?,? )");
		enctrace("insertfee  :"+ insertfee.toString()  );
		x = jdbctemplate.update(insertfee.toString(),new Object[]{instid,feeid,currcode,txncode,feeamt,authcode});
		
		return x;
		
		    
	}
	
	public int insertFeeDesc(String feeid,String instid, String status,String usercode,String feename, String authcode, JdbcTemplate jdbctemplate) 
	{
		Date date =  new Date();
		int x = -1;
		StringBuilder insertfee = new StringBuilder();
		insertfee.append("INSERT INTO FEE_DESC ");
		insertfee.append("(INST_ID, FEE_CODE, STATUS_FLAG, USER_NAME, INTRODATE, FEE_DESC, AUTH_CODE) ");
		insertfee.append("VALUES ");
		
		/*insertfee.append("('"+instid+"', '"+feeid+"', '"+status+"', '"+usercode+"',sysdate, '"+feename+"', '"+authcode+"' )");
		enctrace("insertFeeDesc  :"+ insertfee.toString()  );
		x = jdbctemplate.update(insertfee.toString());*/
		
		//by gowtham200819
		insertfee.append("(?,?,?,?,?,?,? )");
		enctrace("insertFeeDesc  :"+ insertfee.toString()  );
		x = jdbctemplate.update(insertfee.toString(),new Object[]{instid,feeid,status,usercode, date.getCurrentDate(),feename,authcode});
		
		return x;
		
		    
	}
	
	//-----------------------------------------UPDATE METHOD CREATED BY SARDAR ON 12-12-15------------------------//
	
	

                              
	
	
	public int updatefeeDesc(String feeAmt,String instid1,String curcode, String TXN_CODE,String Feeid,String authcode, JdbcTemplate jdbctemplate) 
	{
		
		
		int x = -1;
		StringBuilder updatefeeconfig = new StringBuilder();
	
		
	//	updateqry="UPDATE ACCTTYPE SET ACCTTYPEDESC='"+accounttypename+"',MKCK_STATUS='"+mkchkrstatus+"', AUTH_CODE='"+authcode+"',ADDEDBY='"+usercode+"',ADDED_DATE=sysdate WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+accttypeid+"'";

		
		//by gowtham210819
		//updatefeeconfig.append("update FEE_MASTER set FEEAMT='"+feeAmt+"',auth_code='"+authcode+"' where INST_ID='"+instid1+"' and CURR_CODE='"+curcode+"'  and  TXNCODE='"+TXN_CODE+"' and FEE_ID='"+Feeid+"'"  );
		updatefeeconfig.append("update FEE_MASTER set FEEAMT=? ,auth_code=? where INST_ID=? and CURR_CODE=?  and  TXNCODE=? and FEE_ID=? "  );
		
		
		
	//	update FEE_DESC  set AUTH_CODE='1',CHECKER_DATE=sysdate  where INST_ID='ORBL' and FEE_CODE='9'and FEE_DESC='test123';

		
		enctrace("UpdateFeeDesc  :"+ updatefeeconfig.toString()  );
		x = jdbctemplate.update(updatefeeconfig.toString(),new Object[]{feeAmt,authcode,instid1,curcode,TXN_CODE,Feeid});
		return x;
		
		    
	}
	
	public int UPDATEFeeList(String instid, String authcode,String Feeid,JdbcTemplate jdbctemplate) 
	{
		int x = -1;
		StringBuilder updatefeelist1 = new StringBuilder();
		
		/*updatefeelist1.append("UPDATE FEE_DESC SET INTRODATE=sysdate,auth_code='"+authcode+"' where   INST_ID='"+instid+"' and FEE_CODE='"+Feeid+"'");
		enctrace("insertfee  :"+ updatefeelist1.toString()  );
		x = jdbctemplate.update(updatefeelist1.toString());*/
		
		//by gowtham210819
		updatefeelist1.append("UPDATE FEE_DESC SET INTRODATE=sysdate,auth_code=? where   INST_ID=? and FEE_CODE=? ");
		enctrace("insertfee  :"+ updatefeelist1.toString()  );
		x = jdbctemplate.update(updatefeelist1.toString(),new Object[]{authcode,instid,Feeid});
		
		return x;
		
		    
	}
	
	
	//-----------------------------------------UPDATE METHOD END BY SARDAR ON 12-12-15------------------------//
	
}//end class
 