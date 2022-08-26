package test;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import com.ifg.Config.padss.PadssSecurity;

public class Admincardchn {
	static JdbcTemplate jdbctemplate = new JdbcTemplate();
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	public String generateCHN(String genchn,String sequnce,int baselen,int chnlen) throws Exception
	{
		PadssSecurity sec = new PadssSecurity();
		String cardno = "N";
		//System.out.println( " genchn " + genchn + "---  sequnce, " +sequnce + " , " + baselen+ " baselen " + "chnlen " + chnlen);
		String newsequnce = orderreferenceno(sequnce,baselen);
		if(newsequnce.equals("X"))               
		{
			System.out.println(" Card Sequnce Number Reached Maximum");
			return "M";
		}
	//	System.out.println("New Sequnce Number is ====>"+newsequnce+"Chn Len got is "+chnlen);
		String newcardno = genchn+newsequnce;
		//System.out.println("New Card Number is ====>"+newcardno);
		int nechnlen = newcardno.length();
		//System.out.println("Length Of The CHN is ===>"+nechnlen);
		if(nechnlen == 15 || nechnlen == 18)
		{
			String check_digit = generatedCheckDigit(newcardno);
			System.out.println("Check Digit is ===>"+check_digit);
			cardno = newcardno+check_digit;
			//System.out.println("Card Number Generated is ===>"+sec.getMakedCardno(cardno));
		}else{
			cardno = "N";
			System.out.println("Card Lenght is Not matched ...Generated card number lenghth without check digit: "+nechnlen);
		}
		/*int cardlen = cardno.length();
		System.out.println("configured chn len=============================>"+chnlen);
		System.out.println("generated cardlen=============================>"+cardlen);
		
		if(cardlen-(genchn.length()) != chnlen)
		{
			cardno = "N";
			System.out.println("Card Lenght is Not matched ");
		}
		*/
		System.out.println (" ......... generateCHN method ended");
		return  cardno;
		
	}
	
	public String orderreferenceno(String refnum,int ref_len)
	{
		String ref_num="X";
		System.out.println("############### Refnum Recived is "+refnum+"     Refnum Len "+ref_len);
		int curr_len = refnum.length();
		if(curr_len==ref_len)
		{
			ref_num = refnum;
		}
		else if(curr_len<ref_len)
		{
			int refnum_len = refnum.length();
			System.out.println(" The Len of Exsist Ref num is "+refnum_len);
			int newlength;
			while(refnum_len != ref_len)
			{
				System.out.println("Inside While Loop "+ref_len);
				for(int j=0; j<ref_len;j++)
				{
					refnum = "0"+refnum; 
					newlength = refnum.length();
					if(ref_len == newlength)
					{
						break;
					}
				}
				System.out.println(" Ref Num Generated is "+refnum);
				
				refnum_len = refnum.length();
				System.out.println("refnum_len===InSide While Loop > "+refnum_len);
			}
			ref_num=refnum;
		}
		return ref_num;
	}
	
	public String generatedCheckDigit(String cardno)
	{
		int  checkdigit = generateCheckDigit(cardno);
		String chkdigit = Integer.toString(checkdigit);
		return chkdigit;
	}
	
	public int generateCheckDigit( String s)
	{  
		int digit = 10 - getCheckDigit(s, true) % 10;
		if(digit == 10)
		{
			digit =0;
		}
		return digit; 
	}
	private int getCheckDigit(String s, boolean eventposition )
	{
		  int sum = 0;
		  for (int i = s.length() - 1; i >= 0; i--) {
			  int n = Integer.parseInt(s.substring(i, i+1));
			  if ( eventposition ){
				  n *=2;
				  if (n>9){
					  n = (n % 10) + 1;
				  }
			  }
			  sum += n;
			  eventposition = !eventposition;
		  }
		return sum;
	}
	
	public static void main(String[] args) throws Exception {
		/*Admincardchn ad = new Admincardchn();
		String cardno = "";
		String sequence = "0000";
		int seq =32 ;
		PrintWriter writer = new PrintWriter("F://Ramesh//AdminCards.txt", "UTF-8");
		for(int i=1;i<81;i++){
			if(seq >= 100){
				sequence = "000";
			}
			cardno = ad.generateCHN("100011003",sequence+seq,6,16);
			seq = seq + 1;
			System.out.println("cardno-->"+cardno);
			writer.println(i+"."+cardno);
			
		}
		writer.close();*/
		/*String prefile = "Visa Classic_001_04042016_163447";
		String[] namesplit = prefile.split("_");
		String filedate = namesplit[2];
		String filetime = namesplit[3];
		filedate = filedate.substring(0,4) + filedate.substring(6,8);
		prefile = filedate +"-"+filetime;
		System.out.println("test--->"+filedate);
		System.out.println("test--->"+prefile);*/
		String branch = "";
		String acctno = "14023941020504";
		branch = acctno.substring(6,8);
   	 	//branch = paddingZero(branch, 3);
		System.out.println(branch);
		
		/*List<Map<String,Object>> renewalflag = null;
		String renewalflagqry = "SELECT RENEWALFLAG  FROM CARD_PRODUCTION WHERE INST_ID='ORBL' AND ROWNUM<=1 AND CARD_NO='C1ABE8E1CB894ADE7359B2163E4EDC58' ";
		//enctrace("renewalflagByProd:::"+renewalflagqry);
		try{
			renewalflag  = jdbctemplate.queryForList(renewalflagqry);
			
			if(!renewalflag.isEmpty()){
				renewalflag = (List<Map<String, Object>>) renewalflag.get(0).get("RENEWALFLAG");
			}
			
		}catch(EmptyResultDataAccessException e){}*/
		//return renewalflag;
	}
	public static String paddingZero(String orignalvalue, int strlen){
		String val = orignalvalue.toString();
		
		String formattedresult = org.apache.commons.lang.StringUtils.leftPad(val, strlen, '0');
		return formattedresult;
		
	}
}
