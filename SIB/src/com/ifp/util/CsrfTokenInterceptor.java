package com.ifp.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class CsrfTokenInterceptor implements Interceptor {

	/**
	 * 
	 */
	String tokenCSRF =UUID.randomUUID().toString();
	
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {	
	}

	@Override
	public String intercept(ActionInvocation Invocation) throws Exception {
		final ActionContext ac = Invocation.getInvocationContext();
		HttpServletRequest req = (HttpServletRequest) ac.get(StrutsStatics.HTTP_REQUEST);
		HttpSession ses = req.getSession(false);
	/*	
		if(ses!=null){
			
			
		System.out.println("caLLING ");
		System.out.println("csrftoken----->    " + tokenCSRF);
		ses.getAttribute("");
			
		}*/
	
		return Invocation.invoke();
	}

}
