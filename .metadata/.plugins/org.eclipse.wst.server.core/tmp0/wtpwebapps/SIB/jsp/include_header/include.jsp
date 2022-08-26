<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="jsp/instant/script/ordervalidation.js"></script> 


<link rel="stylesheet" type="text/css" href="js/tableview/media/css/TableTools.css">
<link rel="stylesheet" type="text/css" href="js/tableview/media/css/TableTools_JUI.css">
<script type="text/javascript" language="javascript" src="js/jquery/jquery.js"></script>
<script type="text/javascript" language="javascript" src="js/tableview/jquery_dataTables.js"> </script> 
<script type="text/javascript" language="javascript" src="js/tableview/media/js/TableTools.min.js"> </script>
<script type="text/javascript" language="javascript" src="js/tableview/media/js/ZeroClipboard.js"> </script> 
<link rel="stylesheet" type="text/css" href="js/tableview/reset-min.css">
<link rel="stylesheet" type="text/css" href="js/tableview/complete.css">	
 
		<script type="text/javascript" charset="utf-8">
			/* Formatted numbers sorting */
			$.fn.dataTableExt.oSort['formatted-num-asc'] = function(x,y){
	 			x = parseInt( x.replace(/[^\d\-\.\/]/g,'') );
	 			y = parseInt( y.replace(/[^\d\-\.\/]/g,'') );
				return x - y;
			}
			$.fn.dataTableExt.oSort['formatted-num-desc'] = function(x,y){
	 			x = parseInt( x.replace(/[^\d\-\.\/]/g,'') );
	 			y = parseInt( y.replace(/[^\d\-\.\/]/g,'') );
				return y - x;
			}
			 
		
		
			$(document).ready(function() {
				var columnSort = new Array; 
				var i=0;
				var flag =true;
				$('#example thead tr th').each(function(){
					if($(this).attr('NoSortable') == 'true') {
						columnSort.push(i);						
						flag =false;						
						//alert(columnSort);
					}
					i++;
				});
				
				
				$('#example').dataTable( {
					"sPaginationType": "full_numbers",
					 "iDisplayLength": 100,
					"aoColumnDefs": [ {
						"bSortable":flag,
						"sType": "formatted-num",
						"aTargets": columnSort} ]  

				} );
			} );
		
		 
			
		</script>
		<script type="text/javascript">

(function(){
  var bsa = document.createElement('script');
     bsa.type = 'text/javascript';
     bsa.async = true;
    
  (document.getElementsByTagName('head')[0]||document.getElementsByTagName('body')[0]).appendChild(bsa);
})();

</script>

<style>
.johntest{
	}
</style>