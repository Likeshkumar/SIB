<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

 

<link rel="stylesheet" type="text/css" href="style/calendar.css" />

<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery/jquery_1.9.1.js"></script>
 
 

<style>
table.formtabletop span  { 
	font-weight:bold;
	display:block;
	font-size:15px;
}
span.inactive1 {
	color: red;
}
span.active1 {
	color: green;
}
span.red{
	color:red;
}
span.green{	color:green;}
span.imageblock{ display:block;} 
.scroll{overflow:scroll;width:500px;height:500px;background:black}
.child{width:1000px;height:1000px;background:yellow}
 
 .tooltipbox{
	position:absolute;	
	background-color:#efefef;
	color:#000;
	font-size:15px;
	font-weight:bold;
	border:2px solid #81812A;
	font-family:Verdana, Geneva, sans-serif;
	width:300px;
	display:inline;
	padding:10px;
	z-index:20000;
}

</style>

 <script type='text/javascript'>//<![CDATA[ 
$(window).load(function(){
	
	
	
	var scrollinterval = document.getElementById("scrollinterval").value;
	var scrollval = scrollinterval*1000;
	
	var autscrollreq = document.getElementById("autoscrollreq");
	//alert("scrolval : "+ autscrollreq.value  + " Speed : " + scrollval );
	if( autscrollreq.value=="Y" ){  
		scrollDown($("html,body")); 
	}

	function scrollDown(el) {  
	    el.animate({
	        scrollTop: el[0].scrollHeight
	    }, scrollval, function() {
	        scrollUp(el);
	    });
	};

	function scrollUp(el) {
	    el.animate({
	        scrollTop: 0
	    }, scrollval, function() {
	        scrollDown(el);
	    });
	};
	

});//]]>  

</script>
 
<script type='text/javascript'> 

 
 

$(document).ready(function(){
	
		var refreshinterval = document.getElementById("refreshinterval").value;
		var refreshval = refreshinterval*1000*60; 
		setInterval(function(){cache_clear()},refreshval);
});
	

	function cache_clear()	{
	 	window.location.reload(true);
	}

	 
	 
	 $(document).ready(function(){
			var mousex ,mousey;
			$('[rel=texttooltip]').bind('mousedown', function(ev){
				var shorttext=$(this).attr('alt');
				$('<div class="tooltipbox">'+shorttext+'</div>').appendTo('body');
				var mousex = ev.pageX+10 ; //Get X coodrinates
			    var mousey = ev.pageY+10; //Get Y coordinates
			    var tipWidth = $('div.tooltipbox').width(); //Find width of tooltip
			    var tipHeight = $('div.tooltipbox').height(); //Find height of tooltip
			    var tipVisX = $(window).width()-(mousex);
			    var tipVisY = $(window).height()-(mousey+tipHeight);
			    if ( tipVisX < tipWidth ) {
			        mousex = ev.pageX-tipWidth-50;
			    } if ( tipVisY < tipHeight ) {
			        mousey = ev.pageY-tipHeight-50;
			    }
				$('div.tooltipbox').addClass("tooltipbox");
				$('div.tooltipbox').css({
					'top': mousey +"px",
					'left': mousex +"px"
				});
				$('div.tooltipbox').fadeIn('fast');
				}).bind('mouseout', function(){
					 $('div.tooltipbox').fadeOut('fast', function(){
						 $(this).remove();
					 });
					 
					 $(this).bind('mousemove', function(bn){
						// alert("move");
						 mousey =bn.pageY- 100;
						 mousex = bn.pageX+10;
					  
						$('div.tooltipbox').addClass("tooltipbox");
						$('div.tooltipbox').css({
							'top': mousey +"px",
							'left': mousex +"px"
						});
					 });
					 
			});
		});
</script>

<jsp:include page="/displayresult.jsp"></jsp:include>
<body>

<s:form action="viewTerminalListMerchMoniter.do" name="orderform" onsubmit="return validateRecords()" autocomplete="off" namespace="/">
	
	<s:hidden name="scrollinterval" id="scrollinterval" value="%{monitorbean.scrollinterval}" />
	
	<s:hidden name="refreshinterval" id="refreshinterval" value="%{monitorbean.refreshinterval}" />
	
	<s:hidden name="autoscrollreq" id="autoscrollreq" value="%{monitorbean.scrollrequired}" />
	
	
	<table border='0' cellpadding='0' cellspacing='0' width="99%" style="text-align:center;" class="formtabletop">
		<tr>
			<td> <span class="total"> Total Terminal : <s:property value="monitorbean.totalterminalcount"/> </span>   &nbsp;		</td>
			<td><span class="inactive1"> Total Inactive : <s:property value="monitorbean.totalinactivecount"/> </span>   &nbsp;</td> 
			<td><span class="active1"> Total Active : <s:property value="monitorbean.totalactivecount"/> </span>   &nbsp;</td>  
		</tr>
		
		<tr>
			<td><span class="active1"> Today Transaction Count : <s:property value="monitorbean.totaltodaytxncount"/> </span>   &nbsp;</td>
			<td> <span class="active1"> Total Transaction Count : <s:property value="monitorbean.totaltxncount"/> </span>   &nbsp;		</td>
			<td> &nbsp; </td>  
		</tr>
		
		
		<tr>
		<td colspan="3" style="text-align:left"> <span class="total"> <img src="images/merchant/pos_red.png" style='width:20px'/>  <s:property value="monitorbean.inactivedesc"/> </span> </td>
		 
		</tr>
	</table> 
	<div id="MyDivName" >
	<table border="0" cellpadding="0" cellspacing="0" width="99%" class="formtable"	align="center">
		
		<% int i=0,k=0,no_cols = 7;
			String terminladetails="";
		%> 
			 <tr> 
			  
		 <s:iterator value="monitorbean.terminallistbean">
		  <% String title = "Click here to view detail"; %>
				<% if(i==0) { %>
				<tr>	
				<% } %>
				
				
		 
				 
					<td class="node drop-hover"  alt="${STATUSMESSAGE}"	 rel="texttooltip"
						<% if ( i % 2 == 1) {  %> style='background:#F0F0F0' <% }%>
					 > 
						
							<span  
								<s:if test="TODAYTXNCNT==0">
									class="red";
								</s:if>
								<s:else>
								class="green";
								</s:else>
							>
								Today Txn Count : ${TODAYTXNCNT}
							 </span>
						
						
						<span style='display:block'> Total Txn Count : ${TOTALTXNCNT} </span>
						
						<span class="imageblock"> 
								<s:if test="%{STATUS!=0}"> <img src="images/merchant/pos_red.png" style='width:30px'/> </s:if> 
								<s:if test="%{STATUS==0}"> <img src="images/merchant/pos_green.png" style='width:30px'/> </s:if> 
						</span>
					<br/>  <span>${MERCHANTDESC}  - <small> ${TERMINAL_ID}</small></span>  
					<%-- <br/>  <span>${STOREDESC}</span>  
					<br/>  <span>${TERMINALDESC}</span>  
					 --%>
					</td> 
					
					
					 
					
				
			 <% if(i==no_cols)	{
						i=0;	
					%>
					</tr>
					<%	} else	{
						i=i+1;
					} %>
				 	
		 </s:iterator> 
	</table>  
	 
</div>
</s:form>

</body>