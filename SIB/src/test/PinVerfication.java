package test;

import java.security.KeyException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class PinVerfication {

	public static void main(String[] args) throws Exception {
		
		String key="13DC8C25FD76F2D9";
		String cardno="9004040010311630";
		
		String pin="5171";
		
		
		
		System.out.println(" enc string ::  "+getEncrypted(key, pin));
       System.out.println(" desc string ::  "+getDecrypted(key, "p9k08vxkzjYgLkW4xhWGWu/sF5pfWTQ8WuT3gXTSrXU="));
		
	}
	
	
	
	public static String getEncrypted(String key, String pin) throws KeyException, Exception {

		byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String encryptedText = null;

		try {
			IvParameterSpec iv = new IvParameterSpec(ivFactor);
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(pin.getBytes());
			encryptedText = Base64.encodeBase64String(encrypted);

		} catch (Exception exce) {
			exce.printStackTrace();
		}
		return encryptedText;
	}

	
	
	
	
	
	
	
	
	
	
	/**
	 * This method is used to get The clear Pin
	 * 
	 * @param key
	 * @param encryptPin
	 * @return
	 * @throws KeyException
	 * @throws Exception
	 */
	public  static String getDecrypted(String key, String encryptPin) throws KeyException, Exception {

		String clearText = null;
		try {
			byte[] ivFactor = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec iv = new IvParameterSpec(ivFactor);
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encryptPin));
			clearText = new String(decrypted);
		} catch (Exception exce) {
			exce.printStackTrace();
		}
		return clearText;
	}
	
}
