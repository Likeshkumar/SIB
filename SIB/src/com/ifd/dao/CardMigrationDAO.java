package com.ifd.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class CardMigrationDAO extends BaseAction{
	
	public List<?> binlist(String instid,JdbcTemplate jdbctemplate)
	{
		
		String qury="select PRD_CODE from PRODUCTINFO where inst_id='"+instid+"' AND AUTH_STATUS='1' AND PRD_CODE IN(SELECT  PRD_CODE FROM MIGDATA WHERE PRODUCTINFO.PRD_CODE=MIGDATA.PRD_CODE) ";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);
		return binlist;
	}
	
	public List generateBranchList(String instid, JdbcTemplate jdbctemplate  )
	{  
		String query="select BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID='"+instid+"' and AUTH_CODE='1' AND BRANCH_CODE!='000' AND BRANCH_CODE IN(SELECT trim(BRANCH_CODE) FROM MIGDATA where BRANCH_MASTER.BRANCH_CODE=trim(MIGDATA.BRANCH_CODE) ) order by BRANCH_CODE"; 
		enctrace( " branch query : " + query );
		List branchList = jdbctemplate.queryForList(query); 
		return branchList;   
	}
	
	public List getProductListView( String instid, JdbcTemplate jdbctemplate) throws Exception{ 
		String query="select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID='"+instid+"' AND AUTH_CODE=1 AND DELETED_FLAG !='2'";
		enctrace(  " get prod list : " + query ); 
		return(jdbctemplate.queryForList(query));   
	}
	
	public List getMigrateRecordList(String query,JdbcTemplate jdbctemplate2) {
		List result=null;
		try {
			String qry = "SELECT * FROM MIGDATA WHERE not exists(SELECT ACCOUNTNO FROM ACCOUNTINFO where MIGDATA.PAN=ACCOUNTINFO.accountno) AND "+query + " ORDER BY CHN";
			System.out.println("coming into query value" + qry);
			enctrace("final Qry is " +qry);
			result = jdbctemplate2.queryForList(qry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
