package com.ifp.iso;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


import com.ifp.Action.BaseAction;
import com.ifp.iso.IsoFileGenDAO;
import com.ifp.iso.ServiceBeans;

public class IsoService extends BaseAction {
	 
	public JSONObject sendingISOMessage( String instid, String ifptxncode, ServiceBeans servicebean,   
			JdbcTemplate jdbctemplate,  Socket connect_id,IsoFileGenDAO isodao  ) throws Exception{ 
		 
		System.out.println("/************************ 	ISO COMMUNICATION BEGINS	******************************************************");
		 
		DataOutputStream out = null;
		DataInputStream in = null; 
		JSONObject jsonresp = new JSONObject();  
		String PROCESSCODE = ifptxncode;  
		try {   
			isodao.log("/************************ 	ISO COMMUNICATION BEGINS JOHN ["+PROCESSCODE+"]	******************************************************");
			
			URL urlfilename = getClass().getResource("basic.xml");
 			File isoxmlfile = new File(urlfilename.getPath());
 			String absoluteDiskPath = isoxmlfile.getAbsolutePath();
 			System.out.println("absoluteDiskPath :"+absoluteDiskPath);
			//Properties prop = getCommonDescProperty(); 
			//String absoluteDiskPath =prop.getProperty("iso.rootfile");  
			
			GenericPackager packager = new GenericPackager( absoluteDiskPath );  
			ISOMsg isomsg = new ISOMsg();
			isomsg.setPackager(packager);   
			 
			String datetime = "";
			String isomapkey = isodao.getIsoMapKey(instid, servicebean.getMti(), PROCESSCODE, jdbctemplate);
			isodao.log("Getting isomapkey ..got : " + isomapkey);
			if( isomapkey == null ){
				jsonresp.put("INTERRESP", 1);
		    	jsonresp.put("INTERRESPVAL", "No iso Mapping Key Configured");
		    	isodao.log("No key value pair configured for [ "+PROCESSCODE+" ] table [ IFP_ISO8583_CONFIG ]  ");
		    	return jsonresp;
			}
			Map isokeyvalmap = isodao.getIsoFieldSet(instid, isomapkey, jdbctemplate );
			
			if(  isokeyvalmap != null ){
				String[] keys =  (String[])( isokeyvalmap.keySet().toArray( new String[isokeyvalmap.size()] ) );
				int error = 0; 
				System.out.println("CONFIGURED ISO LENGTH--"+keys.length);
				
				for( int i=0; i<keys.length; i++ ){
					String isovalue = (String) isokeyvalmap.get(keys[i]); 
					int isokey = Integer.parseInt(keys[i]);
					
					switch( isovalue ){
						case "MTI" :
							String mti = servicebean.getMti();
							System.out.println( "isovalue_" + isovalue );
							isomsg.setMTI(mti);
							continue;
						
						case "INSTID" :
							System.out.println( "isovalue_" + isovalue ); 
							isomsg.set(isokey, instid);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +instid);
							isodao.log("KEY : " + isokey + "-VALUE :" +instid);
							continue;
						
						case "CARDNO" :
							System.out.println( "isovalue_" + isovalue ); 
							String cardno = servicebean.getCardno();
							isomsg.set(isokey, cardno);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +cardno);
							isodao.log("KEY : " + isokey + "-VALUE :" +cardno);
							continue;
							
						case "FROMCARD" :
							System.out.println( "isovalue_" + isovalue ); 
							String fromcardnumber = servicebean.getFromcardno();
							isomsg.set(isokey, fromcardnumber);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +fromcardnumber);
							isodao.log("KEY : " + isokey + "-VALUE :" +fromcardnumber);
							continue;
						
						case "TOCARDNO" :
							System.out.println( "isovalue_" + isovalue ); 
							String tocardnumber = servicebean.getTocardno();
							isomsg.set(isokey, tocardnumber);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +tocardnumber);
							isodao.log("KEY : " + isokey + "-VALUE :" +tocardnumber);
							continue;
						
						case "EXPDATE" :
							System.out.println( "isovalue_" + isovalue ); 
							String expdate = servicebean.getExpdate();
							isomsg.set(isokey, expdate);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +expdate);
							isodao.log("KEY : " + isokey + "-VALUE :" +expdate);
							continue;
							
						case "TXNCODE" : 
							//String txncode = servicebean.getTxncode();
							System.out.println( "isovalue_" + isovalue );
							String txncode =  servicebean.getTxncode();
							String orgtxncode =null;
							try{
								orgtxncode = Integer.toString(  Integer.parseInt(txncode) );
							}catch(NumberFormatException ne){
								
								orgtxncode = this.getTxcCodeByAction(instid, txncode, jdbctemplate) ;
							}
							isodao.log("Orginal txn code : " + orgtxncode );
							
							
							isomsg.set(isokey, orgtxncode);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +orgtxncode);
							isodao.log("KEY : " + isokey + "-VALUE :" +orgtxncode);
							continue;
						 
						case "TRANDATETIME" : 
							datetime = isodao.getLocaldatetime("DT");
							if(datetime != null){
								isomsg.set(isokey, datetime);
							}else{	System.out.println("Tran Date Time Received as null");		error = 1;	} 
							System.out.println("SET KEY : " + isokey + "-VALUE :" +datetime);
							isodao.log("KEY : " + isokey + "-VALUE :" +datetime);
							continue;
							
						case  "TRANNO" :
							System.out.println( "isovalue_" + isovalue );
							String traceno = servicebean.getTraceno();
							isomsg.set(isokey, traceno); 
							System.out.println("SET KEY : " + isokey + "-VALUE :" +traceno);
							isodao.log("KEY : " + isokey + "-VALUE :" +traceno);
							continue;
							
						case "MCC" :
							System.out.println( "isovalue_" + isovalue );
							
							String mcc = servicebean.getMcc();
							isomsg.set(isokey, mcc); 
							System.out.println("SET KEY : " + isokey + "-VALUE :" +mcc);
							isodao.log("KEY : " + isokey + "-VALUE :" +mcc);
							continue;
							
						case  "LOCALDATE" :
							System.out.println( "isovalue_" + isovalue );
							
							datetime = isodao.getLocaldatetime("D");
							if(datetime != null){
								isomsg.set(isokey, datetime);
							}else{
								System.out.println("Local Date Time Received as null");
								error = 1;
							}
							System.out.println("SET KEY : " + isokey + "-VALUE :" +datetime);
							isodao.log("KEY : " + isokey + "-VALUE :" +datetime);
							continue;
							
						case   "LOCALTIME" :
							System.out.println( "isovalue_" + isovalue );
							
							datetime = isodao.getLocaldatetime("T");
							if(datetime != null){
								isomsg.set(isokey, datetime);
							}else{
								System.out.println("Local Time Received as null");
								error = 1;
							}
							System.out.println("SET KEY : " + isokey + "-VALUE :" +datetime);
							isodao.log("KEY : " + isokey + "-VALUE :" +datetime);
							continue;
							
						case "REFNO" :  
							System.out.println( "isovalue_" + isovalue); 
							String refno = servicebean.getRefereceno();
							isomsg.set(isokey, refno);
							System.out.println("SET KEY : " + isokey + " -VALUE :" +refno);
							isodao.log("KEY : " + isokey + "-VALUE :" +refno);
							continue; 
						
						case "MERCHANTID":
							System.out.println( "isovalue_" + isovalue); 
							String merchantid = servicebean.getMerchantid();
							isomsg.set(isokey, merchantid);
							System.out.println("SET KEY : " + isokey + " -VALUE :" +merchantid);
							isodao.log("KEY : " + isokey + "-VALUE :" +merchantid);
							continue; 
							
						case   "TERMINAL" :
							System.out.println( "isovalue_" + isovalue ); 
							String terminalid = servicebean.getTerminalid();
							isomsg.set(isokey, terminalid);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +terminalid);
							isodao.log("KEY : " + isokey + "-VALUE :" +terminalid);
							continue;
							
						case   "DEVICETYPE" :
							System.out.println( "isovalue_" + isovalue ); 
							String devicetype = servicebean.getDevicetype();
							isomsg.set(isokey, devicetype);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +devicetype);
							isodao.log("KEY : " + isokey + "-VALUE :" +devicetype);
							continue;
							
						case   "IPADDRESS" :
							System.out.println( "isovalue_" + isovalue );  
							String ipaddress = servicebean.getIpaddress();
							isomsg.set(isokey, ipaddress);
							System.out.println("SET KEY : " + isokey + " -VALUE :" +ipaddress);
							isodao.log("KEY : " + isokey + "-VALUE :" +ipaddress);
							continue;
							
						case  "LOCATION" :
							System.out.println( "isovalue_" + isovalue ); 
							String location = servicebean.getTerminallocation();
							isomsg.set(isokey, location);
							System.out.println("SET KEY : " + isokey + " -VALUE :" +location);
							isodao.log("KEY : " + isokey + "-VALUE :" +location);
							continue;
							
						case   "BASECCY"  :
							System.out.println( "isovalue_" + isovalue);
							String basecurrency = servicebean.getBasecurrency();
							isomsg.set(isokey, basecurrency);
							System.out.println("SET KEY : " + isokey + " -VALUE :" +basecurrency);
							isodao.log("KEY : " + isokey + "-VALUE :" +basecurrency);
							continue;
							
						case   "CVV2"  :
							String cardcvv2 = servicebean.getCvv2();
							System.out.println( "isovalue_" + isovalue); 
							isomsg.set(isokey, cardcvv2);
							System.out.println("SET KEY : " + isokey + " -VALUE :" +cardcvv2);
							isodao.log("KEY : " + isokey + "-VALUE :" +cardcvv2);
							continue;
							
						case   "TXNAMOUNT"  :
							String txnamount = servicebean.getTxnamount()+"00";
							System.out.println( "isovalue_" + isovalue); 
							isomsg.set(isokey, txnamount);
							System.out.println("SET KEY : " + isokey + " -VALUE :" +txnamount);
							isodao.log("KEY : " + isokey + "-VALUE :" +txnamount);
							continue;
						
						case "MOBILENO" :
							String mobileno = servicebean.getMobileno();
							System.out.println( "isovalue_" + isovalue );  
							isomsg.set(isokey, mobileno);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +mobileno);
							isodao.log("KEY : " + isokey + "-VALUE :" +mobileno);
							continue;
							
						case "TXNDESC" :
							String txndesc = servicebean.getTxndesc();
							System.out.println( "isovalue_" + isovalue );  
							isomsg.set(isokey, txndesc);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +txndesc);
							isodao.log("KEY : " + isokey + "-VALUE :" +txndesc);
							continue;
						
						/*case "LUKUMETERNO" :
							String meternumber = servicebean.getLukumeternumber();
							System.out.println( "isovalue_" + isovalue );  
							isomsg.set(isokey, meternumber);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +meternumber);
							isodao.log("KEY : " + isokey + "-VALUE :" +meternumber);
							continue;*/
							
						case "TOENTITY" :
							String toentity = servicebean.getToentity();
							System.out.println( "isovalue_" + isovalue );  
							isomsg.set(isokey, toentity);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +toentity);
							isodao.log("KEY : " + isokey + "-VALUE :" +toentity);
							continue;
						
						case "PINBLOCK" :
							String pinblock = servicebean.getPinblock();
							System.out.println( "isovalue_" + isovalue );  
							isomsg.set(isokey, pinblock);
							System.out.println("SET KEY : " + isokey + "-VALUE :" +pinblock);
							isodao.log("KEY : " + isokey + "-VALUE :" +pinblock);
							continue;
					}
					
					
					 
				} 
			} else{
				jsonresp.put("INTERRESP", 1);
		    	jsonresp.put("INTERRESPVAL", "No iso key and value configured.");
		    	isodao.log("No key value pair configured for [ "+PROCESSCODE+" ] table [ IFP_ISO8583_MAP ]  ");
		    	return jsonresp;
			}
			
		 
			 
			
			 System.out.println("Message is : " + isomsg ); 
			  
			 out = new DataOutputStream (connect_id.getOutputStream());
			 byte[] data = isomsg.pack();
			 String isomsg_str = new String(data);
			 out.writeUTF(isomsg_str);
			 System.out.println("MESSAGE SEND SUCCESFULLY." + isomsg_str);
			 isodao.log("MESSAGE SEND SUCCESFULLY." + isomsg_str);
			 System.out.println("WAITING FOR RESPONSE...........");
			 in = new DataInputStream (connect_id.getInputStream());
			 
			
			 out.flush();
			 //System.out.println("Recieved ....." + in);
			 String responsemsg = in.readUTF();    
			 isodao.log("RESPONSE RECEIVED SUCCESFULLY." + responsemsg);
			 isomsg.unpack(responsemsg.getBytes()); 
			 
			 
			 /*System.out.println("data__" + data );
			 System.out.println("isomsg__" + isomsg );
			 System.out.println("isomsg_str_" + isomsg_str );
			 */
			 isodao.printResponse(isomsg);
			 isodao.log("Has 39 checking....." + isomsg.hasField(39) );
			 if( isomsg.hasField(39) ) {
				 isodao.log("RESPONSE RECEIVED SUCCESFULLY." + isomsg.getString(39));
			 }
			 //System.out.println("RESPONSE RECEIVED SUCCESFULLY. jo" + responsemsg); 
			 
			String resp = isodao.responseISO(isomsg);
			isodao.log( "response : " + resp );  
			String respdesc = isodao.getReponseDescription(resp, jdbctemplate);
			  isodao.log("Response :" + respdesc); 
			  if( Integer.parseInt( isomsg.getString(39) ) == 0 ){
				jsonresp.put("INTERRESP", 0);   
				jsonresp.put("INTERRESPVAL",respdesc);
				
				jsonresp.put("RESPREASON",respdesc);
		    	System.out.println( "Response code is " + resp);
				jsonresp.put("HOSTRESPCODE", resp); 
			}else{
				jsonresp.put("INTERRESP", 1); 
		    	jsonresp.put("INTERRESPVAL",respdesc);
			}   
			 
			isodao.log("jsonresp :" + jsonresp );
			return jsonresp;
		}	catch (Exception e) { 
			System.out.println("EXECPTION : " +e.getMessage());
			e.printStackTrace();
			isodao.log("EXECPTION : " +e.getMessage());
			trace("EXECPTION : " +e.getMessage());
			 
			jsonresp.put("INTERRESP", 1);
	    	jsonresp.put("INTERRESPVAL", "Could not continue the process");
	    	jsonresp.put("TECHREASON", e.getMessage()); 
		}finally{
			if( out != null ){ out.close(); }
			if( in != null ){ in.close(); }
			isodao = null;
		}
		isodao.log("Final Response : "+ jsonresp );
		System.out.println("/**************************** 	ISO COMMUNICATION END	*********************************************************");
		return jsonresp;
	}
	
	
	 


	public String getTxcCodeByAction( String instid, String actioncode, JdbcTemplate jdbctemplate ) throws Exception{
		String txncode = null;
		
		try {
			String statusqry ="SELECT TXN_CODE FROM IFACTIONCODES WHERE INST_ID='"+instid+"' AND ( ACTION_CODE='"+actioncode+"' OR TXN_CODE='"+actioncode+"'  )";
			enctrace ("statusqry :" + statusqry );
			txncode = (String)jdbctemplate.queryForObject(statusqry,String.class);
		} catch (EmptyResultDataAccessException e) {  
			e.printStackTrace();
		}
		return  txncode ;
	}
	
	public String getTxcCodeByAction( String instid, String actioncode, Connection con ) throws Exception{
		String txncode = null;
		ResultSet rs = null;
		PreparedStatement prp = null;
		try {
			String statusqry ="SELECT TXN_CODE FROM IFACTIONCODES WHERE INST_ID='"+instid+"' AND ( ACTION_CODE='"+actioncode+"' OR TXN_CODE='"+actioncode+"'  )";
			enctrace ("statusqry :" + statusqry );
			prp = con.prepareStatement(statusqry);
			rs = prp.executeQuery();
			if( !rs.next() ){
				return txncode;
			}else{
				do{
					txncode = rs.getString("TXN_CODE");
				}while( rs.next() );
			}
		} catch (EmptyResultDataAccessException e) {  
			e.printStackTrace();
		}
		return  txncode ;
	}
	
	
}
