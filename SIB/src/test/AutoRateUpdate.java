package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import connection.CBSConnection;
import connection.Dbcon;


public class AutoRateUpdate {
	
	static{
		final long timeInterval = 30000;
		  Runnable runnable = new Runnable() {
		  public void run() {
		    while (true) {
		      // ------- code for task to run
		      System.out.println("Every 30 seconds static block !!");
		      CBSConnection cbscon = new CBSConnection();
		      Connection conn = null;
		      ResultSet rs=null; 
		      conn = cbscon.getCBSDBConnection();	
		      ArrayList list = new ArrayList();
		      //list.add("EUR");
		      list.add("THB");
		      list.add("USD");
		      
		      DateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");
		      Date date = new Date();
		      String currdate = dateFormat.format(date).toString();
		      
		      try{
			      for(int i=0;i<list.size();i++){
			    	  
			    	  String cbssellrate = "", cbsbuyrate = "", buyrate = "", sellrate = "";
			    	  
			    	  String val = String.valueOf(list.get(i));
			    	  System.out.println("val-->"+val);
			    	  String cbsqry = "select ODBUY,SELLRT from cf05fcrt where FCCD=? and EFFDT > ? order by effdt desc";
			    	  PreparedStatement ps = conn.prepareStatement(cbsqry);
			    	  ps.setString(1, val);
			    	  ps.setString(2, "26-MAR-18");
			    	  rs= ps.executeQuery();
			    	  while (rs.next()){
			    		  cbssellrate = rs.getString("SELLRT");
			    		  cbsbuyrate = rs.getString("ODBUY");
			    	  }
			    	  System.out.println(cbssellrate+"---"+cbsbuyrate);
			    	  Connection conn1 = null;
			    	  Dbcon dbcon = new Dbcon(); 
			    	  conn1 = dbcon.getDBConnection();
			    	  
			    	  String qry = "select buyingrate,sellingrate from ezcurrency where currcode=(select numeric_code from global_currency where currency_code=?)";
			    	  PreparedStatement ps1 = conn1.prepareStatement(qry);
			    	  ps1.setString(1, val);
			    	  rs= ps1.executeQuery();
			    	  
			    	  while (rs.next()){
			    		  sellrate = rs.getString("sellingrate");
			    		  buyrate = rs.getString("buyingrate");
			    	  }
			    	  System.out.println(sellrate+"---"+buyrate);
			    	  
			    	  if(buyrate.equals(cbsbuyrate)){
			    		  System.out.println("buyrate.equals(cbsbuyrate)-->"+buyrate.equals(cbsbuyrate));
			    	  }else{
			    		  String updateqry = "update ezcurrency set buyingrate=?,sellingrate=? where currcode=(select numeric_code from global_currency where currency_code=?)";
			    		  PreparedStatement ps2 = conn1.prepareStatement(updateqry);
				    	  ps2.setString(1, cbsbuyrate);
				    	  ps2.setString(2, cbssellrate);
				    	  ps2.setString(3, val);
				    	  int u = ps2.executeUpdate();
				    	  System.out.println("update result--->"+u);
				    	  if(u == 1){
				    		  conn1.commit();
				    	  }else{
				    		  conn1.rollback();
				    	  }
			    	  }
			    	  
			      }
		      }catch (Exception e) {
				e.printStackTrace();
			}
		      
		      // ------- ends here
		      try {
		       Thread.sleep(timeInterval);
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      }
		      }
		    }
		  };
		  Thread thread = new Thread(runnable);
		  thread.start();
	}
	
}