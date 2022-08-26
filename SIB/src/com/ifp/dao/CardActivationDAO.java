package com.ifp.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class CardActivationDAO extends BaseAction {

	
	
	
	
	public List getProductList(String instid, String processcode, String processtype,String mkckstatus, JdbcTemplate jdbctemplate) throws Exception {
		List prodlist = null;
		String tablename = "";
		if( processtype.equals("INST")){	tablename = "INST_CARD_PROCESS";	}
		else if ( processtype.equals("PERS")){ tablename = "PERS_CARD_PROCESS";	}
		else{	return null; }
		
		String prodlistqry = "SELECT PRODUCT_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CARD_STATUS='"+processcode+"' and MKCK_STATUS='"+mkckstatus+"'  GROUP BY  PRODUCT_CODE ";
		enctrace("product query : " + prodlistqry );
		prodlist = jdbctemplate.queryForList(prodlistqry);
		return prodlist;
	}
	
	public List getinstProductList(String instid, String processcode, String processtype, JdbcTemplate jdbctemplate) throws Exception {
		List prodlist = null;
		String tablename = "";
		if( processtype.equals("INST")){	tablename = "INST_CARD_PROCESS";	}
		else if ( processtype.equals("PERS")){ tablename = "PERS_CARD_PROCESS";	}
		else{	return null; }
		
		String prodlistqry = "SELECT PRODUCT_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND mkck_status='M'AND CARD_STATUS='"+processcode+"'  GROUP BY  PRODUCT_CODE ";
		enctrace("product query : " + prodlistqry );
		prodlist = jdbctemplate.queryForList(prodlistqry);
		return prodlist;
	}
	public List generateBranchList(String instid, String processcode, String processtype,String mkckstatus, JdbcTemplate jdbctemplate) throws Exception {
		List brachlist = null;
		String tablename = "";
		if( processtype.equals("INST")){	tablename = "INST_CARD_PROCESS";	}
		else if ( processtype.equals("PERS")){ tablename = "PERS_CARD_PROCESS";	}
		else{	return null; }
		
		String brachlistqry = "SELECT trim(BRANCH_CODE) FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CARD_STATUS='"+processcode+"' and MKCK_STATUS='"+mkckstatus+"'  GROUP BY  BRANCH_CODE ";
		enctrace("branch query : " + brachlistqry );
		brachlist = jdbctemplate.queryForList(brachlistqry);
		return brachlist;
	}

	/**
	 * This method is used to get list of cards waiting for card issuence at maker side
	 * 
	 * @param instid
	 * @param prodcode
	 * @param processcode
	 * @param processtype
	 * @param condition
	 * @param jdbctemplate
	 * @return
	 * @throws Exception
	 */
	
	
	public List getCardActivationWaitingCardsINST (String instid, String prodcode, String processcode, String processtype,String condition, JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null; 
		String tablename = "";
		if( processtype.equals("INST")){	tablename = "INST_CARD_PROCESS";	}
		else if ( processtype.equals("PERS")){ tablename = "PERS_CARD_PROCESS";	}
		else{	return null; }
		
		//String cardlistqry = "SELECT ORG_CHN,ORDER_REF_NO, CARD_NO,MCARD_NO, CIN, SUB_PROD_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE, BRANCH_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CARD_STATUS='"+processcode+"' AND PRIVILEGE_CODE='02P' AND mkck_status='P' "+condition+" ORDER BY  ORG_CHN,ORDER_REF_NO ";
		
		//COMMANTED ON 12-FRB-20211 
		//String cardlistqry = "SELECT ORG_CHN,ORDER_REF_NO,MCARD_NO, CIN, SUB_PROD_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE, BRANCH_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CARD_STATUS='"+processcode+"' AND PRIVILEGE_CODE='02P' AND mkck_status='P' "+condition+" ORDER BY  ORG_CHN,ORDER_REF_NO ";
		
		String cardlistqry = "SELECT A.ORG_CHN,A.ORDER_REF_NO,A.MCARD_NO, A.CIN, A.SUB_PROD_ID, A.EMB_NAME, "
				+ "TO_CHAR(A.RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(A.PRE_DATE,'DD-MON-YYYY') AS PRE_DATE, "
				+ "A.BRANCH_CODE, (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE=A.PRODUCT_CODE) AS PRODUCUT_DESC FROM "+tablename+" A,INST_PRE_DATA B "
				+ "WHERE A.ORG_CHN=B.CARD_NO AND A.INST_ID='"+instid+"' AND A.CARD_STATUS='"+processcode+"' AND "
				+ "A.PRIVILEGE_CODE='02P' AND A.mkck_status='P' AND B.DOWN_CNT!=0   "+condition+" ORDER BY  A.ORG_CHN,A.ORDER_REF_NO ";
		
		enctrace("cardlistqry----------------> : " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	
	public List getCardActivationWaitingCards (String instid, String prodcode, String processcode, String processtype,String condition, JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null; 
		String tablename = "";
		if( processtype.equals("INST")){	tablename = "INST_CARD_PROCESS";	}
		else if ( processtype.equals("PERS")){ tablename = "PERS_CARD_PROCESS";	}
		else{	return null; }
		
		//String cardlistqry = "SELECT ORG_CHN,ORDER_REF_NO, CARD_NO,MCARD_NO, CIN, SUB_PROD_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE, BRANCH_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CARD_STATUS='"+processcode+"' AND PRIVILEGE_CODE='02P' AND mkck_status='P' "+condition+" ORDER BY  ORG_CHN,ORDER_REF_NO ";
		
		String cardlistqry = "SELECT ORG_CHN,ORDER_REF_NO,MCARD_NO, CIN, SUB_PROD_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE, BRANCH_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND CARD_STATUS='"+processcode+"' AND PRIVILEGE_CODE='02P' AND mkck_status='P' "+condition+" ORDER BY  ORG_CHN,ORDER_REF_NO ";
		
		enctrace("cardlistqry----------------> : " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	
	
	
/**
 * This method is used to get list of cards waiting for card issueance authorization
 * 
 * @param instid
 * @param prodcode
 * @param processcode
 * @param processtype
 * @param condition
 * @param jdbctemplate
 * @return List
 * @throws Exception
 */
	
	public List authgetCardActivationWaitingCards (String instid, String prodcode, String processcode, String processtype, String condition, JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null; 
		String tablename = "";
		if( processtype.equals("INST")){	tablename = "INST_CARD_PROCESS";	}
		else if ( processtype.equals("PERS")){ tablename = "PERS_CARD_PROCESS";	}
		else{	return null; }
		
		//String cardlistqry = "SELECT ORDER_REF_NO,HCARD_NO, CARD_NO,MCARD_NO,ACCT_NO, CIN, SUB_PROD_ID,MAKER_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE,TO_CHAR(MAKER_DATE,'DD-MON-YYYY') AS MAKER_DATE, TO_CHAR(ISSUE_DATE,'DD-MON-YYYY') AS ISSUE_DATE,BRANCH_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"'  AND CARD_STATUS='"+processcode+"' and mkck_status='M' "+condition+"  ORDER BY  ORDER_REF_NO ";
		
		String cardlistqry = "SELECT ORDER_REF_NO,ORG_CHN,MCARD_NO,ACCT_NO, CIN, SUB_PROD_ID,MAKER_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE,TO_CHAR(MAKER_DATE,'DD-MON-YYYY') AS MAKER_DATE, TO_CHAR(ISSUE_DATE,'DD-MON-YYYY') AS ISSUE_DATE,BRANCH_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"'  AND CARD_STATUS='"+processcode+"' and mkck_status='M' "+condition+"  ORDER BY  ORDER_REF_NO ";
		enctrace("authgetCardActivationWaitingCards " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	
	
	public List authgetCardActivationWaitingCardsINSTAUTH (String instid, String prodcode, String processcode, String processtype, String condition, JdbcTemplate jdbctemplate) throws Exception {
		List cardlist = null; 
		String tablename = "";
		System.out.println("prodcode-->"+prodcode);
		if( processtype.equals("INST")){	tablename = "INST_CARD_PROCESS";	}
		else if ( processtype.equals("PERS")){ tablename = "PERS_CARD_PROCESS";	}
		else{	return null; }
		
		//String cardlistqry = "SELECT ORDER_REF_NO,HCARD_NO, CARD_NO,MCARD_NO,ACCT_NO, CIN, SUB_PROD_ID,MAKER_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE,TO_CHAR(MAKER_DATE,'DD-MON-YYYY') AS MAKER_DATE, TO_CHAR(ISSUE_DATE,'DD-MON-YYYY') AS ISSUE_DATE,BRANCH_CODE FROM "+tablename+" WHERE INST_ID='"+instid+"'  AND CARD_STATUS='"+processcode+"' and mkck_status='M' "+condition+"  ORDER BY  ORDER_REF_NO ";
		
		String cardlistqry = "SELECT ORDER_REF_NO,ORG_CHN,MCARD_NO,ACCT_NO, CIN, SUB_PROD_ID,MAKER_ID, EMB_NAME, TO_CHAR(RECV_DATE,'DD-MON-YYYY') AS RECV_DATE,TO_CHAR(PRE_DATE,'DD-MON-YYYY') AS PRE_DATE,TO_CHAR(MAKER_DATE,'DD-MON-YYYY') AS MAKER_DATE, TO_CHAR(ISSUE_DATE,'DD-MON-YYYY') AS ISSUE_DATE,BRANCH_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE='"+prodcode+"') AS PRODUCUT_DESC  FROM "+tablename+" WHERE INST_ID='"+instid+"'  AND CARD_STATUS='"+processcode+"' and mkck_status='M' "+condition+"  ORDER BY  ORDER_REF_NO ";
		enctrace("authgetCardActivationWaitingCards " + cardlistqry );
		cardlist = jdbctemplate.queryForList(cardlistqry);
		return cardlist;
	} 
	
	public int insertAcctounInfo(String instid, String acctno, String curcode, String primflag, JdbcTemplate jdbctemplate ) throws Exception {
		String inserqry = "INSERT INTO ACCOUNTINFO ( INST_ID, ACCT_NO, ACCT_CCY, ACCT_STATUS, AVAIL_BALANCE,FEE_AMOUNT,COMMISSION_AMOUNT,EXPENSE_AMOUNT,DISCOUNT_AMOUNT, ACCT_TYPE, PRIM_FLAG, LEDGER_BALANCE) VALUES " ;
		inserqry += " ( '"+instid+"','"+acctno+"','"+curcode+"','1',0,0,0,0,0,'S', '"+primflag+"', '0' )"; 
		enctrace("Secondary insert : " + inserqry);
		int x = jdbctemplate.update(inserqry);
		return x;
	}

	public int insertCardAccountLink(String instid, String cardno, String customerid, String acctno, String curcode, String usercode, 
			String primflag, JdbcTemplate jdbctemplate) {
		String insertcardlink = "INSERT INTO IFD_CARD_ACCT_LINK ( INST_ID, CARD_NO, CIN, ACCT_NO, MAKER_DATE, ACCT_CCY, ACCT_STATUS, MAKER_ID, ACCT_PRIORITY) VALUES";
		insertcardlink += "('"+instid+"','"+cardno+"','"+customerid+"','"+acctno+"', sysdate, '"+curcode+"','1','"+usercode+"', '"+primflag+"')";
		enctrace("Secondary insert : " + insertcardlink);
		int acctinsert = jdbctemplate.update(insertcardlink); 
		return acctinsert ;		 
	}
	
	public int moveCardToProduction( String instid, String cardno,  String cardactivecode, String switchardstatus, JdbcTemplate jdbctemplate) throws Exception {
		String query=""; 
		int x = -1; 
		query = "INSERT INTO CARD_PRODUCTION  (";
		query += "INST_ID, CARD_NO, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV, CARD_REF_NO,HCARD_NO,MCARD_NO)";
		
		query += " SELECT INST_ID, CARD_NO, CIN, ORDER_REF_NO, '"+cardactivecode+"', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, PIN_DATE, RECV_DATE, SYSDATE, SYSDATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, '"+switchardstatus+"', PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV, CARD_REF_NO,HCARD_NO,MCARD_NO FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		x = jdbctemplate.update(query);		 
		return x;		
	}
	
	public int insertCustToProduction( String instid, String custid, HttpSession session, JdbcTemplate jdbctemplate ) throws Exception{ 
		String custqry = ""; 
		custqry += " insert into IFP_CUSTINFO_PRODUCTION select * from IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
		enctrace( " custqry : " + custqry);
		int y  = jdbctemplate.update(custqry);  
		return y; 
	}
	
	
	
	public String getCardStatusMapCode(String instid, String activestatuscode, JdbcTemplate jdbctemplate ){ 
			String  status = null;
			try{ 
				String statusqry = "SELECT STATUS FROM MAINTAIN_DESC WHERE INST_ID='"+instid+"' AND CARD_ACT_CODE='"+activestatuscode+"'";
				status = (String) jdbctemplate.queryForObject(statusqry, String.class);
			} catch (EmptyResultDataAccessException e) {  }
			
			return status;
		}

 
	public int updateCardStatus(String instid, String cardno, String customerid, String activestatuscode, JdbcTemplate jdbctemplate ){ 
			String status = this.getCardStatusMapCode(instid, activestatuscode, jdbctemplate );
			if( status != null ){
				String updcardstatusqry = "UPDATE CARD_PRODUCTION SET STATUS_CODE = '"+status+"', CIN='"+customerid+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
				int st = jdbctemplate.update( updcardstatusqry );
				return st;
			}else{
				return -1;
			} 
	}
	
	public int deleteCardProcessRecords( String instid, String cardno, JdbcTemplate jdbctemplate) throws Exception { 
		String removeprocesscardqry = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("removeprocesscardqry : " + removeprocesscardqry );
		int card_del_suc = jdbctemplate.update(removeprocesscardqry); 
		return card_del_suc;
	}
	
	public Boolean customerRegisterRequired(String instid, String cardno, String subprod, JdbcTemplate jdbctemplate ){
		Boolean regreq = true;
		String cardreg_req_qry = "SELECT CUST_REG_REQ FROM IFP_INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND SUB_PROD_ID='"+subprod+"'";
		String cardreg_req = (String) jdbctemplate.queryForObject(cardreg_req_qry, String.class);
		if( cardreg_req.equals( "N")){
			regreq = false;
		}
		return regreq;
	}

	public int checkAnyCardExistForCust( String instid, String custid, JdbcTemplate jdbctemplate ){ 
		String anycardexist = "SELECT COUNT(*) FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
		enctrace("anycardexist :" + anycardexist);
		int cnt = jdbctemplate.queryForInt(anycardexist);
		return  cnt; 
	}
	
	public int deleteCustomerProcessRecords( String instid, String cardno, String custid, JdbcTemplate jdbctemplate) throws Exception{    
		int chkcustexist = this.checkAnyCardExistForCust( instid, custid, jdbctemplate ); 	//Deleting records from IFP_CUSTINFO_PROCESS
		if( chkcustexist == 0 ){ 
			String removeprocesscustqry = "DELETE FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";  
			enctrace("removeprocesscustqry :" + removeprocesscustqry);
			int x = jdbctemplate.update(removeprocesscustqry);
			return x;
		}   
		return 1;
	}
	
	public synchronized int updateCINcount(String instid, JdbcTemplate jdbctemplate ) throws Exception	{
		String updqry = "update IFP_SEQUENCE_MASTER set CIN_NO=CIN_NO+1 where INST_ID='"+instid+"'";
		int x = jdbctemplate.update(updqry);
		return x;		
	}
	
	public int updateUnKnownCustomerId( String instid, String cardno, String UNCIN , JdbcTemplate jdbctemplate ){ 
		String updqry = "UPDATE CARD_PRODUCTION SET CIN='"+UNCIN+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		String updacctlink = "UPDATE IFP_CARD_ACCT_LINK  SET CIN='"+UNCIN+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("updqry : " + updqry );
		enctrace("updacctlink : " + updacctlink );  
		int x = jdbctemplate.update(updqry);
		if( x== 1 ){
			int y = jdbctemplate.update(updacctlink);
			return y;
		}
		return -1;		
	}
	
	
	public int updatecardissancestatus(String instid,String cardno,String cardstatus,String mkckstatus,String cardcollectbranch,JdbcTemplate jdbctemplate) throws Exception{
		
		String updqry = "UPDATE INST_CARD_PROCESS SET CARD_STATUS='"+cardstatus+"',ISSUE_DATE=SYSDATE,mkck_status='"+mkckstatus+"',CARD_COLLECT_BRANCH='"+cardcollectbranch+"' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";
		enctrace("updcardiss status : " + updqry ); 
		int x = jdbctemplate.update(updqry);
		return x;		
	}
	
	public int eligiblecardstatusmap(String instid,String cardno,String cardstatus,String mkckstatus,JdbcTemplate jdbctemplate) throws Exception{
			
			String updqry = "UPDATE INST_CARD_PROCESS SET CARD_STATUS='"+cardstatus+"',mkck_status='"+mkckstatus+"' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";
			enctrace("updcardiss status : " + updqry ); 
			int x = jdbctemplate.update(updqry);
			return x;		
		}
	
}//end class
