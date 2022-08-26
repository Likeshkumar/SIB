package com.ifp.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class dashboardActionDAO extends BaseAction{
	
	/*public List getCashInoutMerchantList(String instid, String rowcount, JdbcTemplate jdbctemplate ){
		List cashinoutlist = null;
		String cashinoutqry = "SELECT MERCHANTID, VAS_AMOUNT FROM EZMMS_VASPLTXN  WHERE ROWID IN (SELECT MAX(ROWID) FROM  EZMMS_VASPLTXN WHERE TXNDESC='DEPOSIT'  GROUP BY MERCHANTID, TXNDESC ) AND ROWNUM<="+rowcount;
		System.out.println("cashinoutqry : " + cashinoutqry );
		cashinoutlist = jdbctemplate.queryForList(cashinoutqry); 
		return cashinoutlist;
		
	}*/
	public List getMaxSalesMerchant(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List txnamtlist = null;
		String maxsaleqry = "SELECT MERCHANTID, TXNTYPE, TXNCONVRATE, TXNCURRENCY, TXNAMOUNT FROM EZMMS_TRANSACTION WHERE INSTID='"+instid+"' AND SET_FILE_GENERATED='N' ";
		System.out.println("maxsaleqry : " + maxsaleqry );
		txnamtlist = jdbctemplate.queryForList(maxsaleqry); 
		return txnamtlist;
	}
	
	public int getListOfTxnByCount(String instid, String merchantid, JdbcTemplate jdbctemplate) throws Exception  {
		int txnamtlistcnt = -1;
		String maxcnteqry = "SELECT COUNT(*) AS CNT FROM EZMMS_TRANSACTION WHERE INSTID='"+instid+"' AND MERCHANTID='"+merchantid+"' AND SET_FILE_GENERATED='N'  GROUP BY MERCHANTID";
		System.out.println("maxsaleqry : " + maxcnteqry );
		txnamtlistcnt = jdbctemplate.queryForInt(maxcnteqry);
		return txnamtlistcnt;
	}
	
	
	public int getCompliantCount(String instid, JdbcTemplate jdbctemplate)  throws Exception {
		int compcn = -1;
		String compcntqry = "SELECT COUNT(*) AS CNT FROM EZMMS_COMPLIANTS WHERE INST_ID='"+instid+"' AND STATUS='O'";
		enctrace("compcntqry : " + compcntqry );
		compcn = jdbctemplate.queryForInt(compcntqry);
		return compcn;
	}
 
	public List getTransactionTxnCodeWise(String instid, JdbcTemplate jdbctemplate)  throws Exception  {
		List txnamtlist = null;
		String maxsaleqry = "SELECT SUM(TXNAMOUNT) AS TXNAMOUNT, TXNCODE FROM EZMMS_TRANSACTION WHERE INSTID='"+instid+"' AND SET_FILE_GENERATED='N'   GROUP BY TXNCODE  ";
		System.out.println("maxsaleqry : " + maxsaleqry );
		txnamtlist = jdbctemplate.queryForList(maxsaleqry); 
		return txnamtlist;
	}
	
	public List getTrandacodeList(String instid, JdbcTemplate jdbctemplate )  throws Exception  {
		List txncodelist = null;
		String txncodelistqry = "SELECT DISTINCT TXNCODE FROM EZMMS_TRANSACTION WHERE INSTID='"+instid+"' AND SET_FILE_GENERATED='N' ";
		System.out.println("txncodelistqry : " + txncodelistqry );
		txncodelist = jdbctemplate.queryForList(txncodelistqry); 
		return txncodelist;
	}
	
	public String getMaxDateTxn(String instid, String txncode, JdbcTemplate jdbctemplate )  throws Exception  {
		String maxdate = null;
		try {
			String maxdateqry = "SELECT TO_CHAR( MAX(TRANDATE), 'DD-MON-YYYY' ) AS TRANDATE FROM EZMMS_TRANSACTION WHERE INSTID='"+instid+"' AND TXNCODE='"+txncode+"' AND SET_FILE_GENERATED='N' ";
			System.out.println("maxdateqry : " + maxdateqry );
			maxdate = (String)jdbctemplate.queryForObject(maxdateqry, String.class);
		} catch (EmptyResultDataAccessException e) {} 
		return maxdate;
	}
	
	public String getMaxTimeTxn(String instid, String txncode, String txndate, JdbcTemplate jdbctemplate )  throws Exception  {
		String maxtime = null;
		try {
			String maxtimeqry = "SELECT MAX(TRANTIME) FROM EZMMS_TRANSACTION WHERE INSTID='"+instid+"' AND TXNCODE='"+txncode+"' AND  TRANDATE = TO_DATE('"+txndate+"', 'DD-MON-YYYY') AND SET_FILE_GENERATED='N' ";
			System.out.println("maxtimeqry : " + maxtimeqry );
			maxtime = (String)jdbctemplate.queryForObject(maxtimeqry, String.class);
		} catch (EmptyResultDataAccessException e) {} 
		return maxtime;
	}
	
	public List getLastTransactionAmount(String instid, String txncode, String txndate, String trantime, JdbcTemplate jdbctemplate )  throws Exception  {
		List txnamount = null;
		try {
			String txnamountqry = "SELECT TXNAMOUNT, MERCHANTID FROM EZMMS_TRANSACTION WHERE INSTID='"+instid+"' AND TXNCODE='"+txncode+"' AND  TRANDATE = TO_DATE('"+txndate+"', 'DD-MON-YYYY') AND TRANTIME='"+trantime+"' AND SET_FILE_GENERATED='N' ";
			System.out.println("txnamountqry : " + txnamountqry );
			txnamount = jdbctemplate.queryForList(txnamountqry);
		} catch (EmptyResultDataAccessException e) {} 
		return txnamount;
	}
	
}
