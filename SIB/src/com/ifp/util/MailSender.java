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

public class MailSender implements Runnable {

	String instid;
	String alertid;
	String keymsg;
	JdbcTemplate jdbctemplate; 
	CommonDesc commondesc;
	CommonUtil commonutil;
	HttpSession reqsession;
	JavaMailSender mailSender;
	BaseAction base ;
	
	public MailSender( String instid, String alertid, String keymsg, JdbcTemplate jdbctemplate, CommonDesc commondesc,
			CommonUtil commonutil,HttpSession reqsession, JavaMailSender mailSender ){
		BaseAction base = new BaseAction();
		base.trace("Thread started ");
		try{
			this.instid=instid;
			this.alertid=alertid;
			this.keymsg=keymsg;
			this.jdbctemplate=jdbctemplate; 
			this.commondesc=commondesc;
			this.commonutil=commonutil;
			this.reqsession=reqsession;
			this.mailSender=mailSender;
			this.base=base; 
			new Thread(this).start();
			 
			//this.sendingPendingMails(instid);
		}catch(Exception e){ base.trace("Mail Exception : "+e.getMessage() ); e.printStackTrace(); }
	}
	 
	
	public MailSender(  JavaMailSender mailSender ){
		BaseAction base = new BaseAction();
		base.trace("Thread started ");
		try{ 
			this.mailSender=mailSender;  
			//this.sendingPendingMails(instid);
		}catch(Exception e){ base.trace("Mail Exception : "+e.getMessage() ); e.printStackTrace(); }
	}
	
	public MailSender( ){
		BaseAction base = new BaseAction();
		base.trace("Thread started ");
		try{ 
			this.mailSender=mailSender;  
			//this.sendingPendingMails(instid);
		}catch(Exception e){ base.trace("Mail Exception : "+e.getMessage() ); e.printStackTrace(); }
	}
	
	
	public void run() {
		
		String mailto = "";
		String mailcc = "";
		String mailsubject = "";
		String mailcontent =  "";
		 
		try{ 
			/*************BEGIN MAIL  *************************/
			 
			Boolean mailalertreq = commonutil.mailAlertRequired(instid, jdbctemplate);
			if( mailalertreq ) { 
				System.out.println( "sending mail....");   

			 	String mailattrqry = "SELECT CONTENT.MAIL_TO, CONTENT.MAIL_CC, CONTENT.MAIL_BCC, CONTENT.MAIL_SUBJECT, CONTENT.MAIL_CONTENT " ;
			 	mailattrqry +=  " FROM IFP_MAIL_CONTENTMAP CONTENT, IFP_MAIL_ALERTLIST ALERT WHERE CONTENT.INST_ID=ALERT.INST_ID AND CONTENT.ALERT_ID = ALERT.EMAIL_ALERT_LIST " ;
			 	mailattrqry += " AND ALERT.INST_ID='"+instid+"' AND ALERT.EMAIL_ALERT_LIST='"+alertid+"' AND ROWNUM=1";
			 	System.out.println("mailattqry : "+mailattrqry);
				List mailattrlist = jdbctemplate.queryForList(mailattrqry); 
						if( !mailattrlist.isEmpty() ){
							Iterator itr = mailattrlist.iterator();
							while( itr.hasNext() ){
								Map mp = (Map)itr.next();
								mailto = (String)mp.get("MAIL_TO"); 
								mailcc = (String)mp.get("MAIL_CC"); 
								mailsubject = commonutil.getDynaminContent( (String)mp.get("MAIL_SUBJECT"), keymsg, reqsession, jdbctemplate );  
								mailcontent = commonutil.getDynaminContent( (String)mp.get("MAIL_CONTENT"), keymsg, reqsession, jdbctemplate ); 
								base.trace("Mail content : "+mailcontent );
								mailSendingEngine(mailto, mailcc, mailsubject, mailcontent);
							} 
						}else{
							System.out.println("MAIL NOT REQUIRED ");
						}
			}
		}catch (Exception e) {   
			System.out.println("^^^^^^^^^^^^^^^^^^^SENDING MAIL FAILED ^^^^^^^^^^ ");
			System.out.println("Exception : "+ e.getMessage() );
						/*mailhistoryqry= "INSERT INTO IFP_MAIL_HISTORY ( INST_ID, SEQUANCE_NO, MAIL_TO, MAIL_CC, MAIL_BCC, MAIL_SUBJECT, MAIL_CONTENT, MAIL_STATUS, FAILED_REASON, MAILED_DATE, MAILED_SENT_BY)";
						mailhistoryqry += "VALUES ('"+instid+"', '"+mailhistoryseq+"', '"+commondesc.escSql(mailto)+"','"+commondesc.escSql(mailcc)+"','"+commondesc.escSql(mailbcc)+"','"+commondesc.escSql(mailsubject)+"','"+commondesc.escSql(mailcontent)+"', '0', '"+commondesc.escSql( e.getMessage() )+"', sysdate, '"+usercode+"')" ;
						commondesc.executeMerchantTransaction(mailhistoryqry, jdbctemplate); */
					 	e.printStackTrace();
					 
		}finally{
			//txManager.commit(mailtransact.status); 
			Thread.currentThread().interrupt();
			System.out.println(Thread.currentThread() +" Current thread closed properly");
		}
		 
		} 
	/*************END MAIL*************************/ 

	
	public int updateMailStatus(String instid, String seqno, JdbcTemplate jdbctemplate ) throws Exception{
		int x = -1;
		String updateqry = "UPDATE IFP_MAIL_HISTORY SET MAIL_STATUS='1', RETRYCOUNT=RETRYCOUNT+1  WHERE INST_ID='"+instid+"' AND SEQUANCE_NO='"+seqno+"' ";
		x = jdbctemplate.update(updateqry);
		return x;
		
	}
	public void sendingPendingMails(String instid ) {
		System.out.println("Re-Sending Pending mails...");
		String mailseqno = "";
		String mailto = "";
		String mailcc = "";
		String mailbcc = "";
		String mailsubject = "";
		String mailcontent =  "", failedreason="";
		int mailhistoryseq= commonutil.getMailSequance(instid, jdbctemplate);
		String mailhistoryqry = "";
		String mailstatusupdqry = "";  
		try{
			String pendingqrylist = "SELECT * FROM IFP_MAIL_HISTORY WHERE MAIL_STATUS='0'";
			List pendinglist = jdbctemplate.queryForList(pendingqrylist);
			if( !pendinglist.isEmpty() ){
				//IfpTransObj resendtransact = commondesc.myTranObject("RESENDMAIL", txManager);
				
				Iterator itr = pendinglist.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					mailseqno= (String)(Object)mp.get("SEQUANCE_NO").toString();
					System.out.println("Processing sequnace number..."+ mailseqno);
					mailto = (String)mp.get("MAIL_TO"); 
					mailcc = (String)mp.get("MAIL_CC"); 
					mailbcc = (String)mp.get("MAIL_BCC"); 
					mailsubject =  (String)mp.get("MAIL_SUBJECT"); 
					mailcontent =(String)mp.get("MAIL_CONTENT");  
					failedreason =(String)mp.get("FAILED_REASON");  
					
					mailcontent += "<br/><br/><p color='gray'> This mail is resended mail. the reason is : "+failedreason+" </p>'";
					int mailstatus = mailSendingEngine(mailto, mailcc, mailsubject, mailcontent);
					System.out.println("Mail sent status : "+ mailstatus);
					/*if( mailstatus == 1 ){
						updateMailStatus(instid, mailseqno, jdbctemplate); 
					}*/
					
				}
				
				//txManager.commit(resendtransact.status);
			}
		}catch(Exception e){ 
				System.out.println("Exception : "+e.getMessage()); 
				e.printStackTrace(); 
		}
	}
	
	
	public int mailSendingEngine(String mailto, String mailcc, String mailsubject, String mailcontent ) throws Exception{
	/*	Properties props = System.getProperties(); 
		Session session =  Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		Multipart mp = new MimeMultipart();
		try{ 
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
			return 1;
		}catch(Exception e ){
			System.out.println("Exception : "+e.getMessage()); 
			e.printStackTrace();
			return -1;
		}*/
		return 1;
	}
	
	

}
