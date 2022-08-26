function emailvalidator( evalue )
{
	//alert("Email Id validator  ' "+evalue+" ' ");
	var tempemail = evalue;
	//alert("tempemail  *********** "+tempemail);
	var emailExp = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	//alert("emailExp "+emailExp);
	if(tempemail.match(emailExp))
	{
		//alert("inside if Conditions");
		endchar = tempemail.charAt(tempemail.length - 1);
		//alert("endchar is "+endchar);
		if( endchar >= "a" || endchar <= "z" || endchar == "A" || endchar == "Z" )
		{
			//alert("Character in the id Matched");
			return true;
		}
		else
		{
			//alert("Chatacters in the id not matched");
			return false;
		}
	}
	else
	{
		//alert("If Condition Not Matched");
		return false;
	}
	return true;
}


function addProfileForm()
{
	
	valid = true; 
	if(document.addprofile.instname.value == "-1")
	 {
		 errMessage(instname,"Please Select Institution Name");
		 return false;
	 }
	else if(document.addprofile.profilename.value=="")
	 {
		errMessage(profilename,"Please Enter Profile Name");
		 return false;
	 }	else if(document.addprofile.profilename.value.trim().length == 0)
	 {
			errMessage(profilename,"No space allowed for Profile Name");
			 return false;
		 }
	else if(document.addprofile.profiledesc.value=="")
	{
		errMessage(profiledesc,"Please Enter Profile Description");
		 return false;
	}	else if(document.addprofile.profiledesc.value.trim().length == 0)
	 {
		errMessage(profiledesc,"No space allowed for Profile Description");
		 return false;
	 }	
	return valid;
}

/*function addProfileDetails()
{
	
	valid = true; 
		if(document.addprofiledetails.profilename.value=="")
		 {
			errMessage(profilename,"Please Enter Profile Name");
			return false;
		 }
		else if(document.addprofiledetails.profiledesc.value=="")
		{
			errMessage(profiledesc,"Please Enter Profile Description");
			return false;
		}	
	return valid;
}

*/

function addProfileDetails()
{
	 valid=true;
	 var profilename = document.addprofiledetails.profilename;
	 var profiledesc = document.addprofiledetails.profiledesc;
	 if(profilename.value == "")
	 {
		 errMessage(profilename,"Please Enter Profile Name");
		 return false;
	 }
	 else if(profiledesc.value == "")
		{
		 errMessage(profiledesc,"Please Enter Profile Description");
		 return false;
		}
	return valid;
}


function addUserDetails()
{
	 //alert("sss");
	 var profile = document.ProductAddFofm.profile.value;
	 var usernamel = document.ProductAddFofm.username.value;
	 var password = document.ProductAddFofm.psw.value;
	 var cpassword = document.ProductAddFofm.cpsw.value;
	 var fname = document.ProductAddFofm.fname.value;
	 var lname = document.ProductAddFofm.lname.value;
	 var email = document.ProductAddFofm.email.value;
	 var passwordrpt = document.ProductAddFofm.passwordrpt.value;
	 var status = document.ProductAddFofm.status.value;
	 alert("Validate User from js file");
	 if(profile == "0")
	 {
		 alert("Please Select Profile Name");
		 return false;
	 }	
	 if(usernamel == "")
	 {
		 alert("Please Enter User Name");
		 document.getElementById("username").focus();
	     return false;
	 }	
	 if(password == "")
	 {
		 alert("Please Enter Password");
		 document.getElementById("psw").focus();
		 return false;
	 }	
	 if(cpassword == "")
	 {
		 alert("Please Enter Confirm Password");
		 document.getElementById("cpsw").focus();
		 return false;
	 }	
	 if(password!=cpassword)
		 {
			 alert("Password and Confirm Password Not Matched");
			 document.getElementById("cpsw").focus();
			 return false;
		 }
	 if(fname == "")
	 {
		 alert("Please Enter First Name");
		 cpassword.focus;
		 document.getElementById("fname").focus();
		 return false;
	 }	
	 if(lname == "")
	 {
		 alert("Please Enter Last Name");
		 cpassword.focus;
		 document.getElementById("lname").focus();
		 return false;
	 }	
	 if(email == "")
	 {
		 alert("Please Enter email id");
		 document.getElementById("email").focus();
		 return false;
	 }else
	  {
		 alert("comig inside else");
			 if( !emailvalidator( email ) )
			 {
			 	alert( "InValid E-Mail Address" );
			 	return false;
			 }

	  }
	 if(passwordrpt == "")
	 {
		 alert("Please Enter Password Repeat Count");
		 document.getElementById("passwordrpt").focus();
		 return false;
	 }	
	 if(status == "")
	 {
		 alert("Please Enter User Status");
		 document.getElementById("status").focus();
		 return false;
	 }
	 
 return true;
}

function EditInstUser()
{
	var username = document.editinstuser.username;
	 if(username.value == "-1")
	 {
		 errMessage(username,"Please Select User Name");
		 return false;
	 }
	return true;
}

function editProfileDetails()
{
	var instname = document.ProductAddFofm.instname;
	var subproduct = document.ProductAddFofm.subproduct;
	 if(instname.value == "-1")
	 {
		 errMessage(instname,"Please Select Institution Name");
		 return false;
	 }
	 if(subproduct.value == "00")
	 {
		 errMessage(subproduct,"Please Select Profile Name");
		 return false;
	 }
	return true;
}
