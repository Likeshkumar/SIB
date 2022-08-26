package test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.util.SystemOutLogger;
import org.apache.tiles.jsp.taglib.ContainerTagSupport;
import org.springframework.expression.spel.generated.SpringExpressionsParser.boolLiteral_return;

import com.sun.accessibility.internal.resources.accessibility;

import ch.qos.logback.classic.net.SyslogAppender;
import sun.org.mozilla.javascript.internal.regexp.SubString;
import sun.security.util.Length;

public class Testsubstr {

	public static void main(String args[]) {
		
		String chn="6055540002000001264";
		//6055-54xx-xxxxxxx-1264
		//6055540002000001264
		System.out.println(chn.substring(0,4)+"-"+chn.substring(4,6)+"xx-xxxxxxx-"+chn.substring(15,19));
		
		
		
	}}
	/*	String DOB="";
		System.out.println(DOB+"DOB-->"+DOB.length());
		DOB=DOB.trim();
		System.out.println("DOB-->"+DOB.length());
		String respmsg="";
		String finalgotvalue="";
		
		boolean ans=false;
		DOB="04-Jul-2021";
		System.out.println(DOB+"--DOB-->"+DOB.length());
		DOB=DOB.trim();
		System.out.println(DOB+"--DOB---->"+DOB.length());
		
 		try {
			
 			
 			try{
			 String datevalidation=DOB.substring(0,2);
			 String monthvalidation=DOB.substring(3,6);
			 String yearvalidation=DOB.substring(7,11);
			 String symblvalidation = DOB.substring(2, 3);
			 String symblvalidation1 = DOB.substring(6, 7);
			 
			 System.out.println("<<--datevalidation-->"+datevalidation+"<<--monthvalidation-->>"+monthvalidation+"<<--yearvalidation-->"+yearvalidation);
		     System.out.println("<<--Symbolvalidation-->> "+symblvalidation+" <<--Symbolvalidation1-->> "+symblvalidation1);
			 
			 int presentyear = Calendar.getInstance().get(Calendar.YEAR);
			 System.out.println("year--->"+presentyear);
			 int firstdate = Integer.parseInt(datevalidation);
			 int yearval = Integer.parseInt(yearvalidation);
			
				List<String> arr = new ArrayList<String>(12);
				 arr.add("JAN"); 					 	arr.add("FEB"); 				arr.add("MAR"); 			arr.add("APR"); 
				 arr.add("MAY"); 					arr.add("JUN"); 				arr.add("JUL"); 			arr.add("AUG"); 
				 arr.add("SEP"); 						arr.add("OCT"); 				arr.add("NOV"); 			arr.add("DEC"); 
				 ans=arr.contains(monthvalidation.toUpperCase());
			   
			if(firstdate<31 &&yearval<presentyear){
				System.out.println("date and year validation passed");
				if(symblvalidation.equalsIgnoreCase("-")&&symblvalidation1.equalsIgnoreCase("-")){
					System.out.println("symbol validation passed");
					
					if(ans){
						System.out.println("month validation passed");
					}else {System.out.println("month validation failed");}
					}else{System.out.println("symbol validation failed");}
			}else{
				System.out.println("date and year validation failed");
			}
			
			
			
			
			
			
 			}catch(Exception e)
 			{
 				System.out.println("FALSE......");
 				
 			}
 			 
			
			
			 
		
		
		
		
		String DOBB=" 04 -JUL-1994";
		String mobile=" ";
		int i=1;
		//String DOBB=" 02-FEB-1996";
		
		System.out.println("DOB-->"+ DOBB.length());
		DOBB=DOBB.trim();
		System.out.println("DOB-->"+ DOBB.length());
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			df.parse(DOBB);
			System.out.println("true");

		} catch (Exception e) {
			System.out.println("false");
		}
		
		 if( i!=0 && mobile.length()<=13 && mobile.equalsIgnoreCase("") && mobile==null && mobile.equals("NULL"))
		 {
			 
			 System.out.println("Inside");
		 } 
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		String dd = null;
		String ddd = null;
		
		
	
		
		System.out.println("length of mobile number"+mobile.length());
		
	 
		

		
		 
		if(i!=0 && mobile.length()<=13 && mobile.equals("NULL")){
			System.out.println("mobile number 1st condition passed..........");
			
		}
		
		else if(i!=0 && mobile.equalsIgnoreCase("") || mobile.isEmpty() ){
			System.out.println("mobile number 2nd condition passed..........");
			
		}
		else {
				
				System.out.println("inside else..........");}
		
		if((mobile.length()<=13 && mobile.equals("NULL")) ||  mobile.equalsIgnoreCase("")){
			System.out.println("mobile number condition passed..........");
		}
		
		if(mobile.length()<=13 || mobile.isEmpty() || mobile.equals("NULL") ){
			System.out.println("mobile number condition passed");
		}
		else{System.out.println("mobile number condition fails");}
		
		
		if(mobile.length()<=13 && mobile.equalsIgnoreCase("") && mobile==null && mobile.equals("NULL") && mobile.isEmpty())
		{System.out.println("inside loop");}
		 
		else{
			System.out.println("inside else condition"); 
		}

		// String dt = "01-JAN-2013";
		  DOB = "11-FEB-2012";

		int ie = 1;
		  finalgotvalue = "";
		  respmsg = "";

		if (i != 0 && DOB.isEmpty()) {
			finalgotvalue = "10";
			respmsg = "DOB IS EMPTY";
		}

		else if (i != 0 && !DOB.isEmpty()) {
			DateFormat dff = new SimpleDateFormat("DD-MMM-YYYY");
			try {
				df.parse(DOB);
				// trace("DOB format is correct"+DOB);
				System.out.println("DOB format is correct" + DOB);
			} catch (Exception e) {
				finalgotvalue = "10";
				respmsg = "DOB FROMAT IS WRONG..PROVIDE DD-MON-YYYY FORMAT ONLY ";
				System.out.println("DOB format is WRONG" + DOB);
				// trace("DOB format is WRONG"+DOB);
			}
		} else if (i != 0) {
			System.out.println("  true");
		} else {

			System.out.println("else condition");
		}

		// String DOB = "11-02-93";
		DateFormat dff = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			df.parse(DOB);
			System.out.println("true");

		} catch (Exception e) {
			System.out.println("false");
		}

		String DDOB = "12-APR-2020";

		int doblength = DOB.length();
		String valuefordob = DOB.substring(0, 2);
		int firstdate = 0, monthval = 0;
		String symblvalidation = DOB.substring(2, 3);
		String monthvalidation = DOB.substring(3, 5);
		// System.out.println("valuefordob"+valuefordob+"\n
		// symblvalidation"+symblvalidation+"\n
		// monthvalidation"+monthvalidation);
		if (valuefordob.contains("/") || monthvalidation.contains("/") || doblength > 10 || doblength < 10) {

		} else {
			firstdate = Integer.parseInt(valuefordob);
			monthval = Integer.parseInt(monthvalidation);
		}
		if (firstdate > 31 || !symblvalidation.equalsIgnoreCase("/") || monthval > 12) {

		} else {

		}

		String acctnumber = "+123456789321";

		System.out.println(acctnumber.length());
		int ii = 1;

		if (ii != 0 && acctnumber.length() <= 13 && acctnumber.equalsIgnoreCase("") && acctnumber == null
				&& acctnumber.equals("NULL")) {
			// trace("testing---"+acctnumber);
			System.out.println("mobile num" + acctnumber.length());
			System.out.println("mobile-->" + acctnumber);
			// trace("testttttt-->"+acctnumber);
			// finalgotvalue = "18";
			// respmsg = "Mobile NUmber is Empty/Length is not 15 Digit";
			// trace("testing---"+acctnumber);

		}

		if (acctnumber.length() <= 13) {
			System.out.println("6474")
		}
 
 		
		
		 * if (i != 0 && mobile.equalsIgnoreCase("") && mobile.length() != 15 &&
		 * mobile.equals("NULL") && mobile.isEmpty() ) {
		 * trace("testing---"+mobile); System.out.println("mobile-->"+mobile);
		 * trace("testttttt-->"+mobile); finalgotvalue = "18"; respmsg =
		 * "Mobile NUmber is Empty/Length is not 15 Digit";
		 * trace("testing---"+mobile);
		 * 
		 * }
		 

	

	private static int Length(String mobilenumber1) {
		// TODO Auto-generated method stub
		return 0;
	}
*/

