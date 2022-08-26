package com.ifd.Report;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class CardMaintainReportDAO extends BaseAction {
	
	public List getAuditActionHeadList( JdbcTemplate jdbctemplate ){
		List actionlist = null;
		String actinlistqry = "SELECT AUDIT_ACTIONCODE, AUDIT_ACTIONDESC FROM AUDITRAN_ACTION WHERE HEADACTION='2' ORDER BY ACTORDER ASC ";
		enctrace("actinlistqry  :"+ actinlistqry );
		actionlist = jdbctemplate.queryForList(actinlistqry);
		return actionlist ;
	}
	public List getcardstatus(String instid,JdbcTemplate jdbctemplate) throws Exception{
		List reportlist = null;
		String reportlistqry="";
		
		
		 reportlistqry = "select to_char(count(chn)) as TOTALCARDS,to_char(status)as STATUS  from ezcardinfo group by(status) having count(status)>1";
		
		
		enctrace("getcardstatus :" + reportlistqry  );
		reportlist = jdbctemplate.queryForList(reportlistqry);
		return reportlist ;
	}
	public List getAuditActionList( String headactcode, JdbcTemplate jdbctemplate ){
		List actionlist = null;
		String conditionqry = "";
		if(  !headactcode.equals("$ALL")){
			conditionqry = " WHERE AUDIT_ACTIONCODE LIKE '"+headactcode+"%' AND HEADACTION IS NULL ORDER BY ACTORDER ASC ";
		}else{
			conditionqry = " WHERE HEADACTION IS NULL ORDER BY ACTORDER ASC  ";  
		}
		
		String actinlistqry = "SELECT AUDIT_ACTIONCODE, AUDIT_ACTIONDESC FROM AUDITRAN_ACTION "+conditionqry;
		enctrace("actinlistqry  :"+ actinlistqry );
		actionlist = jdbctemplate.queryForList(actinlistqry);
		return actionlist ;
	}
	   
	public List getAuditReport(String instid,String subcond, String loginuser, String querycondition, String orderbycondition, JdbcTemplate jdbctemplate) throws Exception{
		List reportlist = null;
		String reportlistqry="";
		
		/*if(subcond.equals("")){
			 reportlistqry = "SELECT  CIN as CustomerID, EMB_NAME as Name,NVL(ORG_CHN,'--') AS CARDNO,ACCOUNT_NO AS ACCNO,CARD_COLLECT_BRANCH as BRANCHCODE, NVL( USERCODE, '--' ) AS USERCODE , NVL( CHECKERID, '--' ) AS CHECKERID, TO_CHAR(ACTIONDATE,'DD-MM-YYYY hh:mi:ss') AS ACTIONDATE,AUDITMSG, NVL(REMARKS, '--') AS REMARKS, NVL(BIN, NVL(APPLICATIONID, NVL(PREFILE_NAME, '--')) ) FROM AUDITRAN WHERE INST_ID='"+instid+"' " +querycondition+orderbycondition;

		}else{*/
		 reportlistqry = "SELECT  CIN as CustomerID, CUSTNAME as Name,NVL(CARDNO,'--') AS CARDNO,ACCNO,BRANCHCODE, NVL( USERCODE, '--' ) AS USERCODE , NVL( CHECKERID, '--' ) AS CHECKERID, TO_CHAR(ACTIONDATE,'DD-MM-YYYY hh:mi:ss') AS ACTIONDATE,AUDITMSG, NVL(REMARKS, '--') AS REMARKS, NVL(BIN, NVL(APPLICATIONID, NVL(PREFILE_NAME, '--')) ) as bin FROM AUDITRAN WHERE INST_ID='"+instid+"' " +querycondition+orderbycondition;
		//}
		
	   enctrace("reportlistqry :" + reportlistqry  );
		reportlist = jdbctemplate.queryForList(reportlistqry);
		return reportlist ;
	}
	
	public List getsummary(String instid,String branch,String condi1,String condi2,String condi3,JdbcTemplate jdbctemplate) throws Exception{
		List reportlist = null;
		String reportlistqry="";
		
		/*if(subcond.equals("")){
			 reportlistqry = "SELECT  CIN as CustomerID, EMB_NAME as Name,NVL(ORG_CHN,'--') AS CARDNO,ACCOUNT_NO AS ACCNO,CARD_COLLECT_BRANCH as BRANCHCODE, NVL( USERCODE, '--' ) AS USERCODE , NVL( CHECKERID, '--' ) AS CHECKERID, TO_CHAR(ACTIONDATE,'DD-MM-YYYY hh:mi:ss') AS ACTIONDATE,AUDITMSG, NVL(REMARKS, '--') AS REMARKS, NVL(BIN, NVL(APPLICATIONID, NVL(PREFILE_NAME, '--')) ) FROM AUDITRAN WHERE INST_ID='"+instid+"' " +querycondition+orderbycondition;

		}else{*/
		 reportlistqry = "SELECT TO_CHAR(COUNT(DISTINCT a.card_collect_branch))as InProduction, TO_CHAR(COUNT(DISTINCT b.card_collect_branch))as WaitingForCustMap,TO_CHAR(COUNT(DISTINCT c.card_collect_branch))as WaitingCustMapAuth FROM CARD_PRODUCTION a ,INST_CARD_PROCESS b,INST_CARD_PROCESS c  where a.card_collect_branch='"+branch+"' "+ condi1+" AND    b.card_collect_branch='"+branch+"'    and b.card_status='05' and b.mkck_status='P'  "+ condi2+" AND   c.card_collect_branch='"+branch+"'    and c.card_status='06' and c.mkck_status='M'"+ condi3+" ";
		//}
		
		enctrace("getsummaryinstant :" + reportlistqry  );
		reportlist = jdbctemplate.queryForList(reportlistqry);
		return reportlist ;
	}
	
	public List Renewalreport(String instid,String querycondition,String orderbycondition, JdbcTemplate jdbctemplate) throws Exception{
		List renreportlist = null;
		//String renreportlistqry = "SELECT  TO_CHAR(ACTIONDATE,'DD-MM-YYYY hh:mi:ss') AS ACTIONDATE, NVL(CARDNO,'--') AS CARDNO, NVL( USERCODE, '--' ) AS USERCODE , AUDITMSG,  NVL(REMARKS, '--') AS REMARKS, NVL(BIN, NVL(APPLICATIONID, NVL(PREFILE_NAME, '--')) ) FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' " +querycondition+orderbycondition;
		String renreportlistqry = "SELECT  TO_CHAR(EXPIRY_DATE,'DD-MM-YYYY hh:mi:ss') AS EXPIRYDATE, NVL(CARD_NO,'--') AS CARDNO, NVL( MAKER_ID, '--' ) AS USERCODE , EMB_NAME,  ORDER_REF_NO, PRE_FILE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' " +querycondition+orderbycondition;
		//enctrace("reportlistqry :" + renreportlistqry  );
		renreportlist = jdbctemplate.queryForList(renreportlistqry);
		return renreportlist ;
		
	}
	
	
}
