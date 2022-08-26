package com.ifg.Config.padss;

import java.io.InputStream;
import java.security.KeyException;
import java.security.MessageDigest;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * This class is used to maintain the Payment Application Data Security
 * Standard(PA-DSS)
 * 
 * SRNP0001
 * 
 * @author Prasad
 * @version 2.0
 * @since 1.0
 *
 */

public class PadssSecurity {

	static final String HEXES = "0123456789ABCDEF";
	static String key = "8E22E7EF53EA9478EDE7499C270FC38F";
	static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * This class is used to encrypt the card number using AES By passing below
	 * parameters Clear DPK as KEY, IV parameter(16 characters all zeros), Clear
	 * CardNumber (plain text)
	 * 
	 * @param cdpk
	 * @param ivFactor
	 * @param cardnumber
	 * 
	 * @return Encrypt card number
	 * @throws KeyException
	 * @throws Exception
	 */
	public String getECHN(String cdpk, String  cardnumber) throws KeyException, Exception {

		byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		StringBuilder sb = new StringBuilder();
		String encryptedText = null;

		try {

			IvParameterSpec iv = new IvParameterSpec(ivFactor);
			SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(cardnumber.toString().getBytes());

			// nullify
			cardnumber="0000000000000000";
			cardnumber = null;
			sb.append("0000000000000000");
			sb.append(cardnumber);
			sb.setLength(0);
			encryptedText = Base64.encodeBase64String(encrypted);

		} catch (Exception exce) {
			exce.printStackTrace();
		} finally {
			// nullify
			cardnumber = null;
			sb = null;
		}
		return encryptedText;
	}

	/**
	 * This class is used to decrypt the card number using AES By passing below
	 * parameters
	 * 
	 * Clear DPK as KEY, IV parameter(16 characters all zeros), Encrypted
	 * CardNumber
	 * 
	 * @param cdmk
	 * @param ivFactor
	 * @param encryptCardnumber
	 * @return Decrypt card number
	 * @throws KeyException
	 * @throws Exception
	 */
	public String getCHN(String cdpk, String encryptCardnumber) throws KeyException, Exception {
		
		String s=null;
		StringBuilder sb = new StringBuilder();
		
		try{
		byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		//System.out.println("kkkkkkkkkk"+encryptCardnumber);
		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encryptCardnumber));

		 s= new String(decrypted);
		
		 //nullify
		 encryptCardnumber="0000000000000000";
		 encryptCardnumber=null;
		 sb.append(encryptCardnumber);
		 sb.append("0000000000000000");
		 sb.delete(0, sb.length());
		
		}catch(Exception exce){
			exce.printStackTrace();
		}finally {
			
			//nullify
			 encryptCardnumber=null;
			 sb=null;
		}
		return s;
	}

	/**
	 * This method is used to get the formatted encrypt byte array
	 * 
	 * 
	 * @param ivFactor
	 * @param clearDmk
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getcheckDigitBy(byte[] ivFactor, String clearDmk) throws Exception {

		String key = "-23t56789a@#%^&*";
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(clearDmk.getBytes());

		return encrypted;

	}

	/**
	 * Represents to convert HEX decimal values by passing byte array
	 * 
	 * @param raw
	 * @return HEX decimal value
	 * @throws Exception
	 */
	public static String getHex(byte[] raw) throws Exception {
		if (raw == null) {
			return null;
		}

		final StringBuilder hex = new StringBuilder(2 * raw.length);

		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	/**
	 * It represents to convert hexa decimal values to byte array
	 * 
	 * @param s
	 * @return byte[]
	 */
	public static byte[] hexStringToByteArray(String s) {

		int len = s.length();

		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * This method is used for to get the Check digit to display in UI
	 * 
	 * @param data
	 * @return String
	 * @throws Exception
	 */
	public String getCheckDigit(String data) throws Exception {
		if (data == null) {
			return "-1";
		}

		if (data.length() != 32 && data.length() != 64) {
			return "1";
		}

		System.out.println("calling getCheckDigit()---->");

		// PadssSecurity Sec = new PadssSecurity();
		byte[] Result = null;
		// byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] DataBytes = hexStringToByteArray(data);

		PadssSecurity sec = new PadssSecurity();
		// Result = Sec.DesEncrypt(DataBytes, Dummy);
		Result = sec.getcheckDigitBy(DataBytes, key);
		// System.out.println(getHex(Result));
		return getHex(Result);
	}

	/**
	 * By using this method to get masked card number
	 * 
	 * @param cardno
	 * @return Masked Card number
	 */
	public String getMakedCardno(String  cardno) {

		int index = 0;

		StringBuilder maskedNumber = new StringBuilder();
		StringBuilder number = null;

		System.out.println("enter into Masked card number method ");
		Properties prop = getCommonDescProperty();
		String mask = "";
		if (cardno.length() == 16) {
			mask = prop.getProperty("cardno16.mask");
		} else if (cardno.length() == 19) {
			mask = prop.getProperty("cardno19.mask");
		}

		try {
			number = new StringBuilder(cardno);

			for (int i = 0; i < mask.length(); i++) {
				char c = mask.charAt(i);
				if (c == '#') {
					maskedNumber.append(number.charAt(index));
					index++;
				} else if (c == 'x') {
					maskedNumber.append(c);
					index++;
				} else {
					maskedNumber.append(c);
				}
			}
			// nullify
			cardno="0000000000000000";
			cardno = null;
			number.append("0000000000000000");
			number.setLength(0);
			number = null;

		} catch (Exception exe) {
			exe.printStackTrace();
		} finally {
			// nullify
			cardno = null;
			number = null;
		}
		System.out.println(maskedNumber.toString());
		return maskedNumber.toString();
	}

	/**
	 * It represents to get values from Properties file
	 * 
	 * @return Properties class
	 */
	public Properties getCommonDescProperty() {
		String filename = "CommonDesc.properties";
		Properties prop = new Properties();
		InputStream ins = null;
		try {
			ins = this.getClass().getClassLoader().getResourceAsStream(filename);
			prop.load(ins);
			if (ins != null) {
				ins.close();
			}
		} catch (Exception e) {
			System.out.println("Exception getting property : " + e.getMessage());
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * Method represents to get the formated keys
	 * 
	 * @param data1
	 * @param data2
	 * @param data3
	 * @return Formated String key
	 * @throws Exception
	 */

	public String getFormattedKey(String data1, String data2, String data3) throws Exception {
		if (data1 == null && data2 == null && data3 == null) {
			return "-1";
		}
		// if( data1.length() == data2.length() && data1.length() ==
		// data3.length() ){ return "3"; }

		String R1 = null;
		String R2 = null;

		R1 = xor(data1, data2);
		//System.out.println("Result1--->" + R1);

		R2 = xor(R1, data3);
		//System.out.println("Result1--->" + R2);
		return R2;
	}

	/**
	 * Method represents to get the formated keys
	 * 
	 * @param data1
	 * @param data2
	 * @return
	 * @throws Exception
	 */

	public String getFormattedKey(String data1, String data2) throws Exception {
		if (data1 == null && data2 == null) {
			return "-1";
		}
		// if( data1.length() == data2.length() ){ return "3"; }
		String R1 = null;

		R1 = xor(data1, data2);
		// System.out.println("Result1--->" + R1 );
		return R1;
	}

	/**
	 * This method is used to convert string data to characters
	 * 
	 * @param data1
	 * @param data2
	 * @return String
	 */
	public static String xor(String data1, String data2) {
		char[] data1arr = data1.toCharArray();
		char[] data2arr = data2.toCharArray();
		char[] res = new char[data2.length()];

		for (int i = 0; i < data2arr.length; i++) {
			res[i] = int_to_char((char_to_int(data1arr[i])) ^ (char_to_int(data2arr[i])));
		}

		String formattedres = String.valueOf(res);

		return formattedres;
	}

	/**
	 * This method is used to convert character value to int
	 * 
	 * @param val
	 * @return int
	 */
	private static int char_to_int(char hexval) {
		if (Character.isDigit(hexval))
			return (hexval - 48);
		else
			return (hexval - 55);
	}

	/**
	 * This method is used to convert int value to character
	 * 
	 * @param val
	 * @return Char
	 */
	private static char int_to_char(int val) {
		if (val < 10)
			return (char) (48 + val);
		else
			return (char) (55 + val);
	}

	/**
	 * This method is used to get the Hashed Card number using SHA-512 Algorithm
	 * 
	 * @param inputdata
	 * @return Hash Card number
	 * @throws Exception
	 */
	public StringBuffer getHashedValue(String inputdata) throws Exception {

		StringBuffer hexString = null;
		MessageDigest md = null;
		StringBuilder sb = null;

		try {

			hexString = new StringBuffer();
			sb = new StringBuilder();

			if (inputdata == null) {
				return null;
			}

			md = MessageDigest.getInstance("SHA-512");
			md.update(inputdata.getBytes());

			inputdata = "0000000000000000";
			inputdata = null;
			sb.append("0000000000000000");
			sb.append(inputdata);
			sb.setLength(0);

			byte byteData[] = md.digest();

			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
		} catch (Exception exce) {
			exce.printStackTrace();
		} finally {
			
			//nullify
			inputdata = "0000000000000000";
			inputdata = null;
			sb = null;
		}
		return hexString;
	}

	/**
	 * This method is used to get the Encrypted DPK by using AES By passing
	 * below parameters
	 * 
	 * Clear DMk as KEY, IV parameter(16 characters all zeros), Clear DPK (as
	 * data)
	 * 
	 * @param cdmk
	 * @param ivFactor
	 * @param cdpk
	 * @return Encrypt DPK
	 * @throws KeyException
	 * @throws Exception
	 */

	public String encryptDPK(String cdmk, String cdpk) throws KeyException, Exception {
		byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdmk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(cdpk.getBytes());
		System.out.println("encrypted-->"+encrypted);
		return Base64.encodeBase64String(encrypted);
	}

	/**
	 * This method is used to get the Decrypt DPK by using AES By passing below
	 * parameters
	 * 
	 * Clear DMk as KEY, IV parameter(16 characters all zeros), Encrypted DPK
	 * (as data)
	 * 
	 * @param cdmk
	 * @param ivFactor
	 * @param edpk
	 * @return Clear DPK
	 * @throws KeyException
	 * @throws Exception
	 */
	public String decryptDPK(String cdmk, String edpk) throws KeyException, Exception {

		byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		//System.out.println("cdmk==="+cdmk+"edpk===="+edpk);
		SecretKeySpec skeySpec = new SecretKeySpec(cdmk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
System.out.println();
		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(edpk));

		 //System.out.println("decrypt string:--------> " + Base64.encodeBase64String(decrypted));

		String s = new String(decrypted);
		 System.out.println("checking clear dpk-->"+s);
		return s;
	}

	public static void main(String[] args) throws KeyException, Exception {

		PadssSecurity sec = new PadssSecurity();
		String clearDmk="0125E56066717250";
		CardEncryptAndDecrypt card=new CardEncryptAndDecrypt();
		 //String endpk="GswyY0cqIQfsBNMEOKheiQlswdaLQ+mDyDePH562ZpzjAnCINFIsXP+QuPPoaqoY";
		 String clearDpk="F519413F71658082F519413F71658082";
		// KEYS start
		System.out.println("-------keys process start---------");
		 String edpk = sec.encryptDPK(clearDmk,   clearDpk);

		System.out.println("clear dpk before encryption----> " + clearDpk);

		 System.out.println("ecrypted dpk-----> " + edpk);

		 String dpk = sec.decryptDPK(clearDmk,   edpk);
		 System.out.println("clear dpk after de cryption --> " + dpk);
		System.out.println("-------keys process end---------");

		 byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		// Card process start
		
		  System.out.println("-------Card process start---------"); String
		  clearDPK = sec.decryptDPK(clearDmk,  edpk); System.out.println(
		  "clear dpk is ---->  " + clearDPK);
		 
		 String cardnumber = "8241933789524016"; System.out.println(
		 "before encrypt card number   is ---->  " + cardnumber);
		  
		  String encryptCard = card.encrypty(clearDPK,  iv,cardnumber);
		  System.out.println("encryptCard  is ---->  " + encryptCard);
		  
		  //String decryptCard = sec.decrypt(clearDPK, iv,  encryptCard);
		// System.out.println("decryptCard  is ---->  " + decryptCard);
		  
		  System.out.println("-------Card process end---------");
		  
		  String res = sec.getCheckDigit(clearDmk);
		 System.out.println(res.substring(0, 6));
		 

	}

}
