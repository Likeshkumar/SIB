package pin.safenet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jpos.q2.cli.SLEEP;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.instant.HSMParameter;
import com.ifp.util.CommonDesc;

public class EHsm extends BaseAction {

	public static int atohex(int val) {
		int toreturn;
		if ((val >= 48) && (val <= 57)) {
			toreturn = val - 48;
		} else if ((val >= 65) && (val <= 70)) {
			toreturn = val - 55;
		} else if ((val >= 97) && (val <= 102)) {
			toreturn = val - 87;
		} else {
			toreturn = val;
		}
		return toreturn;
	}

	public static byte[] ascii_to_bcd(String token, int offsetsize) {
		// System.out.println( " ascii_to_bcd( "+token+ ", "+ offsetsize );
		int i = 0;
		int index = 0;
		String value = "";
		byte[] toprocess_token = token.getBytes();
		byte[] buffer = new byte[offsetsize];
		while (i < offsetsize) {
			int lnibble = atohex((int) toprocess_token[index]);
			int rnibble = atohex((int) toprocess_token[index + 1]);
			buffer[i] = (byte) ((lnibble << 4) | (rnibble & 0xf));
			i += 1;
			index += 2;
		}
		return buffer;
	}

	public ByteArrayOutputStream eracom_out(String data, int datasize, String description, int conv_type,
			ByteArrayOutputStream bytestream) throws IOException {
		// trace("Description : " + description);
		byte ascibcd[];
		int offsetsize = 0;
		offsetsize = datasize;
		String data_toprocess;
		if (conv_type == 1) {
			if ((datasize % 2) == 0) {
				offsetsize = datasize / 2;
				data_toprocess = data;
			} else {
				offsetsize = datasize / 2 + 1;
				data_toprocess = "0" + data;
			}
			bytestream.write(ascii_to_bcd(data_toprocess, offsetsize));

			trace(" ERACOM_OUT STR [ " + description + " ] and value " + data_toprocess);

		} else if (conv_type == 2) {
			int dataint = Integer.parseInt(data);
			byte datachar = (byte) dataint;
			bytestream.write(datachar);
			trace(" ERACOM_OUT INT [ " + description + " ] and value " + data);
		} else if (conv_type == 3) {
			bytestream.write(data.getBytes());
			trace(" ERACOM_OUT BYTE [ " + description + " ] and value " + data);
		}

		return bytestream;
	}

	// Safenet Pin Mailer

	public ByteArrayOutputStream ComposePINMailer(String instid, String command, String EPVK, String CHN, String cin,
			String productcode, String subproduct, String gentype, JdbcTemplate jdbctemplate, CommonDesc commondesc,
			ByteArrayOutputStream outputStream, HSMParameter hsmobj) throws Exception {

		int PanOffset = Integer.parseInt(hsmobj.PAN_OFFSET);
		int PanLength = Integer.parseInt(hsmobj.PANVALIDATION_LENGTH);
		String PinType = "01";
		String pinpadchar = hsmobj.PANPADCHAR;

		String PinLen = "04";
		String PinLineNo = ""; // 06 //11
		String PinColumnNo = ""; // 36 //55

		String CHNLineNo = "10";
		String CHNColumnNo = "11";

		String BankNameLineNo = "11";
		String BankNameColumnNo = "11";
		String CustomerName = "12232";
		String CustomerNameLineNo = "10";
		String CustomerNameColumnNo = "0";
		String Address1 = "0";
		String Address1LineNo = "0";
		String Address1ColumnNo = "0";
		String Address2 = "0";
		String Address2LineNo = "0";
		String Address2ColumnNo = "0";

		String Address3 = "0";
		String Address3LineNo = "0";
		String Address3ColumnNo = "0";

		String Address4 = "0";
		String Address4LineNo = "0";
		String Address4ColumnNo = "0";

		outputStream = eracom_out(command, command.length(), "FunctionCode", 1, outputStream);
		outputStream = eracom_out("01", 2, "FUNCTION-MODIFIER", 1, outputStream);

		int len = EPVK.length() / 2 + 1;

		outputStream = eracom_out(Integer.toString(len), Integer.toString(len).length(), "PVK-LENGTH", 2, outputStream);

		if (EPVK.length() == 16)
			outputStream = eracom_out("10", 2, "PVK-FORMAT", 1, outputStream);
		else if (EPVK.length() == 32)
			outputStream = eracom_out("11", 2, "PVK-FORMAT", 1, outputStream);
		else
			outputStream = eracom_out("12", 2, "PVK-FORMAT", 1, outputStream);

		outputStream = eracom_out(EPVK, EPVK.length(), "PVK", 1, outputStream);
		String pandata;
		if (CHN.length() == 19) {
			pandata = CHN.substring(3, 19);
		} else {
			pandata = CHN.substring(0, 16);
		}

		if (pandata.length() < 16) {
			String repeated = String.format(String.format("%%0%dd", (16 - pandata.length())), 0).replace("0",
					pinpadchar);
			trace("Repeated String : " + repeated);
			pandata = pandata + repeated;
			trace("pandata : " + pandata);
		}

		Boolean pinconfigured = false;

		String pinmailerqry = "select trim(FIELD_NAME) as FIELD_NAME, trim(FIELD_LENGTH) AS FIELD_LENGTH , TRIM(X_POS) AS X_POS, TRIM(Y_POS) AS Y_POS from IFP_PINMAILER_PROPERTY WHERE INST_ID='"
				+ instid + "' AND PINMAILER_ID='" + hsmobj.PINMAILER_ID
				+ "' AND PRINT_REQUIRED='Y'  ORDER BY PRINTORDER ";
		enctrace("pinmailerqry :" + pinmailerqry);
		List pinmailerlist = jdbctemplate.queryForList(pinmailerqry);
		if (!pinmailerlist.isEmpty()) {
			Iterator itr = pinmailerlist.iterator();
			String fldkey = null;

			int repeat = pinmailerlist.size() - 1;

			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				fldkey = (String) mp.get("FIELD_NAME");
				trace("Processing...fieldkye :" + fldkey);

				if (fldkey.equals("PINNO")) {
					PinLineNo = (String) mp.get("X_POS");
					PinColumnNo = (String) mp.get("Y_POS");
					outputStream = eracom_out(pandata, pandata.length(), "PAN-VAL-DATA", 1, outputStream);
					outputStream = eracom_out(PinLen, PinLen.length(), "PIN-LEN", 1, outputStream);
					outputStream = eracom_out(PinType, PinType.length(), "PIN-TYPE", 1, outputStream);
					outputStream = eracom_out(PinLineNo, PinLineNo.length(), "PIN-LINE-NO", 2, outputStream);
					outputStream = eracom_out(PinColumnNo, PinColumnNo.length(), "PIN-COLOMN-NO", 2, outputStream);

					outputStream = eracom_out("00", 2, "PAN-LINE-NO", 2, outputStream);
					outputStream = eracom_out("00", 2, "PAN-COLOMN-NO", 2, outputStream);

					outputStream = eracom_out(Integer.toString(repeat), Integer.toString(repeat).length(),
							"REPEAT-COUNT", 2, outputStream);

					pinconfigured = true;
				}

				if (fldkey.equals("CARDNO")) {
					CHNLineNo = (String) mp.get("X_POS");
					CHNColumnNo = (String) mp.get("Y_POS");
					outputStream = eracom_out(CHNLineNo, CHNLineNo.length(), "CHN-LINE-NO", 2, outputStream);
					outputStream = eracom_out(CHNColumnNo, CHNColumnNo.length(), "CHN-COLUMN-NO", 2, outputStream);
					int chnlen = CHN.length();
					outputStream = eracom_out(Integer.toString(chnlen), Integer.toString(chnlen).length(), "CHN-LEN", 2,
							outputStream);
					outputStream = eracom_out(CHN, CHN.length(), "CHN", 3, outputStream);
				}

				else if (fldkey.equals("BANKNAME")) {
					BankNameLineNo = (String) mp.get("X_POS");
					BankNameColumnNo = (String) mp.get("Y_POS");
					String bankname = commondesc.getInstDesc(instid, jdbctemplate);
					outputStream = eracom_out(BankNameLineNo, 1, "BANKNAME-LINE-NO", 2, outputStream);
					outputStream = eracom_out(BankNameColumnNo, 1, "BANKNAME-COLUMN-NO", 2, outputStream);
					int banklen = bankname.length();
					outputStream = eracom_out(Integer.toString(banklen), 1, "BANKNAME-LEN", 2, outputStream);
					outputStream = eracom_out(bankname, bankname.length(), "BANKNAME", 3, outputStream);
				}

				else if (fldkey.equals("CUSTNAME")) {
					CustomerNameLineNo = (String) mp.get("X_POS");
					CustomerNameColumnNo = (String) mp.get("Y_POS");
					trace("gentype: " + gentype);
					CustomerName = commondesc.getCustomerNameByProces(instid, productcode, subproduct, cin, gentype,
							jdbctemplate);
					if (CustomerName == null || CustomerName.equals("")) {
						CustomerName = commondesc.getCustomerNameByProduction(instid, productcode, subproduct, cin,
								gentype, jdbctemplate);
					}

					int customernamelen = CustomerName.length();
					outputStream = eracom_out(CustomerNameLineNo, CustomerNameLineNo.length(), "CUSTNAME-LINE-NO", 2,
							outputStream);
					outputStream = eracom_out(CustomerNameColumnNo, CustomerNameColumnNo.length(), "CUSTNAME-COLUMN-NO",
							2, outputStream);
					outputStream = eracom_out(Integer.toString(customernamelen),
							Integer.toString(customernamelen).length(), "CUSTNAME-LEN", 2, outputStream);
					outputStream = eracom_out(CustomerName, CustomerName.length(), "CUSTOMER NAME", 3, outputStream);
				} else if (fldkey.equals("ADDRESSONE")) {
					Address1LineNo = (String) mp.get("X_POS");
					Address1ColumnNo = (String) mp.get("Y_POS");

					Address1 = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR1",
							jdbctemplate);
					int Address1Len = Address1.length();
					outputStream = eracom_out(Address1LineNo, Address1LineNo.length(), "ADDRESS1-LINE-NO", 2,
							outputStream);
					outputStream = eracom_out(Address1ColumnNo, Address1ColumnNo.length(), "ADDRESS1-COLUMN-NO", 2,
							outputStream);
					outputStream = eracom_out(Integer.toString(Address1Len), Integer.toString(Address1Len).length(),
							"ADDRESS1-LEN", 2, outputStream);
					outputStream = eracom_out(Address1, Address1.length(), "ADDRESS1", 3, outputStream);
				}

				else if (fldkey.equals("ADDRESSTWO")) {
					Address2LineNo = (String) mp.get("X_POS");
					Address2ColumnNo = (String) mp.get("Y_POS");

					Address2 = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR2",
							jdbctemplate);
					int Address2Len = Address2.length();
					outputStream = eracom_out(Address2LineNo, Address2LineNo.length(), "ADDRESS2-LINE-NO", 2,
							outputStream);
					outputStream = eracom_out(Address2ColumnNo, Address2ColumnNo.length(), "ADDRESS2-COLUMN-NO", 2,
							outputStream);
					outputStream = eracom_out(Integer.toString(Address2Len), Integer.toString(Address2Len).length(),
							"ADDRESS2-LEN", 2, outputStream);
					outputStream = eracom_out(Address2, Address2.length(), "ADDRESS2", 3, outputStream);
				}

				else if (fldkey.equals("ADDRESSTHREE")) {
					Address3LineNo = (String) mp.get("X_POS");
					Address3ColumnNo = (String) mp.get("Y_POS");
					Address3 = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR3",
							jdbctemplate);

					int Address3Len = Address3.length();
					outputStream = eracom_out(Address3LineNo, Address3LineNo.length(), "ADDRESS3-LINE-NO", 2,
							outputStream);
					outputStream = eracom_out(Address3ColumnNo, Address3ColumnNo.length(), "ADDRESS3-COLUMN-NO", 2,
							outputStream);
					outputStream = eracom_out(Integer.toString(Address3Len), Integer.toString(Address3Len).length(),
							"ADDRESS3-LEN", 2, outputStream);
					outputStream = eracom_out(Address3, Address3.length(), "ADDRESS3", 3, outputStream);
				}

				else if (fldkey.equals("ADDRESSFOUR")) {
					Address4LineNo = (String) mp.get("X_POS");
					Address4ColumnNo = (String) mp.get("Y_POS");
					Address4 = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR4",
							jdbctemplate);

					int Address4Len = Address4.length();
					outputStream = eracom_out(Address4LineNo, Address4LineNo.length(), "ADDRESS4-LINE-NO", 2,
							outputStream);
					outputStream = eracom_out(Address4ColumnNo, Address4ColumnNo.length(), "ADDRESS4-COLUMN-NO", 2,
							outputStream);
					outputStream = eracom_out(Integer.toString(Address4Len), Integer.toString(Address4Len).length(),
							"ADDRESS4-LEN", 2, outputStream);
					outputStream = eracom_out(Address4, Address4.length(), "ADDRESS4", 3, outputStream);
				}

				/*
				 * else if( fldkey.equals("CUSTID") ){ CustomerIdLineNo =
				 * (String)mp.get("X_POS"); CustomerIdColumnNo=
				 * (String)mp.get("Y_POS"); if( !gentype.equals("INSTANT")){ cin
				 * = "--"; } int customeridlen = cin.length(); outputStream =
				 * eracom_out( CustomerIdLineNo, CustomerIdLineNo.length(),
				 * "CUSTOMER-ID", 2, outputStream); outputStream = eracom_out(
				 * CustomerIdColumnNo,CustomerIdColumnNo.length(),
				 * "CUSTOMER-ID-COLUMN-NO", 2, outputStream); outputStream =
				 * eracom_out( Integer.toString(customeridlen),
				 * Integer.toString(customeridlen).length(), "CUSTOMER-ID-LEN",
				 * 2, outputStream); outputStream = eracom_out( cin,
				 * cin.length(), "CUSTOMER-ID", 3, outputStream); }
				 */
			}

			if (!pinconfigured) {
				trace("!!!!!!!!!!!!!!!!Pin Line or Coloumn not configured!!!!!!!!");
				addActionError("Pin Line or Coloumn not configured");
			}

		}
		return outputStream;
	}

	// Racal Pin Mailer

/*	public ByteArrayOutputStream ComposeRacalPINMailer(String instid, String EPVK, String CHN, String mcard, String cin,
			String productcode, String subproduct, String gentype, JdbcTemplate jdbctemplate, CommonDesc commondesc,
			ByteArrayOutputStream outputStream, HSMParameter hsmobj, DataInputStream in, DataOutputStream out)
			throws Exception {

		HsmTcpIp hsmtcp = new HsmTcpIp();
		// String pinval="";

		try {
			int PanOffset = Integer.parseInt(hsmobj.PAN_OFFSET);
			int PanLength = Integer.parseInt(hsmobj.PANVALIDATION_LENGTH);
			String DecimalizationTable = hsmobj.DECIMILISATION_TABLE;
			String PinType = "01";
			String pinpadchar = hsmobj.PANPADCHAR;
			String MaskBlock = "FFFFFFFFFFFFFFFFFFF";
			String PinLen = "04";
			String PinLineNo = ""; // 06 //11
			String PinColumnNo = ""; // 36 //55

			String CHNLineNo = "10";
			String CHNColumnNo = "11";

			String BankNameLineNo = "11";
			String BankNameColumnNo = "11";
			String CustomerName = "12232";
			String CustomerNameLineNo = "10";
			String CustomerNameColumnNo = "0";
			String Address1 = "0";
			String Address1LineNo = "0";
			String Address1ColumnNo = "0";
			String Address2 = "0";
			String Address2LineNo = "0";
			String Address2ColumnNo = "0";

			String Address3 = "0";
			String Address3LineNo = "0";
			String Address3ColumnNo = "0";

			String Address4 = "0";
			String Address4LineNo = "0";
			String Address4ColumnNo = "0";

			String chn = CHN;
			String pan = "";
			int len = chn.length();
			pan = chn.substring(len - 13, len - 1);
			trace("pan no :::" + pan);// 762020000001

			trace("Generating the Formatted Data");
			String res = getHeaderField(instid, hsmobj.PINMAILER_ID, jdbctemplate, commondesc, productcode, subproduct,
					gentype, cin);
		//	trace("Final pinmailer  postion result is ::::::::   " + res);
			ByteArrayOutputStream formatdata = generateFormattedData(res);
			trace(" formatdata:: "+formatdata);
			out.write(formatdata.toByteArray());
			out.flush();
			byte[] formatresponse = new byte[18];
			in.read(formatresponse);
		
			String rsp = new String(formatresponse);
			trace("GOT RESPONSE FROM HSM");
			trace("response :: "+rsp.trim());
			String convrsp = rsp.trim();
			String format_data = getRacalFormatDataResponse(convrsp);
			trace("RESPONSE AS" + format_data);

			trace("******Generating the Pin Offset****************");
			String pad = "00";
			String val = "HMAS";
			val += "JA";
			val += pan;
			val += PinLen;

			outputStream.write(val.length());
			trace("Byte to Hex Conversion :::" + hsmtcp.byteToHexString(outputStream.toByteArray()));
			String convlen = hsmtcp.byteToHexString(outputStream.toByteArray());
			trace("Byte to Hex String:::" + convlen);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(pad.getBytes());
			bout.write(convlen.getBytes());
			String newdsf = new String(bout.toByteArray());
			trace("Input Parameter :::" + newdsf);
			String ascii = hsmtcp.hexToASCII(newdsf);
			trace("Hex to ASCII conversion:::" + ascii);
			ByteArrayOutputStream pinhex = new ByteArrayOutputStream();
			pinhex.write(ascii.getBytes());
			pinhex.write(val.getBytes());
			out.write(pinhex.toByteArray());
			out.flush();

			byte[] response = new byte[30];
			in.read(response);
			trace("Getting Response from HSM");
			rsp = new String(response);
			convrsp = rsp.trim();
			String rand_pinvalue = getRacalRandomPinResponse(convrsp);
			trace("Random Pin Value is " + rand_pinvalue);
			bout.flush();

			String strpan = chn;// .substring(PanOffset);
			// String strpan = chn.substring(len-13, len-1);
			trace("strpan" + strpan);
			String strcharN = chn.substring(chn.length() - 6, chn.length() - 1);
			trace("strcharN" + strcharN);
			int panData = strpan.length();
			String strpinValidationData = "";
			int ncharLoc;

			strpinValidationData = chn.substring(0, 10) + 'N' + chn.substring(chn.length() - 1);

			
			 * if(strpan.lastIndexOf(strcharN) < 1){
			 * 
			 * 
			 * strpinValidationData = chn.substring(0,10) + 'N' +
			 * chn.substring(chn.length() - 1); } if(strpan.length() == 12){
			 * 
			 * if(strpan.substring(2,7) == strpan.substring(7, 12)){
			 * 
			 * strpan = strpan.substring(0, 7);
			 * 
			 * 
			 * if(strpan.lastIndexOf(strcharN) > 1 && PanLength == panData ) {
			 * if(strpan.substring(strpan.length()-6) ==
			 * chn.substring(chn.length() - 6)) {
			 * 
			 * strpinValidationData = strpan.substring(0,
			 * 7)+'N'+chn.substring(chn.length()-1);
			 * 
			 * } else {
			 * 
			 * strpinValidationData = strpan.substring(0, 7)+'N';
			 * 
			 * } } } else{ ncharLoc = strpan.lastIndexOf(strcharN);
			 * 
			 * if(ncharLoc > 1 && PanLength == panData) {
			 * if(strpan.substring(strpan.length()-6) ==
			 * chn.substring(chn.length()-6)) { strpinValidationData =
			 * strpan.substring(0, ncharLoc)+'N'+chn.substring(chn.length()-1);
			 * }else { strpinValidationData = strpan.substring(0, ncharLoc)+'N';
			 * } } }
			 * 
			 * 
			 * 
			 * }else {
			 * 
			 * ncharLoc = strpan.lastIndexOf(strcharN); trace(
			 * "Last Index Value is::"+ncharLoc); if(ncharLoc > 1 && PanLength
			 * == panData ){
			 * 
			 * if(strpan.substring(strpan.length()-6) ==
			 * chn.substring(chn.length()-6)) {
			 * 
			 * strpinValidationData = strpan.substring(0,
			 * ncharLoc)+'N'+chn.substring(chn.length()-1); trace(
			 * "Pin Validation Data ::::"+strpinValidationData); }else {
			 * 
			 * strpinValidationData = strpan.substring(0, ncharLoc)+'N'; trace(
			 * "Pin Validation Data:::"+strpinValidationData); } } } trace(
			 * "Pin Validation Data:::"+strpinValidationData);
			 * trace("asdfasfasfasf"+strpinValidationData.charAt(10));
			 * if(strpinValidationData.charAt(10) == 'N' &&
			 * strpinValidationData.length() == 11){ trace("dssssss");
			 * strpinValidationData += chn.substring(chn.length()-1);
			 * trace("lastindex00dsdsa"+strpinValidationData); }
			 * 
			 * int npinValidationData = strpinValidationData.length();
			 * 
			 * if(npinValidationData != 12) { trace(
			 * "PVK generation data length is not 12"+npinValidationData);
			 * //strpinValidationData +=
			 * MaskBlock.substring(0,12-npinValidationData); //to date some
			 * value here strpinValidationData +=CHN.substring(15, 16); }
			 * if(strpinValidationData.length() != 12) { trace(
			 * "pin validation data length is not 12"); }
			 
			trace("Pin Validation Data :::" + strpinValidationData);

			ByteArrayOutputStream racalpinoffset = generateRacalPinOffset(EPVK, rand_pinvalue, PinLen, pan,
					DecimalizationTable, strpinValidationData);
			trace("pinoffset value" + racalpinoffset);
			out.write(racalpinoffset.toByteArray());
			out.flush();
			byte[] pinoffsetresponse = new byte[23];
			in.read(pinoffsetresponse);
			trace("Got Response From HSM");
			rsp = new String(pinoffsetresponse);
			convrsp = rsp.trim();
			trace("Response as" + convrsp);
			String pinoffsetvalue = getRacalPinOffsetResponse(convrsp);
			trace("Pin Offset is:::" + pinoffsetvalue);
			racalpinoffset.flush();

			ByteArrayOutputStream racalencryptpinoffset = generateRacalEncryptPinOffset(EPVK, pinoffsetvalue, PinLen,
					pan, DecimalizationTable, strpinValidationData);
			trace("pinoffset value" + racalencryptpinoffset);
			out.write(racalencryptpinoffset.toByteArray());
			out.flush();
			byte[] encryptpinoffsetresponse = new byte[23];
			in.read(encryptpinoffsetresponse);
			trace("Response from HSM for Encrypted Pin.");
			rsp = new String(encryptpinoffsetresponse);
			trace("Encrypted Pin Offset::" + rsp);
			convrsp = rsp.trim();
			String encryptpinoffsetvalue = getRacalRandomPinResponse(convrsp);
			trace("Encrypted Pin Offset Value:::" + encryptpinoffsetvalue);
			racalencryptpinoffset.flush();

			trace("************Pin Mailer Data Start***********************");
			String pinmailerdata = gettingPinMailerDetails(gentype, cin, commondesc, jdbctemplate, chn, mcard, instid,
					hsmobj, productcode, subproduct);
			trace("Pin Mailer Data is::" + pinmailerdata);
			String generatepinmailer = generatePinOffPinMailer(pan, rand_pinvalue, pinmailerdata);
			trace("Generated Pin Mailer Data is::" + generatepinmailer);

			ByteArrayOutputStream pinmailerhex = generatePinMailerDataDetails(generatepinmailer);
			trace("Pin Mailer Hex Value :::" + pinmailerhex);
			out.write(pinmailerhex.toByteArray());
			out.flush();
			byte[] pinmailerhexresponse = new byte[23];
			in.read(pinmailerhexresponse);
			trace("Response From HSM for Pin Mailer...");
			rsp = new String(pinmailerhexresponse);
			convrsp = rsp.trim();
			trace("Before Pin Printing Response as:::" + convrsp);
			String pinmailerhexresponsevalue = getRacalPrinterDataResponse(convrsp);
			trace("Pin Mailer Response is:::" + pinmailerhexresponsevalue);
			String respmsg = "";
			if (pinmailerhexresponsevalue.equals("00")) {
				Thread.sleep(5000);
				byte[] pinmailerhexafterresponse = new byte[23];
				in.read(pinmailerhexafterresponse);
				trace("Getting Response from HSM for After Pin Printing");
				rsp = new String(pinmailerhexafterresponse);
				convrsp = rsp.trim();
				trace("After Pin Printing response as:::" + convrsp);
				String pinmailerhexafterresponsevalue = "00";// getRacalPrinterDataAfterResponse(convrsp);
				trace("Pin Mailer Printer Response" + pinmailerhexafterresponsevalue);
				if (pinmailerhexafterresponsevalue.equals("00")) {
					trace("Pin Mailer generated successfully");
				} else {
					trace("After Pin Mailer Response Error:::" + pinmailerhexafterresponsevalue);
					respmsg = pinmailerhexafterresponsevalue;
					outputStream.reset();
					outputStream.write(respmsg.getBytes());
					return outputStream;
				}

			} else {
				trace("Response Message got Mismatch or Null");
				respmsg = pinmailerhexresponsevalue;
				outputStream.reset();
				outputStream.write(respmsg.getBytes());
				return outputStream;
			}

			// String pinval =
			// pinoffsetvalue.substring(0,Integer.parseInt(PinLen));
			String pinval = pinoffsetvalue.substring(0, 4);
			trace("pinval" + pinval);

			outputStream.reset();
			outputStream.write(pinval.getBytes());

			trace("pinmailerdata end.....");
		} catch (Exception e) {
			System.out.println("Exception Happened" + e.getMessage());
		}
		return outputStream;
	}
*/	
	
	
	public String ComposeRacalPINMailer( String instid,String EPVK, String CHN,String mcard, String cin, String productcode, String subproduct, String gentype, JdbcTemplate jdbctemplate, CommonDesc commondesc, ByteArrayOutputStream outputStream, HSMParameter hsmobj,DataInputStream in, DataOutputStream out  ) throws Exception {   
		
		System.out.println("instid-->"+instid); 
		System.out.println("EPVK-->"+EPVK);
		System.out.println("CHN-->"+CHN);
		System.out.println("CIN-->"+cin);
		System.out.println("productcode-->"+productcode);
		System.out.println("subproduct-->"+subproduct);
		System.out.println("gentype-->"+gentype);
		//System.out.println("CHN-->"+CHN);
		
		
		
		HsmTcpIp hsmtcp = new HsmTcpIp();
		String retpinblock = "NA" , clearpinresponse= "NA";
		try{
		int PanOffset = Integer.parseInt( hsmobj.PAN_OFFSET );
		trace("panoffset -->"+PanOffset);
		int  PanLength =Integer.parseInt( hsmobj.PANVALIDATION_LENGTH );
		String DecimalizationTable = hsmobj.DECIMILISATION_TABLE;
		String PinType = "01" ;
		String pinpadchar =  hsmobj.PANPADCHAR;
		String MaskBlock = "FFFFFFFFFFFFFFFFFFF";
		String PinLen = "04";
		String PinLineNo="";  //06   //11
		String PinColumnNo = ""; //36   //55
		
		String CHNLineNo = "10";
		String CHNColumnNo = "11";
	 
		
		String BankNameLineNo = "11";
		String BankNameColumnNo = "11";  
		String CustomerName = "12232";
		String CustomerNameLineNo="10";
		String CustomerNameColumnNo="0"; 
		String Address1="0";
		String Address1LineNo="0";
		String Address1ColumnNo="0"; 
		String Address2="0";
		String Address2LineNo="0";
		String Address2ColumnNo="0";
		 
		String Address3="0";
		String Address3LineNo="0";
		String Address3ColumnNo="0";
		
		 
		String Address4="0";
		String Address4LineNo="0";
		String Address4ColumnNo="0";
		String res="";
		String convrsp ="";
		String rsp="";
		
		//String chn = mcard;
		String chn = CHN;
		String pan = "";
		int len = chn.length();
		pan = chn.substring(len-13, len-1);
		trace("pan no :::"+pan);//762020000001
		
		trace("Generating the Formatted Data");
		
		
		  res = getHeaderField(instid,hsmobj.PINMAILER_ID,jdbctemplate,commondesc,productcode,subproduct,gentype,cin);
		ByteArrayOutputStream formatdata = generateFormattedData(res);
		out.write(formatdata.toByteArray());
		out.flush(); 
		byte[] formatresponse = new byte[18];
		in.read(formatresponse);		
		trace("getting response from");
		  rsp = new String (  formatresponse );
		 convrsp = rsp.trim();
		String format_data = getRacalFormatDataResponse(convrsp);
		trace("the output came from "+format_data); 
		
		trace("gegerating random pin inputs");
		
		
		trace("Generating theRANDOM PIN");
		String pad = "00";
		String val = "HMAS";
		val += "JA";
		System.out.println("headers-->"+val);
		val += pan;
		System.out.println("with pan number-->"+val);
		val += PinLen;
		System.out.println("with pin length-->"+val);
		
		trace("sending values to JA command " + val);
		System.out.println("final JA COMMAND INPUT-->"+val);
		
		outputStream.write(val.length());
		byte dd[] = outputStream.toByteArray();
	    for (int i = 0; i < dd.length; i++) {
	      System.out.print(+(char) dd[i]);
	      
	    }
		//outputStream.write(PinLen.getBytes());
		trace("dfasfsh"+hsmtcp.byteToHexString(outputStream.toByteArray()));
		String convlen = hsmtcp.byteToHexString(outputStream.toByteArray());
		trace("convlen"+convlen);
		ByteArrayOutputStream bout  = new ByteArrayOutputStream();
		bout.write(pad.getBytes());
		bout.write(convlen.getBytes());
		byte cd[] = bout.toByteArray();
	    for (int i = 0; i < cd.length; i++) {
	      System.out.println(+(char) cd[i]);
	      
	    }
		String newdsf = new String(bout.toByteArray());
		trace("hex to ascii value"+newdsf);
	    	//String ascii = HsmTcpIp.hexToASCII(newdsf);
	    	
	    	String ascii= hsmtcp.hexToASCII(newdsf);
		trace("ascii value"+ascii);
		ByteArrayOutputStream pinhex  = new ByteArrayOutputStream();
		pinhex.write(ascii.getBytes());
		pinhex.write(val.getBytes());
	
		//bout.write(val.getBytes());
		byte c[] = pinhex.toByteArray();
	    for (int i = 0; i < c.length; i++) {
	      System.out.print(+(char) c[i]);
	      
	    }
		out.write(pinhex.toByteArray());
		out.flush(); 
		
		trace("adafasfsdf");
		byte[] response = new byte[30];
		in.read(response);		
		trace("getting response for JA");
		rsp = new String (  response );
	    convrsp = rsp.trim();
		String rand_pinvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from "+rand_pinvalue);
		bout.flush();
		
		
		trace("Generating Pin Offset");
		
		 String strpan = chn;//.substring(PanOffset);
			//String strpan = chn.substring(len-13, len-1);
			trace("strpan"+ strpan);
			String strcharN = chn.substring(chn.length() - 6, chn.length() - 1);
			trace("strcharN"+strcharN);
			int panData = strpan.length();
			String strpinValidationData= "";
			int ncharLoc;
			//strpinValidationData = chn.substring(0,10) + 'N' + chn.substring(chn.length() - 1);
			strpinValidationData = chn.substring(3,13) + 'N' + chn.substring(chn.length() - 1);
			
			
		
		/*String strpan = chn.substring(PanOffset);
		//String strpan = chn.substring(len-13, len-1);
		trace("strpan"+strpan);
		//String strcharN = chn.substring(chn.length() - 6, chn.length() - 1);
		String strcharN = "0000";
		trace("strcharN"+strcharN);
		int panData = strpan.length();
		String strpinValidationData= "";int ncharLoc;
		if(strpan.lastIndexOf(strcharN) < 1){
			
			
			strpinValidationData = chn.substring(0,10) + 'N' + chn.substring(chn.length() - 1);
		}
		if(strpan.length() == 12){
			trace("121212");
			if(strpan.substring(2,7) == strpan.substring(7, 12)){
				
				strpan = strpan.substring(0, 7);
				trace("test11");
				
				if(strpan.lastIndexOf(strcharN) > 1 && PanLength == panData )
				{
					if(strpan.substring(strpan.length()-6) == chn.substring(chn.length() - 6))
					{
						trace("test11");
						strpinValidationData = strpan.substring(0, 7)+'N'+chn.substring(chn.length()-1);
						
					}
					else
					{
						
						strpinValidationData = strpan.substring(0, 7)+'N';
						
					}
				}
			}
				else{
					ncharLoc = strpan.lastIndexOf(strcharN);
					trace("321312321");
					if(ncharLoc > 1 && PanLength == panData)
					{trace("dsgdsgdf");
						if(strpan.substring(strpan.length()-6) == chn.substring(chn.length()-6))
						{
							strpinValidationData = strpan.substring(0, ncharLoc)+'N'+chn.substring(chn.length()-1);
						}else
						{trace("4324324324");
							strpinValidationData  = strpan.substring(0, ncharLoc)+'N';
						}
					}
				}
			
			trace("423432");
			
		}else
		{    
			
			ncharLoc = strpan.lastIndexOf(strcharN);
			trace("sarrr"+ncharLoc);
					if(ncharLoc > 1 && PanLength == panData ){
						
						if(strpan.substring(strpan.length()-6) == chn.substring(chn.length()-6))
						{
							trace("sssss");
							strpinValidationData = strpan.substring(0, ncharLoc)+'N'+chn.substring(chn.length()-1);
							trace("111111111"+strpinValidationData);
						}else
						{
							trace("sardar test");
							strpinValidationData  = strpan.substring(3, 10)+'N';
							trace("222222"+strpinValidationData);
						}
					}
					strpinValidationData  = strpan.substring(3, 10)+'N';
					trace("new pan data-->"+strpinValidationData);
		}
		 trace("asdfasfasfsaf"+strpinValidationData);
		/*trace("asdfasfasfasf"+strpinValidationData.charAt(10));
		if(strpinValidationData.charAt(10) == 'N' && strpinValidationData.length() == 11){
			trace("dssssss");
			strpinValidationData += chn.substring(chn.length()-1);
			trace("lastindex00dsdsa"+strpinValidationData);
		}
		
		int npinValidationData = strpinValidationData.length();
		
		if(npinValidationData != 12)
		{
			trace("PVK generation data length is not 12"+npinValidationData);
			//strpinValidationData +=  MaskBlock.substring(0,12-npinValidationData);  //to date some value here
			strpinValidationData +="0000";
		}
		if(strpinValidationData.length() != 12)
		{
			trace("pin validation data length is not 12");
		}*/
			
			
		//strpinValidationData +="0000";
		trace("strpinValidationData11  "+ strpinValidationData);
		
		ByteArrayOutputStream racalpinoffset = generateRacalPinOffset(EPVK,rand_pinvalue,PinLen,pan,DecimalizationTable,strpinValidationData);
		
		trace("pinoffset value"+racalpinoffset);
		out.write(racalpinoffset.toByteArray());
		out.flush();
		byte[] pinoffsetresponse = new byte[23];
		in.read(pinoffsetresponse);		
		trace("getting response from");
	    rsp = new String (  pinoffsetresponse );
	    convrsp = rsp.trim();
	    trace("convrsp values is"+convrsp);
		String pinoffsetvalue = getRacalPinOffsetResponse(convrsp);
		//pinoffsetvalue += "FFFFFFFF";
		trace("the output came from for pin"+pinoffsetvalue);
		racalpinoffset.flush();
		
		/*ByteArrayOutputStream racalencryptpinoffset = generateRacalEncryptPinOffset(EPVK,pinoffsetvalue,PinLen,pan,DecimalizationTable,strpinValidationData);
		trace("pinoffset value"+racalencryptpinoffset);
		out.write(racalencryptpinoffset.toByteArray());
		out.flush();
		byte[] encryptpinoffsetresponse = new byte[23];
		in.read(encryptpinoffsetresponse);		
		trace("getting response from encrypted pi");
	    rsp = new String (  encryptpinoffsetresponse );
	    trace("fsadffasf"+rsp);
	    convrsp = rsp.trim();
	    trace("getting response value form "+convrsp);
		String encryptpinoffsetvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from "+encryptpinoffsetvalue);
		racalencryptpinoffset.flush();
		
		//Encrypt pinblock
		String pinblockval = "HMAS";
		pinblockval += "JG";
		pinblockval += "U";
		pinblockval += "387D6880CD595458B21EA5585A581637";
		pinblockval += "01";
		pinblockval += pan;
		pinblockval += rand_pinvalue;
		trace("formed pinblockval--->"+pinblockval);
		
		ByteArrayOutputStream racalencryptpinblock = generateRacalEncryptPinBlock(pinblockval);
		trace("pinoffset value JG--->"+racalencryptpinblock);
		out.write(racalencryptpinblock.toByteArray());
		out.flush();
		byte[] encryptpinblockresponse = new byte[28];
		in.read(encryptpinblockresponse);		
		trace("getting response from encrypted JG");
	    rsp = new String (  encryptpinblockresponse );
	    trace("encrypted pinblock--->"+rsp);
	    retpinblock = rsp.trim();
	    trace("getting response value form JG--->"+retpinblock);
		String encryptpinblockvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from JG --->"+encryptpinblockvalue);
		racalencryptpinblock.flush();
		///end encrypt pinblock
		
		//Encrypt clearpin
		String clearpinval = "HMAS";
		clearpinval += "NG";
		clearpinval += pan;
		clearpinval += rand_pinvalue;
		trace("formed clearpinval--->"+clearpinval);
		
		ByteArrayOutputStream genclearpinval = getClearPinVal(clearpinval);
		trace("clearpinval value NG--->"+genclearpinval);
		out.write(genclearpinval.toByteArray());
		out.flush();
		byte[] clearpinvalresponse = new byte[28];
		in.read(clearpinvalresponse);		
		trace("getting response from encrypted NG clearpinval");
	    rsp = new String (  clearpinvalresponse );
	    trace("clearpinval--->"+rsp);
	    clearpinresponse = rsp.trim();
	    trace("getting response value form NG clearpinval--->"+clearpinresponse);
		String encryptpinblockvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from JG --->"+encryptpinblockvalue);
	    genclearpinval.flush();
		///end encrypt pinblock
*/		
		
		trace("************Pin Mailer Data Start***********************");
		String pinmailerdata = gettingPinMailerDetails(gentype, cin, commondesc, jdbctemplate, chn, mcard, instid,
				hsmobj, productcode, subproduct);
		trace("Pin Mailer Data is::" + pinmailerdata);
		String generatepinmailer = generatePinOffPinMailer(pan, rand_pinvalue, pinmailerdata);
		trace("Generated Pin Mailer Data is::" + generatepinmailer);

		ByteArrayOutputStream pinmailerhex = generatePinMailerDataDetails(generatepinmailer);
		trace("Pin Mailer Hex Value :::" + pinmailerhex);
		out.write(pinmailerhex.toByteArray());
		out.flush();
		byte[] pinmailerhexresponse = new byte[23];
		in.read(pinmailerhexresponse);
		trace("Response From HSM for Pin Mailer...");
		rsp = new String(pinmailerhexresponse);
		convrsp = rsp.trim();
		trace("Before Pin Printing Response as:::" + convrsp);
		String pinmailerhexresponsevalue = getRacalPrinterDataResponse(convrsp);
		trace("Pin Mailer Response is:::" + pinmailerhexresponsevalue);
		String respmsg = "";
		if (pinmailerhexresponsevalue.equals("00")) {
			Thread.sleep(5000);
			byte[] pinmailerhexafterresponse = new byte[23];
			in.read(pinmailerhexafterresponse);
			trace("Getting Response from HSM for After Pin Printing");
			rsp = new String(pinmailerhexafterresponse);
			convrsp = rsp.trim();
			trace("After Pin Printing response as:::" + convrsp);
			String pinmailerhexafterresponsevalue = "00";// getRacalPrinterDataAfterResponse(convrsp);
			trace("Pin Mailer Printer Response" + pinmailerhexafterresponsevalue);
			if (pinmailerhexafterresponsevalue.equals("00")) {
				trace("Pin Mailer generated successfully");
			} else {
				trace("After Pin Mailer Response Error:::" + pinmailerhexafterresponsevalue);
				respmsg = pinmailerhexafterresponsevalue;
				outputStream.reset();
				outputStream.write(respmsg.getBytes());
				//return outputStream;
			}

		} else {
			trace("Response Message got Mismatch or Null");
			respmsg = pinmailerhexresponsevalue;
			outputStream.reset();
			outputStream.write(respmsg.getBytes());
			convrsp = convrsp.substring(6,8);
			return convrsp+"~NA~"+respmsg;
		}

		// String pinval =
		// pinoffsetvalue.substring(0,Integer.parseInt(PinLen));
		String pinval = pinoffsetvalue.substring(0, 4);
		trace("pinval" + pinval);

		outputStream.reset();
		outputStream.write(pinval.getBytes());

		trace("pinmailerdata end.....");
	} catch (Exception e) {
		System.out.println("Exception Happened" + e.getMessage());
	}
		return outputStream+"~"+retpinblock+"~"+clearpinresponse;  
	} 
	
public String ComposeRacalPINMailerOtp( String instid,String EPVK, String CHN,String mcard, String cin, String productcode, String subproduct, String gentype, JdbcTemplate jdbctemplate, CommonDesc commondesc, ByteArrayOutputStream outputStream, HSMParameter hsmobj,DataInputStream in, DataOutputStream out  ) throws Exception {   
		
		System.out.println("instid-->"+instid); 
		System.out.println("EPVK-->"+EPVK);
		System.out.println("CHN-->"+CHN);
		System.out.println("CIN-->"+cin);
		System.out.println("productcode-->"+productcode);
		System.out.println("subproduct-->"+subproduct);
		System.out.println("gentype-->"+gentype);
		//System.out.println("CHN-->"+CHN);
		
		
		
		HsmTcpIp hsmtcp = new HsmTcpIp();
		String retpinblock = "NA" , clearpinresponse= "NA";
		try{
		int PanOffset = Integer.parseInt( hsmobj.PAN_OFFSET );
		trace("panoffset -->"+PanOffset);
		int  PanLength =Integer.parseInt( hsmobj.PANVALIDATION_LENGTH );
		String DecimalizationTable = hsmobj.DECIMILISATION_TABLE;
		String PinType = "01" ;
		String pinpadchar =  hsmobj.PANPADCHAR;
		String MaskBlock = "FFFFFFFFFFFFFFFFFFF";
		String PinLen = "04";
		String PinLineNo="";  //06   //11
		String PinColumnNo = ""; //36   //55
		
		String CHNLineNo = "10";
		String CHNColumnNo = "11";
	 
		
		String BankNameLineNo = "11";
		String BankNameColumnNo = "11";  
		String CustomerName = "12232";
		String CustomerNameLineNo="10";
		String CustomerNameColumnNo="0"; 
		String Address1="0";
		String Address1LineNo="0";
		String Address1ColumnNo="0"; 
		String Address2="0";
		String Address2LineNo="0";
		String Address2ColumnNo="0";
		 
		String Address3="0";
		String Address3LineNo="0";
		String Address3ColumnNo="0";
		
		 
		String Address4="0";
		String Address4LineNo="0";
		String Address4ColumnNo="0";
		String res="";
		String convrsp ="";
		String rsp="";
		
		//String chn = mcard;
		String chn = CHN;
		String pan = "";
		int len = chn.length();
		pan = chn.substring(len-13, len-1);
		trace("pan no :::"+pan);//762020000001
		
		trace("Generating the Formatted Data");
		
		
		  res = getHeaderField(instid,hsmobj.PINMAILER_ID,jdbctemplate,commondesc,productcode,subproduct,gentype,cin);
		ByteArrayOutputStream formatdata = generateFormattedData(res);
		out.write(formatdata.toByteArray());
		out.flush(); 
		byte[] formatresponse = new byte[18];
		in.read(formatresponse);		
		trace("getting response from");
		  rsp = new String (  formatresponse );
		 convrsp = rsp.trim();
		String format_data = getRacalFormatDataResponse(convrsp);
		trace("the output came from "+format_data); 
		
		trace("gegerating random pin inputs");
		
		
		trace("Generating theRANDOM PIN");
		String pad = "00";
		String val = "HMAS";
		val += "JA";
		System.out.println("headers-->"+val);
		val += pan;
		System.out.println("with pan number-->"+val);
		val += PinLen;
		System.out.println("with pin length-->"+val);
		
		trace("sending values to JA command " + val);
		System.out.println("final JA COMMAND INPUT-->"+val);
		
		outputStream.write(val.length());
		byte dd[] = outputStream.toByteArray();
	    for (int i = 0; i < dd.length; i++) {
	      System.out.print(+(char) dd[i]);
	      
	    }
		//outputStream.write(PinLen.getBytes());
		trace("dfasfsh"+hsmtcp.byteToHexString(outputStream.toByteArray()));
		String convlen = hsmtcp.byteToHexString(outputStream.toByteArray());
		trace("convlen"+convlen);
		ByteArrayOutputStream bout  = new ByteArrayOutputStream();
		bout.write(pad.getBytes());
		bout.write(convlen.getBytes());
		byte cd[] = bout.toByteArray();
	    for (int i = 0; i < cd.length; i++) {
	      System.out.println(+(char) cd[i]);
	      
	    }
		String newdsf = new String(bout.toByteArray());
		trace("hex to ascii value"+newdsf);
	    	//String ascii = HsmTcpIp.hexToASCII(newdsf);
	    	
	    	String ascii= hsmtcp.hexToASCII(newdsf);
		trace("ascii value"+ascii);
		ByteArrayOutputStream pinhex  = new ByteArrayOutputStream();
		pinhex.write(ascii.getBytes());
		pinhex.write(val.getBytes());
	
		//bout.write(val.getBytes());
		byte c[] = pinhex.toByteArray();
	    for (int i = 0; i < c.length; i++) {
	      System.out.print(+(char) c[i]);
	      
	    }
		out.write(pinhex.toByteArray());
		out.flush(); 
		
		trace("adafasfsdf");
		byte[] response = new byte[30];
		in.read(response);		
		trace("getting response for JA");
		rsp = new String (  response );
	    convrsp = rsp.trim();
		String rand_pinvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from "+rand_pinvalue);
		bout.flush();
		
		
		trace("Generating Pin Offset");
		
		 String strpan = chn;//.substring(PanOffset);
			//String strpan = chn.substring(len-13, len-1);
			trace("strpan"+ strpan);
			String strcharN = chn.substring(chn.length() - 6, chn.length() - 1);
			trace("strcharN"+strcharN);
			int panData = strpan.length();
			String strpinValidationData= "";
			int ncharLoc;
			//strpinValidationData = chn.substring(0,10) + 'N' + chn.substring(chn.length() - 1);
			strpinValidationData = chn.substring(3,13) + 'N' + chn.substring(chn.length() - 1);
			
			
		
		/*String strpan = chn.substring(PanOffset);
		//String strpan = chn.substring(len-13, len-1);
		trace("strpan"+strpan);
		//String strcharN = chn.substring(chn.length() - 6, chn.length() - 1);
		String strcharN = "0000";
		trace("strcharN"+strcharN);
		int panData = strpan.length();
		String strpinValidationData= "";int ncharLoc;
		if(strpan.lastIndexOf(strcharN) < 1){
			
			
			strpinValidationData = chn.substring(0,10) + 'N' + chn.substring(chn.length() - 1);
		}
		if(strpan.length() == 12){
			trace("121212");
			if(strpan.substring(2,7) == strpan.substring(7, 12)){
				
				strpan = strpan.substring(0, 7);
				trace("test11");
				
				if(strpan.lastIndexOf(strcharN) > 1 && PanLength == panData )
				{
					if(strpan.substring(strpan.length()-6) == chn.substring(chn.length() - 6))
					{
						trace("test11");
						strpinValidationData = strpan.substring(0, 7)+'N'+chn.substring(chn.length()-1);
						
					}
					else
					{
						
						strpinValidationData = strpan.substring(0, 7)+'N';
						
					}
				}
			}
				else{
					ncharLoc = strpan.lastIndexOf(strcharN);
					trace("321312321");
					if(ncharLoc > 1 && PanLength == panData)
					{trace("dsgdsgdf");
						if(strpan.substring(strpan.length()-6) == chn.substring(chn.length()-6))
						{
							strpinValidationData = strpan.substring(0, ncharLoc)+'N'+chn.substring(chn.length()-1);
						}else
						{trace("4324324324");
							strpinValidationData  = strpan.substring(0, ncharLoc)+'N';
						}
					}
				}
			
			trace("423432");
			
		}else
		{    
			
			ncharLoc = strpan.lastIndexOf(strcharN);
			trace("sarrr"+ncharLoc);
					if(ncharLoc > 1 && PanLength == panData ){
						
						if(strpan.substring(strpan.length()-6) == chn.substring(chn.length()-6))
						{
							trace("sssss");
							strpinValidationData = strpan.substring(0, ncharLoc)+'N'+chn.substring(chn.length()-1);
							trace("111111111"+strpinValidationData);
						}else
						{
							trace("sardar test");
							strpinValidationData  = strpan.substring(3, 10)+'N';
							trace("222222"+strpinValidationData);
						}
					}
					strpinValidationData  = strpan.substring(3, 10)+'N';
					trace("new pan data-->"+strpinValidationData);
		}
		 trace("asdfasfasfsaf"+strpinValidationData);
		/*trace("asdfasfasfasf"+strpinValidationData.charAt(10));
		if(strpinValidationData.charAt(10) == 'N' && strpinValidationData.length() == 11){
			trace("dssssss");
			strpinValidationData += chn.substring(chn.length()-1);
			trace("lastindex00dsdsa"+strpinValidationData);
		}
		
		int npinValidationData = strpinValidationData.length();
		
		if(npinValidationData != 12)
		{
			trace("PVK generation data length is not 12"+npinValidationData);
			//strpinValidationData +=  MaskBlock.substring(0,12-npinValidationData);  //to date some value here
			strpinValidationData +="0000";
		}
		if(strpinValidationData.length() != 12)
		{
			trace("pin validation data length is not 12");
		}*/
			
			
		//strpinValidationData +="0000";
		trace("strpinValidationData11  "+ strpinValidationData);
		
		ByteArrayOutputStream racalpinoffset = generateRacalPinOffset(EPVK,rand_pinvalue,PinLen,pan,DecimalizationTable,strpinValidationData);
		
		trace("pinoffset value"+racalpinoffset);
		out.write(racalpinoffset.toByteArray());
		out.flush();
		byte[] pinoffsetresponse = new byte[23];
		in.read(pinoffsetresponse);		
		trace("getting response from");
	    rsp = new String (  pinoffsetresponse );
	    convrsp = rsp.trim();
	    trace("convrsp values is"+convrsp);
		String pinoffsetvalue = getRacalPinOffsetResponse(convrsp);
		//pinoffsetvalue += "FFFFFFFF";
		trace("the output came from for pin"+pinoffsetvalue);
		racalpinoffset.flush();
		
		ByteArrayOutputStream racalencryptpinoffset = generateRacalEncryptPinOffset(EPVK,pinoffsetvalue,PinLen,pan,DecimalizationTable,strpinValidationData);
		trace("pinoffset value"+racalencryptpinoffset);
		out.write(racalencryptpinoffset.toByteArray());
		out.flush();
		byte[] encryptpinoffsetresponse = new byte[23];
		in.read(encryptpinoffsetresponse);		
		trace("getting response from encrypted pi");
	    rsp = new String (  encryptpinoffsetresponse );
	    trace("fsadffasf"+rsp);
	    convrsp = rsp.trim();
	    trace("getting response value form "+convrsp);
		String encryptpinoffsetvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from "+encryptpinoffsetvalue);
		racalencryptpinoffset.flush();
		
		//Encrypt pinblock
		String pinblockval = "HMAS";
		pinblockval += "JG";
		pinblockval += "U";
		pinblockval += "387D6880CD595458B21EA5585A581637";
		pinblockval += "01";
		pinblockval += pan;
		pinblockval += rand_pinvalue;
		trace("formed pinblockval--->"+pinblockval);
		
		ByteArrayOutputStream racalencryptpinblock = generateRacalEncryptPinBlock(pinblockval);
		trace("pinoffset value JG--->"+racalencryptpinblock);
		out.write(racalencryptpinblock.toByteArray());
		out.flush();
		byte[] encryptpinblockresponse = new byte[28];
		in.read(encryptpinblockresponse);		
		trace("getting response from encrypted JG");
	    rsp = new String (  encryptpinblockresponse );
	    trace("encrypted pinblock--->"+rsp);
	    retpinblock = rsp.trim();
	    trace("getting response value form JG--->"+retpinblock);
		String encryptpinblockvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from JG --->"+encryptpinblockvalue);
		racalencryptpinblock.flush();
		///end encrypt pinblock
		
		//Encrypt clearpin
		String clearpinval = "HMAS";
		clearpinval += "NG";
		clearpinval += pan;
		clearpinval += rand_pinvalue;
		trace("formed clearpinval--->"+clearpinval);
		
		ByteArrayOutputStream genclearpinval = getClearPinVal(clearpinval);
		trace("clearpinval value NG--->"+genclearpinval);
		out.write(genclearpinval.toByteArray());
		out.flush();
		byte[] clearpinvalresponse = new byte[28];
		in.read(clearpinvalresponse);		
		trace("getting response from encrypted NG clearpinval");
	    rsp = new String (  clearpinvalresponse );
	    trace("clearpinval--->"+rsp);
	    clearpinresponse = rsp.trim();
	    trace("getting response value form NG clearpinval--->"+clearpinresponse);
		encryptpinblockvalue = getRacalRandomPinResponse(convrsp);
		trace("the output came from JG --->"+encryptpinblockvalue);
	    genclearpinval.flush();
		///end encrypt pinblock
		
		
		/*trace("************Pin Mailer Data Start***********************");
		String pinmailerdata = gettingPinMailerDetails(gentype, cin, commondesc, jdbctemplate, chn, mcard, instid,
				hsmobj, productcode, subproduct);
		trace("Pin Mailer Data is::" + pinmailerdata);
		String generatepinmailer = generatePinOffPinMailer(pan, rand_pinvalue, pinmailerdata);
		trace("Generated Pin Mailer Data is::" + generatepinmailer);

		ByteArrayOutputStream pinmailerhex = generatePinMailerDataDetails(generatepinmailer);
		trace("Pin Mailer Hex Value :::" + pinmailerhex);
		out.write(pinmailerhex.toByteArray());
		out.flush();
		byte[] pinmailerhexresponse = new byte[23];
		in.read(pinmailerhexresponse);
		trace("Response From HSM for Pin Mailer...");
		rsp = new String(pinmailerhexresponse);
		convrsp = rsp.trim();
		trace("Before Pin Printing Response as:::" + convrsp);
		String pinmailerhexresponsevalue = getRacalPrinterDataResponse(convrsp);
		trace("Pin Mailer Response is:::" + pinmailerhexresponsevalue);
		String respmsg = "";
		if (pinmailerhexresponsevalue.equals("00")) {
			Thread.sleep(5000);
			byte[] pinmailerhexafterresponse = new byte[23];
			in.read(pinmailerhexafterresponse);
			trace("Getting Response from HSM for After Pin Printing");
			rsp = new String(pinmailerhexafterresponse);
			convrsp = rsp.trim();
			trace("After Pin Printing response as:::" + convrsp);
			String pinmailerhexafterresponsevalue = "00";// getRacalPrinterDataAfterResponse(convrsp);
			trace("Pin Mailer Printer Response" + pinmailerhexafterresponsevalue);
			if (pinmailerhexafterresponsevalue.equals("00")) {
				trace("Pin Mailer generated successfully");
			} else {
				trace("After Pin Mailer Response Error:::" + pinmailerhexafterresponsevalue);
				respmsg = pinmailerhexafterresponsevalue;
				outputStream.reset();
				outputStream.write(respmsg.getBytes());
				//return outputStream;
			}

		} else {
			trace("Response Message got Mismatch or Null");
			respmsg = pinmailerhexresponsevalue;
			outputStream.reset();
			outputStream.write(respmsg.getBytes());
			//return outputStream;
		}*/

		// String pinval =
		// pinoffsetvalue.substring(0,Integer.parseInt(PinLen));
		String pinval = pinoffsetvalue.substring(0,Integer.parseInt(PinLen));
		trace("pinval" + pinval);

		outputStream.reset();
		outputStream.write(pinval.getBytes());

		trace("pinmailerdata end.....");
	} catch (Exception e) {
		System.out.println("Exception Happened" + e.getMessage());
	}
		return outputStream+"~"+retpinblock+"~"+clearpinresponse;  
	} 
	

	/*
	 * pinval = pinoffsetvalue.substring(0,4); trace("pinval_test"+pinval);
	 * 
	 * outputStream.reset(); outputStream.write(pinval.getBytes());
	 * 
	 * trace("pinmailerdata end.....");
	 * 
	 * 
	 * }catch(Exception e) { System.out.println("Exception Happened"
	 * +e.getMessage()); } return outputStream; }
	 */

	private String generatePinOffPinMailer(String pan, String rand_pinvalue, String pinmailerdata) {
		HsmTcpIp hsmtcp = new HsmTcpIp();
		String bufferval = "";
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			bufferval += "C";
			bufferval += pan;
			bufferval += rand_pinvalue;
			bufferval += pinmailerdata;
		} catch (Exception e) {
			System.out.println("Exception Happened" + e.getMessage());
		}
		return bufferval;
	}

	private ByteArrayOutputStream generatePinMailerDataDetails(String pinmailerdata) {
		HsmTcpIp hsmtcp = new HsmTcpIp();

		String bufferval = "";
		String pad = "00";
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			bufferval += "HMAS";
			bufferval += "PE";
			bufferval += pinmailerdata;

			trace("Length of buffer value ::: " + bufferval.length() + "bufferval" + bufferval);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(bufferval.length());
			String pinmailerhexdet = hsmtcp.byteToHexString(outputStream.toByteArray());
			trace("Byte To Hex String:::" + pinmailerhexdet);
			ByteArrayOutputStream newhex = new ByteArrayOutputStream();
			newhex.write(pad.getBytes());
			newhex.write(pinmailerhexdet.getBytes());
			String pinoff = new String(newhex.toByteArray());
			trace("pi off" + pinoff);
			String hexoffset = hsmtcp.hexToASCII(pinoff);
			trace("Hex To ASCII Value is:::" + hexoffset);
			bout.write(hexoffset.getBytes());
			bout.write(bufferval.getBytes());
		} catch (Exception e) {
			trace("Exception Happened" + e.getMessage());
		}

		return bout;
	}

	private String gettingPinMailerDetails(String gentype, String cin, CommonDesc commondesc, JdbcTemplate jdbctemplate,
			String chn, String mcard, String instid, HSMParameter hsmobj, String productcode, String subproduct) {

		String result = "";
		String data = "";
		try {
			String pinmailerqry = "select trim(FIELD_NAME) as FIELD_NAME, trim(FIELD_LENGTH) AS FIELD_LENGTH , TRIM(X_POS) AS X_POS, TRIM(Y_POS) AS Y_POS from PINMAILER_PROPERTY WHERE INST_ID='"
					+ instid + "' AND PINMAILER_ID='" + hsmobj.PINMAILER_ID
					+ "' AND PRINT_REQUIRED='Y'  ORDER BY PRINTORDER ";
			enctrace("pinmailerqry :" + pinmailerqry);
			List pinmailerlist = jdbctemplate.queryForList(pinmailerqry);
			if (!pinmailerlist.isEmpty()) {
				Iterator itr = pinmailerlist.iterator();
				String fldkey = null;

				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					fldkey = (String) mp.get("FIELD_NAME");
					trace("Processing...fieldkye :" + fldkey);

					if (fldkey.equals("CARDNO")) {
						// data = loadMailerData("CHN",gentype);
						if (data != null) {
							data = mcard + ";";
						} else {
							data = ";";
						}
						result = result + data;
					}
					if (fldkey.equals("CUSTNAME")) {
						data = commondesc.getCustomerNameByProces(instid, productcode, subproduct, cin, gentype,
								jdbctemplate);
						if (data == null || data.equals("")) {
							data = commondesc.getCustomerNameByProduction(instid, productcode, subproduct, cin, gentype,
									jdbctemplate);
						}
						if (data != null) {
							data = data + ";";
						} else {
							data = ";";
						}
						result = result + data;
					}

					if (fldkey.equals("ADDRESSONE")) {
						data = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR1",
								jdbctemplate);
						if (data != null) {
							data = data + ";";
						} else {
							data = ";";
						}
						result = result + data;
					}

					if (fldkey.equals("PINNO")) {
						data = "";
						result = result + data;
					}
					if (fldkey.equals("BANKNAME")) {
						data = commondesc.getInstDesc(instid, jdbctemplate);
						;
						if (data != null) {
							data = data + ";";
						} else {
							data = ";";
						}
						result = result + data;
					}

					if (fldkey.equals("ADDRESSTWO")) {
						data = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR2",
								jdbctemplate);
						if (data != null) {
							data = data + ";";
						} else {
							data = ";";
						}
						result = result + data;
					}
					if (fldkey.equals("ADDRESSTHREE")) {
						data = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR3",
								jdbctemplate);
						if (data != null) {
							data = data + ";";
						} else {
							data = ";";
						}
						result = result + data;
					}
					if (fldkey.equals("ADDRESSFOUR")) {
						data = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR4",
								jdbctemplate);
						if (data != null) {
							data = data + ";";
						} else {
							data = ";";
						}
						result = result + data;

					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception Happened" + e.getMessage());
		}

		return result;
	}

	private ByteArrayOutputStream generateFormattedData(String res) {

		HsmTcpIp hsmtcp = new HsmTcpIp();
		ByteArrayOutputStream formdata = new ByteArrayOutputStream();
		try {
			String pad = "00";
			String val = "HMAS";
			val += "PA";
			val += res;
			// val +=
			// ">L>L>L>L>L>L>L>L>L>L>L>L>L>L>014^0>L>015^1>L>016^2>L>017^3>017^4>017^5>017^6>L>018^P>L>020^V>L>L>L>L>L>";
			//trace("Pinmailer Request is :: "+val);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			outStream.write(val.length());
			String convlen = hsmtcp.byteToHexString(outStream.toByteArray());
			trace("Byte to Hex String:::" + convlen);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(pad.getBytes());
			bout.write(convlen.getBytes());
			String hexascii = new String(bout.toByteArray());
			trace("Hex Ascii Value :::" + hexascii);
			String newval = hsmtcp.hexToASCII(hexascii);
			formdata.write(newval.getBytes());
			formdata.write(val.getBytes());
		} catch (Exception e) {
			System.out.println("Exception Happened" + e.getMessage());
		}
		return formdata;
	}

	public String getLineFeed(int num) {
		if (num > 0) {
			String linefeed = "";
			for (int i = 0; i < num; i++) {
				linefeed += ">L";
			}
			return linefeed;
		} else {
			return "";
		}
	}

	private String getHeaderField(String instid, String pINMAILER_ID, JdbcTemplate jdbctemplate, CommonDesc commondesc,
			String productcode, String subproduct, String gentype, String cin) {

		Boolean pinconfigured = false;
		// String endline = ">L>L>L>L>L>L>L>";

		//String endline = ">L>040^V>L>L>L>L>L>L>L";
		// String  endline= ">L>041^V>L>L>L>L";
	      String endline = ">L>053^V>L>L>L>L>L>L>L";
		String result = "";
		try {

			String PinLineNo = ""; // 06 //11
			String PinColumnNo = ""; // 36 //55

			String CHNLineNo = "10";
			String CHNColumnNo = "11";

			String BankNameLineNo = "11";
			String BankNameColumnNo = "11";
			String CustomerName = "12232";
			String CustomerNameLineNo = "10";
			String CustomerNameColumnNo = "0";
			String Address1 = "0";
			String Address1LineNo = "0";
			String Address1ColumnNo = "0";
			String Address2 = "0";
			String Address2LineNo = "0";
			String Address2ColumnNo = "0";

			String Address3 = "0";
			String Address3LineNo = "0";
			String Address3ColumnNo = "0";

			String Address4 = "0";
			String Address4LineNo = "0";
			String Address4ColumnNo = "0";

			String pinmailerqry = "select trim(FIELD_NAME) as FIELD_NAME, trim(FIELD_LENGTH) AS FIELD_LENGTH , TRIM(X_POS) AS X_POS, TRIM(Y_POS) AS Y_POS from PINMAILER_PROPERTY WHERE INST_ID='"
					+ instid + "' AND PINMAILER_ID='" + pINMAILER_ID + "' AND PRINT_REQUIRED='Y'  ORDER BY PRINTORDER ";
			enctrace("pinmailerqry: :: " + pinmailerqry);
			List pinmailerlist = jdbctemplate.queryForList(pinmailerqry);
			if (!pinmailerlist.isEmpty()) {
				Iterator itr = pinmailerlist.iterator();
				String fldkey = null;
				String pinlinefeed = "", chnlinefeed = "", addr1linefeed = "", banklinefeed = "", custlinefeed = "",
						addr2linefeed = "", addr3linefeed = "", addr4linefeed = "";

				int repeat = pinmailerlist.size() - 1;
				int comval = 0;
				int newcomval = 0;
				int base = 0;

				while (itr.hasNext()) {

					Map mp = (Map) itr.next();
					fldkey = (String) mp.get("FIELD_NAME");
					trace("Processing...fieldkye : " + fldkey);

					if (fldkey.equals("CARDNO")) {
						CHNLineNo = (String) mp.get("X_POS");
						trace("asfdasf" + CHNLineNo);
						CHNColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						trace("cardcolumnno" + CHNColumnNo);
						comval = Integer.parseInt(CHNLineNo) - newcomval;
						newcomval = newcomval + comval;
						trace("newconval" + newcomval);
						chnlinefeed = getLineFeed(comval) + ">" + CHNColumnNo + "^" + base;
						trace("cardchecking" + chnlinefeed);
						result = result + chnlinefeed;
						trace("result fieed" + result);

					}

					else if (fldkey.equals("CUSTNAME")) {

						CustomerNameLineNo = (String) mp.get("X_POS");
						CustomerNameColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						CustomerName = commondesc.getCustomerNameByProces(instid, productcode, subproduct, cin, gentype,
								jdbctemplate);
						if (CustomerName == null || CustomerName.equals("")) {
							CustomerName = commondesc.getCustomerNameByProduction(instid, productcode, subproduct, cin,
									gentype, jdbctemplate);
						}
						int customernamelen = CustomerName.length();

						trace("CustomerNameColumnNo i.e Y positon pad values ::: "+CustomerNameColumnNo +"     CustomerNameLineNo i.e X position values :::  "+CustomerNameLineNo);
						comval = Integer.parseInt(CustomerNameLineNo) - newcomval;
						newcomval = newcomval + comval;
						custlinefeed = getLineFeed(comval) + ">" + CustomerNameColumnNo + "^" + base;
						result = result + custlinefeed;
						trace("custname is CUSTNAME" + result);

					}

					else if (fldkey.equals("ADDRESSONE")) {
						Address1LineNo = (String) mp.get("X_POS");
						Address1ColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						Address1 = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR1",
								jdbctemplate);
						int Address1Len = Address1.length();
						
						trace("Address1ColumnNo i.e Y positon pad values ::: "+Address1ColumnNo +"     Address1LineNo i.e X position values :::  "+Address1LineNo);	
						comval = Integer.parseInt(Address1LineNo) - newcomval;
						newcomval = newcomval + comval;
						addr1linefeed = getLineFeed(comval) + ">" + Address1ColumnNo + "^" + base;
						result = result + addr1linefeed;

						trace("address positon result  :::"+result);
					}

					if (fldkey.equals("PINNO")) {
						PinLineNo = (String) mp.get("X_POS");
						PinColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						comval = Integer.parseInt(PinLineNo) - newcomval;
						trace("PinColumnNo i.e Y positon pad values ::: "+PinColumnNo +"     PinLineNo i.e X position values :::  "+PinLineNo);
						newcomval = newcomval + comval;
						pinlinefeed = getLineFeed(comval) + ">" + PinColumnNo + "^P";
						result = result + pinlinefeed;
						pinconfigured = true;
						
						trace("pin postion result is :"+result);
					}

					else if (fldkey.equals("BANKNAME")) {
						BankNameLineNo = (String) mp.get("X_POS");
						BankNameColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						comval = Integer.parseInt(BankNameLineNo) - newcomval;
						newcomval = newcomval + comval;
						banklinefeed = getLineFeed(comval) + ">" + BankNameColumnNo + "^" + base;
						result = result + banklinefeed;

					}

					else if (fldkey.equals("ADDRESSTWO")) {
						Address2LineNo = (String) mp.get("X_POS");
						Address2ColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						Address2 = commondesc.getAddress1(instid, productcode, subproduct, cin, gentype, "$ADDR2",
								jdbctemplate);
						int Address2Len = Address2.length();
						comval = Integer.parseInt(CustomerNameLineNo) - newcomval;
						newcomval = newcomval + comval;
						addr2linefeed = getLineFeed(comval) + ">" + CustomerNameColumnNo + "^" + base;
						result = result + addr2linefeed;
						System.out.println("ADDRESSTWO===");
						trace("Address2ColumnNo i.e Y positon pad values ::: "+Address2ColumnNo +"     Address2LineNo i.e X position values :::  "+Address2LineNo);

					}

					else if (fldkey.equals("ADDRESSTHREE")) {
						Address3LineNo = (String) mp.get("X_POS");
						Address3ColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						Address3 = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR3",
								jdbctemplate);
						int Address3Len = Address3.length();
						comval = Integer.parseInt(CustomerNameLineNo) - newcomval;
						newcomval = newcomval + comval;
						addr3linefeed = getLineFeed(comval) + ">" + CustomerNameColumnNo + "^" + base;
						result = result + addr3linefeed;

					}

					else if (fldkey.equals("ADDRESSFOUR")) {
						Address4LineNo = (String) mp.get("X_POS");
						Address4ColumnNo = commondesc.paddingZero((String) mp.get("Y_POS"), 3);
						Address4 = commondesc.getAddress(instid, productcode, subproduct, cin, gentype, "$ADDR4",
								jdbctemplate);

						int Address4Len = Address4.length();
						comval = Integer.parseInt(CustomerNameLineNo) - newcomval;
						newcomval = newcomval + comval;
						addr4linefeed = getLineFeed(comval) + ">" + CustomerNameColumnNo + "^" + base;
						result = result + addr4linefeed;

					}

					base = base + 1;

					/*
					 * else if( fldkey.equals("CUSTID") ){ CustomerIdLineNo =
					 * (String)mp.get("X_POS"); CustomerIdColumnNo=
					 * (String)mp.get("Y_POS"); if( !gentype.equals("INSTANT")){
					 * cin = "--"; } int customeridlen = cin.length();
					 * outputStream = eracom_out( CustomerIdLineNo,
					 * CustomerIdLineNo.length(), "CUSTOMER-ID", 2,
					 * outputStream); outputStream = eracom_out(
					 * CustomerIdColumnNo,CustomerIdColumnNo.length(),
					 * "CUSTOMER-ID-COLUMN-NO", 2, outputStream); outputStream =
					 * eracom_out( Integer.toString(customeridlen),
					 * Integer.toString(customeridlen).length(),
					 * "CUSTOMER-ID-LEN", 2, outputStream); outputStream =
					 * eracom_out( cin, cin.length(), "CUSTOMER-ID", 3,
					 * outputStream); }
					 */
				}

				if (!pinconfigured) {
					trace("!!!!!!!!!!!!!!!!Pin Line or Coloumn not configured!!!!!!!!");
					addActionError("Pin Line or Coloumn not configured");
				}

			}
		} catch (Exception e) {
			System.out.println("Exception Happened" + e.getMessage());
		}
		trace("result....." + result);
		return result + endline;
		//return result;
	}

	private ByteArrayOutputStream generateRacalPinOffset(String ePVK, String rand_pinvalue, String pinLen, String pan,
			String decimalizationTable, String strpinValidationData) {
		HsmTcpIp hsmtcp = new HsmTcpIp();

		// VHMASDEUE577B792255F836B593E528411ADB55C637130476202000000101234567890123455077620200N
		// + (Card Last Digit number)

		System.out.println("inside--pinoffset generation mtd");
		System.out.println("ePVK-->"+ePVK);
		System.out.println("rand_pinvalue-->"+rand_pinvalue);
		System.out.println("pinLen-->"+pinLen);
		System.out.println("pan-->"+pan);
		System.out.println("decimalizationTable-->"+decimalizationTable);
		System.out.println("strpinValidationData-->"+strpinValidationData);
		
		String bufferval = "";
		String pad = "00";
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			bufferval += "HMAS";
			bufferval += "DE";
			if (ePVK.length() == 16)
				bufferval += ePVK;
			else if (ePVK.length() == 32)
				bufferval += "U" + ePVK;
			// bufferval += ePVK;
			else if (ePVK.length() == 48)
				bufferval += "T" + ePVK;
			else
				trace("Error in ePVK Length:: " + ePVK.length());

			System.out.println("inside evpk value 32 & before adding rand_pin values-->"+ePVK);
			
			bufferval += rand_pinvalue;
			System.out.println("after adding random pin value-->"+bufferval);
			bufferval += pinLen;
			System.out.println("after adding  pin lenght-->"+bufferval);
			bufferval += pan;
			System.out.println("after adding pan data-->"+bufferval);
			bufferval += decimalizationTable;
			System.out.println("after adding decimalizationTable-->"+bufferval);
			bufferval += strpinValidationData;
			System.out.println("after adding strpinValidationData-->"+bufferval);
			trace("Buffer value for pinoffset ::: " + bufferval);
			 System.out.println("final buffer value"+bufferval);
			trace("Length of buffer value ::: " + bufferval.length());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(bufferval.length());
			String pinoffsethex = hsmtcp.byteToHexString(outputStream.toByteArray());
			trace("Byte To Hex String :::" + pinoffsethex);
			ByteArrayOutputStream newhex = new ByteArrayOutputStream();
			newhex.write(pad.getBytes());
			newhex.write(pinoffsethex.getBytes());
			String pinoff = new String(newhex.toByteArray());
			trace("Pin Off Value is" + pinoff);
			String hexoffset = hsmtcp.hexToASCII(pinoff);
			trace("Hex to ASCII value ::" + hexoffset);
			bout.write(hexoffset.getBytes());
			bout.write(bufferval.getBytes());
		} catch (Exception e) {
			trace("Exception Happened" + e.getMessage());
		}

		return bout;
	}

	private ByteArrayOutputStream generateRacalEncryptPinOffset(String ePVK, String pinoffsetvalue, String pinLen,
			String pan, String decimalizationTable, String strpinValidationData) {
		HsmTcpIp hsmtcp = new HsmTcpIp();

		String bufferval = "";
		String pad = "00";
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			bufferval += "HMAS";
			bufferval += "EE";

			if (ePVK.length() == 16)
				bufferval += ePVK;
			else if (ePVK.length() == 32)
				bufferval += "U" + ePVK;
			else if (ePVK.length() == 48)
				bufferval += "T" + ePVK;
			else
				trace("Getting error in ePVK Length" + ePVK.length());

			bufferval += pinoffsetvalue;
			bufferval += pinLen;
			bufferval += pan;
			bufferval += decimalizationTable;
			bufferval += strpinValidationData;

			trace("Buffer value for pinoffset ::: " + bufferval);
			trace("Length of buffer value ::: " + bufferval.length());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(bufferval.length());
			String pinoffsethex = hsmtcp.byteToHexString(outputStream.toByteArray());
			trace("Byte to Hex String Value::" + pinoffsethex);
			ByteArrayOutputStream newhex = new ByteArrayOutputStream();
			newhex.write(pad.getBytes());
			newhex.write(pinoffsethex.getBytes());
			String pinoff = new String(newhex.toByteArray());
			trace("pi off" + pinoff);
			String hexoffset = hsmtcp.hexToASCII(pinoff);
			trace("Hex to Ascii value:::" + hexoffset);
			bout.write(hexoffset.getBytes());
			bout.write(bufferval.getBytes());
		} catch (Exception e) {
			trace("Exception Happened" + e.getMessage());
		}

		return bout;
	}

	public ByteArrayOutputStream composeCvkData(String CVK, ByteArrayOutputStream outputStream) throws IOException {
		String ReqBuffer = "";
		int cvk_len = CVK.length();

		int cvklen = (cvk_len / 2) + 1;
		String Len = Integer.toString(cvklen);

		eracom_out(Len, 1, "cvk Length", 2, outputStream);
		if (cvk_len == 16) {
			ReqBuffer += eracom_out("10", 2, "size", 1, outputStream);
		} else if (cvk_len == 32) {
			ReqBuffer += eracom_out("11", 2, "size", 1, outputStream);
		} else {
			ReqBuffer += eracom_out("12", 2, "size", 1, outputStream);
		}

		eracom_out(CVK, 32, "CVK", 1, outputStream);
		return outputStream;
	}

	public static String fillwithZero(String CVVData) {
		String zerostr = "";

		for (int i = 0; i < 32 - (CVVData.length()); i++) {
			zerostr = zerostr + "0";
		}
		return zerostr;
	}

	public String ComposeCVVLength(String CVK, String CHN, String Expiry, String ServiceCode,
			ByteArrayOutputStream outputStream) throws IOException {
		String ReqBuffer = "";
		String CVVData = CHN + Expiry + ServiceCode;
		trace("CVV DATA " + CVVData + " Length " + CVVData.length());

		ReqBuffer += eracom_out("EE0802", 6, "Command", 1, outputStream);
		ReqBuffer += eracom_out("00", 2, "FUNCTION-MODIFIER", 1, outputStream);

		ReqBuffer += composeCvkData(CVK, outputStream);

		if (CVVData.length() <= 32) {
			CVVData = CVVData + fillwithZero(CVVData);
		}
		trace("After zero : " + CVVData);

		trace("FINAL CVV DATA leng 0 : " + ReqBuffer.length());
		ReqBuffer += eracom_out(CVVData, 32, "CVV - Data", 1, outputStream);
		trace("FINAL CVV DATA : " + ReqBuffer);
		trace("FINAL CVV DATA leng : " + ReqBuffer.length());
		return ReqBuffer;
	}

	public ByteArrayOutputStream ComposeCVV(String CVK, String CHN, String Expiry, String ServiceCode,
			ByteArrayOutputStream outputStream) throws IOException {

		String CVVData = CHN + Expiry + ServiceCode;
		trace("CVV DATA " + CVVData + " Length " + CVVData.length());
		eracom_out("EE0802", 6, "Command", 1, outputStream);
		eracom_out("00", 2, "FUNCTION-MODIFIER", 1, outputStream);
		composeCvkData(CVK, outputStream);
		if (CVVData.length() <= 32) {
			CVVData = CVVData + fillwithZero(CVVData);
		}
		eracom_out(CVVData, 32, "CVV - Data", 1, outputStream);

		trace("cvv.............");
		// trace( Hex.encodeHex( outputStream.toByteArray() ) );
		trace("cvv.............");

		return outputStream;
	}

	public void converttoString(String msg) {
		String result = eracom_in(msg, msg.length(), "COMMAND", 1);
		trace("Command is : " + result);
	}

	public static String eracom_in(String msg, int size, String comm, int conv_type) {
		String re = "";
		int offsetsize = 0;
		offsetsize = size;

		if (conv_type == 1) {
			re = bcd_to_ascii(msg, size);
		} else if (conv_type == 2) {
			re = "DO";
		} else if (conv_type == 3) {
			re = msg;
		}
		return re;
	}

	public static String bcd_to_ascii(String msg, int size) {
		String res = "";
		String value = "";
		int i = 0;
		int lvalue = 0, rvalue = 0;
		char[] toprocess_token = msg.toCharArray();
		while (value.length() < size) {
			lvalue = (((int) toprocess_token[i]) & 0xf0) >> 4;
			if ((lvalue >= 0) && (lvalue <= 9))
				lvalue += 48;
			else
				lvalue += 87;

			rvalue = ((int) toprocess_token[i]) & 0x0f;
			if ((rvalue >= 0) && (rvalue <= 9))
				rvalue += 48;
			else
				rvalue += 87;
			if (((size % 2) != 0) && i == 0) {
				value = value + (char) rvalue;
				i = i + 1;
				continue;
			}
			i = i + 1;
			value = value + (char) lvalue + (char) rvalue;
		}

		return res = value;
	}

	// Eracon Original Value

	public String getCVVResponse(String res_onse) {
		String response = res_onse.substring(18, 24);
		trace("response is getting rom " + response);
		String fresp = response.substring(0, 2);
		trace("asdfasfsf" + fresp);
		String cvv = "-1";
		if (fresp.equals("00")) {
			trace("Test 4 : " + fresp);
			cvv = response.substring(2, 5);
			trace("sdafasfsf cvv value is" + cvv);
			trace("Gennerated cvv value is : " + cvv);
			if (cvv.length() < 3) {
				return "-1";
			}
		}
		return cvv;
	}

	// Racal Function getting cvv value

	public String getRacalCVVResponse(String res_onse) {
		HsmTcpIp hsmtcpip = new HsmTcpIp();
		String respcode = res_onse;
		trace("respcode" + respcode);
		String cvv = hsmtcpip.getRacalHSMResponse(respcode);
		trace("HSM Response Code::" + cvv);
		if (cvv.equals("00")) {
			trace("Test 4 : " + cvv);
			cvv = res_onse.substring(8, 11);
			trace("Gennerated cvv value is : " + cvv);
			if (cvv.length() < 3) {
				return "-1";
			}
		}
		return cvv;
	}

	public String getRacalRandomPinResponse(String res_onse) {
		HsmTcpIp hsmtcpip = new HsmTcpIp();
		String respcode = res_onse;
		trace("random bin respcode--->"+respcode);
		String fresp = hsmtcpip.getRacalHSMResponse(respcode);
		trace("HSM Response Code::" + fresp);
		String randpin = "-1";
		if (fresp.equals("00")) {
			trace("Test 4 : " + fresp);
			randpin = res_onse.substring(8, 13);
			trace(" Random Pin  value is : " + randpin);
			if (randpin.length() < 5) {
				return "-1";
			}
		}
		return randpin;
	}

	public String getRacalFormatDataResponse(String res_onse) throws InterruptedException {
		HsmTcpIp hsmtcpip = new HsmTcpIp();
		String respcode = res_onse;
		String fresp = hsmtcpip.getRacalHSMResponse(respcode);
		trace("HSM Response Code::" + fresp);
		String randpin = "-1";
		if (fresp.equals("00")) {

			randpin = fresp;
			trace(" Random Pin  value is : " + randpin);

		}
		return randpin;
	}

	public String getRacalPrinterDataResponse(String res_onse) throws InterruptedException {
		HsmTcpIp hsmtcpip = new HsmTcpIp();
		String respcode = res_onse;
		String randpin = "-1";
		if (respcode == null || respcode == "") {
			trace("respcode got null");
			return randpin;
		}
		String fresp = hsmtcpip.getRacalHSMResponse(respcode);
		trace("HSM Response Code::" + fresp);
		if (fresp.equals("00")) {
			randpin = fresp;
		}
		randpin = fresp;
		return randpin;
	}

	public String getRacalPrinterDataAfterResponse(String res_onse) throws InterruptedException {
		HsmTcpIp hsmtcpip = new HsmTcpIp();
		String respcode = res_onse;
		String randpin = "-1";
		if (respcode == null || respcode == "") {
			trace("respcode got null");
			return randpin;
		}
		String fresp = hsmtcpip.getRacalHSMAfterResponse(respcode);
		trace("HSM Response Code::" + fresp);
		if (fresp.equals("00")) {
			trace("Test 4 : " + fresp);
			randpin = fresp;
		}
		randpin = fresp;
		return randpin;
	}

	public String getRacalPinOffsetResponse(String res_onse) {
		HsmTcpIp hsmtcpip = new HsmTcpIp();
		String respcode = res_onse;
		trace("response receive from " + respcode);
		String fresp = hsmtcpip.getRacalHSMResponse(respcode);
		trace("HSM Response Code::" + fresp);
		String pinoffset = "-1";
		if (fresp.equals("00")) {
			trace("Test 4 : " + fresp);
			pinoffset = res_onse.substring(8);
			trace(" Random Pin Offset value is : " + pinoffset);
			if (pinoffset.length() < 4) {
				return "-1";
			}
		}
		return pinoffset;
	}

	public String hexa(int num) {
		/*
		 * int m = 0; if( (m = num >>> 4) != 0 ) { hexa( m ); } return
		 * (char)((m=num & 0x0F)+(m<10 ? 48 : 55));
		 */

		return Integer.toHexString(num);
	}

	public ByteArrayOutputStream pinVerifiCationSafeNet(String command, String EPVK, String CHN, String pinblock,
			String ppk, String pinoffset, String acctblock, ByteArrayOutputStream outputStream) throws Exception {
		trace("\n-------------- Pin Verification --------------");

		// ppk - pin protect key

		DataOutputStream out = null;
		DataInputStream in = null;

		try {

			int PanOffset = 0;// Integer.parseInt( hsmobj.PAN_OFFSET );
			int PanLength = 16;// Integer.parseInt( hsmobj.PANVALIDATION_LENGTH
								// );
			String pinpadchar = "F";// hsmobj.PANPADCHAR;
			String pinlen = "04"; // hsmobj.PIN_LENGTH;

			trace("Pan Offset : " + PanOffset);
			trace("Pan Validation Length : " + PanLength);
			trace("Pan Pad Char : " + pinpadchar);
			int len = EPVK.length() / 2 + 1;
			String hexpvklen = hexa(len);

			outputStream = eracom_out(command, command.length(), "FunctionCode", 1, outputStream);
			outputStream = eracom_out("00", 2, "FUNCTION-MODIFIER", 1, outputStream);
			outputStream = eracom_out(pinblock, pinblock.length(), "PIN-BLOCK", 1, outputStream);

			int ppklen = ppk.length() / 2 + 1;
			String hexppklen = hexa(ppklen);

			outputStream = eracom_out(hexppklen, hexppklen.length(), "PPK-LEN", 1, outputStream);
			outputStream = eracom_out("11", 2, "PPK-FORMAT", 1, outputStream);
			outputStream = eracom_out(ppk, ppk.length(), "PPK-FORMAT", 1, outputStream);
			outputStream = eracom_out("01", 2, "PINBLOCK-FORMAT", 1, outputStream);
			outputStream = eracom_out(acctblock, acctblock.length(), "ACCT-BLOCK", 1, outputStream);

			outputStream = eracom_out(hexpvklen, hexpvklen.length(), "PVK-LEN", 1, outputStream);

			if (EPVK.length() == 16)
				outputStream = eracom_out("10", 2, "PVK-FORMAT", 1, outputStream);
			else if (EPVK.length() == 32)
				outputStream = eracom_out("11", 2, "PVK-FORMAT", 1, outputStream);
			else
				outputStream = eracom_out("12", 2, "PVK-FORMAT", 1, outputStream);

			outputStream = eracom_out(EPVK, EPVK.length(), "PVK", 1, outputStream);

			String pandata = CHN.substring(PanOffset, PanLength);
			if (pandata.length() < 16) {
				String repeated = String.format(String.format("%%0%dd", (16 - pandata.length())), 0).replace("0",
						pinpadchar);
				trace("Repeated String : " + repeated);
				pandata = pandata + repeated;
				trace("pandata : " + pandata);
			}

			outputStream = eracom_out(pandata, pandata.length(), "PAN-VAL-DATA", 1, outputStream);
			outputStream = eracom_out(pinoffset, pinoffset.length(), "PIN-OFFSET-DATA", 1, outputStream);
			outputStream = eracom_out(pinlen, pinlen.length(), "PIN-LEN", 1, outputStream);

			/*
			 * out.write( outputStream.toByteArray() ); trace( new String (
			 * Hex.encodeHex( outputStream.toByteArray() ) ) ); out.flush();
			 * byte[] response = new byte[18]; in.read(response); trace(
			 * "----response begin ------"); trace( new String ( Hex.encodeHex(
			 * response) ) ); trace("----response end------");
			 */

		} catch (Exception e) {
			System.out.println("Exception while verify the pin : " + e.getMessage());
			e.printStackTrace();
		}

		return outputStream;
	}

	public ByteArrayOutputStream generateSessionkeyMessage(String command, String TMK,
			ByteArrayOutputStream outputStream) throws Exception {

		outputStream = eracom_out(command, command.length(), "FunctionCode", 1, outputStream);
		outputStream = eracom_out("00", 2, "FUNCTION-MODIFIER", 1, outputStream);

		int TMKLEN = TMK.length() / 2 + 1;
		String hextmklen = hexa(TMKLEN);

		outputStream = eracom_out(hextmklen, hextmklen.length(), "TMK-LEN", 1, outputStream);
		outputStream = eracom_out("11", 2, "TMK-FORMAT", 1, outputStream);
		outputStream = eracom_out(TMK, TMK.length(), "TMK", 1, outputStream);
		outputStream = eracom_out("0200", 4, "KEY FLAGS", 1, outputStream);
		return outputStream;
	}



private ByteArrayOutputStream generateRacalEncryptPinBlock(String data) {
			HsmTcpIp hsmtcp = new HsmTcpIp();
					
					String bufferval = data;
					String pad = "00";  
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					  try {
					   
					   System.out.println("Buffer value for pinoffset ::: "+bufferval);
					   System.out.println("Length of buffer value ::: "+bufferval.length());
				       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				       outputStream.write(bufferval.length());
				       String pinoffsethex = hsmtcp.byteToHexString(outputStream.toByteArray());
				       trace("pinoffsethex"+pinoffsethex);
				       ByteArrayOutputStream newhex = new ByteArrayOutputStream();
				       newhex.write(pad.getBytes());
				       newhex.write(pinoffsethex.getBytes());
				       String pinoff = new String(newhex.toByteArray());
				       trace("pi off"+pinoff);
				       String hexoffset = hsmtcp.hexToASCII(pinoff);
				       trace("hexoffset"+hexoffset);
				       bout.write(hexoffset.getBytes());
				       bout.write(bufferval.getBytes());
				       byte b[] = bout.toByteArray();
					    for (int i = 0; i < b.length; i++) {
					      System.out.println((char) b[i]);
					    }
					  }catch(Exception e)
					  {
						  trace("Exception Happened"+e.getMessage());
					  }
				       
				       
					return bout;
				}
		
private ByteArrayOutputStream getClearPinVal(String data) {
	HsmTcpIp hsmtcp = new HsmTcpIp();
			
			String bufferval = data;
			String pad = "00";  
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			  try {
			   
			   System.out.println("Buffer value for pinoffset ::: "+bufferval);
			   System.out.println("Length of buffer value ::: "+bufferval.length());
		       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		       outputStream.write(bufferval.length());
		       String pinoffsethex = hsmtcp.byteToHexString(outputStream.toByteArray());
		       trace("pinoffsethex"+pinoffsethex);
		       ByteArrayOutputStream newhex = new ByteArrayOutputStream();
		       newhex.write(pad.getBytes());
		       newhex.write(pinoffsethex.getBytes());
		       String pinoff = new String(newhex.toByteArray());
		       trace("pi off"+pinoff);
		       String hexoffset = hsmtcp.hexToASCII(pinoff);
		       trace("hexoffset"+hexoffset);
		       bout.write(hexoffset.getBytes());
		       bout.write(bufferval.getBytes());
		       byte b[] = bout.toByteArray();
			    for (int i = 0; i < b.length; i++) {
			      System.out.println((char) b[i]);
			    }
			  }catch(Exception e)
			  {
				  trace("Exception Happened"+e.getMessage());
			  }
		       
		       
			return bout;
		}
	
}
