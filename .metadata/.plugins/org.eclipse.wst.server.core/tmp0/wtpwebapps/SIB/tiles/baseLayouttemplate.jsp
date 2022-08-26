<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link href="style/ifps.css" rel="stylesheet" type="text/css" />
<style>
table {
	margin:0 auto;
	padding:0px;
	 
}

table td{
	padding-bottom:10px;
	/* font-weight:bold; */
	color: #000080;
	font-size:11px;
}
</style>

  <%
String apptype =  (String)session.getAttribute("APPLICATIONTYPE"); 
String bg = "";
if(apptype.equals("PREPAID")){
	bg = "images/prepaid_onepix.jpg";
}else if( apptype.equals("MERCHANT")){
	bg = "images/merch_onepix.gif";
}else if( apptype.equals("BOTH")){
	bg = "images/prepaid_onepix.jpg";
}else{
	bg = "images/prepaid_onepix.jpg";
}


%>    
        
        
<table border='0' cellpadding='0' cellspacing='0' width='100%' >
  		<tr>
             <td style="background:url(<%=bg%>)">
               <tiles:insertAttribute name="header" />
             </td>
         </tr>
 		<tr>
     		<td class="formtitle" id="title" > 
     		  <tiles:insertAttribute name="title" ignore="true" /> 
     		</td>
     	</tr> 
     	
     	
     	<tr>
     		<td>
     			<tiles:insertAttribute name="body" />
     		</td>
     	</tr>
 	
</table>  		 	
						 
 
