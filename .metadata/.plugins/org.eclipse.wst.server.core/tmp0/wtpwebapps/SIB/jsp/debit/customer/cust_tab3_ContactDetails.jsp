<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-dojo-tags" prefix="sx" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

 	<s:form action="customerDetailsAddCreditCustRegisteration.do"  id="regform" onsubmit="return valiteContactDetails()" name="regform" autocomplete = "off" namespace="/"> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="viewtable formtable" style="border:none;padding-top:10px"  >	
	
		<tr>
			 <td class="txt"> Mobile  <span class="mand">*</span>   </td> <td> <s:textfield name="comobileno" id="comobileno" maxlength="16"   value="%{dbtcustregbean.comobileno}"  /> </td>
			 <td class="txt"> E-Mail      </td> <td> <s:textfield name="secemail" id="secemail" maxlength="32"   value="%{dbtcustregbean.secemail}"   /> </td>
		 
		</tr> 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> Permenent Address
			<input type="hidden" id="reqfrom" name="reqfrom" value="2" /> 
			<input type="hidden" id="gotoval" name="gotoval" value="3" />
			<s:hidden name="applicationid" id="applicationid" value="%{dbtcustregbean.applicationid}"/>
			<s:hidden name="customerid" id="customerid" value="%{dbtcustregbean.customerid}"/>
		</th><tr> 
		<tr>
			 <td class="txt"> Po.Box <s:property value="dbtcustregbean.prphone1"/> <span class="mand">*</span> </td> <td> <s:textfield name="prpobox" id="prpobox" maxlength="32" value="%{dbtcustregbean.prpobox}" /> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> <td> <s:textfield name="prhouseno" id="prhouseno" maxlength="32" value="%{dbtcustregbean.prhouseno}"  /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td> <s:textfield name="prstreetname" id="prstreetname" maxlength="32" value="%{dbtcustregbean.prstreetname}" /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:textfield name="prwardname" id="prwardname" maxlength="32" value="%{dbtcustregbean.prwardname}" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td> <s:textfield name="prcity" id="prcity" maxlength="32" value="%{dbtcustregbean.prcity}"/> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td> <s:textfield name="prdistrict" id="prdistrict" maxlength="32" value="%{dbtcustregbean.prdistrict}"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> <td> <s:textfield name="prphone1" id="prphone1" maxlength="16"  value="%{dbtcustregbean.prphone1}" /> </td>
			 <td class="txt"> Phone2  </td> <td> <s:textfield name="prphone2" id="prphone2" maxlength="16"  value="%{dbtcustregbean.prphone2}" /> </td>
			   
		</tr> 
		
		 
		
		<tr><th colspan="6" style="text-align:left" class="tbheader"> <s:checkbox name="sameasperm" id="sameasperm" onchange="setContactAddress( this.checked )"/> Contact Address </th>
		<tr>
			 <td class="txt"> Po.Box <span class="mand">*</span> </td> <td> <s:textfield name="copobox" id="copobox" maxlength="32" value="%{dbtcustregbean.copobox}"   /> </td>
			  <td class="txt"> House No.  <span class="mand">*</span>   </td> <td> <s:textfield name="cohouseno" id="cohouseno" maxlength="32" value="%{dbtcustregbean.cohouseno}"  /> </td>
			   <td class="txt"> Street Name  <span class="mand">*</span>  </td> <td> <s:textfield name="costreetname" id="costreetname" maxlength="32" value="%{dbtcustregbean.costreetname}"  /> </td>
		</tr> 
		<tr>
			 <td class="txt"> Ward Name   </td> <td> <s:textfield name="cowardname" id="cowardname" maxlength="32" value="%{dbtcustregbean.cowardname}" /> </td>
			  <td class="txt"> City  <span class="mand">*</span>  </td> <td> <s:textfield name="cocity" id="cocity" maxlength="32" value="%{dbtcustregbean.cocity}"/> </td>
			   <td class="txt"> District <span class="mand">*</span>  </td> <td> <s:textfield name="codistrict" id="codistrict" maxlength="32" value="%{dbtcustregbean.codistrict}"/> </td>
		</tr> 
		
		<tr>
			 <td class="txt"> Phone1  </td> <td> <s:textfield name="cophone1" id="cophone1" maxlength="16"  value="%{dbtcustregbean.cophone1}"  /> </td>
			 <td class="txt"> Phone2  </td> <td> <s:textfield name="cophone2" id="cophone2" maxlength="16" value="%{dbtcustregbean.cophone2}"  /> </td>
			   
		</tr> 
		
		 
		
		 
		
	</table>	 
	
<table border="0" cellpadding="0" cellspacing="4" width="20%" >
		<tr>
		<td>
			<s:submit value="Continue"   name="secondtab" id="secondtab" />
		</td>
		<td>
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
			 
		</td>
		</tr>
	</table> 
</s:form>
</body>
</html>