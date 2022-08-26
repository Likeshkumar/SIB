<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script>
	function validateRecords(){ 
		var inactiveinterval = document.getElementById("inactiveinterval");
		  var refreshtimeinterval = document.getElementById("refreshtimeinterval");
		var autoscrollreq = document.getElementById("autoscrollreq");
		var scrolltimeinterval = document.getElementById("scrolltimeinterval"); 
		
		 
		if( !autoscrollreqY.checked && !autoscrollreqN.checked){
			 errMessage(autoscrollreqN, "Auto Scroll Required ?? ") ; return false;
		}
		if( inactiveinterval ) { if( inactiveinterval.value == "" ) { errMessage(inactiveinterval, "Enter Inactive Interval ") ; return false;    } }
		if( refreshtimeinterval ) { if( refreshtimeinterval.value == "" ) { errMessage(refreshtimeinterval, "Enter Refresh Inverval") ; return false;    } }
		if( autoscrollreqY ) { if( autoscrollreqY.checked ) {
				if( scrolltimeinterval.value == "" ) { errMessage(scrolltimeinterval, "Enter scroll interval value") ; return false;    }
			}
		}
	 
		
		parent.showprocessing();
		 
	}
</script>

<style>
a {
	color: blue;
}
</style>
 

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="saveTerminalMonitoringConfigMerchMonitor.do" name="orderform"  method="get" onsubmit="return validateRecords()" autocomplete="off" namespace="/"> 
	<table border="0" cellpadding="0" cellspacing="0" width="60%" class="formtable"	align="center">
		<tr>
			 <td> Inactive Interval ( In Hours )  </td>
			 <td> : <s:textfield name="inavtiveinterval" id="inactiveinterval" value="%{monitorbean.nosingalinterval}" /> </td>
		</tr>
		
		<tr>
			 <td> Refresh Time Interval ( In Minutes )   </td>
			 <td> : <s:textfield name="refreshtimeinterval" id="refreshtimeinterval" value="%{monitorbean.refreshinterval}"/> </td>
		</tr>
		
		<tr>
			 <td> Auto Scroll Required  </td>
			 <td> : <s:radio name="autoscrollreq" id="autoscrollreq" list="#{'N':'No', 'Y':'Yes'}" value="%{monitorbean.scrollrequired}"   />  </td>
		</tr>
		
		<tr>
			 <td> Scroll Time Interval ( In Seconds )  </td>
			 <td> : <s:textfield name="scrolltimeinterval" id="scrolltimeinterval" value="%{monitorbean.scrollinterval}"/> </td>
		</tr>
		
	</table> 
	
	<table border="0" cellpadding="0" cellspacing="0" width="20%" class="submittable">
		<tr>
			<td><s:submit value="Submit" name="order" id="order" onclick="return validateConfiguration();"  /></td>
			<td><input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()" /></td>
		</tr>
	</table>

</s:form>

