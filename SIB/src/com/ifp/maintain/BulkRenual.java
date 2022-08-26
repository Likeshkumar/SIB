package com.ifp.maintain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.dao.CardMaintainActionDAO;
import com.ifp.personalize.PersionalizedcardCondition;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Validation;

public class BulkRenual extends BaseAction{

	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}

	public List getBulkrenualList() {
		return bulkrenualList;
	}

	public void setBulkrenualList(List bulkrenualList) {
		this.bulkrenualList = bulkrenualList;
	}

	private List branchlist;
	private List personalproductlist;
	private List bulkrenualList;
	private String cardgentype;
	private String productcode;
	private String collectbranch;
	
	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public String getCardgentype() {
		return cardgentype;
	}

	public void setCardgentype(String cardgentype) {
		this.cardgentype = cardgentype;
	}

	private String padssenabled;
	public String getPadssenabled() {
		return padssenabled;
	}

	public void setPadssenabled(String padssenabled) {
		this.padssenabled = padssenabled;
	}

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
	
	PersionalizedcardCondition cardcond = new PersionalizedcardCondition();
	 
	public String BulkRenualHome() throws Exception
	{
		System.out.println("BulkRenualHome method called");
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId(session);
		
		String renewalperiods = this.getRenewalPeriods(instid, jdbctemplate);
		List branchlist = cardcond.getBranchCodeForRenual(instid,renewalperiods, jdbctemplate);
		
		setBranchlist(branchlist);
		
		List pers_prodlist=commondesc.getProductList(instid,jdbctemplate, session);
		if (!(pers_prodlist.isEmpty())){
			setPersonalproductlist(pers_prodlist);
		} else{
			System.out.println("No Product Details Found ");
			
			//setCardgenstatus('N');
		}     
		   
		System.out.println("---------------------bulkrenual_home");
		return "bulkrenual_home";
	}
	
	public String singleRenewalHome() throws Exception{
		return "singlerenewal";
	}
	
	/*public String singleRenewal() throws Exception{
		System.out.println("Single Renewal Home method called");
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId(session);
		String renewalperiods = this.getRenewalPeriods(instid, jdbctemplate);
		List branchlist = cardcond.getBranchCodeForSingleRenewal(instid,renewalperiods, jdbctemplate);
		setBranchlist(branchlist);
		
		List pers_prodlist=commondesc.getProductList(instid,jdbctemplate, session);
		if (!(pers_prodlist.isEmpty())){
			setPersonalproductlist(pers_prodlist);
		} else{
			System.out.println("No Product Details Found ");
		}  
		
		System.out.println("---------------------singlerenewalhome");
		return "singlerenewalhome";
	}*/
	
	
	public String singleRenewal() 
	{
		System.out.println("return page called");
		return "singlerenewalhome";
	}
	
	
	private List<Map<String, Object>> checkCardNo(String instid,StringBuffer newhcardno, JdbcTemplate jdbctemplate2) {
		
		/*String qry = "SELECT COUNT(1) as CNT FROM CARD_PRODUCTION WHERE HCARD_NO='"+newhcardno+"' AND INST_ID='"+instid+"'";
		List list = jdbctemplate2.queryForList(qry);*/
		
		
	////by gowtham-300819
			String qry = "SELECT COUNT(1) as CNT FROM CARD_PRODUCTION WHERE HCARD_NO=? AND INST_ID=? ";
			List list = jdbctemplate2.queryForList(qry,new Object[]{newhcardno,instid});
			
		
		return list;
	}

	/*public String changeCardRenual() throws Exception
	{
		String caf_recstatus = "";
		String card_status = "";
		String mkck_status = "P";
		System.out.println( "card status changing as damage");    
		
		System.out.println("changeExpiryDate::::::::::"+getRequest().getParameter("changeExpiryDate"));
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String cardgentype  = getRequest().getParameter("cardgentype")==null?"":getRequest().getParameter("cardgentype");
		String productcode = getRequest().getParameter("productcode");
		IfpTransObj transact = commondesc.myTranObject("BULKRENUAL", txManager);
		
		String padssenable =commondesc.checkPadssEnable(instid, jdbctemplate);
		String encchn="";        
		if(padssenable.equals("Y"))
		{
			
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
		}
		
		CardMaintainActionDAO cardmaintain = new CardMaintainActionDAO();
		PadssSecurity sec = new PadssSecurity();
		String authstatus = "";
		String statusmsg = "";
		String err_msg="";
		String waitingmsg ="";
		String remarks = getRequest().getParameter("reason");
		setCollectbranch(getRequest().getParameter("collectbranch"));
		String branchattch = commondesc.checkBranchattached(instid, jdbctemplate);
		
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		System.out.println("Total Orders Selected ===> "+order_refnum.length);
		if (  getRequest().getParameter("authorize") != null ){
			System.out.println( "AUTHORIZE..........." );
			authstatus = "P";
			statusmsg = " Renewed Successfully and Waiting For Security Data Generation ...";
			err_msg="Authorize";
		}else  if (  getRequest().getParameter("deauthorize") != null  ){
			System.out.println( "DE AUTHORIZE..........." );
			authstatus = "D";
			statusmsg = " De-Authorized Successfully ..";
			err_msg="De-Authorize";
		}
		String changeexpreq = "",expperiod="",subprodid=""; int moveprocess =0;int moveprocessCnt =0;int cafrecstatusupCnt=0;
		String newcardno = "",orderrefno="",newmaskcardno="";
		String keyid = "";String sequncenumber = "";long sequn_no = 0;
		String EDMK="", EDPK="",newenccardno="";
		StringBuffer newhcardno = new StringBuffer();
		
		String username=comUsername();
		Personalizeorderdetails personlizeorderdetails=null;
			int cnt=0;String accountno = "",mcardno="",orgCardno="";
			for(int i=0;i<order_refnum.length;i++)
			{
				if(padssenable.equals("Y"))
				{
					subprodid = commondesc.getSubProductByHCHN(instid, order_refnum[i], jdbctemplate, "PROD");
				}else
				{
					subprodid = commondesc.getSubProductByCHN(instid, order_refnum[i], jdbctemplate, "PROD");
				}
				
				expperiod = commondesc.getSubProductExpPeriod(instid, subprodid, jdbctemplate);
				System.out.println("cardno:::"+order_refnum[i]);    
				if(cardgentype.equals("EXISTCARD") )
				{
							caf_recstatus = "BR";
							waitingmsg=" Waiting For Security Data Generation";
							card_status = "01";
							changeexpreq = "N";
							
			//---------------------------------------Audit Trail is Edited By sardar on 11-12-15 	//			
							try{ 
								mcardno = commondesc.getMaskedCardNo(instid, order_refnum[i],"C", jdbctemplate);
								//if(mcardno==null){mcardno=order_refnum[j];}
								auditbean.setActmsg("card renual for  "+order_refnum[i]  + " is "+statusmsg+"  [ "+mcardno+" ]");
								auditbean.setUsercode(username);
								auditbean.setAuditactcode("0102"); 
								auditbean.setCardno(mcardno);
								auditbean.setRemarks(remarks);
							//	auditbean.setProduct(persorderdetails.product_code);
								//commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
								commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
							 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
							
				}			
				
				
				//---------------------------------------Audit Trail is End By sardar on 11-12-15 	//		
				
				
				else   
				{
					
				
					
					changeexpreq = "Y";              
					caf_recstatus = "BN";          
					waitingmsg=" Waiting For Security Data Generation";
					card_status = "01";
					
					
				}
			
			try{
			if(cardgentype.equals("EXISTCARD") )
			{ 																			
				moveprocess = this.moveCardToProcess(instid,padssenable,accountno,order_refnum[i],mcardno, orgCardno, order_refnum[i], caf_recstatus, card_status,expperiod,changeexpreq,  "CARD_PRODUCTION",newcardno,collectbranch, jdbctemplate);
				moveprocessCnt = moveprocessCnt+moveprocess;   
			}
			else
			{				    																			
				moveprocess = this.moveCardToProcess(instid,padssenable,accountno,order_refnum[i],newmaskcardno,newenccardno, newhcardno.toString(),  caf_recstatus, card_status,expperiod,changeexpreq,  "CARD_PRODUCTION",newcardno,collectbranch, jdbctemplate);
				moveprocessCnt = moveprocessCnt+moveprocess;   
				       
				
			}
				
			}catch(Exception e)
			{
				trace("Exception in inserting .."+e);
				addActionError("Unable to Continue");
				e.printStackTrace();
				return "required_home";     
			}
			
			}         
			
			System.out.println("-------"+moveprocessCnt+"-------"+order_refnum.length);
			
			
			
			if( moveprocessCnt == order_refnum.length ){
				
				for(int i=0;i<order_refnum.length;i++)
				{
				     
					System.err.println("hcardno:::"+order_refnum[i]);        
					
					int x  = jdbctemplate.update("UPDATE CARD_PRODUCTION SET CAF_REC_STATUS='"+caf_recstatus+"',STATUS_CODE='78',CARD_STATUS='12' WHERE HCARD_NO='"+order_refnum[i]+"' and INST_ID = '"+instid+"'");
							 jdbctemplate.update("UPDATE EZCARDINFO SET STATUS='78' WHERE CHN='"+order_refnum[i]+"'");
					cafrecstatusupCnt = cafrecstatusupCnt+x;
				}
				if(cafrecstatusupCnt==order_refnum.length){   
					
				txManager.commit(transact.status);
				addActionMessage(order_refnum.length +" Card(s) "+ statusmsg); 
				return "required_home";         
				}else
				{
					txManager.rollback(transact.status);
					addActionError("Could not move the production records to process"); 
					return "bulkrenual_home"; 
				}
			}
			else{
			txManager.rollback(transact.status);
			addActionError("Could not move the production records to process"); 
			return "bulkrenual_home"; 
			}
		
		int movepinprocess = this.movePintoProcess( instid, cardno, cardno, session);				
		if( movepinprocess != 1 ){
			txManager.rollback(transact.status);
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg"," Could not move the Pin records to process"); 
			return "serach_home"; 
		}				
		      
		
			//return "changeCardRenual";
	}*/
	
	
// BY SIVA 11-07-2019
	
	
	
	public String changeCardRenual() throws Exception 
	{
		String caf_recstatus = "";
		String card_status = "";
		String mkck_status = "P";
		System.out.println("card status changing as damage");
		System.out.println("changeExpiryDate::::::::::" + getRequest().getParameter("changeExpiryDate"));
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String cardgentype = getRequest().getParameter("cardgentype") == null ? "": getRequest().getParameter("cardgentype");
		String productcode = getRequest().getParameter("productcode");
		IfpTransObj transact = commondesc.myTranObject("BULKRENUAL", txManager);
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		String encchn = "";
		
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");

		
		if (padssenable.equals("Y")) 
		{
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			//System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
		}
		CardMaintainActionDAO cardmaintain = new CardMaintainActionDAO();
		PadssSecurity sec = new PadssSecurity();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		String waitingmsg = "";
		String remarks = getRequest().getParameter("reason");
		setCollectbranch(getRequest().getParameter("collectbranch"));
		String branchattch = commondesc.checkBranchattached(instid, jdbctemplate);
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		
		String org_chn = getRequest().getParameter("org_chn");
		String hcard_no = getRequest().getParameter("hcard_no");
		String mcard_no = getRequest().getParameter("mcard_no");
		
		System.out.println("org_chn number checking---> "+org_chn);
		System.out.println("hcard_no number checking---> "+hcard_no);
		System.out.println("mcard_no number checking---> "+mcard_no);		
		System.out.println("Total Orders Selected ===> " + order_refnum.length);
		
		if (getRequest().getParameter("authorize") != null) 
		{
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			statusmsg = " Renewed Successfully and Waiting For Security Data Generation ...";
			err_msg = "Authorize";
		} 
		else if (getRequest().getParameter("deauthorize") != null) 
		{
			System.out.println("DE AUTHORIZE...........");
			authstatus = "D";
			statusmsg = " De-Authorized Successfully ..";
			err_msg = "De-Authorize";
		}
		String changeexpreq = "", expperiod = "", subprodid = "";
		int moveprocess = 0;
		int moveprocessCnt = 0;
		int cafrecstatusupCnt = 0;
		String newcardno = "", orderrefno = "", newmaskcardno = "";
		String keyid = "";
		String sequncenumber = "";
		long sequn_no = 0;
		String EDMK = "", EDPK = "", newenccardno = "";
		StringBuffer newhcardno = new StringBuffer();
		String username = comUsername();
		Personalizeorderdetails personlizeorderdetails = null;
		int cnt = 0;
		String accountno = "", mcardno = "", orgCardno = "";
		for (int i = 0; i < order_refnum.length; i++)
		{
			
			/*
			 * if(padssenable.equals("Y")) { subprodid =
			 * commondesc.getSubProductByHCHN(instid, order_refnum[i],
			 * jdbctemplate, "PROD"); }else { subprodid =
			 * commondesc.getSubProductByCHN(instid, order_refnum[i],
			 * jdbctemplate, "PROD"); }
			 */
			
			System.out.println("card number check--?>" + order_refnum[i]);			
			subprodid = commondesc.getSubProductByCHN11(instid, order_refnum[i], jdbctemplate, "PROD");
			System.out.println("subprodid number check--?>" + subprodid);
			
			expperiod = commondesc.getSubProductExpPeriod(instid, subprodid, jdbctemplate);
			System.out.println("expperiod:::" + expperiod);
			System.out.println("cardgentype--->"+cardgentype);
			if (cardgentype.equals("EXISTCARD")) 
			{
				caf_recstatus = "BR";
				waitingmsg = " Waiting For Security Data Generation";
				card_status = "01";
				changeexpreq = "N";

				// ---------------------------------------Audit Trail is Edited
				// By sardar on 11-12-15 //
				try {
					
					//added by gowtham_220719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);
					
					//mcardno = commondesc.getMaskedCardNo(instid, cardno, "C", jdbctemplate);
					mcardno=mcard_no;
					// if(mcardno==null){mcardno=order_refnum[j];}
					auditbean.setActmsg(
							"card renual for  " + order_refnum[i] + " is " + statusmsg + "  [ " + mcardno + " ]");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0102");
					auditbean.setCardno(mcardno);
					auditbean.setRemarks(remarks);
					// auditbean.setProduct(persorderdetails.product_code);
					// commondesc.insertAuditTrail(instid, userid, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}

			}

			// ---------------------------------------Audit Trail is End By
			// sardar on 11-12-15 //

			else 
			{
				changeexpreq = "Y";
				caf_recstatus = "BN";
				waitingmsg = " Waiting For Security Data Generation";
				card_status = "01";
			}

			try 
			{
				if (cardgentype.equals("EXISTCARD"))
				{
					moveprocess = this.moveCardToProcess(instid, padssenable, accountno, org_chn, mcardno,orgCardno, org_chn, caf_recstatus, card_status, expperiod, changeexpreq,"CARD_PRODUCTION", newcardno, collectbranch, jdbctemplate);
					moveprocessCnt = moveprocessCnt + moveprocess;
				}			
				else 
				{
					String orderrefno1 = commondesc.generateorderRefno(instid, jdbctemplate);
					trace("Generated order reference number is : " + orderrefno1);
					
					moveprocess = this.moveCardToProcess1(hcard_no,orderrefno1,instid, org_chn, mcard_no,order_refnum[i], caf_recstatus, card_status, expperiod, changeexpreq,"CARD_PRODUCTION", collectbranch, jdbctemplate);
					moveprocessCnt = moveprocessCnt + moveprocess;
				}

			} catch (Exception e)
			{
				trace("Exception in inserting .." + e);
				addActionError("Unable to Continue");
				e.printStackTrace();
				return "required_home";
			}
		}
		System.out.println("-------" + moveprocessCnt + "-------" + order_refnum.length);
		if (moveprocessCnt == order_refnum.length)
		{
			for (int i = 0; i < order_refnum.length; i++) {
				System.err.println("hcardno:::" + order_refnum[i]);
				int x = jdbctemplate.update("UPDATE CARD_PRODUCTION SET CAF_REC_STATUS='" + caf_recstatus+ "',STATUS_CODE='78',CARD_STATUS='12' WHERE ORDER_REF_NO=(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+ order_refnum[i] + "' and INST_ID = '" + instid + "') and INST_ID = '" + instid + "'");
				jdbctemplate.update("UPDATE EZCARDINFO SET STATUS='78' WHERE CHN='" + order_refnum[i] + "'");
				cafrecstatusupCnt = cafrecstatusupCnt + x;
			}
			if (cafrecstatusupCnt == order_refnum.length) 
			{
				txManager.commit(transact.status);
				addActionMessage(order_refnum.length + " Card(s) " + statusmsg);
				return "required_home";
			} 
			else 
			{
				txManager.rollback(transact.status);
				addActionError("Could not move the production records to process");
				return "bulkrenual_home";
			}
		} 
		else 
		{
			txManager.rollback(transact.status);
			addActionError("Could not move the production records to process");
			return "bulkrenual_home";
		}
	}	
	
// BY SIVA 11-07-2019	
	
/*public int moveCardToProcess( String instid,String padssenable,String accountno,String hahedcardno,String mcardno, String cardno, String newcardno, String caf_recstatus, String card_status,String expperiod,String changeexpreq, String TABLENAME, String newcardno2, String collectbranch, JdbcTemplate jdbctemplate ) {
		
		
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
		
		String query="";         
		
		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH)";
		
		query += " SELECT INST_ID,CARD_NO,HCARD_NO, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, '"+card_status+"', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "SYSDATE, "+expreqcond+", PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '"+caf_recstatus+"', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH FROM "+TABLENAME+"  WHERE INST_ID='"+instid+"' AND "+padsscon+"";
		       
		enctrace( "moveCardToProcess : " + query ); 
	      
		int x  = jdbctemplate.update(query);
		return x;  
}*/

	public int moveCardToProcess(String instid, String padssenable, String accountno, String hahedcardno,
			String mcardno, String cardno, String newcardno, String caf_recstatus, String card_status, String expperiod,
			String changeexpreq, String TABLENAME, String newcardno2, String collectbranch, JdbcTemplate jdbctemplate) {

		String padsscon = "";
		String expreqcond = "";
		/*
		 * if(padssenable.equals("Y")){ padsscon = "HCARD_NO = '"
		 * +hahedcardno+"'"; }else{padsscon="CARD_NO ='"+cardno+"'";}
		 */
		System.out.println("hahedcardno----->"+hahedcardno);
		System.out.println("mcardno----->"+mcardno);
		//System.out.println("cardno----->"+newcardno);
		//System.out.println("newcardno----->"+newcardno);
		//System.out.println("newcardno2----->"+newcardno2);
		System.out.println("changeexpreq----->"+changeexpreq);
		
		padsscon = "ORG_CHN ='" + newcardno + "'";
		if (changeexpreq.equals("Y")) 
		{
			expreqcond = "add_months(sysdate," + expperiod + ")";
		} 
		else 
		{
			expreqcond = "EXPIRY_DATE";
		}
String order_ref_no1="SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN='"+newcardno+"' ";
enctrace("order_ref_no1---->"+order_ref_no1);
String order_ref_no=(String) jdbctemplate.queryForObject(order_ref_no1, String.class);
int result=0;
String query = "";
		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH)";

		query += " SELECT INST_ID, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, '"
				+ card_status + "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "SYSDATE, " + expreqcond
				+ ", PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '" + caf_recstatus
				+ "', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH FROM " + TABLENAME + "  WHERE INST_ID='"
				+ instid + "' AND " + padsscon + "";
		enctrace("moveCardToProcess : " + query);
		int x = jdbctemplate.update(query);
		

		
		String query1 = "";
		query1 = "INSERT INTO  PERS_CARD_PROCESS_HASH (";
		query1 += "INST_ID,HCARD_NO,ACCT_NO, CIN, ORDER_REF_NO,  CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE,  ";
		query1 += "GENERATED_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ";
		query1 += "BIN)";
		query1 += " SELECT INST_ID,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE ORDER_REF_NO='"+order_ref_no+"' ), ACCOUNT_NO, CIN, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE,";
		query1 += "SYSDATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ";
		query1 += "BIN FROM " + TABLENAME + "  WHERE INST_ID='"+ instid + "' AND " + padsscon + "";
		enctrace("moveCardToProcess : " + query1);
		int x1 = jdbctemplate.update(query);
		
		if(x==1 && x1==1)
		{
			result =1;
		}
		return result;
	}
	
	
	
	public int moveCardToProcess1(String hcard,String order_ref_no2, String instid,String org_chn,
			String mcardno, String hcard_no, String caf_recstatus, String card_status, String expperiod,
			String changeexpreq, String TABLENAME,String collectbranch, JdbcTemplate jdbctemplate) {

		String padsscon = "";
		String expreqcond = "";
		/*
		 * if(padssenable.equals("Y")){ padsscon = "HCARD_NO = '"
		 * +hahedcardno+"'"; }else{padsscon="CARD_NO ='"+cardno+"'";}
		 */
		System.out.println("org_chn----->"+org_chn);
		System.out.println("mcardno----->"+mcardno);
		System.out.println("hcard_no----->"+hcard_no);
		System.out.println("hcard_no----->"+hcard);
		System.out.println("caf_recstatus----->"+caf_recstatus);
		System.out.println("card_status----->"+card_status);
		System.out.println("expperiod----->"+expperiod);
		
		//padsscon = "ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE ORG_CHN ='" + hcard_no + "')";
		
		if (changeexpreq.equals("Y")) 
		{
			expreqcond = "add_months(sysdate," + expperiod + ")";
		} 
		else 
		{
			expreqcond = "EXPIRY_DATE";
		}
/*String order_ref_no1="SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN ='" + org_chn + "') ";
enctrace("order_ref_no1---->"+order_ref_no1);
String order_ref_no=(String) jdbctemplate.queryForObject(order_ref_no1, String.class);*/

int result=0;
String query = "";
		query = "INSERT INTO  PERS_CARD_PROCESS (";
		query += "INST_ID, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
		query += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
		query += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH)";

		query += " SELECT INST_ID, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, '"+ order_ref_no2 + "', '"+ card_status + "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
		query += "SYSDATE, " + expreqcond+ ", PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
		query += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '" + caf_recstatus+ "', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
		query += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
		query += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,CARD_COLLECT_BRANCH FROM CARD_PRODUCTION WHERE INST_ID='"+ instid + "' AND ORG_CHN='"+org_chn+"'  ";
		enctrace("moveCardToProcess : " + query);
		int x = jdbctemplate.update(query);
		

		
		String query1 = "";
		query1 = "INSERT INTO  PERS_CARD_PROCESS_HASH (";
		query1 += "INST_ID,HCARD_NO,ACCT_NO, CIN,  ORDER_REF_NO,  CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE,  ";
		query1 += "GENERATED_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ";
		query1 += "BIN)";
		query1 += " SELECT INST_ID,'"+hcard_no+"', ACCOUNT_NO, CIN, '"+ order_ref_no2 + "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE,";
		query1 += "SYSDATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ";
		query1 += "BIN FROM CARD_PRODUCTION_HASH WHERE INST_ID='"+ instid + "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN='"+org_chn+"') ";
		enctrace("moveCardToProcess : " + query1);
		int x1 = jdbctemplate.update(query1);
		
		if(x==1 && x1==1)
		{
			result =1;
		}
		return result;
	}
	
	
	public String bulkrenualList() throws Exception
	{
		boolean check;
		int checkvalue;
		HttpSession session = getRequest().getSession();
		String searchtype = getRequest().getParameter("searchtype");
		System.out.println("chck selected type-->"+searchtype);
		
		if(searchtype==null)
		{
			System.out.println("Not selected properly");
			addActionError("Kindly Select Any One");
			return "required_home";
		}
		
		String cardgentype = getRequest().getParameter("cardgentype");
		String productcode = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");  
		String flag = getRequest().getParameter("flag");
		String dateflag ="ORDERED_DATE";
		String inst_id=comInstId(session);
		String cardno = null;
		String renewalperiods = this.getRenewalPeriods(inst_id, jdbctemplate);
		List bulkrenualList = null;
		StringBuilder sb=null;
		
		System.out.println("flag----->"+flag);
		System.out.println("productcode----->"+productcode);
		System.out.println("cardgentype----->"+cardgentype);
		
		try 
		{
				trace("Checking bulk renual List...");     
				setCardgentype(cardgentype);
				setProductcode(productcode);
				
				if("S".equalsIgnoreCase(flag))
				{
					trace("flag checking S inside");
					String cond = "";
					cardno = getRequest().getParameter("cardno");
					//System.out.println("cardno-->"+cardno);
					String acctno = getRequest().getParameter("acctno");
					System.out.println("acctno-->"+acctno);
					String custid = getRequest().getParameter("custid");
					System.out.println("custid-->"+custid);
				
					/*String phonno = getRequest().getParameter("phonno");
					trace("phonno-->"+phonno);*/
					
					
					
					
					if(!"".equalsIgnoreCase(cardno))
					{
						
						checkvalue=Validation.number(cardno);
						if(checkvalue==0)
						{
							addActionError("Enter Proper Card Number");
							return "required_home";
						}
						PadssSecurity sec = new PadssSecurity();
						String keyid = "";String sequncenumber = "";long sequn_no = 0;
						String EDMK="", EDPK="",newenccardno="";
						StringBuffer newhcardno = new StringBuffer();
						
						Properties props=getCommonDescProperty();
						 EDPK=props.getProperty("EDPK");
						 
						keyid = commondesc.getSecurityKeyid(inst_id, jdbctemplate);
						System.out.println("keyid::"+keyid);
						List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
						//System.out.println("secList::"+secList);  
						Iterator secitr = secList.iterator();
						
						if(!secList.isEmpty())
						{
								while(secitr.hasNext())
								{
									Map map = (Map) secitr.next(); 
									String CDMK = ((String)map.get("DMK"));
									//String eDPK = ((String)map.get("DPK"));
									String CDPK=sec.decryptDPK(CDMK, EDPK);					
									newhcardno = sec.getHashedValue(cardno+inst_id);
									trace("newhcardno number-->"+newhcardno);
									newenccardno = sec.getECHN(CDPK, cardno);
									trace("card number-->"+newenccardno);
								}      
						}
						cond = " ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO LIKE '%"+newhcardno+"%') ";
					}
					
					else if( !"".equalsIgnoreCase(acctno))
					{
						checkvalue=Validation.number(acctno);
						if(checkvalue==0)
						{
							addActionError("Enter Proper Account Number");
							return "required_home";
						}
						trace("acctno number-->"+acctno);
						cond = " ACCOUNT_NO LIKE '%"+acctno+"%'";
					}
					
					else if(!"".equalsIgnoreCase(custid))
					{
						checkvalue=Validation.number(custid);
						if(checkvalue==0)
						{
							addActionError("Enter Proper Customer Number");
							return "required_home";
						}
						trace("custid number-->"+custid);
						cond = " CIN LIKE '%"+custid+"%'";
					}
					else
					{
						System.out.println("enter the value ");
						addActionError("Enter The Value");
						return "required_home";
					}
					
					/*else if(!"".equalsIgnoreCase(phonno))
					{
						trace("phonno number-->"+phonno);
						cond = " MOBILENO LIKE '%"+phonno+"%'";
					}*/
					System.out.println("cond---->"+cond);
					bulkrenualList = cardcond.getSingleRenewalList(inst_id,cond,renewalperiods,jdbctemplate );
				}
				
				
				else
				{
					bulkrenualList = cardcond.getBulkForRenualList(inst_id,renewalperiods,jdbctemplate );
				}
				
				System.out.println("bulkrenualList===> "+bulkrenualList);
				
				if(!(bulkrenualList.isEmpty()))
				{
					setBulkrenualList(bulkrenualList);
				}
				
	
				cardno="0000000000000000";
				sb=new StringBuilder(cardno);
				sb.setLength(0);
				System.out.println("cardno of renewal ===>"+cardno);
	
		} 
		catch (Exception e) 
		{
			System.out.println("Exception--->"+e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Exception : Error while getting the cards....");
			trace("Error While Fetching The Orders To Card Genetaion "+e.getMessage());
		}finally{
			cardno=null;
			sb=null;}
	
		String padssenabled = commondesc.checkPadssEnable(inst_id, jdbctemplate);
		setPadssenabled(padssenabled);
		
		//System.out.println("cardno"+cardno);
		return "bulkrenualList";
	}
	
	
	public List getCArdListByCArdno(String instid,String cardno,String padssenable,  JdbcTemplate jdbctemplate ) throws Exception {
		List relationlist = null;
		String relationlistqry = "SELECT CARD_NO, HCARD_NO, MCARD_NO, ACCOUNT_NO, ACCTTYPE_ID, ACCTSUB_TYPE_ID from CARD_PRODUCTION  WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"'";
		enctrace("relationlistqry :"+ relationlistqry);
		relationlist= jdbctemplate.queryForList( relationlistqry );
		return relationlist;
	}      
	public String getRenewalPeriods(String instid,JdbcTemplate jdbcTemplate){
		String renewalqry ="SELECT RENEWAL_PERIODS FROM INSTITUTION WHERE INST_ID='"+instid+"'";
		enctrace("renewalqry::"+renewalqry);
		return((String)jdbcTemplate.queryForObject(renewalqry, String.class));
	}

	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}

	public String getCollectbranch() {
		return collectbranch;
	}

	public void setCollectbranch(String collectbranch) {
		this.collectbranch = collectbranch;
	}      
}
         