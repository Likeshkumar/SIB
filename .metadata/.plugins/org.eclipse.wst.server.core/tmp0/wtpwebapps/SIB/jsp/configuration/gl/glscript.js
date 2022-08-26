function glvalidation()
{
	valid= true;
	//alert("fcxong");
	var glgrpcode = document.getElementById("glgroupcode");
	var glgrpdesc = document.getElementById("glgroupddesc");
	var masterglcode = document.getElementById("masterglcode");
	var glcode = document.getElementById("glcode");
	var gldesc = document.getElementById("gldesc");
	var glshortname = document.getElementById("glshortname");
	var baltype = document.getElementById("baltype");
	var alie = document.getElementById("alie");
	var statement_type = document.getElementById("statement_type");
	var entryallowed = document.getElementById("entryallowed");
	var glposition = document.getElementById("glposition");
	var cur_code = document.getElementById("cur_code");
	var mastercode = document.getElementById("mastercode");
	var schemecode = document.getElementById("schemecode");
	var schemedesc = document.getElementById("schemedesc");	
	var schemedescription = document.getElementById("schemedescription");
	var schemeshortname = document.getElementById("schemeshortname");
	var parntglcode = document.getElementById("parntglcode");
	var schemestatus = document.getElementById("schemestatus");
	var trantype = document.getElementById("trantype");
	var schemelist = document.getElementById("schemelist");
	var mastertxncode = document.getElementById("mastertxncode");
	var applicableaction = document.getElementById("applicableaction");
	var schemetype = document.getElementById("schemetype");
	var schemeorder = document.getElementById("schemeorder");
	
	if( glgrpcode ){
		if(glgrpcode.value == "")
		{
			errMessage(document.getElementById("glgroupcode"),"Please Enter GL Code");
			return false;
			alert("glgroupcode"+glgrpcode.value);
		}
	}
	if( glgrpdesc ){
		if(glgrpdesc.value == "")
		{
			errMessage(document.getElementById("glgroupddesc"),"Please Enter GL Description");
			return false;
			alert("glgroupddesc"+glgrpdesc.value);
		}
	}
	if( masterglcode )
	{
		if( masterglcode.value == -1 ){
			
			errMessage(document.getElementById("masterglcode"),"Please select Master Group");
			return false;
			alert("masterglcode"+masterglcode.value);
		}
	}
	
	if( glcode )
	{
		if( glcode.value == "" ){
			
			errMessage(glcode,"Please Enter GL Code");
			return false;
			alert("glcode"+glcode.value);
		}
	}
	
	if( gldesc )
	{
		if( gldesc.value == "" ){
			
			errMessage(document.getElementById("gldesc"),"Please Enter GL Description");
			return false;
			alert("gldesc"+gldesc.value);
		}
	}
	if( glshortname )
	{
		if( glshortname.value == "" ){
			
			errMessage(document.getElementById("glshortname"),"Please Enter GL Short Name");
			return false;
			alert("glshortname"+glshortname.value);
		}
	}

	if( baltype )
	{
		if( baltype.value == "-1" ){
			
			errMessage(document.getElementById("baltype"),"Please select Balance Type");
			return false;
			alert("baltype"+baltype.value);
		}
	}
	
	if( alie )
	{
		if( alie.value == "-1" ){
			
			errMessage(document.getElementById("alie"),"Please select ALIE");
			return false;
			alert("alie"+alie.value);
		}
	}
	
	if( statement_type )
	{
		if( statement_type.value == "-1" ){
			
			errMessage(document.getElementById("statement_type"),"Please select Statement Type");
			return false;
			alert("statement_type"+statement_type.value);
		}
	}
	
	if( entryallowed )
	{
		if( entryallowed.value == "-1" ){
			
			errMessage(document.getElementById("entryallowed"),"Please select Entry allowed");
			return false;
			alert("entryallowed"+entryallowed.value);
		}
	}
	if( glposition )
	{
		if( glposition.value == "-1" ){
			
			errMessage(document.getElementById("glposition"),"Please Select the Position");
			return false;
			alert("glposition"+glposition.value);
		}
	}
	
	if( cur_code )
	{
		if( cur_code.value == "-1" ){
			
			errMessage(document.getElementById("cur_code"),"Please Select the Currency");
			return false;
			alert("cur_code"+cur_code.value);
		}
	}
	if( mastercode )
	{
		if( mastercode.value == "-1" ){
			
			errMessage(document.getElementById("mastercode"),"Please Select the GL Group");
			return false;
			alert("mastercode"+mastercode.value);
		}
	}
	if( schemecode )
	{
		if( schemecode.value == "" ){
			
			errMessage(document.getElementById("schemecode"),"Please Enter the SchemeCode");
			return false;
			alert("schemecode"+schemecode.value);
		}
	}
	if( schemedesc )
	{
		if( schemedesc.value == "" ){
			
			errMessage(document.getElementById("schemedesc"),"Please Enter SchemeDesc");
			return false;
			alert("schemedesc"+schemedesc.value);
		}
	}
	if( schemedescription )
	{
		if( schemedescription.value == "" ){
			
			errMessage(document.getElementById("schemedescription"),"Please Enter SchemeDesc");
			return false;
			alert("schemedescription"+schemedesc.value);
		}
	}
	if( schemeshortname )
	{
		if( schemeshortname.value == "" ){
			
			errMessage(document.getElementById("schemeshortname"),"Please Enter the SchemeShort Name");
			return false;
			alert("schemeshortname"+schemeshortname.value);
		}
	}
	if( parntglcode )
	{
		if( parntglcode.value == "-1" ){
			
			errMessage(document.getElementById("parntglcode"),"Please Select Parent Gl");
			return false;
			alert("parntglcode"+parntglcode.value);
		}
	}
	if( schemestatus )
	{
		if( schemestatus.value == "-1" ){
			
			errMessage(document.getElementById("schemestatus"),"Please Select the Status");
			return false;
			alert("schemestatus"+schemestatus.value);
		}
	}
	if( schemelist )
	{
		if( schemelist.value == "-1" ){
			
			errMessage(document.getElementById("schemelist"),"Please Select the Scheme");
			return false;
			alert("schemelist"+schemelist.value);
		}
	}
	if( trantype )
	{
		if( trantype.value == "-1" ){
			
			errMessage(document.getElementById("trantype"),"Please Select the Transaction type");
			return false;
			alert("trantype"+trantype.value);
		}
	}
	if( mastertxncode )
	{
		if( mastertxncode.value == "-1" ){
			
			errMessage(document.getElementById("mastertxncode"),"Please Select the Transaction");
			return false;
			alert("mastertxncode"+mastertxncode.value);
		}
	}
	
	if( applicableaction ){
		if( applicableaction.disabled == false ){
			if( applicableaction.value == "-1" ){				
				errMessage(applicableaction,"Please Select Map Action  ");
				return false;
				alert("applicableaction "+applicableaction.value);
			}
		}
		
		
	}
	if(schemetype){
		if(schemetype.value == "-1"){
			errMessage(schemetype,"Please Select Scheme type  ");
			return false;	
		}
	}
	if(schemeorder){
		if(schemeorder.value == ""){
			errMessage(schemeorder,"Please Enter Order");
			return false;
		}
	}
	valid=true;
	parent.showprocessing();
	return valid;
}
