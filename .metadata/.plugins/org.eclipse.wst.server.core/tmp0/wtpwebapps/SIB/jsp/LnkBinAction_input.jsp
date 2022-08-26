<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>

<sx:head />
<script type="text/javascript"  src="js/alpahnumeric.js" ></script>
<script>


function validate_linkbin_input_form()
{		
	var bin_value=document.linkbin_input_form.bin.value;
	
	if(bin_value=="")
		{
			alert("Bin Cannot Be Empty " );
			document.getElementById('bin').focus();
			return false;
		}
	if(!(bin_value.length ==6))
		{
			alert(" Bin Length Should Be 6 Digit " );
			document.getElementById('bin').focus();
			return false;
		}
}
</script>
<div align="center">
<s:form name="linkbin_input_form" action="saveLnkBinAction" autocomplete="off">
<table border='0' width='40%' >
		<tr align="center">
			<td colspan="2">
				<%
					String link_bin_message = null;
					String link_bin_error = null;
					link_bin_message = (String) session.getAttribute("link_bin_message");
					link_bin_error=(String) session.getAttribute("link_bin_error");
					session.removeAttribute("link_bin_message");
					session.removeAttribute("link_bin_error");
				%>
				<%
					if (link_bin_message != null) 
					{
				%>
					<font color="Red"><b><%=link_bin_message%></b></font>
				<%
					} 
				%>	
			</td>
		</tr>
		<%
			if(!(link_bin_error =="error"))
			{
		%>
		<!-- if no error your code should come here  -->
		
		
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td> Select Product
			</td>
			<td><select name="parent_bin" >
				<s:iterator value="binList" >
				<option value="${BIN}" > ${PRODUCT_NAME} - ${BIN} </option>
				</s:iterator>
			</select>
			</td>
		</tr>
		<tr>
			<td>
				 Enter the BIN
			</td>
			<td>
				<s:textfield id="child_bin" name="child_bin"  maxlength="6" onkeypress=" return numerals(event); "></s:textfield>
			</td>
		</tr>
		<tr >
			<td colspan='2' align="center">
				<s:submit name="Submit" value="Submit" onclick=" return validate_linkbin_input_form(); " ></s:submit>
			</td>
		</tr>
		
		<!-- code ends here -->		
		<%
			}
		
		%>
					
</table>
</s:form>
</div>
