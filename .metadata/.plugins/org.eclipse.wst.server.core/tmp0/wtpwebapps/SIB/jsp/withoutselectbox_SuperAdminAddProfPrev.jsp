<%@taglib uri="/struts-tags" prefix="s"%>
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

function validateMenu()
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
	  			var listboxid = "mainmenu" + c.value ;
	  			var listbox_id = document.getElementById(listboxid);
	  			if ( btnid == "selectall"){
	  				c.checked=true;
	  				c.disabled=false;
	  				if(c.checked == true)
	  				{
	  				listbox_id.disabled=false;
	  				}
	  			}else{
	  				c.checked=false;
	  				c.disabled=true;
	  				listbox_id.disabled= true;
	  				listbox_id.value="00";
	  			}
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
 </script>


<s:form name="preveledge" id="selectall" action="superSaveProfSuperAdminAddProfAction" autocomplete="off">

<div align="center">
<table><tr><td align="center"><b> Profile For <font color="red"><s:property value="#session.prev_prof_name" /> </font> </b></td></tr>
<s:hidden name="prof_id" id="prof_id" value="#session.prev_prof_id"></s:hidden>
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
	<s:iterator  value="superAddProfilePrevList">
		<tr>
			<td>
			<%int maincnt=maincount++; %>
				<input type="checkbox" name="mainmenu" value="${MENUID}" id="mainmenu<%=maincnt%>" fieldValue="%{MENUID}" onclick="showMenu(this.id, this.value, this.checked)"/>${MENUNAME}
			</td> 
			<s:set name="mainid"> ${MENUID} </s:set>
			
		</tr>
	    		
					<s:action name="superSubMenuListSuperAdminAddProfAction" executeResult="false"  var="sunny">
						<s:param name="menuid" >${MENUID}</s:param>
					</s:action>
					<% int submencnt=0; %>
					<s:iterator  value="#sunny.superAddProfSubmenuList"> 
								<tr> 
										<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="mainmenu${mainid}" id="submenu${mainid}<%=submencnt++%>" value="${MENUID}" disabled=true  onclick="subMenu(this.id, this.value, this.checked,this.name)"/>${MENUNAME}</td>
										
										<td><select name="mainmenu${mainid}Sel${MENUID}" id="mainmenu${MENUID}" style="width:120px" disabled="true" onclick="subMenu(this.id, this.value, this.checked,this.name)"/>
												<option value="00">Select</option>
												<option value="${MENUID}-M">Maker</option>
												<option value="${MENUID}-C">Checker</option>
												<option selected value="${MENUID}-D">Both</option>
											</select>										
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

