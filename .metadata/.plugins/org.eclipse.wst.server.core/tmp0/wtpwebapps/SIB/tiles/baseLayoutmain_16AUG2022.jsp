<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@ page import="sun.misc.BASE64Decoder" %>
<%@taglib uri="/struts-tags" prefix="s" %>
 
<html>
<head>
  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-control" content="no-cache"/>
<meta http-equiv="Cache-control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="js/jquery/base64.js"></script>  
 <LINK REL="SHORTCUT ICON" HREF="images/headerlogo_MQH_icon.ico">
   
<% 
int applying_width = 900; 
int applying_height = 350;
//out.println( "ifp__" + request.getParameter("ifp")  );
if( request.getParameter("ifp") == null ) {// out.println( "null true ");  %>

	<script language="Javascript" type="text/javascript">
	$(document).ready(function(){
		$("ul.dropdown ul").mouseover(function(){
			  var visiblewidth = $('body').innerWidth();
			  var submenuleft= $(this).find("ul").offset();			
  			  var submenuwidth= $(this).find("ul").width();			
			  var newsubmenupos=submenuleft.left+submenuwidth;			  
			  if (newsubmenupos>visiblewidth)
							$(this).find("ul").css({"left":"-99%"});			
		});
	});
</script>
<script language="javascript" type="text/javascript">  
	var scr_width = encode64(screen.width);
	var scr_height = encode64(screen.height); 
	var width = encode64("width");
	var height= encode64("height");
	 
	var url = "instUserHomeAction.do?ifp="+width+"/"+scr_width+"/"+height+"/"+scr_height;
	//var url = "instUserHomeAction.do?john=";
	 //alert(url);
	window.location = url;

</script>
<% } else {

	try{
	response.setHeader( "Pragma", "no-cache" );
  	response.setHeader( "Cache-Control", "no-cache" );
  	response.setDateHeader( "Expires", 0 ); 
  
 	
   
	BASE64Decoder decoder = new BASE64Decoder();
   
	String urlparam =  request.getParameter("ifp");
	String[] params = urlparam.split("/"); 
	String txt_width = params[0];
	String width_val = params[1];
	String txt_height=params[2];
	String height_val = params[3]; 
  
	// out.println("urlparam_ " + urlparam +"<br/>");
	 
	 byte[] widthtxt = decoder.decodeBuffer(txt_width); 
	 String caption_width = new String(widthtxt, "UTF-8");  
	 
	 byte[] height_txt = decoder.decodeBuffer(txt_height); 
	 String caption_height = new String(height_txt, "UTF-8");  
	 
	 
	 
	 byte[] widthval = decoder.decodeBuffer(width_val); 
	 String scr_width = new String(widthval, "UTF-8");  
	 applying_width = Integer.parseInt(scr_width)-40;
	 
	 byte[] hieghtval = decoder.decodeBuffer(height_val);
	 String scr_height = new String(hieghtval, "UTF-8");  
	 applying_height = Integer.parseInt(scr_height)-350;
	 
	 // out.println("screen width is " + applying_width);
	}catch(Exception e ){
		out.println("Exception " + e );
		//session.invalidate();
	%>	<script>
			var url = "logoutAction.do?msg=invalid url modification";
			window.location=url;
		</script>
	<%
	}
   } 

//out.println( "applying_height__" + applying_height );
%>

 
 
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <!-- <title>IFP:<tiles:insertAttribute name="title" ignore="true" /> </title> -->
            <!-- <title>UTBL Bank</title> -->
            <title>RCB Bank</title> 
            <link href="style/ifps.css" rel="stylesheet" type="text/css" />
            <link href="style/dropdown.css" rel="stylesheet" type="text/css">
            <link href="style/default.css" rel="stylesheet" type="text/css"> 
			<link rel="stylesheet" type="text/css" href="style/jquery_css.css">
			<script src="js/jquery/jquery.js"  type="text/javascript"  ></script>
			<script src="js/jquery/jquery-ui.js"  type="text/javascript"  ></script>
			<script src="js/processing.js"></script>
			<link href="style/processing.css" rel="stylesheet" type="text/css">
<script type="text/javascript" language="javascript">
 
 var isClose = false;
 //this code will handle the F5 or Ctrl+F5 key
 //need to handle more cases like ctrl+R whose codes are not listed here

 document.onkeydown = checkKeycode();
 function checkKeycode(e) 
 {	
	 var keycode;
	 if (window.event)
	 	{
		 keycode = window.event.keyCode;
		 alert("Key pressed");
	 	 alert(keycode);
	 	}
	 else if (e)
	 	keycode = e.which;
	 if(keycode == 116)
	 {
		 alert("Key pressed");
	 	isClose = true;
	 } 
	 
 }
 
 function somefunction()
 {
 	isClose = true;
 }
 
 function doUnload()
 {
 	if(!isClose)
	 {
	 	alert('window is closing');
	 }
 }
 
 function noBack() { 
		
		window.history.forward(); 
}

 function center  () {
		
		var top, left;
		//alert("model"+$("#modal").height());
		//alert("body"+$("body").height());
		top = Math.max($("body").height() - $("#modal").height(), 0) / 2;
		left = Math.max($("body").innerWidth() - $("#modal").outerWidth(), 0) / 2;	
		//alert("top"+top);
		$("#modal").css({
			top:top + $(window).scrollTop(), 
			left:left + $(window).scrollLeft()
		});
		
		$("#popdivpos").css("padding-bottom","0px");
		
	}
 
 $(document).ready(function(){
 	$( "#modal").draggable();
 	$( "#modal").resizable();
 	$("#close").click(function(e){
		e.preventDefault();
		$("#modal").hide();
		$("#overlay").hide();
		$("#content").empty();	
		
		 $("#model").load(function(){
			    alert("Image loaded.");
			  });
		
	});
 }); 
 //var allvalsarry = [];
var allvalsarry=[];
var mailtoarryindex;
var loadintextarea;
function updateTextArea() { 
	//alert("index "+ mailtoarryindex);
	$('#content :checked').each(function(i) {	
		//alert("total array"+allvalsarry);
		allvalsarry[mailtoarryindex].push((i!=0?"":"")+ $(this).val());
   });
	//alert("index value"+mailtoarryindex);
   //alert("load textarea"+loadintextarea);
   //alert("array values "+ allvalsarry[mailtoarryindex]);
   window.frames['mainframe'].document.getElementById(loadintextarea).value=allvalsarry[mailtoarryindex];   
   	$("#modal").hide();
	$("#overlay").hide();
	$("#content").empty();	
		 // $('#video0_tags').val(allVals).attr('rows',allVals.length) ;
}

$(function() {
      $('#content input').click(updateTextArea);
      updateTextArea();
});
</script> 
  <!-- 
   <SCRIPT language="JavaScript"> 
		 BEGIN HIDING
		var browserName=navigator.appName;
		var browserVer=parseInt(navigator.appVersion);
		if ((browserName=="Netscape" && browserVer>=3) || (browserName=="Microsoft Internet Explorer" && browserVer>=4)) version="n3";
		else version="n2"; if (version=="n3") alert("Your browser passes the test");
		else  window.location="logoutAction.do"; 
		 
	</SCRIPT>

	-->
	
<style type="text/css">

table.globaltable{
 	margin: 0 auto; 
	width :<%=applying_width%>px; 
}
#modal:hover{
	cursor:move;
}
</style>
 </head>
 
 
 <%
String apptype =  (String)session.getAttribute("APPLICATIONTYPE"); 
String bg = "";
if(apptype.equals("PREPAID")){
	bg = "images/header_pixel.jpg";
}else if( apptype.equals("MERCHANT")){
	bg = "images/merch_onepix.gif";
}else if( apptype.equals("CREDIT")){
	bg = "images/credit_pixel.jpg";
}else if( apptype.equals("BOTH")){
	bg = "images/header_pixel.jpg";
}else{
	bg = "images/one-pix-bic.jpg";
}


%>


 <body class="background" onload="noBack()">  
 <!--  <body onbeforeunload="doUnload();" onmousedown="somefunction();"> prito -->
 
<!-- <body onbeforeunload="ConfirmClose();" onunload="HandleOnClose();" >  -->
            <table border="0" cellpadding="0" cellspacing="0" align="center" width="100%" class="globaltable" id="globaltable">
        	
        
            <tr>
                <td style="background:url(<%=bg%>)" align="center">
                  <tiles:insertAttribute name="header" />
                </td>
            </tr>
           <tr>
                <td>
                	<div class="top-bar">
                		<div class="logout">
                    	<tiles:insertAttribute name="top" />
                    	</div>
                    </div>
                </td>
            </tr>
            
            <tr>
                <td>
                    <tiles:insertAttribute name="menu" />  
                </td>
            </tr>
            
            <tr>
        		<td class="formtitle" id="title"> 
        		 <!-- <tiles:insertAttribute name="title" ignore="true" />  -->	
        		</td>
        	</tr>
            
            <tr>
               <td id="popdivpos">
               	<div id="overlay" class="overlay" style="display:none"></div>
				<div id="modal" style="display:none">
					<a id="close" href="#">close</a>
					<div id="content">text cin</div>
				</div>
				<div id="process_mask" class="mask"></div>
				<div id="process">
					<div id="processimg"><img src="images/loading/Processing.gif" width="250"></div>
				</div>
				<div id="clear"></div>
					<table border='0' cellpadding='0' cellspacing='0' width='100%'>
					<tr>
						<td><img src="images/curve_images/cur_top_lef.jpg" ></td>
						<td width="100%" background="images/curve_images/cur_top_bg.jpg"><img src="images/spacer.gif" width="1" height="29"> 
								</td>
						<td><img src="images/curve_images/cur_top_rig.jpg" ></td>
				   </tr>
					 
					<tr>
					    <td background="images/curve_images/cur_lef_bg.jpg"><img src="images/spacer.gif" width="33" height="1"></td>
					    <td style="height:<%=applying_height%>px;">
						 
						 
								<iframe onload="hideprocessing()"
							name='mainframe' 
							id='mainframe' 
							width='100%' 
							src='' 
							frameborder=0 
							scrolling='auto' 
							style="  
									top:20%; 
									left:1%; 
									height:<%=applying_height-10%>px;
									position:inherit; 
									z-index:0;
									overflow:auto
									">
						</iframe>	
					 				
					   </td>
					    <td background="images/curve_images/cur_rig_bg.jpg"><img src="images/spacer.gif" width="38" height="1"></td>
					  </tr>
					  
					  <tr>
						<td><img src="images/curve_images/cur_bot_lef.jpg" ></td>
						<td background="images/curve_images/cur_bot_bg.jpg"><img src="images/spacer.gif" width="1" height="29"></td>
						<td><img src="images/curve_images/cur_bot_rig.jpg" ></td>
					  </tr> 
								
					 </table> 
			</td>
            </tr>
            <!-- <tr>
                <td>
                	 
                    <tiles:insertAttribute name="footer" />
                     
                </td>
            </tr> -->
        </table>
    </body>
</html>
