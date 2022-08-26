package com.ifg.usermgt;
import it.sauronsoftware.base64.Base64;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.exceptions.exception;



/**
 * SRNP0003
 * @author CGSPL
 *@version 1.0
 */
public class adminPageLoginDetailsAction extends BaseAction {
	  
	String temp;
	//private instbean inbean=new instbean();
	Logger logger = Logger.getLogger(this.getClass());
	//loginbean logbean=new loginbean();
	//public menubean mbean=new menubean();
	private static final long serialVersionUID = 1L;
	HttpServletRequest request ;
	 
	//menubean menuname = new menubean();
	
	/* private static String display ;
	
	
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		adminPageLoginDetailsAction.display = display;
	}
	
	*/
	 
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	

	
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




	public String adminPage() 
	{
		trace("*************** ADMIN LOGIN BEGINS **********");
		enctrace("***************ADMIN LOGIN BEGINS **********");
		try{
		//loginbean logbean=new loginbean();
		String instna,usrname,password,instid;
		List result;		
		String  gpswd=null;
		boolean flag;
		String systemdate1 = null;
		String logintype = null;
		
		HttpSession session = getRequest().getSession();
		// getting the user name and password from form
		
			
		    //loginbean bean=new loginbean();
		    usrname =(String) session.getAttribute("SS_USERNAME");
		    trace("usrname==> "+usrname);
			password=(String) session.getAttribute("adminpswd");
			//System.out.println("password==> "+password);
			//instid = ((getRequest().getParameter("instname"))).trim();
			String encodedpswd = Base64.encode(password);
			//trace("password ---->" +encodedpswd ); 
			
			//String qury="SELECT userid,USERNAME,PROFILEID,FIRSTNAME,LASTNAME,USERPASSWORD,LASTLOGIN,FIRSTTIME FROM USER_DETAILS where username ='"+usrname+"' and USERPASSWORD='"+encodedpswd+"' and LOGINSTATUS='0' and USERSTATUS='1'";
			/*String qury="SELECT USERID,USERNAME,PROFILEID,FIRSTNAME,LASTLOGIN,LASTNAME,USERPASSWORD FROM ADMIN_USER where username ='"+usrname+"' AND USERPASSWORD='"+encodedpswd+"'"; // and USERPASSWORD='"+encodedpswd+"' 			
			enctrace("qury --- "+qury);
			result =jdbctemplate.queryForList(qury);*/
			
			//by gowtham-030919
			String qury="SELECT USERID,USERNAME,PROFILEID,FIRSTNAME,LASTLOGIN,LASTNAME,USERPASSWORD FROM ADMIN_USER"
			+ " where username =? AND USERPASSWORD=? "; // and USERPASSWORD='"+encodedpswd+"' 			
			enctrace("qury --- "+qury);
			result =jdbctemplate.queryForList(qury,new Object[]{usrname,encodedpswd});
			
			//logbean.setUserdetail(result); 
			//System.out.println("result is --->"+result); 
		    
		   
		    if (result.isEmpty())
		    {
		    	 //System.out.println("result is null--->");
				session.setAttribute("message","User name or Password or Institution Mismatch");
				return "invaliduser";
		    }
			else
			{
					Iterator itr = result.iterator();
					while(itr.hasNext())
					{
						String name,firstname,lastname,lastlogin;
						Map map = (Map)itr.next();
						String username = (map.get("USERNAME")).toString();
						String userpasswrd = (map.get("USERPASSWORD")).toString();
						trace("Admint login...:"+username);
						//trace("Admin hashed password : "+userpasswrd);
						//session.setAttribute("SS_USERNAME",username);	
						//String user7name = (String)session.getAttribute("SS_USERNAME");
						//System.out.println("*********************************************----->"+user7name);
					   // logbean.setUsername((String)map.get("USERNAME"));
						// logbean.setLastlogin((String)map.get("LASTLOGIN"));
						firstname=((String)map.get("FIRSTNAME"));
				
						lastname=((String)map.get("LASTNAME"));
						
						name=firstname+" "+lastname;
						//System.out.println("name ---->" +name );
						//logbean.setName(name);
						gpswd=((String)map.get("USERPASSWORD")).trim();
						//System.out.println("password ---->" +gpswd );
						//String tsr= logbean.getLastlogin().trim();
						//System.out.println("password ="+gpswd);
						session.setAttribute("BRANCHCODE","000");
						session.setAttribute("name", name);
						
						lastlogin=((String)map.get("LASTLOGIN")).trim();
						session.setAttribute("lastlogin", lastlogin);
											}
					int re;
					String decodedpswd = Base64.decode(gpswd);
					//System.out.println("decodedpswd ="+decodedpswd);
					//System.out.println("password ="+password);
					re=password.compareTo(decodedpswd);
					//System.out.println(" Compare result=  "+re);
					if(re=='0')
						flag=true;
					else
						flag=false;
					
					
				  if(flag)
				    	{
				    	System.out.println("password not matched");
				    	return "invaliduser";
				    	}
				    /*	else 
				    	{
				    		instna="BUCB";
				    		//System.out.println("The Institution Name is -------------------------"+instna);
				    		session.setAttribute("Instname",instna);
				    		String userId = "select userid from ADMIN_USER where username='"+usrname+"'";
				    		int userid = jdbctemplate.queryForInt(userId);
				    		//System.out.println("The Institution User iD is ~~~~~~~~~~~~~~~~~~"+userid);
				    		String Users_id = Integer.toString(userid);
				    		session.setAttribute("UserId",Users_id);
				    	    //String rt=crtPrntLst();
				    	    return "sucess";
				     	}
				  */
				  return "sucess";
					}
			
		}
		catch (Exception e) 
		{
			exception excp=new exception();
			excp.setException(e);
			e.printStackTrace();
			trace("\n\n"); 
			enctrace("\n\n");
			return "Exception";
		}

	}
	
	
	
    
	public String crtPrntLst()
	{   
		
		//List<List> each_sentence = new ArrayList<List>();
       
		List rsult;String menuid=null;String mainmenuid = null;
		List<List> mstrmenuqryrsult =new ArrayList<List>();
		List<List> mstrmenuqryrsultenable =new ArrayList<List>();
		List<List> mstrmenuqryrsultdisable =new ArrayList<List>();
		
		//menubean menulistvalue = new menubean();
		//menubean profilelist = new menubean();
		//menubean mkrchkrlistbean= new menubean();
		
		
		HttpSession session = getRequest().getSession();
		//String PROFILEID = (String)session.getAttribute("PROFILEID");
		
		String PROFILEID = "11";
		//System.out.println("TEST ---->" +PROFILEID);	
		String PROFILE_ID = PROFILEID.toUpperCase();
		 
		/*String menulist="select MENU_LIST from ADMIN_PROFILE_PRIVILEGE  where PROFILE_ID = '"+PROFILE_ID+"' ";
		System.out.println("menulist ====>"+menulist);  
		List res= jdbctemplate.queryForList(menulist);*/
		
		
		//by gowtham-030919
		String menulist="select MENU_LIST from ADMIN_PROFILE_PRIVILEGE  where PROFILE_ID = ? ";
	//	System.out.println("menulist ====>"+menulist);  
		List res= jdbctemplate.queryForList(menulist,new Object[]{PROFILE_ID});
		
        Iterator itr = res.iterator();
        String proflist =null; 
      
		while(itr.hasNext())
		{
			
			Map map = (Map)itr.next();
			 proflist = (map.get("MENU_LIST")).toString();
			
		}
		int len=proflist.length();
		temp=(proflist.subSequence(1, len-1)).toString();
		 // //////System.out.println("Profile List" +temp);
		////////System.out.println("menu list without braces " +temp);
         //System.out.println("PROFILR LIST1 ::: " +temp);
		//profilelist.setProfilelist(temp);
		session.setAttribute("profilelist", temp);
		setProfilelist(temp);
		
		
		
		
       /* String masterqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and parentid='0' "
        + "ORDER BY MENUORDER ASC";
		List mstrqryrsult = jdbctemplate.queryForList(masterqury);*/
		
		//BY GOWTHAM-030919
		 String masterqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY =? and parentid=? "
		+ "ORDER BY MENUORDER ASC";
	    List mstrqryrsult = jdbctemplate.queryForList(masterqury,new Object[]{"1","0"});
		
		List rsult1=new ArrayList<List>();
		//List rsultenable=new ArrayList<List>();
		//List rsultdisable=new ArrayList<List>();
		Iterator mstrqryitr = mstrqryrsult.iterator();
		//System.out.println(" list befor initialization result  ----->>>>>" +masterqury);
		
		
		mstrmenuqryrsult.clear();
		mstrmenuqryrsultenable.clear();
		mstrmenuqryrsultdisable.clear();
		
		
		while(mstrqryitr.hasNext())
		{
			 Map map = (Map)mstrqryitr.next();
			 mainmenuid = (map.get("MENUID")).toString();
			// String mainmenu = mainmenuid;
			
			 ////System.out.println("Main menu result" +mkrchkrmenuid);
			 menuid = ", "+mainmenuid+",";
			   
			 rsult1.clear();  
			 if(proflist.contains(menuid))
			 {
				
				 /*String mastermenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
						 + "where MENUVISIBILITY ='1'and MENUID='"+mainmenuid+"' ORDER BY MENUORDER ASC";
						 rsult1 = jdbctemplate.queryForList(mastermenuqury);*/
				 
				 
			 String mastermenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
			 + "where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
			 rsult1 = jdbctemplate.queryForList(mastermenuqury,new Object[]{"1",mainmenuid});
				
				  System.out.println("mainmenuid:::::::::"+mainmenuid);
				 
			 }
			 //String menuprev=mkrchkrlistbean.getMenupriviledge();
		     ////System.out.println("menuprev    " +menuprev);
			 mstrmenuqryrsult.addAll(rsult1);
		     // mstrmenuqryrsultenable.addAll(rsultenable);
			 //  mstrmenuqryrsultdisable.addAll(rsultdisable);
		
		}
	
		  // menuname.setMenulist(mstrmenuqryrsult);
		   session.setAttribute("menulist", mstrmenuqryrsult);
		   return null;
        
        
	}
	
	   private String profilelist;
	   public String getProfilelist() {
			return profilelist;
		}
		public void setProfilelist(String profilelist) {
			this.profilelist = profilelist;
		}
		
	
	public String subMenuList() 
	{
		String PROFILEID = "11";
		//System.out.println("TEST ---->" +PROFILEID);	
		String PROFILE_ID = PROFILEID.toUpperCase();
	 
		/*String menulist="select MENU_LIST from ADMIN_PROFILE_PRIVILEGE  where PROFILE_ID = '"+PROFILE_ID+"' ";
		//System.out.println("menulist ====>"+menulist);
		List res= jdbctemplate.queryForList(menulist);*/

		//by gowtham
		String menulist="select MENU_LIST from ADMIN_PROFILE_PRIVILEGE  where PROFILE_ID = ? ";
		//System.out.println("menulist ====>"+menulist);
		List res= jdbctemplate.queryForList(menulist,new Object[]{PROFILE_ID});
		
		
        Iterator itr = res.iterator();
        String proflist =null; 
      
		while(itr.hasNext())
		{
			
			Map map = (Map)itr.next();
			 proflist = (map.get("MENU_LIST")).toString();
			
		}
		int len=proflist.length();
		temp=(proflist.subSequence(1, len-1)).toString();
		 // //////System.out.println("Profile List" +temp);
		////////System.out.println("menu list without braces " +temp);
         ////System.out.println("PROFILR LIST1 ::: " +temp);
		//profilelist.setProfilelist(temp);
		
		setProfilelist(temp);
		List<List> submenulist;
		List<List> submenuqryrsult =new ArrayList<List>();
		
		String menu1=getMenuid();
		String submenuid;
		//System.out.println("proflist ++++++++++++++++++++++++;  "+temp);	
		
		
		//submenubean.getMenulist();
		//System.out.println("getMenuid();"+menu1);
		String adminsubmenu=", "+menu1+",";
		System.out.println("proflist length..."+proflist.length());
		if(proflist.contains(adminsubmenu))   
	    {
			
			System.out.println("Main menu within list is" +menu1);
			
		   /* String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
		    + "where MENUVISIBILITY ='1' and parentid='"+menu1+"' ORDER BY MENUORDER ASC";
	        List rsult= jdbctemplate.queryForList(submenu);*/
			
			//by gowtham--30919
			 String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
					    + "where MENUVISIBILITY =? and parentid=? ORDER BY MENUORDER ASC";
				        List rsult= jdbctemplate.queryForList(submenu,new Object[]{"1",menu1});
	        
	        List rsult1=new ArrayList<List>();
	        //System.out.println("Submenu query" +submenu);
	        Iterator subqryitr = rsult.iterator(); 
	       
			while(subqryitr.hasNext())
			{
				 Map map = (Map)subqryitr.next();
				 submenuid = (map.get("MENUID")).toString();
				
				 submenu = ", "+submenuid+",";
				 //System.out.println("submenu.... "+ submenu);  
				 rsult1.clear();
				 if(proflist.contains(submenu))
				 {
					
					 /* String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+""
					  + " where MENUVISIBILITY ='1' and MENUID='"+submenuid+"' ORDER BY MENUORDER ASC";
					 //String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and parentid='"+submenuid+"' ORDER BY MENUORDER ASC";
					  rsult1 = jdbctemplate.queryForList(subrmenuqury);*/
					 
					 ////by gowtham
					 String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+""
							  + " where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
							 //String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and parentid='"+submenuid+"' ORDER BY MENUORDER ASC";
							  rsult1 = jdbctemplate.queryForList(subrmenuqury,new Object[]{"1",submenuid});
					  
					  System.out.println("Main menu result" +subrmenuqury);
				 }
				 submenuqryrsult.addAll(rsult1);
				
				 
				// System.out.println("rsult1::"+rsult1);
   
			}
			setSubmenulist(submenuqryrsult);
	        
	        
    	  }
	
	    return "sucess";
		
    }
	private List<List> submenulist;
	public  List<List> getSubmenulist() {
		return submenulist;
	}

	public  void setSubmenulist(List<List> mstrmenuqryrsult1) {
		submenulist = mstrmenuqryrsult1;
	}
	
	public String submenuid1;


	public String getSubmenuid1() {
		return submenuid1;
	}
	public void setSubmenuid1(String submenuid1) {
		this.submenuid1 = submenuid1;
	}
	
	public String childMenuList() 
	{
		String PROFILEID = "11";
		//System.out.println("TEST ---->" +PROFILEID);	
		String PROFILE_ID = PROFILEID.toUpperCase();
		 
		/*String menulist="select MENU_LIST from ADMIN_PROFILE_PRIVILEGE  where PROFILE_ID = '"+PROFILE_ID+"' ";
		//System.out.println("menulist ====>"+menulist);
		List res= jdbctemplate.queryForList(menulist);*/
		
		//by gowtham
		String menulist="select MENU_LIST from ADMIN_PROFILE_PRIVILEGE  where PROFILE_ID = ? ";
		//System.out.println("menulist ====>"+menulist);
		List res= jdbctemplate.queryForList(menulist,new Object[]{PROFILE_ID});
		
        Iterator itr = res.iterator();
        String proflist =null; 
          
		while(itr.hasNext())
		{
			
			Map map = (Map)itr.next();
			 proflist = (map.get("MENU_LIST")).toString();
			
		}
		int len=proflist.length();
		temp=(proflist.subSequence(1, len-1)).toString();
		 // //////System.out.println("Profile List" +temp);
		////////System.out.println("menu list without braces " +temp);
         ////System.out.println("PROFILR LIST1 ::: " +temp);
		//profilelist.setProfilelist(temp);
		//session.setAttribute("profilelist", temp);
		setProfilelist(temp);
		
		
		//menubean childmenubean = new menubean();
		//menubean profilelist = new menubean();
		String menuid=null;
		String childmenuid = null;
		List<List> childmenuqryrsult =new ArrayList<List>();
		//String proflist=temp;
		String menu1 =getSubmenuid1();
		
		//////System.out.println("Profile List;  "+proflist);	
		//JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
		
		//childmenubean.getSubmenulist();
		List rsult1=new ArrayList<List>();
		//String chmenu = menu1+"-M";
	//	String chmenuid = ", "+chmenu+"";
		
		//String mhmenu = menu1+"-C";
		//String mhmenuid = ", "+mhmenu+"";
		
		////System.out.println("getChildMenuid();"+chmenuid);
		
		//if(proflist.contains(chmenu))
		{
			//System.out.println("Main menu within list is" +chmenu);
			
		   /* String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
		    + "where MENUVISIBILITY ='1' and parentid='"+menu1+"' and MAK_CHK_ID IN ('D','A') ORDER BY MENUORDER ASC";
		    rsult1 = jdbctemplate.queryForList(submenu);*/
			
			///BY GOWTHAM
			 String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
			 + "where MENUVISIBILITY =? and parentid=? and MAK_CHK_ID IN ('D','A') ORDER BY MENUORDER ASC";
			  rsult1 = jdbctemplate.queryForList(submenu,new Object[]{"1",menu1});
		    
		    ////System.out.println("Submenu maker query" +submenu);
	          
	    }
		/*
		//if(proflist.contains(mhmenuid))
		{
			//////System.out.println("Main menu within list is" +menu1);
		    String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and parentid='"+menu1+"' and MAK_CHK_ID IN ('C','A') ORDER BY MENUORDER ASC";
		    rsult1 = jdbctemplate.queryForList(submenu);
	      
	       // //System.out.println("Submenu checker query" +submenu);
	    }
	    */  
		
		 childmenuqryrsult.addAll(rsult1);
		 setChildmenulist(childmenuqryrsult);
	
	return "sucess";
	
}

	public String menuid;


	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}	 

	

	
	private List<List>  childmenulist;
	public List<List> getChildmenulist() {
		return childmenulist;
	}

	public void setChildmenulist(List<List> submenuqryrsult) {
		childmenulist = submenuqryrsult;
	}
}
