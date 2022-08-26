<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %> 
<head>
<link rel="stylesheet" type="text/css" href="/css/normalize.css">
<link rel="stylesheet" type="text/css" href="/css/result-light.css">
<style type='text/css'></style>
<script type='text/javascript' src='http://code.jquery.com/jquery-1.5.2.js'></script> 

<script type="text/javascript" src="js/tinymce/tinymce.min.js"></script>
<script type="text/javascript" src="js/tinymce/tinymce_import.js"></script>

<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript" src="js/jquery/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="style/jquery_css.css">
<style>
			* {
				margin:0; 
				padding:0;
			}
</style>
<script type="text/javascript" src="js/modelpopup.js"></script>
<script type="text/javascript">
function addCode(code){
    var JS= document.createElement('script');
    JS.text= code;
    parent.document.getElementById("content").appendChild(JS);
}

// Wait until the DOM has loaded before querying the document
$(document).ready(function(){	
	var arrlen 		= 	0;	
	var mailtoarryid = [];
	$('.viewinfo').each(function(e){
		arrlen++;
		$(this).click(function(e){
			var instid 		= 	document.getElementById("instid").value;			
			modal.open({content: 
				$(this).text() 
			});
			var currenttextarea	=	$(this).parent().children("textarea").attr("id");
			var idindex			=	mailtoarryid.indexOf(currenttextarea);
			//alert("find id  ------------------)"+ idindex);
			if(idindex!=-1){
				parent.mailtoarryindex=idindex;
				//alert("index --------------- ) "+parent.mailtoarryindex);				
				parent.loadintextarea=currenttextarea;				
			}else{
				mailtoarryid.push(currenttextarea);
				//alert("id inserted ----)"+mailtoarryid);
				idindex 	=	mailtoarryid.indexOf(currenttextarea);
				//alert("index --------------- ) "	+	idindex);
				parent.mailtoarryindex		=	idindex;
				//alert("parent index value --------------- ) "	+	parent.mailtoarryindex);
				var testarry= [];								
				parent.allvalsarry[idindex]= testarry;
				//alert("array -------------------)  "+parent.allvalsarry[idindex]);
				parent.loadintextarea=currenttextarea;
				//alert("load text "+parent.loadintextarea);
				
			}
			e.preventDefault();
			$f=$(this).parents("#popdivpos").attr("id");
			$(this).parents('#modal').toggle(false);		
			//$( "#modal",document).resizable();					
		});
	});
});


function validationmail()
{
	//alert("WELCOME");
	var to =document.getElementById("tomails");
	var cc =document.getElementById("cc_mail");
	var bcc =document.getElementById("bcc_mail");
	var subject =document.getElementById("subject");
	var content =document.getElementById("content");
	var sourceDiv = document.getElementById('content_ifr').contentWindow.document.getElementById('tinymce').innerHTML;
	//alert(sourceDiv);
	var regex = /(<([^>]+)>)/ig;
	var result = sourceDiv.replace(regex, "");
	//alert(result);
	if(to)
	{
		if(to.value=="")
		{
			errMessage(to,"TO cannot be empty");
			return false;
		}
	}
	if(cc)
	{
		if(cc.value=="")
		{
			errMessage(cc,"CC cannot be empty");
			return false;		
		}
	}
	if(bcc)
	{
		if(bcc.value=="")
		{
			errMessage(bcc,"BCC cannot be empty");
			return false;
		}
	}
	if(subject)
	{
		if(subject.value=="")
		{
			errMessage(subject,"SUBJECT cannot be empty");
			return false;
		}
	}
	if(content)
	{
		
		if(result=="")
		{
			errMessage(content,"CONTENT cannot be empty");
			return false;
		}

	}
	return true;
	
}
</script>
<style>
#textcolor
{
color: maroon;
font-size: small;
}
</style>
</head>
<body>
<%
String instid = (String)session.getAttribute("Instname"); 
String displayalerts = (String)session.getAttribute("alertdescs");
String formvalue="savemailcontentAddMailAction.do";
%>
<jsp:include page="/displayresult.jsp"></jsp:include>
<form action="updatemailcontentAddMailAction.do" method="post" autocomplete="off">
<input type="hidden" name="instid" id="instid" value="<%=instid%>" />

<table>
	<tr><td>CONFIGURATIONS :</td><td class="textcolor"><s:hidden name="alertid" id="alertid" value="mbean.alertdesc"/><s:property value="mbean.alertdesc" /></td></tr>
	<tr>
		<td>TO :</td>
		<td>
			<s:textarea id="tomails"  name="tomails"  style="width: 599px; height: 25px;" value="%{tomails}" readonly="readonly"></s:textarea>
			<img src="images/addnew.png"  alt="Add " title='Add' class='viewinfo'/>
		</td>
		 
	</tr>
	<tr>
		<td>CC :</td>
		<td>
			<s:textarea id="cc_mail" name="cc_mail" style="width: 599px; height: 25px;" value="%{ccmails}" readonly="readonly"></s:textarea>
			<img src="images/addnew.png"  alt="Add " title='Add' class='viewinfo'/>
		</td>
	</tr>
	<tr>
		<td>SUBJECT :</td>
		<td>
			<s:textarea id="subject" name="subject" rows="1" style="width: 599px;height: 50px" value="%{subjectmails}"></s:textarea>
		</td>
	</tr>
	<tr>
		<td></td>
		<td align="center">CONTENT :</td>
	</tr>
	<tr>
		<td></td>
		<td><s:textarea name="content" id="content" value="%{contentmails}"></s:textarea></td>
		<td><table style="border:1px solid #efefef;"  border="0" cellpadding="0" align="center" cellspacing="0"><th id="textcolor">Key Words</th><s:iterator value="reservekey_list"><tr><td>${RESV_KEY}</td></tr></s:iterator></table></td>
	</tr>
	
	<tr>
		<td></td>
		<td align="center"><s:submit value="Update" onclick="return validationmail()"/><s:submit value="Cancel"/></td>
	</tr>
</table>

</form>
</body>