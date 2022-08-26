package com.ifp.mail;
 
import java.util.Properties;

import javax.mail.*; 
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailService {
	public String sendMail( String toaddress, String subject, String content){

	    // *** CHANGED ***
	    final String username = "switch@orient-bank.com"; // ID you log into Windows with
	    final String password = "";
       
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "false");
	    props.put("mail.debug", "true");

	    props.put("mail.smtp.host", "10.207.159.240");      
	    props.put("mail.smtp.port", "25");
	    props.put("mail.smtp.auth.mechanisms","NTLM");

	    // *** CHANGED ***
	    props.put("mail.smtp.auth.ntlm.domain","WINDOMAIN"); 


	    Session session = Session.getInstance(props,new MyAuthenticator(username,password));

	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("switch@orient-bank.com"));
	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(toaddress));//bikash@prabhutech.net
	        message.setSubject( subject );
	        message.setText(content);
        
	        Transport.send(message);

	        System.out.println("Done.. Mail Sent Success");

	    } catch (MessagingException e) {
	        e.printStackTrace();
	        return "FAILED.."+e.getMessage();
	    }
	    
	    return "SUCCESS";
	  }


	  public static class MyAuthenticator extends Authenticator {

	    String user;
	    String pw;
	    public MyAuthenticator (String username, String password)
	    {
	        super();
	        this.user = username;
	        this.pw = password;
	    }
	    public PasswordAuthentication getPasswordAuthentication()
	    {
	        return new PasswordAuthentication(user, pw);
	    }
	  }
	  
	 
	}