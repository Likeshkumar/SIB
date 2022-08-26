package com.ifp.maintain;

 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.SMSSenderBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;
import com.ifp.util.MailSender;
import com.ifp.util.SmsSender;

public class SMSSender extends BaseAction {
	
	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	SMSSenderBeans smsbean = new SMSSenderBeans();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager(); 
	
	
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	public SMSSenderBeans getSmsbean() {
		return smsbean;
	}
	public void setSmsbean(SMSSenderBeans smsbean) {
		this.smsbean = smsbean;
	}
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}


	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	 
	
	 
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	public String comInstId( HttpSession session  ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session ){
		 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}


	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}


	public String sendSMSToCardHome() throws IOException  {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session); 
		try{
			 List binlist = commondesc.getListOfBins(instid, jdbctemplate);
			 if( binlist == null ){
				 session.setAttribute("preverr", "E");
				 session.setAttribute("prevmsg", "No Bin configured....");
				 return "required_home";
			 }
			 System.out.println("binlist: "+binlist);
			 smsbean.setBinlist(binlist); 
			 
		}catch(Exception e){
			 session.setAttribute("preverr", "E");
			 session.setAttribute("prevmsg", "Exception :" + e.getMessage() );
			e.printStackTrace();
		}
		
		return "smssend_home";
		 
	}
	
	public String sendSMSToCardAction(){
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session); 
		String usercode= comUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("sms", txManager);
		String cardnumberslist = getRequest().getParameter("cardnumber");
		String phonenumberlist = getRequest().getParameter("mobilenumber");
		String bin = getRequest().getParameter("bin");
		String prodid = getRequest().getParameter("prodid");
		String subprodid = getRequest().getParameter("subprodid");
		String subject= getRequest().getParameter("subject");
		String smscontent= getRequest().getParameter("smscontent");
		String exceptid = getRequest().getParameter("exceptid");
		List listcard = new ArrayList();
		List listmobile = new ArrayList();
		String entitykey = null;
		String entityvalue = "";
		JSONObject jsonobj = new JSONObject();
		String exceptionvalues = "";
		try{
			
			if( exceptid != null ){
				JSONObject jsonexcept = new JSONObject();
				String exceptbin = getRequest().getParameter("exceptbin");
				if( exceptbin != null && !exceptbin.trim().equals("")){
					jsonexcept.put("$BIN", exceptbin);
				}
				
				String exceptproduct = getRequest().getParameter("exceptproduct");
				if(exceptproduct != null && !exceptproduct.trim().equals("") ){
					jsonexcept.put("$PRODUCT", exceptproduct);
				}
				String subprodidexcept = getRequest().getParameter("subprodidexcept");
				if(exceptproduct != null && !subprodidexcept.trim().equals("")){
					jsonexcept.put("$SUBPRODUCT", exceptproduct);
				}
				
				String exceptmobilenumber = getRequest().getParameter("exceptmobilenumber");
				if(exceptmobilenumber != null && !exceptmobilenumber.trim().equals("") ){
					jsonexcept.put("$MOBILE", exceptmobilenumber);
				}
				
				String exceptcardno= getRequest().getParameter("exceptcardno");
				if(exceptcardno != null && !exceptcardno.trim().equals("") ){
					jsonexcept.put("$CARD", exceptcardno);
				}
				
				exceptionvalues = jsonexcept.toString();
			}
			
			
			String smscode = commondesc.getDate("yyyyMMddhhmmss")+commondesc.generateRandomNumber(4);
			
			if( cardnumberslist != null  && !cardnumberslist.equals("")){ 
				entitykey = "$CARD";
				jsonobj.put(entitykey, cardnumberslist);
				 
				/*if(cardnumberslist.contains(",")){
					String cardlist[] = cardnumberslist.split(",");
					for( int i=0; i<cardlist.length;i++ ){
						listcard.add(cardlist[i]);
					}							
				}else{
					listcard.add(cardnumberslist) ;
				}*/
			}
			else	if( phonenumberlist!=null && !phonenumberlist.equals("") ){
				entitykey = "$MOBILE"; 
				jsonobj.put(entitykey, phonenumberlist);
				/*if(phonenumberlist.contains(",")){
					String phonelist[] = phonenumberlist.split(",");
					for( int i=0; i<phonelist.length;i++ ){
						listmobile.add(phonelist[i]);
					}							
				}else{
					listmobile.add(phonenumberlist) ;
				}*/
			}
			else {
				if( bin != null  ){
					entitykey ="$BIN"; 
					jsonobj.put(entitykey, bin);
				}
				if(prodid !=null    ){
					entitykey ="$PRODUCT";
					jsonobj.put(entitykey, prodid);
				}
				if(subprodid !=null ){
					entitykey ="$SUBPRODUCT";
					if(subprodid.equals("000")){ subprodid = "ALL";}
					jsonobj.put(entitykey, subprodid);
				}
			}
			
			int x = insertBukSmsRecords(instid, smscode, entitykey, jsonobj.toString(), subject, smscontent, usercode, exceptionvalues, jdbctemplate);
			trace("Inserting contents....got : " + x );
			if( x < 0 ){
				txManager.rollback(transact.status);
				 session.setAttribute("preverr", "E");
				 session.setAttribute("prevmsg", "Unable to continue the process");
			}
			 
			 session.setAttribute("preverr", "S");
			 session.setAttribute("prevmsg", "SMS content posted successfully...waiting for verification ");
			 
			txManager.commit(transact.status);
		}catch(Exception e){
			txManager.rollback(transact.status);
			 session.setAttribute("preverr", "E");
			 session.setAttribute("prevmsg", "Exception :" + e.getMessage() );
			e.printStackTrace();
		}
		
		return "smssend_home";
	}
	 
	
	public String getBulkSmsList(){
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try{
			List smslist = this.getBulkSmsList(instid, jdbctemplate);
			if( smslist == null || smslist.isEmpty()){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No SMS waiting for view/authorization");
				return "required_home";
			}
			smsbean.setSmslist(smslist);
			
		}catch(Exception e){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to continue the process");
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
			return "required_home";
		}
		System.out.println("smsbean :"+smsbean.getSmslist());
		return "smsview_home";
	}
	
	public String viewSMSContent(){
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String bulksmscode = getRequest().getParameter("bulksmscode");
		
		List smsgrouplist = new ArrayList();
		List smsexceptlist = new ArrayList();
		try{
			
			String entitylistdata = "", exceptlistdata="";
			
			List smslist = this.getBulkSmsData(instid, bulksmscode, jdbctemplate );
			if( smslist == null || smslist.isEmpty()){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Records found");
				return "required_home";
			}
			
			Map<String, String> configmp = new HashMap<String, String>();
			configmp.put("$BIN", "Bin ");
			configmp.put("$SUBPRODUCT", "Sub-Product ");
			configmp.put("$PRODUCT", "Product ");
			configmp.put("$MOBILE", "Mobile ");
			configmp.put("$CARD", "Card Number ");
			
			Iterator itr = smslist.iterator();
			while( itr.hasNext() ){
				Map mp =(Map)itr.next();
				String entitykey = (String)mp.get("ENTITY") ;
				
				
				String entityvalue= (String)mp.get("ENTITY_VALUE") ;
				JSONObject jsentitykey = new JSONObject (entityvalue); 
				Iterator itrentity = jsentitykey.keys();
				while( itrentity.hasNext() ){
					 String key = (String)itrentity.next();
					 String value = this.getEntityDescription(instid, key, jsentitykey.getString(key), jdbctemplate)  ; 
					 
					 String keydesc =(String)configmp.get(key) ;
					 entitylistdata = keydesc +" : "+value+" ";
					 smsgrouplist.add(entitylistdata);
				}
				smsbean.setSmsgrouplist(smsgrouplist);
				
				
				smsbean.setSmssenderlist(entitylistdata);
				
				String subject= (String)mp.get("SUBJECT") ;
				smsbean.setSmssubject(subject);
				
				String smscontent= (String)mp.get("SMSCONTENT") ;
				smsbean.setSmscontent(smscontent);
				
				String smsdate =  (String)mp.get("SMSDATE") ;
				smsbean.setSmsdate(smsdate);
				
				String configuredby =  (String)mp.get("MAKERID") ;
				String configureduser = commondesc.getUserName(instid, configuredby, jdbctemplate);
				smsbean.setMakerid(configureduser);
				
				String MKCKSTATUS =  (String)mp.get("MKCKSTATUS") ; 
				
				String mkckstatusdesc = "";
				if( MKCKSTATUS.equals("M")){
					mkckstatusdesc = "Waiting for verification";
					smsbean.setVerifyneeded(true);
				}else if( MKCKSTATUS.equals("P")){
					mkckstatusdesc = "Verified...Ready to send";
					smsbean.setVerifyneeded(false);
					smsbean.setSendsmsnow(true);
				}
				smsbean.setSmsstatus(mkckstatusdesc);
				
				
				String exceptvalue=  (String)mp.get("EXCEPT_VALUE") ;
				if( exceptvalue != null ){
					JSONObject jsexceptvalue = new JSONObject (exceptvalue); 
					Iterator itrexceptvalue = jsexceptvalue.keys();
					while( itrexceptvalue.hasNext() ){
						 String key = (String)itrexceptvalue.next();
						 String keydesc = (String)configmp.get(key);
						 String value = jsexceptvalue.getString(key);  
						 exceptlistdata = keydesc +"  : "+value;
						 smsexceptlist.add(exceptlistdata);
					}
				}
				smsbean.setExceptlist(smsexceptlist);
				
				String checkerid =  (String)mp.get("CHECKERID") ;
				String checkedby = "--";
				if( checkerid != null ){
					checkedby = commondesc.getUserName(instid, checkerid, jdbctemplate);
				}
				smsbean.setCheckerid(checkedby);
				
				smsbean.setBulksmsid(bulksmscode);
				
			}
			
		}catch(Exception e){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to continue the process");
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
			return "required_home";
		}
		System.out.println("smsbean :"+smsbean.getSmslist());
		return "smssend_view";
	}
	
	public String getEntityDescription(String instid, String entitykey, String entityvalue, JdbcTemplate jdbctemplate ) throws Exception {
		String entitydesc = null;
		if( entityvalue.equals("ALL")){
			return entityvalue;
		}
		
		if( entitykey.equals("$BIN")){			
			entitydesc  = commondesc.getBinDesc(instid, entityvalue, jdbctemplate);
		}else if( entitykey.equals("$SUBPRODUCT")){
			entitydesc  = commondesc.getSubProductdesc(instid, entityvalue, jdbctemplate);
		}else if( entitykey.equals("$PRODUCT")){
			entitydesc  = commondesc.getProductdesc(instid, entityvalue, jdbctemplate);
		}else{
			entitydesc = entityvalue;
		}
		return entitydesc;
	}
	
	public String editSmsMessage(){
		HttpSession session = getRequest().getSession();
		
		try{
			String bulksmscode = getRequest().getParameter("bulksmsid");
			smsbean.setBulksmsid(bulksmscode);
			String smscontent = getRequest().getParameter("smscontent");
			smsbean.setSmscontent(smscontent);
		}catch(Exception e){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to continue the process");			
			e.printStackTrace();
			
		}
		return "sms_edithome";
	}
	
	public void verifySmsMsg() throws IOException{
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("VERIFY", txManager);
		String result = "";
		try{
			String bulksmscode = getRequest().getParameter("bulksmsid"); 
			String smscontent = getRequest().getParameter("smscontent");
		 
			int x = this.verifySmsContent(instid, bulksmscode, usercode, jdbctemplate);
			trace("Updating sms content...got: " + x );
			if( x < 0 ){
				txManager.rollback(transact.status);
				getResponse().getWriter().write("Unable to process ");
				
			}
			txManager.commit(transact.status);
			result = "Updated successfully";
		}catch(Exception e){
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to continue the process");			
			e.printStackTrace();
			
		}
		getResponse().getWriter().write(result);
	}
	
	public void updateSMSContent() throws IOException{
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		IfpTransObj transact = commondesc.myTranObject("UPDSMS", txManager);
		String result = "";
		try{
			String bulksmscode = getRequest().getParameter("bulksmsid"); 
			String smscontent = getRequest().getParameter("smscontent");
		 
			int x = this.updateSMSContent(instid, bulksmscode, smscontent, jdbctemplate);
			trace("Updating sms content...got: " + x );
			if( x < 0 ){
				txManager.rollback(transact.status);
				getResponse().getWriter().write("Unable to process ");
				
			}
			txManager.commit(transact.status);
			result = "Updated successfully";
		}catch(Exception e){
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to continue the process");			
			e.printStackTrace();
			
		}
		getResponse().getWriter().write(result);
	}
	
	
	public String sendSMSToCustomer(){
		trace("Got requess sendSMSToCustomer....");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		IfpTransObj transact = commondesc.myTranObject("UPDSMS", txManager);
		String result = "";
		String wherecondition = "";
		try{
			String bulksmscode = getRequest().getParameter("bulksmscode");  
			String smscontent = getRequest().getParameter("bulksmscontent");  
			
			List smslist = this.getBulkSmsData(instid, bulksmscode, jdbctemplate );
			trace("Got sms record..."+ smslist );
			if( smslist == null || smslist.isEmpty()){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Records found");
				return "required_home";
			}
			
		 
			
			Iterator itr = smslist.iterator();
			while( itr.hasNext() ){
				Map mp =(Map)itr.next(); 
				String entityvalue= (String)mp.get("ENTITY_VALUE") ;
				JSONObject jsentitykey = new JSONObject (entityvalue); 
				Iterator itrentity = jsentitykey.keys();
				while( itrentity.hasNext() ){
					 String entitykey = (String)itrentity.next();
					 String value =  jsentitykey.getString(entitykey); 
					 
					 System.out.println("raw msg["+jsentitykey+"] key : " + entitykey + " value : " + value);
					 if( value.equals("ALL")){
						 continue;
					 }
					 
					 if( entitykey.equals("$BIN")){			
						 wherecondition += " AND BIN IN ('"+value+"')";
						}else if( entitykey.equals("$SUBPRODUCT")){
							wherecondition += " AND SUB_PROD_ID IN ( '"+value+"' )";
						}else if( entitykey.equals("$PRODUCT")){
							wherecondition += " AND PRODUCT_CODE IN ( '"+value+"' )";
						}else if( entitykey.equals("$MOBILE")){
							String mobstr = "";
							if( value.contains(",")){ 
								String mob[] = value.split(","); 
								for( int i=0; i<mob.length; i++){ 
									mobstr += comutil.formateMobile(mob[i])+","; 
								}
								
								if (mobstr.endsWith(",")) {
									mobstr = mobstr.substring(0, mobstr.length() - 1);
								}
								
							}else{
								mobstr = value;
							}
							
							wherecondition += " AND MOBILENO IN ( "+mobstr+" )";
						}else if( entitykey.equals("$CARD")){
							wherecondition += " AND CARD_NO IN ( '"+value+"' )";
						} 
				}
				
				String exceptvalue=  (String)mp.get("EXCEPT_VALUE") ;
				if( exceptvalue != null ){
					JSONObject jsexceptvalue = new JSONObject (exceptvalue); 
					Iterator itrexceptvalue = jsexceptvalue.keys();
					while( itrexceptvalue.hasNext() ){
						 String entitykey = (String)itrexceptvalue.next(); 
						 String value = jsexceptvalue.getString(entitykey);
						 if( value.equals("ALL")){
							 continue;
						 }
						 
						 if( entitykey.equals("$BIN")){							 
							 wherecondition += " AND BIN NOT IN ('"+value+"')";
							}else if( entitykey.equals("$SUBPRODUCT")){
								wherecondition += " AND SUB_PROD_ID NOT IN ( '"+value+"' )";
							}else if( entitykey.equals("$PRODUCT")){
								wherecondition += " AND PRODUCT_CODE NOT IN ( '"+value+"' )";
							}else if( entitykey.equals("$MOBILE")){
								value = comutil.formateMobile(value);
								wherecondition += " AND MOBILENO NOT IN ( '"+value+"' )";
							}else if( entitykey.equals("$CARD")){
								wherecondition += " AND CARD_NO NOT IN ( '"+value+"' )";
							}  
					}
				}
				
				trace("Where condition : " + wherecondition);
				System.out.println("Where condition : " + wherecondition);
				
				
				String mobilenoqry = "SELECT DISTINCT MOBILENO FROM CARD_PRODUCTION where inst_id='"+instid+"' "+ wherecondition;
				enctrace("mobilenoqry :"+mobilenoqry);
				List mobilenolist = jdbctemplate.queryForList(mobilenoqry);
				trace("Got mobile number ..."+ mobilenolist.size() );
				if( !mobilenolist.isEmpty() ){
					String mobilenumber = null;
					Iterator mobitr = mobilenolist.iterator();
					while( mobitr.hasNext() ){
						Map mobmp = (Map)mobitr.next();
						mobilenumber = (String)mobmp.get("MOBILENO") ;
						trace("Sending sms to " + mobilenumber);
						new SmsSender(instid, mobilenumber, smscontent, comutil);
					}
				}
				
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", mobilenolist.size() +" Sms sent successfully....");
				
			}
				
		
		}catch(Exception e){
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to continue the process");			
			e.printStackTrace();
			
		}
		return "required_home";
	}
	
	public int verifySmsContent(String instid, String smscode, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updqry = "UPDATE  IFP_BULKSMS SET MKCKSTATUS='P', CHECKERID='"+usercode+"', CHECKERDATE=SYSDATE  WHERE INST_ID='"+instid+"' AND BULKSMSCODE='"+smscode+"' ";
		enctrace("verifyqry :"+updqry);
		x = jdbctemplate.update(updqry);
		return x;
	}
	
	public int updateSMSContent(String instid, String smscode, String content, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updqry = "UPDATE  IFP_BULKSMS SET SMSCONTENT='"+content+"' WHERE INST_ID='"+instid+"' AND BULKSMSCODE='"+smscode+"' ";
		enctrace("updqry :"+updqry);
		x = jdbctemplate.update(updqry);
		return x;
	}
	
	public int insertBukSmsRecords(String instid, String smscode, String entitykey, String entityvalue, String subject, String smscontent, String usercode, String exceptionvalues, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String bulkqry="INSERT INTO IFP_BULKSMS(INST_ID,BULKSMSCODE,ENTITY,ENTITY_VALUE,SUBJECT,SMSCONTENT,SMSDATE,MAKERID,MKCKSTATUS,EXCEPT_VALUE) " ;
		bulkqry += " VALUES('"+instid+"','"+smscode+"','"+entitykey+"','"+entityvalue+"','"+subject+"','"+smscontent+"',sysdate,'"+usercode+"','M','"+exceptionvalues+"')";
		enctrace("bulkqry :"+ bulkqry );
		x = jdbctemplate.update(bulkqry);
		return x;
	}
	
	public List getBulkSmsList(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List smslist = null;
		String smslistqry = "SELECT * FROM IFP_BULKSMS WHERE INST_ID='"+instid+"' ";
		enctrace("smslistqry :"+smslistqry);
		smslist = jdbctemplate.queryForList(smslistqry);
		return smslist;
	}
	
	public List getBulkSmsData(String instid, String smscode, JdbcTemplate jdbctemplate  ) throws Exception {
		List smslist = null;
		String smslistqry = "SELECT ENTITY,ENTITY_VALUE,SUBJECT,SMSCONTENT, to_char(SMSDATE,'DD-MON-YY HH:mi:ss') as SMSDATE, MAKERID,MKCKSTATUS,  CHECKERID, EXCEPT_VALUE FROM IFP_BULKSMS WHERE INST_ID='"+instid+"' AND BULKSMSCODE='"+smscode+"' ";
		enctrace("smslistqry :"+smslistqry);
		smslist = jdbctemplate.queryForList(smslistqry);
		return smslist;
	}
	 
}
