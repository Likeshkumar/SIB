$(document).ready(function(){
		$(".mutliSelect").find("li").each(function(){ 

			$(this).find("input").bind("click",function() {
					// alert($(this).is(":checked"));
					if($(this).is(":checked"))
						$(this).prop("checked",false);
					else 
						$(this).prop("checked",true);
				});

			$(this).bind("click", function() {
				var selectval="";	
				var unselected="";				 
				//alert($(this).find("input").is(":checked"));
				if(($(this).find("input").is(":checked"))){					
					unselected=$(this).find("input").next("label").text().replace(/ /g,"");
					$(this).find("input").prop("checked",false);
					var textval = $(this).parent().parent().prev().find("p").text().replace(/\s/g,"");
					if(textval.contains(unselected+",")){
						replacestr = textval.replace(unselected+",","");
					}
					else
					{
						replacestr = textval.replace(","+unselected,"");
					}
					$(this).parent().parent().prev().find("p").text(replacestr);

				}
				else{
					$(this).find("input").prop("checked",true);
					selectval =$(this).parent().parent().prev().find("p").text();				
					if(selectval=="Select")
						selectval="";
					else
						selectval=selectval+",";
					insertval = $(this).find("input").next("label").text().replace(/ /g,'&nbsp;');
					$(this).parent().parent().prev().find("p").html(selectval+insertval);
				}
				if(!$(this).parent().find('input[type="checkbox"]').is(':checked')){					
					$(this).parent().parent().prev().find("p").html("Select");
				}
						
			});
		});
		$(".mutliSelect").bind("mouseleave",function(){
			$(this).css("display","none");
		});
		
	});
	function sildedown(a){
		if($(a).next(".mutliSelect").is(":visible")){
			$(a).next(".mutliSelect").css("display","none");
		} else {
			$(a).next(".mutliSelect").css("display","block");
		}
	}