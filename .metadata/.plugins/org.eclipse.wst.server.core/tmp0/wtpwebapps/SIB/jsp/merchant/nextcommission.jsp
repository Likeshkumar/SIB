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

function commissionrequired(vals)
{
	if(vals=="0")
	{
		document.getElementById("lowtxncnt").disabled=true;
		document.getElementById("highcnt").disabled=true;
		document.getElementById("lowtxncnt").value="";
		document.getElementById("highcnt").value="";
		document.getElementById("lowamt").disabled=false;
		document.getElementById("highamt").disabled=false;
		document.getElementById("lowamt").focus();

	}
	else
	{
		document.getElementById("lowamt").disabled=true;
		document.getElementById("highamt").disabled=true;
		document.getElementById("lowamt").value="";
		document.getElementById("highamt").value="";
		document.getElementById("lowtxncnt").disabled=false;
		document.getElementById("highcnt").disabled=false;
		document.getElementById("lowtxncnt").focus();
	}
}

	function validation()
	{
		var txn = document.getElementsByName("txn"); 
		var allcount = 0; 
		var childfeetype = document.getElementById("childfeetype"); 
		var subfeedecs = document.getElementById("subfeedecs"); 
		var fromdate = document.getElementById("fromdate"); 
		var todate = document.getElementById("todate"); 
		var lowamt = document.getElementById("lowamt"); 
		var highamt = document.getElementById("highamt"); 
		var lowtxncnt = document.getElementById("lowtxncnt"); 
		var highcnt = document.getElementById("highcnt"); 
		if(childfeetype.value == "-1")	{		errMessage(fromdate,"Select Child Commission Type");		return false;		}

		if(childfeetype.value=="S"){ if(subfeedecs.value==""){	errMessage(subfeedecs,"Enter Special Commission Name");		return false;	}
		
		if(fromdate.value=="") {	
			errMessage(fromdate,"Select From-Date");	return false;	}
			if(todate.value=="") {	errMessage(todate,"select To-Date");return false;}
		}

			if(lowamt.value=="")
			{
				if(lowamt.disabled == false)
				{
					errMessage(lowamt,"Enter Low Txn Amount");
					return false;
				} 				
			}
			if(highamt.value=="")
			{
				if(highamt.disabled == false)
				{
					errMessage(highamt,"Enter High Txn Amount");
					return false;
				}
			}
		 
  			if( lowtxncnt ){ 
				if(lowtxncnt.value=="")
				{
					if(lowtxncnt.disabled == false)
					{
						errMessage(lowtxncnt,"Enter Low Txn Count");
						return false;
					}
				}
			}
			if( highcnt ){
				if(highcnt.value=="")	{		if(highcnt.disabled == false)	{	errMessage(highcnt,"Enter High Txn Count");	 return false;	}	} 
			}
	 
			for(var i=0;i<txn.length;i++)
			{	
				allcount = allcount + 1;				 
				var txnid = document.getElementById("txn"+i).value;
				var txndesc = document.getElementById("txndesc"+i).value;
				//alert("Transaction id"+txndesc);			
					var disamt = "disamt"+txnid;
					////alert("pertxnamt====> "+pertxnamt);
					var disamt = document.getElementById(disamt);
					var dismode = "dismode"+txnid;
					////alert("pertxnamt====> "+pertxnamt);
					var dismode = document.getElementById(dismode);
					
					if (disamt.value == "" )
				    {	
				    	 ////alert ( "Please Enter Limit Amount" );
				    	 errMessage(disamt, "Please Enter "+txndesc+" Commission Amount");
				    	 return false; 
					}
					if (dismode.value == "-1" )
				    {	
				    	 ////alert ( "Please Enter Limit Amount" );
				    	 errMessage(dismode, "Please Select "+txndesc+" Commission Mode");
				    	 return false; 
					}			
					
			}
			if(txn.length == allcount)
			{
				////alert("All Cells Checked---> ");
				if ( confirm( 'Do you want to Submit' )){
					parent.showprocessing();
		    		return true;
				}else{
		    		return false;
				}
			}
			
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
	
	function getDelete(commissionid){
		var url = "deleteSubCommissionMerchantProcess.do?commissionid="+commissionid;
		AjaxReturnValue(url);
		var con = confirm("Do you want to delete?");
			if(con)
			{
				document.commissionprocess.action="addMerchCommissionMerchantProcess.do";
				document.getElementById("commissionprocess").submit();
				
			}
	}
	function addAdditionalCommission( commissionid ){
		var com_mastercode = document.getElementById("commastercode").value;
		var url = "addAdditionalCommissionMerchantProcess.do?mastercode="+com_mastercode+"&commissionid="+commissionid; 
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
<form action="savecommissionMerchantProcess.do"  name="commissionprocess" id="commissionprocess" method="post" autocomplete="off">
	<table border="0" cellpadding="0" cellspacing="6" width="80%" class="formtable">
		<tr>
			<td>Commission Name</td>
			<td  align="left" colspan="3" class="brown">
				<s:property value="feenamebean"/>
				<s:hidden name="commissionname" id="commissionname"  value="%{mprbean.feenamebean}"  />
				<input type="hidden" name="commastercode" id="commastercode" value="<%=request.getParameter("feecode")%>">
			</td>
		</tr>
		<tr>  
			<td> Child Commission Type </td>
				<td>
					:  
					<% if(feetype.equals("new")){%>
					<s:select id="childfeetype"  name="childfeetype" list="#{'D':'DEFAULT COMMISSION','S':'SPECIAL COMMISSION'}" headerKey="-1" headerValue="-SELECT-" onchange="showSplFee(this.value)"/>
					<% }else{ %>
					<s:select id="childfeetype"  name="childfeetype" list="#{'S':'SPECIAL COMMISSION'}" headerKey="-1" headerValue="-SELECT-" onchange="showSplFee(this.value)"/>
					<%} %>
				</td> 
			
			<td> Special Commission Name </td>
			<td>
				:
			 	<s:textfield name="subfeedecs" id="subfeedecs"/>
			</td> 
		</tr>
		
		<tr id="spldaterow" style="display:none">  
			<td>
	    		 From Date
	    	</td>
	    	<td> : 
	    			<input type="text" name="fromdate" id="fromdate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.commissionprocess.fromdate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
	        </td>
	        
	        <td> 
				To Date
			</td>
			<td> :  
					<input type="text" name="todate" id="todate"  readonly="readonly" onchange="return yearvalidation(this.id);" style="width:160px">
					<img src="style/images/cal.gif" id="image" onclick="displayCalendar(document.commissionprocess.todate,'dd-mm-yy',this);" title="Click Here to Pick up the date" border="0" width="15" height="17">
			</td>
		</tr>
	  
 		<tr>
			<td>Commission Mode</td>
			<td colspan="3"> <s:radio name="commission_mode" id="commission_mode" list="#{'AMT':'Amount'}" value="'AMT'" onclick="commissionrequired(this.value);"/></td>
																						<!-- ,'CNT':'Count' #commente by pritto...enable for future use -->
		</tr>
		 
</table>
 
		<table>
			<tr><td><s:submit value="Submit" id="formsub" name="formsub" onclick="return validation();"/>
			&nbsp;<input type="button" onclick="return confirmCancel()" class="cancelbtn" value="Cancel" id="cancel" name="cancel"></td></tr>
		</table>
 
<s:if test="%{mprbean.issbucomissionexist}">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" class="formtable">
					<tr> 
						<th align="center" > Commission Name</th>
						<th align="center">From Date </th>
						<th align="center">To Date </th>
						<th align="center">Commission type </th>
						<th align="center">Configured date</th>
						<th align="center">Configured by</th>
						<th align="center">Add / View </th>
						<th align="center">Delete</th>
					</tr>
				 
					<s:iterator value="mprbean.subfeelist">
						<tr>
							<td><a  href="javascript:void(0);" id="viewinfo" link="getCommissionviewDetailsMerchantProcess.do" findby="${COM_SUBCODE}">${SUB_COM_DESC}</a></td>
							<td>${FROMDATE}</td>
							<td>${TODATE}</td>
							<td> ${SUBCOM_TYPE}</td>
							<td>${ADDEDDATE}</td>
							<td>${USERNAME}</td>
							<td>
								<a onclick="return addAdditionalCommission(${COM_SUBCODE});"><img  src="images/addnew.png" alt="Add"/></a>
							</td>
							<td>
								<a onclick="return getDelete(${COM_SUBCODE});"><img  src="images/delete.png" alt="Delete"/></a>
							</td>
						</tr>
					</s:iterator> 
			</table>
</s:if>		

</form>