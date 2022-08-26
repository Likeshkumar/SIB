<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

/* function validateMenu()
{
	
	var mainchecker = false;
	var mainmenu = false;
	var mainmenus = document.getElementsByName("mainmenu");
	//alert("The Totoal Main Menu is ===> "+mainmenus.length);
	var mainmenucount=0;
	for(var i=0;i<mainmenus.length;i++)
	{
		
		var submenu = false;
		var checker = false;
		
		//alert("The I Values is ===> "+i);
		var mainmenuid = "mainmenu"+i;
		//alert("Main Menu id===> "+mainmenuid);
		var mainmenu_checked = document.getElementById(mainmenuid);
		if(mainmenu_checked.checked)
		{
			mainmenucount=mainmenucount+1;
		
			//mainmenu = true;
			var mainmenuvalue = document.getElementById(mainmenuid).value;
			var submenusname = "mainmenu"+mainmenuvalue;
			//alert("The Submenu is ===>  "+submenusname);
			var submenus = document.getElementsByName(submenusname).length;
			//alert("Total Submenu for the Mainmenu "+mainmenuvalue+"  is ===> "+submenus);
			for(var j=0;j<submenus;j++)
			{
				var submenuid = "submenu"+mainmenuvalue+j;
				//alert("submenuid in side for loop ===>"+submenuid);
				var submenuchecked = document.getElementById(submenuid);
				if(submenuchecked.checked)
				{
					checker = false;
					//alert("submenuid===> got selevted break the inner for loop  "+submenuid);
					break;
				}
				else{
					//alert("Checker Value is true");
					checker = true;
				}
				
			}
		}
		if(checker){
			//alert("Checker value is True outer for loop break");
			submenu = false;
			break;
		}
		else{
			submenu = true;
		}
	}
	//alert("The value of J is==> "+mainmenucount);
		if(mainmenucount!=0 && mainmenucount<=mainmenus.length){
			//alert("One Main Menu is selected");
			mainmenu=true;
		}
	
	//alert("Comming To Check the mainmenu flag "+mainmenu+" submenu flag ===> "+submenu);
	var err_msg = document.getElementById("errormsg");
	if( ! mainmenu ){
		errMessage(err_msg,"Please select atleast one menu ");
		window.scrollTo(0,0);
		return false;
	}	
	else if( ! submenu ){
		errMessage(err_msg, "Please select atleast one submenu ");
		return false;
	}

	
} */

function validateMenu()
{
	//alert("Submit Validation");
	
	var submenunotchecked = false;
	var selectbox_notselet = false;
	var mainmenucheck = false;
	var menuselect = 0;
	var err_msg = document.getElementById("errormsg");
	var mainmenus = document.getElementsByName("mainmenu");
	//alert("The Totoal Main Menu is ===> "+mainmenus.length);
	for(var i=0;i<mainmenus.length;i++)
	{
		var submenus_select = false;
		//alert("The Loop Values is ===> "+i);
		var mainmenuid = "mainmenu"+i;
		//alert("Main Menud ID's====> "+mainmenuid);
		var mainmenu_checked = document.getElementById(mainmenuid);
		if(mainmenu_checked.checked)
		{
			menuselect = menuselect + 1;
			var mainmenuvalue = document.getElementById(mainmenuid).value;
		//alert("Mani Menu Values ----> "+mainmenuvalue);
			var submenusname = "mainmenu"+mainmenuvalue;
			//alert("Submenu name is ====> "+submenusname);
			var submenus = document.getElementsByName(submenusname).length;
		//alert("Total Submenus for "+mainmenuvalue+" is ===> "+submenus);
			var submenucheck =true;
			for(var j=0;j<submenus;j++)
			{
				
				var selectbox = false;
				var submenuid = "submenu"+mainmenuvalue+j;
				//alert("Submenu ID's====> "+submenuid);
				var submenuchecked = document.getElementById(submenuid);
				var selectboxid="mainmenu"+submenuchecked.value;
			   //alert("The Select BOX id is ====> "+selectboxid);
				
				if(submenuchecked.checked)
				{
					submenucheck = false;
					var selectvalue = document.getElementById(selectboxid).value;
					//alert("The selectvalue for "+selectvalue+" selectboxid ");
					if(selectvalue == "00")
					{
						//alert("Select Box Values Not Selected true ");
						selectbox = true;
						break;
					}
				}

			}
			//alert("Var submenucheck is =====> "+submenucheck);
			if(submenucheck)
			{
			//alert("No SubMenus Selected For the Main Menu "+mainmenuvalue+ "Break The Main Loop ");
				submenunotchecked =true;
				break;
			}
			//alert("Var Select Box ====> "+selectbox);
			if(selectbox)
			{
				//alert("No Option Selected for the submenu "+submenuid+" of main menu"+mainmenuvalue);
				selectbox_notselet = true;
				break;
			}
		}
	}
	//alert("menuselect===> "+menuselect);
	if(menuselect != 0 && menuselect<=mainmenus.length)
	{
		mainmenucheck = true;
	}

	
	//alert("mainmenucheck ==="+mainmenucheck+" submenucheck =====> "+submenunotchecked+"  selectbox====> "+selectbox_notselet);
	
	if(!mainmenucheck)
	{
		//alert("No Main Menu selected ");
		errMessage(err_msg,"Please select atleast one Main Menu ");
		window.scrollTo(0,0);
		return false;
	}
	if(submenunotchecked)
	{
		//alert("No SubMenus Selected");
		errMessage(err_msg,"Please select atleast one SubMenu ");
		window.scrollTo(0,0);
		return false;
	}
	if(selectbox_notselet)
	{
		//alert("No Option Selected ");
		errMessage(err_msg,"Please select Maker Checker Option ");
		window.scrollTo(0,0);
		return false;
	}

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
	  				c.disabled=true;
	  				
	  			}
	  		}
	  	  }		  			
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
function validation_authprofile(){//Reject
 	var auth = document.getElementById("auth0").value;
 	var prof_id = document.getElementById("prof_id").value;
 	var instid = document.getElementById("instid").value;
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason ){
		 var url = "authorizeProfileDetailsUserManagementAction.do?prof_id="+prof_id+"&reason="+reason+"&auth="+auth+"&instid="+instid;
		 window.location = url; 
	 }  
	 return false;
}


function deleteProfile(){
	//alert("deleted....");
	var instid = document.getElementById("instid");
	if( instid ){
		instid = document.getElementById("instid").value;
	}else{
		instid = "";
	}
	var profileid = document.getElementById("prof_id").value;
	var reason = prompt("Enter Reason For Delete ");
	if( reason == null ){
		return false;
	}
	var url = "deleteProfileUserManagementAction.do?instid="+instid+"&profileid="+profileid+"&reason="+reason;
	//alert("john"+url);
	window.location=url;
	return false;
}


function deleteAuthorize( authtype ){
	 
	var profileid = document.getElementById("prof_id").value;
	
	var instid = document.getElementById("instid");
	if( instid ){
		instid = document.getElementById("instid").value;
	}else{
		instid = "";
	}
	
	var reason = "";
	if( authtype == 'DEAUTH'){
		reason = prompt("Enter Reason For Reject ");
		if( reason == null ){
			return false;
		}	
	}
	
	var url = "deleteProfileAtuhActionUserManagementAction.do?instid="+instid+"&profileid="+profileid+"&reason="+reason+"&authtype="+authtype;
	//("john"+url);
	window.location=url;
	return false;
}



 </script>
<%String instid = (String)session.getAttribute("Instname");%>

<s:form name="preveledge" action="authorizeProfileDetailsUserManagementAction" autocomplete="off">

<div align="center">
<table><tr><td align="center"><b> Profile For <font color="red"><s:property value="prof_name" /> </font> </b></td></tr>
<s:hidden name="prof_id" id="prof_id" value="%{Profile_id}"></s:hidden> 
<s:hidden name="instid" id="instid" value="%{instname}" />
<tr>  
<jsp:include page="/displayresult.jsp"></jsp:include>	
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</div>

<div align='center'>
<input type="hidden" id="errormsg" name="errormsg">
<table border='0' cellpadding='0' cellspacing='0' width='50%' align="center" >
	<!--	<tr><td id="errmsg" colspan="2">&nbsp;</td></tr>
			<tr><td><input type="hidden" id="errormsg" name="errormsg"></td></tr>
	 --> 
	<% int maincount=0; %>	
 <!-- <s:property value="selectedmenulst"/> --> 
	<s:iterator  value="adminproflist">
		<s:set name="mainid"> ${MENUID} </s:set>
		<tr>
			<td>
			<%int maincnt=maincount++; %>
				
				<input type="checkbox"  name="mainmenu" disabled
				
				<s:iterator value="selectedmenulst">
				<s:set name="selmenuid">	<s:property /> </s:set>	 
				<s:if test="%{#selmenuid==#mainid}"> checked='checked' </s:if> 
				</s:iterator>
				
				value="${MENUID}" id="mainmenu<%=maincnt%>" fieldValue="%{MENUID}" onclick="showMenu(this.id, this.value, this.checked)"/>${MENUNAME}
			</td>
		</tr>
		<s:action name="makerchekSuperadminstatus" var="makerchek">
				<s:param name="menu_id">${MENUID} </s:param>
				<s:param name="inst_id">${session.Instname} </s:param>
		</s:action>
				<s:if test="#makerchek.mkckflag=='enable'">	    
					<s:action name="adminProfSubMenuList" executeResult="false"  var="sunny">
						<s:param name="menuid" >${MENUID}</s:param>
					</s:action>
					
					<% int submencnt=0; %>
					<s:iterator  value="#sunny.adminsubmenulist"> 
					<s:set name="menuid"> ${MENUID} </s:set>
					<!-- <s:property value="menuid"/>  -->
								<tr> 
										<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" disabled name="mainmenu${mainid}" 
										
										<s:iterator value="selectedmenulst">
										<s:set name="selmenuid">	<s:property /> </s:set>
										<s:if test="%{#selmenuid==#menuid}"> checked='checked' </s:if> 
										</s:iterator>
									
										id="submenu${mainid}<%=submencnt++%>" value="${MENUID}" />${MENUNAME}</td>
										<td>
												<s:action  name="makercheckerselected" executeResult="false"  var="selectd">
													<s:param name="submenu_id">${MENUID} </s:param>
												</s:action>
												
													<select disabled name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" style="width:120px" onclick="subMenu(this.id, this.value, this.checked,this.name)">
														<option value="00">Select</option>
														<option value="${MENUID}-M" 
														<s:if test="#selectd.mkck_flag=='MAKER'">
														selected="selected" 
														</s:if>>Maker
														</option>
														<option value="${MENUID}-C"
														<s:if test="#selectd.mkck_flag=='CHECKER'">
														selected="selected" 
														</s:if>
														>Checker</option>
														<option value="${MENUID}-D"
														<s:if test="#selectd.mkck_flag=='WITHOUTMCKRCHK'">
														selected="selected" 
														</s:if>														
														>Both</option>
													</select>
											</td>
								</tr>
								
					</s:iterator>
		 </s:if>
		 <s:if test="#makerchek.mkckflag=='disabled'">
					<s:action name="adminProfSubMenuList" executeResult="false"  var="sunny">
						<s:param name="menuid" >${MENUID}</s:param>
					</s:action>
					
					<% int submencnt=0; %>
					<s:iterator  value="#sunny.adminsubmenulist"> 
					<s:set name="menuid"> ${MENUID} </s:set>
					<!-- <s:property value="menuid"/>  -->
								<tr> 
										<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="mainmenu${mainid}"  disabled
										
										<s:iterator value="selectedmenulst">
										<s:set name="selmenuid">	<s:property /> </s:set>
										<s:if test="%{#selmenuid==#menuid}"> checked='checked' </s:if> 
										</s:iterator>
									
										id="submenu${mainid}<%=submencnt++%>" value="${MENUID}" />${MENUNAME}
										<input type="hidden" name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" value="${MENUID}-D"/>
										</td>																					
								</tr>
								
					</s:iterator>
			</s:if>
	</s:iterator>
	
	
	<tr><td>&nbsp;</td></tr>
</table>
<table width='30%'>
	 
	<tr align="center">
	<s:if test="%{doact=='$VIEW'}">
		<td colspan='2' style='text-align:center'> 
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>						
		</td>
	</s:if>
	<s:elseif test="%{doact=='$DEL'}">
		<td colspan='2' style='text-align:center'> 
			<input type="button" name="cancel" id="cancel" value="Delete"    onclick="return deleteProfile()"/>	
			<input type="button" name="cancel" id="cancel" value="Cancel"  class="cancelbtn"  onclick="return confirmCancel()"/>							
		</td>
	</s:elseif>
	
	<s:elseif test="%{doact=='$DELAUTH'}">
		<td colspan='2' style='text-align:center'> 
			<input type="button" name="cancel" id="cancel" value="Authorize"  class="cancelbtn"  onclick="return deleteAuthorize('AUTH')"/>	
			<input type="button" name="cancel" id="cancel" value="Reject"  class="cancelbtn"  onclick="return deleteAuthorize('DEAUTH')"/>							
		</td>
	</s:elseif> 
	<s:elseif test="%{doact=='$AUTH'}">
		<td colspan='2'>
				<input type="submit" name="auth" id="auth1" value="Authorize" onclick="return validation_authfee();"/>
				<input type="button" name="auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return validation_authprofile()"/>						
			</td>
	</s:elseif>  
	<s:else>  
		<td colspan='2'>   <s:property value="doact"/> </td>
	</s:else>
</tr>
</table> 
</div>

</s:form>


