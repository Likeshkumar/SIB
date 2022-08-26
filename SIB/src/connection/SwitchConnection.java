package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.ifp.Action.BaseAction;
import com.ifp.util.EncryptAction;
public class SwitchConnection extends BaseAction {
	
	//Util util=new Util();
	EncryptAction util = new EncryptAction();
	
	public Connection getConnection(){
		Connection connection = null;
		try {
			Properties prop = getCommonDescProperty();
			
			String dburl = prop.getProperty("swhcon.dburl");    
			String dbusername = prop.getProperty("swhcon.dbusername");    
			String dbpassword = prop.getProperty("swhcon.dbpassword");    
			//System.out.println("eedbpassword"+dbpassword);
			//dbpassword=util.decrypt1(dbpassword);
			//System.out.println("dddbpassword"+dbpassword);
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
