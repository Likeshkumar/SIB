package com.ifg.usermgt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;


/**
 * SRNP0003
 * @author CGSPL
 *
 */
public class InstAdminLoginAction extends BaseAction 
{
	
	public  DataSource dataSource;
	
	public  DataSource getDataSource() {
		return dataSource;
	}

	public  void setDataSource(DataSource dataSource1) {
		this.dataSource = dataSource1;
	} 
	
	private String profilelist;
	
	
	public String getProfilelist() {
		return profilelist;
	}
	public void setProfilelist(String profilelist) {
		this.profilelist = profilelist;
	}
	
	String menuid;
	
	
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	
	String temp;

	public String crtPrntLst()

	{   

		//System.out.println("*** Entering Clearprnt *** ");

		//List<List> each_sentence = new ArrayList<List>();

       

		List rsult;

		String mainmenuid = null;

		List<List> mstrmenuqryrsult =new ArrayList<List>();

		List<List> mstrmenuqryrsultenable =new ArrayList<List>();

		List<List> mstrmenuqryrsultdisable =new ArrayList<List>();

		

		//menubean menulistvalue = new menubean();

		//menubean profilelist = new menubean();

		//menubean mkrchkrlistbean= new menubean();

		

		

		HttpSession session = getRequest().getSession();

		String PROFILEID = (String)session.getAttribute("PROFILEID");
		
		String instnamecond = "";
		if( session.getAttribute("Instname") != null ){
			instnamecond = " and inst_id='"+(String)session.getAttribute("Instname")+"'";
		}

		//String PROFILEID = "11";

		//System.out.println("TEST ---->" +PROFILEID);	

		String PROFILE_ID = PROFILEID.toUpperCase();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		/*String menulist="select MENU_LIST from "+getPROFILE_PRIVILEGE()+"  where PROFILE_ID = '"+PROFILE_ID+"' "+instnamecond+"";
		enctrace("Menu list query : "+menulist);
		//System.out.println("menulist ====>"+menulist);
		List res= jdbcTemplate.queryForList(menulist);*/
		
		///by gowtham-030919
		String menulist="select MENU_LIST from "+getPROFILE_PRIVILEGE()+"  where PROFILE_ID = ? "+instnamecond+"";
		enctrace("Menu list query : "+menulist);
		//System.out.println("menulist ====>"+menulist);
		List res= jdbcTemplate.queryForList(menulist,new Object[]{PROFILE_ID});

        Iterator itr = res.iterator();

        String proflist =null; 

      

		while(itr.hasNext())

		{

			

			Map map = (Map)itr.next();

			 proflist = (map.get("MENU_LIST")).toString();

			

		}

		int len=proflist.length();

		temp = (proflist.subSequence(1, len-1)).toString();

		 // //////System.out.println("Profile List" +temp);   

		////////System.out.println("menu list without braces " +temp);

         ////System.out.println("PROFILR LIST1 ::: " +temp);

		//profilelist.setProfilelist(temp);

		session.setAttribute("profilelist", temp);

		setProfilelist(temp);

		

		

		

		

        /*String masterqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and "
        + "parentid='0' ORDER BY MENUORDER ASC";
        List mstrqryrsult = jdbcTemplate.queryForList(masterqury);*/
		
		//BY GOWTHAM-030919
		String masterqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY =? and "
		        + "parentid=? ORDER BY MENUORDER ASC";
		        List mstrqryrsult = jdbcTemplate.queryForList(masterqury,new Object[]{"1","0"});

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

				  /* String mastermenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
				   + "where MENUVISIBILITY ='1' and MENUID='"+mainmenuid+"' ORDER BY MENUORDER ASC";
				   rsult1 = jdbcTemplate.queryForList(mastermenuqury);*/

				 
				 //by gowtham
				 String mastermenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" "
						   + "where MENUVISIBILITY =? and MENUID=? ORDER BY MENUORDER ASC";
						   rsult1 = jdbcTemplate.queryForList(mastermenuqury,new Object[]{"1",mainmenuid});
			 

			 }

			

			 mstrmenuqryrsult.addAll(rsult1);

		    

		}

	

		   session.setAttribute("menulist", mstrmenuqryrsult);

		  // System.out.println("*** End Clearprnt *** "+mstrmenuqryrsult);

		   return null;

        

        

	}	

	

	private List submenulist;

	

	

	public List getSubmenulist() {

		return submenulist;

	}

	public void setSubmenulist(List submenulist) {

		this.submenulist = submenulist;

	}

	

	public String subMenuList() 

	{

		//System.out.println("*** Entering submenulist *** ");

		//String PROFILEID = getProfilelist();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String proflist = getProfilelist(); 

		//System.out.println("proflist +++++++++++++++++++++++++++++++  " +proflist);

		int len=proflist.length();

		temp=(proflist.subSequence(1, len-1)).toString();
		//System.out.println("profile list temp :" + temp);
		setProfilelist(temp);

		List<List> submenulist;

		List<List> submenuqryrsult =new ArrayList<List>();

		String menu1=getMenuid();

		//System.out.println("######################MENU ID FRON JSP     "+menu1);
  
		String submenuid;

		if(proflist.contains(menu1))  

	    {

			/*String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' "
				+ "and parentid='"+menu1+"' ORDER BY MENUORDER ASC";
			//System.out.println("submenu Qury=====>"+submenu);
			List rsult= jdbcTemplate.queryForList(submenu);*/
			
			//by gowtham
			String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY =? "
					+ "and parentid=? ORDER BY MENUORDER ASC";
				//System.out.println("submenu Qury=====>"+submenu);
				List rsult= jdbcTemplate.queryForList(submenu,new Object[]{"1",menu1});

	        List rsult1=new ArrayList<List>();

	        Iterator subqryitr = rsult.iterator();

	       

			while(subqryitr.hasNext())

			{

				 Map map = (Map)subqryitr.next();

				 submenuid = (map.get("MENUID")).toString();

				// System.out.println("Menu id Form ===>"+submenuid);

				 submenu = ""+submenuid+"";

				 //System.out.println(" New Submenu---> "+submenu);

				 rsult1.clear();

				 if(proflist.contains(submenu)) {					

	/* String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and "
	 	+ "MENUID='"+submenuid+"' ORDER BY MENUORDER ASC";
	 	// System.out.println("subrmenuqury===>"+subrmenuqury);
	 rsult1 = jdbcTemplate.queryForList(subrmenuqury);*/
					 
					 ///by gowtham
					 String subrmenuqury="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY =? and "
					+ "MENUID=? ORDER BY MENUORDER ASC";
							 	// System.out.println("subrmenuqury===>"+subrmenuqury);
					rsult1 = jdbcTemplate.queryForList(subrmenuqury,new Object[]{"1",submenuid});

					   // System.out.println("Subr Menuqury rsult1=====>"+rsult1); 
 				 }
 				 submenuqryrsult.addAll(rsult1); 
			}

			setSubmenulist(submenuqryrsult);

			setMenuid(menu1);

			//System.out.println("*** End submenulist *** "+submenuqryrsult);

	        

    	  }

	

	    return "sucess";

		

	}

	

	private String submenuid1;

	

	public String getSubmenuid1() {

		return submenuid1;

	}



	public void setSubmenuid1(String submenuid1) {

		this.submenuid1 = submenuid1;

	}



	public String childMenuList() 

	{

		//System.out.println("*** Entering childMenuList *** ");

		//String PROFILEID = "11";

		//System.out.println("TEST ---->" +PROFILEID);	

		//String PROFILE_ID = PROFILEID.toUpperCase();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		//String menulist="select MENU_LIST from +"getADMIN_PROFILE_PRIVILEGE()+"  where PROFILE_ID = '"+PROFILE_ID+"' ";

		///System.out.println("menulist ====>"+menulist);

		//List res= jdbcTemplate.queryForList(menulist);

      //  Iterator itr = res.iterator();

        String proflist =getProfilelist(); 

      

		//while(itr.hasNext())

	//	{

			

		//	Map map = (Map)itr.next();

		//	 proflist = (map.get("MENU_LIST")).toString();

		//	

		//}

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

		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		

		//childmenubean.getSubmenulist();

		List rsult1=new ArrayList<List>();

		String mhmenu =", "+menu1+"-M";

		//String chmenuid = ""+chmenu+"";

		String directmenu=", "+menu1+"-D";

		String chmenu = ", "+menu1+"-C";

		//String mhmenuid = ""+mhmenu+"";

		

		////System.out.println("getChildMenuid();"+chmenuid);

		

		if(proflist.contains(mhmenu))

		{

			//System.out.println("Main menu within list is" +chmenu);

		   /* String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' "
		    + "and parentid='"+menu1+"' and MAK_CHK_ID IN ('M','A') ORDER BY MENUORDER ASC";
             rsult1 = jdbcTemplate.queryForList(submenu);*/

			///by gowtham
			 String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY =? "
					    + "and parentid=? and MAK_CHK_ID IN ('M','A') ORDER BY MENUORDER ASC";
			             rsult1 = jdbcTemplate.queryForList(submenu,new Object[]{"1",menu1});
			
		    ////System.out.println("Submenu maker query" +submenu);

	         

	    }

	    

		if(proflist.contains(directmenu))

		{

			//////System.out.println("Main menu within list is" +menu1);

		   /* String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and "
		    + "parentid='"+menu1+"' and MAK_CHK_ID IN ('D','A') ORDER BY MENUORDER ASC";
		    rsult1 = jdbcTemplate.queryForList(submenu);*/
			
			//by gowtham
			 String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY =? and "
					    + "parentid=? and MAK_CHK_ID IN ('D','A') ORDER BY MENUORDER ASC";
					    rsult1 = jdbcTemplate.queryForList(submenu,new Object[]{"1",menu1});

	      

	       // //System.out.println("Submenu checker query" +submenu);

	    }
		if(proflist.contains(chmenu))

		{

			//////System.out.println("Main menu within list is" +menu1);

		    /*String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY ='1' and"
		    + " parentid='"+menu1+"' and MAK_CHK_ID IN ('C','A') ORDER BY MENUORDER ASC";
		    rsult1 = jdbcTemplate.queryForList(submenu);*/

			///by gowtham-
			String submenu="SELECT MENUNAME,MENUID,ACTION FROM "+getMENU()+" where MENUVISIBILITY =? and"
				    + " parentid=? and MAK_CHK_ID IN ('C','A') ORDER BY MENUORDER ASC";
				    rsult1 = jdbcTemplate.queryForList(submenu,new Object[]{"1",menu1});
	      

	       // //System.out.println("Submenu checker query" +submenu);

	    }
		

		 childmenuqryrsult.addAll(rsult1);

		 setChildmenulist(childmenuqryrsult);

		// System.out.println("*** Entering childMenuList *** " +childmenuqryrsult);

	return "sucess";

	

	}



	private List childmenulist;



	public List getChildmenulist() {

		return childmenulist;

	}



	public void setChildmenulist(List childmenulist) {

		this.childmenulist = childmenulist;

	}
	
	
	
}
