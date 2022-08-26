package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.ifp.Action.BaseAction;

public class SwitchConnection12072019 extends BaseAction {
	
	public Connection getConnection(){
		Connection connection = null;
		try {
			Properties prop = getCommonDescProperty();
			
			String dburl = prop.getProperty("swhcon.dburl");    
			String dbusername = prop.getProperty("swhcon.dbusername");    
			String dbpassword = prop.getProperty("swhcon.dbpassword");    
			Class.forName("oracle.jdbc.driver.OracleDriver");
			trace("Connecting to switch db.... ");
			connection = DriverManager.getConnection(dburl, dbusername,dbpassword);
			trace("switch db connected.... ");
			connection.setAutoCommit(false);
		} catch (Exception e) {
			System.out.println("JDBC Driver?");        
			e.printStackTrace();  
		}
		return connection;
	}
}
