package test;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class testsha
{
	//testsha pswd_hash=new testsha();
	public final int ITERATION_NUMBER=5;
	String key ="SURESH";
	 /*
	public byte[] encryptPassword(String password,String )throws Exception
    {
    	
    //	byte[] btkey=encoder.encode(key);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
 
        byte byteData[] = md.digest();
       
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        System.out.println("Hex format : " + sb.toString()); 
 
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	System.out.println("Hex format : " + hexString.toString());
    	
    	
        return byteData;
    }
    */
	public byte[] getHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
	       MessageDigest digest = MessageDigest.getInstance("SHA-512");
	       digest.reset();
	       byte[] input = digest.digest(password.getBytes("UTF-8"));
	       return input;
	   }
	   
	
    public  byte[] base64ToByte(String data) throws IOException  
    {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }
    
    public String byteToBase64(byte[] data){
        BASE64Encoder endecoder = new BASE64Encoder();
        return endecoder.encode(data);
    }
    
    public byte[] encryptPassword(String password )throws Exception
    {
    	
    //	byte[] btkey=encoder.encode(key);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(password.getBytes());
 
        byte byteData[] = md.digest();
       
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        System.out.println("Hex format : " + sb.toString()); 
 
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	System.out.println("Hex format : " + hexString.toString());
    	
    	
        return byteData;
    }
  
    public static void main(String[] args) throws Exception {
    	testsha a = new testsha();
    	
    	a.encryptPassword("4322810000000401ORBL");
	
    //	boolean pswdcheck_result=a.checkPassword("y4xfFfHZGEkvaGgbP0ZD7YEBVvM=","IKc7bKmt7Mg=","Asdf@1234","ORBL");
    	
    //	boolean pswdcheck_result=a.checkPassword("b3b1f344bf9e375f3181d7b9eaa4a168585044278bc3ce054d32668e5334a44f5adc084cf52cd94fb6bde97e8efa9a8019f338d41a51d0f9b6fcf9795971b761","ORBL","4322810000000401ORBL","ORBL");
    	
   // 	System.out.println("asdf:"+pswdcheck_result);
	}

}
