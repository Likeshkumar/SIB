package com.ifp.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class AcctRuleDAO extends BaseAction 
{ 
	
	 
	private static final long serialVersionUID = -8376161637970676446L;
	
	 
	public List getAuthList(JdbcTemplate jdbctemplate, HttpSession session){
		List authlist = null;
		try {
			String authlilstqry = "SELECT AUTH_ID, AUTH_NAME FROM IFP_ACCT_AUTHLIST WHERE STATUS='1'";
			System.out.println("authlilstqry--" +authlilstqry);
			authlist = jdbctemplate.queryForList(authlilstqry);
			 
		} catch (DataAccessException e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
			 
		}
		return authlist;
	}
	
	
	public List getAccountRuleData(String instid, String acctruleid, JdbcTemplate jdbctemplate, HttpSession session){
		List acct_list = null;
		try {
			String accrule = "SELECT * FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND ACCOUNTRULECODE='"+acctruleid+"'";
			System.out.println("accrule---> "+accrule);
			acct_list = jdbctemplate.queryForList(accrule);
			 
		} catch (DataAccessException e) { 
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", e.toString());
			e.printStackTrace(); 
		}
		return acct_list;
	}
	
	
	public int checkAcctRuleCodeExist(String instid, String acctrulecode, JdbcTemplate jdbctemplate, HttpSession session){
		int x=-1;
		try {
			String acctruleqry = "SELECT COUNT(*) FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND ACCOUNTRULECODE='"+acctrulecode+"'";
			enctrace("acctrule query "+acctruleqry);
			x = jdbctemplate.queryForInt(acctruleqry);
		} catch (DataAccessException e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception:  WHILE CHECK ACCT RULE EXIST");
			trace( "Exception:  WHILE CHECK ACCT RULE EXIST"+e.getMessage());
			e.printStackTrace();
		}
		return x;
	}
	
	
	public String getAccountRuleName(String instid, String acctrulecode,JdbcTemplate jdbctemplate){
		String acctrulename="NOREC";
		try {
			String acctrulenameqry = "SELECT ACCT_RULEID FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND ACCOUNTRULECODE='"+acctrulecode+"'";
			enctrace("query for account rule name .....:"+acctrulenameqry);
			acctrulename = (String)jdbctemplate.queryForObject(acctrulenameqry, String.class);
		} catch (DataAccessException e) {
		 
		}
		return acctrulename;
	}
	
	public synchronized int getAccountRuleSeq(String instid, JdbcTemplate jdbctemplate ){
		String acctruleseq_qry = "SELECT ACCT_SEQ  FROM IFP_SEQUENCE_MASTER WHERE INST_ID='"+instid+"'";
		int acctrule = jdbctemplate.queryForInt(acctruleseq_qry);
		String updacctrule = "UPDATE IFP_SEQUENCE_MASTER  SET ACCT_SEQ = ACCT_SEQ+1  WHERE INST_ID='"+instid+"'";
		int x = jdbctemplate.update(updacctrule);
		if( x==1 ){
			return acctrule;
		}
		return -1;
		
	}
	public int insertAccountRule(String instid, String acctruleid, JSONObject jsonacctruleargs, JdbcTemplate jdbctemplate ) throws Exception{
		
		String ACCTRULENAME = jsonacctruleargs.getString("ACCTRULENAME");
		String RECORDID = jsonacctruleargs.getString("RECORDID");
		String DEVICETYPE = jsonacctruleargs.getString("DEVICETYPE");
		String MSGTYPE = jsonacctruleargs.getString("MSGTYPE");
		String TXNCODE = jsonacctruleargs.getString("TXNCODE");
		String RESPCODE = jsonacctruleargs.getString("RESPCODE");
		String TXNSRC = jsonacctruleargs.getString("TXNSRC"); 
		String ISSUER = jsonacctruleargs.getString("ISSUER");
		String ACQUIRER = jsonacctruleargs.getString("ACQUIRER");
		String PRODUCTCODE = jsonacctruleargs.getString("PRODUCTCODE");
		String SUBPRODUCT =  jsonacctruleargs.getString("SUBPRODUCT");
		String ROUTEEXP1 = jsonacctruleargs.getString("ROUTEEXP1");
		String ROUTEEXPPRIORITY1 = jsonacctruleargs.getString("ROUTEEXPPRIORITY1");
		String ROUTEEXP2 = jsonacctruleargs.getString("ROUTEEXP2");
		String ROUTEEXPPRIORITY2 = jsonacctruleargs.getString("ROUTEEXPPRIORITY2");
		String AUTHLIST = jsonacctruleargs.getString("AUTHLIST"); 
		String FINTXNFLAG = jsonacctruleargs.getString("FINTXNFLAG");
		String NEWTXNFLAG = jsonacctruleargs.getString("NEWTXNFLAG");
		String REVTXNFLAG = jsonacctruleargs.getString("REVTXNFLAG");
		String FULLREVFLAG = jsonacctruleargs.getString("FULLREVFLAG");
		String AMOUNTCHECKREQ = jsonacctruleargs.getString("AMOUNTCHECKREQ");
		String MAPPINGTXNCODE = jsonacctruleargs.getString("MAPPINGTXNCODE");
		String AMOUNTVARIATION = jsonacctruleargs.getString("AMOUNTVARIATION");
		String ACCTPROPERTY = jsonacctruleargs.getString("ACCTPROPERTY");
		String FEEPROPERTY = jsonacctruleargs.getString("FEEPROPERTY");
		String KEYFIELDS = jsonacctruleargs.getString("KEYFIELDS");
		String ACCOUNTRULECODE = jsonacctruleargs.getString("ACCOUNTREULCODE"); 
		String CONFIGBY = jsonacctruleargs.getString("CONFIGBY");
		String AUTHSTATUS = jsonacctruleargs.getString("AUTHSTATUS");
		
	 
		String insertacctruleqry = " INSERT INTO IFP_ACCT_RULE(RECORDID, INST_ID, DEVICETYPE, MSGTYPE, TXNCODE, RESPCODE, TXNSRC, ISSUER, ACQUIRER, ";
		insertacctruleqry += " PRODUCTCODE,  SUBPRODUCT, ROUTEEXP1, ROUTEEXPPRIORITY1, ROUTEEXP2, ROUTEEXPPRIORITY2, AUTHLIST, ACCT_RULEID, FINTXNFLAG, ";
		insertacctruleqry += " NEWTXNFLAG, REVTXNFLAG,  FULLREVFLAG, AMOUNTCHECKREQ, MAPPINGTXNCODE, AMOUNTVARIATION, ACCTPROPERTY,   FEEPROPERTY, KEYFIELDS, ACCOUNTRULECODE, " ;
		insertacctruleqry += " CONFIG_BY, CONFIG_DATE, AUTH_STATUS ) Values";
		insertacctruleqry += "('"+RECORDID+"', '"+instid+"','"+DEVICETYPE+"','"+MSGTYPE+"','"+TXNCODE+"','"+RESPCODE+"','"+TXNSRC+"','"+ISSUER+"','"+ACQUIRER+"',";
		insertacctruleqry += "'"+PRODUCTCODE+"', '"+SUBPRODUCT+"','"+ROUTEEXP1+"','"+ROUTEEXPPRIORITY1+"','"+ROUTEEXP2+"','"+ROUTEEXPPRIORITY2+"','"+AUTHLIST+"', '"+ACCTRULENAME+"', '"+FINTXNFLAG+"',";
		insertacctruleqry += "'"+NEWTXNFLAG+"', '"+REVTXNFLAG+"','"+FULLREVFLAG+"','"+AMOUNTCHECKREQ+"','"+MAPPINGTXNCODE+"','"+AMOUNTVARIATION+"','"+ACCTPROPERTY+"','"+FEEPROPERTY+"','"+KEYFIELDS+"','"+ACCOUNTRULECODE+"',";
		insertacctruleqry += "'"+CONFIGBY+"', SYSDATE, '"+AUTHSTATUS+"' )";
		enctrace(" Insert account rule "+insertacctruleqry);
		int x = jdbctemplate.update(insertacctruleqry);
		return x;
	}
	
	
	public List getAcctRuleList( String instid, JdbcTemplate jdbctemplate ) throws Exception{
		List acctrulelist = null;
		String acctrulelistqry = "SELECT ACCT_RULEID, ACCOUNTRULECODE FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND INST_ID ='"+instid+"'";
		enctrace(" account rule select query ..........: "  + acctrulelistqry);
		acctrulelist = jdbctemplate.queryForList(acctrulelistqry);
		return acctrulelist;
	}
	
	public List showAcctountRuleData( String instid,  String acctruleid, JdbcTemplate jdbctemplate ) throws Exception { 
		String acctruleqry = "SELECT * FROM IFP_ACCT_RULE WHERE  INST_ID='"+instid+"' AND ACCOUNTRULECODE ='"+acctruleid+"' AND ROWNUM<=1";
		enctrace("account rule query ..........: "+acctruleqry);
		List acctrule = jdbctemplate.queryForList(acctruleqry);
		return acctrule; 
	}
	
	public int deleteAccountRule(String instid, String acctruleid, JdbcTemplate jdbctemplate) throws Exception{
		String delqry = "DELETE FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND ACCOUNTRULECODE='"+acctruleid+"'";
		trace("delete query for account rule "+ delqry);
		int x = jdbctemplate.update(delqry);
		return x;
	} 
	
	
}
 