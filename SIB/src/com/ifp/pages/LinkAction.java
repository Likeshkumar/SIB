package com.ifp.pages;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonUtil;
public class LinkAction extends BaseAction {

	private static final long serialVersionUID = -2613425890762568273L;

	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	public CommonUtil comUtil(){
		CommonUtil  comutil = new CommonUtil( );
		return comutil;
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



	public String login() 
	{
		trace("*************** LOGIN BEGINS.... **********");
		enctrace("*************** LOGIN BEGINS.... **********");  
		HttpSession session = getRequest().getSession(); 
		String applicationtype;
		
		try {
			applicationtype = this.comUtil().selectApplictionType(jdbctemplate);
			System.out.println("applicationtype :" + applicationtype);
			System.out.println("CSRF TOKEN CREATED FOR LOGIN PAGE " );
		
			session = getRequest().getSession();
			String tokencsrf=UUID.randomUUID().toString();
			
			
			System.out.println("tokencsrf----->    "+tokencsrf);
			session.setAttribute("csrfToken", tokencsrf);
			
			trace("applicationtype :" + applicationtype);
			if( applicationtype.equals("NOREC")){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "NO APPLICATION TYPE CONFIGURED");
				trace( "applicationtype__" + applicationtype); 
				return "login";	
			}
			session.setAttribute("APPLICATIONTYPE", applicationtype ); 
		} catch (Exception e) {
			trace( "EXCEPTION : Could not connect the database. Check the network connection... __" + e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Could not connect the database. Check the network connection...");			
			e.printStackTrace();
		}
		trace("\n\n"); 
		enctrace("\n\n");
		return "login";		
	}
	public String enableJavascript()
	{
		System.out.println("Enable java Script ");
		return "enableJavascript";
	}
	
	public String Redirect() {
		return "Redirectto";		
	}
	
	public String welcome() {
		return "welcome";		
	}
	
	public String office() {
		return "office";			
	}
	
	public String Addprofile() {
		return "Addprofile";		
	}	
	public String AddPrivilages() {
		return "AddPrivilages";		
	}
	public String Editprofile() {
		return "Editprofile";		
	}
	public String CardOrder()
	{
		return "CardOrder";
	}
	
	public String Deleteinstcardorder()
	{
		return "Delinstcardorder";
	}
	
	public String Exception() {
		return "Exception";		
	}  
}
