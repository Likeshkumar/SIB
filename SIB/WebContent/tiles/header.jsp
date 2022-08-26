<%
String apptype =  (String)session.getAttribute("APPLICATIONTYPE"); 
String headerimage = "";
if(apptype.equals("PREPAID")){
	headerimage = "images/header_prepaid_1.jpg";
}else if( apptype.equals("MERCHANT")){
	headerimage = "images/header_merchant.jpg";
}else if( apptype.equals("CREDIT")){
	headerimage = "images/header_credit.jpg";
}else if( apptype.equals("FMS")){
	headerimage = "images/header_fms.jpg";
}else if( apptype.equals("DEBIT")){
	//headerimage = "images/orient/header-dcms.jpg";
	headerimage = "images/header-south.jpg";
}else if( apptype.equals("BOTH")){
	headerimage = "images/header_prepaid.jpg";
}else{    
	headerimage = "images/header_prepaid.jpg";
}


%>
<img class="sizeimage" src="<%=headerimage%>"  alt="header image"  /> 


<Style>

.sizeimage{
  width: 1370px;
  height: 100px;
}


</Style>