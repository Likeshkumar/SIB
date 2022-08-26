package com.ifp.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

public class InstUserHomeAction extends BaseAction
{
	
	
/*public String instUserHomeAction()	 
	{
	
	
	     HttpSession session = getRequest().getSession();
		
		 HttpServletRequest req=ServletActionContext.getRequest();
	     HttpSession session= req.getSession();
	    System.out.println( "branch code is ___________::::::    " + session.getAttribute("BRANCH_CODE"));
	     *//*** comment vy ravi ****//*
	   
	    String BRANCHCODE= (String) session.getAttribute("BRANCH_CODE");
	    System.out.println("branch code---- "+BRANCHCODE);	
	    
	    trace("TTTTTTTTTTTTTTTTTTTTTTT");
		return "sucess";
	
	}*/
	
	 public String instUserHomeAction()	 
		{
			HttpSession session = getRequest().getSession();	 
			System.out.println( "branch code is ___________ " + session.getAttribute("BRANCHCODE"));
			return "sucess";	
		}
	
	 
	

}
