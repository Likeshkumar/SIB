package com.ifp.util;


public class ResponseCheck {
public  String respCheck(String resp)
	{
		String responsecode="no_data";
		String respmessage="no message found for response code";
	    switch (resp) {
	    
	    
	    	case "00":  
	    				responsecode = "00";
	    				respmessage = "00";
	    				//System.out.print(respmessage);
	    				return respmessage;
	    				
	    				

	    	case "13":  
	    				responsecode = "13";
	    				respmessage=" LMK error; report to supervisor ";
	    				//System.out.print(respmessage);
	    				return respmessage;
	    				
	        case "15":  
	        			responsecode = "15";
						respmessage="  Error in input data";
						//System.out.print(respmessage);
						return respmessage;
						
	        case "24":  
	        			responsecode = "24";
						respmessage=" PIN length = or > encrypted PIN length ";
						//System.out.print(respmessage);
						return respmessage;
	    }
  return respmessage;
 }
}
