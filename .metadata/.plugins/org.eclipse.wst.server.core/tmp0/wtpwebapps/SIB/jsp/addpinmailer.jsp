<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="style/ifps.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript">

function enableparameters(chkid)
{
		////alert("PIN NUMBER");
		var chek1 = document.getElementById(chkid).checked;
		var fieldname = document.getElementById(chkid).value;
		////alert("fieldname--> " +fieldname);
		var field_len= "fieldlenth"+fieldname;
		var field_xpos= "xpos"+fieldname;
		var field_ypos= "ypos"+fieldname;

		////alert("field_len---> "+field_len+   "field_xpos-->"+field_xpos+   "field_ypos--->"+field_ypos   );
		if(chek1)
		{
			for(var i=1;i<=3;i++)
			{
				document.getElementById(field_len).disabled = false;
				document.getElementById(field_xpos).disabled = false;
				document.getElementById(field_ypos).disabled = false;
			}
		}
		else
		{
			for(var i=1;i<=3;i++)
			{
				document.getElementById(field_len).disabled = true;
				document.getElementById(field_len).value ="";
				document.getElementById(field_xpos).disabled = true;
				document.getElementById(field_xpos).value ="";
				document.getElementById(field_ypos).disabled = true;
				document.getElementById(field_ypos).value ="";
			}
		}
	}

function validatespinmailervalues()
{
		var flag = false;
		//alert("validatespinmailervalues");

		var mailname = document.getElementById("pinname").value;
		if(mailname == "")
		{
			errMessage(pinname,"Please Enter the Pin Mailer Name");
			return false;
		}
		var dctype = document.getElementById("doctype").value;
		if(dctype == "-1")
		{
			errMessage(doctype,"Please Select the Document Type");
			return false;
		}
		var pin_height = document.getElementById("pinheight").value;
		if(pin_height == "")
		{
			errMessage(pinheight,"Please Enter the Pinmailer Height ");
			return false;
		}
		var pin_len = document.getElementById("pinlen").value;
		if(pin_len == "")
		{
			errMessage(pinlen,"Please Enter the Pinmailer Length ");
			return false;
		}
	
		var chckbox_len = document.getElementsByName("p1").length;
		for(var i=0;i<chckbox_len;i++)
		{
		
			var fieldlen = "",fieldxpos = "",fieldypos = "";
			var chcked = document.getElementById("pinno"+i).checked;
			//alert("chcked--> "+chcked);
			if(chcked)
			{
				flag = true;
				var fieldname = document.getElementById("pinno"+i).value;
				//alert("fieldname--> "+fieldname);
				var field_len= "fieldlenth"+fieldname;
				var field_xpos= "xpos"+fieldname;
				var field_ypos= "ypos"+fieldname;

				fieldlen =document.getElementById(field_len); 
				fieldxpos = document.getElementById(field_xpos); 
				fieldypos = document.getElementById(field_ypos);
				

				if(fieldlen.value == "")
				{
					errMessage(fieldlen,"Please Enter the Field Length");
					return false;
				}
				if(( parseInt(fieldlen.value) ) > ( parseInt(pin_len) ))
				{
					errMessage(fieldlen,"Field Length should not be more than Pin Mailer Length");
					return false;
				}
				if(fieldxpos.value == "")
				{
					errMessage(fieldxpos,"Please Enter X Position");
					return false;
				}
				if(fieldypos.value == "")
				{
					errMessage(fieldypos,"Please Enter Y Position");
					return false;
				}
				if( ( parseInt(fieldxpos.value) ) > ( parseInt(pin_height) ) )
				{
					//alert("fieldxpos-->"+fieldxpos);
					errMessage(fieldxpos,"X Position should not exceed more than "+pin_height);
					return false;
				}
				if(parseInt(fieldypos.value)>parseInt(pin_len))
				{
					errMessage(fieldypos,"Y Position should not exceed more than "+pin_len);
					return false;
				}
				
			}
		}
		if(!flag)
		{
		//alert("Check Atleast one checkbox");
		errMessage(document.getElementById("pinname"),"Check Atleast one checkbox");
		return flag;
		}
}
function checkmax_Xvalue(ent_value,idvalue)
{
	//alert("ent_value--> "+ent_value+  "idvalue-->"+idvalue );
	var max_x = document.getElementById("pinheight").value;
	//alert("max_x--> "+max_x);
	if(max_x != "")
	{	
			var valof = document.getElementById(idvalue).value;
			//alert("valof-->"+valof);
			var val = parseInt(valof,10);
			//alert("val--> " +val);
			if(val > max_x || val == 0)
			{
				//alert("You Shold Enter X Position between 1 to"+max_x);
				errMessage(idvalue,"You Shold Enter X Position between 1 to "+max_x);
				document.getElementById(idvalue).value="";
				document.getElementById(idvalue).focus();
				return false;
			}
	}
	
	return true;
}
function checkmax_Yvalue(ent_value,idvalue)
{
	var max_y = document.getElementById("pinlen").value;
	if(max_y != "")
	{
			var valof = document.getElementById(idvalue).value;
			var val = parseInt(valof,10);
			if(val > max_y || val == 0)
			{
				errMessage(idvalue,"You Shold Enter Y Position between 1 to "+max_y);
				document.getElementById(idvalue).value="";
				document.getElementById(idvalue).focus();
				return false;
			}
	}
	else
	{
		errMessage(pinlen,"Enter the Y position");
		document.getElementById("pinlen").focus();
		return false;
	}
	return true;
}
function finalvalidation()
{
	//alert("Welcome");
	var checklen = document.getElementsByName("p1").length;	
	//alert("checkbox len==> "+checklen);
	
	return false;
	
}
</script>
</head>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
	<div align="center">
<s:form action="pinmailerdatasavepinMailerConfigurationAction"  name="pinmailerdataaddform" namespace="/" autocomplete="off">			
				<table border="0" cellpadding="0" cellspacing="4" width="50%">
	
						      <tr>
						    		<td>Pin Mailer Name</td>
									<td><input type="text" name="pinname" id="pinname" maxlength="15"></td>
						      </tr>
						      <tr>
						    		<td>Form Document Type</td>
									<td>
										<select name="doctype" id="doctype">
										  <option value="-1">--Select Document Type--</option>
										  <option value="A">Form-1</option>
										  <option value="B">Form-2</option>
										  <option value="C">Form-3</option>
										</select> 
									</td>
						      </tr>
							  <tr>
						    		<td>Pin Mailer Height [ Max-X ]</td>
									<td><input type="text" name="pinheight" id="pinheight" maxlength="3" style='width: 70px' onKeyPress="return numerals(event);"></td>
							  </tr>
							  <tr>
						    		<td>Pin Mailer Length [ Max-Y ]</td>
									<td><input type="text" name="pinlen" id="pinlen" maxlength="3" style='width: 70px' onKeyPress="return numerals(event);"></td>
						      </tr>
						  
			</table>
			      <br>
				 <table border='0' cellpadding='0' cellspacing='0' width='80%' class='txnlimitstyle'>
						<tr>
							<th>Select</th><th>Mailer Name</th><th>Field Length</th><th>X-Position</th><th>Y-Position</th>
						</tr>
							<%int i=0;%>
						<s:iterator value="pinmailerlist">
							<tr align="center"> 
									<td>
										<input type="checkbox" name="p1" id="pinno<%=i++%>"  value="${MAILER_ID}" onclick="enableparameters(this.id);"/>
									</td>
									<td>
										${MAILER_NAME}
									</td>								 
									<td> 
										<input type='text' id="fieldlenth${MAILER_ID}"  style='width:50px;text-align:right' name='fieldlenth${MAILER_ID}' disabled="disabled" maxlength="11" onKeyPress="return numerals(event);"/> 
									</td>								
									<td> 
										<input type='text' id="xpos${MAILER_ID}"  style='width:50px;text-align:right' name='xpos${MAILER_ID}' disabled="disabled" maxlength="2" onKeyPress="return numerals(event);" onchange="return checkmax_Xvalue(this.value,this.id);"/> 
									</td>															
									<td>
										 <input type='text' id="ypos${MAILER_ID}" style='width:50px;text-align:right' name='ypos${MAILER_ID}' disabled="disabled" maxlength="7" onKeyPress="return numerals(event);" onchange="return checkmax_Yvalue(this.value,this.id);"/> 
									</td>
							</tr>
						</s:iterator>					
						<tr>
	 		  				<td colspan='5' style='text-align:center;'><s:submit name="pinadd" value="     Add     "  onclick="return validatespinmailervalues();"/></td> 		  
	 		  			</tr>
					</table>
</s:form>
	</div>
</body>
</html>