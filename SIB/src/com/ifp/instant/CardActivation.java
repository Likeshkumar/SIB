package com.ifp.instant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifd.beans.CustomerRegisterationBeans;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.CardActivationBeans;
import com.ifp.dao.CardActivationDAO;
import com.ifp.dao.InstCardActivateProcessDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import connection.Dbcon;


public class CardActivation extends BaseAction{
 
	private static final long serialVersionUID = 1L;
	
	CardActivationDAO cardactdao = new CardActivationDAO();
	CardActivationBeans cardactbean = new CardActivationBeans();
	 
	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	private static Boolean  initmail = true; 
	private static  String parentid = "000";
	final String CARDAWAINGCODE = "04";  
	String CARDACTIVATEDCODE = CommonDesc.ACTIVECARDSTATUS;
	
	
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
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	 
	public CardActivationDAO getCardactdao() {
		return cardactdao;
	}
	public void setCardactdao(CardActivationDAO cardactdao) {
		this.cardactdao = cardactdao;
	}
	public CardActivationBeans getCardactbean() {
		return cardactbean;
	}
	public void setCardactbean(CardActivationBeans cardactbean) {
		this.cardactbean = cardactbean;
	}
	
	
	private List  branchlist;
	private List prodlist;
	
	public String getFromdate() {
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}


	private String fromdate;
	private String todate;
	
	private List  cardstobranchlist;
	
	public List getCardstobranchlist() {
		return cardstobranchlist;
	}
	public void setCardstobranchlist(List cardstobranchlist) {
		this.cardstobranchlist = cardstobranchlist;
	}
	public List getBranchlist() {
		return branchlist;
	}
	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}
	public List getProdlist() {
		return prodlist;
	}
	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}


	AuditBeans auditbean = new AuditBeans();
	
	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
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
	
	
	public String comUserId(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
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
	public String comUsername(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
	}
	CustomerRegisterationBeans dbtcustregbean = new CustomerRegisterationBeans();
	
	public String cardActivationHome(){
		trace("Card issuance begins....\n");
		enctrace("Card issuance begins....\n");
		/*HttpSession session = getRequest().getSession(); 
		String instid = comInstId();
		String processtype = "INST";
		String mkckstatus="P";
		try{*/
			/*trace("Getting product list....");
			List prodlist = this.getProductListBySelected( instid, CARDAWAINGCODE,mkckstatus, jdbctemplate );
			if( prodlist==null || prodlist.isEmpty() ){
				addActionError( "No Cards for Issue....");
				trace("No Cards for Issue....");
				return "required_home";
			}
			//ListIterator proditr = prodlist.listIterator();
			while( proditr.hasNext() ){
				Map  prodmp = (Map) proditr.next();
				String prodcode = (String)prodmp.get("PRODUCT_CODE");
				String productdesc = commondesc.getProductdesc(instid, prodcode,  jdbctemplate);
				prodmp.put("PRODUCT_DESC", productdesc);
				proditr.remove();
				proditr.add(prodmp);
			}
			cardactbean.setProdlist(prodlist);
			
			List branchlist =  this.getBranchCodefmProcess(instid, CARDAWAINGCODE,mkckstatus, jdbctemplate );
			if( branchlist==null || branchlist.isEmpty() ){
				addActionError("No Cards for Issue....");
				return "required_home";
			}
			//ListIterator britr = branchlist.listIterator();
			while( britr.hasNext() ){
				Map  brmp = (Map) britr.next();
				String branchcode = (String)brmp.get("BRANCH_CODE");
				String branchdesc = commondesc.getBranchDesc(instid, branchcode, jdbctemplate);
				brmp.put("BRANCH_DESC", branchdesc);
				britr.remove();
				britr.add(brmp);
			}
			cardactbean.setBranchlist(branchlist); 
			
			*//*** MAIL BLOCK ****//*
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
			*//*** MAIL BLOCK ****/
			HttpSession session = getRequest().getSession();
			List pers_prodlist=null,br_list=null;
			String inst_id =comInstId();
			String usertype = comuserType();
			String branch = comBranchId();
			
			String mkrstatus="P";
			try {
				System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
				if (usertype.equals("INSTADMIN")) {
					System.out.println("Branch list start");
					br_list = commondesc.instantcardmapping(inst_id, CARDAWAINGCODE, mkrstatus, jdbctemplate);
					System.out.println("Branch list "+br_list);
					if(!(br_list.isEmpty())){
						setBranchlist(br_list);
						System.out.println("Branch list is not empty");
						//setCardgenstatus('Y');
					}
					else{
						addActionError("No Cards Waiting For Card Issuance ... ");
						System.out.println("Branch List is empty ");
						//return "required_home";    
						return "required_home";
					}
				}
				pers_prodlist=commondesc.InstgetProductListBySelected(inst_id, CARDAWAINGCODE, mkrstatus, jdbctemplate);
				if (!(pers_prodlist.isEmpty())){
					 setProdlist(pers_prodlist);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Product List is ===> "+pers_prodlist);
					//setCardgenstatus('Y');
				} else{
					System.out.println("No Product Details Found ");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Product Details Found ");
					//setCardgenstatus('N');
				}
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
			
		}catch(Exception e ){
			addActionError("Unable to continue ...");
			trace("Exception : cardActivationHome - Could not continue the process..."  + e.getMessage() );
			return "required_home";
		}
		return "activation_home";
		
	}
	
	
	public String listofCardActivCards(){
		trace("List of active cards....");
		enctrace("List of active cards...");
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String usertype = comuserType();
		String processtype = "INST";	
		String mkrstatus="P";
		String branch = getRequest().getParameter("branchcode");
		
		String prodcode = getRequest().getParameter("cardtype");		
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String dateflag="A.PRE_DATE";
		List branchlist=null,br_list=null,pers_prodlist=null;
		try{
			trace("Getting list of waitin for activation cards....");
			
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.instantcardmapping(instid, CARDAWAINGCODE, mkrstatus, jdbctemplate);
				System.out.println("Branch list "+br_list);
				if(!(br_list.isEmpty())){
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					//setCardgenstatus('Y');
				}
				else{
					addActionError("No Cards Waiting For Card Issuance ... ");
					System.out.println("Branch List is empty ");
					return "required_home";    
					
				}
			}
			pers_prodlist=commondesc.InstgetProductListBySelected(instid, CARDAWAINGCODE, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())){
				 setProdlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
				//setCardgenstatus('Y');
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
				//setCardgenstatus('N');
			}
			
			//String condition = commondesc.filterCondition(prodcode, branch, fromdate, todate, dateflag);
			String condition = commondesc.filterConditionINST(prodcode, branch, fromdate, todate, dateflag);
			
			//CHANGED ON 12-FEB-2021 BY GOWTHAM
			 //List cardactivationlist = cardactdao.getCardActivationWaitingCards(instid,prodcode, CARDAWAINGCODE, processtype,condition, jdbctemplate );
			 List cardactivationlist = cardactdao.getCardActivationWaitingCardsINST(instid,prodcode, CARDAWAINGCODE, processtype,condition, jdbctemplate );
			 if( cardactivationlist== null || cardactivationlist.isEmpty() ){
				 addActionError("No Cards for activation....");
				 trace("No cards for activation....");
				 return "activation_home";
			 }
			 ListIterator awtitr = cardactivationlist.listIterator();
			 String product_desc = commondesc.getProductdesc(instid, prodcode, jdbctemplate);
			 while( awtitr.hasNext() ){
				 Map awtmp = (Map)awtitr.next();
				 String subproductode = (String)awtmp.get("SUB_PROD_ID");
				 String subprod_desc = commondesc.getSubProductdesc(instid, subproductode,  jdbctemplate);
				 awtmp.put("SUBPRODUCUT_DESC", subprod_desc );
			 }
			 cardactbean.setCardawaitinglist(cardactivationlist);
			 //Branch 
			 
			 branchlist = commondesc.generateBranchList(instid, jdbctemplate);
				
				if( branchlist.isEmpty() ){
					addActionError("Could not get branch list");
					return "activation_home";
				}
				//dbtcustregbean.setBranchlist(branchlist);
				setCardstobranchlist(branchlist);
				setFromdate(fromdate);
				setTodate(todate);
		}catch(Exception e){
			addActionError("Exceptino : unable to continue the process");
			e.printStackTrace();
			trace("Exception : could not list the activation awaiting cards : " + e.getMessage() );
			return "activation_home";
		}
		//return "activation_list";
		return "activation_home";
	}
	
	public String activattCardAction(){
		trace("Card issuance action begins \n");
		enctrace("Card issuance action begins \n");
		HttpSession session = getRequest().getSession();
		 
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		
		String instid = comInstId();
		String userid = comUserCode();
		String collectbranch = getRequest().getParameter("collectbranch");
		//System.out.println("card collect branch"+collectbranch);
		String[] awaitcardlist = getRequest().getParameterValues("instorderrefnum");
		String processtype = "INST";
		String table="INST_CARD_PROCESS";
		String mkckstatus ="M";
		String cardstatus ="04";
		Boolean updatecardissstatus = true;
		int size = 0;
		int[] updateCounts = new int[size];

		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();
		PreparedStatement pstmt = null;
		
		try{
			if( awaitcardlist== null ){
				addActionError("No card number selected ....");
					//return "required_home";
				return "activation_home";
			}
			int crdcnt = 0;
			
			trace("Getting switch card status for [ "+CARDACTIVATEDCODE+" ] ");
			/*String switchcardstatus = commondesc.getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate);
			if( switchcardstatus == null ){
				addActionError("Could not get switch card map status.");
				trace("Could not get switch card map status.");
			}*/
			
			
			 System.out.println("collectbranch-->"+collectbranch);
			 System.out.println("instid-->"+instid);
			 System.out.println("order_ref_no-->"+awaitcardlist.length);
			
			if (conn != null) {
				System.out.println(" connection opend----->\n");
				trace("connectionopend---\n");
				enctrace("connectionopend---\n");
				conn.setAutoCommit(false);
				String sql = " UPDATE INST_CARD_PROCESS SET ISSUE_DATE=SYSDATE,CARD_STATUS='04',MKCK_STATUS='M', CARD_COLLECT_BRANCH = ? where INST_ID=? AND ORG_CHN=? ";
				pstmt = conn.prepareStatement(sql);
				trace("sql qry for update : " + sql);
				for (int i = 0; i < awaitcardlist.length; i++) {
					pstmt.setString(1, collectbranch);
					pstmt.setString(2, instid);
					pstmt.setString(3, awaitcardlist[i].toString());
					pstmt.addBatch();
				}
				long start = System.currentTimeMillis();
				updateCounts = pstmt.executeBatch();
				long end = System.currentTimeMillis();
				System.out.println("updateCounts--->"+updateCounts.length);
				System.out.println("awaitcardlist.length---->"+awaitcardlist.length);
				trace("awaitcardlist.length---->"+awaitcardlist.length);
				
				if(updateCounts.length==awaitcardlist.length){
				conn.commit();
				addActionMessage( updateCounts.length + " Card(s) Issued Successfully, Waiting for Authorization.");
				System.out.append("Update records-----------> /n" + updateCounts.length);
				trace(updateCounts.length + " Card(s) Issued Successfully..got committed...");
				return cardActivationHome();}
				else{
					conn.rollback();
					pstmt.clearBatch() ;
					System.out.println("could not continue the issuance process.../n--");
					trace("could not continue the issuance process...---");
					addActionError("could not continue the issuance process...!!!");
					return cardActivationHome();
					}
			 }else{
				 
		System.out.println("connection not opended-----/n--");
		trace("connection not opended-------");
		addActionError("DataBase connection not open!!!");}
			
		
		}catch(Exception e){
			txManager.rollback(status);
			addActionError("Exception : could not continue the issuance process...");
			trace("Exception : could not continue the issuance process..."+ e.getMessage());
			e.printStackTrace();
	 		return cardActivationHome();
		}
		finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					System.out.println("prepare stmt closed-----");
				}
				if (conn != null) {
					
					conn.close();
					System.out.println("Jdbc connection closed-----> ");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cardActivationHome();
	}
	
		
	public String authcardActivationHome(){
		/*trace("Card issuance begins....\n");
		enctrace("Card issuance begins....\n");
		HttpSession session = getRequest().getSession(); 
		String instid = comInstId();
		String processtype = "INST";
		String mkckstatus="M";
		
		
		try{*/
			/*trace("Getting product list....");
			List prodlist = this.getProductListBySelected( instid, CARDAWAINGCODE,mkckstatus, jdbctemplate );
			if( prodlist==null || prodlist.isEmpty() ){
				addActionError( "No Cards for Issue....");
				trace("No Cards for Issue....");
				return "required_home";
			}
			ListIterator proditr = prodlist.listIterator();
			while( proditr.hasNext() ){
				Map  prodmp = (Map) proditr.next();
				String prodcode = (String)prodmp.get("PRODUCT_CODE");
				String productdesc = commondesc.getProductdesc(instid, prodcode,  jdbctemplate);
				prodmp.put("PRODUCT_DESC", productdesc);
				proditr.remove();
				proditr.add(prodmp);
			}
			cardactbean.setProdlist(prodlist);
			
			List branchlist =  this.getBranchCodefmProcess(instid, CARDAWAINGCODE,mkckstatus, jdbctemplate );
			if( branchlist==null || branchlist.isEmpty() ){
				addActionError("No Cards for Issue....");
				return "required_home";
			}
			ListIterator britr = branchlist.listIterator();
			while( britr.hasNext() ){
				Map  brmp = (Map) britr.next();
				String branchcode = (String)brmp.get("BRANCH_CODE");
				String branchdesc = commondesc.getBranchDesc(instid, branchcode, jdbctemplate);
				brmp.put("BRANCH_DESC", branchdesc);
				britr.remove();
				britr.add(brmp);
			}
			cardactbean.setBranchlist(branchlist); 
			
			*//*** MAIL BLOCK ****//*
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
			*//*** MAIL BLOCK ****/
			
			HttpSession session = getRequest().getSession();
			List pers_prodlist=null,cardcollectbr_list=null;
			String inst_id =comInstId();
			String usertype = comuserType();
			String branch = comBranchId();
			
			String mkrstatus="M";
			try {
				System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
				if (usertype.equals("INSTADMIN")) {
					System.out.println("Branch list start");
					cardcollectbr_list = commondesc.InstgetcollectBranchCodefmProcess(inst_id, CARDAWAINGCODE, mkrstatus, jdbctemplate);
					System.out.println("Branch list "+cardcollectbr_list);
					if(!(cardcollectbr_list.isEmpty())){
						setBranchlist(cardcollectbr_list);
						System.out.println("Branch list is not empty");
						//setCardgenstatus('Y');
					}
					else{
						addActionError("No Cards Waiting For Card Issuance Authorization ... ");
						System.out.println("Branch List is empty ");
						return "required_home";    
						
					}
				}
				pers_prodlist=commondesc.InstgetProductListBySelected(inst_id, CARDAWAINGCODE, mkrstatus, jdbctemplate);
				if (!(pers_prodlist.isEmpty())){
					setProdlist(pers_prodlist);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Product List is ===> "+pers_prodlist);
					//setCardgenstatus('Y');
				} else{
					System.out.println("No Product Details Found ");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Product Details Found ");
					//setCardgenstatus('N');
				}
			
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
			
		}catch(Exception e ){
			addActionError("Unable to continue ...");
			trace("Exception : cardActivationHome - Could not continue the process..."  + e.getMessage() );
			return "required_home";
		}
		return "authactivation_home";
		
		//authactivation_home
	}
	
	
	
	public String authlistofCardActivCards(){
		trace("List of active cards....");
		enctrace("List of active cards...");
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String prodcode = getRequest().getParameter("cardtype");
		String branchcode = getRequest().getParameter("branchcode");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String processtype = "INST";	
		String dateflag="ISSUE_DATE";
		System.out.println("prodcode--<"+prodcode);
		System.out.println("branchcode--<"+branchcode);
		System.out.println("instid--<"+instid);
		
		try{
			trace("Getting list of waitin for activation cards....");
			
			String condition = commondesc.filtercondcollectbranch(prodcode, branchcode,fromdate, todate, dateflag);
			
			// List cardactivationlist = cardactdao.authgetCardActivationWaitingCards(instid,prodcode, CARDAWAINGCODE, processtype,condition, jdbctemplate );
			 List cardactivationlist = cardactdao.authgetCardActivationWaitingCardsINSTAUTH(instid,prodcode, CARDAWAINGCODE, processtype,condition, jdbctemplate );
			 if( cardactivationlist== null || cardactivationlist.isEmpty() ){
				 addActionError("No Cards for activation....");
				 trace("No cards for activation....");
				 return "required_home";
			 }
			 ListIterator awtitr = cardactivationlist.listIterator();
			 String product_desc = commondesc.getProductdesc(instid, prodcode, jdbctemplate);
			 while( awtitr.hasNext() ){
				 Map awtmp = (Map)awtitr.next();
				 String subproductode = (String)awtmp.get("SUB_PROD_ID");
				 String subprod_desc = commondesc.getSubProductdesc(instid, subproductode,  jdbctemplate);
				 awtmp.put("SUBPRODUCUT_DESC", subprod_desc );
				 
				 
			 }
			
			 cardactbean.setCardawaitinglist(cardactivationlist);
			 
			 
		}catch(Exception e){
			addActionError("Exceptino : unable to continue the process");
			e.printStackTrace();
			trace("Exception : could not list the activation awaiting cards : " + e.getMessage() );
			return "required_home";
		}
		return "authactivation_list";
	}
	
	
	
	
	
	public String authactivateCardNumber(){
		trace("Card issuance action begins \n");
		enctrace("Card issuance action begins \n");
		HttpSession session = getRequest().getSession();
		 
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		
		String instid = comInstId();
		String userid = comUserCode();
		String[] awaitcardlist = getRequest().getParameterValues("instorderrefnum");
		String processtype = "INST";
		String mkckstatus ="P";
		String cardstatus ="05";
		String table="INST_CARD_PROCESS";
		Boolean cardeligibletocust = true;
		int size = 0;
		int[] updateCounts = new int[size];
		
		//Newly added on 16-02-2021
		Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();
		PreparedStatement pstmt = null;
		//String updqry = "UPDATE INST_CARD_PROCESS SET CARD_STATUS='"+cardstatus+"',mkck_status='"+mkckstatus+"' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"'";

		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		trace("awaitcardlist ===>"+awaitcardlist);
		
		try{
			if( awaitcardlist== null ){
				addActionError("No card number selected ....");
					return "required_home";
			}
			int crdcnt = 0;
			
			trace("Getting switch card status for [ "+CARDACTIVATEDCODE+" ] ");
			/*String switchcardstatus = commondesc.getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate);
			if( switchcardstatus == null ){
				addActionError("Could not get switch card map status.");
				trace("Could not get switch card map status.");
			}*/
			
			
			if (conn != null) {
				System.out.println(" connection opend----->\n");
				trace("connectionopend---\n");
				enctrace("connectionopend---\n");
				conn.setAutoCommit(false);
				String sql = " UPDATE INST_CARD_PROCESS SET ISSUE_DATE=SYSDATE,CARD_STATUS='05',MKCK_STATUS='P' where INST_ID=? AND ORG_CHN=? ";
				pstmt = conn.prepareStatement(sql);
				trace("sql qry for update : " + sql);
				for (int i = 0; i < awaitcardlist.length; i++) {
					pstmt.setString(1, instid);
					pstmt.setString(2, awaitcardlist[i].toString());
					pstmt.addBatch();
					
		 		
					
				}
				long start = System.currentTimeMillis();
				updateCounts = pstmt.executeBatch();
				long end = System.currentTimeMillis();
				System.out.println("updateCounts--->"+updateCounts.length);
				System.out.println("awaitcardlist.length---->"+awaitcardlist.length);
				trace("awaitcardlist.length---->"+awaitcardlist.length);
				
				if(updateCounts.length==awaitcardlist.length){
					conn.commit();
					addActionMessage( updateCounts.length + " Card(s) Authorized Successfully,Eligible to Map customer details...");
					System.out.append("Update records-----------> /n" + updateCounts.length);
					trace(updateCounts.length + " Card(s) Issued Successfully..got committed...");
					trace(crdcnt + " Card(s) Issued Successfully..got committed...");
					//cardLinkWithCbs(awaitcardlist,instid,userid);
				return cardActivationHome();}
				else{
					conn.rollback();
					pstmt.clearBatch() ;
					System.out.println("could not continue the issuance process.../n--");
					trace("could not continue the issuance process...---");
					addActionError("could not continue the issuance process...!!!");
					return cardActivationHome();
					}
			 }else{
				 
		System.out.println("connection not opended-----/n--");
		trace("connection not opended-------");
		addActionError("DataBase connection not open!!!");}
			
			
		}catch(Exception e){
			txManager.rollback(status);
			addActionError("Exception : could not continue the issuance process...");
			trace("Exception : could not continue the issuance process..."+ e.getMessage());
			e.printStackTrace();
			return "required_home";
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					System.out.println("prepare stmt closed-----");
				}
				if (conn != null) {
					conn.close();
					System.out.println("Jdbc connection closed-----> ");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return "required_home";
	}
	@Async
	public void cardLinkWithCbs(String[] order_refnum, String instid, String userid) {
	    // to be executed in the background
		
		
		IfpTransObj transact1 = commondesc.myTranObject("INSCBSLINK", txManager);
		HttpServletRequest req = ServletActionContext.getRequest();
		  HttpServletResponse res = ServletActionContext.getResponse();

		  final AsyncContext asyncContext = req.startAsync(req, res);
		  asyncContext.start(new Runnable() {
		    @Override
		    public void run() {
		      try {
		        // doing some work asynchronously ...
		    	  
		    	  int ordercount = order_refnum.length;
		    	  for (int i = 0; i < ordercount; i++) {
		  			//String hcardNumber = commondesc.getHcardNo(order_refnum[i], jdbctemplate);
		  			System.out.println(" Inside Active Card Report Post To CBS ==> ");
		  			trace(" Inside Active Card Report Post To CBS ==> ");
		  			
		  			String docu_qury = "select BRANCH_CODE,ORG_CHN,CIN,ACCOUNT_NO,EMB_NAME,to_char(ISSUE_DATE,'dd-mm-yyyy') as ISSUE_DATE,"
		  					+ "ORDER_REF_NO,MCARD_NO,ORDER_REF_NO,to_char(EXPIRY_DATE,'dd-mm-yyyy') as EXPIRY_DATE "
		  					+ "from INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and org_chn='"+order_refnum[i]+"'";
		  			System.out.println(docu_qury);
		  			trace(docu_qury);
		  			
		  			List carddetlist = jdbctemplate.queryForList(docu_qury);
		  			String request = "";
		  			Iterator custitr = carddetlist.iterator();
		  			while (custitr.hasNext()) {
		  				Map mp = (Map) custitr.next();
		  				System.out.println(mp);
		  				trace("mp ===>"+mp);
		  				request= "{ \"printedCards\":[{" +
		  				   		//"\"branch\":\""+(String) mp.get("BRANCH_CODE")+"\"," +
		  				   		"\"cardNumber\":\""+(String) mp.get("MCARD_NO")+"\"," +
		  				   		//"\"customerNumber\":\""+(String) mp.get("CIN")+"\"," +
		  				   		//"\"accountNumber\":\""+(String) mp.get("ACCOUNT_NO")+"\"," +
		  				   		//"\"emborsedName\":\""+(String) mp.get("EMB_NAME")+"\"," +
		  				   		"\"cardmanReference\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
		  				   		"\"issueDate\":\""+(String) mp.get("ISSUE_DATE")+"\"," +
		  				   		"\"orderRefNo\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
		  				   		"\"maskedCardNumber\":\""+(String) mp.get("MCARD_NO")+"\"," +
		  				   		"\"makerId\":\""+userid+"\"," +
		  				   		//"\"bulkRefId\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
		  				   		"\"cardFlag\":\"\"," +
		  				   		"\"expiryDate\":\""+(String) mp.get("EXPIRY_DATE")+"\"}]}";
		  				
		  			}
		  			
		  			String result = "",ret=""; 
		  			JSONObject json = null;
		  			Properties props = getCommonDescProperty();
		  			String cbsapiurl = props.getProperty("cbs.api.url");
		  			String cbsapikey = props.getProperty("cbs.api.key");
		  			String cbsapisecret = props.getProperty("cbs.api.secret");
		  			String forwd = props.getProperty("cbs.api.fwd");
		  			try {
		  					String url = cbsapiurl+"core/api/v1.0/account/printedCardReport";
		  					System.out.println("url-->"+url);
		  					trace("url-->"+url);
		  					HttpPost post = new HttpPost(url);
		  					HttpClient client = HttpClientBuilder.create().build();
		  					post.setHeader("Content-Type", "application/json");
		  					post.setHeader("x-api-key", cbsapikey);
		  					post.setHeader("x-api-secret", cbsapisecret);
		  					post.setHeader("X-FORWARDED-FOR", forwd);
		  					
		  					System.out.println(request);
		  					trace("request-->"+request);
		  					
		  					StringEntity params =new StringEntity(request);
		  					post.setEntity(params);
		  					HttpResponse response = client.execute(post);
		  					int responseCode = response.getStatusLine().getStatusCode();
		  					System.out.println("Response Code : " + responseCode);
		  					trace("Response Code : " + responseCode);
		  					
		  					BufferedReader rd = new BufferedReader(
		  				                new InputStreamReader(response.getEntity().getContent()));
		  					String line = "";
		  					while ((line = rd.readLine()) != null) {
		  						result+= line;
		  					}
		  					ret = result;
		  				    System.out.println(ret);
		  				    trace("Response  : " + ret);
		  				    json = new JSONObject(ret);
		  				       
		  	        } catch (Exception e) {
		  				e.printStackTrace();
		  				System.out.println("Exception");
		  			}
		  			
		  			String cbslinkinsert = "INSERT INTO CBS_CARD_LINK(INSTID,CARDNO,MCARDNO,ACCT_NO,BRANCH,CIN,EMB_NAME,"
		  					+ "ORDER_REF_NO,UPLOAD_BY,UPLOAD_STATUS,UPLOAD_RESPCODE,ADDED_DATE,TYPE) SELECT INST_ID,ORG_CHN,MCARD_NO,"
		  					+ "ACCOUNT_NO,BRANCH_CODE,CIN,EMB_NAME,ORDER_REF_NO,'"+userid+"','"+json.getString("message")+"',"
		  							+ "'"+json.getString("responseCode")+"',SYSDATE,'I' "
		  					+ "FROM INST_CARD_PROCESS WHERE ORG_CHN='"+order_refnum[i]+"'";
		  			System.out.println(cbslinkinsert);
		  			int insert = jdbctemplate.update(cbslinkinsert);
		  			if(insert > 0){
		  				System.out.println("instant cbs card link insert success");
		  				txManager.commit(transact1.status);
		  			}else{
		  				System.out.println("instant cbs card link insert fail");
		  				txManager.rollback(transact1.status);
		  			}
		  		}
		      }
		      finally {
		        asyncContext.complete();
		      }
		    }
		  });
	}
	

	public int activateCardNumber(String instid, String cardno, String cardstatus, String switchardstatus, String processtype, String usercode, HttpSession session, JdbcTemplate jdbctemplate ) throws Exception { 
		{
		trace("Moving card to production...."); 
		if ( cardactdao.moveCardToProduction(instid, cardno, cardstatus, switchardstatus, jdbctemplate ) < 0 ) {
			addActionError("Could not Move to production for ["+cardno+"]....");
			trace("Could not Move to production for ["+cardno+"]....");
			 return -1;
		}		
		return 1;
	}

}

	
	public int updatecardissuance(String instid, String cardno, String cardstatus,String mkckstatus,String cardcollectbranch,JdbcTemplate jdbctemplate ) throws Exception { 
		{
			trace("Moving card to production...."); 
			if ( cardactdao.updatecardissancestatus(instid, cardno, cardstatus, mkckstatus,cardcollectbranch, jdbctemplate ) < 0 ) {
				addActionError("Could not Move to production for ["+cardno+"]....");
				trace("Could not Move to production for ["+cardno+"]....");
				 return -1;
			}		
			return 1;
		}
		}
	
	
	public int cardstatuseligiblemap(String instid, String cardno, String cardstatus,String mkckstatus,JdbcTemplate jdbctemplate ) throws Exception { 
		{
			trace("Moving card to production...."); 
			if ( cardactdao.eligiblecardstatusmap(instid, cardno, cardstatus, mkckstatus, jdbctemplate ) < 0 ) {
				addActionError("Could not Move to production for ["+cardno+"]....");
				trace("Could not Move to production for ["+cardno+"]....");
				 return -1;
			}		
			return 1;
		}
	}
		public String filterCondition( String productcode, String branch,String fromdate, String todate, String dateflag,JdbcTemplate jdbctemplate ) {
			String bincond = "";
			String branchcond = "";
			
			String datecond = "";
		 
			
			if( "ALL".equalsIgnoreCase(productcode) ){
				bincond = "";
			}else{
				bincond = " AND PRODUCT_CODE='"+productcode+"' ";
			}
			
			if( "ALL".equalsIgnoreCase(branch) ){
				branchcond = "";
			}else{
				branchcond = " AND trim(CARD_COLLECT_BRANCH)='"+branch.trim()+"'";
			}
			 
			if( fromdate != null && todate != null){
				datecond = "AND ( to_date('"+fromdate+"', 'dd-mm-yyyy') <= "+dateflag+" AND to_date('"+todate+"', 'dd-mm-yyyy' )+1 >= "+dateflag +") ";
			}
			
			
			String filtercond = bincond + branchcond + datecond;
			trace("filter"+filtercond);
			
			return filtercond;
			 
			 
		
		
		}
	
	 
	
}
