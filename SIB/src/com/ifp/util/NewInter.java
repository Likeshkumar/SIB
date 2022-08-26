package com.ifp.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class NewInter implements Interceptor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public String intercept(ActionInvocation action) throws Exception 
	{
		//System.out.println("TTTTTTTTTTTT");
		final ActionContext ac = action.getInvocationContext();
		HttpServletRequest request=ServletActionContext.getRequest();
		String url = request.getRequestURL().toString();
       // System.out.println("URL Checking-->"+url);
     
       
        String query = request.getQueryString();
        //System.out.println("Query Value Check-->"+query);
             	
        String URL=url+query; 
        String decodequery=decode(URL);
       // System.out.println("decode url-->"+decodequery);
      
        boolean b=isValid(url+decodequery);
		//System.out.println(" getitn act value------> " + b);	
/*		if(b==false)
		{
			System.out.println("not Valied URL");
			return "required_home1";
		} 
		else
        {
			System.out.println("Valied URL");
			return action.invoke();
        }*/
		return action.invoke();
	}
	public static boolean isValid(String url) 
    { 
        try 
        { 
        	//System.out.println("checking url-->"+url);
            new URL(url).toURI(); 
            return true; 
        } 
        catch (Exception e) 
        { 
            return false; 
        } 
    }
    
    public static String decode(String url)  
    {  
      try {  

    	  		   //System.out.println("T-->"+url);
                   String decodeURL=null;
                   if(!(url==null))  
                   {  
                        decodeURL=URLDecoder.decode(url, "UTF-8" );  
                   }  
                   return decodeURL;  
              } 
      
      		  catch (UnsupportedEncodingException e) 
              {  
                   return "Issue while decoding" +e.getMessage();  
              }  
    }  
}
