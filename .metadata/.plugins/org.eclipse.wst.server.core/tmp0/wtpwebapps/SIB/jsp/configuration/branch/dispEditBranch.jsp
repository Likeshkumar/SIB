<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript">

function checkform()
 {
	
	var valid=true;
	var c=confirm("Do You Want To Edit");
	if (c)
	{
		
		if(document.editBranchDetails.BRANCH_CODE.value=="")
		{
				alert("Branch Code Cannot Be Empty");
				valid=false;
					 	
		}
		
		
		else if(document.editBranchDetails.BRANCH_NAME.value=="")
		{
			alert("Branch Name Cannot Be Empty");
			return false;
		}
		else if(document.editBranchDetails.BR_ADDR1.value.length=="")
		{
			alert("'Branch Address 1' Cannot Be Empty");
			return false;
		}
		else if(document.editBranchDetails.BR_ADDR2.value.length=="")
		{
			alert("'Branch Address 2' Cannot Be Empty");
			return false;
		}
		else if(document.editBranchDetails.BR_ADDR3.value.length=="")
		{
			alert("'Branch Address 3' Cannot Be Empty");
			return false;
		}
		else if(document.editBranchDetails.BR_CITY.value.length=="")
		{
			alert("'Branch City' Cannot Be Empty");
			return false;
		}
		
		else if(document.editBranchDetails.BR_STATE.value.length=="")
		{
			alert("'Branch State' Cannot Be Empty");
			return false;
		}
		
		else if(document.editBranchDetails.BR_PHONE.value.length=="")
		{
			alert("'Branch Phone Number' Cannot Be Empty");
			return false;
		}

		
		return valid;
	}
	else
		return c;
	
 }	
 
	function confirm_check()
	 {
		 var r=confirm("Do You Want To Load Original Values");
		 return r;	 
		 
	 }
	 
 </script>


 	
<s:form name="editBranchDetails" autocomplete="off" action="saveEditbranchBranchAction.do"  method="post"> 

	<s:iterator var="branchdet" value="branch_detail">
								
				<table border="0" cellpadding="0" cellspacing="0" width="40%" >
						<tr align="right"><td ><font color="red">INSTITUTE ID</font></td><td><s:textfield name="INST_ID" id="INST_ID" value="%{INST_ID}" readonly="true"></s:textfield></td></tr>
				</table>
					
				  
				  <table border="0" cellpadding="0" cellspacing="0" width="80%" >
					
					<tr>
						<td>BRANCH CODE </td>
						<td><s:textfield name="BRANCH_CODE" id="BRANCH_CODE" value="%{BRANCH_CODE}" readonly="true" maxlength="4"></s:textfield></td>
						<td>BRANCH NAME</td>
						<td><s:textfield name="BRANCH_NAME" id="BRANCH_NAME" maxlength="20"></s:textfield></td>
					</tr>
					
					<tr>
						<td>BRANCH ADDRESS 1 <span style="color:red">*</span></td>
						<td><s:textfield name="BR_ADDR1" id="BR_ADDR1" maxlength="15"></s:textfield></td>
						<td>BRANCH ADDRESS 2</td>
						<td><s:textfield name="BR_ADDR2" id="BR_ADDR2" maxlength="15"></s:textfield></td>
					</tr>
					
					<tr>
						<td>BRANCH ADDRESS 3 </td>
						<td><s:textfield name="BR_ADDR3" id="BR_ADDR3" maxlength="15"></s:textfield></td>
						<td>BRANCH CITY</td>
						<td><s:textfield name="BR_CITY" id="BR_CITY" maxlength="15"></s:textfield></td>
					</tr>
					
					<tr>
						<td>BRANCH STATE </td>
						<td><s:textfield name="BR_STATE" id="BR_STATE" maxlength="15"></s:textfield></td>
						<td>BR PHONE </td>
						<td><s:textfield name="BR_PHONE" id="BR_PHONE" maxlength="12"></s:textfield></td>
					</tr>
					<tr><td><br></td></tr>
					</table>
					
					<div align="center">
					<table width="30%">
						<tr>
								<td ><s:submit value="Update" name="Update" onclick="return checkform();"/></td>
								<td><s:reset value="Reset" name="Reset" onclick="return confirm_check();"/></td>
								<td><s:reset value="Back" name="Reset" onclick="return goBack();"/></td>
						</tr>
					</table>
					</div>
		</s:iterator>
	
	

</s:form>
