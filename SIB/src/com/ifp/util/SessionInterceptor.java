package com.ifp.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SessionInterceptor implements Interceptor {
	private static final long serialVersionUID = 1L;

	public String intercept(ActionInvocation invocation) throws Exception {
		 
		Map<String,Object> session = invocation.getInvocationContext().getSession();
		
		if(session.isEmpty()) {
			 System.out.println(" Session Expired "); 
			 return "session"; // session is empty/expired
		}    
		System.out.println("Clearing caches....");
		HttpServletResponse response = ServletActionContext.getResponse(); 
	   response.setHeader("Cache-Control", "no-store");
	   response.setHeader("Pragma", "no-cache");
	   response.setDateHeader("Expires", 15);     
	    return invocation.invoke();
}

	public void destroy() {
		System.out.println("Destroying MyLoggingInterceptor...");

	}

	public void init() {

		System.out.println("Initializing MyLoggingInterceptor...");
	}

}