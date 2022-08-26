<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="cardlinklistCbsCardLinkAction"  name="perscardgen" autocomplete = "off" namespace="/">
<%String act = request.getParameter("act")==null?"":request.getParameter("act"); %>
<input type="hidden" value="<%=act%>" name = "act"  />
<table border="0" cellpadding="0" cellspacing="0" width="50%" class="filtertab">
	
    
	<tr>
	   <td align="left" class="fnt">Card No<font class="mand">*</font></td>
		<td> :
		<input type="text" name="cardno" id="cardno" maxlength="19" style="width:160px">
		</td>
	</tr>
	<!-- <tr>
	    <td align="left" class="fnt">To Date <font class="mand">*</font></td>
		<td> :
		<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
		<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.perscardgen.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
		</td>
	</tr> -->
	
</table>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="20%" >
	<tr>
		<td>
			<s:submit value="Next" name="Next" onclick="return viewfilterValidation();"/>
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>

</s:form>
