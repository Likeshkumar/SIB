package com.ifp.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class GLConfigureDAO extends BaseAction 
{ 
	public String getAcctRuleSubProduct(String instid, String acctrulecode, JdbcTemplate jdbctemplate) throws Exception {
		String subprod = null;
		String acctruleqry = "SELECT SUBPRODUCT FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND  ACCOUNTRULECODE='"+acctrulecode+"' and rownum <=1";
		enctrace(" query for account Rule Sub Product list " + acctruleqry);
		try{
			subprod = (String)jdbctemplate.queryForObject(acctruleqry, String.class);
		}catch( EmptyResultDataAccessException e ){} 
		return subprod;
	}
	
	
	
	public List getAccountRuleList(String instid, String acctruleid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctrulelist = null;
		String acctrulelistqry = "SELECT * FROM   IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND  ACCT_RULEID='"+acctruleid+"' ";
		enctrace("acctrulelistqry :" + acctrulelistqry);
		acctrulelist = jdbctemplate.queryForList(acctrulelistqry);
		return acctrulelist ;
	}
	
	public String getAcctRuleProduct(String instid, String acctrulecode, JdbcTemplate jdbctemplate) throws Exception {
		String subprod = null;
		String acctruleqry = "SELECT PRODUCTCODE FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"' AND  ACCOUNTRULECODE='"+acctrulecode+"'";
		enctrace(" query for account Rule Product list " + acctruleqry);
		try{
			subprod = (String)jdbctemplate.queryForObject(acctruleqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return subprod;
	}
	
	public String getProductBySubproduct(String instid, String subprodid, JdbcTemplate jdbctemplate ) throws Exception {
		String acctruleprodqry = "SELECT PRODUCT_CODE FROM IFP_INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND  SUB_PROD_ID='"+subprodid+"'";
		String product  = null;
		System.out.println("acctruleqry__" + acctruleprodqry);
		try{
			product = (String)jdbctemplate.queryForObject(acctruleprodqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		 
		return product;
	}
	
	public String getGlSchmeCode( String instid, String subproduct, JdbcTemplate jdbctemplate ) throws Exception {
		String glschcode = null;
		try {
			String fchglqry = "SELECT GL_SCHEME_CODE FROM IFP_INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND SUB_PROD_ID='"+subproduct+"'";
			System.out.println("fchglqry__" + fchglqry);
			glschcode = (String)jdbctemplate.queryForObject(fchglqry, String.class); 
		} catch (EmptyResultDataAccessException e) {} 
		return glschcode;
		 
	}
	
	public String getGlCode( String instid, String product, JdbcTemplate jdbctemplate ) throws Exception {
		String glschcode = null;
		try {
			String fchglqry = "SELECT GL_CODE FROM IFPRODUCT_MASTER WHERE   INST_ID='"+instid+"' AND  PRODUCT_CODE ='"+product+"'";
			System.out.println("fchglqry__" + fchglqry);
			glschcode = (String)jdbctemplate.queryForObject(fchglqry, String.class);
		} catch (EmptyResultDataAccessException e) {} 
		return glschcode;
	}
	
	public String glGroupCode( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String query="select GL_GRP_SEQ from IFP_GL_SEQUANCE  where INST_ID='"+instid+"' AND  rownum<=1";
		System.out.println("glGroupCode : "+ query);
		String glgrpcode = (String)jdbctemplate.queryForObject(query,String.class);
		return glgrpcode;
	}
	
	public String glSeqCode( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String query="select GL_SEQ from IFP_GL_SEQUANCE  where INST_ID='"+instid+"' AND  rownum<=1";
		System.out.println("glSeqCode : "+ query);
		String glgrpcode = (String)jdbctemplate.queryForObject(query,String.class);
		return glgrpcode;
	}
	
	public String glschemeSeqCode( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String query="select GL_SCHM_SEQ from IFP_GL_SEQUANCE  where INST_ID='"+instid+"' AND  rownum<=1";
		System.out.println("glschemeSeqCode : "+ query);
		String glgrpcode = (String)jdbctemplate.queryForObject(query,String.class);
		return glgrpcode;
	}
	
	
	public String glGenMethod( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String query="select GL_CODE_GEN from INSTITUTION where INST_ID='"+instid+"' AND rownum<=1";
		enctrace( " GL Generate query......... : " + query );
		String glgenmethod=(String)jdbctemplate.queryForObject(query,String.class);		
		return glgenmethod;
	} 
	
	public int checkGlSchemeExist( String instid, String glcode, JdbcTemplate jdbctemplate) throws Exception  {
		String grpexist = "SELECT COUNT(*) FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"+instid+"' AND GL_CODE='"+glcode+"' AND SCH_STATUS = 1";
		System.out.println("grpexist__" + grpexist );
		int exist= (Integer)jdbctemplate.queryForInt(grpexist);
		return exist;
	}
	public String fchMkckStatus (String instid, String actioncode, String schemecode, JdbcTemplate jdbctemplate) throws Exception  {
		String grpdescqry ="SELECT MKCK_STATUS FROM IFP_GL_MAPPING WHERE  INST_ID ='"+instid+"'  AND ACTION_CODE='"+actioncode+"' AND AFFECTING_SCH_CODE='"+schemecode+"' AND ROWNUM<=1";
		String grpdesc  =(String)jdbctemplate.queryForObject(grpdescqry,String.class);
		return  grpdesc;
	}
	public String fchGlGrpDesc( String instid, String grpcode, JdbcTemplate jdbctemplate )  throws Exception  {
		String grpdescqry ="SELECT GROUP_NAME FROM IFP_GL_GROUP WHERE  INST_ID ='"+instid+"'  AND GROUP_CODE='"+grpcode+"'  AND ROWNUM<=1";
		String grpdesc  =(String)jdbctemplate.queryForObject(grpdescqry,String.class);
		return  grpdesc;
	}
	
	 
	public List fchGlGroupList( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		List glgrouplist = null;
		String glgrouplistqry = "SELECT GROUP_CODE,GROUP_NAME,GROUP_PARENTID FROM IFP_GL_GROUP   WHERE INST_ID='"+instid+"' AND MKCK_STATUS = 'P'";		
		enctrace( " GL Group query ......... :" + glgrouplistqry );
		glgrouplist = jdbctemplate.queryForList(glgrouplistqry); 
		return glgrouplist;
	}
	
	public int chkGlGrpExist( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String grpexist = "SELECT COUNT(*) FROM IFP_GL_GROUP WHERE INST_ID='"+instid+"' AND MKCK_STATUS = 'P'";
		enctrace(" gl group query :  "+grpexist);
		int exist= (Integer)jdbctemplate.queryForInt(grpexist);
		return exist;
	}

	
	public String fchGlSequance( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String statusqry ="SELECT max ( GL_SEQ_CODE ) FROM IFP_GL_MASTER WHERE  INST_ID ='"+instid+"'  AND ROWNUM<=1";
		String statusdesc =(String)jdbctemplate.queryForObject(statusqry,String.class);
		return  statusdesc ;
	}
		

	public int chkHoGlExist(String instid, String gldesc, JdbcTemplate jdbctemplate) throws Exception  {
		String statusqry ="SELECT count(*) FROM IFP_GL_MASTER WHERE  INST_ID ='"+instid+"' AND  GL_COM_ID ='"+gldesc+"'";
		enctrace(" HO Query : "+statusqry);
		int hocnt=(Integer)jdbctemplate.queryForInt(statusqry);
		return  hocnt;  
	}
	
	 
	public List fchGlList( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		List glgrouplist = null;
		String glgrouplistqry = "SELECT GL_CODE, GL_NAME FROM IFP_GL_MASTER   WHERE INST_ID='"+instid+"' AND MKCK_STATUS ='P'";
		enctrace(" gl  list query ........: "+glgrouplistqry);
		glgrouplist = jdbctemplate.queryForList(glgrouplistqry); 
		return glgrouplist;
	}
	
	public int chkGlExist( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String grpexist = "SELECT COUNT(*) FROM IFP_GL_MASTER WHERE INST_ID='"+instid+"' AND MKCK_STATUS ='P' AND GL_COM_ID ='BIN'";
		System.out.println("grpexist__" + grpexist );
		int exist= (Integer)jdbctemplate.queryForInt(grpexist);
		return exist;
	}
	
	public int chkGlSchemeExist( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		String grpexist = "SELECT COUNT(*) FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"+instid+"' AND MKCK_STATUS = 'P'";
		enctrace(" count query for gl scheme" + grpexist );
		int exist= (Integer)jdbctemplate.queryForInt(grpexist);
		return exist;
	}
	
	 
	public List fchGlSchemeList( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		List glgrouplist = null;
		String glgrouplistqry = "SELECT SCH_CODE, SCH_NAME FROM IFP_GL_SCHEME_MASTER   WHERE INST_ID='"+instid+"' AND SCH_STATUS=1  "; 
		glgrouplist = jdbctemplate.queryForList(glgrouplistqry); 
		return glgrouplist;
	}
	
	 
	public List fchGlMasterSchemeList( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		List glschlist = null;
		String glschlistqry = "SELECT sh.SCH_CODE, sh.SCH_NAME FROM IFP_GL_SCHEME_MASTER sh, IFP_GL_MASTER gl  WHERE sh.INST_ID='"+instid+"' AND sh.INST_ID=gl.INST_ID AND sh.GL_CODE=gl.GL_CODE AND gl.GL_COM_ID='BIN' AND sh.SCH_STATUS=1  ";
		enctrace(" query for gl scheme master " + glschlistqry );
		glschlist = jdbctemplate.queryForList(glschlistqry); 
		return glschlist;
	}
	
	
	public String fchGlSchemeDesc( String instid, String glschemecode, JdbcTemplate jdbctemplate ) throws Exception  {
		String statusdesc = "NOREC"; 
		try {
			String statusqry ="SELECT SCH_NAME FROM IFP_GL_SCHEME_MASTER  WHERE INST_ID='"+instid+"' AND SCH_CODE='"+glschemecode+"'  AND ROWNUM<=1";
			System.out.println("statusqry__" +  statusqry);
			statusdesc = (String)jdbctemplate.queryForObject(statusqry,String.class);
		} catch (DataAccessException e) {			
			e.printStackTrace();
		}
		return  statusdesc ;
	}
	
	public String fchTxnCodeDesc( String instid, String actioncode, JdbcTemplate jdbctemplate ) throws Exception  {
		String statusqry ="SELECT ACTION_DESC FROM IFACTIONCODES  WHERE INST_ID='"+instid+"' AND ACTION_CODE='"+actioncode+"'  AND ROWNUM<=1";
		System.out.println("statusqry__" +  statusqry);
		String statusdesc =(String)jdbctemplate.queryForObject(statusqry,String.class);
		return  statusdesc ;
	}
	
	 
	public List fchAcctRules( String instid, JdbcTemplate jdbctemplate ) throws Exception  { 
		List txnlist = null;
		//String txnlistqry = "SELECT ACCOUNTRULECODE , ( RECORDID||'-'||ACCT_RULEID ) AS ACCT_RULEID FROM IFP_ACCT_RULE  WHERE INST_ID='"+instid+"' AND AUTH_STATUS=1" ; 
		String txnlistqry = "SELECT DISTINCT ACCT_RULEID AS ACCT_RULEID, ACCT_RULEID AS ACCOUNTRULECODE FROM IFP_ACCT_RULE  WHERE INST_ID='"+instid+"'" ;
		//String txnlistqry = " SELECT ACCT_RULEID AS ACCT_RULEID, ACCT_RULEID  FROM IFP_ACCT_RULE WHERE INST_ID='"+instid+"'   GROUP BY ACCT_RULEID ";
		enctrace(" query for account rule ....... " +  txnlistqry);
		txnlist = jdbctemplate.queryForList(txnlistqry); 
		return txnlist;
	}
	 
	public List fchTxnList( String instid, JdbcTemplate jdbctemplate ) throws Exception  {
		List txnlist = null;
		String txnlistqry = "SELECT ACTION_CODE, ACTION_DESC FROM IFACTIONCODES  WHERE INST_ID='"+instid+"' AND ACTION_STATUS=1  ";
		System.out.println("txnlistqry : " +  txnlistqry);
		txnlist = jdbctemplate.queryForList(txnlistqry); 
		return txnlist;
	}
	
	public List fchGlActionCodes( String instid, JdbcTemplate jdbctemplate ) throws Exception { 
		String actionqry = "SELECT ACTION_CODE, ACTION_DESC FROM IFACTIONCODES WHERE INST_ID='"+instid+"'  AND ACTION_STATUS='1' AND TXN_FLAG='1'";
		enctrace(" query for gl action "+actionqry);
		System.out.println("fchGlActionCodes : " +  actionqry);
		List actionlist = jdbctemplate.queryForList(actionqry); 
		return actionlist; 
	}
	
	public int checkActionFeeExist(String instid, String appactcode, JdbcTemplate jdbctemplate) throws Exception  {
		String feexistqry = "SELECT COUNT(*) FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"+instid+"' AND APP_ACTION='"+appactcode+"'";
		enctrace("query for check fee exist" + feexistqry);
		int x = jdbctemplate.queryForInt(feexistqry);
		return x;
	}
	
	public String getGlComId(String instid, String glcode, JdbcTemplate jdbctemplate) throws Exception  {
		String glcomqry = "SELECT GL_COM_ID FROM IFP_GL_MASTER WHERE INST_ID='"+instid+"' AND  GL_CODE='"+glcode+"'";
		enctrace("gl common id  qruey :" + glcomqry);
		String glcomcode = (String)jdbctemplate.queryForObject(glcomqry, String.class);
		return glcomcode;
	}
	
	public List getGlGrouupDetails( String instid, String grpcode, JdbcTemplate jdbctemplate ) throws Exception {
		String gldetails_query="select * from IFP_GL_GROUP where GROUP_CODE='"+grpcode+"' and inst_id='"+instid+"'";
		enctrace("gl group query  :  "+ gldetails_query);
		List glgrplist = jdbctemplate.queryForList(gldetails_query);
		return glgrplist;
	}
	public int chkActionCodeExist(String instid, JdbcTemplate jdbctemplate){
		String actioncodeqry = "SELECT COUNT(ACTION_CODE) FROM IFACTIONCODES WHERE INST_ID='"+instid+"'";
		enctrace(" count query for action code  : " + actioncodeqry);
		int actioncnt = jdbctemplate.queryForInt(actioncodeqry);
		return actioncnt;
	}
	
	public String getFeeDescription( String instid, String actioncode, JdbcTemplate jdbctemplate ) throws Exception {
		String feedescqry ="SELECT ACTION_DESC FROM IFACTIONCODES WHERE INST_ID='"+instid+"' AND ACTION_CODE='"+actioncode+"'  AND ROWNUM <=1" ;
		System.out.println("feedescqry__"+feedescqry);
		String feeactdesc = null; 
		feeactdesc=(String)jdbctemplate.queryForObject(feedescqry,String.class);
		System.out.println("feeactdesc__" + feeactdesc ); 
		return  feeactdesc; 
	}
	
	public List fchGlGroupForAuth( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List glgrouplist = null;
		String glgrouplistqry = "SELECT GROUP_CODE, GROUP_NAME FROM IFP_GL_GROUP   WHERE INST_ID='"+instid+"' AND MKCK_STATUS = 'M'";
		System.out.println("glgrouplistqry__" + glgrouplistqry);
		glgrouplist = jdbctemplate.queryForList(glgrouplistqry); 
		return glgrouplist;
	}
	
	public List fchGlForAuth( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List gllist = null;
		String gllistqry = "SELECT GL_CODE, GL_NAME FROM IFP_GL_MASTER    WHERE INST_ID='"+instid+"' AND MKCK_STATUS = 'M'";
		System.out.println("gllistqry__" + gllistqry);
		gllist = jdbctemplate.queryForList(gllistqry); 
		return gllist;
	}
	
	public List getGlKeys( JdbcTemplate jdbctemplate ) throws Exception{
		String glkeysqry= "SELECT TXNKEY, TXNKEYDESC FROM IFP_GL_KEYS WHERE STATUS='1'";
		enctrace(" query for trasaction key .....:"+glkeysqry);
		List glkeyslist = jdbctemplate.queryForList(glkeysqry);
		return glkeyslist; 
	}
	
	public String getGlKeyDesc( String glkey, JdbcTemplate jdbctemplate  ){
		String glkeydesc = null; 
		try {
			String glkeydecqry ="SELECT  TXNKEYDESC FROM IFP_GL_KEYS WHERE TXNKEY='"+glkey+"'" ;
			enctrace("glkeydecqry : "+glkeydecqry);		
			glkeydesc=(String)jdbctemplate.queryForObject(glkeydecqry,String.class);
		} catch (EmptyResultDataAccessException e) {} 
		return  glkeydesc; 
	}
	
	public List getSubGlList( String instid, JdbcTemplate jdbctemplate ) throws Exception{
		//String subglqry = "SELECT SCH_CODE, SCH_NAME FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"+instid+"'";
		String subglqry="SELECT SCH_CODE, SCH_NAME FROM IFP_GL_SCHEME_MASTER  UNION SELECT TXNKEY AS SCH_CODE, TXNKEYDESC AS SCH_NAME  FROM IFP_GL_KEYS";
		trace("query for sub gl list  : "+subglqry);
		List subgllist = jdbctemplate.queryForList(subglqry);
		return subgllist; 
	}
	
	public int insertGlMapping(String recordid, String instid, String acctruleid, String trantype, String schemetype, String subgl, String entrytype, String txncode, String glmapkey, JdbcTemplate jdbctemplate) throws Exception {
		String insertqry = "INSERT INTO IFP_GL_MAPPING (RECORDID,INST_ID, ACCT_RULEID, TRAN_TYPE, SCH_CODE, SCH_TYPE, SCH_ENTRY, SCH_STATUS, ACTION_CODE, GLMAP_KEY ) VALUES ";
		insertqry += "('"+recordid+"','"+instid+"','"+acctruleid+"', '"+trantype+"','"+subgl+"','"+schemetype+"','"+entrytype+"', '1', '"+txncode+"', '"+glmapkey+"')";
		enctrace("query for inserting gl mapping "+ insertqry);
		int x = jdbctemplate.update(insertqry);
		return x;
				
	}
	

	public List getConfiguredMappingList(String instid, String glmapkey, JdbcTemplate jdbctemplate ) throws Exception {
		List mappedgllist = null;
		String mappedschemeqry = "SELECT  ACCT_RULEID, TRAN_TYPE, SCH_CODE, SCH_TYPE, SCH_ENTRY, ACTION_CODE  FROM IFP_GL_MAPPING WHERE INST_ID='"+instid+"' AND GLMAP_KEY='"+glmapkey+"' ORDER BY ACCT_RULEID";
		enctrace("query for : mapped scheme list " + mappedschemeqry );
		mappedgllist = jdbctemplate.queryForList(mappedschemeqry);
		return mappedgllist; 
	}

	public String getAcctRuleName( String instid, String acctruleid, JdbcTemplate jdbctemplate) throws Exception {
		String subglqry = "SELECT ACCT_RULEID  FROM IFP_ACCT_RULE  WHERE INST_ID='"+instid+"' AND ACCOUNTRULECODE='"+acctruleid+"' ";
		enctrace(" query for Sub gl list  : "+subglqry);
		String acctrulename = (String)jdbctemplate.queryForObject(subglqry, String.class);
		return acctrulename; 
	}
	
	public int deleteMappedGl(String instid, String glmapid, JdbcTemplate jdbctemplate ){
		String deleteqry = "DELETE FROM IFP_GL_MAPPING WHERE INST_ID='"+instid+"' AND ACCT_RULEID='"+glmapid+"'";
		enctrace("delete query : " + deleteqry );
		//String delmapschemeqry = "UPDATE IFP_GL_MAPPING_HISTORY SET SCH_STATUS='0', DE_ACTIVATED_DATE=SYSDATE, DE_ACTIVATED_BY='"+usercode+"', DE_ACTIVATED_REASON='"+reasonfordel+"' WHERE INST_ID='"+instid+"' AND ACTION_CODE='"+actioncode+"' AND AFFECTING_SCH_CODE='"+affectschcode+"'";
		//System.out.println( "delmapschemeqry__ "+ delmapschemeqry );
		int x = jdbctemplate.update( deleteqry );
		return x;
		
	}
	 
}
 