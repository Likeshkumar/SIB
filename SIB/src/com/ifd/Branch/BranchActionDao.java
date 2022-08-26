package com.ifd.Branch;
 
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

import test.Date;

@Transactional
public class BranchActionDao extends BaseAction{
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
 
	private JdbcTemplate jdbcTemplate;
	
	/*public List getBranchPropResult(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List branch_prop_result = null;
		String branch_prop_insttbl_query="select BRANCHATTCHED,BRCODELEN from INSTITUTION where INST_ID='"+i_Name+"'";
		enctrace("branch_prop_insttbl_query.."+branch_prop_insttbl_query);
		branch_prop_result=jdbcTemplate.queryForList(branch_prop_insttbl_query );
		return branch_prop_result;
	}

	public int getCheckbnchcodeExist(String instid,String br_code,JdbcTemplate jdbcTemplate)
	{
		int checkbnchcode_exist = -1;
		String checkbnch_code ="select count(*) as exist from BRANCH_MASTER where inst_id='"+instid+"' and BRANCH_CODE='"+br_code+"'";
		checkbnchcode_exist = jdbcTemplate.queryForInt(checkbnch_code);
		return checkbnchcode_exist;
	}
	public int getCheckbnchnameExist(String instid,String br_name,JdbcTemplate jdbcTemplate)
	{
		int checkbnchname_exist = -1;
		String checkbnch_name ="select count(*) as exist from BRANCH_MASTER where inst_id='"+instid+"' and BRANCH_NAME='"+br_name+"'";
		//System.out.println("checkbnch_name--> "+checkbnch_name);
		checkbnchname_exist = jdbcTemplate.queryForInt(checkbnch_name);
		return checkbnchname_exist;
	}
	public String getBranchInsertQuery(String instid,String br_code,String br_name,String br_addr1,String br_addr2,String br_addr3,String br_city,String br_state,String br_phone,String usercode,String auth_code,String mkchkrstatus)
	{
		String branch_insert_query = null;
		branch_insert_query="INSERT INTO BRANCH_MASTER (INST_ID,BRANCH_CODE,BRANCH_NAME,BR_ADDR1,BR_ADDR2,BR_ADDR3,BR_CITY,BR_STATE,BR_PHONE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKEDATE) ";
		branch_insert_query=branch_insert_query+" VALUES ('"+instid+"','"+br_code+"','"+br_name+"','"+br_addr1.toUpperCase()+"','"+br_addr2.toUpperCase()+"','"+br_addr3.toUpperCase()+"','"+br_city.toUpperCase()+"','"+br_state.toUpperCase()+"','"+br_phone+"','"+auth_code+"','"+mkchkrstatus+"','"+usercode+"',sysdate)";
		return branch_insert_query;
	}
	public List getBrlistQuery(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+i_Name+"' and AUTH_CODE='1'";
		System.out.println("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);
		return br_list_result;
	}
	public List getBrDetailResult(String inst_id,String br_code,JdbcTemplate jdbcTemplate){
		List br_detail_result = null;
		String br_detail_query="select INST_ID,BRANCH_CODE,BRANCH_NAME,BR_ADDR1,BR_ADDR2,BR_ADDR3,BR_CITY,BR_STATE,BR_PHONE,BR_FAX_NUM,BR_EMAIL,BR_MANAGER,BRANCHIP,BRANCHUNAME,BRANCHPWD,MAKER_ID,MAKEDATE,CHEKR_ID,CHCKDATE,MKCK_STATUS,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from BRANCH_MASTER where INST_ID ='"+inst_id+"' and BRANCH_CODE='"+br_code+"'";
		enctrace(br_detail_query);
		br_detail_result =jdbcTemplate.queryForList(br_detail_query);
		return br_detail_result;
	}
	public String updateBranchMaster(String inst_id,String br_code,String br_name,String br_addr1,String br_addr2,String br_addr3,String br_city,String br_state,String br_phone){
		String update_branch_query = "update BRANCH_MASTER set BRANCH_NAME='"+br_name.toUpperCase()+"',BR_ADDR1='"+br_addr1.toUpperCase()+"',BR_ADDR2='"+br_addr2.toUpperCase()+"',BR_ADDR3='"+br_addr3.toUpperCase()+"',BR_CITY='"+br_city.toUpperCase() +"',BR_PHONE='"+br_phone.toUpperCase()+"',BR_STATE='"+br_state.toUpperCase() +"' where INST_ID='"+inst_id+"' and BRANCH_CODE='"+br_code+"'";
		return update_branch_query;
	}
	public String delBranchMaster(String inst_id,String br_code) {
		String delete_query="delete from BRANCH_MASTER where INST_ID ='"+inst_id+"' and BRANCH_CODE='"+br_code+"'" ;
		enctrace("delete_query :" + delete_query);
		return delete_query;
	}
	
	public String getAuthBranch (String instid,String branchid,JdbcTemplate jdbctemplate)
	{
		String authstatus= null;
		String authsubqry = "SELECT AUTH_CODE FROM BRANCH_MASTER WHERE BRANCH_CODE='"+branchid+"' AND INST_ID='"+instid+"'";
		enctrace("authsubqry --- "+authsubqry);
		authstatus = (String)jdbctemplate.queryForObject(authsubqry, String.class);
		return authstatus;
	}
	public int updateDeAuthBranchStatus(String instid, String mkrstatus,String branchid, String usercode,String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updstatusqry = "UPDATE BRANCH_MASTER SET MKCK_STATUS='"+mkrstatus+"',AUTH_CODE='9', CHEKR_ID='"+usercode+"', CHCKDATE=sysdate,REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"+branchid+"'";
		enctrace("updstatusqry : " + updstatusqry );
		x = jdbctemplate.update(updstatusqry);
		return x;
	}
	public int updateAuthBranchStatus(String instid,String mkrstatus,String branchid, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updstatusqry = "UPDATE BRANCH_MASTER SET MKCK_STATUS='"+mkrstatus+"',AUTH_CODE='1', CHEKR_ID='"+usercode+"', CHCKDATE=sysdate  WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"+branchid+"'";
		enctrace("updstatusqry : " + updstatusqry );
		x = jdbctemplate.update(updstatusqry);
		return x;
	}
	
	public int branchbaseexit(String instid, String  branchid,	JdbcTemplate jdbctemplate) {
		int x=-1;
		String checexist = "SELECT COUNT(*) AS CNT FROM BASENO WHERE INST_ID='"+instid+"'   AND BASENO_CODE='"+branchid+"'";
		select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'
		enctrace("checkcardexist : " + checexist );
		x = jdbctemplate.queryForInt(checexist);
	
		return x;
	}
	
	
	public List getBrlistQueryForChecker(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+i_Name+"' and  AUTH_CODE='0' AND MKCK_STATUS='M' ";
		enctrace("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);
		return br_list_result;
	}
	public List getBrlistQueryForMaker(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+i_Name+"'";
		enctrace("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);
		return br_list_result;
	}
	
	public List getBranchListForEdit(String instid,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+instid+"' and DELETED_FLAG !='2' AND BRANCH_CODE !='000' ";
		enctrace("br_list_query :"+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);
		return br_list_result;
	}
	  
	
	
	
	public List gettingBinList(String instid, JdbcTemplate jdbctemplate) {
		List res = null;
		try {
			String qry = "SELECT BIN,BASELEN FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND ATTACH_BRCODE='Y'";
			enctrace("Query for binlist ::: "+qry);
			res = jdbctemplate.queryForList(qry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}*/
	
	
	public List getBranchPropResult(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List branch_prop_result = null;
		
		/*String branch_prop_insttbl_query="select BRANCHATTCHED,BRCODELEN from INSTITUTION where INST_ID='"+i_Name+"'";
		enctrace("branch_prop_insttbl_query.."+branch_prop_insttbl_query);
		branch_prop_result=jdbcTemplate.queryForList(branch_prop_insttbl_query );*/
		
	//by gowtham-190819
		String branch_prop_insttbl_query="select BRANCHATTCHED,BRCODELEN from INSTITUTION where INST_ID=?";
		enctrace("branch_prop_insttbl_query.."+branch_prop_insttbl_query);
		branch_prop_result=jdbcTemplate.queryForList(branch_prop_insttbl_query,new Object[]{i_Name} );
		
		return branch_prop_result;
	}

	public int getCheckbnchcodeExist(String instid,String br_code,JdbcTemplate jdbcTemplate)
	{
		int checkbnchcode_exist = -1;
		
		/*String checkbnch_code ="select count(*) as exist from BRANCH_MASTER where inst_id='"+instid+"' and BRANCH_CODE='"+br_code+"'";
		checkbnchcode_exist = jdbcTemplate.queryForInt(checkbnch_code);*/

		//BY GOWTHAM-190819
		String checkbnch_code ="select count(*) as exist from BRANCH_MASTER where inst_id=? and BRANCH_CODE=?";
		checkbnchcode_exist = jdbcTemplate.queryForInt(checkbnch_code,new Object[]{instid,br_code});
		return checkbnchcode_exist;
	}
	public int getCheckbnchnameExist(String instid,String br_name,JdbcTemplate jdbcTemplate)
	{
		int checkbnchname_exist = -1;
		String checkbnch_name ="select count(*) as exist from BRANCH_MASTER where inst_id='"+instid+"' and BRANCH_NAME='"+br_name+"'";
		//System.out.println("checkbnch_name--> "+checkbnch_name);
		checkbnchname_exist = jdbcTemplate.queryForInt(checkbnch_name);
		return checkbnchname_exist;
	}
	public String getBranchInsertQuery(String instid,String br_code,String br_name,String br_addr1,String br_addr2,String br_addr3,String br_city,String br_state,String br_phone,String usercode,String auth_code,String mkchkrstatus)
	{
		String branch_insert_query = null;
		
		branch_insert_query="INSERT INTO BRANCH_MASTER (INST_ID,BRANCH_CODE,BRANCH_NAME,BR_ADDR1,BR_ADDR2,BR_ADDR3,BR_CITY,BR_STATE,BR_PHONE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKEDATE) ";
		branch_insert_query=branch_insert_query+" VALUES ('"+instid+"','"+br_code+"','"+br_name+"','"+br_addr1.toUpperCase()+"','"+br_addr2.toUpperCase()+"','"+br_addr3.toUpperCase()+"','"+br_city.toUpperCase()+"','"+br_state.toUpperCase()+"','"+br_phone+"','"+auth_code+"','"+mkchkrstatus+"','"+usercode+"',sysdate)";
		
		
	/*	//by gowtham-190819
		branch_insert_query="INSERT INTO BRANCH_MASTER (INST_ID,BRANCH_CODE,BRANCH_NAME,BR_ADDR1,BR_ADDR2,BR_ADDR3,BR_CITY,BR_STATE,BR_PHONE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKEDATE) ";
		branch_insert_query=branch_insert_query+" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";*/
		return branch_insert_query;
	}
	public List getBrlistQuery(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		
		/*String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+i_Name+"' and AUTH_CODE='1'";
		System.out.println("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);*/
		
		//by gowtham-190819
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID =? and AUTH_CODE=?";
		System.out.println("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query,new Object[]{i_Name,"1"});
		
		return br_list_result;
	}
	public List getBrDetailResult(String inst_id,String br_code,JdbcTemplate jdbcTemplate){
		List br_detail_result = null;
		
		/*String br_detail_query="select INST_ID,BRANCH_CODE,BRANCH_NAME,BR_ADDR1,BR_ADDR2,BR_ADDR3,BR_CITY,BR_STATE,BR_PHONE,BR_FAX_NUM,BR_EMAIL,BR_MANAGER,BRANCHIP,BRANCHUNAME,BRANCHPWD,MAKER_ID,MAKEDATE,CHEKR_ID,CHCKDATE,MKCK_STATUS,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from BRANCH_MASTER where INST_ID ='"+inst_id+"' and BRANCH_CODE='"+br_code+"'";
		enctrace(br_detail_query);
		br_detail_result =jdbcTemplate.queryForList(br_detail_query);*/
		
		//by gowtham-190819
		String br_detail_query="select INST_ID,BRANCH_CODE,BRANCH_NAME,BR_ADDR1,BR_ADDR2,BR_ADDR3,BR_CITY,BR_STATE,BR_PHONE,BR_FAX_NUM,BR_EMAIL,BR_MANAGER,BRANCHIP,BRANCHUNAME,BRANCHPWD,MAKER_ID,MAKEDATE,CHEKR_ID,CHCKDATE,MKCK_STATUS,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','Deauthorized') AS AUTH_CODE,NVL(REMARKS,'--') AS REMARKS from BRANCH_MASTER where INST_ID =? and BRANCH_CODE=?";
		enctrace(br_detail_query);
		br_detail_result =jdbcTemplate.queryForList(br_detail_query,new Object[]{inst_id,br_code});
		
		return br_detail_result;
	}
	public String updateBranchMaster(String inst_id,String br_code,String br_name,String br_addr1,String br_addr2,String br_addr3,String br_city,String br_state,String br_phone){
		
		//by gowtham-190819
		//String update_branch_query = "update BRANCH_MASTER set BRANCH_NAME='"+br_name.toUpperCase()+"',BR_ADDR1='"+br_addr1.toUpperCase()+"',BR_ADDR2='"+br_addr2.toUpperCase()+"',BR_ADDR3='"+br_addr3.toUpperCase()+"',BR_CITY='"+br_city.toUpperCase() +"',BR_PHONE='"+br_phone.toUpperCase()+"',BR_STATE='"+br_state.toUpperCase() +"' where INST_ID='"+inst_id+"' and BRANCH_CODE='"+br_code+"'";
		String update_branch_query = "update BRANCH_MASTER set BRANCH_NAME='"+br_name.toUpperCase()+"',BR_ADDR1='"+br_addr1.toUpperCase()+"',BR_ADDR2='"+br_addr2.toUpperCase()+"',BR_ADDR3='"+br_addr3.toUpperCase()+"',BR_CITY='"+br_city.toUpperCase() +"',BR_PHONE='"+br_phone.toUpperCase()+"',BR_STATE='"+br_state.toUpperCase() +"' where INST_ID='"+inst_id+"' and BRANCH_CODE='"+br_code+"'";
		
		return update_branch_query;
	}
	public int delBranchMaster(String inst_id,String br_code,JdbcTemplate jdbctemplate) {
		
		/*String delete_query="delete from BRANCH_MASTER where INST_ID ='"+inst_id+"' and BRANCH_CODE='"+br_code+"'" ;
		enctrace("delete_query :" + delete_query);*/
		
		//by gowtham-190819
		String delete_query="delete from BRANCH_MASTER where INST_ID =? and BRANCH_CODE=?" ;
		// by gowtham-190819
		int x = jdbctemplate.update(delete_query, new Object[] { inst_id, br_code });

		enctrace("delete_query :" + delete_query);
		
		return x;
	}
	
	public String getAuthBranch (String instid,String branchid,JdbcTemplate jdbctemplate)
	{
		String authstatus= null;
		
		/*String authsubqry = "SELECT AUTH_CODE FROM BRANCH_MASTER WHERE BRANCH_CODE='"+branchid+"' AND INST_ID='"+instid+"'";
		enctrace("authsubqry --- "+authsubqry);
		authstatus = (String)jdbctemplate.queryForObject(authsubqry, String.class);*/
		
		//by gowtham-190819
		String authsubqry = "SELECT AUTH_CODE FROM BRANCH_MASTER WHERE BRANCH_CODE=? AND INST_ID=?";
		enctrace("authsubqry --- "+authsubqry);
		authstatus = (String)jdbctemplate.queryForObject(authsubqry,new Object[]{branchid,instid}, String.class);
		
		return authstatus;
	}
	public int updateDeAuthBranchStatus(String instid, String mkrstatus,String branchid, String usercode,String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		/*String updstatusqry = "UPDATE BRANCH_MASTER SET MKCK_STATUS='"+mkrstatus+"',AUTH_CODE='9', CHEKR_ID='"+usercode+"', CHCKDATE=sysdate,REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"+branchid+"'";
		enctrace("updstatusqry : " + updstatusqry );
		x = jdbctemplate.update(updstatusqry);*/
		
		//by gowtham-190819
		Date date =  new Date();
		String updstatusqry = "UPDATE BRANCH_MASTER SET MKCK_STATUS=?,AUTH_CODE=?, CHEKR_ID=?, CHCKDATE=?,REMARKS=? WHERE INST_ID=? AND BRANCH_CODE=?";
		enctrace("updstatusqry : " + updstatusqry );
		x = jdbctemplate.update(updstatusqry,new Object[]{mkrstatus,"9",usercode,date.getCurrentDate(),remarks,instid,branchid});
		
		return x;
	}
	public int updateAuthBranchStatus(String instid,String mkrstatus,String branchid, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		String updstatusqry = "UPDATE BRANCH_MASTER SET MKCK_STATUS='"+mkrstatus+"',AUTH_CODE='1', CHEKR_ID='"+usercode+"', CHCKDATE=sysdate  WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"+branchid+"'";
		enctrace("updstatusqry : " + updstatusqry );
		x = jdbctemplate.update(updstatusqry);
		
		return x;
	}
	
	public int branchbaseexit(String instid, String  branchid,	JdbcTemplate jdbctemplate) {
		int x=-1;
	/*	
		String checexist = "SELECT COUNT(*) AS CNT FROM BASENO WHERE INST_ID='"+instid+"'   AND BASENO_CODE='"+branchid+"'";
	//	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'
		enctrace("checkcardexist : " + checexist );
		x = jdbctemplate.queryForInt(checexist);*/

		
		String checexist = "SELECT COUNT(*) AS CNT FROM BASENO WHERE INST_ID=?   AND BASENO_CODE=?";
		//	select * from CARD_ACCOUNT_INFO where P_ACCT_NO='"+accountno+"'
			enctrace("checkcardexist : " + checexist );
			x = jdbctemplate.queryForInt(checexist,new Object[]{instid,branchid});
		return x;
	}
	
	
	public List getBrlistQueryForChecker(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		
		/*String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+i_Name+"' and  AUTH_CODE='0' AND MKCK_STATUS='M' ";
		enctrace("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);*/
		
		//by gowtham-190819
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID =? and  AUTH_CODE=? AND MKCK_STATUS=? ";
		enctrace("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query,new Object[]{i_Name,"0","M"});
		
		return br_list_result;
	}
	public List getBrlistQueryForMaker(String i_Name,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		
		/*String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+i_Name+"'";
		enctrace("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);*/
		
		//by gowtham-190819
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID =?";
		enctrace("br_list_query--> "+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query,new Object[]{i_Name});
		
		return br_list_result;
	}
	
	public List getBranchListForEdit(String instid,JdbcTemplate jdbcTemplate)
	{
		List br_list_result = null;
		
		/*String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID ='"+instid+"' and DELETED_FLAG !='2' AND BRANCH_CODE !='000' ";
		enctrace("br_list_query :"+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query);*/

		//by gowtham-190819
		String br_list_query="select BRANCH_NAME,BRANCH_CODE from BRANCH_MASTER where INST_ID =? and DELETED_FLAG !=? AND BRANCH_CODE !=? ";
		enctrace("br_list_query :"+br_list_query);
		br_list_result=jdbcTemplate.queryForList(br_list_query,new Object[]{instid,"2","000"});
		
		return br_list_result;
	}
	  
	
	
	
	public List gettingBinList(String instid, JdbcTemplate jdbctemplate) {
		List res = null;
		try {
			
			/*String qry = "SELECT BIN,BASELEN FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND ATTACH_BRCODE='Y'";
			enctrace("Query for binlist ::: "+qry);
			res = jdbctemplate.queryForList(qry);*/
			
			//by gowtham-190819
			String qry = "SELECT PRD_CODE, BASE_NO_LEN FROM PRODUCTINFO WHERE INST_ID=?";
			enctrace("Query for binlist ::: "+qry);
			res = jdbctemplate.queryForList(qry,new Object[]{instid,"Y"});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	
	
	
}
