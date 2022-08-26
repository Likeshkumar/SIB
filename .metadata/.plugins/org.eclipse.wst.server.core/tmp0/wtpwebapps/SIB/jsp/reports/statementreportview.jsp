<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<style type="text/css">
table.formtable td{
	border:1px solid gray;
}
</style>
<body>
<table align="center" border="0"  cellspacing="1" cellpadding="0" width="90%" class="formtable">
	<tr style="color: maroon;">
		<th>CARD NO.</th>
		<th>TXNDESC</th>
	    <th>TRANDATE</th>
	    <th>REFNUM</th>	    
	    <th>ACCEPTORNAME</th>
	    <th>TERMLOC</th>
	    <th>TXNAMOUNT</th>
	    <th>DEBIT</th>
	    <th>CREDIT</th>
	    <th>REMAINIING BALANCE</th>
	    
	</tr>
	<% int rowcnt = 0; Boolean alt=true; %> 
	<s:iterator  value="stment_report_list">
		<tr <% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>>
				<td> ${CHN} </td>
				<td> ${TXNDESC} </td> 
				<td align=center> ${TRANDATE} </td> 
			    <td align=center> ${REFNUM} </td>  
			    <td>${ACCEPTORNAME}</td>
			    <td>${TERMLOC}</td>
			    <td>${TXNAMOUNT}</td>
			    <td>${OPTYPEDR}</td>
			    <td>${OPTYPECR}</td>
			    <td>${REMAINIINGBALANCE}</td> 
		</tr>
	</s:iterator>
</table>

</body>
</html>
