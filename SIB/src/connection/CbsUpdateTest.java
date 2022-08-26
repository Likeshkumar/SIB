package connection;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CbsUpdateTest {

	public static void main(String[] argv)  {

		System.out.println("-------- Oracle JDBC Connection Testing ------");

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}

		System.out.println("Oracle JDBC Driver Registered!");

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@132.147.160.13:1521:FCUBSPROD", "OBLPROD",
					"oblprod");

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			
			Statement rst;
			try {
				rst = connection.createStatement();
			
			
			String query = "select * from STTM_DEBIT_CARD_MASTER where CUST_AC_NO='24621702010101' and REQUEST_REFERENCE_NO='050615240217'";
			
			ResultSet rrs = rst.executeQuery(query);
			if(!rrs.next()){
				String status ="Fail " ;
				SQLException mySqlEx = new SQLException(status, "Exception");
				status = status + mySqlEx.getMessage();
				System.out.println("status"+mySqlEx.getErrorCode());
				System.out.println("status"+mySqlEx.getLocalizedMessage());
				//System.out.println("status"+mySqlEx.());
			}else{
			while(rrs.next())    
			{
				System.out.println("Card No : "+rrs.getString("CARD_NO"));
			}}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("Failed to make connection!");
		}
		
		
	}

}
