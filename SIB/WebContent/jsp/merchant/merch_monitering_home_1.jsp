<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript"	src="jsp/merchant/script/merchscript.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
 <script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" href="style/tree/jquery.css" />
<link rel="stylesheet" href="style/tree/custom.css" />

<script type="text/JavaScript">
<!--
function AutoRefresh( t ) { 
	setTimeout("location.reload(true);", t);
}
//   -->
</script>


  <script>
	function showDetailedTerminal(merchantid){
		 alert(merchantid);
		var url = "showTreeViewMerchMonitor.do?MERCHANTID="+merchantid;
		if( !confirm("Continue...")){
			
			return false;
		}
		
		if( treeview != true ){
			var treeview = window.open(url,'','left=350,top=150,width=800,height=400,location=no,menubar=no,status=no,scrollbars=yes,resizable=yes,toolbar=no');
		}else{
			treeview.close();
		}
	} 
	
</script>  
 

<script>
$(document).ready(function(){
	
	$("body").css({
		 'padding-top':'0px',
	});
	
$('[rel=tooltip]').bind('click', function(ev){
	 if ($(this).hasClass('ajax')) {
			var ajax = $(this).attr('link');	
		  		
		  $.get(ajax,
		  function(theMessage){	
			 	$('<div class="tooltip">'  + theMessage + '</div>').appendTo('#tooltipcontainer');
			 	var mousex = ev.pageX; //Get X coodrinates
			    var mousey = ev.pageY; //Get Y coordinates
			    var tipWidth = $('div.tooltip').width(); //Find width of tooltip
			    var tipHeight = $('div.tooltip').height(); //Find height of tooltip
			    var tipVisX = 700 -(mousex);			   
			    var tipVisY = $("body").height()+$("body").css("top")-(mousey);
			    
			   // alert($("body").height());
			   // alert("tipWidth"+tipWidth);			    
			   // alert("tipVisY"+tipVisY);
			    //alert("tipHeight"+tipHeight);
			  //  $("#test").html("body height"+$("body").height()+" body width"+$("body").width()+"<br/>tipVisY"+tipVisY+" < tipHeight"+tipHeight+"<br/> tipVisX"+tipVisX+" < tipWidth"+tipWidth);
			    
			    if ( tipVisX < tipWidth ) {			    	
			        mousex = ev.pageX-tipWidth;
			        if(mousex<0)
			        	mousex=0;
			    } if ( tipVisY < tipHeight ) {			    	
			        mousey = ev.pageY-tipHeight;
			        if(mousey<0)
			        	mousey=0;	
			    }		
				
				$('div.tooltip').css({
					'top': mousey+"px",
					 'left': mousex+"px",
					 'display':'block'
					 });
				});
		  
		   $('div.tooltip').fadeIn('fast');
		  }	
 
}).bind('mouseout', function(){
		$('div.tooltip').fadeOut('fast', function(){
			$(this).remove();
		});

		$('div.tooltip').bind('mouseenter', function(){
			$(this).stop().fadeOut();
			$(this).stop().fadeIn();
		});

		$('div.tooltip').bind('mouseleave', function(){
			//alert("dsds"+$('div.tooltipbox').data('fading'));
			if(!$('div.tooltipbox').is(':visible')){
				$('div.tooltip').fadeOut('fast', function(){
					$(this).remove();
				});						
			}
			
		});

	});
	
	

});

</script>

<style>
 
.tooltip{
	position:absolute;	
	background-color:#ffffff;
	background-position:left center;
	color:#000000;
	padding:0px;
	font-size:12px;
	border:2px solid gray ;
	font-family:Verdana, Geneva, sans-serif; 
    display:none;
	} 
 .merchantbox{
 	width:200px;
 	height:20px; 
 	
 }
	td{
		padding:0;margin:0;
	}

 .jOrgChartbox .merchantbox {
	font-size 			: 14px; 
	border-radius 		: 8px;
	border 				: 5px solid white; 
	-moz-border-radius 	: 8px; 
	 display               : inline-block;
	 width                 : 166px;
	 height                : 166px;  
	 border:1px solid #ededed;
	 color:#000;
	 text-align:justify;
	 
}
 
	
	
</style>
 
 


<jsp:include page="/displayresult.jsp"></jsp:include>

<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>

 



<body onload="JavaScript:AutoRefresh(1000*60*10);">

	<s:form action="#" name="custinfoform"
		id="custinfoform" autocomplete="off" namespace="/">

		<table border="1" cellpadding="0" cellspacing="0" width="95%" id="tooltipcontainer"	style='border: 1px solid #efefef' align="center" class="filtertabnew">
			<% int i=0,k=0,no_cols = 7;	%> 
			 <tr> <td> Total Merchants : <s:property value="merchantlist.size()"/> </td>
			 	<td> Warning Merchant count :  <span id='warningblock'> -- </span></td> 
			  </tr>
			 <s:iterator value="merchantlist">
			 <% String title = "Click here to view detail"; %>
				<% if(i==0) { %>
				<tr>	
				<% } %>
			
					 		
					<s:if test="%{MERACHANTISSUE==0}">
						<td class="jOrgChartbox"  >  
						<% String bgstyle = "", imagename = "";%>	
						<% bgstyle="inactive";  title = "TERMINAL OUT OF ORDER..CLICK TO VIEW DETAILS"; 
						   imagename = "images/merchant/inactive_merchant.png";
						%> 
					
					 					 <a  class="ajax" style="cursor:pointer" alt="Merchant" title="<%=title%>" rel="tooltip" link="showTreeViewMerchMonitor.do?MERCHANTID=<s:property value="MERCH_ID"/>">
					 					 	<div <%=bgstyle%>" style="text-align:center" > 
					 					 	<img src="<%=imagename%>" width="50"/>
					 						<br/>	<span style='color:gray;font-weight:bold'> <s:property value="MERCH_NAME"/></span>
					 						 </div> 
					 					</a> 
					 	 </td>
				<% if(i==no_cols)	{
						i=0;	
					%>
					</tr>
					<%	} else	{
						i=i+1;
					} %>
				</s:if>  		
			 </s:iterator>
		
		
		<!-- ////////////// second block  -->
		 	<% int warningmerchcount = 0; %>
			 <s:iterator value="merchantlist">
			 <% String title = "Click here to view detail"; %>
				<% if(i==0) { %>
				<tr>	
				<% } %>
			
					 		
					<s:if test="%{ (MERACHANTISSUE==2 || MERACHANTISSUE==3 ) || MERACHANTISSUE==5}">
						<td class="jOrgChartbox"  >  
						<% String bgstyle = "", imagename = "";%>	
						<% bgstyle="inactive";  title = "TERMINAL OUT OF ORDER..CLICK TO VIEW DETAILS"; 
						imagename = "images/merchant/warning_merchant.png";
						%> 
									
					 					 <a  class="ajax" style="cursor:pointer" alt="Merchant" title="<%=title%>" rel="tooltip" link="showTreeViewMerchMonitor.do?MERCHANTID=<s:property value="MERCH_ID"/>">
					 					 	<div <%=bgstyle%>"  style="text-align:center" > 
					 					 	<img src="<%=imagename%>" width="50"/>
					 						<br/>	<span style='color:gray;font-weight:bold'> <s:property value="MERCH_NAME"/></span>
					 						 </div> 
					 					</a> 
					 	 </td>
				<% if(i==no_cols)	{
						i=0;	
					%>
					</tr>
					<%	} else	{
						i=i+1;
					} %>
					
					<% warningmerchcount++; %>
				</s:if>  		
			 </s:iterator>
		 	 
		 
		     <s:iterator value="merchantlist">
			 <% String title = "Click here to view detail"; %>
				<% if(i==0) { %>
				<tr>	
				<% } %>
			
					 		
					<s:if test="%{ (MERACHANTISSUE==1}">
						<td class="jOrgChartbox"  >  
						<% String bgstyle = "", imagename = "";%>	
						<% bgstyle="inactive";  title = "TERMINAL OUT OF ORDER..CLICK TO VIEW DETAILS"; 
						imagename ="images/merchant/active_merchant.png";
						%> 
								 
					 					 <a  class="ajax" style="cursor:pointer" alt="Merchant" title="<%=title%>" rel="tooltip" link="showTreeViewMerchMonitor.do?MERCHANTID=<s:property value="MERCH_ID"/>">
					 					 	<div <%=bgstyle%>"  > 
					 					 	<img src="<%=imagename%>" width="50"/>
					 						<br/>	<span style='color:gray;font-weight:bold'> <s:property value="MERCH_NAME"/></span>
					 						 </div> 
					 					</a> 
					 	 </td>
				<% if(i==no_cols)	{
						i=0;	
					%>
					</tr>
					<%	} else	{
						i=i+1;
					} %>
				</s:if>  		
			 </s:iterator>
		</tr>
		</table> 
	</s:form>
	
	<div id="test"> </div>

</body>
 