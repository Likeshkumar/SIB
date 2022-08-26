package com.ifg.usermgt;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.exceptions.exception;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Validation;

import com.ifg.Bean.ServerValidationBean;
/**
 * SRNP0003
 * @author CGSPL
 *
 */
public class ForgetpasswordAction extends BaseAction 
{
	ServerValidationBean bean = new ServerValidationBean();
	public ServerValidationBean getBean() {
		return bean;
	}

	public void setBean(ServerValidationBean bean) {
		this.bean = bean;
	}

	private JdbcTemplate jdbctemplate = new JdbcTemplate(); 
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager(); 
	AuditBeans auditbean = new AuditBeans();
	
	public AuditBeans getAuditbean() {
		return auditbean;  
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
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

	CommonDesc commondesc = new CommonDesc();
	 
	 
	List institutionlist;

	public List getInstitutionlist() 
	{
		return institutionlist;
	}
	public void setInstitutionlist(List institutionlist) 
	{
		this.institutionlist = institutionlist;
	}
	private static final long serialVersionUID = 1L;
	
	static Properties properties = new Properties();
	static
	{
	properties.put("mail.smtp.host", "smtp2.accelmail.com");
	properties.put("mail.smtp.socketFactory.port", "587");
	properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	properties.put("mail.smtp.auth", "true");
	properties.put("mail.smtp.port", "587");
	}
	
	public String forgetPasswordhome()
	{
		 //HttpSession session = getRequest().getSession(); 
		 
	/*	// by siva 210819
			HttpSession ses = getRequest().getSession(false);
			String sessioncsrftoken = (String) ses.getAttribute("token");
			String jspcsrftoken = getRequest().getParameter("token");
			
			System.out.println("sessioncsrftoken  ---> "+sessioncsrftoken);
			System.out.println("jspcsrftoken  ---> "+jspcsrftoken);
			
			if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) 
			{
				ses.setAttribute("message", "CSRF Token Mismatch");
				return "invaliduser";
			}*/
			// by siva 210819
	 
		 List inst_result;
		 String institution = "SELECT INST_ID,INST_NAME FROM INSTITUTION";
		 enctrace("institution--> "+institution);
		 inst_result =jdbctemplate.queryForList(institution);
		 trace("inst_result---> "+inst_result);
		 setInstitutionlist(inst_result);
		return "forgetpasswordhome";
	}
 
	public String sendMail()
	{
		final String from = "sankar@cashlinkglobal.com";
		final String password = "Ifprepaid@4884";
		String to = getRequest().getParameter("to");
		String subject = getRequest().getParameter("subject");
		String body = getRequest().getParameter("body");
	
		System.out.println("From===>"+from+"\nPassword===>"+password+"\nTo===>"+to+"\nSubject===>"+subject+"\nBody====>"+body);

		  try
	      {
	         Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator() 
	         {
	            protected PasswordAuthentication getPasswordAuthentication() 
	            {
	            	return new PasswordAuthentication(from, password);
	            }
	          }
	         );
	         System.out.println("------------1------------");
	         Message message = new MimeMessage(session);
	         System.out.println("------------2------------");
	         message.setFrom(new InternetAddress(from));
	         System.out.println("------------3------------");
	         message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
	         System.out.println("------------4------------");
	         message.setSubject(subject);
	         System.out.println("------------5------------");
	         message.setText(body);
	         System.out.println("------------6------------");
	         
	         Transport.send(message);
	         System.out.println("------------7------------");
	      }
	      catch(Exception e)
	      {
	    	  System.out.println("Error While Send Mail  ===>"+e);
	      }
	
		return forgetPasswordhome();
	}
	
	public String updateForgetpasswordrequest()
	{
		     HttpSession session =getRequest().getSession();
		
			System.out.println("check1");
				// by siva 210819
				HttpSession ses = getRequest().getSession();
				String sessioncsrftoken = (String) ses.getAttribute("csrfToken");
			
				String jspcsrftoken = getRequest().getParameter("token");
				System.out.println("sessioncsrftoken ---->  "+sessioncsrftoken);
				System.out.println("jspcsrftoken----> "+jspcsrftoken);
				if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) 
				{
					System.out.println("check2");
					ses.setAttribute("message", "CSRF Token Mismatch");
					addActionError("CSRF Token Mismatch");
					return forgetPasswordhome();
				}
				// by siva 210819
	
		
		IfpTransObj transact = commondesc.myTranObject("FOGOTPASS", txManager);	
		boolean check;
		
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		String username="",emailid;String updaterequest;int status = 0;
		try
		{ 	
			String usertextquery;
			
			String instid=getRequest().getParameter("instid");
			String usertext=getRequest().getParameter("usertext");
			System.out.println("usertext "+usertext);
			
			
			
			bean.setInstid(instid);
			bean.setUsertext(usertext);
			
			check=Validation.NumberCharcter(usertext);
			if(!check)
			{
				System.out.println("check3");
				addFieldError("usertext","Enter User Name");
				return forgetPasswordhome();
			}
			
			check=Validation.NumberCharcter(instid);
			if(!check)
			{
				System.out.println("check4");
				addFieldError("instid","Enter Institution ID");
				return forgetPasswordhome();
			}
			
			if( instid !=null )
			{
				instid = instid.toUpperCase();
			}

				
				
				usertextquery= " AND USERNAME='"+usertext+"'";
				System.out.println(usertextquery);
			
			System.out.println(usertextquery);
		
			//check forgetpasssword request exist
			int forgetrequest =commondesc.checkforgetrequest(instid,usertextquery,jdbctemplate);
			if(forgetrequest>0){
				/*addActionError("Already Request sent to administrator");*/
				addActionError("Please Contact Your Administrator");
				return forgetPasswordhome();
			}
			
			/*String usernamedetails = "SELECT USERNAME,EMAILID FROM USER_DETAILS WHERE INSTID='"+instid+"'";
			System.out.println(usernamedetails);
			ArrayList arry = new ArrayList();
			List usernamelist = jdbctemplate.queryForList(usernamedetails);*/
			
			//by gowtham
			String usernamedetails = "SELECT USERNAME,EMAILID FROM USER_DETAILS WHERE INSTID=? ";
			System.out.println(usernamedetails);
			ArrayList arry = new ArrayList();
			List usernamelist = jdbctemplate.queryForList(usernamedetails,new Object[]{instid});
			
			Iterator itr = usernamelist.iterator();
			while (itr.hasNext()) 
			{
				Map mp = (Map) itr.next();
				username = (String) mp.get("USERNAME");
				emailid = (String) mp.get("EMAILID");
				arry.add(username);
				arry.add(emailid);
			} 
			System.out.println(arry);
			
			/*String querystring = "UPDATE USER_DETAILS SET FORGOTPASSWORDFLAG='1',USERBLOCK='1' WHERE INSTID='"+instid+"' AND ";
			if(arry.contains(usertext)){
			System.out.println("PRESENT IN usertext");
			updaterequest = querystring+"USERNAME='"+usertext+"'";
			System.out.println(updaterequest);
			status = jdbctemplate.update(updaterequest);*/
			
			///by gowtham-030919
			String querystring = "UPDATE USER_DETAILS SET FORGOTPASSWORDFLAG=? ,USERBLOCK=?  WHERE INSTID=? AND ";
			if(arry.contains(usertext)){
			System.out.println("PRESENT IN usertext");
			updaterequest = querystring+"USERNAME=? ";
			System.out.println(updaterequest);
			status = jdbctemplate.update(updaterequest,new Object[]{"1","1",instid,usertext});
				
				System.out.println(status);}
			else
			{
				//session.setAttribute("curerr","E");
				session.setAttribute("curerr","S");
				session.setAttribute("curmsg","If User Name And Institution Correct Request Will Send, Please  Contact Administrator");
				return forgetPasswordhome();
				}
			     
			if(status>=1){
				transact.txManager.commit(transact.status);
				trace( "userdetails committed successfully.");
				session.setAttribute("curerr","S");
				session.setAttribute("curmsg","If User Name And Institution Correct Request Will Send, Please  Contact Administrator");
				
				/*************AUDIT BLOCK**************/ 
				  try{
					  
						
						//added by gowtham_220719
						trace("ip address======>  "+ip);
						auditbean.setIpAdress(ip);
					 
					auditbean.setActmsg("Forgot Password Request Initiated" );
					auditbean.setUsercode(usertext);
					auditbean.setAuditactcode("40"); 
					commondesc.insertAuditTrail(instid, usertext, auditbean, jdbctemplate, txManager);
				 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
				 /*************AUDIT BLOCK**************/
				  
				  
			}else{
				transact.txManager.rollback(transact.status);
				/*session.setAttribute("curerr","E");	
				session.setAttribute("curmsg"," Unable to continue with reset password..");*/
				
				session.setAttribute("curerr","S");	
				session.setAttribute("curmsg"," Unable to continue with reset password..");
				
				trace("userdetails rollbacked successfully.");
				return forgetPasswordhome();}
		}
		catch (Exception e) 
		{
			addActionError("Exception: While requesting for reset password ");
			return forgetPasswordhome();
		}
		
		return forgetPasswordhome();
	}
	

}
