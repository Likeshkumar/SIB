<%@taglib uri="/struts-tags" prefix="s" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<link rel="stylesheet" type="text/css" href="style/calendar.css"/>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jsp/personalize/script/PersonalCardOrder.js"></script>
<script>
function display(radiotype)
{	
	
	if(radiotype=='quartely')
	{
		document.nfs.quartely.disabled=false;
		document.nfs.halfyearly.disabled=true;
		document.nfs.annualy.disabled=false;
	}
	if(radiotype=='halfyearly')
	{
	 	document.nfs.quartely.disabled=true;
		document.nfs.halfyearly.disabled=false;
		document.nfs.annualy.disabled=false;
	}
	if(radiotype=='annualy')
	{
		document.nfs.quartely.disabled=true;
		document.nfs.halfyearly.disabled=true;
		document.nfs.annualy.disabled=false;
	}
}
function validate()
{
	//alert("called validation ");
	var radiobut = document.nfs.nfsrad;
	var radiolen = radiobut.length;
	var criteriaflag = true;
	for(var i=0;i<radiolen;i++){
		if(radiobut[i].checked){
			criteriaflag = false;
			var radiovalue = radiobut[i].value;
			if(radiovalue!="annualy")
			{
				if (document.getElementById(radiovalue+"id").value=="-1")
				{
					errMessage(document.getElementById(radiovalue+"id"),"Please Select "+radiovalue);
					return false;
				}
			}
			if(document.nfs.annualy.value=="-1"){
				errMessage(document.nfs.quartely,"Please Select year");
				return false;
			}
			
		}
	}
	if(criteriaflag){
		alert("Please Select the Criteria type");
		document.getElementById("quartrad").focus();
		return false;
	}
	var radionetwork = document.nfs.network;
	for(var j=0;j<radionetwork.length;j++){
		if(radionetwork[j].checked){
			if(radionetwork[j].value=="NFS"){
				if(document.nfs.instid.value=="-1"){
					errMessage(document.nfs.instid,"Please Select the Institution");
					return false;
				}				
			}
		}
	}
	
	return true;

}
function viewnfs(network){
	if(network=="NFS")
		 document.getElementById("nfstr").style.display="table-row";
	else
		document.getElementById("nfstr").style.display="none";
}
</script>
</head>
<jsp:include page="/displayresult.jsp"></jsp:include>
<% String instid = (String)session.getAttribute("Instname"); %>
<s:form action="genNFSReportReportgenerationAction"  name="nfs" autocomplete = "off" namespace="/" javascriptTooltip="true">
	<table border="0" cellpadding="2" cellspacing="4" width="50%" align="center">
		<tr>
		<td><b>Criteria</b></td>
		<td></td>
		<td>
		<input type="radio" name="nfsrad" id="quartrad" value="quartely" onclick='display(this.value)'/> Quartely
		<input type="radio" name="nfsrad" id="halfrad" value="halfyearly" onclick='display(this.value)' /> Half-yearly
		<input type="radio" name="nfsrad" id="annrad" value="annualy" onclick='display(this.value)'/> Annual
		</tr>
			
		<tr>
			<td>Quartely</td>
			<td> : </td>
			<td>
				<select id="quartelyid" name="quartely" disabled="disabled"/>
					<option value="-1">----Select----</option>
					<option value="0-2">jan-mar</option>
					<option value="3-5">apr-jun</option>
					<option value="6-8">jul-sep</option>
					<option value="9-11">oct-dec</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Half-Yearly</td>
			<td> : </td>
			<td>
				<select id="halfyearlyid" name="halfyearly" disabled="disabled"/>
					<option value="-1">----Select----</option>
					<option value="0-5">jan-jun</option>
					<option value="6-11">jul-dec</option>
					
				</select>
			</td>
		</tr>
		<tr>
			<td>Annualy</td>
			<td> : </td>
			<td>
				<select id="annualyid" name="annualy" disabled="disabled"/>
					<option value="-1">----Select----</option>
					<option value="2000">2000</option>
					<option value="2007">2007</option>
					<option value="2003">2003</option>
					<option value="2011">2011</option>
					<option value="2012">2012</option>
					<option value="2013">2013</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Network Selection</td>
			<td></td>
			<td>
				<input type="radio" name="network" id="networkkucb" value="<%out.print(instid); %>" onclick="viewnfs(this.value)" checked/> <% out.print(instid); %> <input type="radio" name="network" id="networknfs" value="NFS" onclick="viewnfs(this.value)"/> Network
			</td>
		</tr>
		<tr id="nfstr" style="display:none">
			<td>Institution Selection </td>
			<td> : </td>
			<td>
				<select class="inputselect" id="instid" name="instid">
					<option value="-1">Select</option>
					<option value="<%out.print(instid);%>-NFS"> Aquirer inst:-  <% out.print(instid); %> &amp; IssuerInst:- Network </option>
					<option value="NFS-<%out.print(instid);%>"> Aquirer inst:- Network &amp; IssuerInst:-  <% out.print(instid); %> </option>
				</select>
			</td>
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="4" width="20%" >
	<tr>
		<td>	
			<input type="submit" value="Next" name="next_process" id="next_process" onclick=" javascript:return validate();"/>
		</td>
		<td>
			<!-- <input type="button" name="cancle" value="Cancel" onclick="return forwardtoCardOrderPage();"> -->
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>
		</td>
	</tr>
</table>
</s:form>

