<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html><head><meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>Terminal list </title>
    
<link rel="stylesheet" href="style/tree/jquery.css">
 <link rel="stylesheet" href="style/tree/custom.css">
<script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>

<script>
$(document).ready(function(){
	var mousex ,mousey;
	$('[rel=texttooltip]').bind('mouseover', function(ev){
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
<style>
.tooltipbox{
	position:absolute;	
	background-color:#FDFF66;
	color:#81812A;
	font-size:12px;
	border:2px solid #81812A;
	font-family:Verdana, Geneva, sans-serif;
	width:200px;
	display:inline;
	padding:10px;
	z-index:10000;
}
</style>
     
    <!-- jQuery includes -->
     

   
  </head>
<%
	String instid = (String)session.getAttribute("Instname");
	String branch = (String)session.getAttribute("BRANCHCODE");
	String usertype = (String)session.getAttribute("USERTYPE");
	String branchname = (String)session.getAttribute("BRANCH_DESC");
	 
%>

  <body> 
    
   <s:if test="storeavailable">
    <div id="chart" class="orgChart"><div class="jOrgChart"><table border="0" cellpadding="0" cellspacing="0"> 
    
	<tr class="node-cells expanded"><td colspan="10" class="node-cell"><div style="cursor: n-resize;" class="node  drop-hover">
       
       	<s:property value="merchantdescbean"/>
       
     </div></td></tr>
     
    
     <tr style=""><td colspan="10"><div class="line down"></div></td></tr> 
     
     <tr style="">  
     	 
     	
	   <s:iterator status="status" value='storelistbean'>   
	   		 <s:if test="%{#status.count==storelistcnt &&  #status.count ==1 }"> 
    		  		 
    		 </s:if>  
    		 <s:else>
	    		  <s:if test="%{#status.count==1}"> 
	    		  		<td class="line left">&nbsp;</td>
						<td class="line right  top">&nbsp;</td>   
	    		   </s:if> 
	    		  <s:elseif test="%{#status.count !=1  }"> 
	    		  	<s:if test="#status.count !=storelistcnt">
	    		  		<td class="line  top">&nbsp;</td>
						<td class="line right top">&nbsp;</td>  
	    		  	</s:if>  
	    		  </s:elseif> 
	    		  <s:if test="%{#status.count==storelistcnt &&  #status.count !=1 }"> 
	    		  		<td class="line  top">&nbsp;</td>
						<td class="line right  ">&nbsp;</td> 
	    		   </s:if>  
    		  </s:else>
   		</s:iterator>
	 </tr> 
	 
	 <tr style="">
	 	<s:iterator value="storelistbean">
	 			<td colspan="2" class="node-container">
					<table border="0" cellpadding="0" cellspacing="0">
					<tr class="node-cells expanded"><td colspan="10" class="node-cell"><div   class="node drop-hover">
      
       						<s:property value="STORE_NAME"/>
   					 	 </div></td></tr>
					 		
					 	<s:bean name="com.ifp.merchant.MerchMoniter" var="resd1" >
									<s:action  name="getTerminalForStoreMerchMonitor.do" executeResult="false"  var="terminalbean">  
											<s:param name="instid"><%=instid%></s:param>
								  			<s:param name="storeid" >${STORE_ID}</s:param> 								  			
				  				    </s:action>
				  				
						
								
						 
								<tr style=""><td colspan="10"><div class="line down"></div></td></tr>  
							     <tr style="">   
							      
								   <s:iterator status="status" value='#terminalbean.allterminallistbean'>    
								   
								        <s:if test="%{#status.count==#terminalbean.terminalcnt &&  #status.count ==1 }"> 
							    		  		 
							    		 </s:if>  
							    		 <s:else>
								    		  <s:if test="%{#status.count==1}"> 
								    		  		<td class="line left">&nbsp;</td>
													<td class="line right  top">&nbsp;</td>   
								    		   </s:if> 
								    		  <s:elseif test="%{#status.count !=1  }"> 
								    		  	<s:if test="#status.count !=#terminalbean.terminalcnt">
								    		  		<td class="line  top">&nbsp;</td>
													<td class="line right top">&nbsp;</td>  
								    		  	</s:if>  
								    		  </s:elseif> 
								    		  <s:if test="%{#status.count==#terminalbean.terminalcnt &&  #status.count !=1 }"> 
								    		  		<td class="line  top">&nbsp;</td>
													<td class="line right  ">&nbsp;</td> 
								    		   </s:if>  
							    		  </s:else>
							   		</s:iterator>
								 </tr> 
						
								 <tr style="">
	 									<s:iterator value="#terminalbean.allterminallistbean">
	 										<td colspan="2" class="node-container">
												<table border="0" cellpadding="0" cellspacing="0">
													
													<tr> <td>  
													 
													<s:bean name="com.ifp.merchant.MerchMoniter" var="resd" >
													<s:action  name="checkStatusTermReqMerchMonitor.do" executeResult="false"  var="terminfo"> 
															 <s:param name="instid" ><%=instid%></s:param>
												  			<s:param name="merchantid" >${MERCH_ID}</s:param>
												  			<s:param name="storeid" >${STORE_ID}</s:param>
												  			<s:param name="terminalid" >${TERMINAL_ID}</s:param>
												  			
								  				      </s:action>
								  					 
								  					
								  					
														<div class="node drop-hover"  alt="<s:property value="#terminfo.terminalstatusmsg"/>" rel="texttooltip" >
															<%--  <s:property value="TERMINAL_NAME"/> <br/> <s:property value="TERMINAL_ID"/> - 
																<s:property value="#terminfo.activestatus"/>  --%>
																<s:if test="#terminfo.activestatus ==0">
																	<img src="images/merchant/pos_red.png" alt="Inactive"  width="50px"  /> <br/>
																	${TERMINAL_NAME}, ${TERMINAL_LOCATION}
																</s:if>
																<s:elseif test="#terminfo.activestatus ==1" >
																	<img src="images/merchant/pos_green.png" alt="Active"  width="50px"  /> <br/>
																	${TERMINAL_NAME}, ${TERMINAL_LOCATION}
																</s:elseif>
																<s:else>
																	No status found
																</s:else>
														</div>
												 	</s:bean>
													 </td> </tr>
												</table>
											</td>
										</s:iterator>
						
						 </s:bean>
						
					</table>
				</td>
	 	</s:iterator>
		 
		 
	
	 </tr>
	 

 </table></div></div>
   </s:if>
   <s:else>
  		<tr> <td> <div class="node "> NO STORE ADDED </div> </td> </tr></table> 	
   
   </s:else>


</body></html> 