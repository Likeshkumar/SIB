<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 

 
<script type="text/javascript" src="js/script.js"></script> 
<script>
 	function getSubProd( instid, selprodid ){  
 		//alert("instid==> "+instid);	
 		prodid = selprodid.split("~"); 
	 		var url = "getSubProdList.do?instid="+instid+"&prodid="+prodid[0];  
	 		//alert("url===> "+url);
	 		result = AjaxReturnValue(url).split("~");
	 		//alert("result : " + result);
	 		subprodlist = result[0];
	 		maxallcard = result[1];
	 		//alert("subprodlist==> "+subprodlist);
	 		//alert("maxallcard==> "+maxallcard);
	 		document.getElementById("subproductlist").innerHTML = result; 
	 		  
	 		
 	}
 	function maxAllowedCards( instid, subprodid ){
 		var bin = document.getElementById("cardtype").value;
 		if( bin != "-1~-1"){
 			var binval = bin.split("~");
 		var url = "getMaxCardCount.do?instid="+instid+"&subprodid="+subprodid+"&bin="+binval[0];
 		document.getElementById("count").value ="";
 		var result = AjaxReturnValue(url);
 		 
 		document.getElementById("maxallowedcard").value = result;
 		}else{
 			alert("select bin");
 		}
 	}
 	
 	function checkMaxcards( givenmaxcards ){
 		var maxallcards = 1;
 		if ( document.getElementById("maxallowedcard").value != "" ){
 		 		maxallcards = document.getElementById("maxallowedcard").value;
 		} 
 		 
 		
 		if( parseInt (givenmaxcards) > parseInt(maxallcards) ){ 
 			document.getElementById("order").disabled=true; 			
 			errMessage(maxallcards, "Maximum No.of cards allowed " + maxallcards );		
 			
 		}else{
 			document.getElementById("order").disabled=false;
 		}
 	}
 </script>
 

<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String editorder = null;
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	editorder = (String)session.getAttribute("update");
	String dummy=(String)session.getAttribute("dummy");
%>
<div align="center">
<s:form action="saveOrderInstCardorderAction"  name="orderform" onsubmit="return validateinstorder()" autocomplete = "off"  namespace="/">
 
<table border="0" cellpadding="0" cellspacing="0" width="40%" class="" >	 



	
		<tr bgcolor="#3197e0" align="center" >
				<td> Select Branch</td>
				<td> Product </td>
		        <td>Sub-Product</td>
		        <td> No. Of Cards</td>
		
				</tr>
				
				
				<tr>
				<%if(usertype.equals("INSTADMIN"))
		{					
	%>
				<%-- <td> <s:select name="branchcode" id="branch" listKey="BRANCH_CODE" listValue="BRANCH_NAME" list="branchlist"  headerValue="Select Branch" headerKey="-1" tooltip="Select Branch"/> --%>
				<td>
					<select name="branchcode" id="branch">
					<option value="-1~-1">--Select -- </option>
 					<s:iterator  value="branchlist">
 					<option value="${BRANCH_CODE}">${BRANCH_CODE} - ${BRANCH_NAME}</option>
 					</s:iterator>
					</select>
				</td>			
	<%	
		}
				else
				{
			%>
					
					
					<td> : <%out.println( session.getAttribute("BRANCH_DESC") ); %></td>
						<s:hidden name="branchcode" id="branchcode" value="%{#session.BRANCHCODE}" />
					
			<%		
				}
			%>
				
				<td> 
				
 				<select name="cardtype" id="cardtype" onchange="getSubProd( '<%= instid %>', this.value );">
 				<option value="-1~-1">--Select -- </option>
 				<s:iterator  value="prodlist">
 				<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
 				</s:iterator>
 				</select>
		       </td>
				
				<td> 
    		 
    			<select name="subproduct" id="subproductlist" onchange="maxAllowedCards('<%= instid %>', this.value)">
    				<option value="-1">--Select Sub-Product--</option>
    			</select>
    	 
    	      </td>
			<td>  <input type="hidden" name="Ordertype" value="I">
			<input type="hidden" name="maxallowedcard" id="maxallowedcard" />
   	   		<input type="text" name="count" id="count"   maxlength="10" onKeyPress="return numerals(event);" onchange="checkMaxcards(this.value)">
		</td>
	     </tr>
	
		
	
	
    	
   
	 
</table>


<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
		<input type="hidden" name="token" id="csrfToken"value="${token}">
			<s:submit value="Order"	name="order" id="order" onclick="return validateinstorder()"/>
		</td>
		<td>
		
			<input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
</table>
</s:form>
</div>
 