package test;

public class bulkreg {
	public static void main(String[] args) {
		
	
	String acct="894655";
	System.out.println("s :::  "+acct.length());
	if( acct.isEmpty() ||  "".equals(acct)&&  acct.length() !=9  &&   acct.length() !=10 ){
		System.out.println(" Account number Empty / length::::: ");
	}else {
		System.out.println(" length ::::  "+acct.length());
	}
	}
}
