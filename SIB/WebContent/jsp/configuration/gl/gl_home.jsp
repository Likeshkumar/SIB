<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script> 

 <script>
 	function showAmount( chkval, fldid ){
 		
 		var hoamtfld = document.getElementById("hoamountdiv");
 		   
 		if( chkval ){
 			if( fldid== 'ho'){
 				hoamtfld.style.display = "block";
 			}else if ( fldid== 'fee'){
 				hoamtfld.style.display = "none";
 			}
 			//hoamtfld.focus();
 		}else{
 			if( fldid== 'ho'){
 				hoamtfld.style.display = "block";
 			}else if ( fldid== 'fee'){
 				hoamtfld.style.display = "none";
 			}
 		}
 				
 	}
 </script>

<jsp:include page="/displayresult.jsp"></jsp:include>
 
<form action="saveGlCodeGLConfigure.do" method="post" autocomplete="off">
	<input type="hidden" name="regtype" id="regtype" />
	<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="80%" cellspacing="0"  >
	
		<tr>
		 
			<s:if test="glbeans.hodisable">
			<td> <input type='RADIO' name='glcomid'  id='ho'  value='HO' onclick='showAmount(this.checked, this.id)' /> HO </td>
			 <td> <div id='hoamountdiv' style='display:none'>  : <input type='text' name='hoamount' value="Cash On Hand" id='hoamount' onfocus="hideuser(this.value)" onBlur="bluruser(this.value, this.id)" style='text-align:right' /> </div></td>
			</s:if>
			
			 
			<s:if test="glbeans.feedisable">
			<td colspan="2"> : <input type='RADIO' name='glcomid' id='fee' value='FEE' onclick='showAmount(this.checked, this.id)'  /> FEE</td>
			</s:if>
			   
		</tr>
		<tr > 
			 <td>GL Code</td>
			 <td> : <s:textfield name="glcode" id="glcode"  value="%{glbeans.glgrpcode}"  readonly="%{glbeans.flag}"/> </td>
		 
			  <td>GL Desc</td>
			 <td> : <s:textfield name="gldesc" id="gldesc" /> </td>
		</tr>
		 
		 <tr>
			  <td>GL Short Name</td>
			 <td> : <s:textfield name="glshortname" id="glshortname" maxlength='10' /> </td>
			 
			  <td>Balance Type</td>
			 <td> : <select id="baltype" name="baltype">
			 		<option value="-1" > -SELECT- </option>
			 		<option value="CR" > CREDIT </option>
			 		<option value="DR" > DEBIT </option>
			 		<option value="BT" > BOTH </option> 
					</select> 
			</td> 
		</tr>
		
		 <tr>
			   
			  <td>ALIE</td>
			 <td> : <select name="alie" id="alie">
			 		<option value="-1" > -SELECT- </option>
			 		<option value="ASRT" > ASSERT </option>
			 		<option value="LIAB" > LIABLITY </option>
			 		<option value="INC" > INCOME </option>
			 		<option value="EXP" > EXPENDITURE </option> 
					</select> 
			</td> 
			
			 <td>Statement Type</td>
			 <td> : <select name="statement_type" id="statement_type">
				 		<option value="-1" > -SELECT- </option>
				 		<option value="DT" > DETAILS </option>
				 		<option value="BR" > BRIEF </option> 
					</select> 
			</td> 
			
		</tr>
		
		<tr>
			
		 	  <td> Entry Allowed </td>
			 <td> : <select name="entryallowed" id="entryallowed">
			 	 <option value="-1" > -SELECT- </option>
			 	 <option value="Y" > YES </option>
			 	 <option value="N" > NO </option>
			 </select> </td>
			  
			  
			   <td>Position</td>
			 <td> : <select name="glposition" id="glposition">
				 		<option value="-1" > -SELECT- </option>
				 		<option value="BALSHEET" > BALANCE SHEET </option>
				 		<option value="TRIALBAL" > TRIAL BALANCE </option> 
				 		<option value="PROFLOSS" > PROFILE LOSS STATEMENT </option> 
				 		
					</select> 
			</td> 
			
			
			
		</tr>
	   
	 
	 	<tr>
			  <td>Currency</td>
			 <td> : <select name="cur_code" id="cur_code">
			         <option value="-1" > -SELECT- </option> 
			 		 <s:iterator value="glbeans.instcurrencylist">
			 		 <option value='${CUR_CODE}'>${CUR_DESC}</option>
			 		 </s:iterator>
					</select> 
			</td> 
			 
			 
			 <td>GL Group</td> 
			 <td> : <select name="mastercode" id="mastercode">
			 		<option value="-1" > -SELECT- </option>
			 		
			 		<s:iterator  value="glbeans.glgroup">
 				<option value="${GROUP_CODE}" >${GROUP_NAME}</option>
 				</s:iterator>
			 </select> </td>
			 
		</tr>
		
		
	 
	 	<tr>
	 		<td colspan="4">
	 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
						<tr>
						<td>
						<s:form action="editHsmAction" autocomplete="off">
							<s:submit value="Submit" name="order" id="order" onclick="return glvalidation()" />
						</s:form> 
						<td>
							<input type="button"  onclick="return confirmCancel()" name="cancel" id="cancel" value="Cancel"   class="cancelbtn"  />
							 
						</td>
						</tr>
					</table>
	 		</td>
	 	</tr>
	</table>


</form>
 
 