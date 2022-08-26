package com.ifp.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 
 * @author Prasad
 *
 */
public class ClickJackInterceptor implements Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		System.out.println("----Destroying  ClickJAckInterceptors---");
	}

	@Override
	public void init() {

		System.out.println("----Initalizing ClickJAckInterceptors---");
	}

	@Override
	public String intercept(ActionInvocation Invocation) throws Exception {

		final ActionContext ac = Invocation.getInvocationContext();

		HttpServletRequest req = (HttpServletRequest) ac.get(StrutsStatics.HTTP_REQUEST);
		/*
		 * HttpSession ses= req.getSession(); String
		 * tokencsrf=UUID.randomUUID().toString();
		 * 
		 * 
		 * System.out.println("tokencsrf----->    "+tokencsrf);
		 * ses.setAttribute("csrfToken", tokencsrf);
		 */

		HttpServletResponse response = (HttpServletResponse) ac.get(StrutsStatics.HTTP_RESPONSE);
		// HttpServletResponse response = ServletActionContext.getResponse();

		response.addHeader("X-Frame-Options", "SAMEORIGIN");
		response.addHeader("Content-Security-Policy-Report-Only",
				"default-src 'self'; script-src 'self' 'unsafe-inline'; object-src 'none'; style-src 'self' 'unsafe-inline'; img-src 'self'; media-src 'none'; frame-src 'none'; font-src 'self'; connect-src 'self'; report-uri NONE");
		/*response.addHeader("X-Content-Security-Policy-Report-Only",
				"default-src 'self'; script-src 'self' 'unsafe-inline'; object-src 'none'; style-src 'self' 'unsafe-inline'; img-src 'self'; media-src 'none'; frame-src 'none'; font"
				+ "-src 'self'; connect-src 'self'; report-uri NONE");*/
		/*response.addHeader("Content-Security-Policy",
				"default-src 'self'; script-src: 'self' static.domain.tld");*/
		String sessionid = req.getSession().getId();
		response.addHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; HttpOnly;Secure");
		//response.addHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; secure");
		System.out.println("response is ------>   " + response);
		return Invocation.invoke();
	}

}
