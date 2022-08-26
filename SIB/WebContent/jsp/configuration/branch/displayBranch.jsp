<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">



function checkform()
 {
	//alert("hai");
	var brcodelen=(document.getElementById('brcodlen').value);
	//alert ("brcoed: "+brcodelen);
	var BRANCH_CODE=document.addBranchDetails.BRANCH_CODE;
	var BRANCH_NAME=document.addBranchDetails.BRANCH_NAME;
	var BR_ADDR1=document.addBranchDetails.BR_ADDR1;
	var BR_ADDR2=document.addBranchDetails.BR_ADDR2;
	var BR_ADDR3=document.addBranchDetails.BR_ADDR3;
	var BR_CITY=document.addBranchDetails.BR_CITY;
	var BR_STATE=document.addBranchDetails.BR_STATE;
	var BR_PHONE=document.addBranchDetails.BR_PHONE;
	
	valid=true;
		
		if(BRANCH_CODE.value=="")
		{
			 errMessage(BRANCH_CODE,"Branch Code Cannot Be Empty");
			 return false;
					 	
		}
		if(BRANCH_CODE.value == "0000" || BRANCH_CODE.value == "000")
		{
			 errMessage(BRANCH_CODE,"Branch Code is Invalid");
			 return false;
		}
		if(BRANCH_CODE.value.length!=brcodelen)
			{
			errMessage(BRANCH_CODE,"Branch Code Should Be '"+brcodelen+"' Digit'");
			return false;
			}
		
		if(BRANCH_NAME.value=="")
		{
			errMessage(BRANCH_NAME,"Branch Name Cannot Be Empty");
			return false;
		}
		if( BR_ADDR1.value == "")
		{
			errMessage(BR_ADDR1,"Branch Address 1 Cannot Be Empty");
			return false;
		}
		/*
		if(BR_ADDR2.value=="")
		{
			errMessage(BR_ADDR2,"Branch Address 2 Cannot Be Empty");
			return false;
		}
		if(BR_ADDR3.value=="")
		{
			errMessage(BR_ADDR3,"Branch Address 3 Cannot Be Empty");
			return false;
		}
		if(BR_CITY.value=="")
		{
			errMessage(BR_CITY,"Branch City Cannot Be Empty");
			return false;
		}
		
		if(BR_STATE.value=="")
		{
			errMessage(BR_STATE,"Branch State Cannot Be Empty");
			return false;
		}
		
		if(BR_PHONE.value=="")
		{
			errMessage(BR_PHONE,"Branch Phone Number Cannot Be Empty");
			return false;
		}
		*/
	
		var x = confirm( "Do you want to Submit ");
		if ( x ) {
			parent.showprocessing();
			return valid;
		}else{
			return false;
		}
}
function confirmCancel()
{
	if ( confirm("Do You Want To Cancel")  ) 
	{
		return true;
	}
	else
	{
		return false;
	}
}

function chkChars(field,id,enteredchar)
{
	//alert('1:'+field+'2:'+id+'3:'+enteredchar);
	charvalue= "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
    	//alert(document.getElementById(id).value.charAt(i));   
    	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

function validateNumber (field,id,enteredchar)
{
	charvalue= "0123456789";
	//str = document.getElementById(id).value;
    for (var i = 0; i < document.getElementById(id).value.length; i++) {
    	
       if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
       	//alert(document.getElementById(id).value.charAt(i));
       	//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
       	errMessage(document.getElementById(id),"["+document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
    	document.getElementById(id).value = '';
    	return false;
    	}
    }
}

</script>
<%String act = (String)session.getAttribute("act"); %>
 <jsp:include page="/displayresult.jsp"/>
 
 <s:form name="addBranchDetails" autocomplete="off" action="%{branchbean.formaction}"  method="post">
  	<s:hidden name="brcodlen"  id="brcodlen" value="%{brcodlen}"></s:hidden>
 	<input type="hidden" name="act" id="act" value="<%=act%>"> 
	<%-- <div align="center">
		<table border="0" cellpadding="0" cellspacing="0" width="60%" >
		<% 
		String usertype=(String)session.getAttribute("USERTYPE");
		%>
			<tr><td width="30%"><font color="red"><b>INSTITUTION ID</b></font></td><td><% out.println(session.getAttribute("Instname")); %></td></tr>
		</table>
	</div> --%>
	<s:hidden value="%{brcodlen}" id="brcodlen"/>
	<table border="0" cellpadding="0" cellspacing="0" width="80%" >
			<tr>
				<td>BRANCH CODE <span style="color:red">*</span></td>
				<s:if test="branchbean.readonly">
					<td><s:textfield name="BRANCH_CODE" id="BRANCH_CODE" maxlength="%{brcodlen}" value="%{branchbean.branchcode}" readonly="true"/>
					<s:fielderror fieldName="BRANCH_CODE" cssClass="errmsg" />
					</td>
				</s:if>
				<s:else>
<%-- 					<td><s:textfield name="BRANCH_CODE" id="BRANCH_CODE" maxlength="%{brcodlen}" onkeyup="validateNumber('BRANCH CODE',this.id,this.value)" value="%{branchbean.branchcode}"/></td> --%>
					<td><s:textfield name="BRANCH_CODE" id="BRANCH_CODE" maxlength="%{brcodlen}" value="%{branchbean.branchcode}"/>
					<s:fielderror fieldName="BRANCH_CODE" cssClass="errmsg" />
					</td>
				</s:else>
				<td>BRANCH NAME<span style="color:red">*</span></td>
				<td><s:textfield name="BRANCH_NAME" id="BRANCH_NAME" maxlength="20"  value="%{branchbean.branchname}" /><s:fielderror fieldName="BRANCH_NAME" cssClass="errmsg" /></td>
			</tr>
			
			<tr>
				<td>BRANCH ADDRESS 1 <span style="color:red">*</span></td>
				<%-- <td><s:textfield name="BR_ADDR1" id="BR_ADDR1" value="%{branchbean.br_addr1}" onkeyup="chkChars('BRANCH ADDRESS 1',this.id,this.value)" /></td> --%>
				<td><s:textfield name="BR_ADDR1" id="BR_ADDR1" value="%{branchbean.br_addr1}" /><s:fielderror fieldName="BR_ADDR1" cssClass="errmsg" /></td>
				<td>BRANCH ADDRESS 2</td>
				<td><s:textfield name="BR_ADDR2" id="BR_ADDR2" value="%{branchbean.br_addr2}" /></td>
			</tr>
			
			<tr>
				<td>BRANCH ADDRESS 3 </td>
				<td><s:textfield name="BR_ADDR3" id="BR_ADDR3" value="%{branchbean.br_addr3}"  /><s:fielderror fieldName="BR_ADDR3" cssClass="errmsg" /></td>
				<td>BRANCH CITY</td>
				<td><s:textfield name="BR_CITY" id="BR_CITY" value="%{branchbean.br_city}"  /></td>
			</tr>
			
			<tr>
				<td>BRANCH STATE </td>
				<td><s:textfield name="BR_STATE" id="BR_STATE"  value="%{branchbean.br_state}"/></td>
				<td>BR PHONE </td>
				<td><s:textfield name="BR_PHONE" id="BR_PHONE"  maxlength="20" value="%{branchbean.br_phone}" />
				<s:fielderror fieldName="BR_PHONE" cssClass="errmsg" />
				</td>
			</tr>
			<tr>
			
			</tr>
	</table>
	
<table border="0" cellpadding="0" cellspacing="0" width="50%" >
	<tr><td></td></tr>
	<tr align="center" >
		<%-- <td> <s:submit value="Submit" name="submit" onclick="return checkform()"/> </td> --%>
		 <td> <s:submit value="Submit" name="submit" /> </td> 
		<td><input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn" onclick="return confirmCancel()"/></td>
</table>


</s:form>
