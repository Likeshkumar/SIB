package test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES_Encrypt {

	public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] IV) {
		try {
			// Get Cipher Instance
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// Create SecretKeySpec
			SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

			// Create IvParameterSpec
			IvParameterSpec ivSpec = new IvParameterSpec(IV);

			// Initialize Cipher for ENCRYPT_MODE
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			// Perform Encryption
			byte[] cipherText = cipher.doFinal(plaintext);
			return cipherText;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) throws Exception {
		String edpk = "E3A0EA74E0BD69D889BDD7EA6E719A97";
		String dmk = "0654B24D8167B20F3D139E540BD6145F";
		 String plainText = "5018170010011029";
		 
		 String keysWithText=edpk+dmk+plainText;
		 
		 //System.out.println(keysWithText.getBytes());

		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);

		// Generate Key 
		SecretKey key = keyGenerator.generateKey();

		// Generating IV.
		byte[] IV = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(IV);

		System.out.println("Original Text  : " + plainText);
		byte[] cipherText = encrypt(plainText.getBytes(), key, IV);
		
		System.out.println(cipherText);
		
	       System.out.println("Encrypted Text : "+Base64.encodeBase64String(cipherText));
	       
	       
	       String decryptedText = decrypt(cipherText,key, IV);
	        System.out.println("DeCrypted Text : "+decryptedText);

	}

	
	
    public static String decrypt (byte[] cipherText, SecretKey key,byte[] IV) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        
        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        
        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        
        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);
        
        return new String(decryptedText);
    }
}
