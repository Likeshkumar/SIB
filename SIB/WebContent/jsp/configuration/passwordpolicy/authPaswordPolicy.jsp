<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title> New Document </title>
  <meta name="Generator" content="EditPlus">
  <meta name="Author" content="">
  <meta name="Keywords" content="">
  <meta name="Description" content="">
 </head>
<script>
function calcTotal()
{
var LowerCaseAlphabetsAllowed  = document.getElementById('LowerCaseAlphabetsAllowed').value;
var UpperCaseAlphabetsAllowed  = document.getElementById('UpperCaseAlphabetsAllowed').value;
var NumbersAllowed  = document.getElementById('NumbersAllowed').value;
var SpecialCharactersAllowed  = document.getElementById('SpecialCharactersAllowed').value;
var FirstCharacter  = document.getElementById('FirstCharacter').value;
var LastCharacter  = document.getElementById('LastCharacter').value;
var TotalCount  = document.getElementById('TotalCount').value;
TotalCount =  Number(LowerCaseAlphabetsAllowed) + Number(UpperCaseAlphabetsAllowed) + Number(NumbersAllowed) + Number(SpecialCharactersAllowed);
// alert(TotalCount);
 document.getElementById('TotalCount').value = TotalCount;
}
function checkPassword()
{
var LowerCaseAlphabetsAllowed  = document.getElementById('LowerCaseAlphabetsAllowed').value;
var UpperCaseAlphabetsAllowed  = document.getElementById('UpperCaseAlphabetsAllowed').value;
var NumbersAllowed  = document.getElementById('NumbersAllowed').value;
var SpecialCharactersAllowed  = document.getElementById('SpecialCharactersAllowed').value;
var FirstCharacter  = document.getElementById('FirstCharacter').value;
var LastCharacter  = document.getElementById('LastCharacter').value;
var TotalCount  = document.getElementById('TotalCount').value;
//alert(TotalCount);
TotalCount =  Number(LowerCaseAlphabetsAllowed) + Number(UpperCaseAlphabetsAllowed) + Number(NumbersAllowed) + Number(SpecialCharactersAllowed);
// alert(TotalCount);
/*
alert('LowerCaseAlphabetsAllowed : ' +LowerCaseAlphabetsAllowed);
alert('UpperCaseAlphabetsAllowed : ' +UpperCaseAlphabetsAllowed);
alert('NumbersAllowed  : '+NumbersAllowed);
alert('SpecialCharactersAllowed : '+ SpecialCharactersAllowed);
alert('FirstCharacter  : ' +FirstCharacter);
alert('LastCharacter  : ' +LastCharacter);
alert('TotalCount  : ' +TotalCount);
*/



}



function edit_confirm(value,listid,instid){
	if (value=="DeAuthorize"){
 	var reason = prompt('Enter the Reason for Reject?');
	 if( reason ){
		 var url = "authorizePasswordPolicyPasswordPolicyAction.do?auth="+value+"&listid="+listid+"&reason="+reason+"&instid="+instid;
		 window.location = url; 
	 }  
	 return false;
	}
	else
		{
		if(confirm("are sure wat to authorize?"))
			{
			 var url = "authorizePasswordPolicyPasswordPolicyAction.do?auth="+value+"&listid="+listid+"&instid="+instid;
			 window.location = url; 
			}
		return false;
		}
}
</script>

 <body>
 <jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="authorizePasswordPolicyPasswordPolicyAction.do"  name="passwordPolicy" autocomplete = "off" namespace="/"> 
  <table align="center" border="0"  cellspacing="1" cellpadding="0" width="90%" class="formtable">
		<tr>
				<th> Lower Case Alphabets   </th>
				<th> Upper Case Alphabets   </th>
				<th> Numbers Allowed   </th>
				<th> Special Char  </th>
					<th> First Char  </th>
				<th> Last Char   </th>
				<th> Total    </th>
				<th> Reason   </th>
				<th> Authorize/Deauthorize   </th>
		</tr>	
		 <s:iterator value = "passPolicyList">
		<tr>	
		<input type = "hidden" value = "${ID}" name = "listid"/> 
				<th>${LOWERCASE}</th>
				<th>${UPPERCASE}</th>
				<th>${NUMBERS}</th>
				<th>${SPECIAL}</th>
				<th>${FIRSTCHAR_STRING}</th>
				<th>${LASTCHAR_STRING}</th>
				<th>${TOTALCOUNT}</th>
				<th>${REASON}</th>
				<td> 
				Authorize<input type="image"  id = "auth0" src="images/enable.gif" alt="submit Button" onclick="return edit_confirm('Authorize','${ID}','${INST_ID}');">
				Reject<input type="image"  id = "auth1" src="images/disabled.gif" alt="submit Button" onclick="return edit_confirm('DeAuthorize','${ID}','${INST_ID}');">
				</td>
				</tr>
		 </s:iterator>		
				

  </table>
   <tr>
  <td colspan = 2>

 </td>

  </table>
</s:form>


 </body>
</html>
