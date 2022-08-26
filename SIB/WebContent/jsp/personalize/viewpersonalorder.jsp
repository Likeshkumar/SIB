<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@taglib uri="/struts-tags" prefix="s"%> 

<html>
<head>
<script src="js/jquery/jquery.js"></script>
<!--<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>-->

	<link rel="stylesheet" type="text/css" href="style/jquery_css.css">
		<style>
			* {
				margin:0; 
				padding:0;
			}
			
		</style>
<script type="text/javascript">

var modal = (function(){
	var 
	method = {},
	$overlay,
	$modal,
	$content,
	$close;

	
	
	var modaldiv = parent.document.getElementById("modal");
	// Center the modal in the viewport
	

	// Open the modal
	method.open = function (settings) {
		//alert("Setting ==> "+settings);
		$content.empty().append(settings.content);
		//alert("Content ===> ===> "+settings.content);
		//alert("After Append Content is ===> "+$content);
		$.ajax({
			url: 'getCardorderdetailsPersonalCardOrderAction.do',
			type: 'GET',
			data: 'orderrefnum='+settings.content
		})
		.success (
					function(response) 
					{
						parent.document.getElementById("content").innerHTML=response;
						var s= 'parent.center();';
						addCode(s);						
						$(window).bind('resize.modal', method.center);									
						parent.document.getElementById("modal").style.display="block";
						parent.document.getElementById("overlay").style.display="block";
						parent.center();
					}
				  )
		.error   (function()     {						
			$("#content").html("Error While Get Data") ;
		})
		.complete(function()     { /*alert("complete");*/ })
		;
				
		
		//var width=settings.width || 'auto'; 
		//var height=settings.height || 'auto';
		//modaldiv.style.width=width;
		//	modaldiv.style.height=height;
		/* $modal.css({
			width: settings.width || 'auto', 
			height: settings.height || 'auto'						
		});*/
		
		//parent.center();
		
	};

	// Close the modal
	method.close = function () {
		//$(span).parents()
		// $("#modal").hide();
		parent.document.getElementById("modal").style.display="none";
		parent.document.getElementById("overlay").style.display="none";
		parent.document.getElementById("content").innerHTML="";					
		$(window).unbind('resize.modal');
	};

	// Generate the HTML and add it to the document
	
	
	$overlay = $('<div id="overlay"></div>');
	$modal = $('<div id="modal"></div>');
	$content = $('<div id="content"></div>');
	$close = $('<a id="close" href="#">close</a>');
	
	parent.document.getElementById("modal").style.display="none";
	parent.document.getElementById("overlay").style.display="none";					
	
	$modal.append($content, $close);

	$(document).ready(function(){
		//$('body').append($overlay, $modal);						
	});
	return method;
}());

function addCode(code){
    var JS= document.createElement('script');
    JS.text= code;
    parent.document.getElementById("content").appendChild(JS);
}
// Wait until the DOM has loaded before querying the document
$(document).ready(function(){

	$('a#viewinfo').click(function(e){
	
		modal.open({content: 
			$(this).text() 
		});
		
		e.preventDefault();
		$f=$(this).parents("#popdivpos").attr("id");
		
		$(this).parents('#modal').toggle(false);		
		//$( "#modal",document).resizable();					
	});
});
</script>
	</head>

<jsp:include page="/displayresult.jsp"></jsp:include>
<table width="95%" cellpadding="0"  border="0"  cellspacing="0"  class="formtable">
		<tr>
			<th> Sl no </th>
			<th> Order Ref No </th>
			<th>No.of Cards</th>
			<th> Emb Name </th>
			<th> Product Name </th>
			<th> Ordered Date </th>
			<th> Ordered By </th>
			<th> Ordered Status </th>
			<th> Remarks </th>
		</tr>
		<% int rowcnt = 0; Boolean alt=true; %> 
		<s:iterator value="personalorderlist">
			<tr
			<% if (alt) { out.println( "class='alt'" );alt=false;}else{ alt=true;}  int rowcount = ++rowcnt; %>
			>
				<td> <%= rowcount %> </td>
				<td> <a id='viewinfo' href='#'>${ORDER_REF_NO}</a> </td>			
				<td> ${CARD_QUANTITY}</td>
				<td> ${EMBOSSING_NAME} </td>
				<td> ${ PRODBINDESC }  </td>
				<td> ${ ORDERED_DATE }</td> 
			    <td> ${ USERNAME }</td>
			     <td> ${ STATUS }</td>  
			    <td> ${ REMARKS }</td>  
			</tr> 
	 	</s:iterator>		
	</table>
</body>
</html>