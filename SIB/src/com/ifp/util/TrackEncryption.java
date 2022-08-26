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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
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

public class TrackEncryption extends BaseAction {
	public static KeySpec keySpec;
	public static SecretKey key;
	public static IvParameterSpec iv;
	private static final long serialVersionUID = 1L;

	CommonDesc commondesc = new CommonDesc();

	// HttpServletRequest request;

	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	/*
	 * public HttpServletRequest getRequest() { return request; }
	 * 
	 * public void setRequest(HttpServletRequest request) { this.request =
	 * request; }
	 */

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public String encryptHome() {
		trace("called encrypthome method");
		return "encrypthomepage";
	}

	public String decryptHome() {
		trace("called decryptHome method");
		return "decrypthomepage";

	}

	public String encrypt(String clearvalue) {
		String usernameEnkey = null;
		StringBuilder sb=new StringBuilder();
		trace("called encrypt method");
		String key1 = "+Dqfq7kWkRoy9xd1";
		String key2 = "l67bKWrftIc1OS/NmNK7M+fjTUftigH2WUydQ/j0ObTHs/eP5ZFCvdV0tuDNb8f5";
		
		try {
			String s = ASCIItoHEX(key2);
		
			String sub = s.substring(0, 16);
			byte[] IV = new byte[sub.length()];
			usernameEnkey = Encryption(key1.getBytes(), IV, clearvalue);
			
			//nullify 
			clearvalue="000";
			clearvalue=null;
			sb.append(clearvalue);
			sb.delete(0, sb.length());
		} catch (Exception e) {
			e.getMessage();
			//trace("exception occured in encryption " + e.getMessage());
		}finally {
			
			///nullify 
			clearvalue=null; 
		    sb=null;
		}
		return usernameEnkey;
	}

	public String decrypt(String encryptedvalue) {

		/*
		 * Properties prop = commondesc.getCommonDescProperty(); String FullKey1
		 * = prop.getProperty("EPVK"); System.out.println(
		 * "checking full key--->"+FullKey1); String
		 * key1=(FullKey1.substring(16, 32)); trace(
		 * "final key1 length checking -->"+key1);
		 */
		String key1 = "+Dqfq7kWkRoy9xd1";
		String key2 = "l67bKWrftIc1OS/NmNK7M+fjTUftigH2WUydQ/j0ObTHs/eP5ZFCvdV0tuDNb8f5";

		String usernameEnkey = null;
		trace("called decrypt method");
		//HttpSession session = getRequest().getSession();
		//trace("valuesss" + encryptedvalue);
		try {

			String s = ASCIItoHEX(key2);
			//System.out.println(s.length());
			//System.out.println("len--->   " + s);

			// int len = (s.length()) / 8;
			String sub = s.substring(0, 16);
			//System.out.println(sub.length());
			byte[] IV = new byte[sub.length()];
			//System.out.println(IV);
			usernameEnkey = decryptt(key1.getBytes(), IV, encryptedvalue);
			//System.out.println("Encrypted----->   " + encryptedvalue);
			//System.out.println("decryptdd----->   " + usernameEnkey);
		} catch (Exception e) {
			e.getMessage();
			//trace("exception occured in encryption " + e.getMessage());
		}
		return usernameEnkey;
	}

	public String Encryption(byte[] key, byte[] ivfactor, String values) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivfactor);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(values.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String Decryption(String values) {
		Properties prop = commondesc.getCommonDescProperty();
		String FullKey1 = prop.getProperty("EPVK");
		//System.out.println("checking full key--->" + FullKey1);
		String key1 = (FullKey1.substring(16, 32));
		//trace("final key1 length checking -->" + key1);
		String key2 = "l67bKWrftIc1OS/NmNK7M+fjTUftigH2WUydQ/j0ObTHs/eP5ZFCvdV0tuDNb8f5";
		String decryptedval = null;
		try {
			String s = ASCIItoHEX(key2);
			//System.out.println(s.length());
			//System.out.println("len--->   " + s);
			String sub = s.substring(0, 16);
		//	System.out.println(sub.length());
			byte[] IV = new byte[sub.length()];
			//System.out.println(IV);

			IvParameterSpec iv = new IvParameterSpec(IV);
			SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(values));
			decryptedval = new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedval;
	}

	public static String ASCIItoHEX(String ascii) {
		// Initialize final String
		String hex = "";
		// Make a loop to iterate through
		// every character of ascii string
		for (int i = 0; i < ascii.length(); i++) {
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

	public String decryptt(byte[] key, byte[] ivfactor, String encrptedText) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivfactor);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encrptedText));
			//System.out.println("decrypt string:--------> " + Base64.encodeBase64String(decrypted));
			return new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}