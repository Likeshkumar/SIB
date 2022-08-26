package test;



import java.security.KeyException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;



/**
 * 
 * This class is used for encrypt and decrypt the card number using AES
 * @author CGSPL
 *@since 02-07-2019
 *@version 2.0
 */
public class CardEncryptAndDecrypt {
	static final String HEXES = "0123456789ABCDEF";
	static String clearDmk="8E22E7EF53EA9478EDE7499C270FC38F";
	static final String Dummy = "00000000000000000000000000000000";
	
	public static void main(String[] args) throws KeyException, Exception {
		
		String clearDmk="8E22E7EF53EA9478EDE7499C270FC38F";
		String edpk=" lnNoFN5MOhmpklJTDmjcsYX/euNHntuSrxmDtfj1JwtLldOZZ/qqri9h9Ypp1+g/";
		
		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		
		String clearDPK=KeysEncryptAndDecrypt.decrypt(clearDmk, iv, edpk);
		System.out.println("clear dpk is ---->  "+clearDPK);
		
		CardEncryptAndDecrypt crd=new CardEncryptAndDecrypt();
		
		String cardnumber="8241933789524016";	
		System.out.println("before encrypt card number   is ---->  "+cardnumber);
		
		String encryptCard=CardEncryptAndDecrypt.encrypty(clearDPK, iv, cardnumber);
		System.out.println("encryptCard  is ---->  "+encryptCard);
		
		
		String decryptCard=CardEncryptAndDecrypt.decrypt(clearDPK, iv, encryptCard);
		System.out.println("decryptCard  is ---->  "+decryptCard);	
		
		
		String res=crd.getCheckDigit(clearDmk);
		System.out.println(res.substring(0, 6));
		
	}
	
	/**
	 * This  class is used to encrypt the card number using clear DPK 
	 * 
	 * @param cdpk
	 * @param ivFactor
	 * @param cardnumber
	 * 
	 * @return  Encrypt card number
	 * @throws KeyException
	 * @throws Exception
	 */
	public static  String encrypty(String cdpk,byte[] ivFactor,String cardnumber) throws KeyException, Exception{
		
		IvParameterSpec iv = new IvParameterSpec(ivFactor);  
		SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		
		byte[] encrypted = cipher.doFinal(cardnumber.getBytes());
		
		return Base64.encodeBase64String(encrypted);
	}
	
	
	/**
	 * This  class is used to decrypt the card number using clear DMK 
	 * 
	 * @param cdmk
	 * @param ivFactor
	 * @param encryptCardnumber
	 * @return  Decrypt card number
	 * @throws KeyException
	 * @throws Exception
	 */
	public static String decrypt(String cdpk,byte[] ivFactor,String encryptCardnumber) throws KeyException, Exception{
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encryptCardnumber));

		// System.out.println("decrypt string:--------> " +
		// Base64.encodeBase64String(decrypted));

		String s = new String(decrypted);
		return s;
	}
	
	
	
	
	
	public byte[]  test(byte[] ivFactor,String clearDmk)throws Exception{
		
		String key="1234567890123459";
		IvParameterSpec iv = new IvParameterSpec(ivFactor);  
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		
		byte[] encrypted = cipher.doFinal(clearDmk.getBytes());
		
		return encrypted;
		
	}
	
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
	
	public static byte[] hexStringToByteArray(String s) {

		int len = s.length();

		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	
	
	public String getCheckDigit(String data) throws Exception {
		if (data == null) {
			return "-1";
		}

		if (data.length() != 32 && data.length() != 64) {
			return "1";
		}

		//PadssSecurity Sec = new PadssSecurity();
		byte[] Result = null;
		//byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] DataBytes = hexStringToByteArray(data);
		
		CardEncryptAndDecrypt ed=new CardEncryptAndDecrypt();
		//Result = Sec.DesEncrypt(DataBytes, Dummy);
		Result=ed.test(DataBytes, Dummy);
		// System.out.println(getHex(Result));
		return getHex(Result);
	}

}
