package test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class datechek {

	public static void main(String[] args) {
		int firstdate=0,monthval=0;
	String datevalue="ab/13/1979";
	if(datevalue.matches("[a-zA-Z0-9]+")){
		System.out.println("datesss formate is wrong PLEASE PROVIDE 01/12/2018 format");
	}
	String valuefordob=datevalue.substring(0,2);
	
	String symblvalidation=datevalue.substring(2,3);
	String monthvalidation=datevalue.substring(3,5);
	System.out.println("valuefordob"+valuefordob+"\n symblvalidation"+symblvalidation+"\n monthvalidation"+monthvalidation);
	if(valuefordob.contains("/")||monthvalidation.contains("/")){
		System.out.println("date formate is wrong PLEASE PROVIDE 01/12/2018 format");
	
	}
	else{
		 firstdate=Integer.parseInt(valuefordob);
		 monthval=Integer.parseInt(monthvalidation);
	}
	if(firstdate >31 || !symblvalidation.equalsIgnoreCase("/") ||monthval >12){
		System.out.println("failure");
		System.out.println("date formate is wrong PLEASE PROVIDE 01/12/2018 format");
	}
	
	else{
		System.out.println("success");
	}
	
	}
}
