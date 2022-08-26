function processingincenter  () {
		
		var top, left;
		//alert("model"+$("#modal").height());
		//alert("body"+$("body").height());
		top = Math.max($("body").height() - $("#process").height(), 0) / 2;
		left = Math.max($("body").innerWidth() - $("#process").outerWidth(), 0) / 2;	
		//alert("top"+top);
		$("#process").css({
			top:top + $(window).scrollTop(), 
			left:left + $(window).scrollLeft()
		});
	}
$(document).ready(function(){
	processingincenter();
	hideprocessing();
});


function showprocessing(){	
	processingincenter();	
	$("#process").show();
	$("#process_mask").show();	
} 

function hideprocessing(){
	$("#process").hide();
	$("#process_mask").hide();
}
