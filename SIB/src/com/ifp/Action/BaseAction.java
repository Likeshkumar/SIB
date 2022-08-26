package com.ifp.Action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.sql.DataSource;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Base action class for all action.
 * 
 * @author CGS
 *
 */

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, SessionAware {

	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;

	private HttpServletResponse response;

	public HttpSession session;

	private Map sessionMap;

	private JavaMailSender mailSender;

	public String sessionExpired = "sessionExpired";

	public HttpSession getSession() {
		//Httpsession ses=request.getSession();

		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setSession(Map sessionMap) {
		this.sessionMap = sessionMap;
	}

	/*
	 public String getLoginStatus(){
         //session.setAttribute("", arg1);
         return SUCCESS;
    }*/
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String cbsUploadFileTempLocaion() {
		ResourceBundle rb = getCommonDescProperty1();
		String upl_loc = rb.getString("tempcbsuploadfile.loc");
		return upl_loc;
	}

	public String cbsUploadFileLocaion() {
		ResourceBundle rb = getCommonDescProperty1();
		String upl_loc = rb.getString("cbsuploadfile.loc");
		return upl_loc;
	}

	public String getLogFileLoc() {
		ResourceBundle rb = getCommonDescProperty1();
		String upl_loc = rb.getString("log.file.location");
		System.out.println("cbs file upload loc : " + upl_loc);
		return upl_loc;
	}

	private ResourceBundle getCommonDescProperty1() {
		ResourceBundle rb = ResourceBundle.getBundle("CommonDesc");
		return rb;
	}
	
	public void smslog(String message)
	{ 
		 FileWriter fstream  = null;
		BufferedWriter prewriter  = null;     
		Properties prop = getCommonDescProperty();
		
		
		 try {
			 String logprefix = "SMSLOG_SIB_"; 
			String strfiledir = prop.getProperty("log.sms.file.location"); 
			//System.out.println( "strfiledir : " + strfiledir );
			String logfilename = logprefix+getDatetime()+".log";      
			File nwfile = new File( strfiledir+"/"+logfilename); 
			//System.out.println( "nwfile : " + nwfile );
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
	

	public Properties getCommonDescProperty() {
		String filename = "CommonDesc.properties";
		Properties prop = new Properties();
		InputStream ins = null;
		try {
			ins = this.getClass().getClassLoader().getResourceAsStream(filename);
			prop.load(ins);
			if (ins != null) {
				ins.close();
			}
		} catch (Exception e) {
			System.out.println("Exception getting property : " + e.getMessage());
			e.printStackTrace();
		}
		return prop;
	}

	public Properties getLicenceProperty(String filename) {
		Properties prop = new Properties();
		InputStream ins = null;
		try {
			ins = this.getClass().getClassLoader().getResourceAsStream(filename);
			prop.load(ins);
			if (ins != null) {
				ins.close();
			}
		} catch (Exception e) {
			System.out.println("Exception getting property : " + e.getMessage());
			e.printStackTrace();
		}
		return prop;
	}

	public String getDatetime() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}

	public String getDateTimeStamp() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_m_ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}

	public String getYesterday() {
		Date ydate = getDayBefore(new Date());
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		String yesdate = dateFormat.format(ydate);
		return yesdate;
	}

	public static Date getDayBefore(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30);
		return cal.getTime();
	}

	public void enctrace(String message) {
		FileWriter fstream = null;
		BufferedWriter prewriter = null;
		Properties prop = getCommonDescProperty();

		try {
			String logprefix = "SQL_CARDMAN_SIB_";
			String strfiledir = prop.getProperty("log.sql.location");
			String logfilename = logprefix + getDatetime() + ".log";
			File nwfile = new File(strfiledir + "/" + logfilename);
			// System.out.println( "nwfile : " + nwfile );
			File logfiledir = new File(strfiledir);
			if (logfiledir.exists()) {
				if (!nwfile.exists()) {
					nwfile.createNewFile();
				}
			} else {
				System.out.println("COULD NOT UNDERSTAND THE LOG FILE PATH SPECIEFIED [ " + strfiledir + " ] ");
			}
			String yesterdaydate = getYesterday();
			String existfilename = strfiledir + logprefix + yesterdaydate + ".log";
			File f = new File(existfilename);
			if (f.exists()) {
				// f.delete();
				// System.out.println("file_name "+existfilename + " DELETED
				// SUCCESSFULLY");
			}
			fstream = new FileWriter(nwfile, true);
			prewriter = new BufferedWriter(fstream);
			prewriter.write(getDateTimeStamp() + ":" + message + "\n"); // Base64.encode(
																		// getDateTimeStamp()
																		// + ":"
																		// +
																		// message+"\n");
			prewriter.close();
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		} finally {
			try {
				fstream.close();
				prewriter.close();
			} catch (Exception e) {
			}
		}
	}

	public void trace(String message) {
		FileWriter fstream = null;
		BufferedWriter prewriter = null;
		Properties prop = getCommonDescProperty();

		try {
			String logprefix = "CARDMAN_SIB_";
			String strfiledir = prop.getProperty("log.file.location");
			// System.out.println( "strfiledir : " + strfiledir );
			String logfilename = logprefix + getDatetime() + ".log";
			File nwfile = new File(strfiledir + "/" + logfilename);
			// System.out.println( "nwfile : " + nwfile );
			File logfiledir = new File(strfiledir);
			if (logfiledir.exists()) {
				if (!nwfile.exists()) {
					nwfile.createNewFile();
				}
			} else {
				System.out.println("COULD NOT UNDERSTAND THE LOG FILE PATH SPECIEFIED [ " + strfiledir + " ] ");
			}
			String yesterdaydate = getYesterday();
			String existfilename = strfiledir + logprefix + yesterdaydate + ".log";
			File f = new File(existfilename);
			if (f.exists()) {
				// f.delete();
				// System.out.println("file_name "+existfilename + " DELETED
				// SUCCESSFULLY");
			}
			fstream = new FileWriter(nwfile, true);
			prewriter = new BufferedWriter(fstream);
			prewriter.write(getDateTimeStamp() + ":" + message + "\n");
			prewriter.close();

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				fstream.close();
				prewriter.close();
			} catch (Exception e) {
			}
		}
	}

	String sid = "";

	public void addSessionintrace(DataSource dataSource, JdbcTemplate jdbcTemplate, HttpSession session) {
		try {
			String query_getuserdatils = "SELECT SID FROM V$SESSION WHERE AUDSID = SYS_CONTEXT('USERENV','SESSIONID')";
			String sid = (String) jdbcTemplate.queryForObject(query_getuserdatils, String.class);
			String ipAddress = getRequest().getHeader("X-FORWARDED-FOR");
			trace(" The IP ADDERESS IS ======> " + ipAddress);
			
		
			
			if (ipAddress == null) {
				trace(" IP ADDRESS is NULL ");
				ipAddress = getRequest().getRemoteAddr();
				trace(" ipAddress = request.getRemoteAddr() ===> " + ipAddress);
			}
			trace("$$$$$$$$$$$$$$$$$  User ID : " + session.getAttribute("USERID") + "      Session Id  : " + sid
					+ "   App-accessing-IP : " + ipAddress + "  $$$$$$$$$$$$$$$");
			enctrace("$$$$$$$$$$$$$$$$$  User ID : " + session.getAttribute("USERID") + "      Session Id  : " + sid
					+ "   App-accessing-IP : " + ipAddress + "     Access Time   " + getDateTimeStamp()
					+ "  $$$$$$$$$$$$$$$");
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}

	public String getApplictionType(JdbcTemplate jdbctemplate) throws Exception {
		// trace("selectApplictionType method called ...");
		String appname = "NOREC";
		List apptypelist = null;
		
		/*String apptypeqry = "select APP_ID from GLOBAL_APP_TYPE WHERE APP_ENABLED='1'";
		apptypelist = jdbctemplate.queryForList(apptypeqry);*/
		
		//by gowtham
		String apptypeqry = "select APP_ID from GLOBAL_APP_TYPE WHERE APP_ENABLED=? ";
		apptypelist = jdbctemplate.queryForList(apptypeqry,new Object[]{"1"});
		
		if (!apptypelist.isEmpty()) {
			Iterator itr = apptypelist.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				if (apptypelist.size() == 1) {
					appname = (String) mp.get("APP_ID");
					System.out.println("appname --- " + appname);
				} else if (apptypelist.size() > 1) {
					appname = "BOTH";
				} else {
					appname = "NOREC";
				}
			}
		}

		// trace("selectApplictionType method end ...");
		return appname;
	}

	public String getProfilelistTemp() {
		String applicationtype = "DEBIT";
		String tablename = "";
		if (applicationtype.equals("PREPAID")) {
			tablename = "IFPROFILE_LIST_TEMP";
		} else if (applicationtype.equals("DEBIT")) {
			tablename = "PROFILE_LIST";
		}
		return tablename;
	}

	public String getProfilelistMain() {

		String applicationtype = "DEBIT";
		String tablename = "";
		if (applicationtype.equals("PREPAID")) {
			tablename = "IFPROFILE_LIST";
		} else if (applicationtype.equals("DEBIT")) {
			tablename = "PROFILE_LIST";
		}
		return tablename;
	}

	public String getADMIN_PROFILE_PRIVILEGE() {
		String applicationtype = "DEBIT";
		String tablename = "";
		if (applicationtype.equals("PREPAID")) {
			tablename = "IFP_ADMIN_PROFILE_PRIVILEGE";
		} else if (applicationtype.equals("DEBIT")) {
			tablename = "ADMIN_PROFILE_PRIVILEGE";
		}
		return tablename;
	}

	public String getPROFILE_PRIVILEGE_TEMP() {
		String applicationtype = "DEBIT";
		String tablename = "";
		if (applicationtype.equals("PREPAID")) {
			tablename = "IFP_PROFILE_PRIVILEGE_TEMP";
		} else if (applicationtype.equals("DEBIT")) {
			tablename = "PROFILE_PRIVILEGE";
		}
		return tablename;
	}

	public String getPROFILE_PRIVILEGE() {
		String applicationtype = "DEBIT";
		String tablename = "";
		if (applicationtype.equals("PREPAID")) {
			tablename = "IFP_PROFILE_PRIVILEGE";
		} else if (applicationtype.equals("DEBIT")) {
			tablename = "PROFILE_PRIVILEGE";
		}
		return tablename;
	}

	public String getMENU() {
		String applicationtype = "DEBIT";
		String tablename = "";
		if (applicationtype.equals("PREPAID")) {
			tablename = "IFMENU";
		} else if (applicationtype.equals("DEBIT")) {
			tablename = "MENU";
		}
		return tablename;
	}

	public String getMethodName() {
		return "Thread.currentThread().getStackTrace()[1].getMethodName()";
	}

	/**
	 * Date 17-06-19 This method is used to write Encryption DPK value into
	 * CommonDesc.properties file
	 * SRNP0002
	 * @param edpk
	 * @throws Exception 
	 *//*
	public void writeDPK(String edpk) throws Exception {
		//String filename = "src/CommonDesc.properties";
		Properties props = new Properties();
		FileOutputStream out = new FileOutputStream("E:/WORK_SPACE/PADSS_FINAL_NEW/src/CommonDesc.properties",true);
		props.put("EDPK", edpk);
		props.store(out, "new EDPK key added");
		out.close();
		//System.out.println("p file ----> " + props);
	}*/
	
	
	
	
	
	/**
	 * Date 30-07-19 This method is used to write Encryption DPK value into
	 * CommonDesc.properties file
	 * 
	 * @param edpk
	 * @throws Exception 
	 */
	public void writeDPK(String edpk) throws Exception {
		//String filename = "src/CommonDesc.properties";
		/*Properties props = new Properties();
		FileOutputStream out = new FileOutputStream("E:/WORK_SPACE/PADSS/src/CommonDesc.properties",true);
		props.put("EDPK", edpk);
		props.store(out, "new EDPK key added");
		out.close();
		System.out.println("p file ----> " + props);*/
		
		//  /home/cgspl/apache-tomcat-7.0.82/webapps/PADSS/WEB-INF/classes
		
		///home/cardman/apache-tomcat-7.0.99/webapps/UTBL_BANK_PADSS/WEB-INF/classes/CommonDesc.properties
		
	//	PropertiesConfiguration config=new PropertiesConfiguration("/home/cardman/apache-tomcat-7.0.99/webapps/UTBL_BANK/WEB-INF/classes/CommonDesc.properties");
		
		
		//PropertiesConfiguration config=new PropertiesConfiguration("/home/cgspl/apache-tomcat-7.0.82/webapps/PADSS/src/CommonDesc.properties");
	/*	
     	String file="CommonDesc.properties";
     	Properties properties = new Properties();
     	properties.load(getClass().getResourceAsStream("CommonDesc.properties"));
		File f=new File(file);
		
		String path=f.getAbsolutePath();
		
		System.out.println("=========== path is==========   "+path);
		PropertiesConfiguration config=new PropertiesConfiguration(path); */
		
	     PropertiesConfiguration config=new PropertiesConfiguration("F:/GIT_Local_path/UTBSL_BANK/UTBL_BANK_PADSS/src/CommonDesc.properties");
		config.setProperty("EDPK", edpk);
		config.save();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
