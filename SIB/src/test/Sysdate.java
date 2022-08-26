package test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import test.Date;
public class Sysdate { 


	static Date date =  new Date();
	
	public static void main(String args[]) throws Exception
	
		
	{
		String dob="04-jun-2020";
		
		
		
	System.out.println(filterUnwantedSpaces(dob));
	}
	
	
	
	
	
	
	public static String filterUnwantedSpaces(String values){
		String 	res = values.replaceAll("[^a-zA-Z0-9-]", "");
		return res;
	}
}