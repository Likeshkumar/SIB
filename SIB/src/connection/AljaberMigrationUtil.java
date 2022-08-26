package connection;

import java.lang.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
//import com.ifp.Action.BaseAction;
//import com.ifg.Config.padss.PadssSecurity;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.xwork.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class AljaberMigrationUtil {

	static final String HEXES = "0123456789ABCDEF";
	
	public String GetMigrationTblDao()
	{
		String qry = "SELECT BIN_NUM, CARD_NUMBER, ENC_PIN, COMPANY_NAME, EMPLOYEE_MOL_ID, FIRST_NAME, LAST_NAME, EMP_NAME,";
		qry += " ACCOUNT_NO, CARD_AGENT_NO, EMPLOYEE_ID, COMPMOL_ID, BATCH_ID, CARD_STATUS, ICCID,  ACCESS_KEY";
		qry += " FROM";
		qry += " ( SELECT rownum AS RN,'504163' AS BIN_NUM, TBL_A.CARD_STATUS, TBL_A.CARD_NUMBER, TBL_A.ORIG_PIN";
		qry += " AS ENC_PIN, TBL_A.COMPANY_NAME, TBL_A.EMPLOYEE_MOL_ID, TBL_A.FIRST_NAME AS FIRST_NAME,";
		qry += " TBL_A.LAST_NAME AS LAST_NAME,TBL_A.EMP_FNAME AS EMP_NAME, TBL_A.ACCOUNT_NO, '316' AS CARD_AGENT_NO ,TBL_A.LEGACY_NO AS EMPLOYEE_ID,";
		qry += " TBL_A.COMPANY_ID AS COMPMOL_ID, TBL_A.BATCH_ID, TBL_A.ICCID, P_PRIVATEKEY.KEYVALUE AS ACCESS_KEY";
		qry += " FROM MIGRATION_ALJABER_EMP_1712_1 TBL_A";
		qry += " INNER JOIN P_PRIVATEKEY ON TBL_A.ICCID=P_PRIVATEKEY.ICCID"; 
		qry += " where TBL_A.ACCOUNT_NO is not null and  TBL_A.ICCID is not null)";
		qry += " WHERE  RN between 0 and 100";
		
		return qry;
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
	
	public int char_to_int( char hexval )
	{         
	        if( Character.isDigit( hexval ) )
	                return ( hexval - 48 );
	        else
	                return ( hexval - 55 );
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
	
	public String GetPinBLock(String chn, String Pin )
	{
		String Mask_block = "FFFFFFFFFFFFFFFF";
        String clear_block, ChnBlock, clear_pinblock="", tempChn;
        int i,length=0;
        
        System.out.println("Chn ="+chn+" : Pin ="+Pin+" :");
        clear_block = '0' + Integer.toString(Pin.length()) + Pin;
        clear_block += Mask_block.substring(clear_block.length());
        System.out.println("clear_block len ="+clear_block.length()+": clear_block ="+clear_block);
        
        ChnBlock = "000";
        tempChn = chn.substring(0, chn.length()-1);
        System.out.println("tempchn ="+tempChn);
        ChnBlock += '0' +tempChn.substring(ChnBlock.length());
        System.out.println("ChnBlock.length() ="+ChnBlock.length()+"ChnBlock ="+ChnBlock);
                
        if( ChnBlock.length() == 16 && clear_block.length() ==16 ){
        	
        	for( i = 0; i < 16; i++ ){
        		clear_pinblock += Integer.toHexString(char_to_int( clear_block.charAt(i) ) ^ char_to_int( ChnBlock.charAt(i) ));
        		//System.out.println(+i +"Trace ->"+a+": "+b+" : "+c+" : "+((char)c)+"-> "+clear_pinblock+"");
        	}
        	
        }
        System.out.println("clear_pinblock ="+clear_pinblock);
		return clear_pinblock;
	}
	
	public String SDesDecrypt(byte[] message, String hkey) throws Exception {
		if( hkey == null ){ return null; }
	   	
	    byte[] keyValue =  hexStringToByteArray(hkey);// Hex.decodeHex(hkey.toCharArray());   
	   
	    final SecretKey key = new SecretKeySpec(keyValue, "DESede");  
	   
	    final Cipher decipher = Cipher.getInstance("DESede/ECB/NoPadding");  
	    
	    decipher.init(Cipher.DECRYPT_MODE, key);
	    
	    final byte[] plainText = decipher.doFinal(message);   
	    
	    return getHex(plainText);
	}  
	
	public byte[] SDesEncrypt(byte[] plainTextBytes, String hkey) throws Exception  {
		if( hkey == null ){ return null; }
		
		byte[] keyValue =  hexStringToByteArray(hkey) ;// Hex.decodeHex(hkey.toCharArray());  
		
		final SecretKey key = new SecretKeySpec(keyValue, "DESede");  
		
		final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");   
		
		cipher.init(Cipher.ENCRYPT_MODE, key);   
		
		final byte[] cipherText = cipher.doFinal(plainTextBytes);   
		
		return cipherText;
	} 
		
	public String GenPinOffSet(String chn, String Pin ) throws Exception
	{
		
		String PinOffSet="";
		String Clear_Pvk="",DecimalizationTbl="0123456789012345";
		String Lmk = "DDDD11111111DDDDDDDD11111111DDDD", E_Pvk = "11111111111111111111111111111111";
		//String Lmk = "11111111111111111111111111111111", E_Pvk = "11111111111111111111111111111111";
		int PanVal_Offset=0,PanVal_Len=16;
		
		String NaturalPin="",ChnBlock="",EncPanBlock="", tempChn="";
		char[] ChnBlockT = new char[chn.length()];
		char[] PinT = Pin.toCharArray();
		String HexTable = "0123456789ABCDEF";
		byte[] Result = null;
		
		if( Lmk.length() == 32 ) Lmk = Lmk+Lmk.substring(0,16);
		if( E_Pvk.length() == 32 ) E_Pvk = E_Pvk+E_Pvk.substring(0,16);
		
		//System.out.println("Lmk ="+Lmk);System.out.println("E_Pvk ="+E_Pvk);
		
		byte[] DataBytes = hexStringToByteArray(E_Pvk);
		Clear_Pvk = SDesDecrypt(DataBytes, Lmk);
		//System.out.println("Clear_Pvk ="+Clear_Pvk);
		
		for(int i=PanVal_Offset,j=0;i<PanVal_Len+PanVal_Offset;i++,j++)	
			ChnBlockT[j] = chn.charAt(i);
		
		ChnBlock = new String(ChnBlockT);
		//System.out.println("ChnBlock ="+ChnBlock);
		
		DataBytes = hexStringToByteArray(ChnBlock);
		Result = SDesEncrypt( DataBytes, Clear_Pvk);
		EncPanBlock =  getHex(Result);
		Result = null;
		//System.out.println("EncPanBlock ="+EncPanBlock);
		
		/*Getting Natural PIN */		
		char[] Npin= new char[EncPanBlock.length()];
        for(int i=0; i < ChnBlock.length(); i++ )
            for( int j=0; j < DecimalizationTbl.length(); j++ )
                    if( EncPanBlock.charAt(i) == HexTable.charAt(j) )
                    	Npin[i] = DecimalizationTbl.charAt(j);
        
        NaturalPin = new String(Npin);
        //System.out.println("NaturalPin ="+NaturalPin);
        
        /*Getting Pin Offset */
        char[] PinOffT= new char[Pin.length()];
        for(int i=0;i<Pin.length();i++)
        {
                if( Pin.charAt(i) < NaturalPin.charAt(i) )
                		PinOffT[i]= (char) ((PinT[i] - Npin[i] )+ 58);
                else
                		PinOffT[i]= (char) ((PinT[i] - Npin[i] )+ 48);
        }
        PinOffSet=new String(PinOffT);
        
		return PinOffSet;
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
	
	
	public synchronized String generateorderRefno(String inst_id, JdbcTemplate jdbcTemplate) throws Exception	{
		String Order_ref_no="N",refnomax="N",refnum="N"; 
		int ref_len=0;
		
		String refnum_len = "select ORD_REF_LEN from INSTITUTION where trim(INST_ID)='"+inst_id+"'";
		//enctrace("refnum_len : " + refnum_len );
		ref_len = (Integer)jdbcTemplate.queryForObject(refnum_len, Integer.class);
		//trace("Order refme-no len : " + ref_len );
		//String refno = "select 	ORDER_REFNO from SEQUENCE_MASTER where trim(INST_ID)='"+inst_id+"'";
		String refno = "SELECT ORDERREF_SEQUENCE('"+inst_id+"') FROM DUAL";
		//enctrace("refno : " + refno);
		refnum = (String)jdbcTemplate.queryForObject(refno, String.class);
		//trace("Ref-No seq : " + refnum );
		if(!(refnum.equals("N")) && ref_len !=0) 	{ 
			Order_ref_no = orderreferenceno(refnum,ref_len);
		}
		if(!(Order_ref_no.equals("N")) && !(Order_ref_no.equals("X")))	{ 
			refnomax=checkMaxorderrefnum(Order_ref_no,ref_len);
		}
		if(!(refnomax.equals("N")) && !(refnomax.equals("X")))	{  
			if(refnomax.equals("M")) {
				Order_ref_no = "M";
			}
			else {
				Order_ref_no = refnomax;
				 
			}
		}
		//trace("Generatd order-ref-num : " + Order_ref_no);
		return Order_ref_no;
	}
	
	public String orderreferenceno(String refnum,int ref_len)
	{
		String ref_num="X";
		//trace("############### Refnum Recived is "+refnum+"     Refnum Len "+ref_len);
		int curr_len = refnum.length();
		if(curr_len==ref_len)
		{
			ref_num = refnum;
		}
		else if(curr_len<ref_len)
		{
			int refnum_len = refnum.length();
		//	trace(" The Len of Exsist Ref num is "+refnum_len);
			int newlength;
			while(refnum_len != ref_len)
			{
				//trace("Inside While Loop "+ref_len);
				for(int j=0; j<ref_len;j++)
				{
					refnum = "0"+refnum; 
					newlength = refnum.length();
					if(ref_len == newlength)
					{
						break;
					}
				}
				//trace(" Ref Num Generated is "+refnum);
				
				refnum_len = refnum.length();
				//trace("refnum_len===InSide While Loop > "+refnum_len);
			}
			ref_num=refnum;
		}
		return ref_num;
	}
	
	public String checkMaxorderrefnum(String ref,int len)
	{
		//trace("RefNum Received ==>"+ref+"  The Len is ###==> "+len);
		
		String maxno="X";
		String maxnum="";
		for(int n=0;n<len;n++)
		{
			maxnum=maxnum+"9";
			//trace("== maxnum =="+maxnum+"=======");
		}
		//trace("Refnum Before Parse int ==> "+ref);
		long curr_refnum = Long.parseLong(ref);
		//trace("maxnum After "+maxnum.trim());
		long maxnumber = Long.parseLong(maxnum);
		//trace("curr_refnum ====> "+curr_refnum);
		//trace("maxnumber====>"+maxnumber);
		if(curr_refnum==maxnumber)
		{
			//trace("Both Values are Same ");
			maxno = "M";
		}
		else
		{
			maxno = ref;
		}
		//trace("maxno====> "+maxno);
		
		
		return maxno;
	}
	
	public void mtrace(String message){ 
		FileWriter fstream  = null;
		BufferedWriter prewriter  = null;     
		Properties prop = getCommonDescProperty();
		
		
		 try {
			 String logprefix = "CARDMAN_MIGRATION_"; 
			String strfiledir = prop.getProperty("log.migraton.location"); 
			String logfilename = logprefix+getDatetime()+".log";      
			File nwfile = new File( strfiledir+"/"+logfilename); 
			File logfiledir = new File( strfiledir );
			if( logfiledir.exists() ){ 
				if(!nwfile.exists()) {
					nwfile.createNewFile();
				}
			}else{
				System.out.println( "COULD NOT UNDERSTAND THE LOG FILE PATH SPECIEFIED [ "+strfiledir+" ] ");
			}  
			String yesterdaydate = getYesterday(); 
			String existfilename = strfiledir+logprefix+yesterdaydate+".log";
			File f = new File(existfilename);
			if( f.exists() ){
				//f.delete();
				//System.out.println("file_name  "+existfilename + " DELETED SUCCESSFULLY");
			} 
			fstream = new FileWriter(nwfile,true);
			prewriter = new BufferedWriter(fstream); 
			prewriter.write( getDateTimeStamp() + ":" + message + "\n"); 
			prewriter.close();
	
		} catch (Exception e) { 
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}  finally{
			try{			 
				fstream.close();prewriter.close();
			}catch(Exception e){} 
		} 
	}
	public String getDatetime() {
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	public String getDateTimeStamp()
	{
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_m_ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	public static Date getDayBefore(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.add(Calendar.DATE, -30);  
        return cal.getTime();  
    }  
	public String getYesterday() {  
        Date ydate =  getDayBefore(new Date());  
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy"); 
		String yesdate = dateFormat.format(ydate);
		return yesdate;
    } 
	public void fail_err_trace(String message){ 
		FileWriter fstream  = null;
		BufferedWriter prewriter  = null;     
		Properties prop = getCommonDescProperty();
		
		
		 try {
			 String logprefix = "FAILED_MIGRATION_"; 
			String strfiledir = prop.getProperty("log.fail.location"); 
			String logfilename = logprefix+getDatetime()+".log";      
			File nwfile = new File( strfiledir+"/"+logfilename); 
			File logfiledir = new File( strfiledir );
			if( logfiledir.exists() ){ 
				if(!nwfile.exists()) {
					nwfile.createNewFile();
				}
			}else{
				System.out.println( "COULD NOT UNDERSTAND THE LOG FILE PATH SPECIEFIED [ "+strfiledir+" ] ");
			}  
			String yesterdaydate = getYesterday(); 
			String existfilename = strfiledir+logprefix+yesterdaydate+".log";
			File f = new File(existfilename);
			if( f.exists() ){
				//f.delete();
				//System.out.println("file_name  "+existfilename + " DELETED SUCCESSFULLY");
			} 
			fstream = new FileWriter(nwfile,true);
			prewriter = new BufferedWriter(fstream); 
			prewriter.write( getDateTimeStamp() + ":" + message + "\n"); 
			prewriter.close();
	
		} catch (Exception e) { 
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}  finally{
			try{			 
				fstream.close();prewriter.close();
			}catch(Exception e){} 
		} 
	}
	
	public void success_trace(String message){ 
		FileWriter fstream  = null;
		BufferedWriter prewriter  = null;     
		Properties prop = getCommonDescProperty();
		
		
		 try {
			 String logprefix = "SUCCESS_MIGRATION_"; 
			String strfiledir = prop.getProperty("log.success.location"); 
			String logfilename = logprefix+getDatetime()+".log";      
			File nwfile = new File( strfiledir+"/"+logfilename); 
			File logfiledir = new File( strfiledir );
			if( logfiledir.exists() ){ 
				if(!nwfile.exists()) {
					nwfile.createNewFile();
				}
			}else{
				System.out.println( "COULD NOT UNDERSTAND THE LOG FILE PATH SPECIEFIED [ "+strfiledir+" ] ");
			}  
			String yesterdaydate = getYesterday(); 
			String existfilename = strfiledir+logprefix+yesterdaydate+".log";
			File f = new File(existfilename);
			if( f.exists() ){
				//f.delete();
				//System.out.println("file_name  "+existfilename + " DELETED SUCCESSFULLY");
			} 
			fstream = new FileWriter(nwfile,true);
			prewriter = new BufferedWriter(fstream); 
			prewriter.write( getDateTimeStamp() + ":" + message + "\n"); 
			prewriter.close();
	
		} catch (Exception e) { 
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}  finally{
			try{			 
				fstream.close();prewriter.close();
			}catch(Exception e){} 
		} 
	}
	
	public static byte[] decrypt(byte[] encryptedText, Key SecretKeySpec) throws NoSuchAlgorithmException
	{
		byte[] decryptedText = (byte[])null;
		try
		{
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			//logger.debug("SecretKeySpec >>" + SecretKeySpec + "  encryptedText >>" + encryptedText);
			cipher.init(2, SecretKeySpec);
			decryptedText = cipher.doFinal(encryptedText);
			//logger.debug("decryptedText >>" + decryptedText);
		}
		catch (Exception e)
		{
			System.err.println(e);
				      //e.printStackTrace();
		}
		return decryptedText;
  	}
	
	public String GetAccessKey(String E_Accesskey, String Key ) throws Exception
	{
		if( Key.length() == 32 ) Key = Key+Key.substring(0,16);
		//System.out.println("E_Accesskey ="+E_Accesskey);System.out.println("Key ="+Key);
		
		byte[] DataBytes = hexStringToByteArray(E_Accesskey);
		byte[] KeyBytes = hexStringToByteArray(Key);
		String Clear_Accesskey = SDesDecrypt(DataBytes, Key);
	
	return Clear_Accesskey;
	}
	
	/*public IfpTransObj myTranObject(String setname,PlatformTransactionManager  txManager) 
	{  
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);
		setname=setname+randomInt;
		mtrace(" Transaction Name: "+setname);
		def.setName(setname);
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);//ISOLATION_SERIALIZABLE
		mtrace(" Test1: ");
		TransactionStatus status = txManager.getTransaction(def); 
		mtrace(" Test2: ");
		
	return new IfpTransObj( txManager, status );   
	}*/
	
}