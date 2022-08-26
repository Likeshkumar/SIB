<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

 
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript">
	function getSubProd( instid, selprodid ){  
 		prodid = selprodid.split("~"); 
	 		var url = "getSubProdList.do?instid="+instid+"&prodid="+prodid[0];  
	 		result = AjaxReturnValue(url).split("~");
	 		subprodlist = result[0];
	 		maxallcard = result[1];
	 		document.getElementById("subproductlist").innerHTML = result; 
	 		  
	 		
 	}
	
	function goBack()
	{
		window.history.back();
	}
	
	function validate()
	{ 		
		valid=true;
		var glkey = document.getElementById("glkey");
		var subglcode = document.getElementById("subglcode");
		if(glkey.value == "-1")	{		errMessage(glkey,"Select Gl Key");	return false;	}
		if(subglcode.value == "-1"){errMessage(subglcode,"Select Sub-Gl Code");	return false;}
		parent.showprocessing()
		return valid;
	}
	
	function deleteRecords(recordid){
		var url = "deleteMappedRecordAddSubProdAction.do?recordid="+recordid;
		if( confirm ("Do you want to delete ") ){
			window.location = url;
		}
		return false;
		
	}
</script>
</head>
<%
	String editorder = null;
	String instid = (String)session.getAttribute("Instname");
%>

<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="subProdGlMapActionAddSubProdAction.do" autocomplete="off">
<table border="0" cellpadding="0" cellspacing="0"   width="50%" class="formtable" >
 	<tr>
		<td align="right">
		  PRODUCT
		</td>
		<td> : <%=session.getAttribute("PROD_DESC") %>
			<input type="hidden" name="productcode" value='<%=session.getAttribute("PROD_CODE")%>'  />
		</td>
		
		<td align="right">
    		 SUB-PRODUCT
    	</td>
    	<td> :
    		  <%=session.getAttribute("SUBPROD_DESC") %> 
    		  	<input type="hidden" name="subproduct" value='<%=session.getAttribute("SUBPROD_CODE")%>'  />
    	</td>
    	
    </tr>
    <tr>
    </tr>
    
     <tr> 
    	<td align="right">
    		GL Keys : 
    	</td>
    	<td> :
    		 <s:select list="glkeyslist" name="glkey" id="glkey" listKey="TXNKEY" listValue="TXNKEYDESC" headerKey="-1" headerValue="-SELECT-"/>
    	</td>
    	
    	<td align="right">
    		GL Code : 
    	</td>
    	<td> :
    		 <s:select list="subgllist" name="subglcode" id="subglcode" listKey="SCH_CODE" listValue="SCH_NAME" headerKey="-1" headerValue="-SELECT-"/>
    	</td>
    </tr>	
    
    <tr><td colspan="4"> &nbsp; </td></tr>
    
    
    <tr><td colspan="4"> 
    	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="formtable">
    	
    	<s:if test="ismappedavailable">
    		<tr> <th>Gl Key</th>  <th>Gl Code </th> <th> Delete </th> </tr> 
    		<s:iterator value="mappedgl_list">
    			<tr> <td>${GL_TYPE}</td> <td>${GL_CODE}</td> <td> <img src="images/delete.png" onclick="return deleteRecords('${RECORD_ID}')" /></td> </tr>
    		</s:iterator>
    	</s:if>
    	</table>
    </td></tr>
</table>
<table>
<tr>
	<td>
		<input type="submit" value="Add" name="view" onclick="return validate();" />
	</td>
</tr>
</table>
</form>