<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js" ></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/popup.js"></script>
<script>
function editSubfee( subfeeid,masterfeecode ){	 
	var masterfeecode = document.getElementById("masterfeecode"); 
	alert(" masterfeecode "+masterfeecode);
	var url = "editSubFeeHomeFeeConfig.do?masterfeecode="+masterfeecode+"&subfeecode="+subfeeid+"&flag="+"Auth";
	if( confirm( "Do you want to continue...") ){
		window.location=url;
		parent.showprocessing();
	}else{
		return false;
	}
}
</script>
<style type="text/css">
table{
	margin:0 auto;
}

table.subaction td{
	padding-top:10px;
}
.textcolor
{
color: maroon;
font-size: small;
}
.textcolor1
{
color: black;
font-size: small;
}
.bordercl td,th
{
  border:1px solid #CCCCCC;
}
</style>
</head>
<script>
function validation()
{
	var feename = document.getElementById("feecode");
	//alert(commtype);
		if(feename.value=="")
		{
			errMessage(feename,"Select fee Name");
			return false;
		}
}
</script>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="EditViewFeeFeeConfig.do" name="schemeprocess" id="schemeprocess" autocomplete = "off"  namespace="/">
		<table border='0' cellpadding='0' cellspacing='0' width='30%'> 
			<tr>
				<td colspan='2' style='padding-top:30px'>
					<table border='0' cellpadding='0' cellspacing='0' width='100%' class="formtable">
						<tr> 
							<th>Fee Name</th>
								<td><select id="feecode" name="feecode" >
					<s:if test ="%{feebean.feeLisConfig.isEmpty()}">
					<option value="">No Fee Waiting</option>
					</s:if>
					<s:else>
								<option value="" >Select Fee</option>   
								<s:iterator value="feebean.feeLisConfig">
								<option value="<s:property value="FEE_CODE"/>"><s:property value="FEE_DESC"/></option>
								</s:iterator>
					</s:else>
					</select>
					
								</td>
							</tr>
					</table>
				</td>
			</tr>
		</table>
		
		<table>
				<tr>
					<td>
						<s:submit value="Next" onclick="return validation()"/>
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>
</s:form>
</body>
</html>