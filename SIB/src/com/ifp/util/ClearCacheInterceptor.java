package com.ifp.util;

import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor; 

public class ClearCacheInterceptor  extends AbstractInterceptor{

static final Logger ClearCacheLogger = Logger.getLogger(ClearCacheInterceptor.class);
private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception { 
		System.out.println("Clearing cache....");
		ActionContext context=(ActionContext)invocation.getInvocationContext(); 
		HttpServletResponse response=(HttpServletResponse)context.get(StrutsStatics.HTTP_RESPONSE); 
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		ClearCacheLogger.info("Cache Cleared");
		String result=invocation.invoke();
		return result;
	}
}
 