package com.ifp.personalize;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ifp.Action.BaseAction;
public class SendmailAction extends BaseAction 
{
	private static final long serialVersionUID = 1L;
	
	static Properties properties = new Properties();
	static
	{
	
	properties.put("mail.smtp.host", "smtp.gmail.com");
	properties.put("mail.smtp.starttls.enable", "true");
	//properties.put("mail.smtp.socketFactory.port", "587");
	//properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	properties.put("mail.smtp.auth", "true");
	properties.put("mail.smtp.port", "587");
	
	
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
	
	}
	
	public String sendmailhome()
	{
		return "sendmailhome";
	}

	public String sendMailfromGmail()
	{
		final String from = "mowleesankar.hma@gmail.com";
		final String password = "sankar12!@";
		String to = getRequest().getParameter("to");
		String subject = getRequest().getParameter("subject");
		String body = getRequest().getParameter("body");
		System.out.println("SendmailAction");
		System.out.println("From===>"+from+"\nPassword===>"+password+"\nTo===>"+to+"\nSubject===>"+subject+"\nBody====>"+body);

		  try
	      {
	         Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator() 
	         {
	            protected PasswordAuthentication getPasswordAuthentication() 
	            {
	            	return new PasswordAuthentication(from, password);
	            }
	          }
	         );
	         System.out.println("------------1------------");
	         Message message = new MimeMessage(session);
	         System.out.println("------------2------------");
	         message.setFrom(new InternetAddress(from));
	         System.out.println("------------3------------");
	         message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
	         System.out.println("------------4------------");
	         message.setSubject(subject);
	         System.out.println("------------5------------");
	         message.setText(body);
	         System.out.println("------------6------------");
	         
	         Transport.send(message);
	         System.out.println("------------7------------");
	      }
	      catch(Exception e)
	      {
	    	  System.out.println("Error While Send Mail  ===>"+e);
	      }
	
		return sendmailhome();
	}
}

