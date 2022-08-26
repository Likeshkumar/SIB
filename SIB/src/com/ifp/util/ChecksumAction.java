package com.ifp.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
//import com.ifp.Predecenc.AES_CryptoUtils;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
public class ChecksumAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	CommonDesc commondesc = new CommonDesc();
	
	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId() {
		HttpSession session = getRequest().getSession();
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}
	
	private List uploadFileFileName;
	
	public List getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(List uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	private List uploadFile;
	public List getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(List uploadFile) {
		this.uploadFile = uploadFile;
	}

	
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	//AES_CryptoUtils crypt = new AES_CryptoUtils();
	
	public Properties getProp(String filename) {
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {

			e.printStackTrace();
		}
		return prop;
	}
	
	public String getprefilesbatchwise(String searchservice) throws IOException {
		System.out.println("inside");
		Properties prop = getProp("RNFlink.properties");
		String filedir = prop.getProperty("RNF_Filezip");
System.out.println("checkinggg"+filedir);
System.out.println("checkindsddggg"+searchservice);
		BufferedReader buff = new BufferedReader(new FileReader(filedir));
		String line = "";
		try {
			for (; (line = buff.readLine()) != null;) {
				if (line.contains(searchservice)) 
				{
					System.out.println("chhhh");
					break;
				}
			}
		} 
		catch (IOException e) 
		{
			trace("Exception : " + e.getMessage());
		}
		finally
		{
			buff.close();
		}

		return line.replace(searchservice + "= ", "");
	}
	
	public String preDecryptionhome() 
	{
		trace("***********PRE Decryptionhome*********");
		return "preDecryptionhome";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String Checksum() 
	{
		trace("***********Check Sum Upload Home*********");
		return "FileuploadHome";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String Predycryptionprocess() throws Exception 
	{
		trace("*******Rnfdycryptionprocess begins*******");
		IfpTransObj transact = commondesc.myTranObject("RNFBASE24File", txManager);
		String instid = comInstId();
		String userid = comUserId();
		JSONObject jsonmaster = new JSONObject();
		String act = getRequest().getParameter("act");
		trace("instid --->" + instid + " action type --->" + act);
		String todaydate = commondesc.getDate("ddMMyyyy");
		String encryptfor = "PRE_DECRYPTION";
		Iterator filenames = uploadFileFileName.iterator();
		getUploadFileFileName();
		Iterator upfiles = uploadFile.iterator();
		BufferedReader br = null;
		byte[] D_Crypt_outputBytes = null;
		try 
		{
			
			/*int lineno = 0;
			int x, y;
			String cardno = "", valcardno = "", valcardnowithstatus = "", txncode = "", resptimestamp = "",wpsrefnumber = "", wpsrefnumber1 = "", txntype = "", DOB = "", insertresponse = "",insertsuccessresponse = "", empname = "", empname1 = "";
			String iccid = "", valiccid = "";
			String accesskey = "", valaccesskey = "", responsedesc = "";
			StringBuffer hcardno = new StringBuffer();
			String eDMK = "", eDPK = "";			
			String accountnum = "", insertresponse1 = "", batchid = "", empmolid = "", wpsresp = "", CARD_NO = "",cardnum = "", EMPLOYEE_ID = "", fname = "", lname = "", COMPANYNAME = "", COMPANYID = "",CARDAGENTNO = "";
*/
			List secList = null;
			String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			trace("keyid::" + keyid);
			secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			
			/*String[] fields = null;
			String authmsg = "", updatequery = "";
			int updatecount = 0, update_status = 0, successcount = 0, failcount = 0, failinsertcount = 0;*/
			
			String filename = "";
			while (upfiles.hasNext()) 
			{
				while (filenames.hasNext()) 
				{
					String filedata = (String) upfiles.next();
					filename = (String) filenames.next();
					
					/*if (filename.contains(".File")) 
					{
						trace(".File option");
						encryptfor = "PRE_DECRYPTION";
					} 
					 else 
					 {
						 	trace("other then .File option");
							encryptfor = "RNF_DECRYPTION";
					}*/
					
					trace("serialno" + filename + "encryptfor" + encryptfor);
				
					// ****VENDOR FILE DECRYPTION *************//
					String key = "2370491CC49492D5AEF716AD2C206429";
					File ActualFile = new File(filedata);
					trace("Input File Name:" + ActualFile);
					FileInputStream D_Crypt_inputStream = new FileInputStream(ActualFile);
					trace("Input File size:" + ActualFile.length());
					byte[] D_Crypt_inputBytes = new byte[(int) ActualFile.length()];
					trace("Test--1"+D_Crypt_inputBytes);
					D_Crypt_inputStream.read(D_Crypt_inputBytes);
					trace("Test--2"+D_Crypt_inputStream);
					//D_Crypt_outputBytes = crypt.decrypt(key, D_Crypt_inputBytes);
					System.out.println("checking status-->"+D_Crypt_outputBytes);
					System.out.println("Temp File Name/Path:" + this.getprefilesbatchwise(encryptfor) + filename + "ORIGINAL");
					FileOutputStream D_Crypt_outputStream = new FileOutputStream(this.getprefilesbatchwise(encryptfor) + filename + "ORIGINAL");
					D_Crypt_outputStream.write(D_Crypt_outputBytes);
					System.out.println("checking status 2-->"+D_Crypt_outputStream);
					// ****VENDOR FILE DECRYPTION END************//					
					D_Crypt_outputStream.close();
				}
			}
			trace("successfully");
			addActionMessage("  Decrpted Successfully..under." + this.getprefilesbatchwise(encryptfor) + filename + "ORIGINAL");
			return "preDecryptionhome";
		} 
		catch (Exception e) 
		{
			addActionError("File format is not supported..");
			return "preDecryptionhome";
		}
	}
	
	
	
	
	
	
	
	
	
	
	public String Checksumprocess()
	{
		String checksum=null;
		System.out.println("*******Rnfdycryptionprocess begins*******");
		JSONObject jsonmaster = new JSONObject();
		Iterator filenames = uploadFileFileName.iterator();
		System.out.println("filenames------->"+filenames);
		getUploadFileFileName();
		Iterator upfiles = uploadFile.iterator();
	    System.out.println("upfiles------->"+upfiles);
			        String filename = "";
					String filedata = (String) upfiles.next();
					System.out.println("filedata------->"+filedata);
					filename = (String) filenames.next();
					System.out.println("filename------->"+filename);
					File ActualFile = new File(filedata);
					System.out.println("ActualFile------->"+ActualFile);
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			checksum=checksum(ActualFile,md);
			 if(!checksum.equals(" "))
			 {
				 trace("inside trace");
				 addActionMessage("The Uploaded File Check Sum Value is --->>  "+checksum);
				 return "required_home";
			 }
		}
		catch(Exception e)
		{
			e.getMessage();
			trace("exception occured in encryption " + e.getMessage());
		}
		return null;
	}
	
	
	private static String checksum(File actualFile, MessageDigest md) throws IOException
    {
        try (InputStream fis = new FileInputStream(actualFile))
        {
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = fis.read(buffer)) != -1)
            {
                md.update(buffer, 0, nread);
            }
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) 
        {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
	
	
	
	
	
	
	
	
	
	
	
}
