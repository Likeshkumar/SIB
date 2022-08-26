package test;

public class Bulkcustest {
public static void main(String[] args) {
	
	String pacct="";				String s1acct = "123456789009876543";
	String pcurr = "";			String p1curr = "123";
	String Pcurrcode="";			String S1currcode="";
	
	String s2acct = "123456789009876543";
	String p2curr = "123";
	String S2currcode="123";
	
	String s3acct = "123456789009876543";
	String p3curr = "123";
	String S3currcode="123";
	String s4acct = "123456789009876543";
	String p4curr = "123";
	String S4currcode="123";
	String s5acct = "123456789009876543";
	String p5curr = "123";
	String S5currcode="123";
	
	String finalgotvalue="0000", respmsg="";
	String Primary_details="";int i=1, p=0,s1=0,s2=0,s3=0,s4=0,s5=0;
	System.out.println(pacct.trim().length());
	if(!pacct.equals(" ")){
		System.out.println("correct");
	}else{System.out.println("wrong");}
	//ring respmsg="

	if(i!=0 && finalgotvalue.equals("0000")){
	if(!pacct.isEmpty() && !pacct.equalsIgnoreCase("") && !pacct.equalsIgnoreCase("NULL") && pacct.trim().length()!=0)
		{         System.out.println("Primary account number available---->"+i);
		        if(!pcurr.isEmpty() && !pcurr.equalsIgnoreCase("") && !pcurr.equalsIgnoreCase("NULL") && pcurr.trim().length()!=0)
			{     System.out.println("Prinamry accounttype available--->"+i);
				if(!Pcurrcode.isEmpty() && !Pcurrcode.equalsIgnoreCase("") && !Pcurrcode.equalsIgnoreCase("NULL") && Pcurrcode.trim().length()!=0)
			{     System.out.println("Primary accountcurrency available--->"+i);finalgotvalue="0000";
			}else{System.out.println("Primary accountcurrency not available--->"+i);
			respmsg="Pimary account currencncy not available "+i;
			finalgotvalue="pppp";}
			}else{System.out.println("Prinamry accounttype not available--->"+i);
			respmsg="Pimary account type not available "+i;
			finalgotvalue="pppp";}
		    }else{System.out.println("Primary account number not available---->"+i);
		        if(!pcurr.isEmpty() && !pcurr.equalsIgnoreCase("") && !pcurr.equalsIgnoreCase("NULL") && pcurr.trim().length()!=0)
			     {System.out.println("Primary account type  number available---->"+i);
			     respmsg="Pimary account type available "+i;
			     finalgotvalue="ssss";
			}else{ System.out.println("Primary accounttype is not available--->"+i);
			    if(!Pcurrcode.isEmpty() && !Pcurrcode.equalsIgnoreCase("") && !Pcurrcode.equalsIgnoreCase("NULL") && Pcurrcode.trim().length()!=0)	
				 {System.out.println("Primary accountcurrency available--->"+i);
				 respmsg="Pimary account currencncy available "+i;
				 finalgotvalue="ssss";
			}else{System.out.println("Primary accountcurrency not available--->"+i);finalgotvalue="0000";}}}
	}
	
	finalgotvalue="0000";
	
	if(!pacct.isEmpty() && !pacct.equalsIgnoreCase("") && !pacct.equalsIgnoreCase("NULL") && pacct.trim().length()!=0)
	{         System.out.println("Primary account number available---->"+i);
	        if(!pcurr.isEmpty() && !pcurr.equalsIgnoreCase("") && !pcurr.equalsIgnoreCase("NULL") && pcurr.trim().length()!=0)
		{     System.out.println("Prinamry accounttype available--->"+i);
			if(!Pcurrcode.isEmpty() && !Pcurrcode.equalsIgnoreCase("") && !Pcurrcode.equalsIgnoreCase("NULL") && Pcurrcode.trim().length()!=0)
		{     System.out.println("Primary accountcurrency available--->"+i);finalgotvalue="0000";
		}else{System.out.println("Primary accountcurrency not available--->"+i);
		respmsg="Pimary account currencncy not available "+i;
		finalgotvalue="pppp";}
		}else{System.out.println("Prinamry accounttype not available--->"+i);
		respmsg="Pimary account type not available "+i;
		finalgotvalue="pppp";}
	    }else{ System.out.println("PRIMARY ACCOUNT NUMBER NOT AVAILABLE");
	    finalgotvalue="PPPP";
	    respmsg="Primary account number details must required";
}

	 
	 
	if(i!=0 && finalgotvalue.equals("0000")){
		if(!s1acct.isEmpty() && !s1acct.equalsIgnoreCase("") && !s1acct.equalsIgnoreCase("NULL") && s1acct.trim().length()!=0)
			{         System.out.println("S1 account number available---->"+i);
			        if(!p1curr.isEmpty() && !p1curr.equalsIgnoreCase("") && !p1curr.equalsIgnoreCase("NULL")  && p1curr.trim().length()!=0)
				{     System.out.println("S1 accounttype available--->"+i);
					if(!S1currcode.isEmpty() && !S1currcode.equalsIgnoreCase("") && !S1currcode.equalsIgnoreCase("NULL") && S1currcode.trim().length()!=0)
				{     System.out.println("S1 accountcurrency available--->"+i);
					  finalgotvalue="0000";
				}else{System.out.println("S1 accountcurrency not available--->"+i);
				respmsg="SECONDARY1 account currencncy not available "+i;
				finalgotvalue="pppp";}
				}else{System.out.println("S1 accounttype not available--->"+i);
				respmsg="SECONDARY5 account type not available "+i;
				finalgotvalue="pppp";}
			    }else{System.out.println("S1 account number not available---->"+i);
			        if(!p1curr.isEmpty() && !p1curr.equalsIgnoreCase("") && !p1curr.equalsIgnoreCase("NULL") && p1curr.trim().length()!=0)
				     {System.out.println("S1 account type number available---->"+i);
				     respmsg="SECONDARY1 account type is available but accountnumber is available "+i;
				     finalgotvalue="ssss";
				}else{ System.out.println("S1 accounttype is not available--->"+i);
				    if(!S1currcode.isEmpty() && !S1currcode.equalsIgnoreCase("") && !S1currcode.equalsIgnoreCase("NULL") && S1currcode.trim().length()!=0)	
					 {System.out.println("S1 accountcurrency available--->"+i);
					 respmsg="SECONDARY1 account currencncy is available but account number & accttype is availabe "+i;
					 finalgotvalue="ssss";
				}else{System.out.println("S1 accountcurrency not available--->"+i);finalgotvalue="0000";}}}
		}
	
 
	if(i!=0 && finalgotvalue.equals("0000")){
		if(!s2acct.isEmpty() && !s2acct.equalsIgnoreCase("") && !s2acct.equalsIgnoreCase("NULL") && s2acct.trim().length()!=0)
			{         System.out.println("S2 account number available---->"+i);
			        if(!p2curr.isEmpty() && !p2curr.equalsIgnoreCase("") && !p2curr.equalsIgnoreCase("NULL")  && p2curr.trim().length()!=0)
				{     System.out.println("S2 accounttype available--->"+i);
					if(!S2currcode.isEmpty() && !S2currcode.equalsIgnoreCase("") && !S2currcode.equalsIgnoreCase("NULL") && S2currcode.trim().length()!=0)
				{     System.out.println("S2 accountcurrency available--->"+i);
					  finalgotvalue="0000";
				}else{System.out.println("S2 accountcurrency not available--->"+i);
				respmsg="SECONDARY2 account currencncy not available "+i;
				finalgotvalue="pppp";}
				}else{System.out.println("S2 accounttype not available--->"+i);
				respmsg="SECONDARY2 account type not available "+i;
				finalgotvalue="pppp";}
			    }else{System.out.println("S2 account number not available---->"+i);
			        if(!p2curr.isEmpty() && !p2curr.equalsIgnoreCase("") && !p2curr.equalsIgnoreCase("NULL") && p2curr.trim().length()!=0)
				     {System.out.println("S2 account type number available---->"+i);
				     respmsg="SECONDARY2 account type is available but accountnumber is available "+i;finalgotvalue="ssss";
				}else{ System.out.println("S2 accounttype is not available--->"+i);
				    if(!S2currcode.isEmpty() && !S2currcode.equalsIgnoreCase("") && !S2currcode.equalsIgnoreCase("NULL") && S2currcode.trim().length()!=0)	
					 {System.out.println("S2 accountcurrency available--->"+i);
					 respmsg="SECONDARY2 account currencncy is available but account number & accttype is availabe "+i;
					 finalgotvalue="ssss";
				}else{System.out.println("S2 accountcurrency not available--->"+i);finalgotvalue="0000";}}}
		}
 
	if(i!=0 && finalgotvalue.equals("0000")){
		if(!s3acct.isEmpty() && !s3acct.equalsIgnoreCase("") && !s3acct.equalsIgnoreCase("NULL") && s3acct.trim().length()!=0)
			{         System.out.println("S3 account number available---->"+i);
			        if(!p3curr.isEmpty() && !p3curr.equalsIgnoreCase("") && !p3curr.equalsIgnoreCase("NULL")  && p3curr.trim().length()!=0)
				{     System.out.println("S3 accounttype available--->"+i);
					if(!S3currcode.isEmpty() && !S3currcode.equalsIgnoreCase("") && !S3currcode.equalsIgnoreCase("NULL") && S3currcode.trim().length()!=0)
				{     System.out.println("S3 accountcurrency available--->"+i);
					  finalgotvalue="0000";
				}else{System.out.println("S3 accountcurrency not available--->"+i);
				respmsg="SECONDARY3 account currencncy not available "+i;
				finalgotvalue="pppp";}
				}else{System.out.println("S3 accounttype not available--->"+i);
				respmsg="SECONDARY3 account type not available "+i;
				finalgotvalue="pppp";}
			    }else{System.out.println("S3 account number not available---->"+i);
			        if(!p1curr.isEmpty() && !p3curr.equalsIgnoreCase("") && !p3curr.equalsIgnoreCase("NULL") && p3curr.trim().length()!=0)
				     {System.out.println("S3 account type number available---->"+i);
				     respmsg="SECONDARY3 account type is available but accountnumber is available "+i;
				     finalgotvalue="ssss";
				}else{ System.out.println("S3 accounttype is not available--->"+i);
				    if(!S3currcode.isEmpty() && !S3currcode.equalsIgnoreCase("") && !S3currcode.equalsIgnoreCase("NULL") && S3currcode.trim().length()!=0)	
					 {System.out.println("S3 accountcurrency available--->"+i);
					 respmsg="SECONDARY3 account currencncy is available but account number & accttype is availabe "+i;
					 finalgotvalue="ssss";
				}else{System.out.println("S3 accountcurrency not available--->"+i);finalgotvalue="0000";}}}
		}
 
	if(i!=0 && finalgotvalue.equals("0000")){
		if(!s4acct.isEmpty() && !s4acct.equalsIgnoreCase("") && !s4acct.equalsIgnoreCase("NULL") && s4acct.trim().length()!=0)
			{         System.out.println("S4 account number available---->"+i);
			        if(!p4curr.isEmpty() && !p4curr.equalsIgnoreCase("") && !p4curr.equalsIgnoreCase("NULL")  && p4curr.trim().length()!=0)
				{     System.out.println("S4 accounttype available--->"+i);
					if(!S4currcode.isEmpty() && !S4currcode.equalsIgnoreCase("") && !S4currcode.equalsIgnoreCase("NULL") && S4currcode.trim().length()!=0)
				{     System.out.println("S4 accountcurrency available--->"+i);
					  finalgotvalue="0000";
				}else{System.out.println("S4 accountcurrency not available--->"+i);
				respmsg="SECONDARY4 account currencncy not available "+i;finalgotvalue="pppp";}
				}else{System.out.println("S4 accounttype not available--->"+i);
				respmsg="SECONDARY4 account type not available "+i;
				finalgotvalue="pppp";}
			    }else{System.out.println("S4 account number not available---->"+i);
			        if(!p4curr.isEmpty() && !p4curr.equalsIgnoreCase("") && !p4curr.equalsIgnoreCase("NULL") && p4curr.trim().length()!=0)
				     {System.out.println("S4 account type number available---->"+i);
				     respmsg="SECONDARY4 account type is available but accountnumber is available "+i;
				     finalgotvalue="ssss";
				}else{ System.out.println("S4 accounttype is not available--->"+i);
				    if(!S4currcode.isEmpty() && !S4currcode.equalsIgnoreCase("") && !S4currcode.equalsIgnoreCase("NULL") && S4currcode.trim().length()!=0)	
					 {System.out.println("S4 accountcurrency available--->"+i);
					 respmsg="SECONDARY4 account currencncy is available but account number & accttype is availabe "+i;
					 finalgotvalue="ssss";
				}else{System.out.println("S4 accountcurrency not available--->"+i);finalgotvalue="0000";}}}
		}
 
	
	if(i!=0 && finalgotvalue.equals("0000")){
		if(!s5acct.isEmpty() && !s5acct.equalsIgnoreCase("") && !s5acct.equalsIgnoreCase("NULL") && s5acct.trim().length()!=0)
			{         System.out.println("S5 account number available---->"+i);
			        if(!p5curr.isEmpty() && !p5curr.equalsIgnoreCase("") && !p5curr.equalsIgnoreCase("NULL")  && p5curr.trim().length()!=0)
				{     System.out.println("S5 accounttype available--->"+i);
					if(!S5currcode.isEmpty() && !S5currcode.equalsIgnoreCase("") && !S5currcode.equalsIgnoreCase("NULL") && S5currcode.trim().length()!=0)
				{     System.out.println("S5 accountcurrency available--->"+i);
					  finalgotvalue="0000";
				}else{System.out.println("S5 accountcurrency not available--->"+i);
				respmsg="SECONDARY5 account currencncy not available "+i;
				finalgotvalue="pppp";}
				}else{System.out.println("S5 accounttype not available--->"+i);
				respmsg="SECONDARY5 account type not available "+i;
				finalgotvalue="pppp";}
			    }else{System.out.println("S5 account number not available---->"+i);
			        if(!p5curr.isEmpty() && !p5curr.equalsIgnoreCase("") && !p5curr.equalsIgnoreCase("NULL") && p5curr.trim().length()!=0)
				     {System.out.println("S5 account type number available---->"+i);
						respmsg="SECONDARY5 account type is available but accountnumber is available "+i;
				     finalgotvalue="ssss";
				}else{ System.out.println("S5 accounttype is not available--->"+i);
				    if(!S5currcode.isEmpty() && !S5currcode.equalsIgnoreCase("") && !S5currcode.equalsIgnoreCase("NULL") && S5currcode.trim().length()!=0)	
					 {System.out.println("S5 accountcurrency available--->"+i);
						respmsg="SECONDARY5 account currencncy is available but account number & accttype is availabe "+i;
					 finalgotvalue="ssss";
				}else{System.out.println("S5 accountcurrency not available--->"+i);finalgotvalue="0000";}}}
		}

	 
}
}
