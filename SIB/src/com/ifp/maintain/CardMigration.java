package com.ifp.maintain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifd.beans.CardMigrateBean;
import com.ifd.dao.CardMigrationDAO;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class CardMigration extends BaseAction{
	
	public String comBranchId(){
		HttpSession session = getRequest().getSession();
		String br_id = (String)session.getAttribute("BRANCHCODE"); 
		return br_id;
	}
	
	public String comuserType(){
		HttpSession session = getRequest().getSession();
		String usertype = (String)session.getAttribute("USERTYPE"); 
		return usertype;
	}
	private static final long serialVersionUID = -8376161637970676446L;
	  
	CommonDesc commondesc = new CommonDesc();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	CardMigrationDAO cardmigratedao = new CardMigrationDAO();
	CardMigrateBean cardmigratebean = new CardMigrateBean();
	
	public CardMigrationDAO getCardmigratedao() {
		return cardmigratedao;
	}

	public void setCardmigratedao(CardMigrationDAO cardmigratedao) {
		this.cardmigratedao = cardmigratedao;
	}
	
	public CardMigrateBean getCardmigratebean() {
		return cardmigratebean;
	}

	public void setCardmigratebean(CardMigrateBean cardmigratebean) {
		this.cardmigratebean = cardmigratebean;
	}
	
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	
	 

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
    
	String act;  
	

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	List <String> prodlist;
	
	public List <String> getProdlist() {
		return prodlist;
	}

	public void setProdlist(List<String> prodlist) {
		this.prodlist = prodlist;
	}
	
	List <String> branchlist;

	public List <String> getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List<String>	 branchlist) {
		this.branchlist = branchlist ;
	}
	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	public String comUsername(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
	}
	
	final String CARDACTIVATEDCODE=CommonDesc.ACTIVECARDSTATUS;
	
	
	public String comProductId(String instid, String prodbin, JdbcTemplate jdbctemplate){
		String query="select CARD_TYPE_ID from PRODUCT_MASTER where INST_ID='"+instid+"' and BIN='"+prodbin+"' and rownum<=1"; 
		enctrace("getting card type id " + query);
		String temp=null;
		try{
		 temp=(String)jdbctemplate.queryForObject(query,String.class);
		}catch(EmptyResultDataAccessException e){System.out.println("exceptionhap"+e.getMessage());}
		//trace("product id "+temp);
		return temp;
	}
	
	
	public String getProductcode(String instid, String bin, JdbcTemplate jdbctemplate ) throws Exception {
		String product = null;
		String productqry = "SELECT PRODUCT_CODE FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' ";
		enctrace("product code getting :" + productqry );
		try{
			product = (String)jdbctemplate.queryForObject(productqry, String.class);
		}catch(EmptyResultDataAccessException e ){}
		return product;
	}
	
	
	public String getProductBySubProduct(String instid, String productcode, JdbcTemplate jdbctemplate ) throws Exception {
		String product = null;
		String subproduct = "SELECT SUB_PROD_ID FROM INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"' ";
		enctrace("getting sub product :" + subproduct );
		try{
			product = (String)jdbctemplate.queryForObject(subproduct, String.class);
		}catch(EmptyResultDataAccessException e ){}
		return product;
	}
	
	
	public String getSubProductLimitId(String instid, String sub_prod_id, JdbcTemplate jdbctemplate) {
		String limitid_qry ="SELECT LIMIT_ID FROM INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND SUB_PROD_ID='"+sub_prod_id+"' AND ROWNUM <=1" ;
		trace("subprodcodeqry__"+limitid_qry);
		String limitid = null;
		try {
			limitid=(String)jdbctemplate.queryForObject(limitid_qry,String.class);
			trace("limitid__" + limitid );
			 
		} catch (EmptyResultDataAccessException e) { 
			limitid="";
		} 
		return  limitid;  
	}
	public String getorderrefno(String instid, String chn, JdbcTemplate jdbctemplate) {
		String order_qry ="SELECT ORDER_REF_NO FROM CARD_PRODUCTION_MG WHERE INST_ID='"+instid+"' AND HCARD_NO='"+chn+"' AND ROWNUM <=1" ;
		trace("getorderrefno"+order_qry);
		String orderno = null;
		try {
			orderno=(String)jdbctemplate.queryForObject(order_qry,String.class);
			trace("getorderrefno" + orderno );
			 
		} catch (EmptyResultDataAccessException e) { 
			orderno="";
		} 
		return  orderno;  
	}
	
	/*public int getres_5Query(String INSTID_5 ,String LIMITTYPE_5 ,String LIMITID_5 ,String TXNCODE_5 ,String CURRCODE_5 ,String AMOUNT_5 ,String COUNT_5 ,String WAMOUNT_5 ,String WCOUNT_5 ,String MAMOUNT_5 ,String MCOUNT_5 ,String YAMOUNT_5 ,String YCOUNT_5 ,String LIMITDATE_5)
	{
		int ezaccumcnt =-1;
		StringBuilder AccinfiInsert = new StringBuilder();
		AccinfiInsert.append("INSERT INTO EZACCUMINFO ");
		AccinfiInsert.append("(INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE) ");
		AccinfiInsert.append("VALUES ");
		AccinfiInsert.append("('"+INSTID_5+"','"+LIMITTYPE_5+"','"+LIMITID_5+"','"+TXNCODE_5+"','"+CURRCODE_5+"','0',");
		AccinfiInsert.append("'0','0','0','0','0','0','0','"+LIMITDATE_5+"' )");
		int ezaccumcnt = jdbctemplate.update(AccinfiInsert);
		return ezaccumcnt;
	}*/
	
	
	/*public synchronized int personalCardIssuence(String cardno,String padssenable,String instid,String maker_id,String tablename,JdbcTemplate jdbctemplate) throws Exception	{
		trace("**************personalCardIssuence**************");enctrace("**************personalCardIssuence**************");
		int issue_status = -1, custinfo_move = -1,delete_status = -1,custinfo_status = -1;
		
		String cardstatus = CARDACTIVATEDCODE;
		String switchstatus = commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
		String userid =comUserCode();
		
		trace("Activation the card number : "  + cardno );
		String reissuedate="''",reissue_count = "0",repindate="''",repincount="0",damgedate="''",blockdate="''",hotdate="''",closedte="''",pinretry_count="0";
		String active_date="''";
		String status_code= commondesc.getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate);
		  
		
		String deletefromProcess ="";
		if(padssenable.equals("Y")){   
		deletefromProcess = "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"' ";
		}else
		{
		deletefromProcess = "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' ";	
		}
		
		String cardissuetype="";
		
		if(padssenable.equals("Y")){
		cardissuetype = commondesc.getCardIssueTypeByCard(instid,padssenable, cardno, "PERSONAL", jdbctemplate);
		}else{
		cardissuetype = commondesc.getCardIssueTypeByCard(instid,padssenable, cardno, "PERSONAL", jdbctemplate);	
		}
		
		trace("Getting cardissuetype...got :  " + cardissuetype);
		
		String custcin ="";
		if(padssenable.equals("Y")){
		custcin = commondesc.persfchCustomerId(instid,padssenable, cardno, "CARD_PRODUCTION", jdbctemplate);
		}else{
		custcin = commondesc.persfchCustomerId(instid,padssenable, cardno, "CARD_PRODUCTION", jdbctemplate);	
		}
		
		trace("Got the customer id : " + custcin);
		////System.out.println("CUSTOMERN NUMBER IS =====> "+custcin);  
		
		String acctno = null;
		int acctinfo_insert =-1;
		int acctpriority = -1;
		if( cardissuetype !=null && cardissuetype.equals("$SUPLIMENT")){
			acctpriority = 2;
			//String parentcardno = CommonDesc.getParentCardNumberByCard(instid, cardno, "PERSONAL", jdbctemplate);
			//acctno = commondesc.getCardPrimaryAccount(instid, parentcardno, "currency", jdbctemplate);
			//trace("Got the acct number from parent card  : " + acctno );
			//commondesc.updateAddonCountToParent(instid,parentcardno,jdbctemplate);
			acctinfo_insert =1;
		}else{
			acctpriority = 1;
	//		acctno = curcode+commondesc.paddingZero(Integer.toString(curacctseq), Integer.parseInt(acctnolen)-curcode.length());
	//		trace("Generated account number : " + acctno );
			String INSTID_1="" ,CHN_1="" ,ACCOUNTNO_1="" ,ACCOUNTTYPE_1="" ,ACCOUNTFLAG_1="" ,ACCOUNTPRIORITY_1="" ,CURRCODE_1="" ;
			String INSTID_2="" ,ACCOUNTNO_2="" ,ACCOUNTTYPE_2="" ,CURRCODE_2="" ,AVAILBAL_2="" ,LEDGERBAL_2="" ,LIMITFLAG_2="" ,STATUS_2="" ,TXNGROUPID_2="" ,LASTTXNDATE_2="" ,LASTTXNTIME_2="" ,BRANCHCODE_2="" ,PRODUCTCODE_2=""; 
			String INSTID_3="" ,CHN_3="" ,CARDTYPE_3="" ,CUSTID_3="" ,TXNGROUPID_3="" ,LIMITFLAG_3="" ,EXPIRYDATE_3="" ,STATUS="" ,PINOFFSET_3="" ,OLDPINOFFSET_3="" ,TPINOFFSET_3="" ,OLDTPINOFFSET_3="" ,PINRETRYCOUNT_3="" ,TPINRETRYCOUNT_3="" ,PVKI_3="" ,LASTTXNDATE_3="" ,LASTTXNTIME_3="" ,PANSEQNO_3=""; 
			String INSTID_4="" ,CUSTID_4="" ,NAME_4="" ,DOB_4="" ,SPOUSENAME_4="" ,ADDRESS1_4="" ,ADDRESS2_4="" ,ADDRESS3_4="" ,OFFPHONE_4="" ,MOBILE_4="" ,EMAIL_4="" ,RESPHONE_4=""; 
			String LIMITRECID_5="",cardcon1="";
			int res_1 =0;int res_2 =0;int res_3=0;int res_4=0;int res_5 = 0;
			StringBuilder mv = new StringBuilder();
			
			mv.append("SELECT ");
			//--EZAUTHREL start
			//--INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,
			if(padssenable.equals("Y")){
			mv.append("'"+instid+"' INSTID_1, PCP.HCARD_NO CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.CARDISSUETYPE ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
			}else{
			mv.append("'"+instid+"' INSTID_1, PCP.CARD_NO CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.CARDISSUETYPE ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");	
			}
			if(padssenable.equals("Y")){cardcon1="HCARD_NO";}else{cardcon1="CARD_NO";};
			//--EZAUTHREL end  
			//--EZACCOUNTINFO_MG start
			//--INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE
			mv.append("'"+instid+"' INSTID_2, AI.ACCOUNTNO ACCOUNTNO_2, AI.ACCTTYPE_ID ACCOUNTTYPE_2, AI.ACCT_CURRENCY CURRCODE_2, '0' AVAILBAL_2, '0' LEDGERBAL_2,  ");
			mv.append("(SELECT ACCT_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_2, '"+status_code+"' STATUS_2,  ");
			mv.append("'01' TXNGROUPID_2, TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_2,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_2,  ");
			mv.append("PCP.BRANCH_CODE BRANCHCODE_2, AI.ACCTSUB_TYPE_ID PRODUCTCODE_2, ");
			//-- EZACCOUNTINFO_MG end
			//-- EZCARDINFO_MG start
			//--INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME, PANSEQNO
			if(padssenable.equals("Y")){
			mv.append("'"+instid+"' INSTID_3, PCP.HCARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, "); 
			}else{
			mv.append("'"+instid+"' INSTID_3, PCP.CARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, ");	
			}
			mv.append("(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
			mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'"+status_code+"' STATUS , NVL(PCP.PIN_OFFSET,0)  PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, "); 
			mv.append("'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,'0','0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
			mv.append("TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3, ");  
			//-- EZCARDINFO_MG end  
			//--EZCUSTOMERINFO start
			//--INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE
			mv.append("'"+instid+"' INSTID_4,'"+custcin+"' CUSTID_4, CI.FNAME NAME_4, CI.DOB  DOB_4,CI.SPOUCE_NAME SPOUSENAME_4, ");
			mv.append("CI.C_HOUSE_NO ADDRESS1_4, CI.C_STREET_NAME ADDRESS2_4,CI.C_CITY ADDRESS3_4, CI.C_PHONE1 OFFPHONE_4,CI.MOBILE MOBILE_4, ");
			mv.append("CI.E_MAIL  EMAIL_4 ,CI.C_PHONE2 RESPHONE_4, PCP.LIMIT_ID LIMIT_RECORDID_5 FROM ");
			//--EZCUSTOMERINFO end  
			mv.append(" CUSTOMERINFO CI ,ACCOUNTINFO_MG AI ,CARD_PRODUCTION PCP "); 
			mv.append("WHERE  ");
			//modifed by senthil
			//uncommented
			mv.append(" (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND");
			//commened
			//mv.append("(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN) AND ");
			
			mv.append("(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID and pcp.account_no=ai.ACCOUNTNO) ");
			mv.append("AND CI.INST_ID='"+instid+"' AND AI.INST_ID='"+instid+"' AND PCP.INST_ID='"+instid+"'  ");
			//commented
			//mv.append("AND CI.CIN='"+custcin+"' AND AI.CIN='"+custcin+"' AND PCP.CIN='"+custcin+"' AND "+cardcon1+"='"+cardno+"' "); 
			mv.append("AND "+cardcon1+"='"+cardno+"' ");
			//end
			enctrace("Move to Production Query-----------------------------------\n");
			enctrace(mv.toString());
			
			
			List movetoSwitchP = jdbctemplate.queryForList(mv.toString());
			Iterator custitr = movetoSwitchP.iterator();
			while(custitr.hasNext())
			{
				Map mp = (Map)custitr.next();  
				//fname = (String)mp.get("FNAME");
						INSTID_1   = (String) mp.get(" INSTID_1 ");
						CHN_1  = (String) mp.get("CHN_1");
						ACCOUNTNO_1  = (String) mp.get("ACCOUNTNO_1");
						ACCOUNTTYPE_1  =(String)  mp.get("ACCOUNTTYPE_1");
						ACCOUNTFLAG_1  =(String)  mp.get("ACCOUNTFLAG_1");
						ACCOUNTPRIORITY_1  =(String)  mp.get("ACCOUNTPRIORITY_1");
						CURRCODE_1  = (String) mp.get("CURRCODE_1");
						INSTID_2  = (String) mp.get("INSTID_2");
						ACCOUNTNO_2  = (String) mp.get("ACCOUNTNO_2");
						ACCOUNTTYPE_2  = (String) mp.get("ACCOUNTTYPE_2");
						CURRCODE_2  = (String) mp.get("CURRCODE_2");
						AVAILBAL_2  = (String) mp.get("AVAILBAL_2");
						LEDGERBAL_2  = (String) mp.get("LEDGERBAL_2"); 
						LIMITFLAG_2  = (String) mp.get("LIMITFLAG_2");
						STATUS_2  = (String) mp.get("STATUS_2");
						TXNGROUPID_2  = (String) mp.get("TXNGROUPID_2");
						LASTTXNDATE_2  = (String) mp.get("LASTTXNDATE_2");
						LASTTXNTIME_2  = (String) mp.get("LASTTXNTIME_2");
						BRANCHCODE_2  = (String) mp.get("BRANCHCODE_2");
						PRODUCTCODE_2  = (String) mp.get("PRODUCTCODE_2");
						
						////System.out.println("1"+mp.get("INSTID_3").toString());
						//////System.out.println("2"+mp.get("INSTID_3").toString());
						////System.out.println("4"+mp.get("CHN_3").toString());
						////System.out.println("5"+mp.get("CARDTYPE_3").toString());
						////System.out.println("6"+mp.get("CUSTID_3").toString());
						////System.out.println("7"+mp.get("TXNGROUPID_3").toString());
						////System.out.println("8"+mp.get("LIMITFLAG_3").toString());
						////System.out.println("8"+mp.get("EXPIRYDATE_3").toString());
						////System.out.println("9"+mp.get("STATUS").toString());
						////System.out.println("0"+mp.get("PINOFFSET_3") .toString());
						////System.out.println("1"+mp.get("OLDPINOFFSET_3").toString());
						////System.out.println("3"+mp.get("TPINOFFSET_3").toString());
						////System.out.println("5"+mp.get("OLDTPINOFFSET_3").toString()); 
						////System.out.println("6"+mp.get("PINRETRYCOUNT_3").toString());
						////System.out.println("7"+mp.get("TPINRETRYCOUNT_3").toString());
						
						
						CHN_3  = mp.get("CHN_3").toString();
						CARDTYPE_3  = mp.get("CARDTYPE_3").toString();
						CUSTID_3  = mp.get("CUSTID_3").toString();
						TXNGROUPID_3  = mp.get("TXNGROUPID_3").toString();
						LIMITFLAG_3  = mp.get("LIMITFLAG_3").toString();
						EXPIRYDATE_3  = mp.get("EXPIRYDATE_3").toString();
						STATUS  = mp.get("STATUS").toString();
						PINOFFSET_3  = mp.get("PINOFFSET_3") .toString();
						OLDPINOFFSET_3  = mp.get("OLDPINOFFSET_3").toString();
						TPINOFFSET_3  = mp.get("TPINOFFSET_3").toString();
						OLDTPINOFFSET_3  = mp.get("OLDTPINOFFSET_3").toString(); 
						PINRETRYCOUNT_3  = mp.get("PINRETRYCOUNT_3").toString();
						TPINRETRYCOUNT_3  = mp.get("TPINRETRYCOUNT_3").toString();
						PVKI_3  = mp.get("PVKI_3").toString();
						LASTTXNDATE_3  = mp.get("LASTTXNDATE_3").toString();
						LASTTXNTIME_3  = mp.get("LASTTXNTIME_3").toString();
						PANSEQNO_3  = mp.get("PANSEQNO_3") .toString();
						
						
						INSTID_4  = mp.get("INSTID_4").toString();
						CUSTID_4  = mp.get("CUSTID_4").toString();
						NAME_4  = (String)mp.get("NAME_4");
						DOB_4  = (String)mp.get("DOB_4");
						SPOUSENAME_4  = (String)mp.get("SPOUSENAME_4");
						ADDRESS1_4  = (String)mp.get("ADDRESS1_4");
						ADDRESS2_4  = (String)mp.get("ADDRESS2_4");
						ADDRESS3_4  = (String)mp.get("ADDRESS3_4");
						OFFPHONE_4  = (String)mp.get("OFFPHONE_4");
						MOBILE_4  = (String)mp.get("MOBILE_4");
						EMAIL_4  = (String)mp.get("EMAIL_4");
						RESPHONE_4 = (String)mp.get("RESPHONE_4");
						LIMITRECID_5 = (String)mp.get("LIMIT_RECORDID_5");
						
			}
		String cardcon = "";	
		if(padssenable.equals("Y")){cardcon="HCARD_NO";}else{cardcon="CARD_NO";};
			
			String deletefromProcess ="";
			deletefromProcess = "INSERT  INTO  issue_log(chn) VALUES ('"+cardno+"')";
			enctrace("inserting into log table " +deletefromProcess);
			
					
		
		//1	
			
			String custexistqry = "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO WHERE CUSTID= '"+custcin+"'";		
			String custcount = (String) jdbctemplate.queryForObject(custexistqry,String.class);		
			StringBuilder cinf_4 = new StringBuilder();		
					cinf_4.append("INSERT INTO EZCUSTOMERINFO ");
					cinf_4.append("(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) ");
					cinf_4.append("VALUES ");
					cinf_4.append("('"+instid+"','"+CUSTID_4+"','"+NAME_4+"','"+DOB_4+"','"+SPOUSENAME_4+"', ");
					cinf_4.append("'"+ADDRESS1_4+"','"+ADDRESS2_4+"','"+ADDRESS3_4+"','"+OFFPHONE_4+"','"+MOBILE_4+"','"+EMAIL_4+"','"+RESPHONE_4+"') ");
					
					enctrace("cinf_4:::::"+cinf_4.toString());
					
					 try{
				    		if("0".equalsIgnoreCase(custcount)){
				    			res_4 = jdbctemplate.update(cinf_4.toString());
				    		}else{res_4 = 1;}
				    		}catch(Exception e)
				    		{
				    			trace("Exception in moving production :: 1:::::::"+e);
				    			return -1;
				    		}
		//2			
		StringBuilder crdinf_3 = new StringBuilder();		
						crdinf_3.append("INSERT INTO EZCARDINFO_MG ");
						crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
						crdinf_3.append("VALUES ");
						crdinf_3.append("('"+instid+"','"+CHN_3+"','"+CARDTYPE_3+"','"+CUSTID_3+"','"+TXNGROUPID_3+"','"+LIMITFLAG_3+"',TO_DATE('"+EXPIRYDATE_3+"','MM/DD/YYYY'),'"+STATUS+"','"+PINOFFSET_3+"','"+OLDPINOFFSET_3+"',");
						crdinf_3.append("'"+TPINOFFSET_3+"','"+OLDTPINOFFSET_3+"','"+PINRETRYCOUNT_3+"','"+TPINRETRYCOUNT_3+"','"+PVKI_3+"',TO_DATE('"+LASTTXNDATE_3+"','MM/DD/YYYY'),'"+LASTTXNTIME_3+"'  ,'0' ,");
						crdinf_3.append("'"+PANSEQNO_3+"' )");
						
						enctrace("crdinf_3:::::"+crdinf_3.toString());
						
						 try{
					    		
					    		res_3 = jdbctemplate.update(crdinf_3.toString());
					    		}catch(Exception e)
					    		{
					    			trace("Exception in moving production :: 1:::::::"+e);
					    			return -1;
					    		}
		//3	
						 String acctexistqry = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO_MG WHERE ACCOUNTNO= '"+ACCOUNTNO_2+"'";		
							String acctexist = (String) jdbctemplate.queryForObject(acctexistqry,String.class);	
		StringBuilder ezac_2 = new StringBuilder();		
							ezac_2.append("INSERT INTO EZACCOUNTINFO_MG ");
							ezac_2.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
							ezac_2.append("VALUES ");
							ezac_2.append("('"+instid+"','"+ACCOUNTNO_2+"','"+ACCOUNTTYPE_2+"','"+CURRCODE_2+"','"+AVAILBAL_2+"','"+LEDGERBAL_2+"','"+LIMITFLAG_2+"','"+STATUS_2+"',");
							ezac_2.append("'"+TXNGROUPID_2+"',TO_DATE('"+LASTTXNDATE_2+"','MM/DD/YYYY'),'"+LASTTXNTIME_2+"','"+BRANCHCODE_2+"','"+PRODUCTCODE_2+"' )");	
							enctrace("ezac_2::::"+ezac_2.toString());	
							
							
							 try{
								 if("0".equalsIgnoreCase(acctexist)){
									 res_2 = jdbctemplate.update(ezac_2.toString());
						    		}else{res_2 = 1;}
						    		
						    		}catch(Exception e)
						    		{
						    			trace("Exception in moving production :: 1:::::::"+e);
						    			return -1;
						    		}
							      
											      			 
		//4	
		StringBuilder authrel_1 = new StringBuilder();	
		authrel_1.append("INSERT INTO EZAUTHREL ");
		authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
		authrel_1.append("VALUES ");
		authrel_1.append("('"+instid+"','"+CHN_1+"','"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+CURRCODE_1+"') ");
        enctrace("authrel_1::::"+authrel_1.toString());
		
        try{
    		
    		res_1 = jdbctemplate.update(authrel_1.toString());
    		}catch(Exception e)
    		{
    			trace("Exception in moving production :: 1:::::::"+e);
    			return -1;
    		}
		
      //5	
              
              
		
		
		String INSTID_5="", LIMITTYPE_5="", LIMITID_5="", TXNCODE_5="", CURRCODE_5="", AMOUNT_5="", COUNT_5="", WAMOUNT_5="", WCOUNT_5="", MAMOUNT_5="", MCOUNT_5="", YAMOUNT_5="", YCOUNT_5="", LIMITDATE_5="";
		
		StringBuilder accinfo_5 = new StringBuilder();
		accinfo_5.append("SELECT ");
		//--EZACCUMINFO start
		//--INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE
		accinfo_5.append("INSTID INSTID_5, CASE  LIMITTYPE ");
		accinfo_5.append("WHEN 'CDTP'  THEN 'CARD' ");  
		accinfo_5.append("WHEN 'CARD' THEN 'CARD' "); 
		accinfo_5.append("WHEN 'ACTP'  THEN 'ACCT' ");  
		accinfo_5.append("WHEN 'ACCT' THEN 'ACCT' END LIMITTYPE_5 ,"); 
		accinfo_5.append("CASE  LIMITTYPE "); 
		if(padssenable.equals("Y")){
		accinfo_5.append(" WHEN 'CDTP'  THEN '"+cardno+"'");      
		accinfo_5.append(" WHEN 'CARD' THEN '"+cardno+"' "); 
		}
		//wait i wil come
		{
		}
		accinfo_5.append(" WHEN 'ACTP'  THEN '"+ACCOUNTNO_2+"' ");  
		accinfo_5.append(" WHEN 'ACCT' THEN '"+ACCOUNTNO_2+"' END LIMITID_5 ,");  
		accinfo_5.append(" TXNCODE TXNCODE_5, CURRCODE CURRCODE_5, AMOUNT AMOUNT_5, COUNT COUNT_5, "); 
		accinfo_5.append("WAMOUNT WAMOUNT_5, WCOUNT WCOUNT_5, MAMOUNT MAMOUNT_5, MCOUNT MCOUNT_5, YAMOUNT YAMOUNT_5, YCOUNT YCOUNT_5, ");
		accinfo_5.append("(select TO_CHAR(AUTH_DATE,'DD-MON-YYYY') from LIMIT_DESC where INSTID='"+instid+"' AND LIMIT_ID='"+LIMITRECID_5+"') LIMITDATE_5 FROM EZLIMITINFO ");
		accinfo_5.append("WHERE INSTID='"+instid+"' AND LIMIT_RECID = '"+LIMITRECID_5+"'");
		//--EZACCUMINFO end  
		enctrace("accinfo_5:::::"+accinfo_5.toString());
		
		
		
		
		List movetoAccuminfo= jdbctemplate.queryForList(accinfo_5.toString());
		trace("movetoAccuminfo:::::::::::"+movetoAccuminfo.size()+":::::::"+movetoAccuminfo);   
		Iterator accitr = movetoAccuminfo.iterator();
		int as =0; int incCount = 0;
		while(accitr.hasNext())
		{
			////System.out.println("testing :::::::::::::::::::::::1"+as++);
			incCount = incCount+1;    
			
			Map mp2 = (Map)accitr.next();  
			INSTID_5   = mp2.get("INSTID_5").toString();
			////System.out.println("testing :::::::::::::::::::::::2"+as++);
			LIMITTYPE_5   = mp2.get("LIMITTYPE_5").toString();
			////System.out.println("testing :::::::::::::::::::::::3"+as++);
			LIMITID_5   = mp2.get("LIMITID_5").toString();
			////System.out.println("testing :::::::::::::::::::::::4"+mp2.get("LIMITID_5").toString());
			TXNCODE_5= mp2.get("TXNCODE_5").toString();;
			////System.out.println("testing :::::::::::TXNCODE_5::::::::::::4"+mp2.get("TXNCODE_5").toString());
		
			CURRCODE_5   = mp2.get("CURRCODE_5").toString();;
		////System.out.println("testing :::::::::::currr::::::::::"+mp2.get("CURRCODE_5").toString());
		AMOUNT_5   = mp2.get("AMOUNT_5").toString();;
		////System.out.println("testing :::::::::::::::::::::::"+as++);
		COUNT_5   = mp2.get("COUNT_5").toString();
		////System.out.println("testing :::::::::::::::::::::::"+as++);
		WAMOUNT_5   = mp2.get("WAMOUNT_5").toString();
		////System.out.println("testing :::::::::::::::::::::::"+as++);
		WCOUNT_5   = mp2.get("WCOUNT_5").toString();
		////System.out.println("testing :::::::::::::::::::::::"+as++);
		MAMOUNT_5   = mp2.get("MAMOUNT_5").toString();
		////System.out.println("testing :::::::::::::::::::::::"+as++);
		MCOUNT_5   = mp2.get("MCOUNT_5").toString();
		////System.out.println("testing :::::::::::::::::::::::"+as++);
		YAMOUNT_5   = mp2.get("YAMOUNT_5").toString();
		YCOUNT_5   = mp2.get("YCOUNT_5").toString();
		////System.out.println("testing :::::::::::::::::::::::"+as++);
		LIMITDATE_5   = mp2.get("LIMITDATE_5").toString();
		////System.out.println("testing :::::::::::::::::::::::"+as++);
    
		
				
		
		String getres_5Query = getres_5Query(instid, LIMITTYPE_5, LIMITID_5, TXNCODE_5, CURRCODE_5, AMOUNT_5, COUNT_5, WAMOUNT_5, WCOUNT_5, MAMOUNT_5, MCOUNT_5, YAMOUNT_5, YCOUNT_5, LIMITDATE_5);

		enctrace("getres_5Query::"+getres_5Query);  
		 try{
	    		
	    		res_5 = jdbctemplate.update(getres_5Query.toString());
	    		}catch(Exception e)
	    		{
	    			trace("Exception in moving production :: 1:::::::"+e);
	    			return -1;
	    		}
		
		
		
		
		
		//AccinfiInsert = "";
		} 
		
		
		
		//StringBuilder update_production = new StringBuilder();
		//update_production.append("UPDATE "+tablename+" SET CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchstatus+"',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"+userid+"', " );
		//update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='"+instid+"' and "+cardcon+"='"+cardno+"'");
		
		//enctrace("update query for instcard processs "+update_production);
		
		int production_insert = -1;int deletefromprocess = -1,upd_process=-1;;
		
		try{
		
		//upd_process = jdbctemplate.update(update_production.toString());		
		//production_insert = jdbctemplate.update(movetoproduction);
		deletefromprocess = jdbctemplate.update(deletefromProcess);
		}catch(Exception e)
		{
			trace("Exception in moving production :: 2:::"+e);
			return -1;
		}
		
		
		
		enctrace("\n--------------------------------------Move to Production Query");
		trace("####### custinfo_status : "+custinfo_status);
		trace("result :::::::::::"+res_1+res_2+res_3+res_4+incCount+deletefromprocess);   
		//if(res_1==1 && res_2 ==1 && res_3 ==1 && res_4 ==1 && incCount>0 && production_insert >0 && deletefromprocess >0 && upd_process >0)
		if(res_1==1 && res_2 ==1 && res_3 ==1 && res_4 ==1 && incCount>0 && deletefromprocess >0)
		{
			trace("PROCESS COMPLETED");
			issue_status = 1;
		}
		else
		{
			issue_status = -1;
		}
		    
		
		
		}
		  
		
		return issue_status;
	}*/

	
	public String getres_5Query(String INSTID_5 ,String LIMITTYPE_5 ,String LIMITID_5 ,String TXNCODE_5 ,String CURRCODE_5 ,String AMOUNT_5 ,String COUNT_5 ,String WAMOUNT_5 ,String WCOUNT_5 ,String MAMOUNT_5 ,String MCOUNT_5 ,String YAMOUNT_5 ,String YCOUNT_5 ,String LIMITDATE_5)
	{
		StringBuilder AccinfiInsert = new StringBuilder();
		AccinfiInsert.append("INSERT INTO EZACCUMINFO_MG ");
		AccinfiInsert.append("(INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE) ");
		AccinfiInsert.append("VALUES ");
		AccinfiInsert.append("('"+INSTID_5+"','"+LIMITTYPE_5+"','"+LIMITID_5+"','"+TXNCODE_5+"','"+CURRCODE_5+"','0',");
		AccinfiInsert.append("'0','0','0','0','0','0','0','"+LIMITDATE_5+"' )");
		
		return AccinfiInsert.toString();
	}
	

	public String cardMigrateHome() {
		trace("**************Card Migration Home**************");
		enctrace("**************Card Migration Home**************");
		String instid =  comInstId(); 
		HttpSession session = getRequest().getSession();
		try {
			List binlist =  cardmigratedao.binlist(instid, jdbctemplate);
			cardmigratebean.setBinList(binlist);
			List branchList = cardmigratedao.generateBranchList(instid, jdbctemplate);
			cardmigratebean.setBranchList(branchList);
			List productList =  cardmigratedao.getProductListView(instid, jdbctemplate);
			cardmigratebean.setProductList(productList);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception is " +e.getMessage());
		}		
		
		return "search_home";
	}
	
	public String searchList()
	{
		trace("**************Card Migration Search List**************");
		enctrace("**************Card Migration Search List**************");
		String instid =  comInstId(); 
		HttpSession session = getRequest().getSession();
		String bincode =  getRequest().getParameter("binlist");    
		trace("bincode:::"+bincode);    
		String branchcode = getRequest().getParameter("branchlist");
		trace("branchcode:::"+branchcode);
		String query = "";
		try{
			 if(!bincode.equals("ALL")){
				 query = query + "BIN = '"+bincode+"' ";
				 trace("branch qry ::: "+query);
			 }else{	
				 query = query + "PRD_CODE IN (SELECT PRD_CODE FROM PRODUCTINFO WHERE MIGDATA.BIN=PRODUCTINFO.PRD_CODE AND MIGDATA.hcard_no is null AND PRODUCTINFO.AUTH_STATUS='1' ) ";
				 trace("bin qry ::: "+query);
			 }
			
			 if(!branchcode.equals("ALL")){
				 query = query + "AND BRANCH_CODE='"+branchcode.trim()+"' " ;    
				 trace("Product Code ::::"+query);    
			}else{
				//query +="AND trim(BRANCH_CODE) IN (SELECT BRANCH_CODE FROM BRANCH_MASTER WHERE AUTH_CODE='1') ";
				trace("Product Code ::::"+query);
			}
			 
			 List recordlist=null;
			 recordlist = cardmigratedao.getMigrateRecordList(query,jdbctemplate);
			 trace("List value " +recordlist);
			 if(recordlist.isEmpty()){
					trace("No Records found");
					addActionError("No Records Found");
			 }else
			 {
				 cardmigratebean.setRecordsList(recordlist);
			 }
				
	  }catch(Exception e)
	  {
		  e.printStackTrace();
			trace("Exception ::::: "+e.getMessage());
	  }
	
	return "cardmigratedata";
   }
	
	public String selectedCardList() throws Exception, SQLException
	{
		trace("*************CARD MIGRATION SELECTED CARD LIST*****************");
		enctrace("*************CARD MIGRATION SELECTED CARD LIST*****************");
		HttpSession session = getRequest().getSession();
		String instid =  comInstId();
		String userid = comUserCode();
		IfpTransObj transact = commondesc.myTranObject("RECVORDER", txManager);
		String card_no[] = getRequest().getParameterValues("personalrefnum");
		//String accountno[] = getRequest().getParameterValues("accountno");
		
		
		
		//last order ref no=33822
		//int orderrefno = 140224;
		int orderrefnolength = 12;
		//String cardtypeid="001";
		//String acctsubtype="99";
		
		String accounttype="CDTP";
		
		String subproductCode="";
		
		String ClearDMK = null;
		String EDMK = null;
		String DMK_KVC = null;
		String ClearDPK = null;
		String EDPK = null;
		String DPK_KVC = null;
		String ECHN = null;
		//Step 1 -- Get Encrypted DMK
		String DMKComponent1 = "11112222333344445555666677778888";
		String DMKComponent2 = "88887777666655554444333322221111";
		String DMKComponent3 = "AAAABBBBCCCCDDDDEEEEFFFF99990000";
		PadssSecurity sec = new PadssSecurity();
		ClearDMK = sec.getFormattedKey(DMKComponent1, DMKComponent2, DMKComponent3);
		System.out.println("DMK KCV--->" + DMK_KVC ); //For Display & Store in DB
		//EDMK = sec.getEDMK(ClearDMK);
		System.out.println("Encrpted DMK--->" + EDMK );//Store in DB
		//Step 2 -- Get Encrypted DPK
		String DPKComponent1 = "0123456789ABCDEFFEDCBA9876543210";
		String DPKComponent2 = "FEDCBA98765432100123456789ABCDEF";
			
		ClearDPK = sec.getFormattedKey(DPKComponent1, DPKComponent2);
		//System.out.println("ClearDPK--->" + ClearDPK );
		
		String tablename ="CARD_PRODUCTION";
		String productcode="";
		
		DPK_KVC = sec.getCheckDigit(ClearDPK);
		System.out.println("DPK KCV--->" + DPK_KVC ); //For Display & Store in DB
		
		//EDPK = sec.getEDPK(EDMK, ClearDPK);
		//System.out.println("Encrpted DPK--->" + EDPK );//Store in DB
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		int cnt=0,updatecount=0,rescardcnt=0;
		StringBuffer hcardno = null;
		String mcardno="",ecardno="",orederno="",cardtypeid="",limitid="";
		
		hcardno =sec.getHashedValue("5816930000914607"+instid);
		System.out.println("hcardno"+hcardno);
		
		List getorderrefno=null;
		for(int i=0;i<card_no.length;i++)
		{
			//System.out.println("coming into for loop area");
			List cardmarchdet = null,getprodorderref=null;
			int cardcount = 0,pcpcnt=0,aicnt=0,cicnt=0,ezauthcount=0,updezauthcnt=0,updatecuim=0;
			
			int resprod = 0,rescin = 0;
			String getcardmarchdet = "select * FROM MIGDATA where CHN='"+card_no[i].toString()+"' AND HCARD_NO is null ";// AND PRIMARY_ACCOUNT_NUMBER ='"+accountno[i].toString()+"'";
			//System.out.println("geettingin into card march " + getcardmarchdet);
			cardmarchdet = jdbctemplate.queryForList(getcardmarchdet);
			//System.out.println("coming into cardmarchdet" +cardmarchdet);
			
			String orderreferenceno="";
			
			  System.out.println("EDMK"+EDMK+"\n EDPK"+EDPK);
			Iterator cardmarchitr = cardmarchdet.iterator();
			while(cardmarchitr.hasNext())
			{
				//System.out.println("coming into bean area");
				
				Map map = (Map) cardmarchitr.next();
				//Date sa=new Date(map.get("EXP_DATE").toString());
				//System.out.println("date"+sa);
				cardmigratebean.setBincode((String)map.get("BIN")==null?"NA":(String)map.get("BIN"));
				cardmigratebean.setCardno((String)map.get("CHN")==null?"NA":(String)map.get("CHN"));
				cardmigratebean.setExpdate((String)map.get("EXP_DATE").toString()==null?"NA":(String)map.get("EXP_DATE").toString());
				cardmigratebean.setBranchcode((String)map.get("BRANCH_CODE")==null?"NA":(String)map.get("BRANCH_CODE"));
				//cardmigratebean.setProductcode((String)map.get("PRODUCT_CODE")==null?"NA":(String)map.get("PRODUCT_CODE"));
				cardmigratebean.setPacctno((String)map.get("PAN")==null?"NA":(String)map.get("PAN"));
				cardmigratebean.setSacctno((String)map.get("SAN1")==null?"NA":(String)map.get("SAN1"));
				cardmigratebean.setSan3((String)map.get("SAN2")==null?"NA":(String)map.get("SAN2"));
				cardmigratebean.setSan4((String)map.get("SAN3")==null?"NA":(String)map.get("SAN3"));
				cardmigratebean.setSan5((String)map.get("SAN4")==null?"NA":(String)map.get("SAN4"));
				cardmigratebean.setSan6((String)map.get("SAN5")==null?"NA":(String)map.get("SAN5"));


				//cardmigratebean.setSbranchcode((String)map.get("S1_BRANCH_CODE")==null?"NA":(String)map.get("S1_BRANCH_CODE"));
				//cardmigratebean.setSproductdode((String)map.get("S1_PRODUCT_CODE")==null?"NA":(String)map.get("S1_PRODUCT_CODE"));
				cardmigratebean.setEncname((String)map.get("ENC_NAME")==null?"NA":(String)map.get("ENC_NAME"));
				cardmigratebean.setMailadds1((String)map.get("MAIL_ADDS1")==null?"NA":(String)map.get("MAIL_ADDS1"));
				cardmigratebean.setMailadds2((String)map.get("MAIL_ADDS2")==null?"NA":(String)map.get("MAIL_ADDS2"));
				cardmigratebean.setMailadds3((String)map.get("MAIL_ADDS3")==null?"NA":(String)map.get("MAIL_ADDS3"));
				cardmigratebean.setMailadds4((String)map.get("MAIL_ADDS4")==null?"NA":(String)map.get("MAIL_ADDS4"));
				cardmigratebean.setMobile((String)map.get("MOBILE_NO")==null?"NA":(String)map.get("MOBILE_NO"));
				cardmigratebean.setEmail((String)map.get("EMAIL")==null?"NA":(String)map.get("EMAIL"));
				cardmigratebean.setCustid((String)map.get("CUSTOMER_ID")==null?"NA":(String)map.get("CUSTOMER_ID"));
				cardmigratebean.setDob((String)map.get("DOB")==null?"NA":(String)map.get("DOB"));
				String primarycurr=(String)map.get("P_CURR_CODE")==null?"NA":(String)map.get("P_CURR_CODE");
				//System.out.println("accountcurrtest"+primarycurr);
				if(primarycurr.equalsIgnoreCase("YER"))
				{
					
					primarycurr="886";
					//System.out.println("testing sardar"+primarycurr);
				}
				 if (primarycurr.equals("SAR"))
				{
					
					primarycurr="682";
				}
				 if (primarycurr.equalsIgnoreCase("USD"))
				{
					primarycurr="840";
				}
				//System.out.println("testing sardarlast"+primarycurr);
				cardmigratebean.setCurrcode(primarycurr);
						
				cardmigratebean.setAccttype((String)map.get("P_ACCT_TYPE")==null?"NA":(String)map.get("P_ACCT_TYPE"));
			
				
			String secaccount=	(String)map.get("S1_CURR_CODE")==null?"NA":(String)map.get("S1_CURR_CODE");
				if(secaccount.equalsIgnoreCase("YER"))
				{
					secaccount="886";
				}
				if(secaccount.equalsIgnoreCase("SAR"))
				{
					
					secaccount="682";
				}
				if(secaccount.equalsIgnoreCase("USD"))
				{
					secaccount="840";
				}
				cardmigratebean.setCurrcode1(secaccount);
				
			String secaccount3=(String)map.get("S2_CURR_CODE")==null?"NA":(String)map.get("S2_CURR_CODE");
				if(secaccount3.equalsIgnoreCase("YER"))
				{
					secaccount3="886";
				}
				if(secaccount3.equalsIgnoreCase("SAR"))
				{
					
					secaccount3="682";
				}
				if(secaccount3.equalsIgnoreCase("USD"))
				{
					secaccount3="840";
				}
				cardmigratebean.setCurrcode2(secaccount3);
				
			String acct4=(String)map.get("S3_CURR_CODE")==null?"NA":(String)map.get("S3_CURR_CODE");
				
				if(acct4.equalsIgnoreCase("YER"))
				{
					acct4="886";
				}
				if(acct4.equalsIgnoreCase("SAR"))
				{
					
					acct4="682";
				}
				if(acct4.equalsIgnoreCase("USD"))
				{
					acct4="840";
				}
				cardmigratebean.setCurrcode3(acct4);
				String acct5=(String)map.get("S4_CURR_CODE")==null?"NA":(String)map.get("S4_CURR_CODE");
				if(acct5.equalsIgnoreCase("YER"))
				{
					acct5="886";
				}
				if(acct5.equalsIgnoreCase("SAR"))
				{
					
					acct5="682";
				}
				if(acct5.equalsIgnoreCase("USD"))
				{
					acct5="840";
				}
				cardmigratebean.setCurrcode4(acct5);
				String acct6=(String)map.get("S5_CURR_CODE")==null?"NA":(String)map.get("S5_CURR_CODE");
				if(acct6.equalsIgnoreCase("YER"));
				{
					acct6="886";
				}
				if(acct6.equalsIgnoreCase("SAR"))
				{
					
					acct6="682";
				}
				if(acct6.equalsIgnoreCase("USD"))
				{
					acct6="840";
				}
				cardmigratebean.setCurrcode5(acct6);
				cardmigratebean.setAccttype1((String)map.get("S1_ACCT_TYPE")==null?"NA":(String)map.get("S1_ACCT_TYPE"));
				cardmigratebean.setAccttype2((String)map.get("S2_ACCT_TYPE")==null?"NA":(String)map.get("S2_ACCT_TYPE"));
				cardmigratebean.setAccttype3((String)map.get("S3_ACCT_TYPE")==null?"NA":(String)map.get("S3_ACCT_TYPE"));
				cardmigratebean.setAccttype4((String)map.get("S4_ACCT_TYPE")==null?"NA":(String)map.get("S4_ACCT_TYPE"));
				cardmigratebean.setAccttype5((String)map.get("S5_ACCT_TYPE")==null?"NA":(String)map.get("S5_ACCT_TYPE"));
				
				PadssSecurity padsssec = new PadssSecurity();
				//BY SIVA
				hcardno =sec.getHashedValue(cardmigratebean.getCardno()+instid);
				Properties props=getCommonDescProperty();
				 EDPK=props.getProperty("EDPK");
				String CDPK=padsssec.decryptDPK(EDMK, EDPK);
				ecardno =sec.getECHN(CDPK, cardmigratebean.getCardno().toString());
				//BY SIVA
				
				//ecardno =sec.getECHN(EDMK, EDPK, cardmigratebean.getCardno().toString());
				mcardno =sec.getMakedCardno( cardmigratebean.getCardno().toString());
				//primary
				//System.out.println("getting bin code "+cardmigratebean.getBincode());
				cardtypeid = this.comProductId(instid,cardmigratebean.getBincode(),jdbctemplate);
				//System.out.println("ggeting card type id" +cardtypeid);
				productcode = this.getProductcode(instid,cardmigratebean.getBincode(),jdbctemplate);
				//System.out.println("getting product code list "+productcode);
				subproductCode = this.getProductBySubProduct(instid, productcode, jdbctemplate);
			//	System.out.println("getting sub product code "+ subproductCode);
				limitid = this.getSubProductLimitId(instid, subproductCode, jdbctemplate);
			
				int production=0,customerinfo=0,ACCOUNTINFO_MG=0;
				/*												
				String cardcheckqry="select count(*) as cardcnt from CARDMARCH where PRIMARY_ACCOUNT_NUMBER='"+cardmigratebean.getPacctno()+"'";
				
				
				int getcountcard = jdbctemplate.queryForInt(cardcheckqry);
				System.out.println("cardcheckqry"+getcountcard);
				if(getcountcard==1){						
					*/
				int pcustcount =0,ezcustnfo=0,pcardninfo=0,pezaccount=0,pezauth=0,pezaccum=0;
					String cardcheckqry1="select count(CARD_NO) as cardcnt from CARD_PRODUCTION_MG where CARD_NO='"+ecardno+"' ";
					
					
					int ACCOUNTINFO_MG2=0,ACCOUNTINFO_MG3=0,ACCOUNTINFO_MG4=0,ACCOUNTINFO_MG5=0,ACCOUNTINFO_MG6=0;	
					int aicnt2=0,aicnt3=0,aicnt4=0,aicnt5=0,aicnt6=0;
					int pezaccount2=0,pezaccount3=0,pezaccount4=0,pezaccount5=0,pezaccount6=0;
							int pezauth2=0,pezauth3=0,pezauth4=0,pezauth5=0,pezauth6=0;
						int getcountcard1 = jdbctemplate.queryForInt(cardcheckqry1);
						//System.out.println("getcountcard1 "+getcountcard1);					
						
						
					if (getcountcard1 ==0 ){
					
						
						//ORDER REF NUMBER	
						synchronized(this){
							orderreferenceno = this.commondesc.generateorderRefno( instid, jdbctemplate );
							orederno = orderreferenceno;
						}
						if(orederno == null){
								orederno = "LPAD("+orederno+","+orderrefnolength+",'0')";
						}
						
						StringBuilder pcp = new StringBuilder();
						pcp.append("INSERT INTO CARD_PRODUCTION_MG  ( ");
						pcp.append("INST_ID, CARD_NO, CIN, ORDER_REF_NO, CARD_STATUS, ACCTTYPE_ID,ACC_CCY,CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ");
						pcp.append("GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,");
						pcp.append("APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,");
						pcp.append("WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,");
						pcp.append("BIN, AUTH_DATE, STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV, CARD_REF_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO,MOBILENO,MIG_FLAG,CARD_COLLECT_BRANCH,PC_FLAG,RENEWAL_CARD)");
						pcp.append(" SELECT '"+instid+"', '"+ecardno+"' CARD_NO, CUSTOMER_ID, LPAD("+orederno+","+orderrefnolength+",'0') ORDER_REF_NO, '05',P_ACCT_TYPE,'"+primarycurr+"',");
						pcp.append("'"+cardtypeid+"' CARD_TYPE_ID,'"+subproductCode+"' SUB_PROD_ID, '5816931101', trim(BRANCH_CODE), '', '',");
						pcp.append("SYSDATE,to_char(EXP_DATE,'DD-MON-YY')as EXP_DATE, '', '"+comUsername()+"', SYSDATE, '', '', 'P', '886', SYSDATE, ");
						pcp.append("SYSDATE, '', '', SYSDATE,'', 'A', (select FEE_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)FEE_CODE, (select LIMIT_ID from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)LIMIT_ID, '', '', ");
						pcp.append("'','','','','',ENC_NAME,ENC_NAME,'','',CHN,CHN,'',BIN, '', '97', PIN, '', '0', '', '', '', '', '','"+hcardno+"' HCARD_NO,'"+mcardno+"' MCARD_NO,PAN,NVL(MOBILE_NO,'NA') MOBILE_NUMBER,'M','"+cardmigratebean.getBranchcode()+"','P','Y', FROM MIGDATA WHERE CHN='"+cardmigratebean.getCardno()+"' AND ROWNUM=1");
						enctrace("inserting  into ifd card production " + pcp.toString());	
						pcpcnt = jdbctemplate.update(pcp.toString());
	
						production=pcpcnt;
					}
					else{
						production=1;
					}
					/*else{
						
						orederno = this.getorderrefno(instid, hcardno.toString(), jdbctemplate);
						production=1;
						trace("else already production available  "+production+"\n orederno"+orederno);
					}*/
					
					String customercheckqry="select count(CIN) as custcnt from CUSTOMERINFO_MG WHERE CIN='"+cardmigratebean.getCustid()+"'";
					//System.out.println("customercheckqry"+customercheckqry);
					int custcount = jdbctemplate.queryForInt(customercheckqry);
					//System.out.println("customerCINCOUNT"+custcount);
					if(custcount == 0){
							
					 
						StringBuilder ci = new StringBuilder();
						//System.out.println("instid-->"+instid+"--orderrefno-->"+orederno); 
						ci.append("INSERT INTO CUSTOMERINFO_MG ");
						ci.append("select '"+instid+"'INST_ID, '"+orederno+"' ORDER_REF_NO, '"+cardmigratebean.getCustid()+"' CIN, ENC_NAME FNAME, ''MNAME, ''LNAME, ");
						ci.append("NVL(DOB,'01-JAN-1900') DOB, '' GENDER, '' MARITAL_STATUS, '' NATIONALITY, '' DOCUMENT_PROVIDED, '' DOCUMENT_NUMBER, '' SPOUCE_NAME, ");
						ci.append("'' MOTHER_NAME, ''FATHER_NAME, MOBILE_NO MOBILE, EMAIL E_MAIL, '' MAIL_ADDS1, '' MAIL_ADDS2, '' MAIL_ADDS3, '' MAIL_ADDS4, ");
						ci.append("'' MAIL_ADDS5, '' P_DISTRICT, '' P_PHONE1, '' P_PHONE2, '' C_PO_BOX, '' C_HOUSE_NO, '' C_STREET_NAME, '' C_WARD_NAME, ");
						ci.append("'' C_CITY, '' C_DISTRICT, '' C_PHONE1, '' C_PHONE2, '' MAKER_DATE, '1' MAKER_ID, ''CHECKER_DATE, ''CHECKER_ID, 'P'MKCK_STATUS, 'M'CUSTOMER_STATUS from MIGDATA ");
						ci.append("WHERE CUSTOMER_ID='"+cardmigratebean.getCustid()+"' and ROWNUM<='1'");
						enctrace("insert into ifd customerinfo "+ci.toString());
						//System.out.println(ci.toString());     
						
						cicnt = jdbctemplate.update(ci.toString());
						
						customerinfo=cicnt;						
					
					
					}	
					
					else{
						trace("else already customerinfo available  "+customerinfo);
						customerinfo=1;
					}
					orederno = this.getorderrefno(instid, hcardno.toString(), jdbctemplate);
					String oredernoval="";
					if(orederno!=null){
						oredernoval=orederno;	
					}
					else{
						oredernoval=orederno;
					}
					
			//ezcustomerinfo		
					
					String custexistqry = "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO_MG WHERE CUSTID= '"+cardmigratebean.getCustid()+"'";		
					int ezcustcount = jdbctemplate.queryForInt(custexistqry);
					if (ezcustcount == 0){
						StringBuilder cinf_4 = new StringBuilder();		
								cinf_4.append("INSERT INTO EZCUSTOMERINFO_MG ");
								cinf_4.append("(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) ");
								cinf_4.append("VALUES ");
								cinf_4.append("('"+instid+"','"+cardmigratebean.getCustid()+"','"+cardmigratebean.getEncname()+"',TO_CHAR("+cardmigratebean.getDob()+",'DD-MON-YY'),'', ");
								cinf_4.append("'"+cardmigratebean.getMailadds1()+"','"+cardmigratebean.getMailadds2()+"','"+cardmigratebean.getMailadds3()+"','"+cardmigratebean.getMailadds4()+"','"+cardmigratebean.getMobile()+"','"+cardmigratebean.getEmail()+"','"+cardmigratebean.getMobile()+"') ");
								ezcustnfo =jdbctemplate.update(cinf_4.toString());
								enctrace("insert into ezcustinfo " +ezcustnfo);
					}
					
					else{
						ezcustnfo=1;
						trace("else already ezcustcount available  "+pcustcount);
					}
						
	//1st start		
					String accountcheckqry="select count(*) as cnt from ACCOUNTINFO_MG where ACCOUNTNO='"+cardmigratebean.getPacctno()+"' and ACCOUNTNO !='NA'";				
					//System.out.println("accountcheckqry"+accountcheckqry);
					int rescount = jdbctemplate.queryForInt(accountcheckqry);	
					String pan=(String)map.get("PAN");
				
					if (rescount == 0 &&pan !=null ){					
							StringBuilder ai = new StringBuilder();
							ai.append("INSERT INTO ACCOUNTINFO_MG ");
							ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
							ai.append("(select '"+instid+"' INST_ID,'"+orederno+"' ORDER_REF_NO, '"+cardmigratebean.getCustid()+"' CIN, P_ACCT_TYPE ACCTTYPE_ID, ");
							ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, '"+primarycurr+"' ACCT_CURRENCY, PAN ACCOUNTNO, 'CDTP'ACCOUNTTYPE, ");
							ai.append("'0'AVAILBAL, '0'LEDGERBAL,'"+comUsername()+"'ADDEDBY, sysdate ADDED_DATE from MIGDATA ");
							ai.append("WHERE PAN='"+cardmigratebean.getPacctno()+"' )");
							System.out.println(ai.toString());    
							enctrace("insert into ifdACCOUNTINFO_MG"+ ai.toString());
							aicnt = jdbctemplate.update(ai.toString());
							ACCOUNTINFO_MG=aicnt;
									
							
					}
					else{
						ACCOUNTINFO_MG=1;
					}

					 String acctexistqry = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO_MG WHERE ACCOUNTNO= '"+cardmigratebean.getPacctno()+"'and ACCOUNTNO !='NA'";		
						int acctexist = jdbctemplate.queryForInt(acctexistqry);	
						if(acctexist==0 &&pan !=null ){
						StringBuilder ezac_2 = new StringBuilder();		
						ezac_2.append("INSERT INTO EZACCOUNTINFO_MG ");
						ezac_2.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
						ezac_2.append("VALUES ");
						ezac_2.append("('"+instid+"','"+cardmigratebean.getPacctno()+"','"+cardmigratebean.getAccttype()+"','"+primarycurr+"','0','0','"+limitid+"' ,'16',");
						ezac_2.append("'01',TO_CHAR(SYSDATE,'DD-MON-YY'),TO_CHAR(SYSDATE,'HH24MISS'),'"+cardmigratebean.getBranchcode()+"','"+subproductCode+"' )");	
						enctrace("ezac_2::::"+ezac_2.toString());	
						pezaccount = jdbctemplate.update(ezac_2.toString());
							
						
						
						
						}
						
							
						//System.out.println("coming into ezauthrel");
						String authrelcountqry="select count(ACCOUNTNO) as cnt from EZAUTHREL_MG where chn='"+hcardno+"' AND ACCOUNTNO= '"+cardmigratebean.getPacctno()+"'and ACCOUNTNO !='NA' ";	
						ezauthcount = jdbctemplate.queryForInt(authrelcountqry);
						//System.out.println("coming into count"+ezauthcount);
					/*	String authrelcountqry123="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
						int ezauthcount123 = jdbctemplate.queryForInt(authrelcountqry123);
*/
						if(ezauthcount ==0 &&pan !=null ){
							
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL_MG ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+cardmigratebean.getPacctno()+"','"+cardmigratebean.getAccttype()+"','P','1','"+primarycurr+"') ");
					        enctrace("authrel_1::::"+authrel_1.toString());
							pezauth = jdbctemplate.update(authrel_1.toString());
							
						}
		//1st end
						
						
		//2nd start
						
					String sanaccount="select count(*) as cnt from ACCOUNTINFO_MG where ACCOUNTNO='"+cardmigratebean.getSacctno()+"'and ACCOUNTNO !='NA'";				
					//System.out.println("sanaccount"+sanaccount);
					int secacc2 = jdbctemplate.queryForInt(sanaccount);	
					//account2
	              String san1=(String)map.get("SAN1");
	            //  System.out.println("san1111"+san1);
	           
					if (secacc2==0 && san1 !=null ){
						
							StringBuilder ai = new StringBuilder();
							ai.append("INSERT INTO ACCOUNTINFO_MG ");
							ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
							ai.append("(select '"+instid+"' INST_ID,'"+orederno+"' ORDER_REF_NO, '"+cardmigratebean.getCustid()+"' CIN, S1_ACCT_TYPE ACCTTYPE_ID, ");
							ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, '"+secaccount+"' ACCT_CURRENCY, SAN1 ACCOUNTNO, 'CDTP'ACCOUNTTYPE, ");
							ai.append("'0'AVAILBAL, '0'LEDGERBAL,'"+comUsername()+"'ADDEDBY, sysdate ADDED_DATE from MIGDATA ");
							ai.append("WHERE SAN1='"+cardmigratebean.getSacctno()+"' )");
							System.out.println(ai.toString());    
							enctrace("insert into ifdACCOUNTINFO_MG"+ ai.toString());
							aicnt2 = jdbctemplate.update(ai.toString());
							ACCOUNTINFO_MG2=aicnt2;
													
							
					}
					else{
						ACCOUNTINFO_MG=2;
					}
					 String acctexistqry2 = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO_MG WHERE ACCOUNTNO= '"+cardmigratebean.getSacctno()+"'and ACCOUNTNO !='NA'";		
						int acctexist2 = jdbctemplate.queryForInt(acctexistqry2);	
						if(acctexist2==0 && san1 !=null ){
						StringBuilder ezac_3 = new StringBuilder();		
						ezac_3.append("INSERT INTO EZACCOUNTINFO_MG ");
						ezac_3.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
						ezac_3.append("VALUES ");
						ezac_3.append("('"+instid+"','"+cardmigratebean.getSacctno()+"','"+cardmigratebean.getAccttype1()+"','"+secaccount+"','0','0','"+limitid+"' ,'16',");
						ezac_3.append("'01',TO_CHAR(SYSDATE,'DD-MON-YY'),TO_CHAR(SYSDATE,'HH24MISS'),'"+cardmigratebean.getBranchcode()+"','"+subproductCode+"' )");	
						enctrace("ezac_2::::"+ezac_3.toString());	
						pezaccount2 = jdbctemplate.update(ezac_3.toString());
						}
						
						//System.out.println("coming into ezauthrel");
						String authrelcountqry2="select count(ACCOUNTNO) as cnt from EZAUTHREL_MG where chn='"+hcardno+"' AND ACCOUNTNO= '"+cardmigratebean.getSacctno()+"'and ACCOUNTNO !='NA'";	
				int		ezauthcount2 = jdbctemplate.queryForInt(authrelcountqry2);
						//System.out.println("coming into count"+ezauthcount2);
					/*	String authrelcountqry123="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
						int ezauthcount123 = jdbctemplate.queryForInt(authrelcountqry123);
*/
						if(ezauthcount2 ==0 && san1 !=null  ){
							
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL_MG ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+cardmigratebean.getSacctno()+"','"+cardmigratebean.getAccttype1()+"','P','1','"+secaccount+"') ");
					        enctrace("authrel_1::::"+authrel_1.toString());
							pezauth2 = jdbctemplate.update(authrel_1.toString());
							
						}
			//2nd end	
						
		//3nd start				
						
					String sanaccount3="select count(*) as cnt from ACCOUNTINFO_MG where ACCOUNTNO='"+cardmigratebean.getSan3()+"'and ACCOUNTNO !='NA'";				
					//System.out.println("sanaccount"+sanaccount3);
					int secacc3 = jdbctemplate.queryForInt(sanaccount3);
					String san2=(String)map.get("SAN2");
					// System.out.println("san2222"+san2);
					//account3
					if (secacc3==0 && san2 !=null ){
						
						StringBuilder ai = new StringBuilder();
						ai.append("INSERT INTO ACCOUNTINFO_MG ");
						ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
						ai.append("(select '"+instid+"' INST_ID,'"+orederno+"' ORDER_REF_NO,'"+cardmigratebean.getCustid()+"' CIN, S2_ACCT_TYPE ACCTTYPE_ID, ");
						ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, '"+secaccount3+"' ACCT_CURRENCY, SAN2 ACCOUNTNO, 'CDTP'ACCOUNTTYPE, ");
						ai.append("'0'AVAILBAL, '0'LEDGERBAL,'"+comUsername()+"'ADDEDBY, sysdate ADDED_DATE from MIGDATA ");
						ai.append("WHERE  SAN2='"+cardmigratebean.getSan3()+"' )");
						//System.out.println(ai.toString());    
						enctrace("insert into ifdACCOUNTINFO_MG"+ ai.toString());
						aicnt3 = jdbctemplate.update(ai.toString());
						ACCOUNTINFO_MG3=aicnt3;
												
						
				}
					else{
						ACCOUNTINFO_MG3=0;
					}
					 String acctexistqry3 = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO_MG WHERE ACCOUNTNO= '"+cardmigratebean.getSan3()+"'and ACCOUNTNO !='NA'";		
						int acctexist3 = jdbctemplate.queryForInt(acctexistqry3);	
						if(acctexist3==0 && san2 !=null){
						StringBuilder ezac_4 = new StringBuilder();		
						ezac_4.append("INSERT INTO EZACCOUNTINFO_MG ");
						ezac_4.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
						ezac_4.append("VALUES ");
						ezac_4.append("('"+instid+"','"+cardmigratebean.getSan3()+"','"+cardmigratebean.getAccttype2()+"','"+secaccount3+"','0','0','"+limitid+"' ,'16',");
						ezac_4.append("'01',TO_CHAR(SYSDATE,'DD-MON-YY'),TO_CHAR(SYSDATE,'HH24MISS'),'"+cardmigratebean.getBranchcode()+"','"+subproductCode+"' )");	
						enctrace("ezac_2::::"+ezac_4.toString());	
						pezaccount3 = jdbctemplate.update(ezac_4.toString());
							
						}
						//System.out.println("coming into ezauthrel");
						String authrelcountqry3="select count(ACCOUNTNO) as cnt from EZAUTHREL_MG where chn='"+hcardno+"' AND ACCOUNTNO= '"+cardmigratebean.getSan3()+"'and ACCOUNTNO !='NA'";	
				int		ezauthcount3 = jdbctemplate.queryForInt(authrelcountqry3);
						//System.out.println("coming into count3rd"+ezauthcount3);
					/*	String authrelcountqry123="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
						int ezauthcount123 = jdbctemplate.queryForInt(authrelcountqry123);
*/
						if(ezauthcount3 ==0 && san2 !=null){
							
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL_MG ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+cardmigratebean.getSan3()+"','"+cardmigratebean.getAccttype2()+"','P','1','"+secaccount3+"') ");
					        enctrace("authrel_3::::"+authrel_1.toString());
							pezauth3 = jdbctemplate.update(authrel_1.toString());
							
						}
	//3rd end
						
						
	//4th start					
						
					String sanaccount4="select count(*) as cnt from ACCOUNTINFO_MG where ACCOUNTNO='"+cardmigratebean.getSan4()+"'and ACCOUNTNO !='NA'";				
				//	System.out.println("sanaccount4"+sanaccount4);
					int secacc4 = jdbctemplate.queryForInt(sanaccount4);
					String san3=(String)map.get("SAN3");
					// System.out.println("san3333"+san3);
					//account3
					if (secacc4==0 && san3 !=null ){
						
						StringBuilder ai = new StringBuilder();
						ai.append("INSERT INTO ACCOUNTINFO_MG ");
						ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
						ai.append("(select '"+instid+"' INST_ID,'"+orederno+"' ORDER_REF_NO,'"+cardmigratebean.getCustid()+"' CIN, S3_ACCT_TYPE ACCTTYPE_ID, ");
						ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, '"+acct4+"' ACCT_CURRENCY, SAN3 ACCOUNTNO, 'CDTP'ACCOUNTTYPE, ");
						ai.append("'0'AVAILBAL, '0'LEDGERBAL,'"+comUsername()+"'ADDEDBY, sysdate ADDED_DATE from MIGDATA ");
						ai.append("WHERE  SAN3='"+cardmigratebean.getSan4()+"' )");
						System.out.println(ai.toString());    
						enctrace("insert into ifdACCOUNTINFO_MG"+ ai.toString());
						aicnt4 = jdbctemplate.update(ai.toString());
						ACCOUNTINFO_MG4=aicnt4;
												
						
				}
					
					 String acctexistqry4 = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO_MG WHERE ACCOUNTNO= '"+cardmigratebean.getSan4()+"'and ACCOUNTNO !='NA'";		
						int acctexist4 = jdbctemplate.queryForInt(acctexistqry4);	
						if(acctexist4==0 && san3 !=null){
						StringBuilder ezac_5 = new StringBuilder();		
						ezac_5.append("INSERT INTO EZACCOUNTINFO_MG ");
						ezac_5.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
						ezac_5.append("VALUES ");
						ezac_5.append("('"+instid+"','"+cardmigratebean.getSan4()+"','"+cardmigratebean.getAccttype3()+"','"+acct4+"','0','0','"+limitid+"' ,'16',");
						ezac_5.append("'01',TO_CHAR(SYSDATE,'DD-MON-YY'),TO_CHAR(SYSDATE,'HH24MISS'),'"+cardmigratebean.getBranchcode()+"','"+subproductCode+"' )");	
						enctrace("ezac_5::::"+ezac_5.toString());	
						pezaccount4 = jdbctemplate.update(ezac_5.toString());
							
						}
						//System.out.println("coming into ezauthrel5");
						String authrelcountqry5="select count(ACCOUNTNO) as cnt from EZAUTHREL_MG where chn='"+hcardno+"' AND ACCOUNTNO= '"+cardmigratebean.getSan4()+"'and ACCOUNTNO !='NA'";	
				int		ezauthcount5 = jdbctemplate.queryForInt(authrelcountqry5);
						//System.out.println("coming into count5rd"+ezauthcount5);
					/*	String authrelcountqry123="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
						int ezauthcount123 = jdbctemplate.queryForInt(authrelcountqry123);
*/
						if(ezauthcount5 ==0 && san3 !=null ){
							
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL_MG ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+cardmigratebean.getSan4()+"','"+cardmigratebean.getAccttype3()+"','P','1','"+acct4+"') ");
					        enctrace("authrel_3::::"+authrel_1.toString());
							pezauth4 = jdbctemplate.update(authrel_1.toString());
							
						}
						
			//4th end 
						
			//5TH start
						
					String sanaccount5="select count(*) as cnt from ACCOUNTINFO_MG where ACCOUNTNO='"+cardmigratebean.getSan5()+"'";				
					//System.out.println("sanaccount5"+sanaccount5);
					int secacc5 = jdbctemplate.queryForInt(sanaccount5);
					String san4=(String)map.get("SAN4");
					//account3
					if (secacc5==0 && san4 !=null ){
						
						StringBuilder ai = new StringBuilder();
						ai.append("INSERT INTO ACCOUNTINFO_MG ");
						ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
						ai.append("(select '"+instid+"' INST_ID,'"+orederno+"' ORDER_REF_NO, '"+cardmigratebean.getCustid()+"' CIN, S4_ACCT_TYPE ACCTTYPE_ID, ");
						ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, '"+acct5+"' ACCT_CURRENCY, SAN4 ACCOUNTNO, 'CDTP'ACCOUNTTYPE, ");
						ai.append("'0'AVAILBAL, '0'LEDGERBAL,'"+comUsername()+"'ADDEDBY, sysdate ADDED_DATE from MIGDATA ");
						ai.append("WHERE  SAN4='"+cardmigratebean.getSan5()+"' )");
						System.out.println(ai.toString());    
						enctrace("insert into ifdACCOUNTINFO_MG"+ ai.toString());
						aicnt5 = jdbctemplate.update(ai.toString());
						ACCOUNTINFO_MG5=aicnt5;
												
						
				}else{
					ACCOUNTINFO_MG5=0;
				}
					 String acctexistqry5 = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO_MG WHERE ACCOUNTNO= '"+cardmigratebean.getSan5()+"'and ACCOUNTNO !='NA'";		
						int acctexist5 = jdbctemplate.queryForInt(acctexistqry5);	
						if(acctexist5==0  && san4 !=null){
						StringBuilder ezac_6 = new StringBuilder();		
						ezac_6.append("INSERT INTO EZACCOUNTINFO_MG ");
						ezac_6.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
						ezac_6.append("VALUES ");
						ezac_6.append("('"+instid+"','"+cardmigratebean.getSan5()+"','"+cardmigratebean.getAccttype4()+"','"+acct5+"','0','0','"+limitid+"' ,'16',");
						ezac_6.append("'01',TO_CHAR(SYSDATE,'DD-MON-YY'),TO_CHAR(SYSDATE,'HH24MISS'),'"+cardmigratebean.getBranchcode()+"','"+subproductCode+"' )");	
						enctrace("ezac_6::::"+ezac_6.toString());	
						pezaccount5 = jdbctemplate.update(ezac_6.toString());
							
						}
						//System.out.println("coming into ezauthrel6");
						String authrelcountqry6="select count(ACCOUNTNO) as cnt from EZAUTHREL_MG where chn='"+hcardno+"' AND ACCOUNTNO= '"+cardmigratebean.getSan5()+"'and ACCOUNTNO !='NA'";	
				int		ezauthcount6 = jdbctemplate.queryForInt(authrelcountqry6);
						//System.out.println("coming into count5rd"+ezauthcount6);
					/*	String authrelcountqry123="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
						int ezauthcount123 = jdbctemplate.queryForInt(authrelcountqry123);
*/
						if(ezauthcount6 ==0  && san4 !=null){
							
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL_MG ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+cardmigratebean.getSan5()+"','"+cardmigratebean.getAccttype4()+"','P','1','"+acct5+"') ");
					        enctrace("authrel_3::::"+authrel_1.toString());
							pezauth5 = jdbctemplate.update(authrel_1.toString());
							
						}
						
						
			//5th End 
			//6th start 	
						
					String sanaccount6="select count(*) as cnt from ACCOUNTINFO_MG where ACCOUNTNO='"+cardmigratebean.getSan6()+"'";				
				//	System.out.println("sanaccoun6"+sanaccount6);
					int secacc6 = jdbctemplate.queryForInt(sanaccount6);
					String san5=(String)map.get("SAN5");
				//	System.out.println("san55"+san5);
					//account3
					if (secacc6==0 && san5 !=null ){
						
						StringBuilder ai = new StringBuilder();
						ai.append("INSERT INTO ACCOUNTINFO_MG ");
						ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
						ai.append("(select '"+instid+"' INST_ID,'"+orederno+"' ORDER_REF_NO, '"+cardmigratebean.getCustid()+"' CIN, S5_ACCT_TYPE ACCTTYPE_ID, ");
						ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, '"+acct6+"' ACCT_CURRENCY, SAN5 ACCOUNTNO, 'CDTP'ACCOUNTTYPE, ");
						ai.append("'0'AVAILBAL, '0'LEDGERBAL,'"+comUsername()+"'ADDEDBY, sysdate ADDED_DATE from MIGDATA ");
						ai.append("WHERE  SAN5='"+cardmigratebean.getSan6()+"' )");
					//	System.out.println(ai.toString());    
						enctrace("insert into ifdACCOUNTINFO_MG"+ ai.toString());
						aicnt6 = jdbctemplate.update(ai.toString());
						ACCOUNTINFO_MG6=aicnt6;
												
						
				}else{
					ACCOUNTINFO_MG6=0;
				}
					 String acctexistqry6 = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO_MG WHERE ACCOUNTNO= '"+cardmigratebean.getSan6()+"'and ACCOUNTNO !='NA'";		
						int acctexist6 = jdbctemplate.queryForInt(acctexistqry6);	
						if(acctexist6==0 && san5 !=null ){
						StringBuilder ezac_7 = new StringBuilder();		
						ezac_7.append("INSERT INTO EZACCOUNTINFO_MG ");
						ezac_7.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
						ezac_7.append("VALUES ");
						ezac_7.append("('"+instid+"','"+cardmigratebean.getSan6()+"','"+cardmigratebean.getAccttype5()+"','"+acct6+"','0','0','"+limitid+"' ,'16',");
						ezac_7.append("'01',TO_CHAR(SYSDATE,'DD-MON-YY'),TO_CHAR(SYSDATE,'HH24MISS'),'"+cardmigratebean.getBranchcode()+"','"+subproductCode+"' )");	
						enctrace("ezac_7::::"+ezac_7.toString());	
						pezaccount6 = jdbctemplate.update(ezac_7.toString());
						}
						
						//System.out.println("coming into ezauthrel6");
						String authrelcountqry7="select count(ACCOUNTNO) as cnt from EZAUTHREL_MG where chn='"+hcardno+"' AND ACCOUNTNO= '"+cardmigratebean.getSan6()+"'and ACCOUNTNO !='NA'";	
				int		ezauthcount7 = jdbctemplate.queryForInt(authrelcountqry7);
						//System.out.println("coming into count7rd"+ezauthcount7);
					/*	String authrelcountqry123="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
						int ezauthcount123 = jdbctemplate.queryForInt(authrelcountqry123);
*/
						if(ezauthcount6 ==0 && san5 !=null ){
							
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL_MG ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+cardmigratebean.getSan6()+"','"+cardmigratebean.getAccttype5()+"','P','1','"+acct6+"') ");
					        enctrace("authrel_3::::"+authrel_1.toString());
							pezauth6 = jdbctemplate.update(authrel_1.toString());
							
						}
					
					
					//trace("getting count for account "+ACCOUNTINFO_MG);
					//trace("getting count for prodcount "+production);
					//trace("getting count for custcount "+customerinfo);
					
					
			
							try {
								
									//int personalcardissue = this.personalCardIssuence(hcardno.toString(),padssenable,instid,userid,tablename,jdbctemplate);
									
							//	     if (production > 0 && customerinfo>=0 && ACCOUNTINFO_MG >=0){
									
									
									
									//INSERTING INTO PRIMARY DETAILS
								
									try{
									String EZCARDINFO_MGeistqry = "SELECT COUNT(*) as cnt FROM EZCARDINFO_MG WHERE CHN= '"+hcardno+"'";		
									int EZCARDINFO_MGexist = jdbctemplate.queryForInt(EZCARDINFO_MGeistqry);
									if (EZCARDINFO_MGexist == 0){
										String expdate=cardmigratebean.getExpdate().substring(0, 10);
										
												StringBuilder crdinf_3 = new StringBuilder();		
												crdinf_3.append("INSERT INTO EZCARDINFO_MG ");
												crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,INTVLFLAG,CVV, PANSEQNO) ");
												crdinf_3.append("VALUES ");
												crdinf_3.append("('"+instid+"','"+hcardno.toString()+"','"+cardtypeid+"','"+cardmigratebean.getCustid()+"','01','"+limitid+"', (SELECT EXP_DATE from migdata MIGDATA WHERE CHN='"+cardmigratebean.getCardno()+"' AND ROWNUM=1)  ,'97','0','0',");
												crdinf_3.append("'0','0','0','0','0',TO_CHAR(SYSDATE,'DD-MON-YY'),'00','1','0' ,");
												crdinf_3.append("'00' )");
												pcardninfo = jdbctemplate.update(crdinf_3.toString());											
												enctrace("insert into cardinfo "+ crdinf_3);
									}											
								//2	
									else{
										pcardninfo=1;
										trace("else already EZCARDINFO_MGexist available  "+pcardninfo);
									}
									 
									}
									catch(Exception e){
										trace("exception ezcardinfo"+e.getMessage());
										txManager.rollback(transact.status);
									}
											
													
								
								/*	if(ezauthcount ==1 )	{	
												
									String insertezauth = " INSERT INTO EZAUTHREL (" +
											"INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE ) VALUES ( " +
											"'"+instid+"', '"+hcardno+"','"+cardmigratebean.getPacctno()+"' ,'"+cardmigratebean.getAccttype()+"','S','2','"+cardmigratebean.getCurrcode()+"') ";
									//System.out.println("insert into insertezauth " +insertezauth);
									enctrace("inserting secondary acct in ezauthrel "+insertezauth);
									pezauth = jdbctemplate.update(insertezauth);
								} */
							
						      //5	
									/*if(ezauthcount ==2 ){
										String insertezauth = " Delete from EZAUTHREL where  chn='"+hcardno+"' AND ACCOUNTNO= '"+cardmigratebean.getPacctno()+"' AND ROWNUM=1";
										//System.out.println("insert into insertezauth " +insertezauth);
										enctrace("Delting secondary acct in ezauthrel "+insertezauth);
										pezauth = jdbctemplate.update(insertezauth);
									  
									}
								*/
								
								String INSTID_5="", LIMITTYPE_5="", LIMITID_5="", TXNCODE_5="", CURRCODE_5="", AMOUNT_5="", COUNT_5="", WAMOUNT_5="", WCOUNT_5="", MAMOUNT_5="", MCOUNT_5="", YAMOUNT_5="", YCOUNT_5="", LIMITDATE_5="";
								
								StringBuilder accinfo_5 = new StringBuilder();
								accinfo_5.append("SELECT ");
								//--EZACCUMINFO start
								//--INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE
								accinfo_5.append("INSTID INSTID_5, CASE  LIMITTYPE ");
								accinfo_5.append("WHEN 'CDTP'  THEN 'CARD' ");  
								accinfo_5.append("WHEN 'CARD' THEN 'CARD' "); 
								accinfo_5.append("WHEN 'ACTP'  THEN 'ACCT' ");  
								accinfo_5.append("WHEN 'ACCT' THEN 'ACCT' END LIMITTYPE_5 ,"); 
								accinfo_5.append("CASE  LIMITTYPE "); 
								if(padssenable.equals("Y")){
								accinfo_5.append(" WHEN 'CDTP'  THEN '"+hcardno.toString()+"'");      
								accinfo_5.append(" WHEN 'CARD' THEN '"+hcardno.toString()+"' "); 
								}
								//wait i wil come
								{
								}
								accinfo_5.append(" WHEN 'ACTP'  THEN '"+cardmigratebean.getPacctno()+"' ");  
								accinfo_5.append(" WHEN 'ACCT' THEN '"+cardmigratebean.getPacctno()+"' END LIMITID_5 ,");  
								accinfo_5.append(" TXNCODE TXNCODE_5, CURRCODE CURRCODE_5, AMOUNT AMOUNT_5, COUNT COUNT_5, "); 
								accinfo_5.append("WAMOUNT WAMOUNT_5, WCOUNT WCOUNT_5, MAMOUNT MAMOUNT_5, MCOUNT MCOUNT_5, YAMOUNT YAMOUNT_5, YCOUNT YCOUNT_5, ");
								accinfo_5.append("(select TO_CHAR(AUTH_DATE,'DD-MON-YYYY') from LIMIT_DESC where INSTID='"+instid+"' AND LIMIT_ID='1') LIMITDATE_5 FROM LIMITINFO ");
								accinfo_5.append("WHERE INSTID='"+instid+"' AND LIMIT_RECID = '"+limitid+"'");
								//--EZACCUMINFO end  
								enctrace("accinfo_5:::::"+accinfo_5.toString());
								
								
								
								
								List movetoAccuminfo= jdbctemplate.queryForList(accinfo_5.toString());
								trace("movetoAccuminfo:::::::::::"+movetoAccuminfo.size()+":::::::"+movetoAccuminfo);   
								Iterator accitr = movetoAccuminfo.iterator();
								int as =0; int incCount = 0;
								while(accitr.hasNext())
								{
									////System.out.println("testing :::::::::::::::::::::::1"+as++);
									incCount = incCount+1;    
									
									Map mp2 = (Map)accitr.next();  
									INSTID_5   = mp2.get("INSTID_5").toString();
									////System.out.println("testing :::::::::::::::::::::::2"+as++);
									LIMITTYPE_5   = mp2.get("LIMITTYPE_5").toString();
									////System.out.println("testing :::::::::::::::::::::::3"+as++);
									LIMITID_5   = mp2.get("LIMITID_5").toString();
									////System.out.println("testing :::::::::::::::::::::::4"+mp2.get("LIMITID_5").toString());
									TXNCODE_5= mp2.get("TXNCODE_5").toString();;
									////System.out.println("testing :::::::::::TXNCODE_5::::::::::::4"+mp2.get("TXNCODE_5").toString());
								
									CURRCODE_5   = mp2.get("CURRCODE_5").toString();;
								////System.out.println("testing :::::::::::currr::::::::::"+mp2.get("CURRCODE_5").toString());
								AMOUNT_5   = mp2.get("AMOUNT_5").toString();;
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								COUNT_5   = mp2.get("COUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								WAMOUNT_5   = mp2.get("WAMOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								WCOUNT_5   = mp2.get("WCOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								MAMOUNT_5   = mp2.get("MAMOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								MCOUNT_5   = mp2.get("MCOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								YAMOUNT_5   = mp2.get("YAMOUNT_5").toString();
								YCOUNT_5   = mp2.get("YCOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								LIMITDATE_5   = mp2.get("LIMITDATE_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
						    
								
										
								
								//String getres_5Query = getres_5Query(instid, LIMITTYPE_5, LIMITID_5, TXNCODE_5, CURRCODE_5, AMOUNT_5, COUNT_5, WAMOUNT_5, WCOUNT_5, MAMOUNT_5, MCOUNT_5, YAMOUNT_5, YCOUNT_5, LIMITDATE_5);
								System.out.println("coming into updatecuim");
								String cumimcountqry="select count(ACCOUNTNO) as cnt from EZAUTHREL_MG where chn='"+hcardno+"'";
								updatecuim = jdbctemplate.queryForInt(cumimcountqry);
								System.out.println("coming into count"+updatecuim);
								if(updatecuim==0){
									StringBuilder AccinfiInsert = new StringBuilder();
									AccinfiInsert.append("INSERT INTO EZACCUMINFO_MG ");
									AccinfiInsert.append("(INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE) ");
									AccinfiInsert.append("VALUES ");
									AccinfiInsert.append("('"+INSTID_5+"','"+LIMITTYPE_5+"','"+LIMITID_5+"','"+TXNCODE_5+"','"+CURRCODE_5+"','0',");
									AccinfiInsert.append("'0','0','0','0','0','0','0','"+LIMITDATE_5+"' )");
									enctrace("accum info::"+AccinfiInsert);  
									 
									pezaccum = jdbctemplate.update(AccinfiInsert.toString());
									//AccinfiInsert = "";
								}
								else{
									pezaccum=1;
									//trace("else already pezaccum available  "+pezaccount);
								}
									
								} 
								//trace("getting count for pcustcount "+pcustcount);
								//trace("getting count for pcardninfo "+pcardninfo);
								//trace("getting count for pezaccount "+pezaccount);
								//trace("getting count for pezauth "+pezauth);
								//trace("getting count for pezaccum "+pezaccum);
								
								if (customerinfo >0 && production >=0 	&& ACCOUNTINFO_MG >=0 && pcardninfo >=0	){
									
									String hashcard="UPDATE MIGDATA SET HCARD_NO='"+hcardno.toString()+"' WHERE CHN='"+cardmigratebean.getCardno()+"'";
									int hashupdate  = jdbctemplate.update(hashcard);
									
									
									/*String cinorder_sequpdate = "UPDATE SEQUENCE_MASTER SET ORDER_REFNO=ORDER_REFNO+1 WHERE INST_ID='"+instid+"'";
									enctrace("cinorder_sequpdate :  "+cinorder_sequpdate);
									int orderupdate = jdbctemplate.update(cinorder_sequpdate);
									if (orderupdate > 0 ){*/
										updatecount = updatecount + 1;	
										System.out.println("how manyth card"+updatecount);
									//}
								 }else{
									trace("failure cards"+cardmigratebean.getPacctno());
									trace("Process Breaked ====> in CAF REC STATUS A 2");
									txManager.rollback(transact.status);
									addActionError("Unable to Continue...");
									return this.cardMigrateHome();	
								 }
								
									
													
																						
									//ended secondary
					
							}
					
							catch(Exception e){
								txManager.rollback(transact.status);
								addActionError("Unable to continue processs");
								e.printStackTrace();			
							}
			}
				}
					
							
				try{
					trace("FINAL UPDATED COUNT "+updatecount);
					if(updatecount > 0){
						txManager.commit(transact.status);
						addActionMessage("Card Succesfully Migrated For "+updatecount);
						return this.cardMigrateHome();
					}else{
						addActionError("Unable to process");
						txManager.rollback(transact.status);				
					}
				}catch(Exception e){
					txManager.rollback(transact.status);
					e.printStackTrace();			
				}
				return this.cardMigrateHome();		
		}
		
																				
}	
	