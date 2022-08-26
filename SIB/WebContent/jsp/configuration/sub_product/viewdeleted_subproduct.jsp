<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript">
	function getSubProd( instid,userid,selprodid, authstatus ){  
 		prodid = selprodid.split("~"); 
	 		//var url = "getSubProdList.do?instid="+instid+"&prodid="+prodid[0];
	 		var url = "getAuthSubProdList.do?instid="+instid+"&prodid="+prodid[0]+"&AUTHSTATUS="+authstatus+"&userid="+userid;
	 		//alert("url :" + url);
	 		result = AjaxReturnValue(url).split("~");
	 		subprodlist = result[0];
	 		maxallcard = result[1];
	 		document.getElementById("subproductlist").innerHTML = result;	 		
 	}
	
	function goBack()
	{
		window.history.back();
	}
	
	function selectall()
	{
		valid=true;
		var selectbox = document.getElementById("cardtype");
		var selectsubproductbox = document.getElementById("subproductlist");
		if(selectbox.value == "-1")
		{
			errMessage(cardtype,"SELECT PRODUCT");
			return false;
		}
		if(selectsubproductbox.value == "-1")
		{
			errMessage(subproductlist,"SELECT SUB-PRODUCT");
			return false;
		}
		return valid;
	}
</script>
</head>
<%
	String editorder = null;
	String instid = (String)session.getAttribute("Instname");
	String userid = (String)session.getAttribute("USERID"); 
%>

<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action='%{subproductbean.formaction}' >
<input type="hidden" name="actflag" id="actflag" value="${actflag}"> 
<s:hidden name="doact" id="doact" value="%{subproductbean.doact}"/>
<table border="0" cellpadding="0" cellspacing="0"   width="50%" class="formtable" >
 	<tr>
		<td align="right">
		 SUB-PRODUCT
		</td>
		<td> : 
 				
 				 
				 <select name="subproductlist" id="subproductlist" >  
				  	<option value="-1">--Select -- </option>
	 					<s:iterator  value="prodlist">
	 						<option value="${SUB_PROD_ID}">${SUB_PRODUCT_NAME}</option>
	 					</s:iterator>
	 			</select>
	 				
		</td>
    </tr>
    <tr>
    </tr>
     
</table>
<table>
<tr>
	<td>
		
		<s:if test="enableauth"> 
		 	<input type="submit" value="View & Authorize"  name="auth" onclick="return selectall();" />
		 	<input type="hidden" name="next" id="next" value="viewauth">
		</s:if>
		<s:else>
		 	<input type="submit" value="View" name="view" onclick="return selectall();" />
		 	 <s:if test="%{statusflag=='edit'}"><input type="hidden" name="next" id="next" value="viewedit"></s:if>
		 	 <s:else><input type="hidden" name="next" id="next" value="viewauth"></s:else>
		</s:else> 
	</td>
</tr>
</table>
</s:form>