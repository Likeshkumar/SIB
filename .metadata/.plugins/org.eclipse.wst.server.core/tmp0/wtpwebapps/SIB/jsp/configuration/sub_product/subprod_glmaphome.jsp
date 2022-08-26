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
%>

<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="subProdGlMapListAddSubProdAction.do" autocomplete="off">
<table border="0" cellpadding="0" cellspacing="0"   width="50%" class="formtable" >
 	<tr>
		<td align="right">
		  PRODUCT
		</td>
		<td> :
				
 				<select name="cardtype" id="cardtype" onchange="getSubProd( '<%= instid %>', this.value );">
 				<option value="-1">--Select -- </option>
 				<s:iterator  value="prodlist">
 				<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
 				</s:iterator>
 				</select>
		</td>
    </tr>
    <tr>
    </tr>
    <tr> 
    	<td align="right">
    		 SUB-PRODUCT
    	</td>
    	<td> :
    		 
    			<select name="subproduct" id="subproductlist" onchange="maxAllowedCards('<%= instid %>', this.value)">
    				<option value="-1">--Select Sub-Product--</option>
    			</select>
    	 
    	</td>
    </tr>	
</table>
<table>
<tr>
	<td>
		<input type="submit" value="Next" name="view" onclick="return selectall();" />
	</td>
</tr>
</table>
</form>