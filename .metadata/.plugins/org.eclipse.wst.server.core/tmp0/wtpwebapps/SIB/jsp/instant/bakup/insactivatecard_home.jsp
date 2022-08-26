<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s" %> 


<link rel="stylesheet" type="text/css" href="style/calendar.css"/>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>

<script>
	function dontAllowEnter(event){
		if( event.keyCode == 13 ){
			errMessage(document.getElementById("cardno"), "Enter key not allowed. Click Retrieve button");
			return false;
		}else{
			errMessage(document.getElementById("cardno"), " ");
		}
		
	}
	function makeSummery() {

		
		
		//alert("Calling Make Summar");
		var cardno = document.getElementById("cardno");
		//alert("Calling Make Summar 2");
		var defloadamt = document.getElementById("defloadamt");
		//alert("Calling Make Summar 3");
		var min_amount = document.getElementById("minamount");
		//alert("Calling Make Summar 4");
		var max_amount = document.getElementById("maxamount");
		//alert("Calling Make Summar 5");
		//alert("defloadamt===> "+defloadamt.value);
		//alert("min_amount===> "+min_amount.value);
		//alert("max_amount===> "+max_amount.value);
		 
		
		var sec_curreq = document.getElementById("sec_curreq");
		var summery = document.getElementById("summeryblock");
		var sec_succ = false;
		if( defloadamt.value == "" ){
			errMessage("defloadamt", "Enter the Loading amount");
			defloadamt.focus();
			summery.innerHTML = "";
		}

		if(parseInt(defloadamt.value)<parseInt(min_amount.value))
		{
			alert("Load Amount Should Not Be Less The Minimum Amount "+min_amount.value);
			errMessage("defloadamt","Load Amount Should Not Be Less The Minimum Amount "+min_amount.value);
			defloadamt.focus();
			summery.innerHTML = "";
		}
		if(parseInt(defloadamt.value)>parseInt(max_amount.value))
		{
			alert("Load Amount Should Not Exceed Maxmimum Amount "+max_amount.value);
			errMessage("defloadamt","Load Amount Should Not Exceed Maxmimum Amount "+max_amount.value);
			defloadamt.focus();
			summery.innerHTML = "";
		}
		var mymap = {};
		if( sec_curreq ){
		if( sec_curreq.checked ){
			var sec_curname = document.getElementsByName("sec_cur");
			 
			 
			for ( var i=0; i<sec_curname.length; i++ ){
				var sec_curid = document.getElementById("sec_cur"+i);
				var loadamtid = document.getElementById("loadamt"+i);
				if( sec_curid.checked){
					 
					if( loadamtid.value == "" ){
						errMessage(loadamtid, sec_curid.value + " Load Amt could not be empty. Enter Amount");
						loadamtid.focus();
						summery.innerHTML = "";
						return false;
					} 
					
					mymap[sec_curid.value] = loadamtid.value;
					 
					var sec_succ = true;
					 
				}
			}
			 
			if( !sec_succ ){
				summery.innerHTML = "";
				errMessage("sec_curreq", "Select any secondary currency"); 
				 
			}
		}else{
			
			errMessage(sec_curreq, ""); 
			summery.innerHTML = "";
		}
		
	}
		 
		var jsonObj = [];
		for (var key in mymap) {
		     //alert("key : " + key + " value : " + mymap[key]);
		     //currencylist += key+"."+mymap[key]+",";
		      //jsonObj.push(key,mymap[key]);
			jsonObj += key+"-"+mymap[key]+"~";
		}
		
		summery.innerHTML = "PROCESSING....";
		url = "makeSummeryInstCardActivateProcess.do?type=INSTANT&cardno="+cardno.value+"&defcuramt="+defloadamt.value+"&mymap="+jsonObj;
	    alert(url);
		var result = AjaxReturnValue(url);
		summery.innerHTML = result;
		errMessage("defloadamt", "");
	 
		
	//	alert(currencylist);
		
		//var cardno = document.getElementById("cardno").value;
		//var defloadamt = document.getElementById("defloadamt").value;
		//var sec_curreq = document.getElementById("sec_curreq").checked;
		//alert( "making summery");
		return false;
	}
	function enableSecondary( fldchecked ){ 
		 
			var names = document.getElementsByName("sec_cur");
			for ( var i=0; i<names.length; i++ ){
				var seccurcheckboxid = document.getElementById("sec_cur"+i);
				var loadfld = document.getElementById("loadamt"+i);
				if( fldchecked ){
					seccurcheckboxid.disabled = false;
				}else{
					seccurcheckboxid.disabled = true;
					loadfld.disabled=true;
					
					seccurcheckboxid.checked =false;
					loadfld.value = "";
				}
			}
		 
	}
	
	function enableSecondaryAmount( fldchecked, amtid ){ 
		var loadcnt = amtid.replace("sec_cur","");
		 
		var loadfld = document.getElementById("loadamt"+loadcnt);
		 
		if( fldchecked ){
			loadfld.disabled = false;
			loadfld.focus();
		}else{
			loadfld.disabled=true;
		}
	}
</script>
<style>
  table.activatetable{
  	text-align:center;
  }
  table.activatetable, table.summerytable, table.innertable, table.curtable{
 	border:1px solid gray;
 	border-collapse: collapse;
 	
 }
 
 table.curtable td{
 	border:1px solid gray;
 	color:red;
 }
   table.summerytable, table.innertable, table.curtable{
 	border-left:none;
 	border-collapse: collapse;
 	
 }
 
 table.summerytable td, table.innertable td{
 	padding-top:5px;
 	border:1px solid gray;
 	 border-bottom:none;
 	  border-top:none;
 	  text-align:left;
 }
 
 table.summerytable td.amount{
 	text-align:right;
 }
 
</style>
 


<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");  
%>

<jsp:include page="/displayresult.jsp"></jsp:include>

<s:form action="activateInstCardActionInstCardActivateProcess"  name="orderform" onsubmit="return validateActive()" autocomplete = "off"  namespace="/">



<table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="activatetable" >
	 	  
    <tr> 
    <!--   onblur="return getSubprodDet( '<%=instid%>',this.value)"    -->
    	<td>
    		Card No 
    	 : 
    		  <input type="text" name="cardno" id="cardno"  onchange="return getSubprodDet( '<%=instid%>',this.value)"  onkeypress="return dontAllowEnter(event)"  maxlength="19" style="width:250px">   
			<input type="button" value="Retrieve" id="retbtn"/>
        </td>
    </tr>	
    
    <tr>
    	<td id="dynacont"> <!--  start -->
    		&nbsp;
    	</td> <!--  end -->
    </tr>
    
    
    <tr>
    	<td colspan="4">
    		<table border="0" cellpadding="0" cellspacing="4" width="20%"  hidden="true">
				<tr>
				<td>
					<s:submit value="Submit" name="order" id="order" onclick="return validFilter()"/>
				</td>
				<td>
					<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
					 
				</td>
				</tr>
			</table>
    	</td>
    </tr>
    	
   <tr>
   		<td colspan="2">
   			<table border="0" cellpadding="0"  cellspacing="0" id="entrytab" width="100%" style="visibility:hidden;border:1px solid gray">
			 <tr> 
				<td width="30%">
					Loading Amt
			 	</td>
				<td> : 
					  <input type="text" name="loadamt" id="loadamt" style="text-align:right;width:150px"    onKeyPress="return numerals(event);" maxlength="6"  >  

 			   </td>
			</tr>	
   			<tr> <td>    </td> <td>   <table border='0' cellpadding='0' cellspacing='0' width='100%'> <tr> <td  id="sec_cur" > </td> </tr></table></td> </tr>
     
		     <tr><td></td> <td><table border='0' cellpadding='0' cellspacing='0' width='100%'  id="seccur_amt"></table>	 </tr>
		   </table>
		 </td>
   </tr> 
     
</table>




</s:form>
 
 	