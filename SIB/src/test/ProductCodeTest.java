package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ProductCodeTest {
	public static void main(String[] args) {
		
		      String productCode="5608430103";
		      
		      System.out.println("card type   "+productCode.substring(8,10));
		      
		      System.out.println(" BIn number   "+productCode.substring(0,8));
		Connection con=getDBConnection();
		
		System.out.println("connection "+con);
		
	}
	
	public  static Connection getDBConnection(){
		Connection connection = null; 
		try { 
		//	Properties prop = getCommonDescProperty();
			
			String username="UTBL";
			String password="UTBL";
			 //String url = "jdbc:oracle:thin:@172.16.10.14:1521:orcl12";
			 String url = " jdbc:oracle:thin:@localhost:orcl";
			
			
			//String dburl = prop.getProperty("con.dburl");    
			//System.out.println("cnvnvnvh------------- dburl  ===> "+dburl);
			//String dbusername = prop.getProperty("con.dbusername");    
			//System.out.println("cnvnvnvh------------- dbusername  ===> "+dbusername);
			//String dbpassword = prop.getProperty("con.dbpassword");    
			//System.out.println("cnvnvnvh------------- dbpassword  ===> "+dbpassword);
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, username,password);
			
			System.out.println("connection is  -------> "+connection);
			//connection.setAutoCommit(false);
		} catch (ClassNotFoundException e) {      
			System.out.println("JDBC Driver?");        
			e.printStackTrace();      
		}catch(SQLException se){
			
		}
		return connection;
	}
	

}
