package com.ifp.dao; 

import java.util.Iterator;
import java.util.List; 
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate; 
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class EODActionDAO extends BaseAction {

	public String getCurrentBusinessDate(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		String curbusinessdate = null;
		try {
			String curbusinessdateqry = "SELECT TO_CHAR(BUSINESSDATE,'DD-MM-YYYY') AS BUSINESSDATE FROM IFP_BUSINESS_MASTERDATE WHERE INST_ID='"+instid+"'" ;
			enctrace("curbusinessdateqry : " + curbusinessdateqry );
			curbusinessdate = (String) jdbctemplate.queryForObject(curbusinessdateqry, String.class);
		} catch (EmptyResultDataAccessException e) {}
		return curbusinessdate;
	}
	
	public String getPreviousBusinessDate(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		String nextbusinessdate = null;  
		try {
			String nextbusinessdateqry = "SELECT TO_CHAR(LAST_BUSINESSDATE,'DD-MM-YYYY') AS BUSINESSDATE FROM IFP_BUSINESS_MASTERDATE WHERE INST_ID='"+instid+"'";
			enctrace("nextbusinessdateqry : " + nextbusinessdateqry );
			nextbusinessdate = (String) jdbctemplate.queryForObject(nextbusinessdateqry, String.class);
		} catch (Exception e) {	}
		return nextbusinessdate;
	}
	
	
	public int updateNextBusinessDate(String instid, String nextbusinessdate, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String nextbusinesqry = "UPDATE IFP_BUSINESS_MASTERDATE SET LAST_BUSINESSDATE=BUSINESSDATE, BUSINESSDATE=TO_DATE('"+nextbusinessdate+"', 'DD-MM-YYYY') WHERE INST_ID='"+instid+"' ";
		enctrace("nextbusinesqry : " + nextbusinesqry );
		x = jdbctemplate.update(nextbusinesqry);
		return x;
	}

	public List getAllSubGlList(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List subgllist = null;
		String subgllistqry = "	SELECT SCH_CODE, SCH_NAME  FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"+instid+"' ";
		enctrace("subgllistqry : " + subgllistqry );
		subgllist = jdbctemplate.queryForList(subgllistqry);
		return subgllist ;
	}
	
	public List getGlTrasaction(String instid, String subglcode, String businessdate, JdbcTemplate jdbctemplate) throws Exception {
		List gltxnlist = null;
		String gltxnlistqry = "SELECT  OPTYPE, TXNAMOUNT   FROM IFP_GL_TXN WHERE INST_ID='"+instid+"' AND SUBGLCODE='"+subglcode+"' AND BUSINESSDATE = to_date('"+businessdate+"', 'dd-mm-yyyy') AND DAYEND_CLOSED='0' AND DAYEND_REFCODE='00'";
		enctrace("gltxnlistqry : " + gltxnlistqry );
		gltxnlist = jdbctemplate.queryForList(gltxnlistqry);
		return gltxnlist;
	}
	
	public int updateDayEndRefNumberToTransaction(String instid, String subglcode, String currentbusinessdate, String dayendrefcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updatedayendref= "UPDATE  IFP_GL_TXN SET DAYEND_REFCODE='"+dayendrefcode+"',  DAYEND_CLOSED='1'   WHERE INST_ID='"+instid+"' AND  BUSINESSDATE = to_date('"+currentbusinessdate+"', 'dd-mm-yyyy') AND SUBGLCODE='"+subglcode+"' AND DAYEND_CLOSED='0' AND DAYEND_REFCODE='00'";
		enctrace("updatedayendref: " + updatedayendref );
		x = jdbctemplate.update(updatedayendref); 
		return x;
	}

	public int updateDayEndRefNumberToSubGl(String instid, String subglcode, String currentbusinessdate, String dayendrefcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1; 
		String updatedayendrefsubgl= "UPDATE  IFP_GL_SUBGLAMOUNT SET DAYEND_REFCODE='"+dayendrefcode+"',  DAYEND_CLOSE='1'   WHERE INST_ID='"+instid+"' AND SUBGL_CODE='"+subglcode+"' AND  BUSINESS_DATE = TO_DATE('"+currentbusinessdate+"', 'DD-MM-YYYY') AND DAYEND_CLOSE='0' ";
		enctrace("updatedayendrefsubgl: " + updatedayendrefsubgl );
		x = jdbctemplate.update(updatedayendrefsubgl); 
		return x;
	}

	public int updateDayEndRefSequance(String instid, String subglcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updatedqyendrefseq_qry = "UPDATE  IFP_GL_SCHEME_MASTER SET  SUBGL_SEQ=SUBGL_SEQ+1  WHERE INST_ID='"+instid+"'  AND SCH_CODE='"+subglcode+"'";
		enctrace("updatedqyendrefseq_qry : " + updatedqyendrefseq_qry );
		x = jdbctemplate.update(updatedqyendrefseq_qry);
		return x;
	}
	
	public List getSubGlAmount(String instid, String subglcode, String curbusinessdate, JdbcTemplate jdbctemplate) throws Exception {
		List gltxnlist = null;
		String gltxnlistqry = "SELECT CR_AMT, DR_AMT, TOTAL_AMT FROM IFP_GL_SUBGLAMOUNT WHERE INST_ID='"+instid+"' AND SUBGL_CODE='"+subglcode+"' AND BUSINESS_DATE = TO_DATE('"+curbusinessdate+"', 'DD-MM-YYYY') AND DAYEND_CLOSE='0'";
		enctrace("gltxnlistqry : " + gltxnlistqry );
		gltxnlist = jdbctemplate.queryForList(gltxnlistqry);
		return gltxnlist;
	}
	
	
	public int insertSettlementAmount(String instid, Double settleamount, String setrefno, String curbusinessdate, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String settlementqry = "INSERT INTO IFP_GL_SETTLEMENT (INST_ID, SETTLE_AMOUNT, SET_REFNO, AUTH_FLAG, PROCESSED_DATE, GENERATED_BY, BUSINESSDATE  ) VALUES ";
		settlementqry +="('"+instid+"','"+settleamount+"','"+setrefno+"','0',SYSDATE,'"+usercode+"',to_date('"+curbusinessdate+"','dd-mm-yyyy'))"; 
		enctrace("settlementqry : " + settlementqry );
		x = jdbctemplate.update(settlementqry);
		return x;
	}

	public synchronized String generateSettleRefNo(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		String setrefno = null;		
		try {
			String setrefnoqry = "SELECT GLSETTLE_SEQNO FROM IFP_SEQUENCE_MASTER  WHERE INST_ID='"+instid+"'";
			String seqno = (String)jdbctemplate.queryForObject(setrefnoqry, String.class);
			setrefno = "SETGL"+seqno;
		} catch (EmptyResultDataAccessException e) { setrefno = null ;} 
		return setrefno;
	}
	
	public String generateDayEndRefNo(String instid, String subglcode, JdbcTemplate jdbctemplate ) throws Exception {
		String glrefno = null;		
		try {
			String setrefnoqry = "SELECT SUBGL_SEQ FROM IFP_GL_SCHEME_MASTER  WHERE INST_ID='"+instid+"' AND SCH_CODE='"+subglcode+"'";
			String seqno = (String)jdbctemplate.queryForObject(setrefnoqry, String.class);
			glrefno= "REF"+subglcode+seqno;
		} catch (EmptyResultDataAccessException e) { glrefno = null ;} 
		return glrefno;
	}
	
	public synchronized  int updateGlSettleSequance(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String nextbusinesqry = "UPDATE  IFP_SEQUENCE_MASTER SET GLSETTLE_SEQNO=GLSETTLE_SEQNO+1  WHERE INST_ID='"+instid+"' ";
		x = jdbctemplate.update(nextbusinesqry);
		return x;
	}

	public int updateSettleRefNumber(String instid, String subglcode, String currentbusinessdate, String setrefno, JdbcTemplate jdbctemplate) throws Exception { 
		int x = -1;
		String updatesubglamtqry = "UPDATE IFP_GL_SUBGLAMOUNT SET SET_REFNO='"+setrefno+"' , DAYEND_CLOSE='1'  WHERE INST_ID='"+instid+"' AND SUBGL_CODE='"+subglcode+"' AND BUSINESS_DATE =TO_DATE('"+currentbusinessdate+"', 'DD-MM-YYYY')";
		enctrace("updatesubglamtqry : " + updatesubglamtqry );
		x = jdbctemplate.update(updatesubglamtqry);
		return x;
	}

	public int insertNewGlAmountRecords(String instid, String nextbusinessdate, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		
		List subgllist = this.getAllSubGlList(instid, jdbctemplate);
		if( !subgllist.isEmpty() ){
			Iterator itr = subgllist.iterator();
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				String subglcode = (String)mp.get("SCH_CODE");
				trace("Checking subgl [ "+subglcode+" ] exist or not....");
				int checkrecexist = checkSubGlExistForBusinessDate(instid, subglcode, nextbusinessdate, jdbctemplate);
				trace("Checking ...got : " + checkrecexist );
				if( checkrecexist == 0 ){
					trace("Inserting new sub-gl-amount rows....");
					x = insertNewGlAmountRows(instid, subglcode, nextbusinessdate, jdbctemplate);
					trace("Inserting new sub-gl-amount rows....got : " + x );
					if( x < 0 ){
						return -1;
					}
				}
			}
		} 
		return 1;
	}
	
	public int checkSubGlExistForBusinessDate( String instid, String subglcode, String nextbusinessdate, JdbcTemplate jdbctemplate ){
		int x = -1;
		String rowxistqry = "SELECT COUNT(*) AS CNT FROM IFP_GL_SUBGLAMOUNT WHERE INST_ID='"+instid+"' AND SUBGL_CODE='"+subglcode+"' AND  BUSINESS_DATE =TO_DATE('"+nextbusinessdate+"', 'DD-MM-YYYY')";
		x = jdbctemplate.queryForInt(rowxistqry);
		return x;
	}
	public int insertNewGlAmountRows(String instid, String subglcode, String nextbusinessdate, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String subglqry = "INSERT INTO IFP_GL_SUBGLAMOUNT  (INST_ID, SUBGL_CODE, CR_AMT, DR_AMT, TOTAL_AMT, BUSINESS_DATE ) " ;
		subglqry += "SELECT INST_ID,SCH_CODE,0.0,0.0,0.0,TO_DATE('"+nextbusinessdate+"', 'DD-MM-YYYY') FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"+instid+"' AND SCH_CODE='"+subglcode+"'";
		enctrace("subgl amount qry : " + subglqry );
		x = jdbctemplate.update(subglqry);
		return x;
	}

	public List getSubGlAmountList(String instid, String subglcode,	String misbusinessdate, JdbcTemplate jdbctemplate) throws Exception {
		List subglamtlist = null;
		String subglamtlistqry = "SELECT CR_AMT, DR_AMT, TOTAL_AMT FROM IFP_GL_SUBGLAMOUNT WHERE INST_ID='"+instid+"' AND  SUBGL_CODE= '"+subglcode+"' AND BUSINESS_DATE=TO_DATE('"+misbusinessdate+"','DD-MM-YYYY')";
		enctrace("subglamtlistqry : " + subglamtlistqry );
		subglamtlist = jdbctemplate.queryForList(subglamtlistqry);
		return subglamtlist ;
	}

	public List subGlTransactionList(String instid, String subgl, String businessdate, JdbcTemplate jdbctemplate ) throws Exception {
		List subgltxnlist;
		String subgltxnlistqry = "SELECT CHN, TXNDESC, TO_CHAR(TRANDATE,'DD-MON-YYYY') AS TRANDATE, TRANTIME, TERMINALID, TERMLOC, MERCHANTID, TXNAMOUNT, OPTYPE FROM IFP_GL_TXN WHERE INST_ID='"+instid+"' AND SUBGLCODE='"+subgl+"' AND BUSINESSDATE=TO_DATE('"+businessdate+"','DD-MM-YYYY') ORDER BY LOCALDATE";
		enctrace(subgltxnlistqry);
		subgltxnlist = jdbctemplate.queryForList(subgltxnlistqry);
		return subgltxnlist ;
	}
	 
	public List subGlTransactionSummery(String instid, String subgl, String businessdate, JdbcTemplate jdbctemplate ) throws Exception {
		List subgltxnsummery;
		String subgltxnsummeryqry = "SELECT SUM(TXNAMOUNT) AS TXNAMOUNT , OPTYPE  FROM IFP_GL_TXN WHERE INST_ID='"+instid+"' AND SUBGLCODE='"+subgl+"' AND BUSINESSDATE=TO_DATE('"+businessdate+"','DD-MM-YYYY') GROUP BY OPTYPE ";
		subgltxnsummery = jdbctemplate.queryForList(subgltxnsummeryqry);
		return subgltxnsummery ;
	}

	public int insertFirstBusinessDate(String instid, String businessdate, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String insertbusinessdateqry = "INSERT INTO IFP_BUSINESS_MASTERDATE (BUSINESSDATE, MAKERTIME, INST_ID, LAST_BUSINESSDATE) VALUES " ;
		insertbusinessdateqry += "(TO_DATE('"+businessdate+"','DD-MM-YYYY'), sysdate, '"+instid+"', TO_DATE('"+businessdate+"','DD-MM-YYYY') )";
		enctrace("insertbusinessdateqry : " + insertbusinessdateqry );
		x = jdbctemplate.update(insertbusinessdateqry);
		return x;
	}

	public int checkAlreadtInstExist(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String businessdateexist = "SELECT COUNT(*) AS CNT FROM IFP_BUSINESS_MASTERDATE WHERE INST_ID='"+instid+"'";
		x = jdbctemplate.queryForInt(businessdateexist);
		return x;
	}

	public int checkBusinessDateValid(String instid, String nextbusinessdate, JdbcTemplate jdbctemplate) {
		int x = -1;
		String validbusinessdate = "SELECT COUNT(*) AS CNT FROM IFP_GL_SUBGLAMOUNT WHERE INST_ID='"+instid+"' AND BUSINESS_DATE=TO_DATE('"+nextbusinessdate+"','dd-mm-yyyy')";
		x = jdbctemplate.queryForInt(validbusinessdate);
		return x;
	}
	 
}
