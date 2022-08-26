package test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SIBL_Testing {
	public static void main(String args[]) {
		String DOB="";
		String respmsg="";
		String finalgotvalue="0000";
		String name="qwertyuiopasdfghjxcvbnmq";
		 int i=1;
		
		boolean ans=false;
		
		DOB="04-Jun- 1996";
		//System.out.println("Before trim"+DOB+"--Date Of Birth-->"+DOB.length());
		DOB=DOB.trim();
		//System.out.println("After trim"+DOB+"--Date Of Birth-->"+DOB.length());
		
 	  	
 			try{
 				
 				System.out.println("<--Name-->"+name+"<--Name Length-->"+name.trim().length());
 				if(i!=0&&name.trim().length()<26&&name.trim().length()>1){
 					System.out.println("Name Condition Passed");
 				}else{
 					System.out.println("Name Condition Failed");
 				}
 				
 				
 				//Mobile number validation
 				String  mobile1="1234567890";
 				System.out.println("<--mobile number-->"+mobile1+"<--mobile1-->"+mobile1.trim().length()+"<--finalgotvalue-->"+finalgotvalue);
 				if(mobile1.trim().length()<14 && mobile1.trim().length()>10){
 					System.out.println("inside if caondition  failed");
 					finalgotvalue="18";
 		 			}else
 					{
 						System.out.println("inside else  condition passed");
 						finalgotvalue="0000";
 					}
 				System.out.println("<--finalgotvalue-->"+finalgotvalue);
 				
 				//CustomerID validation
 				String  CUSTOMERID="";
 	 			System.out.println("<--customer id-->"+CUSTOMERID+"<--customer id length-->"+CUSTOMERID.trim().length()+"<--finalgotvalue-->"+finalgotvalue);
 	 			if(i!=0&&CUSTOMERID.trim().length()!=6&&finalgotvalue=="0000"){
 	 				System.out.println("Customer id validation failed");
 	 			}else{
 	 				System.out.println("Customer id validation passed");
 	 			}
 				
 			 	
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
 			System.out.println("NAME VALIDATION");
 			String NAME="QWERTYUIOPASDFGHJKLZXCVBN";
 			System.out.println("name--->"+NAME.length());
 			 if(i!=0&&NAME.length()<26){
 				 System.out.println("inside if CONDITION PASSED");
 			 }else{
 				 System.out.println("inside else FAILED");
 			 }
 			 
 			 System.out.println("mobile number validation");
 			 
 			 String mobile="123456789012 ";
 	 		System.out.println("mobile--->"+mobile.length());
 		//	if(i!=0&&mobile.isEmpty()&&(mobile.length()<14)&&mobile.length()>10){ 
 	 		if(i!=0&&mobile.trim().length()<14&&mobile.trim().length()>10){
 				System.out.println("INSIDE IF CONDITION PASSED");
 			}else{
 				System.out.println("INSIDE IF CONDITION FAILED");
 			}
 			finalgotvalue="0000";
 			System.out.println("customer id validation");
 			String  CUSTOMERID="      ";
 			System.out.println("customer id-->"+CUSTOMERID.trim().length()+finalgotvalue);
 			if(i!=0&&CUSTOMERID.trim().length()!=6&&finalgotvalue=="000"){
 				System.out.println("Customer id validation failed");
 			}else{
 				System.out.println("Customer id validation passed");
 			}
 			
 			mobile="123456";
 			if(i!=0&&!mobile.isEmpty()){
				mobile=mobile.trim();
				System.out.println("mobile number-->"+mobile.length());
				if(mobile.length()<14){
					System.out.println("mobile numner length less than 14 digit condition passed");
					if(mobile.length()>10){
						finalgotvalue = "18";
						respmsg = "Mobile Number is Empty/Length is not 12 OR 13 Digit";
						System.out.println("mobile numner length less than 10 digit condition failed");
					}else{
						System.out.println("mobile numner length greater than 10  digit condition passed");
					}
				}else{
					System.out.println("mobile numner length less than 14 digit condition passed");
					finalgotvalue = "18";
					respmsg = "Mobile Number is Empty/Length is not 12 OR 13 Digit";
				}
			}
 			
 			 
}}
