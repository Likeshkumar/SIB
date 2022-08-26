<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
 

<script type="text/javascript" src="js/script.js"></script>
 
 <script>
 	function validate(){
 		if( confirm("Do you want to continue...") ){
 			parent.showprocessing();	
 		}else{
 			return false;
 		}
		
 	} 
 	
 	function checkValidCard(){
 	 
 		var newcardno = document.getElementById("newcardno"); 
 		
 		var submit =  document.getElementById("submit");
 		if( newcardno.value == "" ){
 			errMessage(newcardno, "New card number is empty");
 			return false;
 		}else if( newcardno.value.length < 16 ){
 			errMessage(newcardno, "New card number length is invalid");
 			return false;
 		}
 		
 		var url = "checkValidForReIssueCardCardMaintainAction.do?cardno="+newcardno.value;
 		 
 		var result = AjaxReturnValue(url);
 		 
 		if( result != "1"){
 			errMessage(newcardno, newcardno.value +"  It is not a valid card for Re-issuance...");
 			return false;
 		}
 	 
 		return true;
 		
 		
 		
 	}
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="reIssuCardActionCardMaintainAction.do" name="orderform" onsubmit="validate()" autocomplete="off" namespace="/" >

 
   
  <table  border="0" cellpadding="0" cellspacing="4" width="80%" class="formtable"  > 
		<s:hidden name="cardno" value="%{cardmainbean.cardno}" />
		<tr> 
			<td > Card Number </td> <td class="textcolor" > : <s:property value="cardmainbean.cardno"/>  </td> 
			<td > Card Expiry date </td> <td class="textcolor" > : <s:property value="cardmainbean.expiredate"/>  </td> 
			<td > Status Marked by   </td> <td class="textcolor" > : <s:property value="cardmainbean.username"/>  </td> 
		</tr> 
		<tr> 
			<td > Status Marked date </td> <td class="textcolor" > : <s:property value="cardmainbean.statusmakreddate"/>  </td> 
			<td > Custome Code  </td> <td class="textcolor" > : <s:property value="cardmainbean.customerid"/>  </td>  
			<td colspan="2">  </td> 
		</tr> 
		<tr> 
			<td > Custome Name  </td> <td class="textcolor" > : <s:property value="cardmainbean.customername"/>  </td> 
			<td > Phone No.  </td> <td class="textcolor" > : <s:property value="cardmainbean.phoneno"/>  </td> 
			<td > Mobile No.  </td> <td class="textcolor" > : <s:property value="cardmainbean.mobileno"/>  </td> 
		</tr> 
		<tr>
			<td colspan="6"> New Card Number : &nbsp; &nbsp; &nbsp; &nbsp;  <input type="text" name="newcardno" id="newcardno"  maxlength="19" /> </td>
		</tr>
		
 </table>
 
 <table border="0" cellpadding="0" cellspacing="10" width="20%" >
				<tr>
				<td>
					<s:submit value="Re-Issue Card" name="submit" id="submit" onclick="return checkValidCard()" />
				</td>
				<td>
					<input type="button" name="cancel" id="cancel" value="Back"  class="cancelbtn"  onclick="goBack()"/>
					 
				</td> 
				 
				</tr>
		</table>
			
			  
</s:form>

