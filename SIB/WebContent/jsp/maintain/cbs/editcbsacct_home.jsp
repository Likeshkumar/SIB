<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
 

<jsp:include page="/displayresult.jsp"></jsp:include>


<s:form action="editAcctNumberCbsAccount.do" name="orderform"
	onsubmit="return validateRecords()" autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="0" width="90%"
		align="center">
		<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="30%"
					align="center">
					<tr>
						<td>Card No</td>
						<td> : <input type='text' name='cardno' id='cardno'   maxlength=19/>
						 
					</tr>
					 
					<tr>
						<table border="0" cellpadding="0" cellspacing="0" width="20%">
							<tr>
								<td><s:submit value="Submit" name="order" id="order" /></td>
								<td><input type="button" name="cancel" id="cancel"
									value="Cancel" class="cancelbtn"
									onclick="return confirmCancel()" /></td>
							</tr>
						</table>
					</tr>


				</table>
			</td>
		</tr>


	</table>

</s:form>

