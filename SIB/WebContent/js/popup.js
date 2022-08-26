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
	method.open = function (settings,findval,ajaxurl) {
		 //alert("Setting ==> "+settings);
		$content.empty().append(settings.content);
		//alert("findVALUE === "+findval);
		//alert("Content ===> ===> "+settings.content);
		//alert("After Append Content is ===> "+$content);
		$.ajax({
			url:ajaxurl,
			type: 'GET',
			data: 'subcode='+findval
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
						
					}
				  )
		.error(function(){						
			$("#content").html("Error While Get Data") ;
		})
		.complete(function()     { /*alert("complete");*/ })
		;
	
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

// Add in html body  
//<a  href="javascript:void(0);" id="viewinfo" link="showdetaildiscountMerchantProcess.do" findby="${DISC_SUBCODE}">${SUB_DISC_DESC}</a>