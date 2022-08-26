package com.ifp.instant;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Formatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;
import com.lowagie.text.pdf.codec.Base64.InputStream;


/**
 * SRNP0004
 * @author CGSPL
 *
 */
public class InstReceiveEProcess extends BaseAction 
{ 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8376161637970676446L;
	   
	private static Boolean  initmail = true; 
	private static  String parentid = "000";
 
	CommonUtil comutil= new CommonUtil(); 
	CommonDesc commondesc = new CommonDesc();
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
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
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}
	
	
	public String comUserId(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	
	AuditBeans auditbean = new AuditBeans();
	
	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
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

	List instorderlist;
	public List getInstorderlist() {
		return instorderlist;
	}

	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	}
	
	List <String>prenamelist;
	
	public List<String> getPrenamelist() {
		return prenamelist;
	}

	public void setPrenamelist(List<String> prenamelist) {
		this.prenamelist = prenamelist;
	}

	public String comUsername(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
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
	
	public String reciveInsCardHome() { 
		String instid =  comInstId(); 
		HttpSession session = getRequest().getSession(); 
		 
		try{
			int reqch= commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if( reqch < 0 ){
				return "required_home";
			} 
			
			String act = getRequest().getParameter("act");
			if( act != null ){
				session.setAttribute("act", act);
			}
			
			branchlist =  this.commondesc.generateBranchList(instid, jdbctemplate );
			setBranchlist(branchlist);
			
			prodlist = commondesc.getProductList( instid, jdbctemplate, session ); 
			
			setProdlist(prodlist); 
			
			/*** MAIL BLOCK ****/
			System.out.println( "initmail--" + initmail +" parentid :  " + this.parentid );  
			if( initmail ){
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId( req, jdbctemplate ); 
				if( !menuid.equals("NOREC")){
					System.out.println( "parentid--"+menuid); 
					this.parentid = menuid;
				}else{ 
					this.parentid = "000";
				}
				initmail = false;
			} 
			/*** MAIL BLOCK ****/
			
			
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			e.printStackTrace();
			trace("Exception: Instant recive card : " + e.getMessage() );
			return "required_home";
		}
		return "insrecvcard_home"; 
	}
	
	public String reciveInsCardList(){
		trace("Instant card recieve list.....");
		enctrace("Instant card recieve list.....");
		String instid = comInstId();
		String bin = getRequest().getParameter("cardtype");
		String branch = getRequest().getParameter("branchcode");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate"); 
				
		HttpSession session = getRequest().getSession(); 
		String card_status = "03";
		String mkckstatus = "P"; 
		String datefld =""; 
		datefld = "PRE_DATE"; 
		
		List waitingforcardpin;
		String filtercond = commondesc.filterCondition(bin,branch,fromdate,todate,datefld);
		
		
		try {
			waitingforcardpin = this.commondesc.waitingForInstCardRecvList(instid, card_status,  mkckstatus, filtercond, jdbctemplate); 
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " ); 
				trace("No Records found...");
				return this.reciveInsCardHome();
			}
			
			setInstorderlist(waitingforcardpin); 
			
			 
		} catch (Exception e) {
			trace( "Error whie view recive cards " + e);
			e.printStackTrace();
		} 
		
		return "insrecvcard_list";
	}
	
	public String reciveInsCardAction() {
		trace("*********Recieve card begins ********* ");
		enctrace("*********Recieve card begins ********* ");
		HttpSession session = getRequest().getSession();		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		
		String instid =  comInstId(); 
		String userid =comUserId();
		String usercode = comUserCode();
		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		String[] cardlist = getRequest().getParameterValues("instorderrefnum");
		String table="INST_CARD_PROCESS";
		
		if( cardlist == null ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",   "NO CARDS SELECTED. SELECT ANY CARD AND TRY AGAIN" );   
			return this.reciveInsCardHome();
		}
		
		String makerid = "",checkerid="",makerdate="",checkerdate="", mkckflag="", ckdate="";
		String act = (String)session.getAttribute("act") ;
		
		if ( act.equals("M")){ 
			 makerid = usercode; 
		     mkckflag = "M";
			ckdate = commondesc.default_date_query;
			makerdate = "SYSDATE";
		}else {  // D				 
				makerid = usercode;
				checkerid = makerid;
				mkckflag = "P";
				ckdate = "sysdate";
				makerdate = "SYSDATE";
		}
		
		
		try {
			int cnt = 0;
			trace("Selected card count : " + cardlist.length );
			for ( int i=0; i<cardlist.length; i++ ){
				trace("Updating the process status for the card [ "+cardlist[i]+" ] ");
				String cardauthqry = "UPDATE INST_CARD_PROCESS SET  MKCK_STATUS='"+mkckflag+"', CARD_STATUS='04',  RECV_DATE=sysdate, CHECKER_DATE="+ckdate+" WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardlist[i]+"'"; 
				enctrace( "card recv qry  : " + cardauthqry );
				int x = commondesc.executeTransaction(cardauthqry, jdbctemplate );
				trace("Got : "  + x );
				cnt++;
				
				
				//---------------audit code edited by sardar on 11-12-15---------//	
				try{ 
					
					String mcardno = commondesc.getMaskedCardbyproc(instid, cardlist[i],table,"C", jdbctemplate);
					if(mcardno==null){mcardno=cardlist[i];}
					auditbean.setActmsg("Recieved Card  [ "+mcardno+" ] ");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0205");  
					auditbean.setCardno(mcardno);
					
					//auditbean.setCardnumber(order_refnum[i]);
					//commondesc.insertAuditTrail(in_name, Maker_id, auditbean, jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage() ); }
				 								
				//--------------End on 11-12-15-------------//
			}
			
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", cnt + " Cards Received successfully" );   
			txManager.commit( status );
			trace( cnt + " Cards Received successfully..got committed..." );
			
			/*** MAIL BLOCK ****/
			/*IfpTransObj transactmail = commondesc.myTranObject(); 
			try {
				String alertid = this.parentid; 
				if( alertid != null && ! alertid.equals("000")){
					String keymsg =  cnt + " Instant Cards Received successfully" ;
					int mail = comutil.sendMail( instid, alertid, keymsg, jdbctemplate, session, getMailSender() );
					System.out.println( "mail return__" + mail);
				} 
			} catch (Exception e) {  e.printStackTrace(); }
			  finally{
				transactmail.txManager.commit(transactmail.status);
				System.out.println( "mail commit successfully");
			} */
			/*** MAIL BLOCK ****/
			
			
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  "Exception: Unable to continue the recieve process...");   
			txManager.rollback( status );
			trace("Exception : While recieving the cards : " + e.getMessage() );
		} 
		return this.reciveInsCardHome();
		 
	}
	
	public String reciveInsCardAuthHome(){
		String instid =  comInstId(); 
		trace("recive inst card auth home");
		enctrace("recive inst card auth home");
		HttpSession session = getRequest().getSession(); 
		 
		try{
			int reqch= commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if( reqch < 0 ){
				return "required_home";
			}
			
			String act = getRequest().getParameter("act");
			if( act != null ){
				session.setAttribute("act", act);
			}
			trace("Getting brach list.....");
			branchlist =  this.commondesc.generateBranchList(instid, jdbctemplate );
			setBranchlist(branchlist);
			
			trace("Getting product list.....");
			prodlist = commondesc.getProductList( instid, jdbctemplate, session ); 
			setProdlist(prodlist); 
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			trace("Exception : While Recieve instant card authorization " + e.getMessage());
			e.printStackTrace();
			return "required_home";
		}
		return "insrecvcardauth_home"; 
	}
	
	public String reciveInsAuthList(){
		trace( "Recieve Authorization list....");
		enctrace( "Recieve Authorization list....");
		
		String instid = comInstId();
		String bin = getRequest().getParameter("cardtype");
		String branch = getRequest().getParameter("branchcode");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");		
		HttpSession session = getRequest().getSession(); 
		String card_status = "04";
		String mkckstatus = "M"; 
		String datefld =""; 
		datefld = "PRE_DATE"; 
		
		List waitingforcardpin;
		String filtercond = commondesc.filterCondition(bin,branch,fromdate,todate,datefld);
				
		try {
			trace("Getting list of cards.....");
			waitingforcardpin = this.commondesc.waitingForInstCardProcess(instid, card_status,  mkckstatus, filtercond, jdbctemplate);  
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " );
				trace("No records found...");
				return this.reciveInsCardHome();
			}
			
			setInstorderlist(waitingforcardpin); 
			
			 
		} catch (Exception e) {
			trace( "Error whie view recive auth cards " + e.getMessage() );
			e.printStackTrace();
		}  
		
		return "insrecvcardauth_list"; 
	}
	
	public String authorizeInsRecvCards() {
		trace("Authourize instant receive cards....");
		enctrace("Authourize instant receive cards....");
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		String instid = comInstId();
		String username = comUsername();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		HttpSession session = getRequest().getSession();
		String statusmsg ="Recived Card Authorized";
		String table="INST_CARD_PROCESS";
		try {
			int cnt = 0;
			trace("Got the order count : "  + order_refnum.length);
			for ( int i=0; i<order_refnum.length; i++ ){
				trace("Updating the status for the order [ "+order_refnum[i]+" ] ");
				String cardauthqry = "UPDATE INST_CARD_PROCESS SET  MKCK_STATUS='P',  CHECKER_DATE=sysdate WHERE INST_ID='"+instid+"' AND CARD_STATUS='04' AND ORDER_REF_NO='"+order_refnum[i]+"'"; 
				enctrace( "cardauthqry : " + cardauthqry );
				int x = commondesc.executeTransaction(cardauthqry, jdbctemplate);
				trace("Got : " + x );
				cnt++;
				
				try{ 
					
					String mcardno = commondesc.getMaskedCardbyproc(instid, order_refnum[i],table,"O", jdbctemplate);
					if(mcardno==null){mcardno=order_refnum[i];}
					auditbean.setActmsg("card Recieved   "+cnt+ "Authorized Successfully");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0205"); 
					auditbean.setCardno(mcardno);
					auditbean.setRemarks(statusmsg);
					auditbean.setApplicationid(order_refnum[i].toString().trim());
					//auditbean.setCardnumber(order_refnum[i]);
				//	auditbean.setProduct(persorderdetails.product_code);
					//commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
			} 
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", cnt + " Order Receive Authorized successfully" );   
			txManager.commit( status );
			trace(cnt + " Order Receive Authorized successfully...got committed..." );
			
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  "Exception : could not continue the recieve process....");   
			txManager.rollback( status );
			trace("Exception : Error while update the recieve card status : " + e.getMessage() );
		} 
		return this.reciveInsCardAuthHome();
	}
	
}
 