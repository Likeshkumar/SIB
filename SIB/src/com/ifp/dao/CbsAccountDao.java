package com.ifp.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class CbsAccountDao extends BaseAction{

	public int validateActvatedCard(String instid,String padssenable,String cardno,JdbcTemplate jdbcTemplate)
	{
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
	/*	if(padssenable.equals("Y"))
		{*/
		//valdcardqry.append("select count(1) from CARD_PRODUCTION where Hcard_no in (select chn from EZCARDINFO ) and CARD_STATUS='05' and Hcard_no='"+cardno+"' AND INST_ID='"+instid+"' ");
			
		//valdcardqry.append("select count(1) from CARD_PRODUCTION where ORDER_REF_NO =(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO =(select chn from ezcardinfo where status in ('50','53') and Hcard_no='"+cardno+"'))");
		
		valdcardqry.append("select count(1) from CARD_PRODUCTION where ORDER_REF_NO in (SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO in (select chn from ezcardinfo where status in ('50','53') and Hcard_no='"+cardno+"'))");
		
		
		/*	}*/
		// BY SIVA
		
		/*else
		{
		//use thisquery			//select status from ezcardinfo where chn in (select hcard_no from CARD_PRODUCTION where card_no='F35DD6A752306B8C7359B2163E4EDC58')
			valdcardqry.append("select count(1) from CARD_PRODUCTION where card_no in (select chn from EZCARDINFO ) and CARD_STATUS='05' and card_no='"+cardno+"' AND INST_ID='"+instid+"' ");	
		}*/
		
		// BY SIVA
		enctrace("valdcardqry::"+valdcardqry.toString());  
		validcard = jdbcTemplate.queryForInt(valdcardqry.toString());
		return validcard;
	}
	public int validateaccountfordelinkaccount(String instid,String padssenable,String accountno,JdbcTemplate jdbcTemplate)
	{
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
			valdcardqry.append("select count(1) from ACCT_LINK where  ACCT_NO='"+accountno+"'AND  MKCK_STATUS='M' and FLAG='D' AND INST_ID='"+instid+"' ");	
		
		enctrace("validdelinkacc::"+valdcardqry.toString());  
		validcard = jdbcTemplate.queryForInt(valdcardqry.toString());
		return validcard;
	}
	
	public int insertAccountInfo(String instid, String orderrefno, String customerid,String accountnumber,String accounttype, String accountsubtype,String accountcurrency,String limitbasedon,String usercode, JdbcTemplate jdbctemplate) {
		int x = -1;
		 String insertcustdata = " INSERT INTO ACCOUNTINFO (" +
		 		"INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, ADDEDBY,ADDED_DATE ) VALUES ( " +
		 		"'"+instid+"', '"+orderrefno+"','"+customerid+"' ,'"+accounttype+"', '"+accountsubtype+"','"+accountcurrency+"','"+accountnumber+"','"+limitbasedon+"','"+usercode+"',sysdate) ";
		 enctrace("insertAccountInfo :" + insertcustdata );
		 
		 String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"+accountnumber+"'";
		 enctrace("acctexistqry-->"+acctexistqry);
		 String acctexist = (String)jdbctemplate.queryForObject(acctexistqry, String.class);
		 if("0".equalsIgnoreCase(acctexist)){
			 x=jdbctemplate.update(insertcustdata);
		 }
		 return x;
	}
	
	public int insertEzAccountifo(String instid,String accountnumber, String accounttype, String accountcurrency, String accflag, String status, String txngroup, String branchcode, String accountsubtype,String usercode, JdbcTemplate jdbctemplate) {
		int x = -1;
		
		 String insertcustdata = " INSERT INTO EZACCOUNTINFO (" +
		 		"INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE ) VALUES ( " +
		 		"'"+instid+"', '"+accountnumber+"','"+accounttype+"' ,'"+accountcurrency+"','0','0', '"+accflag+"','"+status+"','"+txngroup+"',sysdate,TO_CHAR(SYSDATE,'HH24MISS'),'"+branchcode+"','"+accountsubtype+"') ";
		 enctrace("insertEzAccountifo :" + insertcustdata );
		 String acctexistqry = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO WHERE ACCOUNTNO= '"+accountnumber+"'";		
		String acctexist = (String) jdbctemplate.queryForObject(acctexistqry,String.class);
		if("0".equalsIgnoreCase(acctexist)){
			x=jdbctemplate.update(insertcustdata);
		}
		 return x;   
	}
	
	public int insertEzauthrel(String instid, String cardno, String accountnumber, String accounttype, String secondflaf, String priority, String accountcurrency, JdbcTemplate jdbctemplate) {
		int x = -1;
		
		String secacctcountqry = "SELECT COUNT(1) AS CNT FROM EZAUTHREL WHERE ACCOUNTFLAG='S' and CHN = '"+cardno+"'";
		
		String count = (String) jdbctemplate.queryForObject(secacctcountqry, String.class);
		if("0".equalsIgnoreCase(count)){
			priority = " '"+priority+"' ";
		}else{
			priority = "(SELECT MAX(ACCOUNTPRIORITY)+1 FROM EZAUTHREL WHERE ACCOUNTFLAG='S' AND CHN = '"+cardno+"')";
		}
		 String insertcustdata = " INSERT INTO EZAUTHREL (" +
		 		"INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE ) VALUES ( " +
		 		"'"+instid+"', '"+cardno+"','"+accountnumber+"' ,'"+accounttype+"','"+secondflaf+"',"+priority+",'"+accountcurrency+"') ";
		 enctrace("insertEzauthrel :" + insertcustdata );
		 x=jdbctemplate.update(insertcustdata);
		 return x;   
	}
	
	public int insertifdacctdel(String instid,String accountnumber,String usercode,JdbcTemplate jdbctemplate){
		
		int ifdacctdel = -1;
		 String insertcustdata = " INSERT INTO ACCOUNTINFO_DELETE (" +
		 		"INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, ADDEDBY,ADDED_DATE) SELECT "
		 		+ "INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, '"+usercode+"',sysdate FROM ACCOUNTINFO WHERE ACCOUNTNO='"+accountnumber+"'";
		 enctrace("insertAccountInfo :" + insertcustdata );
		 
		 String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"+accountnumber+"'";
		 enctrace("acctexistqry-->"+acctexistqry);
		 int acctexist = jdbctemplate.queryForInt(acctexistqry);
		 if(acctexist >0){
			 int x=jdbctemplate.update(insertcustdata);
			 if (x > 0){
				 String delifdacct ="DELETE FROM ACCOUNTINFO WHERE ACCOUNTNO='"+accountnumber+"'";
				 ifdacctdel =jdbctemplate.update(delifdacct);
			 }	 
			 
		 }
		 return ifdacctdel;
	}
	

	public int insertezauthdel(String instid, String cardno, String accountnumber,String usercode, JdbcTemplate jdbctemplate) {
		int ezauthdel = -1;
		
		String secacctcountqry = "SELECT COUNT(1) AS CNT FROM EZAUTHREL WHERE ACCOUNTFLAG='S' and CHN = '"+cardno+"'";
		
		int count =jdbctemplate.queryForInt(secacctcountqry); 
		if(count > 0){
		
			 String insertcustdata = " INSERT INTO EZAUTHREL_DELETE (" +
			 		"INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE ) SELECT "
			 		+ " INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE FROM EZAUTHREL WHERE CHN='"+cardno+"' and ACCOUNTNO='"+accountnumber+"'";
			 enctrace("insertEzauthrel :" + insertcustdata );
			 int x=jdbctemplate.update(insertcustdata);
			 if (x >0){
				 String delezauth ="DELETE FROM EZAUTHREL WHERE CHN='"+cardno+"' and ACCOUNTNO ='"+accountnumber+"'";
				 ezauthdel = jdbctemplate.update(delezauth);
				 
			 }
		}
		 
		 return ezauthdel;   
		 
	}
	
	
	public int insertEzAcctdel(String instid, String accountnumber,String usercode, JdbcTemplate jdbctemplate){
		int ezacctdel = -1;
		
		 String insertcustdata = " INSERT INTO EZACCOUNTINFO_DELETE (" +
		 		"INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE ) SELECT"
		 		+ " INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE FROM EZACCOUNTINFO WHERE ACCOUNTNO='"+accountnumber+"'";
		 enctrace("insertEzAccountifo :" + insertcustdata );		 		
		 int x = jdbctemplate.update(insertcustdata);
		 if (x >0){
			 String delezacct ="DELETE FROM EZACCOUNTINFO WHERE ACCOUNTNO ='"+accountnumber+"'";
			 ezacctdel = jdbctemplate.update(delezacct);			 
		 }
		 return ezacctdel; 
		
	}
	
	

	public int insertcardacctlink(String instid,String cardno,String accountnumber,JdbcTemplate jdbctemplate){
		int insertdeauth =-1;
		try{
			System.out.println(cardno+"<====cardno");
			System.out.println("acctno=====>"+accountnumber);
			String insertdeauthlink = "INSERT INTO ACCT_LINK_DEAUTHORIZED select * from  ACCT_LINK where CARD_NO='"+cardno+"' and ACCT_NO='"+accountnumber+"'";
			enctrace("insert into ACCT_LINK_deauth " + insertdeauthlink);
			insertdeauth = jdbctemplate.update(insertdeauthlink);
		}	
		catch (Exception e){
			trace(" insertto ACCT_LINK_deauth ");						
		}
		
		return insertdeauth;
	}
	
	public int deauthdelete(String instid,String cardno,String accountnumber ,JdbcTemplate jdbctemplate){
		int deletedeauth =-1;
		try{
			String deauthdeletelink = "DELETE FROM  ACCT_LINK  WHERE  CARD_NO='"+cardno+"' and ACCT_NO='"+accountnumber+"'";
			enctrace("deauthdelete card acct link " + deauthdeletelink);
			deletedeauth = jdbctemplate.update(deauthdeletelink);
		}	
		catch (Exception e){
			trace("ifd card account link not deauthdeletelink ");						
		}
		
		return deletedeauth;
	}
	
	public List getAcctTypeList( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND AUTH_CODE='1'";   
		enctrace("acctypelistqry::"+acctypelistqry);  
		acctypelist = jdbctemplate.queryForList(acctypelistqry);
		return acctypelist;
	}
	public List getAcctTypeListNew( String instid,String encCardno, JdbcTemplate jdbctemplate ) throws Exception {
		List acctypelist = null;
		String acctypelistqry = "SELECT ACCTTYPEID, ACCTTYPEDESC FROM ACCTTYPE WHERE INST_ID='"+instid+"' AND AUTH_CODE='1' "
				+ "AND ACCTTYPEID NOT IN (SELECT ACCTTYPE_ID FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND ORG_CHN='"+encCardno+"')";   
		enctrace("acctypelistqrynew ::"+acctypelistqry);  
		acctypelist = jdbctemplate.queryForList(acctypelistqry);
		return acctypelist;
	}
	
	public List getAccounttypeinfo( String instid,String padssenable, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		List cstdetlist = null;
		StringBuilder acctqry = new StringBuilder();
		acctqry.append("SELECT A.INSTID, A.ACCOUNTNO,");
		acctqry.append("A.ACCOUNTTYPE,  ");
		acctqry.append("ACCOUNTTYPEDESC,  ");
		acctqry.append("PRODUCTCODE,  ");
		acctqry.append("PRODUCTCODEDESC, A.CURRCODE, CURRCODEDESC, LIMITFLAG, "); 
		acctqry.append("STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE,  DECODE(ACCOUNTFLAG,'P','Primary','S','Secondary') ACCOUNTFLAG FROM ( ");
		acctqry.append("SELECT INSTID, ACCOUNTNO, ACCOUNTTYPE,  ");
		acctqry.append("(SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE ACCTTYPEID=ACCOUNTTYPE AND INSTID=INST_ID) ACCOUNTTYPEDESC, ");
		acctqry.append("PRODUCTCODE, (SELECT ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE ACCTSUBTYPEID=trim(PRODUCTCODE) AND INSTID=INST_ID) PRODUCTCODEDESC, "); 
		acctqry.append("CURRCODE,  (SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE=CURRCODE ) CURRCODEDESC, AVAILBAL,  ");
		acctqry.append("LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE ");
		acctqry.append("FROM EZACCOUNTINFO  WHERE ACCOUNTNO IN (SELECT ACCOUNTNO FROM EZAUTHREL  ");
		acctqry.append("WHERE CHN='"+cardno+"' AND INSTID='"+instid+"')) A, EZAUTHREL B ");
		acctqry.append("WHERE A.ACCOUNTNO = B.ACCOUNTNO AND A.INSTID = B.INSTID AND B.CHN='"+cardno+"' AND ROWNUM<=2 ORDER BY ACCOUNTFLAG,ACCOUNTPRIORITY");  
		enctrace("getAccounttypeinfo :" + acctqry.toString() );
		cstdetlist = jdbctemplate.queryForList(acctqry.toString());  
		return cstdetlist ;
	}
   
	
	public int insertifdcardAcctlink(String instid, String orderrefno,String cardno, String customerid,String accountnumber,String accounttype, String accountsubtype,String accountcurrency,String limitBasedon,String usercode,String mkckstatus,String addedflag, String acct_priority_str,JdbcTemplate jdbctemplate) {
		int x = -1;
		 String insertcustdata = " INSERT INTO ACCT_LINK (" +
		 		"INST_ID, CARD_NO,ORDER_REF_NO, CIN,ACCTSUB_TYPE_ID,ACCT_CCY, ACCT_NO,ACCTTYPE_ID,MAKER_ID,MAKER_DATE,MKCK_STATUS,LIMITBASED_ON,FLAG,ACCT_PRIORITY) VALUES ( " +
		 		"'"+instid+"','"+cardno+"', '"+orderrefno+"','"+customerid+"' ,'"+accounttype+"', '"+accountsubtype+"','"+accountcurrency+"','"+accountnumber+"','"+usercode+"',sysdate,'"+mkckstatus+"','"+limitBasedon+"','"+addedflag+"','"+acct_priority_str+"')";
		 enctrace("insertAccountInfo :" + insertcustdata );
		 
		 String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"+accountnumber+"'";
		 enctrace("acctexistqry-->"+acctexistqry);
		 String acctexist = (String)jdbctemplate.queryForObject(acctexistqry, String.class);
		 if("0".equalsIgnoreCase(acctexist)){
			 x=jdbctemplate.update(insertcustdata);
		 }
		 return x;
	}
	
	public int delauthifdcardlink(String instid,String acctno,JdbcTemplate jdbctemplate){
		int delacctlink=-1;
		try{
			String delcardacctlink = "DELETE FROM ACCT_LINK WHERE INST_ID='"+instid+"' AND ACCT_NO='"+acctno+"'";
			delacctlink = jdbctemplate.update(delcardacctlink);
						
		}catch (Exception e){
			trace("could not delete ifd card acct link table");
		}
		return delacctlink;		
	}
	
	public int insertCardAcctLinkFromEzauthrel(String instid, String mkckstatus, String cardno,String acctno, String usercode, String customerid, JdbcTemplate jdbctemplate) {
		int res = -1;
		try {
			StringBuilder strbuild = new StringBuilder();
			strbuild.append("INSERT INTO ACCT_LINK(INST_ID,ORDER_REF_NO,CARD_NO,CIN,ACCT_NO,MAKER_DATE,ACCT_CCY,MAKER_ID,ACCTTYPE_ID,MKCK_STATUS,FLAG)");
			strbuild.append(" SELECT INSTID,(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"' ),CHN,'"+customerid+"',ACCOUNTNO,SYSDATE,CURRCODE,'"+usercode+"',ACCOUNTTYPE,'"+mkckstatus+"','D' FROM EZAUTHREL WHERE INSTID='"+instid+"' AND CHN='"+cardno+"' AND ACCOUNTNO='"+acctno+"'");
			enctrace("query for delete account ---> "+strbuild);
			res = jdbctemplate.update(strbuild.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String gettingCustomerID(String instid, String padssenable,String tablename, String cardno, JdbcTemplate jdbctemplate) {
		String custid = null;
		try {
			String cond = "";
			
			// by siva 10-07-19
			/*if(padssenable.equals("Y")){
				cond = "HCARD_NO='"+cardno+"'";
			}else{
				cond = "CARD_NO='"+cardno+"'";
			}*/
			String orderrefqry ="SELECT CIN FROM "+tablename+" WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"' ) AND ROWNUM<=1";
			trace("getting customer id quwerye __"+orderrefqry);
			custid=(String)jdbctemplate.queryForObject(orderrefqry,String.class);
			trace("custid__" + custid );
		} catch (EmptyResultDataAccessException e) { 
			custid= "NOREC";
		} 
		return  custid; 
	}

	
}  
