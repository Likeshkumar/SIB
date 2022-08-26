<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Merchant Montitoring </title>
<script type="text/javascript" src="js/script.js"></script>
<script>
 
	function moniteringMerchant(){  
		 
		parent.showprocessing();
	}
</script>
</head>
<body onload="loadMonitor();" >
	
	<form action="moniteringMerchantMerchMonitor.do" autocomplete="off" id="monfomr" name="monitform" onsubmit="moniteringMerchant()" >
		 

		<table border="0" cellpadding="0" cellspacing="0" width="95%" id="tooltipcontainer"	style='border: 1px solid #efefef' align="center" class="filtertabnew">
		<tr>
			<td style="text-align:center">
				<input type="submit" name="view" id="name" value="Monitor"   />
			</td>
		</tr> 
		</table>
	</form>
</body>

</html>