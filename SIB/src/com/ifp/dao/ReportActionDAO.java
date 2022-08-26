package com.ifp.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class ReportActionDAO extends BaseAction {
	
	public List getAuditActionHeadList( JdbcTemplate jdbctemplate ){
		List actionlist = null;
		String actinlistqry = "SELECT AUDIT_ACTIONCODE, AUDIT_ACTIONDESC FROM AUDITRAN_ACTION WHERE HEADACTION='1' ORDER BY ACTORDER ASC ";
		/*enctrace("actinlistqry  :"+ actinlistqry );*/
		trace("actinlistqry  :");
		actionlist = jdbctemplate.queryForList(actinlistqry);
		return actionlist ;
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
		/*enctrace("actinlistqry  :"+ actinlistqry );*/
		trace("actinlistqry2====>  :");
		actionlist = jdbctemplate.queryForList(actinlistqry);
		return actionlist ;
	}
	   
	public List getAuditReport(String instid, String loginuser, String querycondition, String orderbycondition, JdbcTemplate jdbctemplate) throws Exception{
		List reportlist = null;
		String reportlistqry = "SELECT decode(CIN,'null','--',CIN)AS CIN,decode(CUSTNAME,'null','--',CUSTNAME)AS CUSTNAME,NVL(CARDNO,'--') AS CARDNO,decode(ACCNO,'null','--',ACCNO)AS ACCNO,BRANCHCODE,decode(USERCODE,'null','--',USERCODE)AS USERCODE,decode(CHECKERID,'null','--',CHECKERID)AS CHECKERID,TO_CHAR(ACTIONDATE,'DD-MM-YYYY hh:mi:ss') AS ACTIONDATE   , AUDITMSG,  NVL(REMARKS, '--') AS REMARKS, NVL(BIN, NVL(APPLICATIONID, NVL(PREFILE_NAME, '--')) ) FROM AUDITRAN WHERE INST_ID='"+instid+"' " +querycondition+orderbycondition;
		/*enctrace("reportlistqry :" + reportlistqry  );*/
		trace("reportlistqry1====> : "+reportlistqry);
		reportlist = jdbctemplate.queryForList(reportlistqry);
		return reportlist ;
	}
	
	
}
