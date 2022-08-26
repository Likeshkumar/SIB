package test;

public class panvalidation {

	public static void main(String[] args) {
		
		 //String gg="Gowtham "\S\"";
		//System.out.println(gg);
		int PanOffset=0;
		String chn="6055540002000000357";
		int PanLength=12;
		String strpan = chn.substring(PanOffset);
		//String strpan = chn.substring(len-13, len-1);
		//trace("strpan"+strpan);
		String strcharN = chn.substring(chn.length() - 6, chn.length() - 1);
		//String strcharN = "0000";
		//trace("strcharN"+strcharN);
		int panData = strpan.length();
		String strpinValidationData= "";int ncharLoc;
		if(strpan.lastIndexOf(strcharN) < 1){
			
			
			strpinValidationData = chn.substring(0,10) + 'N' + chn.substring(chn.length() - 1);
		}
		if(strpan.length() == 12){
			//trace("121212");
			if(strpan.substring(2,7) == strpan.substring(7, 12)){
				
				strpan = strpan.substring(0, 7);
				//trace("test11");
				
				if(strpan.lastIndexOf(strcharN) > 1 && PanLength == panData )
				{
					if(strpan.substring(strpan.length()-6) == chn.substring(chn.length() - 6))
					{
						//trace("test11");
						strpinValidationData = strpan.substring(0, 7)+'N'+chn.substring(chn.length()-1);
						
					}
					else
					{
						
						strpinValidationData = strpan.substring(0, 7)+'N';
						
					}
				}
			}
				else{
					ncharLoc = strpan.lastIndexOf(strcharN);
					//trace("321312321");
					if(ncharLoc > 1 && PanLength == panData)
					{//trace("dsgdsgdf");
						if(strpan.substring(strpan.length()-6) == chn.substring(chn.length()-6))
						{
							strpinValidationData = strpan.substring(0, ncharLoc)+'N'+chn.substring(chn.length()-1);
						}else
						{//trace("4324324324");
							strpinValidationData  = strpan.substring(0, ncharLoc)+'N';
						}
					}
				}
			
		//	trace("423432");
			
		}else
		{    
			
			ncharLoc = strpan.lastIndexOf(strcharN);
			//trace("sarrr"+ncharLoc);
					if(ncharLoc > 1 && PanLength == panData ){
						
						if(strpan.substring(strpan.length()-6).equals( chn.substring(chn.length()-6)))
						{
							//trace("sssss");
							strpinValidationData = strpan.substring(0, ncharLoc)+'N'+chn.substring(chn.length()-1);
							//trace("111111111"+strpinValidationData);
						}else
						{
							//trace("sardar test");
							strpinValidationData  = strpan.substring(3, 10)+'N';
							//trace("222222"+strpinValidationData);
						}
					}
					strpinValidationData  = strpan.substring(3, 10)+'N';
					//trace("new pan data-->"+strpinValidationData);
		}
		
		
	}
	
}
