package test;


import java.security.KeyException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * This class is used for Encrypt and decrypt of Keys(DMK,DPK) using AES 
 * 
 * @author CGSPL
 * @since  02-07-2019
 * @version 2.0
 */
public class KeysEncryptAndDecrypt {
	public static void main(String[] args) throws KeyException, Exception {
		
	String clearDmk="8E22E7EF53EA9478EDE7499C270FC38F";
	byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	String claerDpk="F519413F71658082F519413F71658082";
	
	String edpk=KeysEncryptAndDecrypt.encrypty(clearDmk, iv, claerDpk);
	
	System.out.println("clear dpk before encryption---->    "+claerDpk);
	
	System.out.println("ecrypted dpk--->    "+edpk);
	
	String dpk=KeysEncryptAndDecrypt.decrypt(clearDmk, iv, edpk);
	System.out.println("clear dpk after de cryption -->    "+dpk);
	
	}
	
	/**
	 * 
	 * @param cdmk
	 * @param ivFactor
	 * @param cdpk
	 * @return   Encrypt DPK
	 * @throws KeyException
	 * @throws Exception
	 */
	
	public static  String encrypty(String cdmk,byte[] ivFactor,String cdpk) throws KeyException, Exception{
		
		IvParameterSpec iv = new IvParameterSpec(ivFactor);  
		SecretKeySpec skeySpec = new SecretKeySpec(cdmk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		
		byte[] encrypted = cipher.doFinal(cdpk.getBytes());
		
		return Base64.encodeBase64String(encrypted);
	}
	

	/**
	 * 
	 * @param cdmk
	 * @param ivFactor
	 * @param edpk
	 * @return Clear DPK
	 * @throws KeyException
	 * @throws Exception
	 */
	public static String decrypt(String cdmk,byte[] ivFactor,String edpk) throws KeyException, Exception{
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdmk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(edpk));

		// System.out.println("decrypt string:--------> " +
		// Base64.encodeBase64String(decrypted));

		String s = new String(decrypted);
		return s;
	}

}
