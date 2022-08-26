package com.ifp.util;

public class CardnumberMask 
{
	public String CardMasking(String encycrd)
	{
		int leng_card = encycrd.length();
		String mask="XXXXXXXXXXXXXX";
		String bin = encycrd.substring(0,6);
		String last_no = encycrd.substring(leng_card-4);
		if(leng_card == 16)
		{
			mask = bin+"XXXXXX"+last_no;
		}
		else if(leng_card == 19)
		{
			mask = bin+"XXXXXXXXX"+last_no;
		}
		return mask;
	}
}
