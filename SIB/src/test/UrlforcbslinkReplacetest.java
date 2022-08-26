package test;

public class UrlforcbslinkReplacetest {
	
	public static void main(String args[]){
		
		
	//String dsadsa="ABDUL MANAN   .|0774936402|1343735|AFN|01-JAN-87|KABUL,DIS;5TH,LOC;KOTE SANGI NEAR,KABUL,AFG,," +
		//	"KABUL,AF ";
	
			String dsadsa	="TAWOOS KHAN|0764782760|1257231|AFN|01-JAN-92|DIS:4 ASYAB,LOC:QALA-E- WAZIR,NEAR TO MAKTAB SOKHTA,P:KABUL,AFGHANISTAN,,KABUL,AF";

 //cutname  ***************start*********************//	
			 String  Footer ="|";
			 int cnamepipeendlength=dsadsa.indexOf(Footer);
			 String custname=dsadsa.substring(0 , cnamepipeendlength);
			  System.out.println("custname length     :  "+custname.length()); // Prints somestring
		String custname1="",custname2="";
		
		            custname1=custname.replace(",", "");
			 if(custname.length()  > 26 ){
				  custname1=custname.substring(0 , 26);
				  //System.out.println("custname fdsfsd     :  "+custname1)  ;
			 }
			 if(custname.length() <26){
				 custname1= custname;
			 }
			  
			// System.out.println("dddd"+name); 
			  System.out.println("custname     :  "+custname1); // Prints somestring
			  custname2=custname1.replace("'", "");
			  System.out.println("existed qote with '    :  "+custname2);
			  // String datas= custname1.split(",");
			  
			  
			  String aftercustremoved = dsadsa.substring(custname.length()+1);
			  System.out.println("after custname removed"+aftercustremoved );
				
 //  cutname  ***************start*********END***********//
	  
	  
	  
//phone number  ***************start*********************//
	  int phonelen=aftercustremoved.indexOf(Footer);
		 String phone=aftercustremoved.substring(0 , phonelen);
		  System.out.println("phone length     :  "+phone.length()); // Prints somestring
		
		  System.out.println("phone     :  "+phone); // Prints somestring
  
		  String s3 = aftercustremoved.substring(phone.length()+1);
		  System.out.println("after custname and phoneno removed"+s3 );
		  
// phone  ***************start*******end**************//		  
		  
		  int y2=s3.indexOf(Footer);
			 String txnname1111=s3.substring(0 , y2);
			  System.out.println("cin len     :  "+txnname1111.length()); // Prints somestring
			  System.out.println("cin     :  "+txnname1111); // Prints somestring

			  
			  String s4 = s3.substring(txnname1111.length()+1);
			  System.out.println("after custphCin RM"+s4 );
			  
//currency  ***************start***********end**********//			  
			  
			  int y3=s4.indexOf(Footer);
				 String txnname2=s4.substring(0 , y3);
				  System.out.println("currency len     :  "+txnname2.length()); // Prints somestring
				
				  System.out.println("currency     :  "+txnname2); // Prints somestring

				  String s5 = s4.substring(txnname2.length()+1);
				  System.out.println("aftter curr RM"+s5 );
			
// currency  ***************start*********************//		  
				  
				  int y4=s5.indexOf(Footer);
					 String txnname3=s5.substring(0 , y4);
					  System.out.println("DOB len     :  "+txnname3.length()); // Prints somestring
					  System.out.println("DOB     :  "+txnname3); // Prints somestring

					  
					  String s6 = s5.substring(txnname3.length()+1);
					  System.out.println("dob rem"+s6 );
//Currency  ***************end*********************//	
					  
					  
//Address	  ***************start*********************//
					  String   addressdetails=s6.replace("'", "");
					  String[] datas= addressdetails.split(",");
					  System.out.println("adda1"+datas[0] );
					  System.out.println("add 2"+datas[1] );
					  System.out.println("add 3"+datas[2] );
					  System.out.println("add 4"+datas[3] );
					  System.out.println("add 5"+datas[4] );
					  System.out.println("add 6"+datas[5] );
					  System.out.println("city"+datas[6] );
					  System.out.println("country code"+datas[7] );
				  
	 }
}


//datas ABDUL MANAN .
//datas21 KABUL
 