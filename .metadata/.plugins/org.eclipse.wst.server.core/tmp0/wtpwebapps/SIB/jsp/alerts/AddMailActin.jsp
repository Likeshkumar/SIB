<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

function validateMenu()
{
	valid=true;
	var onechecked = false;
	var institution = document.getElementById("inst");
	if(institution)
	{
		if(institution.value == "-1")
		{
			errMessage(institution,"Select Institution");
			return false;
		}	
	}
	var err_msg = document.getElementById("errormsg");
	var submenus = document.getElementsByName("submenu").length;
	//alert("SUBMENU LENGTH  "+submenus);
			for(var j=0;j<submenus;j++)
				{
					var submenuid = "submenu"+j;
					var submenuchecked = document.getElementById(submenuid);
					if(submenuchecked.checked)
					{
						onechecked=true;
						break;
					}
				}
			//alert("CHECKBOX CHECKED SO OUTSIDE FOR LOOP");
			if(!onechecked)
			{
				errMessage(err_msg, "Please select atleast one submenu ");
				return false;
			}
			return valid;
	}


function getValue(control)
{
	
var value1=(control.value);
var name=(control.name);
var ids=(control.id);

var pad="z";
var  newname=(pad.concat(name));
//alert("newname  "+newname);
//alert(ids);

//preveledge.newname.value=value1;
document.preveledge.z11.value=ids;
var c=(document.getElementById("newname").value);
//alert("test "+ c);

}

function showMenu(mainid, menvalue, menchecked)
{
	//alert("Main id "+mainid+" Menu Value===>"+menvalue+" Checked===> "+menchecked);

	 var submenvalue = mainid+menvalue;
	 
	 var submenvalue = "mainmenu"+menvalue;
	 //alert("submenvalue===> "+submenvalue);
	 var submenuidcnt = document.getElementsByName(submenvalue).length; 
	 //alert("submenuidcnt===> "+submenuidcnt);
	 for( var i=0; i<submenuidcnt; i++){
		 submenuid = "submenu"+menvalue+i;
		 
		 if( menchecked ){
		  	document.getElementById(submenuid).disabled=false ;
		 }else if(document.getElementById(submenuid).checked = true){
			 document.getElementById(submenuid).checked = false;
			 document.getElementById(submenuid).disabled=true;
		 }
	 }

}


function checkAll( btnid )
{
	  
		var mainmenlist = document.getElementsByName("mainmenu");
		//alert("Lenght of the Parent ids====> "+mainmenlist.length);
	    for ( var i=0; i<mainmenlist.length; i++ )
	    {
	  	  var mainmenid = "mainmenu"+i;
	  	  //alert("mainmenid====>   "+mainmenid);
	  	  var d=document.getElementById(mainmenid); 
	  	  var mainmenval = document.getElementById(mainmenid).value;
	  	 //alert("mainmenval===> "+mainmenval);
	  	  //var submenname = mainmenid   +  mainmenval; Sankar change
	  	  var submenname =  "mainmenu"+mainmenval;
	  	 //alert("submenname====> "+submenname);
	  	  var submencnt = document.getElementsByName(submenname).length;  
	  		if ( btnid == "selectall"){
		  		d.checked=true;
	  		}else{
	  			d.checked=false;
	  		}
	  		
	 		for( var x=0; x<submencnt; x++ ){
	  			var submenid = "submenu"+mainmenval+x;
	  			var c=document.getElementById(submenid); 
	  			
	  			if ( btnid == "selectall"){
	  				c.checked=true;
	  				c.disabled=false;
	  			}else{
	  				c.checked=false;
	  				
	  				
	  			}
	  		}
	  	  }		  			
}
  
 </script>
<%String username = (String)session.getAttribute("SS_USERNAME");
String usertype = (String)session.getAttribute("USERTYPE");
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<s:form action="saveaddmailactionAddMailAction.do" autocomplete="off">


<div align='center'>
<input type="hidden" id="errormsg" name="errormsg">
<%if(usertype.equals("SUPERADMIN")){ %>
<s:if test="%{checkinstitution=='Y'}">
	<s:iterator value="institution_details">	
		<table>	
			<tr>
				<td>SELECT INSTITUTION</td>
				<td>
				<s:select list="%{institutionlist}" id="inst" name="inst"
							listKey="INST_ID" listValue="INST_ID" headerKey="-1" value="%{INST_ID}"
							headerValue="-SELECT-"/>
				</td>
			</tr>
		</table>
	</s:iterator>
</s:if>
<s:if test="%{checkinstitution=='N'}">
<table>	
		<tr>
			<td>SELECT INSTITUTION</td>
			<td>
			<s:select list="%{institutionlist}" id="inst" name="inst"
						listKey="INST_ID" listValue="INST_ID" headerKey="-1" value="%{INST_ID}"
						headerValue="-SELECT-"/>
			</td>
		</tr>
	</table>
</s:if>
<%} %>
<table border='1' cellpadding='0' cellspacing='0' width='50%' align="center" >
	<% int maincount=0;int submencnt=0; %>
	<s:iterator  value="superAddProfilePrevList">
	<s:set name="mainid"> ${MENUID} </s:set>
		<tr>
			<td>
			<%int maincnt=maincount++; %>			
			<input type="hidden"  name="mainmenu" 
				
				<s:iterator value="selectedmenulst">
				<s:set name="selmenuid">	<s:property /> </s:set>	 
				<s:if test="%{#selmenuid==#mainid}"> checked='checked' </s:if> 
				</s:iterator>
				
				value="${MENUID}" id="mainmenu<%=maincnt%>" fieldValue="%{MENUID}" onclick="showMenu(this.id, this.value, this.checked)"/>${MENUNAME}
			
			</td> 
			<s:set name="mainid"> ${MENUID} </s:set>	
		</tr>
	    			<%
	    			 if(usertype.equals("SUPERADMIN")){ %>
					<s:action name="superSubMenuListAddMailAction" executeResult="false"  var="sunny">
						<s:param name="menuid" >${MENUID}</s:param>
					</s:action>
					<%}else{ %>
					<s:action name="instituteSubMenuListAddMailAction" executeResult="false"  var="sunny">
						<s:param name="menuid" >${MENUID}</s:param>
					</s:action>
					<%} %>
					
					<s:iterator  value="#sunny.superAddProfSubmenuList">
									<s:set name="menuid"> ${MENUID} </s:set>
									 
								<tr> 
										<td>&nbsp;&nbsp;&nbsp;&nbsp;
										 
										<input type="checkbox" name="submenu" 
										
										<s:iterator value="selectedmenulst">
											<s:set name="selmenuid">${EMAIL_ALERT_LIST}</s:set>
											<s:if test="%{#selmenuid==#menuid}"> checked='checked' </s:if> 
										</s:iterator>
									
										id="submenu<%=submencnt++%>" value="${MENUID}" />${MENUNAME}
										
										</td>	
								</tr>
								
					</s:iterator>
		
	</s:iterator>
	<tr><td>&nbsp;</td></tr>
	</table>
<table width='30%'>
	<tr align="center">
	<td colspan='2'>
	<s:submit name="submitprofile" id="submitprofile" value="Save" onclick="return validateMenu()"/></td>
	<!-- <td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/></td>
	<td><input type="button" class="selectallbtn" value="SelectAll" id="selectall" onclick='checkAll(this.id);'/>		
	</td>
	<td>
		<input type="button" class="selectallbtn" value="De-SelectAll" id="deselectall" onclick='checkAll(this.id);'/>
	</td> -->
	</tr>
</table> 
</div>

</s:form>

