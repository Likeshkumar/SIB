package com.ifp.iso;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
 
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ifp.Action.BaseAction;
 

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.binary.Base64;
 

public class CommonUtil extends  BaseAction{
	 
	
	private final long serialVersionUID = 1L;
	String NOREC = "NOREC";
	  
	
	public CommonUtil() {}

	
	
	 
	
	public String generateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }
	
	public String hashPasswordbySHA512(String plainpassword ) throws NoSuchAlgorithmException{  
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(plainpassword.getBytes()); 
        byte byteData[] = md.digest();  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        } 
        return sb.toString();
	}
	
	public String encriptPasswordBase64( String plainpassword ){
	   byte[] encoded = Base64.encodeBase64(plainpassword.getBytes());  
	   String encodedpass = new String(encoded);
	   return encodedpass;
 
	}
	public String decriptPasswordBase64( String encoded ){
		   byte[] decoded = Base64.decodeBase64(encoded.getBytes());       
		   String plaintext =  new String(decoded);
		   return plaintext;
	}
	 
	public Properties getPropertyFile() {
		Properties prop=null;
		try {
			InputStreamReader fMainProp = new InputStreamReader(this.getClass().getResourceAsStream("ISOconfig.properties")); 
			prop = new Properties(); 
			prop.load(fMainProp) ;
		} catch (IOException e) { 
			e.printStackTrace();
		}
		return prop;
	}
	

	public String getHostIpAddress( String instid, String hostid, JdbcTemplate jdbctemplate) {
		String ipaddress =null; 
		try {
			String hostipqry = "SELECT IP_ADDRESS FROM IFP_HOST_MASTER WHERE INST_ID='"+instid+"' AND HOST_ID='"+hostid+"' ";
			ipaddress = (String) jdbctemplate.queryForObject(hostipqry, String.class); 
		} catch (DataAccessException e) { 
			e.printStackTrace();
		}
		return ipaddress;
	}
	
	public String getHostIpAddress( String instid, String hostid, Connection con) throws Exception {
		String ipaddress =null; 
		ResultSet rs = null;
		PreparedStatement prs = null;
		try {
			String hostipqry = "SELECT IP_ADDRESS FROM IFP_HOST_MASTER WHERE INST_ID='"+instid+"' AND HOST_ID='"+hostid+"' ";
			enctrace("hostipqry :"+hostipqry );
			prs = con.prepareStatement(hostipqry);
			rs = prs.executeQuery();
			if( !rs.next() ){
				return ipaddress;
			}else{
				do{
					ipaddress = rs.getString("IP_ADDRESS");
				}while(rs.next());
			}
		} catch (DataAccessException e) { 
			e.printStackTrace();
		}finally{
			if( rs != null ){ rs.close() ; }
			if( prs != null ){ prs.close() ; }
		}
		return ipaddress;
	}
	
	public int getHostPort( String hostid, JdbcTemplate jdbctemplate ) {
		int hostport = -1; 
		try { 
			String hostipqry = "SELECT PORT FROM  (    SELECT PORT FROM IFP_HOST_CONNECTION WHERE CONN_ID='"+hostid+"' AND  PORT_STATUS='0' ORDER BY dbms_random.value  ) WHERE rownum <= 1";
			System.out.println( "hostipqry : " + hostipqry);
			hostport =  jdbctemplate.queryForInt(hostipqry);
		} catch (DataAccessException e) { 
			e.printStackTrace();
		}
		return hostport;
	}

	
	public int getHostPort(  String hostip, Connection con) throws Exception {
		int hostport = -1; 
		ResultSet rs = null;
		PreparedStatement prs = null;
		try {
			String hostipqry = "SELECT PORT FROM  (    SELECT PORT FROM IFP_HOST_CONNECTION WHERE IP_ADDR='"+hostip+"' AND  PORT_STATUS='0' ORDER BY dbms_random.value  ) WHERE rownum <= 1";
			enctrace("hostipqry :"+hostipqry );
			prs = con.prepareStatement(hostipqry);
			rs = prs.executeQuery();
			if( !rs.next() ){
				return hostport;
			}else{
				do{
					hostport = rs.getInt("PORT");
				}while(rs.next());
			}
		} catch (DataAccessException e) { 
			e.printStackTrace();
		}finally{
			if( rs != null ){ rs.close() ; }
			if( prs != null ){ prs.close() ; }
		}
		return hostport;
	}
	 
	
	
	String tranno = "123";
	public String getTransactionNumber() {
		return tranno;
	}

	public String getIpAddress() {
		String ipaddress = "140.0.0.106";
		return ipaddress;
	}

	public String getLocation() {
		String location = "Chennai";
		return location;
	}
	
	
	public String getSubProdGlCodeFromTxnRule( String instid, String txnrule, JdbcTemplate jdbctemplate ) {
		
		String subprodglcode = NOREC; 
		try {
			String subprodglcodeqry = "SELECT SUB.GL_SCHEME_CODE FROM IFP_ACCT_RULE RULE, IFP_INSTPROD_DETAILS SUB WHERE SUB.INST_ID=RULE.INST_ID AND SUB.SUB_PROD_ID=RULE.SUBPRODUCT AND  RULE.INST_ID='"+instid+"' AND RULE.ACCOUNTRULECODE='"+txnrule+"'";
			enctrace("query for getting sub product gl code "+ subprodglcodeqry);
			subprodglcode = (String) jdbctemplate.queryForObject(subprodglcodeqry, String.class);
		} catch (DataAccessException e) {
			trace("Exception: could not excute sub product query ");
			e.printStackTrace();
		}
		return subprodglcode;
	}

	public String selectApplictionType(JdbcTemplate jdbctemplate) throws Exception { 
		String appname = "NOREC";
		List apptypelist = null; 
		String apptypeqry = "select APP_ID from GLOBAL_APP_TYPE WHERE APP_ENABLED='1'"; 
		apptypelist= jdbctemplate.queryForList(apptypeqry);
		if( !apptypelist.isEmpty() ){
			Iterator itr = apptypelist.iterator();
			while( itr.hasNext() ){
				Map mp = (Map) itr.next();
				if( apptypelist.size() == 1 ){
					appname = (String) mp.get("APP_ID"); 
				}else if(apptypelist.size() > 1){
					appname = "BOTH";
				}else{
					appname = "NOREC";
				}
			}
		} 
		return appname;
	}
	
	public String cardCurrentStatus(String instid, String cardno, JdbcTemplate jdbctemplate){
		String ins_cardexist = "SELECT COUNT(*) FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		int ins_exist = jdbctemplate.queryForInt(ins_cardexist);
		if( ins_exist == 0 ){
			String pers_cardexist = "SELECT COUNT(*) FROM IFP_PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
			int pers_exist = jdbctemplate.queryForInt(pers_cardexist);
			if( pers_exist == 0 ){
				return "INVALID CARD NO";
			}else{
				return "VALID";
			}
		}else{
			return "VALID";
		}
	}
	
	public String getMccDesc( String mcccode, JdbcTemplate jdbctemplate ){
		String MCCQRY ="SELECT MCC_DESC FROM EZMMS_MCC_INFO WHERE MCC_CODE='"+mcccode+"' AND ROWNUM<=1 ";
		enctrace("MCCQRY__" + MCCQRY );
		String mccdesc;
		try {
			mccdesc = (String)jdbctemplate.queryForObject(MCCQRY,String.class);
		} catch (EmptyResultDataAccessException e) { 
			
			mccdesc = "NOREC";
			e.printStackTrace();
		}
		return  mccdesc ; 
	}
	
	public String  getUrlChildMenuId( HttpServletRequest request, JdbcTemplate jdbctemplate ){
		 
		String url =  request.getServletPath();
		String finalurl  = null;
		String querystring =  request.getQueryString();  
		if( querystring != null ){
			finalurl =url+"?"+querystring;
		}else{
			finalurl =url;
		}
		String fullurl = finalurl.replace("/",""); 
		System.out.println( "fullurl__" + fullurl);
		String menuid = this.getMenuId(fullurl, jdbctemplate);
		System.out.println( "menuid__" + menuid);
		
		 return menuid;
	}
	
	public String  getUrlMenuId( HttpServletRequest request, JdbcTemplate jdbctemplate ){
		 
		String url =  request.getServletPath();
		String finalurl  = null;
		String querystring =  request.getQueryString();  
		if( querystring != null ){
			finalurl =url+"?"+querystring;
		}else{
			finalurl =url;
		}
		String fullurl = finalurl.replace("/",""); 
		System.out.println( "fullurl__" + fullurl);
		String menuid = this.getMenuId(fullurl, jdbctemplate);
		System.out.println( "menuid__" + menuid);
		
		if ( !menuid.equals(NOREC)  ){
			return this.getParentMenuId(menuid, jdbctemplate);
		}else{
			return NOREC;
		}
	}
	
	public String getMenuId( String urlnameaction, JdbcTemplate jdbctemplate ){
		String MENUIDQRY ="SELECT MENUID FROM MENU WHERE ACTION='"+urlnameaction+"' AND ROWNUM<=1 ";
		System.out.println("MENUIDQRY __" + MENUIDQRY  );
		String menuid = NOREC;
		try {
			menuid = (String) jdbctemplate.queryForObject(MENUIDQRY, String.class);
		} catch (Exception e) {  
			menuid = NOREC; 
		} 
		return  menuid ; 
	}
	
	 public String GetUrlReportIcon(HttpServletRequest request){
			try {
				String iconurl="http://";
				String url =  request.getContextPath();
				String server =request.getServerName();
				iconurl+= server +":" +request.getServerPort()+url;
				iconurl+="/images/logo_280284_web.JPG";		
				return iconurl;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("error :"+e.getMessage());	
				return e.getMessage();
			}
		}
	
	public String GetUrlReportIcon(HttpServletRequest request,String logopath){
		try {			
			String iconurl="http://";
			String url =  request.getContextPath();
			String server =request.getServerName();
			iconurl+= server +":" +request.getServerPort()+url;
			iconurl+=logopath;		
			return iconurl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error :"+e.getMessage());
			e.printStackTrace();
			return e.getMessage();
			
		}
	}

	public String getParentMenuId( String childmenuid, JdbcTemplate jdbctemplate ){
		String MENUIDQRY ="SELECT PARENTID FROM MENU WHERE MENUID='"+childmenuid+"' AND ROWNUM<=1 ";
		System.out.println("MENUIDQRY __" + MENUIDQRY  );
		String menuid = NOREC;
		try {
			menuid = (String) jdbctemplate.queryForObject(MENUIDQRY, String.class);
		} catch (EmptyResultDataAccessException e) {  
			menuid = NOREC;
			e.printStackTrace();
		}catch( NullPointerException ne ){
			menuid = NOREC;
			ne.printStackTrace();
		}
		return  menuid ; 
	}
	
	
	public List getMailReservedKeys(String instid){
		String reservedqry = "SELECT RESV_KEY FROM IFP_MAIL_RESERVED_KEYS WHER INST_ID='"+instid+"'";
		return null;
	}
	
	
	public String getDate() {
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	public String getDatetime() {
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:m:ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	public String getDynaminContent(String mailcontent, String keymsg, HttpSession session, JdbcTemplate jdbcetmplate) {
		System.out.println("keymsg__"+ keymsg);
		String pattern1 = "$";
		String pattern2 = "$";
		
		String newmailcontent = mailcontent;
		String reservedkeys ="";
		if( mailcontent.contains("$")){
			Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
			Matcher m = p.matcher(mailcontent);
			int i = 1;
			while (m.find()) {
				reservedkeys =  "$"+m.group(1)+"$"; 
				 if( reservedkeys.equals("$KEYMSG$")) {
					 newmailcontent =  newmailcontent.replace(reservedkeys, keymsg);
					 continue;
				 } 
				 
				 if( reservedkeys.equals("$DATE$")) {
					 String datetime =  getDatetime();
					 newmailcontent =  newmailcontent.replace(reservedkeys, datetime);
					 continue;
				 } 
				 
				 if( reservedkeys.equals("$DATETIME$")) {
					 String datetime =  getDatetime();
					 newmailcontent =  newmailcontent.replace(reservedkeys, datetime);
					 continue;
				 } 
				 
				 
			}
			
		} 
		return newmailcontent;
	}
  
	public int getMailSequance(  String instid, JdbcTemplate jbdctemplate ){
		String seq_qry = "SELECT nvl( MAX(SEQUANCE_NO), 0 ) FROM IFP_MAIL_HISTORY WHERE INST_ID='"+instid+"'";
		int seqno = jbdctemplate.queryForInt(seq_qry)+1;
		//String updseq = "UPDATE IFP_MAIL_HISTORY SET SEQUANCE_NO='"+seqno+"' WHERE INST_ID ='"+instid+"'";
		//comInsntance().executeMerchantTransaction(updseq, jbdctemplate);
		return seqno;
	}
	
	private String parseAsSendingURL(String smsurl, String SEMSTO,	String SMSMESSAGE) { 
		
		System.out.println("smsurl__"+ smsurl);
		String pattern1 = "$";
		String pattern2 = "$";
		
		String newsmsurl = smsurl;
		
		if( smsurl.contains("$")){
			Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
			Matcher m = p.matcher(smsurl);
			int i = 1;
			while (m.find()) {
				String reservedkeys = "$"+m.group(1)+"$"; 
				 if( reservedkeys.equals("$TO$")) {
					 newsmsurl =  newsmsurl.replace(reservedkeys, SEMSTO);
					 continue;
				 } 
				 
				 if( reservedkeys.equals("$MSG$")) { 
					 newsmsurl =  newsmsurl.replace(reservedkeys, SMSMESSAGE);
					 continue;
				 }  
			}
		}else{
			newsmsurl = NOREC;
		}
		
		return newsmsurl;
	}
	
 
	
	
	public Boolean smsAlertRequired( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		 Boolean mailreq = true;
		try {
			String mailreqry="SELECT SMS_ALERT_REQ FROM IFP_INSTITUTION WHERE INST_ID='"+instid+"'";
			String mailreqstr = (String)jdbctemplate.queryForObject(mailreqry, String.class);
			if ( mailreqstr.equals("N")) {
				mailreq = false;
			}
		} catch (EmptyResultDataAccessException e) {}
		return mailreq;
	}
	
	
	public Boolean mailAlertRequired( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		 Boolean mailreq = true;
		try {
			String mailreqry="SELECT MAIL_ALERT_REQ FROM IFP_INSTITUTION WHERE INST_ID='"+instid+"'";
			String mailreqstr = (String)jdbctemplate.queryForObject(mailreqry, String.class);
			if ( mailreqstr.equals("N")) {
				mailreq = false;
			}
		} catch (EmptyResultDataAccessException e) {}
		return mailreq;
	}
	
	
	 
	
	public String maskNumbers(String number){
		 int index = 0;
		 String mask= "xxxx-xxxx-xxxx-####";
		 System.out.println("Mobileno :" + number + " Mask :" + mask);
		    StringBuilder maskedNumber = new StringBuilder();
		    for (int i = 0; i < mask.length(); i++) {
		        char c = mask.charAt(i);
		        if (c == '#') {
		            maskedNumber.append(number.charAt(index));
		            index++;
		        } else if (c == 'x') {
		            maskedNumber.append(c);
		            index++;
		        } else {
		            maskedNumber.append(c);
		        }
		    }  
		    return maskedNumber.toString();
	}
	
	
	public String formateMobile(  String mobile) {
		String mobilesuffix = "+255";
		String mobileno = null;
		if( mobile == null || mobile.equals("")){ 	return mobileno; 	}
		if(mobile.startsWith(mobilesuffix)){
			if( mobile.length() ==  13 ){ 	mobileno = mobile; }
		} 
		if(mobile.startsWith("0")){
			if( mobile.length() == 10 ){ mobileno = mobilesuffix+mobile.substring(1); 	}
		} 
		
		if( mobile.length() == 9 ){ mobileno = mobilesuffix+mobile; 	}
		
		return mobileno;
	}
	
	public int sendSmsMessage(String phoneno, String message ) throws Exception {
		trace("Phone Number : " + phoneno + " and message : " + message);
		String airtel ="^\\+?255[67]8.*$";
		String vodacom = "^\\+?2557[56].*$";
		String zantel = "^\\+?25577.*";
		String tigo = "^\\+?255(71|65).*$";
		String operator = "tigo-bulk-smpp";
		if( phoneno.matches(vodacom) ){
			trace("Mobile Number : " + phoneno +" and network type is Tigo "  );
			 operator = "vodacom-smpp";
		}else{
			trace("Mobile Number : " + phoneno +" and network type is Not tigo "  );
			 operator = "routesms-smpp";
		}
		 
		
		operator = "vodacom-bulk-smpp-2";
		 
		if( phoneno.matches(tigo) ){
			phoneno = phoneno.replace("+", "");
		}
		String timestamp= this.getDateTimeStamp();
		phoneno = formateMobile( phoneno );
		URL url = new URL("http://41.77.230.124:8080/smsbroker");
		trace(message);
		String data =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><bulk-request login=\"mart\" password=\"sBank\" ref-id=\""+timestamp+"\" delivery-notification-requested=\"true\" version=\"1.0\"><message id=\"1\" msisdn=\""+phoneno+"\" service-number=\"BPESA\" operator=\""+operator+"\"  validity-period=\"3\" priority=\"1\"><content type=\"text/plain\"> "+message+" </content></message></bulk-request>";
		trace(data);
		
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty ( "Content-Type", "text/xml" );
		OutputStreamWriter out = new OutputStreamWriter(
		httpConnection.getOutputStream());
		out.write(data);
		out.flush();
		out.close(); 
		InputStreamReader reader = new InputStreamReader( httpConnection.getInputStream() );
		StringBuilder buf = new StringBuilder();
		char[] cbuf = new char[ 2048 ];
		int num;
		
		while ( -1 != (num=reader.read( cbuf )))
		{
		buf.append( cbuf, 0, num );
		}
		
		String result = buf.toString();
		trace( "\nResponse received from server after POST" + result ); 
	 
		return 1;
	}
	
	
	public Socket getSocketConnection( String instid, Connection con) throws Exception {
		IsoFileGenDAO isodao = new IsoFileGenDAO();
		Socket connect_id = null;
		String hostid = "HOST";
		String hostip = this.getHostIpAddress(instid, hostid, con);
		isodao.log("CONFIGURED THE IP ADDRESS [ "+hostip+" ] ");
		if( hostip.equals("000.000.000.000")){  
			isodao.log("Could not connect host. Ip address not configured properly"); 
			return null;
	    	 
		}
		
		int port = this.getHostPort(hostip, con);
		isodao.log("CONNECTING PORT [ "+port+" ] ");
		if( port < 0 ){
			isodao.log("CONNECTION SERVER...HOST : "+ hostip +"- PORT : "+port); 
			isodao.log("Could not connect host. Ip address not configured properly"); 
			return null;
		}
		
		connect_id = isodao.connectHost( hostip, port, 60000 );
		if( connect_id == null ){ 
			System.out.println("COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port );   
			isodao.log("COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port ); 
	    	return null;
		} 
		return connect_id;
	}
	
	
	public Socket getSocketConnection( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		IsoFileGenDAO isodao = new IsoFileGenDAO();
		Socket connect_id = null;
		String hostid = "HOST";
		String hostip = this.getHostIpAddress(instid, hostid, jdbctemplate);
		isodao.log("CONFIGURED THE IP ADDRESS [ "+hostip+" ] ");
		if( hostip.equals("000.000.000.000")){  
			isodao.log("Could not connect host. Ip address not configured properly"); 
			return null;
	    	 
		}
		
		int port = this.getHostPort(hostid, jdbctemplate);
		isodao.log("CONNECTING PORT [ "+port+" ] ");
		if( port < 0 ){
			isodao.log("CONNECTION SERVER...HOST : "+ hostip +"- PORT : "+port); 
			isodao.log("Could not connect host. Ip address not configured properly"); 
			return null;
		}
		
		connect_id = isodao.connectHost( hostip, port, 60000 );
		if( connect_id == null ){ 
			System.out.println("COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port );   
			isodao.log("COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port ); 
	    	return null;
		} 
		return connect_id;
	}
	
	
}