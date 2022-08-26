package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.ifp.Action.BaseAction;
import com.ifp.util.EncryptAction;
public class CBSConnection extends BaseAction{
	//Util Util= new Util();
	EncryptAction Util = new EncryptAction();
	public Connection getCBSDBConnection()
	{
		Connection connection = null; 
		try { 
			Properties prop = getCommonDescProperty();
			
			String dburl = prop.getProperty("cbscon.dburl");    
			String dbusername = prop.getProperty("cbscon.dbusername");    
			String dbpassword = prop.getProperty("cbscon.dbpassword");  
			System.out.println("dbpassword "+dbpassword);
			dbpassword=	Util.decrypt1(dbpassword);
			System.out.println("original val "+dbpassword);
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			trace("Connecting to cbs db.... ");
			connection = DriverManager.getConnection(dburl, dbusername,dbpassword);
			trace("cbs db connected.... ");
			connection.setAutoCommit(false);
		} catch(SQLException se){
			System.out.println("JDBC Driver?");        
			se.printStackTrace(); 
		}
		return connection;
	}
	
	public Connection OtherdatabaseConnection(){
		Connection connection = null; 
		try { 
			Properties prop = getCommonDescProperty();
			
			String dburl = prop.getProperty("odbcon.dburl");    
			String dbusername = prop.getProperty("odbcon.dbusername");    
			String dbpassword = prop.getProperty("odbcon.dbpassword");    
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			trace("Connecting to cbs db.... ");
			connection = DriverManager.getConnection(dburl, dbusername,dbpassword);
			trace("cbs db connected.... ");
			connection.setAutoCommit(false);
		} catch(SQLException se){
			System.out.println("JDBC Driver?");        
			se.printStackTrace(); 
		}
		return connection;
	}


}
