package com.ifp.util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class XSSInterceptor  implements Interceptor{

	@Override
	public void destroy() {
		
		
	}

	@Override
	public void init() {
		
		
	}

	

	@Override
	public String intercept(ActionInvocation action) throws Exception {
		System.out.println("------------------caling XSSInterceptor-------------");

	    final ActionContext ac = action.getInvocationContext();
		 HttpServletRequest req=ServletActionContext.getRequest();
		   //HttpServletResponse response = (HttpServletResponse) ac.get(StrutsStatics.HTTP_RESPONSE);
		 new XSSRequestingWrapper(req);
		return action.invoke() ;
	}

}
