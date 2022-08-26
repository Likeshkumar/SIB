<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"
	src="jsp/instant/script/ordervalidation.js"></script>
<script>
	

	function validateCard() {
		var cardno = document.getElementById("cardno");
		if (cardno) {
			if (cardno.value == "") {
				errMessage(cardno, "Card Number is empty...");
				return false;
			}
		}
		parent.showprocessing();
		return true;
	}
</script>
<div align="center">
	<jsp:include page="/displayresult.jsp"></jsp:include>
	<s:if test="hasActionErrors()">
		<div style="color: red">
			<s:actionerror />
		</div>
	</s:if>
	<form action="namePrintListCustomerNamePrintAction.do" method="post" autocomplete="off"
		onsubmit="return validateCard();">
		<input type="hidden" name="regtype" id="regtype" value='cardbase' />

		<table style="border: 1px solid #efefef" cellpadding="0"
			align="center" width="40%" cellspacing="0">

			<tr id="cardrow">
				<td align="center" colspan="2">&nbsp;
					<div style="width: 400px;" id='divcard'>
						Card Number &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : <input
							type='text' name='cardno' id='cardno' maxlength='19' />
					</div>

				</td>
			</tr>

		</table>

		<table border="0" cellpadding="0" cellspacing="0" width="15%">
			<tr>
				<td><s:submit value="Submit" name="order" id="order"
						onclick="return validateCard()" />
				<td><input type="button" name="cancel" id="cancel"
					value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />

				</td>
			</tr>
		</table>


	</form>

</div>