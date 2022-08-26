package com.ifp.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonDesc;

@Transactional
public class CardMaintainActionDAO extends BaseAction 
{ 
	
	
	CommonDesc  commondesc=new CommonDesc();
	  
	public int checkCardExist (String instid, String cardno, JdbcTemplate jdbctemplate) throws Exception{
		int x = -1;
		String compupdqry = "SELECT COUNT(*) AS CNT FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("compupdqry : " + compupdqry );
		x = jdbctemplate.queryForInt(compupdqry);
		return x;
	}

	public List getCardDetails(String instid, String cardno, JdbcTemplate jdbctemplate) throws Exception {
		 List carddata = null;
		 String carddataqry = "SELECT BIN,CIN,ORDER_REF_NO,PRODUCT_CODE, SUB_PROD_ID, CARD_STATUS, CAF_REC_STATUS,CARD_TYPE_ID,BRANCH_CODE,TO_CHAR(CHECKER_DATE, 'DD-MON-YYYY') AS CHECKER_DATE,CHECKER_ID,APP_NO,TO_CHAR(APP_DATE,'DD-MON-YYYY') AS APP_DATE,STATUS_CODE, MAKER_ID, TO_CHAR(MAKER_DATE, 'DD-MON-YYYY') AS MAKER_DATE, " ;
		 carddataqry += " TO_CHAR(EXPIRY_DATE, 'dd-mon-yyyy') AS EXPIRY_DATE, MOBILENO,MKCK_STATUS,EMB_NAME,TO_CHAR(REISSUE_DATE, 'DD-MON-YYYY') AS REISSUE_DATE FROM CARD_PRODUCTION ";
		 carddataqry += " WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'" ;
		 enctrace("carddataqry :" + carddataqry );
		 carddata = jdbctemplate.queryForList(carddataqry);
		 return carddata ; 
	}
	
	public int checkCardExistinPersonalization (String instid, String cardno, JdbcTemplate jdbctemplate) throws Exception{
		int x = -1;
		String personalizeqry = "SELECT COUNT(*) AS CNT FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("personalizeqry : " + personalizeqry );
		x = jdbctemplate.queryForInt( personalizeqry );
		return x;
	}
	
	public int updateCardStatus(String instid,String padssenable, String cardno, String cardstatus, String switchcardstatus, String cafrecstatus, String usercode, String TABLENAME, String collectbranch, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;String update_status_qry="";
		if(padssenable.equals("Y")){
			trace("cardproduction status :::  ++++++ "+switchcardstatus);
		update_status_qry = "UPDATE "+TABLENAME+" SET  CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchcardstatus+"', CAF_REC_STATUS='"+cafrecstatus+"', MAKER_ID='"+usercode+"', MAKER_DATE=sysdate WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";
		}
		else
		{
			update_status_qry = "UPDATE "+TABLENAME+" SET  CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchcardstatus+"', CAF_REC_STATUS='"+cafrecstatus+"', MAKER_ID='"+usercode+"', MAKER_DATE=sysdate WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";	
		}
		enctrace("update_status_qry :" + update_status_qry  );
		x = jdbctemplate.update(update_status_qry);
		return x;
	}
	
	public int insertMainintainHistory(String instid, String cardno, String prevstatus, String currentstatus, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		CommonDesc commondesc = new CommonDesc();
		int binlen = commondesc.getBinLen(instid, cardno, jdbctemplate);
		
		String maintain_history_qry  = "INSERT INTO MAINTAIN_HISTORY(INST_ID, BIN, CARD_NO, PREV_STATUS, CHANGED_STATUS, ACTION_DATE, CHANGED_BY)";
		maintain_history_qry  += "VALUES ('"+instid+"', '"+cardno.substring(0, binlen)+"', '"+cardno+"', '"+prevstatus+"','"+currentstatus+"', SYSDATE, '"+usercode+"')";
		//enctrace("maintain_history_qry :" + maintain_history_qry  );
		x = jdbctemplate.update(maintain_history_qry);
		return x;		   
	}

	public int checkValidCardForReissue(String instid, String cardno, String notactivatestatuscode, JdbcTemplate jdbctemplate) {
		int x = -1;
		String validcardqry = "SELECT COUNT(*) AS CNT FROM CARD_PRODUCTION  WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND CARD_STATUS='"+notactivatestatuscode+"'";
		enctrace("validcardqry : " + validcardqry );
		x = jdbctemplate.queryForInt( validcardqry );
		return x;
	}

	public int deleteCardFromProduction(String instid, String cardno, JdbcTemplate jdbctemplate ){
		int x = -1;
		String deletecardqry = "DELETE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("deletecardqry :" + deletecardqry  );
		x = jdbctemplate.update(deletecardqry);
		return x;
	}
	
	public int deleteAccountNumber(String instid, String cardno, JdbcTemplate jdbctemplate ){
		int x = -1;
		String deletecardqry = "DELETE FROM ACCOUNTINFO WHERE INST_ID='"+instid+"' AND ACCT_NO='"+cardno+"'";
		enctrace("deletecardqry :" + deletecardqry  );
		x = jdbctemplate.update(deletecardqry);
		return x;
	}
	
	public int deleteCardAcctLink(String instid, String acctno, JdbcTemplate jdbctemplate ){
		int x = -1;
		//String deletecardqry = "DELETE FROM IFP_CARD_ACCT_LINK WHERE INST_ID='"+instid+"' AND CARD_NO='"+acctno+"'";
		String deletecardqry = "UPDATE IFP_CARD_ACCT_LINK SET CARD_NO='"+acctno+"_0'  WHERE  INST_ID='"+instid+"' AND CARD_NO='"+acctno+"'";
		enctrace("deletecardqry :" + deletecardqry  );
		x = jdbctemplate.update(deletecardqry);
		return x;
	}
	
	public String getAccountNumberByCard(String instid, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		String acctno = null;
		try{
			String acctnoqry = "SELECT ACCT_NO FROM IFP_CARD_ACCT_LINK WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
			trace("acctnoqry :" + acctnoqry );
			acctno = (String)jdbctemplate.queryForObject(acctnoqry, String.class) ;
		}catch(EmptyResultDataAccessException e){}
		return acctno;
	}

	

 /*	public int reIssueCardProduction(String instid, String oldcardno, String newcardno, String newcardstatus, String switchstatus, String cafrecstatus,
			String usercode, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1; 
		String update_status_qry = "UPDATE CARD_PRODUCTION SET CARD_NO='"+newcardno+"', ORG_CHN='"+oldcardno+"', MAKER_ID='"+usercode+"',CARD_STATUS='"+newcardstatus+"', " ;
		update_status_qry += " STATUS_CODE='"+switchstatus+"', CAF_REC_STATUS='"+cafrecstatus+"', MAKER_DATE=sysdate, REISSUE_DATE=sysdate WHERE INST_ID='"+instid+"' AND CARD_NO='"+oldcardno+"'";
		enctrace("update_status_qry :" + update_status_qry  );
		x = jdbctemplate.update(update_status_qry);
		return x;
	}
*/

	
	public int reIssueCardProduction(String instid, String oldcardno, String newcardno, String customerid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1; 
		String update_status_qry = "UPDATE CARD_PRODUCTION SET  ORG_CHN='"+oldcardno+"', CIN='"+customerid+"'  WHERE INST_ID='"+instid+"' AND CARD_NO='"+newcardno+"'" ; 
		enctrace("update_status_qry :" + update_status_qry  );
		x = jdbctemplate.update(update_status_qry);
		
		String updatereissueqry = "UPDATE CARD_PRODUCTION SET  REISSUE_DATE=SYSDATE WHERE INST_ID='"+instid+"' AND CARD_NO='"+oldcardno+"'" ; 
		enctrace("updatereissueqry :" + updatereissueqry  );
		x = jdbctemplate.update(updatereissueqry);
		
		return x;
	}

	
	
	/*public int reIssueCardProduction(String instid, String oldcardno, String newcardno, String cin, String newcardstatus, String switchstatus, String cafrecstatus,
			String usercode, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		InstCardActivateProcessDAO cardactivedao = new InstCardActivateProcessDAO();
		cardactivedao.moveCardToProduction(instid, newcardno, jdbctemplate);
		String update_status_qry = "UPDATE CARD_PRODUCTION SET  CIN='"+cin+"', ORG_CHN='"+oldcardno+"'";
		update_status_qry += " WHERE INST_ID='"+instid+"' AND CARD_NO='"+newcardno+"'";
		enctrace("update_status_qry :" + update_status_qry  );
		x = jdbctemplate.update(update_status_qry);
		return x;
	}*/
	
	public int updateCardAccountLink(String instid, String oldcardno, String newcardno, JdbcTemplate jdbctemplate ){
		int x = -1;
		String updateoriginalchn = "UPDATE IFP_CARD_ACCT_LINK SET CARD_NO='"+newcardno+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+oldcardno+"'";
		enctrace("updateoriginalchn :" + updateoriginalchn  );
		x = jdbctemplate.update(updateoriginalchn);
		return x;
	}
	
	public List getProdcutionCardDetails(String instid, String cardno,String padssenable, String TABLENAME, JdbcTemplate jdbctemplate ) throws Exception {
		List carddata = null;
		String maintainqry=null;
		if(padssenable.equals("Y")){
		maintainqry = "SELECT * FROM "+TABLENAME+"  WHERE INST_ID='"+instid+"' AND order_ref_no =(select order_ref_no from card_production_hash where  HCARD_NO  ='"+cardno+"')";
		}else
		{
			maintainqry = "SELECT * FROM "+TABLENAME+"  WHERE INST_ID='"+instid+"'AND CARD_NO ='"+cardno+"'";	
		}
		
		enctrace("maintainqry : " + maintainqry  );
		carddata = jdbctemplate.queryForList(maintainqry);
		return carddata;
	}
	
	public List getApplicableActionList(String instid, String status_code, JdbcTemplate jdbctemplate ) throws Exception {
		List applacablelist = null;
		String applacablelistqry  = "SELECT APPLICABLE_ACTION FROM MAINTAIN_CONFIG WHERE INST_ID='"+instid+"' AND CARD_ACT_CODE='"+status_code+"' AND STATUS='1'";
		enctrace("applacablelist : " + applacablelistqry );
		applacablelist = jdbctemplate.queryForList(applacablelistqry);
		return applacablelist;
	}
	
	
	public List getactypedesc(String instid, String acctsubtypeid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		String accttypelistqry  = "SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND ACCTTYPEID='"+acctsubtypeid+"' and AUTH_CODE='1'";
		enctrace("accttypelistqry : " + accttypelistqry );
		acctypelist = jdbctemplate.queryForList(accttypelistqry);
		return acctypelist;
	}
	
	public List getaccountlist(String instid,String orderrefno,String hcardno,JdbcTemplate jdbctemplate) throws Exception{
		List acccountnolist = null;
		String acctnolistqry  = "SELECT ACCOUNTNO FROM ACCOUNTINFO WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+orderrefno+"' AND ACCOUNTNO NOT IN(SELECT ACCOUNT_NO FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' and ORG_CHN='"+hcardno+"')";
		enctrace("acctnolistqry : " + acctnolistqry );
		acccountnolist = jdbctemplate.queryForList(acctnolistqry);
		return acccountnolist;
		
	}
	
	public List getcardactivelist(String instid,String hcardno,JdbcTemplate jdbctemplate) throws Exception{
		List cardactivelist = null;
		/*String cardactivelistqry  = "SELECT TO_CHAR(ACTIVE_DATE) as ACTIVEDATE,CASE WHEN CAF_REC_STATUS='R' THEN  to_char(PIN_DATE) WHEN CAF_REC_STATUS!='R' THEN '--'  END as REPINDATE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND  HCARD_NO='"+hcardno+"'";*/
		String cardactivelistqry  = "SELECT TO_CHAR(ACTIVE_DATE) as ACTIVEDATE,CASE WHEN CAF_REC_STATUS='R' THEN  to_char(PIN_DATE) WHEN CAF_REC_STATUS!='R' THEN '--'  END as REPINDATE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND order_ref_no =(select order_ref_no from card_production_hash where  HCARD_NO = '"+hcardno+"')";
		enctrace("cardactivelistqry : " + cardactivelistqry );
		cardactivelist = jdbctemplate.queryForList(cardactivelistqry);
		return cardactivelist;
		
	}
	
public int moveCardToProcess( String instid,String padssenable,String accountno,String hahedcardno,String mcardno, String cardno, String newcardno, String caf_recstatus, String card_status,String expperiod,String changeexpreq, String TABLENAME, String usercode, String collectbranch, JdbcTemplate jdbctemplate ) {
		
	
	String orderrefno="";
	try {
		orderrefno = commondesc.generateorderRefno(instid, jdbctemplate);
	} catch (Exception e) {
		e.printStackTrace();
	}
	trace("Generated order reference number is : " + orderrefno);
		
		String padsscon = "";
		String expreqcond = "";
		if(padssenable.equals("Y")){
			padsscon = "HCARD_NO = '"+hahedcardno+"'";
		}else{padsscon="CARD_NO ='"+cardno+"'";}
	
		if(changeexpreq.equals("Y"))
		{
			expreqcond = "add_months(sysdate,"+expperiod+")"; 
		}
		else
		{
			expreqcond = "EXPIRY_DATE";
		}
		
		/*String query="";        	
		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH)";
	
		query += " SELECT INST_ID,'"+newcardno+"','"+hahedcardno+"','"+mcardno+"', '"+accountno+"',ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, '"+card_status+"', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "GENERATED_DATE, "+expreqcond+", PRE_DATE, '"+usercode+"', MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '"+caf_recstatus+"', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH FROM "+TABLENAME+"  WHERE INST_ID='"+instid+"' AND "+padsscon+"";
		       */
		String query="";        	
		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, ORG_CHN, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH)";
	
		query += " SELECT INST_ID,'"+newcardno+"','"+mcardno+"', '"+accountno+"',ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN ,'"+orderrefno+"','"+card_status+"', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "GENERATED_DATE, "+expreqcond+", PRE_DATE, '"+usercode+"', MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '"+caf_recstatus+"', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH FROM "+TABLENAME+"  WHERE INST_ID='"+instid+"' AND  ORG_CHN='"+newcardno+"'";
		
		enctrace( "moveCardToProcess : " + query ); 
		
		String insertIntoHash="INSERT INTO PERS_CARD_PROCESS_HASH(INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,"
				+ "BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)  SELECT INST_ID,'"+orderrefno+"',BIN,'"+hahedcardno
				+"','"+accountno+ "',CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE FROM"
						+ " CARD_PRODUCTION_HASH  WHERE INST_ID='"+instid+"' AND "+padsscon+"";


		enctrace( "moveCardToProcess  hash: " + insertIntoHash ); 
		
	      
		int x  = jdbctemplate.update(query);
		int y=jdbctemplate.update(insertIntoHash);
		
		if(x ==0|| y==0){
			trace("coludnot insert values into hash table");
		}
		
		return x+y;  
}

public int updateezcardinfo(String instid, String cardno,  String cardstatus, JdbcTemplate jdbctemplate) {
	int x = -1;
	String qry="";
	
	
	if(cardstatus.equals("50")){
	//cardstatus="53";
		cardstatus="50";
	enctrace("ezcardinfo status :::  ++++++++++++++++++++ "+cardstatus);
	qry ="UPDATE EZCARDINFO SET STATUS='"+cardstatus+"' WHERE INSTID='"+instid+"' AND CHN='"+cardno+"'";
	enctrace("updateezcardinfo::"+qry);
	
	}else{
		enctrace("ezcardinfo status :::  ++++++ "+cardstatus);
		qry ="UPDATE EZCARDINFO SET STATUS='"+cardstatus+"' WHERE INSTID='"+instid+"' AND CHN='"+cardno+"'";
		enctrace("updateezcardinfo::"+qry);
	}
	x  = jdbctemplate.update(qry);
	return x;
}

public int updateCardStatusDate(String instid, String cardno, String dbcolumnfld, String TABLENAME, JdbcTemplate jdbctemplate) {
	int x = -1;
	String qry ="UPDATE "+TABLENAME+" SET "+dbcolumnfld+"=SYSDATE WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";
	enctrace("lost/stolenqry : " + qry );
	x  = jdbctemplate.update(qry);
	return x;
}
	 
public int checkPersonalizecard (String instid, String cardno, JdbcTemplate jdbctemplate) throws Exception{
	int x = -1;
	String personalizeqry = "SELECT COUNT(*) AS CNT FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND ORDER_FLAG='P'";
	enctrace("personalizeqry : " + personalizeqry );
	x = jdbctemplate.queryForInt( personalizeqry );
	return x;
}
public int switchstatusforexpcard (String instid, String cardno, JdbcTemplate jdbctemplate) throws Exception{
	int x = -1;
	String personalizeqry = "SELECT COUNT(*) AS CNT FROM EZCARDINFO WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND ORDER_FLAG='P'";
	enctrace("personalizeqry : " + personalizeqry );
	x = jdbctemplate.queryForInt( personalizeqry );
	return x;
}

public int switchCardInfoUpdate(String instid, String hcardno,JdbcTemplate jdbctemplate) {
	int x = -1;
	String switchupdateqry = "SELECT COUNT(*) AS CNT FROM EZCARDINFO WHERE  INSTID='"+instid+"' AND STATUS ='62' AND CHN='"+hcardno+"'";
	enctrace("switchCardInfoUpdate : " + switchupdateqry );
	x = jdbctemplate.queryForInt( switchupdateqry );
	return x;
}

public int updateCardProduction(String instid, String cardno,JdbcTemplate jdbctemplate) {
	int x = -1;
	String produpdateqry = "UPDATE CARD_PRODUCTION SET CARD_STATUS='03', STATUS_CODE='74' WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"'";
	enctrace("produpdateqry : " + produpdateqry );
	x = jdbctemplate.update( produpdateqry );
	return x;
}

//TAG_NAME used for validating through maker side done 
public int checkValidCardForActive(String instid, String cardno, String mkckstatus, String notactivatestatuscode, JdbcTemplate jdbctemplate) {
	int x = -1;
	String validcardqry = "SELECT COUNT(*) AS CNT FROM CARD_PRODUCTION  WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"' AND TAG_NAME='1'  AND mkck_status='"+mkckstatus+"'";
	enctrace("validcardqry : " + validcardqry );
	x = jdbctemplate.queryForInt( validcardqry );
	return x;
}

public int makerCardStatusActive(String instid,String padssenable, String cardno, String cardstatus, String switchcardstatus, String cafrecstatus, String usercode,String mkckstatus, String TABLENAME, String collectbranch, JdbcTemplate jdbctemplate) throws Exception {
	int x = -1;String update_status_qry="";
	if(padssenable.equals("Y")){
	update_status_qry = "UPDATE "+TABLENAME+" SET  MKCK_STATUS='"+mkckstatus+"',TAG_NAME='1'  WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";
	}
	
	{
	update_status_qry = "UPDATE "+TABLENAME+" SET  MKCK_STATUS='"+mkckstatus+"' ,TAG_NAME='1'  WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";	
	}
	enctrace("update_status_qry Active maker:" + update_status_qry  );
	x = jdbctemplate.update(update_status_qry);
	return x;
}
public int updateCardStatusforActive(String instid,String padssenable, String cardno, String cardstatus, String switchcardstatus, String cafrecstatus, String usercode, String TABLENAME, String collectbranch, JdbcTemplate jdbctemplate) throws Exception {
	int x = -1;String update_status_qry="";
	if(padssenable.equals("Y")){
		trace("cardproduction status :::  ++++++ "+switchcardstatus);
	update_status_qry = "UPDATE "+TABLENAME+" SET  CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchcardstatus+"', CAF_REC_STATUS='"+cafrecstatus+"', CHECKER_ID='"+usercode+"', CHECKER_DATE=sysdate, TAG_NAME='0'  WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";
	}
	else
	{
		update_status_qry = "UPDATE "+TABLENAME+" SET  CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchcardstatus+"', CAF_REC_STATUS='"+cafrecstatus+"', MAKER_ID='"+usercode+"', MAKER_DATE=sysdate,TAG_NAME='0'  WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";	
	}
	enctrace("update_status_qry :" + update_status_qry  );
	x = jdbctemplate.update(update_status_qry);
	return x;
}

 

}//end class
 