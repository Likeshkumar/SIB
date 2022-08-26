package com.ifp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import javax.sql.DataSource;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import com.ifp.util.CommonDesc;

public class CommonUtil extends BaseAction implements ServletResponseAware{
	 
	
	private final long serialVersionUID = 1L;
	String NOREC = "NOREC";
	  
	
	public CommonUtil() {}

	
	
	 
	public CommonDesc comInsntance(){
		CommonDesc commondesc = new CommonDesc();
		return commondesc;
	} 
	
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
	

	public String getHostIpAddress( String instid, JdbcTemplate jdbctemplate) {
		String ipaddress =null; 
		try {
			String hostipqry = "SELECT IP_ADDRESS FROM IFD_HOST_MASTER WHERE INST_ID='"+instid+"' AND HOST_ID='HOST' ";
			ipaddress = (String) jdbctemplate.queryForObject(hostipqry, String.class); 
		} catch (DataAccessException e) { 
			e.printStackTrace();
		}
		return ipaddress;
	}
	
	
	public int getHostPort( String hostip, JdbcTemplate jdbctemplate ) {
		int hostport = -1; 
		try { 
			String hostipqry = "SELECT PORT FROM  (    SELECT PORT FROM IFD_HOST_CONNECTION WHERE IP_ADDR='"+hostip+"' AND  PORT_STATUS='0' ORDER BY dbms_random.value  ) WHERE rownum <= 1";
			System.out.println( "hostipqry : " + hostipqry);
			hostport =  jdbctemplate.queryForInt(hostipqry);
		} catch (DataAccessException e) { 
			e.printStackTrace();
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
		//trace("selectApplictionType method called ...");
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
					System.out.println("appname --- "+appname);  
				}else if(apptypelist.size() > 1){
					appname = "BOTH";
				}else{
					appname = "NOREC";
				}
			}
		} 
		      
		//trace("selectApplictionType method end ...");
		return appname;
	}
	
	public String cardCurrentStatus(String instid, String cardno, JdbcTemplate jdbctemplate){
		String ins_cardexist = "SELECT COUNT(*) FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		int ins_exist = jdbctemplate.queryForInt(ins_cardexist);
		if( ins_exist == 0 ){
			String pers_cardexist = "SELECT COUNT(*) FROM PERS_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
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
		
		/*String MENUIDQRY ="SELECT MENUID FROM IFMENU WHERE ACTION='"+urlnameaction+"' AND ROWNUM<=1 ";*/
		//by gowtham-190819
				/*String MENUIDQRY ="SELECT MENUID FROM IFMENU WHERE ACTION='"+urlnameaction+"' AND ROWNUM<=1 ";*/
				String MENUIDQRY ="SELECT MENUID FROM IFMENU WHERE ACTION=? AND ROWNUM<=?";
				
		
		
		System.out.println("MENUIDQRY __" + MENUIDQRY  );
		String menuid = NOREC;
		try {
			
			
			menuid = (String) jdbctemplate.queryForObject(MENUIDQRY,new Object[]{urlnameaction,"1"}, String.class);
			
			/*menuid = (String) jdbctemplate.queryForObject(MENUIDQRY, String.class);*/
			
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
		
		/*String MENUIDQRY ="SELECT PARENTID FROM IFMENU WHERE MENUID='"+childmenuid+"' AND ROWNUM<=1 ";*/
		
		//by gowtham-190819
		String MENUIDQRY ="SELECT PARENTID FROM IFMENU WHERE MENUID=? AND ROWNUM<=? ";

		
		System.out.println("MENUIDQRY __" + MENUIDQRY  );
		String menuid = NOREC;
		try {
			
			/*menuid = (String) jdbctemplate.queryForObject(MENUIDQRY, String.class);*/
			menuid = (String) jdbctemplate.queryForObject(MENUIDQRY,new Object[]{childmenuid,"1"}, String.class);
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
				 
				 if( reservedkeys.equals("$USERNAME$")) { 
					 String usercode =   (String)session.getAttribute("USERID"); 
					 String instid =   (String)session.getAttribute("Instname"); 
					 String username = comInsntance().getUserName(instid, usercode, jdbcetmplate);
					 newmailcontent =  newmailcontent.replace(reservedkeys, username);
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
	
	public int sendSMS( String instid, String alertid, String keymsg, JdbcTemplate jdbctemplate, HttpSession reqsession ) throws IOException{
		String SEMSTO= "";
		String SMSMESSAGE = "";
		String smshistroryqry = "";
		String[] smsresponse = null;
		String mobileno = "";
		String smsrespstatus = "";
		String responsetext = "";
		try {
			String smstemplateqry = "SELECT SMS_CONTENT, SMS_TO FROM IFP_SMS_TEMPLATE WHERE INST_ID='"+instid+"' AND ALERT_ID='"+alertid+"'"; 
			System.out.println( "mailattrqry..." + smstemplateqry ); 
			List mailattrlist = jdbctemplate.queryForList(smstemplateqry); 
			if( !mailattrlist.isEmpty() ){
				Iterator itr = mailattrlist.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					SEMSTO= (String)mp.get("SMS_TO");
					SMSMESSAGE =  this.getDynaminContent(  (String)mp.get("SMS_CONTENT"), keymsg, reqsession, jdbctemplate );
				}
				
				String smsurlqry = "SELECT SMS_URL FROM IFP_SMS_URL WHERE INST_ID='"+instid+"' AND STATUS='1'";
				String smsurl = (String)jdbctemplate.queryForObject(smsurlqry, String.class);
				String sendingurl = this.parseAsSendingURL( smsurl, SEMSTO, SMSMESSAGE );
				System.out.println( "sendingurl__" + sendingurl );
				if( !sendingurl.equals(NOREC)){
			        String stringToReverse = URLEncoder.encode(sendingurl, "UTF-8"); 
			        URL url = new URL(sendingurl);
			        URLConnection connection = url.openConnection();
			        connection.setDoOutput(true);

			        OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream());			        
			        System.out.println("string=" + stringToReverse);
			        out.close();

			        BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
			        String decodedString;
			        
			        while ((decodedString = in.readLine()) != null) {
			            System.out.println(decodedString);
			            System.out.println("decodedString=" + decodedString);
			            
			            smsresponse = decodedString.split("\\|");
			            smsrespstatus = smsresponse[0];
			            System.out.println("smsrespstatus=" + smsrespstatus);
			            mobileno = smsresponse[1];
			            System.out.println("mobileno=" + mobileno);
			            responsetext = smsresponse[2];
			            System.out.println("responsetext=" + responsetext);
			            
			            smshistroryqry = "INSERT INTO IFP_SMS_HISTORY (INST_ID, ALERT_ID, SENDING_MSG, SMS_TO, SMS_SENT_STATUS, SMS_REF_NO, SMS_DATE) VALUES ";
			            smshistroryqry += "( '"+instid+"', '"+alertid+"', '"+SMSMESSAGE+"', '"+mobileno+"', '"+smsrespstatus+"', '"+responsetext+"', SYSDATE)";
			            comInsntance().executeMerchantTransaction(smshistroryqry, jdbctemplate); 
			            
			        }
			        in.close();
				}else{
					responsetext = "NO KEY WORD FOUND IN URL";
					smsrespstatus = "configerror";
					mobileno = SEMSTO;
				} 
				
			}
		} catch (Exception e) {
			responsetext = e.getMessage();
			smsrespstatus = "configerror";
			mobileno = SEMSTO;
		}
		 
		smshistroryqry = "INSERT INTO IFP_SMS_HISTORY (INST_ID, ALERT_ID, SENDING_MSG, SMS_TO, SMS_SENT_STATUS, SMS_REF_NO, SMS_DATE) VALUES ";
        smshistroryqry += "( '"+instid+"', '"+alertid+"', '"+SMSMESSAGE+"', '"+mobileno+"', '"+smsrespstatus+"', '"+responsetext+"', SYSDATE)";
        comInsntance().executeMerchantTransaction(smshistroryqry, jdbctemplate); 
        
		return 1;
}
	
	
	public Boolean smsAlertRequired( String instid, JdbcTemplate jdbctemplate ) throws Exception {
		 Boolean mailreq = true;
		try {
			String mailreqry="SELECT SMS_ALERT_REQ FROM INSTITUTION WHERE INST_ID='"+instid+"'";
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
			String mailreqry="SELECT MAIL_ALERT_REQ FROM INSTITUTION WHERE INST_ID='"+instid+"'";
			String mailreqstr = (String)jdbctemplate.queryForObject(mailreqry, String.class);
			if ( mailreqstr.equals("N")) {
				mailreq = false;
			}
		} catch (EmptyResultDataAccessException e) {}
		return mailreq;
	}
	
	
	public int sendMail(String instid, String alertid, String keymsg, JdbcTemplate jdbctemplate, HttpSession reqsession, JavaMailSender mailSender) throws Exception {
		
		/*************BEGIN SMS *************************//*
			try {
				this.sendSMS(instid, alertid, keymsg, jdbctemplate, reqsession);
			} catch (IOException e1) { 
				e1.printStackTrace();
			}*/
		
		
		/*************END SMS *************************/
		
		/*************BEGIN MAIL  *************************/
		int res = 0 ;
		Boolean mailalertreq = this.mailAlertRequired(instid, jdbctemplate);
		if( mailalertreq ) { 
			System.out.println( "sending mail....");
			
			String mailto = "";
			String mailcc = "";
			String mailbcc = "";
			String mailsubject = "";
			String mailcontent =  "";
			int mailhistoryseq= this.getMailSequance(instid, jdbctemplate);
			String mailhistoryqry = "";
			String mailstatusupdqry = "";  
			String usercode = (String)reqsession.getAttribute("USERID");
		 	String mailattrqry = "SELECT CONTENT.MAIL_TO, CONTENT.MAIL_CC, CONTENT.MAIL_BCC, CONTENT.MAIL_SUBJECT, CONTENT.MAIL_CONTENT " ;
		 	mailattrqry +=  " FROM IFP_MAIL_CONTENTMAP CONTENT, IFP_MAIL_ALERTLIST ALERT WHERE CONTENT.INST_ID=ALERT.INST_ID AND CONTENT.ALERT_ID = ALERT.EMAIL_ALERT_LIST " ;
		 	mailattrqry += " AND ALERT.INST_ID='"+instid+"' AND ALERT.EMAIL_ALERT_LIST='"+alertid+"' AND ROWNUM=1";
		 
			System.out.println( "mailattrqry..." + mailattrqry ); 
			List mailattrlist = jdbctemplate.queryForList(mailattrqry); 
			if( !mailattrlist.isEmpty() ){
				Iterator itr = mailattrlist.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					mailto = (String)mp.get("MAIL_TO"); 
					mailcc = (String)mp.get("MAIL_CC"); 
					mailbcc = (String)mp.get("MAIL_BCC"); 
					mailsubject = this.getDynaminContent( (String)mp.get("MAIL_SUBJECT"), keymsg, reqsession, jdbctemplate );  
					mailcontent = this.getDynaminContent( (String)mp.get("MAIL_CONTENT"), keymsg, reqsession, jdbctemplate );  
					
					mailhistoryqry= "INSERT INTO IFP_MAIL_HISTORY ( INST_ID, SEQUANCE_NO, MAIL_TO, MAIL_CC, MAIL_BCC, MAIL_SUBJECT, MAIL_CONTENT, MAIL_STATUS, FAILED_REASON, MAILED_DATE, MAILED_SENT_BY)";
					mailhistoryqry += "VALUES ('"+instid+"', '"+mailhistoryseq+"', '"+comInsntance().escSql(mailto)+"','"+comInsntance().escSql(mailcc)+"','"+comInsntance().escSql(mailbcc)+"','"+comInsntance().escSql(mailsubject)+"','"+comInsntance().escSql(mailcontent)+"', '1', '', sysdate, '"+usercode+"')" ;
					
					System.out.println( "mailhistoryqry__" + mailhistoryqry );
					comInsntance().executeMerchantTransaction(mailhistoryqry, jdbctemplate);
					
					
				}
				 
				Properties props = System.getProperties();
				System.out.println( "props...."+props);
				Session session =  Session.getDefaultInstance(props, null);
				MimeMessage message = new MimeMessage(session);
				Multipart mp = new MimeMultipart();
				
				 
				try {
					System.out.println( "TO..." + mailto );
					System.out.println( "CCC..." + mailcc );
					System.out.println( "BCC..." + mailbcc );
					System.out.println( "SUBJECT..." + mailsubject );
					System.out.println( "CONTENT..." + mailcontent );
				 
					message.addFrom( InternetAddress.parse("services@bpesa.com") );
					message.setHeader("Content-Type", "text/html");
					message.setRecipients(Message.RecipientType.TO, (InternetAddress.parse(mailto)));
					message.setSubject(mailsubject);
					
					MimeBodyPart htmlPart = new MimeBodyPart();
					htmlPart.setContent(mailcontent, "text/html; charset=ISO-8859-1"); 
					mp.addBodyPart(htmlPart); 
					message.setContent(mp); 
					
					message.setRecipients(Message.RecipientType.CC, (InternetAddress.parse(mailcc)));
				//	message.setRecipients(Message.RecipientType.BCC, (InternetAddress.parse(mailbcc)));
					System.out.println( "message__" + message);
					
					mailSender.send(message);
					System.out.println( " MAIL SENT SUCCESSFULLY " );
					res = 1;
				} catch (Exception e) {  
					CommonDesc commondesc = new CommonDesc();
					mailstatusupdqry = "UPDATE IFP_MAIL_HISTORY SET MAIL_STATUS='0', FAILED_REASON='"+ commondesc.escSql( e.getMessage() ) +"' WHERE INST_ID='"+instid+"' AND SEQUANCE_NO='"+mailhistoryseq+"'";
					System.out.println( " mailstatusupdqry : " + mailstatusupdqry );
					comInsntance().executeMerchantTransaction(mailstatusupdqry, jdbctemplate); 
					System.out.println( "mailstatusupdqry_" + mailstatusupdqry);
					System.out.println( " MAIL SENT FAILED " +e );
					e.printStackTrace();
					res = -1;
				}   
				
			}else{
				res = 2;
			}
		}
		
		return res;
	}
	/*************END MAIL*************************/
	
	
	public void sendMail(String instid, String alertid, String keymsg, JdbcTemplate jdbctemplate, PlatformTransactionManager  txManager, CommonDesc commondesc, HttpSession reqsession, JavaMailSender mailSender) throws Exception {
	
		String mailto = "";
		String mailcc = "";
		String mailbcc = "";
		String mailsubject = "";
		String mailcontent =  "";
		int mailhistoryseq= this.getMailSequance(instid, jdbctemplate);
		String mailhistoryqry = "";
		String mailstatusupdqry = "";  
		IfpTransObj mailtransact = commondesc.myTranObject("MAILSEND", txManager);
		try{ 
			/*************BEGIN MAIL  *************************/
			 
			Boolean mailalertreq = this.mailAlertRequired(instid, jdbctemplate);
			if( mailalertreq ) { 
				System.out.println( "sending mail...."); 
				
				String usercode = (String)reqsession.getAttribute("USERID");
			 	String mailattrqry = "SELECT CONTENT.MAIL_TO, CONTENT.MAIL_CC, CONTENT.MAIL_BCC, CONTENT.MAIL_SUBJECT, CONTENT.MAIL_CONTENT " ;
			 	mailattrqry +=  " FROM IFP_MAIL_CONTENTMAP CONTENT, IFP_MAIL_ALERTLIST ALERT WHERE CONTENT.INST_ID=ALERT.INST_ID AND CONTENT.ALERT_ID = ALERT.EMAIL_ALERT_LIST " ;
			 	mailattrqry += " AND ALERT.INST_ID='"+instid+"' AND ALERT.EMAIL_ALERT_LIST='"+alertid+"' AND ROWNUM=1";
			 
				System.out.println( "mailattrqry..." + mailattrqry ); 
				List mailattrlist = jdbctemplate.queryForList(mailattrqry); 
						if( !mailattrlist.isEmpty() ){
							Iterator itr = mailattrlist.iterator();
							while( itr.hasNext() ){
								Map mp = (Map)itr.next();
								mailto = (String)mp.get("MAIL_TO"); 
								mailcc = (String)mp.get("MAIL_CC"); 
								mailbcc = (String)mp.get("MAIL_BCC"); 
								mailsubject = this.getDynaminContent( (String)mp.get("MAIL_SUBJECT"), keymsg, reqsession, jdbctemplate );  
								mailcontent = this.getDynaminContent( (String)mp.get("MAIL_CONTENT"), keymsg, reqsession, jdbctemplate );  
								
								mailhistoryqry= "INSERT INTO IFP_MAIL_HISTORY ( INST_ID, SEQUANCE_NO, MAIL_TO, MAIL_CC, MAIL_BCC, MAIL_SUBJECT, MAIL_CONTENT, MAIL_STATUS, FAILED_REASON, MAILED_DATE, MAILED_SENT_BY)";
								mailhistoryqry += "VALUES ('"+instid+"', '"+mailhistoryseq+"', '"+comInsntance().escSql(mailto)+"','"+comInsntance().escSql(mailcc)+"','"+comInsntance().escSql(mailbcc)+"','"+comInsntance().escSql(mailsubject)+"','"+comInsntance().escSql(mailcontent)+"', '1', '', sysdate, '"+usercode+"')" ;
								
								System.out.println( "mailhistoryqry__" + mailhistoryqry );
								comInsntance().executeMerchantTransaction(mailhistoryqry, jdbctemplate);
								
								
							}
							 
							Properties props = System.getProperties();
							System.out.println( "props...."+props);
							Session session =  Session.getDefaultInstance(props, null);
							MimeMessage message = new MimeMessage(session);
							Multipart mp = new MimeMultipart();
							
							System.out.println( "mailSender__" + mailSender);  
								message.addFrom( InternetAddress.parse("services@bpesa.com") );
								message.setHeader("Content-Type", "text/html");
								message.setRecipients(Message.RecipientType.TO, (InternetAddress.parse(mailto)));
								message.setSubject(mailsubject);
								
								MimeBodyPart htmlPart = new MimeBodyPart();
								htmlPart.setContent(mailcontent, "text/html; charset=ISO-8859-1"); 
								mp.addBodyPart(htmlPart); 
								message.setContent(mp); 
								
								message.setRecipients(Message.RecipientType.CC, (InternetAddress.parse(mailcc)));
							//	message.setRecipients(Message.RecipientType.BCC, (InternetAddress.parse(mailbcc))); 
								mailSender.send(message);
								System.out.println( " MAIL SENT SUCCESSFULLY " ); 
						}
			}
		} catch (Exception e) {   
					mailstatusupdqry = "UPDATE IFP_MAIL_HISTORY SET MAIL_STATUS='0', FAILED_REASON='"+ commondesc.escSql( e.getMessage() ) +"' WHERE INST_ID='"+instid+"' AND SEQUANCE_NO='"+mailhistoryseq+"'";
				 	comInsntance().executeMerchantTransaction(mailstatusupdqry, jdbctemplate);  
					System.out.println( " MAIL SENT FAILED " +e );
					e.printStackTrace();
					 
		}finally{
			txManager.commit(mailtransact.status);
		}
		 
		} 
	/*************END MAIL*************************/
		

	
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
	
	

	
	
}