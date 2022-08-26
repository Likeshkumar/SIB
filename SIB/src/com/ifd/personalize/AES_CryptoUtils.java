package com.ifd.personalize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


import bigjava.security.SecureRandom;
import bwmorg.bouncycastle.crypto.CryptoException;
 
/**
 * A utility class that encrypts or decrypts a file.
 * @author www.codejava.net
 *
 */
public class AES_CryptoUtils {

   // private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
   // private static final String TRANSFORMATION = "AES/ECB/ISO10126Padding";
	
    public static byte[] encrypt(String key, byte[] Inbyte ) throws CryptoException, UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {

    	byte[] Outbyte = doCrypto_enc(Cipher.ENCRYPT_MODE, key, Inbyte );
    	//System.out.println("Outbyte"+Outbyte);
    //byte[] Outbyte1 = doCrypto_dec(Cipher.DECRYPT_MODE, key, Outbyte );

    	return Outbyte;
    }
 
    public static byte[] decrypt(String key, byte[] Inbyte ) throws CryptoException, UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
    	byte[] Outbyte = doCrypto_dec(Cipher.DECRYPT_MODE, key, Inbyte );
        return Outbyte;
    }
 
    private static byte[] doCrypto_enc(int cipherMode, String key, byte[] Inbyte ) throws CryptoException, UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
    	byte[] Outbyte =null;
        try {
            final String ALGORITHM = "AES";
            final String TRANSFORMATION = "AES";
        	
        	byte[] HexByte = new BigInteger(key,16).toByteArray();
            Key secretKey = new SecretKeySpec(HexByte, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey );
            
            Outbyte = cipher.doFinal(Inbyte);
            
            String OutFile = new String(Outbyte);
           // System.out.println("enc OutPut Buff:"+OutFile.toString());
           
        } catch ( InvalidKeyException ex) {
            throw new CryptoException("Error encrypting/decrypting file"+ex.getMessage());
        }
		return Outbyte;
    }
    private static byte[] doCrypto_dec(int cipherMode, String key, byte[] Inbyte ) throws CryptoException, UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
    	byte[] Outbyte =null;
        try {
            final String ALGORITHM = "AES";
            final String TRANSFORMATION = "AES/ECB/ISO10126Padding";
            
        	byte[] HexByte = new BigInteger(key,16).toByteArray();
            Key secretKey = new SecretKeySpec(HexByte, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey );
            
            Outbyte = cipher.doFinal(Inbyte);
            
            String OutFile = new String(Outbyte);
            System.out.println("DEC_OutPut Buff:"+OutFile.toString());
           
        } catch ( InvalidKeyException ex) {
            throw new CryptoException("Error encrypting/decrypting file"+ex.getMessage());
        }
		return Outbyte;
    } 
    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
    	 
        //File inputFile = new File("E://CARDMAN_LOGS/PRE/090817/5512701005_ccmaker_09082017_141210.mc");
 /*       File encryptedFile = new File("C:/Users/cgspl/Desktop/TEMP_FILE/TEST_1_AES_32byte_ENC_01FEB17.csv");
        File decryptedFile = new File("C:/Users/cgspl/Desktop/TEMP_FILE/TEST_1_AES_32byte_DEC_01FEB17.csv");
  */      
        File encryptedFile = new File("E://CARDMAN_LOGS/PRE/432281_HIRELXXX_090218.csv");
        File decryptedFile = new File("E://CARDMAN_LOGS/PRE/rnf generated by cardman app/432281_HIRELXXX_090218.csv");
        
 /*       File encryptedFile = new File("C:/Users/cgspl/Desktop/Decrypted/ICCIDACKey_AJEX_Batch_3167408_09022017095322_V_Enc.out");
        File decryptedFile = new File("C:/Users/cgspl/Desktop/Decrypted/ICCIDACKey_AJEX_Batch_3167408_09022017095322_V_Dec.csv");
*/
        byte[] outputBytes = null,D_Crypt_outputBytes = null, tempBytes = null;

        String key = "2370491CC49492D5AEF716AD2C206429";
        
        
        int len= 1;
        try {
  
        	/* Encryption */
          /* FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
            
	        outputBytes = encrypt(key, inputBytes);
            
            FileOutputStream outputStream = new FileOutputStream(encryptedFile);
            outputStream.write(outputBytes);
*/
            /* Decryption */
            FileInputStream D_Crypt_inputStream = new FileInputStream(encryptedFile);
            byte[] D_Crypt_inputBytes = new byte[(int) encryptedFile.length()];
            D_Crypt_inputStream.read(D_Crypt_inputBytes);
            
            D_Crypt_outputBytes = decrypt(key, D_Crypt_inputBytes);
            	
            System.out.println("**** KeyToVendor **** \n"+key);
            FileOutputStream D_Crypt_outputStream = new FileOutputStream(decryptedFile);
            D_Crypt_outputStream.write(D_Crypt_outputBytes);

//            inputStream.close();
//            outputStream.close();
            D_Crypt_inputStream.close();
            D_Crypt_outputStream.close();
            
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}