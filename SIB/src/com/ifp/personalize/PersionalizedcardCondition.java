package com.ifp.personalize;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class PersionalizedcardCondition extends BaseAction {
	
	
	public List getBranchCodefmOrder(String instid,String orderStatus,String mkrstatus,JdbcTemplate jdbctemplate)
	{
		StringBuilder query = new StringBuilder();
		query.append("select BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE a.BRANCH_CODE=B.BRANCH_CODE)BRANCH_NAME "); 
		query.append("from PERS_CARD_ORDER b ");
		query.append("WHERE  ");
		
		/*query.append("INST_ID='"+instid+"' AND ORDER_STATUS='"+orderStatus+"' AND MKCK_STATUS='"+mkrstatus+"' "); 
		query.append("GROUP BY BRANCH_CODE  ORDER BY BRANCH_CODE");
		enctrace( " getBranchCodefmOrder : " + query.toString() );
		List branchList = jdbctemplate.queryForList(query.toString()); */

		
		//by gowtham-220819
		query.append("INST_ID=? AND ORDER_STATUS=? AND MKCK_STATUS=? "); 
		query.append("GROUP BY BRANCH_CODE  ORDER BY BRANCH_CODE");
		enctrace( " getBranchCodefmOrder : " + query.toString() );
		List branchList = jdbctemplate.queryForList(query.toString(),new Object[]{instid,orderStatus,mkrstatus}); 
		
		
		return branchList; 
	}
	
	public List getBranchCodefmProcess(String instid,String orderStatus,String mkrstatus,JdbcTemplate jdbctemplate)
	{
		StringBuilder query = new StringBuilder();
		
		/*query.append("select BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE trim(a.BRANCH_CODE)=trim(B.BRANCH_CODE))BRANCH_NAME "); 
		query.append("from PERS_CARD_PROCESS b ");   
		query.append("WHERE  ");
		query.append("INST_ID='"+instid+"' AND CARD_STATUS='"+orderStatus+"' AND MKCK_STATUS='"+mkrstatus+"' "); 
		query.append("GROUP BY BRANCH_CODE  ORDER BY BRANCH_CODE");
		enctrace( " getBranchCodefmProcess : " + query.toString() );
		List branchList = jdbctemplate.queryForList(query.toString()); */
		
		
		
		//by gowtham260819
				query.append("select BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE trim(a.BRANCH_CODE)=trim(B.BRANCH_CODE))BRANCH_NAME "); 
				query.append("from PERS_CARD_PROCESS b ");   
				query.append("WHERE  ");
				query.append("INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? "); 
				query.append("GROUP BY BRANCH_CODE  ORDER BY BRANCH_CODE");
				enctrace( " getBranchCodefmProcess : " + query.toString() );
				List branchList = jdbctemplate.queryForList(query.toString(),new Object[]{instid,orderStatus,mkrstatus,}); 
				
				
		
		return branchList; 
	}
	
	public List getBranchCodeForRenual(String instid,String renewalperiods, JdbcTemplate jdbctemplate)
	{
		StringBuilder query = new StringBuilder();
		query.append("select trim(BRANCH_CODE)BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER A WHERE A.BRANCH_CODE=trim(B.BRANCH_CODE)) BRANCH_NAME ");
		query.append("from CARD_PRODUCTION B where  EXPIRY_DATE between add_months(sysdate,-6) and add_months(sysdate,"+renewalperiods+")   AND INST_ID='"+instid+"' AND  CAF_REC_STATUS NOT IN ('BR','BN','S','D','DE') group by trim(BRANCH_CODE)");
		enctrace( " getBranchCodeForRenual : " + query.toString()   );    
		List branchList = jdbctemplate.queryForList(query.toString());        
		return branchList;      
	}       
	
	public List getBulkForRenualList(String instid,String renewalperiods, JdbcTemplate jdbctemplate)
	{
		StringBuilder query = new StringBuilder();
		
		/*query.append("select distinct(order_ref_no) as ORDER_REF_NO, CIN, CARD_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,");
		query.append("STATUS_CODE,(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS_CODE= STATUS AND INST_ID='"+instid+"') STATUS_DESC,trunc(EXPIRY_DATE)-trunc(sysdate) EXPIRY_COUNT_DAYS from CARD_PRODUCTION ");
		query.append(" where CAF_REC_STATUS NOT IN ('BN','S','D','DE') AND  EXPIRY_DATE between add_months(sysdate,-120) and  add_months(sysdate,"+renewalperiods+") AND INST_ID='"+instid+"' ");
		 enctrace( " getBulkForRenualList : " + query.toString()   );
		List branchList = jdbctemplate.queryForList(query.toString());     */
		
		
		///by gowtham-300819
				query.append("select distinct(order_ref_no) as ORDER_REF_NO, CIN, CARD_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,");
				query.append("STATUS_CODE,(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS_CODE= STATUS AND INST_ID=?) STATUS_DESC,trunc(EXPIRY_DATE)-trunc(sysdate) EXPIRY_COUNT_DAYS from CARD_PRODUCTION ");
				query.append(" where CAF_REC_STATUS NOT IN ('BN','S','D','DE') AND  EXPIRY_DATE between add_months(sysdate,-120) and  add_months(sysdate,"+renewalperiods+") AND INST_ID=? ");
				enctrace( " getBulkForRenualList : " + query.toString()   );
				List branchList = jdbctemplate.queryForList(query.toString(),new Object[]{instid,instid});    
		
		return branchList; 
	}
	
	public List getSingleRenewalList(String instid,String cond, String renewalperiods, JdbcTemplate jdbctemplate)
	{
		StringBuilder query = new StringBuilder();
		
		// BY SIVA 11-07-2019
		/*query.append("select distinct(order_ref_no) as ORDER_REF_NO, CIN, CARD_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,");
		query.append("STATUS_CODE,(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS_CODE= STATUS AND INST_ID='"+instid+"') STATUS_DESC,trunc(EXPIRY_DATE)-trunc(sysdate) EXPIRY_COUNT_DAYS from CARD_PRODUCTION ");
		query.append(" where CAF_REC_STATUS NOT IN ('BN','S','D','DE') and EXPIRY_DATE between add_months(sysdate,-120) and  add_months(sysdate,"+renewalperiods+") AND INST_ID='"+instid+"' AND "+cond+" ");*/
		
		/*query.append("select distinct(order_ref_no) as ORDER_REF_NO, CIN, ORG_CHN,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE INST_ID='"+instid+"' AND "+cond+") as HCARD_NO,MCARD_NO,ACCOUNT_NO ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,");
		query.append("STATUS_CODE,(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS_CODE= STATUS AND INST_ID='"+instid+"') STATUS_DESC,trunc(EXPIRY_DATE)-trunc(sysdate) EXPIRY_COUNT_DAYS from CARD_PRODUCTION ");
		query.append(" where CAF_REC_STATUS NOT IN ('BN','S','D','DE') and EXPIRY_DATE between add_months(sysdate,-120) and  add_months(sysdate,"+renewalperiods+") AND INST_ID='"+instid+"' AND "+cond+" ");
		enctrace( " getSingleRenewalList : " + query.toString()   );
		List branchList = jdbctemplate.queryForList(query.toString());*/
		
		
		///by gowtham300819
				query.append("select distinct(order_ref_no) as ORDER_REF_NO, CIN, ORG_CHN,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE INST_ID=? AND "+cond+") as HCARD_NO,MCARD_NO,ACCOUNT_NO ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,");
				query.append("STATUS_CODE,(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS_CODE= STATUS AND INST_ID=?) STATUS_DESC,trunc(EXPIRY_DATE)-trunc(sysdate) EXPIRY_COUNT_DAYS from CARD_PRODUCTION ");
				query.append(" where CAF_REC_STATUS NOT IN ('BN','S','D','DE') and EXPIRY_DATE between add_months(sysdate,-120) and  add_months(sysdate,"+renewalperiods+") AND INST_ID=? AND "+cond+" ");
				    
				enctrace( " getSingleRenewalList : " + query.toString()   );
				List branchList = jdbctemplate.queryForList(query.toString(),new Object[]{instid,instid,instid});   
		
		return branchList; 
	}
	public List getBranchCodeForSingleRenewal(String instid,String renewalperiods, JdbcTemplate jdbctemplate)
	{
		StringBuilder query = new StringBuilder();
		query.append("SELECT BRANCH_CODE,BRANCH_NAME FROM BRANCH_MASTER order by BRANCH_CODE ");
		//query.append("from CARD_PRODUCTION B where  EXPIRY_DATE between add_months(sysdate,-2) and add_months(sysdate,"+renewalperiods+") AND INST_ID='"+instid+"' AND  CAF_REC_STATUS NOT IN ('BR','BN') group by trim(BRANCH_CODE) order by BRANCH_CODE");
		enctrace( " getBranchCodeForRenual : " + query.toString()   );    
		List branchList = jdbctemplate.queryForList(query.toString());        
		return branchList;      
	}  
	

}
