<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c"%>
 <script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
 
function getValue(control){	
var value1=(control.value);
var name=(control.name);
var ids=(control.id);
var pad="z";
var  newname=(pad.concat(name));
document.preveledge.z11.value=ids;
var c=(document.getElementById("newname").value);
}
function showMenu(mainid, menvalue, menchecked){
	 var submenvalue = "mainmenu"+menvalue;
	 var submenuidcnt = document.getElementsByName(submenvalue).length; 
	 for( var i=0; i<submenuidcnt; i++){
		 submenuid = "submenu"+menvalue+i;
		 var subvalue=document.getElementById(submenuid).value;
		 var selectid="mainmenu"+subvalue;
		 if( menchecked ){
			 document.getElementById(submenuid).disabled=false;	
			 document.getElementById(submenuid).checked=true;	
			 document.getElementById(selectid).disabled=false;
		 }
		 else if(menchecked == false){
			 document.getElementById(submenuid).checked = false;
			 document.getElementById(submenuid).disabled=true;
			 //document.getElementById(selectid).value="00";
			 document.getElementById(selectid).disabled=true;			 
		 }		  
	 }	 
}

function subMenu( mainid, menvalue, menchecked, menname ){
	var sublen=document.getElementsByName(menname).length;
	for(var j=0;j<sublen;j++){
		var selectid="mainmenu"+menvalue;
		if( menchecked ){
			document.getElementById(selectid).disabled = false;
		}else{
			document.getElementById(selectid).disabled = true;	
			if(document.getElementById(selectid).disabled == true){
			document.getElementById(selectid).value = "00";
			}
		}
	}
}

/*function validateMenu()
{
	alert("Welcome");
	var mainmenchk = false;
	var submenchk = false;
	var w=document.getElementsByName("mainmenu");
	for(var r=0;r<w.length;r++)
	{
		
		var valueMainmenu="mainmenu"+r;
		alert("Mainmenu ID==>    " +valueMainmenu);
		var mainmenu=document.getElementById(valueMainmenu);
		if( mainmenu.checked ) 
		{
			alert("Mainmenu Checked");
			mainmenchk = true;
			var submenuname="mainmenu"+mainmenu.value;
			alert("submenuname====> "+submenuname);
			var submenu=document.getElementsByName(submenuname);
			for(var j=0;j<submenu.length;j++){ 
				submenuid="submenu"+mainmenu.value+j;
				alert("SUB MENU ID ==> "+submenuid);
				var submenuid_result=document.getElementById(submenuid);
				alert("SUB MENU VALUE ==> "+submenuid_result.value);
				var selectbox="mainmenu"+submenuid_result.value;
				if( submenuid_result.checked ){
					submenchk = true;
					alert("Submenu Checked");
					if(document.getElementById(selectbox).value == "00"){
						errMessage(document.getElementById("submenu0060"), " Please select atleast one List ");
						window.scrollTo(0,0);
						return false;
					}
				} 
			}
		} 
	}
		if( ! mainmenchk ){
			errMessage(mainmenu,"Please select atleast one menu ");
			window.scrollTo(0,0);
			return false;
		}	
		else if( ! submenchk ){			
			errMessage(document.getElementById("submenu0060"), " Please select atleast one submenu ");
			window.scrollTo(0,0);
			return false;		
		}
		
}*/


function validateMenu(){
	var submenunotchecked = false;
	var selectbox_notselet = false;
	var mainmenucheck = false;
	var menuselect = 0;
	var err_msg = document.getElementById("errormsg");
	var mainmenus = document.getElementsByName("mainmenu");
	for(var i=0;i<mainmenus.length;i++){
		var submenus_select = false;
		var mainmenuid = "mainmenu"+i;
		var mainmenu_checked = document.getElementById(mainmenuid);
		if(mainmenu_checked.checked){
			menuselect = menuselect + 1;
			var mainmenuvalue = document.getElementById(mainmenuid).value;
			var submenusname = "mainmenu"+mainmenuvalue;
			var submenus = document.getElementsByName(submenusname).length;
			var submenucheck =true;
			for(var j=0;j<submenus;j++){
				var selectbox = false;
				var submenuid = "submenu"+mainmenuvalue+j;
				var submenuchecked = document.getElementById(submenuid);
				var selectboxid="mainmenu"+submenuchecked.value;
				if(submenuchecked.checked){
					submenucheck = false;
					var selectvalue = document.getElementById(selectboxid).value;
					if(selectvalue == "00"){						
						selectbox = true;
						break;
					}
				}
			}			
			if(submenucheck){
				submenunotchecked =true;
				break;
			}if(selectbox){				
				selectbox_notselet = true;
				break;
			}
		}
	}
	if(menuselect != 0 && menuselect<=mainmenus.length){
		mainmenucheck = true;
	}if(!mainmenucheck){
		errMessage(err_msg,"Please select atleast one Main Menu ");
		window.scrollTo(0,0);
		return false;
	}if(submenunotchecked){		
		errMessage(err_msg,"Please select atleast one SubMenu ");
		window.scrollTo(0,0);
		return false;
	}if(selectbox_notselet){		
		errMessage(err_msg,"Please select Maker Checker Option ");
		window.scrollTo(0,0);
		return false;
	}
}

function checkAll( btnid ){
		var mainmenlist = document.getElementsByName("mainmenu");
	    for ( var i=0; i<mainmenlist.length; i++ ){
	  	  var mainmenid = "mainmenu"+i;
	  	  var d=document.getElementById(mainmenid); 
	  	  var mainmenval = document.getElementById(mainmenid).value;
	  	  var submenname =  "mainmenu"+mainmenval;
	  	  var submencnt = document.getElementsByName(submenname).length;  
	  		if ( btnid == "selectall"){
		  		d.checked=true;
	  		}else{
	  			d.checked=false;
	  		}
	 		for( var x=0; x<submencnt; x++ ){
		  			var submenid = "submenu"+mainmenval+x;
		  			var c=document.getElementById(submenid);
  					var listboxid = "mainmenu" + c.value ;
					var listbox_id = document.getElementById(listboxid);
	  			if ( btnid == "selectall"){
	  				c.checked=true;
	  				c.disabled=false;
	  				if(c.checked == true){
	  					listbox_id.disabled=false;
	  				}
	  			}else{
	  				c.checked=false;
	  				c.disabled=true;
	  				listbox_id.disabled= true;
	  				//listbox_id.value="00";
	  			}
	  		}
	  	  }		  			
}
 </script>
 <%String profileflag = (String)session.getAttribute("PROFILE_FLAG"); %>
<div align="center">
<s:form name="preveledge" action="prevaddAction" autocomplete="off" >
<input type="hidden" name="profileflag" id="profileflag" value="<%=profileflag%>"/>
		<table><tr><td align="center"><b>Menu Privilege Configuration</b></td></tr>
				<tr>
					<td>
						<%-- <b>Profile Name : <font color="blue"><%String P_name = (String)session.getAttribute("prof_name"); %>
																<%=P_name %>	</font></b> --%>
						<b>Profile Name : <font color="blue"><c:out value="${prof_name}"></c:out></font></b>										
					</td>
				</tr>
		<jsp:include page="/displayresult.jsp"></jsp:include>		
		</table>

<input type="hidden" id="errormsg" name="errormsg">
	<table border='0' cellpadding='0' cellspacing='0' width='50%' align="center" >
		<!--	<tr><td id="errmsg" colspan="2">&nbsp;</td></tr>
				<tr><td><input type="hidden" id="errormsg" name="errormsg"></td></tr>
		 -->
				<% int maincount=0; %>
	
				<s:iterator  value="#session.menulist">
				
				 <s:if test=" MENUID != '30' && MENUID != '20'">
					<tr>
						<td>
				<%int maincnt=maincount++; %>
					<input type="checkbox" name="mainmenu" value="${MENUID}" id="mainmenu<%=maincnt%>" onclick="showMenu(this.id, this.value, this.checked)"/>${MENUNAME}
				</td> 
				<s:set name="mainid"> ${MENUID} </s:set>
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
							
								<tr>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="checkbox" name="mainmenu${mainid}" id="submenu${mainid}<%=submencnt++%>" value="${MENUID}" disabled="true" onclick="subMenu(this.id, this.value, this.checked,this.name)"/>${MENUNAME}	
																														
									</td>
									<td>
										<s:if test='%{flagprofile=="M"}'>
											<select name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" style="width:120px" disabled="true" onclick="subMenu(this.id, this.value, this.checked,this.name)"/>
												<option value="00">Select</option>
												<option value="${MENUID}-M">Maker</option>
												<option value="${MENUID}-C">Checker</option>
											</select>
										</s:if>
										<s:else>
											<select name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" style="width:120px" disabled="true" onclick="subMenu(this.id, this.value, this.checked,this.name)"/>
												<option selected value="00">Select</option>
												<option value="${MENUID}-M">Maker</option>
												<option value="${MENUID}-C">Checker</option>
												<option value="${MENUID}-D">Both</option>
											</select>
										</s:else>
										
									</td>
								</tr>
							</s:iterator>
					</s:if>
					<s:if test="#makerchek.mkckflag=='disabled'">
							<s:action  name="subMenuListInstAdmin" executeResult="false"  var="subMenuListInst">
									  			<s:param name="menuid" >${MENUID}</s:param>
									  			<s:param name="profilelist" >${profilelist}</s:param>
							</s:action>
							<% int submencnt=0,mkckcnt=0; %>
							<s:iterator  value="#subMenuListInst.submenulist" >
								<tr>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<!-- <input type="checkbox" name="mainmenu" id="mainmenu" fieldValue="%{MENUID}-D" disabled="true"/>${MENUNAME} -->
										<input type="checkbox" name="mainmenu${mainid}" id="submenu${mainid}<%=submencnt++%>" value="${MENUID}" disabled="true" onclick="subMenu(this.id, this.value, this.checked,this.name)"/>${MENUNAME}
										<input type="hidden" name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" value="${MENUID}-D"/>																				
									</td>	
								</tr>
							</s:iterator>
					
					</s:if>					
				</s:if>
				</s:iterator>
			
				<tr><td>&nbsp;</td></tr>
				
				<tr align="center">
						<td colspan="4"><s:submit name="submitprofile" id="submitprofile" value="Save" onclick="return validateMenu()"/> <input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/> <input type="button" class="selectallbtn" value="SelectAll" id="selectall" onclick='checkAll(this.id);'/> <input type="button" class="selectallbtn" value="De-SelectAll" id="deselectall" onclick='checkAll(this.id);'/></td>
				</tr>
	</table> 


</s:form>

</div>