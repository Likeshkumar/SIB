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
public class InstCardActivateProcessDAO extends BaseAction 
{ 
	private static final long serialVersionUID = 1L; 
	
	public int checkValidCard( String instid, String cardno, String tablename, String processcode, String table, JdbcTemplate jdbctemplate, HttpSession session ) throws Exception {  
		String subprodcnt_qry = "SELECT count(*)   FROM "+tablename+" WHERE INST_ID='"+instid+"'  AND CARD_NO='"+cardno+"' AND CARD_STATUS='"+processcode+"'";
		System.out.println("subprodcnt_qry :" + subprodcnt_qry);
		int x = jdbctemplate.queryForInt(subprodcnt_qry);
		if( x != 1 ){ 
			return -1;
		} 
		return 1;
		
	}
	public String getProductCode( String instid, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		
		/*String productqry ="SELECT PRODUCT_CODE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND "
		+ "CARD_NO='"+cardno+"' AND ROWNUM<=1";
		String productcode; 
		productcode = (String)jdbctemplate.queryForObject(productqry,String.class);*/
		
		///by gowtham
		String productqry ="SELECT PRODUCT_CODE FROM INST_CARD_PROCESS WHERE INST_ID=? AND "
				+ "CARD_NO=? AND ROWNUM<=?";
				String productcode; 
				productcode = (String)jdbctemplate.queryForObject(productqry,new Object[]{instid,cardno,"1"},String.class);
		
		return productcode; 
	}
	
	public String getProductBySubProduct( String instid, String subproduct, JdbcTemplate jdbctemplate ) throws Exception {
		String productqry ="SELECT PRODUCT_CODE FROM IFP_INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND SUB_PROD_ID='"+subproduct+"' AND ROWNUM<=1";
		String productcode; 
		productcode = (String)jdbctemplate.queryForObject(productqry,String.class);
		return productcode; 
	}
	
	public int getReadyForActivationCardCount(String instid, String orderrefno, String processttatus, JdbcTemplate jdbctemplate) throws Exception{ 
		
	/*	String orderdetailsqry ="SELECT COUNT(*) FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND "
		+ "ORDER_REF_NO='"+orderrefno+"'  AND CARD_STATUS='"+processttatus+"'";
		System.out.println("orderdetailsqry :" + orderdetailsqry);
		int cardcnt = jdbctemplate.queryForInt(orderdetailsqry);*/

		//by gowtham
		String orderdetailsqry ="SELECT COUNT(*) FROM INST_CARD_PROCESS WHERE INST_ID=? AND "
				+ "ORDER_REF_NO=?   AND CARD_STATUS=? ";
				System.out.println("orderdetailsqry :" + orderdetailsqry);
				int cardcnt = jdbctemplate.queryForInt(orderdetailsqry,new Object[]{instid,orderrefno,processttatus});
		
		return cardcnt;
	}
	
	public int checkAnyCardExistForCust( String instid, String custid, JdbcTemplate jdbctemplate ){ 
		String anycardexist = "SELECT COUNT(*) FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
		System.out.println("anycardexist :" + anycardexist);
		int cnt = jdbctemplate.queryForInt(anycardexist);
		return  cnt; 
	}
	
	public int deleteCardProcessRecords( String instid, String cardno, JdbcTemplate jdbctemplate, HttpSession session) throws Exception {
		
		try {
			
			/*String removeprocesscardqry = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND"
			+ " CARD_NO='"+cardno+"'";  
			//Deleting records from INST_CARD_PROCESS
			int card_del_suc = jdbctemplate.update(removeprocesscardqry);*/
			
			///by gowtham
			String removeprocesscardqry = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID=? AND"
					+ " CARD_NO=? ";  
					//Deleting records from INST_CARD_PROCESS
					int card_del_suc = jdbctemplate.update(removeprocesscardqry,new Object[]{instid,cardno});
			
			if( card_del_suc != 1 ){
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Could not delete card detail from instant process" );  
				return -1;
			}  
		} catch (Exception e) { 
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Could not delete Process records" + e  ); 
			return -1;
		} 
		
		return 1;
	}
	
	
	
	public int deleteCustomerProcessRecords( String instid, String cardno, String custid, JdbcTemplate jdbctemplate, HttpSession session) throws Exception{    
		int chkcustexist = this.checkAnyCardExistForCust( instid, custid, jdbctemplate ); 	//Deleting records from IFP_CUSTINFO_PROCESS
		if( chkcustexist == 0 ){ 
			
			/*String removeprocesscustqry = "DELETE FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' "
			+ "AND CIN='"+custid+"'";  
			System.out.println("removeprocesscustqry :" + removeprocesscustqry);
			int x = jdbctemplate.update(removeprocesscustqry);*/

			///byg owtham
			String removeprocesscustqry = "DELETE FROM IFP_CUSTINFO_PROCESS WHERE INST_ID=?  "
					+ "AND CIN=? ";  
					System.out.println("removeprocesscustqry :" + removeprocesscustqry);
					int x = jdbctemplate.update(removeprocesscustqry,new Object[]{instid,custid});
			
			if( x != 1 ){
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Could not delete customer detail from production" );  
				return -1;
			}
		}   
		return 1;
	}
	
	public int moveCardToProduction( String instid, String cardno,  JdbcTemplate jdbctemplate) throws Exception {
		String query=""; 
		int x = -1; 
		query = "INSERT INTO CARD_PRODUCTION  (";
		query += "INST_ID, CARD_NO, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV)";
		
		query += " SELECT INST_ID, CARD_NO, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, SYSDATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		
		/*query += "BIN, AUTH_DATE, STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		x = jdbctemplate.update(query);	*/
		
		//byg owtham
		query += "BIN, AUTH_DATE, STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1,"
		+ " CVV2, ICVV FROM INST_CARD_PROCESS WHERE INST_ID=? AND CARD_NO=? ";
		x = jdbctemplate.update(query,new Object[]{instid,cardno});	
		
		return x;		
	}
	
	
	public int updateCardStatus(String instid, String cardno, String activestatuscode, JdbcTemplate jdbctemplate, HttpSession session  ){
		try {
			String status = this.getCardStatusMapCode(instid, activestatuscode, jdbctemplate, session );
			if( status != null ){
				
				/*String updcardstatusqry = "UPDATE CARD_PRODUCTION SET STATUS_CODE = '"+status+"' "
				+ "WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
				int st = jdbctemplate.update( updcardstatusqry );*/
				
				//by gowtham
				String updcardstatusqry = "UPDATE CARD_PRODUCTION SET STATUS_CODE =? "
						+ "WHERE INST_ID=? AND CARD_NO=? ";
						int st = jdbctemplate.update( updcardstatusqry ,new Object[]{status,instid,cardno});
				
				return st;
			}else{
				return -1;
			}
		} catch (Exception e) {			
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public String getCardStatusMapCode(String instid, String activestatuscode, JdbcTemplate jdbctemplate, HttpSession session ){
		
		String  status = null;
		try {
			String statusqry = "SELECT STATUS FROM MAINTAIN_DESC WHERE INST_ID='"+instid+"' AND CARD_ACT_CODE='"+activestatuscode+"'";
			status = (String) jdbctemplate.queryForObject(statusqry, String.class);
		} catch (DataAccessException e) { 
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "COULD NOT GET STATUS OF THE CARD." + e  );   
			e.printStackTrace();
		}
		
		return status;
	}
	
	public int movePintoProduction( String instid, String cardno, JdbcTemplate jdbctemplate ){
		String pinqry = ""; 
		try {
			pinqry += "INSERT INTO IFP_PIN_PRODUCTION (";
			pinqry += "INST_ID, CARD_NO, PIN_OFFSET, CVV1, CVV2, PVV, PIN_DATE, ICVV, OLD_PIN_OFFSET, OLD_PIN_DATE, ORDER_FLAG, USER_CODE )";
			pinqry += "SELECT INST_ID, CARD_NO, PIN_OFFSET, CVV1, CVV2, PVV, PIN_DATE, ICVV, OLD_PIN_OFFSET, OLD_PIN_DATE, ORDER_FLAG, USER_CODE FROM  IFP_PIN_PROCESS";
			pinqry += " WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'"; 
			System.out.println( "Pin query : " + pinqry);
		} catch (Exception e) {
			return -1;  
		} 
		int x  = jdbctemplate.update(pinqry);
		return x;
	}
	
	public String getCustomerId( String instid, String cardno, JdbcTemplate jdbctemplate) throws Exception { 
		String customerid = null;
		
		/*String customeridqry = "SELECT CIN FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' ";
		System.out.println( "customeridqry : " + customeridqry );
		customerid = (String)jdbctemplate.queryForObject(customeridqry, String.class);*/
		
		//by gowtham
		String customeridqry = "SELECT CIN FROM INST_CARD_PROCESS WHERE INST_ID=? AND CARD_NO=? ";
		System.out.println( "customeridqry : " + customeridqry );
		customerid = (String)jdbctemplate.queryForObject(customeridqry,new Object[]{instid,cardno}, String.class);
		
		return customerid;
	}
	

	public int insertCustToProduction( String instid, String custid, HttpSession session, JdbcTemplate jdbctemplate ) throws Exception{ 
		String custqry = ""; 
		custqry += " insert into IFP_CUSTINFO_PRODUCTION select * from IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
		System.out.println( " custqry : " + custqry);
		int y  = jdbctemplate.update(custqry);  
		return y; 
	}
	
	public int moveCustProcessToProduction( String instid, String cardno, String custid, JdbcTemplate jdbctemplate,   HttpSession session ) throws Exception {
		  
		try{
			
			String chk_kyc_qry = "SELECT KYC_FLAG FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' "
			+ "AND CIN='"+custid+"'";
			System.out.println( "chk_kyc_qry__" + chk_kyc_qry);
			String kyc = (String)jdbctemplate.queryForObject(chk_kyc_qry, String.class);
			
			System.out.println( "kyc value is__"+kyc);
			if( kyc.equals("0")){
				int newcustmove = this.insertCustToProduction( instid, custid, session, jdbctemplate );
				if( newcustmove != 1 ){
					return -1;
				}
			}else{
				int kyc_custmove = this.updateCustProduction( instid, custid, jdbctemplate, session );
				if( kyc_custmove > 0 ){
					return -1;
				}
			}
			
		}catch( Exception e){
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Could not delete customer detail from production" + e  );  
			return -1;
		}
		
		return 1; 
	}
	
	public int deleteCardActivationTable(String instid, String cardno, String entitycode, JdbcTemplate jdbctemplate, HttpSession session){
		try {
			String removecardactive = "DELETE FROM IFP_CARD_ACTIVE WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND ENTITY_CODE='"+entitycode+"'"; 
			String removecardactive_amt = "DELETE FROM IFP_CARD_ACTIVE_AMT WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND ACTION_CODE='"+entitycode+"'"; 
			jdbctemplate.update(removecardactive);
			jdbctemplate.update(removecardactive_amt);
			return 1;
		}catch(Exception e ){
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "COULD NOT DELETE CARD ACTIVE DETAILS AND AMOUNT " +e );  
			return -1;
		}
	}
	
	public int updateCustProduction( String instid, String custid, JdbcTemplate jdbctemplate, HttpSession session ){
		try{
			
			String fchcustdata = "SELECT * FROM IFP_CUSTINFO_PROCESS WHERE INST_ID='"+instid+"' AND CIN='"+custid+"' ";
			List custdata = jdbctemplate.queryForList(fchcustdata);
			if( custdata.isEmpty() ){
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No customer details found in process " + custid  ); 
				return -1;
			}
			else{
				
				Iterator itr = custdata.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					String firstname =(String) mp.get("FNAME");
					String midname =(String) mp.get("MNAME");
					String lastname =(String) mp.get("LNAME");
					String fahtername =(String) mp.get("FATHER_NAME");;
					String mothername =(String) mp.get("MOTHER_NAME");
					String gender =(String) mp.get("GENDER");
					String mstatus =(String) mp.get("MARITAL_STATUS");
					String spname =(String) mp.get("SPOUSE_NAME");
					
					String nationality =(String) mp.get("NATIONALITY");
					
					Date dob =(Date) mp.get("DOB");
					String mobileno =(String) mp.get("MOBILE_NO");
					String phoneno =(String) mp.get("PHONE_NO");
					String email =(String) mp.get("EMAIL_ADDRESS");
					
					String occupation =(String) mp.get("OCCUPATION");
					
					String reqdocuement =(String) mp.get("ID_DOCUMENT");
					String documentid =(String) mp.get("ID_NUMBER");
					
					String makerid  =(String) mp.get("MAKER_DATE");
					String makerdate  =(String) mp.get("MAKER_DATE");
					
					String checkerid  =(String) mp.get("CHECKER_ID");
					String checkderdate  =(String) mp.get("CHECKER_DATE");
					
					String mkckstatus =(String) mp.get("MKCK_STATUS");
					
					String custstatus = (String)mp.get("CUSTOMER_STATUS");
					
					String paddress1 =(String) mp.get("POST_ADDR1");
					String paddress2 =(String) mp.get("POST_ADDR2");
					String paddress3 =(String) mp.get("POST_ADDR3");
					String paddress4 =(String) mp.get("POST_ADDR4");
					
					String resaddress1 =(String) mp.get("RES_ADDR1");
					String resaddress2 =(String) mp.get("RES_ADDR2");
					String resaddress3 =(String) mp.get("RES_ADDR3");
					String resaddress4 =(String) mp.get("RES_ADDR4");
					
					String kycflag = (String) mp.get("KYC_FLAG");
					
					String updqry = "UPDATE IFP_CUSTINFO_PRODUCTION SET ";
					updqry += "FNAME='"+firstname+"',MNAME='"+midname+"',LNAME='"+lastname+"',FATHER_NAME='"+fahtername+"',MOTHER_NAME='"+mothername+"',MARITAL_STATUS='"+mstatus+"',";
					updqry += "SPOUSE_NAME='"+spname+"',GENDER='"+gender+"',DOB='"+dob+"',NATIONALITY='"+nationality+"',EMAIL_ADDRESS='"+email+"',";
					updqry += "MOBILE_NO='"+mobileno+"',PHONE_NO='"+phoneno+"',OCCUPATION='"+occupation+"',ID_NUMBER='"+documentid+"' ,ID_DOCUMENT='"+reqdocuement+"',";
					updqry += "MAKER_DATE='"+makerdate+"',MAKER_ID='"+makerid+"',CHECKER_DATE='"+checkderdate+"',CHECKER_ID='"+checkerid+"',MKCK_STATUS='"+mkckstatus+"',";
					updqry += "CUSTOMER_STATUS='"+custstatus+"',POST_ADDR1='"+paddress1+"',POST_ADDR2='"+paddress2+"',POST_ADDR3='"+paddress3+"',POST_ADDR4='"+paddress4+"',";
					updqry += "RES_ADDR1='"+resaddress1+"',RES_ADDR2='"+resaddress2+"',RES_ADDR3='"+resaddress3+"',RES_ADDR4='"+resaddress4+"',KYC_FLAG='"+kycflag+"' ";
					updqry += "WHERE INST_ID='"+instid+"' AND CIN='"+custid+"'";
					
					System.out.println("updqry__"+updqry); 
					int upd_cnt = jdbctemplate.update(updqry);
					if( upd_cnt != 1 ){
						return -1;
					}
				}
			}
		}catch(Exception e){
			return -1;
		}
		return 1;  
	}
	
	
} // class end
 
 
