package com.ifg.usermgt;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.Licence.Licensemanager;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.ifp.util.PasswordHashing;

/**
 * SRNP0003
 * @author CGSPL
 *
 */
public class Unlockuser extends BaseAction
{
 
	PasswordHashing pswd_hash=new PasswordHashing();
	Logger logger = Logger.getLogger(this.getClass());
//	ErrorClass error_class=new ErrorClass();
	LoginAction_COMMONUSER loginaction_class;
	
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager(); 
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate(); 
	
	
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

	CommonDesc commondesc = new CommonDesc();
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	List institutes;
	
	public List getInstitutes() {
		return institutes;
	}
	public void setInstitutes(List institutes) {
		this.institutes = institutes;
	}
	
	String displayunlock;
	public String getDisplayunlock() {
		return displayunlock;
	}
	public void setDisplayunlock(String displayunlock) {
		this.displayunlock = displayunlock;
	}
	
	String displayunlock1;
	
	public String getDisplayunlock1() {
		return displayunlock1;
	}
	public void setDisplayunlock1(String displayunlock1) {
		this.displayunlock1 = displayunlock1;
	}
	
	
	public synchronized int unblockUser( String instid, String usrname, JdbcTemplate jdbctemplate ) throws Exception {
		
		/*String updatefrstlogin="update USER_DETAILS set LOGINSTATUS = '0', RETRYCOUNT='0'  where username='"+usrname+"' "
		+ "and INSTID='"+instid+"'";
		enctrace("updatefrstlogin :" + updatefrstlogin );
		int x = jdbctemplate.update(updatefrstlogin);*/
		
		
		///by gowtham
		String updatefrstlogin="update USER_DETAILS set LOGINSTATUS =? , RETRYCOUNT=?  where username=? "
				+ "and INSTID=? ";
				enctrace("updatefrstlogin :" + updatefrstlogin );
				int x = jdbctemplate.update(updatefrstlogin,new Object[]{"0","0",usrname,instid});
		
		return x;
	}
	
	public String view()
	{
		List inst;
		String allInst="select INST_ID from INSTITUTION order by PREFERENCE";
		HttpSession session=getRequest().getSession();
		try
		{  
			inst =jdbctemplate.queryForList(allInst);
			System.out.println("Selected Instituion"+inst);
			setInstitutes(inst);
			return "unlockuserEntrypage";
		}
		catch (Exception e) 
		{
			session.setAttribute("unlockUserViewMessage", "Error While Getting The Institution while Unlocking User : Error " +e.getMessage());
			session.setAttribute("UnlockUserViewStatus","E");
			return "unlockuserEntrypage";
		}
		
	}
	
	public String checkuser()
	{
		
		
		String gpswd,db_password=null,db_salt=null,PASSWORDEXPIRYDATE=null;
		HttpSession session=getRequest().getSession();
		int LOGINSTATUS;
		try
			{
			   IfpTransObj transact = commondesc.myTranObject("UNBLOCK", txManager);
				Licensemanager licencemgr=new Licensemanager(); 
		
				System.out.println("checkuser function");
		
				String instid,usrname,password;
				instid = ((getRequest().getParameter("instname"))).trim();
				usrname=(getRequest().getParameter("username"));
				password=((getRequest().getParameter("password"))).trim();
				System.out.println(instid+"--"+usrname+"--"+password);
				
				
				String licence_check =licencemgr.checkfile(instid, commondesc);
			   	System.out.println(" function file not exists   ::"+ licence_check);
			   	if(licence_check.equals("nofile"))
					{
					   	System.out.println(" function file not exists   ::"+ licence_check);
						session.setAttribute("checkuser_unlock_message","License File Does Not Exists For Select Institution" );
						session.setAttribute("checkuser_unlock_Error","S");
						return "UserUnlockResult";
					}
				if((licence_check.equals("matched")))
				{
					/*String query_getuserdatils="select * FROM USER_DETAILS where username ='"+usrname+"' and "
					+ "INSTID='"+instid+"'";
				   	System.out.println("query_getuserdatils  ::"+query_getuserdatils);
					List user_result =jdbctemplate.queryForList(query_getuserdatils);*/

					///byg owtham
					String query_getuserdatils="select * FROM USER_DETAILS where username =? and "
							+ "INSTID=? ";
						   	System.out.println("query_getuserdatils  ::"+query_getuserdatils);
							List user_result =jdbctemplate.queryForList(query_getuserdatils,new Object[]{usrname,instid});
					
				  // 	System.out.println("user_result  ::"+user_result);
	
					if(user_result.isEmpty())
						{
							session.setAttribute("checkuser_unlock_message","User Does Not Exist For Selected Institution");
							session.setAttribute("checkuser_unlock_Error","S");
							return "UserUnlockResult";
						}
					else
						{
						 	int FIRSTTIME = -1;
						    String PSWREPEATCOUNT =null;
						    String FORCEPSWEXP = null;
						    String USERBLOCK=null;
						    String USERID=null;
						    String username=null;
						    int USERSTATUS=-1;
						    int PASSWORDEXPIRYFLAG=-1;
						    String name=null,firstname=null,lastname=null;
						    Iterator iterator_userresult = user_result.iterator();
						   	while(iterator_userresult.hasNext())
									{	
						   				Map mapper_userdetails=(Map)iterator_userresult.next();
										Object FIRSTTIME_obj = (mapper_userdetails.get("FIRSTTIME"));
										String FIRSTTIME_str = FIRSTTIME_obj.toString();
										FIRSTTIME = Integer.parseInt(FIRSTTIME_str);
										
										USERID =  (mapper_userdetails.get("USERID")).toString();
										username = (mapper_userdetails.get("USERNAME")).toString();
										USERBLOCK = (mapper_userdetails.get("USERBLOCK")).toString();
										firstname=((String)mapper_userdetails.get("FIRSTNAME"));
										lastname=((String)mapper_userdetails.get("LASTNAME"));
										name=firstname+" "+lastname;
										gpswd=((String)mapper_userdetails.get("USERPASSWORD"));
										db_password=((String)mapper_userdetails.get("USERPASSWORD"));
									    db_salt=((String)mapper_userdetails.get("SALT_KEY"));
									    Object retrycount_obj=mapper_userdetails.get("RETRYCOUNT");
									    String retrycount =retrycount_obj.toString();
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
										PASSWORDEXPIRYDATE=((String)mapper_userdetails.get("PASSWORDEXPIRYDATE"));
										System.out.println("============== Firstimte Login  "+FIRSTTIME);
										Object PASSWORDEXPIRYFLAG_OBJ=mapper_userdetails.get("PASSWORDEXPIRYFLAG");
										String PASSWORDEXPIRYDATE_s=PASSWORDEXPIRYFLAG_OBJ.toString();
										PASSWORDEXPIRYFLAG=Integer.parseInt(PASSWORDEXPIRYDATE_s);
										System.out.println("  PASSWORDEXPIRYFLAG  "+PASSWORDEXPIRYFLAG);
									    
									 }
							LoginAction_COMMONUSER loginaction_class=new LoginAction_COMMONUSER();
	
						   	boolean pswdcheck_result=loginaction_class.checkPassword(db_password,db_salt,password,instid);
						   	if(pswdcheck_result)
							   	{
						   			System.out.println("  password matched  ");
						   			System.out.println("  password matched  USERSTATUS"+USERSTATUS);
	
						   			if(USERSTATUS==0)
								   		 {
								        		session.setAttribute("checkuser_unlock_message","User Is Inactive, Please Contact Administrator");
												session.setAttribute("checkuser_unlock_Error","S");
												return "UserUnlockResult";
								         }
							        
							   		if(PASSWORDEXPIRYFLAG==1)
									   {
										   		session.setAttribute("checkuser_unlock_message","Your Password Expired, Please Contact Administrator");
												session.setAttribute("checkuser_unlock_Error","S");
												return "UserUnlockResult";
									   }
							   		
							         if(USERBLOCK.equals("1"))
								         {
								        	    session.setAttribute("checkuser_unlock_message","User Is Blocked, Please Contact Administrator");
												session.setAttribute("checkuser_unlock_Error","S");
												return "UserUnlockResult";
								         }
							         System.out.println("unblock user password  matched");
									//String updatefrstlogin="update USER_DETAILS set LOGINSTATUS = '0', RETRYCOUNT='0'  where username='"+usrname+"' and INSTID='"+instid+"'";
								    //jdbcTemplate.update(updatefrstlogin);
							         int x = this.unblockUser(instid, usrname, jdbctemplate);
							         if( x < 0 ){
							        	 transact.txManager.rollback(transact.status);
							        	 session.setAttribute("perverr", "E");
		 					        	 session.setAttribute("pervmsg", "Could not unlock the user");
							        	 trace("Could not unblock the user...got : " + x );
							        	 return "required_home";
							         }
								   	//session.setAttribute("checkuser_unlock_message"," User Is UNLOCKED ");
							         
							        transact.txManager.commit(transact.status);
									session.setAttribute("checkuser_unlock_Error","S");
									System.out.println(" User Status is Unblocked and Return TO Login Page");
									session.setAttribute("message","User Is Unblocked ");
								  
									return "loginpageLink";
							}
						  else
						  	{
							  System.out.println("unblock user password not matched");
					          session.setAttribute("checkuser_unlock_message","Password Is Incorrect");
							  session.setAttribute("checkuser_unlock_Error","S");
							  return "UserUnlockResult";
							 
						  	}
						}
					
				}
				else
				{
					session.setAttribute("checkuser_unlock_message","License File Does Not Exists For Select Institution" );
					session.setAttribute("checkuser_unlock_Error","S");
					return "UserUnlockResult";
				}
					   
			}
		catch (Exception e)
			{	
				session.setAttribute("checkuser_unlock_message"," Error While Unlocking The User "+e.getMessage());
				session.setAttribute("checkuser_unlock_Error","S");
				return "UserUnlockResult";
			}
	}
	

}
