package com.ifp.util;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class BrowserClearCacheInterceptor  implements  Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
	
	}

	@Override
	public void init() {
		
		
	}

	@Override
	public String intercept(ActionInvocation Invocation) throws Exception {
		
	    System.out.println("calling CleearCache---->  ");
		
		    final ActionContext ac = Invocation.getInvocationContext();
		    HttpServletResponse response = (HttpServletResponse) ac.get(StrutsStatics.HTTP_RESPONSE);
		    response.setDateHeader("Expires", 0);
		    response.setDateHeader("Last-Modified", (new Date()).getTime() );
		    response.setHeader("Pragma", "no-cache");
		    response.setHeader("Cache-Control", "private,no-cache,no-store,must-revalidate,pre-check=0, post-check=0, max-age=0, s-maxage=0");
		   
		return Invocation.invoke();
	
	}

}
