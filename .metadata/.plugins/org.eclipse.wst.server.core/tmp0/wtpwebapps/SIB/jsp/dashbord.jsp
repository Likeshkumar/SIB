<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<link rel="stylesheet" type="text/css" href="style/TableCSSCode"/>
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
	
	<table border='0' cellpadding='10' cellspacing='20' width='80%' align="center" style="border-radius:9px;border: 7px solid #808080;">
		
	<tr>
	<s:iterator	value="dashbordreportlist">
	<td>
	  <div class="CSSTableGenerator">
			<table  border='0' cellpadding='0' cellspacing='0' width='100%' align="center">

				<h1 align="center">Personalized Card</h1><br>
				<tr>
				<th>Card Status</th>
				<th>Number of Cards</th>
				</tr>
				
				
				<tr>
				<td align="center">Waiting For Card Generation </td>
				<td>${PWCG}</td>
				</tr>
				
				<tr>
				<td align="center">Waiting For PIN Generation </td>
				<td>${PWPIN}</td>
				</tr>
				
				<tr>
					<td align="center">Waiting For PRE Generation </td>
					<td>${PWPRE}</td>
				</tr>
									
				<tr>
					<td align="center">Waiting For Card Receive </td>
					<td>${PWREC}</td>
				</tr>
				
				<tr>
					<td align="center">Waiting For Card Issue </td>
					<td>${PWISS}</td>
				</tr>
				
				<tr>
					<td align="center">Waiting For Card Activation </td>
					<td>${PCACT}</td>
				</tr>

			</table>
	  </div>
	</td>
	</s:iterator>
		
		
<s:iterator value="dashbordlistinstant">		
<td>
	  <div class="CSSTableGenerator">
			<table  border='0' cellpadding='0' cellspacing='0' width='100%' align="center">
				<h1 align="center">Instant Card</h1><br>
				<tr>
				<th>Card Status</th>
				<th>Number of Cards</th>
				</tr>
				
				
				<tr>
				<td align="center">Waiting For Card Generation </td>
				<td>${IWCG}</td>
				</tr>
				
				<tr>
				<td align="center">Waiting For PIN Generation </td>
				<td>${IWPIN}</td>
				</tr>
				
				<tr>
					<td align="center">Waiting For PRE Generation </td>
					<td>${IWPRE}</td>
				</tr>
									
				<tr>
					<td align="center">Waiting For Card Receive </td>
					<td>${IWREC}</td>
				</tr>
				
				<tr>
					<td align="center">Waiting For Card Issue </td>
					<td>${IWISS}</td>
				</tr>
				
				<tr>
					<td align="center">Waiting For Card Activation </td>
					<td>${ICACT}</td>
				</tr>
			</table>
	  </div>
</td>
	</s:iterator>
	</tr>
	</table>



</body>
</html>
