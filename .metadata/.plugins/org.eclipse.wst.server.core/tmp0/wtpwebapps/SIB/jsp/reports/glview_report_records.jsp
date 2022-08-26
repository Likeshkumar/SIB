<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<sx:head></sx:head>

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script> 
 
</head>
<style>
table.formtable td{
	text-align:left;
}
</style>
<div align="center">
 
	<s:form action="glViewReportActionReportgenerationAction" autocomplete="off" name="reportsgen" namespace="/"   >
		<table border='1' cellpadding='0' cellspacing='0' width='80%' class="formtable" >
		<s:iterator value="glrecordlist">
		<tr>
			<th>Gl group code </th><td> :	${GROUP_CODE}	</td>
			<th>Gl group Name </th><td> :	${GROUP_NAME}	</td> 
		</tr>
			<s:iterator value="GLLIST">
			<tr>
				<td colspan="4">
					<table border='1' width="100%" cellspacing="0" cellpadding="0" style='text-align:left'>
					<tr>
						<td class='textcolor'>Gl  code </td><td> :	${GL_CODE}	</td>
						<td  class='textcolor'>Gl Name </td><td> :	${GL_NAME}	</td>
						<td  class='textcolor'>Gl Balance type </td><td> :	${GL_BAL_TYPEDESC}	</td> 
						<td class='textcolor'>Gl Assert/Liability </td><td> :	${GL_ALIEDESC}	</td>  
					</tr>
					<tr>
						<td class='textcolor'>Gl  Position </td><td> :	${GL_POSITIONDESC}	</td>
						<td class='textcolor'>Currency </td><td> :	${CUR_CODEDESC}	</td>
						<td class='textcolor'>Gl Statement Type </td><td> :	${GL_STATEMENT_TYPEDESC}	</td>  
					</tr>
					</table>
					<s:iterator value="SUBGLLIST">
						<table border='1' width="60%" cellspacing="0" cellpadding="0" class="formtable" >
						<tr>
							<td width="25%" class='textcolor' >Sub Gl  code </td><td width="25%">  :	${SCH_CODE}	</td>
							<td class='textcolor'>Sub Gl Name   </td><td>   :	${SCH_NAME}	</td> 
						</tr>
						</table>
					</s:iterator>
					
				</td>
				 
			</tr>
			<tr><th colspan="4"> &nbsp; </th></tr>
			</s:iterator>
	</s:iterator>
		 
		</table>
	</s:form>
</div>