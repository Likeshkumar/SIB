package com.ifp.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;


@Transactional
public class RevenueSharingDAO extends BaseAction{

	public List getCustomerListForCommission(String instid, String bin, String crntstatus, String datatype, JdbcTemplate jdbctemplate) {
		String datacond ="";
		if( datatype.equals("$ALL")){
			datacond = "";
		}else if( datatype.equals("$REV")){ 
			datacond = " AND ENTITYFLAG = '$REV' ";
		}else if( datatype.equals("$EXP")){ 
			datacond = " AND ENTITYFLAG = '$EXP' ";
		}
		
		enctrace("Got cntstatus : "+crntstatus);
		String authstatuscond="";
		if( crntstatus.equals("VERIFY")){
			authstatuscond = " AND MKCKSTATUS='M'";
		}else if( crntstatus.equals("VIEW")){
			authstatuscond = "";
		}else if( crntstatus.equals("AUTH")){
			authstatuscond = " AND MKCKSTATUS='P'";
		}
		
		List commissionholders = null;
		String commissionqry = "SELECT RECORDID,CARD_NO, CUSTOMERNAME, ENTITY_AMOUNT, DECODE(SUSPECTEDFLAG,'Y','SUSPECTED','N','ACTIVE',SUSPECTEDFLAG) AS SUSPECTEDFLAG, DECODE(MKCKSTATUS,'M','WAITING FOR VERIFY','P','VERIFIED','D','REJECTED',MKCKSTATUS) AS MKCKSTATUS,  MAKERID,TO_CHAR( MAKERDATE,'dd-mm-yyyy') as MAKERDATE, NVL(CHECKERID,'--') as CHECKERID, NVL(CHECKERDATE,'') AS CHECKERDATE , DECODE(ENTITYFLAG,'$REV','Revenue','$EXP','Expense',ENTITYFLAG) as ENTITYFLAG, NVL(FAILED_REASON,'--') AS FAILED_REASON  FROM IFP_COMMISSION  WHERE INST_ID='"+instid+"' "+authstatuscond+" AND BIN='"+bin+"'"+datacond;
		enctrace("commissionqry :" + commissionqry);
		commissionholders = jdbctemplate.queryForList(commissionqry);
		return commissionholders;
	}
	

	public String getCommissionAmount(String instid, String acctno, String curcode, JdbcTemplate jdbctemplate ) throws Exception {
		String commissionamount = null;
		try {
			String commissionamountqry = "SELECT  COMMISSION_AMOUNT  FROM ACCOUNTINFO WHERE INST_ID='"+instid+"' AND ACCT_NO='"+acctno+"' AND ACCT_CCY='"+curcode+"'" ;
			enctrace("commissionamountqry :" +commissionamountqry);
			commissionamount = (String)jdbctemplate.queryForObject(commissionamountqry, String.class);
		} catch (Exception e) {}
		return commissionamount;
	}
	
	
	public String getExpeseAmount(String instid, String acctno, String curcode, JdbcTemplate jdbctemplate ) throws Exception {
		String expenseamount = null;
		try {
			String expenseamountqry = "SELECT   EXPENSE_AMOUNT  FROM ACCOUNTINFO WHERE INST_ID='"+instid+"' AND ACCT_NO='"+acctno+"' AND ACCT_CCY='"+curcode+"'" ;
			enctrace("expenseamountqry :" +expenseamountqry);
			expenseamount = (String)jdbctemplate.queryForObject(expenseamountqry, String.class);
		} catch (Exception e) {}
		return expenseamount;
	}

 
	
	public int insertCommissionPosting(String instid, String batchid, String recordid, String bin, String cardno, String customername,String entityflag, Double commissionamount, 
			String suspectedflag, String mkckstatus,	String makerid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String commissionpostingqry="INSERT INTO IFP_COMMISSION (INST_ID, BATCHID, RECORDID, BIN, CARD_NO,CUSTOMERNAME,  ENTITYFLAG, ENTITY_AMOUNT,SUSPECTEDFLAG, MKCKSTATUS, MAKERID, MAKERDATE ) VALUES ";
		commissionpostingqry += " ('"+instid+"','"+batchid+"','"+recordid+"','"+bin+"','"+cardno+"','"+customername+"','"+entityflag+"','"+commissionamount+"','"+suspectedflag+"','"+mkckstatus+"','"+makerid+"',SYSDATE) ";
		enctrace("commissionpostingqry :" + commissionpostingqry);
		x = jdbctemplate.update(commissionpostingqry);
		return x; 
	}
	
	 


	public String getRecordIdForRevenuPosting(String instid, String bin, JdbcTemplate jdbctemplate) throws Exception {
	    String recordid = null;
		try {
			String recordidqry ="SELECT REVENEUPOSTSEQ FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND PRD_CODE='"+bin+"'";
			enctrace("recordidqry :" + recordidqry);
			String recordidseq = (String)jdbctemplate.queryForObject(recordidqry, String.class);
			recordid = bin+recordidseq;
		} catch (DataAccessException e) {}
		return recordid;
	}
	
	public int updateRecordId(String instid, String bin, JdbcTemplate jdbctemplate ) throws Exception{
		int x = -1;
		String updqry = "UPDATE PRODUCTINFO SET REVENEUPOSTSEQ=REVENEUPOSTSEQ+1  WHERE INST_ID='"+instid+"' AND PRD_CODE='"+bin+"'";
		enctrace("updqry :" + updqry);
		x = jdbctemplate.update(updqry);
		return x;
	}
	
	public String getBatchId(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		 String batchid = null;
			try {
				String batchidqry ="SELECT BATCHID FROM IFP_COMMISSION_BATCH WHERE INST_ID='"+instid+"' AND BATCH_STATUS='1'";
				enctrace("batchidqry :" + batchidqry);
				batchid = (String)jdbctemplate.queryForObject(batchidqry, String.class); 
			} catch (DataAccessException e) {}
			return batchid;
	}


	public int checkCardExistOnThisBatch(String instid, String batchid,	String cardno, String commissiontype, JdbcTemplate jdbctemplate) throws Exception {
		int existbatch = -1;
		try {
			String existbatchqry = "SELECT COUNT(*) FROM IFP_COMMISSION WHERE INST_ID='"+instid+"' AND BATCHID='"+batchid+"' AND CARD_NO='"+cardno+"' AND ENTITYID='"+commissiontype+"'";
			existbatch = jdbctemplate.queryForInt(existbatchqry);
		} catch (Exception e) {	}
		return existbatch ;
	}
	
	public int updateVerificationStatus(String instid, String recordid, String usercode, String status, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updatestatusqry="UPDATE IFP_COMMISSION  SET MKCKSTATUS='"+status+"', CHECKERID='"+usercode+"', CHECKERDATE=sysdate WHERE INST_ID='"+instid+"' AND RECORDID='"+recordid+"'   ";
		enctrace("updatestatusqry :" + updatestatusqry);
		x = jdbctemplate.update(updatestatusqry);
		return x; 
	}


	public int rejectRevenueSharing(String instid, String recordid,String usercode, String status, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String updatestatusqry="UPDATE IFP_COMMISSION  SET MKCKSTATUS='"+status+"', CHECKERID='"+usercode+"', CHECKERDATE=sysdate WHERE INST_ID='"+instid+"' AND RECORDID='"+recordid+"'   ";
		enctrace("updatestatusqry :" + updatestatusqry);
		x = jdbctemplate.update(updatestatusqry); 
		x = this.deleteFromCommissionSharing(instid,recordid,jdbctemplate);
		return x; 
	}
	
	public int inserIntotHistory(String instid, String recordid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String inserthistoryqry = "INSERT INTO IFP_COMMISSION_HISTORY(INST_ID, BATCHID, RECORDID, BIN, CARD_NO , CUSTOMERNAME, AVAIL_BAL, COMM_AMOUNT, ENTITY_AMOUNT, SUSPECTEDFLAG, MKCKSTATUS, MAKERID, MAKERDATE, CHECKERID, CHECKERDATE, AUTHORIZERID, AUTHDATE, ENTITYFLAG ) ";
		inserthistoryqry += " SELECT INST_ID, BATCHID, RECORDID, BIN, CARD_NO , CUSTOMERNAME, AVAIL_BAL, COMM_AMOUNT, ENTITY_AMOUNT, SUSPECTEDFLAG, MKCKSTATUS, MAKERID, MAKERDATE, CHECKERID, CHECKERDATE, AUTHORIZERID, AUTHDATE,ENTITYFLAG FROM IFP_COMMISSION ";
		inserthistoryqry += " WHERE INST_ID='"+instid+"' AND RECORDID='"+recordid+"'";
		enctrace("inserthistoryqry : "+inserthistoryqry);
		x = jdbctemplate.update(inserthistoryqry);
		
		return x;
	}
	
	public int deleteFromCommissionSharing(String instid, String recordid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String updatestatusqry="DELETE IFP_COMMISSION  WHERE INST_ID='"+instid+"' AND RECORDID='"+recordid+"'   ";
		enctrace("updatestatusqry :" + updatestatusqry);
		x = jdbctemplate.update(updatestatusqry);
		
		x = this.inserIntotHistory(instid, recordid, jdbctemplate);
		
		return x; 
	}

	public int deleteProrcessedCommission(String instid, String recordid, String userid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String authupdqry = "UPDATE IFP_COMMISSION SET AUTHORIZERID='"+userid+"' , AUTHDATE=SYSDATE WHERE INST_ID='"+instid+"' AND RECORDID='"+recordid+"'";
		enctrace("Update auth records : "+authupdqry);
		x = jdbctemplate.update(authupdqry);
		x = deleteFromCommissionSharing(instid, recordid, jdbctemplate);
		return x;
		
	}

	public List getTransactionData(String instid, String recordid,	JdbcTemplate jdbctemplate) throws Exception {
		List txndata = null;
		String txndataqry = "SELECT CARD_NO, BIN, ENTITY_AMOUNT, ENTITYFLAG FROM IFP_COMMISSION WHERE INST_ID='"+instid+"' AND RECORDID='"+recordid+"'";
		enctrace("txndataqry :" + txndataqry );
		txndata= jdbctemplate.queryForList(txndataqry);
		return txndata;
	}
	
	public int updateFailedReason(String instid, String recordid,String failedreason, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String updatefailedqry="UPDATE IFP_COMMISSION  SET FAILED_REASON='"+failedreason+"'  WHERE INST_ID='"+instid+"' AND RECORDID='"+recordid+"'   ";
		enctrace("updatefailedqry:" + updatefailedqry);
		x = jdbctemplate.update(updatefailedqry);		
		return x; 
	}
}
