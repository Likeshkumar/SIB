package com.ifp.maintain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Validation;

public class chnageCbsPrimary extends BaseAction {
	
	CommonDesc commondesc = new CommonDesc();
	
	AuditBeans auditbean = new AuditBeans();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
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
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session  ){ 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	public String comUsername(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
	}
	
	
	private List accounttypedetails;
	private String cardno;
	
	public String changeprimaryhome()
	{
		trace("chnageCbsPrimaryHome method called....");
		return "chnageCbsPrimaryHome";
	}
	
	public String getAccountnoDetails() throws Exception
	{
		
		System.out.println("session ----> ");
		trace("getAccountnoDetails method called....");
		
		trace("saveAccountNumber method called....");
		
		//added by prasad 30-10-19
		HttpSession session = getRequest().getSession(false);	 
		String sessioncsrftoken = (String) session.getAttribute("token");
	     trace("Global session token---> "+sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		trace("getAccountnoDetails  token---->    "+jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}
		
		boolean check;
		int checkvalue;
		StringBuilder sb=null;
	    
		
		String instid = comInstId(session);
		String usercode= comUserCode(session);
		String cardno = getRequest().getParameter("cardnum");
		//System.out.println(cardno);
		
		checkvalue=Validation.number(cardno);
		if(checkvalue==0)
		{
			addActionError("Enter Proper Card Number");
			return changeprimaryhome();
		}
		
        String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		
		String keyid = "";
		String EDMK="", EDPK="";
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		if(padssenable.equals("Y"))
		{
			
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			//System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String eDMK = ((String)map.get("DMK"));
					String eDPK = ((String)map.get("DPK"));
					hcardno = padsssec.getHashedValue(cardno+instid);
				}      
				}
		} 
		    
		int checkvalid =-1;
		
		//trace("hcardno::"+hcardno);
		//trace("cardno::::"+cardno);
		
		
		try{   
			
			// BY SIVA 10-07-2019
			/*if(padssenable.equals("Y")){*/
			checkvalid = this.validateActvatedCard(instid,padssenable, hcardno.toString(), jdbctemplate);
			/*}else{
			checkvalid = this.validateActvatedCard(instid,padssenable, cardno, jdbctemplate);	
			}*/
			
			if(checkvalid<=0)
			{
				addActionError("Entered Card no Not Activated ...Enter Valid Card No.");
				return "chnageCbsPrimaryHome";
			}
			else
			{
				List accounttypedetails = null;
				// BY SIVA 10-07-2019
				/*if(padssenable.equals("Y")){*/
				
				accounttypedetails = this.getAccounttypeinfo(instid,padssenable,hcardno.toString(), jdbctemplate);
				
				/*}else{
				accounttypedetails = this.getAccounttypeinfo(instid,padssenable,cardno, jdbctemplate);	
				}*/
				this.setAccounttypedetails(accounttypedetails);
				
				
				String custname="";
				
				// BY SIVA 10-07-2019
				/*if(padssenable.equals("Y"))
				{*/
					custname = this.getCustomerNameByCard(instid,hcardno.toString(),padssenable, jdbctemplate);
				/*}else{
					 custname = this.getCustomerNameByCard(instid,cardno,padssenable, jdbctemplate);	
				}*/
				
				System.out.println("getting customername "+custname); 
				setCustname(custname);
				
				if(padssenable.equals("Y")){
				
				this.setCardno(hcardno.toString());
				}else
				{
				this.setCardno(cardno);	
				}
				
			}
			
			
			cardno="0000000000000000";
			sb=new StringBuilder(cardno);
			sb.setLength(0);
			
			
		}catch(Exception e ){
			//txManager.rollback(transact.status);
			trace("Exception : " + e.getMessage() );
			addActionError("Unable to process..."); 
		}
		finally {
			cardno=null;
			sb=null;		
		}
		
		//System.out.println(cardno);
		//System.out.println(sb);
		
		//System.out.println(cardno);
		return "chnageCbsPrimaryHome";
	}     
	
	public String chengeprim()        
	{    
		trace("savechnagePrimary method called.....");
		
		HttpSession session = getRequest().getSession(false);
		
	 	//added by prasad 30-10-19
		//HttpSession session = getRequest().getSession(false);	 
		String sessioncsrftoken = (String) session.getAttribute("token");
	     System.out.println("Global session token---> "+sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		   System.out.println("chengeprim() method   token---->    "+jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}
		
		
		String instid = comInstId(session);
		String usercode= comUserCode(session);
		String remarks = getRequest().getParameter("reason");
		String username=comUsername();
		Personalizeorderdetails persorderdetails,bindetails,extradetails;
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		String acct = getRequest().getParameter("primarysec");
		trace("check update account-->"+acct);
		
		String cardno = getRequest().getParameter("cardno");
		trace("Card Number-->"+cardno);
	 	
		String accountnumber[] = getRequest().getParameterValues("accountnovalue");   
		String primarysecondary[] = getRequest().getParameterValues("primarysec");
		IfpTransObj transact = commondesc.myTranObject("changeprimary", txManager);
		
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
	 	  String  prod_code="",sub_prod_id="",br_code="",emb_name="",cust_id="",mcard_no="",orderrefno="";
		  String auditmsg="", primary_acct_no="";
		
		try{
			
 			/*
			 * ADDED ON 02-JULY-2021
			 * TO INSERT THE DATA  INTO AUDITRAN TABLE 
			*/
			
			String card_data="SELECT PRODUCT_CODE,SUB_PROD_ID,BRANCH_CODE,EMB_NAME,CIN,MCARD_NO,ORDER_REF_NO FROM CARD_PRODUCTION   WHERE ORDER_REF_NO IN(SELECT "
			+ "ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"' )";
			enctrace("card_data-->"+card_data);
			List CardDetailsFromProd = jdbctemplate.queryForList(card_data);
			 Iterator itrdata=CardDetailsFromProd.iterator();
			 while  (itrdata.hasNext()){
				 Map mp = (Map) itrdata.next();
				   prod_code=(String) mp.get("PRODUCT_CODE");
				   sub_prod_id=(String) mp.get("SUB_PROD_ID");
				   br_code=(String) mp.get("BRANCH_CODE");
				   emb_name=(String) mp.get("EMB_NAME");
				    cust_id=(String) mp.get("CIN");
				    mcard_no=(String) mp.get("MCARD_NO");
				    orderrefno=(String) mp.get("ORDER_REF_NO");
				    };
	 
		System.out.println(accountnumber.length+""+primarysecondary.length);
		String updauthrel="",acctpriority = "";
		String updauthrel1="";
		String updauthrel2="";
		int updcount = 0;
		int updcount1 = 0;
		int updcount2 = 0;
		int secacct= 1;
		for (int i = 0; i < accountnumber.length; i++) {
			System.out.println("-------------------start-------------------");
			System.out.println("accountnumber----"+accountnumber[i]+"primarysecondary::"+primarysecondary[i]);
		 
			if("S".equalsIgnoreCase(primarysecondary[i])){
				secacct = secacct + 1;
				acctpriority = String.valueOf(secacct);
			}else{
				acctpriority = "'1'";
			}
		 	
			///by gowtham-300819
			updauthrel = "UPDATE EZAUTHREL SET ACCOUNTFLAG=?,ACCOUNTPRIORITY="+acctpriority+""
					+ " WHERE ACCOUNTNO = ? AND CHN=? AND INSTID=? ";
					enctrace("updauthrel::1"+updauthrel);  
					updcount=jdbctemplate.update(updauthrel,new Object[]{primarysecondary[i],accountnumber[i],cardno,instid});
	 	
			if("P".equalsIgnoreCase(primarysecondary[i]))
			{
	 			///by gowtham-300819
				updauthrel1 = "UPDATE CARD_PRODUCTION SET ACCOUNT_NO = ? "
						+ "WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=?) AND "
						+ "INST_ID=? ";
						enctrace("updauthrel::2"+updauthrel1);  		
						updcount1 = jdbctemplate.update(updauthrel1,new Object[]{accountnumber[i],cardno,instid});
						
						updauthrel2 = "UPDATE CARD_PRODUCTION_HASH SET ACCOUNT_NO =? WHERE HCARD_NO=? "
								+ "AND"+ " INST_ID=? ";
						enctrace("updauthrel::3"+updauthrel2);  		
					    updcount2 = jdbctemplate.update(updauthrel2,new Object[]{accountnumber[i],cardno,instid});
		    
	 		}
			
		}
		System.out.println("-------------------end-------------------"+updcount);
		trace(accountnumber.length+"-----"+updcount);
		
		
		if(updcount>0 && updcount1>0 && updcount2>0)
		{
			txManager.commit(transact.status);
			addActionMessage("Primary Account Changed Successfully..."); 	
		}
		else
		{
			txManager.rollback(transact.status);
			addActionError("Unable to Continue in Change Primary Account process.."); 
		}
		}
	 	catch(Exception e)
		{
			txManager.rollback(transact.status);
			addActionError("Unable to Continue  in Change Primary Account process.."); 
			trace("Exception in chengeprim::"+e);
			e.printStackTrace();
		}
		
	 //----------------Audit Added By sardar on 11-12-15---------------//		
		for (int i = 0; i < accountnumber.length; i++) {
			System.out.println("-------------------start-------------------");
			System.out.println("accountnumber----"+accountnumber[i]+"primarysecondary::"+primarysecondary[i]);
			
			
		if(primarysecondary[i].equals("P"))
		{ primary_acct_no=accountnumber[i];
			auditmsg="Primary Account is Changed for the Card No [ "+mcard_no+" ] And the primay Account No is [ "+primary_acct_no+" ]";
		}
		else
		{
			auditmsg="Primary Account is Changed for the Card No [ "+mcard_no+" ] And the Secondary Account No  is [ "+accountnumber[i]+" ]";
		}
			
		try{ 
		 
			//added by gowtham_220719
			trace("ip address======>  "+ip);
			auditbean.setIpAdress(ip);
 
			auditbean.setActmsg(auditmsg);
			auditbean.setUsercode(username);
			auditbean.setAuditactcode("9091"); 
			auditbean.setCardno(mcard_no);
			auditbean.setRemarks(remarks);
			
			auditbean.setProduct(prod_code); 
			auditbean.setSubproduct(sub_prod_id);
			auditbean.setCardcollectbranch(br_code);
			auditbean.setCustname(emb_name);
			auditbean.setCin(cust_id);
			auditbean.setBin(prod_code.substring(0,8));
			auditbean.setAccoutnno(accountnumber[i]);
			auditbean.setActiontype("IM");
			auditbean.setApplicationid(orderrefno);
			
	 		commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
		 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage());
		 }
	 	
		}
		
		//----------------End By sardar on 11-12-15---------------//	
		
	 	return "chnageCbsPrimaryHome";
	}
	
	
	private String updatePrimary(String instid,String chn, String accountnumber, String primarysecondary,String usercode,JdbcTemplate jdbctemplate) {
		
		StringBuilder cpdcntqry = new StringBuilder();
		
		String acctpriority = "";
		int secacct= 1;
		if("S".equalsIgnoreCase(primarysecondary)){
			
			/*String acctpriorityqry = "SELECT ACCOUNTPRIORITY FROM EZAUTHREL WHERE ACCOUNTFLAG='S' AND ACCOUNTNO = '"+accountnumber+"' AND CHN='"+chn+"' AND INSTID='"+instid+"' ";
			List<Map<String,Object>> acctprioritylist = jdbctemplate.queryForList(acctpriorityqry);
			if(!acctprioritylist.isEmpty()){
				//String acctpriorityval = (String) acctprioritylist.get(0).get("ACCOUNTPRIORITY");
				//if(acctpriorityval == null){
					acctpriority = "(SELECT MAX(ACCOUNTPRIORITY)+1 FROM EZAUTHREL WHERE ACCOUNTFLAG='S' AND CHN = '"+cardno+"')";
				//}
			}else{
				acctpriority = "'2'";
			}*/
			secacct = secacct + 1;
			acctpriority = String.valueOf(secacct);
		}else{
			acctpriority = "'1'";
		}
		
		cpdcntqry.append("UPDATE EZAUTHREL SET ACCOUNTFLAG='"+primarysecondary+"',ACCOUNTPRIORITY="+acctpriority+" WHERE ACCOUNTNO = '"+accountnumber+"' AND CHN='"+chn+"' AND INSTID='"+instid+"' ");
		enctrace("cpdcnt::"+cpdcntqry.toString());  
		
		return cpdcntqry.toString();
	}
	public int validateActvatedCard(String instid,String padssenable,String cardno,JdbcTemplate jdbcTemplate)
	{
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
		/*if(padssenable.equals("Y")){*/
		
		/*valdcardqry.append("select count(1) from CARD_PRODUCTION where CARD_STATUS='05' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE Hcard_no='"+cardno+"') AND INST_ID='"+instid+"' ");
		*/
		
		
		///by gowtham-300819
				valdcardqry.append("select count(1) from CARD_PRODUCTION where CARD_STATUS='05' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO "
						+ "FROM CARD_PRODUCTION_HASH WHERE Hcard_no=?) AND INST_ID=? ");
				
		
		
		
		/*}
		else{
			valdcardqry.append("select count(1) from CARD_PRODUCTION where card_no in (select chn from EZCARDINFO ) and CARD_STATUS='05' AND card_no='"+cardno+"' AND INST_ID='"+instid+"' ");	
		}*/
		/*enctrace("valdcardqry::"+valdcardqry.toString());      
		validcard = jdbcTemplate.queryForInt(valdcardqry.toString());*/
				
				///by gowtham-300819
				enctrace("valdcardqry::"+valdcardqry.toString());      
				validcard = jdbcTemplate.queryForInt(valdcardqry.toString(),new Object[]{cardno,instid});
				
		return validcard;
	}
	
	public List getAccounttypeinfo( String instid,String padssenable, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		List cstdetlist = null;
		StringBuilder acctqry = new StringBuilder();
		acctqry.append("SELECT A.INSTID, A.ACCOUNTNO, ");
		acctqry.append("A.ACCOUNTTYPE,  ");
		acctqry.append("ACCOUNTTYPEDESC,  ");
		acctqry.append("PRODUCTCODE,  ");
		acctqry.append("PRODUCTCODEDESC, A.CURRCODE, CURRCODEDESC, LIMITFLAG, "); 
		acctqry.append("STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE,  DECODE(ACCOUNTFLAG,'P','Primary','S','Secondary') ACCOUNTFLAG, ACCOUNTFLAG ACCOUNTFLAG_FLAG FROM ( ");
		acctqry.append("SELECT INSTID, ACCOUNTNO, ACCOUNTTYPE,  ");
		acctqry.append("(SELECT ACCTTYPEDESC FROM ACCTTYPE WHERE ACCTTYPEID=ACCOUNTTYPE AND INSTID=INST_ID) ACCOUNTTYPEDESC, ");
		acctqry.append("PRODUCTCODE, (SELECT ACCTSUBTYPEDESC FROM ACCTSUBTYPE WHERE ACCTSUBTYPEID=trim(PRODUCTCODE) AND INSTID=INST_ID) PRODUCTCODEDESC, "); 
		acctqry.append("CURRCODE,  (SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE=CURRCODE ) CURRCODEDESC, AVAILBAL,  ");
		acctqry.append("LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE ");
		acctqry.append("FROM EZACCOUNTINFO  WHERE ACCOUNTNO IN (SELECT ACCOUNTNO FROM EZAUTHREL  ");  
		
		/*acctqry.append("WHERE CHN='"+cardno+"' AND INSTID='"+instid+"')) A, EZAUTHREL B ");
		acctqry.append("WHERE A.ACCOUNTNO = B.ACCOUNTNO AND A.INSTID = B.INSTID AND B.CHN='"+cardno+"' AND ROWNUM<=6 ORDER BY ACCOUNTFLAG");  
		enctrace("getAccounttypeinfo :" + acctqry.toString() );
		cstdetlist = jdbctemplate.queryForList(acctqry.toString());  */
		
		
		///by gowtham-300819
				acctqry.append("WHERE CHN=? AND INSTID=?)) A, EZAUTHREL B ");
				acctqry.append("WHERE A.ACCOUNTNO = B.ACCOUNTNO AND A.INSTID = B.INSTID AND B.CHN=? AND ROWNUM<=? ORDER BY ACCOUNTFLAG,ACCOUNTPRIORITY");  
				enctrace("getAccounttypeinfo :" + acctqry.toString() );
				cstdetlist = jdbctemplate.queryForList(acctqry.toString(),new Object[]{cardno,instid,cardno,"6"});  
				
		
		return cstdetlist ;     
	}
	
	
	
	
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public List getAccounttypedetails() {
		return accounttypedetails;
	}
	public void setAccounttypedetails(List accounttypedetails) {
		this.accounttypedetails = accounttypedetails;
	}
	
	public String custname;
	
	  
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	public String getCustomerNameByCard(String instid, String cardno,String padssenable, JdbcTemplate jdbctemplate ) throws Exception {
		String customername = null;
		String customerid = null;
		String cardcon="";
		
		//if(padssenable.equals("Y")){cardcon="HCARD_NO";}else{cardcon="CARD_NO";};
		
		try {
			/*String customernameqry = "SELECT CIN FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' and ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + cardno + "')";
			enctrace("3030customernameqry :" + customernameqry);
			customerid = (String)jdbctemplate.queryForObject(customernameqry, String.class);*/
			
			///by gowtham-300819
			String customernameqry = "SELECT CIN FROM CARD_PRODUCTION WHERE INST_ID=? and "
					+ "ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? )";
					enctrace("3030customernameqry :" + customernameqry);
					customerid = (String)jdbctemplate.queryForObject(customernameqry, new Object[]{instid,cardno},String.class);
			
			
			
			customername = commondesc.fchCustName(instid, customerid, jdbctemplate);
		} catch (Exception e) {}
		return customername;
	}
	

}  
