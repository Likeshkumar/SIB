<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>

<link rel="stylesheet" type="text/css" href="style/TableCSSCode(1).css"/>
<link rel="stylesheet" type="text/css" href="style/jquery_css.css"/>
		<style>
			* {
				margin:0; 
				padding:0;
			}
			table td
			{
		     	padding-bottom: 0px !important;			
			}
			
	
		</style>
</head>
<body>
<jsp:include page="/displayresult.jsp"></jsp:include>
<%
	String instid = (String)session.getAttribute("Instname");
	System.out.println("instid==> "+instid);
	String branch = (String)session.getAttribute("BRANCHCODE");
	System.out.println("branch==> "+branch);
	String usertype = (String)session.getAttribute("USERTYPE");
	System.out.println("usertype==> "+usertype);
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	System.out.println("branchname==> "+branchname);
	String acttype = (String)session.getAttribute("act_type");
	System.out.println("acttype==> "+acttype);
	
%>
	
	<b></b>
	<b></b>
	<table border='0' cellpadding='10' cellspacing='20' width='40%' align="center" style="border-radius:9px;border: 7px solid #808080;">
	<tr><td>
		<div class="CSSTableGenerator">
			<table  border='0' cellpadding='0' cellspacing='0' width='100%' align="center">
				<tr>
					<th align="center"><b>Reason</b></th>
					<th align="center"><b>Count</b></th>
				</tr>
			
			   <%--  <s:iterator var="studentEntry" status="stat" value="tablenamelist.entrySet()">  
    		    	Student Key: <s:property value="%{#studentEntry.getKey()}"/> - Student Value: <s:property value="%{#studentEntry.getValue()}"/><br>  
        			Address Value: <s:property value="address['S' + #studentEntry.getKey()]"/><br>  
    			</s:iterator>   --%>
    
    
			 
				<s:iterator value="tablenamelist">
					<tr>
						<td style="font-size:12px"> ${TNAME}</td>
					    <td style="font-size:12px"> ${TABTYPE}</td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</td></tr>
	</table>

</body>
</html>
