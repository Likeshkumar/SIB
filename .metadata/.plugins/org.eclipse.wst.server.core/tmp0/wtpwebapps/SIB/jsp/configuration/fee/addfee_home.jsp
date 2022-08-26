<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/script.js " ></script>

	<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
<script>
		
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

	
	
	function validation()
	{
		var feename = document.getElementById("feename");
		//alert(commtype);
			/* if(feename.value=="")
			{
				errMessage(feename,"Enter fee Name");
				return false;
			} */
			
			var url = "checkFeenameExistFeeConfig.do?feename="+feename.value;
			var result = AjaxReturnValue(url);
			//alert(result);
			if(result!='NEW')
				{
				errMessage(feename, "Fee Name Alredy Exist Try Different !");
				return false; 
				
				}
			
		
		
	}
</script>
<%String act = (String)session.getAttribute("act"); %>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<form action="addFeeViewFeeConfig.do" autocomplete="off">
	<input type="hidden" name="act" id="act" value="<%=act%>">
	<input type='hidden' name="currencyco" id="currencyco" value="CURRENCY_CODE" /> 
		<table border="0" cellpadding="0" cellspacing="0" width="50%" class="formtable">
			
			<tr id="FeeNameDiv">
					<td align="right">Enter FEE Name</td>
				    <td align="center">:</td>
					<td>
						<s:textfield name="feename" id="feename"  value="%{feebean.feename}" ></s:textfield>
						<!-- <input type="text" name="feename" id="feename"  onkeyup="chkChars('Fee Name',this.id,this.value)" value="%{feebean.feename}" /> -->
						<s:fielderror fieldName="feename" cssClass="errmsg" />						
					</td>
			<tr>		
			<tr>		
					 <td align="center" colspan="0">Limit Currency   		
					 <td align="center">:</td>
					 <td>				
						<select name="limitcurrncy" id="limitcurrncy">   
							<option value="-1">-SELECT LIMIT CURRENCY-</option>    
								<s:iterator  value="globcurrcy" >
									<option value="${NUMERIC_CODE}">${CURRENCY_DESC}</option>
								</s:iterator>
						</select>   
						<s:fielderror fieldName="limitcurrncy" cssClass="errmsg" />      
					</td>
			</tr>
			
		</table>
		<table>
				<tr>
					<td>
					<%-- <s:submit value="Next" onclick="return validation()"/> --%>
					<s:submit value="Next" />  
		
						<input type="reset" name="Cancel" id="Cancel" value="Cancel">
					</td>
				</tr>
		</table>
	</form>