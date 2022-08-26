<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
.errmsg{color:red;text-align:center;}
.errmsg1{color: red;text-align: left;}
</style>
<script type="text/javascript" src="js/script.js"></script>

<script type="text/javascript">
	function changecomp1next(thisid, nextid) {
		if (document.getElementById(thisid).value.length == 4) {

			if (document.getElementById(thisid).value.length == 4) {
				document.getElementById("comp1_id" + nextid).focus();
			}
		}

	}
	function changecomp2next(thisid, nextid) {
		if (document.getElementById(thisid).value.length == 4) {
			document.getElementById("comp2_id" + nextid).focus();
		}
	}
	function changecomp3next(thisid, nextid) {
		if (document.getElementById(thisid).value.length == 4) {
			document.getElementById("comp3_id" + nextid).focus();
		}
	}
	function changecomppin1next(thisid, nextid) {
		if (document.getElementById(thisid).value.length == 4) {
			document.getElementById("comppin1_id" + nextid).focus();
		}
	}

	function changecomppin2next(thisid, nextid) {
		if (document.getElementById(thisid).value.length == 4) {
			document.getElementById("comppin2_id" + nextid).focus();
		}
	}

	function getdatacheckdigit() {

		var valid = true;
		var keyname = document.getElementById("keyname");
		if (keyname) {
			if (keyname.value == "" || keyname.value == "-1") {
				errMessage(keyname, "Enter Key Name !");
				return false;
			}
		}

		var allcount = 0;
		var comp1 = document.getElementsByName("comp1");
		for (var i = 1; i <= comp1.length; i++) {
			if (document.getElementById("comp1_id" + i).value == "") {
				errMessage(document.getElementById("comp1_id" + i),
						"<b>Component 1</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comp1_id" + i).value.length != 4) {
				errMessage(document.getElementById("comp1_id" + i),
						"<b>Component 1</b> : Value Should be 4 degit");
				return false;
			}
		}
		var comp2 = document.getElementsByName("comp2");
		for (var i = 1; i <= comp2.length; i++) {
			if (document.getElementById("comp2_id" + i).value == "") {
				errMessage(document.getElementById("comp2_id" + i),
						"<b>Component 2</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comp2_id" + i).value.length != 4) {
				errMessage(document.getElementById("comp2_id" + i),
						"<b>Component 2</b> : Value Should be 4 degit");
				return false;
			}

		}

		var comp3 = document.getElementsByName("comp3");
		for (var i = 1; i <= comp3.length; i++) {
			if (document.getElementById("comp3_id" + i).value == "") {
				errMessage(document.getElementById("comp3_id" + i),
						"<b>Component 2</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comp3_id" + i).value.length != 4) {
				errMessage(document.getElementById("comp3_id" + i),
						"<b>Component 2</b> : Value Should be 4 degit");
				return false;
			}
		}
		var comp1_1 = document.getElementById("comp1_id1").value;
		var comp1_2 = document.getElementById("comp1_id2").value;
		var comp1_3 = document.getElementById("comp1_id3").value;
		var comp1_4 = document.getElementById("comp1_id4").value;
		var comp1_5 = document.getElementById("comp1_id5").value;
		var comp1_6 = document.getElementById("comp1_id6").value;
		var comp1_7 = document.getElementById("comp1_id7").value;
		var comp1_8 = document.getElementById("comp1_id8").value;
		var comp1value = (comp1_1 + comp1_2 + comp1_3 + comp1_4 + comp1_5
				+ comp1_6 + comp1_7 + comp1_8).trim();

		var comp2_1 = document.getElementById("comp2_id1").value;
		var comp2_2 = document.getElementById("comp2_id2").value;
		var comp2_3 = document.getElementById("comp2_id3").value;
		var comp2_4 = document.getElementById("comp2_id4").value;
		var comp2_5 = document.getElementById("comp2_id5").value;
		var comp2_6 = document.getElementById("comp2_id6").value;
		var comp2_7 = document.getElementById("comp2_id7").value;
		var comp2_8 = document.getElementById("comp2_id8").value;
		var comp2value = (comp2_1 + comp2_2 + comp2_3 + comp2_4 + comp2_5
				+ comp2_6 + comp2_7 + comp2_8).trim();

		var comp3_1 = document.getElementById("comp3_id1").value;
		var comp3_2 = document.getElementById("comp3_id2").value;
		var comp3_3 = document.getElementById("comp3_id3").value;
		var comp3_4 = document.getElementById("comp3_id4").value;
		var comp3_5 = document.getElementById("comp3_id5").value;
		var comp3_6 = document.getElementById("comp3_id6").value;
		var comp3_7 = document.getElementById("comp3_id7").value;
		var comp3_8 = document.getElementById("comp3_id8").value;
		var comp3value = (comp3_1 + comp3_2 + comp3_3 + comp3_4 + comp3_5
				+ comp3_6 + comp3_7 + comp3_8).trim();

		var url = "getdatacheckdigitPadssConfigAction.do?comp1value="
				+ comp1value + "&comp2value=" + comp2value + "&comp3value="
				+ comp3value;
		var result = AjaxReturnValue(url);
		document.getElementById("DMKKEYComponentDiv").innerHTML = "<input type=\"text\" style=\"width:200px;\" id=\"DMKKEYComponent\" value="+result+" readonly />";

	}

	function getpincheckdigit() {

		//alert('validate');
		var valid = true;
		var keyname = document.getElementById("keyname");
		if (keyname) {
			if (keyname.value == "" || keyname.value == "-1") {
				errMessage(keyname, "Enter Key Name !");
				return false;
			}
		}

		var allcount = 0;
		var comppin1 = document.getElementsByName("comppin1");
		for (var i = 1; i <= comppin1.length; i++) {
			if (document.getElementById("comppin1_id" + i).value == "") {
				errMessage(document.getElementById("comppin1_id" + i),
						"<b>Pin Component 1</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comppin1_id" + i).value.length != 4) {
				errMessage(document.getElementById("comppin1_id" + i),
						"<b>Pin Component 1</b> : Value Should be 4 degit");
				return false;
			}
		}

		var comppin2 = document.getElementsByName("comppin2");
		for (var i = 1; i <= comppin2.length; i++) {
			if (document.getElementById("comppin2_id" + i).value == "") {
				errMessage(document.getElementById("comppin2_id" + i),
						"<b>Pin Component 2</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comppin2_id" + i).value.length != 4) {
				errMessage(document.getElementById("comppin2_id" + i),
						"<b>Pin Component 2</b> : Value Should be 4 degit");
				return false;
			}
		}
		//alert('checkdegit');
		var comppin1_1 = document.getElementById("comppin1_id1").value;
		var comppin1_2 = document.getElementById("comppin1_id2").value;
		var comppin1_3 = document.getElementById("comppin1_id3").value;
		var comppin1_4 = document.getElementById("comppin1_id4").value;
		var comppin1_5 = document.getElementById("comppin1_id5").value;
		var comppin1_6 = document.getElementById("comppin1_id6").value;
		var comppin1_7 = document.getElementById("comppin1_id7").value;
		var comppin1_8 = document.getElementById("comppin1_id8").value;
		var comppin1value = (comppin1_1 + comppin1_2 + comppin1_3 + comppin1_4
				+ comppin1_5 + comppin1_6 + comppin1_7 + comppin1_8).trim();

		var comppin2_1 = document.getElementById("comppin2_id1").value;
		var comppin2_2 = document.getElementById("comppin2_id2").value;
		var comppin2_3 = document.getElementById("comppin2_id3").value;
		var comppin2_4 = document.getElementById("comppin2_id4").value;
		var comppin2_5 = document.getElementById("comppin2_id5").value;
		var comppin2_6 = document.getElementById("comppin2_id6").value;
		var comppin2_7 = document.getElementById("comppin2_id7").value;
		var comppin2_8 = document.getElementById("comppin2_id8").value;
		var comppin2value = (comppin2_1 + comppin2_2 + comppin2_3 + comppin2_4
				+ comppin2_5 + comppin2_6 + comppin2_7 + comppin2_8).trim();

		var url = "getpincheckdigitPadssConfigAction.do?comppin1value="
				+ comppin1value + "&comppin2value=" + comppin2value;
		var result = AjaxReturnValue(url);
		document.getElementById("DMKPINComponentDiv").innerHTML = "<input type=\"text\" style=\"width:200px;\" id=\"DMKPINComponent\" value="+result+" readonly />";

	}

	function validateNumber(field, id, enteredchar) {
		charvalue = "0123456789ABCDEF";
		//str = document.getElementById(id).value;
		for (var i = 0; i < document.getElementById(id).value.length; i++) {

			if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) == -1) {
				//alert(document.getElementById(id).value.charAt(i));
				//errMessage(document.getElementById(id).value.charAt(i)+"] -is Not Allowed For "+field);
				errMessage(document.getElementById(id), "["
						+ document.getElementById(id).value.charAt(i)
						+ "] -Only Hex Decimal Value Allowed For " + field);
				document.getElementById(id).value = '';
				return false;
			}
		}
	}

	function validate_form() {
		//alert('validate');
		var valid = true;
		var keyname = document.getElementById("keyname");
		if (keyname) {
			if (keyname.value == "" || keyname.value == "-1") {
				errMessage(keyname, "Enter Key Name !");
				return false;
			}
		}

		var allcount = 0;
		var comp1 = document.getElementsByName("comp1");
		for (var i = 1; i <= comp1.length; i++) {
			if (document.getElementById("comp1_id" + i).value == "") {
				errMessage(document.getElementById("comp1_id" + i),
						"<b>Component 1</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comp1_id" + i).value.length != 4) {
				errMessage(document.getElementById("comp1_id" + i),
						"<b>Component 1</b> : Value Should be 4 degit");
				return false;
			}
		}
		var comp2 = document.getElementsByName("comp2");
		for (var i = 1; i <= comp2.length; i++) {
			if (document.getElementById("comp2_id" + i).value == "") {
				errMessage(document.getElementById("comp2_id" + i),
						"<b>Component 2</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comp2_id" + i).value.length != 4) {
				errMessage(document.getElementById("comp2_id" + i),
						"<b>Component 2</b> : Value Should be 4 degit");
				return false;
			}

		}

		var comp3 = document.getElementsByName("comp3");
		for (var i = 1; i <= comp3.length; i++) {
			if (document.getElementById("comp3_id" + i).value == "") {
				errMessage(document.getElementById("comp3_id" + i),
						"<b>Component 2</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comp3_id" + i).value.length != 4) {
				errMessage(document.getElementById("comp3_id" + i),
						"<b>Component 2</b> : Value Should be 4 degit");
				return false;
			}
		}

		var comppin1 = document.getElementsByName("comppin1");
		for (var i = 1; i <= comppin1.length; i++) {
			if (document.getElementById("comppin1_id" + i).value == "") {
				errMessage(document.getElementById("comppin1_id" + i),
						"<b>Pin Component 1</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comppin1_id" + i).value.length != 4) {
				errMessage(document.getElementById("comppin1_id" + i),
						"<b>Pin Component 1</b> : Value Should be 4 degit");
				return false;
			}
		}

		var comppin2 = document.getElementsByName("comppin2");
		for (var i = 1; i <= comppin2.length; i++) {
			if (document.getElementById("comppin2_id" + i).value == "") {
				errMessage(document.getElementById("comppin2_id" + i),
						"<b>Pin Component 2</b> : Value Cannot be Empty");
				return false;
			}
			if (document.getElementById("comppin2_id" + i).value.length != 4) {
				errMessage(document.getElementById("comppin2_id" + i),
						"<b>Pin Component 2</b> : Value Should be 4 degit");
				return false;
			}
		}

		var comp1_1 = document.getElementById("comp1_id1").value;
		var comp1_2 = document.getElementById("comp1_id2").value;
		var comp1_3 = document.getElementById("comp1_id3").value;
		var comp1_4 = document.getElementById("comp1_id4").value;
		var comp1_5 = document.getElementById("comp1_id5").value;
		var comp1_6 = document.getElementById("comp1_id6").value;
		var comp1_7 = document.getElementById("comp1_id7").value;
		var comp1_8 = document.getElementById("comp1_id8").value;
		var comp1value = (comp1_1 + comp1_2 + comp1_3 + comp1_4 + comp1_5
				+ comp1_6 + comp1_7 + comp1_8).trim();

		var comp2_1 = document.getElementById("comp2_id1").value;
		var comp2_2 = document.getElementById("comp2_id2").value;
		var comp2_3 = document.getElementById("comp2_id3").value;
		var comp2_4 = document.getElementById("comp2_id4").value;
		var comp2_5 = document.getElementById("comp2_id5").value;
		var comp2_6 = document.getElementById("comp2_id6").value;
		var comp2_7 = document.getElementById("comp2_id7").value;
		var comp2_8 = document.getElementById("comp2_id8").value;
		var comp2value = (comp2_1 + comp2_2 + comp2_3 + comp2_4 + comp2_5
				+ comp2_6 + comp2_7 + comp2_8).trim();

		var comp3_1 = document.getElementById("comp3_id1").value;
		var comp3_2 = document.getElementById("comp3_id2").value;
		var comp3_3 = document.getElementById("comp3_id3").value;
		var comp3_4 = document.getElementById("comp3_id4").value;
		var comp3_5 = document.getElementById("comp3_id5").value;
		var comp3_6 = document.getElementById("comp3_id6").value;
		var comp3_7 = document.getElementById("comp3_id7").value;
		var comp3_8 = document.getElementById("comp3_id8").value;
		var comp3value = (comp3_1 + comp3_2 + comp3_3 + comp3_4 + comp3_5
				+ comp3_6 + comp3_7 + comp3_8).trim();

		var comppin1_1 = document.getElementById("comppin1_id1").value;
		var comppin1_2 = document.getElementById("comppin1_id2").value;
		var comppin1_3 = document.getElementById("comppin1_id3").value;
		var comppin1_4 = document.getElementById("comppin1_id4").value;
		var comppin1_5 = document.getElementById("comppin1_id5").value;
		var comppin1_6 = document.getElementById("comppin1_id6").value;
		var comppin1_7 = document.getElementById("comppin1_id7").value;
		var comppin1_8 = document.getElementById("comppin1_id8").value;
		var comppin1value = (comppin1_1 + comppin1_2 + comppin1_3 + comppin1_4
				+ comppin1_5 + comppin1_6 + comppin1_7 + comppin1_8).trim();

		var comppin2_1 = document.getElementById("comppin2_id1").value;
		var comppin2_2 = document.getElementById("comppin2_id2").value;
		var comppin2_3 = document.getElementById("comppin2_id3").value;
		var comppin2_4 = document.getElementById("comppin2_id4").value;
		var comppin2_5 = document.getElementById("comppin2_id5").value;
		var comppin2_6 = document.getElementById("comppin2_id6").value;
		var comppin2_7 = document.getElementById("comppin2_id7").value;
		var comppin2_8 = document.getElementById("comppin2_id8").value;
		var comppin2value = (comppin2_1 + comppin2_2 + comppin2_3 + comppin2_4
				+ comppin2_5 + comppin2_6 + comppin2_7 + comppin2_8).trim();

		var url = "updatekeyPadssConfigAction.do?keyname="
				+ keyname.value + "&comp1value=" + comp1value + "&comp2value="
				+ comp2value + "&comp3value=" + comp3value + "&comppin1value="
				+ comppin1value + "&comppin2value=" + comppin2value;
		;

		if (confirm("Do you want to Print this Key ? ")) {
			window.print();
		}

		if (confirm("Do you want to submit ? ")) {
			var obj = document.getElementById('addkeyform');
			obj.method = "post";
			obj.action = url;
			obj.submit();
		} else {
			return false;
		}

	}

	function chkChars(field, id, enteredchar) {
		//alert('1:'+field+'2:'+id+'3:'+enteredchar);
		charvalue = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
		//str = document.getElementById(id).value;
		for (var i = 0; i < document.getElementById(id).value.length; i++) {
			if (charvalue.indexOf(document.getElementById(id).value.charAt(i)) != -1) {
				//alert(document.getElementById(id).value.charAt(i));   
				errMessage(document.getElementById(id), "["
						+ document.getElementById(id).value.charAt(i)
						+ "] -is Not Allowed For " + field);
				document.getElementById(id).value = '';
				return false;
			}
		}
	}
</script>


<div align="center">
	<jsp:include page="/displayresult.jsp"></jsp:include>

	<s:form action="updatekeyPadssConfigAction.do" name="addkeyform" id="addkeyform" autocomplete="off">

		<fieldset style="width: 850px;">
			<legend>
				<b>DMK - DATA MASTER KEY COFIGURATION </b>
			</legend>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td>KEY NAME:</td>
					<!-- <td><input type="text" name="keyname" id="keyname"maxlength="10" onkeyup="chkChars('Key Name',this.id,this.value)" /></td> -->
				<td><s:textfield name="keyname" id="keyname" maxlength="10" onkeyup="chkChars('Key Name',this.id,this.value)" value="%{bean.KEYDESC}"/></td>
				<s:fielderror fieldName="keyname" cssClass="errmsg" />
				</tr>
			</table>
		</fieldset>

		<fieldset style="width: 850px;">
			<s:hidden id="preference" name="preference" maxlength="1"
				onkeypress='return numerals(event)'></s:hidden>
			<legend>
				<b>DMK - DATA MASTER KEY COFIGURATION </b>
			</legend>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<th style="color: red">Note : 3 Components 32 Bytes Fixed</th>
				</tr>
				<tr>
					<td>Component 1:</td>
					<td><input type="text" name="comp1" id="comp1_id1"
						maxlength="4" style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomp1next(this.id,'2')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp1" id="comp1_id2" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomp1next(this.id,'3')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp1" id="comp1_id3" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomp1next(this.id,'4')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp1" id="comp1_id4" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomp1next(this.id,'5')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp1" id="comp1_id5" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomp1next(this.id,'6')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp1" id="comp1_id6" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomp1next(this.id,'7')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp1" id="comp1_id7" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomp1next(this.id,'8')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp1" id="comp1_id8" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);" />&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<br>
				<tr>
					<td>Component 2:</td>
					<td><input type="text" name="comp2" id="comp2_id1"
						maxlength="4" style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomp2next(this.id,'2')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp2" id="comp2_id2" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomp2next(this.id,'3')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp2" id="comp2_id3" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomp2next(this.id,'4')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp2" id="comp2_id4" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomp2next(this.id,'5')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp2" id="comp2_id5" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomp2next(this.id,'6')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp2" id="comp2_id6" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomp2next(this.id,'7')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp2" id="comp2_id7" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomp2next(this.id,'8')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp2" id="comp2_id8" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);" />&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<br>
				<tr>
					<td>Component 3:</td>
					<td><input type="text" name="comp3" id="comp3_id1"
						maxlength="4" style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);changecomp3next(this.id,'2')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp3" id="comp3_id2" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);changecomp3next(this.id,'3')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp3" id="comp3_id3" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);changecomp3next(this.id,'4')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp3" id="comp3_id4" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);changecomp3next(this.id,'5')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp3" id="comp3_id5" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);changecomp3next(this.id,'6')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp3" id="comp3_id6" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);changecomp3next(this.id,'7')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp3" id="comp3_id7" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);changecomp3next(this.id,'8')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comp3" id="comp3_id8" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 3 ',this.id,this.value);" />&nbsp;&nbsp;&nbsp;
					</td>
				</tr>

			</table>

			<div align="center">
				<table border="0" cellpadding="0" cellspacing="0" width="80%">
					<tr>
						<td><input type="button" name="submit" id="submit"
							value="Check Digit" onclick="return getdatacheckdigit ( );"></td>
						<td id="DMKKEYComponentDiv"><span id=""></span></td>
					</tr>
				</table>
			</div>

			<br>
		</fieldset>


		<br>

		<fieldset style="width: 850px;">
			<legend>
				<b>DMK - DATA PIN KEY COFIGURATION </b>
			</legend>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<th style="color: red">Note : 2 Components 32 Bytes Fixed</th>
				</tr>
				<tr>
					<td>Component 1:</td>
					<td><input type="text" name="comppin1" id="comppin1_id1"
						maxlength="4" style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomppin1next(this.id,'2')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin1" id="comppin1_id2" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomppin1next(this.id,'3')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin1" id="comppin1_id3" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomppin1next(this.id,'4')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin1" id="comppin1_id4" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomppin1next(this.id,'5')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin1" id="comppin1_id5" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomppin1next(this.id,'6')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin1" id="comppin1_id6" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomppin1next(this.id,'7')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin1" id="comppin1_id7" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);changecomppin1next(this.id,'8')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin1" id="comppin1_id8" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 1 ',this.id,this.value);" />&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<br>
				<tr>
					<td>Component 2:</td>
					<td><input type="text" name="comppin2" id="comppin2_id1"
						maxlength="4" style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomppin2next(this.id,'2')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin2" id="comppin2_id2" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomppin2next(this.id,'3')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin2" id="comppin2_id3" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomppin2next(this.id,'4')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin2" id="comppin2_id4" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomppin2next(this.id,'5')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin2" id="comppin2_id5" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomppin2next(this.id,'6')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin2" id="comppin2_id6" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomppin2next(this.id,'7')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin2" id="comppin2_id7" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);changecomppin2next(this.id,'8')" />&nbsp;&nbsp;&nbsp;
						<input type="text" name="comppin2" id="comppin2_id8" maxlength="4"
						style="width: 50px;"
						onkeyup="validateNumber('Component 2 ',this.id,this.value);" />&nbsp;&nbsp;&nbsp;
					</td>
				</tr>

			</table>

			<div align="center">
				<table border="0" cellpadding="0" cellspacing="0" width="80%">
					<tr>
						<td><input type="button" name="submit" id="submit"
							value="Check Digit" onclick="return getpincheckdigit( );"></td>
						<td><span id="DMKPINComponentDiv"></span></td>
					</tr>
				</table>
			</div>

			<br>
		</fieldset>


		<div align="center">
			<table border="0" cellpadding="0" cellspacing="0" width="20%">
				<tr>
					<td><s:submit id="submit" value="SUBMIT" onclick="return validate_form( );"/></td>
					<td><input type="button" name="cancel" id="cancel"
						value="Cancel" class="cancelbtn" onclick="return confirmCancel()" />
					</td>
				</tr>
			</table>
		</div>



	</s:form>
</div>

   