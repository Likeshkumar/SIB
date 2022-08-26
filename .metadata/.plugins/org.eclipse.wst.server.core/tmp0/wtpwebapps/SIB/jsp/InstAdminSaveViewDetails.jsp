<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
 <script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

function deAuthorizeReason(){
	var reason = prompt("Reason for Reject");
	if( reason == null ){
		return false;
	}
	
	document.getElementById("reason").value = reason;
	//alert(  document.getElementById("reason").value  );
	
}

function getValue(control)
{
	
var value1=(control.value);
var name=(control.name);
var ids=(control.id);

var pad="z";
var  newname=(pad.concat(name));
alert("newname  "+newname);
alert(ids);

//preveledge.newname.value=value1;
document.preveledge.z11.value=ids;
var c=(document.getElementById("newname").value);
alert("test "+ c);

}
function showMenu(mainid, menvalue, menchecked)
{ 
	 var submenvalue = "mainmenu"+menvalue;
	 //alert("submenvalue===>"+submenvalue);
	 var submenuidcnt = document.getElementsByName(submenvalue).length; 
	
	 for( var i=0; i<submenuidcnt; i++){
		 submenuid = "submenu"+menvalue+i;
		 var subvalue=document.getElementById(submenuid).value;
		 var selectid="mainmenu"+subvalue;
		 if( menchecked ){
			 document.getElementById(submenuid).disabled=false;	
		 }
		 else if(menchecked == false){
			 document.getElementById(submenuid).checked = false;
			 document.getElementById(submenuid).disabled=true;
			 document.getElementById(selectid).value="00";
			 document.getElementById(selectid).disabled=true;
		 }
		  
	 }
	 
}

function subMenu( mainid, menvalue, menchecked, menname )
{
	//alert("menvalue===>"+menvalue);
	var sublen=document.getElementsByName(menname).length;
	for(var j=0;j<sublen;j++)
	{
		var selectid="mainmenu"+menvalue;
		if( menchecked )
		{
			document.getElementById(selectid).disabled = false;
			
			
		}
		else
		{
			document.getElementById(selectid).disabled = true;
			if(document.getElementById(selectid).disabled == true)
			{
			document.getElementById(selectid).value = "00";
			}
		}
	}
}

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
	  				//alert("c.value===> "+c.value);
  					var listboxid = "mainmenu" + c.value ;
					//alert("listboxid===> "+listboxid);
					var listbox_id = document.getElementById(listboxid);
					//alert("listbox===> "+listbox_id);
	  			if ( btnid == "selectall")
	  			{
	  				c.checked=true;
	  				c.disabled=false;
	  				if(c.checked == true)
	  				{
	  				listbox_id.disabled=false;
	  				}
	  			}else
	  			{
	  				c.checked=false;
	  				c.disabled=true;
	  				listbox_id.disabled= true;
	  				listbox_id.value="00";
	  			}
	  		}
	  	  }		  			
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
	//alert("john"+url);
	window.location=url;
	return false;
}

function validation_authprofile(){//Reject
 	var auth = "Reject";//document.getElementById("auth0").value;
 	var prof_id = document.getElementById("prof_id").value; 
 	var reason = prompt('Enter the Reason for Reject');
	 if( reason ){
		 var url = "authorizeProfileDetailsUserManagementAction.do?prof_id="+prof_id+"&reason="+reason+"&auth="+auth;
		 //alert(url);
		 window.location = url; 
	 }  
	 return false;
}

 </script>


<div align="center">

	<s:form name="preveledge" action="authorizeProfileDetailsUserManagementAction" autocomplete="off">
		<table>
			<tr>
				<td align="center">
					<b>Menu Privilege Configuration</b>
				</td>
			</tr>
			<tr>
				<td>
				<s:hidden name="reason" id="reason" />
					<b>Profile Name : <font color="blue"> <s:property value="prof_name" /> </font> </b>
					<s:hidden name="prof_id" id="prof_id" value="%{Profile_id}"></s:hidden> 
				</td>
			</tr>
					

		</table>
<jsp:include page="/displayresult.jsp"></jsp:include>
<input type="hidden" id="errormsg" name="errormsg">
<table border='0' cellpadding='0' cellspacing='0' width='60%' align="center" >
	<!--	<tr><td id="errmsg" colspan="2">&nbsp;</td></tr>
			<tr><td><input type="hidden" id="errormsg" name="errormsg"></td></tr>
			<s:property value="selectedinstmenulist"/>
	-->
			<% int maincount=0; %>
				<s:iterator  value="#session.menulist">
				<s:set name="mainid"> ${MENUID} </s:set>
					
					<s:if test=" MENUID != '30' && MENUID != '20'">
							<tr>
								<td>
									<%int maincnt=maincount++; %>
									<input type="checkbox" name="mainmenu" disabled
										<s:iterator value="selectedinstmenulist">
											<s:set name="selmenuid">	<s:property /> </s:set>	 
											<s:if test="%{#selmenuid==#mainid}"> checked='checked' </s:if> 
										</s:iterator>
									value="${MENUID}" id="mainmenu<%=maincnt%>" onclick="showMenu(this.id, this.value, this.checked)"/>${MENUNAME}
								</td> 
								
							</tr>
							<s:action name="makerchekstatus" var="makerchek">
									<s:param name="menu_id">${MENUID} </s:param>
							</s:action>
							<s:if test="#makerchek.mkckflag=='enable'">
								<s:action  name="subMenuListInstAdmin" executeResult="false"  var="subMenuListInst">
									<s:param name="menuid" >${MENUID}</s:param>
									<s:param name="profilelist" >${profilelist}</s:param>
								</s:action>
								<% int submencnt=0,mkckcnt=0; %>
								<s:iterator  value="#subMenuListInst.submenulist" >
									
									<s:set name="menusubid"> ${MENUID} </s:set>
									<s:set name="menuid1"> ${MENUID}-M </s:set>	
									<s:set name="menuid2"> ${MENUID}-C </s:set>
									<s:set name="menuid3"> ${MENUID}-D </s:set>				
										<tr>
											<td>&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="checkbox" disabled name="mainmenu${mainid}" 
													<s:iterator value="selectedinstmenulist">
														<s:set name="selsmenuid"></s:set>
														<s:if test="%{#selsmenuid==#menusubid}"> checked='checked' </s:if>	 
													</s:iterator>									
												id="submenu${mainid}<%=submencnt++%>" value="${MENUID}"  onclick="subMenu(this.id, this.value, this.checked,this.name)"/>${MENUNAME}	
																																
											</td>
											
											<td>
												<s:action  name="makercheckerselected" executeResult="false"  var="selectd">
													<s:param name="submenu_id">${MENUID} </s:param>
												</s:action>
												
													<select  disabled name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" style="width:120px" onclick="subMenu(this.id, this.value, this.checked,this.name)">
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
							
								<s:action  name="subMenuListInstAdmin" executeResult="false"  var="subMenuListInst">
									<s:param name="menuid">${MENUID}</s:param>
									<s:param name="profilelist" >${profilelist}</s:param>
								</s:action>
								
								<% int submencnt=0,mkckcnt=0; %>
									<s:iterator  value="#subMenuListInst.submenulist" >
									<s:set name="menusubid2"> ${MENUID} </s:set>
										<tr>
											<td>&nbsp;&nbsp;&nbsp;&nbsp;
												
												<input type="checkbox" name="mainmenu${mainid}"
												
												<s:iterator value="selectedinstmenulist">
														<s:set name="seltedmenuid"></s:set>
														<s:if test="%{#seltedmenuid==#menusubid2}"> checked='checked' </s:if>	 
												</s:iterator>
												
												id="submenu${mainid}<%=submencnt++%>" value="${MENUID}" onclick="subMenu(this.id, this.value, this.checked,this.name)"/>${MENUNAME}
												
												<input type="hidden" name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" value="${MENUID}-D"/>																				
											</td>	
										</tr>
									</s:iterator>
							</s:if>
					</s:if>
				</s:iterator>
				<!-- <tr><td>&nbsp;</td></tr>
					<tr align="center">
						<td colspan='2'>
							
							<input type="submit" name="auth" id="auth1" value="Authorize" onclick="return validation_authfee();"/>
							<input type="submit" name="de-auth" id="auth0" value="Reject"  class="cancelbtn"  onclick="return deAuthorizeReason()"/>						
						</td>
					</tr> -->
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
			<input type="button" name="cancel" id="cancel" value="Delete"  class="cancelbtn"  onclick="return deleteProfile()"/>	
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

	</s:form>
</div>