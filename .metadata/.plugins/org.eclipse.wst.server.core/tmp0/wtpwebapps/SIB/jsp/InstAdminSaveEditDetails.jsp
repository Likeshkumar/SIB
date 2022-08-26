<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
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
		 }else if(menchecked == false){
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
			}if(submenucheck){
				submenunotchecked =true;
				break;
			}if(selectbox){
				selectbox_notselet = true;
				break;
			}
		}
	}if(menuselect != 0 && menuselect<=mainmenus.length){
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


<div align="center">

	<s:form name="preveledge" action="updateProfprev" autocomplete="off">
		<table>
			<tr>
				<td align="center">
					<b>Menu Privilege Configuration</b>
				</td>
			</tr>
			<tr>
				<td>
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
									<input type="checkbox" name="mainmenu" 
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
												<input type="checkbox" name="mainmenu${mainid}" 
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
										<s:if test='%{flagprofile=="M"}'>
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
											</select>
										</s:if>
										<s:else>
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
										</s:else>
												
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
				<tr><td>&nbsp;</td></tr>
				<tr align="center">
					<td colspan="4">
						<s:submit name="submitprofile" id="submitprofile" value="Save" onclick="return validateMenu()"/> <input type="button" name="cancel" id="cancel" class="cancelbtn" value="Cancel" onclick="return confirmCancel()"/> <input type="button" class="selectallbtn" value="SelectAll" id="selectall" onclick='checkAll(this.id);'/> <input type="button" class="selectallbtn" value="De-SelectAll" id="deselectall" onclick='checkAll(this.id);'/>
					</td>
				</tr>
			</table> 
	</s:form>
</div>