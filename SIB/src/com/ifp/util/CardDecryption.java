package com.ifp.util;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import com.ifp.Action.BaseAction;

import java.security.Key;
import java.security.KeyException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class CardDecryption extends BaseAction
{
	/*public static KeySpec keySpec;
	  public static SecretKey key;
	  public static IvParameterSpec iv;*/
	
	static final String HEXES = "0123456789ABCDEF";
	static String key = "8E22E7EF53EA9478EDE7499C270FC38F";
	static final String Dummy = "00000000000000000000000000000000";
	static String clearDmk = "64540307145AB40FDD6542010449A00D";
	static String clearDpk = "F519413F71658082F519413F71658082";
	static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	private static final long serialVersionUID = 1L;
	 
	static CommonDesc commondesc = new CommonDesc();
	
	private static JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	
	public JdbcTemplate getJdbctemplate()
	{
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate)
	{
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager()
	{
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	
	public String encryptCHN()
	{
		trace("called encrypthome method");
		return "encryptchnhomepage";
	}
	public String decryptCHN()
	{
		trace("called decryptHome method");
		return "decryptchnhomepage";
		
	}
	
	
	public String encrypt()
	{
		String usernameEnkey=null;
		trace("called encrypt method");
		String Encryptedvalue = getRequest().getParameter("encrypt");
		
		//trace("valuesss"+Encryptedvalue);
		try
		{
			 usernameEnkey=Encryption(Encryptedvalue);
			 if(!usernameEnkey.equals(" "))
			 {
				 //trace("inside trace");
				addActionMessage("The Encrypted CHN is --->>  "+usernameEnkey);
				 return "required_home";
			 }
			
		}
		catch(Exception e)
		{
			e.getMessage();
			trace("exception occured in encryption " + e.getMessage());
		}
		return "required_home";
	}
	
	
	public String decrypt()
	{
		String usernameEnkey=null;
		trace("called decrypt method");
		HttpSession session = getRequest().getSession();
		String decryptedvalue = getRequest().getParameter("decrypt");
		//trace("valuesss"+decryptedvalue);
		try
		{
			 usernameEnkey=Decryption(decryptedvalue);
			 if(!usernameEnkey.equals(" "))
			 {
				 trace("inside trace");
				addActionMessage("The Decrypted CHN is --->>  "+usernameEnkey);
				 return "required_home";
			 }
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			e.getMessage();
			trace("exception occured in encryption " + e.getMessage());
		}
		return "required_home";
	}
	
	public static String Encryption(String value) throws KeyException, Exception 
	{
		String instid="SIB";
		String cdmk=null;
		Properties prop = commondesc.getCommonDescProperty();
		String EDPK = prop.getProperty("EDPK");
		System.out.println("checking full key--->"+EDPK);
		
		String keyid1 = commondesc.getSecurityKeyid(instid, jdbctemplate);
		System.out.println("key id checking -->"+keyid1);
		List secList2 = commondesc.getPADSSDetailByIdd(keyid1, jdbctemplate);
		//System.out.println("list22-->"+secList2);
		Iterator secitr = secList2.iterator();
			while(secitr.hasNext())
			{
				Map map = (Map) secitr.next(); 
				cdmk = ((String)map.get("DMK"));
				//System.out.println("new dmk-->"+cdmk);	
			}
	 		String cdpk=decryptDPK(cdmk,EDPK);
	 		//System.out.println("cdpk------->"+cdpk);
	 		String ecard=getECHN(cdpk,value);
	 		//System.out.println("ecard------->"+ecard);
	        return ecard;
	}
	
	
	public static String getECHN(String cdpk,  String cardnumber) throws KeyException, Exception 
	{
	    byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };	
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(cardnumber.getBytes());
		return Base64.encodeBase64String(encrypted);
	}
	public static String getCHN(String cdpk, String encryptCardnumber) throws KeyException, Exception 
	{	
		byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encryptCardnumber));
		String s = new String(decrypted);
		return s;
	}
	public static String Decryption(String value) throws KeyException, Exception 
	{
		String instid="SIB";
		String cdmk=null;
		Properties prop = commondesc.getCommonDescProperty();
		String EDPK = prop.getProperty("EDPK");
		//System.out.println("checking full key--->"+EDPK);
		
		String keyid1 = commondesc.getSecurityKeyid(instid, jdbctemplate);
		//System.out.println("key id checking -->"+keyid1);
		List secList2 = commondesc.getPADSSDetailByIdd(keyid1, jdbctemplate);
		//System.out.println("list22-->"+secList2);
		Iterator secitr = secList2.iterator();
			while(secitr.hasNext())
			{
				Map map = (Map) secitr.next(); 
				cdmk = ((String)map.get("DMK"));
				//System.out.println("new dmk-->"+cdmk);	
			}
		
		//String cdmk="6FA669445B695B75";
 		//String cdpk="5777BA42CE470233276E1737B510D2C5";
		String cdpk1=decryptDPK(cdmk,EDPK);
 		//System.out.println("cdpk------->"+cdpk1);
 		String ccard=getCHN(cdpk1,value);
 		//System.out.println("ccard------->"+ccard);
        return ccard;
	}	
	
	
public static String decryptDPK(String cdmk, String edpk) throws KeyException, Exception 
{
		
		byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdmk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(edpk));
		System.out.println("VVVVVV->");
		//System.out.println("VVVVVV->"+decrypted);
		String s = new String(decrypted);
		//System.out.println("TTTTTTTTTT->"+s);
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
public static String getHex(byte[] raw) throws Exception 
{
	if (raw == null)
	{
		return null;
	}

	final StringBuilder hex = new StringBuilder(2 * raw.length);

	for (final byte b : raw)
	{
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
public static byte[] hexStringToByteArray(String s)
{

	int len = s.length();

	byte[] data = new byte[len / 2];

	for (int i = 0; i < len; i += 2) 
	{
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
public String getCheckDigit(String data) throws Exception
{
	if (data == null) 
	{
		return "-1";
	}

	if (data.length() != 32 && data.length() != 64) 
	{
		return "1";
	}
    
	System.out.println("calling getCheckDigit()---->");
	
	// PadssSecurity Sec = new PadssSecurity();
	byte[] Result = null;
	// byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	byte[] DataBytes = hexStringToByteArray(data);

	CardDecryption sec = new CardDecryption();
	// Result = Sec.DesEncrypt(DataBytes, Dummy);
	Result = sec.getcheckDigitBy(DataBytes, key);
	// System.out.println(getHex(Result));
	return getHex(Result);
}

/**
 * By using this method to get masked card number
 * 
 * @param cardno
 * @return
 */
public String getMakedCardno(String cardno)
{
	int index = 0;

	Properties prop = getCommonDescProperty();
	String mask = "";
	if (cardno.length() == 16) {
		mask = prop.getProperty("cardno16.mask");
	} else if (cardno.length() == 19) {
		mask = prop.getProperty("cardno19.mask");
	}

	String number = cardno;

	System.out.println("CARD :" + number + " Mask :" + mask);
	StringBuilder maskedNumber = new StringBuilder();
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
	System.out.println(maskedNumber.toString());

	return maskedNumber.toString();
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

public String getFormattedKey(String data1, String data2, String data3) throws Exception 
{
	if (data1 == null && data2 == null && data3 == null) {
		return "-1";
	}
	// if( data1.length() == data2.length() && data1.length() ==
	// data3.length() ){ return "3"; }

	String R1 = null;
	String R2 = null;

	R1 = xor(data1, data2);
	System.out.println("Result1--->" + R1 );

	R2 = xor(R1, data3);
	 System.out.println("Result1--->" + R2 );
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
	if (inputdata == null) {
		return null;
	}

	MessageDigest md = MessageDigest.getInstance("SHA-512");
	// inputdata = inputdata.replaceAll("ORBL", "ORIENT");
	md.update(inputdata.getBytes());

	byte byteData[] = md.digest();

	// convert the byte to hex format method 1
	/*
	 * StringBuffer sb = new StringBuffer(); for (int i = 0; i <
	 * byteData.length; i++) { sb.append(Integer.toString((byteData[i] &
	 * 0xff) + 0x100, 16).substring(1)); }
	 */

	// System.out.println("Hex format : " + sb.toString());

	// convert the byte to hex format method 2
	StringBuffer hexString = new StringBuffer();
	for (int i = 0; i < byteData.length; i++) {
		String hex = Integer.toHexString(0xff & byteData[i]);
		if (hex.length() == 1)
			hexString.append('0');
		hexString.append(hex);
	}

	// System.out.println("Hex format : " + hexString.toString());
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

public String encryptDPK(String cdmk,  String cdpk) throws KeyException, Exception 
{
	byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	IvParameterSpec iv = new IvParameterSpec(ivFactor);
	SecretKeySpec skeySpec = new SecretKeySpec(cdmk.getBytes(), "AES");
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

	byte[] encrypted = cipher.doFinal(cdpk.getBytes());

	return Base64.encodeBase64String(encrypted);
}





public static void main(String[] args) throws KeyException, Exception 
{

	CardDecryption sec = new CardDecryption();

	 String edpk="9Sj8xRNeJRzMi0eZ+Dqfq7kWkRoy9xd1ncHl0t7grS0ohLVo2eorR1WQp6nqsz6O";
	// endpk="GswyY0cqIQfsBNMEOKheiQlswdaLQ+mDyDePH562ZpzjAnCINFIsXP+QuPPoaqoY";
	// KEYS start
	System.out.println("-------keys process start---------");
	//String edpk = sec.encryptDPK(clearDmk, iv, clearDpk);

	System.out.println("clear dpk before encryption---->    " + clearDpk);

	//System.out.println("ecrypted dpk--->    " + edpk);

	//String dpk = sec.decryptDPK(clearDmk, iv, edpk);
	//System.out.println("clear dpk after de cryption -->    " + dpk);
	System.out.println("-------keys process end---------");

	// byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	// Card process start
	System.out.println("-------Card process start---------");
	String clearDPK = sec.decryptDPK(clearDmk, edpk);
	System.out.println("clear dpk is ---->  " + clearDPK);

	String cardnumber = "8241933789524016";
	System.out.println("before encrypt card number   is ---->  " + cardnumber);

	String encryptCard = sec.getECHN(clearDPK,cardnumber);
	System.out.println("encryptCard  is ---->  " + encryptCard);

	String decryptCard = sec.getCHN(clearDPK,encryptCard);
	System.out.println("decryptCard  is ---->  " + decryptCard);

	System.out.println("-------Card process end---------");

	String res = sec.getCheckDigit(clearDmk);
	System.out.println(res.substring(0, 6));

}







}