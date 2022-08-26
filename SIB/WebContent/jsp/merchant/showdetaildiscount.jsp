<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html>  

  <body>

   
   <s:if test="mprbean.issubfeeexist">    
     <table border="0" cellpadding="0" cellspacing="6" class="formtable"> 
     	   
    <tr style="">
	   <s:iterator value='mprbean.subdiscountlist'>
  	   <s:set name="isAmt" value="subdiscountlist.lowtxncnt"></s:set>	
	   <s:property value="mprbean.isAmt"/>
	   <s:if test='mprbean.isamountdis'>  
	    <tr>   			  		  		
			<td class="brown">LOW TRANSACTION AMOUNT </td> 
			<td class="brown">HIGH TRANSACTION AMOUNT</td>
		</tr>
		<tr>   			  		  		
			<td>${LOWTXNAMT}</td> 
			<td>${HIGHTXNAMT}</td>
		</tr>
		</s:if>
		<s:else>
		<tr>   			  		  		
			<td class="brown">LOW TRANSACTION  COUNT</td> 
			<td class="brown">HIGH TRANSACTION COUNT</td>
		</tr>
		<tr>
			<td>${LOWTXNCNT}</td>
			<td>${HIGHTXNCNT}</td>
		</tr>
		</s:else>
   		</s:iterator>
	 </tr>
	 <tr style="">
	 	<td colspan="2">
			 <table border="0" cellpadding="0" cellspacing="6" >
				 <tr>
				 	<th>
				 		TRANSACTION NAME
				 	</th>
				 	<th>
				 		TRANSACTION AMOUNT
				 	</th>
				 	<th>
				 		TRANSACTION MODE
				 	</th>
				 </tr>
			 <s:iterator value="mprbean.translist">
			  	<tr>
				  	<td class="brown">
					 		${TXN_CODE}
					</td>
				 	<td>
				 		${DISCOUNT_AMT}
				 	</td>
				 	
				 	<td>
				 		${DISCOUNT_MODE}
				 	</td>
				 	
				 </tr>
			 </s:iterator>
			 </table>
		 </td>
	 </tr>
 	</table>
   </s:if>
   <s:else>
   <table border="0" cellpadding="0" cellspacing="0"> 
  		<tr> <td> <div class="node ">Error in Fetch Data</div> </td> </tr> 	
  	</table>
   </s:else>   
	
</body></html> 