package com.ifp.Action;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.RowSet;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


import com.ifp.beans.AuditBeans;
import com.ifp.dao.pinMailerConfigurationActiondDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
public class pinMailerConfigurationAction extends BaseAction {
	private static final long serialVersionUID = 1L; 
	 
 
	CommonUtil comutil= new CommonUtil(); 
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	pinMailerConfigurationActiondDAO pinmailerdao = new pinMailerConfigurationActiondDAO();
	AuditBeans auditbean = new AuditBeans();
	
	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}
	
	public String comUserCode( HttpSession session ){ 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public pinMailerConfigurationActiondDAO getPinmailerdao() {
		return pinmailerdao;
	}
	public void setPinmailerdao(pinMailerConfigurationActiondDAO pinmailerdao) {
		this.pinmailerdao = pinmailerdao;
	}

	static Boolean  initmail = true; 
	static  String parentid = "000";
	private  List<RowSet> pinmailerlist;
	
	private  List<RowSet> pinmailerdata;
	private  char pinmaildata_status;
	private  List<RowSet> pinmailsdata;
	private  String pinmailerid;

	private  char pinmail_update;
	private  char pinmail_delete;
	List pinmailername;
	public List getPinmailername() {
		return pinmailername;
	}
	public void setPinmailername(List pinmailername) {
		this.pinmailername = pinmailername;
	}
	public char getPinmail_delete() {
		return pinmail_delete;
	}

	public  void setPinmail_delete(char pinmail_delete1) {
		pinmail_delete = pinmail_delete1;
	}

	public char getPinmail_update() {
		return pinmail_update;
	}

	public  void setPinmail_update(char pinmail_update1) {
		pinmail_update = pinmail_update1;
	}

	public String getPinmailerid() {
		return pinmailerid;
	}

	public  void setPinmailerid(String pinmailerid1) {
		pinmailerid = pinmailerid1;
	}

	public List<RowSet> getPinmailsdata() {
		return pinmailsdata;
	}

	public  void setPinmailsdata(List<RowSet> pinmailsdata1) {
		pinmailsdata = pinmailsdata1;
	}

	public char getPinmaildata_status() {
		return pinmaildata_status;
	}

	public  void setPinmaildata_status(char pinmaildata_status1) {
		pinmaildata_status = pinmaildata_status1;
	}

	public List<RowSet> getPinmailerdata() {
		return pinmailerdata;
	}

	public  void setPinmailerdata(List<RowSet> pinmailerdata1) {
		pinmailerdata = pinmailerdata1;
	}
	private  char pinmailes_status;
	public  char getPinmailes_status() {
		return pinmailes_status;
	}

	public  void setPinmailes_status(char pinmailes_status1) {
		pinmailes_status = pinmailes_status1;
	}

	public List<RowSet> getPinmailerlist() {
		return pinmailerlist;
	}

	public  void setPinmailerlist(List<RowSet> pinmailerlist) {
		this.pinmailerlist = pinmailerlist;
	}
	CommonDesc commondesc = new CommonDesc(); 
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	
	public CommonUtil comUtil(){
		CommonUtil comutil= new CommonUtil();
		return comutil;
	} 
	
	public String addpinmailerdata() { 
		trace("***************Adding Pinmailer**********");
		enctrace("***************Adding Pinmailer**********");
		HttpSession session = getRequest().getSession();
		String act = getRequest().getParameter("act");
		trace("Got the mkck profile...: " + act );
		if( act != null ){
			session.setAttribute("act", act );
		}
		
		try { 
			/*** MAIL BLOCK ****/
			System.out.println( "initmail--" + initmail +" parentid :  " + this.parentid );  
			/*if( initmail ){
				HttpServletRequest req = getRequest();
				String menuid = comUtil().getUrlMenuId( req, jdbctemplate ); 
				if( !menuid.equals("NOREC")){
					System.out.println( "parentid--"+menuid);
					//session.setAttribute("URLMENUID", menuid);
					this.parentid = menuid;
				}else{
					//session.setAttribute("URLMENUID", "000");
					this.parentid = "000";
				}
				initmail = false;
			} */
			/*** MAIL BLOCK ****/
			
	/*String fieldname_qry="SELECT MAILER_NAME,MAILER_ID FROM GLOBAL_PINMAILER WHERE STATUS='1' ORDER BY ORDER_PREF";
	enctrace("fieldname_qry... "+fieldname_qry);
	List pinmailers = jdbctemplate.queryForList(fieldname_qry);*/
			
///by gowtham
			
			String fieldname_qry="SELECT MAILER_NAME,MAILER_ID FROM GLOBAL_PINMAILER WHERE STATUS=? ORDER BY ORDER_PREF";
			enctrace("fieldname_qry... "+fieldname_qry);
			List pinmailers = jdbctemplate.queryForList(fieldname_qry,new Object[]{"1"});
			
			trace("got the pinmailer count : "+ pinmailers.size());
			if(!(pinmailers.isEmpty()))	{
				setPinmailerlist(pinmailers);
				session.setAttribute("curerr","S");
			}else{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg"," No datas in IFP GLOBAL PINMAILER Found");
				trace(" No datas in IFP GLOBAL PINMAILER Found");
			}
		} catch (Exception e){
			e.printStackTrace();
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg"," Exception : ");
			trace(" Exception : "+e.getMessage());
			trace("\n"); 
			enctrace("\n");	
		} 	
		return "pinmailer";
	}
	
	public String pinmailerdatasave() {
		trace("*************** pinmailer save data  **********");
		enctrace("*************** pinmailer save data  **********");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("PINMAILERSAVE", txManager);
		String instid = (String)session.getAttribute("Instname");
		String usercode = comUserCode(session);
		String act =(String) session.getAttribute("act");
		trace("Processing....profile..." + act );
		
		
		//added by gowtham_230719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		String pname = "", authstatus = "", mkckstatus = "", authmsg = "",actiontype="";
		try	{ 
			pname = getRequest().getParameter("pinname").trim();
			String Doctype = getRequest().getParameter("doctype").trim();
			String pin_height = getRequest().getParameter("pinheight").trim();
			String pin_length = getRequest().getParameter("pinlen").trim();
			trace("Values Recived isssss "+pname+" "+Doctype+" "+pin_height+" "+pin_length);
			String parameters []=(getRequest().getParameterValues("p1"));
			trace(" parameter Length "+parameters.length);
			int nameexist = pinmailerdao.checkPinmailernameexist(instid,pname,jdbctemplate);
			if(nameexist > 0){
				session.setAttribute("preverr","E");
				session.setAttribute("prevmsg"," PIN MAILER NAME ALREADY EXIST "); 
				trace("PIN MAILER NAME ALREADY EXIST ");
				return addpinmailerdata();
			}
			trace("Getting pinmailerid.. ");
			int pinmailerid =  pinmailerdao.createPinmailer(instid,jdbctemplate);
			trace("pinmailerid.. "+pinmailerid); 
			
			int pindesc_insert = -1;
			if( act !=null  && act.equals("D")){
				authstatus = "1";
				mkckstatus  = "D";
				actiontype="IM";
				pindesc_insert = pinmailerdao.insertPinMailerDesc(instid, "PINMAILER_DESC", pinmailerid, pname, authstatus, mkckstatus, usercode, jdbctemplate);
				trace("Insert mailer desc main table...got : " + pindesc_insert);
				pindesc_insert = pinmailerdao.insertPinMailerDesc(instid, "PINMAILER_DESC", pinmailerid, pname, authstatus, mkckstatus, usercode, jdbctemplate);
				trace("Insert mailer desc temp table...got : " + pindesc_insert);
			}else{
				authstatus = "0";
				mkckstatus  = "M";
				actiontype="IC";
				authmsg = " .Waiting for authorization ";
				pindesc_insert = pinmailerdao.insertPinMailerDesc(instid, "PINMAILER_DESC", pinmailerid, pname, authstatus, mkckstatus, usercode, jdbctemplate);
				trace("Insert mailer desc temp table...got : " + pindesc_insert);
			}
			
			//String pindesc = "INSERT INTO PINMAILER_DESC (INST_ID,PINMAILER_ID,PINMAILER_NAME,STATUS,MKCK_STATUS,ADDED_BY,ADDED_DATE) VALUES('"+instid+"','"+pinmailerid+"','"+pname+"','"+authstatus+"','"+mkckstatus+"','"+usercode+"',SYSDATE)";
			//enctrace("pindesc :"+pindesc);
			//int pindesc_insert = jdbctemplate.update(pindesc);
			//trace("Inserting pin mailer description....got : " + pindesc_insert);
			int i,all_insert=0,parameters_cnt = parameters.length;
			trace("Processing...inserting mailer propertiles....values" + parameters_cnt);
			for(i=0;i<parameters_cnt;i++){
				int pindet_insert = 0;
				String field_length = "",fname = "",x_pos= "",y_pos= "";
				trace("Parameter.. "+parameters[i]);
				fname = parameters[i];
				String flength = "fieldlenth"+parameters[i];
				String xpos = "xpos"+parameters[i];
				String ypos = "ypos"+parameters[i];
				trace("flength    "+flength+" xpos     "+xpos+"   ypos     "+ypos);
				
				field_length = getRequest().getParameter(flength);
				x_pos = getRequest().getParameter(xpos);
				y_pos = getRequest().getParameter(ypos);
				trace("field_length    "+field_length+" x_pos     "+x_pos+"   y_pos     "+y_pos);
				
				/*String pinmailerdata_insert = "INSERT INTO PINMAILER_PROPERTY (PINMAILER_ID,INST_ID, " +
						"DOCUMENT_TYPE, FIELD_NAME, FIELD_LENGTH, X_POS, Y_POS, PRINT_REQUIRED,MAILER_HEIGHT,MAILER_LENGHT) VALUES ('"+pinmailerid+"', " +
						"'"+instid+"', '"+Doctype+"', '"+fname+"', '"+field_length+"', '"+x_pos+"', '"+y_pos+"', 'Y','"+pin_height+"','"+pin_length+"')";
				
				enctrace("pinmailerdata_insert.."+pinmailerdata_insert);*/
				if( act !=null  && act.equals("D")){
					pindet_insert = pinmailerdao.insertPinMailerProperties(instid, "PINMAILER_PROPERTY", Doctype, fname, field_length, x_pos, y_pos, pinmailerid, pin_height, pin_length, parameters, jdbctemplate);
					trace("Inserting mailer properties in temp table...got : " + pindet_insert );
					pindet_insert = pinmailerdao.insertPinMailerProperties(instid, "PINMAILER_PROPERTY", Doctype, fname, field_length, x_pos, y_pos, pinmailerid, pin_height, pin_length, parameters, jdbctemplate);
					trace("Inserting mailer properties in temp table...got : " + pindet_insert );
				}else{
					pindet_insert = pinmailerdao.insertPinMailerProperties(instid, "PINMAILER_PROPERTY", Doctype, fname, field_length, x_pos, y_pos, pinmailerid, pin_height, pin_length, parameters, jdbctemplate);
					trace("Inserting mailer properties in temp table...got : " + pindet_insert );
				}
				
				trace("Inserting [ "+fname+" ]....got : " +pindet_insert );
				if(pindet_insert == 1)	{
					all_insert = all_insert+1;
				}else {
					break;
				}
			}
			if(parameters_cnt == all_insert && pindesc_insert == 1) {
				transact.txManager.commit( transact.status );
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", "Pinmailer Configured Succesfully. "+authmsg);
				trace("Processed sucessfully....Committed success" ); 
				
				
				/*************AUDIT BLOCK**************/
				  try{
					  
					//added by gowtham_230719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);
					
					auditbean.setActmsg("Pin Mailer [ "+pname+" ] Configured "+ authmsg );
					auditbean.setUsercode(usercode);
					auditbean.setAuditactcode("30"); 
					commondesc.insertAuditTrail(instid, usercode, auditbean, jdbctemplate, txManager);
				 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
				 /*************AUDIT BLOCK**************/
				  
			}
			else
			{
				transact.txManager.rollback( transact.status );
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Exception While Insert ");
				trace("Could not process....roll backed successfully");
			}
	
			}
		catch (Exception e) {
			transact.txManager.rollback( transact.status );
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Exception While Insert ");
			trace("Exception While Insert "+e.getMessage());
			e.printStackTrace();			
			trace("\n\n"); 
			enctrace("\n\n");
		}

		/*** MAIL BLOCK ****/
		/*IfpTransObj transactmail = commondesc.myTranObject(); 
		try {
			String alertid = this.parentid; // (String) session.getAttribute("URLMENUID"); 
			if( alertid != null && ! alertid.equals("000")){
				String keymsg = "The pin mailer " +pname;
				int mail = comUtil().sendMail( instid, alertid, keymsg, jdbctemplate, session, getMailSender() );
				trace( "mail return.." + mail);
			}
			
		} catch (Exception e) {  e.printStackTrace(); }
		  finally{
			transactmail.txManager.commit(transactmail.status);
			trace("mail commit successfully");
		} */
		/*** MAIL BLOCK ****/
		

		trace("\n\n"); 
		enctrace("\n\n");
	    return "required_home";
	}
	
	
	public String viewMailerHome() {
		trace("*************** View Pinmailer begins**********");
		enctrace("*************** View Pinmailer begins**********");
		HttpSession session = getRequest().getSession();
		String in_name = (String)session.getAttribute("Instname"); 
		try {
		 
			String pineditviewdata = "SELECT  DISTINCT (PINMAILER_NAME) as MAILERNAME,PINMAILER_ID AS MAILERID FROM PINMAILER_DESC where inst_id='"+in_name+"' ORDER BY PINMAILER_ID";
			enctrace("pineditviewdata "+pineditviewdata);
			List pinmailers = jdbctemplate.queryForList(pineditviewdata);
			trace("Result from Pinmailer Data  "+ pinmailers.size());
			if(!(pinmailers.isEmpty()))	{
				setPinmailerlist(pinmailers);
				session.setAttribute("curerr","S");
			}else{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg"," No PInmailer Configuration Found");
			}
		} catch (Exception e){
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg"," Exception : while fetching pinmailer name");
			trace(" Exception : while fetching pinmailer name "+e.getMessage());
			e.printStackTrace();
		}  
		return "pinmailer_viewhome";		
	}
	
	public String viewpindata(){
		trace("*************** View Pinmailer Data **********");
		enctrace("*************** View Pinmailer Data **********");
		HttpSession session = getRequest().getSession();
		String in_name = (String)session.getAttribute("Instname");  
		String pin_id = getRequest().getParameter("mailerid").trim();
		trace("Pin Mailer Id selected is  "+ pin_id); 
		try{
			List pindata = pinmailerdao.getMailerDescFromTemp(in_name, pin_id, jdbctemplate);// jdbctemplate.queryForList(pinmailer_data);
			List pinsdata = pinmailerdao.getMailerIdDistinct(in_name, pin_id, jdbctemplate) ;// jdbctemplate.queryForList(pins_data);
			List pinmailername = pinmailerdao.getMailerProperties(in_name, pin_id, jdbctemplate) ;//jdbctemplate.queryForList(pinmailer_name);
			trace("Pin Mailer Data from DB " + pindata.size()+" Pins Data  "+pinsdata.size());
			if( !pindata.isEmpty() && !pinsdata.isEmpty()  )		{ 
				String username = "",authstatus="";
				ListIterator itr = pinmailername.listIterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					username = commondesc.getUserName(in_name, (String)mp.get("ADDED_BY"), jdbctemplate);
					if( username == null ){ username = "--" ;}
					mp.put("ADDED_BY", username);
					
					username = commondesc.getUserName(in_name, (String)mp.get("AUTH_BY"), jdbctemplate);
					if( username == null ){ username = "--" ;}
					mp.put("AUTH_BY", username);
					
					authstatus = (String)mp.get("STATUS");
					if(   authstatus.equals("0")) {
						authstatus = "Waiting for authoization";
					}else if(  authstatus.equals("9")){
						authstatus = "De-Authorized";
					}else if(  authstatus.equals("1")){
						authstatus = "Authorized";
					}
					mp.put("AUTH_CODE", authstatus); 
					itr.remove();
					itr.add(mp); 
				} 
				setPinmailerdata(pindata);
				setPinmailsdata(pinsdata);
				setPinmailerid(pin_id);
				setPinmailername(pinmailername);
				setPinmaildata_status('Y');
			}else {
				setPinmaildata_status('N');
			}
			trace("GET PINMAILER "+getPinmail_update()); 
			 
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception....could not able to continue the process " );
			trace("Exception....could not able to continue the process "+ e.getMessage() );
			e.printStackTrace();
		}
		return "pinmailer_viewdata";
	}
	
	public String pinmailerAuthHome() {
		trace("*************** Pin mailer auth home **********");
		enctrace("***************Pin mailer auth home  **********");
		HttpSession session = getRequest().getSession();
		String in_name = (String)session.getAttribute("Instname");
		String authprocess = getRequest().getParameter("dotype");
		trace("Got dotype : " + authprocess );
		
		try { 
			
		/*	String pineditviewdata = "SELECT  DISTINCT (PINMAILER_NAME) as MAILERNAME,PINMAILER_ID AS "
			+ "MAILERID FROM PINMAILER_DESC where inst_id='"+in_name+"' AND STATUS='0' ORDER BY PINMAILER_ID";
			enctrace("pineditviewdata "+pineditviewdata);
			List pinmailers = jdbctemplate.queryForList(pineditviewdata);
			*/
			
			
			//by gowtham
			String pineditviewdata = "SELECT  DISTINCT (PINMAILER_NAME) as MAILERNAME,PINMAILER_ID AS "
					+ "MAILERID FROM PINMAILER_DESC where inst_id=? AND STATUS=? ORDER BY PINMAILER_ID";
					enctrace("pineditviewdata "+pineditviewdata);
					List pinmailers = jdbctemplate.queryForList(pineditviewdata,new Object[]{in_name,"0"});
					
			trace("Result from Pinmailer Data  "+ pinmailers.size());
			if(!(pinmailers.isEmpty()))	{
				setPinmailerlist(pinmailers);
				session.setAttribute("curerr","S");
			}else{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg"," No records waiting for authorization");
				return "required_home";
			}
		} catch (Exception e){
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg"," Exception : while fetching pinmailer name");
			trace(" Exception : while fetching pinmailer name "+e.getMessage());			 
		}  
		return "pinmailer_authhome";	
	}
	
	public String authPinMailerData(){
		trace("*************** View Pinmailer Data **********");
		enctrace("*************** View Pinmailer Data **********");
		HttpSession session = getRequest().getSession();
		String in_name = (String)session.getAttribute("Instname");  
		String pin_id = getRequest().getParameter("mailerid").trim();
		trace("Pin Mailer Id selected is  "+ pin_id);
		//TO get FIELD_NAME,FIELD_LENGTH
		
		try{
			List pindata = pinmailerdao.getMailerDescFromTemp(in_name, pin_id, jdbctemplate);// jdbctemplate.queryForList(pinmailer_data);
			List pinsdata = pinmailerdao.getMailerIdDistinct(in_name, pin_id, jdbctemplate) ;// jdbctemplate.queryForList(pins_data);
			List pinmailername = pinmailerdao.getMailerProperties(in_name, pin_id, jdbctemplate) ;//jdbctemplate.queryForList(pinmailer_name);
			trace("Pin Mailer Data from DB " + pindata.size()+" Pins Data  "+pinsdata.size());
			if(! pindata.isEmpty() && !pinsdata.isEmpty() ) { 
				String username = "",authstatus="";
				ListIterator itr = pinmailername.listIterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					username = commondesc.getUserName(in_name, (String)mp.get("ADDED_BY"), jdbctemplate);
					if( username == null ){ username = "--" ;}
					mp.put("ADDED_BY", username); 
					username = commondesc.getUserName(in_name, (String)mp.get("AUTH_BY"), jdbctemplate);
					if( username == null ){ username = "--" ;}
					mp.put("AUTH_BY", username);
					
					authstatus = (String)mp.get("STATUS");
					if(   authstatus.equals("0")) {
						authstatus = "Waiting for authoization";
					}else if(  authstatus.equals("9")){
						authstatus = "De-Authorized";
					}else if(  authstatus.equals("1")){
						authstatus = "Authorized";
					}
					mp.put("AUTH_CODE", authstatus); 
					itr.remove();
					itr.add(mp); 
				} 
				setPinmailerdata(pindata);
				setPinmailsdata(pinsdata);
				setPinmailerid(pin_id);
				setPinmailername(pinmailername);
				setPinmaildata_status('Y');
			}else {
				setPinmaildata_status('N');
			}
			trace("GET PINMAILER "+getPinmail_update()); 
			 
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception....could not able to continue the process " );
			trace("Exception....could not able to continue the process "+ e.getMessage() );
			e.printStackTrace();
		}
		trace("\n\n"); 
		enctrace("\n\n");		
		return "pinmailerauthdetails";
	}
	
	public String pinMailerAuthorize() {
		trace("*************** edit pinmailer data **********");
		enctrace("*************** edit pinmailer data **********");  
		HttpSession session = getRequest().getSession();
		
		//added by gowtham_230719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		String i_Name = (String)session.getAttribute("Instname");
		String in_name = i_Name.toUpperCase(); 
		String pin_id = getRequest().getParameter("mailerid").trim();
		String usercode = comUserCode(session);
		trace("Pin Mailer Id selected is ===> "+ pin_id);
		 
		/*String fieldname_qry="SELECT MAILER_NAME,MAILER_ID FROM GLOBAL_PINMAILER WHERE STATUS='1' ORDER BY ORDER_PREF";
		enctrace("fieldname_qry "+fieldname_qry);
		List pinmailers = jdbctemplate.queryForList(fieldname_qry);*/
		
		///by gowtham
		String fieldname_qry="SELECT MAILER_NAME,MAILER_ID FROM GLOBAL_PINMAILER WHERE STATUS=? ORDER BY ORDER_PREF";
		enctrace("fieldname_qry "+fieldname_qry);
		List pinmailers = jdbctemplate.queryForList(fieldname_qry,new Object[]{"1"});
		
		trace("Result from GLOBAL_PINMAILER "+ pinmailers.size());
		IfpTransObj transact = commondesc.myTranObject("AUTHMAILER", txManager);
		try{ 
			String authbtn = getRequest().getParameter("authmailer");
			String deauthbtn = getRequest().getParameter("deauthmaier");
			String reason = "",actiontype="";
			trace("got request...authbtn [ "+authbtn+" ]....deauthbtn[ "+deauthbtn+" ] ");
			int result = -1;
			String authmsg = " Authorized ";
			if(  authbtn != null ){
				actiontype="IM";
				trace("Authorizing....");
				result = pinmailerdao.updateMailerDetails(in_name, pin_id, "", usercode, "1", jdbctemplate);
				/*trace("Updating authorizing status....got : " + result );
				//result = pinmailerdao.deleteFromMailerProduction(in_name, pin_id, jdbctemplate);
				//trace("Deleting existing mailer description table prodution ....got : " + result );
				result = pinmailerdao.moveMailerDescToProduction(in_name, pin_id, jdbctemplate);
				trace("Moving temp mailer description to production...got : " + result );
				//result = pinmailerdao.deleteFromMailerPropertiesProduction(in_name, pin_id, jdbctemplate);
				//trace("Deleting mailer properties from prodution....go : " + result );
				result = pinmailerdao.moveMailerPropertiesToProduction(in_name, pin_id, jdbctemplate);
				trace("Moving mailer properties to production...got : " + result );
		*/	}else{
				actiontype="IC";
				trace("De-Authorizing....");
				reason = getRequest().getParameter("reason");
				trace("Got reason ...: " + reason );
				result = pinmailerdao.updateMailerDetails(in_name, pin_id, reason, usercode, "9", jdbctemplate);
				trace("Updating authorizing status....got : " + result );
				authmsg = " De-Authorized ";
			}
			
			trace("Final resutl value..." + result );
			if( result > 0 ){
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", "Pin mailer "+authmsg+" successfully....");
				transact.txManager.commit(transact.status);
				trace("Committed....");
				
				
				/*************AUDIT BLOCK**************/ 
				  try{
					String pinmailername = pinmailerdao.getPinMailerName(in_name, pin_id, jdbctemplate);
					
					//added by gowtham_230719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);
					
					auditbean.setActmsg("Pin Mailer [ "+pinmailername+" ] "+ authmsg );
					auditbean.setUsercode(usercode);
					auditbean.setAuditactcode("30"); 
					auditbean.setRemarks(reason);
					commondesc.insertAuditTrail(in_name,usercode, auditbean, jdbctemplate, txManager);
				 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
				 /*************AUDIT BLOCK**************/
				  
				  
			}else{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Unable to continue the process");
				transact.txManager.rollback(transact.status);
				trace("Got rolled back....");
			}  
			 
		}catch(Exception e ){
			transact.txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception.....could not continue the process " );
			e.printStackTrace();
		} 
		return "required_home";
		
	}
			
	
	public String editMailerHome() {
		trace("*************** Pin mailer auth home **********");
		enctrace("***************Pin mailer auth home  **********");
		HttpSession session = getRequest().getSession();
		String in_name = (String)session.getAttribute("Instname");
		String authprocess = getRequest().getParameter("dotype");
		trace("Got dotype : " + authprocess );
		
		try { 
			
			/*String pineditviewdata = "SELECT  DISTINCT (PINMAILER_NAME) as MAILERNAME,PINMAILER_ID AS MAILERID FROM "
					+ "PINMAILER_DESC where inst_id='"+in_name+"'   ORDER BY PINMAILER_ID";
			enctrace("pineditviewdata "+pineditviewdata);
			List pinmailers = jdbctemplate.queryForList(pineditviewdata);*/
			
			//byg owtham
			String pineditviewdata = "SELECT  DISTINCT (PINMAILER_NAME) as MAILERNAME,PINMAILER_ID AS MAILERID FROM "
					+ "PINMAILER_DESC where inst_id=?   ORDER BY PINMAILER_ID";
			enctrace("pineditviewdata "+pineditviewdata);
			List pinmailers = jdbctemplate.queryForList(pineditviewdata,new Object[]{in_name});
			
			trace("Result from Pinmailer Data  "+ pinmailers.size());
			if(!(pinmailers.isEmpty()))	{
				setPinmailerlist(pinmailers);
				session.setAttribute("curerr","S");
			}else{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg"," No records available");
				return "required_home";
			}
		} catch (Exception e){
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg"," Exception : could not continue the process");
			trace(" Exception : while fetching pinmailer name "+e.getMessage());	
			e.printStackTrace();
		}  
		return "pinmailer_edithome";	
	}
	
	public String editMailerData() {
		trace("*************** Pin mailer auth home **********");
		enctrace("***************Pin mailer auth home  **********");
		HttpSession session = getRequest().getSession();
		String in_name = (String)session.getAttribute("Instname");
		String authprocess = getRequest().getParameter("dotype");
		trace("Got dotype : " + authprocess );
		
		try {
			
			/*String pineditviewdata = "SELECT  DISTINCT (PINMAILER_NAME) as MAILERNAME,PINMAILER_ID AS "
				+ "MAILERID FROM PINMAILER_DESC where inst_id='"+in_name+"'   ORDER BY PINMAILER_ID";
			enctrace("pineditviewdata "+pineditviewdata);
			List pinmailers = jdbctemplate.queryForList(pineditviewdata);*/
			
			//by gowtham
			String pineditviewdata = "SELECT  DISTINCT (PINMAILER_NAME) as MAILERNAME,PINMAILER_ID AS "
					+ "MAILERID FROM PINMAILER_DESC where inst_id=?  ORDER BY PINMAILER_ID";
				enctrace("pineditviewdata "+pineditviewdata);
				List pinmailers = jdbctemplate.queryForList(pineditviewdata,new Object[]{in_name});
			
			trace("Result from Pinmailer Data  "+ pinmailers.size());
			if(!(pinmailers.isEmpty()))	{
				setPinmailerlist(pinmailers);
				session.setAttribute("curerr","S");
			}else{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg"," No records available");
				return "required_home";
			}
		} catch (Exception e){
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg"," Exception : could not continue the process");
			trace(" Exception : while fetching pinmailer name "+e.getMessage());	
			e.printStackTrace();
		}  
		return "pinmailer_authhome";	
	}
	
	public String editpindata() {
		trace("*************** edit pinmailer data **********");
		enctrace("*************** edit pinmailer data **********");  
		HttpSession session = getRequest().getSession();
		String i_Name = (String)session.getAttribute("Instname");
		String in_name = i_Name.toUpperCase(); 
		String pin_id = getRequest().getParameter("mailerid").trim();
		String usercode = comUserCode(session);
		trace("Pin Mailer Id selected is ===> "+ pin_id);
		String autheanabled= getRequest().getParameter("autheanabled");
		String fieldname_qry="SELECT MAILER_NAME,MAILER_ID FROM GLOBAL_PINMAILER WHERE STATUS='1' ORDER BY ORDER_PREF";
		enctrace("fieldname_qry "+fieldname_qry);
		List pinmailers = jdbctemplate.queryForList(fieldname_qry);
		trace("Result from GLOBAL_PINMAILER "+ pinmailers.size());
		IfpTransObj transact = commondesc.myTranObject("AUTHMAILER", txManager);
		try{ 
			String authbtn = getRequest().getParameter("authmailer");
			String deauthbtn = getRequest().getParameter("deauthmaier");
			String reason = "";
			trace("got request...authbtn [ "+authbtn+" ]....deauthbtn[ "+deauthbtn+" ] ");
			int result = -1;
			String authmsg = " Authorized ";  
			
			if( ! pinmailers.isEmpty() ) {
				setPinmailerlist(pinmailers);
				session.setAttribute("curerr","S");
			}else	{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg"," No datas in GLOBAL_PINMAILER Found");
			}
			
			/*String pinmailer_data = "select FIELD_NAME,FIELD_LENGTH,X_POS,Y_POS,DECODE(PRINT_REQUIRED,'Y','YES') AS PRINT_REQUIRED from PINMAILER_PROPERTY where PINMAILER_ID='"+pin_id+"' and inst_id='"+in_name+"'";
			String pins_data = "SELECT DISTINCT PINMAILER_ID,DOCUMENT_TYPE,MAILER_HEIGHT,MAILER_LENGHT FROM PINMAILER_PROPERTY WHERE inst_id='"+in_name+"' AND PINMAILER_ID ='"+pin_id+"'";
			enctrace("pins_data "+pins_data);
			String pinmailer_name = "SELECT PINMAILER_NAME FROM PINMAILER_DESC WHERE INST_ID='"+in_name+"' AND PINMAILER_ID='"+pin_id+"'";
			List pindata = jdbctemplate.queryForList(pinmailer_data);
			List pinsdata = jdbctemplate.queryForList(pins_data);
			List pinmailername = jdbctemplate.queryForList(pinmailer_name);*/
			
			///by gowtham
			String pinmailer_data = "select FIELD_NAME,FIELD_LENGTH,X_POS,Y_POS,DECODE(PRINT_REQUIRED,'Y','YES') AS PRINT_REQUIRED from PINMAILER_PROPERTY where PINMAILER_ID=? and inst_id=? ";
			String pins_data = "SELECT DISTINCT PINMAILER_ID,DOCUMENT_TYPE,MAILER_HEIGHT,MAILER_LENGHT FROM PINMAILER_PROPERTY WHERE inst_id=? AND PINMAILER_ID =?";
			enctrace("pins_data "+pins_data);
			String pinmailer_name = "SELECT PINMAILER_NAME FROM PINMAILER_DESC WHERE INST_ID=? AND PINMAILER_ID=?";
			List pindata = jdbctemplate.queryForList(pinmailer_data,new Object[]{pin_id,in_name});
			List pinsdata = jdbctemplate.queryForList(pins_data,new Object[]{in_name,pin_id});
			List pinmailername = jdbctemplate.queryForList(pinmailer_name,new Object[]{in_name,pin_id});
			
			trace("Pin Mailer Data from DB " + pindata+" Pins Data  "+pinsdata);
			if( !pindata.isEmpty() && !pinsdata.isEmpty()  )	{
				setPinmailerdata(pindata);
				setPinmailsdata(pinsdata);
				setPinmailerid(pin_id);
				setPinmailername(pinmailername);
				setPinmaildata_status('Y');
			} else	{
				setPinmaildata_status('N');
			}
			trace("get pinmailer====> "+getPinmail_update());
			
		}catch(Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception....could not continue the process" );
			trace("Exception...." + e.getMessage() );
			e.printStackTrace();
		} 
		return "PinmailerEditDetails";
	}
	
	public String pinmailerdataEdit(){
		trace("*************** Update pinmailer Edit **********");
		enctrace("*************** Update pinmailer Edit **********");

		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("EDITPINMAILER", txManager);
		
		//added by gowtham_230719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		String act = (String)session.getAttribute("act");
		String usercode = comUserCode(session);
		try	{ 
			String instid = comInstId(session) ;
			String pname = getRequest().getParameter("pinname").trim();
			String Doctype = getRequest().getParameter("doctype").trim();
			String pin_height = getRequest().getParameter("pinheight").trim();
			String pin_length = getRequest().getParameter("pinlen").trim();
			trace("Values Recived isssss "+pname+" "+Doctype+" "+pin_height+" "+pin_length);
			String parameters []=(getRequest().getParameterValues("p1"));
			trace("parameters .."+parameters.length);
			String pinmailerid = getRequest().getParameter("mailerid").trim();
			trace("pinmailerid.. "+pinmailerid);
			
			/*String del_pinmailerdetails = "DELETE FROM PINMAILER_PROPERTY WHERE PINMAILER_ID='"+pinmailerid+"' and INST_ID='"+instid+"'";
			enctrace("del_pinmailerdetails.. "+del_pinmailerdetails);
			int delete_status = jdbctemplate.update(del_pinmailerdetails);*/
	
			
			//by vowtham
			String del_pinmailerdetails = "DELETE FROM PINMAILER_PROPERTY WHERE PINMAILER_ID=? and INST_ID=? ";
			enctrace("del_pinmailerdetails.. "+del_pinmailerdetails);
			int delete_status = jdbctemplate.update(del_pinmailerdetails,new Object[]{pinmailerid,instid});
			
			trace("delete_status.. "+delete_status);
			String authstatus="",mkckstatus="",authmsg = "",actiontype="";
			int result = -1;
			if( act !=null  && act.equals("D")){
				authstatus = "1";
				mkckstatus  = "D";
				actiontype="ID";
				trace("Authorizing....");
				result = pinmailerdao.updateMailerDetails(instid, pinmailerid, "", usercode, "1", jdbctemplate);
				trace("Updating authorizing status....got : " + result );
				result = pinmailerdao.deleteFromMailerProduction(instid, pinmailerid, jdbctemplate);
				trace("Deleting existing mailer description table prodution ....got : " + result );
				result = pinmailerdao.moveMailerDescToProduction(instid, pinmailerid, jdbctemplate);
				trace("Moving temp mailer description to production...got : " + result );
				result = pinmailerdao.deleteFromMailerPropertiesProduction(instid, pinmailerid, jdbctemplate);
				trace("Deleting mailer properties from prodution....go : " + result );
				result = pinmailerdao.moveMailerPropertiesToProduction(instid, pinmailerid, jdbctemplate);
				trace("Moving mailer properties to production...got : " + result );
			}else{
				authstatus = "0";
				mkckstatus  = "M";
				actiontype="IM";
				authmsg = ".Waiting for authorization ";
			} 
			
			if(delete_status != 0)	{	
				
				/*String pindesc = "UPDATE PINMAILER_DESC SET PINMAILER_NAME='"+pname+"',STATUS='"+authstatus+"',"
				+ "ADDED_BY='"+usercode+"',ADDED_DATE=SYSDATE,MKCK_STATUS='"+mkckstatus+"'  "
				+ "WHERE INST_ID='"+instid+"' AND PINMAILER_ID='"+pinmailerid+"'";
				enctrace("pindesc "+pindesc);
				int pindesc_update = jdbctemplate.update(pindesc);*/
				
				///by gowtham
				String pindesc = "UPDATE PINMAILER_DESC SET PINMAILER_NAME=?,STATUS=?,"
						+ "ADDED_BY=?,ADDED_DATE=SYSDATE,MKCK_STATUS=?  "
						+ "WHERE INST_ID=? AND PINMAILER_ID=?";
						enctrace("pindesc "+pindesc);
						int pindesc_update = jdbctemplate.update(pindesc,new Object[]{pname,authstatus,usercode,mkckstatus,instid,pinmailerid});
				
				trace("pindesc_update "+pindesc_update);
				int i,all_update=0,parameters_cnt = parameters.length;
				for(i=0;i<parameters_cnt;i++){
					int pindet_update = 0;
					String field_length = "",fname = "",x_pos= "",y_pos= "";
					trace("Parameter..   "+parameters[i]);
					fname = parameters[i];
					String flength = "fieldlenth"+parameters[i];
					String xpos = "xpos"+parameters[i];
					String ypos = "ypos"+parameters[i];
					trace("flength    "+flength+" xpos     "+xpos+"   ypos     "+ypos);
					
					field_length = getRequest().getParameter(flength);
					x_pos = getRequest().getParameter(xpos);
					y_pos = getRequest().getParameter(ypos);
					trace("field_length    "+field_length+" x_pos     "+x_pos+"   y_pos     "+y_pos);
					
					/*String pinmailerdata_update = "INSERT INTO PINMAILER_PROPERTY (PINMAILER_ID,INST_ID, " +
					"DOCUMENT_TYPE, FIELD_NAME, FIELD_LENGTH, X_POS, Y_POS, PRINT_REQUIRED,MAILER_HEIGHT,MAILER_LENGHT)"
					+ " VALUES ('"+pinmailerid+"', " +	"'"+instid+"', '"+Doctype+"', '"+fname+"', '"+field_length+"', '"+x_pos+"', '"+y_pos+"', 'Y','"+pin_height+"','"+pin_length+"')";
					enctrace("pinmailerdata_update "+pinmailerdata_update);
					pindet_update = jdbctemplate.update(pinmailerdata_update);*/

					////by gowtham
					String pinmailerdata_update = "INSERT INTO PINMAILER_PROPERTY (PINMAILER_ID,INST_ID, " +
							"DOCUMENT_TYPE, FIELD_NAME, FIELD_LENGTH, X_POS, Y_POS, PRINT_REQUIRED,MAILER_HEIGHT,MAILER_LENGHT)"
							+ " VALUES (?,?, ?,?,?,?,?,?,?,?)";
							enctrace("pinmailerdata_update "+pinmailerdata_update);
							pindet_update = jdbctemplate.update(pinmailerdata_update,new Object[]{pinmailerid,instid,Doctype,fname,field_length,x_pos,y_pos,"Y",pin_height,pin_length});
					
					if(pindet_update == 1)	{
						all_update = all_update+1;
					}else{
						break;
					}
				}
				
				if(parameters_cnt == all_update && pindesc_update == 1)
				{
					transact.txManager.commit( transact.status );
					session.setAttribute("preverr", "S");
					session.setAttribute("prevmsg", "Pinmailer Updated Succesfully"+authmsg);
					trace("Committed success" );
					
					/*************AUDIT BLOCK**************/ 
					  try{
						String pinmailername = pinmailerdao.getPinMailerName(instid, pinmailerid, jdbctemplate);
					
						//added by gowtham_230719
						trace("ip address======>  "+ip);
						auditbean.setIpAdress(ip);
						
						auditbean.setActmsg("Pin Mailer [ "+pinmailername+" ] Updated. "+ authmsg );
						auditbean.setUsercode(usercode);
						auditbean.setAuditactcode("30"); 
						commondesc.insertAuditTrail(instid, usercode, auditbean, jdbctemplate, txManager);
					 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
					 /*************AUDIT BLOCK**************/
				}
				else
				{
					transact.txManager.rollback( transact.status );
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Exception While Updating ");
					trace("roll backed successfully");
				}
			}
		}catch (Exception e){
			transact.txManager.rollback( transact.status );
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Exception While Updating ");
			trace("roll backed successfully");
			trace("Exception While Insert====>"+e.getMessage()); 
			e.printStackTrace();
		}
		trace("\n\n"); 
		enctrace("\n\n");
		return "required_home";
		
	}
	
	
}