package connection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.ifp.Action.BaseAction;
import com.opensymphony.xwork2.ActionSupport;
 

public class Dbcon extends BaseAction {
	
	public static String countrycode = "+255";
	
	public Connection getDBConnection(){
		Connection connection = null; 
		try { 
			Properties prop = getCommonDescProperty();
			
			//String username="UTBL";
			//String password="UTBL";
			 //String url = "jdbc:oracle:thin:@172.16.10.14:1521:orcl12";
			// String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			//String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			
			
			String dburl = prop.getProperty("con.dburl");    
			System.out.println("cnvnvnvh------------- dburl  ===> "+dburl);
			String dbusername = prop.getProperty("con.dbusername");    
			System.out.println("cnvnvnvh------------- dbusername  ===> "+dbusername);
			String dbpassword = prop.getProperty("con.dbpassword");    
			System.out.println("cnvnvnvh------------- dbpassword  ===> "+dbpassword);
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(dburl, dbusername,dbpassword);
			
			System.out.println("connection is  -------> "+connection);
			//connection.setAutoCommit(false);
		} catch (ClassNotFoundException e) {      
			System.out.println("JDBC Driver?");        
			e.printStackTrace();      
		}catch(SQLException se){
			
		}
		return connection;
	}
	
	public static DriverManagerDataSource getConnection(){
		  
		  DriverManagerDataSource datasource = new DriverManagerDataSource();
		  datasource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		  datasource.setUrl("jdbc:oracle:thin:@172.16.10.13.:1521:orcl");
		  datasource.setUsername("bkb_cardman");
		  datasource.setPassword("bkb_cardman");
		  return datasource;
	}
	
	public String getDate()
	{
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	
	public void log(String message){
		//System.out.println( "log msg :" +message);
		try {
			Properties prop = this.getProp("AccountingService.properties");
			String strfiledir = prop.getProperty("service.logdir");
			//String logfilename = prop.getProperty("service.logfilename");
			String logfilename = "DDTBCCMS_"+getDate()+".log";// getText("service.logfilename");
			
			File nwfile = new File( strfiledir+"/"+logfilename); 
			File logfiledir = new File( strfiledir );
			if( logfiledir.exists() ){ 
				if(!nwfile.exists()) {
					nwfile.createNewFile();
				}
			}else{
				System.out.println( "COULD NOT UNDERSTAND THE LOG FILE PATH SPECIEFIED [ "+strfiledir+" ] ");
			}
			
			 
			FileWriter fstream = new FileWriter(nwfile,true);
			BufferedWriter prewriter = new BufferedWriter(fstream);
			prewriter.write( getDateTimeStamp() + ":" + message + "\n"); 
			prewriter.close();
		} catch (IOException e) { 
			e.printStackTrace();
		} 

	}

	public String getDateTimeStamp()
	{
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh_m_ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	
	public Properties getProp( String filename ){
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {
			 
			e.printStackTrace();
		} 
		return prop;
	}

	
	public int getMAXID(String TABLENAME , String COLUMN) throws SQLException
	{
		Dbcon serviceMethod = new Dbcon();
		Connection con = new Dbcon().getDBConnection();
		StringBuilder getMaxString = new StringBuilder();
		getMaxString.append("SELECT NVL(MAX("+COLUMN+"),0) ID  FROM "+TABLENAME);
		serviceMethod.log("getMaxString---"+getMaxString.toString());
	    PreparedStatement ps1  = con.prepareStatement(getMaxString.toString()); 
	    ResultSet rs1 = ps1.executeQuery(getMaxString.toString());  
	    int MAXID = 1;
	    while( rs1.next() ){
	    		  MAXID = (int)rs1.getInt("ID");
	    }
		return MAXID+1;
	}
	public String test()
	{
		return "test successs";
	}
	
	
	public static void main(String[] args) {
		Dbcon d = new Dbcon();
		System.out.println(d.getDBConnection());   
	}

} // end class



