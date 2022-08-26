package com.ifg.Config.padss;


import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

public class PadssSecurity_DES {

	/**
	 * Used for PADSS Compliance 
	 * Hashing - SHA 512
	 * CHN Encryption / Decryption - 3DES Double Length - 32, Triple Length - 64
	 */
	
	static final String HEXES = "0123456789ABCDEF";
	static final String LMK = "0123456789ABCDEFFEDCBA9876543210";   
	static final String Dummy = "00000000000000000000000000000000";
	static final String Padding = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
	
	public byte[] DesEncrypt(byte[] plainTextBytes, String hkey) throws Exception  {
		if( hkey == null ){ return null; }
		
		if( hkey.length() == 32 )
			hkey = hkey+hkey.substring(0,16);
	
		byte[] keyValue =  hexStringToByteArray(hkey) ;// Hex.decodeHex(hkey.toCharArray());  
		
		final SecretKey key = new SecretKeySpec(keyValue, "DESede");  
		
		final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");   
		
		cipher.init(Cipher.ENCRYPT_MODE, key);   
		
		final byte[] cipherText = cipher.doFinal(plainTextBytes);   
		
		return cipherText;
	} 
	 
	public String DesDecrypt(byte[] message, String hkey) throws Exception {
		if( hkey == null ){ return null; }
	   	
		if( hkey.length() == 32 )
	   		hkey = hkey+hkey.substring(0,16);
	   	
	    byte[] keyValue =  hexStringToByteArray(hkey);// Hex.decodeHex(hkey.toCharArray());   
	   
	    final SecretKey key = new SecretKeySpec(keyValue, "DESede");  
	   
	    final Cipher decipher = Cipher.getInstance("DESede/ECB/NoPadding");  
	    
	    decipher.init(Cipher.DECRYPT_MODE, key);   
	    
	    final byte[] plainText = decipher.doFinal(message);   
	    
	    return getHex(plainText);  
	}  
	   
	public static byte[] hexStringToByteArray(String s) {

		int len = s.length();
	    
		byte[] data = new byte[len / 2];
	    
		for (int i = 0; i < len; i += 2) 
		{
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	    }
		return data;
	}
	     
	    
	public static String getHex( byte [] raw ) throws Exception {
		if( raw == null ){ return null; }
		
		final StringBuilder hex = new StringBuilder( 2 * raw.length );  
		
		for ( final byte b : raw ) 
		{
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));  
		}
		return hex.toString();  
	}  
	
	private static int char_to_int( char hexval ){
		if( Character.isDigit(hexval) )
			return ( hexval - 48 );
		else
			return ( hexval - 55 );
	}
	
	private static char int_to_char( int val ){
		if( val < 10 )
			return (char) ( 48 + val );
		else
			return (char) ( 55 + val );
	}
		
		

	public static String xor(String data1,String data2 ){
		char[] data1arr = data1.toCharArray();
		char[] data2arr = data2.toCharArray();
		char[] res = new char[data2.length()];
		
		for( int i=0; i< data2arr.length; i++ )
		{
			res[ i ] = int_to_char ( ( char_to_int( data1arr[ i ] ) ) ^ ( char_to_int( data2arr[ i ] ) ) );
		} 
			
		String formattedres = String.valueOf(res);
			
		return formattedres;
	}
	
	public String getFormattedKey(String data1,String data2)throws Exception{
		if( data1 == null && data2 == null  ){ return "-1"; }
		//if( data1.length() == data2.length()  ){ return "3"; }
		String R1 = null;
		
		R1 = xor( data1, data2 );
		//System.out.println("Result1--->" + R1 );
		return R1;
	}

	public  String getFormattedKey(String data1, String data2, String data3)throws Exception{
		if( data1 == null && data2 == null && data3 == null ){ return "-1"; }
		//if( data1.length() == data2.length() && data1.length() == data3.length()  ){ return "3"; }
		
		String R1 = null;
		String R2 = null;
		
		R1 = xor( data1, data2 );
		//System.out.println("Result1--->" + R1 );
		
		R2 = xor( R1, data3 );
		//System.out.println("Result1--->" + R1 );
		return R2;
	}
	
	public StringBuffer getHashedValue( String inputdata ) throws Exception{
		if( inputdata == null ){ return null; }
		
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		//inputdata = inputdata.replaceAll("ORBL", "ORIENT");
		md.update(inputdata.getBytes());
	 
	    byte byteData[] = md.digest();
	       
	    //convert the byte to hex format method 1
	    /*StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < byteData.length; i++) 
	    {
	    	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    }*/
	       
	    //System.out.println("Hex format : " + sb.toString()); 
	 
	    //convert the byte to hex format method 2
	    StringBuffer hexString = new StringBuffer();
	    for (int i=0;i<byteData.length;i++) 
	    {
	    	String hex=Integer.toHexString(0xff & byteData[i]);
	    	if(hex.length()==1) hexString.append('0');
	    	hexString.append(hex);
	    }

	    //System.out.println("Hex format : " + hexString.toString());
		return hexString;
	}
	
	public String getCheckDigit(String data) throws Exception{
		if( data == null ){ return "-1"; }
		
		if( data.length() != 32 && data.length() != 64 ){ return "1"; }
		
		PadssSecurity_DES Sec = new PadssSecurity_DES();
		byte[] Result = null;
		
		
		byte[] DataBytes = hexStringToByteArray(data);
		
		Result = Sec.DesEncrypt( DataBytes, Dummy);
        //System.out.println(getHex(Result));
        return getHex(Result);
	}
	
	
	public String getEDMK(String cDMK) throws Exception{
		if( cDMK == null ){ return "-1"; }
		
		if( cDMK.length() != 32 && cDMK.length() != 64 ){ return "1"; }
		
		
		PadssSecurity_DES Sec = new PadssSecurity_DES();
		byte[] Result = null;
		
		
		byte[] DataBytes = hexStringToByteArray(cDMK);
		
		Result = Sec.DesEncrypt( DataBytes, LMK);
        //System.out.println(getHex(Result));
        return getHex(Result);
	}
	
	public String getEDPK(String eDMK, String cDPK) throws Exception{
		if( eDMK == null ){ return "-1"; }
		
		if( eDMK.length() != 32 && eDMK.length() != 64 ){ return "1"; }
		
		if( cDPK == null ){ return "-1"; }
		
		if( cDPK.length() != 32 && cDPK.length() != 64 ){ return "2"; }
		
		PadssSecurity_DES Sec = new PadssSecurity_DES();
		byte[] Result = null;
		String cDMK = null;
		
		byte[] DataBytes = hexStringToByteArray(eDMK);
		
		cDMK = Sec.DesDecrypt(DataBytes, LMK);
        //System.out.println("clear DMK : " + cDMK);
        
        DataBytes = null;
        
        DataBytes = hexStringToByteArray(cDPK);
        Result = Sec.DesEncrypt( DataBytes, cDMK);
        //System.out.println(getHex(Result));
        return getHex(Result);
	}

	public String getECHN(String eDMK, String eDPK, String CHN) throws Exception{
		PadssSecurity_DES Sec = new PadssSecurity_DES();
		byte[] Result = null;
		String cDMK = null;
		String cDPK = null;
		
		String fCHN = null;
		
		byte[] DataBytes = hexStringToByteArray(eDMK);
				
		cDMK = Sec.DesDecrypt(DataBytes, LMK);
        //System.out.println("clear DMK : " + cDMK);
        
        DataBytes = null;
		DataBytes = hexStringToByteArray(eDPK);
		
		cDPK = Sec.DesDecrypt(DataBytes, cDMK);
        //System.out.println("clear DPK : " + cDPK);
		
		fCHN=StringUtils.rightPad(CHN, 32,"F");
		
        DataBytes = hexStringToByteArray(fCHN);
        Result = Sec.DesEncrypt( DataBytes, cDPK);
        //System.out.println(getHex(Result));
        return getHex(Result);
	}

	public String getCHN(String eDMK, String eDPK, String eCHN) throws Exception{
		PadssSecurity_DES Sec = new PadssSecurity_DES();
		byte[] Result = null;
		String cDMK = null;
		String cDPK = null;
		String CHN = null;
		String fCHN = null;
		
		byte[] DataBytes = hexStringToByteArray(eDMK);
				
		cDMK = Sec.DesDecrypt(DataBytes, LMK);
        //System.out.println("clear DMK : " + cDMK);
        
        DataBytes = null;
		DataBytes = hexStringToByteArray(eDPK);
		
		cDPK = Sec.DesDecrypt(DataBytes, cDMK);
        //System.out.println("clear DPK : " + cDPK);
        
        DataBytes = hexStringToByteArray(eCHN);
        fCHN = Sec.DesDecrypt( DataBytes, cDPK);
        CHN=fCHN.replaceAll("F", "");
        
        //System.out.println(CHN);
        return CHN;
	}
	
	public Properties getCommonDescProperty(){
		String filename = "CommonDesc.properties";
		Properties prop = new Properties();
		InputStream ins = null;
		try {
			 ins = this.getClass().getClassLoader().getResourceAsStream(filename);
			prop.load( ins );
			if( ins != null ){ ins.close(); }
		} catch (Exception e) {
			 System.out.println("Exception getting property : " + e.getMessage());
			e.printStackTrace();
		} 
		return prop;
	} 
	
	public String getMakedCardno(String cardno)
	{
		 int index = 0;
		 
			Properties prop = getCommonDescProperty();
			String mask = "";
			if(cardno.length()==16)
			{
				mask = prop.getProperty("cardno16.mask");
			}
			else if(cardno.length()==19)
			{
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
public static void main(String[] args) throws Exception {
	
	System.out.println("Security Module");
	PadssSecurity_DES Sec = new PadssSecurity_DES();
	
	
	StringBuffer ResultString = null;
	
	String CHN = "4137031001000001";        
	     
	System.out.println("Clear--->" + CHN + "IBA");
	
	ResultString = Sec.getHashedValue(CHN + "IBA");
	
	System.out.println("Hashed CHN--->" + ResultString );
	
	//d01e9601ece7ac14fde4baeff894eb09a18643146835812e472498ddf22a50f3710e7f744a21adcd542728497e08e8ae9cfc46993ef26e7453e520bc794b3f8f
	
	
	//a852b48a6c089f67ecad17382bd32301a33feaf4b0e9ec2c81c92ae811b7ec7c2810dfd8b57742a8dbcce574e742b6adcf2dc5250eba0b35b75ff605312255c6

	
	
	String R1 = null;
	String ClearDMK = null;
	String EDMK = null;
	String DMK_KVC = null;
	String ClearDPK = null;
	String EDPK = null;
	String DPK_KVC = null;
	String ECHN = null;
	
	//Step 1 -- Get Encrypted DMK
	String DMKComponent1 = "11112222333344445555666677778888";
	String DMKComponent2 = "88887777666655554444333322221111";
	String DMKComponent3 = "AAAABBBBCCCCDDDDEEEEFFFF99990000";
	
	ClearDMK = Sec.getFormattedKey(DMKComponent1, DMKComponent2, DMKComponent3);
	System.out.println("ClearDMK--->" + ClearDMK );
	
	DMK_KVC = Sec.getCheckDigit(ClearDMK);
	System.out.println("DMK KCV--->" + DMK_KVC ); //For Display & Store in DB
	
	EDMK = Sec.getEDMK(ClearDMK);
	System.out.println("Encrpted DMK--->" + EDMK );//Store in DB
	
	//Step 2 -- Get Encrypted DPK
	String DPKComponent1 = "0123456789ABCDEFFEDCBA9876543210";
	String DPKComponent2 = "FEDCBA98765432100123456789ABCDEF";
		
	ClearDPK = Sec.getFormattedKey(DPKComponent1, DPKComponent2);
	System.out.println("ClearDPK--->" + ClearDPK );
	
	
	
	
	DPK_KVC = Sec.getCheckDigit(ClearDPK);
	System.out.println("DPK KCV--->" + DPK_KVC ); //For Display & Store in DB
	
	EDPK = Sec.getEDPK(EDMK, ClearDPK);
	System.out.println("Encrpted DPK--->" + EDPK );//Store in DB
	
	//Step 3 -- Get Encrypted CHN
	

	ECHN = Sec.getECHN(EDMK, EDPK, CHN);
	System.out.println("Encrpted CHN--->" + ECHN );//Store in DB
	
	//Step 4 -- Get Clear CHN
	CHN = null;
	CHN = Sec.getCHN(EDMK, EDPK, ECHN);
	System.out.println("Decrpted CHN--->" + CHN );//For Processing
	
	//StringUtils.leftPad("", 1,"0");
	/* TEST DATA
	 * Security Module
	Clear--->4322810000000401ORBL
	Hashed CHN--->b3b1f344bf9e375f3181d7b9eaa4a168585044278bc3ce054d32668e5334a44f5adc084cf52cd94fb6bde97e8efa9a8019f338d41a51d0f9b6fcf9795971b761
	ClearDMK--->3333EEEE9999CCCCFFFFAAAACCCC9999
	DMK KCV--->F2019E7E7D614058D94CA2E4A75D45CF
	Encrpted DMK--->8E22E7EF53EA9478EDE7499C270FC38F
	ClearDPK--->FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
	DPK KCV--->355550B2150E2451355550B2150E2451
	Encrpted DPK--->F519413F71658082F519413F71658082
	Encrpted CHN--->3F474F0D948A91A5
	Decrpted CHN--->4322810000000401
	 * 
	 */
	
	System.out.println("checking::"+StringUtils.rightPad(CHN, 32,"F"));
	
	
}

}