package com.ifp.merchant;

import it.sauronsoftware.base64.Base64;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.jpos.iso.ISODate;
//import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.services.Reveral;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;
import com.opensymphony.xwork2.Result;

import com.ifp.beans.DisputeBeans;
import com.ifp.dao.IsoFileGenDAO;
import com.ifp.iso.RequestSender;
import com.mms.dao.DisputeDAO;

import connection.SwitchConnection;

public class Dispute extends BaseAction 
{ 
	
	 
	private static final long serialVersionUID = -8376161637970676446L;
	
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	
	CommonDesc commondesc = new CommonDesc(); 
	CommonUtil comutil = new CommonUtil();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager(); 
	
	
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
	 
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
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
	
	
	
	/********************** DISPUTE BEGINS**********/
	
	public String txnSearch(){
		return "txnsearch_home";
	}
	
	
	
	/**********************DISPUTE END
	 * @throws JSONException **********/
	
	public void searchTransaction() throws IOException{
		trace("************ searchTransaction ***********" );trace("********** searchTransaction ***********" );
		JSONObject json = new JSONObject();
		try{  
			String instid = comInstId();
			HttpSession session = getRequest().getSession();
			
			
			String cardno = getRequest().getParameter("cardno");
			String txndate = getRequest().getParameter("txndate");
			String traceno = getRequest().getParameter("tracenoval");
			String txnrefno = getRequest().getParameter("txnrefno");
			 
			String searchcond = "";
			if( cardno != "" &&  !cardno.equals("null") ){
				searchcond += " AND CHN='"+cardno+"'";
			}	
			
			if( txndate != "" &&  !txndate.equals("null") ){ 
				searchcond += " AND TRANDATE=to_date('"+txndate+"','dd-mm-yyyy')";
			}
			
			if( traceno != "" &&  !traceno.equals("null") ){  
				searchcond += " AND TRACENO='"+traceno+"'";
			}
			
			if( txnrefno != "" &&  !txnrefno.equals("null") ){   
				searchcond += " AND REFNUM='"+txnrefno+"'";
			}
			trace("Getting records....");
			String txnqry = "SELECT CHN, TXNDESC, TRACENO, TXNREFNUM, REFNUM, to_char(TRANDATE,'dd-mon-yyyy hh:mm:ss') as TRANDATE, TERMLOC, TXNCURRENCY, TXNAMOUNT FROM EZMMS_TRANSACTION ";
			txnqry += " WHERE INSTID='"+instid+"' "+ searchcond;
			enctrace( "txnqry : " + txnqry);
			List txnlist = jdbctemplate.queryForList(txnqry);
			trace("Got the list : " + txnlist.size());
			if( txnlist.isEmpty() ){
				json.put("RESP", 1);
				json.put("REASON", "No records found");
				trace("No Records found...");
			}else{
				Iterator itr = txnlist.iterator();
				int cnt = 0;
				while ( itr.hasNext() ){
					JSONObject jsonnew = new JSONObject();
					Map mp = (Map)itr.next();
					json.put("RESP", 0);
					
					jsonnew.put("CHN", (String)mp.get("CHN"));
					jsonnew.put("TXNDESC", (String)mp.get("TXNDESC"));
					
					Object tracenoobj = (Object)mp.get("TRACENO");					
					jsonnew.put("TRACENO", tracenoobj.toString());
					
					Object txnrefnumobj = (Object)mp.get("REFNUM");
					jsonnew.put("TXNREFNUM", txnrefnumobj.toString());
					
					jsonnew.put("TRANDATE", (String)mp.get("TRANDATE"));
					jsonnew.put("TERMLOC", (String)mp.get("TERMLOC"));
					
					trace("Getting alpha currency code...");
					String currencyalpha = commondesc.getCurrencyAlphaCode((String)mp.get("TXNCURRENCY"), jdbctemplate);
					trace("Got alpha code : " + currencyalpha );
					jsonnew.put("TXNCURRENCY", currencyalpha);
					jsonnew.put("TXNAMOUNT", (String)mp.get("TXNAMOUNT")); 
					json.put("SET"+cnt, jsonnew);
					cnt++;
				} 
				json.put("RECORDCNT", cnt);
			}  
		}catch(Exception e ){
			json.put("RESP", 1);
			json.put("REASON", "Exception : Couldn not continue the process ");
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
		}
		trace("\n\n");enctrace("\n\n");
 
		getResponse().getWriter().write(json.toString());
	}
	
	DisputeBeans dispute = new DisputeBeans();
	DisputeDAO disputedao = new DisputeDAO();
 
	

	public DisputeDAO getDisputedao() {
		return disputedao;
	}

	public void setDisputedao(DisputeDAO disputedao) {
		this.disputedao = disputedao;
	}

	public DisputeBeans getDispute() {
		return dispute;
	}

	public void setDispute(DisputeBeans dispute) {
		this.dispute = dispute;
	}
	
	
	
	public String checkExistCompliant( String instid, String cardno, String refno, String traceno, JdbcTemplate jdbctemplate){
		String compliantid= null;
		try {
			String disputecodeqry = "SELECT COMPLIANT_CODE FROM EZMMS_COMPLIANTS WHERE INSTID='"+instid+"' AND CARD_NO='"+cardno+"' AND REF_NO='"+refno+"' AND TRACE_NO='"+traceno+"' ";
			enctrace("disputecodeqry : "+disputecodeqry);
			compliantid =  (String) jdbctemplate.queryForObject(disputecodeqry, String.class); 
		} catch (DataAccessException e) {	 
			e.printStackTrace(); 
		}
		return compliantid;
	}

	public String complientRegHome() throws Exception{
		trace("************ complientRegHome ***********" );trace("********** complientRegHome ***********" );
		
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String cardno = getRequest().getParameter("CARDNO");
		String refno = getRequest().getParameter("REFNO");
		
		String TCHN = getRequest().getParameter("TCHN");
		
		System.out.println("TCHN1...."+TCHN);
		
		String keyid = "";
		String EDMK="", EDPK="";
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
			
			/*keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			System.out.println("secList::"+secList);  
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

		System.out.println("cardno....."+cardno);
		System.out.println("hcardno....."+hcardno);*/
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strdate = sdf.format(dt);
		String bankname = commondesc.getInstDesc(instid, jdbctemplate);
		
		String cardscheme=null;
		ResultSet rs = null;
		SwitchConnection swhcon = new SwitchConnection();
		Connection conn = swhcon.getConnection();
		trace("Getting transaction details....");
		
		dispute.cardnobean = cardno;
		String transList = "SELECT  TXNCODE, TERMINALID AS TERMID, REFNUM, AMOUNT, TERMLOC, ACQCURRENCYCODE as TXNCURRENCY, TRACENO FROM  EZTXNLOG WHERE  TCHN='"+TCHN+"' AND MSGTYPE='210' AND REFNUM='"+refno+"'";
		PreparedStatement ps = conn.prepareStatement(transList);
		rs = ps.executeQuery(transList);
		System.out.println("transList..."+transList);
		if(rs.next()){
			do{
				cardscheme = cardno.substring(0,6);
				System.out.println("cardschemename..."+cardno.substring(0,6));
				/*String cardschemename = commondesc.getProductdesc(instid,cardscheme,jdbctemplate );
				if( cardschemename == null || cardschemename.equals("NOREC")){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "NO RECORDS FOUND FOR THE CARD SCHEME CODE [ "+cardscheme+" ] ");
					trace("NO RECORDS FOUND FOR THE CARD SCHEME CODE [ "+cardscheme+" ] ");
					return "required_home";
				}*/
				
				String txncode = (String)rs.getString("TXNCODE");
				
				/*String txndesc = commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
				trace("Getting transaction desc [ "+txncode+" ] ...got : " + txndesc);
				if( txndesc.equals("NOREC")){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+txncode+" ] ");
					trace("NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+txncode+" ] ");
					return "required_home";
				}*/
				
				String terminalid =  (String)rs.getString("TERMID");
				System.out.println("terminalid======"+terminalid);
				String refnum =  (String)rs.getString("REFNUM");
				String txnamt =  (String)rs.getString("AMOUNT");
				String terminallocation = (String)rs.getString("TERMLOC");
				String txncurrency = (String)rs.getString("TXNCURRENCY");
				String txncurrencydesc = commondesc.getCurDesc(txncurrency, jdbctemplate);
				Object traceno_obj =  (Object)rs.getString("TRACENO");
	
				//dispute.cardschemebean =cardschemename;
				//dispute.typeoftxnbean=txndesc;
				dispute.terminalidbean=terminalid;
				dispute.refnobean = refnum;
				dispute.txnamtbean =  txnamt;
				dispute.terminallocationbean=terminallocation; 
				dispute.txncurrencybean = txncurrencydesc;
				dispute.trancenobean= traceno_obj.toString();
			}while(rs.next());
		}else{
			System.out.println("else will caleed");
			return "required_home";
		}
		dispute.compliandatetbean = strdate;
		dispute.banknamebean = bankname;
		return "compliant_reghome";
		
		
		/*List translist = disputedao.getTransactionDetails(instid,cardno, refno,jdbctemplate ,session);
		
		dispute.cardnobean = cardno;
		if( translist != null  || !translist.isEmpty() ){
			Iterator txnitr = translist.iterator(); 
			while ( txnitr.hasNext() ){
				Map mp = (Map)txnitr.next();
				String cardscheme = (String)mp.get("BIN");
				System.out.println("cardscheme--"+cardscheme);
				String cardschemename = commondesc.getProductdesc(instid,cardscheme,jdbctemplate );
				if( cardschemename == null || cardschemename.equals("NOREC")){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "NO RECORDS FOUND FOR THE CARD SCHEME CODE [ "+cardscheme+" ] ");
					trace("NO RECORDS FOUND FOR THE CARD SCHEME CODE [ "+cardscheme+" ] ");
					return "required_home";
				}
				
				
				String txncode = (String)mp.get("TXNCODE");
				
				String txndesc = commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
				trace("Getting transaction desc [ "+txncode+" ] ...got : " + txndesc);
				if( txndesc.equals("NOREC")){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+txncode+" ] ");
					trace("NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+txncode+" ] ");
					return "required_home";
				}
				
				String terminalid =  (String)mp.get("TERMID");
				String refnum =  (String)mp.get("REFNUM");
				String txnamt =  (String)mp.get("AMOUNT");
				String terminallocation = (String)mp.get("TERMLOC");
				String txncurrency = (String)mp.get("TXNCURRENCY");
				String txncurrencydesc = commondesc.getCurDesc(txncurrency, jdbctemplate);
				Object traceno_obj =  (Object)mp.get("TRACENO");
	
				dispute.cardschemebean =cardschemename;
				dispute.typeoftxnbean=txndesc;
				dispute.terminalidbean=terminalid;
				dispute.refnobean = refnum;
				dispute.txnamtbean =  txnamt;
				dispute.terminallocationbean=terminallocation; 
				dispute.txncurrencybean = txncurrencydesc;
				dispute.trancenobean= traceno_obj.toString();
			}
		}else{
			return "required_home";
		}
		
		trace("dispute.typeoftxnbean--"+dispute.typeoftxnbean);
		trace("\n\n");enctrace("\n\n");
		
		dispute.compliandatetbean = strdate;
		dispute.banknamebean = bankname;
		return "compliant_reghome";*/
	}
	
	/*private String getProductDesc(String cardscheme, Connection conn) {
		String result = null;
		try {
			String qryproductdesc = "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE='" + cardscheme + "'";
			PreparedStatement ps = conn.prepareStatement(qryproductdesc);
			ResultSet rs = ps.executeQuery(qryproductdesc);
			while(rs.next()){
				result = rs.getString("CARD_TYPE_NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}*/
	
	public String saveregistercomplaint() 
	{
		trace("************ saveregistercomplaint ***********" );trace("********** saveregistercomplaint ***********" );
		HttpSession session=getRequest().getSession();
		 
		IfpTransObj transact = commondesc.myTranObject("SAVECOMPLIANT", txManager);
		 
		try {
			dispute.customernamebean = getRequest().getParameter("customer");
			dispute.cardnobean = getRequest().getParameter("cardno");
			dispute.acctnumberbean = getRequest().getParameter("accnum");
			dispute.emailbean = getRequest().getParameter("emailaddrss");
			dispute.phonenumberbean = getRequest().getParameter("phnum");
			
			//dispute.instidbean = getRequest().getParameter("instidbean");
		 
			dispute.txndatebean = getRequest().getParameter("trandate");
			dispute.commentbean = getRequest().getParameter("comment");
 	 
			dispute.refnobean = getRequest().getParameter("refnum");
			dispute.trancenobean =  getRequest().getParameter("traceno");
			
			dispute.amtreqbean = getRequest().getParameter("amtreq");
			//dispute.amtdispensedbean = getRequest().getParameter("amtdispense");
			//dispute.amtclaimedbean = getRequest().getParameter("amtclaimed");
			dispute.amtdispensedbean = "0";
			dispute.amtclaimedbean = "0";
			
			dispute.reversemode = getRequest().getParameter("reversalmode");
			dispute.instidbean = comInstId();
			dispute.usercodebean = comUserCode();
			 
			trace("Generating txn dispute id...");
			String dispute_id = commondesc.generateTxnDisputeId(dispute.instidbean,"SEQUENCE_MASTER","DISPUTE_CODE_SEQ",6,jdbctemplate);
			trace("Generate txn dispute id..." + dispute_id);
			
			trace("Insergting compliants...");
			int regcompliant = disputedao.insertCompliant(dispute.instidbean, dispute_id, dispute.usercodebean, dispute, jdbctemplate, session);
			trace("Inserting compliants...Got : " + regcompliant );
			if(regcompliant == 1)
			{
				trace("Getting updating sequance...");
				int upd_seq = disputedao.updateSequance(dispute.instidbean, jdbctemplate, session);
				trace("Getting updating sequance...got : " + upd_seq);
				if(upd_seq == 1){
					//commondesc.commitTxn(jdbctemplate);
					txManager.commit(transact.status);
					session.setAttribute("preverr", "S");
					session.setAttribute("prevmsg"," COMPLIANT REGISTERED SUCCESSFULLY FOR CARDNO [ "+dispute.cardnobean+" ]. THE COMPLIANT ID IS [ "+dispute_id+" ]");
					trace("COMPLIANT REGISTERED SUCCESSFULLY FOR CARDNO [ "+dispute.cardnobean+" ]. THE COMPLIANT ID IS [ "+dispute_id+" ]");
					
				}
				else{
					//commondesc.rollbackTxn(jdbctemplate);
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "ERROR  while Registering dispute");
					trace("ERROR  while Registering dispute");
					System.out.println("Roll Back ");
				}
			}
			else{
				//commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " ERROR  while Registering dispute");
				trace(" ERROR  while Registering dispute");
			}
		} catch (Exception e) { 
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the process...");
			trace("Exception : " + e.getMessage());
			System.out.println("Roll Back ");
		}  
		trace("\n\n");enctrace("\n\n");
		return "complaintsuccess";
	}
	
	public String viewRegisteredComplHome(){
		trace("************ viewRegisteredComplHome ***********" );trace("********** viewRegisteredComplHome ***********" );
		HttpSession session=getRequest().getSession();  
		 
		String instid = comInstId(); 
		try{
			trace("Getting list of compliants...");
			List listofcompliants = disputedao.getListOfCompliants(instid, jdbctemplate);
			if( listofcompliants==null || listofcompliants.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Not having any registered complaints ");
				trace("Not having any registered compliants ");
				return "required_home";
			}
			dispute.setCompliantlist(listofcompliants);
		}catch(Exception e){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage() );
			trace("Exception : " + e.getMessage() );
			return "required_home";
		}
		trace("\n\n");enctrace("\n\n");
		return "view_compliant_home";
	}
	
	public String viewCompliantData(){
		trace("************ viewCompliantData ***********" );trace("********** viewCompliantData ***********" );
		HttpSession session=getRequest().getSession();  
		 
		String instid = comInstId(); 
		String enteredispid = getRequest().getParameter("enterdisputeid");
		String selectdispid = getRequest().getParameter("selectdisputeid"); 
		System.out.println();
		try{
			String compliantid ="";
			if( !enteredispid.equals("") ){ compliantid=  enteredispid ; } else { compliantid=  selectdispid; } 
			System.out.println( "compliantid : " + compliantid );
			List compdata = disputedao.getCompliantData(instid, compliantid, jdbctemplate );
			if( compdata==null || compdata.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not get the compliant data for the code [ "+compliantid+" ] ");
				return "required_home";
			}
			else{
				ListIterator itr = compdata.listIterator();
				dispute.compliantcode = compliantid;
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					String registeredby = (String)mp.get("COMPLAINT_REG_BY") ;
					String username = commondesc.getUserName(instid, registeredby, jdbctemplate);
					
					dispute.customernamebean =(String)mp.get("CUSTOMER_NAME") ;
					dispute.cardnobean = (String)mp.get("CARD_NO") ;
					dispute.acctnumberbean = (String)mp.get("ACCT_NO") ;
					dispute.emailbean = (String)mp.get("EMAIL_ADDRS") ;
					dispute.phonenumberbean =(String)mp.get("PHONE_NO") ;
					
				 
					dispute.txndatebean =(String)mp.get("TXN_DATE") ;
					dispute.compliantregister = (String)mp.get("COMPLIANT_DATE") ;
					
					dispute.compliantstatus = (String)mp.get("STATUS") ;
					
					dispute.commentbean =(String)mp.get("COMMENTS") ;
		 	 
					dispute.refnobean = (String)mp.get("REF_NO") ;
					dispute.trancenobean =  (String)mp.get("TRACE_NO") ;
					
					dispute.amtreqbean =  (String ) (Object)mp.get("AMT_REQUESTED").toString() ;
					dispute.amtdispensedbean = (String) (Object) mp.get("AMOUNT_DISPENSED").toString() ;
					dispute.amtclaimedbean = (String) (Object) mp.get("CLAIM_AMT").toString() ;
					
					/*String reversalmodedesc = commondesc.getResponseCodeDesc(instid, (String) (Object) mp.get("REVERSETYPE").toString(), jdbctemplate);					
					dispute.reversemode = reversalmodedesc ;*/
					
					dispute.instidbean =instid;
					dispute.usercodebean = username;
					
					
					List translist = disputedao.getTransactionDetails(instid, dispute.cardnobean, dispute.refnobean, jdbctemplate, session);
					
					 
					if( translist != null  || !translist.isEmpty() ){
						Iterator txnitr = translist.iterator(); 
						while ( txnitr.hasNext() ){
							Map txnmp = (Map)txnitr.next();
							String bin = (String)txnmp.get("BIN");
							System.out.println("bin--"+bin);
							String cardschemename = commondesc.getProductdesc(instid, bin, jdbctemplate) ;
							if( cardschemename.equals("NOREC")){
								session.setAttribute("preverr", "E");
								session.setAttribute("prevmsg", "NO RECORDS FOUND FOR THE CARD BIN [ "+bin+" ] ");
								return "required_home";
							}
							
							
							String txncode = (String)txnmp.get("TXNCODE");
							String txndesc = commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
							if( txndesc.equals("NOREC")){
								session.setAttribute("preverr", "E");
								session.setAttribute("prevmsg", "NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+txncode+" ] ");
								return "required_home";
							}
							 
						 
							String txncurrency = (String)txnmp.get("TXNCURRENCY");
							trace("Getting the currency description...");
							String txncurrencydesc = commondesc.getCurDesc(txncurrency, jdbctemplate);
							trace("Getting the currency description..." + txncurrencydesc );
							dispute.cardschemebean =cardschemename;
							dispute.typeoftxnbean=txndesc;
							dispute.terminalidbean= (String)txnmp.get("TERMID"); ;
							dispute.refnobean =  (String)txnmp.get("REFNUM");
							 
							dispute.terminallocationbean= (String)txnmp.get("TERMLOC");
							dispute.txncurrencybean = txncurrencydesc;
							 
						}
					}
					
					
					
				}
			//	dispute.setCompliantdata(compdata);
			}
		}catch(Exception e){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the process.." );
			trace("Excepton : " + e.getMessage());
			e.printStackTrace();
			return "required_home";
		}
		trace("\n\n");enctrace("\n\n");
		return "view_compliant_data";
	}
	
	public String changeCompliantStatus(){
		trace("************ changeCompliantStatus ***********" );trace("********** changeCompliantStatus ***********" );
		HttpSession session=getRequest().getSession();  
		 
		IfpTransObj transact = commondesc.myTranObject("CHANGECOMPL", txManager);
		String instid = comInstId(); 
		String usercode = comUserCode();
		String compliantid = getRequest().getParameter("compliantid");
		String compstatus = getRequest().getParameter("status"); 
		String closecomment = getRequest().getParameter("closecomment");
		Socket connect_id = null;
		String status = "Updated ";
		try{
			
			trace("Updating status....got ["+compstatus+"] ");
			if( compstatus.equals("A")){
				
				Reveral reversal = new Reveral();
				IsoFileGenDAO isodao = new IsoFileGenDAO();
				
				/*String hostip = comutil.getHostIpAddress(instid, jdbctemplate);
				 isodao.log("CONFIGURED THE IP ADDRESS [ "+hostip+" ] ");
				if( hostip.equals("000.000.000.000")){  
			    	trace("Could not connect host. Ip address not configured properly");
			    	session.setAttribute("curerr", "E");
					session.setAttribute("curmsg","Could not connect host. Ip address not configured properly");
			    	return "required_home";
				}
				
				int port = comutil.getHostPort(hostip, jdbctemplate);
				isodao.log("CONNECTING PORT [ "+port+" ] ");
				if( port < 0 ){
					isodao.log("CONNECTION SERVER...HOST : "+ hostip +"- PORT : "+port);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg","Could not connect host. Ip address not configured properly");
					trace("Could not connect host. Ip address not configured properly");
					return "required_home";
				}
				
				connect_id = isodao.connectHost( hostip, port, 9000 );
				if( connect_id == null ){ 
					System.out.println("COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port );   
			    	trace("COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port );
			    	session.setAttribute("curerr", "E");
					session.setAttribute("curmsg","COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port );
					return "required_home";
				}*/
				
				
				List complianttxn = disputedao.getCompliantTxnRefno(instid, compliantid, jdbctemplate);
				trace("Getting compliant id refno...got ["+complianttxn+"] ");
				if( complianttxn == null ){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Unable to get Referece" );
					trace("Could not get txn reference for the compliant id [ "+compliantid+" ]" );
					return this.viewRegisteredComplHome();
				}
				
				Iterator itr = complianttxn.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					String cardno = (String)mp.get("CARD_NO");
					String refno = (String)mp.get("REF_NO");
					String traceno = (String)mp.get("TRACE_NO");
					String txnamount=   (String) (Object)mp.get("AMT_REQUESTED").toString()+"00";
					trace("Getting transaction details...");
					
					SwitchConnection swhcon = new SwitchConnection();
					Connection conn = swhcon.getConnection();
					ResultSet rs = null;
					String transList = "SELECT TXNCODE, AMOUNT,TERMINALID,TERMLOC, TO_CHAR(TRANDATE,'DDMMyy')||TO_CHAR(TRANTIME) AS TRANDATETIME, DEVICETYPE, ACQUIRERID, ACCEPTORID FROM EZTXNLOG  "+ 
			                  " WHERE ACQUIRERINST='"+instid+"' AND CHN='"+cardno+"' AND  REFNUM='"+refno+"' AND TRACENO='"+traceno+"'";
					PreparedStatement ps = conn.prepareStatement(transList);
					System.out.println("transListqry..."+transList);
					rs = ps.executeQuery(transList);
					System.out.println("transList..."+transList);
					
					String trandatetime = "";
					String terminalid="";
					String terminalloc ="";
					String devicetype="";
					String txncode="";
					String acquireid = "";
					String merchantid = "";
					
					if(rs.next()){
						do{
							trandatetime = rs.getString("TRANDATETIME");
							  terminalid = rs.getString("TERMINALID"); 
							  terminalloc = rs.getString("TERMLOC");
							  devicetype = rs.getString("DEVICETYPE");
							  txncode= rs.getString("TXNCODE");
							  acquireid= rs.getString("ACQUIRERID");
							  merchantid= rs.getString("ACCEPTORID"); 
							
						}while(rs.next());
					}
					/*List txndetails = commondesc.getTransactionDetails(instid, cardno, refno, traceno, jdbctemplate );
					trace("Getting Trandaction details...got ["+txndetails+"] ");
					if( txndetails == null ){
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Unable to get Referece" );
						trace("Could not get txn details for [ instid["+instid+"] cardno["+cardno+"] refno["+refno+"] traceno["+traceno+"]  ]" );
						return this.viewRegisteredComplHome();
					}
					Iterator txnitr = txndetails.iterator();
					String trandatetime = "";
					String terminalid="";
					String terminalloc ="";
					String devicetype="";
					String txncode="";
					String acquireid = "";
					String merchantid = "";
					while( txnitr.hasNext() ){
						Map txnmp = (Map)txnitr.next(); 
						  trandatetime = (String)txnmp.get("TRANDATETIME");
						  terminalid = (String)txnmp.get("TERMINALID");
						  terminalloc = (String)txnmp.get("TERMLOC");
						  devicetype = (String)txnmp.get("DEVICETYPE");
						  txncode= (String)txnmp.get("TXNCODE");
						  acquireid= (String)txnmp.get("ACQUIRERID");
						  merchantid= (String)txnmp.get("ACCEPTORID");
					}*/
					
					trace("Seding reversal message");
					//JSONObject reversaljson =   reversal.fullReversalTransction(instid, cardno, refno, traceno, txnamount, txncode, 
					//		terminalid, terminalloc, devicetype, trandatetime, acquireid, merchantid, isodao,  jdbctemplate, getRequest(), connect_id);
					RequestSender reqsender = new RequestSender(); 
					JSONObject reversaljson = new JSONObject( reqsender.reverseTransaction(instid, "FULLREV", cardno, refno, terminalid, jdbctemplate) );
					trace("Got response : " + reversaljson );
					if( reversaljson.getInt("INTERRESP") != 0 ){
						session.setAttribute("prevmsg", reversaljson.getString("INTERRESPVAL") );
						session.setAttribute("preverr", "E");
						return "required_home";
					}
					status = "Approved ";
				}
				
				
			}
			
			
			trace("Updating compliant status...");
			int updcompliantstatus = disputedao.updateCompliantStatus(instid, compliantid, compstatus, closecomment, usercode, jdbctemplate );
			trace("Got compliant status : " + updcompliantstatus );
			if( updcompliantstatus < 0  ){
				//commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not update the status" );
				trace("Could not update the status" );
				return this.viewRegisteredComplHome();
			}
			
			//commondesc.commitTxn(jdbctemplate);
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Compliant Status "+status+" successfully" );
			trace("Compliant Status updated successfully" );
			
			
		}catch( Exception e ){
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception: Could not continue the process");
			trace("Exception: " + e.getMessage() );
			e.printStackTrace();
			return "required_home";
		}finally{
			try { 
				if( connect_id != null ){
					connect_id.close();
				} 
			} catch (IOException e) {
				System.out.println("IOException : " +e.getMessage());
				e.printStackTrace();
			} 
			System.out.println(" SOCKET CONNECTION CLOSED PROPERLY..." ); 
		}
		trace("\n\n");enctrace("\n\n");
		  return "required_home";
	}
	
	public String viewTransDetails() throws SQLException{

		trace("************ viewTransDetails ***********" );trace("********** viewTransDetails ***********" );
		
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String cardno = getRequest().getParameter("CARDNO");
		String refno = getRequest().getParameter("REFNO");
		
		String TCHN = getRequest().getParameter("TCHN");
		
		System.out.println("TCHN1...."+TCHN);
		System.out.println("cardno...."+cardno);
		
		
		String keyid = "";
		String EDMK="", EDPK="";
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
			
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strdate = sdf.format(dt);
		String bankname = commondesc.getInstDesc(instid, jdbctemplate);
		
		String cardscheme=null;
		ResultSet rs = null;
		SwitchConnection swhcon = new SwitchConnection();
		Connection conn = swhcon.getConnection();
		trace("Getting transaction details....");
		
		dispute.cardnobean = cardno;
		String transList = "SELECT  TCHN,CHN,TRACENO,REFNUM,ACQUIRERINST,RESPCODE,TXNCODE,TO_CHAR(TRANDATE,'DD-MM-YYYY') AS TRANDATE,AMOUNT,ACQCURRENCYCODE,ACQCOUNTRYCODE,TERMINALID,MERCHANTTYPE,ACCEPTORID,TERMLOC," +
				"CASE  WHEN SUBSTR(TRANINPUT ,1,1)='P' THEN 'POS' WHEN SUBSTR(TRANINPUT ,1,1)='A' THEN 'ATM'  WHEN SUBSTR(TRANINPUT ,1,1)='E' " +
				"THEN 'ONLINE' WHEN SUBSTR(TRANINPUT ,1,1)='M' THEN 'MOTO' WHEN SUBSTR(TRANINPUT ,1,1)='F' THEN 'AFD' WHEN SUBSTR(TRANINPUT ,1,1)='T' THEN 'T&E' END AS ACQMODE," +
				"DECODE(SUBSTR(TRANINPUT ,2,1),'Y','YES','N','NO') AS CARDNOPRESENT," +
				"DECODE(SUBSTR(TRANINPUT ,3,1),'Y','YES','N','NO') AS TRACK2PRESENT," +
				"DECODE(SUBSTR(TRANINPUT ,4,1),'Y','YES','N','NO') AS EXPDATEPRESENT," +
				"DECODE(SUBSTR(TRANINPUT ,5,1),'Y','YES','N','NO') AS PINPRESENT," +
				"DECODE(SUBSTR(TRANINPUT ,6,1),'Y','YES','N','NO') AS CHIPDATAPRESENT," +
				"DECODE(SUBSTR(TRANINPUT ,7,1),'Y','YES','N','NO') AS CVV1ICVVPRESENT," +
				"DECODE(SUBSTR(TRANINPUT ,8,1),'Y','YES','N','NO') AS CVV2PRESENT," +
				"DECODE(SUBSTR(SECVALIDATION ,1,1),'S','SUCCESS','F','FAILED','N','NOT APPLICABLE') AS EXPDATECHK," +
				"DECODE(SUBSTR(SECVALIDATION ,2,1),'S','SUCCESS','F','FAILED','N','NOT APPLICABLE') AS PINCHK," +
				"DECODE(SUBSTR(SECVALIDATION ,3,1),'S','SUCCESS','F','FAILED','N','NOT APPLICABLE') AS CRYPTOGRAMCHK," +
				"DECODE(SUBSTR(SECVALIDATION ,4,1),'S','SUCCESS','F','FAILED','N','NOT APPLICABLE') AS CVV1ICVVCHK," +
				"DECODE(SUBSTR(SECVALIDATION ,5,1),'S','SUCCESS','F','FAILED','N','NOT APPLICABLE') AS CVV2CHK" +
				" FROM  EZTXNLOG WHERE TCHN='"+TCHN+"' AND REFNUM='"+refno+"' AND MSGTYPE='210' AND TXNCODE NOT LIKE '36%'";
		PreparedStatement ps = conn.prepareStatement(transList);
		System.out.println("transListqry..."+transList);
		enctrace( "transListqryforviewtxns : " + transList);
		rs = ps.executeQuery(transList);
		System.out.println("transList..."+transList);
		String tchn = "";
		if(rs.next()){
			do{
				dispute.setInstidbean((String)rs.getString("ACQUIRERINST"));
				dispute.setResponse((String)rs.getString("RESPCODE"));
				dispute.setTxndatebean((String)rs.getString("TRANDATE"));
				dispute.setTxnamtbean((String)rs.getString("AMOUNT"));
				dispute.setTxncurrencybean((String)rs.getString("ACQCURRENCYCODE"));
				dispute.setCountry((String)rs.getString("ACQCOUNTRYCODE"));
				dispute.setTerminalidbean((String)rs.getString("TERMINALID"));
				dispute.setActivity((String)rs.getString("MERCHANTTYPE"));
				dispute.setMerchantid((String)rs.getString("ACCEPTORID"));
				dispute.setTerminallocationbean((String)rs.getString("TERMLOC"));
				dispute.setAcquiringmode((String)rs.getString("ACQMODE"));
				dispute.setCarnopresent((String)rs.getString("CARDNOPRESENT"));
				dispute.setTrack2present((String)rs.getString("TRACK2PRESENT"));
				dispute.setExpdatepresent((String)rs.getString("EXPDATEPRESENT"));
				dispute.setPinpresent((String)rs.getString("PINPRESENT"));
				dispute.setChipdatapresent((String)rs.getString("CHIPDATAPRESENT"));
				dispute.setCvv1icvvpresent((String)rs.getString("CVV1ICVVPRESENT"));
				dispute.setCvv2present((String)rs.getString("CVV2PRESENT"));
				dispute.setExpdatecheck((String)rs.getString("EXPDATECHK"));
				dispute.setPincheck((String)rs.getString("PINCHK"));
				dispute.setCryptogramcheck((String)rs.getString("CRYPTOGRAMCHK"));
				dispute.setCvv1icvvcheck((String)rs.getString("CVV1ICVVCHK"));
				dispute.setCvv2check((String)rs.getString("CVV2CHK"));
				dispute.setCardnobean((String)rs.getString("CHN"));
				dispute.setTrancenobean((String)rs.getString("TRACENO"));
				dispute.setRefnobean((String)rs.getString("REFNUM"));
				tchn = rs.getString("TCHN");
				/*cardscheme = cardno.substring(0,6);
				System.out.println("cardschemename..."+cardno.substring(0,6));
				String cardschemename = commondesc.getProductdesc(instid,cardscheme,jdbctemplate );
				if( cardschemename == null || cardschemename.equals("NOREC")){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "NO RECORDS FOUND FOR THE CARD SCHEME CODE [ "+cardscheme+" ] ");
					trace("NO RECORDS FOUND FOR THE CARD SCHEME CODE [ "+cardscheme+" ] ");
					return "required_home";
				}*/
				
				String txncode = (String)rs.getString("TXNCODE");
				
				String txndesc = commondesc.getTransactionDesc(instid, txncode, jdbctemplate);
				trace("Getting transaction desc [ "+txncode+" ] ...got : " + txndesc);
				if( txndesc.equals("NOREC")){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+txncode+" ] ");
					trace("NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+txncode+" ] ");
					return "required_home";
				}
				dispute.setTypeoftxnbean(txndesc);
				
				/*String dtlsqry = "SELECT TO_CHAR(EXPIRY_DATE,'DD-MM-YYYY') AS EXPIRY_DATE,NVL(CIN,'--') AS CUST_ID FROM CARD_PRODUCTION WHERE HCARD_NO='"+tchn+"'";
				System.out.println("rs value" + dtlsqry);
				enctrace( "custnameqry : " + dtlsqry);
				List<Map<String,Object>> custnamelist =  jdbctemplate.queryForList(dtlsqry);
				String custid = "--", expdate= "--";
				if(custnamelist.isEmpty()){
					custid = "--";
					expdate = "--";
				}else{
					custid = (String) custnamelist.get(0).get("CUST_ID");
					expdate = (String) custnamelist.get(0).get("EXPIRY_DATE");
				}
				
				dispute.setExpirydate(expdate);
				dispute.setCustomerid(custid);*/
				
			/*String terminalid =  (String)rs.getString("TERMID");
			System.out.println("terminalid======"+terminalid);
			String refnum =  (String)rs.getString("REFNUM");
			String txnamt =  (String)rs.getString("AMOUNT");
			String terminallocation = (String)rs.getString("TERMLOC");
			String txncurrency = (String)rs.getString("TXNCURRENCY");
			String txncurrencydesc = commondesc.getCurDesc(txncurrency, jdbctemplate);
			Object traceno_obj =  (Object)rs.getString("TRACENO");*/
	
			/*	//dispute.cardschemebean =cardschemename;
				//dispute.typeoftxnbean=txndesc;
				dispute.terminalidbean=terminalid;
				dispute.refnobean = refnum;
				dispute.txnamtbean =  txnamt;
				dispute.terminallocationbean=terminallocation; 
				dispute.txncurrencybean = txncurrencydesc;
				dispute.trancenobean= traceno_obj.toString();*/
			}while(rs.next());
		}else{
			System.out.println("else will caleed");
			return "required_home";
		}
		dispute.compliandatetbean = strdate;
		dispute.banknamebean = bankname;
		return "viewtransdetails";
	
	}
	public String registerComplaint(){
		dispute.setCardnobean(getRequest().getParameter("cardno"));
		dispute.setTrancenobean(getRequest().getParameter("traceno"));
		dispute.setRefnobean(getRequest().getParameter("refno"));
		dispute.setTxnamtbean(getRequest().getParameter("amt"));
		//dispute.setInstidbean(getRequest().getParameter("instidbean"));
		return "compliant_reghome";
	}
	
}//end class
 