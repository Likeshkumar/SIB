package com.ifp.Action;

//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.ifp.util.Randomalphnumeric;
import com.ifp.util.ResponseCheck;
import com.ifp.exceptions.Exceptionifp;
public class ThalesAction extends BaseAction
{
	
	//Pingenerationbean pin_bean = new Pingenerationbean();
	private static final long serialVersionUID = 1L;
	
	
	private PlatformTransactionManager  txManager;

	public PlatformTransactionManager getTxManager() 
	{
		return txManager;
	}
	public void setTxManager(PlatformTransactionManager txManager) 
	{
		this.txManager = txManager;
	}	
	
   
	String Delimtchar="";
	String Message_trailer="",hsmresponse;
	String cvv_delimiter=";";
	

	/*
	ME - Pin maler is empty, no fields are configured for printing on mailer
	PFDE - exception in function getPrintFieldData */	
	
 
	
/*	
	
public String thalesIBMdesPingeneration(Socket connection,String Refnum,String instname, DataSource datasource,HSMParameter hsmparams,PinParameter pinparam,PlatformTransactionManager txManager,String Card_Status) throws Exception
{
	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	Randomalphnumeric rs = new Randomalphnumeric();
	System.out.println("Connectio received is ---->"+connection);
	System.out.println("Refnum received is ---->"+Refnum);
  	System.out.println("Instituion received is ---->"+instname);
  	int n_count = pin_bean.getNum_cards();
  	String pvk = pin_bean.getPvk();
  	String pvki = pin_bean.getPvki();
  	String cvv_req=pin_bean.getCvv_required();
  	System.out.println("Count of cards"+n_count);
  	System.out.println("Pvk   ==>"+pvk+"Pvki    ==>"+pvki);
  	String[] cards=new String[n_count+1]; 
  	int headerlen=pin_bean.getHeader_len();
  	String chnlen=pin_bean.getChnlen();
  	int chnlength=Integer.parseInt(chnlen);
	String hsmresponse=null,random_pin=null,hsmheader_value=null,offset=null;
	String resp_code;
	String query;
	String service_code=pin_bean.getService_code();
	String cvk1=pin_bean.getCvvk1();
	String cvk2=pin_bean.getCvvk2();
	String cvk_pair=cvk1+cvk2;
	String cvv1=null;
	String cvv2=null;
	String pinlength=pin_bean.getPin_length();
	pinlength="0"+pinlength;
	int int_pinlength=Integer.parseInt(pinlength);
	String pin_offset=null;
	String decimilisation_table=pin_bean.getDecimilisation_table();
	String pin_validation_data=null;
	String pan_offset=pin_bean.getPan_offset();
	String pan_validation_length=pin_bean.getPanvalidation_length().trim();
	String pan_pad_char=pin_bean.getPanpadchar();
	String pinmailer_id=pin_bean.getPinmailer_id();
	System.out.println("pan_offset YYYY      YYYYYYYYYYYYYYY  "+ pan_offset+"  pan_validation_length    YYYYYYYYYYYYYYY  "+ pan_validation_length+"  pan_pad_char    YYYYYYYYYYYYYYY  "+ pan_pad_char);
  	System.out.println("Generated Random Number is  XXXXXXXXXXXXXX   "+ chnlength);
  	cards = gettingcardsTO_pingen(Refnum,instname,n_count,datasource);
  	if(cards[0].equals("NOCARD"))
 	{
	  		pin_bean.setPin_proc_status("03");
	  		System.out.println(" Error while getting Cards ");
		    return "Exception";
	  		
  	}
	System.out.println("cardb  return From DB is ============= >>>> "+cards[0]);
	for(int j =0; j<n_count; j++)
	try
	{
			TransactionStatus status = txManager.getTransaction(def);
	  		String Header = rs.Random_number(headerlen);
	  		String chn=cards[j];
	  		System.out.println("Generated Random Number is  ======>   "+ Header);
	  		//command = Header+"PA>L>L>L>L>L>L>L>L>008^0>L>008^1>L>008^2>L>008^3>055^P>L>048^V>L>008^4>L>008^5>L>008^6>L>008^7>L>008^8>L>L>L>L>L>L>L>";
	  		System.out.println("Card Nos to send to HSM is "+chn);
	  		System.out.println("");
	  		System.out.println("");
	  		   if (connection != null) 
	  		   {
	  			    System.out.println("<<< Connected to HSM  >>>:" + connection.isConnected());
	  			    in = new DataInputStream (new BufferedInputStream(connection.getInputStream()));
	  			    out = new DataOutputStream (new BufferedOutputStream(connection.getOutputStream()));
	  		       
	  			    //sending Load pin mailer parameter to hsm 
	  			    command = Header+"PA"+getLoadFormatData(pinmailer_id,datasource,instname);
	  			    //getting hsm response
	  			    hsmresponse=writeCommandToHSM(command);
	  			    hsmheader_value=getheaderValue(hsmresponse,pin_bean);
	  		        resp_code=checkHSMresponse(hsmresponse,command,pin_bean);
	  		        String chn_12digit= chn.substring(chnlength-13, chnlength-1);
	  		        System.out.println("chn_12digit :::::::::  " + chn_12digit);   
	  	        
	  		        
	  		        System.out.println("Header from HSM response:::::::::" + hsmheader_value);   
	  			   //checking Load pin mailer parametes sucess or not if sucess generate random pin for that card
	  			   
	  			    	if((hsmheader_value.equals(Header)))
	  				    {
	  			    		if(!(resp_code.equals("00")))
	  							{
	  					    		pin_bean.setPin_proc_status(resp_code);
	  						    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  							      System.out.println(" response Message ::::::::::::::::  " + resp_check_obj.respCheck(resp_code));
	  							    break;
	  					    	}
	  				    	
	  				    }
	  			    	
	  			    	if(!(hsmheader_value.equals(Header)))
	  			    		{
	  					    		System.out.println("HSM header does'nt match ");
	  					    		pin_bean.setPin_proc_status("01");
	  					    		break;
	  			    		}
	  			    	
	  			    
	  			    if(resp_code.equals("00"))
	  						    {
	  						    	System.out.println("<<< generating random pin     >>> " );
	  						    	System.out.println("<<< ================== >>>"+chnlength+"<<<<=================>>>");
	  						    	command=Header+"JA"+chn_12digit+pinlength+Delimtchar+Message_trailer;
	  								System.out.println("command for generating random pin "+command);
	  								hsmresponse=writeCommandToHSM(command);
	  							    
	  							    hsmheader_value=getheaderValue(hsmresponse,pin_bean);
	  						        resp_code=checkHSMresponse(hsmresponse,command,pin_bean);
	  						        System.out.println("Header from HSM response:::::::::" + hsmheader_value);   
	  						        if((hsmheader_value.equals(Header)))
	  							    {
	  						    		if(!(resp_code.equals("00")))
	  										{
	  								    		pin_bean.setPin_proc_status(resp_code);
	  									    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  										      System.out.println(" response Message ::::::::::::::::  " + resp_check_obj.respCheck(resp_code));
	  										    break;
	  								    	}
	  							    	
	  							    }
	  						    	
	  						    	if(!(hsmheader_value.equals(Header)))
	  						    		{
	  								    		System.out.println("HSM header does'nt match ");
	  								    		pin_bean.setPin_proc_status("01");
	  								    		break;
	  						    		}	    
	  						    }
	  		        
	  			    random_pin=hsmresponse.substring(headerlen+4);
	  			    //random pin will be send for PE while printing pin on mailer
	  			    System.out.println("<<<  random_pin ++++++++++++++++ " + random_pin);
	     
	  			   //GENERATING OFFSET IBMDES 
	  			
	  			    if(resp_code.equals("00"))
	  					    {
	  					    	System.out.println("<<< GENERATING OFFSET IBMDES   >>> " );
	  					    	System.out.println("<<< ================== >>>"+chnlength+"<<<<=================>>>");
	  					    	pin_validation_data=generatePinValidationData(chn,pan_offset,pan_validation_length,pan_pad_char);
	  					    	command=Header+"DE"+pvk+random_pin+pinlength+chn_12digit+decimilisation_table+pin_validation_data;
	  							System.out.println("command for generating IBM DES pin "+command);
	  							hsmresponse=writeCommandToHSM(command);
	  							
	  							hsmheader_value=getheaderValue(hsmresponse,pin_bean);
	  					        resp_code=checkHSMresponse(hsmresponse,command,pin_bean);
	  					        System.out.println("Header from HSM response:::::::::" +hsmheader_value);   
	  					        if((hsmheader_value.equals(Header)))
	  						    {
	  					    		if(!(resp_code.equals("00")))
	  									{
	  							    		pin_bean.setPin_proc_status(resp_code);
	  								    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  									      System.out.println(" response Message ::::::::::::::::  " + resp_check_obj.respCheck(resp_code));
	  									    break;
	  							    	}
	  						    	
	  						    }
	  					    	
	  					    	if(!(hsmheader_value.equals(Header)))
	  					    		{
	  							    		System.out.println("HSM header does'nt match ");
	  							    		pin_bean.setPin_proc_status("01");
	  							    		break;
	  					    		}
	  							
	  							
	  					    }
	  			  
	  			    offset=hsmresponse.substring( headerlen+4);
	  			    System.out.println("<<<  offset ++++++++++++++++ " + offset);
	  			    
	  			  // GENERATING PIN Using the IBM Method
  
	  			  if(resp_code.equals("00"))
				    {
				    	System.out.println("<<< GENERATING ENCRYPTED PIN Using the IBM Method     >>> " );
				    	System.out.println("<<< ================== >>>"+chnlength+"<<<<=================>>>");
				    	pin_validation_data=generatePinValidationData(chn,pan_offset,pan_validation_length,pan_pad_char);
				    	command=Header+"EE"+pvk+offset+pinlength+chn_12digit+decimilisation_table+pin_validation_data;
						System.out.println("command for generating IBM DES pin "+command);
						hsmresponse=writeCommandToHSM(command);
						
						hsmheader_value=getheaderValue(hsmresponse,pin_bean);
				        resp_code=checkHSMresponse(hsmresponse,command,pin_bean);
				        System.out.println("Header from HSM response:::::::::" + hsmheader_value);   
				        if((hsmheader_value.equals(Header)))
						    {
					    		if(!(resp_code.equals("00")))
									{
							    		pin_bean.setPin_proc_status(resp_code);
								    	 // String response_message=ResponseCheck.respCheck(resp_code);
									      System.out.println(" response Message ::::::::::::::::  " + resp_check_obj.respCheck(resp_code));
									    break;
							    	}
						    	
						    }
				    	
				    	if(!(hsmheader_value.equals(Header)))
				    		{
						    		System.out.println("HSM header does'nt match ");
						    		pin_bean.setPin_proc_status("01");
						    		break;
				    		}
						
						
				    }
		  
				    pin_offset=hsmresponse.substring( headerlen+4,headerlen+4+int_pinlength );
				    //pin offset wiill be stored in db
				    System.out.println("<<<  pin_offset ++++++++++++++++ " + pin_offset);
	  			   
				    System.out.println("<<<  resp_code ++++++++++++++++ " + resp_code);
    
	  			    if(resp_code.equals("00"))
	  			    {
	  			    	pinmailer_id=pin_bean.getPinmailer_id();
	  			    	System.out.println("<<<Printing Pin in Pin Mailer   >>> " );
	  			    	command=Header+"PE"+pin_bean.getDocument_type()+chn_12digit+random_pin;
	  			    	command=command+getPrintFieldData(pinmailer_id,datasource,instname,chn,Refnum,pin_bean);
	  			    	//String command=getPinmailerCommand(pinmailer_id);
	  			    	System.out.println("<<< PIN MAILER COMMAND  ------------ "+command );
	  			    	
	  			    	hsmresponse=writeCommandToHSM(command);
	  					
	  					hsmheader_value=getheaderValue(hsmresponse,pin_bean);
	  			        resp_code=checkHSMresponse(hsmresponse,command,pin_bean);
	  			        System.out.println("Header from HSM response:::::::::" + hsmheader_value);   
	  			        if((hsmheader_value.equals(Header)))
					    {
				    		if(!(resp_code.equals("00")))
								{
						    		pin_bean.setPin_proc_status(resp_code);
							    	 // String response_message=ResponseCheck.respCheck(resp_code);
								      System.out.println(" response Message ::::::::::::::::  " + resp_check_obj.respCheck(resp_code));
								    break;
						    	}
					    	
					    }
				    	
				    	if(!(hsmheader_value.equals(Header)))
				    		{
						    		System.out.println("HSM header does'nt match ");
						    		pin_bean.setPin_proc_status("01");
						    		break;
				    		}
				    	
	  					if(resp_code.equals("00"))
	  							{
	  							    	System.out.println("<<< Waiting for Printer Confermation command PZ  ------------ " );
	  							    	hsmresponse = in.readUTF();
	  							    	
	  							    	//for testing PZcommand is sending to get response PZ00
	  							    	hsmheader_value=getheaderValue(hsmresponse,pin_bean);
	  							    	
	  							        System.out.println("hsmresponse  ::::::::: " + hsmresponse);   
	  							        String pz_command=hsmresponse.substring(headerlen, headerlen+2);
	  							        if(!(pz_command.equals("PZ")))
	  							        {
	  							        	pin_bean.setPin_proc_status("PP");
	  							        	break;	  							        	
	  							        }
	  							        resp_code=hsmresponse.substring(headerlen+2, headerlen+4);
	  							        System.out.println("Header from HSM response:::::::::" + hsmheader_value);   
	  							        if((hsmheader_value.equals(Header)))
	  										    {
	  									    		if(!(resp_code.equals("00")))
	  													{
	  											    		pin_bean.setPin_proc_status(resp_code);
	  												    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  													      System.out.println(" response Message ::::::::::::::::  " + resp_check_obj.respCheck(resp_code));
	  													    break;
	  											    	}
	  										    
	  										    }
	  							    	
	  							    	if(!(hsmheader_value.equals(Header)))
	  								    		{
	  										    		System.out.println("HSM header does'nt match ");
	  										    		pin_bean.setPin_proc_status("01");
	  										    		break;
	  								    		}
	  							}
	  			    	
	  			    }
	  		        System.out.println("cvv_req -------------------- " + cvv_req);   
	  			    
	  			  
	  			    if(cvv_req.equals("N"))
	  			    {
	  			    	// insert into pinoffset in db
	  			    	String date_query = "(select to_date(trunc(sysdate)) from dual)";
	  			    	query= "INSERT INTO IFPIN_MASTER (INST_ID, CARD_NO, PIN_OFFSET, CVV1, CVV2, PVV, PIN_DATE) " +
	  			    			"VALUES ('"+instname.trim()+"','"+ chn.trim() +"','"+pin_offset.trim() +"','N','N','"+pin_offset.trim()+"',"+date_query+")"; 
	  			        System.out.println("query for insertion -------------------- " + query); 
	  			        
	  			        String cardstatus_upadate_query="update IFPCARD_PRODUCTION set CARD_STATUS='"+Card_Status+"',MKCK_STATUS='M' where INST_ID='"+instname.trim()+"' and CARD_NO='"+ chn.trim()+"'";
	  			        try
	  				        {
	  							        JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
	  							        int re=jdbcTemplate.update(query);
	  							        if(re!=0)
	  									        {
	  									        		int res=jdbcTemplate.update(cardstatus_upadate_query);
	  											        System.out.println("card ststus upadet in  IFPCARD_PRODUCTION ==========="+res );
	  											        txManager.commit(status);
	  									        }
	  							        
	  				        }
	  			        catch (Exception e)
	  			        {
	  			        	pin_bean.setPin_proc_status("03");
	  			        	txManager.rollback(status);
	  				    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  			    		// System.out.println(" response Message ::::::::::::::::  " + ResponseCheck.respCheck(resp_code));
	  					    break;	
	  					}
	  			    	
	  			    }
	  			    
	  			    if(cvv_req.equals("Y"))
	  					 {
	  					    	
	  					    	String expiry_date=getChnExpiryDate(chn,datasource,instname,"cvv1");
	  					    	command=Header+"CW"+cvk_pair+chn+cvv_delimiter+expiry_date+service_code;	
	  						    System.out.println("command for cvv1 is ((((((((((((((((( "+ command);
	  						    
	  						    hsmresponse=writeCommandToHSM(command);
	  					    	
	  					    	hsmheader_value=getheaderValue(hsmresponse,pin_bean);
	  					        resp_code=checkHSMresponse(hsmresponse,command,pin_bean);
	  					        System.out.println("Header from HSM response:::::::::" + hsmheader_value);   
	  							if(!(hsmheader_value.equals(Header)))
	  							    {
	  							    		
	  							    		System.out.println("HSM header does'nt match ");
	  							    		pin_bean.setPin_proc_status("01");
	  							    		if(!(resp_code.equals("00")))
	  										{
	  								    		pin_bean.setPin_proc_status(resp_code);
	  									    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  								    		// System.out.println(" response Message ::::::::::::::::  " + ResponseCheck.respCheck(resp_code));
	  										    break;
	  								    	}
	  							    		break;
	  							    }
	  							
	  							
	  							cvv1=hsmresponse.substring( headerlen+4 ,headerlen+4+3 );// 4 2-recp_command 2-resp_code  3 for cvv length
	  							  System.out.println(" cvv1  +++++++++++ "+cvv1);
	  							expiry_date=getChnExpiryDate(chn,datasource,instname,"cvv2");
	  					    	command=Header+"CW"+cvk_pair+chn+cvv_delimiter+expiry_date+service_code;	
	  						    System.out.println("command for cvv1 is ((((((((((((((((( "+ command);
	  						    
	  						    hsmresponse=writeCommandToHSM(command);
	  					    	
	  					    	hsmheader_value=getheaderValue(hsmresponse,pin_bean);
	  					        resp_code=checkHSMresponse(hsmresponse,command,pin_bean);
	  					        System.out.println("Header from HSM response:::::::::" + hsmheader_value);   
	  							if(!(hsmheader_value.equals(Header)))
	  							    {
	  							    		
	  							    		System.out.println("HSM header does'nt match ");
	  							    		pin_bean.setPin_proc_status("01");
	  							    		if(!(resp_code.equals("00")))
	  										{
	  								    		pin_bean.setPin_proc_status(resp_code);
	  									    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  								    		// System.out.println(" response Message ::::::::::::::::  " + ResponseCheck.respCheck(resp_code));
	  										    break;
	  								    	}
	  							    		break;
	  							    }
	  							cvv2=hsmresponse.substring( headerlen+4 ,headerlen+4+3 );// 4 2-recp_command 2-resp_code  3 for cvv length
	  						    System.out.println(" cvv2  +++++++++++ "+cvv2);

	  							
	  							String date_query = "(select to_date(trunc(sysdate)) from dual)";
	  					    	query= "INSERT INTO IFPIN_MASTER (INST_ID, CARD_NO, PIN_OFFSET, CVV1, CVV2, PVV, PIN_DATE) " +
	  					    			"VALUES ('"+instname.trim()+"','"+ chn.trim() +"','"+pin_offset.trim() +"','"+cvv1.trim()+"','"+cvv2.trim()+"','"+pin_offset.trim()+"',"+date_query+")"; 
	  					        System.out.println("query for insertion -------------------- " + query); 
	  					      System.out.println(" Card Status Update Is "+Card_Status);
	  					        String cardstatus_upadate_query="update IFPCARD_PRODUCTION set CARD_STATUS='"+Card_Status+"',MKCK_STATUS='M' where INST_ID='"+instname.trim()+"' and CARD_NO='"+ chn.trim()+"'";
	  					        try
	  						        {
	  									        JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
	  									        int re=jdbcTemplate.update(query);
	  									        System.out.println("return for insert query re -------------------- " + re); 

	  									        if(re!=0)
	  											        {
	  											        		int res=jdbcTemplate.update(cardstatus_upadate_query);
	  											        		if(res != 0)
	  											        		{
	  											        			System.out.println("card ststus upadet in  IFPCARD_PRODUCTION ==========="+res );
	  											        			txManager.commit(status);
	  											        			System.out.println("UPdated value after commit#################");
	  											        		}
	  											        		
	  											        }
	  									        
	  						        }
	  					        catch (Exception e)
	  					        {
	  					        	pin_bean.setPin_proc_status("03");
	  					        	txManager.rollback(status);
	  						        System.out.println("Exception "+e ); 
	  						        
	  						    	 // String response_message=ResponseCheck.respCheck(resp_code);
	  					    		// System.out.println(" response Message ::::::::::::::::  " + ResponseCheck.respCheck(resp_code));
	  							    break;	
	  							}
	  			
	  					    }
	  			  
	  		   }
	  		   else
	  			   {
	  				    System.out.println("Error while connecting to HSM ");
	  				   	return "01";
	  			   }   

	  		
	  		
	  	}
	  	catch (Exception e) 
	  	{
			pin_bean.setPin_proc_status("03");
	  		exception.setMessage("Exception occured at thalesVisapingeneration ");
	  		//logger.error("Exception occured at thalesVisapingeneration "+e);
	  		return "Exception";
		}
	  	pin_bean.setPin_proc_status("00");
	  	return "00";
	  }
									 
			*/				    //	pin_validation_data=generatePinValidationData(chn,pan_offset,pan_validation_length,pan_pad_char);
							
		  public String generatePinValidationData(String chn,String pan_offset,String pan_validation_length,String pan_pad_char)
		  
		  {
			  String pin_validation;
			  String pan_pad="";
			  int pan_ofst=Integer.parseInt(pan_offset);
			  int int_pan_validation_length=Integer.parseInt(pan_validation_length);
			  int chn_len=chn.length();
			  String chn_check_digit=chn.substring(chn_len-1,chn_len);
			  String pin_validation_data=null;
			  if(int_pan_validation_length==10)
					  {
						  pin_validation=chn.substring(pan_ofst,(pan_ofst+10));
						  pin_validation_data=pin_validation+"N"+chn_check_digit;
						  System.out.println("pin_validation_data when length=10   ---------"+pin_validation_data);
						  return pin_validation_data;
					  }
			  if(int_pan_validation_length<10)
					  {
						  for(int i=0;i<(10-int_pan_validation_length);i++)
								  {
									  pan_pad=pan_pad+pan_pad_char;
								  }
						  pin_validation=chn.substring(pan_ofst,(pan_ofst+int_pan_validation_length));
						  pin_validation_data=pin_validation+pan_pad+"N"+chn_check_digit;
						  System.out.println("pin_validation_data when length<10   ---------"+pin_validation_data);
			
						  return pin_validation_data;
					  }
			  if(int_pan_validation_length>10)
					  {
						  pin_validation_data=chn.substring(0,10)+"N"+chn_check_digit;
						  System.out.println("pin_validation_data when length>10   ---------"+pin_validation_data);
						  return pin_validation_data;
					  }
			  return pin_validation_data;
			  
		  }
								  

							
public String getChnExpiryDate(String tablename,String chn, DataSource dataSource, String instname,String cvv_type)
{
	System.out.println( "chn expiry date function called " );
	JdbcTemplate jdbcTemplate= new JdbcTemplate(dataSource);   
   
    String expiry_date_query = null;
    
    if(cvv_type.equals("cvv1"))
    	expiry_date_query="select to_char(EXPIRY_DATE,'mmyy') from "+tablename+" where INST_ID='"+instname+"' and CARD_NO='"+chn+"'"; 
    else
    	expiry_date_query="select to_char(EXPIRY_DATE,'yymm') from "+tablename+" where INST_ID='"+instname+"' and CARD_NO='"+chn+"'"; 
    
    //System.out.println( "expiry_date_query" + expiry_date_query);
    
    String expiry_date= (String)jdbcTemplate.queryForObject(expiry_date_query, String.class); 
   // System.out.println("expiry_date +++++++++++ "+expiry_date);
    
    return expiry_date;	
}
							
						
public String getPrintFieldData(String pinmailer_id,DataSource dataSource,String instid,String chnno, String bin, String ordertype)
{
		String command,query;
		List field_name_list;
		ArrayList<String> fieldname_array=new ArrayList<>();
		int len;
		
		query ="select FIELD_NAME from PINMAILER_PROPERTY where PINMAILER_ID='"+pinmailer_id+"' and INST_ID='"+instid+"' and PRINT_REQUIRED ='Y' order by X_POS";
	System.out.println(" query ================= "+query);
	try
	{
		JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
		field_name_list=jdbcTemplate.queryForList(query);
		Iterator itr = field_name_list.iterator();
		if(field_name_list.isEmpty())
		{
			return "ME";
		}
		while(itr.hasNext())
		{
			Map map = (Map)itr.next();
			fieldname_array.add((String)map.get("FIELD_NAME"));
		}
		System.out.println(" Fields  ================= "+fieldname_array);
		len=fieldname_array.size();
		String sub_query="",field_data="",value="";
		for(int i=0;i<len;i++)
		{
				if(fieldname_array.get(i).equals("PINNO"))
				{
					System.out.println("____________This in Pin no field________");
					continue;
				}
				if(fieldname_array.get(i).equals("PRDNAME"))
				{ 
					sub_query="select PRODUCT_NAME from IFP_INSTPROD_DETAILS where INST_ID='"+instid+"' and BIN='"+bin+"' and trim(CARDTYPE_ID)=(select trim(CARD_TYPE) from IFPCARD_ORDER_DETAILS where  inst_id='"+instid+"')";
					System.out.println("query for PRODUCT_NAME___________________"+sub_query);
					value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					System.out.println("value for PRODUCT_NAME___________________"+value);
				}
				String fname,mname,lname,name=null;
				if(fieldname_array.get(i).equals("CUSTNAME"))
				{
					if ( ordertype.equals("I")){
						sub_query="select EMB_NAME  from IFP_TEMP_CONFIG where BIN='"+bin+"'";
						value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					}else{
						System.out.println("query for CUSTNAME___________________"+sub_query);
						List name_list=jdbcTemplate.queryForList(sub_query);
						itr = name_list.iterator();
						while(itr.hasNext())
						{
							Map map = (Map)itr.next();
							fname=((String)map.get("FNAME"));
							fname=((String)map.get("LNAME"));
							mname=((String)map.get("MNAME"));
							if(mname.equals("NO_DATA"))
							{
								name=fname+fname;
							}
							else 
							value=fname+mname+fname;
						}
						System.out.println("value for CUSTNAME___________________"+value);
					}
				}
				if(fieldname_array.get(i).equals("CARDNO"))
				{
					value=chnno;
				}
				if(fieldname_array.get(i).equals("BRNAME"))
				{
					String tablename = null;
					if ( ordertype.equals("I")){
						tablename = "INST_CARD_PROCESS";
						
					}else{
						 tablename = "PERS_CARD_PROCESS";
					}
					sub_query="select BRANCH_CODE from "+tablename+" where CARD_NO='"+chnno+"' and inst_id='"+instid+"'";
					value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					System.out.println("query for BRNAME___________________"+sub_query);
					//value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					System.out.println("value for BRNAME___________________"+value);
				}
				if(fieldname_array.get(i).equals("ADDRESSONE"))
				{
					if ( ordertype.equals("I")){
						sub_query="select MAIL_ADDS1  from IFP_TEMP_CONFIG where BIN='"+bin+"'";
						
					}else{
						sub_query="select substr(POSTAL_ADDRESS,1,instr(POSTAL_ADDRESS,'|',1,1)-1) as ADDRESS from IFPCUSTOMER_INFO where ORDER_REF_NO='"+chnno+"'";
					}
					
					System.out.println("query for ADREES ONE___________________"+sub_query);
					value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					System.out.println("value for ADREES ONE___________________"+value);
				}
				if(fieldname_array.get(i).equals("ADDRESSTWO"))
				{
					if ( ordertype.equals("I")){
						sub_query="select MAIL_ADDS2  from IFP_TEMP_CONFIG where BIN='"+bin+"'";
						
					}else{
						sub_query="select substr(POSTAL_ADDRESS,instr(POSTAL_ADDRESS,'|',1,1)+1,instr(POSTAL_ADDRESS,'|',1,2)-instr(POSTAL_ADDRESS,'|',1,1)-1) as ADDRESS from IFPCUSTOMER_INFO where ORDER_REF_NO='"+chnno+"'";
					}
					
					System.out.println("query for ADDRESSTWO___________________"+sub_query);
					value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					System.out.println("value for ADDRESSTWO___________________"+value);
				}
				if(fieldname_array.get(i).equals("ADDRESSTHREE"))
				{
					if ( ordertype.equals("I")){
						sub_query="select MAIL_ADDS3  from IFP_TEMP_CONFIG where BIN='"+bin+"'"; 
					}else{
					sub_query="select substr(POSTAL_ADDRESS,instr(POSTAL_ADDRESS,'|',1,2)+1,instr(POSTAL_ADDRESS,'|',1,3)-instr(POSTAL_ADDRESS,'|',1,2)-1) as ADDRESS from IFPCUSTOMER_INFO where ORDER_REF_NO='"+chnno+"'";
					}
					
					System.out.println("query for ADDRESSTHREE___________________"+sub_query);
					value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					System.out.println("value for ADDRESSTHREE___________________"+value);
				}
				if(fieldname_array.get(i).equals("ADDRESSFOUR"))
				{
					if ( ordertype.equals("I")){
						sub_query="select MAIL_ADDS4  from IFP_TEMP_CONFIG where BIN='"+bin+"'"; 
					}else{
						sub_query="select substr(POSTAL_ADDRESS,instr(POSTAL_ADDRESS,'|',1,2)+1,instr(POSTAL_ADDRESS,'|',1,3)-instr(POSTAL_ADDRESS,'|',1,2)-1) as ADDRESS from IFPCUSTOMER_INFO where ORDER_REF_NO='"+chnno+"'";
					} 
					
					System.out.println("query for ADDRESSFOUR___________________"+sub_query);
					value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
					System.out.println("value for ADDRESSFOUR___________________"+value);
				}
				 
				if(fieldname_array.get(i).equals("CUSTID"))
				{
					if ( ordertype.equals("I")){
						value = "";
					}else{
						sub_query = "select cin from PERS_CARD_PROCESS where inst_id='"+instid+"' and card_no='"+chnno+"'";

					} 
					value=(String)jdbcTemplate.queryForObject(sub_query , String.class);					 
					System.out.println("value for CUSTID___________________"+value);
				}
				
				if(fieldname_array.get(i).equals("PHNO"))
				{
					if ( !ordertype.equals("I")){
						sub_query="select PHONE_NO from IFP_CUSTINFO_PROCESS where cin =(select cin from PERS_CARD_PROCESS where inst_id='"+instid+"' and card_no='"+chnno+"')";											
						System.out.println("query for PHNO___________________"+sub_query);
						value=(String)jdbcTemplate.queryForObject(sub_query , String.class);
						System.out.println("value for PHNO___________________"+value);
					}
					
				}
				field_data=field_data+value+";";
				System.out.println("mail data  ___________________"+field_data.trim());
		}
		return field_data.trim();
	}
	catch (Exception e) 
	{
		System.out.println(" Exception in getPrintFieldData "+e.getMessage());
		return "mailererr";
	}
	
}




public String getheaderValue(String Command,int headerlen)
	{
		String headervalue;
		//int headerlen=hsmparam.HEADERLEN;
		headervalue=Command.substring(0,headerlen);
	   	return headervalue;
	}
	public String writeCommandToHSM(String command, String hsmheaderType,DataInputStream in,DataOutputStream out)throws Exception
	{
		String msgLength ="0";
		String hsmresponse="RE";
		try
		{
			String hsmheader_Type=hsmheaderType.toString().trim();
			System.out.println(" hsmheaderType Is ====> "+hsmheader_Type);
			int commandLength=command.length();
			 
			System.out.println(" HSM command Length is =====> "+commandLength);
			if(hsmheader_Type.equals("ASCII".toString().trim()))
			{
				byte[] byte_len=new byte[4];
				String temp_resp="";
				msgLength = Integer.toString(commandLength);
				System.out.println("The Message Length in ===== IN ASCII ====> "+msgLength);
				String HSMCommand = "00"+msgLength+command;
				System.out.println("Input to HSM : " +HSMCommand);
				out.writeUTF(HSMCommand);
			    out.flush();
			    System.out.println("msg sent sucessully"); 
			    hsmresponse=in.readUTF();
			    System.out.println( "ASCII response value is " + hsmresponse); 
			}
			if(hsmheader_Type.equals("HEX".toString().trim()))
			{
			
				msgLength = Integer.toHexString(commandLength );
				System.out.println("The Message Length in ===== IN HEX ====> "+msgLength);
				byte[] byte_len=new byte[4];
				String temp_resp="";
				String msg_length="";
				String HSMCommand = command;//msgLength+command;
				System.out.println("Input to HSM : " +HSMCommand);
				out.writeUTF(HSMCommand);
			    out.flush();
			    System.out.println("msg sent sucessully"); 
			    hsmresponse =in.readUTF(); 
			    System.out.println( "HEX response value is " + hsmresponse); 
			}
			 }
		    catch (Exception e) 
		    {
		    	System.out.println("Exceptiopj in WriteCommand Function is ========>>>>> "+e);
		    	hsmresponse = hsmresponse+"~"+e.getMessage();
		    	e.printStackTrace();
			}
		  
	    
	    return hsmresponse;
	}
	public String checkHSMresponse(String hsm_resp,String hsm_req,int headerlen)
	{
		System.out.println(" Header Leght Receied is ===> "+headerlen);
		//int headerlen=pin_bean.getHeader_len();
		String hsmresp_command = hsm_resp.substring(headerlen, headerlen+2);
		String hsmreq_command=hsm_req.substring(headerlen, headerlen+2);
	    int resp_diff=hsmresp_command.compareTo(hsmreq_command);
	    String respcode=hsm_resp.substring(headerlen+2, headerlen+4);
	    //System.out.println(" The Response is "+respcode);
	    if((resp_diff== 1))
		    {
		       	System.out.println("Response from Hsm  matched");
				return respcode;
		    }	
	    else
		    {
    		System.out.println("Response from Hsm Doesnot matched");
    		return "CM";
		    }
	}
	
	public String[] gettingcardsTO_pingen(String Refnum,String instname,int count,DataSource datasource)
	{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
		String cards_query = "select trim(card_no) CARDNO from IFPCARD_PRODUCTION where inst_id='"+instname+"' and REFNUM ='"+Refnum+"'AND CARD_STATUS='P' order by card_no";
		String[] card_nos = new String[count+1];
		try{
					List allcards = jdbcTemplate.queryForList(cards_query);
					System.out.println("List of cards "+allcards);
					int i = 0;
					if(!(allcards.isEmpty()))
					{
						System.out.println("Hsm Details returns Value" +allcards);
						Iterator itr = allcards.iterator();
						while(itr.hasNext())
						{
							Map map = (Map)itr.next();
							card_nos[i] = ((String)map.get("CARDNO"));
							i = i + 1;
						}
					}
					else{
						card_nos[0]="NOCARD";
					}
			}
		catch (Exception e) {
			System.out.println("Exception while seleting card no"+e.getMessage());
			System.out.println("Exception while seleting card no"+e);
			card_nos[0]="NOCARD";
			//return card_nos;
		}
		return card_nos;
	}
	
	public String getLoadFormatData(String pinmailer_id,DataSource datasource,String  instname)
			{
				String sep=">";
				String load_format_data="";
				String query;
				ArrayList<String> fieldname_array = new ArrayList<String>();
				ArrayList<String> field_len_array = new ArrayList<String>(); 
				ArrayList<String> x_pos_array = new ArrayList<String>();
				ArrayList<String> y_pos_array = new ArrayList<String>(); 
				//BasicConfigurator.configure();
				List field_name_list=null;
				int current_row=0;
				query ="select FIELD_NAME,FIELD_LENGTH,X_POS,Y_POS from PINMAILER_PROPERTY where PINMAILER_ID='"+pinmailer_id+"' and INST_ID='"+instname+"' and PRINT_REQUIRED ='Y' order by X_POS";
				System.out.println(" query ================= "+query);
				try
				{
						JdbcTemplate jdbcTemplate=new JdbcTemplate(datasource);
						field_name_list=jdbcTemplate.queryForList(query);
						Iterator itr = field_name_list.iterator();
						while(itr.hasNext())
						{
							Map map = (Map)itr.next();
							fieldname_array.add((String)map.get("FIELD_NAME"));
							field_len_array.add((String)map.get("FIELD_LENGTH"));
							x_pos_array.add((String)map.get("X_POS"));
							y_pos_array.add((String)map.get("Y_POS"));
						}
				}
				catch (Exception e) {
					//BasicConfigurator.configure();
					//logger.error("Error while Forming getLoadFormatData "+e);
					return "Exception";
				}
				System.out.println("FIELD_NAME ARRAY ================= "+fieldname_array);
				System.out.println("FIELD_NAME LENGTH================= "+fieldname_array.size());
				int fieldname_array_len=fieldname_array.size();
				String ypos,y_pos;
				int k=0;
				for(int i=0;i<fieldname_array_len;i++)
				{
					
					String current_L_string=sep;
					int x_p=Integer.parseInt(x_pos_array.get(i));
					ypos=y_pos_array.get(i);
					y_pos=checkPos(ypos);
					System.out.println("FIELD_NAME =================  "+fieldname_array.get(i));
					for(int j=0;j<(x_p-current_row);j++)
						{
							current_L_string=current_L_string+"L"+sep;
						}
					if(fieldname_array.get(i).equals("PINNO"))
						{
							current_L_string=current_L_string+y_pos+"^"+"P";
																			
						}
					else
						{
						current_L_string=current_L_string+y_pos+"^"+k;
						k++;
						}
					current_row=x_p;
					load_format_data=load_format_data+current_L_string;
				}
				
				return load_format_data;
			}
	
	public String checkPos(String position)
	{
		String pos_y=null;
		int len=position.length();
		switch(len)
			{
					case 1: System.out.println("LENGTH IS 1 digit   -----"+position);
							pos_y="00"+position;
							return pos_y;
					case 2:System.out.println("LENGTH IS 2 digit   -----"+position);
							pos_y="0"+position;
							return pos_y;
					case 3:System.out.println("LENGTH IS 3 digit   -----"+position);
						   pos_y=position;
						   return pos_y;
					default: return position;
			}
					
		
	}
							
							
}
