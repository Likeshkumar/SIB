package com.ifg.Config.padss;

import com.ifp.Action.BaseAction;
import java.util.Iterator;
import java.util.List;

import connection.Dbcon;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import java.util.Iterator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifg.Config.padss.PadssSecurity;
public class KeyChnageProcessAction extends BaseAction
{
	/**
	 * 
	 * SRSH0001
	 */
	private static final long serialVersionUID = 1L;
	PadssSecurity padss = new PadssSecurity();
	
	public String comInstId(HttpSession session){
		//HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String comAdminUserCode(HttpSession session){
		//HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("SS_USERNAME"); 
		return instid;
	}

	public String comUserCode(HttpSession session){
		//HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	public String instid;
	
	public String getInstid() 
	{
		return instid;
	}
	public void setInstid(String instid) 
	{
		this.instid = instid;
	}
	
	
private JdbcTemplate jdbctemplate = new JdbcTemplate(); 
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	
	/*public String keyChangeHome()
	{
		trace("method called");
		return "keychangehomepage";
	}*/
	
	public String KeyChangeProcess()
	{
		trace("method called");
		String instid = this.comInstId(session); 
		trace("instid--->"+instid);
		String username=this.comAdminUserCode(session);	
		trace("user name-->"+username);
		String usercode = comAdminUserCode(session);
		System.out.println("usercode --- "+usercode);
		
		String OLD_DMK = getRequest().getParameter("olddmk");
		String OLD_EDPK = getRequest().getParameter("oldedpk");
		String NEW_DMK = getRequest().getParameter("newdmk");
		String NEW_EDPK = getRequest().getParameter("newedpk");

		
		try
		{
			 String clear_old_dpk=padss.decryptDPK(OLD_DMK,OLD_EDPK);
			 String clear_new_dpk=padss.decryptDPK(NEW_DMK,NEW_EDPK);
			 
			 	Connection conn = null;
				Dbcon dbcon = new Dbcon();
				conn = dbcon.getDBConnection();
				CallableStatement cstmt = null;
				cstmt = conn.prepareCall("call KEY_MIG(?,?,?,?,?)");
				trace("procedure--->call KEY_MIG(?,?,?,?,?,)");
				
				cstmt.setString(1, instid);
				cstmt.setString(2, clear_old_dpk);
				cstmt.setString(3, clear_new_dpk);
				cstmt.setString(4, usercode);
				cstmt.setString(5, username);
				cstmt.execute();
				conn.close();
				
				String result="SELECT PERS_SUCCESS_COUNT,INST_SUCCESS_COUNT,PROD_SUCCESS_COUNT,PERS_COUNT,PROD_COUNT,INST_COUNT FROM key_process_mgnt";
				enctrace("result  ::"+result);
				List user_result =jdbctemplate.queryForList(result);
				//trace("user_result  ::"+user_result);
				String PERS_SUCCESS_COUNT = null;
				String INST_SUCCESS_COUNT=null;
				String PROD_SUCCESS_COUNT =null;
			    String pers_count = null;
			    String prod_count=null;
			    String inst_count=null;
				Iterator iterator_userresult = user_result.iterator();
			   	while(iterator_userresult.hasNext())
			   	{
			   		Map mapper_userdetails=(Map)iterator_userresult.next();
			   		PERS_SUCCESS_COUNT = (mapper_userdetails.get("PERS_SUCCESS_COUNT")).toString();
			   		INST_SUCCESS_COUNT = (mapper_userdetails.get("INST_SUCCESS_COUNT")).toString();
			   		PROD_SUCCESS_COUNT = (mapper_userdetails.get("PROD_SUCCESS_COUNT")).toString();
			   		pers_count =  (mapper_userdetails.get("PERS_COUNT")).toString();
			   		prod_count = (mapper_userdetails.get("PROD_COUNT")).toString();
			   		inst_count =  (mapper_userdetails.get("INST_COUNT")).toString();
			   	}
			   	
			   	
				if ((pers_count.equalsIgnoreCase(PERS_SUCCESS_COUNT)) && (prod_count.equalsIgnoreCase(INST_SUCCESS_COUNT)) &&(inst_count.equalsIgnoreCase(PROD_SUCCESS_COUNT))) 
				{
					trace(" Card Migrated Successfully Based on New Key");
					addActionError("Card Migrated Successfully Based on New Key");
					return "required_home";
				}
				else
				{
					trace("Exception occured in Key Change Process ");
					addActionError("Exception Occured in Key Change Process");
					return "required_home";
				}		
		}
		catch(Exception e)
		{
			e.getMessage();
			trace("exception occured in process " + e.getMessage());
		}
		return "required_home";
	}
}
