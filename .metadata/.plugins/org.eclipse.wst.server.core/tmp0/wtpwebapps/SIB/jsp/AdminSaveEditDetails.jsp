<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
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
				//alert("----selectboxid ---- "+selectboxid);
				if(submenuchecked.checked){
					submenucheck = false;
					var selectvalue = document.getElementById(selectboxid).value;	
					//alert(" selectvalue - "+selectvalue);
					if(selectvalue == "00"){						
						selectbox = true;
						break;
					}
				}
			}			
			if(submenucheck){
				submenunotchecked =true;
				break;
			}
			if(selectbox){	
				//alert("selectbox_notselet --- "+selectbox);
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
		//alert("selectbox_notselet --- "+selectbox_notselet);
		errMessage(err_msg,"Please select Maker Checker Option ");
		window.scrollTo(0,0);
		return false;
	}
}
	  
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
	 var submenvalue = mainid+menvalue;	 
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
		 }else if(document.getElementById(submenuid).checked = true){
			 document.getElementById(submenuid).checked = false;
			 document.getElementById(submenuid).disabled=true;
			 document.getElementById(selectid).disabled=true;
		 }
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
	  			}
	  		}
	  	  }		  			
}
  
function getValue(control){	
	var value1=(control.value);
	var name=(control.name);
	var ids=(control.id);
	var pad="z";
	var  newname=(pad.concat(name));
	document.preveledge.z11.value=ids;
	var c=(document.getElementById("newname").value);
}
 </script>
<%String instid = (String)session.getAttribute("Instname");%>

<s:form name="preveledge" action="saveMenuEditDetailsUserManagementAction" autocomplete="off">

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
				
				<input type="checkbox"  name="mainmenu" 
				
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
										<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="mainmenu${mainid}" 
										
										<s:iterator value="selectedmenulst">
										<s:set name="selmenuid">	<s:property /> </s:set>
										<s:if test="%{#selmenuid==#menuid}"> checked='checked' </s:if> 
										</s:iterator>
									
										id="submenu${mainid}<%=submencnt++%>" value="${MENUID}" />${MENUNAME}</td>
										<td>
												<s:action  name="makercheckerselected" executeResult="false"  var="selectd">
													<s:param name="submenu_id">${MENUID} </s:param>
												</s:action>
												
													<select name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" style="width:120px" onclick="subMenu(this.id, this.value, this.checked,this.name)">
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
										<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="mainmenu${mainid}" 
										
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
	<tr align="center"><td colspan='2'><s:submit name="submitprofile" id="submitprofile" value="Save" onclick="return validateMenu()"/></td>
	<td><input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/></td>
	<td><input type="button" class="selectallbtn" value="SelectAll" id="selectall" onclick='checkAll(this.id);'/>		
	</td>
	<td>
		<input type="button" class="selectallbtn" value="De-SelectAll" id="deselectall" onclick='checkAll(this.id);'/>
	</td>
	</tr>
</table> 
</div>

</s:form>


