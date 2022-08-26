package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation 
{
	
	public static boolean NumberCharcter(String s)
	{
		return s != null &&(!s.equals("")) && s.matches("^[a-zA-Z0-9]*$");
	}
	
	public static boolean SpcNumberCharcter(String s)
	{
		return s != null  &&   (!s.equals("")) && s.matches("^[a-zA-Z0-9_ ]+$");
	}
	
	
	public static boolean CheckSpcNumberCharcter(String s)
	{
		if(s==""||s==null)
			
		{
			return true;
		}
		else

		return s.matches("^[a-zA-Z0-9_ ]+$");
	}
	
	public static boolean charcter(String str) 
    { 
        return ((str != null) 
                && (!str.equals("")) 
                && (str.matches("^[a-zA-Z]*$"))); 
    } 
	public static boolean email(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+  "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";                            
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) { return false; }
        return pat.matcher(email).matches(); 
    }
	public static boolean Spccharcter(String str) 
    { 
        return ((str != null) 
                && (!str.equals("")) 
                && (str.matches("^[a-zA-Z_ ]*$"))); 
    } 
	
	public static boolean CheckSpccharcter(String str) 
    { 
	if(str==""||str==null)
			
		{
			return true;
		}
		else
        return  (str.matches("^[a-zA-Z_ ]*$")); 
    } 
	
	
	public static boolean Number(String str) 
    { 
        return ((str != null) 
                && (!str.equals("")) 
                && (str.matches("^[0-9]*$"))); 
    } 
	public static boolean CheckNumber(String str) 
    {
		
        return (str.matches("^[0-9]*$")); 
    } 
	
	public static int number(String message) 
	{    
	   // System.out.print("message"+message);	       
	        try 
	        {
	        	if(message=="" || message==null)
	        	{
	        		System.out.println("null and empty");
	        		return 0;
	        	}
	        	 
	            // regular expression for an integer number 
	            String regex = "[+-]?[0-9][0-9]*"; 
	              
	            // compiling regex 
	            Pattern p = Pattern.compile(regex); 
	              
	            // Creates a matcher that will match input1 against regex 
	            Matcher m = p.matcher(message); 
	                 
	            if(m.find() && m.group().equals(message)) 
	            {
	                //System.out.println(message + " is a valid integer number"); 
	                return 1;
	            }
	            
	            else
	            {
	              //  System.out.println(message + " is not a valid integer number"); 
	                return 0;
	            }
	        } 
	        catch (NumberFormatException e) 
	        {
	        	System.out.println("exception");
	        	return 0;
	        }
		
	    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
  
    // Main method 
    public static void main(String[] args) 
    { 
        // Checking for True case 
        System.out.println("Test Case 1:"); 
  
        String str1 = "GeeksforGeeks"; 
        System.out.println("Input: " + str1); 
        System.out.println("Output: " + charcter(str1)); 
  
 
        
        //Checking for spec String 
        System.out.println("\nTest Case 2:"); 
  
        String str2 = "3412"; 
        System.out.println("Input: " + str2); 
        System.out.println("Output: " + number(str2)); 
        
        
        
        //Checking for spec String 
        System.out.println("\nTest Case 3:"); 
  
        String str3 = "FGDF FGDF 45432"; 
        System.out.println("Input: " + str3); 
        System.out.println("Output: " + NumberCharcter(str3)); 
        

        String str4 = ""; 
        System.out.println("Input: " + str4); 
        System.out.println("Output: " + CheckSpcNumberCharcter(str4)); 
        
        System.out.println("\nTest Case 5:"); 
        
        String str5 = ""; 
        System.out.println("Input: " + str5); 
        System.out.println("Output: " + CheckSpccharcter(str5)); 
        
        
        
    } 
} 

