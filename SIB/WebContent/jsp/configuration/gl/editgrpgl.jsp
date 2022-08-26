<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 
<jsp:include page="/displayresult.jsp"></jsp:include>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/configuration/gl/glscript.js"></script> 
<s:form action="updateGlCodeGLConfigure.do" method="post" autocomplete="off">
	<s:iterator value="glcard">
		<s:set name="curncyglmaster">${CUR_CODE}</s:set>
		<s:set name="glgrpmaster">${GROUP_CODE}</s:set>
			<table style="border:1px solid #efefef;padding:0 auto" cellpadding="0" border="0" align="center" width="80%" cellspacing="0"  >
			
				<tr>
				 
					<s:if test="hodisable">
					<td> <input type='RADIO' name='glcomid'  id='ho'  value='HO' onclick='showAmount(this.checked, this.id)' /> HO </td>
					 <td> <div id='hoamountdiv' style='display:none'>  <input type='text' name='hoamount' value="Cash On Hand" id='hoamount' onfocus="hideuser(this.value)" onBlur="bluruser(this.value, this.id)" style='text-align:right' /> </div></td>
					</s:if>
					
					 
					<s:if test="feedisable">
					<td colspan="2"> <input type='RADIO' name='glcomid' id='fee' value='FEE' onclick='showAmount(this.checked, this.id)'  /> FEE</td>
					</s:if>
					   
				</tr>
				<tr > 
					 <td>GL Code</td>
					 <td> : <s:textfield name="glcode" id="glcode"  value="%{GL_CODE}"  readonly="true"/> 
					 		<s:hidden name="glcode" id="glcode"  value="%{GL_CODE}"/>
					 </td>
				 
					  <td>GL Desc</td>
					 <td> : <s:textfield name="gldesc" id="gldesc" value="%{GL_NAME}"/> </td>
				</tr>
				 
				 <tr>
					  <td>GL Short Name</td>
					 <td> : <s:textfield name="glshortname" id="glshortname" maxlength='10' value="%{GL_SHORT_NAME}"/> </td>
					 
					  <td>Balance Type</td>
					 <td> :
					 	<s:select id="baltype" name="baltype" list="#{'CR':'CREDIT','DR':'DEBIT','BT':'BOTH'}" value="%{GL_BAL_TYPE}" headerKey="-1" headerValue="-SELECT-"></s:select>
					</td> 
				</tr>
				
				 <tr>
					   
					 <td>ALIE</td>
					 <td> :  
							<s:select name="alie" id="alie" list="#{'ASRT':'ASSERT','LIAB':'LIABLITY','INC':'INCOME','EXP':'EXPENDITURE'}" value="%{GL_ALIE}" headerKey="-1" headerValue="-SELECT-"></s:select>
					</td> 
					
					 <td>Statement Type</td>
					 <td> :
							<s:select name="statement_type" id="statement_type" list="#{'DT':'DETAILS','BR':'BRIEF'}" value="%{GL_STATEMENT_TYPE}" headerKey="-1" headerValue="-SELECT-"></s:select> 
					</td> 
					
				</tr>
				
				<tr>
					
				 	  <td> Entry Allowed </td>
					 <td> : 
					 	<s:select name="entryallowed" id="entryallowed" list="#{'Y':'YES','N':'NO'}" value="%{GL_ENTRY_ALLOWED}" headerKey="-1" headerValue="-SELECT-"></s:select>
					 </td>
					  
					  
					   <td>Position</td>
					 <td> : 
							<s:select name="glposition" id="glposition" list="#{'BALSHEET':'BALANCE SHEET','TRIALBAL':'TRIAL BALANCE','PROFLOSS':'PROFILE LOSS STATEMENT'}" value="%{GL_POSITION}" headerKey="-1" headerValue="-SELECT-"></s:select>
					</td> 
					
					
					
				</tr>
			   
			 
			 	<tr>
					 <td>Currency</td>
					 <td> :
					 <select name="cur_code" id="cur_code">
		 				<option value="-1" > -SELECT- </option>
					 		 <s:iterator value="glbeans.instcurrencylist">
					 		 <s:set name="curncyinstitution">${CUR_CODE}</s:set>
					 		 <option value='${CUR_CODE}'
					 				<s:if test="%{#curncyglmaster==#curncyinstitution}">
					 				 selected="${CUR_CODE}"
					 				</s:if>
		 				>${CUR_DESC}</option>
		 				</s:iterator>
		 				
		 			</select>
					</td> 
					 
					 
					 <td>GL Group</td>
					 <td> : 
						  <select name="mastercode" id="mastercode" >
			 				<option value="-1" > -SELECT- </option>
				 			<s:iterator  value="glbeans.glgroup">
				 				<s:set name="grpname">${GROUP_CODE}</s:set>
			 					<option value="${GROUP_CODE}"		 				
					 				<s:if test="%{#glgrpmaster==#grpname}">
					 				 selected="${GROUP_CODE}"
					 				</s:if>
					 				>${GROUP_NAME}
					 			</option>
			 				</s:iterator>		 				
			 			</select>
					 </td>
				</tr>
			 	<tr>
			 		<td colspan="4">
			 				<table border="0" cellpadding="0" cellspacing="4" width="20%" >
								<tr>
								<td>
									<s:submit value="Update" name="order" id="order" onclick="return glvalidation()" /> 
								<td>
									<input type="button"  onclick="goBack()" name="cancel" id="cancel" value="Back"   class="cancelbtn"  />
									 
								</td>
								</tr>
							</table>
			 		</td>
			 	</tr>
			</table>
	</s:iterator>
</s:form>
