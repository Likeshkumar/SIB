<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />
<style>
table.pretty td{ 
	border-left:1px solid #0099ff;
	border-top:1px solid #0099ff;
}
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script>

  function enableField(selectedval)
{
	textcard = document.getElementById("textcard");
	
	if (selectedval == "cardno")
	{
	   // alert(selectedval);
		textcard.innerHTML=" Enetr Card Number";
		document.getElementById("cardno").value="";
		selectedtype.value="cardnum";
	}
	else if(selectedval =="orderrefno"){
		
		//alert(selectedval);
		textcard.innerHTML="Enter Order Reference   number";
		document.getElementById("orderrefno").value="";		
		selectedtype.value="orderrefno";
	}
	else if (selectedval == "phoneno"){
		//alert(selectedval);
		textcard.innerHTML=" Enter Phone Number";
		document.getElementById("phoneno").value="";								
		selectedtype.value="phoneno";
	}
	else {
		//alert(selectedval);
		textcard.innerHTML="Enter Customer ID Number";
		document.getElementById("custid").value="";			
		selectedtype.value="custidno";
	}
					
}
	  
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="editCustomerCustomerDetailsAction.do" name="orderform"  autocomplete="off" namespace="/">

	<table border="0" cellpadding="0" cellspacing="2" width="50%" align="center">
	
		<tr>
			<!-- <td>
				<input type="radio" name="checkedtype" id="cardnoopt" checked  onclick="enableField( this.value )" value="cardnoopt"/> Card Number 
			</td> -->
			<!-- <td>
				<input type="radio" name="checkedtype" id="custnoopt"  onclick="enableField( this.value )" value="custnoopt" /> Customer Id 
			</td> -->
		</tr>
	
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="80%"	align="center">
			<tr>
			<td style="border: 1px solid #0099FF">
				<table border="0" cellpadding="0" cellspacing="4" width="70%"
					align="center">
					
					
					<!-- <tr >
						<td>
						<input type="radio" name="checkedtype" id="cardno" checked  onclick="enableField( this.value )" value="cardnoopt"/> Card Number 
						Card no<input type='text' name='cardno' id='cardno' maxlength="16" onkeypress="return numerals(event);"/>
						<input type="hidden" id="selectedtype" name="selectedtype" value="cardnum"/> 
						</td>
						
						 <td>
						 <input type="radio" name="checkedtype" id="custid"  onclick="enableField( this.value )" value="custnoopt" /> Customer Id 
						 Customer Id no<input type='text' name='custid' id='custid' maxlength="16" onkeypress="return numerals(event);"/>
						<input type="hidden" id="selectedtype" name="selectedtype" value="custidno"/> 
						</td>
						 
						<td>
						<input type="radio" name="checkedtype" id="phoneno"  onclick="enableField( this.value )" value="phonenoopt" /> Phone No 
					 Phone No<input type='text' name='phoneno' id='phoneno' maxlength="12" onkeypress="return numerals(event);"/>
						<input type="hidden" id="selectedtype" name="selectedtype" value="phoneno"/> 
						</td>
						 
						<td>
						<input type="radio" name="checkedtype" id="orderrefno"  onclick="enableField( this.value )" value="ordernoopt"/> Order Reference Number 
					OrderRef No<input type='text' name='orderrefno' id='orderrefno' maxlength="12" onkeypress="return numerals(event);"/>
						<input type="hidden" id="selectedtype" name="selectedtype" value="orderrefno"/> 
						</td>
					</tr>  -->
					
				
				
				
				
				
				
				
				
				<tr >
						<td>
						Card no: <input type='text' name='cardno' id='cardno' maxlength="19" onclick="enableField( this.id )" />
						<input type="hidden" id="selectedtype" name="selectedtype" value="cardnum"/>  
						</td>
						
						 <td>
						 Customer Id no<input type='text' name='custid' id='custid' maxlength="6" onclick="enableField( this.id )" /> 
						<input type="hidden" id="selectedtype" name="selectedtype" value="custidno"/>  
						</td>
						 
						<td>
						 Phone No<input type='text' name='phoneno' id='phoneno'  maxlength="13" onclick="enableField( this.id )"/> 
				  		<input type="hidden" id="selectedtype" name="selectedtype" value="phoneno"/>   
						</td>
						 
						<td>
						OrderRef No<input type='text' name='orderrefno' id='orderrefno' maxlength="13" onclick="enableField( this.id )" /> 
						 <input type="hidden" id="selectedtype" name="selectedtype" value="orderrefno"/>  
						</td>
					</tr> 
				
				
					
					<tr>
						<table border="0" cellpadding="0" cellspacing="0" width="20%">
							<tr>
								<td><s:submit value="Submit" name="order" id="order" /></td>
								<td><input type="button" name="cancel" id="cancel" value="Cancel" class="cancelbtn" onclick="return confirmCancel()" /></td>
							</tr>
						</table>
					</tr>

				</table>
			</td>
		</tr>

	</table>

</s:form>

