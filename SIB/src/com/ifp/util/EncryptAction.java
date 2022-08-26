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
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class EncryptAction extends BaseAction
{
	public static KeySpec keySpec;
	  public static SecretKey key;
	  public static IvParameterSpec iv;
	private static final long serialVersionUID = 1L;
	 
	static CommonDesc commondesc = new CommonDesc();
	 //HttpServletRequest request;
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

/*	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}*/
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	
	public String encryptHome()
	{
		trace("called encrypthome method");
		return "encrypthomepage";
	}
	public String decryptHome()
	{
		trace("called decryptHome method");
		return "decrypthomepage";
		
	}
	
	
	public String encrypt()
	{
		String usernameEnkey=null;
		trace("called encrypt method");
		HttpSession session = getRequest().getSession();
		String Encryptedvalue = getRequest().getParameter("encrypt");
		//trace("valuesss"+Encryptedvalue);
		try
		{
			 usernameEnkey=Encryption(Encryptedvalue);
			 if(!usernameEnkey.equals(" "))
			 {
				 trace("inside trace");
				 addActionMessage("The Encrypted Value is --->>  "+usernameEnkey);
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
	//	trace("valuesss"+decryptedvalue);
		try
		{
			 usernameEnkey=Decryption(decryptedvalue);
			 if(!usernameEnkey.equals(" "))
			 {
				 trace("inside trace");
				 addActionMessage("The Decrypted Value is --->>  "+usernameEnkey);
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
	
	public  static String Encryption(String values)
	{
		String encryptionKey           = "ABCDEFGHIJKLMNOP";
		String encryptedText = "";
        try {
            Cipher cipher   = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] key      = encryptionKey.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(values.getBytes("UTF8"));
           // Base64.Encoder encoder = Base64.getEncoder();
           // encryptedText = encoder.encodeToString(cipherText);
        } 
        catch (Exception E)
        {
             System.err.println("Encrypt Exception : "+E.getMessage());
        }
        return encryptedText;
	}
	
	
	
	public String Decryption(String values)
	{
		String encryptionKey           = "ABCDEFGHIJKLMNOP";
		String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] key = encryptionKey.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            //Base64.Decoder decoder = Base64.getDecoder();
            //byte[] cipherText = decoder.decode(values.getBytes("UTF8"));
            //decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("decrypt Exception : "+E.getMessage());
        }
        return decryptedText;
	}
	
	
	
	
	/*public  String encrypt1(String value) {
	     try 
	     {
	    	trace("encrypted method called");
	        String keyString="8E22E7EF53EA9478EDE7499C270FC38F8E22E7EF53EA9478";
	        final MessageDigest md = MessageDigest.getInstance("md5");
	        final byte[] digestOfPassword = md.digest(Base64.decodeBase64(keyString.getBytes("utf-8")));
	        trace("trace value 1 -->"+digestOfPassword);
	        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
	        trace("trace value 2 -->"+keyBytes);
	        for (int j = 0, k = 16; j < 8;) {
	        keyBytes[k++] = keyBytes[j++];
	        }
	        
	        keySpec = new DESedeKeySpec(keyBytes);
	        trace("trace value 3 -->"+keySpec);
	        key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
	        trace("trace value 4 -->"+key);
	        iv = new IvParameterSpec(new byte[8]);
	        trace("trace value 5 -->"+iv);
	      
	       Cipher ecipher = Cipher.getInstance("DESede/CBC/PKCS5Padding","SunJCE");
	        trace("trace value 6 -->"+ecipher);
	       ecipher.init(Cipher.ENCRYPT_MODE, key, iv);
	       trace("trace value 7 -->"+ecipher);
	       if(value==null)
	         return null;
	       
	       // Encode the string into bytes using utf-8
	       byte[] utf8 = value.getBytes("UTF8");
	       trace("trace value 8 -->"+utf8);
	       // Encrypt
	       byte[] enc = ecipher.doFinal(utf8);
	       trace("trace value 9 -->"+enc);
	       // Encode bytes to base64 to get a string
	       return new String(Base64.encodeBase64(enc),"UTF-8");
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
	     return null;
	   }
	
	public static String decrypt1(String value) {
	     try {
	       //System.out.println(value); 
	       Cipher dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding","SunJCE");
	       String keyString="8E22E7EF53EA9478EDE7499C270FC38F8E22E7EF53EA9478";
	       final MessageDigest md = MessageDigest.getInstance("md5");
	       final byte[] digestOfPassword = md.digest(Base64.decodeBase64(keyString.getBytes("utf-8")));
	       final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
	       for (int j = 0, k = 16; j < 8;) {
	         keyBytes[k++] = keyBytes[j++];
	       }
	       
	       keySpec = new DESedeKeySpec(keyBytes);
	       
	       key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
	       iv = new IvParameterSpec(new byte[8]);

	       dcipher.init(Cipher.DECRYPT_MODE, key, iv);
	       
	       if(value==null)
	         return null;
	       
	       // Decode base64 to get bytes
	       byte[] dec = Base64.decodeBase64(value.getBytes());
	       
	       // Decrypt
	       byte[] utf8 = dcipher.doFinal(dec);
	       //System.out.println(new String(utf8, "UTF8"));
	       // Decode using utf-8
	       return new String(utf8, "UTF8");
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
	     return null;
	}*/

	
	public static String decrypt1(String value) 
	{
	    	Properties prop = commondesc.getCommonDescProperty();
	 		String FullKey1 = prop.getProperty("CIPHER");
	 		//System.out.println("checking full key--->"+FullKey1);
	 		String key1=(FullKey1.substring(0, 16));
	 		//System.out.println("final key1 length checking -->"+key1);
	 		String key2 = "l67bKWrftIc1OS/NmNK7M+fjTUftigH2WUydQ/j0ObTHs/eP5ZFCvdV0tuDNb8f5";
	 		try 
	 		{
	 			String values=value;
	 			String s = ASCIItoHEX(key2);
	 			//System.out.println(s.length());
	 			//System.out.println( "len--->   "+s);
	 			String sub = s.substring(0, 16);
	 			//System.out.println(sub.length());
	 			byte[] IV = new byte[sub.length()];
	 			//System.out.println(IV);
	 			
	 			IvParameterSpec iv = new IvParameterSpec(IV);
	 			SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes(), "AES");
	 			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	 			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	 			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(values));
	 			return new String(decrypted);
	 		} 
	 		catch (Exception e) 
	 		{
	 			e.printStackTrace();
	 		}
	     return null;
	}
	
	public static String ASCIItoHEX(String ascii) 
	{
		// Initialize final String
		String hex = "";
		// Make a loop to iterate through
		// every character of ascii string
		for (int i = 0; i < ascii.length(); i++) 
		{
			// take a char from
			// position i of string
			char ch = ascii.charAt(i);
			// cast char to integer and
			// find its ascii value
			int in = (int) ch;
			// change this ascii value
			// integer to hexadecimal value
			String part = Integer.toHexString(in);
			// add this hexadecimal value
			// to final string.
			hex += part;
		}
		// return the final string hex
		return hex;
	}
	
	
	public static void main(String[] args) {
		   
		
		String encTest=Encryption("SIB_PADSS");
		
		
	}
	
}