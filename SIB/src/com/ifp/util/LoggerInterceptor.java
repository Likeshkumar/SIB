package com.ifp.util;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class LoggerInterceptor implements Interceptor{
	

	public String intercept(ActionInvocation invocation) throws Exception
	{
		System.out.println("~~~~~~~~~~~~~~~~~~~   LOGGER INTERCEPTOR  ~~~~~~~~~~~~~~~~~~~~~~~");
			Map<String,Object> session = invocation.getInvocationContext().getSession();
			//if(session.isEmpty())
			//	{
			//		 System.out.println("Session Expired ");
					
			//		 return "session"; // session is empty/expired
			//	}
			Logger logger = Logger.getLogger(invocation.getClass());	
			System.out.println("Session Not Expired ::" +invocation.getInvocationContext());
			return invocation.invoke();
	}
	
	public void destroy()
	{
		System.out.println("Destroying LOGGER Interceptor...");
	}
	public void init() 
	{
		System.out.println("Initializing LOGGER Interceptor...");
	}
	
	
}
