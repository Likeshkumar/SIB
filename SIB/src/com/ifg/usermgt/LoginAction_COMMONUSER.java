package com.ifg.usermgt;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
//import com.ifp.util.ErrorClass;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
 
import com.ifp.util.ContextUpdater;
import com.ifp.util.IfpTransObj;
import com.ifg.Config.Licence.Licensemanager;
import com.ifp.util.PasswordHashing;



public class LoginAction_COMMONUSER extends BaseAction {
	PasswordHashing pswd_hash=new PasswordHashing();
	Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	HttpServletRequest request ; 
	private static String display1 ; 
	public  String getDisplay1() {
		return display1;
	}
	public void setDisplay1(String display1) {
		LoginAction_COMMONUSER.display1 = display1;
	}
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate(); 
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager(); 
	
	

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

 
	CommonDesc commondesc = new CommonDesc();
	
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public static List Instlist;
	public List getInstlist() 
	{
		return Instlist;
	}
	public void setInstlist(List Instlist1)
	{
		Instlist = Instlist1;
	}
	public String gettingInstitution()
	{
		//////System.out.println("Inside the Selecting INST function");
		List institutes;
		String allInst="select INST_ID from IFP_INSTITUTION order by PREFERENCE"; 
		institutes =jdbctemplate.queryForList(allInst);
		//System.out.println("Selected Instituion"+institutes);
		setInstlist(institutes);
		return "true";
	}
	
	
	String widthparams;
	public String getWidthparams() {
		return widthparams;
	}
	public void setWidthparams(String widthparams) {
		this.widthparams = widthparams;
	}
	
	
	public synchronized int updateUserDetails( String instid, String usrname, String passwdexpdate, JdbcTemplate jdbctemplate ) throws Exception {
		String updatefrstlogin="update IFP_USER_DETAILS set LOGINSTATUS = '1',FRSTLOGINDATE = to_char(sysdate,'DD-MON-YY'),FRSTLOGIN='L',FORCEPSWEXP='"+passwdexpdate+"'  where username='"+usrname+"' and INSTID='"+instid+"'";
		enctrace("updatefrstlogin :" + updatefrstlogin);
		int updatefrstloginres=jdbctemplate.update(updatefrstlogin);
		return updatefrstloginres;
	}
	
	public synchronized int updateUserDetails1( String instid, String usrname, JdbcTemplate jdbctemplate ) throws Exception {
		String updatefrstlogin="update IFP_USER_DETAILS set LOGINSTATUS = '1',FRSTLOGINDATE = to_char(sysdate,'DD-MON-YY'),FRSTLOGIN='L',FORCEPSWEXP='N'  where username='"+usrname+"' and INSTID='"+instid+"'";
		enctrace("updatefrstlogin :" + updatefrstlogin);
		int updatefrstloginres=jdbctemplate.update(updatefrstlogin);
		return updatefrstloginres;
	}
	
	public synchronized int updatePasswordExpired( String instid, String usrname, JdbcTemplate jdbctemplate ) throws Exception {
		String update_pswdexpry_flag="update IFP_USER_DETAILS set PASSWORDEXPIRYFLAG = '1' where username='"+usrname+"' and INSTID='"+instid+"'"; 
		enctrace("update_pswdexpry_flag :" + update_pswdexpry_flag);
		int updexp = jdbctemplate.update(update_pswdexpry_flag);
		return updexp;
	}
	
	 public synchronized int updateRetryCount( String instid, String usrname, JdbcTemplate jdbctemplate ) throws Exception {
		String updatequery="update IFP_USER_DETAILS set LOGINSTATUS = '1', RETRYCOUNT='0' where username='"+usrname+"' and INSTID='"+instid+"'";
		enctrace("updatequery :" + updatequery );
		int updexp = jdbctemplate.update(updatequery);
		return updexp;
	}
	
	public synchronized int updateUserBlock( String instid, String usrname, JdbcTemplate jdbctemplate ) throws Exception {
		String updateretrycount = "update IFP_USER_DETAILS set RETRYCOUNT =  RETRYCOUNT + 1,USERBLOCK = 1 where username='"+usrname+"' and INSTID='"+instid+"'";
		enctrace("updateretrycount :" + updateretrycount );
		int updateretcountres=jdbctemplate.update(updateretrycount);
		return updateretcountres;
	}
	
	public synchronized int updateRetryCountInc( String instid, String usrname, JdbcTemplate jdbctemplate ) throws Exception {
		String updateretrycount = "update IFP_USER_DETAILS set RETRYCOUNT =  RETRYCOUNT + 1 where username='"+usrname+"' and INSTID='"+instid+"'";
		trace("Update Retry Count Qury ==> "+updateretrycount);
		int updateretcountres=jdbctemplate.update(updateretrycount);
		return updateretcountres;
	}
	
	public synchronized int updateUserPassword( String inst_id, String USERID, String hashed_password, String sSalt, String new_force_password_date, JdbcTemplate jdbctemplate  ) throws Exception {
		String update_query = "UPDATE IFP_USER_DETAILS SET USERPASSWORD= '"+hashed_password+"',SALT_KEY='"+sSalt+"',RETRYCOUNT='0',FIRSTTIME ='0',FORCEPSWEXP="+new_force_password_date+",LOGINSTATUS = '0' WHERE  USERID='"+USERID+"' and INSTID='"+inst_id+"'";
		enctrace("update_query :" + update_query );
		int upd = jdbctemplate.update(update_query);
		return upd;
	}
		
	public String getLoginServiceURL( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		String serviceurl = null;
		try{
		String serviceurlqry = "SELECT LOGINSERVICEURL FROM IFP_INSTITUTION WHERE INST_ID='"+instid+"'";
		serviceurl = (String)jdbctemplate.queryForObject(serviceurlqry, String.class);
		}catch(Exception e){}
		return serviceurl;
	}
	
	public String checkInstitutionIsActive(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		String inststatus = null;
		try{
		String inststatusqry ="SELECT STATUS FROM IFP_INSTITUTION WHERE INST_ID='"+instid+"' ";
		enctrace("inststatusqry :" + inststatusqry );
		inststatus = (String)jdbctemplate.queryForObject(inststatusqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return inststatus;
	}
	
	@Transactional
	public String userTest()  throws Exception
	{
		trace("Login action Begins _COMMON**********"  + getRequest().getMethod() );
		enctrace("Login action Begins  **********" );

		
		
		
		
		List user_result;		
		String Sys_date1 = null,logintype = null,db_password=null,db_salt=null,gpswd=null,instna,usrname,password,instid;;
		Licensemanager licencemgr=new Licensemanager();
		Iterator iterator_userresult;
		int retrycount_val=0,LOGINSTATUS=0;
		HttpSession session = getRequest().getSession();
		
		if (  !getRequest().getMethod().equals("POST") ){
			session.setAttribute("message","Some issue with your request. Please login from homepage");
			return "invaliduser";
		}
		
		
		IfpTransObj transact = commondesc.myTranObject("LOGINTEST", txManager);
		 
	 
		
		
		session.invalidate();
		session = getRequest().getSession();
		//String Ip = getRequest().getLocalAddr();
		InetAddress IPAddress=InetAddress.getLocalHost();
		String Ip = IPAddress.getHostAddress();
		
		//System.out.println("IP of my system is := "+Ip);
		 
		//trace("#######################THE IP ADDRESS IS ################# "+Ip);
		trace("Request from : " + Ip);
		usrname= commondesc.escSql( getRequest().getParameter("username") );
		System.out.println("usrname === "+usrname);
		password=commondesc.escSql( getRequest().getParameter("pass") ).trim();
		logintype = ((getRequest().getParameter("logintype"))).trim();
		System.out.println("logintype  === "+logintype);
		//String extusername = getRequest().getParameter("extusername");
		String extusername = (getRequest().getParameter("username"));
		System.out.println("   extusername  === "+extusername );
		String appcode = (String) getRequest().getParameter("appcode");
		String PROFILEID  =  (String) getRequest().getParameter("profileid");
		
		session.setAttribute("APPLICATIONTYPE", "PREPAID");
		
		String current_date=currentServerDate(jdbctemplate);
		
		if(logintype.equals("admin"))
		{
			session.setAttribute("USERTYPE","SUPERADMIN");
			session.setAttribute("SS_USERNAME",usrname);
			session.setAttribute("adminpswd", password);
			session.setAttribute("EXTERNALUSER", extusername);
			
			return "adminpage";
		}
		else
		{
			instid = ((getRequest().getParameter("instname"))).trim().toUpperCase();
		 
			
			String instidstatus = this.checkInstitutionIsActive(instid, jdbctemplate);
			if( instidstatus ==null){
				session.setAttribute("message","Invalid institution");
				return "invaliduser";
			}
			
			
			getRequest().getHeader("VIA");
			String ipAddress = getRequest().getHeader("X-FORWARDED-FOR"); 
			trace(" The IP ADDERESS IS ======> "+ipAddress);
			if (ipAddress == null) 
			{
				trace( " IP ADDRESS is NULL ");
				ipAddress = getRequest().getRemoteAddr();
				trace( " ipAddress = request.getRemoteAddr() ===> "+ipAddress);
			} 
			String licence_check =licencemgr.checkfile(instid, commondesc);
			trace(" function file not exists   ::"+ licence_check);
			/*if(licence_check.equals("Exception"))
			{
				System.out.println(" function file not exists   ::"+ licence_check);
				return "Exception";
			}*/
			if(!(licence_check.equals("matched")))
			{
				return "loglicenceunsucess";
			}
			//--- Checking User Status 
			try
			{
				
				instidstatus = this.checkInstitutionIsActive(instid, jdbctemplate);
				if( instidstatus ==null){
					session.setAttribute("message","There are some issue with this institution. could not get current status");
					return "invaliduser";
				}
				
				
				
				if( instidstatus.equals("0")){
					session.setAttribute("message","Institution has been temporarily blocked by adminstrator");
					return "invaliduser";
				}
				
				if( instidstatus.equals("2")){
					session.setAttribute("message","Institution Not Available");
					return "invaliduser";
				}
				
				String query_getuserdatils="select * FROM IFP_USER_DETAILS where username ='"+usrname+"' and INSTID='"+instid+"'";
				enctrace("query_getuserdatils  ::"+query_getuserdatils);
				user_result =jdbctemplate.queryForList(query_getuserdatils);
			//	trace("user_result  ::"+user_result);
				if(user_result.isEmpty())
				{
					session.setAttribute("message","User Does Not Exist For Selected Institution");
					return "invaliduser";
				}
				else
				{
					int FIRSTTIME = -1;
					
					String FRSTLOGIN=null;
					String PSWREPEATCOUNT =null;
				    String FORCEPSWEXP = null;
				    String USERBLOCK=null;
				    String USERTYPE=null;
				    String USERID=null;
				    String username=null;
				    String branchcode="000";
				    String configip = "";
				    int USERSTATUS=-1;
				    String EXPIRYDATE=null,PASSWORDEXPIRYDATE=null;
				    int PASSWORDEXPIRYFLAG=-1;
				    String name=null,firstname=null,lastname=null;
				   	iterator_userresult = user_result.iterator();
				   	while(iterator_userresult.hasNext())
				   	{
				   		Map mapper_userdetails=(Map)iterator_userresult.next();
						Object FIRSTTIME_obj = (mapper_userdetails.get("FIRSTTIME"));
						String FIRSTTIME_str = FIRSTTIME_obj.toString();
						FIRSTTIME = Integer.parseInt(FIRSTTIME_str);
						//PROFILEID = (mapper_userdetails.get("PROFILEID")).toString();
						
						
						trace("Getting Profileid from master user db...got : " + PROFILEID );
						
						USERID =  (mapper_userdetails.get("USERID")).toString();
						username = (mapper_userdetails.get("USERNAME")).toString();
						lastlogin=((String)mapper_userdetails.get("LASTLOGIN"));
						setLastlogin((String)mapper_userdetails.get("LASTLOGIN"));
						USERBLOCK = (mapper_userdetails.get("USERBLOCK")).toString();
						firstname=((String)mapper_userdetails.get("FIRSTNAME"));
						lastname=((String)mapper_userdetails.get("LASTNAME"));
						name=firstname+" "+lastname;
						gpswd=((String)mapper_userdetails.get("USERPASSWORD"));
						System.out.println("FRSTLOGIN :" + FRSTLOGIN);
						FRSTLOGIN = ((String)mapper_userdetails.get("FRSTLOGIN"));
					    USERTYPE=((String)mapper_userdetails.get("USERTYPE"));
					    configip = (String)mapper_userdetails.get("IPADDRESS");
					    db_password=((String)mapper_userdetails.get("USERPASSWORD"));
					    db_salt=((String)mapper_userdetails.get("SALT_KEY"));
					    Object retrycount_obj=mapper_userdetails.get("RETRYCOUNT");
					    String retrycount =retrycount_obj.toString();
						retrycount_val=Integer.parseInt(retrycount);
						Object loginststatus_obj=mapper_userdetails.get("LOGINSTATUS");
						String loginststatus_s =loginststatus_obj.toString();
						LOGINSTATUS=Integer.parseInt(loginststatus_s);
						PSWREPEATCOUNT = ((String)mapper_userdetails.get("PSWREPEATCOUNT"));
						PSWREPEATCOUNT=PSWREPEATCOUNT.trim();
						FORCEPSWEXP = ((String)mapper_userdetails.get("FORCEPSWEXP"));
						FORCEPSWEXP=FORCEPSWEXP.trim();
						Object USERSTATUS_OBJ=mapper_userdetails.get("USERSTATUS");
						String USERSTATUS_s=USERSTATUS_OBJ.toString();
						USERSTATUS=Integer.parseInt(USERSTATUS_s);
						EXPIRYDATE=((String)mapper_userdetails.get("EXPIRYDATE"));
						PASSWORDEXPIRYDATE=((String)mapper_userdetails.get("PASSWORDEXPIRYDATE"));
						trace("============== Firstimte Login  "+FIRSTTIME);
						Object PASSWORDEXPIRYFLAG_OBJ=mapper_userdetails.get("PASSWORDEXPIRYFLAG");
						String PASSWORDEXPIRYDATE_s=PASSWORDEXPIRYFLAG_OBJ.toString();
						PASSWORDEXPIRYFLAG=Integer.parseInt(PASSWORDEXPIRYDATE_s);
						trace("  PASSWORDEXPIRYFLAG  "+PASSWORDEXPIRYFLAG);
						branchcode=((String)mapper_userdetails.get("BRANCHCODE"));
						trace("  BRANCHCODE  "+branchcode);
												    
				   	}
				   	

					
					//SECURITY CHECK // 
					String profileqry = "SELECT LOGINIPADDRESSREQUIRED, LOGINEXPIRYDATEREQUIRED, LOGINBRANCHCODEREQUIRED, USERPASSWORDREPEATABLE FROM IFP_USER_DETAILS WHERE INSTID='"+instid+"' AND USER_CODE='"+username+"'";
					Map securitymap = new HashMap();
					List profilesequrity = jdbctemplate.queryForList(profileqry);
					if( !profilesequrity.isEmpty() ){
						Iterator profileitr = profilesequrity.iterator();
						while( profileitr.hasNext() ){
							Map profilemp = (Map)profileitr.next();
							String loginip = (String)profilemp.get("LOGINIPADDRESSREQUIRED");
							if( loginip != null ){
								if( loginip.equals("1")){ securitymap.put("LOGINIP", "1"); }
								else{ securitymap.put("LOGINIP", "0"); }
							}else{securitymap.put("LOGINIP", "0");}
							
						}
					}
					
					if( securitymap.get("LOGINIP").equals("1") ){
						String getWay = getRequest().getHeader("VIA");
						trace("Gateway : " + getWay );
						 String remoteip = Ip;// getRequest().getHeader("X-FORWARDED-FOR");
						 trace("Proxy ip : " + remoteip );
						   if (remoteip == null) {  
							   remoteip = getRequest().getRemoteAddr();
							   trace("Remote  ip : " + remoteip );
						   }
						   
						  if( !remoteip.equals(configip)){
							    session.setAttribute("message","You are not allowed to login from this computer.....");
							  	trace("Configured ip address : " +configip );
							  	trace("User accessing from : " +remoteip );   
				   				trace("You are not allowed to login from this computer.");
				   				return "invaliduser"; 
						  }
					}
				   	
				   	boolean pswdcheck_result=checkPassword(db_password,db_salt,password,instid);
				   	if(pswdcheck_result)
				   	{
				   		//ContextUpdater ctu=new ContextUpdater();
				   		ContextUpdater cont=new ContextUpdater();
				   		CommonDesc common_desc_obj=new CommonDesc();
				   		trace("  branchcode, instid.toUpperCase()_______"+branchcode+" )))))))))))))) "+instid.toUpperCase());
			   			String branch_desc=common_desc_obj.genBranchDesc(branchcode, instid.toUpperCase(), jdbctemplate);
			   			trace("  branch desc  " + branch_desc );
			   			trace("  password matched  ");
			   			
			   			
			   			session.setAttribute("USERID",USERID);
			   			session.setAttribute("Instname",instid.toUpperCase());
			   			session.setAttribute("SS_USERNAME",username);	
			   			session.setAttribute("BRANCHCODE",branchcode);
			   			session.setAttribute("BRANCH_DESC",branch_desc);
			   	
			   			session.setAttribute("USERNAME",username);
			   			
			   			if(USERSTATUS==2)
			   			{
			   				session.setAttribute("message","User Is Deleted Please Contact Administrator");
			   				trace("User Is Deleted Please Contact Administrator");
			   				return "invaliduser"; 
			   			}
			   			if(USERSTATUS==0)
			   			{
			   				session.setAttribute("message","User Is Inactive. Please Contact Administrator");
			   				trace("User Is Inactive. Please Contact Administrator");
			   				return "invaliduser"; 
			   			}
			   			if(PASSWORDEXPIRYFLAG==1)
			   			{
			   				String passwordexpdate=dateFormatter(PASSWORDEXPIRYDATE);
   							int pswdexpiry=current_date.compareTo(passwordexpdate);   							
   							if( pswdexpiry >= 0 ){
   								trace("Password Expired. Please Contact Administrator..configured password expirted date : " + PASSWORDEXPIRYDATE + " differnt days: " + pswdexpiry);
   								session.setAttribute("message","Password Expired. Please Contact Administrator");   								
   								return "invaliduser"; 
   							}
			   			}
			   			trace("checking user blocked...got status : " + USERBLOCK );
			   			if(!USERBLOCK.equals("0"))
			   			{
			   				session.setAttribute("message","User Is Blocked. Please Contact Administrator");
			   				trace("User Is Blocked. Please Contact Administrator");
			   				return "invaliduser"; 
			   			}
			   			if(LOGINSTATUS==1)
			   			{
			   				trace("LOGINSTATUS  is  ------------ 1" );
			   				cont.removeUserFromContext(session);
			   				trace("AFTER DELETE ++++++++++++++++");
			   				cont.viewUesrMap(session);
				        	/* session.setAttribute("message","User Is Already Loged In/Not Logged Out Properly. Try Ulock User Link");
				        	 return "invaliduser"; */
				      	 }
			   			trace("LOGINSTATUS  is  ------------ 0" );
				         //ctu.addUserToContext(session);
			   			//HttpSession session=getRequest().getSession();
			   			
			   			//cont.viewcontextVariable(session);
			   			//System.out.println("\n CONTEXT after create variable +++++++++++++++++++ \n");
			   			//cont.createContextVariable(session);
			   			//cont.viewcontextVariable(session);
			   			cont.addUserToContext(session);
			   			trace("AFTER USER MAP LIST");
			   			cont.viewUesrMap(session);
			   			//cont.removeUserFromContext(session);
			   			//System.out.println("\n AFTER DELETE ++++++++++++++++ \n");
			   			//cont.viewUesrMap(session);
			   			
				        trace("First time flag [FIRSTTIME] : " + FIRSTTIME);
			   			if( FIRSTTIME == 1 )
			   			{
			   				return "changepasswordpage";
			   			}
			   			trace("First login flag [FRSTLOGIN] : " + FRSTLOGIN);
			   			if(FRSTLOGIN.equals("F"))
			   			{
			   				trace("FIrst TIme Linging Check F  ");
			   				if(!(PSWREPEATCOUNT.equals("N")))
			   				{
			   					trace(" INSIDE THE N FINDTION");
						 		String password_repeate_date="SELECT to_char(SYSDATE+'"+PSWREPEATCOUNT+"', 'DD-MON-YYYY') as systemdate FROM DUAL";
								Sys_date1 = (String)jdbctemplate.queryForObject(password_repeate_date, String.class);
								//String updatefrstlogin="update IFP_USER_DETAILS set LOGINSTATUS = '1',FRSTLOGINDATE = to_char(sysdate,'DD-MON-YY'),FRSTLOGIN='L',FORCEPSWEXP='"+Sys_date1+"'  where username='"+usrname+"' and INSTID='"+instid+"'";
								//enctrace("updatefrstlogin :" + updatefrstlogin);
				    			int updatefrstloginres= this.updateUserDetails(instid, usrname, Sys_date1, jdbctemplate);
				    			if( updatefrstloginres <= 0 ){
				    				//commondesc.rollbackTxn(jdbctemplate);
				    				transact.txManager.rollback(transact.status);
				    				session.setAttribute("preverr","E");session.setAttribute("premsg","Unable to login....");
				    				trace("Could not update ifp_user_details ......");
				    				return "required_home";
				    			}
				    			session.setAttribute("PROFILEID",PROFILEID);
							    session.setAttribute("lastlogin", lastlogin);
							    session.setAttribute("name", name);
							    String dept_id_qury="select DEPLOY_ID from IFP_INSTITUTION where INST_ID='"+instid.toUpperCase()+"'";
							    String depid = (String)jdbctemplate.queryForObject(dept_id_qury, String.class);
							    trace("deployid ++++++++++++++++++   "+depid);
							    session.setAttribute("deploy_id",depid);
							    if(USERTYPE.equals("A"))
							    {
							    	session.setAttribute("USERTYPE","INSTADMIN");
							    	trace("user TYPE A" );
							    }
							    if(USERTYPE.equals("B"))
							    {
							    	session.setAttribute("USERTYPE","BRANCHUSER");
							    	trace("user TYPE NORMAL" );
							    }
							    trace("if(!(PSWREPEATCOUNT.equals('N')))");
							    //return "sucess";
			   				}
			   				else
			   				{
					 			//String updatefrstlogin="update IFP_USER_DETAILS set LOGINSTATUS = '1',FRSTLOGINDATE = to_char(sysdate,'DD-MON-YY'),FRSTLOGIN='L',FORCEPSWEXP='N'  where username='"+usrname+"' and INSTID='"+instid+"'";
					 			//enctrace("updatefrstlogin :" + updatefrstlogin);
				    			int updatefrstloginres=this.updateUserDetails1(instid, usrname, jdbctemplate );
				    			if( updatefrstloginres <= 0 ){
				    				//commondesc.rollbackTxn(jdbctemplate);
				    				transact.txManager.rollback(transact.status);
				    				session.setAttribute("preverr","E");session.setAttribute("premsg","Unable to login....");
				    				trace("Could not update ifp_user_details ......");
				    				return "required_home";
				    			}
				    			session.setAttribute("PROFILEID",PROFILEID);
							    session.setAttribute("lastlogin", lastlogin);
							    session.setAttribute("name", name);
							    String dept_id_qury="select DEPLOY_ID from IFP_INSTITUTION where INST_ID='"+instid.toUpperCase()+"'";
			 					String depid = (String)jdbctemplate.queryForObject(dept_id_qury, String.class);
			 					trace("deployid ++++++++++++++++++   "+depid);
								session.setAttribute("deploy_id",depid);
							    if(USERTYPE.equals("A"))
							    {
							    session.setAttribute("USERTYPE","INSTADMIN");
							    trace("user TYPE A" );

							    }
							    if(USERTYPE.equals("B"))
							    {
							    	session.setAttribute("USERTYPE","BRANCHUSER");
							    	trace("user TYPE NORMAL" );
							    }
							    trace("Password Expirt NA =========> 1");
							    //return "sucess";
			   				}
			   			}
			   			if(FRSTLOGIN.equals("L"))
			   			{ 
			   				trace("User Loging SECOND TIME ");
			   				
			   				trace("currentServerDate returns  ==============="+current_date);
			   				if(!(EXPIRYDATE.equals("N")))
			   				{	
			   					trace("User Loging SECOND TIME ");
			   					EXPIRYDATE=dateFormatter(EXPIRYDATE);
			   					int comp_userexpiry=current_date.compareTo(EXPIRYDATE);
			   					trace("comp_userexpiry ::::::"+comp_userexpiry);
			   					if(comp_userexpiry <0)
			   					{	
			   						if(!(PASSWORDEXPIRYDATE.equals("N")))
			   						{
			   							PASSWORDEXPIRYDATE=dateFormatter(PASSWORDEXPIRYDATE);
			   							int comp_pswdexpiry=current_date.compareTo(PASSWORDEXPIRYDATE);
			   							if(comp_pswdexpiry>=0)
			   							{
			   								trace("Password has been expired. Please Contact Administrator");
			   								//String update_pswdexpry_flag="update IFP_USER_DETAILS set PASSWORDEXPIRYFLAG = '1' where username='"+usrname+"' and INSTID='"+instid+"'";
			   								int updexp = this.updatePasswordExpired(instid, usrname, jdbctemplate) ;
		   									if( updexp <= 0 ){
		   										//commondesc.rollbackTxn(jdbctemplate);
		   										transact.txManager.rollback(transact.status);
		   										session.setAttribute("preverr", "E");
		   										session.setAttribute("prevmsg", "Unable to update the password.");
		   										trace("Could not update the password...");
		   										return "required_home";
		   									}
			   								 
		   									//commondesc.commitTxn(jdbctemplate);
		   									transact.txManager.commit(transact.status);
			   								session.setAttribute("message","Password has been expired. Please Contact Administrator"); 
			   								return "invaliduser";
			   							}
			   						}
			   						if(!(FORCEPSWEXP.equals("N")))
			   						{
			   							FORCEPSWEXP = dateFormatter(FORCEPSWEXP);
			   							int comp_forcepwd =current_date.compareTo(FORCEPSWEXP);
			   							trace("FORCEPSWEXP " +FORCEPSWEXP);
			   							trace("current_date "+current_date );
			   							if(comp_forcepwd>=0)
			   							{
			   								trace(" force password date exceded go to change password");
			   								return "forcepasswordpage";																					
			   							}
			   						}
			   						String dept_id_qury="select DEPLOY_ID from IFP_INSTITUTION where INST_ID='"+instid.toUpperCase()+"'";
			   						String depid = (String)jdbctemplate.queryForObject(dept_id_qury, String.class);
			   						trace("deployid : "+depid);
			   						session.setAttribute("deploy_id",depid);
			   						//String updatequery="update IFP_USER_DETAILS set LOGINSTATUS = '1', RETRYCOUNT='0' where username='"+usrname+"' and INSTID='"+instid+"'";
			   						//enctrace("updatequery :  "+updatequery);
			   						int updateres=this.updateRetryCount(instid, usrname, jdbctemplate);
			   						if( updateres <= 0 ){
					    				//commondesc.rollbackTxn(jdbctemplate);
			   							transact.txManager.rollback(transact.status);
					    				session.setAttribute("preverr","E");session.setAttribute("premsg","Unable to login....");
					    				trace("Could not update ifp_user_details ......");
					    				return "required_home";
					    			}
			   						trace("updateres ++++++++++++++++++   "+updateres);
			   						session.setAttribute("PROFILEID",PROFILEID);
			   						session.setAttribute("lastlogin", lastlogin);
			   						session.setAttribute("name", name);
			   						if(USERTYPE.equals("A"))
			   						{
			   							session.setAttribute("USERTYPE","INSTADMIN");
			   							trace("user TYPE A" );
			   						}
			   						if(USERTYPE.equals("B"))
			   						{
			   							session.setAttribute("USERTYPE","BRANCHUSER");
			   							trace("user TYPE NORMAL" );
			   						}
			   						//return "sucess";
			   					}
			   					else
			   					{
			   						session.setAttribute("message","User Expired"); 
			   						trace("User Expired");
			   						return "invaliduser";
			   					}
			   				}
			   				if((EXPIRYDATE.equals("N")))
			   				{												 					
			   					if(!(PASSWORDEXPIRYDATE.equals("N")))
			   					{
			   						PASSWORDEXPIRYDATE=dateFormatter(PASSWORDEXPIRYDATE);
			   						int comp_pswdexpiry=current_date.compareTo(PASSWORDEXPIRYDATE);
			   						if(comp_pswdexpiry>=0)
			   						{
			   							trace("password has been expired. Please Contact Administrator");
			   							//String update_pswdexpry_flag="update IFP_USER_DETAILS set PASSWORDEXPIRYFLAG = '1' where username='"+usrname+"' and INSTID='"+instid+"'";
			   							int psflg = this.updatePasswordExpired(instid, usrname, jdbctemplate);
			   							if( psflg  <= 0 ){
						    				//commondesc.rollbackTxn(jdbctemplate);
			   								transact.txManager.rollback(transact.status);
						    				session.setAttribute("preverr","E");session.setAttribute("premsg","Unable to login....");
						    				trace("Could not update ifp_user_details ......");
						    				return "required_home";
						    			}
			   							//commondesc.commitTxn(jdbctemplate);
			   							transact.txManager.commit(transact.status);
			   							session.setAttribute("message","password has been expired. Please Contact Administrator"); 
			   							return "invaliduser";
			   						}
			   					}
			   					if(!(FORCEPSWEXP.equals("N")))
			   					{
			   						FORCEPSWEXP = dateFormatter(FORCEPSWEXP);
			   						int comp_forcepwd =current_date.compareTo(FORCEPSWEXP);
			   						trace("FORCEPSWEXP " +FORCEPSWEXP);
			   						trace("current_date "+current_date );
			   						if(comp_forcepwd>=0)
			   						{
			   							trace(" force password date exceded go to change password");
			   							return "forcepasswordpage";																			
			   						}
			   					}
			   					//String updatequery="update IFP_USER_DETAILS set LOGINSTATUS = '1', RETRYCOUNT='0' where username='"+usrname+"' and INSTID='"+instid+"'";
			   					//enctrace("updatequery :" + updatequery );
			   					int updateres=this.updateRetryCount(instid, usrname, jdbctemplate);
			   					if( updateres  <= 0 ){
				    				//commondesc.rollbackTxn(jdbctemplate);
			   						transact.txManager.rollback(transact.status);
				    				session.setAttribute("preverr","E");session.setAttribute("premsg","Unable to login....");
				    				trace("Could not update ifp_user_details ......");
				    				return "required_home";
				    			}
			   					
			   					session.setAttribute("PROFILEID",PROFILEID);
			   					session.setAttribute("lastlogin", lastlogin);
			   					session.setAttribute("name", name);
			   					String dept_id_qury="select DEPLOY_ID from IFP_INSTITUTION where INST_ID='"+instid.toUpperCase()+"'";
			   					String depid = (String)jdbctemplate.queryForObject(dept_id_qury, String.class);
			   					trace("deployid ++++++++++++++++++   "+depid);
			   					session.setAttribute("deploy_id",depid);
			   					if(USERTYPE.equals("A"))
			   					{
			   						session.setAttribute("USERTYPE","INSTADMIN");
			   						trace("user TYPE A" );
			   					}
			   					if(USERTYPE.equals("B"))
			   					{
			   						session.setAttribute("USERTYPE","BRANCHUSER");
			   						trace("user TYPE NORMAL" );
			   					} 
			   				}
			   			}
										         
			   			String settings_qry = "SELECT * FROM IFP_USER_SETTINGS WHERE INST_ID='"+instid+"' AND USERID='"+USERID+"'";
			   			enctrace( "SETTINGS_QRY__" + settings_qry);
			   			List settings_list = jdbctemplate.queryForList(settings_qry);
			   			String datefilterreq = "1" ;
			   			if( ! settings_list.isEmpty() )
			   			{
			   				Iterator itr = settings_list.iterator();
			   				while( itr.hasNext() )
			   				{
			   					Map mp = ( Map ) itr.next();
			   					datefilterreq = (String)mp.get("DATE_FILTER");
			   				} 
			   			}
			   			
			   			session.setAttribute("EXTERNALUSER", USERID);
			   			
			   			session.setAttribute("DATEFILTER_REQ", datefilterreq);
						String width_parameters = (String) getRequest().getParameter("widthparams");
			   			session.setAttribute("SCREEN_ATTR",width_parameters);
			   			trace("Checking Lisence Data");
			   			long checklic = licencemgr.checkLicenceDate(instid, common_desc_obj, session);
			   			trace("Checked Lisence Data "+checklic);
			   			 
			   			//commondesc.commitTxn(jdbctemplate); 
			   			 
			   			if( checklic != 0 ){
			   				if( checklic == 1 ){
			   					return "expired";
			   				}else if( checklic == 2 ){
			   					transact.txManager.commit(transact.status);
			   					return "expiry_warning";
			   				}
			   			}
			   			
			   			transact.txManager.commit(transact.status);
			   			return "sucess";
				   	}
				   	else
				   	{
				   		trace("Password MISMACT === 1");
				   		//password not matched 
				   		if(USERSTATUS==2)
				   		{
				   			trace(" User Status is Deleted === 2");
				   			session.setAttribute("message","User Already Deleted From The System "); 
				   			return "invaliduser"; 
				   		}
				   		String loginretrycount="select LOGIN_RETRY_CNT from IFP_INSTITUTION where INST_ID ='"+instid+"'";
				   		int loginretrycount_val = jdbctemplate.queryForInt(loginretrycount);
				   		trace(" LOGIN RETRY COUNT FOR THIS INSTITUTION IS ===> "+loginretrycount_val);
				   		
				   		if(loginretrycount_val == 0)
				   		{
				   			trace(" Login Retyr Count is 0 ");
				   			session.setAttribute("message"," Wrong Password, Please Try With Correct Password "); 
				   			trace(" Wrong Password, Please Try With Correct Password ");
				   			return "invaliduser";
				   		}
				   		
				   		if(retrycount_val == loginretrycount_val)
				   		{
				   			trace(" Login Retyr Count is if(retrycount_val == loginretrycount_val) ");
				   			session.setAttribute("message"," User Blocked,Please Contact Administrator "); 
				   			trace(" User Blocked,Please Contact Administrator ");
				   			return "invaliduser";
				   		}
				   		
				   		if( (retrycount_val+1) >= loginretrycount_val )
				   		{
				   			trace(" if( (retrycount_val+1) >= loginretrycount_val ) ");
				   			//String updateretrycount = "update IFP_USER_DETAILS set RETRYCOUNT =  RETRYCOUNT + 1,USERBLOCK = 1 where username='"+usrname+"' and INSTID='"+instid+"'";
				   			
				   			int updateretcountres= this.updateUserBlock(instid, usrname, jdbctemplate);
				   			if( updateretcountres  <= 0 ){
			    				//commondesc.rollbackTxn(jdbctemplate);
				   				transact.txManager.rollback(transact.status);
			    				session.setAttribute("preverr","E");session.setAttribute("premsg","Unable to login....");
			    				trace("Could not update ifp_user_details ......");
			    				return "required_home";
			    			}
				   			
				   			//trace("updateretrycount ==> "+updateretrycount+"updateretcountres=====> "+updateretcountres);
				   			/*updateretrycount = "update IFP_USER_DETAILS set USERBLOCK = 1 where username='"+usrname+"' and INSTID='"+instid+"'";
				   			updateretcountres=jdbctemplate.update(updateretrycount);*/
				   			trace("User is Blocked ==== 3 ");		   			
				   			session.setAttribute("message","User Blocked"); 
				   		
				   		}
				   		else
				   		{
				   			trace(" Login Retry Count For this inst not mached Allow for next attempt ");
				   			//String updateretrycount = "update IFP_USER_DETAILS set RETRYCOUNT =  RETRYCOUNT + 1 where username='"+usrname+"' and INSTID='"+instid+"'";
				   			//trace("Update Retry Count Qury ==> "+updateretrycount);
				   			int updateretcountres=this.updateRetryCountInc(instid, usrname, jdbctemplate);
				   			if( updateretcountres  <= 0 ){
			    				//commondesc.rollbackTxn(jdbctemplate);
				   				transact.txManager.rollback(transact.status);
			    				session.setAttribute("preverr","E");session.setAttribute("premsg","Unable to login....");
			    				trace("Could not update ifp_user_details ......");
			    				return "required_home";
			    			}
				   			
				   			int loginretrycount_nextval = loginretrycount_val - 2 ;
				   			if (loginretrycount_nextval == retrycount_val )
				   			{
				   				trace("User will be blocked in next attempt == 4");
				   				session.setAttribute("message","User will be blocked in next attempt"); 
				   			}
				   			else
				   			{
				   				if(retrycount_val==0)
				   				{
				   					trace(" if(retrycount_val==0) === 5 ");
				   					session.setAttribute("message","User Name/Password mismatch");
				   					trace("User Name/Password mismatch");
				   				}
				   				else
				   				{
				   					trace(" Else Part Of if(retrycount_val==0) === 6 ");
				   					String display=" "+(retrycount_val+1)+ "Time User Name/Password mismatch";
				   					session.setAttribute("message",display); 
				   				}
				   			} 
				   		}
				   		
				   	}
				   	transact.txManager.commit(transact.status);
				   	//commondesc.commitTxn(jdbctemplate);
				}
				
			}
			catch (Exception e)
			{
				//commondesc.rollbackTxn(jdbctemplate);
				transact.txManager.rollback(transact.status);
				trace("  Exception :Error While Login  "+e.getMessage());
				session.setAttribute("message"," Error While Login " +e.getMessage()); 
				e.printStackTrace();
				trace("\n\n"); 
				enctrace("\n\n");
				 
			} 
			return "invaliduser";
		}

	}
	
	public String continueWithExpiry(){
		CommonDesc common_desc = new CommonDesc();
		Licensemanager licencemgr=new Licensemanager();
		HttpSession session = getRequest().getSession(); 
		String instid = (String)session.getAttribute("Instname");
		long checklic = licencemgr.checkLicenceDate(instid, common_desc, session);
		if( checklic == 1 ){
				return "expired";
		}
		return "sucess";
	}
	public String forcePasswordChange() 
	{		
		PasswordHashing pswd_hash_class=new PasswordHashing();
		HttpSession session = getRequest().getSession();
	 
		IfpTransObj transact1 = commondesc.myTranObject("FORCEPASSWORD", txManager);
		
		
		Iterator iterator_userresult;
		LoginAction_COMMONUSER loginActionClass=new LoginAction_COMMONUSER();
		String oldpassword;String newpassword,PSWREPEATCOUNT=null,new_force_password_date;
		oldpassword = (getRequest().getParameter("oldpassword"));
		newpassword = (getRequest().getParameter("newpassword"));
		String user_name=(String)session.getAttribute("SS_USERNAME");
		String USERID=(String)session.getAttribute("USERID");
		String inst_id=(String)session.getAttribute("Instname"),db_password=null,db_salt=null;
		inst_id=inst_id.trim();
		String query_getuserdatils="select * FROM IFP_USER_DETAILS where username ='"+user_name+"' and INSTID='"+inst_id+"'";
		try
		{
			List user_result =jdbctemplate.queryForList(query_getuserdatils);
			System.out.println("password change function  ::"+user_result);
			iterator_userresult = user_result.iterator();
			while(iterator_userresult.hasNext())
			{
				Map mapper_userdetails=(Map)iterator_userresult.next();
				db_password=((String)mapper_userdetails.get("USERPASSWORD"));
				db_salt=((String)mapper_userdetails.get("SALT_KEY"));
				PSWREPEATCOUNT=((String)mapper_userdetails.get("PSWREPEATCOUNT"));
			}
			boolean pswdcheck_result=loginActionClass.checkPassword(db_password,db_salt,oldpassword,inst_id);
			if(pswdcheck_result)
			{
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				// Salt generation 64 bits long
				byte[] bSalt = new byte[8];
				random.nextBytes(bSalt);		
				//Digest computation
				byte[] bDigest = pswd_hash_class.getHash(pswd_hash_class.ITERATION_NUMBER,newpassword,bSalt);
				//convert byte Digest to Base64 String
				String hashed_password = pswd_hash_class.byteToBase64(bDigest);
				String sSalt = pswd_hash_class.byteToBase64(bSalt);
				boolean chck_newpaswd_oldpswd=loginActionClass.checkPassword(db_password,db_salt,newpassword,inst_id);
				if((chck_newpaswd_oldpswd))
				{
					setForcepasswordchangeresult("New Password Cannot Be Same As Old Password");
					return "forcepasswordsucess"; 
				}
				else
				{
					String query_new_force_password_date=" to_char(SYSDATE+"+PSWREPEATCOUNT+", 'DD-MON-YYYY')";  
					//String update_query = "UPDATE IFP_USER_DETAILS SET USERPASSWORD= '"+hashed_password+"',SALT_KEY='"+sSalt+"',RETRYCOUNT='0',FIRSTTIME ='0',FORCEPSWEXP='"+new_force_password_date+"',LOGINSTATUS = '0' WHERE  USERID='"+USERID+"' and INSTID='"+inst_id+"'";
					//enctrace("update_query :" + update_query );
					int upd = this.updateUserPassword(inst_id, USERID, hashed_password, sSalt, query_new_force_password_date, jdbctemplate);
					if( upd <= 0 ){
						txManager.rollback(transact1.status);
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Unable to update the password.");
						trace("Could not update the password...");
					}
					 
					setForcepasswordchangeresult("Password has been Changed Sucessfully"); 
				}
			}
			else
			{
				setForcepasswordchangeresult(" Old Password Is Wrong ");
				
			}
			
			txManager.commit(transact1.status);
		}
		catch (Exception e) 
		{
			txManager.rollback(transact1.status);
			System.out.println("Error while force password cahge time "+e);
			setForcepasswordchangeresult("Unable to continue the process");
			trace("Exception...." + e.getMessage());
			e.printStackTrace();
			return "forcepasswordsucess"; 
		}
		return "forcepasswordsucess"; 
	}
	 
	private String forcepasswordchangeresult;
	public String getForcepasswordchangeresult() 
	{
		return forcepasswordchangeresult;
	}
	public void setForcepasswordchangeresult(String forcepasswordchangeresult) {
		this.forcepasswordchangeresult = forcepasswordchangeresult;
	}
	
	
	public String dateFormatter(String date_value)
	{
		System.out.println("Date Recieved is "+date_value);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Date tt=new Date(date_value);
		String formatted_date = dateFormat.format(tt);
		System.out.println("formatted_date ----------> "+formatted_date);
		return formatted_date;
	}
	public String currentServerDate(JdbcTemplate jdbctemplate)
	{  
	    String crntdateqry="SELECT to_char(SYSDATE+'"+0+"','YYYY-MM-DD') as currentsys FROM DUAL";   
		String crntsysdate = (String)jdbctemplate.queryForObject(crntdateqry, String.class);
		System.out.println("crntsysdate-----------------> "+crntsysdate);
		return 	crntsysdate;
	}
	public Boolean checkPassword(String db_password,String db_salt,String entered_pswd,String instid) throws IOException, NoSuchAlgorithmException
	{
		byte[] bt_db_password = pswd_hash.base64ToByte(db_password);
		byte[] bt_db_salt = pswd_hash.base64ToByte(db_salt);
		//System.out.println("Data base password ::: "+db_password);
		//System.out.println("Data base salt ::: "+db_salt);
		//System.out.println("Data byte password ::: "+bt_db_password);
		//System.out.println("Data byte salt ::: "+bt_db_salt);
		//System.out.println("Data byte entered_pswd ::: "+entered_pswd);
		//Compute the new password with byte[] salt from db 
		byte[] proposedpassword = pswd_hash.getHash(pswd_hash.ITERATION_NUMBER, entered_pswd, bt_db_salt);
		System.out.println("Arrays.equals(proposedDigest, db_password) :"+Arrays.equals(proposedpassword, bt_db_password));
		return Arrays.equals(proposedpassword, bt_db_password) ;
	}
	
	String temp;
	public String crtPrntLst()
 	{   
 		System.out.println("***Inst User Entering Clearprnt *** ");
 		List rsult;String menuid=null;String mainmenuid = null;
 		List<List> mstrmenuqryrsult =new ArrayList<List>();
 		List<List> mstrmenuqryrsultenable =new ArrayList<List>();
 		List<List> mstrmenuqryrsultdisable =new ArrayList<List>();
 		HttpSession session = getRequest().getSession();
 		String PROFILEID =(String)session.getAttribute("PROFILEID");
 		String PROFILE_ID = PROFILEID.toUpperCase();
 		String menulist="select MENU_LIST from IFPROFILE_PRIVILEGE  where PROFILE_ID = '"+PROFILE_ID+"' ";
 		List res= jdbctemplate.queryForList(menulist);
        Iterator itr = res.iterator();
        String proflist =null; 
        System.out.println("Submenu query" +menulist);
		while(itr.hasNext()){ 
			Map map = (Map)itr.next(); 
			proflist = (map.get("MENU_LIST")).toString(); 
		}

		int len=proflist.length();

		temp=(proflist.subSequence(1, len-1)).toString();

		session.setAttribute("profilelist", temp);

		setProfilelist(temp);

        String masterqury="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and parentid='0' ORDER BY MENUORDER ASC";

		List mstrqryrsult = jdbctemplate.queryForList(masterqury);

		List rsult1=new ArrayList<List>();

		Iterator mstrqryitr = mstrqryrsult.iterator();

		mstrmenuqryrsult.clear();

		mstrmenuqryrsultenable.clear();

		mstrmenuqryrsultdisable.clear();

		while(mstrqryitr.hasNext())

		{

			Map map = (Map)mstrqryitr.next();

			mainmenuid = (map.get("MENUID")).toString();

			menuid = ", "+mainmenuid+",";

			rsult1.clear();

			if(proflist.contains(menuid))

			{

				String mastermenuqury="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and MENUID='"+mainmenuid+"' ORDER BY MENUORDER ASC";

				rsult1 = jdbctemplate.queryForList(mastermenuqury);

			}

			mstrmenuqryrsult.addAll(rsult1);

		}

		session.setAttribute("menulist", mstrmenuqryrsult);

		System.out.println("***Inst user End Clearprnt *** "+mstrmenuqryrsult);

		return null;

	}

	
	
	private String profilelist;
	public String getProfilelist() 
	{
		return profilelist;
	}
	public void setProfilelist(String profilelist) 
	{
		this.profilelist = profilelist;
	}
	
	
	public void subMenuList() 

	{

		System.out.println("*** Inst user Entering submenulist *** ");

		List<List> submenulist;

		List<List> submenuqryrsult =new ArrayList<List>();

		String proflist=temp;

		String menu1 =getMenuid();

		String temp_menu=", "+menu1+",";

		//System.out.println("proflist ++++++++++++++++++++++++;  "+temp);	

		////System.out.println("menuid  :  "+menu1);	

		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String submenuid;

		if(proflist.contains(temp_menu))

	    {

			//////System.out.println("Main menu within list is" +menu1);

		    String submenu="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and parentid='"+menu1+"' ORDER BY MENUORDER ASC";

	        List rsult= jdbctemplate.queryForList(submenu);

	        List rsult1=new ArrayList<List>();

	        ////System.out.println("Submenu query" +submenu);

	        Iterator subqryitr = rsult.iterator();

			while(subqryitr.hasNext())

			{

				Map map = (Map)subqryitr.next();

				submenuid = (map.get("MENUID")).toString();

				submenu = ", "+submenuid+",";

				String submenu_disable=", "+submenuid+"-D,";

				rsult1.clear();

				if(proflist.contains(submenu))

				{

					String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and MENUID='"+submenuid+"' ORDER BY MENUORDER ASC";

					rsult1 = jdbctemplate.queryForList(subrmenuqury);

					//System.out.println("Main menu result" +subrmenuqury);

				}

				if(proflist.contains(submenu_disable))

				{

					String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and MENUID='"+submenuid+"' ORDER BY MENUORDER ASC";

					rsult1 = jdbctemplate.queryForList(subrmenuqury);

					//System.out.println("Main menu result" +subrmenuqury);				   

				}

				submenuqryrsult.addAll(rsult1);

			}

			setSubmenulist(submenuqryrsult);

	        //session.setAttribute("submenulist", submenuqryrsult);

	    }

		System.out.println("***Inst user End submenulist *** "+submenuqryrsult);

	}

	
	private List<List> submenulist;
	public  List<List> getSubmenulist() {
		return submenulist;
	}
	public  void setSubmenulist(List<List> mstrmenuqryrsult1) {
		submenulist = mstrmenuqryrsult1;
	}
	public void childMenuList() 

	{

		System.out.println("***Inst user Entering childMenuList *** ");

		//System.out.println(" ===============  childMenuList ===================");	

		List<List> childmenuqryrsult =new ArrayList<List>();

		String proflist=temp;

		String menu1 =getSubmenuid1();

		//System.out.println("Submenuid;  "+menu1);	

		//System.out.println("Profile List;  "+proflist);	

		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List rsult1=new ArrayList<List>();

		String chldmenu_maker =", "+menu1+"-M,";

		//String chmenuid = ""+chmenu+"";

		String chldmenu_checker =", "+menu1+"-C";

		//String mhmenuid = ""+mhmenu+"";

		String chmenu_disable=", "+menu1+"-D";

		//System.out.println("getChildMenuid();"+chmenuid);

		if(proflist.contains(chldmenu_maker))

		{

			System.out.println("*********  Contains Maker ***********");

		    String submenu="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and parentid='"+menu1+"' and MAK_CHK_ID IN ('M','A') ORDER BY MENUORDER ASC";

		    rsult1 = jdbctemplate.queryForList(submenu);

		    //System.out.println("Submenu maker query" +submenu);

		}

		if(proflist.contains(chldmenu_checker))

		{

			System.out.println("*********  Contains Checker ***********");

			String submenu="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and parentid='"+menu1+"' and MAK_CHK_ID IN ('C','A') ORDER BY MENUORDER ASC";

			rsult1 = jdbctemplate.queryForList(submenu);

	       //System.out.println("Submenu checker query" +submenu);

	    }

		if(proflist.contains(chmenu_disable))

		{

			System.out.println("*********  Contains Both ***********");

		    String submenu="SELECT MENUNAME,MENUID,ACTION FROM IFMENU where MENUVISIBILITY ='1' and parentid='"+menu1+"' and MAK_CHK_ID IN ('D','A') ORDER BY MENUORDER ASC";

		    rsult1 = jdbctemplate.queryForList(submenu);

		    //System.out.println("Submenu checker query" +submenu);

	    }

		childmenuqryrsult.addAll(rsult1);

		setChildmenulist(childmenuqryrsult);

		System.out.println("***Inst user Entering childMenuList *** " +childmenuqryrsult);

	}



	
	private List<List>  childmenulist;
	public List<List> getChildmenulist() {
		return childmenulist;
	}
	public void setChildmenulist(List<List> submenuqryrsult) {
		childmenulist = submenuqryrsult;
	}

	private String lastlogin;
	private  String name;
	public String menuid;
	public String submenuid1;
	public String getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getSubmenuid1() {
		return submenuid1;
	}
	public void setSubmenuid1(String submenuid1) {
		this.submenuid1 = submenuid1;
	}
	}
