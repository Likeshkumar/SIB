package com.ifp.maintain;

import org.apache.struts2.convention.annotation.Actions;

import com.opensymphony.xwork2.ActionSupport;

public class EditGridEntry extends ActionSupport {

	  private String              oper;
	  private String              id;
	  private String              name;
	  private String              country;
	  private String              city;
	  private double              creditLimit;

	 
	  public String execute() throws Exception
	  {
	    
		  System.out.println( "json example started");
		return SUCCESS;
	    
	    
	  }
}