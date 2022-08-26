<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js"></script>
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
	var instname  = document.getElementById('instname');
	
	var LowerCaseAlphabetsAllowed  = document.getElementById('LowerCaseAlphabetsAllowed').value;
	var UpperCaseAlphabetsAllowed  = document.getElementById('UpperCaseAlphabetsAllowed').value;
	var NumbersAllowed  = document.getElementById('NumbersAllowed').value;
	var SpecialCharactersAllowed  = document.getElementById('SpecialCharactersAllowed').value;
	
	
	var FirstCharacter  = document.getElementById('FirstCharacter').value;
	//alert(FirstCharacter);
	//alert(FirstCharacter);
	var LastCharacter  = document.getElementById('LastCharacter').value;
	//if( FirstCharacter == "-1" ){ errMessage(FirstCharacter, "Please Select FirstCharacter");  return false;  }
//	if( LastCharacter == "-1" ){ errMessage(LastCharacter, "Please Select LastCharacter");  return false;  }
	var TotalCount  = document.getElementById('TotalCount');
	//TotalCount =  Number(LowerCaseAlphabetsAllowed) + Number(UpperCaseAlphabetsAllowed) + Number(NumbersAllowed) + Number(SpecialCharactersAllowed);
	if( instname.value == "-1" ){ errMessage(instname, "Please Select Institution");  return false;  }
	if( TotalCount.value < 6 ){ errMessage(TotalCount, "Total count should be 6 and above");  return false;  }
	  return true;
}
	
</script>

 <jsp:include page="/displayresult.jsp"></jsp:include>
 
<s:form action="passwordPolicyInsertPasswordPolicyAction"  name="passwordPolicy" autocomplete = "off" namespace="/"> 
<%String username = (String)session.getAttribute("SS_USERNAME");
	String usertype = (String)session.getAttribute("USERTYPE");
	
	%>

<table border='0' cellpadding='0' cellspacing='0' width='30%' >

	<tr>
			<td>
			Institution :
			</td>
			<td>
					
	 				
	 				<select name="instname" id="instname">
	 				<option value="-1">--Select Institution--</option>
	 				<s:iterator  value="institutionList">
	 				<option value="${INST_ID}">${INST_NAME}</option>
	 				</s:iterator>
	 				</select>
			</td>
	</tr>
</table>
 <table border=1 >
		<tr>
				<th style = "text:align:left"> Lower Case Alphabets Allowed :  </th>
				<th>
					  <select name="LowerCaseAlphabetsAllowed" id = "LowerCaseAlphabetsAllowed" onchange = "calcTotal();" >
						<option value="1" selected>1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					  </select>
				<th>
		</tr>
		<tr>
				<th style = "text:align:left"> Upper Case Alphabets Allowed :  </th>
				<th>
					<select name="UpperCaseAlphabetsAllowed" id = "UpperCaseAlphabetsAllowed" onchange = "calcTotal();"()>
						<option value="1" selected>1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</th>
		</tr>
	   <tr>
				<th style = "text:align:left"> Numbers Allowed :  </th>
				<th>
					<select name="NumbersAllowed" id="NumbersAllowed" onchange = "calcTotal();">
						<option value="1" selected>1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</th>
      </tr>
	  <tr>
				<th style = "text:align:left"> Special Characters Allowed :  </th>
				<th>
					<select name="SpecialCharactersAllowed" id = "SpecialCharactersAllowed" onchange = "calcTotal();">
						<option value="1" selected>1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</th>
	</tr>
	<tr>
				<th style = "text:align:left"> First Character :  </th>
				<th> 
					<select name="FirstCharacter" id = "FirstCharacter" >
						<!-- <option value="-1" selected>Select</option> -->
						<option value="U" >UpperCase</option>
						<option value="L">LowerCase</option>
						<option value="S" >SpecialChar</option>
						<option value="N">Number</option>
					</select>
				</th>
	</tr>

	<tr>
				<th style = "text:align:left"> Last Character :  </th>
				<th> 
					<select name="LastCharacter" id ="LastCharacter">
						<!-- <option value="-1" selected>Select</option> -->
						<option value="U" >UpperCase</option>
						<option value="L">LowerCase</option>
						<option value="S" >SpecialChar</option>
						<option value="N">Number</option>
					</select>
				</th>
	</tr>

   <tr>
				<th style = "text:align:left"> Total Count :  </th>
				<th> 
					<input type="text" name="TotalCount" id = "TotalCount" value = "4" readonly></th>
  </tr>
  <tr>
	  <td colspan = 2 align="center">
	 	<input type="submit" name="submit" id="delete" value="SUBMIT" onclick="return checkPassword();" />
	 </td>
  </tr>
  </table>
</s:form>

</html>
