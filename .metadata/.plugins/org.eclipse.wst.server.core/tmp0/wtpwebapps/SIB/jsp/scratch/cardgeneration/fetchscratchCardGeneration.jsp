<%@taglib uri="/struts-tags" prefix="s" %>
<table >
<s:iterator  value="scratchcardbeans.denomMaster">
			<tr>
				<td>Denom Value : </td>
				<td>
				<input type="text" id="scratch_denom_value" value="${DENOM_VALUE}" readonly /></td>
			</tr>
</s:iterator>			
<s:iterator  value="scratchcardbeans.scratchCard">
			<tr id = "scratchCardExpiryDate" >
					<td>Expire Date :
					</td>
					<td>
					<input type="text" name="todate" id="todate"  style="width:160px" value="${EXPIRY_DATE}">
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.addScratchCardForm.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
					</td>				
					</tr>
</s:iterator>			

</table>
