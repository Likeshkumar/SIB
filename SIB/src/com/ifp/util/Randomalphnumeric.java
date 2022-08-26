package com.ifp.util;

import java.util.Random;

public class Randomalphnumeric 
{
	public String Random_number(int len)
	{
        char Alpha_chars[] = {'A','X','Y','W','V','I','J','L','U','H','O','M','N','1','2','3','4','5','6','7','8','9','0'};
        char[] copyTo = new char[100];
        String rad="";
        Random random = new Random();
        int rannum;
        for(int i =0; i<len; i++)
        {
                rannum = randomgenerator(1,22,random);
                copyTo[i] = Alpha_chars[rannum];
                rad = rad + copyTo[i];
        }
        return rad;
	}
    public int randomgenerator(int s,int e, Random ran)
    {
            long range = (long)e - (long)s + 1;
            long fraction = (long)(range * ran.nextDouble());
            int randomNumber =  (int)(fraction + s);
            return randomNumber;
    }

}
