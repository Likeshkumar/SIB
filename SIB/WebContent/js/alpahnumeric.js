// This function used to replace the spaces in the values
function trim(str)
{
	return str.replace(/^\s*|\s*$/g,"");
}

// This function allow only numeric values AND NOT SPECIAL CHARS
function numerals(evt)
{
       //alert( evt.keyCode );
      //alert( evt.charCode );
	var unicode=evt.charCode? evt.charCode : evt.keyCode;
        if( evt.keyCode==35 || evt.keyCode==36 || evt.keyCode==37 || evt.keyCode==39 || evt.keyCode==46 )
              return false;
	if( ( unicode > 47 && unicode < 58 ) || unicode == 8 || unicode == 9)
        {
		return true;
        }
	else
	{
		unicode=0;
		return false;
	}
}

// This function allow only alphabets values
function alphabets(evt)
{
	var unicode=evt.charCode? evt.charCode : evt.keyCode;
	/*alert(unicode);*/
	if((unicode == 35) || ((unicode >= 65) && (unicode <= 90)) || (unicode == 46) || (unicode == 8) || (unicode == 9) || (unicode == 32) || (unicode == 36) || (unicode == 37) || (unicode == 39) || ((unicode >= 97) && (unicode <= 122)))
	{
		return true;
	}
	else
	{
		unicode=0;
		return false;
	}
}


// Allow Alphabets and numeric and not special chars also
function alphanumerals(evt)
{
	//alert("test");
        var unicode=evt.charCode? evt.charCode : evt.keyCode;
        //alert(unicode);
	if((!(unicode<48||unicode>57)) || ((unicode >= 65) && (unicode <= 90)) || (unicode == 8) || (unicode == 9) || (unicode == 32)  || ((unicode >= 97) && (unicode <= 122)))
	{
		return true;
	}
	else
	{
		unicode=0;
		return false;
	}
}

// Return True or False
function ValidateCheckBox_RadioButton_V()
{
	var value = false;
	var anchorTags = document.getElementsByTagName('input');
	for (var i = 0; i < anchorTags.length ; i++)
	{			
		if( anchorTags[i].checked == true)
			{
				if( anchorTags[i].value == 'Y')
					value = true;
			}
	}
	return value;
}
// Return value for seleted check/radio box single Values
function ValidateCheckBox_RadioButton_Value()
{
	var value = '';
	var anchorTags = document.getElementsByTagName('input');
	for (var i = 0; i < anchorTags.length ; i++)
	{
		if( anchorTags[i].checked == true)
		{
			value = anchorTags[i].value;
		}
	}
	return value;
}

// Return Flag value
function ValidateCheckBox_RadioButton_Flag()
{
	var flag = 0;
	var anchorTags = document.getElementsByTagName('input');
	for (var i = 0; i < anchorTags.length ; i++)
	{
		if( anchorTags[i].checked == true)
		{
			flag = 1;
		}
	}
	return flag;
}

// Select_deselect
function Select_deselect( cmd )
{
	var anchorTags = document.getElementsByTagName('input');
	for (var i = 0; i < anchorTags.length ; i++)
	{
		if( cmd == "1")
			anchorTags[i].checked = true;
		if( cmd != "1" )
			anchorTags[i].checked = false;
	}
}

// Return CHN's as Array if only Numeric

/* This function updated on 09_Mar_09*/

function ReturnCHN_Array()
{
	var chn_new_ary = new Array();
	var anchorTags = document.getElementsByTagName('input');
	for (var i = 1,j=0;i < anchorTags.length ; i++)
	{
		if (anchorTags[i].type == "checkbox" )
		{
			if( anchorTags[i].checked == true)
			{
				if ( js_isnumeric(trim(anchorTags[i].value).substring(0,16), "0123456789") )
				{
					chn_new_ary[j] = trim( anchorTags[i].value );
					j++;
				}
			}
		}                
	}
	if (chn_new_ary.length)
		return chn_new_ary;
	else
	{
		return '0';
	}
}

function js_isnumeric(NumStr,String) {
	for(var Idx=0;Idx<NumStr.length;Idx++)	{
		var Char=NumStr.charAt(Idx); var Match=false;
		for(var Idx1=0;Idx1<String.length;Idx1++) {
			if(Char==String.charAt(Idx1)) Match = true;
		}
		if(!Match) return false;
	} return true;
}


function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function eraseCookie(name) {
	createCookie(name,"",-1);
}
function ValidateCheckBoxOnly()
{
	var flag = false;
	var anchorTags = document.getElementsByTagName('input');
       	for (var i = 0; i < anchorTags.length ; i++)
	{
                if( anchorTags[i].type == "checkbox" & anchorTags[i].id == "acctno")
                {
                       
			if( anchorTags[i].checked == true)
			{
				flag = true;
			}
			}
	}
	return flag;
}
function Validate_RadioButton_G1( val )
	{
		var value = false;
		var anchorTags = document.getElementsByTagName('input');
		for (var i = 0; i < anchorTags.length ; i++)
		{	
			if( anchorTags[i].id == val)
		        {
				if( anchorTags[i].checked == true)
				{
					if( anchorTags[i].value == 'Y')
						value = true;
				}
                       	}


		}
		return value;
	}

function ValidateCheckBox_RadioButton_multi()
	{
		if( !Validate_RadioButton_G1('Joint') )
		{
			alert('Add-On Card Will Be Issued Only To Joint A/C Holder To This Account');
		}
		else if( !Validate_RadioButton_G1('Clause') )
		{
			alert('Add-On Card Will Be Issued Only If Operation Clause  Is -Yes');
		}
		else
		{

			return true;
		}
	}


/*
////////***************************** Right Click Disable***************************************////


	var message="Due to security reason, Right Click is not allowed";
	function clickIE4()
	{
		if (event.button==2)
		{
			//alert(message);
			return false;
		}
	}

	function clickNS4(e)
	{
		if (document.layers||document.getElementById&&!document.all)
		{
			if (e.which==2||e.which==3)
			{
				//alert(message);
				return false;
			}
		}
	}

	if (document.layers)
	{
		document.captureEvents(Event.MOUSEDOWN);
		document.onmousedown=clickNS4;
	}
	else if (document.all&&!document.getElementById)
	{
		document.onmousedown=clickIE4;
	}
	/*document.oncontextmenu=new Function("alert(message);return false;");*/
        document.oncontextmenu=new Function("return false;");


//////***************Alpha Numeric Upper Case**************************////

function toAlphaNumeric(o)
{
  o.value=o.value.toUpperCase().replace(/([^0-9A-Z])/g,"");
}


function toAlphaNumSpace(o)
{
  o.value=o.value.toUpperCase().replace(/([^0-9A-Z ])/g,"");
}




 
