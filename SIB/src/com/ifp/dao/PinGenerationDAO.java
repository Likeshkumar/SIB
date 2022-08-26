package com.ifp.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Hex;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;
import com.ifp.instant.HSMParameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Transactional
public class PinGenerationDAO extends BaseAction {

	public int blockAnotherUserGeneratePin(String instid, String branchcode, String usercode,
			JdbcTemplate jdbctemplate) {
		int x = -1;
		String insert_cardgen = "INSERT INTO CARDGEN_STATUS (INST_ID,BRANCH_CODE,USER_ID,CARDGEN_STATUS,LAST_DATE) VALUES ('"
				+ instid + "','" + branchcode + "','" + usercode + "','P', SYSDATE )";
		enctrace("insert cardgen " + insert_cardgen);
		x = jdbctemplate.update(insert_cardgen);
		return x;
	}

	public String getCafRecStatus(String instid, String cardno, String processtype, JdbcTemplate jdbctemplate) {
		String tablename = "INST_CARD_PROCESS";
		if (!processtype.equals("INSTANT")) {
			tablename = "PERS_CARD_PROCESS";
		}

		String cafrecstatus = null;
		String cafrecqry = "SELECT CAF_REC_STATUS FROM " + tablename + " WHERE INST_ID='" + instid + "' AND ORG_CHN='"
				+ cardno + "' ";
		enctrace("cafrecqry---" + cafrecqry);
		try {
			cafrecstatus = (String) jdbctemplate.queryForObject(cafrecqry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return cafrecstatus;
	}

	public int deletePingenerationStatus(String instid, String brcode, JdbcTemplate jdbctemplate) throws Exception {
		String deleteCardgenstatus = "DELETE FROM CARDGEN_STATUS WHERE INST_ID='" + instid + "' AND BRANCH_CODE='"
				+ brcode + "' AND CARDGEN_STATUS='P'";
		int delete_status = jdbctemplate.update(deleteCardgenstatus);
		return delete_status;
	}

	public int deleteFromProcess(String instid, String cardno, String processtype, String hcardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String tablename = "";
		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		} else {
			tablename = "PERS_CARD_PROCESS";
		}
		String deleteCardgenstatus = "DELETE FROM " + tablename + " WHERE INST_ID='" + instid + "' AND ORG_CHN='"
				+ cardno + "'";
		String deleteFromHashTable = "DELETE FROM PERS_CARD_PROCESS_HASH WHERE   INST_ID='" + instid
				+ "'    AND  HCARD_NO='" + hcardno + "'";

		int deleteHash = jdbctemplate.update(deleteFromHashTable);

		int delete_status = jdbctemplate.update(deleteCardgenstatus);

		int result = -1;

		trace("delte result in process::  " + delete_status + "    deleteHash:::   " + deleteHash);

		if (delete_status == 1 && deleteHash == 1) {
			result = 1;
		}

		return result;
	}

	public static Socket ConnectingHSM(String IPaddress, int port, int hsmTimeout) {
		Socket socket = null;
		try {
			socket = new Socket(IPaddress, port);
			System.out.println("Result from socket----> " + socket);
		} catch (Exception e) {
			System.out.println(" connection is 2  " + socket);
			System.out.println("Socket Creation Excetiopjk-=========> " + e.getMessage());
			return socket;
		}

		System.out.println(" Socket Is Returns is ======####====> " + socket);
		return socket;
	}

	public String getChnExpiryDate(String instid, String processtype, String chn, String cvv_type,
			JdbcTemplate jdbctemplate) throws Exception {
		String tablename = "INST_CARD_PROCESS";
		if (!processtype.equals("INSTANT")) {
			tablename = "PERS_CARD_PROCESS";
		}

		String expiry_date_query = null;
		String expiry_date = null;
		if (cvv_type.equals("cvv1"))
			expiry_date_query = "select to_char(EXPIRY_DATE,'yymm') from " + tablename + " where INST_ID='" + instid
					+ "' and ORG_CHN='" + chn + "'";
		else if (cvv_type.equals("icvv"))
			expiry_date_query = "select to_char(EXPIRY_DATE,'yymm') from " + tablename + " where INST_ID='" + instid
					+ "' and ORG_CHN='" + chn + "'";
		else if (cvv_type.equals("cvv2"))
			// expiry_date_query="select to_char(EXPIRY_DATE,'mmyy') from
			// "+tablename+" where INST_ID='"+instid+"' and CARD_NO='"+chn+"'";
			expiry_date_query = "select to_char(EXPIRY_DATE,'mmyy') from " + tablename + " where INST_ID='" + instid
					+ "' and ORG_CHN='" + chn + "'";
		// }

		// expiry_date_query="select to_char(EXPIRY_DATE,'yymm') from
		// "+tablename+" where INST_ID='"+instid+"' and CARD_NO='"+chn+"'";

		// by gowtham an enctrace only
		// enctrace( "expiry_date_query" + expiry_date_query);

		try {
			expiry_date = (String) jdbctemplate.queryForObject(expiry_date_query, String.class);
			// enctrace("cvvtype___"+expiry_date);
		} catch (EmptyResultDataAccessException e) {
		}

		return expiry_date;
	}

	/*
	 * public static String ComposePINMailer() { String commpossed = "";
	 * 
	 * commpossed = eracom_out( "EE0E06", 6, "COMMAND", 1 ); System.out.println(
	 * " RETURN VALUES "+commpossed); commpossed += eracom_out( "01", 2,
	 * "FUNCTION-MODIFIER", 2 ); return commpossed; }
	 */

	public String ComposeCVVLength(String CVK, String CHN, String Expiry, String ServiceCode,
			ByteArrayOutputStream outputStream) throws IOException {
		String ReqBuffer = "";
		String CVVData = CHN + Expiry + ServiceCode;
		System.out.println("CVV DATA " + CVVData + " Length " + CVVData.length());

		ReqBuffer += eracom_out("EE0802", 6, "Command", 1, outputStream);
		ReqBuffer += eracom_out("00", 2, "FUNCTION-MODIFIER", 1, outputStream);

		ReqBuffer += composeCvkData(CVK, outputStream);

		if (CVVData.length() <= 32) {
			CVVData = CVVData + fillwithZero(CVVData);
		}
		System.out.println("After zero : " + CVVData);

		System.out.println("FINAL CVV DATA leng 0 : " + ReqBuffer.length());
		ReqBuffer += eracom_out(CVVData, 32, "CVV - Data", 1, outputStream);

		System.out.println("FINAL CVV DATA : " + ReqBuffer);
		System.out.println("FINAL CVV DATA leng : " + ReqBuffer.length());
		return ReqBuffer;
	}

	public static ByteArrayOutputStream eracom_out(String data, int datasize, String description, int conv_type,
			ByteArrayOutputStream bytestream) throws IOException {
		System.out.println("errrracom----" + data + "----" + datasize + "----" + description + "----" + conv_type);
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

		}
		// else if (conv_type == 2) {
		// int dataint = Integer.parseInt(data);
		// byte datachar= (byte)dataint;
		// bytestream.write( datachar );
		// }

		return bytestream;
	}

	public static byte[] ascii_to_bcd(String token, int offsetsize) {
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

	public static int atohex(int val) {

		int toreturn;

		if ((val >= 48) && (val <= 57)) {
			// .out.println(" CHECKING ---- 1");
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

	public static ByteArrayOutputStream composeCvkData(String CVK, ByteArrayOutputStream outputStream)
			throws IOException {
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

	public static ByteArrayOutputStream ComposeCVV(String CVK, String CHN, String Expiry, String ServiceCode,
			ByteArrayOutputStream outputStream) throws IOException {

		String CVVData = CHN + Expiry + ServiceCode;
		System.out.println("CVV DATA " + CVVData + " Length " + CVVData.length());
		eracom_out("EE0802", 6, "Command", 1, outputStream);
		eracom_out("00", 2, "FUNCTION-MODIFIER", 1, outputStream);
		composeCvkData(CVK, outputStream);
		if (CVVData.length() <= 32) {
			CVVData = CVVData + fillwithZero(CVVData);
		}
		eracom_out(CVVData, 32, "CVV - Data", 1, outputStream);

		System.out.println("cvv.............");
		System.out.println(Hex.encodeHex(outputStream.toByteArray()));
		System.out.println("cvv.............");

		return outputStream;
	}

	public static String fillwithZero(String CVVData) {
		String zerostr = "";

		for (int i = 0; i < 32 - (CVVData.length()); i++) {
			zerostr = zerostr + "0";
		}

		return zerostr;
	}

	public static String deComposemsg(String msg) {
		if (msg.length() < 0)
			return "ERROR";

		String result = "";
		result = eracom_in(msg, 6, "COMMAND", 1);
		System.out.println(
				"=========================================================================================================================");
		System.out.println("The Result for Command is " + result);

		String decom_str = msg.substring(3);

		System.out.println("The decom_str STRING IS");

		String repos = eracom_in(decom_str, 2, "COMMAND", 1);
		System.out.println("Reponse ====> " + repos);

		if (!(repos.equals("00"))) {
			return "NO";
		}
		String outputmsg = decom_str.substring(1);
		System.out.println("outputmsg===> " + outputmsg);
		findStringvalue(outputmsg);
		String cvvdata = eracom_in(outputmsg, outputmsg.length(), "COMMAND", 1);
		System.out.println("CVV DATA ====> " + cvvdata);

		String finalresponse = cvvdata.substring(0, 3);
		System.out.println("The Final OutPut Is ===> " + finalresponse);

		return finalresponse;
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

	public void converttoString(String msg) {
		String result = eracom_in(msg, msg.length(), "COMMAND", 1);
		trace("Command is : " + result);
	}

	public static String findStringvalue(String stringval) {
		String result = eracom_in(stringval, stringval.length(), "COMMAND", 1);
		System.out.println("The Value IN MESSAGE IS " + result);
		return result;
	}

	public HSMParameter gettingBin_details(String binn, String inst_name, JdbcTemplate jdbctemplate) throws Exception {
		HSMParameter hsmparams = null;
		List bindetails_List = null;
		String HSMNAME = "0", HSMPROTOCOL = "0", HSMTYPE = "0", HSMADDRESS = "0", HEADERTYPE = "0", HSM_ID = "0",
				CHNLEN = "0", PVK = "0";
		String PIN_LENGTH = "0", PAN_OFFSET = "0", PINOFFSET_LENGTH = "0", PIN_PAD_CHAR = "0",
				DECIMILISATION_TABLE = "0";
		String MSG_HEADER = "0", MAIL_LENGTH = "0", MAIL_HEIGHT = "0", CVV_REQUIRED = "0", CVV_LENGTH = "0",
				CVVK1 = "0", CVVK2 = "0";
		String PVK1 = "0", PVK2 = "0", GEN_METHOD = "0", PVKI = "0", PPK = "0", PANVALIDATION_LENGTH = "0", PVK3 = "0",
				PVK4 = "0", CVK4 = "0";
		String CVK3 = "0", PROTOCOL_TYPE = "0", STOP_BITS = "0", PARITY_VALUE = "0", DATA_BITS = "0", BAUD_RATE = "0",
				PORT_TYPE = "0";
		String EPVK = "0", PANPADCHAR = "0", DESLENGTH = "0", PINMAILER_ID = "0", SERVICE_CODE = "0",
				PINMAILER_DOC_TYPE = "C";
		int HSMPORT = 0, HEADERLEN = 0, HSMHEADERLEN = 0, HSMTIMEOUT = 0, CONNECTIONINTERVAL = 0, HSMSTATUS = 0;

		String binDetails_Query = "select hd.HSMNAME as HSMNAME,hd.HSMPROTOCOL as HSMPROTOCOL,hd.HSMTYPE as HSMTYPE,hd.HSMADDRESS as HSMADDRESS,"
				+ "hd.HSMPORT as HSMPORT,hd.HEADERLEN as HEADERLEN,hd.HEADERTYPE as HEADERTYPE,hd.HSMHEADERLEN as HSMHEADERLEN,"
				+ "hd.HSMTIMEOUT as HSMTIMEOUT,hd.CONNECTIONINTERVAL as CONNECTIONINTERVAL,hd.HSMSTATUS as HSMSTATUS,hd.HSM_ID as HSM_ID,"
				+ "br.CHNLEN as CHNLEN,br.PVK as PVK,br.PPK as PPK,br.PIN_LENGTH as PIN_LENGTH,br.PAN_OFFSET as PAN_OFFSET,br.PINOFFSET_LENGTH as PINOFFSET_LENGTH,"
				+ "br.DECIMILISATION_TABLE as DECIMILISATION_TABLE,br.CVV_REQUIRED as CVV_REQUIRED,br.CVV_LENGTH as CVV_LENGTH,br.PVK1 as PVK1,"
				+ "br.PVK2 as PVK2,br.GEN_METHOD as GEN_METHOD,br.PVKI as PVKI,br.PANVALIDATION_LENGTH as PANVALIDATION_LENGTH,"
				+ "br.EPVK as EPVK,br.PANPADCHAR as PANPADCHAR,br.DESLENGTH as DESLENGTH,"
				+ "br.PINMAILER_ID as PINMAILER_ID,  br.CVK1 as CVVK1, br.CVK2 as CVVK2"
				+ " from HSM_DETAILS hd,PRODUCTINFO br where  br.INST_ID='" + inst_name + "' and br.BIN='" + binn
				+ "' and hd.HSM_ID=br.HSM_ID ";

		enctrace(" Bin Details Query is : " + binDetails_Query);
		bindetails_List = jdbctemplate.queryForList(binDetails_Query);
		if (!(bindetails_List.isEmpty())) {
			Iterator bin_itr = bindetails_List.iterator();
			while (bin_itr.hasNext()) {
				Map map = (Map) bin_itr.next();
				HSMNAME = ((String) map.get("HSMNAME"));
				HSMPROTOCOL = ((String) map.get("HSMPROTOCOL"));
				HSMTYPE = ((String) map.get("HSMTYPE"));
				HSMADDRESS = ((String) map.get("HSMADDRESS"));
				HSMPORT = ((BigDecimal) map.get("HSMPORT")).intValue();
				HEADERLEN = ((BigDecimal) map.get("HEADERLEN")).intValue();
				HEADERTYPE = ((String) map.get("HEADERTYPE"));
				HSMHEADERLEN = ((BigDecimal) map.get("HSMHEADERLEN")).intValue();
				HSMTIMEOUT = ((BigDecimal) map.get("HSMTIMEOUT")).intValue();
				CONNECTIONINTERVAL = ((BigDecimal) map.get("CONNECTIONINTERVAL")).intValue();
				HSMSTATUS = ((BigDecimal) map.get("HSMSTATUS")).intValue();
				HSM_ID = ((String) map.get("HSM_ID"));
				CHNLEN = ((String) map.get("CHNLEN"));
				PVK = ((String) map.get("PVK"));
				PIN_LENGTH = ((String) map.get("PIN_LENGTH"));
				PAN_OFFSET = ((String) map.get("PAN_OFFSET"));
				PINOFFSET_LENGTH = ((String) map.get("PINOFFSET_LENGTH"));
				PIN_PAD_CHAR = ((String) map.get("PIN_PAD_CHAR"));
				DECIMILISATION_TABLE = ((String) map.get("DECIMILISATION_TABLE"));
				MSG_HEADER = ((String) map.get("MSG_HEADER"));
				MAIL_LENGTH = ((String) map.get("MAIL_LENGTH"));
				MAIL_HEIGHT = ((String) map.get("MAIL_HEIGHT"));
				CVV_REQUIRED = ((String) map.get("CVV_REQUIRED"));
				CVV_LENGTH = ((String) map.get("CVV_LENGTH"));
				CVVK1 = ((String) map.get("CVVK1"));
				CVVK2 = ((String) map.get("CVVK2"));
				// trace( "CVK1 IS" + CVVK1 + ", CVK2 IS " + CVVK1);

				PVK1 = ((String) map.get("PVK1"));
				PVK2 = ((String) map.get("PVK2"));
				GEN_METHOD = ((String) map.get("GEN_METHOD"));
				PVKI = ((String) map.get("PVKI"));
				PANVALIDATION_LENGTH = ((String) map.get("PANVALIDATION_LENGTH"));
				PVK3 = ((String) map.get("PVK3"));
				PVK4 = ((String) map.get("PVK4"));
				CVK4 = ((String) map.get("CVK4"));
				CVK3 = ((String) map.get("CVK3"));
				PPK = ((String) map.get("PPK"));
				PROTOCOL_TYPE = ((String) map.get("PROTOCOL_TYPE"));
				STOP_BITS = ((String) map.get("STOP_BITS"));
				PARITY_VALUE = ((String) map.get("PARITY_VALUE"));
				DATA_BITS = ((String) map.get("DATA_BITS"));
				BAUD_RATE = ((String) map.get("BAUD_RATE"));
				PORT_TYPE = ((String) map.get("PORT_TYPE"));
				EPVK = ((String) map.get("EPVK"));
				PANPADCHAR = ((String) map.get("PANPADCHAR"));
				DESLENGTH = ((String) map.get("DESLENGTH"));
				PINMAILER_ID = ((String) map.get("PINMAILER_ID"));
				SERVICE_CODE = ((String) map.get("SERVICE_CODE"));

			}
			hsmparams = new HSMParameter(HSMNAME, HSMPROTOCOL, HSMTYPE, HSMADDRESS, HSMPORT, HEADERLEN, HEADERTYPE,
					HSMHEADERLEN, HSMTIMEOUT, CONNECTIONINTERVAL, HSMSTATUS, HSM_ID, CHNLEN, PVK, PPK, PIN_LENGTH,
					PAN_OFFSET, PINOFFSET_LENGTH, PIN_PAD_CHAR, DECIMILISATION_TABLE, MSG_HEADER, MAIL_LENGTH,
					MAIL_HEIGHT, CVV_REQUIRED, CVV_LENGTH, CVVK1, CVVK2, PVK1, PVK2, GEN_METHOD, PVKI,
					PANVALIDATION_LENGTH, PVK3, PVK4, CVK4, CVK3, PROTOCOL_TYPE, STOP_BITS, PARITY_VALUE, DATA_BITS,
					BAUD_RATE, PORT_TYPE, EPVK, PANPADCHAR, DESLENGTH, PINMAILER_ID, SERVICE_CODE, PINMAILER_DOC_TYPE);

			hsmparams.setError_hsmparameter("Y");
		}
		return hsmparams;
	}

	public int updateCvvValues(String instid, String CHN, String nextstatus, String processtype, String mkckstatus,
			String cvv1_value, String cvv2_value, String icvv_value, String pinoffset, String encryptpinblock,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String tablename = "INST_CARD_PROCESS";
		if (!processtype.equals("INSTANT")) {
			tablename = "PERS_CARD_PROCESS";
		}
		// String updatecvvqry = "UPDATE "+tablename+" SET
		// CARD_STATUS='"+nextstatus+"',PRIVILEGE_CODE='02M',MKCK_STATUS='"+mkckstatus+"',PIN_DATE=sysdate,
		// CVV1='"+cvv1_value+"', CVV2='"+cvv2_value+"',
		// ICVV='"+icvv_value+"',PIN_OFFSET='"+pinoffset+"',ENCRYPT_PINBLOCK='"+encryptpinblock.substring(8,24)+"'
		// WHERE INST_ID='"+instid+"' AND CARD_NO='"+CHN+"'";
		String updatecvvqry = "UPDATE " + tablename + " SET CARD_STATUS='" + nextstatus
				+ "',PRIVILEGE_CODE='02M',MKCK_STATUS='" + mkckstatus + "',PIN_DATE=sysdate, CVV1='" + cvv1_value
				+ "', CVV2='" + cvv2_value + "', ICVV='" + icvv_value + "',PIN_OFFSET='" + pinoffset
				+ "',ENCRYPT_PINBLOCK='" + encryptpinblock + "' WHERE INST_ID='" + instid + "' AND ORG_CHN='" + CHN
				+ "'";
		trace("updatecvvqry : " + updatecvvqry);
		x = jdbctemplate.update(updatecvvqry);
		return x;
	}

	public int donotupdateCvvValues(String instid, String CHN, String nextstatus, String processtype, String mkckstatus,
			String cvv1_value, String cvv2_value, String icvv_value, String pinoffset, JdbcTemplate jdbctemplate)
			throws Exception {
		int x = -1;
		String tablename = "INST_CARD_PROCESS";
		if (!processtype.equals("INSTANT")) {
			tablename = "PERS_CARD_PROCESS";
		}
		String updatecvvqry = "UPDATE " + tablename + " SET PRIVILEGE_CODE='02M',PIN_DATE=sysdate,PIN_OFFSET='"
				+ pinoffset + "' WHERE INST_ID='" + instid + "' AND CARD_NO='" + CHN + "'";
		trace("nocvvupdatecvvqry : " + updatecvvqry);
		x = jdbctemplate.update(updatecvvqry);
		return x;
	}

	public int updatePinToProduction(String instid, String CHN, String nextstatus, String switchstatus,
			String processtype, String cvv1_value, String cvv2_value, String icvv_value, String pinoffset,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String tablename = "CARD_PRODUCTION";
		if (processtype.equals("CREDIT")) {
			tablename = "IFC_CARD_PRODUCTION";
		}
		String condition = "";
		if (instid.equals("AZIZI")) {
			condition = "STATUS_CODE='53' ,";
			// "CVV1='"+cvv1_value+"', CVV2='"+cvv2_value+"',
			// ICVV='"+icvv_value+"', STATUS_CODE='53'";
		} else {
			condition = "STATUS_CODE='50',";
		}
		String updatecvvqry = "UPDATE " + tablename + " SET CARD_STATUS='" + nextstatus + "' ," + condition
				+ " PIN_DATE=sysdate,CVV1='" + cvv1_value + "', CVV2='" + cvv2_value + "', ICVV='" + icvv_value+"',pin_offset='"+pinoffset
				+ "'  WHERE INST_ID='" + instid + "' AND ORG_CHN='" + CHN + "'";

		trace("updatecvvqry : " + updatecvvqry);
		x = jdbctemplate.update(updatecvvqry);
		return x;
	}

	public int updatePinToProduction1(String instid, String CHN, String nextstatus, String switchstatus,
			String processtype, String cvv1_value, String cvv2_value, String icvv_value, String pinoffset,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String tablename = "CARD_PRODUCTION";
		if (processtype.equals("CREDIT")) {
			tablename = "IFC_CARD_PRODUCTION";
		}
		String condition = "";
		if (instid.equals("AZIZI")) {
			condition = "STATUS_CODE='53' ,";
			trace("ony pin generating CVV1='" + cvv1_value + "', CVV2='" + cvv2_value + "', ICVV='" + icvv_value
					+ "'+ for the card" + CHN + "");
			// "CVV1='"+cvv1_value+"', CVV2='"+cvv2_value+"',
			// ICVV='"+icvv_value+"', STATUS_CODE='53'";
		} else {
			condition = "STATUS_CODE='50',";
		}
		String updatecvvqry = "UPDATE " + tablename + " SET CARD_STATUS='" + nextstatus + "' ," + condition
				+ " PIN_DATE=sysdate WHERE INST_ID='" + instid + "' AND CARD_NO='" + CHN + "'";

		trace("updatecvvqry : " + updatecvvqry);
		x = jdbctemplate.update(updatecvvqry);
		return x;
	}

	public String getCVVResponse(String res_onse) {
		String response = res_onse.substring(18, 24);

		String fresp = response.substring(0, 2);

		String cvv = "-1";
		if (fresp.equals("00")) {
			trace("Test 4 : " + fresp);
			cvv = response.substring(2, 5);
			trace("Gennerated cvv value is : " + cvv);
			if (cvv.length() < 3) {
				return "-1";
			}
		}

		return cvv;
	}

	public int updatePinToSwitch(String instid, String hcardno, String switchstatus, String pinoffset,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String condition = "";
		condition = "STATUS='50',";

		String switchupdate = "UPDATE EZCARDINFO SET " + condition + " PINOFFSET='" + pinoffset
				+ "',PINRETRYCOUNT='0',OTP_INTVL='0',OTPGEN_DATE=SYSDATE WHERE INSTID='" + instid + "' AND CHN='"
				+ hcardno + "'";
		trace("switchupdateQry : " + switchupdate);

		x = jdbctemplate.update(switchupdate);
		return x;
	}

	public int deletefromprocessOTP(String instid, String hcardno, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1, y = -1;
		List prpstmt = null;
		ResultSet rs1 = null, rs2 = null;
		String checkInProduction = "SELECT COUNT(*) FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid
				+ "' AND HCARD_NO='" + hcardno + "'";
		trace("checkInProductionquery : " + checkInProduction);
		prpstmt = jdbctemplate.queryForList(checkInProduction);
		// rs1 = prpstmt.executeQuery(checkInProduction);
		Iterator bin_itr = prpstmt.iterator();
		// 1st
		if (!bin_itr.hasNext()) {

			return x;
		} else {
			do {

				x = rs1.getInt("CNT");
				// System.out.println("ffffffffffff");
			} while (rs1.next());
		}
		if (x > 0) {

			x = 1;
			System.out.println("CARD_PRODUCTION");
		}

		if (x > 0) {
			String processdeleteOTP = "DELETE FROM PERS_CARD_PROCESS WHERE INST_ID='" + instid + "' AND HCARD_NO='"
					+ hcardno + "'";
			trace("processdeleteOTPquery : " + processdeleteOTP);
			prpstmt = jdbctemplate.queryForList(processdeleteOTP);
			// prpstmt = con.prepareStatement(processdeleteOTP);
			// rs2 = prpstmt.executeQuery(processdeleteOTP);
			Iterator re2 = prpstmt.iterator();
			// 1st
			if (!rs2.next()) {

				return y;
			} else {
				do {

					x = rs2.getInt("CNT");
					// System.out.println("ffffffffffff");
				} while (rs2.next());
			}
			if (y > 0) {

				y = 1;
				System.out.println("CARD_PRODUCTION");
			}
		}
		return y;
	}

	public String getChnExpiryDatecvv1(String instid, String processtype, String chn, String cvv_type,
			JdbcTemplate jdbctemplate) throws Exception {

		String tablename = "PERS_CARD_PROCESS";

		String expiry_date_query = null;
		String expiry_date = null;

		expiry_date_query = "select to_char(EXPIRY_DATE,'yymm') from " + tablename + " where INST_ID='" + instid
				+ "' and ORG_CHN='" + chn + "'";

		enctrace("expiry_date_query for cvv1====== :::::::::: " + expiry_date_query);

		try {
			expiry_date = (String) jdbctemplate.queryForObject(expiry_date_query, String.class);
			enctrace("cvvtype___cvv1" + expiry_date);
		} catch (EmptyResultDataAccessException e) {
		}

		return expiry_date;
	}

	public String getChnExpiryDatecvv2(String instid, String processtype, String chn, String cvv_type,
			JdbcTemplate jdbctemplate) throws Exception {

		String tablename = "PERS_CARD_PROCESS";

		String expiry_date_query = null;
		String expiry_date = null;

		expiry_date_query = "select to_char(EXPIRY_DATE,'mmyy') from " + tablename + " where INST_ID='" + instid
				+ "' and ORG_CHN='" + chn + "'";

		enctrace("expiry_date_query for cvv2===== :::::  " + expiry_date_query);

		try {
			expiry_date = (String) jdbctemplate.queryForObject(expiry_date_query, String.class);
			enctrace("cvvtype___cvv2" + expiry_date);
		} catch (EmptyResultDataAccessException e) {
		}

		return expiry_date;
	}

}
