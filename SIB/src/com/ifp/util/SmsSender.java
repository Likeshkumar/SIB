package com.ifp.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;

public class SmsSender implements Runnable {

	String instid;
	String mobileno;
	String smscontent; 
	CommonUtil comutil;
	
	public SmsSender( String instid, String mobilenumber, String smscontent, CommonUtil comutil ){
		BaseAction base = new BaseAction();
		base.trace("Thread started ");
		try{
			this.instid=instid;
			this.mobileno=mobilenumber;
			this.smscontent=smscontent;
			this.comutil=comutil;
			
			new Thread(this).start();
			 
			//this.sendingPendingMails(instid);
		}catch(Exception e){ base.trace("Mail Exception : "+e.getMessage() ); e.printStackTrace(); }
	}
	 
	
	
	public void run() { 
		CommonUtil comutil = new CommonUtil();
		try{ 
			/*************BEGIN SMS*************************/
			comutil.sendSmsMessage(mobileno, smscontent);
		 
		}catch (Exception e) {   
			System.out.println("^^^^^^^^^^^^^^^^^^^SENDING SMS FAILED ^^^^^^^^^^ ");
			System.out.println("Exception sms : "+ e.getMessage() ); 
			e.printStackTrace();
					 
		}finally{ 
			Thread.currentThread().interrupt();
			System.out.println(Thread.currentThread() +" Current thread closed properly");
		}
		 
		} 
	/*************END SMS*************************/ 
 

}
