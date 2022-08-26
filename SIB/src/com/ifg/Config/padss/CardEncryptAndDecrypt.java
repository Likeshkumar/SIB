package com.ifg.Config.padss;

import java.security.KeyException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * 
 * This class is used for encrypt and decrypt the card number using AES
 * 
 * @author CGSPL
 * @since 02-07-2019
 * @version 2.0
 */
public class CardEncryptAndDecrypt {

	public static void main(String[] args) throws KeyException, Exception {

		String clearDmk = "8E22E7EF53EA9478EDE7499C270FC38F";
		String edpk = " lnNoFN5MOhmpklJTDmjcsYX/euNHntuSrxmDtfj1JwtLldOZZ/qqri9h9Ypp1+g/";

		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		KeysEncryptAndDecrypt key = new KeysEncryptAndDecrypt();
		CardEncryptAndDecrypt card=new CardEncryptAndDecrypt();
		String clearDPK = key.decrypt(clearDmk, iv, edpk);
		System.out.println("clear dpk is ---->  " + clearDPK);

		String cardnumber = "8241933789524016";
		System.out.println("before encrypt card number   is ---->  " + cardnumber);

		String encryptCard = card.encrypty(clearDPK, iv, cardnumber);
		System.out.println("encryptCard  is ---->  " + encryptCard);

		String decryptCard = card.decrypt(clearDPK, iv, encryptCard);
		System.out.println("decryptCard  is ---->  " + decryptCard);
	}

	/**
	 * This class is used to encrypt the car number using clear DPK
	 * 
	 * @param cdpk
	 * @param ivFactor
	 * @param cardnumber
	 * 
	 * @return Encrypt card number
	 * @throws KeyException
	 * @throws Exception
	 */
	public  String encrypty(String cdpk, byte[] ivFactor, String cardnumber) throws KeyException, Exception {

		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(cardnumber.getBytes());
System.out.println("encrypted--->"+encrypted);
		return Base64.encodeBase64String(encrypted);
	}

	/**
	 * This class is used to decrypt the card number using clear DMK
	 * 
	 * @param cdmk
	 * @param ivFactor
	 * @param encryptCardnumber
	 * @return Decrypt card number
	 * @throws KeyException
	 * @throws Exception
	 */
	public  String decrypt(String cdpk, byte[] ivFactor, String encryptCardnumber)
			throws KeyException, Exception {
		IvParameterSpec iv = new IvParameterSpec(ivFactor);
		SecretKeySpec skeySpec = new SecretKeySpec(cdpk.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encryptCardnumber));

		System.out.println("decrypt string:--------> " +
		 Base64.encodeBase64String(decrypted));

		String s = new String(decrypted);
		return s;
	}
	
	

}
