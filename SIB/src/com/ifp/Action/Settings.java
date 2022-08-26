package com.ifp.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
 

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
 
import com.ifp.util.IfpTransObj;

public class Settings extends BaseAction{

	 
	 
	private List editsettings;

	JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
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

	public List getEditsettings() {
		return editsettings;
	}

	public void setEditsettings(List editsettings) {
		this.editsettings = editsettings;
	}

	CommonDesc commondesc = new CommonDesc(); 
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	public String addCurrency(){
		HttpSession session = getRequest().getSession();
		session.setAttribute("act_type", "insert");
		return "currencyadd_home";
	}
	
	private String usersetting;
	public String getUsersetting() {
		return usersetting;
	}

	public void setUsersetting(String usersetting) {
		this.usersetting = usersetting;
	}

	public String goConfig()	 
	{
		//HttpSession session = getRequest().getSession();
		System.out.println( "branch code is ___________ ");
	 trace( "branch code is ___________ action 	");
		
		
		String instid=this.comInstId();
		String userid=this.comUserCode();
		String select_query = "SELECT DATE_FILTER FROM USER_SETTINGS where  inst_id='"+instid+"' and userid='"+userid+"'";
		System.out.println( "select_query select_query"+select_query );
		List settinglist =jdbctemplate.queryForList(select_query);
		if(!(settinglist.isEmpty()))
		{
		Iterator itr = settinglist.iterator();
		while( itr.hasNext()){
		Map temp=(Map) itr.next();
		String datefilter=(String) temp.get("DATE_FILTER");
		//String datefilter=(String)jdbcTemplate.queryForObject(select_query, String.class);
		System.out.println("datefilter datefilter"+datefilter); 
		setUsersetting(datefilter);
		  } 
		}
		return "viewsettings";
	}

	public String insertSet()
	{
		String instid,userid,datefilter; 
		IfpTransObj transobj = commondesc.myTranObject("SETTING", txManager); 
		HttpSession session=getRequest().getSession(); 
		instid=this.comInstId();
		userid=this.comUserCode();
		datefilter=getRequest().getParameter("date_req");
		
		String actiontype = (getRequest().getParameter("submitsetting"));
		System.out.println( "actiontype__" + actiontype );
		String inst_settings ;
		String dispmsg ;
		try
			{		
				int userexist = duplicateCheck( instid,userid );
				if( userexist == 0 ){
				inst_settings="insert into USER_SETTINGS(INST_ID,USERID,DATE_FILTER)" + "values('"+instid+"','"+userid+"','"+datefilter+"')";
				System.out.println(inst_settings);
				dispmsg = "Configured";
			}
			else{
				inst_settings = "update USER_SETTINGS set DATE_FILTER='"+datefilter+"' WHERE INST_ID='"+instid+"' and USERID='"+userid+"'";
				System.out.println(inst_settings);
				dispmsg = "Updated";
				if(userexist != 0)
				{
				System.out.println("updated");
				}
			}
						
			System.out.println(inst_settings); 
			
			int x=commondesc.executeTransaction(inst_settings, jdbctemplate);
			System.out.println("query executed result"+x);
			
			if(x<=0)
			{
				transobj.txManager.rollback(transobj.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Could not " + dispmsg + " Setting" );
				return "viewsettings";
			}
			transobj.txManager.commit(transobj.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", " Settings " + dispmsg + " successfully" );
	
		}
		catch (Exception e) 
		{
			transobj.txManager.rollback(transobj.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error in Settings "+e );
			System.out.println(e);
		}
		return "viewsettings";
	}  
	
	public int duplicateCheck( String instid,String userid )
	{
		String selectquery = "SELECT COUNT(*) FROM USER_SETTINGS WHERE INST_ID='"+instid+"' and USERID='"+userid+"'";
		System.out.println( "select qry __" + selectquery); 
		int x = jdbctemplate.queryForInt(selectquery);
		return x;
	}
}