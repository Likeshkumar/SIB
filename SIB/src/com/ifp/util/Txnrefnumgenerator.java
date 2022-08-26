package com.ifp.util;

import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;

public class Txnrefnumgenerator 
{

	public String getTxnrefnum(String Instname)
	{
		
		
		int start =1;
		int end = 45;
		char Alpha_chars[] = {'A','B','0','C','D','1','E','F','2','G','H','3','I','J','4','K','L','5','M','N','6','O','P','7','Q','R','8','S','T','U','9','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
		int rannum=0;
		char[] copyTo = new char[20];
		String inst_name = Instname;
		String chrd = "";
		String txnrefnum ="";
		String datetime ="";
		String gen_instname ="";
		int totlalchars = 15;
	        Random random = new Random();
		for(int i =0; i<=totlalchars; i++)
		{
			rannum = randomgenerator(start,end,random);
			copyTo[i] = Alpha_chars[rannum];	
		}
		for(int j=0;j<=totlalchars; j++)
		{
			chrd = chrd + copyTo[j];
		}
		datetime = getDateandtime();
		gen_instname = instnameparser(inst_name);
		txnrefnum  = gen_instname+chrd+datetime;
		
		return txnrefnum;
	}
	
	public int randomgenerator(int s,int e, Random ran)
	{
		long range = (long)e - (long)s + 1;
		long fraction = (long)(range * ran.nextDouble());
		int randomNumber =  (int)(fraction + s);
		return randomNumber;
	}
	public String instnameparser(String iname)
	{
		String in = iname;
		String GenName = null;
		int nl = in.length();
		if(nl != 0 && iname != null)
		{
			if(nl == 4)
			{
				GenName = iname;

			}
			else if(nl > 4)
			{   
				 GenName= in.substring(0, 4);
			}	
			else if(nl < 4)
			{
				if(nl == 1)
				{
					GenName = iname+"XXX";
				}
				else if(nl == 2)
	            {
	            	GenName = iname+"XX";
	            }
				else if(nl == 3)
	            {
	            	GenName = iname+"X";
	            }
			}
		}
		else
		{
			GenName = "BANK";
		}
	 	return GenName; 
	}
	public String getDateandtime()
	{
		String dttm ="";
		Date date = new Date();
		String DATE_FORMAT = "yyMMddHHmmss";	
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		dttm = sdf.format(date);
		return dttm;
	}

}
