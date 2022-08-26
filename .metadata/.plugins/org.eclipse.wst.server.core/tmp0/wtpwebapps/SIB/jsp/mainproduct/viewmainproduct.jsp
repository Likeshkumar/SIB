<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="js/script.js"></script>
<script>
function makeConfirm( ) {
	
	//var message = "Do you want to "+ msg ;
	//var x = confirm ( 'Do You Wanna Proceed...' );
	if ( confirm ( 'Do You Wanna Proceed...' ) ){
		return true;
	}else {
		return false;
	}
}

function prodscript(product_code)
{
	//alert("product_code"+product_code);
	window.open('proddetailsAddMainProductAction.do?product_code='+product_code,'','left=350,top=150,width=600,height=400,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
}

function selectall()
{
	valid=true;
	var selectbox = document.getElementById("cardtypeid");
	if(selectbox.value=="-1")
	{
		errMessage(cardtypeid,"SELECT PRODUCT");
		return false;
	}
	return valid;
}
</script>
<%String act = (String)session.getAttribute("act");%>
<jsp:include page="/displayresult.jsp"></jsp:include> 
	<s:form action="%{productbean.formaction}" autocomplete="off">
		<s:hidden name="doact" id="doact" value="%{productbean.doact}" /> 
		<div align="center">
			<table align="center" border="0"  cellspacing="0" cellpadding="0" width="50%" class="formtable">
				  	<tr>
				  		<td  align="right">Select Product</td>
					  		<td width="50%" align="left">
								<select id="cardtypeid" name="cardtypeid">
									<option value="-1">-Select_Product-</option>
									<s:iterator  value="card_type_list">
										<option value="${PRODUCT_CODE}">${CARD_TYPE_NAME}</option>
									</s:iterator>
								</select>
							</td>
					</tr>
				</table>
			
			<table>
				<tr><td><s:submit value="View" name="submit" id="submit" onclick="return selectall();"/></td></tr>
			</table>
		</div>
	</s:form>