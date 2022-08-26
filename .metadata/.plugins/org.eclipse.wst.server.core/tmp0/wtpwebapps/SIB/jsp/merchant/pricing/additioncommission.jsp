<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/alpahnumeric.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/validationusermanagement.js"></script>
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/popup.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function showSplFee( feetype ){
	if( feetype == "D"){
		
		var def_fee = document.getElementById("commissionname").value;
		document.getElementById("subfeedecs").value = def_fee;
		document.getElementById("subfeedecs").readOnly = true;
		document.getElementById("spldaterow").style.display = 'none';
	}else{
		document.getElementById("subfeedecs").focus();
		document.getElementById("subfeedecs").readOnly = false;
		document.getElementById("subfeedecs").value = "";
		document.getElementById("spldaterow").style.display = 'table-row';
	}
}

 
	function validation()
	{
		var txncode = document.getElementById("txncode");  
		if(txncode.value == "-1")	{		errMessage(txncode,"Select Transaction type");		return false;		} 
			
	}
	
	function addCode(code){
	    var JS= document.createElement('script');
	    JS.text= code;
	    parent.document.getElementById("content").appendChild(JS);
	}
	// Wait until the DOM has loaded before querying the document
	$(document).ready(function(){

		$('a#viewinfo').click(function(e){
		//alert($("#comcode").val());
			modal.open({content: 
				$("#comcode").val()
			},$(this).attr("findby"),$(this).attr("link"));
			
			e.preventDefault();
			$f=$(this).parents("#popdivpos").attr("id");
			
			$(this).parents('#modal').toggle(false);		
			//$( "#modal",document).resizable();					
		});
	});
	
	function getDelete(recordid){
		var url = "deleteCommissionMerchantProcess.do?recordid="+recordid; 
		var con = confirm("Do you want to delete?");
			if(con)
			{
				AjaxReturnValue(url);  
				window.location = "addAdditionalCommissionMerchantProcess.do";
			}
	}
	function addAdditionalCommission( commissionid ){
		var url = "addAdditionalCommissionMerchantProcess.do?commissionid="+commissionid;
		 
		 
		var con = confirm("Do you want to Add?");
			if(con)
			{
				 
			 window.location = url;
			 parent.showprocessing();
				
			}
	}

</script></head>
<%String feetype = (String)session.getAttribute("feetype");%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="saveCommissionInfoMerchantProcess.do"  name="commissionprocess" id="commissionprocess" method="post" autocomplete="off">
	<table border="0" cellpadding="0" cellspacing="6" width="80%" class="formtable">
		<tr>
			<td>Commission Name</td>
			<td  align="left" colspan="3" class="brown">
				<s:property value="feenamebean"/>
				<s:hidden name="commissionname" id="commissionname"  value="%{mprbean.feenamebean}"  />
			</td>
		</tr> 

 		<tr>
			<td>Commission Mode</td>
			<td colspan="3"> <s:radio name="commission_mode" id="commission_mode" list="#{'AMT':'Amount'}" value="'AMT'" onclick="commissionrequired(this.value);"/></td>
																						<!-- ,'CNT':'Count' #commente by pritto...enable for future use -->
		</tr>
		
		<tr>
			<td>Transaction</td>
			<td colspan="3"> <s:select name="txncode" headerKey="-1" headerValue="-SELECT-" id="txncode" list="mprbean.translist" listKey="TXN_CODE" listValue="ACTION_DESC" onclick="commissionrequired(this.value);"/></td>
																						<!-- ,'CNT':'Count' #commente by pritto...enable for future use -->
		</tr>
		
		
		
		<tr>	
			<td>Low Txn Amount </td>						
			<td> 
				:<input type='text' id="lowamt"  style='width:100px;text-align:right' name='lowamt' maxlength="11"  />
			</td>

			<td>High Txn Amount </td>									
			<td> 
				:<input type='text' id="highamt" style='width:100px;text-align:right' name='highamt'  maxlength="11"  /> 
			</td>
		</tr>
		<tr>	
			<td>Commission Amount </td>						
			<td> 
				:<input type='text' id="commissionamount"  style='width:100px;text-align:right' name='commissionamount' maxlength="11"  />
			</td>

			<td>Mode </td>									
			<td> 
				: <s:select id="mode"  name="mode" list="#{'F':'Flat','P':'Percentage'}" headerKey="-1" headerValue="-SELECT-" onchange="showSplFee(this.value)"/> 
			</td>
		</tr>
		
						
		 
</table>
<br/>
 
		<table>
			<tr><td><s:submit value="Submit" id="formsub" name="formsub" onclick="return validation()"/>
			&nbsp;<input type="button" onclick="return confirmCancel()" class="cancelbtn" value="Cancel" id="cancel" name="cancel"></td></tr>
		</table>
 
<s:if test="%{mprbean.issbucomissionexist}">
				<table border="0" cellpadding="0" cellspacing="0" width="80%" class="formtable">
					<tr> 
						 
						 
						<th style='text-align:center'>Transaction type </th>
						<th style='text-align:right'>Minimum Amount </th>
						<th style='text-align:right'>Maximum Amount</th>
						<th style='text-align:right'>Amount</th>
						<th style='text-align:center'>Mode</th> 
						<th style='text-align:center'>Delete</th>
					</tr> 
					<s:iterator value="mprbean.subfeelist">
						<tr>
							<td><a  href="javascript:void(0);" id="viewinfo" link="getCommissionviewDetailsMerchantProcess.do" findby="${TXN_CODE}">${TXNDESC}</a></td>
							<td style='text-align:right'>${MIN_AMOUNT}</td>
							<td style='text-align:right'>${MAX_AMOUNT}</td>
							<td style='text-align:right'> ${COM_AMT}</td>
							<td>${COMMODE}</td> 
							 
							<td>
								<a onclick="return getDelete(${RECORD_ID});"><img  src="images/delete.png" alt="Delete"/></a>
							</td>
						</tr>
					</s:iterator> 
			</table>
</s:if>		

</form>