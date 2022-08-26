package com.ifp.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.*;

public class ContextUpdater 
{
	
	private static final long serialVersionUID = 1L;

	public void addUserToContext(HttpSession session)
	{
		System.out.println("Adding New Context");
		ServletContext context = checkContextVariable(session);
		String user_id=(String)session.getAttribute("USERID");
		String inst_id=(String)session.getAttribute("Instname");
		System.out.println("User Name "+user_id+" and Is Got From Session "+inst_id);
		Map activeUsers =(Map)context.getAttribute("activeUsers");
		System.out.println(" Active Users Map Cerated ======>#####"+activeUsers+"#############");
		String unique_token=inst_id+"~"+user_id;
		System.out.println("unique_token=======>"+unique_token);
		activeUsers.put(unique_token,session);
		System.out.println("activeUsers.keySet "+activeUsers.keySet());
		System.out.println("Active User Added to Map");
	}
	public void removeUserFromContext(HttpSession session)
	{
		System.out.println("removeUserFromContext function () +++++++++++++ ");
		ServletContext context=checkContextVariable(session);
		String inst_id=(String)session.getAttribute("Instname");
		String user_id=(String)session.getAttribute("USERID");
		String unique_token=inst_id+"~"+user_id;
		viewcontextVariable(context);
		Map activeUsers =(Map)context.getAttribute("activeUsers");
		System.out.println("activeUsers.keySet "+activeUsers.keySet());
		//checking user in context
		HttpSession user_previous_session=(HttpSession)activeUsers.get(unique_token);
		System.out.println("user_previous_session "+user_previous_session);
				
		try{
			user_previous_session.invalidate();
			//activeUsers.remove(unique_token);
			removeMapvalue(session);
		}
		catch (Exception e) 
		{
			System.out.println("Session is already invalidated ");
		}
		System.out.println("removeUserFromContext function () complete +++++++++++++ ");

	}
	
	public void removeMapvalue(HttpSession session)
	{
		//System.out.println("Removing Context Value ===> ");
		ServletContext context=checkContextVariable(session);
		String inst_id=(String)session.getAttribute("Instname");
		String user_id=(String)session.getAttribute("USERID");
		String unique_token=inst_id+"~"+user_id;
		//System.out.println("UNIQUE ID IS ++++===> "+unique_token);
		Map activeUsers =(Map)context.getAttribute("activeUsers");
		System.out.println("Acvtive User ---> "+activeUsers.size());
		//viewcontextVariable(context);
		activeUsers.remove(unique_token);
		//System.out.println("Is contains ==> "+activeUsers.containsKey(unique_token));
		
		//System.out.println("Acvtive User ---> "+activeUsers.size());
		//checkContextVariable(session);
		//viewcontextVariable(context);
		System.out.println("CONTEXT VALUES DELETEED ");
	}
	
	
	public void invalidateUser(HttpSession session,String userid,String inst_id)
	{
		ServletContext context=checkContextVariable(session);
		Map activeUsers =(Map)context.getAttribute("activeUsers");
		//String unique_token=inst_id+"~"+user_id;
	}
	/*public void sessionAssign()
	{
		getRequest().getSession()
	}
	*/
	
	
	 public ServletContext checkContextVariable(HttpSession session)
	 {
		 System.out.println("Check Context Function");	
		 ServletContext context=session.getServletContext();
		 Boolean flag=false;
		 Enumeration context_params=context.getAttributeNames();
		 while(context_params.hasMoreElements())
		 {
			 String context_variable=(String)context_params.nextElement();
			 if(context_variable.equals("activeUsers"))
			 {
				  flag=true;
			 }
		 }
	 	if(!flag)
	 	{
	 		Map activeUsers = new HashMap<>();
	 		context.setAttribute("activeUsers", activeUsers);
	 		System.out.println("New ActivUser Context Created");	
	    }else{
	    	System.out.println("ActivUser Context Available ");
	    }
	 	return context;
	    
	   }

	 public void viewcontextVariable(ServletContext context)
	 {
		// ServletContext context=session.getServletContext();
		// Map activeUsers=(Map)session.getServletContext().getAttribute("activeUsers");
		Enumeration context_params=context.getAttributeNames();
		 while(context_params.hasMoreElements())
		 {
			 String context_variable=(String)context_params.nextElement();
			 System.out.println(" variable inside context "+context_variable);
			 if(context_variable.equals("activeUsers"))
			 {
				 System.out.println(" Active user present !!!!!!!!!!!!!!!!!!!!!!! "+context_variable);
			 }
			 
		 }
	 }
	 
	 
	 
	 
	 public void viewUesrMap(HttpSession session) {
			// Map
			// activeUsers=(Map)session.getServletContext().getAttribute("activeUsers");
			// System.out.println(" Keys inside activeUsers"
			// +activeUsers.entrySet());
			/*
			 * while(activeUsers.hasMoreElements()) {
			 * if(context_params.nextElement().equals("activeUsers")) {
			 * System.out.println(" ACTIVE USER "); Map activeUsers
			 * =(Map)context.getAttribute("activeUsers"); System.out.println(
			 * " Keys inside activeUsers" +activeUsers.keySet()); }
			 */

			Map activeUsers = (Map) session.getServletContext().getAttribute("activeUsers");
			System.out.println("ACTIVE USERS" + activeUsers);
			System.out.println(" Keys inside activeUsers" + activeUsers.entrySet());
			ServletContext context = session.getServletContext();
			System.out.println("Inside while condition");
			Enumeration context_params = context.getAttributeNames();

			System.out.println("Inside while condition");
			while (context_params.hasMoreElements()) {

				if (context_params.nextElement().equals("activeUsers")) {
					System.out.println(" ACTIVE USER ");
					activeUsers = (Map) context.getAttribute("activeUsers");
					System.out.println(" Keys inside activeUsers" + activeUsers.keySet());
				}
			}
		}
	/*public void viewUesrMap(HttpSession session)
	{
		Map activeUsers=(Map)session.getServletContext().getAttribute("activeUsers");
		System.out.println(" Keys inside activeUsers" +activeUsers.entrySet());
		
		while(activeUsers.hasMoreElements())
		 {
			 if(context_params.nextElement().equals("activeUsers"))
			 {
				 System.out.println(" ACTIVE USER ");
				 Map activeUsers =(Map)context.getAttribute("activeUsers");
				 System.out.println(" Keys inside activeUsers" +activeUsers.keySet());
			 }
			 
	}*/
}
	
