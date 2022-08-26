<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

<style>
	ul { 
	list-style-type: none;
	}
</style>

<%
	String curerr=null;
	String curmsg=null;
	String preverr = null;
	String prevmsg= null;
	
	
	curerr = (String) session.getAttribute("curerr");
	curmsg=(String) session.getAttribute("curmsg");
	preverr=(String) session.getAttribute("preverr");
	prevmsg=(String) session.getAttribute("prevmsg");
	
	session.removeAttribute("curerr");
	session.removeAttribute("curmsg");
	session.removeAttribute("preverr");
	session.removeAttribute("prevmsg");
	
	
 	  //out.println( " curerr " + curerr + "\n" );out.println( " curmsg " +  curmsg + "\n" );out.println( " preverr " +  preverr + "\n" );out.println( " prevmsg " +  prevmsg + "\n" ); 

 %>
 

 <% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
 response.setHeader("Pragma","no-cache"); //HTTP 1.0 
 response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>

<table border='0' cellpadding='0' cellspacing='0' width='80%'
	style='text-align: center;'>
	<tr>
		<td style='text-align: center;' id='errmsg'>&nbsp; 
<%			if(preverr != null ) {  
			  if(preverr != "S" ) { 
				  if(prevmsg != null ){                                                %>
					<font color="red"><b><%=prevmsg%></b></font>
<%				}
			  }else if (preverr != "E" ) {
					if(prevmsg != null ) {                                              %>
						<font color="green"><b>	<%=prevmsg%></b></font>				 
<%				        }
			  }
			}
			if(curerr != null ){
				if(curerr.equals("S") ) {
					if(curmsg != null ) {                                                %>
						<font color="green"><b> <%=curmsg%></b></font> 
<%					} 
				}else if ( curerr.equals("E") ){
						if(curmsg != null ) {                                            %> 
							<font color="red"><b><%=curmsg%></b></font> 
<%						}
			 	}
			 }																			 %>
			 
			<s:if test="hasActionErrors()"> 
			    <font color="red"><b> 	<s:actionerror/> </b></font> 
				 
			</s:if> 
			<s:elseif   test="hasActionMessages()">
			 	<font color="green"><b>	<s:actionmessage/> </b></font>	  
			</s:elseif>

		</td>
	</tr>
<tr>
 
</table>
